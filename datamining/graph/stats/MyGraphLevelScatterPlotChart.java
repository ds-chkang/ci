package datamining.graph.stats;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.*;

import datamining.graph.MyEdge;
import datamining.graph.MyNode;
import datamining.utils.system.MyVars;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class MyGraphLevelScatterPlotChart extends JPanel
implements ActionListener {
    private ArrayList<Long> value1 = new ArrayList<>();
    private ArrayList<Long> value2 = new ArrayList<>();
    private String [] valueNames = new String[2];
    private XYDataset dataset;
    public static int instances = 0;
    private ChartPanel chartPanel;
    private JComboBox nodeValueOptionComboBoxMenu1 = new JComboBox();
    private JComboBox edgeValueOptionComboBoxMenu1 = new JComboBox();
    private JComboBox nodeEdgeOptionComboBoxMenu1 = new JComboBox();
    private JComboBox nodeValueOptionComboBoxMenu2 = new JComboBox();
    private JComboBox edgeValueOptionComboBoxMenu2 = new JComboBox();
    private JComboBox nodeEdgeOptionComboBoxMenu2 = new JComboBox();

    public MyGraphLevelScatterPlotChart() {
        decorate();
    }

    private void decorate() {
        final MyGraphLevelScatterPlotChart scatterPlotExample = this;

        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                removeAll();
                setLayout(new BorderLayout(3,3));
                setBackground(Color.WHITE);

                nodeValueOptionComboBoxMenu1.setBackground(Color.WHITE);
                nodeValueOptionComboBoxMenu1.setFocusable(false);
                nodeValueOptionComboBoxMenu1.setFont(MyVars.tahomaPlainFont10);
                nodeValueOptionComboBoxMenu1.addItem("NONE");
                nodeValueOptionComboBoxMenu1.addItem("CONT.");
                nodeValueOptionComboBoxMenu1.addItem("UNIQ.");
                nodeValueOptionComboBoxMenu1.addActionListener(scatterPlotExample);

                edgeValueOptionComboBoxMenu1.setBackground(Color.WHITE);
                edgeValueOptionComboBoxMenu1.setFocusable(false);
                edgeValueOptionComboBoxMenu1.setFont(MyVars.tahomaPlainFont10);
                edgeValueOptionComboBoxMenu1.addItem("NONE");
                edgeValueOptionComboBoxMenu1.addItem("CONT.");
                edgeValueOptionComboBoxMenu1.addItem("UNIQ.");
                edgeValueOptionComboBoxMenu1.addActionListener(scatterPlotExample);

                nodeEdgeOptionComboBoxMenu1.setBackground(Color.WHITE);
                nodeEdgeOptionComboBoxMenu1.setFocusable(false);
                nodeEdgeOptionComboBoxMenu1.setFont(MyVars.tahomaPlainFont10);
                nodeEdgeOptionComboBoxMenu1.addItem("N.");
                nodeEdgeOptionComboBoxMenu1.addItem("E.");
                nodeEdgeOptionComboBoxMenu1.addActionListener(scatterPlotExample);

                nodeValueOptionComboBoxMenu2.setBackground(Color.WHITE);
                nodeValueOptionComboBoxMenu2.setFocusable(false);
                nodeValueOptionComboBoxMenu2.setFont(MyVars.tahomaPlainFont10);
                nodeValueOptionComboBoxMenu2.addItem("NONE");
                nodeValueOptionComboBoxMenu2.addItem("CONT.");
                nodeValueOptionComboBoxMenu2.addItem("UNIQ.");
                nodeValueOptionComboBoxMenu2.addActionListener(scatterPlotExample);

                edgeValueOptionComboBoxMenu2.setBackground(Color.WHITE);
                edgeValueOptionComboBoxMenu2.setFocusable(false);
                edgeValueOptionComboBoxMenu2.setFont(MyVars.tahomaPlainFont10);
                edgeValueOptionComboBoxMenu2.addItem("NONE");
                edgeValueOptionComboBoxMenu2.addItem("CONT.");
                edgeValueOptionComboBoxMenu2.addItem("UNIQ.");
                edgeValueOptionComboBoxMenu2.addActionListener(scatterPlotExample);

                nodeEdgeOptionComboBoxMenu2.setBackground(Color.WHITE);
                nodeEdgeOptionComboBoxMenu2.setFocusable(false);
                nodeEdgeOptionComboBoxMenu2.setFont(MyVars.tahomaPlainFont10);
                nodeEdgeOptionComboBoxMenu2.addItem("N.");
                nodeEdgeOptionComboBoxMenu2.addItem("E.");
                nodeEdgeOptionComboBoxMenu2.addActionListener(scatterPlotExample);

                JButton drawScatterPlotBtn = new JButton("SHOW");
                drawScatterPlotBtn.setBackground(Color.WHITE);
                drawScatterPlotBtn.setFont(MyVars.tahomaPlainFont10);
                drawScatterPlotBtn.setFocusable(false);
                drawScatterPlotBtn.addActionListener(new ActionListener() {
                    @Override public void actionPerformed(ActionEvent e) {
                        dataset = createDataset();
                        JFreeChart chart = ChartFactory.createScatterPlot("", "", "", dataset);
                        if (chartPanel != null) {remove(chartPanel);}
                        chartPanel = new ChartPanel(chart);
                        add(chartPanel, BorderLayout.CENTER);
                        XYPlot plot = (XYPlot) chart.getPlot();
                        plot.setBackgroundPaint(Color.WHITE);//new Color(255,228,196));
                        chart.getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
                        chart.getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);
                        revalidate();
                        repaint();
                    }
                });

                JButton enlargeBtn = new JButton("ENLARGE");
                enlargeBtn.setFont(MyVars.tahomaPlainFont10);
                enlargeBtn.setBackground(Color.WHITE);
                enlargeBtn.setFocusable(false);
                enlargeBtn.addActionListener(new ActionListener() {
                    @Override public void actionPerformed(ActionEvent e) {
                        new Thread(new Runnable() {
                            @Override public void run() {
                                enlarge();
                            }
                        }).start();
                    }
                });

                JPanel menuPanel = new JPanel();
                menuPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 2,2));
                menuPanel.setBackground(Color.WHITE);
                menuPanel.add(nodeEdgeOptionComboBoxMenu1);
                menuPanel.add(nodeValueOptionComboBoxMenu1);
                menuPanel.add(nodeEdgeOptionComboBoxMenu2);
                menuPanel.add(nodeValueOptionComboBoxMenu2);
                menuPanel.add(drawScatterPlotBtn);
                menuPanel.add(enlargeBtn);
                add(menuPanel, BorderLayout.NORTH);

                JFreeChart chart = ChartFactory.createScatterPlot("", "", "", null);
                XYPlot plot = (XYPlot) chart.getPlot();
                plot.setBackgroundPaint(Color.WHITE);//new Color(255,228,196));
                chart.getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
                chart.getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);
                chartPanel = new ChartPanel(chart);
                add(chartPanel, BorderLayout.CENTER);
            }
        });
    }

    protected XYDataset createDataset() {
        XYSeriesCollection dataset = new XYSeriesCollection();

        //Boys (Age,weight) series
        XYSeries dataSeries = new XYSeries(valueNames[0] + ", " + valueNames[1]);
        int dataLength = value1.size();
        if (value1.size() < value2.size()) dataLength = value2.size();
        for (int i=0; i < dataLength; i++) {
            if (value1.get(i) == null && value2.get(i) != null) {
                dataSeries.add(0, value2.get(i));
            } else if (value1.get(i) != null && value2.get(i) == null) {
                dataSeries.add((double)value1.get(i), 0);
            } else if (value1.get(i) == null && value2.get(i) == null) {
                dataSeries.add(0, 0);
            } else {
                dataSeries.add(value1.get(i), value2.get(i));
            }
        }
        dataset.addSeries(dataSeries);
        return dataset;
    }

    public void enlarge() {
        MyGraphLevelScatterPlotChart betweenPathLengthDistributions = new MyGraphLevelScatterPlotChart();
        betweenPathLengthDistributions.setLayout(new BorderLayout(3,3));
        betweenPathLengthDistributions.setBackground(Color.WHITE);

        JFrame distFrame = new JFrame(" NODE & EDGE VALUE RELATIONSHIP ANALYSIS");
        distFrame.setLayout(new BorderLayout(3,3));
        distFrame.getContentPane().add(betweenPathLengthDistributions, BorderLayout.CENTER);
        distFrame.setPreferredSize(new Dimension(450, 350));
        distFrame.pack();
        distFrame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final MyGraphLevelScatterPlotChart scatterPlotExample = this;
        new Thread(new Runnable() {
            @Override public void run() {
                if (e.getSource() == nodeEdgeOptionComboBoxMenu1) {
                    if (nodeEdgeOptionComboBoxMenu1.getSelectedItem().toString().contains("N.")) {
                        nodeValueOptionComboBoxMenu1.removeActionListener(scatterPlotExample);
                        nodeValueOptionComboBoxMenu1.removeAllItems();
                        nodeValueOptionComboBoxMenu1.addItem("NONE");
                        nodeValueOptionComboBoxMenu1.addItem("CONT.");
                        nodeValueOptionComboBoxMenu1.addItem("UNIQ.");
                        nodeValueOptionComboBoxMenu1.addActionListener(scatterPlotExample);
                        nodeValueOptionComboBoxMenu1.revalidate();
                        nodeValueOptionComboBoxMenu1.repaint();
                    } else {
                        edgeValueOptionComboBoxMenu1.removeActionListener(scatterPlotExample);
                        edgeValueOptionComboBoxMenu1.removeAllItems();
                        edgeValueOptionComboBoxMenu1.addItem("NONE");
                        edgeValueOptionComboBoxMenu1.addItem("CONT.");
                        edgeValueOptionComboBoxMenu1.addItem("UNIQ.");
                        edgeValueOptionComboBoxMenu1.addActionListener(scatterPlotExample);
                        edgeValueOptionComboBoxMenu1.revalidate();
                        edgeValueOptionComboBoxMenu1.repaint();
                    }
                } else if (e.getSource() == nodeValueOptionComboBoxMenu1) {
                    if (nodeValueOptionComboBoxMenu1.getSelectedItem().toString().contains("CONT.")) {
                        synchronized (value1) {
                            value1 = new ArrayList<>();
                            Collection<MyNode> nodes = MyVars.g.getVertices();
                            for (MyNode n : nodes) {
                                value1.add(n.getContribution());
                            }
                            valueNames[0] = "CONT.";
                        }
                    } else if (nodeValueOptionComboBoxMenu1.getSelectedItem().toString().contains("UNIQ.")) {
                        synchronized (value1) {
                            value1 = new ArrayList<>();
                            Collection<MyNode> nodes = MyVars.g.getVertices();
                            for (MyNode n : nodes) {
                                value1.add(n.getUniqueContribution());
                            }
                            valueNames[0] = "UNIQ.";
                        }
                    }
                } else if (e.getSource() == edgeValueOptionComboBoxMenu1) {
                    value1.clear();
                    if (edgeValueOptionComboBoxMenu1.getSelectedItem().toString().contains("CONT.")) {
                        synchronized (value1) {
                            value1 = new ArrayList<>();
                            Collection<MyEdge> edges = MyVars.g.getEdges();
                            for (MyEdge e : edges) {
                                value1.add((long) e.getContribution());
                            }
                            valueNames[0] = "CONT.";
                        }
                    } else if (edgeValueOptionComboBoxMenu1.getSelectedItem().toString().contains("UNIQ.")) {
                        synchronized (value1) {
                            value1 = new ArrayList<>();
                            Collection<MyEdge> edges = MyVars.g.getEdges();
                            for (MyEdge e : edges) {
                                value1.add((long) e.getUniqueContribution());
                            }
                            valueNames[0] = "UNIQ.";
                        }
                    }
                } else if (e.getSource() == nodeEdgeOptionComboBoxMenu2) {
                    if (nodeEdgeOptionComboBoxMenu2.getSelectedItem().toString().contains("N.")) {
                        nodeValueOptionComboBoxMenu2.removeActionListener(scatterPlotExample);
                        nodeValueOptionComboBoxMenu2.removeAllItems();
                        nodeValueOptionComboBoxMenu2.addItem("NONE");
                        nodeValueOptionComboBoxMenu2.addItem("CONT.");
                        nodeValueOptionComboBoxMenu2.addItem("UNIQ.");
                        nodeValueOptionComboBoxMenu2.addActionListener(scatterPlotExample);
                        nodeValueOptionComboBoxMenu2.revalidate();
                        nodeValueOptionComboBoxMenu2.repaint();
                    } else {
                        edgeValueOptionComboBoxMenu2.removeActionListener(scatterPlotExample);
                        edgeValueOptionComboBoxMenu2.removeAllItems();
                        edgeValueOptionComboBoxMenu2.addItem("NONE");
                        edgeValueOptionComboBoxMenu2.addItem("CONT.");
                        edgeValueOptionComboBoxMenu2.addItem("UNIQ.");
                        edgeValueOptionComboBoxMenu2.addActionListener(scatterPlotExample);
                        edgeValueOptionComboBoxMenu2.revalidate();
                        edgeValueOptionComboBoxMenu2.repaint();
                    }
                } else if (e.getSource() == nodeValueOptionComboBoxMenu2) {
                    if (nodeValueOptionComboBoxMenu2.getSelectedItem().toString().contains("CONT.")) {
                        synchronized (value2) {
                            value2 = new ArrayList<>();
                            Collection<MyNode> nodes = MyVars.g.getVertices();
                            for (MyNode n : nodes) {
                                value2.add(n.getContribution());
                            }
                            valueNames[1] = "CONT.";
                        }
                    } else if (nodeValueOptionComboBoxMenu2.getSelectedItem().toString().contains("UNIQ.")) {
                        synchronized (value2) {
                            value2 = new ArrayList<>();
                            Collection<MyNode> nodes = MyVars.g.getVertices();
                            for (MyNode n : nodes) {
                                value2.add(n.getUniqueContribution());
                            }
                            valueNames[1] = "UNIQ.";
                        }
                    }
                } else if (e.getSource() == edgeValueOptionComboBoxMenu2) {
                    if (edgeValueOptionComboBoxMenu2.getSelectedItem().toString().contains("CONT.")) {
                        synchronized (value2) {
                            value2 = new ArrayList<>();
                            Collection<MyEdge> edges = MyVars.g.getEdges();
                            for (MyEdge e : edges) {
                                value2.add((long) e.getContribution());
                            }
                            valueNames[1] = "CONT.";
                        }
                    } else if (edgeValueOptionComboBoxMenu2.getSelectedItem().toString().contains("UNIQ.")) {
                        synchronized (value2) {
                            value2 = new ArrayList<>();
                            Collection<MyEdge> edges = MyVars.g.getEdges();
                            for (MyEdge e : edges) {
                                value2.add((long) e.getUniqueContribution());
                            }
                            valueNames[1] = "UNIQ.";
                        }
                    }
                }
            }
        }).start();
    }
}