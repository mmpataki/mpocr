package mpocr;

import java.util.ArrayList;

class Segmentation {
    
    public static final int VERSION = 1;
    
    
    /**
     * 
     * @param oi: image data needed to be segmented.
     * @return the segments formed from the given image data
     * 
     *      The idea is to find the linegaps and divide them into lines and
     * dividing the lines at charcpaces. This needs the image to having the
     * characters or text with equal linespacing and equal charspacing.
     * 
     * NOTE: 
     *      The functions in this class requires the image to binarized.
     */
    public static Segment[] segmentImage(OImage oi) {
        
        if(!oi.isBinarized()) {
            Util.puts("Image sent to the segmentation is not binarized");
            return null;
        }
        
        ArrayList<Segment> alist = new ArrayList<>();
        int[][] idata = oi.getImageData();
        int fg = oi.getForeground();
        int up, down;
        int left, right;
        int i = 0, j;
        double excuse = 0;
        
        while(i < idata.length) {
            
            /* skip the upper empty lines. */
            while(i < idata.length && isemptyLine(idata[i], fg, excuse)) i++;
            up = i - 1;
            while(i < idata.length && !isemptyLine(idata[i], fg, excuse)) i++;
            down = i;
            
            j = 0;
            
            /* get the segments in the line range up <-> down */
            while(j < idata[i].length) {
                
                /* skip the left empty lines. */
                while(j < idata[i].length && isemptyCol(idata, up, down, j, fg, excuse)) j++;
                left = j - 1;
                while(j < idata[i].length && !isemptyCol(idata, up, down, j, fg, excuse)) j++;
                right = j;
                
                Segment seg = new Segment(new int[down - up + 2][right - left + 2]);
                for (int k = up; k < down; k++) {
                    System.arraycopy(idata[k], left, seg.iData[k+1], left + 1, right - left);
                }
                alist.add(seg);
            }
            
        }
        
        Segment segs[] = new Segment[alist.size()];
        for (int k = 0; k < segs.length; k++) {
            segs[k] = alist.get(i);
        }
        alist.clear();
        return segs;
    }
    
    /*
     * this checks whether a row(line) given contains any foreground pixels
     * more than the specified percentage returns false if yes otherwise true.
     */
    private static boolean isemptyLine(int[] line, int fg, double excuse) {
        int fgs = 0;
        excuse /= 100.0;
        for (int i = 0; i < line.length; i++) {
            if(line[i] == fg) {
                fgs++;
            }
        }
        return (((double)fgs / (double)line.length) <= excuse);
    }

    /*
     * this checks whether the specified column col in the image idata is
     * having foreground pixel count 
    */
    private static boolean isemptyCol(int[][] idata, int up, int down, int col, int fg, double excuse) {
        int fgs = 0;
        excuse /= 100.0;
        for (int i = up; i < down; i++) {
            if(idata[up][col] == fg) {
                fgs++;
            }
        }
        return (((double)fgs / (double)(down-up)) <= excuse);
    }
}
