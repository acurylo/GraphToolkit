package algorithms;

import graphs.DirectedGraph;

/**
 * Created by Amanda Curyło on 03.01.2018.
 */
public class TopologicalSort {
    private Iterable<Integer> order;

    public TopologicalSort(DirectedGraph G) {
        if (!G.executeCycle().hasCycle())
            order = G.executeDepthFirstOrder().reversePost();
    }

    public Iterable<Integer> order() {
        if(order == null) throw new NullPointerException("Graph is not a DAG.");
        return order;
    }

    public boolean isDAG() {
        return order != null;
    }
}

