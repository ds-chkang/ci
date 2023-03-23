package datamining.config;

import datamining.utils.system.MyVars;
import datamining.utils.table.MyTableCellRenderer;
import datamining.utils.table.MyTableUtil;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public abstract class MyDirectTable
extends JTable
implements Serializable {

    protected final Font HEAD_FONT = MyVars.tahomaBoldFont12;
    protected final Font CELL_FONT = new Font("Noto Sans", Font.PLAIN, 12);

    public MyDirectTable() {
        super();
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setSelectionBackground(Color.WHITE);
        setSelectionForeground(Color.BLACK);
        getTableHeader().setFont(HEAD_FONT);
    }


    public void setColumnCellRenderer(int columnIndex) {
        getColumnModel().getColumn(columnIndex).setCellRenderer(new MyTableCellRenderer());
    }

    public void setTblRowHeight(int rowHeight) {
        MyTableUtil.setTableRowHeight(this, rowHeight);
    }

    public void setTableRowHeight() {
        MyTableUtil.setTableRowHeight(this, 31);
    }

    public void setTableHeaderHeight() {
        MyTableUtil.setTableHeaderHeight(this, 100, 28);
    }

    public void setTableCellBorderColor() {
        MyTableUtil.setTableCellBorderColor(this, Color.LIGHT_GRAY);
    }

    public void setTableColumnWidth(int colIdx, int width) {
        MyTableUtil.setPreferredTableCellWidth(this, colIdx, width);
    }

    protected abstract void decorate();
}


