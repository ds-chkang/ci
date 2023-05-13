package medousa;

import medousa.direct.config.MyDirectGraphConfigPanel;
import medousa.direct.graph.analysis.MyDirectGraphAnalysisDirectGraphAnalyzer;
import medousa.direct.graph.layout.MyDirectGraphFRLayout;
import medousa.message.MyMessageUtil;
import medousa.direct.utils.MyDirectGraphSysUtil;
import medousa.direct.utils.MyDirectGraphVars;
import medousa.sequential.config.MySequentialGraphConfigPanel;
import medousa.sequential.graph.analysis.MyAnalysisGraphApp;
import medousa.sequential.graph.layout.MyFRLayout;
import medousa.sequential.pattern.MyPatternMiner2;
import medousa.sequential.utils.MySequentialGraphSysUtil;
import medousa.sequential.utils.MySequentialGraphVars;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    private  JButton searchPathBtn = new JButton();
    private  JButton nodeSummaryBtn = new JButton();
    public JButton networkBtn = new JButton();
    private final ImageIcon run_img_icon = new ImageIcon(getClass().getResource(MyDirectGraphVars.imgDir +"run2.png"));
    private final ImageIcon logo_img_icon = new ImageIcon(getClass().getResource(MyDirectGraphVars.imgDir +"medousa.jpg"));
    private final ImageIcon funnel_img_icon = new ImageIcon(getClass().getResource(MyDirectGraphVars.imgDir +"searchPath.png"));
    private final ImageIcon network_img_icon = new ImageIcon(getClass().getResource(MyDirectGraphVars.imgDir +"searchPath.png"));
    private final ImageIcon pathfinder_img_icon = new ImageIcon(getClass().getResource(MyDirectGraphVars.imgDir + "network.png"));

    protected   JComboBox projectMenuComboBox;
    private final String [] projectMenuItems = {"", "DIRECT NETWORK", "SEQUENTIAL NETWORK"};

    public MyToolBar() {
        this.decorate();
    }

    private void decorate() {
        try {
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
                    projectMenuComboBox.setPreferredSize(new Dimension(180, 27));
                    for (String menu : projectMenuItems) {projectMenuComboBox.addItem(menu);}
                    pb.updateValue(60, 100);

                    JLabel leftEmptyLabel = new JLabel(" ");
                    leftEmptyLabel.setFont(MyDirectGraphVars.f_bold_12);
                    JLabel rightEmptyLabel = new JLabel("    ");
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
                    JLabel rightEmptyLabel = new JLabel("    ");
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
                            JLabel rightEmptyLabel = new JLabel("    ");
                            rightEmptyLabel.setFont(MyDirectGraphVars.f_bold_12);

                            toolBar = new JToolBar();
                            toolBar.setFloatable(false);
                            toolBar.setPreferredSize(new Dimension(700, 38));
                            toolBar.add(leftEmptyLabel);
                            toolBar.add(projectMenuComboBox);
                            toolBar.add(rightEmptyLabel);
                            pb.updateValue(60, 100);

                            if (selectedTabIdx == 1) {
                                /**if (MyDirectGraphVars.directGraph != null) {
                                    synchronized (MyDirectGraphVars.directGraph) {
                                        int response = MyMessageUtil.showConfirmMessage("Would you like to save the current direct graph?");
                                        if (response == 1) {
                                            MyNetworkSerializer networkSerializer = new MyNetworkSerializer();
                                            networkSerializer.serializeNetworkToFile();
                                        }
                                    }
                                }*/
                                MyDirectGraphSysUtil.initVariables();
                                MyDirectGraphConfigPanel directConfigPanel = new MyDirectGraphConfigPanel();
                                MyDirectGraphVars.app.getDirectGraphMsgBroker().setDirectConfigPanel(directConfigPanel);
                                JScrollPane scrollPane = new JScrollPane(MyDirectGraphVars.app.getDirectGraphMsgBroker().getDirectConfigPanel());
                                if (MyDirectGraphVars.app.getContentTabbedPane().getTabCount() > 0) {
                                    MyDirectGraphVars.app.getContentTabbedPane().removeAll();
                                }
                                MyDirectGraphVars.app.getContentTabbedPane().addTab("      CONFIGURATION       ", scrollPane);
                                MyDirectGraphVars.app.getContentTabbedPane().addTab("      DASHBOARD       ", MyDirectGraphVars.app.resetDirectGraphDashBoard());

                                setButton(runBtn, run_img_icon, "Discover relations", false);
                                setButton(networkBtn, network_img_icon, "Network Analyses", false);
                            } else if (selectedTabIdx == 2) {
                                /**
                                if (MySequentialGraphVars.g != null) {
                                    synchronized (MySequentialGraphVars.g) {
                                        int response = MyMessageUtil.showConfirmMessage("Would you like to save the current sequential graph?");
                                        if (response == 1) {
                                            MyNetworkSerializer networkSerializer = new MyNetworkSerializer();
                                            networkSerializer.serializeNetworkToFile();
                                        }
                                        MySequentialGraphSysUtil.initVariables();
                                    }
                                }*/
                                MySequentialGraphSysUtil.initVariables();
                                MySequentialGraphConfigPanel sequentialGraphConfigPanel = new MySequentialGraphConfigPanel();
                                MySequentialGraphVars.app.getSequentialGraphMsgBroker().setSequentialConfigPanel(sequentialGraphConfigPanel);
                                if (MySequentialGraphVars.app.getContentTabbedPane().getTabCount() > 0) {
                                    MySequentialGraphVars.app.getContentTabbedPane().removeAll();
                                    runBtn.setEnabled(false);
                                    searchPathBtn.setEnabled(false);
                                    networkBtn.setEnabled(false);
                                }
                                MySequentialGraphVars.app.getContentTabbedPane().addTab("      CONFIGURATION       ", sequentialGraphConfigPanel);
                                MySequentialGraphVars.app.getContentTabbedPane().addTab("      DASHBOARD       ", MySequentialGraphVars.app.resetSequentialGraphDashboard());


                                setButton(runBtn, run_img_icon, "Discover relations", false);
                                setButton(searchPathBtn, pathfinder_img_icon, "Behavioral Pattern Mining", false);
                                setButton(networkBtn, network_img_icon, "Network Analyses", false);
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
                            if (projectMenuComboBox.getSelectedIndex() == 0) {setInitialToolbar();}
                            else {setToolBar();}
                        } catch (Exception ex) {
                        }
                    }
                }).start();
            }
        });
    }

    private void setLogoToolBar() {
        this.logoToolBar = new JToolBar();
        //this.logoToolBar.setBorder(BorderFactory.createRaisedBevelBorder());
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
                if (evt.getSource() == runBtn) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override public void run() {
                            new Thread(new Runnable() {
                                @Override public void run() {
                                    try {
                                        MyProgressBar pb = new MyProgressBar(false);
                                        runBtn.setEnabled(false);
                                        MySequentialGraphVars.outputDir = MySequentialGraphSysUtil.getWorkingDir() + MySequentialGraphSysUtil.getDirectorySlash() + "elements" + MySequentialGraphSysUtil.getDirectorySlash();
                                        MySequentialGraphVars.app.getSequentialGraphMsgBroker().getConfigPanel().getDefaultVariableTable().isTimeVariableOn();
                                        MySequentialGraphVars.app.getSequentialGraphMsgBroker().categorize();
                                        pb.updateValue(5, 100);
                                        MySequentialGraphVars.app.getSequentialGraphMsgBroker().generateInputFeatures();
                                        pb.updateValue(10, 100);
                                        MySequentialGraphVars.app.getSequentialGraphMsgBroker().runEngine();
                                        pb.updateValue(20, 100);
                                        inputBtn.setEnabled(false);
                                        runBtn.setEnabled(false);
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
                                                    searchPathBtn.setEnabled(true);
                                                    networkBtn.setEnabled(true);
                                                    MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.removeActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
                                                    MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.setSelectedIndex(1); // DEFAULT VALUE.
                                                    MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.addActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
                                                    MySequentialGraphVars.getSequentialGraphViewer().vc.vTxtStat.setTextStatistics();

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
                    });
                } else if (evt.getSource() == networkBtn) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override public void run() {
                            new Thread(new Runnable() {
                                @Override public void run() {
                                    MyAnalysisGraphApp networkAnalyzer = new MyAnalysisGraphApp();
                                    networkAnalyzer.setAlwaysOnTop(false);
                                    // networkBtn.setEnabled(false);

                                }
                            }).start();
                        }
                    });
                } else if (evt.getSource() == searchPathBtn) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override public void run() {
                            new Thread(new Runnable() {
                                @Override public void run() {
                                    MyPatternMiner2 pm = new MyPatternMiner2();
                                }
                            }).start();
                        }
                    });
                }
            }
        }
    }

    public JButton getSearchPathButton() { return this.searchPathBtn; }

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