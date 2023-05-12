package medousa.sequential.graph.stats;

import medousa.sequential.graph.MyComboBoxTooltipRenderer;
import medousa.MyProgressBar;
import medousa.sequential.graph.MyNode;
import medousa.sequential.utils.MyMathUtil;
import medousa.sequential.utils.MySequentialGraphSysUtil;
import medousa.sequential.utils.MySequentialGraphVars;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
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
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.util.*;

public class MyGraphLevelUniqueNodesByDepthLineBarChart
extends JPanel{

    public static int instances = 0;
    private int selectedChart = 0;
    private ArrayList<Color> colors;
    private static int BAR_LIMIT = 150;
    private String avg = "";
    private String aboveAvg = "";
    private String belowAvg = "";
    private String equalToAvg = "";
    private String num = "";
    private static boolean MAXIMIZED = false;

    private Random rand = new Random();

    public MyGraphLevelUniqueNodesByDepthLineBarChart() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                setUniqueNodesByDepthLineChart();
            }
        });
    }

    public void setUniqueNodesByDepthLineChart() {
        try {
            removeAll();
            setLayout(new BorderLayout(5, 5));
            setBackground(Color.WHITE);

            XYSeries uniqueNodeSeries = new XYSeries("U. N.");
            XYSeriesCollection dataset = new XYSeriesCollection();
            Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
            if (MySequentialGraphVars.currentGraphDepth == 0) {
                for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                    int totalNode = 0;
                    for (MyNode n : nodes) {
                        if (n.getNodeDepthInfoMap().containsKey(i)) {
                            totalNode++;
                        }
                    }
                    uniqueNodeSeries.add(i, totalNode);
                }
            } else {
                for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                    if (i == MySequentialGraphVars.currentGraphDepth) {
                        int totalNode = 0;
                        for (MyNode n : nodes) {
                            if (n.getNodeDepthInfoMap().containsKey(i)) {
                                totalNode++;
                            }
                        }
                        uniqueNodeSeries.add(i, totalNode);
                    } else {
                        uniqueNodeSeries.add(i, 0);
                    }
                }
            }

            dataset.addSeries(uniqueNodeSeries);

            JFreeChart chart = ChartFactory.createXYLineChart("", "DEPTH", "", dataset);
            chart.getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
            chart.getXYPlot().setBackgroundPaint(Color.WHITE);
            chart.getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
            chart.getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);
            chart.getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
            chart.getXYPlot().getDomainAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
            chart.getXYPlot().getDomainAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
            chart.getXYPlot().getRangeAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
            chart.getXYPlot().getRangeAxis().setLabelFont(MySequentialGraphVars.tahomaPlainFont11);
            chart.getXYPlot().getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
            chart.setBackgroundPaint(Color.WHITE);

            XYPlot plot = (XYPlot) chart.getPlot();
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
            renderer.setSeriesPaint(0, Color.DARK_GRAY);
            renderer.setSeriesStroke(0, new BasicStroke(1.5f));
            renderer.setSeriesShapesVisible(0, true);
            renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
            renderer.setSeriesFillPaint(0, Color.WHITE);

            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(560, 367));

            JLabel titleLabel = new JLabel(" UNIQUE NODES");
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
                @Override public void actionPerformed(ActionEvent e) {
                    new Thread(new Runnable() {
                        @Override public void run() {
                            enlarge();
                        }
                    }).start();
                }
            });

            JComboBox chartMenu = new JComboBox();
            chartMenu.setToolTipText("SELECT A DEPTH FOR DEPTH NODES");
            String [] tooltips = new String[MySequentialGraphVars.mxDepth+2];
            tooltips[0] = "SELECT A DEPTH FOR THE DEPTH NODES.";
            tooltips[1] = "UNIQUE NODE RATIO BY DEPTH";
            for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                tooltips[i+1] = "DEPTH " + i + " FOR THE DEPTH NODES.";
            }
            chartMenu.setRenderer(new MyComboBoxTooltipRenderer(tooltips));

            chartMenu.setFocusable(false);
            chartMenu.setBackground(Color.WHITE);
            chartMenu.setFont(MySequentialGraphVars.tahomaPlainFont11);
            chartMenu.addItem("SELECT");
            chartMenu.addItem("N. R.");
            for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                chartMenu.addItem("" + i);
            }
            chartMenu.setSelectedIndex(selectedChart);
            chartMenu.addActionListener(new ActionListener() {
                @Override public void actionPerformed(ActionEvent e) {
                    if (chartMenu.getSelectedIndex() == 0) {
                        selectedChart = 0;
                        setUniqueNodesByDepthLineChart();
                    } else if (chartMenu.getSelectedIndex() == 1) {
                        selectedChart = 1;
                        setUniqueNodeRatioByDepthLineChart();
                    } else {
                        selectedChart = chartMenu.getSelectedIndex();
                        setUniqueNodeBarChartByDepth();
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
            chart.removeLegend();

            revalidate();
            repaint();
        } catch (Exception ex) {ex.printStackTrace();}
    }

    public void setUniqueNodeRatioByDepthLineChart() {
        try {
            removeAll();
            setLayout(new BorderLayout(5, 5));
            setBackground(Color.WHITE);

            XYSeries uniqueNodeSeries = new XYSeries("U. N.");
            XYSeriesCollection dataset = new XYSeriesCollection();
            Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
            for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                int totalNode = 0;
                for (MyNode n : nodes) {
                    if (n.getNodeDepthInfoMap().containsKey(i)) {
                        totalNode++;
                    }
                }
                uniqueNodeSeries.add(i, Double.parseDouble(MyMathUtil.twoDecimalFormat((double)totalNode/ MySequentialGraphVars.g.getVertexCount())));
            }
            dataset.addSeries(uniqueNodeSeries);

            JFreeChart chart = ChartFactory.createXYLineChart("", "DEPTH", "", dataset);
            chart.getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
            chart.getXYPlot().setBackgroundPaint(Color.WHITE);
            chart.getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
            chart.getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);
            chart.getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
            chart.getXYPlot().getDomainAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
            chart.getXYPlot().getDomainAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
            chart.getXYPlot().getRangeAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
            chart.getXYPlot().getRangeAxis().setLabelFont(MySequentialGraphVars.tahomaPlainFont11);
            chart.getXYPlot().getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
            chart.setBackgroundPaint(Color.WHITE);

            XYPlot plot = (XYPlot) chart.getPlot();
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
            renderer.setSeriesPaint(0, Color.DARK_GRAY);
            renderer.setSeriesStroke(0, new BasicStroke(1.5f));
            renderer.setSeriesShapesVisible(0, true);
            renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
            renderer.setSeriesFillPaint(0, Color.WHITE);

            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(560, 367));

            JLabel titleLabel = new JLabel(" UNIQUE NODES");
            titleLabel.setToolTipText("UNIQUE NODE RATIO BY DEPTH");
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
                        @Override public void run() {
                            enlarge();
                        }
                    }).start();
                }
            });

            JComboBox chartMenu = new JComboBox();
            chartMenu.setToolTipText("SELECT A DEPTH FOR DEPTH NODES");
            String [] tooltips = new String[MySequentialGraphVars.mxDepth+1];
            tooltips[0] = "SELECT A DEPTH FOR THE DEPTH NODES.";
            tooltips[1] = "UNIQUE NODE RATIO BY DEPTH";
            for (int i = 1; i < MySequentialGraphVars.mxDepth; i++) {
                tooltips[i+1] = "DEPTH " + i + " FOR THE DEPTH NODES.";
            }
            chartMenu.setRenderer(new MyComboBoxTooltipRenderer(tooltips));

            chartMenu.setFocusable(false);
            chartMenu.setBackground(Color.WHITE);
            chartMenu.setFont(MySequentialGraphVars.tahomaPlainFont11);
            chartMenu.addItem("SELECT");
            chartMenu.addItem("N. R.");
            for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                chartMenu.addItem("" + i);
            }
            chartMenu.setSelectedIndex(selectedChart);
            chartMenu.addActionListener(new ActionListener() {
                @Override public void actionPerformed(ActionEvent e) {
                    if (chartMenu.getSelectedIndex() == 0) {
                        selectedChart = 0;
                        setUniqueNodesByDepthLineChart();
                    } else if (chartMenu.getSelectedIndex() == 1) {
                        selectedChart = 1;
                        setUniqueNodeRatioByDepthLineChart();
                    } else {
                        selectedChart = chartMenu.getSelectedIndex();
                        setUniqueNodeBarChartByDepth();
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
            chart.removeLegend();

            revalidate();
            repaint();
        } catch (Exception ex) {ex.printStackTrace();}

    }

    private void setUniqueNodeBarChartByDepth() {
        removeAll();
        setLayout(new BorderLayout(5, 5));
        setBackground(Color.WHITE);

        colors = new ArrayList<>();
        LinkedHashMap<String, Long> valueMap = new LinkedHashMap<>();
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0) continue;
            if (n.getNodeDepthInfoMap().containsKey(selectedChart)) {
                String pName = (n.getName().contains("x") ? MySequentialGraphSysUtil.decodeVariable(n.getName()) : MySequentialGraphSysUtil.getDecodedNodeName(n.getName()));
                valueMap.put(pName, (long) n.getCurrentValue());
                final float hue = rand.nextFloat();
                final float saturation = 0.9f;
                final float luminance = 1.0f;
                Color randomColor = Color.getHSBColor(hue, saturation, luminance);
                colors.add(randomColor);
            }
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
                if (i == 6) break;
                ((DefaultCategoryDataset) dataset).addValue(valueMap.get(label), label, "");
                i++;
            }
        }

        JFreeChart chart = ChartFactory.createBarChart("", "", "", dataset);
        chart.getCategoryPlot().setBackgroundPaint(Color.WHITE);
        chart.getCategoryPlot().setDomainGridlinePaint(Color.DARK_GRAY);
        chart.getCategoryPlot().setRangeGridlinePaint(Color.DARK_GRAY);
        chart.getCategoryPlot().getDomainAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
        chart.getCategoryPlot().getRangeAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
        BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();
        renderer.setBarPainter(new StandardBarPainter());
        for (int i = 0; i < dataset.getColumnCount(); i++) {
            renderer.setSeriesPaint(i, colors.get(i));
        }

        renderer.setMaximumBarWidth(0.09);
        renderer.setBaseLegendTextFont(MySequentialGraphVars.tahomaPlainFont11);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(350, 367));

        JLabel titleLabel = new JLabel(" UNIQUE NODES");
        titleLabel.setToolTipText("UNIQUE NODE DISTRIBUTION BY DEPTH");
        titleLabel.setFont(MySequentialGraphVars.tahomaBoldFont12);
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

        JComboBox chartMenu = new JComboBox();
        chartMenu.setFocusable(false);
        chartMenu.setBackground(Color.WHITE);
        chartMenu.setFont(MySequentialGraphVars.tahomaPlainFont11);
        chartMenu.addItem("SELECT");
        chartMenu.addItem("N. R.");
        for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
            chartMenu.addItem("" + i);
        }
        chartMenu.setSelectedIndex(selectedChart);
        chartMenu.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                if (chartMenu.getSelectedIndex() == 0) {
                    selectedChart = 0;
                    setUniqueNodesByDepthLineChart();
                } else if (chartMenu.getSelectedIndex() == 1) {
                    selectedChart = 1;
                    setUniqueNodeRatioByDepthLineChart();
                } else {
                    selectedChart = chartMenu.getSelectedIndex();
                    setUniqueNodeBarChartByDepth();
                }
            }
        });

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Color.WHITE);
        btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 3));
        btnPanel.add(chartMenu);
        btnPanel.add(enlargeBtn);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout(3, 3));
        topPanel.setBackground(Color.WHITE);
        topPanel.add(titlePanel, BorderLayout.WEST);
        topPanel.add(btnPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);
        add(chartPanel, BorderLayout.CENTER);
        chart.removeLegend();

        revalidate();
        repaint();
    }


    public void enlarge() {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            MAXIMIZED = true;
            JFrame f = new JFrame("UNIQUE NODE DISTRIBUTION BY DEPTH");
            f.setLayout(new BorderLayout());
            f.setBackground(Color.WHITE);
            f.setPreferredSize(new Dimension(450, 350));
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.getContentPane().add(new MyGraphLevelUniqueNodesByDepthLineBarChart(), BorderLayout.CENTER);
            f.pack();
            f.setCursor(Cursor.HAND_CURSOR);
            f.setAlwaysOnTop(true);
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
                    super.mouseExited(e);
                    f.setAlwaysOnTop(false);
                }
            });
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
