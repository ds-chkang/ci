package datamining.config;

import datamining.utils.message.MyMessageUtil;
import datamining.utils.system.MyVars;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class MyDirectNodeValueTabPane
extends MyDirectTabPane {

    private final String TAB_TITLE = " NODE VALUES  ";
    private MyDirectNodeValueTable nodeValueTable;
    private JScrollPane tableScrollPane;
    private String [] headers;
    private JPanel tablePanel;
    private JButton headerBtn;
    private JButton dataBtn;
    public File dataFile;

    public MyDirectNodeValueTabPane() {
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
        this.nodeValueTable = new MyDirectNodeValueTable() {@Override
            public TableCellRenderer getCellRenderer(int row, int column) {return new MyParameterTableRowHeaderRenderer();}
        };
        this.tableScrollPane = new JScrollPane(this.nodeValueTable);
        this.tableScrollPane.setBorder(BorderFactory.createEtchedBorder(Color.decode("#bcd1e4"), Color.decode("#bfd4ed")));
        this.tableScrollPane.setPreferredSize(new Dimension(615, 240));
        this.tableScrollPane.setBorder(BorderFactory.createLineBorder(new Color(0.0f, 0.0f, 0.0f, 0.0f)));
        this.tablePanel = new JPanel();
        this.tablePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        this.tablePanel = new JPanel();
        this.tablePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.tablePanel.setLayout(new BorderLayout(3, 3));
        this.tablePanel.add(this.tableScrollPane, BorderLayout.CENTER);

        this.headerBtn = new JButton("HEADER");
        this.headerBtn.setFont(MyVars.tahomaPlainFont12);
        this.headerBtn.setBackground(Color.WHITE);
        this.headerBtn.setFocusable(false);
        this.headerBtn.addActionListener(new ActionListener() {@Override public void actionPerformed(ActionEvent e) {new Thread(new Runnable() {@Override public void run() {setHeaderFile();}}).start();}});

        this.dataBtn = new JButton("DATA");
        this.dataBtn.setFont(MyVars.tahomaPlainFont12);
        this.dataBtn.setFocusable(false);
        this.dataBtn.setEnabled(false);
        this.dataBtn.addActionListener(new ActionListener() {@Override public void actionPerformed(ActionEvent e) {new Thread(new Runnable() {@Override public void run() {setDataFile();}}).start();}});

        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 3));
        btnPanel.add(this.headerBtn);
        btnPanel.add(this.dataBtn);
        this.tablePanel.add(btnPanel, BorderLayout.SOUTH);
    }

    private void setDataFile() {
        try {
            JFileChooser fc = new JFileChooser();
            fc.setFocusable(false);
            fc.setFont(MyVars.f_pln_12);
            fc.setPreferredSize(new Dimension(600, 460));
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
                MyVars.main.getMsgBroker().getDirectConfigPanel().getNodeLabelTable().update(this.headers, true);
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
    public MyDirectNodeValueTable getTable() {return this.nodeValueTable;}
}