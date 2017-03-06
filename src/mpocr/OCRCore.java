package mpocr;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;

class Pixel {

    boolean horRem;
    boolean verRem;

    Pixel() {
        horRem = verRem = false;
    }
}

class Degree {

    int top, down, left, right;
    int height, width;
    int regionNum;

    Degree(int rno, int height, int width) {
        top = down = left = right = 1;
        this.height = height;
        this.width = width;
        regionNum = rno;
    }

    void eval(int i, int j) {
        if (i == 0) {
            top = 0;
        }
        if (j == 0) {
            left = 0;
        }
        if (i == height) {
            height = 0;
        }
        if (j == width) {
            width = 0;
        }
    }

    public int get() {
        return (top + down + right + left);
    }
}

public class OCRCore {

    static final int HOMOGENIOUS_EXPANSION = 1;
    static final int HETEROGENIOUS_EXPANSION = 2;

    static boolean[][] visited;

    public static void cskew(mCanvas c) {

        int[][] idata = c.iData;
        int[] hist = new int[idata.length];
        int max = 0;

        for (int i = 0; i < idata.length; i++) {
            int cnt = 0;
            for (int j = 0; j < idata[i].length; j++) {
                if (idata[i][j] != -1) {
                    cnt++;
                }
            }
            if(max < cnt)
                max = cnt;
            hist[i] = cnt;
        }

        histout(hist, max, 4);
    }

    private static void histout(int[] hist, int max, int factor) {
        try {
            PrintWriter pw = new PrintWriter("hist.html");
            pw.append("<html><head><script src=\"mchartjs.js\"></script><link href=\"mchartjs.css\" rel=\"stylesheet\" type=\"text/css\" ></head>");
            pw.append("<div class=\"mchart\" style='width: 90%; height: 90%' gdata='{\"type\": \"region\", \"ydivs\": " + (max/factor) + ", \"xdivs\": " + (hist.length/factor) + ", \"vgrids\": true, \"hgrids\": true, \"datasets\": [{\"fill\": \"skyblue\", \"data\": ");
            pw.append(Arrays.toString(hist));
            pw.append("}]}'></div></html>");
            pw.flush();
            try {
                Process p = Runtime.getRuntime().exec("firefox hist.html");
                p.waitFor();
            } catch (IOException | InterruptedException e) {
                System.out.println("firefox couldn't be opened");
            }
        } catch (IOException ex) {
            Logger.getLogger(OCRCore.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /* mCanvas is parameter as we want to color the canvas in runtime to analyze the algorithm */
    public static void thin1(mCanvas canvas) {

        /*
         * find all the empty regions 
         * 
         * Each region is identified by the set of indices (x, y) in the 
         * image matrix and stored in a hash table of size = Iw * Ih
         * where Iw = Image width
         * and   Ih = Image height
         *
         * We try to expand the regions as possible by replacing the boundary
         * pixels (the pixels which constitute the character image) without 
         * intersecting the other regions
         *
         * Here goes the algorithm
         *
         *  REGION_EXPANSION(A[0..M][0..N])
         *  1. find out all the regions and mark them with numbers > 0
         *  2. sort the regions according to the degree of coverage
         *     (Degree of coverage : number of directions the region is
         *      covered from 0) in descending order.
         *  3. foreach degree d (DH to DL)
         *  5.     expand until it has homogenious boundaries
         *  6. foreach degree d (DH to DL)
         *  7.     expand until possible
         */
        ArrayList<Degree> regionList = findRegions(canvas);

        canvas.printMatrix();
        visited = new boolean[canvas.iData.length][canvas.iData[0].length];

        regionList.stream().forEach((is) -> {
            expand_region(canvas, is.regionNum, HOMOGENIOUS_EXPANSION);
        });

        regionList.stream().forEach((is) -> {
            expand_region(canvas, is.regionNum, HETEROGENIOUS_EXPANSION);
        });

        canvas.printMatrix();
    }

    private static void expand_rec(mCanvas c, int i, int j, int col, int type) {
        try {
            if (i < 2 || j < 2 || i > (c.iData.length - 2) || j > (c.iData[0].length - 2) || c.iData[i][j] != col || visited[i][j]) {
                return;
            }

            visited[i][j] = true;

            /* trace the window and check where you can expand */
            int indices[][] = {
                {-1, -1}, {-1, 0}, {-1, 1},
                {0, 1},
                {1, 1},
                {1, 0},
                {1, -1},
                {0, -1}
            };
            int m = 0, n = 0;

            for (int k = 0; k < indices.length; k++) {

                m = i + indices[k][0];
                n = j + indices[k][1];

                if (c.iData[m][n] == 0) {

                    int ncolors = 0, maxcolors = 4;

                    if (type == HOMOGENIOUS_EXPANSION) {
                        maxcolors = 3;
                    }

                    /* get the start of a color */
                    int assumed_color = c.iData[m + indices[0][0]][n + indices[0][1]];
                    int l;

                    for (l = 0; l < 8; l++) {
                        if (c.iData[m + indices[l][0]][n + indices[l][1]] != assumed_color) {
                            break;
                        }
                    }

                    /* Now count the number of color boundaries */
                    assumed_color = -1;
                    for (int h = 0; h < 8; h++) {
                        int r = (h + l) % 8;
                        r = c.iData[m + indices[r][0]][n + indices[r][1]];
                        if (r != assumed_color) {
                            assumed_color = r;
                            ncolors++;
                        }
                    }

                    /* Now check how many neighbours does it have if only
                       one you cannot steal that point from image */
                    int neighbours = 0;
                    boolean continous = true;
                    for (int o = 0; o < 8; o++) {
                        if (c.iData[m + indices[o][0]][n + indices[o][1]] == 0) {
                            neighbours++;
                        }
                    }

                    if ((c.iData[m - 1][n] == 0 && c.iData[m + 1][n] == 0)
                            || (c.iData[m][n - 1] == 0 && c.iData[m][n + 1] == 0)) {
                        continous = false;
                    }

                    if (ncolors < maxcolors && neighbours > 1 && !continous) {
                        c.iData[m][n] = col;
                        c.printMatrix();
                    }
                }
            }
            for (int k = 0; k < 8; k++) {
                expand_rec(c, m + indices[k][0], n + indices[k][1], col, type);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static void expand_region(mCanvas c, int rNum, int type) {

        for (boolean[] visited1 : visited) {
            for (int j = 0; j < visited[0].length; j++) {
                visited1[j] = false;
            }
        }
        for (int i = 2; i < c.iData.length - 2; i++) {
            for (int j = 2; j < c.iData[0].length - 2; j++) {
                if (c.iData[i][j] == rNum && !visited[i][j]) {
                    expand_rec(c, i, j, rNum, type);
                }
            }
        }
    }

    private static ArrayList<Degree> findRegions(mCanvas c) {

        int col = 2;
        ArrayList<Degree> regions = new ArrayList<>();
        for (int i = 0; i < c.iData.length; i++) {
            for (int j = 0; j < c.iData[0].length; j++) {
                if (c.iData[i][j] == -1) {
                    Degree degree = new Degree(col, c.iData.length, c.iData[0].length);
                    floodFill(c.iData, i, j, col, degree);
                    regions.add(degree);
                    col++;
                }
            }
        }
        regions.sort(new ComparatorImpl());
        return regions;
    }

    private static void floodFill(int[][] iData, int i, int j, int col, Degree degree) {

        if ((i >= iData.length)
                || (i < 0)
                || (j >= iData[0].length)
                || (j < 0)
                || (iData[i][j] != -1)) {
            return;
        }
        degree.eval(i, j);
        iData[i][j] = col;// * (col | 0x88651430);
        floodFill(iData, i, j - 1, col, degree);
        floodFill(iData, i - 1, j, col, degree);
        floodFill(iData, i, j + 1, col, degree);
        floodFill(iData, i + 1, j, col, degree);
    }

    public static void cover(int[][] iData, int markerCol, int mainCol) {

        int xmin = -1, xmax = -1, ymin = -1, ymax = -1;

        for (int i = 0; i < iData.length - 1; i++) {
            for (int j = 0; j < iData[0].length - 1; j++) {
                if (iData[i][j] == mainCol) {
                    ymin = i;
                    break;
                }
            }
            if (ymin != -1) {
                break;
            }
        }

        for (int i = iData.length - 1; i > 0; i--) {
            for (int j = 0; j < iData[0].length; j++) {
                if (iData[i][j] == mainCol) {
                    ymax = i;
                    break;
                }
            }
            if (ymax != -1) {
                break;
            }
        }

        for (int j = 0; j < iData[0].length - 1; j++) {
            for (int i = 0; i < iData.length - 1; i++) {
                if (iData[i][j] == mainCol) {
                    xmin = j;
                    break;
                }
            }
            if (xmin != -1) {
                break;
            }
        }

        for (int j = iData[0].length - 1; j > 0; j--) {
            for (int i = 0; i < iData.length - 1; i++) {
                if (iData[i][j] == mainCol) {
                    xmax = j;
                    break;
                }
            }
            if (xmax != -1) {
                break;
            }
        }

        /* color it */
        for (int i = xmin + -1; i <= xmax + 1; i++) {

            iData[ymin + -1][i] = markerCol;
            iData[ymax + 1][i] = markerCol;
        }

        for (int i = ymin + -1; i <= ymax + 1; i++) {
            iData[i][xmin + -1] = markerCol;
            iData[i][xmax + 1] = markerCol;
        }

    }

    private static class ComparatorImpl implements Comparator<Degree> {

        @Override
        public int compare(Degree o1, Degree o2) {
            if (o1.get() < o2.get()) {
                return 1;
            }
            if (o1.get() == o2.get()) {
                return 0;
            }
            return -1;
        }
    }

}
