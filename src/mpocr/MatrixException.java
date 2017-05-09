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
class MatrixException extends Exception {

    public MatrixException(int m, int n, int p) {
        super(
                "Matrices are multiplication incompatible. [" +
                m + " x " + n + "], [" + n + " x " + p + "]"
        );
    }
    public MatrixException(String message) {
        super(message);
    }
}
