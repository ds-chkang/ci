package medousa.preview;

import medousa.MyProgressBar;
import medousa.direct.utils.MyDirectGraphMathUtil;
import medousa.direct.utils.MyDirectGraphSysUtil;
import medousa.direct.utils.MyDirectGraphVars;
import medousa.message.MyMessageUtil;
import medousa.table.MyTableCellBarChartRenderer;
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

import javax.media.j3d.Link;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyPivotPreview
extends JPanel
implements ActionListener {

    private JTable dataTbl;
    private String [] columns;
    private JComboBox xVariable;
    private JComboBox yVariable;
    private JButton searchValueBtn;

    private JComboBox xValue;
    private TableRowSorter sorter;
    private JButton showBtn = new JButton("SHOW");
    private JTextField searchTxt = new JTextField();
    private JComboBox searchColumnSelecter;
    private float minValue = 100000000000f;
    private float minFreq = 10000000000f;
    private float maxValue;
    private float maxFreq;
    private float avg;
    private float std;

    public MyPivotPreview() {}

    public void decorate(String [] columns, JTable dataTable) {
        try {
            removeAll();
            setLayout(new BorderLayout(1,1));

            if (this.dataTbl == null) {
                this.dataTbl = dataTable;
                this.columns = columns;
            }

            searchValueBtn = new JButton("S. V.");
            searchValueBtn.setToolTipText("SEARCH VALUE");
            searchValueBtn.setBackground(Color.WHITE);
            searchValueBtn.setFocusable(false);
            searchValueBtn.setFont(MyDirectGraphVars.tahomaPlainFont12);
            searchValueBtn.addActionListener(this);

            xVariable = new JComboBox();
            xVariable.addItem("");
            for (int i=1; i < columns.length; i++) {
                xVariable.addItem(columns[i]);
            }
            xVariable.setBackground(Color.WHITE);
            xVariable.setFont(MyDirectGraphVars.tahomaPlainFont11);
            xVariable.setFocusable(false);
            xVariable.addActionListener(this);

            xValue = new JComboBox();
            xValue.setBackground(Color.WHITE);
            xValue.setFont(MyDirectGraphVars.tahomaPlainFont11);
            xValue.setFocusable(false);

            yVariable = new JComboBox();
            yVariable.addItem("");
            for (int i=1; i < columns.length; i++) {
                yVariable.addItem(columns[i]);
            }
            yVariable.setBackground(Color.WHITE);
            yVariable.setFont(MyDirectGraphVars.tahomaPlainFont11);
            yVariable.setFocusable(false);

            JLabel variableLabel1 = new JLabel("X:");
            variableLabel1.setFont(MyDirectGraphVars.tahomaPlainFont12);

            JLabel variableLabel2 = new JLabel("Y:");
            variableLabel2.setFont(MyDirectGraphVars.tahomaPlainFont12);

            showBtn = new JButton("SHOW");
            showBtn.setFont(MyDirectGraphVars.tahomaPlainFont12);
            showBtn.setFocusable(false);
            showBtn.setBackground(Color.WHITE);
            showBtn.addActionListener(this);

            JLabel variable1EmptyLabel = new JLabel("  ");
            variable1EmptyLabel.setFont(MyDirectGraphVars.tahomaPlainFont12);

            JPanel variable1Panel = new JPanel();
            variable1Panel.setLayout(new FlowLayout(FlowLayout.LEFT, 1,1));
            variable1Panel.add(variableLabel1);
            variable1Panel.add(xVariable);
            variable1Panel.add(variable1EmptyLabel);

            JPanel variable2Panel = new JPanel();
            variable2Panel.setLayout(new FlowLayout(FlowLayout.LEFT, 1,1));
            variable2Panel.add(variableLabel2);
            variable2Panel.add(yVariable);

            JLabel pivotValueLabel = new JLabel("XV:");
            pivotValueLabel.setToolTipText("Select a value of variable X.");
            pivotValueLabel.setFont(MyDirectGraphVars.tahomaPlainFont12);

            JPanel valuePanel = new JPanel();
            valuePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 1,1));
            valuePanel.add(pivotValueLabel);
            valuePanel.add(xValue);

            JPanel controlPanel = new JPanel();
            controlPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 1,1));
            controlPanel.add(variable1Panel);
            controlPanel.add(valuePanel);
            controlPanel.add(searchValueBtn);
            controlPanel.add(variable2Panel);
            controlPanel.add(showBtn);

            TitledBorder border = BorderFactory.createTitledBorder("PIVOT PREVIEW");
            border.setTitleFont(MyDirectGraphVars.tahomaBoldFont12);
            setBorder(BorderFactory.createTitledBorder(border, "", TitledBorder.LEFT, TitledBorder.TOP));

            add(controlPanel, BorderLayout.CENTER);
        } catch (Exception ex) {
           // ex.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final MyPivotPreview pivotPreview = this;
        new Thread(new Runnable() {
            @Override public void run() {
                if (e.getSource() == searchValueBtn) {
                    if (xVariable.getSelectedIndex() == 0) return;
                    JFrame f = new JFrame("SEARCH VALUE");

                    String [] columns = {"NO.", xVariable.getSelectedItem().toString()};
                    String [][] data = {};
                    DefaultTableModel model = new DefaultTableModel(data, columns);
                    Set<String> values = new HashSet<>();
                    int row = 0;
                    int i = 0;
                    while (row < dataTbl.getRowCount()) {
                        String value = dataTbl.getValueAt(row, xVariable.getSelectedIndex()).toString();
                        if (!values.contains(value)) {
                            model.addRow(new String[]{"" + (++i), value.toUpperCase()});
                            values.add(value);
                        }
                        row++;
                    }

                    JTable table = new JTable(model);
                    table.setFont(MyDirectGraphVars.tahomaPlainFont12);
                    table.setBackground(Color.WHITE);
                    table.setFocusable(false);
                    table.setRowHeight(24);
                    table.getColumnModel().getColumn(0).setPreferredWidth(70);
                    table.getColumnModel().getColumn(0).setMaxWidth(70);
                    table.getColumnModel().getColumn(1).setPreferredWidth(140);
                    table.getTableHeader().setFont(MyDirectGraphVars.tahomaBoldFont12);
                    table.getTableHeader().setOpaque(false);
                    table.getTableHeader().setBackground(new Color(0,0,0,0));
                    table.addComponentListener(new ComponentAdapter() {
                        @Override
                        public void componentResized(ComponentEvent e) {
                            super.componentResized(e);
                            table.getColumnModel().getColumn(0).setPreferredWidth(70);
                            table.getColumnModel().getColumn(0).setMaxWidth(70);
                            table.getColumnModel().getColumn(1).setPreferredWidth(140);
                        }
                    });

                    JButton selectBtn = new JButton("SELECT");
                    selectBtn.setFocusable(false);
                    selectBtn.setBackground(Color.WHITE);
                    selectBtn.addActionListener(new ActionListener() {
                        @Override public void actionPerformed(ActionEvent e) {
                            String selectedValue = table.getValueAt(table.getSelectedRow(), 1).toString();
                            for (int i=1; i < xValue.getItemCount(); i++) {
                                if (xValue.getItemAt(i).toString().equals(selectedValue)) {
                                    xValue.setSelectedIndex(i);
                                    break;
                                }
                            }
                            f.dispose();
                        }
                    });

                    JTextField searchTxt = new JTextField();
                    searchTxt.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
                    JPanel searchPanel = MyTableUtil.searchTablePanel(pivotPreview, searchTxt, selectBtn, model, table);

                    f.setLayout(new BorderLayout(1,1));
                    f.getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);
                    f.getContentPane().add(searchPanel, BorderLayout.SOUTH);
                    f.pack();
                    f.setPreferredSize(new Dimension(120, 500));
                    f.setVisible(true);
                } else if (e.getSource() == xVariable) {
                    MyProgressBar pb = new MyProgressBar(false);
                    try {
                        if (xVariable.getSelectedIndex() == 0) return;
                        Set<String> columnValues = new HashSet<>();
                        int row = 0;
                        while (row < dataTbl.getRowCount()) {
                            columnValues.add(dataTbl.getValueAt(row, xVariable.getSelectedIndex()).toString());
                            row++;
                        }
                        xValue.removeAllItems();
                        xValue.addItem("");
                        for (String columnValue : columnValues) {
                            if (columnValue.matches("-?\\d+(\\.\\d+)?")) {
                                xValue.addItem((long) Float.parseFloat(columnValue));
                            } else {
                                xValue.addItem(columnValue);
                            }
                        }
                        pb.updateValue(100, 100);
                        pb.dispose();
                    } catch (Exception ex) {
                        pb.updateValue(100, 100);
                        pb.dispose();
                    }
                } else if (e.getSource() == showBtn) {
                    if (xVariable.getSelectedItem().toString().equals(yVariable.getSelectedItem().toString())) {
                        MyMessageUtil.showInfoMsg("Choose different variables.");
                    } else if (yVariable.getSelectedIndex() == 0) {
                        MyMessageUtil.showInfoMsg("Select y variable.");
                    } else if (xValue.getSelectedIndex() > 0) {
                        searchColumnSelecter = new JComboBox();
                        searchColumnSelecter.setFont(MyDirectGraphVars.tahomaPlainFont12);
                        searchColumnSelecter.setFocusable(false);
                        searchColumnSelecter.setBackground(Color.WHITE);
                        searchColumnSelecter.addItem("");
                        for (int i = 1; i < columns.length; i++) {
                            searchColumnSelecter.addItem(columns[i]);
                        }

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
                        distributionTable.getColumnModel().getColumn(3).setCellRenderer(new MyTableCellBarChartRenderer());
                        sorter = new TableRowSorter<>(distributionTable.getModel());
                        distributionTable.setRowSorter(sorter);

                        JSplitPane tableSplitPane = new JSplitPane();
                        tableSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
                        tableSplitPane.setTopComponent(new JScrollPane(propertyTable));
                        tableSplitPane.setBottomComponent(new JScrollPane(distributionTable));
                        tableSplitPane.setOneTouchExpandable(false);
                        tableSplitPane.setDividerLocation(0.17);
                        tableSplitPane.addComponentListener(new ComponentAdapter() {
                            @Override public void componentResized(ComponentEvent e) {
                            super.componentResized(e);
                            tableSplitPane.setDividerLocation(0.17);
                            }
                        });

                        ChartPanel chartPanel = new ChartPanel(setChart(distributionTable, propertyTable));
                        chartPanel.getChart().getCategoryPlot().setRangeGridlinePaint(Color.DARK_GRAY);
                        chartPanel.getChart().getCategoryPlot().setDomainGridlinePaint(Color.DARK_GRAY);
                        chartPanel.getChart().getCategoryPlot().setBackgroundPaint(Color.WHITE);//setBackgroundPaint(new Color(0,0,0,0.05f));
                        chartPanel.getChart().getCategoryPlot().getDomainAxis().setTickLabelFont(new Font("Arial", Font.PLAIN, 0));
                        chartPanel.getChart().getCategoryPlot().getDomainAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
                        chartPanel.getChart().getCategoryPlot().getRangeAxis().setTickLabelFont(MyDirectGraphVars.f_pln_10);
                        chartPanel.getChart().getCategoryPlot().getRangeAxis().setLabelFont(MyDirectGraphVars.f_pln_8);

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
                        searchBtn.addActionListener(pivotPreview::searchButtonActionPerformed);

                        JSplitPane splitPane = new JSplitPane();
                        splitPane.setLeftComponent(tableSplitPane);
                        splitPane.setRightComponent(chartPanel);
                        splitPane.setDividerLocation(0.20);
                        splitPane.addComponentListener(new ComponentAdapter() {
                            @Override
                            public void componentResized(ComponentEvent e) {
                            super.componentResized(e);
                            splitPane.setDividerLocation(0.20);
                            }
                        });

                        JFrame f = new JFrame("PIVOT PREVIEW");
                        f.setLayout(new BorderLayout(3,3));
                        f.getContentPane().add(splitPane, BorderLayout.CENTER);
                        f.pack();
                        f.setVisible(true);
                    } else { // For xValue is empty.
                        searchColumnSelecter = new JComboBox();
                        searchColumnSelecter.setFont(MyDirectGraphVars.tahomaPlainFont12);
                        searchColumnSelecter.setFocusable(false);
                        searchColumnSelecter.setBackground(Color.WHITE);
                        searchColumnSelecter.addItem("");
                        for (int i = 1; i < columns.length; i++) {
                            searchColumnSelecter.addItem(columns[i]);
                        }

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
                        distributionTable.getColumnModel().getColumn(3).setCellRenderer(new MyTableCellBarChartRenderer());
                        sorter = new TableRowSorter<>(distributionTable.getModel());
                        distributionTable.setRowSorter(sorter);

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
                        searchBtn.addActionListener(pivotPreview::searchButtonActionPerformed);

                        JPanel tablePanel = new JPanel();
                        tablePanel.setLayout(new BorderLayout(1,1));
                        tablePanel.add(new JScrollPane(distributionTable), BorderLayout.CENTER);
                        tablePanel.add(dataSearchPanel, BorderLayout.SOUTH);

                        JSplitPane tableSplitPane = new JSplitPane();
                        tableSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
                        tableSplitPane.setTopComponent(new JScrollPane(propertyTable));
                        tableSplitPane.setBottomComponent(tablePanel);
                        tableSplitPane.setOneTouchExpandable(false);
                        tableSplitPane.setDividerLocation(0.185);
                        tableSplitPane.addComponentListener(new ComponentAdapter() {
                            @Override public void componentResized(ComponentEvent e) {
                                super.componentResized(e);
                                tableSplitPane.setDividerLocation(0.185);
                            }
                        });

                        ChartPanel chartPanel = new ChartPanel(setChart(distributionTable, propertyTable));
                        chartPanel.getChart().getCategoryPlot().setRangeGridlinePaint(Color.DARK_GRAY);
                        chartPanel.getChart().getCategoryPlot().setDomainGridlinePaint(Color.DARK_GRAY);
                        chartPanel.getChart().getCategoryPlot().setBackgroundPaint(Color.WHITE);//setBackgroundPaint(new Color(0,0,0,0.05f));
                        chartPanel.getChart().getCategoryPlot().getDomainAxis().setTickLabelFont(new Font("Arial", Font.PLAIN, 0));
                        chartPanel.getChart().getCategoryPlot().getDomainAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
                        chartPanel.getChart().getCategoryPlot().getRangeAxis().setTickLabelFont(MyDirectGraphVars.f_pln_10);
                        chartPanel.getChart().getCategoryPlot().getRangeAxis().setLabelFont(MyDirectGraphVars.f_pln_8);

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

                        JSplitPane splitPane = new JSplitPane();
                        splitPane.setLeftComponent(tableSplitPane);
                        splitPane.setRightComponent(chartPanel);
                        splitPane.setDividerLocation(0.20);
                        splitPane.addComponentListener(new ComponentAdapter() {
                            @Override
                            public void componentResized(ComponentEvent e) {
                                super.componentResized(e);
                                splitPane.setDividerLocation(0.20);
                            }
                        });

                        JFrame f = new JFrame("PIVOT PREVIEW");
                        f.setLayout(new BorderLayout(3,3));
                        f.getContentPane().add(splitPane, BorderLayout.CENTER);
                        f.pack();
                        f.setVisible(true);
                    }
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

    private static boolean isDateString(String input) {
        // Define the regular expression pattern for date formats (YYYY-MM-DD)
        String datePattern = "^\\d{4}-\\d{2}-\\d{2}$";
        Pattern pattern = Pattern.compile(datePattern);
        Matcher matcher = pattern.matcher(input);

        return matcher.matches();
    }

    private JFreeChart setChart(JTable distributionTable, JTable propertyTable) {
        int row = 0;
        boolean isCategoryChart = false;
        while (row < dataTbl.getRowCount()) {
            String yValue = dataTbl.getValueAt(0, yVariable.getSelectedIndex()).toString();
            if (!yValue.matches("-?\\d+(\\.\\d+)?")) {
                isCategoryChart = true;
                break;
            } else if (isDateString(yValue.split(" ")[0])) {
                isCategoryChart = true;
                break;
            }
        }

        if (isCategoryChart) {
            if (xValue.getSelectedIndex() == 0) {
                return setCategoryPivotChartWithoutXValue(distributionTable, propertyTable);
            } else {
                return setCategoryPivotChart(distributionTable, propertyTable);
            }
        } else {
            return setNumericPivotChart(distributionTable, propertyTable);
        }
    }

    private JFreeChart setNumericPivotChart(JTable distributionTable, JTable propertyTable) {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            LinkedHashMap<Long, Long> valueMap = new LinkedHashMap<>();
            long totalValue = 0;
            int pivotColumn = xVariable.getSelectedIndex();
            int yColumn = yVariable.getSelectedIndex();
            int row = dataTbl.getRowCount() - 1;
            while (row > -1) {
                if (pivotColumn > 0) {
                    String pivotValue = dataTbl.getValueAt(row, pivotColumn).toString();
                    if (pivotValue.equals(this.xValue.getSelectedItem().toString())) {
                        long yValue = (long) Float.parseFloat(dataTbl.getValueAt(row, yColumn).toString());
                        if (valueMap.containsKey(yValue)) {
                            valueMap.put(yValue, valueMap.get(yValue) + 1);
                        } else {
                            valueMap.put(yValue, 1L);
                        }
                        totalValue += yValue;

                        if (maxValue < yValue) {
                            maxValue = yValue;
                        }

                        if (minValue > yValue) {
                            minValue = yValue;
                        }

                        if (maxFreq < valueMap.get(yValue)) {
                            maxFreq = valueMap.get(yValue);
                        }

                        if (minFreq > valueMap.get(yValue)) {
                            minFreq = valueMap.get(yValue);
                        }
                    }
                }
            }

            int i = 1;
            valueMap = MyDirectGraphSysUtil.sortMapByLongKeyByLongValue(valueMap);
            pb.updateValue(50, 100);
            for (Long key : valueMap.keySet()) {
                dataset.addValue(valueMap.get(key), "", key);
                ((DefaultTableModel) distributionTable.getModel()).addRow(new String[]{
                    "" + (i++),
                    "" + key,
                    "" + valueMap.get(key),
                    "" + MyDirectGraphMathUtil.threeDecimalFormat((double) valueMap.get(key) / maxFreq),
                    "" + MyDirectGraphMathUtil.threeDecimalPercent((double) valueMap.get(key) / dataTbl.getRowCount())
                });
            }

            avg = (float) totalValue / row;
            ((DefaultTableModel) propertyTable.getModel()).addRow(new String[]{"MIN.", "" + MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.threeDecimalFormat(minValue))});
            ((DefaultTableModel) propertyTable.getModel()).addRow(new String[]{"MAX.", "" + MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.threeDecimalFormat(maxValue))});
            ((DefaultTableModel) propertyTable.getModel()).addRow(new String[]{"AVG.", "" + MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.threeDecimalFormat(avg))});
            ((DefaultTableModel) propertyTable.getModel()).addRow(new String[]{"STD.", "" + MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.threeDecimalFormat(getStandardDeviation(valueMap, avg)))});

            String plotTitle = "";
            String xaxis = "";
            String yaxis = "";
            PlotOrientation orientation = PlotOrientation.VERTICAL;
            boolean show = false;
            boolean toolTips = true;
            boolean urls = false;
            pb.updateValue(100, 100);
            pb.dispose();
            return ChartFactory.createBarChart(plotTitle, xaxis, yaxis, dataset, orientation, show, toolTips, urls);
        } catch (Exception ex) {
            pb.updateValue(100, 100);
            pb.dispose();
            return null;
        }
    }

    private JFreeChart setCategoryPivotChartWithoutXValue(JTable distributionTable, JTable propertyTable) {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            LinkedHashMap<String, LinkedHashMap<String, Integer>> valueMap = new LinkedHashMap<>();
            int xColumn = xVariable.getSelectedIndex();
            int yColumn = yVariable.getSelectedIndex();
            int maxNumberOfYValueFrequency = 0;
            int minNumberOfYValueFrequency = 1000000000;
            boolean duplicatedValueExistsByID = false;

            int maxNumberOfYValues = 0;
            int minNumberOfYValues = 1000000000;
            int row = 0;
            while (row < dataTbl.getRowCount()) {
                String pivotValue = dataTbl.getValueAt(row, xColumn).toString();
                String yValue = dataTbl.getValueAt(row, yColumn).toString();
                if (valueMap.containsKey(pivotValue)) {
                    if (valueMap.get(pivotValue).containsKey(yValue)) {
                        valueMap.get(pivotValue).put(yValue, valueMap.get(pivotValue).get(yValue)+1);
                        duplicatedValueExistsByID = true;
                    } else {
                        valueMap.get(pivotValue).put(yValue, 1);
                    }
                } else {
                    LinkedHashMap<String, Integer> yValueMap = new LinkedHashMap();
                    yValueMap.put(yValue, 1);
                    valueMap.put(pivotValue, yValueMap);
                }
                row++;
            }

            LinkedHashMap<String, Integer> yValueFrequencyMap = new LinkedHashMap<>();
            for (String pivotKey : valueMap.keySet()) {
                LinkedHashMap<String, Integer> yValueMap = valueMap.get(pivotKey);
                if (maxNumberOfYValues < yValueMap.size()) {
                    maxNumberOfYValues = yValueMap.size();
                }

                if (minNumberOfYValues > yValueMap.size()) {
                    minNumberOfYValues = yValueMap.size();
                }

                for (String yValueKey : yValueMap.keySet()) {
                    if (yValueFrequencyMap.containsKey(yValueKey)) {
                        yValueFrequencyMap.put(yValueKey, yValueFrequencyMap.get(yValueKey)+yValueMap.get(yValueKey));
                    } else {
                        yValueFrequencyMap.put(yValueKey, yValueMap.get(yValueKey));
                    }

                    if (maxNumberOfYValueFrequency < yValueFrequencyMap.get(yValueKey)) {
                        maxNumberOfYValueFrequency = yValueFrequencyMap.get(yValueKey);
                    }

                    if (minNumberOfYValueFrequency > yValueFrequencyMap.get(yValueKey)) {
                        minNumberOfYValueFrequency = yValueFrequencyMap.get(yValueKey);
                    }
                }
            }

            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            int i = 1;

            yValueFrequencyMap = MyDirectGraphSysUtil.sortMapByIntegerValue(yValueFrequencyMap);

            for (String key : yValueFrequencyMap.keySet()) {
                dataset.addValue(yValueFrequencyMap.get(key), "", key);
                ((DefaultTableModel) distributionTable.getModel()).addRow(new String[]{
                        "" + (i++),
                        "" + key,
                        "" + yValueFrequencyMap.get(key),
                        "" + MyDirectGraphMathUtil.threeDecimalFormat((float)yValueFrequencyMap.get(key)/maxNumberOfYValueFrequency),
                        "" + MyDirectGraphMathUtil.threeDecimalPercent((float)yValueFrequencyMap.get(key)/dataTbl.getRowCount())
                });
            }
            ((DefaultTableModel)propertyTable.getModel()).addRow(new String[]{"MIN. NO. OF Y V.", "" + MyDirectGraphMathUtil.getCommaSeperatedNumber(minNumberOfYValues)});
            ((DefaultTableModel)propertyTable.getModel()).addRow(new String[]{"MAX. NO. OF Y V.", "" + MyDirectGraphMathUtil.getCommaSeperatedNumber(maxNumberOfYValues)});
            ((DefaultTableModel)propertyTable.getModel()).addRow(new String[]{"MIN. Y V. FREQ.", "" + MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.threeDecimalFormat(minNumberOfYValueFrequency))});
            ((DefaultTableModel)propertyTable.getModel()).addRow(new String[]{"MAX. Y V. FREQ.", "" + MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.threeDecimalFormat(maxNumberOfYValueFrequency))});
            ((DefaultTableModel)propertyTable.getModel()).addRow(new String[]{"AVG. Y V. FREQ.", "" + MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.threeDecimalFormat((float)dataTbl.getRowCount()/yValueFrequencyMap.size()))});
            ((DefaultTableModel)propertyTable.getModel()).addRow(new String[]{"AVG. Y V. FREQ. BY X VAR.", "" + MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.threeDecimalFormat((float)yValueFrequencyMap.size()/valueMap.size()))});
            ((DefaultTableModel)propertyTable.getModel()).addRow(new String[]{"STD.", "N/A" });
            ((DefaultTableModel)propertyTable.getModel()).addRow(new String[]{"DUP. Y V. BY X VAR. EXISTS.", (duplicatedValueExistsByID ? "TRUE" : "FALSE")});

            String plotTitle = "";
            String xaxis = "";
            String yaxis = "";
            PlotOrientation orientation = PlotOrientation.VERTICAL;
            boolean show = false;
            boolean toolTips = true;
            boolean urls = false;
            pb.updateValue(100, 100);
            pb.dispose();

            return ChartFactory.createBarChart(plotTitle, xaxis, yaxis, dataset, orientation, show, toolTips, urls);
        } catch (Exception ex) {
            ex.printStackTrace();
            pb.updateValue(100, 100);
            pb.dispose();
        }
        return null;
    }

    private JFreeChart setCategoryPivotChart(JTable distributionTable, JTable propertyTable) {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            LinkedHashMap<String, Long> valueMap = new LinkedHashMap<>();
            int xColumn = xVariable.getSelectedIndex();
            int yColumn = yVariable.getSelectedIndex();
            long totalValue = 0;
            int row = 0;
            while (row < dataTbl.getRowCount()) {
                String pivotValue = dataTbl.getValueAt(row, xColumn).toString();
                if (pivotValue.equals(this.xValue.getSelectedItem().toString())) {
                    String yValue = dataTbl.getValueAt(row, yColumn).toString();
                    if (valueMap.containsKey(yValue)) {
                        valueMap.put(yValue, valueMap.get(yValue) + 1);
                    } else {
                        valueMap.put(yValue, 1L);
                    }

                    if (maxFreq < valueMap.get(yValue)) {
                        maxFreq = valueMap.get(yValue);
                    }

                    if (minFreq > valueMap.get(yValue)) {
                        minFreq = valueMap.get(yValue);
                    }
                }
                row++;
            }

            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            int i=1;

            valueMap = MyDirectGraphSysUtil.sortMapByLongValue(valueMap);
            pb.updateValue(50, 100);

            for (String key : valueMap.keySet()) {
                dataset.addValue(valueMap.get(key), "", key);
                ((DefaultTableModel) distributionTable.getModel()).addRow(new String[]{
                    "" + (i++),
                    "" + key,
                    "" + valueMap.get(key),
                    "" + MyDirectGraphMathUtil.threeDecimalFormat((double)valueMap.get(key)/maxFreq),
                    "" + MyDirectGraphMathUtil.threeDecimalPercent((double)valueMap.get(key)/dataTbl.getRowCount())
                });
                    totalValue += valueMap.get(key);
            }
            avg = totalValue/valueMap.size();

            ((DefaultTableModel) propertyTable.getModel()).addRow(new String[]{"MIN.", "" + MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.threeDecimalFormat(minFreq))});
            ((DefaultTableModel) propertyTable.getModel()).addRow(new String[]{"MAX.", "" + MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.threeDecimalFormat(maxFreq))});
            ((DefaultTableModel)propertyTable.getModel()).addRow(new String[]{"AVG.", "" + MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.threeDecimalFormat(avg))});
            ((DefaultTableModel)propertyTable.getModel()).addRow(new String[]{"STD.", "N/A" });

            String plotTitle = "";
            String xaxis = "";
            String yaxis = "";
            PlotOrientation orientation = PlotOrientation.VERTICAL;
            boolean show = false;
            boolean toolTips = true;
            boolean urls = false;
            pb.updateValue(100, 100);
            pb.dispose();

            return ChartFactory.createBarChart(plotTitle, xaxis, yaxis, dataset, orientation, show, toolTips, urls);
        } catch (Exception ex) {
            ex.printStackTrace();
            pb.updateValue(100, 100);
            pb.dispose();
        }
        return null;
    }

    public float getStandardDeviation(LinkedHashMap<Long, Long> valueMap, float avg) {
        try {
            int data = 0;
            float sum = 0f;
            for (long n : valueMap.keySet()) {
                for (int i=0; i < n * valueMap.get(n); i++) {
                    sum += Math.pow(n - avg, 2);
                    data++;
                }
            }
            return (sum == 0.00f ? 0.00f : (float) Math.sqrt(sum / data));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }

}
