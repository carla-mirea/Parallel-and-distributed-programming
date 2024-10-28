package threads;

import model.Vector;

public final class ProducerThread extends Thread{
    public int length;
    public ProducerConsumerBuffer sharedBuffer;
    public Vector vector1, vector2;

    public ProducerThread(ProducerConsumerBuffer sharedBuffer, Vector vector1, Vector vector2) {
        super("PRODUCER");
        this.sharedBuffer = sharedBuffer;
        this.vector1 = vector1;
        this.vector2 = vector2;
        this.length = vector1.getLength();
    }

    @Override
    public void run() {
        for (int i = 0; i < length; i++){
            try {
                System.out.printf("PRODUCER: Sending %d * %d = %d\n", vector1.get(i), vector2.get(i), vector1.get(i) * vector2.get(i));
                sharedBuffer.put(vector1.get(i) * vector2.get(i));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
