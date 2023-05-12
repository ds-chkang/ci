package medousa.table;

import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;



/**
 * GroupableTableHeader
 *
 * @version 1.1 2010/10/23
 * @author Nobuo Tamemasa (modified by Q)
 */

public class MyGroupableTableHeader extends JTableHeader {
    private static final String uiClassID = "GroupableTableHeaderUI";
    protected Vector columnGroups = null;

    public MyGroupableTableHeader(TableColumnModel model) {
        super(model);
        setReorderingAllowed(false);
    }

    public void setReorderingAllowed(boolean b) {
        reorderingAllowed = false;
    }

    public void addColumnGroup(MyColumnGroup g) {
        if (columnGroups == null) {
            columnGroups = new Vector();
        }
        columnGroups.addElement(g);
    }

    public Enumeration getColumnGroups(TableColumn col) {
        if (columnGroups == null) return null;
        Enumeration en = columnGroups.elements();
        while (en.hasMoreElements()) {
            MyColumnGroup cGroup = (MyColumnGroup)en.nextElement();
            Vector v_ret = (Vector)cGroup.getColumnGroups(col,new Vector());
            if (v_ret != null) {
                return v_ret.elements();
            }
        }
        return null;
    }

    @Override
    public void updateUI() {
        setUI(new MyGroupableTableHeaderUI());

        TableCellRenderer tablecellrenderer = getDefaultRenderer();
        if(tablecellrenderer instanceof Component)
            SwingUtilities.updateComponentTreeUI((Component)tablecellrenderer);

    }

    public void setColumnMargin() {
        if (columnGroups == null) return;
        int columnMargin = getColumnModel().getColumnMargin();
        Enumeration en = columnGroups.elements();
        while (en.hasMoreElements()) {
            MyColumnGroup cGroup = (MyColumnGroup)en.nextElement();
            cGroup.setColumnMargin(0/*columnMargin*/);
        }
    }

}

