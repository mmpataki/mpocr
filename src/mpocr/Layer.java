/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpocr;

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
    
    /**
     * .
     * ncount: number of neurons in this layer.
     * nncount: number of neurons in next layer.
     * weights : weight matrix of this layer. The format must be transpose
     *      matrix of the matrix containing the weights emerging out of each
     *      neuron in a row. if provided null the layer will randomize it
     */
    Layer(int ncount, int nncount, double[][] weights,
            double[] biases, ActivationFunction afunc) throws Exception {
        
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
}
