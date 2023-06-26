package medousa.direct.graph;

import edu.uci.ics.jung.algorithms.scoring.BetweennessCentrality;
import medousa.direct.graph.barcharts.MyDirectGraphMultiNodeLevelNeighborNodeValueBarChart;
import medousa.direct.graph.barcharts.MyDirectGraphNeighborNodeValueBarChart;
import medousa.direct.graph.barcharts.MyDirectGraphNodeValueBarChart;
import medousa.message.MyMessageUtil;
import medousa.direct.utils.MyDirectGraphMathUtil;
import medousa.direct.utils.MyDirectGraphSysUtil;
import medousa.direct.utils.MyDirectGraphVars;
import medousa.MyProgressBar;
import medousa.sequential.graph.MyEdge;
import medousa.sequential.graph.MyNode;
import org.apache.commons.collections15.Transformer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

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
    public JComboBox clusteringSelector = new JComboBox();
    public JCheckBox edgeValueCheckBox = new JCheckBox("E. V.");
    public JCheckBox mouseHoverCheckBox = new JCheckBox("M. H.");
    public JCheckBox nodeValueCheckBox = new JCheckBox("N. V.");
    public JCheckBox edgeLabelCheckBox = new JCheckBox("E. L.");
    public JCheckBox nodeLabelCheckBox = new JCheckBox("N. L.");
    public JCheckBox removeEdgeCheckBox = new JCheckBox("R. E.");
    public JCheckBox weightedNodeColorCheckBox = new JCheckBox("N. C.");
    public JLabel percentLabel = new JLabel();
    public JPanel checkBoxControlPanel = new JPanel();
    public JComboBox shortestDistanceNodeValueComboboxMenu;
    public JLabel edgeValueLabel = new JLabel("E. V.:");
    public JLabel clusteringSectorLabel = new JLabel("CLS.:");
    public JLabel edgeLabelLabel = new JLabel("E. L.:");
    public JLabel edgeValueExcludeLabel = new JLabel("E. V.");
    public JLabel edgeLabelExcludeLabel = new JLabel("E. L.");
    public JTabbedPane graphControlTabbedPane = new JTabbedPane();


    public MyDirectGraphController() {
        this.decorate();
    }

    public void decorate() {
        this.setBackground(Color.WHITE);
        this.setLayout(new BorderLayout(3,3));

        JLabel nodeLabelLabel = new JLabel("N. L.:");
        nodeLabelLabel.setToolTipText("NODE LABEL");
        nodeLabelLabel.setFont(MyDirectGraphVars.tahomaPlainFont11);
        nodeLabelLabel.setBackground(Color.WHITE);

        JLabel nodeValueLabel = new JLabel("N. V.:");
        nodeValueLabel.setFont(MyDirectGraphVars.tahomaPlainFont11);
        nodeValueLabel.setBackground(Color.WHITE);
        nodeValueLabel.setToolTipText("NODE VALUE");

        this.edgeLabelLabel.setFont(MyDirectGraphVars.tahomaPlainFont11);
        this.edgeLabelLabel.setBackground(Color.WHITE);
        this.edgeLabelLabel.setToolTipText("EDGE LABEL");

        this.edgeValueLabel.setFont(MyDirectGraphVars.tahomaPlainFont11);
        this.edgeValueLabel.setBackground(Color.WHITE);
        this.edgeValueLabel.setToolTipText("EDGE VALUE");

        this.edgeLabelComboBoxMenu = new JComboBox();
        this.edgeLabelComboBoxMenu.setToolTipText("SELECT AN EDGE LABEL");
        this.edgeLabelComboBoxMenu.setFont(MyDirectGraphVars.tahomaPlainFont11);
        this.edgeLabelComboBoxMenu.setBackground(Color.WHITE);
        this.edgeLabelComboBoxMenu.setFocusable(false);
        this.edgeLabelComboBoxMenu.addItem("");
        for (String edgeLabel : MyDirectGraphVars.userDefinedEdgeLabesl) {
            this.edgeLabelComboBoxMenu.addItem(edgeLabel);
        }
        this.edgeLabelComboBoxMenu.addActionListener(this);

        this.edgeValueComboBoxMenu = new JComboBox();
        this.edgeValueComboBoxMenu.setToolTipText("SELECT AN EDGE VALUE");
        this.edgeValueComboBoxMenu.setFont(MyDirectGraphVars.tahomaPlainFont11);
        this.edgeValueComboBoxMenu.setBackground(Color.WHITE);
        this.edgeValueComboBoxMenu.setFocusable(false);
        this.edgeValueComboBoxMenu.addItem("");
        this.edgeValueComboBoxMenu.addItem("CONT.");
        this.edgeValueComboBoxMenu.addItem("BTW.");
        for (String userDefinedEdgeValue : MyDirectGraphVars.userDefinedEdgeValues.keySet()) {
            this.edgeValueComboBoxMenu.addItem(userDefinedEdgeValue);
        }

        String [] edgeValueTooltips = {"SELECT AN EDGE VALUE", "CONTRIBUTION", "BETWEENESS"};
        MyDirectGraphComboBoxTooltipper edgeValueTooltipper = new MyDirectGraphComboBoxTooltipper();
        edgeValueTooltipper.setTooltips(Arrays.asList(edgeValueTooltips));
        this.edgeValueComboBoxMenu.setRenderer(edgeValueTooltipper);

        this.nodeLabelComboBoxMenu = new JComboBox();
        this.nodeLabelComboBoxMenu.setToolTipText("SELECT A NODE LABEL");
        this.nodeLabelComboBoxMenu.setFont(MyDirectGraphVars.tahomaPlainFont11);
        this.nodeLabelComboBoxMenu.setBackground(Color.WHITE);
        this.nodeLabelComboBoxMenu.setFocusable(false);
        this.nodeLabelComboBoxMenu.addItem("");
        this.nodeLabelComboBoxMenu.addItem("NAME");
        for (String userDefinedNodeLabel : MyDirectGraphVars.userDefinedNodeLabels) {
            this.nodeLabelComboBoxMenu.addItem(userDefinedNodeLabel);
        }
        this.nodeLabelComboBoxMenu.setSelectedIndex(0);

        this.nodeValueComboBoxMenu = new JComboBox();
        this.nodeValueComboBoxMenu.setToolTipText("SELECT A NODE VALUE");
        this.nodeValueComboBoxMenu.setFont(MyDirectGraphVars.tahomaPlainFont11);
        this.nodeValueComboBoxMenu.setBackground(Color.WHITE);
        this.nodeValueComboBoxMenu.setFocusable(false);
        this.nodeValueComboBoxMenu.addItem("CONT.");
        this.nodeValueComboBoxMenu.addItem("IN-CONT.");
        this.nodeValueComboBoxMenu.addItem("OUT-CONT.");
        this.nodeValueComboBoxMenu.addItem("PRED.");
        this.nodeValueComboBoxMenu.addItem("SUCC.");
        this.nodeValueComboBoxMenu.addItem("BTW.");
        this.nodeValueComboBoxMenu.addItem("EIGEN.");
        this.nodeValueComboBoxMenu.addItem("PAGERANK");
        this.nodeValueComboBoxMenu.addItem("RE. OUT-N. C.");
        this.nodeValueComboBoxMenu.addItem("UNR. OUT-N. C.");
        this.nodeValueComboBoxMenu.addItem("RE. IN-N. C.");
        this.nodeValueComboBoxMenu.addItem("UNR. IN-N. C.");
        this.nodeValueComboBoxMenu.addItem("S. AVG. OUT-D");
        this.nodeValueComboBoxMenu.addItem("S. AVG. IN-D.");
        for (String userDefinedNodeValue : MyDirectGraphVars.userDefinedNodeValues.keySet()) {
            this.nodeValueComboBoxMenu.addItem(userDefinedNodeValue);
        }

        String [] nodeValueTooltips = {
                "CONTRIBUTION",
                "IN-CONTRIBUTION",
                "OUT-CONTRIBUTION",
                "PREDECESSOR",
                "SUCCESSOR",
                "BETWEENESS",
                "EIGENVECTOR",
                "PAGERANK",
                "REACHED OUT-NODE COUNT",
                "UNREACHED OUT-NODE COUNT",
                "REACHED IN-NODE COUNT",
                "UNREACHEED IN-NODE COUNT",
                "SHORTEST AVG. OUT-DISTANCE",
                "SHORTEST AVG. IN-DISTANCE"};
        MyDirectGraphComboBoxTooltipper nodeValueTooltipper = new MyDirectGraphComboBoxTooltipper();
        nodeValueTooltipper.setTooltips(Arrays.asList(nodeValueTooltips));
        this.nodeValueComboBoxMenu.setRenderer(nodeValueTooltipper);

        this.shortestDistanceNodeValueComboboxMenu = new JComboBox();
        this.shortestDistanceNodeValueComboboxMenu.setToolTipText("SELECT A NODE VALUE");
        this.shortestDistanceNodeValueComboboxMenu.setFont(MyDirectGraphVars.tahomaPlainFont12);
        this.shortestDistanceNodeValueComboboxMenu.setBackground(Color.WHITE);
        this.shortestDistanceNodeValueComboboxMenu.setFocusable(false);
        this.shortestDistanceNodeValueComboboxMenu.addItem("CONT.");
        this.shortestDistanceNodeValueComboboxMenu.addItem("PRED.");
        this.shortestDistanceNodeValueComboboxMenu.addItem("SUCC.");

        this.edgeLabelCheckBox.setFocusable(false);
        this.edgeLabelCheckBox.setFont(MyDirectGraphVars.tahomaPlainFont12);
        this.edgeLabelCheckBox.setBackground(Color.WHITE);

        this.edgeValueCheckBox.setFocusable(false);
        this.edgeValueCheckBox.setToolTipText("SHOW EDGE VALUES");
        this.edgeValueCheckBox.setFont(MyDirectGraphVars.tahomaPlainFont12);
        this.edgeValueCheckBox.setBackground(Color.WHITE);

        this.nodeLabelExcludeComboBoxMenu = new JComboBox();
        this.nodeLabelExcludeComboBoxMenu.setToolTipText("SELECT NODE LABEL TO EXCLUDE FROM THE GRAPH.");
        this.nodeLabelExcludeComboBoxMenu.setFocusable(false);
        this.nodeLabelExcludeComboBoxMenu.setFont(MyDirectGraphVars.tahomaPlainFont12);
        this.nodeLabelExcludeComboBoxMenu.setBackground(Color.WHITE);
        for (String nodeLabelValue : MyDirectGraphVars.userDefinedNodeLabels) {
            this.nodeLabelExcludeComboBoxMenu.addItem(nodeLabelValue);
        }
        this.nodeLabelExcludeComboBoxMenu.addActionListener(this);

        this.nodeLabelValueExcludeComboBoxMenu = new JComboBox();
        this.nodeLabelValueExcludeComboBoxMenu.setToolTipText("SELECT NODE LABEL VALUE TO EXCLUDE FROM THE GRAPH.");
        this.nodeLabelValueExcludeComboBoxMenu.setFocusable(false);
        this.nodeLabelValueExcludeComboBoxMenu.setFont(MyDirectGraphVars.tahomaPlainFont12);
        this.nodeLabelValueExcludeComboBoxMenu.setBackground(Color.WHITE);

        this.edgeLabelValueExcludeComboBoxMenu = new JComboBox();
        this.edgeLabelValueExcludeComboBoxMenu.setToolTipText("SELECT EDGE LABEL VALUE TO EXCLUDE FROM THE GRAPH.");
        this.edgeLabelValueExcludeComboBoxMenu.setFocusable(false);
        this.edgeLabelValueExcludeComboBoxMenu.setFont(MyDirectGraphVars.tahomaPlainFont12);
        this.edgeLabelValueExcludeComboBoxMenu.setBackground(Color.WHITE);

        this.edgeLabelExcludeComboBoxMenu = new JComboBox();
        this.edgeLabelExcludeComboBoxMenu.setToolTipText("");
        this.edgeLabelExcludeComboBoxMenu.setFocusable(false);
        this.edgeLabelExcludeComboBoxMenu.setFont(MyDirectGraphVars.tahomaPlainFont12);
        this.edgeLabelExcludeComboBoxMenu.setBackground(Color.WHITE);
        for (String nodeLabelValue : MyDirectGraphVars.userDefinedEdgeLabesl) {
            this.edgeLabelExcludeComboBoxMenu.addItem(nodeLabelValue);
        }

        this.excludeBtn = new JButton("EXCL.");
        this.excludeBtn.setToolTipText("EXCLUDE");
        this.excludeBtn.setBackground(Color.WHITE);
        this.excludeBtn.setFocusable(false);
        this.excludeBtn.setFont(MyDirectGraphVars.tahomaPlainFont12);
        this.excludeBtn.addActionListener(this);
        this.edgeValueComboBoxMenu.addActionListener(this);

        this.nodeLabelCheckBox.setToolTipText("SHOW NODE LABELS");
        this.nodeLabelCheckBox.setFocusable(false);
        this.nodeLabelCheckBox.setFont(MyDirectGraphVars.tahomaPlainFont12);
        this.nodeLabelCheckBox.setBackground(Color.WHITE);

        this.nodeValueCheckBox.setToolTipText("SHOW NODE VALUES");
        this.nodeValueCheckBox.setFont(MyDirectGraphVars.tahomaPlainFont12);
        this.nodeValueCheckBox.setBackground(Color.WHITE);
        this.nodeValueCheckBox.setFocusable(false);
        this.nodeLabelCheckBox.addActionListener(this);
        this.nodeValueCheckBox.addActionListener(this);
        this.edgeValueCheckBox.addActionListener(this);
        this.removeEdgeCheckBox.setToolTipText("REMOVE EDGES");
        this.removeEdgeCheckBox.setFont(MyDirectGraphVars.tahomaPlainFont12);
        this.removeEdgeCheckBox.setFocusable(false);
        this.removeEdgeCheckBox.setBackground(Color.WHITE);
        this.mouseHoverCheckBox.setToolTipText("HOVER MOUSE OVER NODES");
        this.mouseHoverCheckBox.setFont(MyDirectGraphVars.tahomaPlainFont12);
        this.mouseHoverCheckBox.setFocusable(false);
        this.mouseHoverCheckBox.setBackground(Color.WHITE);

        clusteringSectorLabel.setBackground(Color.WHITE);
        clusteringSectorLabel.setFont(MyDirectGraphVars.tahomaPlainFont12);

        this.clusteringSelector.setBackground(Color.WHITE);
        this.clusteringSelector.setFont(MyDirectGraphVars.tahomaPlainFont12);
        this.clusteringSelector.setFocusable(false);
        this.clusteringSelector.addItem("");
        this.clusteringSelector.addItem("BTW.");
        this.clusteringSelector.addItem("MOD.");
        this.clusteringSelector.addActionListener(this);

        this.removeEdgeCheckBox.addActionListener(this);
        this.edgeLabelCheckBox.addActionListener(this);
        this.nodeValueComboBoxMenu.addActionListener(this);
        this.nodeLabelComboBoxMenu.addActionListener(this);

        JLabel nodeValueExcludeLabel = new JLabel("N. V.");
        nodeValueExcludeLabel.setToolTipText("SELECT A NODE VALUE TO EXCLUDE");
        nodeValueExcludeLabel.setFocusable(false);
        nodeValueExcludeLabel.setBackground(Color.WHITE);
        nodeValueExcludeLabel.setFont(MyDirectGraphVars.tahomaPlainFont12);

        JLabel nodeLabelExcludeLabel = new JLabel("N. L.");
        nodeLabelExcludeLabel.setFont(MyDirectGraphVars.tahomaPlainFont12);
        nodeLabelExcludeLabel.setToolTipText("SELECT A NODE LABEL TO EXCLUDE");

        this.nodeValueExcludeSymbolComboBoxMenu = new JComboBox();
        this.nodeValueExcludeSymbolComboBoxMenu.setToolTipText("SELECT AN NODE VALUE ARITHMETIC SYMBOL");
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
        this.nodeValueExcludeSymbolComboBoxMenu.setFont(MyDirectGraphVars.tahomaPlainFont12);

        this.nodeLabelExcludeSymbolComboBoxMenu = new JComboBox();
        this.nodeLabelExcludeSymbolComboBoxMenu.addItem("");
        this.nodeLabelExcludeSymbolComboBoxMenu.addItem("==");
        this.nodeLabelExcludeSymbolComboBoxMenu.addItem("!=");
        this.nodeLabelExcludeSymbolComboBoxMenu.setFocusable(false);
        this.nodeLabelExcludeSymbolComboBoxMenu.setBackground(Color.WHITE);
        this.nodeLabelExcludeSymbolComboBoxMenu.setFont(MyDirectGraphVars.tahomaPlainFont12);

        this.nodeValueExcludeTxt = new JTextField();
        this.nodeValueExcludeTxt.setToolTipText("INPUT A THRESHOLD");
        this.nodeValueExcludeTxt.setHorizontalAlignment(JTextField.CENTER);
        this.nodeValueExcludeTxt.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        this.nodeValueExcludeTxt.setPreferredSize(new Dimension(60, 22));
        this.nodeValueExcludeTxt.setFont(MyDirectGraphVars.tahomaPlainFont12);

        this.edgeValueExcludeLabel.setToolTipText("PROVIDE AN EDGE VALUE TO EXCLUDE");
        this.edgeValueExcludeLabel.setFocusable(false);
        this.edgeValueExcludeLabel.setBackground(Color.WHITE);
        this.edgeValueExcludeLabel.setFont(MyDirectGraphVars.tahomaPlainFont12);

        this.edgeLabelExcludeLabel.setFont(MyDirectGraphVars.tahomaPlainFont12);
        this.edgeLabelExcludeLabel.setToolTipText("SELECT AN EDGE LABEL TO EXCLUDE");

        this.edgeValueExcludeSymbolComboBoxMenu = new JComboBox();
        this.edgeValueExcludeSymbolComboBoxMenu.setToolTipText("SELECT AN EDGE VALUE ARITHMETIC SYMBOL");
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
        this.edgeValueExcludeSymbolComboBoxMenu.setFont(MyDirectGraphVars.tahomaPlainFont12);

        this.edgeLabelExcludeSymbolComboBoxMenu = new JComboBox();
        this.edgeLabelExcludeSymbolComboBoxMenu.addItem("");
        this.edgeLabelExcludeSymbolComboBoxMenu.addItem("==");
        this.edgeLabelExcludeSymbolComboBoxMenu.addItem("!=");
        this.edgeLabelExcludeSymbolComboBoxMenu.setFocusable(false);
        this.edgeLabelExcludeSymbolComboBoxMenu.setBackground(Color.WHITE);
        this.edgeLabelExcludeSymbolComboBoxMenu.setFont(MyDirectGraphVars.tahomaPlainFont12);

        this.edgeValueExcludeTxt = new JTextField();
        this.edgeValueExcludeTxt.setToolTipText("INPUT A THRESHOLD");
        this.edgeValueExcludeTxt.setHorizontalAlignment(JTextField.CENTER);
        this.edgeValueExcludeTxt.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        this.edgeValueExcludeTxt.setPreferredSize(new Dimension(60, 22));
        this.edgeValueExcludeTxt.setFont(MyDirectGraphVars.tahomaPlainFont12);

        this.weightedNodeColorCheckBox.setFocusable(false);
        this.weightedNodeColorCheckBox.setToolTipText("SHOW WEIGHTED NODE COLORS");
        this.weightedNodeColorCheckBox.setFont(MyDirectGraphVars.tahomaPlainFont12);
        this.weightedNodeColorCheckBox.setBackground(Color.WHITE);
        this.weightedNodeColorCheckBox.addActionListener(this);

        this.checkBoxControlPanel.setBackground(Color.WHITE);
        this.checkBoxControlPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 2,2));

        this.percentLabel.setFont(MyDirectGraphVars.tahomaPlainFont12);
        this.percentLabel.setBackground(Color.WHITE);
        String nodePercent = MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.directGraph.getVertexCount()) + "[100%]";
        this.percentLabel.setText(nodePercent);

        checkBoxControlPanel.add(clusteringSectorLabel);
        checkBoxControlPanel.add(clusteringSelector);
        checkBoxControlPanel.add(this.mouseHoverCheckBox);
        checkBoxControlPanel.add(this.weightedNodeColorCheckBox);
        checkBoxControlPanel.add(this.removeEdgeCheckBox);
        checkBoxControlPanel.add(this.nodeValueCheckBox);
        checkBoxControlPanel.add(this.edgeValueCheckBox);

        JLabel emptyLabel1 = new JLabel(" ");
        emptyLabel1.setFont(MyDirectGraphVars.tahomaPlainFont10);
        JLabel emptyLabel2 = new JLabel("          ");
        emptyLabel2.setFont(MyDirectGraphVars.tahomaPlainFont10);
        JLabel emptyLabel3 = new JLabel(" ");
        emptyLabel3.setFont(MyDirectGraphVars.tahomaPlainFont10);
        JLabel emptyLabel4 = new JLabel(" ");
        emptyLabel4.setFont(MyDirectGraphVars.tahomaPlainFont10);
        JLabel emptyLabel5 = new JLabel(" ");
        emptyLabel4.setFont(MyDirectGraphVars.tahomaPlainFont10);

        JPanel rightControlPanel = new JPanel();
        rightControlPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3,3));
        rightControlPanel.setBackground(Color.WHITE);

        rightControlPanel.add(emptyLabel1);
        rightControlPanel.add(nodeValueLabel);
        rightControlPanel.add(this.nodeValueComboBoxMenu);

       // if (MyVars.nodeLabels.size() > 0) {
            rightControlPanel.add(nodeLabelLabel);
            rightControlPanel.add(this.nodeLabelComboBoxMenu);
      //  }

        rightControlPanel.add(this.edgeValueLabel);
        rightControlPanel.add(this.edgeValueComboBoxMenu);

        if (MyDirectGraphVars.userDefinedEdgeLabesl.size() > 0) {
            rightControlPanel.add(this.edgeLabelLabel);
            rightControlPanel.add(this.edgeLabelComboBoxMenu);
        }

        JPanel leftControlPanel = new JPanel();
        leftControlPanel.setBackground(Color.WHITE);
        leftControlPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 2,2));

        leftControlPanel.add(nodeValueExcludeLabel);
        leftControlPanel.add(this.nodeValueExcludeSymbolComboBoxMenu);
        leftControlPanel.add(this.nodeValueExcludeTxt);

        if (MyDirectGraphVars.userDefinedNodeLabels.size() > 0) {
            leftControlPanel.add(emptyLabel3);
            leftControlPanel.add(nodeLabelExcludeLabel);
            leftControlPanel.add(this.nodeLabelExcludeSymbolComboBoxMenu);
            leftControlPanel.add(this.nodeLabelExcludeComboBoxMenu);
            leftControlPanel.add(this.nodeLabelValueExcludeComboBoxMenu);
        }

        leftControlPanel.add(emptyLabel4);
        leftControlPanel.add(edgeValueExcludeLabel);
        leftControlPanel.add(this.edgeValueExcludeSymbolComboBoxMenu);
        leftControlPanel.add(this.edgeValueExcludeTxt);

        if (MyDirectGraphVars.userDefinedEdgeLabesl.size() > 0) {
            leftControlPanel.add(emptyLabel5);
            leftControlPanel.add(this.edgeLabelExcludeLabel);
            leftControlPanel.add(this.edgeLabelExcludeSymbolComboBoxMenu);
            leftControlPanel.add(this.edgeLabelExcludeComboBoxMenu);
            leftControlPanel.add(this.edgeLabelValueExcludeComboBoxMenu);
        }

        leftControlPanel.add(this.excludeBtn);
        this.add(rightControlPanel, BorderLayout.EAST);
        this.add(leftControlPanel, BorderLayout.WEST);


        //this.add(checkBoxControlPanel, BorderLayout.EAST);
    }

    @Override public void actionPerformed(ActionEvent ae) {
        MyDirectGraphVars.currentThread = new Thread(new Runnable() {
            @Override public void run() {
                if (ae.getSource() == clusteringSelector) {
                    MyDirectGraphVars.app.getDirectGraphDashBoard().graphFilterPanel.setVisible(false);
                    if (clusteringSelector.getSelectedIndex() == 1) {
                        if (MyDirectGraphClusteringConfig.instances++ == 0) {
                            new Thread(new Runnable() {
                                @Override public void run() {
                                    MyDirectGraphClusteringConfig clusteringConfig = new MyDirectGraphClusteringConfig();
                                    clusteringSelector.setEnabled(false);
                                }
                            }).start();
                        }
                    } else if (clusteringSelector.getSelectedIndex() == 0) {
                        if (MyDirectGraphVars.getDirectGraphViewer().isClustered) {
                            MyDirectGraphVars.getDirectGraphViewer().directGraphViewerMouseListener.setDefaultView();
                            MyDirectGraphVars.getDirectGraphViewer().isClustered = false;
                            clusteringSelector.setEnabled(true);
                        }
                    } else if (clusteringSelector.getSelectedIndex() == 2) {
                        clusteringSelector.setEnabled(false);
                    }
                } else if (ae.getSource() == edgeValueCheckBox) {
                    if (edgeValueComboBoxMenu.getSelectedIndex() == 0) {
                        edgeValueCheckBox.setSelected(false);
                        MyMessageUtil.showInfoMsg(MyDirectGraphVars.app, "Select an edge value, first.");
                        return;
                    }
                    if (edgeValueCheckBox.isSelected()) {
                        edgeValueCheckBox.setToolTipText("HIDE EDGE VALUES");
                        MyDirectGraphVars.getDirectGraphViewer().getRenderContext().setEdgeFontTransformer(new Transformer<MyDirectEdge, Font>() {
                            @Override public Font transform(MyDirectEdge myDirectEdge) {
                                return new Font("Noto Sans", Font.PLAIN, 20);
                            }
                        });
                        MyDirectGraphVars.getDirectGraphViewer().getRenderContext().setEdgeLabelTransformer(new Transformer<MyDirectEdge, String>() {
                            @Override public String transform(MyDirectEdge e) {
                                String value = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(e.getCurrentValue()));
                                value = value.endsWith(".00") ? value.substring(0, value.indexOf(".00")) : value;
                                return value;
                            }
                        });
                    } else {
                        MyDirectGraphVars.getDirectGraphViewer().getRenderContext().setEdgeFontTransformer(new Transformer<MyDirectEdge, Font>() {
                            @Override public Font transform(MyDirectEdge myDirectEdge) {
                                return new Font("Noto Sans", Font.PLAIN, 0);
                            }
                        });
                        edgeValueCheckBox.setToolTipText("SHOW EDGE VALUES");
                    }
                    MyDirectGraphVars.getDirectGraphViewer().revalidate();
                    MyDirectGraphVars.getDirectGraphViewer().repaint();
                } else if (ae.getSource() == excludeBtn) {
                    boolean allPassed = true;
                    long nonZeroNodeCount = MyDirectGraphVars.directGraph.getNonZeroNodes();
                    long nonZeroEdgeCount = MyDirectGraphVars.directGraph.getNonZeroEdges();

                    String edgeStrValue = edgeValueExcludeTxt.getText().trim();
                    if (!edgeStrValue.contains(",") && edgeStrValue.matches("\\d+(\\.\\d+)?")) {
                        double numValue = Double.valueOf(edgeStrValue);
                        if (edgeValueComboBoxMenu.getSelectedIndex() == 0) {
                            MyMessageUtil.showInfoMsg("Select an edge value, first.");
                            return;
                        }

                        Collection<MyDirectEdge> edges = MyDirectGraphVars.directGraph.getEdges();
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
                                if (e.getCurrentValue() > numValue && e.getCurrentValue() > 0) {
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
                                if (e.getCurrentValue() >= numValue && e.getCurrentValue() > 0) {
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
                                if (e.getCurrentValue() == numValue && e.getCurrentValue() > 0) {
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
                                if (e.getCurrentValue() < numValue && e.getCurrentValue() > 0) {
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
                                if (e.getCurrentValue() <= numValue && e.getCurrentValue() > 0) {
                                    e.setOriginalNumericValue(e.getCurrentValue());
                                    e.setCurrentValue(0.0f);
                                }
                            }
                            pb.updateValue(100, 100);
                            pb.dispose();
                        } else if (edgeValueExcludeSymbolComboBoxMenu.getSelectedIndex() == 6) {
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
                        MyDirectGraphVars.getDirectGraphViewer().revalidate();
                        MyDirectGraphVars.getDirectGraphViewer().repaint();
                    } else if (edgeValueExcludeSymbolComboBoxMenu.getSelectedIndex() == 7 && edgeStrValue.contains(",")) {
                        String strValue1 = "";
                        String strValue2 = "";
                        strValue1 = edgeStrValue.split(",")[0];
                        strValue2 = edgeStrValue.split(",")[1];
                        if (strValue1.matches("\\d+") && strValue2.matches("\\d+")) {
                            double numValue1 = Double.valueOf(strValue1);
                            double numValue2 = Double.valueOf(strValue2);
                            MyProgressBar pb = new MyProgressBar(false);
                            Collection<MyDirectEdge> edges = MyDirectGraphVars.directGraph.getEdges();
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
                        Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
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
                                Collection<MyDirectEdge> edges = MyDirectGraphVars.directGraph.getEdges();
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
                                Collection<MyDirectNode> ns = MyDirectGraphVars.directGraph.getVertices();
                                for (MyDirectNode n : ns) {
                                    if (!nonZeroNodes.contains(n)) {
                                            n.setCurrentValue(0f);
                                    }
                                }
                                pb.updateValue(100, 100);
                                pb.dispose();
                            } else {
                                Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
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
                        Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
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
                        Collection<MyDirectEdge> edges = MyDirectGraphVars.directGraph.getEdges();
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
                        MyDirectGraphVars.app.getDirectGraphDashBoard().txtStatistics.setTextStatistics();
                        long updatedNonZeroNodes = MyDirectGraphVars.directGraph.getVertexCount() - MyDirectGraphVars.directGraph.getNonZeroNodes();
                        long updatedNonZeroEdges = MyDirectGraphVars.directGraph.getEdgeCount() - MyDirectGraphVars.directGraph.getNonZeroEdges();

                        MyMessageUtil.showInfoMsg(
                            MyDirectGraphMathUtil.getCommaSeperatedNumber(updatedNonZeroNodes) + "[" +
                            MyDirectGraphMathUtil.twoDecimalPercent((float) updatedNonZeroNodes/MyDirectGraphVars.directGraph.getVertexCount()) + "]" +
                            " nodes got removed and " +
                            MyDirectGraphMathUtil.getCommaSeperatedNumber(updatedNonZeroEdges) + "[" +
                            MyDirectGraphMathUtil.twoDecimalPercent((float) updatedNonZeroEdges/MyDirectGraphVars.directGraph.getEdgeCount()) + "]" +
                            " edges got removed. ");

                        MyDirectGraphVars.getDirectGraphViewer().revalidate();
                        MyDirectGraphVars.getDirectGraphViewer().repaint();
                        MyDirectGraphVars.getDirectGraphViewer().isExcludeBtnOn = true;
                    }
                } else if (ae.getSource() == edgeValueComboBoxMenu) {
                    if (edgeValueComboBoxMenu.getSelectedIndex() == 0) {
                        MyProgressBar pb = new MyProgressBar(false);
                        try {
                            Collection<MyDirectEdge> edges = MyDirectGraphVars.directGraph.getEdges();
                            int pbCnt = 0;
                            for (MyDirectEdge e : edges) {
                                pb.updateValue(++pbCnt, edges.size());
                                e.setCurrentValue(-1);
                            }
                            MyDirectGraphVars.getDirectGraphViewer().getRenderContext().setEdgeFontTransformer(new Transformer<MyDirectEdge, Font>() {
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
                            Collection<MyDirectEdge> edges = MyDirectGraphVars.directGraph.getEdges();
                            int pbCnt = 0;
                            for (MyDirectEdge e : edges) {
                                pb.updateValue(++pbCnt, edges.size());
                                if (e.getCurrentValue() == 0) continue;
                                e.setCurrentValue(e.getContribution());
                                if (max < e.getCurrentValue()) {
                                    max = e.getCurrentValue();
                                }
                            }
                            MyDirectGraphVars.directGraph.maxEdgeValue = max;
                            pb.updateValue(100, 100);
                            pb.dispose();
                        } catch (Exception ex) {
                            pb.updateValue(100, 100);
                            pb.dispose();
                        }
                    } else if (edgeValueComboBoxMenu.getSelectedIndex() == 2) {
                        MyProgressBar pb = new MyProgressBar(false);
                        try {
                            float max = 0f;
                            for (MyDirectEdge e : MyDirectGraphVars.directGraph.getEdges()) {
                                if (e.getCurrentValue() == 0) continue;
                                if (e.betweeness > max) max = e.betweeness;
                                e.setCurrentValue(e.betweeness);
                            }

                            MyDirectGraphVars.directGraph.maxEdgeValue = max;
                            pb.updateValue(100, 100);
                            pb.dispose();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            pb.updateValue(100, 100);
                            pb.dispose();
                        }
                    }
                    MyDirectGraphVars.getDirectGraphViewer().getRenderContext().setEdgeStrokeTransformer(MyDirectGraphVars.getDirectGraphViewer().weightedEdgeStroker);
                    MyDirectGraphVars.app.getDirectGraphDashBoard().updateEdgeValueRelatedComponents();
                    MyDirectGraphVars.app.getDirectGraphDashBoard().updateEdgeTable();
                    MyDirectGraphVars.app.getDirectGraphDashBoard().updateStatTable();
                    if (MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode != null) {
                        MyDirectGraphVars.app.getDirectGraphDashBoard().nodeLevelTabbedPane.setSelectedIndex(1);
                    } else if (MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode != null) {
                        MyDirectGraphVars.app.getDirectGraphDashBoard().multiNodeLevelTabbedPane.setSelectedIndex(1);
                    } else {
                        MyDirectGraphVars.app.getDirectGraphDashBoard().topLevelTabbedPane.setSelectedIndex(1);
                    }
                    MyDirectGraphVars.app.getDirectGraphDashBoard().txtStatistics.setTextStatistics();
                    MyDirectGraphVars.getDirectGraphViewer().revalidate();
                    MyDirectGraphVars.getDirectGraphViewer().repaint();
                } else if (ae.getSource() == edgeLabelComboBoxMenu) {
                    if (edgeLabelComboBoxMenu.getSelectedIndex() == 0) {
                        MyDirectGraphVars.app.getDirectGraphDashBoard().edgeLabelEnlargeBtn.setEnabled(false);
                        MyDirectGraphVars.getDirectGraphViewer().getRenderContext().setEdgeLabelTransformer(new Transformer<MyDirectEdge, String>() {
                            @Override public String transform(MyDirectEdge e) {return "";}});
                    } else {
                        MyDirectGraphVars.getDirectGraphViewer().getRenderContext().setEdgeFontTransformer(new Transformer<MyDirectEdge, Font>() {
                            @Override public Font transform(MyDirectEdge e) {
                                if (e.edgeLabels.containsKey(edgeLabelComboBoxMenu.getSelectedItem().toString())) {
                                    if (e.getCurrentValue() == 0 || e.getSource().getCurrentValue() == 0 || e.getDest().getCurrentValue() == 0) {
                                        return new Font("Noto Sans", Font.PLAIN, 0);
                                    } else {
                                        return new Font("Noto Sans", Font.PLAIN, 60);
                                    }
                                } else {
                                    return new Font("Noto Sans", Font.PLAIN, 00);
                                }
                            }
                        });

                        MyDirectGraphVars.getDirectGraphViewer().getRenderContext().setEdgeLabelTransformer(new Transformer<MyDirectEdge, String>() {
                            @Override public String transform(MyDirectEdge e) {
                                if (e.edgeLabels.containsKey(edgeLabelComboBoxMenu.getSelectedItem().toString())) {
                                    if (e.getCurrentValue() == 0 || e.getSource().getCurrentValue() == 0 || e.getDest().getCurrentValue() == 0) {
                                        return "";
                                    } else {
                                        return e.getCurrentLabel();
                                    }
                                } else {
                                    return "";
                                }
                            }
                        });

                        MyDirectGraphVars.app.getDirectGraphDashBoard().edgeLabelBarChart.setEdgeLabelBarChart();
                        MyDirectGraphVars.app.getDirectGraphDashBoard().chartTabbedPane.setSelectedIndex(1);

                        MyDirectGraphVars.app.getDirectGraphDashBoard().edgeLabelBarChart.revalidate();
                        MyDirectGraphVars.app.getDirectGraphDashBoard().edgeLabelBarChart.repaint();

                        MyDirectGraphVars.app.getDirectGraphDashBoard().edgeLabelEnlargeBtn.setEnabled(true);
                    }
                } else if (ae.getSource() == nodeLabelCheckBox) {
                    if (nodeLabelCheckBox.isSelected()) {
                        nodeLabelCheckBox.setToolTipText("HIDE NODE LABELS");
                        MyDirectGraphVars.getDirectGraphViewer().getRenderContext().setVertexFontTransformer(new Transformer<MyDirectNode, Font>() {
                            @Override public Font transform(MyDirectNode n) {
                                if (MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode != null) {
                                    if (MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode == n ||
                                            MyDirectGraphVars.directGraph.getPredecessors(MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode).contains(n) ||
                                            MyDirectGraphVars.directGraph.getSuccessors(MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode).contains(n)) {
                                        return new Font("Noto Sans", Font.PLAIN, 70);
                                    } else {
                                        return new Font("Noto Sans", Font.PLAIN, 0);
                                    }
                                } else {
                                    return new Font("Noto Sans", Font.PLAIN, 70);
                                }
                            }
                        });

                        MyDirectGraphVars.getDirectGraphViewer().getRenderContext().setVertexLabelTransformer(new Transformer<MyDirectNode, String>() {
                            @Override public String transform(MyDirectNode n) {
                                if (MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode != null) {
                                    if (MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode == n ||
                                            MyDirectGraphVars.directGraph.getPredecessors(MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode).contains(n) ||
                                            MyDirectGraphVars.directGraph.getSuccessors(MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode).contains(n)) {
                                        if (nodeValueCheckBox.isSelected()) {
                                            String value = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(n.getCurrentValue()));
                                            value = value.endsWith(".00") ? value.substring(0, value.indexOf(".00")) : value;
                                            return n.getCurrentLabel() + "[" + value + "]";
                                        } else {
                                            return n.getCurrentLabel();
                                        }
                                    } else {
                                        if (nodeValueCheckBox.isSelected()) {
                                            String value = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(n.getCurrentValue()));
                                            value = value.endsWith(".00") ? value.substring(0, value.indexOf(".00")) : value;
                                            return n.getCurrentLabel() + "[" + value + "]";
                                        } else {
                                            return n.getCurrentLabel();
                                        }
                                    }
                                } else {
                                    if (nodeValueCheckBox.isSelected()) {
                                        String value = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(n.getCurrentValue()));
                                        value = value.endsWith(".00") ? value.substring(0, value.indexOf(".00")) : value;
                                        return n.getCurrentLabel() + "[" + value + "]";
                                    } else {
                                        return n.getCurrentLabel();
                                    }
                                }
                            }
                        });
                    } else {
                        if (nodeValueCheckBox.isSelected()) {
                            MyDirectGraphVars.getDirectGraphViewer().getRenderContext().setVertexLabelTransformer(new Transformer<MyDirectNode, String>() {
                                @Override public String transform(MyDirectNode n) {
                                    n.setCurrentLabel(n.getName());
                                    nodeLabelComboBoxMenu.setSelectedIndex(0);
                                    String value = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(n.getCurrentValue()));
                                    value = value.endsWith(".00") ? value.substring(0, value.indexOf(".00")) : value;
                                    return value;
                                }
                            });
                        } else {
                            MyDirectGraphVars.getDirectGraphViewer().getRenderContext().setVertexLabelTransformer(new Transformer<MyDirectNode, String>() {
                                @Override public String transform(MyDirectNode n) {
                                    return "";
                                }
                            });
                        }
                        nodeLabelCheckBox.setToolTipText("SHOW NODE LABELS");
                    }

                    MyDirectGraphVars.getDirectGraphViewer().revalidate();
                    MyDirectGraphVars.getDirectGraphViewer().repaint();
                } else if (ae.getSource() == nodeValueCheckBox) {
                    if (nodeValueCheckBox.isSelected()) {
                        nodeValueCheckBox.setToolTipText("HIDE NODE VALUES");
                        MyDirectGraphVars.getDirectGraphViewer().getRenderContext().setVertexFontTransformer(new Transformer<MyDirectNode, Font>() {
                            @Override public Font transform(MyDirectNode n) {
                                if (MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode != null) {
                                    if (n.getCurrentValue() == 0) {
                                        return new Font("Noto Sans", Font.PLAIN, 0);
                                    } else if (MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode == n ||
                                            MyDirectGraphVars.directGraph.getPredecessors(MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode).contains(n) ||
                                            MyDirectGraphVars.directGraph.getSuccessors(MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode).contains(n)) {
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
                        MyDirectGraphVars.getDirectGraphViewer().getRenderContext().setVertexLabelTransformer(new Transformer<MyDirectNode, String>() {
                            @Override public String transform(MyDirectNode n) {
                                String value = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(n.getCurrentValue()));
                                value = value.endsWith(".00") ? value.substring(0, value.indexOf(".00")) : value;
                                if (MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode != null) {
                                    if (MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode == n ||
                                            MyDirectGraphVars.directGraph.getPredecessors(MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode).contains(n) ||
                                            MyDirectGraphVars.directGraph.getSuccessors(MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode).contains(n)) {
                                        if (nodeLabelCheckBox.isSelected()) {
                                            return n.getCurrentLabel() + "[" + value + "]";
                                        } else {
                                            return value;
                                        }
                                    } else {
                                        if (nodeLabelCheckBox.isSelected()) {
                                            return n.getCurrentLabel() + "[" + value + "]";
                                        } else {
                                            return value;
                                        }
                                    }
                                } else {
                                    if (nodeLabelCheckBox.isSelected()) {
                                        return n.getCurrentLabel() + "[" + value + "]";
                                    } else {
                                        return value;
                                    }
                                }
                            }
                        });
                    } else {
                        if (nodeLabelCheckBox.isSelected()) {
                            nodeValueCheckBox.setToolTipText("SHOW NODE VALUES");
                            MyDirectGraphVars.getDirectGraphViewer().getRenderContext().setVertexLabelTransformer(new Transformer<MyDirectNode, String>() {
                                @Override public String transform(MyDirectNode n) {
                                    return n.getCurrentLabel();
                                }
                            });
                        } else {
                            MyDirectGraphVars.getDirectGraphViewer().getRenderContext().setVertexLabelTransformer(new Transformer<MyDirectNode, String>() {
                                @Override public String transform(MyDirectNode n) {
                                    return "";
                                }
                            });
                        }
                    }
                    MyDirectGraphVars.getDirectGraphViewer().revalidate();
                    MyDirectGraphVars.getDirectGraphViewer().repaint();
                } else if (ae.getSource() == removeEdgeCheckBox) {
                    if (removeEdgeCheckBox.isSelected()) {
                        removeEdgeCheckBox.setToolTipText("SHOW EDGES");
                        MyDirectGraphVars.getDirectGraphViewer().getRenderContext().setEdgeDrawPaintTransformer(new Transformer<MyDirectEdge, Paint>() {
                            @Override public Paint transform(MyDirectEdge e) {
                                return new Color(0.0f, 0.0f, 0.0f, 0.0f);
                            }
                        });

                        MyDirectGraphVars.getDirectGraphViewer().getRenderContext().setArrowDrawPaintTransformer(new Transformer<MyDirectEdge, Paint>() {
                            @Override
                            public Paint transform(MyDirectEdge e) {
                               return new Color(0, 0, 0, 0f);
                            }
                        });
                        MyDirectGraphVars.getDirectGraphViewer().getRenderContext().setArrowFillPaintTransformer(new Transformer<MyDirectEdge, Paint>() {
                            @Override
                            public Paint transform(MyDirectEdge e) {
                                return new Color(0, 0, 0, 0f);
                            }
                        });
                    } else {
                        MyDirectGraphVars.getDirectGraphViewer().getRenderContext().setArrowDrawPaintTransformer(new Transformer<MyDirectEdge, Paint>() {
                            @Override public Paint transform(MyDirectEdge e) {
                                if (MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode != null &&
                                        (e.getSource() != MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode || e.getDest() != MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode)) {
                                    return new Color(0, 0, 0, 0f);
                                } else {
                                    return Color.GRAY;
                                }
                            }
                        });
                        MyDirectGraphVars.getDirectGraphViewer().getRenderContext().setArrowFillPaintTransformer(new Transformer<MyDirectEdge, Paint>() {
                            @Override public Paint transform(MyDirectEdge e) {
                                if (MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode != null &&
                                        (e.getSource() != MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode || e.getDest() != MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode)) {
                                    return new Color(0, 0, 0, 0f);
                                } else {
                                    return Color.GRAY;
                                }
                            }
                        });
                        removeEdgeCheckBox.setToolTipText("REMOVE EDGES");
                        MyDirectGraphVars.getDirectGraphViewer().setUnWeightedEdgeColor();
                    }
                    MyDirectGraphVars.getDirectGraphViewer().revalidate();
                    MyDirectGraphVars.getDirectGraphViewer().repaint();
                } else if (ae.getSource() == nodeLabelComboBoxMenu) {
                    if (nodeLabelComboBoxMenu.getSelectedIndex() == 1) {
                        MyProgressBar pb = new MyProgressBar(false);
                        try {
                            MyDirectGraphVars.getDirectGraphViewer().getRenderContext().setVertexFontTransformer(new Transformer<MyDirectNode, Font>() {
                                @Override public Font transform(MyDirectNode n) {
                                    if (n.getCurrentValue() != 0) {
                                        return new Font("Noto Sans", Font.PLAIN, 50);
                                    } else {
                                        return new Font("Noto Sans", Font.PLAIN, 0);
                                    }
                                }
                            });

                            MyDirectGraphVars.getDirectGraphViewer().getRenderContext().setVertexLabelTransformer(new Transformer<MyDirectNode, String>() {
                                @Override public String transform(MyDirectNode n) {
                                    if (MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode != null) {
                                        if (MyDirectGraphVars.getDirectGraphViewer().selectedSingleNodeSuccessorSet.contains(n) ||
                                            MyDirectGraphVars.getDirectGraphViewer().selectedSingleNodePredecessorSet.contains(n) ||
                                            n == MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode) {
                                            return n.getCurrentLabel();
                                        } else {return "";}
                                    } else if (MyDirectGraphVars.getDirectGraphViewer().multiNodes != null) {
                                        if (MyDirectGraphVars.getDirectGraphViewer().multiNodes.contains(n) ||
                                            MyDirectGraphVars.getDirectGraphViewer().multiNodePredecessorSet.contains(n) ||
                                            MyDirectGraphVars.getDirectGraphViewer().multiNodeSuccessorSet.contains(n)) {
                                            return n.getCurrentLabel();
                                        } else {return "";}
                                    } else {return n.getCurrentLabel();}
                                }
                            });

                            MyDirectGraphVars.app.getDirectGraphDashBoard().nodeLabelBarChart.decorate();
                            MyDirectGraphVars.app.getDirectGraphDashBoard().chartTabbedPane.setSelectedIndex(1);

                            MyDirectGraphVars.app.getDirectGraphDashBoard().nodeLabelBarChart.revalidate();
                            MyDirectGraphVars.app.getDirectGraphDashBoard().nodeLabelBarChart.repaint();

                            MyDirectGraphVars.getDirectGraphViewer().revalidate();
                            MyDirectGraphVars.getDirectGraphViewer().repaint();
                            MyDirectGraphVars.app.getDirectGraphDashBoard().nodeLabelEnlargeBtn.setEnabled(true);

                            pb.updateValue(100, 100);
                            pb.dispose();
                        } catch (Exception ex) {
                            pb.updateValue(100, 100);
                            pb.dispose();
                        }
                    } else if (nodeLabelComboBoxMenu.getSelectedIndex() > 1) {
                        MyProgressBar pb = new MyProgressBar(false);
                        try {
                            Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
                            for (MyDirectNode n : nodes) {
                                if (n.nodeLabels.containsKey(nodeLabelComboBoxMenu.getSelectedItem().toString())) {
                                    MyDirectGraphVars.getDirectGraphViewer().getRenderContext().setVertexFontTransformer(new Transformer<MyDirectNode, Font>() {
                                        @Override public Font transform(MyDirectNode n) {
                                            if (n.nodeLabels.containsKey(nodeLabelComboBoxMenu.getSelectedItem().toString())) {
                                                return new Font("Noto Sans", Font.PLAIN, 0);
                                            } else {
                                                return new Font("Noto Sans", Font.PLAIN, 60);
                                            }
                                        }
                                    });

                                    MyDirectGraphVars.getDirectGraphViewer().getRenderContext().setVertexLabelTransformer(new Transformer<MyDirectNode, String>() {
                                        @Override public String transform(MyDirectNode n) {
                                            if (MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode != null) {
                                                if (MyDirectGraphVars.getDirectGraphViewer().selectedSingleNodeSuccessorSet.contains(n) ||
                                                    MyDirectGraphVars.getDirectGraphViewer().selectedSingleNodePredecessorSet.contains(n) ||
                                                    n == MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode) {
                                                    return n.getCurrentLabel();
                                                } else {return "";}
                                            } else if (MyDirectGraphVars.getDirectGraphViewer().multiNodes != null) {
                                                if (MyDirectGraphVars.getDirectGraphViewer().multiNodes.contains(n) ||
                                                    MyDirectGraphVars.getDirectGraphViewer().multiNodePredecessorSet.contains(n) ||
                                                    MyDirectGraphVars.getDirectGraphViewer().multiNodeSuccessorSet.contains(n)) {
                                                    return n.getCurrentLabel();
                                                } else {return "";}
                                            } else {
                                                return n.getCurrentLabel();
                                            }
                                        }
                                    });
                                }
                            }

                            MyDirectGraphVars.app.getDirectGraphDashBoard().nodeLabelBarChart.decorate();
                            MyDirectGraphVars.app.getDirectGraphDashBoard().chartTabbedPane.setSelectedIndex(1);

                            MyDirectGraphVars.app.getDirectGraphDashBoard().nodeLabelBarChart.revalidate();
                            MyDirectGraphVars.app.getDirectGraphDashBoard().nodeLabelBarChart.repaint();

                            MyDirectGraphVars.getDirectGraphViewer().revalidate();
                            MyDirectGraphVars.getDirectGraphViewer().repaint();
                            MyDirectGraphVars.app.getDirectGraphDashBoard().nodeLabelEnlargeBtn.setEnabled(true);

                            pb.updateValue(100, 100);
                            pb.dispose();
                        } catch (Exception ex) {
                            pb.updateValue(100, 100);
                            pb.dispose();
                        }
                    } else {
                        MyProgressBar pb = new MyProgressBar(false);

                        try {
                            MyDirectGraphVars.getDirectGraphViewer().getRenderContext().setVertexLabelTransformer(new Transformer<MyDirectNode, String>() {
                                @Override public String transform(MyDirectNode n) {

                                        return "";

                                }
                            });

                            MyDirectGraphVars.app.getDirectGraphDashBoard().nodeLabelBarChart.decorate();
                            MyDirectGraphVars.app.getDirectGraphDashBoard().chartTabbedPane.setSelectedIndex(0);
                            MyDirectGraphVars.app.getDirectGraphDashBoard().nodeLabelBarChart.revalidate();
                            MyDirectGraphVars.app.getDirectGraphDashBoard().nodeLabelBarChart.repaint();
                            MyDirectGraphVars.app.getDirectGraphDashBoard().nodeLabelEnlargeBtn.setEnabled(false);

                            pb.updateValue(100, 100);
                            pb.dispose();
                            MyDirectGraphVars.getDirectGraphViewer().revalidate();
                            MyDirectGraphVars.getDirectGraphViewer().repaint();
                        } catch (Exception ex) {
                            pb.updateValue(100, 100);
                            pb.dispose();
                        }
                    }
                } else if (weightedNodeColorCheckBox == ae.getSource()) {
                    if (weightedNodeColorCheckBox.isSelected()) {
                        weightedNodeColorCheckBox.setToolTipText("SET UNWEIGHTED NODE COLORS");
                        MyDirectGraphVars.getDirectGraphViewer().getRenderContext().setVertexFillPaintTransformer(MyDirectGraphVars.getDirectGraphViewer().weightedNodeColor);
                    } else {
                        weightedNodeColorCheckBox.setToolTipText("SET WEIGHTED NODE COLORS");
                        MyDirectGraphVars.getDirectGraphViewer().getRenderContext().setVertexFillPaintTransformer(MyDirectGraphVars.getDirectGraphViewer().unWeightedNodeColor);
                    }
                    MyDirectGraphVars.getDirectGraphViewer().revalidate();
                    MyDirectGraphVars.getDirectGraphViewer().repaint();
                } else if (edgeLabelCheckBox == ae.getSource()) {
                    if (edgeLabelCheckBox.isSelected()) {
                        edgeLabelCheckBox.setToolTipText("HIDE EDGE LABELS");
                        if (edgeLabelComboBoxMenu.getSelectedIndex() == 0) {
                            MyMessageUtil.showInfoMsg("Select an edge label, first.");
                            edgeLabelCheckBox.setSelected(false);
                            return;
                        }
                        MyDirectGraphVars.getDirectGraphViewer().getRenderContext().setEdgeFontTransformer(new Transformer<MyDirectEdge, Font>() {
                            @Override public Font transform(MyDirectEdge e) {
                                if (MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode != null) {
                                    if (e.getSource().getCurrentValue() == 0 || e.getDest().getCurrentValue() == 0) {
                                        return new Font("Noto Sans", Font.PLAIN, 0);
                                    } else if (MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode == e.getSource() ||
                                            MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode == e.getDest()) {
                                        return new Font("Noto Sans", Font.PLAIN, 60);
                                    } else {
                                        return new Font("Noto Sans", Font.PLAIN, 0);
                                    }
                                } else {
                                    return new Font("Noto Sans", Font.PLAIN, 60);
                                }
                            }
                        });
                        MyDirectGraphVars.getDirectGraphViewer().getRenderContext().setEdgeLabelTransformer(new Transformer<MyDirectEdge, String>() {
                            @Override public String transform(MyDirectEdge e) {
                                return e.edgeLabels.get(edgeLabelComboBoxMenu.getSelectedItem());
                            }
                        });
                    } else {
                        if (edgeValueCheckBox.isSelected()) {
                            MyDirectGraphVars.getDirectGraphViewer().getRenderContext().setEdgeLabelTransformer(new Transformer<MyDirectEdge, String>() {
                                @Override public String transform(MyDirectEdge e) {
                                    return e.getCurrentValue() + "";
                                }
                            });
                        } else {
                            MyDirectGraphVars.getDirectGraphViewer().getRenderContext().setEdgeLabelTransformer(new Transformer<MyDirectEdge, String>() {
                                @Override public String transform(MyDirectEdge e) {
                                    return "";
                                }
                            });
                        }
                        edgeLabelCheckBox.setToolTipText("SHOW EDGE VALUES");
                        edgeLabelComboBoxMenu.setSelectedIndex(0);
                    }
                    MyDirectGraphVars.getDirectGraphViewer().revalidate();
                    MyDirectGraphVars.getDirectGraphViewer().repaint();
                } else if (ae.getSource() == nodeValueComboBoxMenu) {
                    if (MyDirectGraphVars.app.getDirectGraphDashBoard().topLevelTabbedPane.getSelectedIndex() == 3) {
                        MyProgressBar pb = new MyProgressBar(false);
                        int pbCnt = 0;
                        float maxValue = 0.00f;
                        Collection<MyDirectNode> nodes =
                            (MyDirectGraphVars.app.getDirectGraphDashBoard().visitedNodes == null ? MyDirectGraphVars.directGraph.getVertices() : MyDirectGraphVars.app.getDirectGraphDashBoard().visitedNodes);
                        if (nodeValueComboBoxMenu.getSelectedIndex() == 0) {
                            for (MyDirectNode n : nodes) {
                                if (n.getCurrentValue() == 0) continue;
                                n.setCurrentValue((float) n.getContribution());
                                if (maxValue < n.getCurrentValue()) {
                                    maxValue = n.getCurrentValue();
                                }
                                pb.updateValue(++pbCnt, nodes.size());
                            }
                        } else if (nodeValueComboBoxMenu.getSelectedIndex() == 1) {
                            for (MyDirectNode n : nodes) {
                                if (n.getCurrentValue() == 0) continue;
                                n.setCurrentValue((float) n.getInContribution());
                                if (maxValue < n.getCurrentValue()) {
                                    maxValue = n.getCurrentValue();
                                }
                                pb.updateValue(++pbCnt, nodes.size());
                            }
                        } else if (nodeValueComboBoxMenu.getSelectedIndex() == 2) {
                            for (MyDirectNode n : nodes) {
                                if (n.getCurrentValue() == 0) continue;
                                n.setCurrentValue((float) n.getOutContribution());
                                if (maxValue < n.getCurrentValue()) {
                                    maxValue = n.getCurrentValue();
                                }
                                pb.updateValue(++pbCnt, nodes.size());
                            }
                        } else if (nodeValueComboBoxMenu.getSelectedIndex() == 3) {
                            for (MyDirectNode n : nodes) {
                                if (n.getCurrentValue() == 0) continue;
                                n.setCurrentValue((float) MyDirectGraphVars.directGraph.getPredecessorCount(n));
                                if (maxValue < n.getCurrentValue()) {
                                    maxValue = n.getCurrentValue();
                                }
                                pb.updateValue(++pbCnt, nodes.size());
                            }
                        } else if (nodeValueComboBoxMenu.getSelectedIndex() == 4) {
                            for (MyDirectNode n : nodes) {
                                if (n.getCurrentValue() == 0) continue;
                                n.setCurrentValue((float) MyDirectGraphVars.directGraph.getSuccessorCount(n));
                                if (maxValue < n.getCurrentValue()) {
                                    maxValue = n.getCurrentValue();
                                }
                                pb.updateValue(++pbCnt, nodes.size());
                            }
                        } else if (nodeValueComboBoxMenu.getSelectedIndex() == 5) {
                            for (MyDirectNode n : nodes) {
                                if (n.getCurrentValue() == 0) continue;
                                n.setCurrentValue((float) n.getBetweeness());
                                if (maxValue < n.getCurrentValue()) {
                                    maxValue = n.getCurrentValue();
                                }
                                pb.updateValue(++pbCnt, nodes.size());
                            }
                        } else if (nodeValueComboBoxMenu.getSelectedIndex() == 6) {
                            for (MyDirectNode n : nodes) {
                                if (n.getCurrentValue() == 0) continue;
                                n.setCurrentValue((float) n.getEignevector());
                                if (maxValue < n.getCurrentValue()) {
                                    maxValue = n.getCurrentValue();
                                }
                                pb.updateValue(++pbCnt, nodes.size());
                            }
                        } else if (nodeValueComboBoxMenu.getSelectedIndex() == 7) {
                            for (MyDirectNode n : nodes) {
                                if (n.getCurrentValue() == 0) continue;
                                n.setCurrentValue((float) n.getPageRankScore());
                                if (maxValue < n.getCurrentValue()) {
                                    maxValue = n.getCurrentValue();
                                }
                                pb.updateValue(++pbCnt, nodes.size());
                            }
                        } else if (nodeValueComboBoxMenu.getSelectedIndex() == 8) {
                            for (MyDirectNode n : nodes) {
                                if (n.getCurrentValue() == 0) continue;
                                n.setCurrentValue((float) n.getReachedOutNodeCount());
                                if (maxValue < n.getCurrentValue()) {
                                    maxValue = n.getCurrentValue();
                                }
                                pb.updateValue(++pbCnt, nodes.size());
                            }
                        } else if (nodeValueComboBoxMenu.getSelectedIndex() == 9) {
                            for (MyDirectNode n : nodes) {
                                if (n.getCurrentValue() == 0) continue;
                                n.setCurrentValue((float) n.getUnReachedOutNodeCount());
                                if (maxValue < n.getCurrentValue()) {
                                    maxValue = n.getCurrentValue();
                                }
                                pb.updateValue(++pbCnt, nodes.size());
                            }
                        } else if (nodeValueComboBoxMenu.getSelectedIndex() == 10) {
                            for (MyDirectNode n : nodes) {
                                if (n.getCurrentValue() == 0) continue;
                                n.setCurrentValue((float) n.getReachedInNodeCount());
                                if (maxValue < n.getCurrentValue()) {
                                    maxValue = n.getCurrentValue();
                                }
                                pb.updateValue(++pbCnt, nodes.size());
                            }
                        } else if (nodeValueComboBoxMenu.getSelectedIndex() == 11) {
                            int totalNodeCount = (int) MyDirectGraphVars.directGraph.getGraphNodeCount();
                            for (MyDirectNode n : nodes) {
                                if (n.getCurrentValue() == 0) continue;
                                n.setCurrentValue((totalNodeCount - n.getReachedInNodeCount())-1);
                                if (maxValue < n.getCurrentValue()) {
                                    maxValue = n.getCurrentValue();
                                }
                                pb.updateValue(++pbCnt, nodes.size());
                            }
                        } else if (nodeValueComboBoxMenu.getSelectedIndex() == 12) {
                            for (MyDirectNode n : nodes) {
                                if (n.getCurrentValue() == 0) continue;
                                n.setCurrentValue(n.getAverageShortestOutDistance());
                                if (maxValue < n.getCurrentValue()) {
                                    maxValue = n.getCurrentValue();
                                }
                                pb.updateValue(++pbCnt, nodes.size());
                            }
                        } else if (nodeValueComboBoxMenu.getSelectedIndex() == 13) {
                            for (MyDirectNode n : nodes) {
                                if (n.getCurrentValue() == 0) continue;
                                n.setCurrentValue(n.getAverageShortestInDistance());
                                if (maxValue < n.getCurrentValue()) {
                                    maxValue = n.getCurrentValue();
                                }
                                pb.updateValue(++pbCnt, nodes.size());
                            }
                        } else if (nodeValueComboBoxMenu.getSelectedIndex() > 13) {
                            for (MyDirectNode n : nodes) {
                                if (n.getCurrentValue() == 0) continue;
                                if (n.nodeValues.containsKey(nodeValueComboBoxMenu.getSelectedItem().toString())) {
                                    float value = n.nodeValues.get(nodeValueComboBoxMenu.getSelectedItem().toString());
                                    n.setCurrentValue(value);
                                    if (maxValue < n.getCurrentValue()) {
                                        maxValue = n.getCurrentValue();
                                    }
                                } else if (!n.nodeValues.containsKey(nodeValueComboBoxMenu.getSelectedItem().toString())) {
                                    n.setCurrentValue(0);
                                }
                                pb.updateValue(++pbCnt, nodes.size());
                            }
                        }

                        LinkedHashMap<String, Long> valueMap = new LinkedHashMap<>();
                        for (int i = 0; i < MyDirectGraphVars.app.getDirectGraphDashBoard().destTable.getRowCount(); i++) {
                            String node = MyDirectGraphVars.app.getDirectGraphDashBoard().destTable.getValueAt(i, 1).toString().replaceAll(" ", "");
                            MyDirectNode n = (MyDirectNode) MyDirectGraphVars.directGraph.vRefs.get(node);
                            valueMap.put(n.getName(), (long) n.getCurrentValue());
                            if (MyDirectGraphVars.directGraph.maxNodeValue < n.getCurrentValue()) {
                                MyDirectGraphVars.directGraph.maxNodeValue = n.getCurrentValue();
                            }
                        }
                        valueMap = MyDirectGraphSysUtil.sortMapByLongValue(valueMap);

                        while (MyDirectGraphVars.app.getDirectGraphDashBoard().destTable.getRowCount() > 0) {
                            int row = MyDirectGraphVars.app.getDirectGraphDashBoard().destTable.getRowCount() - 1;
                            ((DefaultTableModel) MyDirectGraphVars.app.getDirectGraphDashBoard().destTable.getModel()).removeRow(row);
                        }

                        int i=0;
                        for (String nn : valueMap.keySet()) {
                            MyDirectNode n = (MyDirectNode) MyDirectGraphVars.directGraph.vRefs.get(nn);
                            ((DefaultTableModel) MyDirectGraphVars.app.getDirectGraphDashBoard().destTable.getModel()).addRow(
                                    new String[]{"" + (++i), nn, MyDirectGraphMathUtil.twoDecimalFormat(n.getCurrentValue())});
                        }

                        MyDirectGraphVars.directGraph.maxNodeValue = maxValue;
                        MyDirectGraphVars.getDirectGraphViewer().vc.removeBarCharts();
                        MyDirectGraphVars.getDirectGraphViewer().nodeValueBarChart = new MyDirectGraphNodeValueBarChart();
                        MyDirectGraphVars.getDirectGraphViewer().add(MyDirectGraphVars.getDirectGraphViewer().nodeValueBarChart);
                        MyDirectGraphVars.app.getDirectGraphDashBoard().updateTopLevelCharts();

                        MyDirectGraphVars.app.getDirectGraphDashBoard().txtStatistics.setShortestPathTextStatistics();
                        MyDirectGraphVars.app.getDirectGraphDashBoard().updateEdgeTable();
                        MyDirectGraphVars.app.getDirectGraphDashBoard().updateStatTable();
                        MyDirectGraphVars.app.getDirectGraphDashBoard().updateRemainingNodeTable();
                        MyDirectGraphVars.app.getDirectGraphDashBoard().revalidate();
                        MyDirectGraphVars.app.getDirectGraphDashBoard().repaint();
                        pb.updateValue(100, 100);
                        pb.dispose();
                    } else {
                        MyProgressBar pb = new MyProgressBar(false);
                        int pbCnt = 0;
                        float maxValue = 0.00f;
                        Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
                        if (nodeValueComboBoxMenu.getSelectedIndex() == 0) {
                            for (MyDirectNode n : nodes) {
                                if (n.getCurrentValue() == 0) continue;
                                n.setCurrentValue((float) n.getContribution());
                                if (maxValue < n.getCurrentValue()) {
                                    maxValue = n.getCurrentValue();
                                }
                                pb.updateValue(++pbCnt, nodes.size());
                            }
                        } else if (nodeValueComboBoxMenu.getSelectedIndex() == 1) {
                            for (MyDirectNode n : nodes) {
                                if (n.getCurrentValue() == 0) continue;
                                n.setCurrentValue((float) n.getInContribution());
                                if (maxValue < n.getCurrentValue()) {
                                    maxValue = n.getCurrentValue();
                                }
                                pb.updateValue(++pbCnt, nodes.size());
                            }
                        } else if (nodeValueComboBoxMenu.getSelectedIndex() == 2) {
                            for (MyDirectNode n : nodes) {
                                if (n.getCurrentValue() == 0) continue;
                                n.setCurrentValue((float) n.getOutContribution());
                                if (maxValue < n.getCurrentValue()) {
                                    maxValue = n.getCurrentValue();
                                }
                                pb.updateValue(++pbCnt, nodes.size());
                            }
                        } else if (nodeValueComboBoxMenu.getSelectedIndex() == 3) {
                            for (MyDirectNode n : nodes) {
                                if (n.getCurrentValue() == 0) continue;
                                n.setCurrentValue((float) MyDirectGraphVars.directGraph.getPredecessorCount(n));
                                if (maxValue < n.getCurrentValue()) {
                                    maxValue = n.getCurrentValue();
                                }
                                pb.updateValue(++pbCnt, nodes.size());
                            }
                        } else if (nodeValueComboBoxMenu.getSelectedIndex() == 4) {
                            for (MyDirectNode n : nodes) {
                                if (n.getCurrentValue() == 0) continue;
                                n.setCurrentValue((float) MyDirectGraphVars.directGraph.getSuccessorCount(n));
                                if (maxValue < n.getCurrentValue()) {
                                    maxValue = n.getCurrentValue();
                                }
                                pb.updateValue(++pbCnt, nodes.size());
                            }
                        } else if (nodeValueComboBoxMenu.getSelectedIndex() == 5) {
                            for (MyDirectNode n : nodes) {
                                if (n.getCurrentValue() == 0) continue;
                                n.setCurrentValue((float) n.getBetweeness());
                                if (maxValue < n.getCurrentValue()) {
                                    maxValue = n.getCurrentValue();
                                }
                                pb.updateValue(++pbCnt, nodes.size());
                            }
                        } else if (nodeValueComboBoxMenu.getSelectedIndex() == 6) {
                            for (MyDirectNode n : nodes) {
                                if (n.getCurrentValue() == 0) continue;
                                n.setCurrentValue((float) n.getEignevector());
                                if (maxValue < n.getCurrentValue()) {
                                    maxValue = n.getCurrentValue();
                                }
                                pb.updateValue(++pbCnt, nodes.size());
                            }
                        } else if (nodeValueComboBoxMenu.getSelectedIndex() == 7) {
                            for (MyDirectNode n : nodes) {
                                if (n.getCurrentValue() == 0) continue;
                                n.setCurrentValue((float) n.getPageRankScore());
                                if (maxValue < n.getCurrentValue()) {
                                    maxValue = n.getCurrentValue();
                                }
                                pb.updateValue(++pbCnt, nodes.size());
                            }
                        } else if (nodeValueComboBoxMenu.getSelectedIndex() == 8) {
                            for (MyDirectNode n : nodes) {
                                if (n.getCurrentValue() == 0) continue;
                                n.setCurrentValue((float) n.getReachedOutNodeCount());
                                if (maxValue < n.getCurrentValue()) {
                                    maxValue = n.getCurrentValue();
                                }
                                pb.updateValue(++pbCnt, nodes.size());
                            }
                        } else if (nodeValueComboBoxMenu.getSelectedIndex() == 9) {
                            for (MyDirectNode n : nodes) {
                                if (n.getCurrentValue() == 0) continue;
                                n.setCurrentValue((float) n.getUnReachedOutNodeCount());
                                if (maxValue < n.getCurrentValue()) {
                                    maxValue = n.getCurrentValue();
                                }
                                pb.updateValue(++pbCnt, nodes.size());
                            }
                        } else if (nodeValueComboBoxMenu.getSelectedIndex() == 10) {
                            for (MyDirectNode n : nodes) {
                                if (n.getCurrentValue() == 0) continue;
                                n.setCurrentValue((float) n.getReachedInNodeCount());
                                if (maxValue < n.getCurrentValue()) {
                                    maxValue = n.getCurrentValue();
                                }
                                pb.updateValue(++pbCnt, nodes.size());
                            }
                        } else if (nodeValueComboBoxMenu.getSelectedIndex() == 11) {
                            int totalNodeCount = (int) MyDirectGraphVars.directGraph.getGraphNodeCount();
                            for (MyDirectNode n : nodes) {
                                if (n.getCurrentValue() == 0) continue;
                                n.setCurrentValue((float) ((totalNodeCount - n.reachedInNodeCount)-1));
                                if (maxValue < n.getCurrentValue()) {
                                    maxValue = n.getCurrentValue();
                                }
                                pb.updateValue(++pbCnt, nodes.size());
                            }
                        } else if (nodeValueComboBoxMenu.getSelectedIndex() == 12) {
                            for (MyDirectNode n : nodes) {
                                if (n.getCurrentValue() == 0) continue;
                                n.setCurrentValue(n.getAverageShortestOutDistance());
                                if (maxValue < n.getCurrentValue()) {
                                    maxValue = n.getCurrentValue();
                                }
                                pb.updateValue(++pbCnt, nodes.size());
                            }
                        } else if (nodeValueComboBoxMenu.getSelectedIndex() == 13) {
                              for (MyDirectNode n : nodes) {
                                if (n.getCurrentValue() == 0) continue;
                                n.setCurrentValue(n.getAverageShortestInDistance());
                                if (maxValue < n.getCurrentValue()) {
                                    maxValue = n.getCurrentValue();
                                }
                                pb.updateValue(++pbCnt, nodes.size());
                            }
                        } else if (nodeValueComboBoxMenu.getSelectedIndex() > 13) {
                            for (MyDirectNode n : nodes) {
                                if (n.getCurrentValue() > 0 && n.nodeValues.containsKey(nodeValueComboBoxMenu.getSelectedItem().toString())) {
                                    float value = n.nodeValues.get(nodeValueComboBoxMenu.getSelectedItem().toString());
                                    n.setCurrentValue(value);
                                    if (maxValue < n.getCurrentValue()) {
                                        maxValue = n.getCurrentValue();
                                    }
                                } else if (n.getCurrentValue() > 0 && !n.nodeValues.containsKey(nodeValueComboBoxMenu.getSelectedItem().toString())) {
                                    n.setCurrentValue(0);
                                }
                                pb.updateValue(++pbCnt, nodes.size());
                            }
                        }
                        MyDirectGraphVars.directGraph.maxNodeValue = maxValue;
                        if (MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode != null) {
                            MyDirectGraphVars.getDirectGraphViewer().vc.removeBarCharts();
                            MyDirectGraphVars.getDirectGraphViewer().neighborNodeValueRankBarChart = new MyDirectGraphNeighborNodeValueBarChart();
                            MyDirectGraphVars.getDirectGraphViewer().add(MyDirectGraphVars.getDirectGraphViewer().neighborNodeValueRankBarChart);
                            MyDirectGraphVars.app.getDirectGraphDashBoard().nodeLevelTabbedPane.setSelectedIndex(0);
                        } else if (MyDirectGraphVars.getDirectGraphViewer().multiNodes != null) {
                            MyDirectGraphVars.getDirectGraphViewer().vc.removeBarCharts();
                            MyDirectGraphVars.getDirectGraphViewer().multiNodeLevelNeighborNodeValueBarChart = new MyDirectGraphMultiNodeLevelNeighborNodeValueBarChart();
                            MyDirectGraphVars.getDirectGraphViewer().add(MyDirectGraphVars.getDirectGraphViewer().multiNodeLevelNeighborNodeValueBarChart);
                            MyDirectGraphVars.app.getDirectGraphDashBoard().multiNodeLevelTabbedPane.setSelectedIndex(0);
                        } else {
                            MyDirectGraphVars.getDirectGraphViewer().vc.removeBarCharts();
                            MyDirectGraphVars.getDirectGraphViewer().nodeValueBarChart = new MyDirectGraphNodeValueBarChart();
                            MyDirectGraphVars.getDirectGraphViewer().add(MyDirectGraphVars.getDirectGraphViewer().nodeValueBarChart);
                            MyDirectGraphVars.app.getDirectGraphDashBoard().updateTopLevelCharts();
                            MyDirectGraphVars.app.getDirectGraphDashBoard().topLevelTabbedPane.setSelectedIndex(0);
                        }
                        MyDirectGraphVars.app.getDirectGraphDashBoard().txtStatistics.setTextStatistics();
                        MyDirectGraphVars.app.getDirectGraphDashBoard().updateEdgeTable();
                        MyDirectGraphVars.app.getDirectGraphDashBoard().updateStatTable();
                        MyDirectGraphVars.app.getDirectGraphDashBoard().updateRemainingNodeTable();
                        MyDirectGraphVars.app.getDirectGraphDashBoard().revalidate();
                        MyDirectGraphVars.app.getDirectGraphDashBoard().repaint();
                        pb.updateValue(100, 100);
                        pb.dispose();
                    }
                } else if (ae.getSource() == nodeLabelExcludeComboBoxMenu) {
                    Set<String> uniqueLabelValueSet = new HashSet<>();
                    Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
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
                    Collection<MyDirectEdge> edges = MyDirectGraphVars.directGraph.getEdges();
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
        MyDirectGraphVars.currentThread.start();
    }

    public void removeBarCharts() {

        if (MyDirectGraphVars.getDirectGraphViewer().nodesByShortestDistanceBarChart != null) {
            MyDirectGraphVars.getDirectGraphViewer().remove(MyDirectGraphVars.getDirectGraphViewer().nodesByShortestDistanceBarChart);
        }

        if (MyDirectGraphVars.getDirectGraphViewer().nodeValueBarChart != null) {
            MyDirectGraphVars.getDirectGraphViewer().remove(MyDirectGraphVars.getDirectGraphViewer().nodeValueBarChart);
        }

        if (MyDirectGraphVars.getDirectGraphViewer().neighborNodeValueRankBarChart != null) {
            MyDirectGraphVars.getDirectGraphViewer().remove(MyDirectGraphVars.getDirectGraphViewer().neighborNodeValueRankBarChart);
        }

        if (MyDirectGraphVars.getDirectGraphViewer().edgeValueBarChart != null) {
            MyDirectGraphVars.getDirectGraphViewer().remove(MyDirectGraphVars.getDirectGraphViewer().edgeValueBarChart);
        }

        if (MyDirectGraphVars.getDirectGraphViewer().multiNodeLevelNeighborNodeValueBarChart != null) {
            MyDirectGraphVars.getDirectGraphViewer().remove(MyDirectGraphVars.getDirectGraphViewer().multiNodeLevelNeighborNodeValueBarChart);
        }

        MyDirectGraphVars.getDirectGraphViewer().revalidate();
        MyDirectGraphVars.getDirectGraphViewer().repaint();
    }
}
