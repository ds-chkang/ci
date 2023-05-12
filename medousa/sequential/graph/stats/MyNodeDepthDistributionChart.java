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
import org.jfree.ui.HorizontalAlignment;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MyNodeDepthDistributionChart
extends JPanel {

    public MyNodeDepthDistributionChart() {}

    private Map<Integer, Integer> getNodeDepthDistribution() {
        Map<Integer, Integer> nodeDepthCountMap = new HashMap<>();
        try {
            if (!MySequentialGraphVars.getSequentialGraphViewer().selectedNode.getName().contains("x")) {
                for (int sequence = 0; sequence < MySequentialGraphVars.seqs.length; sequence++) {
                    for (int i = 1; i < MySequentialGraphVars.seqs[sequence].length; i++) {
                        String itemset = (MySequentialGraphVars.isTimeOn ? MySequentialGraphVars.seqs[sequence][i].split(":")[0] : MySequentialGraphVars.seqs[sequence][i]);
                        if (itemset.equals(MySequentialGraphVars.getSequentialGraphViewer().selectedNode.getName())) {
                            if (nodeDepthCountMap.containsKey(i)) {
                                nodeDepthCountMap.put(i, nodeDepthCountMap.get(i)+1);
                            } else {
                                nodeDepthCountMap.put(i, 1);
                            }
                        }
                    }
                }
            } else {
                for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                    String itemset = (MySequentialGraphVars.isTimeOn ? MySequentialGraphVars.seqs[s][0].split(":")[0] : MySequentialGraphVars.seqs[s][0]);
                    if (itemset.equals(MySequentialGraphVars.getSequentialGraphViewer().selectedNode.getName())) {
                        if (nodeDepthCountMap.containsKey(1)) {
                            nodeDepthCountMap.put(1, nodeDepthCountMap.get(1)+1);
                        } else {
                            nodeDepthCountMap.put(1, 1);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return nodeDepthCountMap;
    }

    private DefaultCategoryDataset getDataSet(Map<Integer, Integer> nodeDepthCountMap) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        try {
            for (int j = 1; j < MySequentialGraphVars.mxDepth; j++) {
                if (nodeDepthCountMap.get(j) != null) {
                    dataset.addValue(nodeDepthCountMap.get(j), "DEPTH", String.valueOf(j));
                } else {
                    dataset.addValue(0, "DEPTH", String.valueOf(j));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return dataset;
    }

    private int getTotalDepthAppearnace(Map<Integer, Integer> nodeDepthCountMap) {
        int totalDepthAppearance = 0;
        try {
            for (int j = 1; j < MySequentialGraphVars.mxDepth; j++) {
                if (nodeDepthCountMap.get(j) != null) {
                    totalDepthAppearance += nodeDepthCountMap.get(j);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return totalDepthAppearance;
    }

    private void decorate() {
        try {
            this.setLayout(new BorderLayout(5, 5));
            this.setBackground(Color.WHITE);
            Map<Integer, Integer> nodeDepthCountMap = this.getNodeDepthDistribution();
            DefaultCategoryDataset dataset = this.getDataSet(nodeDepthCountMap);
            int toalDepthAppearance = this.getTotalDepthAppearnace(nodeDepthCountMap);

            String avgDepthAppearance = "";
            if (!MySequentialGraphVars.getSequentialGraphViewer().selectedNode.getName().contains("x")) {
                avgDepthAppearance = MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat((double) toalDepthAppearance/(MySequentialGraphVars.mxDepth - 1)));
            } else {
                avgDepthAppearance = MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat((double) toalDepthAppearance/ MySequentialGraphVars.sequeceFeatureCount));
            }

            JFreeChart chart = ChartFactory.createBarChart("",
                    "DEPTH[AVG. NODE BY DEPTH APPEARANCE: " + avgDepthAppearance + "]", "COUNT",
                    dataset, PlotOrientation.VERTICAL, true, true, false);
            chart.setTitle("");
            chart.removeLegend();
            chart.getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
            chart.getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
            chart.setBackgroundPaint(Color.WHITE);
            chart.getCategoryPlot().getDomainAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
            chart.getCategoryPlot().getDomainAxis().setLabelFont(MySequentialGraphVars.tahomaPlainFont11);
            chart.getCategoryPlot().getRangeAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
            chart.getCategoryPlot().getRangeAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
            chart.getCategoryPlot().setBackgroundPaint(Color.WHITE);
            chart.getCategoryPlot().setDomainGridlinePaint(Color.DARK_GRAY);
            chart.getCategoryPlot().setRangeGridlinePaint(Color.DARK_GRAY);
            CategoryAxis domainAxis = chart.getCategoryPlot().getDomainAxis();
            domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);

            BarRenderer barRenderer = (BarRenderer)chart.getCategoryPlot().getRenderer();
            barRenderer.setSeriesPaint(0,  Color.decode("#2084FE"));
            barRenderer.setShadowPaint(Color.WHITE);
            barRenderer.setBaseFillPaint(Color.decode("#07CF61"));
            barRenderer.setBarPainter(new StandardBarPainter());

            JPanel titlePanel = new JPanel();
            titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            titlePanel.setBackground(Color.WHITE);

            JLabel titleLabel = new JLabel("  NODE APPEARANCE BY DEPTH DISTRIBUTION");
            titleLabel.setBackground(Color.WHITE);
            titleLabel.setFont(MySequentialGraphVars.tahomaBoldFont11);
            titleLabel.setForeground(Color.DARK_GRAY);
            titleLabel.setHorizontalAlignment(JLabel.LEFT);

            titlePanel.add(titleLabel);

            JPanel menuPanel = new JPanel();
            menuPanel.setLayout(new GridLayout(1,2));
            menuPanel.setBackground(Color.WHITE);
            menuPanel.add(titlePanel);

            this.add(menuPanel, BorderLayout.NORTH);
            barRenderer.setBaseLegendTextFont(MySequentialGraphVars.tahomaPlainFont11);
            this.add(new ChartPanel(chart), BorderLayout.CENTER);

            Border blackline = BorderFactory.createLineBorder(Color.black);
            TitledBorder titledBorder = BorderFactory.createTitledBorder(blackline, "");
            titledBorder.setTitleJustification(TitledBorder.LEFT);
            titledBorder.setTitleFont(MySequentialGraphVars.f_pln_6);
            titledBorder.setTitleColor(Color.DARK_GRAY);
            this.setBorder(titledBorder);

            revalidate();
            repaint();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void enlarge() {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            JFrame f = new JFrame("NODE DEPTH DISTRIBUTION");
            f.setLayout(new BorderLayout());
            f.setBackground(Color.WHITE);
            f.setCursor(Cursor.HAND_CURSOR);
            f.setPreferredSize(new Dimension(600, 400));
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            MyNodeDepthDistributionChart nodeDepthDistributionChart = new MyNodeDepthDistributionChart();
            nodeDepthDistributionChart.decorate();
            f.getContentPane().add(nodeDepthDistributionChart, BorderLayout.CENTER);
            f.pack();
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

}
