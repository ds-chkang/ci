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
    private JFrame frame;
    private boolean tableCreated;

    public MyDirectGraphNodeStatistic() {
        this.decorate();
        this.setNodeContributionStatistics();
        this.frame = new JFrame("NODE STATISTICS");
        this.frame.setLayout(new BorderLayout(3,3));
        this.frame.add(this, BorderLayout.CENTER);
        this.frame.pack();
        this.frame.setLocation(5,5);
        this.frame.setAlwaysOnTop(true);
        this.frame.setVisible(true);
        this.frame.setAlwaysOnTop(false);
    }

    private void decorate() {
        this.setPreferredSize(new Dimension(800, 550));
        this.setLayout(new BorderLayout(3, 3));
        this.nodeStatPanel.setLayout(new BorderLayout(3,3));
        this.model = new DefaultTableModel(new String[][]{}, new String[]{"NO.", "NODE", "SUCCESSORS", "PREDECESSORS", "OUT CONTRIBUTION", "IN CONTRIBUTION", "CONTRIBUTION"});
        this.table = new JTable(this.model);
        this.table.getTableHeader().setFont(MyVars.tahomaBoldFont12);
        this.table.setRowHeight(21);
        this.table.setFont(MyVars.f_pln_12);
        this.table.setBackground(Color.WHITE);
        this.nodeStatSaveBtn = new JButton("SAVE");
        this.nodeStatSaveBtn.setFont(MyVars.tahomaPlainFont12);
        this.nodeSearchTxt = new JTextField();
        this.nodeSearchTxt.setFont(MyVars.tahomaPlainFont12);
        this.nodeSearchTxt.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        this.nodeStatDataSavePanel = MyTableUtil.searchAndSaveDataPanelForJTable2(this, this.nodeSearchTxt, this.nodeStatSaveBtn, this.model, this.table);
        this.nodeStatDataSavePanel.setPreferredSize(new Dimension(100, 26));

        JPanel nodeOrderByPanel = new JPanel();
        nodeOrderByPanel.setLayout(new BorderLayout(3, 3));
        nodeOrderByPanel.setPreferredSize(new Dimension(200, 28));
        JLabel nodeOrderByLabel = new JLabel(" ORDER BY: ");
        nodeOrderByLabel.setFont(MyVars.tahomaPlainFont12);
        nodeOrderByLabel.setPreferredSize(new Dimension(70, 25));
        nodeOrderByPanel.add(nodeOrderByLabel, BorderLayout.WEST);
        nodeOrderByPanel.add(this.nodeOrderByComboBox, BorderLayout.CENTER);
        this.nodeOrderByComboBox.setFont(MyVars.tahomaPlainFont12);
        this.nodeOrderByComboBox.addItem("CONTRIBUTION");
        this.nodeOrderByComboBox.addItem("SUCCESSOR");
        this.nodeOrderByComboBox.addItem("PREDECESSOR");
        this.nodeOrderByComboBox.addItem("OUT-CONTRIBUTION");
        this.nodeOrderByComboBox.addItem("IN-CONTRIBUTION");
        this.nodeOrderByComboBox.setFocusable(false);
        this.nodeStatPanel.add(nodeOrderByPanel, BorderLayout.NORTH);
        this.nodeStatPanel.add(new JScrollPane(this.table), BorderLayout.CENTER);
        this.nodeStatPanel.add(this.nodeStatDataSavePanel, BorderLayout.SOUTH);
        this.nodeOrderByComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MyVars.nodeOrderByComboBoxIdx = nodeOrderByComboBox.getSelectedIndex();
                        frame.setAlwaysOnTop(true);
                        setNodeContributionStatistics();
                        revalidate();
                        repaint();
                        frame.setAlwaysOnTop(false);
                    }
                }).start();
            }
        });
        this.add(this.nodeStatPanel, BorderLayout.CENTER);
    }

    private void setNodeContributionStatistics() {
        if (!this.tableCreated) {
            MyProgressBar pb = new MyProgressBar(false);
            MyVars.nodeOrderByComboBoxIdx = 0;
            ArrayList<MyDirectNode> nodes = new ArrayList<>(MyVars.directMarkovChain.getVertices());
            Collections.sort(nodes);
            int recCnt = 0;
            for (int i = nodes.size()-1; i >= 0; i--) {
                this.model.addRow(new String[]{
                    String.valueOf(++recCnt),
                    nodes.get(i).getLabel(),
                    MyMathUtil.getCommaSeperatedNumber(MyVars.directMarkovChain.getSuccessorCount(nodes.get(i))),
                    MyMathUtil.getCommaSeperatedNumber(MyVars.directMarkovChain.getPredecessorCount(nodes.get(i))),
                    MyMathUtil.getCommaSeperatedNumber(MyVars.directMarkovChain.getOutContributionByNode(nodes.get(i))),
                    MyMathUtil.getCommaSeperatedNumber(MyVars.directMarkovChain.getInContributionByNode(nodes.get(i))),
                    MyMathUtil.getCommaSeperatedNumber(nodes.get(i).getContribution())
                });
                pb.updateValue(recCnt, nodes.size());
            }
            pb.updateValue(100, 100);
            pb.dispose();
            this.tableCreated = true;
        } else {
            MyProgressBar pb = new MyProgressBar(false);
            ArrayList<MyDirectNode> nodes = new ArrayList<>(MyVars.directMarkovChain.getVertices());
            Collections.sort(nodes);
            int totalRowCount = nodes.size()*2;
            int recCnt = 0;
            for (int i=this.table.getRowCount()-1; i >= 0; i--) {
                this.model.removeRow(i);
                pb.updateValue(++recCnt, totalRowCount);
            }

            int rowCnt = 0;
            for (int i = nodes.size()-1; i >= 0; i--) {
                this.model.addRow(new String[]{
                    String.valueOf(++rowCnt),
                    nodes.get(i).getLabel(),
                    MyMathUtil.getCommaSeperatedNumber(MyVars.directMarkovChain.getSuccessorCount(nodes.get(i))),
                    MyMathUtil.getCommaSeperatedNumber(MyVars.directMarkovChain.getPredecessorCount(nodes.get(i))),
                    MyMathUtil.getCommaSeperatedNumber(MyVars.directMarkovChain.getOutContributionByNode(nodes.get(i))),
                    MyMathUtil.getCommaSeperatedNumber(MyVars.directMarkovChain.getInContributionByNode(nodes.get(i))),
                    MyMathUtil.getCommaSeperatedNumber(nodes.get(i).getContribution())
                });
                pb.updateValue(++recCnt, totalRowCount);
            }
            pb.updateValue(100, 100);
            pb.dispose();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        new Thread(new Runnable() {
            @Override
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
                        bw.write("NO.,NODE,SUCCESSOR,PREDECESSOR,OUT-CONTRIBUTION,IN-CONTRIBUTION\n");
                        for (int i = 0; i < table.getRowCount(); i++) {
                            bw.write(
                        table.getValueAt(i, 0).toString() + "," +
                             table.getValueAt(i, 1).toString() + "," +
                             table.getValueAt(i, 2).toString() + "," +
                             table.getValueAt(i, 3).toString() + "," +
                             table.getValueAt(i, 4).toString() + "," +
                             table.getValueAt(i, 5).toString() + "\n");
                            pb.updateValue(++pbCnt, table.getRowCount());
                        }
                        bw.close();
                        Thread.sleep(200);
                        pb.dispose();
                        Thread.sleep(200);
                        MyMessageUtil.showInfoMsg("Node statistics have been successfully saved to storage.");
                    } catch (Exception ex) {
                        try {
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
