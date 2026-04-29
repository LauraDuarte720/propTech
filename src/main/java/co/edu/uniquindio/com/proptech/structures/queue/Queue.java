package co.edu.uniquindio.com.proptech.structures.queue;

import co.edu.uniquindio.com.proptech.structures.Node;

/**
 * Queue — built from scratch using {@link Node}.
 * Principle: FIFO (First In, First Out).
 *
 * PROJECT USES:
 * - Client service requests (first to request = first to be served)
 * - Pending visits waiting to be processed
 * - Administrative task follow-up
 * - Pending alerts waiting for review
 */
public class Queue<T> {

    // -------------------------------------------------------
    // Fields
    // -------------------------------------------------------
    private Node<T> front;   // first element — the one that leaves next
    private Node<T> rear;    // last element  — the one that just entered
    private int size;

    // -------------------------------------------------------
    // Constructor
    // -------------------------------------------------------
    public Queue() {
        this.front = null;
        this.rear  = null;
        this.size  = 0;
    }

    // -------------------------------------------------------
    // Core operations
    // -------------------------------------------------------

    /**
     * Enqueue: adds an element at the rear of the queue. O(1)
     * Use case: register a new visit request or service ticket.
     */
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

    /**
     * Dequeue: removes and returns the front element. O(1)
     * Use case: process the next pending visit or service request.
     */
    public T dequeue() {
        if (isEmpty()) throw new RuntimeException("Queue is empty — no pending requests");
        T data = front.data;
        front = front.next;
        if (front == null) rear = null;   // queue became empty
        size--;
        return data;
    }

    /**
     * Peek front: returns the front element without removing it. O(1)
     * Use case: see which client or visit is next without processing it.
     */
    public T peekFront() {
        if (isEmpty()) throw new RuntimeException("Queue is empty");
        return front.data;
    }

    /**
     * Peek rear: returns the last element without removing it. O(1)
     * Use case: see the most recently added request.
     */
    public T peekRear() {
        if (isEmpty()) throw new RuntimeException("Queue is empty");
        return rear.data;
    }

    // -------------------------------------------------------
    // Search & query
    // -------------------------------------------------------

    /** Returns true if the queue contains the given data. O(n) */
    public boolean contains(T data) {
        Node<T> current = front;
        while (current != null) {
            if (current.data.equals(data)) return true;
            current = current.next;
        }
        return false;
    }

    /**
     * Returns the 0-based position from the front (0 = front), or -1 if not found. O(n)
     * Use case: tell a client their position in the waiting line.
     */
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

    /** Number of elements. O(1) */
    public int size() { return size; }

    /** Returns true if the queue has no elements. O(1) */
    public boolean isEmpty() { return front == null; }

    // -------------------------------------------------------
    // Utility
    // -------------------------------------------------------

    /** Removes all elements. O(1) */
    public void clear() { front = null; rear = null; size = 0; }

    /**
     * Appends all elements of another queue to this one, preserving order. O(n)
     * Use case: merge waiting lists from two branches.
     */
    public void merge(Queue<T> other) {
        if (other == null || other.isEmpty()) return;
        Node<T> current = other.front;
        while (current != null) { enqueue(current.data); current = current.next; }
    }

    /** String representation with front on the left. O(n) */
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