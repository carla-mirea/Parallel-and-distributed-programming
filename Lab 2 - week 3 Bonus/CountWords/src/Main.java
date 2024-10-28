import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    private static ConcurrentHashMap<String, Integer> wordsCount = new ConcurrentHashMap<>();

    private static List<String> readFromFile(String filename) throws IOException {
        List<String> words = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;

        while((line = reader.readLine()) != null) {
            String[] splitWords = line.toLowerCase().split("\\W+");
            words.addAll(Arrays.asList(splitWords));
        }

        reader.close();
        return words;
    }

    private static void printOnConsole() {
        for(Map.Entry<String, Integer> entry: wordsCount.entrySet()) {
            System.out.println(entry.getKey() + " appears " + entry.getValue() + " times.");
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        List<String> words = readFromFile("words.txt");

        long startTime = System.currentTimeMillis();

        int threadsCount = 20;
        ExecutorService executorService = Executors.newFixedThreadPool(threadsCount);

        int splitPartsSize = words.size() / threadsCount;

        for(int threadNumber = 0; threadNumber < threadsCount; threadNumber++) {
            int start = threadNumber * splitPartsSize;
            int end = (threadNumber == threadsCount - 1) ? words.size() : (threadNumber + 1) * splitPartsSize;
            List<String> splitPart = words.subList(start, end);

            executorService.submit(new WordCounter(splitPart, threadNumber, wordsCount));
        }

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println("Total time: " + duration + " ms");

        printOnConsole();
    }
}