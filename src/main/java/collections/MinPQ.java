package collections;

/**
 * Created by Amanda Curyło on 04.01.2018.
 */
public class MinPQ<T extends Comparable<T>> implements GraphCollection {
    private T[] pq;
    private int N = 0;

    @SuppressWarnings("unchecked")
    public MinPQ(int maxN) {
        pq = (T[]) new Comparable[maxN+1];
    }

    @SuppressWarnings("unchecked")
    public MinPQ(Iterable<T> items, int maxN) {
        pq = (T[]) new Comparable[maxN+1];
        for (T item : items)
            insert(item);
    }

    @Override
    public boolean isEmpty() {
        return N == 0;
    }

    @Override
    public int size() {
        return N;
    }

    public void insert(T item) {
        pq[++N] = item;
        swim(N);
    }

    public T delMin() {
        if(N == 0) throw new NullPointerException("Queue is empty.");
        T min = pq[1];
        exch(1, N--);
        pq[N+1] = null;
        sink(1);
        return min;
    }

    private boolean less(int i, int j) {
        return pq[i].compareTo(pq[j]) > 0;
    }

    private void exch(int i, int j) {
        T item = pq[i];
        pq[i] = pq[j];
        pq[j] = item;
    }

    private void swim(int k) {
        while (k > 1 && less(k/2, k)) {
            exch(k/2, k);
            k = k/2;
        }
    }

    private void sink(int k) {
        while (2*k <= N) {
            int j = 2*k;
            if (j <N && less(j, j+1)) j++;
            if (!less(k,j)) break;
            exch(k,j);
            k = j;
        }
    }

}
