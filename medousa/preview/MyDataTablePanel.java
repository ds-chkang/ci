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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MyDataTablePanel
extends JPanel {

    private JComboBox searchColumnSelecter;

    private JTextField searchTxt = new JTextField();
    public JTable dataTable;
    public DefaultTableModel dataTableModel;
    public String [] dataTableColumns = MyDirectGraphVars.app.getDirectGraphMsgBroker().getHeaders();
    private TableRowSorter<TableModel> sorter;
    public MyDataPropertyTable dataPropertyTable = new MyDataPropertyTable();

    public MyDataTablePanel() {}

    public void decorate() {
        try {
            removeAll();
            setLayout(new BorderLayout(1,1));

            String[][] data = {};
            this.dataTableModel = new DefaultTableModel(data, this.dataTableColumns);
            this.dataTable = new JTable(this.dataTableModel);
            this.dataTable.setRowHeight(24);
            this.dataTable.setFont(MyDirectGraphVars.f_pln_12);
            this.dataTable.getTableHeader().getColumnModel().getColumn(0).setPreferredWidth(65);
            this.dataTable.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(65);
            this.dataTable.setBackground(Color.WHITE);
            this.dataTable.setFocusable(false);
            this.dataTable.getTableHeader().setBackground(new Color(0, 0, 0, 0));
            this.dataTable.getTableHeader().setOpaque(false);
            this.dataTable.getTableHeader().setFont(MyDirectGraphVars.tahomaBoldFont12);
            this.sorter = new TableRowSorter<>(this.dataTable.getModel());
            this.dataTable.setRowSorter(this.sorter);
            this.dataPropertyTable.numberOfColumns = this.dataTableColumns.length;
            this.dataPropertyTable.decorate();

            JScrollPane dataTableScrollPane = new JScrollPane(this.dataTable);
            dataTableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

            loadDataToTable();

            this.searchColumnSelecter = new JComboBox();
            this.searchColumnSelecter.setFont(MyDirectGraphVars.tahomaPlainFont12);
            this.searchColumnSelecter.setFocusable(false);
            this.searchColumnSelecter.setBackground(Color.WHITE);
            this.searchColumnSelecter.addItem("");
            for (String column : dataTableColumns) {
                this.searchColumnSelecter.addItem(column);
            }

            JButton searchBtn = new JButton("SEARCH");
            searchBtn.setFocusable(false);
            searchBtn.addActionListener(this::searchButtonActionPerformed);

            JButton enlargeBtn = new JButton("+");
            enlargeBtn.setFont(MyDirectGraphVars.tahomaPlainFont12);
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

            JPanel searchBtnEnlargeBtnPanel = new JPanel();
            searchBtnEnlargeBtnPanel.setPreferredSize(new Dimension(140, 27));
            searchBtnEnlargeBtnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 1,1));
            searchBtnEnlargeBtnPanel.add(searchBtn);
            searchBtnEnlargeBtnPanel.add(enlargeBtn);

            this.searchTxt.setFont(MyDirectGraphVars.f_pln_12);
            this.searchTxt.setBorder(BorderFactory.createEtchedBorder());
            this.searchTxt.addKeyListener(new KeyAdapter() {
                public void keyTyped(KeyEvent e) {
                    char keyChar = e.getKeyChar();
                    e.setKeyChar(Character.toUpperCase(keyChar));
                }
            });
            JPanel dataSearchPanel = new JPanel();
            dataSearchPanel.setLayout(new BorderLayout(1, 1));
            dataSearchPanel.add(this.searchColumnSelecter, BorderLayout.WEST);
            dataSearchPanel.add(this.searchTxt, BorderLayout.CENTER);
            dataSearchPanel.add(searchBtnEnlargeBtnPanel, BorderLayout.EAST);

            JPanel dataTableSearchPanel = new JPanel();
            dataTableSearchPanel.setLayout(new BorderLayout(1,1));
            dataTableSearchPanel.add(dataTableScrollPane, BorderLayout.CENTER);
            dataTableSearchPanel.add(dataSearchPanel, BorderLayout.SOUTH);

            JSplitPane dataTablePropertySplitPane = new JSplitPane();
            dataTablePropertySplitPane.setOneTouchExpandable(false);
            dataTablePropertySplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
            dataTablePropertySplitPane.setLeftComponent(this.dataPropertyTable);
            dataTablePropertySplitPane.setRightComponent(dataTableSearchPanel);
            dataTablePropertySplitPane.setDividerLocation(0.23);
            dataTablePropertySplitPane.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    super.componentResized(e);
                    dataTablePropertySplitPane.setDividerLocation(0.23);
                }
            });

            dataTable.addMouseListener(new TableMouseListener());


            add(dataTableSearchPanel, BorderLayout.CENTER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private class TableMouseListener
            implements MouseListener {
        @Override public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isRightMouseButton(e)) {
                int row = dataTable.rowAtPoint(e.getPoint());
                int column = dataTable.columnAtPoint(e.getPoint());

                if (row >= 0 && row < dataTable.getRowCount()) {
                    //columnStatTable.setRowSelectionInterval(row, row);
                    //columnStatTable.setColumnSelectionInterval(column, column);
                    dataTable.setSelectionBackground(Color.WHITE);
                    dataTable.setSelectionForeground(Color.BLACK);
                }
            } else {
                dataTable.setSelectionBackground(Color.ORANGE);
                dataTable.setSelectionForeground(Color.BLACK);
            }
        }

        @Override public void mousePressed(MouseEvent e) {}
        @Override public void mouseReleased(MouseEvent e) {}
        @Override public void mouseEntered(MouseEvent e) {
            dataTable.setSelectionBackground(Color.WHITE);
            dataTable.setSelectionForeground(Color.BLACK);
        }
        @Override public void mouseExited(MouseEvent e) {
            dataTable.setSelectionBackground(Color.WHITE);
            dataTable.setSelectionForeground(Color.BLACK);
        }
    }

    private void enlarge() {
        MyDataTablePanel dataTablePanel = new MyDataTablePanel();
        dataTablePanel.decorate();
        JFrame f = new JFrame("DATA");
        f.setLayout(new BorderLayout(1,1));
        f.getContentPane().add(dataTablePanel, BorderLayout.CENTER);
        f.pack();
        f.setVisible(true);
    }

    private void searchButtonActionPerformed(ActionEvent e) {
        try {
            String searchValue = searchTxt.getText().toUpperCase();
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
        } catch (Exception ex) {}
    }

    public boolean loadDataToTable() {
        try {
            int numericColumns = 0;
            int fineData = 0;
            for (File f : MyDirectGraphVars.app.getDirectGraphMsgBroker().getInputFiles()) {
                BufferedReader br = new BufferedReader(new FileReader(f));
                String line = "";
                while ((line = br.readLine()) != null) {
                    String [] record = new String[this.dataTableColumns.length+1];
                    String [] rawData = line.split(MyDirectGraphVars.commaDelimeter);

                    if (this.dataTableColumns.length != rawData.length) {continue;}

                    boolean isEmptyColumnExist = false;
                    int columnCnt = 1;
                    record[0] = "" + (this.dataTable.getRowCount()+1);

                    for (int col=1; col < rawData.length; col++) {
                        if (rawData[col].trim().length() == 0) {
                            isEmptyColumnExist = true;
                            break;
                        }

                        if (!rawData[col].trim().matches("-?\\d+(\\.\\d+)?")) {
                            if (!checkIfDateTimeColumn(rawData[col]).equals("")) {
                                String [] dateTime = rawData[col].split(" ");
                                if (dateTime.length > 2) {
                                    record[columnCnt++] = rawData[col] + " " + rawData[1];
                                } else {
                                    record[columnCnt++] = rawData[col];
                                }
                            } else {
                                record[columnCnt++] = rawData[col].trim().replaceAll(",", "").toUpperCase();
                            }
                        } else {
                            record[columnCnt++] = rawData[col];
                        }
                    }

                    if (isEmptyColumnExist) {continue;}
                    else if (!isEmptyColumnExist && numericColumns == 0) {
                        for (int col=1; col < rawData.length; col++) {
                            if (rawData[col].trim().matches("-?\\d+(\\.\\d+)?")) {
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

            this.dataPropertyTable.tableModel.addRow(new String[]{"INPUT FILES", MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.app.getDirectGraphMsgBroker().getInputFiles().length)});
            this.dataPropertyTable.tableModel.addRow(new String[]{"COLUMNS", MyDirectGraphMathUtil.getCommaSeperatedNumber(this.dataTableColumns.length)});
            this.dataPropertyTable.tableModel.addRow(new String[]{"DATA", MyDirectGraphMathUtil.getCommaSeperatedNumber(this.dataTable.getRowCount())});
            this.dataPropertyTable.tableModel.addRow(new String[]{"LOADED DATA", MyDirectGraphMathUtil.getCommaSeperatedNumber(fineData) + "(" + MyDirectGraphMathUtil.twoDecimalFormat((float)fineData/this.dataTable.getRowCount()) + ")"});
            this.dataPropertyTable.tableModel.addRow(new String[]{"NUMERIC COLUMNS", MyDirectGraphMathUtil.getCommaSeperatedNumber(numericColumns)});
            this.dataPropertyTable.tableModel.addRow(new String[]{"CATEGORY COLUMNS", MyDirectGraphMathUtil.getCommaSeperatedNumber(this.dataTableColumns.length-numericColumns)});
            this.dataPropertyTable.tableModel.addRow(new String[]{"WORKING DIRECTORY", MyDirectGraphSysUtil.getWorkingDir()});
            this.dataPropertyTable.tableModel.addRow(new String[]{"COLUMN SEPARATOR", "COMMA(,)"});

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private String checkIfDateTimeColumn(String value) {
        String [] dateTime = value.split(" ");
        if (dateTime.length > 2) {
            value = dateTime[0] + " " + dateTime[1];
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd[ HH:mm:ss]");
        try {
            LocalDateTime.parse(value, formatter);
        } catch (Exception e) {
            return "";
        }
        return value;
    }

}
