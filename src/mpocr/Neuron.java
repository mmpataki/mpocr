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
class Neuron {
    
    private double weight;
    private double threshold;
    
    Neuron(double xweight, double xthreshold) {
        weight = xweight;
        threshold = xthreshold;
    }
    
    public double getWeight() {
        return weight;
    }
    public double getThreshold() {
        return threshold;
    }
    
}
