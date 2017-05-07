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

    /**
     * To create a neural network only first and last params are enough Others
     * can be null.
     * @param neuronCounts: numeber of neurons in each layer.
     * @param weights: weight matrices for each layer. If this is null then
     *      the network will randomize it.
     * @param biases: biases of each neuron in each layer. If this is null
     *      the network will randomize it.
     * @param afunc: Activation function used for the network.
     * @param learningRate: learningRate of network.
     * @throws Exception on invalid weight and bias matrix size.
     */
    public NeuralNetwork(int[] neuronCounts, double[][][] weights,
            double[][] biases, ActivationFunction afunc,
            double learningRate) throws Exception {
        
        layers = new ArrayList<>();
        if(afunc == null || neuronCounts == null ) {
            throw new Exception("Activation Function and neuronCounts"
                    + " per layer cannot be null");
        }
        this.afunc = afunc;
        this.learningRate = learningRate;
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
                            (weights == null || (i+2) == ncounts.length) ? null : weights[i],
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
        if(index < 0 || index >= layers.size())
            return null;
        return layers.get(index);
    }
    
    public void propagate(double[] input, double[] output) throws Exception {
        
        //propagate forward
        fpropagate(input);
        
        /* back-propagate */
        
        // first step calculate the error in last layer.
        // (op - expop) (*) zl
        //
        computeLastLayerError(output);
        
        // Now propagate the error backwards to find the gradient.
        for (int i = layersCount() - 2; i > -1; i--) {
            layers.get(i).computeErrors(layers.get(i + 1).getErrors());
        }
        
        //now correct errors
        for (int i = layersCount() - 1; i > -1; i--) {
            layers.get(i).correctErrors(
                    (i == 0) ? input :layers.get(i-1).getActivations(),
                    (i == 0) ? null : layers.get(i-1).getWeights(),
                    learningRate,
                    i
            );
        }
    }

    public double[] getOutput() {
        return layers.get(layers.size()-1).getActivations();
    }

    private void computeLastLayerError(double[] output) {
        Layer ll = layers.get(layersCount() - 1);
        
        Util.puts("Lerror : [");
        for (int i = 0; i < ll.neuronCount(); i++) {
            Neuron n = ll.getNeuron(i);
            n.setError((n.getActivation() - output[i]) * n.getDiffActivation());
            Util.puts(n.getError() + ",");
        }
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
}
