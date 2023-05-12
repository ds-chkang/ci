package medousa.sequential.graph;

import medousa.MyProgressBar;
import medousa.message.MyMessageUtil;
import medousa.sequential.graph.common.MyEdgeBetweennessClusterer;
import medousa.sequential.utils.MyMathUtil;
import medousa.sequential.utils.MyViewerControlComponentUtil;
import medousa.sequential.utils.MySequentialGraphSysUtil;
import medousa.sequential.utils.MySequentialGraphVars;
import medousa.table.MyTableUtil;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.chart.axis.NumberAxis;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class MyClusteringConfig
extends JFrame
implements ActionListener {

    protected static int instances = 0;
    private static JComboBox clusterMenu = new JComboBox();
    private JPanel scatterChartPanel = new JPanel();
    private Map<MyEdge, Float> scoresByRemovedEdge;
    private ChartPanel clusterBarChartPanel;
    public static Paint selectedClusterColor;
    public MyClusteringConfig() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                decorate();
            }
        });
    }

    private JSplitPane contentSplitPane = new JSplitPane();

    private void decorate() {
        final MyClusteringConfig clusteringConfig = this;
        this.setBackground(Color.WHITE);
        this.setLayout(new BorderLayout(3, 3));

        JLabel txtStat = new JLabel();
        txtStat.setBackground(Color.WHITE);
        txtStat.setFont(MySequentialGraphVars.tahomaPlainFont13);

        JLabel numOfEdgesToRemoveLabel = new JLabel(" NO. OF EDGES: ");
        numOfEdgesToRemoveLabel.setFont(MySequentialGraphVars.tahomaBoldFont13);
        numOfEdgesToRemoveLabel.setBackground(Color.WHITE);

        JTextField numOfEdgesToRemoveTxt = new JTextField();
        numOfEdgesToRemoveTxt.setBorder(BorderFactory.createEtchedBorder());
        numOfEdgesToRemoveTxt.setFont(MySequentialGraphVars.tahomaPlainFont13);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setLayout(new BorderLayout(3,3));

        JPanel bottomRightPanel = new JPanel();
        bottomRightPanel.setBackground(Color.WHITE);
        bottomRightPanel.setLayout(new FlowLayout(FlowLayout.RIGHT,3,3));

        JPanel bottomLeftPanel = new JPanel();
        bottomLeftPanel.setBackground(Color.WHITE);
        bottomLeftPanel.setLayout(new FlowLayout(FlowLayout.LEFT,3,3));

        JPanel chartPanel = new JPanel();
        chartPanel.setLayout(new BorderLayout(3, 3));
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setPreferredSize(new Dimension(450, 450));

        JButton runBtn = new JButton(" RUN ");
        runBtn.setFocusable(false);
        runBtn.setPreferredSize(new Dimension(65, 24));
        runBtn.setFont(MySequentialGraphVars.tahomaPlainFont13);
        runBtn.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        if (numOfEdgesToRemoveTxt.getText().length() == 0 || !numOfEdgesToRemoveTxt.getText().matches("[0-9]+")) {
                            MyMessageUtil.showInfoMsg(clusteringConfig, "Provide a number.");
                            return;
                        }

                        chartPanel.removeAll();
                        bottomPanel.removeAll();

                        MyProgressBar pb = new MyProgressBar(false);
                        int numberOfEdges = Integer.parseInt(numOfEdgesToRemoveTxt.getText().replaceAll(" ", ""));
                        Set<String> uniqueColors = new HashSet<>();
                        MyEdgeBetweennessClusterer<MyNode, MyEdge> cluster = new MyEdgeBetweennessClusterer<>(numberOfEdges);
                        Set<Set<MyNode>> clusterSets = cluster.transform(MySequentialGraphVars.g);
                        pb.updateValue(50, 100);
                        Map<Integer, Integer> clustersByEdge = cluster.getClustersByEdge(MySequentialGraphVars.g);
                        setScatterPlotChart(clustersByEdge);
                        scoresByRemovedEdge = cluster.getRemovedEdgeMap();
                        pb.updateValue(70, 100);

                        clusterMenu = new JComboBox();
                        clusterMenu.setFont(MySequentialGraphVars.tahomaPlainFont11);
                        clusterMenu.setBackground(Color.WHITE);
                        clusterMenu.setFocusable(false);
                        clusterMenu.addItem("");
                        for (int i=0; i < clusterSets.size(); i++) {
                            clusterMenu.addItem("" + (i+1));
                        }
                        clusterMenu.setSelectedIndex(0);

                        JLabel clusterLabel = new JLabel("CLUSTER: ");
                        clusterLabel.setBackground(Color.WHITE);
                        clusterLabel.setFont(MySequentialGraphVars.tahomaPlainFont12);

                        bottomRightPanel.removeAll();
                        bottomRightPanel.add(clusterLabel);
                        bottomRightPanel.add(clusterMenu);
                        bottomPanel.add(bottomRightPanel, BorderLayout.EAST);

                        long maxNode = 0;
                        long minNode = 1000000000000L;
                        final Random rand = new Random();
                        for (Set<MyNode> set : clusterSets) {
                            float hue = 0f;
                            float saturation = 0.9f;
                            float luminance = 1.0f;
                            Color nextColor = null;
                            while (true) {
                                hue = rand.nextFloat();
                                String colorName = "" + hue + "" + saturation + "" + luminance;
                                if (!uniqueColors.contains(colorName)) {
                                    uniqueColors.add(colorName);
                                    nextColor = Color.getHSBColor(hue, saturation, luminance);
                                    break;
                                }
                            }

                            for (MyNode n : set) {n.clusteringColor = nextColor;}

                            if (maxNode < set.size()) {
                                maxNode = set.size();
                            }

                            if (minNode > set.size()) {
                                minNode = set.size();
                            }
                        }

                        txtStat.setText("");
                        String statStr = "NO. OF C.: " + MyMathUtil.getCommaSeperatedNumber(clusterSets.size()) +
                        "   AVG. N.: " + MyMathUtil.twoDecimalFormat((double) MySequentialGraphVars.g.getVertexCount()/clusterSets.size()) +
                        "  MIN. N.: " + MyMathUtil.getCommaSeperatedNumber(minNode) + "  MAX. N.: " + MyMathUtil.getCommaSeperatedNumber(maxNode);
                        txtStat.setText(statStr);
                        bottomLeftPanel.add(txtStat);
                        bottomPanel.add(bottomLeftPanel, BorderLayout.CENTER);
                        add(bottomPanel, BorderLayout.SOUTH);

                        MySequentialGraphVars.getSequentialGraphViewer().isClustered = true;
                        contentSplitPane.setDividerSize(6);

                        clusterBarChartPanel = setClusterChart(false);

                        JSplitPane clusterChartAndTableSplitPane = new JSplitPane();
                        clusterChartAndTableSplitPane.setDividerSize(6);
                        clusterChartAndTableSplitPane.setDividerLocation(150);
                        clusterChartAndTableSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
                        clusterChartAndTableSplitPane.setTopComponent(clusterBarChartPanel);
                        clusterChartAndTableSplitPane.setBottomComponent(setRemovedEdgeScoreTable());
                        clusterChartAndTableSplitPane.addComponentListener(new ComponentAdapter() {
                            @Override public void componentResized(ComponentEvent e) {
                                super.componentResized(e);
                                clusterChartAndTableSplitPane.setDividerSize(6);
                                clusterChartAndTableSplitPane.setDividerLocation((int) (getHeight() * 0.45));
                            }
                        });

                        chartPanel.setPreferredSize(new Dimension(550, 500));
                        chartPanel.add(clusterChartAndTableSplitPane, BorderLayout.CENTER);
                        clusterMenu.addActionListener(new ActionListener() {
                            @Override public void actionPerformed(ActionEvent e) {
                                if (clusterMenu.getSelectedIndex() == 0) {
                                    new Thread(new Runnable() {
                                        @Override public void run() {
                                            recoverClusteredNodeValue();
                                            chartPanel.removeAll();
                                            clusterChartAndTableSplitPane.removeAll();
                                            clusterBarChartPanel = setClusterChart(true);
                                            clusterChartAndTableSplitPane.setDividerLocation((int) (getHeight() * 0.45));
                                            clusterChartAndTableSplitPane.setTopComponent(clusterBarChartPanel);
                                            clusterChartAndTableSplitPane.setBottomComponent(setRemovedEdgeScoreTable());
                                            chartPanel.setLayout(new BorderLayout(3, 3));
                                            chartPanel.add(clusterChartAndTableSplitPane, BorderLayout.CENTER);
                                            chartPanel.revalidate();
                                            chartPanel.repaint();
                                            chartPanel.repaint();
                                            MySequentialGraphVars.getSequentialGraphViewer().vc.vTxtStat.setTextStatistics();
                                            MySequentialGraphVars.app.getSequentialGraphDashboard().setGraphLevelDashBoard();
                                            MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueBarChart.setText("N. V. B.");
                                            MySequentialGraphVars.getSequentialGraphViewer().revalidate();
                                            MySequentialGraphVars.getSequentialGraphViewer().repaint();
                                        }
                                    }).start();
                                } else {
                                    new Thread(new Runnable() {
                                        @Override public void run() {
                                            selectedClusterColor = clusterBarChartPanel.getChart().getCategoryPlot().getRenderer().getSeriesPaint(clusterMenu.getSelectedIndex()-1);
                                            chartPanel.removeAll();
                                            clusterChartAndTableSplitPane.removeAll();
                                            setClusteredNodeValue();
                                            clusterChartAndTableSplitPane.setDividerLocation((int) (getHeight() * 0.45));
                                            clusterChartAndTableSplitPane.setTopComponent(setNodeValueLineChart(selectedClusterColor));
                                            clusterChartAndTableSplitPane.setBottomComponent(setClusterNodeTable());
                                            chartPanel.setLayout(new BorderLayout(3, 3));
                                            chartPanel.add(clusterChartAndTableSplitPane, BorderLayout.CENTER);
                                            chartPanel.revalidate();
                                            chartPanel.repaint();
                                            MySequentialGraphVars.getSequentialGraphViewer().vc.vTxtStat.setClusterTextStatistics();
                                            MySequentialGraphVars.app.getSequentialGraphDashboard().setGraphLevelDashBoard();
                                            MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueBarChart.setText("C. N. V. B.");
                                            MySequentialGraphVars.getSequentialGraphViewer().revalidate();
                                            MySequentialGraphVars.getSequentialGraphViewer().repaint();
                                        }
                                    }).start();
                                }
                            }
                        });

                        pb.updateValue(100, 100);
                        pb.dispose();
                        MySequentialGraphVars.getSequentialGraphViewer().revalidate();
                        MySequentialGraphVars.getSequentialGraphViewer().repaint();
                        setAlwaysOnTop(true);
                        revalidate();
                        repaint();
                    }
                }).start();
            }
        });

        JPanel topControlPanel = new JPanel();
        topControlPanel.setBorder(BorderFactory.createEtchedBorder());
        topControlPanel.setBackground(Color.WHITE);
        topControlPanel.setLayout(new BorderLayout(3, 3));
        topControlPanel.add(numOfEdgesToRemoveLabel, BorderLayout.WEST);
        topControlPanel.add(numOfEdgesToRemoveTxt, BorderLayout.CENTER);
        topControlPanel.add(runBtn, BorderLayout.EAST);

        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setLayout(new BorderLayout(3,3));
        //contentPanel.add(topControlPanel, BorderLayout.NORTH);
        contentPanel.add(chartPanel, BorderLayout.CENTER);

        JPanel tablePanel = new JPanel();
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setLayout(new BorderLayout(3,3));
        tablePanel.add(setRemovedEdgeScoreTable(), BorderLayout.CENTER);
        tablePanel.add(setClusterNodeTable(), BorderLayout.EAST);

        JPanel scatterPlotAndEdgeTablePanel = new JPanel();
        scatterPlotAndEdgeTablePanel.setBackground(Color.WHITE);
        scatterPlotAndEdgeTablePanel.setLayout(new BorderLayout(3,3));
        scatterPlotAndEdgeTablePanel.add(setScatterPlotChart(null), BorderLayout.CENTER);
        scatterPlotAndEdgeTablePanel.add(setClusterNodeTable(), BorderLayout.SOUTH);

        contentSplitPane.setDividerLocation(0.45);
        contentSplitPane.setDividerSize(0);
        contentSplitPane.setRightComponent(contentPanel);
        contentSplitPane.setLeftComponent(scatterPlotAndEdgeTablePanel);
        contentSplitPane.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
                if (!MySequentialGraphVars.getSequentialGraphViewer().isClustered) {
                    contentSplitPane.setDividerSize(0);
                    contentSplitPane.setDividerLocation((int) (getWidth() * 0.45));
                } else {
                    contentSplitPane.setDividerSize(6);
                    contentSplitPane.setDividerLocation((int) (getWidth() * 0.45));
                }
            }
        });

        contentSplitPane.setBorder(BorderFactory.createEtchedBorder());
        add(topControlPanel, BorderLayout.NORTH);
        add(contentSplitPane, BorderLayout.CENTER);
        pack();
        setTitle("CLUSTER EXPLORER");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                new Thread(new Runnable() {
                    @Override public void run() {
                        MySequentialGraphVars.getSequentialGraphViewer().vc.depthSelecter.setVisible(true);
                        recoverClusteredNodeValue();
                        MyViewerControlComponentUtil.setDefaultViewerLook();
                        MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueBarChart.setText("N. V. B.");
                        MySequentialGraphVars.getSequentialGraphViewer().vc.clusteringSectorLabel.setEnabled(true);
                        MySequentialGraphVars.getSequentialGraphViewer().vc.clusteringSelector.setEnabled(true);
                        selectedClusterColor = null;
                        instances = 0;
                    }
                }).start();
            }
        });

        addMouseListener(new MouseListener() {
            @Override public void mouseClicked(MouseEvent e) {}
            @Override public void mousePressed(MouseEvent e) {}
            @Override public void mouseReleased(MouseEvent e) {}
            @Override public void mouseEntered(MouseEvent e) {setAlwaysOnTop(true);}
            @Override public void mouseExited(MouseEvent e) {setAlwaysOnTop(false);}
        });
        setPreferredSize(new Dimension(900, 600));
        MySequentialGraphVars.getSequentialGraphViewer().vc.clusteringSectorLabel.setEnabled(false);
        MySequentialGraphVars.getSequentialGraphViewer().vc.clusteringSelector.setEnabled(false);
        setAlwaysOnTop(true);
        setVisible(true);
    }

    private JPanel setRemovedEdgeScoreTable() {
        if (scoresByRemovedEdge == null) {
            JPanel edgeTablePanel = new JPanel();
            edgeTablePanel.setBackground(Color.WHITE);
            return edgeTablePanel;
        }

        float MAX_EDGE_VALUE = 0f;
        float TOTAL_EDGE_VALUE = 0f;
        LinkedHashMap<String, Float> valueMap = new LinkedHashMap<>();
        for (MyEdge e : scoresByRemovedEdge.keySet()) {
            String source = (e.getSource().getName().contains("x") ? MySequentialGraphSysUtil.decodeVariable(e.getSource().getName()) : MySequentialGraphSysUtil.getDecodedNodeName(e.getSource().getName()));
            String dest = (e.getDest().getName().contains("x") ? MySequentialGraphSysUtil.decodeVariable(e.getDest().getName()) : MySequentialGraphSysUtil.getDecodedNodeName(e.getDest().getName()));
            String edgeName = source + "-" + dest;
            valueMap.put(edgeName, scoresByRemovedEdge.get(e));
            if (MAX_EDGE_VALUE < scoresByRemovedEdge.get(e)) {
                MAX_EDGE_VALUE = scoresByRemovedEdge.get(e);
            }
            TOTAL_EDGE_VALUE += scoresByRemovedEdge.get(e);
        }
        valueMap = MySequentialGraphSysUtil.sortMapByFloatValue(valueMap);

        String [] columns = {"NO.", "SOURCE", "DEST", "SCORE", "MAX. V. R.", "TOT. V. R"};
        String [][] data = {};

        DefaultTableModel m = new DefaultTableModel(data, columns);
        JTable edgeTable = new JTable(m);
        edgeTable.getTableHeader().setOpaque(false);
        edgeTable.getTableHeader().setFont(MySequentialGraphVars.tahomaBoldFont12);
        edgeTable.setFont(MySequentialGraphVars.f_pln_12);
        edgeTable.setRowHeight(22);
        edgeTable.setBackground(Color.WHITE);
        edgeTable.getTableHeader().setBackground(new Color(0, 0, 0, 0f));
        edgeTable.getColumnModel().getColumn(0).setPreferredWidth(60);
        edgeTable.getColumnModel().getColumn(0).setMaxWidth(60);
        edgeTable.getColumnModel().getColumn(3).setPreferredWidth(65);
        edgeTable.getColumnModel().getColumn(3).setMaxWidth(90);
        edgeTable.getColumnModel().getColumn(4).setPreferredWidth(90);
        edgeTable.getColumnModel().getColumn(4).setMaxWidth(90);
        edgeTable.getColumnModel().getColumn(5).setPreferredWidth(90);
        edgeTable.getColumnModel().getColumn(5).setMaxWidth(90);

        int count = 0;
        for (String e : valueMap.keySet()) {
            String [] nodes = e.split("-");
            m.addRow(new String[]{
                "" + (++count),
                nodes[0],
                nodes[1],
                MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(valueMap.get(e))),
                    MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(valueMap.get(e)/MAX_EDGE_VALUE)),
                    MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.threeDecimalFormat(valueMap.get(e)/TOTAL_EDGE_VALUE))
            });
        }

        JTextField searchTxt = new JTextField();
        searchTxt.setBorder(BorderFactory.createEtchedBorder());
        searchTxt.setFont(MySequentialGraphVars.tahomaPlainFont12);

        JButton searchBtn = new JButton("");
        JPanel edgeTablePanel = new JPanel();
        edgeTablePanel.setBackground(Color.WHITE);
        edgeTablePanel.setLayout(new BorderLayout(3,3));
        edgeTablePanel.add(new JScrollPane(edgeTable), BorderLayout.CENTER);
        JPanel controlPanel = MyTableUtil.searchAndSaveDataPanelForJTable2(this, searchTxt, searchBtn, m, edgeTable);
        controlPanel.remove(searchBtn);
        edgeTablePanel.add(controlPanel, BorderLayout.SOUTH);
        return edgeTablePanel;
    }

    private JPanel setClusterNodeTable() {
        if (scoresByRemovedEdge == null || clusterMenu.getSelectedIndex() == 0) {
            JPanel clusteNodeTablePanel = new JPanel();
            clusteNodeTablePanel.setBackground(Color.WHITE);
            return clusteNodeTablePanel;
        }

        LinkedHashMap<String, Float> valueMap = new LinkedHashMap<>();
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.clusteringColor == selectedClusterColor) {
                valueMap.put(n.getName(), n.getCurrentValue());
            }
        }
        valueMap = MySequentialGraphSysUtil.sortMapByFloatValue(valueMap);

        String [] columns = {"NO.", "NODE", "VALUE"};
        String [][] data = {};

        DefaultTableModel m = new DefaultTableModel(data, columns);
        JTable clusterNodeTable = new JTable(m);
        clusterNodeTable.getTableHeader().setOpaque(false);
        clusterNodeTable.getTableHeader().setFont(MySequentialGraphVars.tahomaBoldFont12);
        clusterNodeTable.setFont(MySequentialGraphVars.f_pln_12);
        clusterNodeTable.setRowHeight(22);
        clusterNodeTable.setBackground(Color.WHITE);
        clusterNodeTable.getTableHeader().setBackground(new Color(0, 0, 0, 0f));
        clusterNodeTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        clusterNodeTable.getColumnModel().getColumn(0).setMaxWidth(60);
        clusterNodeTable.getColumnModel().getColumn(1).setPreferredWidth(130);
        clusterNodeTable.getColumnModel().getColumn(2).setPreferredWidth(65);
        clusterNodeTable.getColumnModel().getColumn(2).setMaxWidth(80);

        int count = 0;
        for (String n : valueMap.keySet()) {
            String nodeName = (n.contains("x") ? MySequentialGraphSysUtil.decodeVariable(n) : MySequentialGraphSysUtil.getDecodedNodeName(n));
            m.addRow(new String[]{"" + (++count), nodeName, MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(valueMap.get(n)))});
        }

        JTextField searchTxt = new JTextField();
        searchTxt.setBorder(BorderFactory.createEtchedBorder());
        searchTxt.setFont(MySequentialGraphVars.tahomaPlainFont12);

        JButton searchBtn = new JButton("");
        JPanel clusterNodeTablePanel = new JPanel();
        clusterNodeTablePanel.setBackground(Color.WHITE);
        clusterNodeTablePanel.setLayout(new BorderLayout(3,3));
        clusterNodeTablePanel.add(new JScrollPane(clusterNodeTable), BorderLayout.CENTER);
        JPanel controlPanel = MyTableUtil.searchAndSaveDataPanelForJTable2(this, searchTxt, searchBtn, m, clusterNodeTable);
        controlPanel.remove(searchBtn);
        clusterNodeTablePanel.add(controlPanel, BorderLayout.SOUTH);
        return clusterNodeTablePanel;
    }

    private JPanel setScatterPlotChart(Map<Integer, Integer> clustersByEdge) {
        scatterChartPanel.removeAll();
        if (clustersByEdge == null) {
            scatterChartPanel.setBackground(Color.WHITE);
            scatterChartPanel.setLayout(new BorderLayout(3,3));
            return scatterChartPanel;
        }

        XYDataset dataset = createScatterPlotDataset(clustersByEdge);
        JFreeChart chart = ChartFactory.createScatterPlot(
                "NO OF EDGES REMOVED VS NO. OF CLUSTERS",
                "NO. OF EDGES REMOVED", "NO. OF CLUSTERS", dataset);
        chart.setBackgroundPaint(Color.WHITE);
        chart.removeLegend();

        XYPlot plot = (XYPlot)chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.GRAY);
        plot.setDomainGridlinePaint(Color.GRAY);

        ChartPanel panel = new ChartPanel(chart);
        scatterChartPanel.setPreferredSize(new Dimension(450, 500));
        scatterChartPanel.add(panel, BorderLayout.CENTER);
        scatterChartPanel.revalidate();
        scatterChartPanel.repaint();
        return scatterChartPanel;
    }

    private XYDataset createScatterPlotDataset(Map<Integer, Integer> clustersByEdge) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series1 = new XYSeries("NO. OF EDGES REMOVED");
        for (Integer e : clustersByEdge.keySet()) {
            series1.add(e, clustersByEdge.get(e));
        }
        dataset.addSeries(series1);
        return dataset;
    }

    private ChartPanel setClusterChart(boolean isProgressBar) {
        if (!isProgressBar) {
            Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
            LinkedHashMap<Color, Integer> valueMap = new LinkedHashMap<>();
            for (MyNode n : nodes) {
                if (valueMap.containsKey(n.clusteringColor)) {
                    valueMap.put(n.clusteringColor, valueMap.get(n.clusteringColor) + 1);
                } else {
                    valueMap.put(n.clusteringColor, 1);
                }
            }

            int i = 1;
            ArrayList<Color> colorList = new ArrayList<>();
            CategoryDataset dataset = new DefaultCategoryDataset();
            for (Color clusteringColor : valueMap.keySet()) {
                ((DefaultCategoryDataset) dataset).addValue(valueMap.get(clusteringColor), "" + i, "" + (i++));
                colorList.add(clusteringColor);
            }

            JFreeChart chart = ChartFactory.createBarChart("", "", "", dataset);
            chart.getCategoryPlot().setBackgroundPaint(Color.WHITE);
            chart.getCategoryPlot().setDomainGridlinePaint(Color.GRAY);
            chart.getCategoryPlot().setRangeGridlinePaint(Color.GRAY);
            chart.getCategoryPlot().getDomainAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont12);
            chart.getCategoryPlot().getRangeAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont12);
            chart.removeLegend();

            BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();
            renderer.setBarPainter(new StandardBarPainter());
            for (int j = 0; j < dataset.getColumnCount(); j++) {
                renderer.setSeriesPaint(j, colorList.get(j));
            }
            renderer.setMaximumBarWidth(0.09);

            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(550, 367));
            return chartPanel;
        } else {
            MyProgressBar pb = new MyProgressBar(false);
            Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
            LinkedHashMap<Color, Integer> valueMap = new LinkedHashMap<>();
            int pbCnt = 0;
            for (MyNode n : nodes) {
                if (valueMap.containsKey(n.clusteringColor)) {
                    valueMap.put(n.clusteringColor, valueMap.get(n.clusteringColor) + 1);
                } else {
                    valueMap.put(n.clusteringColor, 1);
                }
                pb.updateValue(++pbCnt, nodes.size());
            }

            int i = 1;
            ArrayList<Color> colorList = new ArrayList<>();
            CategoryDataset dataset = new DefaultCategoryDataset();
            for (Color clusteringColor : valueMap.keySet()) {
                ((DefaultCategoryDataset) dataset).addValue(valueMap.get(clusteringColor), "" + i, "" + (i++));
                colorList.add(clusteringColor);
            }

            JFreeChart chart = ChartFactory.createBarChart("", "", "", dataset);
            chart.getCategoryPlot().setBackgroundPaint(Color.WHITE);
            chart.getCategoryPlot().setDomainGridlinePaint(Color.DARK_GRAY);
            chart.getCategoryPlot().setRangeGridlinePaint(Color.DARK_GRAY);
            chart.getCategoryPlot().getDomainAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont12);
            chart.getCategoryPlot().getRangeAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont12);
            chart.removeLegend();

            BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();
            renderer.setBarPainter(new StandardBarPainter());
            for (int j = 0; j < dataset.getColumnCount(); j++) {
                renderer.setSeriesPaint(j, colorList.get(j));
            }
            renderer.setMaximumBarWidth(0.09);

            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(350, 367));
            pb.updateValue(100, 100);
            pb.dispose();
            return chartPanel;
        }
    }

    private JScrollPane setNodeValueLineChart(Paint nodeClusterColor) {
        MyProgressBar pb = new MyProgressBar(false);
        LinkedHashMap<String, Float> nodeMap = new LinkedHashMap<>();
        TreeMap<Integer, Long> valueMap = new TreeMap<>();
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        int pbCnt = 0;
        for (MyNode n : nodes) {
            if (nodeClusterColor == n.clusteringColor) {
                nodeMap.put(n.getName(), n.getCurrentValue());
            }
            pb.updateValue(++pbCnt, 100);
        }
        nodeMap = MySequentialGraphSysUtil.sortMapByFloatValue(nodeMap);

        for (String n : nodeMap.keySet()) {
            int value = (int)((float)nodeMap.get(n));
            if (valueMap.containsKey(value)) {
                valueMap.put(value, valueMap.get(value) + 1);
            } else {
                valueMap.put(value, 1L);
            }
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries valueSeries = new XYSeries("CLUSTER NODE VALUE");
        for (Integer value : valueMap.keySet()) {
            valueSeries.add(value, valueMap.get(value));
        }

        dataset.addSeries(valueSeries);
        JFreeChart chart = ChartFactory.createXYLineChart("", "CLUSTER NODE VALUE DISTRIBUTION", "", dataset);
        chart.removeLegend();
        chart.getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
        chart.getXYPlot().setBackgroundPaint(Color.WHITE);
        chart.getXYPlot().setDomainGridlinePaint(Color.GRAY);
        chart.getXYPlot().setRangeGridlinePaint(Color.GRAY);
        chart.getXYPlot().getDomainAxis().setLabelFont(MySequentialGraphVars.tahomaPlainFont11);
        chart.getXYPlot().getDomainAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
        chart.getXYPlot().getRangeAxis().setLabelFont(MySequentialGraphVars.tahomaPlainFont13);
        chart.getXYPlot().getRangeAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
        chart.getXYPlot().getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        chart.setBackgroundPaint(Color.WHITE);
        XYPlot plot = (XYPlot) chart.getPlot();
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(350, 367));

        JScrollPane chartScrollPane = new JScrollPane(chartPanel);
        chartScrollPane.setPreferredSize(new Dimension(360, 367));
        pb.updateValue(100, 100);
        pb.dispose();
        return chartScrollPane;
    }

    private void setClusteredNodeValue() {
        if (selectedClusterColor == null) return;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (selectedClusterColor == n.clusteringColor) {
                if (n.getCurrentValue() == 0f) {
                    if (n.getOriginalValue() > 0) {
                        n.setCurrentValue(n.getOriginalValue());
                        n.setOriginalValue(0f);
                    }
                }
            } else {
                n.setOriginalValue(n.getCurrentValue());
                n.setCurrentValue(0f);
            }
        }
    }

    private void recoverClusteredNodeValue() {
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0f && n.getOriginalValue() > 0) {
                n.setCurrentValue(n.getOriginalValue());
                n.setOriginalValue(0f);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}