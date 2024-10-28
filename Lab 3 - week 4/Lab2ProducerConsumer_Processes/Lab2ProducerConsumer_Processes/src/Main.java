import model.Vector;
import processes.ProducerConsumerPipe;
import processes.ConsumerProcess;
import processes.ProducerProcess;


import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws Exception {
        Vector vector1 = new Vector(Arrays.asList(2, 4, 6, 8));
        Vector vector2 = new Vector(Arrays.asList(8, 6, 4, 2));

        ProducerConsumerPipe helper = new ProducerConsumerPipe();

        ProducerProcess producer = new ProducerProcess(helper, vector1, vector2);
        ConsumerProcess consumer = new ConsumerProcess(helper);

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();

        helper.closePipes();
    }
}