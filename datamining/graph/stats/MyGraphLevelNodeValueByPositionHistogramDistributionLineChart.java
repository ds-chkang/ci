package datamining.graph.stats;

import datamining.utils.MyMathUtil;
import datamining.utils.system.MySysUtil;
import datamining.utils.system.MyVars;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Random;

public class MyGraphLevelNodeValueByPositionHistogramDistributionLineChart
extends JPanel
implements ActionListener {

    public static int instances = 0;
    private ArrayList<Color> colors;
    private JComboBox chartMenu;
    private int selectedChart;
    private Random rand = new Random();
    private static int BAR_LIMIT = 300;
    private String avg = "";
    private String aboveAvg = "";
    private String belowAvg = "";
    private String equalToAvg = "";
    private String num = "";

    public MyGraphLevelNodeValueByPositionHistogramDistributionLineChart() {
        this.decorate();
    }

    public void decorate() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                try {
                    removeAll();
                    setLayout(new BorderLayout(3, 3));
                    setBackground(Color.WHITE);
                    double totalValue = 0D;

                    if (selectedChart == 0) {
                        colors = new ArrayList<>();
                        LinkedHashMap<String, Long> valueMap = new LinkedHashMap<>();
                        for (int s = 0; s < MyVars.seqs.length; s++) {
                            String startingNode = "";
                            if (MyVars.isSupplementaryOn) {
                                startingNode = MyVars.seqs[s][1].split(":")[0];
                            } else {
                                startingNode = MyVars.seqs[s][0].split(":")[0];
                            }
                            startingNode = (startingNode.contains("x") ? MySysUtil.decodeVariable(startingNode) : MySysUtil.getDecodedNodeName(startingNode));
                            if (valueMap.containsKey(startingNode)) {
                                valueMap.put(startingNode, valueMap.get(startingNode) + 1);
                            } else {
                                valueMap.put(startingNode, 1L);
                            }
                            totalValue++;
                            final float hue = rand.nextFloat();
                            final float saturation = 0.9f;
                            final float luminance = 1.0f;
                            Color randomColor = Color.getHSBColor(hue, saturation, luminance);
                            colors.add(randomColor);
                        }
                        avg = MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(totalValue/MyVars.seqs.length));
                        num = MyMathUtil.getCommaSeperatedNumber(valueMap.size());
                        int above = 0;
                        int below = 0;
                        int equal = 0;
                        for (String n : valueMap.keySet()) {
                            if (Double.parseDouble(avg) < valueMap.get(n)) {
                                above++;
                            } else if (Double.parseDouble(avg) > valueMap.get(n)) {
                                below++;
                            } else {
                                equal++;
                            }
                        }
                        equalToAvg = MyMathUtil.getCommaSeperatedNumber(equal) + "[" + MyMathUtil.twoDecimalFormat(((double)equal/valueMap.size())*100) + "%]";
                        aboveAvg = MyMathUtil.getCommaSeperatedNumber(above) + "[" + MyMathUtil.twoDecimalFormat(((double)above/valueMap.size())*100) + "%]";
                        belowAvg = MyMathUtil.getCommaSeperatedNumber(below) + "[" + MyMathUtil.twoDecimalFormat(((double)below/valueMap.size())*100) + "%]";
                        valueMap = MySysUtil.sortMapByLongValue(valueMap);

                        CategoryDataset dataset = new DefaultCategoryDataset();
                        JFreeChart chart = ChartFactory.createBarChart("", "", "", dataset);
                        BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();

                        if (MAXIMIZED) {
                            int limitCount = 0;
                            for (String label : valueMap.keySet()) {
                                if (!MAXIMIZED && limitCount == 8) break;
                                else if (MAXIMIZED && limitCount == BAR_LIMIT) break;
                                ((DefaultCategoryDataset) dataset).addValue(valueMap.get(label), label, label);
                                limitCount++;
                            }
                            chart.getCategoryPlot().getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.DOWN_45);
                            chart.removeLegend();
                        } else {
                            int limitCount = 0;
                            for (String label : valueMap.keySet()) {
                                if (!MAXIMIZED && limitCount == 8) break;
                                else if (MAXIMIZED && limitCount == BAR_LIMIT) break;
                                ((DefaultCategoryDataset) dataset).addValue(valueMap.get(label), label, "");
                                limitCount++;
                            }
                            renderer.setBaseLegendTextFont(MyVars.tahomaPlainFont10);
                        }
                        chart.getCategoryPlot().setBackgroundPaint(Color.WHITE);
                        chart.getCategoryPlot().setDomainGridlinePaint(Color.DARK_GRAY);
                        chart.getCategoryPlot().setRangeGridlinePaint(Color.DARK_GRAY);
                        chart.getCategoryPlot().getDomainAxis().setLabelFont(MyVars.tahomaPlainFont7);
                        chart.getCategoryPlot().getDomainAxis().setTickLabelFont(MyVars.tahomaPlainFont7);
                        chart.getCategoryPlot().getRangeAxis().setTickLabelFont(MyVars.tahomaPlainFont10);
                        renderer.setBarPainter(new StandardBarPainter());
                        for (int i = 0; i < dataset.getColumnCount(); i++) {
                            renderer.setSeriesPaint(i, colors.get(i));
                        }
                        //renderer.setMaximumBarWidth(0.2);

                        ChartPanel chartPanel = new ChartPanel(chart);
                        chartPanel.setPreferredSize(new Dimension(350, 367));

                        JLabel titleLabel = new JLabel(" HEAD. N. V.");
                        titleLabel.setToolTipText("HEAD NODE VALUE DISTRIBUTION");
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
                        chartMenu.addItem("HEAD");
                        chartMenu.addItem("INTER.");
                        chartMenu.addItem("TAIL");
                        chartMenu.setSelectedIndex(selectedChart);
                        chartMenu.addActionListener(new ActionListener() {
                            @Override public void actionPerformed(ActionEvent e) {
                                if (chartMenu.getSelectedIndex() == 0) {
                                    selectedChart = 0;
                                    decorate();
                                } else if (chartMenu.getSelectedIndex() == 1) {
                                    selectedChart = 1;
                                    decorate();
                                } else if (chartMenu.getSelectedIndex() == 2) {
                                    selectedChart = 2;
                                    decorate();
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
                        setInfoTxt();
                        revalidate();
                        repaint();
                    } else if (selectedChart == 1) {
                        colors = new ArrayList<>();
                        LinkedHashMap<String, Long> valueMap = new LinkedHashMap<>();
                        for (int s = 0; s < MyVars.seqs.length; s++) {
                            for (int i=2; i < MyVars.seqs[s].length-1; i++) {
                                if ((i+1) != MyVars.seqs[s].length) {
                                    String middleNode = MyVars.seqs[s][i].split(":")[0];
                                    middleNode = (middleNode.contains("x") ? MySysUtil.decodeVariable(middleNode) : MySysUtil.getDecodedNodeName(middleNode));
                                    if (valueMap.containsKey(middleNode)) {
                                        valueMap.put(middleNode, valueMap.get(middleNode) + 1);
                                    } else {
                                        valueMap.put(middleNode, 1L);
                                    }
                                    totalValue++;
                                    final float hue = rand.nextFloat();
                                    final float saturation = 0.9f;
                                    final float luminance = 1.0f;
                                    Color randomColor = Color.getHSBColor(hue, saturation, luminance);
                                    colors.add(randomColor);
                                }
                            }
                        }
                        valueMap = MySysUtil.sortMapByLongValue(valueMap);
                        num = MyMathUtil.getCommaSeperatedNumber(valueMap.size());
                        avg = MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(totalValue/MyVars.seqs.length));
                        int above = 0;
                        int below = 0;
                        int equal = 0;
                        for (String n : valueMap.keySet()) {
                            if (Double.parseDouble(avg) < valueMap.get(n)) {
                                above++;
                            } else if (Double.parseDouble(avg) > valueMap.get(n)) {
                                below++;
                            } else {
                                equal++;
                            }
                        }
                        equalToAvg = MyMathUtil.getCommaSeperatedNumber(equal) + "[" + MyMathUtil.twoDecimalFormat(((double)equal/valueMap.size())*100) + "%]";
                        aboveAvg = MyMathUtil.getCommaSeperatedNumber(above) + "[" + MyMathUtil.twoDecimalFormat(((double)above/valueMap.size())*100) + "%]";
                        belowAvg = MyMathUtil.getCommaSeperatedNumber(below) + "[" + MyMathUtil.twoDecimalFormat(((double)below/valueMap.size())*100) + "%]";

                        CategoryDataset dataset = new DefaultCategoryDataset();
                        JFreeChart chart = ChartFactory.createBarChart("", "", "", dataset);
                        BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();
                        ChartPanel chartPanel = new ChartPanel(chart);
                        chartPanel.setPreferredSize(new Dimension(350, 367));

                        if (MAXIMIZED) {
                            int limitCount = 0;
                            for (String label : valueMap.keySet()) {
                                if (!MAXIMIZED && limitCount == 8) break;
                                else if (MAXIMIZED && limitCount == BAR_LIMIT) break;
                                ((DefaultCategoryDataset) dataset).addValue(valueMap.get(label), label, label);
                                limitCount++;
                            }
                            chart.getCategoryPlot().getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.DOWN_45);
                            chart.removeLegend();
                        } else {
                            int limitCount = 0;
                            for (String label : valueMap.keySet()) {
                                if (!MAXIMIZED && limitCount == 8) break;
                                else if (MAXIMIZED && limitCount == BAR_LIMIT) break;
                                ((DefaultCategoryDataset) dataset).addValue(valueMap.get(label), label, "");
                                limitCount++;
                            }
                            renderer.setBaseLegendTextFont(MyVars.tahomaPlainFont10);
                        }
                        chart.getCategoryPlot().setBackgroundPaint(Color.WHITE);
                        chart.getCategoryPlot().setDomainGridlinePaint(Color.DARK_GRAY);
                        chart.getCategoryPlot().setRangeGridlinePaint(Color.DARK_GRAY);
                        chart.getCategoryPlot().getDomainAxis().setLabelFont(MyVars.tahomaPlainFont7);
                        chart.getCategoryPlot().getDomainAxis().setTickLabelFont(MyVars.tahomaPlainFont7);
                        chart.getCategoryPlot().getRangeAxis().setTickLabelFont(MyVars.tahomaPlainFont10);
                        renderer.setBarPainter(new StandardBarPainter());
                        for (int i = 0; i < dataset.getColumnCount(); i++) {
                            renderer.setSeriesPaint(i, colors.get(i));
                        }
                        //renderer.setMaximumBarWidth(0.2);

                        JLabel titleLabel = new JLabel(" INTER. N. V.");
                        titleLabel.setToolTipText("INTERMEDIATE NODE VALUE DISTRIBUTION");
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
                        chartMenu.addItem("HEAD");
                        chartMenu.addItem("INTER.");
                        chartMenu.addItem("TAIL");
                        chartMenu.setSelectedIndex(selectedChart);
                        chartMenu.addActionListener(new ActionListener() {
                            @Override public void actionPerformed(ActionEvent e) {
                                if (chartMenu.getSelectedIndex() == 0) {
                                    selectedChart = 0;
                                    decorate();
                                } else if (chartMenu.getSelectedIndex() == 1) {
                                    selectedChart = 1;
                                    decorate();
                                } else if (chartMenu.getSelectedIndex() == 2) {
                                    selectedChart = 2;
                                    decorate();
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
                        setInfoTxt();
                        revalidate();
                        repaint();
                    } else if (selectedChart == 2) {
                        colors = new ArrayList<>();
                        LinkedHashMap<String, Long> valueMap = new LinkedHashMap<>();
                        for (int s = 0; s < MyVars.seqs.length; s++) {
                            String endingNode = MyVars.seqs[s][MyVars.seqs[s].length - 1].split(":")[0];
                            endingNode = (endingNode.contains("x") ? MySysUtil.decodeVariable(endingNode) : MySysUtil.getDecodedNodeName(endingNode));
                            if (valueMap.containsKey(endingNode)) {
                                valueMap.put(endingNode, valueMap.get(endingNode) + 1);
                            } else {
                                valueMap.put(endingNode, 1L);
                            }
                            totalValue++;
                            final float hue = rand.nextFloat();
                            final float saturation = 0.9f;
                            final float luminance = 1.0f;
                            Color randomColor = Color.getHSBColor(hue, saturation, luminance);
                            colors.add(randomColor);
                        }
                        valueMap = MySysUtil.sortMapByLongValue(valueMap);
                        num = MyMathUtil.getCommaSeperatedNumber(valueMap.size());
                        avg = MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(totalValue/MyVars.seqs.length));
                        int above = 0;
                        int below = 0;
                        int equal = 0;
                        for (String n : valueMap.keySet()) {
                            if (Double.parseDouble(avg) < valueMap.get(n)) {
                                above++;
                            } else if (Double.parseDouble(avg) > valueMap.get(n)) {
                                below++;
                            } else {
                                equal++;
                            }
                        }
                        equalToAvg = MyMathUtil.getCommaSeperatedNumber(equal) + "[" + MyMathUtil.twoDecimalFormat(((double)equal/valueMap.size())*100) + "%]";
                        aboveAvg = MyMathUtil.getCommaSeperatedNumber(above) + "[" + MyMathUtil.twoDecimalFormat(((double)above/valueMap.size())*100) + "%]";
                        belowAvg = MyMathUtil.getCommaSeperatedNumber(below) + "[" + MyMathUtil.twoDecimalFormat(((double)below/valueMap.size())*100) + "%]";

                        CategoryDataset dataset = new DefaultCategoryDataset();
                        JFreeChart chart = ChartFactory.createBarChart("", "", "", dataset);
                        BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();
                        ChartPanel chartPanel = new ChartPanel(chart);
                        chartPanel.setPreferredSize(new Dimension(350, 367));

                        if (MAXIMIZED) {
                            int limitCount = 0;
                            for (String label : valueMap.keySet()) {
                                if (!MAXIMIZED && limitCount == 8) break;
                                else if (MAXIMIZED && limitCount == BAR_LIMIT) break;
                                ((DefaultCategoryDataset) dataset).addValue(valueMap.get(label), label, label);
                                limitCount++;
                            }
                            chart.getCategoryPlot().getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.DOWN_45);
                            chart.removeLegend();
                        } else {
                            int limitCount = 0;
                            for (String label : valueMap.keySet()) {
                                if (!MAXIMIZED && limitCount == 8) break;
                                else if (MAXIMIZED && limitCount == BAR_LIMIT) break;
                                ((DefaultCategoryDataset) dataset).addValue(valueMap.get(label), label, "");
                                limitCount++;
                            }
                            renderer.setBaseLegendTextFont(MyVars.tahomaPlainFont10);
                        }
                        chart.getCategoryPlot().setBackgroundPaint(Color.WHITE);
                        chart.getCategoryPlot().setDomainGridlinePaint(Color.DARK_GRAY);
                        chart.getCategoryPlot().setRangeGridlinePaint(Color.DARK_GRAY);
                        chart.getCategoryPlot().getDomainAxis().setLabelFont(MyVars.tahomaPlainFont7);
                        chart.getCategoryPlot().getDomainAxis().setTickLabelFont(MyVars.tahomaPlainFont7);
                        chart.getCategoryPlot().getRangeAxis().setTickLabelFont(MyVars.tahomaPlainFont10);
                        renderer.setBarPainter(new StandardBarPainter());
                        for (int i = 0; i < dataset.getColumnCount(); i++) {
                            renderer.setSeriesPaint(i, colors.get(i));
                        }
                        //renderer.setMaximumBarWidth(0.2);

                        JLabel titleLabel = new JLabel(" TAIL. N. V.");
                        titleLabel.setToolTipText("TAIL NODE VALUE DISTRIBUTION");
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
                        chartMenu.addItem("HEAD");
                        chartMenu.addItem("INTER.");
                        chartMenu.addItem("TAIL");
                        chartMenu.setSelectedIndex(selectedChart);
                        chartMenu.addActionListener(new ActionListener() {
                            @Override public void actionPerformed(ActionEvent e) {
                                if (chartMenu.getSelectedIndex() == 0) {
                                    selectedChart = 0;
                                    decorate();
                                } else if (chartMenu.getSelectedIndex() == 1) {
                                    selectedChart = 1;
                                    decorate();
                                } else if (chartMenu.getSelectedIndex() == 2) {
                                    selectedChart = 2;
                                    decorate();
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
                        setInfoTxt();
                        revalidate();
                        repaint();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private static boolean MAXIMIZED = false;
    private static JLabel avgLabel = new JLabel();


    public void enlarge() {
        MAXIMIZED = true;
        JFrame f = new JFrame(" NODE VALUE BY POSITION DISTRIBUTION");
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        p.setLayout(new BorderLayout(3,3));
        f.setLayout(new BorderLayout(3,3));
        f.setBackground(Color.WHITE);
        avgLabel.setHorizontalAlignment(JLabel.RIGHT);
        avgLabel.setFont(MyVars.tahomaPlainFont12);
        avgLabel.setBackground(Color.WHITE);
        p.add(avgLabel, BorderLayout.NORTH);
        p.add(new MyGraphLevelNodeValueByPositionHistogramDistributionLineChart(), BorderLayout.CENTER);
        f.getContentPane().add(p, BorderLayout.CENTER);
        f.setPreferredSize(new Dimension(450, 350));
        f.pack();
        f.setVisible(true);
        f.addWindowListener(new WindowListener() {
            @Override public void windowOpened(WindowEvent e) {}
            @Override public void windowClosing(WindowEvent e) {MAXIMIZED = false;}
            @Override public void windowClosed(WindowEvent e) {}
            @Override public void windowIconified(WindowEvent e) {}
            @Override public void windowDeiconified(WindowEvent e) {}
            @Override public void windowActivated(WindowEvent e) {}
            @Override public void windowDeactivated(WindowEvent e) {}
        });
    }

    private void setInfoTxt() {
        avgLabel.setText("N.: " + num + "   " + "AVG.: " + avg + "   " + "BELOW AVG.: " + belowAvg + "   " + "EQUAL TO AVG.: " + equalToAvg + "   " + "ABOVE AVG.: " + aboveAvg + " ");
    }

    @Override public void actionPerformed(ActionEvent e) {

    }

}
