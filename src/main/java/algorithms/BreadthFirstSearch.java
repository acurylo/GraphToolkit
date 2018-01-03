package algorithms;

import edges.Edge;
import graphs.Graph;

import java.util.LinkedList;

/**
 * Created by Amanda Curyło on 03.01.2018.
 */
public class BreadthFirstSearch {
    private boolean marked[];
    private int edgeTo[];
    private final int s;

    public BreadthFirstSearch(Graph G, int s) {
        marked = new boolean[G.V()];
        edgeTo = new int[G.V()];
        this.s = s;
        bfs(G, s);
    }

    private void bfs(Graph G, int s) {
        LinkedList<Integer> queue = new LinkedList<>();
        marked[s] = true;
        queue.push(s);
        while (!queue.isEmpty())
        {
            int v = queue.poll();
            for (Edge e : G.adj(v))
            {
                int w = e.other(v);
                if (!marked[w])
                {
                    edgeTo[w] = v;
                    marked[w] = true;
                    queue.push(w);
                }
            }
        }
    }

    public boolean hasPathTo(int v) {
        return marked[v];
    }

    public Iterable<Integer> pathTo(int v) {
        if(!hasPathTo(v)) throw new IllegalArgumentException("There is no path to this vertex.");
        if (!hasPathTo(v)) return null;
        LinkedList<Integer> path = new LinkedList<>();
        for ( int x = v; x != s; x = edgeTo[x])
            path.push(x);
        path.push(s);
        return path;
    }
}
