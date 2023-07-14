package medousa.test;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LayeredBarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import java.awt.*;

public class DualAxisDemo5 extends ApplicationFrame {

    public DualAxisDemo5(final String title) {
        super(title);
        final double[][] data = new double[][] {
                {41.0, 33.0, 22.0, 64.0, 42.0, 62.0, 22.0, 14.0},
                {55.0, 63.0, 55.0, 48.0, 54.0, 37.0, 41.0, 39.0},
                {57.0, 75.0, 43.0, 33.0, 63.0, 46.0, 57.0, 33.0},
                {80.0, 75.0, 23.0, 43.0, 53.0, 46.0, 57.0, 33.0},
                {77.0, 75.0, 43.0, 33.0, 83.0, 46.0, 57.0, 103.0}
        };

        final CategoryDataset dataset = DatasetUtilities.createCategoryDataset(
                "Series ",
                "Factor ",
                data);

        // create the chart...
        final CategoryAxis categoryAxis = new CategoryAxis("Category");
        final ValueAxis valueAxis = new NumberAxis("Score (%)");
        final CategoryPlot plot = new CategoryPlot(dataset,
                categoryAxis,
                valueAxis,
                new LayeredBarRenderer());

        plot.setOrientation(PlotOrientation.VERTICAL);
        final JFreeChart chart = new JFreeChart(
                "Layered Bar Chart Demo 2",
                JFreeChart.DEFAULT_TITLE_FONT,
                plot,
                true
        );

        final LayeredBarRenderer renderer = (LayeredBarRenderer) plot.getRenderer();

        renderer.setSeriesBarWidth(0, 1.0);
        renderer.setSeriesBarWidth(1, 0.8);
        renderer.setSeriesBarWidth(2, 0.6);
        renderer.setSeriesBarWidth(3, 0.4);
        renderer.setSeriesBarWidth(4, 0.2);

        renderer.setItemMargin(0.01);

        final CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryMargin(0.25);
        domainAxis.setUpperMargin(0.05);
        domainAxis.setLowerMargin(0.05);

        // add the chart to a panel...
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);

    }

    public static void main(final String[] args) {

        final DualAxisDemo5 demo = new DualAxisDemo5("Layered Bar Chart Demo 2");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);

    }

}
