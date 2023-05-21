package medousa.sequential.utils;

import medousa.sequential.graph.MyEdge;
import medousa.sequential.graph.MyNode;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class MySelectedNodeUtil {

    public static double getMinimumInNodeValueFromSelectedNodePredecessors() {
        double minVal = 1000000000000D;
        Set<MyNode> ns = new HashSet<>();
        ns.addAll(MySequentialGraphVars.g.getPredecessors(MySequentialGraphVars.getSequentialGraphViewer().selectedNode));
        for (MyNode n : ns) {if (minVal > n.getCurrentValue()) {minVal = n.getCurrentValue();}}
        if (minVal == 1000000000000L) {return 0;}
        return minVal;
    }

    public static double getMinimumNodeOutValueFromSelectedNodeSuccessors() {
        double minVal = 1000000000000D;
        Set<MyNode> ns = new HashSet<>();
        ns.addAll(MySequentialGraphVars.g.getSuccessors(MySequentialGraphVars.getSequentialGraphViewer().selectedNode));
        for (MyNode n : ns) {if (minVal > n.getCurrentValue()) {minVal = n.getCurrentValue();}}
        if (minVal == 1000000000000L) {return 0;}
        return minVal;
    }

    public static String getSelectedNodeAvgInValue() {
        Collection<MyNode> ps = MySequentialGraphVars.g.getPredecessors(MySequentialGraphVars.getSequentialGraphViewer().selectedNode);
        double totalValue = 0D;
        for (MyNode p : ps) {totalValue += p.getCurrentValue();}
        if (totalValue == 0) {return "0.00";}
        else {return MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(totalValue/ps.size()));}
    }
    public static String getSelectedNodeAvgOutValue() {
        Collection<MyNode> ss = MySequentialGraphVars.g.getSuccessors(MySequentialGraphVars.getSequentialGraphViewer().selectedNode);
        double totalVal = 0D;
        for (MyNode s : ss) {totalVal += s.getCurrentValue();}
        if (totalVal == 0) {return "0.00";}
        else {return MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(totalVal/ss.size()));}
    }

    public static double getMaximumInNodeValueFromSelectedNodePredecessors() {
        double maxVal = 0D;
        Set<MyNode> ns = new HashSet<>();
        ns.addAll(MySequentialGraphVars.g.getPredecessors(MySequentialGraphVars.getSequentialGraphViewer().selectedNode));
        for (MyNode n : ns) {if (maxVal < n.getCurrentValue()) {maxVal = n.getCurrentValue();}}
        return maxVal;
    }

    public static double getMaximumOutNodeValueFromSelectedNodeSuccessors() {
        double maxVal = 0D;
        Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
        for (MyEdge e : edges) {
            if (e.getSource() == MySequentialGraphVars.getSequentialGraphViewer().selectedNode) {
                if (maxVal < e.getCurrentValue()) {
                    maxVal = e.getCurrentValue();
                }
            }
        }
        return maxVal;
    }

    public static void adjustSelectedNodeNeighborNodeValues(MyNode selectedNode) {
        Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
        for (MyEdge e : edges) {
            if (e.getSource() != selectedNode && e.getDest() != selectedNode) {
                e.setCurrentValue(0f);
            }

            if (MySequentialGraphVars.getSequentialGraphViewer().selectedNode != e.getSource() &&
                !MySequentialGraphVars.getSequentialGraphViewer().selectedSingleNodePredecessors.contains(e.getSource()) && !MySequentialGraphVars.getSequentialGraphViewer().selectedSingleNodeSuccessors.contains(e.getSource())) {
                if (e.getSource().getCurrentValue() > 0) {
                    e.getSource().setOriginalValue(e.getSource().getCurrentValue());
                    e.getSource().setCurrentValue(0f);
                }
            }

            if (MySequentialGraphVars.getSequentialGraphViewer().selectedNode != e.getDest() &&
                !MySequentialGraphVars.getSequentialGraphViewer().selectedSingleNodePredecessors.contains(e.getDest()) && !MySequentialGraphVars.getSequentialGraphViewer().selectedSingleNodeSuccessors.contains(e.getDest())) {
                if (e.getDest().getCurrentValue() > 0) {
                    e.getDest().setOriginalValue(e.getDest().getCurrentValue());
                    e.getDest().setCurrentValue(0f);
                }
            }
        }
    }

}
