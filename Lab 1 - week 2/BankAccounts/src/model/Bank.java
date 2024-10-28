package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class Bank {
    public List<Account> accounts;
    private static final long NO_OPERATIONS = 40000;
    private static final int NO_THREADS = 8;
    private static final int NO_ACCOUNTS = 120;
    private static final long OPERATIONS_PER_THREAD = NO_OPERATIONS/NO_THREADS;

    public Lock mtx = new ReentrantLock();

    private boolean checkerStopFlag = false;

    public Bank() {
        accounts = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Bank{" +
                "accounts: " + accounts +
                '}';
    }

    public void run() {
        initializeAccounts();

        float startTime = System.nanoTime() / 1_000_000;

        // start threads for transfer operations
        List<Thread> threads = startTransferThreads();

        // start consistency checker in its own thread
        Thread checker = startCheckerThread();

        // wait for all transfer threads to complete
        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        mtx.lock();

        // signal checker to stop and join it
        checkerStopFlag = true;
        mtx.unlock();
        try {
            checker.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // final correctness check
        runConsistencyCheck();

        float endTime = System.nanoTime() / 1_000_000; // convert to milliseconds
        System.out.println("Total time elapsed: " + (endTime-startTime) / 1000 + " seconds");
    }

    private void initializeAccounts(){
        int uid = 0;
        for (int i = 0; i < NO_ACCOUNTS; ++i){
            accounts.add(new Account(uid++,100));
        }
    }

    private List<Thread> startTransferThreads() {
        List<Thread> threads = new ArrayList<>();
        for(int i = 0; i < NO_THREADS; i++) {
            threads.add(new Thread(() -> executeOperations()));
        }
        threads.forEach(Thread::start);
        return threads;
    }

    private void executeOperations() {
        Random random = new Random();
        for(long j = 0; j < OPERATIONS_PER_THREAD; ++j) {
            int accountID1 = random.nextInt(NO_ACCOUNTS);
            int accountID2 = random.nextInt(NO_ACCOUNTS);

            if(accountID1 == accountID2) {
                j--;
                continue;
            }

            int amount = random.nextInt(25);
            accounts.get(accountID1).makeTransfer(accounts.get(accountID2), amount);
        }
    }

    private Thread startCheckerThread() {
        Thread checker = new Thread(()->{
            while (!checkerStopFlag){
                if (new Random().nextInt(10) == 0){
                    runConsistencyCheck();
                }
            }
        });

        checker.start();
        return checker;
    }

    private void runConsistencyCheck() {
        System.out.println("Started checking logs");
        AtomicInteger failedAccounts = new AtomicInteger();
        // check the balance for each account (if match the operations logged)
        accounts.forEach(account -> {
            account.mtx.lock();
            if (!account.checkConsistency()){
                failedAccounts.getAndIncrement();
            }
            account.mtx.unlock();
        });

        // symmetry check: ensuring operations in both accounts match
        for (Account account : accounts) {
            account.mtx.lock();
            for (Operation op : account.log.operations) {
                Account targetAccount = accounts.get(op.dest);
                if (!targetAccount.log.operations.contains(new Operation(OperationType.RECEIVE, op.dest, op.src, op.amountOfMoney, op.timestamp))) {
                    failedAccounts.getAndIncrement();
                }
            }
            account.mtx.unlock();
        }

        if (failedAccounts.get() > 0){
            throw new RuntimeException("Accounts are no longer consistent");
        }
        System.out.println("Ended checking logs");
    }
}
