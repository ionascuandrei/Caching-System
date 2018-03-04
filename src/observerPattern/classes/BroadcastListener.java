package observerPattern.classes;

import observerPattern.interfaces.CacheListener;

import java.util.ArrayList;

/**
 * The BroadcastListener broadcasts cache events to other listeners that have been added to it.
 */
public class BroadcastListener<K, V> implements CacheListener<K, V> {
    private ArrayList<CacheListener<K, V>> list = new ArrayList<>();

    /**
     * Add a listener to the broadcast list.
     *
     * @param listener the listener
     */
    public final void addListener(final CacheListener<K, V> listener) {
        list.add(listener);
    }

    /**
     * Va itera prin toti cei abonati si le a trimite evenimentul onHit.
     *
     * @param key Adresa fisierului.
     */
    @Override
    public final void onHit(final K key) {
        for (CacheListener<K, V> listen : list) {
            listen.onHit(key);
        }
    }

    /**
     * Va itera prin toti cei abonati si le a trimite evenimentul onMiss.
     *
     * @param key Adresa fisierului.
     */
    @Override
    public final void onMiss(final K key) {
        for (CacheListener<K, V> listen : list) {
            listen.onMiss(key);
        }
    }

    /**
     * Va itera prin toti cei abonati si le a trimite evenimentul onPut.
     *
     * @param key Adresa fisierului.
     */
    @Override
    public final void onPut(final K key, final V value) {
        for (CacheListener<K, V> listen : list) {
            listen.onPut(key, value);
        }
    }
}
