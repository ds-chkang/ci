package datamining.utils;

import datamining.graph.MyEdge;
import datamining.graph.MyNode;
import datamining.utils.system.MySysUtil;
import datamining.utils.system.MyVars;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class MySelectedNodeUtil {

    public static double getMinimumInNodeValueFromSelectedNodePredecessors() {
        double minVal = 1000000000000D;
        Set<MyNode> ns = new HashSet<>();
        ns.addAll(MyVars.g.getPredecessors(MyVars.getViewer().selectedNode));
        for (MyNode n : ns) {if (minVal > n.getCurrentValue()) {minVal = n.getCurrentValue();}}
        if (minVal == 1000000000000L) {return 0;}
        return minVal;
    }

    public static double getMinimumNodeOutValueFromSelectedNodeSuccessors() {
        double minVal = 1000000000000D;
        Set<MyNode> ns = new HashSet<>();
        ns.addAll(MyVars.g.getSuccessors(MyVars.getViewer().selectedNode));
        for (MyNode n : ns) {if (minVal > n.getCurrentValue()) {minVal = n.getCurrentValue();}}
        if (minVal == 1000000000000L) {return 0;}
        return minVal;
    }

    public static String getSelectedNodeAvgInValue() {
        Collection<MyNode> ps = MyVars.g.getPredecessors(MyVars.getViewer().selectedNode);
        double totalValue = 0D;
        for (MyNode p : ps) {totalValue += p.getCurrentValue();}
        if (totalValue == 0) {return "0.00";}
        else {return MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(totalValue/ps.size()));}
    }
    public static String getSelectedNodeAvgOutValue() {
        Collection<MyNode> ss = MyVars.g.getSuccessors(MyVars.getViewer().selectedNode);
        double totalVal = 0D;
        for (MyNode s : ss) {totalVal += s.getCurrentValue();}
        if (totalVal == 0) {return "0.00";}
        else {return MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(totalVal/ss.size()));}
    }

    public static double getMaximumInNodeValueFromSelectedNodePredecessors() {
        double maxVal = 0D;
        Set<MyNode> ns = new HashSet<>();
        ns.addAll(MyVars.g.getPredecessors(MyVars.getViewer().selectedNode));
        for (MyNode n : ns) {if (maxVal < n.getCurrentValue()) {maxVal = n.getCurrentValue();}}
        return maxVal;
    }

    public static double getMaximumOutNodeValueFromSelectedNodeSuccessors() {
        double maxVal = 0D;
        Collection<MyEdge> edges = MyVars.g.getEdges();
        for (MyEdge e : edges) {
            if (e.getSource() == MyVars.getViewer().selectedNode) {
                if (maxVal < e.getCurrentValue()) {
                    maxVal = e.getCurrentValue();
                }
            }
        }
        return maxVal;
    }

    public static void adjustSelectedNodeNeighborNodeValues(MyNode selectedNode) {
        Collection<MyEdge> edges = MyVars.g.getEdges();
        for (MyEdge e : edges) {
            if (e.getSource() != selectedNode && e.getDest() != selectedNode) {
                e.setCurrentValue(0f);
            }
            if (MyVars.getViewer().selectedNode != e.getSource() &&
                    !MyVars.getViewer().selectedSingleNodePredecessors.contains(e.getSource()) && !MyVars.getViewer().selectedSingleNodeSuccessors.contains(e.getSource())) {
                e.getSource().setCurrentValue(0f);
            }
            if (MyVars.getViewer().selectedNode != e.getDest() &&
                    !MyVars.getViewer().selectedSingleNodePredecessors.contains(e.getDest()) && !MyVars.getViewer().selectedSingleNodeSuccessors.contains(e.getDest())) {
                e.getDest().setCurrentValue(0f);
            }
        }
    }

}
