package datamining.utils;

import datamining.main.MyProgressBar;
import datamining.graph.MyEdge;
import datamining.graph.MyNode;
import datamining.utils.system.MySysUtil;
import datamining.utils.system.MyVars;
import org.apache.commons.collections15.Transformer;

import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;

public class MyDepthNodeUtil {

    public static void setDepthNodeValue() {
            if (MyVars.currentGraphDepth == 0) { // Depth is zero and set node values to default value, contribution.
                MyViewerControlComponentUtil.setDefaultLookToViewer();
            } else {// Depth option is selected.
                MyViewerControlComponentUtil.setNodeValueComboBoxMenu(); // Adjust node value options to depth, accordingly.
                if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() == 0) {
                    MyDepthNodeUtil.setContributionToDepthNodes();
                } else if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() == 1) {
                    MyDepthNodeUtil.setInContributionToDepthNodes();
                } else if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() == 2) {
                    MyDepthNodeUtil.setOutContributionToDepthNodes();
                } else if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() == 3) {
                    MyDepthNodeUtil.setInOutContributionDifferenceToDepthNodes();
                } else if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() == 4) {
                    MyDepthNodeUtil.setInNodeCountToDepthNodes();
                } else if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() == 5) {
                    MyDepthNodeUtil.setOutNodeCountToDepthNodes();
                } else if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() == 6) {
                    MyDepthNodeUtil.setInOutNodeDifferenceToDepthNodes();
                } else if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() == 7) {
                    MyDepthNodeUtil.setInOutNodeDifferenceToDepthNodes();
                } else if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() == 8) {
                    MyDepthNodeUtil.setDurationToDepthNodes();
                } else if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() == 9) {
                    MyDepthNodeUtil.setReachTimeToDepthNodes();
                }
                MyEdgeUtil.setEdgesToZeroValues();
            }
    }

    private static void setContributionToDepthNodes() {
        float maxVal = 0.00f;
        Collection<MyNode> nodes = MyVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getNodeDepthInfoMap().containsKey(MyVars.currentGraphDepth)) {
                n.setCurrentValue(n.getNodeDepthInfo(MyVars.currentGraphDepth).getContribution());
                if (n.getCurrentValue() > maxVal) {
                    maxVal = n.getCurrentValue();
                }
            } else {
                n.setCurrentValue(0.00f);
            }
        }
        MyVars.g.MX_N_VAL = maxVal;
    }

    private static void setOutContributionToDepthNodes() {
        MyProgressBar pb = new MyProgressBar(false);
        int pbCnt = 0;
        float maxVal = 0.00f;
        Collection<MyNode> nodes = MyVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getNodeDepthInfoMap().containsKey(MyVars.currentGraphDepth)) {
                n.setCurrentValue((float) n.getNodeDepthInfoMap().get(MyVars.currentGraphDepth).getOutContribution());
                if (n.getCurrentValue() > maxVal) {
                    maxVal = n.getCurrentValue();
                }
            } else {
                n.setCurrentValue(0.00f);
            }
            pb.updateValue(++pbCnt, nodes.size());
        }
        MyVars.g.MX_N_VAL = maxVal;
        pb.updateValue(100, 100);
        pb.dispose();
    }

    public static void setInContributionToDepthNodes() {
        float maxVal = 0.00f;
        Collection<MyNode> nodes = MyVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getNodeDepthInfoMap().containsKey(MyVars.currentGraphDepth)) {
                n.setCurrentValue(n.getNodeDepthInfo(MyVars.currentGraphDepth).getInContribution());
                if (n.getCurrentValue() > maxVal) {
                    maxVal = n.getCurrentValue();
                }
            } else {
                n.setCurrentValue(0.00f);
            }
        }
        MyVars.g.MX_N_VAL = maxVal;
    }

    public static void setInOutContributionDifferenceToDepthNodes() {
        float maxVal = 0.00f;
        Collection<MyNode> nodes = MyVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getNodeDepthInfoMap().containsKey(MyVars.currentGraphDepth)) {
                n.setCurrentValue(Math.abs(n.getNodeDepthInfo(MyVars.currentGraphDepth).getOutContribution()) - (n.getNodeDepthInfo(MyVars.currentGraphDepth).getInContribution()));
                if (n.getCurrentValue() > maxVal) {
                    maxVal = n.getCurrentValue();
                }
            } else {
                n.setCurrentValue(0.00f);
            }
        }
        MyVars.g.MX_N_VAL = maxVal;
    }

    public static void setInOutNodeDifferenceToDepthNodes() {
        float maxVal = 0.00f;
        Collection<MyNode> nodes = MyVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getNodeDepthInfo(MyVars.currentGraphDepth) != null) {
                n.setCurrentValue(Math.abs(n.getNodeDepthInfo(MyVars.currentGraphDepth).getPredecessorCount()) -
                        (n.getNodeDepthInfo(MyVars.currentGraphDepth).getSuccessorCount()));
                if (n.getCurrentValue() > maxVal) {
                    maxVal = n.getCurrentValue();
                }
            } else {
                n.setCurrentValue(0.00f);
            }
        }
        MyVars.g.MX_N_VAL = maxVal;
    }

    public static void setInNodeCountToDepthNodes() {
        float maxVal = 0.00f;
        Collection<MyNode> nodes = MyVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getNodeDepthInfoMap().containsKey(MyVars.currentGraphDepth)) {
                n.setCurrentValue(n.getNodeDepthInfo(MyVars.currentGraphDepth).getPredecessorCount());
                if (n.getCurrentValue() > maxVal) {
                    maxVal = n.getCurrentValue();
                }
            } else {
                n.setCurrentValue(0.00f);
            }
        }
        MyVars.g.MX_N_VAL = maxVal;
    }

    public static void setOutNodeCountToDepthNodes() {
        float maxVal = 0.00f;
        Collection<MyNode> nodes = MyVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getNodeDepthInfoMap().containsKey(MyVars.currentGraphDepth)) {
                n.setCurrentValue(n.getNodeDepthInfo(MyVars.currentGraphDepth).getSuccessorCount());
                if (n.getCurrentValue() > maxVal) {
                    maxVal = n.getCurrentValue();
                }
            } else {
                n.setCurrentValue(0.00f);
            }
        }
        MyVars.g.MX_N_VAL = maxVal;
    }

    public static void setInOutNodeCountDifferenceToDepthNodes() {
        float maxVal = 0.00f;
        Collection<MyNode> nodes = MyVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getNodeDepthInfoMap().containsKey(MyVars.currentGraphDepth)) {

                    n.setCurrentValue(Math.abs(n.getNodeDepthInfo(MyVars.currentGraphDepth).getPredecessorCount()) - (n.getNodeDepthInfo(MyVars.currentGraphDepth).getSuccessorCount()));

                if (n.getCurrentValue() > maxVal) {
                    maxVal = n.getCurrentValue();
                }
            } else {
                n.setCurrentValue(0.00f);
            }
        }
        MyVars.g.MX_N_VAL = maxVal;
    }

    public static void setDurationToDepthNodes() {
        float maxVal = 0.00f;
        Collection<MyNode> nodes = MyVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getNodeDepthInfoMap().containsKey(MyVars.currentGraphDepth)) {
                n.setCurrentValue(n.getNodeDepthInfo(MyVars.currentGraphDepth).getDuration());
                if (n.getCurrentValue() > maxVal) {
                    maxVal = n.getCurrentValue();
                }
            } else {
                n.setCurrentValue(0.00f);
            }
        }
        MyVars.g.MX_N_VAL = maxVal;
    }

    public static void setReachTimeToDepthNodes() {
        float maxVal = 0.00f;
        Collection<MyNode> nodes = MyVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getNodeDepthInfoMap().containsKey(MyVars.currentGraphDepth)) {
                synchronized (n) {
                    n.setCurrentValue(n.getNodeDepthInfo(MyVars.currentGraphDepth).getReachTime());
                }
                if (n.getCurrentValue() > maxVal) {
                    maxVal = n.getCurrentValue();
                }
            } else {
                n.setCurrentValue(0.00f);
            }
        }
        MyVars.g.MX_N_VAL = maxVal;
    }

    private synchronized static void setDepthNodePredecessors() {
        MyVars.getViewer().vc.depthNeighborSet = new HashSet<>();
        Map<String, Map<String, Integer>> predecessorMaps = new HashMap<>();
        for (int s=0; s < MyVars.seqs.length; s++) {
            if (MyVars.seqs[s].length < MyVars.currentGraphDepth) continue;
            String depthNode = MyVars.seqs[s][MyVars.currentGraphDepth-1].split(":")[0];
            if (MyVars.getViewer().vc.depthNodeNameSet.contains(depthNode)) {
                String predecessor = MyVars.seqs[s][MyVars.currentGraphDepth-2].split(":")[0];
                if (predecessorMaps.containsKey(depthNode)) {
                    Map<String, Integer> predecessorMap = predecessorMaps.get(depthNode);
                    if (predecessorMap.containsKey(predecessor)) {
                        predecessorMap.put(predecessor, predecessorMap.get(predecessor)+1);
                    } else {
                        predecessorMap.put(predecessor, 1);
                    }
                } else {
                    Map<String, Integer> predecessorMap = new HashMap<>();
                    predecessorMap.put(predecessor, 1);
                    predecessorMaps.put(depthNode, predecessorMap);
                }
                MyVars.getViewer().vc.depthNeighborSet.add(predecessor);
            }
        }

        float max = 0f;
        Collection<MyEdge> edges = MyVars.g.getEdges();
        for (MyEdge e : edges) {
            if (predecessorMaps.containsKey(e.getDest().getName())) {
                if (predecessorMaps.get(e.getDest().getName()).containsKey(e.getSource().getName())) {
                    e.getSource().setCurrentValue(predecessorMaps.get(e.getDest().getName()).get(e.getSource().getName()));
                    e.setCurrentValue(4.8f);
                }
                if (max < e.getSource().getCurrentValue()) {
                    max = e.getSource().getCurrentValue();
                }
            } else {
                e.setCurrentValue(0f);
            }
        }
        if (max > 0) {
            MyVars.g.MX_N_VAL = max;
        }
        MyVars.g.MX_E_VAL = 40f;
        MyVars.getViewer().vc.depthNodePredecessorMaps = predecessorMaps;
    }

    private synchronized static void setDepthNodeSuccessors() {
        MyVars.getViewer().vc.depthNeighborSet = new HashSet<>();
        Map<String, Map<String, Integer>> successorMaps = new HashMap<>();
        for (int s=0; s < MyVars.seqs.length; s++) {
            if ((MyVars.seqs[s].length) <= MyVars.currentGraphDepth) continue;
            String depthNode = MyVars.seqs[s][MyVars.currentGraphDepth-1].split(":")[0];
            if (depthNode.contains("x")) {
                for (int i = MyVars.currentGraphDepth; i < MyVars.seqs[s].length; i++) {
                    String successor = MyVars.seqs[s][i].split(":")[0];
                    if (successorMaps.containsKey(depthNode)) {
                        Map<String, Integer> successorMap = successorMaps.get(depthNode);
                        if (successorMap.containsKey(successor)) {
                            successorMap.put(successor, successorMap.get(successor)+1);
                        } else {
                            successorMap.put(successor, 1);
                        }
                    } else {
                        Map<String, Integer> successorMap = new HashMap<>();
                        successorMap.put(successor, 1);
                        successorMaps.put(depthNode, successorMap);
                    }
                    MyVars.getViewer().vc.depthNeighborSet.add(successor);
                }
            } else {
                String successor = MyVars.seqs[s][MyVars.currentGraphDepth].split(":")[0];
                if (successorMaps.containsKey(depthNode)) {
                    Map<String, Integer> successorMap = successorMaps.get(depthNode);
                    if (successorMap.containsKey(successor)) {
                        successorMap.put(successor, successorMap.get(successor)+1);
                    } else {
                        successorMap.put(successor, 1);
                    }
                } else {
                    Map<String, Integer> successorMap = new HashMap<>();
                    successorMap.put(successor, 1);
                    successorMaps.put(depthNode, successorMap);
                }
                MyVars.getViewer().vc.depthNeighborSet.add(successor);
            }
        }

        float max = 0f;
        Collection<MyEdge> edges = MyVars.g.getEdges();
        for (MyEdge e : edges) {
            if (successorMaps.containsKey(e.getSource().getName())) {
                if (successorMaps.get(e.getSource().getName()).containsKey(e.getDest().getName())) {
                    e.getDest().setCurrentValue(successorMaps.get(e.getSource().getName()).get(e.getDest().getName()));
                    e.setCurrentValue(4.8f);
                }
                if (max < e.getDest().getCurrentValue()) {
                    max = e.getDest().getCurrentValue();
                }
            } else {
                e.setCurrentValue(0f);
            }
        }

        if (max > 0) {
            MyVars.g.MX_N_VAL = max;
        }
        MyVars.g.MX_E_VAL = 40f;
        MyVars.getViewer().vc.depthNodeSuccessorMaps = successorMaps;
    }

    public static void setSelectedSingleDepthNodeSuccessors() {
        Map<String, Map<String, Integer>> successorMaps = new HashMap<>();
        for (int s=0; s < MyVars.seqs.length; s++) {
            if ((MyVars.seqs[s].length) <= MyVars.currentGraphDepth) continue;
            String depthNode = MyVars.seqs[s][MyVars.currentGraphDepth-1].split(":")[0];
            if (MyVars.getViewer().vc.depthNodeNameSet.contains(depthNode)) {
                if (depthNode.contains("x")) {
                    for (int i = MyVars.currentGraphDepth; i < MyVars.seqs[s].length; i++) {
                        String successor = MyVars.seqs[s][i].split(":")[0];
                        if (successorMaps.containsKey(depthNode)) {
                            Map<String, Integer> successorMap = successorMaps.get(depthNode);
                            if (successorMap.containsKey(successor)) {
                                successorMap.put(successor, successorMap.get(successor) + 1);
                            } else {
                                successorMap.put(successor, 1);
                            }
                        } else {
                            Map<String, Integer> successorMap = new HashMap<>();
                            successorMap.put(successor, 1);
                            successorMaps.put(depthNode, successorMap);
                        }
                    }
                } else {
                    String successor = MyVars.seqs[s][MyVars.currentGraphDepth].split(":")[0];
                    if (successorMaps.containsKey(depthNode)) {
                        Map<String, Integer> successorMap = successorMaps.get(depthNode);
                        if (successorMap.containsKey(successor)) {
                            successorMap.put(successor, successorMap.get(successor) + 1);
                        } else {
                            successorMap.put(successor, 1);
                        }
                    } else {
                        Map<String, Integer> successorMap = new HashMap<>();
                        successorMap.put(successor, 1);
                        successorMaps.put(depthNode, successorMap);
                    }
                }
            }
        }

        float max = 0f;
        Collection<MyEdge> edges = MyVars.g.getEdges();
        for (MyEdge e : edges) {
            if (successorMaps.containsKey(e.getSource().getName())) {
                if (successorMaps.get(e.getSource().getName()).containsKey(e.getDest().getName())) {
                    e.getDest().setCurrentValue(successorMaps.get(e.getSource().getName()).get(e.getDest().getName()));
                    e.setCurrentValue(4.8f);
                }
                if (max < e.getDest().getCurrentValue()) {
                    max = e.getDest().getCurrentValue();
                }
            } else {
                e.setCurrentValue(0f);
            }
        }

        if (max > 0) {
            MyVars.g.MX_N_VAL = max;
        }
        MyVars.g.MX_E_VAL = 40f;
        MyVars.getViewer().vc.depthNodeSuccessorMaps = successorMaps;
    }

    // fix this function.
    public static void setSelectedSingleDepthNodePredecessors() {
        Map<String, Map<String, Integer>> predecessorMaps = new HashMap<>();
        for (int s=0; s < MyVars.seqs.length; s++) {
            if ((MyVars.seqs[s].length) < MyVars.currentGraphDepth) continue;
            String depthNode = MyVars.seqs[s][MyVars.currentGraphDepth-1].split(":")[0];
            if (MyVars.getViewer().vc.depthNodeNameSet.contains(depthNode)) {
                String predecessor = MyVars.seqs[s][MyVars.currentGraphDepth-2].split(":")[0];
                if (predecessorMaps.containsKey(depthNode)) {
                    Map<String, Integer> predecessorMap = predecessorMaps.get(depthNode);
                    if (predecessorMap.containsKey(predecessor)) {
                        predecessorMap.put(predecessor, predecessorMap.get(predecessor)+1);
                    } else {
                        predecessorMap.put(predecessor, 1);
                    }
                } else {
                    Map<String, Integer> predecessorMap = new HashMap<>();
                    predecessorMap.put(predecessor, 1);
                    predecessorMaps.put(depthNode, predecessorMap);
                }
            }
        }

        float max = 0f;
        Collection<MyEdge> edges = MyVars.g.getEdges();
        for (MyEdge e : edges) {
            if (predecessorMaps.containsKey(e.getDest().getName())) {
                if (predecessorMaps.get(e.getDest().getName()).containsKey(e.getSource().getName())) {
                    e.getSource().setCurrentValue(predecessorMaps.get(e.getDest().getName()).get(e.getSource().getName()));
                    e.setCurrentValue(4.8f);
                }
                if (max < e.getSource().getCurrentValue()) {
                    max = e.getSource().getCurrentValue();
                }
            } else {
                e.setCurrentValue(0f);
            }
        }

        if (max > 0) {
            MyVars.g.MX_N_VAL = max;
        }
        MyVars.g.MX_E_VAL = 40f;
        MyVars.getViewer().vc.depthNodePredecessorMaps = predecessorMaps;
    }

    public static void setDepthNodeNeighbors() {
        MyViewerControlComponentUtil.removeBarChartsFromViewer();
        MyViewerControlComponentUtil.removeEdgeValueBarChartFromViewer();
        if (MyVars.getViewer().vc.depthNeighborNodeTypeSelector.getSelectedIndex() == 0) {
            MyVars.getViewer().vc.depthNodeSuccessorMaps = null;
            MyVars.getViewer().vc.depthNodePredecessorMaps = null;
            Collection<MyNode> nodes = MyVars.g.getVertices();
            for (MyNode n : nodes) {
                if (!MyVars.getViewer().vc.depthNodeNameSet.contains(n.getName())) {
                    n.setCurrentValue(0f);
                }
            }
            Collection<MyEdge> edges = MyVars.g.getEdges();
            for (MyEdge e : edges) {
                if (MyVars.getViewer().vc.depthNodeNameSet.contains(e.getSource().getName()) &&
                    MyVars.getViewer().vc.depthNodeNameSet.contains(e.getDest().getName())) {
                    e.setCurrentValue(0f);
                }
            }
        } else if (MyVars.getViewer().vc.depthNeighborNodeTypeSelector.getSelectedItem().toString().equals("S.")) {
            MyVars.getViewer().vc.depthNodeSuccessorMaps = null;
            MyVars.getViewer().vc.depthNodePredecessorMaps = null;
                Collection<MyNode> nodes = MyVars.g.getVertices();
                for (MyNode n : nodes) {
                    if (!MyVars.getViewer().vc.depthNodeNameSet.contains(n.getName())) {
                        n.setCurrentValue(0f);
                    }
                }
                setDepthNodeSuccessors();

        } else if (MyVars.getViewer().vc.depthNeighborNodeTypeSelector.getSelectedItem().toString().equals("P.")) {
            MyVars.getViewer().vc.depthNodeSuccessorMaps = null;
            MyVars.getViewer().vc.depthNodePredecessorMaps = null;
            Collection<MyNode> nodes = MyVars.g.getVertices();
            for (MyNode n : nodes) {
                if (!MyVars.getViewer().vc.depthNodeNameSet.contains(n.getName())) {
                    n.setCurrentValue(0f);
                }
            }
            setDepthNodePredecessors();
        }
    }

    public static void setSelectedSingleDepthNodeNeighbors() {
        MyViewerControlComponentUtil.removeBarChartsFromViewer();
        MyViewerControlComponentUtil.removeEdgeValueBarChartFromViewer();
        if (MyVars.getViewer().vc.selectedNodeNeighborNodeTypeSelector.getSelectedIndex() == 0) {
            MyVars.getViewer().vc.depthNodeSuccessorMaps = null;
            MyVars.getViewer().vc.depthNodePredecessorMaps = null;
            Collection<MyNode> nodes = MyVars.g.getVertices();
            for (MyNode n : nodes) {
                if (!MyVars.getViewer().vc.depthNodeNameSet.contains(n.getName())) {
                    n.setCurrentValue(0f);
                }
            }
            Collection<MyEdge> edges = MyVars.g.getEdges();
            for (MyEdge e : edges) {
                if (MyVars.getViewer().vc.depthNodeNameSet.contains(e.getSource().getName()) &&
                        MyVars.getViewer().vc.depthNodeNameSet.contains(e.getDest().getName())) {
                    e.setCurrentValue(0f);
                }
            }
        } else if (MyVars.getViewer().vc.selectedNodeNeighborNodeTypeSelector.getSelectedItem().toString().equals("S.")) {
            MyVars.getViewer().vc.depthNodeSuccessorMaps = null;
            MyVars.getViewer().vc.depthNodePredecessorMaps = null;
            Collection<MyNode> nodes = MyVars.g.getVertices();
            for (MyNode n : nodes) {
                if (!MyVars.getViewer().vc.depthNodeNameSet.contains(n.getName())) {
                    n.setCurrentValue(0f);
                }
            }
            setSelectedSingleDepthNodeSuccessors();
        } else if (MyVars.getViewer().vc.selectedNodeNeighborNodeTypeSelector.getSelectedItem().toString().equals("P.")) {
            MyVars.getViewer().vc.depthNodeSuccessorMaps = null;
            MyVars.getViewer().vc.depthNodePredecessorMaps = null;
            Collection<MyNode> nodes = MyVars.g.getVertices();
            for (MyNode n : nodes) {
                if (!MyVars.getViewer().vc.depthNodeNameSet.contains(n.getName())) {
                    n.setCurrentValue(0f);
                }
            }
            setSelectedSingleDepthNodePredecessors();
        }
    }

    public static long getDepthNodeSuccessorValue(MyNode n) {
        long value = 0;
        if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() == 0) {
            value = n.getNodeDepthInfo(MyVars.currentGraphDepth +1).getContribution();
        } else if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() == 1) {
            value = n.getNodeDepthInfoMap().get(MyVars.currentGraphDepth + 1).getInContribution();
        } else if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() == 2) {
            value = n.getNodeDepthInfoMap().get(MyVars.currentGraphDepth + 1).getOutContribution();
        } else if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() == 3) {
            value = (n.getNodeDepthInfoMap().get(MyVars.currentGraphDepth + 1).getInContribution() - n.getNodeDepthInfoMap().get(MyVars.currentGraphDepth + 1).getOutContribution());
        } else if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() == 4) {
            value = n.getNodeDepthInfoMap().get(MyVars.currentGraphDepth + 1).getPredecessorCount();
        } else if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() == 5) {
            value = n.getNodeDepthInfoMap().get(MyVars.currentGraphDepth + 1).getSuccessorCount();
        } else if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() == 6) {
            value = n.getNodeDepthInfoMap().get(MyVars.currentGraphDepth + 1).getPredecessorCount() - n.getNodeDepthInfoMap().get(MyVars.currentGraphDepth + 1).getSuccessorCount();
        } else if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() == 7) {
            value = n.getNodeDepthInfoMap().get(MyVars.currentGraphDepth + 1).getDuration();
        } else if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() == 8) {
            value = n.getNodeDepthInfoMap().get(MyVars.currentGraphDepth + 1).getReachTime();
        }
        return value;
    }

    public static long getDepthNodePredecessorValue(MyNode n) {
        long value = 0;
        if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() == 0) {
            value = n.getNodeDepthInfo(MyVars.currentGraphDepth -1).getContribution();
        } else if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() == 1) {
            value = n.getNodeDepthInfoMap().get(MyVars.currentGraphDepth -1).getInContribution();
        } else if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() == 2) {
            value = n.getNodeDepthInfoMap().get(MyVars.currentGraphDepth -1).getOutContribution();
        } else if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() == 3) {
            value = (n.getNodeDepthInfoMap().get(MyVars.currentGraphDepth -1).getInContribution() - n.getNodeDepthInfoMap().get(MyVars.currentGraphDepth + 1).getOutContribution());
        } else if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() == 4) {
            value = n.getNodeDepthInfoMap().get(MyVars.currentGraphDepth -1).getPredecessorCount();
        } else if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() == 5) {
            value = n.getNodeDepthInfoMap().get(MyVars.currentGraphDepth -1).getSuccessorCount();
        } else if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() == 6) {
            value = n.getNodeDepthInfoMap().get(MyVars.currentGraphDepth -1).getPredecessorCount() - n.getNodeDepthInfoMap().get(MyVars.currentGraphDepth + 1).getSuccessorCount();
        } else if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() == 7) {
            value = n.getNodeDepthInfoMap().get(MyVars.currentGraphDepth -1).getDuration();
        } else if (MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex() == 8) {
            value = n.getNodeDepthInfoMap().get(MyVars.currentGraphDepth -1).getReachTime();
        }

        return value;
    }

    public static void updateViewerTableInfosWithDepthNodes() {
        updateCurrentNodeListTableWithDepthNodes();
        updateNodeListTableWithDepthNodes();
    }

    private static void updateNodeListTableWithDepthNodes() {
        for (int i = MyVars.getViewer().vc.nodeListTable.getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) MyVars.getViewer().vc.nodeListTable.getModel()).removeRow(i);
        }

        if (MyVars.getViewer().vc.depthNodeNameSet == null) {
            int i = 0;
            ((DefaultTableModel) MyVars.getViewer().vc.nodeListTable.getModel()).addRow(
                    new String[]{"" + MyMathUtil.getCommaSeperatedNumber(++i),
                            MySysUtil.getDecodedNodeName(MySysUtil.getDecodedNodeName(MyVars.getViewer().selectedNode.getName())),
                            MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(((MyNode) MyVars.g.vRefs.get(MyVars.getViewer().selectedNode.getName())).getCurrentValue()))
                    });
        } else {
            int i = 0;
            for (String depthNode : MyVars.getViewer().vc.depthNodeNameSet) {
                ((DefaultTableModel) MyVars.getViewer().vc.nodeListTable.getModel()).addRow(
                        new String[]{"" + MyMathUtil.getCommaSeperatedNumber(++i),
                                MySysUtil.getDecodedNodeName(depthNode),
                                MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(((MyNode) MyVars.g.vRefs.get(depthNode)).getCurrentValue()))
                        }
                );
            }
        }
    }

    private static void updateCurrentNodeListTableWithDepthNodes() {

    }
}