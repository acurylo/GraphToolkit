package graphs;

import algorithms.BreadthFirstSearch;
import algorithms.DepthFirstSearch;
import collections.Bag;
import edges.Edge;

import java.util.Map;

/**
 * Created by Amanda Curyło on 09.10.2017.
 */
public interface Graph {
    int V();
    int E();
    Graph addVertex();
    Graph addVertex(int vertexCount);
    Graph removeVertex(int v);
    Graph addEdge(int v, int w, Map<String, Double> weight);
    Graph removeEdge(int v, int w);
    Iterable<? extends Edge> edges();
    Iterable<? extends Edge> adj(int v);
    Bag<? extends Edge>[] getAdjCopy();
    Iterable<? extends Edge> nbsI(int v);
    Iterable<? extends Edge> nbsO(int v);
    boolean containsEdge(int v, int w);
    boolean isWeightedGraph();
    DepthFirstSearch executeDepthFirstSearch(int s);
    BreadthFirstSearch executeBreadthFirstSearch(int s);
 //TODO
 // Cycle executeCycle();
 // Dijkstra executeDijkstra(int s);
}
