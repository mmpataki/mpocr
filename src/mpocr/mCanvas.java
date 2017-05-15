package mpocr;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class mCanvas extends JPanel {

    /* image associated with this mCanvas */
    private OImage oimg;
    
    /* pointer just pointing to the image data of oimg */
    private int[][] iData;
    
    /* drawing varibales */
    private int pixelWidth = 1, pixelHeight = 1, offSet = 0;

    public mCanvas() {
        oimg = null;
    }

    public mCanvas(String file) {
        setImage(file);
    }

    public int getPixelWidth() {
        return pixelWidth;
    }
    
    public int getPixelHeight() {
        return pixelHeight;
    }
    
    public void setPixelSize(int width, int height) {
        pixelHeight = height;
        pixelWidth = width;
        repaint();
    }

    public void setOffset(int offset) {
        this.offSet = offset;
        repaint();
    }
    
    public void setPixel(int y, int x, int color) {
        try {
            iData[++x][++y] = color;
            this.getGraphics().setColor(new Color(iData[y][x]));
            this.getGraphics().fillRect(
                    x * (pixelWidth + offSet),
                    y * (pixelHeight + offSet),
                    pixelWidth,
                    pixelHeight
            );
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void paintComponent(Graphics g) {

        if (iData == null) {
            super.paintComponent(g);
            return;
        }
        
        setSize(
                iData[0].length * (pixelWidth + offSet),
                iData.length * (pixelHeight + offSet)
        );
        super.paintComponent(g);
        
        for (int i = 0; i < iData.length; i++) {
            for (int j = 0; j < iData[0].length; j++) {
                if(oimg.isBinarized()) {
                    g.setColor(new Color(iData[i][j] == 0 ? 0 : 0xffffff));
                } else {
                    g.setColor(new Color(iData[i][j]));
                }
                g.fillRect(
                        j * (pixelWidth + offSet),
                        i * (pixelHeight + offSet),
                        pixelWidth,
                        pixelHeight
                );
            }
        }
    }

    /**
     * reads an image and sets it to this canvas.
     * @param path : path to the image.
     */
    final void setImage(String path) {
        oimg = new OImage(path);
        this.iData = oimg.getImageData();
        repaint();
    }

    
    /**
     * Zooms out by scale scale
     * @param scale: the scale to zoomout.
     */
    public void zoomOut(int scale) {
        if (pixelHeight - scale > 0 && pixelWidth - scale > 0) {
            setPixelSize(pixelWidth - scale, pixelHeight - scale);
        }
    }
    
    /**
     * Zooms in by scale scale
     * @param scale: the scale to zoomin.
     */
    public void zoomIn(int scale) {
        setPixelSize(pixelWidth + scale, pixelHeight + scale);
    }

    /**
     * to binarize the image loaded in this canvas.
     */
    public void binarize() {
        oimg.binarize();
        repaint();
    }

    /**
     * @return the image in this canvas
     */
    public OImage getImage() {
        return oimg;
    }
}
