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

    // Graph Level with Time condition on.
    public MyGraphLevelEdgeValueDistributionLineChart graphLevelEdgeValueDistributionLineGraph;
    public MyGraphLevelNodeValueDistributionLineChart graphLevelNodeValueDistributionLineGraph;
    public MyGraphLevelSuccessorCountDistributionLineChart graphLevelSuccessorCountDistributionLineGraph;
    public MyGraphLevelPredecessorCountDistributionLineChart graphLevelPredecessorCountDistributionLineGraph;
    public MyGraphLevelNodeAverageHopCountDistributionLineChart graphLevelNodeHopCountDistributionLineGraph;
    public MyGraphLevelDistributionLineChart graphLevelNodeCountDistributionLineChart;
    public MyGraphLevelContributionByDepthLineChart graphLevelContributionByDepthLineChart;
    public MyGraphLevelUniqueNodesByDepthLineBarChart graphLevelUniqueNodesByDepthLineChart;
    public MyGraphLevelReachTimeByDepthLineChart graphLevelReachTimeByDepthLineChart;
    public MyGraphLevelDurationByDepthLineChart graphLevelDurationByDepthLineChart;
    public MyGraphLevelNodeUniqueContributionDistributionLineChart graphLevelNodeUniqueContributionDistributionLineChart;
    public MyGraphLevelAverageDistributionLineChart graphLevelAvgShortestDistanceDistributionLineChart;
    public MyGraphLevelPredecessorSuccessorByDepthLineChart graphLevelPredecessorSuccessorByDepthLineChart;
    public MyGraphLevelEdgeUniqueContributionDistributionLineChart graphLevelEdgeUniqueContributionDistributionLineChart;

    public MyGraphLevelAverageValuesByDepthLineChart graphLevelAverageValuesByDepthLineChart;
    public MyGraphLevelLabelDistributionLineChart graphLevelLabelValueDistributionLineChart;
    public MyGraphLevelValueDistributionLineChart graphLevelValueDistributionLineChart;


    // Node Level with time condition on for selected node.
    public MySingleNodePredecessorEdgeValueDistributionLineChart singleNodePredecessorEdgeCurrentValueDistributionLineChart;
    public MySingleNodeSuccessorEdgeValueDistributionLineChart singleNodeSuccessorEdgeCurrentValueDistributionLineChart;
    public MySingleNodeSuccessorValueDistributionLineChart singleNodeSuccessorValueDistributionLineGraph;
    public MySingleNodePredecessorValueDistributionLineChart singleNodePredecessorValueDistributionLineGraph;

    // Graph Level Depth Charts with time condition on.
    public MyDepthLevelCurrentValueDistribution depthLevelNodeCurrentValueDistribution;
    public MyDepthLevelHopCountDistribution depthLevelNodeHopCountDistribution;
    public MyDepthLevelEdgeValueDistribution depthLevelEdgeCurrentValueDistribution;
    public MyDepthLevelPredecessorsAndSuccessorsByDepthLineChart depthLevelPredecessorsAndSuccessorsByDepthLineChart;
    public MyDepthLevelNodeValueHistogramDistributionLineChart depthLevelNodeValueHistogramDistributionLineChart;
    public MyDepthLevelEndingNodeValueHistogramDistributionLineChart depthLevelEndingNodeValueHistogramDistributionLineChart;
    public MyDepthLevelStartingNodeValueHistogramDistributionLineChart depthLevelStartingNodeValueHistogramDistributionLineChart;

    public MyMultiNodeSuccessorValueDistributionLineChart multiNodeSuccessorValueDistributionLineChart;
    public MyMultiNodePredecessorValueDistributionLineChart multiNodePredecessorValueDistributionLineChart;
    public MyMultiNodeSharedPredecessorEdgeValueDistributionLineChart multiNodeSharedPredecessorEdgeValueDistributionLineChart;
    public MyMultiNodeSharedSuccessorEdgeValueDistributionLineChart multiNodeSharedSuccessorEdgeValueDistributionLineChart;
    public MyMultiLevelNodeValueDistributionLineChart multiNodeSelectedNodeValueDistributionLineChart;

    public MySequentialGraphDashBoard() {}
    public void setDashboard() {
        setGraphLevelDashBoard();
    }

    TitledBorder titledBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK),
        "DATA PROFILE", TitledBorder.LEFT, TitledBorder.TOP, MySequentialGraphVars.tahomaBoldFont14);

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
                    dataProfilePanel.setBorder(titledBorder);

                    JPanel contentPanel = new JPanel();
                    contentPanel.setBackground(Color.WHITE);
                    contentPanel.setLayout(new BorderLayout(3,3));
                    contentPanel.add(MySequentialGraphVars.app.getSequentialGraphViewerPanel(), BorderLayout.CENTER);

                    JSplitPane graphAndGraphChartSplitPane = new JSplitPane();
                    graphAndGraphChartSplitPane.setDividerLocation((int)(MySequentialGraphSysUtil.getViewerWidth()*0.145));
                    graphAndGraphChartSplitPane.setDividerSize(5);
                    graphAndGraphChartSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
                    graphAndGraphChartSplitPane.setLeftComponent(dataProfilePanel);
                    graphAndGraphChartSplitPane.setRightComponent(contentPanel);

                    add(graphAndGraphChartSplitPane);

                    MySequentialGraphVars.app.addComponentListener(new ComponentAdapter() {
                        public void componentResized(ComponentEvent evt) {
                            graphAndGraphChartSplitPane.setDividerSize(5);
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
                    dataProfilePanel.setBorder(titledBorder);

                    JSplitPane graphAndGraphChartSplitPane = new JSplitPane();
                    graphAndGraphChartSplitPane.setDividerLocation((int)(MySequentialGraphSysUtil.getViewerWidth()*0.145));
                    graphAndGraphChartSplitPane.setDividerSize(5);
                    graphAndGraphChartSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
                    graphAndGraphChartSplitPane.setLeftComponent(dataProfilePanel);
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
                    dataProfilePanel.setBorder(titledBorder);

                    /**
                    JPanel graphProfilePanel = new JPanel();
                    graphProfilePanel.setBackground(Color.WHITE);
                    graphProfilePanel.setLayout(new GridLayout(1, 6));

                    depthLevelNodeCurrentValueDistribution = new MyDepthLevelCurrentValueDistribution();
                    depthLevelEdgeCurrentValueDistribution = new MyDepthLevelEdgeValueDistribution();
                    depthLevelNodeHopCountDistribution = new MyDepthLevelHopCountDistribution();
                    depthLevelStartingNodeValueHistogramDistributionLineChart = new MyDepthLevelStartingNodeValueHistogramDistributionLineChart();
                    depthLevelEndingNodeValueHistogramDistributionLineChart = new MyDepthLevelEndingNodeValueHistogramDistributionLineChart();

                    graphProfilePanel.add(depthLevelStartingNodeValueHistogramDistributionLineChart);
                    graphProfilePanel.add(depthLevelEndingNodeValueHistogramDistributionLineChart);
                    graphProfilePanel.add(depthLevelNodeHopCountDistribution);
                    graphProfilePanel.add(depthLevelNodeCurrentValueDistribution);
                    graphProfilePanel.add(depthLevelEdgeCurrentValueDistribution);
                    graphProfilePanel.add(depthLevelNodeValueHistogramDistributionLineChart);
                     */

                    JSplitPane graphAndGraphChartSplitPane = new JSplitPane();
                    graphAndGraphChartSplitPane.setDividerLocation((int)(MySequentialGraphSysUtil.getViewerWidth()*0.145));
                    graphAndGraphChartSplitPane.setDividerSize(5);
                    graphAndGraphChartSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
                    graphAndGraphChartSplitPane.setLeftComponent(dataProfilePanel);
                    graphAndGraphChartSplitPane.setRightComponent(MySequentialGraphVars.app.getSequentialGraphViewerPanel());

                    add(graphAndGraphChartSplitPane, BorderLayout.CENTER);
                    MySequentialGraphVars.app.addComponentListener(new ComponentAdapter() {
                        public void componentResized(ComponentEvent evt) {
                            //dashBoardSplitPane.setDividerSize(4);
                            //dashBoardSplitPane.setDividerLocation((int) (MySequentialGraphSysUtil.getViewerWidth() * 0.14));

                            graphAndGraphChartSplitPane.setDividerSize(5);
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
                    dataProfilePanel.setBorder(titledBorder);

                    /**
                    JPanel graphProfilePanel = new JPanel();
                    graphProfilePanel.setBackground(Color.WHITE);
                    graphProfilePanel.setLayout(new GridLayout(1, 3));

                    depthLevelNodeCurrentValueDistribution = new MyDepthLevelCurrentValueDistribution();
                    depthLevelEdgeCurrentValueDistribution = new MyDepthLevelEdgeValueDistribution();
                    depthLevelNodeHopCountDistribution = new MyDepthLevelHopCountDistribution();
                    depthLevelStartingNodeValueHistogramDistributionLineChart = new MyDepthLevelStartingNodeValueHistogramDistributionLineChart();
                    depthLevelEndingNodeValueHistogramDistributionLineChart = new MyDepthLevelEndingNodeValueHistogramDistributionLineChart();

                    graphProfilePanel.add(depthLevelStartingNodeValueHistogramDistributionLineChart);
                    graphProfilePanel.add(depthLevelEndingNodeValueHistogramDistributionLineChart);
                    graphProfilePanel.add(depthLevelNodeHopCountDistribution);
                    graphProfilePanel.add(depthLevelNodeCurrentValueDistribution);
                    graphProfilePanel.add(depthLevelEdgeCurrentValueDistribution);
                    graphProfilePanel.add(depthLevelNodeValueHistogramDistributionLineChart);
                     */

                    JSplitPane graphAndGraphChartSplitPane = new JSplitPane();
                    graphAndGraphChartSplitPane.setDividerLocation((int)(MySequentialGraphSysUtil.getViewerWidth()*0.145));
                    graphAndGraphChartSplitPane.setDividerSize(5);
                    graphAndGraphChartSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
                    graphAndGraphChartSplitPane.setLeftComponent(dataProfilePanel);
                    graphAndGraphChartSplitPane.setRightComponent(MySequentialGraphVars.app.getSequentialGraphViewerPanel());

                    add(graphAndGraphChartSplitPane, BorderLayout.CENTER);
                    MySequentialGraphVars.app.addComponentListener(new ComponentAdapter() {
                        public void componentResized(ComponentEvent evt) {
                            //dashBoardSplitPane.setDividerSize(4);
                            //dashBoardSplitPane.setDividerLocation((int) (MySequentialGraphSysUtil.getViewerWidth() * 0.14));

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
                dataProfilePanel.setBorder(titledBorder);

                /**
                JPanel graphProfilePanel = new JPanel();
                graphProfilePanel.setBackground(Color.WHITE);
                graphProfilePanel.setLayout(new GridLayout(1, 7));

                multiNodePredecessorValueDistributionLineChart = new MyMultiNodePredecessorValueDistributionLineChart();
                multiNodeSuccessorValueDistributionLineChart = new MyMultiNodeSuccessorValueDistributionLineChart();
                multiNodeSharedPredecessorEdgeValueDistributionLineChart = new MyMultiNodeSharedPredecessorEdgeValueDistributionLineChart();
                multiNodeSharedSuccessorEdgeValueDistributionLineChart = new MyMultiNodeSharedSuccessorEdgeValueDistributionLineChart();
                multiNodeSelectedNodeValueDistributionLineChart = new MyMultiLevelNodeValueDistributionLineChart();

                graphProfilePanel.add(multiNodePredecessorValueDistributionLineChart);
                graphProfilePanel.add(multiNodeSuccessorValueDistributionLineChart);
                graphProfilePanel.add(multiLevelPredecessorValueHistogramDistributionLineChart);
                graphProfilePanel.add(multiNodeSuccessorValueHistogramDistributionLineChart);
                graphProfilePanel.add(multiNodeSharedPredecessorEdgeValueDistributionLineChart);
                graphProfilePanel.add(multiNodeSharedSuccessorEdgeValueDistributionLineChart);
                graphProfilePanel.add(multiNodeSelectedNodeValueDistributionLineChart);
                 */

                JSplitPane graphAndGraphChartSplitPane = new JSplitPane();
                graphAndGraphChartSplitPane.setDividerLocation((int)(MySequentialGraphSysUtil.getViewerWidth()*0.145));
                graphAndGraphChartSplitPane.setDividerSize(5);
                graphAndGraphChartSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
                graphAndGraphChartSplitPane.setLeftComponent(dataProfilePanel);
                graphAndGraphChartSplitPane.setRightComponent(MySequentialGraphVars.app.getSequentialGraphViewerPanel());

                add(graphAndGraphChartSplitPane, BorderLayout.CENTER);
                MySequentialGraphVars.app.addComponentListener(new ComponentAdapter() {
                    public void componentResized(ComponentEvent evt) {
                        //dashBoardSplitPane.setDividerSize(4);
                        //dashBoardSplitPane.setDividerLocation((int) (MySequentialGraphSysUtil.getViewerWidth() * 0.14));

                        graphAndGraphChartSplitPane.setDividerSize(5);
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
                    MySingleNodeHopCountDistributionLineChart singleNodeHopCountDistribution = new MySingleNodeHopCountDistributionLineChart();
                    MySingleNodePredecessorsAndSuccessorsByDepthLineChart singleNodePredecessorsAndSuccessorsByDepthLineChart = new MySingleNodePredecessorsAndSuccessorsByDepthLineChart();
                    MySingleNodeSuccessorValueHistogramDistributionLineChart singleNodeSuccessorValueHistogramDistributionLineChart = new MySingleNodeSuccessorValueHistogramDistributionLineChart();
                    MySingleNodePredecessorValueHistogramDistributionLineChart singleNodePredecessorValueHistogramDistributionLineChart = new MySingleNodePredecessorValueHistogramDistributionLineChart();
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
                    dataProfilePanel.setBorder(titledBorder);

                    /**JPanel graphProfilePanel = new JPanel();
                    graphProfilePanel.setBackground(Color.WHITE);
                    graphProfilePanel.setLayout(new GridLayout(1, 7));

                    singleNodePredecessorEdgeCurrentValueDistributionLineChart = new MySingleNodePredecessorEdgeValueDistributionLineChart();
                    singleNodeSuccessorEdgeCurrentValueDistributionLineChart = new MySingleNodeSuccessorEdgeValueDistributionLineChart();
                    singleNodePredecessorValueDistributionLineGraph = new MySingleNodePredecessorValueDistributionLineChart();
                    singleNodeSuccessorValueDistributionLineGraph = new MySingleNodeSuccessorValueDistributionLineChart();

                    graphProfilePanel.add(singleNodeHopCountDistribution);
                    graphProfilePanel.add(singleNodePredecessorValueDistributionLineGraph);
                    graphProfilePanel.add(singleNodeSuccessorValueDistributionLineGraph);
                    graphProfilePanel.add(singleNodePredecessorValueHistogramDistributionLineChart);
                    graphProfilePanel.add(singleNodeSuccessorValueHistogramDistributionLineChart);
                    graphProfilePanel.add(singleNodePredecessorEdgeCurrentValueDistributionLineChart);
                    graphProfilePanel.add(singleNodeSuccessorEdgeCurrentValueDistributionLineChart);
                    */

                    JSplitPane graphAndGraphChartSplitPane = new JSplitPane();
                    graphAndGraphChartSplitPane.setDividerLocation((int)(MySequentialGraphSysUtil.getViewerWidth()*0.145));
                    graphAndGraphChartSplitPane.setDividerSize(5);
                    graphAndGraphChartSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
                    graphAndGraphChartSplitPane.setLeftComponent(dataProfilePanel);
                    graphAndGraphChartSplitPane.setRightComponent(MySequentialGraphVars.app.getSequentialGraphViewerPanel());

                    add(graphAndGraphChartSplitPane, BorderLayout.CENTER);
                    MySequentialGraphVars.app.addComponentListener(new ComponentAdapter() {
                        public void componentResized(ComponentEvent evt) {
                            //dashBoardSplitPane.setDividerSize(4);
                            //dashBoardSplitPane.setDividerLocation((int) (MySequentialGraphSysUtil.getViewerWidth() * 0.14));

                            graphAndGraphChartSplitPane.setDividerSize(5);
                            graphAndGraphChartSplitPane.setDividerLocation((int)(MySequentialGraphSysUtil.getViewerWidth()*0.145));
                        }
                    });
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
                    dataProfilePanel.setBorder(titledBorder);

                    /**
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
                    graphProfilePanel.add(singleNodeSuccessorValueHistogramDistributionLineChart);
                    graphProfilePanel.add(singleNodeEndingNodeValueHistogramDistributionLineChart);
                    graphProfilePanel.add(singleNodePredecessorEdgeCurrentValueDistributionLineChart);
                    graphProfilePanel.add(singleNodeSuccessorEdgeCurrentValueDistributionLineChart);
                    graphProfilePanel.add(singleNodePredecessorValueHistogramDistributionLineChart);
                    */

                    JSplitPane graphAndGraphChartSplitPane = new JSplitPane();
                    graphAndGraphChartSplitPane.setDividerLocation((int)(MySequentialGraphSysUtil.getViewerWidth()*0.145));
                    graphAndGraphChartSplitPane.setDividerSize(5);
                    graphAndGraphChartSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
                    graphAndGraphChartSplitPane.setLeftComponent(dataProfilePanel);
                    graphAndGraphChartSplitPane.setRightComponent(MySequentialGraphVars.app.getSequentialGraphViewerPanel());

                    add(graphAndGraphChartSplitPane, BorderLayout.CENTER);
                    MySequentialGraphVars.app.addComponentListener(new ComponentAdapter() {
                        public void componentResized(ComponentEvent evt) {
                            //dashBoardSplitPane.setDividerSize(4);
                            //dashBoardSplitPane.setDividerLocation((int) (MySequentialGraphSysUtil.getViewerWidth() * 0.14));

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