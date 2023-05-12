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
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Random;

public class MyDirectGraphEdgeLabelBarChart
extends JPanel
implements ActionListener {

    private Image img;
    private Graphics graph;
    private final int BAR_WIDTH = 50;
    private final int BAR_LIMIT = 45;
    public LinkedHashMap<String, Float> data;
    public LinkedHashMap<String, Float> predecessorEdgeData;
    public LinkedHashMap<String, Float> successorEdgeData;
    public LinkedHashMap<String, Float> sharedPredecessorEdgeData;
    public LinkedHashMap<String, Float> sharedSuccessorEdgeData;
    private float maxVal = 0.00f;
    private Random rand = new Random();
    private int gap = 2;
    private ArrayList<Color> colors;
    private JFrame f;

    public MyDirectGraphEdgeLabelBarChart() {}

    public synchronized void setEdgeLabelBarChart() {
        decorate();
    }

    public void decorate(){
        this.data = new LinkedHashMap<>();
        this.predecessorEdgeData = new LinkedHashMap<>();
        this.successorEdgeData = new LinkedHashMap<>();
        this.sharedPredecessorEdgeData = new LinkedHashMap<>();
        this.sharedSuccessorEdgeData = new LinkedHashMap<>();
        this.colors = new ArrayList<>();
        this.maxVal = 0;

        if (MyDirectGraphVars.getDirectGraphViewer().vc.nodeLabelComboBoxMenu.getSelectedIndex() > 0) {
            if (MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode != null || MyDirectGraphVars.getDirectGraphViewer().multiNodes != null) {
                this.setPredecessorEdgeLabelData();
                this.setSuccessorEdgeLabelData();
            } else {
                this.setEdgeLabelData();
            }
            this.revalidate();
            this.repaint();
        }
    }

    private void setSuccessorEdgeLabelData() {
        if (MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode != null &&
                MyDirectGraphVars.getDirectGraphViewer().selectedSingleNodePredecessorSet.size() > 0) {
            Collection<MyDirectEdge> edges = MyDirectGraphVars.directGraph.getEdges();
            for (MyDirectEdge e : edges) {

                if (e.getCurrentValue() <= 0 || e.getDest() != MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode) continue;

                if (this.successorEdgeData.containsKey(e.getCurrentLabel())) {
                    this.successorEdgeData.put(e.getCurrentLabel(), this.data.get(e.getCurrentLabel()) + 1);
                } else {
                    this.successorEdgeData.put(e.getCurrentLabel(), 1f);
                    final float hue = this.rand.nextFloat();
                    final float saturation = 0.9f;
                    final float luminance = 1.0f;
                    this.colors.add(Color.getHSBColor(hue, saturation, luminance));
                }

                if (this.maxVal < this.successorEdgeData.get(e.getCurrentLabel())) {
                    this.maxVal = this.successorEdgeData.get(e.getCurrentLabel());
                }
            }
            this.successorEdgeData = MyDirectGraphSysUtil.sortMapByFloatValue(this.successorEdgeData);
        } else if (MyDirectGraphVars.getDirectGraphViewer().multiNodeSharedSuccessorSet != null &&
                MyDirectGraphVars.getDirectGraphViewer().multiNodeSharedSuccessorSet.size() > 0) {

            Collection<MyDirectEdge> edges = MyDirectGraphVars.directGraph.getEdges();
            for (MyDirectEdge e : edges) {
                if (e.getCurrentValue() <= 0 || !MyDirectGraphVars.getDirectGraphViewer().multiNodes.contains(e.getDest())) continue;

                if (this.sharedSuccessorEdgeData.containsKey(e.getCurrentLabel())) {
                    this.sharedSuccessorEdgeData.put(e.getCurrentLabel(), this.sharedSuccessorEdgeData.get(e.getCurrentLabel()) + 1);
                } else {
                    this.sharedSuccessorEdgeData.put(e.getCurrentLabel(), 1f);
                    final float hue = this.rand.nextFloat();
                    final float saturation = 0.9f;
                    final float luminance = 1.0f;
                    this.colors.add(Color.getHSBColor(hue, saturation, luminance));
                }

                if (this.maxVal < this.sharedSuccessorEdgeData.get(e.getCurrentLabel())) {
                    this.maxVal = this.sharedSuccessorEdgeData.get(e.getCurrentLabel());
                }
            }
            this.sharedSuccessorEdgeData = MyDirectGraphSysUtil.sortMapByFloatValue(this.sharedSuccessorEdgeData);
        }
        setBounds(8, 5, 600, 2000);
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0.0f));
    }


    private void setPredecessorEdgeLabelData() {
        if (MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode != null &&
            MyDirectGraphVars.getDirectGraphViewer().selectedSingleNodePredecessorSet.size() > 0) {
            Collection<MyDirectEdge> edges = MyDirectGraphVars.directGraph.getEdges();
            for (MyDirectEdge e : edges) {

                if (e.getCurrentValue() <= 0 || e.getDest() != MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode) continue;

                if (this.predecessorEdgeData.containsKey(e.getCurrentLabel())) {
                    this.predecessorEdgeData.put(e.getCurrentLabel(), this.data.get(e.getCurrentLabel()) + 1);
                } else {
                    this.predecessorEdgeData.put(e.getCurrentLabel(), 1f);
                    final float hue = this.rand.nextFloat();
                    final float saturation = 0.9f;
                    final float luminance = 1.0f;
                    this.colors.add(Color.getHSBColor(hue, saturation, luminance));
                }

                if (this.maxVal < this.predecessorEdgeData.get(e.getCurrentLabel())) {
                    this.maxVal = this.predecessorEdgeData.get(e.getCurrentLabel());
                }
            }
            this.predecessorEdgeData = MyDirectGraphSysUtil.sortMapByFloatValue(this.predecessorEdgeData);
        } else if (MyDirectGraphVars.getDirectGraphViewer().multiNodeSharedPredecessorSet != null &&
            MyDirectGraphVars.getDirectGraphViewer().multiNodeSharedPredecessorSet.size() > 0) {

            Collection<MyDirectEdge> edges = MyDirectGraphVars.directGraph.getEdges();
            for (MyDirectEdge e : edges) {
                if (e.getCurrentValue() <= 0 || !MyDirectGraphVars.getDirectGraphViewer().multiNodes.contains(e.getDest())) continue;

                if (this.sharedPredecessorEdgeData.containsKey(e.getCurrentLabel())) {
                    this.sharedPredecessorEdgeData.put(e.getCurrentLabel(), this.sharedPredecessorEdgeData.get(e.getCurrentLabel()) + 1);
                } else {
                    this.sharedPredecessorEdgeData.put(e.getCurrentLabel(), 1f);
                    final float hue = this.rand.nextFloat();
                    final float saturation = 0.9f;
                    final float luminance = 1.0f;
                    this.colors.add(Color.getHSBColor(hue, saturation, luminance));
                }

                if (this.maxVal < this.sharedPredecessorEdgeData.get(e.getCurrentLabel())) {
                    this.maxVal = this.sharedPredecessorEdgeData.get(e.getCurrentLabel());
                }
            }
            this.sharedPredecessorEdgeData = MyDirectGraphSysUtil.sortMapByFloatValue(this.sharedPredecessorEdgeData);
        }
        setBounds(8, 5, 600, 2000);
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0.0f));
    }

    private void setEdgeLabelData() {
        Collection<MyDirectEdge> edges = MyDirectGraphVars.directGraph.getEdges();
        for (MyDirectEdge e : edges) {
            if (e.getCurrentValue() <= 0) continue;

            if (this.data.containsKey(e.getCurrentLabel())) {
                this.data.put(e.getCurrentLabel(), this.data.get(e.getCurrentLabel()) + 1);
            } else {
                this.data.put(e.getCurrentLabel(), 1f);

                final float hue = this.rand.nextFloat();
                final float saturation = 0.9f;
                final float luminance = 1.0f;
                this.colors.add(Color.getHSBColor(hue, saturation, luminance));
            }

            if (maxVal < this.data.get(e.getCurrentLabel())) {
                maxVal = this.data.get(e.getCurrentLabel());
            }
        }
        this.data = MyDirectGraphSysUtil.sortMapByFloatValue(this.data);

        setBounds(8, 5, 600, 2000);
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0.0f));
    }


    public void enlarge() {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            MyDirectGraphEdgeLabelBarChart topLevelEdgeLabelBarChart = new MyDirectGraphEdgeLabelBarChart();
            topLevelEdgeLabelBarChart.setEdgeLabelBarChart();
            topLevelEdgeLabelBarChart.setPreferredSize(new Dimension(500, (this.data.size()*10)+200));
            JScrollPane nodeLabelChartScrollPane = new JScrollPane(topLevelEdgeLabelBarChart);
            nodeLabelChartScrollPane.setPreferredSize(new Dimension(500, 700));

            String[] edgeTableColumns = {"NO.", "SOURCE", "DEST", "L."};
            String[][] edgeTableData = {};
            DefaultTableModel edgeTableModel = new DefaultTableModel(edgeTableData, edgeTableColumns);
            JTable edgeTable = new JTable(edgeTableModel);
            edgeTable.setBackground(Color.WHITE);
            edgeTable.setFont(MyDirectGraphVars.f_pln_12);
            edgeTable.setRowHeight(22);
            edgeTable.getTableHeader().setFont(MyDirectGraphVars.tahomaBoldFont11);
            edgeTable.getTableHeader().setOpaque(false);
            edgeTable.getTableHeader().setBackground(new Color(0, 0, 0, 0));
            edgeTable.getTableHeader().getColumnModel().getColumn(0).setPreferredWidth(35);
            edgeTable.getTableHeader().getColumnModel().getColumn(1).setPreferredWidth(150);
            edgeTable.getTableHeader().getColumnModel().getColumn(2).setPreferredWidth(150);
            edgeTable.getTableHeader().getColumnModel().getColumn(3).setPreferredWidth(35);

            int i = 0;
            Collection<MyDirectEdge> edges = MyDirectGraphVars.directGraph.getEdges();
            for (MyDirectEdge e : edges) {
                edgeTableModel.addRow(new String[]{"" + (++i), e.getSource().getName(), e.getDest().getName(), e.getCurrentLabel()});
            }

            JScrollPane nodeTableScrollPane = new JScrollPane(edgeTable);
            nodeTableScrollPane.setBackground(Color.WHITE);

            JTextField nodeSearchTxt = new JTextField();
            JButton nodeSelectBtn = new JButton("SEL.");
            nodeSelectBtn.setFont(MyDirectGraphVars.tahomaPlainFont12);
            nodeSelectBtn.setFocusable(false);
            nodeSearchTxt.setBackground(Color.WHITE);
            nodeSearchTxt.setBorder(BorderFactory.createLoweredSoftBevelBorder());
            JPanel nodeTableSearchPanel = MyTableUtil.searchAndSaveDataPanelForJTable2(topLevelEdgeLabelBarChart, nodeSearchTxt, nodeSelectBtn, edgeTableModel, edgeTable);
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

            topLevelEdgeLabelBarChart.f = new JFrame("NODE LABEL DISTRIBUTION");
            topLevelEdgeLabelBarChart.f.setBackground(Color.WHITE);
            topLevelEdgeLabelBarChart.f.setLayout(new BorderLayout(3,3));
            topLevelEdgeLabelBarChart.f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            topLevelEdgeLabelBarChart.f.addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    topLevelEdgeLabelBarChart.f.setAlwaysOnTop(true);
                }

                @Override public void mouseExited(MouseEvent e) {
                    super.mouseExited(e);
                    topLevelEdgeLabelBarChart.f.setAlwaysOnTop(false);
                }
            });

            topLevelEdgeLabelBarChart.f.addComponentListener(new ComponentAdapter() {
                @Override public void componentResized(ComponentEvent e) {
                    contentPane.setDividerLocation(0.35);
                }
            });
            topLevelEdgeLabelBarChart.f.setAlwaysOnTop(true);
            topLevelEdgeLabelBarChart.f.getContentPane().add(contentPane, BorderLayout.CENTER);
            topLevelEdgeLabelBarChart.f.pack();
            topLevelEdgeLabelBarChart.f.setSize(new Dimension(800, 750));
            pb.updateValue(100, 100);
            pb.dispose();
            topLevelEdgeLabelBarChart.f.setVisible(true);
            topLevelEdgeLabelBarChart.f.setAlwaysOnTop(false);
        } catch (Exception ex) {
            pb.updateValue(100, 100);
            pb.dispose();
        }
    }

    @Override public synchronized void paint(Graphics g) {
        if (this.maxVal == 0) return;

        int i = 0;
        if (this.data != null && this.data.size() > 0) {
            int nodeCnt = 0;
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

            g.setFont(MyDirectGraphVars.tahomaBoldFont12);
            g.setColor(Color.DARK_GRAY);
            g.drawString("EDGE LABELS", 0, ((10 * (i + 1)) + ((i * this.gap)) - 1) + 7);
            i++;

            for (String name : this.data.keySet()) {
                g.setColor(this.colors.get(nodeCnt++));
                float valuePortion = this.data.get(name) / this.maxVal;
                valuePortion = BAR_WIDTH * valuePortion;
                g.fillRect(0, ((10 * i) + (i * this.gap)) + 7, (int) valuePortion, 10);

                g.setColor(Color.BLUE);
                g.drawRect(0, ((10 * i) + (i * this.gap)) + 7, (int) valuePortion, 10);

                String value = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(this.data.get(name)));
                value = (value.endsWith(".0") || value.endsWith(".00") ? value.substring(0, value.indexOf(".0")) : value);
                g.setFont(MyDirectGraphVars.f_pln_12);
                g.setColor(Color.DARK_GRAY);
                g.drawString(name + "[" + value + "]", ((int) valuePortion) + 5, ((10 * (i + 1)) + ((i * this.gap) - 2)) + 7);
                i++;
                if (nodeCnt == BAR_LIMIT) {
                    break;
                }
            }
        }

        if (this.predecessorEdgeData != null && this.predecessorEdgeData.size() > 0) {
            int nodeCnt = 0;
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

            g.setFont(MyDirectGraphVars.tahomaBoldFont12);
            g.setColor(Color.DARK_GRAY);
            g.drawString("PREDECESSOR EDGE LABELS", 0, ((10 * (i + 1)) + ((i * this.gap)) - 1) + 7);
            i++;

            for (String name : this.predecessorEdgeData.keySet()) {
                g.setColor(this.colors.get(nodeCnt++));
                float valuePortion = this.predecessorEdgeData.get(name) / this.maxVal;
                valuePortion = BAR_WIDTH * valuePortion;
                g.fillRect(0, ((10 * i) + (i * this.gap)) + 7, (int) valuePortion, 10);

                g.setColor(Color.BLUE);
                g.drawRect(0, ((10 * i) + (i * this.gap)) + 7, (int) valuePortion, 10);

                String value = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(this.predecessorEdgeData.get(name)));
                value = (value.endsWith(".0") || value.endsWith(".00") ? value.substring(0, value.indexOf(".0")) : value);
                g.setFont(MyDirectGraphVars.f_pln_12);
                g.setColor(Color.DARK_GRAY);
                g.drawString(name + "[" + value + "]", ((int) valuePortion) + 5, ((10 * (i + 1)) + ((i * this.gap) - 2)) + 7);
                i++;
                if (nodeCnt == BAR_LIMIT) {
                    break;
                }
            }
        }

        if (this.successorEdgeData != null && this.successorEdgeData.size() > 0) {
            int nodeCnt = 0;
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

            g.setFont(MyDirectGraphVars.tahomaBoldFont12);
            g.setColor(Color.DARK_GRAY);
            g.drawString("SUCCESSOR EDGE LABELS", 0, ((10 * (i + 1)) + ((i * this.gap)) - 1) + 7);
            i++;

            for (String name : this.successorEdgeData.keySet()) {
                g.setColor(this.colors.get(nodeCnt++));
                float valuePortion = this.successorEdgeData.get(name) / this.maxVal;
                valuePortion = BAR_WIDTH * valuePortion;
                g.fillRect(0, ((10 * i) + (i * this.gap)) + 7, (int) valuePortion, 10);

                g.setColor(Color.BLUE);
                g.drawRect(0, ((10 * i) + (i * this.gap)) + 7, (int) valuePortion, 10);

                String value = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(this.successorEdgeData.get(name)));
                value = (value.endsWith(".0") || value.endsWith(".00") ? value.substring(0, value.indexOf(".0")) : value);
                g.setFont(MyDirectGraphVars.f_pln_12);
                g.setColor(Color.DARK_GRAY);
                g.drawString(name + "[" + value + "]", ((int) valuePortion) + 5, ((10 * (i + 1)) + ((i * this.gap) - 2)) + 7);
                i++;
                if (nodeCnt == BAR_LIMIT) {
                    break;
                }
            }
        }

        if (this.sharedPredecessorEdgeData != null && this.sharedPredecessorEdgeData.size() > 0) {
            int nodeCnt = 0;
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

            g.setFont(MyDirectGraphVars.tahomaBoldFont12);
            g.setColor(Color.DARK_GRAY);
            g.drawString("SHARED PREDECESSOR EDGE LABELS", 0, ((10 * (i + 1)) + ((i * this.gap)) - 1) + 7);
            i++;

            for (String name : this.sharedPredecessorEdgeData.keySet()) {
                g.setColor(this.colors.get(nodeCnt++));
                float valuePortion = this.sharedPredecessorEdgeData.get(name) / this.maxVal;
                valuePortion = BAR_WIDTH * valuePortion;
                g.fillRect(0, ((10 * i) + (i * this.gap)) + 7, (int) valuePortion, 10);

                g.setColor(Color.BLUE);
                g.drawRect(0, ((10 * i) + (i * this.gap)) + 7, (int) valuePortion, 10);

                String value = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(this.sharedPredecessorEdgeData.get(name)));
                value = (value.endsWith(".0") || value.endsWith(".00") ? value.substring(0, value.indexOf(".0")) : value);
                g.setFont(MyDirectGraphVars.f_pln_12);
                g.setColor(Color.DARK_GRAY);
                g.drawString(name + "[" + value + "]", ((int) valuePortion) + 5, ((10 * (i + 1)) + ((i * this.gap) - 2)) + 7);
                i++;
                if (nodeCnt == BAR_LIMIT) {
                    break;
                }
            }
        }

        if (this.sharedSuccessorEdgeData != null && this.sharedSuccessorEdgeData.size() > 0) {
            int nodeCnt = 0;
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

            g.setFont(MyDirectGraphVars.tahomaBoldFont12);
            g.setColor(Color.DARK_GRAY);
            g.drawString("SHRED SUCCESSOR EDGE LABELS", 0, ((10 * (i + 1)) + ((i * this.gap)) - 1) + 7);
            i++;

            for (String name : this.sharedSuccessorEdgeData.keySet()) {
                g.setColor(this.colors.get(nodeCnt++));
                float valuePortion = this.sharedSuccessorEdgeData.get(name) / this.maxVal;
                valuePortion = BAR_WIDTH * valuePortion;
                g.fillRect(0, ((10 * i) + (i * this.gap)) + 7, (int) valuePortion, 10);

                g.setColor(Color.BLUE);
                g.drawRect(0, ((10 * i) + (i * this.gap)) + 7, (int) valuePortion, 10);

                String value = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(this.sharedSuccessorEdgeData.get(name)));
                value = (value.endsWith(".0") || value.endsWith(".00") ? value.substring(0, value.indexOf(".0")) : value);
                g.setFont(MyDirectGraphVars.f_pln_12);
                g.setColor(Color.DARK_GRAY);
                g.drawString(name + "[" + value + "]", ((int) valuePortion) + 5, ((10 * (i + 1)) + ((i * this.gap) - 2)) + 7);
                i++;
                if (nodeCnt == BAR_LIMIT) {
                    break;
                }
            }
        }
    }

    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(this.img ==null) {
            this.img =createImage(getWidth(), getHeight());
            this.graph = this.img.getGraphics();
        }
        this.graph.setColor(Color.WHITE);
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

    @Override public void actionPerformed(ActionEvent e) {

    }
}