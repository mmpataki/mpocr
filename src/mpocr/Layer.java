/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpocr;

import java.util.Arrays;

/**
 *
 * @author mmp
 */
public class Layer {
    
    public static final int magic = 0x138d75;
    
    /*
     * NOTE:
     *      The weight matrices here hold the weights by this layer
     *  converging into next layer's corresponding indexed neuron in
     *  each row.
     */
    
    private Neuron[] neurons;
    private double[][] weights;
    private double[][] oldWeights;
    private int index;
    /**
     * .
     * ncount: number of neurons in this layer.
     * nncount: number of neurons in next layer.
     * weights : weight matrix of this layer. The format must be transpose
     *      matrix of the matrix containing the weights emerging out of each
     *      neuron in a row. if provided null the layer will randomize it
     */
    Layer(int ncount, int nncount, double[][] weights,
            double[] biases, ActivationFunction afunc, int index) throws Exception {
        
        if(
            ncount == 0 || nncount == 0 ||
            (
                weights != null && 
                (
                    weights.length == 0 ||
                    weights[0].length != nncount  ||
                    ncount != weights.length
                ) &&
                (biases != null && ncount != biases.length)
            )
        ) {
            throw new Exception("The number of weights didn't match");
        }
        
        neurons = new Neuron[ncount];
        for (int i = 0; i < ncount; i++) {
            /* Please note : if the biase is null then neuron is asked to
             * randomize the bias implicitly
             */
            neurons[i] = new Neuron((biases == null ? null : biases[i]), afunc, i);
        }
        
        this.weights = weights;
        if(this.weights == null) {
            this.weights = new double[nncount][ncount];
            randomize((biases == null));
        }
        this.index = index;
    }
    
    public final void randomize(boolean neuron) {
        
        for (double[] weight : weights) {
            for (int j = 0; j < weights[0].length; j++) {
                weight[j] = Math.random();
            }
        }
        if(neuron) {
            for (Neuron n : neurons) {
                n.randomize();
            }
        }
    }
    
    /**
     * Process this layers data
     * @param pWeights : weights between this and previous layer. for input
     *      layer specify null.
     * @param pActivations 
     */
    public void process(double[][] pWeights, double[] pActivations) {
        for (int i = 0; i < neurons.length; i++) { 
            neurons[i].process(pWeights == null ? null : pWeights[i], pActivations);
        }
        Util.puts("activations(" + index + ") : [");
        for (double x : getActivations()) {
            Util.puts(x + ", ");
        }
        Util.puts("]\n");
    }
    
    public void computeErrors(double[] errors) throws Exception {
        
        double[][] twts = Matrix.transpose(weights);
        double[][] terr = Matrix.transpose(new double[][]{errors});
        double[][] err  = new double[twts.length][1];
        double[]   acts = new double[neuronCount()];
        
        Matrix.multiply(err, twts, terr);
        
        Util.puts("errors [");
        for(int i = 0; i<neuronCount(); i++) {
            err[i][0] *= neurons[i].getDiffActivation();
            neurons[i].setError(err[i][0]);
            Util.puts(err[i][0] + ", ");
        }
        Util.puts("]\n");
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

    public int getMagic() {
        return magic;
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
    
    public void correctErrors(double[] activations, double[][] pWeights, double eta, int index) {
        for (int i = 0; i < neuronCount(); i++) {
            neurons[i].setBias(neurons[i].getBias() - (eta * neurons[i].getError()));
        }
        if(pWeights == null)
            return;
        for (int i = 0; i < pWeights.length; i++) {
            for (int j = 0; j < pWeights[0].length; j++) {
                pWeights[i][j] -= (eta * (activations[j] * neurons[i].getError()));
            }
        }
    }

    @Override
    public String toString() {
        String toret = "\tlayer[" + index + "] : {\n";
        toret += "\t\tweights : " + Arrays.deepToString(weights) + "\n";
        toret += "\t\tneurons : " + Arrays.deepToString(neurons) + "\n";
        return toret + "\t}";
    }
    
}
