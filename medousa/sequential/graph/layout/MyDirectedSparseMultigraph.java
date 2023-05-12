package medousa.sequential.graph.layout;

import edu.uci.ics.jung.graph.AbstractTypedGraph;
import medousa.sequential.graph.MyNode;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;

import java.util.*;

public class MyDirectedSparseMultigraph<V, E>
extends AbstractTypedGraph<V, E> {

    protected Map<V, Pair<Set<E>>> vertices = new HashMap();
    protected Map<E, Pair<V>> edges = new HashMap();

    public Map<String, MyNode> vRefs = new HashMap<>();
    public Set<String> edRefs =  new HashSet();;

    public MyDirectedSparseMultigraph(EdgeType edgeType) { super(edgeType); }
    public MyDirectedSparseMultigraph() {
        super(EdgeType.DIRECTED);
    } // EdgeType.DIRECTED
    public Collection<E> getEdges() {
        return Collections.unmodifiableCollection(this.edges.keySet());
    }
    public Collection<V> getVertices() {
        return Collections.unmodifiableCollection(this.vertices.keySet());
    }
    public boolean containsVertex(V vertex) {
        return this.vertices.keySet().contains(vertex);
    }
    public boolean containsEdge(E edge) {
        return this.edges.keySet().contains(edge);
    }
    protected Collection<E> getIncoming_internal(V vertex) { return this.vertices.get(vertex).getFirst(); }
    protected Collection<E> getOutgoing_internal(V vertex) { return this.vertices.get(vertex).getSecond(); }
    public MyNode getVertex(String nodeName) { return this.vRefs.get(nodeName); }
    public boolean addVertex(V vertex) {
        this.vertices.put(vertex, new Pair(new HashSet(), new HashSet()));
        return true;
    }
    public boolean removeVertex(V vertex) {
        if (!this.containsVertex(vertex)) {
            return false;
        } else {
            Set<E> incident = new HashSet(this.getIncoming_internal(vertex));
            incident.addAll(this.getOutgoing_internal(vertex));
            Iterator itr = incident.iterator();
            while(itr.hasNext()) {
                E edge = (E)itr.next();
                this.removeEdge(edge);
            }
            this.vertices.remove(vertex);
            return true;
        }
    }

    public boolean removeEdge(E edge) {
        if (!this.containsEdge(edge)) {
            return false;
        } else {
            Pair<V> endpoints = this.getEndpoints(edge);
            V source = endpoints.getFirst();
            V dest = endpoints.getSecond();
            this.getOutgoing_internal(source).remove(edge);
            this.getIncoming_internal(dest).remove(edge);
            this.edges.remove(edge);
            return true;
        }
    }

    public Collection<E> getInEdges(V vertex) {
        return !this.containsVertex(vertex) ? null : Collections.unmodifiableCollection(this.getIncoming_internal(vertex));
    }

    public Collection<E> getOutEdges(V vertex) {
        return !this.containsVertex(vertex) ? null : Collections.unmodifiableCollection(this.getOutgoing_internal(vertex));
    }

    public Collection<V> getPredecessors(V vertex) {
        if (!this.containsVertex(vertex)) {
            return null;
        } else {
            Set<V> preds = new HashSet();
            Iterator itr = this.getIncoming_internal(vertex).iterator();

            while(itr.hasNext()) {
                E edge = (E)itr.next();
                preds.add(this.getSource(edge));
            }
            return Collections.unmodifiableCollection(preds);
        }
    }

    public Collection<V> getSuccessors(V vertex) {
        if (!this.containsVertex(vertex)) {
            return null;
        } else {
            Set<V> succs = new HashSet();
            Iterator itr = this.getOutgoing_internal(vertex).iterator();

            while(itr.hasNext()) {
                E edge = (E)itr.next();
                succs.add(this.getDest(edge));
            }
            return Collections.unmodifiableCollection(succs);
        }
    }

    public Collection<V> getNeighbors(V vertex) {
        if (!this.containsVertex(vertex)) {
            return null;
        } else {
            Collection<V> neighbors = new HashSet();
            Iterator itr = this.getIncoming_internal(vertex).iterator();

            Object edge;
            while(itr.hasNext()) {
                edge = itr.next();
                neighbors.add(this.getSource((E)edge));
            }
            itr = this.getOutgoing_internal(vertex).iterator();
            while(itr.hasNext()) {
                edge = itr.next();
                neighbors.add(this.getDest((E)edge));
            }
            return Collections.unmodifiableCollection(neighbors);
        }
    }

    public Collection<E> getIncidentEdges(V vertex) {
        if (!this.containsVertex(vertex)) {
            return null;
        } else {
            Collection<E> incident = new HashSet();
            incident.addAll(this.getIncoming_internal(vertex));
            incident.addAll(this.getOutgoing_internal(vertex));
            return incident;
        }
    }

    public E findEdge(V v1, V v2) {
        if (this.containsVertex(v1) && this.containsVertex(v2)) {
            Iterator itr = this.getOutgoing_internal(v1).iterator();
            Object edge;
            do {
                if (!itr.hasNext()) {
                    return null;
                }
                edge = itr.next();
            } while(!this.getDest((E)edge).equals(v2));
            return (E)edge;
        } else {
            return null;
        }
    }


    public boolean addEdge(E edge, Pair<? extends V> endpoints, EdgeType edgeType) {
        try {
            this.validateEdgeType(edgeType);
            Pair<V> new_endpoints = this.getValidatedEndpoints(edge, endpoints);

                this.edges.put(edge, new_endpoints);
                V source = new_endpoints.getFirst();
                V dest = new_endpoints.getSecond();
                if (!this.containsVertex(source)) {
                    this.addVertex(source);
                }

                if (!this.containsVertex(dest)) {
                    this.addVertex(dest);
                }

                this.getIncoming_internal(dest).add(edge);
                this.getOutgoing_internal(source).add(edge);


        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return true;
    }

    public V getSource(E edge) {
        return !this.containsEdge(edge) ? null : this.getEndpoints(edge).getFirst();
    }
    public V getDest(E edge) {
        return !this.containsEdge(edge) ? null : this.getEndpoints(edge).getSecond();
    }
    public boolean isSource(V vertex, E edge) {
        return this.containsEdge(edge) && this.containsVertex(vertex) ? vertex.equals(this.getEndpoints(edge).getFirst()) : false;
    }
    public boolean isDest(V vertex, E edge) {
        return this.containsEdge(edge) && this.containsVertex(vertex) ? vertex.equals(this.getEndpoints(edge).getSecond()) : false;
    }
    public Pair<V> getEndpoints(E edge) {
        return this.edges.get(edge);
    }
    public int getEdgeCount() {
        return this.edges.size();
    }
    public int getVertexCount() {
        return this.vertices.size();
    }
}
