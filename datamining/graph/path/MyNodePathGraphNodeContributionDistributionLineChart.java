package datamining.graph.path;

import datamining.main.MyProgressBar;
import datamining.graph.layout.MyDirectedSparseMultigraph;
import datamining.graph.MyNode;
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

public class MyNodePathGraphNodeContributionDistributionLineChart extends JPanel {

    public static int instances = 0;
    private MyDirectedSparseMultigraph directedSparseMultigraph;
    public MyNodePathGraphNodeContributionDistributionLineChart(MyDirectedSparseMultigraph directedSparseMultigraph) {
        this.directedSparseMultigraph = directedSparseMultigraph;
        this.decorate();
    }
    public void decorate() {
        try {
            removeAll();
            this.setLayout(new BorderLayout(5,5));
            this.setBackground(Color.WHITE);

            if (this.directedSparseMultigraph == null) {
                JLabel titleLabel = new JLabel(" NODE CONT.");
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
                return;
            }

            TreeMap<Long, Integer> nodeValueMap = new TreeMap<>();
            Collection<MyNode> nodes = this.directedSparseMultigraph.getVertices();
            int count = 0;
            long totalContribution = 0L;
            for (MyNode n : nodes) {
                if (n.getContribution() > 0) {
                    long value = n.getContribution();
                    totalContribution += value;
                    count++;
                      if (nodeValueMap.containsKey(value)) {nodeValueMap.put(value, nodeValueMap.get(value) + 1);}
                    else {nodeValueMap.put(value, 1);}
                }
            }

            XYSeriesCollection dataset = new XYSeriesCollection();
            XYSeries series = new XYSeries("N. CONTRIBUTION[AVG.:" + MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat((double)totalContribution/count)) + "]");
            for (Long nodeValue : nodeValueMap.keySet()) {
                series.add(nodeValue, nodeValueMap.get(nodeValue));
            }
            dataset.addSeries(series);

            JFreeChart chart = ChartFactory.createXYLineChart("", "N. CONT.", "", dataset);
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

            ChartPanel chartPanel = new ChartPanel( chart );
            chartPanel.setPreferredSize( new Dimension( 350 , 367 ) );

            JLabel titleLabel = new JLabel(" NODE CONT.");
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

            if (instances == 0) {
                this.add(menuPanel, BorderLayout.NORTH);
            }
            instances++;

            renderer.setBaseLegendTextFont(MyVars.tahomaPlainFont11);
            this.add(chartPanel, BorderLayout.CENTER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        revalidate();
        repaint();
    }

    public void enlarge() {
        MyProgressBar pb = new MyProgressBar(false);
        JFrame frame = new JFrame(" NODE CONTRIBUTION DISTRIBUTION");
        frame.setLayout(new BorderLayout());
        frame.setBackground(Color.WHITE);
        frame.setPreferredSize(new Dimension(400, 450));
        pb.updateValue(20, 100);

        MyNodePathGraphNodeContributionDistributionLineChart depthNodeLevelNeighborNodeCountDistributionLineGraph = new MyNodePathGraphNodeContributionDistributionLineChart(directedSparseMultigraph);
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
