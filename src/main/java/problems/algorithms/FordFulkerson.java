package problems.algorithms;

import edges.DirectedEdge;
import graphs.DirectedGraph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Created by Amanda Curyło on 26.03.2018.
 */
public class FordFulkerson {
    private List<Double> flow;
    private List<DirectedEdge> edges;
    private boolean[] marked;
    private DirectedEdge[] edgeTo;
    private double value;

    public FordFulkerson(int s, int t, DirectedGraph G) {
        flow = new ArrayList<>();
        edges = new ArrayList<>();
        for (DirectedEdge e: G.edges()) {
            flow.add(0.0);
            edges.add(e);
        }
        while (hasAugmentingPath(s, t, G)) {
            double bottle = Double.POSITIVE_INFINITY;
            for (int v = t; v != s; v = edgeTo[v].other(v))
                bottle = Math.min(bottle, residualCapacityTo(edgeTo[v], v));
            for (int v = t; v != s; v = edgeTo[v].other(v))
               addResidualFlowTo(edgeTo[v], v, bottle);
            value += bottle;
        }
    }
    public double value() { return value; }
    public boolean inCut(int v) { return marked[v]; }

    private boolean hasAugmentingPath(int s, int t, DirectedGraph G) {
        marked = new boolean[G.V()]; // Is path to this vertex known?
        edgeTo = new DirectedEdge[G.V()]; // last edge on path
        Queue<Integer> q = new ArrayDeque<>();
        marked[s] = true; // Mark the source
        q.offer(s); // and put it on the queue.
        while (!q.isEmpty()) {
            int v = q.poll();
            for (DirectedEdge e : G.adj(v)) {
                int w = e.other(v);
                if (residualCapacityTo(e, w) > 0 && !marked[w]) { // For every edge to an unmarked vertex (in residual)
                    edgeTo[w] = e; // Save the last edge on a path.
                    marked[w] = true; // Mark w because a path is known
                    q.offer(w); // and add it to the queue.
                }
            }
        }
        return marked[t];
    }

    public double residualCapacityTo(DirectedEdge e, int vertex) {
        int v = e.from();
        int w = e.to();
        if (vertex == v) return flow.get(find(e));
        else if (vertex == w) return e.weight("weight") - flow.get(find(e));
        else throw new RuntimeException("Inconsistent edge");
    }

    public void addResidualFlowTo(DirectedEdge e, int vertex, double delta) {
        int v = e.from();
        int w = e.to();
        int i = find(e);
        if (vertex == v) flow.set(i, flow.get(i) - delta);
        else if (vertex == w) flow.set(i, flow.get(i) + delta);
        else throw new RuntimeException("Inconsistent edge");
    }

    public int find(DirectedEdge e) {
        for (int i = 0; i < edges.size(); i++) {
            if(e.checkVertexes(edges.get(i)))
                return i;
        }
        return -1;
    }
}
