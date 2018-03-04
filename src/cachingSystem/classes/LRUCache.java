package cachingSystem.classes;

import dataStructures.classes.Pair;

import java.util.HashMap;


/**
 * This cache is very similar to the FIFOCache, but guarantees O(1) complexity for the get, put and
 * remove operations.
 */
public class LRUCache<K, V> extends ObservableCache<K, V> {

    private HashMap<K, Node<K, V>> lruCache;
    private Node<K, V> leastRecentlyUsed;
    private Node<K, V> mostRecentlyUsed;

    /**
     * Getter pentru a oferi accesul la Cache-ul LRU atunci cand se foloseste TLRU.
     *
     * @return Instanta cache-ului LRU
     */
    public HashMap<K, Node<K, V>> getLruCache() {
        return lruCache;
    }

    public LRUCache() {
        leastRecentlyUsed = new Node<>(null, null, null, null);
        mostRecentlyUsed = leastRecentlyUsed;
        lruCache = new HashMap<>();
    }

    /**
     * Functie ce returneaza continutul fisierului dorit in cazul in care acesta exista si i se
     * va actualiza pozitia in cache, acesta devenind cel mai recent fisier accesat. In cazul
     * in care acest fisier nu exista in cache, se va anunta prin functia miss.
     *
     * @param key Adresa fisierului pe care dorim sa il cautam.
     * @return Continutul fisierului cautat, sau null in caz ca acesta nu exista in cache.
     */
    @Override
    public V get(final K key) {
        Node<K, V> tempNode = lruCache.get(key);
        if (tempNode != null) {
            //Daca fisierul este deja cel mai recent accesat va ramane pe pozitia curenta in cache.
            if (tempNode == mostRecentlyUsed) {
                this.cacheListener.onHit(key);
                return tempNode.getValue();
            }
            //Daca este cel mai vechi fisier accesat va deveni cel mai recent
            if (tempNode == leastRecentlyUsed) {
                tempNode.getNext().setPrev(null);
                leastRecentlyUsed = tempNode.getNext();
                tempNode.setNext(null);
                tempNode.setPrev(mostRecentlyUsed);
                mostRecentlyUsed.setNext(tempNode);
                mostRecentlyUsed = tempNode;

                this.clearStaleEntries();
                this.cacheListener.onHit(key);
                return tempNode.getValue();
            }
            //Daca fisierul se afla in cache intre leastRecent si mostReacent
            tempNode.getNext().setPrev(tempNode.getPrev());
            tempNode.getPrev().setNext(tempNode.getNext());
            tempNode.setNext(null);
            tempNode.setPrev(mostRecentlyUsed);
            mostRecentlyUsed.setNext(tempNode);
            mostRecentlyUsed = tempNode;

            this.cacheListener.onHit(key);
            return tempNode.getValue();
        }
        this.cacheListener.onMiss(key);
        return null;
    }

    /**
     * Functie ce va actualiza continutul fisierului in cazul in care acesta exista in cache, sau
     * il va adauga in cache. In ambele cazuri, fisierul va fi salvat ca cel mai recent fisier
     * accesat.
     *
     * @param key   Adresa fisierului pe care dorim sa il adaugam.
     * @param value Continutul fisierului.
     */
    @Override
    public void put(final K key, final V value) {
        if (lruCache.containsKey(key)) {
            Node<K, V> tempNode = lruCache.get(key);
            tempNode.setValue(value);
            //Daca fisierul este deja cel mai recent accesat va ramane pe pozitia curenta in cache.
            if (tempNode == mostRecentlyUsed) {
                this.clearStaleEntries();
                this.cacheListener.onPut(key, value);
                return;
            }
            //Daca este cel mai vechi fisier accesat va deveni cel mai recent
            if (tempNode == leastRecentlyUsed) {
                tempNode.getNext().setPrev(null);
                leastRecentlyUsed = tempNode.getNext();
                tempNode.setNext(null);
                tempNode.setPrev(mostRecentlyUsed);
                mostRecentlyUsed.setNext(tempNode);
                mostRecentlyUsed = tempNode;

                this.clearStaleEntries();
                this.cacheListener.onPut(key, value);
                return;
            }
            //Daca fisierul se afla in cache intre leastRecent si mostReacent
            tempNode.getNext().setPrev(tempNode.getPrev());
            tempNode.getPrev().setNext(tempNode.getNext());
            tempNode.setNext(null);
            tempNode.setPrev(mostRecentlyUsed);
            mostRecentlyUsed.setNext(tempNode);
            mostRecentlyUsed = tempNode;

            this.clearStaleEntries();
            this.cacheListener.onPut(key, value);
            return;
        }

        /*Daca fisierul nu se afla in cache, acesta va fi adaugat si va deveni cel mai recent
        fisier accesat din cache */
        Node<K, V> myNode = new Node<K, V>(mostRecentlyUsed, null, key, value);
        if (mostRecentlyUsed != null) {
            mostRecentlyUsed.setNext(myNode);
        }
        if (isEmpty()) {
            leastRecentlyUsed = myNode;
        }
        mostRecentlyUsed = myNode;

        lruCache.put(key, myNode);
        this.clearStaleEntries();
        this.cacheListener.onPut(key, value);
    }

    /**
     * Dimensiunea cache-ului.
     *
     * @return Dimensiunea cache-ului.
     */
    @Override
    public int size() {
        return lruCache.size();
    }

    /**
     * Verifica daca cache-ul este gol.
     *
     * @return True/False
     */
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Elimina fisierul cel mai vechi accesat din cache.
     *
     * @param key Adresa fisierului ce trebuie eliminat (daca acesta se afla in cache)
     * @return Continutul fisierului eliminat
     */
    @Override
    public V remove(final K key) {
        if (lruCache.containsKey(key)) {
            V myValue = lruCache.get(key).getValue();

            leastRecentlyUsed = leastRecentlyUsed.getNext();
            if (leastRecentlyUsed != null) {
                leastRecentlyUsed.setPrev(null);
            }
            lruCache.remove(key);
            return myValue;
        }
        return null;
    }

    /**
     * Functie ce goleste cache-ul.
     */
    @Override
    public void clearAll() {
        lruCache.clear();
        leastRecentlyUsed = new Node<>(null, null, null, null);
        mostRecentlyUsed = leastRecentlyUsed;
    }

    /**
     * Returneaza cel mai vechi fisier accesat din cache.
     *
     * @return Fisierul cu continutul acestuia.
     */
    @Override
    public Pair<K, V> getEldestEntry() {
        if (isEmpty()) {
            return null;
        } else {
            return new Pair<>(leastRecentlyUsed.getKey(), leastRecentlyUsed.getValue());
        }
    }
}
