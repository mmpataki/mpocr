package mnn;

/**
 * Wrapper around the input and expectedOutput of a training component.
 */
class TrainingElement {
    
    private double[] input;
    private double[] expectedOutput;

    public TrainingElement(double[] input, double[] expectedOutput) {
        this.input = input;
        this.expectedOutput = expectedOutput;
    }

    /**
     * @return the input vector
     */
    public double[] getInputVector() {
        return input;
    }

    /**
     * @return the expectedOutput vector.
     */
    public double[] getExpectedOutput() {
        return expectedOutput;
    }
    
}
