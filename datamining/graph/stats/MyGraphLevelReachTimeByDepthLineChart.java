package datamining.graph.stats;

import datamining.graph.MyComboBoxTooltipRenderer;
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
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.util.*;

public class MyGraphLevelReachTimeByDepthLineChart
extends JPanel
implements ActionListener {

    public static int instances = 0;
    private JComboBox lineChartSelector;
    private int selectedGraphItem = 0;

    public MyGraphLevelReachTimeByDepthLineChart() {
        this.decorate();
    }

    public void decorate() {
        try {
            final MyGraphLevelReachTimeByDepthLineChart graphLevelReachTimeByDepthLineChart = this;
            SwingUtilities.invokeLater(new Runnable() {
                @Override public void run() {
                    final Set<String> filterNodes = new HashSet<>();
                    Collection<MyNode> graphNodes = MyVars.g.getVertices();
                    for (MyNode n : graphNodes) {
                        if (n.getCurrentValue() == 0) {
                            filterNodes.add(n.getName());
                        }
                    }

                    removeAll();
                    setLayout(new BorderLayout(5, 5));
                    setBackground(Color.WHITE);
                    if (selectedGraphItem == 0) {
                        XYSeries reachTimeByDepthSeries = new XYSeries("TOTAL");
                        XYSeries maxReachTimeByDepthSeries = new XYSeries("MAX.");
                        XYSeries minReachTimeByDepthSeries = new XYSeries("MIN.");

                        Map<Integer, Long> reachTimeByDepthMap = new HashMap<>();
                        Map<Integer, Long> maxReachTimeByDepthMap = new HashMap<>();
                        Map<Integer, Long> minReachTimeByDepthMap = new HashMap<>();
                        for (int i = 1; i <= MyVars.mxDepth; i++) {
                            minReachTimeByDepthMap.put(i, 10000000000000000L);
                        }

                        for (int s=0; s < MyVars.seqs.length; s++) {
                            for (int i=0; i < MyVars.seqs[s].length; i++) {
                                String [] itemsetAndTime = MyVars.seqs[s][i].split(":");
                                if (filterNodes.contains(itemsetAndTime[0])) continue;
                                long time = Long.valueOf(itemsetAndTime[1]);

                                if (reachTimeByDepthMap.containsKey(i + 1)) {
                                    reachTimeByDepthMap.put(i+1, reachTimeByDepthMap.get(i+1)+time);
                                } else {
                                    reachTimeByDepthMap.put(i+1, time);
                                }

                                if (maxReachTimeByDepthMap.containsKey(i+1)) {
                                    if (time > maxReachTimeByDepthMap.get(i+1)) {
                                        maxReachTimeByDepthMap.put(i+1, time);
                                    }
                                } else {
                                    maxReachTimeByDepthMap.put(i+1, time);
                                }

                                if (time > 0 && time < minReachTimeByDepthMap.get(i+1)) {
                                    minReachTimeByDepthMap.put(i+1, time);
                                }
                            }
                        }

                        if (MyVars.currentGraphDepth == 0) {
                            for (int i = 1; i <= MyVars.mxDepth; i++) {
                                if (reachTimeByDepthMap.containsKey(i)) {
                                    reachTimeByDepthSeries.add(i, reachTimeByDepthMap.get(i));
                                } else {
                                    reachTimeByDepthSeries.add(i, 0);
                                }
                            }
                        } else {
                            for (int i = 1; i <= MyVars.mxDepth; i++) {
                                if (MyVars.currentGraphDepth == i) {
                                    if (reachTimeByDepthMap.containsKey(i)) {
                                        reachTimeByDepthSeries.add(i, reachTimeByDepthMap.get(i));
                                    } else {
                                        reachTimeByDepthSeries.add(i,0);
                                    }
                                } else {
                                    reachTimeByDepthSeries.add(i,0);
                                }
                            }
                        }

                        if (MyVars.currentGraphDepth == 0) {
                            for (int i = 1; i <= MyVars.mxDepth; i++) {
                                if (maxReachTimeByDepthMap.containsKey(i)) {
                                    maxReachTimeByDepthSeries.add(i, maxReachTimeByDepthMap.get(i));
                                } else {
                                    maxReachTimeByDepthSeries.add(i, 0);
                                }
                            }
                        } else {
                            for (int i = 1; i <= MyVars.mxDepth; i++) {
                                if (MyVars.currentGraphDepth == i) {
                                    if (maxReachTimeByDepthMap.containsKey(i)) {
                                        maxReachTimeByDepthSeries.add(i, maxReachTimeByDepthMap.get(i));
                                    } else {
                                        maxReachTimeByDepthSeries.add(i, 0);
                                    }
                                } else {
                                    maxReachTimeByDepthSeries.add(i, 0);
                                }
                            }
                        }

                        if (MyVars.currentGraphDepth == 0) {
                            for (int i = 1; i <= MyVars.mxDepth; i++) {
                                if (minReachTimeByDepthMap.get(i) == 10000000000000000L) {
                                    minReachTimeByDepthSeries.add(i, 0L);
                                } else {
                                    minReachTimeByDepthSeries.add(i, minReachTimeByDepthMap.get(i));
                                }
                            }
                        } else {
                            for (int i = 1; i <= MyVars.mxDepth; i++) {
                                if (MyVars.currentGraphDepth == i) {
                                    if (minReachTimeByDepthMap.containsKey(i)) {
                                        if (minReachTimeByDepthMap.get(i) == 10000000000000000L) {
                                            minReachTimeByDepthSeries.add(i, 0L);
                                        } else {
                                            minReachTimeByDepthSeries.add(i, minReachTimeByDepthMap.get(i));
                                        }
                                    } else {
                                        minReachTimeByDepthSeries.add(i, 0);
                                    }
                                } else {
                                    minReachTimeByDepthSeries.add(i, 0);
                                }
                            }
                        }


                        XYSeriesCollection dataset = new XYSeriesCollection();
                        dataset.addSeries(reachTimeByDepthSeries);
                        dataset.addSeries(maxReachTimeByDepthSeries);
                        dataset.addSeries(minReachTimeByDepthSeries);

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
                        //chart.getXYPlot().getDomainAxis().setVerticalTickLabels(true);
                        chart.setBackgroundPaint(Color.WHITE);

                        XYPlot plot = (XYPlot) chart.getPlot();
                        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
                        renderer.setSeriesPaint(0, Color.RED);
                        renderer.setSeriesStroke(0, new BasicStroke(1.5f));
                        renderer.setSeriesShapesVisible(0, true);
                        renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                        renderer.setSeriesFillPaint(0, Color.WHITE);

                        renderer.setSeriesPaint(1, Color.decode("#59A869"));
                        renderer.setSeriesStroke(1, new BasicStroke(1.5f));
                        renderer.setSeriesShapesVisible(1, true);
                        renderer.setSeriesShape(1, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                        renderer.setSeriesFillPaint(1, Color.WHITE);

                        renderer.setSeriesPaint(2, Color.BLUE);//Color.decode("#59A869"));
                        renderer.setSeriesStroke(2, new BasicStroke(1.5f));
                        renderer.setSeriesShapesVisible(2, true);
                        renderer.setSeriesShape(2, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                        renderer.setSeriesFillPaint(2, Color.WHITE);
                        renderer.setUseFillPaint(true);

                        ChartPanel chartPanel = new ChartPanel(chart);
                        //chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );

                        JLabel titleLabel = new JLabel(" REACH T.");
                        titleLabel.setToolTipText("REACH TIME BY DEPTH");
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
                                        enlarge();
                                    }
                                }).start();
                            }
                        });

                        lineChartSelector = new JComboBox();
                        lineChartSelector.setBackground(Color.WHITE);
                        lineChartSelector.setFont(MyVars.tahomaPlainFont10);
                        lineChartSelector.setFocusable(false);
                        lineChartSelector.addItem("SELECT");
                        lineChartSelector.addItem("TOTAL");
                        lineChartSelector.addItem("MAX.");
                        lineChartSelector.addItem("MIN.");
                        lineChartSelector.addActionListener(graphLevelReachTimeByDepthLineChart);

                        JPanel btnPanel = new JPanel();
                        btnPanel.setBackground(Color.WHITE);
                        btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
                        btnPanel.add(lineChartSelector);
                        btnPanel.add(enlargeBtn);

                        JPanel menuPanel = new JPanel();
                        menuPanel.setLayout(new BorderLayout(0, 0));
                        menuPanel.setBackground(Color.WHITE);
                        menuPanel.add(titlePanel, BorderLayout.WEST);
                        menuPanel.add(btnPanel, BorderLayout.CENTER);
                        add(menuPanel, BorderLayout.NORTH);

                        add(chartPanel, BorderLayout.CENTER);
                        renderer.setBaseLegendTextFont(MyVars.tahomaPlainFont11);
                    } else if (selectedGraphItem == 1) {
                        XYSeries reachTimeByDepthSeries = new XYSeries("TOTAL");
                        Map<Integer, Long> reachTimeByDepthMap = new HashMap<>();
                        reachTimeByDepthMap.put(1, 0L);
                        for (int s = 0; s < MyVars.seqs.length; s++) {
                            for (int i = 0; i < MyVars.seqs[s].length; i++) {
                                String[] itemsetAndTime = MyVars.seqs[s][i].split(":");
                                if (filterNodes.contains(itemsetAndTime[0])) continue;
                                long time = Long.valueOf(itemsetAndTime[1]);
                                if (reachTimeByDepthMap.containsKey(i + 1)) {
                                    reachTimeByDepthMap.put(i + 1, reachTimeByDepthMap.get(i + 1) + time);
                                } else {
                                    reachTimeByDepthMap.put(i + 1, time);
                                }
                            }
                        }

                        reachTimeByDepthSeries.add(1, 0);
                        if (MyVars.currentGraphDepth == 0) {
                            for (int i = 2; i <= MyVars.mxDepth; i++) {
                                if (reachTimeByDepthMap.containsKey(i)) {
                                    reachTimeByDepthSeries.add(i, reachTimeByDepthMap.get(i));
                                } else {
                                    reachTimeByDepthSeries.add(i, 0);
                                }
                            }
                        } else {
                            for (int i = 2; i <= MyVars.mxDepth; i++) {
                                if (MyVars.currentGraphDepth == i) {
                                    if (reachTimeByDepthMap.containsKey(i)) {
                                        reachTimeByDepthSeries.add(i, reachTimeByDepthMap.get(i));
                                    } else {
                                        reachTimeByDepthSeries.add(i, 0);
                                    }
                                } else {
                                    reachTimeByDepthSeries.add(i, 0);
                                }
                            }
                        }

                        XYSeriesCollection dataset = new XYSeriesCollection();
                        dataset.addSeries(reachTimeByDepthSeries);

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
                        //chart.getXYPlot().getDomainAxis().setVerticalTickLabels(true);
                        chart.setBackgroundPaint(Color.WHITE);

                        XYPlot plot = (XYPlot) chart.getPlot();
                        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
                        renderer.setSeriesPaint(0, Color.RED);
                        renderer.setSeriesStroke(0, new BasicStroke(1.5f));
                        renderer.setSeriesShapesVisible(0, true);
                        renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                        renderer.setSeriesFillPaint(0, Color.WHITE);

                        ChartPanel chartPanel = new ChartPanel(chart);

                        JLabel titleLabel = new JLabel(" REACH T.");
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
                                        enlarge();
                                    }
                                }).start();
                            }
                        });

                        lineChartSelector = new JComboBox();
                        lineChartSelector.setBackground(Color.WHITE);
                        lineChartSelector.setFont(MyVars.tahomaPlainFont10);
                        lineChartSelector.setFocusable(false);
                        lineChartSelector.addItem("SELECT");
                        lineChartSelector.addItem("TOTAL");
                        lineChartSelector.addItem("MAX.");
                        lineChartSelector.addItem("MIN.");
                        lineChartSelector.setSelectedIndex(selectedGraphItem);
                        lineChartSelector.addActionListener(graphLevelReachTimeByDepthLineChart);

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
                        add(menuPanel, BorderLayout.NORTH);
                        add(chartPanel, BorderLayout.CENTER);
                        renderer.setBaseLegendTextFont(MyVars.tahomaPlainFont11);
                    } else if (selectedGraphItem == 2) {
                        XYSeries maxReachTimeByDepthSeries = new XYSeries("MAX.");
                        Map<Integer, Long> maxReachTimeByDepthMap = new HashMap<>();
                        maxReachTimeByDepthMap.put(1, 0L);

                        for (int s = 0; s < MyVars.seqs.length; s++) {
                            for (int i = 0; i < MyVars.seqs[s].length; i++) {
                                String[] itemsetAndTime = MyVars.seqs[s][i].split(":");
                                if (filterNodes.contains(itemsetAndTime[0])) continue;
                                long time = Long.valueOf(itemsetAndTime[1]);
                                if (maxReachTimeByDepthMap.containsKey(i + 1)) {
                                    if (time > maxReachTimeByDepthMap.get(i + 1)) {
                                        maxReachTimeByDepthMap.put(i + 1, time);
                                    }
                                } else {
                                    maxReachTimeByDepthMap.put(i + 1, time);
                                }
                            }
                        }

                        maxReachTimeByDepthSeries.add(1, 0);
                        if (MyVars.currentGraphDepth == 0) {
                            for (int i = 2; i <= MyVars.mxDepth; i++) {
                                if (maxReachTimeByDepthMap.containsKey(i)) {
                                    maxReachTimeByDepthSeries.add(i, maxReachTimeByDepthMap.get(i));
                                } else {
                                    maxReachTimeByDepthSeries.add(i, 0);
                                }
                            }
                        } else {
                            for (int i = 2; i <= MyVars.mxDepth; i++) {
                                if (MyVars.currentGraphDepth == i) {
                                    if (maxReachTimeByDepthMap.containsKey(i)) {
                                        maxReachTimeByDepthSeries.add(i, maxReachTimeByDepthMap.get(i));
                                    } else {
                                        maxReachTimeByDepthSeries.add(i, 0);
                                    }
                                } else {
                                    maxReachTimeByDepthSeries.add(i, 0);
                                }
                            }
                        }

                        XYSeriesCollection dataset = new XYSeriesCollection();
                        dataset.addSeries(maxReachTimeByDepthSeries);

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
                        //chart.getXYPlot().getDomainAxis().setVerticalTickLabels(true);
                        chart.setBackgroundPaint(Color.WHITE);

                        XYPlot plot = (XYPlot) chart.getPlot();
                        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
                        renderer.setSeriesPaint(0, Color.decode("#59A869"));
                        renderer.setSeriesStroke(0, new BasicStroke(1.5f));
                        renderer.setSeriesShapesVisible(0, true);
                        renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                        renderer.setSeriesFillPaint(0, Color.WHITE);

                        ChartPanel chartPanel = new ChartPanel(chart);
                        //chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );

                        JLabel titleLabel = new JLabel(" REACH T.");
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
                                        enlarge();
                                    }
                                }).start();
                            }
                        });

                        lineChartSelector = new JComboBox();
                        lineChartSelector.setBackground(Color.WHITE);
                        lineChartSelector.setFont(MyVars.tahomaPlainFont10);
                        lineChartSelector.setFocusable(false);
                        lineChartSelector.addItem("SELECT");
                        lineChartSelector.addItem("TOTAL");
                        lineChartSelector.addItem("MAX.");
                        lineChartSelector.addItem("MIN.");
                        lineChartSelector.setSelectedIndex(selectedGraphItem);
                        lineChartSelector.addActionListener(graphLevelReachTimeByDepthLineChart);

                        JPanel btnPanel = new JPanel();
                        btnPanel.setBackground(Color.WHITE);
                        btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
                        btnPanel.add(lineChartSelector);
                       // btnPanel.add(depthMenu);
                        btnPanel.add(enlargeBtn);

                        JPanel menuPanel = new JPanel();
                        menuPanel.setLayout(new BorderLayout(0, 0));
                        menuPanel.setBackground(Color.WHITE);
                        menuPanel.add(titlePanel, BorderLayout.WEST);
                        menuPanel.add(btnPanel, BorderLayout.CENTER);
                        add(menuPanel, BorderLayout.NORTH);
                        add(chartPanel, BorderLayout.CENTER);
                        renderer.setBaseLegendTextFont(MyVars.tahomaPlainFont11);
                    } else if (selectedGraphItem == 3) {
                        XYSeries minReachTimeByDepthSeries = new XYSeries("MIN.");
                        Map<Integer, Long> minReachTimeByDepthMap = new HashMap<>();
                        for (int i = 0; i <= MyVars.mxDepth; i++) {minReachTimeByDepthMap.put(i, 10000000000000000L);}

                        for (int s = 0; s < MyVars.seqs.length; s++) {
                            for (int i = 0; i < MyVars.seqs[s].length; i++) {
                                String[] itemsetAndTime = MyVars.seqs[s][i].split(":");
                                if (filterNodes.contains(itemsetAndTime[0])) continue;
                                long time = Long.valueOf(itemsetAndTime[1]);
                                if (time > 0 && time < minReachTimeByDepthMap.get(i)) {
                                    minReachTimeByDepthMap.put(i, time);
                                }
                            }
                        }

                        if (MyVars.currentGraphDepth == 0) {
                            for (int i = 1; i <= MyVars.mxDepth; i++) {
                                if (minReachTimeByDepthMap.get(i) == 10000000000000000L) {
                                    minReachTimeByDepthSeries.add(i, 0L);
                                } else {
                                    minReachTimeByDepthSeries.add(i, minReachTimeByDepthMap.get(i));
                                }
                            }
                        } else {
                            for (int i = 1; i <= MyVars.mxDepth; i++) {
                                if (MyVars.currentGraphDepth == i) {
                                    if (minReachTimeByDepthMap.get(i) == 10000000000000000L) {
                                        minReachTimeByDepthSeries.add(i, 0L);
                                    } else {
                                        minReachTimeByDepthSeries.add(i, minReachTimeByDepthMap.get(i));
                                    }
                                } else {
                                    minReachTimeByDepthSeries.add(i, 0);
                                }
                            }
                        }

                        XYSeriesCollection dataset = new XYSeriesCollection();
                        dataset.addSeries(minReachTimeByDepthSeries);

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
                        //chart.getXYPlot().getDomainAxis().setVerticalTickLabels(true);
                        chart.setBackgroundPaint(Color.WHITE);

                        XYPlot plot = (XYPlot) chart.getPlot();
                        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
                        renderer.setSeriesPaint(0, Color.BLUE);
                        renderer.setSeriesStroke(0, new BasicStroke(1.5f));
                        renderer.setSeriesShapesVisible(0, true);
                        renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                        renderer.setSeriesFillPaint(0, Color.WHITE);

                        ChartPanel chartPanel = new ChartPanel(chart);

                        JLabel titleLabel = new JLabel(" REACH T.");
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
                                        enlarge();
                                    }
                                }).start();
                            }
                        });

                        lineChartSelector = new JComboBox();
                        lineChartSelector.setBackground(Color.WHITE);
                        lineChartSelector.setFont(MyVars.tahomaPlainFont10);
                        lineChartSelector.setFocusable(false);
                        lineChartSelector.addItem("SELECT");
                        lineChartSelector.addItem("TOTAL");
                        lineChartSelector.addItem("MAX.");
                        lineChartSelector.addItem("MIN.");
                        lineChartSelector.setSelectedIndex(selectedGraphItem);
                        lineChartSelector.addActionListener(graphLevelReachTimeByDepthLineChart);

                        JPanel btnPanel = new JPanel();
                        btnPanel.setBackground(Color.WHITE);
                        btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
                        btnPanel.add(lineChartSelector);
                       // btnPanel.add(depthMenu);
                        btnPanel.add(enlargeBtn);

                        JPanel menuPanel = new JPanel();
                        menuPanel.setLayout(new BorderLayout(0, 0));
                        menuPanel.setBackground(Color.WHITE);
                        menuPanel.add(titlePanel, BorderLayout.WEST);
                        menuPanel.add(btnPanel, BorderLayout.CENTER);
                        add(menuPanel, BorderLayout.NORTH);

                        add(chartPanel, BorderLayout.CENTER);
                        renderer.setBaseLegendTextFont(MyVars.tahomaPlainFont11);

                        revalidate();
                        repaint();
                    } else if (selectedGraphItem == 4) {
                        XYSeries reachTimeByDepthDropOffSeries = new XYSeries("DROP-OFF");
                        Map<Integer, Long> reachTimeByDepthMap = new HashMap<>();
                        reachTimeByDepthMap.put(1, 0L);

                        for (int s = 0; s < MyVars.seqs.length; s++) {
                            for (int i = 0; i < MyVars.seqs[s].length; i++) {
                                String[] itemsetAndTime = MyVars.seqs[s][i].split(":");
                                if (filterNodes.contains(itemsetAndTime[0])) continue;
                                long time = Long.valueOf(itemsetAndTime[1]);
                                if (reachTimeByDepthMap.containsKey(i + 1)) {
                                    reachTimeByDepthMap.put(i + 1, reachTimeByDepthMap.get(i + 1) + time);
                                } else {
                                    reachTimeByDepthMap.put(i + 1, time);
                                }
                            }
                        }

                        XYSeriesCollection dataset = new XYSeriesCollection();
                        dataset.addSeries(reachTimeByDepthDropOffSeries);

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
                        //chart.getXYPlot().getDomainAxis().setVerticalTickLabels(true);
                        chart.setBackgroundPaint(Color.WHITE);

                        XYPlot plot = (XYPlot) chart.getPlot();
                        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
                        renderer.setSeriesPaint(0, Color.MAGENTA);
                        renderer.setSeriesStroke(0, new BasicStroke(1.5f));
                        renderer.setSeriesShapesVisible(0, true);
                        renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                        renderer.setSeriesFillPaint(0, Color.WHITE);

                        ChartPanel chartPanel = new ChartPanel(chart);

                        JLabel titleLabel = new JLabel(" REACH T.");
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
                                        enlarge();
                                    }
                                }).start();
                            }
                        });

                        lineChartSelector = new JComboBox();
                        lineChartSelector.setBackground(Color.WHITE);
                        lineChartSelector.setFont(MyVars.tahomaPlainFont10);
                        lineChartSelector.setFocusable(false);
                        lineChartSelector.addItem("SELECT");
                        lineChartSelector.addItem("TOTAL");
                        lineChartSelector.addItem("MAX.");
                        lineChartSelector.addItem("MIN.");
                        lineChartSelector.setSelectedIndex(selectedGraphItem);
                        lineChartSelector.addActionListener(graphLevelReachTimeByDepthLineChart);

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
                        add(menuPanel, BorderLayout.NORTH);

                        add(chartPanel, BorderLayout.CENTER);
                        renderer.setBaseLegendTextFont(MyVars.tahomaPlainFont11);
                        instances++;
                    } else if (selectedGraphItem == 4) {
                        XYSeries avgReachTimeByDepthSeries = new XYSeries("AVG.");
                        if (MyVars.currentGraphDepth == 0) {
                            Collection<MyNode> nodes = MyVars.g.getVertices();
                            for (int i = 1; i <= MyVars.mxDepth; i++) {
                                long totalReachTime = 0L;
                                int count = 0;
                                for (MyNode n : nodes) {
                                    if (n.getCurrentValue() == 0) continue;
                                    if (n.getNodeDepthInfoMap().containsKey(i)) {
                                        totalReachTime += n.getNodeDepthInfo(i).getReachTime();
                                        count++;
                                    }
                                }
                                avgReachTimeByDepthSeries.add(i, (double)totalReachTime/count);
                            }
                        } else {
                            Collection<MyNode> nodes = MyVars.g.getVertices();
                            for (int i = 1; i <= MyVars.mxDepth; i++) {
                                if (i == MyVars.currentGraphDepth) {
                                    long totalReachTime = 0L;
                                    int count = 0;
                                    for (MyNode n : nodes) {
                                        if (n.getCurrentValue() == 0) continue;
                                        if (n.getNodeDepthInfoMap().containsKey(i)) {
                                            totalReachTime += n.getNodeDepthInfo(i).getReachTime();
                                            count++;
                                        }
                                    }
                                    avgReachTimeByDepthSeries.add(i, (double)totalReachTime/count);
                                } else {
                                    avgReachTimeByDepthSeries.add(i, 0D);
                                }
                            }
                        }

                        XYSeriesCollection dataset = new XYSeriesCollection();
                        dataset.addSeries(avgReachTimeByDepthSeries);

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
                        renderer.setSeriesPaint(0, Color.BLUE);
                        renderer.setSeriesStroke(0, new BasicStroke(1.5f));
                        renderer.setSeriesShapesVisible(0, true);
                        renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                        renderer.setSeriesFillPaint(0, Color.WHITE);

                        ChartPanel chartPanel = new ChartPanel(chart);

                        JLabel titleLabel = new JLabel(" REACH T.");
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
                                        enlarge();
                                    }
                                }).start();
                            }
                        });

                        lineChartSelector = new JComboBox();
                        String[] tooltips = {"SELECT A CHART FOR A DEPTH DISTRIBUTION.",
                                "TOTAL TIME DISTRIBUTION BY DEPTH.",
                                "MAX. TIME DISTRIBUTION BY DEPTH",
                                "MIN. TIME DISTRIBUTION BY DEPTH."};
                        lineChartSelector.setRenderer(new MyComboBoxTooltipRenderer(tooltips));
                        lineChartSelector.setBackground(Color.WHITE);
                        lineChartSelector.setFont(MyVars.tahomaPlainFont10);
                        lineChartSelector.setFocusable(false);
                        lineChartSelector.addItem("SELECT");
                        lineChartSelector.addItem("TOTAL");
                        lineChartSelector.addItem("MAX.");
                        lineChartSelector.addItem("MIN.");
                        lineChartSelector.setSelectedIndex(selectedGraphItem);
                        lineChartSelector.addActionListener(graphLevelReachTimeByDepthLineChart);

                        JPanel btnPanel = new JPanel();
                        btnPanel.setBackground(Color.WHITE);
                        btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
                        btnPanel.add(lineChartSelector);
                        btnPanel.add(enlargeBtn);

                        JPanel menuPanel = new JPanel();
                        menuPanel.setLayout(new BorderLayout(0, 0));
                        menuPanel.setBackground(Color.WHITE);
                        menuPanel.add(titlePanel, BorderLayout.WEST);
                        menuPanel.add(btnPanel, BorderLayout.CENTER);
                        add(menuPanel, BorderLayout.NORTH);

                        add(chartPanel, BorderLayout.CENTER);
                        renderer.setBaseLegendTextFont(MyVars.tahomaPlainFont11);
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void enlarge() {
        new Thread(new Runnable() {
            @Override public void run() {
                MyProgressBar pb = new MyProgressBar(false);
                JFrame frame = new JFrame(" REACH TIME STATISTICS BY DEPTH");
                frame.setLayout(new BorderLayout());
                frame.setBackground(Color.WHITE);
                frame.setPreferredSize(new Dimension(450, 350));
                pb.updateValue(20, 100);
                MyGraphLevelReachTimeByDepthLineChart graphLevelReachTimeByDepthLineChart = new MyGraphLevelReachTimeByDepthLineChart();
                frame.getContentPane().add(graphLevelReachTimeByDepthLineChart, BorderLayout.CENTER);
                frame.pack();
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                pb.updateValue(60, 100);
                pb.updateValue(100,100);
                pb.dispose();
                frame.setCursor(Cursor.HAND_CURSOR);
                frame.setVisible(true);
                frame.setAlwaysOnTop(true);
                frame.setAlwaysOnTop(false);
            }
        }).start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        selectedGraphItem = lineChartSelector.getSelectedIndex();
        decorate();
    }
}
