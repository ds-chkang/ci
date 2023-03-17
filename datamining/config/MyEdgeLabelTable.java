package datamining.config;

import datamining.utils.table.MyVariableBindingColumnEditor;

import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Vector;

public class MyEdgeLabelTable
extends MyTable {

    private final String [] isWeights = {" YES", " NO"};
    private final String [] calculationTypes = {" LABEL TYPE", " SUM", " AVERAGE", " DISTINCT"};
    private final Object [] columns = {" COLUMN", " EDGE LABEL", " LABEL TYPE"};
    private final Object [][] data = {};

    public MyEdgeLabelTable() {
        super();
        this.setModel(new DefaultTableModel(data, columns));
        this.decorate();
    }

    @Override
    protected void decorate() {
        this.setRowHeight(29);
        this.setTableCellBorderColor();
        this.setTableHeaderHeight();

        this.setTableColumnWidth(0, 75);
        this.setTableColumnWidth(1, 75);
        this.setTableColumnWidth(2, 65);

        this.setColumnCellRenderer(0);
        this.setColumnCellRenderer(1);
        this.setColumnCellRenderer(2);

        this.setBackground(Color.WHITE);
        this.setForeground(Color.BLACK);
        this.setFocusable(false);
        this.getTableHeader().setFont(HEAD_FONT);
        this.setFont(CELL_FONT);
        this.setSelectionBackground(Color.ORANGE);
    }

    public void update(String [] headers, boolean isUpdate) {
        this.initialize();
        this.getColumnModel().getColumn(0).setCellEditor(new MyVariableBindingColumnEditor(headers, isUpdate));
        this.getColumnModel().getColumn(1).setCellEditor(new MyVariableBindingColumnEditor(this.isWeights, isUpdate));
        this.getColumnModel().getColumn(2).setCellEditor(new MyVariableBindingColumnEditor(this.calculationTypes, isUpdate));

        DefaultTableModel model = (DefaultTableModel) this.getModel();
        int rowCnt = 0;
        for (int i = 0; i < headers.length; i++) {
            if ((headers[i].contains("NO.") && headers[i].length() == 3) ||
                 headers[i].contains("OBJECT ID") ||
                 headers[i].contains("TRANSACTION ID") ||
                 headers[i].contains("ITEM ID") ||
                 headers[i].contains("ITEM NAME")) continue;
            Vector<Object> emptyRowValues = new Vector<>();
            model.addRow(emptyRowValues);
            model.setValueAt(" SELECT COLUMN", rowCnt, 0);
            model.setValueAt(" NO", rowCnt, 1);
            model.setValueAt(" LABEL TYPE", rowCnt++, 2);
        }
    }

    private void initialize() {
        DefaultTableModel dm = (DefaultTableModel) this.getModel();
        for (int i = this.getRowCount()-1; i >= 0; i--) {
            dm.removeRow(i);
        }
    }
}
