package medousa.sequential.graph.stats;

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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MyNodeDepthAppearnaceStatistics
extends JPanel
implements ActionListener {

    private String [] columns = new String[MySequentialGraphVars.mxDepth +2];

    private String [][] data = {};

    private JTable table;
    private DefaultTableModel model;
    private JTextField searchTxt = new JTextField();
    private JButton save = new JButton(" SAVE ");

    public MyNodeDepthAppearnaceStatistics() {
        this.setColumns();
        this.decorate();
        JFrame f = new JFrame("NODE APPEARANCES BY DEPTH STATISTICS");
        f.setLayout(new BorderLayout(5,5));
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.getContentPane().add(this, BorderLayout.CENTER);
        f.setPreferredSize(new Dimension(800, 600));
        f.pack();
        f.setAlwaysOnTop(true);
        f.setVisible(true);
        f.setAlwaysOnTop(false);
    }

    private void setColumns() {
        this.columns[0] = "NO.";
        this.columns[1] = "NODE";
        for (int depth = 0; depth < MySequentialGraphVars.mxDepth; depth++) {
            this.columns[depth+2] = "DEPTH-"+(depth+1);
        }
    }

    private void decorate() {
        MyProgressBar pb = new MyProgressBar(false);
        this.setLayout(new BorderLayout(5, 5));
        this.setBackground(Color.WHITE);
        pb.updateValue(10, 100);

        this.setNodeStatistics();

        JLabel numberOfNodeLabel = new JLabel("NO. OF NODES: " + MyMathUtil.getCommaSeperatedNumber(this.table.getRowCount()));
        numberOfNodeLabel.setBackground(Color.WHITE);
        numberOfNodeLabel.setFont(MySequentialGraphVars.f_pln_12);

        JLabel selectedNode = new JLabel("");
        selectedNode.setFont(MySequentialGraphVars.f_pln_12);
        selectedNode.setBackground(Color.WHITE);

        this.table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                try {
                    selectedNode.setText("    SELECTED NODE: " + table.getValueAt(table.getSelectedRow(), 1).toString());
                } catch (Exception ex) {
                    selectedNode.setText("");
                }
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
        tableScrollPane.setPreferredSize(new Dimension(1000, 650));
        this.add(infoPanel, BorderLayout.NORTH);
        this.add(tableScrollPane, BorderLayout.CENTER);
        JPanel searchAndSavePanel = MyTableUtil.searchAndSaveDataPanelForJTable2(this, searchTxt, this.save, this.model, this.table);
        this.add(searchAndSavePanel, BorderLayout.SOUTH);;
        this.save.setPreferredSize(new Dimension(70, 28));
        this.searchTxt.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        this.searchTxt.setPreferredSize(new Dimension(100, 28));
        this.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.table.setRowSorter(MyTableUtil.setJTableRowSorterWithTextField(this.model, this.searchTxt));
        this.table.setSelectionBackground(Color.PINK);
        this.table.setSelectionForeground(Color.BLACK);
        this.table.setFocusable(false);
        Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
        this.table.setCursor(cursor);
        this.setPreferredSize(new Dimension(450, 700));

        Border blackline = BorderFactory.createLineBorder(Color.black);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(blackline, "NODE STATISTICS");
        titledBorder.setTitleJustification(TitledBorder.LEFT);
        titledBorder.setTitleFont(new Font("Arial", Font.PLAIN, 0));
        titledBorder.setTitleColor(Color.DARK_GRAY);
        //this.setBorder(titledBorder);
        pb.updateValue(100, 100);
        pb.dispose();
    }

    private Map<MyNode, Map<Integer, Integer>> getNodeDepthAppearaceCount(Collection<MyNode> nodes) {
        Map<MyNode, Map<Integer, Integer>> nodeDepthAppearanceCountMap = new HashMap<>();
        try {
            for (MyNode n : nodes) {
                for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                    for (int i = 0; i < MySequentialGraphVars.seqs[s].length; i++) {
                        String itemset = (MySequentialGraphVars.isTimeOn ? MySequentialGraphVars.seqs[s][i].split(":")[0] : MySequentialGraphVars.seqs[s][i]);
                        if (itemset.equals(n.getName())) {
                            if (nodeDepthAppearanceCountMap.containsKey(n)) {
                                if (nodeDepthAppearanceCountMap.get(n).containsKey(i+1)) {
                                    Map<Integer, Integer> depthCountMap = nodeDepthAppearanceCountMap.get(n);
                                    depthCountMap.put(i+1, depthCountMap.get(i+1)+1);
                                    nodeDepthAppearanceCountMap.put(n, depthCountMap);
                                } else {
                                    Map<Integer, Integer> depthCountMap = nodeDepthAppearanceCountMap.get(n);
                                    depthCountMap.put(i+1, 1);
                                    nodeDepthAppearanceCountMap.put(n, depthCountMap);
                                }
                            } else {
                                Map<Integer, Integer> depthCountMap = new HashMap<>();
                                depthCountMap.put(i+1, 1);
                                nodeDepthAppearanceCountMap.put(n, depthCountMap);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return nodeDepthAppearanceCountMap;
    }

    private void setNodeStatistics() {
        try {
            this.model = new DefaultTableModel(this.data, this.columns);
            Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();

            Map<MyNode, Map<Integer, Integer>> nodeDepthAppearanceCountMap = this.getNodeDepthAppearaceCount(nodes);
            int nodeCnt = 0;
            for (MyNode n : nodes) {
                Map<Integer, Integer> depthAppearnceCountMap = nodeDepthAppearanceCountMap.get(n);
                String [] data = new String[MySequentialGraphVars.mxDepth +2];
                data[0] = String.valueOf(++nodeCnt);
                data[1] = MySequentialGraphSysUtil.decodeNodeName(n.getName());
                for (int i = 0; i < MySequentialGraphVars.mxDepth; i++) {
                    data[i+2] = depthAppearnceCountMap.get(i+1) == null ? String.valueOf(0) : MyMathUtil.getCommaSeperatedNumber(depthAppearnceCountMap.get(i+1));
                }
                this.model.addRow(data);
            }
            this.table = new JTable(model);
            this.table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            this.table.getColumnModel().getColumn(0).setPreferredWidth(60);
            this.table.getColumnModel().getColumn(1).setPreferredWidth(250);
            for (int depth = 0; depth < MySequentialGraphVars.mxDepth; depth++) {
                this.table.getColumnModel().getColumn(depth+2).setPreferredWidth(130);
            }

            this.table.setBackground(Color.WHITE);
            this.table.setFont(MySequentialGraphVars.f_pln_12);
            this.table.getTableHeader().setFont(MySequentialGraphVars.tahomaBoldFont12);
            this.table.getTableHeader().setBackground(Color.LIGHT_GRAY);
            this.table.setRowHeight(24);
        } catch (Exception ex) {}
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
