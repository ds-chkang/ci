package medousa.direct.graph.toplevel;

import medousa.direct.graph.MyDirectGraphComboBoxTooltipper;
import medousa.direct.graph.MyDirectNode;
import medousa.MyProgressBar;
import medousa.direct.utils.MyDirectGraphMathUtil;
import medousa.direct.utils.MyDirectGraphSysUtil;
import medousa.direct.utils.MyDirectGraphVars;
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
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.TreeMap;

public class MyDirectGraphTopLevelDistributionChart
extends JPanel {

    protected boolean MAXIMIZED;
    private JComboBox chartMenu;
    private int selectedChartIdx;
    private String [] chartMenuTooltips = {
            "NODE COUNT DISTRIBUTION BY GRAPH",
            "AVERAGE SHORTEST OUT-DISTANCE DISTRIBUTION",
            "AVERAGE SHORTEST IN-DISTANCE DISTRIBUTION",
            "REACHAED OUT-NODE COUNT DISTRIBUTION",
            "UNREACHED  OUT-NODE COUNT DISTRIBUTION"};


    public MyDirectGraphTopLevelDistributionChart() {
        this.decorate();
    }

    public void decorate() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                try {
                    removeAll();
                    setLayout(new BorderLayout(3, 3));
                    setBackground(Color.WHITE);

                    ChartPanel chartPanel = null;
                    if (selectedChartIdx == 0 || chartMenu == null) {
                        chartPanel = new ChartPanel(setGraphNodeCountChart());
                    } else if (selectedChartIdx == 1) {
                        chartPanel = new ChartPanel(setAverageShortestOutDistanceChart());
                    } else if (selectedChartIdx == 2) {
                        chartPanel = new ChartPanel(setAverageShortestInDistanceChart());
                    } else if (selectedChartIdx == 3) {
                        chartPanel = new ChartPanel(setReachedOutNodeCountChart());
                    } else if (selectedChartIdx == 4) {
                        chartPanel = new ChartPanel(setUnReachedOutNodeCountChart());
                    }
                    chartPanel.getChart().getCategoryPlot().setRangeGridlinePaint(Color.DARK_GRAY);
                    chartPanel.getChart().getCategoryPlot().setDomainGridlinePaint(Color.DARK_GRAY);
                    chartPanel.getChart().getCategoryPlot().setBackgroundPaint(Color.WHITE);
                    chartPanel.getChart().getCategoryPlot().getDomainAxis().setTickLabelFont(MyDirectGraphVars.tahomaPlainFont11);
                    chartPanel.getChart().getCategoryPlot().getDomainAxis().setLabelFont(MyDirectGraphVars.tahomaPlainFont11);
                    chartPanel.getChart().getCategoryPlot().getRangeAxis().setTickLabelFont(MyDirectGraphVars.tahomaPlainFont11);
                    chartPanel.getChart().getCategoryPlot().getRangeAxis().setLabelFont(MyDirectGraphVars.tahomaPlainFont11);
                    CategoryAxis domainAxis = chartPanel.getChart().getCategoryPlot().getDomainAxis();
                    domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);

                    BarRenderer renderer = (BarRenderer) chartPanel.getChart().getCategoryPlot().getRenderer();
                    renderer.setSeriesPaint(0, new Color(0, 0, 0, 0.2f));//Color.LIGHT_GRAY);//Color.decode("#2084FE"));
                    renderer.setShadowPaint(Color.WHITE);
                    renderer.setBaseFillPaint(Color.decode("#07CF61"));
                    renderer.setBarPainter(new StandardBarPainter());
                    renderer.setBaseLegendTextFont(MyDirectGraphVars.tahomaPlainFont13);

                    JLabel titleLabel = new JLabel(" DIST.");
                    titleLabel.setToolTipText("DISTRIBUTIONS");
                    titleLabel.setFont(MyDirectGraphVars.tahomaBoldFont12);
                    titleLabel.setBackground(Color.WHITE);
                    titleLabel.setForeground(Color.DARK_GRAY);

                    chartMenu = new JComboBox();
                    chartMenu.addItem("G. N. C.");
                    chartMenu.addItem("AVG. S. OUT-D.");
                    chartMenu.addItem("AVG. S. IN-D.");
                    chartMenu.addItem("R. OUT-N. C.");
                    chartMenu.addItem("UNR. OUT-N. C.");
                    chartMenu.setFocusable(false);
                    chartMenu.setBackground(Color.WHITE);
                    chartMenu.setToolTipText("SELECT A DISTRIBUTION");
                    chartMenu.setFont(MyDirectGraphVars.tahomaPlainFont11);
                    chartMenu.setSelectedIndex(selectedChartIdx);
                    chartMenu.addActionListener(new ActionListener() {
                        @Override public void actionPerformed(ActionEvent e) {
                            new Thread(new Runnable() {
                                @Override public void run() {
                                    MyProgressBar pb = new MyProgressBar(false);
                                    selectedChartIdx = chartMenu.getSelectedIndex();
                                    decorate();
                                    pb.updateValue(100, 100);
                                    pb.dispose();
                                }
                            }).start();
                        }
                        });

                    MyDirectGraphComboBoxTooltipper comboBoxTooltipper = new MyDirectGraphComboBoxTooltipper();
                    comboBoxTooltipper.setTooltips(Arrays.asList(chartMenuTooltips));
                    chartMenu.setRenderer(comboBoxTooltipper);

                    JPanel titlePanel = new JPanel();
                    titlePanel.setBackground(Color.WHITE);
                    titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
                    titlePanel.add(titleLabel);

                    JButton enlargeBtn = new JButton("+");
                    enlargeBtn.setToolTipText("ENLARGE");
                    enlargeBtn.setFont(MyDirectGraphVars.tahomaPlainFont12);
                    enlargeBtn.setBackground(Color.WHITE);
                    enlargeBtn.setFocusable(false);
                    enlargeBtn.addActionListener(new ActionListener() {
                        @Override public void actionPerformed(ActionEvent e) {
                            new Thread(new Runnable() {
                                @Override public void run() {
                                    enlarge();
                                }
                            }).start();
                        }
                    });

                    JPanel menuPanel = new JPanel();
                    menuPanel.setBackground(Color.WHITE);
                    menuPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 3));
                    menuPanel.add(chartMenu);
                    menuPanel.add(enlargeBtn);

                    JPanel topPanel = new JPanel();
                    topPanel.setLayout(new BorderLayout(3, 3));
                    topPanel.setBackground(Color.WHITE);
                    topPanel.add(titlePanel, BorderLayout.WEST);
                    topPanel.add(menuPanel, BorderLayout.EAST);
                    add(topPanel, BorderLayout.NORTH);
                    add(chartPanel, BorderLayout.CENTER);
                    renderer.setBaseLegendTextFont(MyDirectGraphVars.tahomaPlainFont11);
                    revalidate();
                    repaint();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private JFreeChart setAverageShortestOutDistanceChart() {
        int totalCnt = 0;
        double totalValue = 0;
        TreeMap<Integer, Integer> valueMap = new TreeMap<>();
        Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
        for (MyDirectNode n : nodes) {
            if (n.getCurrentValue() <= 0) continue;
            int value = (int)n.getAverageShortestOutDistance();
            totalCnt++;
            totalValue += value;

            if (valueMap.containsKey(value)) {
                valueMap.put(value, valueMap.get(value) + 1);
            } else {
                valueMap.put(value, 1);
            }
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Integer value : valueMap.keySet()) {
            dataset.addValue(valueMap.get(value), "AVG. SHORTEST OUT-DISTANCE", value);
        }

        double avg = totalValue/totalCnt;
        String plotTitle = "";
        String xaxis = "[AVG.: " + MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(avg)) + "]";
        String yaxis = "";
        PlotOrientation orientation = PlotOrientation.VERTICAL;
        boolean show = false;
        boolean toolTips = true;
        boolean urls = false;
        return ChartFactory.createBarChart(plotTitle, xaxis, yaxis, dataset, orientation, show, toolTips, urls);
    }

    private JFreeChart setAverageShortestInDistanceChart() {
        int totalCnt = 0;
        double totalValue = 0;
        TreeMap<Integer, Integer> valueMap = new TreeMap<>();
        Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
        for (MyDirectNode n : nodes) {
            if (n.getCurrentValue() <= 0) continue;
            int value = (int)n.getAverageShortestInDistance();
            totalCnt++;
            totalValue += value;
            if (valueMap.containsKey(value)) {
                valueMap.put(value, valueMap.get(value) + 1);
            } else {
                valueMap.put(value, 1);
            }
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Integer value : valueMap.keySet()) {
            dataset.addValue(valueMap.get(value), "AVG. SHORTEST IN-DISTANCE", value);
        }

        double avg = totalValue/totalCnt;
        String plotTitle = "";
        String xaxis = "[AVG.: " + MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(avg)) + "]";
        String yaxis = "";
        PlotOrientation orientation = PlotOrientation.VERTICAL;
        boolean show = false;
        boolean toolTips = true;
        boolean urls = false;
        return ChartFactory.createBarChart(plotTitle, xaxis, yaxis, dataset, orientation, show, toolTips, urls);
    }

    private JFreeChart setGraphNodeCountChart() {
        double totalValue = 0;
        TreeMap<Integer, Integer> valueMap = new TreeMap<>();
        Set<Set<MyDirectNode>> clusters = MyDirectGraphSysUtil.getGraphs();
        for (Set<MyDirectNode> cluster : clusters) {
            int value = cluster.size();
            totalValue += value;
            if (valueMap.containsKey(value)) {
                valueMap.put(value, valueMap.get(value) + 1);
            } else {
                valueMap.put(value, 1);
            }
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Integer value : valueMap.keySet()) {
            dataset.addValue(valueMap.get(value), "G. N. C.", value);
        }

        String avgValue = MyDirectGraphMathUtil.twoDecimalFormat(totalValue/clusters.size());
        String plotTitle = "";
        String xaxis = "[AVG.: " + MyDirectGraphSysUtil.formatAverageValue(avgValue)+"]";
        String yaxis = "";
        PlotOrientation orientation = PlotOrientation.VERTICAL;
        boolean show = false;
        boolean toolTips = true;
        boolean urls = false;
        return ChartFactory.createBarChart(plotTitle, xaxis, yaxis, dataset, orientation, show, toolTips, urls);
    }

    private JFreeChart setReachedOutNodeCountChart() {
        double totalValue = 0;
        long totalCnt = 0;
        TreeMap<Long, Integer> valueMap = new TreeMap<>();
        Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
        for (MyDirectNode n : nodes) {
            if (n.getCurrentValue() <= 0) continue;
            long value = n.getReachedOutNodeCount();
            totalCnt++;
            totalValue += value;
            if (valueMap.containsKey(value)) {
                valueMap.put(value, valueMap.get(value) + 1);
            } else {
                valueMap.put(value, 1);
            }
        }
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Long value : valueMap.keySet()) {
            dataset.addValue(valueMap.get(value), "R. OUT-N. C.", value);
        }

        String avgValue = MyDirectGraphMathUtil.twoDecimalFormat(totalValue/totalCnt);
        String plotTitle = "";
        String xaxis = "[AVG.: " + MyDirectGraphSysUtil.formatAverageValue(avgValue)+"]";
        String yaxis = "";
        PlotOrientation orientation = PlotOrientation.VERTICAL;
        boolean show = false;
        boolean toolTips = true;
        boolean urls = false;
        return ChartFactory.createBarChart(plotTitle, xaxis, yaxis, dataset, orientation, show, toolTips, urls);
    }

    private JFreeChart setReachedInNodeCountChart() {
        double totalValue = 0;
        long totalCnt = 0;
        TreeMap<Long, Integer> valueMap = new TreeMap<>();
        Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
        for (MyDirectNode n : nodes) {
            if (n.getCurrentValue() <= 0) continue;
            long value = n.getReachedInNodeCount();
            totalCnt++;
            totalValue += value;
            if (valueMap.containsKey(value)) {
                valueMap.put(value, valueMap.get(value) + 1);
            } else {
                valueMap.put(value, 1);
            }
        }
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Long value : valueMap.keySet()) {
            dataset.addValue(valueMap.get(value), "UNR. IN-N. C.", value);
        }

        String avgValue = MyDirectGraphMathUtil.twoDecimalFormat(totalValue/totalCnt);
        String plotTitle = "";
        String xaxis = "[AVG.: " + MyDirectGraphSysUtil.formatAverageValue(avgValue)+"]";
        String yaxis = "";
        PlotOrientation orientation = PlotOrientation.VERTICAL;
        boolean show = false;
        boolean toolTips = true;
        boolean urls = false;
        return ChartFactory.createBarChart(plotTitle, xaxis, yaxis, dataset, orientation, show, toolTips, urls);
    }

    private JFreeChart setUnReachedInNodeCountChart() {
        double totalValue = 0;
        long totalCnt = 0;
        TreeMap<Long, Integer> valueMap = new TreeMap<>();
        int totalNonZeroNodes = (int) MyDirectGraphVars.directGraph.getGraphNodeCount();
        Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
        for (MyDirectNode n : nodes) {
            if (n.getCurrentValue() <= 0) continue;
            long value = (totalNonZeroNodes - n.getReachedInNodeCount())-1;
            totalCnt++;
            totalValue += value;
            if (valueMap.containsKey(value)) {
                valueMap.put(value, valueMap.get(value) + 1);
            } else {
                valueMap.put(value, 1);
            }
        }
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Long value : valueMap.keySet()) {
            dataset.addValue(valueMap.get(value), "UNR. IN-N. C.", value);
        }

        String avgValue = MyDirectGraphMathUtil.twoDecimalFormat(totalValue/totalCnt);
        String plotTitle = "";
        String xaxis = "[AVG.: " + MyDirectGraphSysUtil.formatAverageValue(avgValue)+"]";
        String yaxis = "";
        PlotOrientation orientation = PlotOrientation.VERTICAL;
        boolean show = false;
        boolean toolTips = true;
        boolean urls = false;
        return ChartFactory.createBarChart(plotTitle, xaxis, yaxis, dataset, orientation, show, toolTips, urls);
    }

    private JFreeChart setUnReachedOutNodeCountChart() {
        double totalValue = 0;
        long totalCnt = 0;
        TreeMap<Long, Integer> valueMap = new TreeMap<>();
        Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
        for (MyDirectNode n : nodes) {
            if (n.getCurrentValue() <= 0) continue;
            long value = n.unReachedOutNodeCount;
            totalCnt++;
            totalValue += value;
            if (valueMap.containsKey(value)) {
                valueMap.put(value, valueMap.get(value) + 1);
            } else {
                valueMap.put(value, 1);
            }
        }
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Long value : valueMap.keySet()) {
            dataset.addValue(valueMap.get(value), "R. N. C.", value);
        }

        String avgValue = MyDirectGraphMathUtil.twoDecimalFormat(totalValue/totalCnt);
        String plotTitle = "";
        String xaxis = "[AVG.: " + MyDirectGraphSysUtil.formatAverageValue(avgValue)+"]";
        String yaxis = "";
        PlotOrientation orientation = PlotOrientation.VERTICAL;
        boolean show = false;
        boolean toolTips = true;
        boolean urls = false;
        return ChartFactory.createBarChart(plotTitle, xaxis, yaxis, dataset, orientation, show, toolTips, urls);
    }

    public void enlarge() {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            MyDirectGraphTopLevelDistributionChart topLevelDistributionChart = new MyDirectGraphTopLevelDistributionChart();
            topLevelDistributionChart.MAXIMIZED = true;
            JFrame f = new JFrame(" DISTRIBUTIONS");
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
            f.setLayout(new BorderLayout(3, 3));
            f.getContentPane().add(topLevelDistributionChart, BorderLayout.CENTER);
            f.setPreferredSize(new Dimension(450, 350));
            f.pack();
            pb.updateValue(100, 100);
            pb.dispose();
            f.setAlwaysOnTop(true);
            f.setVisible(true);
            f.setAlwaysOnTop(false);
        } catch (Exception ex) {
            MAXIMIZED = false;
            pb.updateValue(100, 100);
            pb.dispose();
        }
    }
}
