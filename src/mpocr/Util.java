/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpocr;

/**
 *
 * @author mmp
 */
class Util {
    
    /*
     * this method becomes handy when we want to suppress the logs.
     * to suppress just comment the method's body.
     */
    public static void puts(String toput) {
        System.out.print(toput);
    }
    
    /*
     * this checks whether a row(line) given contains any foreground pixels
     * more than the specified percentage returns false if yes otherwise true.
     */
    static boolean isemptyLine(int[] line, int fg, int left, int right, double excuse) {
        int fgs = 0;
        excuse /= 100.0;
        for (int i = left; i < right; i++) {
            if(line[i] == fg) {
                fgs++;
            }
        }
        return (((double)fgs / (double)line.length) <= excuse);
    }

    /*
     * this checks whether the specified column col in the image idata is
     * having foreground pixel count 
    */
    static boolean isemptyCol(int[][] idata, int up, int down, int col, int fg, double excuse) {
        int fgs = 0;
        excuse /= 100.0;
        for (int i = up; i < down; i++) {
            if(idata[i][col] == fg) {
                fgs++;
            }
        }
        return (((double)fgs / (double)(down-up)) <= excuse);
    }
    
    static int minlen(Object[] a, Object[] b) {
        return a.length<b.length?a.length:b.length;
    }

    static int minlen(double[] a, double[] b) {
        return a.length<b.length?a.length:b.length;
    }
}
