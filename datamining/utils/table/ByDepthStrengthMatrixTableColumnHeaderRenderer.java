package datamining.utils.table;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.Serializable;

public class ByDepthStrengthMatrixTableColumnHeaderRenderer
extends JLabel
implements TableCellRenderer, Serializable {

    private int fontSize = 0;
    private String [] nodeList;

    public ByDepthStrengthMatrixTableColumnHeaderRenderer(){}
    public ByDepthStrengthMatrixTableColumnHeaderRenderer(int fontSize, String [] nodeList) {
        this.nodeList = nodeList;
        this.fontSize = fontSize;
    }

    public Component getTableCellRendererComponent(
            JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int rowIndex,
            int vColIndex) {
        setText(" " + value.toString());
        setForeground(Color.BLACK);
        setBackground(Color.LIGHT_GRAY);
        setOpaque(true);
        setBorder(BorderFactory.createRaisedSoftBevelBorder());
        setFont(this.fontSize == 0 ? new Font("helvetica", Font.BOLD, 11) : new Font("helvetica", Font.BOLD, this.fontSize));
        setPreferredSize(new Dimension((int)super.getPreferredSize().getWidth(), 24));
        if (vColIndex > 0 && vColIndex < this.nodeList.length) {
            setToolTipText(this.nodeList[vColIndex-1]);
        }
        return this;
    }

    // The following methods override the defaults for performance reasons
    public void validate() {}
    public void revalidate() {}
    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {}
    public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {}

}

