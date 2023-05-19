package medousa.sequential.graph;

import edu.uci.ics.jung.algorithms.cluster.WeakComponentClusterer;
import medousa.sequential.graph.stats.depthnode.*;
import medousa.sequential.graph.stats.multinode.*;
import medousa.sequential.graph.stats.singlenode.*;
import medousa.sequential.graph.stats.*;
import medousa.sequential.graph.stats.singlenode.MySingleNodeHopCountDistributionLineChart;
import medousa.sequential.utils.MyMathUtil;
import medousa.sequential.utils.MySequentialGraphSysUtil;
import medousa.sequential.utils.MySequentialGraphVars;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Set;

public class MySequentialGraphDashBoard
extends JPanel {

    public MyGraphLevelDistributionLineChart graphLevelNodeCountDistributionLineChart;
    public MyGraphLevelContributionByDepthLineChart graphLevelContributionByDepthLineChart;
    public MyGraphLevelUniqueNodesByDepthLineBarChart graphLevelUniqueNodesByDepthLineChart;
    public MyGraphLevelReachTimeByDepthLineChart graphLevelReachTimeByDepthLineChart;
    public MyGraphLevelDurationByDepthLineChart graphLevelDurationByDepthLineChart;
    public MyGraphLevelPredecessorSuccessorByDepthLineChart graphLevelPredecessorSuccessorByDepthLineChart;

    public MyGraphLevelAverageValuesByDepthLineChart graphLevelAverageValuesByDepthLineChart;

    // Graph Level Depth Charts with time condition on.
    public MyDepthLevelHopCountDistribution depthLevelNodeHopCountDistribution;
    public MyDepthLevelPredecessorsAndSuccessorsByDepthLineChart depthLevelPredecessorsAndSuccessorsByDepthLineChart;
    public MyDepthLevelNodeValueHistogramDistributionLineChart depthLevelNodeValueHistogramDistributionLineChart;
    public MyDepthLevelEndingNodeValueHistogramDistributionLineChart depthLevelEndingNodeValueHistogramDistributionLineChart;

    public MySequentialGraphDashBoard() {}
    public void setDashboard() {
        setGraphLevelDashBoard();
    }

    TitledBorder dataProfileTitledBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK),
        "DATA PROFILE", TitledBorder.LEFT, TitledBorder.TOP, MySequentialGraphVars.tahomaBoldFont12);

    TitledBorder networkTitledBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK),
            "NETWORK", TitledBorder.LEFT, TitledBorder.TOP, MySequentialGraphVars.tahomaBoldFont12);

    public void setGraphLevelDashBoard() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                removeAll();
                setLayout(new BorderLayout(0,0));

                if (MySequentialGraphVars.isTimeOn) {
                    graphLevelAverageValuesByDepthLineChart = new MyGraphLevelAverageValuesByDepthLineChart();
                    graphLevelReachTimeByDepthLineChart = new MyGraphLevelReachTimeByDepthLineChart();
                    graphLevelUniqueNodesByDepthLineChart = new MyGraphLevelUniqueNodesByDepthLineBarChart();
                    graphLevelContributionByDepthLineChart = new MyGraphLevelContributionByDepthLineChart();
                    graphLevelPredecessorSuccessorByDepthLineChart = new MyGraphLevelPredecessorSuccessorByDepthLineChart();
                    graphLevelDurationByDepthLineChart = new MyGraphLevelDurationByDepthLineChart();

                    JPanel dataProfilePanel = new JPanel();
                    dataProfilePanel.setBackground(Color.WHITE);
                    dataProfilePanel.setLayout(new GridLayout(6, 1));
                    dataProfilePanel.add(graphLevelAverageValuesByDepthLineChart);
                    dataProfilePanel.add(graphLevelUniqueNodesByDepthLineChart);
                    dataProfilePanel.add(graphLevelPredecessorSuccessorByDepthLineChart);
                    dataProfilePanel.add(graphLevelReachTimeByDepthLineChart);
                    dataProfilePanel.add(graphLevelDurationByDepthLineChart);
                    dataProfilePanel.add(graphLevelContributionByDepthLineChart);
                    dataProfilePanel.setBorder(dataProfileTitledBorder);

                    JTabbedPane tabbedPane = new JTabbedPane();
                    tabbedPane.addTab("DATA PROFILE", null, dataProfilePanel, "DATA PROFILE");
                    tabbedPane.addTab("GRAPH STATS.", null, MySequentialGraphVars.getSequentialGraphViewer().vc.setStatTable(), "GRAPH STATISTICS");
                    tabbedPane.setFont(MySequentialGraphVars.tahomaBoldFont12);

                    JSplitPane graphAndGraphChartSplitPane = new JSplitPane();
                    graphAndGraphChartSplitPane.setDividerLocation((int)(MySequentialGraphSysUtil.getViewerWidth()*0.145));
                    graphAndGraphChartSplitPane.setDividerSize(4);
                    graphAndGraphChartSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
                    graphAndGraphChartSplitPane.setLeftComponent(tabbedPane);
                    graphAndGraphChartSplitPane.setRightComponent(MySequentialGraphVars.app.getSequentialGraphViewerPanel());

                    add(graphAndGraphChartSplitPane);

                    MySequentialGraphVars.app.addComponentListener(new ComponentAdapter() {
                        public void componentResized(ComponentEvent evt) {
                        graphAndGraphChartSplitPane.setDividerSize(4);
                        graphAndGraphChartSplitPane.setDividerLocation((int)(MySequentialGraphSysUtil.getViewerWidth()*0.145));
                        }
                    });
                } else {
                    graphLevelAverageValuesByDepthLineChart = new MyGraphLevelAverageValuesByDepthLineChart();
                    graphLevelUniqueNodesByDepthLineChart = new MyGraphLevelUniqueNodesByDepthLineBarChart();
                    graphLevelContributionByDepthLineChart = new MyGraphLevelContributionByDepthLineChart();
                    graphLevelPredecessorSuccessorByDepthLineChart = new MyGraphLevelPredecessorSuccessorByDepthLineChart();

                    JPanel dataProfilePanel = new JPanel();
                    dataProfilePanel.setBackground(Color.WHITE);
                    dataProfilePanel.setLayout(new GridLayout(4, 1));
                    dataProfilePanel.add(graphLevelAverageValuesByDepthLineChart);
                    dataProfilePanel.add(graphLevelUniqueNodesByDepthLineChart);
                    dataProfilePanel.add(graphLevelContributionByDepthLineChart);
                    dataProfilePanel.add(graphLevelPredecessorSuccessorByDepthLineChart);
                    dataProfilePanel.setBorder(dataProfileTitledBorder);

                    JTabbedPane tabbedPane = new JTabbedPane();
                    tabbedPane.addTab("DATA PROFILE", null, dataProfilePanel, "DATA PROFILE");
                    tabbedPane.addTab("GRAPH STATS.", null, MySequentialGraphVars.getSequentialGraphViewer().vc.setStatTable(), "GRAPH STATISTICS");
                    tabbedPane.setFont(MySequentialGraphVars.tahomaBoldFont12);

                    JSplitPane graphAndGraphChartSplitPane = new JSplitPane();
                    graphAndGraphChartSplitPane.setDividerLocation((int)(MySequentialGraphSysUtil.getViewerWidth()*0.145));
                    graphAndGraphChartSplitPane.setDividerSize(4);
                    graphAndGraphChartSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
                    graphAndGraphChartSplitPane.setLeftComponent(tabbedPane);
                    graphAndGraphChartSplitPane.setRightComponent(MySequentialGraphVars.app.getSequentialGraphViewerPanel());

                    add(graphAndGraphChartSplitPane);
                    MySequentialGraphVars.app.addComponentListener(new ComponentAdapter() {
                        public void componentResized(ComponentEvent evt) {
                        graphAndGraphChartSplitPane.setDividerSize(4);
                        graphAndGraphChartSplitPane.setDividerLocation((int)(MySequentialGraphSysUtil.getViewerWidth()*0.145));
                        }
                    });
                }
                revalidate();
                repaint();
            }
        });
    }

    public void setDepthNodeDashBoard() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                removeAll();
                setLayout(new BorderLayout(0,0));
                if (MySequentialGraphVars.isTimeOn) {
                    depthLevelPredecessorsAndSuccessorsByDepthLineChart = new MyDepthLevelPredecessorsAndSuccessorsByDepthLineChart();
                    graphLevelAverageValuesByDepthLineChart = new MyGraphLevelAverageValuesByDepthLineChart();
                    graphLevelReachTimeByDepthLineChart = new MyGraphLevelReachTimeByDepthLineChart();
                    MyDepthLevelUniqueNodesByDepthLineChart graphLevelLevelUniqueNodesByDepthLineChart = new MyDepthLevelUniqueNodesByDepthLineChart();
                    MyGraphLevelUniqueNodesByDepthLineBarChart depthLevelUniqueNodesByDepthLineChart = new MyGraphLevelUniqueNodesByDepthLineBarChart();
                    depthLevelNodeHopCountDistribution = new MyDepthLevelHopCountDistribution();
                    graphLevelContributionByDepthLineChart = new MyGraphLevelContributionByDepthLineChart();
                    depthLevelNodeValueHistogramDistributionLineChart = new MyDepthLevelNodeValueHistogramDistributionLineChart();

                    JPanel dataProfilePanel = new JPanel();
                    dataProfilePanel.setBackground(Color.WHITE);
                    dataProfilePanel.setLayout(new GridLayout(6, 1));
                    dataProfilePanel.add(graphLevelLevelUniqueNodesByDepthLineChart);
                    dataProfilePanel.add(graphLevelAverageValuesByDepthLineChart);
                    dataProfilePanel.add(depthLevelUniqueNodesByDepthLineChart);
                    dataProfilePanel.add(depthLevelPredecessorsAndSuccessorsByDepthLineChart);
                    dataProfilePanel.add(graphLevelReachTimeByDepthLineChart);
                    dataProfilePanel.add(graphLevelContributionByDepthLineChart);
                    dataProfilePanel.setBorder(dataProfileTitledBorder);

                    JSplitPane graphAndGraphChartSplitPane = new JSplitPane();
                    graphAndGraphChartSplitPane.setDividerLocation((int)(MySequentialGraphSysUtil.getViewerWidth()*0.145));
                    graphAndGraphChartSplitPane.setDividerSize(4);
                    graphAndGraphChartSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
                    graphAndGraphChartSplitPane.setLeftComponent(dataProfilePanel);
                    graphAndGraphChartSplitPane.setRightComponent(MySequentialGraphVars.app.getSequentialGraphViewerPanel());

                    add(graphAndGraphChartSplitPane, BorderLayout.CENTER);
                    MySequentialGraphVars.app.addComponentListener(new ComponentAdapter() {
                        public void componentResized(ComponentEvent evt) {
                            graphAndGraphChartSplitPane.setDividerSize(4);
                            graphAndGraphChartSplitPane.setDividerLocation((int)(MySequentialGraphSysUtil.getViewerWidth()*0.145));
                        }
                    });
                } else {
                    graphLevelAverageValuesByDepthLineChart = new MyGraphLevelAverageValuesByDepthLineChart();
                    graphLevelUniqueNodesByDepthLineChart = new MyGraphLevelUniqueNodesByDepthLineBarChart();
                    depthLevelNodeHopCountDistribution = new MyDepthLevelHopCountDistribution();
                    graphLevelContributionByDepthLineChart = new MyGraphLevelContributionByDepthLineChart();
                    graphLevelNodeCountDistributionLineChart = new MyGraphLevelDistributionLineChart();
                    graphLevelUniqueNodesByDepthLineChart = new MyGraphLevelUniqueNodesByDepthLineBarChart();

                    JPanel dataProfilePanel = new JPanel();
                    dataProfilePanel.setBackground(Color.WHITE);
                    dataProfilePanel.setLayout(new GridLayout(5, 1));
                    dataProfilePanel.add(graphLevelAverageValuesByDepthLineChart);
                    dataProfilePanel.add(graphLevelUniqueNodesByDepthLineChart);
                    dataProfilePanel.add(graphLevelContributionByDepthLineChart);
                    dataProfilePanel.add(graphLevelNodeCountDistributionLineChart);
                    dataProfilePanel.add(depthLevelEndingNodeValueHistogramDistributionLineChart);
                    dataProfilePanel.setBorder(dataProfileTitledBorder);

                    JSplitPane graphAndGraphChartSplitPane = new JSplitPane();
                    graphAndGraphChartSplitPane.setDividerLocation((int)(MySequentialGraphSysUtil.getViewerWidth()*0.145));
                    graphAndGraphChartSplitPane.setDividerSize(4);
                    graphAndGraphChartSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
                    graphAndGraphChartSplitPane.setLeftComponent(dataProfilePanel);
                    graphAndGraphChartSplitPane.setRightComponent(MySequentialGraphVars.app.getSequentialGraphViewerPanel());

                    add(graphAndGraphChartSplitPane, BorderLayout.CENTER);
                    MySequentialGraphVars.app.addComponentListener(new ComponentAdapter() {
                        public void componentResized(ComponentEvent evt) {
                            graphAndGraphChartSplitPane.setDividerSize(4);
                            graphAndGraphChartSplitPane.setDividerLocation((int)(MySequentialGraphSysUtil.getViewerWidth()*0.145));
                        }
                    });
                }

                revalidate();
                repaint();
            }
        });
    }

    public void setMultiNodeDashBoard() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                removeAll();
                setLayout(new BorderLayout(0, 0));

                MyMultiNodeAppearanceByDepthLineChart sharedNodeLevelSelectedNodesByDepthLineChart = new MyMultiNodeAppearanceByDepthLineChart();
                MyMultiLevelSharedSuccessorAppearancesByDepthLineChart sharedNodeLevelSharedSuccessorAppearancesByDepthLineChart = new MyMultiLevelSharedSuccessorAppearancesByDepthLineChart();
                MyMultiLevelSharedPredecessorAppearancesByDepthLineChart sharedNodeLevelSharedPredecessorAppearancesByDepthLineChart = new MyMultiLevelSharedPredecessorAppearancesByDepthLineChart();
                MyMultiNodePredecessorsAndSuccessorsByDepthLineChart multiLevelPredecessorsAndSuccessorsByDepthLineChart = new MyMultiNodePredecessorsAndSuccessorsByDepthLineChart();
                MyMultiLevelStartingNodeValueHistogramDistributionLineChart multiLevelStartingNodeValueHistogramDistributionLineChart = new MyMultiLevelStartingNodeValueHistogramDistributionLineChart();
                MyMultiLevelEndingNodeValueHistogramDistributionLineChart multiLevelEndingNodeValueHistogramDistributionLineChart = new MyMultiLevelEndingNodeValueHistogramDistributionLineChart();

                JPanel dataProfilePanel = new JPanel();
                dataProfilePanel.setBackground(Color.WHITE);
                dataProfilePanel.setLayout(new GridLayout(6, 1));
                dataProfilePanel.add(sharedNodeLevelSelectedNodesByDepthLineChart);
                dataProfilePanel.add(multiLevelPredecessorsAndSuccessorsByDepthLineChart);
                dataProfilePanel.add(sharedNodeLevelSharedSuccessorAppearancesByDepthLineChart);
                dataProfilePanel.add(sharedNodeLevelSharedPredecessorAppearancesByDepthLineChart);
                dataProfilePanel.add(multiLevelStartingNodeValueHistogramDistributionLineChart);
                dataProfilePanel.add(multiLevelEndingNodeValueHistogramDistributionLineChart);
                dataProfilePanel.setBorder(dataProfileTitledBorder);

                JTabbedPane tabbedPane = new JTabbedPane();
                tabbedPane.addTab("DATA PROFILE", null, dataProfilePanel, "DATA PROFILE");
                tabbedPane.addTab("GRAPH STATS.", null, MySequentialGraphVars.getSequentialGraphViewer().vc.setMultiNodeStatTable(), "GRAPH STATISTICS");
                tabbedPane.setFont(MySequentialGraphVars.tahomaBoldFont12);

                JSplitPane graphAndGraphChartSplitPane = new JSplitPane();
                graphAndGraphChartSplitPane.setDividerLocation((int)(MySequentialGraphSysUtil.getViewerWidth()*0.145));
                graphAndGraphChartSplitPane.setDividerSize(4);
                graphAndGraphChartSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
                graphAndGraphChartSplitPane.setLeftComponent(tabbedPane);
                graphAndGraphChartSplitPane.setRightComponent(MySequentialGraphVars.app.getSequentialGraphViewerPanel());

                add(graphAndGraphChartSplitPane, BorderLayout.CENTER);
                MySequentialGraphVars.app.addComponentListener(new ComponentAdapter() {
                    public void componentResized(ComponentEvent evt) {
                    graphAndGraphChartSplitPane.setDividerSize(4);
                    graphAndGraphChartSplitPane.setDividerLocation((int)(MySequentialGraphSysUtil.getViewerWidth()*0.145));
                    }
                });
                revalidate();
                repaint();
            }
        });
    }

    public void setSingleNodeDashBoard() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                removeAll();
                setLayout(new BorderLayout(0,0));

                if (MySequentialGraphVars.isTimeOn) {
                    MySingleNodeAverageValuesByDepthLineChart singleNodeAverageValuesByDepthLineChart = new MySingleNodeAverageValuesByDepthLineChart();
                    MySingleNodeContributionByDepthLineChart singleNodeNodeContributionByDepthLineChart = new MySingleNodeContributionByDepthLineChart();
                    MySingleNodeReachTimeByDepthLineChart singleNodeReachTimeByDepthLineChart = new MySingleNodeReachTimeByDepthLineChart();
                    MySingleNodePredecessorsAndSuccessorsByDepthLineChart singleNodePredecessorsAndSuccessorsByDepthLineChart = new MySingleNodePredecessorsAndSuccessorsByDepthLineChart();
                    MySingleNodeEndingNodeValueHistogramDistributionLineChart singleNodeEndingNodeValueHistogramDistributionLineChart = new MySingleNodeEndingNodeValueHistogramDistributionLineChart();
                    MySingleNodeStartingNodeValueHistogramDistributionLineChart singleNodeStartingNodeValueHistogramDistributionLineChart = new MySingleNodeStartingNodeValueHistogramDistributionLineChart();

                    JPanel dataProfilePanel = new JPanel();
                    dataProfilePanel.setBackground(Color.WHITE);
                    dataProfilePanel.setLayout(new GridLayout(6, 1));
                    dataProfilePanel.add(singleNodeAverageValuesByDepthLineChart);
                    dataProfilePanel.add(singleNodePredecessorsAndSuccessorsByDepthLineChart);
                    dataProfilePanel.add(singleNodeReachTimeByDepthLineChart);
                    dataProfilePanel.add(singleNodeNodeContributionByDepthLineChart);
                    dataProfilePanel.add(singleNodeStartingNodeValueHistogramDistributionLineChart);
                    dataProfilePanel.add(singleNodeEndingNodeValueHistogramDistributionLineChart);
                    dataProfilePanel.setBorder(dataProfileTitledBorder);

                    JTabbedPane tabbedPane = new JTabbedPane();
                    tabbedPane.addTab("DATA PROFILE", null, dataProfilePanel, "NODE PROFILE");
                    tabbedPane.addTab("NODE STATS.", null, MySequentialGraphVars.getSequentialGraphViewer().vc.setSelectedNodeStatTable(), "GRAPH STATISTICS");
                    tabbedPane.setFont(MySequentialGraphVars.tahomaBoldFont12);

                    JSplitPane graphAndGraphChartSplitPane = new JSplitPane();
                    graphAndGraphChartSplitPane.setDividerLocation((int)(MySequentialGraphSysUtil.getViewerWidth()*0.145));
                    graphAndGraphChartSplitPane.setDividerSize(5);
                    graphAndGraphChartSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
                    graphAndGraphChartSplitPane.setLeftComponent(tabbedPane);
                    graphAndGraphChartSplitPane.setRightComponent(MySequentialGraphVars.app.getSequentialGraphViewerPanel());

                    add(graphAndGraphChartSplitPane, BorderLayout.CENTER);
                    MySequentialGraphVars.app.addComponentListener(new ComponentAdapter() {
                        public void componentResized(ComponentEvent evt) {
                        graphAndGraphChartSplitPane.setDividerSize(5);
                        graphAndGraphChartSplitPane.setDividerLocation((int)(MySequentialGraphSysUtil.getViewerWidth()*0.145));
                        }
                    });
                } else {
                    MySingleNodeAverageValuesByDepthLineChart singleNodeLevelAverageValuesByDepthLineChart = new MySingleNodeAverageValuesByDepthLineChart();
                    MySingleNodeContributionByDepthLineChart singleNodeLevelNodeContributionByDepthLineChart = new MySingleNodeContributionByDepthLineChart();
                    MySingleNodePredecessorsAndSuccessorsByDepthLineChart singleNodePredecessorsAndSuccessorsByDepthLineChart = new MySingleNodePredecessorsAndSuccessorsByDepthLineChart();
                    MySingleNodeEndingNodeValueHistogramDistributionLineChart singleNodeEndingNodeValueHistogramDistributionLineChart = new MySingleNodeEndingNodeValueHistogramDistributionLineChart();

                    JPanel dataProfilePanel = new JPanel();
                    dataProfilePanel.setBackground(Color.WHITE);
                    dataProfilePanel.setLayout(new GridLayout(4, 1));
                    dataProfilePanel.add(singleNodeLevelAverageValuesByDepthLineChart);
                    dataProfilePanel.add(singleNodePredecessorsAndSuccessorsByDepthLineChart);
                    dataProfilePanel.add(singleNodeLevelNodeContributionByDepthLineChart);
                    dataProfilePanel.add(singleNodeEndingNodeValueHistogramDistributionLineChart);
                    dataProfilePanel.setBorder(dataProfileTitledBorder);

                    JTabbedPane tabbedPane = new JTabbedPane();
                    tabbedPane.addTab("DATA PROFILE", null, dataProfilePanel, "NODE PROFILE");
                    tabbedPane.addTab("NODE STATS.", null, MySequentialGraphVars.getSequentialGraphViewer().vc.setSelectedNodeStatTable(), "GRAPH STATISTICS");
                    tabbedPane.setFont(MySequentialGraphVars.tahomaBoldFont12);

                    JSplitPane graphAndGraphChartSplitPane = new JSplitPane();
                    graphAndGraphChartSplitPane.setDividerLocation((int)(MySequentialGraphSysUtil.getViewerWidth()*0.145));
                    graphAndGraphChartSplitPane.setDividerSize(5);
                    graphAndGraphChartSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
                    graphAndGraphChartSplitPane.setLeftComponent(tabbedPane);
                    graphAndGraphChartSplitPane.setRightComponent(MySequentialGraphVars.app.getSequentialGraphViewerPanel());

                    add(graphAndGraphChartSplitPane, BorderLayout.CENTER);
                    MySequentialGraphVars.app.addComponentListener(new ComponentAdapter() {
                        public void componentResized(ComponentEvent evt) {
                            graphAndGraphChartSplitPane.setDividerSize(5);
                            graphAndGraphChartSplitPane.setDividerLocation((int)(MySequentialGraphSysUtil.getViewerWidth()*0.145));
                        }
                    });
                }
                revalidate();
                repaint();
            }
        });
    }
}