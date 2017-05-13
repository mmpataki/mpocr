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
public class Intersects extends FeatureSet {

    public static final int magic = 0x10437;
    private double[] features;
    
    private static final int DIM            = 101;
    private static final int TOP_LEFT       = 0;
    private static final int TOP            = 1;
    private static final int TOP_RIGHT      = 2;
    private static final int LEFT           = 3;
    private static final int RIGHT          = 4;
    private static final int BOTTOM_LEFT    = 5;
    private static final int BOTTOM         = 6;
    private static final int BOTTOM_RIGHT   = 7;
    
    
    Intersects(Segment s) {
        
        s = new Segment(s.getBounded().resizeImage(DIM, DIM).getImageData());
        
        int cc, fg, idata[][], HDIM;
        
        HDIM = (DIM / 2) + (DIM % 2);
        fg = s.getForeground();
        idata = s.getImageData();
        features = new double[8];
        
        /* top-left diagonal */
        cc = ~fg; //just to say not fg.
        for (int i = 0; i < HDIM; i++) {
            if(idata[i][i] != cc) {
                cc = idata[i][i];
                features[TOP_LEFT] += (cc == fg) ? 1 : 0;
            }
        }
        
        /* top vertical */
        cc = ~fg; //just to say not fg.
        for (int i = 0; i < HDIM; i++) {
            if(idata[i][HDIM] != cc) {
                cc = idata[i][HDIM];
                features[TOP] += (cc == fg) ? 1 : 0;
            }
        }
        
        /* top-right diagonal */
        cc = ~fg; //just to say not fg.
        for (int i = 0; i < HDIM; i++) {
            if(idata[i][DIM - i - 1] != cc) {
                cc = idata[i][DIM - i - 1];
                features[TOP_RIGHT] += (cc == fg) ? 1 : 0;
            }
        }
        
        /* left horizontal */
        cc = ~fg; //just to say not fg.
        for (int i = 0; i < HDIM; i++) {
            if(idata[HDIM][i] != cc) {
                cc = idata[HDIM][i];
                features[LEFT] += (cc == fg) ? 1 : 0;
            }
        }
        
        /* right horizontal */
        cc = ~fg; //just to say not fg.
        for (int i = HDIM; i < DIM; i++) {
            if(idata[HDIM][i] != cc) {
                cc = idata[HDIM][i];
                features[RIGHT] += (cc == fg) ? 1 : 0;
            }
        }
        
        /* bottom-left diagonal */
        cc = ~fg; //just to say not fg.
        for (int i = HDIM; i < DIM; i++) {
            if(idata[i][DIM - i] != cc) {
                cc = idata[i][DIM - i];
                features[BOTTOM_LEFT] += (cc == fg) ? 1 : 0;
            }
        }
        
        /* bottom vertical */
        cc = ~fg; //just to say not fg.
        for (int i = HDIM; i < DIM; i++) {
            if(idata[i][HDIM] != cc) {
                cc = idata[i][HDIM];
                features[BOTTOM] += (cc == fg) ? 1 : 0;
            }
        }
        
        /* bottom-right diagonal */
        cc = ~fg; //just to say not fg.
        for (int i = HDIM; i < DIM; i++) {
            if(idata[i][i] != cc) {
                cc = idata[i][i];
                features[BOTTOM_RIGHT] += (cc == fg) ? 1 : 0;
            }
        }
        
    }

    @Override
    public int getmagic() {
        return magic;
    }

    @Override
    public void printFeatures() {
        Util.putsf("Intersections : " + Arrays.toString(features) + "\n");
    }

    @Override
    public double[] getFeatures() {
        return features;
    }
    
}
