package datamining.graph;

import datamining.graph.barcharts.MyMultiNodeLevelNeighborNodeValueBarChart;
import datamining.graph.barcharts.MyNeighborNodeValueBarChart;
import datamining.utils.system.MyVars;

import java.util.Collection;
import java.util.HashSet;

public class MyGraphNodeSearcher {

    public MyGraphNodeSearcher() {}

    public void setEdgeNodes(MyDirectNode source, MyDirectNode dest) {
        try {
            MyVars.getDirectGraphViewer().selectedSingleNode = null;
            MyVars.getDirectGraphViewer().selectedSingleNodeSuccessorSet = null;
            MyVars.getDirectGraphViewer().selectedSingleNodePredecessorSet = null;

            MyVars.getDirectGraphViewer().multiNodes = new HashSet<>();
            MyVars.getDirectGraphViewer().multiNodes.add(source);
            MyVars.getDirectGraphViewer().multiNodes.add(dest);

            MyVars.getDirectGraphViewer().multiNodePredecessorSet = new HashSet<>();
            MyVars.getDirectGraphViewer().multiNodePredecessorSet.addAll(MyVars.directMarkovChain.getPredecessors(source));
            MyVars.getDirectGraphViewer().multiNodePredecessorSet.addAll(MyVars.directMarkovChain.getPredecessors(dest));

            MyVars.getDirectGraphViewer().multiNodeSuccessorSet = new HashSet<>();
            MyVars.getDirectGraphViewer().multiNodeSuccessorSet.addAll(MyVars.directMarkovChain.getSuccessors(source));
            MyVars.getDirectGraphViewer().multiNodeSuccessorSet.addAll(MyVars.directMarkovChain.getSuccessors(dest));

            MyVars.getDirectGraphViewer().multiNodeSharedPredecessorSet = new HashSet<>();
            MyVars.getDirectGraphViewer().multiNodeSharedPredecessorSet.addAll(MyVars.directMarkovChain.getPredecessors(source));
            MyVars.getDirectGraphViewer().multiNodeSharedPredecessorSet.retainAll(MyVars.directMarkovChain.getPredecessors(dest));

            MyVars.getDirectGraphViewer().multiNodeSharedSuccessorSet = new HashSet<>();
            MyVars.getDirectGraphViewer().multiNodeSharedSuccessorSet.addAll(MyVars.directMarkovChain.getSuccessors(source));
            MyVars.getDirectGraphViewer().multiNodeSharedSuccessorSet.retainAll(MyVars.directMarkovChain.getSuccessors(dest));

            MyVars.getDirectGraphViewer().directGraphViewerControlPanel.edgeValueComboBoxMenu.removeActionListener(MyVars.getDirectGraphViewer().directGraphViewerControlPanel);
            MyVars.getDirectGraphViewer().directGraphViewerControlPanel.edgeValueComboBoxMenu.setSelectedIndex(0);
            MyVars.getDirectGraphViewer().directGraphViewerControlPanel.edgeValueComboBoxMenu.addActionListener(MyVars.getDirectGraphViewer().directGraphViewerControlPanel);

            MyVars.getDirectGraphViewer().directGraphViewerControlPanel.removeBarCharts();
            MyVars.main.getDirectMarkovChainDashBoard().setMultiNodeLevelDashBoard(MyVars.getDirectGraphViewer());
            MyVars.getDirectGraphViewer().multiNodeLevelNeighborNodeValueBarChart = new MyMultiNodeLevelNeighborNodeValueBarChart();
            MyVars.getDirectGraphViewer().add(MyVars.getDirectGraphViewer().multiNodeLevelNeighborNodeValueBarChart);
            MyVars.main.getDirectMarkovChainDashBoard().directGraphTextStatisticsPanel.setTextStatistics();
        } catch (Exception ex) {

        }
    }

    public void setSelectedNode(MyDirectNode selectedNode) {
        try {
            if (MyVars.getDirectGraphViewer().selectedSingleNode == null && MyVars.getDirectGraphViewer().multiNodes == null) {
                MyVars.getDirectGraphViewer().selectedSingleNode = selectedNode;
                MyVars.getDirectGraphViewer().selectedSingleNodePredecessorSet = new HashSet<>(MyVars.directMarkovChain.getPredecessors(selectedNode));
                MyVars.getDirectGraphViewer().selectedSingleNodeSuccessorSet = new HashSet<>(MyVars.directMarkovChain.getSuccessors(selectedNode));

                Collection<MyDirectNode> nodes = MyVars.directMarkovChain.getVertices();
                for (MyDirectNode n : nodes) {
                    if (MyVars.getDirectGraphViewer().selectedSingleNode == selectedNode ||
                            MyVars.getDirectGraphViewer().selectedSingleNodePredecessorSet.contains(selectedNode) ||
                            MyVars.getDirectGraphViewer().selectedSingleNodeSuccessorSet.contains(selectedNode)) {
                        continue;
                    }
                    n.setCurrentValue(0);
                }

                MyVars.getDirectGraphViewer().directGraphViewerControlPanel.removeBarCharts();
                MyVars.getDirectGraphViewer().neighborNodeValueRankBarChart = new MyNeighborNodeValueBarChart();
                MyVars.getDirectGraphViewer().add(MyVars.getDirectGraphViewer().neighborNodeValueRankBarChart);
                MyVars.main.getDirectMarkovChainDashBoard().setSelectedNodeLevelDashBoard(MyVars.getDirectGraphViewer());
                MyVars.main.getDirectMarkovChainDashBoard().directGraphTextStatisticsPanel.setTextStatistics();
            } else if (MyVars.getDirectGraphViewer().multiNodes == null) {
                if (selectedNode == MyVars.getDirectGraphViewer().selectedSingleNode) return;
                MyVars.getDirectGraphViewer().multiNodes = new HashSet<>();
                MyVars.getDirectGraphViewer().multiNodePredecessorSet = new HashSet<>();
                MyVars.getDirectGraphViewer().multiNodeSuccessorSet = new HashSet<>();
                MyVars.getDirectGraphViewer().multiNodeSharedPredecessorSet = new HashSet<>();
                MyVars.getDirectGraphViewer().multiNodeSharedSuccessorSet = new HashSet<>();

                synchronized (MyVars.getDirectGraphViewer().multiNodes) {
                    MyVars.getDirectGraphViewer().multiNodes.add(MyVars.getDirectGraphViewer().selectedSingleNode);
                    MyVars.getDirectGraphViewer().multiNodes.add(selectedNode);
                }

                synchronized (MyVars.getDirectGraphViewer().multiNodePredecessorSet) {
                    MyVars.getDirectGraphViewer().multiNodePredecessorSet.addAll(MyVars.getDirectGraphViewer().selectedSingleNodePredecessorSet);
                    MyVars.getDirectGraphViewer().multiNodePredecessorSet.addAll(MyVars.directMarkovChain.getPredecessors(selectedNode));
                }

                synchronized (MyVars.getDirectGraphViewer().multiNodeSuccessorSet) {
                    MyVars.getDirectGraphViewer().multiNodeSuccessorSet.addAll(MyVars.getDirectGraphViewer().selectedSingleNodeSuccessorSet);
                    MyVars.getDirectGraphViewer().multiNodeSuccessorSet.addAll(MyVars.directMarkovChain.getSuccessors(selectedNode));
                }

                synchronized (MyVars.getDirectGraphViewer().multiNodeSharedSuccessorSet) {
                    MyVars.getDirectGraphViewer().multiNodeSharedSuccessorSet.addAll(MyVars.directMarkovChain.getSuccessors(selectedNode));
                    MyVars.getDirectGraphViewer().multiNodeSharedSuccessorSet.retainAll(MyVars.getDirectGraphViewer().selectedSingleNodeSuccessorSet);
                }

                synchronized (MyVars.getDirectGraphViewer().multiNodeSharedPredecessorSet) {
                    MyVars.getDirectGraphViewer().multiNodeSharedPredecessorSet.addAll(MyVars.directMarkovChain.getPredecessors(selectedNode));
                    MyVars.getDirectGraphViewer().multiNodeSharedPredecessorSet.retainAll(MyVars.getDirectGraphViewer().selectedSingleNodeSuccessorSet);
                }

                synchronized (MyVars.getDirectGraphViewer().selectedSingleNode) {
                    MyVars.getDirectGraphViewer().selectedSingleNode = null;
                    MyVars.getDirectGraphViewer().selectedSingleNodeSuccessorSet = null;
                    MyVars.getDirectGraphViewer().selectedSingleNodePredecessorSet = null;
                }

                MyVars.getDirectGraphViewer().directGraphViewerControlPanel.removeBarCharts();
                MyVars.main.getDirectMarkovChainDashBoard().setMultiNodeLevelDashBoard(MyVars.getDirectGraphViewer());
                MyVars.getDirectGraphViewer().multiNodeLevelNeighborNodeValueBarChart = new MyMultiNodeLevelNeighborNodeValueBarChart();
                MyVars.getDirectGraphViewer().add(MyVars.getDirectGraphViewer().multiNodeLevelNeighborNodeValueBarChart);
                MyVars.main.getDirectMarkovChainDashBoard().setMultiNodeLevelDashBoard(MyVars.getDirectGraphViewer());
                MyVars.main.getDirectMarkovChainDashBoard().directGraphTextStatisticsPanel.setTextStatistics();
            } else {
                if (MyVars.getDirectGraphViewer().multiNodes.contains(selectedNode)) {
                    return;
                }
                MyVars.getDirectGraphViewer().multiNodes.add(selectedNode);
                synchronized (MyVars.getDirectGraphViewer().multiNodePredecessorSet) {
                    MyVars.getDirectGraphViewer().multiNodePredecessorSet.addAll(MyVars.directMarkovChain.getPredecessors(selectedNode));
                }

                synchronized (MyVars.getDirectGraphViewer().multiNodeSuccessorSet) {
                    MyVars.getDirectGraphViewer().multiNodeSuccessorSet.addAll(MyVars.directMarkovChain.getSuccessors(selectedNode));
                }

                synchronized (MyVars.getDirectGraphViewer().multiNodeSharedSuccessorSet) {
                    MyVars.getDirectGraphViewer().multiNodeSharedSuccessorSet.retainAll(MyVars.getDirectGraphViewer().multiNodeSuccessorSet);
                }

                synchronized (MyVars.getDirectGraphViewer().multiNodeSharedPredecessorSet) {
                    MyVars.getDirectGraphViewer().multiNodeSharedPredecessorSet.retainAll(MyVars.getDirectGraphViewer().multiNodePredecessorSet);
                }

                MyVars.getDirectGraphViewer().directGraphViewerControlPanel.removeBarCharts();
                MyVars.main.getDirectMarkovChainDashBoard().setMultiNodeLevelDashBoard(MyVars.getDirectGraphViewer());
                MyVars.getDirectGraphViewer().multiNodeLevelNeighborNodeValueBarChart = new MyMultiNodeLevelNeighborNodeValueBarChart();
                MyVars.getDirectGraphViewer().add(MyVars.getDirectGraphViewer().multiNodeLevelNeighborNodeValueBarChart);
                MyVars.main.getDirectMarkovChainDashBoard().directGraphTextStatisticsPanel.setTextStatistics();
            }
        } catch (Exception ex) {

        }
    }


}
