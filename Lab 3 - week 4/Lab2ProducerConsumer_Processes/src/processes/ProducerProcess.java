package processes;

import model.Vector;

import java.io.DataOutputStream;
import java.io.IOException;

public final class ProducerProcess extends Thread{
    public ProducerConsumerPipe pipe;
    public final Vector vector1, vector2;

    public ProducerProcess(ProducerConsumerPipe pipe, Vector vector1, Vector vector2) {
        super("PRODUCER");
        this.pipe = pipe;
        this.vector1 = vector1;
        this.vector2 = vector2;
    }

    @Override
    public void run() {
        // We write data to the output pipe
        try (DataOutputStream out = new DataOutputStream(pipe.getOutputPipe())) {

            for(int i = 0; i < vector1.getLength(); i++) {
                // Compute the product of the corresponding elements from vector1 and vector2
                int product = vector1.get(i) * vector2.get(i);
                System.out.printf("PRODUCER: Sending %d * %d = %d\n", vector1.get(i), vector2.get(i), product);

                // The product is written to the output pipe
                out.writeInt(product);
                // We make sure that the data is immediately sent through the pipe
                out.flush();
            }
            // Here, a termination signal is sent
            out.writeInt(Integer.MIN_VALUE);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            pipe.setCompleted(true);
        }
    }
}
