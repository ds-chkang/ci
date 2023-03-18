package datamining.graph;

import datamining.graph.stats.*;
import datamining.graph.stats.depthnode.*;
import datamining.graph.stats.multinode.*;
import datamining.graph.stats.singlenode.*;
import datamining.graph.stats.singlenode.MySingleNodeHopCountDistributionLineChart;
import datamining.utils.system.MySysUtil;
import datamining.utils.system.MyVars;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class MyDashBoard
extends JPanel {

    // Graph Level with Time condition on.
    public MyGraphLevelEdgeValueDistributionLineChart graphLevelEdgeCurrentValueDistributionLineGraph;
    public MyGraphLevelNodeValueDistributionLineChart graphLevelNodeCurrrentValueDistributionLineGraph;
    public MyGraphLevelSuccessorCountDistributionLineChart graphLevelSuccessorCountDistributionLineGraph;
    public MyGraphLevelPredecessorCountDistributionLineChart graphLevelPredecessorCountDistributionLineGraph;
    public MyGraphLevelNodeAverageHopCountDistributionLineChart graphLevelNodeHopCountDistributionLineGraph;
    public MyGraphLevelNodeCountDistributionLineChart graphLevelNodeCountDistributionLineChart;
    public MyGraphLevelContributionByDepthLineChart graphLevelContributionByDepthLineChart;
    public MyGraphLevelUniqueNodesByDepthLineBarChart graphLevelUniqueNodesByDepthLineChart;
    public MyGraphLevelReachTimeByDepthLineChart graphLevelReachTimeByDepthLineChart;
    public MyGraphLevelNodePathLengthDistributionLineChart graphLevelNodePathLengthDistributionLineChart;
    public MyGraphLevelUniqueContributionDistributionLineChart graphLevelUniqueContributionDistributionLineChart;
    public MyGraphLevelAverageShortestPathLengthDistributionLineChart graphLevelShortestAvgPathLengthDistributionLineChart;
    public MyGraphLevelPredecessorSuccessorByDepthLineChart graphLevelPredecessorSuccessorByDepthLineChart;

    public MyGraphLevelAverageValuesByDepthLineChart graphLevelAverageValuesByDepthLineChart;
    public MyGraphLevelLabelDistributionLineChart graphLevelLabelValueDistributionLineChart;
    public MyGraphLevelValueDistributionLineChart graphLevelValueDistributionLineChart;
    public MyGraphLevelNodeValueByPositionHistogramDistributionLineChart graphLevelEndingNodeValueHistogramDistributionLineChart;


    // Node Level with time condition on for selected node.
    public MySingleNodePredecessorEdgeValueDistributionLineChart singleNodePredecessorEdgeCurrentValueDistributionLineChart;
    public MySingleNodeSuccessorEdgeValueDistributionLineChart singleNodeSuccessorEdgeCurrentValueDistributionLineChart;
    public MySingleNodeSuccessorValueDistributionLineChart singleNodeSuccessorValueDistributionLineGraph;
    public MySingleNodePredecessorValueDistributionLineChart singleNodePredecessorValueDistributionLineGraph;

    // Graph Level Depth Charts with time condition on.
    public MyDepthLevelCurrentValueDistribution depthLevelNodeCurrentValueDistribution;
    public MyDepthLevelHopCountDistribution depthLevelNodeHopCountDistribution;
    public MyDepthLevelEdgeCurrentValueDistribution depthLevelEdgeCurrentValueDistribution;
    public MyDepthLevelPredecessorsAndSuccessorsByDepthLineChart depthLevelPredecessorsAndSuccessorsByDepthLineChart;
    public MyDepthLevelNodeValueHistogramDistributionLineChart depthLevelNodeValueHistogramDistributionLineChart;
    public MyDepthLevelEndingNodeValueHistogramDistributionLineChart depthLevelEndingNodeValueHistogramDistributionLineChart;

    public MyMultiNodeHopCountDistributionLineChart multiNodeNodeHopCountDistributionLineChart;
    public MyMultiNodeSuccessorValueDistributionLineChart multiNodeSuccessorValueDistributionLineChart;
    public MyMultiNodePredecessorValueDistributionLineChart multiNodePredecessorValueDistributionLineChart;
    public MyMultiNodeSharedPredecessorEdgeValueDistributionLineChart multiNodeSharedPredecessorEdgeValueDistributionLineChart;
    public MyMultiNodeSharedSuccessorEdgeValueDistributionLineChart multiNodeSharedSuccessorEdgeValueDistributionLineChart;
    public MyMultiLevelSelectedNodeValueDistributionLineChart multiNodeSelectedNodeValueDistributionLineChart;

    public MyDashBoard() {}
    public void setDashboard() {
        setGraphLevelDashBoard();
    }

    public void setGraphLevelDashBoard() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                removeAll();
                setLayout(new BorderLayout(0,0));
                if (MyVars.isTimeOn) {
                    graphLevelAverageValuesByDepthLineChart = new MyGraphLevelAverageValuesByDepthLineChart();
                    graphLevelReachTimeByDepthLineChart = new MyGraphLevelReachTimeByDepthLineChart();
                    graphLevelUniqueNodesByDepthLineChart = new MyGraphLevelUniqueNodesByDepthLineBarChart();
                    graphLevelContributionByDepthLineChart = new MyGraphLevelContributionByDepthLineChart();
                    graphLevelPredecessorSuccessorByDepthLineChart = new MyGraphLevelPredecessorSuccessorByDepthLineChart();

                    JPanel dataProfilePanel = new JPanel();
                    dataProfilePanel.setBackground(Color.WHITE);
                    dataProfilePanel.setFont(MyVars.tahomaBoldFont10);
                    dataProfilePanel.setLayout(new GridLayout(5, 1));
                    dataProfilePanel.add(graphLevelAverageValuesByDepthLineChart);
                    dataProfilePanel.add(graphLevelUniqueNodesByDepthLineChart);
                    dataProfilePanel.add(graphLevelPredecessorSuccessorByDepthLineChart);
                    dataProfilePanel.add(graphLevelReachTimeByDepthLineChart);
                    dataProfilePanel.add(graphLevelContributionByDepthLineChart);

                    JPanel graphProfilePanel = new JPanel();
                    graphProfilePanel.setBackground(Color.WHITE);
                    graphProfilePanel.setLayout(new GridLayout(1, 6));

                    graphLevelShortestAvgPathLengthDistributionLineChart = new MyGraphLevelAverageShortestPathLengthDistributionLineChart();
                    graphLevelSuccessorCountDistributionLineGraph = new MyGraphLevelSuccessorCountDistributionLineChart();
                    graphLevelPredecessorCountDistributionLineGraph = new MyGraphLevelPredecessorCountDistributionLineChart();
                    graphLevelValueDistributionLineChart = new MyGraphLevelValueDistributionLineChart();
                    graphLevelUniqueContributionDistributionLineChart = new MyGraphLevelUniqueContributionDistributionLineChart();

                    if (MyVars.nodeLabelSet.size() > 0 || MyVars.edgeLabelSet.size() > 0) {
                        graphLevelLabelValueDistributionLineChart = new MyGraphLevelLabelDistributionLineChart();
                        graphProfilePanel.add(graphLevelLabelValueDistributionLineChart);
                    } else {
                        graphLevelNodeCountDistributionLineChart = new MyGraphLevelNodeCountDistributionLineChart();
                        graphProfilePanel.add(graphLevelNodeCountDistributionLineChart);
                    }

                    graphProfilePanel.add(graphLevelShortestAvgPathLengthDistributionLineChart);
                    graphProfilePanel.add(graphLevelUniqueContributionDistributionLineChart);
                    graphProfilePanel.add(graphLevelPredecessorCountDistributionLineGraph);
                    graphProfilePanel.add(graphLevelSuccessorCountDistributionLineGraph);
                    graphProfilePanel.add(graphLevelValueDistributionLineChart);

                    JSplitPane depthChartAndGraphPane = new JSplitPane();
                    depthChartAndGraphPane.setDividerLocation((int)(MySysUtil.getViewerWidth()*0.15));
                    depthChartAndGraphPane.setDividerSize(5);
                    depthChartAndGraphPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
                    depthChartAndGraphPane.setLeftComponent(dataProfilePanel);
                    depthChartAndGraphPane.setRightComponent(MyVars.app.getGraphViewerPanel());

                    JSplitPane dashBoardSplitPane = new JSplitPane();
                    dashBoardSplitPane.setDividerSize(4);
                    dashBoardSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
                    dashBoardSplitPane.setTopComponent(depthChartAndGraphPane);
                    dashBoardSplitPane.setBottomComponent(graphProfilePanel);
                    dashBoardSplitPane.setDividerLocation((int)(MySysUtil.getViewerHeight()*0.69));

                    add(dashBoardSplitPane, BorderLayout.CENTER);
                    MyVars.app.addComponentListener(new ComponentAdapter() {
                        public void componentResized(ComponentEvent evt) {
                            dashBoardSplitPane.setDividerSize(4);
                            dashBoardSplitPane.setDividerLocation((int)(MySysUtil.getViewerHeight()*0.69));

                            depthChartAndGraphPane.setDividerSize(5);
                            depthChartAndGraphPane.setDividerLocation((int)(MySysUtil.getViewerWidth()*0.15));
                        }
                    });
                    TitledBorder nodeDepthChartPanelBorder = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "DATA PROFILE BY DEPTH", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, MyVars.tahomaBoldFont11, Color.DARK_GRAY);
                    dataProfilePanel.setBorder(nodeDepthChartPanelBorder);

                    TitledBorder graphValueDistributionChartPanelBorder = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "GRAPH VALUE PROFILE", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, MyVars.tahomaBoldFont11, Color.DARK_GRAY);
                    graphProfilePanel.setBorder(graphValueDistributionChartPanelBorder);
                } else {
                    graphLevelAverageValuesByDepthLineChart = new MyGraphLevelAverageValuesByDepthLineChart();
                    graphLevelUniqueNodesByDepthLineChart = new MyGraphLevelUniqueNodesByDepthLineBarChart();
                    graphLevelContributionByDepthLineChart = new MyGraphLevelContributionByDepthLineChart();
                    graphLevelShortestAvgPathLengthDistributionLineChart = new MyGraphLevelAverageShortestPathLengthDistributionLineChart();
                    graphLevelPredecessorSuccessorByDepthLineChart = new MyGraphLevelPredecessorSuccessorByDepthLineChart();

                    JPanel dataProfilePanel = new JPanel();
                    dataProfilePanel.setBackground(Color.WHITE);
                    dataProfilePanel.setLayout(new GridLayout(4, 1));
                    dataProfilePanel.add(graphLevelAverageValuesByDepthLineChart);
                    dataProfilePanel.add(graphLevelUniqueNodesByDepthLineChart);
                    dataProfilePanel.add(graphLevelContributionByDepthLineChart);
                    dataProfilePanel.add(graphLevelPredecessorSuccessorByDepthLineChart);

                    JPanel graphProfilePanel = new JPanel();
                    graphProfilePanel.setBackground(Color.WHITE);
                    graphProfilePanel.setLayout(new GridLayout(1, 6));

                    graphLevelSuccessorCountDistributionLineGraph = new MyGraphLevelSuccessorCountDistributionLineChart();
                    graphLevelPredecessorCountDistributionLineGraph = new MyGraphLevelPredecessorCountDistributionLineChart();
                    graphLevelUniqueContributionDistributionLineChart = new MyGraphLevelUniqueContributionDistributionLineChart();
                    graphLevelValueDistributionLineChart = new MyGraphLevelValueDistributionLineChart();
                    graphLevelNodeCountDistributionLineChart = new MyGraphLevelNodeCountDistributionLineChart();

                    graphProfilePanel.add(graphLevelNodeCountDistributionLineChart);
                    graphProfilePanel.add(graphLevelShortestAvgPathLengthDistributionLineChart);
                    graphProfilePanel.add(graphLevelUniqueContributionDistributionLineChart);
                    graphProfilePanel.add(graphLevelPredecessorCountDistributionLineGraph);
                    graphProfilePanel.add(graphLevelSuccessorCountDistributionLineGraph);
                    graphProfilePanel.add(graphLevelValueDistributionLineChart);

                    JSplitPane depthChartAndGraphPane = new JSplitPane();
                    depthChartAndGraphPane.setDividerLocation((int)(MySysUtil.getViewerWidth()*0.15));
                    depthChartAndGraphPane.setDividerSize(4);
                    depthChartAndGraphPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
                    depthChartAndGraphPane.setLeftComponent(dataProfilePanel);
                    depthChartAndGraphPane.setRightComponent(MyVars.app.getGraphViewerPanel());

                    JSplitPane dashBoardSplitPane = new JSplitPane();
                    dashBoardSplitPane.setDividerLocation((int)(MySysUtil.getViewerHeight()*0.69));
                    dashBoardSplitPane.setDividerSize(5);
                    dashBoardSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
                    dashBoardSplitPane.setTopComponent(depthChartAndGraphPane);
                    dashBoardSplitPane.setBottomComponent(graphProfilePanel);

                    add(dashBoardSplitPane, BorderLayout.CENTER);
                    MyVars.app.addComponentListener(new ComponentAdapter() {
                        public void componentResized(ComponentEvent evt) {
                            dashBoardSplitPane.setDividerSize(4);
                            dashBoardSplitPane.setDividerLocation((int) (MySysUtil.getViewerHeight()*0.69));

                            depthChartAndGraphPane.setDividerSize(5);
                            depthChartAndGraphPane.setDividerLocation((int) (MySysUtil.getViewerWidth()*0.15));
                        }
                    });

                    TitledBorder nodeDepthChartPanelBorder = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "DATA PROFILE BY DEPTH", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, MyVars.tahomaBoldFont11, Color.DARK_GRAY);
                    dataProfilePanel.setBorder(nodeDepthChartPanelBorder);

                    TitledBorder graphValueDistributionChartPanelBorder = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "GRAPH VALUE PROFILE", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, MyVars.tahomaBoldFont11, Color.DARK_GRAY);
                    graphProfilePanel.setBorder(graphValueDistributionChartPanelBorder);

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
                if (MyVars.isTimeOn) {
                    depthLevelPredecessorsAndSuccessorsByDepthLineChart = new MyDepthLevelPredecessorsAndSuccessorsByDepthLineChart();
                    graphLevelAverageValuesByDepthLineChart = new MyGraphLevelAverageValuesByDepthLineChart();
                    graphLevelReachTimeByDepthLineChart = new MyGraphLevelReachTimeByDepthLineChart();
                    graphLevelUniqueNodesByDepthLineChart = new MyGraphLevelUniqueNodesByDepthLineBarChart();
                    depthLevelNodeHopCountDistribution = new MyDepthLevelHopCountDistribution();
                    graphLevelContributionByDepthLineChart = new MyGraphLevelContributionByDepthLineChart();
                    depthLevelNodeValueHistogramDistributionLineChart = new MyDepthLevelNodeValueHistogramDistributionLineChart();
                    depthLevelEndingNodeValueHistogramDistributionLineChart = new MyDepthLevelEndingNodeValueHistogramDistributionLineChart();

                    JPanel dataProfilePanel = new JPanel();
                    dataProfilePanel.setBackground(Color.WHITE);
                    dataProfilePanel.setLayout(new GridLayout(6, 1));
                    dataProfilePanel.add(graphLevelAverageValuesByDepthLineChart);
                    dataProfilePanel.add(graphLevelUniqueNodesByDepthLineChart);
                    dataProfilePanel.add(depthLevelPredecessorsAndSuccessorsByDepthLineChart);
                    dataProfilePanel.add(graphLevelReachTimeByDepthLineChart);
                    dataProfilePanel.add(graphLevelContributionByDepthLineChart);
                    dataProfilePanel.add(depthLevelEndingNodeValueHistogramDistributionLineChart);

                    JPanel graphProfilePanel = new JPanel();
                    graphProfilePanel.setBackground(Color.WHITE);
                    graphProfilePanel.setLayout(new GridLayout(1, 4));

                    depthLevelNodeCurrentValueDistribution = new MyDepthLevelCurrentValueDistribution();
                    depthLevelEdgeCurrentValueDistribution = new MyDepthLevelEdgeCurrentValueDistribution();
                    depthLevelNodeHopCountDistribution = new MyDepthLevelHopCountDistribution();

                    graphProfilePanel.add(depthLevelNodeHopCountDistribution);
                    graphProfilePanel.add(depthLevelNodeCurrentValueDistribution);
                    graphProfilePanel.add(depthLevelEdgeCurrentValueDistribution);
                    graphProfilePanel.add(depthLevelNodeValueHistogramDistributionLineChart);

                    JSplitPane depthChartAndGraphPane = new JSplitPane();
                    depthChartAndGraphPane.setDividerLocation((int)(MySysUtil.getViewerWidth()*0.15));
                    depthChartAndGraphPane.setDividerSize(4);
                    depthChartAndGraphPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
                    depthChartAndGraphPane.setLeftComponent(dataProfilePanel);
                    depthChartAndGraphPane.setRightComponent(MyVars.app.getGraphViewerPanel());

                    JSplitPane dashBoardSplitPane = new JSplitPane();
                    dashBoardSplitPane.setDividerSize(5);
                    dashBoardSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
                    dashBoardSplitPane.setTopComponent(depthChartAndGraphPane);
                    dashBoardSplitPane.setBottomComponent(graphProfilePanel);
                    dashBoardSplitPane.setDividerLocation((int)(MySysUtil.getViewerHeight()*0.69));

                    add(dashBoardSplitPane, BorderLayout.CENTER);
                    MyVars.app.addComponentListener(new ComponentAdapter() {
                        public void componentResized(ComponentEvent evt) {
                            dashBoardSplitPane.setDividerSize(4);
                            dashBoardSplitPane.setDividerLocation((int) (MySysUtil.getViewerHeight()*0.69));

                            depthChartAndGraphPane.setDividerSize(5);
                            depthChartAndGraphPane.setDividerLocation((int) (MySysUtil.getViewerWidth()*0.15));
                        }
                    });

                    TitledBorder nodeDepthChartPanelBorder = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "DATA PROFILE BY DEPTH", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, MyVars.tahomaBoldFont11, Color.DARK_GRAY);
                    dataProfilePanel.setBorder(nodeDepthChartPanelBorder);

                    TitledBorder graphValueDistributionChartPanelBorder = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "GRAPH VALUE DISTRIBUTIONS", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, MyVars.tahomaBoldFont11, Color.DARK_GRAY);
                    graphProfilePanel.setBorder(graphValueDistributionChartPanelBorder);

                } else {
                    graphLevelAverageValuesByDepthLineChart = new MyGraphLevelAverageValuesByDepthLineChart();
                    graphLevelUniqueNodesByDepthLineChart = new MyGraphLevelUniqueNodesByDepthLineBarChart();
                    depthLevelNodeHopCountDistribution = new MyDepthLevelHopCountDistribution();
                    graphLevelContributionByDepthLineChart = new MyGraphLevelContributionByDepthLineChart();
                    graphLevelNodeCountDistributionLineChart = new MyGraphLevelNodeCountDistributionLineChart();
                    depthLevelEndingNodeValueHistogramDistributionLineChart = new MyDepthLevelEndingNodeValueHistogramDistributionLineChart();

                    JPanel dataProfilePanel = new JPanel();
                    dataProfilePanel.setBackground(Color.WHITE);
                    dataProfilePanel.setLayout(new GridLayout(5, 1));
                    dataProfilePanel.add(graphLevelAverageValuesByDepthLineChart);
                    dataProfilePanel.add(graphLevelUniqueNodesByDepthLineChart);
                    dataProfilePanel.add(graphLevelContributionByDepthLineChart);
                    dataProfilePanel.add(graphLevelNodeCountDistributionLineChart);
                    dataProfilePanel.add(depthLevelEndingNodeValueHistogramDistributionLineChart);

                    JPanel graphProfilePanel = new JPanel();
                    graphProfilePanel.setBackground(Color.WHITE);
                    graphProfilePanel.setLayout(new GridLayout(1, 3));

                    depthLevelNodeCurrentValueDistribution = new MyDepthLevelCurrentValueDistribution();
                    depthLevelEdgeCurrentValueDistribution = new MyDepthLevelEdgeCurrentValueDistribution();
                    depthLevelNodeHopCountDistribution = new MyDepthLevelHopCountDistribution();

                    JSplitPane depthChartAndGraphPane = new JSplitPane();
                    depthChartAndGraphPane.setDividerLocation((int)(MySysUtil.getViewerWidth()*0.15));
                    depthChartAndGraphPane.setDividerSize(4);
                    depthChartAndGraphPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
                    depthChartAndGraphPane.setLeftComponent(dataProfilePanel);
                    depthChartAndGraphPane.setRightComponent(MyVars.app.getGraphViewerPanel());

                    JSplitPane dashBoardSplitPane = new JSplitPane();
                    dashBoardSplitPane.setDividerLocation((int)(MySysUtil.getViewerHeight()*0.69));
                    dashBoardSplitPane.setDividerSize(5);
                    dashBoardSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
                    dashBoardSplitPane.setTopComponent(depthChartAndGraphPane);
                    dashBoardSplitPane.setBottomComponent(graphProfilePanel);

                    add(dashBoardSplitPane, BorderLayout.CENTER);
                    MyVars.app.addComponentListener(new ComponentAdapter() {
                        public void componentResized(ComponentEvent evt) {
                            dashBoardSplitPane.setDividerSize(4);
                            dashBoardSplitPane.setDividerLocation((int)(MySysUtil.getViewerHeight()*0.69));

                            depthChartAndGraphPane.setDividerSize(5);
                            depthChartAndGraphPane.setDividerLocation((int)(MySysUtil.getViewerWidth()*0.15));
                        }
                    });

                    TitledBorder nodeDepthChartPanelBorder = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "DATA PROFILE BY DEPTH", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, MyVars.tahomaBoldFont11, Color.DARK_GRAY);
                    dataProfilePanel.setBorder(nodeDepthChartPanelBorder);

                    TitledBorder graphValueDistributionChartPanelBorder = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "GRAPH VALUE DISTRIBUTIONS", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, MyVars.tahomaBoldFont11, Color.DARK_GRAY);
                    graphProfilePanel.setBorder(graphValueDistributionChartPanelBorder);
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
                MyMultiLevelPredecessorValueHistogramDistributionLineChart multiLevelPredecessorValueHistogramDistributionLineChart = new MyMultiLevelPredecessorValueHistogramDistributionLineChart();
                MyMultiNodeSuccessorValueHistogramDistributionLineChart multiNodeSuccessorValueHistogramDistributionLineChart = new MyMultiNodeSuccessorValueHistogramDistributionLineChart();
                MyMultiLevelEndingNodeValueHistogramDistributionLineChart multiLevelEndingNodeValueHistogramDistributionLineChart = new MyMultiLevelEndingNodeValueHistogramDistributionLineChart();

                JPanel dataProfilePanel = new JPanel();
                dataProfilePanel.setBackground(Color.WHITE);
                dataProfilePanel.setLayout(new GridLayout(5, 1));
                dataProfilePanel.add(sharedNodeLevelSelectedNodesByDepthLineChart);
                dataProfilePanel.add(multiLevelPredecessorsAndSuccessorsByDepthLineChart);
                dataProfilePanel.add(sharedNodeLevelSharedSuccessorAppearancesByDepthLineChart);
                dataProfilePanel.add(sharedNodeLevelSharedPredecessorAppearancesByDepthLineChart);
                dataProfilePanel.add(multiLevelEndingNodeValueHistogramDistributionLineChart);

                JPanel graphProfilePanel = new JPanel();
                graphProfilePanel.setBackground(Color.WHITE);
                graphProfilePanel.setLayout(new GridLayout(1, 7));

                multiNodePredecessorValueDistributionLineChart = new MyMultiNodePredecessorValueDistributionLineChart();
                multiNodeSuccessorValueDistributionLineChart = new MyMultiNodeSuccessorValueDistributionLineChart();
                multiNodeSharedPredecessorEdgeValueDistributionLineChart = new MyMultiNodeSharedPredecessorEdgeValueDistributionLineChart();
                multiNodeSharedSuccessorEdgeValueDistributionLineChart = new MyMultiNodeSharedSuccessorEdgeValueDistributionLineChart();
                multiNodeSelectedNodeValueDistributionLineChart = new MyMultiLevelSelectedNodeValueDistributionLineChart();

                graphProfilePanel.add(multiNodePredecessorValueDistributionLineChart);
                graphProfilePanel.add(multiNodeSuccessorValueDistributionLineChart);
                graphProfilePanel.add(multiNodeSharedPredecessorEdgeValueDistributionLineChart);
                graphProfilePanel.add(multiNodeSharedSuccessorEdgeValueDistributionLineChart);
                graphProfilePanel.add(multiLevelPredecessorValueHistogramDistributionLineChart);
                graphProfilePanel.add(multiNodeSuccessorValueHistogramDistributionLineChart);
                graphProfilePanel.add(multiNodeSelectedNodeValueDistributionLineChart);

                JSplitPane depthChartAndGraphPane = new JSplitPane();
                depthChartAndGraphPane.setDividerLocation((int) (MySysUtil.getViewerWidth()*0.15));
                depthChartAndGraphPane.setDividerSize(5);
                depthChartAndGraphPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
                depthChartAndGraphPane.setLeftComponent(dataProfilePanel);
                depthChartAndGraphPane.setRightComponent(MyVars.app.getGraphViewerPanel());

                JSplitPane dashBoardSplitPane = new JSplitPane();
                dashBoardSplitPane.setDividerLocation((int) (MySysUtil.getViewerHeight()*0.69));
                dashBoardSplitPane.setDividerSize(5);
                dashBoardSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
                dashBoardSplitPane.setTopComponent(depthChartAndGraphPane);
                dashBoardSplitPane.setBottomComponent(graphProfilePanel);

                add(dashBoardSplitPane, BorderLayout.CENTER);
                MyVars.app.addComponentListener(new ComponentAdapter() {
                    public void componentResized(ComponentEvent evt) {
                        dashBoardSplitPane.setDividerSize(5);
                        dashBoardSplitPane.setDividerLocation((int) (MySysUtil.getViewerHeight()*0.69));

                        depthChartAndGraphPane.setDividerSize(4);
                        depthChartAndGraphPane.setDividerLocation((int) (MySysUtil.getViewerWidth()*0.15));
                    }
                });

                TitledBorder nodeDepthChartPanelBorder = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "DATA PROFILE BY DEPTH", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, MyVars.tahomaBoldFont11, Color.DARK_GRAY);
                dataProfilePanel.setBorder(nodeDepthChartPanelBorder);

                TitledBorder graphValueDistributionChartPanelBorder = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "GRAPH VALUE DISTRIBUTIONS", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, MyVars.tahomaBoldFont11, Color.DARK_GRAY);
                graphProfilePanel.setBorder(graphValueDistributionChartPanelBorder);
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

                if (MyVars.isTimeOn) {
                    MySingleNodeAverageValuesByDepthLineChart singleNodeAverageValuesByDepthLineChart = new MySingleNodeAverageValuesByDepthLineChart();
                    MySingleNodeContributionByDepthLineChart singleNodeNodeContributionByDepthLineChart = new MySingleNodeContributionByDepthLineChart();
                    MySingleNodeReachTimeByDepthLineChart singleNodeReachTimeByDepthLineChart = new MySingleNodeReachTimeByDepthLineChart();
                    MySingleNodeHopCountDistributionLineChart singleNodeHopCountDistribution = new MySingleNodeHopCountDistributionLineChart();
                    MySingleNodePredecessorsAndSuccessorsByDepthLineChart singleNodePredecessorsAndSuccessorsByDepthLineChart = new MySingleNodePredecessorsAndSuccessorsByDepthLineChart();
                    MySingleNodeSuccessorValueHistogramDistributionLineChart singleNodeSuccessorValueHistogramDistributionLineChart = new MySingleNodeSuccessorValueHistogramDistributionLineChart();
                    MySingleNodePredecessorValueHistogramDistributionLineChart singleNodePredecessorValueHistogramDistributionLineChart = new MySingleNodePredecessorValueHistogramDistributionLineChart();
                    MySingleNodeEndingNodeValueHistogramDistributionLineChart singleNodeEndingNodeValueHistogramDistributionLineChart = new MySingleNodeEndingNodeValueHistogramDistributionLineChart();

                    JPanel dataProfilePanel = new JPanel();
                    dataProfilePanel.setBackground(Color.WHITE);
                    dataProfilePanel.setLayout(new GridLayout(5, 1));

                    dataProfilePanel.add(singleNodeAverageValuesByDepthLineChart);
                    dataProfilePanel.add(singleNodePredecessorsAndSuccessorsByDepthLineChart);
                    dataProfilePanel.add(singleNodeReachTimeByDepthLineChart);
                    dataProfilePanel.add(singleNodeNodeContributionByDepthLineChart);
                    dataProfilePanel.add(singleNodeEndingNodeValueHistogramDistributionLineChart);

                    JPanel graphProfilePanel = new JPanel();
                    graphProfilePanel.setBackground(Color.WHITE);
                    graphProfilePanel.setLayout(new GridLayout(1, 7));

                    singleNodePredecessorEdgeCurrentValueDistributionLineChart = new MySingleNodePredecessorEdgeValueDistributionLineChart();
                    singleNodeSuccessorEdgeCurrentValueDistributionLineChart = new MySingleNodeSuccessorEdgeValueDistributionLineChart();
                    singleNodePredecessorValueDistributionLineGraph = new MySingleNodePredecessorValueDistributionLineChart();
                    singleNodeSuccessorValueDistributionLineGraph = new MySingleNodeSuccessorValueDistributionLineChart();

                    graphProfilePanel.add(singleNodeHopCountDistribution);
                    graphProfilePanel.add(singleNodePredecessorValueDistributionLineGraph);
                    graphProfilePanel.add(singleNodeSuccessorValueDistributionLineGraph);
                    graphProfilePanel.add(singleNodePredecessorEdgeCurrentValueDistributionLineChart);
                    graphProfilePanel.add(singleNodeSuccessorEdgeCurrentValueDistributionLineChart);
                    graphProfilePanel.add(singleNodePredecessorValueHistogramDistributionLineChart);
                    graphProfilePanel.add(singleNodeSuccessorValueHistogramDistributionLineChart);

                    JSplitPane depthChartAndGraphPane = new JSplitPane();
                    depthChartAndGraphPane.setDividerLocation((int)(MySysUtil.getViewerWidth()*0.15));
                    depthChartAndGraphPane.setDividerSize(5);
                    depthChartAndGraphPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
                    depthChartAndGraphPane.setLeftComponent(dataProfilePanel);
                    depthChartAndGraphPane.setRightComponent(MyVars.app.getGraphViewerPanel());

                    JSplitPane dashBoardSplitPane = new JSplitPane();
                    dashBoardSplitPane.setDividerLocation((int)(MySysUtil.getViewerHeight()*0.69));
                    dashBoardSplitPane.setDividerSize(5);
                    dashBoardSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
                    dashBoardSplitPane.setTopComponent(depthChartAndGraphPane);
                    dashBoardSplitPane.setBottomComponent(graphProfilePanel);

                    add(dashBoardSplitPane, BorderLayout.CENTER);
                    MyVars.app.addComponentListener(new ComponentAdapter() {
                        public void componentResized(ComponentEvent evt) {
                            dashBoardSplitPane.setDividerSize(5);
                            dashBoardSplitPane.setDividerLocation((int)(MySysUtil.getViewerHeight()*0.69));

                            depthChartAndGraphPane.setDividerSize(4);
                            depthChartAndGraphPane.setDividerLocation((int)(MySysUtil.getViewerWidth()*0.15));
                        }
                    });

                    TitledBorder nodeDepthChartPanelBorder = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "DATA PROFILE BY DEPTH", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, MyVars.tahomaBoldFont11, Color.DARK_GRAY);
                    dataProfilePanel.setBorder(nodeDepthChartPanelBorder);

                    TitledBorder graphValueDistributionChartPanelBorder = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "GRAPH VALUE DISTRIBUTIONS", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, MyVars.tahomaBoldFont11, Color.DARK_GRAY);
                    graphProfilePanel.setBorder(graphValueDistributionChartPanelBorder);
                } else {
                    MySingleNodeAverageValuesByDepthLineChart singleNodeLevelAverageValuesByDepthLineChart = new MySingleNodeAverageValuesByDepthLineChart();
                    MySingleNodeContributionByDepthLineChart singleNodeLevelNodeContributionByDepthLineChart = new MySingleNodeContributionByDepthLineChart();
                    MySingleNodeHopCountDistributionLineChart singleNodeLevelNodeHopCountDistributionLineChart = new MySingleNodeHopCountDistributionLineChart();
                    MySingleNodePredecessorsAndSuccessorsByDepthLineChart singleNodePredecessorsAndSuccessorsByDepthLineChart = new MySingleNodePredecessorsAndSuccessorsByDepthLineChart();
                    MySingleNodeSuccessorValueHistogramDistributionLineChart singleNodeSuccessorValueHistogramDistributionLineChart = new MySingleNodeSuccessorValueHistogramDistributionLineChart();
                    MySingleNodePredecessorValueHistogramDistributionLineChart singleNodePredecessorValueHistogramDistributionLineChart = new MySingleNodePredecessorValueHistogramDistributionLineChart();
                    MySingleNodeEndingNodeValueHistogramDistributionLineChart singleNodeEndingNodeValueHistogramDistributionLineChart = new MySingleNodeEndingNodeValueHistogramDistributionLineChart();

                    JPanel dataProfilePanel = new JPanel();
                    dataProfilePanel.setBackground(Color.WHITE);
                    dataProfilePanel.setLayout(new GridLayout(4, 1));
                    dataProfilePanel.add(singleNodeLevelAverageValuesByDepthLineChart);
                    dataProfilePanel.add(singleNodePredecessorsAndSuccessorsByDepthLineChart);
                    dataProfilePanel.add(singleNodeLevelNodeContributionByDepthLineChart);
                    dataProfilePanel.add(singleNodeEndingNodeValueHistogramDistributionLineChart);

                    JPanel graphProfilePanel = new JPanel();
                    graphProfilePanel.setBackground(Color.WHITE);
                    graphProfilePanel.setLayout(new GridLayout(1, 8));

                    singleNodePredecessorValueDistributionLineGraph = new MySingleNodePredecessorValueDistributionLineChart();
                    singleNodeSuccessorValueDistributionLineGraph = new MySingleNodeSuccessorValueDistributionLineChart();
                    singleNodePredecessorEdgeCurrentValueDistributionLineChart = new MySingleNodePredecessorEdgeValueDistributionLineChart();
                    singleNodeSuccessorEdgeCurrentValueDistributionLineChart = new MySingleNodeSuccessorEdgeValueDistributionLineChart();

                    graphProfilePanel.add(singleNodeLevelNodeHopCountDistributionLineChart);
                    graphProfilePanel.add(singleNodePredecessorValueDistributionLineGraph);
                    graphProfilePanel.add(singleNodePredecessorValueDistributionLineGraph);
                    graphProfilePanel.add(singleNodePredecessorEdgeCurrentValueDistributionLineChart);
                    graphProfilePanel.add(singleNodeSuccessorEdgeCurrentValueDistributionLineChart);
                    graphProfilePanel.add(singleNodePredecessorValueHistogramDistributionLineChart);
                    graphProfilePanel.add(singleNodeSuccessorValueHistogramDistributionLineChart);
                    graphProfilePanel.add(singleNodeEndingNodeValueHistogramDistributionLineChart);

                    JSplitPane depthChartAndGraphPane = new JSplitPane();
                    depthChartAndGraphPane.setDividerLocation((int)(MySysUtil.getViewerWidth()*0.15));
                    depthChartAndGraphPane.setDividerSize(5);
                    depthChartAndGraphPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
                    depthChartAndGraphPane.setLeftComponent(dataProfilePanel);
                    depthChartAndGraphPane.setRightComponent(MyVars.app.getGraphViewerPanel());

                    JSplitPane dashBoardSplitPane = new JSplitPane();
                    dashBoardSplitPane.setDividerLocation((int)(MySysUtil.getViewerHeight()*0.69));
                    dashBoardSplitPane.setDividerSize(5);
                    dashBoardSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
                    dashBoardSplitPane.setTopComponent(depthChartAndGraphPane);
                    dashBoardSplitPane.setBottomComponent(graphProfilePanel);

                    add(dashBoardSplitPane, BorderLayout.CENTER);
                    MyVars.app.addComponentListener(new ComponentAdapter() {
                        public void componentResized(ComponentEvent evt) {
                            dashBoardSplitPane.setDividerSize(5);
                            dashBoardSplitPane.setDividerLocation((int)(MySysUtil.getViewerHeight()*0.69));

                            depthChartAndGraphPane.setDividerSize(4);
                            depthChartAndGraphPane.setDividerLocation((int)(MySysUtil.getViewerWidth()*0.15));
                        }
                    });

                    TitledBorder nodeDepthChartPanelBorder = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "DATA PROFILE BY DEPTH", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, MyVars.tahomaBoldFont11, Color.DARK_GRAY);
                    dataProfilePanel.setBorder(nodeDepthChartPanelBorder);

                    TitledBorder graphValueDistributionChartPanelBorder = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "GRAPH VALUE DISTRIBUTIONS", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, MyVars.tahomaBoldFont11, Color.DARK_GRAY);
                    graphProfilePanel.setBorder(graphValueDistributionChartPanelBorder);
                }
                revalidate();
                repaint();
            }
        });
    }
}