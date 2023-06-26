package medousa.test;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;

public class JTableSearchExample extends JFrame {

    private JTable table;
    private JTextField searchField;
    private JButton searchButton;
    private TableRowSorter<TableModel> sorter;

    public JTableSearchExample() {
        setTitle("JTable Search Example");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
        pack();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        DefaultTableModel model = new DefaultTableModel(
                new Object[][]{
                        {"John", "Doe", 30},
                        {"Jane", "Smith", 25},
                        {"David", "Johnson", 35},
                        {"Sarah", "Williams", 28},
                        {"Michael", "Brown", 40}
                },
                new String[]{"First Name", "Last Name", "Age"}
        );

        table = new JTable(model);
        sorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(sorter);

        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        searchButton.addActionListener(this::searchButtonActionPerformed);

        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Search: "));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(searchPanel, BorderLayout.NORTH);
        getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void searchButtonActionPerformed(ActionEvent e) {
        String searchValue = searchField.getText();
        int columnIndex = 0; // Replace with the actual index of the column you want to search in
        searchTable(searchValue, columnIndex);
    }

    private void searchTable(String searchValue, int columnIndex) {
        RowFilter<TableModel, Object> rowFilter = RowFilter.regexFilter(searchValue, columnIndex);
        sorter.setRowFilter(rowFilter);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JTableSearchExample example = new JTableSearchExample();
            example.setVisible(true);
        });
    }
}
