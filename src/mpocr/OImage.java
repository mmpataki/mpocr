package mpocr;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

class OImage extends BasicImage {

    /* where the image is stored */
    private BufferedImage img;
    private boolean binarized;

    public OImage(String imgpath) {
        this.binarized = false;
        
        readImage(imgpath);
        
        iData = new int[iData.length + 2][iData[0].length + 2];
        for (int x = 0; x < iData.length; x++) {
            for (int y = 0; y < iData[0].length; y++) {
                int c = (img.getRGB(y, x));
                iData[x + 1][y + 1] = c;
            }
        }
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
        for (int i = 1; i < getHeight(); i++) {
            for (int j = 1; j < getWidth(); j++) {
                int c = iData[i][j];
                int r = (c & 0xff);
                int g = (c & 0xff00) >> 8;
                int b = (c & 0xff0000) >> 16;
                iData[i][j] = (((r + g + b) / 3) > 128) ? 0 : -1;
            }
        }
        binarized = true;
    }

    /* export image to a file */
    public void exportImage(String path) {

    }

    /* to invert the color of the image */
    public void invertScale() {

    }

}
