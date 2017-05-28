package mnn;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *  An extended swing component to visualize the NeuralNetwork attached
 *  to this object.
 */
public class NNVisualizer extends JPanel {
    
    /* some drawing info to reduce the redundant calculations */
    private class LayerDrawingInfo {
        int fx, x;
        int fy, y;
        int radius;
        int yoff;
        int nc, dnc;
        int color;

        private void reset() {
            x = fx;
            y = fy;
        }
    }
    
    /* the drawing state objects */
    private int width, height, padding;
    private int node_margin, textheight;
    private int layerheight, layerwidth, layer_margin;
    private int fontsize;
    private int nlayers;
    private boolean drawlinks = true;
    private NeuralNetwork nn;
    Color background, pipecolor, layerbackground, fontcolor;
    
    
    public NNVisualizer() {
        initDefs();
    }
    
    public NNVisualizer(NeuralNetwork nn) {
        initDefs();
        this.nn = nn;
    }
    
    /* initialises the defaults */
    private void initDefs() {
        this.fontcolor= new Color(0x000);
        this.layerbackground = new Color(0xf6f6f6);
        this.background = new Color(0xfafafa);
        this.pipecolor = new Color(0x606060);
        this.fontsize = 16;
        this.layer_margin = 3;
        this.node_margin = 5;
        this.padding = 5;
        this.textheight = 14;
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
        
        LayerDrawingInfo ldi[] = new LayerDrawingInfo[nn.getLayersCount()];
        
        g.setColor(background);
        g.fillRect(0, 0, width, height);
        
        for (int i = 0; i < nlayers; i++) {
            
            ldi[i] = new LayerDrawingInfo();
            Layer l = nn.getLayer(i);
            
            ldi[i].nc = l.neuronCount();
            ldi[i].dnc = Util.min((ldi[i].nc / 13), ldi[i].nc);
            ldi[i].color = 0x256a49;
            ldi[i].fx = ldi[i].x = padding + layer_margin;
            ldi[i].fy = ldi[i].y = padding + (i * ((layer_margin * 2) + layerheight));
            ldi[i].radius = Util.max(
                        Util.min(
                            (layerheight - (2 * node_margin) - textheight),
                            (layerwidth - (2 * ldi[i].dnc * node_margin) - textheight) / ldi[i].dnc
                        ) / 2,
                        5
                    );
            ldi[i].yoff = (layerheight - ((node_margin + ldi[i].radius) * 2)) / 2 + fontsize / 2;
            
            g.setColor(layerbackground);
            g.fillRect(ldi[i].x, ldi[i].y, layerwidth, layerheight);
            g.setFont(new Font("Consolas", Font.BOLD, fontsize - 2));
            g.setColor(fontcolor);
            g.drawString(("L" + (i+1) + " <" + ldi[i].nc + " neurons>"), ldi[i].x, ldi[i].y + fontsize);

            ldi[i].fx = ldi[i].x = (layerwidth - (2 * ldi[i].dnc * (ldi[i].radius + node_margin))) / 2 + node_margin;
            drawlinks = ldi[i].dnc < maxconns;
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
        
        nlayers = nn.getLayersCount();
        if(nlayers == 0) {
            return;
        }
        layerheight = (height - (padding * 2) - (layer_margin * (nlayers + 1))) / nlayers;
        layerwidth = width - (padding * 2) - (layer_margin * 2);
    }

    int maxconns = 10;
    private void drawLayer(Graphics g, LayerDrawingInfo l1, LayerDrawingInfo l2) {

        l1.y += node_margin;
        
        for (int j = 0; j < l1.dnc; j++) {
            
            g.setColor(pipecolor);
            
            if(l2 != null && drawlinks) {
                
                l2.y += node_margin;
                int quant = Util.max((l2.dnc / maxconns), 1);
                
                for (int k = 0; k < l2.dnc; k += quant) {
                    g.drawLine(
                            l1.x + l1.radius, l1.y + l1.yoff + l1.radius,
                            l2.x + l2.radius, l2.y + l2.yoff + l2.radius
                    );
                    l2.x += quant * (l2.radius + node_margin) * 2;
                }
                l2.reset();
            }
            
            g.setColor(new Color(l1.color));
            
            if(l1.nc > l1.dnc && j == (l1.dnc / 2)) {
                //draw 3 circles of size 5px radius
                
                int vmargin = 5;
                int vrad = 7;//((l1.radius*2) - (3 * vmargin)) / 6;
                int vhspace = ((vrad * 2) + vmargin) * 3;
                int vlmargin = ((l1.radius * 2) - vhspace) / 2;
                for (int b = 0; b < 3; b++) {
                    g.fillOval(l1.x + (b * (2 * vrad + vmargin)) + vlmargin, l1.y + l1.yoff + l1.radius - vrad, vrad * 2, vrad * 2);
                }
            } else {
                g.fillOval(l1.x, l1.y + l1.yoff, l1.radius * 2, l1.radius * 2);
            }
            l1.x += (l1.radius + node_margin) * 2;
        }
    }
}
