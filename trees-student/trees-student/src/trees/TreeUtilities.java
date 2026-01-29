/*
 * Copyright 2025 Marc Liberatore.
 */
package trees;

import java.util.ArrayList;
import java.util.List;

public class TreeUtilities {
    /**
     * Perform an in-order traversal of the tree rooted at the given node, and return
     * a list of the elements in the order they were visited.
     * @param node
     * @return a list of elements from the tree from an in-order traversal starting at node
     */
    static <E> List<E> inOrder(Node<E> node) {
        List<E> result = new ArrayList<>();
        inOrderHelper(node, result);
        return result; 
    }

    private static <E> void inOrderHelper(Node<E> node, List<E> result) {
        if (node == null) return;
        inOrderHelper(node.left, result);
        result.add(node.data);
        inOrderHelper(node.right, result);

    }

    /**
     * Returns the height of the node n.
     * 
     * null has a height of -1; otherwise, the height is defined as 
     * 1 + the height of the larger of the node's two subtrees.
     * 
     * @param n
     * @return the height of the node n
     */
    static <E> int height(Node<E> n) {
        if (n == null) return -1;
        return 1 + Math.max(height(n.left), height(n.right));
    }

    /**
     * Return a new, balanced tree containing all the values of the old tree bst.
     * @param bst
     * @return a new, balanced tree containing all the values of the old tree bst
     */
    static <E extends Comparable<E>> BinarySearchTree<E> intoBalanced(BinarySearchTree<E> bst) {
        List<E> sortedValues = inOrder(bst.root); 
        BinarySearchTree<E> balanced = new BinarySearchTree<>();
        buildBalanced(sortedValues, balanced, 0, sortedValues.size() - 1);
        return balanced;
    }

private static <E extends Comparable<E>> void buildBalanced(
            List<E> values, BinarySearchTree<E> tree, int start, int end) {
        if (start > end) return;
        int mid = (start + end) / 2;
        tree.add(values.get(mid));
        buildBalanced(values, tree, start, mid - 1);
        buildBalanced(values, tree, mid + 1, end);
    }

    /**
     * Returns true iff the tree rooted at n is a Binary Search Tree.
     * 
     * It must have no more than two children per node.
     * 
     * Each node's value must be greater than all the values in its left subtree, and smaller
     * than all the values in its right subtree. (This implies duplicate values are not allowed.)
     * 
     * @param n true iff the tree rooted at n is a Binary Search Tree
     * @return 
     */
    static <E extends Comparable<E>> boolean isBST(Node<E> n) {
        return isBSTHelper(n, null, null);
    }

    private static <E extends Comparable<E>> boolean isBSTHelper(Node<E> n, E min, E max) {
    if (n == null) return true;
    if ((min != null && n.data.compareTo(min) <= 0) ||
        (max != null && n.data.compareTo(max) >= 0)) {
        return false;
    }
    return isBSTHelper(n.left, min, n.data) &&
           isBSTHelper(n.right, n.data, max);
}

    /**
     * Returns true iff the tree rooted at n is an AVL tree.
     * 
     * AVL trees are Binary Search trees with the additional property that 
     * every node in the tree has the AVL property.
     * 
     * A node has the AVL property iff the height of its left subtree and the
     * height of its right subtree differ by no more than 1.
     * 
     * @param <E>
     * @param n
     * @returntrue iff the tree rooted at n is an AVL tree
     */
    static <E extends Comparable<E>> boolean isAVLTree(Node<E> n) {
    return checkAVL(n).isAVL;
}

private static class AVLInfo {
    int height;
    boolean isAVL;
    AVLInfo(int h, boolean b) {
        height = h;
        isAVL = b;
    }
}

private static <E extends Comparable<E>> AVLInfo checkAVL(Node<E> n) {
    if (n == null) return new AVLInfo(-1, true);

    AVLInfo left = checkAVL(n.left);
    AVLInfo right = checkAVL(n.right);

    boolean balanced = Math.abs(left.height - right.height) <= 1;
    boolean orderedLeft = (n.left == null || n.left.data.compareTo(n.data) < 0);
    boolean orderedRight = (n.right == null || n.right.data.compareTo(n.data) > 0);

    boolean isAVL = left.isAVL && right.isAVL && balanced && orderedLeft && orderedRight;
    int height = 1 + Math.max(left.height, right.height);

    return new AVLInfo(height, isAVL);
}

    /**
     * Returns true iff the subtrees rooted at n and m have the same values 
     * and same structure. 
     * 
     * Only checks child references, not parent references.
     * @param n
     * @param m
     * @return true iff the subtrees rooted at n and m have the same values and same structure
     */
    static <E> boolean equalSubtrees(Node<E> n, Node<E> m) {
        if (n == null && m == null) return true;   
        if (n == null || m == null) return false; 
        if (!n.data.equals(m.data)) return false;
        return equalSubtrees(n.left, m.left) && equalSubtrees(n.right, m.right);
    }
}