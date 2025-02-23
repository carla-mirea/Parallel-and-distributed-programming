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
                // We read and sum up the product
                int product = in.readInt();
                if(product == Integer.MIN_VALUE) {
                    break;
                }
                result += product;
                System.out.printf("CONSUMER: Result is now %d\n", result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("---------------------------------------------------------");
            System.out.printf("CONSUMER: Final result is: %d", result);
            try {
                pipe.closePipes();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
