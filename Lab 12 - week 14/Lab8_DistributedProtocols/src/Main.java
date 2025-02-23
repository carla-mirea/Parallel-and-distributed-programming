//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import mpi.MPI;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        MPI.Init(args);

        int me = MPI.COMM_WORLD.Rank(); // unique rank (ID) of the current process
        int size = MPI.COMM_WORLD.Size(); // total processes in the system

        DSM dsm = new DSM();

        System.out.println("Starting <" + me + "> of <" + size + ">");

        // initially
        // a = 1
        // b = 2
        // c = 3
        if (me == 0) { // subscribes to all variables and performs several compareAndExchange operations
            Thread thread = new Thread(new Subscriber(dsm));
            thread.start();

            dsm.subscribeToVariable("a");
            // we ensure the events are processed one at a time in a predictable order, for demo purposes
            Thread.sleep(1000);
            dsm.subscribeToVariable("b");
            Thread.sleep(1000);
            dsm.subscribeToVariable("c");
            Thread.sleep(1000);

            // the process performs updates, then it sens a close message to terminate all processes
            dsm.compareAndExchange("a", 1, 10);
            Thread.sleep(1000);
            dsm.compareAndExchange("c", 3, 30);
            Thread.sleep(1000);
            dsm.compareAndExchange("b", 2, 20);
            Thread.sleep(1000);

            dsm.close();
            Thread.sleep(1000);

            thread.join();
        } else if (me == 1) { // subscribes to variables a and c and performs an update on b
            Thread thread = new Thread(new Subscriber(dsm));
            thread.start();

            dsm.subscribeToVariable("a");
            Thread.sleep(1000);
            dsm.subscribeToVariable("c");
            Thread.sleep(1000);

            dsm.compareAndExchange("b", 2, 222);
            Thread.sleep(1000);

            thread.join();
        } else if (me == 2) { // subscribes to b and performs an update on b
            Thread thread = new Thread(new Subscriber(dsm));
            thread.start();

            dsm.subscribeToVariable("b");
            Thread.sleep(1000);
            dsm.compareAndExchange("b", 2, 20);
            Thread.sleep(1000);

            thread.join();
        }

        MPI.Finalize();
    }
}