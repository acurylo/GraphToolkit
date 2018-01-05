package algorithms;

import collections.IndexMinPQ;
import edges.UndirectedEdge;
import graphs.UndirectedGraph;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by Amanda Curyło on 04.01.2018.
 */
public class Prim implements MST{
    private UndirectedEdge[] edgeTo;
    private boolean marked[];
    private IndexMinPQ<Double> pq;
    private double[] distTo;

    public Prim(UndirectedGraph G) {
        if(!G.isWeightedGraph()) throw new IllegalArgumentException("Graph is unweighted.");
        if (!G.executeConnectedComponents().isConnected()) throw new IllegalArgumentException("Graph is not connected.");
        edgeTo = new UndirectedEdge[G.V()];
        marked = new boolean[G.V()];
        pq = new IndexMinPQ<>(G.V());
        distTo = new double[G.V()];
        for (int v = 0; v < G.V(); v++) {
            distTo[v] = Double.POSITIVE_INFINITY;
        }
        distTo[0] = 0.0;
        pq.insert(0, 0.0);
        while (!pq.isEmpty()) {
            visit(G, pq.delMin());
        }
    }

    private void visit(UndirectedGraph G, int v) {
        marked[v] = true;
        for (UndirectedEdge e : G.adj(v)) {
            int w = e.other(v);
            if (marked[w]) continue;
            if (e.weight("weight") < distTo[w]) {
                edgeTo[w] = e;
                distTo[w] = e.weight("weight");
                if (pq.contains(w)) pq.change(w, distTo[w]);
                else pq.insert(w, distTo[w]);
            }
        }
    }

    @Override
    public Iterable<UndirectedEdge> edges() {
        LinkedList<UndirectedEdge> mst = new LinkedList<>();
        mst.addAll(Arrays.asList(edgeTo).subList(1, edgeTo.length));
        return mst;
    }
}
