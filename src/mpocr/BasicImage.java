package mpocr;

import java.util.Arrays;


class BasicImage implements IImage {

    /* buffer holding image pixels */
    protected int[][] iData;
    
    /* to check if the buffer 'iData' is dirty. */
    boolean dirty = false;

    /* constants used while rotating images */
    private static final int NREVERSE = 0;
    private static final int REVERSE = 1;
    
    
    /**
     * Constructor for initialising the image
     * NOTE
     *     The iData you send is just referenced from here hence modifications
     *     through other references will be reflecting here. Careful.
     */
    BasicImage(int[][] iData) {
        this.iData = iData;
    }

    /* Dummy ctor to shut compiler's mouth while extending this in OImage. */
    BasicImage() {  }
    
    
    /* for getting the height and width of the image */
    @Override
    public int getWidth() { return iData.length > 0 ? iData[0].length : 0; }

    @Override
    public int getHeight() { return iData.length; }

    /* to get the foreground and background color of the image */
    @Override
    public int getForeground() { return 0; }

    @Override
    public int getBackground() { return 1; }

    @Override
    public int[][] getImageData() { return iData; }

    
    @Override
    public String toString() {
        
        String toRet = "";
        
        for (int[] row : iData) {
            for (int col : row) {
                toRet += col;
            }
            toRet += "\n";
        }
        return toRet += "\n";
    }

    /* to rotate the image to given degree */
    public void rotate(double deg) {

        double m = (double) Math.tan(deg * Math.PI / 180);
        double am = Math.abs(m);
        int mx = (int) Math.round((double) 1 / am * iData[0].length);
        int my = (int) Math.round((double) 1 / am * iData.length);

        int[][] cp = new int[my][mx];

        for (int i = 0; i < my; i++) {
            for (int j = 0; j < mx; j++) {
                cp[i][j] = 0;
            }
        }
        removeIntersection(iData);
        int yc = getXIntersection(iData, REVERSE);
        for (int i = yc; i < iData.length; i++) {
            int y = 0, x = (int) (am * (i - yc)), ax = 0;
            do {
                cp[i][ax++] = iData[y][x];
                x++;
                y = (int) Math.round(m * x + i);
            } while (y < iData.length && y > -1 && x < iData[0].length);
        }
    }

    /* no errors! proved don't touch. */
    private void removeIntersection(int[][] iData) {

        /* find x & y intersection */
        int xc = getXIntersection(iData, NREVERSE);
        int yc = getYIntersection(iData, NREVERSE);

        for (int i = 1, ic = yc; ic < iData.length; i++, ic++) {
            for (int j = 10, jc = xc; jc < iData[0].length; j++, jc++) {
                iData[i][j] = iData[ic][jc];
            }
        }
    }
    private int getYIntersection(int[][] iData, int type) {
        for (int i = 0; i < iData.length; i++) {
            for (int j = 0; j < iData[0].length; j++) {
                if (iData[i][j] != 0) {
                    return type == NREVERSE ? i : j;
                }
            }
        }
        return 0;
    }
    private int getXIntersection(int[][] iData, int type) {
        for (int i = 0; i < iData[0].length; i++) {
            for (int j = 0; j < iData.length; j++) {
                if (iData[j][i] != 0) {
                    return type == NREVERSE ? i : j;
                }
            }
        }
        return 0;
    }
    
    /**
     * Resizes a segment to fixed dimensions.
     * 
     * ALGORITHM:
     * 
     * 1. Analyze the density of the pixels foreground to background and vice-versa
     * 2. ldp <-> low density pixel.
     * 3. Create a buffer B of size of required dimensions.
     * 4. Fill B with the high density color.
     * 5. foreach low-density pixel in the original image
     * 6.     fill the rectangle of size [ycoeff X xcoeff] with ldp
     * 7. done.
     * 
     * @param s
     * @return 
     */
    
    public BasicImage resizeImage(int HEIGHT, int WIDTH) {
        
        int height = this.getHeight();
        int width = this.getWidth();
        int fg = this.getForeground();
        int bg = this.getBackground();
        int[][] idata = this.getImageData();
        int[][] data = new int[HEIGHT][WIDTH];
        
        int fp = 0, tp = height * width;
        int ldp = bg, hdp = fg;
        int lx, ly, hx, hy;
        
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                fp += (idata[i][j] == fg) ? 1 : 0;
            }
        }
        
        if(fp == 0) {
            return null;
        }
        
        /*
         * bias here helps where the foreground and backgrounds are in almost
         * same proportion.
         */
        double bias = 1.0;
        if(fp < (tp - fp) * bias) {
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
        
        return new BasicImage(data);
    }
    
}
