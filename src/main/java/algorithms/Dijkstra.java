package algorithms;

import collections.IndexMinPQ;
import edges.Edge;
import graphs.Graph;

import java.util.LinkedList;

/**
 * Created by Amanda Curyło on 05.01.2018.
 */
public class Dijkstra {
    private Edge[] edgeTo;
    private double[] distTo;
    private IndexMinPQ<Double> pq;

    public  Dijkstra(Graph G, int s) {
        if(!G.isWeightedGraph()) throw new IllegalArgumentException("Graph is unweighted.");
        edgeTo = new Edge[G.V()];
        distTo = new double[G.V()];
        pq = new IndexMinPQ<>(G.V());
        for (int v = 0; v < G.V(); v++ )
        {
            distTo[v] = Double.POSITIVE_INFINITY;
        }
        distTo[s] = 0.0;
        pq.insert(s, 0.0);
        while (!pq.isEmpty())
        {
            relax(G, pq.delMin());
        }
    }

    private void relax(Graph G, int v) {
        for (Edge e : G.adj(v))
        {
            int w = e.other(v);
            if(distTo[w] > distTo[v] + e.weight("weight"))
            {
                distTo[w] = distTo[v] + e.weight("weight");
                edgeTo[w] = e;
                if (pq.contains(w)) pq.change(w, distTo[w]);
                else pq.insert(w, distTo[w]);
            }

        }
    }

    public double distTo(int v) {
        return distTo[v];
    }

    public boolean hasPathTo(int v) {
        return distTo[v] < Double.POSITIVE_INFINITY;
    }

    public Iterable<Edge> pathTo(int v) {
        if(!hasPathTo(v)) throw new NullPointerException("There is no path to this vertex.");
        LinkedList<Edge> path = new LinkedList<>();
        for (Edge e = edgeTo[v]; e != null; e = edgeTo[v]) {
            v = e.other(v);
            path.push(e);
        }
        return path;
    }
}
