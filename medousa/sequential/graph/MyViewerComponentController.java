package medousa.sequential.graph;

import medousa.MyProgressBar;
import medousa.direct.utils.MyDirectGraphSysUtil;
import medousa.message.MyMessageUtil;
import medousa.sequential.graph.listener.MyEdgeLabelSelecterListener;
import medousa.sequential.graph.listener.MyNodeLabelSelecterListener;
import medousa.sequential.graph.stats.*;
import medousa.sequential.graph.stats.barchart.*;
import medousa.sequential.graph.listener.MyNodeEdgeExclusionActionListener;
import medousa.sequential.utils.*;
import medousa.sequential.utils.MySequentialGraphSysUtil;
import medousa.sequential.utils.MySequentialGraphVars;
import medousa.table.MyTableToolTipper;
import medousa.table.MyTableUtil;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class MyViewerComponentController
extends JPanel
implements ActionListener {

    public final String [] nodeValueItems = {
        "CONTRIBUTION", //1
        "IN-CONTRIBUTION", //2
        "AVG. IN-CONTRIBUTION", //3
        "OUT-CONTRIBUTION", //4
        "AVG. OUT-CONTRIBUTION", //5
        "CONTRIBUTION DIFFERENCE", //6
        "UNIQUE CONTRIBUTION", //7
        "IN-NODES", //8
        "OUT-NODES", //9
        "INOUT-NODE DIFFERENCE", //10
        "END POSITION COUNT", //11
        "AVG. SHORTEST DISTANCE", //12
        "TOTAL RECURSIVE LENGTH", //13
        "MIN. RECURSIVE LENGTH", //14
        "MAX. RECURSIVE LENGTH", //15
        "AVG. RECURSIVE LENGTH", //16
        "AVG. RECURSIVE TIME", //17
        "DURATION", //18
        "AVG. DURATION", //19
        "MAX. DURATION", //20
        "MIN. DURATION", //21
        "AVG. REACH TIME", //22
        "TOTAL REACH TIME", //23
        "TOTAL RECURRENCE COUNT",  //24
        "TOTAL RECURRENCE TIME", //25
        "MAX. RECURRENCE TIME",  //26
        "MIN. RECURRENCE TIME",  //27
        "ITEMSET LENGTH",    //28
        "BETWEENESS",     //29
        "CLOSENESS",   //30
        "EIGENVECTOR",   //31
        "PAGERANK",  //32
        "START POSITION COUNT",  //33
        "UNREACHABLE NODE CCOUNT"  //34
    };

    public final String [] depthNodeValueItems = {
        "CONTRIBUTION",
        "IN-CONTRIBUTION",
        "OUT-CONTRIBUTION",
        "CONTRIBUTION DIFFERENCE",
        "IN-NODES",
        "OUT-NODES",
        "INOUT-NODE DIFFERENCE",
        "DURATION",
        "REACH TIME",
        "MAX. DURATION",
        "MIN. DURATION",
        "MAX. REACH TIME",
        "MIN. REACH TIME"
    };

    private final String [] depthEdgeValueItems = {
            "NONE",
            "IN CONT.",
            "OUT CONT.",
            "CONT. D.",
            "IN N.",
            "OUT N.",
            "INOUT N. D.",
            "DURATION",
            "REACH T."
    };

    public String [] edgeValueItems = {
        "NONE",
        "DEFAULT",
        "CONTRIBUTION",
        "UNIQUE CONTRIBUTION",
        "AVG. BETWEEN TIME",
        "TOTAL BETWEEN TIME",
        "MAX. BETWEEN TIME",
        "MIN. BETWEEN TIME",
        "SUPPORT",
        "CONFIDENCE",
        "LIFT",
        "BETWEENESE"
    };

    public JTable statTable;
    public JTable edgeListTable;
    public JLabel clusteringSectorLabel = new JLabel("CLS.");
    private JPanel topLeftPanel = new JPanel();
    public JPanel bottomLeftControlPanel =new JPanel();
    public JPanel bottomRightControlPanel = new JPanel();
    public JPanel bottomPanel = new JPanel();
    public MyNode shortestDistanceSourceNode;
    public JButton excludeBtn;
    public MyTextStatistics vTxtStat = new MyTextStatistics();
    public JComboBox shortestDistanceMenu = new JComboBox();
    public JComboBox depthSelecter = new JComboBox();
    public JComboBox nodeValueSelecter = new JComboBox();
    public JComboBox edgeValueSelecter = new JComboBox();
    public JComboBox nodeValueExcludeSymbolSelecter = new JComboBox();
    public JComboBox edgeValueExcludeSymbolSelecter  = new JComboBox();
    public JComboBox edgeLabelSelecter = new JComboBox();
    public JComboBox nodeLabelSelecter = new JComboBox();
    public JComboBox nodeLabelExcludeMathSymbolSelecter = new JComboBox();
    public JComboBox nodeLabelExcludeSelecter = new JComboBox();
    public JComboBox nodeLabelValueExcludeSelecter = new JComboBox();
    public JComboBox edgeLabelExcludeMathSymbolSelecter = new JComboBox();
    public JComboBox edgeLabelExcludeSelecter = new JComboBox();
    public JComboBox depthExcludeSelecter = new JComboBox();
    public JComboBox depthExcludeSymbolSelecter = new JComboBox();
    public JComboBox distributionSelecter = new JComboBox();
    public JComboBox edgeLabelValueExcludeSelecter = new JComboBox();
    public JComboBox selectedNodeNeighborNodeTypeSelector = new JComboBox();
    public JComboBox depthNeighborNodeTypeSelector = new JComboBox();
    public JComboBox clusteringSelector = new JComboBox();
    public JTextField nodeValueExcludeTxt = new JTextField();
    public JTextField edgeValueExcludeTxt = new JTextField();
    public JCheckBox weightedNodeColor = new JCheckBox("N. C.");
    public JCheckBox nodeValueBarChart = new JCheckBox("N. V. B.");
    public JCheckBox nodeLabelBarChart = new JCheckBox("N. L. B.");
    public JCheckBox edgeValueBarChart = new JCheckBox("E. V. B.");
    public JCheckBox edgeLabelBarChart = new JCheckBox("E. L. B.");
    public JCheckBox endingNodeBarChart = new JCheckBox("E. N. B.");
    public Map<String, Integer> selectedNodeSuccessorDepthNodeMap;
    public Map<String, Integer> selectedNodePredecessorDepthNodeMap;
    public Map<String, Map<String, Integer>> depthNodeSuccessorMaps;
    public Map<String, Map<String, Integer>> depthNodePredecessorMaps;
    public Set<String> depthNodeNameSet;
    public Set<String> depthNeighborSet;
    public JLabel nodeValueSelecterLabel = new JLabel("  N. V.");
    public JSplitPane graphTableSplitPane = new JSplitPane();
    public JTabbedPane tableTabbedPane = new JTabbedPane();
    public JTable shortestDistanceSourceTable;
    public JTable shortestDistanceDestTable;
    public JTable nodeListTable;
    public JTable selectedNodeStatTable;
    public JTable multiNodeStatTable;
    public JTable currentNodeListTable;
    JComboBox shortestDistanceOptionMenu = new JComboBox();
    JPanel shortestDistanceSourceTablePanel;
    JPanel shortestDistanceDestTablePanel;
    public boolean isTableUpdating;
    public Set<MyNode> visitedNodes;
    public JLabel edgeLabelExcludeComboBoxMenuLabel = new JLabel(" E. L.");
    public JLabel edgeLabelLabel = new JLabel("  E. L.");
    public JLabel edgeValueLabel = new JLabel("   E. V.");
    public JLabel edgeValueExludeLabel = new JLabel(" E. V.");
    public JLabel nodeLabelLabel = new JLabel(" N. L.");

    public MyViewerComponentController() {}

    public void setNodeValueExcludeSymbolComboBox() {
        if (this.nodeValueExcludeSymbolSelecter != null) {
            this.nodeValueExcludeSymbolSelecter.removeActionListener(this);
            this.nodeValueExcludeSymbolSelecter.removeAllItems();
            this.nodeValueExcludeSymbolSelecter.addItem("");
            this.nodeValueExcludeSymbolSelecter.addItem(">");
            this.nodeValueExcludeSymbolSelecter.addItem(">=");
            this.nodeValueExcludeSymbolSelecter.addItem("==");
            this.nodeValueExcludeSymbolSelecter.addItem("!=");
            this.nodeValueExcludeSymbolSelecter.addItem("<");
            this.nodeValueExcludeSymbolSelecter.addItem("<=");
            this.nodeValueExcludeSymbolSelecter.addItem("BTW.");
            this.nodeValueExcludeSymbolSelecter.setToolTipText("SELECT AN ARITHMATIC SYMBOL TO EXCLUDE");
            this.nodeValueExcludeSymbolSelecter.setSelectedIndex(0);
            this.nodeValueExcludeSymbolSelecter.addActionListener(this);
        } else {
            this.nodeValueExcludeSymbolSelecter = new JComboBox();
            this.nodeValueExcludeSymbolSelecter.addItem("");
            this.nodeValueExcludeSymbolSelecter.addItem(">");
            this.nodeValueExcludeSymbolSelecter.addItem(">=");
            this.nodeValueExcludeSymbolSelecter.addItem("==");
            this.nodeValueExcludeSymbolSelecter.addItem("!=");
            this.nodeValueExcludeSymbolSelecter.addItem("<");
            this.nodeValueExcludeSymbolSelecter.addItem("<=");
            this.nodeValueExcludeSymbolSelecter.addItem("BTW.");
            this.nodeValueExcludeSymbolSelecter.setToolTipText("SELECT AN ARITHMATIC SYMBOL TO EXCLUDE");
            this.nodeValueExcludeSymbolSelecter.setFont(MySequentialGraphVars.tahomaPlainFont11);
            this.nodeValueExcludeSymbolSelecter.setBackground(Color.WHITE);
            this.nodeValueExcludeSymbolSelecter.setFocusable(false);
            this.nodeValueExcludeSymbolSelecter.addActionListener(this);
        }
    }

    public void setEdgeValueExcludeSymbolComboBox() {
        if (this.edgeValueExcludeSymbolSelecter != null) {
            this.edgeValueExcludeSymbolSelecter.removeActionListener(this);
            this.edgeValueExcludeSymbolSelecter.removeAllItems();
            this.edgeValueExcludeSymbolSelecter.addItem("");
            this.edgeValueExcludeSymbolSelecter.addItem(">");
            this.edgeValueExcludeSymbolSelecter.addItem(">=");
            this.edgeValueExcludeSymbolSelecter.addItem("==");
            this.edgeValueExcludeSymbolSelecter.addItem("!=");
            this.edgeValueExcludeSymbolSelecter.addItem("<");
            this.edgeValueExcludeSymbolSelecter.addItem("<=");
            this.edgeValueExcludeSymbolSelecter.addItem("BTW.");
            this.edgeValueExcludeSymbolSelecter.setToolTipText("SELECT AN ARITHMATIC SYMBOL TO EXCLUDE");
            this.edgeValueExcludeSymbolSelecter.setSelectedIndex(0);
            this.edgeValueExcludeSymbolSelecter.addActionListener(this);
        } else {
            this.edgeValueExcludeSymbolSelecter = new JComboBox();
            this.edgeValueExcludeSymbolSelecter.addItem("");
            this.edgeValueExcludeSymbolSelecter.addItem(">");
            this.edgeValueExcludeSymbolSelecter.addItem(">=");
            this.edgeValueExcludeSymbolSelecter.addItem("==");
            this.edgeValueExcludeSymbolSelecter.addItem("!=");
            this.edgeValueExcludeSymbolSelecter.addItem("<");
            this.edgeValueExcludeSymbolSelecter.addItem("<=");
            this.edgeValueExcludeSymbolSelecter.addItem("BTW.");
            this.edgeValueExcludeSymbolSelecter.setToolTipText("SELECT AN ARITHMATIC SYMBOL TO EXCLUDE NODES & EDGES");
            this.edgeValueExcludeSymbolSelecter.setFont(MySequentialGraphVars.tahomaPlainFont11);
            this.edgeValueExcludeSymbolSelecter.setBackground(Color.WHITE);
            this.edgeValueExcludeSymbolSelecter.setFocusable(false);
            this.edgeValueExcludeSymbolSelecter.addActionListener(this);
        }
    }

    private void setShortestOutDistancedNodes() {
        if (isTableUpdating || shortestDistanceSourceTable.getSelectedRow() == -1) return;
        String fromTableNodeName = shortestDistanceSourceTable.getValueAt(shortestDistanceSourceTable.getSelectedRow(), 1).toString();
        if (shortestDistanceSourceNode != null && fromTableNodeName.equals(shortestDistanceSourceNode.getName())) {return;}

        isTableUpdating = true;
        MyProgressBar pb = new MyProgressBar(false);
        try {
            shortestDistanceSourceNode = (MyNode) MySequentialGraphVars.g.vRefs.get(MySequentialGraphVars.nodeNameMap.get(fromTableNodeName));
            Set<MyNode> topLevelSuccessors = new HashSet<>(MySequentialGraphVars.g.getSuccessors(shortestDistanceSourceNode));
            topLevelSuccessors.add(shortestDistanceSourceNode);
            Queue<MyNode> Qu = new LinkedList<>();
            visitedNodes = new HashSet();
            Qu.add(shortestDistanceSourceNode);
            Map<MyNode, Integer> distanceMap =  new HashMap();

            int maxDistance = 0;
            distanceMap.put(shortestDistanceSourceNode, 0);
            while (!Qu.isEmpty()) {
                MyNode v = Qu.remove();
                Collection<MyNode> successors = MySequentialGraphVars.g.getSuccessors(v);
                for (MyNode neighbor : successors) {
                    if (!visitedNodes.contains(neighbor)) {
                        int distance = distanceMap.get(v) + 1;
                        neighbor.shortestOutDistance = distance;
                        if (distance > maxDistance) {
                            maxDistance = distance;
                        }
                        distanceMap.put(neighbor, distance);
                        Qu.add(neighbor);
                    }
                }
                visitedNodes.add(v);
            }

            while (shortestDistanceDestTable.getRowCount() > 0) {
                int row = shortestDistanceDestTable.getRowCount() - 1;
                ((DefaultTableModel) shortestDistanceDestTable.getModel()).removeRow(row);
            }

            LinkedHashMap<String, Long> valueMap = new LinkedHashMap<>();
            float maxNodeVal = 0f;
            TreeSet<Float> distances = new TreeSet<>();
            Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
            for (MyNode n : nodes) {
                if (visitedNodes.contains(n)) {
                    if (n == shortestDistanceSourceNode && visitedNodes.size() == 1) {
                        n.setCurrentValue(0);
                        continue;
                    } else {
                        distances.add(n.shortestOutDistance);
                    }
                    valueMap.put(n.getName(), (long) n.shortestOutDistance);
                } else {
                    n.setCurrentValue(0);
                }
            }
            // Create a Comparator to compare values in reverse order
            Comparator<Map.Entry<String, Long>> valueComparator = (e1, e2) -> {
                if (e1.getValue() < e2.getValue()) {
                    return -1; // Return negative value to indicate e1 should appear before e2
                } else if (e1.getValue() > e2.getValue()) {
                    return 1; // Return positive value to indicate e1 should appear after e2
                } else {
                    return 0; // Return zero if values are equal
                }
            };

            // Convert the entrySet to a List and sort it using the valueComparator
            List<Map.Entry<String, Long>> sortedEntries = new ArrayList<>(valueMap.entrySet());
            Collections.sort(sortedEntries, valueComparator);

            // Create a new LinkedHashMap to store the entries in the desired order
            LinkedHashMap<String, Long> reversedMap = new LinkedHashMap<>();

            // Iterate over the sorted list and put the entries into the reversedMap
            for (Map.Entry<String, Long> entry : sortedEntries) {
                reversedMap.put(entry.getKey(), entry.getValue());
            }

            int i=0;
            if (visitedNodes.size() > 1) {
                for (String n : reversedMap.keySet()) {
                    ((DefaultTableModel) shortestDistanceDestTable.getModel()).addRow(
                        new String[]{"" + (++i),
                            MySequentialGraphSysUtil.getNodeName(n),
                            MyMathUtil.getCommaSeperatedNumber(reversedMap.get(n))});
                }

                for (float distance : distances) {
                    if (distance > maxNodeVal) {
                        maxNodeVal = distance;
                    }
                }
            }

            shortestDistanceDestTablePanel.remove(shortestDistanceMenu);
            shortestDistanceMenu = new JComboBox();
            shortestDistanceMenu.setFocusable(false);
            shortestDistanceMenu.setFont(MySequentialGraphVars.tahomaPlainFont10);
            shortestDistanceMenu.addItem("DISTANCE");
            for (float distance : distances) {
                shortestDistanceMenu.addItem("" + (int) distance);
            }

            shortestDistanceMenu.addActionListener(new ActionListener() {
                @Override public void actionPerformed(ActionEvent e) {
                    new Thread(new Runnable() {
                        @Override public void run() {
                            if (shortestDistanceMenu.getSelectedIndex() > 0) {
                                MyProgressBar pb = new MyProgressBar(false);
                                Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
                                for (MyNode n : nodes) {
                                    if (n.getCurrentValue() > 0) {
                                        if (Integer.parseInt(shortestDistanceMenu.getSelectedItem().toString()) != n.shortestOutDistance) {
                                            if (n.getCurrentValue() != 0) {
                                                n.setOriginalValue(n.getCurrentValue());
                                                n.setCurrentValue(0);
                                            }
                                        }
                                    } else if (n.shortestOutDistance == Integer.parseInt(shortestDistanceMenu.getSelectedItem().toString()) && n.getCurrentValue() == 0) {
                                        n.setCurrentValue(n.getOriginalValue());
                                    }

                                    if (MySequentialGraphVars.g.MX_N_VAL < n.getCurrentValue()) {
                                        MySequentialGraphVars.g.MX_N_VAL = n.getCurrentValue();
                                    }
                                }

                                while (shortestDistanceDestTable.getRowCount() > 0) {
                                    int row = shortestDistanceDestTable.getRowCount() - 1;
                                    ((DefaultTableModel) shortestDistanceDestTable.getModel()).removeRow(row);
                                }

                                LinkedHashMap<String, Long> valueMap = new LinkedHashMap<>();
                                for (MyNode n : nodes) {
                                    if (n.getCurrentValue() > 0) {
                                        valueMap.put(n.getName(), (long) n.getCurrentValue());
                                    }
                                }
                                valueMap = MySequentialGraphSysUtil.sortMapByLongValue(valueMap);

                                int i = 0;
                                for (String n : valueMap.keySet()) {
                                    ((MyNode) MySequentialGraphVars.g.vRefs.get(n)).setCurrentValue(valueMap.get(n));
                                    ((DefaultTableModel) shortestDistanceDestTable.getModel()).addRow(new String[]{
                                            "" + (++i),
                                            MySequentialGraphSysUtil.getNodeName(n),
                                            MyMathUtil.getCommaSeperatedNumber(valueMap.get(n))});
                                }

                                //updateTopLevelCharts();
                                //updateTableInfos();

                                MySequentialGraphVars.getSequentialGraphViewer().revalidate();
                                MySequentialGraphVars.getSequentialGraphViewer().repaint();

                                pb.updateValue(100, 100);
                                pb.dispose();
                            } else if (shortestDistanceMenu.getSelectedIndex() == 0) {
                                MyProgressBar pb = new MyProgressBar(false);
                                Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
                                for (MyNode n : nodes) {
                                    if (n.getCurrentValue() == 0) {
                                        n.setCurrentValue(n.getOriginalValue());
                                    }
                                }

                                while (shortestDistanceDestTable.getRowCount() > 0) {
                                    int row = shortestDistanceDestTable.getRowCount() - 1;
                                    ((DefaultTableModel) shortestDistanceDestTable.getModel()).removeRow(row);
                                }

                                float maxValue = 0f;
                                LinkedHashMap<String, Long> valueMap = new LinkedHashMap<>();
                                for (MyNode n : nodes) {
                                    valueMap.put(n.getName(), (long) n.getCurrentValue());
                                    if (maxValue < n.getCurrentValue()) {
                                        maxValue = n.getCurrentValue();
                                    }
                                }
                                valueMap = MySequentialGraphSysUtil.sortMapByLongValue(valueMap);
                                if (maxValue > 0) {
                                    MySequentialGraphVars.g.MX_N_VAL = maxValue;
                                }

                                int i = 0;
                                for (String n : valueMap.keySet()) {
                                    ((MyNode) MySequentialGraphVars.g.vRefs.get(n)).setCurrentValue(valueMap.get(n));
                                    ((DefaultTableModel) shortestDistanceDestTable.getModel()).addRow(new String[]{
                                            "" + (++i),
                                            MySequentialGraphSysUtil.getNodeName(n),
                                            MyMathUtil.getCommaSeperatedNumber(valueMap.get(n))});
                                }

                                //updateTopLevelCharts();
                                //updateTableInfos();

                                MySequentialGraphVars.getSequentialGraphViewer().revalidate();
                                MySequentialGraphVars.getSequentialGraphViewer().repaint();

                                pb.updateValue(100, 100);
                                pb.dispose();
                            }

                            MySequentialGraphVars.app.getSequentialGraphDashboard().graphLevelShortestAverageDistanceDistributionLineChart.decorate();
                            MySequentialGraphVars.app.getSequentialGraphDashboard().graphLevelShortestDistanceUnreachableNodeCountDistributionLineChart.decorate();
                            MySequentialGraphVars.app.getSequentialGraphDashboard().graphLevelShortestDistanceNodeValueDistributionLineChart.decorate();
                            MySequentialGraphVars.app.getSequentialGraphDashboard().graphLevelAverageNodeValueByDistanceDistributionLineChart.decorate();
                        }
                    }).start();
                }
            });

            shortestDistanceDestTablePanel.add(shortestDistanceMenu, BorderLayout.NORTH);
            //updateTopLevelCharts();
            updateTableInfos();

            MySequentialGraphVars.app.getSequentialGraphDashboard().graphLevelShortestAverageDistanceDistributionLineChart.decorate();
            MySequentialGraphVars.app.getSequentialGraphDashboard().graphLevelShortestDistanceUnreachableNodeCountDistributionLineChart.decorate();
            MySequentialGraphVars.app.getSequentialGraphDashboard().graphLevelShortestDistanceNodeValueDistributionLineChart.decorate();
            MySequentialGraphVars.app.getSequentialGraphDashboard().graphLevelAverageNodeValueByDistanceDistributionLineChart.decorate();

            MySequentialGraphVars.currentMaxShortestDistance = shortestDistanceMenu.getItemCount()-2;

            MySequentialGraphVars.getSequentialGraphViewer().revalidate();
            MySequentialGraphVars.getSequentialGraphViewer().repaint();
            pb.updateValue(100, 100);
            pb.dispose();
            isTableUpdating = false;
        } catch (Exception ex) {
            pb.updateValue(100, 100);
            pb.dispose();
            ex.printStackTrace();
        }
    }

    public void updateTopLevelCharts() {
        MySequentialGraphVars.app.getSequentialGraphDashboard().graphLevelAverageValuesByDepthLineChart.decorate();
        MySequentialGraphVars.app.getSequentialGraphDashboard().graphLevelUniqueNodesByDepthLineChart.setUniqueNodesByDepthLineChart();
        MySequentialGraphVars.app.getSequentialGraphDashboard().graphLevelContributionByDepthLineChart.decorate();
        MySequentialGraphVars.app.getSequentialGraphDashboard().graphLevelPredecessorSuccessorByDepthLineChart.setAllCharts();
        MySequentialGraphVars.app.getSequentialGraphDashboard().graphLevelDurationByDepthLineChart.decorate();
        MySequentialGraphVars.app.getSequentialGraphDashboard().graphLevelReachTimeByDepthLineChart.decorate();
    }

    private void setShortestInDistancedNodes() {
        if (isTableUpdating || shortestDistanceDestTable.getSelectedRow() == -1) return;
        String fromTableNodeName = shortestDistanceDestTable.getValueAt(shortestDistanceDestTable.getSelectedRow(), 1).toString();
        if (shortestDistanceSourceNode != null && fromTableNodeName.equals(shortestDistanceSourceNode.getName())) {return;}

        isTableUpdating = true;
        MyProgressBar pb = new MyProgressBar(false);
        try {
            shortestDistanceSourceNode = (MyNode) MySequentialGraphVars.g.vRefs.get(fromTableNodeName);
            Set<MyNode> topLevelPredecessors = new HashSet<>(MySequentialGraphVars.g.getPredecessors(shortestDistanceSourceNode));
            topLevelPredecessors.add(shortestDistanceSourceNode);
            Queue<MyNode> Qu = new LinkedList<>();
            visitedNodes = new HashSet();
            Qu.add(shortestDistanceSourceNode);
            Map<MyNode, Integer> distanceMap =  new HashMap();

            int maxDistance = 0;
            distanceMap.put(shortestDistanceSourceNode, 0);
            while (!Qu.isEmpty()) {
                MyNode successor = Qu.remove();
                Collection<MyNode> predecessors = MySequentialGraphVars.g.getPredecessors(successor);
                for (MyNode predecessor : predecessors) {
                    if (!visitedNodes.contains(predecessor)) {
                        int distance = distanceMap.get(successor) + 1;
                        predecessor.shortestInDistance = distance;
                        if (distance > maxDistance) {
                            maxDistance = distance;
                        }
                        distanceMap.put(predecessor, distance);
                        Qu.add(predecessor);
                    }
                }
                visitedNodes.add(successor);
            }

            while (shortestDistanceSourceTable.getRowCount() > 0) {
                int row = shortestDistanceSourceTable.getRowCount() - 1;
                ((DefaultTableModel) shortestDistanceSourceTable.getModel()).removeRow(row);
            }

            LinkedHashMap<String, Long> valueMap = new LinkedHashMap<>();
            float maxNodeVal = 0f;
            TreeSet<Float> distances = new TreeSet<>();
            Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
            for (MyNode n : nodes) {
                if (visitedNodes.contains(n)) {
                    if (n == shortestDistanceSourceNode && visitedNodes.size() == 1) {
                        n.setCurrentValue(0);
                        continue;
                    } else {
                        distances.add(n.shortestInDistance);
                    }
                    valueMap.put(n.getName(), (long) n.getCurrentValue());
                } else {
                    n.setCurrentValue(0);
                }
            }
            valueMap = MySequentialGraphSysUtil.sortMapByLongValue(valueMap);
            pb.updateValue(80, 100);

            int i=0;
            if (visitedNodes.size() > 1) {
                for (String n : valueMap.keySet()) {
                    ((DefaultTableModel) shortestDistanceSourceTable.getModel()).addRow(new String[]{
                        "" + (++i),
                        MySequentialGraphSysUtil.getNodeName(n),
                        MyMathUtil.getCommaSeperatedNumber(valueMap.get(n))});
                }

                for (float distance : distances) {
                    if (distance > maxNodeVal) {
                        maxNodeVal = distance;
                    }
                }
            }

            shortestDistanceDestTablePanel.remove(shortestDistanceMenu);
            shortestDistanceMenu = new JComboBox();
            shortestDistanceMenu.setFocusable(false);
            shortestDistanceMenu.setFont(MySequentialGraphVars.tahomaPlainFont10);
            shortestDistanceMenu.addItem("DISTANCE");
            for (float distance : distances) {
                shortestDistanceMenu.addItem("" + (int) distance);
            }
            shortestDistanceMenu.addActionListener(new ActionListener() {
                @Override public void actionPerformed(ActionEvent e) {
                    new Thread(new Runnable() {
                        @Override public void run() {
                            if (shortestDistanceMenu.getSelectedIndex() > 0) {
                                MyProgressBar pb = new MyProgressBar(false);
                                Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
                                for (MyNode n : nodes) {
                                    if (n.getCurrentValue() > 0) {
                                        if (Integer.parseInt(shortestDistanceMenu.getSelectedItem().toString()) != n.shortestInDistance) {
                                            if (n.getCurrentValue() != 0) {
                                                n.setOriginalValue(n.getCurrentValue());
                                                n.setCurrentValue(0);
                                            }
                                        }
                                    } else if (n.shortestInDistance == Integer.parseInt(shortestDistanceMenu.getSelectedItem().toString()) && n.getCurrentValue() == 0) {
                                        n.setCurrentValue(n.getOriginalValue());
                                    }

                                    if (MySequentialGraphVars.g.MX_N_VAL < n.getCurrentValue()) {
                                        MySequentialGraphVars.g.MX_N_VAL = n.getCurrentValue();
                                    }
                                }
                                pb.updateValue(40, 100);

                                while (shortestDistanceSourceTable.getRowCount() > 0) {
                                    int row = shortestDistanceSourceTable.getRowCount() - 1;
                                    ((DefaultTableModel) shortestDistanceSourceTable.getModel()).removeRow(row);
                                }
                                pb.updateValue(70, 100);

                                LinkedHashMap<String, Long> valueMap = new LinkedHashMap<>();
                                for (MyNode n : nodes) {
                                    if (n.getCurrentValue() > 0) {
                                        valueMap.put(n.getName(), (long) n.getCurrentValue());
                                    }
                                }
                                valueMap = MyDirectGraphSysUtil.sortMapByLongValue(valueMap);
                                pb.updateValue(80, 100);

                                int i = 0;
                                for (String n : valueMap.keySet()) {
                                    ((MyNode) MySequentialGraphVars.g.vRefs.get(n)).setCurrentValue(valueMap.get(n));
                                    ((DefaultTableModel) shortestDistanceSourceTable.getModel()).addRow(
                                            new String[]{"" + (++i),
                                                MySequentialGraphSysUtil.getNodeName(n),
                                                MyMathUtil.getCommaSeperatedNumber(valueMap.get(n))
                                            }
                                    );
                                }

                                updateTopLevelCharts();
                                updateTableInfos();

                                MySequentialGraphVars.getSequentialGraphViewer().revalidate();
                                MySequentialGraphVars.getSequentialGraphViewer().repaint();
                                pb.updateValue(100, 100);
                                pb.dispose();
                            } else if (shortestDistanceMenu.getSelectedIndex() == 0) {
                                Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
                                for (MyNode n : nodes) {
                                    if (n.getCurrentValue() == 0) {
                                        n.setCurrentValue(n.getOriginalValue());
                                    }
                                }

                                while (shortestDistanceSourceTable.getRowCount() > 0) {
                                    int row = shortestDistanceSourceTable.getRowCount() - 1;
                                    ((DefaultTableModel) shortestDistanceSourceTable.getModel()).removeRow(row);
                                }

                                float maxValue = 0f;
                                LinkedHashMap<String, Long> valueMap = new LinkedHashMap<>();
                                for (MyNode n : nodes) {
                                    valueMap.put(n.getName(), (long) n.getCurrentValue());
                                    if (maxValue < n.getCurrentValue()) {
                                        maxValue = n.getCurrentValue();
                                    }
                                }
                                valueMap = MySequentialGraphSysUtil.sortMapByLongValue(valueMap);
                                if (maxValue > 0) {
                                    MySequentialGraphVars.g.MX_N_VAL = maxValue;
                                }

                                int i=0;
                                for (String n : valueMap.keySet()) {
                                    ((MyNode) MySequentialGraphVars.g.vRefs.get(n)).setCurrentValue(valueMap.get(n));
                                    ((DefaultTableModel) shortestDistanceSourceTable.getModel()).addRow(
                                        new String[]{"" + (++i),
                                            MySequentialGraphSysUtil.getNodeName(n),
                                            MyMathUtil.getCommaSeperatedNumber(valueMap.get(n))
                                        }
                                    );
                                }
                            }

                            updateTopLevelCharts();
                            updateTableInfos();

                            MySequentialGraphVars.getSequentialGraphViewer().revalidate();
                            MySequentialGraphVars.getSequentialGraphViewer().repaint();
                            pb.updateValue(100, 100);
                            pb.dispose();
                        }
                    }).start();
                }
            });
            shortestDistanceDestTablePanel.add(shortestDistanceMenu, BorderLayout.NORTH);

            if (maxNodeVal > 0) {
                MySequentialGraphVars.g.MX_N_VAL = maxNodeVal;
            }
            shortestDistanceDestTablePanel.add(shortestDistanceMenu, BorderLayout.NORTH);

            updateTopLevelCharts();
            updateTableInfos();

            MySequentialGraphVars.getSequentialGraphViewer().revalidate();
            MySequentialGraphVars.getSequentialGraphViewer().repaint();
            pb.updateValue(100, 100);
            pb.dispose();
            isTableUpdating = false;
        } catch (Exception ex) {
            pb.updateValue(100, 100);
            pb.dispose();
            ex.printStackTrace();
        }
    }

    private JPanel setPathFindTable() {
        try {
            this.shortestDistanceSourceTablePanel = new JPanel();
            this.shortestDistanceSourceTablePanel.setLayout(new BorderLayout(2, 0));
            this.shortestDistanceSourceTablePanel.setBackground(Color.WHITE);

            this.shortestDistanceOptionMenu = new JComboBox();
            this.shortestDistanceOptionMenu.setFont(MySequentialGraphVars.tahomaPlainFont10);
            this.shortestDistanceOptionMenu.setFocusable(false);
            this.shortestDistanceOptionMenu.addActionListener(new ActionListener() {
                @Override public void actionPerformed(ActionEvent e) {
                    new Thread(new Runnable() {
                        @Override public void run() {
                            if (shortestDistanceOptionMenu.getSelectedIndex() == 0) {
                                setShortestDistanceConfiguration();
                            }
                            shortestDistanceSourceNode = null;
                        }
                    }).start();
                }
            });
            //pathMenu.addItem("PATH EXPLORATION");
            this.shortestDistanceOptionMenu.addItem("SELECT DISTANCE OPTION");
            this.shortestDistanceOptionMenu.addItem("SHORTEST OUT-DISTANCE");
            //this.shortestDistancePathMenu.addItem("SHORTEST IN-DISTANCE");

            String[] fromColumns = {"NO.", "SOURCE", "R. N.", "V."};
            String[][] fromData = {};
            DefaultTableModel shortestDistanceSourceTableModel = new DefaultTableModel(fromData, fromColumns);
            this.shortestDistanceSourceTable = new JTable(shortestDistanceSourceTableModel) {
                public String getToolTipText(MouseEvent e) {
                    String tip = null;
                    Point p = e.getPoint();
                    int rowIndex = rowAtPoint(p);
                    int colIndex = columnAtPoint(p);
                    try {
                        tip = getValueAt(rowIndex, colIndex).toString();
                    } catch (RuntimeException e1) {
                    }
                    return tip;
                }
            };

            String[] fromTableTooltips = {"NO.", "SOURCE NODE", "NO. OF REACHABLE NODES", "AVERAGE SHORTEST DISTANCE"};
            MyTableToolTipper fromTableTooltipHeader = new MyTableToolTipper(this.shortestDistanceSourceTable.getColumnModel());
            fromTableTooltipHeader.setToolTipStrings(fromTableTooltips);
            this.shortestDistanceSourceTable.setTableHeader(fromTableTooltipHeader);

            Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
            LinkedHashMap<String, Long> sourceNodeValueMap = new LinkedHashMap<>();
            int i = 0;
            for (MyNode n : nodes) {
                sourceNodeValueMap.put(n.getName(), (long) n.getAverageShortestDistance());
            }
            //sourceNodeValueMap = MySequentialGraphSysUtil.sortMapByLongValue(sourceNodeValueMap);

            // Create a Comparator to compare values in reverse order
            Comparator<Map.Entry<String, Long>> valueComparator = (e1, e2) -> {
                if (e1.getValue() < e2.getValue()) {
                    return -1; // Return negative value to indicate e1 should appear before e2
                } else if (e1.getValue() > e2.getValue()) {
                    return 1; // Return positive value to indicate e1 should appear after e2
                } else {
                    return 0; // Return zero if values are equal
                }
            };

            // Convert the entrySet to a List and sort it using the valueComparator
            List<Map.Entry<String, Long>> sortedEntries = new ArrayList<>(sourceNodeValueMap.entrySet());
            Collections.sort(sortedEntries, valueComparator);

            // Create a new LinkedHashMap to store the entries in the desired order
            LinkedHashMap<String, Long> reversedMap = new LinkedHashMap<>();

            // Iterate over the sorted list and put the entries into the reversedMap
            for (Map.Entry<String, Long> entry : sortedEntries) {
                reversedMap.put(entry.getKey(), entry.getValue());
            }

            for (String n : reversedMap.keySet()) {
                if (reversedMap.get(n) == 0) continue;
                shortestDistanceSourceTableModel.addRow(new String[]{
                    String.valueOf(++i),
                    MySequentialGraphSysUtil.getNodeName(n),
                    MyMathUtil.getCommaSeperatedNumber((MySequentialGraphVars.g.getVertexCount()-1)-((MyNode)MySequentialGraphVars.g.vRefs.get(n)).getUnreachableNodeCount()),
                    MyMathUtil.getCommaSeperatedNumber(reversedMap.get(n))});
            }

            this.shortestDistanceSourceTable.setRowHeight(19);
            this.shortestDistanceSourceTable.setBackground(Color.WHITE);
            this.shortestDistanceSourceTable.setFont(MySequentialGraphVars.f_pln_10);
            this.shortestDistanceSourceTable.getTableHeader().setFont(MySequentialGraphVars.tahomaBoldFont10);
            this.shortestDistanceSourceTable.getTableHeader().setOpaque(false);
            this.shortestDistanceSourceTable.getTableHeader().setBackground(new Color(0, 0, 0, 0f));
            this.shortestDistanceSourceTable.getColumnModel().getColumn(0).setPreferredWidth(40);
            this.shortestDistanceSourceTable.getColumnModel().getColumn(1).setPreferredWidth(60);
            this.shortestDistanceSourceTable.getColumnModel().getColumn(2).setPreferredWidth(40);
            this.shortestDistanceSourceTable.getColumnModel().getColumn(3).setPreferredWidth(40);

            JTextField sourceNodeSearchTxt = new JTextField();
            JButton sourceNodeSelectBtn = new JButton();
            sourceNodeSearchTxt.setToolTipText("TYPE A NODE TO SEARCH");
            sourceNodeSearchTxt.setBackground(Color.WHITE);

            sourceNodeSearchTxt.setBorder(BorderFactory.createLoweredSoftBevelBorder());
            JPanel sourceNodeSearchAndSavePanel = MyTableUtil.searchAndSaveDataPanelForJTable2(this, sourceNodeSearchTxt, sourceNodeSelectBtn, shortestDistanceSourceTableModel, this.shortestDistanceSourceTable);
            sourceNodeSelectBtn.setPreferredSize(new Dimension(40, 25));
            sourceNodeSelectBtn.removeActionListener(this);
            sourceNodeSelectBtn.removeActionListener(this);
            sourceNodeSearchAndSavePanel.remove(sourceNodeSelectBtn);
            sourceNodeSearchTxt.setFont(MySequentialGraphVars.f_bold_10);

            add(sourceNodeSearchAndSavePanel, BorderLayout.SOUTH);
            this.shortestDistanceSourceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            this.shortestDistanceSourceTable.setSelectionBackground(Color.LIGHT_GRAY);
            this.shortestDistanceSourceTable.setForeground(Color.BLACK);
            this.shortestDistanceSourceTable.setSelectionForeground(Color.BLACK);
            this.shortestDistanceSourceTable.setFocusable(false);
            this.shortestDistanceSourceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            this.shortestDistanceSourceTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent event) {
                    new Thread(new Runnable() {
                        @Override public void run() {
                            if (shortestDistanceOptionMenu.getSelectedIndex() == 1) {
                                setShortestOutDistancedNodes();
                            }
                        }
                    }).start();
                }
            });

            JScrollPane fromTableScrollPane = new JScrollPane(shortestDistanceSourceTable);
            fromTableScrollPane.setOpaque(false);
            fromTableScrollPane.setBackground(new Color(0, 0, 0, 0f));
            fromTableScrollPane.setPreferredSize(new Dimension(150, 500));
            fromTableScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(7, 0));
            fromTableScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 7));

            this.shortestDistanceSourceTablePanel.add(shortestDistanceOptionMenu, BorderLayout.NORTH);
            this.shortestDistanceSourceTablePanel.add(fromTableScrollPane, BorderLayout.CENTER);
            this.shortestDistanceSourceTablePanel.add(sourceNodeSearchAndSavePanel, BorderLayout.SOUTH);

            this.shortestDistanceDestTablePanel = new JPanel();
            this.shortestDistanceDestTablePanel.setLayout(new BorderLayout(2, 2));
            this.shortestDistanceDestTablePanel.setBackground(Color.WHITE);

            this.shortestDistanceMenu.setFont(MySequentialGraphVars.tahomaPlainFont10);
            this.shortestDistanceMenu.setFocusable(false);
            this.shortestDistanceMenu.addActionListener(new ActionListener() {
                @Override public void actionPerformed(ActionEvent e) {
                    new Thread(new Runnable() {
                        @Override public void run() {
                            if (shortestDistanceMenu.getSelectedIndex() > 0) {
                                MySequentialGraphVars.app.getSequentialGraphDashboard().graphLevelShortestAverageDistanceDistributionLineChart.decorate();
                                MySequentialGraphVars.app.getSequentialGraphDashboard().graphLevelShortestDistanceUnreachableNodeCountDistributionLineChart.decorate();
                                MySequentialGraphVars.app.getSequentialGraphDashboard().graphLevelShortestDistanceNodeValueDistributionLineChart.decorate();
                                MySequentialGraphVars.app.getSequentialGraphDashboard().graphLevelAverageNodeValueByDistanceDistributionLineChart.decorate();
                            }
                        }
                    }).start();
                }
            });

            String [] toColumns = {"NO.", "DEST", "V."};
            String [][] toData = {};
            DefaultTableModel shortestDistanceDestTableModel = new DefaultTableModel(toData, toColumns);
            this.shortestDistanceDestTable = new JTable(shortestDistanceDestTableModel) {
                //Implement table cell tool tips.
                public String getToolTipText(MouseEvent e) {
                    String tip = null;
                    Point p = e.getPoint();
                    int rowIndex = rowAtPoint(p);
                    int colIndex = columnAtPoint(p);

                    try {
                        tip = getValueAt(rowIndex, colIndex).toString();
                    } catch (RuntimeException e1) {
                        e1.printStackTrace();
                    }
                    return tip;
                }
            };

            String[] toTableTooltips = {"NO.", "DESTINATION NODE", "DISTANCE FROM SOURCE NODE"};
            MyTableToolTipper toTableTooltipHeader = new MyTableToolTipper(this.shortestDistanceDestTable.getColumnModel());
            toTableTooltipHeader.setToolTipStrings(toTableTooltips);
            this.shortestDistanceDestTable.setTableHeader(toTableTooltipHeader);

            i = 0;
            for (MyNode n : nodes) {
                shortestDistanceDestTableModel.addRow(new String[]{"" + (++i), MySequentialGraphSysUtil.getNodeName(n.getName()), "0"});
            }

            this.shortestDistanceDestTable.setRowHeight(19);
            this.shortestDistanceDestTable.setBackground(Color.WHITE);
            this.shortestDistanceDestTable.setFont(MySequentialGraphVars.f_pln_10);
            this.shortestDistanceDestTable.getTableHeader().setFont(MySequentialGraphVars.tahomaBoldFont10);
            this.shortestDistanceDestTable.getTableHeader().setOpaque(false);
            this.shortestDistanceDestTable.getTableHeader().setBackground(new Color(0, 0, 0, 0f));
            this.shortestDistanceDestTable.getColumnModel().getColumn(0).setPreferredWidth(45);
            this.shortestDistanceDestTable.getColumnModel().getColumn(1).setPreferredWidth(80);
            this.shortestDistanceDestTable.getColumnModel().getColumn(2).setPreferredWidth(40);
            this.shortestDistanceDestTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            this.shortestDistanceDestTable.setSelectionBackground(Color.LIGHT_GRAY);
            this.shortestDistanceDestTable.setSelectionForeground(Color.BLACK);
            this.shortestDistanceDestTable.setForeground(Color.BLACK);
            this.shortestDistanceDestTable.setFocusable(false);
            this.shortestDistanceDestTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            this.shortestDistanceDestTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent event) {
                    new Thread(new Runnable() {
                        @Override public void run() {
                            if (shortestDistanceOptionMenu.getSelectedIndex() == 2) {
                                setShortestInDistancedNodes();
                            }
                        }
                    }).start();
                }
            });

            JTextField destNodeSearchTxt = new JTextField();
            JButton runBtn = new JButton("RUN");
            runBtn.setFocusable(false);
            runBtn.setFont(MySequentialGraphVars.tahomaPlainFont12);

            destNodeSearchTxt.setBackground(Color.WHITE);
            destNodeSearchTxt.setFont(MySequentialGraphVars.f_bold_10);
            destNodeSearchTxt.setToolTipText("TYPE A NODE NAME TO SEARCH");
            destNodeSearchTxt.setBorder(BorderFactory.createLoweredSoftBevelBorder());
            JPanel destNodeSearchAndSavePanel = MyTableUtil.searchAndSaveDataPanelForJTable2(this, destNodeSearchTxt, runBtn, shortestDistanceDestTableModel, this.shortestDistanceDestTable);
            destNodeSearchAndSavePanel.remove(runBtn);
            add(destNodeSearchAndSavePanel, BorderLayout.SOUTH);

            JScrollPane destNodeTableScrollPane = new JScrollPane(this.shortestDistanceDestTable);
            destNodeTableScrollPane.setOpaque(false);
            destNodeTableScrollPane.setBackground(new Color(0, 0, 0, 0f));
            destNodeTableScrollPane.setPreferredSize(new Dimension(150, 500));
            destNodeTableScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(7, 0));
            destNodeTableScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 7));

            this.shortestDistanceDestTablePanel.add(this.shortestDistanceMenu, BorderLayout.NORTH);
            this.shortestDistanceDestTablePanel.add(destNodeTableScrollPane, BorderLayout.CENTER);
            this.shortestDistanceDestTablePanel.add(destNodeSearchAndSavePanel, BorderLayout.SOUTH);

            JPanel tablePanel = new JPanel();
            tablePanel.setBackground(Color.WHITE);
            tablePanel.setLayout(new GridLayout(2, 1));
            tablePanel.add(this.shortestDistanceSourceTablePanel);
            tablePanel.add(this.shortestDistanceDestTablePanel);
            return tablePanel;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public void decorateGraphViewer(JPanel graphViewer) {
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        this.setBackground(Color.WHITE);
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));

        this.nodeValueBarChart.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.nodeLabelBarChart.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.edgeValueBarChart.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.edgeLabelBarChart.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.weightedNodeColor.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.clusteringSectorLabel.setFont(MySequentialGraphVars.tahomaPlainFont12);

        this.selectedNodeNeighborNodeTypeSelector.setBackground(Color.WHITE);
        this.depthNeighborNodeTypeSelector.setToolTipText("SELECT A NEIGHBOR NODE TYPE FOR SELECTED NODES");
        this.selectedNodeNeighborNodeTypeSelector.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.selectedNodeNeighborNodeTypeSelector.setFocusable(false);
        this.selectedNodeNeighborNodeTypeSelector.addActionListener(this);
        this.selectedNodeNeighborNodeTypeSelector.setVisible(false);

        this.depthNeighborNodeTypeSelector.setBackground(Color.WHITE);
        this.depthNeighborNodeTypeSelector.setToolTipText("SELECT A NEIGHBOR NODE TYPE FOR SELECTED NODES");
        this.depthNeighborNodeTypeSelector.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.depthNeighborNodeTypeSelector.setFocusable(false);
        this.depthNeighborNodeTypeSelector.addActionListener(this);
        this.depthNeighborNodeTypeSelector.setVisible(false);

        this.depthSelecter.setBackground(Color.WHITE);
        this.depthSelecter.setFocusable(false);
        this.depthSelecter.setToolTipText("SELECT A DEPTH");
        this.depthSelecter.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.depthSelecter.addActionListener(this);
        MyViewerComponentControllerUtil.setDepthValueSelecterMenu();

        this.depthExcludeSelecter.setBackground(Color.WHITE);
        this.depthExcludeSelecter.setFocusable(false);
        this.depthExcludeSelecter.setToolTipText("SELECT A DEPTH");
        this.depthExcludeSelecter.setFont(MySequentialGraphVars.tahomaPlainFont12);
        MyViewerComponentControllerUtil.setDepthExcludeSelecterMenu();

        this.depthExcludeSymbolSelecter.setBackground(Color.WHITE);
        this.depthExcludeSymbolSelecter.setFocusable(false);
        this.depthExcludeSymbolSelecter.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.depthExcludeSymbolSelecter.addItem("");
        this.depthExcludeSymbolSelecter.addItem("==");
        this.depthExcludeSymbolSelecter.addItem("!=");
        this.depthExcludeSymbolSelecter.addItem("<");
        this.depthExcludeSymbolSelecter.addItem(">");

        this.nodeValueExcludeSymbolSelecter.setFocusable(false);
        this.nodeValueExcludeSymbolSelecter.setBackground(Color.WHITE);

        this.edgeValueExcludeSymbolSelecter.setFocusable(false);
        this.edgeValueExcludeSymbolSelecter.setBackground(Color.WHITE);

        this.nodeValueSelecterLabel.setToolTipText("NODE VALUE");
        this.nodeValueSelecterLabel.setBackground(Color.WHITE);
        this.nodeValueSelecterLabel.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.nodeValueSelecter.setBackground(Color.WHITE);
        this.nodeValueSelecter.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.nodeValueSelecter.setFocusable(false);
        if (MySequentialGraphVars.isTimeOn) {
            String[] nodeValueTooltips = new String[34];
            nodeValueTooltips[0] = "CONTRIBUTION";
            nodeValueTooltips[1] = "IN-CONTRIBUTION";
            nodeValueTooltips[2] = "AVG. IN-CONTRIBUTION";
            nodeValueTooltips[3] = "OUT-CONTRIBUTION";
            nodeValueTooltips[4] = "AVG. OUT-CONTRIBUTION";
            nodeValueTooltips[5] = "CONTRIBUTION DIFFERENCE";
            nodeValueTooltips[6] = "UNIQUE CONTRIBUTION";
            nodeValueTooltips[7] = "IN-NODES";
            nodeValueTooltips[8] = "OUT-NODES";
            nodeValueTooltips[9] = "INOUT NODE DIFFERENCE";
            nodeValueTooltips[10] = "ENDING POSITION NODE COUNT";
            nodeValueTooltips[11] = "AVERAGE SHORTEST DISTANCE";
            nodeValueTooltips[12] = "TOTAL RECURSIVE LENGTH";
            nodeValueTooltips[13] = "MIN. RECURSIVE LEGNTH";
            nodeValueTooltips[14] = "MAX. RECURSIVE LENGTH";
            nodeValueTooltips[15] = "AVERAGE RECURSIVE LENGTH";
            nodeValueTooltips[16] = "AVERAGE RECURSIVE TIME";
            nodeValueTooltips[17] = "DURATION";
            nodeValueTooltips[18] = "AVERAGE DURATION";
            nodeValueTooltips[19] = "MAX. DURATION";
            nodeValueTooltips[20] = "MIN. DURATION";
            nodeValueTooltips[21] = "AVERAGE REACH TIME";
            nodeValueTooltips[22] = "TOTAL REACH TIME";
            nodeValueTooltips[23] = "RECURRENCE COUNT";
            nodeValueTooltips[24] = "TOTAL RECURRENCE TIME";
            nodeValueTooltips[25] = "MAX. RECURRENCE TIME";
            nodeValueTooltips[26] = "MIN. RECURRENCE TIME";
            nodeValueTooltips[27] = "ITEMSET LENGTH";
            nodeValueTooltips[28] = "BTWEENESS";
            nodeValueTooltips[29] = "CLOSENESS";
            nodeValueTooltips[30] = "EIGENVECTOR";
            nodeValueTooltips[31] = "PAGERANK";
            nodeValueTooltips[32] = "STARTING POSITION NODE COUNT";
            nodeValueTooltips[33] = "UNREACHABLE NODE COUNT";
            this.nodeValueSelecter.setRenderer(new MyComboBoxTooltipRenderer(nodeValueTooltips));
        } else {
            String[] nodeValueTooltips = new String[24];
            nodeValueTooltips[0] = "CONTRIBUTION";
            nodeValueTooltips[1] = "IN-CONTRIBUTION";
            nodeValueTooltips[2] = "AVG. IN-CONTRIBUTION";
            nodeValueTooltips[3] = "OUT-CONTRIBUTION";
            nodeValueTooltips[4] = "AVG. OUT-CONTRIBUTION";
            nodeValueTooltips[5] = "CONTRIBUTION DIFFERENCE";
            nodeValueTooltips[6] = "UNIQUE CONTRIBUTION";
            nodeValueTooltips[7] = "IN-NODES";
            nodeValueTooltips[8] = "OUT-NODES";
            nodeValueTooltips[9] = "INOUT NODE DIFFERENCE";
            nodeValueTooltips[10] = "ENDING POSITION NODE COUNT";
            nodeValueTooltips[11] = "AVERAGE SHORTEST DISTANCE";
            nodeValueTooltips[12] = "TOTAL RECURSIVE LENGTH";
            nodeValueTooltips[13] = "MIN. RECURSIVE LEGNTH";
            nodeValueTooltips[14] = "MAX. RECURSIVE LENGTH";
            nodeValueTooltips[15] = "AVERAGE RECURSIVE LENGTH";
            nodeValueTooltips[16] = "RECURRENCE";
            nodeValueTooltips[17] = "ITEMSET LENGTH";
            nodeValueTooltips[18] = "BTWEENESS";
            nodeValueTooltips[19] = "CLOSENESS";
            nodeValueTooltips[20] = "EIGENVECTOR";
            nodeValueTooltips[21] = "PAGERANK";
            nodeValueTooltips[22] = "STARTING POSITION NODE COUNT";
            nodeValueTooltips[23] = "UNREACHABLE NODE COUNT";
            this.nodeValueSelecter.setRenderer(new MyComboBoxTooltipRenderer(nodeValueTooltips));
        }
        MyViewerComponentControllerUtil.setNodeValueComboBoxMenu();

        this.edgeValueLabel.setToolTipText("EDGE VALUE");
        this.edgeValueLabel.setBackground(Color.WHITE);
        this.edgeValueLabel.setFont(MySequentialGraphVars.tahomaPlainFont12);

        this.edgeLabelLabel.setToolTipText("EDGE LABEL");
        this.edgeLabelLabel.setBackground(Color.WHITE);
        this.edgeLabelLabel.setFont(MySequentialGraphVars.tahomaPlainFont12);

        this.edgeValueSelecter.setBackground(Color.WHITE);
        this.edgeValueSelecter.setToolTipText("SELECT AN EDGE VALUE");
        this.edgeValueSelecter.setFocusable(false);
        this.edgeValueSelecter.setFont(MySequentialGraphVars.tahomaPlainFont12);
        if (MySequentialGraphVars.isTimeOn) {
            String[] edgeValueTooltips = new String[12];
            edgeValueTooltips[0] = "NONE";
            edgeValueTooltips[1] = "DEFAULT";
            edgeValueTooltips[2] = "CONTRIBUTION";
            edgeValueTooltips[3] = "UNIQUE CONTRIBUTION";
            edgeValueTooltips[4] = "AVERAGE TIME";
            edgeValueTooltips[5] = "TOTAL TIME";
            edgeValueTooltips[6] = "MAX. TIME";
            edgeValueTooltips[7] = "MIN. TIME";
            edgeValueTooltips[8] = "SUPPORT";
            edgeValueTooltips[9] = "CONFIDENCE";
            edgeValueTooltips[10] = "LIFT";
            edgeValueTooltips[11] = "BETWEENESS";
            this.edgeValueSelecter.setRenderer(new MyComboBoxTooltipRenderer(edgeValueTooltips));
        } else {
            String[] edgeValueTooltips = new String[8];
            edgeValueTooltips[0] = "NONE";
            edgeValueTooltips[1] = "DEFAULT";
            edgeValueTooltips[2] = "CONTRIBUTION";
            edgeValueTooltips[3] = "UNIQUE CONTRIBUTION";
            edgeValueTooltips[4] = "SUPPORT";
            edgeValueTooltips[5] = "CONFIDENCE";
            edgeValueTooltips[6] = "LIFT";
            edgeValueTooltips[7] = "BETWEENESS";
            this.edgeValueSelecter.setRenderer(new MyComboBoxTooltipRenderer(edgeValueTooltips));
        }
        MyViewerComponentControllerUtil.setEdgeValueSelecterMenu();

        this.edgeLabelSelecter.setFocusable(false);
        this.edgeLabelSelecter.setToolTipText("SELECT AN EDGE LABEL");
        this.edgeLabelSelecter.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.edgeLabelSelecter.setBackground(Color.WHITE);
        this.edgeLabelSelecter.addItem("NONE");
        for (String edgeLabel : MySequentialGraphVars.userDefinedEdgeLabelSet) {
            this.edgeLabelSelecter.addItem(edgeLabel);
        }
        this.edgeLabelSelecter.addActionListener(this);

        nodeLabelLabel.setToolTipText("NODE LABEL");
        nodeLabelLabel.setBackground(Color.WHITE);
        nodeLabelLabel.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.nodeLabelSelecter.setFocusable(false);
        this.nodeLabelSelecter.setToolTipText("SELECT A NODE LABEL");
        this.nodeLabelSelecter.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.nodeLabelSelecter.setBackground(Color.WHITE);
        this.nodeLabelSelecter.addItem("NONE");
        this.nodeLabelSelecter.addItem("NAME");
        for (String nodeLabel : MySequentialGraphVars.userDefinedNodeLabelSet) {
            this.nodeLabelSelecter.addItem(nodeLabel);
        }
        this.nodeLabelSelecter.setSelectedIndex(1);
        this.nodeLabelSelecter.addActionListener(this);

        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.WHITE);
        topPanel.setLayout(new BorderLayout(0,0));

        this.setNodeValueExcludeSymbolComboBox();
        this.setEdgeValueExcludeSymbolComboBox();

        this.nodeValueExcludeTxt.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.nodeValueExcludeTxt.setToolTipText("ENTER A NUMERIC VALUE");
        this.nodeValueExcludeTxt.setHorizontalAlignment(JTextField.CENTER);
        this.nodeValueExcludeTxt.setBorder(new LineBorder(Color.DARK_GRAY,1));
        this.nodeValueExcludeTxt.setBackground(Color.WHITE);
        this.nodeValueExcludeTxt.setPreferredSize(new Dimension(60, 20));

        this.excludeBtn = new JButton("EXCL.");
        //this.excludeBtn.setBackground(Color.GREEN);
        this.excludeBtn.setToolTipText("EXCLUDE NODES AND EDGES");
        this.excludeBtn.setBackground(Color.WHITE);
        this.excludeBtn.setFocusable(false);
        this.excludeBtn.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.excludeBtn.addActionListener(new MyNodeEdgeExclusionActionListener(this));

        JLabel removeEdgeEmptyLabel = new JLabel("  ");
        removeEdgeEmptyLabel.setBackground(Color.WHITE);
        removeEdgeEmptyLabel.setPreferredSize(new Dimension(50, 20));

        this.nodeValueBarChart.setFocusable(false);
        this.nodeValueBarChart.setToolTipText("NODE VALUE BAR CHART");
        this.nodeValueBarChart.setBackground(Color.WHITE);
        this.nodeValueBarChart.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.nodeValueBarChart.addActionListener(this);

        this.nodeLabelBarChart.setFocusable(false);
        this.nodeLabelBarChart.setToolTipText("NODE LABEL BAR CHART");
        this.nodeLabelBarChart.setBackground(Color.WHITE);
        this.nodeLabelBarChart.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.nodeLabelBarChart.addActionListener(this);
        this.nodeLabelBarChart.setVisible(false);

        this.edgeValueBarChart.setFocusable(false);
        this.edgeValueBarChart.setToolTipText("EDGE VALUE BAR CHART");
        this.edgeValueBarChart.setBackground(Color.WHITE);
        this.edgeValueBarChart.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.edgeValueBarChart.addActionListener(this);

        this.edgeLabelBarChart.setFocusable(false);
        this.edgeLabelBarChart.setToolTipText("EDGE VALUE BAR CHART");
        this.edgeLabelBarChart.setBackground(Color.WHITE);
        this.edgeLabelBarChart.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.edgeLabelBarChart.setVisible(false);
        this.edgeLabelBarChart.addActionListener(this);

        this.endingNodeBarChart.setFocusable(false);
        this.endingNodeBarChart.setToolTipText("ENDING NODE BAR CHART");
        this.endingNodeBarChart.setBackground(Color.WHITE);
        this.endingNodeBarChart.setFont(MySequentialGraphVars.tahomaPlainFont12);

        JLabel nodeValueExcludeOptionLabel = new JLabel("N. V.");
        nodeValueExcludeOptionLabel.setFont(MySequentialGraphVars.tahomaPlainFont12);
        nodeValueExcludeOptionLabel.setBackground(Color.WHITE);

        this.edgeValueExludeLabel.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.edgeValueExludeLabel.setBackground(Color.WHITE);

        this.topLeftPanel.setBackground(Color.WHITE);
        this.topLeftPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        this.topLeftPanel.add(nodeValueExcludeOptionLabel);
        this.topLeftPanel.add(this.nodeValueExcludeSymbolSelecter);
        this.topLeftPanel.add(this.nodeValueExcludeTxt);
        this.topLeftPanel.add(this.edgeValueExludeLabel);
        this.topLeftPanel.add(this.edgeValueExcludeSymbolSelecter);
        this.topLeftPanel.add(this.edgeValueExcludeTxt);

        JLabel nodeLabelExcludeComboBoxMenuLabel = new JLabel( "  N. L.");
        nodeLabelExcludeComboBoxMenuLabel.setFont(MySequentialGraphVars.tahomaPlainFont12);
        nodeLabelExcludeComboBoxMenuLabel.setBackground(Color.WHITE);

        this.nodeLabelExcludeMathSymbolSelecter.setBackground(Color.WHITE);
        this.nodeLabelExcludeMathSymbolSelecter.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.nodeLabelExcludeMathSymbolSelecter.setFocusable(false);
        this.nodeLabelExcludeMathSymbolSelecter.addItem("");
        this.nodeLabelExcludeMathSymbolSelecter.addItem("==");
        this.nodeLabelExcludeMathSymbolSelecter.addItem("!=");
        this.nodeLabelExcludeMathSymbolSelecter.setToolTipText("SELECT AN ARITHMATIC SYMBOL TO EXCLUDE");

        this.nodeLabelExcludeSelecter.setBackground(Color.WHITE);
        this.nodeLabelExcludeSelecter.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.nodeLabelExcludeSelecter.setFocusable(false);
        this.nodeLabelExcludeSelecter.setToolTipText("SELECT A NODE LABEL TO EXCLUDE");
        this.nodeLabelExcludeSelecter.addItem("");
        for (String nodeLabel : MySequentialGraphVars.userDefinedNodeLabelSet) {
            this.nodeLabelExcludeSelecter.addItem(nodeLabel);
        }
        this.nodeLabelExcludeSelecter.addActionListener(new MyNodeLabelSelecterListener(this.nodeLabelExcludeSelecter, this.nodeLabelValueExcludeSelecter));

        this.nodeLabelValueExcludeSelecter.setFocusable(false);
        this.nodeLabelValueExcludeSelecter.setToolTipText("SELECT A NODE LABEL VALUE TO EXCLUDE");
        this.nodeLabelValueExcludeSelecter.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.nodeLabelValueExcludeSelecter.setBackground(Color.WHITE);


        this.edgeValueExcludeTxt.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.edgeValueExcludeTxt.setToolTipText("ENTER A NUMERIC VALUE");
        this.edgeValueExcludeTxt.setHorizontalAlignment(JTextField.CENTER);
        this.edgeValueExcludeTxt.setBorder(new LineBorder(Color.DARK_GRAY,1));
        this.edgeValueExcludeTxt.setBackground(Color.WHITE);
        this.edgeValueExcludeTxt.setPreferredSize(new Dimension(60, 20));

        if (MySequentialGraphVars.userDefinedNodeLabelSet.size() > 0) {
            this.topLeftPanel.add(nodeLabelExcludeComboBoxMenuLabel);
            this.topLeftPanel.add(this.nodeLabelExcludeMathSymbolSelecter);
            this.topLeftPanel.add(this.nodeLabelExcludeSelecter);
            this.topLeftPanel.add(this.nodeLabelValueExcludeSelecter);
        }

        this.edgeLabelExcludeComboBoxMenuLabel.setBackground(Color.WHITE);
        this.edgeLabelExcludeComboBoxMenuLabel.setFont(MySequentialGraphVars.tahomaPlainFont12);

        this.distributionSelecter.setBackground(Color.WHITE);
        this.distributionSelecter.setFont(MySequentialGraphVars.tahomaPlainFont11);
        this.distributionSelecter.setFocusable(false);
        this.distributionSelecter.setToolTipText("SELECT A DISTRIBUTION");
        this.distributionSelecter.addItem("DIST.");
        this.distributionSelecter.addItem("C. D. BY OBJ.");
        if (MySequentialGraphVars.isTimeOn) {
            this.distributionSelecter.addItem("R. T. BY OBJ.");
        }
        String[] distributionSelecterTooltips = new String[15];
        distributionSelecterTooltips[0] = "SELECT A DISTRIBUTION";
        distributionSelecterTooltips[1] = "CONTRIBUTION COUNT DISTRIBUTION BY OBJECT";
        if (MySequentialGraphVars.isTimeOn) {
            distributionSelecterTooltips[2] = "TOTAL REACH TIME DISTRIBUTION BY OBJECT";
        }
        this.distributionSelecter.setRenderer(new MyComboBoxTooltipRenderer(distributionSelecterTooltips));
        this.distributionSelecter.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                if (MySequentialGraphVars.isTimeOn) {
                    if (distributionSelecter.getSelectedIndex() == 1) {
                        new Thread(new Runnable() {
                            @Override public void run() {
                                MyGraphLevelTopLevelNodeContributionCountByObjectIDDistribution graphLevelContributionCountByObjectIDDistribution = new MyGraphLevelTopLevelNodeContributionCountByObjectIDDistribution();
                                graphLevelContributionCountByObjectIDDistribution.enlarge();
                            }
                        }).start();
                    } else if (distributionSelecter.getSelectedIndex() == 2) {
                        new Thread(new Runnable() {
                            @Override public void run() {
                                MyGraphLevelTopLevelReachTimeByObjectIDDistribution graphLevelTopLevelReachTimeByObjectIDDistribution = new MyGraphLevelTopLevelReachTimeByObjectIDDistribution();
                                graphLevelTopLevelReachTimeByObjectIDDistribution.enlarge();
                            }
                        }).start();
                    }
                } else {
                    if (distributionSelecter.getSelectedIndex() == 1) {
                        new Thread(new Runnable() {
                            @Override public void run() {
                                MyGraphLevelTopLevelNodeContributionCountByObjectIDDistribution graphLevelContributionCountByObjectIDDistribution = new MyGraphLevelTopLevelNodeContributionCountByObjectIDDistribution();
                                graphLevelContributionCountByObjectIDDistribution.enlarge();
                            }
                        }).start();
                    }
                }
            }
        });

        this.edgeLabelExcludeMathSymbolSelecter.setBackground(Color.WHITE);
        this.edgeLabelExcludeMathSymbolSelecter.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.edgeLabelExcludeMathSymbolSelecter.setFocusable(false);
        this.edgeLabelExcludeMathSymbolSelecter.addItem("");
        this.edgeLabelExcludeMathSymbolSelecter.addItem("==");
        this.edgeLabelExcludeMathSymbolSelecter.addItem("!=");

        this.edgeLabelExcludeSelecter.setBackground(Color.WHITE);
        this.edgeLabelExcludeSelecter.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.edgeLabelExcludeSelecter.setFocusable(false);
        this.edgeLabelExcludeSelecter.addItem("");
        for (String nodeLabel : MySequentialGraphVars.userDefinedEdgeLabelSet) {
            this.edgeLabelExcludeSelecter.addItem(nodeLabel);
        }
        this.edgeLabelExcludeSelecter.addActionListener(new MyEdgeLabelSelecterListener(this.edgeLabelExcludeSelecter, this.edgeLabelValueExcludeSelecter));

        this.edgeLabelValueExcludeSelecter.setBackground(Color.WHITE);
        this.edgeLabelValueExcludeSelecter.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.edgeLabelValueExcludeSelecter.setFocusable(false);
        this.edgeLabelValueExcludeSelecter.addItem("");

        if (MySequentialGraphVars.userDefinedEdgeLabelSet.size() > 0) {
            this.topLeftPanel.add(this.edgeLabelExcludeComboBoxMenuLabel);
            this.topLeftPanel.add(this.edgeLabelExcludeMathSymbolSelecter);
            this.topLeftPanel.add(this.edgeLabelExcludeSelecter);
            this.topLeftPanel.add(this.edgeLabelValueExcludeSelecter);
        }
        this.topLeftPanel.add(this.depthExcludeSelecter);
        this.topLeftPanel.add(this.depthExcludeSymbolSelecter);
        this.topLeftPanel.add(this.excludeBtn);

        this.bottomPanel.setBackground(Color.WHITE);
        this.bottomPanel.setLayout(new BorderLayout(0,0));

        this.clusteringSectorLabel.setBackground(Color.WHITE);
        this.clusteringSectorLabel.setToolTipText("FIND CLUSTERS");
        this.clusteringSectorLabel.setFont(MySequentialGraphVars.tahomaPlainFont11);

        this.clusteringSelector.setBackground(Color.WHITE);
        this.clusteringSelector.setToolTipText("FIND CLUSTERS");
        this.clusteringSelector.setFont(MySequentialGraphVars.tahomaPlainFont11);
        this.clusteringSelector.setFocusable(false);
        this.clusteringSelector.addItem("");
        this.clusteringSelector.addItem("BTW.");
        this.clusteringSelector.addItem("MOD.");
        this.clusteringSelector.addActionListener(this);

        String[] clusteringSelectorTooltips = new String[15];
        clusteringSelectorTooltips[0] = "FIND CLUSTERS";
        clusteringSelectorTooltips[1] = "CLUSTERING WITH BETWEENESS";
        clusteringSelectorTooltips[2] = "CLUSTERING WITH MODULARITY";
        this.clusteringSelector.setRenderer(new MyComboBoxTooltipRenderer(clusteringSelectorTooltips));

        this.bottomLeftControlPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0,0));
        this.bottomLeftControlPanel.setBackground(Color.WHITE);

        this.weightedNodeColor.setFocusable(false);
        this.weightedNodeColor.setToolTipText("WEIGHTED NODE COLORS");
        this.weightedNodeColor.setBackground(Color.WHITE);
        this.weightedNodeColor.setFont(MySequentialGraphVars.tahomaPlainFont11);
        this.weightedNodeColor.addActionListener(this);

        this.bottomLeftControlPanel.add(this.weightedNodeColor);
        this.bottomLeftControlPanel.add(this.nodeValueBarChart);
        this.bottomLeftControlPanel.add(this.nodeLabelBarChart);
        this.bottomLeftControlPanel.add(this.edgeValueBarChart);
        this.bottomLeftControlPanel.add(this.edgeLabelBarChart);

        this.bottomRightControlPanel.setBackground(Color.WHITE);
        this.bottomRightControlPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 0,0));
        this.bottomRightControlPanel.add(this.depthSelecter);
        this.bottomRightControlPanel.add(this.depthNeighborNodeTypeSelector);
        this.bottomRightControlPanel.add(this.selectedNodeNeighborNodeTypeSelector);
        this.bottomRightControlPanel.add(this.nodeValueSelecterLabel);
        this.bottomRightControlPanel.add(this.nodeValueSelecter);
        this.bottomRightControlPanel.add(nodeLabelLabel) ;
        this.bottomRightControlPanel.add(this.nodeLabelSelecter);
        this.bottomRightControlPanel.add(this.edgeValueLabel);
        this.bottomRightControlPanel.add(this.edgeValueSelecter);
        if (MySequentialGraphVars.userDefinedEdgeLabelSet.size() > 0) {
            bottomRightControlPanel.add(this.edgeLabelLabel);
            bottomRightControlPanel.add(this.edgeLabelSelecter);
        }
        this.bottomPanel.add(this.bottomLeftControlPanel, BorderLayout.WEST);
        this.bottomPanel.add(this.bottomRightControlPanel, BorderLayout.CENTER);

        JButton edgeValueDistributionBtn = new JButton("E. V.");
        edgeValueDistributionBtn.setBackground(Color.WHITE);
        edgeValueDistributionBtn.setToolTipText("CURRENT EDGE VALUE DISTRIBUTION");
        edgeValueDistributionBtn.setFont(MySequentialGraphVars.tahomaPlainFont11);
        edgeValueDistributionBtn.setPreferredSize(new Dimension(50, 22));
        edgeValueDistributionBtn.setFocusable(false);
        edgeValueDistributionBtn.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        if (edgeValueSelecter.getSelectedIndex() > 1) {
                            MyGraphLevelTopLevelEdgeValueDistribution graphLevelTopLevelEdgeValueDistribution = new MyGraphLevelTopLevelEdgeValueDistribution();
                            graphLevelTopLevelEdgeValueDistribution.enlarge();
                        } else {
                            MyMessageUtil.showInfoMsg(MySequentialGraphVars.app, "Select an edge value.");
                        }
                    }
                }).start();
            }
        });

        JButton nodeValueDistributionBtn = new JButton("N. V.");
        nodeValueDistributionBtn.setBackground(Color.WHITE);
        nodeValueDistributionBtn.setToolTipText("CURRENT NODE VALUE DISTRIBUTION");
        nodeValueDistributionBtn.setFont(MySequentialGraphVars.tahomaPlainFont11);
        nodeValueDistributionBtn.setPreferredSize(new Dimension(52, 22));
        nodeValueDistributionBtn.setFocusable(false);
        nodeValueDistributionBtn.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        MyGraphLevelTopLevelNodeValueDistribution graphLevelTopLevelNodeValueDistribution = new MyGraphLevelTopLevelNodeValueDistribution();
                        graphLevelTopLevelNodeValueDistribution.enlarge();
                    }
                }).start();
            }
        });

        JPanel topRightPanel = new JPanel();
        topRightPanel.setBackground(Color.WHITE);
        topRightPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));

        JLabel graphGroupNodeNumberPercentLabel = new JLabel();
        graphGroupNodeNumberPercentLabel.setBackground(Color.WHITE);
        graphGroupNodeNumberPercentLabel.setFont(MySequentialGraphVars.tahomaPlainFont11);
        graphGroupNodeNumberPercentLabel.setText("");
        topRightPanel.add(graphGroupNodeNumberPercentLabel);
        topRightPanel.add(this.clusteringSectorLabel);
        topRightPanel.add(this.clusteringSelector);
        topRightPanel.add(nodeValueDistributionBtn);
        topRightPanel.add(edgeValueDistributionBtn);
        topRightPanel.add(distributionSelecter);

        topPanel.add(topRightPanel, BorderLayout.EAST);
        topPanel.add(this.topLeftPanel, BorderLayout.WEST);

        this.tableTabbedPane.setFocusable(false);
        this.tableTabbedPane.setOpaque(false);
        this.tableTabbedPane.setBackground(new Color(0,0,0,0));
        this.tableTabbedPane.setPreferredSize(new Dimension(200, 1000));
        this.tableTabbedPane.setFont(MySequentialGraphVars.tahomaPlainFont12);

        this.tableTabbedPane.addTab("N.", null, setNodeTable(), "NODES");
        this.tableTabbedPane.addTab("E.", null, setEdgeTable(), "EDGES");
        this.tableTabbedPane.addTab("S.", null, setPathFindTable(), "NODES BY SHORTEST DISTANCE");
        this.tableTabbedPane.addChangeListener(new ChangeListener() {
            @Override public void stateChanged(ChangeEvent ae) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        if (tableTabbedPane.getSelectedIndex() == 2) {
                            setShortestDistanceConfiguration();
                        } else if (tableTabbedPane.getSelectedIndex() == 0) {
                            MyViewerComponentControllerUtil.setDefaultViewerLook();
                        }
                    }
                }).start();
            }
        });

        this.tableTabbedPane.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                new Thread(new Runnable() {
                    @Override public void run() {
                        setShortestDistanceConfiguration();
                    }
                }).start();
            }
        });

        JPanel graphPanel = new JPanel();
        graphPanel.setBackground(Color.WHITE);
        graphPanel.setLayout(new BorderLayout(0,0));
        graphPanel.add(graphViewer, BorderLayout.CENTER);
        graphPanel.add(bottomPanel, BorderLayout.SOUTH);

        this.graphTableSplitPane.setDividerSize(4);
        this.graphTableSplitPane.setLeftComponent(this.tableTabbedPane);
        this.graphTableSplitPane.setRightComponent(graphPanel);
        this.graphTableSplitPane.getLeftComponent().setBackground(Color.WHITE);
        this.graphTableSplitPane.setBackground(Color.WHITE);
        this.graphTableSplitPane.getRightComponent().setBackground(Color.WHITE);
        this.graphTableSplitPane.setDividerLocation(0.12);
        this.graphTableSplitPane.setMinimumSize(new Dimension(200, 500));
        this.graphTableSplitPane.addComponentListener(new ComponentAdapter() {
            @Override public synchronized void componentResized(ComponentEvent e) {
                super.componentResized(e);
                graphTableSplitPane.setDividerLocation(0.12);
                if (nodeValueBarChart.isSelected()) {
                    MyViewerComponentControllerUtil.removeNodeBarChartsFromViewer();
                    MyViewerComponentControllerUtil.removeSharedNodeValueBarCharts();
                    if (MySequentialGraphVars.getSequentialGraphViewer().selectedNode != null) {
                        MyViewerComponentControllerUtil.setSelectedNodeNeighborValueBarChartToViewer();
                    } else if (MySequentialGraphVars.getSequentialGraphViewer().multiNodes != null) {
                        MyViewerComponentControllerUtil.setSharedNodeLevelNodeValueBarChart();
                    } else {
                        MySequentialGraphVars.getSequentialGraphViewer().graphLevelNodeValueBarChart = new MyGraphLevelNodeValueBarChart();
                        MySequentialGraphVars.getSequentialGraphViewer().add(MySequentialGraphVars.getSequentialGraphViewer().graphLevelNodeValueBarChart);
                    }
                } else {
                    MyViewerComponentControllerUtil.removeNodeBarChartsFromViewer();
                    MyViewerComponentControllerUtil.removeSharedNodeValueBarCharts();
                }
                MySequentialGraphVars.getSequentialGraphViewer().revalidate();
                MySequentialGraphVars.getSequentialGraphViewer().repaint();
            }
        });

        this.add(topPanel, BorderLayout.NORTH);
        this.add(this.graphTableSplitPane, BorderLayout.CENTER);
    }

    public void setShortestDistanceConfiguration() {
        if (tableTabbedPane.getSelectedIndex() == 2) {
            MyProgressBar pb = new MyProgressBar(false);

            if (graphRemovalPanel != null) {
                graphRemovalPanel.setVisible(false);
            }

            shortestDistanceSourceNode = null;
            isTableUpdating = false;
            visitedNodes = null;

            int row = shortestDistanceDestTable.getRowCount();
            while (row > 0) {
                ((DefaultTableModel) shortestDistanceDestTable.getModel()).removeRow(row - 1);
                row = shortestDistanceDestTable.getRowCount();
            }

            Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
            float max = 0f;
            for (MyNode n : nodes) {
                n.setCurrentValue(n.avgShortestDistance);
                if (n.getCurrentValue() > max) {
                    max = n.getCurrentValue();
                }
            }
            MySequentialGraphVars.g.MX_N_VAL = max;

            int i = 0;
            for (MyNode n : nodes) {
                ((DefaultTableModel) shortestDistanceDestTable.getModel()).addRow(
                        new String[]{"" + (++i),
                                MySequentialGraphSysUtil.getNodeName(n.getName()),
                                "0"
                        }
                );
            }

            clusteringSectorLabel.setVisible(false);
            clusteringSelector.setVisible(false);
            depthSelecter.setVisible(false);
            depthExcludeSelecter.setVisible(false);
            depthExcludeSymbolSelecter.setVisible(false);
            edgeValueExludeLabel.setVisible(false);
            edgeValueLabel.setVisible(false);
            edgeLabelLabel.setVisible(false);
            edgeLabelExcludeComboBoxMenuLabel.setVisible(false);
            edgeValueExcludeSymbolSelecter.setVisible(false);
            edgeLabelSelecter.setVisible(false);
            edgeValueSelecter.setVisible(false);
            edgeValueExcludeTxt.setVisible(false);
            edgeLabelExcludeSelecter.setVisible(false);
            edgeValueBarChart.setVisible(false);
            edgeLabelValueExcludeSelecter.setVisible(false);

            /**
             * Set edge values to zero.
             */
            Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
            for (MyEdge e : edges) {
                e.setCurrentValue(0);
            }

            /**
             * Turn dashborad to shortest distance dashboard.
             */
            MySequentialGraphVars.app.getSequentialGraphDashboard().setShortestDistanceDashBoard();

            MySequentialGraphVars.getSequentialGraphViewer().revalidate();
            MySequentialGraphVars.getSequentialGraphViewer().repaint();
            pb.updateValue(100, 100);
            pb.dispose();
        }
    }

    public JPanel setStatTable() {
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout(0,0));
        tablePanel.setBackground(Color.WHITE);

        String [] statTableColumns = {"PROPERTY.", "VALUE"};
        String [][] statTableData = {};
        DefaultTableModel statTableModel = new DefaultTableModel(statTableData, statTableColumns);

        String [] tablePropertyTooltips = {
            "NODES",
            "ISOLATED NODES",
            "EDGES",
            "DIAMETER",
            "AVERAGE UNREACHABLE NODES",
            "MAX. UNREACHABLE NODES",
            "MIN. UNREACHABLE NODES",
            "STD. UNREACHABLE NODES",
            "RED NODES",
            "BLUE NODES",
            "GREEN NODES",
            "AVG. NODE VALUE",
            "MAX. NODE VALUE",
            "MIN. NODE VALUE",
            "NODE VALUE STANDARD DEVIATION",
            "MAX. IN-NODES",
            "MIN. IN-NODES",
            "AVG. IN-NODES",
            "STD. IN-NODES",
            "MAX. OUT-NODES",
            "MIN. OUT-NODES",
            "AVG. OUT-NODES",
            "STD. OUT-NODES",
            "MAX. IN-CONTRIBUTION",
            "MIN. IN-CONTRIBUTION",
            "AVG. IN-CONTRIBUTION",
            "STD. IN-CONTRIBUTION",
            "MAX. OUT-CONTRIBUTION",
            "MIN. OUT-CONTRIBUTION",
            "AVG. OUT-CONTRIBUTION",
            "STD. OUT-CONTRIBUTION",
            "AVG. UNIQUE NODE CONTRIBUTION",
            "MAX. UNIQUE NODE CONTRIBUTION",
            "MIN. UNIQUE NODE CONTRIBUTION",
            "UNIQUE NODE CONT. STANDARD DEVIATION",
            "AVG. UNIQUE EDGE CONTRIBUTION",
            "MAX. UNIQUE EDGE CONTRIBUTION",
            "MIN. UNIQUE EDGE CONTRIBUTION",
            "STD. UNIQUE EDGE CONTRIBUTION",
            "AVG. EDGE VALUE",
            "MAX. EDGE VALUE",
            "MIN. EDGE VALUE",
            "EDGE VALUE STANDARD DEVIATION",
            "AVG. SHORTEST DISTANCE",
            "NO. OF GRAPHS"
        };

        this.statTable = new JTable(statTableModel) {
            public String getToolTipText(MouseEvent e) {
                String tip = null;
                Point p = e.getPoint();
                int rowIndex = rowAtPoint(p);
                int colIndex = columnAtPoint(p);
                try {
                    tip = tablePropertyTooltips[rowIndex];//getValueAt(rowIndex, colIndex).toString();
                } catch (RuntimeException e1) {}
                return tip;
            }
        };

        String [] toolTips = {"PROPERTY.", "VALUE"};
        MyTableToolTipper tooltipHeader = new MyTableToolTipper(this.statTable.getColumnModel());
        tooltipHeader.setToolTipStrings(toolTips);
        this.statTable.setTableHeader(tooltipHeader);

        this.statTable.setRowHeight(23);
        this.statTable.setBackground(Color.WHITE);
        this.statTable.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.statTable.setFocusable(false);
        this.statTable.getTableHeader().setFont(MySequentialGraphVars.tahomaBoldFont12);
        this.statTable.getTableHeader().setOpaque(false);
        this.statTable.getTableHeader().setBackground(new Color(0,0,0,0f));
        this.statTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        this.statTable.setPreferredSize(new Dimension(145, 1500));
        this.statTable.setForeground(Color.BLACK);
        this.statTable.getColumnModel().getColumn(0).setPreferredWidth(65);
        this.statTable.getColumnModel().getColumn(1).setPreferredWidth(50);
        this.statTable.setSelectionForeground(Color.BLACK);
        this.statTable.setSelectionBackground(Color.LIGHT_GRAY);

        // Set the custom renderer for the first column
        TableColumnModel columnModel = this.statTable.getColumnModel();
        columnModel.getColumn(0).setCellRenderer(new MyGrayCellRenderer());

        MyViewerComponentControllerUtil.setGraphLevelTableStatistics();

        JTextField propertySearchTxt = new JTextField();
        propertySearchTxt.setBorder(BorderFactory.createLoweredSoftBevelBorder());

        JButton propertySearchBtn = new JButton();
        JPanel propertySearchPanel = MyTableUtil.searchTablePanel(this, propertySearchTxt, propertySearchBtn, statTableModel, this.statTable);
        propertySearchPanel.remove(propertySearchBtn);
        propertySearchTxt.setFont(MySequentialGraphVars.tahomaBoldFont12);

        JScrollPane graphStatTableScrollPane = new JScrollPane(this.statTable);
        graphStatTableScrollPane.setOpaque(false);
        graphStatTableScrollPane.setPreferredSize(new Dimension(150, 805));
        graphStatTableScrollPane.setBackground(new Color(0,0,0,0f));
        graphStatTableScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(6, 0));
        graphStatTableScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 6));

        tablePanel.add(graphStatTableScrollPane, BorderLayout.CENTER);
        tablePanel.add(propertySearchPanel, BorderLayout.SOUTH);
        return tablePanel;
    }

    public JPanel setSelectedNodeStatTable() {
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout(0,0));
        tablePanel.setBackground(Color.WHITE);

        String [] statTableColumns = {"PROPERTY.", "VALUE"};
        String [][] statTableData = {};
        DefaultTableModel selectedNodeStatTableModel = new DefaultTableModel(statTableData, statTableColumns);

        String [] tablePropertyTooltips = {
                "NODES",
                "PREDECESSORS",
                "SUCCESSORS",
                "EDGES",
                "RED NODES",
                "BLUE NODES",
                "GREEN NODES",
                "AVG. EDGE VALUE",
                "MAX. EDGE VALUE",
                "MIN. EDGE VALUE",
                "EDGE VALUE STANDARD DEVIATION"
        };

        this.selectedNodeStatTable = new JTable(selectedNodeStatTableModel) {
            public String getToolTipText(MouseEvent e) {
                String tip = null;
                Point p = e.getPoint();
                int rowIndex = rowAtPoint(p);
                int colIndex = columnAtPoint(p);
                try {
                    tip = tablePropertyTooltips[rowIndex];//getValueAt(rowIndex, colIndex).toString();
                } catch (RuntimeException e1) {}
                return tip;
            }
        };

        String [] toolTips = {"PROPERTY.", "VALUE"};
        MyTableToolTipper tooltipHeader = new MyTableToolTipper(this.selectedNodeStatTable.getColumnModel());
        tooltipHeader.setToolTipStrings(toolTips);
        this.selectedNodeStatTable.setTableHeader(tooltipHeader);

        this.selectedNodeStatTable.setRowHeight(22);
        this.selectedNodeStatTable.setBackground(Color.WHITE);
        this.selectedNodeStatTable.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.selectedNodeStatTable.setFocusable(false);
        this.selectedNodeStatTable.getTableHeader().setFont(MySequentialGraphVars.tahomaBoldFont12);
        this.selectedNodeStatTable.getTableHeader().setOpaque(false);
        this.selectedNodeStatTable.getTableHeader().setBackground(new Color(0,0,0,0f));
        this.selectedNodeStatTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        this.selectedNodeStatTable.setPreferredSize(new Dimension(145, 800));
        this.selectedNodeStatTable.setForeground(Color.BLACK);
        this.selectedNodeStatTable.getColumnModel().getColumn(0).setPreferredWidth(65);
        this.selectedNodeStatTable.getColumnModel().getColumn(1).setPreferredWidth(50);
        this.selectedNodeStatTable.setSelectionForeground(Color.BLACK);
        this.selectedNodeStatTable.setSelectionBackground(Color.LIGHT_GRAY);

        int nodes = MySequentialGraphVars.g.getGraphNodeCount();
        String nodeStr = MyMathUtil.getCommaSeperatedNumber(nodes);
        float nodePercent = (float) nodes / MySequentialGraphVars.g.getVertexCount();
        String nodePercentStr = MyMathUtil.twoDecimalFormat((nodePercent*100));

        int predecessors = MySequentialGraphVars.g.getGraphPredecessorCount();
        String predecessorStr = MyMathUtil.getCommaSeperatedNumber(predecessors);
        float predecessorPercent = (float) predecessors / MySequentialGraphVars.g.getVertexCount();
        String predecessorPercentStr = MyMathUtil.twoDecimalFormat((predecessorPercent*100));

        int successors = MySequentialGraphVars.g.getGraphSuccessorCount();
        String successorStr = MyMathUtil.getCommaSeperatedNumber(successors);
        float successorPercent = (float) successors / MySequentialGraphVars.g.getVertexCount();
        String successorPercentStr = MyMathUtil.twoDecimalFormat((successorPercent*100));

        int edges = MySequentialGraphVars.g.getGraphEdgeCount();
        String edgeStr = MyMathUtil.getCommaSeperatedNumber(edges);
        float edgePercent = (float) edges / MySequentialGraphVars.g.getEdgeCount();
        String edgePercentStr = MyMathUtil.twoDecimalFormat((edgePercent*100));

        selectedNodeStatTableModel.addRow(new String[]{"NODES", nodeStr + "[" + nodePercentStr + "]"});
        selectedNodeStatTableModel.addRow(new String[]{"P.", predecessorStr + "[" + predecessorPercentStr + "]"});
        selectedNodeStatTableModel.addRow(new String[]{"S.", successorStr + "[" + successorPercentStr + "]"});
        selectedNodeStatTableModel.addRow(new String[]{"EDGES", edgeStr + "[" + edgePercentStr + "]"});
        selectedNodeStatTableModel.addRow(new String[]{"R. N.", MyMathUtil.getCommaSeperatedNumber(MyNodeUtil.getRedNodeCount()) + "[" + MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getRedNodePercent()*100)) + "]"});
        selectedNodeStatTableModel.addRow(new String[]{"B. N.", MyMathUtil.getCommaSeperatedNumber(MyNodeUtil.getBlueNodeCount()) + "[" + MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getBlueNodePercent()*100)) + "]"});
        selectedNodeStatTableModel.addRow(new String[]{"G. N.", MyMathUtil.getCommaSeperatedNumber(MyNodeUtil.getGreenNodeCount()) + "[" + MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getGreenNodePercent()*100)) + "]"});

        // Edge stats.
        selectedNodeStatTableModel.addRow(new String[]{"AVG. E. V.", MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyEdgeUtil.getAverageEdgeValue()))});
        selectedNodeStatTableModel.addRow(new String[]{"MAX. E. V.", MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyEdgeUtil.getMaximumEdgeValue()))});
        selectedNodeStatTableModel.addRow(new String[]{"MIN. E. V.", MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyEdgeUtil.getMinimumEdgeValue()))});
        selectedNodeStatTableModel.addRow(new String[]{"STD. E. V.", MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyEdgeUtil.getEdgeValueStandardDeviation()))});

        // Set the custom renderer for the first column
        TableColumnModel columnModel = this.selectedNodeStatTable.getColumnModel();
        columnModel.getColumn(0).setCellRenderer(new MyGrayCellRenderer());

        JScrollPane graphStatTableScrollPane = new JScrollPane(this.selectedNodeStatTable);
        graphStatTableScrollPane.setOpaque(false);
        graphStatTableScrollPane.setPreferredSize(new Dimension(150, 1500));
        graphStatTableScrollPane.setBackground(new Color(0,0,0,0f));
        graphStatTableScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(6, 0));
        graphStatTableScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 6));

        tablePanel.add(graphStatTableScrollPane, BorderLayout.CENTER);
        return tablePanel;
    }

    public void updateSelectedNodeStatTable() {
        DefaultTableModel selectedNodeStatTableModel = ((DefaultTableModel) selectedNodeStatTable.getModel());
        int row = selectedNodeStatTable.getRowCount();
        while (row > 0) {
            selectedNodeStatTableModel.removeRow(row-1);
            row = selectedNodeStatTableModel.getRowCount();
        }

        int nodes = MySequentialGraphVars.g.getGraphNodeCount();
        String nodeStr = MyMathUtil.getCommaSeperatedNumber(nodes);
        float nodePercent = (float) nodes / MySequentialGraphVars.g.getVertexCount();
        String nodePercentStr = MyMathUtil.twoDecimalFormat((nodePercent*100));

        int predecessors = MySequentialGraphVars.g.getGraphPredecessorCount();
        String predecessorStr = MyMathUtil.getCommaSeperatedNumber(predecessors);
        float predecessorPercent = (float) predecessors / MySequentialGraphVars.g.getVertexCount();
        String predecessorPercentStr = MyMathUtil.twoDecimalFormat((predecessorPercent*100));

        int successors = MySequentialGraphVars.g.getGraphSuccessorCount();
        String successorStr = MyMathUtil.getCommaSeperatedNumber(successors);
        float successorPercent = (float) successors / MySequentialGraphVars.g.getVertexCount();
        String successorPercentStr = MyMathUtil.twoDecimalFormat((successorPercent*100));

        int edges = MySequentialGraphVars.g.getGraphEdgeCount();
        String edgeStr = MyMathUtil.getCommaSeperatedNumber(edges);
        float edgePercent = (float) edges / MySequentialGraphVars.g.getEdgeCount();
        String edgePercentStr = MyMathUtil.twoDecimalFormat((edgePercent*100));

        selectedNodeStatTableModel.addRow(new String[]{"NODES", nodeStr + "[" + nodePercentStr + "]"});
        selectedNodeStatTableModel.addRow(new String[]{"PREDECESSORS", predecessorStr + "[" + predecessorPercentStr + "]"});
        selectedNodeStatTableModel.addRow(new String[]{"SUCCESSORS", successorStr + "[" + successorPercentStr + "]"});
        selectedNodeStatTableModel.addRow(new String[]{"EDGES", edgeStr + "[" + edgePercentStr + "]"});
        selectedNodeStatTableModel.addRow(new String[]{"R. N.", MyMathUtil.getCommaSeperatedNumber(MyNodeUtil.getRedNodeCount()) + "[" + MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getRedNodePercent()*100)) + "]"});
        selectedNodeStatTableModel.addRow(new String[]{"B. N.", MyMathUtil.getCommaSeperatedNumber(MyNodeUtil.getBlueNodeCount()) + "[" + MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getBlueNodePercent()*100)) + "]"});
        selectedNodeStatTableModel.addRow(new String[]{"G. N.", MyMathUtil.getCommaSeperatedNumber(MyNodeUtil.getGreenNodeCount()) + "[" + MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getGreenNodePercent()*100)) + "]"});

        // Edge stats.
        selectedNodeStatTableModel.addRow(new String[]{"AVG. E. V.", MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyEdgeUtil.getAverageEdgeValue()))});
        selectedNodeStatTableModel.addRow(new String[]{"MAX. E. V.", MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyEdgeUtil.getMaximumEdgeValue()))});
        selectedNodeStatTableModel.addRow(new String[]{"MIN. E. V.", MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyEdgeUtil.getMinimumEdgeValue()))});
        selectedNodeStatTableModel.addRow(new String[]{"STD. E. V.", MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyEdgeUtil.getEdgeValueStandardDeviation()))});
    }

    public JPanel setMultiNodeStatTable() {
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout(0,0));
        tablePanel.setBackground(Color.WHITE);

        String [] statTableColumns = {"PROPERTY.", "VALUE"};
        String [][] statTableData = {};
        DefaultTableModel multiNodeStatTableModel = new DefaultTableModel(statTableData, statTableColumns);

        String [] tablePropertyTooltips = {
                "SELECTED NODES",
                "PREDECESSORS",
                "SUCCESSORS",
                "EDGES",
                "RED NODES",
                "BLUE NODES",
                "GREEN NODES",
                "SHARERD PREDECESSORS",
                "SHARERD SUCCESSOR",
                "AVG. EDGE VALUE",
                "MAX. EDGE VALUE",
                "MIN. EDGE VALUE",
                "EDGE VALUE STANDARD DEVIATION"
        };

        this.multiNodeStatTable = new JTable(multiNodeStatTableModel) {
            public String getToolTipText(MouseEvent e) {
                String tip = null;
                Point p = e.getPoint();
                int rowIndex = rowAtPoint(p);
                int colIndex = columnAtPoint(p);
                try {
                    tip = tablePropertyTooltips[rowIndex];//getValueAt(rowIndex, colIndex).toString();
                } catch (RuntimeException e1) {}
                return tip;
            }
        };

        String [] toolTips = {"PROPERTY.", "VALUE"};
        MyTableToolTipper tooltipHeader = new MyTableToolTipper(this.multiNodeStatTable.getColumnModel());
        tooltipHeader.setToolTipStrings(toolTips);
        this.multiNodeStatTable.setTableHeader(tooltipHeader);

        this.multiNodeStatTable.setRowHeight(22);
        this.multiNodeStatTable.setBackground(Color.WHITE);
        this.multiNodeStatTable.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.multiNodeStatTable.setFocusable(false);
        this.multiNodeStatTable.getTableHeader().setFont(MySequentialGraphVars.tahomaBoldFont12);
        this.multiNodeStatTable.getTableHeader().setOpaque(false);
        this.multiNodeStatTable.getTableHeader().setBackground(new Color(0,0,0,0f));
        this.multiNodeStatTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        this.multiNodeStatTable.setPreferredSize(new Dimension(145, 800));
        this.multiNodeStatTable.setForeground(Color.BLACK);
        this.multiNodeStatTable.getColumnModel().getColumn(0).setPreferredWidth(65);
        this.multiNodeStatTable.getColumnModel().getColumn(1).setPreferredWidth(50);
        this.multiNodeStatTable.setSelectionForeground(Color.BLACK);
        this.multiNodeStatTable.setSelectionBackground(Color.LIGHT_GRAY);

        int predecessors = MySequentialGraphVars.getSequentialGraphViewer().multiNodePredecessors.size();
        String predecessorStr = MyMathUtil.getCommaSeperatedNumber(predecessors);
        float predecessorPercent = (float) predecessors / MySequentialGraphVars.g.getVertexCount();
        String predecessorPercentStr = MyMathUtil.twoDecimalFormat((predecessorPercent*100));

        int successors = MySequentialGraphVars.getSequentialGraphViewer().multiNodeSuccessors.size();
        String successorStr = MyMathUtil.getCommaSeperatedNumber(successors);
        float successorPercent = (float) successors / MySequentialGraphVars.g.getVertexCount();
        String successorPercentStr = MyMathUtil.twoDecimalFormat((successorPercent*100));

        int sharedPredecessors = MySequentialGraphVars.getSequentialGraphViewer().sharedPredecessors.size();
        String sharedPredecessorStr = MyMathUtil.getCommaSeperatedNumber(sharedPredecessors);
        float sharedPredecessorPercent = (float) sharedPredecessors / MySequentialGraphVars.g.getVertexCount();
        String sharedPredecessorPercentStr = MyMathUtil.twoDecimalFormat((sharedPredecessorPercent*100));

        int sharedSuccessors = MySequentialGraphVars.getSequentialGraphViewer().sharedSuccessors.size();
        String sharedSuccessorStr = MyMathUtil.getCommaSeperatedNumber(sharedSuccessors);
        float sharedSuccessorPercent = (float) sharedSuccessors / MySequentialGraphVars.g.getVertexCount();
        String sharedSuccessorPercentStr = MyMathUtil.twoDecimalFormat((sharedSuccessorPercent*100));

        int edges = MySequentialGraphVars.g.getTotalMultiNodeEdges();
        String edgeStr = MyMathUtil.getCommaSeperatedNumber(edges);
        float edgePercent = (float) edges / MySequentialGraphVars.g.getEdgeCount();
        String edgePercentStr = MyMathUtil.twoDecimalFormat((edgePercent*100));

        int selectedNodes = MySequentialGraphVars.getSequentialGraphViewer().multiNodes.size();
        String selectedNodeStr = MyMathUtil.getCommaSeperatedNumber(selectedNodes);
        float selectedNodePercent = (float) selectedNodes / MySequentialGraphVars.g.getVertexCount();
        String selectedNodePercentStr = MyMathUtil.twoDecimalFormat((selectedNodePercent*100));

        multiNodeStatTableModel.addRow(new String[]{"SELECTED N.", selectedNodeStr + "[" + selectedNodePercentStr + "]"});
        multiNodeStatTableModel.addRow(new String[]{"P.", predecessorStr + "[" + predecessorPercentStr + "]"});
        multiNodeStatTableModel.addRow(new String[]{"S.", successorStr + "[" + successorPercentStr + "]"});
        multiNodeStatTableModel.addRow(new String[]{"EDGES", edgeStr + "[" + edgePercentStr + "]"});
        multiNodeStatTableModel.addRow(new String[]{"R. N.", MyMathUtil.getCommaSeperatedNumber(MyNodeUtil.getRedNodeCount()) + "[" + MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getRedNodePercent()*100)) + "]"});
        multiNodeStatTableModel.addRow(new String[]{"B. N.", MyMathUtil.getCommaSeperatedNumber(MyNodeUtil.getBlueNodeCount()) + "[" + MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getBlueNodePercent()*100)) + "]"});
        multiNodeStatTableModel.addRow(new String[]{"G. N.", MyMathUtil.getCommaSeperatedNumber(MyNodeUtil.getGreenNodeCount()) + "[" + MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getGreenNodePercent()*100)) + "]"});
        multiNodeStatTableModel.addRow(new String[]{"SHA. P.", sharedPredecessorStr + "[" + sharedPredecessorPercentStr + "]"});
        multiNodeStatTableModel.addRow(new String[]{"SHA. S.", sharedSuccessorStr + "[" + sharedSuccessorPercentStr + "]"});

        // Edge stats.
        multiNodeStatTableModel.addRow(new String[]{"AVG. E. V.", MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyEdgeUtil.getAverageEdgeValue()))});
        multiNodeStatTableModel.addRow(new String[]{"MAX. E. V.", MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyEdgeUtil.getMaximumEdgeValue()))});
        multiNodeStatTableModel.addRow(new String[]{"MIN. E. V.", MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyEdgeUtil.getMinimumEdgeValue()))});
        multiNodeStatTableModel.addRow(new String[]{"STD. E. V.", MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyEdgeUtil.getEdgeValueStandardDeviation()))});

        // Set the custom renderer for the first column
        TableColumnModel columnModel = this.multiNodeStatTable.getColumnModel();
        columnModel.getColumn(0).setCellRenderer(new MyGrayCellRenderer());

        JScrollPane graphStatTableScrollPane = new JScrollPane(this.multiNodeStatTable);
        graphStatTableScrollPane.setOpaque(false);
        graphStatTableScrollPane.setPreferredSize(new Dimension(150, 1500));
        graphStatTableScrollPane.setBackground(new Color(0,0,0,0f));
        graphStatTableScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(6, 0));
        graphStatTableScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 6));

        tablePanel.add(graphStatTableScrollPane, BorderLayout.CENTER);
        return tablePanel;
    }

    public void updateMultiNodeStatTable() {
        DefaultTableModel multiNodeStatTableModel = ((DefaultTableModel) multiNodeStatTable.getModel());
        int row = multiNodeStatTable.getRowCount();
        while (row > 0) {
            multiNodeStatTableModel.removeRow(row-1);
            row = multiNodeStatTable.getRowCount();
        }

        int predecessors = MySequentialGraphVars.getSequentialGraphViewer().multiNodePredecessors.size();
        String predecessorStr = MyMathUtil.getCommaSeperatedNumber(predecessors);
        float predecessorPercent = (float) predecessors / MySequentialGraphVars.g.getVertexCount();
        String predecessorPercentStr = MyMathUtil.twoDecimalFormat((predecessorPercent*100));

        int successors = MySequentialGraphVars.getSequentialGraphViewer().multiNodeSuccessors.size();
        String successorStr = MyMathUtil.getCommaSeperatedNumber(successors);
        float successorPercent = (float) successors / MySequentialGraphVars.g.getVertexCount();
        String successorPercentStr = MyMathUtil.twoDecimalFormat((successorPercent*100));

        int sharedPredecessors = MySequentialGraphVars.getSequentialGraphViewer().sharedPredecessors.size();
        String sharedPredecessorStr = MyMathUtil.getCommaSeperatedNumber(sharedPredecessors);
        float sharedPredecessorPercent = (float) sharedPredecessors / MySequentialGraphVars.g.getVertexCount();
        String sharedPredecessorPercentStr = MyMathUtil.twoDecimalFormat((sharedPredecessorPercent*100));

        int sharedSuccessors = MySequentialGraphVars.getSequentialGraphViewer().sharedSuccessors.size();
        String sharedSuccessorStr = MyMathUtil.getCommaSeperatedNumber(sharedSuccessors);
        float sharedSuccessorPercent = (float) sharedSuccessors / MySequentialGraphVars.g.getVertexCount();
        String sharedSuccessorPercentStr = MyMathUtil.twoDecimalFormat((sharedSuccessorPercent*100));

        int edges = MySequentialGraphVars.g.getTotalMultiNodeEdges();
        String edgeStr = MyMathUtil.getCommaSeperatedNumber(edges);
        float edgePercent = (float) edges / MySequentialGraphVars.g.getEdgeCount();
        String edgePercentStr = MyMathUtil.twoDecimalFormat((edgePercent*100));

        int selectedNodes = MySequentialGraphVars.getSequentialGraphViewer().multiNodes.size();
        String selectedNodeStr = MyMathUtil.getCommaSeperatedNumber(selectedNodes);
        float selectedNodePercent = (float) selectedNodes / MySequentialGraphVars.g.getVertexCount();
        String selectedNodePercentStr = MyMathUtil.twoDecimalFormat((selectedNodePercent*100));

        multiNodeStatTableModel.addRow(new String[]{"SELECTED N.", selectedNodeStr + "[" + selectedNodePercentStr + "]"});
        multiNodeStatTableModel.addRow(new String[]{"P.", predecessorStr + "[" + predecessorPercentStr + "]"});
        multiNodeStatTableModel.addRow(new String[]{"S.", successorStr + "[" + successorPercentStr + "]"});
        multiNodeStatTableModel.addRow(new String[]{"EDGES", edgeStr + "[" + edgePercentStr + "]"});
        multiNodeStatTableModel.addRow(new String[]{"R. N.", MyMathUtil.getCommaSeperatedNumber(MyNodeUtil.getRedNodeCount()) + "[" + MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getRedNodePercent()*100)) + "]"});
        multiNodeStatTableModel.addRow(new String[]{"B. N.", MyMathUtil.getCommaSeperatedNumber(MyNodeUtil.getBlueNodeCount()) + "[" + MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getBlueNodePercent()*100)) + "]"});
        multiNodeStatTableModel.addRow(new String[]{"G. N.", MyMathUtil.getCommaSeperatedNumber(MyNodeUtil.getGreenNodeCount()) + "[" + MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getGreenNodePercent()*100)) + "]"});
        multiNodeStatTableModel.addRow(new String[]{"SHA. P.", MyMathUtil.getCommaSeperatedNumber(sharedPredecessors) + "[" + sharedPredecessorPercentStr + "]"});
        multiNodeStatTableModel.addRow(new String[]{"SHA. S.", MyMathUtil.getCommaSeperatedNumber(sharedSuccessors) + "[" + sharedSuccessorPercentStr + "]"});

        // Edge stats.
        multiNodeStatTableModel.addRow(new String[]{"AVG. E. V.", MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyEdgeUtil.getAverageEdgeValue()))});
        multiNodeStatTableModel.addRow(new String[]{"MAX. E. V.", MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyEdgeUtil.getMaximumEdgeValue()))});
        multiNodeStatTableModel.addRow(new String[]{"MIN. E. V.", MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyEdgeUtil.getMinimumEdgeValue()))});
        multiNodeStatTableModel.addRow(new String[]{"STD. E. V.", MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyEdgeUtil.getEdgeValueStandardDeviation()))});

    }

    public JPanel setEdgeTable() {
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout(0,0));
        tablePanel.setBackground(Color.WHITE);

        String [] bottomTableColumns = {"SOURCE", "DEST", "V."};
        String [][] bottomTableData = {};
        DefaultTableModel bottomTableModel = new DefaultTableModel(bottomTableData, bottomTableColumns);
        this.edgeListTable = new JTable(bottomTableModel) {
            public String getToolTipText(MouseEvent e) {
                String tip = null;
                Point p = e.getPoint();
                int rowIndex = rowAtPoint(p);
                int colIndex = columnAtPoint(p);
                try {
                    tip = getValueAt(rowIndex, colIndex).toString();
                } catch (RuntimeException e1) {}
                return tip;
            }
        };

        String [] toolTips = {"SOURCE", "DEST", "EDGE VALUE"};
        MyTableToolTipper tooltipHeader = new MyTableToolTipper(this.edgeListTable.getColumnModel());
        tooltipHeader.setToolTipStrings(toolTips);
        this.edgeListTable.setTableHeader(tooltipHeader);

        Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
        LinkedHashMap<String, Float> sortedEdges = new LinkedHashMap<>();
        for (MyEdge e : edges) {
            String ename = e.getSource().getName() + "-" + e.getDest().getName();
            sortedEdges.put(ename, (float)e.getContribution());
        }
        sortedEdges = MySequentialGraphSysUtil.sortMapByFloatValue(sortedEdges);
        for (String e : sortedEdges.keySet()) {
            String source = MySequentialGraphSysUtil.getDecodedNodeName(e.split("-")[0]);
            String dest = MySequentialGraphSysUtil.getDecodedNodeName(e.split("-")[1]);
            bottomTableModel.addRow(
                new String[]{
                    source,
                    dest,
                    MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(sortedEdges.get(e)))
                });
        }

        edgeListTable.setRowHeight(22);
        edgeListTable.setBackground(Color.WHITE);
        edgeListTable.setFont(MySequentialGraphVars.f_pln_10);
        edgeListTable.getTableHeader().setFont(MySequentialGraphVars.tahomaBoldFont10);
        edgeListTable.getTableHeader().setOpaque(false);
        edgeListTable.getTableHeader().setBackground(new Color(0,0,0,0f));
        edgeListTable.getColumnModel().getColumn(0).setPreferredWidth(60);
        edgeListTable.getColumnModel().getColumn(1).setPreferredWidth(60);
        edgeListTable.getColumnModel().getColumn(2).setPreferredWidth(30);

        JTextField edgeTableNodeTableNodeSearchTxt = new JTextField();
        JButton edgeTableNodeSelectBtn = new JButton("SEL.");
        edgeTableNodeSelectBtn.setFont(MySequentialGraphVars.tahomaPlainFont10);
        edgeTableNodeSelectBtn.setFocusable(false);
        edgeTableNodeTableNodeSearchTxt.setBackground(Color.WHITE);
        edgeTableNodeTableNodeSearchTxt.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        JPanel bottomTableSearchAndSavePanel = MyTableUtil.searchAndSaveDataPanelForJTable2(this, edgeTableNodeTableNodeSearchTxt, edgeTableNodeSelectBtn, bottomTableModel, edgeListTable);
        edgeTableNodeTableNodeSearchTxt.setFont(MySequentialGraphVars.f_bold_10);
        edgeTableNodeTableNodeSearchTxt.setToolTipText("TYPE A NODE NAME TO SEARCH");
        edgeTableNodeTableNodeSearchTxt.setPreferredSize(new Dimension(90, 19));
        edgeTableNodeSelectBtn.setPreferredSize(new Dimension(50, 19));
        bottomTableSearchAndSavePanel.remove(edgeTableNodeSelectBtn);

        edgeListTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        edgeListTable.setSelectionBackground(Color.LIGHT_GRAY);
        edgeListTable.setForeground(Color.BLACK);
        edgeListTable.setSelectionForeground(Color.BLACK);
        edgeListTable.setFocusable(false);
        edgeListTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        edgeListTable.setRowSorter(MyTableUtil.setJTableRowSorterWithTextField(bottomTableModel, edgeTableNodeTableNodeSearchTxt));
        edgeListTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        try {
                            MyNode s =  (MyNode) MySequentialGraphVars.g.vRefs.get(MySequentialGraphVars.nodeNameMap.get(edgeListTable.getValueAt(edgeListTable.getSelectedRow(), 0)));
                            MyNode d = (MyNode) MySequentialGraphVars.g.vRefs.get(MySequentialGraphVars.nodeNameMap.get(edgeListTable.getValueAt(edgeListTable.getSelectedRow(), 1)));
                            MyNodeSearcher nodeSearcher = new MyNodeSearcher();
                            nodeSearcher.setEdgeNodes(s, d);
                        } catch (Exception ex) {}
                    }
                }).start();
            }
        });

        JScrollPane edgeTableScrollPane = new JScrollPane(edgeListTable);
        edgeTableScrollPane.setOpaque(false);
        edgeTableScrollPane.setBackground(new Color(0,0,0,0f));
        edgeTableScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(6, 0));
        edgeTableScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 6));

        tablePanel.add(edgeTableScrollPane, BorderLayout.CENTER);
        tablePanel.add(bottomTableSearchAndSavePanel, BorderLayout.SOUTH);
        return tablePanel;
    }

    private JPanel setNodeTable() {
        final MyViewerComponentController viewerController = this;
        JPanel tablePanel = new JPanel();
        tablePanel.setPreferredSize(new Dimension(140, 500));
        tablePanel.setLayout(new GridLayout(2,1));
        tablePanel.setBackground(Color.WHITE);

        JPanel bottomTablePanel = new JPanel();
        bottomTablePanel.setLayout(new BorderLayout(0,0));
        bottomTablePanel.setBackground(Color.WHITE);

        String [] bottomTableColumns = {"NO.", "NODE", "V."};
        String [][] bottomTableData = {};

        DefaultTableModel nodeListTableModel = new DefaultTableModel(bottomTableData, bottomTableColumns);
        this.nodeListTable = new JTable(nodeListTableModel) {
            public String getToolTipText(MouseEvent e) {
                String tip = null;
                Point p = e.getPoint();
                int rowIndex = rowAtPoint(p);
                int colIndex = columnAtPoint(p);

                try {
                    tip = getValueAt(rowIndex, colIndex).toString();
                } catch (RuntimeException e1) {}
                return tip;
            }
        };

        String [] toolTips = {"NO.", "NODE", "NODE VALUE"};
        MyTableToolTipper tooltipHeader = new MyTableToolTipper(this.nodeListTable.getColumnModel());
        tooltipHeader.setToolTipStrings(toolTips);
        this.nodeListTable.setTableHeader(tooltipHeader);

        Collection<MyNode> bottomTableNodes = MySequentialGraphVars.g.getVertices();
        LinkedHashMap<String, Float> valueMap = new LinkedHashMap<>();
        for (MyNode n : bottomTableNodes) {
            valueMap.put(n.getName(), n.getCurrentValue());
        }
        valueMap = MySequentialGraphSysUtil.sortMapByFloatValue(valueMap);

        int i=-0;
        for (String n : valueMap.keySet()) {
            nodeListTableModel.addRow(
                new String[]{
                    String.valueOf(++i),
                    MySequentialGraphSysUtil.getDecodedNodeName(n),
                    MyMathUtil.getCommaSeperatedNumber((long)(float) valueMap.get(n))
            });
        }

        nodeListTable.setRowHeight(22);
        nodeListTable.setBackground(Color.WHITE);
        nodeListTable.setFont(MySequentialGraphVars.f_pln_10);
        nodeListTable.getTableHeader().setFont(MySequentialGraphVars.tahomaBoldFont10);
        nodeListTable.getTableHeader().setOpaque(false);
        nodeListTable.getTableHeader().setBackground(new Color(0,0,0,0f));
        nodeListTable.getColumnModel().getColumn(0).setPreferredWidth(35);
        nodeListTable.getColumnModel().getColumn(1).setPreferredWidth(80);
        nodeListTable.getColumnModel().getColumn(2).setPreferredWidth(40);

        JTextField bottomTableNodeTableNodeSearchTxt = new JTextField();
        JButton bottomTableNodeSelectBtn = new JButton("SEL.");
        bottomTableNodeSelectBtn.setFont(MySequentialGraphVars.tahomaPlainFont10);
        bottomTableNodeSelectBtn.setFocusable(false);
        bottomTableNodeTableNodeSearchTxt.setBackground(Color.WHITE);
        bottomTableNodeTableNodeSearchTxt.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        JPanel bottomTableSearchAndSavePanel = MyTableUtil.searchAndSaveDataPanelForJTable2(this, bottomTableNodeTableNodeSearchTxt, bottomTableNodeSelectBtn, nodeListTableModel, nodeListTable);
        bottomTableNodeTableNodeSearchTxt.setFont(MySequentialGraphVars.f_bold_10);
        bottomTableNodeTableNodeSearchTxt.setToolTipText("TYPE A NODE NAME TO SEARCH");
        bottomTableNodeTableNodeSearchTxt.setPreferredSize(new Dimension(100, 19));
        bottomTableNodeSelectBtn.setPreferredSize(new Dimension(50, 19));
        bottomTableNodeSelectBtn.removeActionListener(this);
        bottomTableNodeSelectBtn.removeActionListener(this);
        bottomTableSearchAndSavePanel.remove(bottomTableNodeSelectBtn);

        nodeListTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        nodeListTable.setSelectionBackground(Color.LIGHT_GRAY);
        nodeListTable.setForeground(Color.BLACK);
        nodeListTable.setSelectionForeground(Color.BLACK);
        nodeListTable.setFocusable(false);
        nodeListTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        nodeListTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                new Thread(new Runnable() {
                    @Override public synchronized void run() {
                        try {
                            if (nodeValueBarChart.isSelected()) {
                                nodeValueBarChart.removeActionListener(viewerController);
                                nodeValueBarChart.setSelected(false);
                                nodeValueBarChart.addActionListener(viewerController);
                            }

                            if (edgeValueBarChart.isSelected()) {
                                edgeValueBarChart.removeActionListener(viewerController);
                                edgeValueBarChart.setSelected(false);
                                edgeValueBarChart.addActionListener(viewerController);
                            }

                            if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() > 0) {
                                nodeValueSelecter.removeActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
                                nodeValueSelecter.setSelectedIndex(0);
                                MyNodeUtil.setDefaultValuesToNodes();
                                MyEdgeUtil.setDefaultValuesToEdges();
                                nodeValueSelecter.addActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
                            }

                            if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthSelecter.getSelectedIndex() > 0) return;
                            if (nodeListTable.getSelectedRow() == -1 || nodeListTable.getRowCount() == 0) return;
                            String nodeName = MySequentialGraphVars.nodeNameMap.get(nodeListTableModel.getValueAt(nodeListTable.getSelectedRow(), 1)).toString();
                            MyNode n = (MyNode) MySequentialGraphVars.g.vRefs.get(nodeName);
                            MyNodeSearcher nodeSearcher = new MyNodeSearcher();
                            nodeSearcher.setSelectedNodes(n);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        JScrollPane bottomTableScrollPane = new JScrollPane(nodeListTable);
        bottomTableScrollPane.setOpaque(false);
        bottomTableScrollPane.setBackground(new Color(0,0,0,0f));
        bottomTableScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(7, 0));
        bottomTableScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 7));

        JPanel topTablePanel = new JPanel();
        topTablePanel.setLayout(new BorderLayout(3,3));
        topTablePanel.setBackground(Color.WHITE);

        String [] topTableColumns = {"NO.", "CURRENT NODE", "V."};
        String [][] topTableData = {};
        DefaultTableModel topTableModel = new DefaultTableModel(topTableData, topTableColumns);
        currentNodeListTable = new JTable(topTableModel) {
            //Implement table cell tool tips.
            public String getToolTipText(MouseEvent e) {
                String tip = null;
                Point p = e.getPoint();
                int rowIndex = rowAtPoint(p);
                int colIndex = columnAtPoint(p);
                try {
                    tip = getValueAt(rowIndex, colIndex).toString();
                } catch (RuntimeException e1) {}
                return tip;
            }
        };

        LinkedHashMap<String, Long> nodeMap = new LinkedHashMap<>();
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            nodeMap.put(n.getName(), (long) n.getCurrentValue());
        }
        nodeMap = MySequentialGraphSysUtil.sortMapByLongValue(nodeMap);
        i = 0;
        for (String n : nodeMap.keySet()) {
            MyNode nn = (MyNode) MySequentialGraphVars.g.vRefs.get(n);
            if (nn.getCurrentValue() > 0) {
                ((DefaultTableModel) currentNodeListTable.getModel()).addRow(new String[]{
                    "" + (++i),
                    MySequentialGraphSysUtil.getDecodedNodeName(nn.getName()),
                    MyMathUtil.getCommaSeperatedNumber((long) nn.getCurrentValue())}
                );
            }
        }

        String [] currentNodeTableHeaderTooltips = {"NO.", "CURRENT NODE", "CURRENT NODE VALUE"};
        MyTableToolTipper currentNodeTooltipHeader = new MyTableToolTipper(this.currentNodeListTable.getColumnModel());
        currentNodeTooltipHeader.setToolTipStrings(currentNodeTableHeaderTooltips);
        this.currentNodeListTable.setTableHeader(currentNodeTooltipHeader);

        currentNodeListTable.setRowHeight(22);
        currentNodeListTable.setBackground(Color.WHITE);
        currentNodeListTable.setFont(MySequentialGraphVars.f_pln_10);
        currentNodeListTable.getTableHeader().setFont(MySequentialGraphVars.tahomaBoldFont10);
        currentNodeListTable.getTableHeader().setOpaque(false);
        currentNodeListTable.getTableHeader().setBackground(new Color(0,0,0,0f));
        currentNodeListTable.getColumnModel().getColumn(0).setPreferredWidth(35);
        currentNodeListTable.getColumnModel().getColumn(1).setPreferredWidth(80);
        currentNodeListTable.getColumnModel().getColumn(2).setPreferredWidth(40);

        JTextField topTableNodeSearchTxt = new JTextField();
        JButton topTableNodeSelectBtn = new JButton("SEL.");
        topTableNodeSelectBtn.setFont(MySequentialGraphVars.tahomaPlainFont10);
        topTableNodeSelectBtn.setFocusable(false);
        topTableNodeSearchTxt.setBackground(Color.WHITE);
        topTableNodeSearchTxt.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        JPanel topTableSearchAndSavePanel = MyTableUtil.searchAndSaveDataPanelForJTable2(this, topTableNodeSearchTxt, topTableNodeSelectBtn, topTableModel, currentNodeListTable);
        topTableNodeSearchTxt.setFont(MySequentialGraphVars.f_bold_10);
        topTableNodeSearchTxt.setToolTipText("TYPE A NODE NAME TO SEARCH");
        topTableNodeSearchTxt.setPreferredSize(new Dimension(90, 19));
        topTableNodeSelectBtn.setPreferredSize(new Dimension(50, 19));
        topTableNodeSelectBtn.removeActionListener(this);
        topTableNodeSelectBtn.removeActionListener(this);
        topTableSearchAndSavePanel.remove(topTableNodeSelectBtn);

        currentNodeListTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        currentNodeListTable.setSelectionBackground(Color.LIGHT_GRAY);
        currentNodeListTable.setForeground(Color.BLACK);
        currentNodeListTable.setSelectionForeground(Color.BLACK);
        currentNodeListTable.setFocusable(false);
        currentNodeListTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        currentNodeListTable.setRowSorter(MyTableUtil.setJTableRowSorterWithTextField(topTableModel, topTableNodeSearchTxt));

        JScrollPane topTableScrollPane = new JScrollPane(currentNodeListTable);
        topTableScrollPane.setOpaque(false);
        topTableScrollPane.setBackground(new Color(0,0,0,0f));
        topTableScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(7, 0));
        topTableScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 7));

        bottomTablePanel.add(bottomTableScrollPane, BorderLayout.CENTER);
        bottomTablePanel.add(bottomTableSearchAndSavePanel, BorderLayout.SOUTH);

        topTablePanel.add(topTableScrollPane, BorderLayout.CENTER);
        topTablePanel.add(topTableSearchAndSavePanel, BorderLayout.SOUTH);

        tablePanel.add(topTablePanel);
        tablePanel.add(bottomTablePanel);

        JPanel tableAndGraphRemovePanel = new JPanel();
        tableAndGraphRemovePanel.setBackground(Color.WHITE);
        tableAndGraphRemovePanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        tableAndGraphRemovePanel.setLayout(new BorderLayout(3,3));
        tableAndGraphRemovePanel.add(tablePanel, BorderLayout.CENTER);
        if (MySequentialGraphVars.numberOfGraphs > 1) {
            tableAndGraphRemovePanel.add(graphRemovalPanel, BorderLayout.SOUTH);
        }

        return tableAndGraphRemovePanel;
    }

    public MyGraphRemovalPanel graphRemovalPanel = new MyGraphRemovalPanel();

    public void updateTableInfos() {
        this.nodeListTable.setAutoCreateRowSorter(false);
        this.currentNodeListTable.setAutoCreateRowSorter(false);
        this.edgeListTable.setAutoCreateRowSorter(false);
        this.shortestDistanceSourceTable.setAutoCreateRowSorter(false);
        this.shortestDistanceDestTable.setAutoCreateRowSorter(false);

        this.updateNodeTable();
        this.nodeListTable.revalidate();
        this.nodeListTable.repaint();
        this.updateEdgeTable();
        this.nodeListTable.revalidate();
        this.nodeListTable.repaint();
        //this.updateStatTable();


        // Get the existing currentNodeListTable RowSorter
        TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) currentNodeListTable.getRowSorter();
        // Get the existing nodeListTable model
        DefaultTableModel tableModel = (DefaultTableModel) sorter.getModel();
        // Create a new sorter with the existing nodeListTable model
        TableRowSorter<DefaultTableModel> newSorter = new TableRowSorter<DefaultTableModel>(tableModel);
        // Set the new sorter as the RowSorter of nodeListTable
        currentNodeListTable.setRowSorter(newSorter);


        // Get the existing nodeListTable RowSorter
        sorter = (TableRowSorter<DefaultTableModel>) nodeListTable.getRowSorter();
        // Get the existing nodeListTable model
        tableModel = (DefaultTableModel) sorter.getModel();
        // Create a new sorter with the existing nodeListTable model
        newSorter = new TableRowSorter<DefaultTableModel>(tableModel);
        // Set the new sorter as the RowSorter of nodeListTable
        nodeListTable.setRowSorter(newSorter);


        // Get the existing edgeListTable RowSorter
        sorter = (TableRowSorter<DefaultTableModel>) edgeListTable.getRowSorter();
        // Get the existing edgeListTable model
        tableModel = (DefaultTableModel) sorter.getModel();
        // Create a new sorter with the existing edgeListTable model
        newSorter = new TableRowSorter<DefaultTableModel>(tableModel);
        // Set the new sorter as the RowSorter of edgeListTable
        edgeListTable.setRowSorter(newSorter);


        // Get the existing pathFromTable RowSorter
        sorter = (TableRowSorter<DefaultTableModel>) shortestDistanceSourceTable.getRowSorter();
        // Get the existing pathFromTable model
        tableModel = (DefaultTableModel) sorter.getModel();
        // Create a new sorter with the existing pathFromTable model
        newSorter = new TableRowSorter<DefaultTableModel>(tableModel);
        // Set the new sorter as the RowSorter of pathFromTable
        shortestDistanceSourceTable.setRowSorter(newSorter);


        // Get the existing pathToTable RowSorter
        sorter = (TableRowSorter<DefaultTableModel>) shortestDistanceDestTable.getRowSorter();
        // Get the existing pathToTable model
        tableModel = (DefaultTableModel) sorter.getModel();
        // Create a new sorter with the existing pathToTable model
        newSorter = new TableRowSorter<DefaultTableModel>(tableModel);
        // Set the new sorter as the RowSorter of pathToTable
        shortestDistanceDestTable.setRowSorter(newSorter);

        this.nodeListTable.setAutoCreateRowSorter(true);
        this.currentNodeListTable.setAutoCreateRowSorter(true);
        this.edgeListTable.setAutoCreateRowSorter(true);
        this.shortestDistanceSourceTable.setAutoCreateRowSorter(true);
        this.shortestDistanceDestTable.setAutoCreateRowSorter(true);
    }

    private void updateNodeTable() {
        int row = this.currentNodeListTable.getRowCount();
        while (row > 0) {
            ((DefaultTableModel) currentNodeListTable.getModel()).removeRow(row-1);
            row = this.currentNodeListTable.getRowCount();
        }

        row = this.nodeListTable.getRowCount();
        while (row > 0) {
            ((DefaultTableModel) nodeListTable.getModel()).removeRow(row-1);
            row = this.nodeListTable.getRowCount();
        }

        if (depthSelecter.getSelectedIndex() > 0 && depthNeighborNodeTypeSelector.getSelectedIndex() > 0 && depthNeighborNodeTypeSelector.getSelectedItem().toString().equals("P.")) {
            if (depthNodeNameSet != null && depthNodeNameSet.size() == 1) {
                int tableRowCount = 0;
                LinkedHashMap<String, Long> predecessorMap = new LinkedHashMap<>();
                Collection<MyNode> predecessors = MySequentialGraphVars.g.getPredecessors(MySequentialGraphVars.getSequentialGraphViewer().selectedNode);
                for (MyNode n : predecessors) {
                    predecessorMap.put(n.getName(), n.getContribution());
                }
                predecessorMap = MySequentialGraphSysUtil.sortMapByLongValue(predecessorMap);

                for (String predecessor : predecessorMap.keySet()) {
                    ((DefaultTableModel) currentNodeListTable.getModel()).addRow(
                        new String[]{"" + MyMathUtil.getCommaSeperatedNumber(++tableRowCount),
                            MySequentialGraphSysUtil.getDecodedNodeName(predecessor),
                            MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(predecessorMap.get(predecessor)))
                        }
                    );
                }

                tableRowCount = 0;
                for (String depthNode : depthNodeNameSet) {
                    ((DefaultTableModel) nodeListTable.getModel()).addRow(
                        new String[]{"" + MyMathUtil.getCommaSeperatedNumber(++tableRowCount),
                            MySequentialGraphSysUtil.getDecodedNodeName(depthNode),
                            MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(((MyNode) MySequentialGraphVars.g.vRefs.get(depthNode)).getInContribution()))
                        }
                    );
                }
            } else {
                Map<String, Integer> uniquePredecessorMap = new HashMap<>();
                Set<String> depthNodeSet = depthNodePredecessorMaps.keySet();
                for (String depthNode : depthNodeSet) {
                    Map<String, Integer> predecessorMap = depthNodePredecessorMaps.get(depthNode);
                    for (String predecessor : predecessorMap.keySet()) {
                        if (uniquePredecessorMap.containsKey(predecessor)) {
                            uniquePredecessorMap.put(predecessor, uniquePredecessorMap.get(predecessor) + 1);
                        } else {
                            uniquePredecessorMap.put(predecessor, 1);
                        }
                    }
                }

                int tableRowCount = 0;
                for (String predecessor : uniquePredecessorMap.keySet()) {
                    ((DefaultTableModel) currentNodeListTable.getModel()).addRow(
                        new String[]{"" + MyMathUtil.getCommaSeperatedNumber(++tableRowCount),
                            MySequentialGraphSysUtil.getDecodedNodeName(predecessor),
                            MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(uniquePredecessorMap.get(predecessor)))
                        }
                    );
                }

                tableRowCount = 0;
                for (String depthNode : depthNodeSet) {
                    ((DefaultTableModel) nodeListTable.getModel()).addRow(
                        new String[]{"" + MyMathUtil.getCommaSeperatedNumber(++tableRowCount),
                            MySequentialGraphSysUtil.getDecodedNodeName(depthNode),
                            MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(((MyNode) MySequentialGraphVars.g.vRefs.get(depthNode)).getInContribution()))
                        }
                    );
                }
            }
        } else if (depthSelecter.getSelectedIndex() > 0 && depthNeighborNodeTypeSelector.getSelectedIndex() > 0 && depthNeighborNodeTypeSelector.getSelectedItem().toString().equals("S.")) {
            if (depthNodeNameSet != null && depthNodeNameSet.size() == 1) {
                MyDepthNodeUtil.setSelectedSingleDepthNodeSuccessors();
                int tableRowCount = 0;
                String selectedSingleNode = depthNodeNameSet.iterator().next();
                Map<String, Integer> successorMap = depthNodeSuccessorMaps.get(selectedSingleNode);
                System.out.println(successorMap);
                for (String successor : successorMap.keySet()) {
                    ((DefaultTableModel) currentNodeListTable.getModel()).addRow(
                        new String [] {"" + MyMathUtil.getCommaSeperatedNumber(++tableRowCount),
                            MySequentialGraphSysUtil.getDecodedNodeName(successor),
                            MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat((double)successorMap.get(successor)))
                        }
                    );
                }
                MySequentialGraphVars.getSequentialGraphViewer().revalidate();
                MySequentialGraphVars.getSequentialGraphViewer().repaint();
            } else {
                Map<String, Integer> uniqueSuccessorMap = new HashMap<>();
                Set<String> depthNodeSet = depthNodeSuccessorMaps.keySet();
                for (String depthNode : depthNodeSet) {
                    Map<String, Integer> successorMap = depthNodeSuccessorMaps.get(depthNode);
                    for (String successor : successorMap.keySet()) {
                        if (uniqueSuccessorMap.containsKey(successor)) {
                            uniqueSuccessorMap.put(successor, uniqueSuccessorMap.get(successor) + 1);
                        } else {
                            uniqueSuccessorMap.put(successor, 1);
                        }
                    }
                }

                int tableRowCount = 0;
                for (String successor : uniqueSuccessorMap.keySet()) {
                    ((DefaultTableModel) currentNodeListTable.getModel()).addRow(
                        new String [] {"" + MyMathUtil.getCommaSeperatedNumber(++tableRowCount),
                            MySequentialGraphSysUtil.getDecodedNodeName(successor),
                            MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(uniqueSuccessorMap.get(successor)))
                        }
                    );
                }

                for (String depthNode : depthNodeSet) {
                    ((DefaultTableModel) nodeListTable.getModel()).addRow(
                        new String [] {"" + MyMathUtil.getCommaSeperatedNumber(++tableRowCount),
                            MySequentialGraphSysUtil.getDecodedNodeName(depthNode),
                            MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(((MyNode) MySequentialGraphVars.g.vRefs.get(depthNode)).getOutContribution()))
                        }
                    );
                }
            }
        } else if (depthSelecter.getSelectedIndex() > 0 && selectedNodeNeighborNodeTypeSelector.getSelectedIndex() > 0 && selectedNodeNeighborNodeTypeSelector.getSelectedItem().toString().equals("S.")) {
            if (depthNodeNameSet != null && depthNodeNameSet.size() == 1) {
                MyDepthNodeUtil.setSelectedSingleDepthNodeSuccessors();
                int tableRowCount = 0;
                String selectedSingleNode = depthNodeNameSet.iterator().next();
                Map<String, Integer> successorMap = depthNodeSuccessorMaps.get(selectedSingleNode);
                for (String successor : successorMap.keySet()) {
                    ((DefaultTableModel) currentNodeListTable.getModel()).addRow(
                            new String [] {"" + MyMathUtil.getCommaSeperatedNumber(++tableRowCount),
                                    MySequentialGraphSysUtil.getDecodedNodeName(successor),
                                    MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat((double)successorMap.get(successor)))
                            }
                    );
                }

                MyNode n = (MyNode) MySequentialGraphVars.g.vRefs.get(selectedSingleNode);
                ((DefaultTableModel) nodeListTable.getModel()).addRow(
                            new String [] {"" + MyMathUtil.getCommaSeperatedNumber(++tableRowCount),
                                    MySequentialGraphSysUtil.getDecodedNodeName(selectedSingleNode),
                                    MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(n.getNodeDepthInfo(MySequentialGraphVars.currentGraphDepth).getContribution()))
                            });

                MySequentialGraphVars.getSequentialGraphViewer().revalidate();
                MySequentialGraphVars.getSequentialGraphViewer().repaint();
            } else {
                Map<String, Integer> uniqueSuccessorMap = new HashMap<>();
                Set<String> depthNodeSet = depthNodeSuccessorMaps.keySet();
                for (String depthNode : depthNodeSet) {
                    Map<String, Integer> successorMap = depthNodeSuccessorMaps.get(depthNode);
                    for (String successor : successorMap.keySet()) {
                        if (uniqueSuccessorMap.containsKey(successor)) {
                            uniqueSuccessorMap.put(successor, uniqueSuccessorMap.get(successor) + 1);
                        } else {
                            uniqueSuccessorMap.put(successor, 1);
                        }
                    }
                }

                int tableRowCount = 0;
                for (String successor : uniqueSuccessorMap.keySet()) {
                    ((DefaultTableModel) currentNodeListTable.getModel()).addRow(
                            new String [] {"" + MyMathUtil.getCommaSeperatedNumber(++tableRowCount),
                                    MySequentialGraphSysUtil.getDecodedNodeName(successor),
                                    MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(uniqueSuccessorMap.get(successor)))
                            }
                    );
                }

                for (String depthNode : depthNodeSet) {
                    ((DefaultTableModel) nodeListTable.getModel()).addRow(
                            new String [] {"" + MyMathUtil.getCommaSeperatedNumber(++tableRowCount),
                                    MySequentialGraphSysUtil.getDecodedNodeName(depthNode),
                                    MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(((MyNode) MySequentialGraphVars.g.vRefs.get(depthNode)).getOutContribution()))
                            }
                    );
                }
            }
        } else if (depthSelecter.getSelectedIndex() > 0) {
            if (depthNodeNameSet != null && depthNodeNameSet.size() == 1) {
                int tableRowCount = 0;
                MyNode selectedSingleNode = (MyNode) MySequentialGraphVars.g.vRefs.get(depthNodeNameSet.iterator().next());
                ((DefaultTableModel) nodeListTable.getModel()).addRow(
                    new String [] {"" + MyMathUtil.getCommaSeperatedNumber(++tableRowCount),
                        MySequentialGraphSysUtil.getDecodedNodeName(selectedSingleNode.getName()),
                        MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(selectedSingleNode.getContribution()))
                    }
                );

                tableRowCount = 0;
                ((DefaultTableModel) currentNodeListTable.getModel()).addRow(
                     new String [] {"" + MyMathUtil.getCommaSeperatedNumber(++tableRowCount),
                         MySequentialGraphSysUtil.getDecodedNodeName(selectedSingleNode.getName()),
                         MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(selectedSingleNode.getContribution()))
                    });
            } else if (depthNodeNameSet != null && MySequentialGraphVars.getSequentialGraphViewer().selectedNode != null) {
                int tableRowCount = 0;
                for (String n : depthNodeNameSet) {
                    ((DefaultTableModel) nodeListTable.getModel()).addRow(
                        new String[]{"" + MyMathUtil.getCommaSeperatedNumber(++tableRowCount),
                            MySequentialGraphSysUtil.getDecodedNodeName(n),
                            MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(((MyNode) MySequentialGraphVars.g.vRefs.get(n)).getContribution()))
                        }
                    );
                }

                LinkedHashMap<String, Long> nodeMap = new LinkedHashMap<>();
                Set<MyEdge> edges = new HashSet<>(MySequentialGraphVars.g.getInEdges(MySequentialGraphVars.getSequentialGraphViewer().selectedNode));
                edges.addAll(MySequentialGraphVars.g.getOutEdges(MySequentialGraphVars.getSequentialGraphViewer().selectedNode));
                if (edges != null) {
                    for (MyEdge e : edges) {
                        if (e.getDest() == MySequentialGraphVars.getSequentialGraphViewer().selectedNode) {
                            nodeMap.put(e.getDest().getName(), (long)e.getContribution());
                        } else if (e.getSource() == MySequentialGraphVars.getSequentialGraphViewer().selectedNode) {
                            nodeMap.put(e.getSource().getName(), (long)e.getContribution());
                        }
                    }
                }

                nodeMap = MySequentialGraphSysUtil.sortMapByLongValue(nodeMap);
                for (String n : nodeMap.keySet()) {
                    ((DefaultTableModel) currentNodeListTable.getModel()).addRow(
                        new String [] {"" + MyMathUtil.getCommaSeperatedNumber(++tableRowCount),
                            MySequentialGraphSysUtil.getDecodedNodeName(n),
                            MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(nodeMap.get(n)))});


                }
            } else if (depthNodeNameSet != null && MySequentialGraphVars.getSequentialGraphViewer().selectedNode == null) {
                LinkedHashMap<String, Long> nodeMap = new LinkedHashMap<>();
                for (String depthNode : depthNodeNameSet) {
                    nodeMap.put(depthNode, (long) ((MyNode) MySequentialGraphVars.g.vRefs.get(depthNode)).getCurrentValue());
                }
                nodeMap = MySequentialGraphSysUtil.sortMapByLongValue(nodeMap);
                int tableRowCount= 0;
                for (String n : nodeMap.keySet()) {
                    MyNode nn = (MyNode) MySequentialGraphVars.g.vRefs.get(n);
                    if (nn.getCurrentValue() > 0) {
                        ((DefaultTableModel) currentNodeListTable.getModel()).addRow(new String[]{
                            "" + (++tableRowCount),
                            MySequentialGraphSysUtil.getDecodedNodeName(nn.getName()),
                            MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(nodeMap.get(n)))}
                        );
                    }
                }

                tableRowCount= 0;
                for (String n :depthNodeNameSet) {
                    ((DefaultTableModel) nodeListTable.getModel()).addRow(
                        new String[]{"" + MyMathUtil.getCommaSeperatedNumber(++tableRowCount),
                            MySequentialGraphSysUtil.getDecodedNodeName(n),
                            MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(nodeMap.get(n)))
                        }
                    );
                }
            }
        } else if (MySequentialGraphVars.getSequentialGraphViewer().selectedNode != null) {
            if (MySequentialGraphVars.getSequentialGraphViewer().predecessorsOnly || MySequentialGraphVars.getSequentialGraphViewer().neighborsOnly || MySequentialGraphVars.getSequentialGraphViewer().successorsOnly) {
                LinkedHashMap<String, Long> nodeValueMap = new LinkedHashMap<>();
                Collection<MyNode> nodes = MySequentialGraphVars.g.getNeighbors(MySequentialGraphVars.getSequentialGraphViewer().selectedNode);
                for (MyNode n : nodes) {
                    if (n.getCurrentValue() > 0) {
                        nodeValueMap.put(n.getName(), (long) n.getCurrentValue());
                    }
                }
                nodeValueMap = MySequentialGraphSysUtil.sortMapByLongValue(nodeValueMap);

                int tableRowCount = 0;
                for (String n : nodeValueMap.keySet()) {
                    ((DefaultTableModel) currentNodeListTable.getModel()).addRow(
                            new String[]{"" + MyMathUtil.getCommaSeperatedNumber(++tableRowCount),
                                    MySequentialGraphSysUtil.getDecodedNodeName(n),
                                    MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(nodeValueMap.get(n)))}
                    );
                }

                tableRowCount= 0;
                nodes = new ArrayList(MySequentialGraphVars.g.getVertices());
                nodeValueMap = new LinkedHashMap<>();
                for (MyNode n : nodes) {
                    if (n.getCurrentValue() == 0 && n.getOriginalValue() > 0) {
                        n.setCurrentValue(n.getOriginalValue());
                    }
                    nodeValueMap.put(n.getName(), n.getContribution());
                }
                nodeValueMap = MySequentialGraphSysUtil.sortMapByLongValue(nodeValueMap);

                for (String n : nodeValueMap.keySet()) {
                    ((DefaultTableModel) nodeListTable.getModel()).addRow(
                            new String[]{"" + MyMathUtil.getCommaSeperatedNumber(++tableRowCount),
                                    MySequentialGraphSysUtil.getDecodedNodeName(n),
                                    MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(nodeValueMap.get(n)))}
                    );
                }
            } else {
                LinkedHashMap<String, Long> nodeValueMap = new LinkedHashMap<>();
                ArrayList<MyNode> nodes = new ArrayList<>(MySequentialGraphVars.g.getSuccessors(MySequentialGraphVars.getSequentialGraphViewer().selectedNode));
                nodes.addAll(MySequentialGraphVars.g.getPredecessors(MySequentialGraphVars.getSequentialGraphViewer().selectedNode));
                for (MyNode n : nodes) {
                    nodeValueMap.put(n.getName(), (long) n.getCurrentValue());
                }

                nodeValueMap = MySequentialGraphSysUtil.sortMapByLongValue(nodeValueMap);
                int tableRowCount = 0;
                for (String n : nodeValueMap.keySet()) {
                    MyNode nn = (MyNode) MySequentialGraphVars.g.vRefs.get(n);
                    ((DefaultTableModel) currentNodeListTable.getModel()).addRow(
                            new String[]{
                                    "" + (++tableRowCount),
                                    MySequentialGraphSysUtil.getDecodedNodeName(nn.getName()),
                                    MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(nodeValueMap.get(n)))});
                }

                tableRowCount= 0;
                nodes = new ArrayList(MySequentialGraphVars.g.getVertices());
                nodeValueMap = new LinkedHashMap<>();
                for (MyNode n : nodes) {
                    nodeValueMap.put(n.getName(), n.getContribution());
                }
                nodeValueMap = MySequentialGraphSysUtil.sortMapByLongValue(nodeValueMap);
                for (String n : nodeValueMap.keySet()) {
                    ((DefaultTableModel) nodeListTable.getModel()).addRow(
                        new String[]{"" + MyMathUtil.getCommaSeperatedNumber(++tableRowCount),
                        MySequentialGraphSysUtil.getDecodedNodeName(n),
                        MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(nodeValueMap.get(n)))}
                    );
                }
            }
        } else if (MySequentialGraphVars.getSequentialGraphViewer().multiNodes != null) {
            LinkedHashMap<String, Long> nodeMap = new LinkedHashMap<>();
            ArrayList<MyNode> nodes = new ArrayList(MySequentialGraphVars.getSequentialGraphViewer().multiNodePredecessors);
            nodes.addAll(MySequentialGraphVars.getSequentialGraphViewer().multiNodeSuccessors);
            for (MyNode n : nodes) {
                nodeMap.put(n.getName(), (long) n.getCurrentValue());
            }

            nodeMap = MySequentialGraphSysUtil.sortMapByLongValue(nodeMap);
            int tableRowCount = 0;
            for (String n : nodeMap.keySet()) {
                MyNode nn = (MyNode) MySequentialGraphVars.g.vRefs.get(n);
                ((DefaultTableModel) currentNodeListTable.getModel()).addRow(
                    new String[]{
                        "" + (++tableRowCount),
                         MySequentialGraphSysUtil.getDecodedNodeName(nn.getName()),
                         MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(nodeMap.get(n)))
                    });
            }

            tableRowCount= 0;
            nodes = new ArrayList(MySequentialGraphVars.g.getVertices());
            nodeMap = new LinkedHashMap<>();
            for (MyNode n : nodes) {
                nodeMap.put(n.getName(), (long) n.getContribution());
            }

            nodeMap = MySequentialGraphSysUtil.sortMapByLongValue(nodeMap);
            for (String n : nodeMap.keySet()) {
                ((DefaultTableModel) nodeListTable.getModel()).addRow(
                        new String[]{"" + MyMathUtil.getCommaSeperatedNumber(++tableRowCount),
                                MySequentialGraphSysUtil.getDecodedNodeName(n),
                                MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(nodeMap.get(n)))
                        }
                );
            }
        } else {
            LinkedHashMap<String, Long> nodeMap = new LinkedHashMap<>();
            Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
            for (MyNode n : nodes) {
                nodeMap.put(n.getName(), (long) n.getCurrentValue());
            }
            nodeMap = MySequentialGraphSysUtil.sortMapByLongValue(nodeMap);
            int tableRowCount= 0;
            for (String n : nodeMap.keySet()) {
                MyNode nn = (MyNode) MySequentialGraphVars.g.vRefs.get(n);
                if (nn.getCurrentValue() > 0) {
                    ((DefaultTableModel) currentNodeListTable.getModel()).addRow(new String[]{
                            "" + (++tableRowCount),
                            MySequentialGraphSysUtil.getDecodedNodeName(nn.getName()),
                            MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(nodeMap.get(n)))}
                    );
                }
            }

            tableRowCount= 0;
            for (String n : nodeMap.keySet()) {
                ((DefaultTableModel) nodeListTable.getModel()).addRow(
                        new String[]{"" + MyMathUtil.getCommaSeperatedNumber(++tableRowCount),
                                MySequentialGraphSysUtil.getDecodedNodeName(n),
                                MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(nodeMap.get(n)))
                        }
                );
            }
        }
    }

    private void updateEdgeTable() {
        int row = this.edgeListTable.getRowCount();
        while (row > 0) {
            ((DefaultTableModel) edgeListTable.getModel()).removeRow(row-1);
            row = this.edgeListTable.getRowCount();
        }

        if (MySequentialGraphVars.getSequentialGraphViewer().selectedNode != null) {
            if (MySequentialGraphVars.getSequentialGraphViewer().predecessorsOnly ||
                MySequentialGraphVars.getSequentialGraphViewer().successorsOnly) {
                LinkedHashMap<String, Long> edgeValueMap = new LinkedHashMap<>();
                Collection<MyEdge> edges = MySequentialGraphVars.g.getIncidentEdges(MySequentialGraphVars.getSequentialGraphViewer().selectedNode);
                for (MyEdge e : edges) {
                    if (MySequentialGraphVars.getSequentialGraphViewer().predecessorsOnly && e.getDest().getName().equals(MySequentialGraphVars.getSequentialGraphViewer().selectedNode.getName())) {
                        edgeValueMap.put(e.getSource() + "-" + e.getDest(), (long) e.getContribution());
                    } else if (MySequentialGraphVars.getSequentialGraphViewer().successorsOnly && e.getSource().getName().equals(MySequentialGraphVars.getSequentialGraphViewer().selectedNode.getName())) {
                        edgeValueMap.put(e.getSource() + "-" + e.getDest(), (long) e.getContribution());
                    }
                }
                edgeValueMap = MySequentialGraphSysUtil.sortMapByLongValue(edgeValueMap);

                for (String edgeName : edgeValueMap.keySet()) {
                    String [] pairNames = edgeName.split("-");
                    ((DefaultTableModel) edgeListTable.getModel()).addRow(new String[]{
                        MySequentialGraphSysUtil.getNodeName(pairNames[0]),
                        MySequentialGraphSysUtil.getNodeName(pairNames[1]),
                        MyMathUtil.getCommaSeperatedNumber(edgeValueMap.get(edgeName))});
                }
            } else {
                LinkedHashMap<String, Long> edgeValueMap = new LinkedHashMap<>();
                Collection<MyEdge> edges = MySequentialGraphVars.g.getIncidentEdges(MySequentialGraphVars.getSequentialGraphViewer().selectedNode);
                for (MyEdge e : edges) {
                    edgeValueMap.put(e.getSource() + "-" + e.getDest(), (long) e.getContribution());
                }
                edgeValueMap = MySequentialGraphSysUtil.sortMapByLongValue(edgeValueMap);

                for (String edgeName : edgeValueMap.keySet()) {
                    String [] pairNames = edgeName.split("-");
                    ((DefaultTableModel) edgeListTable.getModel()).addRow(new String[]{
                        MySequentialGraphSysUtil.getNodeName(pairNames[0]),
                        MySequentialGraphSysUtil.getNodeName(pairNames[1]),
                        MyMathUtil.getCommaSeperatedNumber(edgeValueMap.get(edgeName))});
                }
            }
        } else if (MySequentialGraphVars.getSequentialGraphViewer().multiNodes != null) {
            LinkedHashMap<String, Long> edgeMap = new LinkedHashMap<>();
            for (MyNode selectedNode : MySequentialGraphVars.getSequentialGraphViewer().multiNodes) {
                Collection<MyEdge> edges = MySequentialGraphVars.g.getIncidentEdges(selectedNode);
                for (MyEdge e : edges) {
                    edgeMap.put(e.getSource() + "-" + e.getDest(), (long) e.getContribution());
                }
            }
            edgeMap = MySequentialGraphSysUtil.sortMapByLongValue(edgeMap);
            for (String edgeName : edgeMap.keySet()) {
                String[] pairNames = edgeName.split("-");
                ((DefaultTableModel) edgeListTable.getModel()).addRow(
                        new String[]{
                                MySequentialGraphSysUtil.getNodeName(pairNames[0]),
                                MySequentialGraphSysUtil.getNodeName(pairNames[1]),
                                MyMathUtil.getCommaSeperatedNumber(edgeMap.get(edgeName))});
            }
        } else {
            LinkedHashMap<String, Long> edgeMap = new LinkedHashMap<>();
            Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
            for (MyEdge e : edges) {
                edgeMap.put(e.getSource() + "-" + e.getDest(), (long)e.getContribution());
            }
            edgeMap = MySequentialGraphSysUtil.sortMapByLongValue(edgeMap);
            for (String edgeName : edgeMap.keySet()) {
                String [] pairNames = edgeName.split("-");
                ((DefaultTableModel) edgeListTable.getModel()).addRow(
                        new String[]{
                                MySequentialGraphSysUtil.getNodeName(pairNames[0]),
                                MySequentialGraphSysUtil.getNodeName(pairNames[1]),
                                MyMathUtil.getCommaSeperatedNumber(edgeMap.get(edgeName))});
            }
        }
    }

    public MyViewerComponentController getSequentialGraphControllerPanel() {return this;}

    @Override public void actionPerformed(ActionEvent ae) {

        if (ae.getSource() == nodeValueBarChart) {
            new Thread(new Runnable() {
                @Override public void run() {
                    if (nodeValueBarChart.isSelected()) {
                        nodeLabelBarChart.setSelected(false);
                        MyViewerComponentControllerUtil.removeNodeBarChartsFromViewer();
                        MyViewerComponentControllerUtil.removeSharedNodeValueBarCharts();
                        if (MySequentialGraphVars.getSequentialGraphViewer().isClustered) {
                            if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueBarChart.getText().equals("N. V. B.")) {
                                MyMessageUtil.showInfoMsg("Select a cluster ID.");
                                return;
                            }
                            MySequentialGraphVars.getSequentialGraphViewer().clusteredGraphLevelNodeValueBarChart = new MyClusteredGraphLevelNodeValueBarChart();
                            MySequentialGraphVars.getSequentialGraphViewer().add(MySequentialGraphVars.getSequentialGraphViewer().clusteredGraphLevelNodeValueBarChart);
                        } else if (MySequentialGraphVars.getSequentialGraphViewer().selectedNode != null) {
                            MyViewerComponentControllerUtil.setSelectedNodeNeighborValueBarChartToViewer();
                        } else if (MySequentialGraphVars.getSequentialGraphViewer().multiNodes != null) {
                            MyViewerComponentControllerUtil.setSharedNodeLevelNodeValueBarChart();
                        } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthSelecter.getSelectedIndex() > 0 && MySequentialGraphVars.getSequentialGraphViewer().vc.depthNeighborNodeTypeSelector.getSelectedIndex() == 0) {
                            MyViewerComponentControllerUtil.setDepthNodeBarChartToViewer();
                        } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthSelecter.getSelectedIndex() > 0 && MySequentialGraphVars.getSequentialGraphViewer().vc.depthNeighborNodeTypeSelector.getSelectedIndex() > 0) {
                            MyViewerComponentControllerUtil.setDepthNeighborNodeBarChartToViewer();
                        } else {
                            MyViewerComponentControllerUtil.setNodeBarChartToViewer();
                        }
                    } else {
                        MyViewerComponentControllerUtil.removeNodeBarChartsFromViewer();
                        MyViewerComponentControllerUtil.removeSharedNodeValueBarCharts();
                    }
                    MySequentialGraphVars.getSequentialGraphViewer().revalidate();
                    MySequentialGraphVars.getSequentialGraphViewer().repaint();
                }
            }).start();
        } else if (ae.getSource() == nodeLabelBarChart) {
            new Thread(new Runnable() {
                @Override public void run() {
                    if (nodeLabelBarChart.isSelected()) {
                        nodeValueBarChart.setSelected(false);
                        MyViewerComponentControllerUtil.removeNodeBarChartsFromViewer();
                        MyViewerComponentControllerUtil.removeSharedNodeValueBarCharts();

                        MySequentialGraphVars.getSequentialGraphViewer().graphLevelNodeLabelBarChart = new MyGraphLevelNodeLabelBarChart();
                        MySequentialGraphVars.getSequentialGraphViewer().add(MySequentialGraphVars.getSequentialGraphViewer().graphLevelNodeLabelBarChart);
                    } else {
                        MyViewerComponentControllerUtil.removeNodeBarChartsFromViewer();
                        MyViewerComponentControllerUtil.removeSharedNodeValueBarCharts();
                    }
                    MySequentialGraphVars.getSequentialGraphViewer().revalidate();
                    MySequentialGraphVars.getSequentialGraphViewer().repaint();
                }
            }).start();
        } else if (ae.getSource() == edgeLabelBarChart) {
            new Thread(new Runnable() {
                @Override public void run() {
                    if (edgeLabelBarChart.isSelected()) {
                        edgeValueBarChart.setSelected(false);
                        MyViewerComponentControllerUtil.removeEdgeValueBarChartFromViewer();
                        MySequentialGraphVars.getSequentialGraphViewer().graphLevelEdgeLabelBarChart = new MyGraphLevelEdgeLabelBarChart();
                        MySequentialGraphVars.getSequentialGraphViewer().add(MySequentialGraphVars.getSequentialGraphViewer().graphLevelEdgeLabelBarChart);
                        MySequentialGraphVars.getSequentialGraphViewer().graphLevelEdgeLabelBarChart.setEdgeLabelBarChart();
                    } else if (MySequentialGraphVars.getSequentialGraphViewer().graphLevelEdgeLabelBarChart != null) {
                        MySequentialGraphVars.getSequentialGraphViewer().remove(MySequentialGraphVars.getSequentialGraphViewer().graphLevelEdgeLabelBarChart);
                    }
                    MySequentialGraphVars.getSequentialGraphViewer().revalidate();
                    MySequentialGraphVars.getSequentialGraphViewer().repaint();
                }
            }).start();
        } else if (edgeValueBarChart == ae.getSource()) {
            new Thread(new Runnable() {
                @Override public void run() {
                    if (edgeValueBarChart.isSelected()) {
                        if (edgeValueSelecter.getSelectedIndex()  < 2) {
                            MyMessageUtil.showInfoMsg(MySequentialGraphVars.app, "Set an edge value, first.");
                            edgeValueBarChart.setSelected(false);
                            return;
                        }

                        MyViewerComponentControllerUtil.removeEdgeValueBarChartFromViewer();
                        if (MySequentialGraphVars.getSequentialGraphViewer().isClustered) {
                            MySequentialGraphVars.getSequentialGraphViewer().clusteredGraphLevelEdgeValueBarChart = new MyClusteredGraphLevelEdgeValueBarChart();
                            MySequentialGraphVars.getSequentialGraphViewer().add(MySequentialGraphVars.getSequentialGraphViewer().clusteredGraphLevelEdgeValueBarChart);
                        } else if (MySequentialGraphVars.getSequentialGraphViewer().multiNodes != null &&
                            MySequentialGraphVars.getSequentialGraphViewer().multiNodes.size() > 0) {
                            MyViewerComponentControllerUtil.setShareNodeLevelEdgeValueBarChartToViewer();
                        } else if (depthSelecter.getSelectedIndex() > 0) {
                            if (depthNeighborNodeTypeSelector.getSelectedIndex() > 0) {
                                MySequentialGraphVars.getSequentialGraphViewer().graphLevelEdgeValueBarChart = new MyGraphLevelEdgeValueBarChart();
                                MySequentialGraphVars.getSequentialGraphViewer().graphLevelEdgeValueBarChart.setEdgeValueBarChartForDepthNodes();
                                MySequentialGraphVars.getSequentialGraphViewer().add(MySequentialGraphVars.getSequentialGraphViewer().graphLevelEdgeValueBarChart);
                            }
                        } else if (MySequentialGraphVars.getSequentialGraphViewer().selectedNode != null) {
                            MyViewerComponentControllerUtil.setSingleNodeLevelEdgeBarChartToViewer();
                        } else {
                            MyViewerComponentControllerUtil.setEdgeBarChartToViewer();
                        }
                        edgeLabelBarChart.setSelected(false);
                    } else {
                        if (MySequentialGraphVars.getSequentialGraphViewer().graphLevelEdgeValueBarChart != null) {
                            MySequentialGraphVars.getSequentialGraphViewer().remove(MySequentialGraphVars.getSequentialGraphViewer().graphLevelEdgeValueBarChart);
                        }
                    }
                    MySequentialGraphVars.getSequentialGraphViewer().revalidate();
                    MySequentialGraphVars.getSequentialGraphViewer().repaint();
                }
            }).start();
        } else if (ae.getSource() == edgeLabelSelecter) {
            new Thread(new Runnable() {
                @Override public void run() {
                    if (edgeLabelSelecter.getSelectedIndex() > 0) {
                        edgeLabelBarChart.setVisible(true);
                    } else {
                        edgeLabelBarChart.setVisible(false);
                        edgeLabelBarChart.setSelected(false);
                        MyViewerComponentControllerUtil.removeEdgeValueBarChartFromViewer();
                    }
                    MyViewerComponentControllerUtil.showEdgeLabel();
                }
            }).start();
        } else if (ae.getSource() == nodeLabelSelecter) {
            new Thread(new Runnable() {
                @Override public void run() {
                    if (nodeLabelSelecter.getSelectedIndex() > 1) {
                        nodeLabelBarChart.setVisible(true);
                    } else {
                        nodeLabelBarChart.setVisible(false);
                        nodeLabelBarChart.setSelected(false);
                        MyViewerComponentControllerUtil.removeNodeBarChartsFromViewer();
                    }
                    MyViewerComponentControllerUtil.showNodeLabel();
                }
            }).start();
        } else if (ae.getSource() == clusteringSelector) {
            new Thread(new Runnable() {
                @Override public void run() {
                    if (clusteringSelector.getSelectedIndex() == 1) {
                        if (MyClusteringConfig.instances++ == 0) {
                            edgeValueBarChart.setText("C. E. V. B");
                            nodeValueBarChart.setText("C. N. V. B");
                            MyViewerComponentControllerUtil.setDefaultViewerLook();
                            MyClusteringConfig clusteringConfig = new MyClusteringConfig();
                        }
                    } else if (clusteringSelector.getSelectedIndex() == 0) {
                        if (MySequentialGraphVars.getSequentialGraphViewer().isClustered) {
                            MyViewerComponentControllerUtil.setDefaultViewerLook();
                            MySequentialGraphVars.getSequentialGraphViewer().isClustered = false;
                        }
                    } else if (clusteringSelector.getSelectedIndex() == 2) {

                    }
                }
            }).start();
        } else if (ae.getSource() == weightedNodeColor) {
            new Thread(new Runnable() {
                @Override public void run() {
                    if (weightedNodeColor.isSelected()) {
                        MySequentialGraphVars.getSequentialGraphViewer().getRenderContext().setVertexFillPaintTransformer(MySequentialGraphVars.getSequentialGraphViewer().weightedNodeColor);
                    } else {
                        MySequentialGraphVars.getSequentialGraphViewer().getRenderContext().setVertexFillPaintTransformer(MySequentialGraphVars.getSequentialGraphViewer().unWeightedNodeColor);
                    }
                    MySequentialGraphVars.getSequentialGraphViewer().revalidate();
                    MySequentialGraphVars.getSequentialGraphViewer().repaint();
                }
            }).start();
        } else if (ae.getSource() == depthNeighborNodeTypeSelector) {
            new Thread(new Runnable() {
                @Override public void run() {
                    MyProgressBar pb = new MyProgressBar(false);
                    nodeValueBarChart.setSelected(false);
                    pb.updateValue(40, 100);
                    MyDepthNodeUtil.setDepthNodeNeighbors();
                    pb.updateValue(70, 100);
                    MySequentialGraphVars.app.getSequentialGraphDashboard().setDepthNodeDashBoard();
                    pb.updateValue(85, 100);
                    updateNodeTable();
                    MyViewerComponentControllerUtil.adjustDepthNodeValueMenu();
                    MySequentialGraphVars.getSequentialGraphViewer().revalidate();
                    MySequentialGraphVars.getSequentialGraphViewer().repaint();
                    pb.updateValue(100, 100);
                    pb.dispose();
                }
            }).start();
        } else if (ae.getSource() == selectedNodeNeighborNodeTypeSelector) {
            new Thread(new Runnable() {
                @Override public void run() {
                    MyProgressBar pb = new MyProgressBar(false);
                    MyViewerComponentControllerUtil.setExcludeSymbolOption();
                    pb.updateValue(30, 100);
                    MyDepthNodeUtil.setSelectedSingleDepthNodeNeighbors();
                    pb.updateValue(65, 100);
                    pb.updateValue(95, 100);
                    updateTableInfos();
                    MyViewerComponentControllerUtil.adjustDepthNodeValueMenu();
                    pb.updateValue(100, 100);
                    pb.dispose();
                }
            }).start();
        } else if (ae.getSource() == nodeValueSelecter) {
          new Thread(new Runnable() {
              @Override public void run() {
                  MyProgressBar pb = new MyProgressBar(false);
                  if (selectedNodeNeighborNodeTypeSelector.isShowing()) {
                      pb.updateValue(100, 100);
                      pb.dispose();
                      return;
                  }

                  if (MySequentialGraphVars.currentGraphDepth > 0) {
                      MyDepthNodeUtil.setDepthNodeValue();
                  } else {
                      MyNodeUtil.setNodeValue();
                  }


                  if (edgeValueSelecter.getSelectedIndex() < 2) {
                      edgeValueBarChart.removeActionListener(getSequentialGraphControllerPanel());
                      edgeValueBarChart.setSelected(false);
                      edgeValueBarChart.addActionListener(getSequentialGraphControllerPanel());
                  }

                  updateNodeTable();
                  pb.updateValue(100, 100);
                  pb.dispose();
                  nodeValueSelecter.setToolTipText("NODE VALUE: " + nodeValueSelecter.getSelectedItem().toString());
              }
          }).start();
      } else if (ae.getSource() == edgeValueSelecter) {
            new Thread(new Runnable() {
                @Override public void run() {
                    if ((depthNeighborNodeTypeSelector.isShowing() && depthNeighborNodeTypeSelector.getSelectedIndex() == 0) ||
                        (selectedNodeNeighborNodeTypeSelector.isShowing() && selectedNodeNeighborNodeTypeSelector.getSelectedIndex() == 0)) {
                        MyMessageUtil.showInfoMsg("Select a predecessor or successor relation option, first.");
                        MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.removeActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
                        MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.setSelectedIndex(0);
                        MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.addActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
                        return;
                    }

                    MySequentialGraphVars.getSequentialGraphViewer().edgeValName = edgeValueSelecter.getSelectedItem().toString().replaceAll(" ", "");
                    MyProgressBar pb = new MyProgressBar(false);
                    MyEdgeUtil.setEdgeValue();

                    updateNodeTable();
                    edgeValueSelecter.setToolTipText("EDGE VALUE: " + edgeValueSelecter.getSelectedItem().toString());
                    if (edgeValueSelecter.getSelectedIndex() < 2) {
                        edgeValueBarChart.removeActionListener(getSequentialGraphControllerPanel());
                        edgeValueBarChart.setSelected(false);
                        edgeValueBarChart.addActionListener(getSequentialGraphControllerPanel());
                    }

                    MySequentialGraphVars.getSequentialGraphViewer().revalidate();
                    MySequentialGraphVars.getSequentialGraphViewer().repaint();
                    pb.updateValue(100, 100);
                    pb.dispose();
                }
            }).start();
        } else if (ae.getSource() == depthSelecter) {
            new Thread(new Runnable() { // When depth option selected, edge values and edge label components are disabled.
                @Override public void run() {
                    if (depthSelecter.getSelectedIndex() == 0) MySequentialGraphVars.currentGraphDepth = 0;
                    else MySequentialGraphVars.currentGraphDepth = Integer.parseInt(depthSelecter.getSelectedItem().toString().trim());
                    if (depthSelecter.getSelectedIndex() == 0) {
                        clusteringSectorLabel.setEnabled(true);
                        clusteringSelector.setEnabled(true);
                        tableTabbedPane.setEnabledAt(1, true);
                        tableTabbedPane.setEnabledAt(2, true);

                        MyViewerComponentControllerUtil.setDefaultViewerLook();
                        return;
                    } else if (MySequentialGraphVars.getSequentialGraphViewer().selectedNode != null) {
                        clusteringSectorLabel.setEnabled(false);
                        clusteringSelector.setEnabled(false);

                        if (depthSelecter.getSelectedIndex() == 0) {
                            MyViewerComponentControllerUtil.setDefaultViewerLook();
                            tableTabbedPane.setEnabledAt(1, true);
                            tableTabbedPane.setEnabledAt(2, true);
                        } else {

                            nodeValueBarChart.setText("DEPTH N. V. B.");
                            tableTabbedPane.setEnabledAt(1, false);
                            tableTabbedPane.setEnabledAt(2, false);
                            MyViewerComponentControllerUtil.removeNodeBarChartsFromViewer();
                            MyViewerComponentControllerUtil.removeEdgeValueBarChartFromViewer();
                            depthNodeNameSet = new HashSet<>();
                            depthNodeNameSet.add(MySequentialGraphVars.getSequentialGraphViewer().selectedNode.getName());
                            selectedNodeNeighborNodeTypeSelector.setVisible(true);
                            Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
                            for (MyNode n : nodes) {
                                if (!depthNodeNameSet.contains(n.getName())) {
                                    n.setCurrentValue(0f);
                                }
                            }
                            updateNodeTable();
                            MyViewerComponentControllerUtil.setSelectedNodeNeighborNodeTypeOption();
                            MySequentialGraphVars.getSequentialGraphViewer().selectedNode = null;
                        }
                    } else if (selectedNodeNeighborNodeTypeSelector.isShowing()) {
                        MySequentialGraphVars.currentGraphDepth = Integer.parseInt(depthSelecter.getSelectedItem().toString().trim());
                        clusteringSectorLabel.setEnabled(false);
                        clusteringSelector.setEnabled(false);
                        tableTabbedPane.setEnabledAt(1, false);
                        tableTabbedPane.setEnabledAt(2, false);

                        nodeValueBarChart.setText("DEPTH N. V. B.");
                        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
                        for (MyNode n : nodes) {
                            if (!depthNodeNameSet.contains(n.getName())) {
                                n.setCurrentValue(0f);
                            }
                        }
                        updateNodeTable();
                        MyViewerComponentControllerUtil.setSelectedNodeNeighborNodeTypeOption();
                        MyViewerComponentControllerUtil.setSelectedNodeVisibleOnly();
                        MySequentialGraphVars.getSequentialGraphViewer().revalidate();
                        MySequentialGraphVars.getSequentialGraphViewer().repaint();
                    } else {
                        MyProgressBar pb = new MyProgressBar(false);
                        MySequentialGraphVars.currentGraphDepth = Integer.parseInt(depthSelecter.getSelectedItem().toString().trim());
                        clusteringSectorLabel.setEnabled(false);
                        clusteringSelector.setEnabled(false);
                        tableTabbedPane.setEnabledAt(1, false);
                        tableTabbedPane.setEnabledAt(2, false);
                        nodeValueBarChart.setText("DEPTH N. V. B.");
                        MyViewerComponentControllerUtil.setDepthNodeNameSet();
                        MyViewerComponentControllerUtil.setDepthNodeNeighborNodeTypeOption();
                        MyDepthNodeUtil.setDepthNodeValue();
                        MySequentialGraphVars.app.getSequentialGraphDashboard().setDepthNodeDashBoard();
                        //vTxtStat.setTextStatistics();
                        updateNodeTable();
                        pb.updateValue(100, 100);
                        pb.dispose();
                        MySequentialGraphVars.getSequentialGraphViewer().revalidate();
                        MySequentialGraphVars.getSequentialGraphViewer().repaint();
                    }
                    MyViewerComponentControllerUtil.adjustDepthNodeValueMenu();
                }
            }).start();
        }
    }



}


