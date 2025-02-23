public class KthThread implements Runnable{
    private final Integer[][] result;
    private final Integer k;
    private final Integer stepSize;

    public KthThread(Integer[][] result, Integer k, Integer stepSize) {
        this.result = result;
        this.k = k;
        this.stepSize = stepSize;
    }

    @Override
    public void run() {
        int totalColumns = Main.columnsB;
        int totalRows = Main.rowsA;

        int currentRow = 0;
        int currentCol = k;

        while(true) {
            int overshoot = currentCol / totalColumns;
            currentRow += overshoot;
            currentCol -= overshoot * totalColumns;
            if(currentRow >= totalRows) {
                break;
            }
            result[currentRow][currentCol] = Matrix.computeElement(Main.matrix1, Main.matrix2, currentRow, currentCol);
            currentCol += stepSize;
        }
    }
}
