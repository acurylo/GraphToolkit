package problems;

import edges.DirectedEdge;
import graphs.DirectedGraph;
import problems.algorithms.OptimType;
import problems.algorithms.Simplex;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amanda Curyło on 21.03.2018.
 */
public class MinCut {
    private double optimValue;
    private double solution[];
    private List<DirectedEdge> cut;
    private List<Integer> setWithT;
    private List<Integer> setWithS;

    public MinCut(int s, int t, DirectedGraph G) {
        cut = new ArrayList<>();
        setWithT = new ArrayList<>();
        setWithS = new ArrayList<>();
        int N = G.E() + G.V();
        int M = G.E() + 4;  //+4 because two equations related to s and t
                            //are changed into four equations (standard form)

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

        //constraints related to group members
        for (int i = 0; i < G.E(); i++) {
            for (int j = 0; j < N; j++) {
                if (i == j)
                    A[i][j] = 1;
                else if (j == edges.get(i).from() + G.E())
                    A[i][j] = -1;
                else if (j == edges.get(i).to() + G.E())
                    A[i][j] = 1;
            }
        }
        //corresponding coefficients in the vector b are equal to 0

        //constraints related to s(source) and t(sink)
        A[G.E()][s + G.E()] = 1;
        A[G.E() + 1][s + G.E()] = -1;
        A[G.E() + 2][t + G.E()] = 1;
        A[G.E() + 3][t + G.E()] = -1;
        //corresponding coefficients in the vector b
        b[G.E()] = 1;
        b[G.E() + 1] = -1;

        Simplex simplex = new Simplex(A, c, b, OptimType.MINIMIZE);

        optimValue = simplex.getOptim();
        solution = simplex.getSolution();

        for(int i = 0; i < N; i++){
            if(i < G.E() && solution[i] != 0)
                cut.add(edges.get(i));
            else if(i >= G.E() && solution[i] != 0)
                setWithS.add(i - G.E());
            else if(i >= G.E() && solution[i] == 0)
                setWithT.add(i - G.E());
        }
    }

    public double getOptim() {
        return optimValue;
    }

    public double[] getSolution() {
        return solution;
    }

    public List<DirectedEdge> getCut() {
        return new ArrayList<>(cut);
    }

    public List<Integer> getSetWithT() {
        return new ArrayList<>(setWithT);
    }

    public List<Integer> getSetWithS() {
        return new ArrayList<>(setWithS);
    }

    public String toString(){
        StringBuilder s = new StringBuilder();
        s.append("MIN CUT\n");
        s.append("Cut:\n");
        cut.forEach((e) ->
                s.append("edge:")
                .append(e.between())
                .append("\n"));
        s.append("Vertex in set with s:\n");
        setWithS.forEach((v) -> s.append(v).append("\t"));
        s.append("\nVertex in set with t:\n");
        setWithT.forEach((v) -> s.append(v).append("\t"));
        s.append("\nOptim value: ").append(optimValue);
        return s.toString();
    }
}
