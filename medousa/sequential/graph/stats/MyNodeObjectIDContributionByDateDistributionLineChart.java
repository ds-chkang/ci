package medousa.sequential.graph.stats;

import medousa.direct.utils.MyDirectGraphVars;
import medousa.sequential.graph.MyNode;
import medousa.sequential.utils.MyMathUtil;
import medousa.sequential.utils.MySequentialGraphSysUtil;
import medousa.sequential.utils.MySequentialGraphVars;
import medousa.table.MyTableUtil;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.*;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Ellipse2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.LinkedHashMap;

public class MyNodeObjectIDContributionByDateDistributionLineChart
implements ActionListener {

    private long max = 0;
    private long min = 10000000000L;
    private float avg = 0f;
    private int selectedTable;
    private JComboBox tableOption = new JComboBox();
    private JTable optionTable;
    private JTable bottomTable;
    private ChartPanel chartPanel = new ChartPanel(null);
    private LinkedHashMap<String, Long> timeSeariesDataMap;

    public MyNodeObjectIDContributionByDateDistributionLineChart() {
        try {
            BufferedReader br = new BufferedReader(
                new FileReader(MySequentialGraphSysUtil.getWorkingDir() + MySequentialGraphSysUtil.getDirectorySlash() + "nodedatefeatures.txt"));

            String line = "";
            timeSeariesDataMap = new LinkedHashMap<>();
            while ((line = br.readLine()) != null) {
                String [] itemsets = line.split("-");
                for (String itemset : itemsets) {
                    String date = itemset.split(":")[1];
                    if (timeSeariesDataMap.containsKey(date)) {
                        long nodeContribution = timeSeariesDataMap.get(date) + 1;
                        timeSeariesDataMap.put(date, nodeContribution);
                    } else {
                        timeSeariesDataMap.put(date, 1L);
                    }
                }
            }
            timeSeariesDataMap = MySequentialGraphSysUtil.sortMapByLongValue(timeSeariesDataMap);

            String [] propertyColumns = {"PROPERTY", "VALUE"};
            String [][] propertyData = {};

            DefaultTableModel topTableStatisticsModel = new DefaultTableModel(propertyData, propertyColumns);
            JTable tableTopStatistics = new JTable(topTableStatisticsModel);
            tableTopStatistics.setBackground(Color.WHITE);
            tableTopStatistics.getTableHeader().setFont(MySequentialGraphVars.tahomaBoldFont12);
            tableTopStatistics.getTableHeader().setBackground(new Color(0,0,0,0));
            tableTopStatistics.getTableHeader().setOpaque(false);
            tableTopStatistics.setRowHeight(26);
            tableTopStatistics.setFont(MySequentialGraphVars.f_pln_12);
            tableTopStatistics.getColumnModel().getColumn(1).setPreferredWidth(60);
            tableTopStatistics.getColumnModel().getColumn(0).setPreferredWidth(195);

            topTableStatisticsModel.addRow(new String[]{" NO. OF DATES", " " + MyMathUtil.getCommaSeperatedNumber(timeSeariesDataMap.size())});
            topTableStatisticsModel.addRow(new String[]{" MAX.", " " + MyMathUtil.getCommaSeperatedNumber(max)});
            topTableStatisticsModel.addRow(new String[]{" MIN.", " " + MyMathUtil.getCommaSeperatedNumber(min)});
            topTableStatisticsModel.addRow(new String[]{" AVG.", " " + MyMathUtil.twoDecimalFormat(avg)});

            JSplitPane topMiddleSplitPane = new JSplitPane();
            topMiddleSplitPane.setDividerSize(5);
            topMiddleSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
            topMiddleSplitPane.setTopComponent(new JScrollPane(tableTopStatistics));
            topMiddleSplitPane.setBottomComponent(setOptionTable(timeSeariesDataMap));
            topMiddleSplitPane.setDividerLocation(0.35f);
            topMiddleSplitPane.addComponentListener(new ComponentAdapter() {
                @Override public void componentResized(ComponentEvent e) {
                    super.componentResized(e);
                    topMiddleSplitPane.setDividerLocation(0.35f);
                }
            });

            JSplitPane middleBottomSplitPane = new JSplitPane();
            middleBottomSplitPane.setDividerSize(5);
            middleBottomSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
            middleBottomSplitPane.setTopComponent(topMiddleSplitPane);
            middleBottomSplitPane.setBottomComponent(setBottomTable());
            middleBottomSplitPane.setDividerLocation(0.6f);
            middleBottomSplitPane.addComponentListener(new ComponentAdapter() {
                @Override public void componentResized(ComponentEvent e) {
                    super.componentResized(e);
                    middleBottomSplitPane.setDividerLocation(0.6f);
                }
            });

            JSplitPane tableGraphSplitPane = new JSplitPane();
            tableGraphSplitPane.setDividerSize(5);
            setChart(timeSeariesDataMap);
            tableGraphSplitPane.setLeftComponent(chartPanel);
            tableGraphSplitPane.setRightComponent(middleBottomSplitPane);
            tableGraphSplitPane.setDividerLocation(0.81f);
            tableGraphSplitPane.addComponentListener(new ComponentAdapter() {
                @Override public void componentResized(ComponentEvent e) {
                    super.componentResized(e);
                    tableGraphSplitPane.setDividerLocation(0.81f);
                }
            });

            JFrame f = new JFrame(" NODE CONTRIBUTIONS BY DATE DISTRIBUTION");
            f.setPreferredSize(new Dimension(600, 400));
            f.setLayout(new BorderLayout(3,3));
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.getContentPane().add(tableGraphSplitPane, BorderLayout.CENTER);
            f.pack();
            f.setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setChart(LinkedHashMap<String, Long> timeSeriesDataMap) {
        float totalContribution = 0f;
        chartPanel.removeAll();
        TimeSeries series = new TimeSeries("");
        //System.out.println(timeSeriesDataMap);
        for (String date : timeSeriesDataMap.keySet()) {
            long nodeContribution = timeSeriesDataMap.get(date);
            totalContribution += nodeContribution;

            if (nodeContribution > max) {
                max = nodeContribution;
            }

            if (min > nodeContribution) {
                min = nodeContribution;
            }

            String [] dateElements = (date.contains("*") ? date.split("\\*") : date.split("-"));
            series.add(new Day(Integer.parseInt(dateElements[2]), Integer.parseInt(dateElements[1]), Integer.parseInt(dateElements[0])), timeSeriesDataMap.get(date));
        }

        avg = totalContribution/ timeSeriesDataMap.size();

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(series);

        // Create chart
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "",
                "",
                "",
                dataset,
                false,
                true,
                false
        );

        chart.getXYPlot().setBackgroundPaint(Color.WHITE);
        chart.getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
        chart.getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);

        XYPlot plot = (XYPlot) chart.getPlot();
        DateAxis dateAxis = new DateAxis();
        dateAxis.setDateFormatOverride(new SimpleDateFormat("yyyy-MM-dd"));
        dateAxis.setLabelAngle(Math.PI / 6.0);
        dateAxis.setVerticalTickLabels(true);
        plot.setDomainAxis(dateAxis);
        NumberAxis rangeAxis = (NumberAxis) chart.getXYPlot().getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        chart.getXYPlot().getDomainAxis().setTickLabelFont(MyDirectGraphVars.f_pln_12);
        chart.getXYPlot().getRangeAxis().setTickLabelFont(MyDirectGraphVars.f_pln_12);
        chart.getTitle().setFont(MySequentialGraphVars.tahomaBoldFont12);

        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, Color.DARK_GRAY);
        renderer.setSeriesStroke(0, new BasicStroke(1.4f));
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
        renderer.setSeriesFillPaint(0, Color.WHITE);
        renderer.setUseFillPaint(true);

        renderer.setSeriesToolTipGenerator(0, new StandardXYToolTipGenerator());
        chart.removeLegend();
        chartPanel.setChart(chart);
        chartPanel.revalidate();
        chartPanel.repaint();
    }


    private JPanel setOptionTable(LinkedHashMap<String, Long> nodeContributionByDateMap) {
        tableOption.addItem("DATE");
        tableOption.addItem("NODE");
        tableOption.setSelectedIndex(selectedTable);
        tableOption.setFont(MySequentialGraphVars.tahomaPlainFont12);
        tableOption.setBackground(Color.WHITE);
        tableOption.setFocusable(false);
        tableOption.addActionListener(this);

        String [] optionTableColumns = {"NO.", "DATE", "CONT."};
        String [][] optionTableData = {};
        DefaultTableModel optionTableModel = new DefaultTableModel(optionTableData, optionTableColumns);
        optionTable = new JTable(optionTableModel);
        optionTable.setBackground(Color.WHITE);
        optionTable.getTableHeader().setFont(MySequentialGraphVars.tahomaBoldFont12);
        optionTable.getTableHeader().setBackground(new Color(0,0,0,0));
        optionTable.getTableHeader().setOpaque(false);
        optionTable.setRowHeight(26);
        optionTable.setFont(MySequentialGraphVars.f_pln_12);
        optionTable.getColumnModel().getColumn(1).setPreferredWidth(120);
        optionTable.getColumnModel().getColumn(0).setPreferredWidth(60);
        optionTable.getColumnModel().getColumn(2).setPreferredWidth(60);
        optionTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        //MyProgressBar pb = new MyProgressBar(false);
                        try {
                            if (selectedTable == 0) {
                                String date = optionTable.getValueAt(optionTable.getSelectedRow(), 1).toString().trim();
                                BufferedReader br = new BufferedReader(new FileReader(
                                MySequentialGraphSysUtil.getWorkingDir()+
                                        MySequentialGraphSysUtil.getDirectorySlash()+
                                        "nodedatefeatures.txt"));

                                String line = "";
                                LinkedHashMap<String, Long> nodeMap = new LinkedHashMap<>();
                                while ((line = br.readLine()) != null) {
                                    String [] itemsets = line.split("-");
                                    for (String itemset : itemsets) {
                                        String [] nodeDate = itemset.split(":");
                                        nodeDate[1] = nodeDate[1].replaceAll("\\*", "-").trim();
                                        if (nodeDate[1].equals(date)) {
                                            if (nodeMap.containsKey(nodeDate[0])) {
                                                nodeMap.put(nodeDate[0], nodeMap.get(nodeDate[0])+1);
                                            } else {
                                                nodeMap.put(nodeDate[0], 1L);
                                            }
                                        }
                                    }
                                }
                                nodeMap = MySequentialGraphSysUtil.sortMapByLongValue(nodeMap);

                                int row = bottomTable.getRowCount();
                                while (row > 0) {
                                    ((DefaultTableModel) bottomTable.getModel()).removeRow(row - 1);
                                    row = bottomTable.getRowCount();
                                }

                                int i=0;
                                for (String n : nodeMap.keySet()) {
                                    ((DefaultTableModel) bottomTable.getModel()).addRow(new String[]{
                                        " " + (++i),
                                        " " + MySequentialGraphSysUtil.getNodeName(n),
                                        " " + MyMathUtil.getCommaSeperatedNumber(nodeMap.get(n))
                                    });
                                }

                                bottomTable.revalidate();
                                bottomTable.repaint();
                            } else if (selectedTable == 1) {
                                String node = MySequentialGraphVars.nodeNameMap.get(optionTable.getValueAt(optionTable.getSelectedRow(), 1).toString().trim());
                                BufferedReader br = new BufferedReader(new FileReader(
                                        MySequentialGraphSysUtil.getWorkingDir()+
                                                MySequentialGraphSysUtil.getDirectorySlash()+
                                                "nodedatefeatures.txt"));

                                String line = "";
                                LinkedHashMap<String, Long> dateMap = new LinkedHashMap<>();
                                while ((line = br.readLine()) != null) {
                                    String [] itemsets = line.split("-");
                                    for (String itemset : itemsets) {
                                        String [] nodeDate = itemset.split(":");
                                        nodeDate[1] = nodeDate[1].replaceAll("\\*", "-").trim();
                                        if (nodeDate[0].equals(node)) {
                                            if (dateMap.containsKey(nodeDate[1])) {
                                                dateMap.put(nodeDate[1], dateMap.get(nodeDate[1])+1);
                                            } else {
                                                dateMap.put(nodeDate[1], 1L);
                                            }
                                        }
                                    }
                                }
                                dateMap = MySequentialGraphSysUtil.sortMapByLongValue(dateMap);

                                int row = bottomTable.getRowCount();
                                while (row > 0) {
                                    ((DefaultTableModel) bottomTable.getModel()).removeRow(row - 1);
                                    row = bottomTable.getRowCount();
                                }

                                int i=0;
                                for (String date : dateMap.keySet()) {
                                    ((DefaultTableModel) bottomTable.getModel()).addRow(new String[]{
                                            " " + (++i),
                                            " " + date,
                                            " " + MyMathUtil.getCommaSeperatedNumber(dateMap.get(date))
                                    });
                                }

                                bottomTable.revalidate();
                                bottomTable.repaint();

                                setChart(dateMap);
                            }
                        //    pb.updateValue(100, 100);
                        //    pb.dispose();
                        } catch (Exception ex) {
                            chartPanel.revalidate();
                            chartPanel.repaint();
                            //ex.printStackTrace();
                        //    pb.updateValue(100, 100);
                        //    pb.dispose();
                        }
                    }
                }).start();
            }
        });

        int row = optionTable.getRowCount();
        while (row > 0) {
            ((DefaultTableModel) optionTable.getModel()).removeRow(row - 1);
            row = optionTable.getRowCount();
        }

        int i = 0;
        if (tableOption.getSelectedIndex() == 0) {
            for (String date : nodeContributionByDateMap.keySet()) {
                optionTableModel.addRow(new String[]{" " + (++i),
                        " " + date.replaceAll("\\*", "-"),
                        " " + MyMathUtil.getCommaSeperatedNumber(nodeContributionByDateMap.get(date))
                });
            }
        } else {
            Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
            for (MyNode n : nodes) {
                ((DefaultTableModel) optionTable.getModel()).addRow(new String[]{
                        " " + (++i),
                        " " + MySequentialGraphSysUtil.getNodeName(n.getName()),
                        " " + MyMathUtil.getCommaSeperatedNumber(n.getContribution())
                });
            }
        }

        JButton searchBtn = new JButton();
        JTextField searchTxt = new JTextField();
        searchTxt.setFont(MySequentialGraphVars.f_pln_12);

        JPanel searchTablePanel = MyTableUtil.searchTablePanel(this, searchTxt, searchBtn, optionTableModel, this.optionTable);
        searchTablePanel.remove(searchBtn);

        JPanel optionTablePanel = new JPanel();
        optionTablePanel.setLayout(new BorderLayout(3,3));
        optionTablePanel.add(new JScrollPane(optionTable), BorderLayout.CENTER);
        optionTablePanel.add(tableOption, BorderLayout.NORTH);
        optionTablePanel.add(searchTablePanel, BorderLayout.SOUTH);
        return optionTablePanel;
    }

    private JPanel setBottomTable() {
        String [] bottomTableColumns = {"NO.", "NODE", "CONT."};
        String [][] bottomTableData = {};
        DefaultTableModel bottomTableModel = new DefaultTableModel(bottomTableData, bottomTableColumns);
        bottomTable = new JTable(bottomTableModel);
        bottomTable.getTableHeader().setFont(MySequentialGraphVars.tahomaBoldFont12);
        bottomTable.getTableHeader().setBackground(new Color(0,0,0,0));
        bottomTable.getTableHeader().setOpaque(false);
        bottomTable.setRowHeight(26);
        bottomTable.setBackground(Color.WHITE);
        bottomTable.setFont(MySequentialGraphVars.f_pln_12);
        bottomTable.getColumnModel().getColumn(1).setPreferredWidth(120);
        bottomTable.getColumnModel().getColumn(0).setPreferredWidth(60);
        bottomTable.getColumnModel().getColumn(2).setPreferredWidth(60);

        JButton searchBtn = new JButton();
        JTextField searchTxt = new JTextField();
        searchTxt.setFont(MySequentialGraphVars.f_pln_12);

        JPanel searchTablePanel = MyTableUtil.searchTablePanel(this, searchTxt, searchBtn, bottomTableModel, this.bottomTable);
        searchTablePanel.remove(searchBtn);
        JPanel bottomTablePanel = new JPanel();
        bottomTablePanel.setLayout(new BorderLayout(3,3));
        bottomTablePanel.add(new JScrollPane(bottomTable), BorderLayout.CENTER);
        bottomTablePanel.add(searchTablePanel, BorderLayout.SOUTH);
        return bottomTablePanel;
    }


    public static void main(String[] args) {
        MyNodeObjectIDContributionByDateDistributionLineChart example = new MyNodeObjectIDContributionByDateDistributionLineChart();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == tableOption) {
            if (tableOption.getSelectedIndex() == 0) {
                selectedTable = 0;

                int row = optionTable.getRowCount();
                while (row > 0) {
                    ((DefaultTableModel) optionTable.getModel()).removeRow(row - 1);
                    row = optionTable.getRowCount();
                }

                int i=0;
                for (String date : timeSeariesDataMap.keySet()) {
                    ((DefaultTableModel) optionTable.getModel()).addRow(new String[]{" " + (++i),
                        " " + date.replaceAll("\\*", "-"),
                        " " + MyMathUtil.getCommaSeperatedNumber(timeSeariesDataMap.get(date))
                    });
                }

                JTableHeader th = optionTable.getTableHeader();
                TableColumnModel tcm = th.getColumnModel();
                TableColumn tc = tcm.getColumn(1);
                tc.setHeaderValue( "DATE" );
                th.repaint();

                optionTable.revalidate();
                optionTable.repaint();

                setChart(timeSeariesDataMap);
            } else if (tableOption.getSelectedIndex() == 1) {
                selectedTable = 1;

                int row = optionTable.getRowCount();
                while (row > 0) {
                    ((DefaultTableModel) optionTable.getModel()).removeRow(row - 1);
                    row = optionTable.getRowCount();
                }

                JTableHeader th = optionTable.getTableHeader();
                TableColumnModel tcm = th.getColumnModel();
                TableColumn tc = tcm.getColumn(1);
                tc.setHeaderValue( "NODE" );
                th.repaint();

                int i=0;
                Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
                LinkedHashMap<String, Long> nodeContributionByDateMap = new LinkedHashMap<>();
                for (MyNode n : nodes) {
                    if (nodeContributionByDateMap.containsKey(n.getName())) {
                        nodeContributionByDateMap.put(n.getName(), nodeContributionByDateMap.get(n.getName()) + 1);
                    } else {
                        nodeContributionByDateMap.put(n.getName(), 1L);
                    }
                }

                for (String n : nodeContributionByDateMap.keySet()) {
                    ((DefaultTableModel) optionTable.getModel()).addRow(
                        new String[]{
                            " " + (++i),
                            " " + MySequentialGraphSysUtil.getNodeName(n),
                            " " + MyMathUtil.getCommaSeperatedNumber(nodeContributionByDateMap.get(n))
                    });
                }

                optionTable.revalidate();
                optionTable.repaint();
            }
        }
    }
}