package datamining.graph.stats;

import datamining.graph.MyEdge;
import datamining.graph.MyNode;
import datamining.utils.message.MyMessageUtil;
import datamining.utils.system.MySysUtil;
import datamining.utils.system.MyVars;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.HorizontalAlignment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.util.*;

public class MyGraphLevelValueDistributionLineChart
extends JPanel {

    public static int instances = 0;
    public JComboBox chartMenu;
    private XYSeries valueSeries;

    public MyGraphLevelValueDistributionLineChart() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                setNodeValueLineChart();
            }
        });
    }

    public void decorate() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                try {
                    setNodeValueLineChart();
                } catch (Exception ex) {}
            }
        });
    }

    private TreeMap<Long, Long> createNodeValueMap() {
        TreeMap<Long, Long> valueMap = new TreeMap<>();
        Collection<MyNode> nodes = MyVars.g.getVertices();
        for (MyNode n : nodes) {
            long value = (long)n.getCurrentValue();
            if (value == 0) continue;
            if (valueMap.containsKey(value)) {
                valueMap.put(value, valueMap.get(value) + 1);
            } else {
                valueMap.put(value, 1L);
            }
        }
        return valueMap;
    }

    private TreeMap<Long, Long> createEdgeValueMap() {
        TreeMap<Long, Long> valueMap = new TreeMap<>();
        Collection<MyEdge> edges = MyVars.g.getEdges();
        for (MyEdge e : edges) {
            long value = (long)e.getCurrentValue();
            if (value == 0) continue;
            if (valueMap.containsKey(value)) {
                valueMap.put(value, valueMap.get(value) + 1);
            } else {
                valueMap.put(value, 1L);
            }
        }
        return valueMap;
    }

    private void setNodeValueLineChart() {
        removeAll();
        setLayout(new BorderLayout(3, 3));
        setBackground(Color.WHITE);

        TreeMap<Long, Long> valueMap = createNodeValueMap();
        XYSeriesCollection dataset = new XYSeriesCollection();
        valueSeries = new XYSeries("N. V.");
        for (Long value : valueMap.keySet()) {
            valueSeries.add(value, valueMap.get(value));
        }
        dataset.addSeries(valueSeries);

        JFreeChart chart = ChartFactory.createXYLineChart("", "N. V.", "", dataset);
        chart.getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
        chart.getXYPlot().setBackgroundPaint(Color.WHITE);
        chart.getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
        chart.getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);
        chart.getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
        chart.getXYPlot().getDomainAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
        chart.getXYPlot().getDomainAxis().setTickLabelFont(MyVars.tahomaPlainFont10);
        chart.getXYPlot().getRangeAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
        chart.getXYPlot().getRangeAxis().setTickLabelFont(MyVars.tahomaPlainFont10);
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
        renderer.setBaseLegendTextFont(MyVars.tahomaPlainFont10);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(350, 367));

        JLabel titleLabel = new JLabel(" N. V.");
        titleLabel.setToolTipText("CURRENT NODE VALUE DISTRIBUTION");
        titleLabel.setFont(MyVars.tahomaBoldFont11);
        titleLabel.setBackground(Color.WHITE);
        titleLabel.setForeground(Color.DARK_GRAY);

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
        titlePanel.add(titleLabel);

        JButton enlargeBtn = new JButton("+");
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

        chartMenu = new JComboBox();
        chartMenu.setFocusable(false);
        chartMenu.setFont(MyVars.tahomaPlainFont10);
        chartMenu.setBackground(Color.WHITE);
        //chartMenu.addItem("N.");
        //chartMenu.addItem("E.");
        chartMenu.addItem("N. V.");
        chartMenu.addItem("E. V.");
        chartMenu.setSelectedIndex(0);
        chartMenu.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                /**if (chartMenu.getSelectedIndex() == 0) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            setNodeBarChart();

                        }
                    });
                } else if (chartMenu.getSelectedIndex() == 1) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override public void run() {
                            if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() < 2) {;
                                if (MAXIMIZED) {
                                    MyMessageUtil.showInfoMsg(distFrame, "Select an edge value, first.");
                                    chartMenu.removeActionListener(chartMenu);
                                    chartMenu.setSelectedIndex(0);
                                    chartMenu.addActionListener(chartMenu);
                                } else {
                                    MyMessageUtil.showInfoMsg(distFrame, "Select an edge value, first.");
                                    chartMenu.removeActionListener(chartMenu);
                                    chartMenu.setSelectedIndex(0);
                                    chartMenu.addActionListener(chartMenu);
                                }
                            } else {
                                setEdgeBarChart();
                            }
                        }
                    });
                } else*/ if (chartMenu.getSelectedIndex() == 0) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override public void run() {
                            setNodeValueLineChart();
                        }
                    });
                } else if (chartMenu.getSelectedIndex() == 1) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override public void run() {
                            if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() < 2) {;
                                if (MAXIMIZED) {
                                    MyMessageUtil.showInfoMsg(distFrame, "Select an edge value, first.");
                                    chartMenu.removeActionListener(chartMenu);
                                    chartMenu.setSelectedIndex(0);
                                    chartMenu.addActionListener(chartMenu);
                                } else {
                                    MyMessageUtil.showInfoMsg(distFrame, "Select an edge value, first.");
                                    chartMenu.removeActionListener(chartMenu);
                                    chartMenu.setSelectedIndex(0);
                                    chartMenu.addActionListener(chartMenu);
                                }
                            } else {
                                setEdgeValueLineChart();
                            }
                        }
                    });
                }
            }
        });

        JPanel enlargePanel = new JPanel();
        enlargePanel.setBackground(Color.WHITE);
        enlargePanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 3));
        enlargePanel.add(chartMenu);
        enlargePanel.add(enlargeBtn);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout(3, 3));
        topPanel.setBackground(Color.WHITE);
        topPanel.add(titlePanel, BorderLayout.WEST);
        topPanel.add(enlargePanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);
        add(chartPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void setEdgeValueLineChart() {
        removeAll();
        setLayout(new BorderLayout(3, 3));
        setBackground(Color.WHITE);

        TreeMap<Long, Long> valueMap = createEdgeValueMap();
        XYSeriesCollection dataset = new XYSeriesCollection();
        valueSeries = new XYSeries("E. V.");
        for (Long value : valueMap.keySet()) {
            valueSeries.add(value, valueMap.get(value));
        }
        dataset.addSeries(valueSeries);

        JFreeChart chart = ChartFactory.createXYLineChart("", "E. V.", "", dataset);
        chart.getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
        chart.getXYPlot().setBackgroundPaint(Color.WHITE);
        chart.getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
        chart.getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);
        chart.getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
        chart.getXYPlot().getDomainAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
        chart.getXYPlot().getDomainAxis().setTickLabelFont(MyVars.tahomaPlainFont10);
        chart.getXYPlot().getRangeAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
        chart.getXYPlot().getRangeAxis().setTickLabelFont(MyVars.tahomaPlainFont10);
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
        renderer.setBaseLegendTextFont(MyVars.tahomaPlainFont10);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(350, 367));

        JLabel titleLabel = new JLabel(" E. V.");
        titleLabel.setToolTipText("CURRENT EDGE VALUE DISTRIBUTION");
        titleLabel.setFont(MyVars.tahomaBoldFont11);
        titleLabel.setBackground(Color.WHITE);
        titleLabel.setForeground(Color.DARK_GRAY);

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
        titlePanel.add(titleLabel);

        JButton enlargeBtn = new JButton("+");
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

        chartMenu = new JComboBox();
        chartMenu.setFocusable(false);
        chartMenu.setFont(MyVars.tahomaPlainFont10);
        chartMenu.setBackground(Color.WHITE);
        //chartMenu.addItem("N.");
        //chartMenu.addItem("E.");
        chartMenu.addItem("N. V.");
        chartMenu.addItem("E. V.");
        chartMenu.setSelectedIndex(1);
        chartMenu.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                /**if (chartMenu.getSelectedIndex() == 0) {
                 SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                setNodeBarChart();

                }
                });
                 } else if (chartMenu.getSelectedIndex() == 1) {
                 SwingUtilities.invokeLater(new Runnable() {
                @Override public void run() {
                if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() < 2) {;
                if (MAXIMIZED) {
                MyMessageUtil.showInfoMsg(distFrame, "Select an edge value, first.");
                chartMenu.removeActionListener(chartMenu);
                chartMenu.setSelectedIndex(0);
                chartMenu.addActionListener(chartMenu);
                } else {
                MyMessageUtil.showInfoMsg(distFrame, "Select an edge value, first.");
                chartMenu.removeActionListener(chartMenu);
                chartMenu.setSelectedIndex(0);
                chartMenu.addActionListener(chartMenu);
                }
                } else {
                setEdgeBarChart();
                }
                }
                });
                 } else*/ if (chartMenu.getSelectedIndex() == 0) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override public void run() {
                            setNodeValueLineChart();
                        }
                    });
                } else if (chartMenu.getSelectedIndex() == 1) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override public void run() {
                            if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() < 2) {;
                                if (MAXIMIZED) {
                                    MyMessageUtil.showInfoMsg(distFrame, "Select an edge value, first.");
                                    chartMenu.removeActionListener(chartMenu);
                                    chartMenu.setSelectedIndex(0);
                                    chartMenu.addActionListener(chartMenu);
                                } else {
                                    MyMessageUtil.showInfoMsg(distFrame, "Select an edge value, first.");
                                    chartMenu.removeActionListener(chartMenu);
                                    chartMenu.setSelectedIndex(0);
                                    chartMenu.addActionListener(chartMenu);
                                }
                            } else {
                                setEdgeValueLineChart();
                            }
                        }
                    });
                }
            }
        });

        JPanel enlargePanel = new JPanel();
        enlargePanel.setBackground(Color.WHITE);
        enlargePanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 3));
        enlargePanel.add(chartMenu);
        enlargePanel.add(enlargeBtn);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout(3, 3));
        topPanel.setBackground(Color.WHITE);
        topPanel.add(titlePanel, BorderLayout.WEST);
        topPanel.add(enlargePanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);
        add(chartPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    JFrame distFrame;
    public void enlarge() {
        MAXIMIZED = true;
        distFrame = new JFrame(" NODE & EDGE VALUE DISTRIBUTION");
        distFrame.setLayout(new BorderLayout(3,3));
        distFrame.getContentPane().add(new MyGraphLevelValueDistributionLineChart(), BorderLayout.CENTER);
        distFrame.setPreferredSize(new Dimension(450, 350));
        distFrame.pack();
        distFrame.setVisible(true);
        distFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        distFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
                MAXIMIZED = false;
            }
        });
    }

    private Random rand = new Random();
    private static int BAR_LIMIT = 200;
    private ArrayList<Color> colors;
    private static boolean MAXIMIZED;

    private void setNodeBarChart() {
        removeAll();
        setLayout(new BorderLayout(3, 3));
        setBackground(Color.WHITE);

        this.colors = new ArrayList<>();
        LinkedHashMap<String, Long> valueMap = new LinkedHashMap<>();
        Collection<MyNode> nodes = MyVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0) continue;
            valueMap.put((n.getName().contains("x") ? MySysUtil.decodeVariable(n.getName()) : MySysUtil.getDecodedNodeName(n.getName())), (long)n.getCurrentValue());
            final float hue = rand.nextFloat();
            final float saturation = 0.9f;
            final float luminance = 1.0f;
            Color randomColor = Color.getHSBColor(hue, saturation, luminance);
            colors.add(randomColor);
        }
        valueMap = MySysUtil.sortMapByLongValue(valueMap);

        CategoryDataset dataset = new DefaultCategoryDataset();
        JFreeChart chart = ChartFactory.createBarChart("", "", "", dataset);
        BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();

        if (MAXIMIZED) {
            int limitCount = 0;
            for (String label : valueMap.keySet()) {
                if (MAXIMIZED && limitCount == BAR_LIMIT) break;
                ((DefaultCategoryDataset) dataset).addValue(valueMap.get(label), label, label);
                limitCount++;
            }
            chart.getCategoryPlot().getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.DOWN_90);
            chart.removeLegend();
        } else {
            int limitCount = 0;
            for (String label : valueMap.keySet()) {
                if (!MAXIMIZED && limitCount == 7) break;
                else if (MAXIMIZED && limitCount == BAR_LIMIT) break;
                ((DefaultCategoryDataset) dataset).addValue(valueMap.get(label), label, "");
                limitCount++;
            }
        }
        renderer.setBaseLegendTextFont(MyVars.tahomaPlainFont7);

        chart.getCategoryPlot().setBackgroundPaint(Color.WHITE);
        chart.getCategoryPlot().setDomainGridlinePaint(Color.DARK_GRAY);
        chart.getCategoryPlot().setRangeGridlinePaint(Color.DARK_GRAY);
        chart.getCategoryPlot().getDomainAxis().setLabelFont(MyVars.tahomaPlainFont7);
        chart.getCategoryPlot().getDomainAxis().setTickLabelFont(MyVars.tahomaPlainFont7);
        chart.getCategoryPlot().getRangeAxis().setTickLabelFont(MyVars.tahomaPlainFont7);
        renderer.setBarPainter(new StandardBarPainter());
        for (int i = 0; i < dataset.getColumnCount(); i++) {
            renderer.setSeriesPaint(i, colors.get(i));
        }
        renderer.setMaximumBarWidth(0.09);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(350, 367));

        JLabel titleLabel = new JLabel(" N. V.");
        titleLabel.setToolTipText("NODE VALUE DISTRIBUTION");
        titleLabel.setFont(MyVars.tahomaBoldFont11);
        titleLabel.setBackground(Color.WHITE);
        titleLabel.setForeground(Color.DARK_GRAY);

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
        titlePanel.add(titleLabel);

        JButton enlargeBtn = new JButton("+");
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

        chartMenu = new JComboBox();
        chartMenu.setFocusable(false);
        chartMenu.setFont(MyVars.tahomaPlainFont10);
        chartMenu.setBackground(Color.WHITE);
        //chartMenu.addItem("N.");
        //chartMenu.addItem("E.");
        chartMenu.addItem("N. V.");
        chartMenu.addItem("E. V.");
        chartMenu.setSelectedIndex(0);
        chartMenu.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                /**if (chartMenu.getSelectedIndex() == 0) {
                 SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                setNodeBarChart();

                }
                });
                 } else if (chartMenu.getSelectedIndex() == 1) {
                 SwingUtilities.invokeLater(new Runnable() {
                @Override public void run() {
                if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() < 2) {;
                if (MAXIMIZED) {
                MyMessageUtil.showInfoMsg(distFrame, "Select an edge value, first.");
                chartMenu.removeActionListener(chartMenu);
                chartMenu.setSelectedIndex(0);
                chartMenu.addActionListener(chartMenu);
                } else {
                MyMessageUtil.showInfoMsg(distFrame, "Select an edge value, first.");
                chartMenu.removeActionListener(chartMenu);
                chartMenu.setSelectedIndex(0);
                chartMenu.addActionListener(chartMenu);
                }
                } else {
                setEdgeBarChart();
                }
                }
                });
                 } else*/ if (chartMenu.getSelectedIndex() == 0) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override public void run() {
                            setNodeValueLineChart();
                        }
                    });
                } else if (chartMenu.getSelectedIndex() == 1) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override public void run() {
                            if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() < 2) {;
                                if (MAXIMIZED) {
                                    MyMessageUtil.showInfoMsg(distFrame, "Select an edge value, first.");
                                    chartMenu.removeActionListener(chartMenu);
                                    chartMenu.setSelectedIndex(0);
                                    chartMenu.addActionListener(chartMenu);
                                } else {
                                    MyMessageUtil.showInfoMsg(distFrame, "Select an edge value, first.");
                                    chartMenu.removeActionListener(chartMenu);
                                    chartMenu.setSelectedIndex(0);
                                    chartMenu.addActionListener(chartMenu);
                                }
                            } else {
                                setEdgeValueLineChart();
                            }
                        }
                    });
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 3));
        buttonPanel.add(chartMenu);
        buttonPanel.add(enlargeBtn);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout(3, 3));
        topPanel.setBackground(Color.WHITE);
        topPanel.add(titlePanel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);
        add(chartPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void setEdgeBarChart() {
        removeAll();
        setLayout(new BorderLayout(3, 3));
        setBackground(Color.WHITE);

        this.colors = new ArrayList<>();
        LinkedHashMap<String, Long> valueMap = new LinkedHashMap<>();
        Collection<MyEdge> edges = MyVars.g.getEdges();
        for (MyEdge e : edges) {
            if (e.getCurrentValue() == 0) continue;
            String ps = (e.getSource().getName().contains("x") ? MySysUtil.decodeVariable(e.getSource().getName()) : MySysUtil.getDecodedNodeName(e.getSource().getName()));
            String edge = ps + "-" + (MySysUtil.decodeNodeName(e.getDest().getName()));
            valueMap.put(edge, (long)e.getCurrentValue());
            final float hue = rand.nextFloat();
            final float saturation = 0.9f;
            final float luminance = 1.0f;
            Color randomColor = Color.getHSBColor(hue, saturation, luminance);
            colors.add(randomColor);
        }
        valueMap = MySysUtil.sortMapByLongValue(valueMap);

        CategoryDataset dataset = new DefaultCategoryDataset();
        JFreeChart chart = ChartFactory.createBarChart("", "", "", dataset);
        BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();

        if (MAXIMIZED) {
            int limitCount = 0;
            for (String label : valueMap.keySet()) {
                if (MAXIMIZED && limitCount == BAR_LIMIT) break;
                ((DefaultCategoryDataset) dataset).addValue(valueMap.get(label), label, label);
                limitCount++;
            }
            chart.getCategoryPlot().getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.DOWN_90);
            chart.removeLegend();
        } else {
            int limitCount = 0;
            for (String label : valueMap.keySet()) {
                if (!MAXIMIZED && limitCount == 6) break;
                else if (MAXIMIZED && limitCount == BAR_LIMIT) break;
                ((DefaultCategoryDataset) dataset).addValue(valueMap.get(label), label, "");
                limitCount++;
            }
            renderer.setBaseLegendTextFont(MyVars.tahomaPlainFont7);
        }

        chart.getCategoryPlot().setBackgroundPaint(Color.WHITE);
        chart.getCategoryPlot().setDomainGridlinePaint(Color.DARK_GRAY);
        chart.getCategoryPlot().setRangeGridlinePaint(Color.DARK_GRAY);
        chart.getCategoryPlot().getDomainAxis().setLabelFont(MyVars.tahomaPlainFont7);
        chart.getCategoryPlot().getDomainAxis().setTickLabelFont(MyVars.tahomaPlainFont7);
        chart.getCategoryPlot().getRangeAxis().setTickLabelFont(MyVars.tahomaPlainFont7);
        renderer.setBarPainter(new StandardBarPainter());
        for (int i = 0; i < dataset.getColumnCount(); i++) {
            renderer.setSeriesPaint(i, colors.get(i));
        }

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(350, 367));

        JLabel titleLabel = new JLabel(" E. V.");
        titleLabel.setToolTipText("EDGE VALUE DISTRIBUTION");
        titleLabel.setFont(MyVars.tahomaBoldFont11);
        titleLabel.setBackground(Color.WHITE);
        titleLabel.setForeground(Color.DARK_GRAY);

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
        titlePanel.add(titleLabel);

        JButton enlargeBtn = new JButton("+");
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

        chartMenu = new JComboBox();
        chartMenu.setFocusable(false);
        chartMenu.setFont(MyVars.tahomaPlainFont10);
        chartMenu.setBackground(Color.WHITE);
        //chartMenu.addItem("N.");
        //chartMenu.addItem("E.");
        chartMenu.addItem("N. V.");
        chartMenu.addItem("E. V.");
        chartMenu.setSelectedIndex(1);
        chartMenu.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                /**if (chartMenu.getSelectedIndex() == 0) {
                 SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                setNodeBarChart();

                }
                });
                 } else if (chartMenu.getSelectedIndex() == 1) {
                 SwingUtilities.invokeLater(new Runnable() {
                @Override public void run() {
                if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() < 2) {;
                if (MAXIMIZED) {
                MyMessageUtil.showInfoMsg(distFrame, "Select an edge value, first.");
                chartMenu.removeActionListener(chartMenu);
                chartMenu.setSelectedIndex(0);
                chartMenu.addActionListener(chartMenu);
                } else {
                MyMessageUtil.showInfoMsg(distFrame, "Select an edge value, first.");
                chartMenu.removeActionListener(chartMenu);
                chartMenu.setSelectedIndex(0);
                chartMenu.addActionListener(chartMenu);
                }
                } else {
                setEdgeBarChart();
                }
                }
                });
                 } else*/ if (chartMenu.getSelectedIndex() == 0) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override public void run() {
                            setNodeValueLineChart();
                        }
                    });
                } else if (chartMenu.getSelectedIndex() == 1) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override public void run() {
                            if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() < 2) {;
                                if (MAXIMIZED) {
                                    MyMessageUtil.showInfoMsg(distFrame, "Select an edge value, first.");
                                    chartMenu.removeActionListener(chartMenu);
                                    chartMenu.setSelectedIndex(0);
                                    chartMenu.addActionListener(chartMenu);
                                } else {
                                    MyMessageUtil.showInfoMsg(distFrame, "Select an edge value, first.");
                                    chartMenu.removeActionListener(chartMenu);
                                    chartMenu.setSelectedIndex(0);
                                    chartMenu.addActionListener(chartMenu);
                                }
                            } else {
                                setEdgeValueLineChart();
                            }
                        }
                    });
                }
            }
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 3));
        buttonPanel.add(chartMenu);
        buttonPanel.add(enlargeBtn);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout(3, 3));
        topPanel.setBackground(Color.WHITE);
        topPanel.add(titlePanel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);
        add(chartPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }


}
