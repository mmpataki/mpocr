package mpocr;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

class PlotLayer {
    String fill, stroke, cfill = "white";
    int cradius = 3;
    Vector<Double> data;

    public PlotLayer(String color, String stroke) {
        this.fill = color;
        this.stroke = stroke;
        this.data = new Vector<>();
    }
    
    public String toString() {
        return "{" +
                    "\"fill\": \"" + fill + "\"," +
                    "\"cfill\": \"" + cfill + "\"," +
                    "\"stroke\": \"" + stroke + "\"," +
                    "\"cradius\": " + cradius + "," +
                    "\"data\": " + data +
                "}";
    }
    public void add(Double d) {
        data.addElement(d);
    }
    public int size() {
        return data.size();
    }
    public void flush() {
        data.clear();
    }
}

public class Plotter {
    
    public static final int REGION = 0;
    public static final int BAR = 1;
    public static final int DOT = 2;
    private String[] types = {
        "region", "bar", "dot"
    };
    
    String ofile, cpath, back="transparent", lcolor = "skyblue", stroke = "gray", lstroke = "black";
    int type = REGION;
    int choosen = 0;
    boolean vgrids = true, hgrids = true;
    int ydivs, xdivs, maxx=0; 
    double miny=0, maxy=0;
    Vector<PlotLayer> dlist;
    
    Plotter(String outfile, String relativepath) {
        ofile = outfile;
        cpath = relativepath;
        dlist = new Vector<>();
        dlist.add(new PlotLayer(lcolor, lstroke));
    }

    /**
     * Adds a layer to the plot and selects the layer
     * @param lcolor: New layer's color
     */
    public void addLayer(String lcolor) {
        dlist.add(new PlotLayer(lcolor, lstroke));
        chooseLayer(choosen + 1);
    }
    
    public void flush() {
        for (PlotLayer plotLayer : dlist) {
            plotLayer.flush();
        }
    }

    public void setColor(String color) {
        dlist.get(choosen).fill = color;
    }
    
    public void chooseLayer(int layer) {
        if(layer < 0 || layer >= dlist.size())
            return;
        choosen = layer;
    }
    
    public void addPoint(double d) {
        dlist.get(choosen).add(d);
        if(dlist.get(choosen).size() > maxx)
            maxx++;
        miny = (d > miny) ? miny : d;
        maxy = (d < maxy) ? maxy : d;
    }
    
    private double aMax(Double[] a) {
        Double m = a[0];
        for (int i = 1; i < a.length; i++) {
            if(m < a[i])
                m = a[i];
        }
        return m;
    }
    
    public void plot() {
        plot(10, type);
    }
    
    
    public void plot(int factor, int type) {
        
        //calculate xdivs and ydivs
        xdivs = Integer.min(60, maxx);
        ydivs = Integer.min(20,  Integer.max((int)(maxy-miny), 20));
        
        try {
            String html = "<html><head><script src=\"" + cpath + "mchartjs.js\"></script><link href=\"" + cpath + "mchartjs.css\" rel=\"stylesheet\" type=\"text/css\" ></head><div class=\"mchart\" style='width: 95%; height: 90%' gdata='" + this.toString() + "'></div></html>";
            (new PrintWriter(ofile)).append(html).flush();
            Runtime.getRuntime().exec("firefox " + ofile);
        } catch (IOException ex) {
            Logger.getLogger(Plotter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public String toString() {
        return 
                "{" +
                    "\"type\": \""+ types[type] +"\"," +
                    "\"stroke\": \"" + stroke + "\"," +
                    "\"background\": \"" + back + "\"," +
                    "\"ydivs\": " + ydivs + "," + 
                    "\"xdivs\": " + xdivs + "," + 
                    "\"vgrids\": " + vgrids + "," +
                    "\"hgrids\": " + hgrids + "," +
                    "\"datasets\": " + dlist + "" +
                "}";
    }

    void setType(int type) {
        this.type = type % 3;
    }
}