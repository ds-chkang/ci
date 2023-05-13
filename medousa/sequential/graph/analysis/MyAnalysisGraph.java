package medousa.sequential.graph.analysis;

import edu.uci.ics.jung.graph.util.EdgeType;
import medousa.sequential.graph.layout.MyDirectedSparseMultigraph;

import java.io.Serializable;

public class MyAnalysisGraph<V, E>
extends MyDirectedSparseMultigraph<V, E>
implements Serializable {

    public double MAX_EDGE_VALUE = 0.0D;

    public MyAnalysisGraph() { super(EdgeType.DIRECTED); }


}
