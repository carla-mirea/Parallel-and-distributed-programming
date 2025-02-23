public class RowThread implements Runnable {
    private final Integer[][] result;
    private final Integer start;
    private final Integer end;

    public RowThread(Integer[][] result, Integer start, Integer end) {
        this.result = result;
        this.start = start;
        this.end = end;
    }

    @Override
    public void run() {
        int totalColumns = Main.columnsB;
        int totalRows = Main.rowsA;

        int currentRow = start / totalColumns;
        int currentColumn = start % totalColumns;
        int remainingElements = end - start;

        for(int index = 0; index <= remainingElements; index++) {
            if (currentRow >= totalRows || currentColumn >= totalColumns) {
                break;
            }

            result[currentRow][currentColumn] = Matrix.computeElement(Main.matrix1, Main.matrix2, currentRow, currentColumn);
            currentColumn++;
            if(currentColumn == totalColumns) {
                currentColumn = 0;
                currentRow++;
            }
        }
    }
}
