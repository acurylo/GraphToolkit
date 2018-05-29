package tests;

import edges.DirectedEdge;
import generators.BarabasiDirectedGraphGenerator;
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
import java.util.List;
import java.util.Random;

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
//      vList = Arrays.asList(5,10,15,20,30,40,50);
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
        while(value < 300) {
            value += 10;
            vList.add(value);
        }

        for(int i = 0; i < vList.size(); i++) {
            System.out.println("nodes[" + i +"] = " + vList.get(i) +";");
        }

         int m0 = 3;
         saveNodesInFile();
         int numberOfProbes = 30;
         fordTime = new ArrayList<>();
         flowTime = new ArrayList<>();
         cutTime = new ArrayList<>();
         treeTime = new ArrayList<>();
        for (Integer v: vList) {
            for (int p = 0; p < numberOfProbes; p++) {
               measureTime(v, m0, p);  //measures the duration of algorithms
            }
            saveInFile(v, numberOfProbes);
            clear();    //clear lists to test with other number of nodes
        }
    }

    static private void measureTime(int V, int m0, int iter) throws IOException {
        Random random = new Random();
        int s = random.nextInt(V);
        int t;
        do {
            t = random.nextInt(V);
        } while (s == t);
        long start, stop, difference;
        DirectedGraph G = new BarabasiDirectedGraphGenerator(m0,V, 15, s).getG();
        creatDataFile(G, iter, s, t);

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

        Path pathCplex = Paths.get("C:", "Users", "Amanda", "Documents",
                "magisterka", "wyinikiTestow","problemsTestCplex", "nodes.txt" );
        File fileCplex = pathCplex.toFile();
        FileWriter writerCplex = new FileWriter(fileCplex);
        writerCplex.write(line.toString());
        writerCplex.close();
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

    static private void creatDataFile(DirectedGraph G, int iter, int s, int t) throws IOException {
        File theDir = new File("C:\\Users\\Amanda\\Documents\\magisterka\\wyinikiTestow\\dat\\V" + G.V());

        theDir.mkdir();

        Path path = Paths.get("C:", "Users", "Amanda", "Documents",
                "magisterka", "wyinikiTestow","dat", "V" + G.V(), "dat" + iter + ".dat" );
        File file = path.toFile();

        FileWriter writer = new FileWriter(file);
        writer.write("n=" + G.V() + ";\n");
        writer.write("s=" + (s+1) + ";\n");
        writer.write("t=" + (t+1) + ";\n");
        StringBuilder edges = new StringBuilder();
        StringBuilder capacity = new StringBuilder();
        for (DirectedEdge e : G.edges()) {
            edges.append("<")
                    .append(e.from()+1)
                    .append(",")
                    .append(e.to()+1)
                    .append("> ");
            capacity.append(e.weight("weight")).append(" ");
        }
        writer.write("A={" + edges.toString() + "};\n");
        writer.write("c=[" + capacity.toString() + "];");
        writer.close();
    }
}
