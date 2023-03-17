package datamining.graph.stats.depthnode;

import datamining.graph.MyNode;
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

public class MyDepthLevelNodeValueHistogramDistributionLineChart
extends JPanel
implements ActionListener {

    public static int instances = 0;
    private ArrayList<Color> colors;
    private Random rand = new Random();

    public MyDepthLevelNodeValueHistogramDistributionLineChart() {
        this.decorate();
    }

    public void decorate() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                try {
                    removeAll();
                    setLayout(new BorderLayout(3, 3));
                    setBackground(Color.WHITE);

                    colors = new ArrayList<>();
                    LinkedHashMap<String, Long> depthNodeValueMap = new LinkedHashMap<>();
                    for (String depthNodeName : MyVars.getViewer().vc.depthNodeNameSet) {
                        MyNode depthNode = (MyNode) MyVars.g.vRefs.get(depthNodeName);
                        String decodedDepthNodeName = (depthNodeName.contains("x") ? MySysUtil.decodeVariable(depthNodeName) : MySysUtil.getDecodedNodeName(depthNodeName));
                        depthNodeValueMap.put(decodedDepthNodeName, (long)depthNode.getNodeDepthInfo(MyVars.currentGraphDepth).getContribution());
                        final float hue = rand.nextFloat();
                        final float saturation = 0.9f;
                        final float luminance = 1.0f;
                        Color randomColor = Color.getHSBColor(hue, saturation, luminance);
                        colors.add(randomColor);
                    }
                    depthNodeValueMap = MySysUtil.sortMapByLongValue(depthNodeValueMap);

                    CategoryDataset dataset = new DefaultCategoryDataset();
                    for (String label : depthNodeValueMap.keySet()) {
                        ((DefaultCategoryDataset) dataset).addValue(depthNodeValueMap.get(label), label, "");
                    }

                    // Create a bar chart with the dataset
                    JFreeChart chart = ChartFactory.createBarChart("", "", "", dataset);
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

                    JLabel titleLabel = new JLabel(" DEPTHNODE V.");
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

                    JPanel buttonPanel = new JPanel();
                    buttonPanel.setBackground(Color.WHITE);
                    buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 3));
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
                } catch (Exception ex) {}
            }
        });
    }

    public void enlarge() {
        MyDepthLevelNodeValueHistogramDistributionLineChart labelValueDistribution = new MyDepthLevelNodeValueHistogramDistributionLineChart();

        JFrame distFrame = new JFrame(" NODE & EDGE LABEL VALUE DISTRIBUTION");
        distFrame.setLayout(new BorderLayout(3,3));
        distFrame.getContentPane().add(labelValueDistribution, BorderLayout.CENTER);
        distFrame.setPreferredSize(new Dimension(450, 350));
        distFrame.pack();
        distFrame.setVisible(true);
    }

    @Override public void actionPerformed(ActionEvent e) {

    }

}
