package mpocr;

import java.util.Stack;

/**
 * 
 * @author mmp
 */

class Segment extends BasicImage {

    Features features;
    
    public Segment(int[][] iData) {
        super(iData);
    }
    
    public void printImage() {
        for (int i = 0; i < getWidth(); i++)Util.puts("-"); Util.puts("\n");
        super.printimage();
        for (int i = 0; i < getWidth(); i++)Util.puts("-"); Util.puts("\n");
    }
    
    
    
}
