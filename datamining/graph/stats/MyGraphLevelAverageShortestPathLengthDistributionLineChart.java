package datamining.graph.stats;

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
import java.util.HashMap;
import java.util.Map;

public class MyGraphLevelAverageShortestPathLengthDistributionLineChart
extends JPanel {

    public static int instances = 0;

    public MyGraphLevelAverageShortestPathLengthDistributionLineChart() {
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
                    XYSeries avgShortestPathLengthDistributionSeries = new XYSeries("AVG. SHORTEST PATH LENGTH");
                    Collection<MyNode> nodes = MyVars.g.getVertices();
                    Map<Double, Long> avgShortestPathLengthMap = new HashMap<>();
                    for (MyNode n : nodes) {
                        if (n.getCurrentValue() == 0) continue;
                        double avgShortestPathLen = (Double.parseDouble(MyMathUtil.twoDecimalFormat(n.getAverageShortestPathLength())));
                        if (avgShortestPathLengthMap.containsKey(avgShortestPathLen)) {
                            avgShortestPathLengthMap.put(avgShortestPathLen, avgShortestPathLengthMap.get(avgShortestPathLen)+1);
                        } else {
                            avgShortestPathLengthMap.put(avgShortestPathLen, 1L);
                        }
                    }

                    for (Double avgShortestPathLen : avgShortestPathLengthMap.keySet()) {
                        avgShortestPathLengthDistributionSeries.add(avgShortestPathLen, avgShortestPathLengthMap.get(avgShortestPathLen));
                    }

                    dataset.addSeries(avgShortestPathLengthDistributionSeries);

                    JFreeChart chart = ChartFactory.createXYLineChart("", "AVG. SHORTEST PATH LENGTH", "", dataset);
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
                    chart.setBackgroundPaint(Color.WHITE);

                    XYPlot plot = (XYPlot) chart.getPlot();
                    XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
                    renderer.setSeriesPaint(0, Color.BLACK);
                    renderer.setSeriesStroke(0, new BasicStroke(1.6f));
                    renderer.setSeriesShapesVisible(0, true);
                    renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                    renderer.setSeriesFillPaint(0, Color.WHITE);
                    renderer.setUseFillPaint(true);

                    ChartPanel chartPanel = new ChartPanel(chart);
                    chartPanel.setPreferredSize(new Dimension(350, 367));

                    JLabel titleLabel = new JLabel(" AVG. SHORTEST PATH L.");
                    titleLabel.setToolTipText("AVERAGE SHORTEST PATH LENGTHS");
                    titleLabel.setFont(MyVars.tahomaBoldFont11);
                    titleLabel.setBackground(Color.WHITE);
                    titleLabel.setForeground(Color.DARK_GRAY);

                    JPanel titlePanel = new JPanel();
                    titlePanel.setBackground(Color.WHITE);
                    titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
                    titlePanel.add(titleLabel);

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
                    enlargePanel.add(enlargeBtn);

                    JPanel topPanel = new JPanel();
                    topPanel.setLayout(new BorderLayout(3, 3));
                    topPanel.setBackground(Color.WHITE);
                    topPanel.add(titlePanel, BorderLayout.WEST);
                    topPanel.add(enlargePanel, BorderLayout.EAST);
                    add(topPanel, BorderLayout.NORTH);
                    add(chartPanel, BorderLayout.CENTER);
                    renderer.setBaseLegendTextFont(MyVars.tahomaPlainFont10);
                    revalidate();
                    repaint();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

    }

    public void enlarge() {
        MyGraphLevelAverageShortestPathLengthDistributionLineChart betweenPathLengthDistributions = new MyGraphLevelAverageShortestPathLengthDistributionLineChart();
        betweenPathLengthDistributions.setLayout(new BorderLayout(3,3));
        betweenPathLengthDistributions.setBackground(Color.WHITE);

        JFrame distFrame = new JFrame(" AVERAGE SHORTEST PATH LENGTH DISTRIBUTION");
        distFrame.setLayout(new BorderLayout(3,3));
        distFrame.getContentPane().add(betweenPathLengthDistributions, BorderLayout.CENTER);
        distFrame.setPreferredSize(new Dimension(450, 350));
        distFrame.pack();
        distFrame.setVisible(true);
    }
}
