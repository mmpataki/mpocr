package mpocr;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

class OImage implements IImage {

    /* where the image is stored */
    private BufferedImage img;
    /* this one holds the buffered version of the idata. */
    private int[][] iData = null;
    /* to check if the buffer is dirty. */
    private boolean dirty = false;

    /* to read the image from the file at 'path' */
    public void readImage(String path) {
        try {
            File f = new File(path);
            if (!f.exists()) {
                return;
            }
            img = ImageIO.read(f);
        } catch (IOException ex) {
            Logger.getLogger(OImage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /* for getting the height and width of the image */
    @Override
    public int getWidth() {
        return img.getWidth();
    }

    @Override
    public int getHeight() {
        return img.getHeight();
    }

    /* to get access to the image's RGB data */
    @Override
    public int[][] getImageData() {
        if (iData != null) {
            return iData;
        }
        iData = new int[getHeight() + 2][getWidth() + 2];
        for (int x = 0; x < getHeight(); x++) {
            for (int y = 0; y < getWidth(); y++) {
                int c = (img.getRGB(y, x));
                iData[x + 1][y + 1] = c;
            }
        }
        return iData;
    }

    /* to binarize the image */
    public void binarize() {
        for (int i = 1; i < getHeight(); i++) {
            for (int j = 1; j < getWidth(); j++) {
                int c = iData[i][j];
                int r = (c & 0xff);
                int g = (c & 0xff00) >> 8;
                int b = (c & 0xff0000) >> 16;
                iData[i][j] = (((r + g + b) / 3) > 128) ? 0 : -1;
            }
        }
    }

    /* export image to a file */
    public void exportImage(String path) {

    }

    /* to get the foreground and background color of the image */
    @Override
    public int getForeground() {
        return 1;
    }

    @Override
    public int getBackground() {
        return 0;
    }

    /* to invert the color of the image */
    public void invertScale() {

    }

    private static int NREVERSE = 0;
    private static int REVERSE = 1;

    /* to rotate the image to given degree */
    private static void rotate(int[][] idata, double deg) {

        double m = (double) Math.tan(deg * Math.PI / 180);
        double am = Math.abs(m);
        int mx = (int) Math.round((double) 1 / am * idata[0].length);
        int my = (int) Math.round((double) 1 / am * idata.length);

        System.out.println(mx + ", " + my);
        System.out.println(idata[0].length + ", " + idata.length);

        int[][] cp = new int[my][mx];

        for (int i = 0; i < my; i++) {
            for (int j = 0; j < mx; j++) {
                cp[i][j] = 0;
            }
        }
        int yc = getXIntersection(idata, REVERSE);
        for (int i = yc; i < idata.length; i++) {
            int y = 0, x = (int) (am * (i - yc)), ax = 0;
            do {
                cp[i][ax++] = idata[y][x];
                x++;
                y = (int) Math.round(m * x + i);
            } while (y < idata.length && y > -1 && x < idata[0].length);
        }
    }

    private static void removeIntersection(int[][] idata) {

        /* find x & y intersection */
        int xc = getXIntersection(idata, NREVERSE);
        int yc = getYIntersection(idata, NREVERSE);

        System.out.println(xc + ", " + yc + " " + idata.length);

        for (int i = 1, ic = yc; ic < idata.length; i++, ic++) {
            for (int j = 10, jc = xc; jc < idata[0].length; j++, jc++) {
                idata[i][j] = idata[ic][jc];
            }
        }
    }

    private static int getYIntersection(int[][] idata, int type) {
        for (int i = 0; i < idata.length; i++) {
            for (int j = 0; j < idata[0].length; j++) {
                if (idata[i][j] != 0) {
                    return type == NREVERSE ? i : j;
                }
            }
        }
        return 0;
    }

    private static int getXIntersection(int[][] idata, int type) {
        for (int i = 0; i < idata[0].length; i++) {
            for (int j = 0; j < idata.length; j++) {
                if (idata[j][i] != 0) {
                    return type == NREVERSE ? i : j;
                }
            }
        }
        return 0;
    }
}
