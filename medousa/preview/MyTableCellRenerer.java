package medousa.preview;

import medousa.direct.utils.MyDirectGraphVars;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class MyTableCellRenerer
extends DefaultTableCellRenderer {

    private static Color BOOKED_COLOR = Color.RED;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        } else {
            setBackground(table.getBackground());
            setForeground(table.getForeground());
        }
            // Update the details based on the room properties

        return this;
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        int x = 0;
        int y = 0;
        int w = getWidth();
        int h = getHeight();
        g2d.setColor(Color.decode("#E4E4E4"));
        g2d.fillRect(x, y, w, h);
        g2d.draw3DRect(x, y, w, h, true);
        g2d.setPaint(Color.decode("#E1E3E7"));
        g2d.setColor(Color.BLACK);
        g2d.setFont(MyDirectGraphVars.f_bold_12);
        g2d.drawString("", 5, 14);
    }


}
