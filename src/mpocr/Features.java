/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpocr;

import java.util.HashMap;

/**
 *
 * @author mmp
 */
public class Features {
    
    private HashMap<Integer, FeatureSet> features;
    
    public void add(FeatureSet set) {
        features.put(set.getmagic(), set);
    }
    
    public FeatureSet get(int magic) {
        return features.get(magic);
    }
    
}
