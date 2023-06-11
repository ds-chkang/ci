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

public class MyDirectGraphNodeLevelEdgeValueDistribution
extends JPanel {

    protected boolean isValueExists = false;
    protected boolean MAXIMIZED;

    public MyDirectGraphNodeLevelEdgeValueDistribution() {
        this.decorate();
    }

    public void decorate() {
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
                    JLabel titleLabel = new JLabel(" E. V.");
                    titleLabel.setToolTipText("EDGE VALUE DISTRIBUTION");
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

                    JLabel titleLabel = new JLabel(" E. V.");
                    titleLabel.setToolTipText("EDGE VALUE DISTRIBUTION");
                    titleLabel.setFont(MyDirectGraphVars.tahomaBoldFont12);
                    titleLabel.setBackground(Color.WHITE);
                    titleLabel.setForeground(Color.DARK_GRAY);

                    JPanel titlePanel = new JPanel();
                    titlePanel.setBackground(Color.WHITE);
                    titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
                    titlePanel.add(titleLabel);

                    JButton enlargeBtn = new JButton("+");
                    enlargeBtn.setToolTipText("ENLARGE");
                    enlargeBtn.setFocusable(false);
                    enlargeBtn.setBackground(Color.WHITE);
                    enlargeBtn.setFont(MyDirectGraphVars.tahomaPlainFont12);
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
                    enlargePanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 3));
                    enlargePanel.add(enlargeBtn);

                    JPanel topPanel = new JPanel();
                    topPanel.setBackground(Color.WHITE);
                    topPanel.setLayout(new BorderLayout(3, 3));
                    topPanel.add(titlePanel, BorderLayout.WEST);
                    topPanel.add(enlargePanel, BorderLayout.EAST);

                    add(chartPanel, BorderLayout.CENTER);
                    add(topPanel, BorderLayout.NORTH);

                    revalidate();
                    repaint();
                }
            }
        });
    }

    public void enlarge() {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            MyDirectGraphNodeLevelEdgeValueDistribution nodeLevelEdgeValueDistribution = new MyDirectGraphNodeLevelEdgeValueDistribution();
            nodeLevelEdgeValueDistribution.MAXIMIZED = true;
            JFrame f = new JFrame(" EDGE VALUE DISTRIBUTION");
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
            f.setLayout(new BorderLayout(3, 3));
            f.getContentPane().add(nodeLevelEdgeValueDistribution, BorderLayout.CENTER);
            f.setPreferredSize(new Dimension(550, 450));
            f.pack();
            f.setAlwaysOnTop(true);
            f.setVisible(true);
            f.setAlwaysOnTop(false);
            pb.updateValue(100, 100);
            pb.dispose();
        } catch (Exception ex) {
            pb.updateValue(100, 100);
            pb.dispose();
        }
    }

    private JFreeChart setChart() {
        double totalValue = 0;
        long totalCnt = 0;
        TreeMap<Integer, Integer> valueMap = new TreeMap<>();
        Collection<MyDirectEdge> edges = MyDirectGraphVars.directGraph.getIncidentEdges(MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode);
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

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Integer value : valueMap.keySet()) {
            dataset.addValue(valueMap.get(value), "E. V.", value);
        }

        totalCnt = (totalCnt == 0 ? 1 : totalCnt);
        String avgValue = MyDirectGraphMathUtil.twoDecimalFormat(totalValue/totalCnt);
        String plotTitle = "";
        String xaxis = "[AVG.: " + MyDirectGraphSysUtil.formatAverageValue(avgValue)+"]";
        String yaxis = "";
        PlotOrientation orientation = PlotOrientation.VERTICAL;
        boolean show = false;
        boolean toolTips = true;
        boolean urls = false;
        return ChartFactory.createBarChart(plotTitle, xaxis, yaxis, dataset, orientation, show, toolTips, urls);
    }
}
