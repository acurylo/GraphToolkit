package graphs;

import algorithms.BreadthFirstSearch;
import algorithms.DepthFirstSearch;
import collections.Bag;
import edges.DirectedEdge;
import edges.Edge;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by Amanda Curyło on 11.10.2017.
 */
public class DirectedGraph implements Graph {
    private final int V;
    private final int E;
    private final GraphType type;
    private final Bag<DirectedEdge>[] adj;

    @SuppressWarnings("unchecked")
    public DirectedGraph(int V, GraphType type) {
        this.V = V;
        this.E = 0;
        this.type = type;
        adj = (Bag<DirectedEdge>[]) new Bag[V];
        for (int v = 0; v < V; v++)
            adj[v] = new Bag<>();
    }

    @SuppressWarnings("unchecked")
    public DirectedGraph(String fileName, GraphType type) throws FileNotFoundException {
        File file = new File(fileName);
        Scanner in = new Scanner(file);
        this.type = type;
        this.V = Integer.parseInt(in.nextLine());
        boolean[][] adjMatrix = new boolean[V][V];
        Bag<DirectedEdge>[] adjContainer = (Bag<DirectedEdge>[]) new Bag[V];
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
                DirectedEdge edge = new DirectedEdge(v, w, weight);
                adjContainer[v].add(edge);
                adjMatrix[v][w] = true;
                e++;
            }
        }
        in.close();
        this.E = e;
        adj = adjContainer;
    }

    private DirectedGraph(int V, int E, GraphType type, Bag<DirectedEdge>[] adj) {
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
    public DirectedGraph addVertex() {
        return createGraphWithNewVertex();
    }

    private DirectedGraph createGraphWithNewVertex() {
        return createGraphWithNewVertexes(1);
    }

    @Override
    public DirectedGraph addVertex(int vertexCount) {
        return createGraphWithNewVertexes(vertexCount);
    }

    @SuppressWarnings("unchecked")
    private DirectedGraph createGraphWithNewVertexes(int vertexCount) {
        Bag<DirectedEdge>[] adjCopy = (Bag<DirectedEdge>[]) new Bag[V+vertexCount];
        for (int v = 0; v < V+vertexCount; v++)
            adjCopy[v] = new Bag<>();
        for (int v = 0; v < V; v++) {
            for (DirectedEdge e : adj[v]) {
                DirectedEdge eCopy = new DirectedEdge(e);
                adjCopy[v].add(eCopy);
            }
        }
        return new DirectedGraph(V + vertexCount, E, type, adjCopy);
    }

    @Override
    public DirectedGraph removeVertex(int v) {
        if(V == 0) throw new IndexOutOfBoundsException("");
        return createGraphWithoutVertex(v);
    }

    @SuppressWarnings("unchecked")
    private DirectedGraph createGraphWithoutVertex(int v) {
        int newE = 0;
        Bag<DirectedEdge>[] adjCopy = (Bag<DirectedEdge>[]) new Bag[V-1];
        for (int u = 0; u < V - 1; u++)
            adjCopy[u] = new Bag<>();
        Iterable<DirectedEdge> edges = edges();
        for (DirectedEdge e : edges) {
            int from = e.from();
            int to = e.to();
            if(from != v && to != v) {
                if(from > v) from--;
                if(to > v) to--;
                DirectedEdge edge = new DirectedEdge(from, to, e.weights());
                adjCopy[from].add(edge);
                newE++;
            }
        }
        return new DirectedGraph(V - 1, newE, type, adjCopy);
    }


    @Override
    public DirectedGraph addEdge(int v, int w, Map<String, Double> weight) {
        if (containsEdge(v, w)) throw new IllegalArgumentException("Graph already contains this edge.");
        DirectedEdge edge = new DirectedEdge(v, w, weight);
        return createGraphWithNewEdge(edge);
    }

    public DirectedGraph addEdge(DirectedEdge edge) {
        if (containsEdge(edge)) throw new IllegalArgumentException("Graph already contains this edge.");
        return createGraphWithNewEdge(edge);
    }

    @SuppressWarnings("unchecked")
    private DirectedGraph createGraphWithNewEdge(DirectedEdge e) {
        Bag<DirectedEdge>[] adjCopy = (Bag<DirectedEdge>[]) new Bag[V];
        for (int v = 0; v < V; v++)
            adjCopy[v] = new Bag<>();
        for (int v = 0; v < V; v++) {
            for (DirectedEdge edge : adj[v]) {
                DirectedEdge edgeCopy = new DirectedEdge(edge);
                adjCopy[v].add(edgeCopy);
            }
        }
        DirectedEdge eCopy = new DirectedEdge(e);
        int from = eCopy.from();
        adjCopy[from].add(eCopy);
        return new DirectedGraph(V, E + 1, type, adjCopy);
    }

    public DirectedGraph addEdges(Iterable<DirectedEdge> edges) {
        return createGraphWithNewEdges(edges);
    }

    @SuppressWarnings("unchecked")
    private DirectedGraph createGraphWithNewEdges(Iterable<DirectedEdge> edges) {
        Bag<DirectedEdge>[] adjCopy = (Bag<DirectedEdge>[]) new Bag[V];
        for (int v = 0; v < V; v++)
            adjCopy[v] = new Bag<>();
        for (int v = 0; v < V; v++) {
            for (DirectedEdge e : adj[v]) {
                DirectedEdge eCopy = new DirectedEdge(e);
                adjCopy[v].add(eCopy);
            }
        }
        int newE = 0;
        Bag<DirectedEdge> existingEdges = new Bag<>();
        for (DirectedEdge e : edges) {
            if (!containsEdge(e)) {
                DirectedEdge eCopy = new DirectedEdge(e);
                int from = eCopy.from();
                adjCopy[from].add(eCopy);
                newE++;
            }
            else existingEdges.add(new DirectedEdge(e));
        }
        if (newE == 0) throw new IllegalArgumentException("Graph contains all of this edges");
        else {
            System.out.println("Edges thaa graph already contained:");
            existingEdges.forEach(e -> System.out.println(e.between()));
        }
        return new DirectedGraph(V, E + newE, type, adjCopy);
    }

    @Override
    public DirectedGraph removeEdge(int v, int w) {
        if(!containsEdge(v, w)) throw  new IllegalArgumentException("Graph does not contain this edge.");
        return createGraphWithoutEdge(v, w);
    }

    @SuppressWarnings("unchecked")
    private DirectedGraph createGraphWithoutEdge(int u, int w) {
        Bag<DirectedEdge>[] adjCopy = (Bag<DirectedEdge>[]) new Bag[V];
        for (int v = 0; v < V; v++)
            adjCopy[v] = new Bag<>();
        for (int v = 0; v < V; v++) {
            for (DirectedEdge e : adj[v]) {
                if (!e.checkVertexes(u, w)) {
                    DirectedEdge eCopy = new DirectedEdge(e);
                    adjCopy[v].add(eCopy);
                }
            }
        }
        return new DirectedGraph(V, E - 1, type, adjCopy);
    }

    @Override
    public Iterable<DirectedEdge> edges() {
        Bag<DirectedEdge> edges = new Bag<>();
        for (int v = 0; v < V; v++)
            for (DirectedEdge e : adj[v]) {
                DirectedEdge edge = new DirectedEdge(e);
                edges.add(edge);
            }
        return edges;
    }

    @Override
    public Iterable<DirectedEdge> adj(int v) {
        Bag<DirectedEdge> adjCopy = new Bag<>();
        for(DirectedEdge e : adj[v]) {
            DirectedEdge edge = new DirectedEdge(e);
            adjCopy.add(edge);
        }
        return adjCopy;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Bag<DirectedEdge>[] getAdjCopy() {
        Bag<DirectedEdge>[] adjCopy = (Bag<DirectedEdge>[]) new Bag[V];
        for (int v = 0; v < V; v++) {
            adjCopy[v] = new Bag<>();
            for(DirectedEdge e : adj[v]) {
                DirectedEdge eCopy = new DirectedEdge(e);
                adjCopy[v].add(eCopy);
            }
        }
        return adjCopy;
    }

    @Override
    public Iterable<? extends Edge> nbsI(int v) {
        return null;
    }

    @Override
    public Iterable<? extends Edge> nbsO(int v) {
        return adj(v);
    }

    @Override
    public boolean containsEdge(int v, int w) {
        for (DirectedEdge e : adj[v])
            if (e.checkVertexes(v, w))
                return true;
        return false;
    }

    public boolean containsEdge(DirectedEdge edge) {
        int v = edge.from();
        for (DirectedEdge e : adj[v])
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
        return new DepthFirstSearch(new DirectedGraph(V, E, type, adj), s);
    }

    @Override
    public BreadthFirstSearch executeBreadthFirstSearch(int s) {
        return new BreadthFirstSearch(new DirectedGraph(V, E, type, adj), s);
    }

    public int degreeIn (int v) {
        int count = 0;
        for (DirectedEdge e : edges()) {
            if(e.to() == v)
                count++;
        }
        return count;
    }

    public int degreeOut (int v) {
      return adj[v].size();
    }
}
