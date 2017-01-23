package mpocr;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

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
            if (i < 2 || j < 2 || i > (c.iData.length - 4) || j > (c.iData[0].length - 4) || c.iData[i][j] != col) {
                return;
            }

            /* trace the window and check where you can expand */
            int indices[][] = {
                {-1, 0},
                {0, -1}, {0, 1},
                {1, 0},
                {-1, -1}, {-1, 1},
                {1, -1}, {1, 1}
            };

            /* This array is a circular buffer holding indices in to
             * the array named 'indices'
             *
             * This array is got by tracing the following from 6 to 3
             * in circular fashion
             *
             *  4 0 5
             *  1 X 2
             *  6 3 7
             *
             */
            int[] check_indices = {6, 1, 4, 0, 5, 2, 7, 3};
            int m, n;
            boolean color = true;

            for (int k = 0; k < indices.length; k++) {

                m = i + indices[k][0];
                n = j + indices[k][1];

                if (c.iData[m][n] == 0) {

                    /* check the surroundings 5 pixels only */
                    for (int l = 0; l < 5; l++) {

                        int p = indices[check_indices[(m - i) * 3 + (n - j) + 4]][0];
                        int q = indices[check_indices[(m - i) * 3 + (n - j) + 4]][1];

                        if (c.iData[m + p][n + q] != 0) {
                            /* every second element is corner one */
                            if (type == HOMOGENIOUS_EXPANSION && (l % 2) == 0) {
                                color = false;
                                break;
                            }
                        }
                    }
                }

                if (color) {

                    c.iData[m][n] = col;

                    c.printMatrix();

                    expand_rec(c, m - 1, n - 1, col, type);
                    expand_rec(c, m - 1, n, col, type);
                    expand_rec(c, m - 1, n + 1, col, type);

                    expand_rec(c, m, n - 1, col, type);
                    expand_rec(c, m, n + 1, col, type);

                    expand_rec(c, m + 1, n - 1, col, type);
                    expand_rec(c, m + 1, n, col, type);
                    expand_rec(c, m + 1, n + 1, col, type);
                }
            }
        } catch (Exception e) {
            //System.out.println(e + " i : " + i + " j : " + j);
            //e.printStackTrace();
        }
    }

    private static void expand_region(mCanvas c, int rNum, int type) {

        for (int i = 2; i < c.iData.length - 4; i++) {
            for (int j = 2; j < c.iData[0].length - 4; j++) {
                if (c.iData[i][j] == rNum) {
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

    /*
     * Thinning works as follows
     * 
     *
     * 
     */
    public static void thin(int[][] iData, int mainCol, int markerCol) {

        Pixel pixels[][] = new Pixel[iData.length][iData[0].length];

        for (int i = 0; i < iData.length; i++) {
            for (int j = 0; j < iData[0].length; j++) {
                pixels[i][j] = new Pixel();
            }
        }

        for (int i = 2; i < iData.length - 1; i++) {
            for (int j = 0; j < iData[0].length - 1; j++) {
                pixels[i - 1][j].horRem = (iData[i][j] == mainCol && iData[i - 1][j] == mainCol);
            }
        }

        for (int i = 2; i < iData[0].length - 1; i++) {
            for (int j = 0; j < iData.length - 1; j++) {
                pixels[j][i - 1].verRem = (iData[j][i] == mainCol && iData[j][i - 1] == mainCol);
            }
        }

        for (int i = 0; i < iData.length; i++) {
            for (int j = 0; j < iData[0].length; j++) {
                //if(pixels[i][j].horRem) iData[i][j] = 0x8000;
                //if(pixels[i][j].verRem) iData[i][j] = 0x800000;
                //if(pixels[i][j].horRem && pixels[i][j].verRem) iData[i][j] = 0x80;
            }
        }

        if (iData == null) {
            return;
        }

        for (int i = 1; i < iData.length - 1; i++) {
            for (int j = 1; j < iData[0].length - 1; j++) {

                if (pixels[i][j].horRem && pixels[i][j].verRem) {

                    /*
                    
                    check if you remove this pixel does it affect the links 
                    checking this is simple 
                
                    X X X
                    X 1 0
                    0 0 0
                    
                    ******
                    ******
                    **
                    **
                    ******
                    ******

                      **
                     *
                    **
                     *
                      **
                    
                    
                
                    You have to just check the connectivity of the pixels labelled
                    as 'X'. with far pixels.
                
                    Below is the 2D array where indices indicate node being
                    checked if the node contains -2 as first value don't check
                    else you have to check and if the correponding pixels to be
                    checked are of required color then you cannot remove that
                    pixel break else continue
                     */
                    boolean remove = true;

                    int index_dictionary[][] = {
                        {-1, -1}, {-1, 0}, {-1, 1},
                        {0, -1}, {0, 0}, {0, 1},
                        {1, -1}, {1, 0}, {1, 1}
                    };

                    int check_indices[][] = {
                        {2, 5, 6, 7, 8},
                        {6, 7, 8, -1, -1},
                        {0, 3, 6, 7, 8},
                        {2, 5, 8, -1, -1}
                    };
                    /*
                    int check_indices[][] = {
                        {5, 7, 8},//{2, 5, 6, 7, 8},
                        {6, 7, 8},//{6, 7, 8,-1,-1},
                        {3, 6, 7},//{0, 3, 6, 7, 8},
                        {2, 5, 8} //{2, 5, 8,-1,-1}
                    };
                     */

                    for (int k = 0; k < check_indices.length; k++) {

                        int m = i + index_dictionary[k][0];
                        int n = j + index_dictionary[k][1];

                        if (iData[m][n] == mainCol) {

                            for (int l = 0; l < check_indices[0].length; l++) {

                                if (check_indices[k][l] == -1) {
                                    continue;
                                }
                                int p = i + index_dictionary[check_indices[k][l]][0];
                                int q = i + index_dictionary[check_indices[k][l]][1];

                                if (iData[p][q] == mainCol) {
                                    remove = false;
                                    break;
                                }
                            }
                            if (!remove) {
                                break;
                            }
                        }
                    }

                    if (remove) {
                        iData[i][j] = markerCol;
                    }
                }
            }
        }
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
