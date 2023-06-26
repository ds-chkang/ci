package medousa.table;

import medousa.direct.utils.MyDirectGraphVars;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MyTableUtil {
    protected static final Font HEAD_FONT = new Font("Pretendard", Font.BOLD, 11);

    public static JPanel searchAndSaveDataPanelForJTable(
        ActionListener action_container,
        JTextField searchTxt,
        JButton save,
        DefaultTableModel dtm,
        JTable table) {
        table.setAutoCreateRowSorter(true);
        JPanel nodeExplorationPanel = new JPanel();
        nodeExplorationPanel.setBackground(Color.WHITE);
        nodeExplorationPanel.setLayout(new BorderLayout(3,3));
        save.setPreferredSize(new Dimension(80, 30));

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Color.WHITE);
        btnPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        save.addActionListener(action_container);
        save.setFocusable(false);
        btnPanel.add(save);

        TableRowSorter sorter = setJTableRowSorterWithTextField(dtm, searchTxt);
        table.setRowSorter(sorter);

        nodeExplorationPanel.add(searchTxt, BorderLayout.CENTER);
        nodeExplorationPanel.add(btnPanel, BorderLayout.EAST);
        return nodeExplorationPanel;
    }

    public static JPanel searchAndSaveDataPanelForJTable2(
        ActionListener actionContainer,
        JTextField searchTxt,
        JButton btn,
        DefaultTableModel dtm,
        JTable table) {
        table.setAutoCreateRowSorter(true);
        btn.setPreferredSize(new Dimension(80, 28));
        btn.addActionListener(actionContainer);
        btn.setFocusable(false);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BorderLayout(3,3));
        controlPanel.setPreferredSize(new Dimension(150, 30));

        TableRowSorter sorter = setJTableRowSorterWithTextField(dtm, searchTxt);
        table.setRowSorter(sorter);

        searchTxt.setPreferredSize(new Dimension(100, 28));
        searchTxt.setFont(MyDirectGraphVars.f_pln_13);
        searchTxt.addActionListener(actionContainer);

        controlPanel.add(btn, BorderLayout.EAST);
        controlPanel.add(searchTxt, BorderLayout.CENTER);
        return controlPanel;
    }

    public static JPanel searchTablePanel(
            ActionListener actionContainer,
            JTextField searchTxt,
            JButton btn,
            DefaultTableModel dtm,
            JTable table) {
        table.setAutoCreateRowSorter(true);
        btn.setPreferredSize(new Dimension(80, 28));
        btn.addActionListener(actionContainer);
        btn.setFocusable(false);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BorderLayout(3,3));
        controlPanel.setPreferredSize(new Dimension(150, 30));

        TableRowSorter sorter = setJTableRowSorterWithTextField(dtm, searchTxt);
        table.setRowSorter(sorter);

        searchTxt.setPreferredSize(new Dimension(100, 28));
        searchTxt.setFont(MyDirectGraphVars.f_pln_13);
        searchTxt.addActionListener(actionContainer);

        controlPanel.add(btn, BorderLayout.EAST);
        controlPanel.add(searchTxt, BorderLayout.CENTER);
        return controlPanel;
    }

    public static JPanel searchTablePanelWithColumnSelecter(
            ActionListener actionContainer,
            JTextField searchTxt,
            JButton btn,
            DefaultTableModel dtm,
            JTable table,
            JComboBox columnSelecter) {
        table.setAutoCreateRowSorter(true);
        btn.setPreferredSize(new Dimension(80, 28));
        btn.addActionListener(actionContainer);
        btn.setFocusable(false);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BorderLayout(3,3));
        //controlPanel.setPreferredSize(new Dimension(150, 30));

        searchTxt.setPreferredSize(new Dimension(100, 28));
        searchTxt.setFont(MyDirectGraphVars.f_pln_13);
        searchTxt.addActionListener(actionContainer);

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BorderLayout(1,1));
        searchPanel.add(columnSelecter, BorderLayout.WEST);
        searchPanel.add(searchTxt, BorderLayout.CENTER);

        TableRowSorter sorter = setJTableRowSorterWithTextField(dtm, searchTxt);
        table.setRowSorter(sorter);

        controlPanel.add(btn, BorderLayout.WEST);
        controlPanel.add(searchPanel, BorderLayout.CENTER);
        return controlPanel;
    }

    public static JPanel searchAndAddNodePanelForJTable(
            ActionListener actionContainer,
            JTextField searchTxt,
            JButton btn,
            DefaultTableModel dtm,
            JTable table) {
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout(3,3));

        table.setAutoCreateRowSorter(true);
        btn.setPreferredSize(new Dimension(80, 28));
        btn.setFocusable(false);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BorderLayout(3,3));
        controlPanel.setPreferredSize(new Dimension(150, 30));

        TableRowSorter sorter = setJTableRowSorterWithTextField(dtm, searchTxt);
        table.setRowSorter(sorter);

        searchTxt.setPreferredSize(new Dimension(100, 28));
        searchTxt.setFont(MyDirectGraphVars.f_pln_13);
        searchTxt.addActionListener(actionContainer);

        controlPanel.add(btn, BorderLayout.EAST);
        controlPanel.add(searchTxt, BorderLayout.CENTER);
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        tablePanel.add(controlPanel, BorderLayout.SOUTH);
        return tablePanel;
    }

    public static JPanel searchAndSelectPanelForJTable(
            ActionListener actionContainer,
            JTextField searchTxt,
            JButton btn,
            DefaultTableModel dtm,
            JTable table) {
        table.setAutoCreateRowSorter(true);
        btn.setPreferredSize(new Dimension(100, 28));
        btn.addActionListener(actionContainer);
        btn.setFocusable(false);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BorderLayout(3,3));
        controlPanel.setPreferredSize(new Dimension(150, 30));

        TableRowSorter sorter = setJTableRowSorterWithTextField(dtm, searchTxt);
        table.setRowSorter(sorter);

        searchTxt.setPreferredSize(new Dimension(100, 28));
        searchTxt.setFont(MyDirectGraphVars.f_pln_13);

        controlPanel.add(btn, BorderLayout.EAST);
        controlPanel.add(searchTxt, BorderLayout.CENTER);
        return controlPanel;
    }

    public static JPanel getDataSelectPanelForJTable(
            ActionListener action_container,
            JTextField searchTxt,
            JButton select,
            DefaultTableModel dtm,
            JTable table) {
        table.setAutoCreateRowSorter(true);
        JPanel nodeExplorationPanel = new JPanel();
        nodeExplorationPanel.setBackground(Color.WHITE);
        nodeExplorationPanel.setLayout(new BorderLayout(3,3));
        select.setPreferredSize(new Dimension(80, 30));

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Color.WHITE);
        btnPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        select.addActionListener(action_container);
        btnPanel.add(select);

        JLabel emptyLbl = new JLabel("");
        emptyLbl.setBackground(Color.WHITE);
        emptyLbl.setPreferredSize(new Dimension(2, 2));

        searchTxt.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        TableRowSorter sorter = setJTableRowSorterWithTextField(dtm, searchTxt);
        table.setRowSorter(sorter);

        nodeExplorationPanel.add(emptyLbl, BorderLayout.WEST);
        nodeExplorationPanel.add(searchTxt, BorderLayout.CENTER);
        nodeExplorationPanel.add(btnPanel, BorderLayout.EAST);
        return nodeExplorationPanel;
    }

    public static TableRowSorter setJTableRowSorterWithTextField(DefaultTableModel dtm, JTextField txt) {
        TableRowSorter sorter = new TableRowSorter<>(dtm);
        txt.setFont(new Font("Helvetica", Font.BOLD, 12));
        txt.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                search(txt.getText());
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                search(txt.getText());
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                search(txt.getText());
            }
            public void search(String str) {
                if (str.length() == 0) { sorter.setRowFilter(null); }
                else {
                    try {
                        sorter.setRowFilter(RowFilter.regexFilter(str));
                    } catch (Exception ex) {}
                }
            }
        });
        MyTableUtil.setKeyboardListenerToTextComponent(txt);
        return sorter;
    }

    public static void setColumnHeaderRenderer(JTable targetTable) {
        for (int i=0; i < targetTable.getColumnModel().getColumnCount(); i++) {
            TableColumn col = targetTable.getColumnModel().getColumn(i);
            col.setHeaderRenderer(new MyTableColumnHeaderRenderer());
        }
    }

    public static void setKeyboardListenerToTextComponent(JTextField txt) {
        txt.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char keyChar = e.getKeyChar();
                e.setKeyChar(Character.toUpperCase(keyChar));
            }
        });
    }

    public static void setTableHeaderRenderer(JTable table) {
        table.getTableHeader().setFont(HEAD_FONT);
        TableCellRenderer baseRenderer = table.getTableHeader().getDefaultRenderer();
        table.getTableHeader().setDefaultRenderer(new MyTableHeaderRenderer(baseRenderer));
    }

        // Set row heights of the target table.
    public static void setTableHeaderHeight(JTable targetTable, int width, int height) {
        JTableHeader th = targetTable.getTableHeader();
        th.setPreferredSize(new Dimension(width, height));
    }

    public static void setTableRowHeight(JTable targetTable, int height) {
        for (int i = 0; i < targetTable.getRowCount(); i++) {
            targetTable.setRowHeight(i, height);
        }
    }

    public static void setTableCellBorderColor(JTable targetTable, Color whichColor) {
        MatteBorder border = new MatteBorder(1, 1, 0, 0, whichColor);
        targetTable.setBorder(border);
    }

    public static void setPreferredTableCellWidth(JTable targetTable, int colNum, int width) {
        TableColumn cols = targetTable.getColumnModel().getColumn(colNum);
        cols.setPreferredWidth(width);
    }

    public static void removeRecordsFromTable(DefaultTableModel model) {
        int rowCount = model.getRowCount();
        for (int i = rowCount - 1; i >= 0; i--) {
            model.removeRow(i);
        }
    }
}
