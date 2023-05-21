package medousa.sequential.graph;

import java.io.Serializable;

public class MyDepthEdge
extends MyEdge implements Serializable {

    public MyDepthEdge() {}
    public MyDepthEdge(MyDepthNode pn, MyDepthNode sn) {
        this.source = pn;
        this.dest = sn;
    }
}
