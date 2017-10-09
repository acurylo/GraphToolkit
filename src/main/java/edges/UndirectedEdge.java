package edges;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Amanda Curyło on 08.10.2017.
 */
public class UndirectedEdge implements Edge, Comparable<UndirectedEdge> {
    private final int v;
    private final int w;
    private final Map<String, Double> weight;

    public UndirectedEdge(int v, int w, Map<String,Double> weight) {
        this.v = v;
        this.w = w;
        this.weight = new HashMap<>();
        if(weight.containsKey("weight"))
            this.weight.putAll(weight);
        else
            throw new IllegalArgumentException("\"weight\" key required.");
    }

    public UndirectedEdge(UndirectedEdge e) {
        this.v = e.v;
        this.w = e.w;
        this.weight = new HashMap<>();
        this.weight.putAll(e.weight);
    }

    public int either() {
        return v;
    }

    public int other(int vertex) {
        if      (vertex == v) return w;
        else if (vertex == w) return v;
        else throw new RuntimeException("Inconsistent edge.");
    }

    @Override
    public double weight(String weightName) {
        if (weight.containsKey(weightName))
            return weight.get(weightName);
        else
            throw new IllegalArgumentException("\"" + weightName + "\"" + " is not a weight name.");
    }

    public boolean checkVertexes(UndirectedEdge e) {
        int v = e.either();
        int w = e.other(v);
        return checkVertexes(v, w);
    }

    @Override
    public boolean checkVertexes(int v, int w) {
        return (this.v == v && this.w == w) || (this.v == w && this.w == v);
    }

    @Override
    public String between() {
        return v + "-" + w + "," + weight;
    }

    @Override
    public int compareTo(UndirectedEdge that) {
        if (this.weight("weight") < that.weight("weight")) return -1;
        else if (this.weight("weight") > that.weight("weight")) return +1;
        else return 0;
    }
}
