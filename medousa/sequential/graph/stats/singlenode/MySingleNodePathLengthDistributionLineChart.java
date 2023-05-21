package medousa.sequential.graph.stats.singlenode;

import medousa.sequential.graph.MyNode;
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

public class MySingleNodePathLengthDistributionLineChart
extends JPanel {

    public static int instances = 0;

    public MySingleNodePathLengthDistributionLineChart() {
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
                    if (MySequentialGraphVars.getSequentialGraphViewer().vc.selectedNodeNeighborNodeTypeSelector.isVisible()) {
                        selectedSingleNode = ((MyNode) MySequentialGraphVars.g.vRefs.get(MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeNameSet.iterator().next()));
                    } else {
                        selectedSingleNode = MySequentialGraphVars.getSequentialGraphViewer().selectedNode;
                    }

                    XYSeriesCollection dataset = new XYSeriesCollection();
                    XYSeries pathLengthDistributionSeries = new XYSeries("P. L.");
                    int totalCnt = 0;
                    long totalValue = 0;
                    for (int i = 2; i < selectedSingleNode.pathLengthByDepthMap.size(); i++) {
                        if (selectedSingleNode.pathLengthByDepthMap.containsKey(i)) {
                            totalCnt++;
                            pathLengthDistributionSeries.add((i-1), selectedSingleNode.pathLengthByDepthMap.get(i));
                        } else {
                            pathLengthDistributionSeries.add((i-1), 0);
                        }
                    }
                    dataset.addSeries(pathLengthDistributionSeries);

                    String avg = MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat((double) totalValue / totalCnt));
                    JFreeChart chart = ChartFactory.createXYLineChart("", "PATH LENGTH", "", dataset);
                    chart.getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
                    chart.getXYPlot().setBackgroundPaint(Color.WHITE);
                    chart.getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
                    chart.getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);
                    chart.getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
                    chart.getXYPlot().getDomainAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
                    chart.getXYPlot().getDomainAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont10);
                    chart.getXYPlot().getRangeAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
                    chart.getXYPlot().getRangeAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont10);
                    chart.getXYPlot().getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
                    chart.setBackgroundPaint(Color.WHITE);

                    XYPlot plot = (XYPlot) chart.getPlot();
                    XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
                    renderer.setSeriesPaint(0, Color.RED);
                    renderer.setSeriesStroke(0, new BasicStroke(1.6f));
                    renderer.setSeriesShapesVisible(0, true);
                    renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                    renderer.setSeriesFillPaint(0, Color.WHITE);
                    renderer.setUseFillPaint(true);

                    ChartPanel chartPanel = new ChartPanel(chart);
                    chartPanel.setPreferredSize(new Dimension(350, 367));

                    JLabel titleLabel = new JLabel(" PATH L.");
                    titleLabel.setFont(MySequentialGraphVars.tahomaBoldFont11);
                    titleLabel.setBackground(Color.WHITE);
                    titleLabel.setForeground(Color.DARK_GRAY);

                    JPanel titlePanel = new JPanel();
                    titlePanel.setBackground(Color.WHITE);
                    titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
                    titlePanel.add(titleLabel);

                    JButton enlargeBtn = new JButton("+");
                    enlargeBtn.setFont(MySequentialGraphVars.tahomaPlainFont10);
                    enlargeBtn.setBackground(Color.WHITE);
                    enlargeBtn.setFocusable(false);
                    enlargeBtn.addActionListener(new ActionListener() {
                        @Override public void actionPerformed(ActionEvent e) {
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
                    topPanel.add(titlePanel, BorderLayout.WEST);
                    topPanel.add(enlargePanel, BorderLayout.EAST);
                    add(topPanel, BorderLayout.NORTH);
                    add(chartPanel, BorderLayout.CENTER);
                    renderer.setBaseLegendTextFont(MySequentialGraphVars.tahomaPlainFont10);

                    revalidate();
                    repaint();
                } catch (Exception ex) {}
            }
        });

    }

    public void enlarge() {
        MySingleNodePathLengthDistributionLineChart nodePathLengthDistributions = new MySingleNodePathLengthDistributionLineChart();
        nodePathLengthDistributions.setLayout(new BorderLayout(3,3));
        nodePathLengthDistributions.setBackground(Color.WHITE);

        JFrame distFrame = new JFrame(" NODE BETWEEN PATH LENGTH DISTRIBUTION");
        distFrame.setLayout(new BorderLayout(3,3));
        distFrame.getContentPane().add(nodePathLengthDistributions, BorderLayout.CENTER);
        distFrame.setPreferredSize(new Dimension(450, 350));
        distFrame.pack();
        distFrame.setVisible(true);
    }
}
