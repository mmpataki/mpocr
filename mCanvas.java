/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package samples;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 *
 * @author staff
 */
public class mCanvas extends JPanel
{
	public mCanvas(String file) {
            
            try {
                
                BufferedImage image = ImageIO.read(new File(file));
                iData = new int[image.getHeight()][image.getWidth()];
                
                for (int x = 0; x < image.getHeight(); x++)
                {
                    for (int y = 0; y < image.getWidth(); y++)
                    {
                        iData[x][y] = image.getRGB(x, y);
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(mCanvas.class.getName()).log(Level.SEVERE, null, ex);
            }
	}

        @Override
	public Dimension getPreferredSize() {
		return new Dimension(250,200);
	}

	int[][] iData;

        @Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		for(int i=0; i<iData.length; i++)
			for(int j=0; j<iData[0].length; j++) {
                                g.setColor(new Color(iData[i][j]));
				g.drawLine(i, j, i, j);
                        }

	}
}