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
class TrainingElement {
    
    double[] input;
    double[] expectedOutput;

    public TrainingElement(double[] input, double[] expectedOutput) {
        this.input = input;
        this.expectedOutput = expectedOutput;
    }

    public double[] getInputVector() {
        return input;
    }

    public double[] getExpectedOutput() {
        return expectedOutput;
    }
    
}
