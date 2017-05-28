package mnn;

/**
 *  A must interface for NeuralNetworks which is used by NNVisualizer.
 */
public interface INeuralNetwork {
    
    /* to get the number of layers */
    public int getLayersCount();
    
    /* to get a layer in network. */
    public Layer getLayer(int index);
}
