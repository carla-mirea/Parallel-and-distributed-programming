package processes;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumerPipe {
    private final PipedOutputStream outputStream;
    private final PipedInputStream inputStream;
    private boolean completed = false;

    private final Lock lock = new ReentrantLock();
    private final Condition condVar = lock.newCondition();

    public ProducerConsumerPipe() throws Exception {
        this.outputStream = new PipedOutputStream();
        this.inputStream = new PipedInputStream(outputStream);
    }

    public PipedOutputStream getOutputPipe() {
        return outputStream;
    }

    public PipedInputStream getInputPipe() {
        return inputStream;
    }

    public Lock getLock() {
        return lock;
    }

    public Condition getCondVar() {
        return condVar;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void closePipes() throws Exception {
        outputStream.close();
        inputStream.close();
    }
}
