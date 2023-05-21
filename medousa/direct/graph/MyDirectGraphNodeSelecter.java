package medousa.direct.graph;

import medousa.direct.graph.barcharts.MyDirectGraphMultiNodeLevelNeighborNodeValueBarChart;
import medousa.direct.graph.barcharts.MyDirectGraphNeighborNodeValueBarChart;
import medousa.direct.utils.MyDirectGraphVars;

import java.util.Collection;
import java.util.HashSet;

public class MyDirectGraphNodeSelecter {

    public MyDirectGraphNodeSelecter() {}

    public void setEdgeNodes(MyDirectNode source, MyDirectNode dest) {
        try {
            MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode = null;
            MyDirectGraphVars.getDirectGraphViewer().selectedSingleNodeSuccessorSet = null;
            MyDirectGraphVars.getDirectGraphViewer().selectedSingleNodePredecessorSet = null;

            MyDirectGraphVars.getDirectGraphViewer().multiNodes = new HashSet<>();
            MyDirectGraphVars.getDirectGraphViewer().multiNodes.add(source);
            MyDirectGraphVars.getDirectGraphViewer().multiNodes.add(dest);

            MyDirectGraphVars.getDirectGraphViewer().multiNodePredecessorSet = new HashSet<>();
            MyDirectGraphVars.getDirectGraphViewer().multiNodePredecessorSet.addAll(MyDirectGraphVars.directGraph.getPredecessors(source));
            MyDirectGraphVars.getDirectGraphViewer().multiNodePredecessorSet.addAll(MyDirectGraphVars.directGraph.getPredecessors(dest));

            MyDirectGraphVars.getDirectGraphViewer().multiNodeSuccessorSet = new HashSet<>();
            MyDirectGraphVars.getDirectGraphViewer().multiNodeSuccessorSet.addAll(MyDirectGraphVars.directGraph.getSuccessors(source));
            MyDirectGraphVars.getDirectGraphViewer().multiNodeSuccessorSet.addAll(MyDirectGraphVars.directGraph.getSuccessors(dest));

            MyDirectGraphVars.getDirectGraphViewer().multiNodeSharedPredecessorSet = new HashSet<>();
            MyDirectGraphVars.getDirectGraphViewer().multiNodeSharedPredecessorSet.addAll(MyDirectGraphVars.directGraph.getPredecessors(source));
            MyDirectGraphVars.getDirectGraphViewer().multiNodeSharedPredecessorSet.retainAll(MyDirectGraphVars.directGraph.getPredecessors(dest));

            MyDirectGraphVars.getDirectGraphViewer().multiNodeSharedSuccessorSet = new HashSet<>();
            MyDirectGraphVars.getDirectGraphViewer().multiNodeSharedSuccessorSet.addAll(MyDirectGraphVars.directGraph.getSuccessors(source));
            MyDirectGraphVars.getDirectGraphViewer().multiNodeSharedSuccessorSet.retainAll(MyDirectGraphVars.directGraph.getSuccessors(dest));

            MyDirectGraphVars.getDirectGraphViewer().vc.edgeValueComboBoxMenu.removeActionListener(MyDirectGraphVars.getDirectGraphViewer().vc);
            MyDirectGraphVars.getDirectGraphViewer().vc.edgeValueComboBoxMenu.setSelectedIndex(0);
            MyDirectGraphVars.getDirectGraphViewer().vc.edgeValueComboBoxMenu.addActionListener(MyDirectGraphVars.getDirectGraphViewer().vc);

            MyDirectGraphVars.getDirectGraphViewer().vc.removeBarCharts();
            MyDirectGraphVars.app.getDirectGraphDashBoard().setMultiNodeLevelDashBoard(MyDirectGraphVars.getDirectGraphViewer());
            MyDirectGraphVars.getDirectGraphViewer().multiNodeLevelNeighborNodeValueBarChart = new MyDirectGraphMultiNodeLevelNeighborNodeValueBarChart();
            MyDirectGraphVars.getDirectGraphViewer().add(MyDirectGraphVars.getDirectGraphViewer().multiNodeLevelNeighborNodeValueBarChart);
            MyDirectGraphVars.app.getDirectGraphDashBoard().txtStatistics.setTextStatistics();
        } catch (Exception ex) {

        }
    }

    public void setSelectedNode(MyDirectNode selectedNode) {
        try {
            if (MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode == null && MyDirectGraphVars.getDirectGraphViewer().multiNodes == null) { // No node selected yet.
                MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode = selectedNode;
                MyDirectGraphVars.getDirectGraphViewer().selectedSingleNodePredecessorSet = new HashSet<>(MyDirectGraphVars.directGraph.getPredecessors(selectedNode));
                MyDirectGraphVars.getDirectGraphViewer().selectedSingleNodeSuccessorSet = new HashSet<>(MyDirectGraphVars.directGraph.getSuccessors(selectedNode));

                Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
                for (MyDirectNode n : nodes) {
                    if (MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode == selectedNode ||
                        MyDirectGraphVars.getDirectGraphViewer().selectedSingleNodePredecessorSet.contains(selectedNode) ||
                        MyDirectGraphVars.getDirectGraphViewer().selectedSingleNodeSuccessorSet.contains(selectedNode)) {
                        continue;
                    }
                    n.setCurrentValue(0);
                }

                MyDirectGraphVars.getDirectGraphViewer().vc.removeBarCharts();
                MyDirectGraphVars.getDirectGraphViewer().neighborNodeValueRankBarChart = new MyDirectGraphNeighborNodeValueBarChart();
                MyDirectGraphVars.getDirectGraphViewer().add(MyDirectGraphVars.getDirectGraphViewer().neighborNodeValueRankBarChart);
                MyDirectGraphVars.app.getDirectGraphDashBoard().setSelectedNodeLevelDashBoard(MyDirectGraphVars.getDirectGraphViewer());
                MyDirectGraphVars.app.getDirectGraphDashBoard().txtStatistics.setTextStatistics();

                MyDirectGraphVars.getDirectGraphViewer().revalidate();
                MyDirectGraphVars.getDirectGraphViewer().repaint();
            } else if (MyDirectGraphVars.getDirectGraphViewer().multiNodes == null) {
                if (selectedNode == MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode) return;
                MyDirectGraphVars.getDirectGraphViewer().multiNodes = new HashSet<>();
                MyDirectGraphVars.getDirectGraphViewer().multiNodePredecessorSet = new HashSet<>();
                MyDirectGraphVars.getDirectGraphViewer().multiNodeSuccessorSet = new HashSet<>();
                MyDirectGraphVars.getDirectGraphViewer().multiNodeSharedPredecessorSet = new HashSet<>();
                MyDirectGraphVars.getDirectGraphViewer().multiNodeSharedSuccessorSet = new HashSet<>();

                synchronized (MyDirectGraphVars.getDirectGraphViewer().multiNodes) {
                    MyDirectGraphVars.getDirectGraphViewer().multiNodes.add(MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode);
                    MyDirectGraphVars.getDirectGraphViewer().multiNodes.add(selectedNode);
                }

                synchronized (MyDirectGraphVars.getDirectGraphViewer().multiNodePredecessorSet) {
                    MyDirectGraphVars.getDirectGraphViewer().multiNodePredecessorSet.addAll(MyDirectGraphVars.getDirectGraphViewer().selectedSingleNodePredecessorSet);
                    MyDirectGraphVars.getDirectGraphViewer().multiNodePredecessorSet.addAll(MyDirectGraphVars.directGraph.getPredecessors(selectedNode));
                }

                synchronized (MyDirectGraphVars.getDirectGraphViewer().multiNodeSuccessorSet) {
                    MyDirectGraphVars.getDirectGraphViewer().multiNodeSuccessorSet.addAll(MyDirectGraphVars.getDirectGraphViewer().selectedSingleNodeSuccessorSet);
                    MyDirectGraphVars.getDirectGraphViewer().multiNodeSuccessorSet.addAll(MyDirectGraphVars.directGraph.getSuccessors(selectedNode));
                }

                synchronized (MyDirectGraphVars.getDirectGraphViewer().multiNodeSharedSuccessorSet) {
                    MyDirectGraphVars.getDirectGraphViewer().multiNodeSharedSuccessorSet.addAll(MyDirectGraphVars.directGraph.getSuccessors(selectedNode));
                    MyDirectGraphVars.getDirectGraphViewer().multiNodeSharedSuccessorSet.retainAll(MyDirectGraphVars.getDirectGraphViewer().selectedSingleNodeSuccessorSet);
                }

                synchronized (MyDirectGraphVars.getDirectGraphViewer().multiNodeSharedPredecessorSet) {
                    MyDirectGraphVars.getDirectGraphViewer().multiNodeSharedPredecessorSet.addAll(MyDirectGraphVars.directGraph.getPredecessors(selectedNode));
                    MyDirectGraphVars.getDirectGraphViewer().multiNodeSharedPredecessorSet.retainAll(MyDirectGraphVars.getDirectGraphViewer().selectedSingleNodeSuccessorSet);
                }

                synchronized (MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode) {
                    MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode = null;
                    MyDirectGraphVars.getDirectGraphViewer().selectedSingleNodeSuccessorSet = null;
                    MyDirectGraphVars.getDirectGraphViewer().selectedSingleNodePredecessorSet = null;
                }

                MyDirectGraphVars.getDirectGraphViewer().vc.removeBarCharts();
                MyDirectGraphVars.app.getDirectGraphDashBoard().setMultiNodeLevelDashBoard(MyDirectGraphVars.getDirectGraphViewer());
                MyDirectGraphVars.getDirectGraphViewer().multiNodeLevelNeighborNodeValueBarChart = new MyDirectGraphMultiNodeLevelNeighborNodeValueBarChart();
                MyDirectGraphVars.getDirectGraphViewer().add(MyDirectGraphVars.getDirectGraphViewer().multiNodeLevelNeighborNodeValueBarChart);
                MyDirectGraphVars.app.getDirectGraphDashBoard().setMultiNodeLevelDashBoard(MyDirectGraphVars.getDirectGraphViewer());
                MyDirectGraphVars.app.getDirectGraphDashBoard().txtStatistics.setTextStatistics();

                MyDirectGraphVars.getDirectGraphViewer().revalidate();
                MyDirectGraphVars.getDirectGraphViewer().repaint();
            } else {
                if (MyDirectGraphVars.getDirectGraphViewer().multiNodes.contains(selectedNode)) {
                    return;
                }
                MyDirectGraphVars.getDirectGraphViewer().multiNodes.add(selectedNode);
                synchronized (MyDirectGraphVars.getDirectGraphViewer().multiNodePredecessorSet) {
                    MyDirectGraphVars.getDirectGraphViewer().multiNodePredecessorSet.addAll(MyDirectGraphVars.directGraph.getPredecessors(selectedNode));
                }

                synchronized (MyDirectGraphVars.getDirectGraphViewer().multiNodeSuccessorSet) {
                    MyDirectGraphVars.getDirectGraphViewer().multiNodeSuccessorSet.addAll(MyDirectGraphVars.directGraph.getSuccessors(selectedNode));
                }

                synchronized (MyDirectGraphVars.getDirectGraphViewer().multiNodeSharedSuccessorSet) {
                    MyDirectGraphVars.getDirectGraphViewer().multiNodeSharedSuccessorSet.retainAll(MyDirectGraphVars.getDirectGraphViewer().multiNodeSuccessorSet);
                }

                synchronized (MyDirectGraphVars.getDirectGraphViewer().multiNodeSharedPredecessorSet) {
                    MyDirectGraphVars.getDirectGraphViewer().multiNodeSharedPredecessorSet.retainAll(MyDirectGraphVars.getDirectGraphViewer().multiNodePredecessorSet);
                }

                MyDirectGraphVars.getDirectGraphViewer().vc.removeBarCharts();
                MyDirectGraphVars.app.getDirectGraphDashBoard().setMultiNodeLevelDashBoard(MyDirectGraphVars.getDirectGraphViewer());
                MyDirectGraphVars.getDirectGraphViewer().multiNodeLevelNeighborNodeValueBarChart = new MyDirectGraphMultiNodeLevelNeighborNodeValueBarChart();
                MyDirectGraphVars.getDirectGraphViewer().add(MyDirectGraphVars.getDirectGraphViewer().multiNodeLevelNeighborNodeValueBarChart);
                MyDirectGraphVars.app.getDirectGraphDashBoard().txtStatistics.setTextStatistics();

                MyDirectGraphVars.getDirectGraphViewer().revalidate();
                MyDirectGraphVars.getDirectGraphViewer().repaint();
            }
        } catch (Exception ex) {

        }
    }


}
