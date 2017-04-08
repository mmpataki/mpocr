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
        
        int iht = idata.length - 1;
        int iwd = idata[0].length - 1;
        while(i < iht) {
            
            /* skip the upper empty lines. */
            while(i < iht && isemptyLine(idata[i], fg, excuse)) i++;
            up = i - 1;
            while(i < iht && !isemptyLine(idata[i], fg, excuse)) i++;
            down = i;
            
            j = 0;
            
            /* get the segments in the line range up <-> down */
            while(j < iwd) {
                
                /* skip the left empty lines. */
                while(j < iwd && isemptyCol(idata, up, down, j, fg, excuse)) j++;
                left = j - 1;
                while(j < iwd && !isemptyCol(idata, up, down, j, fg, excuse)) j++;
                right = j;
                
                /* why +1 and not +2 as other images? answer is down-up and
                 * right-left yeild +1 for the expression
                 */
                Segment seg = new Segment(new int[down - up + 1][right - left + 1]);
                for (int k = up + 1; k < down; k++) {
                    System.arraycopy(idata[k], left + 1, seg.iData[k - up], 1, (right - left - 1));
                }
                alist.add(seg);
            }
            
        }
        
        Segment segs[] = new Segment[alist.size()];
        for (int k = 0; k < segs.length; k++) {
            segs[k] = alist.get(k);
            segs[k].printImage();
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
            if(idata[i][col] == fg) {
                fgs++;
            }
        }
        return (((double)fgs / (double)(down-up)) <= excuse);
    }
}
