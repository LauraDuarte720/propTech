package co.edu.uniquindio.com.proptech.structures.hashTable;

import co.edu.uniquindio.com.proptech.structures.linkedList.LinkedList;

public class HashTable<K, V> {

    private EntryNode<K, V>[] table;
    private int capacity;
    private int size;
    private static final double MAX_LOAD_FACTOR = 0.75;

    @SuppressWarnings("unchecked")
    public HashTable(int initialCapacity) {
        this.capacity = nextPrime(initialCapacity);
        this.table    = new EntryNode[this.capacity];
        this.size     = 0;
    }

    public HashTable() { this(16); }

    private int hash(K key) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");
        return Math.abs(key.hashCode() % capacity);
    }

    private int nextPrime(int n) {
        if (n < 2) return 2;
        while (!isPrime(n)) n++;
        return n;
    }

    private boolean isPrime(int n) {
        if (n < 2) return false;
        for (int i = 2; i <= Math.sqrt(n); i++) if (n % i == 0) return false;
        return true;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int oldCapacity = capacity;
        capacity = nextPrime(capacity * 2);
        EntryNode<K, V>[] newTable = new EntryNode[capacity];

        for (int i = 0; i < oldCapacity; i++) {
            EntryNode<K, V> current = table[i];
            while (current != null) {
                int idx = hash(current.key);
                EntryNode<K, V> next = current.next;
                current.next = newTable[idx];
                newTable[idx] = current;
                current = next;
            }
        }
        table = newTable;
    }

    public void put(K key, V value) {
        if ((double) size / capacity >= MAX_LOAD_FACTOR) resize();
        int idx = hash(key);
        EntryNode<K, V> current = table[idx];

        while (current != null) {
            if (current.key.equals(key)) { current.value = value; return; }
            current = current.next;
        }

        EntryNode<K, V> newEntry = new EntryNode<>(key, value);
        newEntry.next = table[idx];
        table[idx]    = newEntry;
        size++;
    }

    public V get(K key) {
        EntryNode<K, V> current = table[hash(key)];
        while (current != null) {
            if (current.key.equals(key)) return current.value;
            current = current.next;
        }
        return null;
    }

    public boolean remove(K key) {
        int idx = hash(key);
        EntryNode<K, V> current = table[idx], prev = null;
        while (current != null) {
            if (current.key.equals(key)) {
                if (prev == null) table[idx] = current.next;
                else              prev.next  = current.next;
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
    }

    public boolean containsKey(K key) { return get(key) != null; }

    public int size() { return size; }

    public boolean isEmpty() { return size == 0; }

    public double loadFactor() { return (double) size / capacity; }

    public LinkedList<K> keys() {
        LinkedList<K> list = new LinkedList<>();
        for (int i = 0; i < capacity; i++) {
            EntryNode<K, V> current = table[i];
            while (current != null) { list.addLast(current.key); current = current.next; }
        }
        return list;
    }

    public LinkedList<V> values() {
        LinkedList<V> list = new LinkedList<>();
        for (int i = 0; i < capacity; i++) {
            EntryNode<K, V> current = table[i];
            while (current != null) { list.addLast(current.value); current = current.next; }
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    public void clear() { table = new EntryNode[capacity]; size = 0; }

    public String stats() {
        int maxChain = 0, usedBuckets = 0;
        for (int i = 0; i < capacity; i++) {
            int len = 0;
            EntryNode<K, V> cur = table[i];
            if (cur != null) usedBuckets++;
            while (cur != null) { len++; cur = cur.next; }
            if (len > maxChain) maxChain = len;
        }
        return String.format(
                "HashTable | capacity=%d | size=%d | loadFactor=%.2f | bucketsUsed=%d | maxChain=%d",
                capacity, size, loadFactor(), usedBuckets, maxChain);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("HashTable {\n");
        for (int i = 0; i < capacity; i++) {
            if (table[i] != null) {
                sb.append("  [").append(i).append("]: ");
                EntryNode<K, V> cur = table[i];
                while (cur != null) {
                    sb.append(cur);
                    if (cur.next != null) sb.append(" -> ");
                    cur = cur.next;
                }
                sb.append("\n");
            }
        }
        return sb.append("}").toString();
    }
}