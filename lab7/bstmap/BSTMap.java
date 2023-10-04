package bstmap;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    private Node root; // The root of BST
    private int size = 0;

    private class Node {
        private final K key;
        private V value;
        private Node left;
        private Node right;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        return containsKey(root, key);
    }

    private boolean containsKey(Node node, K key) {
        if (node == null) {
            return false;
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            return containsKey(node.left, key);
        } else if (cmp > 0) {
            return containsKey(node.right, key);
        } else {
            return true;
        }
    }


    @Override
    public V get(K key) {
        return get(root, key);
    }

    private V get(Node node, K key) {
        if (node == null) {
            return null;
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            return get(node.left, key);
        } else if (cmp > 0) {
            return get(node.right, key);
        } else {
            return node.value;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        root = put(root, key, value);
        size += 1;
    }

    private Node put(Node node, K key, V value) {
        if (node == null) {
            return new Node(key, value);
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = put(node.left, key, value);
        } else if (cmp > 0) {
            node.right = put(node.right, key, value);
        } else {
            node.value = value;
        }
        return node;
    }

    @Override
    public Set<K> keySet() {
        HashSet<K> keys = new HashSet<>();
        addKey(root, keys);
        return keys;
    }

    private void addKey(Node node, Set<K> set) {
        if (node == null) {
            return;
        }
        set.add(node.key);
        addKey(node.left, set);
        addKey(node.right, set);
    }

    @Override
    public V remove(K key) {
        if (containsKey(key)) {
            V removedVal = get(key);
            root = remove(root, key);
            size -= 1;
            return removedVal;
        }
        return null;
    }

    private Node remove(Node node, K key) {
        if (node == null) {
            return null;
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = remove(node.left, key);
        } else if (cmp > 0) {
            node.right = remove(node.right, key);
        } else { // find the key
            // case 1: the deleted node has no right child
            if (node.right == null) {
                return node.left;
            }
            // case 2: the deleted node has no left child
            if (node.left == null) {
                return node.right;
            }
            // case 3: the deleted node has two children
            Node sourceNode = node;
            node = findMaxChild(node.left);
            node.right = sourceNode.right;
            node.left = remove(sourceNode.left, node.key);
        }
        return node;
    }

    private Node findMaxChild(Node node) {
        if (node.right != null) {
            return findMaxChild(node.right);
        } else {
            return node;
        }
    }

    @Override
    public V remove(K key, V value) {
        if (containsKey(key)) {
            V removedVal = get(key);
            if (removedVal.equals(value)) {
                root = remove(root, key);
                size -= 1;
                return removedVal;
            }
        }
        return null;
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }

    public void printInOrder() {
        printInOrder(root);
    }

    private void printInOrder(Node node) {
        if (node == null) {
            return;
        }
        printInOrder(node.left);
        System.out.println(node.key.toString() + "->" + node.value.toString());
        printInOrder(node.right);
    }

//    public static void main(String[] args) {
//    }
}
