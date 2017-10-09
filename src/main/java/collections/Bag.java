package collections;

import java.util.Iterator;

/**
 * Created by Amanda Curyło on 09.10.2017.
 */
public class Bag<T> implements GraphCollection, Iterable<T> {
    private Node first;
    private int N;

    private class Node {
        Node next;
        T item;
    }

    public void add(T item) {
        Node oldFirst = first;
        first = new Node();
        first.next = oldFirst;
        first.item = item;
        N++;
    }

    @Override
    public boolean isEmpty() {
        return N == 0;
    }

    @Override
    public int size() {
        return N;
    }

    @Override
    public Iterator<T> iterator() {
        return new BagIterator();
    }

    private class BagIterator implements Iterator<T> {
        private Node current = first;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public T next() {
            T item = current.item;
            current = current.next;
            return item;
        }
    }
}
