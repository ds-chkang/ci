package medousa.sequential.graph.funnel;

import medousa.MyProgressBar;
import medousa.message.MyMessageUtil;
import medousa.sequential.graph.MyGraph;
import medousa.sequential.graph.layout.MyStaticLayout;
import medousa.sequential.graph.MyEdge;
import medousa.sequential.graph.MyNode;
import medousa.sequential.utils.MyMathUtil;
import medousa.sequential.utils.MySequentialGraphSysUtil;
import medousa.sequential.utils.MySequentialGraphVars;
import edu.uci.ics.jung.visualization.DefaultVisualizationModel;
import medousa.table.MyTableUtil;
import org.apache.commons.collections15.Transformer;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;

public class MyAnalysisGraphApp
extends JFrame
implements ActionListener, WindowListener {

    private MyAnalysisGraph graph;
    protected JButton addBtn;
    protected JTable table;
    private MyAnalysisGraphViewer graphViewer;
    private DefaultTableModel model;
    private JTextField searchTxt = new JTextField();
    protected JComboBox nodeOptionComboBoxMenu = new JComboBox();
    protected JComboBox edgeValueComboBoxMenu = new JComboBox();
    protected JComboBox nodeValueComboBoxMenu = new JComboBox();
    protected JCheckBox nodeValueCheckBoxMenu = new JCheckBox("N. V.");
    protected JCheckBox edgeValueCheckBoxMenu = new JCheckBox("E. V.");
    protected JCheckBox nodeNameCheckBoxMenu = new JCheckBox("N. N.");
    protected JCheckBox edgeWeightCheckBoxMenu = new JCheckBox("E. WGT.");
    protected JCheckBox nodeWeightCheckBoxMenu = new JCheckBox("N. WGT.");
    protected JLabel statisticLabel = new JLabel("");

    public MyAnalysisGraphApp() {
        super("FUNNEL EXPLORER");
        this.decorate();
    }

    private void decorate() {
        this.setLayout(new BorderLayout(5,5));
        this.setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(1000, 600));
        this.setCursor(Cursor.HAND_CURSOR);
        JSplitPane viewerSplitPane = new JSplitPane();
        viewerSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        viewerSplitPane.setDividerSize(5);
        viewerSplitPane.setLeftComponent(this.getLeftPanel());
        viewerSplitPane.setRightComponent(this.getRightPanel());
        viewerSplitPane.setDividerLocation((int) (MySequentialGraphSysUtil.getViewerHeight()*0.835));
        viewerSplitPane.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
            viewerSplitPane.setDividerSize(5);
            viewerSplitPane.setDividerLocation((int) (getWidth()*0.835));
            }
        });
        this.getContentPane().add(viewerSplitPane, BorderLayout.CENTER);
        this.pack();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setAlwaysOnTop(true);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }

    private JPanel getRightPanel() {
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setLayout(new BorderLayout(3,3));

        JPanel addPanel = new JPanel();
        addPanel.setBackground(Color.WHITE);
        addPanel.setLayout(new BorderLayout(2,2));

        this.addBtn = new JButton("ADD");
        this.addBtn.setBackground(Color.WHITE);
        this.addBtn.setFocusable(false);
        this.addBtn.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.addBtn.addActionListener(this);
        this.addBtn.setEnabled(false);
        addPanel.add(this.searchTxt, BorderLayout.CENTER);
        addPanel.add(this.addBtn, BorderLayout.EAST);

        rightPanel.add(this.getTable(), BorderLayout.CENTER);
        rightPanel.add(addPanel, BorderLayout.SOUTH);
        return rightPanel;
    }

    private JPanel getLeftPanel() {
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setLayout(new BorderLayout(3,3));

        JLabel edgeValueLabel = new JLabel(" E. V.: ");
        edgeValueLabel.setFont(MySequentialGraphVars.tahomaPlainFont12);
        edgeValueLabel.setBackground(Color.WHITE);

        this.edgeValueComboBoxMenu.setBackground(Color.WHITE);
        this.edgeValueComboBoxMenu.setFocusable(false);
        this.edgeValueComboBoxMenu.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.edgeValueComboBoxMenu.addItem("NONE");
        this.edgeValueComboBoxMenu.addItem("CONTRIBUTION");
        this.edgeValueComboBoxMenu.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        if (edgeValueComboBoxMenu.getSelectedIndex() > 0) {
                            edgeWeightCheckBoxMenu.setEnabled(true);
                        } else {
                            edgeWeightCheckBoxMenu.setEnabled(false);
                        }
                    }
                }).start();
            }
        });

        JLabel nodeValueLabel = new JLabel("N. V.: ");
        nodeValueLabel.setFont(MySequentialGraphVars.tahomaPlainFont12);
        nodeValueLabel.setBackground(Color.WHITE);

        this.nodeValueComboBoxMenu.setBackground(Color.WHITE);
        this.nodeValueComboBoxMenu.setFocusable(false);
        this.nodeValueComboBoxMenu.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.nodeValueComboBoxMenu.addItem("NONE");
        this.nodeValueComboBoxMenu.addItem("CONTRIBUTION");
        this.nodeValueComboBoxMenu.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        if (nodeValueComboBoxMenu.getSelectedIndex() > 0) {
                            nodeWeightCheckBoxMenu.setEnabled(true);
                        } else {
                            nodeWeightCheckBoxMenu.setEnabled(false);
                            graphViewer.getRenderContext().setVertexLabelTransformer(new Transformer<MyNode, String>() {
                                @Override public String transform(MyNode n) {
                                    return "";
                                }
                            });
                        }
                    }
                }).start();
            }
        });

        JLabel nodeOptionLabel = new JLabel(" N. O.: ");
        nodeOptionLabel.setToolTipText("NODE OPTIONS");
        nodeOptionLabel.setFont(MySequentialGraphVars.tahomaPlainFont12);
        nodeOptionLabel.setBackground(Color.WHITE);

        this.nodeOptionComboBoxMenu.setFocusable(false);
        this.nodeOptionComboBoxMenu.setBackground(Color.WHITE);
        this.nodeOptionComboBoxMenu.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.nodeOptionComboBoxMenu.addItem("");
        this.nodeOptionComboBoxMenu.addItem("NODES");
        //this.nodeOptionComboBoxMenu.addItem("PREDECESSORS");
        this.nodeOptionComboBoxMenu.addItem("SUCCESSORS");
        this.nodeOptionComboBoxMenu.setSelectedIndex(0);
        this.nodeOptionComboBoxMenu.addActionListener(this);

        JPanel topRightPanel = new JPanel();
        topRightPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        topRightPanel.setBackground(Color.WHITE);
        //topRightPanel.add(nodeValueLabel);
        //topRightPanel.add(nodeValueComboBoxMenu);
        //topRightPanel.add(edgeValueLabel);
       // topRightPanel.add(this.edgeValueComboBoxMenu);
        topRightPanel.add(nodeOptionLabel);
        topRightPanel.add(this.nodeOptionComboBoxMenu);

        this.nodeValueCheckBoxMenu.setBackground(Color.WHITE);
        this.nodeValueCheckBoxMenu.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.nodeValueCheckBoxMenu.setFocusable(false);
        this.nodeValueCheckBoxMenu.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        if (nodeValueCheckBoxMenu.isSelected()) {
                            if (nodeNameCheckBoxMenu.isSelected()) {
                                graphViewer.getRenderContext().setVertexLabelTransformer(new Transformer<MyNode, String>() {
                                    @Override public String transform(MyNode n) {
                                        String cont = MyMathUtil.getCommaSeperatedNumber(n.getContribution());
                                        return MySequentialGraphSysUtil.getDecodedNodeName(n.getName()) + "[" + cont + "] ";
                                    }
                                });
                            } else {
                                graphViewer.getRenderContext().setVertexLabelTransformer(new Transformer<MyNode, String>() {
                                    @Override public String transform(MyNode n) {
                                        return MyMathUtil.getCommaSeperatedNumber(n.getContribution());
                                    }
                                });
                            }
                        } else {
                            if (nodeNameCheckBoxMenu.isSelected()) {
                                graphViewer.getRenderContext().setVertexLabelTransformer(new Transformer<MyNode, String>() {
                                    @Override public String transform(MyNode n) {
                                        return MySequentialGraphSysUtil.getDecodedNodeName(n.getName());
                                    }
                                });
                            } else {
                                graphViewer.getRenderContext().setVertexLabelTransformer(new Transformer<MyNode, String>() {
                                    @Override public String transform(MyNode n) {
                                        return "";
                                    }
                                });
                            }
                        }
                        graphViewer.revalidate();
                        graphViewer.repaint();
                    }
                }).start();
            }
        });

        this.nodeNameCheckBoxMenu.setBackground(Color.WHITE);
        this.nodeNameCheckBoxMenu.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.nodeNameCheckBoxMenu.setFocusable(false);
        this.nodeNameCheckBoxMenu.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        if (nodeNameCheckBoxMenu.isSelected()) {
                            if (nodeValueCheckBoxMenu.isSelected()) {
                                graphViewer.getRenderContext().setVertexLabelTransformer(new Transformer<MyNode, String>() {
                                    @Override public String transform(MyNode n) {
                                        String inCont = "IN-C.: " + MyMathUtil.getCommaSeperatedNumber(n.getInContribution());
                                        String outCont = "OUT-C.: " + MyMathUtil.getCommaSeperatedNumber(n.getOutContribution());
                                        return MySequentialGraphSysUtil.getDecodedNodeName(n.getName()) + "[" +
                                                inCont + "  " + outCont + "] ";
                                    }
                                });
                            } else {
                                graphViewer.getRenderContext().setVertexLabelTransformer(new Transformer<MyNode, String>() {
                                    @Override public String transform(MyNode n) {
                                        return MySequentialGraphSysUtil.getDecodedNodeName(n.getName());
                                    }
                                });
                            }
                        } else {
                            if (nodeValueCheckBoxMenu.isSelected()) {
                                graphViewer.getRenderContext().setVertexLabelTransformer(new Transformer<MyNode, String>() {
                                    @Override public String transform(MyNode n) {
                                        return MyMathUtil.getCommaSeperatedNumber(n.getContribution());
                                    }
                                });
                            } else {
                                graphViewer.getRenderContext().setVertexLabelTransformer(new Transformer<MyNode, String>() {
                                    @Override public String transform(MyNode n) {
                                        return "";
                                    }
                                });
                            }
                        }
                        graphViewer.revalidate();
                        graphViewer.repaint();
                    }
                }).start();
            }
        });

        this.edgeValueCheckBoxMenu.setBackground(Color.WHITE);
        this.edgeValueCheckBoxMenu.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.edgeValueCheckBoxMenu.setFocusable(false);
        this.edgeValueCheckBoxMenu.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        if (edgeValueCheckBoxMenu.isSelected()) {
                           // if (edgeValueComboBoxMenu.getSelectedIndex() == 0) {
                           //     MyMessageUtil.showInfoMsg(networkViewer.plusNetworkAnalyzer, "Select an edge value, first.");
                           //     edgeValueCheckBoxMenu.setSelected(false);
                           //     return;
                           // }

                            graphViewer.getRenderContext().setEdgeLabelTransformer(new Transformer<MyEdge, String>() {
                                @Override public String transform(MyEdge e) {
                                    String edgeContributionRatio = MyMathUtil.twoDecimalFormat(((double) e.getContribution() / e.getSource().getContribution()) * 100) + "%";
                                    return MyMathUtil.getCommaSeperatedNumber(e.getContribution()) + " / " + e.getSource().getContribution() + " = " + edgeContributionRatio + " ";
                                }
                            });

                        } else {
                            graphViewer.getRenderContext().setEdgeLabelTransformer(new Transformer<MyEdge, String>() {
                                @Override public String transform(MyEdge e) {
                                    return "";
                                }
                            });
                        }
                        graphViewer.revalidate();
                        graphViewer.repaint();
                    }
                }).start();
            }
        });

        JPanel topLeftPanel = new JPanel();
        topLeftPanel.setBackground(Color.WHITE);
        topLeftPanel.setLayout(new FlowLayout(FlowLayout.LEFT,3,3));

        this.nodeWeightCheckBoxMenu.setBackground(Color.WHITE);
        this.nodeWeightCheckBoxMenu.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.nodeWeightCheckBoxMenu.setFocusable(false);
        this.nodeWeightCheckBoxMenu.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        try {
                            if (nodeWeightCheckBoxMenu.isSelected()) {
                                graphViewer.getRenderContext().setVertexShapeTransformer(new Transformer<MyNode, Shape>() {
                                    @Override public Shape transform(MyNode n) {
                                        float sizeRatio = (float) n.getContribution() / graphViewer.MAX_NODE_VALUE;
                                        float nodeSize = sizeRatio * (graphViewer.MAX_NODE_SIZE);
                                        if (nodeSize < 25f) {nodeSize = 15f;}
                                            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
                                        }
                                    });
                                graphViewer.revalidate();
                                graphViewer.repaint();
                            } else {
                                graphViewer.getRenderContext().setVertexShapeTransformer(new Transformer<MyNode, Shape>() {
                                    @Override public Shape transform(MyNode n) {
                                        return new Ellipse2D.Double(-40, -40, 80, 80);
                                    }
                                });
                                graphViewer.revalidate();
                                graphViewer.repaint();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        this.edgeWeightCheckBoxMenu.setBackground(Color.WHITE);
        this.edgeWeightCheckBoxMenu.setFocusable(false);
        this.edgeWeightCheckBoxMenu.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.edgeWeightCheckBoxMenu.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        if (edgeWeightCheckBoxMenu.isSelected()) {
                            graphViewer.getRenderContext().setEdgeStrokeTransformer(new Transformer<MyEdge, Stroke>() {
                                @Override public Stroke transform(MyEdge e) {
                                    return new BasicStroke(1f);
                                }
                            });
                            graphViewer.revalidate();
                            graphViewer.repaint();
                        } else {
                            graphViewer.getRenderContext().setEdgeStrokeTransformer(new Transformer<MyEdge, Stroke>() {
                                @Override public Stroke transform(MyEdge e) {
                                    return new BasicStroke(1f);
                                }
                            });
                            graphViewer.revalidate();
                            graphViewer.repaint();
                        }
                    }
                }).start();
            }
        });

        topLeftPanel.add(this.nodeWeightCheckBoxMenu);
        topLeftPanel.add(this.edgeWeightCheckBoxMenu);

        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.WHITE);
        topPanel.setLayout(new BorderLayout(3,3));
        topPanel.add(topLeftPanel, BorderLayout.WEST);
        topPanel.add(topRightPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setLayout(new BorderLayout(3,3));

        this.statisticLabel.setBackground(Color.WHITE);
        this.statisticLabel.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.statisticLabel.setHorizontalAlignment(JLabel.RIGHT);

        JPanel bottomLeftPanel = new JPanel();
        bottomLeftPanel.setLayout(new FlowLayout(FlowLayout.LEFT,3,3));
        bottomLeftPanel.setBackground(Color.WHITE);
        bottomLeftPanel.add(this.nodeValueCheckBoxMenu);
        bottomLeftPanel.add(this.edgeValueCheckBoxMenu);
        bottomLeftPanel.add(this.nodeNameCheckBoxMenu);

        JPanel bottomRightPanel = new JPanel();
        bottomRightPanel.setBackground(Color.WHITE);
        bottomRightPanel.setLayout(new FlowLayout(FlowLayout.RIGHT,3,3));
        bottomRightPanel.add(this.statisticLabel);

        bottomPanel.add(bottomLeftPanel, BorderLayout.WEST);
        bottomPanel.add(bottomRightPanel, BorderLayout.CENTER);

        this.graph = new MyAnalysisGraph<>();
        MyStaticLayout staticLayout = new MyStaticLayout(this.graph, new Dimension(400, 500));
        this.graphViewer = new MyAnalysisGraphViewer(new DefaultVisualizationModel<>(staticLayout, new Dimension(400, 500)), this);
        leftPanel.add(topPanel, BorderLayout.NORTH);
        leftPanel.add(this.graphViewer, BorderLayout.CENTER);
        leftPanel.add(bottomPanel, BorderLayout.SOUTH);
        return leftPanel;
    }

    private JScrollPane getTable() {
        String [] columns = {"NO.", "NODE", "CONT."};
        String [][] data = {};
        this.model = new DefaultTableModel(data, columns);
        this.table = new JTable(this.model);
        this.table.getTableHeader().setFont(MySequentialGraphVars.tahomaBoldFont12);
        this.table.getTableHeader().setOpaque(false);
        this.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.table.setFont(MySequentialGraphVars.f_pln_12);
        this.table.getTableHeader().setOpaque(false);
        this.table.getTableHeader().setBackground(new Color(0,0,0,0));
        Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
        this.table.setCursor(cursor);
        this.table.setRowHeight(25);
        this.table.setFocusable(false);
        this.table.setBackground(Color.WHITE);
        this.table.setSelectionBackground(Color.LIGHT_GRAY);
        this.table.setSelectionForeground(Color.BLACK);
        this.table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override public void valueChanged(ListSelectionEvent event) {
                if (table.getSelectedRow() > -1) {
                    addBtn.setEnabled(true);
                }
            }
        });
        this.table.getColumnModel().getColumn(0).setPreferredWidth(45);
        this.table.getColumnModel().getColumn(1).setPreferredWidth(100);
        this.table.getColumnModel().getColumn(2).setPreferredWidth(50);
        this.table.getColumnModel().getColumn(2).setMaxWidth(60);
        TableRowSorter sorter = MyTableUtil.setJTableRowSorterWithTextField(this.model, this.searchTxt);
        this.searchTxt.setPreferredSize(new Dimension(100, 20));
        this.searchTxt.setBorder(BorderFactory.createEtchedBorder());
        this.searchTxt.setFont(MySequentialGraphVars.f_bold_12);
        this.table.setRowSorter(sorter);
        JScrollPane tableScrollPane = new JScrollPane(this.table);
        tableScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(9, 0));
        tableScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 9));
        return tableScrollPane;
    }


    private void loadNodes() {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            int cnt = 0;
            Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
            LinkedHashMap<String, Long> sortedNodes = new LinkedHashMap<>();
            for (MyNode n : nodes) {
                sortedNodes.put(n.getName(), n.getContribution());
            }

            sortedNodes = MySequentialGraphSysUtil.sortMapByLongValue(sortedNodes);
            for (String n : sortedNodes.keySet()) {
                this.model.addRow(
                    new String[]{
                        String.valueOf(++cnt),
                        MySequentialGraphSysUtil.getNodeName(n),
                        MyMathUtil.getCommaSeperatedNumber(((MyNode) MySequentialGraphVars.g.vRefs.get(n)).getContribution())});
            }

            pb.updateValue(100, 100);
            pb.dispose();
        } catch (Exception ex) {
            pb.updateValue(100, 100);
            pb.dispose();
            ex.printStackTrace();
        }
    }

    private void removeTableRecords() {
        int row = this.table.getRowCount();
        while (row > 0) {
            ((DefaultTableModel)this.table.getModel()).removeRow(row-1);
            row = this.table.getRowCount();
        }
    }

    private void loadPredecessors() {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            int cnt = 0;
            LinkedHashMap<String, Long> sortedNodes = new LinkedHashMap<>();
            Collection<MyEdge> originalGraphInEdges = MySequentialGraphVars.g.getInEdges(MySequentialGraphVars.g.vRefs.get(graphViewer.graphMouseListener.selectedNode.getName()));
            Collection<MyEdge> analysisGraphInEdges = graph.getInEdges(graphViewer.graphMouseListener.selectedNode);
            for (MyEdge oe : originalGraphInEdges) {
                boolean alreadExists = false;
                for (MyEdge ae : analysisGraphInEdges) {
                    if (oe.getSource().getName().equals(ae.getSource().getName()) && oe.getDest().getName().equals(ae.getDest().getName())) {
                        alreadExists = true;
                        break;
                    }
                }

                if (!alreadExists) {
                    sortedNodes.put(oe.getSource().getName(), (long) oe.getContribution());
                }
            }

            sortedNodes = MySequentialGraphSysUtil.sortMapByLongValue(sortedNodes);
            pb.updateValue(75, 100);
            for (String n : sortedNodes.keySet()) {
                this.model.addRow(new String[]{String.valueOf(++cnt), MySequentialGraphSysUtil.getNodeName(n), MyMathUtil.getCommaSeperatedNumber(sortedNodes.get(n))});
            }

            pb.updateValue(100, 100);
            pb.dispose();
        } catch (Exception ex) {
            pb.updateValue(100, 100);
            pb.dispose();
            ex.printStackTrace();
        }
    }

    private void loadSuccessors() {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            Set<String> pathSet = new HashSet<>();
            MyAnalysisGraphSuccessorPathExtractor successorPathExtractor =
                new MyAnalysisGraphSuccessorPathExtractor(graph, graphViewer.graphMouseListener.selectedNode);
            Collection<MyNode> nodes = graph.getVertices();
            for (MyNode n : nodes) {
                if (graph.getPredecessorCount(n) == 0) {
                    for (List<MyNode> path : successorPathExtractor.run(n)) {
                        String pathStr = "";
                        for (MyNode pn : path) {
                            if (pathStr.length() == 0) {
                                pathStr = pn.getName();
                            } else {
                                pathStr = pathStr + "-" + pn.getName();
                            }
                        }
                        pathSet.add(pathStr);
                    }
                }
            }

            MySuccessorScanManager successorScanManager = new MySuccessorScanManager(pathSet);
            successorScanManager.run();
            LinkedHashMap<String, Long> successors = new LinkedHashMap<>(successorScanManager.getSuccessors());
            successors = MySequentialGraphSysUtil.sortMapByLongValue(successors);

            if (successors.size() == 0) {
                pb.updateValue(100, 100);
                pb.dispose();
                nodeOptionComboBoxMenu.removeActionListener(graphViewer.analysisGraphApp);
                nodeOptionComboBoxMenu.setSelectedIndex(0);
                nodeOptionComboBoxMenu.addActionListener(graphViewer.analysisGraphApp);
                MyMessageUtil.showInfoMsg(graphViewer.analysisGraphApp, "No successors exist.");
                return;
            }

            int cnt = 0;
            for (String n : successors.keySet()) {
                if (graph.vRefs.containsKey(n)) continue;
                model.addRow(new String[]{"" + (++cnt),
                    MySequentialGraphSysUtil.getNodeName(n),
                    MyMathUtil.getCommaSeperatedNumber(successors.get(n))
                });
            }

            pb.updateValue(100, 100);
            pb.dispose();
        } catch (Exception ex) {
            pb.updateValue(100, 100);
            pb.dispose();
            ex.printStackTrace();
        }
    }

    @Override public void actionPerformed(ActionEvent e) {
        final MyAnalysisGraphApp plusNetworkAnalyzer = this;
        new Thread(new Runnable() {
            @Override public void run() {
                try {
                    if (e.getSource() == addBtn) {
                        if (graphViewer.graphMouseListener.selectedNode == null && nodeOptionComboBoxMenu.getSelectedIndex() > 1) {
                            MyMessageUtil.showInfoMsg(plusNetworkAnalyzer, "Select a node, first.");
                            nodeOptionComboBoxMenu.removeActionListener(plusNetworkAnalyzer);
                            nodeOptionComboBoxMenu.setSelectedIndex(0);
                            nodeOptionComboBoxMenu.addActionListener(plusNetworkAnalyzer);
                        }/** else if (graphViewer.graphMouseListener.selectedNode != null && nodeOptionComboBoxMenu.getSelectedIndex() < 2) {
                            MyMessageUtil.showInfoMsg(plusNetworkAnalyzer, "Select a predecessor or successor.");
                            nodeOptionComboBoxMenu.removeActionListener(plusNetworkAnalyzer);
                            nodeOptionComboBoxMenu.setSelectedIndex(0);
                            nodeOptionComboBoxMenu.addActionListener(plusNetworkAnalyzer);
                        }*/ else {
                            addNodeToGraph();
                        }
                        nodeOptionComboBoxMenu.removeActionListener(plusNetworkAnalyzer);
                        nodeOptionComboBoxMenu.setSelectedIndex(0);
                        nodeOptionComboBoxMenu.addActionListener(plusNetworkAnalyzer);

                    } else if (e.getSource() == nodeOptionComboBoxMenu) {
                        if (graph.getVertexCount() == 0 && graphViewer.graphMouseListener.selectedNode == null && nodeOptionComboBoxMenu.getSelectedIndex() > 1) {
                            MyMessageUtil.showInfoMsg(plusNetworkAnalyzer, "Add a node to the graph.");
                            nodeOptionComboBoxMenu.removeActionListener(plusNetworkAnalyzer);
                            nodeOptionComboBoxMenu.setSelectedIndex(0);
                            nodeOptionComboBoxMenu.addActionListener(plusNetworkAnalyzer);
                        } else if (graphViewer.graphMouseListener.selectedNode != null && nodeOptionComboBoxMenu.getSelectedIndex() < 2) {
                            MyMessageUtil.showInfoMsg(plusNetworkAnalyzer, "Select a predecessor or successor.");
                            nodeOptionComboBoxMenu.removeActionListener(plusNetworkAnalyzer);
                            nodeOptionComboBoxMenu.setSelectedIndex(0);
                            nodeOptionComboBoxMenu.addActionListener(plusNetworkAnalyzer);
                        }

                        if (graphViewer.graphMouseListener.selectedNode == null && nodeOptionComboBoxMenu.getSelectedIndex() > 1) {
                            MyMessageUtil.showInfoMsg(plusNetworkAnalyzer, "Select a node, first.");
                        } else if (nodeOptionComboBoxMenu.getSelectedItem().toString().contains("NODE")) {
                            removeTableRecords();
                            loadNodes();
                        }/** else if (nodeOptionComboBoxMenu.getSelectedItem().toString().contains("PREDECESSOR")) {
                            removeTableRecords();
                            loadPredecessors();
                        }*/ else if (nodeOptionComboBoxMenu.getSelectedItem().toString().contains("SUCCESSOR")) {
                            removeTableRecords();
                            loadSuccessors();
                        } else {
                            addBtn.setEnabled(false);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    protected void addNodeToGraph() {
        try {
            String nodeName = this.table.getValueAt(table.getSelectedRow(), 1).toString();
            String contribution = this.table.getValueAt(table.getSelectedRow(), 2).toString();
            System.out.println(contribution);
            MyNode n = new MyNode(MySequentialGraphVars.nodeNameMap.get(nodeName));
            n.contribution = Long.parseLong(contribution);
            if (this.graph.vRefs.containsKey(n.getName())) {
                if (nodeOptionComboBoxMenu.getSelectedItem().toString().contains("SUCCESSOR")) {
                    String edge = graphViewer.graphMouseListener.selectedNode.getName() + "-" + n.getName();
                    if (!this.graph.edRefs.contains(edge)) {
                        MyEdge e = new MyEdge(0, graphViewer.graphMouseListener.selectedNode, n);
                        this.graph.addEdge(e, graphViewer.graphMouseListener.selectedNode, n);
                        this.graph.edRefs.add(edge);
                    } else {
                        String selectedNodeName = (graphViewer.graphMouseListener.selectedNode.getName().contains("x") ? MySequentialGraphSysUtil.getDecodeVariableNodeName(graphViewer.graphMouseListener.selectedNode.getName()) : MySequentialGraphSysUtil.getDecodedNodeName(graphViewer.graphMouseListener.selectedNode.getName()));
                        MyMessageUtil.showInfoMsg(this, "An edge between " + MySequentialGraphSysUtil.getNodeName(selectedNodeName) + " and " + MySequentialGraphSysUtil.getNodeName(n.getName()) + " already exists.");
                    }
                }
            } else if (this.nodeOptionComboBoxMenu.getSelectedItem().toString().contains("NODE")) {
                this.graph.vRefs.put(n.getName(), n);
                if (graphViewer.mouseClickedLocation != null) {
                    graphViewer.getGraphLayout().setLocation(n,
                        new Point2D.Double(graphViewer.mouseClickedLocation.getX()+150, graphViewer.mouseClickedLocation.getY()));
                    this.graph.addVertex(n);
                } else {
                    graphViewer.getGraphLayout().setLocation(n,
                        new Point2D.Double(getWidth()/2, getHeight()/2));
                    this.graph.addVertex(n);
                }
                graphViewer.mouseClickedLocation = null;

                if (graphViewer.MAX_NODE_VALUE < n.getContribution()) {
                    graphViewer.MAX_NODE_VALUE = n.getContribution();
                }
            }
            /** else if (nodeOptionComboBoxMenu.getSelectedItem().toString().contains("PREDECESSOR")) {
                this.graph.vRefs.put(n.getName(), n);
                if (graphViewer.mouseClickedLocation != null) {
                    graphViewer.getGraphLayout().setLocation(n, new Point2D.Double(graphViewer.mouseClickedLocation.getX()+100, graphViewer.mouseClickedLocation.getY()+150));
                    this.graph.addVertex(n);
                } else {
                    graphViewer.getGraphLayout().setLocation(n, new Point2D.Double(getWidth()/2, getHeight()/2));
                    this.graph.addVertex(n);
                }
                graphViewer.mouseClickedLocation = null;

                if (graphViewer.MAX_NODE_VALUE < n.getContribution()) {
                    graphViewer.MAX_NODE_VALUE = n.getContribution();
                }

                Collection<MyEdge> inEdges = MySequentialGraphVars.g.getInEdges(this.graphViewer.graphMouseListener.selectedNode);
                for (MyEdge e : inEdges) {
                    if (n.getName().equals(e.getSource().getName()) && e.getDest().getName().equals(graphViewer.graphMouseListener.selectedNode.getName())) {
                        this.graph.addEdge(e, n, graphViewer.graphMouseListener.selectedNode);
                        this.graph.edRefs.add(e.getSource().getName() + "-" + e.getDest().getName());
                        if (this.graphViewer.MAX_EDGE_VALUE < e.getContribution()) {
                            this.graphViewer.MAX_EDGE_VALUE = e.getContribution();
                        }
                        break;
                    }
                }
            }*/
            else if (this.nodeOptionComboBoxMenu.getSelectedItem().toString().contains("SUCCESSOR")) {
                this.graph.vRefs.put(n.getName(), n);
                if (graphViewer.mouseClickedLocation != null) {
                    graphViewer.getGraphLayout().setLocation(n, new Point2D.Double(graphViewer.mouseClickedLocation.getX() + 800, graphViewer.mouseClickedLocation.getY() + 150));
                    graphViewer.mouseClickedLocation = null;
                } else {
                    graphViewer.getGraphLayout().setLocation(n, new Point2D.Double(getWidth() / 2, getHeight() / 2));
                }

                this.graph.addVertex(n);

                if (graphViewer.MAX_NODE_VALUE < n.getContribution()) {
                    graphViewer.MAX_NODE_VALUE = n.getContribution();
                }

                String edgeName = graphViewer.graphMouseListener.selectedNode.getName() + "-" + n.getName();
                if (!this.graph.edRefs.contains(edgeName)) {
                    MyEdge e = new MyEdge((int) n.getContribution(), graphViewer.graphMouseListener.selectedNode, n);
                    this.graph.addEdge(e, graphViewer.graphMouseListener.selectedNode, n);
                    this.graph.edRefs.add(edgeName);
                    if (this.graphViewer.MAX_EDGE_VALUE < e.getContribution()) {
                        this.graphViewer.MAX_EDGE_VALUE = e.getContribution();
                    }
                }
            }

            int row = table.getRowCount();
            while (row > 0) {
                ((DefaultTableModel) table.getModel()).removeRow(row-1);
                row = table.getRowCount();
            }

            String statTxt = "N.: " + MyMathUtil.getCommaSeperatedNumber(graph.getVertexCount()) + "   " +
                             "E.: " + MyMathUtil.getCommaSeperatedNumber(graph.getEdgeCount()) + "   " +
                             "MAX. N. V.: " + MyMathUtil.twoDecimalFormat(graphViewer.MAX_NODE_VALUE) + "   " +
                             "MAX. E. V.: " + MyMathUtil.twoDecimalFormat(graphViewer.MAX_EDGE_VALUE);

            this.statisticLabel.setText(statTxt);
            this.graphViewer.revalidate();
            this.graphViewer.repaint();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override public void windowOpened(WindowEvent e) {dispose();}
    @Override public void windowClosing(WindowEvent e) {}
    @Override public void windowClosed(WindowEvent e) {}
    @Override public void windowIconified(WindowEvent e) {}
    @Override public void windowDeiconified(WindowEvent e) {}
    @Override public void windowActivated(WindowEvent e) {}
    @Override public void windowDeactivated(WindowEvent e) {}
}
