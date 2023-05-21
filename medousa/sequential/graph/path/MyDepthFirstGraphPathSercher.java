package medousa.sequential.graph.path;

import medousa.MyProgressBar;
import medousa.sequential.graph.MyEdge;
import medousa.sequential.graph.MyNode;
import medousa.sequential.graph.layout.MyDirectedSparseMultigraph;
import medousa.sequential.graph.layout.MyFRLayout;
import medousa.sequential.utils.MyMathUtil;
import medousa.sequential.utils.MySequentialGraphSysUtil;
import medousa.sequential.utils.MySequentialGraphVars;
import edu.uci.ics.jung.visualization.DefaultVisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import medousa.table.MyTableUtil;
import org.apache.commons.collections15.Transformer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class MyDepthFirstGraphPathSercher
extends JFrame
implements ComponentListener, ActionListener {

    protected Map<String, MyEdge> edgeMap;
    protected TreeMap<Integer, Long> pathLengthByDepthMap;
    protected Map<Integer, Long> nodeInContributionByDepthMap;
    protected Map<Integer, Long> nodeOutContributionByDepthMap;
    public TreeMap<Integer, Set<String>> nodesByDepthMap;
    protected Map<Integer, Map<String, Integer>>  maxNodeContByDepthMap;
    protected Set<MyNode> topSuccessors;
    protected MyNode endNode;
    protected MyNode startNode;
    public MyDirectedSparseMultigraph integratedGraph;
    protected MyNodePathGraphViewer betweenPathGraphViewer;
    protected JCheckBox edgeWgtCheckBoxMenu = new JCheckBox("E. WGT.");
    protected JCheckBox nodeWgtCheckBoxMenu = new JCheckBox("N. WGT. ");
    protected JCheckBox nodeValueBarChartCheckBoxMenu = new JCheckBox("N. CONT. ");
    protected JCheckBox edgeValueBarChartCheckBoxMenu = new JCheckBox("E. CONT. ");
    private JSplitPane contentSplitPane;
    public JSplitPane graphSplitPane;
    public JSplitPane depthStatAndGraphSplitPane;
    private JPanel mainPanel = new JPanel();
    protected JPanel depthStatisticsPanel = new JPanel();
    protected JLabel currentNodeInfoLabel = new JLabel();
    protected JLabel txtStatLabel = new JLabel("");
    private JLabel depthNodePercentLabel = new JLabel();
    public JTabbedPane tabbedPane = new JTabbedPane();
    public JTable statTable;
    public int mxDepth;

    public MyDepthFirstGraphPathSercher() {}

    public void decorate() {
        try {
            this.bw = new BufferedWriter(new FileWriter(new File(MySequentialGraphSysUtil.getWorkingDir() + MySequentialGraphSysUtil.getDirectorySlash() + "pathSequences.txt")));
            final MyDepthFirstGraphPathSercher betweenPathGraphDepthFirstSearch = this;
            nodeWgtCheckBoxMenu.setBackground(Color.WHITE);
            nodeWgtCheckBoxMenu.setFont(MySequentialGraphVars.tahomaPlainFont12);
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
            edgeWgtCheckBoxMenu.setFont(MySequentialGraphVars.tahomaPlainFont12);
            edgeWgtCheckBoxMenu.setVisible(false);
            edgeWgtCheckBoxMenu.setFocusable(false);
            edgeWgtCheckBoxMenu.addActionListener(new ActionListener() {
                @Override public void actionPerformed(ActionEvent e) {
                    new Thread(new Runnable() {
                        @Override public void run() {
                            if (edgeWgtCheckBoxMenu.isSelected()) {
                                betweenPathGraphViewer.getRenderContext().setEdgeStrokeTransformer(new Transformer<MyEdge, Stroke>() {
                                    public Stroke transform(MyEdge e) {
                                        float edgeStrokeWeight = e.getContribution() / betweenPathGraphViewer.MAX_EDGE_SIZE;
                                        return new BasicStroke(edgeStrokeWeight * betweenPathGraphViewer.MAX_EDGE_STOKE);
                                    }
                                });
                                betweenPathGraphViewer.getRenderContext().setEdgeDrawPaintTransformer(betweenPathGraphViewer.weightedEdgeColor);
                            } else {
                                betweenPathGraphViewer.getRenderContext().setEdgeStrokeTransformer(new Transformer<MyEdge, Stroke>() {
                                    public Stroke transform(MyEdge e) {
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
            nodeValueBarChartCheckBoxMenu.setFont(MySequentialGraphVars.tahomaPlainFont12);
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
                                betweenPathGraphViewer.betweenPathGraphNodeValueBarChart = new MyNodePathGraphNodeValueBarChart(betweenPathGraphDepthFirstSearch);
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
            edgeValueBarChartCheckBoxMenu.setFont(MySequentialGraphVars.tahomaPlainFont12);
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
                                betweenPathGraphViewer.betweenPathGraphEdgeValueBarChart = new MyNodePathGraphEdgeValueBarChart(betweenPathGraphDepthFirstSearch);
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
            this.currentNodeInfoLabel.setBackground(Color.WHITE);
            this.currentNodeInfoLabel.setFont(MySequentialGraphVars.tahomaPlainFont12);
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
            txtStatLabel.setFont(MySequentialGraphVars.tahomaPlainFont12);
            txtStatLabel.setPreferredSize(new Dimension(300, 25));

            JPanel graphBottomMainPanel = new JPanel();
            graphBottomMainPanel.setBackground(Color.WHITE);
            graphBottomMainPanel.setLayout(new BorderLayout(3, 3));
            graphBottomMainPanel.add(txtStatLabel, BorderLayout.CENTER);

            mainPanel.add(graphBottomMainPanel, BorderLayout.SOUTH);

            this.graphSplitPane = new JSplitPane();
            this.graphSplitPane.setLeftComponent(mainPanel);
            this.graphSplitPane.setDividerSize(4);

            depthStatisticsPanel.setBackground(Color.WHITE);
            depthStatisticsPanel.setLayout(new GridLayout(5, 1));

            this.depthStatAndGraphSplitPane = new JSplitPane();
            this.depthStatAndGraphSplitPane.setDividerSize(4);
            this.depthStatAndGraphSplitPane.setDividerLocation(250);
            this.depthStatAndGraphSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
            this.depthStatAndGraphSplitPane.setRightComponent(this.graphSplitPane);
            this.depthStatAndGraphSplitPane.setLeftComponent(this.depthStatisticsPanel);

            this.contentSplitPane = new JSplitPane();
            this.contentSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
            this.contentSplitPane.setTopComponent(this.depthStatAndGraphSplitPane);
            this.contentSplitPane.setDividerLocation((int) (MySequentialGraphSysUtil.getViewerHeight() * 0.14));
            this.contentSplitPane.setDividerSize(4);

            this.setLayout(new BorderLayout(3, 3));
            this.setPreferredSize(new Dimension(1050, 800));
            this.setMinimumSize(new Dimension(1050, 500));
            this.setBackground(Color.WHITE);
            this.getContentPane().add(contentSplitPane, BorderLayout.CENTER);
            this.addComponentListener(new ComponentListener() {
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
                        contentSplitPane.setDividerLocation((int) (MySequentialGraphSysUtil.getViewerHeight() * 0.14));
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
            this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            this.addComponentListener(this);
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
        MyFRLayout frLayout = new MyFRLayout<>(integratedGraph, new Dimension(1000, 1000));
        this.betweenPathGraphViewer = new MyNodePathGraphViewer(new DefaultVisualizationModel<>(frLayout));
        this.betweenPathGraphViewer.betweenPathGraphDepthFirstSearch = this;
        return this.betweenPathGraphViewer;
    }

    public void run(String fromChoice, String toChoice) {
        try {
            this.pb = new MyProgressBar(false);
            JPanel topPanel = new JPanel();
            topPanel.setLayout(new BorderLayout(3, 3));
            if (integratedGraph != null) {
                integratedGraph = null;
            }
            edgeMap = new HashMap<>();
            pathLengthByDepthMap = new TreeMap<>();
            nodesByDepthMap = new TreeMap<>();
            nodeInContributionByDepthMap = new HashMap<>();
            nodeOutContributionByDepthMap = new HashMap<>();
            maxNodeContByDepthMap = new HashMap<>();
            integratedGraph = new MyDirectedSparseMultigraph();
            startNode = (MyNode) MySequentialGraphVars.g.vRefs.get(fromChoice);
            endNode = (MyNode) MySequentialGraphVars.g.vRefs.get(toChoice);
            topSuccessors = new HashSet<>(MySequentialGraphVars.g.getSuccessors(startNode));
            doDepthFirstAllPathSearch(startNode, new HashSet<>(), new LinkedList<>());
            mainPanel.add(attachGraphToViewer(), BorderLayout.CENTER);
            nodeWgtCheckBoxMenu.setVisible(true);
            edgeWgtCheckBoxMenu.setVisible(true);
            edgeValueBarChartCheckBoxMenu.setVisible(true);
            nodeValueBarChartCheckBoxMenu.setVisible(true);

            JPanel bottomPanel = new JPanel();
            bottomPanel.setLayout(new GridLayout(1, 5));
            bottomPanel.add(new MyNodePathGraphPredecessorCountDistributionLineChart(integratedGraph));
            bottomPanel.add(new MyNodePathGraphSuccessorCountDistributionLineChart(integratedGraph));
            bottomPanel.add(new MyNodePathGraphNodeContributionDistributionLineChart(integratedGraph));
            bottomPanel.add(new MyNodePathGraphEdgeContributionDistributionLineChart(integratedGraph));
            bottomPanel.add(new MyNodePathGraphPathLengthDistributionLineChart(pathLengthByDepthMap));

            if (betweenPathGraphViewer.betweenPathGraphEdgeValueBarChart != null) {
                betweenPathGraphViewer.remove(betweenPathGraphViewer.betweenPathGraphNodeValueBarChart);
            }
            if (betweenPathGraphViewer.betweenPathGraphNodeValueBarChart != null) {
                betweenPathGraphViewer.remove(betweenPathGraphViewer.betweenPathGraphEdgeValueBarChart);
            }

            String txtStat = " N: " + MyMathUtil.getCommaSeperatedNumber(integratedGraph.getVertexCount()) + "  " +
                             "E: " + MyMathUtil.getCommaSeperatedNumber(integratedGraph.getEdgeCount()) + "  " +
                             "MAX. DEPTH: " + MyMathUtil.getCommaSeperatedNumber(nodesByDepthMap.size()) + "  " +
                             "MAX. N. C.: " + MyMathUtil.getCommaSeperatedNumber((long) betweenPathGraphViewer.MAX_NODE_VALUE) + "  " +
                             "MIN. N. C.: " + MyMathUtil.getCommaSeperatedNumber((long) betweenPathGraphViewer.MIN_NODE_VALUE) + "  " +
                             "AVG. N. C.: " + MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat((double) betweenPathGraphViewer.getTotalNodeContribution()/integratedGraph.getVertexCount())).split("\\.")[0] + "   " +
                             "MAX. E. C.: " + MyMathUtil.getCommaSeperatedNumber((long) betweenPathGraphViewer.MAX_EDGE_SIZE) + "  " +
                             "MIN. E. C.: " + MyMathUtil.getCommaSeperatedNumber((long) betweenPathGraphViewer.MIN_EDGE_VALUE) + "  " +
                             "AVG. E. C.: " + MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat((double) betweenPathGraphViewer.getTotalEdgeContribution()/integratedGraph.getEdgeCount())).split("\\.")[0];
            txtStatLabel.setText(txtStat);

            this.depthStatisticsPanel.add(new MyNodePathGraphNodesByDepthDistributionLinChart(nodesByDepthMap));
            this.depthStatisticsPanel.add(new MyNodePathGraphNodeInContributionDistributionLinChart(nodeInContributionByDepthMap));
            this.depthStatisticsPanel.add(new MyNodePathGraphNodeOutContributionDistributionLinChart(nodeOutContributionByDepthMap));
            this.depthStatisticsPanel.add(new MyNodePathGraphNodeInOutContributionDifferenceDistributionLinChart(nodeInContributionByDepthMap, nodeOutContributionByDepthMap));
            this.depthStatisticsPanel.add(new MyNodePathGraphNodeMaxContributionDistributionLinChart(maxNodeContByDepthMap));


            mainPanel.add(betweenPathGraphViewer, BorderLayout.CENTER);
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int screenHeight = (int) screenSize.getHeight();
            int screenWidth = (int) screenSize.getWidth();

            this.tabbedPane.setFocusable(false);
            this.tabbedPane.setOpaque(false);
            this.tabbedPane.setPreferredSize(new Dimension(150, 500));
            this.tabbedPane.setFont(MySequentialGraphVars.tahomaPlainFont10);
            this.tabbedPane.addTab("NODE", setNodeTable());
            this.tabbedPane.addTab("G. STATS.", setGraphStatTable());

            graphSplitPane.setRightComponent(this.tabbedPane);
            graphSplitPane.setDividerLocation(screenWidth-480);
            contentSplitPane.setBottomComponent(bottomPanel);
            this.pack();
            this.setTitle("ALL PATHS BETWEEN [" + MySequentialGraphSysUtil.getDecodedNodeName(fromChoice) + "  AND  " + MySequentialGraphSysUtil.getDecodedNodeName(toChoice) + "]");
            pb.updateValue(100, 100);
            pb.dispose();
            bw.close();
        } catch (Exception ex) {ex.printStackTrace();}
    }

    private int workCount = 0;
    private MyProgressBar pb;
    private BufferedWriter bw;

    private void doDepthFirstAllPathSearch(MyNode successor, Set<MyNode> visited, LinkedList<MyNode> currpath) {
        try {
            if (visited.contains(successor)) {return;}
            visited.add(successor);
            currpath.addLast(successor);

            if (successor == endNode) {
                int pathLength = currpath.size();
                if (pathLengthByDepthMap.containsKey(pathLength)) {
                    pathLengthByDepthMap.put(pathLength, pathLengthByDepthMap.get(pathLength)+1);
                } else {
                    pathLengthByDepthMap.put(pathLength, 1L);
                }

                int j=0;
                for (MyNode n : currpath) {
                    String decondedNodeName = MySequentialGraphSysUtil.getDecodedNodeName(n.getName());
                    if (nodesByDepthMap.containsKey(j+1)) {
                        nodesByDepthMap.get(j+1).add(decondedNodeName);
                    } else {
                        Set<String> nodeSet = new HashSet<>();
                        nodeSet.add(decondedNodeName);
                        nodesByDepthMap.put(j+1, nodeSet);
                    }

                    if (maxNodeContByDepthMap.containsKey(j+1)) {
                        Map<String, Integer> nodeContributionMap = maxNodeContByDepthMap.get(j+1);
                        if (nodeContributionMap.containsKey(decondedNodeName)) {
                            nodeContributionMap.put(decondedNodeName, nodeContributionMap.get(decondedNodeName)+1);
                        } else {
                            nodeContributionMap.put(decondedNodeName, 1);
                        }
                        maxNodeContByDepthMap.put(j+1, nodeContributionMap);
                    } else {
                        Map<String, Integer> nodeContributionMap = new HashMap<>();
                        nodeContributionMap.put(decondedNodeName, 1);
                        maxNodeContByDepthMap.put(j+1, nodeContributionMap);
                    }

                    if (j == 0) {
                        nodeInContributionByDepthMap.put(j+1, 0L);
                    } else {
                        if (nodeInContributionByDepthMap.containsKey(j+1)) {
                            nodeInContributionByDepthMap.put(j+1, nodeInContributionByDepthMap.get(j+1)+1);
                        } else {
                            nodeInContributionByDepthMap.put(j+1, 1L);
                        }
                    }

                    if (j == currpath.size()-1) {
                        nodeOutContributionByDepthMap.put(j+1, 0L);
                    } else {
                        if (nodeOutContributionByDepthMap.containsKey(j+1)) {
                            nodeOutContributionByDepthMap.put(j+1, nodeOutContributionByDepthMap.get(j+1)+1);
                        } else {
                            nodeOutContributionByDepthMap.put(j+1, 1L);
                        }
                    }

                    if (j == 1) {
                        if (this.topSuccessors.contains(n)) {
                            pb.updateValue(++workCount, this.topSuccessors.size()+3);
                        }
                    }

                    if (!integratedGraph.vRefs.containsKey(n.getName())) {
                        MyNode nn = new MyNode(n.getName());
                        integratedGraph.addVertex(nn);
                        integratedGraph.vRefs.put(nn.getName(), nn);
                        nn.setContribution(1);
                        nn.setOriginalValue(nn.getContribution());
                    } else {
                        MyNode nn = (MyNode) integratedGraph.vRefs.get(n.getName());
                        nn.updateContribution(1);
                        nn.setOriginalValue((float)nn.getContribution());
                    }

                    j++;
                }

                for (int i=1; i < currpath.size(); i++) {
                    String edgeStr = currpath.get(i-1).getName() + "-" + currpath.get(i).getName();
                    if (!integratedGraph.edRefs.contains(edgeStr)) {
                        MyNode pNode = (MyNode) integratedGraph.vRefs.get(currpath.get(i-1).getName());
                        MyNode sNode = (MyNode) integratedGraph.vRefs.get(currpath.get(i).getName());
                        MyEdge edge = new MyEdge(pNode, sNode);
                        integratedGraph.addEdge(edge, pNode, sNode);
                        integratedGraph.edRefs.add(edgeStr);
                        edgeMap.put(edgeStr, edge);
                    } else {
                        edgeMap.get(edgeStr).setContribution(1);
                    }
                }

                /**
                 * Write the current path to file.
                 */
                String line = "";
                for (MyNode n : currpath) {
                    if (line.length() == 0) {
                        line = n.getName();
                    } else {
                        line = line + "-" + n.getName();
                    }
                }
                bw.write(line + "\n");
                if (mxDepth < currpath.size()) {
                    mxDepth = currpath.size();
                }
                currpath.removeLast();
                visited.remove(successor);
                return;
            }

            Collection<MyEdge> outEdges = MySequentialGraphVars.g.getOutEdges(successor);
            for (MyEdge edge : outEdges) {;
                doDepthFirstAllPathSearch(edge.getDest(), visited, currpath);
            }
            currpath.removeLast();
            visited.remove(successor);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String [] args) {
        MyDepthFirstGraphPathSercher graphDepthFirstSearch = new MyDepthFirstGraphPathSercher();
    }

    @Override public void componentResized(ComponentEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                new Thread(new Runnable() {
                    @Override public void run() {
                        if (betweenPathGraphViewer == null) return;
                        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                        int screenHeight = (int) screenSize.getHeight();
                        int screenWidth = (int) screenSize.getWidth();
                        graphSplitPane.setDividerLocation(screenWidth - 480);
                        contentSplitPane.setDividerLocation(screenHeight - 180);
                        betweenPathGraphViewer.revalidate();
                        betweenPathGraphViewer.repaint();
                    }
                }).start();
            }
        });
    }

    @Override public void componentMoved(ComponentEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                new Thread(new Runnable() {
                    @Override public void run() {
                        if (betweenPathGraphViewer == null) return;
                        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                        int screenHeight = (int) screenSize.getHeight();
                        int screenWidth = (int) screenSize.getWidth();
                        graphSplitPane.setDividerLocation(screenWidth - 480);
                        contentSplitPane.setDividerLocation(screenHeight - 180);
                        betweenPathGraphViewer.revalidate();
                        betweenPathGraphViewer.repaint();
                    }
                }).start();
            }
        });
    }

    @Override public void componentShown(ComponentEvent e) {}
    @Override public void componentHidden(ComponentEvent e) {}

    private JPanel setNodeTable() {
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout(3,3));
        tablePanel.setBackground(Color.WHITE);

        String [] columns = {"NO.", "NODE"};
        String [][] data = {};
        DefaultTableModel tableModel = new DefaultTableModel(data, columns);
        JTable nodeTable = new JTable(tableModel);
        int i=-0;
        Collection<MyNode> nodes = integratedGraph.getVertices();
        for (MyNode n : nodes) {
            tableModel.addRow(new String[]{String.valueOf(++i), n.getName()});
        }

        nodeTable.setRowHeight(20);
        nodeTable.setBackground(Color.WHITE);
        nodeTable.setFont(MySequentialGraphVars.f_pln_11);
        nodeTable.getTableHeader().setFont(MySequentialGraphVars.tahomaBoldFont11);
        nodeTable.getTableHeader().setOpaque(false);
        nodeTable.getTableHeader().setBackground(new Color(0,0,0,0f));
        nodeTable.getColumnModel().getColumn(0).setPreferredWidth(35);
        nodeTable.getColumnModel().getColumn(0).setMaxWidth(45);
        nodeTable.getColumnModel().getColumn(1).setPreferredWidth(130);

        JTextField nodeSearchTxt = new JTextField();
        JButton nodeSelectBtn = new JButton("SEL.");
        nodeSelectBtn.setFont(MySequentialGraphVars.tahomaPlainFont11);
        nodeSelectBtn.setFocusable(false);
        nodeSearchTxt.setBackground(Color.WHITE);
        nodeSearchTxt.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        JPanel searchAndSavePanel = MyTableUtil.searchAndSaveDataPanelForJTable2(this, nodeSearchTxt, nodeSelectBtn, tableModel, nodeTable);
        //searchAndSavePanel.remove(nodeSelectBtn);
        nodeSearchTxt.setFont(MySequentialGraphVars.f_bold_10);
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
                                n = MySequentialGraphVars.nodeNameMap.get(n);
                                betweenPathGraphViewer.selectedNode = (MyNode) integratedGraph.vRefs.get(n);
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

        JLabel nodesByDepthLabel = new JLabel("NODES BY DEPTH: ");
        nodesByDepthLabel.setFont(MySequentialGraphVars.tahomaPlainFont11);
        nodesByDepthLabel.setBackground(Color.WHITE);

        this.nodesByDepthComboBoxMenu = new JComboBox();
        nodesByDepthComboBoxMenu.setBackground(Color.WHITE);
        nodesByDepthComboBoxMenu.setFont(MySequentialGraphVars.tahomaPlainFont11);
        nodesByDepthComboBoxMenu.setFocusable(false);
        nodesByDepthComboBoxMenu.addItem("DEPTH");
        for (Integer depth : nodesByDepthMap.keySet()) {
            nodesByDepthComboBoxMenu.addItem(MyMathUtil.getCommaSeperatedNumber(depth));
        }
        nodesByDepthComboBoxMenu.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                try {
                    if (nodesByDepthComboBoxMenu.getSelectedIndex() == 0) {
                        depthNodePercentLabel.setText("");
                        Collection<MyNode> nodes = integratedGraph.getVertices();
                        long maxValue = 0L;
                        for (MyNode n : nodes) {
                            n.setContribution((int) n.getOriginalValue());
                        }

                        for (MyNode n : nodes) {
                            if (n.getContribution() > maxValue) {
                                maxValue = n.getContribution();
                            }
                        }
                        betweenPathGraphViewer.MAX_NODE_SIZE = maxValue;
                        betweenPathGraphViewer.MAX_NODE_VALUE = maxValue;


                        // Remove current node records.
                        for (int i=nodeTable.getRowCount()-1; i >= 0; i--) {
                            tableModel.removeRow(i);
                        }

                        // Add all the nodes.
                        int i=0;
                        for (MyNode n : nodes) {
                            tableModel.addRow(new String[]{"" + (++i), n.getName()});
                        }

                        tablePanel.revalidate();
                        tablePanel.repaint();

                        betweenPathGraphViewer.revalidate();
                        betweenPathGraphViewer.repaint();
                    } else {
                        String depthStr = nodesByDepthComboBoxMenu.getSelectedItem().toString().replaceAll(" ", "");
                        int numOfNodes = nodesByDepthMap.get(Integer.valueOf(depthStr)).size();
                        String percentOfDepthNodes = MyMathUtil.twoDecimalPercent(((double) numOfNodes/integratedGraph.getVertexCount()));
                        depthNodePercentLabel.setText(percentOfDepthNodes);
                        long maxValue = 0L;
                        Set<String> depthNodeSet = new HashSet<>(nodesByDepthMap.get(Integer.valueOf(depthStr)));
                        Collection<MyNode> nodes = integratedGraph.getVertices();
                        for (MyNode n : nodes) {
                            n.setContribution((int) n.getOriginalValue());
                        }

                        for (MyNode n : nodes) {
                            if (!depthNodeSet.contains(n.getName())) {
                                n.setContribution(0);
                            } else {
                                if (n.getContribution() > maxValue) {
                                    maxValue = n.getContribution();
                                }
                            }
                        }

                        // Remove current node records.
                        for (int i=nodeTable.getRowCount()-1; i >= 0; i--) {
                            tableModel.removeRow(i);
                        }

                        // Add all the nodes.
                        int i=0;
                        Set<String> depthNodes = nodesByDepthMap.get(Integer.valueOf(depthStr));
                        for (String n : depthNodes) {
                            tableModel.addRow(new String[]{"" + (++i), n});
                        }
                        tablePanel.revalidate();
                        tablePanel.repaint();

                        betweenPathGraphViewer.MAX_NODE_SIZE = maxValue;
                        betweenPathGraphViewer.MAX_NODE_VALUE = maxValue;
                        betweenPathGraphViewer.revalidate();
                        betweenPathGraphViewer.repaint();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        depthNodePercentLabel.setBackground(Color.WHITE);
        depthNodePercentLabel.setFont(MySequentialGraphVars.tahomaPlainFont11);
        depthNodePercentLabel.setText("");

        topNodesByDepthPanel.add(nodesByDepthLabel);
        topNodesByDepthPanel.add(nodesByDepthComboBoxMenu);
        topNodesByDepthPanel.add(depthNodePercentLabel);

        tablePanel.add(topNodesByDepthPanel, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        tablePanel.add(searchAndSavePanel, BorderLayout.SOUTH);
        return tablePanel;
    }

    protected JComboBox nodesByDepthComboBoxMenu;

    private JPanel setGraphStatTable() {
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout(3,3));
        tablePanel.setBackground(Color.WHITE);

        String [] statTableColumns = {"PROPERTY.", "V."};
        String [][] statTableData = {
                {"NODES", MyMathUtil.getCommaSeperatedNumber(MySequentialGraphVars.g.getVertexCount())},
                {"EDGES", MyMathUtil.getCommaSeperatedNumber(MySequentialGraphVars.g.getEdgeCount())},
                {"MAX N. C.", MyMathUtil.getCommaSeperatedNumber((long) betweenPathGraphViewer.MAX_NODE_VALUE)},
                {"MIN. N. C.", MyMathUtil.getCommaSeperatedNumber((long) betweenPathGraphViewer.MIN_NODE_VALUE)},
                {"AVG. N. C.", MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat((double) betweenPathGraphViewer.getTotalNodeContribution()/integratedGraph.getVertexCount()))},
                {"MAX. E. C.", MyMathUtil.getCommaSeperatedNumber((long) betweenPathGraphViewer.MAX_EDGE_SIZE)},
                {"MIN. E. C.", MyMathUtil.getCommaSeperatedNumber((long) betweenPathGraphViewer.MIN_EDGE_VALUE)},
                {"AVG. E. C.", MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat((double) betweenPathGraphViewer.getTotalEdgeContribution()/integratedGraph.getEdgeCount()))}

        };
        DefaultTableModel statTableModel = new DefaultTableModel(statTableData, statTableColumns);
        this.statTable = new JTable(statTableModel);
        this.statTable.setRowHeight(19);
        this.statTable.setBackground(Color.WHITE);
        this.statTable.setFont(MySequentialGraphVars.tahomaPlainFont10);
        this.statTable.setFocusable(false);
        this.statTable.getTableHeader().setFont(MySequentialGraphVars.tahomaBoldFont10);
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
