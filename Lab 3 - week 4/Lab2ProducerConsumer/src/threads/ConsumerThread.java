package threads;

public class ConsumerThread extends Thread{
    public int result = 0;
    public ProducerConsumerBuffer sharedBuffer;
    public int length;

    public ConsumerThread(ProducerConsumerBuffer sharedBuffer, int length) {
        super("CONSUMER");
        this.sharedBuffer = sharedBuffer;
        this.length = length;
    }

    @Override
    public void run() {
        for (int i = 0; i < this.length; i++) {
            try {
                result += sharedBuffer.get();
                System.out.printf("CONSUMER: Result is now %d\n", result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("---------------------------------------------------------");
        System.out.printf("CONSUMER: Final result is: %d", result);
    }
}
