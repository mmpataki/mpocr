package mpocr;

/**
 * 
 * @author mmp
 */

class Segment extends BasicImage {

    Features features;
    
    public Segment(int[][] iData) {
        super(iData);
        features = new Features();
    }
    
    public void printImage() {
        for (int i = 0; i < getWidth(); i++)Util.puts("-"); Util.puts("\n");
        super.printimage();
        for (int i = 0; i < getWidth(); i++)Util.puts("-"); Util.puts("\n");
    }

    void segmentImage() {
        features.add(new Zones(this));
    }
}
