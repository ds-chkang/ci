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

public class MySingleNodeSuccessorEdgeValueDistributionLineChart
extends JPanel {

    public static int instances = 0;
    public MySingleNodeSuccessorEdgeValueDistributionLineChart() {
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
                    if (MyVars.getViewer().vc.selectedNodeNeighborNodeTypeSelector.isShowing()) {
                        selectedSingleNode = ((MyNode) MyVars.g.vRefs.get(MyVars.getViewer().vc.depthNodeNameSet.iterator().next()));
                    } else if (MyVars.getViewer().selectedNode != null){
                        selectedSingleNode = MyVars.getViewer().selectedNode;
                    }

                    if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() < 2) {
                        JLabel titleLabel = new JLabel(" S. E. V.");
                        titleLabel.setToolTipText("SUCCESSOR EDGE VALUE DISTRIBUTION FOR THE SELECTED NODE");
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
                        int total = 0;
                        int cnt = 0;
                        TreeMap<Integer, Integer> mapByEdgeValue = new TreeMap<>();
                        XYSeries successorEdgeValueSeries = new XYSeries("S. E. V.");
                        Set<MyEdge> edges = new HashSet<>(MyVars.g.getOutEdges(selectedSingleNode));
                        for (MyEdge e : edges) {
                            int value = (int) e.getContribution();
                            cnt++;
                            total += value;
                            if (mapByEdgeValue.containsKey(value)) {
                                mapByEdgeValue.put(value, mapByEdgeValue.get(value) + 1);
                            } else {
                                mapByEdgeValue.put(value, 1);
                            }
                        }
                        if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() > 6) {
                            for (Integer value : mapByEdgeValue.keySet()) {
                                successorEdgeValueSeries.add(mapByEdgeValue.get(value), value);
                            }
                        } else {
                            for (Integer value : mapByEdgeValue.keySet()) {
                                successorEdgeValueSeries.add(mapByEdgeValue.get(value), value);
                            }
                        }
                        if (total == 0) {
                            JLabel titleLabel = new JLabel(" S. E. V.");
                            titleLabel.setToolTipText("SUCCESSOR EDGE VALUE DISTRIBUTION FOR THE SELECTED NODE");
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
                            dataset.addSeries(successorEdgeValueSeries);

                            JFreeChart chart = ChartFactory.createXYLineChart("", "DEPTH", "", dataset);
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

                            JLabel titleLabel = new JLabel(" S. E. V.");
                            titleLabel.setToolTipText("SUCCESSOR EDGE VALUE DISTRIBUTION FOR THE SELECTED NODE");
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
                            add(topPanel, BorderLayout.NORTH);
                            add(chartPanel, BorderLayout.CENTER);
                        }
                    }
                } catch (Exception ex) {ex.printStackTrace();}
            }
        });
    }

    public void enlarge() {
        MyNode selectedSingleNode = null;
        if (MyVars.getViewer().vc.selectedNodeNeighborNodeTypeSelector.isShowing()) {
            selectedSingleNode = ((MyNode) MyVars.g.vRefs.get(MyVars.getViewer().vc.depthNodeNameSet.iterator().next()));
        } else if (MyVars.getViewer().selectedNode != null){
            selectedSingleNode = MyVars.getViewer().selectedNode;
        }

        MySingleNodeSuccessorEdgeValueDistributionLineChart edgeCurrentValueDistribution = new MySingleNodeSuccessorEdgeValueDistributionLineChart();
        edgeCurrentValueDistribution.setLayout(new BorderLayout(3,3));
        edgeCurrentValueDistribution.setBackground(Color.WHITE);

        int totalCnt = 0;
        int totalValue = 0;
        TreeMap<Integer, Integer> mapByEdgeValue = new TreeMap<>();
        XYSeries successorEdgeValueSeries = new XYSeries("S. E. V.");
        Set<MyEdge> edges = new HashSet<>(MyVars.g.getOutEdges(selectedSingleNode));
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
                successorEdgeValueSeries.add(mapByEdgeValue.get(value), value);
            }
        } else {
            for (Integer value : mapByEdgeValue.keySet()) {
                successorEdgeValueSeries.add(mapByEdgeValue.get(value), value);
            }
        }
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(successorEdgeValueSeries);

        JFreeChart chart = ChartFactory.createXYLineChart("", "S. E. V.", "", dataset);
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

        edgeCurrentValueDistribution.add(chartPanel, BorderLayout.CENTER);
        JFrame frame = new JFrame(" SUCCESSOR EDGE VALUE DISTRIBUTION");
        frame.setLayout(new BorderLayout(3,3));
        frame.getContentPane().add(edgeCurrentValueDistribution, BorderLayout.CENTER);
        frame.setPreferredSize(new Dimension(400, 300));
        frame.pack();
        frame.setVisible(true);
    }
}
