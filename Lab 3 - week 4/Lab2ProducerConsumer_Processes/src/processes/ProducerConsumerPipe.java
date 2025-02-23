package processes;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class ProducerConsumerPipe {
    private final PipedOutputStream outputStream;
    private final PipedInputStream inputStream;
    private boolean completed = false;

    public ProducerConsumerPipe() throws IOException {
        this.outputStream = new PipedOutputStream();
        this.inputStream = new PipedInputStream(outputStream);
    }

    public PipedOutputStream getOutputPipe() {
        return outputStream;
    }

    public PipedInputStream getInputPipe() {
        return inputStream;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void closePipes() throws Exception {
        outputStream.close();
        inputStream.close();
    }
}
