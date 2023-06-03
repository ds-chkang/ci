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
    private int selectedGraph;
    private XYSeries inContributionValueSeries;
    private XYSeries outContributionValueSeries;
    private XYSeries maxOutContributionValueSeries;
    private XYSeries minOutContributionValueSeries;
    private XYSeries maxInContributionValueSeries;
    private XYSeries minInContributionValueSeries;
    private XYSeries inOutContributionValueDifferenceSeries;
    public MyGraphLevelContributionByDepthLineChart() {
        this.decorate();
    }

    private void setData() {
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

        inContributionValueSeries = new XYSeries("IN");
        outContributionValueSeries = new XYSeries("OUT");
        maxOutContributionValueSeries = new XYSeries("MAX. OUT");
        minOutContributionValueSeries = new XYSeries("MIN. OUT");
        maxInContributionValueSeries = new XYSeries("MAX. IN");
        minInContributionValueSeries = new XYSeries("MIN. IN");
        inOutContributionValueDifferenceSeries = new XYSeries("DIFF.");

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
    }

    public void decorate() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                try {
                    removeAll();
                    setLayout(new BorderLayout(5, 5));
                    setBackground(Color.WHITE);
                    setData();

                    XYSeriesCollection dataset = new XYSeriesCollection();
                    if (selectedGraph == 0) {
                        dataset.addSeries(inContributionValueSeries);
                        dataset.addSeries(outContributionValueSeries);
                        dataset.addSeries(maxInContributionValueSeries);
                        dataset.addSeries(minInContributionValueSeries);
                        dataset.addSeries(maxOutContributionValueSeries);
                        dataset.addSeries(minOutContributionValueSeries);
                        dataset.addSeries(inOutContributionValueDifferenceSeries);
                    } else if (selectedGraph == 1) {
                        dataset.addSeries(inContributionValueSeries);
                    } else if (selectedGraph == 2) {
                        dataset.addSeries(outContributionValueSeries);
                    } else if (selectedGraph == 3) {
                        dataset.addSeries(maxInContributionValueSeries);
                    } else if (selectedGraph == 4) {
                        dataset.addSeries(minInContributionValueSeries);
                    } else if (selectedGraph == 5) {
                        dataset.addSeries(maxOutContributionValueSeries);
                    } else if (selectedGraph == 6) {
                        dataset.addSeries(minOutContributionValueSeries);
                    } else if (selectedGraph == 7) {
                        dataset.addSeries(inOutContributionValueDifferenceSeries);
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
                    renderer.setSeriesShape(6, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));

                    renderer.setSeriesFillPaint(0, Color.WHITE);
                    renderer.setSeriesFillPaint(1, Color.WHITE);
                    renderer.setSeriesFillPaint(2, Color.WHITE);
                    renderer.setSeriesFillPaint(3, Color.WHITE);
                    renderer.setSeriesFillPaint(4, Color.WHITE);
                    renderer.setSeriesFillPaint(5, Color.WHITE);
                    renderer.setSeriesFillPaint(6, Color.WHITE);

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

                    renderer.setSeriesPaint(6, Color.MAGENTA);
                    renderer.setUseFillPaint(true);

                    ChartPanel chartPanel = new ChartPanel(chart);

                    JLabel titleLabel = new JLabel("CONT.");
                    titleLabel.setToolTipText("CONTRIBUTION BY DEPTH");
                    titleLabel.setFont(MySequentialGraphVars.tahomaBoldFont12);
                    titleLabel.setBackground(Color.WHITE);
                    titleLabel.setForeground(Color.DARK_GRAY);

                    JPanel titlePanel = new JPanel();
                    titlePanel.setBackground(Color.WHITE);
                    titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
                    titlePanel.add(titleLabel);

                    JComboBox graphMenu = new JComboBox();
                    graphMenu.setToolTipText("SELECT A CHART FOR A DEPTH DISTRIBUTION.");
                    String[] tooltips = {"SELECT A CHART FOR A DEPTH DISTRIBUTION.",
                            "IN-CONTRIBUTION DISTRIBUTION BY DEPTH.",
                            "OUT-CONTRIBUTION DISTRIBUTION BY DEPTH",
                            "MAX. IN-CONTRIBUTION DISTRIBUTION BY DEPTH.",
                            "MIN. IN-CONTRIBUTION DISTRIBUTION BY DEPTH",
                            "MAX. OUT-CONTRIBUTION DISTRIBUTION BY DEPTH",
                            "MIN. IN-CONTRIBUTION DISTRIBUTION BY DEPTH,",
                            "CONTRIBUTION DIFFERENCE DISTRIBUTION BY DEPTH."};
                    graphMenu.setRenderer(new MyComboBoxTooltipRenderer(tooltips));
                    graphMenu.addItem("SELECT");
                    graphMenu.addItem("IN");
                    graphMenu.addItem("OUT.");
                    graphMenu.addItem("MAX. IN");
                    graphMenu.addItem("MIN. IN");
                    graphMenu.addItem("MAX. OUT");
                    graphMenu.addItem("MIN. OUT");
                    graphMenu.addItem("DIFF.");
                    graphMenu.setFocusable(false);
                    graphMenu.setFont(MySequentialGraphVars.tahomaPlainFont11);
                    graphMenu.setBackground(Color.WHITE);
                    graphMenu.setSelectedIndex(selectedGraph);
                    graphMenu.addActionListener(new ActionListener() {
                        @Override public void actionPerformed(ActionEvent e) {
                            selectedGraph = graphMenu.getSelectedIndex();
                            decorate();
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
                    btnPanel.add(graphMenu);
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

    public void enlarge() {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            JFrame f = new JFrame("CONTRIBUTION BY DEPTH");
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
