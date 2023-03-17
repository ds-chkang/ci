package datamining.graph;

import datamining.graph.listener.MyEdgeLabelSelecterListener;
import datamining.graph.listener.MyNodeLabelSelecterListener;
import datamining.main.MyProgressBar;
import datamining.graph.path.MyDepthFirstGraphPathSercher;
import datamining.graph.stats.MyTextStatistics;
import datamining.graph.stats.MyGraphLevelEdgeValueBarChart;
import datamining.graph.stats.MyGraphLevelNodeValueBarChart;
import datamining.graph.listener.MyNodeEdgeExclusionActionListener;
import datamining.utils.*;
import datamining.utils.message.MyMessageUtil;
import datamining.utils.system.MySysUtil;
import datamining.utils.system.MyVars;
import datamining.utils.table.MyTableUtil;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class MyViewerComponentController
extends JPanel
implements ActionListener {

    public final String [] nodeValueItems = {
        "CONT.",
        "IN CONT.",
        "OUT CONT.",
        "CONT. D.",
        "UNIQ. CONT.",
        "IN N.",
        "OUT N.",
        "INOUT N. D.",
        "ENDING N.",
        "DURATION",
        "AVG. T.",
        "TOT. T.",
        "RECURRENCE",
        "BETWEENESS",
        "CLOSENESS",
        "EIGENVECTOR",
            "PAGERANK"
    };

    public final String [] depthNodeValueItems = {
        "CONT.",
        "IN CONT.",
        "OUT CONT.",
        "CONT. D.",
        "IN N.",
        "OUT N.",
        "INOUT N. D.",
        "DURATION",
        "REACH T."
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
        "CONT.",
        "UNIQ. CONT.",
        "AVG. T.",
        "TOT. T.",
        "MAX. T.",
        "MIN. T.",
        "SUPPORT",
        "CONFIDENCE",
        "LIFT",
        "BETWEENESS"
    };

    public JTable statTable;
    public JTable edgeTable;
    private JPanel topLeftPanel = new JPanel();
    public JButton excludeBtn;
    public MyTextStatistics vTxtStat = new MyTextStatistics();
    public JComboBox depthSelecter = new JComboBox();
    public JComboBox nodeValueSelecter = new JComboBox();
    public JComboBox edgeValueSelecter = new JComboBox();
    public JComboBox nodeValueExcludeSymbolSelecter;
    public JComboBox edgeValueExcludeSymbolSelecter;
    public JComboBox edgeLabelSelecter = new JComboBox();
    public JComboBox nodeLabelSelecter = new JComboBox();
    public JComboBox nodeLabelExcludeSymbolSelecter = new JComboBox();
    public JComboBox nodeLabelExcludeSelecter = new JComboBox();
    public JComboBox nodeLabelValueExcludeSelecter = new JComboBox();
    public JComboBox edgeLabelExcludeSymbolSelecter = new JComboBox();
    public JComboBox edgeLabelExcludeSelecter = new JComboBox();
    public JComboBox edgeLabelValueExcludeSelecter = new JComboBox();
    public JComboBox selectedNodeNeighborNodeTypeSelector = new JComboBox();
    public JComboBox depthNeighborNodeTypeSelector = new JComboBox();
    public JComboBox graphGroupSelecter = new JComboBox();
    public JLabel graphGroupSelecterLabel = new JLabel("G. G.: ");
    public JTextField nodeValueExcludeTxt = new JTextField();
    public JTextField edgeValueExcludeTxt = new JTextField();
    public JCheckBox weightedNodeColor = new JCheckBox("WGT. N. C.");
    public JCheckBox nodeValueBarChart = new JCheckBox("N. V. B.");
    public JCheckBox edgeValueBarChart = new JCheckBox("E. V. B.");
    public JCheckBox endingNodeBarChart = new JCheckBox("E. N. B.");
    public Map<String, Integer> selectedNodeSuccessorDepthNodeMap;
    public Map<String, Integer> selectedNodePredecessorDepthNodeMap;
    public Map<String, Map<String, Integer>> depthNodeSuccessorMaps;
    public Map<String, Map<String, Integer>> depthNodePredecessorMaps;
    public Set<String> depthNodeNameSet;
    public Set<String> depthNeighborSet;
    public JPanel bottomLeftControlPanel =new JPanel();
    public JSplitPane graphTableSplitPane = new JSplitPane();
    public JTabbedPane tabbedPane = new JTabbedPane();
    public JTable pathFromTable;
    public JTable pathToTable;
    public JTable nodeListTable;
    public JTable currentNodeListTable;

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
            this.nodeValueExcludeSymbolSelecter.setFont(MyVars.tahomaPlainFont11);
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
            this.edgeValueExcludeSymbolSelecter.setFont(MyVars.tahomaPlainFont11);
            this.edgeValueExcludeSymbolSelecter.setBackground(Color.WHITE);
            this.edgeValueExcludeSymbolSelecter.setFocusable(false);
            this.edgeValueExcludeSymbolSelecter.addActionListener(this);
        }
    }

    private JPanel setPathFindTable() {
        JPanel fromTablePanel = new JPanel();
        fromTablePanel.setLayout(new BorderLayout(2, 0));
        fromTablePanel.setBackground(Color.WHITE);

        JComboBox pathMenu = new JComboBox();
        pathMenu.setFont(MyVars.tahomaPlainFont10);
        pathMenu.setFocusable(false);
        pathMenu.addItem("SELECT");
        pathMenu.addItem("PATH EXPLORATION");
        pathMenu.addItem("SHORTEST PATH DEPTH");

        String [] fromColumns = {"NO.", "F. N."};
        String [][] fromData = {};
        DefaultTableModel fromTableModel = new DefaultTableModel(fromData, fromColumns);
        pathFromTable = new JTable(fromTableModel) {
            public String getToolTipText(MouseEvent e) {
                String tip = null;
                java.awt.Point p = e.getPoint();
                int rowIndex = rowAtPoint(p);
                int colIndex = columnAtPoint(p);
                try {
                    tip = getValueAt(rowIndex, colIndex).toString();
                } catch (RuntimeException e1) {}
                return tip;
            }
        };

        String [] fromTableTooltips = {"NO.", "FROM NODE"};
        MyTableToolTipper fromTableTooltipHeader = new MyTableToolTipper(this.pathFromTable.getColumnModel());
        fromTableTooltipHeader.setToolTipStrings(fromTableTooltips);
        this.pathFromTable.setTableHeader(fromTableTooltipHeader);

        int i=-0;
        for (String n : MyVars.nodeNameMap.keySet()) {
            fromTableModel.addRow(new String[]{String.valueOf(++i), n});
        }

        pathFromTable.setRowHeight(19);
        pathFromTable.setBackground(Color.WHITE);
        pathFromTable.setFont(MyVars.f_pln_9);
        pathFromTable.getTableHeader().setFont(MyVars.tahomaBoldFont10);
        pathFromTable.getTableHeader().setOpaque(false);
        pathFromTable.getTableHeader().setBackground(new Color(0,0,0,0f));
        pathFromTable.getColumnModel().getColumn(0).setPreferredWidth(30);
        pathFromTable.getColumnModel().getColumn(0).setMaxWidth(45);
        pathFromTable.getColumnModel().getColumn(1).setPreferredWidth(130);

        JTextField fromNodeSearchTxt = new JTextField();
        JButton fromNodeSelectBtn = new JButton();
        fromNodeSearchTxt.setToolTipText("TYPE A NODE TO SEARCH");
        fromNodeSearchTxt.setBackground(Color.WHITE);

        fromNodeSearchTxt.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        JPanel fromSearchAndSavePanel = MyTableUtil.searchAndSaveDataPanelForJTable2(this, fromNodeSearchTxt, fromNodeSelectBtn, fromTableModel, pathFromTable);
        fromNodeSelectBtn.setPreferredSize(new Dimension(40, 25));
        fromNodeSelectBtn.removeActionListener(this);
        fromNodeSelectBtn.removeActionListener(this);
        fromSearchAndSavePanel.remove(fromNodeSelectBtn);
        fromNodeSearchTxt.setFont(MyVars.f_bold_10);

        add(fromSearchAndSavePanel, BorderLayout.SOUTH);
        pathFromTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pathFromTable.setSelectionBackground(Color.LIGHT_GRAY);
        pathFromTable.setForeground(Color.BLACK);
        pathFromTable.setSelectionForeground(Color.BLACK);
        pathFromTable.setFocusable(false);
        pathFromTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pathFromTable.setRowSorter(MyTableUtil.setJTableRowSorterWithTextField(fromTableModel, fromNodeSearchTxt));

        JScrollPane fromTableScrollPane = new JScrollPane(pathFromTable);
        fromTableScrollPane.setOpaque(false);
        fromTableScrollPane.setBackground(new Color(0,0,0,0f));
        fromTableScrollPane.setPreferredSize(new Dimension(150, 500));
        fromTableScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(7, 0));
        fromTableScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 7));

        fromTablePanel.add(fromTableScrollPane, BorderLayout.CENTER);
        fromTablePanel.add(fromSearchAndSavePanel, BorderLayout.SOUTH);

        JPanel toTablePanel = new JPanel();
        toTablePanel.setLayout(new BorderLayout(2, 2));
        toTablePanel.setBackground(Color.WHITE);

        String [] toColumns = {"NO.", "T. N."};
        String [][] toData = {};
        DefaultTableModel toTableModel = new DefaultTableModel(toData, toColumns);
        pathToTable = new JTable(toTableModel) {
            //Implement table cell tool tips.
            public String getToolTipText(MouseEvent e) {
                String tip = null;
                java.awt.Point p = e.getPoint();
                int rowIndex = rowAtPoint(p);
                int colIndex = columnAtPoint(p);

                try {
                    tip = getValueAt(rowIndex, colIndex).toString();
                } catch (RuntimeException e1) {
                    //catch null pointer exception if mouse is over an empty line
                }

                return tip;
            }
        };

        String [] toTableTooltips = {"NO.", "TO NODE"};
        MyTableToolTipper toTableTooltipHeader = new MyTableToolTipper(this.pathToTable.getColumnModel());
        toTableTooltipHeader.setToolTipStrings(toTableTooltips);
        this.pathToTable.setTableHeader(toTableTooltipHeader);

        int ii=-0;
        for (String n : MyVars.nodeNameMap.keySet()) {
            toTableModel.addRow(new String[]{String.valueOf(++ii), n});
        }

        pathToTable.setRowHeight(19);
        pathToTable.setBackground(Color.WHITE);
        pathToTable.setFont(MyVars.f_pln_9);
        pathToTable.getTableHeader().setFont(MyVars.tahomaBoldFont10);
        pathToTable.getTableHeader().setOpaque(false);
        pathToTable.getTableHeader().setBackground(new Color(0,0,0,0f));
        pathToTable.getColumnModel().getColumn(0).setPreferredWidth(30);
        pathToTable.getColumnModel().getColumn(0).setMaxWidth(45);
        pathToTable.getColumnModel().getColumn(1).setPreferredWidth(130);

        JTextField toNodeSearchTxt = new JTextField();
        JButton runBtn = new JButton("RUN");
        runBtn.setFocusable(false);
        runBtn.setFont(MyVars.tahomaPlainFont11);

        toNodeSearchTxt.setBackground(Color.WHITE);
        toNodeSearchTxt.setFont(MyVars.f_bold_10);
        toNodeSearchTxt.setToolTipText("TYPE A NODE NAME TO SEARCH");
        toNodeSearchTxt.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        JPanel toSearchAndSavePanel = MyTableUtil.searchAndSaveDataPanelForJTable2(this, toNodeSearchTxt, runBtn, toTableModel, pathToTable);

        runBtn.setPreferredSize(new Dimension(50, 25));
        runBtn.removeActionListener(this);
        runBtn.removeActionListener(this);
        runBtn.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        if (pathMenu.getSelectedIndex() == 0) {
                            MyMessageUtil.showInfoMsg(MyVars.app, "Select a path option.");
                        } else if (pathMenu.getSelectedIndex() == 1) {
                            if (pathFromTable.getSelectedRow() >= 0 && pathToTable.getSelectedRow() >= 0) {
                                String fromTableNodeName = pathFromTable.getValueAt(pathFromTable.getSelectedRow(), 1).toString();
                                String toTableNodeName = pathToTable.getValueAt(pathToTable.getSelectedRow(), 1).toString();
                                String fromNode = MyVars.nodeNameMap.get(fromTableNodeName);
                                String toNode = MyVars.nodeNameMap.get(toTableNodeName);
                                if (fromNode.equals(toNode)) {
                                    MyMessageUtil.showInfoMsg("Choose different nodes to find paths.");
                                    return;
                                }
                                MyDepthFirstGraphPathSercher betweenPathGraphDepthFirstSearch = new MyDepthFirstGraphPathSercher();
                                betweenPathGraphDepthFirstSearch.decorate();
                                betweenPathGraphDepthFirstSearch.run(fromNode, toNode);
                                if (betweenPathGraphDepthFirstSearch.integratedGraph.getVertexCount() == 0) {
                                    betweenPathGraphDepthFirstSearch.dispose();
                                    MyMessageUtil.showInfoMsg("There are no reachable paths between [" + fromTableNodeName + "] AND [" + toTableNodeName + "]");
                                    return;
                                }
                                betweenPathGraphDepthFirstSearch.setAlwaysOnTop(true);
                                betweenPathGraphDepthFirstSearch.revalidate();
                                betweenPathGraphDepthFirstSearch.repaint();
                                betweenPathGraphDepthFirstSearch.setVisible(true);
                                betweenPathGraphDepthFirstSearch.setAlwaysOnTop(false);
                            } else {
                                MyMessageUtil.showInfoMsg(MyVars.app, "Two nodes are required for the operation.");
                            }
                        } else if (pathMenu.getSelectedIndex() == 2) {
                            if (pathFromTable.getSelectedRow() >= 0 && pathToTable.getSelectedRow() >= 0) {
                                String fromTableNodeName = pathFromTable.getValueAt(pathFromTable.getSelectedRow(), 1).toString();
                                String toTableNodeName = pathToTable.getValueAt(pathToTable.getSelectedRow(), 1).toString();
                                MyNode fromNode = (MyNode) MyVars.g.vRefs.get(MyVars.nodeNameMap.get(fromTableNodeName));
                                MyNode toNode = (MyNode) MyVars.g.vRefs.get(MyVars.nodeNameMap.get(toTableNodeName));
                                String shortestPathLength = MyMathUtil.getCommaSeperatedNumber(MySysUtil.getUnWeightedBetweenNodeShortestPathLength(fromNode, toNode));
                                try {
                                    Thread.sleep(500);
                                    if (shortestPathLength.equals("0")) {
                                        MyMessageUtil.showInfoMsg(MyVars.app, "[" + toTableNodeName + "] is unreachable from [" + fromTableNodeName + "]");
                                    } else {
                                        MyMessageUtil.showInfoMsg(MyVars.app, "Shortest Path Length Between [" + fromTableNodeName + "] and [" + toTableNodeName + "] is " + shortestPathLength);
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            } else {
                                MyMessageUtil.showInfoMsg(MyVars.app, "Two nodes are required for the operation.");
                            }
                        }
                    }
                }).start();
            }
        });

        add(toSearchAndSavePanel, BorderLayout.SOUTH);
        pathToTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pathToTable.setSelectionBackground(Color.LIGHT_GRAY);
        pathToTable.setSelectionForeground(Color.BLACK);
        pathToTable.setForeground(Color.BLACK);
        pathToTable.setFocusable(false);
        pathToTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pathToTable.setRowSorter(MyTableUtil.setJTableRowSorterWithTextField(toTableModel, toNodeSearchTxt));

        JScrollPane toTableScrollPane = new JScrollPane(pathToTable);
        toTableScrollPane.setOpaque(false);
        toTableScrollPane.setBackground(new Color(0,0,0,0f));
        toTableScrollPane.setPreferredSize(new Dimension(150, 500));
        toTableScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(7, 0));
        toTableScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 7));

        toTablePanel.add(toTableScrollPane, BorderLayout.CENTER);
        toTablePanel.add(toSearchAndSavePanel, BorderLayout.SOUTH);

        JPanel pathPanel = new JPanel();
        pathPanel.setBackground(Color.WHITE);
        pathPanel.setLayout(new BorderLayout(3,1));

        JPanel tablePanel = new JPanel();
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setLayout(new GridLayout(2,1));
        tablePanel.add(fromTablePanel);
        tablePanel.add(toTablePanel);

        pathPanel.add(tablePanel, BorderLayout.CENTER);
        pathPanel.add(pathMenu, BorderLayout.NORTH);
        return pathPanel;
    }

    public void setGraphPanel(JPanel graphPanel) {
        this.setLayout(new BorderLayout());
        this.setBackground(Color.WHITE);
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));

        this.selectedNodeNeighborNodeTypeSelector.setBackground(Color.WHITE);
        this.depthNeighborNodeTypeSelector.setToolTipText("SELECT A NEIGHBOR NODE TYPE FOR SELECTED NODES");
        this.selectedNodeNeighborNodeTypeSelector.setFont(MyVars.tahomaPlainFont11);
        this.selectedNodeNeighborNodeTypeSelector.setFocusable(false);
        this.selectedNodeNeighborNodeTypeSelector.addActionListener(this);
        this.selectedNodeNeighborNodeTypeSelector.setVisible(false);

        this.depthNeighborNodeTypeSelector.setBackground(Color.WHITE);
        this.depthNeighborNodeTypeSelector.setToolTipText("SELECT A NEIGHBOR NODE TYPE FOR SELECTED NODES");
        this.depthNeighborNodeTypeSelector.setFont(MyVars.tahomaPlainFont11);
        this.depthNeighborNodeTypeSelector.setFocusable(false);
        this.depthNeighborNodeTypeSelector.addActionListener(this);
        this.depthNeighborNodeTypeSelector.setVisible(false);

        this.depthSelecter.setBackground(Color.WHITE);
        this.depthSelecter.setFocusable(false);
        this.depthSelecter.setToolTipText("SELECT A DEPTH");
        this.depthSelecter.setFont(MyVars.tahomaPlainFont11);
        this.depthSelecter.addActionListener(this);
        MyViewerControlComponentUtil.setDepthValueSelecterMenu();

        JLabel nodeValueLabel = new JLabel("  N. V.:");
        nodeValueLabel.setToolTipText("NODE VALUE");
        nodeValueLabel.setBackground(Color.WHITE);
        nodeValueLabel.setFont(MyVars.tahomaPlainFont11);
        this.nodeValueSelecter.setBackground(Color.WHITE);
        this.nodeValueSelecter.setFont(MyVars.tahomaPlainFont11);
        this.nodeValueSelecter.setFocusable(false);
        MyViewerControlComponentUtil.setNodeValueComboBoxMenu();

        JLabel edgeValueLabel = new JLabel("   E. V.:");
        edgeValueLabel.setToolTipText("EDGE VALUE");
        edgeValueLabel.setBackground(Color.WHITE);
        edgeValueLabel.setFont(MyVars.tahomaPlainFont11);

        JLabel edgeLabelLabel = new JLabel("  E. L.:");
        edgeLabelLabel.setToolTipText("EDGE LABEL");
        edgeLabelLabel.setBackground(Color.WHITE);
        edgeLabelLabel.setFont(MyVars.tahomaPlainFont11);

        this.edgeValueSelecter.setBackground(Color.WHITE);
        this.edgeValueSelecter.setToolTipText("SELECT AN EDGE VALUE");
        this.edgeValueSelecter.setFocusable(false);
        this.edgeValueSelecter.setFont(MyVars.tahomaPlainFont11);
        MyViewerControlComponentUtil.setEdgeValueSelecterMenu();

        this.edgeLabelSelecter.setFocusable(false);
        this.edgeLabelSelecter.setToolTipText("SELECT AN EDGE LABEL");
        this.edgeLabelSelecter.setFont(MyVars.tahomaPlainFont11);
        this.edgeLabelSelecter.setBackground(Color.WHITE);
        this.edgeLabelSelecter.addItem("NONE");
        for (String edgeLabel : MyVars.edgeLabelSet) {this.edgeLabelSelecter.addItem(edgeLabel);}
        this.edgeLabelSelecter.addActionListener(this);

        JLabel nodeLabelLabel = new JLabel("   N. L.:");
        nodeLabelLabel.setToolTipText("NODE LABEL");
        nodeLabelLabel.setBackground(Color.WHITE);
        nodeLabelLabel.setFont(MyVars.tahomaPlainFont11);
        this.nodeLabelSelecter.setFocusable(false);
        this.nodeLabelSelecter.setToolTipText("SELECT A NODE LABEL");
        this.nodeLabelSelecter.setFont(MyVars.tahomaPlainFont11);
        this.nodeLabelSelecter.setBackground(Color.WHITE);
        this.nodeLabelSelecter.addItem("NONE");
        this.nodeLabelSelecter.addItem("NAME");
        for (String nodeLabel : MyVars.nodeLabelSet) {this.nodeLabelSelecter.addItem(nodeLabel);}
        this.nodeLabelSelecter.setSelectedIndex(1);
        this.nodeLabelSelecter.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                MyViewerControlComponentUtil.showNodeLabel();
            }
        });

        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.WHITE);
        topPanel.setLayout(new BorderLayout(5,5));

        this.setNodeValueExcludeSymbolComboBox();
        this.setEdgeValueExcludeSymbolComboBox();

        this.nodeValueExcludeTxt.setFont(MyVars.tahomaPlainFont11);
        this.nodeValueExcludeTxt.setToolTipText("ENTER A NUMERIC VALUE");
        this.nodeValueExcludeTxt.setHorizontalAlignment(JTextField.CENTER);
        this.nodeValueExcludeTxt.setBorder(new LineBorder(Color.DARK_GRAY,1));
        this.nodeValueExcludeTxt.setBackground(Color.WHITE);
        this.nodeValueExcludeTxt.setPreferredSize(new Dimension(60, 20));

        this.excludeBtn = new JButton("EXCL.");
        this.excludeBtn.setToolTipText("EXCLUDE NODES AND EDGES");
        this.excludeBtn.setBackground(Color.WHITE);
        this.excludeBtn.setFocusable(false);
        this.excludeBtn.setFont(MyVars.tahomaPlainFont11);
        this.excludeBtn.addActionListener(new MyNodeEdgeExclusionActionListener(this));

        this.topLeftPanel.setBackground(Color.WHITE);
        this.topLeftPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JLabel removeEdgeEmptyLabel = new JLabel("  ");
        removeEdgeEmptyLabel.setBackground(Color.WHITE);
        removeEdgeEmptyLabel.setPreferredSize(new Dimension(50, 20));

        this.nodeValueBarChart.setFocusable(false);
        this.nodeValueBarChart.setToolTipText("SHOW NODE VALUE BAR CHART");
        this.nodeValueBarChart.setBackground(Color.WHITE);
        this.nodeValueBarChart.setFont(MyVars.tahomaPlainFont11);
        this.nodeValueBarChart.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        if (nodeValueBarChart.isSelected()) {
                            MyViewerControlComponentUtil.removeBarChartsFromViewer();
                            MyViewerControlComponentUtil.removeSharedNodeValueBarCharts();
                            if (MyVars.getViewer().selectedNode != null) {
                                MyViewerControlComponentUtil.setSelectedNodeNeighborValueBarChartToViewer();
                            } else if (MyVars.getViewer().multiNodes != null) {
                                MyViewerControlComponentUtil.setSharedNodeLevelNodeValueBarChart();
                            } else if (MyVars.getViewer().vc.depthSelecter.getSelectedIndex() > 0 &&
                                MyVars.getViewer().vc.depthNeighborNodeTypeSelector.getSelectedIndex() == 0) {
                                MyViewerControlComponentUtil.setDepthNodeBarChartToViewer();
                            } else if (MyVars.getViewer().vc.depthSelecter.getSelectedIndex() > 0 &&
                                MyVars.getViewer().vc.depthNeighborNodeTypeSelector.getSelectedIndex() > 0) {
                                MyViewerControlComponentUtil.setDepthNeighborNodeBarChartToViewer();
                            } else {
                                MyViewerControlComponentUtil.setNodeBarChartToViewer();
                            }
                        } else {
                            MyViewerControlComponentUtil.removeBarChartsFromViewer();
                            MyViewerControlComponentUtil.removeSharedNodeValueBarCharts();
                        }
                        MyVars.getViewer().revalidate();
                        MyVars.getViewer().repaint();
                    }
                }).start();
            }
        });

        this.edgeValueBarChart.setFocusable(false);
        this.edgeValueBarChart.setToolTipText("SHOW EDGE VALUE BAR CHART");
        this.edgeValueBarChart.setBackground(Color.WHITE);
        this.edgeValueBarChart.setFont(MyVars.tahomaPlainFont11);
        this.edgeValueBarChart.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        if (edgeValueBarChart.isSelected()) {
                            if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex()  < 2) {
                                MyMessageUtil.showInfoMsg(MyVars.app, "Set an edge value, first.");
                                edgeValueBarChart.setSelected(false);
                                return;
                            }
                            if (edgeValueSelecter.getSelectedIndex() < 2) {
                                MyViewerControlComponentUtil.removeEdgeValueBarChartFromViewer();
                            } else if (MyVars.getViewer().multiNodes != null && MyVars.getViewer().multiNodes.size() > 0) {
                                MyViewerControlComponentUtil.setShareNodeLevelEdgeValueBarChartToViewer();
                            } else if (depthSelecter.getSelectedIndex() > 0) {
                                if (depthNeighborNodeTypeSelector.getSelectedIndex() > 0) {
                                    MyViewerControlComponentUtil.removeEdgeValueBarChartFromViewer();
                                    MyVars.getViewer().graphLelvelEdgeValueBarChart = new MyGraphLevelEdgeValueBarChart();
                                    MyVars.getViewer().graphLelvelEdgeValueBarChart.setEdgeValueBarChartForDepthNodes();
                                    MyVars.getViewer().add(MyVars.getViewer().graphLelvelEdgeValueBarChart);
                                }
                            } else if (MyVars.getViewer().selectedNode != null) {
                                MyViewerControlComponentUtil.setSingleNodeLevelEdgeBarChartToViewer();
                            } else {
                                MyViewerControlComponentUtil.setEdgeBarChartToViewer();
                            }
                        } else {
                            if (MyVars.getViewer().graphLelvelEdgeValueBarChart != null) {
                                MyVars.getViewer().remove(MyVars.getViewer().graphLelvelEdgeValueBarChart);
                            }
                        }
                        MyVars.getViewer().revalidate();
                        MyVars.getViewer().repaint();
                    }
                }).start();
            }
        });

        this.endingNodeBarChart.setFocusable(false);
        this.endingNodeBarChart.setToolTipText("SHOW ENDING NODE BAR CHART");
        this.endingNodeBarChart.setBackground(Color.WHITE);
        this.endingNodeBarChart.setFont(MyVars.tahomaPlainFont11);

        JLabel nodeValueExcludeOptionLabel = new JLabel("N. V.");
        nodeValueExcludeOptionLabel.setFont(MyVars.tahomaPlainFont11);
        nodeValueExcludeOptionLabel.setBackground(Color.WHITE);

        this.topLeftPanel.add(nodeValueExcludeOptionLabel);
        this.topLeftPanel.add(this.nodeValueExcludeSymbolSelecter);
        this.topLeftPanel.add(this.nodeValueExcludeTxt);

        JLabel edgeValueExludeLabel = new JLabel("  E. V.");
        edgeValueExludeLabel.setFont(MyVars.tahomaPlainFont11);
        edgeValueExludeLabel.setBackground(Color.WHITE);

        this.topLeftPanel.add(edgeValueExludeLabel);
        this.topLeftPanel.add(this.edgeValueExcludeSymbolSelecter);
        this.topLeftPanel.add(this.edgeValueExcludeTxt);

        JLabel nodeLabelExcludeComboBoxMenuLabel = new JLabel( "  N. L.");
        nodeLabelExcludeComboBoxMenuLabel.setFont(MyVars.tahomaPlainFont11);
        nodeLabelExcludeComboBoxMenuLabel.setBackground(Color.WHITE);

        this.nodeLabelExcludeSymbolSelecter.setBackground(Color.WHITE);
        this.nodeLabelExcludeSymbolSelecter.setFont(MyVars.tahomaPlainFont11);
        this.nodeLabelExcludeSymbolSelecter.setFocusable(false);
        this.nodeLabelExcludeSymbolSelecter.addItem("");
        this.nodeLabelExcludeSymbolSelecter.addItem("==");
        this.nodeLabelExcludeSymbolSelecter.addItem("!=");

        this.nodeLabelExcludeSelecter.setBackground(Color.WHITE);
        this.nodeLabelExcludeSelecter.setFont(MyVars.tahomaPlainFont11);
        this.nodeLabelExcludeSelecter.setFocusable(false);
        this.nodeLabelExcludeSelecter.addItem("");
        for (String nodeLabel : MyVars.nodeLabelSet) {
            this.nodeLabelExcludeSelecter.addItem(nodeLabel);
        }
        this.nodeLabelExcludeSymbolSelecter.addActionListener(new MyNodeLabelSelecterListener(this.nodeLabelExcludeSelecter, this.nodeLabelValueExcludeSelecter));

        this.nodeLabelValueExcludeSelecter.setFocusable(false);
        this.nodeLabelValueExcludeSelecter.setToolTipText("SELECT A NODE LABEL VALUE TO EXCLUDE");
        this.nodeLabelValueExcludeSelecter.setFont(MyVars.tahomaPlainFont11);
        this.nodeLabelValueExcludeSelecter.setBackground(Color.WHITE);

        this.edgeValueExcludeTxt.setFont(MyVars.tahomaPlainFont11);
        this.edgeValueExcludeTxt.setToolTipText("ENTER A NUMERIC VALUE");
        this.edgeValueExcludeTxt.setHorizontalAlignment(JTextField.CENTER);
        this.edgeValueExcludeTxt.setBorder(new LineBorder(Color.DARK_GRAY,1));
        this.edgeValueExcludeTxt.setBackground(Color.WHITE);
        this.edgeValueExcludeTxt.setPreferredSize(new Dimension(60, 20));

        if (MyVars.nodeLabelSet.size() > 0) {
            this.topLeftPanel.add(nodeLabelExcludeComboBoxMenuLabel);
            this.topLeftPanel.add(this.nodeLabelExcludeSymbolSelecter);
            this.topLeftPanel.add(this.nodeLabelExcludeSelecter);
            this.topLeftPanel.add(this.nodeLabelValueExcludeSelecter);
        }

        JLabel edgeLabelExcludeComboBoxMenuLabel = new JLabel("   E. L.");
        edgeLabelExcludeComboBoxMenuLabel.setBackground(Color.WHITE);
        edgeLabelExcludeComboBoxMenuLabel.setFont(MyVars.tahomaPlainFont11);

        this.edgeLabelExcludeSymbolSelecter.setBackground(Color.WHITE);
        this.edgeLabelExcludeSymbolSelecter.setFont(MyVars.tahomaPlainFont11);
        this.edgeLabelExcludeSymbolSelecter.setFocusable(false);
        this.edgeLabelExcludeSymbolSelecter.addItem("");
        this.edgeLabelExcludeSymbolSelecter.addItem("==");
        this.edgeLabelExcludeSymbolSelecter.addItem("!=");

        this.edgeLabelExcludeSelecter.setBackground(Color.WHITE);
        this.edgeLabelExcludeSelecter.setFont(MyVars.tahomaPlainFont11);
        this.edgeLabelExcludeSelecter.setFocusable(false);
        this.edgeLabelExcludeSelecter.addItem("");
        for (String nodeLabel : MyVars.edgeLabelSet) {
            this.edgeLabelExcludeSelecter.addItem(nodeLabel);
        }
        this.edgeLabelExcludeSelecter.addActionListener(new MyEdgeLabelSelecterListener(this.edgeLabelExcludeSelecter, this.edgeLabelValueExcludeSelecter));

        this.edgeLabelValueExcludeSelecter.setBackground(Color.WHITE);
        this.edgeLabelValueExcludeSelecter.setFont(MyVars.tahomaPlainFont11);
        this.edgeLabelValueExcludeSelecter.setFocusable(false);
        this.edgeLabelValueExcludeSelecter.addItem("");

        if (MyVars.edgeLabelSet.size() > 0) {
            this.topLeftPanel.add(edgeLabelExcludeComboBoxMenuLabel);
            this.topLeftPanel.add(this.edgeLabelExcludeSymbolSelecter);
            this.topLeftPanel.add(this.edgeLabelExcludeSelecter);
            this.topLeftPanel.add(this.edgeLabelValueExcludeSelecter);
        }
        this.topLeftPanel.add(this.excludeBtn);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setLayout(new BorderLayout(3,3));

        this.vTxtStat.setTextStatistics();

        graphGroupSelecterLabel.setBackground(Color.WHITE);
        graphGroupSelecterLabel.setFont(MyVars.tahomaPlainFont11);

        this.graphGroupSelecter.setBackground(Color.WHITE);
        this.graphGroupSelecter.setFocusable(false);
        this.graphGroupSelecter.setFont(MyVars.tahomaPlainFont11);
        this.graphGroupSelecter.addItem("");
        for (int i=0; i < MyVars.connectedComponentCountsByGraph.size(); i++) {
            this.graphGroupSelecter.addItem(MyMathUtil.getCommaSeperatedNumber(i+1));
        }
        this.graphGroupSelecter.addActionListener(this);

        this.bottomLeftControlPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3,3));
        this.bottomLeftControlPanel.setBackground(Color.WHITE);

        this.weightedNodeColor.setFocusable(false);
        this.weightedNodeColor.setToolTipText("SHOW WEIGHTED NODE COLORS");
        this.weightedNodeColor.setBackground(Color.WHITE);
        this.weightedNodeColor.setFont(MyVars.tahomaPlainFont11);
        this.weightedNodeColor.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                if (weightedNodeColor.isSelected()) {
                    MyVars.getViewer().getRenderContext().setVertexFillPaintTransformer(MyVars.getViewer().weightedNodeColor);
                } else {
                    MyVars.getViewer().getRenderContext().setVertexFillPaintTransformer(MyVars.getViewer().unWeightedNodeColor);
                }
                MyVars.getViewer().revalidate();
                MyVars.getViewer().repaint();
            }
        });

        JLabel emptyStatIndentLabel = new JLabel();
        emptyStatIndentLabel.setBackground(Color.WHITE);
        emptyStatIndentLabel.setFont(MyVars.tahomaPlainFont11);
        emptyStatIndentLabel.setText("        ");

        this.bottomLeftControlPanel.add(this.weightedNodeColor);
        this.bottomLeftControlPanel.add(this.nodeValueBarChart);
        this.bottomLeftControlPanel.add(this.edgeValueBarChart);
        //this.bottomLeftControlPanel.add(this.endingNodeBarChart);
        this.bottomLeftControlPanel.add(emptyStatIndentLabel);
        this.bottomLeftControlPanel.add(this.vTxtStat);

        JPanel bottomRightPanel = new JPanel();
        bottomRightPanel.setBackground(Color.WHITE);
        bottomRightPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3,3));
        bottomRightPanel.add(this.depthSelecter);
        bottomRightPanel.add(this.depthNeighborNodeTypeSelector);
        bottomRightPanel.add(this.selectedNodeNeighborNodeTypeSelector);
        bottomRightPanel.add(nodeValueLabel);
        bottomRightPanel.add(this.nodeValueSelecter);
        bottomRightPanel.add(nodeLabelLabel);
        bottomRightPanel.add(this.nodeLabelSelecter);
        bottomRightPanel.add(edgeValueLabel);
        bottomRightPanel.add(this.edgeValueSelecter);
        if (MyVars.edgeLabelSet.size() > 0) {
            bottomRightPanel.add(edgeLabelLabel);
            bottomRightPanel.add(this.edgeLabelSelecter);
        }
        bottomPanel.add(this.bottomLeftControlPanel, BorderLayout.WEST);
        bottomPanel.add(bottomRightPanel, BorderLayout.CENTER);

        JPanel topRightPanel = new JPanel();
        topRightPanel.setBackground(Color.WHITE);
        topRightPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 3));

        JLabel graphGroupNodeNumberPercentLabel = new JLabel();
        graphGroupNodeNumberPercentLabel.setBackground(Color.WHITE);
        graphGroupNodeNumberPercentLabel.setFont(MyVars.tahomaPlainFont11);
        graphGroupNodeNumberPercentLabel.setText("0[0%]");
        topRightPanel.add(graphGroupNodeNumberPercentLabel);
        topRightPanel.add(graphGroupSelecterLabel);
        topRightPanel.add(graphGroupSelecter);

        //if (MyVars.connectedComponentCountsByGraph.size() > 1) {
        topPanel.add(topRightPanel, BorderLayout.EAST);
        //}
        topPanel.add(this.topLeftPanel, BorderLayout.WEST);

        this.tabbedPane.setFocusable(false);
        this.tabbedPane.setOpaque(false);
        this.tabbedPane.setPreferredSize(new Dimension(150, 1000));
        this.tabbedPane.setFont(MyVars.tahomaPlainFont10);

        this.tabbedPane.addTab("N.", null, setNodeTable(), "NODES");
        this.tabbedPane.addTab("E.", null, setEdgeTable(), "EDGES");
        this.tabbedPane.addTab("S.", null, setStatTable(), "GRAPH STATISTICS");
        this.tabbedPane.addTab("P.", null, setPathFindTable(), "PATH ANALYSIS BETWEEN TWO NODES");

        this.graphTableSplitPane.setDividerSize(4);
        this.graphTableSplitPane.setLeftComponent(tabbedPane);
        this.graphTableSplitPane.setRightComponent(graphPanel);
        this.graphTableSplitPane.setDividerLocation(.43);
        this.graphTableSplitPane.setMinimumSize(new Dimension(160, 500));
        this.graphTableSplitPane.addComponentListener(new ComponentAdapter() {
            @Override
            public synchronized void componentResized(ComponentEvent e) {
                super.componentResized(e);
                if (MyVars.getViewer().vc.nodeValueBarChart.isSelected()) {
                    MyViewerControlComponentUtil.removeBarChartsFromViewer();
                    MyViewerControlComponentUtil.removeSharedNodeValueBarCharts();
                    if (MyVars.getViewer().selectedNode != null) {
                        MyViewerControlComponentUtil.setSelectedNodeNeighborValueBarChartToViewer();
                    } else if (MyVars.getViewer().multiNodes != null) {
                        MyViewerControlComponentUtil.setSharedNodeLevelNodeValueBarChart();
                    } else {
                        MyVars.getViewer().graphLevelNodeValueBarChart = new MyGraphLevelNodeValueBarChart();
                        MyVars.getViewer().add(MyVars.getViewer().graphLevelNodeValueBarChart);
                    }
                } else {
                    MyViewerControlComponentUtil.removeBarChartsFromViewer();
                    MyViewerControlComponentUtil.removeSharedNodeValueBarCharts();
                }
                MyVars.getViewer().revalidate();
                MyVars.getViewer().repaint();
            }
        });

        this.add(topPanel, BorderLayout.NORTH);
        this.add(this.graphTableSplitPane, BorderLayout.CENTER);
        this.add(bottomPanel, BorderLayout.SOUTH);
    }

    public JPanel setStatTable() {
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout(3,3));
        tablePanel.setBackground(Color.WHITE);

        String [] statTableColumns = {"PROPERTY.", "V."};
        String [][] statTableData = {};
        DefaultTableModel statTableModel = new DefaultTableModel(statTableData, statTableColumns);

        String [] tablePropertyTooltips = {
            '\u03B5' + "(G)",
            "CONNECTANCE",
            "NODES",
            "ISOLATED NODES",
            "EDGES",
            "DIAMETER",
            "AVERAGE PATH LENGTH",
            "AVERAGE UNREACHABLE NODES",
            "TOTAL UNRECHABLE NODES",
            "RED NODES",
            "BLUE NODES",
            "GREEN NODES",
            "AVG. NODE VALUE",
            "MAX. NODE VALUE",
            "MIN. NODE VALUE",
            "NODE VALUE STANDARD DEVIATION",
            "AVG. IN-NODES",
            "AVG. OUT-NODES",
            "AVG. IN-CONT.",
            "AVG. OUT-CONT.",
            "AVG. UNIQUE NODE CONT.",
            "MAX. UNIQUE NODE CONT.",
            "MIN. UNIQUE NODE CONT.",
            "UNIQUE NODE CONT. STANDARD DEVIATION",
            "AVG. UNIQUE EDGE CONT.",
            "MAX. UNIQUE EDGE CONT.",
            "MIN. UNIQUE EDGE CONT.",
            "STD. UNIQUE EDGE CONT.",
            "AVG. EDGE VALUE",
            "MAX. EDGE VALUE",
            "MIN. EDGE VALUE",
            "EDGE VALUE STANDARD DEVIATION"
        };

        this.statTable = new JTable(statTableModel) {
            public String getToolTipText(MouseEvent e) {
                String tip = null;
                java.awt.Point p = e.getPoint();
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

        this.statTable.setRowHeight(18);
        this.statTable.setBackground(Color.WHITE);
        this.statTable.setFont(MyVars.tahomaPlainFont9);
        this.statTable.setFocusable(false);
        this.statTable.getTableHeader().setFont(MyVars.tahomaBoldFont10);
        this.statTable.getTableHeader().setOpaque(false);
        this.statTable.getTableHeader().setBackground(new Color(0,0,0,0f));
        this.statTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        this.statTable.setPreferredSize(new Dimension(145, 800));
        this.statTable.setForeground(Color.BLACK);
        this.statTable.getColumnModel().getColumn(0).setPreferredWidth(55);
        this.statTable.getColumnModel().getColumn(0).setMaxWidth(110);
        this.statTable.getColumnModel().getColumn(1).setPreferredWidth(50);
        this.statTable.getColumnModel().getColumn(1).setMaxWidth(70);
        this.statTable.setSelectionForeground(Color.BLACK);
        this.statTable.setSelectionBackground(Color.LIGHT_GRAY);
        MyViewerControlComponentUtil.setGraphLevelTableStatistics();

        JScrollPane graphStatTableScrollPane = new JScrollPane(this.statTable);
        graphStatTableScrollPane.setOpaque(false);
        graphStatTableScrollPane.setPreferredSize(new Dimension(150, 805));
        graphStatTableScrollPane.setBackground(new Color(0,0,0,0f));
        graphStatTableScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(6, 0));
        graphStatTableScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 6));

        tablePanel.add(graphStatTableScrollPane, BorderLayout.CENTER);
        return tablePanel;
    }

    public JPanel setEdgeTable() {
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout(3,3));
        tablePanel.setBackground(Color.WHITE);

        JPanel bottomTablePanel = new JPanel();
        bottomTablePanel.setLayout(new BorderLayout(3,3));
        bottomTablePanel.setBackground(Color.WHITE);

        String [] bottomTableColumns = {"S.", "D.", "V."};
        String [][] bottomTableData = {};
        DefaultTableModel bottomTableModel = new DefaultTableModel(bottomTableData, bottomTableColumns);
        this.edgeTable = new JTable(bottomTableModel) {
            public String getToolTipText(MouseEvent e) {
                String tip = null;
                java.awt.Point p = e.getPoint();
                int rowIndex = rowAtPoint(p);
                int colIndex = columnAtPoint(p);
                try {
                    tip = getValueAt(rowIndex, colIndex).toString();
                } catch (RuntimeException e1) {}
                return tip;
            }
        };

        String [] toolTips = {"SOURCE", "DEST", "EDGE VALUE"};
        MyTableToolTipper tooltipHeader = new MyTableToolTipper(this.edgeTable.getColumnModel());
        tooltipHeader.setToolTipStrings(toolTips);
        this.edgeTable.setTableHeader(tooltipHeader);

        Collection<MyEdge> edges = MyVars.g.getEdges();
        LinkedHashMap<String, Float> sortedEdges = new LinkedHashMap<>();
        for (MyEdge e : edges) {
            String ename = e.getSource().getName() + "-" + e.getDest().getName();
            sortedEdges.put(ename, (float)e.getContribution());
        }
        sortedEdges = MySysUtil.sortMapByFloatValue(sortedEdges);
        int i=-0;
        for (String e : sortedEdges.keySet()) {
            String source = MySysUtil.getDecodedNodeName(e.split("-")[0]);
            String dest = MySysUtil.getDecodedNodeName(e.split("-")[1]);
            bottomTableModel.addRow(
                    new String[]{
                            source,
                            dest,
                            MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(sortedEdges.get(e)))
                    });
        }

        edgeTable.setRowHeight(19);
        edgeTable.setBackground(Color.WHITE);
        edgeTable.setFont(MyVars.f_pln_9);
        edgeTable.getTableHeader().setFont(MyVars.tahomaBoldFont10);
        edgeTable.getTableHeader().setOpaque(false);
        edgeTable.getTableHeader().setBackground(new Color(0,0,0,0f));
        edgeTable.getColumnModel().getColumn(0).setPreferredWidth(60);
        edgeTable.getColumnModel().getColumn(1).setPreferredWidth(60);
        edgeTable.getColumnModel().getColumn(2).setPreferredWidth(30);

        JTextField edgeTableNodeTableNodeSearchTxt = new JTextField();
        JButton edgeTableNodeSelectBtn = new JButton("SEL.");
        edgeTableNodeSelectBtn.setFont(MyVars.tahomaPlainFont10);
        edgeTableNodeSelectBtn.setFocusable(false);
        edgeTableNodeTableNodeSearchTxt.setBackground(Color.WHITE);
        edgeTableNodeTableNodeSearchTxt.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        JPanel bottomTableSearchAndSavePanel = MyTableUtil.searchAndSaveDataPanelForJTable2(this, edgeTableNodeTableNodeSearchTxt, edgeTableNodeSelectBtn, bottomTableModel, edgeTable);
        edgeTableNodeTableNodeSearchTxt.setFont(MyVars.f_bold_10);
        edgeTableNodeTableNodeSearchTxt.setToolTipText("TYPE A NODE NAME TO SEARCH");
        edgeTableNodeTableNodeSearchTxt.setPreferredSize(new Dimension(90, 19));
        edgeTableNodeSelectBtn.setPreferredSize(new Dimension(50, 19));
        bottomTableSearchAndSavePanel.remove(edgeTableNodeSelectBtn);

        edgeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        edgeTable.setSelectionBackground(Color.LIGHT_GRAY);
        edgeTable.setForeground(Color.BLACK);
        edgeTable.setSelectionForeground(Color.BLACK);
        edgeTable.setFocusable(false);
        edgeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        edgeTable.setRowSorter(MyTableUtil.setJTableRowSorterWithTextField(bottomTableModel, edgeTableNodeTableNodeSearchTxt));
        edgeTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        try {
                            MyNode s =  (MyNode) MyVars.g.vRefs.get(MyVars.nodeNameMap.get(edgeTable.getValueAt(edgeTable.getSelectedRow(), 0)));
                            MyNode d = (MyNode) MyVars.g.vRefs.get(MyVars.nodeNameMap.get(edgeTable.getValueAt(edgeTable.getSelectedRow(), 1)));
                            MyNodeSearcher nodeSearcher = new MyNodeSearcher();
                            nodeSearcher.setEdgeNodes(s, d);
                        } catch (Exception ex) {}
                    }
                }).start();
            }
        });

        JScrollPane edgeTableScrollPane = new JScrollPane(edgeTable);
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
        tablePanel.setLayout(new GridLayout(2,1));
        tablePanel.setBackground(Color.WHITE);

        JPanel bottomTablePanel = new JPanel();
        bottomTablePanel.setLayout(new BorderLayout(3,3));
        bottomTablePanel.setBackground(Color.WHITE);

        String [] bottomTableColumns = {"NO.", "N.", "V."};
        String [][] bottomTableData = {};

        DefaultTableModel bottomTableModel = new DefaultTableModel(bottomTableData, bottomTableColumns);
        this.nodeListTable = new JTable(bottomTableModel) {
            public String getToolTipText(MouseEvent e) {
                String tip = null;
                java.awt.Point p = e.getPoint();
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

        Collection<MyNode> bottomTableNodes = MyVars.g.getVertices();
        LinkedHashMap<String, Float> bottomTableSortedNodes = new LinkedHashMap<>();
        for (MyNode n : bottomTableNodes) {
            bottomTableSortedNodes.put(n.getName(), n.getCurrentValue());
        }
        bottomTableSortedNodes = MySysUtil.sortMapByFloatValue(bottomTableSortedNodes);
        int i=-0;
        for (String n : bottomTableSortedNodes.keySet()) {
            bottomTableModel.addRow(
                new String[]{
                    String.valueOf(++i),
                    MySysUtil.getDecodedNodeName(n),
                    MySysUtil.formatAverageValue(MyMathUtil.oneDecimalFormat(bottomTableSortedNodes.get(n)))
            });
        }

        nodeListTable.setRowHeight(18);
        nodeListTable.setBackground(Color.WHITE);
        nodeListTable.setFont(MyVars.f_pln_9);
        nodeListTable.getTableHeader().setFont(MyVars.tahomaBoldFont10);
        nodeListTable.getTableHeader().setOpaque(false);
        nodeListTable.getTableHeader().setBackground(new Color(0,0,0,0f));
        nodeListTable.getColumnModel().getColumn(0).setPreferredWidth(35);
        nodeListTable.getColumnModel().getColumn(0).setMaxWidth(45);
        nodeListTable.getColumnModel().getColumn(1).setPreferredWidth(80);
        nodeListTable.getColumnModel().getColumn(2).setPreferredWidth(35);
        nodeListTable.getColumnModel().getColumn(2).setMaxWidth(50);

        JTextField bottomTableNodeTableNodeSearchTxt = new JTextField();
        JButton bottomTableNodeSelectBtn = new JButton("SEL.");
        bottomTableNodeSelectBtn.setFont(MyVars.tahomaPlainFont10);
        bottomTableNodeSelectBtn.setFocusable(false);
        bottomTableNodeTableNodeSearchTxt.setBackground(Color.WHITE);
        bottomTableNodeTableNodeSearchTxt.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        JPanel bottomTableSearchAndSavePanel = MyTableUtil.searchAndSaveDataPanelForJTable2(this, bottomTableNodeTableNodeSearchTxt, bottomTableNodeSelectBtn, bottomTableModel, nodeListTable);
        bottomTableNodeTableNodeSearchTxt.setFont(MyVars.f_bold_9);
        bottomTableNodeTableNodeSearchTxt.setToolTipText("TYPE A NODE NAME TO SEARCH");
        bottomTableNodeTableNodeSearchTxt.setPreferredSize(new Dimension(90, 19));
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
        nodeListTable.setRowSorter(MyTableUtil.setJTableRowSorterWithTextField(bottomTableModel, bottomTableNodeTableNodeSearchTxt));
        nodeListTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                new Thread(new Runnable() {
                    @Override public void run() {
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
                            MyVars.getViewer().revalidate();
                            MyVars.getViewer().repaint();
                            if (MyVars.getViewer().vc.depthSelecter.getSelectedIndex() > 0) return;
                            MyNode n = (MyNode) MyVars.g.vRefs.get(MyVars.nodeNameMap.get(nodeListTable.getValueAt(nodeListTable.getSelectedRow(), 1)));
                            MyNodeSearcher nodeSearcher = new MyNodeSearcher();
                            nodeSearcher.setSelectedNodes(n);
                        } catch (Exception ex) {}
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

        String [] topTableColumns = {"NO.", "CUR. N.", "V."};
        String [][] topTableData = {};
        DefaultTableModel topTableModel = new DefaultTableModel(topTableData, topTableColumns);
        currentNodeListTable = new JTable(topTableModel) {
            //Implement table cell tool tips.
            public String getToolTipText(MouseEvent e) {
                String tip = null;
                java.awt.Point p = e.getPoint();
                int rowIndex = rowAtPoint(p);
                int colIndex = columnAtPoint(p);
                try {
                    tip = getValueAt(rowIndex, colIndex).toString();
                } catch (RuntimeException e1) {}
                return tip;
            }
        };

        LinkedHashMap<String, Long> nodeMap = new LinkedHashMap<>();
        Collection<MyNode> nodes = MyVars.g.getVertices();
        for (MyNode n : nodes) {
            nodeMap.put(n.getName(), (long) n.getCurrentValue());
        }
        nodeMap = MySysUtil.sortMapByLongValue(nodeMap);
        i = 0;
        for (String n : nodeMap.keySet()) {
            MyNode nn = (MyNode) MyVars.g.vRefs.get(n);
            if (nn.getCurrentValue() > 0) {
                ((DefaultTableModel) currentNodeListTable.getModel()).addRow(new String[]{
                        "" + (++i),
                        MySysUtil.getDecodedNodeName(nn.getName()),
                        MyMathUtil.getCommaSeperatedNumber((long) nn.getCurrentValue())}
                );
            }
        }

        String [] currentNodeTableHeaderTooltips = {"NO.", "CURRENT NODE", "CURRENT NODE VALUE"};
        MyTableToolTipper currentNodeTooltipHeader = new MyTableToolTipper(this.currentNodeListTable.getColumnModel());
        currentNodeTooltipHeader.setToolTipStrings(currentNodeTableHeaderTooltips);
        this.currentNodeListTable.setTableHeader(currentNodeTooltipHeader);

        currentNodeListTable.setRowHeight(18);
        currentNodeListTable.setBackground(Color.WHITE);
        currentNodeListTable.setFont(MyVars.f_pln_9);
        currentNodeListTable.getTableHeader().setFont(MyVars.tahomaBoldFont10);
        currentNodeListTable.getTableHeader().setOpaque(false);
        currentNodeListTable.getTableHeader().setBackground(new Color(0,0,0,0f));
        currentNodeListTable.getColumnModel().getColumn(0).setPreferredWidth(35);
        currentNodeListTable.getColumnModel().getColumn(0).setMaxWidth(45);
        currentNodeListTable.getColumnModel().getColumn(1).setPreferredWidth(80);
        currentNodeListTable.getColumnModel().getColumn(2).setPreferredWidth(35);
        currentNodeListTable.getColumnModel().getColumn(2).setMaxWidth(50);

        JTextField topTableNodeSearchTxt = new JTextField();
        JButton topTableNodeSelectBtn = new JButton("SEL.");
        topTableNodeSelectBtn.setFont(MyVars.tahomaPlainFont10);
        topTableNodeSelectBtn.setFocusable(false);
        topTableNodeSearchTxt.setBackground(Color.WHITE);
        topTableNodeSearchTxt.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        JPanel topTableSearchAndSavePanel = MyTableUtil.searchAndSaveDataPanelForJTable2(this, topTableNodeSearchTxt, topTableNodeSelectBtn, topTableModel, currentNodeListTable);
        topTableNodeSearchTxt.setFont(MyVars.f_bold_9);
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
        return tablePanel;
    }

    @Override public void actionPerformed(ActionEvent ae) {
      if (ae.getSource() == depthNeighborNodeTypeSelector) {
            new Thread(new Runnable() {
                @Override public void run() {
                    MyProgressBar pb = new MyProgressBar(false);
                    MyViewerControlComponentUtil.setViewerComponentDefaultValues();
                    pb.updateValue(40, 100);
                    MyDepthNodeUtil.setDepthNodeNeighbors();
                    pb.updateValue(70, 100);
                    MyVars.app.getDashboard().setDepthNodeDashBoard();
                    pb.updateValue(85, 100);
                    updateNodeTable();
                    MyViewerControlComponentUtil.adjustDepthNodeValueSelections();
                    MyVars.getViewer().revalidate();
                    MyVars.getViewer().repaint();
                    pb.updateValue(100, 100);
                    pb.dispose();
                }
            }).start();
        } else if (ae.getSource() == selectedNodeNeighborNodeTypeSelector) {
            new Thread(new Runnable() {
                @Override public void run() {
                    MyProgressBar pb = new MyProgressBar(false);
                    MyViewerControlComponentUtil.setExcludeSymbolOption();
                    pb.updateValue(30, 100);
                    MyDepthNodeUtil.setSelectedSingleDepthNodeNeighbors();
                    pb.updateValue(65, 100);
                    MyViewerControlComponentUtil.setBottomCharts();
                    pb.updateValue(95, 100);
                    vTxtStat.setTextStatistics();
                    updateTableInfos();
                    MyViewerControlComponentUtil.adjustDepthNodeValueSelections();
                    pb.updateValue(100, 100);
                    pb.dispose();
                }
            }).start();
        } else if (ae.getSource() == nodeValueSelecter) {
            new Thread(new Runnable() {
                @Override public void run() {
                    MyProgressBar pb = new MyProgressBar(false);
                    pb.updateValue(10, 100);
                    if (selectedNodeNeighborNodeTypeSelector.isShowing()) {
                        pb.updateValue(100, 100);
                        pb.dispose();
                        return;
                    }
                    if (MyVars.currentGraphDepth > 0) {
                        MyDepthNodeUtil.setDepthNodeValue();
                    } else {MyNodeUtil.setNodeValue();}
                    pb.updateValue(75, 100);
                    MyViewerControlComponentUtil.setBottomCharts();
                    pb.updateValue(85, 100);
                    MyViewerControlComponentUtil.updateDepthCharts();
                    pb.updateValue(95, 100);
                    vTxtStat.setTextStatistics();
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
                        return;
                    }
                    MyVars.getViewer().edgeValName = edgeValueSelecter.getSelectedItem().toString().replaceAll(" ", "");
                    MyProgressBar pb = new MyProgressBar(false);
                    pb.updateValue(10, 100);
                    MyEdgeUtil.setEdgeValue();
                    pb.updateValue(50, 100);
                    MyViewerControlComponentUtil.updateDepthCharts();
                    pb.updateValue(85, 100);
                    MyViewerControlComponentUtil.setBottomCharts();
                    pb.updateValue(95, 100);
                    vTxtStat.setTextStatistics();
                    updateNodeTable();
                    pb.updateValue(100, 100);
                    pb.dispose();
                    edgeValueSelecter.setToolTipText("EDGE VALUE: " + edgeValueSelecter.getSelectedItem().toString());
                }
            }).start();
        } else if (ae.getSource() == depthSelecter) {
            new Thread(new Runnable() { // When depth option selected, edge values and edge label components are disabled.
                @Override public void run() {
                    if (depthSelecter.getSelectedIndex() == 0) {
                        MyViewerControlComponentUtil.setDefaultLookToViewer();
                        MyVars.getViewer().revalidate();
                        MyVars.getViewer().repaint();
                        return;
                    } else if (MyVars.getViewer().selectedNode != null) {
                        if (depthSelecter.getSelectedIndex() == 0) {
                            MyViewerControlComponentUtil.setDefaultLookToViewer();
                        } else {
                            nodeValueBarChart.setText("DEPTH N. V. B.");
                            MyVars.currentGraphDepth = Integer.parseInt(depthSelecter.getSelectedItem().toString().trim());
                            MyViewerControlComponentUtil.removeBarChartsFromViewer();
                            MyViewerControlComponentUtil.removeEdgeValueBarChartFromViewer();
                            depthNodeNameSet = new HashSet<>();
                            depthNodeNameSet.add(MyVars.getViewer().selectedNode.getName());
                            selectedNodeNeighborNodeTypeSelector.setVisible(true);
                            Collection<MyNode> nodes = MyVars.g.getVertices();
                            for (MyNode n : nodes) {
                                if (!depthNodeNameSet.contains(n.getName())) {
                                    n.setCurrentValue(0f);
                                }
                            }
                            MyViewerControlComponentUtil.setSelectedNodeNeighborNodeTypeOption();
                            MyViewerControlComponentUtil.setBottomCharts();
                            updateNodeTable();
                            MyVars.getViewer().selectedNode = null;
                        }
                    } else if (selectedNodeNeighborNodeTypeSelector.isShowing()) {
                        nodeValueBarChart.setText("DEPTH N. V. B.");
                        MyVars.currentGraphDepth = Integer.parseInt(depthSelecter.getSelectedItem().toString().trim());
                        Collection<MyNode> nodes = MyVars.g.getVertices();
                        for (MyNode n : nodes) {
                            if (!depthNodeNameSet.contains(n.getName())) {
                                n.setCurrentValue(0f);
                            }
                        }
                        MyViewerControlComponentUtil.setSelectedNodeNeighborNodeTypeOption();
                        MyViewerControlComponentUtil.setSelectedNodeVisibleOnly();
                        MyViewerControlComponentUtil.setBottomCharts();
                        updateNodeTable();
                    } else {
                        MyProgressBar pb = new MyProgressBar(false);
                        nodeValueBarChart.setText("DEPTH N. V. B.");
                        MyVars.currentGraphDepth = Integer.parseInt(depthSelecter.getSelectedItem().toString().trim());
                        MyViewerControlComponentUtil.setDepthNodeNameSet();
                        pb.updateValue(10, 100);
                        MyViewerControlComponentUtil.setDepthNodeNeighborNodeTypeOption();
                        pb.updateValue(15, 100);
                        MyDepthNodeUtil.setDepthNodeValue();
                        pb.updateValue(40, 100);
                        MyVars.app.getDashboard().setDepthNodeDashBoard();
                        pb.updateValue(80, 100);
                        vTxtStat.setTextStatistics();
                        updateNodeTable();
                        pb.updateValue(100, 100);
                        pb.dispose();
                    }
                    MyViewerControlComponentUtil.adjustDepthNodeValueSelections();
                    MyVars.getViewer().revalidate();
                    MyVars.getViewer().repaint();
                }
            }).start();
        }  else if (ae.getSource() == graphGroupSelecter) {
          new Thread(new Runnable() {
              @Override public void run() {
                  if (graphGroupSelecter.getSelectedIndex() > 0) {
                      Collection<MyNode> nodes = MyVars.g.getVertices();
                      for (MyNode n : nodes) {
                          if (!MyVars.connectedComponentCountsByGraph.get(graphGroupSelecter.getSelectedIndex()-1).contains(n)) {
                              n.setCurrentValue(0f);
                          }
                      }
                      String numOfNodes = MyMathUtil.getCommaSeperatedNumber(MyVars.connectedComponentCountsByGraph.get(graphGroupSelecter.getSelectedIndex()-1).size());
                      String percentOfNodes = MyMathUtil.twoDecimalFormat(((double)MyVars.connectedComponentCountsByGraph.get(graphGroupSelecter.getSelectedIndex()-1).size()/MyVars.g.getVertexCount())*100);
                      graphGroupSelecterLabel.setText(numOfNodes + "[" + percentOfNodes + "%]");

                      updateTableInfos();
                      nodeValueBarChart.setSelected(false);
                      edgeValueBarChart.setSelected(false);
                      vTxtStat.setTextStatistics();
                      MyVars.app.getDashboard().setDashboard();

                      MyVars.getViewer().revalidate();
                      MyVars.getViewer().repaint();
                  } else if (graphGroupSelecter.getSelectedIndex() == 0) {
                      if (depthSelecter.getSelectedIndex() == 0) {
                          MyViewerControlComponentUtil.setDefaultLookToViewer();
                          graphGroupSelecterLabel.setText("[0%]");
                      }
                  }
             }
          }).start();
      }
    }

    public void updateTableInfos() {
        this.updateNodeTable();
        this.updateEdgeTable();
        this.updateStatTable();
    }

    private void updateNodeTable() {
        for (int i = currentNodeListTable.getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) currentNodeListTable.getModel()).removeRow(i);
        }
        
        for (int i= nodeListTable.getRowCount()-1; i>= 0; i--) {
            ((DefaultTableModel) nodeListTable.getModel()).removeRow(i);
        }

        if (depthSelecter.getSelectedIndex() > 0 && depthNeighborNodeTypeSelector.getSelectedIndex() > 0 && depthNeighborNodeTypeSelector.getSelectedItem().toString().equals("P.")) {
            if (depthNodeNameSet != null && depthNodeNameSet.size() == 1) {
                int tableRowCount = 0;
                LinkedHashMap<String, Long> predecessorMap = new LinkedHashMap<>();
                Collection<MyNode> predecessors = MyVars.g.getPredecessors(MyVars.getViewer().selectedNode);
                for (MyNode n : predecessors) {
                    predecessorMap.put(n.getName(), n.getContribution());
                }
                predecessorMap = MySysUtil.sortMapByLongValue(predecessorMap);

                for (String predecessor : predecessorMap.keySet()) {
                    ((DefaultTableModel) currentNodeListTable.getModel()).addRow(
                        new String[]{"" + MyMathUtil.getCommaSeperatedNumber(++tableRowCount),
                            MySysUtil.getDecodedNodeName(predecessor),
                            MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(predecessorMap.get(predecessor)))
                        }
                    );
                }

                tableRowCount = 0;
                for (String depthNode : depthNodeNameSet) {
                    ((DefaultTableModel) nodeListTable.getModel()).addRow(
                        new String[]{"" + MyMathUtil.getCommaSeperatedNumber(++tableRowCount),
                            MySysUtil.getDecodedNodeName(depthNode),
                            MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(((MyNode) MyVars.g.vRefs.get(depthNode)).getInContribution()))
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
                            MySysUtil.getDecodedNodeName(predecessor),
                            MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(uniquePredecessorMap.get(predecessor)))
                        }
                    );
                }

                tableRowCount = 0;
                for (String depthNode : depthNodeSet) {
                    ((DefaultTableModel) nodeListTable.getModel()).addRow(
                        new String[]{"" + MyMathUtil.getCommaSeperatedNumber(++tableRowCount),
                            MySysUtil.getDecodedNodeName(depthNode),
                            MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(((MyNode) MyVars.g.vRefs.get(depthNode)).getInContribution()))
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
                            MySysUtil.getDecodedNodeName(successor),
                            MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat((double)successorMap.get(successor)))
                        }
                    );
                }
                MyVars.getViewer().revalidate();
                MyVars.getViewer().repaint();
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
                            MySysUtil.getDecodedNodeName(successor),
                            MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(uniqueSuccessorMap.get(successor)))
                        }
                    );
                }

                for (String depthNode : depthNodeSet) {
                    ((DefaultTableModel) nodeListTable.getModel()).addRow(
                        new String [] {"" + MyMathUtil.getCommaSeperatedNumber(++tableRowCount),
                            MySysUtil.getDecodedNodeName(depthNode),
                            MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(((MyNode) MyVars.g.vRefs.get(depthNode)).getOutContribution()))
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
                                    MySysUtil.getDecodedNodeName(successor),
                                    MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat((double)successorMap.get(successor)))
                            }
                    );
                }

                MyNode n = (MyNode) MyVars.g.vRefs.get(selectedSingleNode);
                ((DefaultTableModel) nodeListTable.getModel()).addRow(
                            new String [] {"" + MyMathUtil.getCommaSeperatedNumber(++tableRowCount),
                                    MySysUtil.getDecodedNodeName(selectedSingleNode),
                                    MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(n.getNodeDepthInfo(MyVars.currentGraphDepth).getContribution()))
                            });

                MyVars.getViewer().revalidate();
                MyVars.getViewer().repaint();
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
                                    MySysUtil.getDecodedNodeName(successor),
                                    MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(uniqueSuccessorMap.get(successor)))
                            }
                    );
                }

                for (String depthNode : depthNodeSet) {
                    ((DefaultTableModel) nodeListTable.getModel()).addRow(
                            new String [] {"" + MyMathUtil.getCommaSeperatedNumber(++tableRowCount),
                                    MySysUtil.getDecodedNodeName(depthNode),
                                    MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(((MyNode) MyVars.g.vRefs.get(depthNode)).getOutContribution()))
                            }
                    );
                }
            }
        } else if (depthSelecter.getSelectedIndex() > 0) {
            if (depthNodeNameSet != null && depthNodeNameSet.size() == 1) {
                int tableRowCount = 0;
                MyNode selectedSingleNode = (MyNode) MyVars.g.vRefs.get(depthNodeNameSet.iterator().next());
                ((DefaultTableModel) nodeListTable.getModel()).addRow(
                    new String [] {"" + MyMathUtil.getCommaSeperatedNumber(++tableRowCount),
                        MySysUtil.getDecodedNodeName(selectedSingleNode.getName()),
                        MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(selectedSingleNode.getContribution()))
                    }
                );

                tableRowCount = 0;
                ((DefaultTableModel) currentNodeListTable.getModel()).addRow(
                        new String [] {"" + MyMathUtil.getCommaSeperatedNumber(++tableRowCount),
                            MySysUtil.getDecodedNodeName(selectedSingleNode.getName()),
                            MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(selectedSingleNode.getContribution()))
                        });

            } else if (depthNodeNameSet != null && MyVars.getViewer().selectedNode != null) {
                int tableRowCount = 0;
                for (String n : depthNodeNameSet) {
                    ((DefaultTableModel) nodeListTable.getModel()).addRow(
                            new String[]{"" + MyMathUtil.getCommaSeperatedNumber(++tableRowCount),
                                    MySysUtil.getDecodedNodeName(n),
                                    MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(((MyNode) MyVars.g.vRefs.get(n)).getContribution()))
                            }
                    );
                }

                LinkedHashMap<String, Long> nodeMap = new LinkedHashMap<>();
                Set<MyEdge> edges = new HashSet<>(MyVars.g.getInEdges(MyVars.getViewer().selectedNode));
                edges.addAll(MyVars.g.getOutEdges(MyVars.getViewer().selectedNode));
                if (edges != null) {
                    for (MyEdge e : edges) {
                        if (e.getDest() == MyVars.getViewer().selectedNode) {
                            nodeMap.put(e.getDest().getName(), (long)e.getContribution());
                        } else if (e.getSource() == MyVars.getViewer().selectedNode) {
                            nodeMap.put(e.getSource().getName(), (long)e.getContribution());
                        }
                    }
                }

                nodeMap = MySysUtil.sortMapByLongValue(nodeMap);
                for (String n : nodeMap.keySet()) {
                    ((DefaultTableModel) currentNodeListTable.getModel()).addRow(
                            new String [] {"" + MyMathUtil.getCommaSeperatedNumber(++tableRowCount),
                                    MySysUtil.getDecodedNodeName(n),
                                    MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(nodeMap.get(n)))});


                }
            } else if (depthNodeNameSet != null && MyVars.getViewer().selectedNode == null) {
                LinkedHashMap<String, Long> nodeMap = new LinkedHashMap<>();
                for (String depthNode : depthNodeNameSet) {
                    nodeMap.put(depthNode, (long) ((MyNode)MyVars.g.vRefs.get(depthNode)).getCurrentValue());
                }
                nodeMap = MySysUtil.sortMapByLongValue(nodeMap);
                int tableRowCount= 0;
                for (String n : nodeMap.keySet()) {
                    MyNode nn = (MyNode) MyVars.g.vRefs.get(n);
                    if (nn.getCurrentValue() > 0) {
                        ((DefaultTableModel) currentNodeListTable.getModel()).addRow(new String[]{
                                "" + (++tableRowCount),
                                MySysUtil.getDecodedNodeName(nn.getName()),
                                MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(nodeMap.get(n)))}
                        );
                    }
                }

                tableRowCount= 0;
                for (String n :depthNodeNameSet) {
                    ((DefaultTableModel) nodeListTable.getModel()).addRow(
                            new String[]{"" + MyMathUtil.getCommaSeperatedNumber(++tableRowCount),
                                    MySysUtil.getDecodedNodeName(n),
                                    MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(nodeMap.get(n)))
                            }
                    );
                }
            }
        } else if (MyVars.getViewer().selectedNode != null) {
            LinkedHashMap<String, Long> nodeMap = new LinkedHashMap<>();
            ArrayList<MyNode> nodes = new ArrayList<>(MyVars.g.getSuccessors(MyVars.getViewer().selectedNode));
            nodes.addAll(MyVars.g.getPredecessors(MyVars.getViewer().selectedNode));
            for (MyNode n : nodes) {
                nodeMap.put(n.getName(), (long) n.getCurrentValue());
            }

            nodeMap = MySysUtil.sortMapByLongValue(nodeMap);
            int tableRowCount = 0;
            for (String n : nodeMap.keySet()) {
                MyNode nn = (MyNode) MyVars.g.vRefs.get(n);
                ((DefaultTableModel) currentNodeListTable.getModel()).addRow(
                    new String[]{
                        "" + (++tableRowCount),
                        MySysUtil.getDecodedNodeName(nn.getName()),
                        MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(nodeMap.get(n)))});
            }

            tableRowCount= 0;
            nodes = new ArrayList(MyVars.g.getVertices());
            nodeMap = new LinkedHashMap<>();
            for (MyNode n : nodes) {
                nodeMap.put(n.getName(), n.getContribution());
            }

            nodeMap = MySysUtil.sortMapByLongValue(nodeMap);
            for (String n : nodeMap.keySet()) {
                ((DefaultTableModel) nodeListTable.getModel()).addRow(
                        new String[]{"" + MyMathUtil.getCommaSeperatedNumber(++tableRowCount),
                                MySysUtil.getDecodedNodeName(n),
                                MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(nodeMap.get(n)))
                        }
                );
            }
        } else if (MyVars.getViewer().multiNodes != null) {
            LinkedHashMap<String, Long> nodeMap = new LinkedHashMap<>();
            ArrayList<MyNode> nodes = new ArrayList(MyVars.getViewer().multiNodePredecessors);
            nodes.addAll(MyVars.getViewer().multiNodeSuccessors);
            for (MyNode n : nodes) {
                nodeMap.put(n.getName(), (long) n.getCurrentValue());
            }

            nodeMap = MySysUtil.sortMapByLongValue(nodeMap);
            int tableRowCount = 0;
            for (String n : nodeMap.keySet()) {
                MyNode nn = (MyNode) MyVars.g.vRefs.get(n);
                ((DefaultTableModel) currentNodeListTable.getModel()).addRow(
                        new String[]{
                                "" + (++tableRowCount),
                                MySysUtil.getDecodedNodeName(nn.getName()),
                                MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(nodeMap.get(n)))
                        });
            }

            tableRowCount= 0;
            nodes = new ArrayList(MyVars.g.getVertices());
            nodeMap = new LinkedHashMap<>();
            for (MyNode n : nodes) {
                nodeMap.put(n.getName(), (long) n.getContribution());
            }

            nodeMap = MySysUtil.sortMapByLongValue(nodeMap);
            for (String n : nodeMap.keySet()) {
                ((DefaultTableModel) nodeListTable.getModel()).addRow(
                        new String[]{"" + MyMathUtil.getCommaSeperatedNumber(++tableRowCount),
                                MySysUtil.getDecodedNodeName(n),
                                MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(nodeMap.get(n)))
                        }
                );
            }
        } else {
            LinkedHashMap<String, Long> nodeMap = new LinkedHashMap<>();
            Collection<MyNode> nodes = MyVars.g.getVertices();
            for (MyNode n : nodes) {
                nodeMap.put(n.getName(), (long) n.getCurrentValue());
            }
            nodeMap = MySysUtil.sortMapByLongValue(nodeMap);
            int tableRowCount= 0;
            for (String n : nodeMap.keySet()) {
                MyNode nn = (MyNode) MyVars.g.vRefs.get(n);
                if (nn.getCurrentValue() > 0) {
                    ((DefaultTableModel) currentNodeListTable.getModel()).addRow(new String[]{
                            "" + (++tableRowCount),
                            MySysUtil.getDecodedNodeName(nn.getName()),
                            MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(nodeMap.get(n)))}
                    );
                }
            }

            tableRowCount= 0;
            for (String n : nodeMap.keySet()) {
                ((DefaultTableModel) nodeListTable.getModel()).addRow(
                        new String[]{"" + MyMathUtil.getCommaSeperatedNumber(++tableRowCount),
                                MySysUtil.getDecodedNodeName(n),
                                MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(nodeMap.get(n)))
                        }
                );
            }
        }
    }

    private void updateEdgeTable() {
        for (int i = edgeTable.getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) edgeTable.getModel()).removeRow(i);
        }

        if (MyVars.getViewer().selectedNode != null) {
            LinkedHashMap<String, Long> edgeMap = new LinkedHashMap<>();
            Collection<MyEdge> edges = MyVars.g.getIncidentEdges(MyVars.getViewer().selectedNode);
            for (MyEdge e : edges) {
                edgeMap.put(e.getSource() + "-" + e.getDest(), (long)e.getContribution());
            }
            edgeMap = MySysUtil.sortMapByLongValue(edgeMap);
            for (String edgeName : edgeMap.keySet()) {
                String [] pairNames = edgeName.split("-");
                ((DefaultTableModel) edgeTable.getModel()).addRow(
                        new String[]{
                                MySysUtil.getDecodedNodeName(pairNames[0]),
                                MySysUtil.getDecodedNodeName(pairNames[1]),
                                MyMathUtil.getCommaSeperatedNumber(edgeMap.get(edgeName))});
            }
        } else if (MyVars.getViewer().multiNodes != null) {
            LinkedHashMap<String, Long> edgeMap = new LinkedHashMap<>();
            for (MyNode selectedNode : MyVars.getViewer().multiNodes) {
                Collection<MyEdge> edges = MyVars.g.getIncidentEdges(selectedNode);
                for (MyEdge e : edges) {
                    edgeMap.put(e.getSource() + "-" + e.getDest(), (long) e.getContribution());
                }
            }
            edgeMap = MySysUtil.sortMapByLongValue(edgeMap);
            for (String edgeName : edgeMap.keySet()) {
                String[] pairNames = edgeName.split("-");
                ((DefaultTableModel) edgeTable.getModel()).addRow(
                        new String[]{
                                MySysUtil.getDecodedNodeName(pairNames[0]),
                                MySysUtil.getDecodedNodeName(pairNames[1]),
                                MyMathUtil.getCommaSeperatedNumber(edgeMap.get(edgeName))});
            }
        } else {
            LinkedHashMap<String, Long> edgeMap = new LinkedHashMap<>();
            Collection<MyEdge> edges = MyVars.g.getEdges();
            for (MyEdge e : edges) {
                edgeMap.put(e.getSource() + "-" + e.getDest(), (long)e.getContribution());
            }
            edgeMap = MySysUtil.sortMapByLongValue(edgeMap);
            for (String edgeName : edgeMap.keySet()) {
                String [] pairNames = edgeName.split("-");
                ((DefaultTableModel) edgeTable.getModel()).addRow(
                        new String[]{
                                MySysUtil.getDecodedNodeName(pairNames[0]),
                                MySysUtil.getDecodedNodeName(pairNames[1]),
                                MyMathUtil.getCommaSeperatedNumber(edgeMap.get(edgeName))});
            }
        }
    }

    private void updateStatTable() {
        for (int i = statTable.getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) statTable.getModel()).removeRow(i);
        }

        if (MyVars.getViewer().selectedNode != null) {
            ((DefaultTableModel) statTable.getModel()).addRow(new String[]{"N.", MySysUtil.getDecodedNodeName(MyVars.getViewer().selectedNode.getName())});
            ((DefaultTableModel) statTable.getModel()).addRow(new String[]{"NO. N.", MyMathUtil.getCommaSeperatedNumber(MyVars.g.getNeighborCount(MyVars.getViewer().selectedNode)+1)});
            ((DefaultTableModel) statTable.getModel()).addRow(new String[]{"E.", MyMathUtil.getCommaSeperatedNumber(MyVars.g.getIncidentEdges(MyVars.getViewer().selectedNode).size())});
            ((DefaultTableModel) statTable.getModel()).addRow(new String[]{"P.", MyMathUtil.getCommaSeperatedNumber(MyVars.g.getPredecessorCount(MyVars.getViewer().selectedNode))});
            ((DefaultTableModel) statTable.getModel()).addRow(new String[]{"S.", MyMathUtil.getCommaSeperatedNumber(MyVars.g.getSuccessorCount(MyVars.getViewer().selectedNode))});
            ((DefaultTableModel) statTable.getModel()).addRow(new String[]{"U. C.", MyMathUtil.getCommaSeperatedNumber(MyVars.getViewer().selectedNode.getUniqueContribution())});
            ((DefaultTableModel) statTable.getModel()).addRow(new String[]{"C.:", MyMathUtil.getCommaSeperatedNumber(MyVars.getViewer().selectedNode.getContribution())});
            ((DefaultTableModel) statTable.getModel()).addRow(new String[]{"IN-C.:", MyMathUtil.getCommaSeperatedNumber(MyVars.getViewer().selectedNode.getInContribution())});
            ((DefaultTableModel) statTable.getModel()).addRow(new String[]{"MAX. IN-C.:", MyMathUtil.getCommaSeperatedNumber(MyVars.getViewer().selectedNode.getMaxInContribution())});
            ((DefaultTableModel) statTable.getModel()).addRow(new String[]{"AVG. IN-C.:", MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyVars.getViewer().selectedNode.getAverageInContribution()))});
            ((DefaultTableModel) statTable.getModel()).addRow(new String[]{"OUT-C.:", MyMathUtil.getCommaSeperatedNumber(MyVars.getViewer().selectedNode.getOutContribution())});
            ((DefaultTableModel) statTable.getModel()).addRow(new String[]{"MAX. OUT-C.:", MyMathUtil.getCommaSeperatedNumber(MyVars.getViewer().selectedNode.getMaxOutContribution())});
            ((DefaultTableModel) statTable.getModel()).addRow(new String[]{"AVG. OUT-C.:", MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyVars.getViewer().selectedNode.getAverageOutContribution()))});
        } else if (MyVars.getViewer().multiNodes != null) {
            ((DefaultTableModel) statTable.getModel()).addRow(new String[]{"SEL. N.", MyMathUtil.getCommaSeperatedNumber(MyVars.getViewer().multiNodes.size())});
            ((DefaultTableModel) statTable.getModel()).addRow(new String[]{"N.", MyMathUtil.getCommaSeperatedNumber(MyVars.getViewer().multiNodePredecessors.size()+MyVars.getViewer().multiNodeSuccessors.size()+MyVars.getViewer().multiNodes.size())});
            ((DefaultTableModel) statTable.getModel()).addRow(new String[]{"P.", MyMathUtil.getCommaSeperatedNumber(MyVars.getViewer().multiNodePredecessors.size())});
            ((DefaultTableModel) statTable.getModel()).addRow(new String[]{"S.", MyMathUtil.getCommaSeperatedNumber(MyVars.getViewer().multiNodeSuccessors.size())});
            ((DefaultTableModel) statTable.getModel()).addRow(new String[]{"SHR P.", MyMathUtil.getCommaSeperatedNumber(MyVars.getViewer().sharedPredecessors.size())});
            ((DefaultTableModel) statTable.getModel()).addRow(new String[]{"SHR S.", MyMathUtil.getCommaSeperatedNumber(MyVars.getViewer().sharedSuccessors.size())});
        } else {
            MyViewerControlComponentUtil.setGraphLevelTableStatistics();
        }
    }


    public JPanel getNetworkChart() {return this;}
}


