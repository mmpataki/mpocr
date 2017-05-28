package mnn;

import java.io.Serializable;
import java.util.Arrays;

/**
 *  The Layer object aggregated in the NeuralNetwork.
 */
public class Layer implements Serializable {
    
    /*
     * DESIGN-NOTE:
     *      The weight matrices here hold the weights by this layer
     *  converging into next layer's corresponding indexed neuron in
     *  each row.
     */
    
    private Neuron[] neurons;
    private double[][] weights;
    private int index;
    
    /**
     *.
     * ncount: number of neurons in this layer.
     * nncount: number of neurons in next layer.
     * afunc : ActivationFunction for the layer.
     * index : index of the layer in the network.
     */
    Layer(int ncount, int nncount, ActivationFunction afunc, int index)
            throws NeuralNetworkException {
        
        if(ncount == 0 || nncount == 0) {
            throw new NeuralNetworkException(
                    "The neurons in a layer cannot be zero"
            );
        }
        
        this.index = index;
        neurons = new Neuron[ncount];
        this.weights = new double[nncount][ncount];
        
        for (int i = 0; i < ncount; i++) {
            neurons[i] = new Neuron(afunc, i);
        }
        
        randomize();
    }
    
    public final void randomize() {
        for (double[] weight : weights) {
            for (int j = 0; j < weights[0].length; j++) {
                weight[j] = Math.random() * Math.pow(-1, (int)(Math.random()*10));
            }
        }
    }
    
    /**
     * Process this layers data
     * @param pWeights : 
     *     weights between this and previous layer for input layer specify null.
     * @param pActivations 
     */
    public void process(double[][] pWeights, double[] pActivations) {
        for (int i = 0; i < neurons.length; i++) {
            neurons[i].process(pWeights == null ? null : pWeights[i], pActivations);
        }
    }
    
    /**
     * Corrects the errors in this layer neurons using GradientDescent.
     * @param pActivations: previous layer activations.
     * @param pWeights: previous layer weights
     * @param eta : learning rate.
     */
    public void correctErrors(double[] pActivations, double[][] pWeights, double eta) {
        for (int i = 0; i < neuronCount(); i++) {
            neurons[i].setBias(neurons[i].getBias() - (eta * neurons[i].getError()));
        }
        if(pWeights == null)
            return;
        
        for (int i = 0; i < pWeights.length; i++) {
            for (int j = 0; j < pActivations.length; j++) {
                pWeights[i][j] -= (eta * (pActivations[j] * neurons[i].getError()));
            }
        }
    }
    
    /**
     * computes errors of this layer neurons and sets them to neurons.
     * @param nextErrors : nextLayer errors.
     */
    public void computeErrors(double[] nextErrors) {

        double[][] transposeWeights, tmpErrors, errors;
        double avgError;

        try {

            transposeWeights = Matrix.transpose(weights);
            tmpErrors = Matrix.transpose(new double[][]{nextErrors});
            errors = new double[transposeWeights.length][1];

            Matrix.multiply(errors, transposeWeights, tmpErrors);

            for (int i = 0; i < neuronCount(); i++) {
                
                errors[i][0] *= neurons[i].getDiffActivation();
                avgError = (neurons[i].getError() + errors[i][0]) / 2;
                neurons[i].setError(avgError);
            }

        } catch (MatrixException me) {
            System.err.println(me.toString());
        }
    }
    
    
    public Neuron getNeuron(int index) {
        if(index < 0 || index >= neurons.length)
            return null;
        return neurons[index];
    }
    
    public double[][] getWeights() {
        return weights;
    }
    
    public int neuronCount() {
        return neurons.length;
    }

    public double[] getActivations() {
        
        double[] ops = new double[neuronCount()];
        for (int i = 0; i < neurons.length; i++) {
            ops[i] = neurons[i].getActivation();
        }
        return ops;
    }

    public double[] getErrors() {
        double[] errors = new double[neuronCount()];
        for (int i = 0; i < errors.length; i++) {
            errors[i] = neurons[i].getError();
        }
        return errors;
    }
    
    @Override
    public String toString() {
        return
                "\n\t\t{" + 
                    "\n\t\t\tweights : " + Arrays.deepToString(weights) + "," + 
                    "\n\t\t\tneurons : " + Arrays.deepToString(neurons) + "" + 
                "\n\t\t}";
    }
}
