package medousa.sequential.config;

import medousa.table.MyVariableBindingColumnEditor;

import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Vector;

public class MyNodeLabelTable
extends MyTable {


    private final String [] dataTypes = {" YES", " NO"};
    private final String [] valueTypes = {" VALUE TYPE", " AVERAGE", " SUM", " DISTINCT"};
    private final Object [] columns = {" COLUMN", " NODE LABEL", " LABEL TYPE"};
    private final Object [][] data = {};

    public MyNodeLabelTable() {
        super();
        this.setModel(new DefaultTableModel(data, columns));
        this.decorate();
    }

    @Override protected void decorate() {
        this.setRowHeight(30);
        this.setTableCellBorderColor();
        this.setTableHeaderHeight();

        this.setTableColumnWidth(0, 50);
        this.setTableColumnWidth(1, 90);
        this.setTableColumnWidth(2, 50);

        this.setFont(CELL_FONT);
        this.setFocusable(false);
        this.setBackground(Color.WHITE);
        this.setForeground(Color.BLACK);
        this.getTableHeader().setFont(HEAD_FONT);
        this.setSelectionBackground(Color.ORANGE);
    }

    public void update(String [] headers, boolean isUpdate) {
        this.initialize();
        this.getColumnModel().getColumn(0).setCellEditor(new MyVariableBindingColumnEditor(headers, isUpdate));
        this.getColumnModel().getColumn(1).setCellEditor(new MyVariableBindingColumnEditor(this.dataTypes, isUpdate));
        this.getColumnModel().getColumn(2).setCellEditor(new MyVariableBindingColumnEditor(this.valueTypes, isUpdate));
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
            model.setValueAt("  COLUMN", rowCnt, 0);
            model.setValueAt("  NO", rowCnt, 1);
            model.setValueAt("  SET LABEL TYPE", rowCnt++, 2);
        }
    }

    private void initialize() {
        DefaultTableModel dm = (DefaultTableModel) this.getModel();
        for (int i = this.getRowCount()-1; i >= 0; i--) {dm.removeRow(i);}
    }

}
