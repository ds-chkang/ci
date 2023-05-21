package medousa.sequential.graph.stats;

import medousa.sequential.graph.MyEdge;
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
import java.util.Collection;
import java.util.TreeMap;

public class MyEdgeValueDistributionChart
extends JFrame {

    private JPanel contentPanel = new JPanel();

    public MyEdgeValueDistributionChart(String title, String xAxisTitle) {
        super(title);
        this.contentPanel.setLayout(new BorderLayout(5,5));
        this.contentPanel.setBackground(Color.WHITE);
        this.decorateDistributionWithEdgeValues(xAxisTitle);

        this.setLayout(new BorderLayout());
        this.setBackground(Color.WHITE);
        this.getContentPane().add(this.contentPanel, BorderLayout.CENTER);
        this.setCursor(Cursor.HAND_CURSOR);
        this.setPreferredSize(new Dimension(400, 450));
        this.pack();

        Border blackline = BorderFactory.createLineBorder(Color.black);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(blackline, "");
        titledBorder.setTitleJustification(TitledBorder.LEFT);
        titledBorder.setTitleFont(new Font("Arial", Font.PLAIN, 0));
        titledBorder.setTitleColor(Color.DARK_GRAY);
        //this.contentPanel.setBorder(titledBorder);
        this.setVisible(true);
    }

    private void decorateDistributionWithEdgeValues(String xAxisTitle) {
        try {
            this.setLayout(new BorderLayout(5, 5));
            this.setBackground(Color.WHITE);
            TreeMap<Long, Integer> nodeValueDistributionMap = new TreeMap<>();
            double totalValue = 0.0D;
            Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
            for (MyEdge edge : edges) {
                long edgeValue = (long)edge.getCurrentValue();
                if (nodeValueDistributionMap.containsKey(edgeValue)) {nodeValueDistributionMap.put(edgeValue, nodeValueDistributionMap.get(edgeValue)+1);
                } else {nodeValueDistributionMap.put(edgeValue, 1);}
                totalValue += (double)edgeValue;
            }
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            for (Long columnValue : nodeValueDistributionMap.keySet()) {
                dataset.addValue(nodeValueDistributionMap.get(columnValue), xAxisTitle, String.valueOf(columnValue));
            }
            String avgValue = MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(totalValue/edges.size()));
            JFreeChart chart = ChartFactory.createBarChart("",
                xAxisTitle + "[AVG.:" + avgValue + "]", "COUNT",
                dataset, PlotOrientation.VERTICAL, true, true, false);
            chart.setTitle("");
            chart.removeLegend();
            chart.getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
            chart.getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
            chart.setBackgroundPaint(Color.WHITE);
            chart.getCategoryPlot().getDomainAxis().setTickLabelFont(MySequentialGraphVars.f_pln_11);
            chart.getCategoryPlot().getDomainAxis().setLabelFont(MySequentialGraphVars.f_pln_11);
            chart.getCategoryPlot().getRangeAxis().setTickLabelFont(MySequentialGraphVars.f_pln_11);
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
            barRenderer.setBaseLegendTextFont(MySequentialGraphVars.tahomaPlainFont11);
            this.contentPanel.add(new ChartPanel(chart), BorderLayout.CENTER);
        } catch (Exception ex) {ex.printStackTrace();}
    }


}
