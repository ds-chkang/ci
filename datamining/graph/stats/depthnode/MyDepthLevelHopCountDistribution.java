package datamining.graph.stats.depthnode;

import datamining.utils.MyMathUtil;
import datamining.utils.system.MySysUtil;
import datamining.utils.system.MyVars;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TreeMap;

public class MyDepthLevelHopCountDistribution
extends JPanel {

    public static int instances = 0;
    public MyDepthLevelHopCountDistribution() {
        this.decorate();
    }

    public void decorate() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                setLayout(new BorderLayout(3,3));
                setBackground(Color.WHITE);

                ChartPanel chartPanel = new ChartPanel(setChart());
                chartPanel.getChart().getCategoryPlot().setRangeGridlinePaint(Color.DARK_GRAY);
                chartPanel.getChart().getCategoryPlot().setDomainGridlinePaint(Color.DARK_GRAY);
                chartPanel.getChart().getCategoryPlot().setBackgroundPaint(Color.WHITE);
                chartPanel.getChart().getCategoryPlot().getDomainAxis().setTickLabelFont(MyVars.tahomaPlainFont10);
                chartPanel.getChart().getCategoryPlot().getDomainAxis().setLabelFont(MyVars.tahomaPlainFont10);
                chartPanel.getChart().getCategoryPlot().getRangeAxis().setTickLabelFont(MyVars.tahomaPlainFont10);
                chartPanel.getChart().getCategoryPlot().getRangeAxis().setLabelFont(MyVars.tahomaPlainFont10);
                chartPanel.getChart().getCategoryPlot().getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());

                CategoryAxis domainAxis = chartPanel.getChart().getCategoryPlot().getDomainAxis();
                domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);

                BarRenderer barRenderer = (BarRenderer)chartPanel.getChart().getCategoryPlot().getRenderer();
                barRenderer.setSeriesPaint(0, new Color(0, 0, 0f, 0.23f));
                barRenderer.setShadowPaint(Color.WHITE);
                barRenderer.setBaseFillPaint(Color.decode("#07CF61"));
                barRenderer.setBarPainter(new StandardBarPainter());
                barRenderer.setBaseLegendTextFont(MyVars.tahomaPlainFont10);

                JLabel titleLabel = new JLabel(" DEPTHNODE H.");
                titleLabel.setFont(MyVars.tahomaBoldFont11);
                titleLabel.setBackground(Color.WHITE);
                titleLabel.setForeground(Color.DARK_GRAY);

                JPanel titlePanel = new JPanel();
                titlePanel.setBackground(Color.WHITE);
                titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
                titlePanel.add(titleLabel);

                JButton enlargeBtn = new JButton("+");
                enlargeBtn.setBackground(Color.WHITE);
                enlargeBtn.setFont(MyVars.tahomaPlainFont10);
                enlargeBtn.setFocusable(false);
                enlargeBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        new Thread(new Runnable() {
                            @Override public void run() {
                                enlarge();
                            }
                        }).start();
                    }
                });

                JPanel enlargePanel = new JPanel();
                enlargePanel.setBackground(Color.WHITE);
                enlargePanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3,3));
                enlargePanel.add(enlargeBtn);

                JPanel topPanel = new JPanel();
                topPanel.setBackground(Color.WHITE);
                topPanel.setLayout(new BorderLayout(3,3));
                topPanel.add(titlePanel, BorderLayout.WEST);
                topPanel.add(enlargePanel, BorderLayout.CENTER);

                setBackground(Color.WHITE);
                add(chartPanel, BorderLayout.CENTER);
                //if (instances == 0) {
                    add(topPanel, BorderLayout.NORTH);
               // }
                instances++;
                revalidate();
                repaint();
            }
        });
    }

    public void enlarge() {
        JFrame frame = new JFrame(" DEPTH NODE HOP DISTRIBUTION");
        frame.setLayout(new BorderLayout(3,3));
        frame.getContentPane().add(new MyDepthLevelHopCountDistribution(), BorderLayout.CENTER);
        frame.setPreferredSize(new Dimension(400, 300));
        frame.pack();
        frame.setVisible(true);
    }

    private JFreeChart setChart() {
        TreeMap<Integer, Integer> nodeHopCountByNodeMap = new TreeMap<>();
        for (int s = 0; s < MyVars.seqs.length; s++) {
            for (int itemsetIdx = 0; itemsetIdx < MyVars.seqs[s].length; itemsetIdx++) {
                String nn = MyVars.seqs[s][itemsetIdx].split(":")[0];
                if (MyVars.getViewer().vc.depthNodeNameSet.contains(nn)) {
                    int hopLength = (MyVars.seqs[s].length-(itemsetIdx+1));
                    if (nodeHopCountByNodeMap.containsKey(hopLength)) {
                        nodeHopCountByNodeMap.put(hopLength, nodeHopCountByNodeMap.get(hopLength)+1);
                    } else {
                        nodeHopCountByNodeMap.put(hopLength, 1);
                    }
                }
            }
        }

        int totalLengths = 0;
        int totalCount = 0;
        for (Integer hopLength : nodeHopCountByNodeMap.keySet()) {
            totalLengths += nodeHopCountByNodeMap.get(hopLength)*hopLength;
            totalCount += nodeHopCountByNodeMap.get(hopLength);
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Integer hopCount : nodeHopCountByNodeMap.keySet()) {
            dataset.addValue(nodeHopCountByNodeMap.get(hopCount), "HOP", hopCount);
        }

        String plotTitle = "";
        String xaxis = " HOP LENGTH[AVG.: " + MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat((double)totalLengths/totalCount)) + "]";
        String yaxis = "";
        PlotOrientation orientation = PlotOrientation.VERTICAL;
        boolean show = false;
        boolean toolTips = true;
        boolean urls = false;
        return ChartFactory.createBarChart(plotTitle, xaxis, yaxis, dataset, orientation, show, toolTips, urls);
    }
}
