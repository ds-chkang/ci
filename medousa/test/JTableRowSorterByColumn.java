package medousa.test;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Arrays;
import java.util.Comparator;

public class JTableRowSorterByColumn
extends JFrame {
    private JTable table;

    public JTableRowSorterByColumn() {
        setTitle("JTable Sorting Example");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create sample data
        Object[][] data = {
                {"John", 25, "USA"},
                {"Alice", 30, "Canada"},
                {"Bob", 20, "UK"},
                {"Emily", 35, "Australia"}
        };

        // Create column names
        String[] columnNames = {"Name", "Age", "Country"};

        // Create table model with the data
        DefaultTableModel model = new DefaultTableModel(data, columnNames);

        // Create the JTable with the model
        table = new JTable(model);

        // Display the table in a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Add a button to trigger the sorting
        JButton sortButton = new JButton("Sort");
        sortButton.addActionListener(e -> sortTable());
        getContentPane().add(sortButton, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    private void sortTable() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        Object[][] data = new Object[model.getRowCount()][model.getColumnCount()];

        // Copy the data from the table model to a separate array
        for (int i = 0; i < model.getRowCount(); i++) {
            for (int j = 0; j < model.getColumnCount(); j++) {
                data[i][j] = model.getValueAt(i, j);
            }
        }

        // Sort the data based on the column index
        Arrays.sort(data, Comparator.comparing(row -> (Comparable) row[1]));

        // Update the table model with the sorted data
        for (int i = 0; i < data.length; i++) {
            model.setValueAt(data[i][0], i, 0);  // Update the first column (name)
            model.setValueAt(data[i][1], i, 1);  // Update the second column (age)
            model.setValueAt(data[i][2], i, 2);  // Update the third column (country)
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JTableRowSorterByColumn example = new JTableRowSorterByColumn();
            example.setVisible(true);
        });
    }
}
