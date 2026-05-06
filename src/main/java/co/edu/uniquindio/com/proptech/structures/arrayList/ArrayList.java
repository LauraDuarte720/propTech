package co.edu.uniquindio.com.proptech.structures.arrayList;

public class ArrayList<T> implements Iterable<T> {

    private Object[] data;
    private int size;
    private static final int DEFAULT_CAPACITY = 16;

    public ArrayList() {
        this.data = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    public ArrayList(int initialCapacity) {
        if (initialCapacity < 1)
            throw new IllegalArgumentException("Capacity must be >= 1");
        this.data = new Object[initialCapacity];
        this.size = 0;
    }

    private void grow() {
        int newCapacity = data.length * 2;
        Object[] newData = new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newData[i] = data[i];
        }
        data = newData;
    }

    private void shrink() {
        if (data.length > DEFAULT_CAPACITY && size < data.length / 4) {
            int newCapacity = Math.max(data.length / 2, DEFAULT_CAPACITY);
            Object[] newData = new Object[newCapacity];
            for (int i = 0; i < size; i++) {
                newData[i] = data[i];
            }
            data = newData;
        }
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException(
                    "Index " + index + " out of bounds for size " + size);
    }

    private void checkIndexForAdd(int index) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException(
                    "Index " + index + " out of bounds for size " + size);
    }

    public T add(T element) {
        if (size == data.length) grow();
        data[size] = element;
        size++;
        return element;
    }

    public void add(int index, T element) {
        checkIndexForAdd(index);
        if (size == data.length) grow();
        for (int i = size; i > index; i--) {
            data[i] = data[i - 1];
        }
        data[index] = element;
        size++;
    }

    public void addAll(ArrayList<T> other) {
        if (other == null || other.isEmpty()) return;
        for (int i = 0; i < other.size(); i++) {
            add(other.get(i));
        }
    }

    @SuppressWarnings("unchecked")
    public T get(int index) {
        checkIndex(index);
        return (T) data[index];
    }

    @SuppressWarnings("unchecked")
    public T set(int index, T element) {
        checkIndex(index);
        T old = (T) data[index];
        data[index] = element;
        return old;
    }

    @SuppressWarnings("unchecked")
    public T getFirst() {
        if (isEmpty()) throw new RuntimeException("ArrayList is empty");
        return (T) data[0];
    }

    @SuppressWarnings("unchecked")
    public T getLast() {
        if (isEmpty()) throw new RuntimeException("ArrayList is empty");
        return (T) data[size - 1];
    }

    @SuppressWarnings("unchecked")
    public T remove(int index) {
        checkIndex(index);
        T old = (T) data[index];
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }
        data[size - 1] = null;
        size--;
        shrink();
        return old;
    }

    public boolean remove(T element) {
        int index = indexOf(element);
        if (index == -1) return false;
        remove(index);
        return true;
    }

    @SuppressWarnings("unchecked")
    public T removeLast() {
        if (isEmpty()) throw new RuntimeException("ArrayList is empty");
        T old = (T) data[size - 1];
        data[size - 1] = null;
        size--;
        shrink();
        return old;
    }

    public void removeRange(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > size || fromIndex >= toIndex)
            throw new IndexOutOfBoundsException(
                    "Invalid range [" + fromIndex + ", " + toIndex + ")");
        int count = toIndex - fromIndex;
        for (int i = fromIndex; i < size - count; i++) {
            data[i] = data[i + count];
        }
        for (int i = size - count; i < size; i++) data[i] = null;
        size -= count;
        shrink();
    }

    public boolean contains(T element) {
        return indexOf(element) != -1;
    }

    public int indexOf(T element) {
        for (int i = 0; i < size; i++) {
            if (element == null ? data[i] == null : element.equals(data[i]))
                return i;
        }
        return -1;
    }

    public int lastIndexOf(T element) {
        for (int i = size - 1; i >= 0; i--) {
            if (element == null ? data[i] == null : element.equals(data[i]))
                return i;
        }
        return -1;
    }

    @SuppressWarnings("unchecked")
    public void sort() {
        for (int i = 1; i < size; i++) {
            T key = (T) data[i];
            int j = i - 1;
            while (j >= 0 && ((Comparable<T>) data[j]).compareTo(key) > 0) {
                data[j + 1] = data[j];
                j--;
            }
            data[j + 1] = key;
        }
    }

    @SuppressWarnings("unchecked")
    public void sort(Comparator<T> comparator) {
        for (int i = 1; i < size; i++) {
            T key = (T) data[i];
            int j = i - 1;
            while (j >= 0 && comparator.compare((T) data[j], key) > 0) {
                data[j + 1] = data[j];
                j--;
            }
            data[j + 1] = key;
        }
    }

    public ArrayList<T> filter(Predicate<T> predicate) {
        ArrayList<T> result = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            T element = get(i);
            if (predicate.test(element)) result.add(element);
        }
        return result;
    }

    public ArrayList<T> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > size || fromIndex >= toIndex)
            throw new IndexOutOfBoundsException(
                    "Invalid range [" + fromIndex + ", " + toIndex + ")");
        ArrayList<T> sub = new ArrayList<>(toIndex - fromIndex);
        for (int i = fromIndex; i < toIndex; i++) sub.add(get(i));
        return sub;
    }

    public int size() { return size; }

    public boolean isEmpty() { return size == 0; }

    public int capacity() { return data.length; }

    public void clear() {
        data = new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    public void trimToSize() {
        if (size < data.length) {
            Object[] trimmed = new Object[size == 0 ? 1 : size];
            for (int i = 0; i < size; i++) trimmed[i] = data[i];
            data = trimmed;
        }
    }

    public Object[] toArray() {
        Object[] arr = new Object[size];
        for (int i = 0; i < size; i++) arr[i] = data[i];
        return arr;
    }

    public ArrayList<T> copy() {
        ArrayList<T> clone = new ArrayList<>(size == 0 ? DEFAULT_CAPACITY : size);
        for (int i = 0; i < size; i++) clone.add(get(i));
        return clone;
    }

    public void reverse() {
        for (int i = 0, j = size - 1; i < j; i++, j--) {
            Object temp = data[i];
            data[i] = data[j];
            data[j] = temp;
        }
    }

    public void swap(int i, int j) {
        checkIndex(i);
        checkIndex(j);
        Object temp = data[i];
        data[i] = data[j];
        data[j] = temp;
    }

    public String stats() {
        return "ArrayList | size=" + size + " | capacity=" + data.length
                + " | loadFactor=" + String.format("%.2f", (double) size / data.length);
    }

    @Override
    public String toString() {
        if (isEmpty()) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(data[i]);
            if (i < size - 1) sb.append(", ");
        }
        return sb.append("]").toString();
    }

    @Override
    public java.util.Iterator<T> iterator() {
        return new ArrayListIterator();
    }

    private class ArrayListIterator implements java.util.Iterator<T> {

        private int cursor = 0;

        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        @Override
        @SuppressWarnings("unchecked")
        public T next() {
            if (!hasNext()) throw new java.util.NoSuchElementException();
            return (T) data[cursor++];
        }
    }

    @FunctionalInterface
    public interface Predicate<T> {
        boolean test(T element);
    }

    @FunctionalInterface
    public interface Comparator<T> {
        int compare(T a, T b);
    }
}