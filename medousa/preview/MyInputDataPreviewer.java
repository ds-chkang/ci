package medousa.preview;

import medousa.direct.utils.MyDirectGraphMathUtil;
import medousa.direct.utils.MyDirectGraphSysUtil;
import medousa.direct.utils.MyDirectGraphVars;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class MyInputDataPreviewer
extends JPanel
implements ActionListener {

    public JTable dataTable;
    public DefaultTableModel dataTableModel;
    public String [] dataTableColumns;
    public JSplitPane chartDataTableSplitPane;
    public JSplitPane propertyChartSplitPane;
    public JSplitPane leftTableSplitPane;
    private JTextField searchTxt = new JTextField();
    private TableRowSorter<TableModel> sorter;
    private JComboBox searchColumnSelecter;
    public MyColumnDistributionTable columnDataDistributionTable = new MyColumnDistributionTable();
    public MyDataPreviewChart dataPreviewChart = new MyDataPreviewChart();
    public MyDataPropertyTable dataPropertyTable = new MyDataPropertyTable();

    public MyInputDataPreviewer() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                removeAll();
            }
        });
    }

    public void decorate() {
        this.setBackground(Color.WHITE);
        this.setLayout(new BorderLayout(3,3));

        String [][] data = {};
        this.dataTableModel = new DefaultTableModel(data, this.dataTableColumns);
        this.dataTable = new JTable(this.dataTableModel);
        this.dataTable.setRowHeight(24);
        this.dataTable.setFont(MyDirectGraphVars.f_pln_12);
        this.dataTable.getTableHeader().getColumnModel().getColumn(0).setPreferredWidth(70);
        this.dataTable.setBackground(Color.WHITE);
        this.dataTable.setFocusable(false);
        this.dataTable.getTableHeader().setBackground(new Color(0,0,0,0));
        this.dataTable.getTableHeader().setOpaque(false);
        this.dataTable.getTableHeader().setFont(MyDirectGraphVars.tahomaBoldFont12);
        this.sorter = new TableRowSorter<>(this.dataTable.getModel());
        this.dataTable.setRowSorter(this.sorter);
        this.dataPropertyTable.numberOfColumns = this.dataTableColumns.length;
        this.dataPropertyTable.decorate();

        this.columnDataDistributionTable.decorate();

        this.leftTableSplitPane = new JSplitPane();
        this.leftTableSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        this.leftTableSplitPane.setTopComponent(this.dataPropertyTable);
        this.leftTableSplitPane.setBottomComponent(this.columnDataDistributionTable);
        this.leftTableSplitPane.setDividerLocation(0.25);
        this.leftTableSplitPane.addComponentListener(new ComponentAdapter() {
            @Override public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                leftTableSplitPane.setDividerLocation(0.25);
            }
        });

        this.propertyChartSplitPane = new JSplitPane();
        this.propertyChartSplitPane.setDividerLocation(0.175);
        this.propertyChartSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        this.propertyChartSplitPane.setLeftComponent(this.leftTableSplitPane);
        this.propertyChartSplitPane.setRightComponent(this.dataPreviewChart);
        this.propertyChartSplitPane.addComponentListener(new ComponentAdapter() {
            @Override public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                propertyChartSplitPane.setDividerLocation(0.175);
            }
        });

        JScrollPane dataTableScrollPane = new JScrollPane(dataTable);
        dataTableScrollPane.setBorder(BorderFactory.createLoweredBevelBorder());

        this.searchColumnSelecter = new JComboBox();
        this.searchColumnSelecter.setFont(MyDirectGraphVars.tahomaPlainFont12);
        this.searchColumnSelecter.setFocusable(false);
        this.searchColumnSelecter.setBackground(Color.WHITE);
        this.searchColumnSelecter.addItem("");
        for (int i=1; i < this.dataTableColumns.length; i++) {
            this.searchColumnSelecter.addItem(this.dataTableColumns[i]);
        }

        JButton searchBtn = new JButton("SEARCH");
        searchBtn.setFocusable(false);
        this.searchTxt.setFont(MyDirectGraphVars.f_pln_12);
        this.searchTxt.setBorder(BorderFactory.createEtchedBorder());
        this.searchTxt.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char keyChar = e.getKeyChar();
                e.setKeyChar(Character.toUpperCase(keyChar));
            }
        });
        JPanel dataSearchPanel = new JPanel();
        dataSearchPanel.setLayout(new BorderLayout(1,1));
        dataSearchPanel.add(this.searchColumnSelecter, BorderLayout.WEST);
        dataSearchPanel.add(this.searchTxt, BorderLayout.CENTER);
        dataSearchPanel.add(searchBtn, BorderLayout.EAST);
        dataSearchPanel.setBorder(BorderFactory.createLoweredSoftBevelBorder());

        searchBtn.addActionListener(this::searchButtonActionPerformed);

        JPanel dataTablePanel = new JPanel();
        dataTablePanel.setLayout(new BorderLayout(2,2));
        dataTablePanel.add(dataTableScrollPane, BorderLayout.CENTER);
        dataTablePanel.add(dataSearchPanel, BorderLayout.SOUTH);

        this.chartDataTableSplitPane = new JSplitPane();
        this.chartDataTableSplitPane.setDividerLocation(0.94);
        this.chartDataTableSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        this.chartDataTableSplitPane.setTopComponent(this.propertyChartSplitPane);
        this.chartDataTableSplitPane.setBottomComponent(dataTablePanel);
        this.chartDataTableSplitPane.addComponentListener(new ComponentAdapter() {
            @Override public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                chartDataTableSplitPane.setDividerLocation(0.94);
            }
        });

        this.add(this.chartDataTableSplitPane, BorderLayout.CENTER);
    }

    private void searchButtonActionPerformed(ActionEvent e) {
        String searchValue = searchTxt.getText();
        searchTable(searchValue);
    }

    private void searchTable(String searchValue) {
        if (this.searchColumnSelecter.getSelectedIndex() == 0) {
            RowFilter<TableModel, Object> rowFilter = RowFilter.regexFilter(searchValue);
            this.sorter.setRowFilter(rowFilter);
        } else {
            RowFilter<TableModel, Object> rowFilter = RowFilter.regexFilter(searchValue, this.searchColumnSelecter.getSelectedIndex());
            this.sorter.setRowFilter(rowFilter);
        }
    }

    public boolean loadInputDataToTable(File [] dataFiles) {
        try {
            int i = 0;
            int numericColumns = 0;
            int fineData = 0;
            for (File f : dataFiles) {
                BufferedReader br = new BufferedReader(new FileReader(f));
                String line = "";
                while ((line = br.readLine()) != null) {
                    String [] record = new String[this.dataTableColumns.length+1];
                    String [] rawData = line.split(MyDirectGraphVars.commaDelimeter);

                    if (this.dataTableColumns.length != rawData.length) {continue;}

                    boolean isEmptyColumnExist = false;
                    int columnCnt = 1;
                    record[0] = "" + (++i);

                    for (int col=1; col < rawData.length; col++) {
                        if (rawData[col].trim().length() == 0) {
                            isEmptyColumnExist = true;
                            break;
                        }

                        if (!rawData[col].trim().matches("\\d+(\\.\\d+)?")) {
                            record[columnCnt++] = rawData[col].trim().toUpperCase();
                        } else {
                            record[columnCnt++] = rawData[col];
                        }
                    }

                    if (isEmptyColumnExist) {continue;}
                    else if (!isEmptyColumnExist && numericColumns == 0) {
                        for (int col=1; col < rawData.length; col++) {
                            if (rawData[col].trim().matches("\\d+(\\.\\d+)?")) {
                                numericColumns++;
                            }
                        }
                    }

                    this.dataTableModel.addRow(record);
                    fineData++;
                }
            }

            int row = this.dataPropertyTable.tableModel.getRowCount()-1;
            while (row > -1) {
                this.dataPropertyTable.tableModel.removeRow(row);
                row = this.dataPropertyTable.tableModel.getRowCount()-1;
            }

            this.dataPropertyTable.tableModel.addRow(new String[]{"COLUMNS", MyDirectGraphMathUtil.getCommaSeperatedNumber(this.dataTableColumns.length)});
            this.dataPropertyTable.tableModel.addRow(new String[]{"DATA", MyDirectGraphMathUtil.getCommaSeperatedNumber(i)});
            this.dataPropertyTable.tableModel.addRow(new String[]{"PASSED DATA", MyDirectGraphMathUtil.getCommaSeperatedNumber(fineData) + "(" + MyDirectGraphMathUtil.twoDecimalFormat((float)fineData/i) + ")"});
            this.dataPropertyTable.tableModel.addRow(new String[]{"NUMERIC COLUMNS", MyDirectGraphMathUtil.getCommaSeperatedNumber(numericColumns)});
            this.dataPropertyTable.tableModel.addRow(new String[]{"CATEGORY COLUMNS", MyDirectGraphMathUtil.getCommaSeperatedNumber(this.dataTableColumns.length-numericColumns)});
            this.dataPropertyTable.tableModel.addRow(new String[]{"WORKING DIRECTORY", MyDirectGraphSysUtil.getWorkingDir()});
            this.dataPropertyTable.tableModel.addRow(new String[]{"COLUMN SEPARATOR", "COMMA(,)"});

            this.dataPreviewChart.decorate(this.dataTableColumns, this.dataTable, this.columnDataDistributionTable.table);

            return true;
        } catch (Exception ex) {
            //ex.printStackTrace();
            return false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
