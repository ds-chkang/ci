package medousa.direct.graph;

import medousa.message.MyMessageUtil;
import medousa.direct.utils.MyDirectGraphMathUtil;
import medousa.direct.utils.MyDirectGraphSysUtil;
import medousa.direct.utils.MyDirectGraphVars;
import medousa.table.MyTableUtil;
import medousa.MyProgressBar;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;

public class MyDirectGraphNodeStatistic
extends JPanel
implements ActionListener{

    JPanel nodeStatPanel = new JPanel();
    private JTable table;
    private DefaultTableModel model;
    private JPanel nodeStatDataSavePanel;
    private JButton nodeStatSaveBtn;
    private JTextField nodeSearchTxt;
    private JComboBox nodeOrderByComboBox = new JComboBox();
    private JFrame f;
    private boolean tableCreated;

    public MyDirectGraphNodeStatistic() {
        this.decorate();
        this.setNodes();
        this.f = new JFrame("NODE STATISTICS");
        this.f.setLayout(new BorderLayout(3,3));
        this.f.add(this, BorderLayout.CENTER);
        this.f.pack();
        this.f.setLocation(5,5);
        this.f.setAlwaysOnTop(true);
        this.f.setVisible(true);
        this.f.setAlwaysOnTop(false);
    }

    private void decorate() {
        this.setPreferredSize(new Dimension(800, 550));
        this.setLayout(new BorderLayout(3, 3));
        this.nodeStatPanel.setLayout(new BorderLayout(3,3));
        this.model = new DefaultTableModel(new String[][]{}, new String[]{
            "NO.",
            "NODE",
            "SUCCESSORS",
            "PREDECESSORS",
            "OUT CONTRIBUTION",
            "IN CONTRIBUTION",
            "CONTRIBUTION",
            "AVG. S. IN-DISTANCE",
            "REACHED IN-NODES",
            "AVG. S. OUT-DISTANCE",
            "REACHED OUT-NODES",
            "BETWEENESS",
            "EIGENVECTOR"});

        this.table = new JTable(this.model);
        for (int i=0; i < this.table.getColumnCount(); i++) {
            if (i == 0) {
                this.table.getTableHeader().getColumnModel().getColumn(0).setPreferredWidth(50);
                this.table.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(60);
            } else {
                this.table.getTableHeader().getColumnModel().getColumn(0).setPreferredWidth(100);
            }
        }
        this.table.getTableHeader().setFont(MyDirectGraphVars.tahomaBoldFont12);
        this.table.setRowHeight(21);
        this.table.setFont(MyDirectGraphVars.f_pln_12);
        this.table.setBackground(Color.WHITE);
        this.table.getTableHeader().setBackground(new Color(0,0,0,0));
        this.table.getTableHeader().setOpaque(false);
        this.nodeStatSaveBtn = new JButton("SAVE");
        this.nodeStatSaveBtn.setFont(MyDirectGraphVars.tahomaPlainFont12);
        this.nodeSearchTxt = new JTextField();
        this.nodeSearchTxt.setFont(MyDirectGraphVars.tahomaPlainFont12);
        this.nodeSearchTxt.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        this.nodeStatDataSavePanel = MyTableUtil.searchAndSaveDataPanelForJTable2(this, this.nodeSearchTxt, this.nodeStatSaveBtn, this.model, this.table);
        this.nodeStatDataSavePanel.setPreferredSize(new Dimension(100, 26));

        JPanel nodeOrderByPanel = new JPanel();
        nodeOrderByPanel.setLayout(new BorderLayout(3, 3));
        nodeOrderByPanel.setPreferredSize(new Dimension(200, 28));
        JLabel nodeOrderByLabel = new JLabel(" ORDER BY: ");
        nodeOrderByLabel.setFont(MyDirectGraphVars.tahomaPlainFont12);
        nodeOrderByLabel.setPreferredSize(new Dimension(70, 25));
        nodeOrderByPanel.add(nodeOrderByLabel, BorderLayout.WEST);
        nodeOrderByPanel.add(this.nodeOrderByComboBox, BorderLayout.CENTER);
        this.nodeOrderByComboBox.setFont(MyDirectGraphVars.tahomaPlainFont12);
        this.nodeOrderByComboBox.addItem("CONTRIBUTION");
        this.nodeOrderByComboBox.addItem("SUCCESSORS");
        this.nodeOrderByComboBox.addItem("PREDECESSORS");
        this.nodeOrderByComboBox.addItem("OUT-CONTRIBUTION");
        this.nodeOrderByComboBox.addItem("IN-CONTRIBUTION");
        this.nodeOrderByComboBox.addItem("AVG. SHORTEST IN-DISTANCE");
        this.nodeOrderByComboBox.addItem("REACHED IN-NODES");
        this.nodeOrderByComboBox.addItem("AVG. SHORTEST OUT-DISTANCE");
        this.nodeOrderByComboBox.addItem("REACHED OUT-NODES");
        this.nodeOrderByComboBox.addItem("BETWEENESS");
        this.nodeOrderByComboBox.addItem("EIGENVECTOR");
        this.nodeOrderByComboBox.setFocusable(false);
        this.nodeStatPanel.add(nodeOrderByPanel, BorderLayout.NORTH);
        this.nodeStatPanel.add(new JScrollPane(this.table), BorderLayout.CENTER);
        this.nodeStatPanel.add(this.nodeStatDataSavePanel, BorderLayout.SOUTH);
        this.nodeOrderByComboBox.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        f.setAlwaysOnTop(true);
                        setNodes();
                        f.setAlwaysOnTop(false);
                    }
                }).start();
            }
        });
        this.add(this.nodeStatPanel, BorderLayout.CENTER);
    }

    private void setNodes() {
        if (!this.tableCreated) {
            MyProgressBar pb = new MyProgressBar(false);
            LinkedHashMap<String, Float> sortedNodeMap = new LinkedHashMap<>();
            Collection<MyDirectNode> nodes = new ArrayList<>(MyDirectGraphVars.directGraph.getVertices());
            for (MyDirectNode n : nodes) {
                if (nodeOrderByComboBox.getSelectedIndex() == 0) {
                    sortedNodeMap.put(n.getName(), (float)n.getContribution());
                } else if (nodeOrderByComboBox.getSelectedIndex()  == 1) {
                    sortedNodeMap.put(n.getName(), (float) MyDirectGraphVars.directGraph.getSuccessorCount(n));
                } else if (nodeOrderByComboBox.getSelectedIndex() == 2) {
                    sortedNodeMap.put(n.getName(), (float) MyDirectGraphVars.directGraph.getPredecessorCount(n));
                } else if (nodeOrderByComboBox.getSelectedIndex()  == 3) {
                    sortedNodeMap.put(n.getName(), (float) n.getOutContribution());
                } else if (nodeOrderByComboBox.getSelectedIndex()  == 4) {
                    sortedNodeMap.put(n.getName(), (float) n.getInContribution());
                } else if (nodeOrderByComboBox.getSelectedIndex()  == 5) {
                    sortedNodeMap.put(n.getName(), n.getAverageShortestInDistance());
                } else if (nodeOrderByComboBox.getSelectedIndex()  == 6) {
                    sortedNodeMap.put(n.getName(), (float) n.getReachedInNodeCount());
                } else if (nodeOrderByComboBox.getSelectedIndex()  == 7) {
                    sortedNodeMap.put(n.getName(), (float) n.getAverageShortestOutDistance());
                } else if (nodeOrderByComboBox.getSelectedIndex()  == 8) {
                    sortedNodeMap.put(n.getName(), (float) n.getReachedOutNodeCount());
                } else if (nodeOrderByComboBox.getSelectedIndex()  == 9) {
                    sortedNodeMap.put(n.getName(), (float) n.getBetweeness());
                } else if (nodeOrderByComboBox.getSelectedIndex()  == 10) {
                    sortedNodeMap.put(n.getName(), (float) n.getEignevector());
                }
            }
            sortedNodeMap = MyDirectGraphSysUtil.sortMapByFloatValue(sortedNodeMap);

            for (int i = this.table.getRowCount()-1; i >= 0; i--) {
                ((DefaultTableModel) this.table.getModel()).removeRow(i);
            }

            int recCnt = 0;
            for (String n : sortedNodeMap.keySet()) {
                this.model.addRow(new String[]{
                    String.valueOf(++recCnt),
                    n,
                    MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.directGraph.getSuccessorCount(MyDirectGraphVars.directGraph.vRefs.get(n))),
                    MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.directGraph.getPredecessorCount(MyDirectGraphVars.directGraph.vRefs.get(n))),
                    MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.directGraph.getOutContributionByNode((MyDirectNode) MyDirectGraphVars.directGraph.vRefs.get(n))),
                    MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.directGraph.getInContributionByNode((MyDirectNode) MyDirectGraphVars.directGraph.vRefs.get(n))),
                    MyDirectGraphMathUtil.getCommaSeperatedNumber(((MyDirectNode) MyDirectGraphVars.directGraph.vRefs.get(n)).getContribution()),
                    MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(((MyDirectNode) MyDirectGraphVars.directGraph.vRefs.get(n)).getAverageShortestInDistance())),
                    MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.getCommaSeperatedNumber(((MyDirectNode) MyDirectGraphVars.directGraph.vRefs.get(n)).getReachedInNodeCount())),
                    MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(((MyDirectNode) MyDirectGraphVars.directGraph.vRefs.get(n)).getAverageShortestInDistance())),
                    MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.getCommaSeperatedNumber(((MyDirectNode) MyDirectGraphVars.directGraph.vRefs.get(n)).getReachedOutNodeCount())),
                    MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(((MyDirectNode) MyDirectGraphVars.directGraph.vRefs.get(n)).getBetweeness())),
                    MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(((MyDirectNode) MyDirectGraphVars.directGraph.vRefs.get(n)).getEignevector()))
                });
                pb.updateValue(recCnt, nodes.size());
            }
            pb.updateValue(100, 100);
            pb.dispose();
            this.tableCreated = true;
        } else {
            MyProgressBar pb = new MyProgressBar(false);
            LinkedHashMap<String, Float> sortedNodeMap = new LinkedHashMap<>();
            Collection<MyDirectNode> nodes = new ArrayList<>(MyDirectGraphVars.directGraph.getVertices());
            for (MyDirectNode n : nodes) {
                if (nodeOrderByComboBox.getSelectedIndex() == 0) {
                    sortedNodeMap.put(n.getName(), (float)n.getContribution());
                } else if (nodeOrderByComboBox.getSelectedIndex()  == 1) {
                    sortedNodeMap.put(n.getName(), (float) MyDirectGraphVars.directGraph.getSuccessorCount(n));
                } else if (nodeOrderByComboBox.getSelectedIndex() == 2) {
                    sortedNodeMap.put(n.getName(), (float) MyDirectGraphVars.directGraph.getPredecessorCount(n));
                } else if (nodeOrderByComboBox.getSelectedIndex()  == 3) {
                    sortedNodeMap.put(n.getName(), (float) n.getOutContribution());
                } else if (nodeOrderByComboBox.getSelectedIndex()  == 4) {
                    sortedNodeMap.put(n.getName(), (float) n.getInContribution());
                } else if (nodeOrderByComboBox.getSelectedIndex()  == 5) {
                    sortedNodeMap.put(n.getName(), n.getAverageShortestInDistance());
                } else if (nodeOrderByComboBox.getSelectedIndex()  == 6) {
                    sortedNodeMap.put(n.getName(), (float) n.getReachedInNodeCount());
                } else if (nodeOrderByComboBox.getSelectedIndex()  == 7) {
                    sortedNodeMap.put(n.getName(), (float) n.getAverageShortestOutDistance());
                } else if (nodeOrderByComboBox.getSelectedIndex()  == 8) {
                    sortedNodeMap.put(n.getName(), (float) n.getReachedOutNodeCount());
                } else if (nodeOrderByComboBox.getSelectedIndex()  == 9) {
                    sortedNodeMap.put(n.getName(), (float) n.getBetweeness());
                } else if (nodeOrderByComboBox.getSelectedIndex()  == 10) {
                    sortedNodeMap.put(n.getName(), (float) n.getEignevector());
                }
            }
            sortedNodeMap = MyDirectGraphSysUtil.sortMapByFloatValue(sortedNodeMap);

            for (int i = this.table.getRowCount()-1; i >= 0; i--) {
                ((DefaultTableModel) this.table.getModel()).removeRow(i);
            }

            int recCnt = 0;
            for (String n : sortedNodeMap.keySet()) {
                this.model.addRow(new String[]{
                        String.valueOf(++recCnt),
                        n,
                        MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.directGraph.getSuccessorCount(MyDirectGraphVars.directGraph.vRefs.get(n))),
                        MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.directGraph.getPredecessorCount(MyDirectGraphVars.directGraph.vRefs.get(n))),
                        MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.directGraph.getOutContributionByNode((MyDirectNode) MyDirectGraphVars.directGraph.vRefs.get(n))),
                        MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.directGraph.getInContributionByNode((MyDirectNode) MyDirectGraphVars.directGraph.vRefs.get(n))),
                        MyDirectGraphMathUtil.getCommaSeperatedNumber(((MyDirectNode) MyDirectGraphVars.directGraph.vRefs.get(n)).getContribution()),
                        MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(((MyDirectNode) MyDirectGraphVars.directGraph.vRefs.get(n)).getAverageShortestInDistance())),
                        MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(((MyDirectNode) MyDirectGraphVars.directGraph.vRefs.get(n)).getReachedInNodeCount())),
                        MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(((MyDirectNode) MyDirectGraphVars.directGraph.vRefs.get(n)).getAverageShortestInDistance())),
                        MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(((MyDirectNode) MyDirectGraphVars.directGraph.vRefs.get(n)).getReachedOutNodeCount())),
                        MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(((MyDirectNode) MyDirectGraphVars.directGraph.vRefs.get(n)).getBetweeness())),
                        MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(((MyDirectNode) MyDirectGraphVars.directGraph.vRefs.get(n)).getEignevector()))
                });
                pb.updateValue(recCnt, nodes.size());
            }
            pb.updateValue(100, 100);
            pb.dispose();
        }
    }

    @Override public void actionPerformed(ActionEvent e) {
        new Thread(new Runnable() {
            @Override public void run() {
                JFileChooser fc = new JFileChooser();
                MyProgressBar pb = null;
                BufferedWriter bw = null;
                if (e.getSource() == nodeStatSaveBtn) {
                    try {
                        fc.setFont(MyDirectGraphVars.f_pln_12); //For hangul file names.
                        fc.setMultiSelectionEnabled(false);
                        fc.setPreferredSize(new Dimension(600, 460));
                        fc.showSaveDialog(MyDirectGraphVars.app);
                        pb = new MyProgressBar(false);
                        int pbCnt = 0;
                        bw = new BufferedWriter(new FileWriter(fc.getSelectedFile()));
                        bw.write("NO.,NODE,CONTRIBUTION,SUCCESSOR,PREDECESSOR,OUT-CONTRIBUTION,IN-CONTRIBUTION,AVG.-SHORTEST-IN-DISTANCE,AVG.-SHORTEST-OUT-DISTANCE,BETWEENESS,EIGENVECTOR" + "\n");
                        for (int i = 0; i < table.getRowCount(); i++) {
                            bw.write(
                        table.getValueAt(i, 0).toString() + "," +
                            table.getValueAt(i, 1).toString() + "," +
                            table.getValueAt(i, 2).toString() + "," +
                            table.getValueAt(i, 3).toString() + "," +
                            table.getValueAt(i, 4).toString() + "," +
                            table.getValueAt(i, 5).toString() + "," +
                                table.getValueAt(i, 6).toString() + "," +
                                table.getValueAt(i, 7).toString() + "," +
                                table.getValueAt(i, 8).toString() + "," +
                                table.getValueAt(i, 9).toString() + "," +
                                table.getValueAt(i, 10).toString() + "," +
                                table.getValueAt(i, 11).toString() + "," +
                            table.getValueAt(i, 12).toString() + "\n");
                            pb.updateValue(++pbCnt, table.getRowCount());
                        }
                        bw.close();
                        pb.updateValue(100, 100);
                        pb.dispose();
                        MyMessageUtil.showInfoMsg("Node statistics have been successfully saved to storage.");
                    } catch (Exception ex) {
                        try {
                            if (pb != null) {
                                pb.updateValue(100, 100);
                                pb.dispose();
                            }
                            MyMessageUtil.showErrorMsg("An error has occurred while saving node statistics to storage!");
                            if (bw != null) bw.close();
                            if (fc.getSelectedFile().exists()) {fc.getSelectedFile().delete();}
                        } catch (Exception e) {e.printStackTrace();}
                    }
                }
            }
        }).start();
    }
}
