package datamining.graph.path;


import datamining.graph.MyDirectEdge;
import datamining.graph.MyDirectMarkovChain;
import datamining.utils.MyMathUtil;
import datamining.utils.system.MySysUtil;
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

public class MyPathGraphEdgeContributionDistributionLineChart
extends JPanel {

    private MyDirectMarkovChain directedSparseMultigraph;
    public static int instances = 0;

    public MyPathGraphEdgeContributionDistributionLineChart(MyDirectMarkovChain directedSparseMultigraph) {
        this.directedSparseMultigraph = directedSparseMultigraph;
        this.decorate();
    }

    public void decorate() {
        try {
            removeAll();
            setLayout(new BorderLayout(3, 3));
            setBackground(Color.WHITE);

            if (this.directedSparseMultigraph == null) {
                JLabel titleLabel = new JLabel(" EDGE CONT.");
                titleLabel.setFont(MyVars.tahomaBoldFont11);
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
                msg.setFont(MyVars.tahomaPlainFont12);
                msg.setBackground(Color.WHITE);
                msg.setHorizontalAlignment(JLabel.CENTER);
                this.add(topPanel, BorderLayout.NORTH);
                this.add(msg, BorderLayout.CENTER);
            } else {
                int total = 0;
                Collection<MyDirectEdge> edges = this.directedSparseMultigraph.getEdges();
                for (MyDirectEdge e : edges) {
                    if (e.getContribution() > 0) {
                        total = 1;
                        break;

                    }
                }
                if (total == 0) {
                    JLabel titleLabel = new JLabel(" EDGE CONT.");
                    titleLabel.setFont(MyVars.tahomaBoldFont11);
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
                    msg.setFont(MyVars.tahomaPlainFont12);
                    msg.setBackground(Color.WHITE);
                    msg.setHorizontalAlignment(JLabel.CENTER);
                    this.add(topPanel, BorderLayout.NORTH);
                    this.add(msg, BorderLayout.CENTER);
                } else {
                    XYSeriesCollection dataset = new XYSeriesCollection();
                    TreeMap<Long, Integer> mapByEdgeValue = new TreeMap<>();
                    edges = directedSparseMultigraph.getEdges();
                    int count = 0;
                    int totalContribution = 0;
                    for (MyDirectEdge e : edges) {
                        long value = e.getContribution();
                        if (value > 0) {
                            count++;
                            totalContribution += value;
                            if (mapByEdgeValue.containsKey(value)) {
                                mapByEdgeValue.put(value, mapByEdgeValue.get(value) + 1);
                            } else {
                                mapByEdgeValue.put(value, 1);
                            }
                        }
                    }

                    XYSeries edgeValueSeries = new XYSeries("E. CONTRIBUTION[AVG.:" + MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat((double)totalContribution/count)) + "]");
                    if (MyVars.getDirectGraphViewer().directGraphViewerControlPanel.edgeValueComboBoxMenu.getSelectedIndex() > 6) {
                        for (Long value : mapByEdgeValue.keySet()) {
                            edgeValueSeries.add(value, mapByEdgeValue.get(value));
                        }
                    } else {
                        for (Long value : mapByEdgeValue.keySet()) {
                            edgeValueSeries.add(value, mapByEdgeValue.get(value));
                        }
                    }
                    dataset.addSeries(edgeValueSeries);

                    JFreeChart chart = ChartFactory.createXYLineChart("", "E. CONT.", "", dataset);
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
                    renderer.setSeriesPaint(0, Color.BLACK);
                    renderer.setSeriesStroke(0, new BasicStroke(1.5f));
                    renderer.setSeriesShapesVisible(0, true);
                    renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                    renderer.setSeriesFillPaint(0, Color.WHITE);
                    renderer.setUseFillPaint(true);

                    ChartPanel chartPanel = new ChartPanel( chart );
                    chartPanel.setPreferredSize( new Dimension( 350 , 367 ) );

                    JLabel titleLabel = new JLabel(" EDGE CONT.");
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
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            new Thread(new Runnable() {@Override public void run() {
                                enlarge();}}).start();
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
                    if (instances == 0) {
                        add(topPanel, BorderLayout.NORTH);
                    }
                    instances++;
                    renderer.setBaseLegendTextFont(MyVars.tahomaPlainFont11);
                    add(chartPanel, BorderLayout.CENTER);
                }
            }
        } catch (Exception ex) {}
        revalidate();
        repaint();
    }

    public void enlarge() {
        MyPathGraphEdgeContributionDistributionLineChart edgeCurrentValueDistribution = new MyPathGraphEdgeContributionDistributionLineChart(this.directedSparseMultigraph);
        edgeCurrentValueDistribution.setLayout(new BorderLayout(3,3));
        edgeCurrentValueDistribution.setBackground(Color.WHITE);

        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries edgeValueSeries = new XYSeries("E. CONTRIBUTION");
        TreeMap<Integer, Integer> mapByEdgeValue = new TreeMap<>();
        Collection<MyDirectEdge> edges = this.directedSparseMultigraph.getEdges();
        int totalCnt = 0;
        int totalValue = 0;
        for (MyDirectEdge e : edges) {
            if (e.getContribution() == 0) continue;
            int value = (int)e.getContribution();
            totalCnt++;
            totalValue += value;
            if (mapByEdgeValue.containsKey(value)) {
                mapByEdgeValue.put(value, mapByEdgeValue.get(value)+1);
            } else {mapByEdgeValue.put(value, 1);}
        }
        if (MyVars.getDirectGraphViewer().directGraphViewerControlPanel.edgeValueComboBoxMenu.getSelectedIndex() > 6) {
            for (Integer value : mapByEdgeValue.keySet()) {
                edgeValueSeries.add(value, mapByEdgeValue.get(value));
            }
        } else {
            for (Integer value : mapByEdgeValue.keySet()) {
                edgeValueSeries.add(value, mapByEdgeValue.get(value));
            }
        }
        String avg = MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat((double) totalValue/totalCnt));
        dataset.addSeries(edgeValueSeries);

        JFreeChart chart = ChartFactory.createXYLineChart("", "E. CONT.", "", dataset);
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
        renderer.setSeriesPaint(0, Color.DARK_GRAY);
        renderer.setSeriesStroke(0, new BasicStroke(1.5f));
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
        renderer.setSeriesFillPaint(0, Color.WHITE);
        renderer.setUseFillPaint(true);

        ChartPanel chartPanel = new ChartPanel(chart);
        edgeCurrentValueDistribution.add(chartPanel, BorderLayout.CENTER);

        JFrame distFrame = new JFrame(" EDGE CONTRIBUTION DISTRIBUTION");
        distFrame.setLayout(new BorderLayout(3,3));
        distFrame.getContentPane().add(edgeCurrentValueDistribution, BorderLayout.CENTER);
        distFrame.setPreferredSize(new Dimension(450, 350));
        distFrame.pack();
        distFrame.setVisible(true);
    }
}
