package problems;

import edges.DirectedEdge;
import graphs.DirectedGraph;
import problems.algorithms.OptimType;
import problems.algorithms.Simplex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Amanda Curyło on 21.03.2018.
 */
public class MaxFlow {
    private double optimValue;
    private double[] solution;
    private Map<DirectedEdge,Double> flow;

    public MaxFlow(int s, int t, DirectedGraph G) {
        flow = new HashMap<>();
        int N = G.E();
        int M = G.E() + 2*(G.V() - 2); //number of edges plus nodes - s(source) - t(sink)
                                       //*2 to make standard form
        double[][] A = new double [M][N];
        double[] c = new double[N];
        double[] b = new double[M];

        ArrayList<DirectedEdge> edges = new ArrayList<>();
        int i = 0 ;
        for (DirectedEdge e : G.edges()) {
            edges.add(e);
            if(e.from() == s)
                c[i] = 1;
            i++;
        }

        int k = 0;
        //capacity constraints
        for (int j = 0; j < G.E(); j++) {
            A[j][k] = 1;
            k++;
        }
        //corresponding coefficients in the vector b
        for (int j = 0; j < G.E(); j++) {
            b[j] = edges.get(j).weight("weight");
        }

        //Kirchhoff equations for transit nodes(V\{s,t})
        int row = G.E() - 1;
        for (int v = 0; v < G.V(); v++) {
            if(v != s && v != t) {
                row++;
                for (int w = 0; w < G.E(); w++) {
                    if(edges.get(w).from() == v) {
                        A[row][w] = -1;
                        A[row+3][w] = 1;
                    }
                    if(edges.get(w).to() == v) {
                        A[row][w] = 1;
                        A[row+3][w] = -1;
                    }
                }
            }
        }
        //corresponding coefficients in the vector b are equal to 0

        Simplex simplex = new Simplex(A, c, b, OptimType.MAXIMIZE);
        optimValue = simplex.getOptim();
        solution = simplex.getSolution();

        for(int j = 0; j < solution.length; j++){
            flow.put(edges.get(j),solution[j]);
        }
    }

    public double getOptim() {
        return optimValue;
    }

    public double[] getSolution() {
        return solution;
    }

    public Map<DirectedEdge, Double> getFlow() {
        return new HashMap<>(flow);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("MAX FLOW\n");
        flow.forEach((k,v) ->
                s.append("edge:")
                .append(k.between())
                .append(", flow: ")
                .append(v)
                .append("\n"));
        s.append("Optim value: ").append(optimValue);
        return s.toString();
    }
}
