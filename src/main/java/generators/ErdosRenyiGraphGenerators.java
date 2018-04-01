package generators;

import algorithms.DepthFirstSearch;
import collections.Bag;
import edges.DirectedEdge;
import edges.UndirectedEdge;
import graphs.DirectedGraph;
import graphs.GraphType;
import graphs.UndirectedGraph;
import graphs.UnionFind;

import java.util.Random;

/**
 * Created by Amanda Curyło on 25.03.2018.
 */
public class ErdosRenyiGraphGenerators {
   public static UndirectedGraph generateUndirectedGraph(int V, double maxWeightofEdge){
        UnionFind uf = new UnionFind(V);
        Random rand = new Random();
        Bag<UndirectedEdge>[] adj = (Bag<UndirectedEdge>[]) new Bag[V];
       boolean[][] adjMatrix = new boolean[V][V];
        int E = 0;
       for (int v = 0; v < V; v++)
           adj[v] = new Bag<>();
        while(!uf.isConnected()) {
            int v = rand.nextInt(V);
            int w = rand.nextInt(V);
            if(!adjMatrix[v][w] && v != w){
                adjMatrix[v][w] = true;
                adjMatrix[w][v] = true;
                UndirectedEdge e = new UndirectedEdge(v, w, rand.nextDouble() * maxWeightofEdge);
                adj[v].add(e);
                adj[w].add(e);
                uf.union(v, w);
                E++;
            }
        }
        return new UndirectedGraph(V, E, GraphType.WEIGHTED, adj);
    }

    public static DirectedGraph generateDirectedGraph(int V, int maxWeightOfEdge, int s, int t) {
        Random rand = new Random();
        DirectedGraph G = new DirectedGraph(V, GraphType.WEIGHTED);
        boolean[][] adjMatrix = new boolean[V][V];
        int E = 0;
        boolean sConnectedToNotAllNodes = true;
        while(sConnectedToNotAllNodes) {
            int from = rand.nextInt(V);
            int to = rand.nextInt(V);
            if(!adjMatrix[from][to] && from != to){
                adjMatrix[from][to] = true;
                G = G.addEdge(new DirectedEdge(from, to, rand.nextInt(maxWeightOfEdge)+1));
                E++;
                DepthFirstSearch dfs = G.executeDepthFirstSearch(s);
                int countAvailableNodes = 0;
                for(int v = 0; v < V; v++) {
                    if(v != s && dfs.hasPathTo(v))
                        countAvailableNodes++;
                }
                if(countAvailableNodes == V-1)
                    sConnectedToNotAllNodes = false;
            }
        }
        return G;
    }

}
