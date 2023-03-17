package datamining.graph.stats;

import datamining.main.MyProgressBar;
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
            if (!MyVars.getViewer().selectedNode.getName().contains("x")) {
                for (int sequence = 0; sequence < MyVars.seqs.length; sequence++) {
                    for (int i = 1; i < MyVars.seqs[sequence].length; i++) {
                        String itemset = (MyVars.isTimeOn ? MyVars.seqs[sequence][i].split(":")[0] : MyVars.seqs[sequence][i]);
                        if (itemset.equals(MyVars.getViewer().selectedNode.getName())) {
                            if (nodeDepthCountMap.containsKey(i)) {
                                nodeDepthCountMap.put(i, nodeDepthCountMap.get(i)+1);
                            } else {
                                nodeDepthCountMap.put(i, 1);
                            }
                        }
                    }
                }
            } else {
                for (int s = 0; s < MyVars.seqs.length; s++) {
                    String itemset = (MyVars.isTimeOn ? MyVars.seqs[s][0].split(":")[0] : MyVars.seqs[s][0]);
                    if (itemset.equals(MyVars.getViewer().selectedNode.getName())) {
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
            for (int j = 1; j < MyVars.mxDepth; j++) {
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
            for (int j = 1; j < MyVars.mxDepth; j++) {
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
            if (!MyVars.getViewer().selectedNode.getName().contains("x")) {
                avgDepthAppearance = MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat((double) toalDepthAppearance/(MyVars.mxDepth - 1)));
            } else {
                avgDepthAppearance = MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat((double) toalDepthAppearance/MyVars.sequeceFeatureCount));
            }

            JFreeChart chart = ChartFactory.createBarChart("",
                    "DEPTH[AVG. DEPTH APPEARANCE: " + avgDepthAppearance + "]", "COUNT",
                    dataset, PlotOrientation.VERTICAL, true, true, false);
            chart.setTitle("");
            chart.removeLegend();
            chart.getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
            chart.getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
            chart.setBackgroundPaint(Color.WHITE);
            chart.getCategoryPlot().getDomainAxis().setTickLabelFont(MyVars.tahomaPlainFont11);
            chart.getCategoryPlot().getDomainAxis().setLabelFont(MyVars.tahomaPlainFont11);
            chart.getCategoryPlot().getRangeAxis().setTickLabelFont(MyVars.tahomaPlainFont11);
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

            JLabel titleLabel = new JLabel("  NODE DEPTH DISTRIBUTION");
            titleLabel.setBackground(Color.WHITE);
            titleLabel.setFont(MyVars.tahomaBoldFont12);
            titleLabel.setForeground(Color.DARK_GRAY);
            titleLabel.setHorizontalAlignment(JLabel.LEFT);

            titlePanel.add(titleLabel);

            JPanel menuPanel = new JPanel();
            menuPanel.setLayout(new GridLayout(1,2));
            menuPanel.setBackground(Color.WHITE);
            menuPanel.add(titlePanel);

            this.add(menuPanel, BorderLayout.NORTH);
            barRenderer.setBaseLegendTextFont(MyVars.tahomaPlainFont11);
            this.add(new ChartPanel(chart), BorderLayout.CENTER);

            Border blackline = BorderFactory.createLineBorder(Color.black);
            TitledBorder titledBorder = BorderFactory.createTitledBorder(blackline, "");
            titledBorder.setTitleJustification(TitledBorder.LEFT);
            titledBorder.setTitleFont(MyVars.f_pln_6);
            titledBorder.setTitleColor(Color.DARK_GRAY);
            this.setBorder(titledBorder);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void enlarge() {
        try {
            MyProgressBar pb = new MyProgressBar(false);
            JFrame frame = new JFrame("NODE DEPTH DISTRIBUTION");
            frame.setLayout(new BorderLayout());
            frame.setBackground(Color.WHITE);
            frame.setCursor(Cursor.HAND_CURSOR);
            frame.setPreferredSize(new Dimension(600, 400));
            pb.updateValue(20, 100);

            MyNodeDepthDistributionChart nodeDepthDistributionChart = new MyNodeDepthDistributionChart();
            nodeDepthDistributionChart.decorate();
            frame.getContentPane().add(nodeDepthDistributionChart, BorderLayout.CENTER);
            frame.pack();
            pb.updateValue(60, 100);

            Border blackline = BorderFactory.createLineBorder(Color.black);
            TitledBorder titledBorder = BorderFactory.createTitledBorder(blackline, "NODE STATISTICS");
            titledBorder.setTitleJustification(TitledBorder.LEFT);
            titledBorder.setTitleFont(new Font("Arial", Font.PLAIN, 0));
            titledBorder.setTitleColor(Color.DARK_GRAY);
            nodeDepthDistributionChart.setBorder(titledBorder);
            pb.updateValue(100, 100);
            pb.dispose();

            frame.setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
