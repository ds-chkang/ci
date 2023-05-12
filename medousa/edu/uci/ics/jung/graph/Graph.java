package medousa.edu.uci.ics.jung.graph;

import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;

import java.util.Collection;

public interface Graph<V, E> extends Hypergraph<V, E> {
    Collection<E> getInEdges(V var1);

    Collection<E> getOutEdges(V var1);

    Collection<V> getPredecessors(V var1);

    Collection<V> getSuccessors(V var1);

    int inDegree(V var1);

    int outDegree(V var1);

    boolean isPredecessor(V var1, V var2);

    boolean isSuccessor(V var1, V var2);

    int getPredecessorCount(V var1);

    int getSuccessorCount(V var1);

    V getSource(E var1);

    V getDest(E var1);

    boolean isSource(V var1, E var2);

    boolean isDest(V var1, E var2);

    void addEdge(E var1, V var2, V var3);

    void addEdge(E var1, V var2, V var3, EdgeType var4);

    Pair<V> getEndpoints(E var1);

    V getOpposite(V var1, E var2);
}
