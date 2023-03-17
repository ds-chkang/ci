package datamining.graph.stats;

import datamining.main.MyProgressBar;
import datamining.graph.common.MyVariableNode;
import datamining.utils.message.MyMessageUtil;
import datamining.utils.MyMathUtil;
import datamining.utils.system.MySysUtil;
import datamining.utils.system.MyVars;
import datamining.utils.table.MyTableUtil;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MyVariableStatisticsByNode
extends JPanel
implements ActionListener {

    private JTable table;
    private String [][] data = {};
    private DefaultTableModel model;
    private JButton save = new JButton(" SAVE ");
    private String [] columns = {"NO.", "VARIABLE", "CONT."};
    private JTextField searchTxt = new JTextField();

    public MyVariableStatisticsByNode() {
        this.decorate();
        JFrame frame = new JFrame("VARIABLE STATISTICS BY NODE");
        frame.setLayout(new BorderLayout(5,5));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(this, BorderLayout.CENTER);
        frame.setPreferredSize(new Dimension(400, 600));
        frame.pack();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int)screenSize.getWidth();
        int height = (int)screenSize.getHeight();

        frame.setLocation(width-395, height-635);
        frame.setVisible(true);
    }

    private void decorate() {
        MyProgressBar pb = new MyProgressBar(false);

        this.setLayout(new BorderLayout(5,5));
        this.setBackground(Color.WHITE);

        pb.updateValue(10, 100);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        infoPanel.setBackground(Color.WHITE);

        JLabel numberOfNodeLabel = new JLabel();
        numberOfNodeLabel.setBackground(Color.WHITE);
        numberOfNodeLabel.setFont(MyVars.tahomaPlainFont11);
        infoPanel.add(numberOfNodeLabel);

        JLabel selectedNodeLabel = new JLabel();
        selectedNodeLabel.setFont(MyVars.tahomaPlainFont11);
        selectedNodeLabel.setBackground(Color.WHITE);
        infoPanel.add(selectedNodeLabel);

        JLabel selectedNode = new JLabel("");
        selectedNode.setFont(MyVars.tahomaPlainFont11);
        selectedNode.setBackground(Color.WHITE);
        infoPanel.add(selectedNode);
        pb.updateValue(25, 100);

        this.setNodeStatistics();
        pb.updateValue(60, 100);

        JScrollPane tableScrollPane = new JScrollPane(this.table);
        tableScrollPane.setPreferredSize(new Dimension(350, 600));
        this.add(tableScrollPane, BorderLayout.CENTER);
        this.searchTxt.setBorder(new LineBorder(Color.DARK_GRAY,1));
        this.searchTxt.setFont(MyVars.f_bold_12);
        JPanel searchAndSavePanel = MyTableUtil.searchAndSaveDataPanelForJTable2(this, this.searchTxt, this.save, this.model, this.table);
        this.save.setFont(MyVars.tahomaPlainFont12);
        this.save.setPreferredSize(new Dimension(70, 29));
        searchAndSavePanel.setBackground(Color.WHITE);
        this.add(searchAndSavePanel, BorderLayout.SOUTH);
        this.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.table.setRowSorter(MyTableUtil.setJTableRowSorterWithTextField(this.model, this.searchTxt));
        this.table.setSelectionBackground(Color.BLACK);
        this.table.setSelectionForeground(Color.WHITE);
        this.table.setFocusable(false);
        Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
        this.table.setCursor(cursor);
        this.setPreferredSize(new Dimension(400, 700));

        numberOfNodeLabel.setText("NO. OF NODES: " + MyMathUtil.getCommaSeperatedNumber(this.table.getRowCount()));
        this.searchTxt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                numberOfNodeLabel.setText("NO. OF NODES: " + MyMathUtil.getCommaSeperatedNumber(table.getRowCount()));
            }
        });
        pb.updateValue(80, 100);

        Border blackline = BorderFactory.createLineBorder(Color.black);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(blackline, "VARIABLE STATISTICS RELATED TO NODE("+ MySysUtil.decodeNodeName(MyVars.getViewer().selectedNode.getName())+")");
        titledBorder.setTitleJustification(TitledBorder.LEFT);
        titledBorder.setTitleFont(MyVars.tahomaBoldFont11);
        titledBorder.setTitleColor(Color.DARK_GRAY);
        this.setBorder(titledBorder);
        pb.updateValue(100,100);
        pb.dispose();
    }

    private void setNodeStatistics() {
        try {
            Map<String, Integer> variableMap = new HashMap<>();
            for (int s=0; s < MyVars.seqs.length; s++) {
                String [] variables = MyVars.seqs[s][0].split(MyVars.commaDelimeter);
                for (int i=0; i < MyVars.seqs[s].length; i++) {
                    String itemset = "";
                    itemset = MyVars.seqs[s][i].split(":")[0];
                    if (itemset.equals(MyVars.getViewer().selectedNode.getName())) {
                        for (String var : variables) {
                            if (variableMap.containsKey(var)) { variableMap.put(var, variableMap.get(var)+1);
                            } else {variableMap.put(var, 1);}
                        }
                    }
                }
            }

            this.model = new DefaultTableModel(this.data, this.columns);
            ArrayList<MyVariableNode> tableRecords = new ArrayList<>();
            for (String key : variableMap.keySet()) {
                MyVariableNode tr = new MyVariableNode();
                tr.setName(MySysUtil.decodeVariable(key));
                tr.setValue(variableMap.get(key));
                tableRecords.add(tr);
            }

            int rowCnt = 0;
            Collections.sort(tableRecords);
            for (MyVariableNode tr : tableRecords) {
                this.model.addRow(new String[]{String.valueOf(++rowCnt), tr.getName(), String.valueOf(tr.getValue())});
            }

            this.table = new JTable(model);
            this.table.getColumnModel().getColumn(0).setPreferredWidth(60);
            this.table.getColumnModel().getColumn(1).setPreferredWidth(250);
            this.table.setBackground(Color.WHITE);
            this.table.setFont(MyVars.f_pln_12);
            this.table.getTableHeader().setFont(MyVars.tahomaBoldFont12);
            this.table.setRowHeight(25);
        } catch (Exception ex) {ex.printStackTrace();}
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fc = new JFileChooser();
        BufferedWriter bw = null;
        if (e.getSource() == this.save) {
            try {
                fc.setFont(MyVars.f_pln_12); //For hangul file names.
                fc.setMultiSelectionEnabled(false);
                fc.setPreferredSize(new Dimension(600, 460));
                fc.showSaveDialog(MyVars.app);
                MyProgressBar pb = new MyProgressBar(false);
                int pbCnt = 0;
                bw = new BufferedWriter(new FileWriter(fc.getSelectedFile()));
                bw.write("NO.,VARIABLE,CONTRIBUTION\n");
                for (int i = 0; i < this.table.getRowCount(); i++) {
                    bw.write(this.table.getValueAt(i, 0).toString() + "," +
                    this.table.getValueAt(i, 1).toString() + "," +
                    this.table.getValueAt(i, 2).toString() + "\n");
                    pb.updateValue(++pbCnt, this.table.getRowCount());
                }
                bw.close();
                Thread.sleep(250);
                pb.dispose();
                Thread.sleep(200);
                MyMessageUtil.showInfoMsg("Variable stastistics have been successfully saved to storage");
            } catch (NullPointerException e2) {
                MyMessageUtil.showErrorMsg("Variable statistics has not been saved to storage!");
            } catch (Exception e3) {
                try {
                    MyMessageUtil.showErrorMsg("An unexpected operation error has occurred while writing variable statistics to storage!");
                    if (bw != null) bw.close();
                    if (fc.getSelectedFile().exists()) {
                        fc.getSelectedFile().delete();
                    }
                } catch (Exception e4) {e4.printStackTrace();}
            }
        }
    }
}




