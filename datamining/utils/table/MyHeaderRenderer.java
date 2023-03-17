package datamining.utils.table;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.Serializable;

public class MyHeaderRenderer
implements TableCellRenderer, Serializable {

    private final TableCellRenderer tcr;
    private int rollOverRowIndex = -1;

    public MyHeaderRenderer(JTable table, TableCellRenderer tcr) {
        this.tcr  = tcr;
    }

    @Override public Component getTableCellRendererComponent(
            JTable tbl,
            Object val,
            boolean isS,
            boolean hasF,
            int row,
            int col) {
        JLabel l;
        boolean flg = row == rollOverRowIndex;
        l = (JLabel) tcr.getTableCellRendererComponent(
                        tbl,
                        val,
                        isS,
                        flg ? flg : hasF, row, col);
        l.setOpaque(!flg);
        return l;
    }

}