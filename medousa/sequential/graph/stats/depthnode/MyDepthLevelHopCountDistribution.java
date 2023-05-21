package medousa.sequential.graph.stats.depthnode;

import medousa.MyProgressBar;
import medousa.sequential.utils.MyMathUtil;
import medousa.sequential.utils.MySequentialGraphSysUtil;
import medousa.sequential.utils.MySequentialGraphVars;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.TreeMap;

public class MyDepthLevelHopCountDistribution
extends JPanel {

    public static int instances = 0;
    public MyDepthLevelHopCountDistribution() {
        this.decorate();
    }

    public void decorate() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                setLayout(new BorderLayout(3,3));
                setBackground(Color.WHITE);

                ChartPanel chartPanel = new ChartPanel(setChart());
                chartPanel.getChart().getCategoryPlot().setRangeGridlinePaint(Color.DARK_GRAY);
                chartPanel.getChart().getCategoryPlot().setDomainGridlinePaint(Color.DARK_GRAY);
                chartPanel.getChart().getCategoryPlot().setBackgroundPaint(Color.WHITE);
                chartPanel.getChart().getCategoryPlot().getDomainAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
                chartPanel.getChart().getCategoryPlot().getDomainAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
                chartPanel.getChart().getCategoryPlot().getRangeAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
                chartPanel.getChart().getCategoryPlot().getRangeAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
                chartPanel.getChart().getCategoryPlot().getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());

                CategoryAxis domainAxis = chartPanel.getChart().getCategoryPlot().getDomainAxis();
                domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);

                BarRenderer barRenderer = (BarRenderer)chartPanel.getChart().getCategoryPlot().getRenderer();
                barRenderer.setSeriesPaint(0, new Color(0, 0, 0f, 0.23f));
                barRenderer.setShadowPaint(Color.WHITE);
                barRenderer.setBaseFillPaint(Color.decode("#07CF61"));
                barRenderer.setBarPainter(new StandardBarPainter());
                barRenderer.setBaseLegendTextFont(MySequentialGraphVars.tahomaPlainFont11);

                JLabel titleLabel = new JLabel(" DEPTHNODE H.");
                titleLabel.setFont(MySequentialGraphVars.tahomaBoldFont12);
                titleLabel.setBackground(Color.WHITE);
                titleLabel.setForeground(Color.DARK_GRAY);

                JPanel titlePanel = new JPanel();
                titlePanel.setBackground(Color.WHITE);
                titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
                titlePanel.add(titleLabel);

                JButton enlargeBtn = new JButton("+");
                enlargeBtn.setBackground(Color.WHITE);
                enlargeBtn.setFont(MySequentialGraphVars.tahomaPlainFont11);
                enlargeBtn.setFocusable(false);
                enlargeBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        new Thread(new Runnable() {
                            @Override public void run() {
                                enlarge();
                            }
                        }).start();
                    }
                });

                JPanel enlargePanel = new JPanel();
                enlargePanel.setBackground(Color.WHITE);
                enlargePanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3,3));
                enlargePanel.add(enlargeBtn);

                JPanel topPanel = new JPanel();
                topPanel.setBackground(Color.WHITE);
                topPanel.setLayout(new BorderLayout(3,3));
                topPanel.add(titlePanel, BorderLayout.WEST);
                topPanel.add(enlargePanel, BorderLayout.CENTER);

                setBackground(Color.WHITE);
                add(chartPanel, BorderLayout.CENTER);
                add(topPanel, BorderLayout.NORTH);
                revalidate();
                repaint();
            }
        });
    }

    public void enlarge() {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            JFrame f = new JFrame(" DEPTH NODE HOP DISTRIBUTION");
            f.setLayout(new BorderLayout(3, 3));
            f.getContentPane().add(new MyDepthLevelHopCountDistribution(), BorderLayout.CENTER);
            f.setPreferredSize(new Dimension(400, 300));
            f.pack();
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
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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

    private JFreeChart setChart() {
        TreeMap<Integer, Integer> nodeHopCountByNodeMap = new TreeMap<>();
        if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeNameSet != null) {
            for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                for (int itemsetIdx = 0; itemsetIdx < MySequentialGraphVars.seqs[s].length; itemsetIdx++) {
                    String nn = MySequentialGraphVars.seqs[s][itemsetIdx].split(":")[0];
                    if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeNameSet.contains(nn)) {
                        int hopLength = (MySequentialGraphVars.seqs[s].length - (itemsetIdx + 1));
                        if (nodeHopCountByNodeMap.containsKey(hopLength)) {
                            nodeHopCountByNodeMap.put(hopLength, nodeHopCountByNodeMap.get(hopLength) + 1);
                        } else {
                            nodeHopCountByNodeMap.put(hopLength, 1);
                        }
                    }
                }
            }
        }

        int totalLengths = 0;
        int totalCount = 0;
        for (Integer hopLength : nodeHopCountByNodeMap.keySet()) {
            totalLengths += nodeHopCountByNodeMap.get(hopLength)*hopLength;
            totalCount += nodeHopCountByNodeMap.get(hopLength);
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Integer hopCount : nodeHopCountByNodeMap.keySet()) {
            dataset.addValue(nodeHopCountByNodeMap.get(hopCount), "HOP", hopCount);
        }

        String plotTitle = "";
        String xaxis = " HOP LENGTH[AVG.: " + MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat((double)totalLengths/totalCount)) + "]";
        String yaxis = "";
        PlotOrientation orientation = PlotOrientation.VERTICAL;
        boolean show = false;
        boolean toolTips = true;
        boolean urls = false;
        return ChartFactory.createBarChart(plotTitle, xaxis, yaxis, dataset, orientation, show, toolTips, urls);
    }
}
