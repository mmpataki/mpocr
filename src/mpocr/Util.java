package mpocr;

import java.awt.Component;
import java.io.File;
import javax.swing.JFileChooser;

/**
 * Some useful, really handy functions provided as static.
 */
class Util {

    /*
     * this method becomes handy when we want to suppress the logs.
     * to suppress just comment the method's body.
     */
    public static void puts(String toput) {
        //System.out.print(toput);
    }

    /**
     * A file chooser dialog creator.
     * @param chooseDirectory : true is want to choose a directory.
     * @param parent : The parent frame invoking this dialog eg. MainPage.this
     * @param title : The title of the dialog.
     * @return the path to selected file.
     */
    public static String choseFile(boolean chooseDirectory, Component parent, String title) {
        String path = "";
        JFileChooser ch = new JFileChooser();
        ch.setDialogTitle(title);

        if (chooseDirectory) {
            ch.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        }
        if (ch.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            File file = ch.getSelectedFile();
            path = file.getAbsolutePath();
        }
        return path;
    }

    /*
     * this checks whether a row(line) given contains any foreground pixels
     * more than the specified percentage returns false if yes otherwise true.
     */
    static boolean isemptyLine(int[] line, int fg, int left, int right, double excuse) {
        int fgs = 0;
        excuse /= 100.0;
        for (int i = left; i < right; i++) {
            if (line[i] == fg) {
                fgs++;
            }
        }
        return (((double) fgs / (double) line.length) <= excuse);
    }

    /*
     * this checks whether the specified column col in the image idata is
     * having foreground pixel count 
     */
    static boolean isemptyCol(int[][] idata, int up, int down, int col, int fg, double excuse) {
        if (up < 0 || down < 0) {
            return false;
        }
        int fgs = 0;
        excuse /= 100.0;
        for (int i = up; i < down; i++) {
            if (idata[i][col] == fg) {
                fgs++;
            }
        }
        return (((double) fgs / (double) (down - up)) <= excuse);
    }

    /**
     * minimum of lengths of two arrays.
     * @param a : array1
     * @param b : array2
     * @return minimum length
     */
    static int minlen(Object[] a, Object[] b) {
        return a.length < b.length ? a.length : b.length;
    }

    static int minlen(double[] a, double[] b) {
        return a.length < b.length ? a.length : b.length;
    }
    
    
    public static double min(double a, double b) {
        return (a < b) ? a : b;
    }
    
    public static int min(int a, int b) {
        return (a < b) ? a : b;
    }

    static int max(int a, int b) {
        return (a < b) ? b : a;
    }
    
}
