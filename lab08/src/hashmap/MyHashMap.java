package hashmap;

import java.util.*;

/**
 * A hash table-backed Map implementation. Provides amortized constant time
 * access to elements via get(), remove(), and put() in the best case.
 * <p>
 * Assumes null keys will never be inserted, and does not resize down upon remove().
 *
 * @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    @Override
    public void put(K key, V value) {
        if (needResize()) {
            resize();
        }
        int hashVal = key.hashCode();
        int bucketIn = Math.floorMod(key.hashCode(), bucketNum);

        for (Node node : buckets[bucketIn]) {
            if (node.key.equals(key)) {
                node.value = value;
                return;
            }
        }

        buckets[bucketIn].add(createNode(key, value));
        itemNum++;

    }

    @Override
    public V get(K key) {
        int bucketIn = Math.floorMod(key.hashCode(), bucketNum);
        for (Node node : buckets[bucketIn]) {
            if (node.key.equals(key)) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        int bucketIn = Math.floorMod(key.hashCode(), bucketNum);
        for (Node node : buckets[bucketIn]) {
            if (node.key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return itemNum;
    }

    @Override
    public void clear() {
        buckets = createTable(initialCapacity);
        itemNum = 0;
        bucketNum = initialCapacity;
    }

    @Override
    public Set<K> keySet() {
        Set<K> set = new HashSet<>();
        for (int ix = 0; ix < bucketNum; ix++) {
            for (Node node : buckets[ix]) {
                set.add(node.key);
            }
        }
        return set;
    }

    @Override
    public V remove(K key) {
        int bucketIn = Math.floorMod(key.hashCode(), bucketNum);
        for (Node node : buckets[bucketIn]) {
            if (node.key.equals(key)) {
                Node temp = new Node(node.key, node.value);
                buckets[bucketIn].remove(temp);
                return node.value;
            }
        }
        return null;
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }

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

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }

            Node other = (Node) obj;
            if (other.key.equals(key)) {
                return true;
            }
            return false;

        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    // You should probably define some more!
    private double loadFactor;
    private int bucketNum;
    private int itemNum;
    private int initialCapacity;

    /**
     * Constructors
     */
    public MyHashMap() {
        this(16, 0.75);
    }

    public MyHashMap(int initialCapacity) {
        this(initialCapacity, 0.75);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialCapacity.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialCapacity initial size of backing array
     * @param loadFactor      maximum load factor
     */
    public MyHashMap(int initialCapacity, double loadFactor) {
        buckets = createTable(initialCapacity);
        this.loadFactor = loadFactor;
        this.bucketNum = initialCapacity;
        this.itemNum = 0;
        this.initialCapacity = initialCapacity;
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     * <p>
     * The only requirements of a hash table bucket are that we can:
     * 1. Insert items (`add` method)
     * 2. Remove items (`remove` method)
     * 3. Iterate through items (`iterator` method)
     * <p>
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     * <p>
     * Override this method to use different data structures as
     * the underlying bucket type
     * <p>
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new ArrayList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     * <p>
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        Collection<Node>[] bucketsLoc = new Collection[tableSize];
        for (int ix = 0; ix < tableSize; ix++) {
            bucketsLoc[ix] = createBucket();
        }
        return bucketsLoc;
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!
    private boolean needResize() {
        return (double) itemNum / bucketNum > loadFactor;
    }

    private void resize() {
//        System.out.println("Resizing");
        int newBucketNum = bucketNum * 2;
        Collection<Node>[] newBuckets = createTable(newBucketNum);
        for (int ix = 0; ix < bucketNum; ix++) {
            for (Node node : buckets[ix]) {
                K key = node.key;
                V value = node.value;
                int hashVal = key.hashCode();
                int bucketIn = Math.floorMod(key.hashCode(), newBucketNum);

                for (Node newNode : newBuckets[bucketIn]) {
                    if (newNode.key.equals(key)) {
                        newNode.value = value;
                        break;
                    }
                }

                newBuckets[bucketIn].add(createNode(key, value));
            }
        }
        bucketNum = newBucketNum;
        buckets = newBuckets;
    }
}
