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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.util.*;

public class MyGraphLevelContributionByDepthLineChart
extends JPanel {

    public static int instances = 0;
    XYSeries inContributionValueSeries = new XYSeries("IN");
    XYSeries outContributionValueSeries = new XYSeries("OUT");
    XYSeries maxOutContributionValueSeries = new XYSeries("MAX. OUT");
    XYSeries minOutContributionValueSeries = new XYSeries("MIN. OUT");
    XYSeries maxInContributionValueSeries = new XYSeries("MAX. IN");
    XYSeries minInContributionValueSeries = new XYSeries("MIN. IN");
    XYSeries inOutContributionValueDifferenceSeries = new XYSeries("DIFF.");
    public MyGraphLevelContributionByDepthLineChart() {
        this.decorate();
    }

    public void decorate() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                try {
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
                    setLayout(new BorderLayout(5, 5));
                    setBackground(Color.WHITE);

                    inContributionValueSeries = new XYSeries("IN");
                    outContributionValueSeries = new XYSeries("OUT");
                    maxOutContributionValueSeries = new XYSeries("MAX. OUT");
                    minOutContributionValueSeries = new XYSeries("MIN. OUT");
                    maxInContributionValueSeries = new XYSeries("MAX. IN");
                    minInContributionValueSeries = new XYSeries("MIN. IN");
                   // inOutContributionValueDifferenceSeries = new XYSeries("DIFF.");

                    Map<Integer, Map<String, Integer>> nodeInContributionMap = new HashMap<>();
                    Map<Integer, Map<String, Integer>> nodeOutContributionMap = new HashMap<>();
                    Map<Integer, Integer> inContributionMap = new HashMap<>();
                    Map<Integer, Integer> outContributionMap = new HashMap<>();
                    for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                        for (int i = 0; i < MySequentialGraphVars.seqs[s].length; i++) {
                            if (i + 1 == MySequentialGraphVars.seqs[s].length) {
                                String n = MySequentialGraphVars.seqs[s][i].split(":")[0];
                                if (!filterNodes.contains(n)) {
                                    if (inContributionMap.containsKey(i + 1)) {
                                        inContributionMap.put(i + 1, inContributionMap.get(i + 1) + 1);
                                    } else {
                                        inContributionMap.put(i + 1, 1);
                                    }
                                    if (!outContributionMap.containsKey(i + 1)) {
                                        outContributionMap.put(i + 1, 0);
                                    } else {
                                        outContributionMap.put(i + 1, outContributionMap.get(i + 1));
                                    }
                                    if (nodeInContributionMap.containsKey(i + 1)) {
                                        Map<String, Integer> nodeInContributionSet = nodeInContributionMap.get(i + 1);
                                        if (nodeInContributionSet.containsKey(n)) {
                                            nodeInContributionSet.put(n, nodeInContributionSet.get(n) + 1);
                                            nodeInContributionMap.put(i + 1, nodeInContributionSet);
                                        } else {
                                            nodeInContributionSet.put(n, 1);
                                            nodeInContributionMap.put(i + 1, nodeInContributionSet);
                                        }
                                    } else {
                                        Map<String, Integer> nodeInContributionSet = new HashMap<>();
                                        nodeInContributionSet.put(n, 1);
                                        nodeInContributionMap.put(i + 1, nodeInContributionSet);
                                    }
                                    if (nodeOutContributionMap.containsKey(i + 1)) {
                                        Map<String, Integer> nodeOutContributionSet = nodeOutContributionMap.get(i + 1);
                                        if (nodeOutContributionSet.containsKey(n)) {
                                            nodeOutContributionSet.put(n, nodeOutContributionSet.get(n));
                                            nodeOutContributionMap.put(i + 1, nodeOutContributionSet);
                                        } else {
                                            nodeOutContributionSet.put(n, 0);
                                            nodeOutContributionMap.put(i + 1, nodeOutContributionSet);
                                        }
                                    } else {
                                        Map<String, Integer> nodeOutContributionSet = new HashMap<>();
                                        nodeOutContributionSet.put(n, 0);
                                        nodeOutContributionMap.put(i + 1, nodeOutContributionSet);
                                    }
                                }
                            } else if (i == 0) {
                                String n = MySequentialGraphVars.seqs[s][i].split(":")[0];
                                if (!filterNodes.contains(n)) {
                                    if (inContributionMap.containsKey(i + 1)) {
                                        inContributionMap.put(i + 1, inContributionMap.get(i + 1));
                                    } else {
                                        inContributionMap.put(i + 1, 0);
                                    }
                                    if (outContributionMap.containsKey(i + 1)) {
                                        outContributionMap.put(i + 1, outContributionMap.get(i + 1) + 1);
                                    } else {
                                        outContributionMap.put(i + 1, 1);
                                    }
                                    if (nodeInContributionMap.containsKey(i + 1)) {
                                        Map<String, Integer> nodeInContributionSet = nodeInContributionMap.get(i + 1);
                                        if (nodeInContributionSet.containsKey(n)) {
                                            nodeInContributionSet.put(n, nodeInContributionSet.get(n));
                                            nodeInContributionMap.put(i + 1, nodeInContributionSet);
                                        } else {
                                            nodeInContributionSet.put(n, 0);
                                            nodeInContributionMap.put(i + 1, nodeInContributionSet);
                                        }
                                    } else {
                                        Map<String, Integer> nodeInContributionSet = new HashMap<>();
                                        nodeInContributionSet.put(n, 0);
                                        nodeInContributionMap.put(i + 1, nodeInContributionSet);
                                    }
                                    if (nodeOutContributionMap.containsKey(i + 1)) {
                                        Map<String, Integer> nodeOutContributionSet = nodeOutContributionMap.get(i + 1);
                                        if (nodeOutContributionSet.containsKey(n)) {
                                            nodeOutContributionSet.put(n, nodeOutContributionSet.get(n) + 1);
                                            nodeOutContributionMap.put(i + 1, nodeOutContributionSet);
                                        } else {
                                            nodeOutContributionSet.put(n, 1);
                                            nodeOutContributionMap.put(i + 1, nodeOutContributionSet);
                                        }
                                    } else {
                                        Map<String, Integer> nodeOutContributionSet = new HashMap<>();
                                        nodeOutContributionSet.put(n, 1);
                                        nodeOutContributionMap.put(i + 1, nodeOutContributionSet);
                                    }
                                }
                            } else {
                                String n = MySequentialGraphVars.seqs[s][i].split(":")[0];
                                if (!filterNodes.contains(n)) {
                                    if (inContributionMap.containsKey(i + 1)) {
                                        inContributionMap.put(i + 1, inContributionMap.get(i + 1) + 1);
                                    } else {
                                        inContributionMap.put(i + 1, 1);
                                    }
                                    if (outContributionMap.containsKey(i + 1)) {
                                        outContributionMap.put(i + 1, outContributionMap.get(i + 1) + 1);
                                    } else {
                                        outContributionMap.put(i + 1, 1);
                                    }
                                    if (nodeInContributionMap.containsKey(i + 1)) {
                                        Map<String, Integer> nodeInContributionSet = nodeInContributionMap.get(i + 1);
                                        if (nodeInContributionSet.containsKey(n)) {
                                            nodeInContributionSet.put(n, nodeInContributionSet.get(n) + 1);
                                            nodeInContributionMap.put(i + 1, nodeInContributionSet);
                                        } else {
                                            nodeInContributionSet.put(n, 1);
                                            nodeInContributionMap.put(i + 1, nodeInContributionSet);
                                        }
                                    } else {
                                        Map<String, Integer> nodeInContributionSet = new HashMap<>();
                                        nodeInContributionSet.put(n, 1);
                                        nodeInContributionMap.put(i + 1, nodeInContributionSet);
                                    }
                                    if (nodeOutContributionMap.containsKey(i + 1)) {
                                        Map<String, Integer> nodeOutContributionSet = nodeOutContributionMap.get(i + 1);
                                        if (nodeOutContributionSet.containsKey(n)) {
                                            nodeOutContributionSet.put(n, nodeOutContributionSet.get(n) + 1);
                                            nodeOutContributionMap.put(i + 1, nodeOutContributionSet);
                                        } else {
                                            nodeOutContributionSet.put(n, 1);
                                            nodeOutContributionMap.put(i + 1, nodeOutContributionSet);
                                        }
                                    } else {
                                        Map<String, Integer> nodeOutContributionSet = new HashMap<>();
                                        nodeOutContributionSet.put(n, 1);
                                        nodeOutContributionMap.put(i + 1, nodeOutContributionSet);
                                    }
                                }
                            }
                        }
                    }

                    if (MySequentialGraphVars.currentGraphDepth == 0) {// In-Out contribution difference.
                        for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                            if (i == 1) {
                                inOutContributionValueDifferenceSeries.add(i, 0);
                            } else {
                                if (inContributionMap.containsKey(i) && outContributionMap.containsKey(i)) {
                                    inOutContributionValueDifferenceSeries.add(i, inContributionMap.get(i) - outContributionMap.get(i));
                                } else if (inContributionMap.containsKey(i) && !outContributionMap.containsKey(i)) {
                                    inOutContributionValueDifferenceSeries.add(i, inContributionMap.get(i));
                                } else if (!inContributionMap.containsKey(i) && outContributionMap.containsKey(i)) {
                                    inOutContributionValueDifferenceSeries.add(i, -outContributionMap.get(i));
                                } else if (!inContributionMap.containsKey(i) && !outContributionMap.containsKey(i)) {
                                    inOutContributionValueDifferenceSeries.add(i, 0);
                                }
                            }
                        }
                    } else {
                        for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                            if (MySequentialGraphVars.currentGraphDepth == 1) inOutContributionValueDifferenceSeries.add(i, 0);
                            else if (i == MySequentialGraphVars.currentGraphDepth) {
                                if (inContributionMap.containsKey(i) && outContributionMap.containsKey(i)) {
                                    inOutContributionValueDifferenceSeries.add(i, inContributionMap.get(i) - outContributionMap.get(i));
                                } else if (inContributionMap.containsKey(i) && !outContributionMap.containsKey(i)) {
                                    inOutContributionValueDifferenceSeries.add(i, inContributionMap.get(i));
                                } else if (!inContributionMap.containsKey(i) && outContributionMap.containsKey(i)) {
                                    inOutContributionValueDifferenceSeries.add(i, -outContributionMap.get(i));
                                } else if (!inContributionMap.containsKey(i) && !outContributionMap.containsKey(i)) {
                                    inOutContributionValueDifferenceSeries.add(i, 0);
                                }
                            } else {
                                inOutContributionValueDifferenceSeries.add(i, 0);
                            }
                        }
                    }

                    if (MySequentialGraphVars.currentGraphDepth == 0) { // Out-Contribution
                        for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                            if (outContributionMap.containsKey(i)) {
                                outContributionValueSeries.add(i, outContributionMap.get(i));
                            } else {
                                outContributionValueSeries.add(i, 0);
                            }
                        }
                    } else {
                        for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                            if (i == MySequentialGraphVars.currentGraphDepth) {
                                if (outContributionMap.containsKey(i)) {
                                    outContributionValueSeries.add(i, outContributionMap.get(i));
                                } else {
                                    outContributionValueSeries.add(i, 0);
                                }
                            } else {
                                outContributionValueSeries.add(i, 0);
                            }
                        }
                    }

                    if (MySequentialGraphVars.currentGraphDepth == 0) { // In-Contribution
                        for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                            if (inContributionMap.containsKey(i)) {
                                inContributionValueSeries.add(i, inContributionMap.get(i));
                            } else {
                                inContributionValueSeries.add(i, 0);
                            }
                        }
                    } else {
                        for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                            if (i == MySequentialGraphVars.currentGraphDepth) {
                                if (inContributionMap.containsKey(i)) {
                                    inContributionValueSeries.add(i, inContributionMap.get(i));
                                } else {
                                    inContributionValueSeries.add(i, 0);
                                }
                            } else {
                                inContributionValueSeries.add(i, 0);
                            }
                        }
                    }

                    if (MySequentialGraphVars.currentGraphDepth == 0) { // Node Out-Contribution Maximum.
                        for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                            if (nodeOutContributionMap.containsKey(i)) {
                                Collection<Integer> values = nodeOutContributionMap.get(i).values();
                                int max = 0;
                                for (Integer v : values) {
                                    if (v > max) {
                                        max = v;
                                    }
                                }
                                maxOutContributionValueSeries.add(i, max);
                            } else {
                                maxOutContributionValueSeries.add(i, 0);
                            }
                        }
                    } else {
                        for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                            if (i == MySequentialGraphVars.currentGraphDepth) {
                                if (nodeOutContributionMap.containsKey(i)) {
                                    Collection<Integer> values = nodeOutContributionMap.get(i).values();
                                    int max = 0;
                                    for (Integer v : values) {
                                        if (v > max) {
                                            max = v;
                                        }
                                    }
                                    maxOutContributionValueSeries.add(i, max);
                                } else {
                                    maxOutContributionValueSeries.add(i, 0);
                                }
                            } else {
                                maxOutContributionValueSeries.add(i, 0);
                            }
                        }
                    }

                    if (MySequentialGraphVars.currentGraphDepth == 0) { // Node Out-Contribution Minimum.
                        for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                            if (nodeOutContributionMap.containsKey(i)) {
                                Collection<Integer> values = nodeOutContributionMap.get(i).values();
                                int min = 1000000000;
                                for (Integer v : values) {
                                    if (v < min && v > 0) {
                                        min = v;
                                    }
                                }
                                if (min == 1000000000) {
                                    minOutContributionValueSeries.add(i, 0);
                                } else {
                                    minOutContributionValueSeries.add(i, min);
                                }
                            } else {
                                minOutContributionValueSeries.add(i, 0);
                            }
                        }
                    } else {
                        for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                            if (i == MySequentialGraphVars.currentGraphDepth) {
                                if (nodeOutContributionMap.containsKey(i)) {
                                    Collection<Integer> values = nodeOutContributionMap.get(i).values();
                                    int min = 1000000000;
                                    for (Integer v : values) {
                                        if (v < min && v > 0) {
                                            min = v;
                                        }
                                    }
                                    if (min == 1000000000) {
                                        minOutContributionValueSeries.add(i, 0);
                                    } else {
                                        minOutContributionValueSeries.add(i, min);
                                    }
                                } else {
                                    minOutContributionValueSeries.add(i, 0);
                                }
                            } else {
                                minOutContributionValueSeries.add(i, 0);
                            }
                        }
                    }

                    if (MySequentialGraphVars.currentGraphDepth == 0) { // Node In-Contribution Minimum.
                        for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                            if (nodeInContributionMap.containsKey(i)) {
                                Collection<Integer> values = nodeInContributionMap.get(i).values();
                                int min = 1000000000;
                                for (Integer v : values) {
                                    if (v < min && v > 0) {
                                        min = v;
                                    }
                                }
                                if (min == 1000000000) {
                                    minInContributionValueSeries.add(i, 0);
                                } else {
                                    minInContributionValueSeries.add(i, min);
                                }
                            } else {
                                minInContributionValueSeries.add(i, 0);
                            }
                        }
                    } else {
                        for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                            if (i == MySequentialGraphVars.currentGraphDepth) {
                                if (nodeInContributionMap.containsKey(i)) {
                                    Collection<Integer> values = nodeInContributionMap.get(i).values();
                                    int min = 1000000000;
                                    for (Integer v : values) {
                                        if (v < min && v > 0) {
                                            min = v;
                                        }
                                    }
                                    if (min == 1000000000) {
                                        minInContributionValueSeries.add(i, 0);
                                    } else {
                                        minInContributionValueSeries.add(i, min);
                                    }
                                } else {
                                    minInContributionValueSeries.add(i, 0);
                                }
                            } else {
                                minInContributionValueSeries.add(i, 0);
                            }
                        }
                    }


                    if (MySequentialGraphVars.currentGraphDepth == 0) { // Node InContribution Maximum
                        for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                            if (nodeInContributionMap.containsKey(i)) {
                                Collection<Integer> values = nodeInContributionMap.get(i).values();
                                int max = 0;
                                for (Integer v : values) {
                                    if (v > max) {
                                        max = v;
                                    }
                                }
                                maxInContributionValueSeries.add(i, max);
                            } else {
                                maxInContributionValueSeries.add(i, 0);
                            }
                        }
                    } else {
                        for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                            if (MySequentialGraphVars.currentGraphDepth == i) {
                                if (nodeInContributionMap.containsKey(i)) {
                                    Collection<Integer> values = nodeInContributionMap.get(i).values();
                                    int max = 0;
                                    for (Integer v : values) {
                                        if (v > max) {
                                            max = v;
                                        }
                                    }
                                    maxInContributionValueSeries.add(i, max);
                                } else {
                                    maxInContributionValueSeries.add(i, 0);
                                }
                            }
                        }
                    }

                    XYSeriesCollection dataset = new XYSeriesCollection();
                    dataset.addSeries(inContributionValueSeries);
                    dataset.addSeries(outContributionValueSeries);
                    dataset.addSeries(maxInContributionValueSeries);
                    dataset.addSeries(minInContributionValueSeries);
                    dataset.addSeries(maxOutContributionValueSeries);
                    dataset.addSeries(minOutContributionValueSeries);
                  //  dataset.addSeries(inOutContributionValueDifferenceSeries);

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
                    renderer.setSeriesStroke(0, new BasicStroke(1.5f));
                    renderer.setSeriesStroke(1, new BasicStroke(1.5f));
                    renderer.setSeriesStroke(2, new BasicStroke(1.5f));
                    renderer.setSeriesStroke(3, new BasicStroke(1.5f));
                    renderer.setSeriesStroke(4, new BasicStroke(1.5f));
                    renderer.setSeriesStroke(5, new BasicStroke(1.5f));
                    renderer.setSeriesStroke(6, new BasicStroke(1.5f));

                    renderer.setSeriesShapesVisible(0, true);
                    renderer.setSeriesShapesVisible(1, true);
                    renderer.setSeriesShapesVisible(2, true);
                    renderer.setSeriesShapesVisible(3, true);
                    renderer.setSeriesShapesVisible(4, true);
                    renderer.setSeriesShapesVisible(5, true);
                    renderer.setSeriesShapesVisible(6, true);

                    renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                    renderer.setSeriesShape(1, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                    renderer.setSeriesShape(2, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                    renderer.setSeriesShape(3, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                    renderer.setSeriesShape(4, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                    renderer.setSeriesShape(5, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                //    renderer.setSeriesShape(6, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));

                    renderer.setSeriesFillPaint(0, Color.WHITE);
                    renderer.setSeriesFillPaint(1, Color.WHITE);
                    renderer.setSeriesFillPaint(2, Color.WHITE);
                    renderer.setSeriesFillPaint(3, Color.WHITE);
                    renderer.setSeriesFillPaint(4, Color.WHITE);
                    renderer.setSeriesFillPaint(5, Color.WHITE);
                  //  renderer.setSeriesFillPaint(6, Color.WHITE);

                    renderer.setSeriesPaint(0, Color.RED);
                    renderer.setUseFillPaint(true);

                    renderer.setSeriesPaint(1, Color.BLUE);
                    renderer.setUseFillPaint(true);

                    renderer.setSeriesPaint(2, Color.DARK_GRAY);
                    renderer.setUseFillPaint(true);

                    renderer.setSeriesPaint(3, Color.decode("#59A869"));
                    renderer.setUseFillPaint(true);

                    renderer.setSeriesPaint(4, Color.BLACK);
                    renderer.setUseFillPaint(true);

                    renderer.setSeriesPaint(5, Color.ORANGE);
                    renderer.setUseFillPaint(true);

                //    renderer.setSeriesPaint(6, Color.MAGENTA);
                //    renderer.setUseFillPaint(true);

                    ChartPanel chartPanel = new ChartPanel(chart);

                    JLabel titleLabel = new JLabel("CONTRIBUTION");
                    titleLabel.setToolTipText("CONTRIBUTION BY DEPTH");
                    titleLabel.setFont(MySequentialGraphVars.tahomaBoldFont12);
                    titleLabel.setBackground(Color.WHITE);
                    titleLabel.setForeground(Color.DARK_GRAY);

                    JPanel titlePanel = new JPanel();
                    titlePanel.setBackground(Color.WHITE);
                    titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
                    titlePanel.add(titleLabel);

                    JComboBox lineChartSelector = new JComboBox();
                    lineChartSelector.setToolTipText("SELECT A CHART FOR A DEPTH DISTRIBUTION.");
                    String[] tooltips = {"SELECT A CHART FOR A DEPTH DISTRIBUTION.",
                            "IN-CONTRIBUTION DISTRIBUTION BY DEPTH.",
                            "OUT-CONTRIBUTION DISTRIBUTION BY DEPTH",
                            "MAX. IN-CONTRIBUTION DISTRIBUTION BY DEPTH.",
                            "MIN. IN-CONTRIBUTION DISTRIBUTION BY DEPTH",
                            "MAX. OUT-CONTRIBUTION DISTRIBUTION BY DEPTH",
                            "MIN. IN-CONTRIBUTION DISTRIBUTION BY DEPTH,"};
                            //"CONTRIBUTION DIFFERENCE DISTRIBUTION BY DEPTH."};
                    lineChartSelector.setRenderer(new MyComboBoxTooltipRenderer(tooltips));
                    lineChartSelector.setFocusable(false);
                    lineChartSelector.setFont(MySequentialGraphVars.tahomaPlainFont11);
                    lineChartSelector.setBackground(Color.WHITE);
                    lineChartSelector.addItem("SELECT");
                    lineChartSelector.addItem("IN");
                    lineChartSelector.addItem("OUT.");
                    lineChartSelector.addItem("MAX. IN");
                    lineChartSelector.addItem("MIN. IN");
                    lineChartSelector.addItem("MAX. OUT");
                    lineChartSelector.addItem("MIN. OUT");
                  //  lineChartSelector.addItem("DIFF.");
                    lineChartSelector.addActionListener(new ActionListener() {
                        @Override public void actionPerformed(ActionEvent e) {
                            setLineGraphRendering(renderer, lineChartSelector.getSelectedIndex(), dataset);
                        }
                    });

                    JComboBox depthMenu = new JComboBox();
                    depthMenu.setFocusable(false);
                    depthMenu.setFont(MySequentialGraphVars.tahomaPlainFont11);
                    depthMenu.setBackground(Color.WHITE);
                    depthMenu.addItem("DEPTH");
                    for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                        depthMenu.addItem("" + i);
                    }
                    depthMenu.addActionListener(new ActionListener() {
                        @Override public void actionPerformed(ActionEvent e) {
                            setLineGraphRendering(renderer, depthMenu.getSelectedIndex(), dataset);
                        }
                    });

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

                    JPanel btnPanel = new JPanel();
                    btnPanel.setBackground(Color.WHITE);
                    btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
                    btnPanel.add(lineChartSelector);
                    //btnPanel.add(depthMenu);
                    btnPanel.add(enlargeBtn);

                    JPanel menuPanel = new JPanel();
                    menuPanel.setLayout(new BorderLayout(0, 0));
                    menuPanel.setBackground(Color.WHITE);
                    menuPanel.add(titlePanel, BorderLayout.WEST);
                    menuPanel.add(btnPanel, BorderLayout.CENTER);

                    renderer.setBaseLegendTextFont(MySequentialGraphVars.tahomaPlainFont11);
                    add(menuPanel, BorderLayout.NORTH);
                    add(chartPanel, BorderLayout.CENTER);
                    chart.removeLegend();

                    revalidate();
                    repaint();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void setLineGraphRendering(XYLineAndShapeRenderer renderer, int selected, XYSeriesCollection dataset) {
        Color colors [] = {Color.DARK_GRAY, Color.DARK_GRAY, Color.DARK_GRAY, Color.DARK_GRAY, Color.DARK_GRAY, Color.DARK_GRAY};
        if (selected == 0) {
            dataset.removeAllSeries();
            dataset.addSeries(inContributionValueSeries);
            dataset.addSeries(outContributionValueSeries);
            dataset.addSeries(maxInContributionValueSeries);
            dataset.addSeries(minInContributionValueSeries);
            dataset.addSeries(maxOutContributionValueSeries);
            dataset.addSeries(minOutContributionValueSeries);
          //  dataset.addSeries(inOutContributionValueDifferenceSeries);

            renderer.setSeriesPaint(0, colors[0]);
            renderer.setSeriesStroke(0, new BasicStroke(1.5f));
            renderer.setSeriesLinesVisible(0, true);
            renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
            renderer.setSeriesFillPaint(0, Color.WHITE);

            renderer.setSeriesPaint(1, colors[1]);
            renderer.setSeriesStroke(1, new BasicStroke(1.5f));
            renderer.setSeriesLinesVisible(1, true);
            renderer.setSeriesShape(1, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
            renderer.setSeriesFillPaint(1, Color.WHITE);

            renderer.setSeriesPaint(2, colors[2]);
            renderer.setSeriesStroke(2, new BasicStroke(1.5f));
            renderer.setSeriesLinesVisible(2, true);
            renderer.setSeriesShape(2, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
            renderer.setSeriesFillPaint(2, Color.WHITE);

            renderer.setSeriesPaint(3, colors[3]);
            renderer.setSeriesStroke(3, new BasicStroke(1.5f));
            renderer.setSeriesLinesVisible(3, true);
            renderer.setSeriesShape(3, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
            renderer.setSeriesFillPaint(3, Color.WHITE);

            renderer.setSeriesPaint(4, colors[4]);
            renderer.setSeriesStroke(4, new BasicStroke(1.5f));
            renderer.setSeriesLinesVisible(4, true);
            renderer.setSeriesShape(4, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
            renderer.setSeriesFillPaint(4, Color.WHITE);

            renderer.setSeriesPaint(5, colors[5]);
            renderer.setSeriesStroke(5, new BasicStroke(1.5f));
            renderer.setSeriesLinesVisible(5, true);
            renderer.setSeriesShape(5, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
            renderer.setSeriesFillPaint(5, Color.WHITE);

        } else {
            dataset.removeAllSeries();
            XYSeriesCollection tmp = new XYSeriesCollection();
            tmp.addSeries(inContributionValueSeries);
            tmp.addSeries(outContributionValueSeries);
            tmp.addSeries(maxInContributionValueSeries);
            tmp.addSeries(minInContributionValueSeries);
            tmp.addSeries(maxOutContributionValueSeries);
            tmp.addSeries(minOutContributionValueSeries);
          //  tmp.addSeries(inOutContributionValueDifferenceSeries);
            dataset.addSeries(tmp.getSeries(selected-1));

            renderer.setSeriesPaint(0, colors[selected-1]);
            renderer.setSeriesStroke(0, new BasicStroke(1.5f));
            renderer.setSeriesLinesVisible(0, true);
            renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
            renderer.setSeriesFillPaint(0, Color.WHITE);
        }
    }

    public void enlarge() {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            JFrame f = new JFrame("INOUT CONTRIBUTION BY DEPTH");
            f.setLayout(new BorderLayout());
            f.setBackground(Color.WHITE);
            f.setPreferredSize(new Dimension(450, 350));
            f.getContentPane().add(new MyGraphLevelContributionByDepthLineChart(), BorderLayout.CENTER);
            f.pack();
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.setCursor(Cursor.HAND_CURSOR);
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
