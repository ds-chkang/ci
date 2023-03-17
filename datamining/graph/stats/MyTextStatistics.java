package datamining.graph.stats;

import datamining.graph.MyNode;
import datamining.utils.MyEdgeUtil;
import datamining.utils.MyMathUtil;
import datamining.utils.MyNodeUtil;
import datamining.utils.MySelectedNodeUtil;
import datamining.utils.system.MySysUtil;
import datamining.utils.system.MyVars;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class MyTextStatistics
extends JLabel {


    public MyTextStatistics() {
        setVerticalAlignment(JLabel.BOTTOM);
        setBackground(new Color(0,0, 0, 0f));
        setHorizontalAlignment(JLabel.RIGHT);
        //setPreferredSize(new Dimension(1500, 20));
        setFont(MyVars.tahomaPlainFont12);
    }

    public void setTextStatistics() {
        if (MyVars.getViewer().multiNodes != null) {
            String numOfSelectedNodes = MyMathUtil.getCommaSeperatedNumber(MyVars.getViewer().multiNodes.size()) + "[" + MyMathUtil.oneDecimalFormat(((double)MyVars.getViewer().multiNodes.size()/MyVars.g.getVertexCount())*100) + "]";
            Set<MyNode> sharedSuccessorSet = new HashSet<>();
            for (MyNode n : MyVars.getViewer().multiNodes) {
                if (sharedSuccessorSet.size() == 0) {
                    sharedSuccessorSet.addAll(MyVars.g.getSuccessors(n));
                } else {
                    sharedSuccessorSet.retainAll(MyVars.g.getSuccessors(n));
                }
            }
            String sharedSuccessors = "SHA. S: " + MyMathUtil.getCommaSeperatedNumber(sharedSuccessorSet.size()) +
                "[" + MyMathUtil.twoDecimalFormat(((double)sharedSuccessorSet.size()/MyVars.g.getVertexCount()) * 100) + "]";

            Set<MyNode> sharedPredecessorSet = new HashSet<>();
            for (MyNode n : MyVars.getViewer().multiNodes) {
                if (sharedPredecessorSet.size() == 0) {
                    sharedPredecessorSet.addAll(MyVars.g.getPredecessors(n));
                } else {
                    sharedPredecessorSet.retainAll(MyVars.g.getPredecessors(n));
                }
            }

            Set<MyNode> nodes = new HashSet<>();
            for (MyNode n : MyVars.getViewer().multiNodes) {
                nodes.addAll(MyVars.g.getNeighbors(n));
            }
            String sharedPredecessors = "SHA P: " + MyMathUtil.getCommaSeperatedNumber(sharedPredecessorSet.size()) +
                "[" + MyMathUtil.twoDecimalFormat(((double)sharedPredecessorSet.size()/MyVars.g.getVertexCount()) * 100) + "]";

            String totalNodes = "N: " + MyMathUtil.getCommaSeperatedNumber(nodes.size()) + "[" + MyMathUtil.oneDecimalFormat(((double)nodes.size()/MyVars.g.getVertexCount()) * 100) + "]";
            String predecessors = "P.: " + MyMathUtil.getCommaSeperatedNumber(MyVars.getViewer().multiNodePredecessors.size()) + "[" + MyMathUtil.twoDecimalFormat(((double)MyVars.getViewer().multiNodePredecessors.size()/MyVars.g.getVertexCount())*100) + "]";
            String successors = "S.: " + MyMathUtil.getCommaSeperatedNumber(MyVars.getViewer().multiNodeSuccessors.size()) + "[" + MyMathUtil.twoDecimalFormat(((double)MyVars.getViewer().multiNodeSuccessors.size()/MyVars.g.getVertexCount())*100) + "]";
            String statStr = "SEL. N: " + numOfSelectedNodes + "   " + totalNodes + "   " + predecessors + "   " + successors + "   " + sharedPredecessors + "   " + sharedSuccessors + " ";
            setText(statStr);
            setTextStatisticsTooltip();
        } else if (MyVars.getViewer().selectedNode != null) {
            Set<MyNode> uniqNodes = new HashSet<>();
            uniqNodes.addAll(MyVars.g.getSuccessors(MyVars.getViewer().selectedNode));
            uniqNodes.addAll(MyVars.g.getPredecessors(MyVars.getViewer().selectedNode));
            int numOfSuccessors = MyVars.g.getSuccessorCount(MyVars.getViewer().selectedNode);
            int numOfPredecessors = MyVars.g.getPredecessorCount(MyVars.getViewer().selectedNode);
            String ss = "S: " + MyMathUtil.getCommaSeperatedNumber(numOfSuccessors) + "[" + MyMathUtil.oneDecimalFormat(((float) numOfSuccessors / MyVars.g.getVertexCount()) * 100) + "]";
            String ps = "P: " + MyMathUtil.getCommaSeperatedNumber(numOfPredecessors) + "[" + MyMathUtil.oneDecimalFormat(((float) numOfPredecessors / MyVars.g.getVertexCount()) * 100) + "]";
            uniqNodes.addAll(MyVars.g.getPredecessors(MyVars.getViewer().selectedNode));
            uniqNodes.addAll(MyVars.g.getSuccessors(MyVars.getViewer().selectedNode));
            String cont = "C.: " + MyMathUtil.getCommaSeperatedNumber(MyVars.getViewer().selectedNode.getContribution());
            String inCont = "IN-C.: " + MyMathUtil.getCommaSeperatedNumber(MyVars.getViewer().selectedNode.getInContribution());
            String outCont = "OUT-C.: " + MyMathUtil.getCommaSeperatedNumber(MyVars.getViewer().selectedNode.getOutContribution());
            String uniqCont = "U. C: " + MyMathUtil.getCommaSeperatedNumber(MyVars.getViewer().selectedNode.getUniqueContribution());
            String maxInCont = "MAX. IN-C.: " + MyMathUtil.getCommaSeperatedNumber(MyVars.getViewer().selectedNode.getMaxInContribution());
            String maxOutCont = "MAX. OUT-C.: " + MyMathUtil.getCommaSeperatedNumber(MyVars.getViewer().selectedNode.getMaxOutContribution());
            String avgInCont = "AVG. IN-C.: " + MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyVars.getViewer().selectedNode.getAverageInContribution()));
            String avgOutCont = "AVG. OUT-C.: " + MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyVars.getViewer().selectedNode.getAverageOutContribution()));
            String numOfEdges = "E: " + MyMathUtil.getCommaSeperatedNumber(MyEdgeUtil.getEdgesGreaterThanZero()) + "[" +
                    MyMathUtil.twoDecimalFormat(((float) MyEdgeUtil.getEdgesGreaterThanZero() / MyVars.g.getEdgeCount()) * 100) + "]";
            String statStr = "N: " + MyMathUtil.getCommaSeperatedNumber(MyVars.getViewer().selectedSingleNodePredecessors.size()+MyVars.getViewer().selectedSingleNodeSuccessors.size()+1) + "   " +
                    "SEL. N.: " + MySysUtil.decodeNodeName(MyVars.getViewer().selectedNode.getName()) + "   " + numOfEdges + "   " + ps + "   " + ss;
            /**+ "   " +
                    cont + "   " +
                    inCont + "   " +
                    outCont + "   " +
                    uniqCont + "   " +
                    maxInCont + "   " +
                    avgInCont + "   " +
                    maxOutCont + "   " +
                    avgOutCont + "   " +

             */
            setText(statStr);
            setTextStatisticsTooltip();
        } else { // Graph Level Text Statistics.

                String numNs = "N: " + MyMathUtil.getCommaSeperatedNumber(MyNodeUtil.getGreaterThanZeroNodeValueCount());
                String numEs = "E: " + MyMathUtil.getCommaSeperatedNumber(MyEdgeUtil.getOnEdgeCount());
                String red = "R: " + MyMathUtil.getCommaSeperatedNumber(MyNodeUtil.getRedNodeCount());
                String blue = "B: " + MyMathUtil.getCommaSeperatedNumber(MyNodeUtil.getBlueNodeCount());
                String green = "G: " + MyMathUtil.getCommaSeperatedNumber(MyNodeUtil.getGreenNodeCount());
                String avgInCont = "AVG. IN-C: " + MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getAverageInContribution())).split("\\.")[0];
                String avgOutCont = "AVG. OUT-C: " + MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getAverageOutContribution())).split("\\.")[0];
                String avgPredCount = "AVG. P: " + MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getAveragePredecessorCount())).split("\\.")[0];
                String avgSuccCount = "AVG. S: " + MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getAverageSuccessorCount())).split("\\.")[0];
                String maxNodeVal = "MAX. N. V: " + MySysUtil.formatAverageValue(String.valueOf(MyNodeUtil.getMaximumNodeValue())).split("\\.")[0];
                String avgNodeVal = "AVG. N. V: " + MyNodeUtil.getAverageNodeValue().split("\\.")[0];
                String minNodeVal = "MIN. N. V: " + MySysUtil.formatAverageValue(String.valueOf(MyNodeUtil.getMinimumNodeValue())).split("\\.")[0];
                String stdNodeVal = "STD. N. V: " + MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getNodeValueStandardDeviation()));
                String maxEVal = "MAX. E. V: " + MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyEdgeUtil.getMaximumEdgeValue())).split("\\.")[0];
                String minEVal = "MIN. E. V: " + MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyEdgeUtil.getMinimumEdgeValue())).split("\\.")[0];
                String avgEVal = "AVG. E. V: " + MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyEdgeUtil.getAverageEdgeValue())).split("\\.")[0];
                String stdEval = "STD. E. V: " + MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyEdgeUtil.getEdgeValueStandardDeviation()));
                //statStr += "  " + maxEVal + "  " + minEVal + "  " + avgEVal + "  " + stdEval + " ";
                String statStr = numNs + "  " + numEs + "  " + red + ", " + blue + ", " + green + "  " + stdNodeVal + "  " + stdEval;// + "   " + maxNodeVal + "  " + minNodeVal + "  " + avgNodeVal + "  " + stdNodeVal + "  " + avgInCont + "  " + avgOutCont + "  " + avgPredCount + "  " + avgSuccCount;
                setText(statStr);

            setTextStatisticsTooltip();
        }
    }

    private void setTextStatisticsTooltip() {
        try {
            if (MyVars.getViewer().multiNodes != null) {
                Set<MyNode> nodes = new HashSet<>();
                nodes.addAll(MyVars.getViewer().multiNodes);
                nodes.addAll(MyVars.getViewer().multiNodeSuccessors);
                nodes.addAll(MyVars.getViewer().multiNodePredecessors);
                String numOfSelectedNodes = MyMathUtil.getCommaSeperatedNumber(MyVars.getViewer().multiNodes.size()) + "[" + MyMathUtil.oneDecimalFormat(((double)MyVars.getViewer().multiNodes.size()/MyVars.g.getVertexCount())*100) + "%]";
                String totalNodes = "NO. OF NODES: " + MyMathUtil.getCommaSeperatedNumber(nodes.size()) + "[" + MyMathUtil.oneDecimalFormat(((double)nodes.size()/MyVars.g.getVertexCount()) * 100) + "%]";
                nodes = new HashSet<>();
                for (MyNode n : MyVars.getViewer().multiNodes) {
                    if (nodes.size() == 0) {
                        nodes.addAll(MyVars.g.getSuccessors(n));
                    } else {
                        Set<MyNode> b = new HashSet<>();
                        b.addAll(MyVars.g.getSuccessors(n));
                        nodes.retainAll(b);
                    }
                }

                String sharedSuccessors = "SHARED SUCCESSORS: " +
                    MyMathUtil.getCommaSeperatedNumber(nodes.size()) + "[" + MyMathUtil.twoDecimalFormat(((double)nodes.size()/MyVars.g.getVertexCount()) * 100) + "%]";

                nodes = new HashSet<>();
                for (MyNode n : MyVars.getViewer().multiNodes) {
                    if (nodes.size() == 0) {
                        nodes.addAll(MyVars.g.getPredecessors(n));
                    } else {
                        Set<MyNode> b = new HashSet<>();
                        b.addAll(MyVars.g.getPredecessors(n));
                        nodes.retainAll(b);
                    }
                }
                String sharedPredecessors =
                    "SHARED PREDECESSORS: " + MyMathUtil.getCommaSeperatedNumber(nodes.size()) +
                    "[" + MyMathUtil.twoDecimalFormat(((double)nodes.size()/MyVars.g.getVertexCount()) * 100) + "%]" ;
                String statStr =
                    "<html><body>SELECTED NODES: " + numOfSelectedNodes +
                    "   <br>" + totalNodes +
                    "   <br>" + sharedPredecessors +
                    "   <br>" + sharedSuccessors +
                    " </body></html>";
                setToolTipText(statStr);
            } else if (MyVars.getViewer().selectedNode != null) {
                Set<MyNode> uniqNodes = new HashSet<>();
                uniqNodes.addAll(MyVars.g.getSuccessors(MyVars.getViewer().selectedNode));
                uniqNodes.addAll(MyVars.g.getPredecessors(MyVars.getViewer().selectedNode));
                int numOfSuccessors = MyVars.g.getSuccessorCount(MyVars.getViewer().selectedNode);
                int numOfPredecessors = MyVars.g.getPredecessorCount(MyVars.getViewer().selectedNode);
                String ss = "SUCCESSORS: " + MyMathUtil.getCommaSeperatedNumber(numOfSuccessors) + "[" + MyMathUtil.oneDecimalFormat(((float) numOfSuccessors / MyVars.g.getVertexCount()) * 100) + "]" + "\n";
                String ps = "PREDECESSORS: " + MyMathUtil.getCommaSeperatedNumber(numOfPredecessors) + "[" + MyMathUtil.oneDecimalFormat(((float) numOfPredecessors / MyVars.g.getVertexCount()) * 100) + "]" + "\n";
                uniqNodes.addAll(MyVars.g.getPredecessors(MyVars.getViewer().selectedNode));
                uniqNodes.addAll(MyVars.g.getSuccessors(MyVars.getViewer().selectedNode));
                String cont = "CONTRIBUTION: " + MyMathUtil.getCommaSeperatedNumber(MyVars.getViewer().selectedNode.getContribution()) + "\n";
                String inCont = "IN-CONTRIBUTION: " + MyMathUtil.getCommaSeperatedNumber(MyVars.getViewer().selectedNode.getInContribution()) + "\n";
                String outCont = "OUT-CONTRIBUTION: " + MyMathUtil.getCommaSeperatedNumber(MyVars.getViewer().selectedNode.getOutContribution()) + "\n";
                String uniqCont = "UNIQUE CONTRIBUTION: " + MyMathUtil.getCommaSeperatedNumber(MyVars.getViewer().selectedNode.getUniqueContribution()) + "\n";
                String numOfEdges = "EDGE: " + MyMathUtil.getCommaSeperatedNumber(MyEdgeUtil.getEdgesGreaterThanZero()) + "[" +
                        MyMathUtil.twoDecimalFormat(((float) MyEdgeUtil.getEdgesGreaterThanZero() / MyVars.g.getEdgeCount()) * 100) + "]" + "\n";
                String statStr = "<html><body>NO. OF NODES: " + MyMathUtil.getCommaSeperatedNumber(MyVars.getViewer().selectedSingleNodePredecessors.size()+MyVars.getViewer().selectedSingleNodeSuccessors.size()+1) + "   " +
                        "   <br>NODE: " + MySysUtil.decodeNodeName(MyVars.getViewer().selectedNode.getName()) +
                        "   <br>" + cont +
                        "   <br>" + inCont +
                        "   <br>" + outCont +
                        "   <br>" + uniqCont +
                        "   <br>" + ps +
                        "   <br>" + ss +
                        "   <br>" + numOfEdges;
                if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() > 1) {
                    String maxInVal = "MAX. IN-NODES: " + MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MySelectedNodeUtil.getMaximumInNodeValueFromSelectedNodePredecessors())) + "\n";
                    String minInVal = "MIN. IN-NODES: " + MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MySelectedNodeUtil.getMinimumInNodeValueFromSelectedNodePredecessors())) + "\n";
                    String maxOutVal = "MAX. OUT-VALUE: " + MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MySelectedNodeUtil.getMaximumOutNodeValueFromSelectedNodeSuccessors())) + "\n";
                    String minOutVal = "MIN. OUT-VALUE: " + MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MySelectedNodeUtil.getMinimumNodeOutValueFromSelectedNodeSuccessors())) + "\n";
                    String avgInVal = "AVG. IN-VALUE: " + MySelectedNodeUtil.getSelectedNodeAvgInValue();
                    String avgOutVal = "AVG. OUT-VALUE: " + MySelectedNodeUtil.getSelectedNodeAvgOutValue();
                    statStr += "    <br>" + minInVal + "   <br>" + maxInVal + "   <br>" + avgInVal + "   <br>" + minOutVal + "   <br>" + maxOutVal + "   <br>" + avgOutVal;
                }
                statStr = statStr + "</body></html>";
                setToolTipText(statStr);
            } else {
                String numNs = "<html><body> NODES: " + MyMathUtil.getCommaSeperatedNumber(MyNodeUtil.getGreaterThanZeroNodeValueCount()) + "[" +
                        MyMathUtil.oneDecimalFormat(((double) MyNodeUtil.getGreaterThanZeroNodeValueCount() / MyVars.g.getVertexCount()) * 100) + "%]";
                String numEs = "<br> EDGES: " + MyMathUtil.getCommaSeperatedNumber(MyEdgeUtil.getOnEdgeCount()) + "[" + MyMathUtil.oneDecimalFormat(((double) MyEdgeUtil.getOnEdgeCount() / MyVars.g.getEdgeCount()) * 100) + "%] ";
                String red = "<br> RED NODES: " + MyMathUtil.getCommaSeperatedNumber(MyNodeUtil.getRedNodeCount());
                String blue = "<br> BLUE NODES: " + MyMathUtil.getCommaSeperatedNumber(MyNodeUtil.getBlueNodeCount());
                String green = "<br> GREEN NODES: " + MyMathUtil.getCommaSeperatedNumber(MyNodeUtil.getGreenNodeCount());
                String avgInCont = "<br> AVG. IN-CONTRIBUTION: " + MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getAverageInContribution()));
                String avgOutCont = "<br> AVG. OUT-CONTRIBUTION: " + MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getAverageOutContribution()));
                String avgPredCount = "<br> AVG. PREDECESSORS: " + MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getAveragePredecessorCount()));
                String avgSuccCount = "<br> AVG. SUCCESSORS: " + MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getAverageSuccessorCount()));
                String maxNodeVal = "<br> MAX. NODE VALUE: " + MySysUtil.formatAverageValue(String.valueOf(MyNodeUtil.getMaximumNodeValue()));
                String avgNodeVal = "<br> AVG. NODE VALUE: " + MyNodeUtil.getAverageNodeValue() + "\n";
                String minNodeVal = "<br> MIN. NODE VALUE: " + MySysUtil.formatAverageValue(String.valueOf(MyNodeUtil.getMinimumNodeValue()));
                String stdNodeVal = "<br> STD. NODE VALUE: " + MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getNodeValueStandardDeviation()));
                String statStr = numNs + "  " + numEs + "  " + red + ", " + blue + ", " + green + "   " + maxNodeVal + "  " + minNodeVal + "  " + avgNodeVal + "  " + stdNodeVal + "  " + avgInCont + "  " + avgOutCont + "  " + avgPredCount + "  " + avgSuccCount;
                if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() > 1) {
                    String maxEVal = "<br> MAX. EDGE VALUE: " + MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyEdgeUtil.getMaximumEdgeValue()));
                    String minEVal = "<br> MIN. EDGE VALUE: " + MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyEdgeUtil.getMinimumEdgeValue()));
                    String avgEVal = "<br> AVG. EDGE VALUE: " + MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyEdgeUtil.getAverageEdgeValue()));
                    String stdEval = "<br> STD. EDGE VALUE: " + MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyEdgeUtil.getEdgeValueStandardDeviation()));
                    statStr += "  " + maxEVal + "  " + minEVal + "  " + avgEVal + "  " + stdEval + " ";
                }
                statStr = statStr + "</body></html>";
                setToolTipText(statStr);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
