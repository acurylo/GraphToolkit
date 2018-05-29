package tests;

import algorithms.*;
import generators.BarabasiDirectedGraphGenerator;
import graphs.DirectedGraph;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Amanda Curyło on 12.04.2018.
 */
public class DirectedGraphAlgoithmsTest {
    private static List<Long> bfsTime;
    private static List<Long> dfsTime;
    private static List<Long> dijkstraTime;
    private static List<Long> cycleTime;
    private static List<Long> connectedComponentsTime;
    private static List<Long> topologicalSort;
    private static List<Integer> vList;

    public static void main(String[] args) throws IOException {
       // vList = Arrays.asList(5,10,15,20,30,40,50,55,60,65,70,75,80,85,90,95,100);
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
        dijkstraTime = new ArrayList<>();
        cycleTime = new ArrayList<>();
        connectedComponentsTime = new ArrayList<>();
        topologicalSort = new ArrayList<>();
        for (Integer v: vList) {
            for (int p = 0; p < numberOfProbes; p++) {
                measureTime(v, m0);  //measures the duration of algorithms
            }
            saveInFile(v, numberOfProbes);
            clear();    //clear lists to test with other number of nodes
        }
    }

    static private void measureTime(int V, int m0){
        Random random = new Random();
        int s = random.nextInt(V);
        long start, stop, difference;
        DirectedGraph G = new BarabasiDirectedGraphGenerator(m0,V,15,s).getG();

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
        Dijkstra dijkstra = new Dijkstra(G, s);
        stop = System.nanoTime();
        difference = stop-start;
        dijkstraTime.add(difference);

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

        start = System.nanoTime();
        TopologicalSort sort = new TopologicalSort(G);
        stop = System.nanoTime();
        difference = stop-start;
        topologicalSort.add(difference);
    }

    static private void saveNodesInFile() throws IOException {
        Path path = Paths.get("C:", "Users", "Amanda", "Documents",
                "magisterka", "wyinikiTestow", "directedGraphsAlgorithmsTest", "nodes.txt" );
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
                "magisterka", "wyinikiTestow", "directedGraphsAlgorithmsTest", "testV" + V + ".txt" );
        File file = path.toFile();
        FileWriter writer = new FileWriter(file);
        for (int i = 0; i < numberOfProbes; i++) {
            StringBuilder line = new StringBuilder();
            line.append(bfsTime.get(i))
                    .append("\t")
                    .append(dfsTime.get(i))
                    .append("\t")
                    .append(dijkstraTime.get(i))
                    .append("\t")
                    .append(cycleTime.get(i))
                    .append("\t")
                    .append(connectedComponentsTime.get(i))
                    .append("\t")
                    .append(topologicalSort.get(i));
            writer.write(line.toString());
            writer.write("\n");
        }
        writer.close();
    }

    static private void clear() {
        bfsTime.clear();
        dfsTime.clear();
        dijkstraTime.clear();
        cycleTime.clear();
        connectedComponentsTime.clear();
        topologicalSort.clear();
    }
}
