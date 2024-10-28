import model.Vector;
import threads.ConsumerThread;
import threads.ProducerConsumerBuffer;
import threads.ProducerThread;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Vector vector1 = new Vector(Arrays.asList(2, 4, 6, 8));
        Vector vector2 = new Vector(Arrays.asList(8, 6, 4, 2));

        ProducerConsumerBuffer helper = new ProducerConsumerBuffer();

        ProducerThread producer = new ProducerThread(helper, vector1,vector2);
        ConsumerThread consumer = new ConsumerThread(helper, vector1.getLength());

        producer.start();
        consumer.start();
    }
}