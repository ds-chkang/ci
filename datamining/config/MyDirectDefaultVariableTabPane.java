package datamining.config;

import datamining.utils.message.MyMessageUtil;
import datamining.utils.system.MyVars;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

public class MyDirectDefaultVariableTabPane
extends MyDirectTabPane implements Serializable {

    private MyDirectDefaultVariableTable targetVariableTable;
    private JScrollPane tableScrollPane;
    private JPanel tablePanel;
    private JButton headerBtn;
    private JButton dataBtn;

    public MyDirectDefaultVariableTabPane() {
        super();

                createAndShowGui();

    }

    private void createAndShowGui() {
        this.decorate();
        this.setFont(MyVars.tahomaBoldFont12);
        this.addTab("" + "" + "" + "" + "" + "" + " DEFAULT VARIABLES    ", this.tablePanel);
        this.setEnabledAt(0, true);
        this.setFocusable(false);
    }

    @Override protected void decorate() {
        this.targetVariableTable = new MyDirectDefaultVariableTable() {
            @Override public TableCellRenderer getCellRenderer(int row, int column) {return new MyParameterTableRowHeaderRenderer();}
        };
        this.targetVariableTable.setBorder(BorderFactory.createEtchedBorder());
        this.tableScrollPane = new JScrollPane(this.targetVariableTable);
        this.tableScrollPane.setBorder(BorderFactory.createEtchedBorder(Color.decode("#bcd1e4"), Color.decode("#bfd4ed")));
        this.tableScrollPane.setPreferredSize(new Dimension(340, 125));
        this.tableScrollPane.setBorder(BorderFactory.createLineBorder(new Color(0.0f, 0.0f, 0.0f, 0.0f)));
        this.tablePanel = new JPanel();
        this.tablePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.tablePanel.setLayout(new BorderLayout(3,3));
        this.tablePanel.add(this.tableScrollPane, BorderLayout.CENTER);

        this.headerBtn = new JButton("HEADER");
        this.headerBtn.setFont(MyVars.tahomaPlainFont12);
        this.headerBtn.setBackground(Color.WHITE);
        this.headerBtn.setFocusable(false);
        this.headerBtn.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                try {
                    JFileChooser fc = new JFileChooser();
                    fc.setFont(MyVars.f_pln_12);
                    fc.setMultiSelectionEnabled(false);
                    fc.setPreferredSize(new Dimension(600, 460));
                    fc.showOpenDialog(MyVars.main);
                    if (fc.getSelectedFile() != null) {
                        String [] headers = MyVars.main.getMsgBroker().loadHeader(fc.getSelectedFile());
                        for (int i=0; i < headers.length; i++) {headers[i] = " " + headers[i];}
                        if (headers != null) {
                            MyVars.main.getMsgBroker().getDirectConfigPanel().getDefaultVariableTable().update(headers, true);
                            MyVars.main.getMsgBroker().getDirectConfigPanel().getEdgeValueTable().update(headers, true);
                            MyVars.main.getMsgBroker().getDirectConfigPanel().getEdgeNameTable().update(headers, true);
                            dataBtn.setEnabled(true);
                        }
                       MyMessageUtil.showInfoMsg("Headers are loaded successfully!");

                    } else {
                        MyMessageUtil.showErrorMsg("Loading headers failed.");
                    }
                } catch (Exception ex) {}
            }
        });

        this.dataBtn = new JButton("DATA");
        this.dataBtn.setFont(MyVars.tahomaPlainFont12);
        this.dataBtn.setBackground(Color.GREEN);
        this.dataBtn.setFocusable(false);
        this.dataBtn.setEnabled(false);
        this.dataBtn.addActionListener(new ActionListener() {@Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.setFont(MyVars.f_pln_12);
                fc.setPreferredSize(new Dimension(600, 460));
                fc.setMultiSelectionEnabled(true);
                fc.showOpenDialog(MyVars.main);
                if (fc.getSelectedFile() != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            MyVars.main.getMsgBroker().setInputFiles(fc.getSelectedFiles());
                            MyMessageUtil.showInfoMsg("Data have been successfully loaded!");
                            dataBtn.setBackground(Color.WHITE);
                            MyVars.main.getToolBar().runBtn.setEnabled(true);
                        }
                    }).start();
                } else {
                    new Thread(new Runnable() {
                        @Override public void run() {
                            MyMessageUtil.showErrorMsg("Failed to load data!");
                        }
                    }).start();
                }
            }});
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 3));
        btnPanel.add(this.headerBtn);
        btnPanel.add(this.dataBtn);
        this.tablePanel.add(btnPanel, BorderLayout.SOUTH);
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
    public MyDirectDefaultVariableTable getTable() {
        return targetVariableTable;
    }
}


