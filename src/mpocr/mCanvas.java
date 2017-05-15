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

    public void redraw() {
        Graphics g = getGraphics();
        for (int i = 0; i < iData.length; i++) {
            for (int j = 0; j < iData[0].length; j++) {
                g.setColor(new Color(iData[i][j] == 0 ? 0 : 0xffffff));
                g.fillRect(
                        j * (pixelWidth + offSet),
                        i * (pixelHeight + offSet),
                        pixelWidth,
                        pixelHeight
                );
            }
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
                g.setColor(new Color(iData[i][j] == 0 ? 0 : 0xffffff));
                g.fillRect(
                        j * (pixelWidth + offSet),
                        i * (pixelHeight + offSet),
                        pixelWidth,
                        pixelHeight
                );
            }
        }
    }

    final void setImage(String path) {
        oimg = new OImage(path);
        this.iData = oimg.getImageData();
        repaint();
    }

    public void zoomOut(int i) {
        if (pixelHeight - i > 0 && pixelWidth - i > 0) {
            setPixelSize(pixelWidth - i, pixelHeight - i);
        }
    }
    
    public void zoomIn(int i) {
        setPixelSize(pixelWidth + i, pixelHeight + i);
    }

    void binarize() {
        oimg.xbinarize();
        repaint();
    }

    public OImage getImage() {
        return oimg;
    }
}
