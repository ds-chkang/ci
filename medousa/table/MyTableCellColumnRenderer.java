package medousa.table;

import medousa.direct.utils.MyDirectGraphVars;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class MyTableCellColumnRenderer
extends DefaultTableCellRenderer {

    int fontSize = 0;
    String value = "";
    public MyTableCellColumnRenderer() { super(); }


    public synchronized Component getTableCellRendererComponent(
            JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int rowIndex,
            int vColIndex) {
        // Configure the component with the specified value
        this.value = value.toString();
        setText(" " + value.toString());
        setForeground(Color.BLACK);
        setBackground(Color.WHITE);
        setOpaque(true);
        //setBorder(BorderFactory.createRaisedSoftBevelBorder());
        setFont(MyDirectGraphVars.tahomaBoldFont11);
        setPreferredSize(new Dimension((int)super.getPreferredSize().getWidth(), 24));
        setToolTipText((String)value);
        return this;
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        int x = 0;
        int y = 0;
        int w = getWidth();
        int h = getHeight();
        g2d.setColor(Color.decode("#D6D9DF"));
        g2d.fillRect(x, y, w, h);
        g2d.draw3DRect(x, y, w, h, true);
        g2d.setPaint(Color.RED);//Color.decode("#E1E3E7"));
        g2d.setColor(Color.BLACK);
        g2d.setFont(MyDirectGraphVars.tahomaBoldFont12);
        g2d.drawString(this.value, 5, 14);
    }
}