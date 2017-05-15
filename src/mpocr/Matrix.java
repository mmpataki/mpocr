package mpocr;

import static java.lang.Integer.min;
import java.util.Arrays;

/**
 *  Alternative matrix package for mpocr project. It has a lot of handy
 *  functions for matrix manipulations.
 */
public class Matrix {
    
    
    private double[][] matrix;
    private int rows;
    private int cols;
    
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
     * @throws mpocr.MatrixException
     */
    public void transpose() throws MatrixException {
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
     * @throws MatrixException
     */
    public static double[][] transpose(double[][] matrix) throws MatrixException {
        if(matrix.length == 0 || matrix[0].length == 0) {
            throw new MatrixException("Null matrix supplied for transpose");
        }
        double[][] dest = new double[matrix[0].length][matrix.length];
        copy(dest, matrix);
        transpose(dest, matrix);
        return dest;
    }
    
    /**
     * Copies the transpose of matrix into dest 
     * @param dest : 2D array of order n x m.
     * @param matrix:2D array of order m x n.
     * @throws MatrixException 
     */
    private static void transpose(double[][] dest, double[][] matrix) throws MatrixException {
        
        if(matrix.length == 0 || matrix[0].length == 0 ||
                dest.length < matrix[0].length ||
                dest[0].length < matrix.length) {
            throw new MatrixException("Null/Incompatible-dest matrix supplied for transpose");
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
    
    /**
     * Multiplies matrix with self and returns the product matrix.
     * @param x : another matrix.
     * @return : product matrix.
     * @throws MatrixException 
     */
    public Matrix multiply(Matrix x) throws MatrixException {
        Matrix m = new Matrix(rows, x.cols);
        multiply(m.matrix, matrix, x.matrix);
        return m;
    }
    
    /**
     * Multiplies matrix (2D array) a & b and stores result in dest.
     * @throws MatrixException 
     */
    public static void multiply(double[][] dest, double[][] a, double[][] b) throws MatrixException {
        if(
                a == null || b == null || dest == null ||
                a.length == 0 || b.length == 0 || dest.length == 0) {
            throw new NullPointerException("Matrices are null");
        }
        if(a[0].length != b.length) {
            throw new MatrixException(a.length, a[0].length, b[0].length);
        }
        if(dest.length < a.length || dest[0].length < b[0].length) {
            throw new MatrixException("Output matrix size is small");
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
    
    @Override
    public String toString() {
        return matToString(matrix);
    }
    
    /**
     * Returs a string represenation of the 2D array.
     * @param matrix: 2D array to stringize.
     * @return String representation of the matrix
     */
    public String matToString(double[][] matrix) {
        return Arrays.deepToString(matrix).replace("],", "]\n");
    }

    /**
     * Copies the given matrix into self.
     * @param m : the matrix to be copied.
     */
    public void copy(Matrix m) {
        if(cols != m.cols || rows != m.rows) {
            matrix = new double[m.rows][m.cols];
        }
        copy(matrix, m.matrix);
    }
    
    /**
     * Copies a 2D array into other.
     * @param dest: destination array.
     * @param src : source array.
     */
    public static void copy(double[][] dest, double[][] src) {
        for (int i = 0; i < min(dest.length, src.length); i++) {
            System.arraycopy(src[i], 0, dest[i], 0, min(src[0].length, dest[0].length));
        }
    }
}
