package medousa.sequential.graph.stats;

import medousa.sequential.graph.MyComboBoxTooltipRenderer;
import medousa.sequential.graph.MyNode;
import medousa.MyProgressBar;
import medousa.sequential.utils.MyMathUtil;
import medousa.sequential.utils.MySequentialGraphVars;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.HorizontalAlignment;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Ellipse2D;
import java.util.*;
import java.util.List;

public class MyGraphLevelPredecessorSuccessorByDepthLineChart
extends JPanel
implements ActionListener {

    public static int instances = 0;
    private int selectedGraph;
    private Map<Integer, Long> uniqueInNodeCountByDepthMap;
    private Map<Integer, Long> uniqueOutNodeCountByDepthMap;
    private Map<Integer, Long> maxUniqueInNodeCountByDepthMap;
    private Map<Integer, Long> maxUniqueOutNodeCountByDepthMap;
    private Map<Integer, Long> minUniqueInNodeCountByDepthMap;
    private Map<Integer, Long> minUniqueOutNodeCountByDepthMap;
    private XYSeries inUniqueNodeSeries;
    private XYSeries maxInUniqueNodeSeries;
    private XYSeries minInUniqueNodeSeries;
    private XYSeries outUniqueNodeSeries;
    private XYSeries maxOutUniqueNodeSeries;
    private XYSeries minOutUniqueNodeSeries;
    private static JTable nodeStatisticsTable;

    public MyGraphLevelPredecessorSuccessorByDepthLineChart() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                decorate();
            }
        });
    }

    private void setData() {
        this.uniqueInNodeCountByDepthMap = new HashMap<>();
        this.uniqueOutNodeCountByDepthMap = new HashMap<>();
        this.maxUniqueInNodeCountByDepthMap = new HashMap<>();
        this.maxUniqueOutNodeCountByDepthMap = new HashMap<>();
        this.minUniqueInNodeCountByDepthMap = new HashMap<>();
        this.minUniqueOutNodeCountByDepthMap = new HashMap<>();

        for (int i=1; i <= MySequentialGraphVars.mxDepth; i++) {
            this.minUniqueInNodeCountByDepthMap.put(i,  100000000000000L);
            this.minUniqueOutNodeCountByDepthMap.put(i, 100000000000000L);
        }

        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (int i=1; i <= MySequentialGraphVars.mxDepth; i++) {
            for (MyNode n : nodes) {
                if (n.nodeDepthInfoMap.containsKey(i)) {
                    if (uniqueInNodeCountByDepthMap.containsKey(i)) {
                        long uniqueInNodeCount = n.getNodeDepthInfo(i).getPredecessorCount() + uniqueInNodeCountByDepthMap.get(i);
                        uniqueInNodeCountByDepthMap.put(i, uniqueInNodeCount);
                    } else {
                        long uniqueInNodeCount = n.getNodeDepthInfo(i).getPredecessorCount();
                        uniqueInNodeCountByDepthMap.put(i, uniqueInNodeCount);
                    }

                    if (uniqueOutNodeCountByDepthMap.containsKey(i)) {
                        long uniqueOutNodeCount = n.getNodeDepthInfo(i).getSuccessorCount() + uniqueOutNodeCountByDepthMap.get(i);
                        uniqueOutNodeCountByDepthMap.put(i, uniqueOutNodeCount);
                    } else {
                        long uniqueOutNodeCount = n.getNodeDepthInfo(i).getSuccessorCount();
                        uniqueOutNodeCountByDepthMap.put(i, uniqueOutNodeCount);
                    }

                    if (maxUniqueInNodeCountByDepthMap.containsKey(i)) {
                        long uniqueInNodeCount = n.getNodeDepthInfo(i).getPredecessorCount();
                        if (uniqueInNodeCount > maxUniqueInNodeCountByDepthMap.get(i)) {
                            maxUniqueInNodeCountByDepthMap.put(i, uniqueInNodeCount);
                        }
                    } else {
                        long uniqueInNodeCount = n.getNodeDepthInfo(i).getPredecessorCount();
                        maxUniqueInNodeCountByDepthMap.put(i, uniqueInNodeCount);
                    }

                    if (maxUniqueOutNodeCountByDepthMap.containsKey(i)) {
                        long uniqueOutNodeCount = n.getNodeDepthInfo(i).getSuccessorCount();
                        if (uniqueOutNodeCount > maxUniqueOutNodeCountByDepthMap.get(i)) {
                            maxUniqueOutNodeCountByDepthMap.put(i, uniqueOutNodeCount);
                        }
                    } else {
                        long uniqueOutNodeCount = n.getNodeDepthInfo(i).getSuccessorCount();
                        maxUniqueOutNodeCountByDepthMap.put(i, uniqueOutNodeCount);
                    }

                    long uniqueInNodeCount = n.getNodeDepthInfo(i).getPredecessorCount();
                    if (uniqueInNodeCount > 0 && uniqueInNodeCount < minUniqueInNodeCountByDepthMap.get(i)) {
                        minUniqueInNodeCountByDepthMap.put(i, uniqueInNodeCount);
                    }

                    long uniqueOutNodeCount = n.getNodeDepthInfo(i).getSuccessorCount();
                    if (uniqueOutNodeCount > 0 && uniqueOutNodeCount < minUniqueOutNodeCountByDepthMap.get(i)) {
                        minUniqueOutNodeCountByDepthMap.put(i, uniqueOutNodeCount);
                    }
                }
            }
        }

        for (int depth : minUniqueInNodeCountByDepthMap.keySet()) {
            if (minUniqueInNodeCountByDepthMap.get(depth) == 100000000000000L) {
                minUniqueInNodeCountByDepthMap.put(depth, 0L);
            }
        }

        for (int depth : minUniqueOutNodeCountByDepthMap.keySet()) {
            if (minUniqueOutNodeCountByDepthMap.get(depth) == 100000000000000L) {
                minUniqueOutNodeCountByDepthMap.put(depth, 0L);
            }
        }

        this.inUniqueNodeSeries = new XYSeries("P.");
        this.outUniqueNodeSeries = new XYSeries("S.");
        this.minInUniqueNodeSeries = new XYSeries("MIN. P.");
        this.maxInUniqueNodeSeries = new XYSeries("MAX. P.");
        this.minOutUniqueNodeSeries = new XYSeries("MIN. S.");
        this.maxOutUniqueNodeSeries = new XYSeries("MAX. S.");

        for (int i=1; i <= MySequentialGraphVars.mxDepth; i++) {
            this.inUniqueNodeSeries.add(i, this.uniqueInNodeCountByDepthMap.get(i));
            this.outUniqueNodeSeries.add(i, this.uniqueOutNodeCountByDepthMap.get(i));
            this.minInUniqueNodeSeries.add(i, this.minUniqueInNodeCountByDepthMap.get(i));
            this.maxInUniqueNodeSeries.add(i, this.maxUniqueInNodeCountByDepthMap.get(i));
            this.minOutUniqueNodeSeries.add(i, this.minUniqueOutNodeCountByDepthMap.get(i));
            this.maxOutUniqueNodeSeries.add(i, this.maxUniqueOutNodeCountByDepthMap.get(i));
        }
    }

    public void decorate() {
        try {
            removeAll();
            setLayout(new BorderLayout(5, 5));
            setBackground(Color.WHITE);
            setData();

            XYSeriesCollection dataset = new XYSeriesCollection();
            if (selectedGraph == 0) {
                dataset.addSeries(this.inUniqueNodeSeries);
                dataset.addSeries(this.minInUniqueNodeSeries);
                dataset.addSeries(this.maxInUniqueNodeSeries);
                dataset.addSeries(this.outUniqueNodeSeries);
                dataset.addSeries(this.minOutUniqueNodeSeries);
                dataset.addSeries(this.maxOutUniqueNodeSeries);
            } else if (selectedGraph == 1) {
                dataset.addSeries(this.inUniqueNodeSeries);
            } else if (selectedGraph == 2) {
                dataset.addSeries(this.minInUniqueNodeSeries);
            } else if (selectedGraph == 3) {
                dataset.addSeries(this.maxInUniqueNodeSeries);
            } else if (selectedGraph == 4) {
                dataset.addSeries(this.outUniqueNodeSeries);
            } else if (selectedGraph == 5) {
                dataset.addSeries(this.minOutUniqueNodeSeries);
            } else if (selectedGraph == 6) {
                dataset.addSeries(this.maxOutUniqueNodeSeries);
            }

            JFreeChart chart = ChartFactory.createXYLineChart("", "DEPTH", "", dataset);
            chart.getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
            chart.getXYPlot().setBackgroundPaint(Color.WHITE);
            chart.getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
            chart.getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);
            chart.getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
            chart.getXYPlot().getDomainAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
            chart.getXYPlot().getDomainAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
            chart.getXYPlot().getRangeAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
            chart.getXYPlot().getRangeAxis().setLabelFont(MySequentialGraphVars.tahomaPlainFont11);
            chart.getXYPlot().getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
            chart.setBackgroundPaint(Color.WHITE);

            XYPlot plot = (XYPlot) chart.getPlot();
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
            renderer.setSeriesPaint(0, Color.DARK_GRAY);
            renderer.setSeriesStroke(0, new BasicStroke(1.5f));
            renderer.setSeriesShapesVisible(0, true);
            renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
            renderer.setSeriesFillPaint(0, Color.WHITE);

            renderer.setSeriesPaint(1, Color.LIGHT_GRAY);
            renderer.setSeriesStroke(1, new BasicStroke(1.5f));
            renderer.setSeriesShapesVisible(1, true);
            renderer.setSeriesShape(1, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
            renderer.setSeriesFillPaint(1, Color.WHITE);

            renderer.setSeriesPaint(2, Color.BLUE);
            renderer.setSeriesStroke(2, new BasicStroke(1.5f));
            renderer.setSeriesShapesVisible(2, true);
            renderer.setSeriesShape(2, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
            renderer.setSeriesFillPaint(2, Color.WHITE);

            renderer.setSeriesPaint(3, Color.RED);
            renderer.setSeriesStroke(3, new BasicStroke(1.5f));
            renderer.setSeriesShapesVisible(3, true);
            renderer.setSeriesShape(3, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
            renderer.setSeriesFillPaint(3, Color.WHITE);
            renderer.setUseFillPaint(true);

            renderer.setSeriesPaint(4, Color.ORANGE);
            renderer.setSeriesStroke(4, new BasicStroke(1.5f));
            renderer.setSeriesShapesVisible(4, true);
            renderer.setSeriesShape(4, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
            renderer.setSeriesFillPaint(4, Color.WHITE);
            renderer.setUseFillPaint(true);

            renderer.setSeriesPaint(5, Color.PINK);
            renderer.setSeriesStroke(5, new BasicStroke(1.5f));
            renderer.setSeriesShapesVisible(5, true);
            renderer.setSeriesShape(5, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
            renderer.setSeriesFillPaint(5, Color.WHITE);
            renderer.setUseFillPaint(true);

            renderer.setSeriesPaint(6, Color.BLACK);
            renderer.setSeriesStroke(6, new BasicStroke(1.5f));
            renderer.setSeriesShapesVisible(6, true);
            renderer.setSeriesShape(6, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
            renderer.setSeriesFillPaint(6, Color.WHITE);
            renderer.setUseFillPaint(true);

            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(560, 367));

            JLabel titleLabel = new JLabel(" P. & S.");
            titleLabel.setToolTipText("PRDECESSORS & SUCCESSORS BY DEPTH");
            titleLabel.setFont(MySequentialGraphVars.tahomaBoldFont12);
            titleLabel.setBackground(Color.WHITE);
            titleLabel.setForeground(Color.DARK_GRAY);

            JPanel titlePanel = new JPanel();
            titlePanel.setBackground(Color.WHITE);
            titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            titlePanel.add(titleLabel);

            JComboBox graphMenu = new JComboBox();
            String[] tooltips = {
                "SELECT A DISTRIBUTION CHART",
                "NO. OF PREDECESSORS BY DEPTH DISTRIBUTION",
                "MIN. NO. OF PREDECESSORS BY DEPTH DISTRIBUTION",
                "MAX. NO. OF PREDECESSORS BY DEPTH DISTRIBUTION",
                "NO. OF SUCCESSORS BY DEPTH DISTRIBUTION",
                "MIN. NO. OF SUCCESSORS BY DEPTH DISTRIBUTION",
                "MAX. NO. OF SUCCESSORS BY DEPTH DISTRIBUTION"
            };
            graphMenu.setRenderer(new MyComboBoxTooltipRenderer(tooltips));
            graphMenu.setBackground(Color.WHITE);
            graphMenu.setFocusable(false);
            graphMenu.setFont(MySequentialGraphVars.tahomaPlainFont11);
            graphMenu.addItem("SELECT");
            graphMenu.addItem("P.");
            graphMenu.addItem("MIN. P.");
            graphMenu.addItem("MAX. P.");
            graphMenu.addItem("S.");
            graphMenu.addItem("MIN. S.");
            graphMenu.addItem("MAX. S.");
            graphMenu.setSelectedIndex(selectedGraph);
            graphMenu.addActionListener(new ActionListener() {
                @Override public void actionPerformed(ActionEvent e) {
                    new Thread(new Runnable() {
                        @Override public void run() {
                            selectedGraph = graphMenu.getSelectedIndex();
                            decorate();
                            if (selectedGraph == 1) {
                                updateNodeStatisticsTable(uniqueInNodeCountByDepthMap);
                            } else if (selectedGraph == 2) {
                                updateNodeStatisticsTable(minUniqueInNodeCountByDepthMap);
                            } else if (selectedGraph == 3) {
                                updateNodeStatisticsTable(maxUniqueInNodeCountByDepthMap);
                            } else if (selectedGraph == 4) {
                                updateNodeStatisticsTable(uniqueOutNodeCountByDepthMap);
                            } else if (selectedGraph == 5) {
                                updateNodeStatisticsTable(minUniqueOutNodeCountByDepthMap);
                            } else if (selectedGraph == 6) {
                                updateNodeStatisticsTable(maxUniqueOutNodeCountByDepthMap);
                            }
                        }
                    }).start();
                }
            });

            JButton enlargeBtn = new JButton("+");
            enlargeBtn.setFont(MySequentialGraphVars.tahomaPlainFont12);
            enlargeBtn.setFocusable(false);
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

            JPanel btnPanel = new JPanel();
            btnPanel.setBackground(Color.WHITE);
            btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
            btnPanel.add(graphMenu);

            JPanel topPanel = new JPanel();
            topPanel.setLayout(new BorderLayout(0, 0));
            topPanel.setBackground(Color.WHITE);
            topPanel.add(btnPanel, BorderLayout.CENTER);
            btnPanel.add(enlargeBtn);
            topPanel.add(titlePanel, BorderLayout.WEST);

            add(topPanel, BorderLayout.NORTH);
            renderer.setBaseLegendTextFont(MySequentialGraphVars.tahomaPlainFont11);
            add(chartPanel, BorderLayout.CENTER);

            revalidate();
            repaint();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void updateNodeStatisticsTable(Map<Integer, Long> nodeStatisticsMap) {
        DefaultTableModel nodeTableModel = (DefaultTableModel) nodeStatisticsTable.getModel();
        int row = nodeStatisticsTable.getRowCount();
        while (row > 0) {
            nodeTableModel.removeRow(row-1);
            row = nodeStatisticsTable.getRowCount();
        }

        long max = 0;
        for (int depth : nodeStatisticsMap.keySet()) {
            if (nodeStatisticsMap.get(depth) > max) {
                max = nodeStatisticsMap.get(depth);
            }
        }

        for (int depth : nodeStatisticsMap.keySet()) {
            nodeTableModel.addRow(new String[] {
                    "" + depth,
                    MyMathUtil.getCommaSeperatedNumber(nodeStatisticsMap.get(depth)),
                    MyMathUtil.twoDecimalFormat((float) nodeStatisticsMap.get(depth)/max)
            });
        }

        nodeStatisticsTable.revalidate();
        nodeStatisticsTable.repaint();
    }


    public void enlarge() {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            JFrame f = new JFrame("PREDECESSORS & SUCCESSORS BY DEPTH");

            String[] depthLevelColumns = {"DEPTH", "NO. OF NODES", "RATIO"};
            String[][] depthLevelData = {};
            DefaultTableModel model = new DefaultTableModel(depthLevelData, depthLevelColumns);
            JTable depthLevelTable = new JTable(model);
            depthLevelTable.setRowHeight(26);
            depthLevelTable.getColumnModel().getColumn(0).setPreferredWidth(66);
            depthLevelTable.getColumnModel().getColumn(0).setMaxWidth(66);
            depthLevelTable.getColumnModel().getColumn(2).setPreferredWidth(49);
            depthLevelTable.getColumnModel().getColumn(2).setMaxWidth(49);
            depthLevelTable.setBackground(Color.WHITE);
            depthLevelTable.getTableHeader().setBackground(new Color(0, 0, 0, 0));
            depthLevelTable.getTableHeader().setOpaque(false);
            depthLevelTable.getTableHeader().setFont(MySequentialGraphVars.tahomaBoldFont12);
            depthLevelTable.setFont(MySequentialGraphVars.tahomaPlainFont12);

            LinkedHashMap<Integer, Set<String>> uniqueNodesByDepthMap = new LinkedHashMap<>();
            List<MyNode> nodes = new ArrayList<>(MySequentialGraphVars.g.getVertices());
            for (int depth = 0; depth <= MySequentialGraphVars.mxDepth; depth++) {
                for (MyNode n : nodes) {
                    if (n.nodeDepthInfoMap.containsKey(depth + 1)) {
                        if (uniqueNodesByDepthMap.containsKey(depth + 1)) {
                            Set<String> depthNodes = uniqueNodesByDepthMap.get(depth + 1);
                            depthNodes.add(n.getName());
                            uniqueNodesByDepthMap.put((depth + 1), depthNodes);
                        } else {
                            Set<String> deptnNodes = new HashSet<>();
                            deptnNodes.add(n.getName());
                            uniqueNodesByDepthMap.put((depth + 1), deptnNodes);
                        }
                    }
                }
            }

            DefaultTableModel depthLevelTableModel = (DefaultTableModel) depthLevelTable.getModel();
            for (int i : uniqueNodesByDepthMap.keySet()) {
                depthLevelTableModel.addRow(new String[]{
                        "" + i,
                        MyMathUtil.getCommaSeperatedNumber(uniqueNodesByDepthMap.get(i).size()),
                        MyMathUtil.twoDecimalFormat((float) uniqueNodesByDepthMap.get(i).size() / nodes.size())
                });
            }

            String[] nodeLevelColumns = {"DEPTH", "VALUE", "RATIO"};
            String[][] nodeLevelData = {};
            DefaultTableModel predecessorTableModel = new DefaultTableModel(nodeLevelData, nodeLevelColumns);
            nodeStatisticsTable = new JTable(predecessorTableModel);
            nodeStatisticsTable.setRowHeight(26);
            nodeStatisticsTable.getColumnModel().getColumn(0).setPreferredWidth(66);
            nodeStatisticsTable.getColumnModel().getColumn(0).setMaxWidth(66);
            nodeStatisticsTable.getColumnModel().getColumn(2).setPreferredWidth(49);
            nodeStatisticsTable.getColumnModel().getColumn(2).setMaxWidth(49);
            nodeStatisticsTable.setBackground(Color.WHITE);
            nodeStatisticsTable.getTableHeader().setBackground(new Color(0, 0, 0, 0));
            nodeStatisticsTable.getTableHeader().setOpaque(false);
            nodeStatisticsTable.getTableHeader().setFont(MySequentialGraphVars.tahomaBoldFont12);
            nodeStatisticsTable.setFont(MySequentialGraphVars.tahomaPlainFont12);

            long max = 0;
            for (int depth : uniqueInNodeCountByDepthMap.keySet()) {
                if (uniqueInNodeCountByDepthMap.get(depth) > max) {
                    max = uniqueInNodeCountByDepthMap.get(depth);
                }
            }

            for (int depth : uniqueInNodeCountByDepthMap.keySet()) {
                predecessorTableModel.addRow(new String[] {
                        "" + depth,
                        MyMathUtil.getCommaSeperatedNumber(uniqueInNodeCountByDepthMap.get(depth)),
                        MyMathUtil.twoDecimalFormat((float) uniqueInNodeCountByDepthMap.get(depth)/max)
                });
            }

            JSplitPane tableSplitPane = new JSplitPane();
            tableSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
            tableSplitPane.setTopComponent(new JScrollPane(depthLevelTable));
            tableSplitPane.setBottomComponent(new JScrollPane(nodeStatisticsTable));
            tableSplitPane.addComponentListener(new ComponentAdapter() {
                @Override public void componentResized(ComponentEvent e) {
                    super.componentResized(e);
                    tableSplitPane.setDividerLocation((int) (f.getHeight()*0.4));
                }
            });

            JSplitPane chartAndTableSplitPane = new JSplitPane();
            chartAndTableSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
            chartAndTableSplitPane.setLeftComponent(new MyGraphLevelPredecessorSuccessorByDepthLineChart());
            chartAndTableSplitPane.setRightComponent(tableSplitPane);
            chartAndTableSplitPane.addComponentListener(new ComponentAdapter() {
                @Override public void componentResized(ComponentEvent e) {
                    super.componentResized(e);
                    chartAndTableSplitPane.setDividerLocation((int) (f.getWidth()*0.8));
                }
            });

            f.setLayout(new BorderLayout());
            f.setBackground(Color.WHITE);
            f.setPreferredSize(new Dimension(450, 350));
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.getContentPane().add(chartAndTableSplitPane, BorderLayout.CENTER);
            f.pack();
            f.setCursor(Cursor.HAND_CURSOR);
            pb.updateValue(100, 100);
            pb.dispose();
            f.setVisible(true);
        } catch (Exception ex) {
            pb.updateValue(100, 100);
            pb.dispose();
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
