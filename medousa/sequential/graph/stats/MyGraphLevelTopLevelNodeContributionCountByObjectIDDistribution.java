package medousa.sequential.graph.stats;

import medousa.MyProgressBar;
import medousa.direct.utils.MyDirectGraphVars;
import medousa.sequential.graph.MyNode;
import medousa.sequential.utils.MyMathUtil;
import medousa.sequential.utils.MySequentialGraphSysUtil;
import medousa.sequential.utils.MySequentialGraphVars;
import medousa.table.MyTableToolTipper;
import medousa.table.MyTableUtil;
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
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.TreeMap;

public class MyGraphLevelTopLevelNodeContributionCountByObjectIDDistribution
extends JPanel
implements ActionListener {

    protected boolean MAXIMIZED;
    private float maxValue = 0;
    private float minValue = 1000000000;
    private float avgValue = 0;
    private float stdValue = 0;

    public MyGraphLevelTopLevelNodeContributionCountByObjectIDDistribution() {}

    public void decorate() {
        removeAll();
        setLayout(new BorderLayout(1, 1));
        setBackground(Color.WHITE);

        ChartPanel chartPanel = new ChartPanel(setValueChart());
        chartPanel.getChart().getCategoryPlot().setRangeGridlinePaint(Color.DARK_GRAY);
        chartPanel.getChart().getCategoryPlot().setDomainGridlinePaint(Color.DARK_GRAY);
        chartPanel.getChart().getCategoryPlot().setBackgroundPaint(Color.WHITE);
        chartPanel.getChart().getCategoryPlot().getDomainAxis().setTickLabelFont(MyDirectGraphVars.tahomaPlainFont10);
        chartPanel.getChart().getCategoryPlot().getDomainAxis().setLabelFont(MyDirectGraphVars.tahomaPlainFont10);
        chartPanel.getChart().getCategoryPlot().getRangeAxis().setTickLabelFont(MyDirectGraphVars.tahomaPlainFont10);
        chartPanel.getChart().getCategoryPlot().getRangeAxis().setLabelFont(MyDirectGraphVars.tahomaPlainFont10);

        CategoryAxis domainAxis = chartPanel.getChart().getCategoryPlot().getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);

        BarRenderer barRenderer = (BarRenderer) chartPanel.getChart().getCategoryPlot().getRenderer();
        barRenderer.setSeriesPaint(0, new Color(0, 0, 0, 0.25f));
        barRenderer.setShadowPaint(Color.WHITE);
        barRenderer.setBaseFillPaint(Color.decode("#07CF61"));
        barRenderer.setBarPainter(new StandardBarPainter());
        barRenderer.setBaseLegendTextFont(MyDirectGraphVars.tahomaPlainFont11);

        JLabel titleLabel = new JLabel(" CONT. CNT. BY OBJ.");
        titleLabel.setToolTipText("CONTRIBUTION COUNT DISTRIBUTION BY OBJECT");
        titleLabel.setFont(MySequentialGraphVars.tahomaBoldFont12);
        titleLabel.setBackground(Color.WHITE);
        titleLabel.setForeground(Color.DARK_GRAY);

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        titlePanel.add(titleLabel);

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
        }});

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Color.WHITE);
        btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 2,2));
        btnPanel.add(enlargeBtn);

        JPanel northPanel = new JPanel();
        northPanel.setBackground(Color.WHITE);
        northPanel.setLayout(new BorderLayout(1,1));
        northPanel.add(titlePanel, BorderLayout.WEST);
        northPanel.add(btnPanel, BorderLayout.CENTER);

        add(northPanel, BorderLayout.NORTH);
        add(chartPanel, BorderLayout.CENTER);
    }

    public void enlarge() {
            MyProgressBar pb = new MyProgressBar(false);
            try {
                MyGraphLevelTopLevelNodeContributionCountByObjectIDDistribution graphLevelTopLevelNodeContributionCountByObjectIDDistribution = new MyGraphLevelTopLevelNodeContributionCountByObjectIDDistribution();
                graphLevelTopLevelNodeContributionCountByObjectIDDistribution.decorate();

                String[] statTableColumns = {"PROPERTY", "VALUE"};
                String[][] statTableData = {
                        {"NODES", MyMathUtil.getCommaSeperatedNumber(this.nodes.size())},
                        {"MAX. VALUE", MyMathUtil.twoDecimalFormat(this.maxValue)},
                        {"NIN. VALUE", MyMathUtil.twoDecimalFormat(this.minValue)},
                        {"AVG. VALUE", MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(this.avgValue))},
                        {"STD. VALUE", MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(this.stdValue))}
                };

                DefaultTableModel statTableModel = new DefaultTableModel(statTableData, statTableColumns);
                JTable statTable = new JTable(statTableModel);
                statTable.setBackground(Color.WHITE);
                statTable.setFont(MySequentialGraphVars.tahomaPlainFont12);
                statTable.setRowHeight(22);
                statTable.getTableHeader().setFont(MySequentialGraphVars.tahomaBoldFont11);
                statTable.getTableHeader().setOpaque(false);
                statTable.getTableHeader().setBackground(new Color(0, 0, 0, 0f));
                statTable.getTableHeader().getColumnModel().getColumn(0).setPreferredWidth(100);

                String [] statTableColumnTooltips = {"NO. OF NODES", "MAX. CONTRIBUTION VALUE", "MIN. CONTRIBUTION VALUE", "AVG. CONTRIBUTION VALUE", "STANDARD DEVIATION CONTRIBUTION VALUE"};
                MyTableToolTipper statTooltipHeader = new MyTableToolTipper(statTable.getColumnModel());
                statTooltipHeader.setToolTipStrings(statTableColumnTooltips);
                statTable.setTableHeader(statTooltipHeader);

                JScrollPane statTableScrollPane = new JScrollPane(statTable);
                statTableScrollPane.setBackground(Color.WHITE);

                String[] nodeTableColumns = {"NO.", "NODE", "OBJ.", "T. CONT.", "AVG.", "R."};
                String[][] nodeTableData = {};
                DefaultTableModel nodeTableModel = new DefaultTableModel(nodeTableData, nodeTableColumns);
                JTable nodeTable = new JTable(nodeTableModel);
                nodeTable.setBackground(Color.WHITE);
                nodeTable.setFont(MySequentialGraphVars.f_pln_12);
                nodeTable.setRowHeight(22);
                nodeTable.getTableHeader().setFont(MySequentialGraphVars.tahomaBoldFont11);
                nodeTable.getTableHeader().setOpaque(false);
                nodeTable.getTableHeader().setBackground(new Color(0, 0, 0, 0f));
                nodeTable.getTableHeader().getColumnModel().getColumn(0).setPreferredWidth(25);
                nodeTable.getTableHeader().getColumnModel().getColumn(2).setPreferredWidth(30);
                nodeTable.getTableHeader().getColumnModel().getColumn(3).setPreferredWidth(30);
                nodeTable.getTableHeader().getColumnModel().getColumn(4).setPreferredWidth(30);
                nodeTable.getTableHeader().getColumnModel().getColumn(5).setPreferredWidth(30);
                nodeTable.setSelectionForeground(Color.BLACK);
                nodeTable.setSelectionBackground(Color.LIGHT_GRAY);

                String [] nodeTableColumnTooltips = {"NO.", "NODE", "NO. OF OBJECTS", "TOTAL CONTRIBUTIONS BY OBJECTS", "AVG. BY OBJECT", "RATIO OT TOTAL CONTRIBUTION AGAINST MAX. TOTAL CONTRIBUTION BY OBJECTS"};
                MyTableToolTipper nodeTooltipHeader = new MyTableToolTipper(nodeTable.getColumnModel());
                nodeTooltipHeader.setToolTipStrings(nodeTableColumnTooltips);
                nodeTable.setTableHeader(nodeTooltipHeader);

                float max = 0f;
                LinkedHashMap<String, Long> valueMap = new LinkedHashMap();
                for (MyNode n : this.nodes) {
                    long totalContributionByObject = n.getTotalContributionByObjects();
                    valueMap.put(n.getName(), totalContributionByObject);
                    if (max < totalContributionByObject) {
                        max = totalContributionByObject;
                    }
                }
                valueMap = MySequentialGraphSysUtil.sortMapByLongValue(valueMap);

                int i = 0;
                for (String n : valueMap.keySet()) {
                    String pr = MyMathUtil.twoDecimalFormat(valueMap.get(n) / max);
                    nodeTableModel.addRow(new String[]{"" + (++i),
                        MySequentialGraphSysUtil.getNodeName(n),
                        MyMathUtil.getCommaSeperatedNumber(((MyNode)MySequentialGraphVars.g.vRefs.get(n)).contributionCountMapByObjectID.size()),
                        MyMathUtil.getCommaSeperatedNumber(((MyNode)MySequentialGraphVars.g.vRefs.get(n)).getTotalContributionByObjects()),
                        MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat((float) ((MyNode)MySequentialGraphVars.g.vRefs.get(n)).getTotalContributionByObjects()/((MyNode)MySequentialGraphVars.g.vRefs.get(n)).contributionCountMapByObjectID.size())),
                        pr});
                }

                JScrollPane nodeTableScrollPane = new JScrollPane(nodeTable);
                nodeTableScrollPane.setBackground(Color.WHITE);

                JTextField nodeSearchTxt = new JTextField();
                JButton nodeSelectBtn = new JButton("SEL.");
                nodeSelectBtn.setFont(MyDirectGraphVars.tahomaPlainFont12);
                nodeSelectBtn.setFocusable(false);
                nodeSearchTxt.setBackground(Color.WHITE);
                nodeSearchTxt.setBorder(BorderFactory.createLoweredSoftBevelBorder());
                JPanel nodeTableSearchPanel = MyTableUtil.searchAndSaveDataPanelForJTable2(this, nodeSearchTxt, nodeSelectBtn, nodeTableModel, nodeTable);
                nodeSearchTxt.setFont(MyDirectGraphVars.f_bold_12);
                nodeSearchTxt.setToolTipText("TYPE A NODE NAME TO SEARCH");
                nodeSearchTxt.setPreferredSize(new Dimension(90, 19));
                nodeSelectBtn.setPreferredSize(new Dimension(54, 19));
                nodeTableSearchPanel.remove(nodeSelectBtn);

                JPanel nodeTablePanel = new JPanel();
                nodeTablePanel.setBackground(Color.WHITE);
                nodeTablePanel.setLayout(new BorderLayout(3, 3));
                nodeTablePanel.add(nodeTableScrollPane, BorderLayout.CENTER);
                nodeTablePanel.add(nodeTableSearchPanel, BorderLayout.SOUTH);

                JFrame f = new JFrame(" CONTRIBUTION COUNT BY OBJECT DISTRIBUTION");
                f.setBackground(Color.WHITE);
                f.setPreferredSize(new Dimension(550, 450));
                f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                JSplitPane tableSplitPane = new JSplitPane();
                tableSplitPane.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
                tableSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
                tableSplitPane.setTopComponent(statTableScrollPane);
                tableSplitPane.setBottomComponent(nodeTablePanel);
                tableSplitPane.getRightComponent().setBackground(Color.WHITE);
                tableSplitPane.setDividerSize(5);

                JSplitPane contentPane = new JSplitPane();
                contentPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
                contentPane.setLeftComponent(graphLevelTopLevelNodeContributionCountByObjectIDDistribution);
                contentPane.setRightComponent(tableSplitPane);
                contentPane.getRightComponent().setBackground(Color.WHITE);
                contentPane.setDividerSize(5);

                f.setLayout(new BorderLayout(3, 3));
                f.getContentPane().add(contentPane, BorderLayout.CENTER);
                f.pack();
                f.addComponentListener(new ComponentAdapter() {
                    @Override public void componentResized(ComponentEvent e) {
                    super.componentResized(e);
                    contentPane.setDividerLocation(0.79);
                    tableSplitPane.setDividerLocation(0.20);
                    }
                });

                pb.updateValue(100, 100);
                pb.dispose();
                f.setVisible(true);
            } catch (Exception ex) {
                pb.updateValue(100, 100);
                pb.dispose();
            }

    }

    private Set<MyNode> nodes = new HashSet<>();

    private JFreeChart setValueChart() {
        TreeMap<Integer, Integer> valueMap = new TreeMap<>();
        if (MySequentialGraphVars.getSequentialGraphViewer().singleNode != null) {
            this.nodes.add(MySequentialGraphVars.getSequentialGraphViewer().singleNode);
        } else if (MySequentialGraphVars.getSequentialGraphViewer().multiNodes != null && MySequentialGraphVars.getSequentialGraphViewer().multiNodes.size() > 0) {
            this.nodes.addAll(MySequentialGraphVars.getSequentialGraphViewer().multiNodes);
        } else {
            this.nodes.addAll(MySequentialGraphVars.g.getVertices());
        }

        int nodeCount = 0;
        float totalValue = 0;

        for (MyNode n : this.nodes) {
            if (n.getCurrentValue() == 0) continue;
            for (int contCount : n.contributionCountMapByObjectID.values()) {
                int value = contCount;
                totalValue += value;
                nodeCount++;

                if (value > this.maxValue) {
                    this.maxValue = value;
                }

                if (value > 0 && value < this.minValue) {
                    this.minValue = value;
                }

                if (valueMap.containsKey(value)) {
                    valueMap.put(value, valueMap.get(value) + 1);
                } else {
                    valueMap.put(value, 1);
                }
            }
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Integer nodeValue : valueMap.keySet()) {
            dataset.addValue(valueMap.get(nodeValue), "", nodeValue);
        }

        if (nodeCount == 0) nodeCount = 1;
        this.avgValue = totalValue/nodeCount;
        this.stdValue = getNodeValueStandardDeviation(valueMap);

        String plotTitle = "";
        String xaxis = "CONTRIBUTION COUNT BY OBJECT";
        String yaxis = "";
        PlotOrientation orientation = PlotOrientation.VERTICAL;
        boolean show = false;
        boolean toolTips = true;
        boolean urls = false;
        return ChartFactory.createBarChart(plotTitle, xaxis, yaxis, dataset, orientation, show, toolTips, urls);
    }

    public float getNodeValueStandardDeviation(TreeMap<Integer, Integer> valueMap) {
        float sum = 0.00f;
        for (int n : valueMap.keySet()) {
            sum += n;
        }
        double mean = sum / valueMap.size();
        sum = 0f;
        for (int n : valueMap.keySet()) {
            sum += Math.pow(n - mean, 2);
        }
        return (sum == 0.00f ? 0.00f : (float) Math.sqrt(sum / valueMap.size()));
    }

    @Override public void actionPerformed(ActionEvent e) {

    }
}
