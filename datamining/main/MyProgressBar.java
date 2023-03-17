package datamining.main;

import datamining.utils.system.MyVars;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

public class MyProgressBar
extends JDialog
implements Serializable {

    private JProgressBar jp;
    private JPanel waitMsgPanel = new JPanel();
    private int currentValue = 0;

    public MyProgressBar() {}
    public MyProgressBar(boolean isInFinite) {
        try {
            if (isInFinite) {
                this.jp = new JProgressBar(JProgressBar.HORIZONTAL);
                this.jp.setStringPainted(false);
                this.jp.setIndeterminate(true);
            } else {
                this.jp = new JProgressBar(JProgressBar.HORIZONTAL, 0, 100);
                this.jp.setStringPainted(true);
                this.jp.setIndeterminate(false);
            }
            this.jp.setFont(MyVars.tahomaBoldFont12);
            this.jp.setBounds(new Rectangle(5, 15, 290, 30));
            JButton cancel = new JButton("Cancel");
            cancel.setFocusPainted(false);
            cancel.setBounds(new Rectangle(100, 65, 100, 30));
            cancel.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setVisible(false);
                    dispose();
                }
            });
            this.waitMsgPanel.setBorder(BorderFactory.createEtchedBorder());
            this.waitMsgPanel.setLayout(new BorderLayout());
            this.waitMsgPanel.add(new MyProgressBarWaitMessage(), BorderLayout.CENTER);
            this.waitMsgPanel.setBounds(new Rectangle(1, 120, 298, 27));
            this.setLayout(null);
            this.setSize(300, 150);
            this.setUndecorated(true);
            this.add(this.jp);
            this.add(this.waitMsgPanel);
            this.add(cancel);
            this.setResizable(false);
            this.setAlwaysOnTop(true);
            this.setFocusable(false);
            this.setVisible(true);
        } catch (Exception ex) {}
    }

    public MyProgressBar(boolean isInFinite, JFrame main) {
        try {
            if (isInFinite) {
                this.jp = new JProgressBar(JProgressBar.HORIZONTAL);
                this.jp.setStringPainted(false);
                this.jp.setIndeterminate(true);
            } else {
                this.jp = new JProgressBar(JProgressBar.HORIZONTAL, 0, 100);
                this.jp.setStringPainted(true);
                this.jp.setIndeterminate(false);
            }
            this.jp.setFont(MyVars.tahomaBoldFont12);
            this.jp.setBounds(new Rectangle(5, 15, 290, 30));
            JButton cancel = new JButton("Cancel");
            cancel.setFocusPainted(false);
            cancel.setBounds(new Rectangle(100, 65, 100, 30));
            cancel.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setVisible(false);
                    dispose();
                }
            });
            this.waitMsgPanel.setBorder(BorderFactory.createEtchedBorder());
            this.waitMsgPanel.setLayout(new BorderLayout());
            this.waitMsgPanel.add(new MyProgressBarWaitMessage(), BorderLayout.CENTER);
            this.waitMsgPanel.setBounds(new Rectangle(1, 120, 298, 27));
            this.setLayout(null);
            this.setSize(300, 150);
            this.setUndecorated(true);
            this.add(this.jp);
            this.add(this.waitMsgPanel);
            this.add(cancel);
            this.setResizable(false);
            this.setAlwaysOnTop(true);
            this.setFocusable(false);
            this.setVisible(true);
        } catch (Exception ex) {}
    }

    public void updateValue(long currentValue, long totalValue) {
        final int currentPercent = (int)(currentValue*100.0/totalValue);
        if (currentPercent < 48) {
            this.jp.setValue(currentPercent);
        } else {
            this.jp.setValue(currentPercent);
            this.setForeground(Color.WHITE);
        }
        this.currentValue = currentPercent;
    }

    public int getCurrentValue() { return this.currentValue; }
    public void dispose() {
        try {
            this.updateValue(100, 100);
            Thread.sleep(500);
            this.setVisible(false);
        } catch (Exception ex) {ex.printStackTrace();}
    }

}

