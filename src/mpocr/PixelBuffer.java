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
        
        int idata[][] = s.getImageData();
        
        int ht = s.getHeight(), wd = s.getWidth();
        int fg = s.getForeground();
        int top = 0, left = 0, right = wd - 1, bottom = ht - 1;
        while(Util.isemptyLine(idata[top], fg, 0, wd, 0.0)) top++;
        while(Util.isemptyLine(idata[bottom], fg, 0, wd, 0.0)) bottom--;
        while(Util.isemptyCol(idata, top, bottom, left, fg, 0.0)) left++;
        while(Util.isemptyCol(idata, top, bottom, right, fg, 0.0)) right--;

        int[][] bndbox = new int[(bottom - top + 1)][(right - left + 1)];
        
        //copy data
        for (int i = top; i <= bottom; i++) {
            for (int j = left; j <= right; j++) {
                bndbox[i - top][j - left] = idata[i][j];
            }
        }
        
        int[][] xdata = resizeImage(new Segment(bndbox));
        
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

    
    /**
     * Resizes a segment to fixed dimensions.
     * 
     * ALGORITHM:
     * 
     * 1. Analyze the density of the pixels foreground to background and vice-versa
     * 2. ldp <- low density pixel.
     * 3. Create a buffer B of size of required dimensions.
     * 4. Fill B with the high density color.
     * 5. foreach low-density pixel in the original image
     * 6.     fill the rectangle of size [ycoeff X xcoeff] with ldp
     * 7. done.
     * 
     * @param s
     * @return 
     */
    
    private int[][] resizeImage(Segment s) {
        
        int height = s.getHeight();
        int width = s.getWidth();
        int fg = s.getForeground();
        int bg = s.getBackground();
        int[][] idata = s.getImageData();
        int[][] data = new int[HEIGHT+1][WIDTH+1];
        
        
        int fp = 0, tp = height * width;
        int ldp = bg, hdp = fg;
        int lx, ly, hx, hy;
        
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                fp += (idata[i][j] == fg) ? 1 : 0;
            }
        }
        
        /*
         * bias here helps where the foreground and backgrounds are in almost
         * same proportion.
         */
        double bias = 0.1;
        if(fp < (tp - fp + (tp * bias))) {
            ldp = fg;
            hdp = bg;
        }
        
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                data[i][j] = hdp;
            }
        }
        
        double xcoeff = (double)WIDTH / width;
        double ycoeff = (double)HEIGHT / height;
        
        s.printImageForce();
        
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                
                if(idata[i][j] == hdp)
                    continue;
                
                lx = (int)(j * xcoeff);
                ly = (int)(i * ycoeff);
                
                hy = (int) (ly + ycoeff);
                hx = (int) (lx + xcoeff);
                
                for (int k = ly; k <= hy; k++) {
                    for (int l = lx; l <= hx; l++) {
                        if(l < WIDTH && k < HEIGHT)
                            data[k][l] = ldp;
                    }
                }
            }
        }
        
        //TESTING PURPOSE
        System.out.print("RESIZED IMAGE");
        for (int i = 0; i < HEIGHT; i++) {
            System.out.println("");
            for (int j = 0; j < WIDTH; j++) {
                System.out.print((data[i][j] == bg) ? "." : "#");
            }
        }
        /**/
        
        return data;
    }
    
}
