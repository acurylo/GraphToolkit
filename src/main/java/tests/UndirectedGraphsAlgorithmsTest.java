package tests;

import algorithms.*;
import generators.BarabasiUndirectedGraphGenerator;
import graphs.UndirectedGraph;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by Amanda Curyło on 01.04.2018.
 */
public class UndirectedGraphsAlgorithmsTest {
    private static List<Long> bfsTime;
    private static List<Long> dfsTime;
    private static List<Long> kruskalTime;
    private static List<Long> primTime;
    private static List<Long> dijkstraTime;
    private static List<Long> cycleTime;
    private static List<Long> connectedComponentsTime;
    private static List<Integer> vList;
    private static Set<Integer> eSet;
    private static Map<Integer,TreeSet<Integer>> nodeEdgeMap;

    public static void main(String[] args) throws IOException {
        eSet = new TreeSet<>();
        nodeEdgeMap = new TreeMap<>();
 //     vList = Arrays.asList(5,10,15,20,30,40,50);
        vList = new ArrayList<>();
        int value = 9;
        while(value < 20) {
            value += 1;
            vList.add(value);
        }

        while(value < 100) {
            value += 5;
            vList.add(value);
        }
        while(value < 5000) {
            value += 10;
            vList.add(value);
        }
        while(value < 10000) {
            value += 20;
            vList.add(value);
        } while(value < 30000) {
            value += 50;
            vList.add(value);
        }
        int m0 = 3;
        saveNodesInFile();
        int numberOfProbes = 30;
        bfsTime = new ArrayList<>();
        dfsTime = new ArrayList<>();
        kruskalTime = new ArrayList<>();
        primTime = new ArrayList<>();
        dijkstraTime = new ArrayList<>();
        cycleTime = new ArrayList<>();
        connectedComponentsTime = new ArrayList<>();
        for (Integer v: vList) {
            for (int p = 0; p < numberOfProbes; p++) {
                measureTime(v, m0);  //measures the duration of algorithms
            }
            saveInFile(v, numberOfProbes);
            clear();    //clear lists to test with other number of nodes
        }
        saveEdgesInFile();
        saveNodesEdgesInFile();
    }

    static private void measureTime(int V, int m0) throws IOException {
        Random random = new Random();
        int s = random.nextInt(V);
        long start, stop, difference;
        UndirectedGraph G = new BarabasiUndirectedGraphGenerator(m0,V,15d).getG();
        eSet.add(G.E());
        if(nodeEdgeMap.containsKey(G.V())){
            nodeEdgeMap.get(G.V()).add(G.E());
        }
        else {
            TreeSet<Integer> edges = new TreeSet<>();
            edges.add(G.E());
            nodeEdgeMap.putIfAbsent(G.V(), edges);
        }

        start = System.nanoTime();
        BreadthFirstSearch bfs = new BreadthFirstSearch(G, s);
        stop = System.nanoTime();
        difference = stop-start;
        bfsTime.add(difference);

        start = System.nanoTime();
        DepthFirstSearch dfs = new DepthFirstSearch(G,s);
        stop = System.nanoTime();
        difference = stop-start;
        dfsTime.add(difference);

        start = System.nanoTime();
        Kruskal kruskal = new Kruskal(G);
        stop = System.nanoTime();
        difference = stop-start;
        kruskalTime.add(difference);
        groupByEdgesToFile(G.E(),difference);

        start = System.nanoTime();
        Prim prim = new Prim(G);
        stop = System.nanoTime();
        difference = stop-start;
        primTime.add(difference);
        groupByEdgesNodesToFile(G.E(), G.V(), difference, "prim");

        start = System.nanoTime();
        Dijkstra dijkstra = new Dijkstra(G, s);
        stop = System.nanoTime();
        difference = stop-start;
        dijkstraTime.add(difference);
        groupByEdgesNodesToFile(G.E(), G.V(), difference, "dijkstra");

        start = System.nanoTime();
        Cycle cycle = new Cycle(G);
        stop = System.nanoTime();
        difference = stop-start;
        cycleTime.add(difference);

        start = System.nanoTime();
        ConnectedComponents components = new ConnectedComponents(G);
        stop = System.nanoTime();
        difference = stop-start;
        connectedComponentsTime.add(difference);
    }

    static private void saveNodesInFile() throws IOException {
        Path path = Paths.get("C:", "Users", "Amanda", "Documents",
                "magisterka", "wyinikiTestow", "undirectedGraphsAlgorithmsTest", "nodes.txt" );
        File file = path.toFile();
        StringBuilder line = new StringBuilder();
        for (Integer node: vList) {
            line.append(node).append("\t");
        }
        FileWriter writer = new FileWriter(file);
        writer.write(line.toString());
        writer.close();
    }

    static private void saveInFile(int V, int numberOfProbes) throws IOException {
        Path path = Paths.get("C:", "Users", "Amanda", "Documents",
                "magisterka", "wyinikiTestow", "undirectedGraphsAlgorithmsTest", "testV" + V + ".txt" );
        File file = path.toFile();
        FileWriter writer = new FileWriter(file);
        for (int i = 0; i < numberOfProbes; i++) {
            StringBuilder line = new StringBuilder();
            line.append(bfsTime.get(i))
                    .append("\t")
                    .append(dfsTime.get(i))
                    .append("\t")
                    .append(kruskalTime.get(i))
                    .append("\t")
                    .append(primTime.get(i))
                    .append("\t")
                    .append(dijkstraTime.get(i))
                    .append("\t")
                    .append(cycleTime.get(i))
                    .append("\t")
                    .append(connectedComponentsTime.get(i));
            writer.write(line.toString());
            writer.write("\n");
        }
        writer.close();
    }

    static private void groupByEdgesToFile(int E, long time) throws IOException {
        Path path = Paths.get("C:", "Users", "Amanda", "Documents",
                "magisterka", "wyinikiTestow", "groupByEdges", "kruskalE" + E + ".txt" );
        File file = path.toFile();
        FileWriter writer = new FileWriter(file,true);
        writer.write(time + "\t");
        writer.close();
    }

    static private void groupByEdgesNodesToFile(int E, int V, long time, String algorithm) throws IOException {
        Path path = Paths.get("C:", "Users", "Amanda", "Documents",
                "magisterka", "wyinikiTestow", "groupByEdgesNodes", algorithm + "V" + V + "E" + E + ".txt" );
        File file = path.toFile();
        FileWriter writer = new FileWriter(file,true);
        writer.write(time + "\t");
        writer.close();
    }


    static private void saveEdgesInFile() throws IOException {
        Path path = Paths.get("C:", "Users", "Amanda", "Documents",
                "magisterka", "wyinikiTestow", "groupByEdges", "edges.txt" );
        File file = path.toFile();
        StringBuilder line = new StringBuilder();
        for (Integer edge: eSet) {
            line.append(edge).append("\t");
        }
        FileWriter writer = new FileWriter(file);
        writer.write(line.toString());
        writer.close();
    }

    static private void saveNodesEdgesInFile() throws IOException {
        Path path = Paths.get("C:", "Users", "Amanda", "Documents",
                "magisterka", "wyinikiTestow", "groupByEdgesNodes", "nodesEdges.txt" );
        File file = path.toFile();
        StringBuilder line = new StringBuilder();
        for (Integer v: nodeEdgeMap.keySet()) {
            for (Integer e: nodeEdgeMap.get(v)) {
                line.append(v).append("\t").append(e).append("\n");
            }
        }
        FileWriter writer = new FileWriter(file);
        writer.write(line.toString());
        writer.close();
    }

    static private void clear() {
        bfsTime.clear();
        dfsTime.clear();
        kruskalTime.clear();
        primTime.clear();
        dijkstraTime.clear();
        cycleTime.clear();
        connectedComponentsTime.clear();
    }
}
