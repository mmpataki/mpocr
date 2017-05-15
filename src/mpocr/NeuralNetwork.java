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
 * An implementation of FeedForward NeuralNetork with Backpropogation 
 * @author mmp
 */
public class NeuralNetwork implements INeuralNetwork, Serializable {

    /* size of the epoch to feed before a GD. */
    private int epochSize;
    
    /* learning rate of the network. [0, 1] */
    private double learningRate;
    
    /* current Iteration of training */
    private int currentIteration;
    
    /* maximum error allowed below which training stops */
    private double maxError = 0.0;
    
    /* cost and error of the network */
    private double cost, totalError;
    
    /* layers in this network. */
    private ArrayList<Layer> layers;
    
    /* maximum iterations allowed for a training. */
    private int maxIterations = Integer.MAX_VALUE;
    
    /* a callback object which is called per iteration of training. */
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
    NeuralNetwork(
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
    public int getLayersCount() {
        return layers.size();
    }

    @Override
    public Layer getLayer(int index) {
        if (index < 0 || index >= layers.size()) {
            return null;
        }
        return layers.get(index);
    }
    
    public double[] getOutput() {
        return layers.get(layers.size() - 1).getActivations();
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
     * Load a neural stored at path 'fileName'
     * @param fileName : path of file where a NeuralNetwork is stored by save()
     * @return a loaded NeuralNetwork
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public static NeuralNetwork load(String fileName) throws FileNotFoundException, IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
        NeuralNetwork net = (NeuralNetwork) ois.readObject();
        return net;
    }
    
    /**
     * Store this NeuralNetwork at path 'storePath'.
     * @param storePath: path to a file where to store this network
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public void save(String storePath) throws FileNotFoundException, IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storePath));
        oos.writeObject(this);
    }
    
    /**
     * Trains the Neural Network with the given TrainingSet.
     *
     * @param set : Trainingset to be used to train.
     */
    public void train(TrainingSet set) {

        int currentIndex;
        double[] input, expectedOutput;

        currentIndex = 0;
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
                for (int i = getLayersCount() - 2; i > 0; i--) {
                    layers.get(i).computeErrors(layers.get(i + 1).getErrors());
                }

                /* gradient-descent only after completion of an epoch. */
                if ((currentIndex % epochSize) == 0) {

                    /* now correct errors */
                    for (int i = getLayersCount() - 1; i > 0; i--) {
                        layers.get(i).correctErrors(
                                (i == 0) ? input : layers.get(i - 1).getActivations(),
                                (i == 0) ? null : layers.get(i - 1).getWeights(),
                                learningRate
                        );
                    }
                }
            }
            trainingCallBack.function(this);
            currentIteration++;
        } while (maxError < totalError && currentIteration < maxIterations);

    }

    /**
     * Propogates the input through the netork.
     * @param input : input to the network.
     */
    public void fpropagate(double[] input) {
        double[][] wts = null;
        double[] ip = input;
        for (int i = 0; i < getLayersCount(); i++) {
            layers.get(i).process(wts, ip);
            wts = layers.get(i).getWeights();
            ip = layers.get(i).getActivations();
        }
    }
    
    private void computeLastLayerError(double[] output) {
        double err, errSum, diff;
        Layer lastLayer;
        Neuron neuron;

        errSum = 0;
        lastLayer = layers.get(getLayersCount() - 1);

        for (int i = 0; i < lastLayer.neuronCount(); i++) {
            neuron = lastLayer.getNeuron(i);
            diff = (neuron.getActivation() - output[i]);
            err = diff * neuron.getDiffActivation();
            errSum += (diff * diff);
            neuron.setError(err);
        }
        totalError += errSum / 2;
    }

    @Override
    public String toString() {
        return 
                "{" +
                    "\n\tlearningrate: " + learningRate + "," +
                    "\n\tlayers : " + layers.toString() +
                "}";
    }
}
