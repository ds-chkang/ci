package medousa.sequential.graph.stats;

import medousa.sequential.graph.MyComboBoxTooltipRenderer;
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
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.util.*;

public class MyGraphLevelAverageValuesByDepthLineChart
extends JPanel
implements ActionListener {

    private JComboBox graphOptionComboBox;
    private int selectedOption = 0;
    public static int instances = 0;
    private static boolean MAXIMIZED = false;

    XYSeries totalNodesByDepthSereis = new XYSeries("TOTAL NODE");
    XYSeries totalContributionByDepthSereis = new XYSeries("TOTAL CONT.");
    XYSeries totalInContributionByDepthSereis = new XYSeries("TOTAL IN-CONT.");
    XYSeries totalOutContributionByDepthSereis = new XYSeries("TOTAL OUT-CONT.");
    XYSeries totalPredecessorByDepthSereis = new XYSeries("TOTAL P.");
    XYSeries totalSuccessorByDepthSereis = new XYSeries("TOTAL S.");
    XYSeries totalReachTimeByDepthSereis = new XYSeries("TOTAL REACH TIME");
    XYSeries totalDurationByDepthSereis = new XYSeries("TOTAL DURATION");
    XYSeries averageContributionByDepthSeries = new XYSeries("CONT.");
    XYSeries averageInContributionByDepthSeries = new XYSeries("IN-CONT.");
    XYSeries averageOutContributionByDepthSeries = new XYSeries("OUT-CONT.");
    XYSeries averagePredecessorByDepthSeries = new XYSeries("P.");
    XYSeries averageSuccessorByDepthSeries = new XYSeries("S.");
    XYSeries averageReachTimeByDepthSeries = new XYSeries("REACH TIME");
    XYSeries averageDurationByDepthSeries = new XYSeries("DURATION");

    public MyGraphLevelAverageValuesByDepthLineChart() {
        decorate();
    }

    public void decorate() {
        try {
            final MyGraphLevelAverageValuesByDepthLineChart graphLevelAverageValuesByDepthLineChart = this;
            SwingUtilities.invokeLater(new Runnable() {
                @Override public void run() {
                    final Set<String> filterNodes = new HashSet<>();
                    if (MySequentialGraphVars.getSequentialGraphViewer().singleNode != null) {
                        for (MyNode n : MySequentialGraphVars.getSequentialGraphViewer().singleNodePredecessors) {
                            if (n.getCurrentValue() == 0) {
                                filterNodes.add(n.getName());
                            }
                        }

                        for (MyNode n : MySequentialGraphVars.getSequentialGraphViewer().singleNodeSuccessors) {
                            if (n.getCurrentValue() == 0) {
                                filterNodes.add(n.getName());
                            }
                        }
                    } else {
                        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
                        for (MyNode n : nodes) {
                            if (n.getCurrentValue() == 0) {
                                filterNodes.add(n.getName());
                            }
                        }
                    }
                    removeAll();

                    if (graphOptionComboBox == null) {
                        graphOptionComboBox = new JComboBox();
                        String[] tooltips = {
                                "SELECT AN AVERAGE VALUE",
                                "AVERAGE CONTRIBUTION BY DEPTH ",
                                "AVERAGE IN-CONTRIBUTION BY DEPTH ",
                                "AVERAGE OUT-CONTRIBUTION BY DEPTH",
                                "AVERAGE PREDECESSORS BY DEPTH",
                                "AVERAGE SUCCESSORS BY DEPTH",
                                "AVERAGE REACH TIME BY DEPTH",
                                "AVERAGE DURATION BY DEPTH"};
                        graphOptionComboBox.setRenderer(new MyComboBoxTooltipRenderer(tooltips));
                        graphOptionComboBox.setFont(MySequentialGraphVars.tahomaPlainFont11);
                        graphOptionComboBox.setBackground(Color.WHITE);
                        graphOptionComboBox.setFocusable(false);
                        graphOptionComboBox.addItem("SELECT");
                        graphOptionComboBox.addItem("CONT.");
                        graphOptionComboBox.addItem("IN-CONT.");
                        graphOptionComboBox.addItem("OUT-CONT.");
                        graphOptionComboBox.addItem("PRED.");
                        graphOptionComboBox.addItem("SUCC.");
                        graphOptionComboBox.addItem("REACH T.");
                        graphOptionComboBox.addItem("DURATION");
                        graphOptionComboBox.setSelectedIndex(selectedOption);
                    }

                    setLayout(new BorderLayout(3, 3));
                    setBackground(Color.WHITE);

                    Map<Integer, Long> totalReachTimeByDepthMap = new HashMap<>();
                    Map<Integer, Double> totalDurationByDepthMap = new HashMap<>();
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
                    Map<Integer, Double> averageReachTimeByDepthMap = new HashMap<>();
                    Map<Integer, Double> averageDurationByDepthMap = new HashMap<>();
                    Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();

                    for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                        int count = 0;
                        double totalDuration = 0;
                        double totalReachTime = 0;
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
                                    totalReachTime += n.getNodeDepthInfo(i).getReachTime();
                                    totalDuration += n.getNodeDepthInfo(i).getDuration();
                                    count++;
                                }
                            }
                        }

                        totalDurationByDepthMap.put(i, totalDuration);
                        totalContributionByDepthMap.put(i, (int) contTotal);
                        totalInContributionByDepthMap.put(i, (int) inContTotal);
                        totalOutContributionByDepthMap.put(i, (int) outContTotal);
                        totalPredecessorByDepthMap.put(i, (int) predecessorTotal);
                        totalSuccessorByDepthMap.put(i, (int) successorTotal);
                        totalReachTimeByDepthMap.put(i, (long) totalReachTime);

                        averageContributionByDepthMap.put(i, ((count == 0) ? 0 : (contTotal / count)));
                        averageInContributionByDepthMap.put(i, ((count == 0) ? 0 : (inContTotal / count)));
                        averageOutContributionByDepthMap.put(i, ((count == 0) ? 0 : (outContTotal / count)));
                        averagePredecessorByDepthMap.put(i, ((count == 0) ? 0 : (predecessorTotal / count)));
                        averageSuccessorByDepthMap.put(i, ((count == 0) ? 0 : (successorTotal / count)));
                        averageReachTimeByDepthMap.put(i, ((count == 0) ? 0 : (totalReachTime / count)));
                        averageDurationByDepthMap.put(i, ((count == 0) ? 0 : (totalDuration / count)));
                    }

                    totalNodesByDepthSereis = new XYSeries("TOTAL NODE");
                    totalContributionByDepthSereis = new XYSeries("TOTAL CONT.");
                    totalInContributionByDepthSereis = new XYSeries("TOTAL IN-CONT.");
                    totalOutContributionByDepthSereis = new XYSeries("TOTAL OUT-CONT.");
                    totalPredecessorByDepthSereis = new XYSeries("TOTAL P.");
                    totalSuccessorByDepthSereis = new XYSeries("TOTAL S.");
                    totalReachTimeByDepthSereis = new XYSeries("TOTAL REACH TIME");
                    totalDurationByDepthSereis = new XYSeries("TOTAL DURATION");

                    averageContributionByDepthSeries = new XYSeries("AVG. CONT.");
                    averageInContributionByDepthSeries = new XYSeries("AVG. IN-CONT.");
                    averageOutContributionByDepthSeries = new XYSeries("AVG. OUT-CONT.");
                    averagePredecessorByDepthSeries = new XYSeries("AVG. P.");
                    averageSuccessorByDepthSeries = new XYSeries("AVG. S.");
                    averageReachTimeByDepthSeries = new XYSeries("AVG. REACH TIME");
                    averageDurationByDepthSeries = new XYSeries("AVG. DURATION");

                    if (MySequentialGraphVars.currentGraphDepth == 0) {
                        for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
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
                            if (averageReachTimeByDepthMap.containsKey(i)) {
                                averageReachTimeByDepthSeries.add(i, averageReachTimeByDepthMap.get(i));
                            } else {
                                averageReachTimeByDepthSeries.add(i, 0);
                            }
                            if (averageDurationByDepthMap.containsKey(i)) {
                                averageDurationByDepthSeries.add(i, averageDurationByDepthMap.get(i));
                            } else {
                                averageDurationByDepthSeries.add(i, 0);
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
                            if (totalReachTimeByDepthMap.containsKey(i)) {
                                totalReachTimeByDepthSereis.add(i, totalReachTimeByDepthMap.get(i));
                            } else {
                                totalReachTimeByDepthSereis.add(i, 0);
                            }
                            if (totalDurationByDepthMap.containsKey(i)) {
                                totalDurationByDepthSereis.add(i, totalDurationByDepthMap.get(i));
                            } else {
                                totalDurationByDepthSereis.add(i, 0);
                            }
                        }
                    } else {
                        for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                            if (MySequentialGraphVars.currentGraphDepth == i) {
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
                                if (averageReachTimeByDepthMap.containsKey(i)) {
                                    averageReachTimeByDepthSeries.add(i, averageReachTimeByDepthMap.get(i));
                                } else {
                                    averageReachTimeByDepthSeries.add(i, 0);
                                }
                                if (averageDurationByDepthMap.containsKey(i)) {
                                    averageDurationByDepthSeries.add(i, averageDurationByDepthMap.get(i));
                                } else {
                                    averageDurationByDepthSeries.add(i, 0);
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
                                if (totalReachTimeByDepthMap.containsKey(i)) {
                                    totalReachTimeByDepthSereis.add(i, totalReachTimeByDepthMap.get(i));
                                } else {
                                    totalReachTimeByDepthSereis.add(i, 0);
                                }
                                if (totalDurationByDepthMap.containsKey(i)) {
                                    totalDurationByDepthSereis.add(i, totalDurationByDepthMap.get(i));
                                } else {
                                    totalDurationByDepthSereis.add(i, 0);
                                }
                            } else {
                                averageContributionByDepthSeries.add(i, 0);
                                averageInContributionByDepthSeries.add(i, 0);
                                averageOutContributionByDepthSeries.add(i, 0);
                                averagePredecessorByDepthSeries.add(i, 0);
                                averageSuccessorByDepthSeries.add(i, 0);
                                averageReachTimeByDepthSeries.add(i, 0);
                                averageDurationByDepthSeries.add(i, 0);
                                totalContributionByDepthSereis.add(i, 0);
                                totalInContributionByDepthSereis.add(i, 0);
                                totalOutContributionByDepthSereis.add(i, 0);
                                totalPredecessorByDepthSereis.add(i, 0);
                                totalSuccessorByDepthSereis.add(i, 0);
                                totalReachTimeByDepthSereis.add(i, 0);
                                totalDurationByDepthSereis.add(i, 0);
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
                        dataset.addSeries(averageReachTimeByDepthSeries);
                        dataset.addSeries(averageDurationByDepthSeries);
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
                    } else if (graphOptionComboBox.getSelectedIndex() == 6) {
                        dataset.addSeries(averageReachTimeByDepthSeries);
                    } else if (graphOptionComboBox.getSelectedIndex() == 7) {
                        dataset.addSeries(averageDurationByDepthSeries);
                    }

                    JFreeChart chart = ChartFactory.createXYLineChart("", "DEPTH", "", dataset);
                    chart.getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
                    chart.getXYPlot().setBackgroundPaint(Color.WHITE);
                    chart.getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
                    chart.getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);
                    chart.getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
                    chart.getXYPlot().getDomainAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
                    chart.getXYPlot().getDomainAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
                    chart.getXYPlot().getRangeAxis().setLabelFont(MySequentialGraphVars.tahomaPlainFont11);
                    chart.getXYPlot().getRangeAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
                    chart.getXYPlot().getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
                    chart.setBackgroundPaint(Color.WHITE);

                    XYPlot plot = (XYPlot) chart.getPlot();
                    XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
                    if (graphOptionComboBox.getSelectedIndex() == 0) {
                        graphOptionComboBox.setToolTipText("SELECT AN AVERAGE VALUE CHART FOR DISTRIBUTION");
                        renderer.setSeriesPaint(0, Color.DARK_GRAY);
                        renderer.setSeriesStroke(0, new BasicStroke(1.5f));
                        renderer.setSeriesShapesVisible(0, true);
                        renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                        renderer.setSeriesFillPaint(0, Color.WHITE);

                        renderer.setSeriesPaint(1, Color.DARK_GRAY);
                        renderer.setSeriesStroke(1, new BasicStroke(1.5f));
                        renderer.setSeriesShapesVisible(1, true);
                        renderer.setSeriesShape(1, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                        renderer.setSeriesFillPaint(1, Color.WHITE);

                        renderer.setSeriesPaint(2, Color.DARK_GRAY);
                        renderer.setSeriesStroke(2, new BasicStroke(1.5f));
                        renderer.setSeriesShapesVisible(2, true);
                        renderer.setSeriesShape(2, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                        renderer.setSeriesFillPaint(2, Color.WHITE);

                        renderer.setSeriesPaint(3, Color.RED);
                        renderer.setSeriesStroke(3, new BasicStroke(1.5f));
                        renderer.setSeriesShapesVisible(3, true);
                        renderer.setSeriesShape(3, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                        renderer.setSeriesFillPaint(3, Color.WHITE);

                        renderer.setSeriesPaint(4, Color.DARK_GRAY);
                        renderer.setSeriesStroke(4, new BasicStroke(1.5f));
                        renderer.setSeriesShapesVisible(4, true);
                        renderer.setSeriesShape(4, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                        renderer.setSeriesFillPaint(4, Color.WHITE);
                        renderer.setUseFillPaint(true);

                        renderer.setSeriesPaint(5, Color.RED);
                        renderer.setSeriesStroke(5, new BasicStroke(1.5f));
                        renderer.setSeriesShapesVisible(5, true);
                        renderer.setSeriesShape(5, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                        renderer.setSeriesFillPaint(5, Color.WHITE);
                        renderer.setUseFillPaint(true);

                        renderer.setSeriesPaint(6, Color.BLUE);
                        renderer.setSeriesStroke(6, new BasicStroke(1.5f));
                        renderer.setSeriesShapesVisible(6, true);
                        renderer.setSeriesShape(6, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                        renderer.setSeriesFillPaint(6, Color.WHITE);
                        renderer.setUseFillPaint(true);

                        renderer.setSeriesPaint(7, Color.RED);
                        renderer.setSeriesStroke(7, new BasicStroke(1.5f));
                        renderer.setSeriesShapesVisible(7, true);
                        renderer.setSeriesShape(7, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                        renderer.setSeriesFillPaint(7, Color.WHITE);
                        renderer.setUseFillPaint(true);
                    } else if (graphOptionComboBox.getSelectedIndex() == 1) {
                        graphOptionComboBox.setToolTipText("AVERAGE CONTRIBUTION BY DEPTH");
                        renderer.setSeriesPaint(0, Color.DARK_GRAY);
                        renderer.setSeriesStroke(0, new BasicStroke(1.5f));
                        renderer.setSeriesShapesVisible(0, true);
                        renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                        renderer.setSeriesFillPaint(0, Color.WHITE);
                        renderer.setUseFillPaint(true);
                    } else if (graphOptionComboBox.getSelectedIndex() == 2) {
                        graphOptionComboBox.setToolTipText("AVERAGE IN-CONTRIBUTION BY DEPTH");
                        renderer.setSeriesPaint(0, Color.DARK_GRAY);
                        renderer.setSeriesStroke(0, new BasicStroke(1.5f));
                        renderer.setSeriesShapesVisible(0, true);
                        renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                        renderer.setSeriesFillPaint(0, Color.WHITE);
                        renderer.setUseFillPaint(true);
                    } else if (graphOptionComboBox.getSelectedIndex() == 3) {
                        graphOptionComboBox.setToolTipText("AVERAGE OUT-CONTRIBUTION BY DEPTH");
                        renderer.setSeriesPaint(0, Color.DARK_GRAY);
                        renderer.setSeriesStroke(0, new BasicStroke(1.5f));
                        renderer.setSeriesShapesVisible(0, true);
                        renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                        renderer.setSeriesFillPaint(0, Color.WHITE);
                        renderer.setUseFillPaint(true);
                    } else if (graphOptionComboBox.getSelectedIndex() == 4) {
                        graphOptionComboBox.setToolTipText("AVERAGE PREDECESSORS BY DEPTH");
                        renderer.setSeriesPaint(0, Color.DARK_GRAY);
                        renderer.setSeriesStroke(0, new BasicStroke(1.5f));
                        renderer.setSeriesShapesVisible(0, true);
                        renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                        renderer.setSeriesFillPaint(0, Color.WHITE);
                        renderer.setUseFillPaint(true);
                    } else if (graphOptionComboBox.getSelectedIndex() == 5) {
                        graphOptionComboBox.setToolTipText("AVERAGE SUCCESSORS BY DEPTH");
                        renderer.setSeriesPaint(0, Color.DARK_GRAY);
                        renderer.setSeriesStroke(0, new BasicStroke(1.5f));
                        renderer.setSeriesShapesVisible(0, true);
                        renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                        renderer.setSeriesFillPaint(0, Color.WHITE);
                        renderer.setUseFillPaint(true);
                    } else if (graphOptionComboBox.getSelectedIndex() == 6) {
                        graphOptionComboBox.setToolTipText("AVERAGE REACH TIME BY DEPTH");
                        renderer.setSeriesPaint(0, Color.DARK_GRAY);
                        renderer.setSeriesStroke(0, new BasicStroke(1.5f));
                        renderer.setSeriesShapesVisible(0, true);
                        renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                        renderer.setSeriesFillPaint(0, Color.WHITE);
                        renderer.setUseFillPaint(true);
                    } else if (graphOptionComboBox.getSelectedIndex() == 7) {
                        graphOptionComboBox.setToolTipText("AVERAGE DURATION BY DEPTH");
                        renderer.setSeriesPaint(0, Color.DARK_GRAY);
                        renderer.setSeriesStroke(0, new BasicStroke(1.5f));
                        renderer.setSeriesShapesVisible(0, true);
                        renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                        renderer.setSeriesFillPaint(0, Color.WHITE);
                        renderer.setUseFillPaint(true);
                    }

                    ChartPanel chartPanel = new ChartPanel(chart);
                    chartPanel.setPreferredSize(new Dimension(560, 367));

                    JLabel titleLabel = new JLabel(" AVGS.");
                    titleLabel.setToolTipText("AVERAGE VALUES BY DEPTH");
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
                                    MAXIMIZED = true;
                                    enlarge();
                                }
                            }).start();
                        }
                    });

                    JComboBox depthMenu = new JComboBox();
                    depthMenu.setFocusable(false);
                    depthMenu.setFont(MySequentialGraphVars.tahomaPlainFont11);
                    depthMenu.setBackground(Color.WHITE);
                    depthMenu.addItem("DEPTH");
                    for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {depthMenu.addItem("" + i);}

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

                    chart.removeLegend();
                    add(chartPanel, BorderLayout.CENTER);

                    revalidate();
                    repaint();
                }
            });
        } catch (Exception ex) {ex.printStackTrace();}
    }

    @Override public void actionPerformed(ActionEvent e) {
        new Thread(new Runnable() {
            @Override public void run() {
                if (e.getSource() == graphOptionComboBox) {
                    if (!MAXIMIZED) {instances = 0;}
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
        try {
            MAXIMIZED = true;
            JFrame f = new JFrame("AVERAGE VALUES BY DEPTH");
            f.setLayout(new BorderLayout());
            f.setBackground(Color.WHITE);
            f.setPreferredSize(new Dimension(450, 350));
            f.getContentPane().add(new MyGraphLevelAverageValuesByDepthLineChart(), BorderLayout.CENTER);
            f.pack();
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.addWindowListener(new WindowListener() {
                @Override public void windowOpened(WindowEvent e) {}
                @Override public void windowClosing(WindowEvent e) {
                    MAXIMIZED = false;
                }
                @Override public void windowClosed(WindowEvent e) {}
                @Override public void windowIconified(WindowEvent e) {}
                @Override public void windowDeiconified(WindowEvent e) {}
                @Override public void windowActivated(WindowEvent e) {}
                @Override public void windowDeactivated(WindowEvent e) {}
            });

            f.addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    f.setAlwaysOnTop(true);
                }

                @Override public void mouseExited(MouseEvent e) {
                    super.mouseEntered(e);
                    f.setAlwaysOnTop(false);
                }
            });

            f.setCursor(Cursor.HAND_CURSOR);
            f.setAlwaysOnTop(true);
            pb.updateValue(100, 100);
            pb.dispose();
            f.setVisible(true);
            f.setAlwaysOnTop(false);
        } catch (Exception ex) {
            MAXIMIZED = false;
            pb.updateValue(100, 100);
            pb.dispose();
        }
    }
}
