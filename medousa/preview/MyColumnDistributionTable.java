package medousa.preview;

import medousa.direct.utils.MyDirectGraphVars;
import medousa.table.MyTableCellBarChartRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;

public class MyColumnDistributionTable
extends JPanel {

    public JTable columnDistributionTable;
    public DefaultTableModel tableModel;
    private JTextField searchTxt = new JTextField();
    private TableRowSorter<TableModel> sorter;

    public MyColumnDistributionTable() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                removeAll();
            }
        });
    }

    public void decorate() {
        try {
            //this.setBackground(Color.WHITE);
            this.setLayout(new BorderLayout(1, 1));

            String[] columns = {"NO.", "VALUE", "FREQ.", "MAX. R.", "R."};
            String[][] data = {};

            tableModel = new DefaultTableModel(data, columns);
            columnDistributionTable = new JTable(tableModel);
            columnDistributionTable.setBackground(Color.WHITE);
            columnDistributionTable.setFont(MyDirectGraphVars.f_pln_12);
            columnDistributionTable.setFocusable(false);
            columnDistributionTable.setRowHeight(25);
            columnDistributionTable.getTableHeader().getColumnModel().getColumn(0).setPreferredWidth(55);
            columnDistributionTable.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(60);
            columnDistributionTable.getTableHeader().getColumnModel().getColumn(1).setPreferredWidth(130);
            columnDistributionTable.getTableHeader().getColumnModel().getColumn(2).setPreferredWidth(75);
            columnDistributionTable.getTableHeader().getColumnModel().getColumn(3).setPreferredWidth(70);
            columnDistributionTable.getTableHeader().getColumnModel().getColumn(4).setPreferredWidth(70);
            columnDistributionTable.getTableHeader().setFont(MyDirectGraphVars.tahomaBoldFont12);
            columnDistributionTable.getTableHeader().setBackground(new Color(0, 0, 0, 0));
            columnDistributionTable.getTableHeader().setOpaque(false);
            columnDistributionTable.getColumnModel().getColumn(3).setCellRenderer(new MyTableCellBarChartRenderer());
            columnDistributionTable.addMouseListener(new TableMouseListener());
            this.sorter = new TableRowSorter<>(this.columnDistributionTable.getModel());
            this.columnDistributionTable.setRowSorter(this.sorter);

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
            searchBtnEnlargeBtnPanel.setPreferredSize(new Dimension(120, 27));
            searchBtnEnlargeBtnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 1,1));
            searchBtnEnlargeBtnPanel.add(searchBtn);
            searchBtnEnlargeBtnPanel.add(enlargeBtn);

            JPanel dataSearchPanel = new JPanel();
            dataSearchPanel.setLayout(new BorderLayout(1, 1));
            dataSearchPanel.add(this.searchTxt, BorderLayout.CENTER);
            dataSearchPanel.add(searchBtnEnlargeBtnPanel, BorderLayout.EAST);
            //dataSearchPanel.setBorder(BorderFactory.createLoweredSoftBevelBorder());

            searchBtn.addActionListener(this::searchButtonActionPerformed);

            add(new JScrollPane(columnDistributionTable), BorderLayout.CENTER);
            add(dataSearchPanel, BorderLayout.SOUTH);
        } catch (Exception ex) {

        }
    }


    private class TableMouseListener
            implements MouseListener {
        @Override public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isRightMouseButton(e)) {
                int row = columnDistributionTable.rowAtPoint(e.getPoint());
                int column = columnDistributionTable.columnAtPoint(e.getPoint());

                if (row >= 0 && row < columnDistributionTable.getRowCount()) {
                    //columnStatTable.setRowSelectionInterval(row, row);
                    //columnStatTable.setColumnSelectionInterval(column, column);
                    columnDistributionTable.setSelectionBackground(Color.WHITE);
                    columnDistributionTable.setSelectionForeground(Color.BLACK);
                }
            } else {
                columnDistributionTable.setSelectionBackground(Color.ORANGE);
                columnDistributionTable.setSelectionForeground(Color.BLACK);
            }
        }

        @Override public void mousePressed(MouseEvent e) {}
        @Override public void mouseReleased(MouseEvent e) {}
        @Override public void mouseEntered(MouseEvent e) {
            columnDistributionTable.setSelectionBackground(Color.WHITE);
            columnDistributionTable.setSelectionForeground(Color.BLACK);
        }
        @Override public void mouseExited(MouseEvent e) {
            columnDistributionTable.setSelectionBackground(Color.WHITE);
            columnDistributionTable.setSelectionForeground(Color.BLACK);
        }
    }

    private void enlarge() {
        MyColumnDistributionTable dataDistributionTablePanel = new MyColumnDistributionTable();
        dataDistributionTablePanel.decorate();
        int row = 0;
        while (row < MyDirectGraphVars.app.getToolBar().getPreviewer().columnDistributionTable.columnDistributionTable.getRowCount()) {
            String no = MyDirectGraphVars.app.getToolBar().getPreviewer().columnDistributionTable.columnDistributionTable.getValueAt(row, 0).toString();
            String value = MyDirectGraphVars.app.getToolBar().getPreviewer().columnDistributionTable.columnDistributionTable.getValueAt(row, 1).toString();
            String count = MyDirectGraphVars.app.getToolBar().getPreviewer().columnDistributionTable.columnDistributionTable.getValueAt(row, 2).toString();
            String maxR = MyDirectGraphVars.app.getToolBar().getPreviewer().columnDistributionTable.columnDistributionTable.getValueAt(row, 3).toString();
            String r = MyDirectGraphVars.app.getToolBar().getPreviewer().columnDistributionTable.columnDistributionTable.getValueAt(row, 4).toString();

            ((DefaultTableModel) dataDistributionTablePanel.columnDistributionTable.getModel()).addRow(new String[]{no, value, count, maxR, r});
            row++;
        }
        JFrame f = new JFrame("Variable Value Distribution");
        f.setLayout(new BorderLayout(1,1));
        f.getContentPane().add(dataDistributionTablePanel, BorderLayout.CENTER);
        f.pack();
        f.setVisible(true);
    }

    private void searchButtonActionPerformed(ActionEvent e) {
        try {
            if (searchTxt.getText().length() > 0) {
                String searchValue = searchTxt.getText();
                searchTable(searchValue);
            }
        } catch (Exception ex) {

        }
    }

    private void searchTable(String searchValue) {
        try {
            if (searchValue.length() > 0) {
                RowFilter<TableModel, Object> rowFilter = RowFilter.regexFilter(searchValue);
                this.sorter.setRowFilter(rowFilter);
            }
        } catch (Exception ex) {

        }
    }

}
