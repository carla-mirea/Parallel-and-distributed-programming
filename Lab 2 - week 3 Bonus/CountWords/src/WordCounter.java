import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

class WordCounter implements Runnable {
    private List<String> words;
    private int threadId;
    private ConcurrentHashMap<String, Integer> wordsCount;

    public WordCounter(List<String> words, int threadId, ConcurrentHashMap<String, Integer> wordsCount) {
        this.words = words;
        this.threadId = threadId;
        this.wordsCount = wordsCount;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();

        for(String word: words) {
            wordsCount.merge(word, 1, Integer::sum);
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println("Thread " + threadId + ": " + duration + " ms");
    }
}