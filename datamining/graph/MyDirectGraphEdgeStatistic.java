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
        this.setPreferredSize(new Dimension(800, 550));
        this.setLayout(new BorderLayout(3, 3));

        this.edgeStatPanel.setLayout(new BorderLayout(3, 3));
        this.model = new DefaultTableModel(new String[][]{}, new String[]{"NO.", "SOURCE", "DEST", "CONT.", "SUPPORT", "CONFIDENCE", "LIFT"});
        this.table = new JTable(this.model);

        this.table.getTableHeader().setFont(MyVars.tahomaBoldFont12);
        this.table.setRowHeight(21);
        this.table.setFont(MyVars.f_pln_12);
        this.table.setBackground(Color.WHITE);
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
        this.edgeOrderByComboBox.addItem("SUPPORT");
        this.edgeOrderByComboBox.addItem("CONFIDENCE");
        this.edgeOrderByComboBox.addItem("LIFT");
        this.edgeOrderByComboBox.setFocusable(false);
        this.edgeStatPanel.add(edgeOrderByPanel, BorderLayout.NORTH);
        this.edgeStatPanel.add(new JScrollPane(this.table), BorderLayout.CENTER);
        this.edgeStatPanel.add(this.edgeStatDataSavePanel, BorderLayout.SOUTH);

        this.edgeOrderByComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MyVars.edgeOrderByComboBoxIdx = edgeOrderByComboBox.getSelectedIndex();
                        frame.setAlwaysOnTop(true);
                        setEdgeContributionStatistics();
                        revalidate();
                        repaint();
                        frame.setAlwaysOnTop(false);
                    }
                }).start();
            }
        });
        this.add(this.edgeStatPanel, BorderLayout.CENTER);
        this.tableCreated = true;
    }

    private void setEdgeContributionStatistics() {
        if (!this.tableCreated) {
            MyProgressBar pb = new MyProgressBar(false);

            int recCnt = 0;
            ArrayList<MyDirectEdge> edges = new ArrayList<>(MyVars.directMarkovChain.getEdges());
            Collections.sort(edges);
            for (int i = edges.size() - 1; i >= 0; i--) {
                this.model.addRow(new String[]{
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

            pb.updateValue(100, 100);
            pb.dispose();
        } else {
            MyProgressBar pb = new MyProgressBar(false);
            ArrayList<MyDirectEdge> edges = new ArrayList<>(MyVars.directMarkovChain.getEdges());
            Collections.sort(edges);
            int totalRowCount = edges.size()*2;
            int recCnt = 0;
            for (int i=this.table.getRowCount()-1; i >= 0; i--) {
                this.model.removeRow(i);
                pb.updateValue(++recCnt, totalRowCount);
            }

            int rowCnt = 0;
            for (int i = edges.size() - 1; i >= 0; i--) {
                this.model.addRow(new String[]{
                    String.valueOf(++rowCnt),
                    edges.get(i).getSource().getLabel(),
                    edges.get(i).getDest().getLabel(),
                    MyMathUtil.getCommaSeperatedNumber(edges.get(i).getContribution()),
                    MyMathUtil.threeDecimalFormat(edges.get(i).getSupport()),
                    MyMathUtil.threeDecimalFormat(edges.get(i).getConfidence()),
                    MyMathUtil.threeDecimalFormat(edges.get(i).getLift())
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
                if (e.getSource() == edgeStatSaveBtn) {
                    try {
                        fc.setFont(MyVars.f_pln_12); //For hangul file names.
                        fc.setMultiSelectionEnabled(false);
                        fc.setPreferredSize(new Dimension(600, 460));
                        fc.showSaveDialog(MyVars.main);
                        MyProgressBar pb = new MyProgressBar(false);
                        int pbCnt = 0;
                        bw = new BufferedWriter(new FileWriter(fc.getSelectedFile()));
                        bw.write("NO.,SOURCE,DEST,CONTRIBUTION,SUPPORT,CONFIDENCE,LIFT\n");
                        for (int i = 0; i < table.getRowCount(); i++) {
                            bw.write(table.getValueAt(i, 0).toString() + "," +
                                table.getValueAt(i, 1).toString() + "," +
                                table.getValueAt(i, 2).toString() + "," +
                                table.getValueAt(i, 3).toString() + "," +
                                table.getValueAt(i, 4).toString() + "," +
                                table.getValueAt(i, 5).toString() + "\n");
                            pb.updateValue(++pbCnt, table.getRowCount());
                        }
                        bw.close();
                        Thread.sleep(250);
                        pb.dispose();
                        Thread.sleep(200);
                        MyMessageUtil.showInfoMsg("Node statistics have been successfully saved to storage.");
                    } catch (Exception ex) {
                        try {
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
