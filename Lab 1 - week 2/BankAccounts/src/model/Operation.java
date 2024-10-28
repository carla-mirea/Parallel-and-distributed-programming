package model;

import java.util.Objects;

public final class Operation {
    public int src;
    public int dest;
    public String type;
    public int amountOfMoney;
    public long timestamp;

    public Operation(String type, int src, int dest, int amountOfMoney, long timestamp) {
        this.src = src;
        this.dest = dest;
        this.type = type;
        this.amountOfMoney = amountOfMoney;
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Operation operation = (Operation) o;
        return src == operation.src &&
                dest == operation.dest &&
                amountOfMoney == operation.amountOfMoney &&
                timestamp == operation.timestamp;
    }

    @Override
    public int hashCode() {
        return Objects.hash(src, dest, amountOfMoney, timestamp);
    }

    @Override
    public String toString() {
        return "Operation{ " +
                "src: " + src +
                ", dest: " + dest +
                ", type: " + type +
                ", amount: " + amountOfMoney +
                ", timestamp: " + timestamp +
                " }";
    }
}
