package generators;

import algorithms.DepthFirstSearch;
import collections.Bag;
import edges.DirectedEdge;
import graphs.DirectedGraph;
import graphs.GraphType;

import java.util.*;

/**
 * Created by Amanda Curyło on 12.04.2018.
 */
public class BarabasiDirectedGraphGenerator {
    private List<Integer> outDegrees; //Out degree of each node
    private List<Integer> inDegrees; //In degree of each node
    private int m0;                //The initial number of nodes
    private  int sumOutDeg;           //The sum of out degrees of all nodes
    private  int sumInDeg;           //The sum of in degrees of all nodes
    private int sumOutDegRemaining;   //The sum of out degrees of nodes not connected to the new node
    private int sumInDegRemaining;   //The sum of in degrees of nodes not connected to the new node
    private Set<Integer> toNodes;//Set of indices of nodes for outgoing edges from new node
    private Set<Integer> fromNodes;//Set of indices of nodes for ingoing edges to new node
    private DirectedGraph G;
    private double maxWeightOfEdge;
    private Random rand;
    private Bag<DirectedEdge>[] adj;
    private int E;                  //Number of all edges
    private int s;                  //source

    @SuppressWarnings("unchecked")
    public BarabasiDirectedGraphGenerator(int m0, int V, double maxWeightOfEdge, int s) {
        if(m0 >= V) throw new IllegalArgumentException("Number of nodes must be higher than initial number of nodes");
        adj = (Bag<DirectedEdge>[]) new Bag[V];
        for (int v = 0; v < V; v++)
            adj[v] = new Bag<>();
        this.s = s;
        rand = new Random();
        outDegrees = new ArrayList<>();
        inDegrees = new ArrayList<>();
        toNodes = new HashSet<>();
        fromNodes = new HashSet<>();
        this.m0 = m0;
        this.maxWeightOfEdge = maxWeightOfEdge;

        initial();

        for (int i = 0; i < V - m0; i++)
            addNewNode();

        G = new DirectedGraph(V, E, GraphType.WEIGHTED, adj);

        makeSourceConnectedToReamining();

        end();
    }

    //Initial connected network of m0 nodes
    private void initial() {
        double weight = rand.nextDouble()*maxWeightOfEdge;
        initialConnectedNetwork();

        //Initial degrees of m0 nodes for connected graph
        for (int i = 0; i < m0; i++) {
            outDegrees.add(m0 - 1);
            inDegrees.add(m0 - 1);
        }

        //Initial sum of degrees
        sumOutDeg = m0*(m0-1);
        sumInDeg = m0*(m0-1);
    }

    private void initialConnectedNetwork() {
        for (int i = 0; i < m0 - 1; i++) {
            for (int j = i + 1; j < m0; j ++) {
                if (i != j) {
                    addEdge(i, j, rand.nextDouble() * maxWeightOfEdge);
                    addEdge(j, i, rand.nextDouble() * maxWeightOfEdge);
                }
            }
        }
    }

    //Add edges to adjacency list
    private void addEdge(int v, int w, double weight) {
        DirectedEdge edge = new DirectedEdge(v, w, weight);
        adj[v].add(edge);
        E++;
    }

    private void addNewNode() {
        int nodeCount = outDegrees.size();      //It could be also inDegrees.size()

        //Attach to an from how many existing nodes?
        int numOfOutLinks = rand.nextInt(m0) + 1;
        int numOfInLinks = rand.nextInt(m0) + 1;

        //Remaining (out/in) degrees sum for every new node equals sumDeg at start
        sumOutDegRemaining = sumOutDeg;
        sumInDegRemaining = sumInDeg;

        //Choose the nodes to outgoing links
        for (int i = 0; i < numOfOutLinks; i++)
            chooseNodeToOutLink();

        //Choose the nodes to ingoing links
        for (int i = 0; i < numOfInLinks; i++)
            chooseNodeToInLink();

        //Update degrees and add edges to new node
        //When adding out link from new node, in degree of other node is increased
        //When adding in link to new node, out degree of other node is increased
        for (int i : toNodes) {
            addEdge(nodeCount, i, rand.nextDouble()*maxWeightOfEdge);
            inDegrees.set(i, inDegrees.get(i) + 1);
        }
        for (int i : fromNodes) {
            addEdge(i, nodeCount, rand.nextDouble()*maxWeightOfEdge);
            outDegrees.set(i, outDegrees.get(i) + 1);
        }

        //Clear set of connected nodes to new node and update sumDeg and list of degrees
        toNodes.clear();
        fromNodes.clear();
        outDegrees.add(numOfOutLinks);
        inDegrees.add(numOfInLinks);
        sumOutDeg += numOfOutLinks + numOfInLinks;
        sumInDeg += numOfOutLinks + numOfInLinks;
    }

    private void chooseNodeToOutLink() {
        //Choose node base on the probability
        //giving more chance to a node if it has a high degree.
        if(m0 == 1 && outDegrees.size() == 1){
            toNodes.add(0);
        }
        else {
            int r = rand.nextInt(sumOutDegRemaining);
            int runningSum = 0;
            int i = 0;
            while (runningSum <= r) {
                if (!toNodes.contains(i))
                    runningSum += outDegrees.get(i);
                i++;
            }
            i--;
            toNodes.add(i);
            sumOutDegRemaining -= outDegrees.get(i);
        }
    }

    private void chooseNodeToInLink() {
        //Choose node base on the probability
        //giving more chance to a node if it has a high degree.
        if(m0 == 1 && inDegrees.size() == 1){
            fromNodes.add(0);
        }
        else {
            int r = rand.nextInt(sumInDegRemaining);
            int runningSum = 0;
            int i = 0;
            while (runningSum <= r) {
                if (!fromNodes.contains(i))
                    runningSum += inDegrees.get(i);
                i++;
            }
            i--;
            fromNodes.add(i);
            sumInDegRemaining -= inDegrees.get(i);
        }
    }

    private void end() {
        outDegrees.clear();
        inDegrees.clear();
        outDegrees = null;
        inDegrees = null;
        toNodes = null;
        fromNodes = null;
    }

    private void  makeSourceConnectedToReamining() {
        while (!isSourceConnectedToRemaining()) {
            int v = rand.nextInt(G.V());
            int w = rand.nextInt(G.V());
            double weight = rand.nextDouble()*maxWeightOfEdge;
            if(!G.containsEdge(v,w)) {
                DirectedEdge edge = new DirectedEdge(v,w, weight);
                G = G.addEdge(edge);
            }
        }
    }

    private boolean isSourceConnectedToRemaining() {
        DepthFirstSearch dfs = G.executeDepthFirstSearch(s);
        int countAvailableNodes = 0;
        for(int v = 0; v < G.V(); v++) {
            if(v != s && dfs.hasPathTo(v))
                countAvailableNodes++;
        }
        return countAvailableNodes == G.V() - 1;
    }

    public DirectedGraph getG() {
        return G;
    }
}
