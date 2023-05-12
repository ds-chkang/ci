package medousa.edu.uci.ics.jung.graph;


import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public abstract class AbstractGraph<V, E> implements Graph<V, E>, Serializable {
    public AbstractGraph() {
    }

    public void addEdge(E edge, Collection<? extends V> vertices) {
        this.addEdge(edge, vertices, this.getDefaultEdgeType());
    }

    public void addEdge(E edge, Collection<? extends V> vertices, EdgeType edgeType) {
        this.addEdge(edge, vertices instanceof Pair ? (Pair) vertices : new Pair(vertices), edgeType);
    }

    public void addEdge(E e, V v1, V v2) {
        this.addEdge(e, v1, v2, this.getDefaultEdgeType());
    }

    public void addEdge(E e, V v1, V v2, EdgeType edge_type) {
        this.addEdge(e, new Pair(v1, v2), edge_type);
    }

    public void addEdge(E edge, Pair<? extends V> endpoints) {
        this.addEdge(edge, endpoints, this.getDefaultEdgeType());
    }

    public abstract void addEdge(E var1, Pair<? extends V> var2, EdgeType var3);

    protected Pair<V> getValidatedEndpoints(E edge, Pair<? extends V> endpoints) {

        Pair<V> new_endpoints = new Pair(endpoints.getFirst(), endpoints.getSecond());
        if (this.containsEdge(edge)) {
            Pair<V> existing_endpoints = this.getEndpoints(edge);
            if (!existing_endpoints.equals(new_endpoints)) {
                throw new IllegalArgumentException("edge " + edge + " already exists in this datamining.spm.edu.uci.ics.jung.graph with endpoints " + existing_endpoints + " and cannot be added with endpoints " + endpoints);
            } else {
                return null;
            }
        } else {
            return new_endpoints;
        }

    }

    public int inDegree(V vertex) {
        return this.getInEdges(vertex).size();
    }

    public int outDegree(V vertex) {
        return this.getOutEdges(vertex).size();
    }

    public boolean isPredecessor(V v1, V v2) {
        return this.getPredecessors(v1).contains(v2);
    }

    public boolean isSuccessor(V v1, V v2) {
        return this.getSuccessors(v1).contains(v2);
    }

    public int getPredecessorCount(V vertex) {
        return this.getPredecessors(vertex).size();
    }

    public int getSuccessorCount(V vertex) {
        return this.getSuccessors(vertex).size();
    }

    public boolean isNeighbor(V v1, V v2) {
        if (this.containsVertex(v1) && this.containsVertex(v2)) {
            return this.getNeighbors(v1).contains(v2);
        } else {
            throw new IllegalArgumentException("At least one of these not in this datamining.spm.edu.uci.ics.jung.graph: " + v1 + ", " + v2);
        }
    }

    public boolean isIncident(V vertex, E edge) {
        if (this.containsVertex(vertex) && this.containsEdge(edge)) {
            return this.getIncidentEdges(vertex).contains(edge);
        } else {
            throw new IllegalArgumentException("At least one of these not in this datamining.spm.edu.uci.ics.jung.graph: " + vertex + ", " + edge);
        }
    }

    public int getNeighborCount(V vertex) {
        if (!this.containsVertex(vertex)) {
            throw new IllegalArgumentException(vertex + " is not a vertex in this datamining.spm.edu.uci.ics.jung.graph");
        } else {
            return this.getNeighbors(vertex).size();
        }
    }

    public int degree(V vertex) {
        if (!this.containsVertex(vertex)) {
            throw new IllegalArgumentException(vertex + " is not a vertex in this datamining.spm.edu.uci.ics.jung.graph");
        } else {
            return this.getIncidentEdges(vertex).size();
        }
    }

    public int getIncidentCount(E edge) {
        Pair<V> incident = this.getEndpoints(edge);
        if (incident == null) {
            return 0;
        } else {
            return incident.getFirst() == incident.getSecond() ? 1 : 2;
        }
    }

    public V getOpposite(V vertex, E edge) {
        Pair<V> incident = this.getEndpoints(edge);
        V first = incident.getFirst();
        V second = incident.getSecond();
        if (vertex.equals(first)) {
            return second;
        } else if (vertex.equals(second)) {
            return first;
        } else {
            throw new IllegalArgumentException(vertex + " is not incident to " + edge + " in this datamining.spm.edu.uci.ics.jung.graph");
        }
    }

    public E findEdge(V v1, V v2) {
        Iterator<E> i$ = this.getOutEdges(v1).iterator();

        while (i$.hasNext()) {
            E e = i$.next();
            if (this.getOpposite(v1, e).equals(v2)) {
                return e;
            }
        }

        return null;
    }

    public Collection<E> findEdgeSet(V v1, V v2) {
        if (!this.getVertices().contains(v1)) {
            throw new IllegalArgumentException(v1 + " is not an element of this datamining.spm.edu.uci.ics.jung.graph");
        } else if (!this.getVertices().contains(v2)) {
            throw new IllegalArgumentException(v2 + " is not an element of this datamining.spm.edu.uci.ics.jung.graph");
        } else {
            Collection<E> edges = new ArrayList();
            Iterator<E> i$ = this.getOutEdges(v1).iterator();

            while (i$.hasNext()) {
                E e = i$.next();
                if (this.getOpposite(v1, e).equals(v2)) {
                    edges.add(e);
                }
            }

            return Collections.unmodifiableCollection(edges);
        }
    }

    public Collection<V> getIncidentVertices(E edge) {
        Pair<V> endpoints = this.getEndpoints(edge);
        Collection<V> incident = new ArrayList();
        incident.add(endpoints.getFirst());
        incident.add(endpoints.getSecond());
        return Collections.unmodifiableCollection(incident);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("Vertices:");
        Iterator i$ = this.getVertices().iterator();

        Object e;
        while (i$.hasNext()) {
            e = i$.next();
            sb.append(e + ",");
        }

        sb.setLength(sb.length() - 1);
        sb.append("\nEdges:");
        i$ = this.getEdges().iterator();

        while (i$.hasNext()) {
            e = i$.next();
            Pair<V> ep = this.getEndpoints((E) e);
            sb.append(e + "[" + ep.getFirst() + "," + ep.getSecond() + "] ");
        }

        return sb.toString();
    }
}
