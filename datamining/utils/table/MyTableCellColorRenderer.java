package datamining.utils.table;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.io.Serializable;

public class MyTableCellColorRenderer
extends DefaultTableCellRenderer
implements Serializable {
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {
        Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        //String s = table.getModel().getValueAt(row, column).toString();
        if (column == 0) {
            comp.setBackground(Color.LIGHT_GRAY);
        } else {
            comp.setBackground(Color.WHITE);
        }
        return (comp);
    }
}