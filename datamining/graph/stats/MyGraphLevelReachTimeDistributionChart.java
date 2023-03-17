package datamining.graph.stats;

import datamining.main.MyProgressBar;
import datamining.utils.MyMathUtil;
import datamining.utils.system.MyVars;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class MyGraphLevelReachTimeDistributionChart
extends JPanel {

   private int startDepth = 0;
   private int endDepth = 0;

    public MyGraphLevelReachTimeDistributionChart() {
        this.decorate();
    }

    private void decorate() {
        this.setLayout(new BorderLayout(5,5));
        this.setBackground(Color.WHITE);

        CategoryPlot plot = new CategoryPlot();

        // Add the second dataset and render as lines
        CategoryItemRenderer lineAndShapeRenderer = new LineAndShapeRenderer();
        plot.setDataset(0, setLineCharts());
        plot.setRenderer(0, lineAndShapeRenderer);
        lineAndShapeRenderer.setBaseSeriesVisible(true);
        lineAndShapeRenderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());


        // Add the first dataset and render as bar
        CategoryItemRenderer barRenderer = new BarRenderer();
        plot.setDataset(1, setBarCharts());
        plot.setRenderer(1, barRenderer);

        ((BarRenderer)barRenderer).setBarPainter(new StandardBarPainter());
        ((BarRenderer)barRenderer).setShadowPaint(new Color(0.0f, 0.0f, 0.0f, 0.0f));
        ((BarRenderer)barRenderer).setSeriesPaint(0, new Color(1.0f, 1.0f, 1.0f, 0.09f));
        ((BarRenderer)barRenderer).setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
        ((BarRenderer)barRenderer).setBaseLegendTextFont(MyVars.tahomaPlainFont11);

        // Set Axis
        plot.setDomainAxis(new CategoryAxis("REACH TIME"));
        plot.setRangeAxis(new NumberAxis("COUNT"));
        plot.getRangeAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
        plot.setDomainGridlinePaint(Color.DARK_GRAY);
        plot.setBackgroundPaint(Color.DARK_GRAY);
        plot.getRangeAxis().setTickLabelFont(MyVars.tahomaPlainFont11);
        plot.getDomainAxis().setTickLabelFont(MyVars.tahomaPlainFont11);
        plot.setOutlineVisible(true);
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);

        JFreeChart chart = new JFreeChart(plot);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.getChart().setBackgroundPaint(Color.WHITE);


        this.add(this.setMenuPanel(), BorderLayout.NORTH);
        this.add(chartPanel, BorderLayout.CENTER);
    }

    private JPanel setMenuPanel() {
        JLabel depthTimeDistributionMenuLabel = new JLabel("  TIME DISTRIBUTION BY DEPTH: ");
        depthTimeDistributionMenuLabel.setBackground(Color.WHITE);
        depthTimeDistributionMenuLabel.setFont(MyVars.tahomaPlainFont12);

        JComboBox depthTimeDistributionMenu = new JComboBox();
        depthTimeDistributionMenu.setFont(MyVars.tahomaPlainFont12);
        depthTimeDistributionMenu.setBackground(Color.WHITE);
        depthTimeDistributionMenu.setFocusable(false);

        depthTimeDistributionMenu.addItem("ALL");
        for (int i=this.startDepth; i <= this.endDepth; i++) {
            depthTimeDistributionMenu.addItem("DEPTH-" + i);
        }

        depthTimeDistributionMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == depthTimeDistributionMenu) {

                }
            }
        });

        JLabel timeConversionMenuLabel = new JLabel("TIME CONVERSION: ");
        timeConversionMenuLabel.setBackground(Color.WHITE);
        timeConversionMenuLabel.setFont(MyVars.tahomaPlainFont12);

        JComboBox timeConversionMenu = new JComboBox();
        timeConversionMenu.setFont(MyVars.tahomaPlainFont12);
        timeConversionMenu.setBackground(Color.WHITE);
        timeConversionMenu.setFocusable(false);

        timeConversionMenu.addItem("SECENDS");
        timeConversionMenu.addItem("MINUTES");
        timeConversionMenu.addItem("HOURS");

        timeConversionMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == timeConversionMenu) {
                    //System.out.println(timeConversionMenu.getSelectedIndex());
                }
            }
        });

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        menuPanel.setBackground(Color.WHITE);
        menuPanel.add(timeConversionMenuLabel);
        menuPanel.add(timeConversionMenu);
        menuPanel.add(depthTimeDistributionMenuLabel);
        menuPanel.add(depthTimeDistributionMenu);
        return menuPanel;
    }

    private DefaultCategoryDataset setLineCharts() {
        //2. Add Line Chart.
        Map<Integer, Map<Long, Integer>> durationByDepthMap = new HashMap<>();
        for (int sequece = 0; sequece < MyVars.seqs.length; sequece++) {
            for (int i = 2; i < MyVars.seqs[sequece].length; i++) {
                long time = Long.valueOf(MyVars.seqs[sequece][i].split(":")[1]);
                if (durationByDepthMap.containsKey(i)) {
                    if (durationByDepthMap.get(i).containsKey(time)) {
                        Map<Long, Integer> depthDurationMap = durationByDepthMap.get(i);
                        depthDurationMap.put(time, depthDurationMap.get(time) + 1);
                        durationByDepthMap.put(i, depthDurationMap);
                    } else {
                        Map<Long, Integer> depthDurationMap = durationByDepthMap.get(i);
                        depthDurationMap.put(time, 1);
                        durationByDepthMap.put(i, depthDurationMap);
                    }
                } else {
                    Map<Long, Integer> depthDurationMap = new HashMap<>();
                    depthDurationMap.put(time, 1);
                    durationByDepthMap.put(i, depthDurationMap);
                }
            }
        }

        this.startDepth = 2;
        this.endDepth = this.startDepth + (durationByDepthMap.size()-1);

        TreeMap<Integer, TreeMap<Long, Integer>> sortedDurationByDepthMap = new TreeMap<>();
        for (Integer depth : durationByDepthMap.keySet()) {
            TreeMap<Long, Integer> depthDurationMap = new TreeMap<>(durationByDepthMap.get(depth));
            sortedDurationByDepthMap.put(depth, depthDurationMap);
        }

        DefaultCategoryDataset lineDataSet = new DefaultCategoryDataset();
        for (Integer depth : sortedDurationByDepthMap.keySet()) {
            TreeMap<Long, Integer> sortedDepthDurationMap = sortedDurationByDepthMap.get(depth);
            for (Long duration : sortedDepthDurationMap.keySet()) {
                lineDataSet.addValue(sortedDepthDurationMap.get(duration), "DEPTH-" + depth, MyMathUtil.getCommaSeperatedNumber(duration));
            }

        }
        return lineDataSet;
    }

    private DefaultCategoryDataset setBarCharts() {
        String series = "REACH TIME";
        //1. Add Distribution Bar Chart.
        Map<Long, Integer> durationMap = new HashMap<>();
        for (int sequece = 0; sequece < MyVars.seqs.length; sequece++) {
            for (int i = 2; i < MyVars.seqs[sequece].length; i++) {
                long time = Long.valueOf(MyVars.seqs[sequece][i].split(":")[1]);
                if (durationMap.containsKey(time)) {
                    durationMap.put(time, durationMap.get(time)+1);
                } else {
                    durationMap.put(time, 1);
                }
            }
        }

        TreeMap<Long, Integer> sortedDurationMap = new TreeMap<>();
        sortedDurationMap.putAll(durationMap);

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Long duration : durationMap.keySet()) {
            dataset.addValue(durationMap.get(duration), series, MyMathUtil.getCommaSeperatedNumber(duration));
        }
        return dataset;
    }

    public void enlarge() {
        MyProgressBar pb = new MyProgressBar(false);
        final JFrame frame = new JFrame("REACH TIME DISTRIBUTION");
        frame.setLayout(new BorderLayout());
        pb.updateValue(20, 100);

        MyGraphLevelReachTimeDistributionChart reachTimeDistributionChart = new MyGraphLevelReachTimeDistributionChart();
        frame.getContentPane().add(reachTimeDistributionChart, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setPreferredSize(new Dimension(400, 450));
        frame.pack();
        pb.updateValue(60, 100);

        Border blackline = BorderFactory.createLineBorder(Color.black);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(blackline, "");
        titledBorder.setTitleJustification(TitledBorder.LEFT);
        titledBorder.setTitleFont(new Font("Arial", Font.PLAIN, 0));
        titledBorder.setTitleColor(Color.DARK_GRAY);
        reachTimeDistributionChart.setBorder(titledBorder);
        pb.updateValue(80, 100);

        frame.setCursor(Cursor.HAND_CURSOR);
        frame.setMaximumSize(new Dimension(1300, 1200));
        frame.setMinimumSize(new Dimension(600, 600));
        pb.updateValue(100, 100);
        pb.dispose();

        frame.setVisible(true);
    }
}
