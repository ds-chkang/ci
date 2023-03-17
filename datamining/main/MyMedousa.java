package datamining.main;

import datamining.broker.MyMessageBroker;
import datamining.graph.MyDashBoard;
import datamining.graph.stats.MyGraphLevelNodeValueBarChart;
import datamining.graph.stats.depthnode.MyDepthLevelNeighborNodeValueBarChart;
import datamining.graph.stats.MyGraphLevelEdgeValueBarChart;
import datamining.graph.stats.multinode.MyMultiLevelEdgeValueBarChart;
import datamining.utils.MyNodeUtil;
import datamining.utils.MyViewerControlComponentUtil;
import datamining.utils.security.MyMultipleInstanceRunMonitor;
import datamining.utils.message.MyMessageUtil;
import datamining.utils.system.MyVars;
import datamining.utils.security.MyDateMonitor;

import javax.swing.plaf.ColorUIResource;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MyMedousa
extends JFrame
implements ComponentListener {
    private JDesktopPane deskpane;
    private MyToolBar toolBar;
    private JTabbedPane tabbedPane;
    private MyMessageBroker msgBroker;
    private JPanel tabbedPanePanel;
    public MyMedousa() {
        try {
            MyVars.app = this;
            MyVars.app.addComponentListener(this);
            Thread t = new Thread(new Runnable() {
                @Override public void run() {
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
                @Override public void run() {
                    try {
                        MyLogoPanel.launchLogo(MyVars.app);
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
            @Override public void windowClosing(WindowEvent e) {
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

    public static void main(String [] args) throws Exception {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                try {
                    MyDateMonitor.checkDate();
                    MyMultipleInstanceRunMonitor.monitorInstances();
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

    public void setGrpahViewer(JPanel graphContainer) {
        try {
            MyNodeUtil.setContributionToNodes();
            MyVars.getViewer().vc.setGraphPanel(graphContainer);
        } catch (Exception ex) {}
    }

    public JPanel getGraphViewerPanel() { return MyVars.getViewer().vc.getNetworkChart(); }
    public JTabbedPane getContentTabbedPane() {return this.tabbedPane;}
    public MyToolBar getToolBar() {return this.toolBar;}
    public MyMessageBroker getMsgBroker() {return this.msgBroker;}
    public MyDashBoard getDashboard() {return MyVars.dashBoard;}

    public MyDashBoard resetDashboard() {
        MyVars.dashBoard = new MyDashBoard();
        return MyVars.dashBoard;
    }

    private synchronized void updateSplitPaneDividerLocations() {
        if (MyVars.getViewer() != null && MyVars.app.getWidth() >= 1200) {
            if (MyVars.getViewer().vc.nodeValueBarChart.isSelected()) {
                MyViewerControlComponentUtil.removeBarChartsFromViewer();
                MyViewerControlComponentUtil.removeEdgeValueBarChartFromViewer();
                if (MyVars.getViewer().vc.depthSelecter.getSelectedIndex() > 0) {
                    if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() < 2) {
                        MyViewerControlComponentUtil.removeEdgeValueBarChartFromViewer();
                        return;
                    }
                    if (MyVars.getViewer().vc.depthNeighborNodeTypeSelector.getSelectedItem().toString().contains("P.")) {
                        if (MyVars.getViewer().vc.nodeValueBarChart.isSelected()) {
                            MyVars.getViewer().remove(MyVars.getViewer().depthNodeLevelNodeValueBarChart);
                            MyVars.getViewer().depthNodeLevelNeighborNodeValueBarChart = new MyDepthLevelNeighborNodeValueBarChart();
                            MyVars.getViewer().depthNodeLevelNeighborNodeValueBarChart.setPredecessorValueBarChartsForDepthNodes();
                            MyVars.getViewer().add(MyVars.getViewer().depthNodeLevelNeighborNodeValueBarChart);
                        }
                    } else if (MyVars.getViewer().vc.depthNeighborNodeTypeSelector.getSelectedItem().toString().contains("S.")) {
                        if (MyVars.getViewer().vc.nodeValueBarChart.isSelected()) {
                            MyVars.getViewer().remove(MyVars.getViewer().depthNodeLevelNodeValueBarChart);
                            MyVars.getViewer().depthNodeLevelNeighborNodeValueBarChart = new MyDepthLevelNeighborNodeValueBarChart();
                            MyVars.getViewer().depthNodeLevelNeighborNodeValueBarChart.setSuccessorValueBarChartsForDepthNodes();
                            MyVars.getViewer().add(MyVars.getViewer().depthNodeLevelNeighborNodeValueBarChart);
                        }
                    }
                } else if (MyVars.getViewer().multiNodes != null) {
                    MyViewerControlComponentUtil.setSharedNodeLevelNodeValueBarChart();
                } else if (MyVars.getViewer().selectedNode != null) {
                    MyViewerControlComponentUtil.setSelectedNodeNeighborValueBarChartToViewer();
                } else {
                    MyVars.getViewer().graphLevelNodeValueBarChart = new MyGraphLevelNodeValueBarChart();
                    MyVars.getViewer().add(MyVars.getViewer().graphLevelNodeValueBarChart);
                }
            } else {
                MyViewerControlComponentUtil.removeBarChartsFromViewer();
            }

            if (MyVars.getViewer().vc.edgeValueBarChart.isSelected()) {
                if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() > 1) {
                    if (MyVars.getViewer().graphLelvelEdgeValueBarChart != null) {
                        MyVars.getViewer().remove(MyVars.getViewer().graphLelvelEdgeValueBarChart);
                        MyVars.getViewer().graphLelvelEdgeValueBarChart = new MyGraphLevelEdgeValueBarChart();
                        MyVars.getViewer().graphLelvelEdgeValueBarChart.setEdgeValueBarChart();
                        MyVars.getViewer().add(MyVars.getViewer().graphLelvelEdgeValueBarChart);
                    }

                    if (MyVars.getViewer().multiNodeLevelEdgeValueBarChart != null) {
                        MyVars.getViewer().remove(MyVars.getViewer().multiNodeLevelEdgeValueBarChart);
                        MyVars.getViewer().multiNodeLevelEdgeValueBarChart = new MyMultiLevelEdgeValueBarChart();
                        MyVars.getViewer().multiNodeLevelEdgeValueBarChart.setMultiNodeSharedEdgeValueRankBarChart();
                        MyVars.getViewer().add(MyVars.getViewer().multiNodeLevelEdgeValueBarChart);
                    }
                }
            } else {
                MyViewerControlComponentUtil.removeEdgeValueBarChartFromViewer();
            }
            MyVars.getViewer().revalidate();
            MyVars.getViewer().repaint();
        }
    }

    @Override public void componentResized(ComponentEvent e) {
        new Thread(new Runnable() {
            @Override public void run() {
                updateSplitPaneDividerLocations();
            }
        }).start();
    }

    @Override public void componentMoved(ComponentEvent e) {
        new Thread(new Runnable() {
            @Override public void run() {
                updateSplitPaneDividerLocations();
            }
        }).start();
    }

    @Override public void componentShown(ComponentEvent e) {}
    @Override public void componentHidden(ComponentEvent e) {}
}