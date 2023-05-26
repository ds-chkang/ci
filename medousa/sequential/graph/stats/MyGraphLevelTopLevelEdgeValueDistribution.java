package medousa.sequential.graph.stats;

import medousa.MyProgressBar;
import medousa.direct.utils.MyDirectGraphVars;
import medousa.sequential.graph.MyEdge;
import medousa.sequential.utils.MyMathUtil;
import medousa.sequential.utils.MySequentialGraphSysUtil;
import medousa.sequential.utils.MySequentialGraphVars;
import medousa.table.MyTableToolTipper;
import medousa.table.MyTableUtil;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class MyGraphLevelTopLevelEdgeValueDistribution
extends JPanel
implements ActionListener {

    protected boolean MAXIMIZED;
    private float maxValue = 0;
    private float minValue = 1000000000;
    private float avgValue = 0;
    private float stdValue = 0;

    public MyGraphLevelTopLevelEdgeValueDistribution() {

    }

    public void decorate() {
        setLayout(new BorderLayout(3, 3));
        setBackground(Color.WHITE);

        ChartPanel chartPanel = new ChartPanel(setValueChart());
        chartPanel.getChart().getCategoryPlot().setRangeGridlinePaint(Color.DARK_GRAY);
        chartPanel.getChart().getCategoryPlot().setDomainGridlinePaint(Color.DARK_GRAY);
        chartPanel.getChart().getCategoryPlot().setBackgroundPaint(Color.WHITE);
        chartPanel.getChart().getCategoryPlot().getDomainAxis().setTickLabelFont(MyDirectGraphVars.tahomaPlainFont10);
        chartPanel.getChart().getCategoryPlot().getDomainAxis().setLabelFont(MyDirectGraphVars.tahomaPlainFont10);
        chartPanel.getChart().getCategoryPlot().getRangeAxis().setTickLabelFont(MyDirectGraphVars.tahomaPlainFont10);
        chartPanel.getChart().getCategoryPlot().getRangeAxis().setLabelFont(MyDirectGraphVars.tahomaPlainFont10);

        CategoryAxis domainAxis = chartPanel.getChart().getCategoryPlot().getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);

        BarRenderer barRenderer = (BarRenderer) chartPanel.getChart().getCategoryPlot().getRenderer();
        barRenderer.setSeriesPaint(0, new Color(0, 0, 0, 0.25f));//Color.LIGHT_GRAY);//Color.decode("#2084FE"));
        barRenderer.setShadowPaint(Color.WHITE);
        barRenderer.setBaseFillPaint(Color.decode("#07CF61"));
        barRenderer.setBarPainter(new StandardBarPainter());
        barRenderer.setBaseLegendTextFont(MyDirectGraphVars.tahomaPlainFont11);

        add(chartPanel, BorderLayout.CENTER);
    }

    public void enlarge() {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            this.decorate();

            String[] edgeStatTableColumns = {"PROPERTY", "VALUE"};
            String[][] edgeStatTableData = {
                    {"EDGES", MyMathUtil.getCommaSeperatedNumber(this.edges.size())},
                    {"MAX. VALUE", MyMathUtil.twoDecimalFormat(this.maxValue)},
                    {"NIN. VALUE", MyMathUtil.twoDecimalFormat(this.minValue)},
                    {"AVG. VALUE", MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(this.avgValue))},
                    {"STD. VALUE", MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(this.stdValue))}
            };

            DefaultTableModel edgeStatTableModel = new DefaultTableModel(edgeStatTableData, edgeStatTableColumns);
            JTable edgeStatTable = new JTable(edgeStatTableModel);
            edgeStatTable.setBackground(Color.WHITE);
            edgeStatTable.setFont(MySequentialGraphVars.tahomaPlainFont12);
            edgeStatTable.setRowHeight(22);
            edgeStatTable.getTableHeader().setFont(MySequentialGraphVars.tahomaBoldFont11);
            edgeStatTable.getTableHeader().setOpaque(false);
            edgeStatTable.getTableHeader().setBackground(new Color(0, 0, 0, 0));
            edgeStatTable.getTableHeader().getColumnModel().getColumn(0).setPreferredWidth(100);

            JScrollPane edgeStatTableScrollPane = new JScrollPane(edgeStatTable);
            edgeStatTableScrollPane.setBackground(Color.WHITE);

            String[] edgeTableColumns = {"NO.", "S.", "D.", "V.", "V. R."};
            String[][] edgeTableData = {};

            DefaultTableModel edgeTableModel = new DefaultTableModel(edgeTableData, edgeTableColumns);
            JTable edgeTable = new JTable(edgeTableModel);
            edgeTable.setBackground(Color.WHITE);
            edgeTable.setFont(MySequentialGraphVars.f_pln_12);
            edgeTable.setRowHeight(22);
            edgeTable.getTableHeader().setFont(MySequentialGraphVars.tahomaBoldFont11);
            edgeTable.getTableHeader().setOpaque(false);
            edgeTable.getTableHeader().setBackground(new Color(0, 0, 0, 0));
            edgeTable.setSelectionForeground(Color.BLACK);
            edgeTable.setSelectionBackground(Color.LIGHT_GRAY);
            edgeTable.getTableHeader().getColumnModel().getColumn(0).setPreferredWidth(35);
            edgeTable.getTableHeader().getColumnModel().getColumn(3).setPreferredWidth(35);
            edgeTable.getTableHeader().getColumnModel().getColumn(4).setPreferredWidth(35);

            String[] edgeTableColumnTooltips = {"NO.", "SOURCE", "DEST", "CURRENT EDGE VALUE", "CURRENT EDGE VALUE RATIO AGAINST MAX. EDGE VALUE"};
            MyTableToolTipper edgeTooltipHeader = new MyTableToolTipper(edgeTable.getColumnModel());
            edgeTooltipHeader.setToolTipStrings(edgeTableColumnTooltips);
            edgeTable.setTableHeader(edgeTooltipHeader);

            int i = 0;
            LinkedHashMap<String, Float> valueMap = new LinkedHashMap();
            for (MyEdge e : this.edges) {
                String edgeName = e.getSource().getName() + "-" + e.getDest().getName();
                valueMap.put(edgeName, e.getCurrentValue());
            }
            valueMap = MySequentialGraphSysUtil.sortMapByFloatValue(valueMap);

            for (String e : valueMap.keySet()) {
                String pr = MyMathUtil.twoDecimalFormat((double) valueMap.get(e) / this.maxValue);
                String[] nodes = e.split("-");
                edgeTableModel.addRow(new String[]{
                        "" + (++i),
                        MySequentialGraphSysUtil.getNodeName(nodes[0]),
                        MySequentialGraphSysUtil.getNodeName(nodes[1]),
                        MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(valueMap.get(e))),
                        pr});
            }

            JScrollPane nodeTableScrollPane = new JScrollPane(edgeTable);
            nodeTableScrollPane.setBackground(Color.WHITE);

            JTextField edgeSearchTxt = new JTextField();
            JButton edgeSelectBtn = new JButton("SEL.");
            edgeSelectBtn.setFont(MyDirectGraphVars.tahomaPlainFont12);
            edgeSelectBtn.setFocusable(false);
            edgeSearchTxt.setBackground(Color.WHITE);
            edgeSearchTxt.setBorder(BorderFactory.createLoweredSoftBevelBorder());
            JPanel edgeTableSearchPanel = MyTableUtil.searchAndSaveDataPanelForJTable2(this, edgeSearchTxt, edgeSelectBtn, edgeTableModel, edgeTable);
            edgeSearchTxt.setFont(MyDirectGraphVars.f_bold_12);
            edgeSearchTxt.setToolTipText("TYPE AN EDGE NAME TO SEARCH");
            edgeSearchTxt.setPreferredSize(new Dimension(90, 19));
            edgeSelectBtn.setPreferredSize(new Dimension(54, 19));
            edgeTableSearchPanel.remove(edgeSelectBtn);

            JPanel edgeTablePanel = new JPanel();
            edgeTablePanel.setBackground(Color.WHITE);
            edgeTablePanel.setLayout(new BorderLayout(3, 3));
            edgeTablePanel.add(nodeTableScrollPane, BorderLayout.CENTER);
            edgeTablePanel.add(edgeTableSearchPanel, BorderLayout.SOUTH);

            JFrame f = new JFrame(" EDGE VALUE DISTRIBUTION");
            f.setBackground(Color.WHITE);
            f.setPreferredSize(new Dimension(550, 450));
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JSplitPane tableSplitPane = new JSplitPane();
            tableSplitPane.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            tableSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
            tableSplitPane.setTopComponent(edgeStatTableScrollPane);
            tableSplitPane.setBottomComponent(edgeTablePanel);
            tableSplitPane.getRightComponent().setBackground(Color.WHITE);
            tableSplitPane.setDividerSize(5);

            JSplitPane contentPane = new JSplitPane();
            contentPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
            contentPane.setLeftComponent(this);
            contentPane.setRightComponent(tableSplitPane);
            contentPane.getRightComponent().setBackground(Color.WHITE);
            contentPane.setDividerSize(5);

            f.setLayout(new BorderLayout(3, 3));
            f.getContentPane().add(contentPane, BorderLayout.CENTER);
            f.pack();
            f.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    super.componentResized(e);
                    contentPane.setDividerLocation(0.81);
                    tableSplitPane.setDividerLocation(0.20);
                }
            });
            pb.updateValue(100, 100);
            pb.dispose();
            f.setVisible(true);
        } catch (Exception ex) {
            pb.updateValue(100, 100);
            pb.dispose();
        }
    }

    private Set<MyEdge> edges = new HashSet<>();

    private JFreeChart setValueChart() {
        int edgeCount = 0;
        float totalValue = 0;

        TreeMap<Integer, Integer> valueMap = new TreeMap<>();
        if (MySequentialGraphVars.getSequentialGraphViewer().selectedNode != null) {
            if (MySequentialGraphVars.getSequentialGraphViewer().predecessorsOnly) {
                this.edges = new HashSet<>(MySequentialGraphVars.g.getEdges());
                for (MyEdge e : this.edges) {
                    if (!MySequentialGraphVars.getSequentialGraphViewer().selectedSingleNodePredecessors.contains(e.getSource())) {
                         this.edges.remove(e);
                    }
                }
            } else if (MySequentialGraphVars.getSequentialGraphViewer().successorsOnly) {
                this.edges = new HashSet<>(MySequentialGraphVars.g.getEdges());
                for (MyEdge e : this.edges) {
                    if (!MySequentialGraphVars.getSequentialGraphViewer().selectedSingleNodeSuccessors.contains(e.getDest())) {
                        this.edges.remove(e);
                    }
                }
            } else if (MySequentialGraphVars.getSequentialGraphViewer().neighborsOnly) {
                this.edges = new HashSet<>(MySequentialGraphVars.g.getEdges());
                for (MyEdge e : this.edges) {
                    if (!MySequentialGraphVars.getSequentialGraphViewer().selectedSingleNodeSuccessors.contains(e.getDest()) &&
                        !MySequentialGraphVars.getSequentialGraphViewer().selectedSingleNodePredecessors.contains(e.getSource())) {
                        this.edges.remove(e);
                    }
                }
            }
        } else if (MySequentialGraphVars.getSequentialGraphViewer().multiNodes != null && MySequentialGraphVars.getSequentialGraphViewer().multiNodes.size() > 0) {
            if (MySequentialGraphVars.getSequentialGraphViewer().predecessorsOnly) {
                this.edges = new HashSet<>(MySequentialGraphVars.g.getEdges());
                for (MyEdge e : this.edges) {
                    if (!MySequentialGraphVars.getSequentialGraphViewer().multiNodePredecessors.contains(e.getSource())) {
                        this.edges.remove(e);
                    }
                }
            } else if (MySequentialGraphVars.getSequentialGraphViewer().successorsOnly) {
                this.edges = new HashSet<>(MySequentialGraphVars.g.getEdges());
                for (MyEdge e : this.edges) {
                    if (!MySequentialGraphVars.getSequentialGraphViewer().multiNodeSuccessors.contains(e.getDest())) {
                        this.edges.remove(e);
                    }
                }
            } else if (MySequentialGraphVars.getSequentialGraphViewer().neighborsOnly) {
                this.edges = new HashSet<>(MySequentialGraphVars.g.getEdges());
                for (MyEdge e : this.edges) {
                    if (!MySequentialGraphVars.getSequentialGraphViewer().multiNodeSuccessors.contains(e.getDest()) &&
                        !MySequentialGraphVars.getSequentialGraphViewer().multiNodePredecessors.contains(e.getSource())) {
                        this.edges.remove(e);
                    }
                }
            } else if (MySequentialGraphVars.getSequentialGraphViewer().sharedNeighborsOnly) {
                this.edges = new HashSet<>(MySequentialGraphVars.g.getEdges());
                for (MyEdge e : this.edges) {
                    if (!MySequentialGraphVars.getSequentialGraphViewer().sharedSuccessors.contains(e.getDest()) &&
                        !MySequentialGraphVars.getSequentialGraphViewer().sharedPredecessors.contains(e.getSource())) {
                        this.edges.remove(e);
                    }
                }
            } else if (MySequentialGraphVars.getSequentialGraphViewer().sharedPredecessorsOly) {
                this.edges = new HashSet<>(MySequentialGraphVars.g.getEdges());
                for (MyEdge e : edges) {
                    if (!MySequentialGraphVars.getSequentialGraphViewer().sharedPredecessors.contains(e.getSource())) {
                        this.edges.remove(e);
                    }
                }
            } else if (MySequentialGraphVars.getSequentialGraphViewer().sharedSuccessorsOnly) {
                this.edges = new HashSet<>(MySequentialGraphVars.g.getEdges());
                for (MyEdge e : edges) {
                    if (!MySequentialGraphVars.getSequentialGraphViewer().sharedSuccessors.contains(e.getDest())) {
                        this.edges.remove(e);
                    }
                }
            }
        } else {
            this.edges = new HashSet<>(MySequentialGraphVars.g.getEdges());
        }
        for (MyEdge e : this.edges) {
            if (e.getCurrentValue() == 0) continue;
            int value = (int) e.getCurrentValue();
            totalValue += value;
            edgeCount++;

            if (value > this.maxValue) {
                this.maxValue = value;
            }

            if (value > 0 && value < this.minValue) {
                this.minValue = value;
            }

            if (valueMap.containsKey(value)) {
                valueMap.put(value, valueMap.get(value) + 1);
            } else {
                valueMap.put(value, 1);
            }
        }

        if (edgeCount == 0) {
            this.minValue = 0;
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Integer edgeValue : valueMap.keySet()) {
            dataset.addValue(valueMap.get(edgeValue), "", edgeValue);
        }

        this.avgValue = totalValue/edgeCount;
        this.stdValue = getEdgeValueStandardDeviation(valueMap);

        String plotTitle = "";
        String xaxis = "EDGE VALUE";
        String yaxis = "";
        PlotOrientation orientation = PlotOrientation.VERTICAL;
        boolean show = false;
        boolean toolTips = true;
        boolean urls = false;

        return ChartFactory.createBarChart(plotTitle, xaxis, yaxis, dataset, orientation, show, toolTips, urls);
    }

    public float getEdgeValueStandardDeviation(TreeMap<Integer, Integer> valueMap) {
        float sum = 0.00f;
        for (int n : valueMap.keySet()) {
            sum += n;
        }
        double mean = sum / valueMap.size();
        sum = 0f;
        for (int n : valueMap.keySet()) {
            sum += Math.pow(n - mean, 2);
        }
        return (sum == 0.00f ? 0.00f : (float) Math.sqrt(sum / valueMap.size()));
    }

    @Override public void actionPerformed(ActionEvent e) {

    }
}
