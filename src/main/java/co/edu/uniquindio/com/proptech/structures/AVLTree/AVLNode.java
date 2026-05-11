package co.edu.uniquindio.com.proptech.structures.AVLTree;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@Setter
public class AVLNode<T extends Comparable<T>> {

    // =========================================================
    // ATTRIBUTES
    // =========================================================

    T data;
    AVLNode<T> left;
    AVLNode<T> right;
    int height;

    public AVLNode(T data) {
        this.data   = data;
        this.left   = null;
        this.right  = null;
        this.height = 1;
    }

}
