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
    
    public void printImageForce() {
        for (int i = 0; i < getWidth(); i++)Util.puts("-"); System.out.print("\n");
        super.printimageforce();
        for (int i = 0; i < getWidth(); i++)Util.puts("-"); System.out.print("\n");
    }

    void extractFeatures() {
        features.add(new Zones(this));
        features.add(new PixelBuffer(this));
    }
}
