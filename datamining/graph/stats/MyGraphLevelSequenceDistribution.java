package datamining.graph.stats;

import datamining.utils.MyMathUtil;
import datamining.utils.system.MySysUtil;
import datamining.utils.system.MyVars;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TreeMap;

public class MyGraphLevelSequenceDistribution
extends JPanel {

    protected JFrame distFrame = null;

    public MyGraphLevelSequenceDistribution() {
        this.decorate();
        JFrame f = new JFrame(" SEQUENCE LENGTH DISTRIBUTION");
        f.setPreferredSize(new Dimension(350, 350));
        f.setBackground(Color.WHITE);
        f.setLayout(new BorderLayout(3,3));
        f.add(this, BorderLayout.CENTER);
        f.pack();
        f.setVisible(true);
    }

    private void decorate() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                try {
                    removeAll();
                    setLayout(new BorderLayout(3, 3));
                    setBackground(Color.WHITE);

                    ChartPanel chartPanel = new ChartPanel(setChart());
                    chartPanel.getChart().getCategoryPlot().setRangeGridlinePaint(Color.DARK_GRAY);
                    chartPanel.getChart().getCategoryPlot().setDomainGridlinePaint(Color.DARK_GRAY);
                    chartPanel.getChart().getCategoryPlot().setBackgroundPaint(Color.WHITE);
                    chartPanel.getChart().getCategoryPlot().getDomainAxis().setTickLabelFont(MyVars.tahomaPlainFont11);
                    chartPanel.getChart().getCategoryPlot().getDomainAxis().setLabelFont(MyVars.tahomaPlainFont11);
                    chartPanel.getChart().getCategoryPlot().getRangeAxis().setTickLabelFont(MyVars.tahomaPlainFont11);
                    chartPanel.getChart().getCategoryPlot().getRangeAxis().setLabelFont(MyVars.tahomaPlainFont11);

                    CategoryAxis domainAxis = chartPanel.getChart().getCategoryPlot().getDomainAxis();
                    domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);

                    BarRenderer barRenderer = (BarRenderer) chartPanel.getChart().getCategoryPlot().getRenderer();
                    barRenderer.setSeriesPaint(0, new Color(0, 0, 0f, 0.23f));
                    //barRenderer.setSeriesPaint(0,  Color.RED);
                    barRenderer.setShadowPaint(Color.WHITE);
                    barRenderer.setBaseFillPaint(Color.decode("#07CF61"));
                    barRenderer.setBarPainter(new StandardBarPainter());
                    barRenderer.setBaseLegendTextFont(MyVars.tahomaPlainFont11);

                    JLabel titleLabel = new JLabel(" SEQUENCES");
                    titleLabel.setFont(MyVars.tahomaBoldFont11);
                    titleLabel.setBackground(Color.WHITE);
                    titleLabel.setForeground(Color.DARK_GRAY);

                    JButton enlargeBtn = new JButton("+");
                    enlargeBtn.setFont(MyVars.tahomaPlainFont10);
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

                    JPanel enlargePanel = new JPanel();
                    enlargePanel.setBackground(Color.WHITE);
                    enlargePanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 3));
                    enlargePanel.add(enlargeBtn);

                    JPanel topPanel = new JPanel();
                    topPanel.setLayout(new BorderLayout(3, 3));
                    topPanel.setBackground(Color.WHITE);
                    topPanel.add(titleLabel, BorderLayout.WEST);
                    topPanel.add(enlargePanel, BorderLayout.EAST);
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

    public void enlarge() {
        setLayout(new BorderLayout(3,3));
        setBackground(Color.WHITE);

        ChartPanel chartPanel = new ChartPanel(setChart());
        chartPanel.getChart().getCategoryPlot().setRangeGridlinePaint(Color.DARK_GRAY);
        chartPanel.getChart().getCategoryPlot().setDomainGridlinePaint(Color.DARK_GRAY);
        chartPanel.getChart().getCategoryPlot().setBackgroundPaint(Color.WHITE);
        chartPanel.getChart().getCategoryPlot().getDomainAxis().setTickLabelFont(MyVars.tahomaPlainFont11);
        chartPanel.getChart().getCategoryPlot().getDomainAxis().setLabelFont(MyVars.tahomaPlainFont11);
        chartPanel.getChart().getCategoryPlot().getRangeAxis().setTickLabelFont(MyVars.tahomaPlainFont11);
        chartPanel.getChart().getCategoryPlot().getRangeAxis().setLabelFont(MyVars.tahomaPlainFont11);

        CategoryAxis domainAxis = chartPanel.getChart().getCategoryPlot().getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);

        BarRenderer barRenderer = (BarRenderer)chartPanel.getChart().getCategoryPlot().getRenderer();
        //barRenderer.setSeriesPaint(0,  new Color(0, 0, 0f, 0.23f));
        barRenderer.setSeriesPaint(0,  new Color(0, 0, 0f, 0.23f));
        barRenderer.setShadowPaint(Color.WHITE);
        barRenderer.setBaseFillPaint(Color.decode("#07CF61"));
        barRenderer.setBarPainter(new StandardBarPainter());
        barRenderer.setBaseLegendTextFont(MyVars.tahomaPlainFont11);

        JFrame frame = new JFrame(" SEQUENCE DISTRIBUTION");
        frame.setLayout(new BorderLayout(3,3));
        frame.getContentPane().add(chartPanel, BorderLayout.CENTER);
        frame.setPreferredSize(new Dimension(350, 350));
        frame.pack();
        frame.setVisible(true);
    }

    private JFreeChart setChart() {
        int totalLength = 0;
        TreeMap<Integer, Integer> seqLengthMap = new TreeMap<>();
        for (int s=0; s < MyVars.seqs.length; s++) {
            totalLength += MyVars.seqs[s].length;
            if (seqLengthMap.containsKey(MyVars.seqs[s].length)) {
                seqLengthMap.put(MyVars.seqs[s].length, seqLengthMap.get(MyVars.seqs[s].length)+1);
            } else {seqLengthMap.put(MyVars.seqs[s].length, 1);}
        }
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Integer contributionCount : seqLengthMap.keySet()) {
            dataset.addValue(seqLengthMap.get(contributionCount), "SEQUENCE LENGTH", contributionCount);
        }
        double avgLength = (double)totalLength/MyVars.seqs.length;
        String plotTitle = "";
        String xaxis = "SEQUENCE LENGTH[AVG.: " + MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(avgLength)) + "]";
        String yaxis = "";
        PlotOrientation orientation = PlotOrientation.VERTICAL;
        boolean show = false;
        boolean toolTips = true;
        boolean urls = false;
        return ChartFactory.createBarChart(plotTitle, xaxis, yaxis, dataset, orientation, show, toolTips, urls);
    }



}
