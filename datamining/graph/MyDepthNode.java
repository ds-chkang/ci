package datamining.graph;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public class MyDepthNode
extends MyNode {

    public int depth;
    public Point2D nodeLocation;
    public Ellipse2D.Double nodeSize;

    public MyDepthNode() {}

    public MyDepthNode(String name, int depth) {
        this.name = name;
        this.depth = depth+1;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public Point2D getNodeLocation() {
        return nodeLocation;
    }

    public void setNodeLocation(Point2D nodeLocation) {
        this.nodeLocation = nodeLocation;
    }

    public Ellipse2D.Double getNodeSize() {
        return nodeSize;
    }

    public void setNodeSize(Ellipse2D.Double nodeSize) {
        this.nodeSize = nodeSize;
    }

    public long getContribution() {
        return contribution;
    }

    public void setContribution(long contribution) {
        this.contribution = contribution;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
