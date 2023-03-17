package datamining.graph;

import datamining.graph.layout.MyDirectedSparseMultigraph;
import datamining.utils.system.MyVars;
import edu.uci.ics.jung.graph.util.EdgeType;

import java.io.Serializable;
import java.util.*;

public class MyGraph<V, E>
extends MyDirectedSparseMultigraph<V, E>
implements Serializable {

    public long totalEdgeContribution = 0;
    public double graphEbasilon;
    public long totalContribution;
    public long totalUniqueContribution;
    public long maxUniqueContribution;
    public long maxNodeContribution;
    public long totalReachTime = 0L;
    public long maxTotalReachTime = 0L;
    public Float MX_N_VAL = 0f;
    public double MN_X_VAL = 0f;
    public Float MX_E_VAL = 42f;
    public float DEFALUT_EDGE_VALUE = 4.8f;
    public long minNodeContribution = 1000000000L;
    public long minTotalReachTime = 10000000000000L;
    public long minUniqueContribution = 1000000000L;
    public int remainingNodes = 0;
    public int remainingEdges = 0;
    public double connectance;
    public long totalDuration = 0;
    public long maxDuration = 0;
    public long minDuration = 0;
    public Map<String, MyEdge> edgeRefMap = new HashMap<>();
    public void setMaxNodeValue() {
        Collection<V> nodes = this.getVertices();
        for (V v : nodes) {
            if (((MyNode)v).getCurrentValue() > this.MX_N_VAL) {
                this.MX_N_VAL = ((MyNode)v).getCurrentValue();
            }
        }
    }

    public int getIsolatedNodeCount() {
        int isolated = 0;
        Collection<MyNode> nodes = MyVars.g.getVertices();
        for (MyNode n : nodes) {
            if (MyVars.g.getSuccessorCount(n) == 0 && MyVars.g.getPredecessorCount(n) == 0) {
                isolated++;
            }
        }
        return isolated;
    }

    public void setTotalEdgeContribution() {
        Collection<MyEdge> edges = MyVars.g.getEdges();
        for (MyEdge edge : edges) {
            this.totalEdgeContribution += edge.getContribution();
        }
    }
    public long getTotalEdgeContribution() {return this.totalEdgeContribution;}
    public MyGraph() { super(EdgeType.UNDIRECTED); }

    public Set<MyNode> getDepthNodes() {
        Set<MyNode> depthNodes = new HashSet<>();
        Collection<MyNode> nodes = MyVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getNodeDepthInfo(MyVars.currentGraphDepth) != null) {
                depthNodes.add(n);
            }
        }
        return depthNodes;
    }


}


