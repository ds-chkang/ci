package medousa;

import medousa.direct.config.MyDirectGraphConfigPanel;
import medousa.direct.graph.analysis.MyDirectGraphAnalysisDirectGraphAnalyzer;
import medousa.direct.graph.layout.MyDirectGraphFRLayout;
import medousa.message.MyMessageUtil;
import medousa.direct.utils.MyDirectGraphSysUtil;
import medousa.direct.utils.MyDirectGraphVars;
import medousa.sequential.config.MySequentialGraphConfigPanel;
import medousa.sequential.graph.clustering.MyClusteringConfig;
import medousa.sequential.graph.funnel.MyAnalysisGraphApp;
import medousa.sequential.graph.layout.MyFRLayout;
import medousa.sequential.utils.MySequentialGraphSysUtil;
import medousa.sequential.utils.MySequentialGraphVars;
import medousa.sequential.utils.MyViewerComponentControllerUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;

public class MyToolBar
extends JPanel
implements ActionListener {

    private JToolBar toolBar = new JToolBar();
    private JToolBar logoToolBar = new JToolBar();
    private JButton logo = new JButton();
    private  JButton headerBtn = new JButton();
    private  JButton inputBtn = new JButton();
    public  JButton runBtn = new JButton();
    public  JButton funnelBtn = new JButton();
    private  JButton searchSequentialPatternBtn = new JButton();
    public  JButton clusteringBtn = new JButton();
    public JButton networkBtn = new JButton();
    private final ImageIcon run_img_icon = new ImageIcon(getClass().getResource(MyDirectGraphVars.imgDir +"run.png"));
    private final ImageIcon header_img_icon = new ImageIcon(getClass().getResource(MyDirectGraphVars.imgDir +"header.png"));
    private final ImageIcon input_img_icon = new ImageIcon(getClass().getResource(MyDirectGraphVars.imgDir +"input.png"));
    private final ImageIcon logo_img_icon = new ImageIcon(getClass().getResource(MyDirectGraphVars.imgDir +"medousa.jpg"));
    private final ImageIcon funnel_img_icon = new ImageIcon(getClass().getResource(MyDirectGraphVars.imgDir +"simulation.png"));
    private final ImageIcon clustering_img_icon = new ImageIcon(getClass().getResource(MyDirectGraphVars.imgDir + "clustering.png"));
    private final ImageIcon network_img_icon = new ImageIcon(getClass().getResource(MyDirectGraphVars.imgDir + "network.png"));

    public JComboBox distributionSelecter = new JComboBox();

    protected   JComboBox projectMenuComboBox;
    private final String [] projectMenuItems = {"", "DIRECT NETWORK", "COMPLEX NETWORK"};

    public MyToolBar() {
        this.decorate();
    }

    private void decorate() {
        try {
            distributionSelecter.setVisible(false);
            distributionSelecter.setPreferredSize(new Dimension(180, 26));
            this.setLayout(new BorderLayout(0, 0));
            this.setInitialToolbar();
            this.setLogoToolBar();
        } catch (Exception ex) {}
    }

    private void setInitialToolbar() {
        if (MyDirectGraphVars.isTimeOn) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override public void run() {
                    MyProgressBar pb = new MyProgressBar(false);
                    remove(toolBar);
                    pb.updateValue(10, 100);

                    MyDirectGraphVars.app.getContentTabbedPane().removeAll();
                    pb.updateValue(20, 100);
                    projectMenuComboBox = new JComboBox();
                    projectMenuComboBox.setFont(MyDirectGraphVars.tahomaPlainFont12);
                    projectMenuComboBox.setBackground(Color.LIGHT_GRAY);
                    projectMenuComboBox.setFocusable(false);
                    projectMenuComboBox.setSelectedItem(0);
                    projectMenuComboBox.setPreferredSize(new Dimension(180, 26));
                    for (String menu : projectMenuItems) {projectMenuComboBox.addItem(menu);}
                    pb.updateValue(60, 100);

                    JLabel leftEmptyLabel = new JLabel(" ");
                    leftEmptyLabel.setFont(MyDirectGraphVars.f_bold_12);
                    JLabel rightEmptyLabel = new JLabel("     ");
                    rightEmptyLabel.setFont(MyDirectGraphVars.f_bold_12);

                    toolBar = new JToolBar();
                    toolBar.setFloatable(false);
                    toolBar.setPreferredSize(new Dimension(700, 38));
                    toolBar.add(leftEmptyLabel);
                    toolBar.add(projectMenuComboBox);
                    toolBar.add(rightEmptyLabel);
                    add(toolBar, BorderLayout.CENTER);
                    setProjectMenuActionListener();
                    pb.updateValue(100, 100);
                    pb.dispose();
                }
            });
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                @Override public void run() {
                    remove(toolBar);
                    MyDirectGraphVars.app.getContentTabbedPane().removeAll();

                    projectMenuComboBox = new JComboBox();
                    projectMenuComboBox.setFont(MyDirectGraphVars.tahomaPlainFont12);
                    projectMenuComboBox.setBackground(Color.LIGHT_GRAY);
                    projectMenuComboBox.setFocusable(false);
                    projectMenuComboBox.setSelectedItem(0);
                    projectMenuComboBox.setPreferredSize(new Dimension(180, 27));
                    for (String menu : projectMenuItems) {projectMenuComboBox.addItem(menu);}
                    JLabel leftEmptyLabel = new JLabel(" ");
                    leftEmptyLabel.setFont(MyDirectGraphVars.f_bold_12);
                    JLabel rightEmptyLabel = new JLabel("     ");
                    rightEmptyLabel.setFont(MyDirectGraphVars.f_bold_12);

                    toolBar = new JToolBar();
                    toolBar.setFloatable(false);
                    toolBar.setPreferredSize(new Dimension(700, 38));
                    toolBar.add(leftEmptyLabel);
                    toolBar.add(projectMenuComboBox);
                    toolBar.add(rightEmptyLabel);
                    add(toolBar, BorderLayout.CENTER);
                    setProjectMenuActionListener();
                }
            });
        }
    }

    private void setToolBar() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                new Thread(new Runnable() {
                    @Override public void run() {
                        try {
                            MyProgressBar pb = new MyProgressBar(false);
                            MyDirectGraphVars.app.getContentTabbedPane().removeAll();
                            remove(toolBar);
                            pb.updateValue(10, 100);

                            int selectedTabIdx = projectMenuComboBox.getSelectedIndex();
                            pb.updateValue(30, 100);

                            projectMenuComboBox = new JComboBox();
                            projectMenuComboBox.setFont(MyDirectGraphVars.tahomaPlainFont12);
                            projectMenuComboBox.setBackground(Color.LIGHT_GRAY);
                            projectMenuComboBox.setFocusable(false);
                            for (String menu : projectMenuItems) {
                                projectMenuComboBox.addItem(menu);
                            }
                            projectMenuComboBox.setPreferredSize(new Dimension(180, 27));
                            projectMenuComboBox.setSelectedIndex(1);

                            JLabel leftEmptyLabel = new JLabel(" ");
                            leftEmptyLabel.setFont(MyDirectGraphVars.f_bold_12);
                            JLabel rightEmptyLabel = new JLabel("     ");
                            rightEmptyLabel.setFont(MyDirectGraphVars.f_bold_12);

                            toolBar = new JToolBar();
                            toolBar.setFloatable(false);
                            toolBar.setPreferredSize(new Dimension(700, 38));
                            toolBar.add(leftEmptyLabel);
                            toolBar.add(projectMenuComboBox);
                            toolBar.add(rightEmptyLabel);
                            pb.updateValue(60, 100);

                            if (selectedTabIdx == 1) {
                                MyDirectGraphSysUtil.initVariables();
                                MyDirectGraphConfigPanel directConfigPanel = new MyDirectGraphConfigPanel();
                                MyDirectGraphVars.app.getDirectGraphMsgBroker().setDirectConfigPanel(directConfigPanel);
                                JScrollPane scrollPane = new JScrollPane(MyDirectGraphVars.app.getDirectGraphMsgBroker().getDirectConfigPanel());
                                if (MyDirectGraphVars.app.getContentTabbedPane().getTabCount() > 0) {
                                    MyDirectGraphVars.app.getContentTabbedPane().removeAll();
                                }
                                MyDirectGraphVars.app.getContentTabbedPane().addTab("      CONFIGURATION       ", scrollPane);
                                MyDirectGraphVars.app.getContentTabbedPane().addTab("      DASHBOARD       ", MyDirectGraphVars.app.resetDirectGraphDashBoard());

                                runBtn.setVisible(true);
                                setButton(runBtn, run_img_icon, "CREATE NETWORK", false);
                            } else if (selectedTabIdx == 2) {
                                MySequentialGraphSysUtil.initVariables();
                                MySequentialGraphConfigPanel sequentialGraphConfigPanel = new MySequentialGraphConfigPanel();
                                MySequentialGraphVars.app.getSequentialGraphMsgBroker().setSequentialConfigPanel(sequentialGraphConfigPanel);

                                MySequentialGraphVars.app.getContentTabbedPane().addTab("      CONFIGURATION       ", sequentialGraphConfigPanel);
                                MySequentialGraphVars.app.getContentTabbedPane().addTab("      DASHBOARD       ", MySequentialGraphVars.app.resetSequentialGraphDashboard());

                                setButton(headerBtn, header_img_icon, "Set header", true);
                                setButton(inputBtn, input_img_icon, "FUNNEL EXPLORER", false);
                                setButton(runBtn, run_img_icon, "CREATE NETWORK", false);
                                setButton(funnelBtn, funnel_img_icon, "Funnel Analyses", false);
                                setButton(clusteringBtn, clustering_img_icon, "Clustering Analysis", false);

                                headerBtn.setEnabled(true);
                                headerBtn.setVisible(true);
                                inputBtn.setVisible(true);
                                runBtn.setVisible(true);

                                clusteringBtn.setVisible(false);
                                funnelBtn.setVisible(false);
                            }

                            add(toolBar, BorderLayout.CENTER);
                            pb.updateValue(80, 100);

                            if (selectedTabIdx == 1) {
                                projectMenuComboBox.setSelectedIndex(1);
                            } else if (selectedTabIdx == 2) {
                                projectMenuComboBox.setSelectedIndex(2);
                            }

                            setProjectMenuActionListener();
                            pb.updateValue(100, 100);
                            pb.dispose();
                            MyDirectGraphVars.app.revalidate();
                            MyDirectGraphVars.app.repaint();
                        } catch (Exception ex) {}
                    }
                }).start();
            }
        });
    }


    private void setProjectMenuActionListener() {
        this.projectMenuComboBox.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        try {
                            if (projectMenuComboBox.getSelectedIndex() == 0) {
                                setInitialToolbar();
                            } else {setToolBar();}
                        } catch (Exception ex) {}
                    }
                }).start();
            }
        });
    }

    private void setLogoToolBar() {
        this.logoToolBar = new JToolBar();
        this.logoToolBar.setBackground(Color.decode("#D6D9DF"));
        this.logoToolBar.setLayout(new BorderLayout(0,0));
        this.logoToolBar.setPreferredSize(new Dimension(169, 28));
        this.logoToolBar.setFloatable(false);
        this.logo.setIcon(this.logo_img_icon);
        this.logo.setBorder(BorderFactory.createRaisedBevelBorder());
        this.logo.setFocusable(false);
        this.logo.setBackground(Color.decode("#D6D9DF"));
        this.logoToolBar.add(this.logo, BorderLayout.CENTER);
        this.add(this.logoToolBar, BorderLayout.EAST);
    }

    private void setButton(JButton toolBarBtn, ImageIcon imgIcon, String toolTip, boolean enable) {
        toolBarBtn.removeActionListener(this);
        toolBarBtn.addActionListener(this);
        toolBarBtn.setToolTipText(toolTip);
        toolBarBtn.setEnabled(enable);
        toolBarBtn.setIcon(imgIcon);
        toolBarBtn.setFocusable(false);
        this.toolBar.add(toolBarBtn);
    }

    @Override public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() instanceof JButton) {
            if (projectMenuComboBox.getSelectedIndex() == 1) {
                MyDirectGraphVars.isDirectMarkovRelation = true;
                this.doDirectRelationOperations(evt);
            } else if (projectMenuComboBox.getSelectedIndex() == 2) {
                if (evt.getSource() == headerBtn) {
                    new Thread(new Runnable() {
                        @Override public void run() {
                            try {
                                JFileChooser fc = new JFileChooser();
                                fc.setFocusable(false);
                                fc.setFont(MySequentialGraphVars.f_pln_12);
                                fc.setPreferredSize(new Dimension(600, 460));
                                fc.setMultiSelectionEnabled(false);
                                fc.showOpenDialog(MySequentialGraphVars.app);

                                if (fc.getSelectedFile() != null) {
                                    String [] headers = MySequentialGraphVars.app.getSequentialGraphMsgBroker().loadHeader(fc.getSelectedFile());
                                    if (headers == null) {
                                        MyMessageUtil.showErrorMsg( "Please, check the format of the provided header file.");
                                        return;
                                    }
                                    BufferedReader br = new BufferedReader(new FileReader(fc.getSelectedFile()));
                                    String line = br.readLine();
                                    headers = line.split(MySequentialGraphVars.commaDelimeter);
                                    for (int i=0; i < headers.length; i++) {
                                        headers[i] = " " + headers[i].toUpperCase();
                                    }

                                    MySequentialGraphVars.app.getSequentialGraphMsgBroker().getConfigPanel().getDefaultVariableTable().updateTable(headers, true);
                                    MySequentialGraphVars.app.getSequentialGraphMsgBroker().getConfigPanel().getSupplimentaryVariableTable().update(headers, true);
                                    MySequentialGraphVars.app.getSequentialGraphMsgBroker().getConfigPanel().getEdgeValueTable().update(headers, true);
                                    MySequentialGraphVars.app.getSequentialGraphMsgBroker().getConfigPanel().getEdgeLabelTable().update(headers, true);
                                    MySequentialGraphVars.app.getSequentialGraphMsgBroker().getConfigPanel().getNodeValueTable().update(headers, true);
                                    MySequentialGraphVars.app.getSequentialGraphMsgBroker().getConfigPanel().getNodeLabelTable().update(headers, true);
                                    inputBtn.setEnabled(true);
                                } else {
                                    MyMessageUtil.showErrorMsg("Failed to header!");
                                }
                            } catch (Exception ex) {ex.printStackTrace();}
                        }
                    }).start();
                } else if (evt.getSource() == inputBtn) {
                    new Thread(new Runnable() {
                        @Override public void run() {
                            try {
                                JFileChooser fc = new JFileChooser();
                                fc.setFocusable(false);
                                fc.setFont(MySequentialGraphVars.f_pln_12);
                                fc.setPreferredSize(new Dimension(600, 460));
                                fc.setMultiSelectionEnabled(true);
                                fc.showOpenDialog(MySequentialGraphVars.app);
                                if (fc.getSelectedFile() != null) {
                                    MySequentialGraphVars.app.getSequentialGraphMsgBroker().setInputFiles(fc.getSelectedFiles());
                                    MyMessageUtil.showInfoMsg("Data have been successfully loaded!");
                                    inputBtn.setBackground(Color.WHITE);
                                    runBtn.setEnabled(true);
                                    runBtn.setBackground(Color.GREEN);
                                } else {
                                    inputBtn.setEnabled(true);
                                    MyMessageUtil.showErrorMsg("Failed to load data file!");
                                }
                            } catch (Exception ex) {
                                inputBtn.setEnabled(true);
                                ex.printStackTrace();
                            }
                        }
                    }).start();
                } else if (evt.getSource() == runBtn) {
                    new Thread(new Runnable() {
                        @Override public void run() {
                            MyProgressBar pb = new MyProgressBar(false);
                            try {
                                inputBtn.setVisible(false);
                                runBtn.setVisible(false);
                                MySequentialGraphVars.outputDir = MySequentialGraphSysUtil.getWorkingDir() + MySequentialGraphSysUtil.getDirectorySlash() + "elements" + MySequentialGraphSysUtil.getDirectorySlash();
                                MySequentialGraphVars.app.getSequentialGraphMsgBroker().getConfigPanel().getDefaultVariableTable().isTimeVariableOn();
                                MySequentialGraphVars.app.getSequentialGraphMsgBroker().categorize();
                                pb.updateValue(5, 100);
                                MySequentialGraphVars.app.getSequentialGraphMsgBroker().generateInputFeatures();
                                pb.updateValue(10, 100);
                                MySequentialGraphVars.app.getSequentialGraphMsgBroker().runEngine();

                                new Thread(new Runnable() {
                                    @Override public void run() {
                                        MySequentialGraphVars.app.getContentTabbedPane().setSelectedIndex(1);
                                    }
                                }).start();

                                new Thread(new Runnable() {
                                    @Override public void run() {
                                        try {
                                            headerBtn.setVisible(false);
                                            inputBtn.setVisible(false);
                                            runBtn.setVisible(false);

                                            MyFRLayout layout = new MyFRLayout<>(MySequentialGraphVars.app.getSequentialGraphMsgBroker().createGraph(), new Dimension(5500, 4500));
                                            MySequentialGraphVars.app.setSequentialGrpahViewer(MySequentialGraphVars.app.getSequentialGraphMsgBroker().createSequentialGraphView(layout, new Dimension(5500, 4500)));

                                            pb.updateValue(60, 100);
                                            funnelBtn.setEnabled(true);
                                            funnelBtn.setVisible(true);
                                            clusteringBtn.setEnabled(true);
                                            clusteringBtn.setVisible(true);

                                            JLabel emptyLabel = new JLabel("        ");
                                            emptyLabel.setFont(MySequentialGraphVars.tahomaBoldFont12);
                                            toolBar.add(emptyLabel);
                                            toolBar.add(distributionSelecter);
                                            distributionSelecter.setVisible(true);
                                            projectMenuComboBox.setBackground(Color.GRAY);
                                            //networkBtn.setEnabled(true);
                                            //networkBtn.setVisible(true);
                                            MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.removeActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
                                            MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.setSelectedIndex(1); // DEFAULT VALUE.
                                            MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.addActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);

                                            pb.updateValue(80, 100);
                                            MySequentialGraphVars.app.getSequentialGraphDashboard().setDashboard();

                                            pb.updateValue(100, 100);
                                            pb.dispose();
                                            MySequentialGraphVars.app.revalidate();
                                            MySequentialGraphVars.app.repaint();

                                            if (MySequentialGraphVars.g.getVertices().size() == 0) {
                                                pb.updateValue(100, 100);
                                                pb.dispose();
                                                funnelBtn.setVisible(false);
                                                clusteringBtn.setVisible(false);
                                                networkBtn.setVisible(false);
                                                MyMessageUtil.showInfoMsg("<html><body>An exception has occurred while creating a network.<br>Please, check the information provided in the configuration panel.</body></html>");
                                            } else {
                                                MyMessageUtil.showInfoMsg("Network has successfully been built!");
                                            }
                                        } catch (Exception ex) {
                                            pb.updateValue(100, 100);
                                            pb.dispose();

                                            headerBtn.setVisible(true);
                                            inputBtn.setVisible(true);
                                            runBtn.setVisible(true);

                                            headerBtn.setEnabled(true);
                                            inputBtn.setEnabled(false);
                                            runBtn.setEnabled(false);

                                            funnelBtn.setVisible(false);
                                            clusteringBtn.setVisible(false);
                                            //networkBtn.setEnabled(false);
                                            networkBtn.setVisible(false);
                                            MyMessageUtil.showInfoMsg("<html><body>An exception has occurred while creating a network.<br>Please, check the information provided in the configuration panel.</body></html>");
                                        } finally {

                                        }
                                    }
                                }).start();
                            } catch (Exception ex) {
                                pb.updateValue(100, 100);
                                pb.dispose();
                                MyMessageUtil.showInfoMsg("<html><body>An exception has occurred while creating a network.<br>Please, check the information provided in the configuration panel.</body></html>");

                            }
                        }
                    }).start();
                } else if (evt.getSource() == funnelBtn) {
                    new Thread(new Runnable() {
                        @Override public void run() {
                            MyAnalysisGraphApp networkAnalyzer = new MyAnalysisGraphApp();
                            networkAnalyzer.setAlwaysOnTop(false);
                        }
                    }).start();
                } else if (evt.getSource() == clusteringBtn) {
                    new Thread(new Runnable() {
                        @Override public void run() {
                            MyViewerComponentControllerUtil.setDefaultViewerLook();
                        }
                    }).start();

                    new Thread(new Runnable() {
                        @Override public void run() {
                            MyClusteringConfig clusteringConfig = new MyClusteringConfig();
                        }
                    }).start();
                }
            }
        }
    }

    public JButton getSearchPathButton() { return this.searchSequentialPatternBtn; }

    private void doDirectRelationOperations(ActionEvent evt) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                if (evt.getSource() == runBtn) {
                    new Thread(new Runnable() {
                        @Override public void run() {
                            try {
                                if (MyDirectGraphVars.app.getDirectGraphMsgBroker().getInputFiles() == null) {
                                    MyMessageUtil.showErrorMsg("Please, set input data files!");
                                    return;
                                }
                                MyProgressBar pb = new MyProgressBar(false);
                                MyDirectGraphVars.app.getDirectGraphMsgBroker().categorize();
                                pb.updateValue(20, 100);
                                Thread.sleep(200);
                                pb.updateValue(50, 100);
                                MyDirectGraphFRLayout layout = new MyDirectGraphFRLayout<>(MyDirectGraphVars.app.getDirectGraphMsgBroker().createDirectGraph(), new Dimension(6000, 5500));
                                MyDirectGraphVars.app.setDirectGraph(MyDirectGraphVars.app.getDirectGraphMsgBroker().createDirectGraphView(layout, new Dimension(6000, 5500)));
                                MyDirectGraphVars.getDirectGraphViewer().layout = layout;
                                MyDirectGraphVars.app.getContentTabbedPane().setSelectedIndex(1);
                                pb.updateValue(80, 100);
                                networkBtn.setEnabled(true);
                                pb.updateValue(80, 100);
                                Thread.sleep(300);
                                runBtn.setEnabled(false);
                                runBtn.setVisible(false);
                                pb.updateValue(100, 100);
                                pb.dispose();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }).start();
                } else if (evt.getSource() == networkBtn) {
                    new Thread(new Runnable() {
                        @Override public void run() {
                            MyDirectGraphAnalysisDirectGraphAnalyzer networkAnalyzer = new MyDirectGraphAnalysisDirectGraphAnalyzer();
                            networkAnalyzer.setAlwaysOnTop(false);
                          //  networkBtn.setEnabled(false);
                        }
                    }).start();
                }
            }
        });
    }
}