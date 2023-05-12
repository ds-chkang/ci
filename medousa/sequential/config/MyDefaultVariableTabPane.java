package medousa.sequential.config;


import medousa.message.MyMessageUtil;
import medousa.sequential.utils.MySequentialGraphVars;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Serializable;

/**
 * Created by changhee on 17. 7. 17.
 */
public class MyDefaultVariableTabPane
extends JTabbedPane
implements Serializable {

    private MyDefaultVariableTable defaultVariableTable;
    private JScrollPane tableScrollPane;
    private JPanel tablePanel;
    private String [] headers;
    private JButton headerBtn;
    private JButton dataBtn;
    public File dataFile;
    public MyDefaultVariableTabPane() {
        super();
        this.decorate();
    }

    private void decorate() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                setTable();
                setFont(MySequentialGraphVars.tahomaBoldFont12);
                addTab(" DEFAULT VARIABLES    ", tablePanel);
                setFocusable(false);
            }
        });
    }

    private void setTable() {
        this.defaultVariableTable = new MyDefaultVariableTable() {@Override
        public TableCellRenderer getCellRenderer(int row, int column) {return new MyParameterTableRowHeaderRenderer();}
        };

        this.tableScrollPane = new JScrollPane(this.defaultVariableTable);
        this.tableScrollPane.setBorder(BorderFactory.createEtchedBorder(Color.decode("#bcd1e4"), Color.decode("#bfd4ed")));
        this.tableScrollPane.setPreferredSize(new Dimension(615, 155));
        this.tableScrollPane.setBorder(BorderFactory.createLineBorder(new Color(0.0f, 0.0f, 0.0f, 0.0f)));
        this.tablePanel = new JPanel();
        this.tablePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        this.tablePanel = new JPanel();
        this.tablePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.tablePanel.setLayout(new BorderLayout(3, 3));
        this.tablePanel.add(this.tableScrollPane, BorderLayout.CENTER);

        this.headerBtn = new JButton("HEADER");
        this.headerBtn.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.headerBtn.setBackground(Color.WHITE);
        this.headerBtn.setFocusable(false);
        this.headerBtn.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override public void run() {
                        setHeaderFile();
                }
            });
        }});

        this.dataBtn = new JButton(" DATA ");
        this.dataBtn.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.dataBtn.setFocusable(false);
        this.dataBtn.setEnabled(false);
        this.dataBtn.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        setDataFile();}
                }).start();
            }
        });

        JPanel btnPanel = new JPanel();
        btnPanel.setPreferredSize(new Dimension(300, 29));
        btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 3));
        btnPanel.add(this.headerBtn);
        btnPanel.add(this.dataBtn);
        this.tablePanel.add(btnPanel, BorderLayout.SOUTH);
    }

    private void setDataFile() {
        try {
            this.dataBtn.setEnabled(false);
            JFileChooser fc = new JFileChooser();
            fc.setFocusable(false);
            fc.setFont(MySequentialGraphVars.f_pln_12);
            fc.setPreferredSize(new Dimension(600, 460));
            fc.setMultiSelectionEnabled(true);
            fc.showOpenDialog(MySequentialGraphVars.app);
            if (fc.getSelectedFile() != null) {
                MySequentialGraphVars.app.getSequentialGraphMsgBroker().setInputFiles(fc.getSelectedFiles());
                MyMessageUtil.showInfoMsg("Data have been successfully loaded!");
                this.dataBtn.setBackground(Color.WHITE);
                MySequentialGraphVars.app.getToolBar().runBtn.setEnabled(true);
            } else {MyMessageUtil.showErrorMsg("Failed to load data file!");}
            this.dataBtn.setEnabled(true);
        } catch (Exception ex) {ex.printStackTrace();}
    }

    private void setHeaderFile() {
        try {
            this.headerBtn.setEnabled(false);
            JFileChooser fc = new JFileChooser();
            fc.setFocusable(false);
            fc.setFont(MySequentialGraphVars.f_pln_12);
            fc.setPreferredSize(new Dimension(600, 460));
            fc.setMultiSelectionEnabled(false);
            fc.showOpenDialog(MySequentialGraphVars.app);
            if (fc.getSelectedFile() != null) {
                this.headers = MySequentialGraphVars.app.getSequentialGraphMsgBroker().loadHeader(fc.getSelectedFile());
                if (this.headers == null) {
                    MyMessageUtil.showErrorMsg( "Please, check the format of the provided header file.");
                    return;
                }
                BufferedReader br = new BufferedReader(new FileReader(fc.getSelectedFile()));
                String line = br.readLine();
                this.headers = line.split(MySequentialGraphVars.commaDelimeter);
                for (int i=0; i < this.headers.length; i++) {
                    this.headers[i] = " " + this.headers[i].toUpperCase();
                }
                this.defaultVariableTable.updateTable(this.headers, true);
                MySequentialGraphVars.app.getSequentialGraphMsgBroker().getConfigPanel().getSupplimentaryVariableTable().update(this.headers, true);
                MySequentialGraphVars.app.getSequentialGraphMsgBroker().getConfigPanel().getEdgeValueTable().update(this.headers, true);
                MySequentialGraphVars.app.getSequentialGraphMsgBroker().getConfigPanel().getEdgeLabelTable().update(this.headers, true);
                MySequentialGraphVars.app.getSequentialGraphMsgBroker().getConfigPanel().getNodeValueTable().update(this.headers, true);
                MySequentialGraphVars.app.getSequentialGraphMsgBroker().getConfigPanel().getNodeLabelTable().update(this.headers, true);
                this.dataBtn.setEnabled(true);
                this.dataBtn.setBackground(Color.GREEN);
            } else {MyMessageUtil.showErrorMsg("Failed to header!");}
            this.headerBtn.setEnabled(true);
        } catch (Exception ex) {ex.printStackTrace();}
    }
    public MyDefaultVariableTable getTable() {
        return this.defaultVariableTable;
    }


    class MyParameterTableRowHeaderRenderer extends JPanel implements TableCellRenderer {
        public MyParameterTableRowHeaderRenderer() {
            this.setLayout(new BorderLayout(0,5));
        }
        @Override public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel itemLabel = new JLabel(value.toString());
            itemLabel.setVerticalAlignment(CENTER);
            itemLabel.setFont(new Font("Noto Sans", Font.PLAIN, 12));
            this.add(itemLabel);
            return this;
        }
        @Override public void paintComponent(Graphics g) {}
    }
}


