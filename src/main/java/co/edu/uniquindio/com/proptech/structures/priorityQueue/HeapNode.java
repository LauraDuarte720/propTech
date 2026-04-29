package co.edu.uniquindio.com.proptech.structures.priorityQueue;

public class HeapNode<T> {

    public T data;
    public int priority;   // 1 = most urgent

    public HeapNode(T data, int priority) {
        this.data     = data;
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "[" + data + " | priority=" + priority + "]";
    }
}