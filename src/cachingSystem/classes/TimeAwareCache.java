package cachingSystem.classes;

import cachingSystem.interfaces.CacheStalePolicy;
import dataStructures.classes.Pair;

import java.sql.Timestamp;
import java.util.HashMap;

/**
 * The TimeAwareCache offers the same functionality as the LRUCache, but also stores a timestamp for
 * each element. The timestamp is updated after each get / put operation for a key. This
 * functionality allows for time based cache stale policies (e.g. removing entries that are older
 * than 1 second).
 */
public class TimeAwareCache<K, V> extends LRUCache<K, V> {
    private HashMap<K, Long> tlruCache = new HashMap<>();

    /**
     * Functie ce returneaza cotinutul fisierului dorit. Daca acest fisier exista in cache, i se va
     * actualiza timestamp-ul si se va apela functia get din LRUCache.
     *
     * @param key Adresa fisierului pe care dorim sa il cautam.
     * @return Continutul fisierului dorit.
     */
    @Override
    public V get(final K key) {
        if (!isEmpty()) {
            this.clearStaleEntries();
        }
        if (this.getLruCache().containsKey(key)) {
            long timestamp = System.currentTimeMillis();
            tlruCache.put(key, timestamp);
        }
        return super.get(key);
    }

    /**
     * Functie ce va adauga/suprascrie continutul unui fisier dorit si va actualiza timestamp-ul
     * acelui fisier in cazul in care este sau nu existent in cache.
     *
     * @param key   Adresa fisierului pe care dorim sa il adaugam.
     * @param value Continutul fisierului.
     */
    @Override
    public void put(final K key, final V value) {
        if (!tlruCache.containsKey(key)) {
            tlruCache.put(key, System.currentTimeMillis());
            super.put(key, value);
        } else {
            super.put(key, value);
            tlruCache.put(key, System.currentTimeMillis());
        }
    }

    /**
     * Eliminarea fisierului dorit.
     *
     * @param key Adresa fisierului ce trebuie eliminat (daca acesta se afla in cache)
     * @return Continutul fisierului ce a fost eliminat din cache.
     */
    @Override
    public V remove(final K key) {
        tlruCache.remove(key);
        return super.remove(key);
    }

    /**
     * Get the timestamp associated with a key, or null if the key is not stored in the cache.
     *
     * @param key the key
     * @return the timestamp, or null
     */
    public Timestamp getTimestampOfKey(final K key) {
        return new Timestamp(tlruCache.get(key));
    }

    /**
     * Set a cache stale policy that should remove all elements older than @millisToExpire
     * milliseconds. This is a convenience method for setting a time based policy for the cache.
     *
     * @param millisToExpire the expiration time, in milliseconds
     */
    public void setExpirePolicy(final long millisToExpire) {
        this.setStalePolicy(new CacheStalePolicy<K, V>() {
            @Override
            public boolean shouldRemoveEldestEntry(final Pair<K, V> entry) {
                long currentts = System.currentTimeMillis();
                if (isEmpty()) {
                    return false;
                } else {
                    return (currentts - getTimestampOfKey(entry.getKey()).getTime())
                            > millisToExpire;
                }
            }
        });
    }
}
