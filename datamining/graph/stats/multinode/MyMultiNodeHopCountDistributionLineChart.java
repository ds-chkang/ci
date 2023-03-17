package datamining.graph.stats.multinode;

import datamining.main.MyProgressBar;
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
import java.util.TreeMap;

public class MyMultiNodeHopCountDistributionLineChart
extends JPanel {

    public static int instances = 0;
    public MyMultiNodeHopCountDistributionLineChart() {
        this.decorate();
    }

    public void decorate() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                setLayout(new BorderLayout(3,3));
                setBackground(Color.WHITE);

                TreeMap<Integer, Integer> nodeHopCountByNodeMap = new TreeMap<>();
                for (int s = 0; s < MyVars.seqs.length; s++) {
                    for (int i = 0; i < MyVars.seqs[s].length; i++) {
                        String nn = MyVars.seqs[s][i].split(":")[0];
                        MyNode n = (MyNode)MyVars.g.vRefs.get(nn);
                        if (MyVars.getViewer().multiNodes.contains(n)) {
                            int hopCount = (MyVars.seqs[s].length-(i+1));
                            if (nodeHopCountByNodeMap.containsKey(hopCount)) {
                                nodeHopCountByNodeMap.put(hopCount, nodeHopCountByNodeMap.get(hopCount)+1);
                            } else {
                                nodeHopCountByNodeMap.put(hopCount, 1);
                            }
                        }
                    }
                }

                for (Integer hopCount : nodeHopCountByNodeMap.keySet()) {
                    if (nodeHopCountByNodeMap.containsKey(hopCount)) {
                        nodeHopCountByNodeMap.put(hopCount, nodeHopCountByNodeMap.get(hopCount) + 1);
                    } else {
                        nodeHopCountByNodeMap.put(hopCount, 1);
                    }
                }

                XYSeriesCollection dataset = new XYSeriesCollection();
                XYSeries hopSereis = new XYSeries("HOP");
                for (Integer hopCount : nodeHopCountByNodeMap.keySet()) {
                    hopSereis.add(hopCount, nodeHopCountByNodeMap.get(hopCount));
                }
                dataset.addSeries(hopSereis);

                JFreeChart chart = ChartFactory.createXYLineChart("", "HOP", "", dataset);
                chart.getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
                chart.getXYPlot().setBackgroundPaint(Color.WHITE);
                chart.getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
                chart.getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);
                chart.getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
                chart.getXYPlot().getDomainAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
                chart.getXYPlot().getDomainAxis().setTickLabelFont(MyVars.tahomaPlainFont10);
                chart.getXYPlot().getRangeAxis().setTickLabelFont(MyVars.tahomaPlainFont10);
                chart.getXYPlot().getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
                chart.setBackgroundPaint(Color.WHITE);

                XYPlot plot = (XYPlot)chart.getPlot();
                XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
                renderer.setSeriesPaint(0, Color.BLACK);
                renderer.setSeriesStroke(0, new BasicStroke(1.5f));
                renderer.setSeriesShapesVisible(0, true);
                renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                renderer.setSeriesFillPaint(0, Color.WHITE);
                renderer.setUseFillPaint(true);

                ChartPanel chartPanel = new ChartPanel( chart );
                chartPanel.setPreferredSize( new Dimension( 350 , 367 ) );

                JLabel titleLabel = new JLabel(" MULTINODE H.");
                titleLabel.setToolTipText("MULTINODE HOP DISTRIBUTION");
                titleLabel.setFont(MyVars.tahomaBoldFont11);
                titleLabel.setBackground(Color.WHITE);
                titleLabel.setForeground(Color.DARK_GRAY);

                JPanel titlePanel = new JPanel();
                titlePanel.setBackground(Color.WHITE);
                titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
                titlePanel.add(titleLabel);

                JButton enlargeBtn = new JButton("+");
                enlargeBtn.setBackground(Color.WHITE);
                enlargeBtn.setFont(MyVars.tahomaPlainFont10);
                enlargeBtn.setFocusable(false);
                enlargeBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        new Thread(new Runnable() {
                            @Override public void run() {
                                enlarge();
                            }
                        }).start();
                    }
                });

                JPanel enlargePanel = new JPanel();
                enlargePanel.setBackground(Color.WHITE);
                enlargePanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3,3));
                enlargePanel.add(enlargeBtn);

                JPanel topPanel = new JPanel();
                topPanel.setBackground(Color.WHITE);
                topPanel.setLayout(new BorderLayout(3,3));
                topPanel.add(titlePanel, BorderLayout.WEST);
                topPanel.add(enlargePanel, BorderLayout.CENTER);

                setBackground(Color.WHITE);
                add(chartPanel, BorderLayout.CENTER);
                add(topPanel, BorderLayout.NORTH);

                revalidate();
                repaint();
            }
        });
    }

    public void enlarge() {
        MyProgressBar pb = new MyProgressBar(false);
        MyMultiNodeHopCountDistributionLineChart multiNodeLevelNodeHopCountDistributionLineChart = new MyMultiNodeHopCountDistributionLineChart();
        multiNodeLevelNodeHopCountDistributionLineChart.setLayout(new BorderLayout(3,3));
        multiNodeLevelNodeHopCountDistributionLineChart.setBackground(Color.WHITE);
        pb.updateValue(10, 100);

        TreeMap<Integer, Integer> nodeHopCountByNodeMap = new TreeMap<>();
        for (int s = 0; s < MyVars.seqs.length; s++) {
            for (int i = 0; i < MyVars.seqs[s].length; i++) {
                String nn = MyVars.seqs[s][i].split(":")[0];
                MyNode n = (MyNode)MyVars.g.vRefs.get(nn);
                if (MyVars.getViewer().multiNodes.contains(n)) {
                    int hopCount = (MyVars.seqs[s].length-(i+1));
                    if (nodeHopCountByNodeMap.containsKey(hopCount)) {
                        nodeHopCountByNodeMap.put(hopCount, nodeHopCountByNodeMap.get(hopCount)+1);
                    } else {
                        nodeHopCountByNodeMap.put(hopCount, 1);
                    }
                }
            }
        }
        pb.updateValue(20, 100);

        for (Integer hopCount : nodeHopCountByNodeMap.keySet()) {
            if (nodeHopCountByNodeMap.containsKey(hopCount)) {
                nodeHopCountByNodeMap.put(hopCount, nodeHopCountByNodeMap.get(hopCount) + 1);
            } else {
                nodeHopCountByNodeMap.put(hopCount, 1);
            }
        }
        pb.updateValue(30, 100);

        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries hopSereis = new XYSeries("HOP COUNT");
        for (Integer hopCount : nodeHopCountByNodeMap.keySet()) {
            hopSereis.add(hopCount, nodeHopCountByNodeMap.get(hopCount));
        }
        dataset.addSeries(hopSereis);
        pb.updateValue(40, 100);

        JFreeChart chart = ChartFactory.createXYLineChart("", "HOP", "", dataset);
        chart.getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
        chart.getXYPlot().setBackgroundPaint(Color.WHITE);
        chart.getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
        chart.getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);
        chart.getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
        chart.getXYPlot().getDomainAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
        chart.getXYPlot().getDomainAxis().setTickLabelFont(MyVars.tahomaPlainFont10);
        chart.getXYPlot().getRangeAxis().setTickLabelFont(MyVars.tahomaPlainFont10);
        chart.getXYPlot().getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        chart.setBackgroundPaint(Color.WHITE);
        pb.updateValue(50, 100);

        XYPlot plot = (XYPlot)chart.getPlot();
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, Color.BLACK);
        renderer.setSeriesStroke(0, new BasicStroke(1.5f));
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
        renderer.setSeriesFillPaint(0, Color.WHITE);
        renderer.setUseFillPaint(true);
        pb.updateValue(60, 100);

        ChartPanel chartPanel = new ChartPanel( chart );
        chartPanel.setPreferredSize( new Dimension( 350 , 367 ) );

        multiNodeLevelNodeHopCountDistributionLineChart.add(chartPanel, BorderLayout.CENTER);
        pb.updateValue(70, 100);

        JFrame frame = new JFrame(" MULTINODE HOP DISTRIBUTION");
        frame.setLayout(new BorderLayout(3,3));
        frame.getContentPane().add(multiNodeLevelNodeHopCountDistributionLineChart, BorderLayout.CENTER);
        frame.setPreferredSize(new Dimension(400, 300));
        frame.pack();
        pb.updateValue(100, 100);
        pb.dispose();
        frame.setVisible(true);
    }

}
