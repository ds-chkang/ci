package datamining.config;

import datamining.utils.table.MyVariableBindingColumnEditor;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.io.Serializable;
import java.util.Vector;

public class MyCategoryTable
extends MyTable
implements Serializable {

    private final String [] dataTypes = {" SET DATA TYPE", " BINARY", " INTEGER", " REAL"};
    private final Object [] columns = {" PROPERTY", " VALUE", " NO. OF CATEGORIES", " TYPE"};
    private final Object [][] data = {};

    public MyCategoryTable() {
        super();
        this.setModel(new DefaultTableModel(data, columns));
        this.decorate();
    }

    @Override
    protected void decorate() {
        this.setTableRowHeight();
        this.setTableCellBorderColor();
        this.setTableHeaderHeight();
        this.setTableColumnWidth(0, 50);
        this.setTableColumnWidth(1, 90);
        this.setTableColumnWidth(2, 50);
        this.setTableColumnWidth(3, 40);
        this.setColumnCellRenderer(0);
        this.setColumnCellRenderer(1);
        this.setColumnCellRenderer(2);
        this.setColumnCellRenderer(3);
        this.setBackground(Color.WHITE);
        this.setForeground(Color.BLACK);
        this.getTableHeader().setFont(HEAD_FONT);
        this.setFont(CELL_FONT);
        this.setSelectionBackground(Color.decode("#D6D9DF"));
    }

    public void update(String [] headers, boolean isUpdate) {
        // Before updating table, remove existing rows first.
        this.removeRows();
        TableColumn comboColumn = this.getColumnModel().getColumn(1);
        comboColumn.setCellEditor(new MyVariableBindingColumnEditor(headers, isUpdate));
        comboColumn = this.getColumnModel().getColumn(3);
        comboColumn.setCellEditor(new MyVariableBindingColumnEditor(this.dataTypes, isUpdate));
        // Add rows of supplemeratry variables to bind
        DefaultTableModel model = (DefaultTableModel) this.getModel();
        for (int i = 0; i < (headers.length - 4); i++) {
            Vector<Object> emptyRowValues = new Vector<Object>();
            model.addRow(emptyRowValues);
            model.setValueAt(" COLUMN NAME", i, 0);
            model.setValueAt(" SET COLUMN", i, 1);
            model.setValueAt(" ", i, 2);
            model.setValueAt(" SET DATA TYPE", i, 3);
        }
        this.setTableRowHeight();
        this.setTableCellBorderColor();
        this.setTableHeaderHeight();
        this.setTableColumnWidth(0, 80);
        this.setTableColumnWidth(1, 60);
        this.setTableColumnWidth(2, 50);
        this.setTableColumnWidth(3, 40);
        this.repaint();
        this.revalidate();
    }

    private void removeRows() {
        DefaultTableModel dm = (DefaultTableModel) this.getModel();
        if (dm.getRowCount() > 0 ) {
            for (int i = this.getRowCount()-1; i >= 0; i--) {
                dm.removeRow(i);
            }
        }
    }

    public String getVariableName(int rowIndex, int columnIndex) { return (this.getValueAt(rowIndex, columnIndex)).toString().trim(); }
    public String getCategoryType(int rowIndex, int columnIndex) { return (this.getValueAt(rowIndex, columnIndex)).toString().trim(); }
    public String getCategoryNumber(int rowIndex, int columnIndex) { return (this.getValueAt(rowIndex, columnIndex)).toString().trim(); }


}
