public class ColumnThread implements Runnable {
    private final Integer[][] result;
    private final Integer start;
    private final Integer end;

    public ColumnThread(Integer[][] result, Integer left, Integer right) {
        this.result = result;
        this.start = left;
        this.end = right;
    }

    @Override
    public void run() {
        int totalRows = Main.rowsA;
        int totalCols = Main.columnsB;

        int currentRow = start % totalRows;
        int currentCol = start / totalRows;
        int remainingElements = end - start;

        for(int i = 0; i <= remainingElements; i++) {
            if (currentCol >= totalCols || currentRow >= totalRows) {
                break;
            }

            result[currentRow][currentCol] = Matrix.computeElement(Main.matrix1, Main.matrix2, currentRow, currentCol);
            currentRow++;

            if(currentRow == totalRows) {
                currentRow = 0;
                currentCol++;
            }
        }
    }
}
