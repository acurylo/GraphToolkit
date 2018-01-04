package graphs;

import algorithms.BreadthFirstSearch;
import algorithms.ConnectedComponents;
import algorithms.Cycle;
import algorithms.DepthFirstSearch;
import collections.Bag;
import edges.Edge;
import edges.UndirectedEdge;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by Amanda Curyło on 09.10.2017.
 */
public class UndirectedGraph implements Graph {
    private final int V;
    private final int E;
    private final GraphType type;
    private final Bag<UndirectedEdge>[] adj;

    @SuppressWarnings("unchecked")
    public UndirectedGraph(int V, GraphType type) {
        this.V = V;
        this.E = 0;
        this.type = type;
        adj = (Bag<UndirectedEdge>[]) new Bag[V];
        for (int v = 0; v < V; v++)
            adj[v] = new Bag<>();
    }

    @SuppressWarnings("unchecked")
    public UndirectedGraph(String fileName, GraphType type) throws FileNotFoundException {
        File file = new File(fileName);
        Scanner in = new Scanner(file);
        this.type = type;
        this.V = Integer.parseInt(in.nextLine());
        boolean[][] adjMatrix = new boolean[V][V];
        Bag<UndirectedEdge>[] adjContainer = (Bag<UndirectedEdge>[]) new Bag[V];
        int e = 0;
        for (int v = 0; v < V; v++)
            adjContainer[v] = new Bag<>();
        while (in.hasNext()) {
            Map<String, Double> weight = new HashMap<>();
            String edges[] = in.nextLine().split("[-,:]");
            int v = Integer.parseInt(edges[0].trim());
            int w = Integer.parseInt(edges[1].trim());
            if (!adjMatrix[v][w] && v != w) {
                if (type == GraphType.WEIGHTED) {
                    for (int i = 2; i < edges.length; i += 2) {
                        weight.put(edges[i].trim(), Double.parseDouble(edges[i + 1].trim()));
                    }
                }
                else {
                    weight.put("weight", 0.0);
                }
                UndirectedEdge edge = new UndirectedEdge(v, w, weight);
                adjContainer[v].add(edge);
                adjContainer[w].add(edge);
                adjMatrix[v][w] = true;
                adjMatrix[w][v] = true;
                e++;
            }
        }
        in.close();
        this.E = e;
        adj = adjContainer;
    }

    private UndirectedGraph(int V, int E, GraphType type, Bag<UndirectedEdge>[] adj) {
        this.V = V;
        this.E = E;
        this.type = type;
        this.adj = adj;
    }

    @Override
    public int V() {
        return V;
    }

    @Override
    public int E() {
        return E;
    }

    @Override
    public UndirectedGraph addVertex() {
        return createGraphWithNewVertex();
    }

    private UndirectedGraph createGraphWithNewVertex() {
        return createGraphWithNewVertexes(1);
    }

    @Override
    public UndirectedGraph addVertex(int vertexCount) {
        return createGraphWithNewVertexes(vertexCount);
    }

    @SuppressWarnings("unchecked")
    private UndirectedGraph createGraphWithNewVertexes(int vertexCount) {
        Bag<UndirectedEdge>[] adjCopy = (Bag<UndirectedEdge>[]) new Bag[V+vertexCount];
        for (int v = 0; v < V+vertexCount; v++)
            adjCopy[v] = new Bag<>();
        for (int v = 0; v < V; v++) {
            for (UndirectedEdge e : adj[v]) {
                UndirectedEdge eCopy = new UndirectedEdge(e);
                adjCopy[v].add(eCopy);
            }
        }
        return new UndirectedGraph(V + vertexCount, E, type, adjCopy);
    }

    @Override
    public UndirectedGraph removeVertex(int v) {
        if(V == 0) throw new IndexOutOfBoundsException("");
        return createGraphWithoutVertex(v);
    }

    @SuppressWarnings("unchecked")
    private UndirectedGraph createGraphWithoutVertex(int v) {
        int newE = 0;
        Bag<UndirectedEdge>[] adjCopy = (Bag<UndirectedEdge>[]) new Bag[V-1];
        for (int u = 0; u < V - 1; u++)
            adjCopy[u] = new Bag<>();
        Iterable<UndirectedEdge> edges = edges();
        for (UndirectedEdge e : edges) {
            int either = e.either();
            int other = e.other(either);
            if(either != v && other != v) {
                if(either > v) either--;
                if(other > v) other--;
                UndirectedEdge edge = new UndirectedEdge(either, other, e.weights());
                adjCopy[either].add(edge);
                adjCopy[other].add(edge);
                newE++;
            }
        }
        return new UndirectedGraph(V - 1, newE, type, adjCopy);
    }

    @Override
    public UndirectedGraph addEdge(int v, int w, Map<String, Double> weight) {
        if (containsEdge(v, w)) throw new IllegalArgumentException("Graph already contains this edge.");
        UndirectedEdge edge = new UndirectedEdge(v, w, weight);
        return createGraphWithNewEdge(edge);
    }

    public UndirectedGraph addEdge(UndirectedEdge edge) {
        if (containsEdge(edge)) throw new IllegalArgumentException("Graph already contains this edge.");
        return createGraphWithNewEdge(edge);
    }

    @SuppressWarnings("unchecked")
    private UndirectedGraph createGraphWithNewEdge(UndirectedEdge e) {
        Bag<UndirectedEdge>[] adjCopy = (Bag<UndirectedEdge>[]) new Bag[V];
        for (int v = 0; v < V; v++)
            adjCopy[v] = new Bag<>();
        for (int v = 0; v < V; v++) {
            for (UndirectedEdge edge : adj[v]) {
                UndirectedEdge edgeCopy = new UndirectedEdge(edge);
                adjCopy[v].add(edgeCopy);
            }
        }
        UndirectedEdge eCopy = new UndirectedEdge(e);
        int v = eCopy.either();
        int w = eCopy.other(v);
        adjCopy[v].add(eCopy);
        adjCopy[w].add(eCopy);
        return new UndirectedGraph(V, E + 1, type, adjCopy);
    }

    public UndirectedGraph addEdges(Iterable<UndirectedEdge> edges) {
        return createGraphWithNewEdges(edges);
    }

    @SuppressWarnings("unchecked")
    private UndirectedGraph createGraphWithNewEdges(Iterable<UndirectedEdge> edges) {
        Bag<UndirectedEdge>[] adjCopy = (Bag<UndirectedEdge>[]) new Bag[V];
        for (int v = 0; v < V; v++)
            adjCopy[v] = new Bag<>();
        for (int v = 0; v < V; v++) {
            for (UndirectedEdge e : adj[v]) {
                UndirectedEdge eCopy = new UndirectedEdge(e);
                adjCopy[v].add(eCopy);
            }
        }
        int newE = 0;
        Bag<UndirectedEdge> existingEdges = new Bag<>();
        for (UndirectedEdge e : edges) {
            if (!containsEdge(e)) {
                UndirectedEdge eCopy = new UndirectedEdge(e);
                int v = eCopy.either();
                int w = eCopy.other(v);
                adjCopy[v].add(eCopy);
                adjCopy[w].add(eCopy);
                newE++;
            }
            else existingEdges.add(new UndirectedEdge(e));
        }
        if (newE == 0) throw new IllegalArgumentException("Graph contains all of this edges");
        else {
                System.out.println("Edges thaa graph already contained:");
                existingEdges.forEach(e -> System.out.println(e.between()));
        }
        return new UndirectedGraph(V, E + newE, type, adjCopy);
    }

    @Override
    public UndirectedGraph removeEdge(int v, int w) {
        if(!containsEdge(v, w)) throw  new IllegalArgumentException("Graph does not contain this edge.");
        return createGraphWithoutEdge(v, w);
    }

    @SuppressWarnings("unchecked")
    private UndirectedGraph createGraphWithoutEdge(int u, int w) {
        Bag<UndirectedEdge>[] adjCopy = (Bag<UndirectedEdge>[]) new Bag[V];
        for (int v = 0; v < V; v++)
            adjCopy[v] = new Bag<>();
        for (int v = 0; v < V; v++) {
            for (UndirectedEdge e : adj[v]) {
                if (!e.checkVertexes(u, w)) {
                    UndirectedEdge eCopy = new UndirectedEdge(e);
                    adjCopy[v].add(eCopy);
                }
            }
        }
        return new UndirectedGraph(V, E - 1, type, adjCopy);
    }

    @Override
    public Iterable<UndirectedEdge> edges() {
        Bag<UndirectedEdge> edges = new Bag<>();
        for (int v = 0; v < V; v++)
            for (UndirectedEdge e : adj[v])
                if (e.other(v) > v) {
                    UndirectedEdge edge = new UndirectedEdge(e);
                    edges.add(edge);
                }
        return edges;
    }

    @Override
    public Iterable<UndirectedEdge> adj(int v) {
        Bag<UndirectedEdge> adjCopy = new Bag<>();
        for(UndirectedEdge e : adj[v]) {
            UndirectedEdge edge = new UndirectedEdge(e);
            adjCopy.add(edge);
        }
        return adjCopy;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Bag<UndirectedEdge>[] getAdjCopy() {
        Bag<UndirectedEdge>[] adjCopy = (Bag<UndirectedEdge>[]) new Bag[V];
        for (int v = 0; v < V; v++) {
            adjCopy[v] = new Bag<>();
            for(UndirectedEdge e : adj[v]) {
                UndirectedEdge eCopy = new UndirectedEdge(e);
                adjCopy[v].add(eCopy);
            }
        }
        return adjCopy;
    }

    @Override
    public Iterable<? extends Edge> nbsI(int v) {
        return adj(v);
    }

    @Override
    public Iterable<? extends Edge> nbsO(int v) {
        return adj(v);
    }

    @Override
    public boolean containsEdge(int v, int w) {
        for (UndirectedEdge e : adj[v])
            if (e.checkVertexes(v, w))
                return true;
        return false;
    }

    public boolean containsEdge(UndirectedEdge edge) {
        int v = edge.either();
        for (UndirectedEdge e : adj[v])
            if (e.checkVertexes(edge))
                return true;
        return false;
    }

    @Override
    public boolean isWeightedGraph() {
        return type == GraphType.WEIGHTED;
    }

    @Override
    public DepthFirstSearch executeDepthFirstSearch(int s) {
        return new DepthFirstSearch(new UndirectedGraph(V, E, type, adj), s);
    }

    @Override
    public BreadthFirstSearch executeBreadthFirstSearch(int s) {
        return new BreadthFirstSearch(new UndirectedGraph(V, E, type, adj), s);
    }

    @Override
    public Cycle executeCycle() {
        return new Cycle(new UndirectedGraph(V, E, type, adj));
    }

    @Override
    public ConnectedComponents executeConnectedComponents() {
        return new ConnectedComponents(new UndirectedGraph(V, E, type, adj));
    }

    public int degree(int v) {
        return adj[v].size();
    }

    public int maxDegree() {
        int max = 0;
        for (int v = 0; v < V; v++)
            if(degree(v) > max)
                max = degree(v);
        return max;
    }

    public int avgDegree() {
        return 2*E/V;
    }

    public boolean isCompleteGraph() {
        return E == V*(V-1)/2;
    }

    //TODO
    public boolean isTree() {
       return false;
    }

}
