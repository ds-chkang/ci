package medousa.sequential.utils;

import medousa.MyProgressBar;
import medousa.sequential.graph.MyEdge;
import medousa.sequential.graph.MyNode;

import javax.swing.table.DefaultTableModel;
import java.util.*;

public class MyDepthNodeUtil {

    public static void setDepthNodeValue() {
        if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 0) {
            MyDepthNodeUtil.setContributionToDepthNodes();
        } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 1) {
            MyDepthNodeUtil.setInContributionToDepthNodes();
        } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 2) {
            MyDepthNodeUtil.setOutContributionToDepthNodes();
        } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 3) {
            MyDepthNodeUtil.setInOutContributionDifferenceToDepthNodes();
        } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 4) {
            MyDepthNodeUtil.setInNodeCountToDepthNodes();
        } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 5) {
            MyDepthNodeUtil.setOutNodeCountToDepthNodes();
        } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 6) {
            MyDepthNodeUtil.setInOutNodeCountDifferenceToDepthNodes();
        } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 7) {
            MyDepthNodeUtil.setDurationToDepthNodes();
        } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 8) {
            MyDepthNodeUtil.setReachTimeToDepthNodes();
        } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 9) {
            MyDepthNodeUtil.setMaxDurationToDepthNodes();
        } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 10) {
            MyDepthNodeUtil.setMinDurationToDepthNodes();
        } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 11) {
            MyDepthNodeUtil.setMaxReachTimeToDepthNodes();
        } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 12) {
            MyDepthNodeUtil.setMinReachTimeToDepthNodes();
        }


        MyEdgeUtil.setEdgesToZeroValues();
    }

    private static void setContributionToDepthNodes() {
        float maxVal = 0.00f;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getNodeDepthInfoMap().containsKey(MySequentialGraphVars.currentGraphDepth)) {
                n.setCurrentValue(n.getNodeDepthInfo(MySequentialGraphVars.currentGraphDepth).getContribution());
                if (n.getCurrentValue() > maxVal) {
                    maxVal = n.getCurrentValue();
                }
            } else {
                n.setCurrentValue(0.00f);
            }
        }
        MySequentialGraphVars.g.MX_N_VAL = maxVal;
    }

    private static void setOutContributionToDepthNodes() {
        MyProgressBar pb = new MyProgressBar(false);
        int pbCnt = 0;
        float maxVal = 0.00f;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getNodeDepthInfoMap().containsKey(MySequentialGraphVars.currentGraphDepth)) {
                n.setCurrentValue((float) n.getNodeDepthInfoMap().get(MySequentialGraphVars.currentGraphDepth).getOutContribution());
                if (n.getCurrentValue() > maxVal) {
                    maxVal = n.getCurrentValue();
                }
            } else {
                n.setCurrentValue(0.00f);
            }
            pb.updateValue(++pbCnt, nodes.size());
        }
        MySequentialGraphVars.g.MX_N_VAL = maxVal;
        pb.updateValue(100, 100);
        pb.dispose();
    }

    public static void setInContributionToDepthNodes() {
        float maxVal = 0.00f;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getNodeDepthInfoMap().containsKey(MySequentialGraphVars.currentGraphDepth)) {
                n.setCurrentValue(n.getNodeDepthInfo(MySequentialGraphVars.currentGraphDepth).getInContribution());
                if (n.getCurrentValue() > maxVal) {
                    maxVal = n.getCurrentValue();
                }
            } else {
                n.setCurrentValue(0.00f);
            }
        }
        MySequentialGraphVars.g.MX_N_VAL = maxVal;
    }

    public static void setInOutContributionDifferenceToDepthNodes() {
        float maxVal = 0.00f;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getNodeDepthInfoMap().containsKey(MySequentialGraphVars.currentGraphDepth)) {
                n.setCurrentValue(Math.abs(n.getNodeDepthInfo(MySequentialGraphVars.currentGraphDepth).getOutContribution()) - (n.getNodeDepthInfo(MySequentialGraphVars.currentGraphDepth).getInContribution()));
                if (n.getCurrentValue() > maxVal) {
                    maxVal = n.getCurrentValue();
                }
            } else {
                n.setCurrentValue(0.00f);
            }
        }
        MySequentialGraphVars.g.MX_N_VAL = maxVal;
    }

    public static void setInOutNodeDifferenceToDepthNodes() {
        float maxVal = 0.00f;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getNodeDepthInfo(MySequentialGraphVars.currentGraphDepth) != null) {
                n.setCurrentValue(Math.abs(n.getNodeDepthInfo(MySequentialGraphVars.currentGraphDepth).getPredecessorCount()) -
                        (n.getNodeDepthInfo(MySequentialGraphVars.currentGraphDepth).getSuccessorCount()));
                if (n.getCurrentValue() > maxVal) {
                    maxVal = n.getCurrentValue();
                }
            } else {
                n.setCurrentValue(0.00f);
            }
        }
        MySequentialGraphVars.g.MX_N_VAL = maxVal;
    }

    public static void setInNodeCountToDepthNodes() {
        float maxVal = 0.00f;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getNodeDepthInfoMap().containsKey(MySequentialGraphVars.currentGraphDepth)) {
                n.setCurrentValue(n.getNodeDepthInfo(MySequentialGraphVars.currentGraphDepth).getPredecessorCount());
                if (n.getCurrentValue() > maxVal) {
                    maxVal = n.getCurrentValue();
                }
            } else {
                n.setCurrentValue(0.00f);
            }
        }
        MySequentialGraphVars.g.MX_N_VAL = maxVal;
    }

    public static void setOutNodeCountToDepthNodes() {
        float maxVal = 0.00f;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getNodeDepthInfoMap().containsKey(MySequentialGraphVars.currentGraphDepth)) {
                n.setCurrentValue(n.getNodeDepthInfo(MySequentialGraphVars.currentGraphDepth).getSuccessorCount());
                if (n.getCurrentValue() > maxVal) {
                    maxVal = n.getCurrentValue();
                }
            } else {
                n.setCurrentValue(0.00f);
            }
        }
        MySequentialGraphVars.g.MX_N_VAL = maxVal;
    }

    public static void setInOutNodeCountDifferenceToDepthNodes() {
        float maxVal = 0.00f;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getNodeDepthInfoMap().containsKey(MySequentialGraphVars.currentGraphDepth)) {
                n.setCurrentValue(Math.abs(n.getNodeDepthInfo(MySequentialGraphVars.currentGraphDepth).getPredecessorCount()) - (n.getNodeDepthInfo(MySequentialGraphVars.currentGraphDepth).getSuccessorCount()));
                if (n.getCurrentValue() > maxVal) {
                    maxVal = n.getCurrentValue();
                }
            } else {
                n.setCurrentValue(0.00f);
            }
        }
        MySequentialGraphVars.g.MX_N_VAL = maxVal;
    }

    public static void setDurationToDepthNodes() {
        float maxVal = 0.00f;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getNodeDepthInfoMap().containsKey(MySequentialGraphVars.currentGraphDepth)) {
                n.setCurrentValue(n.getNodeDepthInfo(MySequentialGraphVars.currentGraphDepth).getDuration());
                if (n.getCurrentValue() > maxVal) {
                    maxVal = n.getCurrentValue();
                }
            } else {
                n.setCurrentValue(0.00f);
            }
        }
        MySequentialGraphVars.g.MX_N_VAL = maxVal;
    }

    public static void setReachTimeToDepthNodes() {
        float maxVal = 0.00f;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getNodeDepthInfoMap().containsKey(MySequentialGraphVars.currentGraphDepth)) {
                n.setCurrentValue(n.getNodeDepthInfo(MySequentialGraphVars.currentGraphDepth).getReachTime());
                if (n.getCurrentValue() > maxVal) {
                    maxVal = n.getCurrentValue();
                }
            } else {
                n.setCurrentValue(0.00f);
            }
        }
        MySequentialGraphVars.g.MX_N_VAL = maxVal;
    }

    public static void setMaxReachTimeToDepthNodes() {
        float maxVal = 0.00f;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getNodeDepthInfoMap().containsKey(MySequentialGraphVars.currentGraphDepth)) {
                n.setCurrentValue(n.getNodeDepthInfo(MySequentialGraphVars.currentGraphDepth).getMaxReachTime());
                if (n.getCurrentValue() > maxVal) {
                    maxVal = n.getCurrentValue();
                }
            } else {
                n.setCurrentValue(0.00f);
            }
        }
        MySequentialGraphVars.g.MX_N_VAL = maxVal;
    }

    public static void setMaxDurationToDepthNodes() {
        float maxVal = 0.00f;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getNodeDepthInfoMap().containsKey(MySequentialGraphVars.currentGraphDepth)) {
                n.setCurrentValue(n.getNodeDepthInfo(MySequentialGraphVars.currentGraphDepth).getMaxDuration());
                if (n.getCurrentValue() > maxVal) {
                    maxVal = n.getCurrentValue();
                }
            } else {
                n.setCurrentValue(0.00f);
            }
        }
        MySequentialGraphVars.g.MX_N_VAL = maxVal;
    }

    public static void setMinReachTimeToDepthNodes() {
        float maxVal = 0.00f;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getName().contains("x")) {n.setCurrentValue(0f);}
            else if (n.getNodeDepthInfoMap().containsKey(MySequentialGraphVars.currentGraphDepth)) {
                n.setCurrentValue(n.getNodeDepthInfo(MySequentialGraphVars.currentGraphDepth).getMinReachTime());
                if (n.getCurrentValue() > maxVal) {
                    maxVal = n.getCurrentValue();
                }
            } else {
                n.setCurrentValue(0.00f);
            }
        }
        MySequentialGraphVars.g.MX_N_VAL = maxVal;
    }

    public static void setMinDurationToDepthNodes() {
        float maxVal = 0.00f;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getName().contains("x") || MySequentialGraphVars.g.getSuccessors(n).size() == 0) {n.setCurrentValue(0f);}
            else if (n.getNodeDepthInfoMap().containsKey(MySequentialGraphVars.currentGraphDepth)) {
                n.setCurrentValue(n.getNodeDepthInfo(MySequentialGraphVars.currentGraphDepth).getMinDuration());
                if (n.getCurrentValue() > maxVal) {
                    maxVal = n.getCurrentValue();
                }
            } else {
                n.setCurrentValue(0.00f);
            }
        }
        MySequentialGraphVars.g.MX_N_VAL = maxVal;
    }

    private synchronized static void setDepthNodePredecessors() {
        MySequentialGraphVars.getSequentialGraphViewer().vc.depthNeighborSet = new HashSet<>();
        Map<String, Map<String, Integer>> predecessorMaps = new HashMap<>();
        for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
            if (MySequentialGraphVars.seqs[s].length < MySequentialGraphVars.currentGraphDepth) continue;
            String depthNode = MySequentialGraphVars.seqs[s][MySequentialGraphVars.currentGraphDepth-1].split(":")[0];
            if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeNameSet.contains(depthNode)) {
                String predecessor = MySequentialGraphVars.seqs[s][MySequentialGraphVars.currentGraphDepth-2].split(":")[0];
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
                MySequentialGraphVars.getSequentialGraphViewer().vc.depthNeighborSet.add(predecessor);
            }
        }

        float max = 0f;
        Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
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
            MySequentialGraphVars.g.MX_N_VAL = max;
        }
        MySequentialGraphVars.g.MX_E_VAL = 40f;
        MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodePredecessorMaps = predecessorMaps;
    }

    private synchronized static void setDepthNodeSuccessors() {
        MySequentialGraphVars.getSequentialGraphViewer().vc.depthNeighborSet = new HashSet<>();
        Map<String, Map<String, Integer>> successorMaps = new HashMap<>();
        for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
            if ((MySequentialGraphVars.seqs[s].length) <= MySequentialGraphVars.currentGraphDepth) continue;
            String depthNode = MySequentialGraphVars.seqs[s][MySequentialGraphVars.currentGraphDepth-1].split(":")[0];
            if (depthNode.contains("x")) {
                for (int i = MySequentialGraphVars.currentGraphDepth; i < MySequentialGraphVars.seqs[s].length; i++) {
                    String successor = MySequentialGraphVars.seqs[s][i].split(":")[0];
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
                    MySequentialGraphVars.getSequentialGraphViewer().vc.depthNeighborSet.add(successor);
                }
            } else {
                String successor = MySequentialGraphVars.seqs[s][MySequentialGraphVars.currentGraphDepth].split(":")[0];
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
                MySequentialGraphVars.getSequentialGraphViewer().vc.depthNeighborSet.add(successor);
            }
        }

        float max = 0f;
        Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
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
            MySequentialGraphVars.g.MX_N_VAL = max;
        }
        MySequentialGraphVars.g.MX_E_VAL = 40f;
        MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeSuccessorMaps = successorMaps;
    }

    public static void setSelectedSingleDepthNodeSuccessors() {
        Map<String, Map<String, Integer>> successorMaps = new HashMap<>();
        for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
            if ((MySequentialGraphVars.seqs[s].length) <= MySequentialGraphVars.currentGraphDepth) continue;
            String depthNode = MySequentialGraphVars.seqs[s][MySequentialGraphVars.currentGraphDepth-1].split(":")[0];
            if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeNameSet.contains(depthNode)) {
                if (depthNode.contains("x")) {
                    for (int i = MySequentialGraphVars.currentGraphDepth; i < MySequentialGraphVars.seqs[s].length; i++) {
                        String successor = MySequentialGraphVars.seqs[s][i].split(":")[0];
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
                    String successor = MySequentialGraphVars.seqs[s][MySequentialGraphVars.currentGraphDepth].split(":")[0];
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
        Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
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
            MySequentialGraphVars.g.MX_N_VAL = max;
        }
        MySequentialGraphVars.g.MX_E_VAL = 40f;
        MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeSuccessorMaps = successorMaps;
    }

    // fix this function.
    public static void setSelectedSingleDepthNodePredecessors() {
        Map<String, Map<String, Integer>> predecessorMaps = new HashMap<>();
        for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
            if ((MySequentialGraphVars.seqs[s].length) < MySequentialGraphVars.currentGraphDepth) continue;
            String depthNode = MySequentialGraphVars.seqs[s][MySequentialGraphVars.currentGraphDepth-1].split(":")[0];
            if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeNameSet.contains(depthNode)) {
                String predecessor = MySequentialGraphVars.seqs[s][MySequentialGraphVars.currentGraphDepth-2].split(":")[0];
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
        Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
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
            MySequentialGraphVars.g.MX_N_VAL = max;
        }
        MySequentialGraphVars.g.MX_E_VAL = 40f;
        MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodePredecessorMaps = predecessorMaps;
    }

    public static void setDepthNodeNeighbors() {
        MyViewerComponentControllerUtil.removeBarChartsFromViewer();
        MyViewerComponentControllerUtil.removeEdgeValueBarChartFromViewer();
        if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthNeighborNodeTypeSelector.getSelectedIndex() == 0) {
            MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeSuccessorMaps = null;
            MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodePredecessorMaps = null;
            Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
            for (MyNode n : nodes) {
                if (!MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeNameSet.contains(n.getName())) {
                    n.setCurrentValue(0f);
                }
            }
            Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
            for (MyEdge e : edges) {
                if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeNameSet.contains(e.getSource().getName()) &&
                    MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeNameSet.contains(e.getDest().getName())) {
                    e.setCurrentValue(0f);
                }
            }
        } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthNeighborNodeTypeSelector.getSelectedItem().toString().equals("S.")) {
            MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeSuccessorMaps = null;
            MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodePredecessorMaps = null;
                Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
                for (MyNode n : nodes) {
                    if (!MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeNameSet.contains(n.getName())) {
                        n.setCurrentValue(0f);
                    }
                }
                setDepthNodeSuccessors();
        } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthNeighborNodeTypeSelector.getSelectedItem().toString().equals("P.")) {
            MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeSuccessorMaps = null;
            MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodePredecessorMaps = null;
            Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
            for (MyNode n : nodes) {
                if (!MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeNameSet.contains(n.getName())) {
                    n.setCurrentValue(0f);
                }
            }
            setDepthNodePredecessors();
        }
    }

    public static void setSelectedSingleDepthNodeNeighbors() {
        MyViewerComponentControllerUtil.removeBarChartsFromViewer();
        MyViewerComponentControllerUtil.removeEdgeValueBarChartFromViewer();
        if (MySequentialGraphVars.getSequentialGraphViewer().vc.selectedNodeNeighborNodeTypeSelector.getSelectedIndex() == 0) {
            MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeSuccessorMaps = null;
            MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodePredecessorMaps = null;
            Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
            for (MyNode n : nodes) {
                if (!MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeNameSet.contains(n.getName())) {
                    n.setCurrentValue(0f);
                }
            }
            Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
            for (MyEdge e : edges) {
                if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeNameSet.contains(e.getSource().getName()) &&
                        MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeNameSet.contains(e.getDest().getName())) {
                    e.setCurrentValue(0f);
                }
            }
        } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.selectedNodeNeighborNodeTypeSelector.getSelectedItem().toString().equals("S.")) {
            MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeSuccessorMaps = null;
            MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodePredecessorMaps = null;
            Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
            for (MyNode n : nodes) {
                if (!MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeNameSet.contains(n.getName())) {
                    n.setCurrentValue(0f);
                }
            }
            setSelectedSingleDepthNodeSuccessors();
        } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.selectedNodeNeighborNodeTypeSelector.getSelectedItem().toString().equals("P.")) {
            MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeSuccessorMaps = null;
            MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodePredecessorMaps = null;
            Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
            for (MyNode n : nodes) {
                if (!MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeNameSet.contains(n.getName())) {
                    n.setCurrentValue(0f);
                }
            }
            setSelectedSingleDepthNodePredecessors();
        }
    }

    public static long getDepthNodeSuccessorValue(MyNode n) {
        long value = 0;
        if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 0) {
            value = n.getNodeDepthInfo(MySequentialGraphVars.currentGraphDepth +1).getContribution();
        } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 1) {
            value = n.getNodeDepthInfoMap().get(MySequentialGraphVars.currentGraphDepth + 1).getInContribution();
        } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 2) {
            value = n.getNodeDepthInfoMap().get(MySequentialGraphVars.currentGraphDepth + 1).getOutContribution();
        } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 3) {
            value = (n.getNodeDepthInfoMap().get(MySequentialGraphVars.currentGraphDepth + 1).getInContribution() - n.getNodeDepthInfoMap().get(MySequentialGraphVars.currentGraphDepth + 1).getOutContribution());
        } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 4) {
            value = n.getNodeDepthInfoMap().get(MySequentialGraphVars.currentGraphDepth + 1).getPredecessorCount();
        } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 5) {
            value = n.getNodeDepthInfoMap().get(MySequentialGraphVars.currentGraphDepth + 1).getSuccessorCount();
        } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 6) {
            value = n.getNodeDepthInfoMap().get(MySequentialGraphVars.currentGraphDepth + 1).getPredecessorCount() - n.getNodeDepthInfoMap().get(MySequentialGraphVars.currentGraphDepth + 1).getSuccessorCount();
        } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 7) {
            value = n.getNodeDepthInfoMap().get(MySequentialGraphVars.currentGraphDepth + 1).getDuration();
        } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 8) {
            value = n.getNodeDepthInfoMap().get(MySequentialGraphVars.currentGraphDepth + 1).getReachTime();
        }
        return value;
    }

    public static long getDepthNodePredecessorValue(MyNode n) {
        long value = 0;
        if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 0) {
            value = n.getNodeDepthInfo(MySequentialGraphVars.currentGraphDepth -1).getContribution();
        } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 1) {
            value = n.getNodeDepthInfoMap().get(MySequentialGraphVars.currentGraphDepth -1).getInContribution();
        } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 2) {
            value = n.getNodeDepthInfoMap().get(MySequentialGraphVars.currentGraphDepth -1).getOutContribution();
        } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 3) {
            value = (n.getNodeDepthInfoMap().get(MySequentialGraphVars.currentGraphDepth -1).getInContribution() - n.getNodeDepthInfoMap().get(MySequentialGraphVars.currentGraphDepth + 1).getOutContribution());
        } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 4) {
            value = n.getNodeDepthInfoMap().get(MySequentialGraphVars.currentGraphDepth -1).getPredecessorCount();
        } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 5) {
            value = n.getNodeDepthInfoMap().get(MySequentialGraphVars.currentGraphDepth -1).getSuccessorCount();
        } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 6) {
            value = n.getNodeDepthInfoMap().get(MySequentialGraphVars.currentGraphDepth -1).getPredecessorCount() - n.getNodeDepthInfoMap().get(MySequentialGraphVars.currentGraphDepth + 1).getSuccessorCount();
        } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 7) {
            value = n.getNodeDepthInfoMap().get(MySequentialGraphVars.currentGraphDepth -1).getDuration();
        } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 8) {
            value = n.getNodeDepthInfoMap().get(MySequentialGraphVars.currentGraphDepth -1).getReachTime();
        }

        return value;
    }

    public static void updateViewerTableInfosWithDepthNodes() {
        updateCurrentNodeListTableWithDepthNodes();
        updateNodeListTableWithDepthNodes();
    }

    private static void updateNodeListTableWithDepthNodes() {
        for (int i = MySequentialGraphVars.getSequentialGraphViewer().vc.nodeListTable.getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) MySequentialGraphVars.getSequentialGraphViewer().vc.nodeListTable.getModel()).removeRow(i);
        }

        if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeNameSet == null) {
            int i = 0;
            ((DefaultTableModel) MySequentialGraphVars.getSequentialGraphViewer().vc.nodeListTable.getModel()).addRow(
                    new String[]{"" + MyMathUtil.getCommaSeperatedNumber(++i),
                            MySequentialGraphSysUtil.getDecodedNodeName(MySequentialGraphSysUtil.getDecodedNodeName(MySequentialGraphVars.getSequentialGraphViewer().selectedNode.getName())),
                            MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(((MyNode) MySequentialGraphVars.g.vRefs.get(MySequentialGraphVars.getSequentialGraphViewer().selectedNode.getName())).getCurrentValue()))
                    });
        } else {
            int i = 0;
            for (String depthNode : MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeNameSet) {
                ((DefaultTableModel) MySequentialGraphVars.getSequentialGraphViewer().vc.nodeListTable.getModel()).addRow(
                        new String[]{"" + MyMathUtil.getCommaSeperatedNumber(++i),
                                MySequentialGraphSysUtil.getDecodedNodeName(depthNode),
                                MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(((MyNode) MySequentialGraphVars.g.vRefs.get(depthNode)).getCurrentValue()))
                        }
                );
            }
        }
    }

    private static void updateCurrentNodeListTableWithDepthNodes() {

    }
}