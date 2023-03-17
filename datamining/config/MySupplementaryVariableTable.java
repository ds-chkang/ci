package datamining.config;

import datamining.utils.system.MyVars;
import datamining.utils.table.MyVariableBindingColumnEditor;

import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Vector;

public class MySupplementaryVariableTable
extends MyTable {

    private final String [] dataTypes = {" SET DATA TYPE", " BINARY", " INTEGER", " REAL"};
    private final Object [] columns = {" COLUMN", " NO. OF CATEGORIES", " VALUE TYPE"};
    private final Object [][] data = {};

    public MySupplementaryVariableTable() {
        super();
        this.setModel(new DefaultTableModel(data, columns));
        this.decorate();
    }

    @Override
    protected void decorate() {
        this.setRowHeight(30);
        this.setTableCellBorderColor();
        this.setTableHeaderHeight();

        this.setTableColumnWidth(0, 50);
        this.setTableColumnWidth(1, 90);
        this.setTableColumnWidth(2, 50);

        this.setColumnCellRenderer(0);
        this.setColumnCellRenderer(1);
        this.setColumnCellRenderer(2);

        this.setBackground(Color.WHITE);
        this.setForeground(Color.BLACK);
        this.getTableHeader().setFont(HEAD_FONT);
        this.setFont(CELL_FONT);
        this.setFocusable(false);
        this.setSelectionBackground(Color.decode("#D6D9DF"));
    }

    public void update(String [] headers, boolean isUpdate) {
        try {
            this.initialize();
            this.getColumnModel().getColumn(0).setCellEditor(new MyVariableBindingColumnEditor(headers, isUpdate));
            this.getColumnModel().getColumn(2).setCellEditor(new MyVariableBindingColumnEditor(this.dataTypes, isUpdate));
            DefaultTableModel model = (DefaultTableModel) this.getModel();
            int rowCnt = 0;
            for (int i = 0; i < headers.length; i++) {
                if ((headers[i].toUpperCase().contains("NO.") && headers[i].length() == 3) ||
                    headers[i].toUpperCase().contains("OBJECT") ||
                    headers[i].toUpperCase().contains("TRANSACTION") ||
                    headers[i].toUpperCase().contains("FROM") ||
                    headers[i].toUpperCase().contains("TO")) continue;
                Vector<Object> emptyRowValues = new Vector<>();
                model.addRow(emptyRowValues);
                model.setValueAt("   SET COLUMN", rowCnt, 0);
                model.setValueAt(" ", rowCnt, 1);
                model.setValueAt("   SET VALUE TYPE", rowCnt++, 2);
            }
        } catch (Exception ex) {ex.printStackTrace();}
    }

    private void initialize() {
        DefaultTableModel dm = (DefaultTableModel) this.getModel();
        for (int i = this.getRowCount()-1; i >= 0; i--) {dm.removeRow(i);}
    }

    public void isSupplementaryOn() {
        for (int i=0; i < this.getRowCount(); i++) {
            if (!this.getValueAt(i, 0).toString().contains("SET") && !this.getValueAt(i, 2).toString().contains("SET")) {
                MyVars.isSupplementaryOn = true;
                break;
            }
        }
    }

    public String getVariableName(int rowIndex, int columnIndex) { return getValueAt(rowIndex, columnIndex).toString().trim(); }
    public String getCategoryType(int rowIndex, int columnIndex) { return getValueAt(rowIndex, columnIndex).toString().trim(); }
    public String getCategoryNumber(int rowIndex, int columnIndex) { return getValueAt(rowIndex, columnIndex).toString().trim(); }
}
