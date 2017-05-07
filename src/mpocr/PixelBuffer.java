/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpocr;

import java.util.Arrays;

/**
 *
 * @author mmp
 */
public class PixelBuffer extends FeatureSet {

    public static final int magic = 0x78342d;
    private double[] features;

    /**
     *
     * @param s: segment of which feature is to be exported.
     */
    public PixelBuffer(Segment s) {
        
        int height = s.getHeight();
        int width = s.getWidth();
        int[][] idata = s.getImageData();
        features = new double[width * height];
        
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                features[i * width + j] = idata[i][j];
            }
        }
    }
    
    @Override
    public int getmagic() {
        return magic;
    }

    @Override
    public double compare(FeatureSet fs1, FeatureSet fs2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void printFeatures() {
        Util.puts(Arrays.toString(features) + "\n");
    }

    @Override
    public double[] getFeatures() {
        return features;
    }
    
}
