package mpocr;

/**
 * A util class which never got used.
 */
public class Stat {

    int min, max, average, nonzeros, sum, nzavg;
    
    public Stat(int array[]) {
        max = array[0];
        min = array[0];
        nonzeros = sum = 0;
        for (int i = 1; i < array.length; i++) {
            if(max < array[i]) max = array[i];
            if(min > array[i]) min = array[i];
            if(array[i] != 0) nonzeros++;
            sum += array[i];
        }
        nzavg = sum / nonzeros;
        average = sum / array.length;
    }
    
    @Override
    public String toString() {
        return String.format("min      : %d\nmax      : %d\nsum      : %d\naverage: %d\nnzavg     : %d\nnonzeroes: %d", min, max, sum, average, nzavg, nonzeros);
    }
    
}
