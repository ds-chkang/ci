package medousa.sequential.graph.stats.singlenode;

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

public class MySingleNodePredecessorsAndSuccessorsByDepthLineChart
extends JPanel{

    public static int instances = 0;
    private JComboBox chartMenu;
    private int selectedChart = 0;
    public MySingleNodePredecessorsAndSuccessorsByDepthLineChart() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                decorate();
            }
        });
    }

        public void decorate() {
        try {
            removeAll();
            setLayout(new BorderLayout(5, 5));
            setBackground(Color.WHITE);

            XYSeries successorSeries = new XYSeries("S.");
            XYSeries predecessorSeries = new XYSeries("P.");

            XYSeriesCollection dataset = new XYSeriesCollection();

            if (selectedChart == 0) {
                if (MySequentialGraphVars.getSequentialGraphViewer().singleNode.getName().contains("x")) {
                    int total = 0;
                    for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                        String n = MySequentialGraphVars.seqs[s][0].split(":")[0];
                        if (n.equals(MySequentialGraphVars.getSequentialGraphViewer().singleNode.getName())) {
                            total += (MySequentialGraphVars.seqs[s].length-1);
                        }
                    }
                    for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                        if (i == 1) {
                            successorSeries.add(i, total);
                            predecessorSeries.add(i, 0);
                        } else {
                            successorSeries.add(i, 0);
                            predecessorSeries.add(i, 0);
                        }
                    }
                    dataset.removeAllSeries();
                    dataset.addSeries(successorSeries);
                    dataset.addSeries(predecessorSeries);
                } else {
                    Map<Integer, Set<String>> predecessorMap = new HashMap<>();
                    for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                        for (int i = 1; i < MySequentialGraphVars.seqs[s].length; i++) {
                            String ps = MySequentialGraphVars.seqs[s][i-1].split(":")[0];
                            String n = MySequentialGraphVars.seqs[s][i].split(":")[0];
                            if (n.equals(MySequentialGraphVars.getSequentialGraphViewer().singleNode.getName())) {
                                if (predecessorMap.containsKey(i + 1)) {
                                    Set<String> predecessors = predecessorMap.get(i + 1);
                                    predecessors.add(ps);
                                    predecessorMap.put(i + 1, predecessors);
                                } else {
                                    Set<String> predecessorSet = new HashSet<>();
                                    predecessorSet.add(ps);
                                    predecessorMap.put(i + 1, predecessorSet);
                                }
                            }
                        }
                    }

                    Map<Integer, Set<String>> successorMap = new HashMap<>();
                    for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                        for (int i = 0; i < MySequentialGraphVars.seqs[s].length - 1; i++) {
                            String n = MySequentialGraphVars.seqs[s][i].split(":")[0];
                            String ss = MySequentialGraphVars.seqs[s][i+1].split(":")[0];
                            if (n.equals(MySequentialGraphVars.getSequentialGraphViewer().singleNode.getName())) {
                                if (successorMap.containsKey(i + 1)) {
                                    Set<String> successors = successorMap.get(i + 1);
                                    successors.add(ss);
                                    successorMap.put(i + 1, successors);
                                } else {
                                    Set<String> successors = new HashSet<>();
                                    successors.add(ss);
                                    successorMap.put(i + 1, successors);
                                }
                            }
                        }
                    }

                    for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                        if (predecessorMap.containsKey(i)) {
                            predecessorSeries.add(i, predecessorMap.get(i).size());
                        } else {
                            predecessorSeries.add(i, 0);
                        }
                    }

                    for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                        if (successorMap.containsKey(i)) {
                            successorSeries.add(i, successorMap.get(i).size());
                        } else {
                            successorSeries.add(i, 0);
                        }
                    }

                    dataset.removeAllSeries();
                    dataset.addSeries(predecessorSeries);
                    dataset.addSeries(successorSeries);
                }

                JFreeChart chart = ChartFactory.createXYLineChart("", "P. & S.", "", dataset);
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
                renderer.setSeriesPaint(0, Color.RED);
                renderer.setSeriesStroke(0, new BasicStroke(1.5f));
                renderer.setSeriesShapesVisible(0, true);
                renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                renderer.setSeriesFillPaint(0, Color.WHITE);

                renderer.setSeriesPaint(1, Color.BLUE);
                renderer.setSeriesStroke(1, new BasicStroke(1.5f));
                renderer.setSeriesShapesVisible(1, true);
                renderer.setSeriesShape(1, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                renderer.setSeriesFillPaint(1, Color.WHITE);

                ChartPanel chartPanel = new ChartPanel(chart);
                chartPanel.setPreferredSize(new Dimension(560, 367));

                JLabel titleLabel = new JLabel(" PRED. & SUCC.");
                titleLabel.setToolTipText("UNIQUE NODE DISTRIBUTION BY DEPTH");
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

                chartMenu = new JComboBox();
                chartMenu.setFocusable(false);
                chartMenu.setFont(MySequentialGraphVars.tahomaPlainFont11);
                chartMenu.setBackground(Color.WHITE);
                chartMenu.addItem("SELECT");
                chartMenu.addItem("P.");
                chartMenu.addItem("S.");
                chartMenu.setSelectedIndex(selectedChart);
                chartMenu.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (chartMenu.getSelectedIndex() == 0) {
                            selectedChart = 0;
                            decorate();
                        } else if (chartMenu.getSelectedIndex() == 1) {
                            selectedChart = 1;
                            decorate();
                        } else if (chartMenu.getSelectedIndex() == 2) {
                            selectedChart = 2;
                            decorate();
                        }
                    }
                });

                JPanel menuPanel = new JPanel();
                menuPanel.setBackground(Color.WHITE);
                menuPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

                JPanel topPanel = new JPanel();
                topPanel.setLayout(new BorderLayout(0, 0));
                topPanel.setBackground(Color.WHITE);
                topPanel.add(menuPanel, BorderLayout.CENTER);
                menuPanel.add(chartMenu);
                menuPanel.add(enlargeBtn);
                topPanel.add(titlePanel, BorderLayout.WEST);

                add(topPanel, BorderLayout.NORTH);
                renderer.setBaseLegendTextFont(MySequentialGraphVars.tahomaPlainFont11);
                add(chartPanel, BorderLayout.CENTER);
                revalidate();
                repaint();
            } else if (selectedChart == 1) {
                Map<Integer, Set<String>> predecessorMap = new HashMap<>();
                for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                    for (int i = 1; i < MySequentialGraphVars.seqs[s].length; i++) {
                        String ps = MySequentialGraphVars.seqs[s][i - 1].split(":")[0];
                        String n = MySequentialGraphVars.seqs[s][i].split(":")[0];
                        if (n.equals(MySequentialGraphVars.getSequentialGraphViewer().singleNode.getName())) {
                            if (predecessorMap.containsKey(i + 1)) {
                                Set<String> predecessors = predecessorMap.get(i + 1);
                                predecessors.add(ps);
                                predecessorMap.put(i + 1, predecessors);
                            } else {
                                Set<String> predecessorSet = new HashSet<>();
                                predecessorSet.add(ps);
                                predecessorMap.put(i + 1, predecessorSet);
                            }
                        }
                    }


                    for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                        if (predecessorMap.containsKey(i)) {
                            predecessorSeries.add(i, predecessorMap.get(i).size());
                        } else {
                            predecessorSeries.add(i, 0);
                        }
                    }

                    dataset.removeAllSeries();
                    dataset.addSeries(predecessorSeries);
                }

                JFreeChart chart = ChartFactory.createXYLineChart("", "P. & S.", "", dataset);
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
                renderer.setSeriesPaint(0, Color.RED);
                renderer.setSeriesStroke(0, new BasicStroke(1.5f));
                renderer.setSeriesShapesVisible(0, true);
                renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                renderer.setSeriesFillPaint(0, Color.WHITE);

                ChartPanel chartPanel = new ChartPanel(chart);
                chartPanel.setPreferredSize(new Dimension(560, 367));

                JLabel titleLabel = new JLabel(" PRED. & SUCC.");
                titleLabel.setToolTipText("UNIQUE NODE DISTRIBUTION BY DEPTH");
                titleLabel.setFont(MySequentialGraphVars.tahomaBoldFont11);
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

                chartMenu = new JComboBox();
                chartMenu.setFocusable(false);
                chartMenu.setFont(MySequentialGraphVars.tahomaPlainFont11);
                chartMenu.setBackground(Color.WHITE);
                chartMenu.addItem("SELECT");
                chartMenu.addItem("P.");
                chartMenu.addItem("S.");
                chartMenu.setSelectedIndex(selectedChart);
                chartMenu.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (chartMenu.getSelectedIndex() == 0) {
                            selectedChart = 0;
                            decorate();
                        } else if (chartMenu.getSelectedIndex() == 1) {
                            selectedChart = 1;
                            decorate();
                        } else if (chartMenu.getSelectedIndex() == 2) {
                            selectedChart = 2;
                            decorate();
                        }
                    }
                });

                JPanel menuPanel = new JPanel();
                menuPanel.setBackground(Color.WHITE);
                menuPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

                JPanel topPanel = new JPanel();
                topPanel.setLayout(new BorderLayout(0, 0));
                topPanel.setBackground(Color.WHITE);
                topPanel.add(menuPanel, BorderLayout.CENTER);
                menuPanel.add(chartMenu);
                menuPanel.add(enlargeBtn);
                topPanel.add(titlePanel, BorderLayout.WEST);

                add(topPanel, BorderLayout.NORTH);
                renderer.setBaseLegendTextFont(MySequentialGraphVars.tahomaPlainFont11);
                add(chartPanel, BorderLayout.CENTER);
                revalidate();
                repaint();
            } else if (selectedChart == 2) {
                if (MySequentialGraphVars.getSequentialGraphViewer().singleNode.getName().contains("x")) {
                    int total = 0;
                    for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                        String n = MySequentialGraphVars.seqs[s][0].split(":")[0];
                        if (n.equals(MySequentialGraphVars.getSequentialGraphViewer().singleNode.getName())) {
                            total += (MySequentialGraphVars.seqs[s].length - 1);
                        }
                    }
                    for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                        if (i == 1) {
                            successorSeries.add(i, total);
                            predecessorSeries.add(i, 0);
                        } else {
                            successorSeries.add(i, 0);
                            predecessorSeries.add(i, 0);
                        }
                    }

                    dataset.removeAllSeries();
                    dataset.addSeries(successorSeries);
                } else {
                    Map<Integer, Set<String>> successorMap = new HashMap<>();
                    for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                        for (int i = 0; i < MySequentialGraphVars.seqs[s].length - 1; i++) {
                            String n = MySequentialGraphVars.seqs[s][i].split(":")[0];
                            String ss = MySequentialGraphVars.seqs[s][i + 1].split(":")[0];
                            if (n.equals(MySequentialGraphVars.getSequentialGraphViewer().singleNode.getName())) {
                                if (successorMap.containsKey(i + 1)) {
                                    Set<String> successors = successorMap.get(i + 1);
                                    successors.add(ss);
                                    successorMap.put(i + 1, successors);
                                } else {
                                    Set<String> successors = new HashSet<>();
                                    successors.add(ss);
                                    successorMap.put(i + 1, successors);
                                }
                            }
                        }
                    }

                    for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                        if (successorMap.containsKey(i)) {
                            successorSeries.add(i, successorMap.get(i).size());
                        } else {
                            successorSeries.add(i, 0);
                        }
                    }

                    dataset.removeAllSeries();
                    dataset.addSeries(successorSeries);
                }

                JFreeChart chart = ChartFactory.createXYLineChart("", "P. & S.", "", dataset);
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
                renderer.setSeriesPaint(0, Color.RED);
                renderer.setSeriesStroke(0, new BasicStroke(1.5f));
                renderer.setSeriesShapesVisible(0, true);
                renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                renderer.setSeriesFillPaint(0, Color.WHITE);

                ChartPanel chartPanel = new ChartPanel(chart);
                chartPanel.setPreferredSize(new Dimension(560, 367));

                JLabel titleLabel = new JLabel(" PRED. & SUCC.");
                titleLabel.setToolTipText("UNIQUE NODE DISTRIBUTION BY DEPTH");
                titleLabel.setFont(MySequentialGraphVars.tahomaBoldFont11);
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

                chartMenu = new JComboBox();
                chartMenu.setFocusable(false);
                chartMenu.setFont(MySequentialGraphVars.tahomaPlainFont11);
                chartMenu.setBackground(Color.WHITE);
                chartMenu.addItem("SELECT");
                chartMenu.addItem("P.");
                chartMenu.addItem("S.");
                chartMenu.setSelectedIndex(selectedChart);
                chartMenu.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (chartMenu.getSelectedIndex() == 0) {
                            selectedChart = 0;
                            decorate();
                        } else if (chartMenu.getSelectedIndex() == 1) {
                            selectedChart = 1;
                            decorate();
                        } else if (chartMenu.getSelectedIndex() == 2) {
                            selectedChart = 2;
                            decorate();
                        }
                    }
                });

                JPanel menuPanel = new JPanel();
                menuPanel.setBackground(Color.WHITE);
                menuPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

                JPanel topPanel = new JPanel();
                topPanel.setLayout(new BorderLayout(0, 0));
                topPanel.setBackground(Color.WHITE);
                topPanel.add(menuPanel, BorderLayout.CENTER);
                menuPanel.add(chartMenu);
                menuPanel.add(enlargeBtn);
                topPanel.add(titlePanel, BorderLayout.WEST);

                add(topPanel, BorderLayout.NORTH);
                renderer.setBaseLegendTextFont(MySequentialGraphVars.tahomaPlainFont11);
                add(chartPanel, BorderLayout.CENTER);
                revalidate();
                repaint();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void enlarge() {
        MyProgressBar pb = new MyProgressBar(false);
        JFrame frame = new JFrame("PREDECESSOR AND SUCCESSOR BY DEPTH DISTRIBUTION");
        frame.setLayout(new BorderLayout());
        frame.setBackground(Color.WHITE);
        frame.setPreferredSize(new Dimension(450, 350));
        pb.updateValue(20, 100);

        MySingleNodePredecessorsAndSuccessorsByDepthLineChart uniqueNodesByDepthLineChart = new MySingleNodePredecessorsAndSuccessorsByDepthLineChart();
        frame.getContentPane().add(uniqueNodesByDepthLineChart, BorderLayout.CENTER);
        frame.pack();
        pb.updateValue(60, 100);
        pb.updateValue(100,100);
        pb.dispose();

        frame.setCursor(Cursor.HAND_CURSOR);
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);
        frame.setAlwaysOnTop(false);
    }

}
