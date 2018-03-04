package cachingSystem.classes;

import cachingSystem.interfaces.Cache;
import dataStructures.classes.Pair;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The FIFOCache class should be considered a blackbox. All you need is its API!
 */
public class FIFOCache<K, V> implements Cache<K, V> {

    private LinkedHashMap<K, V> cache;

    public FIFOCache() {
        cache = new LinkedHashMap<>();
    }

    @Override
    public final V get(final K key) {
        return cache.get(key);
    }

    @Override
    public final void put(final K key, final V value) {
        cache.put(key, value);
    }

    @Override
    public final int size() {
        return cache.size();
    }

    @Override
    public final boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public final V remove(final K key) {
        return cache.remove(key);
    }

    @Override
    public final void clearAll() {
        cache.clear();
    }

    @Override
    public final Pair<K, V> getEldestEntry() {
        if (isEmpty()) {
            return null;
        }

        Map.Entry<K, V> eldest = cache.entrySet().iterator().next();

        return new Pair<K, V>(eldest.getKey(), eldest.getValue());
    }
}
