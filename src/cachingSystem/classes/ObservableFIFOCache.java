package cachingSystem.classes;

import dataStructures.classes.Pair;

/**
 * Class that adapts the FIFOCache class to the ObservableCache abstract class.
 */
public class ObservableFIFOCache<K, V> extends ObservableCache<K, V> {
    private FIFOCache<K, V> fifoCache;

    public ObservableFIFOCache() {
        fifoCache = new FIFOCache<>();
    }

    @Override
    public final V get(final K key) {
        if (fifoCache.get(key) != null) {
            this.cacheListener.onHit(key);
            return fifoCache.get(key);
        } else {
            this.cacheListener.onMiss(key);
            return null;
        }
    }

    @Override
    public final void put(final K key, final V value) {
        fifoCache.put(key, value);
        this.cacheListener.onPut(key, value);
        this.clearStaleEntries();
    }

    @Override
    public final int size() {
        return fifoCache.size();
    }

    @Override
    public final boolean isEmpty() {
        return fifoCache.isEmpty();
    }

    @Override
    public final V remove(final K key) {
        return fifoCache.remove(key);
    }

    @Override
    public final void clearAll() {
        fifoCache.clearAll();
    }

    @Override
    public final Pair<K, V> getEldestEntry() {
        return fifoCache.getEldestEntry();
    }
}
