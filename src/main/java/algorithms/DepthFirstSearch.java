package algorithms;

import edges.Edge;
import graphs.Graph;

import java.util.LinkedList;

/**
 * Created by Amanda Curyło on 03.01.2018.
 */
public class DepthFirstSearch {
    private boolean[] marked;
    private int count;
    private int[] edgeTo;
    private final int s;

    public DepthFirstSearch(Graph G, int s) {
        marked = new boolean[G.V()];
        edgeTo = new int[G.V()];
        this.s = s;
        dfs(G, s);
    }

    private void dfs(Graph G, int v) {
        marked[v] = true;
        count++;
        for (Edge e : G.adj(v)){
            int w = e.other(v);
            if (!marked[w]) {
                edgeTo[w] = v;
                dfs(G, w);
            }
        }
    }

    public boolean hasPathTo(int v) {
        return marked[v]; }

    public Iterable<Integer> pathTo(int v) {
        if(!hasPathTo(v)) throw new IllegalArgumentException("There is no path to this vertex.");
        if (!hasPathTo(v)) return null;
        LinkedList<Integer> path = new LinkedList<>();
        for (int x = v; x != s; x = edgeTo[x])
            path.push(x);
        path.push(s);
        return path;
    }

    public int countConnectedComponent() {
        return count;
    }
}

