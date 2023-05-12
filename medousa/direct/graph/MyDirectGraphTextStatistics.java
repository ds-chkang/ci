package medousa.direct.graph;


import medousa.direct.utils.MyDirectGraphMathUtil;
import medousa.direct.utils.MyDirectGraphSysUtil;
import medousa.direct.utils.MyDirectGraphVars;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class MyDirectGraphTextStatistics
extends JPanel {

    private MyDirectNode selectedSingleNode;
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    double width = screenSize.getWidth();
    double height = screenSize.getHeight();

    public MyDirectGraphTextStatistics() {
        this.setTextStatistics();
    }

    public void setTextStatistics() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                if (MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode != null) {
                    selectedSingleNode = MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode;;
                    setSelectedSingleNodeTextStatistics();
                } else if (MyDirectGraphVars.getDirectGraphViewer().multiNodes != null) {
                    setMultiNodeLevelTextStatistics();
                } else if (MyDirectGraphVars.getDirectGraphViewer().isClustered) {
                    setClusterTextStatistics();
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

                    Rectangle r = MyDirectGraphVars.app.getBounds();
                    int h = (int) r.getHeight();
                    setBounds(5, h - 150, 1650, 30);

                    Set<MyDirectNode> intersectedNodeSet = new HashSet<>();
                    intersectedNodeSet.addAll(MyDirectGraphVars.directGraph.getPredecessors(MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode));
                    intersectedNodeSet.retainAll(MyDirectGraphVars.directGraph.getSuccessors(MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode));

                    JLabel numOfIntersectedNodeLabel = new JLabel();
                    numOfIntersectedNodeLabel.setFont(MyDirectGraphVars.f_pln_13);
                    numOfIntersectedNodeLabel.setBackground(Color.WHITE);
                    numOfIntersectedNodeLabel.setForeground(Color.BLACK);
                    String numOfIntersectedNodeValue = MyDirectGraphMathUtil.getCommaSeperatedNumber(intersectedNodeSet.size()) + "[" + MyDirectGraphMathUtil.twoDecimalFormat((double)intersectedNodeSet.size()/(MyDirectGraphVars.getDirectGraphViewer().selectedSingleNodePredecessorSet.size() + MyDirectGraphVars.getDirectGraphViewer().selectedSingleNodeSuccessorSet.size())) + "]";
                    numOfIntersectedNodeLabel.setText("INTER. N.: " + numOfIntersectedNodeValue);

                    JLabel selectedNodeLabel = new JLabel();
                    selectedNodeLabel.setFont(MyDirectGraphVars.f_pln_13);
                    selectedNodeLabel.setBackground(Color.WHITE);
                    selectedNodeLabel.setForeground(Color.BLACK);
                    String selectNodeValue = selectedSingleNode.getName();
                    selectedNodeLabel.setText("N.: " + selectNodeValue);

                    JLabel numOfEdgeLabel = new JLabel();
                    numOfEdgeLabel.setFont(MyDirectGraphVars.tahomaPlainFont13);
                    numOfEdgeLabel.setBackground(Color.WHITE);
                    numOfEdgeLabel.setForeground(Color.BLACK);
                    String numOfEdgeValue = MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.directGraph.getOutEdges(selectedSingleNode).size() + MyDirectGraphVars.directGraph.getInEdges(selectedSingleNode).size());
                    String numOfEdgePercent = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat((double) MyDirectGraphVars.directGraph.getNeighborCount(selectedSingleNode)/ MyDirectGraphVars.directGraph.getEdgeCount()));
                    numOfEdgeLabel.setText("E.: " + numOfEdgeValue + "[" + numOfEdgePercent + "]");

                    JLabel numOfNodeLabel = new JLabel();
                    numOfNodeLabel.setFont(MyDirectGraphVars.tahomaPlainFont13);
                    numOfNodeLabel.setBackground(Color.WHITE);
                    numOfNodeLabel.setForeground(Color.BLACK);
                    String numberOfNodeValue = MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.directGraph.getNeighborCount(selectedSingleNode));
                    String numberOfNodePercent = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat((double) MyDirectGraphVars.directGraph.getNeighborCount(selectedSingleNode)/ MyDirectGraphVars.directGraph.getVertexCount()));
                    numOfNodeLabel.setText("N.: " + numberOfNodeValue + "[" + numberOfNodePercent + "]");

                    JLabel successorCountLabel = new JLabel();
                    successorCountLabel.setFont(MyDirectGraphVars.tahomaPlainFont13);
                    successorCountLabel.setBackground(Color.WHITE);
                    successorCountLabel.setForeground(Color.BLACK);
                    String successorCountValue = MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.directGraph.getSuccessorCount(selectedSingleNode));
                    String successorPercent = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat((double) MyDirectGraphVars.directGraph.getSuccessorCount(selectedSingleNode)/ MyDirectGraphVars.directGraph.getNeighborCount(selectedSingleNode)));
                    successorCountLabel.setText("S.: " + successorCountValue + "[" + successorPercent + "]");

                    JLabel predecessorCountLabel = new JLabel();
                    predecessorCountLabel.setFont(MyDirectGraphVars.tahomaPlainFont13);
                    predecessorCountLabel.setBackground(Color.WHITE);
                    predecessorCountLabel.setForeground(Color.BLACK);
                    String predecessorCountValue = MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.directGraph.getPredecessorCount(MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode));
                    String predecessorPercent = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat((double) MyDirectGraphVars.directGraph.getPredecessorCount(selectedSingleNode)/ MyDirectGraphVars.directGraph.getNeighborCount(selectedSingleNode)));
                    predecessorCountLabel.setText("P.: " + predecessorCountValue + "[" + predecessorPercent + "]");

                    JLabel totalInEdgeValueLabel = new JLabel();
                    totalInEdgeValueLabel.setFont(MyDirectGraphVars.tahomaPlainFont13);
                    totalInEdgeValueLabel.setBackground(Color.WHITE);
                    totalInEdgeValueLabel.setForeground(Color.BLACK);
                    String totalInEdgeValue = MyDirectGraphVars.directGraph.getTotalInEdgeValue();
                    totalInEdgeValueLabel.setText("T. IN-E. V.: " + totalInEdgeValue);

                    JLabel totalOutEdgeValueLabel = new JLabel();
                    totalOutEdgeValueLabel.setFont(MyDirectGraphVars.tahomaPlainFont13);
                    totalOutEdgeValueLabel.setBackground(Color.WHITE);
                    totalOutEdgeValueLabel.setForeground(Color.BLACK);
                    String totalOutEdgeValue = MyDirectGraphVars.directGraph.getTotalOutEdgeValue();
                    totalOutEdgeValueLabel.setText("T. OUT-E. V.: " + totalOutEdgeValue);

                    JLabel avgInEdgeValueLabel = new JLabel();
                    avgInEdgeValueLabel.setFont(MyDirectGraphVars.tahomaPlainFont13);
                    avgInEdgeValueLabel.setBackground(Color.WHITE);
                    avgInEdgeValueLabel.setForeground(Color.BLACK);
                    String avgInEdgeValue = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(MyDirectGraphVars.directGraph.getAverageInEdgeValueForSelectedNode()));
                    avgInEdgeValueLabel.setText("AVG. IN-E. V.: " + avgInEdgeValue);

                    JLabel avgOutEdgeValueLabel = new JLabel();
                    avgOutEdgeValueLabel.setFont(MyDirectGraphVars.tahomaPlainFont13);
                    avgOutEdgeValueLabel.setBackground(Color.WHITE);
                    avgOutEdgeValueLabel.setForeground(Color.BLACK);
                    String avgOutEdgeValue = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(MyDirectGraphVars.directGraph.getAverageOutEdgeValueForSelectedNode()));
                    avgOutEdgeValueLabel.setText("AVG. OUT-E. V.: " + avgOutEdgeValue);

                    JLabel avgShortestDistance = new JLabel();
                    avgShortestDistance.setFont(MyDirectGraphVars.tahomaPlainFont13);
                    avgShortestDistance.setBackground(Color.WHITE);
                    avgShortestDistance.setForeground(Color.BLACK);
                    String avgShortestReachValue = MyDirectGraphMathUtil.twoDecimalFormat(MyDirectGraphSysUtil.getAverageShortestOutDistance());
                    avgShortestDistance.setText("AVG. S. D.: " + avgShortestReachValue);

                    JLabel emptyhLabel0 = new JLabel(" ");
                    JLabel emptyLabel1 = new JLabel(" ");
                    emptyLabel1.setFont(MyDirectGraphVars.tahomaPlainFont9);
                    JLabel emptyLabel2 = new JLabel(" ");
                    emptyLabel2.setFont(MyDirectGraphVars.tahomaPlainFont9);
                    JLabel emptyLabel3 = new JLabel(" ");
                    emptyLabel3.setFont(MyDirectGraphVars.tahomaPlainFont9);
                    JLabel emptyLabel4 = new JLabel(" ");
                    emptyLabel4.setFont(MyDirectGraphVars.tahomaPlainFont9);
                    JLabel emptyLabel5 = new JLabel(" ");
                    emptyLabel5.setFont(MyDirectGraphVars.tahomaPlainFont9);
                    JLabel emptyLabel6 = new JLabel(" ");
                    emptyLabel6.setFont(MyDirectGraphVars.tahomaPlainFont9);
                    JLabel emptyLabel7 = new JLabel(" ");
                    emptyLabel7.setFont(MyDirectGraphVars.tahomaPlainFont9);
                    JLabel emptyLabel8 = new JLabel(" ");
                    emptyLabel8.setFont(MyDirectGraphVars.tahomaPlainFont9);
                    JLabel emptyLabel9 = new JLabel(" ");
                    emptyLabel9.setFont(MyDirectGraphVars.tahomaPlainFont9);
                    JLabel emptyLabel10 = new JLabel(" ");
                    emptyLabel10.setFont(MyDirectGraphVars.tahomaPlainFont9);
                    JLabel emptyLabel11 = new JLabel(" ");
                    emptyLabel11.setFont(MyDirectGraphVars.tahomaPlainFont9);

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

                    add(emptyLabel11);
                    add(avgShortestDistance);

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
                            "<br>AVERAGE IN-EDGE VALUE: " + avgInEdgeValue +
                            "<br>AVERAGE OUT-EDGE VALUE: " + avgOutEdgeValue +
                            "<br>NO. OF INTERSECTED NODES: " + numOfIntersectedNodeValue +
                            "<br>AVERAGE SHORTEST DISTNACE: " + avgShortestReachValue +
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

                    Rectangle r = MyDirectGraphVars.app.getBounds();
                    int h = (int) r.getHeight();
                    setBounds(5, h - 150, 1650, 30);

                    Set<MyDirectNode> intersectedNodeSet = new HashSet<>();
                    intersectedNodeSet.addAll(MyDirectGraphVars.directGraph.getPredecessors(MyDirectGraphVars.getDirectGraphViewer().graphMouseMotionListener.hoveredNode));
                    intersectedNodeSet.retainAll(MyDirectGraphVars.directGraph.getSuccessors(MyDirectGraphVars.getDirectGraphViewer().graphMouseMotionListener.hoveredNode));

                    Set<MyDirectNode> neighborNodeSet = new HashSet<>();
                    neighborNodeSet.addAll(MyDirectGraphVars.directGraph.getSuccessors(MyDirectGraphVars.getDirectGraphViewer().graphMouseMotionListener.hoveredNode));
                    neighborNodeSet.addAll(MyDirectGraphVars.directGraph.getPredecessors(MyDirectGraphVars.getDirectGraphViewer().graphMouseMotionListener.hoveredNode));

                    JLabel numOfIntersectedNodeLabel = new JLabel();
                    numOfIntersectedNodeLabel.setFont(MyDirectGraphVars.f_pln_13);
                    numOfIntersectedNodeLabel.setBackground(Color.WHITE);
                    numOfIntersectedNodeLabel.setForeground(Color.BLACK);
                    String numOfIntersectedNodeValue = MyDirectGraphMathUtil.getCommaSeperatedNumber(intersectedNodeSet.size()) + "[" + MyDirectGraphMathUtil.twoDecimal(((double)intersectedNodeSet.size()/neighborNodeSet.size())*100)+ "]";
                    numOfIntersectedNodeLabel.setText("INTER. N.: " + numOfIntersectedNodeValue);

                    JLabel selectedNodeLabel = new JLabel();
                    selectedNodeLabel.setFont(MyDirectGraphVars.f_pln_13);
                    selectedNodeLabel.setBackground(Color.WHITE);
                    selectedNodeLabel.setForeground(Color.BLACK);
                    String selectNodeValue = MyDirectGraphVars.getDirectGraphViewer().graphMouseMotionListener.hoveredNode.getName();
                    selectedNodeLabel.setText(selectNodeValue);

                    JLabel numOfEdgeLabel = new JLabel();
                    numOfEdgeLabel.setFont(MyDirectGraphVars.tahomaPlainFont13);
                    numOfEdgeLabel.setBackground(Color.WHITE);
                    numOfEdgeLabel.setForeground(Color.BLACK);
                    String numOfEdgeValue = MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.directGraph.getOutEdges(MyDirectGraphVars.getDirectGraphViewer().graphMouseMotionListener.hoveredNode).size() + MyDirectGraphVars.directGraph.getInEdges(MyDirectGraphVars.getDirectGraphViewer().graphMouseMotionListener.hoveredNode).size());
                    String numOfEdgePercent = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat((double) MyDirectGraphVars.directGraph.getNeighborCount(MyDirectGraphVars.getDirectGraphViewer().graphMouseMotionListener.hoveredNode)/ MyDirectGraphVars.directGraph.getEdgeCount()));
                    numOfEdgeLabel.setText("E.: " + numOfEdgeValue + "[" + numOfEdgePercent + "]");

                    JLabel numOfNodeLabel = new JLabel();
                    numOfNodeLabel.setFont(MyDirectGraphVars.tahomaPlainFont13);
                    numOfNodeLabel.setBackground(Color.WHITE);
                    numOfNodeLabel.setForeground(Color.BLACK);
                    String numberOfNodeValue = MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.directGraph.getNeighborCount(MyDirectGraphVars.getDirectGraphViewer().graphMouseMotionListener.hoveredNode));
                    String numberOfNodePercent = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat((double) MyDirectGraphVars.directGraph.getNeighborCount(MyDirectGraphVars.getDirectGraphViewer().graphMouseMotionListener.hoveredNode)/ MyDirectGraphVars.directGraph.getVertexCount()));
                    numOfNodeLabel.setText("N.: " + numberOfNodeValue + "[" + numberOfNodePercent + "]");

                    JLabel successorCountLabel = new JLabel();
                    successorCountLabel.setFont(MyDirectGraphVars.tahomaPlainFont13);
                    successorCountLabel.setBackground(Color.WHITE);
                    successorCountLabel.setForeground(Color.BLACK);
                    String successorCountValue = MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.directGraph.getSuccessorCount(MyDirectGraphVars.getDirectGraphViewer().graphMouseMotionListener.hoveredNode));
                    String successorPercent = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat((double) MyDirectGraphVars.directGraph.getSuccessorCount(MyDirectGraphVars.getDirectGraphViewer().graphMouseMotionListener.hoveredNode)/ MyDirectGraphVars.directGraph.getNeighborCount(MyDirectGraphVars.getDirectGraphViewer().graphMouseMotionListener.hoveredNode)));
                    successorCountLabel.setText("S.: " + successorCountValue + "[" + successorPercent + "]");

                    JLabel predecessorCountLabel = new JLabel();
                    predecessorCountLabel.setFont(MyDirectGraphVars.tahomaPlainFont13);
                    predecessorCountLabel.setBackground(Color.WHITE);
                    predecessorCountLabel.setForeground(Color.BLACK);
                    String predecessorCountValue = MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.directGraph.getPredecessorCount(MyDirectGraphVars.getDirectGraphViewer().graphMouseMotionListener.hoveredNode));
                    String predecessorPercent = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat((double) MyDirectGraphVars.directGraph.getPredecessorCount(MyDirectGraphVars.getDirectGraphViewer().graphMouseMotionListener.hoveredNode)/ MyDirectGraphVars.directGraph.getNeighborCount(MyDirectGraphVars.getDirectGraphViewer().graphMouseMotionListener.hoveredNode)));
                    predecessorCountLabel.setText("P.: " + predecessorCountValue + "[" + predecessorPercent + "]");

                    JLabel avgShortestDistance = new JLabel();
                    avgShortestDistance.setFont(MyDirectGraphVars.tahomaPlainFont13);
                    avgShortestDistance.setBackground(Color.WHITE);
                    avgShortestDistance.setForeground(Color.BLACK);
                    String avgShortestDistanceValue = MyDirectGraphMathUtil.twoDecimalFormat(MyDirectGraphSysUtil.getAverageShortestOutDistance());
                    avgShortestDistance.setText("AVG. S. R.: " + avgShortestDistanceValue);

                    JLabel emptyhLabel0 = new JLabel(" ");
                    JLabel emptyLabel1 = new JLabel(" ");
                    emptyLabel1.setFont(MyDirectGraphVars.tahomaPlainFont9);
                    JLabel emptyLabel2 = new JLabel(" ");
                    emptyLabel2.setFont(MyDirectGraphVars.tahomaPlainFont9);
                    JLabel emptyLabel3 = new JLabel(" ");
                    emptyLabel3.setFont(MyDirectGraphVars.tahomaPlainFont9);
                    JLabel emptyLabel4 = new JLabel(" ");
                    emptyLabel4.setFont(MyDirectGraphVars.tahomaPlainFont9);
                    JLabel emptyLabel5 = new JLabel(" ");
                    emptyLabel5.setFont(MyDirectGraphVars.tahomaPlainFont9);
                    JLabel emptyLabel6 = new JLabel(" ");
                    emptyLabel6.setFont(MyDirectGraphVars.tahomaPlainFont9);
                    JLabel emptyLabel11 = new JLabel(" ");
                    emptyLabel11.setFont(MyDirectGraphVars.tahomaPlainFont9);

                    add(numOfNodeLabel);

                    add(emptyLabel2);
                    add(numOfEdgeLabel);

                    add(emptyLabel3);
                    add(predecessorCountLabel);

                    add(emptyLabel4);
                    add(successorCountLabel);

                    add(emptyLabel5);
                    add(numOfIntersectedNodeLabel);

                    add(emptyLabel11);
                    add(avgShortestDistance);

                    revalidate();
                    repaint();

                    setToolTipText("<html><body>" +
                            "NODE: " + selectNodeValue +
                            "<br>TOTAL NODES: " + numberOfNodeValue +
                            "<br>TOTAL EDGES: " + numOfEdgeValue +
                            "<br>TOTAL SUCCESSORS.: " + successorCountValue +
                            "<br>TOTAL PREDECESSORS: " + predecessorCountValue +
                            "<br>AVERAGE SHORTEST DISTANCE: " + avgShortestDistanceValue +
                            "</body></html>");
                }
            }).start();
        } catch (Exception ex) {}
    }

    private void setMultiNodeLevelTextStatistics() {
        try {
            removeAll();
            setBackground(Color.WHITE);
            setLayout(new FlowLayout(FlowLayout.LEFT, 5, 3));

            Rectangle r = MyDirectGraphVars.app.getBounds();
            int h = (int) r.getHeight();
            setBounds(5, h - 150, 1650, 30);

            JLabel numOfMultiNodeLabel = new JLabel();
            numOfMultiNodeLabel.setFont(MyDirectGraphVars.tahomaPlainFont13);
            numOfMultiNodeLabel.setBackground(Color.WHITE);
            numOfMultiNodeLabel.setForeground(Color.BLACK);
            String numOfMultiNodeValue = MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.getDirectGraphViewer().multiNodes.size());
            numOfMultiNodeLabel.setText("S. N.: " + numOfMultiNodeValue);

            JLabel totalNodeLabel = new JLabel();
            long totalNode = MyDirectGraphVars.getDirectGraphViewer().multiNodes.size() + MyDirectGraphVars.getDirectGraphViewer().multiNodePredecessorSet.size() + MyDirectGraphVars.getDirectGraphViewer().multiNodeSuccessorSet.size();
            totalNodeLabel.setFont(MyDirectGraphVars.tahomaPlainFont13);
            totalNodeLabel.setBackground(Color.WHITE);
            totalNodeLabel.setForeground(Color.BLACK);
            String totalNodeValue = MyDirectGraphMathUtil.getCommaSeperatedNumber(totalNode) + "[" + MyDirectGraphMathUtil.twoDecimalFormat((double)totalNode/ MyDirectGraphVars.directGraph.getVertexCount()) + "]";
            totalNodeLabel.setText("N.: " + totalNodeValue);

            JLabel numOfMultiNodeEdgesLabel = new JLabel();
            numOfMultiNodeEdgesLabel.setFont(MyDirectGraphVars.tahomaPlainFont13);
            numOfMultiNodeEdgesLabel.setBackground(Color.WHITE);
            numOfMultiNodeEdgesLabel.setForeground(Color.BLACK);
            long totalEdge = 0;
            Set<MyDirectEdge> edges = new HashSet<>();
            for (MyDirectNode n : MyDirectGraphVars.getDirectGraphViewer().multiNodes) {
                edges.addAll(MyDirectGraphVars.directGraph.getInEdges(n));
                edges.addAll(MyDirectGraphVars.directGraph.getOutEdges(n));
            }
            totalEdge = edges.size();
            String numOfEdgesValue = MyDirectGraphMathUtil.getCommaSeperatedNumber(totalEdge) + "[" + (MyDirectGraphMathUtil.twoDecimalFormat((double)totalEdge/ MyDirectGraphVars.directGraph.getEdgeCount())) + "]";
            numOfMultiNodeEdgesLabel.setText("E.: " + numOfEdgesValue);

            JLabel numOfSuccessorLabel = new JLabel();
            numOfSuccessorLabel.setFont(MyDirectGraphVars.tahomaPlainFont13);
            numOfSuccessorLabel.setBackground(Color.WHITE);
            numOfSuccessorLabel.setForeground(Color.BLACK);
            String numOfSuccessorValue = MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.getDirectGraphViewer().multiNodeSuccessorSet.size());
            numOfSuccessorValue += "[" + MyDirectGraphMathUtil.twoDecimalFormat((double) MyDirectGraphVars.getDirectGraphViewer().multiNodeSuccessorSet.size()/totalNode) + "]";
            numOfSuccessorLabel.setText("S.: " + numOfSuccessorValue);

            JLabel numOfPredecessorLabel = new JLabel();
            numOfPredecessorLabel.setFont(MyDirectGraphVars.tahomaPlainFont13);
            numOfPredecessorLabel.setBackground(Color.WHITE);
            numOfPredecessorLabel.setForeground(Color.BLACK);
            String numOfPredecessorValue = MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.getDirectGraphViewer().multiNodePredecessorSet.size());
            numOfPredecessorValue += "[" + MyDirectGraphMathUtil.twoDecimalFormat((double) MyDirectGraphVars.getDirectGraphViewer().multiNodePredecessorSet.size()/totalNode) + "]";
            numOfPredecessorLabel.setText("P.: " + numOfPredecessorValue);

            JLabel numOfSharedPredecessorLabel = new JLabel();
            numOfSharedPredecessorLabel.setFont(MyDirectGraphVars.tahomaPlainFont13);
            numOfSharedPredecessorLabel.setBackground(Color.WHITE);
            numOfSharedPredecessorLabel.setForeground(Color.BLACK);
            String numOfSharedPredecessorValue = MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.getDirectGraphViewer().multiNodeSharedPredecessorSet.size());
            numOfSharedPredecessorValue += "[" + MyDirectGraphMathUtil.twoDecimalFormat((double) MyDirectGraphVars.getDirectGraphViewer().multiNodeSharedPredecessorSet.size()/ MyDirectGraphVars.getDirectGraphViewer().multiNodePredecessorSet.size()) + "]";
            numOfSharedPredecessorLabel.setText("SHARED S.: " + numOfSharedPredecessorValue);

            JLabel numOfSharedSuccessorLabel = new JLabel();
            numOfSharedSuccessorLabel.setFont(MyDirectGraphVars.tahomaPlainFont13);
            numOfSharedSuccessorLabel.setBackground(Color.WHITE);
            numOfSharedSuccessorLabel.setForeground(Color.BLACK);
            String numOfSharedSuccessorValue = MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.getDirectGraphViewer().multiNodeSharedSuccessorSet.size());
            numOfSharedSuccessorValue += "[" + MyDirectGraphMathUtil.twoDecimalFormat((double) MyDirectGraphVars.getDirectGraphViewer().multiNodeSharedSuccessorSet.size()/ MyDirectGraphVars.getDirectGraphViewer().multiNodeSuccessorSet.size()) + "]";
            numOfSharedSuccessorLabel.setText("SHARED S.: " + numOfSharedSuccessorValue);

            JLabel avgShortestReachLabel = new JLabel();
            avgShortestReachLabel.setFont(MyDirectGraphVars.tahomaPlainFont13);
            avgShortestReachLabel.setBackground(Color.WHITE);
            avgShortestReachLabel.setForeground(Color.BLACK);
            String avgShortestReachValue = MyDirectGraphMathUtil.twoDecimalFormat(MyDirectGraphSysUtil.getMultiNodeAverageShortestOutDistance());
            avgShortestReachLabel.setText("AVG. S. D.: " + avgShortestReachValue);

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
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setTopLevelTextStatistics() {
        this.removeAll();
        this.setBackground(Color.WHITE);
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));

        Rectangle r = MyDirectGraphVars.app.getBounds();
        int h = (int) r.getHeight();
        this.setBounds(5, h - 150, 1800, 30);

        JLabel numOfNetworkLabel = new JLabel();
        numOfNetworkLabel.setFont(MyDirectGraphVars.tahomaPlainFont13);
        numOfNetworkLabel.setBackground(Color.WHITE);
        numOfNetworkLabel.setForeground(Color.BLACK);
        String numOfNetworkValue = MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.connectedComponentCountsByGraph.size());
        numOfNetworkLabel.setText("N.: " + numOfNetworkValue);

        JLabel maxNodeContLabel = new JLabel();
        maxNodeContLabel.setFont(MyDirectGraphVars.tahomaPlainFont13);
        maxNodeContLabel.setBackground(Color.WHITE);
        maxNodeContLabel.setForeground(Color.BLACK);
        String maxNodeContValue = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(MyDirectGraphVars.directGraph.getMaxNodeValue())).split("\\.")[0];
        maxNodeContLabel.setText("MAX. N. V.: " + maxNodeContValue);

        JLabel minNodeContLabel = new JLabel();
        minNodeContLabel.setFont(MyDirectGraphVars.tahomaPlainFont13);
        minNodeContLabel.setBackground(Color.WHITE);
        minNodeContLabel.setForeground(Color.BLACK);
        String minNodeContValue = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(MyDirectGraphVars.directGraph.getMinNodeValue())).split("\\.")[0];
        minNodeContLabel.setText("MIN. N. V.: " + minNodeContValue);

        JLabel avgNodeContLabel = new JLabel();
        avgNodeContLabel.setFont(MyDirectGraphVars.tahomaPlainFont13);
        avgNodeContLabel.setBackground(Color.WHITE);
        avgNodeContLabel.setForeground(Color.BLACK);
        String avgNodeContValue = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(MyDirectGraphVars.directGraph.getAverageNodeValue())).split("\\.")[0];
        avgNodeContLabel.setText("AVG. N. V.: " + avgNodeContValue);

        JLabel nodeValueStdLabel = new JLabel();
        nodeValueStdLabel.setFont(MyDirectGraphVars.tahomaPlainFont13);
        nodeValueStdLabel.setBackground(Color.WHITE);
        nodeValueStdLabel.setForeground(Color.BLACK);
        String nodeValueStdValue = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(MyDirectGraphVars.directGraph.getNodeValueStandardDeviation())).split("\\.")[0];
        nodeValueStdLabel.setText("N. V. STD.: " + nodeValueStdValue);

        JLabel maxEdgeContLabel = new JLabel();
        maxEdgeContLabel.setFont(MyDirectGraphVars.tahomaPlainFont13);
        maxEdgeContLabel.setBackground(Color.WHITE);
        maxEdgeContLabel.setForeground(Color.BLACK);
        String maxEdgeContValue = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(MyDirectGraphVars.directGraph.getMaxEdgeValue())).split("\\.")[0];
        maxEdgeContLabel.setText("MAX. E. V.: " + maxEdgeContValue);

        JLabel minEdgeContLabel = new JLabel();
        minEdgeContLabel.setFont(MyDirectGraphVars.tahomaPlainFont13);
        minEdgeContLabel.setBackground(Color.WHITE);
        minEdgeContLabel.setForeground(Color.BLACK);
        String minEdgeContValue = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(MyDirectGraphVars.directGraph.getMinEdgeValue())).split("\\.")[0];
        minEdgeContLabel.setText("MIN. E. V.: " + minEdgeContValue);

        JLabel avgEdgeContLabel = new JLabel();
        avgEdgeContLabel.setFont(MyDirectGraphVars.tahomaPlainFont13);
        avgEdgeContLabel.setBackground(Color.WHITE);
        avgEdgeContLabel.setForeground(Color.BLACK);
        String avgEdgeContValue = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(MyDirectGraphVars.directGraph.getAverageEdgeValue())).split("\\.")[0];
        avgEdgeContLabel.setText("AVG. E. V.: " + avgEdgeContValue);

        JLabel edgeStdLabel = new JLabel();
        edgeStdLabel.setFont(MyDirectGraphVars.tahomaPlainFont13);
        edgeStdLabel.setBackground(Color.WHITE);
        edgeStdLabel.setForeground(Color.BLACK);
        String edgeValueStdValue = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(MyDirectGraphVars.directGraph.getEdgValueStandardDeviation())).split("\\.")[0];
        edgeStdLabel.setText("E. V. STD.: " + edgeValueStdValue);

        JLabel maxSucCntLabel = new JLabel();
        maxSucCntLabel.setFont(MyDirectGraphVars.tahomaPlainFont13);
        maxSucCntLabel.setBackground(Color.WHITE);
        maxSucCntLabel.setForeground(Color.BLACK);
        String maxSucCntValue = MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.directGraph.getMaxSuccessorCount());
        maxSucCntLabel.setText("MAX. S.: " + maxSucCntValue);

        JLabel avgSucCntLabel = new JLabel();
        avgSucCntLabel.setFont(MyDirectGraphVars.tahomaPlainFont13);
        avgSucCntLabel.setBackground(Color.WHITE);
        avgSucCntLabel.setForeground(Color.BLACK);
        String avgSucCntValue = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(MyDirectGraphVars.directGraph.getAverageSuccessors())).split("\\.")[0];
        avgSucCntLabel.setText("AVG. S.: " + avgSucCntValue);

        JLabel maxPredCntLabel = new JLabel();
        maxPredCntLabel.setFont(MyDirectGraphVars.tahomaPlainFont13);
        maxPredCntLabel.setBackground(Color.WHITE);
        maxPredCntLabel.setForeground(Color.BLACK);
        String maxPredCntValue = MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.directGraph.getMaxPredecessorCount());
        maxPredCntLabel.setText("MAX. P. C.: " + maxPredCntValue);

        JLabel avgPredCntLabel = new JLabel();
        avgPredCntLabel.setFont(MyDirectGraphVars.tahomaPlainFont13);
        avgPredCntLabel.setBackground(Color.WHITE);
        avgPredCntLabel.setForeground(Color.BLACK);
        String avgPredCntValue = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(MyDirectGraphVars.directGraph.getAveragePredecessors())).split("\\.")[0];
        avgPredCntLabel.setText("AVG. P.: " + avgPredCntValue);

        JLabel redNodeCntLabel = new JLabel();
        redNodeCntLabel.setFont(MyDirectGraphVars.tahomaPlainFont13);
        redNodeCntLabel.setBackground(Color.WHITE);
        redNodeCntLabel.setForeground(Color.BLACK);
        String redNodeCntValue = MyDirectGraphSysUtil.getCommaSeperateString(MyDirectGraphVars.directGraph.getRedNodeCount()) + "[" + MyDirectGraphMathUtil.twoDecimalFormat(((double) MyDirectGraphVars.directGraph.getRedNodeCount()/ MyDirectGraphVars.directGraph.getVertexCount())) + "]";
        redNodeCntLabel.setText("R: " + redNodeCntValue);

        JLabel blueNodeCntLabel = new JLabel();
        blueNodeCntLabel.setFont(MyDirectGraphVars.tahomaPlainFont13);
        blueNodeCntLabel.setBackground(Color.WHITE);
        blueNodeCntLabel.setForeground(Color.BLACK);
        String blueNodeCntValue = MyDirectGraphSysUtil.getCommaSeperateString(MyDirectGraphVars.directGraph.getBlueNodeCount()) + "[" + MyDirectGraphMathUtil.twoDecimalFormat(((double) MyDirectGraphVars.directGraph.getBlueNodeCount()/ MyDirectGraphVars.directGraph.getVertexCount())) + "]";;
        blueNodeCntLabel.setText("B: " + blueNodeCntValue);

        JLabel greenNodeCntLabel = new JLabel();
        greenNodeCntLabel.setFont(MyDirectGraphVars.tahomaPlainFont13);
        greenNodeCntLabel.setBackground(Color.WHITE);
        greenNodeCntLabel.setForeground(Color.BLACK);
        String greenNodeCntValue = MyDirectGraphSysUtil.getCommaSeperateString(MyDirectGraphVars.directGraph.getGreenNodeCount()) + "[" + MyDirectGraphMathUtil.twoDecimalFormat(((double) MyDirectGraphVars.directGraph.getGreenNodeCount()/ MyDirectGraphVars.directGraph.getVertexCount())) + "]";;
        greenNodeCntLabel.setText("G: " + greenNodeCntValue);

        JLabel numOfNodes = new JLabel();
        numOfNodes.setFont(MyDirectGraphVars.tahomaPlainFont13);
        numOfNodes.setBackground(Color.WHITE);
        numOfNodes.setForeground(Color.BLACK);
        String numOfNodesValue = MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.directGraph.getGraphNodeCount());
        numOfNodes.setText("N.: " + numOfNodesValue);

        JLabel numOfEdges = new JLabel();
        numOfEdges.setFont(MyDirectGraphVars.tahomaPlainFont13);
        numOfEdges.setBackground(Color.WHITE);
        numOfEdges.setForeground(Color.BLACK);
        String numeOfEdgesValue = MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.directGraph.getGraphEdgeCount());
        numOfEdges.setText("E.: " + numeOfEdgesValue);

        JLabel avgShortestReach = new JLabel();
        avgShortestReach.setFont(MyDirectGraphVars.tahomaPlainFont13);
        avgShortestReach.setBackground(Color.WHITE);
        avgShortestReach.setForeground(Color.BLACK);
        String avgShortestReachValue = MyDirectGraphMathUtil.twoDecimalFormat(MyDirectGraphSysUtil.getAverageShortestOutDistance());
        avgShortestReach.setText("AVG. S. D.: " + avgShortestReachValue);

        JLabel emptyLabel1 = new JLabel(" ");
        emptyLabel1.setFont(MyDirectGraphVars.f_pln_10);
        JLabel emptyLabel2 = new JLabel(" ");
        emptyLabel2.setFont(MyDirectGraphVars.f_pln_10);
        JLabel emptyLabel3 = new JLabel(" ");
        emptyLabel3.setFont(MyDirectGraphVars.f_pln_10);
        JLabel emptyLabel4 = new JLabel(" ");
        emptyLabel4.setFont(MyDirectGraphVars.f_pln_10);
        JLabel emptyLabel5 = new JLabel(" ");
        emptyLabel5.setFont(MyDirectGraphVars.f_pln_10);
        JLabel emptyLabel6 = new JLabel(" ");
        emptyLabel6.setFont(MyDirectGraphVars.f_pln_10);
        JLabel emptyLabel7 = new JLabel(" ");
        emptyLabel7.setFont(MyDirectGraphVars.f_pln_10);
        JLabel emptyLabel8 = new JLabel(" ");
        emptyLabel8.setFont(MyDirectGraphVars.f_pln_10);
        JLabel emptyLabel9 = new JLabel(" ");
        emptyLabel9.setFont(MyDirectGraphVars.f_pln_10);
        JLabel emptyLabel10 = new JLabel(" ");
        emptyLabel10.setFont(MyDirectGraphVars.f_pln_10);
        JLabel emptyLabel11 = new JLabel(" ");
        emptyLabel11.setFont(MyDirectGraphVars.f_pln_10);
        JLabel emptyLabel12 = new JLabel(" ");
        emptyLabel12.setFont(MyDirectGraphVars.f_pln_10);
        JLabel emptyLabel13 = new JLabel(" ");
        emptyLabel13.setFont(MyDirectGraphVars.f_pln_10);
        JLabel emptyLabel14 = new JLabel(" ");
        emptyLabel14.setFont(MyDirectGraphVars.f_pln_10);
        JLabel emptyLabel15 = new JLabel(" ");
        emptyLabel15.setFont(MyDirectGraphVars.f_pln_10);
        JLabel emptyLabel16 = new JLabel(" ");
        emptyLabel16.setFont(MyDirectGraphVars.f_pln_10);
        JLabel emptyLabel17 = new JLabel(" ");
        emptyLabel17.setFont(MyDirectGraphVars.f_pln_10);
        JLabel emptyLabel18 = new JLabel(" ");
        emptyLabel18.setFont(MyDirectGraphVars.f_pln_10);
        JLabel emptyLabel19 = new JLabel(" ");
        emptyLabel19.setFont(MyDirectGraphVars.f_pln_10);
        JLabel emptyLabel20 = new JLabel(" ");
        emptyLabel20.setFont(MyDirectGraphVars.f_pln_10);
        JLabel emptyLabel21 = new JLabel(" ");
        emptyLabel21.setFont(MyDirectGraphVars.f_pln_10);

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

    public void setShortestPathTextStatistics() {
        this.removeAll();
        this.setBackground(Color.WHITE);
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));

        Rectangle r = MyDirectGraphVars.app.getBounds();
        int h = (int) r.getHeight();
        this.setBounds(5, h - 150, 1800, 30);

        JLabel numOfNodesLabel = new JLabel();
        numOfNodesLabel.setFont(MyDirectGraphVars.tahomaPlainFont13);
        numOfNodesLabel.setBackground(Color.WHITE);
        numOfNodesLabel.setForeground(Color.BLACK);
        String numberOfNodesValue = MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.directGraph.getGraphNodeCount()-1);
        numOfNodesLabel.setText("N.: " + numberOfNodesValue);

        JLabel maxDistanceLabel = new JLabel();
        maxDistanceLabel.setFont(MyDirectGraphVars.tahomaPlainFont13);
        maxDistanceLabel.setBackground(Color.WHITE);
        maxDistanceLabel.setForeground(Color.BLACK);
        String maxDistanceValue = MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.app.getDirectGraphDashBoard().distanceMenu.getItemCount()-1);
        maxDistanceLabel.setText("   MAX. D: " + maxDistanceValue);

        JLabel avgPredecessorLabel = new JLabel();
        avgPredecessorLabel.setFont(MyDirectGraphVars.tahomaPlainFont13);
        avgPredecessorLabel.setBackground(Color.WHITE);
        avgPredecessorLabel.setForeground(Color.BLACK);
        String avgPredecessorValue = MyDirectGraphMathUtil.twoDecimalFormat(MyDirectGraphVars.directGraph.getAveragePredecessors());
        avgPredecessorLabel.setText("   AVG. P: " + avgPredecessorValue);

        JLabel avgSuccessorLabel = new JLabel();
        avgSuccessorLabel.setFont(MyDirectGraphVars.tahomaPlainFont13);
        avgSuccessorLabel.setBackground(Color.WHITE);
        avgSuccessorLabel.setForeground(Color.BLACK);
        String avgSuccessorValue = MyDirectGraphMathUtil.twoDecimalFormat(MyDirectGraphVars.directGraph.getAverageSuccessors());
        avgSuccessorLabel.setText("   AVG. S: " + avgSuccessorValue);

        String tooltip = "<html><body>" +
                         "NO. OF NODES: " + numberOfNodesValue +
                         "<br> MAX. DISTANCE: " + avgSuccessorValue +
                         "<br> AVG. P.: " + avgPredecessorValue +
                         "<br> AVG. S.: " + avgSuccessorValue +
                         "</body></html>";
        setToolTipText(tooltip);

        this.add(numOfNodesLabel);
        this.add(maxDistanceLabel);
        this.add(avgPredecessorLabel);
        this.add(avgSuccessorLabel);
        revalidate();
        repaint();
    }

    private void setClusterTextStatistics() {
        this.removeAll();
        this.setBackground(Color.WHITE);
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));

        Rectangle r = MyDirectGraphVars.app.getBounds();
        int h = (int) r.getHeight();
        this.setBounds(5, h - 150, 1800, 30);

        JLabel numOfNetworkLabel = new JLabel();
        numOfNetworkLabel.setFont(MyDirectGraphVars.tahomaPlainFont13);
        numOfNetworkLabel.setBackground(Color.WHITE);
        numOfNetworkLabel.setForeground(Color.BLACK);
        String numOfNetworkValue = MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.connectedComponentCountsByGraph.size());
        numOfNetworkLabel.setText("N.: " + numOfNetworkValue);

        JLabel maxNodeContLabel = new JLabel();
        maxNodeContLabel.setFont(MyDirectGraphVars.tahomaPlainFont13);
        maxNodeContLabel.setBackground(Color.WHITE);
        maxNodeContLabel.setForeground(Color.BLACK);
        String maxNodeContValue = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(MyDirectGraphVars.directGraph.getMaxNodeValue())).split("\\.")[0];
        maxNodeContLabel.setText("MAX. N. V.: " + maxNodeContValue);

        JLabel minNodeContLabel = new JLabel();
        minNodeContLabel.setFont(MyDirectGraphVars.tahomaPlainFont13);
        minNodeContLabel.setBackground(Color.WHITE);
        minNodeContLabel.setForeground(Color.BLACK);
        String minNodeContValue = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(MyDirectGraphVars.directGraph.getMinNodeValue())).split("\\.")[0];
        minNodeContLabel.setText("MIN. N. V.: " + minNodeContValue);

        JLabel avgNodeContLabel = new JLabel();
        avgNodeContLabel.setFont(MyDirectGraphVars.tahomaPlainFont13);
        avgNodeContLabel.setBackground(Color.WHITE);
        avgNodeContLabel.setForeground(Color.BLACK);
        String avgNodeContValue = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(MyDirectGraphVars.directGraph.getAverageNodeValue())).split("\\.")[0];
        avgNodeContLabel.setText("AVG. N. V.: " + avgNodeContValue);

        JLabel nodeValueStdLabel = new JLabel();
        nodeValueStdLabel.setFont(MyDirectGraphVars.tahomaPlainFont13);
        nodeValueStdLabel.setBackground(Color.WHITE);
        nodeValueStdLabel.setForeground(Color.BLACK);
        String nodeValueStdValue = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(MyDirectGraphVars.directGraph.getNodeValueStandardDeviation())).split("\\.")[0];
        nodeValueStdLabel.setText("N. V. STD.: " + nodeValueStdValue);

        JLabel maxEdgeContLabel = new JLabel();
        maxEdgeContLabel.setFont(MyDirectGraphVars.tahomaPlainFont13);
        maxEdgeContLabel.setBackground(Color.WHITE);
        maxEdgeContLabel.setForeground(Color.BLACK);
        String maxEdgeContValue = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(MyDirectGraphVars.directGraph.getMaxEdgeValue())).split("\\.")[0];
        maxEdgeContLabel.setText("MAX. E. V.: " + maxEdgeContValue);

        JLabel minEdgeContLabel = new JLabel();
        minEdgeContLabel.setFont(MyDirectGraphVars.tahomaPlainFont13);
        minEdgeContLabel.setBackground(Color.WHITE);
        minEdgeContLabel.setForeground(Color.BLACK);
        String minEdgeContValue = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(MyDirectGraphVars.directGraph.getMinEdgeValue())).split("\\.")[0];
        minEdgeContLabel.setText("MIN. E. V.: " + minEdgeContValue);

        JLabel avgEdgeContLabel = new JLabel();
        avgEdgeContLabel.setFont(MyDirectGraphVars.tahomaPlainFont13);
        avgEdgeContLabel.setBackground(Color.WHITE);
        avgEdgeContLabel.setForeground(Color.BLACK);
        String avgEdgeContValue = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(MyDirectGraphVars.directGraph.getAverageEdgeValue())).split("\\.")[0];
        avgEdgeContLabel.setText("AVG. E. V.: " + avgEdgeContValue);

        JLabel edgeStdLabel = new JLabel();
        edgeStdLabel.setFont(MyDirectGraphVars.tahomaPlainFont13);
        edgeStdLabel.setBackground(Color.WHITE);
        edgeStdLabel.setForeground(Color.BLACK);
        String edgeValueStdValue = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(MyDirectGraphVars.directGraph.getEdgValueStandardDeviation())).split("\\.")[0];
        edgeStdLabel.setText("E. V. STD.: " + edgeValueStdValue);

        JLabel maxSucCntLabel = new JLabel();
        maxSucCntLabel.setFont(MyDirectGraphVars.tahomaPlainFont13);
        maxSucCntLabel.setBackground(Color.WHITE);
        maxSucCntLabel.setForeground(Color.BLACK);
        String maxSucCntValue = MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.directGraph.getMaxSuccessorCount());
        maxSucCntLabel.setText("MAX. S.: " + maxSucCntValue);

        JLabel avgSucCntLabel = new JLabel();
        avgSucCntLabel.setFont(MyDirectGraphVars.tahomaPlainFont13);
        avgSucCntLabel.setBackground(Color.WHITE);
        avgSucCntLabel.setForeground(Color.BLACK);
        String avgSucCntValue = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(MyDirectGraphVars.directGraph.getAverageSuccessors())).split("\\.")[0];
        avgSucCntLabel.setText("AVG. S.: " + avgSucCntValue);

        JLabel maxPredCntLabel = new JLabel();
        maxPredCntLabel.setFont(MyDirectGraphVars.tahomaPlainFont13);
        maxPredCntLabel.setBackground(Color.WHITE);
        maxPredCntLabel.setForeground(Color.BLACK);
        String maxPredCntValue = MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.directGraph.getMaxPredecessorCount());
        maxPredCntLabel.setText("MAX. P. C.: " + maxPredCntValue);

        JLabel avgPredCntLabel = new JLabel();
        avgPredCntLabel.setFont(MyDirectGraphVars.tahomaPlainFont13);
        avgPredCntLabel.setBackground(Color.WHITE);
        avgPredCntLabel.setForeground(Color.BLACK);
        String avgPredCntValue = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(MyDirectGraphVars.directGraph.getAveragePredecessors())).split("\\.")[0];
        avgPredCntLabel.setText("AVG. P.: " + avgPredCntValue);

        JLabel redNodeCntLabel = new JLabel();
        redNodeCntLabel.setFont(MyDirectGraphVars.tahomaPlainFont13);
        redNodeCntLabel.setBackground(Color.WHITE);
        redNodeCntLabel.setForeground(Color.BLACK);
        String redNodeCntValue = MyDirectGraphSysUtil.getCommaSeperateString(MyDirectGraphVars.directGraph.getRedNodeCount()) + "[" + MyDirectGraphMathUtil.twoDecimalFormat(((double) MyDirectGraphVars.directGraph.getRedNodeCount()/ MyDirectGraphVars.directGraph.getVertexCount())) + "]";
        redNodeCntLabel.setText("R: " + redNodeCntValue);

        JLabel blueNodeCntLabel = new JLabel();
        blueNodeCntLabel.setFont(MyDirectGraphVars.tahomaPlainFont13);
        blueNodeCntLabel.setBackground(Color.WHITE);
        blueNodeCntLabel.setForeground(Color.BLACK);
        String blueNodeCntValue = MyDirectGraphSysUtil.getCommaSeperateString(MyDirectGraphVars.directGraph.getBlueNodeCount()) + "[" + MyDirectGraphMathUtil.twoDecimalFormat(((double) MyDirectGraphVars.directGraph.getBlueNodeCount()/ MyDirectGraphVars.directGraph.getVertexCount())) + "]";;
        blueNodeCntLabel.setText("B: " + blueNodeCntValue);

        JLabel greenNodeCntLabel = new JLabel();
        greenNodeCntLabel.setFont(MyDirectGraphVars.tahomaPlainFont13);
        greenNodeCntLabel.setBackground(Color.WHITE);
        greenNodeCntLabel.setForeground(Color.BLACK);
        String greenNodeCntValue = MyDirectGraphSysUtil.getCommaSeperateString(MyDirectGraphVars.directGraph.getGreenNodeCount()) + "[" + MyDirectGraphMathUtil.twoDecimalFormat(((double) MyDirectGraphVars.directGraph.getGreenNodeCount()/ MyDirectGraphVars.directGraph.getVertexCount())) + "]";;
        greenNodeCntLabel.setText("G: " + greenNodeCntValue);

        JLabel numOfNodes = new JLabel();
        numOfNodes.setFont(MyDirectGraphVars.tahomaPlainFont13);
        numOfNodes.setBackground(Color.WHITE);
        numOfNodes.setForeground(Color.BLACK);
        String numOfNodesValue = MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.directGraph.getGraphNodeCount());
        numOfNodes.setText("N.: " + numOfNodesValue);

        JLabel numOfEdges = new JLabel();
        numOfEdges.setFont(MyDirectGraphVars.tahomaPlainFont13);
        numOfEdges.setBackground(Color.WHITE);
        numOfEdges.setForeground(Color.BLACK);
        long edgeCount = MyDirectGraphVars.directGraph.getGraphEdgeCount();
        String numeOfEdgesValue = MyDirectGraphMathUtil.getCommaSeperatedNumber(edgeCount);
        numOfEdges.setText("E.: " + numeOfEdgesValue);

        JLabel emptyLabel1 = new JLabel(" ");
        emptyLabel1.setFont(MyDirectGraphVars.f_pln_10);
        JLabel emptyLabel2 = new JLabel(" ");
        emptyLabel2.setFont(MyDirectGraphVars.f_pln_10);
        JLabel emptyLabel3 = new JLabel(" ");
        emptyLabel3.setFont(MyDirectGraphVars.f_pln_10);
        JLabel emptyLabel4 = new JLabel(" ");
        emptyLabel4.setFont(MyDirectGraphVars.f_pln_10);
        JLabel emptyLabel5 = new JLabel(" ");
        emptyLabel5.setFont(MyDirectGraphVars.f_pln_10);
        JLabel emptyLabel6 = new JLabel(" ");
        emptyLabel6.setFont(MyDirectGraphVars.f_pln_10);
        JLabel emptyLabel7 = new JLabel(" ");
        emptyLabel7.setFont(MyDirectGraphVars.f_pln_10);
        JLabel emptyLabel8 = new JLabel(" ");
        emptyLabel8.setFont(MyDirectGraphVars.f_pln_10);
        JLabel emptyLabel9 = new JLabel(" ");
        emptyLabel9.setFont(MyDirectGraphVars.f_pln_10);
        JLabel emptyLabel10 = new JLabel(" ");
        emptyLabel10.setFont(MyDirectGraphVars.f_pln_10);
        JLabel emptyLabel11 = new JLabel(" ");
        emptyLabel11.setFont(MyDirectGraphVars.f_pln_10);
        JLabel emptyLabel12 = new JLabel(" ");
        emptyLabel12.setFont(MyDirectGraphVars.f_pln_10);
        JLabel emptyLabel13 = new JLabel(" ");
        emptyLabel13.setFont(MyDirectGraphVars.f_pln_10);
        JLabel emptyLabel14 = new JLabel(" ");
        emptyLabel14.setFont(MyDirectGraphVars.f_pln_10);
        JLabel emptyLabel15 = new JLabel(" ");
        emptyLabel15.setFont(MyDirectGraphVars.f_pln_10);
        JLabel emptyLabel16 = new JLabel(" ");
        emptyLabel16.setFont(MyDirectGraphVars.f_pln_10);
        JLabel emptyLabel17 = new JLabel(" ");
        emptyLabel17.setFont(MyDirectGraphVars.f_pln_10);
        JLabel emptyLabel18 = new JLabel(" ");
        emptyLabel18.setFont(MyDirectGraphVars.f_pln_10);
        JLabel emptyLabel19 = new JLabel(" ");
        emptyLabel19.setFont(MyDirectGraphVars.f_pln_10);
        JLabel emptyLabel20 = new JLabel(" ");
        emptyLabel20.setFont(MyDirectGraphVars.f_pln_10);

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
        String toolTip = "<html><body>" +
                         "NO OF NETWORKS: " + MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.connectedComponentCountsByGraph.size()) +
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
                         "<br>GREEN NODES: " + greenNodeCount +
                         "</body></html>";
        this.setToolTipText(toolTip);
    }
}
