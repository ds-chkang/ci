package datamining.graph;

import datamining.utils.message.MyMessageUtil;
import datamining.utils.MyMathUtil;
import datamining.utils.system.MyVars;
import datamining.utils.table.MyTableUtil;
import datamining.main.MyProgressBar;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;

public class MyDirectGraphStatisticsPanel
extends JPanel
implements ActionListener {

    JPanel nodeStatPanel = new JPanel();
    JPanel edgeStatPanel = new JPanel();
    private JTable nodeContributionStatTable;
    private DefaultTableModel nodeTableModel;
    private JPanel nodeStatDataSavePanel;
    private JTable edgeContributionStatTable;
    private DefaultTableModel edgeTableModel;
    private JPanel edgeStatDataSavePanel;
    private JButton nodeStatSaveBtn;
    private JButton edgeStatSaveBtn;
    private JTextField nodeSearchTxt;
    private JTextField edgeSearchTxt;
    private JComboBox nodeOrderBy = new JComboBox();
    private JComboBox edgeOrderBy = new JComboBox();
    private JButton nodeEnlargeBtn = new JButton("+");
    private JButton edgeEnlargeBtn = new JButton("+");

    public MyDirectGraphStatisticsPanel() {}

    protected void decorate() {
        this.setBorder(BorderFactory.createEtchedBorder());
        this.setPreferredSize(new Dimension(260, 300));
        this.setLayout(new BorderLayout(3, 3));
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(this.setNodeContributionStatistics());
        splitPane.setBottomComponent(this.setEdgeContributionStatistics());
        splitPane.setDividerSize(5);
        splitPane.setDividerLocation(0.5);
        splitPane.setResizeWeight(0.5);

        this.nodeOrderBy.addActionListener(new ActionListener() {@Override
            public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {@Override
                    public void run() {
                        setNodeContributionStatistics();
                        splitPane.revalidate();
                        splitPane.repaint();
                    }
                }).start();
            }
        });

        this.edgeOrderBy.addActionListener(new ActionListener() {@Override
            public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {@Override
                    public void run() {
                        setEdgeContributionStatistics();
                        splitPane.revalidate();
                        splitPane.repaint();
                    }
                }).start();
            }
        });

        this.nodeEnlargeBtn.addActionListener(new ActionListener() {@Override
            public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {@Override
                    public void run() {
                        MyDirectGraphNodeStatistic directGraphNodeStatistic = new MyDirectGraphNodeStatistic();
                    }
                }).start();
            }
        });

        this.edgeEnlargeBtn.addActionListener(new ActionListener() { @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {@Override
                    public void run() {
                        MyDirectGraphEdgeStatistic directGraphEdgeStatistic = new MyDirectGraphEdgeStatistic();
                    }
                }).start();
            }
        });

        this.add(splitPane, BorderLayout.CENTER);
    }

    private JPanel setNodeContributionStatistics() {
        if (this.nodeTableModel == null) {
            this.nodeStatPanel.setLayout(new BorderLayout(3,3));
            this.nodeTableModel = new DefaultTableModel(new String[][]{}, new String[]{"NO.", "NODE", "SUCC.", "SUCC. R.", "PRED.", "PRED. R.", "OUT-CONT.", "IN-CONT.", "CONT.", "CONT. R."});
            this.nodeContributionStatTable = new JTable(this.nodeTableModel);
            this.nodeContributionStatTable.getTableHeader().setFont(MyVars.tahomaBoldFont12);
            this.nodeContributionStatTable.setRowHeight(21);
            this.nodeContributionStatTable.setFont(MyVars.f_pln_12);
            this.nodeContributionStatTable.setBackground(Color.WHITE);
            this.nodeStatSaveBtn = new JButton("SAVE");
            this.nodeStatSaveBtn.setFont(MyVars.tahomaPlainFont11);
            this.nodeSearchTxt = new JTextField();
            this.nodeSearchTxt.setBorder(BorderFactory.createLoweredSoftBevelBorder());
            this.nodeStatDataSavePanel = MyTableUtil.searchAndSaveDataPanelForJTable2(this, this.nodeSearchTxt, this.nodeStatSaveBtn, this.nodeTableModel, this.nodeContributionStatTable);
            this.nodeStatDataSavePanel.setPreferredSize(new Dimension(100, 26));
            this.nodeStatPanel.setBorder(BorderFactory.createTitledBorder("NODE STATISTICS("+ MyMathUtil.getCommaSeperatedNumber(MyVars.directMarkovChain.getVertexCount())+")"));

            JPanel nodeOrderByPanel = new JPanel();
            nodeOrderByPanel.setLayout(new BorderLayout(3, 3));
            nodeOrderByPanel.setPreferredSize(new Dimension(200, 28));
            JLabel nodeOrderByLabel = new JLabel("ORDER BY: ");
            nodeOrderByLabel.setFont(MyVars.f_pln_12);
            nodeOrderByLabel.setPreferredSize(new Dimension(70, 25));
            this.nodeEnlargeBtn.setFont(MyVars.f_pln_11);
            nodeOrderByPanel.add(nodeOrderByLabel, BorderLayout.WEST);
            nodeOrderByPanel.add(this.nodeOrderBy, BorderLayout.CENTER);
            nodeOrderByPanel.add(this.nodeEnlargeBtn, BorderLayout.EAST);
            this.nodeOrderBy.setFont(MyVars.tahomaPlainFont12);
            this.nodeOrderBy.addItem("CONTRIBUTION");
            this.nodeOrderBy.addItem("SUCCESSOR");
            this.nodeOrderBy.addItem("PREDECESSOR");
            this.nodeOrderBy.addItem("OUT-CONTRIBUTION");
            this.nodeOrderBy.addItem("IN-CONTRIBUTION");
            this.nodeOrderBy.setFocusable(false);
            this.nodeStatPanel.add(nodeOrderByPanel, BorderLayout.NORTH);
            this.nodeStatPanel.add(new JScrollPane(this.nodeContributionStatTable), BorderLayout.CENTER);
            this.nodeStatPanel.add(this.nodeStatDataSavePanel, BorderLayout.SOUTH);

            ArrayList<MyDirectNode> nodes = new ArrayList<>(MyVars.directMarkovChain.getVertices());
            Collections.sort(nodes);
            int recCnt = 0;
            for (int i = nodes.size() - 1; i >= 0; i--) {
                this.nodeTableModel.addRow(new String[]{
                    String.valueOf(++recCnt),
                    nodes.get(i).getLabel(),
                    MyMathUtil.getCommaSeperatedNumber(MyVars.directMarkovChain.getSuccessorCount(nodes.get(i))),
                    MyMathUtil.threeDecimalFormat((double)MyVars.directMarkovChain.getSuccessorCount(nodes.get(i))/MyVars.directMarkovChain.getVertexCount()),
                    MyMathUtil.getCommaSeperatedNumber(MyVars.directMarkovChain.getPredecessorCount(nodes.get(i))),
                    MyMathUtil.threeDecimalFormat((double)MyVars.directMarkovChain.getPredecessorCount(nodes.get(i))/MyVars.directMarkovChain.getVertexCount()),
                    MyMathUtil.getCommaSeperatedNumber(MyVars.directMarkovChain.getOutContributionByNode(nodes.get(i))),
                    MyMathUtil.getCommaSeperatedNumber(MyVars.directMarkovChain.getInContributionByNode(nodes.get(i))),
                    MyMathUtil.getCommaSeperatedNumber(nodes.get(i).getContribution()),
                    MyMathUtil.threeDecimalFormat((double) nodes.get(i).getContribution() / MyVars.directMarkovChain.getTotalNodeContribution())
                });
            }
        } else {
            MyTableUtil.removeRecordsFromTable(this.nodeTableModel);
            ArrayList<MyDirectNode> nodes = new ArrayList<>(MyVars.directMarkovChain.getVertices());
            Collections.sort(nodes);
            int recCnt = 0;
            MyProgressBar pb = new MyProgressBar(false);
            for (int i = nodes.size() - 1; i >= 0; i--) {
                this.nodeTableModel.addRow(new String[]{
                    String.valueOf(++recCnt),
                    nodes.get(i).getLabel(),
                    MyMathUtil.getCommaSeperatedNumber(MyVars.directMarkovChain.getSuccessorCount(nodes.get(i))),
                    MyMathUtil.getCommaSeperatedNumber(MyVars.directMarkovChain.getPredecessorCount(nodes.get(i))),
                    MyMathUtil.getCommaSeperatedNumber(MyVars.directMarkovChain.getOutContributionByNode(nodes.get(i))),
                    MyMathUtil.getCommaSeperatedNumber(MyVars.directMarkovChain.getInContributionByNode(nodes.get(i))),
                    MyMathUtil.getCommaSeperatedNumber(nodes.get(i).getContribution()),
                    MyMathUtil.threeDecimalFormat((double) nodes.get(i).getContribution()/MyVars.directMarkovChain.getTotalNodeContribution())
                });
                pb.updateValue(recCnt, nodes.size());
            }
            pb.dispose();
        }

        return this.nodeStatPanel;
    }

    private JPanel setEdgeContributionStatistics() {
        if (this.edgeTableModel == null) {
            this.edgeStatPanel.setLayout(new BorderLayout(3, 3));
            this.edgeTableModel = new DefaultTableModel(new String[][]{}, new String[]{"NO.", "SOURCE", "DEST", "CONT.", "SUPPORT", "CONFIDENCE", "LIFT"});
            this.edgeContributionStatTable = new JTable(this.edgeTableModel);

            this.edgeContributionStatTable.getTableHeader().setFont(MyVars.tahomaBoldFont12);
            this.edgeContributionStatTable.setRowHeight(21);
            this.edgeContributionStatTable.setFont(MyVars.f_pln_12);
            this.edgeContributionStatTable.setBackground(Color.WHITE);
            this.edgeStatSaveBtn = new JButton("SAVE");
            this.edgeStatSaveBtn.setFont(MyVars.tahomaPlainFont11);
            this.edgeSearchTxt = new JTextField();
            this.edgeSearchTxt.setBorder(BorderFactory.createLoweredSoftBevelBorder());
            this.edgeStatDataSavePanel = MyTableUtil.searchAndSaveDataPanelForJTable2(this, edgeSearchTxt, this.edgeStatSaveBtn, this.edgeTableModel, this.edgeContributionStatTable);
            this.edgeStatDataSavePanel.setPreferredSize(new Dimension(100, 26));
            this.edgeStatPanel.setBorder(BorderFactory.createTitledBorder("EDGE STATISTICS("+MyMathUtil.getCommaSeperatedNumber(MyVars.directMarkovChain.getTotalEdgeContribution())+")"));

            JPanel edgeOrderByPanel = new JPanel();
            edgeOrderByPanel.setLayout(new BorderLayout(3, 3));
            edgeOrderByPanel.setPreferredSize(new Dimension(200, 28));
            JLabel edgeOrderByLabel = new JLabel("ORDER BY: ");
            edgeOrderByLabel.setFont(MyVars.tahomaPlainFont12);
            edgeOrderByLabel.setPreferredSize(new Dimension(70, 25));
            edgeOrderByPanel.add(edgeOrderByLabel, BorderLayout.WEST);
            edgeOrderByPanel.add(this.edgeOrderBy, BorderLayout.CENTER);
            edgeOrderByPanel.add(this.edgeEnlargeBtn, BorderLayout.EAST);
            this.edgeOrderBy.setFont(MyVars.tahomaPlainFont12);
            this.edgeOrderBy.addItem("CONTRIBUTION");
            this.edgeOrderBy.addItem("SUPPORT");
            this.edgeOrderBy.addItem("CONFIDENCE");
            this.edgeOrderBy.addItem("LIFT");
            this.edgeOrderBy.setFocusable(false);
            this.edgeEnlargeBtn.setFont(MyVars.tahomaPlainFont11);
            this.edgeStatPanel.add(edgeOrderByPanel, BorderLayout.NORTH);
            this.edgeStatPanel.add(new JScrollPane(this.edgeContributionStatTable), BorderLayout.CENTER);
            this.edgeStatPanel.add(this.edgeStatDataSavePanel, BorderLayout.SOUTH);

            int recCnt = 0;
            ArrayList<MyDirectEdge> edges = new ArrayList<>(MyVars.directMarkovChain.getEdges());
            Collections.sort(edges);
            for (int i = edges.size() - 1; i >= 0; i--) {
                this.edgeTableModel.addRow(new String[]{
                    String.valueOf(++recCnt),
                    edges.get(i).getSource().getLabel(),
                    edges.get(i).getDest().getLabel(),
                    MyMathUtil.getCommaSeperatedNumber(edges.get(i).getContribution()),
                    MyMathUtil.threeDecimalFormat(edges.get(i).getSupport()),
                    MyMathUtil.threeDecimalFormat(edges.get(i).getConfidence()),
                    MyMathUtil.threeDecimalFormat(edges.get(i).getLift())
                });
            }
        } else {
            MyTableUtil.removeRecordsFromTable(this.edgeTableModel);
            int recCnt = 0;
            ArrayList<MyDirectEdge> edges = new ArrayList<>(MyVars.directMarkovChain.getEdges());
            Collections.sort(edges);
            MyProgressBar pb = new MyProgressBar(false);
            for (int i = edges.size() - 1; i >= 0; i--) {
                this.edgeTableModel.addRow(new String[]{
                    String.valueOf(++recCnt),
                    edges.get(i).getSource().getLabel(),
                    edges.get(i).getDest().getLabel(),
                    MyMathUtil.getCommaSeperatedNumber(edges.get(i).getContribution()),
                    MyMathUtil.threeDecimalFormat(edges.get(i).getSupport()),
                    MyMathUtil.threeDecimalFormat(edges.get(i).getConfidence()),
                    MyMathUtil.threeDecimalFormat(edges.get(i).getLift())
                });
                pb.updateValue(recCnt, edges.size());
            }
            pb.dispose();
        }
        return this.edgeStatPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        new Thread(new Runnable() {@Override
            public void run() {
                JFileChooser fc = new JFileChooser();
                BufferedWriter bw = null;
                if (e.getSource() == nodeStatSaveBtn) {
                    try {
                        fc.setFont(MyVars.f_pln_12); //For hangul file names.
                        fc.setMultiSelectionEnabled(false);
                        fc.setPreferredSize(new Dimension(600, 460));
                        fc.showSaveDialog(MyVars.main);
                        MyProgressBar pb = new MyProgressBar(false);
                        int pbCnt = 0;
                        bw = new BufferedWriter(new FileWriter(fc.getSelectedFile()));
                        bw.write("NO.,NODE,SUCCESSOR,SUCCESSOR RATIO,PREDECESSOR,PREDECESSOR RATIO,OUT-CONTRIBUTION,IN-CONTRIBUTION,CONTRIBUTION RATIO\n");
                        for (int i = 0; i < nodeContributionStatTable.getRowCount(); i++) {
                            bw.write(nodeContributionStatTable.getValueAt(i, 0).toString() + "," +
                                nodeContributionStatTable.getValueAt(i, 1).toString() + "," +
                                nodeContributionStatTable.getValueAt(i, 2).toString() + "," +
                                nodeContributionStatTable.getValueAt(i, 3).toString() + "\n");
                            pb.updateValue(++pbCnt, nodeContributionStatTable.getRowCount());
                        }
                        bw.close();
                        Thread.sleep(250);
                        pb.dispose();
                        Thread.sleep(200);
                        MyMessageUtil.showInfoMsg("노드 통계 정보가 성공적으로 저장되었습니다.");
                    } catch (Exception ex) {
                        try {
                            MyMessageUtil.showErrorMsg("노드 통계 정보를 기록하는 중에 에러가 발생했습니다.");
                            if (bw != null) bw.close();
                            if (fc.getSelectedFile().exists()) {
                                fc.getSelectedFile().delete();
                            }
                        } catch (Exception e) {e.printStackTrace();}
                    }
                } else if (e.getSource() == edgeStatSaveBtn) {
                    try {
                        fc.setFont(MyVars.f_pln_12); //For hangul file names.
                        fc.setMultiSelectionEnabled(false);
                        fc.setPreferredSize(new Dimension(600, 460));
                        fc.showSaveDialog(MyVars.main);
                        MyProgressBar pb = new MyProgressBar(false);
                        int pbCnt = 0;
                        bw = new BufferedWriter(new FileWriter(fc.getSelectedFile()));
                        bw.write("NO.,SOURCE,DEST,CONTRIBUTION,SUPPORT,CONFIDENCE,LIFT\n");
                        for (int i = 0; i < edgeContributionStatTable.getRowCount(); i++) {
                            bw.write(edgeContributionStatTable.getValueAt(i, 0).toString() + "," +
                            edgeContributionStatTable.getValueAt(i, 1).toString() + "," +
                            edgeContributionStatTable.getValueAt(i, 2).toString() + "," +
                            edgeContributionStatTable.getValueAt(i, 3).toString() + "," +
                            edgeContributionStatTable.getValueAt(i, 4).toString() + "," +
                            edgeContributionStatTable.getValueAt(i, 5).toString() + "\n");
                            pb.updateValue(++pbCnt, edgeContributionStatTable.getRowCount());
                        }
                        bw.close();
                        Thread.sleep(250);
                        pb.dispose();
                        Thread.sleep(200);
                        MyMessageUtil.showInfoMsg("엣지 통계 정보가 성공적으로 저장되었습니다.");
                    }  catch (Exception ex) {
                        try {
                            MyMessageUtil.showErrorMsg("엣지 통계 정보를 기록하는 중에 에러가 발생했습니다.");
                            if (bw != null) bw.close();
                            if (fc.getSelectedFile().exists()) {fc.getSelectedFile().delete();}
                        } catch (Exception e) {e.printStackTrace();}
                    }
                }
            }
        }).start();
    }

    public int getNodeStatOrderByIndex() {return this.nodeOrderBy.getSelectedIndex();}
    public int getEdgeStatOrderByIndex() {return this.edgeOrderBy.getSelectedIndex();}

}
