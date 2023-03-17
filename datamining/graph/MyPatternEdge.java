package datamining.graph;

public class MyPatternEdge
extends MyDepthEdge {

    public MyPatternEdge() {}
    public MyPatternEdge(MyDepthNode pn, MyDepthNode sn) {
        this.source = pn;
        this.dest = sn;
    }
}
