/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpocr;

import static java.lang.Integer.max;
import java.util.ArrayList;

/**
 *
 * @author mmp
 */
public class NeuralNetwork implements INeuralNetwork {
    
    ArrayList<Layer> layers;
    double[][] inputWeights;

    /**
     * To create a neural network only first and last params are enough Others
     * can be null.
     * @param neuronCounts: numeber of neurons in each layer.
     * @param weights: weight matrices for each layer. If this is null then
     *      the network will randomize it.
     * @param biases: biases of each neuron in each layer. If this is null
     *      the network will randomize it.
     * @param afunc: Activation function used for the network.
     * @throws Exception on invalid weight and bias matrix size.
     */
    public NeuralNetwork(int[] neuronCounts, double[][][] weights,
            double[][] biases, ActivationFunction afunc) throws Exception {
        
        layers = new ArrayList<>();
        if(afunc == null || neuronCounts == null ) {
            throw new Exception("Activation Function and neuronCounts"
                    + " per layer cannot be null");
        }
        
        /* copy the original array and add an extra layer to it. */
        int[] ncounts = new int[neuronCounts.length + 1];
        System.arraycopy(neuronCounts, 0, ncounts, 0, neuronCounts.length);
        ncounts[neuronCounts.length] = neuronCounts[neuronCounts.length - 1];
        
        /* add the layers */
        for (int i = 0; i < ncounts.length - 1; i++) {
            layers.add(
                    new Layer(
                            ncounts[i],
                            ncounts[i+1],
                            (weights == null) ? null : weights[i],
                            (biases == null) ? null : biases[i],
                            afunc
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
        if(index < 0 || index >= layers.size())
            return null;
        return layers.get(index);
    }
    
    public void propagate(double[] input) {
        double[][] wts = null;
        for (int i = 0; i < layersCount(); i++) {
            layers.get(i).process(wts, input);
            wts = layers.get(i).getWeights();
        }
    }

    public double[] getOutput() {
        return layers.get(layers.size()-1).getActivations();
    }
}
