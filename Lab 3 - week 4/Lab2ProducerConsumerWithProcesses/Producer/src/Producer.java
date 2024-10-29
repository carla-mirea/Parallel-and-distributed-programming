import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Producer {
    public static void main(String[] args) {
        // Initialise the vectors
        int[] vectorA = {11, 2, 15, 24};
        int[] vectorB = {10, 23, 2, 0};

        // Start socket on port 5000
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Producer waiting for consumer to connect...");
            // Wait for the consumer to connect
            Socket socket = serverSocket.accept();
            System.out.println("Consumer connected!");

            try (PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {
                // Calculate and send product to consumer
                for (int i = 0; i < vectorA.length; i++) {
                    int product = vectorA[i] * vectorB[i];
                    writer.println(product);
                    System.out.println("Sent to Consumer: " + product);
                }
                // Signal end of data
                writer.println("EOF");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
