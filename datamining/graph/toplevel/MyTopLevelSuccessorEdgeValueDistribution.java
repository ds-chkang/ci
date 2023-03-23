package datamining.graph.toplevel;

import datamining.graph.MyDirectEdge;
import datamining.graph.MyDirectNode;
import datamining.main.MyProgressBar;
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
import java.util.Collection;
import java.util.TreeMap;

public class MyTopLevelSuccessorEdgeValueDistribution
extends JPanel {

    protected static int instances = 0;
    private boolean isValueExists = false;
    protected boolean MAXIMIZED;

    public MyTopLevelSuccessorEdgeValueDistribution() {
        this.decorate();
    }

    public void decorate() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                removeAll();
                setLayout(new BorderLayout(3,3));
                setBackground(Color.WHITE);

                JFreeChart chart = null;
                if (MyVars.getDirectGraphViewer().directGraphViewerControlPanel.edgeValueComboBoxMenu.getSelectedIndex() > 0) {
                    chart = setChart();
                } else {
                    isValueExists = false;
                }

                if (!isValueExists) {
                    JLabel titleLabel = new JLabel(" S. E. V.");
                    titleLabel.setToolTipText("SUCCESSOR EDGE VALUE DISTRIBUTION");
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
                    revalidate();
                    repaint();
                } else {
                    try {
                        ChartPanel chartPanel = new ChartPanel(chart);
                        chartPanel.getChart().getCategoryPlot().setRangeGridlinePaint(Color.DARK_GRAY);
                        chartPanel.getChart().getCategoryPlot().setDomainGridlinePaint(Color.DARK_GRAY);
                        chartPanel.getChart().getCategoryPlot().setBackgroundPaint(Color.WHITE);
                        chartPanel.getChart().getCategoryPlot().getRangeAxis().setTickLabelFont(MyVars.tahomaPlainFont10);
                        chartPanel.getChart().getCategoryPlot().getDomainAxis().setTickLabelFont(MyVars.tahomaPlainFont10);
                        chartPanel.getChart().getCategoryPlot().getRangeAxis().setLabelFont(MyVars.tahomaPlainFont10);
                        chartPanel.getChart().getCategoryPlot().getDomainAxis().setLabelFont(MyVars.tahomaPlainFont10);

                        CategoryAxis domainAxis = chartPanel.getChart().getCategoryPlot().getDomainAxis();
                        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);

                        BarRenderer barRenderer = (BarRenderer) chartPanel.getChart().getCategoryPlot().getRenderer();
                        barRenderer.setSeriesPaint(0, new Color(0, 0, 0, 0.25f));//Color.LIGHT_GRAY);//Color.decode("#2084FE"));
                        barRenderer.setShadowPaint(Color.WHITE);
                        barRenderer.setBaseFillPaint(Color.decode("#07CF61"));
                        barRenderer.setBarPainter(new StandardBarPainter());
                        barRenderer.setBaseLegendTextFont(MyVars.tahomaPlainFont11);

                        JLabel titleLabel = new JLabel(" S. E. V.");
                        titleLabel.setToolTipText("SUCCESSOR EDGE VALUE DISTRIBUTION");
                        titleLabel.setFont(MyVars.tahomaBoldFont10);
                        titleLabel.setBackground(Color.WHITE);
                        titleLabel.setForeground(Color.DARK_GRAY);

                        JPanel titlePanel = new JPanel();
                        titlePanel.setBackground(Color.WHITE);
                        titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
                        titlePanel.add(titleLabel);

                        JButton enlargeBtn = new JButton("+");
                        enlargeBtn.setToolTipText("ENLARGE");
                        enlargeBtn.setBackground(Color.WHITE);
                        enlargeBtn.setFocusable(false);
                        enlargeBtn.setFont(MyVars.tahomaPlainFont10);
                        enlargeBtn.addActionListener(new ActionListener() {
                            @Override public void actionPerformed(ActionEvent e) {
                                SwingUtilities.invokeLater(new Runnable() {
                                    @Override public void run() {
                                        enlarge();
                                    }
                                });
                            }
                        });

                        JPanel enlargePanel = new JPanel();
                        enlargePanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 3));
                        enlargePanel.setBackground(Color.WHITE);
                        enlargePanel.add(enlargeBtn);

                        JPanel topPanel = new JPanel();
                        topPanel.setBackground(Color.WHITE);
                        topPanel.setLayout(new BorderLayout(3, 3));
                        topPanel.add(titlePanel, BorderLayout.WEST);
                        topPanel.add(enlargePanel, BorderLayout.EAST);

                        setBackground(Color.WHITE);
                        add(chartPanel, BorderLayout.CENTER);
                        add(topPanel, BorderLayout.NORTH);
                    } catch (Exception ex) {}
                }
                revalidate();
                repaint();
            }
        });
    }

    public void enlarge() {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            MAXIMIZED = true;
            JFrame f = new JFrame(" SUCCESSOR EDGE VALUE DISTRIBUTION");
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    super.windowClosing(e);
                    MAXIMIZED = false;
                }
            });
            f.setLayout(new BorderLayout(3, 3));
            f.getContentPane().add(new MyTopLevelSuccessorEdgeValueDistribution(), BorderLayout.CENTER);
            f.setPreferredSize(new Dimension(550, 450));
            f.pack();
            f.setAlwaysOnTop(true);
            pb.updateValue(100, 100);
            pb.dispose();
            f.setVisible(true);
            f.setAlwaysOnTop(false);
        } catch (Exception ex) {
            MAXIMIZED = false;
            pb.updateValue(100, 100);
            pb.dispose();
        }
    }

    private JFreeChart setChart() {
        int cnt = 0;
        double totalValue = 0;
        TreeMap<Double, Integer> valueMap = new TreeMap<>();
        Collection<MyDirectNode> nodes = MyVars.directMarkovChain.getVertices();
        for (MyDirectNode n : nodes) {
            Collection<MyDirectEdge> edges = MyVars.directMarkovChain.getOutEdges(n);
            for (MyDirectEdge e : edges) {
                if (e.getCurrentValue() <= 0) continue;
                double value = e.getCurrentValue();
                cnt++;
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
                isValueExists = true;
            }
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Double successorValue : valueMap.keySet()) {
            dataset.addValue(valueMap.get(successorValue), "S. E. V.", successorValue);
        }

        if (cnt == 0) cnt = 1;
        double avg = (double)totalValue/cnt;
        String plotTitle = "";
        String xaxis = "[AVG.: " + MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(avg)) + "]";
        String yaxis = "";
        PlotOrientation orientation = PlotOrientation.VERTICAL;
        boolean show = false;
        boolean toolTips = true;
        boolean urls = false;
        return ChartFactory.createBarChart(plotTitle, xaxis, yaxis, dataset, orientation, show, toolTips, urls);
    }

}
