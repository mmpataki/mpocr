package mpocr;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;


/**
 * Wrapper around BasicImage and some extensions like binarize, grayscale etc.
 */
class OImage extends BasicImage {

    /* where the image is stored */
    private BufferedImage img;
    private boolean binarized;

    public OImage(String imgpath) {
        
        this.binarized = false;
        
        readImage(imgpath);
        
        iData = new int[img.getHeight() + 2][img.getWidth() + 2];
        for (int x = 0; x < img.getHeight(); x++) {
            for (int y = 0; y < img.getWidth(); y++) {
                int c = (img.getRGB(y, x));
                iData[x + 1][y + 1] = c;
            }
        }
        for (int[] iData1 : iData) {
            iData1[0] = iData1[iData[0].length-1] = 0;
        }
        for (int j = 0; j < iData[0].length; j++) {
            iData[0][j] = iData[iData.length-1][j] = 0;    
        }
    }

    OImage(int[][] xdata) {
        dirty = true;
        iData = xdata;
    }

    /* to read the image from the file at 'path' */
    public final void readImage(String path) {
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

    public boolean isBinarized() {
        return binarized;
    }
    
    /* to binarize the image */
    public void binarize() {
        
        int threshold, ht, wd, bg, fg;
        
        if(isBinarized())
            return;
        
        /* first we need to convert image to grayscale*/
        grayscale();
        exportImage("grayscale");
        
        wd = getWidth();
        ht = getHeight();
        fg = getForeground();
        bg = getBackground();
        threshold = threshold();
        
        for (int i = 0; i < ht; i++) {
            for (int j = 0; j < wd; j++) {
                iData[i][j] = (iData[i][j] < threshold) ? bg : fg;
            }
        }
        dirty = binarized = true;
        exportImage("binarized");
    }

    private int threshold() {
        
        int[] histogram = imageHistogram();
        int total = img.getHeight() * img.getWidth();

        float sum = 0;
        for (int i = 0; i < 256; i++)
            sum += i * histogram[i];

        float sumB = 0;
        int wB = 0;
        int wF;
        float varMax = 0;
        int threshold = 0;

        for (int i = 0; i < 256; i++) {
            
            wB += histogram[i];
            if (wB == 0)
                continue;
            wF = total - wB;

            if (wF == 0)
                break;

            sumB += (float) (i * histogram[i]);
            float mB = sumB / wB;
            float mF = (sum - sumB) / wF;

            float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);

            if (varBetween > varMax) {
                varMax = varBetween;
                threshold = i;
            }
        }

        return threshold;
    }

    private int[] imageHistogram()
    {
        int[] histogram = new int[256];

        for (int i = 0; i < histogram.length; i++)
            histogram[i] = 0;

        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                histogram[iData[i][j] & 0xff]++;
            }
        }
        return histogram;
    }

    /* export image to a file */
    public void exportImage(String path) {
        try {
            File ProcessedImage = new File(path + ".png");

            if(dirty) {
                int wd = getWidth() - 1;
                int ht = getHeight() - 1;
                img = new BufferedImage(wd - 1, ht - 1, BufferedImage.TYPE_INT_RGB);
                for(int i = 1; i < ht; i++) {
                    for(int j = 1; j < wd; j++) {
                        img.setRGB(j - 1, i - 1, iData[i][j]);
                    }
                }
            }
            ImageIO.write(img, "png", ProcessedImage);
        } catch (IOException ex) {
            Logger.getLogger(OImage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /* to invert the color of the image */
    public void invertScale() {
        int fg = getForeground();
        int bg = getBackground();
        for (int i = 0; i < getHeight(); i++) {
            for (int j = 0; j < getWidth(); j++) {
                iData[i][j] = iData[i][j] == fg ? bg : fg;
            }
        }
    }

    private int getLowByte(int word32, int byteno) {
        return ((word32 >> (byteno << 3)) & 0xff);
    }
    
    /**
     * Converts an image into grayscale.
     */
    private void grayscale() {
        int wd = getWidth();
        int ht = getHeight();
        for (int i = 0; i < ht; i++) {
            for (int j = 0; j < wd; j++) {
                int xIntensity = (int) (getLowByte(iData[i][j], 3) * 0.2126 +
                                        getLowByte(iData[i][j], 2) * 0.7152 +
                                        getLowByte(iData[i][j], 0) * 0.0722);
                iData[i][j] = xIntensity;
            }
        }
    }
    
}