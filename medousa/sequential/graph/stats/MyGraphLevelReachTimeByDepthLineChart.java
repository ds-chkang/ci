package medousa.sequential.graph.stats;

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

public class MyGraphLevelReachTimeByDepthLineChart
extends JPanel
implements ActionListener {

    public static int instances = 0;
    private int selectedGraph = 0;
    private XYSeries reachTimeByDepthSeries;
    private XYSeries maxReachTimeByDepthSeries;
    private XYSeries minReachTimeByDepthSeries;

    private Map<Integer, Long> reachTimeByDepthMap;
    private Map<Integer, Long> maxReachTimeByDepthMap;
    private Map<Integer, Long> minReachTimeByDepthMap;
    private float toTime = 1f;
    private int selectedTime = 0;

    public MyGraphLevelReachTimeByDepthLineChart() {
        this.decorate();
    }

    private void setData() {
        this.reachTimeByDepthMap = new HashMap<>();
        this.maxReachTimeByDepthMap = new HashMap<>();
        this.minReachTimeByDepthMap = new HashMap<>();

        this.reachTimeByDepthSeries = new XYSeries("TOTAL");
        this.maxReachTimeByDepthSeries = new XYSeries("MAX.");
        this.minReachTimeByDepthSeries = new XYSeries("MIN.");

        for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
            minReachTimeByDepthMap.put(i, 10000000000000000L);
        }

        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (int i=1; i <= MySequentialGraphVars.mxDepth; i++) {
            for (MyNode n : nodes) {
                if (n.getNodeDepthInfoMap().containsKey(i)) {
                    long time = (long) (n.getNodeDepthInfo(i).getReachTime()/toTime);

                    if (reachTimeByDepthMap.containsKey(i)) {
                        reachTimeByDepthMap.put(i, reachTimeByDepthMap.get(i) + time);
                    } else {
                        reachTimeByDepthMap.put(i, time);
                    }

                    if (maxReachTimeByDepthMap.containsKey(i)) {
                        if (time > maxReachTimeByDepthMap.get(i)) {
                            maxReachTimeByDepthMap.put(i, time);
                        }
                    } else {
                        maxReachTimeByDepthMap.put(i, time);
                    }

                    if (time > 0) {
                        if (minReachTimeByDepthMap.containsKey(i)) {
                            if (time < minReachTimeByDepthMap.get(i)) {
                                minReachTimeByDepthMap.put(i, time);
                            }
                        } else {
                            minReachTimeByDepthMap.put(i, time);
                        }
                    }
                }
            }
        }

        if (MySequentialGraphVars.currentGraphDepth == 0) {
            for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                if (reachTimeByDepthMap.containsKey(i)) {
                    reachTimeByDepthSeries.add(i, reachTimeByDepthMap.get(i));
                } else {
                    reachTimeByDepthSeries.add(i, 0);
                }
            }
        } else {
            for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                if (MySequentialGraphVars.currentGraphDepth == i) {
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

        if (MySequentialGraphVars.currentGraphDepth == 0) {
            for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                if (maxReachTimeByDepthMap.containsKey(i)) {
                    maxReachTimeByDepthSeries.add(i, maxReachTimeByDepthMap.get(i));
                } else {
                    maxReachTimeByDepthSeries.add(i, 0);
                }
            }
        } else {
            for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                if (MySequentialGraphVars.currentGraphDepth == i) {
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

        if (MySequentialGraphVars.currentGraphDepth == 0) {
            for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                if (minReachTimeByDepthMap.get(i) == 10000000000000000L) {
                    minReachTimeByDepthSeries.add(i, 0L);
                } else {
                    minReachTimeByDepthSeries.add(i, minReachTimeByDepthMap.get(i));
                }
            }
        } else {
            for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                if (MySequentialGraphVars.currentGraphDepth == i) {
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
    }

    public void decorate() {
        try {
            final MyGraphLevelReachTimeByDepthLineChart graphLevelReachTimeByDepthLineChart = this;
            SwingUtilities.invokeLater(new Runnable() {
                @Override public void run() {
                    removeAll();
                    setLayout(new BorderLayout(5, 5));
                    setBackground(Color.WHITE);
                    setData();

                    XYSeriesCollection dataset = new XYSeriesCollection();

                    if (selectedGraph == 0) {
                        dataset.addSeries(reachTimeByDepthSeries);
                        dataset.addSeries(maxReachTimeByDepthSeries);
                        dataset.addSeries(minReachTimeByDepthSeries);
                    } else if (selectedGraph == 1) {
                        dataset.addSeries(reachTimeByDepthSeries);
                    } else if (selectedGraph == 2) {
                        dataset.addSeries(maxReachTimeByDepthSeries);
                    } else if (selectedGraph == 3) {
                        dataset.addSeries(minReachTimeByDepthSeries);
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

                    renderer.setSeriesPaint(3, Color.DARK_GRAY);//Color.decode("#59A869"));
                    renderer.setSeriesStroke(3, new BasicStroke(1.5f));
                    renderer.setSeriesShapesVisible(3, true);
                    renderer.setSeriesShape(3, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                    renderer.setSeriesFillPaint(3, Color.WHITE);
                    renderer.setUseFillPaint(true);

                    ChartPanel chartPanel = new ChartPanel(chart);
                    //chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );

                    JLabel titleLabel = new JLabel(" R. T.");
                    titleLabel.setToolTipText("REACH TIME BY DEPTH");
                    titleLabel.setFont(MySequentialGraphVars.tahomaBoldFont12);
                    titleLabel.setBackground(Color.WHITE);
                    titleLabel.setForeground(Color.DARK_GRAY);

                    JComboBox timeConvertMenu = new JComboBox();
                    timeConvertMenu.addItem("SECOND");
                    timeConvertMenu.addItem("MINUTE");
                    timeConvertMenu.addItem("HOUR");
                    timeConvertMenu.setSelectedIndex(selectedTime);
                    timeConvertMenu.setFont(MySequentialGraphVars.tahomaPlainFont11);
                    timeConvertMenu.setFocusable(false);
                    timeConvertMenu.setBackground(Color.WHITE);
                    timeConvertMenu.addActionListener(new ActionListener() {
                        @Override public void actionPerformed(ActionEvent e) {
                            new Thread(new Runnable() {
                                @Override public void run() {
                                    if (timeConvertMenu.getSelectedIndex() == 0) {
                                        toTime = 1;
                                        selectedTime = 0;
                                        decorate();
                                    } else if (timeConvertMenu.getSelectedIndex() == 1) {
                                        toTime = 60;
                                        selectedTime = 1;
                                        decorate();
                                    } else if (timeConvertMenu.getSelectedIndex() == 2) {
                                        toTime = 3600;
                                        selectedTime = 2;
                                        decorate();
                                    }
                                }
                            }).start();
                        }
                    });

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

                    JComboBox lineChartSelector = new JComboBox();
                    lineChartSelector.setBackground(Color.WHITE);
                    lineChartSelector.setFont(MySequentialGraphVars.tahomaPlainFont11);
                    lineChartSelector.setFocusable(false);
                    lineChartSelector.addItem("SELECT");
                    lineChartSelector.addItem("TOTAL");
                    lineChartSelector.addItem("MAX.");
                    lineChartSelector.addItem("MIN.");
                    lineChartSelector.setSelectedIndex(selectedGraph);
                    lineChartSelector.addActionListener(new ActionListener() {
                        @Override public void actionPerformed(ActionEvent e) {
                            new Thread(new Runnable() {
                                @Override public void run() {
                                    selectedGraph = lineChartSelector.getSelectedIndex();
                                    decorate();
                                }
                            }).start();
                        }
                    });

                    JPanel btnPanel = new JPanel();
                    btnPanel.setBackground(Color.WHITE);
                    btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
                    btnPanel.add(lineChartSelector);
                    btnPanel.add(timeConvertMenu);
                    btnPanel.add(enlargeBtn);

                    JPanel menuPanel = new JPanel();
                    menuPanel.setLayout(new BorderLayout(0, 0));
                    menuPanel.setBackground(Color.WHITE);
                    menuPanel.add(titlePanel, BorderLayout.WEST);
                    menuPanel.add(btnPanel, BorderLayout.CENTER);
                    add(menuPanel, BorderLayout.NORTH);

                    add(chartPanel, BorderLayout.CENTER);
                    renderer.setBaseLegendTextFont(MySequentialGraphVars.tahomaPlainFont11);
                    chart.removeLegend();

                    revalidate();
                    repaint();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void enlarge() {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            JFrame f = new JFrame(" REACH TIME STATISTICS BY DEPTH");
            f.setLayout(new BorderLayout());
            f.setBackground(Color.WHITE);
            f.setPreferredSize(new Dimension(450, 350));
            f.getContentPane().add(new MyGraphLevelReachTimeByDepthLineChart(), BorderLayout.CENTER);
            f.pack();
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.setCursor(Cursor.HAND_CURSOR);
            f.setAlwaysOnTop(true);
            pb.updateValue(100, 100);
            pb.dispose();
            f.setVisible(true);
        } catch (Exception ex) {
            pb.updateValue(100, 100);
            pb.dispose();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
