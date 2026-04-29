package co.edu.uniquindio.com.proptech.structures.stack;

import co.edu.uniquindio.com.proptech.structures.Node;

/**
 * Stack — built from scratch using {@link Node}.
 * Principle: LIFO (Last In, First Out).
 *
 * PROJECT USES:
 * - Undo recent changes to a property listing
 * - Reverse modifications to a property's status
 * - Maintain a history of administrative actions
 */
public class Stack<T> {

    // -------------------------------------------------------
    // Fields
    // -------------------------------------------------------
    private Node<T> top;
    private int size;

    // -------------------------------------------------------
    // Constructor
    // -------------------------------------------------------
    public Stack() {
        this.top  = null;
        this.size = 0;
    }

    // -------------------------------------------------------
    // Core operations
    // -------------------------------------------------------

    /**
     * Push: adds an element on top of the stack. O(1)
     * Use case: record a property-status change before applying it,
     *           so it can be undone later.
     */
    public void push(T data) {
        Node<T> newNode = new Node<>(data);
        newNode.next = top;
        top = newNode;
        size++;
    }

    /**
     * Pop: removes and returns the top element. O(1)
     * Use case: undo the last administrative action or status change.
     */
    public T pop() {
        if (isEmpty()) throw new RuntimeException("Stack is empty — no actions to undo");
        T data = top.data;
        top = top.next;
        size--;
        return data;
    }

    /**
     * Peek: returns the top element without removing it. O(1)
     * Use case: preview the last action before confirming the undo.
     */
    public T peek() {
        if (isEmpty()) throw new RuntimeException("Stack is empty");
        return top.data;
    }

    // -------------------------------------------------------
    // Search & query
    // -------------------------------------------------------

    /** Returns true if the stack contains the given data. O(n) */
    public boolean contains(T data) {
        Node<T> current = top;
        while (current != null) {
            if (current.data.equals(data)) return true;
            current = current.next;
        }
        return false;
    }

    /**
     * Returns the 0-based position from the top (0 = top), or -1 if not found. O(n)
     * Useful for knowing how old an action is in the history.
     */
    public int search(T data) {
        Node<T> current = top;
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

    /** Returns true if the stack has no elements. O(1) */
    public boolean isEmpty() { return top == null; }

    // -------------------------------------------------------
    // Utility
    // -------------------------------------------------------

    /** Removes all elements. O(1) */
    public void clear() { top = null; size = 0; }

    /**
     * Returns a new stack with elements in reversed order (bottom becomes new top). O(n)
     * Useful for traversing the history in chronological order.
     */
    public Stack<T> reversedCopy() {
        Stack<T> copy = new Stack<>();
        Node<T> current = top;
        while (current != null) { copy.push(current.data); current = current.next; }
        return copy;
    }

    /** String representation with top on the left. O(n) */
    @Override
    public String toString() {
        if (isEmpty()) return "Stack []";
        StringBuilder sb = new StringBuilder("Top -> [");
        Node<T> current = top;
        while (current != null) {
            sb.append(current.data);
            if (current.next != null) sb.append(", ");
            current = current.next;
        }
        return sb.append("]").toString();
    }
}
