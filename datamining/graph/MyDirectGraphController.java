package datamining.graph;

import datamining.graph.barcharts.MyMultiNodeLevelNeighborNodeValueBarChart;
import datamining.graph.barcharts.MyNeighborNodeValueBarChart;
import datamining.graph.barcharts.MyNodeValueBarChart;
import datamining.utils.message.MyMessageUtil;
import datamining.utils.MyMathUtil;
import datamining.utils.system.MySysUtil;
import datamining.utils.system.MyVars;
import datamining.main.MyProgressBar;
import org.apache.commons.collections15.Transformer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class MyDirectGraphController
extends JPanel
implements ActionListener {

    protected JTextField nodeValueExcludeTxt;
    protected JTextField edgeValueExcludeTxt;
    public JComboBox nodeValueExcludeComboBoxMenu;
    public JComboBox nodeLabelExcludeComboBoxMenu;
    public JComboBox edgeLabelExcludeComboBoxMenu;
    public JComboBox edgeLabelValueExcludeComboBoxMenu;
    public JComboBox nodeLabelValueExcludeComboBoxMenu;
    public JComboBox nodeValueExcludeSymbolComboBoxMenu;
    public JComboBox edgeValueExcludeSymbolComboBoxMenu;
    public JComboBox nodeLabelExcludeSymbolComboBoxMenu;

    public JComboBox edgeLabelExcludeSymbolComboBoxMenu;
    public JComboBox edgeLabelComboBoxMenu;
    public JComboBox edgeValueComboBoxMenu;
    public JComboBox nodeValueComboBoxMenu;
    public JComboBox nodeLabelComboBoxMenu;
    public JButton excludeBtn;
    public JCheckBox edgeValueCheckBox = new JCheckBox("E. V.");
    public JCheckBox mouseHoverCheckBox = new JCheckBox("M. H.");
    public JCheckBox nodeValueCheckBox = new JCheckBox("N. V.");
    public JCheckBox edgeLabelCheckBox = new JCheckBox("E. L.");
    public JCheckBox nodeLabelCheckBox = new JCheckBox("N. L.");
    public JCheckBox removeEdgeCheckBox = new JCheckBox("R. E.");
    public JCheckBox weightedNodeColorCheckBox = new JCheckBox("N. C.");
    public JLabel percentLabel = new JLabel();
    public JPanel checkBoxControlPanel = new JPanel();

    public MyDirectGraphController() {
        this.decorate();
    }

    public void decorate() {
        this.setBackground(Color.WHITE);
        this.setLayout(new BorderLayout(3,3));

        JLabel nodeLabelLabel = new JLabel("N. L.:");
        nodeLabelLabel.setToolTipText("NODE LABEL");
        nodeLabelLabel.setFont(MyVars.tahomaPlainFont11);
        nodeLabelLabel.setBackground(Color.WHITE);

        JLabel nodeValueLabel = new JLabel("N. V.:");
        nodeValueLabel.setFont(MyVars.tahomaPlainFont11);
        nodeValueLabel.setBackground(Color.WHITE);
        nodeValueLabel.setToolTipText("NODE VALUE");

        JLabel edgeLabelLabel = new JLabel("E. L.:");
        edgeLabelLabel.setFont(MyVars.tahomaPlainFont11);
        edgeLabelLabel.setBackground(Color.WHITE);
        edgeLabelLabel.setToolTipText("EDGE LABEL");

        JLabel edgeValueLabel = new JLabel("E. V.:");
        edgeValueLabel.setFont(MyVars.tahomaPlainFont11);
        edgeValueLabel.setBackground(Color.WHITE);
        edgeValueLabel.setToolTipText("EDGE VALUE");

        this.edgeLabelComboBoxMenu = new JComboBox();
        this.edgeLabelComboBoxMenu.setToolTipText("SELECT AN EDGE LABEL");
        this.edgeLabelComboBoxMenu.setFont(MyVars.tahomaPlainFont11);
        this.edgeLabelComboBoxMenu.setBackground(Color.WHITE);
        this.edgeLabelComboBoxMenu.setFocusable(false);
        this.edgeLabelComboBoxMenu.addItem("");
        for (String edgeLabel : MyVars.edgeLabels) {
            this.edgeLabelComboBoxMenu.addItem(edgeLabel);
        }

        this.edgeValueComboBoxMenu = new JComboBox();
        this.edgeValueComboBoxMenu.setToolTipText("SELECT AN EDGE VALUE");
        this.edgeValueComboBoxMenu.setFont(MyVars.tahomaPlainFont11);
        this.edgeValueComboBoxMenu.setBackground(Color.WHITE);
        this.edgeValueComboBoxMenu.setFocusable(false);
        this.edgeValueComboBoxMenu.addItem("");
        this.edgeValueComboBoxMenu.addItem("CONT.");
        this.edgeValueComboBoxMenu.addItem("BTW.");
        for (String edgeValue : MyVars.edgeValues.keySet()) {this.edgeValueComboBoxMenu.addItem(edgeValue);}

        this.nodeLabelComboBoxMenu = new JComboBox();
        this.nodeLabelComboBoxMenu.setToolTipText("SELECT A NODE LABEL");
        this.nodeLabelComboBoxMenu.setFont(MyVars.tahomaPlainFont11);
        this.nodeLabelComboBoxMenu.setBackground(Color.WHITE);
        this.nodeLabelComboBoxMenu.setFocusable(false);
        this.nodeLabelComboBoxMenu.addItem("");
        this.nodeLabelComboBoxMenu.addItem("NAME");
        for (String nodeLabel : MyVars.nodeLabels) {this.nodeLabelComboBoxMenu.addItem(nodeLabel);}

        this.nodeValueComboBoxMenu = new JComboBox();
        this.nodeValueComboBoxMenu.setToolTipText("SELECT A NODE VALUE");
        this.nodeValueComboBoxMenu.setFont(MyVars.tahomaPlainFont11);
        this.nodeValueComboBoxMenu.setBackground(Color.WHITE);
        this.nodeValueComboBoxMenu.setFocusable(false);
        this.nodeValueComboBoxMenu.addItem("CONT.");
        this.nodeValueComboBoxMenu.addItem("IN-CONT.");
        this.nodeValueComboBoxMenu.addItem("OUT-CONT.");
        this.nodeValueComboBoxMenu.addItem("PRED.");
        this.nodeValueComboBoxMenu.addItem("SUCC.");
        this.nodeValueComboBoxMenu.addItem("AVG. S. R.");
        this.nodeValueComboBoxMenu.addItem("BTW.");
        this.nodeValueComboBoxMenu.addItem("CLO.");
        this.nodeValueComboBoxMenu.addItem("EIGEN.");
        this.nodeValueComboBoxMenu.addItem("PAGERANK");
        for (String nodeValueVariable : MyVars.nodeValues.keySet()) {this.nodeValueComboBoxMenu.addItem(nodeValueVariable);}

        this.edgeLabelCheckBox.setFocusable(false);
        this.edgeLabelCheckBox.setFont(MyVars.tahomaPlainFont11);
        this.edgeLabelCheckBox.setBackground(Color.WHITE);

        this.edgeValueCheckBox.setFocusable(false);
        this.edgeValueCheckBox.setFont(MyVars.tahomaPlainFont11);
        this.edgeValueCheckBox.setBackground(Color.WHITE);

        this.nodeLabelExcludeComboBoxMenu = new JComboBox();
        this.nodeLabelExcludeComboBoxMenu.setToolTipText("SELECT NODE LABEL TO EXCLUDE FROM THE GRAPH.");
        this.nodeLabelExcludeComboBoxMenu.setFocusable(false);
        this.nodeLabelExcludeComboBoxMenu.setFont(MyVars.tahomaPlainFont11);
        this.nodeLabelExcludeComboBoxMenu.setBackground(Color.WHITE);
        for (String nodeLabelValue : MyVars.nodeLabels) {
            this.nodeLabelExcludeComboBoxMenu.addItem(nodeLabelValue);
        }
        this.nodeLabelExcludeComboBoxMenu.addActionListener(this);

        this.nodeLabelValueExcludeComboBoxMenu = new JComboBox();
        this.nodeLabelValueExcludeComboBoxMenu.setToolTipText("SELECT NODE LABEL VALUE TO EXCLUDE FROM THE GRAPH.");
        this.nodeLabelValueExcludeComboBoxMenu.setFocusable(false);
        this.nodeLabelValueExcludeComboBoxMenu.setFont(MyVars.tahomaPlainFont11);
        this.nodeLabelValueExcludeComboBoxMenu.setBackground(Color.WHITE);

        this.edgeLabelValueExcludeComboBoxMenu = new JComboBox();
        this.edgeLabelValueExcludeComboBoxMenu.setToolTipText("SELECT EDGE LABEL VALUE TO EXCLUDE FROM THE GRAPH.");
        this.edgeLabelValueExcludeComboBoxMenu.setFocusable(false);
        this.edgeLabelValueExcludeComboBoxMenu.setFont(MyVars.tahomaPlainFont11);
        this.edgeLabelValueExcludeComboBoxMenu.setBackground(Color.WHITE);

        this.edgeLabelExcludeComboBoxMenu = new JComboBox();
        this.edgeLabelExcludeComboBoxMenu.setToolTipText("");
        this.edgeLabelExcludeComboBoxMenu.setFocusable(false);
        this.edgeLabelExcludeComboBoxMenu.setFont(MyVars.tahomaPlainFont11);
        this.edgeLabelExcludeComboBoxMenu.setBackground(Color.WHITE);
        for (String nodeLabelValue : MyVars.edgeLabels) {
            this.edgeLabelExcludeComboBoxMenu.addItem(nodeLabelValue);
        }

        this.excludeBtn = new JButton("EXCL.");
        this.excludeBtn.setToolTipText("EXCLUDE");
        this.excludeBtn.setBackground(Color.WHITE);
        this.excludeBtn.setFocusable(false);
        this.excludeBtn.setFont(MyVars.tahomaPlainFont11);
        this.excludeBtn.addActionListener(this);
        this.edgeValueComboBoxMenu.addActionListener(this);
        this.edgeLabelComboBoxMenu.addActionListener(this);

        this.nodeLabelCheckBox.setToolTipText("SHOW NODE LABELS");
        this.nodeLabelCheckBox.setFocusable(false);
        this.nodeLabelCheckBox.setFont(MyVars.tahomaPlainFont11);
        this.nodeLabelCheckBox.setBackground(Color.WHITE);

        this.nodeValueCheckBox.setToolTipText("SHOW NODE VALUES");
        this.nodeValueCheckBox.setFont(MyVars.tahomaPlainFont11);
        this.nodeValueCheckBox.setBackground(Color.WHITE);
        this.nodeValueCheckBox.setFocusable(false);
        this.nodeLabelCheckBox.addActionListener(this);
        this.nodeValueCheckBox.addActionListener(this);
        this.edgeValueCheckBox.addActionListener(this);
        this.removeEdgeCheckBox.setToolTipText("REMOVE EDGES");
        this.removeEdgeCheckBox.setFont(MyVars.tahomaPlainFont11);
        this.removeEdgeCheckBox.setFocusable(false);
        this.removeEdgeCheckBox.setBackground(Color.WHITE);
        this.mouseHoverCheckBox.setToolTipText("HOVER MOUSE OVER NODES");
        this.mouseHoverCheckBox.setFont(MyVars.tahomaPlainFont11);
        this.mouseHoverCheckBox.setFocusable(false);
        this.mouseHoverCheckBox.setBackground(Color.WHITE);


        this.removeEdgeCheckBox.addActionListener(this);
        this.edgeLabelCheckBox.addActionListener(this);
        this.nodeValueComboBoxMenu.addActionListener(this);
        this.nodeLabelComboBoxMenu.addActionListener(this);

        JLabel nodeValueExcludeLabel = new JLabel("N. V.");
        nodeValueExcludeLabel.setToolTipText("SELECT A NODE VALUE TO EXCLUDE");
        nodeValueExcludeLabel.setFocusable(false);
        nodeValueExcludeLabel.setBackground(Color.WHITE);
        nodeValueExcludeLabel.setFont(MyVars.tahomaPlainFont11);

        JLabel nodeLabelExcludeLabel = new JLabel("N. L.");
        nodeLabelExcludeLabel.setFont(MyVars.tahomaPlainFont11);
        nodeLabelExcludeLabel.setToolTipText("SELECT A NODE LABEL TO EXCLUDE");

        this.nodeValueExcludeSymbolComboBoxMenu = new JComboBox();
        this.nodeValueExcludeSymbolComboBoxMenu.addItem("");
        this.nodeValueExcludeSymbolComboBoxMenu.addItem(">");
        this.nodeValueExcludeSymbolComboBoxMenu.addItem(">=");
        this.nodeValueExcludeSymbolComboBoxMenu.addItem("==");
        this.nodeValueExcludeSymbolComboBoxMenu.addItem("<");
        this.nodeValueExcludeSymbolComboBoxMenu.addItem("<=");
        this.nodeValueExcludeSymbolComboBoxMenu.addItem("!=");
        this.nodeValueExcludeSymbolComboBoxMenu.addItem("BTW.");
        this.nodeValueExcludeSymbolComboBoxMenu.setFocusable(false);
        this.nodeValueExcludeSymbolComboBoxMenu.setBackground(Color.WHITE);
        this.nodeValueExcludeSymbolComboBoxMenu.setFont(MyVars.tahomaPlainFont11);

        this.nodeLabelExcludeSymbolComboBoxMenu = new JComboBox();
        this.nodeLabelExcludeSymbolComboBoxMenu.addItem("");
        this.nodeLabelExcludeSymbolComboBoxMenu.addItem("==");
        this.nodeLabelExcludeSymbolComboBoxMenu.addItem("!=");
        this.nodeLabelExcludeSymbolComboBoxMenu.setFocusable(false);
        this.nodeLabelExcludeSymbolComboBoxMenu.setBackground(Color.WHITE);
        this.nodeLabelExcludeSymbolComboBoxMenu.setFont(MyVars.tahomaPlainFont11);

        this.nodeValueExcludeTxt = new JTextField();
        this.nodeValueExcludeTxt.setToolTipText("INPUT A THRESHOLD");
        this.nodeValueExcludeTxt.setHorizontalAlignment(JTextField.CENTER);
        this.nodeValueExcludeTxt.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        this.nodeValueExcludeTxt.setPreferredSize(new Dimension(60, 22));
        this.nodeValueExcludeTxt.setFont(MyVars.tahomaPlainFont11);

        JLabel edgeValueExcludeLabel = new JLabel("E. V.");
        edgeValueExcludeLabel.setToolTipText("SELECT AN EDGE VALUE TO EXCLUDE");
        edgeValueExcludeLabel.setFocusable(false);
        edgeValueExcludeLabel.setBackground(Color.WHITE);
        edgeValueExcludeLabel.setFont(MyVars.tahomaPlainFont11);

        JLabel edgeLabelExcludeLabel = new JLabel("E. L.");
        edgeLabelExcludeLabel.setFont(MyVars.tahomaPlainFont11);
        edgeLabelExcludeLabel.setToolTipText("SELECT AN EDGE LABEL TO EXCLUDE");

        this.edgeValueExcludeSymbolComboBoxMenu = new JComboBox();
        this.edgeValueExcludeSymbolComboBoxMenu.addItem("");
        this.edgeValueExcludeSymbolComboBoxMenu.addItem(">");
        this.edgeValueExcludeSymbolComboBoxMenu.addItem(">=");
        this.edgeValueExcludeSymbolComboBoxMenu.addItem("==");
        this.edgeValueExcludeSymbolComboBoxMenu.addItem("<");
        this.edgeValueExcludeSymbolComboBoxMenu.addItem("<=");
        this.edgeValueExcludeSymbolComboBoxMenu.addItem("!=");
        this.edgeValueExcludeSymbolComboBoxMenu.addItem("BTW.");
        this.edgeValueExcludeSymbolComboBoxMenu.setFocusable(false);
        this.edgeValueExcludeSymbolComboBoxMenu.setBackground(Color.WHITE);
        this.edgeValueExcludeSymbolComboBoxMenu.setFont(MyVars.tahomaPlainFont11);

        this.edgeLabelExcludeSymbolComboBoxMenu = new JComboBox();
        this.edgeLabelExcludeSymbolComboBoxMenu.addItem("");
        this.edgeLabelExcludeSymbolComboBoxMenu.addItem("==");
        this.edgeLabelExcludeSymbolComboBoxMenu.addItem("!=");
        this.edgeLabelExcludeSymbolComboBoxMenu.setFocusable(false);
        this.edgeLabelExcludeSymbolComboBoxMenu.setBackground(Color.WHITE);
        this.edgeLabelExcludeSymbolComboBoxMenu.setFont(MyVars.tahomaPlainFont11);

        this.edgeValueExcludeTxt = new JTextField();
        this.edgeValueExcludeTxt.setToolTipText("INPUT A THRESHOLD");
        this.edgeValueExcludeTxt.setHorizontalAlignment(JTextField.CENTER);
        this.edgeValueExcludeTxt.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        this.edgeValueExcludeTxt.setPreferredSize(new Dimension(60, 22));
        this.edgeValueExcludeTxt.setFont(MyVars.tahomaPlainFont11);

        this.weightedNodeColorCheckBox.setFocusable(false);
        this.weightedNodeColorCheckBox.setToolTipText("SHOW WEIGHTED NODE COLORS");
        this.weightedNodeColorCheckBox.setFont(MyVars.tahomaPlainFont11);
        this.weightedNodeColorCheckBox.setBackground(Color.WHITE);
        this.weightedNodeColorCheckBox.addActionListener(this);

        checkBoxControlPanel.setBackground(Color.WHITE);
        checkBoxControlPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3,3));

        this.percentLabel.setFont(MyVars.tahomaPlainFont11);
        this.percentLabel.setBackground(Color.WHITE);
        String nodePercent = MyMathUtil.getCommaSeperatedNumber(MyVars.directMarkovChain.getVertexCount()) + "[100%]";
        this.percentLabel.setText(nodePercent);

        checkBoxControlPanel.add(this.mouseHoverCheckBox);
        checkBoxControlPanel.add(this.weightedNodeColorCheckBox);
        checkBoxControlPanel.add(this.removeEdgeCheckBox);
        checkBoxControlPanel.add(this.nodeValueCheckBox);
        checkBoxControlPanel.add(this.edgeValueCheckBox);

        JLabel emptyLabel1 = new JLabel(" ");
        emptyLabel1.setFont(MyVars.tahomaPlainFont10);
        JLabel emptyLabel2 = new JLabel("          ");
        emptyLabel2.setFont(MyVars.tahomaPlainFont10);
        JLabel emptyLabel3 = new JLabel(" ");
        emptyLabel3.setFont(MyVars.tahomaPlainFont10);
        JLabel emptyLabel4 = new JLabel(" ");
        emptyLabel4.setFont(MyVars.tahomaPlainFont10);
        JLabel emptyLabel5 = new JLabel(" ");
        emptyLabel4.setFont(MyVars.tahomaPlainFont10);

        JPanel leftControlPanel = new JPanel();
        leftControlPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3,3));
        leftControlPanel.setBackground(Color.WHITE);

        leftControlPanel.add(emptyLabel1);
        leftControlPanel.add(nodeValueLabel);
        leftControlPanel.add(this.nodeValueComboBoxMenu);

        if (MyVars.nodeLabels.size() > 0) {
            leftControlPanel.add(nodeLabelLabel);
            leftControlPanel.add(this.nodeLabelComboBoxMenu);
        }

        leftControlPanel.add(edgeValueLabel);
        leftControlPanel.add(this.edgeValueComboBoxMenu);

        if (MyVars.edgeLabels.size() > 0) {
            leftControlPanel.add(edgeLabelLabel);
            leftControlPanel.add(this.edgeLabelComboBoxMenu);
        }

        JPanel rightControlPanel = new JPanel();
        rightControlPanel.setBackground(Color.WHITE);
        rightControlPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3,3));

        rightControlPanel.add(nodeValueExcludeLabel);
        rightControlPanel.add(this.nodeValueExcludeSymbolComboBoxMenu);
        rightControlPanel.add(this.nodeValueExcludeTxt);

        if (MyVars.nodeLabels.size() > 0) {
            rightControlPanel.add(emptyLabel3);
            rightControlPanel.add(nodeLabelExcludeLabel);
            rightControlPanel.add(this.nodeLabelExcludeSymbolComboBoxMenu);
            rightControlPanel.add(this.nodeLabelExcludeComboBoxMenu);
            rightControlPanel.add(this.nodeLabelValueExcludeComboBoxMenu);
        }

        rightControlPanel.add(emptyLabel4);
        rightControlPanel.add(edgeValueExcludeLabel);
        rightControlPanel.add(this.edgeValueExcludeSymbolComboBoxMenu);
        rightControlPanel.add(this.edgeValueExcludeTxt);

        if (MyVars.edgeLabels.size() > 0) {
            rightControlPanel.add(emptyLabel5);
            rightControlPanel.add(edgeLabelExcludeLabel);
            rightControlPanel.add(this.edgeLabelExcludeSymbolComboBoxMenu);
            rightControlPanel.add(this.edgeLabelExcludeComboBoxMenu);
            rightControlPanel.add(this.edgeLabelValueExcludeComboBoxMenu);
        }

        rightControlPanel.add(this.excludeBtn);
        this.add(leftControlPanel, BorderLayout.EAST);
        this.add(rightControlPanel, BorderLayout.WEST);
        //this.add(checkBoxControlPanel, BorderLayout.EAST);
    }

    @Override public void actionPerformed(ActionEvent ae) {
        MyVars.currentThread = new Thread(new Runnable() {
            @Override public void run() {
                if (ae.getSource() == edgeValueCheckBox) {
                    if (edgeValueComboBoxMenu.getSelectedIndex() == 0) {
                        edgeValueCheckBox.setSelected(false);
                        MyMessageUtil.showInfoMsg(MyVars.main, "Select an edge value, first.");
                        return;
                    }
                    if (edgeValueCheckBox.isSelected()) {
                        edgeValueCheckBox.setToolTipText("HIDE EDGE VALUES");
                        MyVars.getDirectGraphViewer().getRenderContext().setEdgeFontTransformer(new Transformer<MyDirectEdge, Font>() {
                            @Override
                            public Font transform(MyDirectEdge myDirectEdge) {
                                return new Font("Noto Sans", Font.PLAIN, 20);
                            }
                        });
                        MyVars.getDirectGraphViewer().getRenderContext().setEdgeLabelTransformer(new Transformer<MyDirectEdge, String>() {
                            @Override public String transform(MyDirectEdge e) {
                                String value = MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(e.getCurrentValue()));
                                value = value.endsWith(".00") ? value.substring(0, value.indexOf(".00")) : value;
                                return value;
                            }
                        });
                    } else {
                        MyVars.getDirectGraphViewer().getRenderContext().setEdgeFontTransformer(new Transformer<MyDirectEdge, Font>() {
                            @Override
                            public Font transform(MyDirectEdge myDirectEdge) {
                                return new Font("Noto Sans", Font.PLAIN, 0);
                            }
                        });
                        edgeValueCheckBox.setToolTipText("SHOW EDGE VALUES");
                    }
                    MyVars.getDirectGraphViewer().revalidate();
                    MyVars.getDirectGraphViewer().repaint();
                } else if (ae.getSource() == excludeBtn) {
                    boolean allPassed = true;
                    String edgeStrValue = edgeValueExcludeTxt.getText().trim();
                    if (!edgeStrValue.contains(",") && edgeStrValue.matches("\\d")) {
                        double numValue = Double.valueOf(edgeStrValue);
                        Collection<MyDirectNode> nodes = MyVars.directMarkovChain.getEdges();
                        if (edgeValueComboBoxMenu.getSelectedIndex() == 0) {
                            MyMessageUtil.showInfoMsg("Select an edge value, first.");
                            return;
                        }
                        Collection<MyDirectEdge> edges = MyVars.directMarkovChain.getEdges();
                        if (edgeValueExcludeSymbolComboBoxMenu.getSelectedIndex() == 0) {
                            MyProgressBar pb = new MyProgressBar(false);
                            int pbCnt = 0;
                            for (MyDirectEdge e : edges) {
                                pb.updateValue(++pbCnt, edges.size());
                                if (e.getCurrentValue() > numValue && e.getCurrentValue() > 0) {
                                    e.setOriginalNumericValue(e.getCurrentValue());
                                    e.setCurrentValue(0.0f);
                                }
                            }
                            pb.updateValue(100, 100);
                            pb.dispose();
                        } else if (edgeValueExcludeSymbolComboBoxMenu.getSelectedIndex() == 1) {
                            MyProgressBar pb = new MyProgressBar(false);
                            for (MyDirectEdge e : edges) {
                                if (e.getCurrentValue() >= numValue && e.getCurrentValue() > 0) {
                                    e.setOriginalNumericValue(e.getCurrentValue());
                                    e.setCurrentValue(0.0f);
                                }
                            }
                            pb.updateValue(100, 100);
                            pb.dispose();
                        } else if (edgeValueExcludeSymbolComboBoxMenu.getSelectedIndex() == 2) {
                            MyProgressBar pb = new MyProgressBar(false);
                            int pbCnt = 0;
                            for (MyDirectEdge e : edges) {
                                pb.updateValue(++pbCnt, edges.size());
                                if (e.getCurrentValue() == numValue && e.getCurrentValue() > 0) {
                                    e.setOriginalNumericValue(e.getCurrentValue());
                                    e.setCurrentValue(0.0f);
                                }
                            }
                            pb.updateValue(100, 100);
                            pb.dispose();
                        } else if (edgeValueExcludeSymbolComboBoxMenu.getSelectedIndex() == 3) {
                            MyProgressBar pb = new MyProgressBar(false);
                            int pbCnt = 0;
                            for (MyDirectEdge e : edges) {
                                pb.updateValue(++pbCnt, edges.size());
                                if (e.getCurrentValue() < numValue && e.getCurrentValue() > 0) {
                                    e.setOriginalNumericValue(e.getCurrentValue());
                                    e.setCurrentValue(0.0f);
                                }
                            }
                            pb.updateValue(100, 100);
                            pb.dispose();
                        } else if (edgeValueExcludeSymbolComboBoxMenu.getSelectedIndex() == 4) {
                            MyProgressBar pb = new MyProgressBar(false);
                            int pbCnt = 0;
                            for (MyDirectEdge e : edges) {
                                pb.updateValue(++pbCnt, edges.size());
                                if (e.getCurrentValue() <= numValue && e.getCurrentValue() > 0) {
                                    e.setOriginalNumericValue(e.getCurrentValue());
                                    e.setCurrentValue(0.0f);
                                }
                            }
                            pb.updateValue(100, 100);
                            pb.dispose();
                        } else if (edgeValueExcludeSymbolComboBoxMenu.getSelectedIndex() == 5) {
                            MyProgressBar pb = new MyProgressBar(false);
                            int pbCnt = 0;
                            for (MyDirectEdge e : edges) {
                                pb.updateValue(++pbCnt, edges.size());
                                if (e.getCurrentValue() != numValue && e.getCurrentValue() > 0) {
                                    e.setOriginalNumericValue(e.getCurrentValue());
                                    e.setCurrentValue(0.0f);
                                }
                            }
                            pb.updateValue(100, 100);
                            pb.dispose();
                        }
                    } else if (edgeValueExcludeSymbolComboBoxMenu.getSelectedIndex() == 6 && edgeStrValue.contains(",")) {
                        String strValue1 = "";
                        String strValue2 = "";
                        strValue1 = edgeStrValue.split(",")[0];
                        strValue2 = edgeStrValue.split(",")[1];
                        if (strValue1.matches("\\d+") && strValue2.matches("\\d+")) {
                            double numValue1 = Double.valueOf(strValue1);
                            double numValue2 = Double.valueOf(strValue2);
                            MyProgressBar pb = new MyProgressBar(false);
                            Collection<MyDirectEdge> edges = MyVars.directMarkovChain.getEdges();
                            int pbCnt = 0;
                            for (MyDirectEdge e : edges) {
                                pb.updateValue(++pbCnt, edges.size());
                                if ((e.getCurrentValue() < numValue1 || e.getCurrentValue() > numValue2) && e.getCurrentValue() > 0) {
                                    e.setOriginalNumericValue(e.getCurrentValue());
                                    e.setCurrentValue(0.0f);
                                }
                            }
                            pb.updateValue(100, 100);
                            pb.dispose();
                        }
                    } else if (edgeValueExcludeSymbolComboBoxMenu.getSelectedIndex() == 6 && !edgeStrValue.contains(",")) {
                        MyMessageUtil.showInfoMsg("Provide two numbers and a comma between the numbers.");
                        return;
                    }

                    String nodeStrValue = nodeValueExcludeTxt.getText().trim();
                    if (!nodeStrValue.contains(",") && nodeStrValue.matches("\\d+")) {
                        double numValue = Double.valueOf(nodeStrValue);
                        Collection<MyDirectNode> nodes = MyVars.directMarkovChain.getVertices();
                        if (nodeValueExcludeSymbolComboBoxMenu.getSelectedIndex() == 0) {
                            MyProgressBar pb = new MyProgressBar(false);
                            int count = 0;
                            for (MyDirectNode n : nodes) {
                                pb.updateValue(++count, nodes.size());
                                if (n.getCurrentValue() == 0) continue;
                                if (n.getCurrentValue() > numValue) {
                                    n.setOriginalValue(n.getCurrentValue());
                                    n.setCurrentValue(0);
                                }
                            }
                            pb.updateValue(100, 100);
                            pb.dispose();
                        } else if (nodeValueExcludeSymbolComboBoxMenu.getSelectedIndex() == 1) {
                            MyProgressBar pb = new MyProgressBar(false);
                            int count = 0;
                            for (MyDirectNode n : nodes) {
                                pb.updateValue(++count, nodes.size());
                                if (n.getCurrentValue() == 0) continue;
                                if (n.getCurrentValue() >= numValue) {
                                    n.setOriginalValue(n.getCurrentValue());
                                    n.setCurrentValue(0);
                                }
                            }
                            pb.updateValue(100, 100);
                            pb.dispose();
                        } else if (nodeValueExcludeSymbolComboBoxMenu.getSelectedIndex() == 2) {
                            MyProgressBar pb = new MyProgressBar(false);
                            int count = 0;
                            for (MyDirectNode n : nodes) {
                                pb.updateValue(++count, nodes.size());
                                if (n.getCurrentValue() == 0) continue;
                                if (n.getCurrentValue() == numValue) {
                                    n.setOriginalValue(n.getCurrentValue());
                                    n.setCurrentValue(0);
                                }
                            }
                            pb.updateValue(100, 100);
                            pb.dispose();
                        } else if (nodeValueExcludeSymbolComboBoxMenu.getSelectedIndex() == 3) {
                            MyProgressBar pb = new MyProgressBar(false);
                            int count = 0;
                            for (MyDirectNode n : nodes) {
                                pb.updateValue(++count, nodes.size());
                                if (n.getCurrentValue() == 0) continue;
                                if (n.getCurrentValue() < numValue) {
                                    n.setOriginalValue(n.getCurrentValue());
                                    n.setCurrentValue(0);
                                }
                            }
                            pb.updateValue(100, 100);
                            pb.dispose();
                        } else if (nodeValueExcludeSymbolComboBoxMenu.getSelectedIndex() == 4) {
                            MyProgressBar pb = new MyProgressBar(false);
                            int count = 0;
                            for (MyDirectNode n : nodes) {
                                pb.updateValue(++count, nodes.size());
                                if (n.getCurrentValue() == 0) continue;
                                if (n.getCurrentValue() <= numValue) {
                                    n.setOriginalValue(n.getCurrentValue());
                                    n.setCurrentValue(0);
                                }
                            }
                            pb.updateValue(100, 100);
                            pb.dispose();
                        } else if (nodeValueExcludeSymbolComboBoxMenu.getSelectedIndex() == 5) {
                            MyProgressBar pb = new MyProgressBar(false);
                            int count = 0;
                            for (MyDirectNode n : nodes) {
                                pb.updateValue(++count, nodes.size());
                                if (n.getCurrentValue() == 0) continue;
                                if (n.getCurrentValue() != numValue) {
                                    n.setOriginalValue(n.getCurrentValue());
                                    n.setCurrentValue(0);
                                }
                            }
                            pb.updateValue(100, 100);
                            pb.dispose();
                        }
                    } else if (nodeValueExcludeSymbolComboBoxMenu.getSelectedIndex() == 6 && nodeStrValue.contains(",")) {
                        String strValue1 = "";
                        String strValue2 = "";
                        strValue1 = nodeStrValue.split(",")[0];
                        strValue2 = nodeStrValue.split(",")[1];
                        if (strValue1.matches("\\d+") && strValue2.matches("\\d+")) {
                            double numValue1 = Double.valueOf(strValue1);
                            double numValue2 = Double.valueOf(strValue2);
                            MyProgressBar pb = new MyProgressBar(false);
                            if (nodeValueExcludeComboBoxMenu.getSelectedIndex() == 1) {
                                Collection<MyDirectEdge> edges = MyVars.directMarkovChain.getEdges();
                                for (MyDirectEdge e : edges) {
                                    if ((e.getCurrentValue() < numValue1 || e.getCurrentValue() > numValue2) && e.getCurrentValue() > 0) {
                                            e.setOriginalNumericValue(e.getCurrentValue());
                                            e.setCurrentValue(0.0f);
                                    }
                                }
                                pb.updateValue(30, 100);
                                Set<MyDirectNode> nonZeroNodes = new HashSet<>();
                                for (MyDirectEdge e : edges) {
                                    if (e.getCurrentValue() > 0f) {
                                            nonZeroNodes.add(e.getSource());
                                            nonZeroNodes.add(e.getDest());
                                    }
                                }
                                pb.updateValue(60, 100);
                                Collection<MyDirectNode> ns = MyVars.directMarkovChain.getVertices();
                                for (MyDirectNode n : ns) {
                                    if (!nonZeroNodes.contains(n)) {
                                            n.setCurrentValue(0f);
                                    }
                                }
                                pb.updateValue(100, 100);
                                pb.dispose();
                            } else {
                                Collection<MyDirectNode> nodes = MyVars.directMarkovChain.getVertices();
                                for (MyDirectNode n : nodes) {
                                    if (n.getCurrentValue() == 0) continue;
                                    if (n.getCurrentValue() < numValue1 || n.getCurrentValue() > numValue2) {
                                        n.setOriginalValue(n.getCurrentValue());
                                        n.setCurrentValue(0);
                                    }
                                }
                            }
                        }
                    } else if (nodeValueExcludeSymbolComboBoxMenu.getSelectedIndex() == 6 && !nodeStrValue.contains(",")) {
                        MyMessageUtil.showInfoMsg("Provide two numbers and a comma between the numbers.");
                        return;
                    }

                    if (nodeLabelExcludeComboBoxMenu.getSelectedIndex() > 0) {
                        Collection<MyDirectNode> nodes = MyVars.directMarkovChain.getVertices();
                        if (nodeLabelExcludeSymbolComboBoxMenu.getSelectedIndex() == 1) {
                            for (MyDirectNode n : nodes) {
                                if (n.getCurrentValue() == 0) continue;
                                if (n.nodeLabels.get(nodeLabelValueExcludeComboBoxMenu.getSelectedItem().toString()).equals(nodeLabelValueExcludeComboBoxMenu.getSelectedItem().toString())) {
                                   n.setOriginalValue(n.getCurrentValue());
                                    n.setCurrentValue(0);
                                }
                            }
                        } else {
                            for (MyDirectNode n : nodes) {
                                if (n.getCurrentValue() == 0) continue;
                                if (!n.nodeLabels.get(nodeLabelValueExcludeComboBoxMenu.getSelectedItem().toString()).equals(nodeLabelValueExcludeComboBoxMenu.getSelectedItem().toString())) {
                                    n.setOriginalValue(n.getCurrentValue());
                                    n.setCurrentValue(0);
                                }
                            }
                        }
                    }

                    if (edgeLabelExcludeComboBoxMenu.getSelectedIndex() > 0) {
                        Collection<MyDirectEdge> edges = MyVars.directMarkovChain.getEdges();
                        if (edgeLabelExcludeSymbolComboBoxMenu.getSelectedIndex() == 1) {
                            for (MyDirectEdge e : edges) {
                                if (e.getCurrentValue() == 0) continue;
                                if (e.edgeLabels.get(edgeLabelValueExcludeComboBoxMenu.getSelectedItem().toString()).equals(edgeLabelValueExcludeComboBoxMenu.getSelectedItem().toString())) {
                                    e.setOriginalNumericValue(e.getCurrentValue());
                                    e.setCurrentValue(0);
                                }
                            }
                        } else {
                            for (MyDirectEdge e : edges) {
                                if (e.getCurrentValue() == 0) continue;
                                if (!e.edgeLabels.get(edgeLabelValueExcludeComboBoxMenu.getSelectedItem().toString()).equals(edgeLabelValueExcludeComboBoxMenu.getSelectedItem().toString())) {
                                    e.setOriginalNumericValue(e.getCurrentValue());
                                    e.setCurrentValue(0);
                                }
                            }
                        }
                    }

                    if (allPassed) {
                        MyVars.main.getDirectMarkovChainDashBoard().directGraphTextStatisticsPanel.setTextStatistics();
                        MyVars.getDirectGraphViewer().revalidate();
                        MyVars.getDirectGraphViewer().repaint();
                        MyMessageUtil.showInfoMsg(MyMathUtil.getCommaSeperatedNumber(MyVars.directMarkovChain.getNonZeroEdges()) + "[" + MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat((MyVars.directMarkovChain.getNonZeroEdges() / MyVars.directMarkovChain.getEdgeCount()) * 100)) + "%] edges and " +
                                MyMathUtil.getCommaSeperatedNumber(MyVars.directMarkovChain.getNonZeroNodes()) + "[" + MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat((MyVars.directMarkovChain.getNonZeroNodes() / MyVars.directMarkovChain.getVertexCount()) * 100)) + "%] nodes are remaining.");
                    }
                } else if (ae.getSource() == edgeValueComboBoxMenu) {
                    if (edgeValueComboBoxMenu.getSelectedIndex() == 0) {
                        MyProgressBar pb = new MyProgressBar(false);
                        try {
                            Collection<MyDirectEdge> edges = MyVars.directMarkovChain.getEdges();
                            int pbCnt = 0;
                            for (MyDirectEdge e : edges) {
                                pb.updateValue(++pbCnt, edges.size());
                                e.setCurrentValue(-1);
                            }
                            MyVars.getDirectGraphViewer().getRenderContext().setEdgeFontTransformer(new Transformer<MyDirectEdge, Font>() {
                                @Override public Font transform(MyDirectEdge e) {
                                    return new Font("Noto Sans", Font.PLAIN, 0);
                                }
                            });
                            pb.updateValue(100, 100);
                            pb.dispose();
                        } catch (Exception ex) {
                            pb.updateValue(100, 100);
                            pb.dispose();
                        }
                    } else if (edgeValueComboBoxMenu.getSelectedIndex() == 1) {
                        MyProgressBar pb = new MyProgressBar(false);
                        try {
                            float max = 0f;
                            Collection<MyDirectEdge> edges = MyVars.directMarkovChain.getEdges();
                            int pbCnt = 0;
                            for (MyDirectEdge e : edges) {
                                pb.updateValue(++pbCnt, edges.size());
                                if (e.getCurrentValue() == 0) continue;
                                e.setCurrentValue(e.getContribution());
                                if (max < e.getCurrentValue()) {
                                    max = e.getCurrentValue();
                                }
                            }
                            MyVars.directMarkovChain.maxEdgeValue = max;
                            pb.updateValue(100, 100);
                            pb.dispose();
                        } catch (Exception ex) {
                            pb.updateValue(100, 100);
                            pb.dispose();
                        }
                    } else if (edgeValueComboBoxMenu.getSelectedIndex() == 2) {
                        try {
                            float max = 0f;
                            MyEdgeBetweennessComputer edgeBetweennessComputer = new MyEdgeBetweennessComputer();
                            edgeBetweennessComputer.compute();
                            Collection<MyDirectEdge> edges = MyVars.directMarkovChain.getEdges();
                            for (MyDirectEdge e : edges) {
                                if (e.getCurrentValue() == 0) continue;
                                if (e.betweeness > max) {
                                    max = e.betweeness;
                                }
                            }
                            MyVars.directMarkovChain.maxEdgeValue = max;
                            for (MyDirectEdge e : edges) {
                                if (e.getCurrentValue() == 0) continue;
                                e.setCurrentValue(e.betweeness);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    MyVars.getDirectGraphViewer().getRenderContext().setEdgeStrokeTransformer(MyVars.getDirectGraphViewer().weightedEdgeStroker);
                    MyVars.main.getDirectMarkovChainDashBoard().updateEdgeValueRelatedComponents();
                    MyVars.main.getDirectMarkovChainDashBoard().updateEdgeTable();
                    MyVars.main.getDirectMarkovChainDashBoard().updateStatTable();
                    if (MyVars.getDirectGraphViewer().selectedSingleNode != null) {
                        MyVars.main.getDirectMarkovChainDashBoard().nodeLevelTabbedPane.setSelectedIndex(1);
                    } else if (MyVars.getDirectGraphViewer().selectedSingleNode != null) {
                        MyVars.main.getDirectMarkovChainDashBoard().multiNodeLevelTabbedPane.setSelectedIndex(1);
                    } else {
                        MyVars.main.getDirectMarkovChainDashBoard().topLevelTabbedPane.setSelectedIndex(1);
                    }
                    MyVars.main.getDirectMarkovChainDashBoard().directGraphTextStatisticsPanel.setTextStatistics();
                    MyVars.getDirectGraphViewer().revalidate();
                    MyVars.getDirectGraphViewer().repaint();
                } else if (ae.getSource() == edgeLabelComboBoxMenu) {
                    Collection<MyDirectEdge> edges = MyVars.directMarkovChain.getEdges();
                    for (MyDirectEdge e : edges) {
                        if (e.edgeLabels.get(edgeLabelComboBoxMenu.getSelectedItem()) != null) {
                            MyVars.getDirectGraphViewer().getRenderContext().setEdgeFontTransformer(new Transformer<MyDirectEdge, Font>() {
                                @Override public Font transform(MyDirectEdge e) {
                                    if (MyVars.getDirectGraphViewer().selectedSingleNode != null) {
                                        if (e.getSource().getCurrentValue() == 0 || e.getDest().getCurrentValue() == 0) {
                                            return new Font("Noto Sans", Font.PLAIN, 0);
                                        } else if (MyVars.getDirectGraphViewer().selectedSingleNode == e.getSource() ||
                                                MyVars.getDirectGraphViewer().selectedSingleNode == e.getDest()) {
                                            return new Font("Noto Sans", Font.PLAIN, 60);
                                        } else {
                                            return new Font("Noto Sans", Font.PLAIN, 0);
                                        }
                                    } else {
                                        return new Font("Noto Sans", Font.PLAIN, 60);
                                    }
                                }
                            });

                            MyVars.getDirectGraphViewer().getRenderContext().setEdgeLabelTransformer(new Transformer<MyDirectEdge, String>() {
                                @Override public String transform(MyDirectEdge e) {
                                    return "";
                                }
                            });
                        }
                    }
                } else if (ae.getSource() == nodeLabelCheckBox) {
                    if (nodeLabelCheckBox.isSelected()) {
                        nodeLabelCheckBox.setToolTipText("HIDE NODE LABELS");
                        MyVars.getDirectGraphViewer().getRenderContext().setVertexFontTransformer(new Transformer<MyDirectNode, Font>() {
                            @Override public Font transform(MyDirectNode n) {
                                if (MyVars.getDirectGraphViewer().selectedSingleNode != null) {
                                    if (MyVars.getDirectGraphViewer().selectedSingleNode == n ||
                                            MyVars.directMarkovChain.getPredecessors(MyVars.getDirectGraphViewer().selectedSingleNode).contains(n) ||
                                            MyVars.directMarkovChain.getSuccessors(MyVars.getDirectGraphViewer().selectedSingleNode).contains(n)) {
                                        return new Font("Noto Sans", Font.PLAIN, 70);
                                    } else {
                                        return new Font("Noto Sans", Font.PLAIN, 0);
                                    }
                                } else {
                                    return new Font("Noto Sans", Font.PLAIN, 70);
                                }
                            }
                        });

                        MyVars.getDirectGraphViewer().getRenderContext().setVertexLabelTransformer(new Transformer<MyDirectNode, String>() {
                            @Override public String transform(MyDirectNode n) {
                                if (MyVars.getDirectGraphViewer().selectedSingleNode != null) {
                                    if (MyVars.getDirectGraphViewer().selectedSingleNode == n ||
                                            MyVars.directMarkovChain.getPredecessors(MyVars.getDirectGraphViewer().selectedSingleNode).contains(n) ||
                                            MyVars.directMarkovChain.getSuccessors(MyVars.getDirectGraphViewer().selectedSingleNode).contains(n)) {
                                        if (nodeValueCheckBox.isSelected()) {
                                            String value = MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(n.getCurrentValue()));
                                            value = value.endsWith(".00") ? value.substring(0, value.indexOf(".00")) : value;
                                            return n.getLabel() + "[" + value + "]";
                                        } else {
                                            return n.getLabel();
                                        }
                                    } else {
                                        if (nodeValueCheckBox.isSelected()) {
                                            String value = MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(n.getCurrentValue()));
                                            value = value.endsWith(".00") ? value.substring(0, value.indexOf(".00")) : value;
                                            return n.getLabel() + "[" + value + "]";
                                        } else {
                                            return n.getLabel();
                                        }
                                    }
                                } else {
                                    if (nodeValueCheckBox.isSelected()) {
                                        String value = MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(n.getCurrentValue()));
                                        value = value.endsWith(".00") ? value.substring(0, value.indexOf(".00")) : value;
                                        return n.getLabel() + "[" + value + "]";
                                    } else {
                                        return n.getLabel();
                                    }
                                }
                            }
                        });
                    } else {
                        if (nodeValueCheckBox.isSelected()) {
                            MyVars.getDirectGraphViewer().getRenderContext().setVertexLabelTransformer(new Transformer<MyDirectNode, String>() {
                                @Override public String transform(MyDirectNode n) {
                                    n.setLabel(n.getName());
                                    nodeLabelComboBoxMenu.setSelectedIndex(0);
                                    String value = MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(n.getCurrentValue()));
                                    value = value.endsWith(".00") ? value.substring(0, value.indexOf(".00")) : value;
                                    return value;
                                }
                            });
                        } else {
                            MyVars.getDirectGraphViewer().getRenderContext().setVertexLabelTransformer(new Transformer<MyDirectNode, String>() {
                                @Override public String transform(MyDirectNode n) {
                                    return "";
                                }
                            });
                        }
                        nodeLabelCheckBox.setToolTipText("SHOW NODE LABELS");
                    }

                    MyVars.getDirectGraphViewer().revalidate();
                    MyVars.getDirectGraphViewer().repaint();
                } else if (ae.getSource() == nodeValueCheckBox) {
                    if (nodeValueCheckBox.isSelected()) {
                        nodeValueCheckBox.setToolTipText("HIDE NODE VALUES");
                        MyVars.getDirectGraphViewer().getRenderContext().setVertexFontTransformer(new Transformer<MyDirectNode, Font>() {
                            @Override public Font transform(MyDirectNode n) {
                                if (MyVars.getDirectGraphViewer().selectedSingleNode != null) {
                                    if (n.getCurrentValue() == 0) {
                                        return new Font("Noto Sans", Font.PLAIN, 0);
                                    } else if (MyVars.getDirectGraphViewer().selectedSingleNode == n ||
                                            MyVars.directMarkovChain.getPredecessors(MyVars.getDirectGraphViewer().selectedSingleNode).contains(n) ||
                                            MyVars.directMarkovChain.getSuccessors(MyVars.getDirectGraphViewer().selectedSingleNode).contains(n)) {
                                        return new Font("Noto Sans", Font.PLAIN, 70);
                                    } else {
                                        return new Font("Noto Sans", Font.PLAIN, 0);
                                    }
                                } else if (n.getCurrentValue() == 0) {
                                    return new Font("Noto Sans", Font.PLAIN, 0);
                                } else {
                                    return new Font("Noto Sans", Font.PLAIN, 70);
                                }
                            }
                        });
                        MyVars.getDirectGraphViewer().getRenderContext().setVertexLabelTransformer(new Transformer<MyDirectNode, String>() {
                            @Override public String transform(MyDirectNode n) {
                                String value = MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(n.getCurrentValue()));
                                value = value.endsWith(".00") ? value.substring(0, value.indexOf(".00")) : value;
                                if (MyVars.getDirectGraphViewer().selectedSingleNode != null) {
                                    if (MyVars.getDirectGraphViewer().selectedSingleNode == n ||
                                            MyVars.directMarkovChain.getPredecessors(MyVars.getDirectGraphViewer().selectedSingleNode).contains(n) ||
                                            MyVars.directMarkovChain.getSuccessors(MyVars.getDirectGraphViewer().selectedSingleNode).contains(n)) {
                                        if (nodeLabelCheckBox.isSelected()) {
                                            return n.getLabel() + "[" + value + "]";
                                        } else {
                                            return value;
                                        }
                                    } else {
                                        if (nodeLabelCheckBox.isSelected()) {
                                            return n.getLabel() + "[" + value + "]";
                                        } else {
                                            return value;
                                        }
                                    }
                                } else {
                                    if (nodeLabelCheckBox.isSelected()) {
                                        return n.getLabel() + "[" + value + "]";
                                    } else {
                                        return value;
                                    }
                                }
                            }
                        });
                    } else {
                        if (nodeLabelCheckBox.isSelected()) {
                            nodeValueCheckBox.setToolTipText("SHOW NODE VALUES");
                            MyVars.getDirectGraphViewer().getRenderContext().setVertexLabelTransformer(new Transformer<MyDirectNode, String>() {
                                @Override public String transform(MyDirectNode n) {
                                    return n.getLabel();
                                }
                            });
                        } else {
                            MyVars.getDirectGraphViewer().getRenderContext().setVertexLabelTransformer(new Transformer<MyDirectNode, String>() {
                                @Override public String transform(MyDirectNode n) {
                                    return "";
                                }
                            });
                        }
                    }
                    MyVars.getDirectGraphViewer().revalidate();
                    MyVars.getDirectGraphViewer().repaint();
                } else if (ae.getSource() == removeEdgeCheckBox) {
                    if (removeEdgeCheckBox.isSelected()) {
                        removeEdgeCheckBox.setToolTipText("SHOW EDGES");
                        MyVars.getDirectGraphViewer().getRenderContext().setEdgeDrawPaintTransformer(new Transformer<MyDirectEdge, Paint>() {
                            @Override public Paint transform(MyDirectEdge e) {
                                return new Color(0.0f, 0.0f, 0.0f, 0.0f);
                            }
                        });

                        MyVars.getDirectGraphViewer().getRenderContext().setArrowDrawPaintTransformer(new Transformer<MyDirectEdge, Paint>() {
                            @Override
                            public Paint transform(MyDirectEdge e) {
                               return new Color(0, 0, 0, 0f);
                            }
                        });
                        MyVars.getDirectGraphViewer().getRenderContext().setArrowFillPaintTransformer(new Transformer<MyDirectEdge, Paint>() {
                            @Override
                            public Paint transform(MyDirectEdge e) {
                                return new Color(0, 0, 0, 0f);
                            }
                        });
                    } else {
                        MyVars.getDirectGraphViewer().getRenderContext().setArrowDrawPaintTransformer(new Transformer<MyDirectEdge, Paint>() {
                            @Override
                            public Paint transform(MyDirectEdge e) {
                                if (MyVars.getDirectGraphViewer().selectedSingleNode != null &&
                                        (e.getSource() != MyVars.getDirectGraphViewer().selectedSingleNode || e.getDest() != MyVars.getDirectGraphViewer().selectedSingleNode)) {
                                    return new Color(0, 0, 0, 0f);
                                } else {
                                    return Color.GRAY;
                                }
                            }
                        });
                        MyVars.getDirectGraphViewer().getRenderContext().setArrowFillPaintTransformer(new Transformer<MyDirectEdge, Paint>() {
                            @Override
                            public Paint transform(MyDirectEdge e) {
                                if (MyVars.getDirectGraphViewer().selectedSingleNode != null &&
                                        (e.getSource() != MyVars.getDirectGraphViewer().selectedSingleNode || e.getDest() != MyVars.getDirectGraphViewer().selectedSingleNode)) {
                                    return new Color(0, 0, 0, 0f);
                                } else {
                                    return Color.GRAY;
                                }
                            }
                        });
                        removeEdgeCheckBox.setToolTipText("REMOVE EDGES");
                        MyVars.getDirectGraphViewer().setUnWeightedEdgeColor();
                    }
                    MyVars.getDirectGraphViewer().revalidate();
                    MyVars.getDirectGraphViewer().repaint();
                } else if (ae.getSource() == nodeLabelComboBoxMenu) {
                    if (nodeLabelComboBoxMenu.getSelectedIndex() > 0) {
                        Collection<MyDirectNode> nodes = MyVars.directMarkovChain.getVertices();
                        for (MyDirectNode n : nodes) {
                            if (n.nodeLabels.get(nodeLabelComboBoxMenu.getSelectedItem()) != null) {
                                MyVars.getDirectGraphViewer().getRenderContext().setVertexFontTransformer(new Transformer<MyDirectNode, Font>() {
                                    @Override public Font transform(MyDirectNode n) {
                                        if (MyVars.getDirectGraphViewer().selectedSingleNode != null) {
                                            if (n.getCurrentValue() == 0) {
                                                return new Font("Noto Sans", Font.PLAIN, 0);
                                            } else if (MyVars.getDirectGraphViewer().selectedSingleNode == n) {
                                                return new Font("Noto Sans", Font.PLAIN, 60);
                                            } else {
                                                return new Font("Noto Sans", Font.PLAIN, 0);
                                            }
                                        } else {
                                            return new Font("Noto Sans", Font.PLAIN, 60);
                                        }
                                    }
                                });
                                MyVars.getDirectGraphViewer().getRenderContext().setVertexLabelTransformer(new Transformer<MyDirectNode, String>() {
                                    @Override public String transform(MyDirectNode n) {
                                        return "";
                                    }
                                });
                                n.setLabel(n.nodeLabels.get(nodeLabelComboBoxMenu.getSelectedItem().toString()));
                            }
                        }
                    } else {
                        Collection<MyDirectNode> nodes = MyVars.directMarkovChain.getVertices();
                        for (MyDirectNode n : nodes) {
                            if (n.nodeLabels.get(nodeValueComboBoxMenu.getSelectedItem()) != null) {
                                MyVars.getDirectGraphViewer().getRenderContext().setVertexFontTransformer(new Transformer<MyDirectNode, Font>() {
                                    @Override public Font transform(MyDirectNode n) {
                                        if (MyVars.getDirectGraphViewer().selectedSingleNode != null) {
                                            if (n.getCurrentValue() == 0) {
                                                return new Font("Noto Sans", Font.PLAIN, 0);
                                            } else if (MyVars.getDirectGraphViewer().selectedSingleNode == n) {
                                                return new Font("Noto Sans", Font.PLAIN, 60);
                                            } else {
                                                return new Font("Noto Sans", Font.PLAIN, 0);
                                            }
                                        } else {
                                            return new Font("Noto Sans", Font.PLAIN, 60);
                                        }
                                    }
                                });
                                MyVars.getDirectGraphViewer().getRenderContext().setVertexLabelTransformer(new Transformer<MyDirectNode, String>() {
                                    @Override public String transform(MyDirectNode n) {
                                        return "";
                                    }
                                });
                                n.setLabel(n.getName());
                            }
                        }
                    }
                } else if (weightedNodeColorCheckBox == ae.getSource()) {
                    if (weightedNodeColorCheckBox.isSelected()) {
                        weightedNodeColorCheckBox.setToolTipText("SET UNWEIGHTED NODE COLORS");
                        MyVars.getDirectGraphViewer().getRenderContext().setVertexFillPaintTransformer(MyVars.getDirectGraphViewer().weightedNodeColor);
                    } else {
                        weightedNodeColorCheckBox.setToolTipText("SET WEIGHTED NODE COLORS");
                        MyVars.getDirectGraphViewer().getRenderContext().setVertexFillPaintTransformer(MyVars.getDirectGraphViewer().unWeightedNodeColor);
                    }
                    MyVars.getDirectGraphViewer().revalidate();
                    MyVars.getDirectGraphViewer().repaint();
                } else if (edgeLabelCheckBox == ae.getSource()) {
                    if (edgeLabelCheckBox.isSelected()) {
                        edgeLabelCheckBox.setToolTipText("HIDE EDGE LABELS");
                        if (edgeLabelComboBoxMenu.getSelectedIndex() == 0) {
                            MyMessageUtil.showInfoMsg("Select an edge label, first.");
                            edgeLabelCheckBox.setSelected(false);
                            return;
                        }
                        MyVars.getDirectGraphViewer().getRenderContext().setEdgeFontTransformer(new Transformer<MyDirectEdge, Font>() {
                            @Override public Font transform(MyDirectEdge e) {
                                if (MyVars.getDirectGraphViewer().selectedSingleNode != null) {
                                    if (e.getSource().getCurrentValue() == 0 || e.getDest().getCurrentValue() == 0) {
                                        return new Font("Noto Sans", Font.PLAIN, 0);
                                    } else if (MyVars.getDirectGraphViewer().selectedSingleNode == e.getSource() ||
                                            MyVars.getDirectGraphViewer().selectedSingleNode == e.getDest()) {
                                        return new Font("Noto Sans", Font.PLAIN, 60);
                                    } else {
                                        return new Font("Noto Sans", Font.PLAIN, 0);
                                    }
                                } else {
                                    return new Font("Noto Sans", Font.PLAIN, 60);
                                }
                            }
                        });
                        MyVars.getDirectGraphViewer().getRenderContext().setEdgeLabelTransformer(new Transformer<MyDirectEdge, String>() {
                            @Override public String transform(MyDirectEdge e) {
                                return e.edgeLabels.get(edgeLabelComboBoxMenu.getSelectedItem());
                            }
                        });
                    } else {
                        if (edgeValueCheckBox.isSelected()) {
                            MyVars.getDirectGraphViewer().getRenderContext().setEdgeLabelTransformer(new Transformer<MyDirectEdge, String>() {
                                @Override public String transform(MyDirectEdge e) {
                                    return e.getCurrentValue() + "";
                                }
                            });
                        } else {
                            MyVars.getDirectGraphViewer().getRenderContext().setEdgeLabelTransformer(new Transformer<MyDirectEdge, String>() {
                                @Override public String transform(MyDirectEdge e) {
                                    return "";
                                }
                            });
                        }
                        edgeLabelCheckBox.setToolTipText("SHOW EDGE VALUES");
                        edgeLabelComboBoxMenu.setSelectedIndex(0);
                    }
                    MyVars.getDirectGraphViewer().revalidate();
                    MyVars.getDirectGraphViewer().repaint();
                } else if (ae.getSource() == nodeValueComboBoxMenu) {
                    MyProgressBar pb = new MyProgressBar(false);
                    int pbCnt = 0;
                    float maxValue = 0.00f;
                    Collection<MyDirectNode> nodes = MyVars.directMarkovChain.getVertices();
                    if (nodeValueComboBoxMenu.getSelectedIndex() == 0) {
                        for (MyDirectNode n : nodes) {
                            n.setCurrentValue((float) n.getContribution());
                            if (maxValue < n.getCurrentValue()) {
                                maxValue = n.getCurrentValue();
                            }
                            pb.updateValue(++pbCnt, nodes.size());
                        }
                    } else if (nodeValueComboBoxMenu.getSelectedIndex() == 1) {
                        for (MyDirectNode n : nodes) {
                            if (n.getCurrentValue() == 0) continue;;
                            n.setCurrentValue((float) n.getInContribution());
                            if (maxValue < n.getCurrentValue()) {
                                maxValue = n.getCurrentValue();
                            }
                            pb.updateValue(++pbCnt, nodes.size());
                        }
                    } else if (nodeValueComboBoxMenu.getSelectedIndex() == 2) {
                        for (MyDirectNode n : nodes) {
                            if (n.getCurrentValue() == 0) continue;;
                            n.setCurrentValue((float) n.getOutContribution());
                            if (maxValue < n.getCurrentValue()) {
                                maxValue = n.getCurrentValue();
                            }
                            pb.updateValue(++pbCnt, nodes.size());
                        }
                    } else if (nodeValueComboBoxMenu.getSelectedIndex() == 3) {
                        for (MyDirectNode n : nodes) {
                            if (n.getCurrentValue() == 0) continue;;
                            n.setCurrentValue((float) MyVars.directMarkovChain.getPredecessorCount(n));
                            if (maxValue < n.getCurrentValue()) {
                                maxValue = n.getCurrentValue();
                            }
                            pb.updateValue(++pbCnt, nodes.size());
                        }
                    } else if (nodeValueComboBoxMenu.getSelectedIndex() == 4) {
                        for (MyDirectNode n : nodes) {
                            if (n.getCurrentValue() == 0) continue;;
                            n.setCurrentValue((float) MyVars.directMarkovChain.getSuccessorCount(n));
                            if (maxValue < n.getCurrentValue()) {
                                maxValue = n.getCurrentValue();
                            }
                            pb.updateValue(++pbCnt, nodes.size());
                        }
                    } else if (nodeValueComboBoxMenu.getSelectedIndex() == 5) {
                        for (MyDirectNode n : nodes) {
                            if (n.getCurrentValue() == 0) continue;;
                            n.setCurrentValue((float) n.getAverageShortestPathLength());
                            if (maxValue < n.getCurrentValue()) {
                                maxValue = n.getCurrentValue();
                            }
                            pb.updateValue(++pbCnt, nodes.size());
                        }
                    } else if (nodeValueComboBoxMenu.getSelectedIndex() == 6) {
                        for (MyDirectNode n : nodes) {
                            if (n.getCurrentValue() == 0) continue;;
                            n.setCurrentValue((float) n.getBetweeness());
                            if (maxValue < n.getCurrentValue()) {
                                maxValue = n.getCurrentValue();
                            }
                            pb.updateValue(++pbCnt, nodes.size());
                        }
                    } else if (nodeValueComboBoxMenu.getSelectedIndex() == 7) {
                        for (MyDirectNode n : nodes) {
                            if (n.getCurrentValue() == 0) continue;;
                            n.setCurrentValue((float) n.getCloseness());
                            if (maxValue < n.getCurrentValue()) {
                                maxValue = n.getCurrentValue();
                            }

                            pb.updateValue(++pbCnt, nodes.size());
                        }
                    } else if (nodeValueComboBoxMenu.getSelectedIndex() == 8) {
                        for (MyDirectNode n : nodes) {
                            if (n.getCurrentValue() == 0) continue;;
                            n.setCurrentValue((float) n.getEignevector());
                            if (maxValue < n.getCurrentValue()) {
                                maxValue = n.getCurrentValue();
                            }
                            pb.updateValue(++pbCnt, nodes.size());
                        }
                    } else if (nodeValueComboBoxMenu.getSelectedIndex() == 9) {
                        for (MyDirectNode n : nodes) {
                            if (n.getCurrentValue() == 0) continue;;
                            n.setCurrentValue((float) n.getPageRankScore());
                            if (maxValue < n.getCurrentValue()) {
                                maxValue = n.getCurrentValue();
                            }
                            pb.updateValue(++pbCnt, nodes.size());
                        }
                    } else if (nodeValueComboBoxMenu.getSelectedIndex() > 9) {
                        for (MyDirectNode n : nodes) {
                            if (n.getCurrentValue() != 0 && n.nodeValues.containsKey(nodeValueComboBoxMenu.getSelectedItem().toString())) {
                                float value = n.nodeValues.get(nodeValueComboBoxMenu.getSelectedItem().toString());
                                n.setCurrentValue(value);
                                if (maxValue < n.getCurrentValue()) {
                                    maxValue = n.getCurrentValue();
                                }
                            } else if (n.getCurrentValue() != 0 && !n.nodeValues.containsKey(nodeValueComboBoxMenu.getSelectedItem().toString())) {
                                n.setCurrentValue(0);
                            }
                            pb.updateValue(++pbCnt, nodes.size());
                        }
                        MyVars.directMarkovChain.maxNodeValue = maxValue;
                    }
                    if (MyVars.getDirectGraphViewer().selectedSingleNode != null) {
                        MyVars.getDirectGraphViewer().directGraphViewerControlPanel.removeBarCharts();
                        MyVars.getDirectGraphViewer().neighborNodeValueRankBarChart = new MyNeighborNodeValueBarChart();
                        MyVars.getDirectGraphViewer().add(MyVars.getDirectGraphViewer().neighborNodeValueRankBarChart);
                        MyVars.main.getDirectMarkovChainDashBoard().nodeLevelTabbedPane.setSelectedIndex(0);
                    } else if (MyVars.getDirectGraphViewer().multiNodes != null) {
                        MyVars.getDirectGraphViewer().directGraphViewerControlPanel.removeBarCharts();
                        MyVars.getDirectGraphViewer().multiNodeLevelNeighborNodeValueBarChart = new MyMultiNodeLevelNeighborNodeValueBarChart();
                        MyVars.getDirectGraphViewer().add(MyVars.getDirectGraphViewer().multiNodeLevelNeighborNodeValueBarChart);
                        MyVars.main.getDirectMarkovChainDashBoard().multiNodeLevelTabbedPane.setSelectedIndex(0);
                    } else {
                        MyVars.getDirectGraphViewer().directGraphViewerControlPanel.removeBarCharts();
                        MyVars.getDirectGraphViewer().nodeValueRankBarChart = new MyNodeValueBarChart();
                        MyVars.getDirectGraphViewer().add(MyVars.getDirectGraphViewer().nodeValueRankBarChart);
                        MyVars.main.getDirectMarkovChainDashBoard().topLevelNodeValueDistribution.decorate();
                        MyVars.main.getDirectMarkovChainDashBoard().topLevelTabbedPane.setSelectedIndex(0);
                    }
                    MyVars.main.getDirectMarkovChainDashBoard().directGraphTextStatisticsPanel.setTextStatistics();
                    MyVars.main.getDirectMarkovChainDashBoard().updateEdgeTable();
                    MyVars.main.getDirectMarkovChainDashBoard().updateStatTable();
                    MyVars.main.getDirectMarkovChainDashBoard().updateRemainingNodeTable();
                    MyVars.main.getDirectMarkovChainDashBoard().revalidate();
                    MyVars.main.getDirectMarkovChainDashBoard().repaint();
                    pb.updateValue(100, 100);
                    pb.dispose();
                } else if (ae.getSource() == nodeLabelExcludeComboBoxMenu) {
                    Set<String> uniqueLabelValueSet = new HashSet<>();
                    Collection<MyDirectNode> nodes = MyVars.directMarkovChain.getVertices();
                    for (MyDirectNode n : nodes) {
                        if (!uniqueLabelValueSet.contains(n.nodeLabels.get(nodeLabelExcludeComboBoxMenu.getSelectedItem().toString()))) {
                            uniqueLabelValueSet.add(n.nodeLabels.get(nodeLabelExcludeComboBoxMenu.getSelectedItem().toString()));
                        }
                    }
                    nodeLabelValueExcludeComboBoxMenu.removeAllItems();
                    nodeLabelValueExcludeComboBoxMenu.addItem("");
                    for (String nodeLabelValue : uniqueLabelValueSet) {
                        nodeLabelValueExcludeComboBoxMenu.addItem(nodeLabelValue);
                    }
                } else if (ae.getSource() == edgeLabelExcludeComboBoxMenu) {
                    Set<String> uniqueLabelValueSet = new HashSet<>();
                    Collection<MyDirectEdge> edges = MyVars.directMarkovChain.getEdges();
                    for (MyDirectEdge e : edges) {
                        if (!uniqueLabelValueSet.contains(e.edgeLabels.get(edgeLabelExcludeComboBoxMenu.getSelectedItem().toString()))) {
                            uniqueLabelValueSet.add(e.edgeLabels.get(edgeLabelExcludeComboBoxMenu.getSelectedItem().toString()));
                        }
                    }
                    edgeLabelValueExcludeComboBoxMenu.removeAllItems();
                    edgeLabelValueExcludeComboBoxMenu.addItem("");
                    for (String edgeLabelValue : uniqueLabelValueSet) {
                        edgeLabelValueExcludeComboBoxMenu.addItem(edgeLabelValue);
                    }
                }
            }
        });
        MyVars.currentThread.start();
    }

    public void removeBarCharts() {
        if (MyVars.getDirectGraphViewer().nodeValueRankBarChart != null) {
            MyVars.getDirectGraphViewer().remove(MyVars.getDirectGraphViewer().nodeValueRankBarChart);
        }

        if (MyVars.getDirectGraphViewer().neighborNodeValueRankBarChart != null) {
            MyVars.getDirectGraphViewer().remove(MyVars.getDirectGraphViewer().neighborNodeValueRankBarChart);
        }

        if (MyVars.getDirectGraphViewer().edgeValueBarChart != null) {
            MyVars.getDirectGraphViewer().remove(MyVars.getDirectGraphViewer().edgeValueBarChart);
        }

        if (MyVars.getDirectGraphViewer().multiNodeLevelNeighborNodeValueBarChart != null) {
            MyVars.getDirectGraphViewer().remove(MyVars.getDirectGraphViewer().multiNodeLevelNeighborNodeValueBarChart);
        }
    }
}
