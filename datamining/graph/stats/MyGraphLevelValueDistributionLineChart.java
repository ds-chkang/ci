package datamining.graph.stats;

import datamining.graph.MyEdge;
import datamining.graph.MyNode;
import datamining.utils.message.MyMessageUtil;
import datamining.utils.system.MyVars;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
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
import java.util.TreeMap;

public class MyGraphLevelValueDistributionLineChart
extends JPanel
implements ActionListener {

    public static int instances = 0;
    public JComboBox valueMenu;
    private XYSeries valueSeries;
    private int selectedChart;

    public MyGraphLevelValueDistributionLineChart() {
        this.decorate();
    }

    public void decorate() {
        final MyGraphLevelValueDistributionLineChart graphLevelValueDistributionLineChart = this;
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                try {
                    setChart();
                    repaint();
                } catch (Exception ex) {}
            }
        });
    }

    private TreeMap<Long, Long> createNodeValueMap() {
        TreeMap<Long, Long> valueMap = new TreeMap<>();
        Collection<MyNode> nodes = MyVars.g.getVertices();
        for (MyNode n : nodes) {
            long value = (long)n.getCurrentValue();
            if (value == 0) continue;
            if (valueMap.containsKey(value)) {
                valueMap.put(value, valueMap.get(value) + 1);
            } else {
                valueMap.put(value, 1L);
            }
        }
        return valueMap;
    }

    private TreeMap<Long, Long> createEdgeValueMap() {
        TreeMap<Long, Long> valueMap = new TreeMap<>();
        Collection<MyEdge> edges = MyVars.g.getEdges();
        for (MyEdge e : edges) {
            long value = (long)e.getCurrentValue();
            if (value == 0) continue;
            if (valueMap.containsKey(value)) {
                valueMap.put(value, valueMap.get(value) + 1);
            } else {
                valueMap.put(value, 1L);
            }
        }
        return valueMap;
    }

    private void setChart() {
        removeAll();
        setLayout(new BorderLayout(3, 3));
        setBackground(Color.WHITE);

        TreeMap<Long, Long> valueMap = (valueMenu == null || valueMenu.getSelectedIndex() == 0 ? createNodeValueMap() : createEdgeValueMap());
        XYSeriesCollection dataset = new XYSeriesCollection();
        valueSeries = new XYSeries("C. V.");
        for (Long value : valueMap.keySet()) {
            valueSeries.add(value, valueMap.get(value));
        }
        dataset.addSeries(valueSeries);

        JFreeChart chart = ChartFactory.createXYLineChart("", "C. V.", "", dataset);
        chart.getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
        chart.getXYPlot().setBackgroundPaint(Color.WHITE);
        chart.getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
        chart.getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);
        chart.getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
        chart.getXYPlot().getDomainAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
        chart.getXYPlot().getDomainAxis().setTickLabelFont(MyVars.tahomaPlainFont10);
        chart.getXYPlot().getRangeAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
        chart.getXYPlot().getRangeAxis().setTickLabelFont(MyVars.tahomaPlainFont10);
        chart.getXYPlot().getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        chart.setBackgroundPaint(Color.WHITE);

        XYPlot plot = (XYPlot) chart.getPlot();
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, Color.BLACK);
        renderer.setSeriesStroke(0, new BasicStroke(1.5f));
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
        renderer.setSeriesFillPaint(0, Color.WHITE);
        renderer.setUseFillPaint(true);
        renderer.setBaseLegendTextFont(MyVars.tahomaPlainFont10);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(350, 367));

        JLabel titleLabel = new JLabel(" C. V.");
        titleLabel.setToolTipText("CURRENT NODE & EDGE VALUE DISTRIBUTION");
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
            @Override public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        enlarge();
                    }
                }).start();
            }
        });

        valueMenu = new JComboBox();
        valueMenu.setFocusable(false);
        valueMenu.setFont(MyVars.tahomaPlainFont10);
        valueMenu.setBackground(Color.WHITE);
        valueMenu.addItem("NODE");
        valueMenu.addItem("EDGE");
        valueMenu.setSelectedIndex(selectedChart);
        valueMenu.addActionListener(this);

        JPanel enlargePanel = new JPanel();
        enlargePanel.setBackground(Color.WHITE);
        enlargePanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 3));
        enlargePanel.add(valueMenu);
        enlargePanel.add(enlargeBtn);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout(3, 3));
        topPanel.setBackground(Color.WHITE);
        topPanel.add(titlePanel, BorderLayout.WEST);
        topPanel.add(enlargePanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);
        add(chartPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    JFrame distFrame = new JFrame(" NODE & EDGE VALUE DISTRIBUTION");
    public void enlarge() {
        distFrame.setLayout(new BorderLayout(3,3));
        distFrame.getContentPane().add(new MyGraphLevelValueDistributionLineChart(), BorderLayout.CENTER);
        distFrame.setPreferredSize(new Dimension(450, 350));
        distFrame.pack();
        distFrame.setVisible(true);
        distFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == valueMenu) {
            if (valueMenu.getSelectedIndex() == 0) {
                selectedChart = 0;
                valueMenu.setSelectedIndex(0);
            } else if (valueMenu.getSelectedIndex() == 1) {
                if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() < 2) {;
                    MyMessageUtil.showInfoMsg("Select an edge value, first.");
                    selectedChart = 0;
                    valueMenu.setSelectedIndex(0);
                } else {
                    selectedChart = 1;
                    valueMenu.setSelectedIndex(1);
                }
            }
        }
    }
}
