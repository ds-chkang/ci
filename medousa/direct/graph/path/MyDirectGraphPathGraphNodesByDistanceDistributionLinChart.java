package medousa.direct.graph.path;

import medousa.direct.utils.MyDirectGraphMathUtil;
import medousa.direct.utils.MyDirectGraphSysUtil;
import medousa.direct.utils.MyDirectGraphVars;
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
import java.util.Set;

public class MyDirectGraphPathGraphNodesByDistanceDistributionLinChart
extends JPanel {

    public static int instances = 0;
    private Map<Integer, Set<String>> nodesByDepthMap;

    public MyDirectGraphPathGraphNodesByDistanceDistributionLinChart(Map<Integer, Set<String>> nodesByDepthMap) {
        this.nodesByDepthMap = nodesByDepthMap;
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
                    for (Integer depth : nodesByDepthMap.keySet()) {
                        totalNodes += nodesByDepthMap.get(depth).size();
                    }

                    XYSeriesCollection dataset = new XYSeriesCollection();
                    XYSeries nodesByDepthSeries = null;
                    if (nodesByDepthMap.size() == 0) {
                        nodesByDepthSeries = new XYSeries("[AVG.:" + "0.00]");
                        nodesByDepthSeries.add(0, 0);
                    } else {
                        nodesByDepthSeries = new XYSeries("[AVG.:" + MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat((double) totalNodes/nodesByDepthMap.size())) + "]");
                        for (Integer depth : nodesByDepthMap.keySet()) {
                            nodesByDepthSeries.add((double)depth, nodesByDepthMap.get(depth).size());
                        }
                    }

                    dataset.addSeries(nodesByDepthSeries);

                    JFreeChart chart = ChartFactory.createXYLineChart("", "NODE BY DISTANCE", "", dataset);
                    chart.getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
                    chart.getXYPlot().setBackgroundPaint(Color.WHITE);
                    chart.getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
                    chart.getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);
                    chart.getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
                    chart.getXYPlot().getDomainAxis().setLabelFont(MyDirectGraphVars.tahomaPlainFont11);
                    chart.getXYPlot().getDomainAxis().setTickLabelFont(MyDirectGraphVars.tahomaPlainFont11);
                    chart.getXYPlot().getRangeAxis().setTickLabelFont(MyDirectGraphVars.tahomaPlainFont11);
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

                    JLabel titleLabel = new JLabel(" NODES BY DEPTH");
                    titleLabel.setFont(MyDirectGraphVars.tahomaBoldFont11);
                    titleLabel.setBackground(Color.WHITE);
                    titleLabel.setForeground(Color.DARK_GRAY);

                    JButton enlargeBtn = new JButton("+");
                    enlargeBtn.setFont(MyDirectGraphVars.tahomaPlainFont11);
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
                    renderer.setBaseLegendTextFont(MyDirectGraphVars.tahomaPlainFont11);
                    add(chartPanel, BorderLayout.CENTER);
                } catch (Exception ex) {

                }
            }
        });
    }

    public void enlarge() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                try {
                    MyDirectGraphPathGraphNodesByDistanceDistributionLinChart nodesByDepthDistributionLinChart = new MyDirectGraphPathGraphNodesByDistanceDistributionLinChart(nodesByDepthMap);
                    JFrame f = new JFrame(" NODE DISTRIBUTIONS BY DEPTH");
                    f.setLayout(new BorderLayout(3,3));
                    f.getContentPane().add(nodesByDepthDistributionLinChart, BorderLayout.CENTER);
                    f.setPreferredSize(new Dimension(450, 350));
                    f.pack();
                    f.setVisible(true);
                } catch (Exception ex) {ex.printStackTrace();}
            }
        });
    }
}
