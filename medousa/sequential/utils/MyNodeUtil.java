package medousa.sequential.utils;

import medousa.sequential.graph.MyClusteringConfig;
import medousa.sequential.graph.MyEdge;
import medousa.sequential.graph.MyNode;

import java.util.*;

public class MyNodeUtil {

    public static void setUserDefinedNodeValuesToNodes() {
        float maxVal = 0.00f;
        ArrayList<MyNode> nodes = new ArrayList<>(MySequentialGraphVars.g.getVertices());
        for (MyNode n : nodes) {
            n.setCurrentValue(n.nodeNumericValueMap.get(MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedItem().toString()));
            if (n.getCurrentValue() > maxVal) {maxVal = n.getCurrentValue();}
        }

        if (maxVal > 0) {
            MySequentialGraphVars.g.MX_N_VAL = maxVal;
        }
        MySequentialGraphVars.getSequentialGraphViewer().nodeValueName =
            MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedItem().toString();
    }

    public static void setDefaultValuesToNodes() {
        MySequentialGraphVars.getSequentialGraphViewer().nodeValueName = "DEFAULT";
        float maxVal = 0.00f;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            n.setCurrentValue((float) n.getContribution());
            if (maxVal < n.getCurrentValue()) {
                maxVal = n.getCurrentValue();
            }
        }

        if (maxVal > 0) {
            MySequentialGraphVars.g.MX_N_VAL = maxVal;
        }
    }

    public static void setClusteringDefaultValuesToNodes() {
        MySequentialGraphVars.getSequentialGraphViewer().nodeValueName = "CONTRIBUTION";
        float maxVal = 0.00f;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (MyClusteringConfig.selectedClusterColor != null && MyClusteringConfig.selectedClusterColor == n.clusteringColor) {
                n.setCurrentValue((float) n.getContribution());
                if (maxVal < n.getCurrentValue()) {
                    maxVal = n.getCurrentValue();
                }
            }
        }

        if (maxVal > 0) {
            MySequentialGraphVars.g.MX_N_VAL = maxVal;
        }
    }

    public static void setInOutNodeDifferenceToNodes() {
        MySequentialGraphVars.getSequentialGraphViewer().nodeValueName = "INOUT-NODE-DIFFERENCE";
        float maxVal = 0.00f;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            n.setCurrentValue(Math.abs(n.getPredecessorCount() - n.getSuccessorCount()));
            if (maxVal < n.getCurrentValue()) {maxVal = n.getCurrentValue();}
        }

        if (maxVal > 0) {
            MySequentialGraphVars.g.MX_N_VAL = maxVal;
        }
    }

    public static void setUniqueInNodeCountToNodes() {
        MySequentialGraphVars.getSequentialGraphViewer().nodeValueName = "UNIQUE-IN-COUNT";
        float maxVal = 0L;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            n.setCurrentValue(n.getPredecessorCount());
            if (maxVal < n.getCurrentValue()) {maxVal = n.getCurrentValue();}
        }

        if (maxVal > 0) {
            MySequentialGraphVars.g.MX_N_VAL = maxVal;
        }
    }

    public static void setDurationToNodes() {
        MySequentialGraphVars.getSequentialGraphViewer().nodeValueName = "DURATION";
        float maxVal = 0.00f;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            float totalReachTime = 0f;
            for (Integer depth : n.getNodeDepthInfoMap().keySet()) {
                totalReachTime += (float)n.getNodeDepthInfoMap().get(depth).getReachTime();
            }
            n.setCurrentValue(totalReachTime);
            if (maxVal < n.getCurrentValue()) {maxVal = n.getCurrentValue();}
        }

        if (maxVal > 0) {
            MySequentialGraphVars.g.MX_N_VAL = maxVal;
        }
    }

    public static void setEigenVectorCentralityToNodes() {
        MySequentialGraphVars.getSequentialGraphViewer().nodeValueName = "EIGENVECTOR-CENTRALITY";
        float maxVal = 0.00f;
        ArrayList<MyNode> nodes = new ArrayList<>(MySequentialGraphVars.g.getVertices());
        for (MyNode n : nodes) {
            n.setCurrentValue((float) n.getEignevector());
            if (n.getCurrentValue() > maxVal) {maxVal = n.getCurrentValue();}
        }

        if (maxVal > 0) {
            MySequentialGraphVars.g.MX_N_VAL = maxVal;
        }
    }

    public static void setBetweenessCentralityToNodes() {
        MySequentialGraphVars.getSequentialGraphViewer().nodeValueName = "BETWEENESS-CENTRALITY";
        float maxVal = 0.00f;
        ArrayList<MyNode> nodes = new ArrayList<>(MySequentialGraphVars.g.getVertices());
        for (MyNode n : nodes) {
            n.setCurrentValue((float) n.getBetweeness());
            if (n.getCurrentValue() > maxVal) {maxVal = n.getCurrentValue();}
        }
        MySequentialGraphVars.g.MX_N_VAL = maxVal;
    }

    public static void setClosenessCentralityToNodes() {
        MySequentialGraphVars.getSequentialGraphViewer().nodeValueName = "CLOSENESS-CENTRALITY";
        float maxVal = 0.00f;
        ArrayList<MyNode> nodes = new ArrayList<>(MySequentialGraphVars.g.getVertices());
        for (MyNode n : nodes) {
            n.setCurrentValue((float) n.getCloseness());
            if (n.getCurrentValue() > maxVal) {maxVal = n.getCurrentValue();}
        }
        MySequentialGraphVars.g.MX_N_VAL = maxVal;
    }

    public static void setRecurrenceCountToNodes() {
        MySequentialGraphVars.getSequentialGraphViewer().nodeValueName = "RECURRENCE-COUNT";
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        float maxVal = 0.00f;
        for (MyNode n : nodes) {
            n.setCurrentValue(n.getTotalNodeRecurrentCount());
            if (maxVal < n.getCurrentValue()) {maxVal = n.getCurrentValue();}
        }
        if (maxVal > 0) {
            MySequentialGraphVars.g.MX_N_VAL = maxVal;
        }    }

    public static void setTotalRecurrenceTimeToNodes() {
        MySequentialGraphVars.getSequentialGraphViewer().nodeValueName = "TOTAL-RECURRENCE-TIME";
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        float maxVal = 0.00f;
        for (MyNode n : nodes) {
            n.setCurrentValue(n.getTotalNodeRecurrenceTime());
            if (maxVal < n.getCurrentValue()) {
                maxVal = n.getCurrentValue();
            }
        }
        if (maxVal > 0) {
            MySequentialGraphVars.g.MX_N_VAL = maxVal;
        }
    }

    public static void setAverageReachTimeToNodes() {
        MySequentialGraphVars.getSequentialGraphViewer().nodeValueName = "AVG-REACH-TIME";
        float maxVal = 0.00f;
        Collection<MyNode> ns = MySequentialGraphVars.g.getVertices();
        for (MyNode n : ns) {
            float totalReachTime = 0.00f;
            for (Integer depth : n.getNodeDepthInfoMap().keySet()) {
                totalReachTime += (float)n.getNodeDepthInfoMap().get(depth).getReachTime();
            }
            n.setCurrentValue(totalReachTime/n.getNodeDepthInfoMap().size());
            if (maxVal < n.getCurrentValue()) {maxVal = n.getCurrentValue();}
        }
        if (maxVal > 0) {
            MySequentialGraphVars.g.MX_N_VAL = maxVal;
        }
    }
    public static void setEndNodeCountToNodes() {
        MySequentialGraphVars.getSequentialGraphViewer().nodeValueName = "END-POSITION-COUNT";
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        float maxVal = 0.00f;
        for (MyNode n : nodes) {
            n.setCurrentValue(n.getEndPositionNodeCount());
            if (maxVal < n.getCurrentValue()) {maxVal = n.getCurrentValue();}
        }
        if (maxVal > 0) {
            MySequentialGraphVars.g.MX_N_VAL = maxVal;
        }
    }

    public static void setStartNodeCountToNodes() {
        MySequentialGraphVars.getSequentialGraphViewer().nodeValueName = "START-POSITION-COUNT";
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        float maxVal = 0.00f;
        for (MyNode n : nodes) {
            n.setCurrentValue(n.getStartPositionNodeCount());
            if (maxVal < n.getCurrentValue()) {
                maxVal = n.getCurrentValue();
            }
        }
        if (maxVal > 0) {
            MySequentialGraphVars.g.MX_N_VAL = maxVal;
        }
    }

    public static void setUniqueContributionToNodes() {
        MySequentialGraphVars.getSequentialGraphViewer().nodeValueName = "UNIQUE-CONTRIBUTION";
        float maxVal = 0.00f;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            n.setCurrentValue(n.getUniqueContribution());
            if (maxVal < n.getCurrentValue()) {maxVal = n.getCurrentValue();}
        }
        if (maxVal > 0) {
            MySequentialGraphVars.g.MX_N_VAL = maxVal;
        }
    }

    public static void setUniqueOutNodeCountToNodes() {
        MySequentialGraphVars.getSequentialGraphViewer().nodeValueName = "UNIQUE-OUTCOUNT";
        float maxVal = 0.00f;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            n.setCurrentValue(n.getSuccessorCount());
            if (maxVal < n.getCurrentValue()) {maxVal = n.getCurrentValue();}
        }

        if (maxVal > 0) {
            MySequentialGraphVars.g.MX_N_VAL = maxVal;
        }
    }

    public static void setContributionToNodes() {
        MySequentialGraphVars.getSequentialGraphViewer().nodeValueName = "CONTRIBUTION";
        float maxVal = 0.00f;

        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            n.setCurrentValue((float) n.getContribution());
            if (maxVal < n.getCurrentValue()) {
                maxVal = n.getCurrentValue();
            }
        }

        if (maxVal > 0) {
            MySequentialGraphVars.g.MX_N_VAL = maxVal;
        }
    }

    public static void setInContributionToNodes() {
        MySequentialGraphVars.getSequentialGraphViewer().nodeValueName = "IN-CONTRIBUTION";
        float maxVal = 0.00f;

        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getInContribution() == 0) {
                n.setCurrentValue(0f);
            } else if (n.getCurrentValue() == 0) {
                n.setCurrentValue(0f);
            } else {
                n.setCurrentValue(n.getInContribution());
                if (maxVal < n.getCurrentValue()) {
                       maxVal = n.getInContribution();
                }
            }
        }

        if (maxVal > 0) {
            MySequentialGraphVars.g.MX_N_VAL = maxVal;
        }
    }

    public static void setAverageInContributionToNodes() {
        MySequentialGraphVars.getSequentialGraphViewer().nodeValueName = "AVERAGE-IN-CONTRIBUTION";
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        float maxVal = 0.00f;

        for (MyNode n : nodes) {
            n.setCurrentValue(n.getAverageInContribution());
            if (maxVal < n.getCurrentValue()) {
                maxVal = n.getAverageInContribution();
            }
        }

        if (maxVal > 0) {
            MySequentialGraphVars.g.MX_N_VAL = maxVal;
        }
    }

    public static void setOutContributionToNodes() {
        MySequentialGraphVars.getSequentialGraphViewer().nodeValueName = "OUT-CONTRIBUTION";
        float maxVal = 0.00f;

        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getOutContribution() == 0) {
                    n.setCurrentValue(0f);
            } else if (n.getCurrentValue() == 0) {
                    n.setCurrentValue(0f);
            } else {
                n.setCurrentValue(n.getOutContribution());
            }

            if (maxVal < n.getCurrentValue()) {
                maxVal = n.getCurrentValue();
            }
        }

        if (maxVal > 0) {
            MySequentialGraphVars.g.MX_N_VAL = maxVal;
        }
    }

    public static void setPageRankScoresToNodes() {
        MySequentialGraphVars.getSequentialGraphViewer().nodeValueName = "PAGERANK";
        float maxVal = 0.00f;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            n.setCurrentValue((float) n.getPageRankScore());
            if (maxVal < n.getCurrentValue()) {
                maxVal = n.getCurrentValue();
            }
        }

        if (maxVal > 0) {
            MySequentialGraphVars.g.MX_N_VAL = maxVal;
        }
    }

    public static void setAverageOutContributionToNodes() {
        MySequentialGraphVars.getSequentialGraphViewer().nodeValueName = "AVG.-OUT-CONTRIBUTION";
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        float maxVal = 0.00f;
        for (MyNode n : nodes) {
            n.setCurrentValue(n.getAverageOutContribution());
            if (maxVal < n.getCurrentValue()) {maxVal = n.getAverageOutContribution();}
        }

        if (maxVal > 0) {
            MySequentialGraphVars.g.MX_N_VAL = maxVal;
        }
    }

    public static void setTotalTimeToNodes() {
        MySequentialGraphVars.getSequentialGraphViewer().nodeValueName = "TOTAL-REACH-TIME";
        float maxVal = 0.00f;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            n.setCurrentValue(n.getTotalReachTime());
            if (n.getCurrentValue() > maxVal) {
                maxVal = n.getCurrentValue();
            }
        }

        if (maxVal > 0) {
            MySequentialGraphVars.g.MX_N_VAL = maxVal;
        }
    }

    public static void setItemsetLengthToNodes() {
        MySequentialGraphVars.getSequentialGraphViewer().nodeValueName = "ITEMSET-LENGTH";
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        float maxVal = 0.00f;
        for (MyNode n : nodes) {
            n.setCurrentValue(n.getName().split(",").length);
            if (maxVal < n.getCurrentValue()) {maxVal = n.getCurrentValue();}
        }

        if (maxVal > 0) {
            MySequentialGraphVars.g.MX_N_VAL = maxVal;
        }
    }

    public static void setMaxReachTimeToNodes() {
        MySequentialGraphVars.getSequentialGraphViewer().nodeValueName = "MAX-REACH-TIME";
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        float maxVal = 0.00f;
        for (MyNode n : nodes) {
            n.setCurrentValue(n.getMaxReachTime());
            if (maxVal < n.getCurrentValue()) {maxVal = n.getCurrentValue();}
        }

        if (maxVal > 0) {
            MySequentialGraphVars.g.MX_N_VAL = maxVal;
        }
    }

    public static void setAverageShortestDistanceToNodes() {
        MySequentialGraphVars.getSequentialGraphViewer().nodeValueName = "AVG-SHORTEST-DISTANCE";
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        float maxVal = 0.00f;
        for (MyNode n : nodes) {
            n.setCurrentValue(n.getAverageShortestDistance());
            if (maxVal < n.getCurrentValue()) {maxVal = n.getCurrentValue();}
        }

        if (maxVal > 0) {
            MySequentialGraphVars.g.MX_N_VAL = maxVal;
        }
    }

    public static void setUnReachableNodeCountToNodes() {
        MySequentialGraphVars.getSequentialGraphViewer().nodeValueName = "UNREACHABLE-NODE-COUNT";
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        float maxVal = 0.00f;
        for (MyNode n : nodes) {
            n.setCurrentValue(n.getMaxReachTime());
            if (maxVal < n.getCurrentValue()) {
                maxVal = n.getUnreachableNodeCount();
            }
        }

        if (maxVal > 0) {
            MySequentialGraphVars.g.MX_N_VAL = maxVal;
        }
    }



    public static void setMinReachTimeToNodes() {
        MySequentialGraphVars.getSequentialGraphViewer().nodeValueName = "MIN-REACH-TIME";
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        float maxVal = 0.00f;
        for (MyNode n : nodes) {
            if (n.getName().contains("x")) {
                n.setCurrentValue(0f);
            } else {
                n.setCurrentValue(n.getMinReachTime());
                if (maxVal < n.getCurrentValue()) {
                    maxVal = n.getCurrentValue();
                }
            }
        }

        if (maxVal > 0) {
            MySequentialGraphVars.g.MX_N_VAL = maxVal;
        }
    }

    public static void setMaxDurationToNodes() {
        MySequentialGraphVars.getSequentialGraphViewer().nodeValueName = "MAX-DURATION";
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        float maxVal = 0.00f;
        for (MyNode n : nodes) {
            if (n.getName().contains("x")) {
                n.setCurrentValue(0f);
            } else {
                n.setCurrentValue(n.getMaxDuration());
                if (maxVal < n.getCurrentValue()) {
                    maxVal = n.getCurrentValue();
                }
            }
        }

        if (maxVal > 0) {
            MySequentialGraphVars.g.MX_N_VAL = maxVal;
        }
    }

    public static void setTotalRecursiveLengthToNodes() {
        MySequentialGraphVars.getSequentialGraphViewer().nodeValueName = "TOTAL-RECURSIVE-LENGTH";
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        float maxVal = 0.00f;
        for (MyNode n : nodes) {
            n.setCurrentValue(n.getTotalRecursiveLength());
            if (maxVal < n.getCurrentValue()) {
                maxVal = n.getCurrentValue();
            }
        }

        if (maxVal > 0) {
            MySequentialGraphVars.g.MX_N_VAL = maxVal;
        }
    }

    public static void setMinRecursiveLengthToNodes() {
        MySequentialGraphVars.getSequentialGraphViewer().nodeValueName = "MIN-RECURSIVE-LENGTH";
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        float maxVal = 0.00f;
        for (MyNode n : nodes) {
            n.setCurrentValue(n.getMinRecursiveLength());
            if (maxVal < n.getCurrentValue()) {
                maxVal = n.getCurrentValue();
            }
        }

        if (maxVal > 0) {
            MySequentialGraphVars.g.MX_N_VAL = maxVal;
        }
    }

    public static void setMaxRecursiveLengthToNodes() {
        MySequentialGraphVars.getSequentialGraphViewer().nodeValueName = "MAX-RECURSIVE-LENGTH";
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        float maxVal = 0.00f;
        for (MyNode n : nodes) {
            n.setCurrentValue(n.getMaxRecursiveLength());
            if (maxVal < n.getCurrentValue()) {
                maxVal = n.getCurrentValue();
            }
        }

        if (maxVal > 0) {
            MySequentialGraphVars.g.MX_N_VAL = maxVal;
        }
    }

    public static void setAverageRecursiveLengthToNodes() {
        MySequentialGraphVars.getSequentialGraphViewer().nodeValueName = "AVG-RECURSIVE-LENGTH";
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        float maxVal = 0.00f;
        for (MyNode n : nodes) {
            n.setCurrentValue(n.getAverageRecursiveLength());
            if (maxVal < n.getCurrentValue()) {
                maxVal = n.getCurrentValue();
            }
        }

        if (maxVal > 0) {
            MySequentialGraphVars.g.MX_N_VAL = maxVal;
        }
    }

    public static void setMinDurationToNodes() {
        MySequentialGraphVars.getSequentialGraphViewer().nodeValueName = "MIN-DURATION";
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        float maxVal = 0.00f;
        for (MyNode n : nodes) {
            if (n.getName().contains("x") || MySequentialGraphVars.g.getSuccessors(n).size() == 0) {
                n.setCurrentValue(0f);
            } else {
                n.setCurrentValue(n.getMinDuration());
                if (maxVal < n.getCurrentValue()) {
                    maxVal = n.getCurrentValue();
                }
            }
        }

        if (maxVal > 0) {
            MySequentialGraphVars.g.MX_N_VAL = maxVal;
        }
    }

    public static void setAverageDurationToNodes() {
        MySequentialGraphVars.getSequentialGraphViewer().nodeValueName = "AVG-DURATION";
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        float maxVal = 0.00f;
        for (MyNode n : nodes) {
            if (n.getName().contains("x") || MySequentialGraphVars.g.getSuccessors(n).size() == 0) {
                n.setCurrentValue(0f);
            } else {
                n.setCurrentValue(n.getAverageDuration());
                if (maxVal < n.getCurrentValue()) {
                    maxVal = n.getCurrentValue();
                }
            }
        }

        if (maxVal > 0) {
            MySequentialGraphVars.g.MX_N_VAL = maxVal;
        }
    }

    public static void setAverageRecursiveTime() {
        MySequentialGraphVars.getSequentialGraphViewer().nodeValueName = "AVG-RECURSIVE-TIME";
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        float maxVal = 0.00f;
        for (MyNode n : nodes) {
            n.setCurrentValue(n.getAvgRecursiveTime());
            if (n.getCurrentValue() > maxVal) {
                maxVal = n.getCurrentValue();
            }
        }

        if (maxVal > 0) {
            MySequentialGraphVars.g.MX_N_VAL = maxVal;
        }
    }

    public static void setInOutContributionDifferenceToNodes() {
        MySequentialGraphVars.getSequentialGraphViewer().nodeValueName = "INOUT-CONTRIBUTION-DIFF.";
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        float maxVal = 0.00f;
        for (MyNode n : nodes) {
            n.setCurrentValue(Math.abs(n.getOutContribution() - n.getInContribution()));
            if (n.getCurrentValue() > maxVal) {
                maxVal = n.getCurrentValue();
            }
        }

        if (maxVal > 0) {
            MySequentialGraphVars.g.MX_N_VAL = maxVal;
        }
    }

    public static void setContributionToSelectedNodeNeighbors() {
        MyNode selectedNode = MySequentialGraphVars.getSequentialGraphViewer().selectedNode;
        Collection<MyEdge> inEdges = MySequentialGraphVars.g.getInEdges(selectedNode);
        for (MyEdge e : inEdges) {
            e.getSource().setCurrentValue(e.getContribution());
        }

        Collection<MyEdge> outEdges = MySequentialGraphVars.g.getOutEdges(selectedNode);
        for (MyEdge e : outEdges) {
            if (e.getSource() == e.getDest() && e.getSource() == selectedNode) {
                selectedNode.setCurrentValue(e.getContribution());
            } else {
                e.getDest().setCurrentValue(e.getContribution());
            }
        }
        selectedNode.setCurrentValue(selectedNode.getContribution());
        MySequentialGraphVars.g.MX_N_VAL = (float) selectedNode.getContribution();
    }

    public static void setInContributionToSelectedNodeNeighbors() {
        float maxVal = 0f;
        MyNode selectedNode = MySequentialGraphVars.getSequentialGraphViewer().selectedNode;
        Collection<MyEdge> inEdges = MySequentialGraphVars.g.getInEdges(selectedNode);
        for (MyEdge e : inEdges) {
            e.getSource().setCurrentValue(e.getContribution());
        }

        Collection<MyEdge> outEdges = MySequentialGraphVars.g.getOutEdges(selectedNode);
        for (MyEdge e : outEdges) {
            if (e.getSource() == e.getDest() && e.getSource() == selectedNode) continue;
            e.getDest().setCurrentValue(0f);
        }

        selectedNode.setCurrentValue(selectedNode.getInContribution());
        MySequentialGraphVars.g.MX_N_VAL = (float) selectedNode.getInContribution();
    }

    public static void setOutContributionToSelectedNodeNeighbors() {
        MyNode selectedNode = MySequentialGraphVars.getSequentialGraphViewer().selectedNode;
        Collection<MyEdge> inEdges = MySequentialGraphVars.g.getInEdges(selectedNode);
        for (MyEdge e : inEdges) {
            if (e.getSource() == e.getDest() && e.getSource() == selectedNode) continue;
            e.getSource().setCurrentValue(0f);
        }

        Collection<MyEdge> outEdges = MySequentialGraphVars.g.getOutEdges(selectedNode);
        for (MyEdge e : outEdges) {
            e.getDest().setCurrentValue(e.getContribution());
        }
        selectedNode.setCurrentValue(selectedNode.getOutContribution());
        MySequentialGraphVars.g.MX_N_VAL = (float) selectedNode.getOutContribution();
    }

    public static void setUniqueInContributionToSelectedNodeNeighbors() {
        MyNode selectedNode = MySequentialGraphVars.getSequentialGraphViewer().selectedNode;
        Collection<MyEdge> inEdges = MySequentialGraphVars.g.getInEdges(selectedNode);
        for (MyEdge e : inEdges) {
            e.getSource().setCurrentValue(e.getUniqueContribution());
        }

        Collection<MyEdge> outEdges = MySequentialGraphVars.g.getOutEdges(selectedNode);
        for (MyEdge e : outEdges) {
            if (e.getSource() == e.getDest() && e.getSource() == selectedNode) continue;
            e.getDest().setCurrentValue(0f);
        }

        selectedNode.setCurrentValue(selectedNode.getUniqueContribution());
        MySequentialGraphVars.g.MX_N_VAL = (float) selectedNode.getUniqueContribution();
    }

    public static void setUniqueOutContributionToSelectedNodeNeighbors() {
        MyNode selectedNode = MySequentialGraphVars.getSequentialGraphViewer().selectedNode;
        Collection<MyEdge> inEdges = MySequentialGraphVars.g.getInEdges(selectedNode);
        for (MyEdge e : inEdges) {
            if (e.getSource() == e.getDest() && e.getSource() == selectedNode) continue;
            e.getSource().setCurrentValue(0f);
        }

        Collection<MyEdge> outEdges = MySequentialGraphVars.g.getOutEdges(selectedNode);
        for (MyEdge e : outEdges) {
            e.getDest().setCurrentValue(e.getUniqueContribution());
        }
        selectedNode.setCurrentValue(selectedNode.getUniqueContribution());
        MySequentialGraphVars.g.MX_N_VAL = (float) selectedNode.getUniqueContribution();
    }

    public static void setNodeValue() {
        try {
            if (MySequentialGraphVars.isTimeOn) {
                if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 0) {
                    setContributionToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 1) {
                    setInContributionToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 2) {
                    setAverageInContributionToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 3) {
                    setOutContributionToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 4) {
                    setAverageOutContributionToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 5) {
                    setInOutContributionDifferenceToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 6) {
                    setUniqueContributionToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 7) {
                    setUniqueInNodeCountToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 8) {
                    setUniqueOutNodeCountToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 9) {
                    setInOutNodeDifferenceToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 10) {
                    setEndNodeCountToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 11) {
                    setAverageShortestDistanceToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 12) {
                    setTotalRecursiveLengthToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 13) {
                    setMinRecursiveLengthToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 14) {
                    setMaxRecursiveLengthToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 15) {
                    setAverageRecursiveLengthToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 16) {
                    setAverageRecursiveTime();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 17) {
                    setDurationToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 18) {
                    setAverageDurationToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 19) {
                    setMaxDurationToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 20) {
                    setMinDurationToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 21) {
                    setAverageReachTimeToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 22) {
                    setTotalTimeToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 23) {
                    setRecurrenceCountToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 24) {
                    setTotalRecurrenceTimeToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 25) {
                    setMaxReachTimeToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 26) {
                    setMinReachTimeToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 27) {
                    setItemsetLengthToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 28) {
                    setBetweenessCentralityToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 29) {
                    setClosenessCentralityToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 30) {
                    setEigenVectorCentralityToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 31) {
                    setPageRankScoresToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 32) {
                    setStartNodeCountToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 33) {
                    setUnReachableNodeCountToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() > 33) {
                    setUserDefinedNodeValuesToNodes();
                }
            } else {
                if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 0) {
                    setContributionToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 1) {
                    setInContributionToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 2) {
                    setOutContributionToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 3) {
                    setAverageOutContributionToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 4) {
                    setOutContributionToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 5) {
                    setInOutContributionDifferenceToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 6) {
                    setUniqueContributionToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 7) {
                    setUniqueInNodeCountToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 8) {
                    setUniqueOutNodeCountToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 9) {
                    setInOutNodeDifferenceToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 10) {
                    setEndNodeCountToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 11) {
                    setAverageShortestDistanceToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 12) {
                    setTotalRecursiveLengthToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 13) {
                    setMinRecursiveLengthToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 14) {
                    setMaxRecursiveLengthToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 15) {
                    setAverageRecursiveLengthToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 16) {
                    setRecurrenceCountToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 17) {
                    setItemsetLengthToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 18) {
                    setBetweenessCentralityToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 19) {
                    setClosenessCentralityToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 20) {
                    setEigenVectorCentralityToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 21) {
                    setPageRankScoresToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 22) {
                    setStartNodeCountToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 23) {
                    setUnReachableNodeCountToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() > 23) {
                    setUserDefinedNodeValuesToNodes();
                }
            }

            if (MySequentialGraphVars.getSequentialGraphViewer().vc.tableTabbedPane.getSelectedIndex() == 2) {
                MySequentialGraphVars.app.getSequentialGraphDashboard().graphLevelShortestAverageDistanceDistributionLineChart.decorate();
                MySequentialGraphVars.app.getSequentialGraphDashboard().graphLevelShortestDistanceUnreachableNodeCountDistributionLineChart.decorate();
                MySequentialGraphVars.app.getSequentialGraphDashboard().graphLevelShortestDistanceNodeValueDistributionLineChart.decorate();
            }

            if (MySequentialGraphVars.getSequentialGraphViewer().selectedNode != null) {
                MySequentialGraphVars.getSequentialGraphViewer().vc.updateSelectedNodeStatTable();
            } else if (MySequentialGraphVars.getSequentialGraphViewer().multiNodes != null && MySequentialGraphVars.getSequentialGraphViewer().multiNodes.size() > 0) {
                MySequentialGraphVars.getSequentialGraphViewer().vc.updateMultiNodeStatTable();
            } else {
                MyViewerComponentControllerUtil.setGraphLevelTableStatistics();
            }

            if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() > 1) {
                MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.setSelectedIndex(
                    MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex());
            }

            if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueBarChart.isSelected()) {
                MyViewerComponentControllerUtil.removeBarChartsFromViewer();
                MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueBarChart.removeActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
                MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueBarChart.setSelected(false);
                MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueBarChart.addActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
            }

            if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueBarChart.isSelected()) {
                MyViewerComponentControllerUtil.removeEdgeValueBarChartFromViewer();
                MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueBarChart.removeActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
                MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueBarChart.setSelected(false);
                MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueBarChart.addActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
            }

            MySequentialGraphVars.getSequentialGraphViewer().revalidate();
            MySequentialGraphVars.getSequentialGraphViewer().repaint();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static double getNodeValueStandardDeviation() {
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        double sum = 0.00d;
        int numNodes = 0;
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0) {continue;}
            numNodes++;
            sum += n.getCurrentValue();
        }
        if (numNodes == 0) return 0d;

        double mean = sum/numNodes;
        double std = 0.00D;
        for(MyNode n : nodes) {
            std += Math.pow(n.getCurrentValue()-mean, 2);
        }
        return std/numNodes;
    }

    public static int getRedNodeCount() {
        Collection<MyNode> ns = MySequentialGraphVars.g.getVertices();
        int cnt = 0;
        for (MyNode n : ns) {
            if (n.getCurrentValue() > 0) {
                if (MySequentialGraphVars.g.getPredecessorCount( n) == 0 && MySequentialGraphVars.g.getSuccessorCount( n) > 0) {
                    cnt++;
                }
            }
        }
        return cnt;
    }

    public static double getRedNodePercent() {
        Collection<MyNode> ns = MySequentialGraphVars.g.getVertices();
        int cnt = 0;
        for (MyNode n : ns) {
            if (n.getCurrentValue() > 0) {
                if (MySequentialGraphVars.g.getPredecessorCount( n) == 0 && MySequentialGraphVars.g.getSuccessorCount( n) > 0) {
                    cnt++;
                }
            }
        }
        return ((double)cnt/ns.size());
    }

    public static int getBlueNodeCount() {
        Collection<MyNode> ns = MySequentialGraphVars.g.getVertices();
        int cnt = 0;
        for (MyNode n : ns) {
            if (n.getCurrentValue() > 0) {
                if (MySequentialGraphVars.g.getPredecessorCount(n) > 0 && MySequentialGraphVars.g.getSuccessorCount(n) > 0) {
                    cnt++;
                }
            }
        }
        return cnt;
    }

    public static double getBlueNodePercent() {
        Collection<MyNode> ns = MySequentialGraphVars.g.getVertices();
        int cnt = 0;
        for (MyNode n : ns) {
            if (n.getCurrentValue() > 0) {
                if (MySequentialGraphVars.g.getPredecessorCount(n) > 0 && MySequentialGraphVars.g.getSuccessorCount(n) > 0) {
                    cnt++;
                }
            }
        }
        return ((double)cnt/ns.size());
    }

    public static double getGreenNodePercent() {
        Collection<MyNode> ns = MySequentialGraphVars.g.getVertices();
        int cnt = 0;
        for (MyNode n : ns) {
            if (n.getCurrentValue() > 0) {
                if (MySequentialGraphVars.g.getPredecessorCount(n) > 0 && MySequentialGraphVars.g.getSuccessorCount(n) == 0) {
                    cnt++;
                }
            }
        }
        return ((double)cnt/ns.size());
    }

    public static int getGreenNodeCount() {
        Collection<MyNode> ns = MySequentialGraphVars.g.getVertices();
        int cnt = 0;
        for (MyNode n : ns) {
            if (n.getCurrentValue() > 0) {
                if (MySequentialGraphVars.g.getPredecessorCount(n) > 0 && MySequentialGraphVars.g.getSuccessorCount(n) == 0) {
                    cnt++;
                }
            }
        }
        return cnt;
    }

    public static double getMaximumNodeValue() {
        double maxValue = 0D;
        Collection<MyNode> ns = MySequentialGraphVars.g.getVertices();
        for (MyNode n : ns) {
            if (n.getCurrentValue() == 0) continue;
            if (maxValue < n.getCurrentValue()) {
                maxValue = n.getCurrentValue();
            }
        }
        return maxValue;
    }
    public static double getMinimumNodeValue() {
        double minValue = 1000000000000D;
        Collection<MyNode> ns = MySequentialGraphVars.g.getVertices();
        for (MyNode n : ns) {
            if (n.getCurrentValue() > 0 && minValue > n.getCurrentValue()) {
                minValue = n.getCurrentValue();
            }
        }
        return (minValue == 1000000000000D ? 0.00D : minValue);
    }

    public static String getAverageNodeValue() {
        Collection<MyNode> ns = MySequentialGraphVars.g.getVertices();
        float totalVal = 0f;
        for (MyNode n : ns) {
            if (n.getCurrentValue() > 0) {
                totalVal += n.getCurrentValue();
            }
        }
        return MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(totalVal/ns.size()));
    }

    public static int getGreaterThanZeroNodeValueCount() {
        Collection<MyNode> ns = MySequentialGraphVars.g.getVertices();
        int cnt=0;
        for (MyNode n : ns) {
            if (n.getCurrentValue() > 0) {
                cnt++;
            }
        }
        return cnt;
    }

    public static double getAverageInContribution() {
        long total = 0L;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0) continue;
            total += n.getInContribution();
        }
        return (double)total/nodes.size();
    }

    public static double getAverageOutContribution() {
        long total = 0L;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0) continue;
            total += n.getOutContribution();
        }
        return (double)total/nodes.size();
    }

    public static double getAveragePredecessorCount() {
        long total = 0L;
        int ncnt = 0;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0) continue;
            int val = MySequentialGraphVars.g.getPredecessorCount(n);
            if (val > 0) {
                total += val;
                ncnt++;
            }
        }
        return (double)total/ncnt;
    }

    public static double getAverageSuccessorCount() {
        long total = 0L;
        int ncnt = 0;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0) continue;
            int val = MySequentialGraphVars.g.getSuccessorCount(n);
            if (val > 0) {
                total += val;
                ncnt++;
            }
        }
        return (double)total/ncnt;
    }

    public static double getAverageUnreachableNodeCount() {
        long total = 0L;
        int count = 0;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0) continue;
            int val = n.getUnreachableNodeCount();
            if (val > 0) {
                total += val;
                count++;
            }
        }
        return (double)total/count;
    }

    public static double getAverageUniqueContribution() {
        long total = 0L;
        int ncnt = 0;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0) continue;
            long val = n.getUniqueContribution();
            if (val > 0) {
                total += val;
                ncnt++;
            }
        }
        return (double)total/ncnt;
    }

    public static double getMaximumUniqueContribution() {
        long max = 0;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0) continue;
            long val = n.getUniqueContribution();
            if (val > max) {
                max = val;
            }
        }
        return max;
    }

    public static long getMinimumUniqueContribution() {
        long min = 1000000000000L;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0) continue;
            long val = n.getUniqueContribution();
            if (val < min && n.getUniqueContribution() > 0) {
                min = val;
            }
        }
        return (min == 1000000000000L ? 0 : min);
    }

    public static double getUniqueContributionStandardDeviation() {
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        double sum = 0.00d;
        int numNodes = 0;
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0) {continue;}
            numNodes++;
            sum += n.getUniqueContribution();
        }
        double mean = sum/numNodes;
        double std = 0.00D;
        for(MyNode n : nodes) {std += Math.pow(n.getUniqueContribution()-mean, 2);}
        return Math.sqrt(std/numNodes);
    }

    public static long getMaxUnreachableNodeCount() {
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        long max = 0L;
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0) continue;;
            if (n.getUnreachableNodeCount() > max) {
                max = n.getUnreachableNodeCount();
            }
        }
        return max;
    }

    public static float getUnreachableNodeCountStandardDeviation() {
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        double sum = 0.00d;
        int numNodes = 0;
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0) {continue;}
            numNodes++;
            sum += n.getUnreachableNodeCount();
        }
        double mean = sum/numNodes;
        sum = 0;
        for(MyNode n : nodes) {
            sum += Math.pow(n.getUnreachableNodeCount()-mean, 2);
        }
        return (float) Math.sqrt(sum/numNodes);
    }

    public static int getMaximumInNodeCount() {
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        int max = 0;
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0) continue;
            int value = MySequentialGraphVars.g.getPredecessorCount(n);
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    public static int getMinimumInNodeCount() {
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        int min = 1000000000;
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0) continue;
            int value = MySequentialGraphVars.g.getPredecessorCount(n);
            if (value > 0 && value < min) {
                min = value;
            }
        }
        return (min == 1000000000 ? 0 : min);
    }

    public static float getInNodeCountStandardDeviation() {
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        double sum = 0.00d;
        int count = 0;
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0 || MySequentialGraphVars.g.getPredecessorCount(n) == 0) {continue;}
            count++;
            sum += MySequentialGraphVars.g.getPredecessorCount(n);
        }
        double mean = sum/count;

        sum = 0;
        for(MyNode n : nodes) {
            int value = MySequentialGraphVars.g.getPredecessorCount(n);
            if (value == 0) continue;
            sum += Math.pow(value-mean, 2);
        }
        return (float) Math.sqrt(sum/count);
    }

    public static float getAverageInNodeCount() {
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        float sum = 0.00f;
        int count = 0;
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0 || MySequentialGraphVars.g.getSuccessorCount(n) == 0) {continue;}
            count++;
            sum += MySequentialGraphVars.g.getPredecessorCount(n);
        }
        return (count == 0 ? 0.00f : sum/count);
    }

    public static int getMaximumOutNodeCount() {
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        int max = 0;
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0) continue;
            int value = MySequentialGraphVars.g.getSuccessorCount(n);
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    public static int getMinimumOutNodeCount() {
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        int min = 1000000000;
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0) continue;
            int value = MySequentialGraphVars.g.getSuccessorCount(n);
            if (value > 0 && value < min) {
                min = value;
            }
        }
        return (min == 1000000000 ? 0 : min);
    }

    public static float getOutNodeCountStandardDeviation() {
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        double sum = 0.00d;
        int count = 0;
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0 || MySequentialGraphVars.g.getSuccessorCount(n) == 0) {continue;}
            count++;
            sum += MySequentialGraphVars.g.getSuccessorCount(n);
        }
        double mean = sum/count;

        sum = 0;
        for(MyNode n : nodes) {
            int value = MySequentialGraphVars.g.getSuccessorCount(n);
            if (value == 0) continue;
            sum += Math.pow(value-mean, 2);
        }
        return (float) Math.sqrt(sum/count);
    }

    public static int getMaximumInContribution() {
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        int max = 0;
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0) continue;
            int value = n.getInContribution();
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    public static int getMinimumInContribution() {
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        int min = 1000000000;
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0) continue;
            int value = n.getInContribution();
            if (value > 0 && value < min) {
                min = value;
            }
        }
        return (min == 1000000000 ? 0 : min);
    }

    public static float getInContributionStandardDeviation() {
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        double sum = 0.00d;
        int count = 0;
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0 || n.getInContribution() == 0) {continue;}
            count++;
            sum += n.getInContribution();
        }
        double mean = sum/count;

        sum = 0;
        for(MyNode n : nodes) {
            int value = n.getInContribution();
            if (value == 0) continue;
            sum += Math.pow(value-mean, 2);
        }
        return (float) Math.sqrt(sum/count);
    }

    public static int getMaximumOutContribution() {
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        int max = 0;
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0) continue;
            int value = n.getOutContribution();
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    public static int getMinimumOutContribution() {
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        int min = 1000000000;
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0) continue;
            int value = n.getOutContribution();
            if (value > 0 && value < min) {
                min = value;
            }
        }
        return (min == 1000000000 ? 0 : min);
    }

    public static float getOutContributionStandardDeviation() {
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        double sum = 0.00d;
        int count = 0;
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0 || n.getOutContribution() == 0) {continue;}
            count++;
            sum += n.getOutContribution();
        }
        double mean = sum/count;

        sum = 0;
        for(MyNode n : nodes) {
            int value = n.getOutContribution();
            if (value == 0) continue;
            sum += Math.pow(value-mean, 2);
        }
        return (float) Math.sqrt(sum/count);
    }

    public static long getMinUnreachableNodeCount() {
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        long min = 10000000000L;
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0) continue;
            if (n.getUnreachableNodeCount() > 0 && n.getUnreachableNodeCount() < min) {
                min = n.getUnreachableNodeCount();
            }
        }
        return (min == 1000000000 ? 0 : min);
    }



}
