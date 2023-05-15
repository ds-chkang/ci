package medousa.sequential.graph.stats.multinode;

import medousa.MyProgressBar;
import medousa.sequential.graph.MyNode;
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

public class MyMultiNodePredecessorValueDistributionLineChart
extends JPanel
implements ActionListener {

    public static int instances = 0;
    private JComboBox chartMenu;
    private ArrayList<Color> colors = new ArrayList<>();
    private Random rand = new Random();
    private static boolean MAXIMIZED;

    public MyMultiNodePredecessorValueDistributionLineChart() {
        this.decorateValueLineChart();
    }

    public void decorateValueLineChart() {
        final MyMultiNodePredecessorValueDistributionLineChart multiNodePredecessorValueDistributionLineChart = this;
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                try {
                    removeAll();
                    setLayout(new BorderLayout(5,5));
                    setBackground(Color.WHITE);

                    Map<Integer, Integer> predecessorValueMap = new HashMap<>();
                    XYSeries predecessorValueSeries = new XYSeries("P. V.");

                    for (MyNode n : MySequentialGraphVars.getSequentialGraphViewer().multiNodePredecessors) {
                        if (predecessorValueMap.containsKey((int) n.getCurrentValue())) {
                            predecessorValueMap.put((int) n.getCurrentValue(), predecessorValueMap.get((int) n.getCurrentValue()) + 1);
                        } else {
                            predecessorValueMap.put((int) n.getCurrentValue(), 1);
                        }
                    }

                    for (Integer predecessorValue : predecessorValueMap.keySet()) {
                        predecessorValueSeries.add(predecessorValue, predecessorValueMap.get(predecessorValue));
                    }

                    XYSeriesCollection dataset = new XYSeriesCollection();
                    dataset.addSeries(predecessorValueSeries);

                    JFreeChart chart = ChartFactory.createXYLineChart("", "P. V.", "", dataset);
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

                    ChartPanel chartPanel = new ChartPanel( chart );
                    chartPanel.setPreferredSize( new Dimension( 350 , 367 ) );

                    JLabel titleLabel = new JLabel(" MULTINODE P. V.");
                    titleLabel.setToolTipText("MULTINODE PREDECESSOR VALUE DISTRIBUTION");
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
                    chartMenu.setBackground(Color.WHITE);
                    chartMenu.setFont(MySequentialGraphVars.tahomaPlainFont11);
                    chartMenu.setFocusable(false);
                    chartMenu.addItem("VALUE");
                    chartMenu.addItem("NODE");
                    chartMenu.addActionListener(multiNodePredecessorValueDistributionLineChart);

                    JPanel btnPanel = new JPanel();
                    btnPanel.setBackground(Color.WHITE);
                    btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
                    btnPanel.add(chartMenu);
                    btnPanel.add(enlargeBtn);

                    JPanel topPanel = new JPanel();
                    topPanel.setLayout(new BorderLayout(0,0));
                    topPanel.setBackground(Color.WHITE);
                    topPanel.add(titlePanel, BorderLayout.WEST);
                    topPanel.add(btnPanel, BorderLayout.CENTER);
                    add(chartPanel, BorderLayout.CENTER);
                    add(topPanel, BorderLayout.NORTH);
                    renderer.setBaseLegendTextFont(MySequentialGraphVars.tahomaPlainFont11);

                    revalidate();
                    repaint();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void decorateNodeChart(int selectedMenuIdx) {
        final MyMultiNodePredecessorValueDistributionLineChart multiNodePredecessorValueDistributionLineChart = this;
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                try {
                    removeAll();
                    setLayout(new BorderLayout(3, 3));
                    setBackground(Color.WHITE);

                    for (int i = 0; i < MySequentialGraphVars.getSequentialGraphViewer().multiNodes.size(); i++) {
                        final float hue = rand.nextFloat();
                        final float saturation = 0.9f;
                        final float luminance = 1.0f;
                        Color randomColor = Color.getHSBColor(hue, saturation, luminance);
                        colors.add(randomColor);
                    }

                    LinkedHashMap<String, Long> predecessorValueMap = new LinkedHashMap<>();
                    for (MyNode n : MySequentialGraphVars.getSequentialGraphViewer().multiNodePredecessors) {
                        if (n.getCurrentValue() == 0) continue;
                        String nn = (n.getName().contains("x") ? MySequentialGraphSysUtil.getDecodeVariableNodeName(n.getName()) : MySequentialGraphSysUtil.getDecodedNodeName(n.getName()));
                        predecessorValueMap.put(nn, (long)n.getCurrentValue());
                        if (!MAXIMIZED && predecessorValueMap.size() == 6) {
                            break;
                        } else if (MAXIMIZED && predecessorValueMap.size() == 50) {
                            break;
                        }
                    }
                    predecessorValueMap = MySequentialGraphSysUtil.sortMapByLongValue(predecessorValueMap);

                    CategoryDataset dataset = new DefaultCategoryDataset();
                    for (String label : predecessorValueMap.keySet()) {
                        ((DefaultCategoryDataset) dataset).addValue(predecessorValueMap.get(label), label, "");
                    }

                    // Create a bar chart with the dataset
                    JFreeChart chart = ChartFactory.createBarChart(
                            "",        // chart title
                            "",           // x axis label
                            "",           // y axis label
                            dataset                   // data
                    );
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
                    renderer.setMaximumBarWidth(0.07);
                    renderer.setBaseLegendTextFont(MySequentialGraphVars.tahomaPlainFont11);
                    // Create a ChartPanel and display it
                    ChartPanel chartPanel = new ChartPanel(chart);
                    chartPanel.setPreferredSize(new Dimension(350, 367));

                    JLabel titleLabel = new JLabel(" MULTINODE P. V.");
                    titleLabel.setToolTipText("MULTINODE PREDECESSOR VALUE DISTRIBUTION");
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
                    chartMenu.setBackground(Color.WHITE);
                    chartMenu.setFont(MySequentialGraphVars.tahomaPlainFont11);
                    chartMenu.setFocusable(false);
                    chartMenu.addItem("VALUE");
                    chartMenu.addItem("NODE");
                    chartMenu.setSelectedIndex(selectedMenuIdx);
                    chartMenu.addActionListener(multiNodePredecessorValueDistributionLineChart);

                    JPanel btnPanel = new JPanel();
                    btnPanel.setBackground(Color.WHITE);
                    btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
                    btnPanel.add(chartMenu);
                    btnPanel.add(enlargeBtn);

                    JPanel topPanel = new JPanel();
                    topPanel.setLayout(new BorderLayout(3, 3));
                    topPanel.setBackground(Color.WHITE);
                    topPanel.add(titlePanel, BorderLayout.WEST);
                    topPanel.add(btnPanel, BorderLayout.EAST);
                    add(topPanel, BorderLayout.NORTH);
                    add(chartPanel, BorderLayout.CENTER);
                    revalidate();
                    repaint();
                } catch (Exception ex) {}
            }
        });
    }

    public void enlarge() {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            MAXIMIZED = true;
            JFrame f = new JFrame(" MULTINODE PREDECESSOR VALUE DISTRIBUTION");
            f.setLayout(new BorderLayout());
            f.setBackground(Color.WHITE);
            f.setPreferredSize(new Dimension(400, 300));
            f.getContentPane().add(new MyMultiNodePredecessorValueDistributionLineChart(), BorderLayout.CENTER);
            f.pack();
            f.addWindowListener(new WindowAdapter() {
                @Override public void windowClosing(WindowEvent e) {
                    super.windowClosing(e);
                    MAXIMIZED = false;
                }
            });
            f.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    f.setAlwaysOnTop(true);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    super.mouseEntered(e);
                    f.setAlwaysOnTop(false);
                }
            });
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.setCursor(Cursor.HAND_CURSOR);
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

    @Override public void actionPerformed(ActionEvent e) {
        if (e.getSource() == chartMenu) {
            if (chartMenu.getSelectedItem().toString().equals("NODE")) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        decorateNodeChart(chartMenu.getSelectedIndex());
                    }
                }).start();
            } else if (chartMenu.getSelectedItem().toString().equals("VALUE")) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        decorateValueLineChart();
                    }
                }).start();
            }
        }
    }
}
