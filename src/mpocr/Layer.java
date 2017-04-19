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
    
    private Neuron[] neurons;
    private double[][] weights;
    private double[][] tweights;
    private double[][] oldWeights;
    
    
    /*
     * ncount: number of neurons in this layer.
     * nncount: number of neurons in next layer.
     */
    Layer(int ncount, int nncount, double[][] weights, double[] biases, ActivationFunction afunc) throws Exception {
        
        if(
            ncount == 0 || nncount == 0 ||
            ncount != weights.length ||
            ncount != biases.length ||
            weights.length == 0 ||
            weights[0].length != nncount) {
            throw new Exception("The number of weights didn't match");
        }
        
        neurons = new Neuron[ncount];
        this.weights = weights;
        for (int i = 0; i < ncount; i++) {
            neurons[i] = new Neuron(biases[i], afunc);
        }
    }
    
    public Neuron getNeuron(int index) {
        if(index < 0 || index >= neurons.length)
            return null;
        return neurons[index];
    }
    public int neuronCount() {
        return neurons.length;
    }

    public int getMagic() {
        return magic;
    }

}
