package algorithms;

import edges.DirectedEdge;
import graphs.DirectedGraph;

import java.util.LinkedList;
import java.util.Stack;

/**
 * Created by Amanda Curyło on 03.01.2018.
 */
public class DepthFirstOrder {
    private boolean[] marked;
    private Stack<Integer> pre;
    private Stack<Integer> post;
    private LinkedList<Integer> reversePost;

    public DepthFirstOrder(DirectedGraph G) {
        pre = new Stack<>();
        post = new Stack<>();
        reversePost = new LinkedList<>();
        marked = new boolean[G.V()];
        for (int v = 0; v < G.V(); v++)
            if (!marked[v]) dfs(G, v);
    }

    private void dfs(DirectedGraph G, int v) {
        pre.push(v);
        marked[v] = true;
        for (DirectedEdge e : G.adj(v))
        {
            int w = e.to();
            if (!marked[w])
                dfs(G, w);
        }
        post.push(v);
        reversePost.push(v);
    }

    public Iterable<Integer> pre() {
        return pre;
    }

    public Iterable<Integer> post() {
        return post;
    }

    public Iterable<Integer> reversePost() {
        return reversePost;
    }
}
