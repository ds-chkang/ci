package datamining.graph.analysis;

import datamining.graph.layout.MyDirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;

import java.io.Serializable;

public class MyAnalyzerGraph<V, E>
extends MyDirectedSparseMultigraph<V, E>
implements Serializable {

    public double MAX_EDGE_VALUE = 0.0D;

    public MyAnalyzerGraph() { super(EdgeType.DIRECTED); }


}
