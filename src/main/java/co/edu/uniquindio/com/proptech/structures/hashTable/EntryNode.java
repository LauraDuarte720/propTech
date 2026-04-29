package co.edu.uniquindio.com.proptech.structures.hashTable;


public class EntryNode<V> {

    public String key;
    public V value;
    public EntryNode<V> next;

    public EntryNode(String key, V value) {
        this.key   = key;
        this.value = value;
        this.next  = null;
    }

    @Override
    public String toString() {
        return "(" + key + " -> " + value + ")";
    }
}