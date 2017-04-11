/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpocr;

import java.util.ArrayList;

/**
 *
 * @author mmp
 */
public class ClassifierNetwork extends NeuralNetwork {
    
    ArrayList<Layer> layers;

    public ClassifierNetwork() {
         layers = new ArrayList<>();
    }
    
    public int addLayer(Layer l) {
        layers.add(l);
        return 0;
    }
    
    public int deleteLayer(int index) {
        layers.remove(index);
        return 0;
    }

    @Override
    public int layersCount() {
        return layers.size();
    }

    @Override
    public Layer getLayer(int index) {
        if(index < 0 || index >= layers.size())
            return null;
        return layers.get(index);
    }
}
