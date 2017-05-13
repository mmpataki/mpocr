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
class TrainingSet {
    
    ArrayList<TrainingElement> set;

    public TrainingSet() {
        set = new ArrayList<>();
    }
    
    public void add(TrainingElement trainingElement) {
        set.add(trainingElement);
    }
    
    public int size() {
        return set.size();
    }

    ArrayList<TrainingElement> getElements() {
        return set;
    }

    public TrainingElement elementAt(int i) {
        return set.get(i);
    }
    
}
