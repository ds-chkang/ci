package datamining.main;

import datamining.utils.message.MyMessageUtil;
import datamining.utils.system.MyVars;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MyHeaderDelimiter
extends JDialog
implements ActionListener {

    private JTextField headerDelimiterTxt = new JTextField();
    private JButton headerDelimiterCmd = new JButton("   OK   ");

    public MyHeaderDelimiter() {
        super(MyVars.main);
        this.decorate();
    }

    private void decorate() {
        this.setTitle("Set header delimiter");
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(350, 120));

        this.headerDelimiterTxt.setFont(new Font("Arial", Font.BOLD, 30));
        this.headerDelimiterTxt.setPreferredSize(new Dimension(200, 45));

        JPanel headerDelimiterPanel = new JPanel();
        headerDelimiterPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel headerDelimiterLabel = new JLabel("Header Delimiter: ");
        headerDelimiterLabel.setFont(MyVars.f_pln_14);
        headerDelimiterPanel.add(headerDelimiterLabel);
        headerDelimiterPanel.add(this.headerDelimiterTxt);

        JPanel cmdPanel = new JPanel();
        this.headerDelimiterCmd.addActionListener(this);
        cmdPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        cmdPanel.add(this.headerDelimiterCmd);

        this.getContentPane().add(headerDelimiterPanel, BorderLayout.CENTER);
        this.getContentPane().add(cmdPanel, BorderLayout.SOUTH);
        this.setResizable(false);
        this.setAlwaysOnTop(true);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                MyMessageUtil.showErrorMsg("Set the header delimiter.");
            }
        });

        this.pack();
        this.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {

    }

}
