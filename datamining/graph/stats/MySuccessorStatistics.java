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
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;

public class MySuccessorStatistics
extends JPanel
implements ActionListener {

    private String [] columns = {
            "NO.",
            "SUCCESSOR",
            "CONTRIBUTION",
            "UNIQUE CONTRIBUTION",
            "REACH TIME"
    };
    private String [][] data = {};
    private JTable table;
    private DefaultTableModel model;
    private JTextField searchTxt = new JTextField();
    private JButton save = new JButton(" SAVE ");

    public MySuccessorStatistics() {
        this.decorate();
        JFrame frame = new JFrame("SUCCESSOR SUMMARY STATISTICS");
        frame.setLayout(new BorderLayout(5,5));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(this, BorderLayout.CENTER);
        frame.pack();
        frame.setPreferredSize(new Dimension(800, 600));
        frame.setVisible(true);
    }

    private void decorate() {
        MyProgressBar pb = new MyProgressBar(false);
        this.setLayout(new BorderLayout(5, 5));
        this.setBackground(Color.WHITE);
        pb.updateValue(10, 100);

        if (MyVars.isTimeOn) {
            this.setTimeConstrainedSuccessorStatistics();
        } else {
            this.setSuccessorStatistics();
        }

        JLabel numberOfNodeLabel = new JLabel("NO. OF NODES: " + MyMathUtil.getCommaSeperatedNumber(this.table.getRowCount()));
        numberOfNodeLabel.setBackground(Color.WHITE);
        numberOfNodeLabel.setFont(MyVars.tahomaPlainFont12);

        JLabel selectedNode = new JLabel("");
        selectedNode.setFont(MyVars.tahomaPlainFont12);
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
        JPanel searchAndSaveJPanel = MyTableUtil.searchAndSaveDataPanelForJTable2(this, this.searchTxt, this.save, this.model, this.table);
        searchAndSaveJPanel.setBackground(Color.WHITE);
        this.save.setPreferredSize(new Dimension(70, 29));
        this.add(searchAndSaveJPanel, BorderLayout.SOUTH);
        this.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.table.setRowSorter(MyTableUtil.setJTableRowSorterWithTextField(this.model, this.searchTxt));
        this.table.setSelectionBackground(Color.BLACK);
        this.table.setSelectionForeground(Color.WHITE);
        this.table.setFocusable(false);
        this.table.getTableHeader().setFont(MyVars.tahomaBoldFont12);
        Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
        this.table.setCursor(cursor);
        this.setPreferredSize(new Dimension(450, 700));

        Border blackline = BorderFactory.createLineBorder(Color.black);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(blackline, "NODE STATISTICS");
        titledBorder.setTitleJustification(TitledBorder.LEFT);
        titledBorder.setTitleFont(MyVars.tahomaBoldFont12);
        titledBorder.setTitleColor(Color.DARK_GRAY);
        this.setBorder(titledBorder);
        pb.updateValue(100, 100);
        pb.dispose();
    }

    private Map<MyNode, Integer> getSuccessorContributions(Collection<MyNode> successors) {
        Map<MyNode, Integer> successorContributionMap = new HashMap<>();
        try {
            for (MyNode sNode : successors) {
                int cont = 0;
                for (int s = 0; s < MyVars.seqs.length; s++) {
                    for (int i = 1; i < MyVars.seqs[s].length; i++) {
                        String pred = MyVars.seqs[s][i-1].split(":")[0];
                        String suc = MyVars.seqs[s][i].split(":")[0];
                        if (sNode.getName().equals(suc) && pred.equals(MyVars.getViewer().selectedNode.getName())) {
                            cont++;
                        }
                    }
                }
                successorContributionMap.put(sNode, cont);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return successorContributionMap;
    }

    private Map<MyNode, Integer> getSuccessorUniqueContributions(Collection<MyNode> successors) {
        Map<MyNode, Integer> sucUniqContMap = new HashMap<>();
        try {
            for (MyNode sNode : successors) {
                int uniqCont = 0;
                for (int s = 0; s < MyVars.seqs.length; s++) {
                    for (int i = 1; i < MyVars.seqs[s].length; i++) {
                        String pred = MyVars.seqs[s][i-1].split(":")[0];
                        String suc = MyVars.seqs[s][i].split(":")[0];
                        if (sNode.getName().equals(suc) && pred.equals(MyVars.getViewer().selectedNode.getName())) {
                            uniqCont++;
                            break;
                        }
                    }
                }
                sucUniqContMap.put(sNode, uniqCont);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return sucUniqContMap;
    }

    private Map<MyNode, Long> getSuccessorReachTimes(Collection<MyNode> successors) {
        Map<MyNode, Long> successorReachTimeMap = new HashMap<>();
        try {
            for (MyNode sNode : successors) {
                long reachTime = 0;
                for (int sequence = 0; sequence < MyVars.seqs.length; sequence++) {
                    for (int i = 1; i < MyVars.seqs[sequence].length; i++) {
                        String pred = MyVars.seqs[sequence][i - 1].split(":")[0];
                        String suc = MyVars.seqs[sequence][i].split(":")[0];
                        if (sNode.getName().equals(suc) && pred.equals(MyVars.getViewer().selectedNode.getName())) {
                            reachTime += Long.valueOf(MyVars.seqs[sequence][i].split(":")[1]);
                        }
                    }
                }
                successorReachTimeMap.put(sNode, reachTime);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return successorReachTimeMap;
    }

    private Set<MyNode> getDepthSuccessors() {
        Set<MyNode> depthSuccessorSet = new HashSet<>();
        for (int s = 0; s < MyVars.seqs.length; s++) {
            if (MyVars.seqs[s].length-1 > MyVars.currentGraphDepth) {
                String pred = MyVars.seqs[s][MyVars.currentGraphDepth].split(":")[0];
                String suc = MyVars.seqs[s][MyVars.currentGraphDepth +1].split(":")[0];
                if (pred.equals(MyVars.getViewer().selectedNode.getName())) {
                    MyNode sNode = (MyNode)MyVars.g.vRefs.get(suc);
                    if (!depthSuccessorSet.contains(sNode)) {
                        depthSuccessorSet.add(sNode);
                    }
                }
            }
        }
        return depthSuccessorSet;
    }

    private Map<MyNode, Integer> getDepthSuccessorContributions(Collection<MyNode> successors) {
        Map<MyNode, Integer> depthSuccessorContributionMap = new HashMap<>();
        try {
            for (MyNode sNode : successors) {
                int cont = 0;
                for (int s = 0; s < MyVars.seqs.length; s++) {
                    if (MyVars.seqs[s].length-1 > MyVars.currentGraphDepth) {
                        String pred = MyVars.seqs[s][MyVars.currentGraphDepth].split(":")[0];
                        String suc = MyVars.seqs[s][MyVars.currentGraphDepth +1].split(":")[0];
                        if (pred.equals(MyVars.getViewer().selectedNode.getName()) && suc.equals(sNode.getName())) {
                            cont++;
                        }
                    }
                }
                depthSuccessorContributionMap.put(sNode, cont);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return depthSuccessorContributionMap;
    }

    private Map<MyNode, Integer> getDepthSuccessorUniqueContributions(Collection<MyNode> successors) {
        Map<MyNode, Integer> depthSuccessorContributionMap = new HashMap<>();
        try {
            for (MyNode sNode : successors) {
                int cont = 0;
                for (int s = 0; s < MyVars.seqs.length; s++) {
                    if (MyVars.seqs[s].length-1 > MyVars.currentGraphDepth) {
                        String pred = MyVars.seqs[s][MyVars.currentGraphDepth].split(":")[0];
                        String suc = MyVars.seqs[s][MyVars.currentGraphDepth +1].split(":")[0];
                        if (pred.equals(MyVars.getViewer().selectedNode.getName()) && suc.equals(sNode.getName())) {
                            cont++;
                        }
                    }
                }
                depthSuccessorContributionMap.put(sNode, cont);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return depthSuccessorContributionMap;
    }

    private Map<MyNode, Long> getDepthSuccessorReachTimes(Collection<MyNode> successors) {
        Map<MyNode, Long> depthSuccessorReachTimeMap = new HashMap<>();
        try {
            for (MyNode sNode : successors) {
                long reachTime = 0;
                for (int s = 0; s < MyVars.seqs.length; s++) {
                    if (MyVars.seqs[s].length-1 > MyVars.currentGraphDepth) {
                        String pred = MyVars.seqs[s][MyVars.currentGraphDepth].split(":")[0];
                        String suc = MyVars.seqs[s][MyVars.currentGraphDepth +1].split(":")[0];
                        if (pred.equals(MyVars.getViewer().selectedNode.getName()) && suc.equals(sNode.getName())) {
                            reachTime += Long.valueOf(MyVars.seqs[s][MyVars.currentGraphDepth].split(":")[1]);
                        }
                    }
                }
                depthSuccessorReachTimeMap.put(sNode, reachTime);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return depthSuccessorReachTimeMap;
    }

    private void setTimeConstrainedSuccessorStatistics() {
        try {
            this.model = new DefaultTableModel(this.data, this.columns);

            int nodeCnt = 0;
            Collection<MyNode> successors = null;
            Map<MyNode, Integer> successorContributionMap = null;
            Map<MyNode, Integer> successorUniqueContributionMap = null;
            Map<MyNode, Long> successorReachTimeMap = null;
            if (MyVars.currentGraphDepth == -1) {
                successors = MyVars.g.getSuccessors(MyVars.getViewer().selectedNode);
                successorContributionMap = this.getSuccessorContributions(successors);
                successorUniqueContributionMap = this.getSuccessorUniqueContributions(successors);
                successorReachTimeMap = this.getSuccessorReachTimes(successors);
            } else {
                successors = this.getDepthSuccessors();
                successorContributionMap = this.getDepthSuccessorContributions(successors);
                successorUniqueContributionMap = this.getDepthSuccessorUniqueContributions(successors);
                successorReachTimeMap = this.getDepthSuccessorReachTimes(successors);
            }

            for (MyNode successor : successors) {
                model.addRow(new String[]{
                        String.valueOf(++nodeCnt),
                        MySysUtil.decodeNodeName(successor.getName()),
                        MyMathUtil.getCommaSeperatedNumber(successorContributionMap.get(successor)),
                        MyMathUtil.getCommaSeperatedNumber(successorUniqueContributionMap.get(successor)),
                        MyMathUtil.getCommaSeperatedNumber(successorReachTimeMap.get(successor))
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
            JTableHeader header = table.getTableHeader();
            header.setBackground(Color.ORANGE);
            this.table.getTableHeader().setBackground(Color.LIGHT_GRAY);
            this.table.setRowHeight(24);
        } catch (Exception ex) {}
    }

    private void setSuccessorStatistics() {
        try {
            this.model = new DefaultTableModel(this.data, this.columns);
            int nodeCnt = 0;
            Collection<MyNode> successors = null;
            Map<MyNode, Integer> successorContributionMap = null;
            Map<MyNode, Integer> successorUniqueContributionMap = null;
            Map<MyNode, Long> successorReachTimeMap = null;
            if (MyVars.currentGraphDepth == -1) {
                successors = MyVars.g.getSuccessors(MyVars.getViewer().selectedNode);
                successorContributionMap = this.getSuccessorContributions(successors);
                successorUniqueContributionMap = this.getSuccessorUniqueContributions(successors);
            } else {
                successors = this.getDepthSuccessors();
                successorContributionMap = this.getDepthSuccessorContributions(successors);
                successorUniqueContributionMap = this.getDepthSuccessorUniqueContributions(successors);
            }
            for (MyNode successor : successors) {
                model.addRow(new String[]{
                        String.valueOf(++nodeCnt),
                        MySysUtil.decodeNodeName(successor.getName()),
                        MyMathUtil.getCommaSeperatedNumber(successorContributionMap.get(successor)),
                        MyMathUtil.getCommaSeperatedNumber(successorUniqueContributionMap.get(successor)),
                });
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
