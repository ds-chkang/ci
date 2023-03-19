package datamining.graph.stats;

import datamining.graph.MyComboBoxTooltipRenderer;
import datamining.graph.MyNode;
import datamining.utils.MyMathUtil;
import datamining.utils.system.MyVars;
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

public class MyGraphLevelNodeCountDistributionLineChart
extends JPanel {

    public static int instances = 0;

    public MyGraphLevelNodeCountDistributionLineChart() {
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
                    XYSeries openNodeCountSeries = new XYSeries("O. N. CNT.");
                    TreeMap<Integer, Integer> openNodeCountByValueMap = new TreeMap<>();
                    Collection<MyNode> nodes = MyVars.g.getVertices();
                    int totalCnt = 0;
                    int totalValue = 0;
                    for (MyNode n : nodes) {
                        if (n.getCurrentValue() == 0) continue;
                        int value = n.getOpenNodeCount();
                        totalCnt++;
                        totalValue += value;
                        if (openNodeCountByValueMap.containsKey(value)) {
                            openNodeCountByValueMap.put(value, openNodeCountByValueMap.get(value) + 1);
                        } else {
                            openNodeCountByValueMap.put(value, 1);
                        }
                    }

                    for (Integer value : openNodeCountByValueMap.keySet()) {
                        openNodeCountSeries.add(value, openNodeCountByValueMap.get(value));
                    }
                    dataset.addSeries(openNodeCountSeries);

                    XYSeries endNodeCountSeries = new XYSeries("E. N. CNT.");
                    TreeMap<Integer, Integer> endNodeCountByValueMap = new TreeMap<>();
                    totalCnt = 0;
                    totalValue = 0;
                    for (MyNode n : nodes) {
                        if (n.getCurrentValue() == 0) continue;
                        int value = n.getEndNodeCount();
                        totalCnt++;
                        totalValue += value;
                        if (endNodeCountByValueMap.containsKey(value)) {
                            endNodeCountByValueMap.put(value, endNodeCountByValueMap.get(value) + 1);
                        } else {
                            endNodeCountByValueMap.put(value, 1);
                        }
                    }

                    for (Integer value : endNodeCountByValueMap.keySet()) {
                        endNodeCountSeries.add(value, endNodeCountByValueMap.get(value));
                    }

                    XYSeries directRecurrenceNodeCountSeries = new XYSeries("R. CNT.");
                    TreeMap<Integer, Integer> reccurrenceNodeCountByValueMap = new TreeMap<>();
                    totalCnt = 0;
                    totalValue = 0;
                    for (MyNode n : nodes) {
                        if (n.getCurrentValue() == 0) continue;
                        int value = n.getDirectRecurrenceCount();
                        totalCnt++;
                        totalValue += value;
                        if (reccurrenceNodeCountByValueMap.containsKey(value)) {
                            reccurrenceNodeCountByValueMap.put(value, reccurrenceNodeCountByValueMap.get(value) + 1);
                        } else {
                            reccurrenceNodeCountByValueMap.put(value, 1);
                        }
                    }

                    for (Integer value : reccurrenceNodeCountByValueMap.keySet()) {
                        directRecurrenceNodeCountSeries.add(value, reccurrenceNodeCountByValueMap.get(value));
                    }

                    TreeMap<Integer, Integer> recurrencePeriodMap = new TreeMap<>();
                    totalCnt = 0;
                    for (MyNode n : nodes) {
                        if (n.getCurrentValue() == 0) continue;
                        int value = (int)n.getAverageRecurrenceLength();
                        if (value > 0) totalCnt++;
                        if (recurrencePeriodMap.containsKey(value)) {
                            recurrencePeriodMap.put(value, recurrencePeriodMap.get(value) + 1);
                        } else {
                            recurrencePeriodMap.put(value, 1);
                        }
                    }
                    XYSeries avgRecurrencePeriodSeries = new XYSeries("AVG. N. R. [" + MyMathUtil.getCommaSeperatedNumber(totalCnt) + " NODES.]");

                    for (Integer value : recurrencePeriodMap.keySet()) {
                        avgRecurrencePeriodSeries.add(value, recurrencePeriodMap.get(value));
                    }

                    TreeMap<Integer, Integer> itemsetLengthMap = new TreeMap<>();
                    totalCnt = 0;
                    for (MyNode n : nodes) {
                        if (n.getCurrentValue() == 0) continue;
                        int value = n.getName().split(",").length;
                        if (value > 0) totalCnt++;
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

                    TreeMap<Integer, Integer> nodeHopCountByNodeMap = new TreeMap<>();
                    for (MyNode n : nodes) {
                        if (n.getCurrentValue() == 0) continue;
                        int cnt = 0;
                        int totalHop = 0;
                        for (int s = 0; s < MyVars.seqs.length; s++) {
                            for (int itemsetIdx = 0; itemsetIdx < MyVars.seqs[s].length; itemsetIdx++) {
                                String nn = MyVars.seqs[s][itemsetIdx].split(":")[0];
                                if (nn.equals(n.getName())) {
                                    int hopCount = (MyVars.seqs[s].length - (itemsetIdx + 1));
                                    if (hopCount > 0) {
                                        totalHop += hopCount;
                                        cnt++;
                                    }
                                }
                            }
                        }
                        if (cnt > 0) {
                            int avgHop = (int)((double)totalHop/cnt);
                            if (nodeHopCountByNodeMap.containsKey(avgHop)) {
                                nodeHopCountByNodeMap.put(avgHop, nodeHopCountByNodeMap.get(avgHop)+1);
                            } else {
                                nodeHopCountByNodeMap.put(avgHop, 1);
                            }
                        }
                    }

                    XYSeries avgHopSeries = new XYSeries("AVG. N. HOP");
                    for (Integer avgHop : nodeHopCountByNodeMap.keySet()) {
                        avgHopSeries.add(avgHop, nodeHopCountByNodeMap.get(avgHop));
                    }

                    JFreeChart chart = ChartFactory.createXYLineChart("", "COUNT", "", dataset);
                    chart.getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
                    chart.getXYPlot().setBackgroundPaint(Color.WHITE);
                    chart.getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
                    chart.getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);
                    chart.getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
                    chart.getXYPlot().getDomainAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
                    chart.getXYPlot().getDomainAxis().setTickLabelFont(MyVars.tahomaPlainFont10);
                    chart.getXYPlot().getRangeAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
                    chart.getXYPlot().getRangeAxis().setTickLabelFont(MyVars.tahomaPlainFont10);
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

                    JLabel titleLabel = new JLabel(" N. C.");
                    titleLabel.setToolTipText("NODE COUNT DISTRIBUTION");
                    titleLabel.setFont(MyVars.tahomaBoldFont11);
                    titleLabel.setBackground(Color.WHITE);
                    titleLabel.setForeground(Color.DARK_GRAY);

                    JPanel titlePanel = new JPanel();
                    titlePanel.setBackground(Color.WHITE);
                    titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
                    titlePanel.add(titleLabel);

                    JComboBox chartOption = new JComboBox();
                    chartOption.setToolTipText("SELECT A CHART FOR DISTRIBUTION");
                    String[] tooltips = {"SELECT A CHART FOR A DEPTH DISTRIBUTION.",
                            "OPEN NODE COUNT DISTRIBUTION.",
                            "ENDING NODE COUNT DISTRIBUTION.",
                            "DIRECT RECURRENCE COUNT DISTRIBUTION.",
                            "AVERAGE RECURRENCE PERIOD DISTRIBUTION.",
                            "ITEM LENGTH DISTRIBUTION.",
                            "AVERAGE NODE HOP DISTRIBUTION."};
                    chartOption.setRenderer(new MyComboBoxTooltipRenderer(tooltips));
                    chartOption.setFocusable(false);
                    chartOption.setBackground(Color.WHITE);
                    chartOption.setFont(MyVars.tahomaPlainFont10);
                    chartOption.addItem("O. N."); // Open Node Count.
                    chartOption.addItem("E. N."); // Ending Node Count.
                    chartOption.addItem("D. R."); // Direct Recurrence Count.
                    chartOption.addItem("AVG. RP."); // Average recurrence period.
                    chartOption.addItem("ITEM L."); // Item length.
                    chartOption.addItem("AVG. H. C."); // Average hop count.
                    //chartOption.addItem("PATH L. DIST."); // Path length length.
                    chartOption.addActionListener(new ActionListener() {
                        @Override public void actionPerformed(ActionEvent e) {
                            if (chartOption.getSelectedIndex() == 0) {
                                dataset.removeAllSeries();
                                dataset.addSeries(openNodeCountSeries);
                            } else if (chartOption.getSelectedIndex() == 1) {
                                dataset.removeAllSeries();
                                dataset.addSeries(endNodeCountSeries);
                            } else if (chartOption.getSelectedIndex() == 2) {
                                dataset.removeAllSeries();
                                dataset.addSeries(directRecurrenceNodeCountSeries);
                            } else if (chartOption.getSelectedIndex() == 3) {
                                dataset.removeAllSeries();
                                dataset.addSeries(avgRecurrencePeriodSeries);
                            } else if (chartOption.getSelectedIndex() == 4) {
                                dataset.removeAllSeries();
                                dataset.addSeries(itemsetLengthSeries);
                            } else if (chartOption.getSelectedIndex() == 5) {
                                dataset.removeAllSeries();
                                dataset.addSeries(avgHopSeries);
                            }
                        }
                    });



                    JButton enlargeBtn = new JButton("+");
                    enlargeBtn.setFont(MyVars.tahomaPlainFont10);
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
                    renderer.setBaseLegendTextFont(MyVars.tahomaPlainFont10);
                    revalidate();
                    repaint();
                } catch (Exception ex) {}
            }
        });

    }

    public void enlarge() {
        MyGraphLevelNodeCountDistributionLineChart nodeCountDistributionLineChart = new MyGraphLevelNodeCountDistributionLineChart();
        nodeCountDistributionLineChart.setLayout(new BorderLayout(3,3));
        nodeCountDistributionLineChart.setBackground(Color.WHITE);

        JFrame distFrame = new JFrame(" NODE COUNT DISTRIBUTION");
        distFrame.setLayout(new BorderLayout(3,3));
        distFrame.getContentPane().add(nodeCountDistributionLineChart, BorderLayout.CENTER);
        distFrame.setPreferredSize(new Dimension(450, 350));
        distFrame.pack();
        distFrame.setVisible(true);
    }
}
