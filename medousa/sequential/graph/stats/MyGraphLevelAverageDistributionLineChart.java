package medousa.sequential.graph.stats;

import medousa.sequential.graph.clustering.MyClusteringConfig;
import medousa.sequential.graph.MyComboBoxTooltipRenderer;
import medousa.sequential.graph.MyEdge;
import medousa.sequential.graph.MyNode;
import medousa.MyProgressBar;
import medousa.sequential.utils.MyMathUtil;
import medousa.sequential.utils.MySequentialGraphVars;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.HorizontalAlignment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class MyGraphLevelAverageDistributionLineChart
extends JPanel {

    private JComboBox averageChartMenu;
    private int selectedChartIdx;

    public MyGraphLevelAverageDistributionLineChart() {
        this.decorate();
    }

    private JFreeChart setAverageShortestDistanceChart() {
        XYSeries avgShortestDistanceDistributionSeries = new XYSeries("AVG. SHORTEST DISTANCE DISTRIBUTION");
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        Map<Double, Long> avgShortestDistanceLengthMap = new HashMap<>();
        for (MyNode n : nodes) {
            if (MySequentialGraphVars.getSequentialGraphViewer().isClustered) {
                if (n.clusteringColor != MyClusteringConfig.selectedClusterColor) continue;
            } else if (n.getCurrentValue() == 0) continue;
            double avgShortestDistance = (Double.parseDouble(MyMathUtil.twoDecimalFormat(n.getAverageShortestDistance())));
            if (avgShortestDistanceLengthMap.containsKey(avgShortestDistance)) {
                avgShortestDistanceLengthMap.put(avgShortestDistance, avgShortestDistanceLengthMap.get(avgShortestDistance)+1);
            } else {
                avgShortestDistanceLengthMap.put(avgShortestDistance, 1L);
            }
        }

        for (Double avgShortestPathLen : avgShortestDistanceLengthMap.keySet()) {
            avgShortestDistanceDistributionSeries.add(avgShortestPathLen, avgShortestDistanceLengthMap.get(avgShortestPathLen));
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(avgShortestDistanceDistributionSeries);

        JFreeChart chart = ChartFactory.createXYLineChart("", "AVG. SHORTEST DISTANCE", "", dataset);
        chart.getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
        chart.getXYPlot().setBackgroundPaint(Color.WHITE);
        chart.getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
        chart.getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);
        chart.getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
        chart.getXYPlot().getDomainAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
        chart.getXYPlot().getDomainAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
        chart.getXYPlot().getRangeAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
        chart.getXYPlot().getRangeAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
        chart.getXYPlot().getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        chart.setBackgroundPaint(Color.WHITE);

        return chart;
    }

    private JFreeChart setAverageNodeRecurrenceTimeChart() {
        XYSeries avgRecurrenceTimeDistributionSeries = new XYSeries("AVG. RECURENCE TIME DISTRIBUTION");
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        Map<Double, Long> avgRecurrenceTimeMap = new HashMap<>();
        for (MyNode n : nodes) {
            if (MySequentialGraphVars.getSequentialGraphViewer().isClustered) {
                if (n.clusteringColor != MyClusteringConfig.selectedClusterColor) continue;
            } else if (n.getCurrentValue() == 0) continue;
            double avgRecurrenceTime = (Double.parseDouble(MyMathUtil.twoDecimalFormat(n.getAvgRecurrenceTime())));
            if (avgRecurrenceTimeMap.containsKey(avgRecurrenceTime)) {
                avgRecurrenceTimeMap.put(avgRecurrenceTime, avgRecurrenceTimeMap.get(avgRecurrenceTime) + 1);
            } else {
                avgRecurrenceTimeMap.put(avgRecurrenceTime, 1L);
            }
        }

        for (Double avgRecurrenceTime : avgRecurrenceTimeMap.keySet()) {
            avgRecurrenceTimeDistributionSeries.add(avgRecurrenceTime, avgRecurrenceTimeMap.get(avgRecurrenceTime));
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(avgRecurrenceTimeDistributionSeries);

        JFreeChart chart = ChartFactory.createXYLineChart("", "AVG. RECURRENCE TIME", "", dataset);
        chart.getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
        chart.getXYPlot().setBackgroundPaint(Color.WHITE);
        chart.getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
        chart.getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);
        chart.getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
        chart.getXYPlot().getDomainAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
        chart.getXYPlot().getDomainAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
        chart.getXYPlot().getRangeAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
        chart.getXYPlot().getRangeAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
        chart.getXYPlot().getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        chart.setBackgroundPaint(Color.WHITE);

        return chart;
    }

    private JFreeChart setAverageRecurrenceLengthChart() {
        XYSeries avgNodeRecurrenceLengthDistributionSeries = new XYSeries("AVG. NODE RECURRENCE LENGTH DISTRIBUTION");
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        Map<Double, Long> avgNodeRecurrenceLengthMap = new HashMap<>();
        for (MyNode n : nodes) {
            if (MySequentialGraphVars.getSequentialGraphViewer().isClustered) {
                if (n.clusteringColor != MyClusteringConfig.selectedClusterColor) continue;
            } else if (n.getCurrentValue() == 0) continue;
            double avgNodeRecurrenceLength = (Double.parseDouble(MyMathUtil.twoDecimalFormat(n.getAverageRecurrenceLength())));
            if (avgNodeRecurrenceLengthMap.containsKey(avgNodeRecurrenceLength)) {
                avgNodeRecurrenceLengthMap.put(avgNodeRecurrenceLength, avgNodeRecurrenceLengthMap.get(avgNodeRecurrenceLength)+1);
            } else {
                avgNodeRecurrenceLengthMap.put(avgNodeRecurrenceLength, 1L);
            }
        }

        for (Double avgNodeRecurrence : avgNodeRecurrenceLengthMap.keySet()) {
            avgNodeRecurrenceLengthDistributionSeries.add(avgNodeRecurrence, avgNodeRecurrenceLengthMap.get(avgNodeRecurrence));
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(avgNodeRecurrenceLengthDistributionSeries);

        JFreeChart chart = ChartFactory.createXYLineChart("", "AVG. NODE RECURRENCE LENGTH", "", dataset);
        chart.getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
        chart.getXYPlot().setBackgroundPaint(Color.WHITE);
        chart.getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
        chart.getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);
        chart.getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
        chart.getXYPlot().getDomainAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
        chart.getXYPlot().getDomainAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
        chart.getXYPlot().getRangeAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
        chart.getXYPlot().getRangeAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
        chart.getXYPlot().getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        chart.setBackgroundPaint(Color.WHITE);

        return chart;
    }

    private JFreeChart setAverageNodeReachTimeChart() {
        XYSeries avgNodeTimeDistributionSeries = new XYSeries("AVG. NODE REACH TIME DISTRIBUTION");
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        Map<Double, Long> avgNodeTimeMap = new HashMap<>();
        for (MyNode n : nodes) {
            if (MySequentialGraphVars.getSequentialGraphViewer().isClustered) {
                if (n.clusteringColor != MyClusteringConfig.selectedClusterColor) continue;
            } else if (n.getCurrentValue() == 0) continue;
            double avgNodeTime = (Double.parseDouble(MyMathUtil.twoDecimalFormat(n.getAverageReachTime())));
            if (avgNodeTimeMap.containsKey(avgNodeTime)) {
                avgNodeTimeMap.put(avgNodeTime, avgNodeTimeMap.get(avgNodeTime)+1);
            } else {
                avgNodeTimeMap.put(avgNodeTime, 1L);
            }
        }

        for (Double avgNodeTime : avgNodeTimeMap.keySet()) {
            avgNodeTimeDistributionSeries.add(avgNodeTime, avgNodeTimeMap.get(avgNodeTime));
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(avgNodeTimeDistributionSeries);

        JFreeChart chart = ChartFactory.createXYLineChart("", "AVG. NODE REACH TIME", "", dataset);
        chart.getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
        chart.getXYPlot().setBackgroundPaint(Color.WHITE);
        chart.getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
        chart.getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);
        chart.getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
        chart.getXYPlot().getDomainAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
        chart.getXYPlot().getDomainAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
        chart.getXYPlot().getRangeAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
        chart.getXYPlot().getRangeAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
        chart.getXYPlot().getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        chart.setBackgroundPaint(Color.WHITE);

        return chart;
    }

    private JFreeChart setAverageNodeDurationChart() {
        XYSeries avgNodeDurationDistributionSeries = new XYSeries("AVG. NODE DURATION DISTRIBUTION");
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        Map<Double, Long> avgNodeDurationMap = new HashMap<>();
        for (MyNode n : nodes) {
            if (MySequentialGraphVars.getSequentialGraphViewer().isClustered) {
                if (n.clusteringColor != MyClusteringConfig.selectedClusterColor) continue;
            } else if (n.getCurrentValue() == 0) continue;
            double avgDuration = (Double.parseDouble(MyMathUtil.twoDecimalFormat(n.getAverageDuration())));
            if (avgNodeDurationMap.containsKey(avgDuration)) {
                avgNodeDurationMap.put(avgDuration, avgNodeDurationMap.get(avgDuration)+1);
            } else {
                avgNodeDurationMap.put(avgDuration, 1L);
            }
        }

        for (Double avgDuration : avgNodeDurationMap.keySet()) {
            avgNodeDurationDistributionSeries.add(avgDuration, avgNodeDurationMap.get(avgDuration));
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(avgNodeDurationDistributionSeries);

        JFreeChart chart = ChartFactory.createXYLineChart("", "AVG. NODE DURATION", "", dataset);
        chart.getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
        chart.getXYPlot().setBackgroundPaint(Color.WHITE);
        chart.getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
        chart.getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);
        chart.getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
        chart.getXYPlot().getDomainAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
        chart.getXYPlot().getDomainAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
        chart.getXYPlot().getRangeAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
        chart.getXYPlot().getRangeAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
        chart.getXYPlot().getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        chart.setBackgroundPaint(Color.WHITE);

        return chart;
    }

    private JFreeChart setAverageEdgeTimeChart() {
        XYSeries avgEdgeTimeDistributionSeries = new XYSeries("AVG. EDGE TIME DISTRIBUTION");
        Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
        Map<Double, Long> avgEdgeTimeMap = new HashMap<>();
        for (MyEdge e : edges) {
            if (MySequentialGraphVars.getSequentialGraphViewer().isClustered) {
                if (e.getSource().clusteringColor != MyClusteringConfig.selectedClusterColor ||
                    e.getDest().clusteringColor != MyClusteringConfig.selectedClusterColor) continue;
            } else if (e.getCurrentValue() == 0) continue;
            double avgNodeTime = (Double.parseDouble(MyMathUtil.twoDecimalFormat(e.getAverageTime())));
            if (avgEdgeTimeMap.containsKey(avgNodeTime)) {
                avgEdgeTimeMap.put(avgNodeTime, avgEdgeTimeMap.get(avgNodeTime)+1);
            } else {
                avgEdgeTimeMap.put(avgNodeTime, 1L);
            }
        }

        for (Double avgNodeTime : avgEdgeTimeMap.keySet()) {
            avgEdgeTimeDistributionSeries.add(avgNodeTime, avgEdgeTimeMap.get(avgNodeTime));
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(avgEdgeTimeDistributionSeries);

        JFreeChart chart = ChartFactory.createXYLineChart("", "AVG. EDGE TIME", "", dataset);
        chart.getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
        chart.getXYPlot().setBackgroundPaint(Color.WHITE);
        chart.getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
        chart.getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);
        chart.getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
        chart.getXYPlot().getDomainAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
        chart.getXYPlot().getDomainAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
        chart.getXYPlot().getRangeAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
        chart.getXYPlot().getRangeAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
        chart.getXYPlot().getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        chart.setBackgroundPaint(Color.WHITE);

        return chart;
    }

    private JFreeChart setAverageNodeHopChart() {
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        TreeMap<Integer, Integer> nodeHopCountMap = new TreeMap<>();
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0) continue;
            int cnt = 0;
            int totalHop = 0;
            for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                for (int itemsetIdx = 0; itemsetIdx < MySequentialGraphVars.seqs[s].length; itemsetIdx++) {
                    String nn = MySequentialGraphVars.seqs[s][itemsetIdx].split(":")[0];
                    if (nn.equals(n.getName())) {
                        int hopCount = (MySequentialGraphVars.seqs[s].length - (itemsetIdx + 1));
                        if (hopCount > 0) {
                            totalHop += hopCount;
                            cnt++;
                        }
                    }
                }
            }
            if (cnt > 0) {
                int avgHop = (int)((double)totalHop/cnt);
                if (nodeHopCountMap.containsKey(avgHop)) {
                    nodeHopCountMap.put(avgHop, nodeHopCountMap.get(avgHop)+1);
                } else {
                    nodeHopCountMap.put(avgHop, 1);
                }
            }
        }

        XYSeries avgHopSeries = new XYSeries("AVG. N. HOP");
        for (Integer avgHop : nodeHopCountMap.keySet()) {
            avgHopSeries.add(avgHop, nodeHopCountMap.get(avgHop));
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(avgHopSeries);

        JFreeChart chart = ChartFactory.createXYLineChart("", "AVG. NODE HOPE", "", dataset);
        chart.getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
        chart.getXYPlot().setBackgroundPaint(Color.WHITE);
        chart.getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
        chart.getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);
        chart.getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
        chart.getXYPlot().getDomainAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
        chart.getXYPlot().getDomainAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
        chart.getXYPlot().getRangeAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
        chart.getXYPlot().getRangeAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
        chart.getXYPlot().getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        chart.setBackgroundPaint(Color.WHITE);

        return chart;
    }

    public void decorate() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                try {
                    removeAll();
                    setLayout(new BorderLayout(3, 3));
                    setBackground(Color.WHITE);

                    ChartPanel chartPanel = null;
                    if (selectedChartIdx == 0) {
                        chartPanel = new ChartPanel(setAverageShortestDistanceChart());
                    } else if (selectedChartIdx == 1) {
                        chartPanel = new ChartPanel(setAverageRecurrenceLengthChart());
                    } else if (selectedChartIdx == 2) {
                        chartPanel = new ChartPanel(setAverageNodeReachTimeChart());
                    } else if (selectedChartIdx == 3) {
                        chartPanel = new ChartPanel(setAverageEdgeTimeChart());
                    } else if (selectedChartIdx == 4) {
                        chartPanel = new ChartPanel(setAverageNodeHopChart());
                    } else if (selectedChartIdx == 5) {
                        chartPanel = new ChartPanel(setAverageNodeRecurrenceTimeChart());
                    } else if (selectedChartIdx == 6) {
                        chartPanel = new ChartPanel(setAverageNodeDurationChart());
                    }
                    chartPanel.setPreferredSize(new Dimension(350, 367));

                    XYPlot plot = (XYPlot) chartPanel.getChart().getPlot();
                    XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
                    renderer.setSeriesPaint(0, Color.DARK_GRAY);
                    renderer.setSeriesStroke(0, new BasicStroke(1.5f));
                    renderer.setSeriesShapesVisible(0, true);
                    renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                    renderer.setSeriesFillPaint(0, Color.WHITE);
                    renderer.setUseFillPaint(true);

                    JLabel titleLabel = new JLabel(" AVG. DIST.");
                    titleLabel.setToolTipText("AVERAGE VALUE DISTRIBUTION");
                    titleLabel.setFont(MySequentialGraphVars.tahomaBoldFont11);
                    titleLabel.setBackground(Color.WHITE);
                    titleLabel.setForeground(Color.DARK_GRAY);

                    JPanel titlePanel = new JPanel();
                    titlePanel.setBackground(Color.WHITE);
                    titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
                    titlePanel.add(titleLabel);

                    JButton enlargeBtn = new JButton("+");
                    enlargeBtn.setFont(MySequentialGraphVars.tahomaPlainFont11);
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

                    averageChartMenu = new JComboBox();
                    averageChartMenu.setFocusable(false);
                    averageChartMenu.setFont(MySequentialGraphVars.tahomaPlainFont11);
                    averageChartMenu.setBackground(Color.WHITE);
                    averageChartMenu.setToolTipText("SELECT AN AVERAGE VALUE DISTRIBUTION");
                    averageChartMenu.addItem("AVG. S. D.");
                    averageChartMenu.addItem("AVG. R. L.");
                    averageChartMenu.addItem("AVG. N. H.");
                    if (MySequentialGraphVars.isTimeOn) {
                        averageChartMenu.addItem("AVG. N. R. T.");
                        averageChartMenu.addItem("AVG. E. T.");
                        averageChartMenu.addItem("AVG. N. RC. T.");
                        averageChartMenu.addItem("AVG. D.");
                    }
                    String[] chartMenuTooltips = new String[7];
                    chartMenuTooltips[0] = "AVERAGE SHORTEST DISTANCE";
                    chartMenuTooltips[1] = "AVERAGE NODE RECURRENCE LENGTH";
                    chartMenuTooltips[2] = "AVERAGE NODE HOPE";

                    if (MySequentialGraphVars.isTimeOn) {
                        chartMenuTooltips[3] = "AVERAGE NODE REACH TIME";
                        chartMenuTooltips[4] = "AVERAGE EDGE TIME";
                        chartMenuTooltips[5] = "AVERAGE NODE RECURRENCE TIME";
                        chartMenuTooltips[6] = "AVERAGE NODE DURATION";
                    }

                    averageChartMenu.setRenderer(new MyComboBoxTooltipRenderer(chartMenuTooltips));
                    averageChartMenu.setSelectedIndex(selectedChartIdx);
                    averageChartMenu.addActionListener(new ActionListener() {
                        @Override public void actionPerformed(ActionEvent e) {
                            new Thread(new Runnable() {
                                @Override public void run() {
                                    selectedChartIdx = averageChartMenu.getSelectedIndex();
                                    decorate();
                                }
                            }).start();
                        }
                    });

                    JPanel menuPanel = new JPanel();
                    menuPanel.setBackground(Color.WHITE);
                    menuPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 3));
                    menuPanel.add(averageChartMenu);
                    menuPanel.add(enlargeBtn);

                    JPanel topPanel = new JPanel();
                    topPanel.setLayout(new BorderLayout(3, 3));
                    topPanel.setBackground(Color.WHITE);
                    topPanel.add(titlePanel, BorderLayout.WEST);
                    topPanel.add(menuPanel, BorderLayout.EAST);
                    add(topPanel, BorderLayout.NORTH);
                    add(chartPanel, BorderLayout.CENTER);
                    renderer.setBaseLegendTextFont(MySequentialGraphVars.tahomaPlainFont11);

                    revalidate();
                    repaint();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

    }

    public void enlarge() {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            JFrame f = new JFrame(" NODE & EDGE AVERAGE DISTRIBUTION");
            f.setLayout(new BorderLayout(3, 3));
            f.getContentPane().add( new MyGraphLevelAverageDistributionLineChart(), BorderLayout.CENTER);
            f.setPreferredSize(new Dimension(450, 350));
            f.pack();
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.setAlwaysOnTop(true);
            f.addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    f.setAlwaysOnTop(true);
                }
                @Override public void mouseExited(MouseEvent e) {
                    super.mouseEntered(e);
                    f.setAlwaysOnTop(false);
                }
            });
            pb.updateValue(100, 100);
            pb.dispose();
            f.setVisible(true);
            f.setAlwaysOnTop(false);
        } catch (Exception ex) {
            pb.updateValue(100, 100);
            pb.dispose();
        }
    }
}
