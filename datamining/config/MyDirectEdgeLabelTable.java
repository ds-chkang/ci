package datamining.config;

import datamining.utils.table.MyVariableBindingColumnEditor;

import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.Vector;

public class MyDirectEdgeLabelTable
extends MyDirectTable {

    public String [] headers;
    public File dataFile;
    private final String [] dataTypes = {" YES", " NO"};
    private final String [] valueTypes = {" SELECT DATA TYPE", " CATEGORY"};
    private final Object [] columns = {" COLUMN", " EDGE LABEL", " LABEL TYPE"};
    private final Object [][] data = {};

    public MyDirectEdgeLabelTable() {
        super();
        this.setModel(new DefaultTableModel(data, columns));
        this.decorate();
    }

    @Override
    protected void decorate() {
        this.setRowHeight(28);
        this.setTableCellBorderColor();
        this.setTableHeaderHeight();

        this.setColumnCellRenderer(0);
        this.setColumnCellRenderer(1);
        this.setColumnCellRenderer(2);

        this.setTableColumnWidth(0, 120);
        this.setTableColumnWidth(1, 90);

        this.setFocusable(false);
        this.setFont(CELL_FONT);
        this.setBackground(Color.WHITE);
        this.setForeground(Color.BLACK);
        this.getTableHeader().setFont(HEAD_FONT);
        this.setSelectionBackground(Color.ORANGE);
    }

    public void update(String [] headers, boolean isUpdate) {
        if (this.getRowCount() > 0) {initialize();}

        this.getColumnModel().getColumn(0).setCellEditor(new MyVariableBindingColumnEditor(headers, isUpdate));
        this.getColumnModel().getColumn(1).setCellEditor(new MyVariableBindingColumnEditor(this.dataTypes, isUpdate));
        this.getColumnModel().getColumn(2).setCellEditor(new MyVariableBindingColumnEditor(this.valueTypes, isUpdate));

        DefaultTableModel model = (DefaultTableModel) this.getModel();
        int rowCnt = 0;
        for (int i = 0; i < headers.length; i++) {
            Vector<Object> emptyRowValues = new Vector<>();
            model.addRow(emptyRowValues);
            model.setValueAt("  SELECT COLUMN", rowCnt, 0);
            model.setValueAt("  NO", rowCnt, 1);
            model.setValueAt("  LABEL TYPE", rowCnt++, 2);
        }
    }

    private void initialize() {
        DefaultTableModel model = (DefaultTableModel)this.getModel();
        for (int i=this.getRowCount()-1; i >= 0; i--) {model.removeRow(i);}
    }

}
