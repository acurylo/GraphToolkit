package algorithms;

import edges.DirectedEdge;
import edges.Edge;
import graphs.DirectedGraph;
import graphs.UndirectedGraph;

/**
 * Created by Amanda Curyło on 03.01.2018.
 */
public class ConnectedComponents {
    private boolean[] marked;
    private int[] id;
    private int count;

    public ConnectedComponents(UndirectedGraph G) {
        marked = new boolean[G.V()];
        id = new int[G.V()];
        for (int s = 0; s < G.V(); s++)
            if (!marked[s]) {
                dfs(G, s);
                count++;
            }
    }

    private void dfs(UndirectedGraph G, int v) {
        id[v] = count;
        marked[v] = true;
        for(Edge e : G.adj(v))
        {
            int w = e.other(v);
            if(!marked[w])
            {
                dfs(G, w);
            }
        }
    }

    public ConnectedComponents(DirectedGraph G) {
        marked = new boolean[G.V()];
        id = new int[G.V()];
        DepthFirstOrder order = new DepthFirstOrder(G.reverse());
        for (int s : order.reversePost())
            if (!marked[s])
            { dfs(G, s); count++; }
    }

    private void dfs(DirectedGraph G, int v) {
        marked[v] = true;
        id[v] = count;
        for (DirectedEdge e : G.adj(v)) {
            int w = e.to();
            if (!marked[w])
                dfs(G, w);
        }
    }

    public boolean connected(int v, int w) {
        return id[v] == id[w]; }

    public int id(int v) {
        return id[v]; }

    public int countComponents() {
        return count; }

    public boolean isConnected() {
        return count == 1;
    }
}
