package medousa.sequential.graph.stats;

import medousa.MyProgressBar;
import medousa.direct.utils.MyDirectGraphVars;
import medousa.sequential.graph.MyNode;
import medousa.sequential.utils.MySequentialGraphSysUtil;
import medousa.sequential.utils.MySequentialGraphVars;
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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class MyBetweenContributionDistributionByObjectLineChart
extends JPanel
implements ActionListener {

    public static int instances = 0;
    private float toTime = 1f;
    private int selectedTime = 0;
    private MyProgressBar pb;
    private String source;
    private String dest;
    private JPanel tablePanel = new JPanel();
    private JPanel destTablePanel;

    public MyBetweenContributionDistributionByObjectLineChart() {}

    public void enlarge() {
        this.pb = new MyProgressBar(false);
        try {
            decorate();
            JFrame f = new JFrame(" CONTRIBUTION BY OBJECT DISTRIBUTION BETWEEN TWO NODES");
            f.setLayout(new BorderLayout());
            f.setBackground(Color.WHITE);
            f.setPreferredSize(new Dimension(700, 500));
            f.getContentPane().add(this, BorderLayout.CENTER);
            f.pack();
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.setCursor(Cursor.HAND_CURSOR);
            pb.updateValue(100, 100);
            pb.dispose();
            f.setVisible(true);
        } catch (Exception ex) {
            pb.updateValue(100, 100);
            pb.dispose();
        }
    }

    public void decorate() {
        try {
            removeAll();
            setBackground(Color.WHITE);
            setLayout(new BorderLayout(2,2));

            String [] sourceTableColumns = {"NO.", "SOURCE"};
            String [][] sourceTableData = {};
            DefaultTableModel sourceTableModel = new DefaultTableModel(sourceTableData, sourceTableColumns);
            JTable sourceTable = new JTable(sourceTableModel);

            sourceTable.getColumnModel().getColumn(0).setPreferredWidth(40);
            sourceTable.getColumnModel().getColumn(1).setPreferredWidth(200);
            sourceTable.getTableHeader().setOpaque(false);
            sourceTable.getTableHeader().setFont(MySequentialGraphVars.tahomaBoldFont12);
            sourceTable.getTableHeader().setBackground(new Color(0,0,0,0));
            sourceTable.setBackground(Color.WHITE);
            sourceTable.setFocusable(false);
            sourceTable.setFont(MySequentialGraphVars.f_pln_12);
            sourceTable.setRowHeight(26);
            sourceTable.setSelectionForeground(Color.WHITE);
            sourceTable.setSelectionBackground(Color.BLACK);
            sourceTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent event) {
                    new Thread(new Runnable() {
                        @Override public void run() {
                            MyProgressBar pb = new MyProgressBar(false);
                            try {
                                source = MySequentialGraphVars.nodeNameMap.get(sourceTable.getValueAt(sourceTable.getSelectedRow(), 1).toString());
                                setDestTable();
                                pb.updateValue(100, 100);
                                pb.dispose();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                pb.updateValue(100, 100);
                                pb.dispose();
                            }
                        }
                    }).start();
                }
            });

            JTextField sourceTableSearchTxt = new JTextField();
            sourceTableSearchTxt.setBorder(BorderFactory.createEtchedBorder());
            sourceTableSearchTxt.setFont(MySequentialGraphVars.f_pln_12);
            JButton sourceTableBtn = new JButton("");
            JPanel sourceTableSearchPanel = MyTableUtil.searchTablePanel(this, sourceTableSearchTxt, sourceTableBtn, sourceTableModel, sourceTable);
            sourceTableSearchPanel.remove(sourceTableBtn);

            JScrollPane sourceTableScrollPane = new JScrollPane(sourceTable);
            sourceTableScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
            sourceTableScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 8));

            JPanel sourceTablePanel = new JPanel();
            sourceTablePanel.setBackground(Color.WHITE);
            sourceTablePanel.setLayout(new BorderLayout(1,1));
            sourceTablePanel.add(sourceTableScrollPane, BorderLayout.CENTER);
            sourceTablePanel.add(sourceTableSearchPanel, BorderLayout.SOUTH);

            String [] destTableColumns = {"NO.", "DEST"};
            String [][] destTableData = {};
            DefaultTableModel destTableModel = new DefaultTableModel(destTableData, destTableColumns);
            JTable destTable = new JTable(destTableModel);
            destTable.getTableHeader().setOpaque(false);
            destTable.getTableHeader().setBackground(new Color(0,0,0,0));
            destTable.getTableHeader().setFont(MySequentialGraphVars.tahomaBoldFont12);

            JTextField destTableSearchTxt = new JTextField();
            destTableSearchTxt.setBorder(BorderFactory.createEtchedBorder());
            destTableSearchTxt.setFont(MySequentialGraphVars.f_pln_12);
            JButton destTableBtn = new JButton("");
            JPanel destTableSearchPanel = MyTableUtil.searchTablePanel(this, destTableSearchTxt, destTableBtn, destTableModel, destTable);
            destTableSearchPanel.remove(destTableBtn);

            JScrollPane destTableScrollPane = new JScrollPane(destTable);
            destTableScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(7, 0));
            destTableScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 7));

            destTablePanel = new JPanel();
            destTablePanel.setBackground(Color.WHITE);
            destTablePanel.setLayout(new BorderLayout(1,1));
            destTablePanel.add(destTableScrollPane, BorderLayout.CENTER);
            destTablePanel.add(destTableSearchPanel, BorderLayout.SOUTH);

            int count = 0;
            Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
            for (MyNode n : nodes) {
                sourceTableModel.addRow(new String[]{"" + (++count),
                    MySequentialGraphSysUtil.getNodeName(n.getName())
                });
            }

            tablePanel.setBackground(Color.WHITE);
            tablePanel.setLayout(new GridLayout(2,1));
            tablePanel.add(sourceTablePanel);
            tablePanel.add(destTablePanel);
            tablePanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

            JSplitPane tableChartSplitPane = new JSplitPane();
            tableChartSplitPane.setDividerSize(5);
            tableChartSplitPane.setDividerLocation(0.15);
            tableChartSplitPane.setLeftComponent(tablePanel);
            tableChartSplitPane.setRightComponent(setGraph());
            tableChartSplitPane.addComponentListener(new ComponentAdapter() {
                @Override public void componentResized(ComponentEvent e) {
                    super.componentResized(e);
                    tableChartSplitPane.setDividerLocation(0.15);
                }
            });

            add(tableChartSplitPane, BorderLayout.CENTER);
            revalidate();
            repaint();
        } catch (Exception ex) {
            ex.printStackTrace();
            pb.updateValue(100, 100);
            pb.dispose();
        }
    }

    private void setDestTable() {
        tablePanel.remove(destTablePanel);

        Set<String> successors = new HashSet<>();
        for (int s=0; s < MySequentialGraphVars.seqs.length; s++) {
            for (int i = 1; i < MySequentialGraphVars.seqs[s].length; i++) {
                String sourceItemset = MySequentialGraphVars.seqs[s][i-1].split(":")[0];
                if (sourceItemset.equals(source)) {
                    String destItemset = MySequentialGraphVars.seqs[s][i].split(":")[0];
                    successors.add(destItemset);
                }
            }
        }

        String [] destTableColumns = {"NO.", "DEST"};
        String [][] destTableData = {};
        DefaultTableModel destTableModel = new DefaultTableModel(destTableData, destTableColumns);
        JTable destTable = new JTable(destTableModel);
        destTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        destTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        destTable.getTableHeader().setFont(MySequentialGraphVars.tahomaBoldFont12);
        destTable.setFocusable(false);
        destTable.getTableHeader().setOpaque(false);
        destTable.setSelectionForeground(Color.WHITE);
        destTable.setSelectionBackground(Color.BLACK);
        destTable.getTableHeader().setBackground(new Color(0,0,0,0));
        destTable.setBackground(Color.WHITE);
        destTable.setFont(MySequentialGraphVars.f_pln_12);
        destTable.setRowHeight(26);
        destTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        MyProgressBar pb = new MyProgressBar(false);
                        try {
                            dest = MySequentialGraphVars.nodeNameMap.get(destTable.getValueAt(destTable.getSelectedRow(), 1).toString());
                            setGraph();
                            pb.updateValue(100, 100);
                            pb.dispose();
                            revalidate();
                            repaint();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            pb.updateValue(100, 100);
                            pb.dispose();
                        }
                    }
                }).start();
            }
        });

        int count = 0;
        for (String successor : successors) {
            destTableModel.addRow(new String[]{"" + (++count),
                    MySequentialGraphSysUtil.getNodeName(successor)
            });
        }

        JScrollPane destTableScrollPane = new JScrollPane(destTable);
        destTableScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
        destTableScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 8));

        JTextField destTableSearchTxt = new JTextField();
        destTableSearchTxt.setBorder(BorderFactory.createEtchedBorder());
        destTableSearchTxt.setFont(MySequentialGraphVars.f_pln_12);
        JButton destTableBtn = new JButton("");
        JPanel destTableSearchPanel = MyTableUtil.searchTablePanel(this, destTableSearchTxt, destTableBtn, destTableModel, destTable);
        destTableSearchPanel.remove(destTableBtn);

        destTablePanel = new JPanel();
        destTablePanel.setBackground(Color.WHITE);
        destTablePanel.setLayout(new BorderLayout(1,1));
        destTablePanel.add(destTableScrollPane, BorderLayout.CENTER);
        destTablePanel.add(destTableSearchPanel, BorderLayout.SOUTH);
        destTablePanel.revalidate();

        tablePanel.add(destTablePanel);
        tablePanel.revalidate();
        tablePanel.repaint();
    }

    private JPanel graphPanel = new JPanel();

    private JPanel setGraph() {
        ChartPanel chartPanel = new ChartPanel(setChart());
        chartPanel.getChart().getCategoryPlot().setRangeGridlinePaint(Color.DARK_GRAY);
        chartPanel.getChart().getCategoryPlot().setDomainGridlinePaint(Color.DARK_GRAY);
        chartPanel.getChart().getCategoryPlot().setBackgroundPaint(Color.WHITE);
        chartPanel.getChart().getCategoryPlot().getDomainAxis().setTickLabelFont(MyDirectGraphVars.tahomaPlainFont10);
        chartPanel.getChart().getCategoryPlot().getDomainAxis().setLabelFont(MyDirectGraphVars.tahomaPlainFont10);
        chartPanel.getChart().getCategoryPlot().getRangeAxis().setTickLabelFont(MyDirectGraphVars.tahomaPlainFont10);
        chartPanel.getChart().getCategoryPlot().getRangeAxis().setLabelFont(MyDirectGraphVars.tahomaPlainFont10);

        CategoryAxis domainAxis = chartPanel.getChart().getCategoryPlot().getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
        //Get the plot and set the range axis tick unit
        //NumberAxis rangeAxis = (NumberAxis) chartPanel.getChart().getCategoryPlot().getRangeAxis();
        //rangeAxis.setTickUnit(new NumberTickUnit(1));

        BarRenderer barRenderer = (BarRenderer) chartPanel.getChart().getCategoryPlot().getRenderer();
        barRenderer.setSeriesPaint(0, new Color(0, 0, 0, 0.25f));//Color.LIGHT_GRAY);//Color.decode("#2084FE"));
        barRenderer.setShadowPaint(Color.WHITE);
        barRenderer.setBaseFillPaint(Color.decode("#07CF61"));
        barRenderer.setBarPainter(new StandardBarPainter());
        barRenderer.setBaseLegendTextFont(MyDirectGraphVars.tahomaPlainFont11);

        graphPanel.removeAll();
        graphPanel.setBackground(Color.WHITE);
        graphPanel.setLayout(new BorderLayout(1,1));
        graphPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        graphPanel.add(chartPanel, BorderLayout.CENTER);
        return graphPanel;
    }

    private JFreeChart setChart() {
        try {
            if (dest == null) {
                String plotTitle = "";
                String xaxis = "CONTRIBUTION BY OBJECT BETWEEN TWO NODES";
                String yaxis = "";
                PlotOrientation orientation = PlotOrientation.VERTICAL;
                boolean show = false;
                boolean toolTips = true;
                boolean urls = false;
                return ChartFactory.createBarChart(plotTitle, xaxis, yaxis, new DefaultCategoryDataset(), orientation, show, toolTips, urls);
            }

            BufferedReader br = new BufferedReader(new FileReader(MySequentialGraphVars.sequencesWithObjectIDs));
            String line = "";
            TreeMap<String, Long> betweenContributionByObjectMap = new TreeMap<>();
            if (source.endsWith("x")) {
                while ((line = br.readLine()) != null) {
                    String [] itemsets = line.split(MySequentialGraphVars.hyphenDelimeter);
                    String sourceItemset = itemsets[1].split(":")[0];
                    if (sourceItemset.equals(source)) {
                        for (int i=2; i < itemsets.length; i++) {
                            String destItemset = itemsets[i].split(":")[0];
                            if (destItemset.equals(dest)) {
                                String objectID = itemsets[0].split(":")[0];
                                if (betweenContributionByObjectMap.containsKey(objectID)) {
                                    betweenContributionByObjectMap.put(objectID, betweenContributionByObjectMap.get(objectID) + 1);
                                } else {
                                    betweenContributionByObjectMap.put(objectID, 1L);
                                }
                            }
                        }
                    }
                }
            } else {
                while ((line = br.readLine()) != null) {
                    String[] itemsets = line.split(MySequentialGraphVars.hyphenDelimeter);
                    for (int i = 3; i < itemsets.length; i++) {
                        String sourceItemset = itemsets[i-1].split(":")[0];
                        if (sourceItemset.equals(source)) {
                            String destItemset = itemsets[i].split(":")[0];
                            if (destItemset.equals(dest)) {
                                String objectID = itemsets[0].split(":")[0];
                                if (betweenContributionByObjectMap.containsKey(objectID)) {
                                    betweenContributionByObjectMap.put(objectID, betweenContributionByObjectMap.get(objectID) + 1);
                                } else {
                                    betweenContributionByObjectMap.put(objectID, 1L);
                                }
                            }
                        }
                    }
                }
            }

            LinkedHashMap<Long, Long> contributionCountMap = new LinkedHashMap<>();
            for (long betweenContributionByObjectID : betweenContributionByObjectMap.values()) {
                if (contributionCountMap.containsKey(betweenContributionByObjectID)) {
                    contributionCountMap.put(betweenContributionByObjectID, contributionCountMap.get(betweenContributionByObjectID) + 1);
                } else {
                    contributionCountMap.put(betweenContributionByObjectID, 1L);
                }
            }

            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            for (Long contributionCount : contributionCountMap.keySet()) {
                dataset.addValue(contributionCountMap.get(contributionCount), "CONTRIBUTION BY OBJECT", contributionCount);
            }

            String plotTitle = "";
            String xaxis = "CONTRIBUTION BY OBJECT BETWEEN TWO NODES";
            String yaxis = "";
            PlotOrientation orientation = PlotOrientation.VERTICAL;
            boolean show = false;
            boolean toolTips = true;
            boolean urls = false;
            return ChartFactory.createBarChart(plotTitle, xaxis, yaxis, dataset, orientation, show, toolTips, urls);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
