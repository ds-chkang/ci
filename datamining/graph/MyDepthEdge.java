package datamining.graph;

public class MyDepthEdge
extends MyEdge {

    public MyDepthEdge() {}
    public MyDepthEdge(MyDepthNode pn, MyDepthNode sn) {
        this.source = pn;
        this.dest = sn;
    }
}
