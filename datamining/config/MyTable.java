package datamining.config;

import datamining.utils.system.MyVars;
import datamining.utils.table.MyTableCellRenderer;
import datamining.utils.table.MyTableUtil;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public abstract class MyTable
extends JTable
implements Serializable {

    // set table header font.
    protected final Font HEAD_FONT = MyVars.tahomaBoldFont12;
    protected final Font CELL_FONT = new Font("Noto Sans", Font.PLAIN, 12);

    // set table cell renderer.
    /**
     * default constructor.
     */
    public MyTable() {
        super();
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setSelectionBackground(Color.WHITE);
        setSelectionForeground(Color.BLACK);
        getTableHeader().setFont(HEAD_FONT);
    }

    /**
     * sets column header renderer.
     * @param columnIndex
     */
    public void setColumnCellRenderer(int columnIndex) {getColumnModel().getColumn(columnIndex).setCellRenderer(new MyTableCellRenderer());}

    /**
     * set table row height with default height.
     */
    public void setTableRowHeight() {
        MyTableUtil.setTableRowHeight(this, 31);
    }

    /**
     * set table header height with default width and height.
     */
    public void setTableHeaderHeight() {
        MyTableUtil.setTableHeaderHeight(this, 100, 28);
    }

    /**
     * set table cell border color.
     */
    public void setTableCellBorderColor() {
        MyTableUtil.setTableCellBorderColor(this, Color.LIGHT_GRAY);
    }

    /**
     * set table column width by index.
     * @param colIdx
     * @param width
     */
    public void setTableColumnWidth(int colIdx, int width) {MyTableUtil.setPreferredTableCellWidth(this, colIdx, width);}

    protected abstract void decorate();
}


