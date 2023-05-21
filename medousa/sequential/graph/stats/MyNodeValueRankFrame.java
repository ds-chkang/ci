package medousa.sequential.graph.stats;

import medousa.MyProgressBar;
import medousa.sequential.graph.MyNode;
import medousa.sequential.utils.MyMathUtil;
import medousa.sequential.utils.MySequentialGraphSysUtil;
import medousa.sequential.utils.MySequentialGraphVars;
import medousa.table.MyTableUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

public class MyNodeValueRankFrame
extends JFrame
implements ActionListener {

    private JTable table;
    private DefaultTableModel model;
    private JTextField searchTxt = new JTextField();
    private JButton saveBtn = new JButton("SAVE");

    public MyNodeValueRankFrame() {
        super(" NODE " + MySequentialGraphVars.getSequentialGraphViewer().nodeValueName + " VALUES");
        this.decorate();
    }

    private void decorate() {
        this.setLayout(new BorderLayout(3,3));
        this.setPreferredSize(new Dimension(600, 550));
        this.setNodeValues();
        JScrollPane tableScrollPane = new JScrollPane(this.table);
        tableScrollPane.setBackground(Color.WHITE);
        this.table.getColumnModel().getColumn(0).setPreferredWidth(60);
        this.table.getColumnModel().getColumn(1).setPreferredWidth(200);
        this.table.getColumnModel().getColumn(2).setPreferredWidth(60);
        JPanel searchAndSavePanel = MyTableUtil.searchAndSaveDataPanelForJTable2(this, this.searchTxt, this.saveBtn, this.model, this.table);
        this.searchTxt.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        this.saveBtn.setPreferredSize(new Dimension(70, 29));
        this.getContentPane().add(searchAndSavePanel, BorderLayout.SOUTH);
        this.getContentPane().add(tableScrollPane, BorderLayout.CENTER);
        this.pack();
        this.setVisible(true);
    }

    private void setNodeValues() {
        MyProgressBar pb = new MyProgressBar(false);
        String [] columns = new String[3];
        columns[0] = "NO.";
        columns[1] = "NODE";
        columns[2] = MySequentialGraphVars.getSequentialGraphViewer().nodeValueName;
        String [][] data = {};
        this.model = new DefaultTableModel(data, columns);
        this.table = new JTable(this.model);
        ArrayList<MyNode> nodes = new ArrayList<>(MySequentialGraphVars.g.getVertices());
        Collections.sort(nodes);
        int pbCnt = 0;
        for (MyNode n : nodes) {
            String nodeValue = MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(n.getCurrentValue()));
            nodeValue = (nodeValue.endsWith(".0") || nodeValue.endsWith(".00")? nodeValue.substring(0, nodeValue.indexOf(".0")) : nodeValue);
            this.model.addRow(new String[]{String.valueOf(++pbCnt), MySequentialGraphSysUtil.decodeNodeName(n.getName()), nodeValue});
            pb.updateValue(pbCnt, nodes.size());
        }
        this.table.getTableHeader().setFont(MySequentialGraphVars.tahomaBoldFont12);
        this.table.setRowHeight(28);
        this.table.setBackground(Color.WHITE);
        this.table.setFont(MySequentialGraphVars.f_pln_12);
        pb.updateValue(100, 100);
        pb.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {}
}
