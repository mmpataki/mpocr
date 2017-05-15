/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpocr;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;

/**
 *
 * @author mmp
 */
public class NeuralNetwork implements INeuralNetwork, Serializable {

    private double learningRate;
    private double cost, totalError;
    private int epochSize;
    private double trainingAccuracy;
    private double maxError = 0.0;
    private int currentIteration;
    private int maxIterations = Integer.MAX_VALUE;
    private ArrayList<Layer> layers;
    private transient CallBack trainingCallBack;

    /**
     * To create a neural network only first and last params are enough Others
     * can be null.
     *
     * @param neuronCounts: numeber of neurons in each layer.
     * @param afunc: Activation function used for the network.
     * @param learningRate: learningRate of network.
     * @param epochSize : number of iterations before each GD. (for SGD)
     * @throws NeuralNetworkException on invalid weight and bias matrix size.
     */
    public NeuralNetwork(
            Vector<Integer> neuronCounts,
            ActivationFunction afunc,
            double learningRate,
            int epochSize
    ) throws NeuralNetworkException {

        layers = new ArrayList<>();
        if (afunc == null || neuronCounts == null) {
            throw new NeuralNetworkException(
                    "Activation Function and neuronCounts"
                    + " per layer cannot be null"
            );
        }

        this.learningRate = learningRate;
        this.epochSize = epochSize;

        /* Add an extra layer to it. */
        neuronCounts.add(neuronCounts.get(neuronCounts.size() - 1));

        /* add the layers */
        for (int i = 0; i < neuronCounts.size() - 1; i++) {
            layers.add(
                    new Layer(
                            neuronCounts.get(i),
                            neuronCounts.get(i + 1), afunc, i
                    )
            );
        }
    }

    @Override
    public int layersCount() {
        return layers.size();
    }

    @Override
    public Layer getLayer(int index) {
        if (index < 0 || index >= layers.size()) {
            return null;
        }
        return layers.get(index);
    }

    public int getEpochSize() {
        return epochSize;
    }

    public void setEpochSize(int epochSize) {
        this.epochSize = epochSize;
    }

    public double getCost() {
        return cost;
    }

    public double getNetworkError() {
        return totalError;
    }

    public void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations == 0 ?
                                    Integer.MAX_VALUE :
                                    maxIterations;
    }

    public void setMaxError(double maxError) {
        this.maxError = maxError;
    }

    public void setTrainingCallBack(CallBack callBack) {
        this.trainingCallBack = callBack;
    }
    
    /**
     * Trains the Neural Network with the given TrainingSet.
     *
     * @param set : Trainingset to be used to train.
     */
    public synchronized void train(TrainingSet set) {

        int currentIndex;
        double[] input, expectedOutput;

        currentIndex = 0;
        trainingAccuracy = 0;
        currentIteration = 0;

        do {

            totalError = 0;

            for (TrainingElement element : set.getElements()) {

                input = element.getInputVector();
                expectedOutput = element.getExpectedOutput();

                currentIndex++;

                /* propagate forward */
                fpropagate(input);

                computeLastLayerError(expectedOutput);

                /* Now propagate the error backwards to find the gradient. */
                for (int i = layersCount() - 2; i > 0; i--) {
                    layers.get(i).computeErrors(layers.get(i + 1).getErrors());
                }

                /* gradient-descent only after completion of an epoch. */
                if ((currentIndex % epochSize) == 0) {

                    /* now correct errors */
                    for (int i = layersCount() - 1; i > 0; i--) {
                        layers.get(i).correctErrors(
                                (i == 0) ? input : layers.get(i - 1).getActivations(),
                                (i == 0) ? null : layers.get(i - 1).getWeights(),
                                learningRate,
                                i
                        );
                    }
                }
            }
            trainingCallBack.function(this);
            currentIteration++;
        } while (maxError < totalError && currentIteration < maxIterations);

    }

    public double[] getOutput() {
        return layers.get(layers.size() - 1).getActivations();
    }

    private void computeLastLayerError(double[] output) {
        double err, errSum, diff;
        Layer lastLayer;
        Neuron neuron;

        errSum = 0;
        lastLayer = layers.get(layersCount() - 1);

        for (int i = 0; i < lastLayer.neuronCount(); i++) {
            neuron = lastLayer.getNeuron(i);
            diff = (neuron.getActivation() - output[i]);
            err = diff * neuron.getDiffActivation();
            errSum += (diff * diff);
            neuron.setError(err);
        }
        totalError += errSum / 2;
    }

    public void fpropagate(double[] input) {
        double[][] wts = null;
        double[] ip = input;
        for (int i = 0; i < layersCount(); i++) {
            layers.get(i).process(wts, ip);
            wts = layers.get(i).getWeights();
            ip = layers.get(i).getActivations();
        }
    }

    @Override
    public String toString() {
        return 
                "{" +
                    "\n\tlearningrate: " + learningRate + "," +
                    "\n\tlayers : " + layers.toString() +
                "}";
    }

    public int getOutputSize() {
        return layers.get(layersCount() - 1).neuronCount();
    }

    public double getTrainingAccuracy() {
        return trainingAccuracy;
    }

    public static NeuralNetwork load(String choseFile) throws FileNotFoundException, IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(choseFile));
        NeuralNetwork net = (NeuralNetwork) ois.readObject();
        return net;
    }
    
    public void save(String storePath) throws FileNotFoundException, IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storePath));
        oos.writeObject(this);
    }
    
}
