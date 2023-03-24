package datamining.graph.analysis;

import datamining.graph.MyDirectMarkovChain;
import edu.uci.ics.jung.graph.util.EdgeType;

import java.io.Serializable;

public class MyGraph<V, E>
extends MyDirectMarkovChain<V, E>
implements Serializable {

    public double MAX_EDGE_VALUE = 0.0D;

    public MyGraph() { super(EdgeType.DIRECTED); }


}
