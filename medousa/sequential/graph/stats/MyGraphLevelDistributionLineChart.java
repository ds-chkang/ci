package medousa.sequential.graph.stats;

import medousa.sequential.graph.MyComboBoxTooltipRenderer;
import medousa.sequential.graph.MyNode;
import medousa.MyProgressBar;
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
import java.awt.geom.Ellipse2D;
import java.util.Collection;
import java.util.TreeMap;

public class MyGraphLevelDistributionLineChart
extends JPanel {

    public static int instances = 0;

    public MyGraphLevelDistributionLineChart() {
        this.decorate();
    }

    public void decorate() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                try {
                    removeAll();
                    setLayout(new BorderLayout(3, 3));
                    setBackground(Color.WHITE);

                    XYSeriesCollection dataset = new XYSeriesCollection();
                    XYSeries initialNodeCountSeries = new XYSeries("O. N. CNT.");
                    TreeMap<Integer, Integer> openNodeCountMap = new TreeMap<>();
                    Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
                    for (MyNode n : nodes) {
                        if (n.getCurrentValue() <= 0) continue;
                        int value = n.getStartPositionNodeCount();
                        if (openNodeCountMap.containsKey(value)) {
                            openNodeCountMap.put(value, openNodeCountMap.get(value) + 1);
                        } else {
                            openNodeCountMap.put(value, 1);
                        }
                    }

                    for (Integer value : openNodeCountMap.keySet()) {
                        initialNodeCountSeries.add(value, openNodeCountMap.get(value));
                    }
                    dataset.addSeries(initialNodeCountSeries);

                    XYSeries terminatingNodeCountSeries = new XYSeries("END. N. CNT.");
                    TreeMap<Integer, Integer> endNodeCountMap = new TreeMap<>();
                    for (MyNode n : nodes) {
                        if (n.getCurrentValue() <= 0) continue;
                        int value = n.getEndPositionNodeCount();
                        if (endNodeCountMap.containsKey(value)) {
                            endNodeCountMap.put(value, endNodeCountMap.get(value) + 1);
                        } else {
                            endNodeCountMap.put(value, 1);
                        }
                    }

                    for (Integer value : endNodeCountMap.keySet()) {
                        terminatingNodeCountSeries.add(value, endNodeCountMap.get(value));
                    }

                    XYSeries recurrenceNodeCountSeries = new XYSeries("R. CNT.");
                    TreeMap<Integer, Integer> reccurrenceCountMap = new TreeMap<>();
                    for (MyNode n : nodes) {
                        if (n.getCurrentValue() <= 0) continue;
                        int value = n.getTotalNodeRecurrentCount();
                        if (reccurrenceCountMap.containsKey(value)) {
                            reccurrenceCountMap.put(value, reccurrenceCountMap.get(value) + 1);
                        } else {
                            reccurrenceCountMap.put(value, 1);
                        }
                    }

                    for (Integer value : reccurrenceCountMap.keySet()) {
                        recurrenceNodeCountSeries.add(value, reccurrenceCountMap.get(value));
                    }

                    XYSeries avgRecurrenceLengthSeries = new XYSeries("AVG. RECURRENCE LENGTH");
                    TreeMap<Integer, Integer> avgRecurrentLengthMap = new TreeMap<>();
                    for (MyNode n : nodes) {
                        if (n.getCurrentValue() <= 0) continue;
                        int value = (int)n.getAverageRecurrenceLength();
                        if (avgRecurrentLengthMap.containsKey(value)) {
                            avgRecurrentLengthMap.put(value, avgRecurrentLengthMap.get(value) + 1);
                        } else {
                            avgRecurrentLengthMap.put(value, 1);
                        }
                    }


                    for (Integer value : avgRecurrentLengthMap.keySet()) {
                        avgRecurrenceLengthSeries.add(value, avgRecurrentLengthMap.get(value));
                    }

                    TreeMap<Integer, Integer> itemsetLengthMap = new TreeMap<>();
                    for (MyNode n : nodes) {
                        if (n.getCurrentValue() <= 0) continue;
                        int value = n.getName().split(",").length;
                        if (itemsetLengthMap.containsKey(value)) {
                            itemsetLengthMap.put(value, itemsetLengthMap.get(value) + 1);
                        } else {
                            itemsetLengthMap.put(value, 1);
                        }
                    }
                    XYSeries itemsetLengthSeries = new XYSeries("ITEM L.");
                    for (Integer value : itemsetLengthMap.keySet()) {
                        itemsetLengthSeries.add(value, itemsetLengthMap.get(value));
                    }

                    TreeMap<Integer, Integer> totalNodeReucrrenceTimeMap = new TreeMap<>();
                    for (MyNode n : nodes) {
                        if (n.getCurrentValue() <= 0) continue;
                        int value = n.getName().split(",").length;
                        if (totalNodeReucrrenceTimeMap.containsKey(value)) {
                            totalNodeReucrrenceTimeMap.put(value, totalNodeReucrrenceTimeMap.get(value) + 1);
                        } else {
                            totalNodeReucrrenceTimeMap.put(value, 1);
                        }
                    }
                    XYSeries totalNodeReucrrenceTimeSeries = new XYSeries("TOTAl RECUR. TIME");
                    for (Integer value : totalNodeReucrrenceTimeMap.keySet()) {
                        totalNodeReucrrenceTimeSeries.add(value, totalNodeReucrrenceTimeMap.get(value));
                    }

                    JFreeChart chart = ChartFactory.createXYLineChart("", "COUNT", "", dataset);
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
                    chart.getXYPlot().getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
                    chart.setBackgroundPaint(Color.WHITE);

                    XYPlot plot = (XYPlot) chart.getPlot();
                    XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
                    renderer.setSeriesPaint(0, Color.DARK_GRAY);
                    renderer.setSeriesStroke(0, new BasicStroke(1.5f));
                    renderer.setSeriesShapesVisible(0, true);
                    renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                    renderer.setSeriesFillPaint(0, Color.WHITE);
                    renderer.setUseFillPaint(true);

                    ChartPanel chartPanel = new ChartPanel(chart);
                    chartPanel.setPreferredSize(new Dimension(350, 367));

                    JLabel titleLabel = new JLabel(" DIST.");
                    titleLabel.setToolTipText("NODE COUNT DISTRIBUTION");
                    titleLabel.setFont(MySequentialGraphVars.tahomaBoldFont11);
                    titleLabel.setBackground(Color.WHITE);
                    titleLabel.setForeground(Color.DARK_GRAY);

                    JPanel titlePanel = new JPanel();
                    titlePanel.setBackground(Color.WHITE);
                    titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
                    titlePanel.add(titleLabel);

                    JComboBox chartOption = new JComboBox();
                    chartOption.setToolTipText("SELECT A CHART FOR NODE COUNT DISTRIBUTION");
                    String[] tooltips = {
                            "INITIAL NODE COUNT DISTRIBUTION",
                            "TERMINATING NODE COUNT DISTRIBUTION",
                            "RECURRENCE COUNT DISTRIBUTION",
                            "ITEM LENGTH DISTRIBUTION",
                            "NODE TOTAl RECURRENCE TIME"};
                    chartOption.setRenderer(new MyComboBoxTooltipRenderer(tooltips));
                    chartOption.setFocusable(false);
                    chartOption.setBackground(Color.WHITE);
                    chartOption.setFont(MySequentialGraphVars.tahomaPlainFont11);
                    chartOption.addItem("I. N."); // Open Node Count.
                    chartOption.addItem("T. N."); // Ending Node Count.
                    chartOption.addItem("RECUR."); // Direct Recurrence Count.
                    chartOption.addItem("ITEM L."); // Item length.
                    chartOption.addItem("T. RECUR. T."); // Total recurrence time.
                    chartOption.addActionListener(new ActionListener() {
                        @Override public void actionPerformed(ActionEvent e) {
                            if (chartOption.getSelectedIndex() == 0) {
                                dataset.removeAllSeries();
                                dataset.addSeries(initialNodeCountSeries);
                            } else if (chartOption.getSelectedIndex() == 1) {
                                dataset.removeAllSeries();
                                dataset.addSeries(terminatingNodeCountSeries);
                            } else if (chartOption.getSelectedIndex() == 2) {
                                dataset.removeAllSeries();
                                dataset.addSeries(recurrenceNodeCountSeries);
                            } else if (chartOption.getSelectedIndex() == 3) {
                                dataset.removeAllSeries();
                                dataset.addSeries(itemsetLengthSeries);
                            } else if (chartOption.getSelectedIndex() == 4) {
                                dataset.removeAllSeries();
                                dataset.addSeries(totalNodeReucrrenceTimeSeries);
                            }
                        }
                    });

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

                    JPanel enlargePanel = new JPanel();
                    enlargePanel.setBackground(Color.WHITE);
                    enlargePanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 3));
                    enlargePanel.add(chartOption);
                    enlargePanel.add(enlargeBtn);

                    JPanel topPanel = new JPanel();
                    topPanel.setLayout(new BorderLayout(3, 3));
                    topPanel.setBackground(Color.WHITE);
                    topPanel.add(titlePanel, BorderLayout.WEST);
                    topPanel.add(enlargePanel, BorderLayout.CENTER);
                    add(topPanel, BorderLayout.NORTH);
                    add(chartPanel, BorderLayout.CENTER);
                    renderer.setBaseLegendTextFont(MySequentialGraphVars.tahomaPlainFont11);

                    revalidate();
                    repaint();
                } catch (Exception ex) {}
            }
        });

    }

    public void enlarge() {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            JFrame f = new JFrame(" NODE COUNT DISTRIBUTION");
            f.setLayout(new BorderLayout(3, 3));
            f.getContentPane().add(new MyGraphLevelDistributionLineChart(), BorderLayout.CENTER);
            f.setPreferredSize(new Dimension(450, 350));
            f.pack();
            f.setCursor(Cursor.HAND_CURSOR);
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
}
