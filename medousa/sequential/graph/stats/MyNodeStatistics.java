package medousa.sequential.graph.stats;

import medousa.MyProgressBar;
import medousa.sequential.graph.MyNode;
import medousa.sequential.utils.MyMathUtil;
import medousa.sequential.utils.MySequentialGraphSysUtil;
import medousa.sequential.utils.MySequentialGraphVars;
import medousa.table.MyTableUtil;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;

public class MyNodeStatistics
extends JPanel
implements ActionListener {

    private String [] timeConstrainedColumns = {
            "NO.",
            "NODE",
            "CONTRIBUTION",
            "IN CONTRIBUTION",
            "OUT CONTRIBUTION",
            "UNIQ. CONTRIBUTION",
            "START NODE COUNT",
            "END NODE COUNT",
            "SUCCESSORS",
            "PREDECESSORS",
            "NODE DIFF.",
            "RECURRENCE",
            "BETWEENESS",
            "CLOSENESS",
            "EIGENVECTOR",
            "AVG. REACH TIME",
            "DURATION",
            "RECURRENCE L."
    };

    private String [] columns = {
            "NO.",
            "NODE",
            "CONTRIBUTION",
            "IN CONTRIBUTION",
            "OUT CONTRIBUTION",
            "UNIQ. CONTRIBUTION",
            "START NODE COUNT",
            "END NODE COUNT",
            "SUCCESSORS",
            "PREDECESSORS",
            "NODE DIFF.",
            "RECURRENCE",
            "BETWEENESS",
            "CLOSENESS",
            "EIGENVECTOR",
            "RECURRENCE L."
    };

    private String [][] data = {};

    private JTable table;
    private DefaultTableModel model;
    private JTextField searchTxt = new JTextField();
    private JButton save = new JButton(" SAVE ");
    private JFrame frame;

    public MyNodeStatistics() {
        this.decorate();
        this.frame = new JFrame("NODE SUMMARY STATISTICS");
        this.frame.setLayout(new BorderLayout(5,5));
        this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.frame.getContentPane().add(this, BorderLayout.CENTER);
        this.frame.setPreferredSize(new Dimension(800, 600));
        this.frame.pack();
        this.frame.setLocation(MySequentialGraphSysUtil.getViewerWidth()-800, 5);
        this.frame.setAlwaysOnTop(true);
        this.frame.setVisible(true);
        this.frame.setAlwaysOnTop(false);    }

    private void decorate() {
        MyProgressBar pb = new MyProgressBar(false);
        this.setLayout(new BorderLayout(5,5));
        this.setBackground(Color.WHITE);
        pb.updateValue(10, 100);
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(1, 2));
        menuPanel.setBackground(Color.WHITE);
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        infoPanel.setBackground(Color.WHITE);
        JLabel numberOfNodeLabel = new JLabel();
        numberOfNodeLabel.setBackground(Color.WHITE);
        numberOfNodeLabel.setFont(MySequentialGraphVars.tahomaPlainFont13);
        infoPanel.add(numberOfNodeLabel);
        JLabel selectedNodeLabel = new JLabel();
        selectedNodeLabel.setFont(MySequentialGraphVars.tahomaPlainFont12);
        selectedNodeLabel.setBackground(Color.WHITE);
        infoPanel.add(selectedNodeLabel);
        JLabel selectedNode = new JLabel("");
        selectedNode.setFont(MySequentialGraphVars.tahomaPlainFont12);
        selectedNode.setBackground(Color.WHITE);
        infoPanel.add(selectedNode);
        menuPanel.add(infoPanel, BorderLayout.WEST);
        JPanel orderAndDistributionPanel = new JPanel();
        orderAndDistributionPanel.setBackground(Color.WHITE);
        orderAndDistributionPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 1));
        JLabel distributionLabel = new JLabel("DISTRIBUTIONS: ");
        distributionLabel.setFont(MySequentialGraphVars.tahomaPlainFont12);
        JComboBox distributionComboBox = new JComboBox();
        distributionComboBox.setFocusable(false);
        distributionComboBox.setBackground(Color.WHITE);
        distributionComboBox.setFont(MySequentialGraphVars.tahomaPlainFont12);
        distributionComboBox.addItem("CONTRIBUTION");
        distributionComboBox.addItem("IN CONTRIBUTION");
        distributionComboBox.addItem("OUT CONTRIBUTIOIN");
        distributionComboBox.addItem("UNIQUE CONTRIBUTION");
        distributionComboBox.addItem("OPEN NODE COUNT");
        distributionComboBox.addItem("END NODE COUNT");
        distributionComboBox.addItem("SUCCESSORS");
        distributionComboBox.addItem("PREDECESSORS");
        distributionComboBox.addItem("NODE DIFFERENCE");
        distributionComboBox.addItem("RECURRENCE");
        distributionComboBox.addItem("BETWEENESS");
        distributionComboBox.addItem("CLOSENESS");
        distributionComboBox.addItem("EIGENVECTOR");
        distributionComboBox.addItem("RECURRENCE L.");
        if (MySequentialGraphVars.isTimeOn) {
            distributionComboBox.addItem("REACH TIME");
            distributionComboBox.addItem("DURATION");
        }
        distributionComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (MySequentialGraphVars.isTimeOn) {
                    if (distributionComboBox.getSelectedIndex() == 0) {
                        showDistribution("NODE CONTRIBUTION DISTRIBUTION", 2, "CONTRIBUTION");
                    } else if (distributionComboBox.getSelectedIndex() == 1) {
                        showDistribution("NODE IN CONTRIBUTION DISTRIBUTION", 3, "IN CONTRIBUTION");
                    } else if (distributionComboBox.getSelectedIndex() == 2) {
                        showDistribution("NODE OUT CONTRIBUTION DISTRIBUTION", 4, "OUT CONTRIBUTION");
                    } else if (distributionComboBox.getSelectedIndex() == 3) {
                        showDistribution("NODE UNIQUE CONTRIBUTION DISTRIBUTION", 5, "UNIQUE CONTRIBUTION");
                    } else if (distributionComboBox.getSelectedIndex() == 4) {
                        showDistribution("OPEN NODE COUNT DISTRIBUTION", 6, "OPEN NODE COUNT");
                    } else if (distributionComboBox.getSelectedIndex() == 5) {
                        showDistribution("EDN NODE COUNT DISTRIBUTION", 7, "END NODE COUNT");
                    } else if (distributionComboBox.getSelectedIndex() == 6) {
                        showDistribution("SUCCESSOR COUNT DISTRIBUTION", 8, "SUCCESSOR COUNT");
                    } else if (distributionComboBox.getSelectedIndex() == 7) {
                        showDistribution("PREDECESSOR COUNT DISTRIBUTION", 9, "PREDECESSOR COUNT");
                    } else if (distributionComboBox.getSelectedIndex() == 8) {
                        showDistribution("NODE DIFFERENCE COUNT DISTRIBUTION", 10, "NODE DIFFERENCE COUNT");
                    } else if (distributionComboBox.getSelectedIndex() == 9) {
                        showDistribution("RECUCRRENCE COUNT DISTRIBUTION", 11, "RECURRENT COUNT");
                    } else if (distributionComboBox.getSelectedIndex() == 10) {
                        showDistribution("BETWEENESS DISTRIBUTION", 12, "BETWEENESS");
                    } else if (distributionComboBox.getSelectedIndex() == 11) {
                        showDistribution("CLOSENESS DISTRIBUTION", 13, "CLOSENESS");
                    } else if (distributionComboBox.getSelectedIndex() == 12) {
                        showDistribution("EIGENVECTOR DISTRIBUTION", 14, "EIGENVECTOR");
                    } else if (distributionComboBox.getSelectedIndex() == 13) {
                        showDistribution("NODE REACH TIME DISTRIBUTION", 15, "AVG. REACH TIME");
                    } else if (distributionComboBox.getSelectedIndex() == 14) {
                        showDistribution("NODE DURATION DISTRIBUTION", 16, "DURATION");
                    } else if (distributionComboBox.getSelectedIndex() == 15) {
                        showDistribution("RECURRENCE LENGTH DISTRIBUTION", 17, "RECURRENCE LENGTH");
                    }
                } else {
                    if (distributionComboBox.getSelectedIndex() == 0) {
                        showDistribution("NODE CONTRIBUTION DISTRIBUTION", 2, "CONTRIBUTION");
                    } else if (distributionComboBox.getSelectedIndex() == 1) {
                        showDistribution("NODE IN-CONTRIBUTION DISTRIBUTION", 3, "IN-CONTRIBUTION");
                    } else if (distributionComboBox.getSelectedIndex() == 2) {
                        showDistribution("NODE OUT-CONTRIBUTION DISTRIBUTION", 4, "OUT-CONTRIBUTION");
                    } else if (distributionComboBox.getSelectedIndex() == 3) {
                        showDistribution("NODE UNIQUE CONTRIBUTION DISTRIBUTION", 5, "UNIQUE CONTRIBUTION");
                    } else if (distributionComboBox.getSelectedIndex() == 4) {
                        showDistribution("OPEN NODE COUNT DISTRIBUTION", 6, "OPEN NODE COUNT");
                    } else if (distributionComboBox.getSelectedIndex() == 5) {
                        showDistribution("EDN NODE COUNT DISTRIBUTION", 7, "END NODE COUNT");
                    } else if (distributionComboBox.getSelectedIndex() == 6) {
                        showDistribution("SUCCESSOR COUNT DISTRIBUTION", 8, "SUCCESSOR COUNT");
                    } else if (distributionComboBox.getSelectedIndex() == 7) {
                        showDistribution("PREDECESSOR COUNT DISTRIBUTION", 9, "PREDECESSOR COUNT");
                    } else if (distributionComboBox.getSelectedIndex() == 8) {
                        showDistribution("NODE DIFFERENCE COUNT DISTRIBUTION", 10, "NODE DIFFERENCE COUNT");
                    } else if (distributionComboBox.getSelectedIndex() == 9) {
                        showDistribution("RECUCRRENCE COUNT DISTRIBUTION", 11, "RECURRENT COUNT");
                    } else if (distributionComboBox.getSelectedIndex() == 10) {
                        showDistribution("BETWEENESS DISTRIBUTION", 12, "BETWEENESS");
                    } else if (distributionComboBox.getSelectedIndex() == 11) {
                        showDistribution("CLOSENESS DISTRIBUTION", 13, "CLOSENESS");
                    } else if (distributionComboBox.getSelectedIndex() == 12) {
                        showDistribution("EIGENVECTOR DISTRIBUTION", 14, "EIGENVECTOR");
                    } else if (distributionComboBox.getSelectedIndex() == 13) {
                        showDistribution("RECURRENCE LENGTH DISTRIBUTION", 15, "RECURRENCE LENGTH");
                    }
                }
            }
        });
        JLabel orderByLabel = new JLabel("ORDER BY: ");
        orderByLabel.setFont(MySequentialGraphVars.tahomaPlainFont12);
        JComboBox nodeOrderByComboBox = new JComboBox();
        nodeOrderByComboBox.setFocusable(false);
        nodeOrderByComboBox.setBackground(Color.WHITE);
        nodeOrderByComboBox.setFont(MySequentialGraphVars.tahomaPlainFont12);
        nodeOrderByComboBox.addItem("CONTRIBUTION");
        nodeOrderByComboBox.addItem("IN CONTRIBUTION");
        nodeOrderByComboBox.addItem("OUT CONTRIBUTION");
        nodeOrderByComboBox.addItem("UNIQ. CONT.");
        nodeOrderByComboBox.addItem("OPEN NODE");
        nodeOrderByComboBox.addItem("END NODE");
        nodeOrderByComboBox.addItem("SUCCESSOR");
        nodeOrderByComboBox.addItem("PREDECESSOR");
        nodeOrderByComboBox.addItem("NODE DIFF.");
        nodeOrderByComboBox.addItem("RECURRENCE");
        nodeOrderByComboBox.addItem("BETWEENESS");
        nodeOrderByComboBox.addItem("CLOSENESS");
        nodeOrderByComboBox.addItem("EIGENVECTOR");
        nodeOrderByComboBox.addItem("RECURRENCE L.");
        if (MySequentialGraphVars.isTimeOn) {
            nodeOrderByComboBox.addItem("REACH TIME");
            nodeOrderByComboBox.addItem("DURATION");
        }
        nodeOrderByComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (MySequentialGraphVars.isTimeOn) {
                                frame.setAlwaysOnTop(true);
                                MyProgressBar pb = new MyProgressBar(false);
                                MySequentialGraphVars.nodeOrderByComboBoxIdx = nodeOrderByComboBox.getSelectedIndex();
                                for (int i = model.getRowCount() - 1; i >= 0; i--) {
                                    model.removeRow(i);
                                }
                                pb.updateValue(40, 100);
                                ArrayList<MyNode> nodes = new ArrayList<>(MySequentialGraphVars.g.getVertices());
                                Map<MyNode, Integer> inContributionByNodeMap = getInContributionByNodeMap(nodes);
                                Map<MyNode, Integer> outContributionByNodeMap = getOutContributionByNodeMap(nodes);
                                Collections.sort(nodes);
                                pb.updateValue(70, 100);
                                int nodeCnt = 0;
                                for (MyNode n : nodes) {
                                    model.addRow(new String[]{
                                            String.valueOf(++nodeCnt),
                                            MySequentialGraphSysUtil.decodeNodeName(n.getName()),
                                            MyMathUtil.getCommaSeperatedNumber(n.getContribution()),
                                            MyMathUtil.getCommaSeperatedNumber(inContributionByNodeMap.get(n) == null ? 0 : inContributionByNodeMap.get(n)),
                                            MyMathUtil.getCommaSeperatedNumber(outContributionByNodeMap.get(n) == null ? 0 : outContributionByNodeMap.get(n)),
                                            MyMathUtil.getCommaSeperatedNumber(n.getUniqueContribution()),
                                            MyMathUtil.getCommaSeperatedNumber(n.getStartPositionNodeCount()),
                                            MyMathUtil.getCommaSeperatedNumber(n.getEndPositionNodeCount()),
                                            MyMathUtil.getCommaSeperatedNumber(n.getSuccessorCount()),
                                            MyMathUtil.getCommaSeperatedNumber(n.getPredecessorCount()),
                                            MyMathUtil.getCommaSeperatedNumber(Math.abs(n.getSuccessorCount() - n.getPredecessorCount())),
                                            MyMathUtil.getCommaSeperatedNumber(n.getTotalNodeRecurrentCount()),
                                            MyMathUtil.twoDecimalFormat(n.getBetweeness()),
                                            MyMathUtil.twoDecimalFormat(n.getCloseness()),
                                            MyMathUtil.twoDecimalFormat(n.getEignevector()),
                                            MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(n.getAverageReachTime())),
                                            MyMathUtil.getCommaSeperatedNumber(n.getDuration()),
                                            MyMathUtil.twoDecimalFormat(n.getAverageRecurrenceLength())});
                                }
                                pb.updateValue(100, 100);
                                pb.dispose();
                                frame.setAlwaysOnTop(false);
                            } else {
                                frame.setAlwaysOnTop(true);
                                MyProgressBar pb = new MyProgressBar(false);
                                MySequentialGraphVars.nodeOrderByComboBoxIdx = nodeOrderByComboBox.getSelectedIndex();
                                for (int i = model.getRowCount() - 1; i >= 0; i--) {
                                    model.removeRow(i);
                                }
                                pb.updateValue(40, 100);
                                ArrayList<MyNode> nodes = new ArrayList<>(MySequentialGraphVars.g.getVertices());
                                Collections.sort(nodes);
                                Map<MyNode, Integer> inContributionByNodeMap = getInContributionByNodeMap(nodes);
                                Map<MyNode, Integer> outContributionByNodeMap = getOutContributionByNodeMap(nodes);
                                pb.updateValue(70, 100);
                                int nodeCnt = 0;
                                for (MyNode n : nodes) {
                                    model.addRow(new String[]{
                                            String.valueOf(++nodeCnt),
                                            MySequentialGraphSysUtil.decodeNodeName(n.getName()),
                                            MyMathUtil.getCommaSeperatedNumber(n.getContribution()),
                                            MyMathUtil.getCommaSeperatedNumber(inContributionByNodeMap.get(n) == null ? 0 : inContributionByNodeMap.get(n)),
                                            MyMathUtil.getCommaSeperatedNumber(outContributionByNodeMap.get(n) == null ? 0 : outContributionByNodeMap.get(n)),
                                            MyMathUtil.getCommaSeperatedNumber(n.getUniqueContribution()),
                                            MyMathUtil.getCommaSeperatedNumber(n.getStartPositionNodeCount()),
                                            MyMathUtil.getCommaSeperatedNumber(n.getEndPositionNodeCount()),
                                            MyMathUtil.getCommaSeperatedNumber(n.getSuccessorCount()),
                                            MyMathUtil.getCommaSeperatedNumber(n.getPredecessorCount()),
                                            MyMathUtil.getCommaSeperatedNumber(Math.abs(n.getSuccessorCount() - n.getPredecessorCount())),
                                            MyMathUtil.getCommaSeperatedNumber(n.getTotalNodeRecurrentCount()),
                                            MyMathUtil.twoDecimalFormat(n.getBetweeness()),
                                            MyMathUtil.twoDecimalFormat(n.getCloseness()),
                                            MyMathUtil.twoDecimalFormat(n.getEignevector()),
                                            MyMathUtil.twoDecimalFormat(n.getAverageRecurrenceLength())});
                                }
                                pb.updateValue(100, 100);
                                pb.dispose();
                                frame.setAlwaysOnTop(false);
                            }
                        } catch (Exception ex) {ex.printStackTrace();}
                    }
                }).start();
            }
        });
        orderAndDistributionPanel.add(orderByLabel);
        orderAndDistributionPanel.add(nodeOrderByComboBox);
        orderAndDistributionPanel.add(distributionLabel);
        orderAndDistributionPanel.add(distributionComboBox);
        menuPanel.add(orderAndDistributionPanel);
        this.add(menuPanel, BorderLayout.NORTH);
        pb.updateValue(25, 100);
        this.setNodeStatistics();
        pb.updateValue(80, 100);
        this.table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                selectedNode.setText("    SELECTED NODE: " + table.getValueAt(table.getSelectedRow(), 1).toString());
            }
        });
        JScrollPane tableScrollPane = new JScrollPane(this.table);
        tableScrollPane.setPreferredSize(new Dimension(1000, 650));
        this.add(tableScrollPane, BorderLayout.CENTER);
        this.searchTxt.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        JPanel searchAndSavePanel = MyTableUtil.searchAndSaveDataPanelForJTable2(this, this.searchTxt, this.save, this.model, this.table);
        this.add(searchAndSavePanel, BorderLayout.SOUTH);
        this.save.setPreferredSize(new Dimension(70, 29));
        this.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.table.setRowSorter(MyTableUtil.setJTableRowSorterWithTextField(this.model, this.searchTxt));
        this.table.setSelectionBackground(Color.BLACK);
        this.table.setSelectionForeground(Color.WHITE);
        this.table.setFocusable(false);
        this.table.getTableHeader().setFont(MySequentialGraphVars.tahomaBoldFont12);
        Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
        this.table.setCursor(cursor);
        this.setPreferredSize(new Dimension(450, 700));
        numberOfNodeLabel.setText("NO. OF NODES: " + MyMathUtil.getCommaSeperatedNumber(this.table.getRowCount()));
        this.searchTxt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                numberOfNodeLabel.setText("NO. OF NODES: " + MyMathUtil.getCommaSeperatedNumber(table.getRowCount()));
            }
        });
        pb.updateValue(100,100);
        pb.dispose();
    }

    private void showDistribution(String title, int column, String xAxisTitle) {
        MyValueDistributionChartGenerator nodeStatTableDistributionChart = new MyValueDistributionChartGenerator( title, table, column, xAxisTitle);
    }

    private Map<MyNode, Integer> getInContributionByNodeMap(Collection<MyNode> nodes) {
        /**11. In Contribution Count.*/
        int maxInContribution = 0;
        Map<MyNode, Integer> inContributionByNodeMap = new HashMap<>();
        for (MyNode node : nodes) {
            for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                for (int itemsetIdx = 1; itemsetIdx < MySequentialGraphVars.seqs[s].length; itemsetIdx++) {
                    String nodeName = (MySequentialGraphVars.isTimeOn ? MySequentialGraphVars.seqs[s][itemsetIdx].split(":")[0] : MySequentialGraphVars.seqs[s][itemsetIdx]);
                    if (nodeName.equals(node.getName())) {
                        if (inContributionByNodeMap.containsKey(node)) {
                            inContributionByNodeMap.put(node, inContributionByNodeMap.get(node)+1);
                            if (inContributionByNodeMap.get(node) > maxInContribution) {
                                maxInContribution = inContributionByNodeMap.get(node);
                            }
                        } else {
                            inContributionByNodeMap.put(node, 1);
                            if (maxInContribution < inContributionByNodeMap.get(node)) {
                                maxInContribution = inContributionByNodeMap.get(node);
                            }
                        }
                    }
                }
            }
        }
        return inContributionByNodeMap;
    }

    private Map<MyNode, Integer> getOutContributionByNodeMap(Collection<MyNode> nodes) {
        /**12. Out Contribution Count.*/
        int maxOutContribution = 0;
        Map<MyNode, Integer> outContributionByNodeMap = new HashMap<>();
        for (MyNode node : nodes) {
            for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                for (int itemsetIdx = 0; itemsetIdx < MySequentialGraphVars.seqs[s].length; itemsetIdx++) {
                    if (itemsetIdx + 1 != MySequentialGraphVars.seqs[s].length) {
                        String nodeName = (MySequentialGraphVars.isTimeOn ? MySequentialGraphVars.seqs[s][itemsetIdx].split(":")[0] : MySequentialGraphVars.seqs[s][itemsetIdx]);
                        if (nodeName.equals(node.getName())) {
                            if (outContributionByNodeMap.containsKey(node)) {
                                outContributionByNodeMap.put(node, outContributionByNodeMap.get(node) + 1);
                                if (outContributionByNodeMap.get(node) > maxOutContribution) {
                                    maxOutContribution = outContributionByNodeMap.get(node);
                                }
                            } else {
                                outContributionByNodeMap.put(node, 1);
                                if (maxOutContribution < outContributionByNodeMap.get(node)) {
                                    maxOutContribution = outContributionByNodeMap.get(node);
                                }
                            }
                        }
                    }
                }
            }
        }
        return outContributionByNodeMap;
    }

    private void setNodeStatistics() {
        try {
            if (MySequentialGraphVars.isTimeOn) {
                this.model = new DefaultTableModel(this.data, this.timeConstrainedColumns);
                ArrayList<MyNode> nodes = new ArrayList<>(MySequentialGraphVars.g.getVertices());
                Map<MyNode, Integer> inContributionByNodeMap = this.getInContributionByNodeMap(nodes);
                Map<MyNode, Integer> outContributionByNodeMap = this.getOutContributionByNodeMap(nodes);
                Collections.sort(nodes);
                int nodeCnt = 0;
                for (MyNode n : nodes) {
                    model.addRow(new String[]{
                            String.valueOf(++nodeCnt),
                            MySequentialGraphSysUtil.decodeNodeName(n.getName()),
                            MyMathUtil.getCommaSeperatedNumber(n.getContribution()),
                            MyMathUtil.getCommaSeperatedNumber(inContributionByNodeMap.get(n) == null ? 0 : inContributionByNodeMap.get(n)),
                            MyMathUtil.getCommaSeperatedNumber(outContributionByNodeMap.get(n) == null ? 0 : outContributionByNodeMap.get(n)),
                            MyMathUtil.getCommaSeperatedNumber(n.getUniqueContribution()),
                            MyMathUtil.getCommaSeperatedNumber(n.getStartPositionNodeCount()),
                            MyMathUtil.getCommaSeperatedNumber(n.getEndPositionNodeCount()),
                            MyMathUtil.getCommaSeperatedNumber(n.getSuccessorCount()),
                            MyMathUtil.getCommaSeperatedNumber(n.getPredecessorCount()),
                            MyMathUtil.getCommaSeperatedNumber(Math.abs(n.getSuccessorCount() - n.getPredecessorCount())),
                            MyMathUtil.getCommaSeperatedNumber(n.getTotalNodeRecurrentCount()),
                            MyMathUtil.twoDecimalFormat(n.getBetweeness()),
                            MyMathUtil.twoDecimalFormat(n.getCloseness()),
                            MyMathUtil.twoDecimalFormat(n.getEignevector()),
                            MyMathUtil.getCommaSeperatedNumber(n.getTotalReachTime()),
                            MyMathUtil.getCommaSeperatedNumber(n.getDuration()),
                            MyMathUtil.twoDecimalFormat(n.getAverageRecurrenceLength())});
                }
                this.table = new JTable(model);
                this.table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                this.table.getColumnModel().getColumn(0).setPreferredWidth(60);
                this.table.getColumnModel().getColumn(1).setPreferredWidth(100);
                this.table.getColumnModel().getColumn(2).setPreferredWidth(100);
                this.table.getColumnModel().getColumn(3).setPreferredWidth(60);
                this.table.getColumnModel().getColumn(4).setPreferredWidth(60);
                this.table.getColumnModel().getColumn(5).setPreferredWidth(60);
                this.table.getColumnModel().getColumn(6).setPreferredWidth(60);
                this.table.getColumnModel().getColumn(7).setPreferredWidth(60);
                this.table.getColumnModel().getColumn(8).setPreferredWidth(60);
                this.table.getColumnModel().getColumn(9).setPreferredWidth(60);
                this.table.getColumnModel().getColumn(10).setPreferredWidth(60);
                this.table.getColumnModel().getColumn(11).setPreferredWidth(60);
                this.table.getColumnModel().getColumn(12).setPreferredWidth(60);
                this.table.getColumnModel().getColumn(13).setPreferredWidth(60);
                this.table.getColumnModel().getColumn(14).setPreferredWidth(60);
                this.table.getColumnModel().getColumn(15).setPreferredWidth(60);
                this.table.getColumnModel().getColumn(16).setPreferredWidth(60);
                this.table.getColumnModel().getColumn(17).setPreferredWidth(60);

                this.table.setBackground(Color.WHITE);
                this.table.setFont(MySequentialGraphVars.f_pln_12);
                this.table.setRowHeight(24);
            } else {
                this.model = new DefaultTableModel(this.data, this.columns);
                ArrayList<MyNode> nodes = new ArrayList<>(MySequentialGraphVars.g.getVertices());
                Map<MyNode, Integer> inContributionByNodeMap = this.getInContributionByNodeMap(nodes);
                Map<MyNode, Integer> outContributionByNodeMap = this.getOutContributionByNodeMap(nodes);
                Collections.sort(nodes);
                int nodeCnt = 0;
                for (MyNode n : nodes) {
                    model.addRow(new String[]{
                            String.valueOf(++nodeCnt),
                            MySequentialGraphSysUtil.decodeNodeName(n.getName()),
                            MyMathUtil.getCommaSeperatedNumber(n.getContribution()),
                            MyMathUtil.getCommaSeperatedNumber(inContributionByNodeMap.get(n) == null ? 0 : inContributionByNodeMap.get(n)),
                            MyMathUtil.getCommaSeperatedNumber(outContributionByNodeMap.get(n) == null ? 0 : outContributionByNodeMap.get(n)),
                            MyMathUtil.getCommaSeperatedNumber(n.getUniqueContribution()),
                            MyMathUtil.getCommaSeperatedNumber(n.getStartPositionNodeCount()),
                            MyMathUtil.getCommaSeperatedNumber(n.getEndPositionNodeCount()),
                            MyMathUtil.getCommaSeperatedNumber(n.getSuccessorCount()),
                            MyMathUtil.getCommaSeperatedNumber(n.getPredecessorCount()),
                            MyMathUtil.getCommaSeperatedNumber(Math.abs(n.getSuccessorCount() - n.getPredecessorCount())),
                            MyMathUtil.getCommaSeperatedNumber(n.getTotalNodeRecurrentCount()),
                            MyMathUtil.twoDecimalFormat(n.getBetweeness()),
                            MyMathUtil.twoDecimalFormat(n.getCloseness()),
                            MyMathUtil.twoDecimalFormat(n.getEignevector()),
                            MyMathUtil.twoDecimalFormat(n.getAverageRecurrenceLength())

                    });
                }
                this.table = new JTable(model);
                this.table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                this.table.getColumnModel().getColumn(0).setPreferredWidth(60);
                this.table.getColumnModel().getColumn(1).setPreferredWidth(150);
                this.table.getColumnModel().getColumn(2).setPreferredWidth(60);
                this.table.getColumnModel().getColumn(3).setPreferredWidth(60);
                this.table.getColumnModel().getColumn(4).setPreferredWidth(60);
                this.table.getColumnModel().getColumn(5).setPreferredWidth(60);
                this.table.getColumnModel().getColumn(6).setPreferredWidth(60);
                this.table.getColumnModel().getColumn(7).setPreferredWidth(60);
                this.table.getColumnModel().getColumn(8).setPreferredWidth(60);
                this.table.getColumnModel().getColumn(9).setPreferredWidth(60);
                this.table.getColumnModel().getColumn(10).setPreferredWidth(60);
                this.table.getColumnModel().getColumn(11).setPreferredWidth(60);
                this.table.getColumnModel().getColumn(11).setPreferredWidth(60);
                this.table.getColumnModel().getColumn(12).setPreferredWidth(60);
                this.table.getColumnModel().getColumn(13).setPreferredWidth(60);
                this.table.getColumnModel().getColumn(14).setPreferredWidth(60);

                this.table.setBackground(Color.WHITE);
                this.table.setFont(MySequentialGraphVars.f_pln_12);
                this.table.setRowHeight(24);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override public void actionPerformed(ActionEvent e) {}

}
