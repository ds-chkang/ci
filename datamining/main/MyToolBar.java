package datamining.main;

import datamining.config.MyDirectConfigPanel;
import datamining.graph.analysis.MyGraphAnalyzer;
import datamining.graph.layout.MyFRLayout;
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
    private final ImageIcon pathfinder_img_icon = new ImageIcon(getClass().getResource(MyVars.imgDir + "network.png"));

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
        if (MyVars.isTimeOn) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override public void run() {
                    MyProgressBar pb = new MyProgressBar(false);
                    remove(toolBar);
                    pb.updateValue(10, 100);

                    MyVars.main.getContentTabbedPane().removeAll();
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
                    MyVars.main.getContentTabbedPane().removeAll();

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
                            MyVars.main.getContentTabbedPane().removeAll();
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
                            if (selectedTabIdx == 1) {
                                MyDirectConfigPanel directConfigPanel = new MyDirectConfigPanel();
                                MyVars.main.getMsgBroker().setDirectConfigPanel(directConfigPanel);
                                JScrollPane scrollPane = new JScrollPane(MyVars.main.getMsgBroker().getDirectConfigPanel());
                                if (MyVars.main.getContentTabbedPane().getTabCount() > 0) {
                                    MyVars.main.getContentTabbedPane().removeAll();
                                }
                                MyVars.main.getContentTabbedPane().addTab("      CONFIGURATION       ", scrollPane);
                                MyVars.main.getContentTabbedPane().addTab("      DASHBOARD       ", MyVars.main.resetDirectMarkovChainDashBoard());
                            } else if (selectedTabIdx == 2) {

                            }

                            setButton(runBtn, run_img_icon, "Discover relations", false);
                            setButton(networkBtn, network_img_icon, "Network Analyses", false);

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
                            MyVars.main.revalidate();
                            MyVars.main.repaint();
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
        if (evt.getSource() instanceof JButton) {
            if (projectMenuComboBox.getSelectedIndex() == 1) {
                MyVars.isDirectMarkovRelation = true;
                this.doDirectRelationOperations(evt);
            }// else if (projectMenuComboBox.getSelectedIndex() == 2) {
              //  MyVars.isDirectMarkovRelation = false;
                //this.doSequentialPlusRelationOperations(evt);
           // }
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
                                if (MyVars.main.getMsgBroker().getInputFiles() == null) {
                                    MyMessageUtil.showErrorMsg("Please, set input data files!");
                                    return;
                                }
                                MyProgressBar pb = new MyProgressBar(false);
                                MyVars.main.getMsgBroker().categorize();
                                pb.updateValue(20, 100);
                                Thread.sleep(200);
                                pb.updateValue(50, 100);
                                MyFRLayout layout = new MyFRLayout<>(MyVars.main.getMsgBroker().createDirectGraph(), new Dimension(6000, 5500));
                                MyVars.main.setDirectGraph(MyVars.main.getMsgBroker().createDirectGraphView(layout, new Dimension(6000, 5500)));
                                MyVars.getDirectGraphViewer().layout = layout;
                                MyVars.main.getContentTabbedPane().setSelectedIndex(1);
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
                            MyGraphAnalyzer networkAnalyzer = new MyGraphAnalyzer();
                            networkAnalyzer.setAlwaysOnTop(false);
                          //  networkBtn.setEnabled(false);
                        }
                    }).start();
                }
            }
        });
    }
}