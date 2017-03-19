class OImage {

	/* where the image is stored */
	BufferedImage img;

	/* to read the image from the file at 'path' */
	void getImage(String path);

	/* for getting the height and width of the image */
	int getWidth();
	int getHeight();

	/* to get access to the image's RGB data */
	int[][] getImageData();

	/* to rotate the image to given degree */
	void rotate(double degree);

	/* to binarize the image */
	void binarize();

	/* export image to a file */
	void exportImage(String path);

	/* to get the foreground and background color of the image */
	int getForeground();
	int getBackground();

	/* to invert the color of the image */
	void invertScale();

}
