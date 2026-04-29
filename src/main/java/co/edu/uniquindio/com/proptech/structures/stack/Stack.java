package co.edu.uniquindio.com.proptech.structures.stack;

import co.edu.uniquindio.com.proptech.structures.Node;


public class Stack<T> {

    private Node<T> top;
    private int size;

    public Stack() {
        this.top  = null;
        this.size = 0;
    }


    public void push(T data) {
        Node<T> newNode = new Node<>(data);
        newNode.next = top;
        top = newNode;
        size++;
    }


    public T pop() {
        if (isEmpty()) throw new RuntimeException("Stack is empty — no actions to undo");
        T data = top.data;
        top = top.next;
        size--;
        return data;
    }


    public T peek() {
        if (isEmpty()) throw new RuntimeException("Stack is empty");
        return top.data;
    }

    public boolean contains(T data) {
        Node<T> current = top;
        while (current != null) {
            if (current.data.equals(data)) return true;
            current = current.next;
        }
        return false;
    }


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


    public int size() { return size; }


    public boolean isEmpty() { return top == null; }


    public void clear() { top = null; size = 0; }


    public Stack<T> reversedCopy() {
        Stack<T> copy = new Stack<>();
        Node<T> current = top;
        while (current != null) { copy.push(current.data); current = current.next; }
        return copy;
    }


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
