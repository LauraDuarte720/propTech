package co.edu.uniquindio.com.proptech.structures.hashTable;


public class EntryNode<K,V> {

    public K key;
    public V value;
    public EntryNode<K, V> next;

    public EntryNode(K key, V value) {
        this.key   = key;
        this.value = value;
        this.next  = null;
    }

    @Override
    public String toString() {
        return "(" + key + " -> " + value + ")";
    }
}