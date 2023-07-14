package medousa.test;

import java.awt.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import medousa.direct.utils.MyDirectGraphVars;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.statistics.BoxAndWhiskerCalculator;
import org.jfree.data.statistics.BoxAndWhiskerXYDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerXYDataset;
import org.jfree.date.DateUtilities;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class BoxPlotExample extends ApplicationFrame {

    public BoxPlotExample(String titel) {
        super(titel);

        final BoxAndWhiskerXYDataset dataset = createDataset();
        final JFreeChart chart = createChart(dataset);

        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 300));
        setContentPane(chartPanel);

    }

    private BoxAndWhiskerXYDataset createDataset() {
        final int ENTITY_COUNT = 10;

        DefaultBoxAndWhiskerXYDataset dataset = new
                DefaultBoxAndWhiskerXYDataset("Test");

        for (int i = 0; i < ENTITY_COUNT; i++) {
            Date date = DateUtilities.createDate(2003, 7, i + 1, 12, 0);
            List values = new ArrayList();
            for (int j = 0; j < 10; j++) {
                values.add(new Double(10.0 + Math.random() * 10.0));
            }
            dataset.add(date,
                    BoxAndWhiskerCalculator.calculateBoxAndWhiskerStatistics(values));
        }
        return dataset;
    }

    private JFreeChart createChart(final BoxAndWhiskerXYDataset dataset) {
        JFreeChart chart = ChartFactory.createBoxAndWhiskerChart(
                "Box and Whisker Chart", "Time", "Value", dataset, true);
        chart.setBackgroundPaint(new Color(249, 231, 236));

        return chart;
    }

    public static void main(final String[] args) {

        final BoxPlotExample demo = new BoxPlotExample("");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }
}
