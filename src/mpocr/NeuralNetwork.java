/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpocr;

import java.util.ArrayList;

/**
 *
 * @author mmp
 */
public class NeuralNetwork implements INeuralNetwork {

    ArrayList<Layer> layers;
    double[][] inputWeights;
    ActivationFunction afunc;
    double learningRate;
    double cost, totalError;
    private int epochSize;
    Callback callBack;
    private double trainingAccuracy;

    /**
     * To create a neural network only first and last params are enough Others
     * can be null.
     *
     * @param neuronCounts: numeber of neurons in each layer.
     * @param weights: weight matrices for each layer. If this is null then the
     * network will randomize it.
     * @param biases: biases of each neuron in each layer. If this is null the
     * network will randomize it.
     * @param afunc: Activation function used for the network.
     * @param learningRate: learningRate of network.
     * @throws NeuralNetworkException on invalid weight and bias matrix size.
     */
    public NeuralNetwork(
            int[] neuronCounts,
            double[][][] weights,
            double[][] biases,
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

        this.afunc = afunc;
        this.learningRate = learningRate;
        this.epochSize = epochSize;

        /* copy the original array and add an extra layer to it. */
        int[] ncounts = new int[neuronCounts.length + 1];
        System.arraycopy(neuronCounts, 0, ncounts, 0, neuronCounts.length);
        ncounts[neuronCounts.length] = neuronCounts[neuronCounts.length - 1];

        /* add the layers */
        for (int i = 0; i < ncounts.length - 1; i++) {
            layers.add(
                    new Layer(
                            ncounts[i],
                            ncounts[i + 1],
                            (weights == null || (i + 2) == ncounts.length) ? null : weights[i],
                            (biases == null) ? null : biases[i],
                            afunc,
                            i
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

    public void setCallBack(Callback callback) {
        this.callBack = callback;
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

    /**
     * Trains the Neural Network with the given TrainingSet.
     *
     * @param set : Trainingset to be used to train.
     */
    public void train(TrainingSet set) {

        int currentIndex = 0;
        trainingAccuracy = 0;

        for (TrainingElement element : set.getElements()) {

            double[] input = element.getInputVector();
            double[] expectedOutput = element.getExpectedOutput();

            /* propagate forward */
            fpropagate(input);

            currentIndex++;
            totalError = 0;

            computeLastLayerError(expectedOutput);

            /* Now propagate the error backwards to find the gradient. */
            for (int i = layersCount() - 2; i > 0; i--) {
                totalError += layers.get(i).computeErrors(layers.get(i + 1).getErrors());
            }

            /* gradient-descent only after completion of an epoch. */
            if ((currentIndex % epochSize) == 0) {

                //now correct errors
                for (int i = layersCount() - 1; i > 0; i--) {
                    layers.get(i).correctErrors(
                            (i == 0) ? input : layers.get(i - 1).getActivations(),
                            (i == 0) ? null : layers.get(i - 1).getWeights(),
                            learningRate,
                            i
                    );
                }

                if (callBack != null) {
                    callBack.function(this);
                }
            }
        }
    }

    public double[] getOutput() {
        return layers.get(layers.size() - 1).getActivations();
    }

    private void computeLastLayerError(double[] output) {
        double diff, err;
        Layer lastLayer;
        Neuron neuron;

        lastLayer = layers.get(layersCount() - 1);
        cost = 0;
        Util.puts("errors [");
        for (int i = 0; i < lastLayer.neuronCount(); i++) {
            neuron = lastLayer.getNeuron(i);
            diff = (neuron.getActivation() - output[i]);
            cost += diff * diff;
            err = diff * neuron.getDiffActivation();
            totalError += err;
            neuron.setError(err);
            Util.puts(neuron.getError() + ",");
        }
        cost /= 2;
        Util.puts("]\n");
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
        String toret = "{\n" + "\tlearningrate: " + learningRate + "\n";
        for (Layer layer : layers) {
            toret += layer.toString() + "\n";
        }
        return toret + "\n}";
    }

    public int getOutputSize() {
        return layers.get(layersCount() - 1).neuronCount();
    }

    public double getTrainingAccuracy() {
        return trainingAccuracy;
    }
}
