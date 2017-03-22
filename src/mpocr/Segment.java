package mpocr;

class Segment implements IImage {

    int[][] segdata;
    FeatureSet features;

    /* from the IImage interface */
    @Override
    public int[][] getImageData() {
        return null;
    }

    @Override
    public int getWidth() {
        return segdata[0].length;
    }

    @Override
    public int getHeight() {
        return segdata.length;
    }

    @Override
    public int getForeground() {
        return 0;
    }

    @Override
    public int getBackground() {
        return -1;
    }

    public void printSeg() {
        
    }

    public void rotate(double degree) {
        
    }
}
