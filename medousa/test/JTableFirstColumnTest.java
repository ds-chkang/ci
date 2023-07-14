package medousa.test;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class JTableFirstColumnTest extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;

    public JTableFirstColumnTest() {
        setTitle("Scroll Always Visible Example");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(400, 300));

        // Create table model with initial data
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Name");
        tableModel.addColumn("2");
        tableModel.addColumn("3");
        tableModel.addColumn("Age");
        tableModel.addRow(new Object[]{"John Doe", "a", "b", 30});
        tableModel.addRow(new Object[]{"Jane Smith", "c", "d", 25});

        // Create JTable with the table model
        table = new JTable(tableModel);
        table.setPreferredSize(new Dimension(2000, 500));

        // Create JScrollPane and set scroll mode
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        //scrollPane.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);

        // Add the JScrollPane to the frame
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JTableFirstColumnTest example = new JTableFirstColumnTest();
            example.setVisible(true);
        });
    }
}