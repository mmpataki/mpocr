package mpocr;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is seperated out from MainPage's function for creating a
 * reusable code. This creates a HTML document from the segments recognised.
 */

public class DocumentCreator {

    private String document = "";
    private final String SPACE = "&nbsp;";
    private final String LINEBREAK = "<br/>";
    
    void create(Segment[] segments) {
        
        int lineHeight = 30;//AnalyseLineHeight(segments);
        int spaceWidth = 30;//AnalyseCharWidth(segments);

        for (Segment seg : segments) {
            
            if(seg.getHeight() < 4 || seg.getWidth() < 4) {
                for (int i = 0; i < (seg.getNextLines() / lineHeight); i++) {
                    document += LINEBREAK;
                }
                continue;
            }
            
            for (int i = 0; i < (seg.getPrevSpaces() / spaceWidth); i++) {
                document += SPACE;
            }
            
            document += seg.getDetectedChar();
            
            for (int i = 0; i < (seg.getNextLines() / lineHeight); i++) {
                document += LINEBREAK;
            }
        }
    }

    private int AnalyseLineHeight(Segment[] segments) {

        HashMap<Integer, Integer> map = new HashMap<>();
        
        for (Segment seg : segments) {
            int nls = seg.getNextLines();
            if(nls != 0) {
                if(map.get(nls) == null) {
                    map.put(nls, 0);
                }
                map.put(nls, map.get(nls) + 1);
            }
        }
        
        Integer maxval = 0;
        for (Integer key : map.keySet()) {
            if(map.get(key) > map.get(maxval))
                maxval = key;
        }
        
        return maxval;
    }

    private int AnalyseCharWidth(Segment[] segments) {
        
        HashMap<Integer, Integer> map = new HashMap<>();
        
        for (Segment seg : segments) {
            int nls = seg.getWidth();
            if(nls > 3) {
                if(map.get(nls) == null) {
                    map.put(nls, 0);
                }
                Integer prevval = map.get(nls);
                prevval++;
            }
        }
        
        Integer maxval = 0;
        for (Integer key : map.keySet()) {
            if(map.get(key) > map.get(maxval))
                maxval = key;
        }
        
        return maxval;
    }
    
    public void flush(String file) {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(file);
            pw.write(document);
            pw.flush();
            Runtime.getRuntime().exec("firefox " + file);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DocumentCreator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DocumentCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
}
