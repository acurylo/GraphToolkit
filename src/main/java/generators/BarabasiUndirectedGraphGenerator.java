package generators;

import collections.Bag;
import edges.UndirectedEdge;
import graphs.GraphType;
import graphs.UndirectedGraph;

import java.util.*;

/**
 * Created by Amanda Curyło on 29.03.2018.
 */
public class BarabasiUndirectedGraphGenerator {
    private List<Integer> degrees; //Degree of each node
    private int m0;                //The initial number of nodes
    private  int sumDeg;           //The sum of degrees of all nodes
    private int sumDegRemaining;   //The sum of degrees of nodes not connected to the new node
    private Set<Integer> connected;//Set of indices of nodes connected to the new node
    private UndirectedGraph G;
    private double maxWeightOfEdge;
    private Random rand;
    private Bag<UndirectedEdge>[] adj;
    private int E;                  //Number of all edges

    public BarabasiUndirectedGraphGenerator(int m0, int V, double maxWeightOfEdge) {
        if(m0 >= V) throw new IllegalArgumentException("Number of nodes must be higher than initial number of nodes");
        adj = (Bag<UndirectedEdge>[]) new Bag[V];
        for (int v = 0; v < V; v++)
            adj[v] = new Bag<>();
        rand = new Random();
        degrees = new ArrayList<>();
        connected = new HashSet<>();
        this.m0 = m0;
        this.maxWeightOfEdge = maxWeightOfEdge;

        initial();

        for (int i = 0; i < V - m0; i++)
            addNewNode();

        G = new UndirectedGraph(V, E, GraphType.WEIGHTED, adj);

        end();
    }

    //Initial connected network of m0 nodes
    private void initial() {
        double weight = rand.nextDouble()*maxWeightOfEdge;
        initialConnectedNetwork();

        //Initial degrees of m0 nodes for connected graph
        for (int i = 0; i < m0; i++)
            degrees.add(m0-1);

        //Initial sum of degrees
        sumDeg = m0*(m0-1);
    }

    private void initialConnectedNetwork() {
        for (int i = 0; i < m0 - 1; i++) {
            for (int j = i + 1; j < m0; j ++) {
                addEdge(i, j, rand.nextDouble()*maxWeightOfEdge);
            }
        }
    }

    //Add edges to adjacency list
    private void addEdge(int v, int w, double weight) {
        UndirectedEdge edge = new UndirectedEdge(v, w, weight);
        adj[v].add(edge);
        adj[w].add(edge);
        E++;
    }

    private void addNewNode() {
        int nodeCount = degrees.size();

        //Attach to how many existing nodes?
        int numOfLinks = rand.nextInt(m0) + 1;

        //Remaining degrees sum for every new node equals sumDeg at start
        sumDegRemaining = sumDeg;

        //Choose the nodes to attach to
        for (int i = 0; i < numOfLinks; i++)
            chooseNodeToAttach();

        //Update degrees and add edges to new node
        for (int i : connected) {
            addEdge(nodeCount, i, rand.nextDouble()*maxWeightOfEdge);
            degrees.set(i, degrees.get(i) + 1);
        }

        //Clear set of connected nodes to new node and update sumDeg and list of degrees
        connected.clear();
        degrees.add(numOfLinks);
        sumDeg += 2 * numOfLinks;
    }

    private void chooseNodeToAttach() {
        //Choose node base on the probability
        //giving more chance to a node if it has a high degree.
        if(m0 == 1 && degrees.size() == 1){
            connected.add(0);
        }
        else {
            int r = rand.nextInt(sumDegRemaining);
            int runningSum = 0;
            int i = 0;
            while (runningSum <= r) {
                if (!connected.contains(i))
                    runningSum += degrees.get(i);
                i++;
            }
            i--;
            connected.add(i);
            sumDegRemaining -= degrees.get(i);
        }
    }

    private void end() {
        degrees.clear();
        degrees = null;
        connected = null;
    }

    public UndirectedGraph getG() {
        return G;
    }
}
