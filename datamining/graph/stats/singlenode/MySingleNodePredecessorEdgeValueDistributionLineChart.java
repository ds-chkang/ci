package datamining.graph.stats.singlenode;

import datamining.graph.MyEdge;
import datamining.graph.MyNode;
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
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

public class MySingleNodePredecessorEdgeValueDistributionLineChart
extends JPanel {

    public static int instances = 0;
    public MySingleNodePredecessorEdgeValueDistributionLineChart() {
        this.decorate();
    }

    public void decorate() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                try {
                    removeAll();
                    setLayout(new BorderLayout(3, 3));
                    setBackground(Color.WHITE);

                    MyNode selectedSingleNode = null;
                    if (MyVars.getViewer().vc.depthNodeNameSet != null && MyVars.getViewer().vc.depthNodeNameSet.size() == 1) {
                        selectedSingleNode = ((MyNode) MyVars.g.vRefs.get(MyVars.getViewer().vc.depthNodeNameSet.iterator().next()));
                    } else if (MyVars.getViewer().selectedNode != null){
                        selectedSingleNode = MyVars.getViewer().selectedNode;
                    }

                    if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() < 2) {
                        JLabel titleLabel = new JLabel(" P. E. V.");
                        titleLabel.setToolTipText("PREDECESSOR EDGE VALUE DISTRIBUTION FOR THE SELECTED NODE");
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
                        add(topPanel, BorderLayout.NORTH);
                        add(msg, BorderLayout.CENTER);
                    } else {
                        int totalCnt = 0;
                        int totalValue = 0;
                        Set<MyEdge> edges = new HashSet<>(MyVars.g.getInEdges(selectedSingleNode));
                        TreeMap<Integer, Integer> mapByEdgeValue = new TreeMap<>();
                        XYSeries predecessorEdgeValueSeries = new XYSeries("P. E. V.");
                        for (MyEdge e : edges) {
                            int value = (int) e.getContribution();
                            totalCnt++;
                            totalValue += value;
                            if (mapByEdgeValue.containsKey(value)) {
                                mapByEdgeValue.put(value, mapByEdgeValue.get(value) + 1);
                            } else {
                                mapByEdgeValue.put(value, 1);
                            }
                        }
                        if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() > 6) {
                            for (Integer value : mapByEdgeValue.keySet()) {
                                predecessorEdgeValueSeries.add(value, mapByEdgeValue.get(value));
                            }
                        } else {
                            for (Integer value : mapByEdgeValue.keySet()) {
                                predecessorEdgeValueSeries.add(value, mapByEdgeValue.get(value));
                            }
                        }
                        if (totalCnt == 0) {
                            JLabel titleLabel = new JLabel(" P. E. V.");
                            titleLabel.setToolTipText("PREDECESSOR EDGE VALUE DISTRIBUTION FOR THE SELECTED NODE");
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
                            add(topPanel, BorderLayout.NORTH);
                            add(msg, BorderLayout.CENTER);
                        } else {
                            XYSeriesCollection dataset = new XYSeriesCollection();
                            dataset.addSeries(predecessorEdgeValueSeries);

                            JFreeChart chart = ChartFactory.createXYLineChart("", "P. E. V.", "", dataset);
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
                            renderer.setSeriesStroke(0, new BasicStroke(1.5f));
                            renderer.setSeriesShapesVisible(0, true);
                            renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                            renderer.setSeriesFillPaint(0, Color.WHITE);
                            renderer.setUseFillPaint(true);

                            ChartPanel chartPanel = new ChartPanel( chart );
                            chartPanel.setPreferredSize( new Dimension( 350 , 367 ) );

                            JLabel titleLabel = new JLabel(" P. E. V.");
                            titleLabel.setToolTipText("PREDECESSOR EDGE VALUE DISTRIBUTION FOR THE SELECTED NODE");
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

                            add(chartPanel, BorderLayout.CENTER);
                            add(topPanel, BorderLayout.NORTH);
                        }
                    }
                } catch (Exception ex) {ex.printStackTrace();}
            }
        });
    }

    public void enlarge() {
        MyNode selectedSingleNode = null;
        if (MyVars.getViewer().vc.depthNodeNameSet != null && MyVars.getViewer().vc.depthNodeNameSet.size() == 1) {
            selectedSingleNode = ((MyNode) MyVars.g.vRefs.get(MyVars.getViewer().vc.depthNodeNameSet.iterator().next()));
        } else if (MyVars.getViewer().selectedNode != null){
            selectedSingleNode = MyVars.getViewer().selectedNode;
        }

        MySingleNodePredecessorEdgeValueDistributionLineChart edgeCurrentValueDistribution = new MySingleNodePredecessorEdgeValueDistributionLineChart();
        edgeCurrentValueDistribution.setLayout(new BorderLayout(3,3));
        edgeCurrentValueDistribution.setBackground(Color.WHITE);

        int totalCnt = 0;
        int totalValue = 0;
        TreeMap<Integer, Integer> mapByEdgeValue = new TreeMap<>();
        XYSeries predecessorEdgeValueSeries = new XYSeries("P. E. V.");
        Set<MyEdge> edges = new HashSet<>(MyVars.g.getInEdges(selectedSingleNode));
        for (MyEdge e : edges) {
            if (e.getCurrentValue() == 0) continue;
            int value = (int) e.getCurrentValue();
            totalCnt++;
            totalValue += e.getCurrentValue();
            if (mapByEdgeValue.containsKey(value)) {
                mapByEdgeValue.put(value, mapByEdgeValue.get(value) + 1);
            } else {
                mapByEdgeValue.put(value, 1);
            }
        }
        if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() > 6) {
            for (Integer value : mapByEdgeValue.keySet()) {
                predecessorEdgeValueSeries.add(mapByEdgeValue.get(value), value);
            }
        } else {
            for (Integer value : mapByEdgeValue.keySet()) {
                predecessorEdgeValueSeries.add(mapByEdgeValue.get(value), value);
            }
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(predecessorEdgeValueSeries);

        JFreeChart chart = ChartFactory.createXYLineChart("", "P. E. V.", "", dataset);
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
        renderer.setSeriesStroke(0, new BasicStroke(1.5f));
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
        renderer.setSeriesFillPaint(0, Color.WHITE);
        renderer.setUseFillPaint(true);

        ChartPanel chartPanel = new ChartPanel( chart );
        chartPanel.setPreferredSize( new Dimension( 350 , 367 ) );

        edgeCurrentValueDistribution.add(chartPanel, BorderLayout.CENTER);
        JFrame distFrame = new JFrame(" PREDECESSOR EDGE VALUE DISTRIBUTION");
        distFrame.setLayout(new BorderLayout(3,3));
        distFrame.getContentPane().add(edgeCurrentValueDistribution, BorderLayout.CENTER);
        distFrame.setPreferredSize(new Dimension(400, 300));
        distFrame.pack();
        distFrame.setVisible(true);
    }
}
