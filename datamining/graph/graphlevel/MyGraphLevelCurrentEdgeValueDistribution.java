package datamining.graph.graphlevel;

import datamining.graph.MyDirectEdge;
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

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.TreeMap;

public class MyGraphLevelCurrentEdgeValueDistribution
extends JPanel {

    protected static int instances = 0;
    public MyGraphLevelCurrentEdgeValueDistribution() {
        this.decorate();
    }

    public void decorate() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                removeAll();
                setLayout(new BorderLayout(3,3));
                setBackground(Color.WHITE);

                ChartPanel chartPanel = new ChartPanel(setChart());
                chartPanel.getChart().getCategoryPlot().setRangeGridlinePaint(Color.DARK_GRAY);
                chartPanel.getChart().getCategoryPlot().setDomainGridlinePaint(Color.DARK_GRAY);
                chartPanel.getChart().getCategoryPlot().setBackgroundPaint(Color.WHITE);
                chartPanel.getChart().getCategoryPlot().getDomainAxis().setTickLabelFont(MyVars.tahomaPlainFont11);
                chartPanel.getChart().getCategoryPlot().getDomainAxis().setLabelFont(MyVars.tahomaPlainFont11);
                chartPanel.getChart().getCategoryPlot().getRangeAxis().setTickLabelFont(MyVars.tahomaPlainFont11);
                chartPanel.getChart().getCategoryPlot().getRangeAxis().setLabelFont(MyVars.tahomaPlainFont11);

                CategoryAxis domainAxis = chartPanel.getChart().getCategoryPlot().getDomainAxis();
                domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);

                BarRenderer barRenderer = (BarRenderer)chartPanel.getChart().getCategoryPlot().getRenderer();
                barRenderer.setSeriesPaint(0,  new Color(0, 0, 0f, 0.23f));
                barRenderer.setShadowPaint(Color.WHITE);
                barRenderer.setBaseFillPaint(Color.decode("#07CF61"));
                barRenderer.setBarPainter(new StandardBarPainter());
                barRenderer.setBaseLegendTextFont(MyVars.tahomaPlainFont11);

                JLabel titleLabel = new JLabel(" E. V.");
                titleLabel.setToolTipText("EDGE VALUE DISTRIBUTION.");
                titleLabel.setFont(MyVars.tahomaBoldFont10);
                titleLabel.setBackground(Color.WHITE);
                titleLabel.setForeground(Color.DARK_GRAY);

                JPanel titlePanel = new JPanel();
                titlePanel.setBackground(Color.WHITE);
                titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3,3));
                titlePanel.add(titleLabel);

                JButton enlargeBtn = new JButton("+");
                enlargeBtn.setFocusable(false);
                enlargeBtn.setBackground(Color.WHITE);
                enlargeBtn.setFont(MyVars.tahomaPlainFont10);
                enlargeBtn.addActionListener(new ActionListener() {
                    @Override public void actionPerformed(ActionEvent e) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override public void run() {
                                new Thread(new Runnable() {
                                    @Override public void run() {
                                        enlarge();
                                    }
                                }).start();
                            }
                        });
                    }
                });

                JPanel enlargePanel = new JPanel();
                enlargePanel.setBackground(Color.WHITE);
                enlargePanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3,3));
                enlargePanel.add(enlargeBtn);

                JPanel topPanel = new JPanel();
                topPanel.setBackground(Color.WHITE);
                topPanel.setLayout(new BorderLayout(3,3));
                topPanel.add(titlePanel, BorderLayout.WEST);
                topPanel.add(enlargePanel, BorderLayout.EAST);

                add(chartPanel, BorderLayout.CENTER);
                if (instances == 0) {
                    add(topPanel, BorderLayout.NORTH);
                }
                revalidate();
                repaint();
            }
        });
    }

    public void enlarge() {
        JFrame frame = new JFrame(" EDGE VALUE DISTRIBUTION.");
        frame.setLayout(new BorderLayout(3,3));
        frame.getContentPane().add(new MyGraphLevelCurrentEdgeValueDistribution(), BorderLayout.CENTER);
        frame.setPreferredSize(new Dimension(550, 450));
        frame.pack();
        frame.setVisible(true);
    }

    private JFreeChart setChart() {
        int totalCnt = 0;
        int totalValue = 0;
        TreeMap<Integer, Integer> mapByEdgeValue = new TreeMap<>();
        Collection<MyDirectEdge> edges = MyVars.directMarkovChain.getEdges();
        for (MyDirectEdge e : edges) {
            int value = (int)e.getCurrentValue();
            if (value > 0) {
                totalCnt++;
                totalValue += value;
                if (mapByEdgeValue.containsKey(value)) {mapByEdgeValue.put(value, mapByEdgeValue.get(value)+1);}
                else {mapByEdgeValue.put(value, 1);}
            }
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Integer value : mapByEdgeValue.keySet()) {
            dataset.addValue(mapByEdgeValue.get(value), "E. V.", MyVars.getDirectGraphViewer().directGraphViewerControlPanel.edgeValueComboBoxMenu.getSelectedIndex() == 1 ? value : value);
        }

        String avgValue = MyMathUtil.twoDecimalFormat((double)totalValue/totalCnt);

        String plotTitle = "";
        String xaxis = "[AVG.: " + MySysUtil.formatAverageValue(avgValue)+"]";
        String yaxis = "";
        PlotOrientation orientation = PlotOrientation.VERTICAL;
        boolean show = false;
        boolean toolTips = true;
        boolean urls = false;
        return ChartFactory.createBarChart(plotTitle, xaxis, yaxis, dataset, orientation, show, toolTips, urls);
    }
}
