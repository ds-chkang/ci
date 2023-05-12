package medousa.sequential.graph;

import medousa.MyProgressBar;
import medousa.message.MyMessageUtil;
import medousa.sequential.graph.listener.MyEdgeLabelSelecterListener;
import medousa.sequential.graph.listener.MyNodeLabelSelecterListener;
import medousa.sequential.graph.stats.barchart.MyClusteredGraphLevelEdgeValueBarChart;
import medousa.sequential.graph.stats.barchart.MyClusteredGraphLevelNodeValueBarChart;
import medousa.sequential.graph.path.MyDepthFirstGraphPathSercher;
import medousa.sequential.graph.stats.MyTextStatistics;
import medousa.sequential.graph.stats.barchart.MyGraphLevelEdgeValueBarChart;
import medousa.sequential.graph.stats.barchart.MyGraphLevelNodeValueBarChart;
import medousa.sequential.graph.listener.MyNodeEdgeExclusionActionListener;
import medousa.sequential.utils.*;
import medousa.sequential.utils.MySequentialGraphSysUtil;
import medousa.sequential.utils.MySequentialGraphVars;
import medousa.table.MyTableToolTipper;
import medousa.table.MyTableUtil;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

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
        "DURATION", //17
        "AVG. DURATION", //18
        "MAX. DURATION", //19
        "MIN. DURATION", //20
        "AVG. REACH TIME", //21
        "TOTAL REACH TIME", //22
        "TOTAL RECURRENCE COUNT",  //23
        "TOTAL RECURRENCE TIME", //24
        "MAX. RECURRENCE TIME",  //25
        "MIN. RECURRENCE TIME",  //26
        "ITEMSET LENGTH",    //27
        "BETWEENESS",     //28
        "CLOSENESS",   //29
        "EIGENVECTOR",   //30
        "PAGERANK",  //31
        "START POSITION COUNT",  //32
        "UNREACHABLE NODE CCOUNT"  //33
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
    public JPanel bottomRightPanel = new JPanel();
    public JPanel bottomPanel = new JPanel();
    public JButton excludeBtn;
    public MyTextStatistics vTxtStat = new MyTextStatistics();
    public JComboBox depthSelecter = new JComboBox();
    public JComboBox nodeValueSelecter = new JComboBox();
    public JComboBox edgeValueSelecter = new JComboBox();
    public JComboBox nodeValueExcludeSymbolSelecter = new JComboBox();
    public JComboBox edgeValueExcludeSymbolSelecter  = new JComboBox();
    public JComboBox edgeLabelSelecter = new JComboBox();
    public JComboBox nodeLabelSelecter = new JComboBox();
    public JComboBox nodeLabelExcludeSymbolSelecter = new JComboBox();
    public JComboBox nodeLabelExcludeSelecter = new JComboBox();
    public JComboBox nodeLabelValueExcludeSelecter = new JComboBox();
    public JComboBox edgeLabelExcludeSymbolSelecter = new JComboBox();
    public JComboBox edgeLabelExcludeSelecter = new JComboBox();
    public JComboBox depthExcludeSelecter = new JComboBox();
    public JComboBox depthExcludeSymbolSelecter = new JComboBox();
    public JComboBox edgeLabelValueExcludeSelecter = new JComboBox();
    public JComboBox selectedNodeNeighborNodeTypeSelector = new JComboBox();
    public JComboBox depthNeighborNodeTypeSelector = new JComboBox();
    public JComboBox clusteringSelector = new JComboBox();
    public JTextField nodeValueExcludeTxt = new JTextField();
    public JTextField edgeValueExcludeTxt = new JTextField();
    public JCheckBox weightedNodeColor = new JCheckBox("N. C.");
    public JCheckBox nodeValueBarChart = new JCheckBox("N. V. B.");
    public JCheckBox edgeValueBarChart = new JCheckBox("E. V. B.");
    public JCheckBox endingNodeBarChart = new JCheckBox("E. N. B.");
    public Map<String, Integer> selectedNodeSuccessorDepthNodeMap;
    public Map<String, Integer> selectedNodePredecessorDepthNodeMap;
    public Map<String, Map<String, Integer>> depthNodeSuccessorMaps;
    public Map<String, Map<String, Integer>> depthNodePredecessorMaps;
    public Set<String> depthNodeNameSet;
    public Set<String> depthNeighborSet;
    public JSplitPane graphTableSplitPane = new JSplitPane();
    public JTabbedPane tableTabbedPane = new JTabbedPane();
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

    private JPanel setPathFindTable() {
        JPanel fromTablePanel = new JPanel();
        fromTablePanel.setLayout(new BorderLayout(2, 0));
        fromTablePanel.setBackground(Color.WHITE);

        JComboBox pathMenu = new JComboBox();
        pathMenu.setFont(MySequentialGraphVars.tahomaPlainFont11);
        pathMenu.setFocusable(false);
        pathMenu.addItem("SELECT");
        pathMenu.addItem("PATH EXPLORATION");
        pathMenu.addItem("SHORTEST REACH");

        String [] fromColumns = {"NO.", "FROM NODE"};
        String [][] fromData = {};
        DefaultTableModel fromTableModel = new DefaultTableModel(fromData, fromColumns);
        pathFromTable = new JTable(fromTableModel) {
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

        String [] fromTableTooltips = {"NO.", "FROM NODE"};
        MyTableToolTipper fromTableTooltipHeader = new MyTableToolTipper(this.pathFromTable.getColumnModel());
        fromTableTooltipHeader.setToolTipStrings(fromTableTooltips);
        this.pathFromTable.setTableHeader(fromTableTooltipHeader);

        int i=-0;
        for (String n : MySequentialGraphVars.nodeNameMap.keySet()) {
            fromTableModel.addRow(new String[]{String.valueOf(++i), n});
        }

        pathFromTable.setRowHeight(22);
        pathFromTable.setBackground(Color.WHITE);
        pathFromTable.setFont(MySequentialGraphVars.f_pln_10);
        pathFromTable.getTableHeader().setFont(MySequentialGraphVars.tahomaBoldFont12);
        pathFromTable.getTableHeader().setOpaque(false);
        pathFromTable.getTableHeader().setBackground(new Color(0,0,0,0f));
        pathFromTable.getColumnModel().getColumn(0).setPreferredWidth(35);
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
        fromNodeSearchTxt.setFont(MySequentialGraphVars.f_bold_10);

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

        String [] toColumns = {"NO.", "TO NODE"};
        String [][] toData = {};
        DefaultTableModel toTableModel = new DefaultTableModel(toData, toColumns);
        pathToTable = new JTable(toTableModel) {
            //Implement table cell tool tips.
            public String getToolTipText(MouseEvent e) {
                String tip = null;
                Point p = e.getPoint();
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
        for (String n : MySequentialGraphVars.nodeNameMap.keySet()) {
            toTableModel.addRow(new String[]{String.valueOf(++ii), n});
        }

        pathToTable.setRowHeight(22);
        pathToTable.setBackground(Color.WHITE);
        pathToTable.setFont(MySequentialGraphVars.f_pln_10);
        pathToTable.getTableHeader().setFont(MySequentialGraphVars.tahomaBoldFont12);
        pathToTable.getTableHeader().setOpaque(false);
        pathToTable.getTableHeader().setBackground(new Color(0,0,0,0f));
        pathToTable.getColumnModel().getColumn(0).setPreferredWidth(35);
        pathToTable.getColumnModel().getColumn(1).setPreferredWidth(130);

        JTextField toNodeSearchTxt = new JTextField();
        JButton runBtn = new JButton("RUN");
        runBtn.setFocusable(false);
        runBtn.setFont(MySequentialGraphVars.tahomaPlainFont11);

        toNodeSearchTxt.setBackground(Color.WHITE);
        toNodeSearchTxt.setFont(MySequentialGraphVars.f_bold_10);
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
                            MyMessageUtil.showInfoMsg(MySequentialGraphVars.app, "Select a path option.");
                        } else if (pathMenu.getSelectedIndex() == 1) {
                            if (pathFromTable.getSelectedRow() >= 0 && pathToTable.getSelectedRow() >= 0) {
                                String fromTableNodeName = pathFromTable.getValueAt(pathFromTable.getSelectedRow(), 1).toString();
                                String toTableNodeName = pathToTable.getValueAt(pathToTable.getSelectedRow(), 1).toString();
                                String fromNode = MySequentialGraphVars.nodeNameMap.get(fromTableNodeName);
                                String toNode = MySequentialGraphVars.nodeNameMap.get(toTableNodeName);
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
                                MyMessageUtil.showInfoMsg(MySequentialGraphVars.app, "Two nodes are required for the operation.");
                            }
                        } else if (pathMenu.getSelectedIndex() == 2) {
                            if (pathFromTable.getSelectedRow() >= 0 && pathToTable.getSelectedRow() >= 0) {
                                String fromTableNodeName = pathFromTable.getValueAt(pathFromTable.getSelectedRow(), 1).toString();
                                String toTableNodeName = pathToTable.getValueAt(pathToTable.getSelectedRow(), 1).toString();
                                MyNode fromNode = (MyNode) MySequentialGraphVars.g.vRefs.get(MySequentialGraphVars.nodeNameMap.get(fromTableNodeName));
                                MyNode toNode = (MyNode) MySequentialGraphVars.g.vRefs.get(MySequentialGraphVars.nodeNameMap.get(toTableNodeName));
                                String shortestPathLength = MyMathUtil.getCommaSeperatedNumber(MySequentialGraphSysUtil.getUnWeightedBetweenNodeShortestPathLength(fromNode, toNode));
                                try {
                                    Thread.sleep(500);
                                    if (shortestPathLength.equals("0")) {
                                        MyMessageUtil.showInfoMsg(MySequentialGraphVars.app, "[" + toTableNodeName + "] is unreachable from [" + fromTableNodeName + "]");
                                    } else {
                                        MyMessageUtil.showInfoMsg(MySequentialGraphVars.app, "Shortest Path Length Between [" + fromTableNodeName + "] and [" + toTableNodeName + "] is " + shortestPathLength);
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            } else {
                                MyMessageUtil.showInfoMsg(MySequentialGraphVars.app, "Two nodes are required for the operation.");
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

    public void decorateGraphViewer(JPanel graphViewer) {
        this.setLayout(new BorderLayout());
        this.setBackground(Color.WHITE);
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));

        this.nodeValueBarChart.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.edgeValueBarChart.setFont(MySequentialGraphVars.tahomaPlainFont12);
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
        MyViewerControlComponentUtil.setDepthValueSelecterMenu();

        this.depthExcludeSelecter.setBackground(Color.WHITE);
        this.depthExcludeSelecter.setFocusable(false);
        this.depthExcludeSelecter.setToolTipText("SELECT A DEPTH");
        this.depthExcludeSelecter.setFont(MySequentialGraphVars.tahomaPlainFont12);
        MyViewerControlComponentUtil.setDepthExcludeSelecterMenu();

        this.depthExcludeSymbolSelecter.setBackground(Color.WHITE);
        this.depthExcludeSymbolSelecter.setFocusable(false);
        this.depthExcludeSymbolSelecter.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.depthExcludeSymbolSelecter.addItem("");
        this.depthExcludeSymbolSelecter.addItem("==");
        this.depthExcludeSymbolSelecter.addItem("!=");
        this.depthExcludeSymbolSelecter.addItem("<");
        this.depthExcludeSymbolSelecter.addItem(">");

        nodeValueExcludeSymbolSelecter.setFocusable(false);
        nodeValueExcludeSymbolSelecter.setBackground(Color.WHITE);

        edgeValueExcludeSymbolSelecter.setFocusable(false);
        edgeValueExcludeSymbolSelecter.setBackground(Color.WHITE);

        JLabel nodeValueLabel = new JLabel("  N. V.");
        nodeValueLabel.setToolTipText("NODE VALUE");
        nodeValueLabel.setBackground(Color.WHITE);
        nodeValueLabel.setFont(MySequentialGraphVars.tahomaPlainFont12);
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
        MyViewerControlComponentUtil.setNodeValueComboBoxMenu();

        JLabel edgeValueLabel = new JLabel("   E. V.");
        edgeValueLabel.setToolTipText("EDGE VALUE");
        edgeValueLabel.setBackground(Color.WHITE);
        edgeValueLabel.setFont(MySequentialGraphVars.tahomaPlainFont12);

        JLabel edgeLabelLabel = new JLabel("  E. L.");
        edgeLabelLabel.setToolTipText("EDGE LABEL");
        edgeLabelLabel.setBackground(Color.WHITE);
        edgeLabelLabel.setFont(MySequentialGraphVars.tahomaPlainFont12);

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
        MyViewerControlComponentUtil.setEdgeValueSelecterMenu();

        this.edgeLabelSelecter.setFocusable(false);
        this.edgeLabelSelecter.setToolTipText("SELECT AN EDGE LABEL");
        this.edgeLabelSelecter.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.edgeLabelSelecter.setBackground(Color.WHITE);
        this.edgeLabelSelecter.addItem("NONE");
        for (String edgeLabel : MySequentialGraphVars.userDefinedEdgeLabelSet) {this.edgeLabelSelecter.addItem(edgeLabel);}

        this.edgeLabelSelecter.addActionListener(this);

        JLabel nodeLabelLabel = new JLabel("  N. L.");
        nodeLabelLabel.setToolTipText("NODE LABEL");
        nodeLabelLabel.setBackground(Color.WHITE);
        nodeLabelLabel.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.nodeLabelSelecter.setFocusable(false);
        this.nodeLabelSelecter.setToolTipText("SELECT A NODE LABEL");
        this.nodeLabelSelecter.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.nodeLabelSelecter.setBackground(Color.WHITE);
        this.nodeLabelSelecter.addItem("NONE");
        this.nodeLabelSelecter.addItem("NAME");
        for (String nodeLabel : MySequentialGraphVars.userDefinedNodeLabelSet) {this.nodeLabelSelecter.addItem(nodeLabel);}
        this.nodeLabelSelecter.setSelectedIndex(1);
        this.nodeLabelSelecter.addActionListener(this);

        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.WHITE);
        topPanel.setLayout(new BorderLayout(5,5));

        this.setNodeValueExcludeSymbolComboBox();
        this.setEdgeValueExcludeSymbolComboBox();

        this.nodeValueExcludeTxt.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.nodeValueExcludeTxt.setToolTipText("ENTER A NUMERIC VALUE");
        this.nodeValueExcludeTxt.setHorizontalAlignment(JTextField.CENTER);
        this.nodeValueExcludeTxt.setBorder(new LineBorder(Color.DARK_GRAY,1));
        this.nodeValueExcludeTxt.setBackground(Color.WHITE);
        this.nodeValueExcludeTxt.setPreferredSize(new Dimension(60, 20));

        this.excludeBtn = new JButton("EXCL.");
        this.excludeBtn.setToolTipText("EXCLUDE NODES AND EDGES");
        this.excludeBtn.setBackground(Color.WHITE);
        this.excludeBtn.setFocusable(false);
        this.excludeBtn.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.excludeBtn.addActionListener(new MyNodeEdgeExclusionActionListener(this));

        this.topLeftPanel.setBackground(Color.WHITE);
        this.topLeftPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JLabel removeEdgeEmptyLabel = new JLabel("  ");
        removeEdgeEmptyLabel.setBackground(Color.WHITE);
        removeEdgeEmptyLabel.setPreferredSize(new Dimension(50, 20));

        this.nodeValueBarChart.setFocusable(false);
        this.nodeValueBarChart.setToolTipText("SHOW NODE VALUE BAR CHART");
        this.nodeValueBarChart.setBackground(Color.WHITE);
        this.nodeValueBarChart.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.nodeValueBarChart.addActionListener(this);

        this.edgeValueBarChart.setFocusable(false);
        this.edgeValueBarChart.setToolTipText("SHOW EDGE VALUE BAR CHART");
        this.edgeValueBarChart.setBackground(Color.WHITE);
        this.edgeValueBarChart.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.edgeValueBarChart.addActionListener(this);

        this.endingNodeBarChart.setFocusable(false);
        this.endingNodeBarChart.setToolTipText("SHOW ENDING NODE BAR CHART");
        this.endingNodeBarChart.setBackground(Color.WHITE);
        this.endingNodeBarChart.setFont(MySequentialGraphVars.tahomaPlainFont12);

        JLabel nodeValueExcludeOptionLabel = new JLabel("N. V.");
        nodeValueExcludeOptionLabel.setFont(MySequentialGraphVars.tahomaPlainFont12);
        nodeValueExcludeOptionLabel.setBackground(Color.WHITE);

        this.topLeftPanel.add(nodeValueExcludeOptionLabel);
        this.topLeftPanel.add(this.nodeValueExcludeSymbolSelecter);
        this.topLeftPanel.add(this.nodeValueExcludeTxt);

        JLabel edgeValueExludeLabel = new JLabel("  E. V.");
        edgeValueExludeLabel.setFont(MySequentialGraphVars.tahomaPlainFont12);
        edgeValueExludeLabel.setBackground(Color.WHITE);

        this.topLeftPanel.add(edgeValueExludeLabel);
        this.topLeftPanel.add(this.edgeValueExcludeSymbolSelecter);
        this.topLeftPanel.add(this.edgeValueExcludeTxt);

        JLabel nodeLabelExcludeComboBoxMenuLabel = new JLabel( "  N. L.");
        nodeLabelExcludeComboBoxMenuLabel.setFont(MySequentialGraphVars.tahomaPlainFont12);
        nodeLabelExcludeComboBoxMenuLabel.setBackground(Color.WHITE);

        this.nodeLabelExcludeSymbolSelecter.setBackground(Color.WHITE);
        this.nodeLabelExcludeSymbolSelecter.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.nodeLabelExcludeSymbolSelecter.setFocusable(false);
        this.nodeLabelExcludeSymbolSelecter.addItem("");
        this.nodeLabelExcludeSymbolSelecter.addItem("==");
        this.nodeLabelExcludeSymbolSelecter.addItem("!=");

        this.nodeLabelExcludeSelecter.setBackground(Color.WHITE);
        this.nodeLabelExcludeSelecter.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.nodeLabelExcludeSelecter.setFocusable(false);
        this.nodeLabelExcludeSelecter.addItem("");
        for (String nodeLabel : MySequentialGraphVars.userDefinedNodeLabelSet) {
            this.nodeLabelExcludeSelecter.addItem(nodeLabel);
        }
        this.nodeLabelExcludeSymbolSelecter.addActionListener(new MyNodeLabelSelecterListener(this.nodeLabelExcludeSelecter, this.nodeLabelValueExcludeSelecter));

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
            this.topLeftPanel.add(this.nodeLabelExcludeSymbolSelecter);
            this.topLeftPanel.add(this.nodeLabelExcludeSelecter);
            this.topLeftPanel.add(this.nodeLabelValueExcludeSelecter);
        }

        JLabel edgeLabelExcludeComboBoxMenuLabel = new JLabel("   E. L.");
        edgeLabelExcludeComboBoxMenuLabel.setBackground(Color.WHITE);
        edgeLabelExcludeComboBoxMenuLabel.setFont(MySequentialGraphVars.tahomaPlainFont12);

        this.edgeLabelExcludeSymbolSelecter.setBackground(Color.WHITE);
        this.edgeLabelExcludeSymbolSelecter.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.edgeLabelExcludeSymbolSelecter.setFocusable(false);
        this.edgeLabelExcludeSymbolSelecter.addItem("");
        this.edgeLabelExcludeSymbolSelecter.addItem("==");
        this.edgeLabelExcludeSymbolSelecter.addItem("!=");

        this.edgeLabelExcludeSelecter.setBackground(Color.WHITE);
        this.edgeLabelExcludeSelecter.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.edgeLabelExcludeSelecter.setFocusable(false);
        this.edgeLabelExcludeSelecter.addItem("");
        for (String nodeLabel : MySequentialGraphVars.userDefinedEdgeLabelSet) {
            this.edgeLabelExcludeSelecter.addItem(nodeLabel);
        }
        this.edgeLabelExcludeSelecter.addActionListener(
            new MyEdgeLabelSelecterListener(this.edgeLabelExcludeSelecter, this.edgeLabelValueExcludeSelecter));

        this.edgeLabelValueExcludeSelecter.setBackground(Color.WHITE);
        this.edgeLabelValueExcludeSelecter.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.edgeLabelValueExcludeSelecter.setFocusable(false);
        this.edgeLabelValueExcludeSelecter.addItem("");

        if (MySequentialGraphVars.userDefinedEdgeLabelSet.size() > 0) {
            this.topLeftPanel.add(edgeLabelExcludeComboBoxMenuLabel);
            this.topLeftPanel.add(this.edgeLabelExcludeSymbolSelecter);
            this.topLeftPanel.add(this.edgeLabelExcludeSelecter);
            this.topLeftPanel.add(this.edgeLabelValueExcludeSelecter);
        }
        this.topLeftPanel.add(this.depthExcludeSelecter);
        this.topLeftPanel.add(this.depthExcludeSymbolSelecter);
        this.topLeftPanel.add(this.excludeBtn);

        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setLayout(new BorderLayout(3,3));

        this.vTxtStat.setTextStatistics();

        clusteringSectorLabel.setBackground(Color.WHITE);
        clusteringSectorLabel.setToolTipText("FIND CLUSTERS");
        clusteringSectorLabel.setFont(MySequentialGraphVars.tahomaPlainFont12);

        this.clusteringSelector.setBackground(Color.WHITE);
        this.clusteringSelector.setToolTipText("FIND CLUSTERS");
        this.clusteringSelector.setFont(MySequentialGraphVars.tahomaPlainFont12);
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

        this.bottomLeftControlPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3,3));
        this.bottomLeftControlPanel.setBackground(Color.WHITE);

        this.weightedNodeColor.setFocusable(false);
        this.weightedNodeColor.setToolTipText("SHOW WEIGHTED NODE COLORS");
        this.weightedNodeColor.setBackground(Color.WHITE);
        this.weightedNodeColor.setFont(MySequentialGraphVars.tahomaPlainFont11);
        this.weightedNodeColor.addActionListener(this);

        this.bottomLeftControlPanel.add(this.weightedNodeColor);
        this.bottomLeftControlPanel.add(this.nodeValueBarChart);
        this.bottomLeftControlPanel.add(this.edgeValueBarChart);

        this.bottomRightPanel.setBackground(Color.WHITE);
        this.bottomRightPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3,3));
        this.bottomRightPanel.add(this.depthSelecter);
        this.bottomRightPanel.add(this.depthNeighborNodeTypeSelector);
        this.bottomRightPanel.add(this.selectedNodeNeighborNodeTypeSelector);
        this.bottomRightPanel.add(nodeValueLabel);
        this.bottomRightPanel.add(this.nodeValueSelecter);
        this.bottomRightPanel.add(nodeLabelLabel) ;
        this.bottomRightPanel.add(this.nodeLabelSelecter);
        this.bottomRightPanel.add(edgeValueLabel);
        this.bottomRightPanel.add(this.edgeValueSelecter);
        if (MySequentialGraphVars.userDefinedEdgeLabelSet.size() > 0) {
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
        graphGroupNodeNumberPercentLabel.setFont(MySequentialGraphVars.tahomaPlainFont11);
        graphGroupNodeNumberPercentLabel.setText("");
        topRightPanel.add(graphGroupNodeNumberPercentLabel);
        topRightPanel.add(clusteringSectorLabel);
        topRightPanel.add(clusteringSelector);

        topPanel.add(topRightPanel, BorderLayout.EAST);
        topPanel.add(this.topLeftPanel, BorderLayout.WEST);

        this.tableTabbedPane.setFocusable(false);
        this.tableTabbedPane.setOpaque(false);
        this.tableTabbedPane.setPreferredSize(new Dimension(200, 1000));
        this.tableTabbedPane.setFont(MySequentialGraphVars.tahomaPlainFont12);

        this.tableTabbedPane.addTab("N.", null, setNodeTable(), "NODES");
        this.tableTabbedPane.addTab("E.", null, setEdgeTable(), "EDGES");
        this.tableTabbedPane.addTab("S.", null, setStatTable(), "GRAPH STATISTICS");
        this.tableTabbedPane.addTab("P.", null, setPathFindTable(), "PATH ANALYSIS BETWEEN TWO NODES");

        JPanel graphPanel = new JPanel();
        graphPanel.setBackground(Color.WHITE);
        graphPanel.setLayout(new BorderLayout(3,3));
        graphPanel.add(graphViewer, BorderLayout.CENTER);
        graphPanel.add(bottomPanel, BorderLayout.SOUTH);

        this.graphTableSplitPane.setDividerSize(4);
        this.graphTableSplitPane.setLeftComponent(tableTabbedPane);
        this.graphTableSplitPane.setRightComponent(graphPanel);
        this.graphTableSplitPane.setDividerLocation(0.11);
        this.graphTableSplitPane.setMinimumSize(new Dimension(200, 500));
        this.graphTableSplitPane.addComponentListener(new ComponentAdapter() {
            @Override public synchronized void componentResized(ComponentEvent e) {
                super.componentResized(e);
                graphTableSplitPane.setDividerLocation(0.11);

                if (nodeValueBarChart.isSelected()) {
                    MyViewerControlComponentUtil.removeBarChartsFromViewer();
                    MyViewerControlComponentUtil.removeSharedNodeValueBarCharts();
                    if (MySequentialGraphVars.getSequentialGraphViewer().selectedNode != null) {
                        MyViewerControlComponentUtil.setSelectedNodeNeighborValueBarChartToViewer();
                    } else if (MySequentialGraphVars.getSequentialGraphViewer().multiNodes != null) {
                        MyViewerControlComponentUtil.setSharedNodeLevelNodeValueBarChart();
                    } else {
                        MySequentialGraphVars.getSequentialGraphViewer().graphLevelNodeValueBarChart = new MyGraphLevelNodeValueBarChart();
                        MySequentialGraphVars.getSequentialGraphViewer().add(MySequentialGraphVars.getSequentialGraphViewer().graphLevelNodeValueBarChart);
                    }
                } else {
                    MyViewerControlComponentUtil.removeBarChartsFromViewer();
                    MyViewerControlComponentUtil.removeSharedNodeValueBarCharts();
                }
                MySequentialGraphVars.getSequentialGraphViewer().revalidate();
                MySequentialGraphVars.getSequentialGraphViewer().repaint();
            }
        });

        this.add(topPanel, BorderLayout.NORTH);
        this.add(this.graphTableSplitPane, BorderLayout.CENTER);
        //this.add(this.vTxtStat, BorderLayout.SOUTH);
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

        this.statTable.setRowHeight(22);
        this.statTable.setBackground(Color.WHITE);
        this.statTable.setFont(MySequentialGraphVars.tahomaPlainFont10);
        this.statTable.setFocusable(false);
        this.statTable.getTableHeader().setFont(MySequentialGraphVars.tahomaBoldFont10);
        this.statTable.getTableHeader().setOpaque(false);
        this.statTable.getTableHeader().setBackground(new Color(0,0,0,0f));
        this.statTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        this.statTable.setPreferredSize(new Dimension(145, 800));
        this.statTable.setForeground(Color.BLACK);
        this.statTable.getColumnModel().getColumn(0).setPreferredWidth(55);
        this.statTable.getColumnModel().getColumn(1).setPreferredWidth(50);
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
        int i=-0;
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
        bottomTablePanel.setLayout(new BorderLayout(3,3));
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
        nodeListTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        nodeListTable.getColumnModel().getColumn(2).setPreferredWidth(35);

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
        currentNodeListTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        currentNodeListTable.getColumnModel().getColumn(2).setPreferredWidth(35);

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
        this.pathFromTable.setAutoCreateRowSorter(false);
        this.pathToTable.setAutoCreateRowSorter(false);

        this.updateNodeTable();
        this.updateEdgeTable();
        this.updateStatTable();

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
        sorter = (TableRowSorter<DefaultTableModel>) pathFromTable.getRowSorter();
        // Get the existing pathFromTable model
        tableModel = (DefaultTableModel) sorter.getModel();
        // Create a new sorter with the existing pathFromTable model
        newSorter = new TableRowSorter<DefaultTableModel>(tableModel);
        // Set the new sorter as the RowSorter of pathFromTable
        pathFromTable.setRowSorter(newSorter);


        // Get the existing pathToTable RowSorter
        sorter = (TableRowSorter<DefaultTableModel>) pathToTable.getRowSorter();
        // Get the existing pathToTable model
        tableModel = (DefaultTableModel) sorter.getModel();
        // Create a new sorter with the existing pathToTable model
        newSorter = new TableRowSorter<DefaultTableModel>(tableModel);
        // Set the new sorter as the RowSorter of pathToTable
        pathToTable.setRowSorter(newSorter);

        this.nodeListTable.setAutoCreateRowSorter(true);
        this.currentNodeListTable.setAutoCreateRowSorter(true);
        this.edgeListTable.setAutoCreateRowSorter(true);
        this.pathFromTable.setAutoCreateRowSorter(true);
        this.pathToTable.setAutoCreateRowSorter(true);
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
            if (MySequentialGraphVars.getSequentialGraphViewer().predecessorsOnly || MySequentialGraphVars.getSequentialGraphViewer().successorsOnly) {
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
                    String[] pairNames = edgeName.split("-");
                    ((DefaultTableModel) edgeListTable.getModel()).addRow(
                            new String[]{
                                    MySequentialGraphSysUtil.getDecodedNodeName(pairNames[0]),
                                    MySequentialGraphSysUtil.getDecodedNodeName(pairNames[1]),
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
                    String[] pairNames = edgeName.split("-");
                    ((DefaultTableModel) edgeListTable.getModel()).addRow(
                            new String[]{
                                    MySequentialGraphSysUtil.getDecodedNodeName(pairNames[0]),
                                    MySequentialGraphSysUtil.getDecodedNodeName(pairNames[1]),
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
                                MySequentialGraphSysUtil.getDecodedNodeName(pairNames[0]),
                                MySequentialGraphSysUtil.getDecodedNodeName(pairNames[1]),
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
                                MySequentialGraphSysUtil.getDecodedNodeName(pairNames[0]),
                                MySequentialGraphSysUtil.getDecodedNodeName(pairNames[1]),
                                MyMathUtil.getCommaSeperatedNumber(edgeMap.get(edgeName))});
            }
        }
    }

    private void updateStatTable() {
        int row = this.edgeListTable.getRowCount();
        while (row > 0) {
            ((DefaultTableModel) edgeListTable.getModel()).removeRow(row-1);
            row = this.edgeListTable.getRowCount();
        }

        if (MySequentialGraphVars.getSequentialGraphViewer().selectedNode != null) {
            ((DefaultTableModel) statTable.getModel()).addRow(new String[]{"N.", MySequentialGraphSysUtil.getDecodedNodeName(MySequentialGraphVars.getSequentialGraphViewer().selectedNode.getName())});
            ((DefaultTableModel) statTable.getModel()).addRow(new String[]{"NO. N.", MyMathUtil.getCommaSeperatedNumber(MySequentialGraphVars.g.getNeighborCount(MySequentialGraphVars.getSequentialGraphViewer().selectedNode)+1)});
            ((DefaultTableModel) statTable.getModel()).addRow(new String[]{"E.", MyMathUtil.getCommaSeperatedNumber(MySequentialGraphVars.g.getIncidentEdges(MySequentialGraphVars.getSequentialGraphViewer().selectedNode).size())});
            ((DefaultTableModel) statTable.getModel()).addRow(new String[]{"P.", MyMathUtil.getCommaSeperatedNumber(MySequentialGraphVars.g.getPredecessorCount(MySequentialGraphVars.getSequentialGraphViewer().selectedNode))});
            ((DefaultTableModel) statTable.getModel()).addRow(new String[]{"S.", MyMathUtil.getCommaSeperatedNumber(MySequentialGraphVars.g.getSuccessorCount(MySequentialGraphVars.getSequentialGraphViewer().selectedNode))});
            ((DefaultTableModel) statTable.getModel()).addRow(new String[]{"U. C.", MyMathUtil.getCommaSeperatedNumber(MySequentialGraphVars.getSequentialGraphViewer().selectedNode.getUniqueContribution())});
            ((DefaultTableModel) statTable.getModel()).addRow(new String[]{"C.:", MyMathUtil.getCommaSeperatedNumber(MySequentialGraphVars.getSequentialGraphViewer().selectedNode.getContribution())});
            ((DefaultTableModel) statTable.getModel()).addRow(new String[]{"IN-C.:", MyMathUtil.getCommaSeperatedNumber(MySequentialGraphVars.getSequentialGraphViewer().selectedNode.getInContribution())});
            ((DefaultTableModel) statTable.getModel()).addRow(new String[]{"MAX. IN-C.:", MyMathUtil.getCommaSeperatedNumber(MySequentialGraphVars.getSequentialGraphViewer().selectedNode.getMaxInContribution())});
            ((DefaultTableModel) statTable.getModel()).addRow(new String[]{"AVG. IN-C.:", MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MySequentialGraphVars.getSequentialGraphViewer().selectedNode.getAverageInContribution()))});
            ((DefaultTableModel) statTable.getModel()).addRow(new String[]{"OUT-C.:", MyMathUtil.getCommaSeperatedNumber(MySequentialGraphVars.getSequentialGraphViewer().selectedNode.getOutContribution())});
            ((DefaultTableModel) statTable.getModel()).addRow(new String[]{"MAX. OUT-C.:", MyMathUtil.getCommaSeperatedNumber(MySequentialGraphVars.getSequentialGraphViewer().selectedNode.getMaxOutContribution())});
            ((DefaultTableModel) statTable.getModel()).addRow(new String[]{"AVG. OUT-C.:", MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MySequentialGraphVars.getSequentialGraphViewer().selectedNode.getAverageOutContribution()))});
        } else if (MySequentialGraphVars.getSequentialGraphViewer().multiNodes != null) {
            ((DefaultTableModel) statTable.getModel()).addRow(new String[]{"SEL. N.", MyMathUtil.getCommaSeperatedNumber(MySequentialGraphVars.getSequentialGraphViewer().multiNodes.size())});
            ((DefaultTableModel) statTable.getModel()).addRow(new String[]{"N.", MyMathUtil.getCommaSeperatedNumber(MySequentialGraphVars.getSequentialGraphViewer().multiNodePredecessors.size()+ MySequentialGraphVars.getSequentialGraphViewer().multiNodeSuccessors.size()+ MySequentialGraphVars.getSequentialGraphViewer().multiNodes.size())});
            ((DefaultTableModel) statTable.getModel()).addRow(new String[]{"P.", MyMathUtil.getCommaSeperatedNumber(MySequentialGraphVars.getSequentialGraphViewer().multiNodePredecessors.size())});
            ((DefaultTableModel) statTable.getModel()).addRow(new String[]{"S.", MyMathUtil.getCommaSeperatedNumber(MySequentialGraphVars.getSequentialGraphViewer().multiNodeSuccessors.size())});
            ((DefaultTableModel) statTable.getModel()).addRow(new String[]{"SHR P.", MyMathUtil.getCommaSeperatedNumber(MySequentialGraphVars.getSequentialGraphViewer().sharedPredecessors.size())});
            ((DefaultTableModel) statTable.getModel()).addRow(new String[]{"SHR S.", MyMathUtil.getCommaSeperatedNumber(MySequentialGraphVars.getSequentialGraphViewer().sharedSuccessors.size())});
        } else {
            MyViewerControlComponentUtil.setGraphLevelTableStatistics();
        }
    }

    public JPanel getSequentialGraphChart() {return this;}

    @Override public void actionPerformed(ActionEvent ae) {
      if (ae.getSource() == nodeValueBarChart) {
            new Thread(new Runnable() {
                @Override public void run() {
                    if (nodeValueBarChart.isSelected()) {
                        MyViewerControlComponentUtil.removeBarChartsFromViewer();
                        MyViewerControlComponentUtil.removeSharedNodeValueBarCharts();
                        if (MySequentialGraphVars.getSequentialGraphViewer().isClustered) {
                            if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueBarChart.getText().equals("N. V. B.")) {
                                MyMessageUtil.showInfoMsg("Select a cluster ID.");
                                return;
                            }
                            MySequentialGraphVars.getSequentialGraphViewer().clusteredGraphLevelNodeValueBarChart = new MyClusteredGraphLevelNodeValueBarChart();
                            MySequentialGraphVars.getSequentialGraphViewer().add(MySequentialGraphVars.getSequentialGraphViewer().clusteredGraphLevelNodeValueBarChart);
                        } else if (MySequentialGraphVars.getSequentialGraphViewer().selectedNode != null) {
                            MyViewerControlComponentUtil.setSelectedNodeNeighborValueBarChartToViewer();
                        } else if (MySequentialGraphVars.getSequentialGraphViewer().multiNodes != null) {
                            MyViewerControlComponentUtil.setSharedNodeLevelNodeValueBarChart();
                        } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthSelecter.getSelectedIndex() > 0 && MySequentialGraphVars.getSequentialGraphViewer().vc.depthNeighborNodeTypeSelector.getSelectedIndex() == 0) {
                            MyViewerControlComponentUtil.setDepthNodeBarChartToViewer();
                        } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthSelecter.getSelectedIndex() > 0 && MySequentialGraphVars.getSequentialGraphViewer().vc.depthNeighborNodeTypeSelector.getSelectedIndex() > 0) {
                            MyViewerControlComponentUtil.setDepthNeighborNodeBarChartToViewer();
                        } else {
                            MyViewerControlComponentUtil.setNodeBarChartToViewer();
                        }
                    } else {
                        MyViewerControlComponentUtil.removeBarChartsFromViewer();
                        MyViewerControlComponentUtil.removeSharedNodeValueBarCharts();
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
                        if (edgeValueSelecter.getSelectedIndex() < 2) {
                            MyViewerControlComponentUtil.removeEdgeValueBarChartFromViewer();
                        } else if (MySequentialGraphVars.getSequentialGraphViewer().isClustered) {
                            MyViewerControlComponentUtil.removeEdgeValueBarChartFromViewer();
                            MySequentialGraphVars.getSequentialGraphViewer().clusteredGraphLevelEdgeValueBarChart = new MyClusteredGraphLevelEdgeValueBarChart();
                            MySequentialGraphVars.getSequentialGraphViewer().add(MySequentialGraphVars.getSequentialGraphViewer().clusteredGraphLevelEdgeValueBarChart);
                        } else if (MySequentialGraphVars.getSequentialGraphViewer().multiNodes != null && MySequentialGraphVars.getSequentialGraphViewer().multiNodes.size() > 0) {
                            MyViewerControlComponentUtil.setShareNodeLevelEdgeValueBarChartToViewer();
                        } else if (depthSelecter.getSelectedIndex() > 0) {
                            if (depthNeighborNodeTypeSelector.getSelectedIndex() > 0) {
                                MyViewerControlComponentUtil.removeEdgeValueBarChartFromViewer();
                                MySequentialGraphVars.getSequentialGraphViewer().graphLelvelEdgeValueBarChart = new MyGraphLevelEdgeValueBarChart();
                                MySequentialGraphVars.getSequentialGraphViewer().graphLelvelEdgeValueBarChart.setEdgeValueBarChartForDepthNodes();
                                MySequentialGraphVars.getSequentialGraphViewer().add(MySequentialGraphVars.getSequentialGraphViewer().graphLelvelEdgeValueBarChart);
                            }
                        } else if (MySequentialGraphVars.getSequentialGraphViewer().selectedNode != null) {
                            MyViewerControlComponentUtil.setSingleNodeLevelEdgeBarChartToViewer();
                        } else {
                            MyViewerControlComponentUtil.setEdgeBarChartToViewer();
                        }
                    } else {
                        if (MySequentialGraphVars.getSequentialGraphViewer().graphLelvelEdgeValueBarChart != null) {
                            MySequentialGraphVars.getSequentialGraphViewer().remove(MySequentialGraphVars.getSequentialGraphViewer().graphLelvelEdgeValueBarChart);
                        }
                    }
                    MySequentialGraphVars.getSequentialGraphViewer().revalidate();
                    MySequentialGraphVars.getSequentialGraphViewer().repaint();
                }
            }).start();
        } else if (ae.getSource() == edgeLabelSelecter) {
            new Thread(new Runnable() {
                @Override public void run() {

                }
            }).start();
        } else if (ae.getSource() == nodeLabelSelecter) {
            new Thread(new Runnable() {
                @Override public void run() {
                    MyViewerControlComponentUtil.showNodeLabel();
                }
            }).start();
        } else if (ae.getSource() == clusteringSelector) {
            new Thread(new Runnable() {
                @Override public void run() {
                    if (clusteringSelector.getSelectedIndex() == 1) {
                        if (MyClusteringConfig.instances++ == 0) {
                            edgeValueBarChart.setText("C. E. V. B");
                            nodeValueBarChart.setText("C. N. V. B");
                            MyViewerControlComponentUtil.setDefaultViewerLook();
                            MyClusteringConfig clusteringConfig = new MyClusteringConfig();
                        }
                    } else if (clusteringSelector.getSelectedIndex() == 0) {
                        if (MySequentialGraphVars.getSequentialGraphViewer().isClustered) {
                            MyViewerControlComponentUtil.setDefaultViewerLook();
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
                    //MyViewerControlComponentUtil.setDefaultViewerLook();
                    nodeValueBarChart.setSelected(false);
                    pb.updateValue(40, 100);
                    MyDepthNodeUtil.setDepthNodeNeighbors();
                    pb.updateValue(70, 100);
                    MySequentialGraphVars.app.getSequentialGraphDashboard().setDepthNodeDashBoard();
                    pb.updateValue(85, 100);
                    updateNodeTable();
                    MyViewerControlComponentUtil.adjustDepthNodeValueMenu();
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
                    MyViewerControlComponentUtil.setExcludeSymbolOption();
                    pb.updateValue(30, 100);
                    MyDepthNodeUtil.setSelectedSingleDepthNodeNeighbors();
                    pb.updateValue(65, 100);
                    MyViewerControlComponentUtil.setBottomCharts();
                    pb.updateValue(95, 100);
                    vTxtStat.setTextStatistics();
                    updateTableInfos();
                    MyViewerControlComponentUtil.adjustDepthNodeValueMenu();
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

                    MyViewerControlComponentUtil.setBottomCharts();
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
                        MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.removeActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
                        MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.setSelectedIndex(0);
                        MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.addActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
                        return;
                    }

                    MySequentialGraphVars.getSequentialGraphViewer().edgeValName = edgeValueSelecter.getSelectedItem().toString().replaceAll(" ", "");
                    MyProgressBar pb = new MyProgressBar(false);
                    pb.updateValue(10, 100);
                    MyEdgeUtil.setEdgeValue();
                    pb.updateValue(50, 100);
                    pb.updateValue(85, 100);
                    MyViewerControlComponentUtil.setBottomCharts();
                    pb.updateValue(95, 100);
                    vTxtStat.setTextStatistics();
                    updateNodeTable();
                    pb.updateValue(100, 100);
                    pb.dispose();
                    edgeValueSelecter.setToolTipText("EDGE VALUE: " + edgeValueSelecter.getSelectedItem().toString());

                    MySequentialGraphVars.getSequentialGraphViewer().revalidate();
                    MySequentialGraphVars.getSequentialGraphViewer().repaint();
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
                        tableTabbedPane.setEnabledAt(3, true);

                        MyViewerControlComponentUtil.setDefaultViewerLook();
                        return;
                    } else if (MySequentialGraphVars.getSequentialGraphViewer().selectedNode != null) {
                        clusteringSectorLabel.setEnabled(false);
                        clusteringSelector.setEnabled(false);

                        if (depthSelecter.getSelectedIndex() == 0) {
                            MyViewerControlComponentUtil.setDefaultViewerLook();
                            tableTabbedPane.setEnabledAt(1, true);
                            tableTabbedPane.setEnabledAt(2, true);
                            tableTabbedPane.setEnabledAt(3, true);
                        } else {
                            nodeValueBarChart.setText("DEPTH N. V. B.");
                            tableTabbedPane.setEnabledAt(1, false);
                            tableTabbedPane.setEnabledAt(2, false);
                            tableTabbedPane.setEnabledAt(3, false);
                            MyViewerControlComponentUtil.removeBarChartsFromViewer();
                            MyViewerControlComponentUtil.removeEdgeValueBarChartFromViewer();
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
                            MyViewerControlComponentUtil.setSelectedNodeNeighborNodeTypeOption();
                            MyViewerControlComponentUtil.setBottomCharts();
                            MySequentialGraphVars.getSequentialGraphViewer().selectedNode = null;
                        }
                    } else if (selectedNodeNeighborNodeTypeSelector.isShowing()) {
                        MySequentialGraphVars.currentGraphDepth = Integer.parseInt(depthSelecter.getSelectedItem().toString().trim());
                        clusteringSectorLabel.setEnabled(false);
                        clusteringSelector.setEnabled(false);
                        tableTabbedPane.setEnabledAt(1, false);
                        tableTabbedPane.setEnabledAt(2, false);
                        tableTabbedPane.setEnabledAt(3, false);

                        nodeValueBarChart.setText("DEPTH N. V. B.");
                        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
                        for (MyNode n : nodes) {
                            if (!depthNodeNameSet.contains(n.getName())) {
                                n.setCurrentValue(0f);
                            }
                        }
                        updateNodeTable();
                        MyViewerControlComponentUtil.setSelectedNodeNeighborNodeTypeOption();
                        MyViewerControlComponentUtil.setSelectedNodeVisibleOnly();
                        MyViewerControlComponentUtil.setBottomCharts();
                        MySequentialGraphVars.getSequentialGraphViewer().revalidate();
                        MySequentialGraphVars.getSequentialGraphViewer().repaint();
                    } else {
                        MyProgressBar pb = new MyProgressBar(false);
                        MySequentialGraphVars.currentGraphDepth = Integer.parseInt(depthSelecter.getSelectedItem().toString().trim());
                        clusteringSectorLabel.setEnabled(false);
                        clusteringSelector.setEnabled(false);
                        tableTabbedPane.setEnabledAt(1, false);
                        tableTabbedPane.setEnabledAt(2, false);
                        tableTabbedPane.setEnabledAt(3, false);
                        nodeValueBarChart.setText("DEPTH N. V. B.");
                        MyViewerControlComponentUtil.setDepthNodeNameSet();
                        MyViewerControlComponentUtil.setDepthNodeNeighborNodeTypeOption();
                        MyDepthNodeUtil.setDepthNodeValue();
                        MySequentialGraphVars.app.getSequentialGraphDashboard().setDepthNodeDashBoard();
                        vTxtStat.setTextStatistics();
                        updateNodeTable();
                        pb.updateValue(100, 100);
                        pb.dispose();
                        MySequentialGraphVars.getSequentialGraphViewer().revalidate();
                        MySequentialGraphVars.getSequentialGraphViewer().repaint();
                    }
                    MyViewerControlComponentUtil.adjustDepthNodeValueMenu();
                }
            }).start();
        }
    }



}


