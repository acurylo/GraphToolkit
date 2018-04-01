package tests;

import generators.ErdosRenyiGraphGenerators;
import graphs.DirectedGraph;
import problems.MaxFlow;
import problems.MinCut;
import problems.ShortestPathTree;
import problems.algorithms.FordFulkerson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Amanda Curyło on 31.03.2018.
 */
public class ProblemsTest {
    private static List<Long> fordTime;
    private static List<Long> flowTime;
    private static List<Long> cutTime;
    private static List<Long> treeTime;
    private static List<Integer> vList;

    public static void main(String[] args) throws IOException {
         vList = Arrays.asList(5,10,15,20,30,40,50);
         saveNodesInFile();
         int numberOfProbes = 30;
         fordTime = new ArrayList<>();
         flowTime = new ArrayList<>();
         cutTime = new ArrayList<>();
         treeTime = new ArrayList<>();
        for (Integer v: vList) {
            for (int p = 0; p < numberOfProbes; p++) {
               measureTime(v);  //measures the duration of algorithms
            }
            saveInFile(v, numberOfProbes);
            clear();    //clear lists to test with other number of nodes
        }
    }

    static private void measureTime(int V){
        int s = 0;
        int t = V-1;
        long start, stop, difference;
        DirectedGraph G = ErdosRenyiGraphGenerators.generateDirectedGraph(V,15,s,t);

        start = System.nanoTime();
        FordFulkerson ford = new FordFulkerson(s, t, G);
        stop = System.nanoTime();
        difference = stop-start;
        fordTime.add(difference);

        start = System.nanoTime();
        MaxFlow flow = new MaxFlow(s, t, G);
        stop = System.nanoTime();
        difference = stop-start;
        flowTime.add(difference);

        start = System.nanoTime();
        MinCut cut = new MinCut(s, t, G);
        stop = System.nanoTime();
        difference = stop-start;
        cutTime.add(difference);

        start = System.nanoTime();
        ShortestPathTree tree = new ShortestPathTree(s, G);
        stop = System.nanoTime();
        difference = stop-start;
        treeTime.add(difference);
    }

    static private void saveNodesInFile() throws IOException {
        Path path = Paths.get("C:", "Users", "Amanda", "Documents",
                "magisterka", "wyinikiTestow","problemsTest", "nodes.txt" );
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
                "magisterka", "wyinikiTestow", "problemsTest", "testV" + V + ".txt" );
        File file = path.toFile();
        FileWriter writer = new FileWriter(file);
        for (int i = 0; i < numberOfProbes; i++) {
            StringBuilder line = new StringBuilder();
            line.append(fordTime.get(i))
                    .append("\t")
                    .append(flowTime.get(i))
                    .append("\t")
                    .append(cutTime.get(i))
                    .append("\t")
                    .append(treeTime.get(i));
            writer.write(line.toString());
            writer.write("\n");
        }
        writer.close();
    }

    static private void clear() {
        fordTime.clear();
        flowTime.clear();
        cutTime.clear();
        treeTime.clear();
    }
}
