package medousa.sequential.graph;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import java.awt.*;

public class MyComboBoxTooltipRenderer
extends BasicComboBoxRenderer {

    private String [] tooltips;

    public MyComboBoxTooltipRenderer(String [] tooltips) {
        this.tooltips = tooltips;
        this.setFocusable(false);
    }

    public Component getListCellRendererComponent(
        JList list,
        Object value,
        int index,
        boolean isSelected,
        boolean cellHasFocus) {
        try {
            if (isSelected) {
                setBackground(Color.WHITE);
                setForeground(Color.BLACK);
                if (-1 < index) {
                    list.setToolTipText(tooltips[index]);
                } else {

                }
            } else {
                setBackground(Color.WHITE);
                setForeground(Color.BLACK);
            }
            setFont(list.getFont());
            setText((value == null) ? "" : value.toString());
            return this;
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
        return null;
    }
}
