package datamining.graph.path;

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
import java.util.HashMap;
import java.util.Map;

public class MyNodePathGraphNodeMinContributionDistributionLinChart
extends JPanel {

    public static int instances = 0;
    private Map<Integer, Map<String, Integer>> minNodeContributionByDepthMap;

    public MyNodePathGraphNodeMinContributionDistributionLinChart(Map<Integer, Map<String, Integer>> minNodeContributionByDepthMap) {
        this.minNodeContributionByDepthMap = minNodeContributionByDepthMap;
        decorate();
    }

    public void decorate() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                try {
                    removeAll();
                    setLayout(new BorderLayout(3, 3));
                    setBackground(Color.WHITE);

                    Map<Integer, Integer> filteredMinNodeContributionMap = new HashMap<>();
                    for (Integer depth : minNodeContributionByDepthMap.keySet()) {
                        int min = 2000000000;
                        Map<String, Integer> nodeContMap = minNodeContributionByDepthMap.get(depth);
                        for (String n : nodeContMap.keySet()) {
                            int nodeCont = nodeContMap.get(n);
                            if (nodeCont < min) {
                                min = nodeCont;
                            }
                        }
                        filteredMinNodeContributionMap.put(depth, min);
                    }

                    XYSeriesCollection dataset = new XYSeriesCollection();
                    XYSeries nodesByDepthSeries = new XYSeries("MIN. N. CONT.");
                    for (Integer depth : filteredMinNodeContributionMap.keySet()) {
                        nodesByDepthSeries.add((double)depth, filteredMinNodeContributionMap.get(depth));
                    }
                    dataset.addSeries(nodesByDepthSeries);

                    JFreeChart chart = ChartFactory.createXYLineChart("", "MIN. N. CONT.", "", dataset);
                    chart.getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
                    chart.getXYPlot().setBackgroundPaint(Color.WHITE);
                    chart.getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
                    chart.getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);
                    chart.getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
                    chart.getXYPlot().getDomainAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
                    chart.getXYPlot().getDomainAxis().setTickLabelFont(MyVars.tahomaPlainFont10);
                    chart.getXYPlot().getRangeAxis().setTickLabelFont(MyVars.tahomaPlainFont10);
                    chart.getXYPlot().getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
                    chart.getXYPlot().getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
                    chart.setBackgroundPaint(Color.WHITE);

                    XYPlot plot = (XYPlot)chart.getPlot();
                    XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
                    renderer.setSeriesPaint(0, Color.DARK_GRAY);
                    renderer.setSeriesStroke(0, new BasicStroke(1.5f));
                    renderer.setSeriesShapesVisible(0, true);
                    renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                    renderer.setSeriesFillPaint(0, Color.WHITE);
                    renderer.setUseFillPaint(true);

                    ChartPanel chartPanel = new ChartPanel( chart );
                    chartPanel.setPreferredSize( new Dimension( 600 , 100 ) );

                    JLabel titleLabel = new JLabel(" MIN. N. CONT.");
                    titleLabel.setFont(MyVars.tahomaBoldFont11);
                    titleLabel.setBackground(Color.WHITE);
                    titleLabel.setForeground(Color.DARK_GRAY);

                    JButton enlargeBtn = new JButton("ENLARGE");
                    enlargeBtn.setFont(MyVars.tahomaPlainFont10);
                    enlargeBtn.setBackground(Color.WHITE);
                    enlargeBtn.setFocusable(false);
                    enlargeBtn.addActionListener(new ActionListener() {@Override public void actionPerformed(ActionEvent e) {enlarge();}});

                    JPanel enlargePanel = new JPanel();
                    enlargePanel.setBackground(Color.WHITE);
                    enlargePanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 3));
                    enlargePanel.add(enlargeBtn);

                    JPanel topPanel = new JPanel();
                    topPanel.setLayout(new BorderLayout(3, 3));
                    topPanel.setBackground(Color.WHITE);
                    topPanel.add(titleLabel, BorderLayout.WEST);
                    topPanel.add(enlargePanel, BorderLayout.EAST);
                    add(topPanel, BorderLayout.NORTH);
                    instances++;
                    renderer.setBaseLegendTextFont(MyVars.tahomaPlainFont10);
                    add(chartPanel, BorderLayout.CENTER);
                } catch (Exception ex) {ex.printStackTrace();}
            }
        });
    }

    public void enlarge() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                try {
                    MyNodePathGraphNodeMinContributionDistributionLinChart minContributionDistributionLinChart = new MyNodePathGraphNodeMinContributionDistributionLinChart(minNodeContributionByDepthMap);
                    JFrame frame = new JFrame(" MINIMUM NODE CONTRIBUTION BY DEPTH");
                    frame.setLayout(new BorderLayout(3,3));
                    frame.getContentPane().add(minContributionDistributionLinChart, BorderLayout.CENTER);
                    frame.setPreferredSize(new Dimension(450, 350));
                    frame.pack();
                    frame.setVisible(true);
                } catch (Exception ex) {ex.printStackTrace();}
            }
        });
    }
}
