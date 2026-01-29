/*
 * Copyright 2025 Marc Liberatore.
 */

package trees;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;


 public class AVLTreeTest {
 
    //  @Rule
    //  public Timeout globalTimeout = Timeout.seconds(10); // 10 seconds

    @Before
    public void setup() {
    }

    @Test
    public void testLLSimple() throws Exception {
        Node<Integer> A = new Node<>(5);
        Node<Integer> B = new Node<>(10, A);
        Node<Integer> T1 = new Node<>(0, A);
        A.left = T1;
        A.right = B;
        assertTrue(TreeUtilities.isAVLTree(A));

        AVLTree<Integer> t = new AVLTree<>();
        t.add(10);
        assertTrue(TreeUtilities.isAVLTree(t.root));
        t.add(5);
        assertTrue(TreeUtilities.isAVLTree(t.root));
        t.add(0);
        assertTrue(TreeUtilities.isAVLTree(t.root));

        assertTrue(TreeUtilities.equalSubtrees(A, t.root));
    }

    @Test
    public void testRRSimple() throws Exception {
        Node<Integer> B = new Node<>(10);
        Node<Integer> A = new Node<>(5, B);
        Node<Integer> T3 = new Node<>(15, B);
        B.left = A;
        B.right = T3;
        assertTrue(TreeUtilities.isAVLTree(B));

        AVLTree<Integer> t = new AVLTree<>();
        t.add(5);
        assertTrue(TreeUtilities.isAVLTree(t.root));
        t.add(10);
        assertTrue(TreeUtilities.isAVLTree(t.root));
        t.add(15);
        assertTrue(TreeUtilities.isAVLTree(t.root));

        assertTrue(TreeUtilities.equalSubtrees(B, t.root));
    }

    @Test
    public void testLRSimple() throws Exception {
        Node<Integer> B = new Node<>(15);
        Node<Integer> A = new Node<>(10, B);
        Node<Integer> C = new Node<>(20, B);
        Node<Integer> T3 = new Node<>(18, C);
        B.left = A;
        B.right = C;
        C.left = T3;
        assertTrue(TreeUtilities.isAVLTree(B));

        AVLTree<Integer> t = new AVLTree<>();
        t.add(20);
        assertTrue(TreeUtilities.isAVLTree(t.root));
        t.add(10);
        assertTrue(TreeUtilities.isAVLTree(t.root));
        t.add(15);
        assertTrue(TreeUtilities.isAVLTree(t.root));
        t.add(18);
        assertTrue(TreeUtilities.isAVLTree(t.root));

        assertTrue(TreeUtilities.equalSubtrees(B, t.root));
    }

    @Test
    public void testRLSimple() throws Exception {
        Node<Integer> B = new Node<>(15);
        Node<Integer> A = new Node<>(10, B);
        Node<Integer> C = new Node<>(20, B);
        Node<Integer> T3 = new Node<>(18, C);
        B.left = A;
        B.right = C;
        C.left = T3;
        assertTrue(TreeUtilities.isAVLTree(B));

        AVLTree<Integer> t = new AVLTree<>();
        t.add(10);
        assertTrue(TreeUtilities.isAVLTree(t.root));
        t.add(20);
        assertTrue(TreeUtilities.isAVLTree(t.root));
        t.add(15);
        assertTrue(TreeUtilities.isAVLTree(t.root));
        t.add(18);
        assertTrue(TreeUtilities.isAVLTree(t.root));

        assertTrue(TreeUtilities.equalSubtrees(B, t.root));
    }

    // @Test
    //public void testAVLWarning() throws Exception {        
       // fail("This test is intended to fail. Read the comment and then comment out this test.");
        // We are not providing you with exhaustive tests for your AVL tree implementation,
        // just the simple ones above.
        // The next test (testAVL100Randomized) creates 100 random trees and checks them
        // using isAVLTree. On your computer, this might erroneously pass if your isAVLTree()
        // implementation is flawed! On Gradescope, we will use our own implementation of
        // isAVLTree() when running testAVL100Randomized().
        // So, if testAVL100Randomized passes on your computer but not Gradescope, 
        // *you need to figure out which of your rotations is broken* -- start by writing
        // more robust test cases where the various subtrees (T1, etc.) are nodes with
        // parent pointers, as the simple tests above don't check those cases.
    //} 

    @Test
    public void testRemoveLeaf() throws Exception {
        AVLTree<Integer> tree = new AVLTree<>();
        tree.add(10);
        tree.add(5);
        tree.add(15);

        assertEquals(3, tree.size());
        assertEquals(Integer.valueOf(5), tree.remove(5));
        assertEquals(2, tree.size());
        assertFalse(tree.contains(5));
        assertTrue(TreeUtilities.isAVLTree(tree.root));
    }

    @Test
    public void testRemoveNodeWithOneChild() throws Exception {
        AVLTree<Integer> tree = new AVLTree<>();
        tree.add(10);
        tree.add(5);
        tree.add(15);
        tree.add(12);

        assertEquals(4, tree.size());
        assertEquals(Integer.valueOf(15), tree.remove(15));
        assertEquals(3, tree.size());
        assertFalse(tree.contains(15));
        assertTrue(tree.contains(12));
        assertTrue(TreeUtilities.isAVLTree(tree.root));
    }

    @Test
    public void testRemoveNodeWithTwoChildren() throws Exception {
        AVLTree<Integer> tree = new AVLTree<>();
        tree.add(10);
        tree.add(5);
        tree.add(15);
        tree.add(12);
        tree.add(20);

        assertEquals(5, tree.size());
        assertEquals(Integer.valueOf(15), tree.remove(15));
        assertEquals(4, tree.size());
        assertFalse(tree.contains(15));
        assertTrue(tree.contains(12));
        assertTrue(tree.contains(20));
        assertTrue(TreeUtilities.isAVLTree(tree.root));
    }

    @Test
    public void testRemoveRoot() throws Exception {
        AVLTree<Integer> tree = new AVLTree<>();
        tree.add(10);
        tree.add(5);
        tree.add(15);

        assertEquals(3, tree.size());
        assertEquals(Integer.valueOf(10), tree.remove(10));
        assertEquals(2, tree.size());
        assertFalse(tree.contains(10));
        assertTrue(TreeUtilities.isAVLTree(tree.root));
    }

    @Test
    public void testRemoveNonExistent() throws Exception {
        AVLTree<Integer> tree = new AVLTree<>();
        tree.add(10);
        tree.add(5);
        tree.add(15);

        assertEquals(3, tree.size());
        assertNull(tree.remove(100));
        assertEquals(3, tree.size());
        assertTrue(TreeUtilities.isAVLTree(tree.root));
    }

    @Test
    public void testRemoveMultipleWithRebalancing() throws Exception {
        AVLTree<Integer> tree = new AVLTree<>();
        // Build a larger tree
        for (int i = 1; i <= 15; i++) {
            tree.add(i);
            assertTrue(TreeUtilities.isAVLTree(tree.root));
        }

        // Remove elements one by one and verify AVL property
        for (int i = 1; i <= 15; i++) {
            assertEquals(Integer.valueOf(i), tree.remove(i));
            assertFalse(tree.contains(i));
            assertTrue(TreeUtilities.isAVLTree(tree.root));
        }

        assertEquals(0, tree.size());
    }

    @Test
    public void testRemoveOtherOrder() throws Exception {
        AVLTree<Integer> tree = new AVLTree<>();
        int[] values = {50, 25, 75, 10, 30, 60, 80, 5, 15, 27, 55, 65, 77, 85};

        for (int val : values) {
            tree.add(val);
            assertTrue(TreeUtilities.isAVLTree(tree.root));
        }

        // Remove in different order
        int[] removeOrder = {25, 75, 50, 10, 80, 60, 30, 5, 15, 27, 55, 65, 77, 85};
        for (int val : removeOrder) {
            assertEquals(Integer.valueOf(val), tree.remove(val));
            assertFalse(tree.contains(val));
            assertTrue(TreeUtilities.isAVLTree(tree.root));
        }

        assertEquals(0, tree.size());
    }

    @Test
    public void testAVL100Randomized() throws Exception {
        for (int seed = 0; seed < 100; seed++) {
            // the seed controls the rest of the random choices in this method
            Random r = new Random(seed);

            int size = 1 + r.nextInt((int) Math.pow(2, 9));

            List<Integer> l = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                l.add(i);
            }
            Collections.shuffle(l, r);
            AVLTree<Integer> a = new AVLTree<>();
            for (Integer i : l) {
                a.add(i);
                assertTrue(TreeUtilities.isAVLTree(a.root));
            }
        }
    }

    @Test
    public void testIteratorEmpty() throws Exception {
        AVLTree<Integer> tree = new AVLTree<>();
        for (Integer i : tree) {
            fail("Iteration on a empty tree should never enter the loop.");
        }
    }

    @Test
    public void testIteratorSimple() {
        AVLTree<Integer> tree = new AVLTree<>();
        tree.add(10);
        tree.add(5);
        tree.add(15);

        List<Integer> l = new ArrayList<>();
        for (Integer i: tree) {
            l.add(i);
        }
        l.sort(null);

        assertEquals(Arrays.asList(5, 10, 15), l);
    }

    @Test
    public void testIteratorComplex() {
        AVLTree<Integer> tree = new AVLTree<>();
        int[] values = {50, 25, 75, 10, 30, 60, 80, 5, 15, 27, 55, 65, 77, 85};
        for (int val : values) {
            tree.add(val);
        }
        assertTrue(TreeUtilities.isAVLTree(tree.root));

        List<Integer> l = new ArrayList<>();
        for (Integer i: tree) {
            l.add(i);
        }
        Collections.sort(l);

        int[] toRemove = {25, 75, 50, 10, 80};

        for (int val : toRemove) {
            l.remove(Integer.valueOf(val));
            assertEquals(Integer.valueOf(val), tree.remove(val));
        }

        List<Integer> expected = Arrays.asList(5, 15, 27, 30, 55, 60, 65, 77, 85);
        List<Integer> actual = new ArrayList<>();
        for (Integer i: tree) {
            actual.add(i);
        }
        actual.sort(null);
        assertEquals(expected, actual);
    }

    @Test
    public void testAVLAll() throws Exception {
        testLLSimple();
        testRRSimple();
        testLRSimple();
        testRLSimple();
        testRemoveLeaf();
        testRemoveNodeWithOneChild();
        testRemoveNodeWithTwoChildren();
        testRemoveRoot();
        testRemoveNonExistent();
        testRemoveMultipleWithRebalancing();
        testRemoveOtherOrder();
        testAVL100Randomized();
        testIteratorEmpty();
        testIteratorSimple();
        testIteratorComplex();
    }
 }