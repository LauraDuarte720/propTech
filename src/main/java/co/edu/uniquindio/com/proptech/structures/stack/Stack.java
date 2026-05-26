package co.edu.uniquindio.com.proptech.structures.stack;

import co.edu.uniquindio.com.proptech.structures.Node;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class Stack<T> implements Iterable<T> {

    private Node<T> top;
    private int size;

    // null = sin límite
    private final Integer maxSize;

    public Stack() {
        this.top = null;
        this.size = 0;
        this.maxSize = null;
    }

    // Constructor con tamaño máximo
    public Stack(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("Max size must be greater than 0");
        }

        this.top = null;
        this.size = 0;
        this.maxSize = maxSize;
    }

    public void push(T data) {

        if (isFull()) {
            throw new RuntimeException("Stack is full");
        }

        Node<T> newNode = new Node<>(data);
        newNode.next = top;
        top = newNode;
        size++;
    }

    public T pop() {
        if (isEmpty()) {
            throw new RuntimeException("Stack is empty — no actions to undo");
        }

        T data = top.data;
        top = top.next;
        size--;

        return data;
    }

    public T peek() {
        if (isEmpty()) {
            throw new RuntimeException("Stack is empty");
        }

        return top.data;
    }

    public boolean contains(T data) {
        Node<T> current = top;

        while (current != null) {

            if (current.data.equals(data)) {
                return true;
            }

            current = current.next;
        }

        return false;
    }

    public int search(T data) {

        Node<T> current = top;
        int pos = 0;

        while (current != null) {

            if (current.data.equals(data)) {
                return pos;
            }

            current = current.next;
            pos++;
        }

        return -1;
    }

    public int size() {
        return size;
    }

    public Integer getMaxSize() {
        return maxSize;
    }

    public boolean isFull() {
        return maxSize != null && size >= maxSize;
    }

    public boolean isEmpty() {
        return top == null;
    }

    public void clear() {
        top = null;
        size = 0;
    }

    public Stack<T> reversedCopy() {

        Stack<T> copy = maxSize == null
                ? new Stack<>()
                : new Stack<>(maxSize);

        Node<T> current = top;

        while (current != null) {
            copy.push(current.data);
            current = current.next;
        }

        return copy;
    }

    @Override
    public String toString() {

        if (isEmpty()) {
            return "Stack []";
        }

        StringBuilder sb = new StringBuilder("Top -> [");

        Node<T> current = top;

        while (current != null) {

            sb.append(current.data);

            if (current.next != null) {
                sb.append(", ");
            }

            current = current.next;
        }

        return sb.append("]").toString();
    }

    public List<T> toJavaList() {

        List<T> list = new java.util.ArrayList<>();

        Node<T> current = top;

        while (current != null) {
            list.add(current.data);
            current = current.next;
        }

        return list;
    }

    @Override
    public Iterator<T> iterator() {

        return new Iterator<T>() {

            private Node<T> current = top;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public T next() {

                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                T data = current.data;
                current = current.next;

                return data;
            }
        };
    }
}