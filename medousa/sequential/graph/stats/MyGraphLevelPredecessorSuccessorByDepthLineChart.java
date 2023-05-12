package medousa.sequential.graph.stats;

import medousa.sequential.graph.MyComboBoxTooltipRenderer;
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

            Map<Integer, Set<String>> uniqueNodeCountByDepthMap = new HashMap<>();
            Map<Integer, Set<String>> inUniqueNodeCountByDepthMap = new HashMap<>();
            Map<Integer, Set<String>> outUniqueNodeCountByDepthMap = new HashMap<>();
            Map<Integer, Map<String, Integer>> nodeInUniqueNodeCountByDepthMap = new HashMap<>();
            Map<Integer, Map<String, Integer>> nodeOutUniqueNodeCountByDepthMap = new HashMap<>();

            for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                for (int i = 0; i < MySequentialGraphVars.seqs[s].length; i++) {
                    String n = MySequentialGraphVars.seqs[s][i].split(":")[0];
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
                            if (i+1 < MySequentialGraphVars.seqs[s].length) {
                                String ss = MySequentialGraphVars.seqs[s][i + 1].split(":")[0];
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
                        } else if ((i+1) == MySequentialGraphVars.seqs[s].length) {
                            outUniqueNodeCountByDepthMap.put(i+1, new HashSet<>());
                            nodeOutUniqueNodeCountByDepthMap.put(i+1, new HashMap<>());

                            String p = MySequentialGraphVars.seqs[s][i-1].split(":")[0];
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

                        } else {
                            String ss = MySequentialGraphVars.seqs[s][i+1].split(":")[0];
                            String p = MySequentialGraphVars.seqs[s][i-1].split(":")[0];
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

            XYSeries uniqueNodeSeries = new XYSeries("UNIQ.");
            XYSeries inUniqueNodeSeries = new XYSeries("IN");
            XYSeries maxInUniqueNodeSeries = new XYSeries("MAX. IN");
            XYSeries outUniqueNodeSeries = new XYSeries("OUT");
            XYSeries maxOutUniqueNodeSeries = new XYSeries("MAX. OUT");

                for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
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



            XYSeriesCollection dataset = new XYSeriesCollection();
            dataset.addSeries(inUniqueNodeSeries);
            dataset.addSeries(maxInUniqueNodeSeries);
            dataset.addSeries(outUniqueNodeSeries);
            dataset.addSeries(maxOutUniqueNodeSeries);

            JFreeChart chart = ChartFactory.createXYLineChart("", "DEPTH", "", dataset);
            chart.getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
            chart.getXYPlot().setBackgroundPaint(Color.WHITE);
            chart.getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
            chart.getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);
            chart.getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
            chart.getXYPlot().getDomainAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
            chart.getXYPlot().getDomainAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
            chart.getXYPlot().getRangeAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
            chart.getXYPlot().getRangeAxis().setLabelFont(MySequentialGraphVars.tahomaPlainFont11);
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

            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(560, 367));

            JLabel titleLabel = new JLabel(" PRED. & SUCC.");
            titleLabel.setToolTipText("PRDECESSORS & SUCCESSORS BY DEPTH");
            titleLabel.setFont(MySequentialGraphVars.tahomaBoldFont12);
            titleLabel.setBackground(Color.WHITE);
            titleLabel.setForeground(Color.DARK_GRAY);

            JPanel titlePanel = new JPanel();
            titlePanel.setBackground(Color.WHITE);
            titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            titlePanel.add(titleLabel);

            graphSelection = new JComboBox();
            String[] tooltips = {"SELECT A CHART FOR A DEPTH DISTRIBUTION.",
                                 "PREDECESSOR DISTRIBUTION BY DEPTH.",
                                 "MAX. PREDECESSOR DISTRIBUTION BY DEPTH.",
                                 "SUCCESSOR DISTRIBUTION BY DEPTH.",
                                 "MAX. SUCCESSOR DISTRIBUTION BY DEPTH."};
            graphSelection.setRenderer(new MyComboBoxTooltipRenderer(tooltips));
            graphSelection.setBackground(Color.WHITE);
            graphSelection.setFocusable(false);
            graphSelection.setFont(MySequentialGraphVars.tahomaPlainFont11);
            graphSelection.addItem("SELECT");
            graphSelection.addItem("IN");
            graphSelection.addItem("MAX. IN");
            graphSelection.addItem("OUT");
            graphSelection.addItem("MAX. OUT");
            graphSelection.setSelectedIndex(selected);
            graphSelection.addActionListener(this);

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
            btnPanel.add(graphSelection);

            JPanel topPanel = new JPanel();
            topPanel.setLayout(new BorderLayout(0, 0));
            topPanel.setBackground(Color.WHITE);
            topPanel.add(btnPanel, BorderLayout.CENTER);
            btnPanel.add(enlargeBtn);
            topPanel.add(titlePanel, BorderLayout.WEST);

            add(topPanel, BorderLayout.NORTH);
            renderer.setBaseLegendTextFont(MySequentialGraphVars.tahomaPlainFont11);
            add(chartPanel, BorderLayout.CENTER);
            chart.removeLegend();

            revalidate();
            repaint();
        } catch (Exception ex) {ex.printStackTrace();}
    }

    public void setInUniqueNodeChart() {
        try {
            removeAll();
            setLayout(new BorderLayout(5, 5));
            setBackground(Color.WHITE);

            Map<Integer, Set<String>> inUniqueNodeCountByDepthMap = new HashMap<>();
            inUniqueNodeCountByDepthMap.put(1, new HashSet<>());
            for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                for (int i = 1; i < MySequentialGraphVars.seqs[s].length; i++) {
                    String p = MySequentialGraphVars.seqs[s][i-1].split(":")[0];
                    String n = MySequentialGraphVars.seqs[s][i].split(":")[0];
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

            XYSeries inUniqueNodeSeries = new XYSeries("IN");

                for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
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
            chart.getXYPlot().getDomainAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
            chart.getXYPlot().getRangeAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
            chart.getXYPlot().getRangeAxis().setLabelFont(MySequentialGraphVars.tahomaPlainFont11);
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

            JLabel titleLabel = new JLabel(" PRED. & SUCC.");
            titleLabel.setToolTipText("PRDECESSORS & SUCCESSORS BY DEPTH");
            titleLabel.setFont(MySequentialGraphVars.tahomaBoldFont12);
            titleLabel.setBackground(Color.WHITE);
            titleLabel.setForeground(Color.DARK_GRAY);

            JPanel titlePanel = new JPanel();
            titlePanel.setBackground(Color.WHITE);
            titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            titlePanel.add(titleLabel);

            graphSelection = new JComboBox();
            graphSelection.setBackground(Color.WHITE);
            graphSelection.setFocusable(false);
            graphSelection.setFont(MySequentialGraphVars.tahomaPlainFont11);
            graphSelection.addItem("SELECT");
            graphSelection.addItem("IN");
            graphSelection.addItem("MAX. IN");
            graphSelection.addItem("OUT");
            graphSelection.addItem("MAX. OUT");
            graphSelection.setSelectedIndex(selected);
            graphSelection.addActionListener(this);

            String[] tooltips = {"SELECT A CHART FOR A DEPTH DISTRIBUTION.",
                    "PREDECESSOR DISTRIBUTION BY DEPTH.",
                    "MAX. PREDECESSOR DISTRIBUTION BY DEPTH.",
                    "SUCCESSOR DISTRIBUTION BY DEPTH.",
                    "MAX. SUCCESSOR DISTRIBUTION BY DEPTH."};
            graphSelection.setRenderer(new MyComboBoxTooltipRenderer(tooltips));

            JButton enlargeBtn = new JButton("+");
            enlargeBtn.setFont(MySequentialGraphVars.tahomaPlainFont11);
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
            renderer.setBaseLegendTextFont(MySequentialGraphVars.tahomaPlainFont11);
            add(chartPanel, BorderLayout.CENTER);
            chart.removeLegend();

            revalidate();
            repaint();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setOutUniqueNodeChart() {
        try {
            removeAll();
            setLayout(new BorderLayout(5, 5));
            setBackground(Color.WHITE);

            Map<Integer, Set<String>> outUniqueNodeCountByDepthMap = new HashMap<>();
            outUniqueNodeCountByDepthMap.put(MySequentialGraphVars.mxDepth, new HashSet<>());

                for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                    for (int i = 0; i < MySequentialGraphVars.seqs[s].length-1; i++) {
                        String n = MySequentialGraphVars.seqs[s][i].split(":")[0];
                        String ss = MySequentialGraphVars.seqs[s][i+1].split(":")[0];
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


            XYSeries outUniqueNodeSeries = new XYSeries("OUT");
            if (MySequentialGraphVars.currentGraphDepth > 0) {
                for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                    if (MySequentialGraphVars.currentGraphDepth == i) {
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
                for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
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
            chart.getXYPlot().getDomainAxis().setLabelFont(MySequentialGraphVars.tahomaPlainFont11);
            chart.getXYPlot().getDomainAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
            chart.getXYPlot().getRangeAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
            chart.getXYPlot().getRangeAxis().setLabelFont(MySequentialGraphVars.tahomaPlainFont11);
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

            JLabel titleLabel = new JLabel(" PRED. & SUCC.");
            titleLabel.setToolTipText("PRDECESSORS & SUCCESSORS BY DEPTH");
            titleLabel.setFont(MySequentialGraphVars.tahomaBoldFont12);
            titleLabel.setBackground(Color.WHITE);
            titleLabel.setForeground(Color.DARK_GRAY);

            JPanel titlePanel = new JPanel();
            titlePanel.setBackground(Color.WHITE);
            titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            titlePanel.add(titleLabel);

            graphSelection = new JComboBox();
            graphSelection.setBackground(Color.WHITE);
            graphSelection.setFocusable(false);
            graphSelection.setFont(MySequentialGraphVars.tahomaPlainFont11);
            graphSelection.addItem("SELECT");
            graphSelection.addItem("IN");
            graphSelection.addItem("MAX. IN");
            graphSelection.addItem("OUT");
            graphSelection.addItem("MAX. OUT");
            graphSelection.setSelectedIndex(selected);
            graphSelection.addActionListener(this);

            String[] tooltips = {"SELECT A CHART FOR A DEPTH DISTRIBUTION.",
                    "PREDECESSOR DISTRIBUTION BY DEPTH.",
                    "MAX. PREDECESSOR DISTRIBUTION BY DEPTH.",
                    "SUCCESSOR DISTRIBUTION BY DEPTH.",
                    "MAX. SUCCESSOR DISTRIBUTION BY DEPTH."};
            graphSelection.setRenderer(new MyComboBoxTooltipRenderer(tooltips));

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
            btnPanel.add(graphSelection);

            JPanel topPanel = new JPanel();
            topPanel.setLayout(new BorderLayout(0, 0));
            topPanel.setBackground(Color.WHITE);
            topPanel.add(btnPanel, BorderLayout.CENTER);

            btnPanel.add(enlargeBtn);
            topPanel.add(titlePanel, BorderLayout.WEST);

            add(topPanel, BorderLayout.NORTH);
            renderer.setBaseLegendTextFont(MySequentialGraphVars.tahomaPlainFont11);
            add(chartPanel, BorderLayout.CENTER);

            revalidate();
            repaint();
        } catch (Exception ex) {ex.printStackTrace();}
    }

    private void setMaxOutUniqueNodeChart() {
        try {
            removeAll();
            setLayout(new BorderLayout(5, 5));
            setBackground(Color.WHITE);

            Map<Integer, Map<String, Integer>> nodeOutUniqueNodeCountByDepthMap = new HashMap<>();
            nodeOutUniqueNodeCountByDepthMap.put(MySequentialGraphVars.mxDepth, new HashMap<>());
            for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                for (int i = 0; i < MySequentialGraphVars.seqs[s].length-1; i++) {
                    String n = MySequentialGraphVars.seqs[s][i].split(":")[0];
                    String ss = MySequentialGraphVars.seqs[s][i+1].split(":")[0];
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

            XYSeries maxOutUniqueNodeSeries = new XYSeries("MAX. OUT");
            for (int i = 0; i <= MySequentialGraphVars.mxDepth; i++) {
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
            chart.getXYPlot().getDomainAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
            chart.getXYPlot().getRangeAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
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

            JLabel titleLabel = new JLabel(" PRED. & SUCC.");
            titleLabel.setToolTipText("PRDECESSORS & SUCCESSORS BY DEPTH");
            titleLabel.setFont(MySequentialGraphVars.tahomaBoldFont12);
            titleLabel.setBackground(Color.WHITE);
            titleLabel.setForeground(Color.DARK_GRAY);

            JPanel titlePanel = new JPanel();
            titlePanel.setBackground(Color.WHITE);
            titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            titlePanel.add(titleLabel);

            graphSelection = new JComboBox();
            graphSelection.setBackground(Color.WHITE);
            graphSelection.setFocusable(false);
            graphSelection.setFont(MySequentialGraphVars.tahomaPlainFont11);
            graphSelection.addItem("SELECT");
            graphSelection.addItem("IN");
            graphSelection.addItem("MAX. IN");
            graphSelection.addItem("OUT");
            graphSelection.addItem("MAX. OUT");
            graphSelection.setSelectedIndex(selected);
            graphSelection.addActionListener(this);


            String[] tooltips = {"SELECT A CHART FOR A DEPTH DISTRIBUTION.",
                    "PREDECESSOR DISTRIBUTION BY DEPTH.",
                    "MAX. PREDECESSOR DISTRIBUTION BY DEPTH.",
                    "SUCCESSOR DISTRIBUTION BY DEPTH.",
                    "MAX. SUCCESSOR DISTRIBUTION BY DEPTH."};
            graphSelection.setRenderer(new MyComboBoxTooltipRenderer(tooltips));

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
            btnPanel.add(graphSelection);

            JPanel topPanel = new JPanel();
            topPanel.setLayout(new BorderLayout(0, 0));
            topPanel.setBackground(Color.WHITE);
            topPanel.add(btnPanel, BorderLayout.CENTER);

            btnPanel.add(enlargeBtn);
            topPanel.add(titlePanel, BorderLayout.WEST);

            add(topPanel, BorderLayout.NORTH);
            renderer.setBaseLegendTextFont(MySequentialGraphVars.tahomaPlainFont11);
            add(chartPanel, BorderLayout.CENTER);
        } catch (Exception ex) {ex.printStackTrace();}
        revalidate();
        repaint();
    }

    private void setMaxInUniqueNodeChart() {
        try {
            removeAll();
            setLayout(new BorderLayout(5, 5));
            setBackground(Color.WHITE);

            Map<Integer, Map<String, Integer>> nodeInUniqueNodeCountByDepthMap = new HashMap<>();
            nodeInUniqueNodeCountByDepthMap.put(1, new HashMap<>());
            for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                for (int i = 1; i < MySequentialGraphVars.seqs[s].length; i++) {
                    String n = MySequentialGraphVars.seqs[s][i].split(":")[0];
                    String p = MySequentialGraphVars.seqs[s][i-1].split(":")[0];
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

            XYSeries maxInUniqueNodeSeries = new XYSeries("MAX. IN");
            for (int i = 0; i <= MySequentialGraphVars.mxDepth; i++) {
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
            chart.getXYPlot().getDomainAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
            chart.getXYPlot().getRangeAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
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

            JLabel titleLabel = new JLabel(" PRED. & SUCC.");
            titleLabel.setToolTipText("PRDECESSORS & SUCCESSORS BY DEPTH");
            titleLabel.setFont(MySequentialGraphVars.tahomaBoldFont12);
            titleLabel.setBackground(Color.WHITE);
            titleLabel.setForeground(Color.DARK_GRAY);

            JPanel titlePanel = new JPanel();
            titlePanel.setBackground(Color.WHITE);
            titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            titlePanel.add(titleLabel);

            graphSelection = new JComboBox();
            graphSelection.setBackground(Color.WHITE);
            graphSelection.setFocusable(false);
            graphSelection.setFont(MySequentialGraphVars.tahomaPlainFont11);
            graphSelection.addItem("SELECT");
            graphSelection.addItem("IN");
            graphSelection.addItem("MAX. IN");
            graphSelection.addItem("OUT");
            graphSelection.addItem("MAX. OUT");
            graphSelection.setSelectedIndex(selected);
            graphSelection.addActionListener(this);

            String[] tooltips = {"SELECT A CHART FOR A DEPTH DISTRIBUTION.",
                    "PREDECESSOR DISTRIBUTION BY DEPTH.",
                    "MAX. PREDECESSOR DISTRIBUTION BY DEPTH.",
                    "SUCCESSOR DISTRIBUTION BY DEPTH.",
                    "MAX. SUCCESSOR DISTRIBUTION BY DEPTH."};
            graphSelection.setRenderer(new MyComboBoxTooltipRenderer(tooltips));

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
            btnPanel.add(graphSelection);

            JPanel topPanel = new JPanel();
            topPanel.setLayout(new BorderLayout(0, 0));
            topPanel.setBackground(Color.WHITE);
            topPanel.add(btnPanel, BorderLayout.CENTER);

            btnPanel.add(enlargeBtn);
            topPanel.add(titlePanel, BorderLayout.WEST);

            add(topPanel, BorderLayout.NORTH);
            renderer.setBaseLegendTextFont(MySequentialGraphVars.tahomaPlainFont11);
            add(chartPanel, BorderLayout.CENTER);
        } catch (Exception ex) {ex.printStackTrace();}
        revalidate();
        repaint();
    }


    public void enlarge() {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            JFrame f = new JFrame("PREDECESSORS & SUCCESSORS BY DEPTH");
            f.setLayout(new BorderLayout());
            f.setBackground(Color.WHITE);
            f.setPreferredSize(new Dimension(450, 350));
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.getContentPane().add(new MyGraphLevelPredecessorSuccessorByDepthLineChart(), BorderLayout.CENTER);
            f.pack();
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
            } else if (this.graphSelection.getSelectedIndex() == 0) {
                setAllCharts();
            }
        }
    }
}
