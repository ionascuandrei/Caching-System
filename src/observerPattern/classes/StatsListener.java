package observerPattern.classes;

import observerPattern.interfaces.CacheListener;

/**
 * The StatsListener collects hit / miss / update stats for a cache.
 *
 * @param <K>
 * @param <V>
 */
public class StatsListener<K, V> implements CacheListener<K, V> {
    private int hits = 0;
    private int misses = 0;
    private int pushes = 0;

    /**
     * Get the number of hits for the cache.
     *
     * @return number of hits
     */
    public int getHits() {
        return hits;
    }

    /**
     * Get the number of misses for the cache.
     *
     * @return number of misses
     */
    public int getMisses() {
        return misses;
    }

    /**
     * Get the number of updates (put operations) for the cache.
     *
     * @return number of updates
     */
    public int getUpdates() {
        return pushes;
    }

    @Override
    public final void onHit(final K key) {
        hits++;
    }

    @Override
    public final void onMiss(final K key) {
        misses++;
    }

    @Override
    public final void onPut(final K key, final V value) {
        pushes++;
    }
}
