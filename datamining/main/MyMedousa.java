package datamining.main;

import datamining.broker.MyMessageBroker;
import datamining.graph.MyDirectMarkovChainDashBoard;
import datamining.graph.MyDirectMarkovChainViewer;
import datamining.utils.security.MyMultipleInstanceRunMonitor;
import datamining.utils.message.MyMessageUtil;
import datamining.utils.system.MyVars;
import datamining.utils.security.MyDateMonitor;

import javax.swing.plaf.ColorUIResource;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MyMedousa
extends JFrame {
    private JDesktopPane deskpane;
    private MyToolBar toolBar;
    private JTabbedPane tabbedPane;
    private MyMessageBroker msgBroker;
    private JPanel tabbedPanePanel;
    public MyMedousa() {
        try {
            MyVars.main = this;
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        deskpane = new JDesktopPane();
                        tabbedPane = new JTabbedPane();
                        toolBar = new MyToolBar();
                        msgBroker = new MyMessageBroker();
                        setCursor(Cursor.HAND_CURSOR);
                        addComponentListener(new ComponentAdapter() {
                            public void componentResized(ComponentEvent evt) {
                                Component c = (Component) evt.getSource();
                            }
                        });
                        MyVars.isAppStarted = true;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

            Thread logoThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        MyLogoPanel.launchLogo(MyVars.main);
                        Thread.sleep(2700);
                        MyLogoPanel.disposeLogo();
                        Thread.sleep(300);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

            t.start();
            logoThread.start();
            t.join();
        } catch (Exception ex) {}
    }

    private void decorate() {
        this.setTitle(MyVars.appTitleMsg);
        this.setLayout(new BorderLayout(0, 0));
        this.tabbedPanePanel = new JPanel();
        this.tabbedPanePanel.setLayout(new BorderLayout(3,3));
        this.tabbedPanePanel.add(this.tabbedPane, BorderLayout.CENTER);
        this.tabbedPane.setFont(MyVars.tahomaBoldFont12);
        this.tabbedPane.setTabPlacement(JTabbedPane.TOP);
        this.tabbedPane.setFocusable(false);
        this.deskpane.setBackground(Color.WHITE);
        this.deskpane.setLayout(new BorderLayout(0, 0));
        this.deskpane.add(this.toolBar, BorderLayout.NORTH);
        this.deskpane.add(this.tabbedPanePanel, BorderLayout.CENTER);
        this.deskpane.add(new MyStatusBar(), BorderLayout.SOUTH);
        this.setPreferredSize(new Dimension(800, 800));

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
            int response = MyMessageUtil.showConfirmMessage(MyVars.stopAppMsg);
            if (response == 1) {System.exit(0);}
            }
        });

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setMinimumSize(new Dimension(800, 650)
        );
        this.setContentPane(this.deskpane);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    public static void setUI() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            UIDefaults defaults = UIManager.getLookAndFeelDefaults();
            ToolTipManager.sharedInstance().setInitialDelay(100);
            ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
            defaults.put("Table.gridColor", Color.decode("#9da4b2"));
            defaults.put("Table.disabled", false);
            defaults.put("Table.showGrid", true);
            defaults.put("Tree.drawHorizontalLines", true);
            defaults.put("JTree.lineStyle", "Angled");
            defaults.put("Tree.drawVerticalLines", true);
            defaults.put("Tree.linesStyle", "dashed");
            defaults.put("ProgressBar[Enabled+Indeterminate].foregroundPainter", new MyIndeterminateProgressBarRegionPainter());
            defaults.put("Table.intercellSpacing", new Dimension(2, 1));
            defaults.put("ScrollBar.thumb", new ColorUIResource(0, 0, 0));
            defaults.put("nimbusOrange", defaults.get("nimbusBase"));
            defaults.put("Slider.thumbHeight", 13); // change height
            defaults.put("Slider.thumbWidth", 13); // change width
            defaults.put("OptionPane.minimumSize", new Dimension(450, 130));
            defaults.put("ToolTip.font", MyVars.f_pln_12);
            defaults.put("OptionPane.okButtonText", "    OK    ");
        } catch (Exception ex) {}
    }

    public static void main(String [] args) throws Exception {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    MyDateMonitor.checkDate();
                    MyMultipleInstanceRunMonitor.monitorInstances();
                    MyMedousa.setUI();
                    MyMedousa medousa = new MyMedousa();
                    medousa.decorate();
                    medousa.setAlwaysOnTop(true);
                    medousa.setVisible(true);
                    medousa.setAlwaysOnTop(false);
                    medousa.revalidate();
                    medousa.repaint();
                } catch (Exception ex) {}
            }
        });

    }

    public void setDirectGraph(JPanel graphContainer) {
        try {
            this.getDirectMarkovChainDashBoard().setTopLevelDashboard((MyDirectMarkovChainViewer) graphContainer);
        } catch (Exception ex) {}
    }

    public JTabbedPane getContentTabbedPane() {return this.tabbedPane;}
    public MyToolBar getToolBar() {return this.toolBar;}
    public MyMessageBroker getMsgBroker() {return this.msgBroker;}
    public MyDirectMarkovChainDashBoard getDirectMarkovChainDashBoard() {return MyVars.directMarkovChainDashBoard;}

    public MyDirectMarkovChainDashBoard resetDirectMarkovChainDashBoard() {
        MyVars.directMarkovChainDashBoard = new MyDirectMarkovChainDashBoard();
        return MyVars.directMarkovChainDashBoard;
    }


}