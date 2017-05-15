package mpocr;

import java.io.Serializable;

/**
 *  Genaralization of Activation Function. Use this class to implement other
 *  ActivationFunctions like SigmoidFunction, Tanh ...
 */
public abstract class ActivationFunction implements Serializable {
    
    /* function to compute the activation */
    public abstract double fire(double z);
    
    /* function to compute the derivate of the activation */
    public abstract double derivative(double a);
    
}
