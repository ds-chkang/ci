package medousa.sequential.graph.stats;

import medousa.sequential.utils.MySequentialGraphVars;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

// Custom renderer for the first column
public class MyGrayCellRenderer
extends DefaultTableCellRenderer {

    @Override public Component getTableCellRendererComponent(
        JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int column) {

        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        cell.setBackground(Color.decode("#F0F0F0"));
        cell.setFont(MySequentialGraphVars.tahomaPlainFont12);
        return cell;
    }
}
