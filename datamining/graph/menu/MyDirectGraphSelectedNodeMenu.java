package datamining.graph.menu;

import datamining.graph.barcharts.MyNeighborNodeValueBarChart;
import datamining.utils.system.MyVars;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyDirectGraphSelectedNodeMenu
extends JPopupMenu {

    private JMenuItem pick = new JMenuItem("PICK");
    private JMenuItem transform = new JMenuItem("TRANSFORM");
    private JMenuItem showPredecessorOnly = new JMenuItem("PREDECESSORS ONLY");
    private JMenuItem showSuccessorOnly = new JMenuItem("SUCCESSORS ONLY");

    public MyDirectGraphSelectedNodeMenu() {
        this.decorate();
    }

    private void decorate() {
        this.pick.setFont(MyVars.tahomaPlainFont12);
        this.transform.setFont(MyVars.tahomaPlainFont12);
        this.showPredecessorOnly.setFont(MyVars.tahomaPlainFont12);
        this.showSuccessorOnly.setFont(MyVars.tahomaPlainFont12);

        this.add(this.pick);
        this.add(new JSeparator());
        this.add(this.transform);
        this.add(new JSeparator());
        this.add(this.showPredecessorOnly);
        this.add(new JSeparator());
        this.add(this.showSuccessorOnly);


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
    }
}
