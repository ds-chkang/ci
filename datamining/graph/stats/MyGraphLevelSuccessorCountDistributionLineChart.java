package datamining.graph.stats;

import datamining.main.MyProgressBar;
import datamining.graph.MyNode;
import datamining.utils.system.MySysUtil;
import datamining.utils.system.MyVars;
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.util.*;

public class MyGraphLevelSuccessorCountDistributionLineChart extends JPanel {

    public static int instances = 0;
    public MyGraphLevelSuccessorCountDistributionLineChart() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                setSuccessorCountLineChart();
            }
        });
    }
    public void setSuccessorCountLineChart() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                try {
                    removeAll();
                    setLayout(new BorderLayout(5,5));
                    setBackground(Color.WHITE);

                    Collection<MyNode> nodes = MyVars.g.getVertices();
                    Map<Integer, Integer> successorCountMap = new HashMap<>();
                    XYSeries successorCountSeries = new XYSeries("S. C.");
                    for (MyNode n : nodes) {
                        if (n.getCurrentValue() == 0) continue;
                        int successors = MyVars.g.getSuccessorCount(n);
                        if (successors > 0) {
                            if (successorCountMap.containsKey(successors)) {
                                successorCountMap.put(successors, successorCountMap.get(successors)+1);
                            } else {
                                successorCountMap.put(successors, 1);
                            }
                        }
                    }

                    for (Integer successors : successorCountMap.keySet()) {
                        successorCountSeries.add(successors, successorCountMap.get(successors));
                    }
                    XYSeriesCollection dataset = new XYSeriesCollection();
                    dataset.addSeries(successorCountSeries);

                    JFreeChart chart = ChartFactory.createXYLineChart("", "DEPTH", "", dataset);
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
                    renderer.setSeriesPaint(0, Color.DARK_GRAY);
                    renderer.setSeriesStroke(0, new BasicStroke(1.5f));
                    renderer.setSeriesShapesVisible(0, true);
                    renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                    renderer.setSeriesFillPaint(0, Color.WHITE);
                    renderer.setUseFillPaint(true);

                    ChartPanel chartPanel = new ChartPanel( chart );
                    chartPanel.setPreferredSize( new Dimension( 350 , 367 ) );

                    JLabel titleLabel = new JLabel(" S. C.");
                    titleLabel.setToolTipText("SUCCESSOR COUNT DISTRIBUTION");
                    titleLabel.setFont(MyVars.tahomaBoldFont11);
                    titleLabel.setBackground(Color.WHITE);
                    titleLabel.setForeground(Color.DARK_GRAY);

                    JPanel titlePanel = new JPanel();
                    titlePanel.setBackground(Color.WHITE);
                    titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
                    titlePanel.add(titleLabel);

                    JButton enlargeBtn = new JButton("+");
                    enlargeBtn.setFont(MyVars.tahomaPlainFont10);
                    enlargeBtn.setFocusable(false);
                    enlargeBtn.setBackground(Color.WHITE);
                    enlargeBtn.addActionListener(new ActionListener() {@Override
                    public void actionPerformed(ActionEvent e) {
                        new Thread(new Runnable() {@Override
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
                    chartMenu.addItem("S. C. B.");
                    chartMenu.addItem("S. C.");
                    chartMenu.setSelectedIndex(1);
                    chartMenu.addActionListener(new ActionListener() {
                        @Override public void actionPerformed(ActionEvent e) {
                            if (chartMenu.getSelectedIndex() == 0) {
                                SwingUtilities.invokeLater(new Runnable() {
                                    @Override public void run() {
                                        setSuccessorCountBarChart();

                                    }
                                });
                            } else if (chartMenu.getSelectedIndex() == 1) {
                                SwingUtilities.invokeLater(new Runnable() {
                                    @Override public void run() {
                                        setSuccessorCountLineChart();
                                    }
                                });
                            }
                        }
                    });

                    JPanel btnPanel = new JPanel();
                    btnPanel.setBackground(Color.WHITE);
                    btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
                    btnPanel.add(chartMenu);
                    btnPanel.add(enlargeBtn);

                    JPanel menuPanel = new JPanel();
                    menuPanel.setLayout(new BorderLayout(0,0));
                    menuPanel.setBackground(Color.WHITE);
                    menuPanel.add(titlePanel, BorderLayout.WEST);
                    menuPanel.add(btnPanel, BorderLayout.CENTER);
                    add(menuPanel, BorderLayout.NORTH);
                    renderer.setBaseLegendTextFont(MyVars.tahomaPlainFont10);
                    add(chartPanel, BorderLayout.CENTER);
                    revalidate();
                    repaint();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private Random rand = new Random();
    private static int BAR_LIMIT = 200;
    private ArrayList<Color> colors;
    private static boolean MAXIMIZED;
    private JComboBox chartMenu;

    private void setSuccessorCountBarChart() {
        removeAll();
        setLayout(new BorderLayout(3, 3));
        setBackground(Color.WHITE);

        colors = new ArrayList<>();
        LinkedHashMap<String, Long> valueMap = new LinkedHashMap<>();
        Collection<MyNode> nodes = MyVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0) continue;
            String nodeName = (n.getName().contains("x") ? MySysUtil.decodeVariable(n.getName()) : MySysUtil.getDecodedNodeName(n.getName()));
            valueMap.put(nodeName, (long)MyVars.g.getSuccessorCount(n));
            final float hue = rand.nextFloat();
            final float saturation = 0.9f;
            final float luminance = 1.0f;
            Color randomColor = Color.getHSBColor(hue, saturation, luminance);
            colors.add(randomColor);
        }
        if (valueMap.size() == 0) {
            JLabel titleLabel = new JLabel(" S. C.");
            titleLabel.setToolTipText("NODE VALUE DISTRIBUTION");
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

        JLabel titleLabel = new JLabel(" S. C.");
        titleLabel.setToolTipText("NODE VALUE DISTRIBUTION");
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
        chartMenu.addItem("S. C. B.");
        chartMenu.addItem("S. C.");
        chartMenu.setSelectedIndex(0);
        chartMenu.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                if (chartMenu.getSelectedIndex() == 0) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override public void run() {
                            setSuccessorCountBarChart();

                        }
                    });
                } else if (chartMenu.getSelectedIndex() == 1) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override public void run() {
                          setSuccessorCountLineChart();
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

    public void enlarge() {
        MyProgressBar pb = new MyProgressBar(false);
        MAXIMIZED = true;
        JFrame frame = new JFrame(" SUCCESSOR COUNT DISTRIBUTION");
        frame.setLayout(new BorderLayout());
        frame.setBackground(Color.WHITE);
        frame.setPreferredSize(new Dimension(400, 450));
        pb.updateValue(20, 100);

        frame.getContentPane().add(new MyGraphLevelSuccessorCountDistributionLineChart(), BorderLayout.CENTER);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                MAXIMIZED = false;
            }
        });
        frame.pack();
        pb.updateValue(100,100);
        pb.dispose();
        frame.setCursor(Cursor.HAND_CURSOR);
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);
        frame.setAlwaysOnTop(false);
    }

}
