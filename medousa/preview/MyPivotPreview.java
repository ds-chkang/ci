package medousa.preview;

import medousa.MyProgressBar;
import medousa.direct.utils.MyDirectGraphMathUtil;
import medousa.direct.utils.MyDirectGraphSysUtil;
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
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

public class MyPivotPreview
extends JPanel
implements ActionListener {

    private JTable dataTbl;
    private String [] columns;
    private JComboBox variable1;
    private JComboBox variable2;

    private JComboBox variable1Value;
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

            variable1 = new JComboBox();
            variable1.addItem("");
            for (int i=1; i < columns.length; i++) {
                variable1.addItem(columns[i]);
            }
            variable1.setBackground(Color.WHITE);
            variable1.setFont(MyDirectGraphVars.tahomaPlainFont11);
            variable1.setFocusable(false);
            variable1.addActionListener(this);

            variable1Value = new JComboBox();
            variable1Value.setBackground(Color.WHITE);
            variable1Value.setFont(MyDirectGraphVars.tahomaPlainFont11);
            variable1Value.setFocusable(false);

            variable2 = new JComboBox();
            variable2.addItem("");
            for (int i=1; i < columns.length; i++) {
                variable2.addItem(columns[i]);
            }
            variable2.setBackground(Color.WHITE);
            variable2.setFont(MyDirectGraphVars.tahomaPlainFont11);
            variable2.setFocusable(false);

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
            variable1Panel.add(variable1);
            variable1Panel.add(variable1EmptyLabel);

            JPanel variable2Panel = new JPanel();
            variable2Panel.setLayout(new FlowLayout(FlowLayout.LEFT, 1,1));
            variable2Panel.add(variableLabel2);
            variable2Panel.add(variable2);

            JLabel pivotValueLabel = new JLabel("XV:");
            pivotValueLabel.setToolTipText("Select a value of variable X.");
            pivotValueLabel.setFont(MyDirectGraphVars.tahomaPlainFont12);

            JPanel valuePanel = new JPanel();
            valuePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 1,1));
            valuePanel.add(pivotValueLabel);
            valuePanel.add(variable1Value);

            JPanel controlPanel = new JPanel();
            controlPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 1,1));
            controlPanel.add(variable1Panel);
            controlPanel.add(valuePanel);
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
                if (e.getSource() == variable1) {
                    MyProgressBar pb = new MyProgressBar(false);
                    try {
                        if (variable1.getSelectedIndex() == 0) return;
                        Set<String> columnValues = new HashSet<>();
                        int row = dataTbl.getRowCount();
                        while (row > 0) {
                            columnValues.add(dataTbl.getValueAt(row - 1, variable1.getSelectedIndex()).toString());
                            row--;
                        }
                        variable1Value.removeAllItems();
                        variable1Value.addItem("");
                        for (String columnValue : columnValues) {
                            if (columnValue.matches("\\d+(\\.\\d+)?")) {
                                variable1Value.addItem((long) Float.parseFloat(columnValue));
                            } else {
                                variable1Value.addItem(columnValue);
                            }
                        }
                        pb.updateValue(100, 100);
                        pb.dispose();
                    } catch (Exception ex) {
                        pb.updateValue(100, 100);
                        pb.dispose();
                    }
                } else if (e.getSource() == showBtn) {
                    if (variable1.getSelectedItem().toString().trim().equals(variable2.getSelectedItem().toString().trim())) {
                        MyMessageUtil.showInfoMsg("Choose different variables.");
                    } else if (variable2.getSelectedIndex() == 0) {
                        MyMessageUtil.showInfoMsg("Select the target variable.");
                    } else {
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
                        distributionTable.getColumnModel().getColumn(3).setCellRenderer(new MyTableCellRenderer());
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

                        ChartPanel chartPanel = new ChartPanel(setPivotChart(distributionTable, propertyTable));
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
                        chartPanel.setBorder(BorderFactory.createLoweredBevelBorder());

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

    private JFreeChart setPivotChart(JTable distributionTable, JTable propertyTable) {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            LinkedHashMap<Long, Long> numericValueMap = null;
            LinkedHashMap<String, Long> stringValueMap = null;

            long totalValue = 0;
            int row = dataTbl.getRowCount() - 1;
            while (row > -1) {
                if (!dataTbl.getValueAt(0, variable1.getSelectedIndex()).toString().matches("\\d+(\\.\\d+)?")) {
                    String pivotValue = dataTbl.getValueAt(row, variable1.getSelectedIndex()).toString();
                    if (pivotValue.equals(this.variable1Value.getSelectedItem().toString())) {
                        if (dataTbl.getValueAt(0, variable2.getSelectedIndex()).toString().matches("\\d+(\\.\\d+)?")) {
                            if (numericValueMap == null) numericValueMap = new LinkedHashMap<>();
                            long targetValue = (long) Float.parseFloat(dataTbl.getValueAt(row, variable2.getSelectedIndex()).toString());
                            totalValue += targetValue;
                            if (numericValueMap.containsKey(targetValue)) {
                                numericValueMap.put(targetValue, numericValueMap.get(targetValue) + 1);
                            } else {
                                numericValueMap.put(targetValue, 1L);
                            }

                            if (maxValue < targetValue) {
                                maxValue = targetValue;
                            }

                            if (minValue > targetValue) {
                                minValue = targetValue;
                            }

                            if (maxFreq < numericValueMap.get(targetValue)) {
                                maxFreq = numericValueMap.get(targetValue);
                            }

                            if (minFreq > numericValueMap.get(targetValue)) {
                                minFreq = numericValueMap.get(targetValue);
                            }
                        } else {
                            if (stringValueMap == null) stringValueMap = new LinkedHashMap<>();
                            String targetValue = dataTbl.getValueAt(row, variable2.getSelectedIndex()).toString();
                            if (stringValueMap.containsKey(targetValue)) {
                                stringValueMap.put(targetValue, stringValueMap.get(targetValue) + 1);
                            } else {
                                stringValueMap.put(targetValue, 1L);
                            }

                            if (maxFreq < stringValueMap.get(targetValue)) {
                                maxFreq = stringValueMap.get(targetValue);
                            }

                            if (minFreq > stringValueMap.get(targetValue)) {
                                minFreq = stringValueMap.get(targetValue);
                            }
                        }
                    }
                } else {
                    long pivotValue = (long) (Float.parseFloat(dataTbl.getValueAt(row, variable1.getSelectedIndex()).toString().split("\\.")[0]));
                    if (pivotValue == Long.parseLong(this.variable1Value.getSelectedItem().toString())) {
                        if (!dataTbl.getValueAt(0, variable2.getSelectedIndex()).toString().matches("\\d+(\\.\\d+)?")) {
                            if (stringValueMap == null) stringValueMap = new LinkedHashMap<>();
                            String targetValue = dataTbl.getValueAt(row, variable2.getSelectedIndex()).toString();
                            if (stringValueMap.containsKey(targetValue)) {
                                stringValueMap.put(targetValue, stringValueMap.get(targetValue) + 1);
                            } else {
                                stringValueMap.put(targetValue, 1L);
                            }

                            if (maxFreq < stringValueMap.get(targetValue)) {
                                maxFreq = stringValueMap.get(targetValue);
                            }

                            if (minFreq > stringValueMap.get(targetValue)) {
                                minFreq = stringValueMap.get(targetValue);
                            }
                        } else {
                            if (numericValueMap == null) numericValueMap = new LinkedHashMap<>();
                            long targetValue = Long.parseLong(dataTbl.getValueAt(row, variable2.getSelectedIndex()).toString().split("\\.")[0]);
                            if (numericValueMap.containsKey(targetValue)) {
                                numericValueMap.put(targetValue, numericValueMap.get(targetValue) + 1);
                            } else {
                                numericValueMap.put(targetValue, 1L);
                            }

                            if (maxValue < targetValue) {
                                maxValue = targetValue;
                            }

                            if (minValue > targetValue) {
                                minValue = targetValue;
                            }

                            if (maxFreq < numericValueMap.get(targetValue)) {
                                maxFreq = numericValueMap.get(targetValue);
                            }

                            if (minFreq > numericValueMap.get(targetValue)) {
                                minFreq = numericValueMap.get(targetValue);
                            }
                        }
                    }
                }
                row--;
            }

            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            int i=1;
            if (numericValueMap != null) {
                numericValueMap = MyDirectGraphSysUtil.sortMapByLongKeyByLongValue(numericValueMap);
                pb.updateValue(50, 100);

                int totalRows = 0;
                for (Long key : numericValueMap.keySet()) {
                    dataset.addValue(numericValueMap.get(key), "", key);
                    ((DefaultTableModel) distributionTable.getModel()).addRow(new String[]{
                            "" + (i++),
                            "" + key,
                            "" + numericValueMap.get(key),
                            "" + MyDirectGraphMathUtil.threeDecimalFormat((double)numericValueMap.get(key)/maxFreq),
                            "" + MyDirectGraphMathUtil.threeDecimalPercent((double)numericValueMap.get(key)/dataTbl.getRowCount())
                    });
                    totalRows += key * numericValueMap.get(key);
                }
                avg = (float)totalValue/totalRows;

                ((DefaultTableModel)propertyTable.getModel()).addRow(new String[]{"MIN.", "" + minValue});
                ((DefaultTableModel)propertyTable.getModel()).addRow(new String[]{"MAX.", "" + maxValue});
                ((DefaultTableModel)propertyTable.getModel()).addRow(new String[]{"AVG.", "" + MyDirectGraphMathUtil.threeDecimalFormat(avg)});
                ((DefaultTableModel)propertyTable.getModel()).addRow(new String[]{"STD.", "" + MyDirectGraphMathUtil.threeDecimalFormat(getStandardDeviation(numericValueMap))});
            } else {
                stringValueMap = MyDirectGraphSysUtil.sortMapByLongValue(stringValueMap);
                pb.updateValue(50, 100);

                for (String key : stringValueMap.keySet()) {
                    dataset.addValue(stringValueMap.get(key), "", key);
                    ((DefaultTableModel) distributionTable.getModel()).addRow(new String[]{
                            "" + (i++),
                            "" + key,
                            "" + stringValueMap.get(key),
                            "" + MyDirectGraphMathUtil.threeDecimalFormat((double)stringValueMap.get(key)/maxFreq),
                            "" + MyDirectGraphMathUtil.threeDecimalPercent((double)stringValueMap.get(key)/dataTbl.getRowCount())
                    });
                    totalValue += stringValueMap.get(key);
                }
                avg = totalValue/stringValueMap.size();

                ((DefaultTableModel)propertyTable.getModel()).addRow(new String[]{"MIN.", "" + minFreq});
                ((DefaultTableModel)propertyTable.getModel()).addRow(new String[]{"MAX.", "" + maxFreq});
                ((DefaultTableModel)propertyTable.getModel()).addRow(new String[]{"AVG.", "" + MyDirectGraphMathUtil.threeDecimalFormat(avg)});
                ((DefaultTableModel)propertyTable.getModel()).addRow(new String[]{"STD.", "" });
            }

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
