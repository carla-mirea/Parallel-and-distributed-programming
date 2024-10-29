import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Consumer {
    public static void main(String[] args) {
        // Initialise the sum of all products, which will represent the scalar product of the vectors
        int scalarProduct = 0;

        // Connect to the producer
        try (Socket socket = new Socket("localhost", 5000);
             // Get the data from the producer
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals("EOF")) {
                    break;
                }
                // Sum up products -> for future, convert in bytes, do not work with strings!!
                int product = Integer.parseInt(line);
                scalarProduct += product;
                System.out.println("Received from Producer: " + product);
            }
            System.out.println("Scalar Product is: " + scalarProduct);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
