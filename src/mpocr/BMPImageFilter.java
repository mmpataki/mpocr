package mpocr;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class BMPImageFilter extends FileFilter {

    @Override
    public boolean accept(File f) {
        String s = f.getAbsolutePath();
        if(!s.contains("."))
            return true;
        if(".bmp".equals(s.substring(s.lastIndexOf('.')))) {
            return true;
        }
        return false;
    }

    @Override
    public String getDescription() {
        return "Select BMP files";
    }
    
}
