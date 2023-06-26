package medousa.preview;

import medousa.direct.utils.MyDirectGraphVars;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MyColumnDistributionTable
extends JPanel {

    public JTable table;
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
        this.setBackground(Color.WHITE);
        this.setLayout(new BorderLayout(3,3));

        String [] columns = {"NO.", "NAME", "VALUE"};
        String [][] data = {};

        tableModel = new DefaultTableModel(data, columns);
        table = new JTable(tableModel);
        table.setBackground(Color.WHITE);
        table.setFont(MyDirectGraphVars.f_pln_12);
        table.setFocusable(false);
        table.setRowHeight(25);
        table.getTableHeader().getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getTableHeader().getColumnModel().getColumn(1).setPreferredWidth(140);
        table.getTableHeader().getColumnModel().getColumn(2).setPreferredWidth(70);
        table.getTableHeader().setFont(MyDirectGraphVars.tahomaBoldFont12);
        table.getTableHeader().setBackground(new Color(0,0,0,0));
        table.getTableHeader().setOpaque(false);
        this.sorter = new TableRowSorter<>(this.table.getModel());
        this.table.setRowSorter(this.sorter);

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
        dataSearchPanel.add(this.searchTxt, BorderLayout.CENTER);
        dataSearchPanel.add(searchBtn, BorderLayout.EAST);
        dataSearchPanel.setBorder(BorderFactory.createLoweredSoftBevelBorder());

        searchBtn.addActionListener(this::searchButtonActionPerformed);

        this.add(new JScrollPane(table), BorderLayout.CENTER);
        this.add(dataSearchPanel, BorderLayout.SOUTH);
    }

    private void searchButtonActionPerformed(ActionEvent e) {
        String searchValue = searchTxt.getText();
        searchTable(searchValue);
    }

    private void searchTable(String searchValue) {
        RowFilter<TableModel, Object> rowFilter = RowFilter.regexFilter(searchValue);
        this.sorter.setRowFilter(rowFilter);

    }

}
