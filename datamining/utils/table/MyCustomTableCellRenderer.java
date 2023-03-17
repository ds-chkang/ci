package datamining.utils.table;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class MyCustomTableCellRenderer
extends DefaultTableCellRenderer {

    public MyCustomTableCellRenderer() { super(); }

    public Component getTableCellRendererComponent(
            JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int column) {

        Component rendererComp =
                super.
                        getTableCellRendererComponent(
                                table,
                                value,
                                isSelected,
                                hasFocus,
                                row,
                                column);
        rendererComp.setBackground(Color.decode("#ffc801"));
        if (column != 0) {
            rendererComp.setForeground(Color.BLACK);
            return rendererComp;
        } else {
            rendererComp.setForeground(Color.BLACK);
            return rendererComp;
        }

    }

}
