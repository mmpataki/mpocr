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
    
    private double bias;
    private double error;
    private double activation;
    private double winput;
    private int index;
    
    ActivationFunction afunc;
    
    Neuron(Double bias, ActivationFunction afunc, int index) {
        if(bias == null) {
            randomize();
        } else {
            this.bias = bias;
        }
        this.afunc = afunc;
        this.index = index;
    }
    
    /* calculates the activation of this neuron. */
    public double process(double pWeights[], double pActivations[]) {
        if(pWeights == null) {
            winput = pActivations[index];
        } else {
            winput = 0.0;
            for (int i = 0; i < Util.minlen(pWeights, pActivations); i++) {
                winput += pWeights[i] * pActivations[i];
            }
        }

        activation = afunc.fire(winput + bias);
        return activation;
    }
    
    public double getActivation() {
        return activation;
    }
    public double getBias() {
        return bias;
    }
    public final void randomize() {
        this.bias = Math.pow(-1, (int)(Math.random()*100)) * Math.random();
    }
    public double getError() {
        return error;
    }
    public void setError(double error) {
        this.error = error;
    }
    public double getDiffActivation() {
        return afunc.derivative(getWeightedInput());
    }
    public double getWeightedInput() {
        return winput + bias;
    }
    public void setBias(double d) {
        bias = d;
    }
    @Override
    public String toString() {
        String toret = "\n\t\t\tneuron[" + index + "]: {";
        toret += "\n\t\t\t\tbias: " + bias + "\n\t\t\t\terror: " + error + "\n\t\t\t\tactivation: " + activation + "\n\t\t\t\twinput: " + winput;
        return toret + "\n\t\t\t}";
    }
}
