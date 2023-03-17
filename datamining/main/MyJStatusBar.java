package datamining.main;

import javax.swing.*;
import java.awt.*;

public class MyJStatusBar extends JPanel {

    protected JPanel leftPanel;
    protected JPanel rightPanel;
    protected JLabel emptyLabel;
 
    public MyJStatusBar() {
        decorate();
    }
 
    protected void decorate() {
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(getWidth(), 22));

        this.leftPanel = new JPanel(new BorderLayout(5,2));
        this.leftPanel.setOpaque(false);
        this.add(leftPanel, BorderLayout.CENTER);

        this.rightPanel = new JPanel(new FlowLayout(
            FlowLayout.TRAILING, 5, 3));
        this.rightPanel.setOpaque(false);
        this.add(rightPanel, BorderLayout.EAST);
    }
 
    public void setLeftComponentToWEST(JComponent component) {
        leftPanel.add(component, BorderLayout.WEST);
    }

 
    public void addRightComponent(JComponent component) {
        JPanel panel = new JPanel(new FlowLayout(
            FlowLayout.LEADING, 5, 1));
        panel.add(component);
        this.rightPanel.add(panel);
    }
 
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
 
        int y = 0;
        g.setColor(new Color(156, 154, 140));
        g.drawLine(0, y, getWidth(), y);
        y++;
 
        g.setColor(new Color(196, 194, 183));
        g.drawLine(0, y, getWidth(), y);
        y++;
 
        g.setColor(new Color(218, 215, 201));
        g.drawLine(0, y, getWidth(), y);
        y++;
 
        g.setColor(new Color(233, 231, 217));
        g.drawLine(0, y, getWidth(), y);
 
        y = getHeight() - 3;
 
        g.setColor(new Color(233, 232, 218));
        g.drawLine(0, y, getWidth(), y);
        y++;
 
        g.setColor(new Color(233, 231, 216));
        g.drawLine(0, y, getWidth(), y);
        y++;
 
        g.setColor(new Color(221, 221, 220));
        g.drawLine(0, y, getWidth(), y);
    }
 
}