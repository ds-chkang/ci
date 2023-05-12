package medousa.direct.config;

import medousa.table.MyVariableBindingColumnEditor;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.Vector;

public class MyDirectGraphSupplementaryTable
extends MyDirectGraphTable {

    private final String [] dataTypes = {" SET DATA TYPE", " BINARY", " INTEGER", " REAL"};
    private final Object [] columns = {" PROPERTY", " VALUE", " NO. OF CATEGORIES", " VALUE TYPE"};
    private final Object [][] data = {};

    public MyDirectGraphSupplementaryTable() {
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
        this.removeRows();
        TableColumn comboColumn = this.getColumnModel().getColumn(1);
        comboColumn.setCellEditor(new MyVariableBindingColumnEditor(headers, isUpdate));
        comboColumn = this.getColumnModel().getColumn(3);
        comboColumn.setCellEditor(new MyVariableBindingColumnEditor(this.dataTypes, isUpdate));
        DefaultTableModel model = (DefaultTableModel) this.getModel();
        for (int i = 0; i < headers.length; i++) {
            if ((headers[i].contains("NO.") && headers[i].length() == 3) ||
                headers[i].contains("OBJECT") ||
                headers[i].contains("TRANSACTION") ||
                headers[i].contains("FROM ITEM") ||
                headers[i].contains("TO ITEM")) continue;
            Vector<Object> emptyRowValues = new Vector<Object>();
            model.addRow(emptyRowValues);
            model.setValueAt(" COLUMN NAME", i, 0);
            model.setValueAt(" SET COLUMN", i, 1);
            model.setValueAt("  ", i, 2);
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

    public boolean needCategorization() {
        if (this.getCategoryType(0, 3).trim().equals("BINARY")  ||
            this.getCategoryType(0, 3).trim().equals("INTEGER") ||
            this.getCategoryType(0, 3).trim().equals("REAL")) {
            return true;
        } else return false;
    }
    public String getVariableName(int rowIndex, int columnIndex) { return ((String)this.getValueAt(rowIndex, columnIndex)).trim(); }
    public String getCategoryType(int rowIndex, int columnIndex) { return ((String)this.getValueAt(rowIndex, columnIndex)).trim(); }
    public String getCategoryNumber(int rowIndex, int columnIndex) { return ((String)this.getValueAt(rowIndex, columnIndex)).trim(); }


}
