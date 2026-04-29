package co.edu.uniquindio.com.proptech.structures.priorityQueue;

public class PriorityQueue<T> {


    private HeapNode<T>[] heap;
    private int size;
    private static final int INITIAL_CAPACITY = 16;


    @SuppressWarnings("unchecked")
    public PriorityQueue() {
        heap = new HeapNode[INITIAL_CAPACITY];
        size = 0;
    }


    private int parent(int i)    { return (i - 1) / 2; }
    private int leftChild(int i) { return 2 * i + 1;   }
    private int rightChild(int i){ return 2 * i + 2;   }


    private void swap(int i, int j) {
        HeapNode<T> temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }


    private void bubbleUp(int i) {
        while (i > 0 && heap[i].priority < heap[parent(i)].priority) {
            swap(i, parent(i));
            i = parent(i);
        }
    }


    private void bubbleDown(int i) {
        int smallest = i;
        int left  = leftChild(i);
        int right = rightChild(i);

        if (left  < size && heap[left].priority  < heap[smallest].priority) smallest = left;
        if (right < size && heap[right].priority < heap[smallest].priority) smallest = right;

        if (smallest != i) {
            swap(i, smallest);
            bubbleDown(smallest);
        }
    }


    @SuppressWarnings("unchecked")
    private void resize() {
        HeapNode<T>[] newHeap = new HeapNode[heap.length * 2];
        System.arraycopy(heap, 0, newHeap, 0, size);
        heap = newHeap;
    }


    public void insert(T data, int priority) {
        if (size == heap.length) resize();
        heap[size] = new HeapNode<>(data, priority);
        bubbleUp(size);
        size++;
    }


    public T extractMin() {
        if (isEmpty()) throw new RuntimeException("PriorityQueue is empty");
        T data = heap[0].data;
        heap[0] = heap[size - 1];
        heap[size - 1] = null;
        size--;
        if (!isEmpty()) bubbleDown(0);
        return data;
    }


    public T peekMin() {
        if (isEmpty()) throw new RuntimeException("PriorityQueue is empty");
        return heap[0].data;
    }


    public int peekMinPriority() {
        if (isEmpty()) throw new RuntimeException("PriorityQueue is empty");
        return heap[0].priority;
    }

    public boolean contains(T data) {
        for (int i = 0; i < size; i++)
            if (heap[i].data.equals(data)) return true;
        return false;
    }

    public boolean updatePriority(T data, int newPriority) {
        for (int i = 0; i < size; i++) {
            if (heap[i].data.equals(data)) {
                int old = heap[i].priority;
                heap[i].priority = newPriority;
                if (newPriority < old) bubbleUp(i);
                else                  bubbleDown(i);
                return true;
            }
        }
        return false;
    }

    public int size() { return size; }


    public boolean isEmpty() { return size == 0; }


    @SuppressWarnings("unchecked")
    public void clear() { heap = new HeapNode[INITIAL_CAPACITY]; size = 0; }


    @Override
    public String toString() {
        if (isEmpty()) return "PriorityQueue []";
        StringBuilder sb = new StringBuilder("PriorityQueue [");
        for (int i = 0; i < size; i++) {
            sb.append(heap[i]);
            if (i < size - 1) sb.append(", ");
        }
        return sb.append("]").toString();
    }
}