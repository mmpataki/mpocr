/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpocr;

import java.util.ArrayList;
import java.util.Vector;

/**
 *
 * @author mmp
 */
public class WTrainingSet {
    
    TrainingSet tset;
    ArrayList<Vector<Double>> eopSet;

    public WTrainingSet() {
        tset = new TrainingSet();
        eopSet = new ArrayList<>();
    }
    
}
