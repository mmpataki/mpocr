/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpocr;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author mmp
 */
public class NNVisualizer extends JPanel {
    
    private class LayerDrawingInfo {
        int fx, x;
        int fy, y;
        int radius;
        int yoff;
        int nc;
        int color;

        private void reset() {
            x = fx;
            y = fy;
        }
    }
    
    private int width, height, padding;
    private int node_margin;
    private int layerheight, layerwidth, layer_margin;
    private int fontsize;
    
    private NeuralNetwork nn;
    private int nlayers;
    
    Color background, pipecolor, layerbackground, fontcolor;
    
    public NNVisualizer() {
        initDefs();
    }
    
    public NNVisualizer(NeuralNetwork nn) {
        initDefs();
        this.nn = nn;
    }
    
    private void initDefs() {
        this.fontcolor= new Color(0x000);
        this.layerbackground = new Color(0xf6f6f6);
        this.background = new Color(0xfafafa);
        this.pipecolor = new Color(0x000000);
        
        this.fontsize = 16;
        this.layer_margin = 3;
        this.node_margin = 15;
        this.padding = 10;
    }
    
    
    public void setNN(NeuralNetwork nn) {
        this.nn = nn;
        repaint();
    }
    
    @Override
    public void paintComponent(Graphics g) {
        
        prepareEnv();

        if(nn == null) {
            return;
        }
        
        LayerDrawingInfo ldi[] = new LayerDrawingInfo[nn.layersCount()];
        
        g.setColor(background);
        g.fillRect(0, 0, width, height);
        
        for (int i = 0; i < nlayers; i++) {
            
            ldi[i] = new LayerDrawingInfo();
            Layer l = nn.getLayer(i);
            
            ldi[i].nc = l.neuronCount();
            ldi[i].color = l.getMagic();
            ldi[i].fx = ldi[i].x = padding + layer_margin;
            ldi[i].fy = ldi[i].y = padding + (i * ((layer_margin * 2) + layerheight));
            ldi[i].radius = Integer.max(
                        Integer.min(
                            (layerheight - (2 * node_margin)),
                            (layerwidth - (2 * ldi[i].nc * node_margin)) / ldi[i].nc
                        ) / 2,
                        3
                    );
            ldi[i].yoff = (layerheight - ((node_margin + ldi[i].radius) * 2)) / 2 + fontsize / 2;
            
            g.setColor(layerbackground);
            g.fillRect(ldi[i].x, ldi[i].y, layerwidth, layerheight);
            g.setFont(new Font("Consolas", Font.BOLD, fontsize - 2));
            g.setColor(fontcolor);
            g.drawString(l.getName(), ldi[i].x, ldi[i].y + fontsize);

            ldi[i].fx = ldi[i].x = (layerwidth - (2 * ldi[i].nc * (ldi[i].radius + node_margin))) / 2 + node_margin;
            
        }
        
        for (int i = 0; i < nlayers; i++) {
            drawLayer(g, ldi[i], (i + 1) < nlayers ? ldi[i+1] : null);
        }
    }

    private void prepareEnv() {
        width = getWidth();
        height = getHeight();
        
        if(nn == null) {
            return;
        }
        
        nlayers = nn.layersCount();
        layerheight = (height - (padding * 2) - (layer_margin * (nlayers + 1))) / nlayers;
        layerwidth = width - (padding * 2) - (layer_margin * 2);
    }

    private void drawLayer(Graphics g, LayerDrawingInfo l1, LayerDrawingInfo l2) {

        l1.y += node_margin;
        
        for (int j = 0; j < l1.nc; j++) {
            
            g.setColor(pipecolor);
            
            if(l2 != null) {
                
                l2.y += node_margin;
                
                for (int k = 0; k < l2.nc; k++) {
                    g.drawLine(
                            l1.x + l1.radius, l1.y + l1.yoff + l1.radius,
                            l2.x + l2.radius, l2.y + l2.yoff + l2.radius
                    );
                    l2.x += (l2.radius + node_margin) * 2;
                }
                l2.reset();
            }
            
            g.setColor(new Color(l1.color));
            g.fillOval(l1.x, l1.y + l1.yoff, l1.radius * 2, l1.radius * 2);
            l1.x += (l1.radius + node_margin) * 2;
        }
    }
}
