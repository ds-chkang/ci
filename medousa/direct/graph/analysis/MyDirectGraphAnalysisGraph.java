package medousa.direct.graph.analysis;

import edu.uci.ics.jung.graph.util.EdgeType;

import java.io.Serializable;

public class MyDirectGraphAnalysisGraph<V, E>
extends medousa.direct.graph.MyDirectGraph<V, E>
implements Serializable {

    public double MAX_EDGE_VALUE = 0.0D;

    public MyDirectGraphAnalysisGraph() { super(EdgeType.DIRECTED); }


}
