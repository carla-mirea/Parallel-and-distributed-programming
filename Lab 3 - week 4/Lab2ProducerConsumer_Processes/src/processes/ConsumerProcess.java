package processes;

import java.io.DataInputStream;
import java.io.IOException;

public class ConsumerProcess extends Thread{
    public int result = 0;
    public ProducerConsumerPipe pipe;

    public ConsumerProcess(ProducerConsumerPipe pipe) {
        super("CONSUMER");
        this.pipe = pipe;
    }

    @Override
    public void run() {
        try(DataInputStream in = new DataInputStream(pipe.getInputPipe())) {
            while (true) {
                pipe.getLock().lock();
                try {
                    // Wait for the producer to finish
                    while (in.available() == 0 && !pipe.isCompleted()) {
                        pipe.getCondVar().await();
                    }

                    // We check if the producer has completed and no more products are available
                    if (pipe.isCompleted() && in.available() == 0) {
                        break;
                    }

                    // We read and sum up the product
                    int product = in.readInt();
                    result += product;
                    System.out.printf("CONSUMER: Result is now %d\n", result);
                } finally {
                    pipe.getLock().unlock();
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("---------------------------------------------------------");
            System.out.printf("CONSUMER: Final result is: %d", result);
        }
    }
}
