package mpocr;

import java.io.Serializable;

/**
 * Abstract class to implement callbacks in NeuralNetowrk training.
 */
public abstract class CallBack implements Serializable {

    /**
     * this function will be called as a callback
     * @param param : The reference to the caller.
     */
    public abstract void function(Object param);
    
}
