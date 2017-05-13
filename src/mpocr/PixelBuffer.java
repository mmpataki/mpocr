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
    private static final int WIDTH = 10, HEIGHT = 10;
    private double[] features;

    /**
     *
     * @param s: segment of which feature is to be exported.
     */
    PixelBuffer(Segment s) {
        
        int[][] xdata = s.getBounded().resizeImage(HEIGHT, WIDTH).getImageData();
        
        features = new double[WIDTH * HEIGHT];
        
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                features[i * WIDTH + j] = xdata[i][j];
            }
        }
    }
    
    @Override
    public int getmagic() {
        return magic;
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
