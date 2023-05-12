package medousa.sequential.utils;

import medousa.sequential.graph.MyClusteringConfig;
import medousa.sequential.graph.MyEdge;
import medousa.sequential.graph.MyNode;
import medousa.sequential.graph.MyViewerComponentController;
import medousa.sequential.graph.stats.barchart.MyGraphLevelEdgeValueBarChart;
import medousa.sequential.graph.stats.barchart.MyGraphLevelNodeValueBarChart;

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
            n.setCurrentValue(n.getEndNodeCount());
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
            n.setCurrentValue(n.getOpenNodeCount());
            if (maxVal < n.getCurrentValue()) {maxVal = n.getCurrentValue();}
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

    public static void setInContributionToNodes() {
        MySequentialGraphVars.getSequentialGraphViewer().nodeValueName = "IN-CONTRIBUTION";
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        float maxVal = 0.00f;
        for (MyNode n : nodes) {
            n.setCurrentValue(n.getInContribution());
            if (maxVal < n.getCurrentValue()) {maxVal = n.getInContribution();}
        }

        if (maxVal > 0) {
            MySequentialGraphVars.g.MX_N_VAL = maxVal;
        }
    }

    public static void setAverageInContributionToNodes() {
        MySequentialGraphVars.getSequentialGraphViewer().nodeValueName = "AVG.-IN-CONTRIBUTION";
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        float maxVal = 0.00f;
        for (MyNode n : nodes) {
            n.setCurrentValue(n.getAverageInContribution());
            if (maxVal < n.getCurrentValue()) {maxVal = n.getAverageInContribution();}
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

    public static void setOutContributionToNodes() {
        MySequentialGraphVars.getSequentialGraphViewer().nodeValueName = "OUT-CONTRIBUTION";
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        float maxVal = 0.00f;
        for (MyNode n : nodes) {
            n.setCurrentValue(n.getOutContribution());
            if (maxVal < n.getCurrentValue()) {maxVal = n.getCurrentValue();}
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
                    setDurationToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 19) {
                    setAverageDurationToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 20) {
                    setMaxDurationToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 21) {
                    setMinDurationToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 22) {
                    setAverageReachTimeToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 23) {
                    setTotalTimeToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 24) {
                    setRecurrenceCountToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 25) {
                    setTotalRecurrenceTimeToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 26) {
                    setMaxReachTimeToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 27) {
                    setMinReachTimeToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 28) {
                    setItemsetLengthToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 29) {
                    setBetweenessCentralityToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 30) {
                    setClosenessCentralityToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 31) {
                    setEigenVectorCentralityToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 32) {
                    setPageRankScoresToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 33) {
                    setStartNodeCountToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() == 34) {
                    setUnReachableNodeCountToNodes();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() > 34) {
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
            MyViewerControlComponentUtil.setGraphLevelTableStatistics();
            //MySequentialGraphVars.app.getSequentialGraphDashboard().graphLevelNodeValueDistributionLineGraph.decorate();

            if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() > 1) {
                MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.setSelectedIndex(
                    MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex());
            }

            if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueBarChart.isSelected()) {
                MyViewerControlComponentUtil.removeBarChartsFromViewer();
                MySequentialGraphVars.getSequentialGraphViewer().graphLevelNodeValueBarChart = new MyGraphLevelNodeValueBarChart();
                MySequentialGraphVars.getSequentialGraphViewer().add(MySequentialGraphVars.getSequentialGraphViewer().graphLevelNodeValueBarChart);
            }

            if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueBarChart.isSelected()) {
                MyViewerControlComponentUtil.removeEdgeValueBarChartFromViewer();
                MySequentialGraphVars.getSequentialGraphViewer().graphLelvelEdgeValueBarChart = new MyGraphLevelEdgeValueBarChart();
                MySequentialGraphVars.getSequentialGraphViewer().add(MySequentialGraphVars.getSequentialGraphViewer().graphLelvelEdgeValueBarChart);
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
        int ncnt = 0;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0) continue;
            int val = n.getUnreachableNodeCount();
            if (val > 0) {
                total += val;
                ncnt++;
            }
        }
        return (double)total/ncnt;
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

    public static long getTotalUnreachableNodeCount() {
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        long count = 0L;
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0) continue;;
            count += n.getUnreachableNodeCount();
        }
        return count;
    }

}
