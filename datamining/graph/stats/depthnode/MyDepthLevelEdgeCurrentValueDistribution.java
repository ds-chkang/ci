package datamining.graph.stats.depthnode;

import datamining.graph.MyEdge;
import datamining.utils.MyMathUtil;
import datamining.utils.system.MySysUtil;
import datamining.utils.system.MyVars;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Collection;
import java.util.TreeMap;

public class MyDepthLevelEdgeCurrentValueDistribution
extends JPanel {

    protected JFrame distFrame = null;

    public MyDepthLevelEdgeCurrentValueDistribution() {
        this.decorate();
    }

    public void decorate() {
        try {
            removeAll();
            setLayout(new BorderLayout(3, 3));
            setBackground(Color.WHITE);

            if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() < 2) {
                JLabel titleLabel = new JLabel(" DEPTHNODE E. V.");
                titleLabel.setFont(MyVars.tahomaBoldFont11);
                titleLabel.setBackground(Color.WHITE);
                titleLabel.setForeground(Color.DARK_GRAY);

                JPanel titlePanel = new JPanel();
                titlePanel.setBackground(Color.WHITE);
                titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 6));
                titlePanel.add(titleLabel);

                JPanel topPanel = new JPanel();
                topPanel.setLayout(new BorderLayout(3, 8));
                topPanel.setBackground(Color.WHITE);
                topPanel.add(titlePanel, BorderLayout.WEST);

                JLabel msg = new JLabel("N/A");
                msg.setFont(MyVars.tahomaPlainFont12);
                msg.setBackground(Color.WHITE);
                msg.setHorizontalAlignment(JLabel.CENTER);
                this.add(topPanel, BorderLayout.NORTH);
                this.add(msg, BorderLayout.CENTER);
            } else {
                int total = 0;
                Collection<MyEdge> edges = MyVars.g.getEdges();
                for (MyEdge e : edges) {
                    if (e.getCurrentValue() > 0) {
                        total = 1;
                        break;

                    }
                }
                if (total == 0) {
                    JLabel titleLabel = new JLabel(" E. V.");
                    titleLabel.setFont(MyVars.tahomaBoldFont11);
                    titleLabel.setBackground(Color.WHITE);
                    titleLabel.setForeground(Color.DARK_GRAY);

                    JPanel titlePanel = new JPanel();
                    titlePanel.setBackground(Color.WHITE);
                    titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 6));
                    titlePanel.add(titleLabel);

                    JPanel topPanel = new JPanel();
                    topPanel.setLayout(new BorderLayout(3, 8));
                    topPanel.setBackground(Color.WHITE);
                    topPanel.add(titlePanel, BorderLayout.WEST);

                    JLabel msg = new JLabel("N/A");
                    msg.setFont(MyVars.tahomaPlainFont12);
                    msg.setBackground(Color.WHITE);
                    msg.setHorizontalAlignment(JLabel.CENTER);
                    this.add(topPanel, BorderLayout.NORTH);
                    this.add(msg, BorderLayout.CENTER);
                } else {
                    ChartPanel chartPanel = new ChartPanel(setChart());
                    chartPanel.getChart().getCategoryPlot().setRangeGridlinePaint(Color.DARK_GRAY);
                    chartPanel.getChart().getCategoryPlot().setDomainGridlinePaint(Color.DARK_GRAY);
                    chartPanel.getChart().getCategoryPlot().setBackgroundPaint(Color.WHITE);
                    chartPanel.getChart().getCategoryPlot().getDomainAxis().setTickLabelFont(MyVars.tahomaPlainFont11);
                    chartPanel.getChart().getCategoryPlot().getDomainAxis().setLabelFont(MyVars.tahomaPlainFont11);
                    chartPanel.getChart().getCategoryPlot().getRangeAxis().setTickLabelFont(MyVars.tahomaPlainFont11);
                    chartPanel.getChart().getCategoryPlot().getRangeAxis().setLabelFont(MyVars.tahomaPlainFont11);
                    chartPanel.getChart().getCategoryPlot().getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());

                    CategoryAxis domainAxis = chartPanel.getChart().getCategoryPlot().getDomainAxis();
                    domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);

                    BarRenderer barRenderer = (BarRenderer) chartPanel.getChart().getCategoryPlot().getRenderer();
                    barRenderer.setSeriesPaint(0, new Color(0, 0, 0f, 0.23f));
                    barRenderer.setShadowPaint(Color.WHITE);
                    barRenderer.setBaseFillPaint(Color.decode("#07CF61"));
                    barRenderer.setBarPainter(new StandardBarPainter());
                    barRenderer.setBaseLegendTextFont(MyVars.tahomaPlainFont11);

                    JLabel titleLabel = new JLabel(" E. V.");
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
                            new Thread(new Runnable() {@Override public void run() {showDistribution();}}).start();
                        }
                    });

                    JPanel enlargePanel = new JPanel();
                    enlargePanel.setBackground(Color.WHITE);
                    enlargePanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 3));
                    enlargePanel.add(enlargeBtn);

                    JPanel topPanel = new JPanel();
                    topPanel.setLayout(new BorderLayout(3, 3));
                    topPanel.setBackground(Color.WHITE);
                    topPanel.add(titlePanel, BorderLayout.WEST);
                    topPanel.add(enlargePanel, BorderLayout.EAST);

                    add(chartPanel, BorderLayout.CENTER);
                    add(topPanel, BorderLayout.NORTH);
                }
            }
        } catch (Exception ex) {}
        revalidate();
        repaint();
    }

    private JFreeChart setChart() {
        try {
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            int totalCnt = 0;
            int totalValue = 0;
            TreeMap<Integer, Integer> mapByEdgeValue = new TreeMap<>();
            Collection<MyEdge> edges = MyVars.g.getEdges();
            for (MyEdge e : edges) {
                if (e.getCurrentValue() == 0) continue;
                int value = (int)e.getCurrentValue();
                totalCnt++;
                totalValue += value;
                if (mapByEdgeValue.containsKey(value)) {
                    mapByEdgeValue.put(value, mapByEdgeValue.get(value)+1);
                } else {mapByEdgeValue.put(value, 1);}
            }
            if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() > 6) {
                for (Integer value : mapByEdgeValue.keySet()) {
                    dataset.addValue(mapByEdgeValue.get(value), "E. V.", value);
                }
            } else {
                for (Integer value : mapByEdgeValue.keySet()) {
                    dataset.addValue(mapByEdgeValue.get(value), "E. V.", value);
                }
            }
            String plotTitle = "";
            String avg = MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat((double) totalValue/totalCnt));
            String xAxis = "[AVG.: " + avg + "]";
            String yAxis = "";
            PlotOrientation orientation = PlotOrientation.VERTICAL;
            boolean show = false;
            boolean toolTips = true;
            boolean urls = false;
            return ChartFactory.createBarChart(plotTitle, xAxis, yAxis, dataset, orientation, show, toolTips, urls);
        } catch (Exception ex) {}
        return null;
    }

    public void showDistribution() {
        MyDepthLevelEdgeCurrentValueDistribution edgeCurrentValueDistribution = new MyDepthLevelEdgeCurrentValueDistribution();
        edgeCurrentValueDistribution.setLayout(new BorderLayout(3,3));
        edgeCurrentValueDistribution.setBackground(Color.WHITE);

        ChartPanel chartPanel = new ChartPanel(this.setChart());
        chartPanel.getChart().getCategoryPlot().setRangeGridlinePaint(Color.DARK_GRAY);
        chartPanel.getChart().getCategoryPlot().setDomainGridlinePaint(Color.DARK_GRAY);
        chartPanel.getChart().getCategoryPlot().setBackgroundPaint(Color.WHITE);
        chartPanel.getChart().getCategoryPlot().getDomainAxis().setTickLabelFont(MyVars.tahomaPlainFont11);
        chartPanel.getChart().getCategoryPlot().getDomainAxis().setLabelFont(MyVars.tahomaPlainFont11);
        chartPanel.getChart().getCategoryPlot().getRangeAxis().setTickLabelFont(MyVars.tahomaPlainFont11);
        chartPanel.getChart().getCategoryPlot().getRangeAxis().setLabelFont(MyVars.tahomaPlainFont11);
        chartPanel.getChart().getCategoryPlot().getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        CategoryAxis domainAxis = chartPanel.getChart().getCategoryPlot().getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);

        BarRenderer barRenderer = (BarRenderer)chartPanel.getChart().getCategoryPlot().getRenderer();
        barRenderer.setSeriesPaint(0,  new Color(0, 0, 0f, 0.23f));
        barRenderer.setShadowPaint(Color.WHITE);
        barRenderer.setBaseFillPaint(Color.decode("#07CF61"));
        barRenderer.setBarPainter(new StandardBarPainter());
        barRenderer.setBaseLegendTextFont(MyVars.tahomaPlainFont11);

        edgeCurrentValueDistribution.add(chartPanel, BorderLayout.CENTER);

        edgeCurrentValueDistribution.distFrame = new JFrame(" EDGE VALUE DISTRIBUTION");
        edgeCurrentValueDistribution.distFrame.setLayout(new BorderLayout(3,3));
        edgeCurrentValueDistribution.distFrame.getContentPane().add(edgeCurrentValueDistribution, BorderLayout.CENTER);
        edgeCurrentValueDistribution.distFrame.setPreferredSize(new Dimension(450, 350));
        edgeCurrentValueDistribution.distFrame.pack();
        edgeCurrentValueDistribution.distFrame.addWindowListener(new WindowListener() {
            @Override public void windowOpened(WindowEvent e) {}
            @Override public void windowClosing(WindowEvent e) {}
            @Override public void windowClosed(WindowEvent e) {}
            @Override public void windowIconified(WindowEvent e) {}
            @Override public void windowDeiconified(WindowEvent e) {}
            @Override public void windowActivated(WindowEvent e) {}
            @Override public void windowDeactivated(WindowEvent e) {}
        });
        edgeCurrentValueDistribution.distFrame.setVisible(true);
    }
}
