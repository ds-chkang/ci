package medousa.test;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

public class JTableSortingByDate
extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;

    public JTableSortingByDate() {
        setTitle("JTable Sorting Example");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create sample data
        Object[][] data = {
                {"John", getDateFromString("2022-05-10 1:12:11"), "USA"},
                {"Alice", getDateFromString("2022-04-15 2:12:11"), "Canada"},
                {"Bob", getDateFromString("2022-06-20 12:12:12"), "UK"},
                {"Emily", getDateFromString("2022-03-05 00:00:00"), "Australia"}
        };

        // Create column names
        String[] columnNames = {"Name", "Date", "Country"};

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

        // Sort the data based on the date column index
        Arrays.sort(data, Comparator.comparing(row -> (Date) row[columnIndex]));

        // Update the table model with the sorted data
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                tableModel.setValueAt(data[i][j], i, j);
            }
        }
    }

    private Date getDateFromString(String dateString) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JTableSortingByDate example = new JTableSortingByDate();
            example.setVisible(true);
        });
    }
}
