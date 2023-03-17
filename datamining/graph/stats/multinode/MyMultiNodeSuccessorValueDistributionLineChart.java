package datamining.graph.stats.multinode;

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
import java.awt.geom.Ellipse2D;
import java.util.*;

public class MyMultiNodeSuccessorValueDistributionLineChart
extends JPanel
implements ActionListener {

    public static int instances = 0;
    private JComboBox chartMenu;
    private java.util.ArrayList<Color> colors = new ArrayList<>();
    private Random rand = new Random();
    public MyMultiNodeSuccessorValueDistributionLineChart() {
        this.decorateValueChart();
    }
    public void decorateValueChart() {
        final MyMultiNodeSuccessorValueDistributionLineChart multiNodeSuccessorValueDistributionLineChart = this;
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                try {
                    removeAll();
                    setLayout(new BorderLayout(5,5));
                    setBackground(Color.WHITE);

                    Map<Integer, Integer> successorValueMap = new HashMap<>();
                    XYSeries successorValueSeries = new XYSeries("S. VALUE");

                    for (MyNode n : MyVars.getViewer().multiNodeSuccessors) {
                        if (successorValueMap.containsKey((int) n.getCurrentValue())) {
                            successorValueMap.put((int) n.getCurrentValue(), successorValueMap.get((int) n.getCurrentValue()) + 1);
                        } else {
                            successorValueMap.put((int) n.getCurrentValue(), 1);
                        }
                    }

                    for (Integer successorValue : successorValueMap.keySet()) {
                        successorValueSeries.add(successorValue, successorValueMap.get(successorValue));
                    }

                    XYSeriesCollection dataset = new XYSeriesCollection();
                    dataset.addSeries(successorValueSeries);

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
                    renderer.setSeriesPaint(0, Color.BLACK);
                    renderer.setSeriesStroke(0, new BasicStroke(1.5f));
                    renderer.setSeriesShapesVisible(0, true);
                    renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                    renderer.setSeriesFillPaint(0, Color.WHITE);
                    renderer.setUseFillPaint(true);
                    renderer.setBaseLegendTextFont(MyVars.tahomaPlainFont10);

                    ChartPanel chartPanel = new ChartPanel( chart );
                    chartPanel.setPreferredSize( new Dimension( 350 , 367 ) );

                    JLabel titleLabel = new JLabel(" MULTINODE S. V.");
                    titleLabel.setToolTipText("SUCCESSOR VALUE DISTRIBUTION");
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
                    chartMenu.setBackground(Color.WHITE);
                    chartMenu.setFont(MyVars.tahomaPlainFont10);
                    chartMenu.setFocusable(false);
                    chartMenu.addItem("VALUE");
                    chartMenu.addItem("NODE");
                    chartMenu.addActionListener(multiNodeSuccessorValueDistributionLineChart);

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
                    add(chartPanel, BorderLayout.CENTER);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                revalidate();
                repaint();
            }
        });
    }


    public void decorateNodeChart() {
        final MyMultiNodeSuccessorValueDistributionLineChart multiNodeSuccessorValueDistributionLineChart = this;
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                try {
                    removeAll();
                    setLayout(new BorderLayout(3, 3));
                    setBackground(Color.WHITE);

                    for (int i=0; i < MyVars.getViewer().multiNodes.size(); i++) {
                        final float hue = rand.nextFloat();
                        final float saturation = 0.9f;
                        final float luminance = 1.0f;
                        Color randomColor = Color.getHSBColor(hue, saturation, luminance);
                        colors.add(randomColor);
                    }

                    LinkedHashMap<String, Long> multiNodeNameValueMap = new LinkedHashMap<>();
                    for (MyNode n : MyVars.getViewer().multiNodeSuccessors) {
                        if (n.getCurrentValue() == 0) continue;
                        String nn = (n.getName().contains("x") ? MySysUtil.decodeVariable(n.getName()) : MySysUtil.getDecodedNodeName(n.getName()));
                        multiNodeNameValueMap.put(nn, (long)n.getCurrentValue());
                    }
                    multiNodeNameValueMap = MySysUtil.sortMapByLongValue(multiNodeNameValueMap);

                    CategoryDataset dataset = new DefaultCategoryDataset();
                    for (String label : multiNodeNameValueMap.keySet()) {
                        ((DefaultCategoryDataset) dataset).addValue(multiNodeNameValueMap.get(label), label, "");
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
                    chart.getCategoryPlot().getDomainAxis().setTickLabelFont(MyVars.tahomaPlainFont10);
                    chart.getCategoryPlot().getRangeAxis().setTickLabelFont(MyVars.tahomaPlainFont10);
                    BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();
                    renderer.setBarPainter(new StandardBarPainter());
                    // Set the colors for each data item in the series
                    for (int i = 0; i <= dataset.getColumnCount(); i++) {
                        renderer.setSeriesPaint(i, colors.get(i));
                    }
                    renderer.setMaximumBarWidth(0.07);
                    renderer.setBaseLegendTextFont(MyVars.tahomaPlainFont10);
                    // Create a ChartPanel and display it
                    ChartPanel chartPanel = new ChartPanel(chart);
                    chartPanel.setPreferredSize(new Dimension(350, 367));

                    JLabel titleLabel = new JLabel(" MULTINODE S. V.");
                    titleLabel.setToolTipText("MULTINODE SUCCESSOR VALUE DISTRIBUTION");
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
                    chartMenu.setBackground(Color.WHITE);
                    chartMenu.setFont(MyVars.tahomaPlainFont10);
                    chartMenu.setFocusable(false);
                    chartMenu.addItem("VALUE");
                    chartMenu.addItem("NODE");
                    chartMenu.addActionListener(multiNodeSuccessorValueDistributionLineChart);

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
        MyMultiNodeSuccessorValueDistributionLineChart multiNodeLevelSuccessorValueDistributionLineChart = new MyMultiNodeSuccessorValueDistributionLineChart();
        multiNodeLevelSuccessorValueDistributionLineChart.setLayout(new BorderLayout(5,5));
        multiNodeLevelSuccessorValueDistributionLineChart.setBackground(Color.WHITE);

        Map<Integer, Integer> successorValueMap = new HashMap<>();
        XYSeries successorValueSeries = new XYSeries("S. V.");

        for (MyNode n : MyVars.getViewer().multiNodeSuccessors) {
            if (successorValueMap.containsKey((int) n.getCurrentValue())) {
                successorValueMap.put((int) n.getCurrentValue(), successorValueMap.get((int) n.getCurrentValue()) + 1);
            } else {
                successorValueMap.put((int) n.getCurrentValue(), 1);
            }
        }

        for (Integer successorValue : successorValueMap.keySet()) {
            successorValueSeries.add(successorValue, successorValueMap.get(successorValue));
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(successorValueSeries);

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
        renderer.setSeriesPaint(0, Color.BLACK);
        renderer.setSeriesStroke(0, new BasicStroke(1.5f));
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
        renderer.setSeriesFillPaint(0, Color.WHITE);
        renderer.setUseFillPaint(true);

        ChartPanel chartPanel = new ChartPanel( chart );
        chartPanel.setPreferredSize( new Dimension( 350 , 367 ) );
        multiNodeLevelSuccessorValueDistributionLineChart.add(chartPanel, BorderLayout.CENTER);

        JFrame frame = new JFrame(" MULTI-NODE SUCCESSOR VALUE DISTRIBUTION");
        frame.setLayout(new BorderLayout());
        frame.setBackground(Color.WHITE);
        frame.setPreferredSize(new Dimension(400, 300));
        pb.updateValue(20, 100);

        frame.getContentPane().add(multiNodeLevelSuccessorValueDistributionLineChart, BorderLayout.CENTER);
        frame.pack();
        pb.updateValue(60, 100);

        pb.updateValue(100,100);
        pb.dispose();

        frame.setCursor(Cursor.HAND_CURSOR);
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);
        frame.setAlwaysOnTop(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
            if (e.getSource() == chartMenu) {
                if (chartMenu.getSelectedItem().toString().equals("NODE")) {
                    new Thread(new Runnable() {
                        @Override public void run() {
                            decorateNodeChart();
                        }
                    }).start();
                } else if (chartMenu.getSelectedItem().toString().equals("VALUE")) {
                    new Thread(new Runnable() {
                        @Override public void run() {
                            decorateValueChart();
                        }
                    }).start();
                }
            }

    }
}
