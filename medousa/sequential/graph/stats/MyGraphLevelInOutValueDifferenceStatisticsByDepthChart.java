package medousa.sequential.graph.stats;

import medousa.MyProgressBar;
import medousa.sequential.graph.MyNode;
import medousa.sequential.utils.MySequentialGraphVars;
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
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class MyGraphLevelInOutValueDifferenceStatisticsByDepthChart
extends JPanel {

    public MyGraphLevelInOutValueDifferenceStatisticsByDepthChart() {
        SwingUtilities.invokeLater(
                new Runnable() {
                    @Override public void run() {
                        decorate();
                    }
                }
        );
    }

    private void decorate() {
        try {
            this.setLayout(new BorderLayout(2, 2));
            this.setBackground(Color.WHITE);

            Map<Integer, Long> inNodesByDepthMap = new HashMap<>();
            Map<Integer, Long> outNodesByDepthMap = new HashMap<>();

            Map<Integer, Long> inContributionByDepthMap = new HashMap<>();
            Map<Integer, Long> outContributionByDepthMap = new HashMap<>();

            Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
            for (int i=1; i <= MySequentialGraphVars.mxDepth; i++) {
                for (MyNode n : nodes) {
                    if (n.getNodeDepthInfoMap().containsKey(i)) {
                        if (inContributionByDepthMap.containsKey(i)) {
                            inContributionByDepthMap.put(i, inContributionByDepthMap.get(i) + n.getNodeDepthInfo(i).getInContribution());
                        } else {
                            inContributionByDepthMap.put(i, (long) n.getNodeDepthInfo(i).getInContribution());
                        }

                        if (outContributionByDepthMap.containsKey(i)) {
                            outContributionByDepthMap.put(i, outContributionByDepthMap.get(i) + n.getNodeDepthInfo(i).getOutContribution());
                        } else {
                            outContributionByDepthMap.put(i, (long) n.getNodeDepthInfo(i).getOutContribution());
                        }

                        if (inContributionByDepthMap.containsKey(i)) {
                            inNodesByDepthMap.put(i, (long) n.getNodeDepthInfo(i).getPredecessorCount() + n.getNodeDepthInfo(i).getPredecessorCount());
                        } else {
                            inNodesByDepthMap.put(i, (long) n.getNodeDepthInfo(i).getPredecessorCount());
                        }

                        if (outNodesByDepthMap.containsKey(i)) {
                            outNodesByDepthMap.put(i, (long) n.getNodeDepthInfo(i).getSuccessorCount() + n.getNodeDepthInfo(i).getSuccessorCount());
                        } else {
                            outNodesByDepthMap.put(i, (long) n.getNodeDepthInfo(i).getSuccessorCount());
                        }
                    }
                }
            }

            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

            // In & Out contribution difference.
            for (int depth = 1; depth <= MySequentialGraphVars.mxDepth; depth++) {
                if (!inContributionByDepthMap.containsKey(depth) && !outContributionByDepthMap.containsKey(depth)) {
                    dataset.addValue(0, "INOUT-CONTRIBUTION-DIFFERENCE", String.valueOf(depth));
                } else if (!inContributionByDepthMap.containsKey(depth) && outContributionByDepthMap.containsKey(depth)) {
                    dataset.addValue(outContributionByDepthMap.get(depth), "INOUT-CONTRIBUTION-DIFFERENCE", String.valueOf(depth));
                } else if (inContributionByDepthMap.containsKey(depth) && !outContributionByDepthMap.containsKey(depth)) {
                    dataset.addValue(inContributionByDepthMap.get(depth), "INOUT-CONTRIBUTION-DIFFERENCE", String.valueOf(depth));
                } else {
                    dataset.addValue(inContributionByDepthMap.get(depth) - outContributionByDepthMap.get(depth), "INOUT-CONTRIBUTION-DIFFERENCE", String.valueOf(depth));
                }
            }

            for (int depth = 1; depth <= MySequentialGraphVars.mxDepth; depth++) {
                if (!inNodesByDepthMap.containsKey(depth) && !outNodesByDepthMap.containsKey(depth)) {
                    dataset.addValue(0, "INOUT-NODE-DIFFERENCE", String.valueOf(depth));
                } else if (!inNodesByDepthMap.containsKey(depth) && outNodesByDepthMap.containsKey(depth)) {
                    dataset.addValue(inNodesByDepthMap.get(depth), "INOUT-NODE-DIFFERENCE", String.valueOf(depth));
                } else if (inNodesByDepthMap.containsKey(depth) && !outNodesByDepthMap.containsKey(depth)) {
                    dataset.addValue(inNodesByDepthMap.get(depth), "INOUT-NODE-DIFFERENCE", String.valueOf(depth));
                } else {
                    dataset.addValue(inNodesByDepthMap.get(depth) - outNodesByDepthMap.get(depth), "INOUT-NODE-DIFFERENCE", String.valueOf(depth));
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

            yAxis.setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
            xAxis.setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
            plot.setOrientation(PlotOrientation.VERTICAL);

            ((BarRenderer) plot.getRenderer()).setBarPainter(new StandardBarPainter());
            ((BarRenderer) plot.getRenderer()).setShadowPaint(new Color(0, 0, 0, 0.0f));
            ((BarRenderer) plot.getRenderer()).setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());

            JFreeChart chart = new JFreeChart(plot);
            chart.setTitle("");
            chart.getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
            chart.getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
            chart.getTitle().setFont(MySequentialGraphVars.tahomaBoldFont12);
            chart.setBackgroundPaint(Color.WHITE);

            JLabel titleLabel = new JLabel("  INOUT-VALUE DIFFERENCES");
            titleLabel.setBackground(Color.WHITE);
            titleLabel.setFont(MySequentialGraphVars.tahomaBoldFont12);
            titleLabel.setForeground(Color.DARK_GRAY);
            titleLabel.setHorizontalAlignment(JLabel.LEFT);

            JButton enlargeBtn = new JButton("+");
            enlargeBtn.setFocusable(false);
            enlargeBtn.setFont(MySequentialGraphVars.tahomaPlainFont12);
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
            topPanel.setLayout(new GridLayout(1, 2));
            topPanel.setBackground(Color.WHITE);
            topPanel.add(titleLabel);
            topPanel.add(titleLabel);
            topPanel.add(enlargePanel);

            BarRenderer r = (BarRenderer)chart.getCategoryPlot().getRenderer();
            r.setSeriesPaint(0, Color.BLUE);
            r.setSeriesPaint(1, Color.RED);
            r.setBaseLegendTextFont(MySequentialGraphVars.tahomaPlainFont11);

            this.add(topPanel, BorderLayout.NORTH);
            this.add(new ChartPanel(chart), BorderLayout.CENTER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void enlarge() {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            JFrame f = new JFrame("INOUT-VALUE DIFFERENCE BY DEPTH STATISTICS");
            f.setLayout(new BorderLayout());
            f.setBackground(Color.WHITE);
            f.setPreferredSize(new Dimension(760, 450));
            f.getContentPane().add(new MyGraphLevelInOutValueDifferenceStatisticsByDepthChart(), BorderLayout.CENTER);
            f.pack();
            f.setAlwaysOnTop(true);
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.setCursor(Cursor.HAND_CURSOR);
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
