package medousa.sequential.graph;

import java.io.Serializable;

public class MyPatternEdge
extends MyDepthEdge implements Serializable {

    public MyPatternEdge() {}
    public MyPatternEdge(MyDepthNode pn, MyDepthNode sn) {
        this.source = pn;
        this.dest = sn;
    }
}
