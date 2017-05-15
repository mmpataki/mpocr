package mpocr;

import java.util.Arrays;

/**
 * Features containing the foreground pixel density of the image
 * in 9 parts of the image.
 */
public class Zones implements IFeatureSet {

    /* the magic number of the FeatureSet */
    public static final int magic = 0x0001;
    
    /* feature vector */
    private double zones[];
    
    /**
     * The idea is to divide the image into 9 equal parts and store the
     * foreground pixel densities of each zone. The proof this method works
     * also for all images, lie in the minimum character segment size. It's
     * never less than 3x3 matrix.
     */
    Zones(Segment s) {
        
        int ht = s.getHeight();
        int wd = s.getWidth();
        int[][] idata = s.getImageData();
        int fg = s.getForeground();
        int top = 0, left = 0, right = wd - 1, bottom = ht - 1;
        zones = new double[9];
        
        if(ht < 3 || wd < 3) {
            Util.puts("segment too small for feature extraction\n");
            return;
        }
        
        while(Util.isemptyLine(idata[top], fg, 0, wd, 0.0)) top++;
        while(Util.isemptyLine(idata[bottom], fg, 0, wd, 0.0)) bottom--;
        while(Util.isemptyCol(idata, top, bottom, left, fg, 0.0)) left++;
        while(Util.isemptyCol(idata, top, bottom, right, fg, 0.0)) right--;

        ht = bottom - top + 1;
        wd = right - left + 1;
        
        int wf = (int)Math.ceil((double)wd / 3);
        int hf = (int)Math.ceil((double)ht / 3);
        int ppz = wf * hf;
        
        for (int i = 0; i < zones.length; i++) {
            zones[i] = 0;
        }
        int zone = 0;
        for (int i = top; i <= bottom; i++) {
            for (int j = left; j <= right; j++) {
                zone = ((i-top) / hf) * 3 + ((j-left) / wf); //don't try to oversimplify.
                zones[zone] += ((idata[i][j] == fg) ? 1 : 0);
            }
        }
        for (int i = 0; i < zones.length; i++) {
            zones[i] /= ppz;
        }
    }
    
    @Override
    public int getmagic() {
        return magic;
    }

    @Override
    public void printFeatures() {
        Util.puts("Zones : " + Arrays.toString(zones) + "\n");
    }
    
    @Override
    public double[] getFeatures() {
        return zones;
    }
}
