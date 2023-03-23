package datamining.graph.multilevel;

import datamining.main.MyProgressBar;
import datamining.graph.MyDirectEdge;
import datamining.graph.MyDirectNode;
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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

public class MyMultiNodeLevelPredecessorEdgeValueDistributionLineChart
extends JPanel {

    private boolean MAXIMIZED;
    public static int instances = 0;
    private boolean isValueExists = false;


    public MyMultiNodeLevelPredecessorEdgeValueDistributionLineChart() {
        this.decorate();
    }

    public void decorate() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                try {
                    removeAll();
                    setLayout(new BorderLayout(3, 3));
                    setBackground(Color.WHITE);

                    JFreeChart chart = null;
                    if (MyVars.getDirectGraphViewer().directGraphViewerControlPanel.edgeValueComboBoxMenu.getSelectedIndex() > 0) {
                        chart = setChart();
                    } else {
                        isValueExists = false;
                    }

                    if (!isValueExists) {
                            JLabel titleLabel = new JLabel(" P. E. V.");
                            titleLabel.setToolTipText("PREDECESSOR EDGE VALUE DISTRIBUTION");
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
                            add(topPanel, BorderLayout.NORTH);
                            add(msg, BorderLayout.CENTER);
                    } else {
                        ChartPanel chartPanel = new ChartPanel(chart);
                        chartPanel.getChart().getCategoryPlot().setRangeGridlinePaint(Color.DARK_GRAY);
                        chartPanel.getChart().getCategoryPlot().setDomainGridlinePaint(Color.DARK_GRAY);
                        chartPanel.getChart().getCategoryPlot().setBackgroundPaint(Color.WHITE);
                        chartPanel.getChart().getCategoryPlot().getDomainAxis().setTickLabelFont(MyVars.tahomaPlainFont11);
                        chartPanel.getChart().getCategoryPlot().getDomainAxis().setLabelFont(MyVars.tahomaPlainFont11);
                        chartPanel.getChart().getCategoryPlot().getRangeAxis().setTickLabelFont(MyVars.tahomaPlainFont11);
                        chartPanel.getChart().getCategoryPlot().getRangeAxis().setLabelFont(MyVars.tahomaPlainFont11);

                        CategoryAxis domainAxis = chartPanel.getChart().getCategoryPlot().getDomainAxis();
                        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);

                        BarRenderer barRenderer = (BarRenderer) chartPanel.getChart().getCategoryPlot().getRenderer();
                        barRenderer.setSeriesPaint(0, new Color(0, 0, 0, 0.25f));//Color.LIGHT_GRAY);//Color.decode("#2084FE"));
                        barRenderer.setShadowPaint(Color.WHITE);
                        barRenderer.setBaseFillPaint(Color.decode("#07CF61"));
                        barRenderer.setBarPainter(new StandardBarPainter());
                        barRenderer.setBaseLegendTextFont(MyVars.tahomaPlainFont11);

                        JLabel titleLabel = new JLabel(" P. E. V.");
                        titleLabel.setToolTipText("PREDECESSOR EDGE VALUE DISTRIBUTION");
                        titleLabel.setFont(MyVars.tahomaBoldFont11);
                        titleLabel.setBackground(Color.WHITE);
                        titleLabel.setForeground(Color.DARK_GRAY);

                        JPanel titlePanel = new JPanel();
                        titlePanel.setBackground(Color.WHITE);
                        titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
                        titlePanel.add(titleLabel);

                        JButton enlargeBtn = new JButton("+");
                        enlargeBtn.setToolTipText("ENLARGE");
                        enlargeBtn.setFont(MyVars.tahomaPlainFont10);
                        enlargeBtn.setBackground(Color.WHITE);
                        enlargeBtn.setFocusable(false);
                        enlargeBtn.addActionListener(new ActionListener() {
                            @Override public void actionPerformed(ActionEvent e) {
                                new Thread(new Runnable() {
                                    @Override public void run() {
                                        enlarge();
                                    }
                                }).start();
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
                    revalidate();
                    repaint();
                } catch (Exception ex) {ex.printStackTrace();}
            }
        });
    }

    private JFreeChart setChart() {
        try {
            int totalCnt = 0;
            double totalValue = 0;
            TreeMap<Integer, Integer> valueMap = new TreeMap<>();
            for (MyDirectNode n : MyVars.getDirectGraphViewer().multiNodes) {
                Set<MyDirectEdge> edges = new HashSet<>(MyVars.directMarkovChain.getInEdges(n));
                for (MyDirectEdge e : edges) {
                    if (e.getCurrentValue() <= 0) continue;
                    int value = (int) e.getCurrentValue();
                    totalCnt++;
                    totalValue += e.getCurrentValue();
                    if (!MAXIMIZED) {
                        if (valueMap.size() <= 15) {
                            if (valueMap.containsKey(value)) {
                                valueMap.put(value, valueMap.get(value) + 1);
                            } else {
                                valueMap.put(value, 1);
                            }
                        }
                    } else if (valueMap.size() <= 200) {
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
                dataset.addValue(valueMap.get(value), "P. E. V.", value);
            }

            if (totalCnt == 0) {
                totalCnt = 1;
            } else {
                isValueExists = true;
            }

            String avgValue = MyMathUtil.twoDecimalFormat((double) totalValue / totalCnt);
            String plotTitle = "";
            String xAxis = "[AVG.: " + MySysUtil.formatAverageValue(avgValue) + "]";
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
            MAXIMIZED = true;
            JFrame frame = new JFrame(" PREDECESSOR EDGE VALUE DISTRIBUTION");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    super.windowClosing(e);
                    MAXIMIZED = false;
                }
            });
            frame.setLayout(new BorderLayout(3, 3));
            frame.getContentPane().add(new MyMultiNodeLevelPredecessorEdgeValueDistributionLineChart(), BorderLayout.CENTER);
            frame.setPreferredSize(new Dimension(400, 300));
            frame.pack();
            frame.setAlwaysOnTop(true);
            frame.setVisible(true);
            frame.setAlwaysOnTop(false);
        } catch (Exception ex) {
            MAXIMIZED = false;
            pb.updateValue(100, 100);
            pb.dispose();
        }
    }
}
