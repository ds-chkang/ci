package datamining.graph.stats;

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
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MyGraphLevelPredecessorSuccessorByDepthLineChart
extends JPanel
implements ActionListener {

    public static int instances = 0;
    private JComboBox graphSelection = new JComboBox();
    Set<String> filteredGraphNodes;
    public MyGraphLevelPredecessorSuccessorByDepthLineChart() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                setAllCharts();
            }
        });
    }

    public void setAllCharts() {
        try {
            removeAll();
            setLayout(new BorderLayout(5, 5));
            setBackground(Color.WHITE);

            filteredGraphNodes = new HashSet<>();
            Set<MyNode> graphNodes = new HashSet<>(MyVars.g.getVertices());
            for (MyNode n : graphNodes) {
                if (n.getCurrentValue() == 0) {
                    filteredGraphNodes.add(n.getName());
                }
            }

            Map<Integer, Set<String>> uniqueNodeCountByDepthMap = new HashMap<>();
            Map<Integer, Set<String>> inUniqueNodeCountByDepthMap = new HashMap<>();
            Map<Integer, Set<String>> outUniqueNodeCountByDepthMap = new HashMap<>();
            Map<Integer, Map<String, Integer>> nodeInUniqueNodeCountByDepthMap = new HashMap<>();
            Map<Integer, Map<String, Integer>> nodeOutUniqueNodeCountByDepthMap = new HashMap<>();

            for (int s = 0; s < MyVars.seqs.length; s++) {
                for (int i = 0; i < MyVars.seqs[s].length; i++) {
                    String n = MyVars.seqs[s][i].split(":")[0];
                    if (!filteredGraphNodes.contains(n)) {
                        if (uniqueNodeCountByDepthMap.containsKey(i+1)) {
                            Set<String> uniqueNodes = uniqueNodeCountByDepthMap.get(i+1);
                            if (!uniqueNodes.contains(n)) {
                                uniqueNodes.add(n);
                                uniqueNodeCountByDepthMap.put(i+1, uniqueNodes);
                            }
                        } else {
                            Set<String> uniqueNodes = new HashSet<>();
                            uniqueNodes.add(n);
                            uniqueNodeCountByDepthMap.put(i+1, uniqueNodes);
                        }

                        if (i==0) {
                            if (i+1 < MyVars.seqs[s].length) {
                                String ss = MyVars.seqs[s][i + 1].split(":")[0];
                                if (!filteredGraphNodes.contains(ss)) {
                                    if (outUniqueNodeCountByDepthMap.containsKey(i + 1)) {
                                        Set<String> outUniqueNodeSet = outUniqueNodeCountByDepthMap.get(i + 1);
                                        outUniqueNodeSet.add(ss);
                                        outUniqueNodeCountByDepthMap.put(i + 1, outUniqueNodeSet);
                                    } else {
                                        Set<String> outUniqueNodeSet = new HashSet<>();
                                        outUniqueNodeSet.add(ss);
                                        outUniqueNodeCountByDepthMap.put(i + 1, outUniqueNodeSet);
                                    }

                                    if (nodeOutUniqueNodeCountByDepthMap.containsKey(i + 1)) {
                                        Map<String, Integer> nodeOutUniqueNodeSet = nodeOutUniqueNodeCountByDepthMap.get(i + 1);
                                        if (nodeOutUniqueNodeSet.containsKey(ss)) {
                                            nodeOutUniqueNodeSet.put(ss, nodeOutUniqueNodeSet.get(ss) + 1);
                                            nodeOutUniqueNodeCountByDepthMap.put(i + 1, nodeOutUniqueNodeSet);
                                        } else {
                                            nodeOutUniqueNodeSet.put(ss, 1);
                                            nodeOutUniqueNodeCountByDepthMap.put(i + 1, nodeOutUniqueNodeSet);
                                        }
                                    } else {
                                        Map<String, Integer> nodeOutUniqueNodeSet = new HashMap<>();
                                        nodeOutUniqueNodeSet.put(ss, 1);
                                        nodeOutUniqueNodeCountByDepthMap.put(i + 1, nodeOutUniqueNodeSet);
                                    }
                                }

                                if (inUniqueNodeCountByDepthMap.containsKey(i + 1)) {
                                    inUniqueNodeCountByDepthMap.put(i + 1, inUniqueNodeCountByDepthMap.get(i + 1));
                                } else {
                                    inUniqueNodeCountByDepthMap.put(i + 1, new HashSet<>());
                                }

                                if (nodeInUniqueNodeCountByDepthMap.containsKey(i + 1)) {
                                    nodeInUniqueNodeCountByDepthMap.put(i + 1, nodeInUniqueNodeCountByDepthMap.get(i + 1));
                                } else {
                                    nodeInUniqueNodeCountByDepthMap.put(i + 1, new HashMap<>());
                                }
                            }
                        } else if ((i+1) == MyVars.seqs[s].length) {
                            outUniqueNodeCountByDepthMap.put(i+1, new HashSet<>());
                            nodeOutUniqueNodeCountByDepthMap.put(i+1, new HashMap<>());

                            String p = MyVars.seqs[s][i-1].split(":")[0];
                            if (!filteredGraphNodes.contains(p)) {
                                if (inUniqueNodeCountByDepthMap.containsKey(i+1)) {
                                    Set<String> inUniqueNodeSet = inUniqueNodeCountByDepthMap.get(i+1);
                                    inUniqueNodeSet.add(p);
                                    inUniqueNodeCountByDepthMap.put(i + 1, inUniqueNodeSet);
                                } else {
                                    Set<String> inUniqueNodeSet = new HashSet<>();
                                    inUniqueNodeSet.add(p);
                                    inUniqueNodeCountByDepthMap.put(i + 1, inUniqueNodeSet);
                                }

                                if (nodeInUniqueNodeCountByDepthMap.containsKey(i+1)) {
                                    Map<String, Integer> nodeInUniqueNodeSet = nodeInUniqueNodeCountByDepthMap.get(i+1);
                                    if (nodeInUniqueNodeSet.containsKey(p)) {
                                        nodeInUniqueNodeSet.put(p, nodeInUniqueNodeSet.get(p)+1);
                                        nodeInUniqueNodeCountByDepthMap.put(i+1, nodeInUniqueNodeSet);
                                    } else {
                                        nodeInUniqueNodeSet.put(p, 1);
                                        nodeInUniqueNodeCountByDepthMap.put(i+1, nodeInUniqueNodeSet);
                                    }
                                } else {
                                    Map<String, Integer> nodeInUniqueNodeSet = new HashMap<>();
                                    nodeInUniqueNodeSet.put(p, 1);
                                    nodeInUniqueNodeCountByDepthMap.put(i+1, nodeInUniqueNodeSet);
                                }
                            }
                        } else {
                            String ss = MyVars.seqs[s][i+1].split(":")[0];
                            String p = MyVars.seqs[s][i-1].split(":")[0];
                            if (!filteredGraphNodes.contains(ss)) {
                                if (outUniqueNodeCountByDepthMap.containsKey(i+1)) {
                                    Set<String> outUniqueNodeSet = outUniqueNodeCountByDepthMap.get(i+1);
                                    outUniqueNodeSet.add(ss);
                                    outUniqueNodeCountByDepthMap.put(i+1, outUniqueNodeSet);
                                } else {
                                    Set<String> outUniqueNodeSet = new HashSet<>();
                                    outUniqueNodeSet.add(ss);
                                    outUniqueNodeCountByDepthMap.put(i+1, outUniqueNodeSet);
                                }

                                if (nodeOutUniqueNodeCountByDepthMap.containsKey(i+1)) {
                                    Map<String, Integer> nodeOutUniqueNodeSet = nodeOutUniqueNodeCountByDepthMap.get(i+1);
                                    if (nodeOutUniqueNodeSet.containsKey(ss)) {
                                        nodeOutUniqueNodeSet.put(ss, nodeOutUniqueNodeSet.get(ss) + 1);
                                        nodeOutUniqueNodeCountByDepthMap.put(i + 1, nodeOutUniqueNodeSet);
                                    } else {
                                        nodeOutUniqueNodeSet.put(ss, 1);
                                        nodeOutUniqueNodeCountByDepthMap.put(i+1, nodeOutUniqueNodeSet);
                                    }
                                } else {
                                    Map<String, Integer> nodeOutUniqueNodeSet = new HashMap<>();
                                    nodeOutUniqueNodeSet.put(ss, 1);
                                    nodeOutUniqueNodeCountByDepthMap.put(i+1, nodeOutUniqueNodeSet);
                                }
                            }

                            if (!filteredGraphNodes.contains(p)) {
                                if (inUniqueNodeCountByDepthMap.containsKey(i + 1)) {
                                    Set<String> inUniqueNodeSet = inUniqueNodeCountByDepthMap.get(i + 1);
                                    inUniqueNodeSet.add(p);
                                    inUniqueNodeCountByDepthMap.put(i + 1, inUniqueNodeSet);
                                } else {
                                    Set<String> inUniqueNodeSet = new HashSet<>();
                                    inUniqueNodeSet.add(p);
                                    inUniqueNodeCountByDepthMap.put(i + 1, inUniqueNodeSet);
                                }

                                if (nodeInUniqueNodeCountByDepthMap.containsKey(i+1)) {
                                    Map<String, Integer> nodeInUniqueNodeSet = nodeInUniqueNodeCountByDepthMap.get(i+1);
                                    if (nodeInUniqueNodeSet.containsKey(p)) {
                                        nodeInUniqueNodeSet.put(p, nodeInUniqueNodeSet.get(p)+1);
                                        nodeInUniqueNodeCountByDepthMap.put(i+1, nodeInUniqueNodeSet);
                                    } else {
                                        nodeInUniqueNodeSet.put(p, 1);
                                        nodeInUniqueNodeCountByDepthMap.put(i+1, nodeInUniqueNodeSet);
                                    }
                                } else {
                                    Map<String, Integer> nodeInUniqueNodeSet = new HashMap<>();
                                    nodeInUniqueNodeSet.put(p, 1);
                                    nodeInUniqueNodeCountByDepthMap.put(i+1, nodeInUniqueNodeSet);
                                }
                            }
                        }
                    }
                }
            }

            XYSeries uniqueNodeSeries = new XYSeries("UNIQ.");
            XYSeries inUniqueNodeSeries = new XYSeries("IN");
            XYSeries maxInUniqueNodeSeries = new XYSeries("MAX. IN");
            XYSeries outUniqueNodeSeries = new XYSeries("OUT");
            XYSeries maxOutUniqueNodeSeries = new XYSeries("MAX. OUT");

                for (int i = 1; i <= MyVars.mxDepth; i++) {
                    if (uniqueNodeCountByDepthMap.containsKey(i)) {
                        uniqueNodeSeries.add(i, uniqueNodeCountByDepthMap.get(i).size());
                    } else {
                        uniqueNodeSeries.add(i, 0);
                    }
                    if (inUniqueNodeCountByDepthMap.containsKey(i)) {
                        inUniqueNodeSeries.add(i, inUniqueNodeCountByDepthMap.get(i).size());
                    } else {
                        inUniqueNodeSeries.add(i, 0);
                    }
                    if (outUniqueNodeCountByDepthMap.containsKey(i)) {
                        outUniqueNodeSeries.add(i, outUniqueNodeCountByDepthMap.get(i).size());
                    } else {
                        outUniqueNodeSeries.add(i, 0);
                    }
                    if (nodeOutUniqueNodeCountByDepthMap.containsKey(i)) {
                        int max = 0;
                        Map<String, Integer> nodeOutUniqueNodeSet = nodeOutUniqueNodeCountByDepthMap.get(i);
                        for (String n : nodeOutUniqueNodeSet.keySet()) {
                            if (nodeOutUniqueNodeSet.get(n) > max) {
                                max = nodeOutUniqueNodeSet.get(n);
                            }
                        }
                        maxOutUniqueNodeSeries.add(i, max);
                    } else {
                        maxOutUniqueNodeSeries.add(i, 0);
                    }
                    if (nodeInUniqueNodeCountByDepthMap.containsKey(i)) {
                        int max = 0;
                        Map<String, Integer> nodeInUniqueNodeSet = nodeInUniqueNodeCountByDepthMap.get(i);
                        for (String n : nodeInUniqueNodeSet.keySet()) {
                            if (nodeInUniqueNodeSet.get(n) > max) {
                                max = nodeInUniqueNodeSet.get(n);
                            }
                        }
                        maxInUniqueNodeSeries.add(i, max);
                    } else {
                        maxInUniqueNodeSeries.add(i, 0);
                    }
                }


            XYSeries uniqueNodeDropOffSeries = new XYSeries("DROP-OFF");
            uniqueNodeDropOffSeries.add(1, 0);
            if (MyVars.currentGraphDepth > 0) {
                for (int i = 2; i <= MyVars.mxDepth; i++) {
                    if (MyVars.currentGraphDepth == i) {
                        if (uniqueNodeCountByDepthMap.get(i) != null && uniqueNodeCountByDepthMap.get(i-1) != null) {
                            int depthDiff = uniqueNodeCountByDepthMap.get(i).size() - uniqueNodeCountByDepthMap.get(i-1).size();
                            uniqueNodeDropOffSeries.add(i, depthDiff);
                        }
                    } else {
                        uniqueNodeDropOffSeries.add(i, 0);
                    }
                }
            } else {
                for (int i = 2; i <= MyVars.mxDepth; i++) {
                    if (uniqueNodeCountByDepthMap.containsKey(i)) {
                        if (uniqueNodeCountByDepthMap.containsKey(i-1)) {
                            int depthDiff = uniqueNodeCountByDepthMap.get(i).size() - uniqueNodeCountByDepthMap.get(i-1).size();
                            uniqueNodeDropOffSeries.add(i, depthDiff);
                        } else {
                            int depthDiff = uniqueNodeCountByDepthMap.get(i).size();
                            uniqueNodeDropOffSeries.add(i, depthDiff);
                        }
                    } else {
                        uniqueNodeDropOffSeries.add(i, 0);
                    }
                }
            }

            XYSeries inOutUniqueNodeSeries = new XYSeries("DIFF.");
            if (MyVars.currentGraphDepth > 0) {
                for (int i = 1; i <= MyVars.mxDepth; i++) {
                    if (MyVars.currentGraphDepth == i) {
                        if (inUniqueNodeCountByDepthMap.containsKey(i)) {
                            if (outUniqueNodeCountByDepthMap.containsKey(i)) {
                                inOutUniqueNodeSeries.add(i, (inUniqueNodeCountByDepthMap.get(i).size() - outUniqueNodeCountByDepthMap.get(i).size()));
                            } else {
                                inOutUniqueNodeSeries.add(i, (inUniqueNodeCountByDepthMap.get(i).size()));
                            }
                        } else {
                            inOutUniqueNodeSeries.add(i, 0);
                        }
                    } else {
                        inOutUniqueNodeSeries.add(i, 0);
                    }
                }
            } else {
                for (int i = 1; i <= MyVars.mxDepth; i++) {
                    if (inUniqueNodeCountByDepthMap.containsKey(i)) {
                        if (!outUniqueNodeCountByDepthMap.containsKey(i)) {
                            inOutUniqueNodeSeries.add(i, (inUniqueNodeCountByDepthMap.get(i).size() - 0));
                        } else {
                            inOutUniqueNodeSeries.add(i, (inUniqueNodeCountByDepthMap.get(i).size() - outUniqueNodeCountByDepthMap.get(i).size()));
                        }
                    } else {
                        inOutUniqueNodeSeries.add(i, 0);
                    }
                }
            }

            XYSeriesCollection dataset = new XYSeriesCollection();
            dataset.addSeries(inUniqueNodeSeries);
            dataset.addSeries(maxInUniqueNodeSeries);
            dataset.addSeries(outUniqueNodeSeries);
            dataset.addSeries(maxOutUniqueNodeSeries);
            dataset.addSeries(inOutUniqueNodeSeries);
            dataset.addSeries(uniqueNodeDropOffSeries);

            JFreeChart chart = ChartFactory.createXYLineChart("", "DEPTH", "", dataset);
            chart.getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
            chart.getXYPlot().setBackgroundPaint(Color.WHITE);
            chart.getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
            chart.getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);
            chart.getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
            chart.getXYPlot().getDomainAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
            chart.getXYPlot().getDomainAxis().setTickLabelFont(MyVars.tahomaPlainFont11);
            chart.getXYPlot().getRangeAxis().setTickLabelFont(MyVars.tahomaPlainFont11);
            chart.getXYPlot().getRangeAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
            chart.getXYPlot().getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
            chart.setBackgroundPaint(Color.WHITE);

            XYPlot plot = (XYPlot) chart.getPlot();
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
            renderer.setSeriesPaint(0, Color.DARK_GRAY);
            renderer.setSeriesStroke(0, new BasicStroke(1.5f));
            renderer.setSeriesShapesVisible(0, true);
            renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
            renderer.setSeriesFillPaint(0, Color.WHITE);

            renderer.setSeriesPaint(1, Color.LIGHT_GRAY);
            renderer.setSeriesStroke(1, new BasicStroke(1.5f));
            renderer.setSeriesShapesVisible(1, true);
            renderer.setSeriesShape(1, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
            renderer.setSeriesFillPaint(1, Color.WHITE);

            renderer.setSeriesPaint(2, Color.BLUE);
            renderer.setSeriesStroke(2, new BasicStroke(1.5f));
            renderer.setSeriesShapesVisible(2, true);
            renderer.setSeriesShape(2, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
            renderer.setSeriesFillPaint(2, Color.WHITE);

            renderer.setSeriesPaint(3, Color.RED);
            renderer.setSeriesStroke(3, new BasicStroke(1.5f));
            renderer.setSeriesShapesVisible(3, true);
            renderer.setSeriesShape(3, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
            renderer.setSeriesFillPaint(3, Color.WHITE);
            renderer.setUseFillPaint(true);

            renderer.setSeriesPaint(4, Color.ORANGE);
            renderer.setSeriesStroke(4, new BasicStroke(1.5f));
            renderer.setSeriesShapesVisible(4, true);
            renderer.setSeriesShape(4, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
            renderer.setSeriesFillPaint(4, Color.WHITE);
            renderer.setUseFillPaint(true);

            renderer.setSeriesPaint(5, Color.decode("#59A869"));
            renderer.setSeriesStroke(5, new BasicStroke(1.5f));
            renderer.setSeriesShapesVisible(5, true);
            renderer.setSeriesShape(5, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
            renderer.setSeriesFillPaint(5, Color.WHITE);
            renderer.setUseFillPaint(true);

            renderer.setSeriesPaint(6, Color.MAGENTA);
            renderer.setSeriesStroke(6, new BasicStroke(1.5f));
            renderer.setSeriesShapesVisible(6, true);
            renderer.setSeriesShape(6, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
            renderer.setSeriesFillPaint(6, Color.WHITE);
            renderer.setUseFillPaint(true);

            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(560, 367));

            JLabel titleLabel = new JLabel(" P. & S.");
            titleLabel.setToolTipText("PRDECESSORS & SUCCESSORS BY DEPTH");
            titleLabel.setFont(MyVars.tahomaBoldFont11);
            titleLabel.setBackground(Color.WHITE);
            titleLabel.setForeground(Color.DARK_GRAY);

            JPanel titlePanel = new JPanel();
            titlePanel.setBackground(Color.WHITE);
            titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            titlePanel.add(titleLabel);

            graphSelection = new JComboBox();
            graphSelection.setBackground(Color.WHITE);
            graphSelection.setFocusable(false);
            graphSelection.setFont(MyVars.tahomaPlainFont10);
            graphSelection.addItem("SELECT");
            graphSelection.addItem("IN");
            graphSelection.addItem("MAX. IN");
            graphSelection.addItem("OUT");
            graphSelection.addItem("MAX. OUT");
            graphSelection.addItem("DIFF.");
            graphSelection.addItem("DROP-OFF");
            graphSelection.setSelectedIndex(selected);
            graphSelection.addActionListener(this);

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
                            enlarge();
                        }
                    }).start();
                }
            });

            JPanel btnPanel = new JPanel();
            btnPanel.setBackground(Color.WHITE);
            btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
            btnPanel.add(graphSelection);

            JPanel topPanel = new JPanel();
            topPanel.setLayout(new BorderLayout(0, 0));
            topPanel.setBackground(Color.WHITE);
            topPanel.add(btnPanel, BorderLayout.CENTER);
            btnPanel.add(enlargeBtn);
            topPanel.add(titlePanel, BorderLayout.WEST);

            add(topPanel, BorderLayout.NORTH);
            renderer.setBaseLegendTextFont(MyVars.tahomaPlainFont11);
            add(chartPanel, BorderLayout.CENTER);
            instances++;
        } catch (Exception ex) {ex.printStackTrace();}
        revalidate();
        repaint();
    }

    public void setUniqueNodeChart() {
        try {
            removeAll();
            setLayout(new BorderLayout(5, 5));
            setBackground(Color.WHITE);

            filteredGraphNodes = new HashSet<>();
            Set<MyNode> graphNodes = new HashSet<>(MyVars.g.getVertices());
            for (MyNode n : graphNodes) {
                if (n.getCurrentValue() == 0) {
                    filteredGraphNodes.add(n.getName());
                }
            }

            Map<Integer, Set<String>> uniqueNodeCountByDepthMap = new HashMap<>();
            for (int s = 0; s < MyVars.seqs.length; s++) {
                for (int i = 0; i < MyVars.seqs[s].length; i++) {
                    String itemset = MyVars.seqs[s][i].split(":")[0];
                    if (!filteredGraphNodes.contains(itemset)) {
                        if (uniqueNodeCountByDepthMap.containsKey(i+1)) {
                            Set<String> uniqueNodes = uniqueNodeCountByDepthMap.get(i+1);
                            if (!uniqueNodes.contains(itemset)) {
                                uniqueNodes.add(itemset);
                                uniqueNodeCountByDepthMap.put(i+1, uniqueNodes);
                            }
                        } else {
                            Set<String> uniqueNodes = new HashSet<>();
                            uniqueNodes.add(itemset);
                            uniqueNodeCountByDepthMap.put(i+1, uniqueNodes);
                        }
                    }
                }
            }

            XYSeries uniqueNodeSeries = new XYSeries("UNIQ.");

                for (int i = 1; i <= MyVars.mxDepth; i++) {
                    if (uniqueNodeCountByDepthMap.containsKey(i)) {
                        uniqueNodeSeries.add(i, uniqueNodeCountByDepthMap.get(i).size());
                    } else {
                        uniqueNodeSeries.add(i, 0);
                    }
                }


            XYSeriesCollection dataset = new XYSeriesCollection();
            dataset.addSeries(uniqueNodeSeries);

            JFreeChart chart = ChartFactory.createXYLineChart("", "U. N.", "", dataset);
            chart.getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
            chart.getXYPlot().setBackgroundPaint(Color.WHITE);
            chart.getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
            chart.getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);
            chart.getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
            chart.getXYPlot().getDomainAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
            chart.getXYPlot().getDomainAxis().setTickLabelFont(MyVars.tahomaPlainFont11);
            chart.getXYPlot().getRangeAxis().setTickLabelFont(MyVars.tahomaPlainFont11);
            chart.getXYPlot().getRangeAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
            chart.getXYPlot().getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
            chart.setBackgroundPaint(Color.WHITE);

            XYPlot plot = (XYPlot) chart.getPlot();
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
            renderer.setSeriesPaint(0, Color.DARK_GRAY);
            renderer.setSeriesStroke(0, new BasicStroke(1.5f));
            renderer.setSeriesShapesVisible(0, true);
            renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
            renderer.setSeriesFillPaint(0, Color.WHITE);

            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(560, 367));

            JLabel titleLabel = new JLabel(" P. & S.");
            titleLabel.setToolTipText("PRDECESSORS & SUCCESSORS BY DEPTH");
            titleLabel.setFont(MyVars.tahomaBoldFont11);
            titleLabel.setBackground(Color.WHITE);
            titleLabel.setForeground(Color.DARK_GRAY);

            JPanel titlePanel = new JPanel();
            titlePanel.setBackground(Color.WHITE);
            titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            titlePanel.add(titleLabel);

            this.graphSelection = new JComboBox();
            graphSelection.setBackground(Color.WHITE);
            graphSelection.setFocusable(false);
            graphSelection.setFont(MyVars.tahomaPlainFont10);
            graphSelection.addItem("SELECT");
            graphSelection.addItem("IN");
            graphSelection.addItem("MAX. IN");
            graphSelection.addItem("OUT");
            graphSelection.addItem("MAX. OUT");
            graphSelection.addItem("DIFF.");
            graphSelection.addItem("DROP-OFF");
            graphSelection.setSelectedIndex(selected);
            graphSelection.addActionListener(this);

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
                            enlarge();
                        }
                    }).start();
                }
            });

            JPanel btnPanel = new JPanel();
            btnPanel.setBackground(Color.WHITE);
            btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
            btnPanel.add(graphSelection);

            JPanel topPanel = new JPanel();
            topPanel.setLayout(new BorderLayout(0, 0));
            topPanel.setBackground(Color.WHITE);
            topPanel.add(btnPanel, BorderLayout.CENTER);

                btnPanel.add(enlargeBtn);
                topPanel.add(titlePanel, BorderLayout.WEST);

            add(topPanel, BorderLayout.NORTH);
            renderer.setBaseLegendTextFont(MyVars.tahomaPlainFont11);
            add(chartPanel, BorderLayout.CENTER);
            instances++;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        revalidate();
        repaint();
    }


    public void setInUniqueNodeChart() {
        try {
            removeAll();
            setLayout(new BorderLayout(5, 5));
            setBackground(Color.WHITE);

            filteredGraphNodes = new HashSet<>();
            Set<MyNode> graphNodes = new HashSet<>(MyVars.g.getVertices());
            for (MyNode n : graphNodes) {
                if (n.getCurrentValue() == 0) {
                    filteredGraphNodes.add(n.getName());
                }
            }

            Map<Integer, Set<String>> inUniqueNodeCountByDepthMap = new HashMap<>();
            inUniqueNodeCountByDepthMap.put(1, new HashSet<>());
            for (int s = 0; s < MyVars.seqs.length; s++) {
                for (int i = 1; i < MyVars.seqs[s].length; i++) {
                    String p = MyVars.seqs[s][i-1].split(":")[0];
                    String n = MyVars.seqs[s][i].split(":")[0];
                    if (!filteredGraphNodes.contains(n)) {
                        if (inUniqueNodeCountByDepthMap.containsKey(i+1)) {
                            Set<String> exUniqueInNodes = inUniqueNodeCountByDepthMap.get(i+1);
                            exUniqueInNodes.add(p);
                            inUniqueNodeCountByDepthMap.put(i+1, exUniqueInNodes);
                        } else {
                            Set<String> uniqueInNodes = new HashSet<>();
                            uniqueInNodes.add(p);
                            inUniqueNodeCountByDepthMap.put(i+1, uniqueInNodes);
                        }
                    }
                }
            }

            XYSeries inUniqueNodeSeries = new XYSeries("IN");

                for (int i = 1; i <= MyVars.mxDepth; i++) {
                    if (inUniqueNodeCountByDepthMap.containsKey(i)) {
                        inUniqueNodeSeries.add(i, inUniqueNodeCountByDepthMap.get(i).size());
                    } else {
                        inUniqueNodeSeries.add(i, 0);
                    }
                }


            XYSeriesCollection dataset = new XYSeriesCollection();
            dataset.addSeries(inUniqueNodeSeries);

            JFreeChart chart = ChartFactory.createXYLineChart("", "IN-U. N.", "", dataset);
            chart.getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
            chart.getXYPlot().setBackgroundPaint(Color.WHITE);
            chart.getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
            chart.getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);
            chart.getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
            chart.getXYPlot().getDomainAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
            chart.getXYPlot().getDomainAxis().setTickLabelFont(MyVars.tahomaPlainFont11);
            chart.getXYPlot().getRangeAxis().setTickLabelFont(MyVars.tahomaPlainFont11);
            chart.getXYPlot().getRangeAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
            chart.getXYPlot().getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
            chart.setBackgroundPaint(Color.WHITE);

            XYPlot plot = (XYPlot) chart.getPlot();
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
            renderer.setSeriesPaint(0, Color.RED);
            renderer.setSeriesStroke(0, new BasicStroke(1.5f));
            renderer.setSeriesShapesVisible(0, true);
            renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
            renderer.setSeriesFillPaint(0, Color.WHITE);

            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(560, 367));

            JLabel titleLabel = new JLabel(" P. & S.");
            titleLabel.setToolTipText("PRDECESSORS & SUCCESSORS BY DEPTH");
            titleLabel.setFont(MyVars.tahomaBoldFont11);
            titleLabel.setBackground(Color.WHITE);
            titleLabel.setForeground(Color.DARK_GRAY);

            JPanel titlePanel = new JPanel();
            titlePanel.setBackground(Color.WHITE);
            titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            titlePanel.add(titleLabel);

            graphSelection = new JComboBox();
            graphSelection.setBackground(Color.WHITE);
            graphSelection.setFocusable(false);
            graphSelection.setFont(MyVars.tahomaPlainFont10);
            graphSelection.addItem("SELECT");
            graphSelection.addItem("IN");
            graphSelection.addItem("MAX. IN");
            graphSelection.addItem("OUT");
            graphSelection.addItem("MAX. OUT");
            graphSelection.addItem("DIFF.");
            graphSelection.addItem("DROP-OFF");
            graphSelection.setSelectedIndex(selected);
            graphSelection.addActionListener(this);

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
                            enlarge();
                        }
                    }).start();
                }
            });
            JPanel btnPanel = new JPanel();
            btnPanel.setBackground(Color.WHITE);
            btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
            btnPanel.add(graphSelection);

            JPanel topPanel = new JPanel();
            topPanel.setLayout(new BorderLayout(0, 0));
            topPanel.setBackground(Color.WHITE);
            topPanel.add(btnPanel, BorderLayout.CENTER);

                btnPanel.add(enlargeBtn);
                topPanel.add(titlePanel, BorderLayout.WEST);

            add(topPanel, BorderLayout.NORTH);
            renderer.setBaseLegendTextFont(MyVars.tahomaPlainFont11);
            add(chartPanel, BorderLayout.CENTER);
            instances++;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        revalidate();
        repaint();
    }

    private void setOutUniqueNodeChart() {
        try {
            removeAll();
            setLayout(new BorderLayout(5, 5));
            setBackground(Color.WHITE);

            filteredGraphNodes = new HashSet<>();
            Set<MyNode> graphNodes = new HashSet<>(MyVars.g.getVertices());
            for (MyNode n : graphNodes) {
                if (n.getCurrentValue() == 0) {
                    filteredGraphNodes.add(n.getName());
                }
            }

            Map<Integer, Set<String>> outUniqueNodeCountByDepthMap = new HashMap<>();
            outUniqueNodeCountByDepthMap.put(MyVars.mxDepth, new HashSet<>());

                for (int s = 0; s < MyVars.seqs.length; s++) {
                    for (int i = 0; i < MyVars.seqs[s].length-1; i++) {
                        String n = MyVars.seqs[s][i].split(":")[0];
                        String ss = MyVars.seqs[s][i+1].split(":")[0];
                        if (!filteredGraphNodes.contains(n)) {
                            if (outUniqueNodeCountByDepthMap.containsKey(i+1)) {
                                Set<String> exUniqueOutNodes = outUniqueNodeCountByDepthMap.get(i+1);
                                exUniqueOutNodes.add(ss);
                                outUniqueNodeCountByDepthMap.put(i+1, exUniqueOutNodes);
                            } else {
                                Set<String> uniqueOutNodes = new HashSet<>();
                                uniqueOutNodes.add(ss);
                                outUniqueNodeCountByDepthMap.put(i+1, uniqueOutNodes);
                            }
                        }
                    }
                }


            XYSeries outUniqueNodeSeries = new XYSeries("OUT");
            if (MyVars.currentGraphDepth > 0) {
                for (int i = 1; i <= MyVars.mxDepth; i++) {
                    if (MyVars.currentGraphDepth == i) {
                        if (outUniqueNodeCountByDepthMap.containsKey(i)) {
                            outUniqueNodeSeries.add(i, outUniqueNodeCountByDepthMap.get(i).size());
                        } else {
                            outUniqueNodeSeries.add(i, 0);
                        }
                    } else {
                        outUniqueNodeSeries.add(i, 0);
                    }
                }
            } else {
                for (int i = 1; i <= MyVars.mxDepth; i++) {
                    if (outUniqueNodeCountByDepthMap.containsKey(i)) {
                        outUniqueNodeSeries.add(i, outUniqueNodeCountByDepthMap.get(i).size());
                    } else {
                        outUniqueNodeSeries.add(i, 0);
                    }
                }
            }

            XYSeriesCollection dataset = new XYSeriesCollection();
            dataset.addSeries(outUniqueNodeSeries);

            JFreeChart chart = ChartFactory.createXYLineChart("", "OUT-U. N.", "", dataset);
            chart.getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
            chart.getXYPlot().setBackgroundPaint(Color.WHITE);
            chart.getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
            chart.getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);
            chart.getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
            chart.getXYPlot().getDomainAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
            chart.getXYPlot().getDomainAxis().setTickLabelFont(MyVars.tahomaPlainFont11);
            chart.getXYPlot().getRangeAxis().setTickLabelFont(MyVars.tahomaPlainFont11);
            chart.getXYPlot().getRangeAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
            chart.getXYPlot().getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
            chart.setBackgroundPaint(Color.WHITE);

            XYPlot plot = (XYPlot) chart.getPlot();
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
            renderer.setSeriesPaint(0, Color.BLUE);
            renderer.setSeriesStroke(0, new BasicStroke(1.5f));
            renderer.setSeriesShapesVisible(0, true);
            renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
            renderer.setSeriesFillPaint(0, Color.WHITE);

            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(560, 367));

            JLabel titleLabel = new JLabel(" P. & S.");
            titleLabel.setToolTipText("PRDECESSORS & SUCCESSORS BY DEPTH");
            titleLabel.setFont(MyVars.tahomaBoldFont11);
            titleLabel.setBackground(Color.WHITE);
            titleLabel.setForeground(Color.DARK_GRAY);

            JPanel titlePanel = new JPanel();
            titlePanel.setBackground(Color.WHITE);
            titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            titlePanel.add(titleLabel);

            graphSelection = new JComboBox();
            graphSelection.setBackground(Color.WHITE);
            graphSelection.setFocusable(false);
            graphSelection.setFont(MyVars.tahomaPlainFont10);
            graphSelection.addItem("SELECT");
            graphSelection.addItem("IN");
            graphSelection.addItem("MAX. IN");
            graphSelection.addItem("OUT");
            graphSelection.addItem("MAX. OUT");
            graphSelection.addItem("DIFF.");
            graphSelection.addItem("DROP-OFF");
            graphSelection.setSelectedIndex(selected);
            graphSelection.addActionListener(this);

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
                            enlarge();
                        }
                    }).start();
                }
            });

            JPanel btnPanel = new JPanel();
            btnPanel.setBackground(Color.WHITE);
            btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
            btnPanel.add(graphSelection);

            JPanel topPanel = new JPanel();
            topPanel.setLayout(new BorderLayout(0, 0));
            topPanel.setBackground(Color.WHITE);
            topPanel.add(btnPanel, BorderLayout.CENTER);

                btnPanel.add(enlargeBtn);
                topPanel.add(titlePanel, BorderLayout.WEST);

            add(topPanel, BorderLayout.NORTH);
            renderer.setBaseLegendTextFont(MyVars.tahomaPlainFont11);
            add(chartPanel, BorderLayout.CENTER);
            instances++;
        } catch (Exception ex) {ex.printStackTrace();}
        revalidate();
        repaint();
    }

    private void setMaxOutUniqueNodeChart() {
        try {
            removeAll();
            setLayout(new BorderLayout(5, 5));
            setBackground(Color.WHITE);

            filteredGraphNodes = new HashSet<>();
            Set<MyNode> graphNodes = new HashSet<>(MyVars.g.getVertices());
            for (MyNode n : graphNodes) {
                if (n.getCurrentValue() == 0) {
                    filteredGraphNodes.add(n.getName());
                }
            }

            Map<Integer, Map<String, Integer>> nodeOutUniqueNodeCountByDepthMap = new HashMap<>();
            nodeOutUniqueNodeCountByDepthMap.put(MyVars.mxDepth, new HashMap<>());
            for (int s = 0; s < MyVars.seqs.length; s++) {
                for (int i = 0; i < MyVars.seqs[s].length-1; i++) {
                    String n = MyVars.seqs[s][i].split(":")[0];
                    String ss = MyVars.seqs[s][i+1].split(":")[0];
                    if (!filteredGraphNodes.contains(ss)) {
                        if (nodeOutUniqueNodeCountByDepthMap.containsKey(i+1)) {
                            Map<String, Integer> nodeOutUniqueNodeSet = nodeOutUniqueNodeCountByDepthMap.get(i+1);
                            if (nodeOutUniqueNodeSet.containsKey(ss)) {
                                nodeOutUniqueNodeSet.put(ss, nodeOutUniqueNodeSet.get(ss)+1);
                                nodeOutUniqueNodeCountByDepthMap.put(i+1, nodeOutUniqueNodeSet);
                            } else {
                                nodeOutUniqueNodeSet.put(ss, 1);
                                nodeOutUniqueNodeCountByDepthMap.put(i+1, nodeOutUniqueNodeSet);
                            }
                        } else {
                            Map<String, Integer> nodeOutUniqueNodeSet = new HashMap<>();
                            nodeOutUniqueNodeSet.put(ss, 1);
                            nodeOutUniqueNodeCountByDepthMap.put(i+1, nodeOutUniqueNodeSet);
                        }
                    }
                }
            }

            XYSeries maxOutUniqueNodeSeries = new XYSeries("MAX. OUT");
            for (int i = 0; i <= MyVars.mxDepth; i++) {
                if (nodeOutUniqueNodeCountByDepthMap.containsKey(i)) {
                    int max = 0;
                    Map<String, Integer> nodeOutUniqueNodeSet = nodeOutUniqueNodeCountByDepthMap.get(i);
                    for (String n : nodeOutUniqueNodeSet.keySet()) {
                        if (nodeOutUniqueNodeSet.get(n) > max) {
                            max = nodeOutUniqueNodeSet.get(n);
                        }
                    }
                    maxOutUniqueNodeSeries.add(i, max);
                } else {
                    maxOutUniqueNodeSeries.add(i, 0);
                }
            }

            XYSeriesCollection dataset = new XYSeriesCollection();
            dataset.addSeries(maxOutUniqueNodeSeries);

            JFreeChart chart = ChartFactory.createXYLineChart("", "MAX. OUT-U. N.", "", dataset);
            chart.getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
            chart.getXYPlot().setBackgroundPaint(Color.WHITE);
            chart.getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
            chart.getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);
            chart.getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
            chart.getXYPlot().getDomainAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
            chart.getXYPlot().getDomainAxis().setTickLabelFont(MyVars.tahomaPlainFont11);
            chart.getXYPlot().getRangeAxis().setTickLabelFont(MyVars.tahomaPlainFont11);
            chart.getXYPlot().getRangeAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
            chart.getXYPlot().getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
            chart.setBackgroundPaint(Color.WHITE);

            XYPlot plot = (XYPlot) chart.getPlot();
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
            renderer.setSeriesPaint(0, Color.ORANGE);
            renderer.setSeriesStroke(0, new BasicStroke(1.5f));
            renderer.setSeriesShapesVisible(0, true);
            renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
            renderer.setSeriesFillPaint(0, Color.WHITE);

            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(560, 367));

            JLabel titleLabel = new JLabel(" P. & S.");
            titleLabel.setToolTipText("PRDECESSORS & SUCCESSORS BY DEPTH");
            titleLabel.setFont(MyVars.tahomaBoldFont11);
            titleLabel.setBackground(Color.WHITE);
            titleLabel.setForeground(Color.DARK_GRAY);

            JPanel titlePanel = new JPanel();
            titlePanel.setBackground(Color.WHITE);
            titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            titlePanel.add(titleLabel);

            graphSelection = new JComboBox();
            graphSelection.setBackground(Color.WHITE);
            graphSelection.setFocusable(false);
            graphSelection.setFont(MyVars.tahomaPlainFont10);
            graphSelection.addItem("SELECT");
            graphSelection.addItem("IN");
            graphSelection.addItem("MAX. IN");
            graphSelection.addItem("OUT");
            graphSelection.addItem("MAX. OUT");
            graphSelection.addItem("DIFF.");
            graphSelection.addItem("DROP-OFF");
            graphSelection.setSelectedIndex(selected);
            graphSelection.addActionListener(this);

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
                            enlarge();
                        }
                    }).start();
                }
            });

            JPanel btnPanel = new JPanel();
            btnPanel.setBackground(Color.WHITE);
            btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
            btnPanel.add(graphSelection);

            JPanel topPanel = new JPanel();
            topPanel.setLayout(new BorderLayout(0, 0));
            topPanel.setBackground(Color.WHITE);
            topPanel.add(btnPanel, BorderLayout.CENTER);

            btnPanel.add(enlargeBtn);
            topPanel.add(titlePanel, BorderLayout.WEST);

            add(topPanel, BorderLayout.NORTH);
            renderer.setBaseLegendTextFont(MyVars.tahomaPlainFont11);
            add(chartPanel, BorderLayout.CENTER);
            instances++;
        } catch (Exception ex) {ex.printStackTrace();}
        revalidate();
        repaint();
    }

    private void setMaxInUniqueNodeChart() {
        try {
            removeAll();
            setLayout(new BorderLayout(5, 5));
            setBackground(Color.WHITE);

            filteredGraphNodes = new HashSet<>();
            Set<MyNode> graphNodes = new HashSet<>(MyVars.g.getVertices());
            for (MyNode n : graphNodes) {
                if (n.getCurrentValue() == 0) {
                    filteredGraphNodes.add(n.getName());
                }
            }

            Map<Integer, Map<String, Integer>> nodeInUniqueNodeCountByDepthMap = new HashMap<>();
            nodeInUniqueNodeCountByDepthMap.put(1, new HashMap<>());
            for (int s = 0; s < MyVars.seqs.length; s++) {
                for (int i = 1; i < MyVars.seqs[s].length; i++) {
                    String n = MyVars.seqs[s][i].split(":")[0];
                    String p = MyVars.seqs[s][i-1].split(":")[0];
                    if (!filteredGraphNodes.contains(n)) {
                        if (nodeInUniqueNodeCountByDepthMap.containsKey(i + 1)) {
                            Map<String, Integer> nodeInUniqueNodeSet = nodeInUniqueNodeCountByDepthMap.get(i + 1);
                            if (nodeInUniqueNodeSet.containsKey(p)) {
                                nodeInUniqueNodeSet.put(p, nodeInUniqueNodeSet.get(p) + 1);
                                nodeInUniqueNodeCountByDepthMap.put(i + 1, nodeInUniqueNodeSet);
                            } else {
                                nodeInUniqueNodeSet.put(p, 1);
                                nodeInUniqueNodeCountByDepthMap.put(i + 1, nodeInUniqueNodeSet);
                            }
                        } else {
                            Map<String, Integer> nodeOutUniqueNodeSet = new HashMap<>();
                            nodeOutUniqueNodeSet.put(p, 1);
                            nodeInUniqueNodeCountByDepthMap.put(i + 1, nodeOutUniqueNodeSet);
                        }
                    }
                }
            }

            XYSeries maxInUniqueNodeSeries = new XYSeries("MAX. IN");
            for (int i = 0; i <= MyVars.mxDepth; i++) {
                if (nodeInUniqueNodeCountByDepthMap.containsKey(i)) {
                    int max = 0;
                    Map<String, Integer> nodeOutUniqueNodeSet = nodeInUniqueNodeCountByDepthMap.get(i);
                    for (String n : nodeOutUniqueNodeSet.keySet()) {
                        if (nodeOutUniqueNodeSet.get(n) > max) {
                            max = nodeOutUniqueNodeSet.get(n);
                        }
                    }
                    maxInUniqueNodeSeries.add(i, max);
                } else {
                    maxInUniqueNodeSeries.add(i, 0);
                }
            }

            XYSeriesCollection dataset = new XYSeriesCollection();
            dataset.addSeries(maxInUniqueNodeSeries);

            JFreeChart chart = ChartFactory.createXYLineChart("", "MAX. IN-U. N.", "", dataset);
            chart.getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
            chart.getXYPlot().setBackgroundPaint(Color.WHITE);
            chart.getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
            chart.getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);
            chart.getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
            chart.getXYPlot().getDomainAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
            chart.getXYPlot().getDomainAxis().setTickLabelFont(MyVars.tahomaPlainFont11);
            chart.getXYPlot().getRangeAxis().setTickLabelFont(MyVars.tahomaPlainFont11);
            chart.getXYPlot().getRangeAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
            chart.getXYPlot().getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
            chart.setBackgroundPaint(Color.WHITE);

            XYPlot plot = (XYPlot) chart.getPlot();
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
            renderer.setSeriesPaint(0, Color.DARK_GRAY);
            renderer.setSeriesStroke(0, new BasicStroke(1.5f));
            renderer.setSeriesShapesVisible(0, true);
            renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
            renderer.setSeriesFillPaint(0, Color.WHITE);

            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(560, 367));

            JLabel titleLabel = new JLabel(" P. & S.");
            titleLabel.setToolTipText("PRDECESSORS & SUCCESSORS BY DEPTH");
            titleLabel.setFont(MyVars.tahomaBoldFont11);
            titleLabel.setBackground(Color.WHITE);
            titleLabel.setForeground(Color.DARK_GRAY);

            JPanel titlePanel = new JPanel();
            titlePanel.setBackground(Color.WHITE);
            titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            titlePanel.add(titleLabel);

            graphSelection = new JComboBox();
            graphSelection.setBackground(Color.WHITE);
            graphSelection.setFocusable(false);
            graphSelection.setFont(MyVars.tahomaPlainFont10);
            graphSelection.addItem("SELECT");
            graphSelection.addItem("IN");
            graphSelection.addItem("MAX. IN");
            graphSelection.addItem("OUT");
            graphSelection.addItem("MAX. OUT");
            graphSelection.addItem("DIFF.");
            graphSelection.addItem("DROP-OFF");
            graphSelection.setSelectedIndex(selected);
            graphSelection.addActionListener(this);

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
                            enlarge();
                        }
                    }).start();
                }
            });

            JPanel btnPanel = new JPanel();
            btnPanel.setBackground(Color.WHITE);
            btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
            btnPanel.add(graphSelection);

            JPanel topPanel = new JPanel();
            topPanel.setLayout(new BorderLayout(0, 0));
            topPanel.setBackground(Color.WHITE);
            topPanel.add(btnPanel, BorderLayout.CENTER);

            btnPanel.add(enlargeBtn);
            topPanel.add(titlePanel, BorderLayout.WEST);

            add(topPanel, BorderLayout.NORTH);
            renderer.setBaseLegendTextFont(MyVars.tahomaPlainFont11);
            add(chartPanel, BorderLayout.CENTER);
            instances++;
        } catch (Exception ex) {ex.printStackTrace();}
        revalidate();
        repaint();
    }


    public void enlarge() {
        MyProgressBar pb = new MyProgressBar(false);
        JFrame frame = new JFrame("PREDECESSORS & SUCCESSORS BY DEPTH");
        frame.setLayout(new BorderLayout());
        frame.setBackground(Color.WHITE);
        frame.setPreferredSize(new Dimension(450, 350));
        pb.updateValue(20, 100);

        MyGraphLevelPredecessorSuccessorByDepthLineChart graphLevelPredecessorSuccessorByDepthLineChart = new MyGraphLevelPredecessorSuccessorByDepthLineChart();
        frame.getContentPane().add(graphLevelPredecessorSuccessorByDepthLineChart, BorderLayout.CENTER);
        frame.pack();
        pb.updateValue(60, 100);
        pb.updateValue(100,100);
        pb.dispose();

        frame.setCursor(Cursor.HAND_CURSOR);
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);
        frame.setAlwaysOnTop(false);
    }

    private void setDropOffUniqueNodeChart() {
        try {
            removeAll();
            setLayout(new BorderLayout(5, 5));
            setBackground(Color.WHITE);

            filteredGraphNodes = new HashSet<>();
            Set<MyNode> graphNodes = new HashSet<>(MyVars.g.getVertices());
            for (MyNode n : graphNodes) {
                if (n.getCurrentValue() == 0) {
                    filteredGraphNodes.add(n.getName());
                }
            }

            Map<Integer, Set<String>> uniqueNodeCountByDepthMap = new HashMap<>();
            for (int s = 0; s < MyVars.seqs.length; s++) {
                for (int i=0; i < MyVars.seqs[s].length; i++) {
                    String itemset = MyVars.seqs[s][i].split(":")[0];
                    if (!filteredGraphNodes.contains(itemset)) {
                        if (uniqueNodeCountByDepthMap.containsKey(i+1)) {
                            Set<String> uniqueNodes = uniqueNodeCountByDepthMap.get(i+1);
                            if (!uniqueNodes.contains(itemset)) {
                                uniqueNodes.add(itemset);
                                uniqueNodeCountByDepthMap.put(i+1, uniqueNodes);
                            }
                        } else {
                            Set<String> uniqueNodes = new HashSet<>();
                            uniqueNodes.add(itemset);
                            uniqueNodeCountByDepthMap.put(i + 1, uniqueNodes);
                        }
                    }
                }
            }

            XYSeries uniqueNodeDropOffSeries = new XYSeries("DROP-OFF");
            uniqueNodeDropOffSeries.add(1, 0);

                for (int i = 2; i <= MyVars.mxDepth; i++) {
                    if (uniqueNodeCountByDepthMap.get(i) != null && uniqueNodeCountByDepthMap.get(i-1) != null) {
                        int depthDiff = uniqueNodeCountByDepthMap.get(i).size() - uniqueNodeCountByDepthMap.get(i-1).size();
                        uniqueNodeDropOffSeries.add(i, depthDiff);
                    } else {
                        uniqueNodeDropOffSeries.add(i, 0);
                    }
                }


            XYSeriesCollection dataset = new XYSeriesCollection();
            dataset.addSeries(uniqueNodeDropOffSeries);

            JFreeChart chart = ChartFactory.createXYLineChart("", "DROP-OFF", "", dataset);
            chart.getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
            chart.getXYPlot().setBackgroundPaint(Color.WHITE);
            chart.getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
            chart.getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);
            chart.getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
            chart.getXYPlot().getDomainAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
            chart.getXYPlot().getDomainAxis().setTickLabelFont(MyVars.tahomaPlainFont11);
            chart.getXYPlot().getRangeAxis().setTickLabelFont(MyVars.tahomaPlainFont11);
            chart.getXYPlot().getRangeAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
            chart.getXYPlot().getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
            chart.setBackgroundPaint(Color.WHITE);

            XYPlot plot = (XYPlot) chart.getPlot();
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
            renderer.setSeriesPaint(0, Color.MAGENTA);
            renderer.setSeriesStroke(0, new BasicStroke(1.5f));
            renderer.setSeriesShapesVisible(0, true);
            renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
            renderer.setSeriesFillPaint(0, Color.WHITE);

            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(560, 367));

            JLabel titleLabel = new JLabel(" P. & S.");
            titleLabel.setToolTipText("PRDECESSORS & SUCCESSORS BY DEPTH");
            titleLabel.setFont(MyVars.tahomaBoldFont11);
            titleLabel.setBackground(Color.WHITE);
            titleLabel.setForeground(Color.DARK_GRAY);

            JPanel titlePanel = new JPanel();
            titlePanel.setBackground(Color.WHITE);
            titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            titlePanel.add(titleLabel);

            graphSelection = new JComboBox();
            graphSelection.setBackground(Color.WHITE);
            graphSelection.setFocusable(false);
            graphSelection.setFont(MyVars.tahomaPlainFont10);
            graphSelection.addItem("SELECT");
            graphSelection.addItem("IN");
            graphSelection.addItem("MAX. IN");
            graphSelection.addItem("OUT");
            graphSelection.addItem("MAX. OUT");
            graphSelection.addItem("DIFF.");
            graphSelection.addItem("DROP-OFF");
            graphSelection.setSelectedIndex(selected);
            graphSelection.addActionListener(this);

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
                            enlarge();
                        }
                    }).start();
                }
            });

            JPanel btnPanel = new JPanel();
            btnPanel.setBackground(Color.WHITE);
            btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
            btnPanel.add(graphSelection);

            JPanel topPanel = new JPanel();
            topPanel.setLayout(new BorderLayout(0, 0));
            topPanel.setBackground(Color.WHITE);
            topPanel.add(btnPanel, BorderLayout.CENTER);

                btnPanel.add(enlargeBtn);
                topPanel.add(titlePanel, BorderLayout.WEST);

            add(topPanel, BorderLayout.NORTH);
            renderer.setBaseLegendTextFont(MyVars.tahomaPlainFont11);
            add(chartPanel, BorderLayout.CENTER);
            instances++;
        } catch (Exception ex) {ex.printStackTrace();}
        revalidate();
        repaint();
    }

    private void setInOutUniqueNodeDifferenceChart() {
        try {
            removeAll();
            setLayout(new BorderLayout(5, 5));
            setBackground(Color.WHITE);

            filteredGraphNodes = new HashSet<>();
            Set<MyNode> graphNodes = new HashSet<>(MyVars.g.getVertices());
            for (MyNode n : graphNodes) {
                if (n.getCurrentValue() == 0) {
                    filteredGraphNodes.add(n.getName());
                }
            }

            Map<Integer, Set<String>> inUniqueNodeCountByDepthMap = new HashMap<>();
            Map<Integer, Set<String>> outUniqueNodeCountByDepthMap = new HashMap<>();
            for (int s = 0; s < MyVars.seqs.length; s++) {
                for (int i = 0; i < MyVars.seqs[s].length; i++) {
                    if ((i + 1) == MyVars.seqs[s].length) {
                        String p = MyVars.seqs[s][i - 1].split(":")[0];
                        String n = MyVars.seqs[s][i].split(":")[0];
                        if (!filteredGraphNodes.contains(p) && !filteredGraphNodes.contains(n)) {
                            outUniqueNodeCountByDepthMap.put(i+1, new HashSet<>());
                            if (inUniqueNodeCountByDepthMap.containsKey(i+1)) {
                                Set<String> exUniqueInNodes = inUniqueNodeCountByDepthMap.get(i+1);
                                exUniqueInNodes.add(p);
                                inUniqueNodeCountByDepthMap.put(i+1, exUniqueInNodes);
                            } else {
                                Set<String> uniqueInNodes = new HashSet<>();
                                uniqueInNodes.add(p);
                                inUniqueNodeCountByDepthMap.put(i+1, uniqueInNodes);
                            }
                        }
                    } else if (i == 0) {
                        inUniqueNodeCountByDepthMap.put(1, new HashSet<>());
                        String n = MyVars.seqs[s][i].split(":")[0];
                        String ss = MyVars.seqs[s][i+1].split(":")[0];
                        if (!filteredGraphNodes.contains(n)) {
                            if (outUniqueNodeCountByDepthMap.containsKey(i+1)) {
                                Set<String> exUniqueOutNodes = outUniqueNodeCountByDepthMap.get(i+1);
                                exUniqueOutNodes.add(MyVars.seqs[s][i+1].split(":")[0]);
                                outUniqueNodeCountByDepthMap.put(i+1, exUniqueOutNodes);
                            } else {
                                Set<String> uniqueOutNodes = new HashSet<>();
                                uniqueOutNodes.add(MyVars.seqs[s][i+1].split(":")[0]);
                                outUniqueNodeCountByDepthMap.put(i+1, uniqueOutNodes);
                            }
                        }
                    } else {
                        String n = MyVars.seqs[s][i].split(":")[0];
                        String p = MyVars.seqs[s][i-1].split(":")[0];
                        if (!filteredGraphNodes.contains(n)) {
                            if (inUniqueNodeCountByDepthMap.containsKey(i+1)) {
                                Set<String> exUniqueInNodes = inUniqueNodeCountByDepthMap.get(i+1);
                                exUniqueInNodes.add(p);
                                inUniqueNodeCountByDepthMap.put(i+1, exUniqueInNodes);
                            } else {
                                Set<String> uniqueInNodes = new HashSet<>();
                                uniqueInNodes.add(p);
                                inUniqueNodeCountByDepthMap.put(i+1, uniqueInNodes);
                            }
                        }
                        String ss = MyVars.seqs[s][i + 1].split(":")[0];
                        if (!filteredGraphNodes.contains(n) && !filteredGraphNodes.contains(ss)) {
                            if (outUniqueNodeCountByDepthMap.containsKey(i+1)) {
                                Set<String> exUniqueOutNodes = outUniqueNodeCountByDepthMap.get(i+1);
                                exUniqueOutNodes.add(MyVars.seqs[s][i+1].split(":")[0]);
                                outUniqueNodeCountByDepthMap.put(i+1, exUniqueOutNodes);
                            } else {
                                Set<String> uniqueOutNodes = new HashSet<>();
                                uniqueOutNodes.add(MyVars.seqs[s][i+1].split(":")[0]);
                                outUniqueNodeCountByDepthMap.put(i+1, uniqueOutNodes);
                            }
                        }
                    }
                }
            }

            XYSeries inOutUniqueNodeSeries = new XYSeries("DIFF.");

                for (int i = 1; i <= MyVars.mxDepth; i++) {
                    if (inUniqueNodeCountByDepthMap.containsKey(i)) {
                        if (inUniqueNodeCountByDepthMap.get(i) == null) {
                            inOutUniqueNodeSeries.add(i, -outUniqueNodeCountByDepthMap.get(i).size());
                        } else {
                            inOutUniqueNodeSeries.add(i, (inUniqueNodeCountByDepthMap.get(i).size() - outUniqueNodeCountByDepthMap.get(i).size()));
                        }
                    } else {
                        inOutUniqueNodeSeries.add(i, 0);
                    }
                }


            XYSeriesCollection dataset = new XYSeriesCollection();
            dataset.addSeries(inOutUniqueNodeSeries);

            JFreeChart chart = ChartFactory.createXYLineChart("", "DIFF.", "", dataset);
            chart.getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
            chart.getXYPlot().setBackgroundPaint(Color.WHITE);
            chart.getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
            chart.getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);
            chart.getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
            chart.getXYPlot().getDomainAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
            chart.getXYPlot().getDomainAxis().setTickLabelFont(MyVars.tahomaPlainFont11);
            chart.getXYPlot().getRangeAxis().setTickLabelFont(MyVars.tahomaPlainFont11);
            chart.getXYPlot().getRangeAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
            chart.getXYPlot().getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
            chart.setBackgroundPaint(Color.WHITE);

            XYPlot plot = (XYPlot) chart.getPlot();
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
            renderer.setSeriesPaint(0, Color.decode("#59A869"));
            renderer.setSeriesStroke(0, new BasicStroke(1.5f));
            renderer.setSeriesShapesVisible(0, true);
            renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
            renderer.setSeriesFillPaint(0, Color.WHITE);

            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(560, 367));

            JLabel titleLabel = new JLabel(" P. & S.");
            titleLabel.setToolTipText("PRDECESSORS & SUCCESSORS BY DEPTH");
            titleLabel.setFont(MyVars.tahomaBoldFont11);
            titleLabel.setBackground(Color.WHITE);
            titleLabel.setForeground(Color.DARK_GRAY);

            JPanel titlePanel = new JPanel();
            titlePanel.setBackground(Color.WHITE);
            titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            titlePanel.add(titleLabel);

            graphSelection = new JComboBox();
            graphSelection.setBackground(Color.WHITE);
            graphSelection.setFocusable(false);
            graphSelection.setFont(MyVars.tahomaPlainFont10);
            graphSelection.addItem("SELECT");
            graphSelection.addItem("IN");
            graphSelection.addItem("MAX. IN");
            graphSelection.addItem("OUT");
            graphSelection.addItem("MAX. OUT");
            graphSelection.addItem("DIFF.");
            graphSelection.addItem("DROP-OFF");
            graphSelection.setSelectedIndex(selected);
            graphSelection.addActionListener(this);

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
                            enlarge();
                        }
                    }).start();
                }
            });

            JPanel btnPanel = new JPanel();
            btnPanel.setBackground(Color.WHITE);
            btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
            btnPanel.add(graphSelection);

            JPanel topPanel = new JPanel();
            topPanel.setLayout(new BorderLayout(0, 0));
            topPanel.setBackground(Color.WHITE);
            topPanel.add(btnPanel, BorderLayout.CENTER);

                btnPanel.add(enlargeBtn);
                topPanel.add(titlePanel, BorderLayout.WEST);

            add(topPanel, BorderLayout.NORTH);
            renderer.setBaseLegendTextFont(MyVars.tahomaPlainFont11);
            add(chartPanel, BorderLayout.CENTER);
            instances++;
        } catch (Exception ex) {ex.printStackTrace();}
        revalidate();
        repaint();
    }

    int selected = 0;

    @Override
    public void actionPerformed(ActionEvent e) {
        selected = this.graphSelection.getSelectedIndex();
        if (e.getSource() == this.graphSelection) {
            if (this.graphSelection.getSelectedIndex() == 1) {
                setInUniqueNodeChart();
            } else if (this.graphSelection.getSelectedIndex() == 2) {
                setMaxInUniqueNodeChart();
            } else if (this.graphSelection.getSelectedIndex() == 3) {
                setOutUniqueNodeChart();
            } else if (this.graphSelection.getSelectedIndex() == 4) {
                setMaxOutUniqueNodeChart();;
            } else if (this.graphSelection.getSelectedIndex() == 5) {
                setInOutUniqueNodeDifferenceChart();
            } else if (this.graphSelection.getSelectedIndex() == 6) {
                setDropOffUniqueNodeChart();
            } else if (this.graphSelection.getSelectedIndex() == 0) {
                setAllCharts();
            }
        }
    }
}
