package datamining.main;

import datamining.utils.system.MyVars;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class MyProgressBarWaitMessage
            extends JPanel
            implements Serializable {

    public static JLabel waitingMsgLabel = new JLabel();
    public static String waitMsg = "Please, wait... It may take some time.";

    public MyProgressBarWaitMessage() {
        this.decorate();
    }

    private void decorate() {
        this.setBorder(BorderFactory.createLoweredBevelBorder());
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 2));
        waitingMsgLabel.setText(this.waitMsg);
        waitingMsgLabel.setPreferredSize(new Dimension(298, 25));
        waitingMsgLabel.setVerticalAlignment(JLabel.TOP);
        waitingMsgLabel.setFont(MyVars.tahomaPlainFont12);
        waitingMsgLabel.setForeground(Color.BLACK);
        this.add(waitingMsgLabel);
    }
}
