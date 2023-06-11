package medousa.direct.graph.multilevel;

import medousa.MyProgressBar;
import medousa.direct.graph.MyDirectEdge;
import medousa.direct.utils.MyDirectGraphMathUtil;
import medousa.direct.utils.MyDirectGraphSysUtil;
import medousa.direct.utils.MyDirectGraphVars;
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
import java.awt.event.*;
import java.util.*;

public class MyDirectGraphMultiNodeLevelSharedSuccessorEdgeValueDistributionLineChart
extends JPanel {

    private boolean isValueExists;
    private boolean MAXIMIZED;

    public MyDirectGraphMultiNodeLevelSharedSuccessorEdgeValueDistributionLineChart() {
        this.decorate();
    }
    public void decorate() {
        try {
            SwingUtilities.invokeLater(new Runnable() {
                @Override public void run() {
                    removeAll();
                    setLayout(new BorderLayout(5,5));
                    setBackground(Color.WHITE);

                    JFreeChart chart = null;
                    if (MyDirectGraphVars.getDirectGraphViewer().vc.edgeValueComboBoxMenu.getSelectedIndex() > 0) {
                        chart = setChart();
                    } else {
                        isValueExists = false;
                    }

                    if (!isValueExists) {
                        JLabel titleLabel = new JLabel(" SHARED S. E. V.");
                        titleLabel.setToolTipText("SHARED SUCCESSOR EDGE VALUE DISTRIBUTION");
                        titleLabel.setFont(MyDirectGraphVars.tahomaBoldFont11);
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
                        msg.setFont(MyDirectGraphVars.tahomaPlainFont12);
                        msg.setBackground(Color.WHITE);
                        msg.setHorizontalAlignment(JLabel.CENTER);
                        add(topPanel, BorderLayout.NORTH);
                        add(msg, BorderLayout.CENTER);

                        revalidate();
                        repaint();
                    } else {
                        ChartPanel chartPanel = new ChartPanel(chart);
                        chartPanel.getChart().getCategoryPlot().setRangeGridlinePaint(Color.DARK_GRAY);
                        chartPanel.getChart().getCategoryPlot().setDomainGridlinePaint(Color.DARK_GRAY);
                        chartPanel.getChart().getCategoryPlot().setBackgroundPaint(Color.WHITE);
                        chartPanel.getChart().getCategoryPlot().getDomainAxis().setTickLabelFont(MyDirectGraphVars.tahomaPlainFont11);
                        chartPanel.getChart().getCategoryPlot().getDomainAxis().setLabelFont(MyDirectGraphVars.tahomaPlainFont11);
                        chartPanel.getChart().getCategoryPlot().getRangeAxis().setTickLabelFont(MyDirectGraphVars.tahomaPlainFont11);
                        chartPanel.getChart().getCategoryPlot().getRangeAxis().setLabelFont(MyDirectGraphVars.tahomaPlainFont11);

                        CategoryAxis domainAxis = chartPanel.getChart().getCategoryPlot().getDomainAxis();
                        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);

                        BarRenderer barRenderer = (BarRenderer) chartPanel.getChart().getCategoryPlot().getRenderer();
                        barRenderer.setSeriesPaint(0, new Color(0, 0, 0, 0.25f));//Color.LIGHT_GRAY);//Color.decode("#2084FE"));
                        barRenderer.setShadowPaint(Color.WHITE);
                        barRenderer.setBaseFillPaint(Color.decode("#07CF61"));
                        barRenderer.setBarPainter(new StandardBarPainter());
                        barRenderer.setBaseLegendTextFont(MyDirectGraphVars.tahomaPlainFont11);

                        JLabel titleLabel = new JLabel(" SHARED S. E. V.");
                        titleLabel.setToolTipText("SHARED SUCCESSOR EDGE VALUE DISTRIBUTION");
                        titleLabel.setFont(MyDirectGraphVars.tahomaBoldFont12);
                        titleLabel.setBackground(Color.WHITE);
                        titleLabel.setForeground(Color.DARK_GRAY);

                        JPanel titlePanel = new JPanel();
                        titlePanel.setBackground(Color.WHITE);
                        titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
                        titlePanel.add(titleLabel);

                        JButton enlargeBtn = new JButton("+");
                        enlargeBtn.setToolTipText("ENLARGE");
                        enlargeBtn.setFont(MyDirectGraphVars.tahomaPlainFont12);
                        enlargeBtn.setFocusable(false);
                        enlargeBtn.setBackground(Color.WHITE);
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

                        JPanel btnPanel = new JPanel();
                        btnPanel.setBackground(Color.WHITE);
                        btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
                        btnPanel.add(enlargeBtn);

                        JPanel menuPanel = new JPanel();
                        menuPanel.setLayout(new BorderLayout(0, 0));
                        menuPanel.setBackground(Color.WHITE);
                        menuPanel.add(titlePanel, BorderLayout.WEST);
                        menuPanel.add(btnPanel, BorderLayout.CENTER);
                        barRenderer.setBaseLegendTextFont(MyDirectGraphVars.tahomaPlainFont11);
                        add(menuPanel, BorderLayout.NORTH);
                        add(chartPanel, BorderLayout.CENTER);

                        revalidate();
                        repaint();
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private JFreeChart setChart() {
        try {
            int totalCnt = 0;
            double totalValue = 0;
            Map<Integer, Integer> valueMap = new HashMap<>();
            Collection<MyDirectEdge> edges = MyDirectGraphVars.directGraph.getEdges();
            for (MyDirectEdge e : edges) {
                if (MyDirectGraphVars.getDirectGraphViewer().multiNodeSharedSuccessorSet.contains(e.getDest())) {
                    if (e.getCurrentValue() > 0) {
                        int value = (int) e.getCurrentValue();
                        totalCnt++;
                        totalValue += e.getCurrentValue();
                        if (valueMap.containsKey(value)) {
                            valueMap.put(value, valueMap.get(value) + 1);
                        } else {
                            valueMap.put(value, 1);
                        }
                    }
                }
            }

            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            for (Integer value : valueMap.keySet()) {
                dataset.addValue(valueMap.get(value), "SHARED S. E. V.", value);
            }

            if (totalCnt == 0) {
                totalCnt = 1;
            } else {
                isValueExists = true;
            }

            String avgValue = MyDirectGraphMathUtil.twoDecimalFormat((double) totalValue/totalCnt);
            String plotTitle = "";
            String xAxis = "[AVG.: " + MyDirectGraphSysUtil.formatAverageValue(avgValue) + "]";
            String yAxis = "";
            PlotOrientation orientation = PlotOrientation.VERTICAL;
            boolean show = false;
            boolean toolTips = true;
            boolean urls = false;
            return ChartFactory.createBarChart(plotTitle, xAxis, yAxis, dataset, orientation, show, toolTips, urls);
        } catch (Exception ex) {ex.printStackTrace();}
        return null;
    }

    public void enlarge() {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            MyDirectGraphMultiNodeLevelSharedSuccessorEdgeValueDistributionLineChart multiNodeLevelSharedSuccessorEdgeValueDistributionLineChart = new MyDirectGraphMultiNodeLevelSharedSuccessorEdgeValueDistributionLineChart();
            multiNodeLevelSharedSuccessorEdgeValueDistributionLineChart.MAXIMIZED = true;
            JFrame f = new JFrame(" PREDECESSOR VALUE DISTRIBUTION");
            f.setLayout(new BorderLayout());
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    f.setAlwaysOnTop(true);
                }

                @Override public void mouseExited(MouseEvent e) {
                    super.mouseExited(e);
                    f.setAlwaysOnTop(false);
                }
            });
            f.setBackground(Color.WHITE);
            f.setPreferredSize(new Dimension(400, 300));
            f.getContentPane().add(multiNodeLevelSharedSuccessorEdgeValueDistributionLineChart, BorderLayout.CENTER);
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
