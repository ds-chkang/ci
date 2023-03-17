package datamining.graph.stats;

import datamining.main.MyProgressBar;
import datamining.graph.MyEdge;
import datamining.utils.system.MySysUtil;
import datamining.utils.system.MyVars;
import datamining.utils.table.MyTableUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

public class MyEdgeValueRankFrame
extends JFrame
implements ActionListener {

    private JTable table;
    private DefaultTableModel model;
    private JTextField searchTxt = new JTextField();
    private JButton saveBtn = new JButton("SAVE");

    public MyEdgeValueRankFrame() {
        super(" EDGE " + MyVars.getViewer().edgeValName + " VALUES");
        this.decorate();
    }

    private void decorate() {
        this.setLayout(new BorderLayout(3,3));
        this.setPreferredSize(new Dimension(500, 550));
        this.setNodeValues();
        JScrollPane tableScrollPane = new JScrollPane(this.table);
        tableScrollPane.setBackground(Color.WHITE);
        this.table.getColumnModel().getColumn(0).setPreferredWidth(60);
        this.table.getColumnModel().getColumn(1).setPreferredWidth(160);
        this.table.getColumnModel().getColumn(2).setPreferredWidth(160);
        this.table.getColumnModel().getColumn(3).setPreferredWidth(60);
        JPanel searchAndSavePanel = MyTableUtil.searchAndSaveDataPanelForJTable2(this, this.searchTxt, this.saveBtn, this.model, this.table);
        this.searchTxt.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        this.searchTxt.setPreferredSize(new Dimension(150, 30));
        this.saveBtn.setPreferredSize(new Dimension(70, 30));
        this.searchTxt.setFont(MyVars.f_bold_12);
        this.saveBtn.setFont(MyVars.tahomaPlainFont12);
        this.getContentPane().add(searchAndSavePanel, BorderLayout.SOUTH);
        this.getContentPane().add(tableScrollPane, BorderLayout.CENTER);
        this.pack();
        this.setVisible(true);
    }

    private void setNodeValues() {
        MyProgressBar pb = new MyProgressBar(false);
        String [] columns = new String[4];
        columns[0] = "NO.";
        columns[1] = "PREDECESSOR";
        columns[2] = "SUCCESSOR";
        columns[3] = MyVars.getViewer().edgeValName;
        String [][] data = {};
        this.model = new DefaultTableModel(data, columns);
        this.table = new JTable(this.model);
        ArrayList<MyEdge> edges = new ArrayList<>(MyVars.g.getEdges());
        Collections.sort(edges);
        int pbCnt = 0;
        for (MyEdge e : edges) {
            String edgeValue = String.valueOf(e.getCurrentValue());
            edgeValue = (edgeValue.endsWith(".0") ? edgeValue.substring(0, edgeValue.indexOf(".0")) : edgeValue);
            edgeValue = MySysUtil.formatAverageValue(edgeValue);
            this.model.addRow(new String[]{String.valueOf(++pbCnt), MySysUtil.decodeNodeName(e.getSource().getName()), MySysUtil.decodeNodeName(e.getDest().getName()), edgeValue});
            pb.updateValue(pbCnt, edges.size());
        }
        this.table.getTableHeader().setFont(MyVars.tahomaBoldFont12);
        this.table.setRowHeight(27);
        this.table.setBackground(Color.WHITE);
        this.table.setFont(MyVars.f_pln_12);
        pb.updateValue(100, 100);
        pb.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
