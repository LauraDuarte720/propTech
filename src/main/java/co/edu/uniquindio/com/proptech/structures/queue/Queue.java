package co.edu.uniquindio.com.proptech.structures.queue;

import co.edu.uniquindio.com.proptech.structures.Node;


public class Queue<T> {


    private Node<T> front;
    private Node<T> rear;
    private int size;


    public Queue() {
        this.front = null;
        this.rear  = null;
        this.size  = 0;
    }


    public void enqueue(T data) {
        Node<T> newNode = new Node<>(data);
        if (isEmpty()) {
            front = newNode;
            rear  = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }
        size++;
    }


    public T dequeue() {
        if (isEmpty()) throw new RuntimeException("Queue is empty — no pending requests");
        T data = front.data;
        front = front.next;
        if (front == null) rear = null;   // queue became empty
        size--;
        return data;
    }


    public T peekFront() {
        if (isEmpty()) throw new RuntimeException("Queue is empty");
        return front.data;
    }


    public T peekRear() {
        if (isEmpty()) throw new RuntimeException("Queue is empty");
        return rear.data;
    }




    public boolean contains(T data) {
        Node<T> current = front;
        while (current != null) {
            if (current.data.equals(data)) return true;
            current = current.next;
        }
        return false;
    }

    public int positionOf(T data) {
        Node<T> current = front;
        int pos = 0;
        while (current != null) {
            if (current.data.equals(data)) return pos;
            current = current.next;
            pos++;
        }
        return -1;
    }


    public int size() { return size; }


    public boolean isEmpty() { return front == null; }


    public void clear() { front = null; rear = null; size = 0; }


    public void merge(Queue<T> other) {
        if (other == null || other.isEmpty()) return;
        Node<T> current = other.front;
        while (current != null) { enqueue(current.data); current = current.next; }
    }


    @Override
    public String toString() {
        if (isEmpty()) return "Queue []";
        StringBuilder sb = new StringBuilder("Front -> [");
        Node<T> current = front;
        while (current != null) {
            sb.append(current.data);
            if (current.next != null) sb.append(", ");
            current = current.next;
        }
        return sb.append("] <- Rear").toString();
    }
}