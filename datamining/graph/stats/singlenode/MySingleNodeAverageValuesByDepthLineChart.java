package datamining.graph.stats.singlenode;

import datamining.graph.MyNode;
import datamining.main.MyProgressBar;
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
    XYSeries averageInContributionByDepthSeries = new XYSeries("IN-CONT.");
    XYSeries averageOutContributionByDepthSeries = new XYSeries("OUT-CONT.");
    XYSeries averageInOutDiffByDepthSeries = new XYSeries("DIFF..");
    private static boolean enlarged = false;

    public MySingleNodeAverageValuesByDepthLineChart() {
        decorate();
    }

    public synchronized void decorate() {
        final MySingleNodeAverageValuesByDepthLineChart graphLevelAverageValuesByDepthLineChart = this;
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                try {
                    removeAll();
                    MyNode selectedSingleNode = null;
                    if (MyVars.getViewer().vc.depthNodeNameSet != null && MyVars.getViewer().vc.depthNodeNameSet.size() == 1) {
                        selectedSingleNode = ((MyNode) MyVars.g.vRefs.get(MyVars.getViewer().vc.depthNodeNameSet.iterator().next()));
                    } else if (MyVars.getViewer().selectedNode != null) {
                        selectedSingleNode = MyVars.getViewer().selectedNode;
                    }


                    if (graphOptionComboBox == null) {
                        graphOptionComboBox = new JComboBox();
                        graphOptionComboBox.setFont(MyVars.tahomaPlainFont10);
                        graphOptionComboBox.setBackground(Color.WHITE);
                        graphOptionComboBox.setFocusable(false);
                        graphOptionComboBox.addItem("SELECT");
                        graphOptionComboBox.addItem("IN-CONT.");
                        graphOptionComboBox.addItem("OUT-CONT.");
                        graphOptionComboBox.addItem("DIFF.");
                        graphOptionComboBox.setSelectedIndex(selectedOption);
                    }

                    setLayout(new BorderLayout(5, 5));
                    setBackground(Color.WHITE);

                    Map<Integer, Double> averageInContributionByDepthMap = new HashMap<>();
                    Map<Integer, Double> averageOutContributionByDepthMap = new HashMap<>();
                    Map<Integer, Double> averageDiffByDepthMap = new HashMap<>();

                    for (int i = 1; i <= MyVars.mxDepth; i++) {
                        if (selectedSingleNode.getNodeDepthInfoMap().containsKey(i)) {
                            double inContTotal = selectedSingleNode.getNodeDepthInfo(i).getInContribution();
                            double outContTotal = selectedSingleNode.getNodeDepthInfo(i).getOutContribution();
                            double inContAvg = 0D;
                            double outContAvg = 0D;

                            if (inContTotal == 0) {
                                averageInContributionByDepthMap.put(i, 0D);
                            } else {
                                inContAvg = inContTotal/selectedSingleNode.getNodeDepthInfo(i).getPredecessorCount();
                                averageInContributionByDepthMap.put(i, inContAvg);
                            }

                            if (outContTotal == 0) {
                                averageOutContributionByDepthMap.put(i, 0D);
                            } else {
                                outContAvg = outContTotal/selectedSingleNode.getNodeDepthInfo(i).getSuccessorCount();
                                averageOutContributionByDepthMap.put(i, outContAvg);
                            }

                            if (inContTotal == 0 && outContTotal > 0) {
                                averageDiffByDepthMap.put(i, -outContAvg);
                            } else if (inContTotal == 0 && outContTotal == 0) {
                                averageDiffByDepthMap.put(i, 0D);
                            } else if (inContTotal > 0 && outContTotal == 0) {
                                averageDiffByDepthMap.put(i, inContAvg);
                            } else if (inContTotal > 0 && outContTotal > 0) {
                                averageDiffByDepthMap.put(i, inContAvg-outContAvg);
                            }
                        } else {
                            averageInContributionByDepthMap.put(i, 0D);
                            averageOutContributionByDepthMap.put(i, 0D);
                            averageDiffByDepthMap.put(i, 0D);
                        }
                    }

                    averageInContributionByDepthSeries = new XYSeries("IN-CONT.");
                    averageOutContributionByDepthSeries = new XYSeries("OUT-CONT.");
                    averageInOutDiffByDepthSeries = new XYSeries("DIFF..");

                    if (MyVars.currentGraphDepth == 0) {
                        for (int i = 1; i <= MyVars.mxDepth; i++) {
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

                            if (averageDiffByDepthMap.containsKey(i)) {
                                averageInOutDiffByDepthSeries.add(i, averageDiffByDepthMap.get(i));
                            } else {
                                averageInOutDiffByDepthSeries.add(i, 0);
                            }
                        }
                    } else {
                        for (int i = 1; i <= MyVars.mxDepth; i++) {
                            if (MyVars.currentGraphDepth == i) {
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
                                if (averageDiffByDepthMap.containsKey(i)) {
                                    averageInOutDiffByDepthSeries.add(i, averageDiffByDepthMap.get(i));
                                } else {
                                    averageInOutDiffByDepthSeries.add(i, 0);
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

                                if (averageDiffByDepthMap.containsKey(i)) {
                                    averageInOutDiffByDepthSeries.add(i, averageDiffByDepthMap.get(i));
                                } else {
                                    averageInOutDiffByDepthSeries.add(i, 0);
                                }
                            }
                        }
                    }

                    XYSeriesCollection dataset = new XYSeriesCollection();
                    if (graphOptionComboBox.getSelectedIndex() == 0) {
                        dataset.addSeries(averageInContributionByDepthSeries);
                        dataset.addSeries(averageOutContributionByDepthSeries);
                    } else if (graphOptionComboBox.getSelectedIndex() == 1) {
                        dataset.addSeries(averageInContributionByDepthSeries);
                    } else if (graphOptionComboBox.getSelectedIndex() == 2) {
                        dataset.addSeries(averageOutContributionByDepthSeries);
                    } else if (graphOptionComboBox.getSelectedIndex() == 3) {
                        dataset.addSeries(averageInOutDiffByDepthSeries);
                    }

                    JFreeChart chart = ChartFactory.createXYLineChart("", "DEPTH", "", dataset);
                    chart.getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
                    chart.getXYPlot().setBackgroundPaint(Color.WHITE);
                    chart.getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
                    chart.getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);
                    chart.getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
                    chart.getXYPlot().getDomainAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
                    chart.getXYPlot().getDomainAxis().setTickLabelFont(MyVars.tahomaPlainFont11);
                    chart.getXYPlot().getRangeAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
                    chart.getXYPlot().getRangeAxis().setTickLabelFont(MyVars.tahomaPlainFont11);
                    chart.getXYPlot().getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
                    chart.setBackgroundPaint(Color.WHITE);

                    XYPlot plot = (XYPlot) chart.getPlot();
                    XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
                    if (graphOptionComboBox.getSelectedIndex() == 0) {
                        renderer.setSeriesPaint(0, Color.BLUE);
                        renderer.setSeriesStroke(0, new BasicStroke(1.3f));
                        renderer.setSeriesShapesVisible(0, true);
                        renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                        renderer.setSeriesFillPaint(0, Color.WHITE);

                        renderer.setSeriesPaint(1, Color.BLACK);
                        renderer.setSeriesStroke(1, new BasicStroke(1.3f));
                        renderer.setSeriesShapesVisible(1, true);
                        renderer.setSeriesShape(1, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                        renderer.setSeriesFillPaint(1, Color.WHITE);

                        renderer.setSeriesPaint(2, Color.RED);
                        renderer.setSeriesStroke(2, new BasicStroke(1.3f));
                        renderer.setSeriesShapesVisible(2, true);
                        renderer.setSeriesShape(2, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                        renderer.setSeriesFillPaint(2, Color.WHITE);
                    } else if (graphOptionComboBox.getSelectedIndex() == 1) {
                        renderer.setSeriesPaint(0, Color.BLUE);
                        renderer.setSeriesStroke(0, new BasicStroke(1.3f));
                        renderer.setSeriesShapesVisible(0, true);
                        renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                    } else if (graphOptionComboBox.getSelectedIndex() == 2) {
                        renderer.setSeriesPaint(0, Color.BLACK);
                        renderer.setSeriesStroke(0, new BasicStroke(1.5f));
                        renderer.setSeriesShapesVisible(0, true);
                        renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                    } else if (graphOptionComboBox.getSelectedIndex() == 3) {
                        renderer.setSeriesPaint(0, Color.RED);
                        renderer.setSeriesStroke(0, new BasicStroke(1.5f));
                        renderer.setSeriesShapesVisible(0, true);
                        renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                    }
                    renderer.setUseFillPaint(true);
                    ChartPanel chartPanel = new ChartPanel(chart);
                    chartPanel.setPreferredSize(new Dimension(560, 367));

                    JLabel titleLabel = new JLabel(" AVG.");
                    titleLabel.setToolTipText("AVERAGE VALUES BY DEPTH FOR THE SELECTED NODE");
                    titleLabel.setFont(MyVars.tahomaBoldFont11);
                    titleLabel.setBackground(Color.WHITE);
                    titleLabel.setForeground(Color.DARK_GRAY);

                    JPanel titlePanel = new JPanel();
                    titlePanel.setBackground(Color.WHITE);
                    titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
                    titlePanel.add(titleLabel);

                    JButton enlargeBtn = new JButton("+");
                    enlargeBtn.setFont(MyVars.tahomaPlainFont10);
                    enlargeBtn.setFocusable(false);
                    enlargeBtn.setBackground(Color.WHITE);
                    enlargeBtn.addActionListener(new ActionListener() {
                        @Override public void actionPerformed(ActionEvent e) {
                            new Thread(new Runnable() {
                                @Override public void run() {
                                    enlarged = true;
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

                    renderer.setBaseLegendTextFont(MyVars.tahomaPlainFont11);
                    add(chartPanel, BorderLayout.CENTER);
                } catch (Exception ex) {ex.printStackTrace();}
            }
        });
    }

    @Override public void actionPerformed(ActionEvent e) {
        new Thread(new Runnable() {
            @Override public void run() {
                if (e.getSource() == graphOptionComboBox) {
                    if (!enlarged) {instances = 0;}
                    selectedOption = graphOptionComboBox.getSelectedIndex();
                    graphOptionComboBox = null;
                    removeAll();
                    decorate();
                    revalidate();
                    repaint();
                }
            }
        }).start();
    }

    public void enlarge() {
        MyProgressBar pb = new MyProgressBar(false);
        JFrame frame = new JFrame("AVERAGE VALUES BY DEPTH");
        frame.setLayout(new BorderLayout());
        frame.setBackground(Color.WHITE);
        frame.setPreferredSize(new Dimension(450, 350));
        pb.updateValue(20, 100);
        MySingleNodeAverageValuesByDepthLineChart averageValueyDepthLineChart = new MySingleNodeAverageValuesByDepthLineChart();
        frame.getContentPane().add(averageValueyDepthLineChart, BorderLayout.CENTER);
        frame.pack();
        frame.addWindowListener(new WindowListener() {
            @Override public void windowOpened(WindowEvent e) {}
            @Override public void windowClosing(WindowEvent e) {
                enlarged = false;
            }
            @Override public void windowClosed(WindowEvent e) {}
            @Override public void windowIconified(WindowEvent e) {}
            @Override public void windowDeiconified(WindowEvent e) {}
            @Override public void windowActivated(WindowEvent e) {}
            @Override public void windowDeactivated(WindowEvent e) {}
        });
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pb.updateValue(60, 100);
        Border blackline = BorderFactory.createLineBorder(Color.black);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(blackline, "");
        titledBorder.setTitleJustification(TitledBorder.LEFT);
        titledBorder.setTitleFont(new Font("Arial", Font.PLAIN, 0));
        titledBorder.setTitleColor(Color.DARK_GRAY);
        //averageValueyDepthLineChart.setBorder(titledBorder);
        pb.updateValue(100,100);
        pb.dispose();
        frame.setCursor(Cursor.HAND_CURSOR);
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);
        frame.setAlwaysOnTop(false);
    }
}
