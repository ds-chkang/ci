package datamining.graph.common;

import datamining.graph.MyDirectEdge;
import datamining.utils.message.MyMessageUtil;
import datamining.utils.MyMathUtil;
import datamining.utils.system.MySysUtil;
import datamining.utils.system.MyVars;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyCommunityClusterDialog
extends JFrame {

    public MyCommunityClusterDialog() {
        super(" FIND COMMUNITIES");
        this.decorate();
    }

    private void decorate() {
        try {
            Thread.sleep(300);
            this.setPreferredSize(new Dimension(350, 130));
            this.setLayout(new BorderLayout(3, 3));

            JPanel msgPanel = new JPanel();
            msgPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));

            JLabel msg = new JLabel("    Set the number of edges to remove: ");
            msg.setFont(MyVars.tahomaPlainFont12);

            JTextField txt = new JTextField(8);
            txt.setFont(MyVars.tahomaPlainFont12);

            msgPanel.add(msg);
            msgPanel.add(txt);

            JPanel btnPanel = new JPanel();
            btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 3));

            JButton okBtn = new JButton("   OK   ");
            okBtn.setFont(MyVars.tahomaPlainFont12);
            okBtn.setFocusable(false);
            okBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (e.getSource() == okBtn) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    int numOfEdgesToRemove = Integer.valueOf(txt.getText().replaceAll(" ", ""));
                                    if (numOfEdgesToRemove > MyVars.directMarkovChain.getVertexCount()) {
                                        MyMessageUtil.showInfoMsg("The number of edges must be less than " + MyMathUtil.getCommaSeperatedNumber((int)((double)MyVars.directMarkovChain.getVertexCount()/2)));
                                        decorate();
                                    } else {
                                        dispose();
                                        try {Thread.sleep(250);}
                                        catch (Exception ex) {ex.printStackTrace();}
                                        MyEdgeBetweennessClusterer edgeBetweennessClusterer = new MyEdgeBetweennessClusterer(numOfEdgesToRemove);
                                        edgeBetweennessClusterer.transform(MyVars.directMarkovChain);
                                        for (Object e : edgeBetweennessClusterer.getEdgesRemoved()) {
                                            String p = ((MyDirectEdge) e).getSource().getName();
                                            String s = ((MyDirectEdge) e).getDest().getName();
                                            p = (p.contains("x") ? MySysUtil.decodeVariable(p) : MySysUtil.decodeNodeName(p));
                                            s = (s.contains("x") ? MySysUtil.decodeVariable(s) : MySysUtil.decodeNodeName(s));
                                            System.out.println(p + "-" + s);
                                        }
                                    }
                                } catch (NumberFormatException ex) {
                                    MyMessageUtil.showInfoMsg("Provide the number of edges to remove!");
                                }
                            }
                        }).start();
                    }
                }
            });
            btnPanel.add(okBtn);

            this.getContentPane().add(msgPanel, BorderLayout.CENTER);
            this.getContentPane().add(btnPanel, BorderLayout.SOUTH);
            this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            this.pack();
            this.setResizable(false);
            this.setVisible(true);
        } catch (Exception ex) {ex.printStackTrace();}
    }
}
