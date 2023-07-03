package medousa.table;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class MyNodeListTableCellColorRenderer
extends DefaultTableCellRenderer {

    private JTextField nodeSearchTxt;

    public MyNodeListTableCellColorRenderer() {
    }

    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {
        Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        //String s = table.getModel().getValueAt(row, column).toString();
        if (isSelected) {
            comp.setFont(new Font("Arial", Font.BOLD, 15));
            comp.setBackground(Color.PINK);
            comp.setForeground(Color.BLUE);
        } else {
            comp.setForeground(Color.BLACK);
            comp.setBackground(Color.WHITE);
        }
        return (comp);
    }
}