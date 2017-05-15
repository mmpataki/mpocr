package mpocr;

/**
 * Just a wrapper around the Exception class with some debuggging information.
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
