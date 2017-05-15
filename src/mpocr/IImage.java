package mpocr;

/**
 * A must interface for all Image and Segmement classes.
 */
public interface IImage {

    /* for getting the height and width of the image */
    int getWidth();
    int getHeight();

    /* to get access to the image's RGB data */
    int[][] getImageData();

    /* to get the foreground and background color of the image */
    int getForeground();
    int getBackground();
}

