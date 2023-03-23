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
import org.jfree.ui.HorizontalAlignment;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Collection;
import java.util.TreeMap;

public class MyValueDistributionChart
extends JFrame {

    private JPanel contentPanel = new JPanel();

    public MyValueDistributionChart(String title) {
        super(title);
    }

    public void setNodeValueDistribution(String xAxisTitle) {
        try {
            this.setLayout(new BorderLayout(5, 5));
            this.setBackground(Color.WHITE);
            this.contentPanel.setLayout(new BorderLayout(5,5));
            this.contentPanel.setBackground(Color.WHITE);

            TreeMap<Long, Integer> nodeValueDistributionMap = new TreeMap<>();
            double totalValue = 0.0D;
            Collection<MyDirectNode> nodes = MyVars.directMarkovChain.getVertices();
            for (MyDirectNode n : nodes) {
                if (Double.isNaN(n.getCurrentValue())) continue;
                long nodeValue = (long)n.getCurrentValue();
                if (nodeValueDistributionMap.containsKey(nodeValue)) {
                    nodeValueDistributionMap.put(nodeValue, nodeValueDistributionMap.get(nodeValue) + 1);
                } else {nodeValueDistributionMap.put(nodeValue, 1);}
                totalValue += (double) nodeValue;
            }

            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            for (Long columnValue : nodeValueDistributionMap.keySet()) {
                dataset.addValue((int)nodeValueDistributionMap.get(columnValue), xAxisTitle, String.valueOf(columnValue));
            }

            String avgValue = MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(totalValue/nodes.size()));
            JFreeChart chart = ChartFactory.createBarChart("",
                xAxisTitle + " [AVG.:" + avgValue + "]", "COUNT",
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
            this.contentPanel.add(new ChartPanel(chart), BorderLayout.CENTER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setEdgeValueDistribution(String xAxisTitle) {
        try {
            this.setLayout(new BorderLayout(5, 5));
            this.setBackground(Color.WHITE);
            this.contentPanel.setLayout(new BorderLayout(5,5));
            this.contentPanel.setBackground(Color.WHITE);

            TreeMap<Float, Integer> nodeValueDistributionMap = new TreeMap<>();
            float totalValue = 0.00f;
            Collection<MyDirectEdge> edges = MyVars.directMarkovChain.getEdges();
            for (MyDirectEdge e : edges) {
                if (Float.isNaN(e.getCurrentValue())) continue;
                float edgeValue = e.getCurrentValue();
                if (nodeValueDistributionMap.containsKey(edgeValue)) {
                    nodeValueDistributionMap.put(edgeValue, nodeValueDistributionMap.get(edgeValue) + 1);
                } else {nodeValueDistributionMap.put(edgeValue, 1);}
                totalValue += edgeValue;
            }

            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            for (Float columnValue : nodeValueDistributionMap.keySet()) {
                dataset.addValue((int)nodeValueDistributionMap.get(columnValue), xAxisTitle, String.valueOf(columnValue));
            }

            String avgValue = MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(totalValue/edges.size()));
            JFreeChart chart = ChartFactory.createBarChart("",
                xAxisTitle + " [AVG.:" + avgValue + "]", "COUNT",
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
            this.contentPanel.add(new ChartPanel(chart), BorderLayout.CENTER);
        } catch (Exception ex) {ex.printStackTrace();}
    }

    public void showDistribution() {
        this.setCursor(Cursor.HAND_CURSOR);
        this.setPreferredSize(new Dimension(400, 400));
        Border blackline = BorderFactory.createLineBorder(Color.black);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(blackline, "");
        titledBorder.setTitleJustification(TitledBorder.LEFT);
        titledBorder.setTitleFont(MyVars.tahomaBoldFont12);
        titledBorder.setTitleColor(Color.DARK_GRAY);
        //this.contentPanel.setBorder(titledBorder);
        this.getContentPane().add(this.contentPanel, BorderLayout.CENTER);
        this.pack();
        this.setVisible(true);
    }
}
