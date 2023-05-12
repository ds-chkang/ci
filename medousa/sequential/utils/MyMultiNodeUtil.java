package medousa.sequential.utils;

import medousa.sequential.graph.MyEdge;
import medousa.sequential.graph.MyNode;

import java.util.Collection;

public class MyMultiNodeUtil {


    public static void adjustMultiNodeNeighborNodeValues() {
        Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
        for (MyEdge e : edges) {
            if (!MySequentialGraphVars.getSequentialGraphViewer().multiNodes.contains(e.getSource()) && !MySequentialGraphVars.getSequentialGraphViewer().multiNodes.contains(e.getDest())) {
                e.setCurrentValue(0f);
            }
        }

        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0 &&
                (MySequentialGraphVars.getSequentialGraphViewer().multiNodes.contains(n) || MySequentialGraphVars.getSequentialGraphViewer().multiNodePredecessors.contains(n) || MySequentialGraphVars.getSequentialGraphViewer().multiNodeSuccessors.contains(n))) {
                n.setCurrentValue(n.getOriginalValue());
                n.setOriginalValue(0f);
            }
        }
    }
}
