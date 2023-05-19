package medousa.sequential.config;


import medousa.MyProgressBar;
import medousa.message.MyMessageUtil;
import medousa.sequential.graph.layout.MyFRLayout;
import medousa.sequential.utils.MySequentialGraphSysUtil;
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
    private JButton runBtn;
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
                        setDataFile();
                    }
                }).start();
            }
        });

        this.runBtn = new JButton("  RUN  ");
        this.runBtn.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.runBtn.setFocusable(false);
        this.runBtn.setEnabled(false);
        this.runBtn.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        createNetwork();
                    }
                }).start();
            }
        });

        JPanel headerDataPanel = new JPanel();
        headerDataPanel.setPreferredSize(new Dimension(200, 29));
        headerDataPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 3));
        headerDataPanel.add(this.headerBtn);
        headerDataPanel.add(this.dataBtn);

        JPanel runPanel = new JPanel();
        runPanel.setPreferredSize(new Dimension(100, 29));
        runPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
        runPanel.add(runBtn);


        JPanel btnPanel = new JPanel();
        btnPanel.setPreferredSize(new Dimension(300, 29));
        btnPanel.setLayout(new BorderLayout(3, 3));
        btnPanel.add(runPanel, BorderLayout.WEST);
        btnPanel.add(headerDataPanel, BorderLayout.EAST);

       // this.tablePanel.add(btnPanel, BorderLayout.SOUTH);

    }

    private void createNetwork() {
        new Thread(new Runnable() {
            @Override public void run() {
                try {
                    MyProgressBar pb = new MyProgressBar(false);
                    //runBtn.setForeground(Color.BLACK);
                    dataBtn.setEnabled(false);
                    runBtn.setForeground(dataBtn.getForeground());
                    runBtn.setEnabled(false);
                    MySequentialGraphVars.outputDir = MySequentialGraphSysUtil.getWorkingDir() + MySequentialGraphSysUtil.getDirectorySlash() + "elements" + MySequentialGraphSysUtil.getDirectorySlash();
                    MySequentialGraphVars.app.getSequentialGraphMsgBroker().getConfigPanel().getDefaultVariableTable().isTimeVariableOn();
                    MySequentialGraphVars.app.getSequentialGraphMsgBroker().categorize();
                    pb.updateValue(5, 100);
                    MySequentialGraphVars.app.getSequentialGraphMsgBroker().generateInputFeatures();
                    pb.updateValue(10, 100);
                    MySequentialGraphVars.app.getSequentialGraphMsgBroker().runEngine();
                    pb.updateValue(20, 100);
                    new Thread(new Runnable() {
                        @Override public void run() {
                            MySequentialGraphVars.app.getContentTabbedPane().setSelectedIndex(1);
                        }
                    }).start();

                    new Thread(new Runnable() {
                        @Override public void run() {
                            try {
                                MyFRLayout layout = new MyFRLayout<>(MySequentialGraphVars.app.getSequentialGraphMsgBroker().createGraph(pb), new Dimension(5500, 4500));
                                MySequentialGraphVars.app.setSequentialGrpahViewer(MySequentialGraphVars.app.getSequentialGraphMsgBroker().createSequentialGraphView(layout, new Dimension(5500, 4500)));

                                pb.updateValue(60, 100);
                                MySequentialGraphVars.app.getToolBar().networkBtn.setEnabled(true);
                                MySequentialGraphVars.app.getToolBar().getSearchPathButton().setEnabled(true);
                                MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.removeActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
                                MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.setSelectedIndex(1); // DEFAULT VALUE.
                                MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.addActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
                                //MySequentialGraphVars.getSequentialGraphViewer().vc.vTxtStat.setTextStatistics();

                                pb.updateValue(80, 100);
                                MySequentialGraphVars.app.getSequentialGraphDashboard().setDashboard();
                                // System.out.println(MyVars.inputSequenceFile);

                                pb.updateValue(100, 100);
                                pb.dispose();
                                MySequentialGraphVars.app.revalidate();
                                MySequentialGraphVars.app.repaint();

                                if (MySequentialGraphVars.g.getVertices().size() == 0) {
                                    pb.updateValue(100, 100);
                                    pb.dispose();
                                    MyMessageUtil.showInfoMsg("<html><body>An exception has occurred while creating a network.<br>Please, check the information provided in the configuration panel.</body></html>");
                                } else {
                                    MyMessageUtil.showInfoMsg("Network has successfully been built!");
                                }
                            } catch (Exception ex) {
                                pb.updateValue(100, 100);
                                pb.dispose();
                                MyMessageUtil.showInfoMsg("<html><body>An exception has occurred while creating a network.<br>Please, check the information provided in the configuration panel.</body></html>");
                            }
                        }
                    }).start();
                } catch (Exception ex) {
                        runBtn.setEnabled(true);
                }
            }
        }).start();
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
                //MySequentialGraphVars.app.getToolBar().runBtn.setEnabled(true);
                this.runBtn.setEnabled(true);
                this.runBtn.setBackground(Color.GREEN);
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


