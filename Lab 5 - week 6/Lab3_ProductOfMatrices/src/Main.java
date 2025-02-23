import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Main {
    public static int rowsA, columnsA, columnsB;
    // public static int functionType;
    public static int partitionType;
    public static int totalTasks;
    public static Matrix matrix1;
    public static Matrix matrix2;
    public static boolean stopCondition = true;

    public static void getParameters() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter rows for Matrix A: ");
        rowsA = scanner.nextInt();
        System.out.println("Enter columns for Matrix A / rows for Matrix B: ");
        columnsA = scanner.nextInt();
        System.out.println("Enter columns for Matrix B: ");
        columnsB = scanner.nextInt();
        // System.out.println("Choose function type: \n0. Separate Threads \n1. Thread Pool");
        // functionType = scanner.nextInt();
        System.out.println("Choose partition type:\n0. Row-wise \n1. Column-wise \n2. Kth element");
        partitionType = scanner.nextInt();
        System.out.println("Enter number of tasks:");
        totalTasks = scanner.nextInt();
        initializeMatrices();
    }

    public static void initializeMatrices() {
        matrix1 = new Matrix(rowsA, columnsA);
        matrix2 = new Matrix(columnsA, columnsB);
    }

    public static void defaultParameters() {
        rowsA = 3;
        columnsA = 4;
        columnsB = 3;
        // functionType = 1;
        partitionType = 1;
        matrix1 = new Matrix(new Integer[][]{{1, 2, 3, 4}, {3, 4, 5, 6}, {5, 6, 7, 8}});
        matrix2 = new Matrix(new Integer[][]{{7, 8, 9}, {9, 7, 8}, {8, 9, 9}, {3, 2, 2}});
        totalTasks = 3;
        stopCondition = false;
    }

    public static Matrix computeWithThreads() throws InterruptedException {
        Integer[][] result = new Integer[rowsA][columnsB];
        List<Thread> threads = new ArrayList<>();
        int elementsPerTask = rowsA * columnsB / totalTasks;

        for (int i = 0; i < totalTasks; i++) {
            int start = i * elementsPerTask;
            int end = Math.min((i + 1) * elementsPerTask, rowsA * columnsB);
            if (partitionType == 0) {
                threads.add(new Thread(new RowThread(result, start, end)));
            } else if (partitionType == 1) {
                threads.add(new Thread(new ColumnThread(result, start, end)));
            } else {
                threads.add(new Thread(new KthThread(result, i, totalTasks)));
            }
        }

        for (Thread thread : threads) thread.start();
        for (Thread thread : threads) thread.join();

        return new Matrix(result);
    }

    public static Matrix computeWithThreadPool() throws InterruptedException {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(totalTasks);
        Integer[][] result = new Integer[rowsA][columnsB];
        int elementsPerTask = rowsA * columnsB / totalTasks;
        List<Runnable> tasks = new ArrayList<>();

        for (int i = 0; i < totalTasks; i++) {
            int start = i * elementsPerTask;
            int end = Math.min((i + 1) * elementsPerTask, rowsA * columnsB);
            if (partitionType == 0) {
                tasks.add(new RowThread(result, start, end));
            } else if (partitionType == 1) {
                tasks.add(new ColumnThread(result, start, end));
            } else {
                tasks.add(new KthThread(result, i, totalTasks));
            }
        }

        for (Runnable task : tasks) executor.execute(task);
        executor.shutdown();
        while (!executor.awaitTermination(1, TimeUnit.DAYS)) {
            System.out.println("Still waiting for termination..");
        }
        return new Matrix(result);
    }

    public static void main(String[] args) throws InterruptedException {
        while (stopCondition) {
            getParameters();
            // defaultParameters()
            Matrix trueProduct = Matrix.computeProduct(matrix1, matrix2);

            System.out.println("The matrices are: ");
            matrix1.print();
            System.out.println();
            matrix2.print();
            System.out.println();

            Matrix computedProduct;
            double startTime = System.nanoTime();

            /*if (functionType == 0) {
                computedProduct = computeWithThreads();
            } else {
                computedProduct = computeWithThreadPool();
            }*/
            computedProduct = computeWithThreads();
            double endTime = System.nanoTime();
            computedProduct.print();
            System.out.println("Time taken: " + (endTime - startTime) / 1_000_000_000.0 + " seconds\n");

            startTime = System.nanoTime();

            computedProduct = computeWithThreadPool();
            endTime = System.nanoTime();

            computedProduct.print();
            System.out.println("Time taken: " + (endTime - startTime) / 1_000_000_000.0 + " seconds\n");

            if (trueProduct.equals(computedProduct)) {
                System.out.println("Correct");
            } else {
                System.out.println("Incorrect");
            }
        }
    }
}