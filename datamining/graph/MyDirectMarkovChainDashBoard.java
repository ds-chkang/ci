package datamining.graph;

import datamining.graph.barcharts.MyEdgeValueBarChart;
import datamining.graph.multilevel.*;
import datamining.graph.path.MyDepthFirstGraphPathSercher;
import datamining.graph.singlelevel.*;
import datamining.graph.toplevel.*;
import datamining.utils.MyMathUtil;
import datamining.utils.message.MyMessageUtil;
import datamining.utils.system.MySysUtil;
import datamining.utils.system.MyVars;
import datamining.utils.table.MyTableUtil;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class MyDirectMarkovChainDashBoard
extends JPanel
implements ActionListener {

    protected MyMultiNodeLevelPredecessorValueDistributionLineChart multiNodeLevelPredecessorValueDistributionLineChart;
    protected MyMultiNodeLevelSuccessorValueDistributionLineChart multiNodeLevelSuccessorValueDistributionLineChart;
    protected MyMultiNodeLevelPredecessorEdgeValueDistributionLineChart multiNodeLevelPredecessorEdgeValueDistributionLineChart;
    protected MyMultiNodeLevelSuccessorEdgeValueDistributionLineChart multiNodeLevelSuccessorEdgeValueDistributionLineChart;
    protected MyMultiNodeLevelSharedPredecessorEdgeValueDistributionLineChart multiNodeLevelSharedPredecessorsEdgeValueDistributionChart;
    protected MyMultiNodeLevelSharedSuccessorEdgeValueDistributionLineChart multiNodeLevelSharedSuccessorEdgeValueDistributionLineChart;

    public MyDirectGraphTextStatisticsPanel directGraphTextStatisticsPanel;

    protected MyTopLevelAverageShortestReachDistributionLineChart topLevelAverageShortestReachDistributionLineChart;
    protected MyTopLevelNodeValueDistribution topLevelNodeValueDistribution;
    protected MyTopLevelEdgeValueDistribution topLevelEdgeValueDistribution;
    protected MyTopLevelPredecessorCountDistribution topLevelPredecessorCountDistribution;
    protected MyTopLevelSuccessorCountDistribution topLevelSuccessorCountDistribution;
    protected MyTopLevelPredecessorEdgeValueDistribution topLevelPredecessorEdgeValueDistribution;
    protected MyTopLevelSuccessorEdgeValueDistribution topLevelSuccessorEdgeValueDistribution;

    protected MyNodeLevelPredecessorEdgeValueDistribution nodeLevelPredecessorEdgeValueDistribution;
    protected MyNodeLevelSuccessorEdgeValueDistribution nodeLevelSuccessorEdgeValueDistribution;
    protected MyNodeLevelPredecessorValueDistribution nodeLevelPredecessorValueDistribution;
    protected MyNodeLevelSuccessorValueDistribution nodeLevelSuccessorValueDistribution;
    protected MyNodeLevelEdgeValueDistribution nodeLevelEdgeValueDistribution;

    public JTable nodeListTable;
    public JTable currentNodeListTable;
    public JTable statTable;
    public JTable edgeTable;
    public JTable pathFromTable;
    public JTable pathToTable;
    public MyDirectMarkovChainDashBoard() {}

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

        Collection<MyDirectNode> nodes = MyVars.directMarkovChain.getVertices();
        int i = 0;
        for (MyDirectNode n : nodes) {
            fromTableModel.addRow(new String[]{String.valueOf(++i), n.getName()});
        }

        pathFromTable.setRowHeight(19);
        pathFromTable.setBackground(Color.WHITE);
        pathFromTable.setFont(MyVars.f_pln_12);
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

        i = 0;
        for (MyDirectNode n : nodes) {
            toTableModel.addRow(new String[]{String.valueOf(++i), n.getName()});
        }

        pathToTable.setRowHeight(19);
        pathToTable.setBackground(Color.WHITE);
        pathToTable.setFont(MyVars.f_pln_12);
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
                            MyMessageUtil.showInfoMsg(MyVars.main, "Select a path option.");
                        } else if (pathMenu.getSelectedIndex() == 1) {
                            if (pathFromTable.getSelectedRow() >= 0 && pathToTable.getSelectedRow() >= 0) {
                                String fromTableNodeName = pathFromTable.getValueAt(pathFromTable.getSelectedRow(), 1).toString();
                                String toTableNodeName = pathToTable.getValueAt(pathToTable.getSelectedRow(), 1).toString();
                                if (fromTableNodeName.equals(toTableNodeName)) {
                                    MyMessageUtil.showInfoMsg("Choose different nodes to find paths.");
                                    return;
                                }
                                MyDepthFirstGraphPathSercher betweenPathGraphDepthFirstSearch = new MyDepthFirstGraphPathSercher();
                                betweenPathGraphDepthFirstSearch.decorate();
                                betweenPathGraphDepthFirstSearch.run(fromTableNodeName, toTableNodeName);
                                if (betweenPathGraphDepthFirstSearch.integratedGraph.getVertexCount() == 0) {
                                    betweenPathGraphDepthFirstSearch.dispose();
                                    MyMessageUtil.showInfoMsg("There are no reachable paths between [" + fromTableNodeName + "] AND [" + toTableNodeName + "]");
                                    return;
                                }
                                System.out.println(betweenPathGraphDepthFirstSearch.integratedGraph.getVertexCount());
                                betweenPathGraphDepthFirstSearch.setAlwaysOnTop(true);
                                betweenPathGraphDepthFirstSearch.setVisible(true);
                                betweenPathGraphDepthFirstSearch.setAlwaysOnTop(false);
                            } else {
                                MyMessageUtil.showInfoMsg(MyVars.main, "Two nodes are required for the operation.");
                            }
                        } else if (pathMenu.getSelectedIndex() == 2) {
                            if (pathFromTable.getSelectedRow() >= 0 && pathToTable.getSelectedRow() >= 0) {
                                String fromTableNodeName = pathFromTable.getValueAt(pathFromTable.getSelectedRow(), 1).toString();
                                String toTableNodeName = pathToTable.getValueAt(pathToTable.getSelectedRow(), 1).toString();
                                MyDirectNode fromNode = (MyDirectNode) MyVars.directMarkovChain.vRefs.get(fromTableNodeName);
                                MyDirectNode toNode = (MyDirectNode) MyVars.directMarkovChain.vRefs.get(toTableNodeName);
                                String shortestPathLength = MyMathUtil.getCommaSeperatedNumber(MySysUtil.getUnWeightedBetweenNodeShortestPathLength(fromNode, toNode));
                                try {
                                    Thread.sleep(500);
                                    if (shortestPathLength.equals("0")) {
                                        MyMessageUtil.showInfoMsg(MyVars.main, "[" + toTableNodeName + "] is unreachable from [" + fromTableNodeName + "]");
                                    } else {
                                        MyMessageUtil.showInfoMsg(MyVars.main, "Shortest Path Length Between [" + fromTableNodeName + "] and [" + toTableNodeName + "] is " + shortestPathLength);
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            } else {
                                MyMessageUtil.showInfoMsg(MyVars.main, "Two nodes are required for the operation.");
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

    private JPanel setNodeLevelCharts() {
        this.nodeLevelPredecessorEdgeValueDistribution = new MyNodeLevelPredecessorEdgeValueDistribution();
        this.nodeLevelSuccessorEdgeValueDistribution = new MyNodeLevelSuccessorEdgeValueDistribution();
        this.nodeLevelPredecessorValueDistribution = new MyNodeLevelPredecessorValueDistribution();
        this.nodeLevelSuccessorValueDistribution = new MyNodeLevelSuccessorValueDistribution();
        this.nodeLevelEdgeValueDistribution = new MyNodeLevelEdgeValueDistribution();

        JPanel nodeLevelStatPanel = new JPanel();
        nodeLevelStatPanel.setBackground(Color.WHITE);
        nodeLevelStatPanel.setLayout(new GridLayout(5, 1));

        nodeLevelStatPanel.add(this.nodeLevelPredecessorValueDistribution);
        nodeLevelStatPanel.add(this.nodeLevelSuccessorValueDistribution);
        nodeLevelStatPanel.add(this.nodeLevelEdgeValueDistribution);
        nodeLevelStatPanel.add(this.nodeLevelPredecessorEdgeValueDistribution);
        nodeLevelStatPanel.add(this.nodeLevelSuccessorEdgeValueDistribution);

        return nodeLevelStatPanel;
    }

    private JPanel setMultiNodeLevelCharts() {
        this.multiNodeLevelPredecessorValueDistributionLineChart = new MyMultiNodeLevelPredecessorValueDistributionLineChart();
        this.multiNodeLevelSuccessorValueDistributionLineChart = new MyMultiNodeLevelSuccessorValueDistributionLineChart();
        this.multiNodeLevelPredecessorEdgeValueDistributionLineChart = new MyMultiNodeLevelPredecessorEdgeValueDistributionLineChart();
        this.multiNodeLevelSuccessorEdgeValueDistributionLineChart = new MyMultiNodeLevelSuccessorEdgeValueDistributionLineChart();
        this.multiNodeLevelSharedPredecessorsEdgeValueDistributionChart = new MyMultiNodeLevelSharedPredecessorEdgeValueDistributionLineChart();
        this.multiNodeLevelSharedSuccessorEdgeValueDistributionLineChart = new MyMultiNodeLevelSharedSuccessorEdgeValueDistributionLineChart();

        JPanel multinodeLevelStatPanel = new JPanel();
        multinodeLevelStatPanel.setBackground(Color.WHITE);
        multinodeLevelStatPanel.setLayout(new GridLayout(6, 1));

        multinodeLevelStatPanel.add(this.multiNodeLevelPredecessorValueDistributionLineChart);
        multinodeLevelStatPanel.add(this.multiNodeLevelSuccessorValueDistributionLineChart);
        multinodeLevelStatPanel.add(this.multiNodeLevelPredecessorEdgeValueDistributionLineChart);
        multinodeLevelStatPanel.add(this.multiNodeLevelSuccessorEdgeValueDistributionLineChart);
        multinodeLevelStatPanel.add(this.multiNodeLevelSharedPredecessorsEdgeValueDistributionChart);
        multinodeLevelStatPanel.add(this.multiNodeLevelSharedSuccessorEdgeValueDistributionLineChart);

        return multinodeLevelStatPanel;
    }

    public JPanel setTopLevelCharts() {
        this.topLevelNodeValueDistribution = new MyTopLevelNodeValueDistribution();
        this.topLevelEdgeValueDistribution = new MyTopLevelEdgeValueDistribution();
        this.topLevelPredecessorCountDistribution = new MyTopLevelPredecessorCountDistribution();
        this.topLevelSuccessorCountDistribution = new MyTopLevelSuccessorCountDistribution();
        this.topLevelPredecessorEdgeValueDistribution = new MyTopLevelPredecessorEdgeValueDistribution();
        this.topLevelSuccessorEdgeValueDistribution = new MyTopLevelSuccessorEdgeValueDistribution();
        this.topLevelAverageShortestReachDistributionLineChart = new MyTopLevelAverageShortestReachDistributionLineChart();

        JPanel graphStatPanel = new JPanel();
        graphStatPanel.setBackground(Color.WHITE);
        graphStatPanel.setLayout(new GridLayout(7, 1));
        graphStatPanel.add(this.topLevelNodeValueDistribution);
        graphStatPanel.add(this.topLevelPredecessorCountDistribution);
        graphStatPanel.add(this.topLevelSuccessorCountDistribution);
        graphStatPanel.add(this.topLevelEdgeValueDistribution);
        graphStatPanel.add(this.topLevelPredecessorEdgeValueDistribution);
        graphStatPanel.add(this.topLevelSuccessorEdgeValueDistribution);
        graphStatPanel.add(this.topLevelAverageShortestReachDistributionLineChart);

        return graphStatPanel;
    }

    public JTabbedPane topLevelTabbedPane;
    public JTabbedPane nodeLevelTabbedPane;
    public JTabbedPane multiNodeLevelTabbedPane;

    private JPanel setTopLevelTabPane() {
        this.topLevelTabbedPane = new JTabbedPane();

        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout(1,1));
        tablePanel.setBackground(Color.WHITE);

        this.topLevelTabbedPane.setFont(MyVars.tahomaPlainFont11);
        this.topLevelTabbedPane.setFocusable(false);
        this.topLevelTabbedPane.addTab("N.",  null, setNodeTable(), "NODES FOR THE CURRENT GRAPH");
        this.topLevelTabbedPane.addTab("E.",  null, setEdgeTable(), "EDGES FOR THE CURRENT GRAPH");
        this.topLevelTabbedPane.addTab("S.",  null, setTopLevelStatTable(), "STATISTICAL PROPERTIES FOR THE CURRENT GRAPH");
        tablePanel.add(this.topLevelTabbedPane, BorderLayout.CENTER);
        return tablePanel;
    }

    private JTabbedPane setNodeLevelTabPane() {
        this.nodeLevelTabbedPane = new JTabbedPane();
        this.nodeLevelTabbedPane.setFocusable(false);
        this.nodeLevelTabbedPane.setOpaque(false);
        this.nodeLevelTabbedPane.setPreferredSize(new Dimension(160, 2000));
        this.nodeLevelTabbedPane.setFont(MyVars.tahomaPlainFont11);
        this.nodeLevelTabbedPane.addTab("N.", null, setNodeTable(), "NODES FOR THE CURRENT GRAPH");
        this.nodeLevelTabbedPane.addTab("E.", null, setEdgeTable(), "EDGES FOR THE CURRENT GRAPH");
        this.nodeLevelTabbedPane.addTab("S.", null, setNodeLevelStatTable(), "STATISTICAL PROPERTIES FOR THE CURRENT GRAPH");
        return this.nodeLevelTabbedPane;
    }

    private JTabbedPane setMultiNodeLevelTabPane() {
        this.multiNodeLevelTabbedPane = new JTabbedPane();
        this.multiNodeLevelTabbedPane.setFocusable(false);
        this.multiNodeLevelTabbedPane.setOpaque(false);
        this.multiNodeLevelTabbedPane.setPreferredSize(new Dimension(160, 2000));
        this.multiNodeLevelTabbedPane.setFont(MyVars.tahomaPlainFont11);
        this.multiNodeLevelTabbedPane.addTab("N.", null, setNodeTable(), "NODES FOR THE CURRENT GRAPH");
        this.multiNodeLevelTabbedPane.addTab("E.", null, setEdgeTable(), "EDGES FOR THE CURRENT GRAPH");
        this.multiNodeLevelTabbedPane.addTab("S.", null, setMultiNodeLevelStatTable(), "STATISTICAL PROPERTIES FOR THE CURRENT GRAPH");
        return this.multiNodeLevelTabbedPane;
    }

    public JTextField nodeListTableNodeTableNodeSearchTxt;

    private JPanel setNodeTable() {
        JPanel componentPanel = new JPanel();
        try {
            componentPanel.setLayout(new BorderLayout(3, 3));
            componentPanel.setBackground(Color.WHITE);

            JPanel tablePanel = new JPanel();
            tablePanel.setLayout(new GridLayout(2, 1));
            tablePanel.setBackground(Color.WHITE);

            JPanel bottomTablePanel = new JPanel();
            bottomTablePanel.setLayout(new BorderLayout(3, 3));
            bottomTablePanel.setBackground(Color.WHITE);

            String[] bottomTableColumns = {"NO.", "N.", "V."};
            String[][] bottomTableData = {};

            DefaultTableModel bottomTableModel = new DefaultTableModel(bottomTableData, bottomTableColumns);
            this.nodeListTable = new JTable(bottomTableModel) {
                public String getToolTipText(MouseEvent e) {
                    String tip = null;
                    Point p = e.getPoint();
                    int rowIndex = rowAtPoint(p);
                    int colIndex = columnAtPoint(p);
                    try {tip = getValueAt(rowIndex, colIndex).toString();
                    } catch (RuntimeException e1) {}
                    return tip;
                }
            };

            String[] toolTips = {"NO.", "NODE", "NODE VALUE"};
            MyToolTipHeader tooltipHeader = new MyToolTipHeader(this.nodeListTable.getColumnModel());
            tooltipHeader.setToolTipStrings(toolTips);
            this.nodeListTable.setTableHeader(tooltipHeader);

            Collection<MyDirectNode> bottomTableNodes = MyVars.directMarkovChain.getVertices();
            LinkedHashMap<String, Float> bottomTableSortedNodes = new LinkedHashMap<>();
            for (MyDirectNode n : bottomTableNodes) {
                bottomTableSortedNodes.put(n.getName(), n.getCurrentValue());
            }
            bottomTableSortedNodes = MySysUtil.sortMapByFloatValue(bottomTableSortedNodes);
            int i = -0;
            for (String n : bottomTableSortedNodes.keySet()) {
                bottomTableModel.addRow(
                        new String[]{
                                String.valueOf(++i),
                                n,
                                MySysUtil.formatAverageValue(MyMathUtil.oneDecimalFormat(bottomTableSortedNodes.get(n))).split("\\.")[0]
                        }
                );
            }

            this.nodeListTable.setRowHeight(20);
            this.nodeListTable.setBackground(Color.WHITE);
            this.nodeListTable.setFont(MyVars.f_pln_11);
            this.nodeListTable.getTableHeader().setFont(MyVars.tahomaBoldFont10);
            this.nodeListTable.getTableHeader().setOpaque(false);
            this.nodeListTable.getTableHeader().setBackground(new Color(0, 0, 0, 0f));
            this.nodeListTable.getColumnModel().getColumn(0).setPreferredWidth(35);
            this.nodeListTable.getColumnModel().getColumn(0).setMaxWidth(45);
            this.nodeListTable.getColumnModel().getColumn(1).setPreferredWidth(55);
            this.nodeListTable.getColumnModel().getColumn(2).setPreferredWidth(30);
            this.nodeListTable.getColumnModel().getColumn(2).setMaxWidth(35);
            this.nodeListTable.addComponentListener(new ComponentListener() {
                @Override public void componentResized(ComponentEvent e) {
                    new Thread(new Runnable() {
                        @Override public void run() {}
                    }).start();
                }
                @Override public void componentMoved(ComponentEvent e) {}
                @Override public void componentShown(ComponentEvent e) {}
                @Override public void componentHidden(ComponentEvent e) {}
            });

            JButton bottomTableNodeSelectBtn = new JButton("SEL.");
            bottomTableNodeSelectBtn.setFont(MyVars.tahomaPlainFont10);
            bottomTableNodeSelectBtn.setFocusable(false);
            this.nodeListTableNodeTableNodeSearchTxt = new JTextField();
            this.nodeListTableNodeTableNodeSearchTxt.setBackground(Color.WHITE);
            this.nodeListTableNodeTableNodeSearchTxt.setBorder(BorderFactory.createLoweredSoftBevelBorder());
            JPanel bottomTableSearchAndSavePanel = MyTableUtil.searchAndSaveDataPanelForJTable2(this, this.nodeListTableNodeTableNodeSearchTxt, bottomTableNodeSelectBtn, bottomTableModel, nodeListTable);
            this.nodeListTableNodeTableNodeSearchTxt.setFont(MyVars.f_bold_12);
            this.nodeListTableNodeTableNodeSearchTxt.setToolTipText("TYPE A NODE NAME TO SEARCH");
            this.nodeListTableNodeTableNodeSearchTxt.setPreferredSize(new Dimension(90, 19));
            bottomTableNodeSelectBtn.setPreferredSize(new Dimension(50, 19));
            bottomTableNodeSelectBtn.setToolTipText("SELECT A NODE");
            bottomTableNodeSelectBtn.removeActionListener(this);
            bottomTableNodeSelectBtn.removeActionListener(this);
            bottomTableSearchAndSavePanel.remove(bottomTableNodeSelectBtn);

            this.nodeListTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            this.nodeListTable.setSelectionBackground(Color.LIGHT_GRAY);
            this.nodeListTable.setForeground(Color.BLACK);
            this.nodeListTable.setSelectionForeground(Color.BLACK);
            this.nodeListTable.setFocusable(false);
            this.nodeListTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent event) {
                    MyVars.currentThread = new Thread(new Runnable() {
                        @Override public void run() {
                            try {
                                if (nodeListTable.getSelectedRow() >= 0) {
                                    MyDirectNode n = (MyDirectNode) MyVars.directMarkovChain.vRefs.get(nodeListTable.getValueAt(nodeListTable.getSelectedRow(), 1));
                                    synchronized (n) {
                                        if (n != MyVars.getDirectGraphViewer().selectedSingleNode) {
                                            MyGraphNodeSelecter graphNodeSearcher = new MyGraphNodeSelecter();
                                            graphNodeSearcher.setSelectedNode(n);
                                        }
                                    }
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                    MyVars.currentThread.start();
                    try {
                        MyVars.currentThread.join();
                        MyVars.currentThread = null;
                    } catch (Exception ex) {
                        MyVars.currentThread = null;
                    }
                }
            });

            JScrollPane bottomTableScrollPane = new JScrollPane(this.nodeListTable);
            bottomTableScrollPane.setOpaque(false);
            bottomTableScrollPane.setBackground(new Color(0, 0, 0, 0f));
            bottomTableScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(7, 0));
            bottomTableScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 7));

            JPanel topTablePanel = new JPanel();
            topTablePanel.setLayout(new BorderLayout(3, 3));
            topTablePanel.setBackground(Color.WHITE);

            String[] topTableColumns = {"NO.", "NEIGHBOR N.", "V."};
            String[][] topTableData = {};
            DefaultTableModel topTableModel = new DefaultTableModel(topTableData, topTableColumns);
            this.currentNodeListTable = new JTable(topTableModel) {
                public String getToolTipText(MouseEvent e) {
                    String tip = null;
                    Point p = e.getPoint();
                    int rowIndex = rowAtPoint(p);
                    int colIndex = columnAtPoint(p);
                    try {tip = getValueAt(rowIndex, colIndex).toString();
                    } catch (RuntimeException e1) {}
                    return tip;
                }
            };

            String [] currentNodeTableHeaderTooltips = {"NO.", "NEIGHBOR NODE", "CURRENT NODE VALUE"};
            MyToolTipHeader currentNodeTooltipHeader = new MyToolTipHeader(this.currentNodeListTable.getColumnModel());
            currentNodeTooltipHeader.setToolTipStrings(currentNodeTableHeaderTooltips);
            this.currentNodeListTable.setTableHeader(currentNodeTooltipHeader);

            Collection<MyDirectNode> topTableNodes = MyVars.directMarkovChain.getVertices();
            LinkedHashMap<String, Float> topTableSortedNodes = new LinkedHashMap<>();
            for (MyDirectNode n : topTableNodes) {
                topTableSortedNodes.put(n.getName(), n.getCurrentValue());
            }

            topTableSortedNodes = MySysUtil.sortMapByFloatValue(topTableSortedNodes);
            int k = -0;
            for (String n : topTableSortedNodes.keySet()) {
                topTableModel.addRow(
                    new String[]{
                        String.valueOf(++k),
                        n,
                        MySysUtil.formatAverageValue(MyMathUtil.oneDecimalFormat(topTableSortedNodes.get(n))).split("\\.")[0]
                    });
            }

            this.currentNodeListTable.setRowHeight(20);
            this.currentNodeListTable.setBackground(Color.WHITE);
            this.currentNodeListTable.setFont(MyVars.f_pln_11);
            this.currentNodeListTable.getTableHeader().setFont(MyVars.tahomaBoldFont10);
            this.currentNodeListTable.getTableHeader().setOpaque(false);
            this.currentNodeListTable.getTableHeader().setBackground(new Color(0, 0, 0, 0f));
            this.currentNodeListTable.getColumnModel().getColumn(0).setPreferredWidth(35);
            this.currentNodeListTable.getColumnModel().getColumn(0).setMaxWidth(45);
            this.currentNodeListTable.getColumnModel().getColumn(1).setPreferredWidth(55);
            this.currentNodeListTable.getColumnModel().getColumn(2).setPreferredWidth(30);
            this.currentNodeListTable.getColumnModel().getColumn(2).setMaxWidth(35);
            this.currentNodeListTable.addComponentListener(new ComponentListener() {
                @Override public void componentResized(ComponentEvent e) {
                    new Thread(new Runnable() {
                        @Override public void run() {}
                    }).start();
                }
                @Override public void componentMoved(ComponentEvent e) {}
                @Override public void componentShown(ComponentEvent e) {}
                @Override public void componentHidden(ComponentEvent e) {}
            });

            JTextField topTableNodeSearchTxt = new JTextField();
            JButton topTableNodeSelectBtn = new JButton("SEL.");
            topTableNodeSelectBtn.setFont(MyVars.tahomaPlainFont10);
            topTableNodeSelectBtn.setFocusable(false);
            topTableNodeSearchTxt.setBackground(Color.WHITE);
            topTableNodeSearchTxt.setBorder(BorderFactory.createLoweredSoftBevelBorder());
            JPanel topTableSearchAndSavePanel = MyTableUtil.searchAndSaveDataPanelForJTable2(this, topTableNodeSearchTxt, topTableNodeSelectBtn, topTableModel, currentNodeListTable);
            topTableNodeSearchTxt.setFont(MyVars.f_bold_12);
            topTableNodeSearchTxt.setToolTipText("TYPE A NODE NAME TO SEARCH");
            topTableNodeSearchTxt.setPreferredSize(new Dimension(90, 19));
            topTableNodeSelectBtn.setPreferredSize(new Dimension(50, 19));
            topTableNodeSelectBtn.removeActionListener(this);
            topTableNodeSelectBtn.removeActionListener(this);
            topTableSearchAndSavePanel.remove(topTableNodeSelectBtn);

            this.currentNodeListTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            this.currentNodeListTable.setSelectionBackground(Color.LIGHT_GRAY);
            this.currentNodeListTable.setForeground(Color.BLACK);
            this.currentNodeListTable.setSelectionForeground(Color.BLACK);
            this.currentNodeListTable.setFocusable(false);
            this.currentNodeListTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            this.currentNodeListTable.setRowSorter(MyTableUtil.setJTableRowSorterWithTextField(topTableModel, topTableNodeSearchTxt));

            JScrollPane topTableScrollPane = new JScrollPane(this.currentNodeListTable);
            topTableScrollPane.setOpaque(false);
            topTableScrollPane.setBackground(new Color(0, 0, 0, 0f));
            topTableScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(7, 0));
            topTableScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 7));

            bottomTablePanel.add(bottomTableScrollPane, BorderLayout.CENTER);
            bottomTablePanel.add(bottomTableSearchAndSavePanel, BorderLayout.SOUTH);
            topTablePanel.add(topTableScrollPane, BorderLayout.CENTER);
            topTablePanel.add(topTableSearchAndSavePanel, BorderLayout.SOUTH);
            tablePanel.add(topTablePanel);
            tablePanel.add(bottomTablePanel);

            componentPanel.add(tablePanel, BorderLayout.CENTER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return componentPanel;
    }

    private boolean isEdgeTableSelected = false;
    private JPanel setEdgeTable() {
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout(3,3));
        tablePanel.setBackground(Color.WHITE);

        JPanel bottomTablePanel = new JPanel();
        bottomTablePanel.setLayout(new BorderLayout(3,3));
        bottomTablePanel.setBackground(Color.WHITE);

        String [] bottomTableColumns = {"NO.", "S.", "D.", "V."};
        String [][] bottomTableData = {};
        DefaultTableModel bottomTableModel = new DefaultTableModel(bottomTableData, bottomTableColumns);
        this.edgeTable = new JTable(bottomTableModel) {
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

        String [] toolTips = {"NO.", "SOURCE", "DEST", "CURRENT EDGE VALUE"};
        MyToolTipHeader tooltipHeader = new MyToolTipHeader(this.edgeTable.getColumnModel());
        tooltipHeader.setToolTipStrings(toolTips);
        this.edgeTable.setTableHeader(tooltipHeader);

        Collection<MyDirectEdge> edges = MyVars.directMarkovChain.getEdges();
        LinkedHashMap<String, Float> sortedEdges = new LinkedHashMap<>();
        for (MyDirectEdge e : edges) {
            String ename = e.getSource().getName() + "-" + e.getDest().getName();
            sortedEdges.put(ename, (float)e.getContribution());
        }
        sortedEdges = MySysUtil.sortMapByFloatValue(sortedEdges);
        int i=-0;
        for (String e : sortedEdges.keySet()) {
            String source = e.split("-")[0];
            String dest = e.split("-")[1];
            bottomTableModel.addRow(
                    new String[]{
                            "" + (++i),
                            source,
                            dest,
                            (MyVars.getDirectGraphViewer().directGraphViewerControlPanel.edgeValueComboBoxMenu.getSelectedIndex() == 0 ?
                                    "0" : MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(sortedEdges.get(e))))
                    });
        }

        edgeTable.setRowHeight(20);
        edgeTable.setBackground(Color.WHITE);
        edgeTable.setFont(MyVars.f_pln_11);
        edgeTable.getTableHeader().setFont(MyVars.tahomaBoldFont10);
        edgeTable.getTableHeader().setOpaque(false);
        edgeTable.getTableHeader().setBackground(new Color(0,0,0,0f));
        edgeTable.getColumnModel().getColumn(0).setPreferredWidth(25);
        edgeTable.getColumnModel().getColumn(1).setPreferredWidth(60);
        edgeTable.getColumnModel().getColumn(2).setPreferredWidth(60);
        edgeTable.getColumnModel().getColumn(3).setPreferredWidth(40);
        edgeTable.getColumnModel().getColumn(3).setMaxWidth(55);

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
                            if (!isEdgeTableSelected) {
                                isEdgeTableSelected = true;
                                MyDirectNode source = (MyDirectNode) MyVars.directMarkovChain.vRefs.get(edgeTable.getValueAt(edgeTable.getSelectedRow(), 1));
                                MyDirectNode dest = (MyDirectNode) MyVars.directMarkovChain.vRefs.get(edgeTable.getValueAt(edgeTable.getSelectedRow(), 2));
                                MyGraphNodeSelecter graphNodeSearcher = new MyGraphNodeSelecter();
                                graphNodeSearcher.setEdgeNodes(source, dest);
                                isEdgeTableSelected = false;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
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

    private String [][] topLevelStatTablePropertyValuePairs;
    private JPanel setTopLevelStatTable() {
        JPanel statTablePanel = new JPanel();
        statTablePanel.setLayout(new BorderLayout(3,3));
        statTablePanel.setBackground(Color.WHITE);

        String [] statTableColumns = {"PROPERTY", "V."};
        String [][] statTableData = {};
        DefaultTableModel statTableModel = new DefaultTableModel(statTableData, statTableColumns);
        this.topLevelStatTablePropertyValuePairs = updateTopLevelGraphStatToolTips();

        this.statTable = new JTable(statTableModel) {
            public String getToolTipText(MouseEvent e) {
                String tip = null;
                Point p = e.getPoint();
                int rowIndex = rowAtPoint(p);
                int colIndex = columnAtPoint(p);
                try {
                    tip = topLevelStatTablePropertyValuePairs[rowIndex][colIndex];
                } catch (RuntimeException e1) {}
                return tip;
            }
        };

        String [] toolTips = {"PROPERTY.", "VALUE"};
        MyToolTipHeader tooltipHeader = new MyToolTipHeader(this.statTable.getColumnModel());
        tooltipHeader.setToolTipStrings(toolTips);
        this.statTable.setTableHeader(tooltipHeader);
        this.statTable.setRowHeight(19);
        this.statTable.setBackground(Color.WHITE);
        this.statTable.setFont(MyVars.tahomaPlainFont11);
        this.statTable.setFocusable(false);
        this.statTable.getTableHeader().setFont(MyVars.tahomaBoldFont11);
        this.statTable.getTableHeader().setOpaque(false);
        this.statTable.getTableHeader().setBackground(new Color(0,0,0,0f));
        this.statTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        this.statTable.setPreferredSize(new Dimension(145, 1199));
        this.statTable.setForeground(Color.BLACK);
        this.statTable.getColumnModel().getColumn(1).setPreferredWidth(60);
        this.statTable.setSelectionForeground(Color.BLACK);
        this.statTable.setSelectionBackground(Color.LIGHT_GRAY);

        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"G.: ", topLevelStatTablePropertyValuePairs[0][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"NODES", topLevelStatTablePropertyValuePairs[1][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"EDGES", topLevelStatTablePropertyValuePairs[2][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"MAX. D.", topLevelStatTablePropertyValuePairs[3][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"MAX. NO. OF N.", topLevelStatTablePropertyValuePairs[4][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"MIN. NO. OF N.", topLevelStatTablePropertyValuePairs[5][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"NO. OF G.", topLevelStatTablePropertyValuePairs[6][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"AVG. NO. OF N. BY G.", topLevelStatTablePropertyValuePairs[7][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"G. WITH MAX. NO. OF N.", topLevelStatTablePropertyValuePairs[8][1].split("\\[")[0]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"G. WITH MIN. NO. OF N.", topLevelStatTablePropertyValuePairs[9][1].split("\\[")[0]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"R. N.", topLevelStatTablePropertyValuePairs[10][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"B. N.", topLevelStatTablePropertyValuePairs[11][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"G. N.", topLevelStatTablePropertyValuePairs[12][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"AVG. N. V.", topLevelStatTablePropertyValuePairs[13][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"MAX. N. V.", topLevelStatTablePropertyValuePairs[14][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"MIN. N. V.", topLevelStatTablePropertyValuePairs[15][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"STD. N. V.", topLevelStatTablePropertyValuePairs[16][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"MAX. P.", topLevelStatTablePropertyValuePairs[17][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"AVG. P.", topLevelStatTablePropertyValuePairs[18][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"MIN. P.", topLevelStatTablePropertyValuePairs[19][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"MAX. S.", topLevelStatTablePropertyValuePairs[20][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"AVG. S.", topLevelStatTablePropertyValuePairs[21][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"MIN. S.", topLevelStatTablePropertyValuePairs[22][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"MAX. E. V.", "0"});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"AVG. E. V.", "0"});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"MIN. E. V.", "0"});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"STD. E. V.", "0"});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"ISLAND N.", MyMathUtil.getCommaSeperatedNumber(MyVars.directMarkovChain.getIslandNodes())});

        JScrollPane graphStatTableScrollPane = new JScrollPane(this.statTable);
        graphStatTableScrollPane.setOpaque(false);
        graphStatTableScrollPane.setPreferredSize(new Dimension(150, 1200));
        graphStatTableScrollPane.setBackground(Color.WHITE);
        graphStatTableScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(6, 0));
        graphStatTableScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 6));

        statTablePanel.add(graphStatTableScrollPane, BorderLayout.CENTER);
        return statTablePanel;
    }

    private JPanel setMultiNodeLevelStatTable() {
        JPanel statTablePanel = new JPanel();
        statTablePanel.setLayout(new BorderLayout(3,3));
        statTablePanel.setBackground(Color.WHITE);

        String [][] nodeLevelStatTableToolTipPairs = {
                {"GRAPH NUMBER", MyMathUtil.getCommaSeperatedNumber(MyVars.directMarkovChain.getNodeGraphNumber(MyVars.getDirectGraphViewer().selectedSingleNode))},
                {"NODES", MyMathUtil.getCommaSeperatedNumber(MyVars.directMarkovChain.getVertexCount())},
                {"EDGES", MyMathUtil.getCommaSeperatedNumber(MyVars.directMarkovChain.getEdgeCount())},
                {"PREDECESSOR COUNT", MyMathUtil.getCommaSeperatedNumber(MyVars.getDirectGraphViewer().multiNodePredecessorSet.size())},
                {"SUCCESSOR COUNT", MyMathUtil.getCommaSeperatedNumber(MyVars.getDirectGraphViewer().multiNodeSuccessorSet.size())},
                {"SHARED PREDECESSOR COUNT", MyMathUtil.getCommaSeperatedNumber(MyVars.getDirectGraphViewer().multiNodeSharedPredecessorSet.size())},
                {"SHARED SUCCESSOR COUNT", MyMathUtil.getCommaSeperatedNumber(MyVars.getDirectGraphViewer().multiNodeSharedSuccessorSet.size())},
                {"MAX. IN-EDGE VALUE", MyMathUtil.getCommaSeperatedNumber((long)MyVars.directMarkovChain.getMaxInEdgeValueForSelectedMultiNodes())},
                {"AVG. IN-EDGE VALUE", MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyVars.directMarkovChain.getAverageInEdgeValueForSelectedMultiNodes()))},
                {"MIN. IN-EDGE VALUE", MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyVars.directMarkovChain.getMinInEdgeValueForSelectedMultiNodes()))},
                {"IN-EDGE VALUE STANDARD DEVIATION", MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyVars.directMarkovChain.getInEdgValueStandardDeviationForSelectedMultiNode()))},
                {"MAX. OUT-EDGE VALUE", MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyVars.directMarkovChain.getMaxOutEdgeValueForSelectedMultiNodes()))},
                {"AVG. OUT-EDGE VALUE", MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyVars.directMarkovChain.getAverageOutEdgeValueForSelectedMultiNodes()))},
                {"MIN. OUT-EDGE VALUE", MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyVars.directMarkovChain.getMinOutEdgeValueForSelectedMultiNodes()))},
                {"OUT-EDGE VALUE STANDARD DEVIATION", MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyVars.directMarkovChain.getOutEdgValueStandardDeviationForSelectedMultiNode()))}
        };

        String [] statTableColumns = {"PROPERTY", "V."};
        String [][] statTableData = {};
        DefaultTableModel statTableModel = new DefaultTableModel(statTableData, statTableColumns);

        this.statTable = new JTable(statTableModel) {
            public String getToolTipText(MouseEvent e) {
                String tip = null;
                Point p = e.getPoint();
                int rowIndex = rowAtPoint(p);
                int colIndex = columnAtPoint(p);
                try {
                    tip = nodeLevelStatTableToolTipPairs[rowIndex][colIndex];
                } catch (RuntimeException e1) {}
                return tip;
            }
        };

        String [] toolTips = {"PROPERTY.", "VALUE"};
        MyToolTipHeader tooltipHeader = new MyToolTipHeader(this.statTable.getColumnModel());
        tooltipHeader.setToolTipStrings(toolTips);
        this.statTable.setTableHeader(tooltipHeader);
        this.statTable.setRowHeight(19);
        this.statTable.setBackground(Color.WHITE);
        this.statTable.setFont(MyVars.tahomaPlainFont11);
        this.statTable.setFocusable(false);
        this.statTable.getTableHeader().setFont(MyVars.tahomaBoldFont11);
        this.statTable.getTableHeader().setOpaque(false);
        this.statTable.getTableHeader().setBackground(new Color(0,0,0,0f));
        this.statTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        this.statTable.setPreferredSize(new Dimension(145, 1199));
        this.statTable.setForeground(Color.BLACK);
        this.statTable.getColumnModel().getColumn(1).setPreferredWidth(60);
        this.statTable.setSelectionForeground(Color.BLACK);
        this.statTable.setSelectionBackground(Color.LIGHT_GRAY);

        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"G. N.", nodeLevelStatTableToolTipPairs[0][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"NODES", nodeLevelStatTableToolTipPairs[1][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"EDGES", nodeLevelStatTableToolTipPairs[2][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"P. C.", nodeLevelStatTableToolTipPairs[3][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"S. C", nodeLevelStatTableToolTipPairs[4][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"MAX. IN-E. V.", nodeLevelStatTableToolTipPairs[5][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"AVG. IN-E. V.", nodeLevelStatTableToolTipPairs[6][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"MIN. IN-E. V.", nodeLevelStatTableToolTipPairs[7][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"STD. IN-E. V.", nodeLevelStatTableToolTipPairs[8][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"MAX. OUT-E. V.", nodeLevelStatTableToolTipPairs[9][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"AVG. OUT-E. V.", nodeLevelStatTableToolTipPairs[10][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"MIN. OUT-E. V.", nodeLevelStatTableToolTipPairs[11][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"STD. OUT-E. V.", nodeLevelStatTableToolTipPairs[12][1]});

        JScrollPane graphStatTableScrollPane = new JScrollPane(this.statTable);
        graphStatTableScrollPane.setOpaque(false);
        graphStatTableScrollPane.setPreferredSize(new Dimension(150, 1200));
        graphStatTableScrollPane.setBackground(Color.WHITE);
        graphStatTableScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(6, 0));
        graphStatTableScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 6));

        statTablePanel.add(graphStatTableScrollPane, BorderLayout.CENTER);
        return statTablePanel;
    }

    private JPanel setNodeLevelStatTable() {
        JPanel statTablePanel = new JPanel();
        statTablePanel.setLayout(new BorderLayout(3,3));
        statTablePanel.setBackground(Color.WHITE);

        String [][] nodeLevelStatTableToolTipPairs = {
                {"GRAPH NUMBER", MyMathUtil.getCommaSeperatedNumber(MyVars.directMarkovChain.getNodeGraphNumber(MyVars.getDirectGraphViewer().selectedSingleNode))},
                {"NODES", MyMathUtil.getCommaSeperatedNumber(MyVars.directMarkovChain.getVertexCount())},
                {"EDGES", MyMathUtil.getCommaSeperatedNumber(MyVars.directMarkovChain.getEdgeCount())},
                {"PREDECESSOR COUNT", MyMathUtil.getCommaSeperatedNumber(MyVars.directMarkovChain.getPredecessorCountForSelectedNode())},
                {"SUCCESSOR COUNT", MyMathUtil.getCommaSeperatedNumber(MyVars.directMarkovChain.getSuccessorCountForSelectedNode())},
                {"MAX. IN-EDGE VALUE", MyMathUtil.getCommaSeperatedNumber((long)MyVars.directMarkovChain.getMaxInEdgeValueForSelectedNode())},
                {"AVG. IN-EDGE VALUE", MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyVars.directMarkovChain.getAverageInEdgeValueForSelectedNode()))},
                {"MIN. IN-EDGE VALUE", MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyVars.directMarkovChain.getMinInEdgeValueForSelectedNode()))},
                {"IN-EDGE VALUE STANDARD DEVIATION", MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyVars.directMarkovChain.getInEdgValueStandardDeviationForSelectedNode()))},
                {"MAX. OUT-EDGE VALUE", MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyVars.directMarkovChain.getMaxOutEdgeValueForSelectedNode()))},
                {"AVG. OUT-EDGE VALUE", MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyVars.directMarkovChain.getAverageOutEdgeValueForSelectedNode()))},
                {"MIN. OUT-EDGE VALUE", MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyVars.directMarkovChain.getMinOutEdgeValueForSelectedNode()))},
                {"OUT-EDGE VALUE STANDARD DEVIATION", MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyVars.directMarkovChain.getOutEdgValueStandardDeviationForSelectedNode()))}
        };

        String [] statTableColumns = {"PROPERTY", "V."};
        String [][] statTableData = {};
        DefaultTableModel statTableModel = new DefaultTableModel(statTableData, statTableColumns);

        this.statTable = new JTable(statTableModel) {
            public String getToolTipText(MouseEvent e) {
                String tip = null;
                Point p = e.getPoint();
                int rowIndex = rowAtPoint(p);
                int colIndex = columnAtPoint(p);
                try {
                    tip = nodeLevelStatTableToolTipPairs[rowIndex][colIndex];
                } catch (RuntimeException e1) {}
                return tip;
            }
        };

        String [] toolTips = {"PROPERTY.", "VALUE"};
        MyToolTipHeader tooltipHeader = new MyToolTipHeader(this.statTable.getColumnModel());
        tooltipHeader.setToolTipStrings(toolTips);
        this.statTable.setTableHeader(tooltipHeader);
        this.statTable.setRowHeight(19);
        this.statTable.setBackground(Color.WHITE);
        this.statTable.setFont(MyVars.tahomaPlainFont11);
        this.statTable.setFocusable(false);
        this.statTable.getTableHeader().setFont(MyVars.tahomaBoldFont11);
        this.statTable.getTableHeader().setOpaque(false);
        this.statTable.getTableHeader().setBackground(new Color(0,0,0,0f));
        this.statTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        this.statTable.setPreferredSize(new Dimension(145, 1199));
        this.statTable.setForeground(Color.BLACK);
        this.statTable.getColumnModel().getColumn(1).setPreferredWidth(60);
        this.statTable.setSelectionForeground(Color.BLACK);
        this.statTable.setSelectionBackground(Color.LIGHT_GRAY);

        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"G. N.", nodeLevelStatTableToolTipPairs[0][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"NODES", nodeLevelStatTableToolTipPairs[1][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"EDGES", nodeLevelStatTableToolTipPairs[2][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"P. C.", nodeLevelStatTableToolTipPairs[3][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"S. C", nodeLevelStatTableToolTipPairs[4][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"MAX. IN-E. V.", nodeLevelStatTableToolTipPairs[5][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"AVG. IN-E. V.", nodeLevelStatTableToolTipPairs[6][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"MIN. IN-E. V.", nodeLevelStatTableToolTipPairs[7][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"STD. IN-E. V.", nodeLevelStatTableToolTipPairs[8][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"MAX. OUT-E. V.", nodeLevelStatTableToolTipPairs[9][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"AVG. OUT-E. V.", nodeLevelStatTableToolTipPairs[10][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"MIN. OUT-E. V.", nodeLevelStatTableToolTipPairs[11][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"STD. OUT-E. V.", nodeLevelStatTableToolTipPairs[12][1]});

        JScrollPane graphStatTableScrollPane = new JScrollPane(this.statTable);
        graphStatTableScrollPane.setOpaque(false);
        graphStatTableScrollPane.setPreferredSize(new Dimension(150, 1200));
        graphStatTableScrollPane.setBackground(Color.WHITE);
        graphStatTableScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(6, 0));
        graphStatTableScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 6));

        statTablePanel.add(graphStatTableScrollPane, BorderLayout.CENTER);
        return statTablePanel;
    }

    private String [][] updateTopLevelGraphStatToolTips() {
        String [][] topLevelStatTablePropertyValuePairs = {
                {"NO. OF GRAPHS", MyMathUtil.getCommaSeperatedNumber(MyVars.connectedComponentCountsByGraph.size())},
                {"NODES", MyMathUtil.getCommaSeperatedNumber(MyVars.directMarkovChain.getVertexCount())},
                {"EDGES", MyMathUtil.getCommaSeperatedNumber(MyVars.directMarkovChain.getEdgeCount())},
                {"MAX. DIAMETER", MyMathUtil.getCommaSeperatedNumber(MyVars.diameter)},
                {"MAX. NO. OF NODES", MyMathUtil.getCommaSeperatedNumber(MyVars.directMarkovChain.getMaxNumberofNodesAmongGraphs())},
                {"MIN. NO. OF NODES", MyMathUtil.getCommaSeperatedNumber(MyVars.directMarkovChain.getMinNumberofNodesAmongGraphs())},
                {"NO. OF GRAPHS", MyMathUtil.getCommaSeperatedNumber(MyVars.connectedComponentCountsByGraph.size())},
                {"AVG. NO. OF NODES BY GRAPH", MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyVars.directMarkovChain.getAverageConnectedNodeCountByGraph()))},
                {"GRAPH WITH MAX. NO OF NODES", MyVars.directMarkovChain.getGraphsWIthMaxNumberOfNodes()},
                {"GRAPH WITH MIN. NO OF NODES", MyVars.directMarkovChain.getGraphsWithMinNumberOfNodes()},
                {"RED NODES", MyMathUtil.getCommaSeperatedNumber(getRedNodeCount()) + "[" + MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(getRedNodePercent()*100)) + "]"},
                {"BLUE NODES", MyMathUtil.getCommaSeperatedNumber(getBlueNodeCount()) + "[" + MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(getBlueNodePercent()*100)) + "]"},
                {"GREEN NODES", MyMathUtil.getCommaSeperatedNumber(getGreenNodeCount()) + "[" + MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(getGreenNodePercent()*100)) + "]"},
                {"AVG. NODE VALUE", MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(Double.parseDouble(getAverageNodeValue())))},
                {"MAX. NODE VALUE", MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(getMaximumNodeValue()))},
                {"MIN. NODE VALUE", MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(getMinimumNodeValue()))},
                {"NODE VALUE STANDARD DEVIATION", MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(getNodeValueStandardDeviation()))},
                {"MAX. PREDECESSOR COUNT", MyMathUtil.getCommaSeperatedNumber(MyVars.directMarkovChain.getMaxPredecessorCount())},
                {"AVG. PREDECESSOR COUNT", MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(getAveragePredecessorCount()))},
                {"MIN. PREDECESSOR COUNT", MyMathUtil.getCommaSeperatedNumber(MyVars.directMarkovChain.getMinPredecessorCount())},
                {"MAX. SUCCESSOR COUNT", MyMathUtil.getCommaSeperatedNumber(MyVars.directMarkovChain.getMaxSuccessorCount())},
                {"AVG. SUCCESSOR COUNT", MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(getAverageSuccessorCount()))},
                {"MIN. SUCCESSOR COUNT", MyMathUtil.getCommaSeperatedNumber(MyVars.directMarkovChain.getMinSuccesosrCount())},
                {"AVG. EDGE VALUE", MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyVars.directMarkovChain.getAverageEdgeValue()))},
                {"MAX. EDGE VALUE", MyMathUtil.getCommaSeperatedNumber((long)MyVars.directMarkovChain.getMaxEdgeValue())},
                {"MIN. EDGE VALUE", MyMathUtil.getCommaSeperatedNumber((long)MyVars.directMarkovChain.getMinEdgeValue())},
                {"EDGE VALUE STANDARD DEVIATION", MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyVars.directMarkovChain.getEdgValueStandardDeviation()))},
                {"ISLAND NODES: ", MyMathUtil.getCommaSeperatedNumber(MyVars.directMarkovChain.getIslandNodes())}
        };
        return topLevelStatTablePropertyValuePairs;
    }


    public void setTopLevelDashboard(MyDirectMarkovChainViewer directGraphViewer) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                removeAll();
                setBackground(Color.WHITE);
                setLayout(new BorderLayout(1, 1));
                MyVars.getDirectGraphViewer().directGraphViewerControlPanel = new MyDirectGraphController();

                directGraphTextStatisticsPanel = new MyDirectGraphTextStatisticsPanel();
                JPanel bottomPanel = new JPanel();
                bottomPanel.setBackground(Color.WHITE);
                bottomPanel.setLayout(new BorderLayout(3,3));
                bottomPanel.add(directGraphTextStatisticsPanel, BorderLayout.CENTER);
                bottomPanel.add(MyVars.getDirectGraphViewer().directGraphViewerControlPanel.checkBoxControlPanel, BorderLayout.EAST);

                JPanel graphViewer = new JPanel();
                graphViewer.setOpaque(false);
                graphViewer.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.0f));
                graphViewer.setLayout(new BorderLayout(3, 3));
                graphViewer.add(directGraphViewer, BorderLayout.CENTER);
                graphViewer.add(bottomPanel, BorderLayout.SOUTH);

                JSplitPane nodeListGraphSplitPane = new JSplitPane();
                nodeListGraphSplitPane.setDividerLocation((int) (MySysUtil.getViewerWidth() * 0.105));
                nodeListGraphSplitPane.setDividerSize(5);
                nodeListGraphSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
                nodeListGraphSplitPane.setLeftComponent(setTopLevelTabPane());
                nodeListGraphSplitPane.setRightComponent(graphViewer);

                JPanel contentPanel = new JPanel();
                contentPanel.setBackground(Color.WHITE);
                contentPanel.setLayout(new BorderLayout(3,3));
                contentPanel.add(nodeListGraphSplitPane, BorderLayout.CENTER);
                contentPanel.add(MyVars.getDirectGraphViewer().directGraphViewerControlPanel, BorderLayout.NORTH);

                JSplitPane chartNodeListSplitPane = new JSplitPane();
                chartNodeListSplitPane.setDividerLocation((int) (MySysUtil.getViewerWidth() * 0.125));
                chartNodeListSplitPane.setDividerSize(5);
                chartNodeListSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
                chartNodeListSplitPane.setLeftComponent(setTopLevelCharts());
                chartNodeListSplitPane.setRightComponent(contentPanel);

                MyVars.main.addComponentListener(new ComponentAdapter() {
                    public void componentResized(ComponentEvent evt) {
                        nodeListGraphSplitPane.setDividerLocation((int) (MySysUtil.getViewerWidth() * 0.105));
                        chartNodeListSplitPane.setDividerLocation((int) (MySysUtil.getViewerWidth() * 0.125));
                    }
                });

                add(chartNodeListSplitPane, BorderLayout.CENTER);
                MyVars.main.revalidate();
                MyVars.main.repaint();
            }
        });
    }

    public void setSelectedNodeLevelDashBoard(MyDirectMarkovChainViewer directGraphViewer) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                removeAll();
                setBackground(Color.WHITE);
                setLayout(new BorderLayout(3, 3));

                directGraphTextStatisticsPanel = new MyDirectGraphTextStatisticsPanel();
                JPanel checkBoxControlPanel = new JPanel();
                checkBoxControlPanel.setBackground(Color.WHITE);
                checkBoxControlPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3,3));
                checkBoxControlPanel.add(MyVars.getDirectGraphViewer().directGraphViewerControlPanel.weightedNodeColorCheckBox);
                checkBoxControlPanel.add(MyVars.getDirectGraphViewer().directGraphViewerControlPanel.removeEdgeCheckBox);
                checkBoxControlPanel.add(MyVars.getDirectGraphViewer().directGraphViewerControlPanel.nodeValueCheckBox);
                checkBoxControlPanel.add(MyVars.getDirectGraphViewer().directGraphViewerControlPanel.edgeValueCheckBox);

                JPanel bottomPanel = new JPanel();
                bottomPanel.setBackground(Color.WHITE);
                bottomPanel.setLayout(new BorderLayout(3,3));
                bottomPanel.add(directGraphTextStatisticsPanel, BorderLayout.CENTER);
                bottomPanel.add(checkBoxControlPanel, BorderLayout.EAST);

                JPanel graphViewer = new JPanel();
                graphViewer.setOpaque(false);
                graphViewer.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.0f));
                graphViewer.setLayout(new BorderLayout(3, 3));

                MyVars.getDirectGraphViewer().directGraphViewerControlPanel = new MyDirectGraphController();
                graphViewer.add(MyVars.getDirectGraphViewer().directGraphViewerControlPanel, BorderLayout.NORTH);
                graphViewer.add(directGraphViewer, BorderLayout.CENTER);
                graphViewer.add(bottomPanel, BorderLayout.SOUTH);

                JSplitPane nodeListGraphSplitPane = new JSplitPane();
                nodeListGraphSplitPane.setDividerLocation((int) (MySysUtil.getViewerWidth() * 0.105));
                nodeListGraphSplitPane.setDividerSize(5);
                nodeListGraphSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
                nodeListGraphSplitPane.setLeftComponent(setNodeLevelTabPane());
                nodeListGraphSplitPane.setRightComponent(graphViewer);

                JPanel contentPanel = new JPanel();
                contentPanel.setBackground(Color.WHITE);
                contentPanel.setLayout(new BorderLayout(3,3));
                contentPanel.add(nodeListGraphSplitPane, BorderLayout.CENTER);
                contentPanel.add(MyVars.getDirectGraphViewer().directGraphViewerControlPanel, BorderLayout.NORTH);

                JSplitPane chartNodeListSplitPane = new JSplitPane();
                chartNodeListSplitPane.setDividerLocation((int) (MySysUtil.getViewerWidth() * 0.125));
                chartNodeListSplitPane.setDividerSize(5);
                chartNodeListSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
                chartNodeListSplitPane.setLeftComponent(setNodeLevelCharts());
                chartNodeListSplitPane.setRightComponent(contentPanel);

                MyVars.main.addComponentListener(new ComponentAdapter() {
                    public void componentResized(ComponentEvent evt) {
                        chartNodeListSplitPane.setDividerLocation((int) (MySysUtil.getViewerWidth() * 0.125));
                        nodeListGraphSplitPane.setDividerLocation((int) (MySysUtil.getViewerWidth() * 0.105));
                    }
                });

                updateRemainingNodeTable();
                updateEdgeTable();

                add(chartNodeListSplitPane, BorderLayout.CENTER);
                MyVars.main.revalidate();
                MyVars.main.repaint();
            }
        });
    }

    public void setMultiNodeLevelDashBoard(MyDirectMarkovChainViewer directGraphViewer) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                removeAll();
                setBackground(Color.WHITE);
                setLayout(new BorderLayout(3, 3));
                directGraphTextStatisticsPanel = new MyDirectGraphTextStatisticsPanel();

                JPanel checkBoxControlPanel = new JPanel();
                checkBoxControlPanel.setBackground(Color.WHITE);
                checkBoxControlPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3,3));
                checkBoxControlPanel.add(MyVars.getDirectGraphViewer().directGraphViewerControlPanel.weightedNodeColorCheckBox);
                checkBoxControlPanel.add(MyVars.getDirectGraphViewer().directGraphViewerControlPanel.removeEdgeCheckBox);
                checkBoxControlPanel.add(MyVars.getDirectGraphViewer().directGraphViewerControlPanel.nodeValueCheckBox);
                checkBoxControlPanel.add(MyVars.getDirectGraphViewer().directGraphViewerControlPanel.edgeValueCheckBox);

                JPanel bottomPanel = new JPanel();
                bottomPanel.setBackground(Color.WHITE);
                bottomPanel.setLayout(new BorderLayout(3,3));
                bottomPanel.add(directGraphTextStatisticsPanel, BorderLayout.CENTER);
                bottomPanel.add(checkBoxControlPanel, BorderLayout.EAST);

                JPanel graphViewer = new JPanel();
                graphViewer.setOpaque(false);
                graphViewer.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.0f));
                graphViewer.setLayout(new BorderLayout(3, 3));
                MyVars.getDirectGraphViewer().directGraphViewerControlPanel = new MyDirectGraphController();
                graphViewer.add(MyVars.getDirectGraphViewer().directGraphViewerControlPanel, BorderLayout.NORTH);
                graphViewer.add(directGraphViewer, BorderLayout.CENTER);
                graphViewer.add(bottomPanel, BorderLayout.SOUTH);

                JSplitPane nodeListGraphSplitPane = new JSplitPane();
                nodeListGraphSplitPane.setDividerLocation((int) (MySysUtil.getViewerWidth() * 0.105));
                nodeListGraphSplitPane.setDividerSize(5);
                nodeListGraphSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
                nodeListGraphSplitPane.setLeftComponent(setMultiNodeLevelTabPane());
                nodeListGraphSplitPane.setRightComponent(graphViewer);

                JPanel contentPanel = new JPanel();
                contentPanel.setBackground(Color.WHITE);
                contentPanel.setLayout(new BorderLayout(3,3));
                contentPanel.add(nodeListGraphSplitPane, BorderLayout.CENTER);
                contentPanel.add(MyVars.getDirectGraphViewer().directGraphViewerControlPanel, BorderLayout.NORTH);

                JSplitPane chartNodeListSplitPane = new JSplitPane();
                chartNodeListSplitPane.setDividerLocation((int) (MySysUtil.getViewerWidth() * 0.125));
                chartNodeListSplitPane.setDividerSize(5);
                chartNodeListSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
                chartNodeListSplitPane.setLeftComponent(setMultiNodeLevelCharts());
                chartNodeListSplitPane.setRightComponent(contentPanel);

                MyVars.main.addComponentListener(new ComponentAdapter() {
                    public void componentResized(ComponentEvent evt) {
                        nodeListGraphSplitPane.setDividerLocation((int) (MySysUtil.getViewerWidth() * 0.105));
                        chartNodeListSplitPane.setDividerLocation((int) (MySysUtil.getViewerWidth() * 0.125));
                    }
                });

                updateEdgeTable();
                updateRemainingNodeTable();

                add(chartNodeListSplitPane, BorderLayout.CENTER);
                MyVars.main.revalidate();
                MyVars.main.repaint();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public int getRedNodeCount() {
        Collection<MyDirectNode> ns = MyVars.directMarkovChain.getVertices();
        int cnt = 0;
        for (MyDirectNode n : ns) {
            if (n.getCurrentValue() > 0) {
                if (MyVars.directMarkovChain.getPredecessorCount( n) == 0 && MyVars.directMarkovChain.getSuccessorCount( n) > 0) {
                    cnt++;
                }
            }
        }
        return cnt;
    }

    public double getRedNodePercent() {
        Collection<MyDirectNode> ns = MyVars.directMarkovChain.getVertices();
        int cnt = 0;
        for (MyDirectNode n : ns) {
            if (n.getCurrentValue() > 0) {
                if (MyVars.directMarkovChain.getPredecessorCount( n) == 0 && MyVars.directMarkovChain.getSuccessorCount( n) > 0) {
                    cnt++;
                }
            }
        }
        return ((double)cnt/ns.size());
    }

    public int getBlueNodeCount() {
        Collection<MyDirectNode> ns = MyVars.directMarkovChain.getVertices();
        int cnt = 0;
        for (MyDirectNode n : ns) {
            if (n.getCurrentValue() > 0) {
                if (MyVars.directMarkovChain.getPredecessorCount(n) > 0 && MyVars.directMarkovChain.getSuccessorCount(n) > 0) {
                    cnt++;
                }
            }
        }
        return cnt;
    }

    public double getBlueNodePercent() {
        Collection<MyDirectNode> ns = MyVars.directMarkovChain.getVertices();
        int cnt = 0;
        for (MyDirectNode n : ns) {
            if (n.getCurrentValue() > 0) {
                if (MyVars.directMarkovChain.getPredecessorCount(n) > 0 && MyVars.directMarkovChain.getSuccessorCount(n) > 0) {
                    cnt++;
                }
            }
        }
        return ((double)cnt/ns.size());
    }

    public double getGreenNodePercent() {
        Collection<MyDirectNode> ns = MyVars.directMarkovChain.getVertices();
        int cnt = 0;
        for (MyDirectNode n : ns) {
            if (n.getCurrentValue() > 0) {
                if (MyVars.directMarkovChain.getPredecessorCount(n) > 0 && MyVars.directMarkovChain.getSuccessorCount(n) == 0) {
                    cnt++;
                }
            }
        }
        return ((double)cnt/ns.size());
    }

    public int getGreenNodeCount() {
        Collection<MyDirectNode> ns = MyVars.directMarkovChain.getVertices();
        int cnt = 0;
        for (MyDirectNode n : ns) {
            if (n.getCurrentValue() > 0) {
                if (MyVars.directMarkovChain.getPredecessorCount(n) > 0 && MyVars.directMarkovChain.getSuccessorCount(n) == 0) {
                    cnt++;
                }
            }
        }
        return cnt;
    }

    public double getMaximumNodeValue() {
        double maxValue = 0D;
        Collection<MyDirectNode> ns = MyVars.directMarkovChain.getVertices();
        for (MyDirectNode n : ns) {
            if (n.getCurrentValue() == 0) continue;
            if (maxValue < n.getCurrentValue()) {
                maxValue = n.getCurrentValue();
            }
        }
        return maxValue;
    }
    public double getMinimumNodeValue() {
        double minValue = 1000000000000D;
        Collection<MyDirectNode> ns = MyVars.directMarkovChain.getVertices();
        for (MyDirectNode n : ns) {
            if (n.getCurrentValue() > 0 && minValue > n.getCurrentValue()) {
                minValue = n.getCurrentValue();
            }
        }
        return minValue;
    }

    public String getAverageNodeValue() {
        Collection<MyDirectNode> ns = MyVars.directMarkovChain.getVertices();
        float totalVal = 0f;
        for (MyDirectNode n : ns) {
            if (n.getCurrentValue() > 0) {
                totalVal += n.getCurrentValue();
            }
        }
        return MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(totalVal/ns.size()));
    }

    public double getNodeValueStandardDeviation() {
        Collection<MyDirectNode> nodes = MyVars.directMarkovChain.getVertices();
        double sum = 0.00d;
        int numNodes = 0;
        for (MyDirectNode n : nodes) {
            if (n.getCurrentValue() == 0) {continue;}
            numNodes++;
            sum += n.getCurrentValue();
        }
        double mean = sum/numNodes;
        double std = 0.00D;
        for(MyDirectNode n : nodes) {std += Math.pow(n.getCurrentValue()-mean, 2);}
        return Math.sqrt(std/numNodes);
    }

    public double getAveragePredecessorCount() {
        long total = 0L;
        int ncnt = 0;
        Collection<MyDirectNode> nodes = MyVars.directMarkovChain.getVertices();
        for (MyDirectNode n : nodes) {
            if (n.getCurrentValue() == 0) continue;
            int val = MyVars.directMarkovChain.getPredecessorCount(n);
            if (val > 0) {
                total += val;
                ncnt++;
            }
        }
        return (double)total/ncnt;
    }

    public double getAverageSuccessorCount() {
        long total = 0L;
        int ncnt = 0;
        Collection<MyDirectNode> nodes = MyVars.directMarkovChain.getVertices();
        for (MyDirectNode n : nodes) {
            if (n.getCurrentValue() == 0) continue;
            int val = MyVars.directMarkovChain.getSuccessorCount(n);
            if (val > 0) {
                total += val;
                ncnt++;
            }
        }
        return (double)total/ncnt;
    }

    public void updateEdgeTable() {
        for (int i = edgeTable.getRowCount()-1; i >= 0; i--) {
            ((DefaultTableModel) edgeTable.getModel()).removeRow(i);
        }

        if (MyVars.getDirectGraphViewer().selectedSingleNode != null) {
            LinkedHashMap<String, Long> edgeMap = new LinkedHashMap<>();
            ArrayList<MyDirectEdge> edges = new ArrayList<>(MyVars.directMarkovChain.getInEdges(MyVars.getDirectGraphViewer().selectedSingleNode));
            edges.addAll(MyVars.directMarkovChain.getOutEdges(MyVars.getDirectGraphViewer().selectedSingleNode));
            for (MyDirectEdge e : edges) {
                String edgeName = e.getSource().getName() + "-" + e.getDest().getName();
                edgeMap.put(edgeName, (long) e.getCurrentValue());
            }

            edgeMap = MySysUtil.sortMapByLongValue(edgeMap);
            int i = 0;
            for (String edge : edgeMap.keySet()) {
                String [] edgePair = edge.split("-");
                ((DefaultTableModel) edgeTable.getModel()).addRow(
                        new String[]{
                                "" + (++i),
                                edgePair[0],
                                edgePair[1],
                                (MyVars.getDirectGraphViewer().directGraphViewerControlPanel.edgeValueComboBoxMenu.getSelectedIndex() == 0 || edgeMap.get(edge) == -1 ?
                                        "0" : MyMathUtil.getCommaSeperatedNumber(edgeMap.get(edge)))});
            }
        } else if (MyVars.getDirectGraphViewer().multiNodes != null) {
            LinkedHashMap<String, Float> edgeMap = new LinkedHashMap<>();
            for (MyDirectNode selectedNode : MyVars.getDirectGraphViewer().multiNodes) {
                Collection<MyDirectEdge> edges = MyVars.directMarkovChain.getInEdges(selectedNode);
                if (edges != null) {
                    for (MyDirectEdge e : edges) {
                        String edgeName = e.getSource().getName() + "-" + e.getDest().getName();
                        edgeMap.put(edgeName, e.getCurrentValue());
                    }
                }

                edges = MyVars.directMarkovChain.getOutEdges(selectedNode);
                if (edges != null) {
                    for (MyDirectEdge e : edges) {
                        String edgeName = e.getSource().getName() + "-" + e.getDest().getName();
                        edgeMap.put(edgeName, e.getCurrentValue());
                    }
                }
            }

            edgeMap = MySysUtil.sortMapByFloatValue(edgeMap);
            int i = 0;
            for (String edgeName : edgeMap.keySet()) {
                String [] edgePair = edgeName.split("-");
                float edgeValue = edgeMap.get(edgeName);
                ((DefaultTableModel) edgeTable.getModel()).addRow(
                        new String[]{
                                "" + (++i),
                                edgePair[0],
                                edgePair[1],
                                (MyVars.getDirectGraphViewer().directGraphViewerControlPanel.edgeValueComboBoxMenu.getSelectedIndex() == 0 || edgeMap.get(edgeName) == -1 ?
                                        "0" : MyMathUtil.getCommaSeperatedNumber((long)edgeValue))});
            }
        } else {
            LinkedHashMap<String, Float> edgeMap = new LinkedHashMap<>();
            Collection<MyDirectEdge> edges = MyVars.directMarkovChain.getEdges();
            for (MyDirectEdge e : edges) {
                String edgeName = e.getSource().getName() + "-" + e.getDest().getName();
                edgeMap.put(edgeName, e.getCurrentValue());

            }

            edgeMap = MySysUtil.sortMapByFloatValue(edgeMap);
            int i = 0;
            for (String edgeName : edgeMap.keySet()) {
                String [] edgePair = edgeName.split("-");
                float edgeValue = edgeMap.get(edgeName);
                ((DefaultTableModel) edgeTable.getModel()).addRow(
                        new String[]{
                                "" + (++i),
                                edgePair[0],
                                edgePair[1],
                                (MyVars.getDirectGraphViewer().directGraphViewerControlPanel.edgeValueComboBoxMenu.getSelectedIndex() == 0 ?
                                        "0" : MyMathUtil.getCommaSeperatedNumber((long)edgeValue))});
            }
        }
        edgeTable.revalidate();
        edgeTable.repaint();
    }

    public void updateRemainingNodeTable() {
        for (int i = currentNodeListTable.getRowCount()-1; i >= 0; i--) {
            ((DefaultTableModel) currentNodeListTable.getModel()).removeRow(i);
        }

        //for (int i = nodeListTable.getRowCount()-1; i >= 0; i--) {
        //    ((DefaultTableModel) nodeListTable.getModel()).removeRow(i);
        //}

        if (MyVars.getDirectGraphViewer().selectedSingleNode != null) {
            LinkedHashMap<String, Long> nodeMap = new LinkedHashMap<>();
            ArrayList<MyDirectNode> nodes = new ArrayList<>(MyVars.getDirectGraphViewer().selectedSingleNodePredecessorSet);
            nodes.addAll(MyVars.getDirectGraphViewer().selectedSingleNodeSuccessorSet);
            for (MyDirectNode n : nodes) {
                if (n.getCurrentValue() == 0) continue;
                nodeMap.put(n.getName(), (long) n.getCurrentValue());
            }
            nodeMap = MySysUtil.sortMapByLongValue(nodeMap);

            int i = 0;
            for (String n : nodeMap.keySet()) {
                MyDirectNode nn = (MyDirectNode) MyVars.directMarkovChain.vRefs.get(n);
                if (nn.getCurrentValue() == 0) continue;
                ((DefaultTableModel) currentNodeListTable.getModel()).addRow(
                        new String[]{
                                "" + (++i),
                                n,
                                MyMathUtil.getCommaSeperatedNumber((long) nn.getCurrentValue())});
            }
            nodeMap = MySysUtil.sortMapByLongValue(nodeMap);
        } else if (MyVars.getDirectGraphViewer().multiNodes != null) {
            LinkedHashMap<String, Long> nodeMap = new LinkedHashMap<>();
            ArrayList<MyDirectNode> nodes = new ArrayList<>(MyVars.getDirectGraphViewer().multiNodePredecessorSet);
            nodes.addAll(MyVars.getDirectGraphViewer().multiNodeSuccessorSet);
            for (MyDirectNode n : nodes) {
                if (n.getCurrentValue() == 0) continue;
                nodeMap.put(n.getName(), (long) n.getCurrentValue());
            }

            nodeMap = MySysUtil.sortMapByLongValue(nodeMap);
            int i = 0;
            for (String n : nodeMap.keySet()) {
                MyDirectNode nn = (MyDirectNode) MyVars.directMarkovChain.vRefs.get(n);
                if (nn.getCurrentValue() == 0) continue;

                ((DefaultTableModel) currentNodeListTable.getModel()).addRow(
                        new String[]{
                                "" + (++i),
                                n,
                                MyMathUtil.getCommaSeperatedNumber((long) nn.getCurrentValue())});
            }
        } else {
            LinkedHashMap<String, Long> nodeMap = new LinkedHashMap<>();
            Collection<MyDirectNode> nodes = MyVars.directMarkovChain.getVertices();
            for (MyDirectNode n : nodes) {
                nodeMap.put(n.getName(), (long) n.getCurrentValue());
            }
            nodeMap = MySysUtil.sortMapByLongValue(nodeMap);

            int i = 0;
            for (String n : nodeMap.keySet()) {
                MyDirectNode nn = (MyDirectNode) MyVars.directMarkovChain.vRefs.get(n);
                if (nn.getCurrentValue() <= 0) continue;;

                ((DefaultTableModel) currentNodeListTable.getModel()).addRow(
                        new String[]{
                                "" + (++i),
                                n,
                                MyMathUtil.getCommaSeperatedNumber((long) nn.getCurrentValue())});
            }
        }
    }

    public void updateStatTable() {
        if (MyVars.getDirectGraphViewer().selectedSingleNode != null) {
            
        }
    }

    public void updateEdgeValueRelatedComponents() {
        if (MyVars.getDirectGraphViewer().selectedSingleNode != null) {
            MyVars.main.getDirectMarkovChainDashBoard().nodeLevelSuccessorEdgeValueDistribution.decorate();
            MyVars.main.getDirectMarkovChainDashBoard().nodeLevelPredecessorEdgeValueDistribution.decorate();
            MyVars.main.getDirectMarkovChainDashBoard().nodeLevelEdgeValueDistribution.decorate();
            if (MyVars.getDirectGraphViewer().directGraphViewerControlPanel.edgeValueComboBoxMenu.getSelectedIndex() == 0) {
                MyVars.getDirectGraphViewer().setUnWeightedEdgeColor();
                MyVars.getDirectGraphViewer().setUnWeigtedEdgeStroker();
                if (MyVars.getDirectGraphViewer().edgeValueBarChart != null) {
                    MyVars.getDirectGraphViewer().remove(MyVars.getDirectGraphViewer().edgeValueBarChart);
                }
                 (statTable.getModel()).setValueAt( "0.00", 5, 1);
                 (statTable.getModel()).setValueAt("0.00", 6, 1);
                 (statTable.getModel()).setValueAt("0.00", 7, 1);
                 (statTable.getModel()).setValueAt("0.00", 8, 1);
                 (statTable.getModel()).setValueAt("0.00", 9, 1);
                 (statTable.getModel()).setValueAt("0.00", 10, 1);
                 (statTable.getModel()).setValueAt("0.00", 11, 1);
                 (statTable.getModel()).setValueAt("0.00", 12, 1);
            } else {
                MyVars.getDirectGraphViewer().setWeightedEdgeColor();
                MyVars.getDirectGraphViewer().setWeightedEdgeStroker();
                if (MyVars.getDirectGraphViewer().edgeValueBarChart != null) {
                    MyVars.getDirectGraphViewer().remove(MyVars.getDirectGraphViewer().edgeValueBarChart);
                }

                if (MyVars.getDirectGraphViewer().isPredecessorOnly) {
                    MyVars.getDirectGraphViewer().edgeValueBarChart = new MyEdgeValueBarChart();
                    MyVars.getDirectGraphViewer().edgeValueBarChart.setPredecessorEdgeValueBarChart();
                    MyVars.getDirectGraphViewer().add(MyVars.getDirectGraphViewer().edgeValueBarChart);
                } else if (MyVars.getDirectGraphViewer().isSuccessorOnly) {
                    MyVars.getDirectGraphViewer().edgeValueBarChart = new MyEdgeValueBarChart();
                    MyVars.getDirectGraphViewer().edgeValueBarChart.setSuccessorEdgeValueBarChart();
                    MyVars.getDirectGraphViewer().add(MyVars.getDirectGraphViewer().edgeValueBarChart);
                } else {
                    MyVars.getDirectGraphViewer().edgeValueBarChart = new MyEdgeValueBarChart();
                    MyVars.getDirectGraphViewer().edgeValueBarChart.showPredecesorAndSuccessorEdgeBarChart();
                    MyVars.getDirectGraphViewer().add(MyVars.getDirectGraphViewer().edgeValueBarChart);
                }
                 (statTable.getModel()).setValueAt( MyMathUtil.getCommaSeperatedNumber((long)MyVars.directMarkovChain.getMaxInEdgeValueForSelectedNode()), 5, 1);
                 (statTable.getModel()).setValueAt(MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyVars.directMarkovChain.getAverageInEdgeValueForSelectedNode())), 6, 1);
                 (statTable.getModel()).setValueAt(MyMathUtil.getCommaSeperatedNumber((long)MyVars.directMarkovChain.getMinInEdgeValueForSelectedNode()), 7, 1);
                 (statTable.getModel()).setValueAt(MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyVars.directMarkovChain.getInEdgValueStandardDeviationForSelectedNode())), 8, 1);
                 (statTable.getModel()).setValueAt(MyMathUtil.getCommaSeperatedNumber((long)MyVars.directMarkovChain.getMaxOutEdgeValueForSelectedNode()), 9, 1);
                 (statTable.getModel()).setValueAt(MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyVars.directMarkovChain.getAverageOutEdgeValueForSelectedNode())), 10, 1);
                 (statTable.getModel()).setValueAt(MyMathUtil.getCommaSeperatedNumber((long)MyVars.directMarkovChain.getMinOutEdgeValueForSelectedNode()), 11, 1);
                 (statTable.getModel()).setValueAt(MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyVars.directMarkovChain.getOutEdgValueStandardDeviationForSelectedNode())), 12, 1);
             }
        } else if (MyVars.getDirectGraphViewer().multiNodes != null) {
            MyVars.main.getDirectMarkovChainDashBoard().multiNodeLevelSuccessorEdgeValueDistributionLineChart.decorate();
            MyVars.main.getDirectMarkovChainDashBoard().multiNodeLevelPredecessorEdgeValueDistributionLineChart.decorate();
            MyVars.main.getDirectMarkovChainDashBoard().multiNodeLevelSharedSuccessorEdgeValueDistributionLineChart.decorate();
            MyVars.main.getDirectMarkovChainDashBoard().multiNodeLevelSharedPredecessorsEdgeValueDistributionChart.decorate();
        } else {
            if (MyVars.getDirectGraphViewer().directGraphViewerControlPanel.edgeValueComboBoxMenu.getSelectedIndex() > 0) {
                MyVars.getDirectGraphViewer().setWeightedEdgeStroker();
                MyVars.getDirectGraphViewer().setWeightedEdgeColor();
                MyVars.main.getDirectMarkovChainDashBoard().topLevelEdgeValueDistribution.decorate();
                MyVars.main.getDirectMarkovChainDashBoard().topLevelSuccessorEdgeValueDistribution.decorate();
                MyVars.main.getDirectMarkovChainDashBoard().topLevelPredecessorEdgeValueDistribution.decorate();
                if (MyVars.getDirectGraphViewer().edgeValueBarChart != null) {
                    MyVars.getDirectGraphViewer().remove(MyVars.getDirectGraphViewer().edgeValueBarChart);
                }
                MyVars.getDirectGraphViewer().edgeValueBarChart = new MyEdgeValueBarChart();
                MyVars.getDirectGraphViewer().edgeValueBarChart.setEdgeValueBarChart();
                MyVars.getDirectGraphViewer().add(MyVars.getDirectGraphViewer().edgeValueBarChart);
                (statTable.getModel()).setValueAt(MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyVars.directMarkovChain.getAverageEdgeValue())), statTable.getRowCount()-4,1 );
                (statTable.getModel()).setValueAt(MyMathUtil.getCommaSeperatedNumber((long) MyVars.directMarkovChain.getMaxEdgeValue()), statTable.getRowCount()-3, 1);
                (statTable.getModel()).setValueAt(MyMathUtil.getCommaSeperatedNumber((long) MyVars.directMarkovChain.getMinEdgeValue()), statTable.getRowCount()-2, 1);
                (statTable.getModel()).setValueAt(MyMathUtil.getCommaSeperatedNumber((long) MyVars.directMarkovChain.getEdgValueStandardDeviation()), statTable.getRowCount()-1, 1);
            } else {
                MyVars.main.getDirectMarkovChainDashBoard().topLevelEdgeValueDistribution.decorate();
                MyVars.main.getDirectMarkovChainDashBoard().topLevelSuccessorEdgeValueDistribution.decorate();
                MyVars.main.getDirectMarkovChainDashBoard().topLevelPredecessorEdgeValueDistribution.decorate();
                if (MyVars.getDirectGraphViewer().directGraphViewerControlPanel.edgeValueComboBoxMenu.getSelectedIndex() == 0) {
                    MyVars.getDirectGraphViewer().setUnWeightedEdgeColor();
                    MyVars.getDirectGraphViewer().setUnWeigtedEdgeStroker();
                    topLevelStatTablePropertyValuePairs = updateTopLevelGraphStatToolTips();
                    (statTable.getModel()).setValueAt("0.00", statTable.getRowCount()-4,1 );
                    (statTable.getModel()).setValueAt("0", statTable.getRowCount()-3, 1);
                    (statTable.getModel()).setValueAt("0", statTable.getRowCount()-2, 1);
                    (statTable.getModel()).setValueAt("0.00", statTable.getRowCount()-1, 1);
                    statTable.revalidate();
                    statTable.repaint();
                    if (MyVars.getDirectGraphViewer().edgeValueBarChart != null) {
                        MyVars.getDirectGraphViewer().remove(MyVars.getDirectGraphViewer().edgeValueBarChart);
                    }
                } else {
                    (statTable.getModel()).setValueAt(MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyVars.directMarkovChain.getAverageEdgeValue())), statTable.getRowCount()-4,1 );
                    (statTable.getModel()).setValueAt(MyMathUtil.getCommaSeperatedNumber((long) MyVars.directMarkovChain.getMaxEdgeValue()), statTable.getRowCount()-3, 1);
                    (statTable.getModel()).setValueAt(MyMathUtil.getCommaSeperatedNumber((long) MyVars.directMarkovChain.getMinEdgeValue()), statTable.getRowCount()-2, 1);
                    (statTable.getModel()).setValueAt(MyMathUtil.getCommaSeperatedNumber((long) MyVars.directMarkovChain.getEdgValueStandardDeviation()), statTable.getRowCount()-1, 1);
                    MyVars.getDirectGraphViewer().setWeightedEdgeColor();
                    MyVars.getDirectGraphViewer().setWeightedEdgeStroker();
                    topLevelStatTablePropertyValuePairs = updateTopLevelGraphStatToolTips();
                    statTable.revalidate();
                    statTable.repaint();
                }
            }
        }
    }


}
