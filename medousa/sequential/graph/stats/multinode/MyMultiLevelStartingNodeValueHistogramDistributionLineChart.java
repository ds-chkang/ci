package medousa.sequential.graph.stats.multinode;

import medousa.sequential.utils.MyMathUtil;
import medousa.table.MyTableToolTipper;
import medousa.table.MyTableUtil;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import medousa.MyProgressBar;
import medousa.sequential.utils.MySequentialGraphSysUtil;
import medousa.sequential.utils.MySequentialGraphVars;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Random;

public class MyMultiLevelStartingNodeValueHistogramDistributionLineChart
extends JPanel
implements ActionListener {

    private static boolean MAXIMIZED;
    public static int instances = 0;
    private ArrayList<Color> colors;
    private int selelctedGraph;
    private Random rand = new Random();

    public MyMultiLevelStartingNodeValueHistogramDistributionLineChart() {
        this.decorate();
    }

    public void decorate() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                try {
                    removeAll();
                    setLayout(new BorderLayout(3, 3));
                    setBackground(Color.WHITE);

                    int count = 0;
                    colors = new ArrayList<>();
                    LinkedHashMap<String, Long> nodeValueMap = new LinkedHashMap<>();
                    for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                        for (int i = 1; i < MySequentialGraphVars.seqs[s].length; i++) {
                            String n = MySequentialGraphVars.seqs[s][i].split(":")[0];
                            if (MySequentialGraphVars.getSequentialGraphViewer().multiNodes.contains(MySequentialGraphVars.g.vRefs.get(n))) {
                                String startingNode = null;
                                if (selelctedGraph == 0) {
                                    startingNode = MySequentialGraphSysUtil.getNodeName(MySequentialGraphVars.seqs[s][0].split(":")[0]);
                                } else {
                                    startingNode = MySequentialGraphSysUtil.getNodeName(MySequentialGraphVars.seqs[s][1].split(":")[0]);
                                }
                                if (nodeValueMap.containsKey(startingNode)) {
                                    nodeValueMap.put(startingNode, nodeValueMap.get(startingNode) + 1);
                                } else {
                                    nodeValueMap.put(startingNode, 1L);
                                }
                                final float hue = rand.nextFloat();
                                final float saturation = 0.9f;
                                final float luminance = 1.0f;
                                Color randomColor = Color.getHSBColor(hue, saturation, luminance);
                                colors.add(randomColor);
                                count++;
                                break;
                            }
                        }
                        if (count == 6 && !MAXIMIZED) {
                            break;
                        } else if (count == 50 && MAXIMIZED) {
                            break;
                        }
                    }
                    nodeValueMap = MySequentialGraphSysUtil.sortMapByLongValue(nodeValueMap);

                    CategoryDataset dataset = new DefaultCategoryDataset();
                    for (String label : nodeValueMap.keySet()) {
                        ((DefaultCategoryDataset) dataset).addValue(nodeValueMap.get(label), label, "");
                    }

                    // Create a bar chart with the dataset
                    JFreeChart chart = ChartFactory.createBarChart("", "", "", dataset);
                    chart.getCategoryPlot().setBackgroundPaint(Color.WHITE);
                    chart.getCategoryPlot().setDomainGridlinePaint(Color.DARK_GRAY);
                    chart.getCategoryPlot().setRangeGridlinePaint(Color.DARK_GRAY);
                    chart.getCategoryPlot().getDomainAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
                    chart.getCategoryPlot().getRangeAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
                    BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();
                    renderer.setBarPainter(new StandardBarPainter());
                    // Set the colors for each data item in the series
                    for (int i = 0; i < dataset.getColumnCount(); i++) {
                        renderer.setSeriesPaint(i, colors.get(i));
                    }
                    renderer.setMaximumBarWidth(0.07);
                    renderer.setBaseLegendTextFont(MySequentialGraphVars.tahomaPlainFont11);
                    // Create a ChartPanel and display it
                    ChartPanel chartPanel = new ChartPanel(chart);
                    chartPanel.setPreferredSize(new Dimension(350, 367));

                    JLabel titleLabel = new JLabel(" STARTING. N.");
                    titleLabel.setToolTipText("STARTING NODES");
                    titleLabel.setFont(MySequentialGraphVars.tahomaBoldFont12);
                    titleLabel.setBackground(Color.WHITE);
                    titleLabel.setForeground(Color.DARK_GRAY);

                    JPanel titlePanel = new JPanel();
                    titlePanel.setBackground(Color.WHITE);
                    titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
                    titlePanel.add(titleLabel);

                    JButton enlargeBtn = new JButton("+");
                    enlargeBtn.setFont(MySequentialGraphVars.tahomaPlainFont11);
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

                    JComboBox variableAndItemMenu = new JComboBox();
                    variableAndItemMenu.addItem("VAR.");
                    variableAndItemMenu.addItem("ITEM");
                    variableAndItemMenu.setFocusable(false);
                    variableAndItemMenu.setSelectedIndex(selelctedGraph);
                    variableAndItemMenu.setBackground(Color.WHITE);
                    variableAndItemMenu.setFont(MySequentialGraphVars.tahomaPlainFont11);
                    variableAndItemMenu.addActionListener(new ActionListener() {
                        @Override public void actionPerformed(ActionEvent e) {
                            selelctedGraph = variableAndItemMenu.getSelectedIndex();
                            decorate();
                        }
                    });

                    JPanel buttonPanel = new JPanel();
                    buttonPanel.setBackground(Color.WHITE);
                    buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 3));
                    if (MySequentialGraphVars.isSupplementaryOn) {
                        buttonPanel.add(variableAndItemMenu);
                    }
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
        MyProgressBar pb = new MyProgressBar(false);
        try {
            MyMultiLevelStartingNodeValueHistogramDistributionLineChart multiLevelStartingNodeValueHistogramDistributionLineChart = new MyMultiLevelStartingNodeValueHistogramDistributionLineChart();

            String [] columns = {"NO.", "NODE", "CNT."};
            String [][] data= {};

            DefaultTableModel model = new DefaultTableModel(data, columns);

            JTable nodeTable = new JTable(model);
            nodeTable.setBackground(Color.WHITE);
            nodeTable.setRowHeight(25);
            nodeTable.getTableHeader().setFont(MySequentialGraphVars.tahomaBoldFont11);
            nodeTable.getTableHeader().setOpaque(false);
            nodeTable.getTableHeader().setBackground(new Color(0,0,0,0));
            String [] toolTips = {"NO..", "NODE NAME", "COUNT FOR THE APPEARANCES AT STATING POSITION"};
            MyTableToolTipper tooltipHeader = new MyTableToolTipper(nodeTable.getColumnModel());
            tooltipHeader.setToolTipStrings(toolTips);
            nodeTable.setTableHeader(tooltipHeader);

            JButton btn = new JButton();
            JPanel nodeSearchPanel = MyTableUtil.searchTablePanel(this, new JTextField(), btn, model, nodeTable);
            nodeSearchPanel.remove(btn);

            LinkedHashMap<String, Long> nodeValueMap = new LinkedHashMap<>();
            for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                for (int i = 1; i < MySequentialGraphVars.seqs[s].length; i++) {
                    String n = MySequentialGraphVars.seqs[s][i].split(":")[0];
                    if (MySequentialGraphVars.getSequentialGraphViewer().multiNodes.contains(MySequentialGraphVars.g.vRefs.get(n))) {
                        String startingNode = null;
                        if (MySequentialGraphVars.isSupplementaryOn) {
                                startingNode = MySequentialGraphVars.seqs[s][0].split(":")[0];
                                if (nodeValueMap.containsKey(startingNode)) {
                                    nodeValueMap.put(startingNode, nodeValueMap.get(startingNode) + 1);
                                } else {
                                    nodeValueMap.put(startingNode, 1L);
                                }

                                startingNode = MySequentialGraphVars.seqs[s][1].split(":")[0];
                                if (nodeValueMap.containsKey(startingNode)) {
                                    nodeValueMap.put(startingNode, nodeValueMap.get(startingNode) + 1);
                                } else {
                                    nodeValueMap.put(startingNode, 1L);
                                }

                        } else {
                            startingNode = MySequentialGraphVars.seqs[s][0].split(":")[0];
                            if (nodeValueMap.containsKey(startingNode)) {
                                nodeValueMap.put(startingNode, nodeValueMap.get(startingNode) + 1);
                            } else {
                                nodeValueMap.put(startingNode, 1L);
                            }
                        }
                        break;
                    }
                }
            }
            nodeValueMap = MySequentialGraphSysUtil.sortMapByLongValue(nodeValueMap);

            int count = 0;
            for (String n : nodeValueMap.keySet()) {
                model.addRow(new String[]{"" + (count++),
                        MySequentialGraphSysUtil.getNodeName(n),
                        MyMathUtil.getCommaSeperatedNumber(nodeValueMap.get(n))
                });
            }

            JPanel tablePanel = new JPanel();
            tablePanel.setLayout(new BorderLayout(3,3));
            tablePanel.setBackground(Color.WHITE);
            tablePanel.add(new JScrollPane(nodeTable), BorderLayout.CENTER);
            tablePanel.add(nodeSearchPanel, BorderLayout.SOUTH);

            MAXIMIZED = true;
            JFrame f = new JFrame(" STARTING NODE VALUE DISTRIBUTION");
            f.setLayout(new BorderLayout(3, 3));
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.setPreferredSize(new Dimension(450, 350));
            f.pack();
            f.setCursor(Cursor.HAND_CURSOR);

            JSplitPane graphTableSplitPane = new JSplitPane();
            graphTableSplitPane.setDividerLocation(0.8f);
            graphTableSplitPane.setDividerSize(6);
            graphTableSplitPane.setLeftComponent(multiLevelStartingNodeValueHistogramDistributionLineChart);
            graphTableSplitPane.setRightComponent(tablePanel);
            graphTableSplitPane.addComponentListener(new ComponentAdapter() {
                @Override public void componentResized(ComponentEvent e) {
                    super.componentResized(e);
                    graphTableSplitPane.setDividerLocation(0.8f);
                }
            });

            f.getContentPane().add(graphTableSplitPane, BorderLayout.CENTER);
            pb.updateValue(100, 100);
            pb.dispose();
            f.setVisible(true);
        } catch (Exception ex) {
            MAXIMIZED = false;
            pb.updateValue(100, 100);
            pb.dispose();
        }
    }

    @Override public void actionPerformed(ActionEvent e) {

    }

}
