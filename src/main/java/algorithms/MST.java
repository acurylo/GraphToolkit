package algorithms;

import edges.UndirectedEdge;

/**
 * Created by Amanda Curyło on 04.01.2018.
 */
public interface MST {
    Iterable<UndirectedEdge> edges();
    default double weight() {
        Iterable<UndirectedEdge> edges = edges();
        double weight = 0;
        for (UndirectedEdge e : edges)
            weight += e.weight("weight");
        return weight;
    }
}
