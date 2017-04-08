package mpocr;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

class OImage extends BasicImage {

    /* where the image is stored */
    private static BufferedImage img;
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
        
        /* first we need to convert image to grayscale*/
        grayscale(img);
        exportImage("grayscale.jpg");
        int red;
        int newPixel;
        int threshold = Treshold( );
        
        for(int i=0; i<img.getWidth(); i++) {
        for(int j=0; j<img.getHeight(); j++) {
 
            // Get pixels
            red = new Color(img.getRGB(i, j)).getRed();
            int alpha = new Color(img.getRGB(i, j)).getAlpha();
            if(red > threshold) {
                newPixel = 255;
            }
            else {
                newPixel = 0;
            }
            newPixel = colorToRGB(alpha, newPixel, newPixel, newPixel);
            img.setRGB(i, j, newPixel); 
 
        }
    }
        
 binarized = true;
 exportImage("binarized.jpg");
    }
    private static int colorToRGB(int alpha, int red, int green, int blue) {
 
        int newPixel = 0;
        newPixel += alpha;
        newPixel = newPixel << 8;
        newPixel += red; newPixel = newPixel << 8;
        newPixel += green; newPixel = newPixel << 8;
        newPixel += blue;
 
        return newPixel;
 
    }
    private static int Treshold( ) {
     int[] histogram = imageHistogram();
    int total = img.getHeight() * img.getWidth();
 
    float sum = 0;
    for(int i=0; i<256; i++) sum += i * histogram[i];
 
    float sumB = 0;
    int wB = 0;
    int wF = 0;float varMax = 0;
    int threshold = 0;
 
    for(int i=0 ; i<256 ; i++)
    {
        wB += histogram[i];
        if(wB == 0) continue;
        wF = total - wB;
 
        if(wF == 0) break;
 
        sumB += (float) (i * histogram[i]);
        float mB = sumB / wB;
        float mF = (sum - sumB) / wF;
 
        float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);
 
        if(varBetween > varMax) 
        {
            varMax = varBetween;
            threshold = i;
        }
    }
 
    return threshold;}
    
    
    
    private static int[] imageHistogram() 
     
    {
 
        int[] histogram = new int[256];
 
        for(int i=0; i<histogram.length; i++) histogram[i] = 0;
 
        for(int i=0; i<img.getWidth(); i++) {
            for(int j=0; j<img.getHeight(); j++) {
                int red = new Color(img.getRGB (i, j)).getRed();
                histogram[red]++;
            }
        }
 
        return histogram;
 
    }

    /* export image to a file */
    public void exportImage(String path) {
        try {
            File ProcessedImage=new File(path);
            ImageIO.write(img, "jpg", ProcessedImage);
        } catch (IOException ex) {
            Logger.getLogger(OImage.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /* to invert the color of the image */
    public void invertScale() {

    }
    
    public void grayscale(BufferedImage Image)
    {
        for(int i=0;i<getWidth();i++){
          for(int j=0;j<getHeight();j++){
              //Using awt Colors
               Color c = new Color(Image.getRGB(j, i));
                int red = (int)(c.getRed() * 0.2126);
               int green = (int)(c.getGreen() * 0.7152);
               int blue = (int)(c.getBlue() *0.0722);
               Color newColor = new Color(red+green+blue,red+green+blue,red+green+blue);
              Image.setRGB(j,i,newColor.getRGB());
          }
        }
        
    }


}
        
    



