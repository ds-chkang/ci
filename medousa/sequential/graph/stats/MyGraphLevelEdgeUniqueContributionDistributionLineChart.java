package medousa.sequential.graph.stats;

import medousa.sequential.graph.MyClusteringConfig;
import medousa.sequential.graph.MyEdge;
import medousa.MyProgressBar;
import medousa.sequential.utils.MyMathUtil;
import medousa.sequential.utils.MySequentialGraphSysUtil;
import medousa.sequential.utils.MySequentialGraphVars;
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
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Random;

public class MyGraphLevelEdgeUniqueContributionDistributionLineChart
extends JPanel {

    public static int instances = 0;
    private Random rand = new Random();
    private static int BAR_LIMIT = 150;
    private ArrayList<Color> colors;
    private static boolean MAXIMIZED;
    private JComboBox chartMenu;

    public MyGraphLevelEdgeUniqueContributionDistributionLineChart() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                setEdgeUniqueContributionLineChart();
            }
        });
    }


    private void setEdgeUniqueContributionBarChart() {
        removeAll();
        setLayout(new BorderLayout(3, 3));
        setBackground(Color.WHITE);

        colors = new ArrayList<>();
        LinkedHashMap<String, Long> valueMap = new LinkedHashMap<>();
        Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
        for (MyEdge e : edges) {
            if (MySequentialGraphVars.getSequentialGraphViewer().isClustered) {
                if (e.getSource().clusteringColor != MyClusteringConfig.selectedClusterColor || e.getDest().clusteringColor != MyClusteringConfig.selectedClusterColor)
                    continue;
            } else if (e.getCurrentValue() == 0) continue;
            String p = (e.getSource().getName().contains("x") ? MySequentialGraphSysUtil.decodeVariable(e.getSource().getName()) : MySequentialGraphSysUtil.getDecodedNodeName(e.getSource().getName()));
            String edge = p + "-" + MySequentialGraphSysUtil.getDecodedNodeName(e.getDest().getName());
            valueMap.put(edge, e.getUniqueContribution());
            final float hue = rand.nextFloat();
            final float saturation = 0.9f;
            final float luminance = 1.0f;
            Color randomColor = Color.getHSBColor(hue, saturation, luminance);
            colors.add(randomColor);
        }

        valueMap = MySequentialGraphSysUtil.sortMapByLongValue(valueMap);
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
        chart.getCategoryPlot().getDomainAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
        chart.getCategoryPlot().getRangeAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
        BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();
        renderer.setBarPainter(new StandardBarPainter());
        // Set the colors for each data item in the series
        for (int i = 0; i < dataset.getColumnCount(); i++) {
            renderer.setSeriesPaint(i, colors.get(i));
        }

        renderer.setMaximumBarWidth(0.09);
        renderer.setBaseLegendTextFont(MySequentialGraphVars.tahomaPlainFont11);
        // Create a ChartPanel and display it
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(350, 367));

        JLabel titleLabel = new JLabel(" E. U. C.");
        titleLabel.setToolTipText("EDGE UNIQUE CONTRIBUTION DISTRIBUTION");
        titleLabel.setFont(MySequentialGraphVars.tahomaBoldFont11);
        titleLabel.setBackground(Color.WHITE);
        titleLabel.setForeground(Color.DARK_GRAY);

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
        titlePanel.add(titleLabel);

        chartMenu = new JComboBox();
        chartMenu.setFocusable(false);
        chartMenu.setFont(MySequentialGraphVars.tahomaPlainFont11);
        chartMenu.setBackground(Color.WHITE);
        chartMenu.addItem("E. U. C. B.");
        chartMenu.addItem("E. U. C. ");
        chartMenu.setSelectedIndex(1);
        chartMenu.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                if (chartMenu.getSelectedIndex() == 0) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override public void run() {
                            setEdgeUniqueContributionBarChart();
                        }
                    });
                } else if (chartMenu.getSelectedIndex() == 1) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override public void run() {
                            setEdgeUniqueContributionLineChart();
                        }
                    });
                }
            }
        });

        JButton enlargeBtn = new JButton("+");
        enlargeBtn.setFont(MySequentialGraphVars.tahomaPlainFont11);
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
                    chartPanel.getChart().getXYPlot().getDomainAxis().setLabelFont(MySequentialGraphVars.tahomaPlainFont11);
                    chartPanel.getChart().getXYPlot().getDomainAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
                    chartPanel.getChart().getXYPlot().getRangeAxis().setLabelFont(MySequentialGraphVars.tahomaPlainFont11);
                    chartPanel.getChart().getXYPlot().getRangeAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
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

                    JLabel titleLabel = new JLabel(" E. U. C.");
                    titleLabel.setToolTipText("EDGE UNIQUE CONTRIBUTION VALUE DISTRIBUTION");
                    titleLabel.setFont(MySequentialGraphVars.tahomaBoldFont11);
                    titleLabel.setBackground(Color.WHITE);
                    titleLabel.setForeground(Color.DARK_GRAY);

                    JPanel titlePanel = new JPanel();
                    titlePanel.setBackground(Color.WHITE);
                    titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
                    titlePanel.add(titleLabel);

                    JButton enlargeBtn = new JButton("+");
                    enlargeBtn.setFont(MySequentialGraphVars.tahomaPlainFont11);
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
                    chartMenu.setFont(MySequentialGraphVars.tahomaPlainFont11);
                    chartMenu.setBackground(Color.WHITE);
                    chartMenu.addItem("E. U. C. B.");
                    chartMenu.addItem("E. U. C. ");
                    chartMenu.setSelectedIndex(1);
                    chartMenu.addActionListener(new ActionListener() {
                        @Override public void actionPerformed(ActionEvent e) {
                            if (chartMenu.getSelectedIndex() == 0) {
                                SwingUtilities.invokeLater(new Runnable() {
                                    @Override public void run() {
                                        setEdgeUniqueContributionBarChart();
                                    }
                                });
                            } else if (chartMenu.getSelectedIndex() == 1) {
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
        XYSeries uniqueContributionSeries = new XYSeries(" E. U. C.");
        LinkedHashMap<Long, Long> uniqueContributionMap = new LinkedHashMap<>();
        Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
        for (MyEdge e : edges) {
            if (MySequentialGraphVars.getSequentialGraphViewer().isClustered) {
                if (e.getSource().clusteringColor != MyClusteringConfig.selectedClusterColor || e.getDest().clusteringColor != MyClusteringConfig.selectedClusterColor)
                    continue;
            } else if (e.getCurrentValue() == 0) continue;
            total += e.getUniqueContribution();
            if (uniqueContributionMap.containsKey(e.getUniqueContribution())) {
                uniqueContributionMap.put(e.getUniqueContribution(), uniqueContributionMap.get(e.getUniqueContribution())+1);
            } else {
                uniqueContributionMap.put(e.getUniqueContribution(), 1L);
            }
        }

        for (Long uniqueContribution : uniqueContributionMap.keySet()) {
            uniqueContributionSeries.add(uniqueContribution, uniqueContributionMap.get(uniqueContribution));
        }
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(uniqueContributionSeries);

        double avgUniqCont = total/edges.size();
        String plotTitle = "";
        String xaxis = "[AVG.: " + MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(avgUniqCont)) + "]";
        String yaxis = "";
        PlotOrientation orientation = PlotOrientation.VERTICAL;
        boolean show = false;
        boolean toolTips = true;
        boolean urls = false;
        return ChartFactory.createXYLineChart("", "EDGE UNIQUE CONTRIBUTION", "", dataset);
    }

    public void enlarge() {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            MAXIMIZED = true;
            JFrame f = new JFrame(" EDGE UNIQUE CONTRIBUTION DISTRIBUTION");
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.setLayout(new BorderLayout(3, 3));
            f.getContentPane().add(new MyGraphLevelEdgeUniqueContributionDistributionLineChart(), BorderLayout.CENTER);
            f.setPreferredSize(new Dimension(450, 350));
            f.pack();
            f.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
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
                    super.mouseEntered(e);
                    f.setAlwaysOnTop(false);
                }
            });
            f.setAlwaysOnTop(true);
            pb.updateValue(100, 100);
            pb.dispose();
            f.setVisible(true);
            f.setAlwaysOnTop(false);
        } catch (Exception ex) {
            MAXIMIZED = false;
            pb.updateValue(100, 100);
            pb.dispose();
        }
    }
}
