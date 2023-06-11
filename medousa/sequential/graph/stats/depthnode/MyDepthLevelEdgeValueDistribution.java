package medousa.sequential.graph.stats.depthnode;

import medousa.sequential.graph.MyEdge;
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
import java.awt.event.*;
import java.util.Collection;
import java.util.TreeMap;

public class MyDepthLevelEdgeValueDistribution
extends JPanel {

    public MyDepthLevelEdgeValueDistribution() {
        this.decorate();
    }

    public void decorate() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                try {
                    removeAll();
                    setLayout(new BorderLayout(3, 3));
                    setBackground(Color.WHITE);

                    ChartPanel chartPanel = new ChartPanel(setChart());
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

                    JLabel titleLabel = new JLabel(" E. V.");
                    titleLabel.setFont(MySequentialGraphVars.tahomaBoldFont12);
                    titleLabel.setBackground(Color.WHITE);
                    titleLabel.setForeground(Color.DARK_GRAY);

                    JPanel titlePanel = new JPanel();
                    titlePanel.setBackground(Color.WHITE);
                    titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
                    titlePanel.add(titleLabel);

                    JButton enlargeBtn = new JButton("+");
                    enlargeBtn.setFont(MySequentialGraphVars.tahomaBoldFont12);
                    enlargeBtn.setBackground(Color.WHITE);
                    enlargeBtn.setFocusable(false);
                    enlargeBtn.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    showDistribution();
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
                    topPanel.add(titlePanel, BorderLayout.WEST);
                    topPanel.add(enlargePanel, BorderLayout.EAST);

                    add(chartPanel, BorderLayout.CENTER);
                    add(topPanel, BorderLayout.NORTH);

                    revalidate();
                    repaint();
                } catch (Exception ex) {}
                revalidate();
                repaint();
            }
        });
    }

    private JFreeChart setChart() {
        try {
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            int totalCnt = 0;
            int totalValue = 0;
            TreeMap<Integer, Integer> mapByEdgeValue = new TreeMap<>();
            Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
            for (MyEdge e : edges) {
                if (e.getCurrentValue() == 0) continue;
                int value = (int)e.getCurrentValue();
                totalCnt++;
                totalValue += value;
                if (mapByEdgeValue.containsKey(value)) {
                    mapByEdgeValue.put(value, mapByEdgeValue.get(value)+1);
                } else {mapByEdgeValue.put(value, 1);}
            }
            if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() > 6) {
                for (Integer value : mapByEdgeValue.keySet()) {
                    dataset.addValue(mapByEdgeValue.get(value), "E. V.", value);
                }
            } else {
                for (Integer value : mapByEdgeValue.keySet()) {
                    dataset.addValue(mapByEdgeValue.get(value), "E. V.", value);
                }
            }
            String plotTitle = "";
            String avg = MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat((double) totalValue/totalCnt));
            String xAxis = "[AVG.: " + avg + "]";
            String yAxis = "";
            PlotOrientation orientation = PlotOrientation.VERTICAL;
            boolean show = false;
            boolean toolTips = true;
            boolean urls = false;
            return ChartFactory.createBarChart(plotTitle, xAxis, yAxis, dataset, orientation, show, toolTips, urls);
        } catch (Exception ex) {}
        return null;
    }

    public void showDistribution() {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            JFrame f = new JFrame(" DEPTHNODE EDGE VALUE DISTRIBUTION");
            f.setLayout(new BorderLayout(3, 3));
            f.getContentPane().add(new MyDepthLevelStartingNodeValueHistogramDistributionLineChart(), BorderLayout.CENTER);
            f.setPreferredSize(new Dimension(450, 350));
            f.pack();
            f.setAlwaysOnTop(true);
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    f.setAlwaysOnTop(true);
                }

                @Override public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    f.setAlwaysOnTop(false);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    super.mouseExited(e);
                }
            });
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
