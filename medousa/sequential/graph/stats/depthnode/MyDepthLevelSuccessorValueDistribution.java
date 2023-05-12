package medousa.sequential.graph.stats.depthnode;

import medousa.sequential.graph.MyNode;
import medousa.MyProgressBar;
import medousa.sequential.utils.MyMathUtil;
import medousa.sequential.utils.MyDepthNodeUtil;
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
import java.util.Collection;
import java.util.TreeMap;

public class MyDepthLevelSuccessorValueDistribution
extends JPanel {

    public MyDepthLevelSuccessorValueDistribution() {
        this.decorate();
    }

    public void decorate() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                try {
                    removeAll();
                    setLayout(new BorderLayout(3, 3));
                    setBackground(Color.WHITE);
                    if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthNeighborNodeTypeSelector.getSelectedItem().toString().contains("S.") ||
                        MySequentialGraphVars.getSequentialGraphViewer().vc.depthNeighborNodeTypeSelector.getSelectedItem().toString().contains("BOTH")) {
                        int total = 0;
                        Collection<MyNode> nodes = MySequentialGraphVars.g.getDepthNodes();
                        for (MyNode n : nodes) {
                            if (n.getCurrentValue() > 0) {
                                if (n.getNodeDepthInfo(MySequentialGraphVars.currentGraphDepth).getSuccessorCount() > 0) {
                                    total = 1;
                                    break;
                                }
                            }
                        }
                        if (total == 0) {
                            JLabel titleLabel = new JLabel(" S. V.");
                            titleLabel.setFont(MySequentialGraphVars.tahomaBoldFont11);
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
                            msg.setFont(MySequentialGraphVars.tahomaPlainFont12);
                            msg.setBackground(Color.WHITE);
                            msg.setHorizontalAlignment(JLabel.CENTER);
                            add(topPanel, BorderLayout.NORTH);
                            add(msg, BorderLayout.CENTER);
                        } else {
                            ChartPanel chartPanel = new ChartPanel(setDepthChart());
                            chartPanel.getChart().getCategoryPlot().setRangeGridlinePaint(Color.DARK_GRAY);
                            chartPanel.getChart().getCategoryPlot().setDomainGridlinePaint(Color.DARK_GRAY);
                            chartPanel.getChart().getCategoryPlot().setBackgroundPaint(Color.WHITE);
                            chartPanel.getChart().getCategoryPlot().getDomainAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
                            chartPanel.getChart().getCategoryPlot().getDomainAxis().setLabelFont(MySequentialGraphVars.tahomaPlainFont11);
                            chartPanel.getChart().getCategoryPlot().getRangeAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
                            chartPanel.getChart().getCategoryPlot().getRangeAxis().setLabelFont(MySequentialGraphVars.tahomaPlainFont11);
                            chartPanel.getChart().getCategoryPlot().getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());

                            CategoryAxis domainAxis = chartPanel.getChart().getCategoryPlot().getDomainAxis();
                            domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);

                            BarRenderer barRenderer = (BarRenderer) chartPanel.getChart().getCategoryPlot().getRenderer();
                            barRenderer.setSeriesPaint(0, new Color(0, 0, 0f, 0.23f));
                            barRenderer.setShadowPaint(Color.WHITE);
                            barRenderer.setBaseFillPaint(Color.decode("#07CF61"));
                            barRenderer.setBarPainter(new StandardBarPainter());
                            barRenderer.setBaseLegendTextFont(MySequentialGraphVars.tahomaPlainFont11);

                            JLabel titleLabel = new JLabel(" S. V.");
                            titleLabel.setFont(MySequentialGraphVars.tahomaBoldFont12);
                            titleLabel.setBackground(Color.WHITE);
                            titleLabel.setForeground(Color.DARK_GRAY);

                            JButton enlargeBtn = new JButton("+");
                            enlargeBtn.setFont(MySequentialGraphVars.tahomaPlainFont11);
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

                            JPanel enlargePanel = new JPanel();
                            enlargePanel.setBackground(Color.WHITE);
                            enlargePanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 3));
                            enlargePanel.add(enlargeBtn);

                            JPanel topPanel = new JPanel();
                            topPanel.setLayout(new BorderLayout(3, 3));
                            topPanel.setBackground(Color.WHITE);
                            topPanel.add(titleLabel, BorderLayout.WEST);
                            topPanel.add(enlargePanel, BorderLayout.EAST);

                            add(chartPanel, BorderLayout.CENTER);
                            add(topPanel, BorderLayout.NORTH);
                        }
                    } else {
                        JLabel titleLabel = new JLabel(" S. V.");
                        titleLabel.setFont(MySequentialGraphVars.tahomaBoldFont11);
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
                        msg.setFont(MySequentialGraphVars.tahomaPlainFont12);
                        msg.setBackground(Color.WHITE);
                        msg.setHorizontalAlignment(JLabel.CENTER);
                        add(topPanel, BorderLayout.NORTH);
                        add(msg, BorderLayout.CENTER);
                    }
                } catch (Exception ex) {}
            }
        });
    }

    public void enlarge() {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            JFrame f = new JFrame(" SUCCESSOR VALUE DISTRIBUTION");
            f.setLayout(new BorderLayout(3, 3));
            f.add(new ChartPanel(setDepthChart()), BorderLayout.CENTER);
            f.setPreferredSize(new Dimension(450, 350));
            f.pack();
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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

    private JFreeChart setDepthChart() {
        try {
            long max = 0;
            long min = 1000000000;
            long totalCnt = 0;
            double totalValue = 0;
            TreeMap<Long, Integer> successorValueMap = new TreeMap<>();
            for (String s  : MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeSuccessorMaps.keySet()) {
                totalCnt++;
                long value = MyDepthNodeUtil.getDepthNodeSuccessorValue((MyNode) MySequentialGraphVars.g.vRefs.get(s));
                if (successorValueMap.containsKey(value)) {
                    successorValueMap.put(value, successorValueMap.get(value) + 1);
                } else {
                    successorValueMap.put(value, 1);
                }
                if (max < value) {
                    max = value;
                }
                if (min > value && value > 0) {
                    min = value;
                }
            }

            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            for (Long successorValue : successorValueMap.keySet()) {
                dataset.addValue(successorValueMap.get(successorValue), "S. VALUE", successorValue);
            }

            String plotTitle = "";
            String avg = MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(totalValue/totalCnt));
            String xAxis = "[AVG.: " + avg.substring(0, avg.indexOf(".")) + "  MAX.: " + MyMathUtil.getCommaSeperatedNumber(max) + "  MIN.: " + MyMathUtil.getCommaSeperatedNumber(min) + "]";
            String yaxis = "";
            PlotOrientation orientation = PlotOrientation.VERTICAL;
            boolean show = false;
            boolean toolTips = true;
            boolean urls = false;
            return ChartFactory.createBarChart(plotTitle, xAxis, yaxis, dataset, orientation, show, toolTips, urls);
        } catch (Exception ex) {ex.printStackTrace();}
        return null;
    }
}
