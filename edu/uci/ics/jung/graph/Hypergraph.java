package edu.uci.ics.jung.graph;


import edu.uci.ics.jung.graph.util.EdgeType;

import java.util.Collection;

public interface Hypergraph<V, E> {
    Collection<E> getEdges();

    Collection<V> getVertices();

    boolean containsVertex(V var1);

    boolean containsEdge(E var1);

    int getEdgeCount();

    int getVertexCount();

    Collection<V> getNeighbors(V var1);

    Collection<E> getIncidentEdges(V var1);

    Collection<V> getIncidentVertices(E var1);

    E findEdge(V var1, V var2);

    Collection<E> findEdgeSet(V var1, V var2);

    void addVertex(V var1);

    void addEdge(E var1, Collection<? extends V> var2);

    void addEdge(E var1, Collection<? extends V> var2, EdgeType var3);

    boolean removeVertex(V var1);

    boolean removeEdge(E var1);

    boolean isNeighbor(V var1, V var2);

    boolean isIncident(V var1, E var2);

    int degree(V var1);

    int getNeighborCount(V var1);

    int getIncidentCount(E var1);

    EdgeType getEdgeType(E var1);

    EdgeType getDefaultEdgeType();

    Collection<E> getEdges(EdgeType var1);

    int getEdgeCount(EdgeType var1);

    Collection<E> getInEdges(V var1);

    Collection<E> getOutEdges(V var1);

    int inDegree(V var1);

    int outDegree(V var1);

    V getSource(E var1);

    V getDest(E var1);

    Collection<V> getPredecessors(V var1);

    Collection<V> getSuccessors(V var1);
}
