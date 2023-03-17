package datamining.graph.stats;

import datamining.main.MyProgressBar;
import datamining.graph.MyNode;
import datamining.utils.MyMathUtil;
import datamining.utils.system.MySysUtil;
import datamining.utils.system.MyVars;
import datamining.utils.table.MyTableUtil;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;

public class MyPredecessorStatistics
extends JPanel
implements ActionListener {

    private String [] timeConstrainedColumns = {
            "NO.",
            "PREDECESSOR",
            "CONTRIBUTION",
            "UNIQUE CONTRIBUTION",
            "REACH TIME"
    };

    private String [] columns = {
            "NO.",
            "PREDECESSOR",
            "CONTRIBUTION",
            "UNIQUE CONTRIBUTION"
    };

    private String [][] data = {};
    private JTable table;
    private DefaultTableModel model;
    private JTextField searchTxt = new JTextField();
    private JButton save = new JButton(" SAVE ");

    public MyPredecessorStatistics() {
        this.decorate();
        JFrame frame = new JFrame("PREDECESSOR STATISTICS");
        frame.setLayout(new BorderLayout(5,5));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(this, BorderLayout.CENTER);
        frame.pack();
        frame.setPreferredSize(new Dimension(850, 550));
        frame.setVisible(true);
    }

    private void decorate() {
        MyProgressBar pb = new MyProgressBar(false);
        this.setLayout(new BorderLayout(5, 5));
        this.setBackground(Color.WHITE);
        pb.updateValue(10, 100);

        if (MyVars.isTimeOn) {
            this.setTimeConstrainedPredecessorStatistics();
        } else {
            this.setPredecessorStatistics();
        }

        JLabel numberOfNodeLabel = new JLabel("NO. OF NODES: " + MyMathUtil.getCommaSeperatedNumber(this.table.getRowCount()));
        numberOfNodeLabel.setBackground(Color.WHITE);
        numberOfNodeLabel.setFont(MyVars.f_pln_12);

        JLabel selectedNode = new JLabel("");
        selectedNode.setFont(MyVars.f_pln_12);
        selectedNode.setBackground(Color.WHITE);

        this.table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                selectedNode.setText("    SELECTED NODE: " + table.getValueAt(table.getSelectedRow(), 1).toString());
            }
        });

        this.searchTxt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                numberOfNodeLabel.setText("NO. OF NODES: " + MyMathUtil.getCommaSeperatedNumber(table.getRowCount()));
            }
        });

        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        infoPanel.add(numberOfNodeLabel);
        infoPanel.add(selectedNode);

        JScrollPane tableScrollPane = new JScrollPane(this.table);
        tableScrollPane.setPreferredSize(new Dimension(1000, 650));
        this.add(infoPanel, BorderLayout.NORTH);
        this.add(tableScrollPane, BorderLayout.CENTER);
        this.searchTxt.setBorder(new LineBorder(Color.DARK_GRAY,1));
        JPanel searchAndSave = MyTableUtil.searchAndSaveDataPanelForJTable2(this, this.searchTxt, this.save, this.model, this.table);
        searchAndSave.setBackground(Color.WHITE);
        this.add(searchAndSave, BorderLayout.SOUTH);
        this.table.getTableHeader().setFont(MyVars.tahomaBoldFont12);
        this.save.setPreferredSize(new Dimension(70, 29));
        this.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.table.setRowSorter(MyTableUtil.setJTableRowSorterWithTextField(this.model, this.searchTxt));
        this.table.setSelectionBackground(Color.BLACK);
        this.table.setSelectionForeground(Color.WHITE);
        this.table.setFocusable(false);
        Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
        this.table.setCursor(cursor);
        this.setPreferredSize(new Dimension(450, 700));

        Border blackline = BorderFactory.createLineBorder(Color.black);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(blackline, "");
        titledBorder.setTitleJustification(TitledBorder.LEFT);
        titledBorder.setTitleFont(MyVars.tahomaBoldFont12);
        titledBorder.setTitleColor(Color.DARK_GRAY);
        this.setBorder(titledBorder);
        pb.updateValue(100, 100);
        pb.dispose();
    }

    private Map<MyNode, Integer> getPredecessorContributions(Collection<MyNode> predecessors) {
        Map<MyNode, Integer> predecessorContributionMap = new HashMap<>();
        try {
            for (MyNode pNode : predecessors) {
                int contribution = 0;
                for (int s = 0; s < MyVars.seqs.length; s++) {
                    for (int i = 1; i < MyVars.seqs[s].length; i++) {
                        String pred = MyVars.seqs[s][i-1].split(":")[0];
                        String suc = MyVars.seqs[s][i].split(":")[0];
                        if (pNode.getName().equals(pred) && suc.equals(MyVars.getViewer().selectedNode.getName())) {
                            contribution++;
                        }
                    }
                }
                predecessorContributionMap.put(pNode, contribution);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return predecessorContributionMap;
    }

    private Map<MyNode, Integer> getPredecessorUniqueContributions(Collection<MyNode> predecessors) {
        Map<MyNode, Integer> predecessorUniqueContributionMap = new HashMap<>();
        try {
            for (MyNode pNode : predecessors) {
                int uniqCont = 0;
                for (int s = 0; s < MyVars.seqs.length; s++) {
                    for (int i = 1; i < MyVars.seqs[s].length; i++) {
                        String pred = MyVars.seqs[s][i-1].split(":")[0];
                        String suc = MyVars.seqs[s][i].split(":")[0];
                        if (pNode.getName().equals(pred) && suc.equals(MyVars.getViewer().selectedNode.getName())) {
                            uniqCont++;
                            break;
                        }
                    }
                }
                predecessorUniqueContributionMap.put(pNode, uniqCont);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return predecessorUniqueContributionMap;
    }

    private Map<MyNode, Long> getPredecessorReachTimes(Collection<MyNode> predecessors) {
        Map<MyNode, Long> predecessorReachTimeMap = new HashMap<>();
        try {
            for (MyNode pNode : predecessors) {
                long reachTime = 0;
                for (int s = 0; s < MyVars.seqs.length; s++) {
                    for (int i = 1; i < MyVars.seqs[s].length; i++) {
                        String pred = MyVars.seqs[s][i-1].split(":")[0];
                        String suc = MyVars.seqs[s][i].split(":")[0];
                        if (pNode.getName().equals(pred) && suc.equals(MyVars.getViewer().selectedNode.getName())) {
                            reachTime += Long.valueOf(MyVars.seqs[s][i].split(":")[1]);
                        }
                    }
                }
                predecessorReachTimeMap.put(pNode, reachTime);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return predecessorReachTimeMap;
    }

    private Set<MyNode> getDepthPredecessors() {
        Set<MyNode> depthPredecessorSet = new HashSet<>();
        if (MyVars.currentGraphDepth == 0) {
            return depthPredecessorSet;
        }
        for (int s = 0; s < MyVars.seqs.length; s++) {
            if (MyVars.seqs[s].length-1 >= MyVars.currentGraphDepth) {
                String pred = MyVars.seqs[s][MyVars.currentGraphDepth -1].split(":")[0];
                String suc = MyVars.seqs[s][MyVars.currentGraphDepth].split(":")[0];
                if (suc.equals(MyVars.getViewer().selectedNode.getName())) {
                    MyNode pNode = (MyNode)MyVars.g.vRefs.get(pred);
                    if (!depthPredecessorSet.contains(pNode)) {
                        depthPredecessorSet.add(pNode);
                    }
                }
            }
        }
        return depthPredecessorSet;
    }

    private Map<MyNode, Integer> getDepthPredecessorContributions(Collection<MyNode> predecessors) {
        Map<MyNode, Integer> depthPredecessorContributionMap = new HashMap<>();
        try {
            for (MyNode pNode : predecessors) {
                int cont = 0;
                for (int s = 0; s < MyVars.seqs.length; s++) {
                    if (MyVars.seqs[s].length-1 >= MyVars.currentGraphDepth) {
                        String pred = MyVars.seqs[s][MyVars.currentGraphDepth -1].split(":")[0];
                        String suc = MyVars.seqs[s][MyVars.currentGraphDepth].split(":")[0];
                        if (pNode.getName().equals(pred) && suc.equals(MyVars.getViewer().selectedNode.getName())) {
                            cont++;
                        }
                    }
                }
                depthPredecessorContributionMap.put(pNode, cont);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return depthPredecessorContributionMap;
    }

    private Map<MyNode, Long> getDepthPredecessorReachTimes(Collection<MyNode> predecessors) {
        Map<MyNode, Long> depthPredecessorReachTimeMap = new HashMap<>();
        try {
            for (MyNode pNode : predecessors) {
                long reachTime = 0;
                for (int s = 0; s < MyVars.seqs.length; s++) {
                    if (MyVars.seqs[s].length-1 >= MyVars.currentGraphDepth) {
                        String pred = MyVars.seqs[s][MyVars.currentGraphDepth -1].split(":")[0];
                        String suc = MyVars.seqs[s][MyVars.currentGraphDepth].split(":")[0];
                        if (pNode.getName().equals(pred) && suc.equals(MyVars.getViewer().selectedNode.getName())) {
                            reachTime += Long.valueOf(MyVars.seqs[s][MyVars.currentGraphDepth].split(":")[1]);
                        }
                    }
                }
                depthPredecessorReachTimeMap.put(pNode, reachTime);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return depthPredecessorReachTimeMap;
    }

    private void setTimeConstrainedPredecessorStatistics() {
        try {
            this.model = new DefaultTableModel(this.data, this.timeConstrainedColumns);

            int nodeCnt = 0;
            Collection<MyNode> predecessors = null;
            Map<MyNode, Integer> predecessorContributionMap = null;
            Map<MyNode, Integer> predecessorUniqueContributionMap = null;
            Map<MyNode, Long> predecessorReachTimeMap = null;

            if (MyVars.currentGraphDepth == -1) {
                predecessors = MyVars.g.getPredecessors(MyVars.getViewer().selectedNode);
                predecessorContributionMap = this.getPredecessorContributions(predecessors);
                predecessorUniqueContributionMap = this.getPredecessorUniqueContributions(predecessors);
                predecessorReachTimeMap = this.getPredecessorReachTimes(predecessors);
            } else {
                predecessors = this.getDepthPredecessors();
                predecessorContributionMap = this.getDepthPredecessorContributions(predecessors);
                predecessorUniqueContributionMap = this.getDepthPredecessorContributions(predecessors);
                predecessorReachTimeMap = this.getDepthPredecessorReachTimes(predecessors);
            }

            for (MyNode predecessor : predecessors) {
                model.addRow(new String[]{
                    String.valueOf(++nodeCnt),
                    MySysUtil.decodeNodeName(predecessor.getName()),
                    MyMathUtil.getCommaSeperatedNumber(predecessorContributionMap.get(predecessor)),
                    MyMathUtil.getCommaSeperatedNumber(predecessorUniqueContributionMap.get(predecessor)),
                    MyMathUtil.getCommaSeperatedNumber(predecessorReachTimeMap.get(predecessor))
                });
            }
            this.table = new JTable(model);
            this.table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            this.table.getColumnModel().getColumn(0).setPreferredWidth(60);
            this.table.getColumnModel().getColumn(1).setPreferredWidth(250);
            this.table.getColumnModel().getColumn(2).setPreferredWidth(160);
            this.table.getColumnModel().getColumn(3).setPreferredWidth(160);
            this.table.getColumnModel().getColumn(4).setPreferredWidth(160);

            this.table.setBackground(Color.WHITE);
            this.table.setFont(MyVars.f_pln_12);
            this.table.getTableHeader().setBackground(Color.ORANGE);
            this.table.getTableHeader().setBackground(Color.LIGHT_GRAY);
            this.table.setRowHeight(24);
        } catch (Exception ex) {}
    }

    private void setPredecessorStatistics() {
        try {
            this.model = new DefaultTableModel(this.data, this.columns);

            int nodeCnt = 0;
            Collection<MyNode> predecessors = null;
            Map<MyNode, Integer> predecessorContributionMap = null;
            Map<MyNode, Integer> predecessorUniqueContributionMap = null;

            if (MyVars.currentGraphDepth == 0) {
                predecessors = MyVars.g.getPredecessors(MyVars.getViewer().selectedNode);
                predecessorContributionMap = this.getPredecessorContributions(predecessors);
                predecessorUniqueContributionMap = this.getPredecessorUniqueContributions(predecessors);
            } else {
                predecessors = this.getDepthPredecessors();
                predecessorContributionMap = this.getDepthPredecessorContributions(predecessors);
                predecessorUniqueContributionMap = this.getDepthPredecessorContributions(predecessors);
            }

            for (MyNode predecessor : predecessors) {
                model.addRow(new String[]{
                    String.valueOf(++nodeCnt),
                    MySysUtil.decodeNodeName(predecessor.getName()),
                    MyMathUtil.getCommaSeperatedNumber(predecessorContributionMap.get(predecessor)),
                    MyMathUtil.getCommaSeperatedNumber(predecessorUniqueContributionMap.get(predecessor))});
            }
            this.table = new JTable(model);
            this.table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            this.table.getColumnModel().getColumn(0).setPreferredWidth(60);
            this.table.getColumnModel().getColumn(1).setPreferredWidth(250);
            this.table.getColumnModel().getColumn(2).setPreferredWidth(160);
            this.table.getColumnModel().getColumn(3).setPreferredWidth(160);

            this.table.setBackground(Color.WHITE);
            this.table.setFont(MyVars.f_pln_12);
            this.table.getTableHeader().setBackground(Color.ORANGE);
            this.table.getTableHeader().setBackground(Color.LIGHT_GRAY);
            this.table.setRowHeight(24);
        } catch (Exception ex) {}
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
