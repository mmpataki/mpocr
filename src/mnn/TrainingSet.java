package mnn;

import java.util.ArrayList;

/**
 * Wrapper around the collection of TrainingElements.
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
