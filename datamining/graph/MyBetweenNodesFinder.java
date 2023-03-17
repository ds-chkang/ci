package datamining.graph;

import datamining.utils.system.MySysUtil;
import datamining.utils.system.MyVars;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MyBetweenNodesFinder
extends JFrame {

    int focusedTxtId = -1;
    JTextField sourceTxt = new JTextField();
    JTextField destTxt = new JTextField();
    JButton showBtn = new JButton(" SHOW ");
    JButton searchNodeBtn = new JButton("SEARCH NODE");

    public MyBetweenNodesFinder() {
        super("FIND NODES BETWEEN NODES");
        this.decorate();
        this.runShowButtonThread();
    }

    private void runShowButtonThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(350);
                        if (sourceTxt.getText().length() > 1 && destTxt.getText().length() > 1) {
                            showBtn.setEnabled(true);
                            break;
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void decorate() {
        MyBetweenNodesFinder f = this;
        this.setLayout(new BorderLayout(10, 10));
        this.setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(640, 200));

        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setLayout(new BorderLayout(5,5));

        JPanel betweenNodePanel = new JPanel();
        betweenNodePanel.setBackground(Color.WHITE);
        betweenNodePanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel betweenLabel = new JLabel("BETWEEN ");
        betweenLabel.setBackground(Color.WHITE);
        betweenLabel.setFont(MyVars.tahomaPlainFont12);

        showBtn.setFocusable(false);
        showBtn.setEnabled(false);
        showBtn.setBackground(Color.WHITE);
        showBtn.setFont(MyVars.tahomaPlainFont12);
        showBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        findNodes();
                    }
                }).start();
            }
        });

        sourceTxt.setBorder(new LineBorder(Color.DARK_GRAY,1));
        sourceTxt.setFont(MyVars.tahomaPlainFont12);
        sourceTxt.setBackground(Color.WHITE);
        sourceTxt.setPreferredSize(new Dimension(222, 25));
        sourceTxt.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                focusedTxtId = 0;
            }
        });

        JLabel andLabel = new JLabel("~");
        andLabel.setBackground(Color.WHITE);
        andLabel.setFont(MyVars.tahomaPlainFont12);

        destTxt.setBorder(new LineBorder(Color.DARK_GRAY,1));
        destTxt.setBackground(Color.WHITE);
        destTxt.setFont(MyVars.tahomaPlainFont12);
        destTxt.setPreferredSize(new Dimension(222, 25));
        destTxt.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                focusedTxtId = 1;
            }
        });

        searchNodeBtn.setFont(MyVars.tahomaPlainFont12);
        searchNodeBtn.setBackground(Color.WHITE);
        searchNodeBtn.setFocusable(false);
        searchNodeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (focusedTxtId == 0) {
                            MyNodeLister nodeSearch = new MyNodeLister(sourceTxt, f);
                        } else if (focusedTxtId == 1) {
                            MyNodeLister nodeSearch = new MyNodeLister(sourceTxt, destTxt, f);
                        }
                    }
                }).start();
            }
        });

        JPanel searchNodePanel = new JPanel();
        searchNodePanel.setBackground(Color.WHITE);
        searchNodePanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        searchNodePanel.add(searchNodeBtn);

        betweenNodePanel.add(betweenLabel);
        betweenNodePanel.add(sourceTxt);
        betweenNodePanel.add(andLabel);
        betweenNodePanel.add(destTxt);
        betweenNodePanel.add(showBtn);

        contentPanel.add(betweenNodePanel, BorderLayout.CENTER);
        contentPanel.add(searchNodePanel, BorderLayout.SOUTH);

        Border blackline = BorderFactory.createLineBorder(Color.black);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(blackline, "SET BETWEEN NODES");
        titledBorder.setTitleJustification(TitledBorder.LEFT);
        titledBorder.setTitleFont(MyVars.tahomaBoldFont12);
        titledBorder.setTitleColor(Color.DARK_GRAY);
        contentPanel.setBorder(titledBorder);

        this.getContentPane().add(contentPanel, BorderLayout.CENTER);
        this.pack();
        this.setResizable(false);
        this.setAlwaysOnTop(true);
        this.setVisible(true);
        this.setAlwaysOnTop(false);
    }

    private void findNodes() {
        String source = this.getEncodedName(sourceTxt.getText());
        String dest = this.getEncodedName(destTxt.getText());

        //Set all edge values to zero.
        Collection<MyEdge> es = MyVars.g.getEdges();
        for (MyEdge e : es) {
            e.setCurrentValue(0);
        }

        Map<String, Integer> nCons = new HashMap<>();
        for (int s=0; s < MyVars.seqs.length; s++) {
            Map<String, Integer> lNCons = new HashMap<>();
            for (int i=1; i < MyVars.seqs[s].length; i++) {
                String iset = (MyVars.isTimeOn ? MyVars.seqs[s][i-1].split(":")[0] : MyVars.seqs[s][i-1]);
                if (source.equals(iset)) {
                    if (lNCons.containsKey(iset)) {
                        lNCons.put(iset, lNCons.get(iset)+1);
                    } else {
                        lNCons.put(iset, 1);
                    }
                    for (; i < MyVars.seqs[s].length; i++) {
                        iset = (MyVars.isTimeOn ? MyVars.seqs[s][i].split(":")[0] : MyVars.seqs[s][i]);
                        if (lNCons.containsKey(iset)) {
                            lNCons.put(iset, lNCons.get(iset)+1);
                        } else {
                            lNCons.put(iset, 1);
                        }
                        if (iset.equals(dest)) {
                            for (String n : lNCons.keySet()) {
                                if (nCons.containsKey(n)) {
                                    nCons.put(n, nCons.get(n)+lNCons.get(n));
                                } else {
                                    nCons.put(n, lNCons.get(n));
                                }
                            }
                            break;
                        }
                    }
                    break;
                }
            }
        }

        Map<String, Integer> eCons = new HashMap<>();
        for (int s=0; s < MyVars.seqs.length; s++) {
            Map<String, Integer> lECons = new HashMap<>();
            for (int i=1; i < MyVars.seqs[s].length; i++) {
                String pSet = (MyVars.isTimeOn ? MyVars.seqs[s][i-1].split(":")[0] : MyVars.seqs[s][i-1]);
                if (source.equals(pSet)) {
                    String sSet = (MyVars.isTimeOn ? MyVars.seqs[s][i].split(":")[0] : MyVars.seqs[s][i]);
                    String e = pSet + "-" + sSet;
                    if (lECons.containsKey(e)) {lECons.put(e, lECons.get(e)+1);}
                    else {lECons.put(e, 1);}

                    if (sSet.equals(dest)) {
                        for (String en : lECons.keySet()) {
                            if (eCons.containsKey(en)) {eCons.put(en, eCons.get(en)+lECons.get(en));}
                            else {eCons.put(en, lECons.get(en));}
                        }
                        break;
                    }

                    for (i=i+1; i < MyVars.seqs[s].length; i++) {
                        pSet = (MyVars.isTimeOn ? MyVars.seqs[s][i-1].split(":")[0] : MyVars.seqs[s][i-1]);
                        sSet = (MyVars.isTimeOn ? MyVars.seqs[s][i].split(":")[0] : MyVars.seqs[s][i]);
                        e = pSet + "-" + sSet;
                        if (lECons.containsKey(e)) {lECons.put(e, lECons.get(e)+1);}
                        else {lECons.put(e, 1);}
                        if (sSet.equals(dest)) {
                            for (String en : lECons.keySet()) {
                                if (eCons.containsKey(en)) {eCons.put(en, eCons.get(en)+lECons.get(en));}
                                else {eCons.put(en, lECons.get(en));}
                            }
                            break;
                        }
                    }
                    break;
                }
            }
        }

        // Set node contributions.
        Collection<MyNode> ns = MyVars.g.getVertices();
        long max = 0;
        for (MyNode n : ns) {
            if (nCons.containsKey(n.getName())) {
                n.setCurrentValue(nCons.get(n.getName()));
                if (max < (long)n.getCurrentValue()) {max = (long)n.getCurrentValue();}
            }
        }
        synchronized (MyVars.g.MX_N_VAL) {
            MyVars.g.MX_N_VAL = Float.valueOf(max);
        }

        // Set edge contributions.
        float eMaxVal = 0.0f;
        for (MyEdge e : es) {
            String en = e.getSource().getName() + "-" + e.getDest().getName();
            if (eCons.containsKey(en)) {
                e.setCurrentValue(eCons.get(en));
                if (eMaxVal < e.getCurrentValue()) {
                    eMaxVal = e.getCurrentValue();
                }
            }
        }

        MyVars.g.MX_E_VAL = eMaxVal;
        MyVars.getViewer().nodesBetweenTwoNodes = nCons.keySet();
        MyVars.getViewer().revalidate();
        MyVars.getViewer().repaint();
    }

    private String getEncodedName(String txt) {
        String encoded = MySysUtil.encodeItemSet(txt);
        if (encoded.equals("") || encoded == null) {
            encoded = MySysUtil.encodeVariableItemSet(txt);
        }
        return encoded;
    }

}
