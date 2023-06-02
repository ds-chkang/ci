package medousa.sequential.graph.stats;

import medousa.MyProgressBar;
import medousa.sequential.graph.MyComboBoxTooltipRenderer;
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

public class MyGraphLevelDurationByDepthLineChart
extends JPanel
implements ActionListener {

    public static int instances = 0;
    private int selectedGraph;
    private float toTime = 1f;
    private int selectedTime = 0;

    XYSeries totalDurationByDepthSeries;
    XYSeries maxDurationByDepthSeries;
    XYSeries minDurationByDepthSeries;

    Map<Integer, Long> totalDurationByDepthMap;
    Map<Integer, Long> maxDurationByDepthMap;
    Map<Integer, Long> minDurationByDepthMap;

    public MyGraphLevelDurationByDepthLineChart() {
        this.decorate();
    }

    private void setData() {
        this.totalDurationByDepthMap = new HashMap<>();
        this.maxDurationByDepthMap = new HashMap<>();
        this.minDurationByDepthMap = new HashMap<>();

        this.totalDurationByDepthSeries = new XYSeries("TOTAL");
        this.maxDurationByDepthSeries = new XYSeries("MAX.");
        this.minDurationByDepthSeries = new XYSeries("MIN.");

        for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
            minDurationByDepthMap.put(i, 10000000000000000L);
        }

        this.totalDurationByDepthSeries = new XYSeries("TOTAL");
        this.totalDurationByDepthMap = new HashMap<>();
        this.minDurationByDepthSeries = new XYSeries("MIN.");
        this.minDurationByDepthMap = new HashMap<>();
        this.maxDurationByDepthSeries = new XYSeries("MAX.");
        this.maxDurationByDepthMap = new HashMap<>();

        for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
            minDurationByDepthMap.put(i, 10000000000000000L);
        }

        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (int i=1; i <= MySequentialGraphVars.mxDepth; i++) {
            for (MyNode n : nodes) {
                if (n.getNodeDepthInfoMap().containsKey(i)) {
                    long time = (long) (n.getNodeDepthInfo(i).getDuration()/toTime);

                    if (totalDurationByDepthMap.containsKey(i)) {
                        totalDurationByDepthMap.put(i, totalDurationByDepthMap.get(i) + time);
                    } else {
                        totalDurationByDepthMap.put(i, time);
                    }

                    if (maxDurationByDepthMap.containsKey(i)) {
                        if (time > maxDurationByDepthMap.get(i)) {
                            maxDurationByDepthMap.put(i, time);
                        }
                    } else {
                        maxDurationByDepthMap.put(i, time);
                    }

                    if (time > 0) {
                        if (minDurationByDepthMap.containsKey(i)) {
                            if (time < minDurationByDepthMap.get(i)) {
                                minDurationByDepthMap.put(i, time);
                            }
                        } else {
                            minDurationByDepthMap.put(i, time);
                        }
                    }
                }
            }
        }

        if (MySequentialGraphVars.currentGraphDepth == 0) {
            for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                if (totalDurationByDepthMap.containsKey(i)) {
                    totalDurationByDepthSeries.add(i, totalDurationByDepthMap.get(i));
                } else {
                    totalDurationByDepthSeries.add(i, 0);
                }
            }
        } else {
            for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                if (MySequentialGraphVars.currentGraphDepth == i) {
                    if (totalDurationByDepthMap.containsKey(i)) {
                        totalDurationByDepthSeries.add(i, totalDurationByDepthMap.get(i));
                    } else {
                        totalDurationByDepthSeries.add(i,0);
                    }
                } else {
                    totalDurationByDepthSeries.add(i,0);
                }
            }
        }

        if (MySequentialGraphVars.currentGraphDepth == 0) {
            for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                if (maxDurationByDepthMap.containsKey(i)) {
                    maxDurationByDepthSeries.add(i, maxDurationByDepthMap.get(i));
                } else {
                    maxDurationByDepthSeries.add(i, 0);
                }
            }
        } else {
            for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                if (MySequentialGraphVars.currentGraphDepth == i) {
                    if (maxDurationByDepthMap.containsKey(i)) {
                        maxDurationByDepthSeries.add(i, maxDurationByDepthMap.get(i));
                    } else {
                        maxDurationByDepthSeries.add(i, 0);
                    }
                } else {
                    maxDurationByDepthSeries.add(i, 0);
                }
            }
        }

        if (MySequentialGraphVars.currentGraphDepth == 0) {
            for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                if (minDurationByDepthMap.get(i) == 10000000000000000L) {
                    minDurationByDepthSeries.add(i, 0L);
                } else {
                    minDurationByDepthSeries.add(i, minDurationByDepthMap.get(i));
                }
            }
        } else {
            for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                if (MySequentialGraphVars.currentGraphDepth == i) {
                    if (minDurationByDepthMap.containsKey(i)) {
                        if (minDurationByDepthMap.get(i) == 10000000000000000L) {
                            minDurationByDepthSeries.add(i, 0L);
                        } else {
                            minDurationByDepthSeries.add(i, minDurationByDepthMap.get(i));
                        }
                    } else {
                        minDurationByDepthSeries.add(i, 0);
                    }
                } else {
                    minDurationByDepthSeries.add(i, 0);
                }
            }
        }
    }

    public void decorate() {
        try {
            SwingUtilities.invokeLater(new Runnable() {
                @Override public void run() {
                    removeAll();
                    setLayout(new BorderLayout(5, 5));
                    setBackground(Color.WHITE);
                    setData();

                    XYSeriesCollection dataset = new XYSeriesCollection();
                    if (selectedGraph == 0) {
                        dataset.addSeries(totalDurationByDepthSeries);
                        dataset.addSeries(maxDurationByDepthSeries);
                        dataset.addSeries(minDurationByDepthSeries);
                    } else if (selectedGraph == 1) {
                        dataset.addSeries(totalDurationByDepthSeries);
                    } else if (selectedGraph == 2) {
                        dataset.addSeries(maxDurationByDepthSeries);
                    } else if (selectedGraph == 3) {
                        dataset.addSeries(minDurationByDepthSeries);
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
                    renderer.setSeriesPaint(0, Color.DARK_GRAY);
                    renderer.setSeriesStroke(0, new BasicStroke(1.5f));
                    renderer.setSeriesShapesVisible(0, true);
                    renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                    renderer.setSeriesFillPaint(0, Color.WHITE);

                    renderer.setSeriesPaint(1, Color.BLUE);
                    renderer.setSeriesStroke(1, new BasicStroke(1.5f));
                    renderer.setSeriesShapesVisible(1, true);
                    renderer.setSeriesShape(1, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                    renderer.setSeriesFillPaint(1, Color.WHITE);

                    renderer.setSeriesPaint(2, Color.DARK_GRAY);//Color.decode("#59A869"));
                    renderer.setSeriesStroke(2, new BasicStroke(1.5f));
                    renderer.setSeriesShapesVisible(2, true);
                    renderer.setSeriesShape(2, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                    renderer.setSeriesFillPaint(2, Color.WHITE);
                    renderer.setUseFillPaint(true);

                    ChartPanel chartPanel = new ChartPanel(chart);

                    JLabel titleLabel = new JLabel(" DUR.");
                    titleLabel.setToolTipText("DURATION BY DEPTH");
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
                                @Override public void run() {enlarge();}
                            }).start();
                        }
                    });

                    JComboBox graphMenu = new JComboBox();
                    graphMenu.addItem("SELECT");
                    graphMenu.addItem("TOTAL");
                    graphMenu.addItem("MAX.");
                    graphMenu.addItem("MIN.");
                    graphMenu.setBackground(Color.WHITE);
                    graphMenu.setFont(MySequentialGraphVars.tahomaPlainFont11);
                    graphMenu.setFocusable(false);
                    graphMenu.setSelectedIndex(selectedGraph);
                    graphMenu.addActionListener(new ActionListener() {
                        @Override public void actionPerformed(ActionEvent e) {
                            selectedGraph = graphMenu.getSelectedIndex();
                            decorate();
                        }
                    });

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

                    JPanel btnPanel = new JPanel();
                    btnPanel.setBackground(Color.WHITE);
                    btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
                    btnPanel.add(graphMenu);
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
            JFrame f = new JFrame(" DURATION BY DEPTH");
            f.setLayout(new BorderLayout());
            f.setBackground(Color.WHITE);
            f.setPreferredSize(new Dimension(450, 350));
            f.getContentPane().add(new MyGraphLevelDurationByDepthLineChart(), BorderLayout.CENTER);
            f.pack();
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.setCursor(Cursor.HAND_CURSOR);
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
