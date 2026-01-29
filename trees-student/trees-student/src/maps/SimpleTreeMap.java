package maps;

import java.util.Set;
import java.util.HashSet;
import trees.AVLTree;
import trees.Node;

public class SimpleTreeMap<K extends Comparable<K>, V> implements SimpleOrderedMap<K, V> {

    private final AVLTree<SimpleOrderedMapEntry<K, V>> tree;

    public SimpleTreeMap() {
        tree = new AVLTree<>();
    }

    @Override
    public int size() {
        return tree.size();
    }

    @Override
    public void put(K k, V v) {
        tree.add(new SimpleOrderedMapEntry<>(k, v));
    }

    @Override
    public V get(K k) {
        SimpleOrderedMapEntry<K, V> entry = tree.get(new SimpleOrderedMapEntry<>(k, null));
        return entry == null ? null : entry.v;
    }

    @Override
    public V getOrDefault(K k, V defaultValue) {
        V value = get(k);
        return (value == null) ? defaultValue : value;
    }

    @Override
    public V remove(K k) {
        SimpleOrderedMapEntry<K, V> removed = tree.remove(new SimpleOrderedMapEntry<>(k, null));
        return (removed == null) ? null : removed.v;
    }

    @Override
    public Set<K> keys() {
        Set<K> keySet = new HashSet<>();
        for (SimpleOrderedMapEntry<K, V> entry : tree) {
            keySet.add(entry.k);
        }
        return keySet;
    }    
    
}