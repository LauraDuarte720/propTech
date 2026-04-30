package co.edu.uniquindio.com.proptech.structures.linkedList;

import co.edu.uniquindio.com.proptech.structures.Node;

public class LinkedList<T> {


    private Node<T> head;
    private int size;

    public LinkedList() {
        this.head = null;
        this.size = 0;
    }



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


    public void addFirst(T data) {
        Node<T> newNode = new Node<>(data);
        newNode.next = head;
        head = newNode;
        size++;
    }


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


    public T removeFirst() {
        if (isEmpty()) throw new RuntimeException("List is empty");
        T data = head.data;
        head = head.next;
        size--;
        return data;
    }


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


    public T get(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        Node<T> current = head;
        for (int i = 0; i < index; i++) current = current.next;
        return current.data;
    }

    public T peekFirst() {
        if (isEmpty()) throw new RuntimeException("List is empty");
        return head.data;
    }


    public T peekLast() {
        if (isEmpty()) throw new RuntimeException("List is empty");
        Node<T> current = head;
        while (current.next != null) current = current.next;
        return current.data;
    }

    public boolean contains(T data) {
        Node<T> current = head;
        while (current != null) {
            if (current.data.equals(data)) return true;
            current = current.next;
        }
        return false;
    }


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


    public int size() { return size; }

    public boolean isEmpty() { return size == 0; }

    public void clear() { head = null; size = 0; }

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

    public Object[] toArray() {
        Object[] arr = new Object[size];
        Node<T> current = head;
        for (int i = 0; i < size; i++) { arr[i] = current.data; current = current.next; }
        return arr;
    }

    public void set(int index, T data) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        Node<T> current = head;
        for (int i = 0; i < index; i++) current = current.next;
        current.data = data;
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
