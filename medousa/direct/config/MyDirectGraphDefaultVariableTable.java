package medousa.direct.config;

import medousa.table.MyVariableBindingColumnEditor;
import medousa.direct.utils.MyDirectGraphVars;

import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.Serializable;

public class MyDirectGraphDefaultVariableTable
extends MyDirectGraphTable
implements Serializable {

    private Object columns[] = {" DEFAULT VARIABLE", " DATA VARIABLE", " DATA TYPE"};
    private Object data[][] = {
            {"  OBJECT ID", "  SELECT COLUMN", "  STRING"},
            {"  FROM ITEM", "  SELECT COLUMN", "  STRING"},
            {"  TO ITEM", "  SELECT COLUMN", "  STRING"}};

    public MyDirectGraphDefaultVariableTable() {
        super();
        this.setModel(new DefaultTableModel(data, columns));
        this.decorate();
    }

    @Override
    protected void decorate() {
        this.setTableRowHeight();
        this.setTableCellBorderColor();
        this.setTableHeaderHeight();

        this.setColumnCellRenderer(0);
        this.setColumnCellRenderer(1);
        this.setColumnCellRenderer(2);

        this.setTableColumnWidth(0, 120);
        this.setTableColumnWidth(1, 90);

        this.setForeground(Color.BLACK);
        this.getTableHeader().setFont(HEAD_FONT);
        this.setFont(MyDirectGraphVars.tahomaPlainFont12);
        this.setSelectionBackground(Color.decode("#D6D9DF"));
    }

    public void update(String[] headers, boolean isUpdate) {
        this.getColumnModel().getColumn(1).setCellEditor(new MyVariableBindingColumnEditor(headers, isUpdate));
        this.repaint();
        this.revalidate();
    }
}
