package datamining.graph.stats;

import datamining.main.MyProgressBar;
import datamining.utils.MyMathUtil;
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
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.Ellipse2D;
import java.util.HashMap;
import java.util.Map;

public class MyGraphLevelDurationByDepthLineChart
extends JPanel
implements ActionListener {

    private int selectedGraphOption = 0;
    private JComboBox graphOptionMenu;
    private static boolean enlarged = false;
    public static int instances = 0;

    public MyGraphLevelDurationByDepthLineChart() {
        this.decorate();
    }

    public void decorate() {
        try {
            final MyGraphLevelDurationByDepthLineChart graphLevelDurationByDepthLineChart = this;
            SwingUtilities.invokeLater(new Runnable() {
                @Override public void run() {
                    removeAll();
                    setLayout(new BorderLayout(5,5));
                    setBackground(Color.WHITE);

                    graphOptionMenu = new JComboBox();
                    graphOptionMenu.addItem("SELECT");
                    graphOptionMenu.addItem("TOTAL");
                    graphOptionMenu.addItem("AVG.");
                    graphOptionMenu.addItem("MAX.");
                    graphOptionMenu.addItem("DROP-OFF");
                    graphOptionMenu.setBackground(Color.WHITE);
                    graphOptionMenu.setFocusable(false);
                    graphOptionMenu.setFont(MyVars.tahomaPlainFont10);
                    graphOptionMenu.setSelectedIndex(selectedGraphOption);

                    XYSeries totalDurationSeries = new XYSeries("TOTAL");
                    XYSeries totalDurationDropOffSeries = new XYSeries("DROP-OFF");
                    XYSeries maxDurationSeries = null;
                    XYSeries avgDurationSeries = null;

                    Map<Integer, Long> timeByDepthMap = new HashMap<>();
                    for (int s = 0; s < MyVars.seqs.length; s++) {
                        for (int itemsetIdx = 1; itemsetIdx < MyVars.seqs[s].length-1; itemsetIdx++) {
                            String [] itemsetAndTime = MyVars.seqs[s][itemsetIdx+1].split(":");
                            long time = Long.valueOf(itemsetAndTime[1]);
                            if (timeByDepthMap.containsKey(itemsetIdx)) {timeByDepthMap.put(itemsetIdx, timeByDepthMap.get(itemsetIdx)+time);}
                            else {timeByDepthMap.put(itemsetIdx, time);}
                        }
                    }

                    totalDurationDropOffSeries.add(1, 0);
                    if (MyVars.currentGraphDepth == 0) {
                        for (int depth = 2; depth <= MyVars.mxDepth; depth++) {
                            if (timeByDepthMap.containsKey(depth)) {
                                long timeDiff = timeByDepthMap.get(depth) - timeByDepthMap.get(depth - 1);
                                totalDurationDropOffSeries.add(depth, timeDiff);
                            } else {
                                totalDurationDropOffSeries.add(depth, 0);
                            }
                        }
                    } else {
                        for (int depth = 2; depth <= MyVars.mxDepth; depth++) {
                            if (MyVars.currentGraphDepth == depth) {
                                if (timeByDepthMap.containsKey(depth)) {
                                    long timeDiff = timeByDepthMap.get(depth) - timeByDepthMap.get(depth - 1);
                                    totalDurationDropOffSeries.add(depth, timeDiff);
                                }
                            } else {
                                totalDurationDropOffSeries.add(depth, 0);
                            }
                        }
                    }

                    Map<Integer, Long> maxDurationByDepthMap = new HashMap<>();
                    Map<Integer, Long> totalDurationByDepthMap = new HashMap<>();
                    Map<Integer, Integer> itemsetCountByDepthMap = new HashMap<>();

                    for (int s=0; s < MyVars.seqs.length; s++) {
                        for (int itemsetIdx = 1; itemsetIdx < MyVars.seqs[s].length; itemsetIdx++) {
                            if (itemsetIdx == 1) {
                                totalDurationByDepthMap.put(itemsetIdx, 0L);
                                maxDurationByDepthMap.put(itemsetIdx, 0L);
                                itemsetCountByDepthMap.put(itemsetIdx, 0);
                            } else {
                                long time = Long.valueOf(MyVars.seqs[s][itemsetIdx].split(":")[1]);
                                if (totalDurationByDepthMap.containsKey(itemsetIdx)) {
                                    if (maxDurationByDepthMap.get(itemsetIdx) < time) {maxDurationByDepthMap.put(itemsetIdx, time);}
                                    itemsetCountByDepthMap.put(itemsetIdx, itemsetCountByDepthMap.get(itemsetIdx)+1);
                                    totalDurationByDepthMap.put(itemsetIdx, totalDurationByDepthMap.get(itemsetIdx)+time);
                                } else {
                                    totalDurationByDepthMap.put(itemsetIdx, time);
                                    maxDurationByDepthMap.put(itemsetIdx, time);
                                    itemsetCountByDepthMap.put(itemsetIdx, 1);
                                }
                            }
                        }
                    }

                    maxDurationSeries = new XYSeries("MAX.");
                    avgDurationSeries = new XYSeries("AVG.");
                    if (MyVars.currentGraphDepth == 0) {
                        totalDurationSeries.add(1, 0.0D);
                        maxDurationSeries.add(1, 0.0D);
                        avgDurationSeries.add(1, 0.0D);
                        for (int depth = 2; depth <= totalDurationByDepthMap.size(); depth++) {
                            totalDurationSeries.add(depth, totalDurationByDepthMap.get(depth));
                            maxDurationSeries.add(depth, maxDurationByDepthMap.get(depth));
                            avgDurationSeries.add(depth, Double.valueOf(MyMathUtil.twoDecimalFormat((double) totalDurationByDepthMap.get(depth)/itemsetCountByDepthMap.get(depth))));
                        }
                    } else {
                        totalDurationSeries.add(1, 0.0D);
                        maxDurationSeries.add(1, 0.0D);
                        avgDurationSeries.add(1, 0.0D);
                        for (int depth = 2; depth <= totalDurationByDepthMap.size(); depth++) {
                            if (MyVars.currentGraphDepth == depth) {
                                totalDurationSeries.add(depth, totalDurationByDepthMap.get(depth));
                                maxDurationSeries.add(depth, maxDurationByDepthMap.get(depth));
                                avgDurationSeries.add(depth, Double.valueOf(MyMathUtil.twoDecimalFormat((double) totalDurationByDepthMap.get(depth)/itemsetCountByDepthMap.get(depth))));
                            } else {
                                totalDurationSeries.add(depth, 0);
                                maxDurationSeries.add(depth, 0);
                                avgDurationSeries.add(depth, 0);
                            }
                        }
                    }

                    XYSeriesCollection dataset = new XYSeriesCollection();
                    if (selectedGraphOption == 0) {
                        dataset.addSeries(totalDurationSeries);
                        dataset.addSeries(avgDurationSeries);
                        dataset.addSeries(maxDurationSeries);
                        dataset.addSeries(totalDurationDropOffSeries);
                    } else if (selectedGraphOption == 1) {
                        dataset.addSeries(totalDurationSeries);
                    } else if (selectedGraphOption == 2) {
                        dataset.addSeries(avgDurationSeries);
                    } else if (selectedGraphOption == 3) {
                        dataset.addSeries(maxDurationSeries);
                    } else if (selectedGraphOption == 4) {
                        dataset.addSeries(totalDurationDropOffSeries);
                    }

                    JFreeChart chart = ChartFactory.createXYLineChart("", "DEPTH", "", dataset);
                    chart.getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
                    chart.getXYPlot().setBackgroundPaint(Color.WHITE);
                    chart.getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
                    chart.getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);

                    chart.getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
                    chart.getXYPlot().getDomainAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
                    chart.getXYPlot().getRangeAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
                    chart.getXYPlot().getDomainAxis().setTickLabelFont(MyVars.tahomaPlainFont11);
                    chart.getXYPlot().getRangeAxis().setTickLabelFont(MyVars.tahomaPlainFont11);
                    chart.getXYPlot().getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
                    chart.setBackgroundPaint(Color.WHITE);

                    ChartPanel chartPanel = new ChartPanel( chart );
                    chartPanel.setPreferredSize( new Dimension( 560 , 367 ) );

                    JLabel titleLabel = new JLabel(" DURATION");
                    titleLabel.setFont(MyVars.tahomaBoldFont11);
                    titleLabel.setBackground(Color.WHITE);
                    titleLabel.setForeground(Color.DARK_GRAY);

                    JPanel titlePanel = new JPanel();
                    titlePanel.setBackground(Color.WHITE);
                    titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
                    titlePanel.add(titleLabel);

                    JButton enlargeBtn = new JButton("ENLARGE");
                    enlargeBtn.setFont(MyVars.tahomaPlainFont10);
                    enlargeBtn.setFocusable(false);
                    enlargeBtn.setBackground(Color.WHITE);
                    enlargeBtn.addActionListener(new ActionListener() {@Override
                    public void actionPerformed(ActionEvent e) {
                        new Thread(new Runnable() {@Override
                        public void run() {
                            enlarged = true;
                            enlarge();
                        }
                        }).start();
                    }
                    });
                    graphOptionMenu.addActionListener(graphLevelDurationByDepthLineChart);
                    JPanel menuPanel = new JPanel();
                    menuPanel.setLayout(new BorderLayout(0,0));
                    menuPanel.setBackground(Color.WHITE);

                    JPanel btnPanel = new JPanel();
                    btnPanel.setBackground(Color.WHITE);
                    btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
                    btnPanel.add(graphOptionMenu);
                    btnPanel.add(enlargeBtn);
                    menuPanel.add(titlePanel, BorderLayout.WEST);
                    menuPanel.add(btnPanel, BorderLayout.CENTER);
                    add(menuPanel, BorderLayout.NORTH);

                    XYPlot plot = (XYPlot) chart.getPlot();
                    XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
                    if (selectedGraphOption == 0) {
                        renderer.setSeriesPaint(0, Color.MAGENTA);
                        renderer.setSeriesStroke(0, new BasicStroke(1.3f));
                        renderer.setSeriesShapesVisible(0, true);
                        renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                        renderer.setSeriesFillPaint(0, Color.WHITE);

                        renderer.setSeriesPaint(1, Color.DARK_GRAY);
                        renderer.setSeriesStroke(1, new BasicStroke(1.3f));
                        renderer.setSeriesShapesVisible(1, true);
                        renderer.setSeriesShape(1, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                        renderer.setSeriesFillPaint(1, Color.WHITE);

                        renderer.setSeriesPaint(2, Color.decode("#F0CF4C"));
                        renderer.setSeriesStroke(2, new BasicStroke(1.3f));
                        renderer.setSeriesShapesVisible(2, true);
                        renderer.setSeriesShape(2, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                        renderer.setSeriesFillPaint(2, Color.WHITE);

                        renderer.setSeriesPaint(3, Color.RED);
                        renderer.setSeriesStroke(3, new BasicStroke(1.3f));
                        renderer.setSeriesShapesVisible(3, true);
                        renderer.setSeriesShape(3, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                        renderer.setSeriesFillPaint(3, Color.WHITE);
                        graphOptionMenu.removeActionListener(graphLevelDurationByDepthLineChart);
                        graphOptionMenu.addActionListener(graphLevelDurationByDepthLineChart);
                    } else if (selectedGraphOption ==1) {
                        renderer.setSeriesPaint(0, Color.MAGENTA);
                        renderer.setSeriesStroke(0, new BasicStroke(1.5f));
                        renderer.setSeriesShapesVisible(0, true);
                        renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                        renderer.setSeriesFillPaint(0, Color.WHITE);
                        graphOptionMenu.removeActionListener(graphLevelDurationByDepthLineChart);
                        graphOptionMenu.addActionListener(graphLevelDurationByDepthLineChart);
                    } else if (selectedGraphOption == 2) {
                        renderer.setSeriesPaint(0, Color.DARK_GRAY);
                        renderer.setSeriesStroke(0, new BasicStroke(1.5f));
                        renderer.setSeriesShapesVisible(0, true);
                        renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                        renderer.setSeriesFillPaint(0, Color.WHITE);
                        graphOptionMenu.removeActionListener(graphLevelDurationByDepthLineChart);
                        graphOptionMenu.addActionListener(graphLevelDurationByDepthLineChart);
                    } else if (selectedGraphOption == 3) {
                        renderer.setSeriesPaint(0, Color.decode("#F0CF4C"));
                        renderer.setSeriesStroke(0, new BasicStroke(1.5f));
                        renderer.setSeriesShapesVisible(0, true);
                        renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                        renderer.setSeriesFillPaint(0, Color.WHITE);
                        graphOptionMenu.removeActionListener(graphLevelDurationByDepthLineChart);
                        graphOptionMenu.addActionListener(graphLevelDurationByDepthLineChart);
                    } else if (selectedGraphOption == 4) {
                        renderer.setSeriesPaint(0, Color.RED);
                        renderer.setSeriesStroke(0, new BasicStroke(1.5f));
                        renderer.setSeriesShapesVisible(0, true);
                        renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                        renderer.setSeriesFillPaint(0, Color.WHITE);
                        graphOptionMenu.removeActionListener(graphLevelDurationByDepthLineChart);
                        graphOptionMenu.addActionListener(graphLevelDurationByDepthLineChart);
                    }
                    renderer.setBaseLegendTextFont(MyVars.tahomaPlainFont11);
                    add(menuPanel, BorderLayout.NORTH);
                    add(chartPanel, BorderLayout.CENTER);
                    instances++;
                    revalidate();
                    repaint();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        revalidate();
        repaint();
    }

    @Override public void actionPerformed(ActionEvent e) {
        new Thread(new Runnable() {
            @Override public void run() {
                if (e.getSource() == graphOptionMenu) {
                    selectedGraphOption = graphOptionMenu.getSelectedIndex();
                    graphOptionMenu = null;
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
        JFrame frame = new JFrame("DURATION BY DEPTH");
        frame.setLayout(new BorderLayout());
        frame.setBackground(Color.WHITE);
        frame.setPreferredSize(new Dimension(450, 350));
        pb.updateValue(20, 100);

        MyGraphLevelDurationByDepthLineChart reachTimeByDepthLineChart = new MyGraphLevelDurationByDepthLineChart();
        frame.getContentPane().add(reachTimeByDepthLineChart, BorderLayout.CENTER);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowListener() {
            @Override public void windowOpened(WindowEvent e) {}
            @Override public void windowClosing(WindowEvent e) {
                enlarged = false;
            }
            @Override public void windowClosed(WindowEvent e) {}
            @Override public void windowIconified(WindowEvent e) {}
            @Override public void windowDeiconified(WindowEvent e) {}
            @Override public void windowActivated(WindowEvent e) {}
            @Override public void windowDeactivated(WindowEvent e) {}
        });
        pb.updateValue(60, 100);
        pb.updateValue(100,100);
        pb.dispose();

        frame.setCursor(Cursor.HAND_CURSOR);
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);
        frame.setAlwaysOnTop(false);
    }

}
