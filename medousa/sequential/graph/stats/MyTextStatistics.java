package medousa.sequential.graph.stats;

import medousa.sequential.graph.MyNode;
import medousa.sequential.utils.MyEdgeUtil;
import medousa.sequential.utils.MyMathUtil;
import medousa.sequential.utils.MyNodeUtil;
import medousa.sequential.utils.MySelectedNodeUtil;
import medousa.sequential.utils.MySequentialGraphSysUtil;
import medousa.sequential.utils.MySequentialGraphVars;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class MyTextStatistics
extends JLabel {

    public MyTextStatistics() {
        setVerticalAlignment(JLabel.BOTTOM);
        setBackground(new Color(0,0, 0, 0f));
        setHorizontalAlignment(JLabel.LEFT);
        setFont(MySequentialGraphVars.tahomaPlainFont12);
    }

    public void setTextStatistics() {
        /**
        if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthSelecter.getSelectedIndex() > 0) {
            Set<MyNode> successors = new HashSet<>();
            for (String depthNode : MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeNameSet) {
                successors.addAll(MySequentialGraphVars.g.getSuccessors(MySequentialGraphVars.g.vRefs.get(depthNode)));
            }

            Set<MyNode> predecessors = new HashSet<>();
            for (String depthNode : MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeNameSet) {
                predecessors.addAll(MySequentialGraphVars.g.getPredecessors(MySequentialGraphVars.g.vRefs.get(depthNode)));
            }

            Set<MyEdge> edges = new HashSet<>();
            for (String depthNode : MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeNameSet) {
                edges.addAll(MySequentialGraphVars.g.getIncidentEdges(MySequentialGraphVars.g.vRefs.get(depthNode)));
            }

            double nodeRatio = (double) MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeNameSet.size()/ MySequentialGraphVars.g.getVertexCount();

            String txtStat = "D. N.: " + MyMathUtil.getCommaSeperatedNumber(MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeNameSet.size()) + "   " +
                "P.: " + MyMathUtil.getCommaSeperatedNumber(predecessors.size()) + "   " +
                "S.: " + MyMathUtil.getCommaSeperatedNumber(successors.size()) + "   " +
                "E.: " + MyMathUtil.getCommaSeperatedNumber(edges.size()) + "   " +
                "D. N. R.: " + MyMathUtil.twoDecimalFormat(nodeRatio) + "   ";
            setToolTipText(
                    "<html><body>" +
                    "DEPTH NODE: " + MyMathUtil.getCommaSeperatedNumber(MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeNameSet.size()) + "   " +
                    "<br>NO. OF PREDECESSORS: " + MyMathUtil.getCommaSeperatedNumber(predecessors.size()) + "   " +
                    "<br>NO. OF SUCCESSORS: " + MyMathUtil.getCommaSeperatedNumber(successors.size()) + "   " +
                    "<br>NO. OF EDGES: " + MyMathUtil.getCommaSeperatedNumber(edges.size()) + "   " +
                    "<br>DEPTH NODE RATIO: " + MyMathUtil.twoDecimalFormat(nodeRatio) + "</body></html>");

            setText(txtStat);
        } else if (MySequentialGraphVars.getSequentialGraphViewer().multiNodes != null) {
            String numOfSelectedNodes = MyMathUtil.getCommaSeperatedNumber(MySequentialGraphVars.getSequentialGraphViewer().multiNodes.size()) + "[" + MyMathUtil.oneDecimalFormat(((double) MySequentialGraphVars.getSequentialGraphViewer().multiNodes.size()/ MySequentialGraphVars.g.getVertexCount())*100) + "]";
            Set<MyNode> sharedSuccessorSet = new HashSet<>();
            for (MyNode n : MySequentialGraphVars.getSequentialGraphViewer().multiNodes) {
                if (sharedSuccessorSet.size() == 0) {
                    sharedSuccessorSet.addAll(MySequentialGraphVars.g.getSuccessors(n));
                } else {
                    sharedSuccessorSet.retainAll(MySequentialGraphVars.g.getSuccessors(n));
                }
            }
            String sharedSuccessors = "SHA. S: " + MyMathUtil.getCommaSeperatedNumber(sharedSuccessorSet.size()) +
                "[" + MyMathUtil.twoDecimalFormat(((double)sharedSuccessorSet.size()/ MySequentialGraphVars.g.getVertexCount()) * 100) + "]";

            Set<MyNode> sharedPredecessorSet = new HashSet<>();
            for (MyNode n : MySequentialGraphVars.getSequentialGraphViewer().multiNodes) {
                if (sharedPredecessorSet.size() == 0) {
                    sharedPredecessorSet.addAll(MySequentialGraphVars.g.getPredecessors(n));
                } else {
                    sharedPredecessorSet.retainAll(MySequentialGraphVars.g.getPredecessors(n));
                }
            }

            Set<MyNode> nodes = new HashSet<>();
            for (MyNode n : MySequentialGraphVars.getSequentialGraphViewer().multiNodes) {
                nodes.addAll(MySequentialGraphVars.g.getNeighbors(n));
            }

            String sharedPredecessors = "SHA P: " + MyMathUtil.getCommaSeperatedNumber(sharedPredecessorSet.size()) +
                "[" + MyMathUtil.twoDecimalFormat(((double)sharedPredecessorSet.size()/ MySequentialGraphVars.g.getVertexCount()) * 100) + "]";
            String totalNodes = "N: " + MyMathUtil.getCommaSeperatedNumber(nodes.size()) + "[" + MyMathUtil.oneDecimalFormat(((double)nodes.size()/ MySequentialGraphVars.g.getVertexCount()) * 100) + "]";
            String predecessors = "P.: " + MyMathUtil.getCommaSeperatedNumber(MySequentialGraphVars.getSequentialGraphViewer().multiNodePredecessors.size()) + "[" + MyMathUtil.twoDecimalFormat(((double) MySequentialGraphVars.getSequentialGraphViewer().multiNodePredecessors.size()/ MySequentialGraphVars.g.getVertexCount())*100) + "]";
            String successors = "S.: " + MyMathUtil.getCommaSeperatedNumber(MySequentialGraphVars.getSequentialGraphViewer().multiNodeSuccessors.size()) + "[" + MyMathUtil.twoDecimalFormat(((double) MySequentialGraphVars.getSequentialGraphViewer().multiNodeSuccessors.size()/ MySequentialGraphVars.g.getVertexCount())*100) + "]";
            String statStr = "SEL. N: " + numOfSelectedNodes + "   " + totalNodes + "   " + predecessors + "   " + successors + "   " + sharedPredecessors + "   " + sharedSuccessors + " ";
//            statStr += "AVG. S. D: " + MyMathUtil.twoDecimalFormat(MySequentialGraphSysUtil.getAverageMultiNodeShortestPathLength());

            setText(statStr);
            setTextStatisticsTooltip();
        } else if (MySequentialGraphVars.getSequentialGraphViewer().selectedNode != null) {
            Set<MyNode> uniqNodes = new HashSet<>();
            uniqNodes.addAll(MySequentialGraphVars.g.getSuccessors(MySequentialGraphVars.getSequentialGraphViewer().selectedNode));
            uniqNodes.addAll(MySequentialGraphVars.g.getPredecessors(MySequentialGraphVars.getSequentialGraphViewer().selectedNode));
            int numOfSuccessors = MySequentialGraphVars.g.getSuccessorCount(MySequentialGraphVars.getSequentialGraphViewer().selectedNode);
            int numOfPredecessors = MySequentialGraphVars.g.getPredecessorCount(MySequentialGraphVars.getSequentialGraphViewer().selectedNode);
            String ss = "S: " + MyMathUtil.getCommaSeperatedNumber(numOfSuccessors) + "[" + MyMathUtil.oneDecimalFormat(((float) numOfSuccessors / MySequentialGraphVars.g.getVertexCount()) * 100) + "]";
            String ps = "P: " + MyMathUtil.getCommaSeperatedNumber(numOfPredecessors) + "[" + MyMathUtil.oneDecimalFormat(((float) numOfPredecessors / MySequentialGraphVars.g.getVertexCount()) * 100) + "]";
            uniqNodes.addAll(MySequentialGraphVars.g.getPredecessors(MySequentialGraphVars.getSequentialGraphViewer().selectedNode));
            uniqNodes.addAll(MySequentialGraphVars.g.getSuccessors(MySequentialGraphVars.getSequentialGraphViewer().selectedNode));
            String cont = "C.: " + MyMathUtil.getCommaSeperatedNumber(MySequentialGraphVars.getSequentialGraphViewer().selectedNode.getContribution());
            String inCont = "IN-C.: " + MyMathUtil.getCommaSeperatedNumber(MySequentialGraphVars.getSequentialGraphViewer().selectedNode.getInContribution());
            String outCont = "OUT-C.: " + MyMathUtil.getCommaSeperatedNumber(MySequentialGraphVars.getSequentialGraphViewer().selectedNode.getOutContribution());
            String uniqCont = "U. C: " + MyMathUtil.getCommaSeperatedNumber(MySequentialGraphVars.getSequentialGraphViewer().selectedNode.getUniqueContribution());
            String maxInCont = "MAX. IN-C.: " + MyMathUtil.getCommaSeperatedNumber(MySequentialGraphVars.getSequentialGraphViewer().selectedNode.getMaxInContribution());
            String maxOutCont = "MAX. OUT-C.: " + MyMathUtil.getCommaSeperatedNumber(MySequentialGraphVars.getSequentialGraphViewer().selectedNode.getMaxOutContribution());
            String avgInCont = "AVG. IN-C.: " + MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MySequentialGraphVars.getSequentialGraphViewer().selectedNode.getAverageInContribution()));
            String avgOutCont = "AVG. OUT-C.: " + MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MySequentialGraphVars.getSequentialGraphViewer().selectedNode.getAverageOutContribution()));
            String numOfEdges = "E: " + MyMathUtil.getCommaSeperatedNumber(MyEdgeUtil.getEdgesGreaterThanZero()) + "[" +
                    MyMathUtil.twoDecimalFormat(((float) MyEdgeUtil.getEdgesGreaterThanZero() / MySequentialGraphVars.g.getEdgeCount()) * 100) + "]";
            String statStr = "N: " + MyMathUtil.getCommaSeperatedNumber(MySequentialGraphVars.getSequentialGraphViewer().selectedSingleNodePredecessors.size()+ MySequentialGraphVars.getSequentialGraphViewer().selectedSingleNodeSuccessors.size()+1) + "   " +
                    "SEL. N.: " + MySequentialGraphSysUtil.decodeNodeName(MySequentialGraphVars.getSequentialGraphViewer().selectedNode.getName()) + "   " + numOfEdges + "   " + ps + "   " + ss;
            //statStr += "   " + "AVG. S. D.: " + MyMathUtil.twoDecimalFormat(MySequentialGraphVars.getSequentialGraphViewer().selectedNode.getAverageShortestDistance());
            setText(statStr);
            setTextStatisticsTooltip();
        } else { // Graph Level Text Statistics.
            String numNs = "N: " + MyMathUtil.getCommaSeperatedNumber(MyNodeUtil.getGreaterThanZeroNodeValueCount());
            String numEs = "E: " + MyMathUtil.getCommaSeperatedNumber(MyEdgeUtil.getOnEdgeCount());
            String red = "R: " + MyMathUtil.getCommaSeperatedNumber(MyNodeUtil.getRedNodeCount()) + "[" + MyMathUtil.twoDecimalFormat(((double)MyNodeUtil.getRedNodeCount()/ MySequentialGraphVars.g.getVertexCount())*100) + "]";
            String blue = "B: " + MyMathUtil.getCommaSeperatedNumber(MyNodeUtil.getBlueNodeCount()) + "[" + MyMathUtil.twoDecimalFormat(((double)MyNodeUtil.getBlueNodeCount()/ MySequentialGraphVars.g.getVertexCount())*100) + "]";
            String green = "G: " + MyMathUtil.getCommaSeperatedNumber(MyNodeUtil.getGreenNodeCount()) + "[" + MyMathUtil.twoDecimalFormat(((double)MyNodeUtil.getGreenNodeCount()/ MySequentialGraphVars.g.getVertexCount())*100) + "]";
            String avgInCont = "AVG. IN-C: " + MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getAverageInContribution()));
            String avgOutCont = "AVG. OUT-C: " + MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getAverageOutContribution()));
            String avgPredCount = "AVG. P: " + MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getAveragePredecessorCount()));
            String avgSuccCount = "AVG. S: " + MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getAverageSuccessorCount()));
            //String maxNodeVal = "MAX. N. V: " + MySequentialGraphSysUtil.formatAverageValue(String.valueOf(MyNodeUtil.getMaximumNodeValue()));
            String avgNodeVal = "AVG. N. V: " + MyNodeUtil.getAverageNodeValue();
            //String minNodeVal = "MIN. N. V: " + MySequentialGraphSysUtil.formatAverageValue(String.valueOf(MyNodeUtil.getMinimumNodeValue()));
            String stdNodeVal = "STD. N. V: " + MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getNodeValueStandardDeviation()));
            String maxEVal = "MAX. E. V: " + MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyEdgeUtil.getMaximumEdgeValue()));
            String minEVal = "MIN. E. V: " + MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyEdgeUtil.getMinimumEdgeValue()));
            String avgEVal = "AVG. E. V: " + MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyEdgeUtil.getAverageEdgeValue()));
            String stdEval = "STD. E. V: " + MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyEdgeUtil.getEdgeValueStandardDeviation()));
            String avgShortestDistance = " AVG. S. D.: " + MyMathUtil.twoDecimalFormat(MySequentialGraphSysUtil.getAverageShortestPathLength());
            String statStr =
                            numNs + "  " +
                            numEs + "  " +
                            red + ", " +
                            blue + ", " +
                            green + "  " +
                            avgNodeVal + "  " +
                            avgEVal + "  " +
                            stdNodeVal + "  " +
                            stdEval + "   " +
                            avgNodeVal + "  " +
                            stdNodeVal + "  ";

            setText(statStr);

            setTextStatisticsTooltip();
        }
         */
    }

    public void setClusterTextStatistics() {
        this.removeAll();
        this.setBackground(Color.WHITE);
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));

        Rectangle r = MySequentialGraphVars.app.getBounds();
        int h = (int) r.getHeight();
        this.setBounds(5, h - 150, 1800, 30);

        JLabel maxNodeContLabel = new JLabel();
        maxNodeContLabel.setFont(MySequentialGraphVars.tahomaPlainFont13);
        maxNodeContLabel.setBackground(Color.WHITE);
        maxNodeContLabel.setForeground(Color.BLACK);
        String maxNodeContValue = MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MySequentialGraphVars.g.getClusteredMaximumNodeValue()));
        maxNodeContLabel.setText("MAX. N. V.: " + maxNodeContValue);

        JLabel minNodeContLabel = new JLabel();
        minNodeContLabel.setFont(MySequentialGraphVars.tahomaPlainFont13);
        minNodeContLabel.setBackground(Color.WHITE);
        minNodeContLabel.setForeground(Color.BLACK);
        String minNodeContValue = MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MySequentialGraphVars.g.getClusteredMinimumNodeValue()));
        minNodeContLabel.setText("MIN. N. V.: " + minNodeContValue);

        JLabel avgNodeContLabel = new JLabel();
        avgNodeContLabel.setFont(MySequentialGraphVars.tahomaPlainFont13);
        avgNodeContLabel.setBackground(Color.WHITE);
        avgNodeContLabel.setForeground(Color.BLACK);
        String avgNodeContValue = MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MySequentialGraphVars.g.getClusteredAverageNodeValue()));
        avgNodeContLabel.setText("AVG. N. V.: " + avgNodeContValue);

        JLabel nodeValueStdLabel = new JLabel();
        nodeValueStdLabel.setFont(MySequentialGraphVars.tahomaPlainFont13);
        nodeValueStdLabel.setBackground(Color.WHITE);
        nodeValueStdLabel.setForeground(Color.BLACK);
        String nodeValueStdValue = MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MySequentialGraphVars.g.getClusteredNodeValueStandardDeviation()));
        nodeValueStdLabel.setText("N. V. STD.: " + nodeValueStdValue);

        JLabel maxEdgeContLabel = new JLabel();
        maxEdgeContLabel.setFont(MySequentialGraphVars.tahomaPlainFont13);
        maxEdgeContLabel.setBackground(Color.WHITE);
        maxEdgeContLabel.setForeground(Color.BLACK);
        String maxEdgeContValue = MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MySequentialGraphVars.g.getClusteredMaximumEdgeValue()));
        maxEdgeContLabel.setText("MAX. E. V.: " + maxEdgeContValue);

        JLabel minEdgeContLabel = new JLabel();
        minEdgeContLabel.setFont(MySequentialGraphVars.tahomaPlainFont13);
        minEdgeContLabel.setBackground(Color.WHITE);
        minEdgeContLabel.setForeground(Color.BLACK);
        String minEdgeContValue = MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MySequentialGraphVars.g.getClusteredMinimumEdgeValue()));
        minEdgeContLabel.setText("MIN. E. V.: " + minEdgeContValue);

        JLabel avgEdgeContLabel = new JLabel();
        avgEdgeContLabel.setFont(MySequentialGraphVars.tahomaPlainFont13);
        avgEdgeContLabel.setBackground(Color.WHITE);
        avgEdgeContLabel.setForeground(Color.BLACK);
        String avgEdgeContValue = MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MySequentialGraphVars.g.getClusteredAverageEdgeValue()));
        avgEdgeContLabel.setText("AVG. E. V.: " + avgEdgeContValue);

        JLabel edgeStdLabel = new JLabel();
        edgeStdLabel.setFont(MySequentialGraphVars.tahomaPlainFont13);
        edgeStdLabel.setBackground(Color.WHITE);
        edgeStdLabel.setForeground(Color.BLACK);
        String edgeValueStdValue = MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MySequentialGraphVars.g.getEdgeValueStandardDeviation()));
        edgeStdLabel.setText("E. V. STD.: " + edgeValueStdValue);

        JLabel maxSucCntLabel = new JLabel();
        maxSucCntLabel.setFont(MySequentialGraphVars.tahomaPlainFont13);
        maxSucCntLabel.setBackground(Color.WHITE);
        maxSucCntLabel.setForeground(Color.BLACK);
        String maxSucCntValue = MyMathUtil.getCommaSeperatedNumber(MySequentialGraphVars.g.getClusteredMaximumSuccessorCount());
        maxSucCntLabel.setText("MAX. S.: " + maxSucCntValue);

        JLabel avgSucCntLabel = new JLabel();
        avgSucCntLabel.setFont(MySequentialGraphVars.tahomaPlainFont13);
        avgSucCntLabel.setBackground(Color.WHITE);
        avgSucCntLabel.setForeground(Color.BLACK);
        String avgSucCntValue = MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MySequentialGraphVars.g.getClusteredAverageSuccessorCount()));
        avgSucCntLabel.setText("AVG. S.: " + avgSucCntValue);

        JLabel maxPredCntLabel = new JLabel();
        maxPredCntLabel.setFont(MySequentialGraphVars.tahomaPlainFont13);
        maxPredCntLabel.setBackground(Color.WHITE);
        maxPredCntLabel.setForeground(Color.BLACK);
        String maxPredCntValue = MyMathUtil.getCommaSeperatedNumber(MySequentialGraphVars.g.getClusteredMaximumPredecessorCount());
        maxPredCntLabel.setText("MAX. P. C.: " + maxPredCntValue);

        JLabel avgPredCntLabel = new JLabel();
        avgPredCntLabel.setFont(MySequentialGraphVars.tahomaPlainFont13);
        avgPredCntLabel.setBackground(Color.WHITE);
        avgPredCntLabel.setForeground(Color.BLACK);
        String avgPredCntValue = MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MySequentialGraphVars.g.getClusteredAveragePredecessorCount()));
        avgPredCntLabel.setText("AVG. P.: " + avgPredCntValue);

        JLabel redNodeCntLabel = new JLabel();
        redNodeCntLabel.setFont(MySequentialGraphVars.tahomaPlainFont13);
        redNodeCntLabel.setBackground(Color.WHITE);
        redNodeCntLabel.setForeground(Color.BLACK);
        String redNodeCntValue = MySequentialGraphSysUtil.getCommaSeperateString(MySequentialGraphVars.g.getClusteredRedNodeCount()) + "[" + MyMathUtil.twoDecimalFormat(((double) MySequentialGraphVars.g.getClusteredRedNodeCount()/ MySequentialGraphVars.g.getGraphNodeCount())) + "]";
        redNodeCntLabel.setText("R: " + redNodeCntValue);

        JLabel blueNodeCntLabel = new JLabel();
        blueNodeCntLabel.setFont(MySequentialGraphVars.tahomaPlainFont13);
        blueNodeCntLabel.setBackground(Color.WHITE);
        blueNodeCntLabel.setForeground(Color.BLACK);
        String blueNodeCntValue = MySequentialGraphSysUtil.getCommaSeperateString(MySequentialGraphVars.g.getClusteredBlueNodeCount()) + "[" + MyMathUtil.twoDecimalFormat(((double) MySequentialGraphVars.g.getClusteredBlueNodeCount()/ MySequentialGraphVars.g.getGraphNodeCount())) + "]";;
        blueNodeCntLabel.setText("B: " + blueNodeCntValue);

        JLabel greenNodeCntLabel = new JLabel();
        greenNodeCntLabel.setFont(MySequentialGraphVars.tahomaPlainFont13);
        greenNodeCntLabel.setBackground(Color.WHITE);
        greenNodeCntLabel.setForeground(Color.BLACK);
        String greenNodeCntValue = MySequentialGraphSysUtil.getCommaSeperateString(MySequentialGraphVars.g.getClusteredGreenNodeCount()) + "[" + MyMathUtil.twoDecimalFormat(((double) MySequentialGraphVars.g.getClusteredGreenNodeCount()/ MySequentialGraphVars.g.getGraphNodeCount())) + "]";;
        greenNodeCntLabel.setText("G: " + greenNodeCntValue);

        JLabel numOfNodes = new JLabel();
        numOfNodes.setFont(MySequentialGraphVars.tahomaPlainFont13);
        numOfNodes.setBackground(Color.WHITE);
        numOfNodes.setForeground(Color.BLACK);
        String numOfNodesValue = MyMathUtil.getCommaSeperatedNumber(MySequentialGraphVars.g.getClusteredGraphNodeCount());
        numOfNodes.setText("N.: " + numOfNodesValue);

        JLabel numOfEdges = new JLabel();
        numOfEdges.setFont(MySequentialGraphVars.tahomaPlainFont13);
        numOfEdges.setBackground(Color.WHITE);
        numOfEdges.setForeground(Color.BLACK);
        long edgeCount = MySequentialGraphVars.g.getClusteredGraphEdgeCount();
        String numeOfEdgesValue = MyMathUtil.getCommaSeperatedNumber(edgeCount);
        numOfEdges.setText("E.: " + numeOfEdgesValue);

        String statStr = numOfNodes.getText() + "  " + numOfEdges.getText() + "  " + avgNodeContLabel.getText() + "  " + nodeValueStdLabel.getText() + "  " +
                avgEdgeContLabel.getText() + "  " + edgeStdLabel.getText() + "  " + redNodeCntLabel.getText() + "  " + blueNodeCntLabel.getText() + "  " +
                greenNodeCntLabel.getText();
        setText("");
        setText(statStr);
        revalidate();
        repaint();
    }

    private void setTextStatisticsTooltip() {
        try {
            if (MySequentialGraphVars.getSequentialGraphViewer().multiNodes != null) {
                Set<MyNode> nodes = new HashSet<>();
                nodes.addAll(MySequentialGraphVars.getSequentialGraphViewer().multiNodes);
                nodes.addAll(MySequentialGraphVars.getSequentialGraphViewer().multiNodeSuccessors);
                nodes.addAll(MySequentialGraphVars.getSequentialGraphViewer().multiNodePredecessors);
                String numOfSelectedNodes = MyMathUtil.getCommaSeperatedNumber(MySequentialGraphVars.getSequentialGraphViewer().multiNodes.size()) + "[" + MyMathUtil.oneDecimalFormat(((double) MySequentialGraphVars.getSequentialGraphViewer().multiNodes.size()/ MySequentialGraphVars.g.getVertexCount())*100) + "%]";
                String totalNodes = "NO. OF NODES: " + MyMathUtil.getCommaSeperatedNumber(nodes.size()) + "[" + MyMathUtil.oneDecimalFormat(((double)nodes.size()/ MySequentialGraphVars.g.getVertexCount()) * 100) + "%]";
                nodes = new HashSet<>();
                for (MyNode n : MySequentialGraphVars.getSequentialGraphViewer().multiNodes) {
                    if (nodes.size() == 0) {
                        nodes.addAll(MySequentialGraphVars.g.getSuccessors(n));
                    } else {
                        Set<MyNode> b = new HashSet<>();
                        b.addAll(MySequentialGraphVars.g.getSuccessors(n));
                        nodes.retainAll(b);
                    }
                }

                String sharedSuccessors = "SHARED SUCCESSORS: " +
                    MyMathUtil.getCommaSeperatedNumber(nodes.size()) + "[" + MyMathUtil.twoDecimalFormat(((double)nodes.size()/ MySequentialGraphVars.g.getVertexCount()) * 100) + "%]";

                nodes = new HashSet<>();
                for (MyNode n : MySequentialGraphVars.getSequentialGraphViewer().multiNodes) {
                    if (nodes.size() == 0) {
                        nodes.addAll(MySequentialGraphVars.g.getPredecessors(n));
                    } else {
                        Set<MyNode> b = new HashSet<>();
                        b.addAll(MySequentialGraphVars.g.getPredecessors(n));
                        nodes.retainAll(b);
                    }
                }
                String sharedPredecessors =
                    "SHARED PREDECESSORS: " + MyMathUtil.getCommaSeperatedNumber(nodes.size()) +
                    "[" + MyMathUtil.twoDecimalFormat(((double)nodes.size()/ MySequentialGraphVars.g.getVertexCount()) * 100) + "%]" ;

                String statStr =
                    "<html><body>SELECTED NODES: " + numOfSelectedNodes +
                    "   <br>" + totalNodes +
                    "   <br>" + sharedPredecessors +
                    "   <br>" + sharedSuccessors;
                statStr += "<br>AVERAGE SHORTEST DISTANCE: " + MyMathUtil.twoDecimalFormat(MySequentialGraphSysUtil.getAverageMultiNodeShortestPathLength()) + " </body></html>";
                setToolTipText(statStr);
            } else if (MySequentialGraphVars.getSequentialGraphViewer().selectedNode != null) {
                Set<MyNode> uniqNodes = new HashSet<>();
                uniqNodes.addAll(MySequentialGraphVars.g.getSuccessors(MySequentialGraphVars.getSequentialGraphViewer().selectedNode));
                uniqNodes.addAll(MySequentialGraphVars.g.getPredecessors(MySequentialGraphVars.getSequentialGraphViewer().selectedNode));
                int numOfSuccessors = MySequentialGraphVars.g.getSuccessorCount(MySequentialGraphVars.getSequentialGraphViewer().selectedNode);
                int numOfPredecessors = MySequentialGraphVars.g.getPredecessorCount(MySequentialGraphVars.getSequentialGraphViewer().selectedNode);
                String ss = "SUCCESSORS: " + MyMathUtil.getCommaSeperatedNumber(numOfSuccessors) + "[" + MyMathUtil.oneDecimalFormat(((float) numOfSuccessors / MySequentialGraphVars.g.getVertexCount()) * 100) + "]" + "\n";
                String ps = "PREDECESSORS: " + MyMathUtil.getCommaSeperatedNumber(numOfPredecessors) + "[" + MyMathUtil.oneDecimalFormat(((float) numOfPredecessors / MySequentialGraphVars.g.getVertexCount()) * 100) + "]" + "\n";
                uniqNodes.addAll(MySequentialGraphVars.g.getPredecessors(MySequentialGraphVars.getSequentialGraphViewer().selectedNode));
                uniqNodes.addAll(MySequentialGraphVars.g.getSuccessors(MySequentialGraphVars.getSequentialGraphViewer().selectedNode));
                String cont = "CONTRIBUTION: " + MyMathUtil.getCommaSeperatedNumber(MySequentialGraphVars.getSequentialGraphViewer().selectedNode.getContribution()) + "\n";
                String inCont = "IN-CONTRIBUTION: " + MyMathUtil.getCommaSeperatedNumber(MySequentialGraphVars.getSequentialGraphViewer().selectedNode.getInContribution()) + "\n";
                String outCont = "OUT-CONTRIBUTION: " + MyMathUtil.getCommaSeperatedNumber(MySequentialGraphVars.getSequentialGraphViewer().selectedNode.getOutContribution()) + "\n";
                String uniqCont = "UNIQUE CONTRIBUTION: " + MyMathUtil.getCommaSeperatedNumber(MySequentialGraphVars.getSequentialGraphViewer().selectedNode.getUniqueContribution()) + "\n";
                String numOfEdges = "EDGE: " + MyMathUtil.getCommaSeperatedNumber(MyEdgeUtil.getEdgesGreaterThanZero()) + "[" +
                        MyMathUtil.twoDecimalFormat(((float) MyEdgeUtil.getEdgesGreaterThanZero() / MySequentialGraphVars.g.getEdgeCount()) * 100) + "]" + "\n";
                String statStr = "<html><body>NO. OF NODES: " + MyMathUtil.getCommaSeperatedNumber(MySequentialGraphVars.getSequentialGraphViewer().selectedSingleNodePredecessors.size()+ MySequentialGraphVars.getSequentialGraphViewer().selectedSingleNodeSuccessors.size()+1) + "   " +
                        "   <br>NODE: " + MySequentialGraphSysUtil.decodeNodeName(MySequentialGraphVars.getSequentialGraphViewer().selectedNode.getName()) +
                        "   <br>" + cont +
                        "   <br>" + inCont +
                        "   <br>" + outCont +
                        "   <br>" + uniqCont +
                        "   <br>" + ps +
                        "   <br>" + ss +
                        "   <br>" + numOfEdges;
                if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() > 1) {
                    String maxInVal = "MAX. IN-NODES: " + MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MySelectedNodeUtil.getMaximumInNodeValueFromSelectedNodePredecessors())) + "\n";
                    String minInVal = "MIN. IN-NODES: " + MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MySelectedNodeUtil.getMinimumInNodeValueFromSelectedNodePredecessors())) + "\n";
                    String maxOutVal = "MAX. OUT-VALUE: " + MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MySelectedNodeUtil.getMaximumOutNodeValueFromSelectedNodeSuccessors())) + "\n";
                    String minOutVal = "MIN. OUT-VALUE: " + MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MySelectedNodeUtil.getMinimumNodeOutValueFromSelectedNodeSuccessors())) + "\n";
                    String avgInVal = "AVG. IN-VALUE: " + MySelectedNodeUtil.getSelectedNodeAvgInValue();
                    String avgOutVal = "AVG. OUT-VALUE: " + MySelectedNodeUtil.getSelectedNodeAvgOutValue();
                    statStr += "    <br>" + minInVal + "   <br>" + maxInVal + "   <br>" + avgInVal + "   <br>" + minOutVal + "   <br>" + maxOutVal + "   <br>" + avgOutVal;
                }
                statStr += "<br>AVERAGE SHORTEST DISTANCE: " + MyMathUtil.twoDecimalFormat(MySequentialGraphVars.getSequentialGraphViewer().selectedNode.getAverageShortestDistance());
                statStr = statStr + "</body></html>";
                setToolTipText(statStr);
            } else {
                String numNs = "<html><body> NODES: " + MyMathUtil.getCommaSeperatedNumber(MyNodeUtil.getGreaterThanZeroNodeValueCount()) + "[" +
                        MyMathUtil.oneDecimalFormat(((double) MyNodeUtil.getGreaterThanZeroNodeValueCount() / MySequentialGraphVars.g.getVertexCount()) * 100) + "%]";
                String numEs = "<br> EDGES: " + MyMathUtil.getCommaSeperatedNumber(MyEdgeUtil.getOnEdgeCount()) + "[" + MyMathUtil.oneDecimalFormat(((double) MyEdgeUtil.getOnEdgeCount() / MySequentialGraphVars.g.getEdgeCount()) * 100) + "%] ";
                String red = "<br> RED NODES: " + MyMathUtil.getCommaSeperatedNumber(MyNodeUtil.getRedNodeCount());
                String blue = "<br> BLUE NODES: " + MyMathUtil.getCommaSeperatedNumber(MyNodeUtil.getBlueNodeCount());
                String green = "<br> GREEN NODES: " + MyMathUtil.getCommaSeperatedNumber(MyNodeUtil.getGreenNodeCount());
                String avgInCont = "<br> AVG. IN-CONTRIBUTION: " + MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getAverageInContribution()));
                String avgOutCont = "<br> AVG. OUT-CONTRIBUTION: " + MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getAverageOutContribution()));
                String avgPredCount = "<br> AVG. PREDECESSORS: " + MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getAveragePredecessorCount()));
                String avgSuccCount = "<br> AVG. SUCCESSORS: " + MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getAverageSuccessorCount()));
                String maxNodeVal = "<br> MAX. NODE VALUE: " + MySequentialGraphSysUtil.formatAverageValue(String.valueOf(MyNodeUtil.getMaximumNodeValue()));
                String avgNodeVal = "<br> AVG. NODE VALUE: " + MyNodeUtil.getAverageNodeValue() + "\n";
                String minNodeVal = "<br> MIN. NODE VALUE: " + MySequentialGraphSysUtil.formatAverageValue(String.valueOf(MyNodeUtil.getMinimumNodeValue()));
                String stdNodeVal = "<br> STD. NODE VALUE: " + MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getNodeValueStandardDeviation()));
                String statStr = numNs + "  " + numEs + "  " + red + ", " + blue + ", " + green + "   " + maxNodeVal + "  " + minNodeVal + "  " + avgNodeVal + "  " + stdNodeVal + "  " + avgInCont + "  " + avgOutCont + "  " + avgPredCount + "  " + avgSuccCount;
                if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() > 1) {
                    String maxEVal = "<br> MAX. EDGE VALUE: " + MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyEdgeUtil.getMaximumEdgeValue()));
                    String minEVal = "<br> MIN. EDGE VALUE: " + MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyEdgeUtil.getMinimumEdgeValue()));
                    String avgEVal = "<br> AVG. EDGE VALUE: " + MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyEdgeUtil.getAverageEdgeValue()));
                    String stdEval = "<br> STD. EDGE VALUE: " + MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyEdgeUtil.getEdgeValueStandardDeviation()));
                    statStr += "  " + maxEVal + "  " + minEVal + "  " + avgEVal + "  " + stdEval + " ";
                }
                statStr += "<br>AVERAGE SHORTEST DISTANCE: " + MyMathUtil.twoDecimalFormat(MySequentialGraphSysUtil.getAverageShortestDistance());
                statStr = statStr + "</body></html>";
                setToolTipText(statStr);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
