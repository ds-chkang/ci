package medousa.sequential.graph.path;

import medousa.sequential.utils.MyMathUtil;
import medousa.sequential.utils.MySequentialGraphSysUtil;
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
import java.util.Map;

public class MyNodePathGraphNodeOutContributionDistributionLinChart
extends JPanel {

    public static int instances = 0;
    private Map<Integer, Long> outContributionByDepthMap;

    public MyNodePathGraphNodeOutContributionDistributionLinChart(Map<Integer, Long> outContributionByDepthMap) {
        this.outContributionByDepthMap = outContributionByDepthMap;
        decorate();
    }

    public void decorate() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                try {
                    removeAll();
                    setLayout(new BorderLayout(3, 3));
                    setBackground(Color.WHITE);

                    long totalNodes = 0L;
                    for (Integer depth : outContributionByDepthMap.keySet()) {
                        totalNodes += outContributionByDepthMap.get(depth);
                    }

                    XYSeriesCollection dataset = new XYSeriesCollection();
                    XYSeries nodesByDepthSeries = new XYSeries("[AVG.:" + MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat((double)totalNodes/ outContributionByDepthMap.size())) + "]");
                    for (Integer depth : outContributionByDepthMap.keySet()) {
                        nodesByDepthSeries.add((double)depth, outContributionByDepthMap.get(depth));
                    }

                    dataset.addSeries(nodesByDepthSeries);

                    JFreeChart chart = ChartFactory.createXYLineChart("", "OUT-CONT.", "", dataset);
                    chart.getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
                    chart.getXYPlot().setBackgroundPaint(Color.WHITE);
                    chart.getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
                    chart.getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);
                    chart.getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
                    chart.getXYPlot().getDomainAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
                    chart.getXYPlot().getDomainAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
                    chart.getXYPlot().getRangeAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
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

                    JLabel titleLabel = new JLabel(" OUT-CONT.");
                    titleLabel.setFont(MySequentialGraphVars.tahomaBoldFont11);
                    titleLabel.setBackground(Color.WHITE);
                    titleLabel.setForeground(Color.DARK_GRAY);

                    JButton enlargeBtn = new JButton("ENLARGE");
                    enlargeBtn.setFont(MySequentialGraphVars.tahomaPlainFont11);
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
                    renderer.setBaseLegendTextFont(MySequentialGraphVars.tahomaPlainFont11);
                    add(chartPanel, BorderLayout.CENTER);

                    revalidate();
                    repaint();
                } catch (Exception ex) {ex.printStackTrace();}
            }
        });
    }

    public void enlarge() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                try {
                    MyNodePathGraphNodeOutContributionDistributionLinChart outContributionDistributionLinChart = new MyNodePathGraphNodeOutContributionDistributionLinChart(outContributionByDepthMap);
                    JFrame frame = new JFrame(" OUT-CONTRIBUTION DISTRIBUTIONS BY DEPTH");
                    frame.setLayout(new BorderLayout(3,3));
                    frame.getContentPane().add(outContributionDistributionLinChart, BorderLayout.CENTER);
                    frame.setPreferredSize(new Dimension(450, 350));
                    frame.pack();
                    frame.setVisible(true);
                } catch (Exception ex) {ex.printStackTrace();}
            }
        });
    }
}
