package datamining.graph.menu;

import datamining.graph.MyDirectGraphEdgeStatistic;
import datamining.graph.MyDirectGraphNodeStatistic;
import datamining.graph.MyValueDistributionChart;
import datamining.graph.barcharts.MyNeighborNodeValueBarChart;
import datamining.utils.system.MyVars;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyDirectGraphViewerMenu
extends JPopupMenu {

    private JMenuItem pick = new JMenuItem("PICK");
    private JMenuItem transform = new JMenuItem("TRANSFORM");
    private JMenuItem searchNode = new JMenuItem("SEARCH NODE");
    private JMenuItem showNodeValueDistribution = new JMenuItem("NODE VALUE DISTRIBUTION");
    private JMenuItem showEdgeValueDistribution = new JMenuItem("EDGE VALUE DISTRIBUTION");
    private JMenuItem showNodeLabelDistribution = new JMenuItem("NODE LABLE DISTRIBUTION");
    private JMenuItem showEdgeLabelDistribution = new JMenuItem("EDGE LABEL DISTRIBUTION");
    private JMenuItem showNodeStatistics = new JMenuItem("NODE STATISTICS");
    private JMenuItem showEdgeStatistics = new JMenuItem("EDGE STATISTICS");
    private JMenuItem showSharedPredecessorOnly = new JMenuItem("SHARED PREDECESSORS ONLY");
    private JMenuItem showSharedSuccessorOnly = new JMenuItem("SHARED SUCCESSORS ONLY");
    private JMenuItem showPredecessorOnly = new JMenuItem("PREDECESSORS ONLY");
    private JMenuItem showSuccessorOnly = new JMenuItem("SUCCESSORS ONLY");
    private JMenuItem shakeGraph = new JMenuItem("SHAKE GRAPH");

    public MyDirectGraphViewerMenu() {
        this.decorate();
    }

    private void decorate() {
        this.showNodeValueDistribution.setFont(MyVars.tahomaPlainFont12);
        if (MyVars.nodeLabels.size() > 0) {
            this.showNodeLabelDistribution.setFont(MyVars.tahomaPlainFont12);
        }

        this.showEdgeValueDistribution.setFont(MyVars.tahomaPlainFont12);
        if (MyVars.edgeLabels.size() > 0) {
            this.showEdgeLabelDistribution.setFont(MyVars.tahomaPlainFont12);
        }

        if (MyVars.getDirectGraphViewer().multiNodeSharedPredecessorSet != null && MyVars.getDirectGraphViewer().multiNodeSharedPredecessorSet.size() > 0) {
            this.showSharedPredecessorOnly.setFont(MyVars.tahomaPlainFont12);
        }

        if (MyVars.getDirectGraphViewer().multiNodeSharedSuccessorSet != null && MyVars.getDirectGraphViewer().multiNodeSharedSuccessorSet.size() > 0) {
            this.showSharedSuccessorOnly.setFont(MyVars.tahomaPlainFont12);
        }

        this.showNodeStatistics.setFont(MyVars.tahomaPlainFont12);
        this.showEdgeStatistics.setFont(MyVars.tahomaPlainFont12);
        this.searchNode.setFont(MyVars.tahomaPlainFont12);
        this.pick.setFont(MyVars.tahomaPlainFont12);
        this.transform.setFont(MyVars.tahomaPlainFont12);
        this.shakeGraph.setFont(MyVars.tahomaPlainFont12);

        this.add(this.pick);
        this.add(new JSeparator());
        this.add(this.transform);
        this.add(new JSeparator());
        this.add(this.shakeGraph);
        this.add(new JSeparator());
        this.add(this.searchNode);
        this.add(new JSeparator());
        this.add(this.showNodeValueDistribution);
        if (MyVars.nodeLabels.size() > 0) {
            this.add(new JSeparator());
            this.add(this.showNodeLabelDistribution);
        }

        this.add(new JSeparator());
        this.add(this.showEdgeValueDistribution);
        if (MyVars.edgeLabels.size() > 0) {
            this.add(new JSeparator());
            this.add(this.showEdgeLabelDistribution);
        }

        if (MyVars.getDirectGraphViewer().selectedSingleNode != null || MyVars.getDirectGraphViewer().multiNodes != null) {
            this.add(new JSeparator());
            this.add(this.showPredecessorOnly);
        }

        if (MyVars.getDirectGraphViewer().selectedSingleNode != null || MyVars.getDirectGraphViewer().multiNodes != null) {
            this.add(new JSeparator());
            this.add(this.showSuccessorOnly);
        }

        if (MyVars.getDirectGraphViewer().multiNodeSharedPredecessorSet != null && MyVars.getDirectGraphViewer().multiNodeSharedPredecessorSet.size() > 0) {
            this.add(new JSeparator());
            this.showSharedPredecessorOnly.setFont(MyVars.tahomaPlainFont12);
            this.add(this.showSharedPredecessorOnly);
        }

        if (MyVars.getDirectGraphViewer().multiNodeSharedSuccessorSet != null && MyVars.getDirectGraphViewer().multiNodeSharedSuccessorSet.size() > 0) {
            this.add(new JSeparator());
            this.showSharedSuccessorOnly.setFont(MyVars.tahomaPlainFont12);
            this.add(this.showSharedSuccessorOnly);
        }

        this.add(new JSeparator());
        this.add(this.showNodeStatistics);
        this.add(new JSeparator());
        this.add(this.showEdgeStatistics);

        this.shakeGraph.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                MyVars.currentThread = new Thread(new Runnable() {
                    @Override public void run() {
                      MyVars.getDirectGraphViewer().layout.shakeGraph(0.75f, 0.85f, 0);
                    }
                });
                MyVars.currentThread.start();
            }
        });

        this.showSharedPredecessorOnly.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        MyVars.getDirectGraphViewer().isSharedSuccessorOnly = false;
                        MyVars.getDirectGraphViewer().isSharedPredecessorOnly = true;
                        MyVars.main.getDirectMarkovChainDashBoard().directGraphTextStatisticsPanel.setTextStatistics();
                    }
                }).start();
            }
        });

        this.showSharedSuccessorOnly.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        MyVars.getDirectGraphViewer().isSharedPredecessorOnly = false;
                        MyVars.getDirectGraphViewer().isSharedSuccessorOnly = true;
                        MyVars.main.getDirectMarkovChainDashBoard().directGraphTextStatisticsPanel.setTextStatistics();
                    }
                }).start();
            }
        });


        this.showPredecessorOnly.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        MyVars.getDirectGraphViewer().isSuccessorOnly = false;
                        MyVars.getDirectGraphViewer().isPredecessorOnly = true;
                        if (MyVars.getDirectGraphViewer().neighborNodeValueRankBarChart != null) {
                            MyVars.getDirectGraphViewer().remove(MyVars.getDirectGraphViewer().neighborNodeValueRankBarChart);
                            MyVars.BAR_CHART_RECORD_LIMIT = 100;
                            MyVars.getDirectGraphViewer().neighborNodeValueRankBarChart = new MyNeighborNodeValueBarChart();
                            MyVars.getDirectGraphViewer().neighborNodeValueRankBarChart.setPredecessorValueBarChartOnly();
                            MyVars.getDirectGraphViewer().add(MyVars.getDirectGraphViewer().neighborNodeValueRankBarChart);
                        } else if (MyVars.getDirectGraphViewer().multiNodeLevelNeighborNodeValueBarChart != null) {
                            MyVars.getDirectGraphViewer().remove(MyVars.getDirectGraphViewer().multiNodeLevelNeighborNodeValueBarChart);
                            MyVars.BAR_CHART_RECORD_LIMIT = 100;

                        }
                        MyVars.main.getDirectMarkovChainDashBoard().directGraphTextStatisticsPanel.setTextStatistics();
                    }
                }).start();
            }
        });

        this.showSuccessorOnly.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        MyVars.getDirectGraphViewer().isPredecessorOnly = false;
                        MyVars.getDirectGraphViewer().isSuccessorOnly = true;
                        if (MyVars.getDirectGraphViewer().neighborNodeValueRankBarChart != null) {
                            MyVars.getDirectGraphViewer().remove(MyVars.getDirectGraphViewer().neighborNodeValueRankBarChart);
                            MyVars.BAR_CHART_RECORD_LIMIT = 100;
                            MyVars.getDirectGraphViewer().neighborNodeValueRankBarChart = new MyNeighborNodeValueBarChart();
                            MyVars.getDirectGraphViewer().neighborNodeValueRankBarChart.setSuccessorValueBarChartOnly();
                            MyVars.getDirectGraphViewer().add(MyVars.getDirectGraphViewer().neighborNodeValueRankBarChart);
                        } else if (MyVars.getDirectGraphViewer().multiNodeLevelNeighborNodeValueBarChart != null) {
                            MyVars.getDirectGraphViewer().remove(MyVars.getDirectGraphViewer().multiNodeLevelNeighborNodeValueBarChart);
                            MyVars.BAR_CHART_RECORD_LIMIT = 100;

                        }
                        MyVars.main.getDirectMarkovChainDashBoard().directGraphTextStatisticsPanel.setTextStatistics();
                    }
                }).start();
            }
        });


        this.pick.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        DefaultModalGraphMouse graphMouse = new DefaultModalGraphMouse();
                        graphMouse.setMode(DefaultModalGraphMouse.Mode.PICKING);
                        MyVars.getDirectGraphViewer().setGraphMouse(graphMouse);
                    }
                }).start();}});

        this.transform.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
            new Thread(new Runnable() {
                @Override public void run() {
                    DefaultModalGraphMouse graphMouse = new DefaultModalGraphMouse();
                    graphMouse.setMode(DefaultModalGraphMouse.Mode.TRANSFORMING);
                    MyVars.getDirectGraphViewer().setGraphMouse(graphMouse);
                }
            }).start();}});

        this.showNodeValueDistribution.addActionListener(new ActionListener() {@
            Override public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        String valueTitle = MyVars.getDirectGraphViewer().directGraphViewerControlPanel.nodeValueComboBoxMenu.getSelectedItem().toString();
                        MyValueDistributionChart valueDistributionChart = new MyValueDistributionChart("NODE " + valueTitle.toUpperCase() + " VALUE DISTRIBUTION");
                        valueDistributionChart.setNodeValueDistribution(valueTitle);
                        valueDistributionChart.showDistribution();
                    }}).start();}});

        this.showEdgeValueDistribution.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        String valueTitle = MyVars.getDirectGraphViewer().directGraphViewerControlPanel.edgeValueComboBoxMenu.getSelectedItem().toString();
                        MyValueDistributionChart valueDistributionChart = new MyValueDistributionChart("EDGE " + valueTitle.toUpperCase() + " VALUE DISTRIBUTION");
                        valueDistributionChart.setEdgeValueDistribution(valueTitle);
                        valueDistributionChart.showDistribution();}}).start();}});

        this.showNodeStatistics.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        MyDirectGraphNodeStatistic directGraphNodeStatistic = new MyDirectGraphNodeStatistic();}
                }).start();}});

        this.showEdgeStatistics.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {new Thread(new Runnable() {
                @Override public void run() {
                    MyDirectGraphEdgeStatistic directGraphEdgeStatistic = new MyDirectGraphEdgeStatistic();}}).start();
            }
        });

    }
}
