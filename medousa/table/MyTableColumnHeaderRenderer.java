package medousa.table;

import medousa.direct.utils.MyDirectGraphVars;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.Serializable;

public class MyTableColumnHeaderRenderer
extends JLabel
implements TableCellRenderer, Serializable {

    private int fontSize = 0;

    public MyTableColumnHeaderRenderer(){}
    public MyTableColumnHeaderRenderer(int fontSize) {
        this.fontSize = fontSize;
    }

    public Component getTableCellRendererComponent(
            JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int rowIndex,
            int vColIndex) {
        // Configure the component with the specified value
        setText(" " + value.toString());
        setForeground(Color.BLACK);
        setBackground(Color.LIGHT_GRAY);
        setOpaque(true);
        setBorder(BorderFactory.createRaisedSoftBevelBorder());
        setFont(this.fontSize == 0 ? new Font("helvetica", Font.BOLD, 12) : new Font("helvetica", Font.BOLD, this.fontSize));
        setPreferredSize(new Dimension((int)super.getPreferredSize().getWidth(), 26));
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
        g2d.setColor(Color.decode("#E4E4E4"));
        g2d.fillRect(x, y, w, h);
        g2d.draw3DRect(x, y, w, h, true);
        g2d.setPaint(Color.decode("#E1E3E7"));
        g2d.setColor(Color.BLACK);
        g2d.setFont(MyDirectGraphVars.f_bold_12);
        g2d.drawString("", 5, 14);
    }

    // The following methods override the defaults for performance reasons
    public void validate() {}
    public void revalidate() {}
    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {}
    public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {}

}