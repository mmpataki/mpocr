/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpocr;


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
    
}
