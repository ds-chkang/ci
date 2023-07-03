package medousa.test;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Comparator;

public class JTableSorterByMultiColumns
extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;

    public JTableSorterByMultiColumns() {
        setTitle("JTable Sorting Example");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create sample data
        Object[][] data = {
            {"John", "2022-05-10", "USA", "Item A"},
            {"Alice", "2022-04-15", "Canada", "Item B"},
            {"Bob", "2022-06-20", "UK", "Item C"},
            {"Emily", "2022-03-05", "Australia", "Item D"},
            {"John", "2022-05-10", "Canada", "Item E"},
            {"Alice", "2022-04-15", "USA", "Item F"}
        };

        // Create column names
        String[] columnNames = {"Name", "Date", "Country", "Item"};

        // Create table model with the data
        tableModel = new DefaultTableModel(data, columnNames);

        // Create the JTable with the model
        table = new JTable(tableModel);

        // Set the custom mouse listener on the table headers
        table.getTableHeader().addMouseListener(new ColumnHeaderMouseListener());

        // Display the table in a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }

    private class ColumnHeaderMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            int columnIndex = table.getTableHeader().columnAtPoint(e.getPoint());

            // Sort the table by the clicked column
            sortTable(columnIndex);
        }
    }

    private void sortTable(int columnIndex) {
        Object[][] data = new Object[tableModel.getRowCount()][tableModel.getColumnCount()];

        // Copy the data from the table model to a separate array
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            for (int j = 0; j < tableModel.getColumnCount(); j++) {
                data[i][j] = tableModel.getValueAt(i, j);
            }
        }

        // Sort the data based on the selected column
        Arrays.sort(data, new MultipleColumnComparator(columnIndex));

        // Update the table model with the sorted data
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                tableModel.setValueAt(data[i][j], i, j);
            }
        }
    }

    private class MultipleColumnComparator implements Comparator<Object[]> {
        private final int columnIndex;

        public MultipleColumnComparator(int columnIndex) {
            this.columnIndex = columnIndex;
        }

        @Override
        public int compare(Object[] row1, Object[] row2) {
            for (int i = 0; i <= columnIndex; i++) {
                int result = ((Comparable) row1[i]).compareTo(row2[i]);
                if (result != 0) {
                    return result;
                }
            }
            return 0;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JTableSorterByMultiColumns example = new JTableSorterByMultiColumns();
            example.setVisible(true);
        });
    }
}
