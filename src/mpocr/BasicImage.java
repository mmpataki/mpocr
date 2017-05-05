package mpocr;

/**
 * @author mmp
 */
class BasicImage implements IImage {

    protected int[][] iData;
    /* to check if the buffer 'iData' is dirty. */
    boolean dirty = false;

    /*
     * constructor for initialising the image.
     * NOTE: 
     *     The iData you send is just referenced from here hence modifications
     *     through other references will be reflecting here. Careful.
     */
    BasicImage(int[][] iData) {
        this.iData = iData;
    }

    /* dummy constructor. to shut the java compiler mouth. */
    BasicImage() {  }
    
    /* for getting the height and width of the image */
    @Override
    public int getWidth() {
        return iData.length > 0 ? iData[0].length : 0;
    }

    @Override
    public int getHeight() {
        return iData.length;
    }

    /* to get the foreground and background color of the image */
    @Override
    public int getForeground() {
        return -1;
    }

    @Override
    public int getBackground() {
        return 0;
    }

    @Override
    public int[][] getImageData() {
        return iData;
    }

    public void printimage() {
        for (int[] row : iData) {
            Util.puts("\n");
            for (int j = 0; j < iData[0].length; j++) {
                Util.puts(row[j] == getBackground() ? "." : "#");
            }
        }
        Util.puts("\n");
    }
    
    public void printimageforce() {
        for (int[] row : iData) {
            System.out.print("\n");
            for (int j = 0; j < iData[0].length; j++) {
                System.out.print(row[j] == getBackground() ? "." : "#");
            }
        }
        System.out.print("\n");
    }
    
    private static final int NREVERSE = 0;
    private static final int REVERSE = 1;

    /* to rotate the image to given degree */
    public void rotate(double deg) {

        double m = (double) Math.tan(deg * Math.PI / 180);
        double am = Math.abs(m);
        int mx = (int) Math.round((double) 1 / am * iData[0].length);
        int my = (int) Math.round((double) 1 / am * iData.length);

        Util.puts(mx + ", " + my);
        Util.puts(iData[0].length + ", " + iData.length);

        int[][] cp = new int[my][mx];

        for (int i = 0; i < my; i++) {
            for (int j = 0; j < mx; j++) {
                cp[i][j] = 0;
            }
        }
        removeIntersection(iData);
        int yc = getXIntersection(iData, REVERSE);
        for (int i = yc; i < iData.length; i++) {
            int y = 0, x = (int) (am * (i - yc)), ax = 0;
            do {
                cp[i][ax++] = iData[y][x];
                x++;
                y = (int) Math.round(m * x + i);
            } while (y < iData.length && y > -1 && x < iData[0].length);
        }
    }

    /* no errors! proved don't touch. */
    private void removeIntersection(int[][] iData) {

        /* find x & y intersection */
        int xc = getXIntersection(iData, NREVERSE);
        int yc = getYIntersection(iData, NREVERSE);

        Util.puts(xc + ", " + yc + " " + iData.length);

        for (int i = 1, ic = yc; ic < iData.length; i++, ic++) {
            for (int j = 10, jc = xc; jc < iData[0].length; j++, jc++) {
                iData[i][j] = iData[ic][jc];
            }
        }
    }
    private int getYIntersection(int[][] iData, int type) {
        for (int i = 0; i < iData.length; i++) {
            for (int j = 0; j < iData[0].length; j++) {
                if (iData[i][j] != 0) {
                    return type == NREVERSE ? i : j;
                }
            }
        }
        return 0;
    }
    private int getXIntersection(int[][] iData, int type) {
        for (int i = 0; i < iData[0].length; i++) {
            for (int j = 0; j < iData.length; j++) {
                if (iData[j][i] != 0) {
                    return type == NREVERSE ? i : j;
                }
            }
        }
        return 0;
    }
}
