package model;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account {
    public int uid;
    public Log log;
    public int initialBalance;
    public int balance;
    public Lock mtx;

    public Account(int uid, int balance) {
        this.uid = uid;
        this.log = new Log();
        this.initialBalance = balance;
        this.balance = balance;
        this.mtx = new ReentrantLock();
    };

    public boolean makeTransfer(Account other, int sum) {
        if(sum > balance) {
            return false;
        }

        // locking accounts before transfer
        // if a1 wants to transfer to a2 and a2 to a1, there would be a deadlock
        // so the order matters, and the mutexes are blocked in the order of accounts' ids
        if(this.uid < other.uid) {
            this.mtx.lock();
            other.mtx.lock();
        } else {
            other.mtx.lock();
            this.mtx.lock();
        }

        // perform transfer
        balance -= sum;
        other.balance += sum;

        // log the transfer operations
        long timestamp = System.currentTimeMillis();
        logTransfer(OperationType.SEND, this.uid, other.uid, sum, timestamp);
        other.logTransfer(OperationType.RECEIVE, other.uid, this.uid, sum, timestamp);

        // unlocking the accounts after transfer
        this.mtx.unlock();
        other.mtx.unlock();

        return true;
    }

    // log transfer operations in both accounts' logs
    public void logTransfer(String type, int src, int dest, int sum, long timestamp) {
        log.log(type, sum, src, dest, timestamp);
    }


    public boolean checkConsistency() {
        int calculateBalance = this.initialBalance;
        for(Operation operation: this.log.operations) {
            if(operation.type.equals(OperationType.SEND))
                calculateBalance -= operation.amountOfMoney;
            else if(operation.type.equals(OperationType.RECEIVE))
                calculateBalance += operation.amountOfMoney;
        }
        return calculateBalance == this.balance;
    }
}
