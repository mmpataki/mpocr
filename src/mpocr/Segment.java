package mpocr;

/**
 * Wrapper around BasicImage with extension store the features and some methods
 * to manipulate the segments such as bounding, thinning etc.
 */

class Segment extends BasicImage {

    /* collection of features */
    private Features features;
    
    /* spaces before the segment */
    private int prevSpaces;
    
    /* lines next to this segment this is set only for last segment in a line*/
    private int nextLines;
    
    /* detected chracter after OCR. */
    private char detectedChar;
    
    /* numbers to add for getting the neighbours of a pixel in clockwise */
    private static final int[][] neighbours= {
                 {-1,0}, {-1,1},
                         {0, 1},
                         {1, 1},
                 {1, 0},
        {1, -1},
        {0,-1},
        {-1,-1},
                 {-1, 0}
    };
    
    public Segment(int[][] iData) {
        super(iData);
        features = null;
    }
    
    /**
     * Get featureSet with magic number.
     * @param magic : magic number of feature required.
     * @return the featureset with the given magic number
     */
    public IFeatureSet getFeature(int magic) {
        if(features == null)
            extractFeatures();
        return features.get(magic);
    }
    
    private void extractFeatures() {
        features = new Features();
        features.add(new Zones(this));
        features.add(new PixelBuffer(this));
        features.add(new Intersects(this));
        features.add(new GridIntersects(this));
    }

    public int getPrevSpaces() {
        return prevSpaces;
    }
    
    public void setPrevSpaces(int prevSpaces) {
        this.prevSpaces = prevSpaces;
    }
    
    public int getNextLines() {
        return nextLines;
    }
    
    void setNextLines(int nextLines) {
        this.nextLines = nextLines;
    }
    
    public char getDetection() {
        return detectedChar;
    }
    
    public void setDetection(char ch) {
        detectedChar = ch;
    }

    /**
     * Export the current image as an png image.
     * @param: path
     */
    public void exportBinary(String path) {
        int ht, wd;
        int[][] xdata;
        
        ht = getHeight();
        wd = getWidth();
        xdata = new int[ht][wd];
        
        for (int i = 0; i < ht; i++) {
            for (int j = 0; j < wd; j++) {
                xdata[i][j] = -iData[i][j];
            }
        }
        
        OImage img = new OImage(xdata);
        img.exportImage(path);
    }    
    
    /**
     * return a new tighly bounded character of this segment.
     * @return a new bounded segment.
     */
    Segment getBounded() {
        
        int wd, ht, fg;
        int[][] idata;
        
        fg = getForeground();
        wd = getWidth();
        ht = getHeight();
        idata = getImageData();
        
        int top = 0, left = 0, right = wd - 1, bottom = ht - 1;
        while(Util.isemptyLine(idata[top], fg, 0, wd, 0.0)) top++;
        while(Util.isemptyLine(idata[bottom], fg, 0, wd, 0.0)) bottom--;
        while(Util.isemptyCol(idata, top, bottom, left, fg, 0.0)) left++;
        while(Util.isemptyCol(idata, top, bottom, right, fg, 0.0)) right--;

        int[][] bndbox = new int[(bottom - top + 1)][(right - left + 1)];
        
        /* copy data */
        for (int i = top; i <= bottom; i++) {
            for (int j = left; j <= right; j++) {
                bndbox[i - top][j - left] = idata[i][j];
            }
        }

        return new Segment(bndbox);
    }
    
    
    /** BELOW CODE CURRENTLY NOT USED **
    
    public void thin() {
        
        int ht, wd, bg, fg, change, iter;
        boolean[][] pkeep;
        int[][][] iterationVars;
        
        iter = 1;
        change = 0;
        fg = getForeground();
        bg = getBackground();
        ht = getHeight();
        wd = getWidth();
        pkeep = new boolean[ht][wd];
        iterationVars = new int[][][] {
            {{2,4,6}, {4,6,8}},
            {{2,4,8}, {2,6,8}}
        };
        
        do {
            iter = (iter + 1)  % 2;
            for (int i = 0; i < ht; i++) {
                for (int j = 0; j < wd; j++) {
                    
                    if(iData[i][j] != fg)
                        continue;
                    
                    pkeep[i][j] = (
                                neighboursIn2n6(i, j) &&
                                getConnectivity(i, j) == 1 &&
                                prod(i, j, iterationVars[iter][0]) == 0 &&
                                prod(i, j, iterationVars[iter][1]) == 0
                            );
                }
            }
            
            for (int i = 0; i < ht; i++) {
                for (int j = 0; j < wd; j++) {
                    iData[i][j] = pkeep[i][j] ? bg : fg;
                }
            }
            
        } while( change > 0);
        
    }
    
    public void thinx() {
        
        int ht, wd, bg;
        
        bg = getBackground();
        ht = getHeight();
        wd = getWidth();
        
        for (int i = 0; i < ht; i++) {
            for (int j = 0; j < wd; j++) {
                if(
                        getConnectivity(i, j) == 1 &&
                        neighboursIn2n6(i, j) &&
                        anyOneOfFourIsBackground(i, j)
                ) {
                    iData[i][j] = bg;
                }
            }
        }
        
        for (int i = 0; i < ht; i++) {
            for (int j = 0; j < wd; j++) {
                if(
                        getConnectivity(i, j) == 1 &&
                        neighboursIn2n6(i, j) &&
                        anyOneOfFourIsBackground(i, j)
                ) {
                    iData[i][j] = bg;
                }
            }
        }
        
    }
    
    private int getPixel(int y, int x) {
        if(y < 0 || y >= getHeight() || x < 0 || x >= getWidth())
            return getBackground();
        return iData[y][x];
    }
    
    private int getConnectivity(int i, int j) {
        
        int bg, conn, cc, col;
        
        bg = 0;
        col = bg;
        cc = 2;
        conn = 0;
        
        for (int[] n : neighbours) {
            
            col = getPixel(i + n[0], j + n[1]);
            
            if(cc != col) {
                cc = col;
                conn += (cc == bg) ? 1 : 0;
            }
        }
        
        if(getPixel(i + neighbours[0][0], j + neighbours[0][1]) == col && col == bg)
            conn--;
        
        return conn;
    }

    private boolean neighboursIn2n6(int i, int j) {
        int bg, count;
        
        count = 0;
        bg = getBackground();
        
        for (int[] n : neighbours) {
            if(getPixel(i + n[0], j + n[1]) != bg)
                count++;
        }
        return (count >= 2 && count <= 6);
    }

    private boolean anyOneOfFourIsBackground(int i, int j) {
        
        int bg = getBackground();
        return (
                    getPixel(i + -1, j) == bg ||
                    getPixel(i, j - 1) == bg ||
                    getPixel(i, j + 1) == bg ||
                    getPixel(i + 1, j) == bg
               );
    }

    private int prod(int i, int j, int[] array) {
        int p = 0;
        for (int k = 0; k < array.length; k++) {
            p *= getPixel(i + neighbours[array[k]-1][0], j + neighbours[array[k]-1][1]);
        }
        return p;
    }
    /**/
}
