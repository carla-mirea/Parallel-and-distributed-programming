import java.util.Random;

public class Matrix {
    private final Integer[][] data;
    private final Integer rows, cols;

    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.data = new Integer[rows][cols];
        Random random = new Random();
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) {
                this.data[i][j] = random.nextInt(10);
            }
        }
    }

    public Matrix(Integer[][] matrix) {
        this.data = matrix;
        this.rows = matrix.length;
        this.cols = matrix[0].length;
    }

    public static int computeElement(Matrix a, Matrix b, int row, int col) {
        int result = 0;
        for(int i = 0; i < a.cols; i++) {
            result += a.get(row, i) * b.get(i, col);
        }
        return result;
    }

    public int get(int row, int col) {
        return data[row][col];
    }

    public static Matrix computeProduct(Matrix a, Matrix b) {
        Integer[][] result = new Integer[a.rows][b.cols];
        for(int i = 0; i < a.rows; i++) {
            for(int j = 0; j < b.cols; j++) {
                result[i][j] = computeElement(a, b, i, j);
            }
        }
        return new Matrix(result);
    }

    public void print() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(data[i][j] + " ");
            }
            System.out.println();
        }
        // System.out.println();
    }

    public boolean equals(Matrix other) {
        if (this.rows != other.rows || this.cols != other.cols) return false;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (!data[i][j].equals(other.data[i][j])) return false;
            }
        }
        return true;
    }
}
