package medousa.direct.graph.menu;

import medousa.direct.graph.*;
import medousa.direct.graph.barcharts.MyDirectGraphNeighborNodeValueBarChart;
import medousa.direct.utils.MyDirectGraphVars;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyDirectGraphViewerMenu
extends JPopupMenu {

    private JMenuItem pick = new JMenuItem("PICK");
    private JMenuItem transform = new JMenuItem("TRANSFORM");
    private JMenuItem searchNode = new JMenuItem("SEARCH NODE");
    private JMenuItem showNodeLabelDistribution = new JMenuItem("NODE LABLE DISTRIBUTION");
    private JMenuItem showEdgeLabelDistribution = new JMenuItem("EDGE LABEL DISTRIBUTION");
    private JMenuItem showNodeStatistics = new JMenuItem("NODE STATISTICS");
    private JMenuItem showEdgeStatistics = new JMenuItem("EDGE STATISTICS");
    private JMenuItem showSharedPredecessorOnly = new JMenuItem("SHARED PREDECESSORS ONLY");
    private JMenuItem showSharedSuccessorOnly = new JMenuItem("SHARED SUCCESSORS ONLY");
    private JMenuItem showPredecessorOnly = new JMenuItem("PREDECESSORS ONLY");
    private JMenuItem showSuccessorOnly = new JMenuItem("SUCCESSORS ONLY");
    private JMenuItem shakeGraph = new JMenuItem("SHAKE GRAPH");
    private JMenuItem clustering = new JMenuItem("CLUSTER GRAPH");

    public MyDirectGraphViewerMenu() {
        this.decorate();
    }

    private void decorate() {
        if (MyDirectGraphVars.getDirectGraphViewer().multiNodeSharedPredecessorSet != null && MyDirectGraphVars.getDirectGraphViewer().multiNodeSharedPredecessorSet.size() > 0) {
            this.showSharedPredecessorOnly.setFont(MyDirectGraphVars.tahomaPlainFont12);
        }

        if (MyDirectGraphVars.getDirectGraphViewer().multiNodeSharedSuccessorSet != null && MyDirectGraphVars.getDirectGraphViewer().multiNodeSharedSuccessorSet.size() > 0) {
            this.showSharedSuccessorOnly.setFont(MyDirectGraphVars.tahomaPlainFont12);
        }

        this.showNodeStatistics.setFont(MyDirectGraphVars.tahomaPlainFont12);
        this.showEdgeStatistics.setFont(MyDirectGraphVars.tahomaPlainFont12);
        this.searchNode.setFont(MyDirectGraphVars.tahomaPlainFont12);
        this.pick.setFont(MyDirectGraphVars.tahomaPlainFont12);
        this.transform.setFont(MyDirectGraphVars.tahomaPlainFont12);
        this.shakeGraph.setFont(MyDirectGraphVars.tahomaPlainFont12);
        this.clustering.setFont(MyDirectGraphVars.tahomaPlainFont12);

        this.add(this.pick);
        this.add(new JSeparator());
        this.add(this.transform);
        this.add(new JSeparator());
        this.add(this.shakeGraph);
        if (MyDirectGraphVars.userDefinedNodeLabels.size() > 0) {
            this.add(new JSeparator());
            this.add(this.showNodeLabelDistribution);
        }

        this.add(new JSeparator());
        if (MyDirectGraphVars.userDefinedEdgeLabesl.size() > 0) {
            this.add(new JSeparator());
            this.add(this.showEdgeLabelDistribution);
        }

        if (MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode != null || MyDirectGraphVars.getDirectGraphViewer().multiNodes != null) {
            this.add(new JSeparator());
            this.add(this.showPredecessorOnly);
        }

        if (MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode != null || MyDirectGraphVars.getDirectGraphViewer().multiNodes != null) {
            this.add(new JSeparator());
            this.add(this.showSuccessorOnly);
        }

        if (MyDirectGraphVars.getDirectGraphViewer().multiNodeSharedPredecessorSet != null && MyDirectGraphVars.getDirectGraphViewer().multiNodeSharedPredecessorSet.size() > 0) {
            this.add(new JSeparator());
            this.showSharedPredecessorOnly.setFont(MyDirectGraphVars.tahomaPlainFont12);
            this.add(this.showSharedPredecessorOnly);
        }

        if (MyDirectGraphVars.getDirectGraphViewer().multiNodeSharedSuccessorSet != null && MyDirectGraphVars.getDirectGraphViewer().multiNodeSharedSuccessorSet.size() > 0) {
            this.add(new JSeparator());
            this.showSharedSuccessorOnly.setFont(MyDirectGraphVars.tahomaPlainFont12);
            this.add(this.showSharedSuccessorOnly);
        }

        this.add(this.showNodeStatistics);
        this.add(new JSeparator());
        this.add(this.showEdgeStatistics);
        this.add(new JSeparator());
        this.add(this.clustering);

        this.clustering.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
               MyDirectGraphClusteringConfig clusteringConfig = new MyDirectGraphClusteringConfig();
            }
        });

        this.shakeGraph.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                MyDirectGraphVars.currentThread = new Thread(new Runnable() {
                    @Override public void run() {
                      MyDirectGraphVars.getDirectGraphViewer().layout.shakeGraph(0.75f, 0.85f, 0);
                    }
                });
                MyDirectGraphVars.currentThread.start();
            }
        });

        this.showSharedPredecessorOnly.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        MyDirectGraphVars.getDirectGraphViewer().isSharedSuccessorOnly = false;
                        MyDirectGraphVars.getDirectGraphViewer().isSharedPredecessorOnly = true;
                        MyDirectGraphVars.app.getDirectGraphDashBoard().txtStatistics.setTextStatistics();
                    }
                }).start();
            }
        });

        this.showSharedSuccessorOnly.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        MyDirectGraphVars.getDirectGraphViewer().isSharedPredecessorOnly = false;
                        MyDirectGraphVars.getDirectGraphViewer().isSharedSuccessorOnly = true;
                        MyDirectGraphVars.app.getDirectGraphDashBoard().txtStatistics.setTextStatistics();
                    }
                }).start();
            }
        });


        this.showPredecessorOnly.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        MyDirectGraphVars.getDirectGraphViewer().isSuccessorOnly = false;
                        MyDirectGraphVars.getDirectGraphViewer().isPredecessorOnly = true;
                        if (MyDirectGraphVars.getDirectGraphViewer().neighborNodeValueRankBarChart != null) {
                            MyDirectGraphVars.getDirectGraphViewer().remove(MyDirectGraphVars.getDirectGraphViewer().neighborNodeValueRankBarChart);
                            MyDirectGraphVars.BAR_CHART_RECORD_LIMIT = 100;
                            MyDirectGraphVars.getDirectGraphViewer().neighborNodeValueRankBarChart = new MyDirectGraphNeighborNodeValueBarChart();
                            MyDirectGraphVars.getDirectGraphViewer().neighborNodeValueRankBarChart.setPredecessorValueBarChartOnly();
                            MyDirectGraphVars.getDirectGraphViewer().add(MyDirectGraphVars.getDirectGraphViewer().neighborNodeValueRankBarChart);
                        } else if (MyDirectGraphVars.getDirectGraphViewer().multiNodeLevelNeighborNodeValueBarChart != null) {
                            MyDirectGraphVars.getDirectGraphViewer().remove(MyDirectGraphVars.getDirectGraphViewer().multiNodeLevelNeighborNodeValueBarChart);
                            MyDirectGraphVars.BAR_CHART_RECORD_LIMIT = 100;

                        }
                        MyDirectGraphVars.app.getDirectGraphDashBoard().txtStatistics.setTextStatistics();
                    }
                }).start();
            }
        });

        this.showSuccessorOnly.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        MyDirectGraphVars.getDirectGraphViewer().isPredecessorOnly = false;
                        MyDirectGraphVars.getDirectGraphViewer().isSuccessorOnly = true;
                        if (MyDirectGraphVars.getDirectGraphViewer().neighborNodeValueRankBarChart != null) {
                            MyDirectGraphVars.getDirectGraphViewer().remove(MyDirectGraphVars.getDirectGraphViewer().neighborNodeValueRankBarChart);
                            MyDirectGraphVars.BAR_CHART_RECORD_LIMIT = 100;
                            MyDirectGraphVars.getDirectGraphViewer().neighborNodeValueRankBarChart = new MyDirectGraphNeighborNodeValueBarChart();
                            MyDirectGraphVars.getDirectGraphViewer().neighborNodeValueRankBarChart.setSuccessorValueBarChartOnly();
                            MyDirectGraphVars.getDirectGraphViewer().add(MyDirectGraphVars.getDirectGraphViewer().neighborNodeValueRankBarChart);
                        } else if (MyDirectGraphVars.getDirectGraphViewer().multiNodeLevelNeighborNodeValueBarChart != null) {
                            MyDirectGraphVars.getDirectGraphViewer().remove(MyDirectGraphVars.getDirectGraphViewer().multiNodeLevelNeighborNodeValueBarChart);
                            MyDirectGraphVars.BAR_CHART_RECORD_LIMIT = 100;

                        }
                        MyDirectGraphVars.app.getDirectGraphDashBoard().txtStatistics.setTextStatistics();
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
                        MyDirectGraphVars.getDirectGraphViewer().setGraphMouse(graphMouse);
                    }
                }).start();}});

        this.transform.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
            new Thread(new Runnable() {
                @Override public void run() {
                    DefaultModalGraphMouse graphMouse = new DefaultModalGraphMouse();
                    graphMouse.setMode(DefaultModalGraphMouse.Mode.TRANSFORMING);
                    MyDirectGraphVars.getDirectGraphViewer().setGraphMouse(graphMouse);
                }
            }).start();}});

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
