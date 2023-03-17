package datamining.graph.stats;

import datamining.main.MyProgressBar;
import datamining.graph.MyNode;
import datamining.utils.MyMathUtil;
import datamining.utils.system.MyVars;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.HorizontalAlignment;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class MyInOutValueDifferenceStatByDepthChart
extends JPanel {

    private static int instances;

    public MyInOutValueDifferenceStatByDepthChart() {
        this.decorate();
    }

    private void decorate() {
        try {
            this.setLayout(new BorderLayout(5, 5));
            this.setBackground(Color.WHITE);

            Map<Integer, Set<MyNode>> inContributionByDepthNodeSetMap = new HashMap<>();
            Map<Integer, Set<MyNode>> outContributionByDepthNodeSetMap = new HashMap<>();

            Map<Integer, Long> inContributionByDepthMap = new HashMap<>();
            Map<Integer, Long> outContributionByDepthMap = new HashMap<>();

            Collection<MyNode> nodes = MyVars.g.getVertices();
            for (MyNode node : nodes) {
                for (int s = 0; s < MyVars.seqs.length; s++) {
                    for (int itemsetIdx = 1; itemsetIdx < MyVars.seqs[s].length; itemsetIdx++) {
                        String itemset = (MyVars.isTimeOn ? MyVars.seqs[s][itemsetIdx].split(":")[0] : MyVars.seqs[s][itemsetIdx]);
                        if (node.getName().equals(itemset)) {
                            if (itemsetIdx + 1 != MyVars.seqs[s].length) {
                                if (inContributionByDepthMap.containsKey(itemsetIdx)) {
                                    inContributionByDepthMap.put(itemsetIdx, inContributionByDepthMap.get(itemsetIdx) + 1);
                                    outContributionByDepthMap.put(itemsetIdx, outContributionByDepthMap.get(itemsetIdx) + 1);

                                    Set<MyNode> nodeSet = inContributionByDepthNodeSetMap.get(itemsetIdx);
                                    nodeSet.add(node);
                                    inContributionByDepthNodeSetMap.put(itemsetIdx, nodeSet);

                                    Set<MyNode> outNodeSet = outContributionByDepthNodeSetMap.get(itemsetIdx);
                                    outNodeSet.add(node);
                                    outContributionByDepthNodeSetMap.put(itemsetIdx, outNodeSet);
                                } else {
                                    inContributionByDepthMap.put(itemsetIdx, 1L);
                                    outContributionByDepthMap.put(itemsetIdx, 1L);

                                    Set<MyNode> inNodeSet = new HashSet<>();
                                    inNodeSet.add(node);
                                    inContributionByDepthNodeSetMap.put(itemsetIdx, inNodeSet);

                                    Set<MyNode> outNodeSet = new HashSet<>();
                                    outNodeSet.add(node);
                                    outContributionByDepthNodeSetMap.put(itemsetIdx, outNodeSet);
                                }
                            } else {
                                if (inContributionByDepthMap.containsKey(itemsetIdx)) {
                                    inContributionByDepthMap.put(itemsetIdx, inContributionByDepthMap.get(itemsetIdx) + 1);
                                    outContributionByDepthMap.put(itemsetIdx, outContributionByDepthMap.get(itemsetIdx) + 0L);

                                    Set<MyNode> inNodeSet = inContributionByDepthNodeSetMap.get(itemsetIdx);
                                    inNodeSet.add(node);
                                    inContributionByDepthNodeSetMap.put(itemsetIdx, inNodeSet);

                                    Set<MyNode> outNodeSet = outContributionByDepthNodeSetMap.get(itemsetIdx);
                                    outContributionByDepthNodeSetMap.put(itemsetIdx, outNodeSet);
                                } else {
                                    inContributionByDepthMap.put(itemsetIdx, 1L);
                                    outContributionByDepthMap.put(itemsetIdx, 0L);

                                    Set<MyNode> inNodeSet = new HashSet<>();
                                    inNodeSet.add(node);
                                    inContributionByDepthNodeSetMap.put(itemsetIdx, inNodeSet);

                                    Set<MyNode> outNodeSet = new HashSet<>();
                                    outContributionByDepthNodeSetMap.put(itemsetIdx, outNodeSet);
                                }
                            }
                        }
                    }
                }
            }

            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

            // In & Out contribution difference.
            for (int depth = 1; depth <= MyVars.mxDepth - 1; depth++) {
                if (inContributionByDepthMap.get(depth) != null) {
                    dataset.addValue(outContributionByDepthMap.get(depth) - inContributionByDepthMap.get(depth), "INOUT CONT. DIFF. ", String.valueOf(depth));
                }
            }

            for (int depth = 1; depth <= MyVars.mxDepth - 1; depth++) {
                if (inContributionByDepthNodeSetMap.get(depth) != null) {
                    dataset.addValue(outContributionByDepthNodeSetMap.get(depth).size() - inContributionByDepthNodeSetMap.get(depth).size(), "INOUT UNIQ. NODE DIFF. ", String.valueOf(depth));
                }
            }

            // In & Out average contribution.
            for (int depth = 1; depth <= MyVars.mxDepth - 1; depth++) {
                if (inContributionByDepthMap.get(depth) != null) {
                    dataset.addValue(Double.valueOf(MyMathUtil.twoDecimalFormat(((double) outContributionByDepthMap.get(depth) / outContributionByDepthNodeSetMap.get(depth).size())
                            - ((double) inContributionByDepthMap.get(depth) / inContributionByDepthNodeSetMap.get(depth).size()))), "INOUT AVG. CONT. DIFF.", String.valueOf(depth));
                }
            }

            BarRenderer render = new BarRenderer();
            CategoryAxis xAxis = new CategoryAxis("DEPTH");
            xAxis.setLabelFont(new Font("Arial", Font.PLAIN, 0));

            ValueAxis yAxis = new NumberAxis();
            yAxis.setLabelFont(new Font("Arial", Font.PLAIN, 0));

            CategoryPlot plot = new CategoryPlot(dataset, xAxis, yAxis, render);
            plot.setBackgroundPaint(Color.WHITE);
            plot.setRangeGridlinePaint(Color.DARK_GRAY);
            plot.getRangeAxis().setMinorTickCount(1);

            yAxis.setTickLabelFont(MyVars.tahomaPlainFont11);
            xAxis.setTickLabelFont(MyVars.tahomaPlainFont11);
            plot.setOrientation(PlotOrientation.VERTICAL);

            ((BarRenderer) plot.getRenderer()).setBarPainter(new StandardBarPainter());
            ((BarRenderer) plot.getRenderer()).setShadowPaint(new Color(0, 0, 0, 0.0f));
            ((BarRenderer) plot.getRenderer()).setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());

            JFreeChart chart = new JFreeChart(plot);
            chart.setTitle("");
            chart.getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
            chart.getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
            chart.getTitle().setFont(MyVars.tahomaBoldFont11);
            chart.setBackgroundPaint(Color.WHITE);

            JLabel titleLabel = new JLabel("  INOUT-VALUE DIFFERENCE");
            titleLabel.setBackground(Color.WHITE);
            titleLabel.setFont(MyVars.tahomaBoldFont12);
            titleLabel.setForeground(Color.DARK_GRAY);
            titleLabel.setHorizontalAlignment(JLabel.LEFT);

            JButton enlargeBtn = new JButton("ENLARGE");
            enlargeBtn.setFocusable(false);
            enlargeBtn.setFont(MyVars.tahomaPlainFont11);
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

            JPanel enlargePanel = new JPanel();
            enlargePanel.setBackground(Color.WHITE);
            enlargePanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
            enlargePanel.add(enlargeBtn);

            JPanel topPanel = new JPanel();
            topPanel.setLayout(new GridLayout(1, 2));
            topPanel.setBackground(Color.WHITE);
            topPanel.add(titleLabel);
            if (instances == 0) {
                topPanel.add(titleLabel);
                topPanel.add(enlargePanel);
            }

            BarRenderer r = (BarRenderer)chart.getCategoryPlot().getRenderer();
            r.setSeriesPaint(0, Color.BLUE);
            r.setSeriesPaint(1, Color.decode("#07CF61"));
            r.setSeriesPaint(2, Color.DARK_GRAY);
            r.setBaseLegendTextFont(MyVars.tahomaPlainFont11);

            this.add(topPanel, BorderLayout.NORTH);
            this.add(new ChartPanel(chart), BorderLayout.CENTER);
            instances++;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void enlarge() {
        try {
            MyProgressBar pb = new MyProgressBar(false);
            JFrame frame = new JFrame("INOUT-VALUE DIFFERENCE BY DEPTH STATISTICS");
            frame.setLayout(new BorderLayout());
            frame.setBackground(Color.WHITE);
            frame.setPreferredSize(new Dimension(760, 450));
            pb.updateValue(10, 100);

            MyInOutValueDifferenceStatByDepthChart inOutDifferenceStatByDepthChart = new MyInOutValueDifferenceStatByDepthChart();
            frame.getContentPane().add(inOutDifferenceStatByDepthChart, BorderLayout.CENTER);
            pb.updateValue(60, 100);

            frame.pack();
            frame.setCursor(Cursor.HAND_CURSOR);
            pb.updateValue(80, 100);

            Border blackline = BorderFactory.createLineBorder(Color.black);
            TitledBorder titledBorder = BorderFactory.createTitledBorder(blackline, "");
            titledBorder.setTitleJustification(TitledBorder.LEFT);
            titledBorder.setTitleFont(new Font("Arial", Font.PLAIN, 0));
            titledBorder.setTitleColor(Color.DARK_GRAY);
            inOutDifferenceStatByDepthChart.setBorder(titledBorder);
            pb.updateValue(100, 100);
            pb.dispose();

            frame.setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
