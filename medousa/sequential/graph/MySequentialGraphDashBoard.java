package medousa.sequential.graph;

import medousa.sequential.graph.stats.depthnode.*;
import medousa.sequential.graph.stats.multinode.*;
import medousa.sequential.graph.stats.singlenode.*;
import medousa.sequential.graph.stats.*;
import medousa.sequential.utils.MySequentialGraphSysUtil;
import medousa.sequential.utils.MySequentialGraphVars;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Collection;

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

    public MyGraphLevelNodesByShortestDistanceDistributionLineChart graphLevelShortestAverageDistanceDistributionLineChart;
    public MyGraphLevelShortestDistanceNodeValueDistributionLineChart graphLevelShortestDistanceNodeValueDistributionLineChart;
    public MyGraphLevelShortestDistanceUnreachableNodeCountDistributionLineChart graphLevelShortestDistanceUnreachableNodeCountDistributionLineChart;
    public MyGraphLevelAverageNodeValueByDistanceDistributionLineChart graphLevelAverageNodeValueByDistanceDistributionLineChart;


    public MySequentialGraphDashBoard() {}
    public void setDashboard() {
        setGraphLevelDashBoard();
    }

    TitledBorder dataProfileTitledBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK),
        "DISTRIBUTIONS", TitledBorder.LEFT, TitledBorder.TOP, MySequentialGraphVars.tahomaBoldFont12);

    TitledBorder shortestDistanceTitledBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK),
            "DISTRIBUTIONS", TitledBorder.LEFT, TitledBorder.TOP, MySequentialGraphVars.tahomaBoldFont12);

    public void setShortestDistanceDashBoard() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                removeAll();
                setLayout(new BorderLayout(0,0));

                TitledBorder networkTitledBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK),
                    "NETWORK EXPLORER", TitledBorder.LEFT, TitledBorder.TOP, MySequentialGraphVars.tahomaBoldFont12);

                graphLevelShortestAverageDistanceDistributionLineChart = new MyGraphLevelNodesByShortestDistanceDistributionLineChart();
                graphLevelShortestDistanceNodeValueDistributionLineChart = new MyGraphLevelShortestDistanceNodeValueDistributionLineChart();
                graphLevelShortestDistanceUnreachableNodeCountDistributionLineChart = new MyGraphLevelShortestDistanceUnreachableNodeCountDistributionLineChart();
                graphLevelAverageNodeValueByDistanceDistributionLineChart = new MyGraphLevelAverageNodeValueByDistanceDistributionLineChart();
                //graphLevelTotalNodeByShortestDistanceDistributionLineChart = new MyGraphLevelTotalNodeByShortestDistanceDistributionLineChart();

                JPanel shortestDistanceProfilePanel = new JPanel();
                shortestDistanceProfilePanel.setBackground(Color.WHITE);
                shortestDistanceProfilePanel.setLayout(new GridLayout(4, 1));
                shortestDistanceProfilePanel.add(graphLevelShortestDistanceUnreachableNodeCountDistributionLineChart);
                shortestDistanceProfilePanel.add(graphLevelShortestAverageDistanceDistributionLineChart);
                //shortestDistanceProfilePanel.add(graphLevelTotalNodeByShortestDistanceDistributionLineChart);
                shortestDistanceProfilePanel.add(graphLevelAverageNodeValueByDistanceDistributionLineChart);
                shortestDistanceProfilePanel.add(graphLevelShortestDistanceNodeValueDistributionLineChart);
                shortestDistanceProfilePanel.setBorder(shortestDistanceTitledBorder);

                JTabbedPane tabbedPane = new JTabbedPane();
                tabbedPane.addTab("NODE DISTANCE PROFILE", null, shortestDistanceProfilePanel, "NODE DISTANCE PROFILE");
                //tabbedPane.addTab("GRAPH STATS.", null, MySequentialGraphVars.getSequentialGraphViewer().vc.setStatTable(), "GRAPH STATISTICS");
                tabbedPane.setFont(MySequentialGraphVars.tahomaBoldFont12);

                MySequentialGraphVars.app.getSequentialGraphViewerPanel().setBorder(networkTitledBorder);

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

                revalidate();
                repaint();
            }
        });
    }


    public void setGraphLevelDashBoard() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                removeAll();
                setLayout(new BorderLayout(0,0));

                TitledBorder networkTitledBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY),
                "NETWORK EXPLORER", TitledBorder.LEFT, TitledBorder.TOP, MySequentialGraphVars.tahomaBoldFont12);

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
                    tabbedPane.addTab("D. P.", null, dataProfilePanel, "DATA PROFILE");
                    tabbedPane.addTab("G. S.", null, MySequentialGraphVars.getSequentialGraphViewer().vc.setStatTable(), "GRAPH TEXT STATISTICS");
                    tabbedPane.setFont(MySequentialGraphVars.tahomaBoldFont12);

                    MySequentialGraphVars.app.getSequentialGraphViewerPanel().setBorder(networkTitledBorder);

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
                    tabbedPane.addTab("GRAPH PROFILE", null, dataProfilePanel, "GRAPH PROFILE");
                    tabbedPane.addTab("GRAPH STATS.", null, MySequentialGraphVars.getSequentialGraphViewer().vc.setStatTable(), "GRAPH STATISTICS");
                    tabbedPane.setFont(MySequentialGraphVars.tahomaBoldFont12);

                    MySequentialGraphVars.app.getSequentialGraphViewerPanel().setBorder(networkTitledBorder);

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

                TitledBorder networkTitledBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK),
                        "NETWORK EXPLORER", TitledBorder.LEFT, TitledBorder.TOP, MySequentialGraphVars.tahomaBoldFont12);

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

                    MySequentialGraphVars.app.getSequentialGraphViewerPanel().setBorder(networkTitledBorder);

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

                    MySequentialGraphVars.app.getSequentialGraphViewerPanel().setBorder(networkTitledBorder);

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

                TitledBorder networkTitledBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK),
                        "NETWORK EXPLORER", TitledBorder.LEFT, TitledBorder.TOP, MySequentialGraphVars.tahomaBoldFont12);


                /**
                 * Set nodes that are not neighbor nodes to zeros.
                 */
                Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
                for (MyNode n : nodes) {
                    if (!MySequentialGraphVars.getSequentialGraphViewer().multiNodeSuccessors.contains(n) &&
                        !MySequentialGraphVars.getSequentialGraphViewer().multiNodePredecessors.contains(n)) {
                        n.setCurrentValue(0f);
                    }
                }

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
                tabbedPane.addTab("DATA PROFILE", null, dataProfilePanel, "SELECTED MULTINODE DATA PROFILE");
                tabbedPane.addTab("NODE STATS.", null, MySequentialGraphVars.getSequentialGraphViewer().vc.setMultiNodeStatTable(), "SELECTED MULTINODE STATISTICS");
                tabbedPane.setFont(MySequentialGraphVars.tahomaBoldFont12);

                MySequentialGraphVars.app.getSequentialGraphViewerPanel().setBorder(networkTitledBorder);

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

                TitledBorder networkTitledBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK),
                    "NETWORK EXPLORER", TitledBorder.LEFT, TitledBorder.TOP, MySequentialGraphVars.tahomaBoldFont12);


                /**
                 * Set nodes that are not neighbor nodes to zeros.
                 */
                Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
                for (MyNode n : nodes) {
                    if (!MySequentialGraphVars.getSequentialGraphViewer().selectedSingleNodePredecessors.contains(n) &&
                        !MySequentialGraphVars.getSequentialGraphViewer().selectedSingleNodeSuccessors.contains(n)) {
                        n.setCurrentValue(0f);
                    }
                }

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
                    tabbedPane.addTab("DATA PROFILE", null, dataProfilePanel, "SELECTED NODE DATA PROFILE");
                    tabbedPane.addTab("NODE STATS.", null, MySequentialGraphVars.getSequentialGraphViewer().vc.setSelectedNodeStatTable(), "SELECTED NODE STATISTICS");
                    tabbedPane.setFont(MySequentialGraphVars.tahomaBoldFont12);

                    MySequentialGraphVars.app.getSequentialGraphViewerPanel().setBorder(networkTitledBorder);

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
                    tabbedPane.addTab("NODE STATS.", null, MySequentialGraphVars.getSequentialGraphViewer().vc.setSelectedNodeStatTable(), "NODE STATISTICS");
                    tabbedPane.setFont(MySequentialGraphVars.tahomaBoldFont12);

                    MySequentialGraphVars.app.getSequentialGraphViewerPanel().setBorder(networkTitledBorder);

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
                }
                revalidate();
                repaint();
            }
        });
    }
}