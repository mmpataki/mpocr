package mpocr;

/**
 * Implementation of sigmoid function
 */
public class SigmoidFunction extends ActivationFunction {

    /*
     *   Sigmoid function:
     *      This function has a smooth curve bounded by [0,1]
     *   This is usually used for the neural-network training
     *   because of the smooth continuity it provides.
     */
    
    @Override
    public double fire(double z) {
        return (1.0 / (1 + Math.exp(-z)));
    }

    @Override
    public double derivative(double z) {
        double x = fire(z);
        return x * (1 - x);
    }
    
}
