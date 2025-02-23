package Algorithms;

import Model.Polynomial;
import Model.Task;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ParallelClassic {

    private static final int NO_THREADS = 4;

    public static Polynomial multiply(Polynomial p1, Polynomial p2) throws InterruptedException {
        int sizeOfResultCoefficientList = p1.getDegree() + p2.getDegree() + 1;

        List<Integer> coefficients = IntStream.range(0, sizeOfResultCoefficientList).mapToObj(i -> 0).collect(Collectors.toList());
        Polynomial result = new Polynomial(coefficients);

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(NO_THREADS);

        int step = sizeOfResultCoefficientList / NO_THREADS;

        if(step == 0){
            step = 1;
        }

        int end;
        for(int i = 0; i < sizeOfResultCoefficientList; i += step){
            end = i + step;
            Task task = new Task(i, end, p1, p2, result);
            executor.execute(task);
        }

        executor.shutdown();
        executor.awaitTermination(50, TimeUnit.SECONDS);

        return result;
    }
}
