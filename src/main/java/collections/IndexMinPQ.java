package collections;

/**
 * Created by Amanda Curyło on 04.01.2018.
 */
public class IndexMinPQ<T extends Comparable<T>> implements GraphCollection {
    private int[] pq;
    private int[] qp;
    private T[] keys;
    private int N = 0;

    @SuppressWarnings("unchecked")
    public IndexMinPQ(int maxN) {
        pq = new int[maxN+1];
        qp = new int[maxN+1];
        keys = (T[]) new Comparable[maxN+1];
        for(int i = 0; i <= maxN; i++)
            qp[i] = -1;
    }

    @Override
    public boolean isEmpty() {
        return N == 0;
    }

    @Override
    public int size() {
        return N;
    }

    public void insert(int k, T key) {
        N++;
        pq[N] = k;
        qp[k] = N;
        keys[k] = key;
        swim(N);
    }

    public int delMin() {
        if(N == 0) throw new NullPointerException("Queue is empty.");
        int indexOfMin = pq[1];
        exch(1, N--);
        sink(1);
        keys[pq[N+1]] = null;
        qp[pq[N+1]] = -1;
        return indexOfMin;
    }

    public int minIndex() {
        return pq[1]; }

    public boolean contains(int k) {
        return qp[k] != -1; }

    public void change(int k, T key) {
        keys[k] = key;
        swim(qp[k]);
        sink(qp[k]);
    }

    private void swim(int k) {
        while (k > 1 && less(k/2, k)) {
            exch(k/2, k);
            k = k/2;
        }
    }

    private void sink(int k) {
        while (2*k <= size()) {
            int j = 2*k;
            if (j < size() && less(j, j+1)) j++;
            if (!less(k,j)) break;
            exch(k,j);
            k = j;
        }
    }

    private boolean less(int i, int j) {
        return keys[pq[i]].compareTo(keys[pq[j]]) > 0;  }

    private void exch(int i, int j) {
        int t = qp[pq[j]];
        qp[pq[j]] =  qp[pq[i]];
        qp[pq[i]] = t;
        int k = pq[i];
        pq[i] = pq[j];
        pq[j] = k;
    }
}
