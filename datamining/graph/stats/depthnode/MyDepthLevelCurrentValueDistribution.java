package datamining.graph.stats.depthnode;

import datamining.graph.MyNode;
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
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.TreeMap;

public class MyDepthLevelCurrentValueDistribution
extends JPanel {

    private JFrame distFrame;
    public MyDepthLevelCurrentValueDistribution() {
        decorate();
    }

    public void decorate() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    removeAll();
                    setLayout(new BorderLayout(3, 3));
                    setBackground(Color.WHITE);

                    int total = 0;
                    Collection<MyNode> nodes = MyVars.g.getDepthNodes();
                    for (MyNode n : nodes) {
                        if ((int)n.getCurrentValue() > 0) {
                            total = 1;
                            break;
                        }
                    }
                    if (total == 0) {
                        JLabel titleLabel = new JLabel(" DEPTHNODE V.");
                        titleLabel.setFont(MyVars.tahomaBoldFont11);
                        titleLabel.setBackground(Color.WHITE);
                        titleLabel.setForeground(Color.DARK_GRAY);

                        JPanel titlePanel = new JPanel();
                        titlePanel.setBackground(Color.WHITE);
                        titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
                        titlePanel.add(titleLabel);

                        JPanel topPanel = new JPanel();
                        topPanel.setLayout(new BorderLayout(3, 6));
                        topPanel.setBackground(Color.WHITE);
                        topPanel.add(titlePanel, BorderLayout.WEST);

                        JLabel msg = new JLabel("N/A");
                        msg.setFont(MyVars.tahomaPlainFont12);
                        msg.setBackground(Color.WHITE);
                        msg.setHorizontalAlignment(JLabel.CENTER);
                        add(topPanel, BorderLayout.NORTH);
                        add(msg, BorderLayout.CENTER);
                    } else {
                        ChartPanel chartPanel = new ChartPanel(setChart());
                        chartPanel.getChart().getCategoryPlot().setRangeGridlinePaint(Color.DARK_GRAY);
                        chartPanel.getChart().getCategoryPlot().setDomainGridlinePaint(Color.DARK_GRAY);
                        chartPanel.getChart().getCategoryPlot().setBackgroundPaint(Color.WHITE);
                        chartPanel.getChart().getCategoryPlot().getDomainAxis().setTickLabelFont(MyVars.tahomaPlainFont11);
                        chartPanel.getChart().getCategoryPlot().getDomainAxis().setLabelFont(MyVars.tahomaPlainFont11);
                        chartPanel.getChart().getCategoryPlot().getRangeAxis().setTickLabelFont(MyVars.tahomaPlainFont11);
                        chartPanel.getChart().getCategoryPlot().getRangeAxis().setLabelFont(MyVars.tahomaPlainFont11);
                        final NumberAxis axis1 = (NumberAxis)chartPanel.getChart().getCategoryPlot().getRangeAxis(0);
                        DecimalFormat df = new DecimalFormat("0");
                        axis1.setNumberFormatOverride(df);
                        CategoryAxis domainAxis = chartPanel.getChart().getCategoryPlot().getDomainAxis();
                        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);

                        BarRenderer barRenderer = (BarRenderer) chartPanel.getChart().getCategoryPlot().getRenderer();
                        barRenderer.setSeriesPaint(0, new Color(0, 0, 0f, 0.23f));
                        barRenderer.setShadowPaint(Color.WHITE);
                        barRenderer.setBaseFillPaint(Color.decode("#07CF61"));
                        barRenderer.setBarPainter(new StandardBarPainter());
                        barRenderer.setBaseLegendTextFont(MyVars.tahomaPlainFont11);

                        JLabel titleLabel = new JLabel(" DEPTHNODE V.");
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
                                enlarge();
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
                } catch (Exception ex) {}
                revalidate();
                repaint();
            }
        });
    }

    public void enlarge() {
        MyDepthLevelCurrentValueDistribution currentValueDistribution = new MyDepthLevelCurrentValueDistribution();
        currentValueDistribution.setLayout(new BorderLayout(3,3));
        currentValueDistribution.setBackground(Color.WHITE);

        ChartPanel chartPanel = new ChartPanel(currentValueDistribution.setChart());
        chartPanel.getChart().getCategoryPlot().setRangeGridlinePaint(Color.DARK_GRAY);
        chartPanel.getChart().getCategoryPlot().setDomainGridlinePaint(Color.DARK_GRAY);
        chartPanel.getChart().getCategoryPlot().setBackgroundPaint(Color.WHITE);
        chartPanel.getChart().getCategoryPlot().getDomainAxis().setTickLabelFont(MyVars.tahomaPlainFont11);
        chartPanel.getChart().getCategoryPlot().getDomainAxis().setLabelFont(MyVars.tahomaPlainFont11);
        chartPanel.getChart().getCategoryPlot().getRangeAxis().setTickLabelFont(MyVars.tahomaPlainFont11);
        chartPanel.getChart().getCategoryPlot().getRangeAxis().setLabelFont(MyVars.tahomaPlainFont11);

        final NumberAxis axis1 = (NumberAxis)chartPanel.getChart().getCategoryPlot().getRangeAxis(0);
        DecimalFormat df = new DecimalFormat("0");
        axis1.setNumberFormatOverride(df);
        CategoryAxis domainAxis = chartPanel.getChart().getCategoryPlot().getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);

        BarRenderer barRenderer = (BarRenderer)chartPanel.getChart().getCategoryPlot().getRenderer();
        barRenderer.setSeriesPaint(0,  new Color(0, 0, 0f, 0.23f));
        barRenderer.setShadowPaint(Color.WHITE);
        barRenderer.setBaseFillPaint(Color.decode("#07CF61"));
        barRenderer.setBarPainter(new StandardBarPainter());
        barRenderer.setBaseLegendTextFont(MyVars.tahomaPlainFont11);

        currentValueDistribution.add(chartPanel, BorderLayout.CENTER);
        currentValueDistribution.distFrame = new JFrame(" DEPTH NODE VALUE DISTRIBUTION");
        currentValueDistribution.distFrame.setLayout(new BorderLayout(3,3));
        currentValueDistribution.distFrame.getContentPane().add(currentValueDistribution, BorderLayout.CENTER);
        currentValueDistribution.distFrame.setPreferredSize(new Dimension(450, 350));
        currentValueDistribution.distFrame.pack();
        currentValueDistribution.distFrame.addWindowListener(new WindowListener() {
            @Override public void windowOpened(WindowEvent e) {}
            @Override public void windowClosing(WindowEvent e) {}
            @Override public void windowClosed(WindowEvent e) {}
            @Override public void windowIconified(WindowEvent e) {}
            @Override public void windowDeiconified(WindowEvent e) {}
            @Override public void windowActivated(WindowEvent e) {}
            @Override public void windowDeactivated(WindowEvent e) {}
        });
        currentValueDistribution.distFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        currentValueDistribution.distFrame.setVisible(true);
    }

    private JFreeChart setChart() {
        try {
            int totalCnt = 0;
            int totalValue = 0;
            TreeMap<Integer, Integer> nodeValueMap = new TreeMap<>();
            Collection<MyNode> nodes = MyVars.g.getVertices();
            for (MyNode n : nodes) {
                if (n.getCurrentValue() > 0) {
                    int value = (int)n.getCurrentValue();
                    totalCnt++;
                    totalValue += value;
                    if (nodeValueMap.containsKey(value)) {nodeValueMap.put(value, nodeValueMap.get(value)+1);}
                    else {nodeValueMap.put(value, 1);}
                }
            }

            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            for (Integer value : nodeValueMap.keySet()) {
                dataset.addValue(nodeValueMap.get(value), "NODE VALUE", value);
            }

            double avgValue = (double) totalValue/totalCnt;

            String plotTitle = "";
            String xaxis = "[AVG.: " + MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(avgValue)) + "]";
            String yaxis = "";
            PlotOrientation orientation = PlotOrientation.VERTICAL;
            boolean show = false;
            boolean toolTips = true;
            boolean urls = false;
            return ChartFactory.createBarChart(plotTitle, xaxis, yaxis, dataset, orientation, show, toolTips, urls);
        } catch (Exception ex) {}
        return null;
    }
}
