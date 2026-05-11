package co.edu.uniquindio.com.proptech.structures.AVLTree;

import java.util.ArrayList;
import java.util.List;

public class AVLTree<T extends Comparable<T>> {

    private AVLNode<T> root;
    private int size;


    public AVLTree() {
        this.root = null;
        this.size = 0;
    }

    private int height(AVLNode<T> node) {
        return (node == null) ? 0 : node.height;
    }

    private void updateHeight(AVLNode<T> node) {
        node.height = 1 + Math.max(height(node.left), height(node.right));
    }

    private int balanceFactor(AVLNode<T> node) {
        return (node == null) ? 0 : height(node.left) - height(node.right);
    }

    private AVLNode<T> rotateRight(AVLNode<T> y) {
        AVLNode<T> x  = y.left;
        AVLNode<T> T2 = x.right;

        x.right = y;
        y.left  = T2;

        updateHeight(y);
        updateHeight(x);
        return x;
    }

    private AVLNode<T> rotateLeft(AVLNode<T> x) {
        AVLNode<T> y  = x.right;
        AVLNode<T> T2 = y.left;

        y.left  = x;
        x.right = T2;

        updateHeight(x);
        updateHeight(y);
        return y;
    }

    private AVLNode<T> rebalance(AVLNode<T> node) {
        updateHeight(node);
        int bf = balanceFactor(node);

        // Left-Left (LL)
        if (bf > 1 && balanceFactor(node.left) >= 0) {
            return rotateRight(node);
        }

        // Left-Right (LR)
        if (bf > 1 && balanceFactor(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        // Right-Right (RR)
        if (bf < -1 && balanceFactor(node.right) <= 0) {
            return rotateLeft(node);
        }

        // Right-Left (RL)
        if (bf < -1 && balanceFactor(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node; // already balanced
    }

    public void insert(T data) {
        if (data == null) throw new IllegalArgumentException("Data cannot be null.");
        root = insert(root, data);
        size++;
    }

    private AVLNode<T> insert(AVLNode<T> node, T data) {
        if (node == null) return new AVLNode<>(data);

        int cmp = data.compareTo(node.data);
        if      (cmp < 0) node.left  = insert(node.left,  data);
        else if (cmp > 0) node.right = insert(node.right, data);
        else { size--; return node; } // duplicate: skip

        return rebalance(node);
    }


    public boolean delete(T data) {
        if (data == null || root == null) return false;
        int before = size;
        root = delete(root, data);
        return size < before;
    }

    private AVLNode<T> delete(AVLNode<T> node, T data) {
        if (node == null) return null;

        int cmp = data.compareTo(node.data);
        if (cmp < 0) {
            node.left  = delete(node.left,  data);
        } else if (cmp > 0) {
            node.right = delete(node.right, data);
        } else {
            // Node found
            size--;
            if (node.left  == null) return node.right;
            if (node.right == null) return node.left;

            // Replace with in-order successor (minimum of right subtree)
            AVLNode<T> successor = minNode(node.right);
            node.data  = successor.data;
            node.right = delete(node.right, successor.data);
            size++; // compensate double decrement
        }
        return rebalance(node);
    }

    private AVLNode<T> minNode(AVLNode<T> node) {
        while (node.left != null) node = node.left;
        return node;
    }



    /**
     * Checks whether the tree contains a given value.
     *
     * @param data Value to search for
     * @return true if found, false otherwise
     */
    public boolean contains(T data) {
        return search(root, data) != null;
    }

    /**
     * Retrieves the stored value equal to the given data.
     *
     * @param data Value to search for
     * @return The stored element, or null if not found
     */
    public T get(T data) {
        AVLNode<T> result = search(root, data);
        return (result == null) ? null : result.data;
    }

    private AVLNode<T> search(AVLNode<T> node, T data) {
        if (node == null || data == null) return null;
        int cmp = data.compareTo(node.data);
        if      (cmp < 0) return search(node.left,  data);
        else if (cmp > 0) return search(node.right, data);
        else              return node;
    }




    public List<T> rangeSearch(T min, T max) {
        if (min == null || max == null)
            throw new IllegalArgumentException("Range bounds cannot be null.");
        if (min.compareTo(max) > 0)
            throw new IllegalArgumentException("min cannot be greater than max.");

        List<T> result = new ArrayList<>();
        rangeSearch(root, min, max, result);
        return result;
    }

    private void rangeSearch(AVLNode<T> node, T min, T max, List<T> result) {
        if (node == null) return;

        int cmpMin = min.compareTo(node.data);
        int cmpMax = max.compareTo(node.data);

        // Explore left subtree only if it may contain values >= min
        if (cmpMin < 0) rangeSearch(node.left, min, max, result);

        // Include current node if it falls within range
        if (cmpMin <= 0 && cmpMax >= 0) result.add(node.data);

        // Explore right subtree only if it may contain values <= max
        if (cmpMax > 0) rangeSearch(node.right, min, max, result);
    }

    /**
     * Returns the smallest element in the tree.
     * @throws IllegalStateException if the tree is empty
     */
    public T minimum() {
        if (root == null) throw new IllegalStateException("Tree is empty.");
        return minNode(root).data;
    }

    /**
     * Returns the largest element in the tree.
     * @throws IllegalStateException if the tree is empty
     */
    public T maximum() {
        if (root == null) throw new IllegalStateException("Tree is empty.");
        AVLNode<T> node = root;
        while (node.right != null) node = node.right;
        return node.data;
    }



    /** In-order traversal — returns elements sorted ascending */
    public List<T> inOrder() {
        List<T> list = new ArrayList<>();
        inOrder(root, list);
        return list;
    }

    private void inOrder(AVLNode<T> node, List<T> list) {
        if (node == null) return;
        inOrder(node.left,  list);
        list.add(node.data);
        inOrder(node.right, list);
    }

    /** Pre-order traversal */
    public List<T> preOrder() {
        List<T> list = new ArrayList<>();
        preOrder(root, list);
        return list;
    }

    private void preOrder(AVLNode<T> node, List<T> list) {
        if (node == null) return;
        list.add(node.data);
        preOrder(node.left,  list);
        preOrder(node.right, list);
    }

    /** Post-order traversal */
    public List<T> postOrder() {
        List<T> list = new ArrayList<>();
        postOrder(root, list);
        return list;
    }

    private void postOrder(AVLNode<T> node, List<T> list) {
        if (node == null) return;
        postOrder(node.left,  list);
        postOrder(node.right, list);
        list.add(node.data);
    }



    public boolean isEmpty() { return root == null; }
    public int     getSize() { return size; }
    public int     getHeight() { return height(root); }


    public void printTree() {
        System.out.println("=== AVL Tree ===");
        printTree(root, "", true);
    }

    private void printTree(AVLNode<T> node, String prefix, boolean isLast) {
        if (node == null) return;
        System.out.println(prefix + (isLast ? "└── " : "├── ")
                + node.data
                + "  [h=" + node.height + ", bf=" + balanceFactor(node) + "]");
        String newPrefix = prefix + (isLast ? "    " : "│   ");
        printTree(node.left,  newPrefix, false);
        printTree(node.right, newPrefix, true);
    }

}