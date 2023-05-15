package medousa.sequential.graph.stats;

import medousa.sequential.graph.MyNode;
import medousa.MyProgressBar;
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

public class MyGraphLevelValueDistributionLineChart
extends JPanel {

    public static int instances = 0;
    public JComboBox chartMenu;
    private XYSeries valueSeries;
    private Random rand = new Random();
    private static int BAR_LIMIT = 150;
    private ArrayList<Color> colors;
    private static boolean MAXIMIZED;

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
                    setNodeValueBarChart();
                } catch (Exception ex) {}
            }
        });
    }

    private TreeMap<Long, Long> createNodeValueMap() {
        TreeMap<Long, Long> valueMap = new TreeMap<>();
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
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
        chart.getXYPlot().getDomainAxis().setLabelFont(MySequentialGraphVars.tahomaPlainFont11);
        chart.getXYPlot().getDomainAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
        chart.getXYPlot().getRangeAxis().setLabelFont(MySequentialGraphVars.tahomaPlainFont11);
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
        renderer.setBaseLegendTextFont(MySequentialGraphVars.tahomaPlainFont11);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(350, 367));

        JLabel titleLabel = new JLabel(" N. V.");
        titleLabel.setToolTipText("CURRENT NODE VALUE DISTRIBUTION");
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
        chartMenu.addItem("N. V.");
        chartMenu.addItem("N. V. B.");
        chartMenu.setSelectedIndex(0);
        chartMenu.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                if (chartMenu.getSelectedIndex() == 1) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override public void run() {
                            setNodeValueBarChart();
                        }
                    });
                } else if (chartMenu.getSelectedIndex() == 0) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override public void run() {
                            setNodeValueLineChart();
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

    public void enlarge() {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            MAXIMIZED = true;
            JFrame f = new JFrame(" NODE VALUE DISTRIBUTION");
            f.setLayout(new BorderLayout(3, 3));
            f.getContentPane().add(new MyGraphLevelNodeValueDistributionLineChart(), BorderLayout.CENTER);
            f.setPreferredSize(new Dimension(450, 350));
            f.pack();
            f.setCursor(Cursor.HAND_CURSOR);
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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



    private void setNodeValueBarChart() {
        removeAll();
        setLayout(new BorderLayout(3, 3));
        setBackground(Color.WHITE);

        colors = new ArrayList<>();
        LinkedHashMap<String, Long> valueMap = new LinkedHashMap<>();
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0) continue;
            String nodeName = (n.getName().contains("x") ? MySequentialGraphSysUtil.getDecodeVariableNodeName(n.getName()) : MySequentialGraphSysUtil.getDecodedNodeName(n.getName()));
            valueMap.put(nodeName, (long)n.getCurrentValue());
            final float hue = rand.nextFloat();
            final float saturation = 0.9f;
            final float luminance = 1.0f;
            Color randomColor = Color.getHSBColor(hue, saturation, luminance);
            colors.add(randomColor);
        }
        if (valueMap.size() == 0) {
            JLabel titleLabel = new JLabel(" N. V.");
            titleLabel.setToolTipText("NODE VALUE DISTRIBUTION");
            titleLabel.setFont(MySequentialGraphVars.tahomaBoldFont11);
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
            msg.setFont(MySequentialGraphVars.tahomaPlainFont12);
            msg.setBackground(Color.WHITE);
            msg.setHorizontalAlignment(JLabel.CENTER);
            add(topPanel, BorderLayout.NORTH);
            add(msg, BorderLayout.CENTER);
            return;
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

        JLabel titleLabel = new JLabel(" N. V.");
        titleLabel.setToolTipText("NODE VALUE DISTRIBUTION");
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
        chartMenu.addItem("N. V.");
        chartMenu.addItem("N. V. B.");
        chartMenu.setSelectedIndex(1);
        chartMenu.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                if (chartMenu.getSelectedIndex() == 1) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override public void run() {
                            setNodeValueBarChart();

                        }
                    });
                } else if (chartMenu.getSelectedIndex() == 0) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override public void run() {
                            setNodeValueLineChart();
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

}
