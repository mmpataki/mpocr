package mpocr;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

public class mCanvas extends JPanel {

    /* image associated with this mCanvas */
    OImage oimg;
    /* pointer just pointing to the image data of oimg */
    int[][] iData;
    int pixelWidth = 1, pixelHeight = 1, offSet = 0;

    public mCanvas() {
        oimg = null;
    }

    public mCanvas(String file) {
        setImage(file);
    }

    public void setPixelSize(int width, int height) {
        pixelHeight = height;
        pixelWidth = width;
        repaint();
    }

    public int getPixelWidth() {
        return pixelWidth;
    }

    public int getPixelHeight() {
        return pixelHeight;
    }

    public void printMatrix() {
        oimg.printimage();
    }

    public void thin() {
        if(oimg == null)
            return;
        OCRCore.thin1(this); redraw();
    }
    
    public void setOffset(int offset) {
        this.offSet = offset;
        repaint();
    }
    
    public void setPixel(int y, int x, int color) {
        try {
            ++x;
            ++y;
            iData[x][y] = color;
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
                iData[i][j] *= iData[i][j] | 0x88651430;
                g.setColor(new Color(iData[i][j]));
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
                g.setColor(new Color(iData[i][j]));
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

    void zoomOut(int i) {
        if (pixelHeight - i > 0 && pixelWidth - i > 0) {
            setPixelSize(pixelWidth - i, pixelHeight - i);
        }
    }

    void zoomIn(int i) {
        setPixelSize(pixelWidth + i, pixelHeight + i);
    }

    void cover() {
        printMatrix();
        OCRCore.cover(iData, 0x723478, 0);
        repaint();
    }
    
    public void mouseReleased(MouseEvent evt) {
        int x = evt.getX() / (pixelWidth + offSet);
        int y = evt.getY() / (pixelHeight + offSet);
        setPixel(y, x, 783456);
    }

    void binarize() {
        oimg.binarize();
        repaint();
    }
}
