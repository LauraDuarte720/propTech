package co.edu.uniquindio.com.proptech.structures.priorityQueue;

public class HeapNode<T> {

    public T data;

    public HeapNode(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "[" + data + "]";
    }
}