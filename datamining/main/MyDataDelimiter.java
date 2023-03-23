package datamining.main;

import datamining.utils.message.MyMessageUtil;
import datamining.utils.system.MyVars;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MyDataDelimiter
extends JDialog
implements ActionListener {

    private JTextField dataDelimiterTxt = new JTextField();
    private JButton dataDelimiterCmd = new JButton("   OK   ");

    public MyDataDelimiter() {
        super(MyVars.main);
        this.decorate();
    }

    private void decorate() {
        this.setTitle("Set data delimiter");
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(330, 120));
        this.dataDelimiterTxt.setFont(new Font("Arial", Font.BOLD, 24));
        this.dataDelimiterTxt.setPreferredSize(new Dimension(200, 34));
        JPanel headerDelimiterPanel = new JPanel();
        headerDelimiterPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel headerDelimiterLabel = new JLabel("Data Delimiter: ");
        headerDelimiterPanel.add(headerDelimiterLabel);
        headerDelimiterPanel.add(this.dataDelimiterTxt);
        JPanel cmdPanel = new JPanel();
        this.dataDelimiterCmd.addActionListener(this);
        cmdPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        cmdPanel.add(this.dataDelimiterCmd);
        this.getContentPane().add(headerDelimiterPanel, BorderLayout.CENTER);
        this.getContentPane().add(cmdPanel, BorderLayout.SOUTH);
        this.setResizable(false);
        this.setAlwaysOnTop(true);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                MyMessageUtil.showErrorMsg("Set the data delimiter.");
            }
        });
        this.pack();
        this.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {

    }
}
