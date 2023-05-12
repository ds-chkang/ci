package medousa.direct.graph.toplevel;

import medousa.direct.graph.MyDirectEdge;
import medousa.MyProgressBar;
import medousa.direct.utils.MyDirectGraphMathUtil;
import medousa.direct.utils.MyDirectGraphSysUtil;
import medousa.direct.utils.MyDirectGraphVars;
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
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.TreeMap;

public class MyDirectGraphTopLevelEdgeValueDistribution
extends JPanel
implements ActionListener {

    protected boolean isValueExists = false;
    protected boolean MAXIMIZED;
    private float maxValue = 0;
    private float minValue = 1000000000;
    private float avgValue = 0;
    private float stdValue = 0;

    public MyDirectGraphTopLevelEdgeValueDistribution() {
        this.decorate();
    }

    public void decorate() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                removeAll();
                setLayout(new BorderLayout(3, 3));
                setBackground(Color.WHITE);

                JFreeChart chart = null;
                if (MyDirectGraphVars.getDirectGraphViewer().vc.edgeValueComboBoxMenu.getSelectedIndex() > 0) {
                    chart = setChart();
                } else {
                    isValueExists = false;
                }
                if (!isValueExists) {
                    JLabel titleLabel = new JLabel(" E. V.");
                    titleLabel.setToolTipText("EDGE VALUE DISTRIBUTION");
                    titleLabel.setFont(MyDirectGraphVars.tahomaBoldFont11);
                    titleLabel.setBackground(Color.WHITE);
                    titleLabel.setForeground(Color.DARK_GRAY);

                    JPanel titlePanel = new JPanel();
                    titlePanel.setBackground(Color.WHITE);
                    titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 6));
                    titlePanel.add(titleLabel);

                    JPanel topPanel = new JPanel();
                    topPanel.setLayout(new BorderLayout(3, 8));
                    topPanel.setBackground(Color.WHITE);
                    topPanel.add(titlePanel, BorderLayout.WEST);

                    JLabel msg = new JLabel("N/A");
                    msg.setFont(MyDirectGraphVars.tahomaPlainFont12);
                    msg.setBackground(Color.WHITE);
                    msg.setHorizontalAlignment(JLabel.CENTER);
                    add(topPanel, BorderLayout.NORTH);
                    add(msg, BorderLayout.CENTER);
                    revalidate();
                    repaint();
                } else {
                    ChartPanel chartPanel = new ChartPanel(chart);
                    chartPanel.getChart().getCategoryPlot().setRangeGridlinePaint(Color.DARK_GRAY);
                    chartPanel.getChart().getCategoryPlot().setDomainGridlinePaint(Color.DARK_GRAY);
                    chartPanel.getChart().getCategoryPlot().setBackgroundPaint(Color.WHITE);
                    chartPanel.getChart().getCategoryPlot().getDomainAxis().setTickLabelFont(MyDirectGraphVars.tahomaPlainFont11);
                    chartPanel.getChart().getCategoryPlot().getDomainAxis().setLabelFont(MyDirectGraphVars.tahomaPlainFont11);
                    chartPanel.getChart().getCategoryPlot().getRangeAxis().setTickLabelFont(MyDirectGraphVars.tahomaPlainFont11);
                    chartPanel.getChart().getCategoryPlot().getRangeAxis().setLabelFont(MyDirectGraphVars.tahomaPlainFont11);

                    CategoryAxis domainAxis = chartPanel.getChart().getCategoryPlot().getDomainAxis();
                    domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);

                    BarRenderer barRenderer = (BarRenderer) chartPanel.getChart().getCategoryPlot().getRenderer();
                    barRenderer.setSeriesPaint(0, new Color(0, 0, 0, 0.25f));//Color.LIGHT_GRAY);//Color.decode("#2084FE"));
                    barRenderer.setShadowPaint(Color.WHITE);
                    barRenderer.setBaseFillPaint(Color.decode("#07CF61"));
                    barRenderer.setBarPainter(new StandardBarPainter());
                    barRenderer.setBaseLegendTextFont(MyDirectGraphVars.tahomaPlainFont11);

                    JLabel titleLabel = new JLabel(" E. V.");
                    titleLabel.setToolTipText("EDGE VALUE DISTRIBUTION");
                    titleLabel.setFont(MyDirectGraphVars.tahomaBoldFont11);
                    titleLabel.setBackground(Color.WHITE);
                    titleLabel.setForeground(Color.DARK_GRAY);

                    JPanel titlePanel = new JPanel();
                    titlePanel.setBackground(Color.WHITE);
                    titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
                    titlePanel.add(titleLabel);

                    JButton enlargeBtn = new JButton("+");
                    enlargeBtn.setToolTipText("ENLARGE");
                    enlargeBtn.setFocusable(false);
                    enlargeBtn.setBackground(Color.WHITE);
                    enlargeBtn.setFont(MyDirectGraphVars.tahomaPlainFont11);
                    enlargeBtn.addActionListener(new ActionListener() {
                        @Override public void actionPerformed(ActionEvent e) {
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override public void run() {
                                    new Thread(new Runnable() {
                                        @Override public void run() {
                                            enlarge();
                                        }
                                    }).start();
                                }
                            });
                        }
                    });

                    JPanel enlargePanel = new JPanel();
                    enlargePanel.setBackground(Color.WHITE);
                    enlargePanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 3));
                    enlargePanel.add(enlargeBtn);

                    JPanel topPanel = new JPanel();
                    topPanel.setBackground(Color.WHITE);
                    topPanel.setLayout(new BorderLayout(3, 3));
                    topPanel.add(titlePanel, BorderLayout.WEST);
                    topPanel.add(enlargePanel, BorderLayout.EAST);

                    add(chartPanel, BorderLayout.CENTER);
                    add(topPanel, BorderLayout.NORTH);

                    revalidate();
                    repaint();
                }
            }
        });
    }

    public void enlarge() {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            MyDirectGraphTopLevelEdgeValueDistribution edgeValueDistribution = new MyDirectGraphTopLevelEdgeValueDistribution();
            edgeValueDistribution.MAXIMIZED = true;

            String[] statTableColumns = {"PROPERTY", "VALUE"};
            String[][] statTableData = {
                    {"MAX. VALUE", MyDirectGraphMathUtil.twoDecimalFormat(maxValue)},
                    {"NIN. VALUE", MyDirectGraphMathUtil.twoDecimalFormat(minValue)},
                    {"AVG. VALUE", MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(avgValue))},
                    {"STD. VALUE", MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(stdValue))}
            };

            DefaultTableModel statTableModel = new DefaultTableModel(statTableData, statTableColumns);
            JTable statTable = new JTable(statTableModel);
            statTable.setBackground(Color.WHITE);
            statTable.setFont(MyDirectGraphVars.tahomaPlainFont12);
            statTable.setRowHeight(22);
            statTable.getTableHeader().setFont(MyDirectGraphVars.tahomaBoldFont11);
            statTable.getTableHeader().setOpaque(false);
            statTable.getTableHeader().setBackground(new Color(0, 0, 0, 0));
            statTable.getTableHeader().getColumnModel().getColumn(0).setPreferredWidth(100);

            JScrollPane statTableScrollPane = new JScrollPane(statTable);
            statTableScrollPane.setBackground(Color.WHITE);

            String[] edgeTableColumns = {"NO.", "SOURCE", "DEST", "V.", "V. R."};
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
            edgeTable.getTableHeader().getColumnModel().getColumn(3).setPreferredWidth(35);
            edgeTable.getTableHeader().getColumnModel().getColumn(4).setPreferredWidth(35);

            int i = 0;
            Collection<MyDirectEdge> edges = MyDirectGraphVars.directGraph.getEdges();
            LinkedHashMap<String, Float> valueMap = new LinkedHashMap();
            for (MyDirectEdge e : edges) {
                String edgeName = e.getSource().getName() + "-" + e.getDest().getName();
                valueMap.put(edgeName, e.getCurrentValue());
            }
            valueMap = MyDirectGraphSysUtil.sortMapByFloatValue(valueMap);

            for (String n : valueMap.keySet()) {
                String p = n.split("-")[0];
                String s = n.split("-")[1];
                float value = valueMap.get(n);
                String pr = MyDirectGraphMathUtil.twoDecimalFormat(valueMap.get(n) / MyDirectGraphVars.directGraph.maxEdgeValue);
                edgeTableModel.addRow(new String[]{"" + (++i), p, s, MyDirectGraphMathUtil.getCommaSeperatedNumber((long) value), pr});
            }

            JScrollPane nodeTableScrollPane = new JScrollPane(edgeTable);
            nodeTableScrollPane.setBackground(Color.WHITE);

            JTextField searchTxt = new JTextField();
            JButton selectBtn = new JButton("SEL.");
            selectBtn.setFont(MyDirectGraphVars.tahomaPlainFont12);
            selectBtn.setFocusable(false);
            searchTxt.setBackground(Color.WHITE);
            searchTxt.setBorder(BorderFactory.createLoweredSoftBevelBorder());
            JPanel searchPanel = MyTableUtil.searchAndSaveDataPanelForJTable2(edgeValueDistribution, searchTxt, selectBtn, edgeTableModel, edgeTable);
            searchTxt.setFont(MyDirectGraphVars.f_bold_12);
            searchTxt.setToolTipText("TYPE AN EDGE NAME TO SEARCH");
            searchTxt.setPreferredSize(new Dimension(90, 19));
            selectBtn.setPreferredSize(new Dimension(54, 19));
            searchPanel.remove(selectBtn);

            JPanel tablePanel = new JPanel();
            tablePanel.setBackground(Color.WHITE);
            tablePanel.setLayout(new BorderLayout(3, 3));
            tablePanel.add(nodeTableScrollPane, BorderLayout.CENTER);
            tablePanel.add(searchPanel, BorderLayout.SOUTH);

            JFrame f = new JFrame(" EDGE VALUE DISTRIBUTION");
            f.setBackground(Color.WHITE);
            f.setPreferredSize(new Dimension(550, 450));
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    f.setAlwaysOnTop(true);
                }

                @Override public void mouseExited(MouseEvent e) {
                    super.mouseExited(e);
                    f.setAlwaysOnTop(false);
                }
            });

            JSplitPane tableSplitPane = new JSplitPane();
            tableSplitPane.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            tableSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
            tableSplitPane.setTopComponent(statTableScrollPane);
            tableSplitPane.setBottomComponent(tablePanel);
            tableSplitPane.getRightComponent().setBackground(Color.WHITE);
            tableSplitPane.setDividerSize(5);

            JSplitPane contentPane = new JSplitPane();
            contentPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
            contentPane.setLeftComponent(edgeValueDistribution);
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

            f.setAlwaysOnTop(true);
            pb.updateValue(100, 100);
            pb.dispose();

            f.setVisible(true);
            f.setAlwaysOnTop(false);
        } catch (Exception ex) {
            pb.updateValue(100, 100);
            pb.dispose();
        }
    }

    private JFreeChart setChart() {
        int nodeCount = 0;
        float totalValue = 0;

        TreeMap<Integer, Integer> valueMap = new TreeMap<>();
        Collection<MyDirectEdge> edges = MyDirectGraphVars.directGraph.getEdges();
        for (MyDirectEdge e : edges) {
            float value = e.getCurrentValue();
            totalValue += value;
            nodeCount++;

            if (value > this.maxValue) {
                this.maxValue = value;
            }

            if (value > 0 && value < this.minValue) {
                this.minValue = value;
            }

            if (valueMap.containsKey((int) value)) {
                valueMap.put((int) value, valueMap.get((int) value) + 1);
            } else {
                valueMap.put((int) value, 1);
            }

            isValueExists = true;
        }

        this.avgValue = totalValue/nodeCount;
        this.stdValue = getEdgeValueStandardDeviation(valueMap);

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Integer value : valueMap.keySet()) {
            dataset.addValue(valueMap.get(value), "E. V.", value);
        }

        String plotTitle = "";
        String xaxis = "EDGE VALUE DISTRIBUTION";
        String yaxis = "";
        PlotOrientation orientation = PlotOrientation.VERTICAL;
        boolean show = false;
        boolean toolTips = true;
        boolean urls = false;
        return ChartFactory.createBarChart(plotTitle, xaxis, yaxis, dataset, orientation, show, toolTips, urls);
    }

    public float getEdgeValueStandardDeviation(TreeMap<Integer, Integer> valueMap) {
        float sum = 0.00F;
        for (int n : valueMap.keySet()) {
            sum += n;
        }
        double mean = sum / valueMap.size();
        sum = 0F;
        for (int n : valueMap.keySet()) {
            sum += Math.pow(n - mean, 2);
        }
        return (sum == 0 ? 0.00F : (float) Math.sqrt(sum / valueMap.size()));
    }

    @Override public void actionPerformed(ActionEvent e) {}
}
