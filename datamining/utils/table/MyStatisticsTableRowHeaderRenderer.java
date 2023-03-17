package datamining.utils.table;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class MyStatisticsTableRowHeaderRenderer
extends JPanel
implements TableCellRenderer {

    private final Font FONT =
            new Font("helvetica", Font.PLAIN, 11);

    public MyStatisticsTableRowHeaderRenderer() {
        this.setBackground(Color.LIGHT_GRAY);
        this.setBorder(BorderFactory.createEmptyBorder());
        this.setLayout(new BorderLayout(0,0));
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int column) {
        this.removeAll();
        JLabel itemLabel = new JLabel("  "+value.toString());
        itemLabel.setVerticalAlignment(JLabel.CENTER);
        itemLabel.setFont(FONT);
        this.add(itemLabel);
        return this;
    }

    @Override
    public void paintComponent(Graphics g) {}

}