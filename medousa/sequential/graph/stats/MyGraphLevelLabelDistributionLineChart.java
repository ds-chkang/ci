package medousa.sequential.graph.stats;

import medousa.message.MyMessageUtil;
import medousa.sequential.graph.MyEdge;
import medousa.sequential.graph.MyNode;
import medousa.MyProgressBar;
import medousa.sequential.utils.MySequentialGraphSysUtil;
import medousa.sequential.utils.MySequentialGraphVars;
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
    private ArrayList<Color> colors;
    private Random rand = new Random();

    public MyGraphLevelLabelDistributionLineChart() {
        this.decorate();
    }

    public void decorate() {
        final MyGraphLevelLabelDistributionLineChart graphLevelLabelDistributionLineChart = this;
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                try {
                    removeAll();
                    setLayout(new BorderLayout(3, 3));
                    setBackground(Color.WHITE);

                    JLabel titleLabel = new JLabel(" L. V.");
                    titleLabel.setToolTipText("NODE & EDGE LABEL VALUE DISTRIBUTION");
                    titleLabel.setFont(MySequentialGraphVars.tahomaBoldFont11);
                    titleLabel.setBackground(Color.WHITE);
                    titleLabel.setForeground(Color.DARK_GRAY);

                    JButton enlargeBtn = new JButton("+");
                    enlargeBtn.setFont(MySequentialGraphVars.tahomaPlainFont11);
                    enlargeBtn.setFocusable(false);
                    enlargeBtn.setBackground(Color.WHITE);
                    enlargeBtn.addActionListener(new ActionListener() {@Override
                    public void actionPerformed(ActionEvent e) {
                        new Thread(new Runnable() {@Override
                        public void run() {
                            enlarge();
                        }}).start();}});

                    labelValueMenu = new JComboBox();
                    labelValueMenu.setBackground(Color.WHITE);
                    labelValueMenu.setFont(MySequentialGraphVars.tahomaPlainFont11);
                    labelValueMenu.setFocusable(false);
                    labelValueMenu.addItem("");
                    if (MySequentialGraphVars.userDefinedNodeLabelSet.size() > 0) labelValueMenu.addItem("NODE");
                    if (MySequentialGraphVars.userDefinedEdgeLabelSet.size() > 0) labelValueMenu.addItem("EDGE");
                    labelValueMenu.addActionListener(graphLevelLabelDistributionLineChart);

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

                    add(topPanel, BorderLayout.NORTH);
                    revalidate();
                    repaint();
                } catch (Exception ex) {}
            }
        });

    }

    public void enlarge() {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            JFrame f = new JFrame(" NODE & EDGE LABEL VALUE DISTRIBUTION");
            f.setLayout(new BorderLayout());
            f.setBackground(Color.WHITE);
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.setPreferredSize(new Dimension(400, 450));
            f.getContentPane().add(new MyGraphLevelLabelDistributionLineChart(), BorderLayout.CENTER);
            f.pack();
            f.setCursor(Cursor.HAND_CURSOR);
            pb.updateValue(100, 100);
            pb.dispose();
            f.setVisible(true);
        } catch (Exception ex) {
            pb.updateValue(100, 100);
            pb.dispose();
        }
    }

    @Override public void actionPerformed(ActionEvent e) {
        final MyGraphLevelLabelDistributionLineChart graphLevelLabelDistributionLineChart = this;
        new Thread(new Runnable() {
            @Override public void run() {
                if (e.getSource() == labelValueMenu) {
                    if (labelValueMenu.getSelectedIndex() == 0) {
                        decorate();
                        return;
                    }
                    LinkedHashMap<String, Long> labelValueMap = new LinkedHashMap<>();
                    if (labelValueMenu.getSelectedItem().toString().contains("NODE")) {
                        if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeLabelSelecter.getSelectedIndex() < 2) {
                            MyMessageUtil.showInfoMsg("Select a node label.");
                            //labelValueMenu.removeActionListener(graphLevelLabelDistributionLineChart);
                            labelValueMenu.setSelectedIndex(0);
                            //labelValueMenu.addActionListener(graphLevelLabelDistributionLineChart);
                            return;
                        }
                        MyProgressBar pb = new MyProgressBar(false);
                        int pbCnt = 0;
                        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
                        colors = new ArrayList<>();
                        for (MyNode n : nodes) {
                            pb.updateValue(++pbCnt, nodes.size());
                            if (n.getCurrentValue() == 0) continue;

                            final float hue = rand.nextFloat();
                            final float saturation = 0.9f;
                            final float luminance = 1.0f;
                            Color randomColor = Color.getHSBColor(hue, saturation, luminance);
                            colors.add(randomColor);

                            if (n.nodeLabelMap.containsKey(MySequentialGraphVars.getSequentialGraphViewer().vc.nodeLabelSelecter.getSelectedItem().toString())) {
                                if (labelValueMap.containsKey(n.nodeLabelMap.get(MySequentialGraphVars.getSequentialGraphViewer().vc.nodeLabelSelecter.getSelectedItem().toString()))) {
                                    labelValueMap.put(n.nodeLabelMap.get(MySequentialGraphVars.getSequentialGraphViewer().vc.nodeLabelSelecter.getSelectedItem().toString()), labelValueMap.get(n.nodeLabelMap.get(MySequentialGraphVars.getSequentialGraphViewer().vc.nodeLabelSelecter.getSelectedItem().toString())) + 1);
                                } else {
                                    labelValueMap.put(n.nodeLabelMap.get(MySequentialGraphVars.getSequentialGraphViewer().vc.nodeLabelSelecter.getSelectedItem().toString()), 1L);
                                }
                            }
                        }
                        labelValueMap = MySequentialGraphSysUtil.sortMapByLongValue(labelValueMap);
                        setLabelDistribution(labelValueMap, labelValueMenu.getSelectedIndex());
                        pb.updateValue(100, 100);
                        pb.dispose();
                    } else if (labelValueMenu.getSelectedItem().toString().contains("EDGE")) {
                        if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeLabelSelecter.getSelectedIndex() == 0) {
                            MyMessageUtil.showInfoMsg("Select an edge label, first");
                            //labelValueMenu.removeActionListener(graphLevelLabelDistributionLineChart);
                            labelValueMenu.setSelectedIndex(0);
                           //labelValueMenu.addActionListener(graphLevelLabelDistributionLineChart);
                            return;
                        }
                        MyProgressBar pb = new MyProgressBar(false);
                        int pbCnt = 0;
                        Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
                        colors = new ArrayList<>();
                        for (MyEdge e : edges) {
                            pb.updateValue(++pbCnt, edges.size());
                            if (e.getCurrentValue() == 0) continue;

                            final float hue = rand.nextFloat();
                            final float saturation = 0.9f;
                            final float luminance = 1.0f;
                            Color randomColor = Color.getHSBColor(hue, saturation, luminance);
                            colors.add(randomColor);

                            if (e.edgeLabelMap.containsKey(MySequentialGraphVars.getSequentialGraphViewer().vc.edgeLabelSelecter.getSelectedItem().toString())) {
                                if (labelValueMap.containsKey(e.edgeLabelMap.get(MySequentialGraphVars.getSequentialGraphViewer().vc.edgeLabelSelecter.getSelectedItem().toString()))) {
                                    labelValueMap.put(e.edgeLabelMap.get(MySequentialGraphVars.getSequentialGraphViewer().vc.edgeLabelSelecter.getSelectedItem().toString()), labelValueMap.get(e.edgeLabelMap.get(MySequentialGraphVars.getSequentialGraphViewer().vc.edgeLabelSelecter.getSelectedItem().toString())) + 1);
                                } else {
                                    labelValueMap.put(e.edgeLabelMap.get(MySequentialGraphVars.getSequentialGraphViewer().vc.edgeLabelSelecter.getSelectedItem().toString()), 1L);
                                }
                            }
                        }
                        labelValueMap = MySequentialGraphSysUtil.sortMapByLongValue(labelValueMap);
                        setLabelDistribution(labelValueMap, labelValueMenu.getSelectedIndex());
                        pb.updateValue(100, 100);
                        pb.dispose();
                    } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeLabelSelecter.getSelectedIndex() == 0) {
                        MyMessageUtil.showInfoMsg("Select an node or edge label, first.");
                    }
                }
            }
        }).start();}

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
        chart.getCategoryPlot().getDomainAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
        chart.getCategoryPlot().getRangeAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
        BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();
        renderer.setBarPainter(new StandardBarPainter());
        // Set the colors for each data item in the series
        for (int i = 0; i <= dataset.getColumnCount(); i++) {
            renderer.setSeriesPaint(i, colors.get(i));
        }
        renderer.setMaximumBarWidth(0.09);
        renderer.setBaseLegendTextFont(MySequentialGraphVars.tahomaPlainFont11);
        // Create a ChartPanel and display it
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(350, 367));

        JLabel titleLabel = new JLabel(" L. V.");
        titleLabel.setToolTipText("NODE & EDGE LABEL VALUE DISTRIBUTION");
        titleLabel.setFont(MySequentialGraphVars.tahomaBoldFont12);
        titleLabel.setBackground(Color.WHITE);
        titleLabel.setForeground(Color.DARK_GRAY);

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
        titlePanel.add(titleLabel);

        JButton enlargeBtn = new JButton("+");
        enlargeBtn.setFont(MySequentialGraphVars.tahomaPlainFont12);
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

        labelValueMenu.removeActionListener(this);
        labelValueMenu.removeAllItems();
        labelValueMenu.setBackground(Color.WHITE);
        labelValueMenu.setFont(MySequentialGraphVars.tahomaPlainFont11);
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
