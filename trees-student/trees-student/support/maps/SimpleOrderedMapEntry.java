/*
 * Copyright 2025 Marc Liberatore.
 */
package maps;

/**
 * An entry representing a key-value pair, to be used in an implementation of
 * SimpleOrderedMap.
 * 
 * The SimpleOrderedMapEntry associates a given key and value as a pair.
 * SimpleOrderedMapEntries are considered equal on the basis of their keys 
 * -- their value is ignored.
 * 
 * SimpleOrderedMapEntry have a natural ordering imposed by the natural
 * order of their key.
 */
public class SimpleOrderedMapEntry<K extends Comparable<K>, V> implements Comparable<SimpleOrderedMapEntry<K, V>> {
    final K k;
    final V v;

    @Override
    public String toString() {
        return k + "=" + v;
    }

    public SimpleOrderedMapEntry(K k, V v) {
        this.k = k;
        this.v = v;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((k == null) ? 0 : k.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SimpleOrderedMapEntry other = (SimpleOrderedMapEntry) obj;
        if (k == null) {
            if (other.k != null)
                return false;
        } else if (!k.equals(other.k))
            return false;
        return true;
    }

    @Override
    public int compareTo(SimpleOrderedMapEntry<K, V> o) {
        return k.compareTo(o.k);
    }
}
