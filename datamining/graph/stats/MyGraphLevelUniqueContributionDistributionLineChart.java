package datamining.graph.stats;

import datamining.graph.MyEdge;
import datamining.graph.MyNode;
import datamining.utils.MyMathUtil;
import datamining.utils.system.MySysUtil;
import datamining.utils.system.MyVars;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Random;

public class MyGraphLevelUniqueContributionDistributionLineChart
extends JPanel {

    public static int instances = 0;
    private Random rand = new Random();
    private static int BAR_LIMIT = 200;
    private ArrayList<Color> colors;
    private static boolean MAXIMIZED;
    private JComboBox chartMenu;

    public MyGraphLevelUniqueContributionDistributionLineChart() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                setNodeUniqueContributionLineChart();
            }
        });
    }

    private void setNodeUniqueContributionBarChart() {
        removeAll();
        setLayout(new BorderLayout(3, 3));
        setBackground(Color.WHITE);

        colors = new ArrayList<>();
        LinkedHashMap<String, Long> valueMap = new LinkedHashMap<>();
        Collection<MyNode> nodes = MyVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0) continue;
            String nodeName = (n.getName().contains("x") ? MySysUtil.decodeVariable(n.getName()) : MySysUtil.getDecodedNodeName(n.getName()));
            valueMap.put(nodeName, n.getUniqueContribution());
            final float hue = rand.nextFloat();
            final float saturation = 0.9f;
            final float luminance = 1.0f;
            Color randomColor = Color.getHSBColor(hue, saturation, luminance);
            colors.add(randomColor);
        }
        if (valueMap.size() == 0) {
            JLabel titleLabel = new JLabel(" U. C.");
            titleLabel.setToolTipText("UNIQUE CONTRIBUTION DISTRIBUTION");
            titleLabel.setFont(MyVars.tahomaBoldFont11);
            titleLabel.setBackground(Color.WHITE);
            titleLabel.setForeground(Color.DARK_GRAY);

            JPanel titlePanel = new JPanel();
            titlePanel.setBackground(Color.WHITE);
            titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
            titlePanel.add(titleLabel);

            JPanel topPanel = new JPanel();
            topPanel.setLayout(new BorderLayout(3, 8));
            topPanel.setBackground(Color.WHITE);
            topPanel.add(titlePanel, BorderLayout.WEST);

            JLabel msg = new JLabel("N/A");
            msg.setFont(MyVars.tahomaPlainFont12);
            msg.setBackground(Color.WHITE);
            msg.setHorizontalAlignment(JLabel.CENTER);
            add(topPanel, BorderLayout.NORTH);
            add(msg, BorderLayout.CENTER);
            return;
        }

        valueMap = MySysUtil.sortMapByLongValue(valueMap);
        CategoryDataset dataset = new DefaultCategoryDataset();
        if (MAXIMIZED) {
            int i=0;
            for (String label : valueMap.keySet()) {
                if (i == BAR_LIMIT) break;
                ((DefaultCategoryDataset) dataset).addValue(valueMap.get(label), label, "");
                i++;
            }
        } else {
            int i=0;
            for (String label : valueMap.keySet()) {
                if (i == 7) break;
                ((DefaultCategoryDataset) dataset).addValue(valueMap.get(label), label, "");
                i++;
            }
        }

        // Create a bar chart with the dataset
        JFreeChart chart = ChartFactory.createBarChart("", "", "", dataset);
        chart.getCategoryPlot().setBackgroundPaint(Color.WHITE);
        chart.getCategoryPlot().setDomainGridlinePaint(Color.DARK_GRAY);
        chart.getCategoryPlot().setRangeGridlinePaint(Color.DARK_GRAY);
        chart.getCategoryPlot().getDomainAxis().setTickLabelFont(MyVars.tahomaPlainFont7);
        chart.getCategoryPlot().getRangeAxis().setTickLabelFont(MyVars.tahomaPlainFont7);
        BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();
        renderer.setBarPainter(new StandardBarPainter());
        // Set the colors for each data item in the series
        for (int i = 0; i <= dataset.getColumnCount(); i++) {
            renderer.setSeriesPaint(i, colors.get(i));
        }

        renderer.setMaximumBarWidth(0.09);
        renderer.setBaseLegendTextFont(MyVars.tahomaPlainFont7);
        // Create a ChartPanel and display it
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(350, 367));

        JLabel titleLabel = new JLabel(" U. C.");
        titleLabel.setToolTipText("UNIQUE CONTRIBUTION DISTRIBUTION");
        titleLabel.setFont(MyVars.tahomaBoldFont11);
        titleLabel.setBackground(Color.WHITE);
        titleLabel.setForeground(Color.DARK_GRAY);

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
        titlePanel.add(titleLabel);

        chartMenu = new JComboBox();
        chartMenu.setFocusable(false);
        chartMenu.setFont(MyVars.tahomaPlainFont10);
        chartMenu.setBackground(Color.WHITE);
        chartMenu.addItem("N. U. C. B.");
        chartMenu.addItem("N. U. C.");
        chartMenu.addItem("E. U. C. B.");
        chartMenu.addItem("E. U. C. ");
        chartMenu.setSelectedIndex(0);
        chartMenu.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                if (chartMenu.getSelectedIndex() == 0) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override public void run() {
                            setNodeUniqueContributionBarChart();

                        }
                    });
                } else if (chartMenu.getSelectedIndex() == 1) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override public void run() {
                            setNodeUniqueContributionLineChart();
                        }
                    });
                } else if (chartMenu.getSelectedIndex() == 2) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override public void run() {
                            setEdgeUniqueContributionBarChart();
                        }
                    });
                } else if (chartMenu.getSelectedIndex() == 3) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override public void run() {
                            setEdgeUniqueContributionLineChart();
                        }
                    });
                }
            }
        });

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

    private void setEdgeUniqueContributionBarChart() {
        removeAll();
        setLayout(new BorderLayout(3, 3));
        setBackground(Color.WHITE);

        colors = new ArrayList<>();
        LinkedHashMap<String, Long> valueMap = new LinkedHashMap<>();
        Collection<MyEdge> edges = MyVars.g.getEdges();
        for (MyEdge e : edges) {
            if (e.getCurrentValue() == 0) continue;
            String p = (e.getSource().getName().contains("x") ? MySysUtil.decodeVariable(e.getSource().getName()) : MySysUtil.getDecodedNodeName(e.getSource().getName()));
            String edge = p + "-" + MySysUtil.getDecodedNodeName(e.getDest().getName());
            valueMap.put(edge, e.getUniqueContribution());
            final float hue = rand.nextFloat();
            final float saturation = 0.9f;
            final float luminance = 1.0f;
            Color randomColor = Color.getHSBColor(hue, saturation, luminance);
            colors.add(randomColor);
        }
        if (valueMap.size() == 0) {
            JLabel titleLabel = new JLabel(" U. C.");
            titleLabel.setToolTipText("UNIQUE CONTRIBUTION DISTRIBUTION");
            titleLabel.setFont(MyVars.tahomaBoldFont11);
            titleLabel.setBackground(Color.WHITE);
            titleLabel.setForeground(Color.DARK_GRAY);

            JPanel titlePanel = new JPanel();
            titlePanel.setBackground(Color.WHITE);
            titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
            titlePanel.add(titleLabel);

            JPanel topPanel = new JPanel();
            topPanel.setLayout(new BorderLayout(3, 8));
            topPanel.setBackground(Color.WHITE);
            topPanel.add(titlePanel, BorderLayout.WEST);

            JLabel msg = new JLabel("N/A");
            msg.setFont(MyVars.tahomaPlainFont12);
            msg.setBackground(Color.WHITE);
            msg.setHorizontalAlignment(JLabel.CENTER);
            add(topPanel, BorderLayout.NORTH);
            add(msg, BorderLayout.CENTER);
            return;
        }

        valueMap = MySysUtil.sortMapByLongValue(valueMap);
        CategoryDataset dataset = new DefaultCategoryDataset();
        if (MAXIMIZED) {
            int i=0;
            for (String label : valueMap.keySet()) {
                if (i == BAR_LIMIT) break;
                ((DefaultCategoryDataset) dataset).addValue(valueMap.get(label), label, "");
                i++;
            }
        } else {
            int i=0;
            for (String label : valueMap.keySet()) {
                if (i == 7) break;
                ((DefaultCategoryDataset) dataset).addValue(valueMap.get(label), label, "");
                i++;
            }
        }

        // Create a bar chart with the dataset
        JFreeChart chart = ChartFactory.createBarChart("", "", "", dataset);
        chart.getCategoryPlot().setBackgroundPaint(Color.WHITE);
        chart.getCategoryPlot().setDomainGridlinePaint(Color.DARK_GRAY);
        chart.getCategoryPlot().setRangeGridlinePaint(Color.DARK_GRAY);
        chart.getCategoryPlot().getDomainAxis().setTickLabelFont(MyVars.tahomaPlainFont7);
        chart.getCategoryPlot().getRangeAxis().setTickLabelFont(MyVars.tahomaPlainFont7);
        BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();
        renderer.setBarPainter(new StandardBarPainter());
        // Set the colors for each data item in the series
        for (int i = 0; i <= dataset.getColumnCount(); i++) {
            renderer.setSeriesPaint(i, colors.get(i));
        }

        renderer.setMaximumBarWidth(0.09);
        renderer.setBaseLegendTextFont(MyVars.tahomaPlainFont7);
        // Create a ChartPanel and display it
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(350, 367));

        JLabel titleLabel = new JLabel(" U. C.");
        titleLabel.setToolTipText("UNIQUE CONTRIBUTION DISTRIBUTION");
        titleLabel.setFont(MyVars.tahomaBoldFont11);
        titleLabel.setBackground(Color.WHITE);
        titleLabel.setForeground(Color.DARK_GRAY);

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
        titlePanel.add(titleLabel);

        chartMenu = new JComboBox();
        chartMenu.setFocusable(false);
        chartMenu.setFont(MyVars.tahomaPlainFont10);
        chartMenu.setBackground(Color.WHITE);
        chartMenu.addItem("N. U. C. B.");
        chartMenu.addItem("N. U. C.");
        chartMenu.addItem("E. U. C. B.");
        chartMenu.addItem("E. U. C. ");
        chartMenu.setSelectedIndex(2);
        chartMenu.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                if (chartMenu.getSelectedIndex() == 0) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override public void run() {
                            setNodeUniqueContributionBarChart();

                        }
                    });
                } else if (chartMenu.getSelectedIndex() == 1) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override public void run() {
                            setNodeUniqueContributionLineChart();
                        }
                    });
                } else if (chartMenu.getSelectedIndex() == 2) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override public void run() {
                            setEdgeUniqueContributionBarChart();
                        }
                    });
                } else if (chartMenu.getSelectedIndex() == 3) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override public void run() {
                            setEdgeUniqueContributionLineChart();
                        }
                    });
                }
            }
        });

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

    public void setNodeUniqueContributionLineChart() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                try {
                    removeAll();
                    setLayout(new BorderLayout(3, 3));
                    setBackground(Color.WHITE);

                    ChartPanel chartPanel = new ChartPanel(setNodeChart());
                    chartPanel.setPreferredSize(new Dimension(350, 367));
                    chartPanel.getChart().getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
                    chartPanel.getChart().getXYPlot().setBackgroundPaint(Color.WHITE);
                    chartPanel.getChart().getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
                    chartPanel.getChart().getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);
                    chartPanel.getChart().getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
                    chartPanel.getChart().getXYPlot().getDomainAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
                    chartPanel.getChart().getXYPlot().getDomainAxis().setTickLabelFont(MyVars.tahomaPlainFont10);
                    chartPanel.getChart().getXYPlot().getRangeAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
                    chartPanel.getChart().getXYPlot().getRangeAxis().setTickLabelFont(MyVars.tahomaPlainFont10);
                    chartPanel.getChart().getXYPlot().getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
                    chartPanel.getChart().setBackgroundPaint(Color.WHITE);

                    XYPlot plot = (XYPlot) chartPanel.getChart().getPlot();
                    XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
                    renderer.setSeriesPaint(0, Color.BLACK);
                    renderer.setSeriesStroke(0, new BasicStroke(1.6f));
                    renderer.setSeriesShapesVisible(0, true);
                    renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                    renderer.setSeriesFillPaint(0, Color.WHITE);
                    renderer.setUseFillPaint(true);

                    JLabel titleLabel = new JLabel(" U. C.");
                    titleLabel.setToolTipText("UNIQUE CONTRIBUTION VALUE DISTRIBUTION");
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
                    chartMenu.setFont(MyVars.tahomaPlainFont10);
                    chartMenu.setBackground(Color.WHITE);
                    chartMenu.addItem("N. U. C. B.");
                    chartMenu.addItem("N. U. C.");
                    chartMenu.addItem("E. U. C. B.");
                    chartMenu.addItem("E. U. C. ");
                    chartMenu.setSelectedIndex(1);
                    chartMenu.addActionListener(new ActionListener() {
                        @Override public void actionPerformed(ActionEvent e) {
                            if (chartMenu.getSelectedIndex() == 0) {
                                SwingUtilities.invokeLater(new Runnable() {
                                    @Override public void run() {
                                        setNodeUniqueContributionBarChart();

                                    }
                                });
                            } else if (chartMenu.getSelectedIndex() == 1) {
                                SwingUtilities.invokeLater(new Runnable() {
                                    @Override public void run() {
                                        setNodeUniqueContributionLineChart();
                                    }
                                });
                            } else if (chartMenu.getSelectedIndex() == 2) {
                                SwingUtilities.invokeLater(new Runnable() {
                                    @Override public void run() {
                                        setEdgeUniqueContributionBarChart();
                                    }
                                });
                            } else if (chartMenu.getSelectedIndex() == 3) {
                                SwingUtilities.invokeLater(new Runnable() {
                                    @Override public void run() {
                                        setEdgeUniqueContributionLineChart();
                                    }
                                });
                            }
                        }
                    });

                    JPanel menuPanel = new JPanel();
                    menuPanel.setBackground(Color.WHITE);
                    menuPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 3));
                    menuPanel.add(chartMenu);
                    menuPanel.add(enlargeBtn);

                    JPanel topPanel = new JPanel();
                    topPanel.setLayout(new BorderLayout(3, 3));
                    topPanel.setBackground(Color.WHITE);
                    topPanel.add(titlePanel, BorderLayout.WEST);
                    topPanel.add(menuPanel, BorderLayout.EAST);
                    add(topPanel, BorderLayout.NORTH);
                    add(chartPanel, BorderLayout.CENTER);
                    revalidate();
                    repaint();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private JFreeChart setNodeChart() {
        double total = 0;
        XYSeries uniqueContributionSeries = new XYSeries("U. C.");
        LinkedHashMap<Long, Long> uniqueContributionMap = new LinkedHashMap<>();
        Collection<MyNode> nodes = MyVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0) continue;
            total += n.getUniqueContribution();
            if (uniqueContributionMap.containsKey(n.getUniqueContribution())) {
                uniqueContributionMap.put(n.getUniqueContribution(), uniqueContributionMap.get(n.getUniqueContribution())+1);
            } else {
                uniqueContributionMap.put(n.getUniqueContribution(), 1L);
            }
        }

        for (Long uniqueContribution : uniqueContributionMap.keySet()) {
            uniqueContributionSeries.add(uniqueContribution, uniqueContributionMap.get(uniqueContribution));
        }
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(uniqueContributionSeries);

        double avgUniqCont = total/nodes.size();
        String plotTitle = "";
        String xaxis = "UNIQUE CONTRIBUTION[AVG.: " + MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(avgUniqCont)) + "]";
        String yaxis = "";
        PlotOrientation orientation = PlotOrientation.VERTICAL;
        boolean show = false;
        boolean toolTips = true;
        boolean urls = false;
        return ChartFactory.createXYLineChart("", "DEPTH", "", dataset);
    }

    public void setEdgeUniqueContributionLineChart() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                try {
                    removeAll();
                    setLayout(new BorderLayout(3, 3));
                    setBackground(Color.WHITE);

                    ChartPanel chartPanel = new ChartPanel(setEdgeChart());
                    chartPanel.setPreferredSize(new Dimension(350, 367));
                    chartPanel.getChart().getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
                    chartPanel.getChart().getXYPlot().setBackgroundPaint(Color.WHITE);
                    chartPanel.getChart().getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
                    chartPanel.getChart().getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);
                    chartPanel.getChart().getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
                    chartPanel.getChart().getXYPlot().getDomainAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
                    chartPanel.getChart().getXYPlot().getDomainAxis().setTickLabelFont(MyVars.tahomaPlainFont10);
                    chartPanel.getChart().getXYPlot().getRangeAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
                    chartPanel.getChart().getXYPlot().getRangeAxis().setTickLabelFont(MyVars.tahomaPlainFont10);
                    chartPanel.getChart().getXYPlot().getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
                    chartPanel.getChart().setBackgroundPaint(Color.WHITE);

                    XYPlot plot = (XYPlot) chartPanel.getChart().getPlot();
                    XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
                    renderer.setSeriesPaint(0, Color.BLACK);
                    renderer.setSeriesStroke(0, new BasicStroke(1.6f));
                    renderer.setSeriesShapesVisible(0, true);
                    renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                    renderer.setSeriesFillPaint(0, Color.WHITE);
                    renderer.setUseFillPaint(true);

                    JLabel titleLabel = new JLabel(" U. C.");
                    titleLabel.setToolTipText("UNIQUE CONTRIBUTION VALUE DISTRIBUTION");
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
                    chartMenu.setFont(MyVars.tahomaPlainFont10);
                    chartMenu.setBackground(Color.WHITE);
                    chartMenu.addItem("N. U. C. B.");
                    chartMenu.addItem("N. U. C.");
                    chartMenu.addItem("E. U. C. B.");
                    chartMenu.addItem("E. U. C. ");
                    chartMenu.setSelectedIndex(3);
                    chartMenu.addActionListener(new ActionListener() {
                        @Override public void actionPerformed(ActionEvent e) {
                            if (chartMenu.getSelectedIndex() == 0) {
                                SwingUtilities.invokeLater(new Runnable() {
                                    @Override public void run() {
                                        setNodeUniqueContributionBarChart();

                                    }
                                });
                            } else if (chartMenu.getSelectedIndex() == 1) {
                                SwingUtilities.invokeLater(new Runnable() {
                                    @Override public void run() {
                                        setNodeUniqueContributionLineChart();
                                    }
                                });
                            } else if (chartMenu.getSelectedIndex() == 2) {
                                SwingUtilities.invokeLater(new Runnable() {
                                    @Override public void run() {
                                        setEdgeUniqueContributionBarChart();
                                    }
                                });
                            } else if (chartMenu.getSelectedIndex() == 3) {
                                SwingUtilities.invokeLater(new Runnable() {
                                    @Override public void run() {
                                        setEdgeUniqueContributionLineChart();
                                    }
                                });
                            }
                        }
                    });

                    JPanel menuPanel = new JPanel();
                    menuPanel.setBackground(Color.WHITE);
                    menuPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 3));
                    menuPanel.add(chartMenu);
                    menuPanel.add(enlargeBtn);

                    JPanel topPanel = new JPanel();
                    topPanel.setLayout(new BorderLayout(3, 3));
                    topPanel.setBackground(Color.WHITE);
                    topPanel.add(titlePanel, BorderLayout.WEST);
                    topPanel.add(menuPanel, BorderLayout.EAST);
                    add(topPanel, BorderLayout.NORTH);
                    add(chartPanel, BorderLayout.CENTER);
                    revalidate();
                    repaint();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private JFreeChart setEdgeChart() {
        double total = 0;
        XYSeries uniqueContributionSeries = new XYSeries("U. C.");
        LinkedHashMap<Long, Long> uniqueContributionMap = new LinkedHashMap<>();
        Collection<MyEdge> edges = MyVars.g.getEdges();
        for (MyEdge e : edges) {
            if (e.getCurrentValue() == 0) continue;
            total += e.getUniqueContribution();
            if (uniqueContributionMap.containsKey(e.getUniqueContribution())) {
                uniqueContributionMap.put((long)e.getUniqueContribution(), uniqueContributionMap.get(e.getUniqueContribution())+1);
            } else {
                uniqueContributionMap.put((long)e.getUniqueContribution(), 1L);
            }
        }

        for (Long uniqueContribution : uniqueContributionMap.keySet()) {
            uniqueContributionSeries.add(uniqueContribution, uniqueContributionMap.get(uniqueContribution));
        }
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(uniqueContributionSeries);

        double avgUniqCont = total/edges.size();
        String plotTitle = "";
        String xaxis = "UNIQUE CONTRIBUTION[AVG.: " + MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(avgUniqCont)) + "]";
        String yaxis = "";
        PlotOrientation orientation = PlotOrientation.VERTICAL;
        boolean show = false;
        boolean toolTips = true;
        boolean urls = false;
        return ChartFactory.createXYLineChart("", "UNIQUE CONTRIBUTION", "", dataset);
    }

    public void enlarge() {
        MAXIMIZED = true;
        JFrame distFrame = new JFrame(" UNIQUE CONTRIBUTION DISTRIBUTION");
        distFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        distFrame.setLayout(new BorderLayout(3,3));
        distFrame.getContentPane().add(new MyGraphLevelUniqueContributionDistributionLineChart(), BorderLayout.CENTER);
        distFrame.setPreferredSize(new Dimension(450, 350));
        distFrame.pack();
        distFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                MAXIMIZED = false;
            }
        });
        distFrame.setVisible(true);
    }
}
