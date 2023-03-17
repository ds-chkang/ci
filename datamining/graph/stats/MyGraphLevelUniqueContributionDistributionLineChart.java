package datamining.graph.stats;

import datamining.graph.MyEdge;
import datamining.graph.MyNode;
import datamining.utils.MyMathUtil;
import datamining.utils.system.MySysUtil;
import datamining.utils.system.MyVars;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
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
import java.util.Collection;
import java.util.LinkedHashMap;

public class MyGraphLevelUniqueContributionDistributionLineChart
extends JPanel {

    public static int instances = 0;

    public MyGraphLevelUniqueContributionDistributionLineChart() {
        this.decorate();
    }

    public void decorate() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                try {
                    removeAll();
                    setLayout(new BorderLayout(3, 3));
                    setBackground(Color.WHITE);

                    ChartPanel chartPanel = new ChartPanel(setNodeChart());
                    chartPanel.setPreferredSize(new Dimension(350, 367));
                    chartPanel.getChart().getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
                    chartPanel.getChart().getXYPlot().setBackgroundPaint(Color.WHITE);
                    chartPanel.getChart().getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
                    chartPanel.getChart().getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);
                    chartPanel.getChart().getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
                    chartPanel.getChart().getXYPlot().getDomainAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
                    chartPanel.getChart().getXYPlot().getDomainAxis().setTickLabelFont(MyVars.tahomaPlainFont10);
                    chartPanel.getChart().getXYPlot().getRangeAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
                    chartPanel.getChart().getXYPlot().getRangeAxis().setTickLabelFont(MyVars.tahomaPlainFont10);
                    chartPanel.getChart().getXYPlot().getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
                    chartPanel.getChart().setBackgroundPaint(Color.WHITE);

                    XYPlot plot = (XYPlot) chartPanel.getChart().getPlot();
                    XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
                    renderer.setSeriesPaint(0, Color.BLACK);
                    renderer.setSeriesStroke(0, new BasicStroke(1.6f));
                    renderer.setSeriesShapesVisible(0, true);
                    renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                    renderer.setSeriesFillPaint(0, Color.WHITE);
                    renderer.setUseFillPaint(true);

                    JLabel titleLabel = new JLabel(" UNIQ. C.");
                    titleLabel.setToolTipText("UNIQUE CONTRIBUTION VALUE DISTRIBUTION");
                    titleLabel.setFont(MyVars.tahomaBoldFont11);
                    titleLabel.setBackground(Color.WHITE);
                    titleLabel.setForeground(Color.DARK_GRAY);

                    JPanel titlePanel = new JPanel();
                    titlePanel.setBackground(Color.WHITE);
                    titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
                    titlePanel.add(titleLabel);

                    JButton enlargeBtn = new JButton("+");
                    enlargeBtn.setFont(MyVars.tahomaPlainFont10);
                    enlargeBtn.setBackground(Color.WHITE);
                    enlargeBtn.setFocusable(false);
                    enlargeBtn.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    enlarge();
                                }
                            }).start();
                        }
                    });

                    JComboBox selectUniqueContribution = new JComboBox();
                    selectUniqueContribution.setFocusable(false);
                    selectUniqueContribution.setBackground(Color.WHITE);
                    selectUniqueContribution.setFont(MyVars.tahomaPlainFont10);
                    selectUniqueContribution.addActionListener(new ActionListener() {
                        @Override public void actionPerformed(ActionEvent e) {
                            if (selectUniqueContribution.getSelectedIndex() == 0) {
                                chartPanel.removeAll();
                                chartPanel.setChart(setNodeChart());
                                chartPanel.setPreferredSize(new Dimension(350, 367));
                                chartPanel.getChart().getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
                                chartPanel.getChart().getXYPlot().setBackgroundPaint(Color.WHITE);
                                chartPanel.getChart().getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
                                chartPanel.getChart().getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);
                                chartPanel.getChart().getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
                                chartPanel.getChart().getXYPlot().getDomainAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
                                chartPanel.getChart().getXYPlot().getDomainAxis().setTickLabelFont(MyVars.tahomaPlainFont10);
                                chartPanel.getChart().getXYPlot().getRangeAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
                                chartPanel.getChart().getXYPlot().getRangeAxis().setTickLabelFont(MyVars.tahomaPlainFont10);
                                chartPanel.getChart().getXYPlot().getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
                                chartPanel.getChart().setBackgroundPaint(Color.WHITE);

                                XYPlot plot = (XYPlot) chartPanel.getChart().getPlot();
                                XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
                                renderer.setSeriesPaint(0, Color.BLACK);
                                renderer.setSeriesStroke(0, new BasicStroke(1.6f));
                                renderer.setSeriesShapesVisible(0, true);
                                renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                                renderer.setSeriesFillPaint(0, Color.WHITE);
                                renderer.setUseFillPaint(true);
                                chartPanel.revalidate();
                                chartPanel.repaint();
                            } else {
                                chartPanel.removeAll();
                                chartPanel.setChart(setEdgeChart());
                                chartPanel.setPreferredSize(new Dimension(350, 367));
                                chartPanel.getChart().getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
                                chartPanel.getChart().getXYPlot().setBackgroundPaint(Color.WHITE);
                                chartPanel.getChart().getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
                                chartPanel.getChart().getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);
                                chartPanel.getChart().getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
                                chartPanel.getChart().getXYPlot().getDomainAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
                                chartPanel.getChart().getXYPlot().getDomainAxis().setTickLabelFont(MyVars.tahomaPlainFont10);
                                chartPanel.getChart().getXYPlot().getRangeAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
                                chartPanel.getChart().getXYPlot().getRangeAxis().setTickLabelFont(MyVars.tahomaPlainFont10);
                                chartPanel.getChart().getXYPlot().getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
                                chartPanel.getChart().setBackgroundPaint(Color.WHITE);

                                XYPlot plot = (XYPlot) chartPanel.getChart().getPlot();
                                XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
                                renderer.setSeriesPaint(0, Color.BLACK);
                                renderer.setSeriesStroke(0, new BasicStroke(1.6f));
                                renderer.setSeriesShapesVisible(0, true);
                                renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                                renderer.setSeriesFillPaint(0, Color.WHITE);
                                renderer.setUseFillPaint(true);
                                chartPanel.revalidate();
                                chartPanel.repaint();
                            }
                        }
                    });
                    selectUniqueContribution.addItem("NODE");
                    selectUniqueContribution.addItem("EDGE");

                    JPanel menuPanel = new JPanel();
                    menuPanel.setBackground(Color.WHITE);
                    menuPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 3));
                    menuPanel.add(selectUniqueContribution);
                    menuPanel.add(enlargeBtn);

                    JPanel topPanel = new JPanel();
                    topPanel.setLayout(new BorderLayout(3, 3));
                    topPanel.setBackground(Color.WHITE);
                    topPanel.add(titlePanel, BorderLayout.WEST);
                    topPanel.add(menuPanel, BorderLayout.EAST);
                    add(topPanel, BorderLayout.NORTH);
                    add(chartPanel, BorderLayout.CENTER);
                    revalidate();
                    repaint();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private JFreeChart setNodeChart() {
        double total = 0;
        XYSeries uniqueContributionSeries = new XYSeries("U. C.");
        LinkedHashMap<Long, Long> uniqueContributionMap = new LinkedHashMap<>();
        Collection<MyNode> nodes = MyVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0) continue;
            total += n.getUniqueContribution();
            if (uniqueContributionMap.containsKey(n.getUniqueContribution())) {
                uniqueContributionMap.put(n.getUniqueContribution(), uniqueContributionMap.get(n.getUniqueContribution())+1);
            } else {
                uniqueContributionMap.put(n.getUniqueContribution(), 1L);
            }
        }

        for (Long uniqueContribution : uniqueContributionMap.keySet()) {
            uniqueContributionSeries.add(uniqueContribution, uniqueContributionMap.get(uniqueContribution));
        }
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(uniqueContributionSeries);

        double avgUniqCont = total/nodes.size();
        String plotTitle = "";
        String xaxis = "UNIQUE CONTRIBUTION[AVG.: " + MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(avgUniqCont)) + "]";
        String yaxis = "";
        PlotOrientation orientation = PlotOrientation.VERTICAL;
        boolean show = false;
        boolean toolTips = true;
        boolean urls = false;
        return ChartFactory.createXYLineChart("", "DEPTH", "", dataset);
    }

    private JFreeChart setEdgeChart() {
        double total = 0;
        XYSeries uniqueContributionSeries = new XYSeries("U. C.");
        LinkedHashMap<Long, Long> uniqueContributionMap = new LinkedHashMap<>();
        Collection<MyEdge> edges = MyVars.g.getEdges();
        for (MyEdge e : edges) {
            if (e.getCurrentValue() == 0) continue;
            total += e.getUniqueContribution();
            if (uniqueContributionMap.containsKey(e.getUniqueContribution())) {
                uniqueContributionMap.put((long)e.getUniqueContribution(), uniqueContributionMap.get(e.getUniqueContribution())+1);
            } else {
                uniqueContributionMap.put((long)e.getUniqueContribution(), 1L);
            }
        }

        for (Long uniqueContribution : uniqueContributionMap.keySet()) {
            uniqueContributionSeries.add(uniqueContribution, uniqueContributionMap.get(uniqueContribution));
        }
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(uniqueContributionSeries);

        double avgUniqCont = total/edges.size();
        String plotTitle = "";
        String xaxis = "UNIQUE CONTRIBUTION[AVG.: " + MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(avgUniqCont)) + "]";
        String yaxis = "";
        PlotOrientation orientation = PlotOrientation.VERTICAL;
        boolean show = false;
        boolean toolTips = true;
        boolean urls = false;
        return ChartFactory.createXYLineChart("", "UNIQUE CONTRIBUTION", "", dataset);
    }

    public void enlarge() {
        MyGraphLevelUniqueContributionDistributionLineChart uniqueContributionDistributionLineChart = new MyGraphLevelUniqueContributionDistributionLineChart();
        JFrame distFrame = new JFrame(" UNIQUE CONTRIBUTION DISTRIBUTION");
        distFrame.setLayout(new BorderLayout(3,3));
        distFrame.getContentPane().add(uniqueContributionDistributionLineChart, BorderLayout.CENTER);
        distFrame.setPreferredSize(new Dimension(450, 350));
        distFrame.pack();
        distFrame.setVisible(true);
    }
}
