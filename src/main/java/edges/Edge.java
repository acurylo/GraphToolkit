package edges;

import java.util.Map;

/**
 * Created by Amanda Curyło on 08.10.2017.
 */
public interface Edge {
    int either();
    int other(int vertex);
    double weight(String weightName);
    Map<String, Double> weights();
    boolean checkVertexes(int v, int w);
    String between();
}
