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
