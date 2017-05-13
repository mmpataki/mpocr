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
public class GridIntersects extends FeatureSet {

    public static final int magic = 0x37845;
    private static final int DIVS = 10;
    private static final int WIDTH = 100;
    private static final int HEIGHT = 100;
    private double[] features;

    GridIntersects(Segment s) {
        
        int k, fg, bg, cc, incx, incy, idata[][];
        
        k = 0;
        incx = WIDTH / DIVS;
        incy = HEIGHT / DIVS;
        fg = s.getForeground();
        bg = s.getBackground();
        features = new double[DIVS * 2];
        idata = s.getBounded().resizeImage(HEIGHT, WIDTH).getImageData();
        
        for (int i = 0; i < HEIGHT; i += DIVS) {
            cc = bg;
            for (int j = 0; j < WIDTH; j++) {
                if(cc != idata[i][j]) {
                    cc = idata[i][j];
                    features[k] += (cc == fg) ? 1 : 0;
                }
            }
            k++;
        }
        
        for (int i = 0; i < WIDTH; i += incx) {
            cc = bg;
            for (int j = 0; j < HEIGHT; j++) {
                if(cc != idata[j][i]) {
                    cc = idata[j][i];
                    features[k] += (cc == fg) ? 1 : 0;
                }
            }
            k++;
        }
        
    }
    
    @Override
    public int getmagic() {
        return magic;
    }

    @Override
    public void printFeatures() {
        Util.puts("GridIntersects : " + Arrays.toString(features) + "\n");
    }

    @Override
    public double[] getFeatures() {
        return features;
    }

}
