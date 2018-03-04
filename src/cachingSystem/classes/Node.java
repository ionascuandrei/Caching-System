package cachingSystem.classes;

public class Node<K, V> {
    private Node<K, V> prev;
    private Node<K, V> next;
    private K key;
    private V value;

    public Node(final Node<K, V> prev, final Node<K, V> next, final K key, final V value) {
        this.prev = prev;
        this.next = next;
        this.key = key;
        this.value = value;
    }

    public final Node<K, V> getPrev() {
        return prev;
    }

    public final void setPrev(final Node<K, V> prev) {
        this.prev = prev;
    }

    public final Node<K, V> getNext() {
        return next;
    }

    public final void setNext(final Node<K, V> next) {
        this.next = next;
    }

    public final K getKey() {
        return key;
    }

    public final void setKey(final K key) {
        this.key = key;
    }

    public final V getValue() {
        return value;
    }

    public final void setValue(final V value) {
        this.value = value;
    }
}
