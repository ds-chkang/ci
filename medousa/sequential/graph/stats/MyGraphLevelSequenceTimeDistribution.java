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

public class MyGraphLevelSequenceTimeDistribution
extends JPanel {

    private float toTime = 0f;
    private int selectedMenu = 0;

    public MyGraphLevelSequenceTimeDistribution() {
        try {
            this.decorate();
            JFrame f = new JFrame(" SEQUENCE TIME DISTRIBUTION");
            f.setPreferredSize(new Dimension(350, 350));
            f.setBackground(Color.WHITE);
            f.setLayout(new BorderLayout(3, 3));
            f.add(this, BorderLayout.CENTER);
            f.pack();
            f.setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void decorate() {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            removeAll();
            setLayout(new BorderLayout(3, 3));
            setBackground(Color.WHITE);

            ChartPanel chartPanel = new ChartPanel(setChart());
            chartPanel.getChart().getCategoryPlot().setRangeGridlinePaint(Color.DARK_GRAY);
            chartPanel.getChart().getCategoryPlot().setDomainGridlinePaint(Color.DARK_GRAY);
            chartPanel.getChart().getCategoryPlot().setBackgroundPaint(Color.WHITE);
            chartPanel.getChart().getCategoryPlot().getDomainAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont12);
            chartPanel.getChart().getCategoryPlot().getDomainAxis().setLabelFont(MySequentialGraphVars.tahomaPlainFont12);
            chartPanel.getChart().getCategoryPlot().getRangeAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont12);
            chartPanel.getChart().getCategoryPlot().getRangeAxis().setLabelFont(MySequentialGraphVars.tahomaPlainFont12);

            CategoryAxis domainAxis = chartPanel.getChart().getCategoryPlot().getDomainAxis();
            domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);

            BarRenderer barRenderer = (BarRenderer) chartPanel.getChart().getCategoryPlot().getRenderer();
            barRenderer.setSeriesPaint(0, new Color(0, 0, 0f, 0.23f));
            barRenderer.setShadowPaint(Color.WHITE);
            barRenderer.setBaseFillPaint(Color.DARK_GRAY);
            barRenderer.setBarPainter(new StandardBarPainter());
            barRenderer.setBaseLegendTextFont(MySequentialGraphVars.tahomaPlainFont12);

            JLabel titleLabel = new JLabel(" SEQUENCE TIME DISTRIBUTION");
            titleLabel.setFont(MySequentialGraphVars.tahomaBoldFont12);
            titleLabel.setBackground(Color.WHITE);
            titleLabel.setForeground(Color.DARK_GRAY);

            JComboBox timeConvertMenu = new JComboBox();
            timeConvertMenu.addItem("SECOND");
            timeConvertMenu.addItem("MINUTE");
            timeConvertMenu.addItem("HOUR");
            timeConvertMenu.setSelectedIndex(selectedMenu);
            timeConvertMenu.setFont(MySequentialGraphVars.tahomaPlainFont12);
            timeConvertMenu.setFocusable(false);
            timeConvertMenu.setBackground(Color.WHITE);
            timeConvertMenu.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new Thread(new Runnable() {
                        @Override public void run() {
                            if (timeConvertMenu.getSelectedIndex() == 0) {
                                toTime = 0;
                                selectedMenu = 0;
                                decorate();
                            } else if (timeConvertMenu.getSelectedIndex() == 1) {
                                selectedMenu = 1;
                                toTime = 60;
                                decorate();
                            } else if (timeConvertMenu.getSelectedIndex() == 2) {
                                toTime = 3600;
                                selectedMenu = 2;
                                decorate();
                            }
                        }
                    }).start();
                }
            });

            JPanel timeConvertMenuPanel = new JPanel();
            timeConvertMenuPanel.setBackground(Color.WHITE);
            timeConvertMenuPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 3));
            timeConvertMenuPanel.add(timeConvertMenu);

            JPanel topPanel = new JPanel();
            topPanel.setLayout(new BorderLayout(3, 3));
            topPanel.setBackground(Color.WHITE);
            topPanel.add(titleLabel, BorderLayout.WEST);
            topPanel.add(timeConvertMenuPanel, BorderLayout.EAST);
            add(topPanel, BorderLayout.NORTH);
            add(chartPanel, BorderLayout.CENTER);

            pb.updateValue(100, 100);
            pb.dispose();

            revalidate();
            repaint();
        } catch (Exception ex) {
            ex.printStackTrace();
            pb.updateValue(100, 100);
            pb.dispose();
        }
    }

    private JFreeChart setChart() {
        long totalTime = 0L;
        TreeMap<Long, Integer> sequenceTimeMap = new TreeMap<>();
        int i = (MySequentialGraphVars.isSupplementaryOn ? 2 : 1);
        for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
            for (; i < MySequentialGraphVars.seqs[s].length; i++) {
                long time = (Long.parseLong(MySequentialGraphVars.seqs[s][i].split(":")[1]));
                if (this.toTime > 0) {
                    long convertedTime = (long) ((float) time/this.toTime);
                    if (sequenceTimeMap.containsKey(convertedTime)) {
                        sequenceTimeMap.put(convertedTime, sequenceTimeMap.get(convertedTime)+1);
                    } else {
                        sequenceTimeMap.put(convertedTime, 1);
                    }
                } else {
                    if (sequenceTimeMap.containsKey(time)) {
                        sequenceTimeMap.put(time, sequenceTimeMap.get(time)+1);
                    } else {
                        sequenceTimeMap.put(time, 1);
                    }
                }
            }
            i = (MySequentialGraphVars.isSupplementaryOn ? 2 : 1);
        }
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Long sequenceToTalTime : sequenceTimeMap.keySet()) {
            dataset.addValue(sequenceTimeMap.get(sequenceToTalTime), "TIME", sequenceToTalTime);
        }
        double avgLength = (double) totalTime/MySequentialGraphVars.seqs.length;
        String plotTitle = "";
        String xaxis = "TIME";
        String yaxis = "";
        PlotOrientation orientation = PlotOrientation.VERTICAL;
        boolean show = false;
        boolean toolTips = true;
        boolean urls = false;
        return ChartFactory.createBarChart(plotTitle, xaxis, yaxis, dataset, orientation, show, toolTips, urls);
    }



}
