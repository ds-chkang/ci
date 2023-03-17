package datamining.main;

import datamining.config.MySequentialConfigPanel;
import datamining.graph.analysis.MyGraphAnalyzer;
import datamining.graph.layout.MyFRLayout;
import datamining.pattern.MyPatternMiner;
import datamining.pattern.MyPatternMiner2;
import datamining.utils.message.MyMessageUtil;
import datamining.utils.system.MySysUtil;
import datamining.utils.system.MyVars;

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
    private final ImageIcon run_img_icon = new ImageIcon(getClass().getResource(MyVars.imgDir +"run2.png"));
    private final ImageIcon logo_img_icon = new ImageIcon(getClass().getResource(MyVars.imgDir +"thomas2.jpg"));
    private final ImageIcon funnel_img_icon = new ImageIcon(getClass().getResource(MyVars.imgDir +"funnel.png"));
    private final ImageIcon network_img_icon = new ImageIcon(getClass().getResource(MyVars.imgDir +"network.png"));
    private final ImageIcon pathfinder_img_icon = new ImageIcon(getClass().getResource(MyVars.imgDir + "searchPath.png"));

    protected   JComboBox projectMenuComboBox;
    private final String [] projectMenuItems = {"", "SEQUENTIAL NETWORK"};

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
        if (MyVars.isTimeOn) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override public void run() {
                    MyProgressBar pb = new MyProgressBar(false);
                    remove(toolBar);
                    pb.updateValue(10, 100);

                    MyVars.app.getContentTabbedPane().removeAll();
                    pb.updateValue(20, 100);
                    projectMenuComboBox = new JComboBox();
                    projectMenuComboBox.setFont(MyVars.tahomaPlainFont12);
                    projectMenuComboBox.setBackground(Color.LIGHT_GRAY);
                    projectMenuComboBox.setFocusable(false);
                    projectMenuComboBox.setSelectedItem(0);
                    projectMenuComboBox.setPreferredSize(new Dimension(180, 27));
                    for (String menu : projectMenuItems) {projectMenuComboBox.addItem(menu);}
                    pb.updateValue(60, 100);

                    JLabel leftEmptyLabel = new JLabel(" ");
                    leftEmptyLabel.setFont(MyVars.f_bold_12);
                    JLabel rightEmptyLabel = new JLabel("    ");
                    rightEmptyLabel.setFont(MyVars.f_bold_12);

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
                    MyVars.app.getContentTabbedPane().removeAll();

                    projectMenuComboBox = new JComboBox();
                    projectMenuComboBox.setFont(MyVars.tahomaPlainFont12);
                    projectMenuComboBox.setBackground(Color.LIGHT_GRAY);
                    projectMenuComboBox.setFocusable(false);
                    projectMenuComboBox.setSelectedItem(0);
                    projectMenuComboBox.setPreferredSize(new Dimension(180, 27));
                    for (String menu : projectMenuItems) {projectMenuComboBox.addItem(menu);}
                    JLabel leftEmptyLabel = new JLabel(" ");
                    leftEmptyLabel.setFont(MyVars.f_bold_12);
                    JLabel rightEmptyLabel = new JLabel("    ");
                    rightEmptyLabel.setFont(MyVars.f_bold_12);

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
                            MyVars.app.getContentTabbedPane().removeAll();
                            remove(toolBar);
                            pb.updateValue(10, 100);

                            int selectedTabIdx = projectMenuComboBox.getSelectedIndex();
                            pb.updateValue(30, 100);

                            projectMenuComboBox = new JComboBox();
                            projectMenuComboBox.setFont(MyVars.tahomaPlainFont12);
                            projectMenuComboBox.setBackground(Color.LIGHT_GRAY);
                            projectMenuComboBox.setFocusable(false);
                            for (String menu : projectMenuItems) {
                                projectMenuComboBox.addItem(menu);
                            }
                            projectMenuComboBox.setPreferredSize(new Dimension(180, 27));
                            projectMenuComboBox.setSelectedIndex(1);

                            JLabel leftEmptyLabel = new JLabel(" ");
                            leftEmptyLabel.setFont(MyVars.f_bold_12);
                            JLabel rightEmptyLabel = new JLabel("    ");
                            rightEmptyLabel.setFont(MyVars.f_bold_12);

                            toolBar = new JToolBar();
                            toolBar.setFloatable(false);
                            toolBar.setPreferredSize(new Dimension(700, 38));
                            toolBar.add(leftEmptyLabel);
                            toolBar.add(projectMenuComboBox);
                            toolBar.add(rightEmptyLabel);
                            pb.updateValue(60, 100);

                            MySysUtil.resetVariables();
                            if (selectedTabIdx == 0) {

                            } else if (selectedTabIdx == 1) {
                                MySequentialConfigPanel sequentialConfigPanel = new MySequentialConfigPanel();
                                MyVars.app.getMsgBroker().setSequentialConfigPanel(sequentialConfigPanel);
                                if (MyVars.app.getContentTabbedPane().getTabCount() > 0) {
                                    MyVars.app.getContentTabbedPane().removeAll();
                                    runBtn.setEnabled(false);
                                    searchPathBtn.setEnabled(false);
                                    networkBtn.setEnabled(false);
                                }
                                MyVars.app.getContentTabbedPane().addTab("      CONFIGURATION       ", sequentialConfigPanel);
                                MyVars.app.getContentTabbedPane().addTab("      DASHBOARD       ", MyVars.app.resetDashboard());
                            }

                            setButton(runBtn, run_img_icon, "Discover relations", false);
                            setButton(searchPathBtn, pathfinder_img_icon, "Behavioral Pattern Mining", false);
                            setButton(networkBtn, network_img_icon, "Network Analyses", false);

                            add(toolBar, BorderLayout.CENTER);
                            pb.updateValue(80, 100);

                            if (selectedTabIdx == 1) {
                                projectMenuComboBox.setSelectedIndex(1);
                            }
                            setProjectMenuActionListener();
                            pb.updateValue(100, 100);
                            pb.dispose();
                            MyVars.app.revalidate();
                            MyVars.app.repaint();
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
                        try {MySysUtil.resetVariables();
                            if (projectMenuComboBox.getSelectedIndex() == 0) {setInitialToolbar();}
                            else {setToolBar();}
                        } catch (Exception ex) {
                            revalidate();
                            repaint();
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
        this.logo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // add module update calls.
            }
        });
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
        if (projectMenuComboBox.getSelectedIndex() == 1) {
            if (evt.getSource() == runBtn) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override public void run() {
                        new Thread(new Runnable() {
                            @Override public void run() {
                                try {
                                    MyProgressBar pb = new MyProgressBar(false);
                                    runBtn.setEnabled(false);
                                    MyVars.outputDir = MySysUtil.getWorkingDir() + MySysUtil.getDirectorySlash() + "elements" + MySysUtil.getDirectorySlash();
                                    MyVars.app.getMsgBroker().getConfigPanel().getDefaultVariableTable().isTimeVariableOn();
                                    MyVars.app.getMsgBroker().categorize();
                                    pb.updateValue(5, 100);
                                    MyVars.app.getMsgBroker().generateInputFeatures();
                                    pb.updateValue(10, 100);
                                    MyVars.app.getMsgBroker().runEngine();
                                    pb.updateValue(20, 100);
                                    inputBtn.setEnabled(false);
                                    runBtn.setEnabled(false);
                                    new Thread(new Runnable() {
                                        @Override public void run() {
                                            MyVars.app.getContentTabbedPane().setSelectedIndex(1);
                                        }
                                    }).start();
                                    new Thread(new Runnable() {
                                        @Override public void run() {
                                            try {
                                                MyFRLayout layout = new MyFRLayout<>(MyVars.app.getMsgBroker().createGraph(pb), new Dimension(5500, 4500));
                                                MyVars.app.setGrpahViewer(MyVars.app.getMsgBroker().createPlusGraphView(layout, new Dimension(5500, 4500)));
                                                pb.updateValue(60, 100);
                                                searchPathBtn.setEnabled(true);
                                                networkBtn.setEnabled(true);
                                                MyVars.getViewer().vc.edgeValueSelecter.removeActionListener(MyVars.getViewer().vc);
                                                MyVars.getViewer().vc.edgeValueSelecter.setSelectedIndex(1); // DEFAULT VALUE.
                                                MyVars.getViewer().vc.edgeValueSelecter.addActionListener(MyVars.getViewer().vc);
                                                MyVars.getViewer().vc.vTxtStat.setTextStatistics();
                                                pb.updateValue(80, 100);
                                                MyVars.app.getDashboard().setDashboard();
                                                System.out.println(MyVars.inputSequenceFile);
                                                pb.updateValue(100, 100);
                                                pb.dispose();
                                                MyVars.app.revalidate();
                                                MyVars.app.repaint();
                                                if (MyVars.g.getVertices().size() == 0) {
                                                    MyMessageUtil.showInfoMsg("<html><body>An exception has occurred while creating a network.<br>Please, check the information provided in the configuration panel.</body></html>");
                                                } else {
                                                    MyMessageUtil.showInfoMsg("Network has successfully been built!");
                                                }
                                            } catch (Exception ex) {
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
                                MyGraphAnalyzer networkAnalyzer = new MyGraphAnalyzer();
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

    public JButton getSearchPathButton() { return this.searchPathBtn; }

}