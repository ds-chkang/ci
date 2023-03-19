package datamining.graph.stats.multinode;

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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Random;

public class MyMultiLevelEndingNodeValueHistogramDistributionLineChart
extends JPanel
implements ActionListener {

    public static int instances = 0;
    private ArrayList<Color> colors;
    private Random rand = new Random();

    public MyMultiLevelEndingNodeValueHistogramDistributionLineChart() {
        this.decorate();
    }

    public synchronized void decorate() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                try {
                    removeAll();
                    setLayout(new BorderLayout(3, 3));
                    setBackground(Color.WHITE);

                    colors = new ArrayList<>();
                    LinkedHashMap<String, Long> endingNodeValueMap = new LinkedHashMap<>();
                    for (MyNode selectedNode : MyVars.getViewer().multiNodes) {
                        for (int s = 0; s < MyVars.seqs.length; s++) {
                            for (int i = 0; i < MyVars.seqs[s].length; i++) {
                                String n = MyVars.seqs[s][i].split(":")[0];
                                if ((i + 1) != MyVars.seqs[s].length && n.equals(selectedNode.getName())) {
                                    String endingNode = MyVars.seqs[s][MyVars.seqs[s].length - 1].split(":")[0];
                                    endingNode = (endingNode.contains("x") ? MySysUtil.decodeVariable(endingNode) : MySysUtil.getDecodedNodeName(endingNode));
                                    if (endingNodeValueMap.containsKey(endingNode)) {
                                        endingNodeValueMap.put(endingNode, endingNodeValueMap.get(endingNode) + 1);
                                    } else {
                                        endingNodeValueMap.put(endingNode, 1L);
                                    }
                                    final float hue = rand.nextFloat();
                                    final float saturation = 0.9f;
                                    final float luminance = 1.0f;
                                    Color randomColor = Color.getHSBColor(hue, saturation, luminance);
                                    colors.add(randomColor);
                                    break;
                                }
                            }
                        }
                    }
                    endingNodeValueMap = MySysUtil.sortMapByLongValue(endingNodeValueMap);

                    CategoryDataset dataset = new DefaultCategoryDataset();
                    for (String label : endingNodeValueMap.keySet()) {
                        ((DefaultCategoryDataset) dataset).addValue(endingNodeValueMap.get(label), label, "");
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
                    renderer.setMaximumBarWidth(0.07);
                    renderer.setBaseLegendTextFont(MyVars.tahomaPlainFont10);
                    // Create a ChartPanel and display it
                    ChartPanel chartPanel = new ChartPanel(chart);
                    chartPanel.setPreferredSize(new Dimension(350, 367));

                    JLabel titleLabel = new JLabel(" E. N.");
                    titleLabel.setToolTipText("ENDING NODE VALUE DISTRIBUTION");
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
                        @Override public void actionPerformed(ActionEvent e) {
                            new Thread(new Runnable() {
                                @Override public void run() {
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
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void enlarge() {
        MyMultiLevelEndingNodeValueHistogramDistributionLineChart labelValueDistribution = new MyMultiLevelEndingNodeValueHistogramDistributionLineChart();

        JFrame distFrame = new JFrame(" SUCCESSOR VALUE DISTRIBUTION");
        distFrame.setLayout(new BorderLayout(3,3));
        distFrame.getContentPane().add(labelValueDistribution, BorderLayout.CENTER);
        distFrame.setPreferredSize(new Dimension(450, 350));
        distFrame.pack();
        distFrame.setVisible(true);
    }

    @Override public void actionPerformed(ActionEvent e) {

    }

}
