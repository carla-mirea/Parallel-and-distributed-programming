import Message.Message;
import Message.CloseMessage;
import Message.SubscribeMessage;
import Message.UpdateMessage;
import mpi.MPI;

public class Subscriber implements Runnable{
    private final DSM dsm;

    public Subscriber(DSM dsm) {
        this.dsm = dsm;
    }

    @Override
    public void run() { // separate thread to handle incoming messages
        while (true) {
            System.out.println("<" + MPI.COMM_WORLD.Rank() + "> is waiting...");
            Object[] messagesObject = new Object[1];

            // listens for incoming messages
            MPI.COMM_WORLD.Recv(messagesObject, 0, 1, MPI.OBJECT, MPI.ANY_SOURCE, MPI.ANY_TAG);
            Message message = (Message) messagesObject[0];

            // handles different message types: stops the thread, updates the subscriber list or updates the variable's value
            if (message instanceof CloseMessage) {
                System.out.println("<" +MPI.COMM_WORLD.Rank() + "> stopped!");
                break;
            } else if (message instanceof SubscribeMessage subscribeMessage) {
                System.out.println("Subscriber message in <" + MPI.COMM_WORLD.Rank() + ">: " + subscribeMessage.rank + " subscribes to " + subscribeMessage.variable);
                this.dsm.syncSubscription(subscribeMessage.variable, subscribeMessage.rank);
            }  else if (message instanceof UpdateMessage updateMessage) {
                System.out.println("Update message in <" + MPI.COMM_WORLD.Rank() + ">: " + updateMessage.variable + " -> " + updateMessage.value);
                this.dsm.setVariable(updateMessage.variable, updateMessage.value);
            }
        }
        System.out.println("Finalizing <" + MPI.COMM_WORLD.Rank() + "> - " + this.dsm);
    }
}