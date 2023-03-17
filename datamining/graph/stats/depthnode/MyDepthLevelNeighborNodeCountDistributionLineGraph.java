package datamining.graph.stats.depthnode;

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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MyDepthLevelNeighborNodeCountDistributionLineGraph extends JPanel {

    public static int instances = 0;
    public MyDepthLevelNeighborNodeCountDistributionLineGraph() {
        this.decorate();
    }
    public void decorate() {
        try {
            removeAll();
            this.setLayout(new BorderLayout(5,5));
            this.setBackground(Color.WHITE);

            Collection<MyNode> nodes = MyVars.g.getDepthNodes();
            Map<Integer, Integer> successorCountMap = new HashMap<>();
            Map<Integer, Integer> predecessorCountMap = new HashMap<>();
            XYSeries successorCountSeries = new XYSeries("S. C.");
            XYSeries predecessorCountSeries = new XYSeries("P. C.");
            for (MyNode n : nodes) {
                int successors = MyVars.g.getSuccessorCount(n);
                int predecessors = MyVars.g.getPredecessorCount(n);
                if (successors > 0) {
                    if (successorCountMap.containsKey(successors)) {
                        successorCountMap.put(successors, successorCountMap.get(successors)+1);
                    } else {
                        successorCountMap.put(successors, 1);
                    }
                }
                if (predecessors > 0) {
                    if (predecessorCountMap.containsKey(predecessors)) {
                        predecessorCountMap.put(predecessors, predecessorCountMap.get(predecessors)+1);
                    } else {
                        predecessorCountMap.put(predecessors, 1);
                    }
                }
            }

            for (Integer successors : successorCountMap.keySet()) {
                successorCountSeries.add(successors, successorCountMap.get(successors));
            }
            for (Integer predecessors : predecessorCountMap.keySet()) {
                predecessorCountSeries.add(predecessors, predecessorCountMap.get(predecessors));
            }
            XYSeriesCollection dataset = new XYSeriesCollection();
            dataset.addSeries(successorCountSeries);
            dataset.addSeries(predecessorCountSeries);

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
            renderer.setSeriesPaint(0, Color.BLUE);
            renderer.setSeriesStroke(0, new BasicStroke(1.5f));
            renderer.setSeriesShapesVisible(0, true);
            renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
            renderer.setSeriesFillPaint(0, Color.WHITE);
            renderer.setUseFillPaint(true);

            renderer.setSeriesPaint(1, Color.RED);
            renderer.setSeriesStroke(1, new BasicStroke(1.5f));
            renderer.setSeriesShapesVisible(1, true);
            renderer.setSeriesShape(1, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
            renderer.setSeriesFillPaint(1, Color.WHITE);
            renderer.setUseFillPaint(true);

            ChartPanel chartPanel = new ChartPanel( chart );
            chartPanel.setPreferredSize( new Dimension( 350 , 367 ) );

            JLabel titleLabel = new JLabel(" NEIGHBOR N. C.");
            titleLabel.setFont(MyVars.tahomaBoldFont11);
            titleLabel.setBackground(Color.WHITE);
            titleLabel.setForeground(Color.DARK_GRAY);

            JPanel titlePanel = new JPanel();
            titlePanel.setBackground(Color.WHITE);
            titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            titlePanel.add(titleLabel);

            JButton enlargeBtn = new JButton("ENLARGE");
            enlargeBtn.setFont(MyVars.tahomaPlainFont10);
            enlargeBtn.setFocusable(false);
            enlargeBtn.setBackground(Color.WHITE);
            enlargeBtn.addActionListener(new ActionListener() {@Override
            public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {@Override
                public void run() {
                    enlarge();
                }
                }).start();
            }
            });

            JPanel btnPanel = new JPanel();
            btnPanel.setBackground(Color.WHITE);
            btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
            btnPanel.add(enlargeBtn);

            JPanel menuPanel = new JPanel();
            menuPanel.setLayout(new BorderLayout(0,0));
            menuPanel.setBackground(Color.WHITE);
            menuPanel.add(titlePanel, BorderLayout.WEST);
            menuPanel.add(btnPanel, BorderLayout.CENTER);
            renderer.setBaseLegendTextFont(MyVars.tahomaPlainFont10);
            this.add(menuPanel, BorderLayout.NORTH);
            this.add(chartPanel, BorderLayout.CENTER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        revalidate();
        repaint();
    }

    public void enlarge() {
        MyProgressBar pb = new MyProgressBar(false);
        JFrame frame = new JFrame(" NEIGHBOR N. COUNT DISTRIBUTION");
        frame.setLayout(new BorderLayout());
        frame.setBackground(Color.WHITE);
        frame.setPreferredSize(new Dimension(400, 300));
        pb.updateValue(20, 100);

        MyDepthLevelNeighborNodeCountDistributionLineGraph depthNodeLevelNeighborNodeCountDistributionLineGraph = new MyDepthLevelNeighborNodeCountDistributionLineGraph();
        frame.getContentPane().add(depthNodeLevelNeighborNodeCountDistributionLineGraph, BorderLayout.CENTER);
        frame.pack();
        pb.updateValue(60, 100);

        pb.updateValue(100,100);
        pb.dispose();

        frame.setCursor(Cursor.HAND_CURSOR);
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);
        frame.setAlwaysOnTop(false);
    }

}
