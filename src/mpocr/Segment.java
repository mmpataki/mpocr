class Segment implements IImage {

	int[][] segdata;
	FeatureSet features;
	
	/* from the IImage interface */
	int[][] getImageData();
	int getWidth();
	int getHeight();
	int getForeground();
	int getBackground();

}
