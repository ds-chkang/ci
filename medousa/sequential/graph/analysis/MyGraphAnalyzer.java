package medousa.sequential.graph.analysis;

import medousa.MyProgressBar;
import medousa.message.MyMessageUtil;
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
import java.util.Collection;
import java.util.LinkedHashMap;

public class MyGraphAnalyzer
extends JFrame
implements ActionListener, WindowListener {

    private MyGraph graph;
    protected JButton addBtn;
    private JTable table;
    private MyGraphAnalyzerViewer networkViewer;
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

    public MyGraphAnalyzer() {
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
        this.addBtn.setFont(MySequentialGraphVars.tahomaPlainFont11);
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
                            networkViewer.getRenderContext().setVertexLabelTransformer(new Transformer<MyNode, String>() {
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
        this.nodeOptionComboBoxMenu.setFont(MySequentialGraphVars.tahomaPlainFont11);
        this.nodeOptionComboBoxMenu.addItem("");
        this.nodeOptionComboBoxMenu.addItem("NODES");
        this.nodeOptionComboBoxMenu.addItem("PREDECESSORS");
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
        this.nodeValueCheckBoxMenu.setFont(MySequentialGraphVars.tahomaPlainFont11);
        this.nodeValueCheckBoxMenu.setFocusable(false);
        this.nodeValueCheckBoxMenu.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        if (nodeValueCheckBoxMenu.isSelected()) {
                            if (nodeNameCheckBoxMenu.isSelected()) {
                                networkViewer.getRenderContext().setVertexLabelTransformer(new Transformer<MyNode, String>() {
                                    @Override public String transform(MyNode n) {
                                        String inCont = "IN-C.: " + MyMathUtil.getCommaSeperatedNumber(n.getInContribution());
                                        String outCont = "OUT-C.: " + MyMathUtil.getCommaSeperatedNumber(n.getOutContribution());
                                        return MySequentialGraphSysUtil.getDecodedNodeName(n.getName()) + "[" +
                                                inCont + "  " + outCont + "] ";
                                    }
                                });
                            } else {
                                networkViewer.getRenderContext().setVertexLabelTransformer(new Transformer<MyNode, String>() {
                                    @Override public String transform(MyNode n) {
                                        String inCont = "IN-C.: " + MyMathUtil.getCommaSeperatedNumber(n.getInContribution());
                                        String outCont = "OUT-C.: " + MyMathUtil.getCommaSeperatedNumber(n.getOutContribution());
                                        return inCont + "  " + outCont;
                                    }
                                });
                            }
                        } else {
                            if (nodeNameCheckBoxMenu.isSelected()) {
                                networkViewer.getRenderContext().setVertexLabelTransformer(new Transformer<MyNode, String>() {
                                    @Override public String transform(MyNode n) {
                                        return MySequentialGraphSysUtil.getDecodedNodeName(n.getName());
                                    }
                                });
                            } else {
                                networkViewer.getRenderContext().setVertexLabelTransformer(new Transformer<MyNode, String>() {
                                    @Override public String transform(MyNode n) {
                                        return "";
                                    }
                                });
                            }
                        }
                        networkViewer.revalidate();
                        networkViewer.repaint();
                    }
                }).start();
            }
        });

        this.nodeNameCheckBoxMenu.setBackground(Color.WHITE);
        this.nodeNameCheckBoxMenu.setFont(MySequentialGraphVars.tahomaPlainFont11);
        this.nodeNameCheckBoxMenu.setFocusable(false);
        this.nodeNameCheckBoxMenu.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        if (nodeNameCheckBoxMenu.isSelected()) {
                            if (nodeValueCheckBoxMenu.isSelected()) {
                                networkViewer.getRenderContext().setVertexLabelTransformer(new Transformer<MyNode, String>() {
                                    @Override public String transform(MyNode n) {
                                        String inCont = "IN-C.: " + MyMathUtil.getCommaSeperatedNumber(n.getInContribution());
                                        String outCont = "OUT-C.: " + MyMathUtil.getCommaSeperatedNumber(n.getOutContribution());
                                        return MySequentialGraphSysUtil.getDecodedNodeName(n.getName()) + "[" +
                                                inCont + "  " + outCont + "] ";
                                    }
                                });
                            } else {
                                networkViewer.getRenderContext().setVertexLabelTransformer(new Transformer<MyNode, String>() {
                                    @Override public String transform(MyNode n) {
                                        return MySequentialGraphSysUtil.getDecodedNodeName(n.getName());
                                    }
                                });
                            }
                        } else {
                            if (nodeValueCheckBoxMenu.isSelected()) {
                                networkViewer.getRenderContext().setVertexLabelTransformer(new Transformer<MyNode, String>() {
                                    @Override public String transform(MyNode n) {
                                        return MyMathUtil.getCommaSeperatedNumber(n.getContribution());
                                    }
                                });
                            } else {
                                networkViewer.getRenderContext().setVertexLabelTransformer(new Transformer<MyNode, String>() {
                                    @Override public String transform(MyNode n) {
                                        return "";
                                    }
                                });
                            }
                        }
                        networkViewer.revalidate();
                        networkViewer.repaint();
                    }
                }).start();
            }
        });

        this.edgeValueCheckBoxMenu.setBackground(Color.WHITE);
        this.edgeValueCheckBoxMenu.setFont(MySequentialGraphVars.tahomaPlainFont11);
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

                            networkViewer.getRenderContext().setEdgeLabelTransformer(new Transformer<MyEdge, String>() {
                                @Override public String transform(MyEdge e) {
                                    String edgeContributionRatio = MyMathUtil.twoDecimalFormat(((double) e.getContribution() / e.getSource().getOutContribution()) * 100) + "%";
                                    return MyMathUtil.getCommaSeperatedNumber(e.getContribution()) + " / " + MyMathUtil.getCommaSeperatedNumber(e.getSource().getOutContribution()) + " = " + edgeContributionRatio + " ";
                                }
                            });

                        } else {
                            networkViewer.getRenderContext().setEdgeLabelTransformer(new Transformer<MyEdge, String>() {
                                @Override public String transform(MyEdge e) {
                                    return "";
                                }
                            });
                        }
                        networkViewer.revalidate();
                        networkViewer.repaint();
                    }
                }).start();
            }
        });

        JPanel topLeftPanel = new JPanel();
        topLeftPanel.setBackground(Color.WHITE);
        topLeftPanel.setLayout(new FlowLayout(FlowLayout.LEFT,3,3));

        this.nodeWeightCheckBoxMenu.setBackground(Color.WHITE);
        this.nodeWeightCheckBoxMenu.setFont(MySequentialGraphVars.tahomaPlainFont11);
        this.nodeWeightCheckBoxMenu.setFocusable(false);
        this.nodeWeightCheckBoxMenu.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        try {
                            if (nodeWeightCheckBoxMenu.isSelected()) {
                                networkViewer.getRenderContext().setVertexShapeTransformer(new Transformer<MyNode, Shape>() {
                                    @Override public Shape transform(MyNode n) {
                                        float sizeRatio = (float) n.getContribution() / networkViewer.MAX_NODE_VALUE;
                                        float nodeSize = sizeRatio * (networkViewer.MAX_NODE_SIZE);
                                        if (nodeSize < 25f) {nodeSize = 15f;}
                                            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
                                        }
                                    });
                                networkViewer.revalidate();
                                networkViewer.repaint();
                            } else {
                                networkViewer.getRenderContext().setVertexShapeTransformer(new Transformer<MyNode, Shape>() {
                                    @Override public Shape transform(MyNode n) {
                                        return new Ellipse2D.Double(-40, -40, 80, 80);
                                    }
                                });
                                networkViewer.revalidate();
                                networkViewer.repaint();
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
        this.edgeWeightCheckBoxMenu.setFont(MySequentialGraphVars.tahomaPlainFont11);
        this.edgeWeightCheckBoxMenu.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        if (edgeWeightCheckBoxMenu.isSelected()) {
                            networkViewer.getRenderContext().setEdgeStrokeTransformer(new Transformer<MyEdge, Stroke>() {
                                @Override public Stroke transform(MyEdge e) {
                                    return new BasicStroke(1f);
                                }
                            });
                            networkViewer.revalidate();
                            networkViewer.repaint();
                        } else {
                            networkViewer.getRenderContext().setEdgeStrokeTransformer(new Transformer<MyEdge, Stroke>() {
                                @Override public Stroke transform(MyEdge e) {
                                    return new BasicStroke(1f);
                                }
                            });
                            networkViewer.revalidate();
                            networkViewer.repaint();
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
        this.statisticLabel.setFont(MySequentialGraphVars.tahomaPlainFont11);
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

        this.graph = new MyGraph<>();
        MyStaticLayout staticLayout = new MyStaticLayout(this.graph, new Dimension(400, 500));
        this.networkViewer = new MyGraphAnalyzerViewer(new DefaultVisualizationModel<>(staticLayout, new Dimension(400, 500)), nodeOptionComboBoxMenu);
        leftPanel.add(topPanel, BorderLayout.NORTH);
        leftPanel.add(this.networkViewer, BorderLayout.CENTER);
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
        this.searchTxt.setFont(MySequentialGraphVars.f_bold_11);
        this.table.setRowSorter(sorter);
        //this.loadNodes(new MyProgressBar(false));
        JScrollPane tableScrollPane = new JScrollPane(this.table);
        tableScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(9, 0));
        tableScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 9));
        tableScrollPane.setBackground(Color.WHITE);
        return tableScrollPane;
    }

    private void loadNodes(MyProgressBar pb) {
        try {
            int cnt = 0;
            Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
            LinkedHashMap<String, Long> sortedNodes = new LinkedHashMap<>();
            for (MyNode n : nodes) {
                sortedNodes.put(n.getName(), n.getContribution());
            }

            sortedNodes = MySequentialGraphSysUtil.sortMapByLongValue(sortedNodes);
            double work = (double) 50 / nodes.size();
            double totalWork = 0;
            for (String n : sortedNodes.keySet()) {
                this.model.addRow(
                    new String[]{
                            String.valueOf(++cnt),
                            MySequentialGraphSysUtil.decodeNodeName(n),
                            MyMathUtil.getCommaSeperatedNumber(((MyNode) MySequentialGraphVars.g.vRefs.get(n)).getContribution())});
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
            Collection<MyNode> predecessors = MySequentialGraphVars.g.getPredecessors(networkViewer.graphMouseListener.selectedNode);
            for (MyNode n : predecessors) {
                sortedNodes.put(n.getName(), n.getContribution());
            }

            sortedNodes = MySequentialGraphSysUtil.sortMapByLongValue(sortedNodes);
            pb.updateValue(75, 100);
            for (String n : sortedNodes.keySet()) {
                this.model.addRow(new String[]{String.valueOf(++cnt), MySequentialGraphSysUtil.decodeNodeName(n), MyMathUtil.getCommaSeperatedNumber(sortedNodes.get(n))});
            }

            pb.updateValue(100, 100);
            pb.dispose();
        } catch (Exception ex) {}
    }

    private void loadSuccessors(MyProgressBar pb) {
        try {
            int cnt = 0;
            LinkedHashMap<String, Long> sortedNodes = new LinkedHashMap<>();
            Collection<MyNode> successors = MySequentialGraphVars.g.getSuccessors(networkViewer.graphMouseListener.selectedNode);
            for (MyNode n : successors) {
                sortedNodes.put(n.getName(), n.getContribution());
            }

            sortedNodes = MySequentialGraphSysUtil.sortMapByLongValue(sortedNodes);
            pb.updateValue(75, 100);
            for (String n : sortedNodes.keySet()) {
                this.model.addRow(new String[]{String.valueOf(++cnt), MySequentialGraphSysUtil.decodeNodeName(n), MyMathUtil.getCommaSeperatedNumber(sortedNodes.get(n))});
            }

            pb.updateValue(100, 100);
            pb.dispose();
        } catch (Exception ex) {}
    }

    @Override public void actionPerformed(ActionEvent e) {
        final MyGraphAnalyzer plusNetworkAnalyzer = this;
        try {
            new Thread(new Runnable() {
                @Override public void run() {
                    try {
                        if (e.getSource() == addBtn) {
                            if (networkViewer.graphMouseListener.selectedNode == null && nodeOptionComboBoxMenu.getSelectedIndex() > 1) {
                                MyMessageUtil.showInfoMsg(plusNetworkAnalyzer, "Select a node, first.");
                                nodeOptionComboBoxMenu.removeActionListener(plusNetworkAnalyzer);
                                nodeOptionComboBoxMenu.setSelectedIndex(0);
                                nodeOptionComboBoxMenu.addActionListener(plusNetworkAnalyzer);
                            } else if (networkViewer.graphMouseListener.selectedNode != null && nodeOptionComboBoxMenu.getSelectedIndex() < 2) {
                                MyMessageUtil.showInfoMsg(plusNetworkAnalyzer, "Select a predecessor or successor.");
                                nodeOptionComboBoxMenu.removeActionListener(plusNetworkAnalyzer);
                                nodeOptionComboBoxMenu.setSelectedIndex(0);
                                nodeOptionComboBoxMenu.addActionListener(plusNetworkAnalyzer);
                            }  else {
                                addNodeToGraph();
                                networkViewer.setEdgeContributionValue();
                                networkViewer.graphMouseListener.selectedNode = null;
                            }
                            nodeOptionComboBoxMenu.removeActionListener(plusNetworkAnalyzer);
                            nodeOptionComboBoxMenu.setSelectedIndex(0);
                            nodeOptionComboBoxMenu.addActionListener(plusNetworkAnalyzer);
                        } else if (e.getSource() == nodeOptionComboBoxMenu) {
                            if (graph.getVertexCount() == 0 && networkViewer.graphMouseListener.selectedNode == null  && nodeOptionComboBoxMenu.getSelectedIndex() > 1) {
                                MyMessageUtil.showInfoMsg(plusNetworkAnalyzer, "Add a node to the graph.");
                                nodeOptionComboBoxMenu.removeActionListener(plusNetworkAnalyzer);
                                nodeOptionComboBoxMenu.setSelectedIndex(0);
                                nodeOptionComboBoxMenu.addActionListener(plusNetworkAnalyzer);
                            } else if (networkViewer.graphMouseListener.selectedNode != null && nodeOptionComboBoxMenu.getSelectedIndex() < 2) {
                                MyMessageUtil.showInfoMsg(plusNetworkAnalyzer, "Select a predecessor or successor.");
                                nodeOptionComboBoxMenu.removeActionListener(plusNetworkAnalyzer);
                                nodeOptionComboBoxMenu.setSelectedIndex(0);
                                nodeOptionComboBoxMenu.addActionListener(plusNetworkAnalyzer);
                            } if (networkViewer.graphMouseListener.selectedNode == null && nodeOptionComboBoxMenu.getSelectedIndex() > 1) {
                                MyMessageUtil.showInfoMsg(plusNetworkAnalyzer, "Select a node, first.");
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
        MyNode n = ((MyNode) MySequentialGraphVars.g.vRefs.get(MySequentialGraphVars.nodeNameMap.get(tableNodeName)));
        if (this.graph.vRefs.containsKey(n.getName())) {
            if (nodeOptionComboBoxMenu.getSelectedIndex() <= 1) {
                MyMessageUtil.showInfoMsg(this, "The selected node already exists in the graph.");
                return;
            } else if (nodeOptionComboBoxMenu.getSelectedItem().toString().contains("SUCCESSOR")) {
                String edge = networkViewer.graphMouseListener.selectedNode.getName() + "-" + n.getName();
                if (!this.graph.edRefs.contains(edge)) {
                    Collection<MyEdge> outEdges = MySequentialGraphVars.g.getOutEdges(networkViewer.graphMouseListener.selectedNode);
                    for (MyEdge e : outEdges) {
                        if (e.getDest() == n) {
                            this.graph.addEdge(e, networkViewer.graphMouseListener.selectedNode, n);
                            this.graph.edRefs.add(edge);
                            break;
                        }
                    }
                } else {
                    String selectedNodeName = (networkViewer.graphMouseListener.selectedNode.getName().contains("x") ? MySequentialGraphSysUtil.decodeVariable(networkViewer.graphMouseListener.selectedNode.getName()) : MySequentialGraphSysUtil.getDecodedNodeName(networkViewer.graphMouseListener.selectedNode.getName()));
                    MyMessageUtil.showInfoMsg(this, "An edge between " + selectedNodeName + " and " + n.getName() + " already exists.");
                }
            }
        } else if (this.nodeOptionComboBoxMenu.getSelectedItem().toString().contains("NODE")) {
            this.graph.vRefs.put(n.getName(), n);
            if (networkViewer.mouseClickedLocation != null) {
                networkViewer.getGraphLayout().setLocation(n, new Point2D.Double(networkViewer.mouseClickedLocation.getX()+150, networkViewer.mouseClickedLocation.getY()));
                this.graph.addVertex(n);
            } else {
                networkViewer.getGraphLayout().setLocation(n, new Point2D.Double(getWidth()/2, getHeight()/2));
                this.graph.addVertex(n);
            }
            networkViewer.mouseClickedLocation = null;

            if (networkViewer.MAX_NODE_VALUE < n.getContribution()) {
                networkViewer.MAX_NODE_VALUE = n.getContribution();
            }
        } else if (nodeOptionComboBoxMenu.getSelectedItem().toString().contains("PREDECESSOR")) {
            this.graph.vRefs.put(n.getName(), n);
            if (networkViewer.mouseClickedLocation != null) {
                networkViewer.getGraphLayout().setLocation(n, new Point2D.Double(networkViewer.mouseClickedLocation.getX()+100, networkViewer.mouseClickedLocation.getY()+150));
                this.graph.addVertex(n);
            } else {
                networkViewer.getGraphLayout().setLocation(n, new Point2D.Double(getWidth()/2, getHeight()/2));
                this.graph.addVertex(n);
            }
            networkViewer.mouseClickedLocation = null;

            if (networkViewer.MAX_NODE_VALUE < n.getContribution()) {
                networkViewer.MAX_NODE_VALUE = n.getContribution();
            }

            Collection<MyEdge> inEdges = MySequentialGraphVars.g.getInEdges(this.networkViewer.graphMouseListener.selectedNode);
            for (MyEdge e : inEdges) {
                if (n.getName().equals(e.getSource().getName()) && e.getDest().getName().equals(networkViewer.graphMouseListener.selectedNode.getName())) {
                    this.graph.addEdge(e, n, networkViewer.graphMouseListener.selectedNode);
                    this.graph.edRefs.add(e.getSource().getName() + "-" + e.getDest().getName());
                    if (this.networkViewer.MAX_EDGE_VALUE < e.getContribution()) {
                        this.networkViewer.MAX_EDGE_VALUE = e.getContribution();
                    }
                    break;
                }
            }
        } else if (this.nodeOptionComboBoxMenu.getSelectedItem().toString().contains("SUCCESSOR")) {
            this.graph.vRefs.put(n.getName(), n);
            if (networkViewer.mouseClickedLocation != null) {
                networkViewer.getGraphLayout().setLocation(n, new Point2D.Double(networkViewer.mouseClickedLocation.getX()+800, networkViewer.mouseClickedLocation.getY()+150));
                this.graph.addVertex(n);
            } else {
                networkViewer.getGraphLayout().setLocation(n, new Point2D.Double(getWidth()/2, getHeight()/2));
                this.graph.addVertex(n);
            }
            networkViewer.mouseClickedLocation = null;
            if (networkViewer.MAX_NODE_VALUE < n.getContribution()) {
                networkViewer.MAX_NODE_VALUE = n.getContribution();
            }

            String userSelectedEdgeName = networkViewer.graphMouseListener.selectedNode.getName() + "-" + n.getName();
            if (!this.graph.edRefs.contains(userSelectedEdgeName)) {
                Collection<MyEdge> outEdges = MySequentialGraphVars.g.getOutEdges(networkViewer.graphMouseListener.selectedNode);
                for (MyEdge e : outEdges) {
                    String exEdName = e.getSource().getName() + "-" + e.getDest().getName();
                    if (exEdName.equals(userSelectedEdgeName)) {
                        this.graph.addEdge(e, networkViewer.graphMouseListener.selectedNode, n);
                        this.graph.edRefs.add(userSelectedEdgeName);
                        if (this.networkViewer.MAX_EDGE_VALUE < e.getContribution()) {
                            this.networkViewer.MAX_EDGE_VALUE = e.getContribution();
                        }
                        break;
                    }
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
                         "MAX. N. V.: " + MyMathUtil.twoDecimalFormat(networkViewer.MAX_NODE_VALUE) + "   " +
                         "MAX. E. V.: " + MyMathUtil.twoDecimalFormat(networkViewer.MAX_EDGE_VALUE);
        this.statisticLabel.setText(statTxt);
        this.networkViewer.revalidate();
        this.networkViewer.repaint();
    }

    @Override public void windowOpened(WindowEvent e) {dispose();}
    @Override public void windowClosing(WindowEvent e) {}
    @Override public void windowClosed(WindowEvent e) {}
    @Override public void windowIconified(WindowEvent e) {}
    @Override public void windowDeiconified(WindowEvent e) {}
    @Override public void windowActivated(WindowEvent e) {}
    @Override public void windowDeactivated(WindowEvent e) {}
}
