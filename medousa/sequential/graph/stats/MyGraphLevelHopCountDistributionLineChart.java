package medousa.sequential.graph.stats;

import medousa.MyProgressBar;
import medousa.sequential.graph.MyNode;
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

public class MyGraphLevelHopCountDistributionLineChart
extends JPanel {

    public static int instances = 0;
    public MyGraphLevelHopCountDistributionLineChart() {decorate();}

    public void decorate() {
        try {
            setLayout(new BorderLayout(3, 3));
            setBackground(Color.WHITE);

            JFreeChart chart = ChartFactory.createXYLineChart("", "DEPTH", "", setDataset());
            chart.getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
            chart.getXYPlot().setBackgroundPaint(Color.WHITE);
            chart.getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
            chart.getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);
            chart.getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
            chart.getXYPlot().getDomainAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
            chart.getXYPlot().getDomainAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
            chart.getXYPlot().getRangeAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
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
            chartPanel.setPreferredSize(new Dimension(350, 367));

            JButton enlargeBtn = new JButton("+");
            enlargeBtn.setFont(MySequentialGraphVars.tahomaPlainFont12);
            enlargeBtn.setBackground(Color.WHITE);
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
            enlargePanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 3));
            enlargePanel.add(enlargeBtn);

            JPanel topPanel = new JPanel();
            topPanel.setLayout(new BorderLayout(3, 3));
            topPanel.setBackground(Color.WHITE);
            add(topPanel, BorderLayout.NORTH);
            renderer.setBaseLegendTextFont(MySequentialGraphVars.tahomaPlainFont11);
            add(chartPanel, BorderLayout.CENTER);
            revalidate();
            repaint();
        } catch (Exception ex) {ex.printStackTrace();}
     }

     private XYSeriesCollection setDataset() {
        TreeMap<Integer, Integer> nodeHopCountByNodeMap = new TreeMap<>();
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0) continue;
            int cnt = 0;
            int totalHop = 0;
            for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                for (int itemsetIdx = 0; itemsetIdx < MySequentialGraphVars.seqs[s].length; itemsetIdx++) {
                    String nn = MySequentialGraphVars.seqs[s][itemsetIdx].split(":")[0];
                    if (nn.equals(n.getName())) {
                        int hopCount = (MySequentialGraphVars.seqs[s].length - (itemsetIdx + 1));
                        if (hopCount > 0) {
                            totalHop += hopCount;
                            cnt++;
                        }
                    }
                }
            }
            if (cnt > 0) {
                int avgHop = (int) ((double) totalHop / cnt);
                if (nodeHopCountByNodeMap.containsKey(avgHop)) {
                    nodeHopCountByNodeMap.put(avgHop, nodeHopCountByNodeMap.get(avgHop) + 1);
                } else {
                    nodeHopCountByNodeMap.put(avgHop, 1);
                }
            }
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries avgHopSeries = new XYSeries("AVG. N. HOP");
        for (Integer avgHop : nodeHopCountByNodeMap.keySet()) {
            avgHopSeries.add(avgHop, nodeHopCountByNodeMap.get(avgHop));
        }
        dataset.addSeries(avgHopSeries);
        return dataset;
    }

    public void enlarge() {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            JFrame f = new JFrame("AVERAGE NODE HOP DISTRIBUTION");
            f.setLayout(new BorderLayout(3, 3));
            f.getContentPane().add(this, BorderLayout.CENTER);
            f.setPreferredSize(new Dimension(450, 350));
            f.pack();
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
