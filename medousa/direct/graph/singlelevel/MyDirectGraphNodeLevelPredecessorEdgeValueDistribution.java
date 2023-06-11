package medousa.direct.graph.singlelevel;

import medousa.direct.graph.MyDirectEdge;
import medousa.MyProgressBar;
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
import java.util.Collection;
import java.util.TreeMap;

public class MyDirectGraphNodeLevelPredecessorEdgeValueDistribution
extends JPanel {

    private boolean isValueExists = false;
    private boolean MAXIMIZED;

    public MyDirectGraphNodeLevelPredecessorEdgeValueDistribution() {
        this.decorate();
    }

    public void decorate() {
        try {
            SwingUtilities.invokeLater(new Runnable() {
                @Override public void run() {
                    removeAll();
                    setLayout(new BorderLayout(3, 3));
                    setBackground(Color.WHITE);

                    JFreeChart chart = null;
                    if (MyDirectGraphVars.getDirectGraphViewer().vc.edgeValueComboBoxMenu.getSelectedIndex() > 0) {
                        chart = setChart();
                    } else {
                        isValueExists = false;
                    }

                    if (!isValueExists) {
                        JLabel titleLabel = new JLabel(" P. E. V.");
                        titleLabel.setToolTipText("PREDECESSOR EDGE VALUE DISTRIBUTION");
                        titleLabel.setFont(MyDirectGraphVars.tahomaBoldFont12);
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
                        barRenderer.setSeriesPaint(0, new Color(0, 0, 0f, 0.23f));
                        barRenderer.setShadowPaint(Color.WHITE);
                        barRenderer.setBaseFillPaint(Color.decode("#07CF61"));
                        barRenderer.setBarPainter(new StandardBarPainter());
                        barRenderer.setBaseLegendTextFont(MyDirectGraphVars.tahomaPlainFont11);

                        JLabel titleLabel = new JLabel(" P. E. V.");
                        titleLabel.setToolTipText("PREDECESSOR EDGE VALUE DISTRIBUTION");
                        titleLabel.setFont(MyDirectGraphVars.tahomaBoldFont12);
                        titleLabel.setBackground(Color.WHITE);
                        titleLabel.setForeground(Color.DARK_GRAY);

                        JPanel titlePanel = new JPanel();
                        titlePanel.setBackground(Color.WHITE);
                        titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
                        titlePanel.add(titleLabel);

                        JButton enlargeBtn = new JButton("+");
                        enlargeBtn.setBackground(Color.WHITE);
                        enlargeBtn.setFont(MyDirectGraphVars.tahomaPlainFont12);
                        enlargeBtn.setFocusable(false);
                        enlargeBtn.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                SwingUtilities.invokeLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                enlarge();
                                            }
                                        }).start();
                                    }
                                });
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
                }
            });

        } catch (Exception ex) {ex.printStackTrace();}
    }

    private JFreeChart setChart() {
        try {
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            long totalCnt = 0;
            double totalValue = 0;
            TreeMap<Integer, Integer> valueMap = new TreeMap<>();
            Collection<MyDirectEdge> edges = MyDirectGraphVars.directGraph.getInEdges(MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode);
            for (MyDirectEdge e : edges) {
                if (e.getCurrentValue() <= 0) continue;
                int value = (int) e.getCurrentValue();
                totalCnt++;
                totalValue += e.getCurrentValue();
                if (valueMap.containsKey(value)) {
                    valueMap.put(value, valueMap.get(value) + 1);
                } else {
                    valueMap.put(value, 1);
                }
                isValueExists = true;
            }

            for (Integer value : valueMap.keySet()) {
                dataset.addValue(valueMap.get(value), "P. E. V.", value);
            }

            totalCnt = (totalCnt == 0 ? 1 : totalCnt);
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
            MyDirectGraphNodeLevelPredecessorEdgeValueDistribution nodeLevelPredecessorEdgeValueDistribution = new MyDirectGraphNodeLevelPredecessorEdgeValueDistribution();
            nodeLevelPredecessorEdgeValueDistribution.MAXIMIZED = true;
            JFrame f = new JFrame(" PREDECESSOR EDGE VALUE DISTRIBUTION");
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    f.setAlwaysOnTop(true);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    super.mouseExited(e);
                    f.setAlwaysOnTop(false);
                }
            });
            f.setLayout(new BorderLayout(3, 3));
            f.getContentPane().add(nodeLevelPredecessorEdgeValueDistribution, BorderLayout.CENTER);
            f.setPreferredSize(new Dimension(400, 450));
            f.pack();
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
