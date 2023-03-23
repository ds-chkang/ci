package datamining.config;

import datamining.utils.table.MyVariableBindingColumnEditor;

import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.Vector;

public class MyDirectNodeLabelTable
extends MyDirectTable {

    public String [] headers;
    public File dataFile;
    private final String [] dataTypes = {" YES", " NO"};
    private final String [] valueTypes = {" SELECT DATA TYPE", " CATEGORY"};
    private final Object [] columns = {" PROPERTY", " COLUMN", " NODE LABEL", " LABEL TYPE"};
    private final Object [][] data = {};

    public MyDirectNodeLabelTable() {
        super();
        this.setModel(new DefaultTableModel(data, columns));
        this.decorate();
    }

    @Override
    protected void decorate() {
        this.setRowHeight(29);
        this.setTableCellBorderColor();
        this.setTableHeaderHeight();

        this.setColumnCellRenderer(0);
        this.setColumnCellRenderer(1);
        this.setColumnCellRenderer(2);
        this.setColumnCellRenderer(3);

        this.setTableColumnWidth(0, 50);
        this.setTableColumnWidth(1, 90);
        this.setTableColumnWidth(2, 50);

        this.setFocusable(false);
        this.setFont(CELL_FONT);
        this.setBackground(Color.WHITE);
        this.setForeground(Color.BLACK);
        this.getTableHeader().setFont(HEAD_FONT);
        this.setSelectionBackground(Color.ORANGE);
    }

    public void update(String [] headers, boolean isUpdate) {
        if (this.getRowCount() > 0) {initialize();}
        this.getColumnModel().getColumn(1).setCellEditor(new MyVariableBindingColumnEditor(headers, isUpdate));
        this.getColumnModel().getColumn(2).setCellEditor(new MyVariableBindingColumnEditor(this.dataTypes, isUpdate));
       // this.getColumnModel().getColumn(3).setCellEditor(new MyVariableBindingColumnEditor(this.valueTypes, isUpdate));
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
            model.setValueAt("  SELECT COLUMN", rowCnt, 1);
            model.setValueAt("  NO", rowCnt, 2);
            model.setValueAt("  CATEGORY", rowCnt++, 3);
        }
        this.setTableRowHeight();
        this.repaint();
        this.revalidate();
    }

    private void initialize() {
        DefaultTableModel model = (DefaultTableModel)this.getModel();
        for (int i=this.getRowCount()-1; i >= 0; i--) {
            model.removeRow(i);
        }
    }
}
