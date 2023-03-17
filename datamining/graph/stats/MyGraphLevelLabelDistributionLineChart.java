package datamining.graph.stats;

import datamining.graph.MyEdge;
import datamining.graph.MyNode;
import datamining.main.MyProgressBar;
import datamining.utils.message.MyMessageUtil;
import datamining.utils.system.MySysUtil;
import datamining.utils.system.MyVars;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class MyGraphLevelLabelDistributionLineChart
extends JPanel
implements ActionListener {

    public static int instances = 0;
    private JComboBox labelValueMenu = new JComboBox();
    private java.util.ArrayList<Color> colors;
    private Random rand = new Random();

    public MyGraphLevelLabelDistributionLineChart() {
        this.decorate();
    }

    public void decorate() {
        final MyGraphLevelLabelDistributionLineChart graphLevelLabelValueDistributionLineChart = this;
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                try {
                    removeAll();
                    setLayout(new BorderLayout(3, 3));
                    setBackground(Color.WHITE);

                    JLabel titleLabel = new JLabel(" L. V.");
                    titleLabel.setToolTipText("NODE & EDGE LABEL VALUE DISTRIBUTION");
                    titleLabel.setFont(MyVars.tahomaBoldFont11);
                    titleLabel.setBackground(Color.WHITE);
                    titleLabel.setForeground(Color.DARK_GRAY);

                    JButton enlargeBtn = new JButton("+");
                    enlargeBtn.setFont(MyVars.tahomaPlainFont10);
                    enlargeBtn.setFocusable(false);
                    enlargeBtn.setBackground(Color.WHITE);
                    enlargeBtn.addActionListener(new ActionListener() {@Override
                    public void actionPerformed(ActionEvent e) {
                        new Thread(new Runnable() {@Override
                        public void run() {
                            enlarge();
                        }
                        }).start();
                    }
                    });

                    labelValueMenu.setBackground(Color.WHITE);
                    labelValueMenu.setFont(MyVars.tahomaPlainFont10);
                    labelValueMenu.setFocusable(false);
                    labelValueMenu.addItem("");
                    if (MyVars.nodeLabelSet.size() > 0) labelValueMenu.addItem("NODE");
                    if (MyVars.edgeLabelSet.size() > 0) labelValueMenu.addItem("EDGE");
                    labelValueMenu.addActionListener(graphLevelLabelValueDistributionLineChart);

                    JPanel titlePanel = new JPanel();
                    titlePanel.setBackground(Color.WHITE);
                    titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 6));
                    titlePanel.add(titleLabel);

                    JPanel buttonPanel = new JPanel();
                    buttonPanel.setBackground(Color.WHITE);
                    buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3,3));
                    buttonPanel.add(labelValueMenu);
                    buttonPanel.add(enlargeBtn);

                    JPanel topPanel = new JPanel();
                    topPanel.setLayout(new BorderLayout(3, 8));
                    topPanel.setBackground(Color.WHITE);
                    topPanel.add(titlePanel, BorderLayout.WEST);
                    topPanel.add(buttonPanel, BorderLayout.EAST);

                    JLabel msg = new JLabel("N/A");
                    msg.setFont(MyVars.tahomaPlainFont12);
                    msg.setBackground(Color.WHITE);
                    msg.setHorizontalAlignment(JLabel.CENTER);
                    add(topPanel, BorderLayout.NORTH);
                    add(msg, BorderLayout.CENTER);
                } catch (Exception ex) {}
            }
        });

    }

    public void enlarge() {
        MyGraphLevelLabelDistributionLineChart labelValueDistribution = new MyGraphLevelLabelDistributionLineChart();

        JFrame distFrame = new JFrame(" NODE & EDGE LABEL VALUE DISTRIBUTION");
        distFrame.setLayout(new BorderLayout(3,3));
        distFrame.getContentPane().add(labelValueDistribution, BorderLayout.CENTER);
        distFrame.setPreferredSize(new Dimension(450, 350));
        distFrame.pack();
        distFrame.setVisible(true);
    }

    @Override public void actionPerformed(ActionEvent e) {
        final MyGraphLevelLabelDistributionLineChart graphLevelLabelValueDistributionLineChart = this;
        new Thread(new Runnable() {
            @Override public void run() {
                if (e.getSource() == labelValueMenu) {
                    LinkedHashMap<String, Long> labelValueMap = new LinkedHashMap<>();
                    if (labelValueMenu.getSelectedItem().toString().contains("NODE")) {
                        if (MyVars.getViewer().vc.nodeLabelSelecter.getSelectedIndex() < 2) {
                            MyMessageUtil.showInfoMsg("Select a node label, first.");
                            labelValueMenu.removeActionListener(graphLevelLabelValueDistributionLineChart);
                            labelValueMenu.setSelectedIndex(0);
                            labelValueMenu.addActionListener(graphLevelLabelValueDistributionLineChart);
                            return;
                        }
                        MyProgressBar pb = new MyProgressBar(false);
                        int pbCnt = 0;
                        Collection<MyNode> nodes = MyVars.g.getVertices();
                        colors = new ArrayList<>();
                        for (MyNode n : nodes) {
                            pb.updateValue(++pbCnt, nodes.size());
                            if (n.getCurrentValue() == 0) continue;

                            final float hue = rand.nextFloat();
                            final float saturation = 0.9f;
                            final float luminance = 1.0f;
                            Color randomColor = Color.getHSBColor(hue, saturation, luminance);
                            colors.add(randomColor);

                            if (n.nodeLabelMap.containsKey(MyVars.getViewer().vc.nodeLabelSelecter.getSelectedItem().toString())) {
                                if (labelValueMap.containsKey(n.nodeLabelMap.get(MyVars.getViewer().vc.nodeLabelSelecter.getSelectedItem().toString()))) {
                                    labelValueMap.put(n.nodeLabelMap.get(MyVars.getViewer().vc.nodeLabelSelecter.getSelectedItem().toString()), labelValueMap.get(n.nodeLabelMap.get(MyVars.getViewer().vc.nodeLabelSelecter.getSelectedItem().toString())) + 1);
                                } else {
                                    labelValueMap.put(n.nodeLabelMap.get(MyVars.getViewer().vc.nodeLabelSelecter.getSelectedItem().toString()), 1L);
                                }
                            }
                        }
                        labelValueMap = MySysUtil.sortMapByLongValue(labelValueMap);
                        setLabelDistribution(labelValueMap, labelValueMenu.getSelectedIndex());
                        pb.updateValue(100, 100);
                        pb.dispose();
                    } else if (labelValueMenu.getSelectedItem().toString().contains("EDGE")) {
                        if (MyVars.getViewer().vc.edgeLabelSelecter.getSelectedIndex() == 0) {
                            MyMessageUtil.showInfoMsg("Select an edge label, first");
                            labelValueMenu.removeActionListener(graphLevelLabelValueDistributionLineChart);
                            labelValueMenu.setSelectedIndex(0);
                            labelValueMenu.addActionListener(graphLevelLabelValueDistributionLineChart);
                            return;
                        }
                        MyProgressBar pb = new MyProgressBar(false);
                        int pbCnt = 0;
                        Collection<MyEdge> edges = MyVars.g.getEdges();
                        colors = new ArrayList<>();
                        for (MyEdge e : edges) {
                            pb.updateValue(++pbCnt, edges.size());
                            if (e.getCurrentValue() == 0) continue;

                            final float hue = rand.nextFloat();
                            final float saturation = 0.9f;
                            final float luminance = 1.0f;
                            Color randomColor = Color.getHSBColor(hue, saturation, luminance);
                            colors.add(randomColor);

                            if (e.edgeLabelMap.containsKey(MyVars.getViewer().vc.edgeLabelSelecter.getSelectedItem().toString())) {
                                if (labelValueMap.containsKey(e.edgeLabelMap.get(MyVars.getViewer().vc.edgeLabelSelecter.getSelectedItem().toString()))) {
                                    labelValueMap.put(e.edgeLabelMap.get(MyVars.getViewer().vc.edgeLabelSelecter.getSelectedItem().toString()), labelValueMap.get(e.edgeLabelMap.get(MyVars.getViewer().vc.edgeLabelSelecter.getSelectedItem().toString())) + 1);
                                } else {
                                    labelValueMap.put(e.edgeLabelMap.get(MyVars.getViewer().vc.edgeLabelSelecter.getSelectedItem().toString()), 1L);
                                }
                            }
                        }
                        labelValueMap = MySysUtil.sortMapByLongValue(labelValueMap);
                        setLabelDistribution(labelValueMap, labelValueMenu.getSelectedIndex());
                        pb.updateValue(100, 100);
                        pb.dispose();
                    } else if (MyVars.getViewer().vc.nodeLabelSelecter.getSelectedIndex() == 0) {
                        MyMessageUtil.showInfoMsg("Select an node or edge label, first.");
                        return;
                    }
                }
            }
        }).start();
    }

    private void setLabelDistribution(Map<String, Long> labelValueMap, int selectedItemIdx) {
        removeAll();
        setLayout(new BorderLayout(3, 3));
        setBackground(Color.WHITE);

        CategoryDataset dataset = new DefaultCategoryDataset();
        for (String label : labelValueMap.keySet()) {
            ((DefaultCategoryDataset) dataset).addValue(labelValueMap.get(label), label, "");
        }

        // Create a bar chart with the dataset
        JFreeChart chart = ChartFactory.createBarChart(
                "",        // chart title
                "",           // x axis label
                "",           // y axis label
                dataset                   // data
        );
        chart.getCategoryPlot().setBackgroundPaint(Color.WHITE);
        chart.getCategoryPlot().setDomainGridlinePaint(Color.DARK_GRAY);
        chart.getCategoryPlot().setRangeGridlinePaint(Color.DARK_GRAY);
        chart.getCategoryPlot().getDomainAxis().setTickLabelFont(MyVars.tahomaPlainFont10);
        chart.getCategoryPlot().getRangeAxis().setTickLabelFont(MyVars.tahomaPlainFont10);
        BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();
        renderer.setBarPainter(new StandardBarPainter());
        // Set the colors for each data item in the series
        for (int i = 0; i <= dataset.getColumnCount(); i++) {
            renderer.setSeriesPaint(i, colors.get(i));
        }
        renderer.setMaximumBarWidth(0.09);
        renderer.setBaseLegendTextFont(MyVars.tahomaPlainFont10);
        // Create a ChartPanel and display it
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(350, 367));

        JLabel titleLabel = new JLabel(" L. V.");
        titleLabel.setToolTipText("NODE & EDGE LABEL VALUE DISTRIBUTION");
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
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        enlarge();
                    }
                }).start();
            }
        });

        labelValueMenu.removeActionListener(this);
        labelValueMenu.removeAllItems();
        labelValueMenu.setBackground(Color.WHITE);
        labelValueMenu.setFont(MyVars.tahomaPlainFont10);
        labelValueMenu.setFocusable(false);
        labelValueMenu.addItem("");
        labelValueMenu.addItem("NODE");
        labelValueMenu.addItem("EDGE");
        labelValueMenu.setSelectedIndex(selectedItemIdx);
        labelValueMenu.addActionListener(this);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 3));
        buttonPanel.add(labelValueMenu);
        buttonPanel.add(enlargeBtn);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout(3, 3));
        topPanel.setBackground(Color.WHITE);
        topPanel.add(titlePanel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);
        add(chartPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}
