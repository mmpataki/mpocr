package mpocr;

import java.util.ArrayList;
import java.util.Stack;

/**
 * 
 * @author mmp
 */

class Segment extends BasicImage {

    FeatureSet features;
    
    public Segment(int[][] iData) {
        super(iData);
    }
    
    public void printImage() {
        for (int i = 0; i < getWidth(); i++)Util.puts("-"); Util.puts("\n");
        super.printimage();
        for (int i = 0; i < getWidth(); i++)Util.puts("-"); Util.puts("\n");
    }
    
    final int tr = 0;
    final int r  = 1;
    final int br = 2;
    final int b  = 3;
    final int bl = 4;
    final int[][] diradds = { {-1, 1}, {0, 1}, {1, 1}, {1, 0}, {1, -1} };
    final String[] dirs = {"TR", "RT", "BR", "BT", "BL"};
    
    int fg, bg;
    void extractFeatures() {
        
        int[][] cpd = iData.clone();
        fg = getForeground();
        bg = getBackground();
        
        for (int i = 0; i < cpd.length; i++) {
            for (int j = 0; j < cpd[0].length; j++) {
                if(cpd[i][j] == fg) {
                    getfeatures(iData, i, j, r);
                }
            }
        }
        System.exit(0);
    }
    
    Stack<Integer> stk = new Stack<>();
    
    void getfeatures(int[][] ar, int y, int x, int dir) {

        stk.push(dir);
        ar[y][x] = bg;
        
        Util.puts(dirs[dir] + " => ");
        
        final int X = 1, Y = 0;
        
        /* try to construct a the same line your parent wished */
        for (int i = 0; i < 5; i++) {
            int j = (dir + i) % 5;
            
            int gx = gencoord(ar, x, j, X);
            int gy = gencoord(ar, y, j, Y);
            
            if(ar[gy][gx] == fg) {
                getfeatures(ar, gy, gx, j);
                Util.puts("      ");
            }
        }
        Util.puts("\n");
    }

    private int gencoord(int[][] ar, int x, int j, int axisid) {
        if(!((x + diradds[j][axisid]) > ar[0].length || x < 0))
            x += diradds[j][axisid];
        return x;
    }
    
}
