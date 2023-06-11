package medousa.sequential.graph.stats.multinode;

import medousa.sequential.graph.MyEdge;
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
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.util.Collection;
import java.util.TreeMap;

public class MyMultiNodeSharedSuccessorEdgeValueDistributionLineChart
extends JPanel {

    public static int instances = 0;
    private static boolean MAXIMIZED;
    public MyMultiNodeSharedSuccessorEdgeValueDistributionLineChart() {
        this.decorate();
    }

    public void decorate() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                try {
                    removeAll();
                    setLayout(new BorderLayout(3, 3));
                    setBackground(Color.WHITE);

                    if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() < 2) {
                        JLabel titleLabel = new JLabel(" SHARED S. E. V.");
                        titleLabel.setToolTipText("SHARED SUCCESSOR EDGE VALUE DISTRIBUTION");
                        titleLabel.setFont(MySequentialGraphVars.tahomaBoldFont12);
                        titleLabel.setBackground(Color.WHITE);
                        titleLabel.setForeground(Color.DARK_GRAY);

                        JPanel titlePanel = new JPanel();
                        titlePanel.setBackground(Color.WHITE);
                        titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 6));
                        titlePanel.add(titleLabel);

                        JPanel topPanel = new JPanel();
                        topPanel.setLayout(new BorderLayout(3, 8));
                        topPanel.setBackground(Color.WHITE);
                        topPanel.add(titlePanel, BorderLayout.WEST);

                        JLabel msg = new JLabel("N/A");
                        msg.setFont(MySequentialGraphVars.tahomaPlainFont12);
                        msg.setBackground(Color.WHITE);
                        msg.setHorizontalAlignment(JLabel.CENTER);
                        add(topPanel, BorderLayout.NORTH);
                        add(msg, BorderLayout.CENTER);
                    } else {
                        int count = 0;
                        int totalValue = 0;
                        TreeMap<Integer, Integer> mapByEdgeValue = new TreeMap<>();
                        XYSeries successorEdgeValueSeries = new XYSeries("SHARED S. E. V.");
                        Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
                        for (MyEdge e : edges) {
                            if (e.getCurrentValue() == 0) continue;
                            if (MySequentialGraphVars.getSequentialGraphViewer().multiNodes.contains(e.getSource()) && MySequentialGraphVars.getSequentialGraphViewer().sharedSuccessors.contains(e.getDest())) {
                                int value = (int) e.getCurrentValue();
                                count++;
                                totalValue += e.getCurrentValue();
                                if (mapByEdgeValue.containsKey(value)) {
                                    mapByEdgeValue.put(value, mapByEdgeValue.get(value) + 1);
                                } else {
                                    mapByEdgeValue.put(value, 1);
                                }
                            }
                            if (!MAXIMIZED && count == 6) {
                                break;
                            } else if (MAXIMIZED && count == 200) {
                                break;
                            }
                        }
                        if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() > 6) {
                            for (Integer value : mapByEdgeValue.keySet()) {
                                successorEdgeValueSeries.add(value, mapByEdgeValue.get(value));
                            }
                        } else {
                            for (Integer value : mapByEdgeValue.keySet()) {
                                successorEdgeValueSeries.add(value, mapByEdgeValue.get(value));
                            }
                        }

                        XYSeriesCollection dataset = new XYSeriesCollection();
                        dataset.addSeries(successorEdgeValueSeries);

                        JFreeChart chart = ChartFactory.createXYLineChart("", "SHARED S. E. V.", "", dataset);
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
                        renderer.setSeriesPaint(0, Color.BLACK);
                        renderer.setSeriesStroke(0, new BasicStroke(1.5f));
                        renderer.setSeriesShapesVisible(0, true);
                        renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                        renderer.setSeriesFillPaint(0, Color.WHITE);
                        renderer.setUseFillPaint(true);

                        ChartPanel chartPanel = new ChartPanel(chart);
                        chartPanel.setPreferredSize(new Dimension(350, 367));

                        JLabel titleLabel = new JLabel(" SHARED S. E. V.");
                        titleLabel.setToolTipText("SHARED SUCCESSOR EDGE VALUE DISTRIBUTION");
                        titleLabel.setFont(MySequentialGraphVars.tahomaBoldFont12);
                        titleLabel.setBackground(Color.WHITE);
                        titleLabel.setForeground(Color.DARK_GRAY);

                        JPanel titlePanel = new JPanel();
                        titlePanel.setBackground(Color.WHITE);
                        titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
                        titlePanel.add(titleLabel);

                        JButton enlargeBtn = new JButton("+");
                        enlargeBtn.setFont(MySequentialGraphVars.tahomaBoldFont12);
                        enlargeBtn.setBackground(Color.WHITE);
                        enlargeBtn.setFocusable(false);
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

                        JPanel enlargePanel = new JPanel();
                        enlargePanel.setBackground(Color.WHITE);
                        enlargePanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 3));
                        enlargePanel.add(enlargeBtn);

                        JPanel topPanel = new JPanel();
                        topPanel.setLayout(new BorderLayout(3, 3));
                        topPanel.setBackground(Color.WHITE);
                        topPanel.add(titlePanel, BorderLayout.WEST);
                        topPanel.add(enlargePanel, BorderLayout.EAST);

                        add(chartPanel, BorderLayout.CENTER);
                        add(topPanel, BorderLayout.NORTH);
                    }
                    revalidate();
                    repaint();
                } catch (Exception ex) {ex.printStackTrace();}
            }
        });
    }

    public void enlarge() {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            MAXIMIZED = true;
            JFrame f = new JFrame(" SHARED SUCCESSOR EDGE VALUE DISTRIBUTION");
            f.setLayout(new BorderLayout(3, 3));
            f.getContentPane().add(new MyMultiNodeSharedSuccessorEdgeValueDistributionLineChart(), BorderLayout.CENTER);
            f.setPreferredSize(new Dimension(400, 300));
            f.pack();
            f.setAlwaysOnTop(true);
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.addWindowListener(new WindowAdapter() {
                @Override public void windowClosing(WindowEvent e) {
                    super.windowClosing(e);
                    MAXIMIZED = false;
                }
            });
            f.addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    f.setAlwaysOnTop(true);
                }

                @Override public void mouseExited(MouseEvent e) {
                    super.mouseExited(e);
                    f.setAlwaysOnTop(false);
                }
            });
            f.setVisible(true);
            f.setAlwaysOnTop(false);
        } catch (Exception ex) {
            MAXIMIZED = false;
            pb.updateValue(100, 100);
            pb.dispose();
        }
    }
}
