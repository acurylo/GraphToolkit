package edges;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Amanda Curyło on 08.10.2017.
 */
public class DirectedEdge implements Edge, Comparable<DirectedEdge> {
    private final int v;
    private final int w;
    private final Map<String, Double> weight;

    public DirectedEdge(int v, int w, Map<String, Double> weight) {
        this.v = v;
        this.w = w;
        this.weight = new HashMap<>();
        if(weight.containsKey("weight"))
            this.weight.putAll(weight);
        else
            throw new IllegalArgumentException("\"weight\" key required.");
    }

    public DirectedEdge(DirectedEdge e) {
        this.v = e.v;
        this.w = e.w;
        this.weight = new HashMap<>();
        this.weight.putAll(e.weight);
    }

    public int from() {
        return v;
    }

    public int to() {
        return w;
    }

    public DirectedEdge reverse() {
        return new DirectedEdge(to(), from(), weight);
    }

    @Override
    public double weight(String weightName) {
        if (weight.containsKey(weightName))
            return weight.get(weightName);
        else
            throw new IllegalArgumentException("\"" + weightName + "\"" + " is not a weight name.");
    }

    public boolean checkVertexes(DirectedEdge e) {
        int v = e.from();
        int w = e.to();
        return checkVertexes(v, w);
    }

    @Override
    public boolean checkVertexes(int v, int w) {
        return (this.v == v && this.w == w);
    }

    @Override
    public String between() {
        return v + "->" + w + "," + weight;
    }

    @Override
    public int compareTo(DirectedEdge that) {
        if (this.weight("weight") < that.weight("weight")) return -1;
        else if (this.weight("weight") > that.weight("weight")) return +1;
        else return 0;
    }
}
