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
    
    private Neuron[] neurons;
    private String name;
    public static final int magic = 0x138d75;
    
    Layer(int ncount, double[] weights, double[] thresholds, String name) {
        
        if(ncount != weights.length || ncount != thresholds.length) {
            Util.puts("The number of weights didn't match");
            return;
        }
        
        neurons = new Neuron[ncount];
        for (int i = 0; i < ncount; i++) {
            neurons[i] = new Neuron(weights[i], thresholds[i]);
        }
        this.name = name;
    }
    
    Layer(int ncount, double weight, double threshold, String name) {
        
        neurons = new Neuron[ncount];
        for (int i = 0; i < ncount; i++) {
            neurons[i] = new Neuron(weight, threshold);
        }
        this.name = name;
        
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

    String getName() {
        return name;
    }
}
