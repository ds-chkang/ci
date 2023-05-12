package medousa.sequential.graph.stats;

import medousa.MyProgressBar;
import medousa.sequential.utils.MyMathUtil;
import medousa.sequential.utils.MySequentialGraphSysUtil;
import medousa.sequential.utils.MySequentialGraphVars;
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

public class MyGraphLevelSequenceTotalTimeDistribution
extends JPanel {


    public MyGraphLevelSequenceTotalTimeDistribution() {
        this.decorate();
        JFrame f = new JFrame(" SEQUENCE TOTAL TIME DISTRIBUTION");
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
                    chartPanel.getChart().getCategoryPlot().getDomainAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
                    chartPanel.getChart().getCategoryPlot().getDomainAxis().setLabelFont(MySequentialGraphVars.tahomaPlainFont11);
                    chartPanel.getChart().getCategoryPlot().getRangeAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
                    chartPanel.getChart().getCategoryPlot().getRangeAxis().setLabelFont(MySequentialGraphVars.tahomaPlainFont11);

                    CategoryAxis domainAxis = chartPanel.getChart().getCategoryPlot().getDomainAxis();
                    domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);

                    BarRenderer barRenderer = (BarRenderer) chartPanel.getChart().getCategoryPlot().getRenderer();
                    barRenderer.setSeriesPaint(0, new Color(0, 0, 0f, 0.23f));
                    //barRenderer.setSeriesPaint(0,  Color.RED);
                    barRenderer.setShadowPaint(Color.WHITE);
                    barRenderer.setBaseFillPaint(Color.DARK_GRAY);
                    barRenderer.setBarPainter(new StandardBarPainter());
                    barRenderer.setBaseLegendTextFont(MySequentialGraphVars.tahomaPlainFont11);

                    JLabel titleLabel = new JLabel(" SEQUENCE TOTAL TIME DISTRIBUTION");
                    titleLabel.setFont(MySequentialGraphVars.tahomaBoldFont12);
                    titleLabel.setBackground(Color.WHITE);
                    titleLabel.setForeground(Color.DARK_GRAY);

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
        MyProgressBar pb = new MyProgressBar(false);
        try {
            JFrame f = new JFrame(" SEQUENCE TOTAL TIME DISTRIBUTION");
            f.setLayout(new BorderLayout(3, 3));
            f.getContentPane().add(new ChartPanel(setChart()), BorderLayout.CENTER);
            f.setPreferredSize(new Dimension(350, 350));
            f.pack();
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.setAlwaysOnTop(true);
            pb.updateValue(100, 100);
            pb.dispose();
            f.setVisible(true);
            f.setAlwaysOnTop(false);
        } catch (Exception ex) {
            pb.updateValue(100, 100);
            pb.dispose();
        }
    }

    private JFreeChart setChart() {
        long totalTime = 0L;
        TreeMap<Long, Integer> sequenceTimeMap = new TreeMap<>();
        for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
            long totalSequenceTime = 0L;
            for (int i=1; i < MySequentialGraphVars.seqs[s].length; i++) {
                long time = (Long.parseLong(MySequentialGraphVars.seqs[s][i].split(":")[1]) / 60);
                totalSequenceTime += time;
                totalTime += time;
            }

            if (sequenceTimeMap.containsKey(totalSequenceTime)) {
                sequenceTimeMap.put(totalSequenceTime, sequenceTimeMap.get(totalSequenceTime)+1);
            } else {
                sequenceTimeMap.put(totalSequenceTime, 1);
            }
        }
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Long sequenceToTalTime : sequenceTimeMap.keySet()) {
            dataset.addValue(sequenceTimeMap.get(sequenceToTalTime), "SEQUENCE TOTAL TIME", sequenceToTalTime);
        }
        double avgLength = (double) totalTime/MySequentialGraphVars.seqs.length;
        String plotTitle = "";
        String xaxis = "[AVG.: " + MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(avgLength)) + "]";
        String yaxis = "";
        PlotOrientation orientation = PlotOrientation.VERTICAL;
        boolean show = false;
        boolean toolTips = true;
        boolean urls = false;
        return ChartFactory.createBarChart(plotTitle, xaxis, yaxis, dataset, orientation, show, toolTips, urls);
    }



}
