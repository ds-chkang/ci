package medousa.sequential.graph.stats;

import medousa.MyProgressBar;
import medousa.sequential.utils.MySequentialGraphVars;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.HorizontalAlignment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.util.HashMap;
import java.util.Map;

public class MySequenceLengthByDepthLineChart
extends JPanel {

    public static int instances = 0;

    public MySequenceLengthByDepthLineChart() {

        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                decorate();
            }
        });
    }

    private void decorate() {
        try {
            this.setLayout(new BorderLayout(5,5));
            this.setBackground(Color.WHITE);

            Map<Integer, Integer> sequenceLengthCountMap = new HashMap<>();
            for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                int sequenceLength = MySequentialGraphVars.seqs[s].length;
                if (sequenceLengthCountMap.containsKey(sequenceLength)) {
                    sequenceLengthCountMap.put(sequenceLength, sequenceLengthCountMap.get(sequenceLength)+1);
                } else {
                    sequenceLengthCountMap.put(sequenceLength, 1);
                }
            }

            XYSeries sequenceDistributionByDepthSeries = new XYSeries("SEQUENCES");
            for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                if (sequenceLengthCountMap.get(i) != null) {
                    sequenceDistributionByDepthSeries.add(i, sequenceLengthCountMap.get(i));
                }
            }

            XYSeries sequenceDistributionDropOffByDepthSeries = new XYSeries("DROP-OFF");
            sequenceDistributionDropOffByDepthSeries.add(1, 0);
            sequenceDistributionByDepthSeries.add(1, 0);
            for (int i = 2; i <= MySequentialGraphVars.mxDepth; i++) {
                if (sequenceLengthCountMap.get(i) != null && sequenceLengthCountMap.get(i-1) != null) {
                    int sequenceLengthDiff = sequenceLengthCountMap.get(i) - sequenceLengthCountMap.get(i - 1);
                    sequenceDistributionDropOffByDepthSeries.add(i, sequenceLengthDiff);
                } else if (sequenceLengthCountMap.get(i) != null && sequenceLengthCountMap.get(i-1) == null) {
                    sequenceDistributionDropOffByDepthSeries.add(i, sequenceLengthCountMap.get(i));
                } else if (sequenceLengthCountMap.get(i) == null && sequenceLengthCountMap.get(i-1) != null) {
                    sequenceDistributionDropOffByDepthSeries.add(i, -sequenceLengthCountMap.get(i-1));
                } else {
                    sequenceDistributionDropOffByDepthSeries.add(i, 0);
                }
            }

            XYSeriesCollection dataset = new XYSeriesCollection();
            dataset.addSeries(sequenceDistributionByDepthSeries);
            dataset.addSeries(sequenceDistributionDropOffByDepthSeries);

            JFreeChart chart = ChartFactory.createXYLineChart("", "DEPTH", "", dataset);
            chart.getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
            chart.getXYPlot().setBackgroundPaint(Color.WHITE);
            chart.getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
            chart.getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);
            chart.getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
            chart.getXYPlot().getDomainAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
            chart.getXYPlot().getDomainAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
            chart.getXYPlot().getRangeAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
            chart.getXYPlot().getRangeAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
            chart.getXYPlot().getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
            chart.setBackgroundPaint(Color.WHITE);

            XYPlot plot = (XYPlot) chart.getPlot();
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
            renderer.setSeriesPaint(0, Color.RED);
            renderer.setSeriesStroke(0, new BasicStroke(1.5f));
            renderer.setSeriesShapesVisible(0, true);
            renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
            renderer.setSeriesFillPaint(0, Color.WHITE);

            renderer.setSeriesPaint(1, Color.MAGENTA);
            renderer.setSeriesStroke(1, new BasicStroke(1.5f));
            renderer.setSeriesShapesVisible(1, true);
            renderer.setSeriesShape(1, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
            renderer.setSeriesFillPaint(1, Color.WHITE);
            renderer.setUseFillPaint(true);

            ChartPanel chartPanel = new ChartPanel( chart );

            JLabel titleLabel = new JLabel(" SEQUENCES BY DEPTH");
            titleLabel.setFont(MySequentialGraphVars.tahomaBoldFont11);
            titleLabel.setBackground(Color.WHITE);
            titleLabel.setForeground(Color.DARK_GRAY);

            JPanel titlePanel = new JPanel();
            titlePanel.setBackground(Color.WHITE);
            titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            titlePanel.add(titleLabel);

            JButton enlargeBtn = new JButton("ENLARGE");
            enlargeBtn.setFont(MySequentialGraphVars.tahomaPlainFont11);
            enlargeBtn.setFocusable(false);
            enlargeBtn.setBackground(Color.WHITE);
            enlargeBtn.addActionListener(new ActionListener() {@Override
                public void actionPerformed(ActionEvent e) {new Thread(new Runnable() {@Override public void run() {enlarge();}}).start();}});

            JComboBox selectComboBoxMenu = new JComboBox();
            selectComboBoxMenu.setFocusable(false);
            selectComboBoxMenu.setFont(MySequentialGraphVars.tahomaPlainFont11);
            selectComboBoxMenu.setBackground(Color.WHITE);
            selectComboBoxMenu.addItem("SELECT");
            selectComboBoxMenu.addItem("SEQUENCES");
            selectComboBoxMenu.addItem("DROP-OFFS");
            selectComboBoxMenu.addActionListener(new ActionListener() {@Override
                public void actionPerformed(ActionEvent e) {
                    if (selectComboBoxMenu.getSelectedIndex() == 0) {
                        renderer.setSeriesStroke(0, new BasicStroke(1.5f));
                        renderer.setSeriesStroke(1, new BasicStroke(1.5f));
                        renderer.setSeriesLinesVisible(0, true);
                        renderer.setSeriesLinesVisible(1, true);
                        renderer.setSeriesFillPaint(0, Color.WHITE);
                        renderer.setSeriesFillPaint(1, Color.WHITE);
                        renderer.setSeriesPaint(0, Color.RED);
                        renderer.setSeriesPaint(1, Color.MAGENTA);
                    } else if (selectComboBoxMenu.getSelectedIndex() == 1) {
                        renderer.setSeriesStroke(0, new BasicStroke(1.5f));
                        renderer.setSeriesStroke(1, new BasicStroke(0.0f));
                        renderer.setSeriesLinesVisible(0, true);
                        renderer.setSeriesLinesVisible(1, false);
                        renderer.setSeriesFillPaint(0, Color.WHITE);
                        renderer.setSeriesFillPaint(1, new Color(0,0,0,0.0f));
                        renderer.setSeriesPaint(0, Color.RED);
                        renderer.setSeriesPaint(1, new Color(0,0,0,0.0f));
                    } else if (selectComboBoxMenu.getSelectedIndex() == 2) {
                        renderer.setSeriesStroke(0, new BasicStroke(0.0f));
                        renderer.setSeriesStroke(1, new BasicStroke(1.5f));
                        renderer.setSeriesLinesVisible(0, false);
                        renderer.setSeriesLinesVisible(1, true);
                        renderer.setSeriesFillPaint(0, new Color(0,0,0,0.0f));
                        renderer.setSeriesFillPaint(1, Color.WHITE);
                        renderer.setSeriesPaint(0, new Color(0,0,0,0.0f));
                        renderer.setSeriesPaint(1, Color.MAGENTA);
                    }
                }
            });

            JPanel btnPanel = new JPanel();
            btnPanel.setBackground(Color.WHITE);
            btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
            btnPanel.add(selectComboBoxMenu);

            JPanel menuPanel = new JPanel();
            menuPanel.setLayout(new BorderLayout(0,0));
            menuPanel.setBackground(Color.WHITE);
            menuPanel.add(titlePanel, BorderLayout.WEST);

            menuPanel.add(btnPanel, BorderLayout.CENTER);
            renderer.setBaseLegendTextFont(MySequentialGraphVars.tahomaPlainFont11);
            this.add(menuPanel, BorderLayout.NORTH);
            this.add(chartPanel, BorderLayout.CENTER);

            revalidate();
            repaint();
        } catch (Exception ex) {ex.printStackTrace();}
    }

    public void enlarge() {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            JFrame f = new JFrame("SEQUENCES BY DEPTH");
            f.setLayout(new BorderLayout());
            f.setBackground(Color.WHITE);
            f.setPreferredSize(new Dimension(600, 400));
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            MySequenceLengthByDepthLineChart reachTimeByDepthLineChart = new MySequenceLengthByDepthLineChart();
            f.getContentPane().add(reachTimeByDepthLineChart, BorderLayout.CENTER);
            f.pack();
            f.setCursor(Cursor.HAND_CURSOR);
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
