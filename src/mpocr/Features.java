package mpocr;

import java.util.HashMap;

/**
 * A wrapper around all the features. This can help in storing the features
 * as a collection and accessing them using the KEY as the magic of the
 * Feature class.
 */
public class Features {
    
    private HashMap<Integer, IFeatureSet> features;

    public Features() {
        features = new HashMap<>();
    }
    
    public void add(IFeatureSet set) {
        features.put(set.getmagic(), set);
    }
    
    public IFeatureSet get(int magic) {
        return features.get(magic);
    }
    
}
