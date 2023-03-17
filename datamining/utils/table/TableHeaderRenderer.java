package datamining.utils.table;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.Serializable;

public class TableHeaderRenderer
implements TableCellRenderer, Serializable {
    private final TableCellRenderer baseRenderer;

    public TableHeaderRenderer(TableCellRenderer baseRenderer) {
        this.baseRenderer = baseRenderer;
    }
    @Override
    public Component getTableCellRendererComponent(
            JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int column) {
        JComponent c = (JComponent)baseRenderer.
                getTableCellRendererComponent(
                        table,
                        value,
                        isSelected,
                        hasFocus,
                        row,
                        column);
        c.setBackground(Color.LIGHT_GRAY);
        Border raisedBorder = BorderFactory.createRaisedSoftBevelBorder();
        c.setBorder(raisedBorder);
        return c;
    }

}