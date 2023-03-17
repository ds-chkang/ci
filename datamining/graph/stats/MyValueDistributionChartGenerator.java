package datamining.graph.stats;

import datamining.graph.MyNode;
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

public class MyValueDistributionChartGenerator
extends JFrame {

    private JPanel contentPanel = new JPanel();

    public MyValueDistributionChartGenerator(String title, JTable table, int column, String xAxisTitle) {
        super(title);
        this.contentPanel.setLayout(new BorderLayout(5,5));
        this.contentPanel.setBackground(Color.WHITE);
        this.decorateWithTableValues(table, column, xAxisTitle);
        this.setLayout(new BorderLayout());
        this.setBackground(Color.WHITE);
        this.getContentPane().add(this.contentPanel, BorderLayout.CENTER);
        this.setCursor(Cursor.HAND_CURSOR);
        this.setPreferredSize(new Dimension(350, 400));
        this.pack();
        Border blackline = BorderFactory.createLineBorder(Color.black);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(blackline, "");
        titledBorder.setTitleJustification(TitledBorder.LEFT);
        titledBorder.setTitleFont(new Font("Arial", Font.PLAIN, 0));
        titledBorder.setTitleColor(Color.DARK_GRAY);
        //this.contentPanel.setBorder(titledBorder);
        this.setVisible(true);
    }

    public MyValueDistributionChartGenerator(String title, String xAxisTitle) {
        super(title);
        this.contentPanel.setLayout(new BorderLayout(5,5));
        this.contentPanel.setBackground(Color.WHITE);
        this.decorateWithNodeValues(xAxisTitle);

        this.setLayout(new BorderLayout());
        this.setBackground(Color.WHITE);
        this.getContentPane().add(this.contentPanel, BorderLayout.CENTER);
        this.setCursor(Cursor.HAND_CURSOR);
        this.setPreferredSize(new Dimension(350, 400));
        this.pack();
        Border blackline = BorderFactory.createLineBorder(Color.black);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(blackline, "");
        titledBorder.setTitleJustification(TitledBorder.LEFT);
        titledBorder.setTitleFont(new Font("Arial", Font.PLAIN, 0));
        titledBorder.setTitleColor(Color.DARK_GRAY);
        //this.contentPanel.setBorder(titledBorder);
        this.setVisible(true);
    }

    private void decorateWithTableValues(JTable table, int column, String xAxisTitle) {
        try {
            this.setLayout(new BorderLayout(5, 5));
            this.setBackground(Color.WHITE);
            TreeMap<Double, Integer> columnValueDistributionMap = new TreeMap<>();
            double totalValue = 0.0D;
            for (int j=0; j < table.getRowCount(); j++) {
                double numberColumnValue = Double.valueOf(table.getValueAt(j, column).toString().split("\\.")[0].replaceAll("\\,", ""));
                if (columnValueDistributionMap.containsKey(numberColumnValue)) {columnValueDistributionMap.put(numberColumnValue, columnValueDistributionMap.get(numberColumnValue)+1);
                } else {columnValueDistributionMap.put(numberColumnValue, 1);}
                totalValue += numberColumnValue;
            }
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            for (Double columnValue : columnValueDistributionMap.keySet()) {dataset.addValue(columnValueDistributionMap.get(columnValue), xAxisTitle, String.valueOf(columnValue));}
            String avgValue = MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(totalValue/table.getRowCount()));
            JFreeChart chart = ChartFactory.createBarChart("", xAxisTitle + "[AVG.:" + avgValue + "]", "COUNT", dataset, PlotOrientation.VERTICAL, true, true, false);
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
            barRenderer.setSeriesPaint(0,  new Color(0, 0, 0f, 0.23f));
            barRenderer.setShadowPaint(Color.WHITE);
            barRenderer.setBaseFillPaint(new Color(0, 0, 0f, 0.23f));
            barRenderer.setBarPainter(new StandardBarPainter());
            barRenderer.setBaseLegendTextFont(MyVars.tahomaPlainFont11);
            this.contentPanel.add(new ChartPanel(chart), BorderLayout.CENTER);
        } catch (Exception ex) {ex.printStackTrace();}
    }

    private void decorateWithNodeValues(String xAxisTitle) {
        try {
            this.setLayout(new BorderLayout(5, 5));
            this.setBackground(Color.WHITE);
            TreeMap<Float, Integer> nodeValueDistributionMap = new TreeMap<>();
            double totalValue = 0.0D;
            Collection<MyNode> nodes = MyVars.g.getVertices();
            for (MyNode node : nodes) {
                if (Double.isNaN(node.getCurrentValue())) continue;
                float nodeValue = node.getCurrentValue();
                if (nodeValueDistributionMap.containsKey(nodeValue)) {nodeValueDistributionMap.put(nodeValue, nodeValueDistributionMap.get(nodeValue) + 1);
                } else {nodeValueDistributionMap.put(nodeValue, 1);}
                totalValue += (double) nodeValue;
            }

            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            for (Float columnValue : nodeValueDistributionMap.keySet()) {dataset.addValue(nodeValueDistributionMap.get(columnValue), xAxisTitle, String.valueOf(columnValue));}
            String avgValue = MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(totalValue/nodes.size()));
            JFreeChart chart = ChartFactory.createBarChart("", xAxisTitle + "[AVG.:" + avgValue + "]", "COUNT", dataset, PlotOrientation.VERTICAL, true, true, false);
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
            barRenderer.setSeriesPaint(0,  Color.LIGHT_GRAY);//Color.decode("#2084FE"));
            barRenderer.setShadowPaint(Color.WHITE);
            barRenderer.setBaseFillPaint(Color.decode("#07CF61"));
            barRenderer.setBarPainter(new StandardBarPainter());
            this.contentPanel.add(new ChartPanel(chart), BorderLayout.CENTER);
        } catch (Exception ex) {ex.printStackTrace();}
    }
}
