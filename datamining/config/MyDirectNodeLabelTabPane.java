package datamining.config;

import datamining.utils.message.MyMessageUtil;
import datamining.utils.system.MyVars;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class MyDirectNodeLabelTabPane
extends MyDirectTabPane {

    private final String TAB_TITLE = " NODE LABELS  ";
    private JPanel tablePanel;
    private JScrollPane tableScrollPane;
    private JButton headerBtn;
    private JButton dataBtn;
    private MyDirectNodeLabelTable nodeLabelTable;
    private String [] headers;
    public File dataFile;

    public MyDirectNodeLabelTabPane() {
        super();
        this.decorate();
    }

    @Override protected void decorate() {
        this.setTable();
        this.setFont(TAB_TITLE_FONT);
        this.addTab(TAB_TITLE, this.tablePanel);
        this.setEnabledAt(0, true);
        this.setFocusable(false);
    }

    public void setTable() {
        this.nodeLabelTable = new MyDirectNodeLabelTable() {@Override
            public TableCellRenderer getCellRenderer(int row, int column) {
            return new MyParameterTableRowHeaderRenderer();
        }
        };
        this.nodeLabelTable.setBorder(BorderFactory.createEtchedBorder());
        this.tableScrollPane = new JScrollPane(this.nodeLabelTable);
        this.tableScrollPane.setBorder(BorderFactory.createEtchedBorder(Color.decode("#bcd1e4"), Color.decode("#bfd4ed")));
        this.tableScrollPane.setPreferredSize(new Dimension(615, 240));
        this.tableScrollPane.setBorder(BorderFactory.createLineBorder(new Color(0.0f, 0.0f, 0.0f, 0.0f)));
        this.tablePanel = new JPanel();
        this.tablePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.tablePanel.setLayout(new BorderLayout(3, 3));
        this.tablePanel.add(this.tableScrollPane, BorderLayout.CENTER);
    }

    private void setDataFile() {
        try {
            JFileChooser fc = new JFileChooser();
            fc.setFocusable(false);
            fc.setFont(MyVars.f_pln_12);
            fc.setPreferredSize(new Dimension(600, 260));
            fc.setMultiSelectionEnabled(false);
            fc.showOpenDialog(MyVars.main);
            if (fc.getSelectedFile() != null) {
                this.dataFile = fc.getSelectedFile();
                MyMessageUtil.showInfoMsg("Data have been successfully loaded!");
                dataBtn.setBackground(Color.WHITE);
            } else {MyMessageUtil.showErrorMsg("Failed to load data file!");}
        } catch (Exception ex) {ex.printStackTrace();}
    }

    private void setHeaderFile() {
        try {
            JFileChooser fc = new JFileChooser();
            fc.setFocusable(false);
            fc.setFont(MyVars.f_pln_12);
            fc.setPreferredSize(new Dimension(600, 460));
            fc.setMultiSelectionEnabled(false);
            fc.showOpenDialog(MyVars.main);
            if (fc.getSelectedFile() != null) {
                BufferedReader br = new BufferedReader(new FileReader(fc.getSelectedFile()));
                String line = br.readLine();
                this.headers = line.split(MyVars.commaDelimeter);
                for (int i=0; i < this.headers.length; i++) {this.headers[i] = " " + this.headers[i].toUpperCase();}
                MyVars.main.getMsgBroker().getDirectConfigPanel().getNodeValueTable().update(this.headers, true);
                MyMessageUtil.showInfoMsg("Header has been successfully loaded!");
                dataBtn.setEnabled(true);
                dataBtn.setBackground(Color.GREEN);
            } else {MyMessageUtil.showErrorMsg("Failed to header!");}
        } catch (Exception ex) {ex.printStackTrace();}
    }

    public ArrayList<ArrayList<String>> getNodeValueData() {
        ArrayList<ArrayList<String>> data = new ArrayList<>();
        try {
            String line = "";
            BufferedReader br = new BufferedReader(new FileReader(this.dataFile));
            while ((line = br.readLine()) != null) {
                String [] rowArr = line.toUpperCase().split(MyVars.commaDelimeter);
                ArrayList<String> rowList = new ArrayList<>();
                for (String value : rowArr) {rowList.add(value);}
                data.add(rowList);
            }
        } catch (Exception ex) {ex.printStackTrace();}
        return data;
    }

    public int getNodeValueHeaderIndex(String header) {
        int idx = -1;
        for (int i=0; i < this.headers.length; i++) {
            if (this.headers[i].contains(header)) {
                idx = i;
                break;
            }
        }
        return idx;
    }

    class MyParameterTableRowHeaderRenderer extends JPanel implements TableCellRenderer {
        public MyParameterTableRowHeaderRenderer() {this.setLayout(new BorderLayout(0,5));}
        @Override public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel itemLabel = new JLabel(value.toString());
            itemLabel.setVerticalAlignment(CENTER);
            itemLabel.setFont(MyVars.tahomaPlainFont12);
            this.add(itemLabel);
            return this;
        }
        @Override public void paintComponent(Graphics g) {}
    }
    public MyDirectNodeLabelTable getTable() {return this.nodeLabelTable;}
}