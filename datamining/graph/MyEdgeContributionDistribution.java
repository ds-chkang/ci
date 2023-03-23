package datamining.graph;

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
import java.util.Collection;
import java.util.TreeMap;

public class MyEdgeContributionDistribution
extends JPanel {

    public MyEdgeContributionDistribution() {
        this.decorate();
    }

    private void decorate() {
        this.setLayout(new BorderLayout(3,3));
        this.setBackground(Color.WHITE);

        ChartPanel chartPanel = new ChartPanel(this.setChart());
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
        barRenderer.setSeriesPaint(0,  new Color(0, 0, 0, 0.25f));//Color.LIGHT_GRAY);//Color.decode("#2084FE"));
        barRenderer.setShadowPaint(Color.WHITE);
        barRenderer.setBaseFillPaint(Color.decode("#07CF61"));
        barRenderer.setBarPainter(new StandardBarPainter());
        barRenderer.setBaseLegendTextFont(MyVars.tahomaPlainFont11);

        JLabel titleLabel = new JLabel(" EDGE CONTRIBUTION DISTRIBUTION");
        titleLabel.setFont(MyVars.tahomaBoldFont11);
        titleLabel.setBackground(Color.WHITE);
        titleLabel.setForeground(Color.DARK_GRAY);

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        titlePanel.add(titleLabel);

        this.setBackground(Color.WHITE);
        this.add(chartPanel, BorderLayout.CENTER);
        this.add(titlePanel, BorderLayout.NORTH);
    }

    public void showOutContributionDistribution() {
        JFrame frame = new JFrame(" EDGE CONTRIBUTION DISTRIBUTION");
        frame.setLayout(new BorderLayout(3,3));
        frame.getContentPane().add(this, BorderLayout.CENTER);
        frame.setPreferredSize(new Dimension(550, 450));
        frame.pack();
        frame.setVisible(true);
    }

    private JFreeChart setChart() {
        int totalCnt = 0;
        int totalContribution = 0;
        TreeMap<Integer, Integer> edgeContributionCountByNodeMap = new TreeMap<>();
        Collection<MyDirectEdge> edges = MyVars.directMarkovChain.getEdges();
        for (MyDirectEdge e : edges) {
            int edgeContribution = e.getContribution();
            if (edgeContribution > 0) {
                totalCnt++;
                totalContribution += edgeContribution;
                if (edgeContributionCountByNodeMap.containsKey(edgeContribution)) {edgeContributionCountByNodeMap.put(edgeContribution, edgeContributionCountByNodeMap.get(edgeContribution)+1);}
                else {edgeContributionCountByNodeMap.put(edgeContribution, 1);}
            }
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Integer contributionCount : edgeContributionCountByNodeMap.keySet()) {
            dataset.addValue(edgeContributionCountByNodeMap.get(contributionCount), "EDGE CONTRIBUTION", contributionCount);
        }

        double avgContribution = (double)totalContribution/totalCnt;

        String plotTitle = "";
        String xaxis = "EDGE CONTRIBUTION[AVG.: " + MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(avgContribution)) + "]";
        String yaxis = "";
        PlotOrientation orientation = PlotOrientation.VERTICAL;
        boolean show = false;
        boolean toolTips = true;
        boolean urls = false;
        return ChartFactory.createBarChart(plotTitle, xaxis, yaxis, dataset, orientation, show, toolTips, urls);
    }
}
