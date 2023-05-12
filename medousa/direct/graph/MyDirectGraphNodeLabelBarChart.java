package medousa.direct.graph;

import medousa.MyProgressBar;
import medousa.direct.utils.MyDirectGraphMathUtil;
import medousa.direct.utils.MyDirectGraphSysUtil;
import medousa.direct.utils.MyDirectGraphVars;
import medousa.table.MyTableUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class MyDirectGraphNodeLabelBarChart
extends JPanel
implements ActionListener {

    private Image img;
    private final int BAR_LIMIT = 45;
    private Graphics graph;
    private final int BAR_WIDTH = 50;
    public LinkedHashMap<String, Float> data;
    private float maxVal = 0.00f;
    private Random rand = new Random();
    private int gap = 2;
    private ArrayList<Color> colors;
    private JFrame f;
    private LinkedHashMap<String, Float> predecessorData;
    private LinkedHashMap<String, Float> successorData;
    private LinkedHashMap<String, Float> sharedPredecessorData;
    private LinkedHashMap<String, Float> sharedSuccessorData;

    public MyDirectGraphNodeLabelBarChart() {
        decorate();
    }

    public synchronized void decorate() {
            this.data = new LinkedHashMap<>();
            this.colors = new ArrayList<>();
            this.predecessorData = new LinkedHashMap<>();
            this.successorData = new LinkedHashMap<>();
            this.sharedPredecessorData = new LinkedHashMap<>();
            this.sharedSuccessorData = new LinkedHashMap<>();
            this.maxVal = 0;

        if (MyDirectGraphVars.getDirectGraphViewer().vc.nodeLabelComboBoxMenu.getSelectedIndex() > 0) {
            if (MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode != null || MyDirectGraphVars.getDirectGraphViewer().multiNodes != null) {
                this.setPredecessorLabelData();
                this.setSuccessorLabelData();
            } else {
                this.setNodeLabelData();
            }
            this.revalidate();
            this.repaint();
        }
    }

    public void enlarge() {

        if (MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode != null) {
            MyProgressBar pb = new MyProgressBar(false);
            try {
                MyDirectGraphNodeLabelBarChart topLevelNodeLabelBarChart = new MyDirectGraphNodeLabelBarChart();
                topLevelNodeLabelBarChart.decorate();
                topLevelNodeLabelBarChart.setBackground(Color.WHITE);
                topLevelNodeLabelBarChart.setPreferredSize(new Dimension(500, (this.data.size()*10)+200));
                JScrollPane nodeLabelBarChartScrollPane = new JScrollPane(topLevelNodeLabelBarChart);
                nodeLabelBarChartScrollPane.setPreferredSize(new Dimension(500, 700));

                String[] predecessorColumns = {"NO.", "PREDECESSOR", "L."};
                String[][] predecessorData = {};
                DefaultTableModel predecessorTableModel = new DefaultTableModel(predecessorData, predecessorColumns);
                JTable predecessorTable = new JTable(predecessorTableModel);
                predecessorTable.setBackground(Color.WHITE);
                predecessorTable.setFont(MyDirectGraphVars.f_pln_12);
                predecessorTable.setRowHeight(22);
                predecessorTable.getTableHeader().setFont(MyDirectGraphVars.tahomaBoldFont11);
                predecessorTable.getTableHeader().setOpaque(false);
                predecessorTable.getTableHeader().setBackground(new Color(0, 0, 0, 0));
                predecessorTable.getTableHeader().getColumnModel().getColumn(0).setPreferredWidth(35);
                predecessorTable.getTableHeader().getColumnModel().getColumn(1).setPreferredWidth(300);
                predecessorTable.getTableHeader().getColumnModel().getColumn(1).setPreferredWidth(35);

                int i=0;
                Set<MyDirectNode> predecessors = MyDirectGraphVars.getDirectGraphViewer().selectedSingleNodePredecessorSet;
                for (MyDirectNode n : predecessors) {
                    predecessorTableModel.addRow(new String[]{"" + (++i), n.getName(), "" + n.getCurrentLabel()});
                }

                JScrollPane predecessorTableScrollPane = new JScrollPane(predecessorTable);
                predecessorTableScrollPane.setBackground(Color.WHITE);

                JTextField predecessorSearchTxt = new JTextField();
                JButton predecessorSelectBtn = new JButton("SEL.");
                predecessorSelectBtn.setFont(MyDirectGraphVars.tahomaPlainFont12);
                predecessorSelectBtn.setFocusable(false);
                predecessorSearchTxt.setBackground(Color.WHITE);
                predecessorSearchTxt.setBorder(BorderFactory.createLoweredSoftBevelBorder());
                JPanel predecessorSearchTxtPanel = MyTableUtil.searchAndSaveDataPanelForJTable2(topLevelNodeLabelBarChart, predecessorSearchTxt, predecessorSelectBtn, predecessorTableModel, predecessorTable);
                predecessorSearchTxtPanel.setFont(MyDirectGraphVars.f_bold_12);
                predecessorSearchTxtPanel.setToolTipText("TYPE A PREDECESSOR NAME TO SEARCH");
                predecessorSearchTxtPanel.setPreferredSize(new Dimension(90, 19));
                predecessorSelectBtn.setPreferredSize(new Dimension(54, 19));
                predecessorSearchTxtPanel.remove(predecessorSelectBtn);

                JPanel predecessorTablePanel = new JPanel();
                predecessorTablePanel.setBackground(Color.WHITE);
                predecessorTablePanel.setLayout(new BorderLayout(3, 3));
                predecessorTablePanel.add(predecessorTableScrollPane, BorderLayout.CENTER);
                predecessorTablePanel.add(predecessorSearchTxtPanel, BorderLayout.SOUTH);

                String[] successorColumns = {"NO.", "SUCCESSOR", "L."};
                String[][] successorData = {};
                DefaultTableModel successorTableModel = new DefaultTableModel(successorData, successorColumns);
                JTable successorTable = new JTable(successorTableModel);
                successorTable.setBackground(Color.WHITE);
                successorTable.setFont(MyDirectGraphVars.f_pln_12);
                successorTable.setRowHeight(22);
                successorTable.getTableHeader().setFont(MyDirectGraphVars.tahomaBoldFont11);
                successorTable.getTableHeader().setOpaque(false);
                successorTable.getTableHeader().setBackground(new Color(0, 0, 0, 0));
                successorTable.getTableHeader().getColumnModel().getColumn(0).setPreferredWidth(35);
                successorTable.getTableHeader().getColumnModel().getColumn(1).setPreferredWidth(300);
                successorTable.getTableHeader().getColumnModel().getColumn(1).setPreferredWidth(35);

                Set<MyDirectNode> successors = MyDirectGraphVars.getDirectGraphViewer().selectedSingleNodeSuccessorSet;
                for (MyDirectNode n : successors) {
                    successorTableModel.addRow(new String[]{"" + (++i), n.getName(), "" + n.getCurrentLabel()});
                }

                JScrollPane successorTableScrollPane = new JScrollPane(successorTable);
                successorTableScrollPane.setBackground(Color.WHITE);

                JTextField successorSearchTxt = new JTextField();
                JButton successorSelectBtn = new JButton("SEL.");
                successorSelectBtn.setFont(MyDirectGraphVars.tahomaPlainFont12);
                successorSelectBtn.setFocusable(false);
                successorSearchTxt.setBackground(Color.WHITE);
                successorSearchTxt.setBorder(BorderFactory.createLoweredSoftBevelBorder());
                JPanel successorSearchTxtPanel = MyTableUtil.searchAndSaveDataPanelForJTable2(topLevelNodeLabelBarChart, successorSearchTxt, successorSelectBtn, successorTableModel, successorTable);
                successorSearchTxtPanel.setFont(MyDirectGraphVars.f_bold_12);
                successorSearchTxtPanel.setToolTipText("TYPE A SUCCESSOR NAME TO SEARCH");
                successorSearchTxtPanel.setPreferredSize(new Dimension(90, 19));
                successorSelectBtn.setPreferredSize(new Dimension(54, 19));
                successorSearchTxtPanel.remove(successorSelectBtn);

                JPanel successorTablePanel = new JPanel();
                successorTablePanel.setBackground(Color.WHITE);
                successorTablePanel.setLayout(new BorderLayout(3, 3));
                successorTablePanel.add(successorTableScrollPane, BorderLayout.CENTER);
                successorTablePanel.add(successorSearchTxtPanel, BorderLayout.SOUTH);

                JSplitPane predecessorAndSuccessorSplitPane = new JSplitPane();
                predecessorAndSuccessorSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
                predecessorAndSuccessorSplitPane.setDividerSize(5);
                predecessorAndSuccessorSplitPane.setDividerLocation(MyDirectGraphSysUtil.screenHeight/2);
                predecessorAndSuccessorSplitPane.setTopComponent(predecessorTablePanel);
                predecessorAndSuccessorSplitPane.setBottomComponent(successorTablePanel);

                JSplitPane contentPane = new JSplitPane();
                contentPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
                contentPane.setLeftComponent(nodeLabelBarChartScrollPane);
                contentPane.setRightComponent(predecessorAndSuccessorSplitPane);
                contentPane.getLeftComponent().setBackground(Color.WHITE);
                contentPane.setDividerSize(5);

                topLevelNodeLabelBarChart.f = new JFrame("PREDECESSOR & SUCCESSOR NODE LABEL DISTRIBUTION");
                topLevelNodeLabelBarChart.f.setBackground(Color.WHITE);
                topLevelNodeLabelBarChart.f.setLayout(new BorderLayout(3,3));
                topLevelNodeLabelBarChart.f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                topLevelNodeLabelBarChart.f.addMouseListener(new MouseAdapter() {
                    @Override public void mouseEntered(MouseEvent e) {
                        super.mouseEntered(e);
                        topLevelNodeLabelBarChart.f.setAlwaysOnTop(true);
                    }

                    @Override public void mouseExited(MouseEvent e) {
                        super.mouseExited(e);
                        topLevelNodeLabelBarChart.f.setAlwaysOnTop(false);
                    }
                });

                topLevelNodeLabelBarChart.f.addComponentListener(new ComponentAdapter() {
                    @Override public void componentResized(ComponentEvent e) {
                        contentPane.setDividerLocation(0.35);
                    }
                });
                topLevelNodeLabelBarChart.f.setAlwaysOnTop(true);
                topLevelNodeLabelBarChart.f.getContentPane().add(contentPane, BorderLayout.CENTER);
                topLevelNodeLabelBarChart.f.pack();
                topLevelNodeLabelBarChart.f.setSize(new Dimension(800, 750));
                pb.updateValue(100, 100);
                pb.dispose();
                topLevelNodeLabelBarChart.f.setVisible(true);
                topLevelNodeLabelBarChart.f.setAlwaysOnTop(false);
            } catch (Exception ex) {
                pb.updateValue(100, 100);
                pb.dispose();
            }
        } else if (MyDirectGraphVars.getDirectGraphViewer().multiNodes != null) {
            MyProgressBar pb = new MyProgressBar(false);
            try {
                MyDirectGraphNodeLabelBarChart topLevelNodeLabelBarChart = new MyDirectGraphNodeLabelBarChart();
                topLevelNodeLabelBarChart.decorate();
                topLevelNodeLabelBarChart.setBackground(Color.WHITE);
                topLevelNodeLabelBarChart.setPreferredSize(new Dimension(500, (this.data.size()*10)+200));
                JScrollPane nodeLabelBarChartScrollPane = new JScrollPane(topLevelNodeLabelBarChart);
                nodeLabelBarChartScrollPane.setPreferredSize(new Dimension(500, 700));

                String[] sharedPredecessorColumns = {"NO.", "SHARED PREDECESSOR", "L."};
                String[][] sharedPredecessorData = {};
                DefaultTableModel sharedPredecessorTableModel = new DefaultTableModel(sharedPredecessorData, sharedPredecessorColumns);
                JTable sharedPredecessorTable = new JTable(sharedPredecessorTableModel);
                sharedPredecessorTable.setBackground(Color.WHITE);
                sharedPredecessorTable.setFont(MyDirectGraphVars.f_pln_12);
                sharedPredecessorTable.setRowHeight(22);
                sharedPredecessorTable.getTableHeader().setFont(MyDirectGraphVars.tahomaBoldFont11);
                sharedPredecessorTable.getTableHeader().setOpaque(false);
                sharedPredecessorTable.getTableHeader().setBackground(new Color(0, 0, 0, 0));
                sharedPredecessorTable.getTableHeader().getColumnModel().getColumn(0).setPreferredWidth(35);
                sharedPredecessorTable.getTableHeader().getColumnModel().getColumn(1).setPreferredWidth(300);
                sharedPredecessorTable.getTableHeader().getColumnModel().getColumn(1).setPreferredWidth(35);

                int i = 0;
                Set<MyDirectNode> sharedPredecessors = MyDirectGraphVars.getDirectGraphViewer().multiNodeSharedPredecessorSet;
                for (MyDirectNode n : sharedPredecessors) {
                    sharedPredecessorTableModel.addRow(new String[]{"" + (++i), n.getName(), "" + n.getCurrentLabel()});
                }

                JScrollPane sharedPredecessorTableScrollPane = new JScrollPane(sharedPredecessorTable);
                sharedPredecessorTableScrollPane.setBackground(Color.WHITE);

                JTextField sharedPredecessorSearchTxt = new JTextField();
                JButton sharedPredecessorSelectBtn = new JButton("SEL.");
                sharedPredecessorSelectBtn.setFont(MyDirectGraphVars.tahomaPlainFont12);
                sharedPredecessorSelectBtn.setFocusable(false);
                sharedPredecessorSearchTxt.setBackground(Color.WHITE);
                sharedPredecessorSearchTxt.setBorder(BorderFactory.createLoweredSoftBevelBorder());
                JPanel sharedPredecessorSearchTxtPanel = MyTableUtil.searchAndSaveDataPanelForJTable2(topLevelNodeLabelBarChart, sharedPredecessorSearchTxt, sharedPredecessorSelectBtn, sharedPredecessorTableModel, sharedPredecessorTable);
                sharedPredecessorSearchTxtPanel.setFont(MyDirectGraphVars.f_bold_12);
                sharedPredecessorSearchTxtPanel.setToolTipText("TYPE A PREDECESSOR NAME TO SEARCH");
                sharedPredecessorSearchTxtPanel.setPreferredSize(new Dimension(90, 19));
                sharedPredecessorSelectBtn.setPreferredSize(new Dimension(54, 19));
                sharedPredecessorSearchTxtPanel.remove(sharedPredecessorSelectBtn);

                JPanel sharedPredecessorTablePanel = new JPanel();
                sharedPredecessorTablePanel.setBackground(Color.WHITE);
                sharedPredecessorTablePanel.setLayout(new BorderLayout(3, 3));
                sharedPredecessorTablePanel.add(sharedPredecessorTableScrollPane, BorderLayout.CENTER);
                sharedPredecessorTablePanel.add(sharedPredecessorSearchTxtPanel, BorderLayout.SOUTH);

                String[] sharedSuccessorColumns = {"NO.", "SHARED SUCCESSOR", "L."};
                String[][] sharedSuccessorData = {};
                DefaultTableModel sharedSuccessorTableModel = new DefaultTableModel(sharedSuccessorData, sharedSuccessorColumns);
                JTable sharedSuccessorTable = new JTable(sharedSuccessorTableModel);
                sharedSuccessorTable.setBackground(Color.WHITE);
                sharedSuccessorTable.setFont(MyDirectGraphVars.f_pln_12);
                sharedSuccessorTable.setRowHeight(22);
                sharedSuccessorTable.getTableHeader().setFont(MyDirectGraphVars.tahomaBoldFont11);
                sharedSuccessorTable.getTableHeader().setOpaque(false);
                sharedSuccessorTable.getTableHeader().setBackground(new Color(0, 0, 0, 0));
                sharedSuccessorTable.getTableHeader().getColumnModel().getColumn(0).setPreferredWidth(35);
                sharedSuccessorTable.getTableHeader().getColumnModel().getColumn(1).setPreferredWidth(300);
                sharedSuccessorTable.getTableHeader().getColumnModel().getColumn(1).setPreferredWidth(35);

                Set<MyDirectNode> sharedSuccessors = MyDirectGraphVars.getDirectGraphViewer().multiNodeSharedSuccessorSet;
                i = 0;
                for (MyDirectNode n : sharedSuccessors) {
                    sharedSuccessorTableModel.addRow(new String[]{"" + (++i), n.getName(), n.getCurrentLabel()});
                }

                JScrollPane sharedSuccessorTableScrollPane = new JScrollPane(sharedSuccessorTable);
                sharedSuccessorTableScrollPane.setBackground(Color.WHITE);

                JTextField sharedSuccessorSearchTxt = new JTextField();
                JButton sharedSuccessorSelectBtn = new JButton("SEL.");
                sharedSuccessorSelectBtn.setFont(MyDirectGraphVars.tahomaPlainFont12);
                sharedSuccessorSelectBtn.setFocusable(false);
                sharedSuccessorSearchTxt.setBackground(Color.WHITE);
                sharedSuccessorSearchTxt.setBorder(BorderFactory.createLoweredSoftBevelBorder());
                JPanel sharedSuccessorSearchTxtPanel = MyTableUtil.searchAndSaveDataPanelForJTable2(topLevelNodeLabelBarChart, sharedSuccessorSearchTxt, sharedSuccessorSelectBtn, sharedSuccessorTableModel, sharedSuccessorTable);
                sharedSuccessorSearchTxtPanel.setFont(MyDirectGraphVars.f_bold_12);
                sharedSuccessorSearchTxtPanel.setToolTipText("TYPE A SUCCESSOR NAME TO SEARCH");
                sharedSuccessorSearchTxtPanel.setPreferredSize(new Dimension(90, 19));
                sharedSuccessorSelectBtn.setPreferredSize(new Dimension(54, 19));
                sharedSuccessorSearchTxtPanel.remove(sharedSuccessorSelectBtn);

                JPanel sharedSuccessorTablePanel = new JPanel();
                sharedSuccessorTablePanel.setBackground(Color.WHITE);
                sharedSuccessorTablePanel.setLayout(new BorderLayout(3, 3));
                sharedSuccessorTablePanel.add(sharedSuccessorTableScrollPane, BorderLayout.CENTER);
                sharedSuccessorTablePanel.add(sharedSuccessorSearchTxtPanel, BorderLayout.SOUTH);

                JSplitPane sharePredecessorAndSuccessorSplitPane = new JSplitPane();
                sharePredecessorAndSuccessorSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
                sharePredecessorAndSuccessorSplitPane.setDividerSize(5);
                sharePredecessorAndSuccessorSplitPane.setDividerLocation(MyDirectGraphSysUtil.screenHeight/2);
                sharePredecessorAndSuccessorSplitPane.setTopComponent(sharedPredecessorTablePanel);
                sharePredecessorAndSuccessorSplitPane.setBottomComponent(sharedSuccessorTablePanel);

                JSplitPane contentPane = new JSplitPane();
                contentPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
                contentPane.setLeftComponent(nodeLabelBarChartScrollPane);
                contentPane.setRightComponent(sharePredecessorAndSuccessorSplitPane);
                contentPane.getLeftComponent().setBackground(Color.WHITE);
                contentPane.setDividerSize(5);

                topLevelNodeLabelBarChart.f = new JFrame("SHARED PREDECESSOR & SUCCESSOR NODE LABEL DISTRIBUTION");
                topLevelNodeLabelBarChart.f.setBackground(Color.WHITE);
                topLevelNodeLabelBarChart.f.setLayout(new BorderLayout(3,3));
                topLevelNodeLabelBarChart.f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                topLevelNodeLabelBarChart.f.addMouseListener(new MouseAdapter() {
                    @Override public void mouseEntered(MouseEvent e) {
                        super.mouseEntered(e);
                        topLevelNodeLabelBarChart.f.setAlwaysOnTop(true);
                    }

                    @Override public void mouseExited(MouseEvent e) {
                        super.mouseExited(e);
                        topLevelNodeLabelBarChart.f.setAlwaysOnTop(false);
                    }
                });

                topLevelNodeLabelBarChart.f.addComponentListener(new ComponentAdapter() {
                    @Override public void componentResized(ComponentEvent e) {
                        contentPane.setDividerLocation(0.35);
                    }
                });
                topLevelNodeLabelBarChart.f.setAlwaysOnTop(true);
                topLevelNodeLabelBarChart.f.getContentPane().add(contentPane, BorderLayout.CENTER);
                topLevelNodeLabelBarChart.f.pack();
                topLevelNodeLabelBarChart.f.setSize(new Dimension(800, 750));
                pb.updateValue(100, 100);
                pb.dispose();
                topLevelNodeLabelBarChart.f.setVisible(true);
                topLevelNodeLabelBarChart.f.setAlwaysOnTop(false);
            } catch (Exception ex) {
                pb.updateValue(100, 100);
                pb.dispose();
            }
        } else {
            MyProgressBar pb = new MyProgressBar(false);
            try {
                MyDirectGraphNodeLabelBarChart topLevelNodeLabelBarChart = new MyDirectGraphNodeLabelBarChart();
                topLevelNodeLabelBarChart.decorate();
                topLevelNodeLabelBarChart.setBackground(Color.WHITE);
                topLevelNodeLabelBarChart.setPreferredSize(new Dimension(500, (this.data.size()*10)+200));
                JScrollPane nodeLabelChartScrollPane = new JScrollPane(topLevelNodeLabelBarChart);
                nodeLabelChartScrollPane.setPreferredSize(new Dimension(500, 700));

                String[] nodeTableColumns = {"NO.", "NODE", "L."};
                String[][] nodeTableData = {};
                DefaultTableModel nodeTableModel = new DefaultTableModel(nodeTableData, nodeTableColumns);
                JTable nodeTable = new JTable(nodeTableModel);
                nodeTable.setBackground(Color.WHITE);
                nodeTable.setFont(MyDirectGraphVars.f_pln_12);
                nodeTable.setRowHeight(22);
                nodeTable.getTableHeader().setFont(MyDirectGraphVars.tahomaBoldFont11);
                nodeTable.getTableHeader().setOpaque(false);
                nodeTable.getTableHeader().setBackground(new Color(0, 0, 0, 0));
                nodeTable.getTableHeader().getColumnModel().getColumn(0).setPreferredWidth(35);
                nodeTable.getTableHeader().getColumnModel().getColumn(1).setPreferredWidth(300);
                nodeTable.getTableHeader().getColumnModel().getColumn(1).setPreferredWidth(35);

                int i = 0;
                Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
                for (MyDirectNode n : nodes) {
                    nodeTableModel.addRow(new String[]{"" + (++i), n.getName(), "" + n.getCurrentLabel()});
                }

                JScrollPane nodeTableScrollPane = new JScrollPane(nodeTable);
                nodeTableScrollPane.setBackground(Color.WHITE);

                JTextField nodeSearchTxt = new JTextField();
                JButton nodeSelectBtn = new JButton("SEL.");
                nodeSelectBtn.setFont(MyDirectGraphVars.tahomaPlainFont12);
                nodeSelectBtn.setFocusable(false);
                nodeSearchTxt.setBackground(Color.WHITE);
                nodeSearchTxt.setBorder(BorderFactory.createLoweredSoftBevelBorder());
                JPanel nodeTableSearchPanel = MyTableUtil.searchAndSaveDataPanelForJTable2(topLevelNodeLabelBarChart, nodeSearchTxt, nodeSelectBtn, nodeTableModel, nodeTable);
                nodeSearchTxt.setFont(MyDirectGraphVars.f_bold_12);
                nodeSearchTxt.setToolTipText("TYPE A NODE NAME TO SEARCH");
                nodeSearchTxt.setPreferredSize(new Dimension(90, 19));
                nodeSelectBtn.setPreferredSize(new Dimension(54, 19));
                nodeTableSearchPanel.remove(nodeSelectBtn);

                JPanel nodeTablePanel = new JPanel();
                nodeTablePanel.setBackground(Color.WHITE);
                nodeTablePanel.setLayout(new BorderLayout(3, 3));
                nodeTablePanel.add(nodeTableScrollPane, BorderLayout.CENTER);
                nodeTablePanel.add(nodeTableSearchPanel, BorderLayout.SOUTH);

                JSplitPane contentPane = new JSplitPane();
                contentPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
                contentPane.setLeftComponent(nodeLabelChartScrollPane);
                contentPane.setRightComponent(nodeTablePanel);
                contentPane.getLeftComponent().setBackground(Color.WHITE);
                contentPane.setDividerSize(5);

                topLevelNodeLabelBarChart.f = new JFrame("NODE LABEL DISTRIBUTION");
                topLevelNodeLabelBarChart.f.setBackground(Color.WHITE);
                topLevelNodeLabelBarChart.f.setLayout(new BorderLayout(3,3));
                topLevelNodeLabelBarChart.f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                topLevelNodeLabelBarChart.f.addMouseListener(new MouseAdapter() {
                    @Override public void mouseEntered(MouseEvent e) {
                        super.mouseEntered(e);
                        topLevelNodeLabelBarChart.f.setAlwaysOnTop(true);
                    }

                    @Override public void mouseExited(MouseEvent e) {
                        super.mouseExited(e);
                        topLevelNodeLabelBarChart.f.setAlwaysOnTop(false);
                    }
                });

                topLevelNodeLabelBarChart.f.addComponentListener(new ComponentAdapter() {
                    @Override public void componentResized(ComponentEvent e) {
                        contentPane.setDividerLocation(0.35);
                    }
                });
                topLevelNodeLabelBarChart.f.setAlwaysOnTop(true);
                topLevelNodeLabelBarChart.f.getContentPane().add(contentPane, BorderLayout.CENTER);
                topLevelNodeLabelBarChart.f.pack();
                topLevelNodeLabelBarChart.f.setSize(new Dimension(800, 750));
                pb.updateValue(100, 100);
                pb.dispose();
                topLevelNodeLabelBarChart.f.setVisible(true);
                topLevelNodeLabelBarChart.f.setAlwaysOnTop(false);
            } catch (Exception ex) {
                pb.updateValue(100, 100);
                pb.dispose();
            }
        }
    }

    public void setPredecessorLabelData() {
        if (MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode != null &&
            MyDirectGraphVars.getDirectGraphViewer().selectedSingleNodePredecessorSet.size() > 0) {

            for (MyDirectNode n : MyDirectGraphVars.getDirectGraphViewer().selectedSingleNodePredecessorSet) {

                if (this.predecessorData.containsKey(n.getCurrentLabel())) {

                    this.successorData.put(n.getCurrentLabel(), this.predecessorData.get(n.getCurrentLabel())+1);

                } else {

                    this.predecessorData.put(n.getCurrentLabel(), 1f);
                    final float hue = this.rand.nextFloat();
                    final float saturation = 0.9f;
                    final float luminance = 1.0f;
                    this.colors.add(Color.getHSBColor(hue, saturation, luminance));

                }

                if (this.maxVal < this.predecessorData.get(n.getCurrentLabel())) {
                    this.maxVal = this.predecessorData.get(n.getCurrentLabel());
                }

            }

            this.predecessorData = MyDirectGraphSysUtil.sortMapByFloatValue(this.predecessorData);

        } else if (MyDirectGraphVars.getDirectGraphViewer().multiNodeSharedPredecessorSet != null &&
            MyDirectGraphVars.getDirectGraphViewer().multiNodeSharedPredecessorSet.size() > 0) {

            for (MyDirectNode n : MyDirectGraphVars.getDirectGraphViewer().multiNodeSharedPredecessorSet) {

                if (this.sharedPredecessorData.containsKey(n.getCurrentLabel())) {

                    this.sharedPredecessorData.put(n.getCurrentLabel(), this.sharedPredecessorData.get(n.getCurrentLabel()) + 1);

                } else {

                    this.sharedPredecessorData.put(n.getCurrentLabel(), 1f);
                    final float hue = this.rand.nextFloat();
                    final float saturation = 0.9f;
                    final float luminance = 1.0f;
                    this.colors.add(Color.getHSBColor(hue, saturation, luminance));

                }

                if (this.maxVal < this.sharedPredecessorData.get(n.getCurrentLabel())) {
                    this.maxVal = this.sharedPredecessorData.get(n.getCurrentLabel());
                }

            }

            this.sharedPredecessorData = MyDirectGraphSysUtil.sortMapByFloatValue(this.sharedPredecessorData);
        }

        setBounds(8, 5, 600, 2000);
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0.0f));
    }

    public void setSuccessorLabelData() {
        if (MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode != null &&
            MyDirectGraphVars.getDirectGraphViewer().selectedSingleNodeSuccessorSet.size() > 0) {

            for (MyDirectNode n : MyDirectGraphVars.getDirectGraphViewer().selectedSingleNodeSuccessorSet) {

                if (this.successorData.containsKey(n.getCurrentLabel())) {
                    this.successorData.put(n.getCurrentLabel(), this.successorData.get(n.getCurrentLabel())+1);
                } else {
                    this.successorData.put(n.getCurrentLabel(), 1f);
                    final float hue = this.rand.nextFloat();
                    final float saturation = 0.9f;
                    final float luminance = 1.0f;
                    this.colors.add(Color.getHSBColor(hue, saturation, luminance));
                }

                if (this.maxVal < this.successorData.get(n.getCurrentLabel())) {
                    this.maxVal = this.successorData.get(n.getCurrentLabel());
                }

                final float hue = this.rand.nextFloat();
                final float saturation = 0.9f;
                final float luminance = 1.0f;
                this.colors.add(Color.getHSBColor(hue, saturation, luminance));
            }

            this.successorData = MyDirectGraphSysUtil.sortMapByFloatValue(this.successorData);

        } else if (MyDirectGraphVars.getDirectGraphViewer().multiNodeSharedSuccessorSet != null &&
            MyDirectGraphVars.getDirectGraphViewer().multiNodeSharedSuccessorSet.size() > 0) {

            for (MyDirectNode n : MyDirectGraphVars.getDirectGraphViewer().multiNodeSharedSuccessorSet) {

                if (this.sharedSuccessorData.containsKey(n.getCurrentLabel())) {
                    this.sharedSuccessorData.put(n.getCurrentLabel(), this.sharedSuccessorData.get(n.getCurrentLabel())+1);
                } else {
                    this.sharedSuccessorData.put(n.getCurrentLabel(), 1f);
                    final float hue = this.rand.nextFloat();
                    final float saturation = 0.9f;
                    final float luminance = 1.0f;
                    this.colors.add(Color.getHSBColor(hue, saturation, luminance));
                }

                if (this.maxVal < this.sharedSuccessorData.get(n.getCurrentLabel())) {
                    this.maxVal = this.sharedSuccessorData.get(n.getCurrentLabel());
                }
            }
            this.sharedSuccessorData = MyDirectGraphSysUtil.sortMapByFloatValue(this.sharedSuccessorData);
        }
        setBounds(8, 5, 600, 2000);
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0.0f));
    }

    public void setNodeLabelData() {
        Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
        for (MyDirectNode n : nodes) {
            if (n.getCurrentValue() <= 0) continue;

            if (this.data.containsKey(n.getCurrentLabel())) {
                this.data.put(n.getCurrentLabel(), this.data.get(n.getCurrentLabel())+1);
            } else {
                this.data.put(n.getCurrentLabel(), 1f);
                final float hue = this.rand.nextFloat();
                final float saturation = 0.9f;
                final float luminance = 1.0f;
                this.colors.add(Color.getHSBColor(hue, saturation, luminance));
            }

            if (this.maxVal < this.data.get(n.getCurrentLabel())) {
                this.maxVal = this.data.get(n.getCurrentLabel());
            }
        }
        this.data = MyDirectGraphSysUtil.sortMapByFloatValue(this.data);
        setBounds(8, 5, 600, 2000);
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0.0f));
    }


    @Override public synchronized void paint(Graphics g) {
        if (this.maxVal == 0) return;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

        int i = 0;
        int nodeCnt = 0;
        if (this.predecessorData != null && this.predecessorData.size() > 0) {
            g.setFont(MyDirectGraphVars.tahomaBoldFont12);
            g.setColor(Color.DARK_GRAY);
            g.drawString("PREDECESSOR NODE LABELS", 0, (10 * (i + 1)) + ((i * this.gap)) - 1);
            i++;

            for (String name : this.predecessorData.keySet()) {
                g.setColor(this.colors.get(nodeCnt++));
                float valuePortion = this.predecessorData.get(name) / this.maxVal;
                valuePortion = BAR_WIDTH * valuePortion;
                g.fillRect(0, (10 * i) + (i * this.gap), (int) valuePortion, 10);

                g.setColor(Color.BLUE);
                g.drawRect(0, (10 * i) + (i * this.gap), (int) valuePortion, 10);

                String value = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(this.predecessorData.get(name)));
                value = (value.endsWith(".0") || value.endsWith(".00") ? value.substring(0, value.indexOf(".0")) : value);
                g.setFont(MyDirectGraphVars.f_pln_12);
                g.setColor(Color.DARK_GRAY);
                g.drawString(name + "[" + value + "]", ((int) valuePortion) + 5, (10 * (i + 1)) + ((i * this.gap) - 2));
                i++;
                if (nodeCnt == BAR_LIMIT) { break; }
            }
        }

        if (this.successorData != null && this.successorData.size() > 0) {
            nodeCnt = 0;
            g.setFont(MyDirectGraphVars.tahomaBoldFont12);
            g.setColor(Color.DARK_GRAY);
            g.drawString("", 0, (10 * (i + 1)) + ((i * this.gap)) - 1);
            i++;
            g.drawString("SUCCESSOR NODE LABELS", 0, (10 * (i + 1)) + ((i * this.gap)) - 2);
            i++;
            for (String name : this.successorData.keySet()) {
                g.setColor(this.colors.get(nodeCnt++));
                float valuePortion = this.successorData.get(name) / this.maxVal;
                valuePortion = BAR_WIDTH * valuePortion;
                g.fillRect(0, (10 * i) + (i * this.gap), (int) valuePortion, 9);

                g.setColor(Color.BLUE);
                g.drawRect(0, (10 * i) + (i * this.gap), (int) valuePortion, 9);

                String value = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(this.successorData.get(name)));
                value = (value.endsWith(".0") || value.endsWith(".00") ? value.substring(0, value.indexOf(".0")) : value);
                g.setFont(MyDirectGraphVars.f_pln_12);
                g.setColor(Color.DARK_GRAY);
                g.drawString(name + "[" + value + "]", ((int) valuePortion) + 5, (10 * (i + 1)) + ((i * this.gap) - 2));
                i++;
                if (nodeCnt == BAR_LIMIT) { break; }
            }
        }

        if (this.sharedPredecessorData != null && this.sharedPredecessorData.size() > 0) {
            nodeCnt = 0;
            g.setFont(MyDirectGraphVars.tahomaBoldFont12);
            g.setColor(Color.DARK_GRAY);
            g.drawString("", 0, (10 * (i + 1)) + ((i * this.gap)) - 1);
            i++;
            g.drawString("SHARED PREDECESSOR NODE LABELS", 0, (10 * (i + 1)) + ((i * this.gap)) - 2);
            i++;
            for (String name : this.sharedPredecessorData.keySet()) {
                g.setColor(this.colors.get(nodeCnt++));
                float valuePortion = this.sharedPredecessorData.get(name) / this.maxVal;
                valuePortion = BAR_WIDTH * valuePortion;
                g.fillRect(0, (10 * i) + (i * this.gap), (int) valuePortion, 9);

                g.setColor(Color.BLUE);
                g.drawRect(0, (10 * i) + (i * this.gap), (int) valuePortion, 9);

                String value = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(this.sharedPredecessorData.get(name)));
                value = (value.endsWith(".0") || value.endsWith(".00") ? value.substring(0, value.indexOf(".0")) : value);
                g.setFont(MyDirectGraphVars.f_pln_12);
                g.setColor(Color.DARK_GRAY);
                g.drawString(name + "[" + value + "]", ((int) valuePortion) + 5, (10 * (i + 1)) + ((i * this.gap) - 2));
                i++;
                if (nodeCnt == BAR_LIMIT) { break; }
            }
        }

        if (this.sharedSuccessorData != null && this.sharedSuccessorData.size() > 0) {
            nodeCnt = 0;
            g.setFont(MyDirectGraphVars.tahomaBoldFont12);
            g.setColor(Color.DARK_GRAY);
            g.drawString("", 0, (10 * (i + 1)) + ((i * this.gap)) - 1);
            i++;
            g.drawString("SHARED SUCCESSOR NODE LABELS", 0, (10 * (i + 1)) + ((i * this.gap)) - 2);
            i++;
            for (String name : this.sharedSuccessorData.keySet()) {
                g.setColor(this.colors.get(nodeCnt++));
                float valuePortion = this.sharedSuccessorData.get(name) / this.maxVal;
                valuePortion = BAR_WIDTH * valuePortion;
                g.fillRect(0, (10 * i) + (i * this.gap), (int) valuePortion, 9);

                g.setColor(Color.BLUE);
                g.drawRect(0, (10 * i) + (i * this.gap), (int) valuePortion, 9);

                String value = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(this.sharedSuccessorData.get(name)));
                value = (value.endsWith(".0") || value.endsWith(".00") ? value.substring(0, value.indexOf(".0")) : value);
                g.setFont(MyDirectGraphVars.f_pln_12);
                g.setColor(Color.DARK_GRAY);
                g.drawString(name + "[" + value + "]", ((int) valuePortion) + 5, (10 * (i + 1)) + ((i * this.gap) - 2));
                i++;
                if (nodeCnt == BAR_LIMIT) { break; }
            }
        }



        if (this.data != null && this.data.size() > 0) {
            nodeCnt = 0;
            g.setFont(MyDirectGraphVars.tahomaBoldFont12);
            g.setColor(Color.DARK_GRAY);
            g.drawString("NODE LABELS", 0, (10 * (i + 1)) + ((i * this.gap)) - 1);
            i++;

            for (String name : this.data.keySet()) {
                g.setColor(this.colors.get(nodeCnt++));
                float valuePortion = this.data.get(name) / this.maxVal;
                valuePortion = BAR_WIDTH * valuePortion;
                g.fillRect(0, (10 * i) + (i * this.gap), (int) valuePortion, 10);

                g.setColor(Color.BLUE);
                g.drawRect(0, (10 * i) + (i * this.gap), (int) valuePortion, 10);

                String value = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(this.data.get(name)));
                value = (value.endsWith(".0") || value.endsWith(".00") ? value.substring(0, value.indexOf(".0")) : value);
                g.setFont(MyDirectGraphVars.f_pln_12);
                g.setColor(Color.DARK_GRAY);
                g.drawString(name + "[" + value + "]", ((int) valuePortion) + 5, (10 * (i + 1)) + ((i * this.gap) - 2));
                i++;
                if (nodeCnt == 200) { break; }
            }
        }
    }

    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(this.img ==null) {
            this.img =createImage(getWidth(), getHeight());
            this.graph = this.img.getGraphics();
        }
        this.graph.setColor(getBackground());
        this.graph.fillRect(0, 0, getWidth(), getHeight());
        this.graph.setColor(getForeground());
        paint(this.graph);
        g.drawImage(this.img,0,0,this);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Bar Chart");
        frame.setPreferredSize(new Dimension(500, 200));

        LinkedHashMap<String, Float> entities = new LinkedHashMap<>();
        entities.put("A", 100f);
        entities.put("B", 200f);
        entities.put("C", 120f);
        entities.put("D", 250f);
        entities.put("E", 110f);
        entities.put("F", 90f);
        entities.put("G", 20f);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    @Override public void actionPerformed(ActionEvent e) {}
}