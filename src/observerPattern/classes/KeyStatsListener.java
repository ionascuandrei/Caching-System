package observerPattern.classes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import observerPattern.interfaces.CacheListener;

/**
 * The KeyStatsListener collects key-level stats for cache operations.
 *
 * @param <K>
 * @param <V>
 */
public class KeyStatsListener<K, V> implements CacheListener<K, V> {

    private HashMap<K, Integer> hits = new HashMap<>();
    private HashMap<K, Integer> misses = new HashMap<>();
    private HashMap<K, Integer> pushes = new HashMap<>();


    /**
     * Get the number of hits for a key.
     *
     * @param key the key
     * @return number of hits
     */
    public int getKeyHits(final K key) {
        return get(hits, key);
    }

    /**
     * Get the number of misses for a key.
     *
     * @param key the key
     * @return number of misses
     */
    public int getKeyMisses(final K key) {
        return get(misses, key);
    }

    /**
     * Get the number of updates for a key.
     *
     * @param key the key
     * @return number of updates
     */
    public int getKeyUpdates(final K key) {
        return get(pushes, key);
    }

    /**
     * Get the @top most hit keys.
     *
     * @param top number of top keys
     * @return the list of keys
     */
    public List<K> getTopHitKeys(final int top) {
        hits = sortByValues(hits);
        return listTop(hits, top);
    }

    /**
     * Get the @top most missed keys.
     *
     * @param top number of top keys
     * @return the list of keys
     */
    public List<K> getTopMissedKeys(final int top) {
        misses = sortByValues(misses);
        return listTop(misses, top);

    }

    /**
     * Get the @top most updated keys.
     *
     * @param top number of top keys
     * @return the list of keys
     */
    public List<K> getTopUpdatedKeys(final int top) {
        pushes = sortByValues(pushes);
        return listTop(pushes, top);
    }

    @Override
    public final void onHit(final K key) {
        find(hits, key);
    }

    @Override
    public final void onMiss(final K key) {
        find(misses, key);
    }

    @Override
    public final void onPut(final K key, final V value) {
        find(pushes, key);
    }

    /**
     * Functie ce imi va actualiza contorul pentru fisierul dorit, in cazul in care acesta exista,
     * sau il va adauga pentru urmarirea statisticii.
     *
     * @param map hit/miss/push
     * @param key Adresa fisierului dorit
     */
    private void find(final HashMap<K, Integer> map, final K key) {
        if (map.containsKey(key)) {
            map.put(key, map.get(key) + 1);
            return;
        }
        map.put(key, 1);
    }

    /**
     * Returneaza numarul de miss/put/hit pentru fisierul dorit.
     *
     * @param map miss/hit/push
     * @param key Fisierul dorit
     * @return numarul de miss/put/hit
     */
    private int get(final HashMap<K, Integer> map, final K key) {
        if (map.containsKey(key)) {
            return map.get(key);
        }
        return 0;
    }

    /**
     * Creerea listei cu cele mai accesate fisiere pe baza statisticii urmarite.
     *
     * @param map miss/push/hit
     * @param top Numarul maxim de elemente dorite.
     * @return Lista de fisiere.
     */
    private List<K> listTop(final HashMap<K, Integer> map, final int top) {
        ArrayList<K> list = new ArrayList<>();
        int i = 0;
        Iterator<Map.Entry<K, Integer>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<K, Integer> me = iterator.next();
            list.add(me.getKey());
            i++;
            if (i == top) {
                break;
            }
        }
        return list;
    }

    /**
     * Comparatorul ce imi ordoneaza fisierele in ordine descrescatoare in functie de contor.
     *
     * @param map hit/miss/put.
     * @return HashMap-ul ordonat
     */
    private HashMap<K, Integer> sortByValues(final HashMap<K, Integer> map) {
        List list = new LinkedList(map.entrySet());
        // Comparatorul
        Collections.sort(list, new Comparator() {
            public int compare(final Object o1, final Object o2) {
                return ((Comparable) ((Map.Entry) (o2)).getValue())
                        .compareTo(((Map.Entry) (o1)).getValue());
            }
        });

        HashMap sortedHashMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }
}
