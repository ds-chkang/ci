package datamining.graph.stats;

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
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.Ellipse2D;
import java.util.*;

public class MyGraphLevelAverageValuesByDepthLineChart
extends JPanel
implements ActionListener {

    private JComboBox graphOptionComboBox;
    private int selectedOption = 0;
    public static int instances = 0;
    private static boolean enlarged = false;

    XYSeries totalNodesByDepthSereis = new XYSeries("TOTAL NODE");
    XYSeries totalContributionByDepthSereis = new XYSeries("TOTAL CONT.");
    XYSeries totalInContributionByDepthSereis = new XYSeries("TOTAL IN-CONT.");
    XYSeries totalOutContributionByDepthSereis = new XYSeries("TOTAL OUT-CONT.");
    XYSeries totalPredecessorByDepthSereis = new XYSeries("TOTAL P.");
    XYSeries totalSuccessorByDepthSereis = new XYSeries("TOTAL S.");
    XYSeries totalReachTimeByDepthSereis = new XYSeries("REACH TIME");
    XYSeries averageContributionByDepthSeries = new XYSeries("CONT.");
    XYSeries averageInContributionByDepthSeries = new XYSeries("IN-CONT.");
    XYSeries averageOutContributionByDepthSeries = new XYSeries("OUT-CONT.");
    XYSeries averagePredecessorByDepthSeries = new XYSeries("P.");
    XYSeries averageSuccessorByDepthSeries = new XYSeries("S.");
    XYSeries averageReachTimeByDepthSeries = new XYSeries("REACh TIME");

    public MyGraphLevelAverageValuesByDepthLineChart() {
        decorate();
    }

    public void decorate() {
        try {
            final Set<String> filterNodes = new HashSet<>();
            if (MyVars.getViewer().selectedNode != null) {
                for (MyNode n : MyVars.getViewer().selectedSingleNodePredecessors) {
                    if (n.getCurrentValue() == 0) {
                        filterNodes.add(n.getName());
                    }
                }

                for (MyNode n : MyVars.getViewer().selectedSingleNodeSuccessors) {
                    if (n.getCurrentValue() == 0) {
                        filterNodes.add(n.getName());
                    }
                }
            } else {
                Collection<MyNode> nodes = MyVars.g.getVertices();
                for (MyNode n : nodes) {
                    if (n.getCurrentValue() == 0) {
                        filterNodes.add(n.getName());
                    }
                }
            }

            final MyGraphLevelAverageValuesByDepthLineChart graphLevelAverageValuesByDepthLineChart = this;
            SwingUtilities.invokeLater(new Runnable() {
                @Override public void run() {
                    removeAll();
                    if (graphOptionComboBox == null) {
                        graphOptionComboBox = new JComboBox();
                        graphOptionComboBox.setFont(MyVars.tahomaPlainFont10);
                        graphOptionComboBox.setBackground(Color.WHITE);
                        graphOptionComboBox.setFocusable(false);
                        graphOptionComboBox.addItem("SELECT");
                        graphOptionComboBox.addItem("CONT.");
                        graphOptionComboBox.addItem("IN-CONT.");
                        graphOptionComboBox.addItem("OUT-CONT.");
                        graphOptionComboBox.addItem("PRED.");
                        graphOptionComboBox.addItem("SUCC.");
                        graphOptionComboBox.setSelectedIndex(selectedOption);
                    }

                    setLayout(new BorderLayout(5, 5));
                    setBackground(Color.WHITE);

                    Map<Integer, Set<MyNode>> totalNodesByDepth = new HashMap<>();
                    Map<Integer, Integer> totalContributionByDepthMap = new HashMap<>();
                    Map<Integer, Integer> totalInContributionByDepthMap = new HashMap<>();
                    Map<Integer, Integer> totalOutContributionByDepthMap = new HashMap<>();
                    Map<Integer, Integer> totalPredecessorByDepthMap = new HashMap<>();
                    Map<Integer, Integer> totalSuccessorByDepthMap = new HashMap<>();
                    Map<Integer, Double> averagePredecessorByDepthMap = new HashMap<>();
                    Map<Integer, Double> averageSuccessorByDepthMap = new HashMap<>();
                    Map<Integer, Double> averageContributionByDepthMap = new HashMap<>();
                    Map<Integer, Double> averageInContributionByDepthMap = new HashMap<>();
                    Map<Integer, Double> averageOutContributionByDepthMap = new HashMap<>();
                    Collection<MyNode> nodes = MyVars.g.getVertices();

                    for (int i = 1; i <= MyVars.mxDepth; i++) {
                        int count = 0;
                        double reachTimeTotal = 0;
                        double contTotal = 0;
                        double inContTotal = 0;
                        double outContTotal = 0;
                        double predecessorTotal = 0;
                        double successorTotal = 0;
                        for (MyNode n : nodes) {
                            if (!filterNodes.contains(n.getName())) {
                                if (n.getNodeDepthInfoMap().containsKey(i)) {
                                    contTotal += n.getNodeDepthInfo(i).getContribution();
                                    inContTotal += n.getNodeDepthInfo(i).getInContribution();
                                    outContTotal += n.getNodeDepthInfo(i).getOutContribution();
                                    predecessorTotal += n.getNodeDepthInfo(i).getPredecessorCount();
                                    successorTotal += n.getNodeDepthInfo(i).getSuccessorCount();
                                    reachTimeTotal += n.getNodeDepthInfo(i).getReachTime();
                                    count++;
                                    if (!totalNodesByDepth.containsKey(i)) {
                                        Set<MyNode> depthNodes = new HashSet<>();
                                        depthNodes.add(n);
                                        totalNodesByDepth.put(i, depthNodes);
                                    } else {
                                        totalNodesByDepth.get(i).add(n);
                                    }
                                }
                            }
                        }

                        totalContributionByDepthMap.put(i, (int) contTotal);
                        totalInContributionByDepthMap.put(i, (int) inContTotal);
                        totalOutContributionByDepthMap.put(i, (int) outContTotal);
                        totalPredecessorByDepthMap.put(i, (int) predecessorTotal);
                        totalSuccessorByDepthMap.put(i, (int) successorTotal);

                        averageContributionByDepthMap.put(i, ((count == 0) ? 0 : (contTotal / count)));
                        averageInContributionByDepthMap.put(i, ((count == 0) ? 0 : (inContTotal / count)));
                        averageOutContributionByDepthMap.put(i, ((count == 0) ? 0 : (outContTotal / count)));
                        averagePredecessorByDepthMap.put(i, ((count == 0) ? 0 : (predecessorTotal / count)));
                        averageSuccessorByDepthMap.put(i, ((count == 0) ? 0 : (successorTotal / count)));
                    }


                    totalNodesByDepthSereis = new XYSeries("TOTAL NODE");
                    totalContributionByDepthSereis = new XYSeries("TOTAL CONT.");
                    totalInContributionByDepthSereis = new XYSeries("TOTAL IN-CONT.");
                    totalOutContributionByDepthSereis = new XYSeries("TOTAL OUT-CONT.");
                    totalPredecessorByDepthSereis = new XYSeries("TOTAL P.");
                    totalSuccessorByDepthSereis = new XYSeries("TOTAL S.");
                    totalReachTimeByDepthSereis = new XYSeries("REACH TIME");

                    averageContributionByDepthSeries = new XYSeries("CONT.");
                    averageInContributionByDepthSeries = new XYSeries("IN-CONT.");
                    averageOutContributionByDepthSeries = new XYSeries("OUT-CONT.");
                    averagePredecessorByDepthSeries = new XYSeries("P.");
                    averageSuccessorByDepthSeries = new XYSeries("S.");
                    averageReachTimeByDepthSeries = new XYSeries("REACH TIME");

                    if (MyVars.currentGraphDepth == 0) {
                        for (int i = 1; i <= MyVars.mxDepth; i++) {
                            if (averageContributionByDepthMap.containsKey(i)) {
                                averageContributionByDepthSeries.add(i, averageContributionByDepthMap.get(i));
                            } else {
                                averageContributionByDepthSeries.add(i, 0);
                            }
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
                            if (averagePredecessorByDepthMap.containsKey(i)) {
                                averagePredecessorByDepthSeries.add(i, averagePredecessorByDepthMap.get(i));
                            } else {
                                averagePredecessorByDepthSeries.add(i, 0);
                            }
                            if (averageSuccessorByDepthMap.containsKey(i)) {
                                averageSuccessorByDepthSeries.add(i, averageSuccessorByDepthMap.get(i));
                            } else {
                                averageSuccessorByDepthSeries.add(i, 0);
                            }
                            if (totalContributionByDepthMap.containsKey(i)) {
                                totalContributionByDepthSereis.add(i, totalContributionByDepthMap.get(i));
                            } else {
                                totalContributionByDepthSereis.add(i, 0);
                            }
                            if (totalInContributionByDepthMap.containsKey(i)) {
                                totalInContributionByDepthSereis.add(i, totalInContributionByDepthMap.get(i));
                            } else {
                                totalInContributionByDepthSereis.add(i, 0);
                            }
                            if (totalOutContributionByDepthMap.containsKey(i)) {
                                totalOutContributionByDepthSereis.add(i, totalOutContributionByDepthMap.get(i));
                            } else {
                                totalOutContributionByDepthSereis.add(i, 0);
                            }
                            if (totalPredecessorByDepthMap.containsKey(i)) {
                                totalPredecessorByDepthSereis.add(i, totalPredecessorByDepthMap.get(i));
                            } else {
                                totalPredecessorByDepthSereis.add(i, 0);
                            }
                            if (totalPredecessorByDepthMap.containsKey(i)) {
                                totalSuccessorByDepthSereis.add(i, totalSuccessorByDepthMap.get(i));
                            } else {
                                totalSuccessorByDepthSereis.add(i, 0);
                            }
                            if (totalNodesByDepth.containsKey(i)) {
                                totalNodesByDepthSereis.add(i, totalNodesByDepth.get(i).size());
                            } else {
                                totalNodesByDepthSereis.add(i, 0);
                            }
                        }
                    } else {
                        for (int i = 1; i <= MyVars.mxDepth; i++) {
                            if (MyVars.currentGraphDepth == i) {
                                if (averageContributionByDepthMap.containsKey(i)) {
                                    averageContributionByDepthSeries.add(i, averageContributionByDepthMap.get(i));
                                } else {
                                    averageContributionByDepthSeries.add(i, 0);
                                }
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
                                if (averagePredecessorByDepthMap.containsKey(i)) {
                                    averagePredecessorByDepthSeries.add(i, averagePredecessorByDepthMap.get(i));
                                } else {
                                    averagePredecessorByDepthSeries.add(i, 0);
                                }
                                if (averageSuccessorByDepthMap.containsKey(i)) {
                                    averageSuccessorByDepthSeries.add(i, averageSuccessorByDepthMap.get(i));
                                } else {
                                    averageSuccessorByDepthSeries.add(i, 0);
                                }
                                if (totalContributionByDepthMap.containsKey(i)) {
                                    totalContributionByDepthSereis.add(i, totalContributionByDepthMap.get(i));
                                } else {
                                    totalContributionByDepthSereis.add(i, 0);
                                }
                                if (totalInContributionByDepthMap.containsKey(i)) {
                                    totalInContributionByDepthSereis.add(i, totalInContributionByDepthMap.get(i));
                                } else {
                                    totalInContributionByDepthSereis.add(i, 0);
                                }
                                if (totalOutContributionByDepthMap.containsKey(i)) {
                                    totalOutContributionByDepthSereis.add(i, totalOutContributionByDepthMap.get(i));
                                } else {
                                    totalOutContributionByDepthSereis.add(i, 0);
                                }
                                if (totalPredecessorByDepthMap.containsKey(i)) {
                                    totalPredecessorByDepthSereis.add(i, totalPredecessorByDepthMap.get(i));
                                } else {
                                    totalPredecessorByDepthSereis.add(i, 0);
                                }
                                if (totalPredecessorByDepthMap.containsKey(i)) {
                                    totalSuccessorByDepthSereis.add(i, totalSuccessorByDepthMap.get(i));
                                } else {
                                    totalSuccessorByDepthSereis.add(i, 0);
                                }
                                if (totalNodesByDepth.containsKey(i)) {
                                    totalNodesByDepthSereis.add(i, totalNodesByDepth.get(i).size());
                                } else {
                                    totalNodesByDepthSereis.add(i, 0);
                                }
                            } else {
                                averageContributionByDepthSeries.add(i, 0);
                                averageInContributionByDepthSeries.add(i, 0);
                                averageOutContributionByDepthSeries.add(i, 0);
                                averagePredecessorByDepthSeries.add(i, 0);
                                averageSuccessorByDepthSeries.add(i, 0);
                                totalContributionByDepthSereis.add(i, 0);
                                totalInContributionByDepthSereis.add(i, 0);
                                totalOutContributionByDepthSereis.add(i, 0);
                                totalPredecessorByDepthSereis.add(i, 0);
                                totalSuccessorByDepthSereis.add(i, 0);
                                totalNodesByDepthSereis.add(i, 0);
                            }
                        }
                    }

                    XYSeriesCollection dataset = new XYSeriesCollection();
                    if (graphOptionComboBox.getSelectedIndex() == 0) {
                        dataset.addSeries(averageContributionByDepthSeries);
                        dataset.addSeries(averageInContributionByDepthSeries);
                        dataset.addSeries(averageOutContributionByDepthSeries);
                        dataset.addSeries(averagePredecessorByDepthSeries);
                        dataset.addSeries(averageSuccessorByDepthSeries);
                    } else if (graphOptionComboBox.getSelectedIndex() == 1) {
                        dataset.addSeries(averageContributionByDepthSeries);
                    } else if (graphOptionComboBox.getSelectedIndex() == 2) {
                        dataset.addSeries(averageInContributionByDepthSeries);
                    } else if (graphOptionComboBox.getSelectedIndex() == 3) {
                        dataset.addSeries(averageOutContributionByDepthSeries);
                    } else if (graphOptionComboBox.getSelectedIndex() == 4) {
                        dataset.addSeries(averagePredecessorByDepthSeries);
                    } else if (graphOptionComboBox.getSelectedIndex() == 5) {
                        dataset.addSeries(averageSuccessorByDepthSeries);
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
                        renderer.setSeriesPaint(0, Color.MAGENTA);
                        renderer.setSeriesStroke(0, new BasicStroke(1.3f));
                        renderer.setSeriesShapesVisible(0, true);
                        renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                        renderer.setSeriesFillPaint(0, Color.WHITE);

                        renderer.setSeriesPaint(1, Color.DARK_GRAY);
                        renderer.setSeriesStroke(1, new BasicStroke(1.3f));
                        renderer.setSeriesShapesVisible(1, true);
                        renderer.setSeriesShape(1, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                        renderer.setSeriesFillPaint(1, Color.WHITE);

                        renderer.setSeriesPaint(2, Color.decode("#F0CF4C"));
                        renderer.setSeriesStroke(2, new BasicStroke(1.3f));
                        renderer.setSeriesShapesVisible(2, true);
                        renderer.setSeriesShape(2, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                        renderer.setSeriesFillPaint(2, Color.WHITE);

                        renderer.setSeriesPaint(3, Color.RED);
                        renderer.setSeriesStroke(3, new BasicStroke(1.3f));
                        renderer.setSeriesShapesVisible(3, true);
                        renderer.setSeriesShape(3, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                        renderer.setSeriesFillPaint(3, Color.WHITE);

                        renderer.setSeriesPaint(4, Color.decode("#59A869"));
                        renderer.setSeriesStroke(4, new BasicStroke(1.3f));
                        renderer.setSeriesShapesVisible(4, true);
                        renderer.setSeriesShape(4, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                        renderer.setSeriesFillPaint(4, Color.WHITE);
                        renderer.setUseFillPaint(true);
                    } else if (graphOptionComboBox.getSelectedIndex() == 1) {
                        renderer.setSeriesPaint(0, Color.MAGENTA);
                        renderer.setSeriesStroke(0, new BasicStroke(1.5f));
                        renderer.setSeriesShapesVisible(0, true);
                        renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                        renderer.setSeriesFillPaint(0, Color.WHITE);
                        renderer.setUseFillPaint(true);
                    } else if (graphOptionComboBox.getSelectedIndex() == 2) {
                        renderer.setSeriesPaint(0, Color.DARK_GRAY);
                        renderer.setSeriesStroke(0, new BasicStroke(1.5f));
                        renderer.setSeriesShapesVisible(0, true);
                        renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                        renderer.setSeriesFillPaint(0, Color.WHITE);
                        renderer.setUseFillPaint(true);
                    } else if (graphOptionComboBox.getSelectedIndex() == 3) {
                        renderer.setSeriesPaint(0, Color.PINK);
                        renderer.setSeriesStroke(0, new BasicStroke(1.5f));
                        renderer.setSeriesShapesVisible(0, true);
                        renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                        renderer.setSeriesFillPaint(0, Color.WHITE);
                        renderer.setUseFillPaint(true);
                    } else if (graphOptionComboBox.getSelectedIndex() == 4) {
                        renderer.setSeriesPaint(0, Color.RED);
                        renderer.setSeriesStroke(0, new BasicStroke(1.5f));
                        renderer.setSeriesShapesVisible(0, true);
                        renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                        renderer.setSeriesFillPaint(0, Color.WHITE);
                        renderer.setUseFillPaint(true);
                    } else if (graphOptionComboBox.getSelectedIndex() == 5) {
                        renderer.setSeriesPaint(0, Color.RED);
                        renderer.setSeriesStroke(0, new BasicStroke(1.5f));
                        renderer.setSeriesShapesVisible(0, true);
                        renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                        renderer.setSeriesFillPaint(0, Color.WHITE);
                        renderer.setUseFillPaint(true);
                    }
                    ChartPanel chartPanel = new ChartPanel(chart);
                    chartPanel.setPreferredSize(new Dimension(560, 367));

                    JLabel titleLabel = new JLabel(" AVG.");
                    titleLabel.setToolTipText("AVERAGE VALUES BY DEPTH");
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
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    enlarged = true;
                                    enlarge();
                                }
                            }).start();
                        }
                    });

                    JComboBox depthMenu = new JComboBox();
                    depthMenu.setFocusable(false);
                    depthMenu.setFont(MyVars.tahomaPlainFont10);
                    depthMenu.setBackground(Color.WHITE);
                    depthMenu.addItem("DEPTH");
                    for (int i = 1; i <= MyVars.mxDepth; i++) {
                        depthMenu.addItem("" + i);
                    }

                    graphOptionComboBox.addActionListener(graphLevelAverageValuesByDepthLineChart);
                    JPanel menuPanel = new JPanel();
                    menuPanel.setLayout(new BorderLayout(0, 0));
                    menuPanel.setBackground(Color.WHITE);
                    JPanel btnPanel = new JPanel();
                    btnPanel.setBackground(Color.WHITE);
                    btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

                    btnPanel.add(graphOptionComboBox);
                    //btnPanel.add(depthMenu);
                    btnPanel.add(enlargeBtn);
                    menuPanel.add(titlePanel, BorderLayout.WEST);
                    menuPanel.add(btnPanel, BorderLayout.CENTER);
                    add(menuPanel, BorderLayout.NORTH);

                    renderer.setBaseLegendTextFont(MyVars.tahomaPlainFont11);
                    add(chartPanel, BorderLayout.CENTER);
                }
            });
            revalidate();
            repaint();
        } catch (Exception ex) {ex.printStackTrace();}
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
        MyGraphLevelAverageValuesByDepthLineChart averageValueyDepthLineChart = new MyGraphLevelAverageValuesByDepthLineChart();
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
