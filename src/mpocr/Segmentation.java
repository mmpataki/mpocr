package mpocr;

import java.util.ArrayList;

/**
 * A static implementation of segmentation.
 */
class Segmentation {
    
    /**
     * Segments the given binarized image.
     * @param oi: binarized image to be segmented.
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
        int up = 0, down = 0;
        int left, right;
        int i = 0, j;
        double excuse = 0;
        int iht = idata.length - 1;
        int iwd = idata[0].length - 1;
        int lineSpacing = 0, prevSpaces;
        
        /* we need this dummy element for keep track of initial skipped lines */
        alist.add(new Segment(new int[1][1]));
        
        while(i < iht) {
            
            /* find out line spacing and attach it to last Segment */
            while(i < iht && Util.isemptyLine(idata[i], fg, 0, idata[i].length, excuse)) i++;
            lineSpacing = i - up;
            up = i - 1;
            
            alist.get(alist.size() - 1).setNextLines(lineSpacing);
            
            while(i < iht && !Util.isemptyLine(idata[i], fg, 0, idata[i].length, excuse)) i++;
            down = i;
            
            left = j = 0;
            
            /* get the segments in the line range up <-> down */
            while(j < iwd) {
                
                /* skip the left empty lines. */
                while(j < iwd && Util.isemptyCol(idata, up, down, j, fg, excuse)) j++;
                prevSpaces = j - left;
                left = j - 1;
                while(j < iwd && !Util.isemptyCol(idata, up, down, j, fg, excuse)) j++;
                right = j;
                
                Segment seg = new Segment(new int[down - up - 1][right - left - 1]);
                for (int k = up + 1; k < down; k++) {
                    System.arraycopy(idata[k], left + 1, seg.iData[k - up - 1], 0, (right - left - 1));
                }
                seg.setPrevSpaces(prevSpaces);
                alist.add(seg);
            }
        }
        
        Segment segs[] = new Segment[alist.size()];
        for (int k = 0; k < segs.length; k++) {
            segs[k] = alist.get(k);
        }
        alist.clear();
        return segs;
    }
}
