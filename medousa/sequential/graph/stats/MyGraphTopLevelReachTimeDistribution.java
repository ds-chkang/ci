package medousa.sequential.graph.stats;

import medousa.MyProgressBar;
import medousa.direct.utils.MyDirectGraphVars;
import medousa.sequential.graph.MyNode;
import medousa.sequential.utils.MySequentialGraphVars;
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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class MyGraphTopLevelReachTimeDistribution
extends JPanel
implements ActionListener {

    protected boolean MAXIMIZED;
    private float maxValue = 0;
    private float minValue = 1000000000;
    private float avgValue = 0;
    private float stdValue = 0;
    private float toTime = 0f;
    private int selectedMenu = 0;

    public MyGraphTopLevelReachTimeDistribution() {}

    public void decorate() {
        MyProgressBar pb = new MyProgressBar(false);
        removeAll();
        setLayout(new BorderLayout(3, 3));
        setBackground(Color.WHITE);

        ChartPanel chartPanel = new ChartPanel(setValueChart());
        chartPanel.getChart().getCategoryPlot().setRangeGridlinePaint(Color.DARK_GRAY);
        chartPanel.getChart().getCategoryPlot().setDomainGridlinePaint(Color.DARK_GRAY);
        chartPanel.getChart().getCategoryPlot().setBackgroundPaint(Color.WHITE);
        chartPanel.getChart().getCategoryPlot().getDomainAxis().setTickLabelFont(MyDirectGraphVars.tahomaPlainFont10);
        chartPanel.getChart().getCategoryPlot().getDomainAxis().setLabelFont(MyDirectGraphVars.tahomaPlainFont10);
        chartPanel.getChart().getCategoryPlot().getRangeAxis().setTickLabelFont(MyDirectGraphVars.tahomaPlainFont10);
        chartPanel.getChart().getCategoryPlot().getRangeAxis().setLabelFont(MyDirectGraphVars.tahomaPlainFont10);

        CategoryAxis domainAxis = chartPanel.getChart().getCategoryPlot().getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);

        BarRenderer barRenderer = (BarRenderer) chartPanel.getChart().getCategoryPlot().getRenderer();
        barRenderer.setSeriesPaint(0, new Color(0, 0, 0, 0.25f));//Color.LIGHT_GRAY);//Color.decode("#2084FE"));
        barRenderer.setShadowPaint(Color.WHITE);
        barRenderer.setBaseFillPaint(Color.decode("#07CF61"));
        barRenderer.setBarPainter(new StandardBarPainter());
        barRenderer.setBaseLegendTextFont(MyDirectGraphVars.tahomaPlainFont11);

        JComboBox timeConvertMenu = new JComboBox();
        timeConvertMenu.addItem("SECOND");
        timeConvertMenu.addItem("MINUTE");
        timeConvertMenu.addItem("HOUR");
        timeConvertMenu.setSelectedIndex(selectedMenu);
        timeConvertMenu.setFont(MySequentialGraphVars.tahomaPlainFont12);
        timeConvertMenu.setFocusable(false);
        timeConvertMenu.setBackground(Color.WHITE);
        timeConvertMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        if (timeConvertMenu.getSelectedIndex() == 0) {
                            toTime = 0;
                            selectedMenu = 0;
                            decorate();
                        } else if (timeConvertMenu.getSelectedIndex() == 1) {
                            selectedMenu = 1;
                            toTime = 60;
                            decorate();
                        } else if (timeConvertMenu.getSelectedIndex() == 2) {
                            toTime = 3600;
                            selectedMenu = 2;
                            decorate();
                        }
                    }
                }).start();
            }
        });
        JPanel timeConvertMenuPanel = new JPanel();
        timeConvertMenuPanel.setBackground(Color.WHITE);
        timeConvertMenuPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 3));
        timeConvertMenuPanel.add(timeConvertMenu);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout(3, 3));
        topPanel.setBackground(Color.WHITE);
        topPanel.add(timeConvertMenuPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
        add(chartPanel, BorderLayout.CENTER);

        pb.updateValue(100, 100);
        pb.dispose();
    }

    public void enlarge() {
            try {
                this.decorate();

                JFrame f = new JFrame(" REACH TIME DISTRIBUTION");
                f.setBackground(Color.WHITE);
                f.setPreferredSize(new Dimension(550, 450));
                f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                f.setLayout(new BorderLayout(3, 3));
                f.getContentPane().add(this, BorderLayout.CENTER);
                f.pack();
                f.setVisible(true);
            } catch (Exception ex) {
            }

    }

    private Set<MyNode> nodes = null;

    private JFreeChart setValueChart() {
        int nodeCount = 0;
        float totalValue = 0;

        TreeMap<Long, Integer> valueMap = new TreeMap<>();
        if (MySequentialGraphVars.getSequentialGraphViewer().singleNode != null) {
            if (MySequentialGraphVars.getSequentialGraphViewer().predecessorsOnly) {
                this.nodes = MySequentialGraphVars.getSequentialGraphViewer().singleNodePredecessors;
            } else if (MySequentialGraphVars.getSequentialGraphViewer().successorsOnly) {
                this.nodes = MySequentialGraphVars.getSequentialGraphViewer().singleNodeSuccessors;
            } else if (MySequentialGraphVars.getSequentialGraphViewer().neighborsOnly) {
                this.nodes = MySequentialGraphVars.getSequentialGraphViewer().singleNodeSuccessors;
                this.nodes.addAll(MySequentialGraphVars.getSequentialGraphViewer().singleNodePredecessors);
            }
        } else if (MySequentialGraphVars.getSequentialGraphViewer().multiNodes != null && MySequentialGraphVars.getSequentialGraphViewer().multiNodes.size() > 0) {
            if (MySequentialGraphVars.getSequentialGraphViewer().predecessorsOnly) {
                this.nodes = MySequentialGraphVars.getSequentialGraphViewer().multiNodePredecessors;
            } else if (MySequentialGraphVars.getSequentialGraphViewer().successorsOnly) {
                this.nodes = MySequentialGraphVars.getSequentialGraphViewer().multiNodeSuccessors;
            } else if (MySequentialGraphVars.getSequentialGraphViewer().neighborsOnly) {
                this.nodes = MySequentialGraphVars.getSequentialGraphViewer().multiNodePredecessors;
                this.nodes.addAll(MySequentialGraphVars.getSequentialGraphViewer().multiNodeSuccessors);
            } else if (MySequentialGraphVars.getSequentialGraphViewer().sharedNeighborsOnly) {
                this.nodes = MySequentialGraphVars.getSequentialGraphViewer().sharedSuccessors;
                this.nodes.addAll(MySequentialGraphVars.getSequentialGraphViewer().sharedPredecessors);
            } else if (MySequentialGraphVars.getSequentialGraphViewer().sharedPredecessorsOly) {
                this.nodes = MySequentialGraphVars.getSequentialGraphViewer().sharedPredecessors;
            } else if (MySequentialGraphVars.getSequentialGraphViewer().sharedSuccessorsOnly) {
                this.nodes = MySequentialGraphVars.getSequentialGraphViewer().sharedSuccessors;
            }
        } else {
            this.nodes = new HashSet<>(MySequentialGraphVars.g.getVertices());
        }

        for (MyNode n : this.nodes) {
            if (n.getCurrentValue() == 0) continue;
            for (Map<Long, Integer> reachTimeMap : n.reachTimeMapByObjectID.values()) {
                for (long reachTime : reachTimeMap.keySet()) {
                    if (reachTime == 0) continue;
                    long value = (this.toTime > 0 ? (long) (reachTime/this.toTime) : reachTime);
                    totalValue += value;
                    nodeCount++;

                    if (value > this.maxValue) {
                        this.maxValue = value;
                    }

                    if (value > 0 && value < this.minValue) {
                        this.minValue = value;
                    }

                    if (valueMap.containsKey(value)) {
                        valueMap.put(value, valueMap.get(value) + 1);
                    } else {
                        valueMap.put(value, 1);
                    }
                }
            }
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Long nodeValue : valueMap.keySet()) {
            dataset.addValue(valueMap.get(nodeValue), "", nodeValue);
        }

        this.avgValue = totalValue/nodeCount;
        this.stdValue = getNodeValueStandardDeviation(valueMap);

        String plotTitle = "";
        String xaxis = "REACH TIME";
        String yaxis = "";
        PlotOrientation orientation = PlotOrientation.VERTICAL;
        boolean show = false;
        boolean toolTips = true;
        boolean urls = false;
        return ChartFactory.createBarChart(plotTitle, xaxis, yaxis, dataset, orientation, show, toolTips, urls);
    }

    public float getNodeValueStandardDeviation(TreeMap<Long, Integer> valueMap) {
        float sum = 0.00f;
        for (long n : valueMap.keySet()) {
            sum += n;
        }
        double mean = sum / valueMap.size();
        sum = 0f;
        for (long n : valueMap.keySet()) {
            sum += Math.pow(n - mean, 2);
        }
        return (sum == 0.00f ? 0.00f : (float) Math.sqrt(sum / valueMap.size()));
    }

    @Override public void actionPerformed(ActionEvent e) {

    }
}
