package mpocr;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class mCanvas extends JPanel {

    int[][] iData;
    int pixelWidth = 1, pixelHeight = 1;

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
                System.out.print(iData[i][j]);
            }
        }
    }

    public void setPixel(int y, int x, int color) {
        try {
            iData[x][y] = color;
            this.getGraphics().setColor(new Color(iData[y][x]));
            this.getGraphics().fillRect(
                    x * pixelWidth,
                    y * pixelHeight,
                    pixelWidth,
                    pixelHeight
            );
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (iData == null) {
            return;
        }
        for (int i = 0; i < iData.length; i++) {
            for (int j = 0; j < iData[0].length; j++) {
                g.setColor(new Color(iData[i][j]));
                g.fillRect(
                        j * pixelWidth,
                        i * pixelHeight,
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
            iData = new int[image.getHeight()][image.getWidth()];

            for (int x = 0; x < image.getHeight(); x++) {
                for (int y = 0; y < image.getWidth(); y++) {
                    iData[x][y] = image.getRGB(y, x);
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
}
