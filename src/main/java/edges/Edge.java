package edges;

/**
 * Created by Amanda Curyło on 08.10.2017.
 */
public interface Edge {
    double weight(String weightName);
    boolean checkVertexes(int v, int w);
    String between();
}
