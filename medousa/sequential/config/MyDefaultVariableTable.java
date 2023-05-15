package medousa.sequential.config;

import medousa.sequential.graph.stats.MyGrayCellRenderer;
import medousa.sequential.utils.MySequentialGraphVars;
import medousa.table.MyVariableBindingColumnEditor;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.io.Serializable;

public class MyDefaultVariableTable
extends MyTable
implements Serializable {

    private String columns[] = {" COLUMN", " TARGET COLUMN", " VALUE TYPE"};
    private String data[][] = {
            {"   OBJECT ID", "   SELECT COLUMN", "  STRING"},
            {"   TRANSACTION ID", "   SELECT COLUMN",  "  STRING"},
            {"   ITEM ID", "   SELECT COLUMN", "  STRING"},
            {"   ITEM NAME", "   SELECT COLUMN", "  STRING"},
            {"   TRANSACTION TIME", "   SELECT COLUMN", "  STRING"}};

    public MyDefaultVariableTable() {
        super();
        this.setModel(new DefaultTableModel(data, columns));
        this.decorate();
    }

    @Override protected void decorate() {
        this.setTableRowHeight();
        this.setTableCellBorderColor();
        this.setTableHeaderHeight();

        this.setTableColumnWidth(0, 75);
        this.setTableColumnWidth(1, 75);
        this.setTableColumnWidth(2, 65);

        this.setColumnCellRenderer(0);
        this.setColumnCellRenderer(1);
        this.setColumnCellRenderer(2);

        this.setForeground(Color.BLACK);
        this.getTableHeader().setFont(HEAD_FONT);
        this.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.setSelectionBackground(Color.decode("#D6D9DF"));
    }

    public void updateTable(String [] headers, boolean isUpdate) {
        this.getColumnModel().getColumn(1).setCellEditor(new MyVariableBindingColumnEditor(headers, isUpdate));
    }

    public void isTimeVariableOn() {
        if (this.getValueAt(4, 1).toString().contains("TRANSACTION TIME")) {
            MySequentialGraphVars.isTimeOn = true;
        }
    }
}
