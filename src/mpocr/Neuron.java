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
    
    private double bias, oldbias;
    private double error;
    private double activation;
    private double winput;
    
    ActivationFunction afunc;
    
    Neuron(double bias, ActivationFunction afunc) {
        this.bias = bias;
        this.afunc = afunc;
    }
    
    /* calculates the activation of this neuron. */
    public double process(double pWeights[], double pActivations[]) {
        winput = 0.0;
        for (int i = 0; i < Util.minlen(pWeights, pActivations); i++) {
            winput += pWeights[i] * pActivations[i];
        }
        activation = afunc.fire(winput + bias);
        return activation;
    }
    
    public double getWinput() {
        return winput;
    }
    public double getActivation() {
        return activation;
    }
    public double getBias() {
        return bias;
    }
    public double getOldBias() {
        return oldbias;
    }
}
