package medousa.direct.graph.path;

import medousa.MyProgressBar;
import medousa.direct.graph.MyDirectEdge;
import medousa.direct.graph.MyDirectGraph;
import medousa.direct.graph.MyDirectNode;
import medousa.direct.graph.layout.MyDirectGraphFRLayout;
import medousa.direct.utils.MyDirectGraphMathUtil;
import medousa.direct.utils.MyDirectGraphSysUtil;
import medousa.direct.utils.MyDirectGraphVars;
import medousa.table.MyTableUtil;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.DefaultVisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import org.apache.commons.collections15.Transformer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class MyDirectGraphDepthFirstGraphPathSercher
extends JFrame
implements ComponentListener, ActionListener {

    protected Map<String, MyDirectEdge> edgeMap;
    protected TreeMap<Integer, Long> pathLengthByDepthMap;
    protected Map<Integer, Long> nodeInContributionByDepthMap;
    protected Map<Integer, Long> nodeOutContributionByDepthMap;
    public TreeMap<Integer, Set<String>> nodesByDepthMap;
    protected Map<Integer, Map<String, Integer>>  maxNodeContByDepthMap;
    protected MyDirectNode endNode;
    protected MyDirectNode startNode;
    public MyDirectGraph integratedGraph;
    protected MyDirectGraphPathGraphViewer betweenPathGraphViewer;
    protected JCheckBox edgeWgtCheckBoxMenu = new JCheckBox("E. WGT.");
    protected JCheckBox nodeWgtCheckBoxMenu = new JCheckBox("N. WGT. ");
    protected JCheckBox nodeValueBarChartCheckBoxMenu = new JCheckBox("N. CONT. ");
    protected JCheckBox edgeValueBarChartCheckBoxMenu = new JCheckBox("E. CONT. ");
    private JSplitPane contentSplitPane;
    public JSplitPane graphSplitPane;
    private JPanel mainPanel = new JPanel();
    protected JLabel currentNodeInfoLabel = new JLabel();
    protected JLabel txtStatLabel = new JLabel("");
    private JLabel depthNodePercentLabel = new JLabel();
    public JTabbedPane tabbedPane = new JTabbedPane();
    public JTable statTable;
    protected JComboBox nodesByDepthComboBoxMenu;

    public MyDirectGraphDepthFirstGraphPathSercher() {}

    public void decorate() {
        final MyDirectGraphDepthFirstGraphPathSercher betweenPathGraphDepthFirstSearch = this;
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                try {
                    nodeWgtCheckBoxMenu.setBackground(Color.WHITE);
                    nodeWgtCheckBoxMenu.setFont(MyDirectGraphVars.tahomaPlainFont12);
                    nodeWgtCheckBoxMenu.setVisible(false);
                    nodeWgtCheckBoxMenu.setFocusable(false);
                    nodeWgtCheckBoxMenu.addActionListener(new ActionListener() {
                        @Override public void actionPerformed(ActionEvent e) {
                            new Thread(new Runnable() {
                                @Override public void run() {
                                    if (nodeWgtCheckBoxMenu.isSelected()) {
                                        betweenPathGraphViewer.getRenderContext().setVertexFillPaintTransformer(betweenPathGraphViewer.weightedNodeColor);
                                    } else {
                                        betweenPathGraphViewer.getRenderContext().setVertexFillPaintTransformer(betweenPathGraphViewer.unWeightedNodeColor);
                                    }
                                    betweenPathGraphViewer.revalidate();
                                    betweenPathGraphViewer.repaint();
                                }
                            }).start();
                        }
                    });

                    edgeWgtCheckBoxMenu.setBackground(Color.WHITE);
                    edgeWgtCheckBoxMenu.setFont(MyDirectGraphVars.tahomaPlainFont12);
                    edgeWgtCheckBoxMenu.setVisible(false);
                    edgeWgtCheckBoxMenu.setFocusable(false);
                    edgeWgtCheckBoxMenu.addActionListener(new ActionListener() {
                        @Override public void actionPerformed(ActionEvent e) {
                            new Thread(new Runnable() {
                                @Override public void run() {
                                    if (edgeWgtCheckBoxMenu.isSelected()) {
                                        betweenPathGraphViewer.getRenderContext().setEdgeStrokeTransformer(new Transformer<MyDirectEdge, Stroke>() {
                                            public Stroke transform(MyDirectEdge e) {
                                                float edgeStrokeWeight = e.getContribution() / betweenPathGraphViewer.MAX_EDGE_SIZE;
                                                return new BasicStroke(edgeStrokeWeight * betweenPathGraphViewer.MAX_EDGE_STOKE);
                                            }
                                        });
                                        betweenPathGraphViewer.getRenderContext().setEdgeDrawPaintTransformer(betweenPathGraphViewer.weightedEdgeColor);
                                    } else {
                                        betweenPathGraphViewer.getRenderContext().setEdgeStrokeTransformer(new Transformer<MyDirectEdge, Stroke>() {
                                            public Stroke transform(MyDirectEdge e) {
                                                return new BasicStroke(1f);
                                            }
                                        });

                                        betweenPathGraphViewer.getRenderContext().setEdgeDrawPaintTransformer(betweenPathGraphViewer.edgeDrawPainter);
                                    }
                                    betweenPathGraphViewer.revalidate();
                                    betweenPathGraphViewer.repaint();
                                }
                            }).start();
                        }
                    });

                    nodeValueBarChartCheckBoxMenu.setBackground(Color.WHITE);
                    nodeValueBarChartCheckBoxMenu.setFont(MyDirectGraphVars.tahomaPlainFont12);
                    nodeValueBarChartCheckBoxMenu.setVisible(false);
                    nodeValueBarChartCheckBoxMenu.setFocusable(false);
                    nodeValueBarChartCheckBoxMenu.addActionListener(new ActionListener() {
                        @Override public void actionPerformed(ActionEvent e) {
                            new Thread(new Runnable() {
                                @Override public void run() {
                                    if (nodeValueBarChartCheckBoxMenu.isSelected()) {
                                        if (betweenPathGraphViewer.betweenPathGraphNodeValueBarChart != null) {
                                            betweenPathGraphViewer.remove(betweenPathGraphViewer.betweenPathGraphNodeValueBarChart);
                                        }
                                        betweenPathGraphViewer.betweenPathGraphNodeValueBarChart = new MyDirectGraphPathGraphNodeValueBarChart(betweenPathGraphDepthFirstSearch);
                                        betweenPathGraphViewer.add(betweenPathGraphViewer.betweenPathGraphNodeValueBarChart);
                                    } else {
                                        if (betweenPathGraphViewer.betweenPathGraphNodeValueBarChart != null) {
                                            betweenPathGraphViewer.remove(betweenPathGraphViewer.betweenPathGraphNodeValueBarChart);
                                        }
                                    }
                                    betweenPathGraphViewer.revalidate();
                                    betweenPathGraphViewer.repaint();
                                }
                            }).start();
                        }
                    });

                    edgeValueBarChartCheckBoxMenu.setBackground(Color.WHITE);
                    edgeValueBarChartCheckBoxMenu.setFont(MyDirectGraphVars.tahomaPlainFont12);
                    edgeValueBarChartCheckBoxMenu.setVisible(false);
                    edgeValueBarChartCheckBoxMenu.setFocusable(false);
                    edgeValueBarChartCheckBoxMenu.addActionListener(new ActionListener() {
                        @Override public void actionPerformed(ActionEvent e) {
                            new Thread(new Runnable() {
                                @Override public void run() {
                                    if (edgeValueBarChartCheckBoxMenu.isSelected()) {
                                        if (betweenPathGraphViewer.betweenPathGraphEdgeValueBarChart != null) {
                                            betweenPathGraphViewer.remove(betweenPathGraphViewer.betweenPathGraphEdgeValueBarChart);
                                        }
                                        betweenPathGraphViewer.betweenPathGraphEdgeValueBarChart = new MyDirectGraphPathGraphEdgeValueBarChart(betweenPathGraphDepthFirstSearch);
                                        betweenPathGraphViewer.add(betweenPathGraphViewer.betweenPathGraphEdgeValueBarChart);
                                    } else {
                                        if (betweenPathGraphViewer.betweenPathGraphEdgeValueBarChart != null) {
                                            betweenPathGraphViewer.remove(betweenPathGraphViewer.betweenPathGraphEdgeValueBarChart);
                                        }
                                    }
                                    revalidate();
                                    repaint();
                                }
                            }).start();
                        }
                    });

                    JPanel infoPanel = new JPanel();
                    infoPanel.setLayout(new BorderLayout(3, 3));
                    infoPanel.setBackground(Color.WHITE);

                    JPanel nodeInfoPanel = new JPanel();
                    nodeInfoPanel.setBackground(Color.WHITE);
                    nodeInfoPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
                    currentNodeInfoLabel.setBackground(Color.WHITE);
                    currentNodeInfoLabel.setFont(MyDirectGraphVars.tahomaPlainFont12);
                    nodeInfoPanel.add(currentNodeInfoLabel);

                    JPanel checkBoxMenuPanel = new JPanel();
                    checkBoxMenuPanel.setBackground(Color.WHITE);
                    checkBoxMenuPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 3));
                    checkBoxMenuPanel.add(nodeWgtCheckBoxMenu);
                    checkBoxMenuPanel.add(edgeWgtCheckBoxMenu);
                    checkBoxMenuPanel.add(nodeValueBarChartCheckBoxMenu);
                    checkBoxMenuPanel.add(edgeValueBarChartCheckBoxMenu);

                    infoPanel.add(nodeInfoPanel, BorderLayout.CENTER);
                    infoPanel.add(checkBoxMenuPanel, BorderLayout.EAST);

                    mainPanel.setBackground(Color.WHITE);
                    mainPanel.setLayout(new BorderLayout(3, 3));

                    JPanel emptyGraphViewerPanel = new JPanel();
                    emptyGraphViewerPanel.setBackground(Color.WHITE);

                    mainPanel.add(infoPanel, BorderLayout.NORTH);
                    txtStatLabel.setBackground(Color.LIGHT_GRAY);
                    txtStatLabel.setFont(MyDirectGraphVars.f_pln_12);
                    txtStatLabel.setPreferredSize(new Dimension(300, 25));

                    JPanel graphBottomMainPanel = new JPanel();
                    graphBottomMainPanel.setBackground(Color.WHITE);
                    graphBottomMainPanel.setLayout(new BorderLayout(3, 3));
                    graphBottomMainPanel.add(txtStatLabel, BorderLayout.CENTER);

                    mainPanel.add(graphBottomMainPanel, BorderLayout.SOUTH);

                    graphSplitPane = new JSplitPane();
                    graphSplitPane.setRightComponent(mainPanel);
                    graphSplitPane.setDividerSize(4);
                    graphSplitPane.setDividerLocation(0.2);

                    contentSplitPane = new JSplitPane();
                    contentSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
                    contentSplitPane.setTopComponent(graphSplitPane);
                    contentSplitPane.setDividerLocation(0.21);
                    contentSplitPane.setDividerSize(4);

                    setLayout(new BorderLayout(3, 3));
                    setPreferredSize(new Dimension(1050, 800));
                    setMinimumSize(new Dimension(1050, 500));
                    setBackground(Color.WHITE);
                    getContentPane().add(contentSplitPane, BorderLayout.CENTER);
                    addComponentListener(new ComponentListener() {
                        @Override public void componentResized(ComponentEvent e) {
                            try {
                                if (betweenPathGraphViewer != null) {
                                    if (betweenPathGraphViewer.betweenPathGraphEdgeValueBarChart != null) {
                                        betweenPathGraphViewer.remove(betweenPathGraphViewer.betweenPathGraphEdgeValueBarChart);
                                    }
                                    if (betweenPathGraphViewer.betweenPathGraphNodeValueBarChart != null) {
                                        betweenPathGraphViewer.remove(betweenPathGraphViewer.betweenPathGraphNodeValueBarChart);
                                    }
                                }
                                contentSplitPane.setDividerLocation(0.21);
                                graphSplitPane.setDividerLocation(0.2);
                                revalidate();
                                repaint();

                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        @Override public void componentMoved(ComponentEvent e) {}
                        @Override public void componentShown(ComponentEvent e) {}
                        @Override public void componentHidden(ComponentEvent e) {}
                    });

                    addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e) {
                            super.windowClosing(e);
                            new Thread(new Runnable() {
                                @Override public void run() {
                                    Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
                                    for (MyDirectNode n : nodes) {
                                        n.shortestOutDistance = -1;
                                    }
                                }
                            }).start();
                        }
                    });
                    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    addComponentListener(betweenPathGraphDepthFirstSearch);
                    setExtendedState(JFrame.MAXIMIZED_BOTH);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private VisualizationViewer attachGraphToViewer() {
        if (betweenPathGraphViewer != null) {
            betweenPathGraphViewer = null;
        }
        nodeWgtCheckBoxMenu.setSelected(false);
        nodeWgtCheckBoxMenu.setVisible(false);
        edgeWgtCheckBoxMenu.setSelected(false);
        edgeWgtCheckBoxMenu.setVisible(false);
        nodeValueBarChartCheckBoxMenu.setSelected(false);
        nodeValueBarChartCheckBoxMenu.setVisible(false);
        edgeValueBarChartCheckBoxMenu.setSelected(false);
        edgeValueBarChartCheckBoxMenu.setVisible(false);

        MyDirectGraphFRLayout frLayout = new MyDirectGraphFRLayout<>(integratedGraph, new Dimension(5000, 5000));
        this.betweenPathGraphViewer = new MyDirectGraphPathGraphViewer(new DefaultVisualizationModel<>(frLayout));

        this.betweenPathGraphViewer.betweenPathGraphDepthFirstSearch = this;
        return this.betweenPathGraphViewer;
    }

    public void run(String fromChoice, String toChoice) {
        try {
            MyProgressBar pb = new MyProgressBar(false);
            JPanel topPanel = new JPanel();
            topPanel.setLayout(new BorderLayout(3, 3));
            if (this.integratedGraph != null) {this.integratedGraph = null;}
            this.edgeMap = new HashMap<>();
            this.pathLengthByDepthMap = new TreeMap<>();
            this.nodesByDepthMap = new TreeMap<>();
            this.nodeInContributionByDepthMap = new HashMap<>();
            this.nodeOutContributionByDepthMap = new HashMap<>();
            this.maxNodeContByDepthMap = new HashMap<>();
            this.integratedGraph = new MyDirectGraph(EdgeType.DIRECTED);
            this.startNode = (MyDirectNode) MyDirectGraphVars.directGraph.vRefs.get(fromChoice);
            this.endNode = (MyDirectNode) MyDirectGraphVars.directGraph.vRefs.get(toChoice);

            findPaths(pb);

            this.mainPanel.add(attachGraphToViewer(), BorderLayout.CENTER);
            this.nodeWgtCheckBoxMenu.setVisible(true);
            this.edgeWgtCheckBoxMenu.setVisible(true);
            this.edgeValueBarChartCheckBoxMenu.setVisible(true);
            this.nodeValueBarChartCheckBoxMenu.setVisible(true);

            this.bottomPanel = new JPanel();
            this.bottomPanel.setLayout(new GridLayout(1, 3));
            this.bottomPanel.add(new MyDirectGraphPathGraphNodesByDistanceDistributionLinChart(nodesByDepthMap));
            this.bottomPanel.add(new MyDirectGraphPathGraphNodeContributionDistributionLineChart(integratedGraph));
            this.bottomPanel.add(new MyDirectGraphPathGraphEdgeContributionDistributionLineChart(integratedGraph));
            this.bottomPanel.add(new MyDirectGraphPathGraphPathLengthDistributionLineChart(pathLengthByDepthMap));

            if (this.betweenPathGraphViewer.betweenPathGraphEdgeValueBarChart != null) {
                this.betweenPathGraphViewer.remove(betweenPathGraphViewer.betweenPathGraphNodeValueBarChart);
            }
            if (this.betweenPathGraphViewer.betweenPathGraphNodeValueBarChart != null) {
                this.betweenPathGraphViewer.remove(betweenPathGraphViewer.betweenPathGraphEdgeValueBarChart);
            }

            String txtStat = " N: " + MyDirectGraphMathUtil.getCommaSeperatedNumber(integratedGraph.getVertexCount()) + "  " +
                             "E: " + MyDirectGraphMathUtil.getCommaSeperatedNumber(integratedGraph.getEdgeCount()) + "  " +
                             "MAX. DEPTH: " + MyDirectGraphMathUtil.getCommaSeperatedNumber(nodesByDepthMap.size()) + "  " +
                             "MAX. N. C.: " + MyDirectGraphMathUtil.getCommaSeperatedNumber((long) betweenPathGraphViewer.MAX_NODE_VALUE) + "  " +
                             "MIN. N. C.: " + MyDirectGraphMathUtil.getCommaSeperatedNumber((long) betweenPathGraphViewer.MIN_NODE_VALUE) + "  " +
                             "AVG. N. C.: " + MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat((double) betweenPathGraphViewer.getTotalNodeContribution()/integratedGraph.getVertexCount())).split("\\.")[0] + "   " +
                             "MAX. E. C.: " + MyDirectGraphMathUtil.getCommaSeperatedNumber((long) betweenPathGraphViewer.MAX_EDGE_SIZE) + "  " +
                             "MIN. E. C.: " + MyDirectGraphMathUtil.getCommaSeperatedNumber((long) betweenPathGraphViewer.MIN_EDGE_VALUE) + "  " +
                             "AVG. E. C.: " + MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat((double) betweenPathGraphViewer.getTotalEdgeContribution()/integratedGraph.getEdgeCount())).split("\\.")[0];
            this.txtStatLabel.setText(txtStat);

            //this.depthStatisticsPanel.add(new MyPathGraphNodesByDepthDistributionLinChart(nodesByDepthMap));
            //this.depthStatisticsPanel.add(new MyPathGraphNodeInContributionDistributionLinChart(nodeInContributionByDepthMap));
            //this.depthStatisticsPanel.add(new MyPathGraphNodeOutContributionDistributionLinChart(nodeOutContributionByDepthMap));
            //this.depthStatisticsPanel.add(new MyPathGraphNodeInOutContributionDifferenceDistributionLinChart(nodeInContributionByDepthMap, nodeOutContributionByDepthMap));
            //this.depthStatisticsPanel.add(new MyPathGraphNodeMaxContributionDistributionLinChart(maxNodeContByDepthMap));

            this.mainPanel.add(betweenPathGraphViewer, BorderLayout.CENTER);

            this.tabbedPane.setFocusable(false);
            this.tabbedPane.setOpaque(false);
            this.tabbedPane.setPreferredSize(new Dimension(150, 500));
            this.tabbedPane.setFont(MyDirectGraphVars.tahomaPlainFont12);
            this.tabbedPane.addTab("NODE", setNodeTable());
            this.tabbedPane.addTab("STATS.", setGraphStatTable());

            this.graphSplitPane.setLeftComponent(this.tabbedPane);
            //graphSplitPane.setDividerLocation(screenWidth-100);
            this.contentSplitPane.setBottomComponent(bottomPanel);
            this.pack();
            this.setTitle("ALL PATHS BETWEEN [" + fromChoice + "  AND  " + toChoice + "]");
            pb.updateValue(100, 100);
            pb.dispose();
        } catch (Exception ex) {ex.printStackTrace();}
    }

    private JPanel bottomPanel;

    private void updateBottomChartForSelectedDepth() {
        try {
            if (nodesByDepthComboBoxMenu.getSelectedIndex() > 0) {
                this.bottomPanel.removeAll();
                bottomPanel.setLayout(new GridLayout(1, 1));
                bottomPanel.add(new MyDirectGraphPathGraphDepthNodeContributionDistributionLineChart(this));
                contentSplitPane.setDividerLocation(0.87);
            } else {
                this.bottomPanel.removeAll();
                this.bottomPanel.setLayout(new GridLayout(1, 3));
                this.bottomPanel.add(new MyDirectGraphPathGraphNodesByDistanceDistributionLinChart(nodesByDepthMap));
                this.bottomPanel.add(new MyDirectGraphPathGraphNodeContributionDistributionLineChart(integratedGraph));
                this.bottomPanel.add(new MyDirectGraphPathGraphEdgeContributionDistributionLineChart(integratedGraph));
                this.bottomPanel.add(new MyDirectGraphPathGraphPathLengthDistributionLineChart(pathLengthByDepthMap));
                contentSplitPane.setDividerLocation(0.87);
            }
        } catch (Exception ex) {ex.printStackTrace();}
    }

    private void findPaths(MyProgressBar pb) {
        try {
            int topLevelSuccessorCount = 0;
            Set<MyDirectNode> topLevelSuccessors = new HashSet<>(MyDirectGraphVars.directGraph.getSuccessors(startNode));
            topLevelSuccessors.add(startNode);
            Queue<MyDirectNode> Qu = new LinkedList<>();
            Set<MyDirectNode> visited = new HashSet();
            LinkedHashSet<MyDirectNode> currPath = new LinkedHashSet<>();
            currPath.add(startNode);
            Qu.add(startNode);

            while (!Qu.isEmpty()) {
                MyDirectNode vertex = Qu.remove();
                Collection<MyDirectNode> successors = MyDirectGraphVars.directGraph.getSuccessors(vertex);

                for (MyDirectNode neighbor : successors) {
                    if (!visited.contains(neighbor)) {
                        Qu.add(neighbor);
                    }

                    currPath.add(neighbor);

                    if (neighbor == endNode) {
                        buildGraph(currPath);
                        currPath.remove(neighbor);
                        break;
                    }
                }

                visited.add(vertex);
                currPath.remove(vertex);

                if (topLevelSuccessors.contains(vertex)) {
                    topLevelSuccessorCount++;
                    pb.updateValue(topLevelSuccessorCount, topLevelSuccessors.size());
                }
            }

            pb.updateValue(100, 100);
            pb.dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void buildGraph(LinkedHashSet<MyDirectNode> currPath) {
        try {
            if (pathLengthByDepthMap.containsKey(currPath.size())) {
                pathLengthByDepthMap.put(currPath.size(), pathLengthByDepthMap.get(currPath.size()) + 1);
            } else {
                pathLengthByDepthMap.put(currPath.size(), 1L);
            }

            int j = 0;
            for (MyDirectNode n : currPath) {
                if (n != startNode && n != endNode) {
                    if (nodesByDepthMap.containsKey(j + 1)) {
                        nodesByDepthMap.get(j + 1).add(n.getName());
                    } else {
                        Set<String> nodeSet = new HashSet<>();
                        nodeSet.add(n.getName());
                        nodesByDepthMap.put(j + 1, nodeSet);
                    }
                    j++;
                }

                if (!integratedGraph.vRefs.containsKey(n.getName())) {
                    MyDirectNode nn = new MyDirectNode(n.getName());
                    integratedGraph.addVertex(nn);
                    integratedGraph.vRefs.put(nn.getName(), nn);
                    nn.setContribution(1);
                    nn.setOriginalValue((float) nn.getContribution());
                } else {
                    MyDirectNode nn = (MyDirectNode) integratedGraph.vRefs.get(n.getName());
                    nn.updateContribution(1);
                    nn.setOriginalValue((float) nn.getContribution());
                }
            }

            Iterator itr = currPath.iterator();
            MyDirectNode p = ((MyDirectNode) itr.next());
            while (itr.hasNext()) {
                MyDirectNode s = ((MyDirectNode) itr.next());
                String edgeStr = p.getName() + "-" + s.getName();
                if (!edgeMap.containsKey(edgeStr)) {
                    MyDirectNode pN = (MyDirectNode) integratedGraph.vRefs.get(p.getName());
                    MyDirectNode sN = (MyDirectNode) integratedGraph.vRefs.get(s.getName());
                    MyDirectEdge edge = new MyDirectEdge(pN, sN);
                    integratedGraph.addEdge(edge, pN, sN);
                    integratedGraph.edRefs.add(edge);
                    edgeMap.put(edgeStr, edge);
                } else {
                    edgeMap.get(edgeStr).setContribution(1);
                }
                p = s;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String [] args) {
        MyDirectGraphDepthFirstGraphPathSercher graphDepthFirstSearch = new MyDirectGraphDepthFirstGraphPathSercher();
    }

    @Override public void componentResized(ComponentEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                new Thread(new Runnable() {
                    @Override public void run() {
                        if (betweenPathGraphViewer == null) return;
                        graphSplitPane.setDividerLocation(0.145);
                        contentSplitPane.setDividerLocation(0.87);
                        betweenPathGraphViewer.revalidate();
                        betweenPathGraphViewer.repaint();
                    }
                }).start();
            }
        });
    }

    @Override public void componentMoved(ComponentEvent e) {}
    @Override public void componentShown(ComponentEvent e) {}
    @Override public void componentHidden(ComponentEvent e) {}

    private JPanel setNodeTable() {
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout(3,3));
        tablePanel.setBackground(Color.WHITE);

        String [] columns = {"NO.", "NODE", "CONT."};
        String [][] data = {};
        DefaultTableModel tableModel = new DefaultTableModel(data, columns);
        JTable nodeTable = new JTable(tableModel);
        nodeTable.getTableHeader().getColumnModel().getColumn(0).setPreferredWidth(60);
        nodeTable.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(80);
        nodeTable.getTableHeader().getColumnModel().getColumn(2).setPreferredWidth(60);
        nodeTable.getTableHeader().getColumnModel().getColumn(2).setMaxWidth(80);

        int i=-0;
        Collection<MyDirectNode> nodes = integratedGraph.getVertices();
        LinkedHashMap<String, Long> nodeValueMap = new LinkedHashMap<>();
        for (MyDirectNode n : nodes) {
            nodeValueMap.put(n.getName(), (long) n.getContribution());
        }
        nodeValueMap = MyDirectGraphSysUtil.sortMapByLongValue(nodeValueMap);

        for (String n : nodeValueMap.keySet()) {
            tableModel.addRow(new String[]{String.valueOf(++i), n, MyDirectGraphMathUtil.getCommaSeperatedNumber(nodeValueMap.get(n))});
        }

        nodeTable.setRowHeight(20);
        nodeTable.setBackground(Color.WHITE);
        nodeTable.setFont(MyDirectGraphVars.f_pln_12);
        nodeTable.getTableHeader().setFont(MyDirectGraphVars.tahomaBoldFont12);
        nodeTable.getTableHeader().setOpaque(false);
        nodeTable.getTableHeader().setBackground(new Color(0,0,0,0f));
        nodeTable.getColumnModel().getColumn(0).setPreferredWidth(35);
        nodeTable.getColumnModel().getColumn(0).setMaxWidth(45);
        nodeTable.getColumnModel().getColumn(1).setPreferredWidth(130);

        JTextField nodeSearchTxt = new JTextField();
        JButton nodeSelectBtn = new JButton("SEL.");
        nodeSelectBtn.setFont(MyDirectGraphVars.tahomaPlainFont12);
        nodeSelectBtn.setFocusable(false);
        nodeSearchTxt.setBackground(Color.WHITE);
        nodeSearchTxt.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        JPanel searchAndSavePanel = MyTableUtil.searchAndSaveDataPanelForJTable2(this, nodeSearchTxt, nodeSelectBtn, tableModel, nodeTable);
        //searchAndSavePanel.remove(nodeSelectBtn);
        nodeSearchTxt.setFont(MyDirectGraphVars.f_bold_13);
        nodeSearchTxt.setPreferredSize(new Dimension(130, 19));
        nodeSelectBtn.setPreferredSize(new Dimension(55, 19));
        nodeSelectBtn.removeActionListener(this);
        nodeSelectBtn.removeActionListener(this);
        nodeSelectBtn.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        try {
                            synchronized (nodeSelectBtn) {
                                String n = nodeTable.getValueAt(nodeTable.getSelectedRow(), 1).toString();
                                betweenPathGraphViewer.selectedNode = (MyDirectNode) integratedGraph.vRefs.get(n);
                                betweenPathGraphViewer.setMaximumNodeValue();
                                betweenPathGraphViewer.setMaximumNodeSize();
                                betweenPathGraphViewer.revalidate();
                                betweenPathGraphViewer.repaint();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        add(searchAndSavePanel, BorderLayout.SOUTH);
        nodeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        nodeTable.setSelectionBackground(Color.LIGHT_GRAY);
        nodeTable.setForeground(Color.BLACK);
        nodeTable.setSelectionForeground(Color.BLACK);
        nodeTable.setFocusable(false);
        Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
        nodeTable.setCursor(cursor);
        nodeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        nodeTable.setRowSorter(MyTableUtil.setJTableRowSorterWithTextField(tableModel, nodeSearchTxt));

        JScrollPane scrollPane = new JScrollPane(nodeTable);
        scrollPane.setOpaque(false);
        scrollPane.setBackground(new Color(0,0,0,0f));
        scrollPane.setPreferredSize(new Dimension(150, 500));
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
        scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 8));

        JPanel topNodesByDepthPanel = new JPanel();
        topNodesByDepthPanel.setBackground(Color.WHITE);
        topNodesByDepthPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3,3));

        JLabel nodesByDepthLabel = new JLabel("NODES BY DISTANCE: ");
        nodesByDepthLabel.setFont(MyDirectGraphVars.tahomaPlainFont12);
        nodesByDepthLabel.setBackground(Color.WHITE);

        this.nodesByDepthComboBoxMenu = new JComboBox();
        this.nodesByDepthComboBoxMenu.setBackground(Color.WHITE);
        this.nodesByDepthComboBoxMenu.setFont(MyDirectGraphVars.tahomaPlainFont12);
        this.nodesByDepthComboBoxMenu.setFocusable(false);
        this.nodesByDepthComboBoxMenu.addItem("DISTANCE");
        for (Integer depth : nodesByDepthMap.keySet()) {
            this.nodesByDepthComboBoxMenu.addItem(MyDirectGraphMathUtil.getCommaSeperatedNumber(depth));
        }
        this.nodesByDepthComboBoxMenu.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        try {
                            MyProgressBar pb = new MyProgressBar(false);
                            if (nodesByDepthComboBoxMenu.getSelectedIndex() == 0) {
                                depthNodePercentLabel.setText("");
                                long maxValue = 0L;
                                for (MyDirectNode n : nodes) {
                                    if (n.getContribution() > maxValue) {
                                        maxValue = n.getContribution();
                                    }
                                }

                                betweenPathGraphViewer.MAX_NODE_SIZE = maxValue;
                                betweenPathGraphViewer.MAX_NODE_VALUE = maxValue;

                                for (int i=nodeTable.getRowCount()-1; i >= 0; i--) {
                                    tableModel.removeRow(i);
                                }

                                int i=0;
                                for (MyDirectNode n : nodes) {
                                    tableModel.addRow(new String[]{"" + (++i), n.getName(), MyDirectGraphMathUtil.getCommaSeperatedNumber(n.getContribution())});
                                }

                                tablePanel.revalidate();
                                tablePanel.repaint();

                                updateBottomChartForSelectedDepth();
                                bottomPanel.revalidate();
                                bottomPanel.repaint();
                                revalidate();
                                repaint();
                            } else {
                                String depthStr = nodesByDepthComboBoxMenu.getSelectedItem().toString().replaceAll(" ", "");
                                int numOfNodes = nodesByDepthMap.get(Integer.valueOf(depthStr)).size();
                                String percentOfDepthNodes = MyDirectGraphMathUtil.twoDecimalPercent(((double) numOfNodes/integratedGraph.getVertexCount()));
                                depthNodePercentLabel.setText(percentOfDepthNodes);

                                long maxValue = 0L;
                                Collection<MyDirectNode> nodes = integratedGraph.getVertices();
                                Set<String> depthNodes = nodesByDepthMap.get(Integer.valueOf(depthStr));
                                for (MyDirectNode n : nodes) {
                                    if (depthNodes.contains(n.getName())) {
                                        n.shortestOutDistance = nodesByDepthComboBoxMenu.getSelectedIndex();
                                        if (n.getContribution() > maxValue) {
                                            maxValue = n.getContribution();
                                        }
                                    }
                                }

                                // Remove current node records.
                                for (int i=nodeTable.getRowCount()-1; i >= 0; i--) {tableModel.removeRow(i);}

                                // Add all the nodes.
                                int i=0;
                                LinkedHashMap<String, Long> nodeValueMap = new LinkedHashMap<>();
                                for (MyDirectNode n : nodes) {
                                    if (depthNodes.contains(n.getName())) {
                                        nodeValueMap.put(n.getName(), (long) n.getContribution());
                                    }
                                }
                                nodeValueMap = MyDirectGraphSysUtil.sortMapByLongValue(nodeValueMap);

                                for (String n : nodeValueMap.keySet()) {

                                    tableModel.addRow(new String[]{"" + (++i), n,
                                            MyDirectGraphMathUtil.getCommaSeperatedNumber(nodeValueMap.get(n))});

                                }
                                tablePanel.revalidate();
                                tablePanel.repaint();

                                betweenPathGraphViewer.MAX_NODE_SIZE = maxValue;
                                betweenPathGraphViewer.MAX_NODE_VALUE = maxValue;

                                updateBottomChartForSelectedDepth();
                                bottomPanel.revalidate();
                                bottomPanel.repaint();
                                revalidate();
                                repaint();
                            }
                            pb.updateValue(100, 100);
                            pb.dispose();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        this.depthNodePercentLabel.setBackground(Color.WHITE);
        this.depthNodePercentLabel.setFont(MyDirectGraphVars.tahomaPlainFont12);
        this.depthNodePercentLabel.setText("");

        topNodesByDepthPanel.add(nodesByDepthLabel);
        topNodesByDepthPanel.add(this.nodesByDepthComboBoxMenu);
        topNodesByDepthPanel.add(this.depthNodePercentLabel);

        tablePanel.add(topNodesByDepthPanel, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        tablePanel.add(searchAndSavePanel, BorderLayout.SOUTH);
        return tablePanel;
    }

    private JPanel setGraphStatTable() {
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout(3,3));
        tablePanel.setBackground(Color.WHITE);

        String [] statTableColumns = {"PROPERTY.", "V."};
        String [][] statTableData = {
                {"NODES", MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.directGraph.getVertexCount())},
                {"EDGES", MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.directGraph.getEdgeCount())},
                {"MAX N. C.", MyDirectGraphMathUtil.getCommaSeperatedNumber((long) betweenPathGraphViewer.MAX_NODE_VALUE)},
                {"MIN. N. C.", MyDirectGraphMathUtil.getCommaSeperatedNumber((long) betweenPathGraphViewer.MIN_NODE_VALUE)},
                {"AVG. N. C.", MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat((double) betweenPathGraphViewer.getTotalNodeContribution()/integratedGraph.getVertexCount()))},
                {"MAX. E. C.", MyDirectGraphMathUtil.getCommaSeperatedNumber((long) betweenPathGraphViewer.MAX_EDGE_SIZE)},
                {"MIN. E. C.", MyDirectGraphMathUtil.getCommaSeperatedNumber((long) betweenPathGraphViewer.MIN_EDGE_VALUE)},
                {"AVG. E. C.", MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat((double) betweenPathGraphViewer.getTotalEdgeContribution()/integratedGraph.getEdgeCount()))}

        };
        DefaultTableModel statTableModel = new DefaultTableModel(statTableData, statTableColumns);
        this.statTable = new JTable(statTableModel);
        this.statTable.setRowHeight(19);
        this.statTable.setBackground(Color.WHITE);
        this.statTable.setFont(MyDirectGraphVars.tahomaPlainFont12);
        this.statTable.setFocusable(false);
        this.statTable.getTableHeader().setFont(MyDirectGraphVars.tahomaBoldFont12);
        this.statTable.getTableHeader().setOpaque(false);
        this.statTable.getTableHeader().setBackground(new Color(0,0,0,0f));
        this.statTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        this.statTable.getColumnModel().getColumn(1).setPreferredWidth(45);
        this.statTable.setPreferredSize(new Dimension(145, 800));
        this.statTable.setForeground(Color.BLACK);
        this.statTable.setSelectionForeground(Color.BLACK);
        this.statTable.setSelectionBackground(Color.LIGHT_GRAY);

        JScrollPane graphStatTableScrollPane = new JScrollPane(this.statTable);
        graphStatTableScrollPane.setOpaque(false);
        graphStatTableScrollPane.setPreferredSize(new Dimension(150, 805));
        graphStatTableScrollPane.setBackground(new Color(0,0,0,0f));
        graphStatTableScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(7, 0));
        graphStatTableScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 7));

        tablePanel.add(graphStatTableScrollPane, BorderLayout.CENTER);
        return tablePanel;
    }

    @Override public void actionPerformed(ActionEvent e) {

    }
}
