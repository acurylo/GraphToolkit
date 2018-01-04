package algorithms;

import edges.Edge;
import graphs.DirectedGraph;
import graphs.UndirectedGraph;

import java.util.LinkedList;

/**
 * Created by Amanda Curyło on 03.01.2018.
 */
public class Cycle {
    private boolean[] marked;
    private int[] edgeTo;
    private LinkedList<Integer> cycle;
    private boolean[] onStack;

    public Cycle(UndirectedGraph G) {
        marked = new boolean[G.V()];
        onStack = new boolean[G.V()];
        edgeTo = new int[G.V()];
        for (int v = 0; v < G.V(); v++)
            if (!marked[v]) dfs(G, v, v);
    }

    public Cycle(DirectedGraph G) {
        marked = new boolean[G.V()];
        onStack = new boolean[G.V()];
        edgeTo = new int[G.V()];
        for (int v = 0; v < G.V(); v++)
            if (!marked[v]) dfs(G, v, v);
    }

    private void dfs(UndirectedGraph G, int v, int u) {
        marked[v] = true;
        onStack[v] = true;
        for (Edge e : G.adj(v))
        {
            int w = e.other(v);
            if (hasCycle()) return;
            else if (!marked[w]) {
                edgeTo[w] = v;
                dfs(G, w, v);
            }
            else if (w != u) {
                cycle = new LinkedList<>();
                for (int x = v; x != w; x = edgeTo[x])
                    cycle.push(x);
                cycle.push(w);
                cycle.push(v);
            }
        }
        onStack[v] = false;
    }

    private void dfs(DirectedGraph G, int v, int u) {
        marked[v] = true;
        onStack[v] = true;
        for (Edge e : G.adj(v))
        {
            int w = e.other(v);
            if (hasCycle()) return;
            else if (!marked[w]) {
                edgeTo[w] = v;
                dfs(G, w, v);
            }
            else if (onStack[w]) {
                cycle = new LinkedList<>();
                for (int x = v; x != w; x = edgeTo[x])
                    cycle.push(x);
                cycle.push(w);
                cycle.push(v);
            }
        }
        onStack[v] = false;
    }

    public boolean hasCycle() {
        return cycle != null;
    }

    public Iterable<Integer> cycle() {
        if(!hasCycle()) throw new NullPointerException("There is no cycle.");
        return cycle;
    }

}
