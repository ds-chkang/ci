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

public class MyGraphLevelReachTimeByDepthLineChart
extends JPanel
implements ActionListener {

    public static int instances = 0;
    private int selectedGraph = 0;
    private float toTime = 1f;
    private int selectedTime = 0;
    XYSeries reachTimeByDepthSeries;
    XYSeries maxReachTimeByDepthSeries;
    XYSeries minReachTimeByDepthSeries;

    Map<Integer, Long> reachTimeByDepthMap;
    Map<Integer, Long> maxReachTimeByDepthMap;
    Map<Integer, Long> minReachTimeByDepthMap;

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
            this.minReachTimeByDepthMap.put(i, 10000000000000000L);
        }

        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
            for (MyNode n : nodes) {
                if (n.getNodeDepthInfoMap().containsKey(i)) {
                    long time = (long) ((float)n.getNodeDepthInfo(i).getReachTime()/toTime);

                    if (this.reachTimeByDepthMap.containsKey(i)) {
                        this.reachTimeByDepthMap.put(i, this.reachTimeByDepthMap.get(i) + time);
                    } else {
                        this.reachTimeByDepthMap.put(i, time);
                    }

                    if (this.maxReachTimeByDepthMap.containsKey(i)) {
                        if (time > maxReachTimeByDepthMap.get(i)) {
                            this.maxReachTimeByDepthMap.put(i, time);
                        }
                    } else {
                        this.maxReachTimeByDepthMap.put(i, time);
                    }

                    if (time > 0) {
                        if (time < this.minReachTimeByDepthMap.get(i)) {
                            this.minReachTimeByDepthMap.put(i, time);
                        }
                    }
                }
            }
        }

        if (MySequentialGraphVars.currentGraphDepth == 0) {
            for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                if (this.reachTimeByDepthMap.containsKey(i)) {
                    reachTimeByDepthSeries.add(i, this.reachTimeByDepthMap.get(i));
                } else {
                    this.reachTimeByDepthSeries.add(i, 0);
                }

                if (this.maxReachTimeByDepthMap.containsKey(i)) {
                    this.maxReachTimeByDepthSeries.add(i, this.maxReachTimeByDepthMap.get(i));
                } else {
                    this.maxReachTimeByDepthSeries.add(i, 0);
                }

                if (this.minReachTimeByDepthMap.get(i) == 10000000000000000L) {
                    this.minReachTimeByDepthSeries.add(i, 0L);
                } else {
                    this.minReachTimeByDepthSeries.add(i, this.minReachTimeByDepthMap.get(i));
                }
            }
        } else {
            for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                if (MySequentialGraphVars.currentGraphDepth == i) {
                    if (this.reachTimeByDepthMap.containsKey(i)) {
                        this.reachTimeByDepthSeries.add(i, this.reachTimeByDepthMap.get(i));
                        this.maxReachTimeByDepthSeries.add(i, this.maxReachTimeByDepthMap.get(i));

                        if (this.minReachTimeByDepthMap.get(i) == 10000000000000000L) {
                            this.minReachTimeByDepthSeries.add(i, 0L);
                        } else {
                            this.minReachTimeByDepthSeries.add(i, this.minReachTimeByDepthMap.get(i));
                        }
                    } else {
                        this.reachTimeByDepthSeries.add(i, 0);
                        this.maxReachTimeByDepthSeries.add(i, 0);
                    }
                } else {
                    this.reachTimeByDepthSeries.add(i, 0);
                    this.maxReachTimeByDepthSeries.add(i, 0);
                    this.minReachTimeByDepthSeries.add(i, 0L);
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

                    ChartPanel chartPanel = new ChartPanel(chart);

                    JLabel titleLabel = new JLabel(" R. T.");
                    titleLabel.setToolTipText("REACH TIME BY DEPTH");
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
                        @Override public void actionPerformed(ActionEvent e) {
                            new Thread(new Runnable() {
                                @Override public void run() {
                                    enlarge();
                                }
                            }).start();
                        }
                    });

                    JComboBox graphMenu = new JComboBox();
                    graphMenu.setBackground(Color.WHITE);
                    graphMenu.setFont(MySequentialGraphVars.tahomaPlainFont11);
                    graphMenu.setFocusable(false);
                    graphMenu.addItem("SELECT");
                    graphMenu.addItem("TOTAL");
                    graphMenu.addItem("MAX.");
                    graphMenu.addItem("MIN.");
                    graphMenu.setSelectedIndex(selectedGraph);
                    graphMenu.addActionListener(new ActionListener() {
                        @Override public void actionPerformed(ActionEvent e) {
                            selectedGraph = graphMenu.getSelectedIndex();
                            decorate();
                        }
                    });

                    JComboBox timeMenu = new JComboBox();
                    timeMenu.setBackground(Color.WHITE);
                    timeMenu.setFont(MySequentialGraphVars.tahomaPlainFont11);
                    timeMenu.setFocusable(false);
                    timeMenu.addItem("SEC.");
                    timeMenu.addItem("MIN.");
                    timeMenu.addItem("HR.");
                    timeMenu.setSelectedIndex(selectedTime);
                    timeMenu.addActionListener(new ActionListener() {
                        @Override public void actionPerformed(ActionEvent e) {
                            if (timeMenu.getSelectedIndex() == 0) {
                                selectedTime = timeMenu.getSelectedIndex();
                                toTime = 1;
                                decorate();
                            } else if (timeMenu.getSelectedIndex() == 1) {
                                selectedTime = timeMenu.getSelectedIndex();
                                toTime = 60;
                                decorate();
                            } else if (timeMenu.getSelectedIndex() == 2) {
                                selectedTime = timeMenu.getSelectedIndex();
                                toTime = 3600;
                                decorate();
                            }
                        }
                    });

                    JPanel btnPanel = new JPanel();
                    btnPanel.setBackground(Color.WHITE);
                    btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
                    btnPanel.add(graphMenu);
                    btnPanel.add(timeMenu);
                    btnPanel.add(enlargeBtn);

                    JPanel northPanel = new JPanel();
                    northPanel.setLayout(new BorderLayout(0, 0));
                    northPanel.setBackground(Color.WHITE);
                    northPanel.add(titlePanel, BorderLayout.WEST);
                    northPanel.add(btnPanel, BorderLayout.CENTER);

                    add(northPanel, BorderLayout.NORTH);
                    add(chartPanel, BorderLayout.CENTER);
                    renderer.setBaseLegendTextFont(MySequentialGraphVars.tahomaPlainFont11);
                    //chart.removeLegend();

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
