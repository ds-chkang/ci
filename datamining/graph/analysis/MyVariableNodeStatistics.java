package datamining.graph.analysis;

import datamining.graph.MyDirectNode;
import datamining.main.MyProgressBar;
import datamining.utils.MyMathUtil;
import datamining.utils.system.MySysUtil;
import datamining.utils.system.MyVars;
import datamining.utils.table.MyTableUtil;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class MyVariableNodeStatistics
extends JFrame
implements ActionListener {

    private String [] columns = {
            "NO.",
            "VARIABLE NODE",
            "CONT.",
            "UNIQ. CONT."
    };

    private String [][] data = {};

    private JTable table;
    private DefaultTableModel model;
    private JTextField searchTxt = new JTextField();
    private JButton save = new JButton(" SAVE ");
    private MyGraphAnalyzerViewer funnelViewer;
    private MyDirectNode currentSelectedNode;
    private JPanel contentPanel = new JPanel();

    public MyVariableNodeStatistics(MyGraphAnalyzerViewer funnelViewer, MyDirectNode currentSelectedNode) {
        super("VARIABLE NODE STATISTICS");
        this.funnelViewer = funnelViewer;
        this.currentSelectedNode = currentSelectedNode;

        this.contentPanel.setBackground(Color.WHITE);
        this.contentPanel.setLayout(new BorderLayout(5,5));
        this.decorate();
        this.setLayout(new BorderLayout(5,5));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.getContentPane().add(this.contentPanel, BorderLayout.CENTER);
        this.setPreferredSize(new Dimension(800, 600));
        this.pack();
        this.setVisible(true);
    }

    private void decorate() {
        MyProgressBar pb = new MyProgressBar(false);
        this.setLayout(new BorderLayout(5, 5));
        this.setBackground(Color.WHITE);
        pb.updateValue(10, 100);

        this.setVariableNodeStatistics();

        JLabel numberOfNodeLabel = new JLabel("NO. OF NODES: " + MyMathUtil.getCommaSeperatedNumber(this.table.getRowCount()));
        numberOfNodeLabel.setBackground(Color.WHITE);
        numberOfNodeLabel.setFont(MyVars.tahomaPlainFont12);

        JLabel selectedNode = new JLabel("");
        selectedNode.setFont(MyVars.tahomaPlainFont12);
        selectedNode.setBackground(Color.WHITE);

        this.table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                selectedNode.setText("    SELECTED EDGE: " + table.getValueAt(table.getSelectedRow(), 1).toString() + " - " +
                                                             table.getValueAt(table.getSelectedRow(), 2).toString());
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
        this.contentPanel.add(infoPanel, BorderLayout.NORTH);
        this.contentPanel.add(tableScrollPane, BorderLayout.CENTER);
        this.contentPanel.add(MyTableUtil.searchAndSaveDataPanelForJTable(this, searchTxt, this.save, this.model, this.table), BorderLayout.SOUTH);
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
        titledBorder.setTitleFont(MyVars.tahomaBoldFont12);
        titledBorder.setTitleColor(Color.DARK_GRAY);
        this.contentPanel.setBorder(titledBorder);
        pb.updateValue(100, 100);
        pb.dispose();
    }

    private Map<String, Integer> getContributions() {
        Map<String, Integer> variableNodeMap = new HashMap<>();
        for (int sequence=0; sequence < MyVars.seqs.length; sequence++) {
            String predecessor = MyVars.seqs[sequence][0].split(":")[0];
            for (int i=1; i < MyVars.seqs[sequence].length; i++) {
                String successor = MyVars.seqs[sequence][i-1].split(":")[0];
                if (successor.equals(this.currentSelectedNode.getName())) {
                    if (variableNodeMap.containsKey(predecessor)) {
                        variableNodeMap.put(predecessor, variableNodeMap.get(predecessor)+1);
                    } else {
                        variableNodeMap.put(predecessor, 1);
                    }
                }
            }
        }
        return variableNodeMap;
    }

    private Map<String, Integer> getUniqueContributions() {
        Map<String, Integer> variableNodeMap = new HashMap<>();
        for (int sequence=0; sequence < MyVars.seqs.length; sequence++) {
            String predecessor = MyVars.seqs[sequence][0].split(":")[0];
            for (int i=1; i < MyVars.seqs[sequence].length; i++) {
                String successor = MyVars.seqs[sequence][i].split(":")[0];
                if (successor.equals(this.currentSelectedNode.getName())) {
                    if (variableNodeMap.containsKey(predecessor)) {
                        variableNodeMap.put(predecessor, variableNodeMap.get(predecessor) + 1);
                    } else {
                        variableNodeMap.put(predecessor, 1);
                    }
                    break;
                }
            }
        }
        return variableNodeMap;
    }

    private void setVariableNodeStatistics() {
        try {
            this.model = new DefaultTableModel(this.data, this.columns);
            Map<String, Integer> contributionMap = this.getContributions();
            Map<String, Integer> uniqueContributionMap = this.getUniqueContributions();

            int nodeCnt = 0;
            for (String varaibleItemset : contributionMap.keySet()) {
                model.addRow(new String[]{
                        String.valueOf(++nodeCnt),
                        MySysUtil.decodeNodeName(varaibleItemset),
                        MyMathUtil.getCommaSeperatedNumber(contributionMap.get(varaibleItemset)),
                        MyMathUtil.getCommaSeperatedNumber(uniqueContributionMap.get(varaibleItemset))
                        });
            }
            this.table = new JTable(model);
            this.table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            this.table.getColumnModel().getColumn(0).setPreferredWidth(60);
            this.table.getColumnModel().getColumn(1).setPreferredWidth(250);
            this.table.getColumnModel().getColumn(2).setPreferredWidth(250);
            this.table.getColumnModel().getColumn(3).setPreferredWidth(250);

            this.table.setBackground(Color.WHITE);
            this.table.setFont(MyVars.f_pln_12);
            JTableHeader header = table.getTableHeader();
            header.setBackground(Color.ORANGE);
            this.table.getTableHeader().setBackground(Color.LIGHT_GRAY);
            this.table.setRowHeight(24);
        } catch (Exception ex) {}
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
