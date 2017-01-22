package mpocr;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class mCanvas extends JPanel {

    int[][] iData;
    int pixelWidth = 1, pixelHeight = 1, offSet = 0;

    public mCanvas() {
        iData = null;
    }

    public mCanvas(String file) {
        readImage(file);
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
        for (int i = 0; i < iData.length; i++) {
            System.out.println();
            for (int j = 0; j < iData[0].length; j++) {
                if(iData[i][j] == 0) {
                    System.out.print("#  ");
                } else {
                    System.out.print(iData[i][j] + ", ");
                }
            }
        }
        System.out.println();
    }

    public void thin() {
        if(iData == null)
            return;
        OCRCore.thin1(this);
        redraw();
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

    void setImage(String path) {
        readImage(path);
        repaint();
    }

    private void readImage(String file) {
        try {

            File f = new File(file);
            if (!f.exists()) {
                return;
            }

            BufferedImage image = ImageIO.read(f);
            iData = new int[image.getHeight() + 2][image.getWidth() + 2];

            for (int i = 0; i < iData.length; i++) {
                for (int j = 0; j < iData[0].length; j++) {
                    iData[i][j] = -1;
                }
            }
            
            for (int x = 0; x < image.getHeight(); x++) {
                for (int y = 0; y < image.getWidth(); y++) {
                    iData[x + 1][y + 1] = (image.getRGB(y, x) != -1) ? 0 : -1;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(mCanvas.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    
}
