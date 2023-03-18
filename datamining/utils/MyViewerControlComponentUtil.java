package datamining.utils;

import datamining.graph.stats.*;
import datamining.main.MyProgressBar;
import datamining.graph.MyEdge;
import datamining.graph.MyNode;
import datamining.graph.stats.depthnode.MyDepthLevelNeighborNodeValueBarChart;
import datamining.graph.stats.depthnode.MyDepthLevelNodeValueBarChart;
import datamining.graph.stats.singlenode.*;
import datamining.graph.stats.multinode.MyMultiLevelEdgeValueBarChart;
import datamining.graph.stats.multinode.MyMultiLevelNeighborNodeValueBarChart;
import datamining.utils.message.MyMessageUtil;
import datamining.utils.system.MySysUtil;
import datamining.utils.system.MyVars;
import org.apache.commons.collections15.Transformer;

import javax.swing.table.DefaultTableModel;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MyViewerControlComponentUtil {

    public static void setNodeBarChartToViewer() {
        MyVars.getViewer().graphLevelNodeValueBarChart = new MyGraphLevelNodeValueBarChart();
        MyVars.getViewer().graphLevelNodeValueBarChart.setNodeValueBarChart();
        MyVars.getViewer().add(MyVars.getViewer().graphLevelNodeValueBarChart);
        MyVars.getViewer().revalidate();
        MyVars.getViewer().repaint();
    }

    public static void setDepthNodeBarChartToViewer() {
        MyVars.getViewer().depthNodeLevelNodeValueBarChart = new MyDepthLevelNodeValueBarChart();
        MyVars.getViewer().depthNodeLevelNodeValueBarChart.setDepthNodeValueBarCharts();
        MyVars.getViewer().add(MyVars.getViewer().depthNodeLevelNodeValueBarChart);
    }


    public static void setDepthNeighborNodeBarChartToViewer() {
        MyVars.getViewer().depthNodeLevelNeighborNodeValueBarChart = new MyDepthLevelNeighborNodeValueBarChart();
        MyVars.getViewer().depthNodeLevelNeighborNodeValueBarChart.setNeighborNodeValueBarChart();
        MyVars.getViewer().add(MyVars.getViewer().depthNodeLevelNeighborNodeValueBarChart);
    }

    public static void updateDepthCharts() {
        if (MyVars.getViewer().vc.selectedNodeNeighborNodeTypeSelector.isShowing()) {
            MyGraphLevelAverageValuesByDepthLineChart.instances = 0;
            if (MyVars.isTimeOn) {
                MyGraphLevelReachTimeByDepthLineChart.instances = 0;
            }
            MyVars.app.getDashboard().setSingleNodeDashBoard();
        } else if (MyVars.getViewer().selectedNode != null) {
            if (MyVars.currentGraphDepth == 0) {
                MyGraphLevelAverageValuesByDepthLineChart.instances = 0;
                if (MyVars.isTimeOn) {
                    MyGraphLevelReachTimeByDepthLineChart.instances = 0;
                }
                MyGraphLevelUniqueNodesByDepthLineBarChart.instances = 0;
                MyGraphLevelNodeAverageHopCountDistributionLineChart.instances = 0;
                MyVars.app.getDashboard().setSingleNodeDashBoard();
            }
        } else {
            if (MyVars.currentGraphDepth == 0) {
                MyGraphLevelAverageValuesByDepthLineChart.instances = 0;
                MyVars.app.getDashboard().graphLevelAverageValuesByDepthLineChart.decorate();

                if (MyVars.isTimeOn) {
                    MyGraphLevelReachTimeByDepthLineChart.instances = 0;
                    MyVars.app.getDashboard().graphLevelReachTimeByDepthLineChart.decorate();
                }
                MyGraphLevelUniqueNodesByDepthLineBarChart.instances = 0;
                MyVars.app.getDashboard().graphLevelUniqueNodesByDepthLineChart.setUniqueNodesByDepthLineChart();
                MyGraphLevelNodeAverageHopCountDistributionLineChart.instances = 0;
                MyVars.app.getDashboard().graphLevelNodeHopCountDistributionLineGraph.decorate();
                MyVars.app.getDashboard().graphLevelContributionByDepthLineChart.decorate();
            } else {
                MyGraphLevelAverageValuesByDepthLineChart.instances = 0;
                if (MyVars.isTimeOn) {
                    MyGraphLevelReachTimeByDepthLineChart.instances = 0;
                }
                MyGraphLevelUniqueNodesByDepthLineBarChart.instances = 0;
                MyGraphLevelNodeAverageHopCountDistributionLineChart.instances = 0;
                if (!MyVars.getViewer().vc.selectedNodeNeighborNodeTypeSelector.isVisible()) {
                    MyVars.app.getDashboard().setDepthNodeDashBoard();
                } else {
                    MyVars.app.getDashboard().setSingleNodeDashBoard();
                }
            }
        }
    }

    public static void setSelectedNodeNeighborValueBarChartToViewer() {
        if (MyVars.getViewer().graphLevelNodeValueBarChart != null) {
            MyVars.getViewer().remove(MyVars.getViewer().graphLevelNodeValueBarChart);
        }
        if (MyVars.getViewer().nodeLevelNeighborNodeValueBarChart != null) {
            MyVars.getViewer().remove(MyVars.getViewer().nodeLevelNeighborNodeValueBarChart);
        }
        if (MyVars.getViewer().sharedNodeValueBarChart != null) {
            MyVars.getViewer().remove(MyVars.getViewer().sharedNodeValueBarChart);
        }
        if (MyVars.getViewer().depthNodeLevelNodeValueBarChart != null) {
            MyVars.getViewer().remove(MyVars.getViewer().depthNodeLevelNodeValueBarChart);
        }
        if (MyVars.getViewer().vc.depthSelecter.getSelectedIndex() > 0) {
            if (MyVars.getViewer().vc.selectedNodeNeighborNodeTypeSelector.getSelectedIndex() > 0) {
                if (MyVars.getViewer().vc.selectedNodeNeighborNodeTypeSelector.getSelectedItem().toString().contains("S.")) {
                    MyVars.getViewer().nodeLevelNeighborNodeValueBarChart = new MySingleNodeNeighborNodeValueBarChart();
                    MyVars.getViewer().nodeLevelNeighborNodeValueBarChart.setSuccessorDepthNodeValueBarChartForSelectedNode();
                    MyVars.getViewer().add(MyVars.getViewer().nodeLevelNeighborNodeValueBarChart);
                } else if (MyVars.getViewer().vc.selectedNodeNeighborNodeTypeSelector.getSelectedItem().toString().contains("P.")) {
                    MyVars.getViewer().nodeLevelNeighborNodeValueBarChart = new MySingleNodeNeighborNodeValueBarChart();
                    MyVars.getViewer().nodeLevelNeighborNodeValueBarChart.setPredecessorDepthNodeValueBarChartForSelectedNode();
                    MyVars.getViewer().add(MyVars.getViewer().nodeLevelNeighborNodeValueBarChart);
                } else if (MyVars.getViewer().vc.selectedNodeNeighborNodeTypeSelector.getSelectedItem().toString().contains("BOTH")) {
                    MyVars.getViewer().nodeLevelNeighborNodeValueBarChart = new MySingleNodeNeighborNodeValueBarChart();
                    MyVars.getViewer().nodeLevelNeighborNodeValueBarChart.setNeighborNodeValueBarChart();
                    MyVars.getViewer().add(MyVars.getViewer().nodeLevelNeighborNodeValueBarChart);
                }
            }
        } else {
            MyVars.getViewer().nodeLevelNeighborNodeValueBarChart = new MySingleNodeNeighborNodeValueBarChart();
            MyVars.getViewer().add(MyVars.getViewer().nodeLevelNeighborNodeValueBarChart);
        }
        MyVars.getViewer().revalidate();
        MyVars.getViewer().repaint();
    }

    public static void setEdgeBarChartToViewer() {
        if (MyVars.getViewer().graphLelvelEdgeValueBarChart != null) {
            MyVars.getViewer().remove(MyVars.getViewer().graphLelvelEdgeValueBarChart);
        }
        if (MyVars.getViewer().multiNodeLevelEdgeValueBarChart != null) {
            MyVars.getViewer().remove(MyVars.getViewer().multiNodeLevelEdgeValueBarChart);
        }
        if (MyVars.getViewer().singleNodeLevelEdgeValueBarChart != null) {
            MyVars.getViewer().remove(MyVars.getViewer().singleNodeLevelEdgeValueBarChart);
        }

        if (MyVars.getViewer().vc.depthSelecter.getSelectedIndex() > 0) {
            if (MyVars.getViewer().vc.depthNeighborNodeTypeSelector.getSelectedIndex() > 0) {
                MyVars.getViewer().graphLelvelEdgeValueBarChart = new MyGraphLevelEdgeValueBarChart();
                MyVars.getViewer().graphLelvelEdgeValueBarChart.setEdgeValueBarChartForDepthNodes();
                MyVars.getViewer().add(MyVars.getViewer().graphLelvelEdgeValueBarChart);
            }
        } else {
            if (MyVars.getViewer().selectedNode == null) {
                MyVars.getViewer().graphLelvelEdgeValueBarChart = new MyGraphLevelEdgeValueBarChart();
                MyVars.getViewer().graphLelvelEdgeValueBarChart.setEdgeValueBarChart();
                MyVars.getViewer().add(MyVars.getViewer().graphLelvelEdgeValueBarChart);
            } else if (MyVars.getViewer().multiNodes != null && MyVars.getViewer().multiNodes.size() > 0) {
                MyVars.getViewer().graphLelvelEdgeValueBarChart = new MyGraphLevelEdgeValueBarChart();
                MyVars.getViewer().graphLelvelEdgeValueBarChart.setEdgeValueBarChartForSelectedNode();
                MyVars.getViewer().add(MyVars.getViewer().graphLelvelEdgeValueBarChart);
            }
        }

        MyVars.getViewer().revalidate();
        MyVars.getViewer().repaint();
    }

    public static void setSingleNodeLevelEdgeBarChartToViewer() {
        if (MyVars.getViewer().graphLelvelEdgeValueBarChart != null) {
            MyVars.getViewer().remove(MyVars.getViewer().graphLelvelEdgeValueBarChart);
        }
        if (MyVars.getViewer().singleNodeLevelEdgeValueBarChart != null) {
            MyVars.getViewer().remove(MyVars.getViewer().singleNodeLevelEdgeValueBarChart);
        }

        MyVars.getViewer().singleNodeLevelEdgeValueBarChart = new MySingleNodeEdgeValueBarChart();
        MyVars.getViewer().singleNodeLevelEdgeValueBarChart.setSingleNodeEdgeValueBarChart();
        MyVars.getViewer().add(MyVars.getViewer().singleNodeLevelEdgeValueBarChart);

        MyVars.getViewer().revalidate();
        MyVars.getViewer().repaint();
    }

    public static void setDepthOptionForSelectedNode() {
        try {
            MyVars.getViewer().vc.depthSelecter.removeActionListener(MyVars.getViewer().vc);
            MyVars.getViewer().vc.depthSelecter.removeAllItems();

            MyVars.getViewer().vc.depthSelecter.addItem("DEPTH");
            for (int i = 1; i <= MyVars.mxDepth; i++) {
                if (MyVars.getViewer().selectedNode.getNodeDepthInfoMap().containsKey(i)) {
                    MyVars.getViewer().vc.depthSelecter.addItem("" + i);
                }
            }

            MyVars.getViewer().vc.depthSelecter.addActionListener(MyVars.getViewer().vc);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void setNodeValueComboBoxMenu() {
        MyVars.getViewer().vc.nodeValueSelecter.removeActionListener(MyVars.getViewer().vc);
        int selectedIdx = MyVars.getViewer().vc.nodeValueSelecter.getSelectedIndex();
        MyVars.getViewer().vc.nodeValueSelecter.removeAllItems();
        if (MyVars.currentGraphDepth == 0) {
            for (int i = 0; i < MyVars.getViewer().vc.nodeValueItems.length; i++) {
                if (!MyVars.isTimeOn) {
                    if (i == 9 || i == 10) continue;
                    MyVars.getViewer().vc.nodeValueSelecter.addItem(MyVars.getViewer().vc.nodeValueItems[i]);
                } else {MyVars.getViewer().vc.nodeValueSelecter.addItem(MyVars.getViewer().vc.nodeValueItems[i]);}
            }
            for (String nodeValue : MyVars.nodeValueMap.keySet()) {
                MyVars.getViewer().vc.nodeValueSelecter.addItem(nodeValue);
            }
            MyVars.getViewer().vc.nodeValueSelecter.setSelectedIndex((selectedIdx == -1 ? 0 : selectedIdx));
            MyVars.getViewer().vc.nodeValueSelecter.addActionListener(MyVars.getViewer().vc);
        } else {
            int i=0;
            for (String depthNodeOptionItems : MyVars.getViewer().vc.depthNodeValueItems) {
                if (!MyVars.isTimeOn) {
                    if (i == 7 || i == 8) continue;
                    MyVars.getViewer().vc.nodeValueSelecter.addItem(depthNodeOptionItems);
                } else {
                    MyVars.getViewer().vc.nodeValueSelecter.addItem(depthNodeOptionItems);
                }
                i++;
            }
            for (String nodeValue : MyVars.nodeValueMap.keySet()) {
                MyVars.getViewer().vc.nodeValueSelecter.addItem(nodeValue);
            }
            MyVars.getViewer().vc.nodeValueSelecter.setSelectedIndex((selectedIdx == -1 ? 0 : selectedIdx));
            MyVars.getViewer().vc.nodeValueSelecter.addActionListener(MyVars.getViewer().vc);
        }
        MyVars.getViewer().vc.nodeValueSelecter.setToolTipText("NODE VALUE: " + MyVars.getViewer().vc.nodeValueSelecter.getSelectedItem().toString());
    }

    public static void setSelectedNodeNeighborNodeTypeOption() {
        MyVars.getViewer().vc.selectedNodeNeighborNodeTypeSelector.removeActionListener(MyVars.getViewer().vc);
        MyVars.getViewer().vc.selectedNodeNeighborNodeTypeSelector.removeAllItems();
        MyVars.getViewer().vc.selectedNodeNeighborNodeTypeSelector.addItem("NONE");
        int successors = 0;
        int predecessors = 0;

        if (MyVars.getViewer().selectedNode.getNodeDepthInfoMap().containsKey(MyVars.currentGraphDepth)) {
            successors = MyVars.getViewer().selectedNode.getNodeDepthInfo(MyVars.currentGraphDepth).getSuccessorCount();
            predecessors = MyVars.getViewer().selectedNode.getNodeDepthInfo(MyVars.currentGraphDepth).getPredecessorCount();
        }

        if (predecessors > 0) {
            MyVars.getViewer().vc.selectedNodeNeighborNodeTypeSelector.addItem("P.");
        }
        if (successors > 0) {
            MyVars.getViewer().vc.selectedNodeNeighborNodeTypeSelector.addItem("S.");
        }
        MyVars.getViewer().vc.selectedNodeNeighborNodeTypeSelector.addActionListener(MyVars.getViewer().vc);
        MyVars.getViewer().vc.selectedNodeNeighborNodeTypeSelector.setVisible(true);
    }

    public static void setSelectedNodeVisibleOnly() {
        Collection<MyNode> nodes = MyVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n != MyVars.getViewer().selectedNode) {
                n.setCurrentValue(0f);
            }
        }
    }

    public static void setDepthNodeNeighborNodeTypeOption() {
         if (MyVars.getViewer().vc.depthSelecter.getSelectedIndex() > 0) {
            Collection<MyNode> depthNodes = MyVars.g.getDepthNodes();
            boolean isPredecessorExists = false;
            boolean isSuccessorExists = false;
            for (MyNode n : depthNodes) {
                if (n.getNodeDepthInfoMap().containsKey(MyVars.currentGraphDepth) && n.getNodeDepthInfo(MyVars.currentGraphDepth).getPredecessorCount() > 0) {
                    isPredecessorExists = true;
                }
                if (n.getNodeDepthInfoMap().containsKey(MyVars.currentGraphDepth) && n.getNodeDepthInfo(MyVars.currentGraphDepth).getSuccessorCount() > 0) {
                    isSuccessorExists = true;
                }
            }

            Set<MyNode> sharedSuccessorSet = new HashSet<>();
            Iterator itr = depthNodes.iterator();
            sharedSuccessorSet.addAll(MyVars.g.getSuccessors(itr.next()));
            while (itr.hasNext()) {
                sharedSuccessorSet.retainAll(MyVars.g.getSuccessors(itr.next()));
            }

             Set<MyNode> sharedPredecessorSet = new HashSet<>();
             itr = depthNodes.iterator();
             sharedPredecessorSet.addAll(MyVars.g.getPredecessors(itr.next()));
             while (itr.hasNext()) {
                 sharedPredecessorSet.retainAll(MyVars.g.getPredecessors(itr.next()));
             }
             if (sharedPredecessorSet.size() > 0) {
                 isPredecessorExists = true;
             }

            MyVars.getViewer().vc.depthNeighborNodeTypeSelector.removeActionListener(MyVars.getViewer().vc);
            MyVars.getViewer().vc.depthNeighborNodeTypeSelector.removeAllItems();
            MyVars.getViewer().vc.depthNeighborNodeTypeSelector.addItem("NONE");
            if (isPredecessorExists) {
                MyVars.getViewer().vc.depthNeighborNodeTypeSelector.addItem("P.");
            }
            if (isSuccessorExists) {
                MyVars.getViewer().vc.depthNeighborNodeTypeSelector.addItem("S.");
            }

            MyVars.getViewer().vc.depthNeighborNodeTypeSelector.addActionListener(MyVars.getViewer().vc);
            MyVars.getViewer().vc.depthNeighborNodeTypeSelector.setVisible(true);
        } else {

         }
    }

    public static void setDepthValueSelecterMenu() {
        MyVars.getViewer().vc.depthSelecter.removeActionListener(MyVars.getViewer().vc);
        MyVars.getViewer().vc.depthSelecter.removeAllItems();
        MyVars.getViewer().vc.depthSelecter.addItem("DEPTH");
        for (int i = 1; i <= MyVars.mxDepth; i++) {
            MyVars.getViewer().vc.depthSelecter.addItem(""+i);
        }
        MyVars.getViewer().vc.depthSelecter.addActionListener(MyVars.getViewer().vc);
    }

    public static void setEdgeValueSelecterMenu() {
        MyVars.getViewer().vc.edgeValueSelecter.removeActionListener(MyVars.getViewer().vc);
        MyVars.getViewer().vc.edgeValueSelecter.removeAllItems();
        for (int i = 0; i < MyVars.getViewer().vc.edgeValueItems.length; i++) {
            if (MyVars.isTimeOn) {
                MyVars.getViewer().vc.edgeValueSelecter.addItem(MyVars.getViewer().vc.edgeValueItems[i]);
            } else {
                if (i < 3 || i > 6) {
                    MyVars.getViewer().vc.edgeValueSelecter.addItem(MyVars.getViewer().vc.edgeValueItems[i]);
                }
            }
        }
        for (String edgeValueVariable : MyVars.edgeValueMap.keySet()) {
            MyVars.getViewer().vc.edgeValueSelecter.addItem(edgeValueVariable);
        }
        MyVars.getViewer().vc.edgeValueSelecter.addActionListener(MyVars.getViewer().vc);
    }

    public static void removeEdgeValueBarChartFromViewer() {
        if (MyVars.getViewer().graphLelvelEdgeValueBarChart != null) {
            MyVars.getViewer().remove(MyVars.getViewer().graphLelvelEdgeValueBarChart);
        }
        if (MyVars.getViewer().multiNodeLevelEdgeValueBarChart != null) {
            MyVars.getViewer().remove(MyVars.getViewer().sharedNodeValueBarChart);
        }
        if (MyVars.getViewer().singleNodeLevelEdgeValueBarChart != null) {
            MyVars.getViewer().remove(MyVars.getViewer().singleNodeLevelEdgeValueBarChart);
        }
        MyVars.getViewer().revalidate();
        MyVars.getViewer().repaint();
    }

    public static void setDepthNodeNameSet() {
        MyVars.getViewer().vc.depthNodeNameSet = new HashSet<>();
        Collection<MyNode> depthNodes = MyVars.g.getDepthNodes();
        for (MyNode depthNode : depthNodes) {
            MyVars.getViewer().vc.depthNodeNameSet.add(depthNode.getName());
        }
    }
    public static void removeBarChartsFromViewer() {
        if (MyVars.getViewer().graphLevelNodeValueBarChart != null) {
            MyVars.getViewer().remove(MyVars.getViewer().graphLevelNodeValueBarChart);
        }
        if (MyVars.getViewer().nodeLevelNeighborNodeValueBarChart != null) {
            MyVars.getViewer().remove(MyVars.getViewer().nodeLevelNeighborNodeValueBarChart);
        }
        if (MyVars.getViewer().multiNodeLevelEdgeValueBarChart != null) {
            MyVars.getViewer().remove(MyVars.getViewer().multiNodeLevelEdgeValueBarChart);
        }
        if (MyVars.getViewer().sharedNodeValueBarChart != null) {
            MyVars.getViewer().remove(MyVars.getViewer().sharedNodeValueBarChart);
        }
        if (MyVars.getViewer().depthNodeLevelNodeValueBarChart != null) {
            MyVars.getViewer().remove(MyVars.getViewer().depthNodeLevelNodeValueBarChart);
        }
        if (MyVars.getViewer().depthNodeLevelNeighborNodeValueBarChart != null) {
            MyVars.getViewer().remove(MyVars.getViewer().depthNodeLevelNeighborNodeValueBarChart);
        }
    }

    public static void setExcludeSymbolOption() {
        MyVars.getViewer().vc.setNodeValueExcludeSymbolComboBox();

    }

    public static void setBottomCharts() {
        if (MyVars.currentGraphDepth > 0) {
            if (MyVars.getViewer().vc.selectedNodeNeighborNodeTypeSelector.isShowing()) {
                MyVars.app.getDashboard().singleNodePredecessorEdgeCurrentValueDistributionLineChart.decorate();
                MyVars.app.getDashboard().singleNodeSuccessorEdgeCurrentValueDistributionLineChart.decorate();
            } else if (MyVars.getViewer().vc.depthNeighborNodeTypeSelector.isShowing()) {
                MyVars.app.getDashboard().depthLevelNodeHopCountDistribution.decorate();
                MyVars.app.getDashboard().depthLevelNodeCurrentValueDistribution.decorate();
                MyVars.app.getDashboard().depthLevelEdgeCurrentValueDistribution.decorate();
            } else {
                MyVars.app.getDashboard().graphLevelSuccessorCountDistributionLineGraph.setSuccessorCountLineChart();
                MyVars.app.getDashboard().graphLevelNodeHopCountDistributionLineGraph.decorate();
                MyVars.app.getDashboard().graphLevelEdgeCurrentValueDistributionLineGraph.decorate();
                MyVars.app.getDashboard().graphLevelNodeCurrrentValueDistributionLineGraph.decorate();
                MyVars.app.getDashboard().graphLevelSuccessorCountDistributionLineGraph.setSuccessorCountLineChart();
            }
        } else if (MyVars.getViewer().multiNodes != null && MyVars.getViewer().multiNodes.size() > 0) {
//            MyVars.app.getDashboard().multiNodeNodeHopCountDistributionLineChart.decorate();
            MyVars.app.getDashboard().multiNodePredecessorValueDistributionLineChart.decorateValueChart();
            MyVars.app.getDashboard().multiNodeSuccessorValueDistributionLineChart.decorateValueChart();
            MyVars.app.getDashboard().multiNodeSharedPredecessorEdgeValueDistributionLineChart.decorate();
            MyVars.app.getDashboard().multiNodeSharedSuccessorEdgeValueDistributionLineChart.decorate();
        } else if (MyVars.getViewer().selectedNode != null) {
            //MyVars.app.getDashboard().singleNodeLevelPredecessorValueDistributionLineGraph.decorate();
            //MyVars.app.getDashboard().singleNodeLevelSuccessorValueDistributionLineGraph.decorate();
            MyVars.app.getDashboard().singleNodePredecessorEdgeCurrentValueDistributionLineChart.decorate();
            MyVars.app.getDashboard().singleNodeSuccessorEdgeCurrentValueDistributionLineChart.decorate();
        } else {
            MyGraphLevelEdgeValueDistributionLineChart.instances = 0;
            MyGraphLevelNodeValueDistributionLineChart.instances = 0;
            MyGraphLevelNodeAverageHopCountDistributionLineChart.instances = 0;
            MyGraphLevelPredecessorCountDistributionLineChart.instances = 0;
            MyGraphLevelSuccessorCountDistributionLineChart.instances = 0;

            MyVars.app.getDashboard().graphLevelSuccessorCountDistributionLineGraph.setSuccessorCountLineChart();
            MyVars.app.getDashboard().graphLevelNodeHopCountDistributionLineGraph.decorate();
            //MyVars.app.getDashboard().graphLevelEdgeCurrentValueDistributionLineGraph.decorate();
            //MyVars.app.getDashboard().graphLevelNodeCurrrentValueDistributionLineGraph.decorate();
            //MyVars.app.getDashboard().graphLevelValueDistributionLineChart.valueMenu.setSelectedIndex(1); // Edge value selection.
            MyVars.app.getDashboard().graphLevelSuccessorCountDistributionLineGraph.setSuccessorCountLineChart();
        }
        MyVars.app.getDashboard().revalidate();
        MyVars.app.getDashboard().repaint();
    }

    public static void setDefaultLookToViewer() {
        try {
            MyProgressBar pb = new MyProgressBar(false);
            if (MyVars.getViewer().selectedNode != null) MyVars.getViewer().selectedNode.pathLengthByDepthMap = null;
            MyVars.getViewer().selectedNode = null;
            MyVars.getViewer().vc.depthNodeNameSet = null;
            MyVars.getViewer().vc.selectedNodePredecessorDepthNodeMap = null;
            MyVars.getViewer().vc.selectedNodeSuccessorDepthNodeMap = null;
            MyVars.getViewer().vc.depthNodePredecessorMaps = null;
            MyVars.getViewer().vc.depthNodeSuccessorMaps = null;
            MyVars.getViewer().selectedTableNodeSet = null;
            MyVars.getViewer().selectedSingleNodePredecessors = new HashSet<>();
            MyVars.getViewer().selectedSingleNodeSuccessors = new HashSet<>();
            MyVars.getViewer().multiNodes = null;
            MyVars.getViewer().multiNodePredecessors = new HashSet<>();
            MyVars.getViewer().multiNodeSuccessors = new HashSet<>();
            MyVars.getViewer().sharedSuccessors = new HashSet<>();
            MyVars.getViewer().sharedPredecessors = new HashSet<>();
            MyVars.getViewer().predecessorsOnly = false;
            MyVars.getViewer().successorsOnly = false;
            MyVars.getViewer().predecessorsAndSuccessors = false;
            MyVars.getViewer().vc.nodeValueExcludeTxt.setText("");
            MyVars.getViewer().vc.edgeValueExcludeTxt.setText("");
            MyVars.getViewer().isExcludeBtnOn = false;
            MyVars.currentGraphDepth = 0;
            MyViewerControlComponentUtil.setEdgeValueSelecterMenu();
            MyViewerControlComponentUtil.setDepthValueSelecterMenu();
            MyViewerControlComponentUtil.setNodeValueComboBoxMenu();
            pb.updateValue(60, 100);

            MyVars.getViewer().vc.nodeValueSelecter.removeActionListener(MyVars.getViewer().vc);
            MyVars.getViewer().vc.edgeValueSelecter.removeActionListener(MyVars.getViewer().vc);
            MyVars.getViewer().vc.edgeLabelSelecter.removeActionListener(MyVars.getViewer().vc);
            MyVars.getViewer().vc.depthSelecter.removeActionListener(MyVars.getViewer().vc);
            MyVars.getViewer().vc.nodeLabelSelecter.removeActionListener(MyVars.getViewer().vc);
            MyVars.getViewer().vc.depthSelecter.setSelectedIndex(0);
            MyVars.getViewer().vc.edgeValueSelecter.setSelectedIndex(1);
            MyVars.getViewer().vc.edgeLabelSelecter.setSelectedIndex(0);
            MyVars.getViewer().vc.nodeLabelSelecter.setSelectedIndex(0);
            MyVars.getViewer().vc.nodeValueSelecter.setSelectedIndex(1);
            MyVars.getViewer().vc.edgeLabelSelecter.addActionListener(MyVars.getViewer().vc);
            MyVars.getViewer().vc.nodeLabelSelecter.addActionListener(MyVars.getViewer().vc);
            MyVars.getViewer().vc.nodeValueSelecter.addActionListener(MyVars.getViewer().vc);
            MyVars.getViewer().vc.edgeValueSelecter.addActionListener(MyVars.getViewer().vc);
            MyVars.getViewer().vc.depthSelecter.addActionListener(MyVars.getViewer().vc);
            MyVars.getViewer().vc.selectedNodeNeighborNodeTypeSelector.setVisible(false);
            MyVars.getViewer().vc.depthNeighborNodeTypeSelector.setVisible(false);

            pb.updateValue(80, 100);
            MyVars.dashBoard.setGraphLevelDashBoard();
            pb.updateValue(90, 100);

            MyVars.getViewer().vc.nodeValueBarChart.setSelected(false);
            MyVars.getViewer().vc.edgeValueBarChart.setSelected(false);
            MyVars.getViewer().vc.nodeListTable.clearSelection();
            MyVars.getViewer().vc.currentNodeListTable.clearSelection();
            MyVars.getViewer().vc.statTable.clearSelection();
            MyVars.getViewer().vc.edgeTable.clearSelection();
            MyVars.getViewer().vc.pathFromTable.clearSelection();
            MyVars.getViewer().vc.pathToTable.clearSelection();

            MyNodeUtil.setDefaultValuesToNodes();
            MyEdgeUtil.setDefaultValuesToEdges();
            MyVars.getViewer().vc.updateTableInfos();
            MyVars.getViewer().vc.nodeLabelSelecter.setSelectedIndex(1);
            MyVars.getViewer().vc.nodeValueBarChart.setText("N. V. B.");

            MyViewerControlComponentUtil.removeBarChartsFromViewer();
            MyViewerControlComponentUtil.removeEdgeValueBarChartFromViewer();
            MyVars.getViewer().getRenderContext().setEdgeDrawPaintTransformer(MyVars.getViewer().edgeColor);
            pb.updateValue(95, 100);
            pb.updateValue(100, 100);
            MyVars.getViewer().revalidate();
            MyVars.getViewer().repaint();
            pb.dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void setGraphLevelTableStatistics() {
        DefaultTableModel dm = (DefaultTableModel) MyVars.getViewer().vc.statTable.getModel();
        dm.getDataVector().removeAllElements();

        ((DefaultTableModel) MyVars.getViewer().vc.statTable.getModel()).addRow(new String[]{'\u03B5' + "(G)", MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyVars.g.graphEbasilon))});
        ((DefaultTableModel) MyVars.getViewer().vc.statTable.getModel()).addRow(new String[]{"CONNECTANCE", MyMathUtil.twoDecimalFormat(MyVars.g.connectance)});
        ((DefaultTableModel) MyVars.getViewer().vc.statTable.getModel()).addRow(new String[]{"NODES", MyMathUtil.getCommaSeperatedNumber(MyVars.g.getVertexCount())});
        ((DefaultTableModel) MyVars.getViewer().vc.statTable.getModel()).addRow(new String[]{"I. S.", MyMathUtil.getCommaSeperatedNumber(MyVars.g.getIsolatedNodeCount())});
        ((DefaultTableModel) MyVars.getViewer().vc.statTable.getModel()).addRow(new String[]{"EDGES", MyMathUtil.getCommaSeperatedNumber(MyVars.g.getEdgeCount())});
        ((DefaultTableModel) MyVars.getViewer().vc.statTable.getModel()).addRow(new String[]{"DIAMETER", MyMathUtil.getCommaSeperatedNumber(MyVars.diameter)});
        ((DefaultTableModel) MyVars.getViewer().vc.statTable.getModel()).addRow(new String[]{"AVG. P. LEN.", MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyVars.avgShortestPathLen))});
        ((DefaultTableModel) MyVars.getViewer().vc.statTable.getModel()).addRow(new String[]{"AVG. U. N.", MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getAverageUnreachableNodeCount()))});
        ((DefaultTableModel) MyVars.getViewer().vc.statTable.getModel()).addRow(new String[]{"TOT. U. N.", MyMathUtil.getCommaSeperatedNumber(MyNodeUtil.getTotalUnreachableNodeCount()) + "[" + MyMathUtil.twoDecimalFormat((double) MyNodeUtil.getTotalUnreachableNodeCount()/MyVars.g.getVertexCount())+ "]"});
        ((DefaultTableModel) MyVars.getViewer().vc.statTable.getModel()).addRow(new String[]{"R. N.", MyMathUtil.getCommaSeperatedNumber(MyNodeUtil.getRedNodeCount()) + "[" + MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getRedNodePercent()*100)) + "]"});
        ((DefaultTableModel) MyVars.getViewer().vc.statTable.getModel()).addRow(new String[]{"B. N.", MyMathUtil.getCommaSeperatedNumber(MyNodeUtil.getBlueNodeCount()) + "[" + MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getBlueNodePercent()*100)) + "]"});
        ((DefaultTableModel) MyVars.getViewer().vc.statTable.getModel()).addRow(new String[]{"G. N.", MyMathUtil.getCommaSeperatedNumber(MyNodeUtil.getGreenNodeCount()) + "[" + MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getGreenNodePercent()*100)) + "]"});
        ((DefaultTableModel) MyVars.getViewer().vc.statTable.getModel()).addRow(new String[]{"AVG. N. V.", MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(Double.parseDouble(MyNodeUtil.getAverageNodeValue())))});
        ((DefaultTableModel) MyVars.getViewer().vc.statTable.getModel()).addRow(new String[]{"MAX. N. V.", MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getMaximumNodeValue()))});
        ((DefaultTableModel) MyVars.getViewer().vc.statTable.getModel()).addRow(new String[]{"MIN. N. V.", MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getMinimumNodeValue()))});
        ((DefaultTableModel) MyVars.getViewer().vc.statTable.getModel()).addRow(new String[]{"STD. N. V.", MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getNodeValueStandardDeviation()))});
        ((DefaultTableModel) MyVars.getViewer().vc.statTable.getModel()).addRow(new String[]{"AVG. IN-N.", MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getAveragePredecessorCount()))});
        ((DefaultTableModel) MyVars.getViewer().vc.statTable.getModel()).addRow(new String[]{"AVG. OUT.N.", MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getAverageSuccessorCount()))});
        ((DefaultTableModel) MyVars.getViewer().vc.statTable.getModel()).addRow(new String[]{"AVG. IN-C.", MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getAverageInContribution()))});
        ((DefaultTableModel) MyVars.getViewer().vc.statTable.getModel()).addRow(new String[]{"AVG. OUT-C.", MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getAverageOutContribution()))});
        ((DefaultTableModel) MyVars.getViewer().vc.statTable.getModel()).addRow(new String[]{"AVG. UNIQ. N. C.", MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getAverageUniqueContribution()))});
        ((DefaultTableModel) MyVars.getViewer().vc.statTable.getModel()).addRow(new String[]{"MIN. UNIQ. N. C.", MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getMaximumUniqueContribution()))});
        ((DefaultTableModel) MyVars.getViewer().vc.statTable.getModel()).addRow(new String[]{"MAX. UNIQ. N. C.", MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getMinimumUniqueContribution()))});
        ((DefaultTableModel) MyVars.getViewer().vc.statTable.getModel()).addRow(new String[]{"STD. UNIQ. N. C.", MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getUniqueContributionStandardDeviation()))});
        ((DefaultTableModel) MyVars.getViewer().vc.statTable.getModel()).addRow(new String[]{"AVG. UNIQ. E. C.", MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyEdgeUtil.getAverageUniqueContribution()))});
        ((DefaultTableModel) MyVars.getViewer().vc.statTable.getModel()).addRow(new String[]{"MAX. UNIQ. E. C.", MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyEdgeUtil.getMaximumUniqueContribution()))});
        ((DefaultTableModel) MyVars.getViewer().vc.statTable.getModel()).addRow(new String[]{"MIN. UNIQ. E. C.", MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyEdgeUtil.getMinimumUniqueContribution()))});
        ((DefaultTableModel) MyVars.getViewer().vc.statTable.getModel()).addRow(new String[]{"STD. UNIQ. E. C.", MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyEdgeUtil.getUniqueContributionStandardDeviation()))});


        if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() > 1) {
            ((DefaultTableModel) MyVars.getViewer().vc.statTable.getModel()).addRow(new String[]{"AVG. E. V:", MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyEdgeUtil.getAverageEdgeValue()))});
            ((DefaultTableModel) MyVars.getViewer().vc.statTable.getModel()).addRow(new String[]{"MAX. E. V.", MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyEdgeUtil.getMaximumEdgeValue()))});
            ((DefaultTableModel) MyVars.getViewer().vc.statTable.getModel()).addRow(new String[]{"MIN. E. V:", MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyEdgeUtil.getMinimumEdgeValue()))});
            ((DefaultTableModel) MyVars.getViewer().vc.statTable.getModel()).addRow(new String[]{"STD. E. V:", MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyEdgeUtil.getEdgeValueStandardDeviation()))});
        }
        dm.fireTableDataChanged();
        MyVars.getViewer().revalidate();
        MyVars.getViewer().repaint();
    }

    public static void showEdgeLabel() {
        if (MyVars.getViewer().vc.edgeLabelSelecter.getSelectedIndex() > 0) {
            MyVars.getViewer().getRenderContext().setEdgeLabelTransformer(new Transformer<MyEdge, String>() {@Override
            public String transform(MyEdge e) {
                return e.edgeLabelMap.get(MyVars.getViewer().vc.edgeLabelSelecter.getSelectedItem().toString().replaceAll(" ", ""));
            }});
        } else {
            MyVars.getViewer().getRenderContext().setEdgeLabelTransformer(new Transformer<MyEdge, String>() {
                @Override public String transform(MyEdge e) {
                    return "";}}
            );
        }
        MyVars.getViewer().revalidate();
        MyVars.getViewer().repaint();
    }

    public static void showEdgeValue() {
        MyVars.getViewer().edgeValName = MyVars.getViewer().vc.edgeValueSelecter.getSelectedItem().toString().replaceAll(" ", "");
        if (MyVars.getViewer().edgeValName.contains("NONE")) {
            MyMessageUtil.showErrorMsg("Select an edge value, first!");return;}
        new Thread(new Runnable() {@Override public void run() {
            MyEdgeValueRankFrame edgeValueRankFrame = new MyEdgeValueRankFrame();}}).start();
        MyVars.getViewer().getRenderContext().setEdgeLabelTransformer(new Transformer<MyEdge, String>() {@Override
        public String transform(MyEdge e) {
            if (MyVars.getViewer().selectedNode != null) {
                if (e.getSource() == MyVars.getViewer().selectedNode || e.getDest() == MyVars.getViewer().selectedNode) {
                    String value = MyMathUtil.getCommaSeperatedNumber((long)e.getCurrentValue());
                    if (MyVars.getViewer().vc.edgeLabelSelecter.getSelectedIndex() > 0) {
                        return e.edgeLabelMap.get(MyVars.getViewer().vc.edgeLabelSelecter.getSelectedItem().toString())+"["+MySysUtil.formatAverageValue(MySysUtil.formatAverageValue(value))+"]";
                    } else {return MySysUtil.formatAverageValue(value);}
                } else {
                    if (MyVars.getViewer().vc.edgeLabelSelecter.getSelectedIndex() > 0) {
                        return e.edgeLabelMap.get(MyVars.getViewer().vc.edgeLabelSelecter.getSelectedItem().toString());
                    } else {return "";}
                }
            } else {
                String value = MyMathUtil.getCommaSeperatedNumber((long)e.getCurrentValue());
                if (MyVars.getViewer().vc.edgeLabelSelecter.getSelectedIndex() > 0) {
                    return e.edgeLabelMap.get(MyVars.getViewer().vc.edgeLabelSelecter.getSelectedItem().toString())+"["+MySysUtil.formatAverageValue(MySysUtil.formatAverageValue(value))+"]";
                } else {return MySysUtil.formatAverageValue(value);}
            }}});
        MyVars.getViewer().revalidate();
        MyVars.getViewer().repaint();
    }

    public static void showNodeValue() {
        new Thread(new Runnable() {@Override public void run() {MyNodeValueRankFrame nodeValueRankFrame = new MyNodeValueRankFrame();}}).start();
        MyVars.getViewer().getRenderContext().setVertexLabelTransformer(new Transformer<MyNode, String>() {@Override
        public String transform(MyNode n) {
            String value = MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(n.getCurrentValue()));
            value = (value.endsWith(".0") || value.endsWith(".00") ? value.substring(0, value.indexOf(".0")) : value);
            if (MyVars.getViewer().selectedNode != null) {
                if (n == MyVars.getViewer().selectedNode || MyVars.getViewer().selectedSingleNodePredecessors.contains(n) ||
                        MyVars.getViewer().selectedSingleNodeSuccessors.contains(n)) {
                    return value;
                } else {return "";}
            }
            return value;
        }});
    }

    public static void setSharedNodeLevelNodeValueBarChart() {
        if (MyVars.getViewer().graphLevelNodeValueBarChart != null) {
            MyVars.getViewer().remove(MyVars.getViewer().graphLevelNodeValueBarChart);
        }
        if (MyVars.getViewer().nodeLevelNeighborNodeValueBarChart != null) {
            MyVars.getViewer().remove(MyVars.getViewer().nodeLevelNeighborNodeValueBarChart);
        }
        if (MyVars.getViewer().sharedNodeValueBarChart != null) {
            MyVars.getViewer().remove(MyVars.getViewer().sharedNodeValueBarChart);
        }
        MyVars.getViewer().sharedNodeValueBarChart = new MyMultiLevelNeighborNodeValueBarChart();
        MyVars.getViewer().add(MyVars.getViewer().sharedNodeValueBarChart);
    }

    public static void removeSharedNodeValueBarCharts() {
        if (MyVars.getViewer().sharedNodeValueBarChart != null) {
            MyVars.getViewer().remove(MyVars.getViewer().sharedNodeValueBarChart);
        }
    }

    public static void setShareNodeLevelEdgeValueBarChartToViewer() {
        if (MyVars.getViewer().graphLelvelEdgeValueBarChart != null) {
            MyVars.getViewer().remove(MyVars.getViewer().graphLelvelEdgeValueBarChart);
        }
        if (MyVars.getViewer().multiNodeLevelEdgeValueBarChart != null) {
            MyVars.getViewer().remove(MyVars.getViewer().multiNodeLevelEdgeValueBarChart);
        }
        MyVars.getViewer().multiNodeLevelEdgeValueBarChart = new MyMultiLevelEdgeValueBarChart();
        MyVars.getViewer().add(MyVars.getViewer().multiNodeLevelEdgeValueBarChart);
    }

    public static void showNodeLabel() {
        if (MyVars.getViewer().vc.nodeLabelSelecter.getSelectedIndex() == 0) {
            MyVars.getViewer().getRenderContext().setVertexLabelTransformer(new Transformer<MyNode, String>() {@Override
            public String transform(MyNode n) {
                return "";
            }
            });
        } else if (MyVars.getViewer().vc.nodeLabelSelecter.getSelectedIndex() == 1) {
            MyVars.getViewer().getRenderContext().setVertexLabelTransformer(new Transformer<MyNode, String>() {@Override
            public String transform(MyNode n) {
                String name = "";
                if (n.getName().contains("x")) {
                    name = MySysUtil.decodeVariable(n.getName());
                } else {
                    name = MySysUtil.decodeNodeName(n.getName());
                }
                if (MyVars.getViewer().multiNodes != null && MyVars.getViewer().multiNodes.size() > 0) {
                    if (MyVars.getViewer().multiNodes.contains(n) || MyVars.getViewer().multiNodeSuccessors.contains(n) || MyVars.getViewer().multiNodePredecessors.contains(n)) {
                        return name;
                    } else {
                        return "";
                    }
                } else if (MyVars.getViewer().selectedNode != null) {
                    if (MyVars.getViewer().predecessorsOnly) {
                        if (n == MyVars.getViewer().selectedNode || MyVars.getViewer().selectedSingleNodePredecessors.contains(n)) {
                            return name;
                        } else {
                            return "";
                        }
                    } else if (MyVars.getViewer().successorsOnly) {
                        if (n == MyVars.getViewer().selectedNode || MyVars.getViewer().selectedSingleNodeSuccessors.contains(n)) {
                            return name;
                        } else {
                            return "";
                        }
                    } else if (MyVars.getViewer().predecessorsAndSuccessors) {
                        if (n == MyVars.getViewer().selectedNode || MyVars.getViewer().selectedSingleNodeSuccessors.contains(n) || MyVars.getViewer().selectedSingleNodePredecessors.contains(n)) {
                            return name;
                        } else {
                            return "";
                        }
                    } else if (n == MyVars.getViewer().selectedNode || MyVars.getViewer().selectedSingleNodeSuccessors.contains(n) || MyVars.getViewer().selectedSingleNodePredecessors.contains(n)) {
                        return name;
                    } else {
                        return "";
                    }
                } else {
                    return name;
                }
            }});
        } else {
            MyVars.getViewer().getRenderContext().setVertexLabelTransformer(new Transformer<MyNode, String>() {@Override
            public String transform(MyNode n) {
                if (MyVars.getViewer().multiNodes != null && MyVars.getViewer().multiNodes.size() > 0) {
                    if (MyVars.getViewer().multiNodes.contains(n) || MyVars.getViewer().multiNodeSuccessors.contains(n) || MyVars.getViewer().multiNodePredecessors.contains(n)) {
                        return n.nodeLabelMap.get(MyVars.getViewer().vc.nodeLabelSelecter.getSelectedItem().toString());
                    } else {
                        return "";
                    }
                } else if (MyVars.getViewer().selectedNode != null) {
                    if (MyVars.getViewer().predecessorsOnly) {
                        if (n == MyVars.getViewer().selectedNode || MyVars.getViewer().selectedSingleNodePredecessors.contains(n)) {
                            return n.nodeLabelMap.get(MyVars.getViewer().vc.nodeLabelSelecter.getSelectedItem().toString());
                        } else {
                            return "";
                        }
                    } else if (MyVars.getViewer().successorsOnly) {
                        if (n == MyVars.getViewer().selectedNode || MyVars.getViewer().selectedSingleNodeSuccessors.contains(n)) {
                            return n.nodeLabelMap.get(MyVars.getViewer().vc.nodeLabelSelecter.getSelectedItem().toString());
                        } else {
                            return "";
                        }
                    } else if (MyVars.getViewer().predecessorsAndSuccessors) {
                        if (n == MyVars.getViewer().selectedNode || MyVars.getViewer().selectedSingleNodeSuccessors.contains(n) || MyVars.getViewer().selectedSingleNodePredecessors.contains(n)) {
                            return n.nodeLabelMap.get(MyVars.getViewer().vc.nodeLabelSelecter.getSelectedItem().toString());
                        } else {
                            return "";
                        }
                    } else if (n == MyVars.getViewer().selectedNode || MyVars.getViewer().selectedSingleNodeSuccessors.contains(n) || MyVars.getViewer().selectedSingleNodePredecessors.contains(n)) {
                        return n.nodeLabelMap.get(MyVars.getViewer().vc.nodeLabelSelecter.getSelectedItem().toString());
                    } else {
                        return "";
                    }
                } else {
                    return n.nodeLabelMap.get(MyVars.getViewer().vc.nodeLabelSelecter.getSelectedItem().toString());
                }
            }
            });
        }
        MyVars.getViewer().revalidate();
        MyVars.getViewer().repaint();
    }

    public static void setViewerComponentDefaultValues() {
        MyVars.getViewer().vc.nodeValueBarChart.removeActionListener(MyVars.getViewer().vc);
        MyVars.getViewer().vc.edgeValueBarChart.removeActionListener(MyVars.getViewer().vc);
        MyVars.getViewer().vc.weightedNodeColor.removeActionListener(MyVars.getViewer().vc);
        MyVars.getViewer().vc.nodeValueSelecter.removeActionListener(MyVars.getViewer().vc);
        MyVars.getViewer().vc.edgeValueSelecter.removeActionListener(MyVars.getViewer().vc);
        MyVars.getViewer().vc.edgeLabelSelecter.removeActionListener(MyVars.getViewer().vc);
        MyVars.getViewer().vc.nodeLabelSelecter.removeActionListener(MyVars.getViewer().vc);
        MyVars.getViewer().vc.nodeValueBarChart.setSelected(false);
        MyVars.getViewer().vc.edgeValueBarChart.setSelected(false);
        MyVars.getViewer().vc.weightedNodeColor.setSelected(false);
        MyVars.getViewer().vc.nodeValueSelecter.setSelectedIndex(0);
        MyVars.getViewer().vc.edgeValueSelecter.setSelectedIndex(0);
        MyVars.getViewer().vc.edgeLabelSelecter.setSelectedIndex(0);
        MyVars.getViewer().vc.nodeLabelSelecter.setSelectedIndex(1);
        MyVars.getViewer().vc.nodeValueBarChart.addActionListener(MyVars.getViewer().vc);
        MyVars.getViewer().vc.edgeValueBarChart.addActionListener(MyVars.getViewer().vc);
        MyVars.getViewer().vc.weightedNodeColor.addActionListener(MyVars.getViewer().vc);
        MyVars.getViewer().vc.nodeValueSelecter.addActionListener(MyVars.getViewer().vc);
        MyVars.getViewer().vc.edgeValueSelecter.addActionListener(MyVars.getViewer().vc);
        MyVars.getViewer().vc.edgeLabelSelecter.addActionListener(MyVars.getViewer().vc);
        MyVars.getViewer().vc.nodeLabelSelecter.addActionListener(MyVars.getViewer().vc);
    }

    public static void adjustDepthNodeValueSelections() {
        MyVars.getViewer().vc.edgeValueSelecter.removeActionListener(MyVars.getViewer().vc);
        MyVars.getViewer().vc.edgeValueSelecter.removeAllItems();
        MyVars.getViewer().vc.edgeValueSelecter.addItem("NONE");
        MyVars.getViewer().vc.edgeValueSelecter.addItem("DEFALUT");
        MyVars.getViewer().vc.edgeValueSelecter.addItem("CONT.");
        MyVars.getViewer().vc.edgeValueSelecter.setSelectedIndex(1);
        MyVars.getViewer().vc.edgeValueSelecter.addActionListener(MyVars.getViewer().vc);

        MyVars.getViewer().vc.nodeValueSelecter.removeActionListener(MyVars.getViewer().vc);
        MyVars.getViewer().vc.nodeValueSelecter.removeAllItems();
        MyVars.getViewer().vc.nodeValueSelecter.addItem("CONT.");
        MyVars.getViewer().vc.nodeValueSelecter.addActionListener(MyVars.getViewer().vc);
        MyVars.getViewer().vc.nodeLabelSelecter.removeActionListener(MyVars.getViewer().vc);
        MyVars.getViewer().vc.nodeLabelSelecter.setSelectedIndex(1);
        MyVars.getViewer().vc.nodeLabelSelecter.addActionListener(MyVars.getViewer().vc);

        /**
         * Edge value selection option adjustment.
         */
        MyVars.getViewer().vc.edgeValueSelecter.removeActionListener(MyVars.getViewer().vc);
        MyVars.getViewer().vc.edgeValueSelecter.removeAllItems();
        MyVars.getViewer().vc.edgeValueSelecter.addItem("NONE");
        MyVars.getViewer().vc.edgeValueSelecter.addItem("DEFAULT");
        MyVars.getViewer().vc.edgeValueSelecter.addItem("CONT.");
        MyVars.getViewer().vc.edgeValueSelecter.setSelectedIndex(1);
        MyVars.getViewer().vc.edgeValueSelecter.addActionListener(MyVars.getViewer().vc);

        /**
         * Edge label selection option adjustment.
         */
        MyVars.getViewer().vc.edgeLabelSelecter.removeActionListener(MyVars.getViewer().vc);
        MyVars.getViewer().vc.edgeLabelSelecter.setSelectedIndex(0);
        MyVars.getViewer().vc.edgeLabelSelecter.addActionListener(MyVars.getViewer().vc);
    }

}
