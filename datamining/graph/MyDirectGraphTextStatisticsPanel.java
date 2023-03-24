package datamining.graph;


import datamining.utils.MyMathUtil;
import datamining.utils.system.MySysUtil;
import datamining.utils.system.MyVars;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class MyDirectGraphTextStatisticsPanel
extends JPanel {

    private MyDirectNode selectedSingleNode;

    public MyDirectGraphTextStatisticsPanel() {
        this.setTextStatistics();
    }

    public void setTextStatistics() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                if (MyVars.getDirectGraphViewer().selectedSingleNode != null) {
                    selectedSingleNode = MyVars.getDirectGraphViewer().selectedSingleNode;;
                    setSelectedSingleNodeTextStatistics();
                } else if (MyVars.getDirectGraphViewer().multiNodes != null) {
                    setSelectedMultiNodeLevelTextStatistics();
                } else {
                    setTopLevelTextStatistics();
                }
            }
        });
    }

    private void setSelectedSingleNodeTextStatistics() {
        try {
            new Thread(new Runnable() {
                @Override public void run() {
                    removeAll();
                    setBackground(Color.WHITE);
                    setLayout(new FlowLayout(FlowLayout.LEFT, 5, 3));

                    Rectangle r = MyVars.main.getBounds();
                    int h = (int) r.getHeight();
                    setBounds(5, h - 150, 1650, 30);

                    Set<MyDirectNode> intersectedNodeSet = new HashSet<>();
                    intersectedNodeSet.addAll(MyVars.directMarkovChain.getPredecessors(MyVars.getDirectGraphViewer().selectedSingleNode));
                    intersectedNodeSet.retainAll(MyVars.directMarkovChain.getSuccessors(MyVars.getDirectGraphViewer().selectedSingleNode));


                    JLabel numOfIntersectedNodeLabel = new JLabel();
                    numOfIntersectedNodeLabel.setFont(MyVars.f_pln_13);
                    numOfIntersectedNodeLabel.setBackground(Color.WHITE);
                    numOfIntersectedNodeLabel.setForeground(Color.BLACK);
                    String numOfIntersectedNodeValue = MyMathUtil.getCommaSeperatedNumber(intersectedNodeSet.size()) + "[" + MyMathUtil.twoDecimalFormat((double)intersectedNodeSet.size()/(MyVars.getDirectGraphViewer().selectedSingleNodePredecessorSet.size() + MyVars.getDirectGraphViewer().selectedSingleNodeSuccessorSet.size())) + "]";
                    numOfIntersectedNodeLabel.setText("INTER. N.: " + numOfIntersectedNodeValue);

                    JLabel selectedNodeLabel = new JLabel();
                    selectedNodeLabel.setFont(MyVars.f_pln_13);
                    selectedNodeLabel.setBackground(Color.WHITE);
                    selectedNodeLabel.setForeground(Color.BLACK);
                    String selectNodeValue = selectedSingleNode.getName();
                    selectedNodeLabel.setText("N.: " + selectNodeValue);

                    JLabel numOfEdgeLabel = new JLabel();
                    numOfEdgeLabel.setFont(MyVars.tahomaPlainFont13);
                    numOfEdgeLabel.setBackground(Color.WHITE);
                    numOfEdgeLabel.setForeground(Color.BLACK);
                    String numOfEdgeValue = MyMathUtil.getCommaSeperatedNumber(MyVars.directMarkovChain.getOutEdges(selectedSingleNode).size() + MyVars.directMarkovChain.getInEdges(selectedSingleNode).size());
                    String numOfEdgePercent = MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat((double) MyVars.directMarkovChain.getNeighborCount(selectedSingleNode)/MyVars.directMarkovChain.getEdgeCount()));
                    numOfEdgeLabel.setText("E.: " + numOfEdgeValue + "[" + numOfEdgePercent + "]");

                    JLabel numOfNodeLabel = new JLabel();
                    numOfNodeLabel.setFont(MyVars.tahomaPlainFont13);
                    numOfNodeLabel.setBackground(Color.WHITE);
                    numOfNodeLabel.setForeground(Color.BLACK);
                    String numberOfNodeValue = MyMathUtil.getCommaSeperatedNumber(MyVars.directMarkovChain.getNeighborCount(selectedSingleNode));
                    String numberOfNodePercent = MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat((double) MyVars.directMarkovChain.getNeighborCount(selectedSingleNode)/MyVars.directMarkovChain.getVertexCount()));
                    numOfNodeLabel.setText("N.: " + numberOfNodeValue + "[" + numberOfNodePercent + "]");

                    JLabel successorCountLabel = new JLabel();
                    successorCountLabel.setFont(MyVars.tahomaPlainFont13);
                    successorCountLabel.setBackground(Color.WHITE);
                    successorCountLabel.setForeground(Color.BLACK);
                    String successorCountValue = MyMathUtil.getCommaSeperatedNumber(MyVars.directMarkovChain.getSuccessorCount(selectedSingleNode));
                    String successorPercent = MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat((double) MyVars.directMarkovChain.getSuccessorCount(selectedSingleNode)/MyVars.directMarkovChain.getNeighborCount(selectedSingleNode)));
                    successorCountLabel.setText("S.: " + successorCountValue + "[" + successorPercent + "]");

                    JLabel predecessorCountLabel = new JLabel();
                    predecessorCountLabel.setFont(MyVars.tahomaPlainFont13);
                    predecessorCountLabel.setBackground(Color.WHITE);
                    predecessorCountLabel.setForeground(Color.BLACK);
                    String predecessorCountValue = MyMathUtil.getCommaSeperatedNumber(MyVars.directMarkovChain.getPredecessorCount(MyVars.getDirectGraphViewer().selectedSingleNode));
                    String predecessorPercent = MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat((double) MyVars.directMarkovChain.getPredecessorCount(selectedSingleNode)/MyVars.directMarkovChain.getNeighborCount(selectedSingleNode)));
                    predecessorCountLabel.setText("P.: " + predecessorCountValue + "[" + predecessorPercent + "]");

                    JLabel totalInEdgeValueLabel = new JLabel();
                    totalInEdgeValueLabel.setFont(MyVars.tahomaPlainFont13);
                    totalInEdgeValueLabel.setBackground(Color.WHITE);
                    totalInEdgeValueLabel.setForeground(Color.BLACK);
                    String totalInEdgeValue = MyVars.directMarkovChain.getTotalInEdgeValue();
                    totalInEdgeValueLabel.setText("T. IN-E. V.: " + totalInEdgeValue);

                    JLabel totalOutEdgeValueLabel = new JLabel();
                    totalOutEdgeValueLabel.setFont(MyVars.tahomaPlainFont13);
                    totalOutEdgeValueLabel.setBackground(Color.WHITE);
                    totalOutEdgeValueLabel.setForeground(Color.BLACK);
                    String totalOutEdgeValue = MyVars.directMarkovChain.getTotalOutEdgeValue();
                    totalOutEdgeValueLabel.setText("T. OUT-E. V.: " + totalOutEdgeValue);

                    JLabel avgInEdgeValueLabel = new JLabel();
                    avgInEdgeValueLabel.setFont(MyVars.tahomaPlainFont13);
                    avgInEdgeValueLabel.setBackground(Color.WHITE);
                    avgInEdgeValueLabel.setForeground(Color.BLACK);
                    String avgInEdgeValue = MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyVars.directMarkovChain.getAverageInEdgeValueForSelectedNode()));
                    avgInEdgeValueLabel.setText("AVG. IN-E. V.: " + avgInEdgeValue);

                    JLabel avgOutEdgeValueLabel = new JLabel();
                    avgOutEdgeValueLabel.setFont(MyVars.tahomaPlainFont13);
                    avgOutEdgeValueLabel.setBackground(Color.WHITE);
                    avgOutEdgeValueLabel.setForeground(Color.BLACK);
                    String avgOutEdgeValue = MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyVars.directMarkovChain.getAverageOutEdgeValueForSelectedNode()));
                    avgOutEdgeValueLabel.setText("AVG. OUT-E. V.: " + avgOutEdgeValue);


                    JLabel emptyhLabel0 = new JLabel(" ");
                    JLabel emptyLabel1 = new JLabel(" ");
                    emptyLabel1.setFont(MyVars.tahomaPlainFont9);
                    JLabel emptyLabel2 = new JLabel(" ");
                    emptyLabel2.setFont(MyVars.tahomaPlainFont9);
                    JLabel emptyLabel3 = new JLabel(" ");
                    emptyLabel3.setFont(MyVars.tahomaPlainFont9);
                    JLabel emptyLabel4 = new JLabel(" ");
                    emptyLabel4.setFont(MyVars.tahomaPlainFont9);
                    JLabel emptyLabel5 = new JLabel(" ");
                    emptyLabel5.setFont(MyVars.tahomaPlainFont9);
                    JLabel emptyLabel6 = new JLabel(" ");
                    emptyLabel6.setFont(MyVars.tahomaPlainFont9);
                    JLabel emptyLabel7 = new JLabel(" ");
                    emptyLabel7.setFont(MyVars.tahomaPlainFont9);
                    JLabel emptyLabel8 = new JLabel(" ");
                    emptyLabel8.setFont(MyVars.tahomaPlainFont9);
                    JLabel emptyLabel9 = new JLabel(" ");
                    emptyLabel9.setFont(MyVars.tahomaPlainFont9);
                    JLabel emptyLabel10 = new JLabel(" ");
                    emptyLabel10.setFont(MyVars.tahomaPlainFont9);

                    //add(selectedNodeLabel);
                    //MyVars.main.getDirectMarkovChainDashBoard().nodeListTableNodeTableNodeSearchTxt.setText(selectedNodeLabel.getText());

                    add(emptyLabel1);
                    add(numOfNodeLabel);

                    add(emptyLabel2);
                    add(numOfEdgeLabel);

                    add(emptyLabel3);
                    add(predecessorCountLabel);

                    add(emptyLabel4);
                    add(successorCountLabel);

                    add(emptyLabel6);
                    add(numOfIntersectedNodeLabel);

                    add(emptyLabel9);
                    add(avgInEdgeValueLabel);

                    add(emptyLabel10);
                    add(avgOutEdgeValueLabel);
                    revalidate();
                    repaint();

                    setToolTipText("<html><body>" +
                            "NODE: " + selectNodeValue +
                            "<br>TOTAL NODES: " + numberOfNodeValue +
                            "<br>TOTAL EDGES: " + numOfEdgeValue +
                            "<br>TOTAL SUCCESSORS.: " + successorCountValue +
                            "<br>TOTAL PREDECESSORS: " + predecessorCountValue +
                            "<br>TOTAL IN-EDGE VALUE: " + totalInEdgeValue +
                            "<br>TOTAL OUT-EDGE VALUE: " + totalOutEdgeValue +
                            "<br>AVG. IN-EDGE VALUE: " + avgInEdgeValue +
                            "<br>AVG. OUT-EDGE VALUE: " + avgOutEdgeValue +
                            "<br>NO. OF INTERSECTED NODES: " + numOfIntersectedNodeValue +
                            "</body></html>");
                }
            }).start();
        } catch (Exception ex) {}
    }

    public void setMouseHoveredNodeTextStatistics() {
        try {
            new Thread(new Runnable() {
                @Override public void run() {
                    removeAll();
                    setBackground(Color.WHITE);
                    setLayout(new FlowLayout(FlowLayout.LEFT, 5, 3));

                    Rectangle r = MyVars.main.getBounds();
                    int h = (int) r.getHeight();
                    setBounds(5, h - 150, 1650, 30);

                    Set<MyDirectNode> intersectedNodeSet = new HashSet<>();
                    intersectedNodeSet.addAll(MyVars.directMarkovChain.getPredecessors(MyVars.getDirectGraphViewer().graphMouseMotionListener.hoveredNode));
                    intersectedNodeSet.retainAll(MyVars.directMarkovChain.getSuccessors(MyVars.getDirectGraphViewer().graphMouseMotionListener.hoveredNode));

                    Set<MyDirectNode> neighborNodeSet = new HashSet<>();
                    neighborNodeSet.addAll(MyVars.directMarkovChain.getSuccessors(MyVars.getDirectGraphViewer().graphMouseMotionListener.hoveredNode));
                    neighborNodeSet.addAll(MyVars.directMarkovChain.getPredecessors(MyVars.getDirectGraphViewer().graphMouseMotionListener.hoveredNode));

                    JLabel numOfIntersectedNodeLabel = new JLabel();
                    numOfIntersectedNodeLabel.setFont(MyVars.f_pln_13);
                    numOfIntersectedNodeLabel.setBackground(Color.WHITE);
                    numOfIntersectedNodeLabel.setForeground(Color.BLACK);
                    String numOfIntersectedNodeValue = MyMathUtil.getCommaSeperatedNumber(intersectedNodeSet.size()) + "[" + MyMathUtil.twoDecimal(((double)intersectedNodeSet.size()/neighborNodeSet.size())*100)+ "]";
                    numOfIntersectedNodeLabel.setText("INTER. N.: " + numOfIntersectedNodeValue);

                    JLabel selectedNodeLabel = new JLabel();
                    selectedNodeLabel.setFont(MyVars.f_pln_13);
                    selectedNodeLabel.setBackground(Color.WHITE);
                    selectedNodeLabel.setForeground(Color.BLACK);
                    String selectNodeValue = MyVars.getDirectGraphViewer().graphMouseMotionListener.hoveredNode.getName();
                    selectedNodeLabel.setText(selectNodeValue);

                    JLabel numOfEdgeLabel = new JLabel();
                    numOfEdgeLabel.setFont(MyVars.tahomaPlainFont13);
                    numOfEdgeLabel.setBackground(Color.WHITE);
                    numOfEdgeLabel.setForeground(Color.BLACK);
                    String numOfEdgeValue = MyMathUtil.getCommaSeperatedNumber(MyVars.directMarkovChain.getOutEdges(MyVars.getDirectGraphViewer().graphMouseMotionListener.hoveredNode).size() + MyVars.directMarkovChain.getInEdges(MyVars.getDirectGraphViewer().graphMouseMotionListener.hoveredNode).size());
                    String numOfEdgePercent = MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat((double) MyVars.directMarkovChain.getNeighborCount(MyVars.getDirectGraphViewer().graphMouseMotionListener.hoveredNode)/MyVars.directMarkovChain.getEdgeCount()));
                    numOfEdgeLabel.setText("E.: " + numOfEdgeValue + "[" + numOfEdgePercent + "]");

                    JLabel numOfNodeLabel = new JLabel();
                    numOfNodeLabel.setFont(MyVars.tahomaPlainFont13);
                    numOfNodeLabel.setBackground(Color.WHITE);
                    numOfNodeLabel.setForeground(Color.BLACK);
                    String numberOfNodeValue = MyMathUtil.getCommaSeperatedNumber(MyVars.directMarkovChain.getNeighborCount(MyVars.getDirectGraphViewer().graphMouseMotionListener.hoveredNode));
                    String numberOfNodePercent = MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat((double) MyVars.directMarkovChain.getNeighborCount(MyVars.getDirectGraphViewer().graphMouseMotionListener.hoveredNode)/MyVars.directMarkovChain.getVertexCount()));
                    numOfNodeLabel.setText("N.: " + numberOfNodeValue + "[" + numberOfNodePercent + "]");

                    JLabel successorCountLabel = new JLabel();
                    successorCountLabel.setFont(MyVars.tahomaPlainFont13);
                    successorCountLabel.setBackground(Color.WHITE);
                    successorCountLabel.setForeground(Color.BLACK);
                    String successorCountValue = MyMathUtil.getCommaSeperatedNumber(MyVars.directMarkovChain.getSuccessorCount(MyVars.getDirectGraphViewer().graphMouseMotionListener.hoveredNode));
                    String successorPercent = MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat((double) MyVars.directMarkovChain.getSuccessorCount(MyVars.getDirectGraphViewer().graphMouseMotionListener.hoveredNode)/MyVars.directMarkovChain.getNeighborCount(MyVars.getDirectGraphViewer().graphMouseMotionListener.hoveredNode)));
                    successorCountLabel.setText("S.: " + successorCountValue + "[" + successorPercent + "]");

                    JLabel predecessorCountLabel = new JLabel();
                    predecessorCountLabel.setFont(MyVars.tahomaPlainFont13);
                    predecessorCountLabel.setBackground(Color.WHITE);
                    predecessorCountLabel.setForeground(Color.BLACK);
                    String predecessorCountValue = MyMathUtil.getCommaSeperatedNumber(MyVars.directMarkovChain.getPredecessorCount(MyVars.getDirectGraphViewer().graphMouseMotionListener.hoveredNode));
                    String predecessorPercent = MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat((double) MyVars.directMarkovChain.getPredecessorCount(MyVars.getDirectGraphViewer().graphMouseMotionListener.hoveredNode)/MyVars.directMarkovChain.getNeighborCount(MyVars.getDirectGraphViewer().graphMouseMotionListener.hoveredNode)));
                    predecessorCountLabel.setText("P.: " + predecessorCountValue + "[" + predecessorPercent + "]");

                    JLabel emptyhLabel0 = new JLabel(" ");
                    JLabel emptyLabel1 = new JLabel(" ");
                    emptyLabel1.setFont(MyVars.tahomaPlainFont9);
                    JLabel emptyLabel2 = new JLabel(" ");
                    emptyLabel2.setFont(MyVars.tahomaPlainFont9);
                    JLabel emptyLabel3 = new JLabel(" ");
                    emptyLabel3.setFont(MyVars.tahomaPlainFont9);
                    JLabel emptyLabel4 = new JLabel(" ");
                    emptyLabel4.setFont(MyVars.tahomaPlainFont9);
                    JLabel emptyLabel5 = new JLabel(" ");
                    emptyLabel5.setFont(MyVars.tahomaPlainFont9);
                    JLabel emptyLabel6 = new JLabel(" ");
                    emptyLabel6.setFont(MyVars.tahomaPlainFont9);

                    //add(selectedNodeLabel);

                    //add(emptyLabel1);
                    add(numOfNodeLabel);

                    add(emptyLabel2);
                    add(numOfEdgeLabel);

                    add(emptyLabel3);
                    add(predecessorCountLabel);

                    add(emptyLabel4);
                    add(successorCountLabel);
                    add(emptyLabel5);
                    add(numOfIntersectedNodeLabel);

                    revalidate();
                    repaint();

                    setToolTipText("<html><body>" +
                            "NODE: " + selectNodeValue +
                            "<br>TOTAL NODES: " + numberOfNodeValue +
                            "<br>TOTAL EDGES: " + numOfEdgeValue +
                            "<br>TOTAL SUCCESSORS.: " + successorCountValue +
                            "<br>TOTAL PREDECESSORS: " + predecessorCountValue +
                            "</body></html>");
                }
            }).start();
        } catch (Exception ex) {}
    }

    private void setSelectedMultiNodeLevelTextStatistics() {
        try {
            removeAll();
            setBackground(Color.WHITE);
            setLayout(new FlowLayout(FlowLayout.LEFT, 5, 3));

            Set<MyDirectNode> intersectedNodes = new HashSet<>();
            intersectedNodes.addAll(MyVars.getDirectGraphViewer().multiNodePredecessorSet);
            intersectedNodes.retainAll(MyVars.getDirectGraphViewer().multiNodeSuccessorSet);

            Rectangle r = MyVars.main.getBounds();
            int h = (int) r.getHeight();
            setBounds(5, h - 150, 1650, 30);

            JLabel numOfMultiNodeLabel = new JLabel();
            numOfMultiNodeLabel.setFont(MyVars.tahomaPlainFont13);
            numOfMultiNodeLabel.setBackground(Color.WHITE);
            numOfMultiNodeLabel.setForeground(Color.BLACK);
            String numOfMultiNodeValue = MyMathUtil.getCommaSeperatedNumber(MyVars.getDirectGraphViewer().multiNodes.size());
            numOfMultiNodeLabel.setText("S. N.: " + numOfMultiNodeValue);

            JLabel totalNodeLabel = new JLabel();
            long totalNode = MyVars.getDirectGraphViewer().multiNodes.size() + MyVars.getDirectGraphViewer().multiNodePredecessorSet.size() + MyVars.getDirectGraphViewer().multiNodeSuccessorSet.size();
            totalNodeLabel.setFont(MyVars.tahomaPlainFont13);
            totalNodeLabel.setBackground(Color.WHITE);
            totalNodeLabel.setForeground(Color.BLACK);
            String totalNodeValue = MyMathUtil.getCommaSeperatedNumber(totalNode) + "[" + MyMathUtil.twoDecimalFormat((double)totalNode/MyVars.directMarkovChain.getVertexCount()) + "]";
            totalNodeLabel.setText("N.: " + totalNodeValue);

            JLabel numOfMultiNodeEdgesLabel = new JLabel();
            numOfMultiNodeEdgesLabel.setFont(MyVars.tahomaPlainFont13);
            numOfMultiNodeEdgesLabel.setBackground(Color.WHITE);
            numOfMultiNodeEdgesLabel.setForeground(Color.BLACK);
            long totalEdge = 0;
            Set<MyDirectEdge> edges = new HashSet<>();
            for (MyDirectNode n : MyVars.getDirectGraphViewer().multiNodes) {
                edges.addAll(MyVars.directMarkovChain.getInEdges(n));
                edges.addAll(MyVars.directMarkovChain.getOutEdges(n));
            }
            totalEdge = edges.size();
            String numOfEdgesValue = MyMathUtil.getCommaSeperatedNumber(totalEdge) + "[" + (MyMathUtil.twoDecimalFormat((double)totalEdge/MyVars.directMarkovChain.getEdgeCount())) + "]";
            numOfMultiNodeEdgesLabel.setText("E.: " + numOfEdgesValue);

            JLabel numOfSuccessorLabel = new JLabel();
            numOfSuccessorLabel.setFont(MyVars.tahomaPlainFont13);
            numOfSuccessorLabel.setBackground(Color.WHITE);
            numOfSuccessorLabel.setForeground(Color.BLACK);
            String numOfSuccessorValue = MyMathUtil.getCommaSeperatedNumber(MyVars.getDirectGraphViewer().multiNodeSuccessorSet.size());
            numOfSuccessorValue += "[" + MyMathUtil.twoDecimalFormat((double)MyVars.getDirectGraphViewer().multiNodeSuccessorSet.size()/totalNode) + "]";
            numOfSuccessorLabel.setText("S.: " + numOfSuccessorValue);

            JLabel numOfPredecessorLabel = new JLabel();
            numOfPredecessorLabel.setFont(MyVars.tahomaPlainFont13);
            numOfPredecessorLabel.setBackground(Color.WHITE);
            numOfPredecessorLabel.setForeground(Color.BLACK);
            String numOfPredecessorValue = MyMathUtil.getCommaSeperatedNumber(MyVars.getDirectGraphViewer().multiNodePredecessorSet.size());
            numOfPredecessorValue += "[" + MyMathUtil.twoDecimalFormat((double)MyVars.getDirectGraphViewer().multiNodePredecessorSet.size()/totalNode) + "]";
            numOfPredecessorLabel.setText("P.: " + numOfPredecessorValue);

            JLabel numOfSharedPredecessorLabel = new JLabel();
            numOfSharedPredecessorLabel.setFont(MyVars.tahomaPlainFont13);
            numOfSharedPredecessorLabel.setBackground(Color.WHITE);
            numOfSharedPredecessorLabel.setForeground(Color.BLACK);
            String numOfSharedPredecessorValue = MyMathUtil.getCommaSeperatedNumber(MyVars.getDirectGraphViewer().multiNodeSharedPredecessorSet.size());
            numOfSharedPredecessorValue += "[" + MyMathUtil.twoDecimalFormat((double)MyVars.getDirectGraphViewer().multiNodeSharedPredecessorSet.size()/MyVars.getDirectGraphViewer().multiNodePredecessorSet.size()) + "]";
            numOfSharedPredecessorLabel.setText("SHARED S.: " + numOfSharedPredecessorValue);

            JLabel numOfSharedSuccessorLabel = new JLabel();
            numOfSharedSuccessorLabel.setFont(MyVars.tahomaPlainFont13);
            numOfSharedSuccessorLabel.setBackground(Color.WHITE);
            numOfSharedSuccessorLabel.setForeground(Color.BLACK);
            String numOfSharedSuccessorValue = MyMathUtil.getCommaSeperatedNumber(MyVars.getDirectGraphViewer().multiNodeSharedSuccessorSet.size());
            numOfSharedSuccessorValue += "[" + MyMathUtil.twoDecimalFormat((double)MyVars.getDirectGraphViewer().multiNodeSharedSuccessorSet.size()/MyVars.getDirectGraphViewer().multiNodeSuccessorSet.size()) + "]";
            numOfSharedSuccessorLabel.setText("SHARED S.: " + numOfSharedSuccessorValue);

            JLabel numOfIntersectedNodeLabel = new JLabel();
            numOfIntersectedNodeLabel.setFont(MyVars.tahomaPlainFont13);
            numOfIntersectedNodeLabel.setBackground(Color.WHITE);
            numOfIntersectedNodeLabel.setForeground(Color.BLACK);
            String numOfIntersectedNodeValue = MyMathUtil.getCommaSeperatedNumber(intersectedNodes.size());
            numOfIntersectedNodeValue += "[" + MyMathUtil.twoDecimalFormat((double)intersectedNodes.size()/(MyVars.getDirectGraphViewer().multiNodeSuccessorSet.size()+MyVars.getDirectGraphViewer().multiNodePredecessorSet.size())) + "]";
            numOfIntersectedNodeLabel.setText("INTER. N.: " + numOfIntersectedNodeValue);

            totalNodeLabel.setText(totalNodeLabel.getText() + "  ");
            numOfMultiNodeLabel.setText(numOfMultiNodeLabel.getText() + "  ");
            numOfMultiNodeEdgesLabel.setText(numOfMultiNodeEdgesLabel.getText() + "  ");
            numOfPredecessorLabel.setText(numOfPredecessorLabel.getText() + "  ");
            numOfSuccessorLabel.setText(numOfSuccessorLabel.getText() + "  ");
            numOfSharedPredecessorLabel.setText(numOfSharedPredecessorLabel.getText() + "  ");
            numOfSharedSuccessorLabel.setText(numOfSharedSuccessorLabel.getText() + "  ");

            add(numOfMultiNodeLabel);
            add(totalNodeLabel);
            add(numOfMultiNodeEdgesLabel);
            add(numOfPredecessorLabel);
            add(numOfSuccessorLabel);
            add(numOfSharedPredecessorLabel);
            add(numOfSharedSuccessorLabel);
            add(numOfIntersectedNodeLabel);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setTopLevelTextStatistics() {
        this.removeAll();
        this.setBackground(Color.WHITE);
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));

        Rectangle r = MyVars.main.getBounds();
        int h = (int) r.getHeight();
        this.setBounds(5, h - 150, 1800, 30);

        JLabel numOfNetworkLabel = new JLabel();
        numOfNetworkLabel.setFont(MyVars.tahomaPlainFont13);
        numOfNetworkLabel.setBackground(Color.WHITE);
        numOfNetworkLabel.setForeground(Color.BLACK);
        String numOfNetworkValue = MyMathUtil.getCommaSeperatedNumber(MyVars.connectedComponentCountsByGraph.size());
        numOfNetworkLabel.setText("N.: " + numOfNetworkValue);

        JLabel maxNodeContLabel = new JLabel();
        maxNodeContLabel.setFont(MyVars.tahomaPlainFont13);
        maxNodeContLabel.setBackground(Color.WHITE);
        maxNodeContLabel.setForeground(Color.BLACK);
        String maxNodeContValue = MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyVars.directMarkovChain.getMaxNodeValue())).split("\\.")[0];
        maxNodeContLabel.setText("MAX. N. V.: " + maxNodeContValue);

        JLabel minNodeContLabel = new JLabel();
        minNodeContLabel.setFont(MyVars.tahomaPlainFont13);
        minNodeContLabel.setBackground(Color.WHITE);
        minNodeContLabel.setForeground(Color.BLACK);
        String minNodeContValue = MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyVars.directMarkovChain.getMinNodeValue())).split("\\.")[0];
        minNodeContLabel.setText("MIN. N. V.: " + minNodeContValue);

        JLabel avgNodeContLabel = new JLabel();
        avgNodeContLabel.setFont(MyVars.tahomaPlainFont13);
        avgNodeContLabel.setBackground(Color.WHITE);
        avgNodeContLabel.setForeground(Color.BLACK);
        String avgNodeContValue = MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyVars.directMarkovChain.getAverageNodeValue())).split("\\.")[0];
        avgNodeContLabel.setText("AVG. N. V.: " + avgNodeContValue);

        JLabel nodeValueStdLabel = new JLabel();
        nodeValueStdLabel.setFont(MyVars.tahomaPlainFont13);
        nodeValueStdLabel.setBackground(Color.WHITE);
        nodeValueStdLabel.setForeground(Color.BLACK);
        String nodeValueStdValue = MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyVars.directMarkovChain.getNodeValueStandardDeviation())).split("\\.")[0];
        nodeValueStdLabel.setText("N. V. STD.: " + nodeValueStdValue);

        JLabel maxEdgeContLabel = new JLabel();
        maxEdgeContLabel.setFont(MyVars.tahomaPlainFont13);
        maxEdgeContLabel.setBackground(Color.WHITE);
        maxEdgeContLabel.setForeground(Color.BLACK);
        String maxEdgeContValue = MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyVars.directMarkovChain.getMaxEdgeValue())).split("\\.")[0];
        maxEdgeContLabel.setText("MAX. E. V.: " + maxEdgeContValue);

        JLabel minEdgeContLabel = new JLabel();
        minEdgeContLabel.setFont(MyVars.tahomaPlainFont13);
        minEdgeContLabel.setBackground(Color.WHITE);
        minEdgeContLabel.setForeground(Color.BLACK);
        String minEdgeContValue = MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyVars.directMarkovChain.getMinEdgeValue())).split("\\.")[0];
        minEdgeContLabel.setText("MIN. E. V.: " + minEdgeContValue);

        JLabel avgEdgeContLabel = new JLabel();
        avgEdgeContLabel.setFont(MyVars.tahomaPlainFont13);
        avgEdgeContLabel.setBackground(Color.WHITE);
        avgEdgeContLabel.setForeground(Color.BLACK);
        String avgEdgeContValue = MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyVars.directMarkovChain.getAverageEdgeValue())).split("\\.")[0];
        avgEdgeContLabel.setText("AVG. E. V.: " + avgEdgeContValue);

        JLabel edgeStdLabel = new JLabel();
        edgeStdLabel.setFont(MyVars.tahomaPlainFont13);
        edgeStdLabel.setBackground(Color.WHITE);
        edgeStdLabel.setForeground(Color.BLACK);
        String edgeValueStdValue = MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyVars.directMarkovChain.getEdgValueStandardDeviation())).split("\\.")[0];
        edgeStdLabel.setText("E. V. STD.: " + edgeValueStdValue);

        JLabel maxSucCntLabel = new JLabel();
        maxSucCntLabel.setFont(MyVars.tahomaPlainFont13);
        maxSucCntLabel.setBackground(Color.WHITE);
        maxSucCntLabel.setForeground(Color.BLACK);
        String maxSucCntValue = MyMathUtil.getCommaSeperatedNumber(MyVars.directMarkovChain.getMaxSuccessorCount());
        maxSucCntLabel.setText("MAX. S.: " + maxSucCntValue);

        JLabel avgSucCntLabel = new JLabel();
        avgSucCntLabel.setFont(MyVars.tahomaPlainFont13);
        avgSucCntLabel.setBackground(Color.WHITE);
        avgSucCntLabel.setForeground(Color.BLACK);
        String avgSucCntValue = MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyVars.directMarkovChain.getAverageSuccessors())).split("\\.")[0];
        avgSucCntLabel.setText("AVG. S.: " + avgSucCntValue);

        JLabel maxPredCntLabel = new JLabel();
        maxPredCntLabel.setFont(MyVars.tahomaPlainFont13);
        maxPredCntLabel.setBackground(Color.WHITE);
        maxPredCntLabel.setForeground(Color.BLACK);
        String maxPredCntValue = MyMathUtil.getCommaSeperatedNumber(MyVars.directMarkovChain.getMaxPredecessorCount());
        maxPredCntLabel.setText("MAX. P. C.: " + maxPredCntValue);

        JLabel avgPredCntLabel = new JLabel();
        avgPredCntLabel.setFont(MyVars.tahomaPlainFont13);
        avgPredCntLabel.setBackground(Color.WHITE);
        avgPredCntLabel.setForeground(Color.BLACK);
        String avgPredCntValue = MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyVars.directMarkovChain.getAveragePredecessors())).split("\\.")[0];
        avgPredCntLabel.setText("AVG. P.: " + avgPredCntValue);

        JLabel redNodeCntLabel = new JLabel();
        redNodeCntLabel.setFont(MyVars.tahomaPlainFont13);
        redNodeCntLabel.setBackground(Color.WHITE);
        redNodeCntLabel.setForeground(Color.BLACK);
        String redNodeCntValue = MySysUtil.getCommaSeperateString(MyVars.directMarkovChain.getRedNodeCount()) + "[" + MyMathUtil.twoDecimalFormat(((double)MyVars.directMarkovChain.getRedNodeCount()/MyVars.directMarkovChain.getVertexCount())) + "]";
        redNodeCntLabel.setText("R: " + redNodeCntValue);

        JLabel blueNodeCntLabel = new JLabel();
        blueNodeCntLabel.setFont(MyVars.tahomaPlainFont13);
        blueNodeCntLabel.setBackground(Color.WHITE);
        blueNodeCntLabel.setForeground(Color.BLACK);
        String blueNodeCntValue = MySysUtil.getCommaSeperateString(MyVars.directMarkovChain.getBlueNodeCount()) + "[" + MyMathUtil.twoDecimalFormat(((double)MyVars.directMarkovChain.getBlueNodeCount()/MyVars.directMarkovChain.getVertexCount())) + "]";;
        blueNodeCntLabel.setText("B: " + blueNodeCntValue);

        JLabel greenNodeCntLabel = new JLabel();
        greenNodeCntLabel.setFont(MyVars.tahomaPlainFont13);
        greenNodeCntLabel.setBackground(Color.WHITE);
        greenNodeCntLabel.setForeground(Color.BLACK);
        String greenNodeCntValue = MySysUtil.getCommaSeperateString(MyVars.directMarkovChain.getGreenNodeCount()) + "[" + MyMathUtil.twoDecimalFormat(((double)MyVars.directMarkovChain.getGreenNodeCount()/MyVars.directMarkovChain.getVertexCount())) + "]";;
        greenNodeCntLabel.setText("G: " + greenNodeCntValue);

        JLabel numOfNodes = new JLabel();
        numOfNodes.setFont(MyVars.tahomaPlainFont13);
        numOfNodes.setBackground(Color.WHITE);
        numOfNodes.setForeground(Color.BLACK);
        String numOfNodesValue = MyMathUtil.getCommaSeperatedNumber(MyVars.directMarkovChain.getVertexCount());
        numOfNodes.setText("N.: " + numOfNodesValue);

        JLabel numOfEdges = new JLabel();
        numOfEdges.setFont(MyVars.tahomaPlainFont13);
        numOfEdges.setBackground(Color.WHITE);
        numOfEdges.setForeground(Color.BLACK);
        String numeOfEdgesValue = MyMathUtil.getCommaSeperatedNumber(MyVars.directMarkovChain.getEdgeCount());
        numOfEdges.setText("E.: " + numeOfEdgesValue);

        JLabel emptyLabel1 = new JLabel(" ");
        emptyLabel1.setFont(MyVars.f_pln_10);
        JLabel emptyLabel2 = new JLabel(" ");
        emptyLabel2.setFont(MyVars.f_pln_10);
        JLabel emptyLabel3 = new JLabel(" ");
        emptyLabel3.setFont(MyVars.f_pln_10);
        JLabel emptyLabel4 = new JLabel(" ");
        emptyLabel4.setFont(MyVars.f_pln_10);
        JLabel emptyLabel5 = new JLabel(" ");
        emptyLabel5.setFont(MyVars.f_pln_10);
        JLabel emptyLabel6 = new JLabel(" ");
        emptyLabel6.setFont(MyVars.f_pln_10);
        JLabel emptyLabel7 = new JLabel(" ");
        emptyLabel7.setFont(MyVars.f_pln_10);
        JLabel emptyLabel8 = new JLabel(" ");
        emptyLabel8.setFont(MyVars.f_pln_10);
        JLabel emptyLabel9 = new JLabel(" ");
        emptyLabel9.setFont(MyVars.f_pln_10);
        JLabel emptyLabel10 = new JLabel(" ");
        emptyLabel10.setFont(MyVars.f_pln_10);
        JLabel emptyLabel11 = new JLabel(" ");
        emptyLabel11.setFont(MyVars.f_pln_10);
        JLabel emptyLabel12 = new JLabel(" ");
        emptyLabel12.setFont(MyVars.f_pln_10);
        JLabel emptyLabel13 = new JLabel(" ");
        emptyLabel13.setFont(MyVars.f_pln_10);
        JLabel emptyLabel14 = new JLabel(" ");
        emptyLabel14.setFont(MyVars.f_pln_10);
        JLabel emptyLabel15 = new JLabel(" ");
        emptyLabel15.setFont(MyVars.f_pln_10);
        JLabel emptyLabel16 = new JLabel(" ");
        emptyLabel16.setFont(MyVars.f_pln_10);
        JLabel emptyLabel17 = new JLabel(" ");
        emptyLabel17.setFont(MyVars.f_pln_10);
        JLabel emptyLabel18 = new JLabel(" ");
        emptyLabel18.setFont(MyVars.f_pln_10);
        JLabel emptyLabel19 = new JLabel(" ");
        emptyLabel19.setFont(MyVars.f_pln_10);
        JLabel emptyLabel20 = new JLabel(" ");
        emptyLabel20.setFont(MyVars.f_pln_10);

        //this.add(numOfNetworkLabel);
        //this.add(emptyLabel15);
        this.add(numOfNodes);
        this.add(emptyLabel19);
        this.add(numOfEdges);
        this.add(emptyLabel13);
        this.add(avgNodeContLabel);
        this.add(emptyLabel6);
        this.add(nodeValueStdLabel);
        this.add(emptyLabel14);
        this.add(avgEdgeContLabel);
        this.add(emptyLabel4);
        this.add(edgeStdLabel);
        this.add(emptyLabel8);
        this.add(redNodeCntLabel);
        this.add(emptyLabel9);
        this.add(blueNodeCntLabel);
        this.add(emptyLabel10);
        this.add(greenNodeCntLabel);
        setTextStatisticsToolTipText(numOfNodesValue, numeOfEdgesValue, maxNodeContValue, minNodeContValue, avgNodeContValue,
                nodeValueStdValue, maxEdgeContValue, minEdgeContValue, avgEdgeContValue,
                edgeValueStdValue, maxPredCntValue, avgPredCntValue, maxSucCntValue, avgSucCntValue,
                redNodeCntValue, blueNodeCntValue, greenNodeCntValue);

        revalidate();
        repaint();
    }

    private void setTextStatisticsToolTipText(String numOfNodes, String numOfEdges, String maxNodeCont, String minNodeCont, String avgNodeContLabel, String nodeStd, String maxEdgeCont, String minEdgeCont,
                                              String avgEdgeCont, String edgeStd, String maxPredCcount, String avgPredCount, String maxSuccessorCount, String avgSuccessorCount,
                                              String redNodeCount, String blueNodeCount, String greenNodeCount) {
        String nodePercent = "[100%]";
        String toolTip = "<html><body>" +
                         "NO OF NETWORKS: " + MyMathUtil.getCommaSeperatedNumber(MyVars.connectedComponentCountsByGraph.size()) +
                         "<br>NODES: " + numOfNodes +
                         "<br>EDGES: " + numOfEdges +
                         "<br>MAX. NODE VALUE: " + maxNodeCont +
                         "<br>MIN. NODE VALUE: " + minNodeCont +
                         "<br>AVG. NODE VALUE: " + avgNodeContLabel +
                         "<br>STD. NODE VALUE: " + nodeStd +
                         "<br>MAX. EDGE VALUE: " + maxEdgeCont +
                         "<br>MIN. EDGE VALUE: " + minEdgeCont +
                         "<br>AVG. EDGE VALUE: " + avgEdgeCont +
                         "<br>STD. EDGE VALUE: " + edgeStd +
                         "<br>MAX. PREDECESSORS: " + maxPredCcount +
                         "<br>AVG PREDECESSSORS: " + avgPredCount +
                         "<br>MAX. SUCCESSORS: " + maxSuccessorCount +
                         "<br>AVG. SUCCESSORS: " + avgSuccessorCount +
                         "<br>RED NODES: " + redNodeCount +
                         "<br>BLUE NODES: " + blueNodeCount +
                         "<br>GREEN NODES: " + greenNodeCount + "</body></html>";
        this.setToolTipText(toolTip);
    }
}
