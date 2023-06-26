package medousa.preview;

import medousa.direct.utils.MyDirectGraphVars;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyDataTimeIntervalPreviewControl
extends JPanel
implements ActionListener{

    private JTable dataTbl;
    private MyDataPreviewChart dataPreviewChart;
    private JComboBox targetVariable;
    private JComboBox value2;
    private JComboBox value1;
    private JComboBox timeVariable;
    private JComboBox objectVariable;
    private JLabel result = new JLabel();

    private JButton showBtn = new JButton("SHOW");

    public MyDataTimeIntervalPreviewControl() {}


    public void decorate(String [] columns, JTable dataTable) {
        try {
            removeAll();
            setLayout(new BorderLayout(3,3));

            if (dataTbl == null) {dataTbl = dataTable;}
            if (this.dataPreviewChart == null) {this.dataPreviewChart = dataPreviewChart;}

            showBtn.setFont(MyDirectGraphVars.tahomaPlainFont12);
            showBtn.setFocusable(false);
            showBtn.setBackground(Color.WHITE);
            showBtn.addActionListener(this);

            targetVariable = new JComboBox();
            targetVariable.addItem("");
            for (int i=1; i < columns.length; i++) {
                targetVariable.addItem(columns[i]);
            }
            targetVariable.setBackground(Color.WHITE);
            targetVariable.setFont(MyDirectGraphVars.tahomaPlainFont11);
            targetVariable.setFocusable(false);
            targetVariable.addActionListener(new ActionListener() {
                @Override public void actionPerformed(ActionEvent e) {
                    new Thread(new Runnable() {
                        @Override public void run() {
                            try {
                                if (targetVariable.getSelectedIndex() != 0) {

                                }
                            } catch (Exception ex) {

                            }
                        }
                    }).start();
                }
            });

            objectVariable = new JComboBox();
            objectVariable.addItem("");
            for (int i=1; i < columns.length; i++) {
                objectVariable.addItem(columns[i]);
            }
            objectVariable.setBackground(Color.WHITE);
            objectVariable.setFont(MyDirectGraphVars.tahomaPlainFont11);
            objectVariable.setFocusable(false);
            objectVariable.addActionListener(new ActionListener() {
                @Override public void actionPerformed(ActionEvent e) {
                    new Thread(new Runnable() {
                        @Override public void run() {
                            try {
                                if (objectVariable.getSelectedIndex() != 0) {

                                }
                            } catch (Exception ex) {

                            }
                        }
                    }).start();
                }
            });

            timeVariable = new JComboBox();
            timeVariable.addItem("");
            for (int i=1; i < columns.length; i++) {
                timeVariable.addItem(columns[i]);
            }
            timeVariable.setBackground(Color.WHITE);
            timeVariable.setFont(MyDirectGraphVars.tahomaPlainFont11);
            timeVariable.setFocusable(false);
            timeVariable.addActionListener(new ActionListener() {
                @Override public void actionPerformed(ActionEvent e) {
                    new Thread(new Runnable() {
                        @Override public void run() {
                            try {
                                if (timeVariable.getSelectedIndex() != 0) {

                                }

                            } catch (Exception ex) {

                            }
                        }
                    }).start();
                }
            });

            value1 = new JComboBox();
            value1.addItem("");
            for (int i=1; i < columns.length; i++) {
                value1.addItem(columns[i]);
            }
            value1.setBackground(Color.WHITE);
            value1.setFont(MyDirectGraphVars.tahomaPlainFont11);
            value1.setFocusable(false);
            value1.addActionListener(new ActionListener() {
                @Override public void actionPerformed(ActionEvent e) {
                    new Thread(new Runnable() {
                        @Override public void run() {
                            try {

                            } catch (Exception ex) {

                            }
                        }
                    }).start();
                }
            });

            value2 = new JComboBox();
            value2.addItem("");
            for (int i=1; i < columns.length; i++) {
                value2.addItem(columns[i]);
            }
            value2.setBackground(Color.WHITE);
            value2.setFont(MyDirectGraphVars.tahomaPlainFont11);
            value2.setFocusable(false);
            value2.addActionListener(new ActionListener() {
                @Override public void actionPerformed(ActionEvent e) {
                    new Thread(new Runnable() {
                        @Override public void run() {
                            try {

                            } catch (Exception ex) {

                            }
                        }
                    }).start();
                }
            });

            JPanel objectPanel = new JPanel();
            objectPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 2,2));
            JLabel objectVariableLabel = new JLabel( "OBJECT ID:");
            objectVariableLabel.setFont(MyDirectGraphVars.tahomaPlainFont12);
            objectPanel.add(objectVariableLabel);
            objectPanel.add(objectVariable);

            JPanel targetPanel = new JPanel();
            targetPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 2,2));
            JLabel targetVariableLabel = new JLabel("TARGET:");
            targetVariableLabel.setFont(MyDirectGraphVars.tahomaPlainFont12);
            targetPanel.add(targetVariableLabel);
            targetPanel.add(targetVariable);

            JPanel value1Panel = new JPanel();
            value1Panel.setLayout(new FlowLayout(FlowLayout.LEFT, 2,2));
            JLabel value1Label = new JLabel( "VALUE 1:");
            value1Label.setFont(MyDirectGraphVars.tahomaPlainFont12);
            value1Panel.add(value1Label);
            value1Panel.add(value1);

            JPanel value2Panel = new JPanel();
            value2Panel.setLayout(new FlowLayout(FlowLayout.LEFT, 2,2));
            JLabel value2Label = new JLabel("VALUE 2:");
            value2Label.setFont(MyDirectGraphVars.tahomaPlainFont12);
            value2Panel.add(value2Label);
            value2Panel.add(value2);

            JPanel timeVariablePanel = new JPanel();
            JLabel timeVarLabel = new JLabel("TIME");
            timeVarLabel.setFont(MyDirectGraphVars.tahomaPlainFont12);
            value2Label.setFont(MyDirectGraphVars.tahomaPlainFont12);
            timeVariablePanel.add(timeVarLabel);
            timeVariablePanel.add(timeVariable);

            JLabel emptyLabel = new JLabel("       ");
            emptyLabel.setFont(MyDirectGraphVars.tahomaPlainFont12);

            JLabel emptyLabel2 = new JLabel("       ");
            emptyLabel2.setFont(MyDirectGraphVars.tahomaPlainFont12);

            result.setFont(MyDirectGraphVars.tahomaBoldFont12);

            JPanel controlPanel = new JPanel();
            controlPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3,3));
            controlPanel.add(objectPanel);
            controlPanel.add(targetPanel);
            //controlPanel.add(emptyLabel);
            controlPanel.add(value1Panel);
            controlPanel.add(value2Panel);
            //controlPanel.add(emptyLabel2);
            controlPanel.add(timeVariablePanel);
            controlPanel.add(showBtn);

            TitledBorder border = BorderFactory.createTitledBorder("TIME INTERVAL PREVIEW");
            border.setTitleFont(MyDirectGraphVars.tahomaBoldFont12);
            setBorder(BorderFactory.createTitledBorder(border, "", TitledBorder.LEFT, TitledBorder.TOP));

            add(controlPanel, BorderLayout.CENTER);
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
    }

    @Override public void actionPerformed(ActionEvent e) {
        if (e.getSource() == showBtn) {
            
        }
    }
}
