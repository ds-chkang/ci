package datamining.utils;

import datamining.graph.MyEdge;
import datamining.utils.system.MyVars;

import java.util.Collection;

public class MyMultiNodeUtil {


    public static void adjustMultiNodeNeighborNodeValues() {
        Collection<MyEdge> edges = MyVars.g.getEdges();
        for (MyEdge e : edges) {
            if (!MyVars.getViewer().multiNodes.contains(e.getSource()) && !MyVars.getViewer().multiNodes.contains(e.getDest())) {
                e.setCurrentValue(0f);
            }
            if (!MyVars.getViewer().multiNodes.contains(e.getSource()) &&
                !MyVars.getViewer().multiNodePredecessors.contains(e.getSource()) && !MyVars.getViewer().multiNodeSuccessors.contains(e.getSource())) {
                e.getSource().setCurrentValue(0f);
            }
            if (!MyVars.getViewer().multiNodes.contains(e.getDest()) &&
                !MyVars.getViewer().multiNodePredecessors.contains(e.getDest()) && !MyVars.getViewer().multiNodeSuccessors.contains(e.getDest())) {
                e.getDest().setCurrentValue(0f);
            }
        }
    }

}
