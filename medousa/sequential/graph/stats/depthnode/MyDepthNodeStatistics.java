package medousa.sequential.graph.stats.depthnode;

import medousa.MyProgressBar;
import medousa.sequential.graph.MyNode;
import medousa.sequential.utils.MyMathUtil;
import medousa.sequential.utils.MySequentialGraphSysUtil;
import medousa.sequential.utils.MySequentialGraphVars;
import medousa.table.MyTableUtil;

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

public class MyDepthNodeStatistics
extends JPanel
implements ActionListener {

    private String [] timeConstrainedColumns = {
            "NO.",
            "NODE",
            "CONTRIBUTION"
    };

    private String [] columns = {
            "NO.",
            "NODE",
            "CONTRIBUTION"
    };

    private String [][] data = {};
    private JTable table;
    private DefaultTableModel model;
    private JTextField searchTxt = new JTextField();
    private JButton save = new JButton(" SAVE ");

    public MyDepthNodeStatistics() {
        this.decorate();
        JFrame f = new JFrame("DEPTH NODE STRENGTH STATISTICS");
        f.setLayout(new BorderLayout(5,5));
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.getContentPane().add(this, BorderLayout.CENTER);
        f.pack();
        f.setPreferredSize(new Dimension(450, 600));
        f.setAlwaysOnTop(true);
        f.setVisible(true);
        f.setAlwaysOnTop(false);
    }

    private void decorate() {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            this.setLayout(new BorderLayout(5, 5));
            this.setBackground(Color.WHITE);
            pb.updateValue(10, 100);

            if (MySequentialGraphVars.isTimeOn) {
                this.setNodeStatistics();
            } else {
                this.setNodeStatistics();
            }

            JLabel numberOfNodeLabel = new JLabel("NO. OF NODES: " + MyMathUtil.getCommaSeperatedNumber(this.table.getRowCount()));
            numberOfNodeLabel.setBackground(Color.WHITE);
            numberOfNodeLabel.setFont(MySequentialGraphVars.tahomaPlainFont12);

            JLabel selectedNode = new JLabel("");
            selectedNode.setFont(MySequentialGraphVars.tahomaPlainFont12);
            selectedNode.setBackground(Color.WHITE);

            this.table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
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
            table.getTableHeader().setBackground(new Color(0, 0, 0, 0f));
            Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
            this.table.setCursor(cursor);
            this.setPreferredSize(new Dimension(460, 600));

            Border blackline = BorderFactory.createLineBorder(Color.black);
            TitledBorder titledBorder = BorderFactory.createTitledBorder(blackline, "NODE STATISTICS");
            titledBorder.setTitleJustification(TitledBorder.LEFT);
            titledBorder.setTitleFont(new Font("Arial", Font.PLAIN, 0));
            titledBorder.setTitleColor(Color.DARK_GRAY);
            this.setBorder(titledBorder);
            pb.updateValue(100, 100);
            pb.dispose();
        } catch (Exception ex) {
            pb.updateValue(100, 100);
            pb.dispose();
        }
    }

    private Map<MyNode, Integer> getContributionCountByNode(Collection<MyNode> nodes) {
        Map<MyNode, Integer> nodeContributionByDepthMap = new HashMap<>();
        try {
            for (MyNode n : nodes) {
                int cont = 0;
                for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                    if (MySequentialGraphVars.seqs[s].length-1 >= MySequentialGraphVars.currentGraphDepth) {
                        String iset = (MySequentialGraphVars.isTimeOn ? MySequentialGraphVars.seqs[s][MySequentialGraphVars.currentGraphDepth].split(":")[0] : MySequentialGraphVars.seqs[s][MySequentialGraphVars.currentGraphDepth]);
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
            if (MySequentialGraphVars.currentGraphDepth == 0) {return nodeContributionByDepthMap;}
            for (MyNode n : nodes) {
                int inContribution = 0;
                for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                    if (MySequentialGraphVars.seqs[s].length-1 >= MySequentialGraphVars.currentGraphDepth) {
                        String iset = (MySequentialGraphVars.isTimeOn ? MySequentialGraphVars.seqs[s][MySequentialGraphVars.currentGraphDepth].split(":")[0] :
                            MySequentialGraphVars.seqs[s][MySequentialGraphVars.currentGraphDepth]);
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
            if (MySequentialGraphVars.currentGraphDepth == MySequentialGraphVars.mxDepth -1) {return outContributionByDepthMap;}
            for (MyNode n : nodes) {
                int inCont = 0;
                for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                    if (MySequentialGraphVars.seqs[s].length-1 > MySequentialGraphVars.currentGraphDepth) {
                        String iset = (MySequentialGraphVars.isTimeOn ? MySequentialGraphVars.seqs[s][MySequentialGraphVars.currentGraphDepth].split(":")[0] : MySequentialGraphVars.seqs[s][MySequentialGraphVars.currentGraphDepth]);
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
                for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                    if (MySequentialGraphVars.seqs[s].length-1 >= MySequentialGraphVars.currentGraphDepth) {
                        String iset = (MySequentialGraphVars.isTimeOn ? MySequentialGraphVars.seqs[s][MySequentialGraphVars.currentGraphDepth].split(":")[0] : MySequentialGraphVars.seqs[s][MySequentialGraphVars.currentGraphDepth]);
                        if (n.getName().equals(iset) && MySequentialGraphVars.currentGraphDepth != MySequentialGraphVars.seqs[s].length-1) {
                            MyNode suc = (MySequentialGraphVars.isTimeOn ? (MyNode) MySequentialGraphVars.g.vRefs.get(MySequentialGraphVars.seqs[s][MySequentialGraphVars.currentGraphDepth +1].split(":")[0]) :
                            (MyNode) MySequentialGraphVars.g.vRefs.get(MySequentialGraphVars.seqs[s][MySequentialGraphVars.currentGraphDepth +1]));
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
                for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                    if (MySequentialGraphVars.currentGraphDepth != 0) {
                        if (MySequentialGraphVars.seqs[s].length-1 >= MySequentialGraphVars.currentGraphDepth) {
                            String iset = (MySequentialGraphVars.isTimeOn ? MySequentialGraphVars.seqs[s][MySequentialGraphVars.currentGraphDepth].split(":")[0] : MySequentialGraphVars.seqs[s][MySequentialGraphVars.currentGraphDepth]);
                            if (n.getName().equals(iset)) {
                                MyNode pred = (MySequentialGraphVars.isTimeOn ? (MyNode) MySequentialGraphVars.g.vRefs.get(MySequentialGraphVars.seqs[s][MySequentialGraphVars.currentGraphDepth -1].split(":")[0]) :
                                    (MyNode) MySequentialGraphVars.g.vRefs.get(MySequentialGraphVars.seqs[s][MySequentialGraphVars.currentGraphDepth - 1]));
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
                for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                    if (MySequentialGraphVars.seqs[s].length-1 >= MySequentialGraphVars.currentGraphDepth) {
                        String [] nodeAndTime = MySequentialGraphVars.seqs[s][MySequentialGraphVars.currentGraphDepth].split(":");
                        long t = Long.valueOf(MySequentialGraphVars.seqs[s][MySequentialGraphVars.currentGraphDepth].split(":")[1]);
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
                for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                    if (MySequentialGraphVars.seqs[s].length-1 >= MySequentialGraphVars.currentGraphDepth) {
                        String iset = (MySequentialGraphVars.isTimeOn ? MySequentialGraphVars.seqs[s][MySequentialGraphVars.currentGraphDepth].split(":")[0] : MySequentialGraphVars.seqs[s][MySequentialGraphVars.currentGraphDepth]);
                        if (((MySequentialGraphVars.seqs[s].length-1)- MySequentialGraphVars.currentGraphDepth) == 0) {
                            if (n.getName().equals(iset)) {endCnt++;}
                        }
                    }
                }
                endCntByNodeMap.put(n, endCnt);
            }
        } catch (Exception ex) {ex.printStackTrace();}
        return endCntByNodeMap;
    }

    private void setNodeStatistics() {
        try {
            this.model = new DefaultTableModel(this.data, this.timeConstrainedColumns);
            if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthSelecter.getSelectedIndex() > 0 && MySequentialGraphVars.getSequentialGraphViewer().vc.depthNeighborNodeTypeSelector.getSelectedIndex() > 0 &&
                MySequentialGraphVars.getSequentialGraphViewer().vc.depthNeighborNodeTypeSelector.getSelectedItem().toString().equals("S.")) {
                LinkedHashMap<String, Long> successorMap = new LinkedHashMap<>();
                for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                    if ((MySequentialGraphVars.seqs[s].length) <= MySequentialGraphVars.currentGraphDepth) continue;
                    String depthNode = MySequentialGraphVars.seqs[s][MySequentialGraphVars.currentGraphDepth-1].split(":")[0];
                    if (depthNode.contains("x")) {
                        for (int i = MySequentialGraphVars.currentGraphDepth; i < MySequentialGraphVars.seqs[s].length; i++) {
                            String successor = MySequentialGraphVars.seqs[s][i].split(":")[0];
                            if (successorMap.containsKey(successor)) {
                                successorMap.put(successor, successorMap.get(successor)+1);
                            } else {
                                successorMap.put(successor, 1L);
                            }
                        }
                    } else {
                        String successor = MySequentialGraphVars.seqs[s][MySequentialGraphVars.currentGraphDepth].split(":")[0];
                        if (successorMap.containsKey(successor)) {
                            successorMap.put(successor, successorMap.get(successor)+1);
                        } else {
                            successorMap.put(successor, 1L);
                        }
                    }
                }

                int nodeCnt = 0;
                successorMap = MySequentialGraphSysUtil.sortMapByLongValue(successorMap);
                Collection<MyNode> depthNodes = MySequentialGraphVars.g.getDepthNodes();
                for (String nn : successorMap.keySet()) {
                    MyNode n = (MyNode) MySequentialGraphVars.g.vRefs.get(nn);
                    if (!depthNodes.contains(n)) {
                        model.addRow(new String[]{
                                String.valueOf(++nodeCnt),
                                MySequentialGraphSysUtil.decodeNodeName(nn),
                                MyMathUtil.getCommaSeperatedNumber(successorMap.get(nn))
                        });
                    }
                }
            } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthSelecter.getSelectedIndex() > 0 && MySequentialGraphVars.getSequentialGraphViewer().vc.depthNeighborNodeTypeSelector.getSelectedIndex() > 0 &&
                MySequentialGraphVars.getSequentialGraphViewer().vc.depthNeighborNodeTypeSelector.getSelectedItem().toString().equals("P.")) {
                LinkedHashMap<String, Long> predecessorMap = new LinkedHashMap<>();
                for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                    if (MySequentialGraphVars.seqs[s].length < MySequentialGraphVars.currentGraphDepth) continue;
                    String predecessor = MySequentialGraphVars.seqs[s][MySequentialGraphVars.currentGraphDepth-2].split(":")[0];
                    if (predecessorMap.containsKey(predecessor)) {
                        predecessorMap.put(predecessor, predecessorMap.get(predecessor) + 1);
                    } else {
                        predecessorMap.put(predecessor, 1L);
                    }
                }

                int nodeCnt = 0;
                predecessorMap = MySequentialGraphSysUtil.sortMapByLongValue(predecessorMap);
                Collection<MyNode> depthNodes = MySequentialGraphVars.g.getDepthNodes();
                for (String nn : predecessorMap.keySet()) {
                    MyNode n = (MyNode) MySequentialGraphVars.g.vRefs.get(nn);
                    if (!depthNodes.contains(n)) {
                        model.addRow(new String[]{
                                String.valueOf(++nodeCnt),
                                MySequentialGraphSysUtil.decodeNodeName(nn),
                                MyMathUtil.getCommaSeperatedNumber(predecessorMap.get(nn))
                        });
                    }
                }
            } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthSelecter.getSelectedIndex() > 0) {
                int nodeCnt = 0;
                Collection<MyNode> depthNodes = MySequentialGraphVars.g.getDepthNodes();
                for (MyNode n : depthNodes) {
                    model.addRow(new String[]{
                        String.valueOf(++nodeCnt),
                        MySequentialGraphSysUtil.decodeNodeName(n.getName()),
                        MyMathUtil.getCommaSeperatedNumber(n.getNodeDepthInfo(MySequentialGraphVars.currentGraphDepth).getContribution())
                    });
                }
            }
            this.table = new JTable(model);
            this.table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            this.table.getColumnModel().getColumn(0).setPreferredWidth(60);
            this.table.getColumnModel().getColumn(1).setPreferredWidth(250);
            this.table.getColumnModel().getColumn(2).setPreferredWidth(130);

            this.table.setBackground(Color.WHITE);
            this.table.setFont(MySequentialGraphVars.f_pln_12);
            this.table.getTableHeader().setFont(MySequentialGraphVars.tahomaBoldFont12);
            this.table.getTableHeader().setBackground(Color.LIGHT_GRAY);
            this.table.setRowHeight(25);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override public void actionPerformed(ActionEvent e) {}
}
