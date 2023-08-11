import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    int size = 0;
    BSTNode rootNode;

    @Override
    public void put(K k, V v) {
        if (rootNode != null) {
            BSTNode lookup = rootNode.getLast(k);
            // lookup can never be null
            int compareResult = k.compareTo(lookup.key);
            if (compareResult == 0) {
                lookup.value = v;
            } else if (compareResult < 0) {
                lookup.leftChild = new BSTNode(k, v, null, null);
                size += 1;
            } else if (compareResult > 0) {
                lookup.rightChild = new BSTNode(k, v, null, null);
                size += 1;

            }
        } else {
            rootNode = new BSTNode(k, v, null, null);
            size += 1;

        }
    }

    @Override
    public V get(K key) {
        if (rootNode == null) {
            return null;
        }
        BSTNode findNode = rootNode.get(key);
        if (findNode == null) {
            return null;
        } else {
            return findNode.value;
        }
    }

    @Override
    public boolean containsKey(K key) {
        if (rootNode == null) {
            return false;
        }
        return rootNode.get(key) != null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        size = 0;
        rootNode = null;
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key) {
      throw new UnsupportedOperationException();

    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }

    private class BSTNode {
        K key;
        V value;
        BSTNode leftChild;
        BSTNode rightChild;

        BSTNode(K k, V v, BSTNode l, BSTNode r) {
            key = k;
            value = v;
            leftChild = l;
            rightChild = r;
        }

        BSTNode get(K k) {
            if (k == null) {
                throw new NullPointerException("Search key can not be null");
            }

            int comp = k.compareTo(key);

            if (comp < 0 && leftChild != null) {
                return leftChild.get(k);
            } else if (comp == 0) {
                return this;
            } else if (comp > 0 && rightChild != null) {
                return rightChild.get(k);
            } else {
                return null;
            }
        }

        /**
         * Though it's called get "Leaf", it only returns leaf when k does not exist.
         *
         * @param k
         * @return If k exists, return the exact node. If k does not exist, return the leaf node.
         */
        BSTNode getLast(K k) {
            if (k == null) {
                throw new NullPointerException("Search key can not be null");
            }

            int compareResult = k.compareTo(key);

            if (compareResult < 0 && leftChild != null) {
                return leftChild.getLast(k);
            } else if (compareResult == 0) {
                return this;
            } else if (compareResult > 0 && rightChild != null) {
                return rightChild.getLast(k);
            } else {
                return this;
            }
        }

        boolean hasLeftChild() {
            return leftChild != null;
        }

        boolean hasRightChild() {
            return rightChild != null;
        }
    }
}
