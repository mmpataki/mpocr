package mpocr;

/**
 * 
 * @author mmp
 */

class Segment extends BasicImage {

    Features features;
    int prevSpaces;
    int prevLines;
    boolean isUpperCase;
    char detectedChar;
    
    public Segment(int[][] iData) {
        super(iData);
        isUpperCase = true;
        features = new Features();
    }
    
    public void printImage() {
        for (int i = 0; i < getWidth(); i++)Util.puts("-"); Util.puts("\n");
        super.printimage();
        for (int i = 0; i < getWidth(); i++)Util.puts("-"); Util.puts("\n");
    }
    
    public void printImageForce() {
        for (int i = 0; i < getWidth(); i++)Util.puts("-"); System.out.print("\n");
        super.printimageforce();
        for (int i = 0; i < getWidth(); i++)Util.puts("-"); System.out.print("\n");
    }

    public void extractFeatures() {
        features.add(new Zones(this));
        features.add(new PixelBuffer(this));
        features.add(new Intersects(this));
        features.add(new GridIntersects(this));
    }

    public int getPrevSpaces() {
        return prevSpaces;
    }
    
    public int getPrevLines() {
        return prevLines;
    }
    
    public void setDetection(char ch) {
        detectedChar = ch;
    }
    
    public char getDetectedChar() {
        return detectedChar;
    }
    
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
        /*
         * I don't think these are necessary.
         * while(Util.isemptyCol(idata, top, bottom, left, fg, 0.0)) left++;
         * while(Util.isemptyCol(idata, top, bottom, right, fg, 0.0)) right--;
         */

        int[][] bndbox = new int[(bottom - top + 1)][(right - left + 1)];
        
        /* copy data */
        for (int i = top; i <= bottom; i++) {
            for (int j = left; j <= right; j++) {
                bndbox[i - top][j - left] = idata[i][j];
            }
        }

        /* set some flags which can help in getting rid of some ambiguities */
        isUpperCase = !(((ht - bottom) >= (ht / 4)) || (top > (ht / 4)));
        
        
        
        return new Segment(bndbox);
    }
}
