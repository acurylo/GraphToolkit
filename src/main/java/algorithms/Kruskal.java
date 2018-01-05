package algorithms;

import collections.MinPQ;
import edges.UndirectedEdge;
import graphs.UndirectedGraph;
import graphs.UnionFind;

import java.util.LinkedList;

/**
 * Created by Amanda Curyło on 04.01.2018.
 */
public class Kruskal implements MST {
    private LinkedList<UndirectedEdge> mst;

    public Kruskal(UndirectedGraph G) {
        if(!G.isWeightedGraph()) throw new IllegalArgumentException("Graph is unweighted.");
        if (!G.executeConnectedComponents().isConnected()) throw new IllegalArgumentException("Graph is not connected.");
        mst = new LinkedList<>();
        MinPQ<UndirectedEdge> pq = new MinPQ<>(G.edges(), G.E());
        UnionFind uf = new UnionFind(G.V());
        while (!pq.isEmpty() && mst.size() < G.V()-1)
        {
            UndirectedEdge e = pq.delMin();
            int v = e.either(), w = e.other(v);
            if (uf.connected(v, w)) continue;
            uf.union(v, w);
            mst.push(e);
        }
    }
    @Override
    public Iterable<UndirectedEdge> edges() {
        return mst;
    }
}
