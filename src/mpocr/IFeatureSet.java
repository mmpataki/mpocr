/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpocr;

/**
 *
 * @author mmp
 */
public interface IFeatureSet {
    
    /* return magic number of this featureset. */
    public int getmagic();
    
    /* return comparison percentage of the two FeatureSet */
    public double compare(FeatureSet fs1, FeatureSet fs2);
    
    /* to print features */
    public void printFeatures();

    /* to get features */
    public double[] getFeatures();
}
