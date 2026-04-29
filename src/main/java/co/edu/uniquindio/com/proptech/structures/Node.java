package co.edu.uniquindio.com.proptech.structures;


public class Node<T> {

    public T data;
    public Node<T> next;

    public Node(T data) {
        this.data = data;
        this.next = null;
    }

    @Override
    public String toString() {
        return data != null ? data.toString() : "null";
    }
}