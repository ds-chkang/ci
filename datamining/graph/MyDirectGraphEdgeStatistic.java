package datamining.graph;

import datamining.utils.message.MyMessageUtil;
import datamining.utils.MyMathUtil;
import datamining.utils.system.MySysUtil;
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
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;

public class MyDirectGraphEdgeStatistic
extends JPanel
implements ActionListener{

    JPanel edgeStatPanel = new JPanel();
    private JTable table;
    private DefaultTableModel model;
    private JPanel edgeStatDataSavePanel;
    private JButton edgeStatSaveBtn;
    private JTextField edgeSearchTxt;
    private JComboBox edgeOrderByComboBox = new JComboBox();
    private JFrame frame;
    private boolean tableCreated = false;

    public MyDirectGraphEdgeStatistic() {
        this.decorate();
        this.setEdgeContributionStatistics();
        this.frame = new JFrame("EDGE STATISTICS");
        this.frame.setLayout(new BorderLayout(3,3));
        this.frame.add(this, BorderLayout.CENTER);
        this.frame.pack();
        this.frame.setLocation(5,5);
        this.frame.setAlwaysOnTop(true);
        this.frame.setVisible(true);
    }

    private void decorate() {
        this.setPreferredSize(new Dimension(500, 550));
        this.setLayout(new BorderLayout(3, 3));

        this.edgeStatPanel.setLayout(new BorderLayout(3, 3));
        this.model = new DefaultTableModel(new String[][]{}, new String[]{"NO.", "SOURCE", "DEST", "CONT.", "BETWEENESS"});
        this.table = new JTable(this.model);

        this.table.getTableHeader().setFont(MyVars.tahomaBoldFont12);
        this.table.setRowHeight(21);
        this.table.setFont(MyVars.f_pln_12);
        this.table.setBackground(Color.WHITE);
        this.table.getTableHeader().setBackground(new Color(0,0,0,0));
        this.table.getTableHeader().setOpaque(false);
        this.edgeStatSaveBtn = new JButton("SAVE");
        this.edgeStatSaveBtn.setFont(MyVars.tahomaPlainFont11);
        this.edgeSearchTxt = new JTextField();
        this.edgeSearchTxt.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        this.edgeStatDataSavePanel = MyTableUtil.searchAndSaveDataPanelForJTable2(this, edgeSearchTxt, this.edgeStatSaveBtn, this.model, this.table);
        this.edgeStatDataSavePanel.setPreferredSize(new Dimension(100, 26));

        JPanel edgeOrderByPanel = new JPanel();
        edgeOrderByPanel.setLayout(new BorderLayout(3, 3));
        edgeOrderByPanel.setPreferredSize(new Dimension(200, 28));
        JLabel edgeOrderByLabel = new JLabel(" ORDER BY: ");
        edgeOrderByLabel.setFont(MyVars.tahomaPlainFont12);
        edgeOrderByLabel.setPreferredSize(new Dimension(70, 25));
        edgeOrderByPanel.add(edgeOrderByLabel, BorderLayout.WEST);
        edgeOrderByPanel.add(this.edgeOrderByComboBox, BorderLayout.CENTER);
        this.edgeOrderByComboBox.setFont(MyVars.tahomaPlainFont12);
        this.edgeOrderByComboBox.addItem("CONTRIBUTION");
        this.edgeOrderByComboBox.addItem("BETWEENESS");
        this.edgeOrderByComboBox.setFocusable(false);
        this.edgeStatPanel.add(edgeOrderByPanel, BorderLayout.NORTH);
        this.edgeStatPanel.add(new JScrollPane(this.table), BorderLayout.CENTER);
        this.edgeStatPanel.add(this.edgeStatDataSavePanel, BorderLayout.SOUTH);

        this.edgeOrderByComboBox.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                MyVars.currentThread = new Thread(new Runnable() {
                    @Override public void run() {
                        frame.setAlwaysOnTop(false);
                        setEdgeContributionStatistics();
                    }
                });
                MyVars.currentThread.start();
            }
        });
        this.add(this.edgeStatPanel, BorderLayout.CENTER);
        this.tableCreated = true;
    }

    private void setEdgeContributionStatistics() {
        if (!this.tableCreated) {
            MyProgressBar pb = new MyProgressBar(false);
            LinkedHashMap<String, Float> sortedEdgeMap = new LinkedHashMap<>();
            Collection<MyDirectEdge> edges = new ArrayList<>(MyVars.directMarkovChain.getEdges());
            for (MyDirectEdge e : edges) {
                String edge = e.getSource().getName() + "-" + e.getDest().getName();
                if (edgeOrderByComboBox.getSelectedIndex() == 0) {
                    sortedEdgeMap.put(edge, (float)e.getContribution());
                } else if (edgeOrderByComboBox.getSelectedIndex() == 1) {
                    sortedEdgeMap.put(edge, e.betweeness);
                }
            }
            sortedEdgeMap = MySysUtil.sortMapByFloatValue(sortedEdgeMap);

            for (int i = this.table.getRowCount()-1; i >= 0; i--) {
                ((DefaultTableModel) this.table.getModel()).removeRow(i);
            }

            int recCnt = 0;
            for (String edge : sortedEdgeMap.keySet()) {
                this.model.addRow(new String[]{
                    String.valueOf(++recCnt),
                    edge.split("-")[0],
                    edge.split("-")[1],
                        MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(sortedEdgeMap.get(edge))).split("\\.")[0],
                    "0.00"
                });
                pb.updateValue(recCnt, edges.size());
            }

            pb.updateValue(100, 100);
            pb.dispose();
        } else {
            if (edgeOrderByComboBox.getSelectedIndex() == 0) {
                MyProgressBar pb = new MyProgressBar(false);
                LinkedHashMap<String, Float> sortedEdgeMap = new LinkedHashMap<>();
                Collection<MyDirectEdge> edges = new ArrayList<>(MyVars.directMarkovChain.getEdges());
                for (MyDirectEdge e : edges) {
                    String edge = e.getSource().getName() + "-" + e.getDest().getName();
                    sortedEdgeMap.put(edge, (float) e.getContribution());
                }
                sortedEdgeMap = MySysUtil.sortMapByFloatValue(sortedEdgeMap);
                for (int i = this.table.getRowCount()-1; i >= 0; i--) {((DefaultTableModel) this.table.getModel()).removeRow(i);}

                int recCnt = 0;
                for (String edge : sortedEdgeMap.keySet()) {
                    this.model.addRow(new String[]{
                        String.valueOf(++recCnt),
                        edge.split("-")[0],
                        edge.split("-")[1],
                        MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(sortedEdgeMap.get(edge))).split("\\.")[0],
                        "0.00"
                    });
                    pb.updateValue(recCnt, edges.size());
                }
                pb.updateValue(100, 100);
                pb.dispose();
            } else if (edgeOrderByComboBox.getSelectedIndex() == 1) {
                MyEdgeBetweennessComputer edgeBetweennessComputer = new MyEdgeBetweennessComputer();
                LinkedHashMap<String, Float> sortedEdgeMap = edgeBetweennessComputer.getRankedEdgeBetweeness();
                for (int i = this.table.getRowCount()-1; i >= 0; i--) {((DefaultTableModel) this.table.getModel()).removeRow(i);}

                int recCnt = 0;
                for (String edge : sortedEdgeMap.keySet()) {
                    this.model.addRow(new String[]{
                        String.valueOf(++recCnt),
                        edge.split("-")[0],
                        edge.split("-")[1],
                        MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(sortedEdgeMap.get(edge))).split("\\.")[0],
                        MyMathUtil.twoDecimalFormat(sortedEdgeMap.get(edge))
                    });
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JFileChooser fc = new JFileChooser();
                BufferedWriter bw = null;
                MyProgressBar pb = null;
                if (e.getSource() == edgeStatSaveBtn) {
                    try {
                        fc.setFont(MyVars.f_pln_12); //For hangul file names.
                        fc.setMultiSelectionEnabled(false);
                        fc.setPreferredSize(new Dimension(600, 460));
                        fc.showSaveDialog(MyVars.main);
                        pb = new MyProgressBar(false);
                        int pbCnt = 0;
                        bw = new BufferedWriter(new FileWriter(fc.getSelectedFile()));
                        bw.write("NO.,SOURCE,DEST,CONTRIBUTION,SUPPORT,CONFIDENCE,LIFT\n");
                        for (int i = 0; i < table.getRowCount(); i++) {
                            bw.write(table.getValueAt(i, 0).toString() + "," +
                                table.getValueAt(i, 1).toString() + "," +
                                    table.getValueAt(i, 2).toString() + "," +
                                    table.getValueAt(i, 3).toString() + "," +
                                table.getValueAt(i, 4).toString() + "," + "\n");
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
                            if (fc.getSelectedFile().exists()) {
                                fc.getSelectedFile().delete();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }
}
