package problems;

import edges.DirectedEdge;
import graphs.DirectedGraph;
import problems.algorithms.OptimType;
import problems.algorithms.Simplex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Amanda Curyło on 21.03.2018.
 */
public class ShortestPathTree {
    private double optimValue;
    private double solution[];
    private Map<DirectedEdge,Double> path;

    public ShortestPathTree(int s, DirectedGraph G) {
        path = new HashMap<>();
        int N = G.E();
        int M = 2*((G.V()-1)+1);  //G.V()-1 -constraints for all nodes except s(source)
                                  //+1 for equation related to outgoing units from s(source)
                                  //*2 to make standard form

        double[][] A = new double[M][N];
        double[] c = new double[N];
        double[] b = new double[M];

        List<DirectedEdge> edges = new ArrayList<>();
        int iter = 0;
        for (DirectedEdge e : G.edges()) {
            edges.add(e);
            c[iter] = e.weight("weight");
            iter++;
        }

        //constraints related to number of outgoing units from source
        for(int j = 0; j < N; j++) {
            if(edges.get(j).from()==s) {
                A[0][j] = 1;
                A[1][j] = -1;
            }
        }
        //corresponding coefficients in the vector b
        b[0] = G.V() - 1;
        b[1] = -(G.V() - 1);

        //constraints related to all nodes except s(source)
        int row = 0;
        for (int v = 0; v < G.V(); v++) {
            if(v != s) {
                row = row + 2;
                for (int w = 0; w < G.E(); w++) {
                    if(edges.get(w).from() == v) {
                        A[row][w] = 1;
                        A[row+1][w] = -1;
                    }
                    if(edges.get(w).to() == v) {
                        A[row][w] = -1;
                        A[row+1][w] = 1;
                    }
                }
            }
        }
        //corresponding coefficients in the vector b
        for(int i = 2; i < M; i = i + 2) {
            b[i] = -1;
            b[i+1]= 1;
        }

        Simplex simplex = new Simplex(A, c, b, OptimType.MINIMIZE);

        optimValue = simplex.getOptim();
        solution = simplex.getSolution();
        for(int i = 0; i <N; i++ ){
            if(solution[i] != 0)
                path.put(edges.get(i),solution[i]);
        }
    }

    public double getOptim() {
        return optimValue;
    }

    public double[] getSolution() {
        return solution;
    }

    public Map<DirectedEdge, Double> getPath() {
        return new HashMap<>(path);
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("MIN PATH\n");
        path.forEach((k,v) ->
                s.append("edge:")
                .append(k.between())
                .append(",flow: ")
                .append(v).append("\n"));
        s.append("Optim value: ").append(optimValue);
        return s.toString();
    }
}
