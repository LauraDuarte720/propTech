package co.edu.uniquindio.com.proptech.structures.linkedList;

import co.edu.uniquindio.com.proptech.structures.Node;

/**
 * Singly Linked List — built from scratch using {@link Node}.
 *
 * PROJECT USES:
 * - Visit history per client
 * - Browsing history (properties consulted)
 * - Favorites list per client
 * - Properties assigned to an advisor
 * - Registered contracts
 * - Completed operations
 */
public class LinkedList<T> {

    // -------------------------------------------------------
    // Fields
    // -------------------------------------------------------
    private Node<T> head;
    private int size;

    // -------------------------------------------------------
    // Constructor
    // -------------------------------------------------------
    public LinkedList() {
        this.head = null;
        this.size = 0;
    }

    // -------------------------------------------------------
    // Insertion
    // -------------------------------------------------------

    /** Appends an element at the end of the list. O(n) */
    public void addLast(T data) {
        Node<T> newNode = new Node<>(data);
        if (head == null) {
            head = newNode;
        } else {
            Node<T> current = head;
            while (current.next != null) current = current.next;
            current.next = newNode;
        }
        size++;
    }

    /** Prepends an element at the beginning of the list. O(1) */
    public void addFirst(T data) {
        Node<T> newNode = new Node<>(data);
        newNode.next = head;
        head = newNode;
        size++;
    }

    /** Inserts an element at the given 0-based index. O(n) */
    public void addAt(int index, T data) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        if (index == 0) { addFirst(data); return; }

        Node<T> newNode = new Node<>(data);
        Node<T> current = head;
        for (int i = 0; i < index - 1; i++) current = current.next;
        newNode.next = current.next;
        current.next = newNode;
        size++;
    }

    // -------------------------------------------------------
    // Removal
    // -------------------------------------------------------

    /** Removes and returns the first element. O(1) */
    public T removeFirst() {
        if (isEmpty()) throw new RuntimeException("List is empty");
        T data = head.data;
        head = head.next;
        size--;
        return data;
    }

    /** Removes and returns the last element. O(n) */
    public T removeLast() {
        if (isEmpty()) throw new RuntimeException("List is empty");
        if (head.next == null) {
            T data = head.data;
            head = null;
            size--;
            return data;
        }
        Node<T> current = head;
        while (current.next.next != null) current = current.next;
        T data = current.next.data;
        current.next = null;
        size--;
        return data;
    }

    /** Removes the first occurrence of the given data. O(n) */
    public boolean remove(T data) {
        if (isEmpty()) return false;
        if (head.data.equals(data)) { head = head.next; size--; return true; }
        Node<T> current = head;
        while (current.next != null) {
            if (current.next.data.equals(data)) {
                current.next = current.next.next;
                size--;
                return true;
            }
            current = current.next;
        }
        return false;
    }

    /** Removes and returns the element at the given index. O(n) */
    public T removeAt(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        if (index == 0) return removeFirst();
        Node<T> current = head;
        for (int i = 0; i < index - 1; i++) current = current.next;
        T data = current.next.data;
        current.next = current.next.next;
        size--;
        return data;
    }

    // -------------------------------------------------------
    // Access
    // -------------------------------------------------------

    /** Returns the element at the given index without removing it. O(n) */
    public T get(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        Node<T> current = head;
        for (int i = 0; i < index; i++) current = current.next;
        return current.data;
    }

    /** Returns the first element without removing it. O(1) */
    public T peekFirst() {
        if (isEmpty()) throw new RuntimeException("List is empty");
        return head.data;
    }

    /** Returns the last element without removing it. O(n) */
    public T peekLast() {
        if (isEmpty()) throw new RuntimeException("List is empty");
        Node<T> current = head;
        while (current.next != null) current = current.next;
        return current.data;
    }

    // -------------------------------------------------------
    // Search
    // -------------------------------------------------------

    /** Returns true if the list contains the given data. O(n) */
    public boolean contains(T data) {
        Node<T> current = head;
        while (current != null) {
            if (current.data.equals(data)) return true;
            current = current.next;
        }
        return false;
    }

    /** Returns the 0-based index of the first occurrence, or -1 if not found. O(n) */
    public int indexOf(T data) {
        Node<T> current = head;
        int index = 0;
        while (current != null) {
            if (current.data.equals(data)) return index;
            current = current.next;
            index++;
        }
        return -1;
    }

    // -------------------------------------------------------
    // Utility
    // -------------------------------------------------------

    /** Number of elements. O(1) */
    public int size() { return size; }

    /** Returns true if the list has no elements. O(1) */
    public boolean isEmpty() { return size == 0; }

    /** Removes all elements. O(1) */
    public void clear() { head = null; size = 0; }

    /** Reverses the list in-place. O(n) */
    public void reverse() {
        Node<T> prev = null, current = head;
        while (current != null) {
            Node<T> next = current.next;
            current.next = prev;
            prev = current;
            current = next;
        }
        head = prev;
    }

    /** Converts the list to an Object array. O(n) */
    public Object[] toArray() {
        Object[] arr = new Object[size];
        Node<T> current = head;
        for (int i = 0; i < size; i++) { arr[i] = current.data; current = current.next; }
        return arr;
    }

    @Override
    public String toString() {
        if (isEmpty()) return "[]";
        StringBuilder sb = new StringBuilder("[");
        Node<T> current = head;
        while (current != null) {
            sb.append(current.data);
            if (current.next != null) sb.append(" -> ");
            current = current.next;
        }
        return sb.append("]").toString();
    }
}
