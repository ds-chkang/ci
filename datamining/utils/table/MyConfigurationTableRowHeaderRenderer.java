package datamining.utils.table;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class MyConfigurationTableRowHeaderRenderer
extends JPanel
implements TableCellRenderer {

    private final Font FONT =
            new Font("helvetica", Font.PLAIN, 11);

    public MyConfigurationTableRowHeaderRenderer() {
        this.setLayout(new BorderLayout(0,5));
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int column) {
        JLabel itemLabel = new JLabel(value.toString());
        itemLabel.setVerticalAlignment(JLabel.CENTER);
        itemLabel.setHorizontalAlignment(JLabel.CENTER);
        itemLabel.setBorder(BorderFactory.createRaisedBevelBorder());
        itemLabel.setFont(FONT);
        this.add(itemLabel);
        return this;
    }

    @Override
    public void paintComponent(Graphics g) {}

}