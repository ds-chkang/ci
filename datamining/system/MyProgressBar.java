package datamining.system;


import datamining.system.MyVars;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by changhee on 17. 7. 22.
 */
public class MyProgressBar
extends JFrame {

    int previousPercent;
    private JProgressBar jp;

    public MyProgressBar() {}

    public MyProgressBar(boolean isInFinite) {
        //Create a border
        JPanel pbPanel = new JPanel();
        if (isInFinite) {
            this.jp = new JProgressBar(JProgressBar.HORIZONTAL);
            this.jp.setStringPainted(false);
            this.jp.setIndeterminate(true);
        } else {
            this.jp = new JProgressBar(JProgressBar.HORIZONTAL, 0, 100);
            this.jp.setStringPainted(true);
            this.jp.setIndeterminate(false);
        }
        this.jp.setFont(MyVars.f_bold_12);
        this.jp.setBounds(new Rectangle(20,20,260,30));
        JButton cancel = new JButton("Cancel");
        cancel.setFocusPainted(false);
        cancel.setBounds(new Rectangle(100, 70, 100, 30));
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        this.setLayout(null);
        this.setSize(300,120);
        this.setUndecorated(true);
        this.add(this.jp);
        this.add(cancel);
        this.setResizable(false);
        this.setAlwaysOnTop(true);
        this.setFocusable(false);
        this.setVisible(true);
    }

    public void updateValue(long currentValue, long totalValue) {
        final int currentPercent = (int) (currentValue * 100.0 / totalValue);
        if (this.previousPercent < currentPercent) {
            this.previousPercent = currentPercent;
            this.jp.setValue(currentPercent);
            try {
                Thread.sleep(3);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public int getCurrentValue() { return this.jp.getValue(); }

    public void dispose() {
        try {
            this.updateValue(100, 100);
            Thread.sleep(700);
            this.setVisible(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
