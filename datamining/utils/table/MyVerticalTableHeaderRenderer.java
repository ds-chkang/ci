package datamining.utils.table;

import java.awt.*;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicLabelUI;
import javax.swing.table.DefaultTableCellRenderer;

public class MyVerticalTableHeaderRenderer
        extends DefaultTableCellRenderer {

    static final int MARGIN = 2;
    static final Border border =
            BorderFactory.createBevelBorder(BevelBorder.RAISED);
    Dimension d;

    public MyVerticalTableHeaderRenderer() {
        setUI(new VerticalLabelUI());
    }

    @Override
    public Component getTableCellRendererComponent(JTable table,
                                                   Object value, boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected,
                hasFocus, row, column);
        d = super.getPreferredSize();
        d.height = d.width;
        d.width = table.getColumnModel().getColumn(column).getWidth();
        setBackground(Color.LIGHT_GRAY);
        setBorder(border);
        return this;
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        d.height = d.width + MARGIN * 2;
        return d;
    }

    private class VerticalLabelUI extends BasicLabelUI {

        private Rectangle paintTextR;

        @Override
        protected String layoutCL(JLabel label, FontMetrics fontMetrics,
                                  String text, Icon icon, Rectangle viewR, Rectangle iconR,
                                  Rectangle textR) {
            super.layoutCL(label, fontMetrics, text, icon, viewR, iconR, textR);
            paintTextR = textR;
            return text;
        }

        @Override
        protected void paintEnabledText(JLabel l, Graphics g, String s,
                                        int textX, int textY) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.rotate(-Math.PI / 2);
            String [] splitHeaders = s.split(",");
            for (String header : splitHeaders) {
                super.paintEnabledText(l, g2, s, -(textX + MARGIN + d.height),
                        (d.width + textY - paintTextR.y) / 2 - MARGIN);
            }
            g2.dispose();
        }

        @Override
        protected void paintDisabledText(JLabel l, Graphics g, String s,
                                         int textX, int textY) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.rotate(-Math.PI / 2);
            super.paintDisabledText(l, g2, s, -(textX + MARGIN + d.height),
                    (d.width + textY - paintTextR.y) / 2 - MARGIN);
            g2.dispose();
        }
    }
}