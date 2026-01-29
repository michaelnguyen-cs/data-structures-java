/*
 * Copyright 2025 Marc Liberatore.
 */
package trees;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

public class AVLTree<E extends Comparable<E>> implements Iterable<E> {
    Node<E> root;
    int size;

    /**
     * Return the size of (number of elements stored in) the tree.
     * @return the size of the tree
     */
    public int size() {
        return size;
    }

    /**
     * Return the height of a subtree rooted at a given node; empty trees (that is,
     * when the node is null) are defined to have a height of -1.
     * @param n a node
     * @return the height of the subtree rooted at n
     */
    private int height(Node<E> n) {
        if (n == null) return -1;
        return 1 + Math.max(height(n.left), height(n.right));
    }

    /**
     * Return true iff the node n is a valid AVL tree node, that is, if it
     * is "balanced" according to the AVL property. In other words, the heights
     * of its left and right subtrees differ by no more than 1.
     * @param n
     * @return
     */
    private boolean isAVL(Node<E> n) {
    if (n == null) return true;
    int hl = height(n.left);
    int hr = height(n.right);
    if (Math.abs(hl - hr) > 1) return false;
    return isAVL(n.left) && isAVL(n.right);
}


    /**
     * Return true iff the tree contains the value e.
     * @param e
     * @return true iff the tree contains the value e
     */
    public boolean contains(E e) {
        return find(e) != null;
    }

    /**
     * Helper method for find().
     * 
     * Recursively walk the BST rooted at n to find and return the node,
     * containing a value equals() to e, or null if not found.
     * @return the node containing the value equals to e if it's in the tree rooted at n, null otherwise
     */
    private Node<E> find(E e, Node<E> n) {
        if (n == null) {
            return null;
        } else if (e.equals(n.data)) {
            return n;
        } else if (e.compareTo(n.data) < 0) { // left
            return find(e, n.left);
        } else {  // right
            return find(e, n.right);
        }
    }
    
    /**
     * @param e
     * @return the node containing a value equals() to e contained in the tree, or null if no such value is found
     */
    private Node<E> find(E e) {
        return find(e, root);
    }

    /**
     * @param e
     * @return the value equals() to e contained in the tree, or null if no such value is found
     */
    public E get(E e) {
        Node<E> n = find(e);
        return (n == null) ? null : n.data;
    }

    /**
     * Add e to the tree.
     * 
     * e is overwritten if it's already in the tree -- no duplication allowed.
     * @param e
     */
    public void add(E e) {
        if (root == null) {
            root = new Node<>(e);
            size = 1;
            return;
        }
        add(e, root);
    }

    /**
     * The recursive helper method for add(E e).
     * @param e
     * @param node
     */
    private void add(E e, Node<E> node) {
        if (e.equals(node.data)) {
            node.data = e;
        }  else if (e.compareTo(node.data) < 0) {
            if (node.left == null) {
                node.left = new Node<>(e, node);
                size++;
                insertionCheck(node.left);
                return;
            } else {
                add(e, node.left);
            }
        } else {
            if (node.right == null) {
                node.right = new Node<>(e, node);
                size++;
                insertionCheck(node.right);
                return;
            } else {
                add(e, node.right);
            }
        }
    }

    /**
     * Check that the AVL property has not been broken after node's insertion.
     * If it has, then perform the rotation needed to restore it.
     *
     * Works by traversting "up" through the parent pointers, keeping track of whether
     * each node is the left or right (L or R) child, so that when a node with an
     * invalid balance factor (that is !isAVL()) is found, the correct rotations can
     * be performed to fix the tree.
     *
     * @param node
     */
    private void insertionCheck(Node<E> node) {
        Node<E> n = node;
        String path = "";
        while (true) {
            if (!isAVL(n)) {
                // rotate (and be done, if this is insertion)
                String rot = path.substring(0, 2);
                if (rot.equals("LL")) {
                    rotateRight(n);
                } else if (rot.equals("RR")) {
                    rotateLeft(n);
                } else if (rot.equals("LR")) {
                    rotateLeft(n.left);
                    rotateRight(n);
                } else if (rot.equals("RL")) {
                    rotateRight(n.right);
                    rotateLeft(n);
                } else {
                    // should never get here!
                    throw new IllegalStateException();
                }
                break; // if insertion, you're done after one fix
            } else if (n != root) {
                if (n == n.parent.left) {
                    path = "L" + path;
                } else {
                    path = "R" + path;
                }
                n = n.parent;
            } else { // n was the root
                // and so we're done
                break;
            }
        }
    }

    /**
     * Remove e from the tree, returning the value removed (or null if the
     * tree was unchanged.)
     *
     * @param e the value to remove
     */
    public E remove(E e) {
        Node<E> node = find(e);
        if (node == null) {
            return null;
        }

        E data = node.data;
        size--;

        // Case 1: Node has 0 or 1 child
        if (node.left == null || node.right == null) {
            Node<E> parent = node.parent;
            splice(node);
            if (parent != null) {
                deletionCheck(parent);
            }
        }
        // Case 2: Node has 2 children
        else {
            // Find in-order successor (leftmost node in right subtree)
            Node<E> successor = node.right;
            while (successor.left != null) {
                successor = successor.left;
            }

            // Copy successor's data to node
            node.data = successor.data;

            // Remove successor (which has at most 1 child)
            Node<E> successorParent = successor.parent;
            splice(successor);
            if (successorParent != null) {
                deletionCheck(successorParent);
            }
        }

        return data;
    }

    /**
     * Handles deletion in the case where a given node has zero 
     * or one children.
     * @param n the node to delete.
     */
    private void splice(Node<E> n) {
        if (n.left != null && n.right != null) {
            throw new IllegalArgumentException();
        }

        Node<E> subNode, newParent;
        if (n.left != null) { // if the left subnode is not not null, that's the one we're going
                              // to "slide up" or splice into place
            subNode = n.left;
        } else { // otherwise it's the right subnode we're going to slide up
            subNode = n.right;
        }

        // if both were null, then subNode is now also null

        if (n == root) { // special case, n is the root
            root = subNode;
            newParent = null;
        } else {
            newParent = n.parent;
            // now do the slide up
            if (newParent.left == n) { // if the node we're deleting is the left
                                       // child of its parent
                newParent.left = subNode;
            } else { // otherwise it's the right child
                newParent.right = subNode;
            }
        }

        // finally, we fix the parent pointer of subNode
        if (subNode != null) {
            subNode.parent = newParent;
        }
    }


    /**
     * Check that the AVL property has not been broken after node's deletion.
     * If it has, then perform the rotation needed to restore it.
     *
     * Unlike insertionCheck, this method continues checking up to the root
     * because deletion can cause multiple imbalances along the path. Here,
     * we demonstrate the alternate method of determining imbalance, by checking
     * the left and right subtrees of a node with an imbalance, rather than tracking
     * the path.
     *
     * @param node
     */
    private void deletionCheck(Node<E> node) {
        Node<E> n = node;
        while (n != null) {
            if (!isAVL(n)) {
                // Store the parent before rotation (rotation will change n's parent)
                Node<E> parent = n.parent;

                // Determine which case we're in based on heights, not path
                int leftHeight = height(n.left);
                int rightHeight = height(n.right);

                if (leftHeight > rightHeight) {
                    // Left subtree is taller - LL or LR case
                    int leftLeftHeight = height(n.left.left);
                    int leftRightHeight = height(n.left.right);

                    if (leftLeftHeight >= leftRightHeight) {
                        // LL case
                        rotateRight(n);
                    } else {
                        // LR case
                        rotateLeft(n.left);
                        rotateRight(n);
                    }
                } else {
                    // Right subtree is taller - RR or RL case
                    int rightLeftHeight = height(n.right.left);
                    int rightRightHeight = height(n.right.right);

                    if (rightRightHeight >= rightLeftHeight) {
                        // RR case
                        rotateLeft(n);
                    } else {
                        // RL case
                        rotateRight(n.right);
                        rotateLeft(n);
                    }
                }

                // After rotation, continue checking from the parent
                n = parent;
            } else {
                // Node is balanced, move up to parent
                n = n.parent;
            }
        }
    }

    /**
     * Perform a right rotation around n.
     * @param n
     */
    private void rotateRight(Node<E> n) {
        Node<E> A, B, T1, T2, T3, p; // p is B's parent ; note we never use T1 or T3!

        B = n;
        p = B.parent;
        A = B.left;
        T1 = A.left;
        T2 = A.right;
        T3 = B.right;

        // making A the root of the subtree
        if (B == root) { // special case: B was the root of the *whole* tree
            root = A;
            A.parent = null;
        } else { // otherwise, B was just a node in the tree, not its root
            if (p.left == B) {
                p.left = A;
            } else {
                p.right = A;
            }
            A.parent = p;
        }

        // now let's make B into A's right subchild
        A.right = B;
        B.parent = A;

        // finally, let's move T2 to B's new left subchild
        B.left = T2;
        if (T2 != null) {
            T2.parent = B;
        }
    }

    /**
     * Perform a left rotation around n.
     * @param n
     */
    private void rotateLeft(Node<E> n) {
        Node<E> B, C, T2, p;

        B = n;
        C = B.right;
        T2 = C.left;
        p = B.parent;

        if (B == root) {
            root = C;
            C.parent = null;
        } else {
            if (p.left == B) {
                p.left = C;
            } else {
                p.right = C;
            }
            C.parent = p;
        }

        C.left = B;
        B.parent = C;

        B.right = T2;
        if (T2 != null) {
            T2.parent = B;
        }
    }

    @Override
    public Iterator<E> iterator() {
        List<E> values = new ArrayList<>();
        inOrder(root, values);
        return values.iterator();
    }

    private void inOrder(Node<E> node, List<E> result) {
        if (node == null) return;
        inOrder(node.left, result);
        result.add(node.data);
        inOrder(node.right, result);
    }
}