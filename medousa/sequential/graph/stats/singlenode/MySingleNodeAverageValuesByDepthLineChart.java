package medousa.sequential.graph.stats.singlenode;

import medousa.sequential.graph.MyNode;
import medousa.MyProgressBar;
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
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.Ellipse2D;
import java.util.*;

public class MySingleNodeAverageValuesByDepthLineChart
extends JPanel
implements ActionListener {

    private JComboBox graphOptionComboBox;
    private int selectedOption = 0;
    public static int instances = 0;
    private XYSeries averageInContributionByDepthSeries = new XYSeries("IN-CONT.");
    private XYSeries averageOutContributionByDepthSeries = new XYSeries("OUT-CONT.");
    private XYSeries averagReachTimeByDepthSeries = new XYSeries("REACH TIME");
    private XYSeries averageInOutDiffByDepthSeries = new XYSeries("CONT-DIFF.");

    public MySingleNodeAverageValuesByDepthLineChart() {
        decorate();
    }

    public synchronized void decorate() {
        final MySingleNodeAverageValuesByDepthLineChart graphLevelAverageValuesByDepthLineChart = this;
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                try {
                    removeAll();
                    setLayout(new BorderLayout(5, 5));
                    setBackground(Color.WHITE);

                    MyNode selectedSingleNode = null;
                    if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeNameSet != null && MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeNameSet.size() == 1) {
                        selectedSingleNode = ((MyNode) MySequentialGraphVars.g.vRefs.get(MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeNameSet.iterator().next()));
                    } else if (MySequentialGraphVars.getSequentialGraphViewer().selectedNode != null) {
                        selectedSingleNode = MySequentialGraphVars.getSequentialGraphViewer().selectedNode;
                    }

                    graphOptionComboBox = new JComboBox();
                    graphOptionComboBox.setFont(MySequentialGraphVars.tahomaPlainFont12);
                    graphOptionComboBox.setBackground(Color.WHITE);
                    graphOptionComboBox.setFocusable(false);
                    graphOptionComboBox.addItem("SELECT");
                    graphOptionComboBox.addItem("IN-CONT.");
                    graphOptionComboBox.addItem("OUT-CONT.");
                    graphOptionComboBox.addItem("REACH TIME");
                    graphOptionComboBox.setSelectedIndex(selectedOption);

                    Map<Integer, Double> averageInContributionByDepthMap = new HashMap<>();
                    Map<Integer, Double> averageOutContributionByDepthMap = new HashMap<>();
                    Map<Integer, Double> averageReachTimeByDepthMap = new HashMap<>();
                    Map<Integer, Double> averageDiffByDepthMap = new HashMap<>();

                    for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                        if (selectedSingleNode.getNodeDepthInfoMap().containsKey(i)) {
                            double inContTotal = selectedSingleNode.getNodeDepthInfo(i).getInContribution();
                            double outContTotal = selectedSingleNode.getNodeDepthInfo(i).getOutContribution();
                            double inContAvg = inContTotal/selectedSingleNode.getNodeDepthInfo(i).getPredecessorCount();
                            double outContAvg = outContTotal/selectedSingleNode.getNodeDepthInfo(i).getSuccessorCount();

                            averageInContributionByDepthMap.put(i, inContAvg);
                            averageOutContributionByDepthMap.put(i, outContAvg);
                            averageReachTimeByDepthMap.put(i, selectedSingleNode.getNodeDepthInfo(i).getAverageReachTime());
                        } else {
                            averageReachTimeByDepthMap.put(i, 0D);
                            averageInContributionByDepthMap.put(i, 0D);
                            averageOutContributionByDepthMap.put(i, 0D);
                        }
                    }

                    if (MySequentialGraphVars.currentGraphDepth == 0) {
                        for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                            if (averageInContributionByDepthMap.containsKey(i)) {
                                averageInContributionByDepthSeries.add(i, averageInContributionByDepthMap.get(i));
                            } else {
                                averageInContributionByDepthSeries.add(i, 0);
                            }

                            if (averageOutContributionByDepthMap.containsKey(i)) {
                                averageOutContributionByDepthSeries.add(i, averageOutContributionByDepthMap.get(i));
                            } else {
                                averageOutContributionByDepthSeries.add(i, 0);
                            }

                            if (averageReachTimeByDepthMap.containsKey(i)) {
                                averagReachTimeByDepthSeries.add(i, averageReachTimeByDepthMap.get(i));
                            } else {
                                averagReachTimeByDepthSeries.add(i, 0);
                            }
                        }
                    } else {
                        for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                            if (MySequentialGraphVars.currentGraphDepth == i) {
                                if (averageInContributionByDepthMap.containsKey(i)) {
                                    averageInContributionByDepthSeries.add(i, averageInContributionByDepthMap.get(i));
                                } else {
                                    averageInContributionByDepthSeries.add(i, 0);
                                }

                                if (averageOutContributionByDepthMap.containsKey(i)) {
                                    averageOutContributionByDepthSeries.add(i, averageOutContributionByDepthMap.get(i));
                                } else {
                                    averageOutContributionByDepthSeries.add(i, 0);
                                }

                                if (averageReachTimeByDepthMap.containsKey(i)) {
                                    averagReachTimeByDepthSeries.add(i, averageReachTimeByDepthMap.get(i));
                                } else {
                                    averagReachTimeByDepthSeries.add(i, 0);
                                }
                            } else {
                                if (averageInContributionByDepthMap.containsKey(i)) {
                                    averageInContributionByDepthSeries.add(i, averageInContributionByDepthMap.get(i));
                                } else {
                                    averageInContributionByDepthSeries.add(i, 0);
                                }

                                if (averageOutContributionByDepthMap.containsKey(i)) {
                                    averageOutContributionByDepthSeries.add(i, averageOutContributionByDepthMap.get(i));
                                } else {
                                    averageOutContributionByDepthSeries.add(i, 0);
                                }

                                if (averageReachTimeByDepthMap.containsKey(i)) {
                                    averagReachTimeByDepthSeries.add(i, averageReachTimeByDepthMap.get(i));
                                } else {
                                    averagReachTimeByDepthSeries.add(i, 0);
                                }
                            }
                        }
                    }

                    XYSeriesCollection dataset = new XYSeriesCollection();
                    if (graphOptionComboBox.getSelectedIndex() == 0) {
                        dataset.removeAllSeries();
                        dataset.addSeries(averageInContributionByDepthSeries);
                        dataset.addSeries(averageOutContributionByDepthSeries);
                        dataset.addSeries(averagReachTimeByDepthSeries);
                    } else if (graphOptionComboBox.getSelectedIndex() == 1) {
                        dataset.removeAllSeries();
                        dataset.addSeries(averageInContributionByDepthSeries);
                    } else if (graphOptionComboBox.getSelectedIndex() == 2) {
                        dataset.removeAllSeries();
                        dataset.addSeries(averageOutContributionByDepthSeries);
                    } else if (graphOptionComboBox.getSelectedIndex() == 3) {
                        dataset.removeAllSeries();
                        dataset.addSeries(averagReachTimeByDepthSeries);
                    }

                    JFreeChart chart = ChartFactory.createXYLineChart("", "DEPTH", "", dataset);
                    chart.getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
                    chart.getXYPlot().setBackgroundPaint(Color.WHITE);
                    chart.getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
                    chart.getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);
                    chart.getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
                    chart.getXYPlot().getDomainAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
                    chart.getXYPlot().getDomainAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
                    chart.getXYPlot().getRangeAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
                    chart.getXYPlot().getRangeAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
                    chart.getXYPlot().getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
                    chart.setBackgroundPaint(Color.WHITE);

                    XYPlot plot = (XYPlot) chart.getPlot();
                    XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
                    if (graphOptionComboBox.getSelectedIndex() == 0) {
                        renderer.setSeriesPaint(0, Color.DARK_GRAY);
                        renderer.setSeriesStroke(0, new BasicStroke(1.3f));
                        renderer.setSeriesShapesVisible(0, true);
                        renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                        renderer.setSeriesFillPaint(0, Color.WHITE);

                        renderer.setSeriesPaint(1, Color.DARK_GRAY);
                        renderer.setSeriesStroke(1, new BasicStroke(1.3f));
                        renderer.setSeriesShapesVisible(1, true);
                        renderer.setSeriesShape(1, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                        renderer.setSeriesFillPaint(1, Color.WHITE);

                        renderer.setSeriesPaint(2, Color.DARK_GRAY);
                        renderer.setSeriesStroke(2, new BasicStroke(1.3f));
                        renderer.setSeriesShapesVisible(2, true);
                        renderer.setSeriesShape(2, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                        renderer.setSeriesFillPaint(2, Color.WHITE);
                    } else if (graphOptionComboBox.getSelectedIndex() == 1) {
                        renderer.setSeriesPaint(0, Color.DARK_GRAY);
                        renderer.setSeriesStroke(0, new BasicStroke(1.3f));
                        renderer.setSeriesShapesVisible(0, true);
                        renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                    } else if (graphOptionComboBox.getSelectedIndex() == 2) {
                        renderer.setSeriesPaint(1, Color.DARK_GRAY);
                        renderer.setSeriesStroke(1, new BasicStroke(1.5f));
                        renderer.setSeriesShapesVisible(1, true);
                        renderer.setSeriesShape(1, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                    } else if (graphOptionComboBox.getSelectedIndex() == 3) {
                        renderer.setSeriesPaint(2, Color.DARK_GRAY);
                        renderer.setSeriesStroke(2, new BasicStroke(1.5f));
                        renderer.setSeriesShapesVisible(2, true);
                        renderer.setSeriesShape(2, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                    }
                    renderer.setUseFillPaint(true);
                    ChartPanel chartPanel = new ChartPanel(chart);
                    chartPanel.setPreferredSize(new Dimension(560, 367));

                    JLabel titleLabel = new JLabel(" AVGS.");
                    titleLabel.setToolTipText("AVERAGE VALUES BY DEPTH FOR THE SELECTED NODE");
                    titleLabel.setFont(MySequentialGraphVars.tahomaBoldFont12);
                    titleLabel.setBackground(Color.WHITE);
                    titleLabel.setForeground(Color.DARK_GRAY);

                    JPanel titlePanel = new JPanel();
                    titlePanel.setBackground(Color.WHITE);
                    titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
                    titlePanel.add(titleLabel);

                    JButton enlargeBtn = new JButton("+");
                    enlargeBtn.setFont(MySequentialGraphVars.tahomaPlainFont11);
                    enlargeBtn.setFocusable(false);
                    enlargeBtn.setBackground(Color.WHITE);
                    enlargeBtn.addActionListener(new ActionListener() {
                        @Override public void actionPerformed(ActionEvent e) {
                            new Thread(new Runnable() {
                                @Override public void run() {
                                    enlarge();
                                }
                            }).start();
                        }
                    });
                    graphOptionComboBox.addActionListener(graphLevelAverageValuesByDepthLineChart);
                    JPanel menuPanel = new JPanel();
                    menuPanel.setLayout(new BorderLayout(0, 0));
                    menuPanel.setBackground(Color.WHITE);

                    JPanel btnPanel = new JPanel();
                    btnPanel.setBackground(Color.WHITE);
                    btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

                    btnPanel.add(graphOptionComboBox);
                    btnPanel.add(enlargeBtn);
                    menuPanel.add(titlePanel, BorderLayout.WEST);
                    menuPanel.add(btnPanel, BorderLayout.CENTER);
                    add(menuPanel, BorderLayout.NORTH);

                    renderer.setBaseLegendTextFont(MySequentialGraphVars.tahomaPlainFont11);
                    add(chartPanel, BorderLayout.CENTER);

                    revalidate();
                    repaint();
                } catch (Exception ex) {ex.printStackTrace();}
            }
        });
    }

    @Override public void actionPerformed(ActionEvent e) {
        new Thread(new Runnable() {
            @Override public void run() {
                if (e.getSource() == graphOptionComboBox) {
                    selectedOption = graphOptionComboBox.getSelectedIndex();
                    decorate();
                }
            }
        }).start();
    }

    public void enlarge() {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            JFrame f = new JFrame("AVERAGE VALUES BY DEPTH");
            f.setLayout(new BorderLayout());
            f.setBackground(Color.WHITE);
            f.setPreferredSize(new Dimension(450, 350));
            pb.updateValue(20, 100);
            MySingleNodeAverageValuesByDepthLineChart averageValueyDepthLineChart = new MySingleNodeAverageValuesByDepthLineChart();
            f.getContentPane().add(averageValueyDepthLineChart, BorderLayout.CENTER);
            f.pack();
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            pb.updateValue(60, 100);
            Border blackline = BorderFactory.createLineBorder(Color.black);
            TitledBorder titledBorder = BorderFactory.createTitledBorder(blackline, "");
            titledBorder.setTitleJustification(TitledBorder.LEFT);
            titledBorder.setTitleFont(new Font("Arial", Font.PLAIN, 0));
            titledBorder.setTitleColor(Color.DARK_GRAY);
            f.setAlwaysOnTop(true);
            f.setCursor(Cursor.HAND_CURSOR);
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
