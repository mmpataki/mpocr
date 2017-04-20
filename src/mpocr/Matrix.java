/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpocr;

import static java.lang.Integer.min;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mmp
 */
public class Matrix {
    double[][] matrix;
    int rows;
    int cols;
    
    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        matrix = new double[rows][cols];
    }
    
    /* very handy for wrapping the data and calculating */
    public Matrix(double[][] values) {
        matrix = values;
        rows = values.length;
        cols = values[0].length;
    }
    
    public Matrix(Matrix x) {
        rows = x.rows;
        cols = x.cols;
        matrix = new double[rows][cols];
        copy(matrix, x.matrix);
    }
    /**
     * Transposes the matrix itself.
     */
    public void transpose() throws Exception {
        double[][] dest = matrix;
        if(rows != cols) {
            dest = new double[cols][rows];
            copy(dest, matrix);
        }
        transpose(dest, matrix);
        matrix = dest;
    }
    /**
     * @param matrix: the matrix to be transposed
     * @return returns the transpose of the supplied matrix
     * @throws Exception 
     */
    public static double[][] transpose(double[][] matrix) throws Exception {
        if(matrix.length == 0 || matrix[0].length == 0) {
            throw new Exception("Null matrix supplied for transpose");
        }
        double[][] dest = new double[matrix[0].length][matrix.length];
        copy(dest, matrix);
        transpose(dest, matrix);
        return dest;
    }
    private static void transpose(double[][] dest, double[][] matrix) throws Exception {
        
        if(matrix.length == 0 || matrix[0].length == 0 ||
                dest.length < matrix[0].length ||
                dest[0].length < matrix.length) {
            throw new Exception("Null/Incompatible-dest matrix supplied for transpose");
        }
        int rows = matrix.length;
        int cols = matrix[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = i; j < cols; j++) {
                double tmp = matrix[i][j];
                matrix[i][j] = dest[j][i];
                dest[j][i] = tmp;
            }
        }
    }
    public Matrix multiply(Matrix x) throws Exception {
        Matrix m = new Matrix(rows, x.cols);
        multiply(m.matrix, matrix, x.matrix);
        return m;
    }
    
    public static void multiply(double[][] dest, double[][] a, double[][] b) throws Exception {
        if(
                a == null || b == null || dest == null ||
                a.length == 0 || b.length == 0 || dest.length == 0) {
            throw new NullPointerException("Matrices are null");
        }
        if(a[0].length != b.length) {
            throw new Exception("matrices are multiplication incompatible");
        }
        if(dest.length < a.length || dest[0].length < b[0].length) {
            throw new Exception("Output matrix size is small");
        }
        int m = a.length, n = a[0].length, p = b[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < p; j++) {
                double sum = 0;
                for (int k = 0; k < n; k++) {
                    sum += a[i][k] * b[k][j];
                }
                dest[i][j] = sum;
            }
        }
    }
    
    public void test() {
        double[][] a= {{1,1,1,1},{1,1,1,1},{1,1,1,1},{1,1,1,1}};
        double[][] b= {{1,1,1},{1,1,1},{1,1,1},{1,1,1}};
        double[][] c= new double[4][3];
        try {
            System.out.println("Testing multiplication");
            Matrix.multiply(c, a, b);
            System.out.println(matToString(c));
            System.out.println("Testing transpose");
            c = Matrix.transpose(c);
            System.out.println(matToString(c));
        } catch (Exception ex) {
            Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public String toString() {
        return matToString(matrix);
    }
    
    public String matToString(double[][] c) {
        String s = "";
        for (double[] ds : c) {
                s += "\n";
                for (double d : ds) {
                    s += (d + " ");
                }
            }
        return s;
    }

    public void copy(Matrix x) {
        if(cols != x.cols || rows != x.rows) {
            matrix = new double[x.rows][x.cols];
        }
        copy(matrix, x.matrix);
    }
    
    public static void copy(double[][] dest, double[][] src) {
        for (int i = 0; i < min(dest.length, src.length); i++) {
            System.arraycopy(src[i], 0, dest[i], 0, min(src[0].length, dest[0].length));
        }
    }
}
