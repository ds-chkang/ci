package medousa.test;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;

import java.awt.Color;
import java.util.ArrayList;

public class BoxPlotExample {
    public static void main(String[] args) {

        // Step 1: Create a DefaultBoxAndWhiskerCategoryDataset and add data
        DefaultBoxAndWhiskerCategoryDataset dataset = new DefaultBoxAndWhiskerCategoryDataset();
        java.util.List<Double> values = new ArrayList<>();
        values.add(1.5);
        values.add(2.0);
        values.add(3.6);
        values.add(2.8);
        values.add(2.1);
        dataset.add(values, "Series 1", "Category 1");

        // Step 2: Create a JFreeChart and configure the plot
        JFreeChart chart = ChartFactory.createBoxAndWhiskerChart(
                "Boxplot",      // Chart title
                "Category",     // X-axis label
                "Value",        // Y-axis label
                dataset,        // Dataset
                true            // Show legend
        );

        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        BoxAndWhiskerRenderer renderer = (BoxAndWhiskerRenderer) plot.getRenderer();

        // Step 3: Customize the appearance of the boxplot
        renderer.setFillBox(false);      // Do not fill boxes
        renderer.setMeanVisible(false);  // Do not show the mean
        renderer.setMedianVisible(true); // Show the median
        renderer.setUseOutlinePaintForWhiskers(true); // Use outline paint for whiskers

        // Set custom colors
        renderer.setSeriesPaint(0, Color.blue);
        renderer.setSeriesPaint(1, Color.red);

        // Enable tooltips for data points
        renderer.setToolTipGenerator(new StandardCategoryToolTipGenerator());

        // Show outliers
        renderer.setUseOutlinePaintForWhiskers(true);
        renderer.setBaseOutlinePaint(Color.black);

        // Step 4: Create a ChartFrame and display the chart
        ChartFrame frame = new ChartFrame("Boxplot", chart);
        frame.pack();
        frame.setVisible(true);
    }
}
