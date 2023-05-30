package medousa.sequential.graph.stats;

import medousa.MyProgressBar;
import medousa.direct.utils.MyDirectGraphVars;
import medousa.sequential.graph.MyNode;
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.TreeMap;

public class MyGraphLevelTopLevelNodeTimeValueDistribution
extends JPanel
implements ActionListener {

    protected boolean MAXIMIZED;
    private float maxValue = 0;
    private float minValue = 1000000000;
    private float avgValue = 0;
    private float stdValue = 0;
    private Set<MyNode> nodes = null;
    private float toTime = 0f;
    private int selectedMenu = 0;
    private JTable nodeTable;

    public MyGraphLevelTopLevelNodeTimeValueDistribution() {}

    public void decorate() {
        removeAll();
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

        JComboBox timeConvertMenu = new JComboBox();
        timeConvertMenu.addItem("SECOND");
        timeConvertMenu.addItem("MINUTE");
        timeConvertMenu.addItem("HOUR");
        timeConvertMenu.setSelectedIndex(selectedMenu);
        timeConvertMenu.setFont(MySequentialGraphVars.tahomaPlainFont12);
        timeConvertMenu.setFocusable(false);
        timeConvertMenu.setBackground(Color.WHITE);
        timeConvertMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        if (timeConvertMenu.getSelectedIndex() == 0) {
                            maxValue = 0;
                            toTime = 0;
                            selectedMenu = 0;
                            decorate();
                            updateNodeTable();
                        } else if (timeConvertMenu.getSelectedIndex() == 1) {
                            maxValue = 0;
                            selectedMenu = 1;
                            toTime = 60;
                            decorate();
                            updateNodeTable();
                        } else if (timeConvertMenu.getSelectedIndex() == 2) {
                            maxValue = 0;
                            toTime = 3600;
                            selectedMenu = 2;
                            decorate();
                            updateNodeTable();
                        }
                    }
                }).start();
            }
        });

        JPanel timeConvertMenuPanel = new JPanel();
        timeConvertMenuPanel.setBackground(Color.WHITE);
        timeConvertMenuPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 3));
        timeConvertMenuPanel.add(timeConvertMenu);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout(3, 3));
        topPanel.setBackground(Color.WHITE);
        topPanel.add(timeConvertMenuPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
        add(chartPanel, BorderLayout.CENTER);
    }

    private void updateNodeTable() {
        int row = 0;
        while (row < nodeTable.getRowCount()) {
            String node = MySequentialGraphVars.nodeNameMap.get(nodeTable.getValueAt(row, 1).toString());
            MyNode n = (MyNode) MySequentialGraphVars.g.vRefs.get(node);
            long time = (this.toTime > 0 ? (long) (n.getCurrentValue()/this.toTime): (long) n.getCurrentValue());
            String strTime = MyMathUtil.getCommaSeperatedNumber(time);
            String strTimeRatio = MyMathUtil.twoDecimalFormat((float) time/maxValue);
            nodeTable.setValueAt(strTime, row, 2);
            nodeTable.setValueAt(strTimeRatio, row, 3);
            row++;
        }
    }

    public void enlarge() {
            MyProgressBar pb = new MyProgressBar(false);
            try {
                this.decorate();

                String[] statTableColumns = {"PROPERTY", "VALUE"};
                String[][] statTableData = {
                        {"NODES", MyMathUtil.getCommaSeperatedNumber(this.nodes.size())},
                        {"MAX. VALUE", MyMathUtil.twoDecimalFormat(this.maxValue)},
                        {"NIN. VALUE", MyMathUtil.twoDecimalFormat(this.minValue)},
                        {"AVG. VALUE", MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(this.avgValue))},
                        {"STD. VALUE", MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(this.stdValue))}
                };

                DefaultTableModel statTableModel = new DefaultTableModel(statTableData, statTableColumns);
                JTable statTable = new JTable(statTableModel);
                statTable.setBackground(Color.WHITE);
                statTable.setFont(MySequentialGraphVars.tahomaPlainFont12);
                statTable.setRowHeight(22);
                statTable.getTableHeader().setFont(MySequentialGraphVars.tahomaBoldFont11);
                statTable.getTableHeader().setOpaque(false);
                statTable.getTableHeader().setBackground(new Color(0, 0, 0, 0));
                statTable.getTableHeader().getColumnModel().getColumn(0).setPreferredWidth(100);

                JScrollPane statTableScrollPane = new JScrollPane(statTable);
                statTableScrollPane.setBackground(Color.WHITE);

                String[] nodeTableColumns = {"NO.", "NODE", "V.", "V. R."};
                String[][] nodeTableData = {};
                DefaultTableModel nodeTableModel = new DefaultTableModel(nodeTableData, nodeTableColumns);
                nodeTable = new JTable(nodeTableModel);
                nodeTable.setBackground(Color.WHITE);
                nodeTable.setFont(MySequentialGraphVars.f_pln_12);
                nodeTable.setRowHeight(22);
                nodeTable.getTableHeader().setFont(MySequentialGraphVars.tahomaBoldFont11);
                nodeTable.getTableHeader().setOpaque(false);
                nodeTable.getTableHeader().setBackground(new Color(0, 0, 0, 0));
                nodeTable.getTableHeader().getColumnModel().getColumn(0).setPreferredWidth(25);
                nodeTable.getTableHeader().getColumnModel().getColumn(2).setPreferredWidth(25);
                nodeTable.getTableHeader().getColumnModel().getColumn(3).setPreferredWidth(25);
                nodeTable.setSelectionForeground(Color.BLACK);
                nodeTable.setSelectionBackground(Color.LIGHT_GRAY);

                String [] nodeTableColumnTooltips = {"NO.", "NODE", "CURRENT NODE VALUE", "CURRENT NODE VALUE RATIO AGAINST MAX. NODE VALUE"};
                MyTableToolTipper nodeTooltipHeader = new MyTableToolTipper(nodeTable.getColumnModel());
                nodeTooltipHeader.setToolTipStrings(nodeTableColumnTooltips);
                nodeTable.setTableHeader(nodeTooltipHeader);

                int i = 0;
                LinkedHashMap<String, Float> valueMap = new LinkedHashMap();
                for (MyNode n : this.nodes) {
                    valueMap.put(n.getName(),  n.getCurrentValue());
                }
                valueMap = MySequentialGraphSysUtil.sortMapByFloatValue(valueMap);

                for (String n : valueMap.keySet()) {
                    String pr = MyMathUtil.twoDecimalFormat((double) valueMap.get(n) / this.maxValue);
                    nodeTableModel.addRow(new String[]{
                        "" + (++i),
                        MySequentialGraphSysUtil.getNodeName(n),
                        MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(valueMap.get(n))),
                        pr
                    });
                }

                JScrollPane nodeTableScrollPane = new JScrollPane(nodeTable);
                nodeTableScrollPane.setBackground(Color.WHITE);

                JTextField nodeSearchTxt = new JTextField();
                JButton nodeSelectBtn = new JButton("SEL.");
                nodeSelectBtn.setFont(MyDirectGraphVars.tahomaPlainFont12);
                nodeSelectBtn.setFocusable(false);
                nodeSearchTxt.setBackground(Color.WHITE);
                nodeSearchTxt.setBorder(BorderFactory.createLoweredSoftBevelBorder());
                JPanel nodeTableSearchPanel = MyTableUtil.searchAndSaveDataPanelForJTable2(this, nodeSearchTxt, nodeSelectBtn, nodeTableModel, nodeTable);
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

                JFrame f = new JFrame(" " + MySequentialGraphVars.getSequentialGraphViewer().nodeValueName + " BY NODE DISTRIBUTION");
                f.setBackground(Color.WHITE);
                f.setPreferredSize(new Dimension(550, 450));
                f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                JSplitPane tableSplitPane = new JSplitPane();
                tableSplitPane.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
                tableSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
                tableSplitPane.setTopComponent(statTableScrollPane);
                tableSplitPane.setBottomComponent(nodeTablePanel);
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
                    @Override public void componentResized(ComponentEvent e) {
                        super.componentResized(e);
                        contentPane.setDividerLocation(0.84);
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

    private JFreeChart setValueChart() {
        int nodeCount = 0;
        float totalValue = 0;

        TreeMap<Long, Integer> valueMap = new TreeMap<>();
        if (MySequentialGraphVars.getSequentialGraphViewer().singleNode != null) {
            if (MySequentialGraphVars.getSequentialGraphViewer().predecessorsOnly) {
                nodes = MySequentialGraphVars.getSequentialGraphViewer().singleNodePredecessors;
            } else if (MySequentialGraphVars.getSequentialGraphViewer().successorsOnly) {
                nodes = MySequentialGraphVars.getSequentialGraphViewer().singleNodeSuccessors;
            } else if (MySequentialGraphVars.getSequentialGraphViewer().neighborsOnly) {
                nodes = MySequentialGraphVars.getSequentialGraphViewer().singleNodeSuccessors;
                nodes.addAll(MySequentialGraphVars.getSequentialGraphViewer().singleNodePredecessors);
            }
        } else if (MySequentialGraphVars.getSequentialGraphViewer().multiNodes != null && MySequentialGraphVars.getSequentialGraphViewer().multiNodes.size() > 0) {
            if (MySequentialGraphVars.getSequentialGraphViewer().predecessorsOnly) {
                nodes = MySequentialGraphVars.getSequentialGraphViewer().multiNodePredecessors;
            } else if (MySequentialGraphVars.getSequentialGraphViewer().successorsOnly) {
                nodes = MySequentialGraphVars.getSequentialGraphViewer().multiNodeSuccessors;
            } else if (MySequentialGraphVars.getSequentialGraphViewer().neighborsOnly) {
                nodes = MySequentialGraphVars.getSequentialGraphViewer().multiNodePredecessors;
                nodes.addAll(MySequentialGraphVars.getSequentialGraphViewer().multiNodeSuccessors);
            } else if (MySequentialGraphVars.getSequentialGraphViewer().sharedNeighborsOnly) {
                nodes = MySequentialGraphVars.getSequentialGraphViewer().sharedSuccessors;
                nodes.addAll(MySequentialGraphVars.getSequentialGraphViewer().sharedPredecessors);
            } else if (MySequentialGraphVars.getSequentialGraphViewer().sharedPredecessorsOly) {
                nodes = MySequentialGraphVars.getSequentialGraphViewer().sharedPredecessors;
            } else if (MySequentialGraphVars.getSequentialGraphViewer().sharedSuccessorsOnly) {
                nodes = MySequentialGraphVars.getSequentialGraphViewer().sharedSuccessors;
            }
        } else {
            nodes = new HashSet<>(MySequentialGraphVars.g.getVertices());
        }
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0) continue;
            long value = (this.toTime > 0 ? (long) (n.getCurrentValue()/this.toTime): (long) n.getCurrentValue());
            totalValue += value;
            nodeCount++;

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

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Long nodeValue : valueMap.keySet()) {
            dataset.addValue(valueMap.get(nodeValue), "", nodeValue);
        }

        this.avgValue = totalValue/nodeCount;
        this.stdValue = getNodeValueStandardDeviation(valueMap);

        String plotTitle = "";
        String xaxis = MySequentialGraphVars.getSequentialGraphViewer().nodeValueName + " BY NODE";
        String yaxis = "";
        PlotOrientation orientation = PlotOrientation.VERTICAL;
        boolean show = false;
        boolean toolTips = true;
        boolean urls = false;
        return ChartFactory.createBarChart(plotTitle, xaxis, yaxis, dataset, orientation, show, toolTips, urls);
    }

    public float getNodeValueStandardDeviation(TreeMap<Long, Integer> valueMap) {
        float sum = 0.00f;
        for (long n : valueMap.keySet()) {
            sum += n;
        }
        double mean = sum / valueMap.size();
        sum = 0f;
        for (long n : valueMap.keySet()) {
            sum += Math.pow(n - mean, 2);
        }
        return (sum == 0.00f ? 0.00f : (float) Math.sqrt(sum / valueMap.size()));
    }

    @Override public void actionPerformed(ActionEvent e) {

    }
}
