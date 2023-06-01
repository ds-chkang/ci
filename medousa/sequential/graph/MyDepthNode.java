package medousa.sequential.graph;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.io.Serializable;

public class MyDepthNode
extends MyNode
implements Serializable {

    public int relativeDepth;
    public int physicalDepth;
    public Point2D nodeLocation;
    public Ellipse2D.Double nodeSize;
    public boolean isSelectedNodePath;

    public MyDepthNode() {}

    public MyDepthNode(String name, int relativeDepth) {
        this.name = name;
        this.relativeDepth = relativeDepth +1;
    }

    public int getRelativeDepth() {
        return relativeDepth;
    }

    public void setRelativeDepth(int relativeDepth) {
        this.relativeDepth = relativeDepth;
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
