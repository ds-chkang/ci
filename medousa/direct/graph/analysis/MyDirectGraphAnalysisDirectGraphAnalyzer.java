package medousa.direct.graph.analysis;

import medousa.MyProgressBar;
import medousa.direct.graph.layout.MyDirectGraphStaticLayout;
import medousa.direct.graph.MyDirectEdge;
import medousa.direct.graph.MyDirectNode;
import medousa.direct.utils.MyDirectGraphMathUtil;
import medousa.message.MyMessageUtil;
import medousa.direct.utils.MyDirectGraphSysUtil;
import medousa.direct.utils.MyDirectGraphVars;
import medousa.table.MyTableUtil;
import edu.uci.ics.jung.visualization.DefaultVisualizationModel;
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
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;

public class MyDirectGraphAnalysisDirectGraphAnalyzer
extends JFrame
implements ActionListener, WindowListener, Serializable {

    private MyDirectGraphAnalysisGraph graph;
    protected JButton addBtn;
    private JTable table;
    private MyDirectGraphAnalysisDirectGraphViewer graphViewer;
    private DefaultTableModel model;
    private JTextField searchTxt = new JTextField();
    protected JComboBox nodeOptionComboBoxMenu = new JComboBox();
    private JComboBox depthOptionComboBox = new JComboBox();
    protected JComboBox edgeValueComboBoxMenu = new JComboBox();
    protected JComboBox nodeValueComboBoxMenu = new JComboBox();
    protected JCheckBox nodeValueCheckBoxMenu = new JCheckBox("N. V.");
    protected JCheckBox edgeValueCheckBoxMenu = new JCheckBox("E. V.");
    protected JCheckBox nodeNameCheckBoxMenu = new JCheckBox("N. N.");
    protected JCheckBox edgeWeightCheckBoxMenu = new JCheckBox("E. WGT.");
    protected JCheckBox nodeWeightCheckBoxMenu = new JCheckBox("N. WGT.");
    protected JLabel statisticLabel = new JLabel("");
    public MyDirectGraphAnalysisDirectGraphAnalyzer() {
        super("GRAPH RELATION EXPLORER");
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
        viewerSplitPane.setDividerLocation((int) (MyDirectGraphSysUtil.getViewerHeight()*0.83));
        viewerSplitPane.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
                viewerSplitPane.setDividerSize(5);
                viewerSplitPane.setDividerLocation((int) (getWidth()*0.83));
            }
        });
        this.getContentPane().add(viewerSplitPane, BorderLayout.CENTER);
        this.pack();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setAlwaysOnTop(true);
        this.addWindowListener(new WindowListener() {
            @Override public void windowOpened(WindowEvent e) {}
            @Override public void windowClosing(WindowEvent e) {

            }
            @Override public void windowClosed(WindowEvent e) {}
            @Override public void windowIconified(WindowEvent e) {}
            @Override public void windowDeiconified(WindowEvent e) {}
            @Override public void windowActivated(WindowEvent e) {}
            @Override public void windowDeactivated(WindowEvent e) {}
        });
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
        this.addBtn.setFont(MyDirectGraphVars.tahomaPlainFont11);
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
        edgeValueLabel.setFont(MyDirectGraphVars.tahomaPlainFont12);
        edgeValueLabel.setBackground(Color.WHITE);

        this.edgeValueComboBoxMenu.setBackground(Color.WHITE);
        this.edgeValueComboBoxMenu.setFocusable(false);
        this.edgeValueComboBoxMenu.setFont(MyDirectGraphVars.tahomaPlainFont12);
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
        nodeValueLabel.setFont(MyDirectGraphVars.tahomaPlainFont12);
        nodeValueLabel.setBackground(Color.WHITE);

        this.nodeValueComboBoxMenu.setBackground(Color.WHITE);
        this.nodeValueComboBoxMenu.setFocusable(false);
        this.nodeValueComboBoxMenu.setFont(MyDirectGraphVars.tahomaPlainFont12);
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
                        }
                    }
                }).start();
            }
        });

        JLabel nodeOptionLabel = new JLabel(" N. O.: ");
        nodeOptionLabel.setFont(MyDirectGraphVars.tahomaPlainFont12);
        nodeOptionLabel.setBackground(Color.WHITE);

        this.nodeOptionComboBoxMenu.setFocusable(false);
        this.nodeOptionComboBoxMenu.setBackground(Color.WHITE);
        this.nodeOptionComboBoxMenu.setFont(MyDirectGraphVars.tahomaPlainFont11);
        this.nodeOptionComboBoxMenu.addItem("");
        this.nodeOptionComboBoxMenu.addItem("NODES");
        this.nodeOptionComboBoxMenu.addItem("PREDECESSORS");
        this.nodeOptionComboBoxMenu.addItem("SUCCESSORS");
        this.nodeOptionComboBoxMenu.setSelectedIndex(0);
        this.nodeOptionComboBoxMenu.addActionListener(this);

        JPanel topRightPanel = new JPanel();
        topRightPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        topRightPanel.setBackground(Color.WHITE);
        topRightPanel.add(nodeValueLabel);
        topRightPanel.add(nodeValueComboBoxMenu);
        topRightPanel.add(edgeValueLabel);
        topRightPanel.add(this.edgeValueComboBoxMenu);
        topRightPanel.add(nodeOptionLabel);
        topRightPanel.add(this.nodeOptionComboBoxMenu);

        this.nodeValueCheckBoxMenu.setBackground(Color.WHITE);
        this.nodeValueCheckBoxMenu.setFont(MyDirectGraphVars.tahomaPlainFont11);
        this.nodeValueCheckBoxMenu.setFocusable(false);
        this.nodeValueCheckBoxMenu.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        if (nodeValueCheckBoxMenu.isSelected()) {
                            if (nodeNameCheckBoxMenu.isSelected()) {
                                graphViewer.getRenderContext().setVertexLabelTransformer(new Transformer<MyDirectNode, String>() {
                                    @Override public String transform(MyDirectNode n) {
                                        return n.getName() + "[" + MyDirectGraphMathUtil.getCommaSeperatedNumber(n.getContribution()) + "] ";
                                    }
                                });
                            } else {
                                graphViewer.getRenderContext().setVertexLabelTransformer(new Transformer<MyDirectNode, String>() {
                                    @Override public String transform(MyDirectNode n) {
                                        return MyDirectGraphMathUtil.getCommaSeperatedNumber(n.getContribution());
                                    }
                                });
                            }
                        } else {
                            if (nodeNameCheckBoxMenu.isSelected()) {
                                graphViewer.getRenderContext().setVertexLabelTransformer(new Transformer<MyDirectNode, String>() {
                                    @Override public String transform(MyDirectNode n) {
                                        return n.getName();
                                    }
                                });
                            } else {
                                graphViewer.getRenderContext().setVertexLabelTransformer(new Transformer<MyDirectNode, String>() {
                                    @Override public String transform(MyDirectNode n) {
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
        this.nodeNameCheckBoxMenu.setFont(MyDirectGraphVars.tahomaPlainFont11);
        this.nodeNameCheckBoxMenu.setFocusable(false);
        this.nodeNameCheckBoxMenu.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        if (nodeNameCheckBoxMenu.isSelected()) {
                            if (nodeValueCheckBoxMenu.isSelected()) {
                                graphViewer.getRenderContext().setVertexLabelTransformer(new Transformer<MyDirectNode, String>() {
                                    @Override public String transform(MyDirectNode n) {
                                        return n.getName() + "[" + MyDirectGraphMathUtil.getCommaSeperatedNumber(n.getContribution()) + "] ";
                                    }
                                });
                            } else {
                                graphViewer.getRenderContext().setVertexLabelTransformer(new Transformer<MyDirectNode, String>() {
                                    @Override public String transform(MyDirectNode n) {
                                        return n.getName();
                                    }
                                });
                            }
                        } else {
                            if (nodeValueCheckBoxMenu.isSelected()) {
                                graphViewer.getRenderContext().setVertexLabelTransformer(new Transformer<MyDirectNode, String>() {
                                    @Override public String transform(MyDirectNode n) {
                                        return MyDirectGraphMathUtil.getCommaSeperatedNumber(n.getContribution());
                                    }
                                });
                            } else {
                                graphViewer.getRenderContext().setVertexLabelTransformer(new Transformer<MyDirectNode, String>() {
                                    @Override public String transform(MyDirectNode n) {
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
        this.edgeValueCheckBoxMenu.setFont(MyDirectGraphVars.tahomaPlainFont11);
        this.edgeValueCheckBoxMenu.setFocusable(false);
        this.edgeValueCheckBoxMenu.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        if (edgeValueCheckBoxMenu.isSelected()) {
                            if (edgeValueComboBoxMenu.getSelectedIndex() == 0) {
                                MyMessageUtil.showInfoMsg(graphViewer.graphAnalyzer, "Select an edge value, first.");
                                edgeValueCheckBoxMenu.setSelected(false);
                                return;
                            }

                            graphViewer.getRenderContext().setEdgeLabelTransformer(new Transformer<MyDirectEdge, String>() {
                                @Override public String transform(MyDirectEdge e) {
                                    String edgeContributionRatio = MyDirectGraphMathUtil.twoDecimalFormat(((double) e.getContribution()/e.getSource().getContribution()) * 100) + "%";
                                    return MyDirectGraphMathUtil.getCommaSeperatedNumber(e.getContribution()) + " / " + MyDirectGraphMathUtil.getCommaSeperatedNumber(e.getSource().getContribution()) + " = " + edgeContributionRatio + " ";
                                }
                            });

                        } else {
                            graphViewer.getRenderContext().setEdgeLabelTransformer(new Transformer<MyDirectEdge, String>() {
                                @Override public String transform(MyDirectEdge e) {
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
        this.nodeWeightCheckBoxMenu.setFont(MyDirectGraphVars.tahomaPlainFont11);
        this.nodeWeightCheckBoxMenu.setFocusable(false);
        this.nodeWeightCheckBoxMenu.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        try {
                            if (nodeWeightCheckBoxMenu.isSelected()) {
                                if (nodeValueComboBoxMenu.getSelectedIndex() == 0) {
                                    MyMessageUtil.showInfoMsg(graphViewer.graphAnalyzer, "Select a node value, first.");
                                    nodeWeightCheckBoxMenu.setSelected(false);
                                    return;
                                }
                                if (nodeValueComboBoxMenu.getSelectedIndex() == 1) {
                                    edgeValueComboBoxMenu.setSelectedIndex(0);
                                    graphViewer.getRenderContext().setVertexShapeTransformer(new Transformer<MyDirectNode, Shape>() {
                                        @Override public Shape transform(MyDirectNode n) {
                                            float sizeRatio = (float) n.getContribution() / graphViewer.MAX_NODE_VALUE;
                                            float nodeSize = sizeRatio * (graphViewer.MAX_NODE_SIZE);
                                            if (nodeSize < 25f) {
                                                nodeSize = 15f;
                                            }
                                            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
                                        }
                                    });
                                    edgeValueComboBoxMenu.setSelectedIndex(1);
                                } else if (nodeValueComboBoxMenu.getSelectedIndex() == 0) {
                                    graphViewer.getRenderContext().setVertexShapeTransformer(new Transformer<MyDirectNode, Shape>() {
                                        @Override public Shape transform(MyDirectNode n) {
                                            return new Ellipse2D.Double(-40, -40, 80, 80);
                                        }
                                    });
                                }
                                graphViewer.revalidate();
                                graphViewer.repaint();
                            } else {
                                graphViewer.getRenderContext().setVertexShapeTransformer(new Transformer<MyDirectNode, Shape>() {
                                    @Override public Shape transform(MyDirectNode n) {
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
        this.edgeWeightCheckBoxMenu.setFont(MyDirectGraphVars.tahomaPlainFont11);
        this.edgeWeightCheckBoxMenu.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        if (edgeWeightCheckBoxMenu.isSelected()) {
                            if (edgeValueComboBoxMenu.getSelectedIndex() == 0) {
                                MyMessageUtil.showInfoMsg(graphViewer.graphAnalyzer, "Select a node value, first.");
                                edgeWeightCheckBoxMenu.setSelected(false);
                                return;
                            }
                            if (edgeValueComboBoxMenu.getSelectedIndex() == 0) {
                                graphViewer.getRenderContext().setEdgeStrokeTransformer(new Transformer<MyDirectEdge, Stroke>() {
                                    @Override public Stroke transform(MyDirectEdge e) {
                                        return new BasicStroke(1f);
                                    }
                                });
                            } else if (edgeValueComboBoxMenu.getSelectedIndex() == 1) {
                                graphViewer.getRenderContext().setEdgeStrokeTransformer(new Transformer<MyDirectEdge, Stroke>() {
                                    @Override public Stroke transform(MyDirectEdge e) {
                                        return new BasicStroke((((float) e.getContribution() / graphViewer.MAX_EDGE_VALUE) * graphViewer.MAX_EDGE_SIZE)/2);
                                    }
                                });
                            }
                            graphViewer.revalidate();
                            graphViewer.repaint();
                        } else {
                            graphViewer.getRenderContext().setEdgeStrokeTransformer(new Transformer<MyDirectEdge, Stroke>() {
                                @Override public Stroke transform(MyDirectEdge e) {
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
        this.statisticLabel.setFont(MyDirectGraphVars.tahomaPlainFont11);
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

        this.graph = new MyDirectGraphAnalysisGraph<>();
        MyDirectGraphStaticLayout staticLayout = new MyDirectGraphStaticLayout(this.graph, new Dimension(400, 500));
        this.graphViewer = new MyDirectGraphAnalysisDirectGraphViewer(new DefaultVisualizationModel<>(staticLayout, new Dimension(400, 500)),  this);
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
        this.table.getTableHeader().setFont(MyDirectGraphVars.tahomaBoldFont11);
        this.table.getTableHeader().setOpaque(false);
        this.table.getTableHeader().setBackground(new Color(0,0,0,0));
        this.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.table.setFont(MyDirectGraphVars.f_pln_12);
        Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
        this.table.setCursor(cursor);
        this.table.setRowHeight(23);
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
        this.table.getColumnModel().getColumn(0).setMaxWidth(55);
        this.table.getColumnModel().getColumn(1).setPreferredWidth(100);
        this.table.getColumnModel().getColumn(2).setPreferredWidth(45);
        this.table.getColumnModel().getColumn(2).setMaxWidth(60);
        TableRowSorter sorter = MyTableUtil.setJTableRowSorterWithTextField(this.model, this.searchTxt);
        this.searchTxt.setPreferredSize(new Dimension(100, 20));
        this.searchTxt.setBorder(BorderFactory.createEtchedBorder());
        this.searchTxt.setFont(MyDirectGraphVars.f_bold_11);
        this.table.setRowSorter(sorter);
        JScrollPane tableScrollPane = new JScrollPane(this.table);
        tableScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(9, 0));
        tableScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 9));
        tableScrollPane.setBackground(Color.WHITE);
        return tableScrollPane;
    }

    private void loadNodes(MyProgressBar pb) {
        try {
            int cnt = 0;
            Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
            LinkedHashMap<String, Long> sortedNodes = new LinkedHashMap<>();
            for (MyDirectNode n : nodes) {
                sortedNodes.put(n.getName(), (long)n.getContribution());
            }

            sortedNodes = MyDirectGraphSysUtil.sortMapByLongValue(sortedNodes);
            double work = (double) 50 / nodes.size();
            double totalWork = 0;
            for (String n : sortedNodes.keySet()) {
                this.model.addRow(
                    new String[]{
                            String.valueOf(++cnt),
                            n,
                            MyDirectGraphMathUtil.getCommaSeperatedNumber(((MyDirectNode) MyDirectGraphVars.directGraph.vRefs.get(n)).getContribution())});
                totalWork += work;
                pb.updateValue((int) totalWork, nodes.size());
            }

            pb.updateValue(100, 100);
            pb.dispose();
        } catch (Exception ex) {}
    }

    private void removeTableRecords(MyProgressBar pb) {
        int cnt = 0;
        int totalRecord = this.table.getRowCount();

        for (int i=this.table.getRowCount()-1; i >= 0; i--) {
            this.model.removeRow(i);
            pb.updateValue(++cnt, totalRecord);
        }

        this.table.revalidate();
        this.table.repaint();
        pb.updateValue(50, 100);
    }

    private void loadPredecessors(MyProgressBar pb) {
        try {
            int cnt = 0;
            LinkedHashMap<String, Long> sortedNodes = new LinkedHashMap<>();
            Collection<MyDirectNode> predecessors = MyDirectGraphVars.directGraph.getPredecessors(graphViewer.graphMouseListener.selectedNode);
            for (MyDirectNode n : predecessors) {
                sortedNodes.put(n.getName(), (long)n.getContribution());
            }

            sortedNodes = MyDirectGraphSysUtil.sortMapByLongValue(sortedNodes);
            pb.updateValue(75, 100);
            for (String n : sortedNodes.keySet()) {
                this.model.addRow(new String[]{String.valueOf(++cnt), n, MyDirectGraphMathUtil.getCommaSeperatedNumber(sortedNodes.get(n))});
            }

            pb.updateValue(100, 100);
            pb.dispose();
        } catch (Exception ex) {}
    }

    private void loadSuccessors(MyProgressBar pb) {
        try {
            int cnt = 0;
            LinkedHashMap<String, Long> sortedNodes = new LinkedHashMap<>();
            Collection<MyDirectNode> successors = MyDirectGraphVars.directGraph.getSuccessors(graphViewer.graphMouseListener.selectedNode);
            for (MyDirectNode n : successors) {
                sortedNodes.put(n.getName(), (long)n.getContribution());
            }

            sortedNodes = MyDirectGraphSysUtil.sortMapByLongValue(sortedNodes);
            pb.updateValue(75, 100);
            for (String n : sortedNodes.keySet()) {
                this.model.addRow(new String[]{String.valueOf(++cnt), n, MyDirectGraphMathUtil.getCommaSeperatedNumber(sortedNodes.get(n))});
            }

            pb.updateValue(100, 100);
            pb.dispose();
        } catch (Exception ex) {}
    }

    @Override public void actionPerformed(ActionEvent e) {
        final MyDirectGraphAnalysisDirectGraphAnalyzer networkAnalyzer = this;
        try {
            new Thread(new Runnable() {
                @Override public void run() {
                    try {
                        if (e.getSource() == addBtn) {
                            if (graphViewer.graphMouseListener.selectedNode == null && nodeOptionComboBoxMenu.getSelectedIndex() > 1) {
                                MyMessageUtil.showInfoMsg(networkAnalyzer, "Select a node, first.");
                                nodeOptionComboBoxMenu.removeActionListener(networkAnalyzer);
                                nodeOptionComboBoxMenu.setSelectedIndex(0);
                                nodeOptionComboBoxMenu.addActionListener(networkAnalyzer);
                            } else if (graphViewer.graphMouseListener.selectedNode != null && nodeOptionComboBoxMenu.getSelectedIndex() < 2) {
                                MyMessageUtil.showInfoMsg(networkAnalyzer, "Select a predecessor or successor.");
                                nodeOptionComboBoxMenu.removeActionListener(networkAnalyzer);
                                nodeOptionComboBoxMenu.setSelectedIndex(0);
                                nodeOptionComboBoxMenu.addActionListener(networkAnalyzer);
                            }  else {
                                addNodeToGraph();
                                graphViewer.setEdgeContributionValue();
                                graphViewer.graphMouseListener.selectedNode = null;
                                nodeOptionComboBoxMenu.removeActionListener(networkAnalyzer);
                                nodeOptionComboBoxMenu.setSelectedIndex(0);
                                nodeOptionComboBoxMenu.addActionListener(networkAnalyzer);
                            }
                        } else if (e.getSource() == nodeOptionComboBoxMenu) {
                            if (graph.getVertexCount() == 0 && graphViewer.graphMouseListener.selectedNode == null  && nodeOptionComboBoxMenu.getSelectedIndex() > 1) {
                                MyMessageUtil.showInfoMsg(networkAnalyzer, "Add a node to the graph.");
                                nodeOptionComboBoxMenu.removeActionListener(networkAnalyzer);
                                nodeOptionComboBoxMenu.setSelectedIndex(0);
                                nodeOptionComboBoxMenu.addActionListener(networkAnalyzer);
                            } else if (graphViewer.graphMouseListener.selectedNode != null && nodeOptionComboBoxMenu.getSelectedIndex() < 2) {
                                MyMessageUtil.showInfoMsg(networkAnalyzer, "Select a predecessor or successor.");
                                nodeOptionComboBoxMenu.removeActionListener(networkAnalyzer);
                                nodeOptionComboBoxMenu.setSelectedIndex(0);
                                nodeOptionComboBoxMenu.addActionListener(networkAnalyzer);
                            } if (graphViewer.graphMouseListener.selectedNode == null && nodeOptionComboBoxMenu.getSelectedIndex() > 1) {
                                MyMessageUtil.showInfoMsg(networkAnalyzer, "Select a node, first.");
                            } else if (nodeOptionComboBoxMenu.getSelectedItem().toString().contains("NODE")) {
                                MyProgressBar pb = new MyProgressBar(false);
                                removeTableRecords(pb);
                                loadNodes(pb);
                            } else if (nodeOptionComboBoxMenu.getSelectedItem().toString().contains("PREDECESSOR")) {
                                MyProgressBar pb = new MyProgressBar(false);
                                removeTableRecords(pb);
                                loadPredecessors(pb);
                            } else if (nodeOptionComboBoxMenu.getSelectedItem().toString().contains("SUCCESSOR")) {
                                MyProgressBar pb = new MyProgressBar(false);
                                removeTableRecords(pb);
                                loadSuccessors(pb);
                            } else {
                                addBtn.setEnabled(false);
                            }
                        }
                    } catch (Exception ex) {}
                }
            }).start();
        } catch (Exception ex) {ex.printStackTrace();}
    }

    protected void addNodeToGraph() {
        String tableNodeName = this.table.getValueAt(table.getSelectedRow(), 1).toString();
        MyDirectNode n = ((MyDirectNode) MyDirectGraphVars.directGraph.vRefs.get(tableNodeName));
        if (this.graph.vRefs.containsKey(n.getName())) {
            if (nodeOptionComboBoxMenu.getSelectedIndex() <= 1) {
                MyMessageUtil.showInfoMsg(this, "The selected node already exists in the graph.");
                return;
            } else if (nodeOptionComboBoxMenu.getSelectedItem().toString().contains("SUCCESSOR")) {
                String edge = graphViewer.graphMouseListener.selectedNode.getName() + "-" + n.getName();
                if (!this.graph.edRefs.contains(edge)) {
                    Collection<MyDirectEdge> outEdges = MyDirectGraphVars.directGraph.getOutEdges(graphViewer.graphMouseListener.selectedNode);
                    for (MyDirectEdge e : outEdges) {
                        if (e.getDest() == n) {
                            this.graph.addEdge(e, graphViewer.graphMouseListener.selectedNode, n);
                            this.graph.edRefs.add(edge);
                            break;
                        }
                    }
                } else {
                    String selectedNodeName = (graphViewer.graphMouseListener.selectedNode.getName().contains("x") ? MyDirectGraphSysUtil.decodeVariable(graphViewer.graphMouseListener.selectedNode.getName()) : MyDirectGraphSysUtil.getDecodedNodeName(graphViewer.graphMouseListener.selectedNode.getName()));
                    MyMessageUtil.showInfoMsg(this, "An edge between " + selectedNodeName + " and " + n.getName() + " already exists.");
                }
            }
        } else if (this.nodeOptionComboBoxMenu.getSelectedItem().toString().contains("NODE")) {
            this.graph.vRefs.put(n.getName(), n);
            if (graphViewer.mouseClickedLocation != null) {
                graphViewer.getGraphLayout().setLocation(n, new Point2D.Double(graphViewer.mouseClickedLocation.getX()+150, graphViewer.mouseClickedLocation.getY()));
                this.graph.addVertex(n);
            } else {
                graphViewer.getGraphLayout().setLocation(n, new Point2D.Double(getWidth()/2, getHeight()/2));
                this.graph.addVertex(n);
            }
            graphViewer.mouseClickedLocation = null;

            if (graphViewer.MAX_NODE_VALUE < n.getContribution()) {
                graphViewer.MAX_NODE_VALUE = n.getContribution();
            }
        } else if (nodeOptionComboBoxMenu.getSelectedItem().toString().contains("PREDECESSOR")) {
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

            Collection<MyDirectEdge> inEdges = MyDirectGraphVars.directGraph.getInEdges(this.graphViewer.graphMouseListener.selectedNode);
            for (MyDirectEdge e : inEdges) {
                if (n.getName().equals(e.getSource().getName()) && e.getDest().getName().equals(graphViewer.graphMouseListener.selectedNode.getName())) {
                    this.graph.addEdge(e, n, graphViewer.graphMouseListener.selectedNode);
                    this.graph.edRefs.add(e.getSource().getName() + "-" + e.getDest().getName());
                    if (this.graphViewer.MAX_EDGE_VALUE < e.getContribution()) {
                        this.graphViewer.MAX_EDGE_VALUE = e.getContribution();
                    }
                    break;
                }
            }
        } else if (this.nodeOptionComboBoxMenu.getSelectedItem().toString().contains("SUCCESSOR")) {
            this.graph.vRefs.put(n.getName(), n);
            if (graphViewer.mouseClickedLocation != null) {
                graphViewer.getGraphLayout().setLocation(n, new Point2D.Double(graphViewer.mouseClickedLocation.getX()+800, graphViewer.mouseClickedLocation.getY()+150));
                this.graph.addVertex(n);
            } else {
                graphViewer.getGraphLayout().setLocation(n, new Point2D.Double(getWidth()/2, getHeight()/2));
                this.graph.addVertex(n);
            }
            graphViewer.mouseClickedLocation = null;
            if (graphViewer.MAX_NODE_VALUE < n.getContribution()) {
                graphViewer.MAX_NODE_VALUE = n.getContribution();
            }

            String userSelectedEdgeName = graphViewer.graphMouseListener.selectedNode.getName() + "-" + n.getName();
            if (!this.graph.edRefs.contains(userSelectedEdgeName)) {
                Collection<MyDirectEdge> outEdges = MyDirectGraphVars.directGraph.getOutEdges(graphViewer.graphMouseListener.selectedNode);
                for (MyDirectEdge e : outEdges) {
                    String exEdName = e.getSource().getName() + "-" + e.getDest().getName();
                    if (exEdName.equals(userSelectedEdgeName)) {
                        this.graph.addEdge(e, graphViewer.graphMouseListener.selectedNode, n);
                        this.graph.edRefs.add(userSelectedEdgeName);
                        if (this.graphViewer.MAX_EDGE_VALUE < e.getContribution()) {
                            this.graphViewer.MAX_EDGE_VALUE = e.getContribution();
                        }
                        break;
                    }
                }
            }
        }

        for (int i = table.getRowCount() - 1; i >= 0; i--) {
            ((DefaultTableModel) table.getModel()).removeRow(i);
        }

        String statTxt = "N.: " + MyDirectGraphMathUtil.getCommaSeperatedNumber(graph.getVertexCount()) + "   " +
                         "E.: " + MyDirectGraphMathUtil.getCommaSeperatedNumber(graph.getEdgeCount()) + "   " +
                         "MAX. N. V.: " + MyDirectGraphMathUtil.twoDecimalFormat(graphViewer.MAX_NODE_VALUE) + "   " +
                         "MAX. E. V.: " + MyDirectGraphMathUtil.twoDecimalFormat(graphViewer.MAX_EDGE_VALUE);
        this.statisticLabel.setText(statTxt);
        this.graphViewer.revalidate();
        this.graphViewer.repaint();
    }

    @Override public void windowOpened(WindowEvent e) {dispose();}
    @Override public void windowClosing(WindowEvent e) {}
    @Override public void windowClosed(WindowEvent e) {}
    @Override public void windowIconified(WindowEvent e) {}
    @Override public void windowDeiconified(WindowEvent e) {}
    @Override public void windowActivated(WindowEvent e) {}
    @Override public void windowDeactivated(WindowEvent e) {}
}
