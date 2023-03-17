package datamining.utils.table;

import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Arrays;

public class MyTableRowColorer extends DefaultTableModel {

    public MyTableRowColorer(String [][] data, String [] columns) {
        super(data, columns);
    }

    java.util.List<Color> rowColours = Arrays.asList(
            Color.RED,
            Color.GREEN,
            Color.CYAN
    );

    public void setRowColour(int row, Color c) {
        rowColours.set(row, c);
        fireTableRowsUpdated(row, row);
    }

    public Color getRowColour(int row) {
        return rowColours.get(row);
    }

    @Override
    public int getRowCount() {
        return super.getRowCount();
    }

    @Override
    public int getColumnCount() {
        return super.getColumnCount();
    }

    @Override
    public Object getValueAt(int row, int column) {
        return super.getValueAt(row, column);
        //return String.format("%d %d", row, column);
    }
}