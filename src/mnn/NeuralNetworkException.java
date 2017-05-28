package mnn;

/**
 * Wrapper around the Exception with a good name.
 */
public class NeuralNetworkException extends Exception {
    NeuralNetworkException(String message) {
        super(message);
    }
}
