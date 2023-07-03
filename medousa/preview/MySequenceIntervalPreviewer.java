package medousa.preview;

import medousa.MyProgressBar;
import medousa.direct.utils.MyDirectGraphMathUtil;
import medousa.direct.utils.MyDirectGraphVars;
import medousa.message.MyMessageUtil;
import medousa.table.MyTableCellRenderer;
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
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MySequenceIntervalPreviewer
extends JPanel
implements ActionListener {

    private JTable dataTbl;
    private String [] columns;
    private JComboBox variable1;
    private JComboBox variable2;
    private JComboBox variable3;
    private JComboBox variable4;
    private JComboBox value1;
    private JComboBox value2;
    private JComboBox valueType;
    private TableRowSorter sorter;
    private JButton showBtn = new JButton("SHOW");
    private JTextField searchTxt;
    private JComboBox searchColumnSelecter;
    private float minValue = 100000000000f;
    private float minFreq = 10000000000f;
    private float maxValue;
    private float maxFreq;
    private float avg;
    private float std;
    private JComboBox timeOptions;
    private JPanel variable2Panel = new JPanel();
    private LinkedHashMap<Long, Integer> valueWithTargetValueMap;
    private LinkedHashMap<String, Integer> countWithNoSourceValueMap;
    private LinkedHashMap<String, Integer> countWithNoTargetValueMap;

    public MySequenceIntervalPreviewer() {}

    public void decorate(String [] columns, JTable dataTable) {
        try {
            removeAll();
            setLayout(new BorderLayout(1,1));

            if (dataTbl == null) {
                dataTbl = dataTable;
                this.columns = columns;
            }

            variable1 = new JComboBox();
            variable1.addItem("");
            for (int i=1; i < columns.length; i++) {
                variable1.addItem(columns[i]);
            }
            variable1.setBackground(Color.WHITE);
            variable1.setFont(MyDirectGraphVars.tahomaPlainFont11);
            variable1.setFocusable(false);
            variable1.addActionListener(this);

            JLabel valueTypeLabel = new JLabel("INTERVAL TYPE");
            valueTypeLabel.setFont(MyDirectGraphVars.tahomaPlainFont12);

            valueType = new JComboBox();
            valueType.addItem("COUNT");
            valueType.addItem("TIME");
            valueType.setBackground(Color.WHITE);
            valueType.setFont(MyDirectGraphVars.tahomaPlainFont11);
            valueType.setFocusable(false);
            valueType.addActionListener(this);

            JPanel valueTypePanel = new JPanel();
            valueTypePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 1,1));
            valueTypePanel.add(valueTypeLabel);
            valueTypePanel.add(valueType);

            JLabel value1Label = new JLabel("XV1:");
            value1Label.setToolTipText("Set the first event value.");
            value1Label.setFont(MyDirectGraphVars.tahomaPlainFont12);

            value1 = new JComboBox();
            value1.setBackground(Color.WHITE);
            value1.setFont(MyDirectGraphVars.tahomaPlainFont11);
            value1.setFocusable(false);

            JPanel value1Panel = new JPanel();
            value1Panel.setLayout(new FlowLayout(FlowLayout.LEFT, 1,1));
            value1Panel.add(value1Label);
            value1Panel.add(value1);

            JLabel variable1Label = new JLabel("W:");
            variable1Label.setToolTipText("Set the event column.");
            variable1Label.setFont(MyDirectGraphVars.tahomaPlainFont12);

            JPanel variable1Panel = new JPanel();
            variable1Panel.setLayout(new FlowLayout(FlowLayout.LEFT, 1,1));
            variable1Panel.add(variable1Label);
            variable1Panel.add(variable1);

            JLabel variable2Label = new JLabel("X:");
            variable2Label.setToolTipText("Set a product or item id column for the corresponding event.");
            variable2Label.setFont(MyDirectGraphVars.tahomaPlainFont12);

            variable2 = new JComboBox();
            variable2.addItem("");
            for (int i=1; i < columns.length; i++) {
                variable2.addItem(columns[i]);
            }
            variable2.setBackground(Color.WHITE);
            variable2.setFont(MyDirectGraphVars.tahomaPlainFont11);
            variable2.setFocusable(false);
            variable2.addActionListener(this);

            variable2Panel.setLayout(new FlowLayout(FlowLayout.LEFT, 1,1));
            variable2Panel.add(variable2Label);
            variable2Panel.add(variable2);

            JLabel variable3Label = new JLabel("Y:");
            variable3Label.setToolTipText("Set a datetime column for the corresponding event.");
            variable3Label.setFont(MyDirectGraphVars.tahomaPlainFont12);
            variable3Label.setVisible(false);

            variable3 = new JComboBox();
            variable3.addItem("");
            for (int i=1; i < columns.length; i++) {
                variable3.addItem(columns[i]);
            }
            variable3.setBackground(Color.WHITE);
            variable3.setFont(MyDirectGraphVars.tahomaPlainFont11);
            variable3.setFocusable(false);
            variable3.addActionListener(this);

            JPanel variable3Panel = new JPanel();
            variable3Panel.setLayout(new FlowLayout(FlowLayout.LEFT, 1,1));
            variable3Panel.add(variable3Label);
            variable3Panel.add(variable3);

            JLabel variable4Label = new JLabel("Z:");
            variable4Label.setToolTipText("Set the event owner id(Ex.: a session, user, or object id.)");
            variable4Label.setFont(MyDirectGraphVars.tahomaPlainFont12);

            variable4 = new JComboBox();
            variable4.addItem("");
            for (int i=1; i < columns.length; i++) {
                variable4.addItem(columns[i]);
            }
            variable4.setBackground(Color.WHITE);
            variable4.setFont(MyDirectGraphVars.tahomaPlainFont11);
            variable4.setFocusable(false);
            variable4.addActionListener(this);

            JPanel variable4Panel = new JPanel();
            variable4Panel.setLayout(new FlowLayout(FlowLayout.LEFT, 1,1));
            variable4Panel.add(variable4Label);
            variable4Panel.add(variable4);

            JLabel value2Label = new JLabel("XV2:");
            value2Label.setToolTipText("Set the second event value.");
            value2Label.setFont(MyDirectGraphVars.tahomaPlainFont12);

            value2 = new JComboBox();
            value2.setBackground(Color.WHITE);
            value2.setFont(MyDirectGraphVars.tahomaPlainFont11);
            value2.setFocusable(false);

            JPanel value2Panel = new JPanel();
            value2Panel.setLayout(new FlowLayout(FlowLayout.LEFT, 1,1));
            value2Panel.add(value2Label);
            value2Panel.add(value2);

            showBtn = new JButton("SHOW");
            showBtn.setFont(MyDirectGraphVars.tahomaPlainFont12);
            showBtn.setFocusable(false);
            showBtn.setBackground(Color.WHITE);
            showBtn.addActionListener(this);

            JPanel controlPanel = new JPanel();
            controlPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 1,1));
            controlPanel.add(valueTypePanel);
            controlPanel.add(variable1Panel);
            controlPanel.add(value1Panel);
            controlPanel.add(value2Panel);
            controlPanel.add(variable2Panel);
            controlPanel.add(variable3Panel);
            controlPanel.add(variable4Panel);
            controlPanel.add(showBtn);

            TitledBorder border = BorderFactory.createTitledBorder("SEQUENCE INTERVAL PREVIEW");
            border.setTitleFont(MyDirectGraphVars.tahomaBoldFont12);
            setBorder(BorderFactory.createTitledBorder(border, "", TitledBorder.LEFT, TitledBorder.TOP));

            add(controlPanel, BorderLayout.CENTER);
        } catch (Exception ex) {
           // ex.printStackTrace();
        }
    }

    private JSplitPane setResultTablePanel(JTable table) {
        String [] propertyColumns = {"PROPERTY", "VALUE"};
        String [][] propertyData = {};

        DefaultTableModel propertyModel = new DefaultTableModel(propertyData, propertyColumns);
        JTable propertyTable = new JTable(propertyModel);
        propertyTable.setRowHeight(24);
        propertyTable.setFont(MyDirectGraphVars.f_pln_12);
        propertyTable.setBackground(Color.WHITE);
        propertyTable.setFocusable(false);
        propertyTable.getTableHeader().setBackground(new Color(0,0,0,0));
        propertyTable.getTableHeader().setOpaque(false);
        propertyTable.getTableHeader().setFont(MyDirectGraphVars.tahomaBoldFont12);
        propertyModel.addRow(new String[]{"NO TARGET", MyDirectGraphMathUtil.getCommaSeperatedNumber(countWithNoTargetValueMap.size())});
        propertyModel.addRow(new String[]{"NO SOURCE", MyDirectGraphMathUtil.getCommaSeperatedNumber(countWithNoSourceValueMap.size())});
        propertyModel.addRow(new String[]{"MAX.", MyDirectGraphMathUtil.getCommaSeperatedNumber((long) Float.parseFloat(String.valueOf(maxFreq)))});
        propertyModel.addRow(new String[]{"MIN.", MyDirectGraphMathUtil.getCommaSeperatedNumber((long) Float.parseFloat(String.valueOf(minFreq)))});

        String [] columns = {"NO.", "VALUE", "FREQ.", "MAX. R.", "R."};
        String [][] data = {};

        DefaultTableModel model = new DefaultTableModel(data, columns);
        JTable distributionTable = new JTable(model);
        distributionTable.setRowHeight(24);
        distributionTable.setFont(MyDirectGraphVars.f_pln_12);
        distributionTable.getTableHeader().getColumnModel().getColumn(0).setPreferredWidth(55);
        distributionTable.getTableHeader().getColumnModel().getColumn(1).setPreferredWidth(120);
        distributionTable.getTableHeader().getColumnModel().getColumn(2).setPreferredWidth(75);
        distributionTable.getTableHeader().getColumnModel().getColumn(3).setPreferredWidth(80);
        distributionTable.getTableHeader().getColumnModel().getColumn(4).setPreferredWidth(80);
        distributionTable.setBackground(Color.WHITE);
        distributionTable.setFocusable(false);
        distributionTable.getTableHeader().setBackground(new Color(0,0,0,0));
        distributionTable.getTableHeader().setOpaque(false);
        distributionTable.getTableHeader().setFont(MyDirectGraphVars.tahomaBoldFont12);
        distributionTable.getColumnModel().getColumn(3).setCellRenderer(new MyTableCellRenderer());
        sorter = new TableRowSorter<>(distributionTable.getModel());
        distributionTable.setRowSorter(sorter);

        int i = 0;
        for (long key : valueWithTargetValueMap.keySet()) {
            model.addRow(new String[]{"" + (++i),
                MyDirectGraphMathUtil.getCommaSeperatedNumber(key),
                MyDirectGraphMathUtil.getCommaSeperatedNumber(valueWithTargetValueMap.get(key)),
                MyDirectGraphMathUtil.threeDecimalFormat((double) valueWithTargetValueMap.get(key)/maxFreq),
                MyDirectGraphMathUtil.threeDecimalPercent((double) valueWithTargetValueMap.get(key)/table.getRowCount())
            });
        }

        JButton searchBtn = new JButton("SEARCH");
        searchBtn.setFocusable(false);
        searchTxt.setFont(MyDirectGraphVars.f_pln_12);
        searchTxt.setBorder(BorderFactory.createEtchedBorder());
        searchTxt.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char keyChar = e.getKeyChar();
                e.setKeyChar(Character.toUpperCase(keyChar));
            }
        });

        JPanel dataSearchPanel = new JPanel();
        dataSearchPanel.setLayout(new BorderLayout(1,1));
        dataSearchPanel.add(searchTxt, BorderLayout.CENTER);
        dataSearchPanel.add(searchBtn, BorderLayout.EAST);
        searchBtn.addActionListener(this::searchButtonActionPerformed);

        JPanel distributionTableDataSearchPanel = new JPanel();
        distributionTableDataSearchPanel.setLayout(new BorderLayout(1,1));
        distributionTableDataSearchPanel.add(new JScrollPane(distributionTable), BorderLayout.CENTER);
        distributionTableDataSearchPanel.add(dataSearchPanel, BorderLayout.SOUTH);

        JSplitPane tableSplitPane = new JSplitPane();
        tableSplitPane.setDividerLocation(0.2);
        tableSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        tableSplitPane.setTopComponent(new JScrollPane(propertyTable));
        tableSplitPane.setBottomComponent(distributionTableDataSearchPanel);
        tableSplitPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                tableSplitPane.setDividerLocation(0.2);
            }
        });

        return tableSplitPane;
    }

    public void setTable() {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            if (variable4.getSelectedIndex() == 0) {
                MyMessageUtil.showInfoMsg("Varible Z must be selected.");
                return;
            }

            if (valueType.getSelectedIndex() == 1) {

            } else if (valueType.getSelectedIndex() == 1 && variable3.getSelectedIndex() == 0) {
                MyMessageUtil.showInfoMsg("Variable Y must be selected for Time Value Type.");
                return;
            }

            java.util.List<String> columns = new ArrayList<>();
            columns.add(variable1.getSelectedItem().toString());
            columns.add(variable2.getSelectedItem().toString());

            if (variable3.getSelectedItem().toString().length() > 0) {
                columns.add(variable3.getSelectedItem().toString());
            }

            if (variable4.getSelectedItem().toString().length() > 0) {
                columns.add(variable4.getSelectedItem().toString());
            }

            String[][] data = {};
            int pbCnt = 0;

            DefaultTableModel model = new DefaultTableModel(data, columns.toArray());
            int row = dataTbl.getRowCount();
            while (row > 0) {
                String variable1Value = dataTbl.getValueAt(row - 1, variable1.getSelectedIndex()).toString();
                String variable2Value = dataTbl.getValueAt(row - 1, variable2.getSelectedIndex()).toString();
                String variable3Value = "";
                String variable4Value = "";

                if (variable3.getSelectedIndex() > 0) {
                    variable3Value = dataTbl.getValueAt(row - 1, variable3.getSelectedIndex()).toString();
                }

                if (variable4.getSelectedIndex() > 0) {
                    variable4Value = dataTbl.getValueAt(row - 1, variable4.getSelectedIndex()).toString();
                }

                if (value1.getSelectedItem().toString().equals(variable1Value) ||
                    value2.getSelectedItem().toString().equals(variable1Value)) {
                    if (value1.getSelectedItem().toString().equals(variable1Value)) {
                        variable1Value = "1";
                    } else {
                        variable1Value = "2";
                    }
                    if (variable3Value.length() == 0 && variable4Value.length() > 0) {
                        model.addRow(new String[]{variable1Value, variable2Value, variable4Value});
                    } else {
                        model.addRow(new String[]{variable1Value, variable2Value, variable3Value, variable4Value});
                    }
                }
                row--;
                pb.updateValue(++pbCnt, dataTbl.getRowCount());
            }
            JTable table = new JTable(model);
            table.setBackground(Color.WHITE);
            table.setFont(MyDirectGraphVars.tahomaPlainFont12);
            table.setRowHeight(24);
            table.setFocusable(false);
            table.getTableHeader().setOpaque(false);
            table.getTableHeader().setBackground(new Color(0, 0, 0, 0));
            table.getTableHeader().setFont(MyDirectGraphVars.tahomaBoldFont12);
            this.sorter = new TableRowSorter<>(table.getModel());
            table.setRowSorter(this.sorter);

            if (valueType.getSelectedIndex() == 0) {
                sortTableForCountInterval(table);
            }

            JButton searchBtn = new JButton("SEARCH");
            searchBtn.setFocusable(false);
            searchBtn.addActionListener(this::searchButtonActionPerformed);

            searchColumnSelecter = new JComboBox();
            searchColumnSelecter.setFont(MyDirectGraphVars.tahomaPlainFont12);
            searchColumnSelecter.setFocusable(false);
            searchColumnSelecter.setBackground(Color.WHITE);
            searchColumnSelecter.addItem("");
            for (String column : columns) {
                searchColumnSelecter.addItem(column);
            }

            searchTxt = new JTextField();
            searchTxt.setFont(MyDirectGraphVars.f_pln_12);
            searchTxt.setBorder(BorderFactory.createEtchedBorder());
            searchTxt.addKeyListener(new KeyAdapter() {
                public void keyTyped(KeyEvent e) {
                    char keyChar = e.getKeyChar();
                    e.setKeyChar(Character.toUpperCase(keyChar));
                }
            });
            JPanel dataSearchPanel = new JPanel();
            dataSearchPanel.setLayout(new BorderLayout(1, 1));
            dataSearchPanel.add(searchColumnSelecter, BorderLayout.WEST);
            dataSearchPanel.add(searchTxt, BorderLayout.CENTER);
            dataSearchPanel.add(searchBtn, BorderLayout.EAST);

            JPanel dataTableSearchPanel = new JPanel();
            dataTableSearchPanel.setLayout(new BorderLayout(1, 1));
            dataTableSearchPanel.add(new JScrollPane(table), BorderLayout.CENTER);
            dataTableSearchPanel.add(dataSearchPanel, BorderLayout.SOUTH);

            pb.updateValue(100, 100);
            pb.dispose();
            setData(table);

            JPanel chartControlPanel = new JPanel();
            chartControlPanel.setLayout(new BorderLayout(1,1));

            JLabel timeOptionLabel = new JLabel();
            timeOptionLabel.setFont(MyDirectGraphVars.tahomaPlainFont12);
            timeOptionLabel.setText("TIME: ");

            timeOptions = new JComboBox();
            timeOptions.addItem("SEC.");
            timeOptions.addItem("MIN.");
            timeOptions.addItem("HR.");
            timeOptions.addItem("DAY");
            timeOptions.setBackground(Color.WHITE);
            timeOptions.setFont(MyDirectGraphVars.tahomaPlainFont12);
            timeOptions.setFocusable(false);

            JPanel timeOptionPanel = new JPanel();
            timeOptionPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 1,1));
            timeOptionPanel.add(timeOptionLabel);
            timeOptionPanel.add(timeOptions);

            chartControlPanel.add(setChart(), BorderLayout.CENTER);
            chartControlPanel.add(timeOptionPanel, BorderLayout.NORTH);

            JSplitPane chatPropertyChartSplitPane = new JSplitPane();
            chatPropertyChartSplitPane.setDividerLocation(0.18);
            chatPropertyChartSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
            chatPropertyChartSplitPane.setRightComponent(chartControlPanel);
            chatPropertyChartSplitPane.setLeftComponent(setResultTablePanel(table));
            chatPropertyChartSplitPane.addComponentListener(new ComponentAdapter() {
                @Override public void componentResized(ComponentEvent e) {
                    super.componentResized(e);
                    chatPropertyChartSplitPane.setDividerLocation(0.18);
                }
            });

            JSplitPane dataTableChartSplitPane = new JSplitPane();
            dataTableChartSplitPane.setDividerLocation(0.65);
            dataTableChartSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
            dataTableChartSplitPane.setTopComponent(chatPropertyChartSplitPane);
            dataTableChartSplitPane.setBottomComponent(dataTableSearchPanel);
            dataTableChartSplitPane.addComponentListener(new ComponentAdapter() {
                @Override public void componentResized(ComponentEvent e) {
                    super.componentResized(e);
                    dataTableChartSplitPane.setDividerLocation(0.65);
                }
            });

            JFrame f = new JFrame();
            f.setLayout(new BorderLayout(1, 1));
            f.getContentPane().add(dataTableChartSplitPane, BorderLayout.CENTER);
            f.pack();
            f.setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            pb.updateValue(100, 100);
            pb.dispose();
        }
    }

    private ChartPanel setChart() {
        String plotTitle = "";
        String xaxis = "";
        String yaxis = "";
        PlotOrientation orientation = PlotOrientation.VERTICAL;
        boolean show = false;
        boolean toolTips = true;
        boolean urls = false;

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Long key : valueWithTargetValueMap.keySet()) {
            if (maxFreq < valueWithTargetValueMap.get(key)) {
                maxFreq = valueWithTargetValueMap.get(key);
            }

            if (minFreq > valueWithTargetValueMap.get(key)) {
                minFreq = valueWithTargetValueMap.get(key);
            }

            dataset.addValue(valueWithTargetValueMap.get(key), "", key);
        }

        JFreeChart chart = ChartFactory.createBarChart(plotTitle, xaxis, yaxis, dataset, orientation, show, toolTips, urls);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.getChart().getCategoryPlot().setRangeGridlinePaint(Color.DARK_GRAY);
        chartPanel.getChart().getCategoryPlot().setDomainGridlinePaint(Color.DARK_GRAY);
        chartPanel.getChart().getCategoryPlot().setBackgroundPaint(Color.WHITE);//setBackgroundPaint(new Color(0,0,0,0.05f));
        chartPanel.getChart().getCategoryPlot().getDomainAxis().setTickLabelFont(MyDirectGraphVars.f_pln_11);
        chartPanel.getChart().getCategoryPlot().getDomainAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
        chartPanel.getChart().getCategoryPlot().getRangeAxis().setTickLabelFont(MyDirectGraphVars.f_pln_10);
        chartPanel.getChart().getCategoryPlot().getRangeAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));

        CategoryAxis domainAxis = chartPanel.getChart().getCategoryPlot().getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);

        BarRenderer barRenderer = (BarRenderer) chartPanel.getChart().getCategoryPlot().getRenderer();
        barRenderer.setSeriesPaint(0, Color.decode("#489EE0"));//new Color(0, 0, 0, 0.3f));
        barRenderer.setShadowPaint(Color.WHITE);
        barRenderer.setBaseFillPaint(Color.decode("#07CF61"));
        barRenderer.setBarPainter(new StandardBarPainter());
        barRenderer.setBaseLegendTextFont(MyDirectGraphVars.tahomaPlainFont10);

        chartPanel.getChart().removeLegend();
        //chartPanel.setBorder(BorderFactory.createLoweredBevelBorder());

        return chartPanel;
    }

    private void sortTableForCountInterval(JTable table) {
        Object[][] data = new Object[table.getModel().getRowCount()][table.getModel().getColumnCount()];

        for (int i = 0; i < table.getModel().getRowCount(); i++) {
            for (int j = 0; j < table.getModel().getColumnCount(); j++) {
                data[i][j] = table.getModel().getValueAt(i, j);
            }
        }

        Arrays.sort(data, new MyMultipleColumnComparatorForCountInterval());

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                table.getModel().setValueAt(data[i][j], i, j);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        new Thread(new Runnable() {
            @Override public void run() {
                if (e.getSource() == variable1) {
                    MyProgressBar pb = new MyProgressBar(false);
                    try {
                        if (variable1.getSelectedIndex() == 0) return;
                        Set<String> columnValues = new HashSet<>();
                        int row = dataTbl.getRowCount();
                        while (row > 0) {
                            String value = dataTbl.getValueAt(row - 1, variable1.getSelectedIndex()).toString();
                            columnValues.add(value);
                            row--;
                        }
                        value1.removeAllItems();
                        value1.addItem("");
                        for (String value : columnValues) {
                            if (value.matches("\\d+(\\.\\d+)?")) {
                                value1.addItem((long) Float.parseFloat(value));
                            } else {
                                value1.addItem(value);
                            }
                        }

                        value2.removeAllItems();
                        value2.addItem("");
                        for (String value : columnValues) {
                            if (value.matches("\\d+(\\.\\d+)?")) {
                                value2.addItem((long) Float.parseFloat(value));
                            } else {
                                value2.addItem(value);
                            }
                        }
                        pb.updateValue(100, 100);
                        pb.dispose();
                    } catch (Exception ex) {
                        pb.updateValue(100, 100);
                        pb.dispose();
                    }
                } else if (e.getSource() == showBtn) {
                    valueWithTargetValueMap = new LinkedHashMap<>();
                    countWithNoSourceValueMap = new LinkedHashMap<>();;
                    countWithNoTargetValueMap = new LinkedHashMap<>();;
                    setTable();
                }
            }
        }).start();
    }

    private void searchButtonActionPerformed(ActionEvent e) {
        try {
            String searchValue = searchTxt.getText();
            searchTable(searchValue);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void searchTable(String searchValue) {
        try {
            if (this.searchColumnSelecter.getSelectedIndex() == 0) {
                RowFilter<TableModel, Object> rowFilter = RowFilter.regexFilter(searchValue);
                this.sorter.setRowFilter(rowFilter);
            } else {
                RowFilter<TableModel, Object> rowFilter = RowFilter.regexFilter(searchValue, this.searchColumnSelecter.getSelectedIndex());
                this.sorter.setRowFilter(rowFilter);
            }
        } catch (Exception ex) {

        }
    }

    private void setData(JTable table) {
        try {
            int row = 0;
            while (row < table.getRowCount()) {
                String outerVariable1Value = table.getValueAt(row, 0).toString();
                String outerVariable2Value = table.getValueAt(row, 1).toString();
                String outerVariable3Value = table.getValueAt(row, 2).toString();
                String outerVariable4Value = table.getValueAt(row, 3).toString();

                if (outerVariable1Value.equals("2")) {
                    if (countWithNoSourceValueMap.containsKey(outerVariable4Value)) {
                        countWithNoSourceValueMap.put(outerVariable4Value, countWithNoSourceValueMap.get(outerVariable4Value) + 1);
                    } else {
                        countWithNoSourceValueMap.put(outerVariable4Value, 1);
                    }
                } else if (++row < table.getRowCount()) {
                    long hopCount = 0;
                    boolean isEndedWithSecondValue = false;
                    String innerVariable1Value = "";
                    String innerVariable2Value = "";
                    String innerVariable3Value = "";
                    String innerVariable4Value = "";
                    do {
                        innerVariable1Value = table.getValueAt(row, 0).toString();
                        innerVariable2Value = table.getValueAt(row, 1).toString();
                        innerVariable3Value = table.getValueAt(row, 2).toString();
                        innerVariable4Value = table.getValueAt(row, 3).toString();

                        hopCount++;
                        if (innerVariable1Value.equals("2")) {
                            isEndedWithSecondValue = true;
                            break;
                        }
                    } while (++row < table.getRowCount() && innerVariable2Value.equals(outerVariable2Value) && innerVariable4Value.equals(outerVariable4Value));

                    if (valueType.getSelectedIndex() == 0) {
                        if (isEndedWithSecondValue) {
                            if (valueWithTargetValueMap.containsKey(hopCount)) {
                                valueWithTargetValueMap.put(hopCount, valueWithTargetValueMap.get(hopCount) + 1);
                            } else {
                                valueWithTargetValueMap.put(hopCount, 1);
                            }
                        } else {
                            if (countWithNoTargetValueMap.containsKey(outerVariable4Value)) {
                                countWithNoTargetValueMap.put(outerVariable4Value, countWithNoTargetValueMap.get(outerVariable4Value) + 1);
                            } else {
                                countWithNoTargetValueMap.put(outerVariable4Value, 1);
                            }
                        }
                    } else {//Time
                        if (isEndedWithSecondValue) {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss");
                            LocalDateTime dateTime1 = LocalDateTime.parse(outerVariable3Value, formatter);
                            LocalDateTime dateTime2 = LocalDateTime.parse(innerVariable3Value, formatter);
                            long duration = Math.abs((Duration.between(dateTime1, dateTime2)).getSeconds());

                            if (valueWithTargetValueMap.containsKey(duration)) {
                                valueWithTargetValueMap.put(duration, valueWithTargetValueMap.get(duration) + 1);
                            } else {
                                valueWithTargetValueMap.put(duration, 1);
                            }
                        } else {
                            if (countWithNoTargetValueMap.containsKey(outerVariable4Value)) {
                                countWithNoTargetValueMap.put(outerVariable4Value, countWithNoTargetValueMap.get(outerVariable4Value) + 1);
                            } else {
                                countWithNoTargetValueMap.put(outerVariable4Value, 1);
                            }
                        }
                    }
                    row--;
                }
                row++;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public float getStandardDeviation(LinkedHashMap<Long, Long> valueMap) {
        float sum = 0.00f;
        int data = 0;
        for (long n : valueMap.keySet()) {
            for (int i=0; i < n*valueMap.get(n); i++) {
                sum += n;
                data++;
            }
        }
        double mean = sum / data;
        sum = 0f;
        for (long n : valueMap.keySet()) {
            sum += Math.pow(n - mean, 2);
        }
        return (sum == 0.00f ? 0.00f : (float) Math.sqrt(sum / data));
    }

}
