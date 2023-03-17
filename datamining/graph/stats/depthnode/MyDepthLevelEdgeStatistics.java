package datamining.graph.stats.depthnode;

import datamining.graph.MyEdge;
import datamining.graph.MyNode;
import datamining.main.MyProgressBar;
import datamining.utils.MyMathUtil;
import datamining.utils.system.MySysUtil;
import datamining.utils.system.MyVars;
import datamining.utils.table.MyTableUtil;

import javax.swing.*;
import javax.swing.border.Border;
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

public class MyDepthLevelEdgeStatistics
extends JPanel
implements ActionListener {

    private String [] timeConstrainedColumns = {
            "NO.",
            "SOURCE",
            "DEST",
            "CONTRIBUTION"
    };


    private String [][] data = {};
    private JTable table;
    private DefaultTableModel model;
    private JTextField searchTxt = new JTextField();
    private JButton save = new JButton(" SAVE ");

    public MyDepthLevelEdgeStatistics() {
        this.decorate();
        JFrame frame = new JFrame("DEPTH EDGE STRENGTH STATISTICS");
        frame.setLayout(new BorderLayout(5,5));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(this, BorderLayout.CENTER);
        frame.pack();
        frame.setPreferredSize(new Dimension(423, 600));
        frame.setVisible(true);
    }

    private void decorate() {
        MyProgressBar pb = new MyProgressBar(false);
        this.setLayout(new BorderLayout(5, 5));
        this.setBackground(Color.WHITE);
        pb.updateValue(10, 100);

        if (MyVars.isTimeOn) {this.setEdgeStatistics();
        } else {this.setEdgeStatistics();}

        JLabel numberOfNodeLabel = new JLabel("NO. OF EDGES: " + MyMathUtil.getCommaSeperatedNumber(this.table.getRowCount()));
        numberOfNodeLabel.setBackground(Color.WHITE);
        numberOfNodeLabel.setFont(MyVars.tahomaPlainFont12);

        JLabel selectedNode = new JLabel("");
        selectedNode.setFont(MyVars.tahomaPlainFont12);
        selectedNode.setBackground(Color.WHITE);

        this.table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                selectedNode.setText("    SELECTED EDGE: " + table.getValueAt(table.getSelectedRow(), 0).toString());
            }
        });

        this.searchTxt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                numberOfNodeLabel.setText("NO. OF EDGES: " + MyMathUtil.getCommaSeperatedNumber(table.getRowCount()));
            }
        });

        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        infoPanel.add(numberOfNodeLabel);
        infoPanel.add(selectedNode);

        JScrollPane tableScrollPane = new JScrollPane(this.table);
        this.add(infoPanel, BorderLayout.NORTH);
        this.add(tableScrollPane, BorderLayout.CENTER);
        JPanel searchAndSavePanel = MyTableUtil.searchAndSaveDataPanelForJTable2(this, searchTxt, this.save, this.model, this.table);
        this.add(searchAndSavePanel, BorderLayout.SOUTH);
        this.save.setPreferredSize(new Dimension(70, 30));
        this.searchTxt.setPreferredSize(new Dimension(100, 30));
        this.searchTxt.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        this.save.setPreferredSize(new Dimension(70, 29));
        this.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.table.setRowSorter(MyTableUtil.setJTableRowSorterWithTextField(this.model, this.searchTxt));
        this.table.setSelectionBackground(Color.PINK);
        this.table.setSelectionForeground(Color.BLACK);
        this.table.setFocusable(false);
        table.getTableHeader().setOpaque(false);
        table.getTableHeader().setBackground(new Color(0,0,0,0f));
        Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
        this.table.setCursor(cursor);
        this.setPreferredSize(new Dimension(460, 600));

        Border blackline = BorderFactory.createLineBorder(Color.black);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(blackline, "EDGE STATISTICS");
        titledBorder.setTitleJustification(TitledBorder.LEFT);
        titledBorder.setTitleFont(new Font("Arial", Font.PLAIN, 0));
        titledBorder.setTitleColor(Color.DARK_GRAY);
        this.setBorder(titledBorder);
        pb.updateValue(100, 100);
        pb.dispose();
    }

    private Map<MyNode, Integer> getContributionCountByNode(Collection<MyNode> nodes) {
        Map<MyNode, Integer> nodeContributionByDepthMap = new HashMap<>();
        try {
            for (MyNode n : nodes) {
                int cont = 0;
                for (int s = 0; s < MyVars.seqs.length; s++) {
                    if (MyVars.seqs[s].length-1 >= MyVars.currentGraphDepth) {
                        String iset = (MyVars.isTimeOn ? MyVars.seqs[s][MyVars.currentGraphDepth].split(":")[0] : MyVars.seqs[s][MyVars.currentGraphDepth]);
                        if (n.getName().equals(iset)) {cont++;}
                    }
                }
                nodeContributionByDepthMap.put(n, cont);
            }
        } catch (Exception ex) {ex.printStackTrace();}
        return nodeContributionByDepthMap;
    }

    private Map<MyNode, Integer> getInContributionCountByNode(Collection<MyNode> nodes) {
        Map<MyNode, Integer> nodeContributionByDepthMap = new HashMap<>();
        try {
            if (MyVars.currentGraphDepth == 0) {return nodeContributionByDepthMap;}
            for (MyNode n : nodes) {
                int inContribution = 0;
                for (int s = 0; s < MyVars.seqs.length; s++) {
                    if (MyVars.seqs[s].length-1 >= MyVars.currentGraphDepth) {
                        String iset = (MyVars.isTimeOn ? MyVars.seqs[s][MyVars.currentGraphDepth].split(":")[0] :
                            MyVars.seqs[s][MyVars.currentGraphDepth]);
                        if (n.getName().equals(iset)) {inContribution++;}
                    }
                }
                nodeContributionByDepthMap.put(n, inContribution);
            }
        } catch (Exception ex) {ex.printStackTrace();}
        return nodeContributionByDepthMap;
    }

    private Map<MyNode, Integer> getOutContributionCountByNode(Collection<MyNode> nodes) {
        Map<MyNode, Integer> outContributionByDepthMap = new HashMap<>();
        try {
            if (MyVars.currentGraphDepth == MyVars.mxDepth -1) {return outContributionByDepthMap;}
            for (MyNode n : nodes) {
                int inCont = 0;
                for (int s = 0; s < MyVars.seqs.length; s++) {
                    if (MyVars.seqs[s].length-1 > MyVars.currentGraphDepth) {
                        String iset = (MyVars.isTimeOn ? MyVars.seqs[s][MyVars.currentGraphDepth].split(":")[0] : MyVars.seqs[s][MyVars.currentGraphDepth]);
                        if (n.getName().equals(iset)) {inCont++;}
                    }
                }
                outContributionByDepthMap.put(n, inCont);
            }
        } catch (Exception ex) {ex.printStackTrace();}
        return outContributionByDepthMap;
    }

    private Map<MyNode, Integer> getSuccessorCountByNode(Collection<MyNode> nodes) {
        Map<MyNode, Integer> successorCountByNodeMap = new HashMap<>();
        try {
            for (MyNode n : nodes) {
                Set<MyNode> sSet = new HashSet<>();
                for (int s = 0; s < MyVars.seqs.length; s++) {
                    if (MyVars.seqs[s].length-1 >= MyVars.currentGraphDepth) {
                        String iset = (MyVars.isTimeOn ? MyVars.seqs[s][MyVars.currentGraphDepth].split(":")[0] : MyVars.seqs[s][MyVars.currentGraphDepth]);
                        if (n.getName().equals(iset) && MyVars.currentGraphDepth != MyVars.seqs[s].length-1) {
                            MyNode suc = (MyVars.isTimeOn ? (MyNode) MyVars.g.vRefs.get(MyVars.seqs[s][MyVars.currentGraphDepth +1].split(":")[0]) :
                            (MyNode) MyVars.g.vRefs.get(MyVars.seqs[s][MyVars.currentGraphDepth +1]));
                            sSet.add(suc);
                        }
                    }
                }
                successorCountByNodeMap.put(n, sSet.size());
            }
        } catch (Exception ex) {ex.printStackTrace();}
        return successorCountByNodeMap;
    }

    private Map<MyNode, Integer> getPredecessorCountByNode(Collection<MyNode> nodes) {
        Map<MyNode, Integer> predecessorCountByNodeMap = new HashMap<>();
        try {
            for (MyNode n : nodes) {
                Set<MyNode> pSet = new HashSet<>();
                for (int s = 0; s < MyVars.seqs.length; s++) {
                    if (MyVars.currentGraphDepth != 0) {
                        if (MyVars.seqs[s].length-1 >= MyVars.currentGraphDepth) {
                            String iset = (MyVars.isTimeOn ? MyVars.seqs[s][MyVars.currentGraphDepth].split(":")[0] : MyVars.seqs[s][MyVars.currentGraphDepth]);
                            if (n.getName().equals(iset)) {
                                MyNode pred = (MyVars.isTimeOn ? (MyNode) MyVars.g.vRefs.get(MyVars.seqs[s][MyVars.currentGraphDepth -1].split(":")[0]) :
                                    (MyNode) MyVars.g.vRefs.get(MyVars.seqs[s][MyVars.currentGraphDepth - 1]));
                                pSet.add(pred);
                            }
                        }
                    }
                }
                predecessorCountByNodeMap.put(n, pSet.size());
            }
        } catch (Exception ex) {ex.printStackTrace();}
        return predecessorCountByNodeMap;
    }

    private Map<MyNode, Integer> getNodeDifferenceCountByNode(Collection<MyNode> nodes) {
        Map<MyNode, Integer> pCntMap = this.getPredecessorCountByNode(nodes);
        Map<MyNode, Integer> sCntMap = this.getSuccessorCountByNode(nodes);
        Map<MyNode, Integer> nDiffMap = new HashMap<>();
        try {
            for (MyNode n : nodes) {
                int pCnt = pCntMap.get(n);
                int sCnt = sCntMap.get(n);
                int diff = Math.abs(pCnt-sCnt);
                nDiffMap.put(n, diff);
            }
        } catch (Exception ex) {ex.printStackTrace();}
        return nDiffMap;
    }

    private Map<MyNode, Long> getReachTimeByNode(Collection<MyNode> nodes) {
        Map<MyNode, Long> reachTimeByNode = new HashMap<>();
        try {
            for (MyNode n : nodes) {
                long reachTime = 0;
                for (int s = 0; s < MyVars.seqs.length; s++) {
                    if (MyVars.seqs[s].length-1 >= MyVars.currentGraphDepth) {
                        String [] nodeAndTime = MyVars.seqs[s][MyVars.currentGraphDepth].split(":");
                        long t = Long.valueOf(MyVars.seqs[s][MyVars.currentGraphDepth].split(":")[1]);
                        if (n.getName().equals(nodeAndTime[0])) {reachTime += t;}
                    }
                }
                reachTimeByNode.put(n, reachTime);
            }
        } catch (Exception ex) {ex.printStackTrace();}
        return reachTimeByNode;
    }

    private Map<MyNode, Integer> getEndCountByNode(Collection<MyNode> nodes) {
        Map<MyNode, Integer> endCntByNodeMap = new HashMap<>();
        try {
            for (MyNode n : nodes) {
                int endCnt = 0;
                for (int s = 0; s < MyVars.seqs.length; s++) {
                    if (MyVars.seqs[s].length-1 >= MyVars.currentGraphDepth) {
                        String iset = (MyVars.isTimeOn ? MyVars.seqs[s][MyVars.currentGraphDepth].split(":")[0] : MyVars.seqs[s][MyVars.currentGraphDepth]);
                        if (((MyVars.seqs[s].length-1)-MyVars.currentGraphDepth) == 0) {
                            if (n.getName().equals(iset)) {endCnt++;}
                        }
                    }
                }
                endCntByNodeMap.put(n, endCnt);
            }
        } catch (Exception ex) {ex.printStackTrace();}
        return endCntByNodeMap;
    }

    private void setEdgeStatistics() {
        try {
            this.model = new DefaultTableModel(this.data, this.timeConstrainedColumns);
            LinkedHashMap<String, Long> edgeMap = new LinkedHashMap<>();
            Collection<MyEdge> edges = MyVars.g.getEdges();
            for (MyEdge e : edges) {
                if (e.getCurrentValue() == 0) continue;
                String en = e.getSource().getName() + "-" + e.getDest().getName();
                edgeMap.put(en, (long)e.getCurrentValue());
            }

            int nodeCnt = 0;
            edgeMap = MySysUtil.sortMapByLongValue(edgeMap);
            for (String en : edgeMap.keySet()) {
                String [] nodePair = en.split("-");
                model.addRow(new String[]{
                        String.valueOf(++nodeCnt),
                        MySysUtil.decodeNodeName(nodePair[0]),
                        MySysUtil.decodeNodeName(nodePair[1]),
                        MyMathUtil.getCommaSeperatedNumber(edgeMap.get(en))
                });
            }

            this.table = new JTable(model);
            this.table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            this.table.getColumnModel().getColumn(0).setPreferredWidth(60);
            this.table.getColumnModel().getColumn(1).setPreferredWidth(130);
            this.table.getColumnModel().getColumn(2).setPreferredWidth(130);
            this.table.getColumnModel().getColumn(3).setPreferredWidth(100);

            this.table.setBackground(Color.WHITE);
            this.table.setFont(MyVars.f_pln_12);
            this.table.getTableHeader().setFont(MyVars.tahomaBoldFont12);
            this.table.getTableHeader().setBackground(Color.LIGHT_GRAY);
            this.table.setRowHeight(25);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override public void actionPerformed(ActionEvent e) {}
}
