package medousa.sequential.graph.stats.singlenode;

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
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

public class MySingleNodeReachTimeDistribution
extends JPanel
implements ActionListener {

    protected boolean MAXIMIZED;
    private float maxValue = 0;
    private float minValue = 1000000000;
    private float avgValue = 0;
    private float stdValue = 0;
    private float toTime = 1f;
    private int selectedTime = 0;

    public MySingleNodeReachTimeDistribution() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                decorate();
            }
        });
    }

    public void decorate() {
        MyProgressBar pb = new MyProgressBar(false);
        removeAll();
        setLayout(new BorderLayout(3, 3));
        setBackground(Color.WHITE);

        ChartPanel chartPanel = new ChartPanel(setValueChart());
        chartPanel.getChart().getCategoryPlot().setRangeGridlinePaint(Color.DARK_GRAY);
        chartPanel.getChart().getCategoryPlot().setDomainGridlinePaint(Color.DARK_GRAY);
        chartPanel.getChart().getCategoryPlot().setBackgroundPaint(Color.WHITE);
        chartPanel.getChart().getCategoryPlot().getDomainAxis().setTickLabelFont(MyDirectGraphVars.tahomaPlainFont11);
        chartPanel.getChart().getCategoryPlot().getDomainAxis().setLabelFont(MyDirectGraphVars.tahomaPlainFont11);
        chartPanel.getChart().getCategoryPlot().getRangeAxis().setTickLabelFont(MyDirectGraphVars.tahomaPlainFont11);
        chartPanel.getChart().getCategoryPlot().getRangeAxis().setLabelFont(MyDirectGraphVars.tahomaPlainFont11);

        CategoryAxis domainAxis = chartPanel.getChart().getCategoryPlot().getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);

        BarRenderer barRenderer = (BarRenderer) chartPanel.getChart().getCategoryPlot().getRenderer();
        barRenderer.setSeriesPaint(0, new Color(0, 0, 0, 0.25f));//Color.LIGHT_GRAY);//Color.decode("#2084FE"));
        barRenderer.setShadowPaint(Color.WHITE);
        barRenderer.setBaseFillPaint(Color.decode("#07CF61"));
        barRenderer.setBarPainter(new StandardBarPainter());
        barRenderer.setBaseLegendTextFont(MyDirectGraphVars.tahomaPlainFont11);

        JComboBox timeMenu = new JComboBox();
        timeMenu.addItem("SEC.");
        timeMenu.addItem("MIN.");
        timeMenu.addItem("HR.");
        timeMenu.setSelectedIndex(selectedTime);
        timeMenu.setFont(MySequentialGraphVars.tahomaPlainFont12);
        timeMenu.setFocusable(false);
        timeMenu.setBackground(Color.WHITE);
        timeMenu.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        if (timeMenu.getSelectedIndex() == 0) {
                            toTime = 1;
                            selectedTime = 0;
                            decorate();
                        } else if (timeMenu.getSelectedIndex() == 1) {
                            selectedTime = 1;
                            toTime = 60;
                            decorate();
                        } else if (timeMenu.getSelectedIndex() == 2) {
                            toTime = 3600;
                            selectedTime = 2;
                            decorate();
                        }
                    }
                }).start();
            }
        });

        JButton enlargeBtn = new JButton("+");
        enlargeBtn.setBackground(Color.WHITE);
        enlargeBtn.setFont(MySequentialGraphVars.tahomaPlainFont12);
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

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Color.WHITE);
        btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 3));
        btnPanel.add(timeMenu);
        btnPanel.add(enlargeBtn);

        JLabel titleLabel = new JLabel(" N. H.");
        titleLabel.setToolTipText("NODE HOP COUNT DISTRIBUTION");
        titleLabel.setFont(MySequentialGraphVars.tahomaBoldFont12);
        titleLabel.setBackground(Color.WHITE);
        titleLabel.setForeground(Color.DARK_GRAY);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout(3, 3));
        topPanel.setBackground(Color.WHITE);
        topPanel.add(titleLabel, BorderLayout.EAST);
        topPanel.add(btnPanel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(chartPanel, BorderLayout.CENTER);

        pb.updateValue(100, 100);
        pb.dispose();
    }

    public void enlarge() {
            try {
                this.decorate();
                JFrame f = new JFrame(" REACH TIME BY NODE DISTRIBUTION");
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

    private JFreeChart setValueChart() {
        int nodeCount = 0;
        float totalValue = 0;

        TreeMap<Long, Integer> valueMap = new TreeMap<>();
        for (String n : MySequentialGraphVars.getSequentialGraphViewer().singleNode.reachTimeMapByObjectID.keySet()) {
            for (long time : MySequentialGraphVars.getSequentialGraphViewer().singleNode.reachTimeMapByObjectID.get(n).keySet()) {
                time = (long) (time/toTime);
                if (time > this.maxValue) {
                    this.maxValue = time;
                }

                if (time > 0 && time < this.minValue) {
                    this.minValue = time;
                }

                if (valueMap.containsKey(time)) {
                    valueMap.put(time, valueMap.get(time) + 1);
                } else {
                    valueMap.put(time, 1);
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
        String xaxis = "REACH TIME BY NODE";
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
