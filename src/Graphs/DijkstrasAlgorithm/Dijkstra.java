package Graphs.DijkstrasAlgorithm;
import static Graphs.DijkstrasAlgorithm.Graph.*;

/**
 * For a given source vertex (node) in the graph, the algorithm finds the path with lowest cost
 * (i.e. the shortest path) between that vertex and every other vertex. It can also be used for
 * finding costs of shortest paths from a single vertex to a single destination vertex by stopping
 * the algorithm once the shortest path to the destination vertex has been determined.
 *
 * @author kkmonlee
 */
public class Dijkstra {

    private static final Edge[] GRAPH = {
            new Edge("a", "b", 7),
            new Edge("a", "c", 9),
            new Edge("a", "f", 14),
            new Edge("b", "c", 10),
            new Edge("b", "d", 15),
            new Edge("c", "d", 11),
            new Edge("c", "f", 2),
            new Edge("d", "e", 6),
            new Edge("e", "f", 9),
    };

}
