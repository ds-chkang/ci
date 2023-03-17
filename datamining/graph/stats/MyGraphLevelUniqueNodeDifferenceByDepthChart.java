package datamining.graph.stats;

import datamining.main.MyProgressBar;
import datamining.graph.MyNode;
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

public class MyGraphLevelUniqueNodeDifferenceByDepthChart
extends JPanel {

    public static int instances = 0;

    class MyBarRenderer extends BarRenderer {
        public MyBarRenderer() {
            super();
        }
        public Paint getItemPaint(int x_row, int x_col) {
            return Color.RED;
        }
    }

    public MyGraphLevelUniqueNodeDifferenceByDepthChart() {
        this.decorate();
    }

    public void decorate() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                try {
                    removeAll();
                    setLayout(new BorderLayout(5,5));
                    setBackground(Color.WHITE);

                    Collection<MyNode> nodes = MyVars.g.getVertices();
                    Map<Integer, Integer> predecessorsByDepth = new HashMap<>();
                    Map<Integer, Integer> successorsByDepth = new HashMap<>();

                    for (MyNode n : nodes) {
                        for (int i = 1; i <= MyVars.mxDepth; i++) {
                            if (n.getNodeDepthInfo(i) != null) {
                                if (predecessorsByDepth.containsKey(i)) {
                                    predecessorsByDepth.put(i, n.getNodeDepthInfo(i).getPredecessorCount() + predecessorsByDepth.get(i));
                                    successorsByDepth.put(i, n.getNodeDepthInfo(i).getSuccessorCount() + successorsByDepth.get(i));
                                } else {
                                    predecessorsByDepth.put(i, n.getNodeDepthInfo(i).getPredecessorCount());
                                    successorsByDepth.put(i, n.getNodeDepthInfo(i).getSuccessorCount());
                                }
                            }
                        }
                    }

                    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
                    if (MyVars.currentGraphDepth == 0) {
                        for (int depth = 1; depth <= MyVars.mxDepth; depth++) {
                            if (predecessorsByDepth.get(depth) != null) {
                                dataset.addValue(successorsByDepth.get(depth) - predecessorsByDepth.get(depth), "UNIQ. NODE DIFF.[SUCC.-PRED.]", String.valueOf(depth));
                            } else {
                                dataset.addValue(0, "UNIQ. NODE DIFF.[SUCC.-PRED.]", String.valueOf(depth));
                            }
                        }
                    } else {
                        for (int depth = 1; depth <= MyVars.mxDepth; depth++) {
                            if (depth == MyVars.currentGraphDepth) {
                                dataset.addValue(
                                        successorsByDepth.get(MyVars.currentGraphDepth) - predecessorsByDepth.get(MyVars.currentGraphDepth),
                                        "UNIQ. NODE DIFF.[SUCC.-PRED.]",
                                        String.valueOf(MyVars.currentGraphDepth));
                            } else {
                                dataset.addValue(0, "UNIQ. NODE DIFF.[SUCC.-PRED.]", String.valueOf(depth));
                            }
                        }
                    }

                    MyBarRenderer renderer = new MyBarRenderer();
                    CategoryAxis xAxis = new CategoryAxis("DEPTH");
                    xAxis.setLabelFont(new Font("Arial", Font.PLAIN, 0));

                    ValueAxis yAxis = new NumberAxis();
                    yAxis.setLabelFont(new Font("Arial", Font.PLAIN, 0));

                    CategoryPlot plot = new CategoryPlot(dataset, xAxis, yAxis, renderer);
                    plot.setBackgroundPaint(Color.WHITE);
                    plot.setRangeGridlinePaint(Color.DARK_GRAY);
                    plot.getRangeAxis().setMinorTickCount(1);

                    yAxis.setTickLabelFont(MyVars.tahomaPlainFont10);
                    xAxis.setTickLabelFont(MyVars.tahomaPlainFont10);
                    plot.setOrientation(PlotOrientation.VERTICAL);

                    renderer.setBarPainter(new StandardBarPainter());
                    renderer.setShadowPaint(new Color(0, 0, 0, 0.0f));
                    renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());

                    JFreeChart chart = new JFreeChart(plot);
                    chart.setTitle("");
                    chart.getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
                    chart.getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
                    chart.getTitle().setFont(MyVars.tahomaBoldFont12);
                    chart.setBackgroundPaint(Color.WHITE);

                    JLabel titleLabel = new JLabel("  UNIQUE NODE DIFF.");
                    titleLabel.setBackground(Color.WHITE);
                    titleLabel.setFont(MyVars.tahomaBoldFont11);
                    titleLabel.setForeground(Color.DARK_GRAY);
                    titleLabel.setHorizontalAlignment(JLabel.LEFT);

                    JButton enlargeBtn = new JButton("+");
                    enlargeBtn.setFocusable(false);
                    enlargeBtn.setFont(MyVars.tahomaPlainFont10);
                    enlargeBtn.setBackground(Color.WHITE);
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
                    enlargePanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
                    enlargePanel.add(enlargeBtn);

                    JPanel topPanel = new JPanel();
                    topPanel.setLayout(new GridLayout(1,2));
                    topPanel.setBackground(Color.WHITE);
                    topPanel.add(titleLabel);
                    topPanel.add(enlargePanel);

                    BarRenderer r = (BarRenderer)chart.getCategoryPlot().getRenderer();
                    r.setSeriesPaint(0, Color.PINK);
                    r.setSeriesPaint(1, Color.LIGHT_GRAY);//Color.decode("#07CF61"));
                    r.setSeriesPaint(2, Color.DARK_GRAY);
                    r.setBaseLegendTextFont(MyVars.tahomaPlainFont11);
                    add(topPanel, BorderLayout.NORTH);
                    add(new ChartPanel(chart), BorderLayout.CENTER);
                    revalidate();
                    repaint();
                } catch (Exception ex) {ex.printStackTrace();}
            }
        });
    }

    public void enlarge() {
        try {
            MyProgressBar pb = new MyProgressBar(false);
            JFrame frame = new JFrame("UNIQUE NODE DIFFERENCE BY DEPTH STATISTICS");
            frame.setLayout(new BorderLayout());
            frame.setBackground(Color.WHITE);
            frame.setPreferredSize(new Dimension(450, 350));
            pb.updateValue(10, 100);

            MyGraphLevelUniqueNodeDifferenceByDepthChart inOutDifferenceStatByDepthChart = new MyGraphLevelUniqueNodeDifferenceByDepthChart();
            frame.getContentPane().add(inOutDifferenceStatByDepthChart, BorderLayout.CENTER);
            pb.updateValue(60, 100);

            frame.pack();
            frame.setCursor(Cursor.HAND_CURSOR);
            pb.updateValue(80, 100);

            Border blackline = BorderFactory.createLineBorder(Color.black);
            TitledBorder titledBorder = BorderFactory.createTitledBorder(blackline, "");
            titledBorder.setTitleJustification(TitledBorder.LEFT);
            titledBorder.setTitleFont(MyVars.f_bold_12);
            titledBorder.setTitleColor(Color.DARK_GRAY);
            inOutDifferenceStatByDepthChart.setBorder(titledBorder);
            pb.updateValue(100, 100);
            pb.dispose();

            frame.setVisible(true);
        } catch (Exception ex) {ex.printStackTrace();}
    }
}
