package mpocr;

import java.io.Serializable;

/**
 *  Functional core of the Neural Network.
 */
public class Neuron implements Serializable {
    
    private int index;
    private double bias;
    private double error;
    private double winput;
    private double activation;
    private ActivationFunction afunc;
    
    /**
     * @param afunc : Activation function to be used.
     * @param index : index of this neuron in the parent layer.
     */
    Neuron(ActivationFunction afunc, int index) {
        this.afunc = afunc;
        this.index = index;
        randomize();
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
    public double getError() {
        return error;
    }
    public double getDiffActivation() {
        return afunc.derivative(getWeightedInput());
    }
    public double getWeightedInput() {
        return winput + bias;
    }
    
    public void setError(double error) {
        this.error = error;
    }
    public void setBias(double d) {
        bias = d;
    }
    
    public final void randomize() {
        this.bias = Math.pow(-1, (int)(Math.random()*100)) * Math.random();
    }
    
    @Override
    public String toString() {
        return
                "\n\t\t\t\t{" + 
                    "\n\t\t\t\t\tbias: " + bias + "," +
                    "\n\t\t\t\t\terror: " + error + "," +
                    "\n\t\t\t\t\tactivation: " + activation + "," +
                    "\n\t\t\t\t\twinput: " + winput +
                "\n\t\t\t\t}";
    }
}
