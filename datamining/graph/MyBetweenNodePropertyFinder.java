package datamining.graph;

import datamining.main.MyProgressBar;
import datamining.utils.message.MyMessageUtil;
import datamining.utils.MyMathUtil;
import datamining.utils.system.MySysUtil;
import datamining.utils.system.MyVars;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class MyBetweenNodePropertyFinder
extends JFrame {

    private int focusedTxtId = -1;
    private JTextField sourceTxt = new JTextField();
    JTextField destTxt = new JTextField();
    JButton showBtn = new JButton(" SHOW ");
    JButton searchNodeBtn = new JButton("SEARCH NODE");
    MyBetweenNodePropertyFinder btwFiner;
    private JLabel reachTimeValue = new JLabel();
    private JLabel occurrenceValue = new JLabel();
    private JLabel minReachTimeValue = new JLabel();
    private JLabel maxReachTimeValue = new JLabel();
    private JLabel avgHopValue = new JLabel();
    private JLabel minHopValue = new JLabel();
    private JLabel maxHopValue = new JLabel();
    private JLabel avgReachTimeValue = new JLabel();

    public MyBetweenNodePropertyFinder() {
        super("SHOW BETWEEN NODE PROPERTIES ");
        this.decorate();
        this.runShowButtonThread();
    }

    private void runShowButtonThread() {
        new Thread(new Runnable() {@Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(250);
                        if (sourceTxt.getText().length() > 1 && destTxt.getText().length() > 1) {
                            showBtn.setEnabled(true);
                            break;
                        }
                    } catch (Exception ex) {ex.printStackTrace();}
                }
            }
        }).start();
    }

    private void decorate() {
        MyBetweenNodePropertyFinder f = this;
        this.setLayout(new BorderLayout(10, 10));
        this.setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(640, 340));

        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setLayout(new BorderLayout(5,5));

        JPanel betweenNodePanel = new JPanel();
        betweenNodePanel.setPreferredSize(new Dimension(300, 85));
        betweenNodePanel.setBackground(Color.WHITE);
        betweenNodePanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel betweenLabel = new JLabel("BETWEEN ");
        betweenLabel.setBackground(Color.WHITE);
        betweenLabel.setFont(MyVars.tahomaPlainFont11);

        this.showBtn.setFocusable(false);
        this.showBtn.setEnabled(false);
        this.showBtn.setBackground(Color.WHITE);
        this.showBtn.setFont(MyVars.tahomaPlainFont11);
        this.showBtn.addActionListener(new ActionListener() {@Override
            public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {@Override
                    public void run() {
                        setAlwaysOnTop(true);
                        if (MyVars.isTimeOn) {setTimeConstrainedPropertyValues();}
                        else {setPropertyValues();}
                        setAlwaysOnTop(false);
                    }
                }).start();
            }
        });

        this.sourceTxt.setBorder(new LineBorder(Color.DARK_GRAY,1));
        this.sourceTxt.setFont(MyVars.tahomaPlainFont12);
        this.sourceTxt.setBackground(Color.WHITE);
        this.sourceTxt.setPreferredSize(new Dimension(222, 23));
        this.sourceTxt.addFocusListener(new FocusAdapter() {@Override
            public void focusGained(FocusEvent e) {focusedTxtId = 0;}
        });

        JLabel andLabel = new JLabel("~");
        andLabel.setBackground(Color.WHITE);
        andLabel.setFont(MyVars.tahomaPlainFont12);

        this.destTxt.setBorder(new LineBorder(Color.DARK_GRAY,1));
        this.destTxt.setBackground(Color.WHITE);
        this.destTxt.setFont(MyVars.tahomaPlainFont12);
        this.destTxt.setPreferredSize(new Dimension(222, 23));
        this.destTxt.addFocusListener(new FocusAdapter() {@Override
            public void focusGained(FocusEvent e) {focusedTxtId = 1;}});

        this.searchNodeBtn.setFont(MyVars.tahomaPlainFont11);
        this.searchNodeBtn.setBackground(Color.WHITE);
        this.searchNodeBtn.setFocusable(false);
        this.searchNodeBtn.addActionListener(new ActionListener() {@Override
            public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {@Override
                    public void run() {
                        if (focusedTxtId == 0) {
                            setAlwaysOnTop(true);
                            MyNodeLister nodeSearch = new MyNodeLister(sourceTxt, f);
                            nodeSearch.setAlwaysOnTop(true);
                            setAlwaysOnTop(false);
                        } else if (focusedTxtId == 1) {
                            setAlwaysOnTop(true);
                            MyNodeLister nodeSearch = new MyNodeLister(sourceTxt, destTxt, f);
                            nodeSearch.setAlwaysOnTop(true);
                            setAlwaysOnTop(false);
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

        contentPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        contentPanel.add(betweenNodePanel, BorderLayout.CENTER);
        contentPanel.add(searchNodePanel, BorderLayout.SOUTH);

        JPanel propertyPanel = this.setBetweenNodeProperties();
        this.getContentPane().setBackground(Color.WHITE);
        this.getContentPane().add(contentPanel, BorderLayout.CENTER);
        this.getContentPane().add(propertyPanel, BorderLayout.SOUTH);
        this.pack();
        this.setResizable(false);
        this.setAlwaysOnTop(true);
        this.setVisible(true);
        this.setAlwaysOnTop(false);
    }

    private JPanel setBetweenNodeProperties() {
        JPanel betweenNodePropertyPanel = new JPanel();
        betweenNodePropertyPanel.setLayout(new GridLayout(8, 2, 5,3));
        betweenNodePropertyPanel.setBackground(Color.WHITE);

        JLabel reachTimeLabel = new JLabel(" TOTAL TIME: ");
        reachTimeLabel.setBackground(Color.WHITE);
        reachTimeLabel.setFont(MyVars.tahomaPlainFont11);

        this.reachTimeValue.setBackground(Color.WHITE);
        this.reachTimeValue.setFont(MyVars.tahomaPlainFont11);
        this.reachTimeValue.setHorizontalAlignment(JLabel.LEFT);

        JLabel avgReachTimeLabel = new JLabel(" AVG. TIME: ");
        avgReachTimeLabel.setBackground(Color.WHITE);
        avgReachTimeLabel.setFont(MyVars.tahomaPlainFont11);

        this.avgReachTimeValue.setHorizontalAlignment(JLabel.LEFT);
        this.avgReachTimeValue.setBackground(Color.WHITE);
        this.avgReachTimeValue.setFont(MyVars.tahomaPlainFont11);

        JLabel minReachTimeLabel = new JLabel("MIN. TIME: ");
        minReachTimeLabel.setBackground(Color.WHITE);
        minReachTimeLabel.setFont(MyVars.tahomaPlainFont12);

        this.minReachTimeValue.setBackground(Color.WHITE);
        this.minReachTimeValue.setFont(MyVars.tahomaPlainFont11);
        this.minReachTimeValue.setHorizontalAlignment(JLabel.LEFT);

        JLabel maxReachTimeLabel = new JLabel(" MAX. TIME: ");
        maxReachTimeLabel.setBackground(Color.WHITE);
        maxReachTimeLabel.setFont(MyVars.tahomaPlainFont11);

        this.maxReachTimeValue.setHorizontalAlignment(JLabel.LEFT);
        this.maxReachTimeValue.setBackground(Color.WHITE);
        this.maxReachTimeValue.setFont(MyVars.tahomaPlainFont11);

        JLabel occurrenceLabel = new JLabel(" NO. OF OCCURRENCES: ");
        occurrenceLabel.setBackground(Color.WHITE);
        occurrenceLabel.setFont(MyVars.tahomaPlainFont11);

        this.occurrenceValue.setBackground(Color.WHITE);
        this.occurrenceValue.setFont(MyVars.tahomaPlainFont11);
        this.occurrenceValue.setHorizontalAlignment(JLabel.LEFT);

        JLabel avgHopLabel = new JLabel(" AVG. HOPS:");
        avgHopLabel.setBackground(Color.WHITE);
        avgHopLabel.setFont(MyVars.tahomaPlainFont11);

        this.avgHopValue.setHorizontalAlignment(JLabel.LEFT);
        this.avgHopValue.setBackground(Color.WHITE);
        this.avgHopValue.setFont(MyVars.tahomaPlainFont11);

        JLabel minHopLabel = new JLabel(" MIN. HOPS:");
        minHopLabel.setBackground(Color.WHITE);
        minHopLabel.setFont(MyVars.tahomaPlainFont11);

        this.minHopValue.setHorizontalAlignment(JLabel.LEFT);
        this.minHopValue.setBackground(Color.WHITE);
        this.minHopValue.setFont(MyVars.tahomaPlainFont11);

        JLabel maxHopLabel = new JLabel(" MAX. HOPS:");
        maxHopLabel.setBackground(Color.WHITE);
        maxHopLabel.setFont(MyVars.tahomaPlainFont11);

        this.maxHopValue.setHorizontalAlignment(JLabel.LEFT);
        this.maxHopValue.setBackground(Color.WHITE);
        this.maxHopValue.setFont(MyVars.tahomaPlainFont11);

        betweenNodePropertyPanel.add(occurrenceLabel);
        betweenNodePropertyPanel.add(this.occurrenceValue);
        betweenNodePropertyPanel.add(avgHopLabel);
        betweenNodePropertyPanel.add(this.avgHopValue);
        betweenNodePropertyPanel.add(minHopLabel);
        betweenNodePropertyPanel.add(this.minHopValue);
        betweenNodePropertyPanel.add(maxHopLabel);
        betweenNodePropertyPanel.add(this.maxHopValue);
        betweenNodePropertyPanel.add(maxReachTimeLabel);
        betweenNodePropertyPanel.add(this.maxReachTimeValue);
        betweenNodePropertyPanel.add(minReachTimeLabel);
        betweenNodePropertyPanel.add(this.minReachTimeValue);
        betweenNodePropertyPanel.add(avgReachTimeLabel);
        betweenNodePropertyPanel.add(this.avgReachTimeValue);
        betweenNodePropertyPanel.add(reachTimeLabel);
        betweenNodePropertyPanel.add(this.reachTimeValue);
        return betweenNodePropertyPanel;
    }

    private void setTimeConstrainedPropertyValues() {
        MyProgressBar pb = new MyProgressBar(false);
        int workCnt = 0;
        int totalHops = 0;
        long totalReachTime = 0L;
        int maxHops = 0;
        int minHops = 1000000000;
        long minReachTime = 1000000000000L;
        long maxReachTime = 0L;
        int occurrence = 0;
        for (int s = 0; s < MyVars.seqs.length; s++) {
            long localReachTime = 0L;
            boolean found = false;
            for (int i = 1; i < MyVars.seqs[s].length; i++) {
                String p = MyVars.seqs[s][i - 1].split(":")[0];
                if (this.getEncodedName(this.sourceTxt.getText().trim()).equals(p)) {
                    int localHops = 0;
                    for (; i < MyVars.seqs[s].length; i++) {
                        localHops++;
                        String ss = MyVars.seqs[s][i].split(":")[0];
                        localReachTime += Long.valueOf(MyVars.seqs[s][i].split(":")[1]);
                        if (ss.equals(this.getEncodedName(this.destTxt.getText().trim()))) {
                            found = true;
                            totalHops += localHops;
                            break;
                        }
                    }
                    if (found) {
                        if (maxHops < localHops) {maxHops = localHops;}
                        if (minHops > localHops) {minHops = localHops;}
                        if (maxReachTime < localReachTime) {maxReachTime = localReachTime;}
                        if (minReachTime > localReachTime) {minReachTime = localReachTime;}
                        totalReachTime += localReachTime;
                        occurrence++;
                    }
                    break;
                }
            }
            pb.updateValue(++workCnt, MyVars.sequeceFeatureCount);
        }

        if (occurrence > 0) {
            this.reachTimeValue.setText(MyMathUtil.getCommaSeperatedNumber(totalReachTime));
            this.occurrenceValue.setText(MyMathUtil.getCommaSeperatedNumber(occurrence));
            this.minReachTimeValue.setText(MyMathUtil.getCommaSeperatedNumber(minReachTime));
            this.maxReachTimeValue.setText(MyMathUtil.getCommaSeperatedNumber(maxReachTime));
            this.minHopValue.setText(MyMathUtil.getCommaSeperatedNumber(minHops));
            this.maxHopValue.setText(MyMathUtil.getCommaSeperatedNumber(maxHops));
            this.avgHopValue.setText(MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat((double) totalHops / occurrence)));
            this.avgReachTimeValue.setText(MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat((double) totalReachTime / occurrence)));
            pb.updateValue(100,100);
            pb.dispose();
            this.setAlwaysOnTop(true);
            this.setAlwaysOnTop(false);
        } else {
            pb.updateValue(100,100);
            pb.dispose();
            MyMessageUtil.showInfoMsg("THERE ARE NO OCCURRENCES BETWEEN THE TWO NODES!");
            this.reachTimeValue.setText("");
            this.occurrenceValue.setText("");
            this.minReachTimeValue.setText("");
            this.maxReachTimeValue.setText("");
            this.minHopValue.setText("");
            this.maxHopValue.setText("");
            this.avgHopValue.setText("");
            this.avgReachTimeValue.setText("");
            this.setAlwaysOnTop(true);
            this.setAlwaysOnTop(false);
        }
        this.showBtn.setEnabled(false);
    }

    private void setPropertyValues() {
        MyProgressBar pb = new MyProgressBar(false);
        int workCnt = 0;
        int totalHops = 0;
        int maxHops = 0;
        int minHops = 1000000000;
        int occurrence = 0;
        for (int s = 0; s < MyVars.seqs.length; s++) {
            boolean found = false;
            for (int i = 1; i < MyVars.seqs[s].length; i++) {
                String p = MyVars.seqs[s][i-1];
                if (this.getEncodedName(this.sourceTxt.getText().trim()).equals(p)) {
                    int localHops = 0;
                    for (; i < MyVars.seqs[s].length; i++) {
                        localHops++;
                        String ss = MyVars.seqs[s][i];
                        if (ss.equals(this.getEncodedName(this.destTxt.getText().trim()))) {
                            found = true;
                            totalHops += localHops;
                            break;
                        }
                    }
                    if (found) {
                        if (maxHops < localHops) {maxHops = localHops;}
                        if (minHops > localHops) {minHops = localHops;}
                        occurrence++;
                    }
                    break;
                }
            }
            pb.updateValue(++workCnt, MyVars.sequeceFeatureCount);
        }

        if (occurrence > 0) {
            this.occurrenceValue.setText(MyMathUtil.getCommaSeperatedNumber(occurrence));
            this.minReachTimeValue.setText("0");
            this.maxReachTimeValue.setText("0");
            this.reachTimeValue.setText("0");
            this.minHopValue.setText(MyMathUtil.getCommaSeperatedNumber(minHops));
            this.maxHopValue.setText(MyMathUtil.getCommaSeperatedNumber(maxHops));
            this.avgHopValue.setText(MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat((double) totalHops / occurrence)));
            this.avgReachTimeValue.setText("0.00");
            pb.updateValue(100,100);
            pb.dispose();
            this.setAlwaysOnTop(true);
            this.setAlwaysOnTop(false);
        } else {
            pb.updateValue(100,100);
            pb.dispose();
            MyMessageUtil.showInfoMsg("THERE ARE NO OCCURRENCES BETWEEN THE TWO NODES!");
            this.reachTimeValue.setText("");
            this.occurrenceValue.setText("");
            this.minReachTimeValue.setText("");
            this.maxReachTimeValue.setText("");
            this.minHopValue.setText("");
            this.maxHopValue.setText("");
            this.avgHopValue.setText("");
            this.avgReachTimeValue.setText("");
            this.setAlwaysOnTop(true);
            this.setAlwaysOnTop(false);
        }
        this.showBtn.setEnabled(false);
    }

    private String getEncodedName(String txt) {
        String encoded = MySysUtil.encodeItemSet(txt);
        if (encoded.equals("") || encoded == null) {encoded = MySysUtil.encodeVariableItemSet(txt);}
        return encoded;
    }
}