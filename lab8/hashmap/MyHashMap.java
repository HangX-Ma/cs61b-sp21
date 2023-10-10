package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {
    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    // You should probably define some more!
    /** Constructors */
    public MyHashMap() {
        this(DEFAULT_BUCKET_SIZE, DEFAULT_MAX_LOAD_FACTOR);
    }

    public MyHashMap(int initialSize) {
        this(initialSize, DEFAULT_MAX_LOAD_FACTOR);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        maxLoadFactor = maxLoad;
        buckets = createTable(initialSize);
    }

    private static final double DEFAULT_MAX_LOAD_FACTOR = 0.75;
    private static final int DEFAULT_BUCKET_SIZE = 16;

    private final double maxLoadFactor;
    private int size = 0;

    /* Instance Variables */
    private Collection<Node>[] buckets;

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        Collection<Node>[] bucketCollection = new Collection[tableSize];
        for (int i = 0; i < tableSize; i++) {
            bucketCollection[i] = createBucket();
        }
        return bucketCollection;
    }

    @Override
    public void clear() {
        buckets = createTable(DEFAULT_BUCKET_SIZE);
        size = 0;
    }

    /* We can assume the 'null' key will never be inserted. */
    @Override
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    @Override
    public V get(K key) {
        int index = getIndex(key, buckets);
        for (Node node : buckets[index]) {
            if (node.key.equals(key)) {
                return node.value;
            }
        }
        return null;
    }

    private Node getNode(K key) {
        int index = getIndex(key, buckets);
        for (Node node : buckets[index]) {
            if (node.key.equals(key)) {
                return node;
            }
        }
        return null;
    }


    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key, buckets);
        Node node = getNode(key);
        if (node != null) {
            node.value = value;
            return;
        }
        node = createNode(key, value);
        buckets[index].add(node);
        size += 1;
        if (checkLoadFactor()) {
            resize();
        }
    }

    private void resize() {
        int index;
        Node node;

        Collection<Node>[] newTable = createTable(buckets.length * 2);
        Iterator<Node> hashMapNodeIterator = new hashMapNodeIteractor();

        while (hashMapNodeIterator.hasNext()) {
            node = hashMapNodeIterator.next();
            index = getIndex(node.key, newTable);
            newTable[index].add(node);
        }
        buckets = newTable;
    }

    private boolean checkLoadFactor() {
        return (double) size / buckets.length >= maxLoadFactor;
    }

    private int getIndex(K key, Collection<Node>[] buckets) {
        return Math.floorMod(key.hashCode(), buckets.length);
    }

    @Override
    public Set<K> keySet() {
        HashSet<K> set = new HashSet<>();
        for (K key : this) {
            set.add(key);
        }
        return set;
    }

    @Override
    public V remove(K key) {
        Node node = getNode(key);
        if (node == null) {
            return null;
        }
        buckets[getIndex(key, buckets)].remove(node);
        size -= 1;
        return node.value;
    }

    @Override
    public V remove(K key, V value) {
        Node node = getNode(key);
        if (node == null || !node.value.equals(value)) {
            return null;
        }
        buckets[getIndex(key, buckets)].remove(node);
        size -= 1;
        return node.value;
    }

    @Override
    public Iterator<K> iterator() {
        return new hashMapKeyIterator();
    }

    private class hashMapKeyIterator implements Iterator<K> {
        private final Iterator<Node> nodeIterator = new hashMapNodeIteractor();

        @Override
        public boolean hasNext() {
            return nodeIterator.hasNext();
        }

        @Override
        public K next() {
            return nodeIterator.next().key;
        }
    }

    private class hashMapNodeIteractor implements Iterator<Node> {
        private final Iterator<Collection<Node>> bucketIterator = Arrays.stream(buckets).iterator();
        private Iterator<Node> collectionIterator;
        private int leftNodeSize = size;

        @Override
        public boolean hasNext() {
            return leftNodeSize > 0;
        }

        @Override
        public Node next() {
            if (collectionIterator == null || !collectionIterator.hasNext()) {
                while (bucketIterator.hasNext()) {
                    Collection<Node> collection = bucketIterator.next();
                    if (collection.isEmpty()) {
                        continue;
                    }
                    collectionIterator = collection.iterator();
                    break;
                }
            }
            leftNodeSize -= 1;
            return collectionIterator.next();
        }
    }

}
