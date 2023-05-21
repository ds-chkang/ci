package medousa.direct.graph.menu;

import medousa.direct.graph.barcharts.MyDirectGraphNeighborNodeValueBarChart;
import medousa.direct.utils.MyDirectGraphVars;
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
        this.pick.setFont(MyDirectGraphVars.tahomaPlainFont12);
        this.transform.setFont(MyDirectGraphVars.tahomaPlainFont12);
        this.showPredecessorOnly.setFont(MyDirectGraphVars.tahomaPlainFont12);
        this.showSuccessorOnly.setFont(MyDirectGraphVars.tahomaPlainFont12);

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
    }
}
