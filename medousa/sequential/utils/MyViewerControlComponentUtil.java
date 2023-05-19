package medousa.sequential.utils;

import medousa.MyProgressBar;
import medousa.message.MyMessageUtil;
import medousa.sequential.graph.MyComboBoxTooltipRenderer;
import medousa.sequential.graph.stats.*;
import medousa.sequential.graph.stats.barchart.MyGraphLevelEdgeValueBarChart;
import medousa.sequential.graph.stats.barchart.MyGraphLevelNodeValueBarChart;
import medousa.sequential.graph.stats.barchart.MySingleNodeEdgeValueBarChart;
import medousa.sequential.graph.stats.barchart.MySingleNodeNeighborNodeValueBarChart;
import medousa.sequential.graph.MyEdge;
import medousa.sequential.graph.MyNode;
import medousa.sequential.graph.stats.barchart.MyDepthLevelNeighborNodeValueBarChart;
import medousa.sequential.graph.stats.barchart.MyDepthLevelNodeValueBarChart;
import medousa.sequential.graph.stats.barchart.MyMultiLevelEdgeValueBarChart;
import medousa.sequential.graph.stats.barchart.MyMultiLevelNeighborNodeValueBarChart;
import org.apache.commons.collections15.Transformer;

import javax.swing.table.DefaultTableModel;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MyViewerControlComponentUtil {

    public static void setNodeBarChartToViewer() {
        MySequentialGraphVars.getSequentialGraphViewer().graphLevelNodeValueBarChart = new MyGraphLevelNodeValueBarChart();
        MySequentialGraphVars.getSequentialGraphViewer().graphLevelNodeValueBarChart.setNodeValueBarChart();
        MySequentialGraphVars.getSequentialGraphViewer().add(MySequentialGraphVars.getSequentialGraphViewer().graphLevelNodeValueBarChart);
        MySequentialGraphVars.getSequentialGraphViewer().revalidate();
        MySequentialGraphVars.getSequentialGraphViewer().repaint();
    }

    public static void setDepthNodeBarChartToViewer() {
        MySequentialGraphVars.getSequentialGraphViewer().depthNodeLevelNodeValueBarChart = new MyDepthLevelNodeValueBarChart();
        MySequentialGraphVars.getSequentialGraphViewer().depthNodeLevelNodeValueBarChart.setDepthNodeValueBarCharts();
        MySequentialGraphVars.getSequentialGraphViewer().add(MySequentialGraphVars.getSequentialGraphViewer().depthNodeLevelNodeValueBarChart);
    }


    public static void setDepthNeighborNodeBarChartToViewer() {
        MySequentialGraphVars.getSequentialGraphViewer().depthNodeLevelNeighborNodeValueBarChart = new MyDepthLevelNeighborNodeValueBarChart();
        MySequentialGraphVars.getSequentialGraphViewer().depthNodeLevelNeighborNodeValueBarChart.setNeighborNodeValueBarChart();
        MySequentialGraphVars.getSequentialGraphViewer().add(MySequentialGraphVars.getSequentialGraphViewer().depthNodeLevelNeighborNodeValueBarChart);
    }

    public static void setSelectedNodeNeighborValueBarChartToViewer() {
        if (MySequentialGraphVars.getSequentialGraphViewer().graphLevelNodeValueBarChart != null) {
            MySequentialGraphVars.getSequentialGraphViewer().remove(MySequentialGraphVars.getSequentialGraphViewer().graphLevelNodeValueBarChart);
        }
        if (MySequentialGraphVars.getSequentialGraphViewer().nodeLevelNeighborNodeValueBarChart != null) {
            MySequentialGraphVars.getSequentialGraphViewer().remove(MySequentialGraphVars.getSequentialGraphViewer().nodeLevelNeighborNodeValueBarChart);
        }
        if (MySequentialGraphVars.getSequentialGraphViewer().sharedNodeValueBarChart != null) {
            MySequentialGraphVars.getSequentialGraphViewer().remove(MySequentialGraphVars.getSequentialGraphViewer().sharedNodeValueBarChart);
        }
        if (MySequentialGraphVars.getSequentialGraphViewer().depthNodeLevelNodeValueBarChart != null) {
            MySequentialGraphVars.getSequentialGraphViewer().remove(MySequentialGraphVars.getSequentialGraphViewer().depthNodeLevelNodeValueBarChart);
        }
        if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthSelecter.getSelectedIndex() > 0) {
            if (MySequentialGraphVars.getSequentialGraphViewer().vc.selectedNodeNeighborNodeTypeSelector.getSelectedIndex() > 0) {
                if (MySequentialGraphVars.getSequentialGraphViewer().vc.selectedNodeNeighborNodeTypeSelector.getSelectedItem().toString().contains("S.")) {
                    MySequentialGraphVars.getSequentialGraphViewer().nodeLevelNeighborNodeValueBarChart = new MySingleNodeNeighborNodeValueBarChart();
                    MySequentialGraphVars.getSequentialGraphViewer().nodeLevelNeighborNodeValueBarChart.setSuccessorDepthNodeValueBarChartForSelectedNode();
                    MySequentialGraphVars.getSequentialGraphViewer().add(MySequentialGraphVars.getSequentialGraphViewer().nodeLevelNeighborNodeValueBarChart);
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.selectedNodeNeighborNodeTypeSelector.getSelectedItem().toString().contains("P.")) {
                    MySequentialGraphVars.getSequentialGraphViewer().nodeLevelNeighborNodeValueBarChart = new MySingleNodeNeighborNodeValueBarChart();
                    MySequentialGraphVars.getSequentialGraphViewer().nodeLevelNeighborNodeValueBarChart.setPredecessorDepthNodeValueBarChartForSelectedNode();
                    MySequentialGraphVars.getSequentialGraphViewer().add(MySequentialGraphVars.getSequentialGraphViewer().nodeLevelNeighborNodeValueBarChart);
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.selectedNodeNeighborNodeTypeSelector.getSelectedItem().toString().contains("BOTH")) {
                    MySequentialGraphVars.getSequentialGraphViewer().nodeLevelNeighborNodeValueBarChart = new MySingleNodeNeighborNodeValueBarChart();
                    MySequentialGraphVars.getSequentialGraphViewer().nodeLevelNeighborNodeValueBarChart.setNeighborNodeValueBarChart();
                    MySequentialGraphVars.getSequentialGraphViewer().add(MySequentialGraphVars.getSequentialGraphViewer().nodeLevelNeighborNodeValueBarChart);
                }
            }
        } else {
            MySequentialGraphVars.getSequentialGraphViewer().nodeLevelNeighborNodeValueBarChart = new MySingleNodeNeighborNodeValueBarChart();
            MySequentialGraphVars.getSequentialGraphViewer().add(MySequentialGraphVars.getSequentialGraphViewer().nodeLevelNeighborNodeValueBarChart);
        }
        MySequentialGraphVars.getSequentialGraphViewer().revalidate();
        MySequentialGraphVars.getSequentialGraphViewer().repaint();
    }

    public static void setEdgeBarChartToViewer() {
        if (MySequentialGraphVars.getSequentialGraphViewer().graphLelvelEdgeValueBarChart != null) {
            MySequentialGraphVars.getSequentialGraphViewer().remove(MySequentialGraphVars.getSequentialGraphViewer().graphLelvelEdgeValueBarChart);
        }
        if (MySequentialGraphVars.getSequentialGraphViewer().multiNodeLevelEdgeValueBarChart != null) {
            MySequentialGraphVars.getSequentialGraphViewer().remove(MySequentialGraphVars.getSequentialGraphViewer().multiNodeLevelEdgeValueBarChart);
        }
        if (MySequentialGraphVars.getSequentialGraphViewer().singleNodeLevelEdgeValueBarChart != null) {
            MySequentialGraphVars.getSequentialGraphViewer().remove(MySequentialGraphVars.getSequentialGraphViewer().singleNodeLevelEdgeValueBarChart);
        }

        if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthSelecter.getSelectedIndex() > 0) {
            if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthNeighborNodeTypeSelector.getSelectedIndex() > 0) {
                MySequentialGraphVars.getSequentialGraphViewer().graphLelvelEdgeValueBarChart = new MyGraphLevelEdgeValueBarChart();
                MySequentialGraphVars.getSequentialGraphViewer().graphLelvelEdgeValueBarChart.setEdgeValueBarChartForDepthNodes();
                MySequentialGraphVars.getSequentialGraphViewer().add(MySequentialGraphVars.getSequentialGraphViewer().graphLelvelEdgeValueBarChart);
            }
        } else {
            if (MySequentialGraphVars.getSequentialGraphViewer().selectedNode == null) {
                MySequentialGraphVars.getSequentialGraphViewer().graphLelvelEdgeValueBarChart = new MyGraphLevelEdgeValueBarChart();
                MySequentialGraphVars.getSequentialGraphViewer().graphLelvelEdgeValueBarChart.setEdgeValueBarChart();
                MySequentialGraphVars.getSequentialGraphViewer().add(MySequentialGraphVars.getSequentialGraphViewer().graphLelvelEdgeValueBarChart);
            } else if (MySequentialGraphVars.getSequentialGraphViewer().multiNodes != null && MySequentialGraphVars.getSequentialGraphViewer().multiNodes.size() > 0) {
                MySequentialGraphVars.getSequentialGraphViewer().graphLelvelEdgeValueBarChart = new MyGraphLevelEdgeValueBarChart();
                MySequentialGraphVars.getSequentialGraphViewer().graphLelvelEdgeValueBarChart.setEdgeValueBarChartForSelectedNode();
                MySequentialGraphVars.getSequentialGraphViewer().add(MySequentialGraphVars.getSequentialGraphViewer().graphLelvelEdgeValueBarChart);
            }
        }

        MySequentialGraphVars.getSequentialGraphViewer().revalidate();
        MySequentialGraphVars.getSequentialGraphViewer().repaint();
    }

    public static void setSingleNodeLevelEdgeBarChartToViewer() {
        if (MySequentialGraphVars.getSequentialGraphViewer().graphLelvelEdgeValueBarChart != null) {
            MySequentialGraphVars.getSequentialGraphViewer().remove(MySequentialGraphVars.getSequentialGraphViewer().graphLelvelEdgeValueBarChart);
        }
        if (MySequentialGraphVars.getSequentialGraphViewer().singleNodeLevelEdgeValueBarChart != null) {
            MySequentialGraphVars.getSequentialGraphViewer().remove(MySequentialGraphVars.getSequentialGraphViewer().singleNodeLevelEdgeValueBarChart);
        }

        MySequentialGraphVars.getSequentialGraphViewer().singleNodeLevelEdgeValueBarChart = new MySingleNodeEdgeValueBarChart();
        MySequentialGraphVars.getSequentialGraphViewer().singleNodeLevelEdgeValueBarChart.setSingleNodeEdgeValueBarChart();
        MySequentialGraphVars.getSequentialGraphViewer().add(MySequentialGraphVars.getSequentialGraphViewer().singleNodeLevelEdgeValueBarChart);

        MySequentialGraphVars.getSequentialGraphViewer().revalidate();
        MySequentialGraphVars.getSequentialGraphViewer().repaint();
    }

    public static void setDepthOptionForSelectedNode() {
        try {
            MySequentialGraphVars.getSequentialGraphViewer().vc.depthSelecter.removeActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
            MySequentialGraphVars.getSequentialGraphViewer().vc.depthSelecter.removeAllItems();

            MySequentialGraphVars.getSequentialGraphViewer().vc.depthSelecter.addItem("DEPTH");
            for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                if (MySequentialGraphVars.getSequentialGraphViewer().selectedNode.getNodeDepthInfoMap().containsKey(i)) {
                    MySequentialGraphVars.getSequentialGraphViewer().vc.depthSelecter.addItem("" + i);
                }
            }

            MySequentialGraphVars.getSequentialGraphViewer().vc.depthSelecter.addActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void setNodeValueComboBoxMenu() {
        MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.removeActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
        int selectedIdx = MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex();
        MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.removeAllItems();
        if (MySequentialGraphVars.currentGraphDepth == 0) {
            for (int i = 0; i < MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueItems.length; i++) {
                if (!MySequentialGraphVars.isTimeOn) {
                    if (i == 16 || i == 17 || i == 18 || i == 19 || i == 20 || i == 21 || i == 22 || i == 24 || i == 25 || i == 26) continue;
                    MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.addItem(MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueItems[i]);
                } else {
                    MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.addItem(MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueItems[i]);}
            }
            for (String nodeValue : MySequentialGraphVars.userDefinedNodeValueMap.keySet()) {
                MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.addItem(nodeValue);
            }
            MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.setSelectedIndex((selectedIdx == -1 ? 0 : selectedIdx));
            MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.addActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
        } else {
            int i=0;
            for (String depthNodeOptionItems : MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeValueItems) {
                if (!MySequentialGraphVars.isTimeOn) {
                    if (i > 6) continue;
                    else MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.addItem(depthNodeOptionItems);
                } else {
                    MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.addItem(depthNodeOptionItems);
                }
                i++;
            }
            for (String nodeValue : MySequentialGraphVars.userDefinedNodeValueMap.keySet()) {
                MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.addItem(nodeValue);
            }
            MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.setSelectedIndex((selectedIdx == -1 ? 0 : selectedIdx));
            MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.addActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
        }
    }

    public static void setSelectedNodeNeighborNodeTypeOption() {
        MySequentialGraphVars.getSequentialGraphViewer().vc.selectedNodeNeighborNodeTypeSelector.removeActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
        MySequentialGraphVars.getSequentialGraphViewer().vc.selectedNodeNeighborNodeTypeSelector.removeAllItems();
        MySequentialGraphVars.getSequentialGraphViewer().vc.selectedNodeNeighborNodeTypeSelector.addItem("NONE");
        int successors = 0;
        int predecessors = 0;

        if (MySequentialGraphVars.getSequentialGraphViewer().selectedNode.getNodeDepthInfoMap().containsKey(MySequentialGraphVars.currentGraphDepth)) {
            successors = MySequentialGraphVars.getSequentialGraphViewer().selectedNode.getNodeDepthInfo(MySequentialGraphVars.currentGraphDepth).getSuccessorCount();
            predecessors = MySequentialGraphVars.getSequentialGraphViewer().selectedNode.getNodeDepthInfo(MySequentialGraphVars.currentGraphDepth).getPredecessorCount();
        }

        if (predecessors > 0) {
            MySequentialGraphVars.getSequentialGraphViewer().vc.selectedNodeNeighborNodeTypeSelector.addItem("P.");
        }
        if (successors > 0) {
            MySequentialGraphVars.getSequentialGraphViewer().vc.selectedNodeNeighborNodeTypeSelector.addItem("S.");
        }
        MySequentialGraphVars.getSequentialGraphViewer().vc.selectedNodeNeighborNodeTypeSelector.addActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
        MySequentialGraphVars.getSequentialGraphViewer().vc.selectedNodeNeighborNodeTypeSelector.setVisible(true);
    }

    public static void setSelectedNodeVisibleOnly() {
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n != MySequentialGraphVars.getSequentialGraphViewer().selectedNode) {
                n.setCurrentValue(0f);
            }
        }
    }

    public static void setDepthNodeNeighborNodeTypeOption() {
         if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthSelecter.getSelectedIndex() > 0) {
            Collection<MyNode> depthNodes = MySequentialGraphVars.g.getDepthNodes();
            boolean isPredecessorExists = false;
            boolean isSuccessorExists = false;
            for (MyNode n : depthNodes) {
                if (n.getNodeDepthInfoMap().containsKey(MySequentialGraphVars.currentGraphDepth) && n.getNodeDepthInfo(MySequentialGraphVars.currentGraphDepth).getPredecessorCount() > 0) {
                    isPredecessorExists = true;
                }
                if (n.getNodeDepthInfoMap().containsKey(MySequentialGraphVars.currentGraphDepth) && n.getNodeDepthInfo(MySequentialGraphVars.currentGraphDepth).getSuccessorCount() > 0) {
                    isSuccessorExists = true;
                }
            }

            Set<MyNode> sharedSuccessorSet = new HashSet<>();
            Iterator itr = depthNodes.iterator();
            if (itr.hasNext()) {
                sharedSuccessorSet.addAll(MySequentialGraphVars.g.getSuccessors(itr.next()));
                while (itr.hasNext()) {
                    sharedSuccessorSet.retainAll(MySequentialGraphVars.g.getSuccessors(itr.next()));
                }
            }

             Set<MyNode> sharedPredecessorSet = new HashSet<>();
             itr = depthNodes.iterator();
             if (itr.hasNext()) {
                 sharedPredecessorSet.addAll(MySequentialGraphVars.g.getPredecessors(itr.next()));
                 while (itr.hasNext()) {
                     sharedPredecessorSet.retainAll(MySequentialGraphVars.g.getPredecessors(itr.next()));
                 }
                 if (sharedPredecessorSet.size() > 0) {
                     isPredecessorExists = true;
                 }
             }

            MySequentialGraphVars.getSequentialGraphViewer().vc.depthNeighborNodeTypeSelector.removeActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
            MySequentialGraphVars.getSequentialGraphViewer().vc.depthNeighborNodeTypeSelector.removeAllItems();
            MySequentialGraphVars.getSequentialGraphViewer().vc.depthNeighborNodeTypeSelector.addItem("NONE");
            if (isPredecessorExists) {
                MySequentialGraphVars.getSequentialGraphViewer().vc.depthNeighborNodeTypeSelector.addItem("P.");
            }
            if (isSuccessorExists) {
                MySequentialGraphVars.getSequentialGraphViewer().vc.depthNeighborNodeTypeSelector.addItem("S.");
            }

            MySequentialGraphVars.getSequentialGraphViewer().vc.depthNeighborNodeTypeSelector.addActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
            MySequentialGraphVars.getSequentialGraphViewer().vc.depthNeighborNodeTypeSelector.setVisible(true);
        } else {

         }
    }

    public static void setDepthValueSelecterMenu() {
        MySequentialGraphVars.getSequentialGraphViewer().vc.depthSelecter.removeActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
        MySequentialGraphVars.getSequentialGraphViewer().vc.depthSelecter.removeAllItems();
        MySequentialGraphVars.getSequentialGraphViewer().vc.depthSelecter.addItem("DEP.");
        for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
            MySequentialGraphVars.getSequentialGraphViewer().vc.depthSelecter.addItem(""+i);
        }
        MySequentialGraphVars.getSequentialGraphViewer().vc.depthSelecter.addActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
    }

    public static void setDepthExcludeSelecterMenu() {
        MySequentialGraphVars.getSequentialGraphViewer().vc.depthExcludeSelecter.removeActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
        MySequentialGraphVars.getSequentialGraphViewer().vc.depthExcludeSelecter.removeAllItems();
        MySequentialGraphVars.getSequentialGraphViewer().vc.depthExcludeSelecter.addItem("DEP.");
        for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
            MySequentialGraphVars.getSequentialGraphViewer().vc.depthExcludeSelecter.addItem(""+i);
        }
        MySequentialGraphVars.getSequentialGraphViewer().vc.depthExcludeSelecter.addActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
    }

    public static void setEdgeValueSelecterMenu() {
        MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.removeActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
        MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.removeAllItems();
        for (int i = 0; i < MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueItems.length; i++) {
            if (MySequentialGraphVars.isTimeOn) {
                MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.addItem(MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueItems[i]);
            } else {
                if (i < 4 || i > 7) {
                    MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.addItem(MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueItems[i]);
                }
            }
        }
        for (String edgeValueVariable : MySequentialGraphVars.userDefinedEdgeValueMap.keySet()) {
            MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.addItem(edgeValueVariable);
        }
        MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.addActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
    }

    public static void removeEdgeValueBarChartFromViewer() {
        if (MySequentialGraphVars.getSequentialGraphViewer().clusteredGraphLevelEdgeValueBarChart != null) {
            MySequentialGraphVars.getSequentialGraphViewer().remove(MySequentialGraphVars.getSequentialGraphViewer().clusteredGraphLevelEdgeValueBarChart);
        }
        if (MySequentialGraphVars.getSequentialGraphViewer().graphLelvelEdgeValueBarChart != null) {
            MySequentialGraphVars.getSequentialGraphViewer().remove(MySequentialGraphVars.getSequentialGraphViewer().graphLelvelEdgeValueBarChart);
        }
        if (MySequentialGraphVars.getSequentialGraphViewer().multiNodeLevelEdgeValueBarChart != null) {
            MySequentialGraphVars.getSequentialGraphViewer().remove(MySequentialGraphVars.getSequentialGraphViewer().sharedNodeValueBarChart);
        }
        if (MySequentialGraphVars.getSequentialGraphViewer().singleNodeLevelEdgeValueBarChart != null) {
            MySequentialGraphVars.getSequentialGraphViewer().remove(MySequentialGraphVars.getSequentialGraphViewer().singleNodeLevelEdgeValueBarChart);
        }
        MySequentialGraphVars.getSequentialGraphViewer().revalidate();
        MySequentialGraphVars.getSequentialGraphViewer().repaint();
    }

    public static void setDepthNodeNameSet() {
        MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeNameSet = new HashSet<>();
        Collection<MyNode> depthNodes = MySequentialGraphVars.g.getDepthNodes();
        for (MyNode depthNode : depthNodes) {
            MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeNameSet.add(depthNode.getName());
        }
    }
    public static void removeBarChartsFromViewer() {
        if (MySequentialGraphVars.getSequentialGraphViewer().clusteredGraphLevelNodeValueBarChart != null) {
            MySequentialGraphVars.getSequentialGraphViewer().remove(MySequentialGraphVars.getSequentialGraphViewer().clusteredGraphLevelNodeValueBarChart);
        }
        if (MySequentialGraphVars.getSequentialGraphViewer().graphLevelNodeValueBarChart != null) {
            MySequentialGraphVars.getSequentialGraphViewer().remove(MySequentialGraphVars.getSequentialGraphViewer().graphLevelNodeValueBarChart);
        }
        if (MySequentialGraphVars.getSequentialGraphViewer().nodeLevelNeighborNodeValueBarChart != null) {
            MySequentialGraphVars.getSequentialGraphViewer().remove(MySequentialGraphVars.getSequentialGraphViewer().nodeLevelNeighborNodeValueBarChart);
        }
        if (MySequentialGraphVars.getSequentialGraphViewer().multiNodeLevelEdgeValueBarChart != null) {
            MySequentialGraphVars.getSequentialGraphViewer().remove(MySequentialGraphVars.getSequentialGraphViewer().multiNodeLevelEdgeValueBarChart);
        }
        if (MySequentialGraphVars.getSequentialGraphViewer().sharedNodeValueBarChart != null) {
            MySequentialGraphVars.getSequentialGraphViewer().remove(MySequentialGraphVars.getSequentialGraphViewer().sharedNodeValueBarChart);
        }
        if (MySequentialGraphVars.getSequentialGraphViewer().depthNodeLevelNodeValueBarChart != null) {
            MySequentialGraphVars.getSequentialGraphViewer().remove(MySequentialGraphVars.getSequentialGraphViewer().depthNodeLevelNodeValueBarChart);
        }
        if (MySequentialGraphVars.getSequentialGraphViewer().depthNodeLevelNeighborNodeValueBarChart != null) {
            MySequentialGraphVars.getSequentialGraphViewer().remove(MySequentialGraphVars.getSequentialGraphViewer().depthNodeLevelNeighborNodeValueBarChart);
        }
    }

    public static void setExcludeSymbolOption() {
        MySequentialGraphVars.getSequentialGraphViewer().vc.setNodeValueExcludeSymbolComboBox();

    }

    public static void setBottomCharts() {
        if (MySequentialGraphVars.currentGraphDepth > 0) {
            if (MySequentialGraphVars.getSequentialGraphViewer().vc.selectedNodeNeighborNodeTypeSelector.isShowing()) {
               // MySequentialGraphVars.app.getSequentialGraphDashboard().singleNodePredecessorEdgeCurrentValueDistributionLineChart.decorate();
              //  MySequentialGraphVars.app.getSequentialGraphDashboard().singleNodeSuccessorEdgeCurrentValueDistributionLineChart.decorate();
            } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthNeighborNodeTypeSelector.isShowing()) {
              //  MySequentialGraphVars.app.getSequentialGraphDashboard().depthLevelNodeHopCountDistribution.decorate();
              //  MySequentialGraphVars.app.getSequentialGraphDashboard().depthLevelNodeCurrentValueDistribution.decorate();
              //  MySequentialGraphVars.app.getSequentialGraphDashboard().depthLevelEdgeCurrentValueDistribution.decorate();
            } else {
               // MySequentialGraphVars.app.getSequentialGraphDashboard().graphLevelSuccessorCountDistributionLineGraph.setSuccessorCountLineChart();
              //  MySequentialGraphVars.app.getSequentialGraphDashboard().graphLevelNodeHopCountDistributionLineGraph.decorate();
              //  MySequentialGraphVars.app.getSequentialGraphDashboard().graphLevelEdgeValueDistributionLineGraph.setEdgeValueLineChart();
              //  MySequentialGraphVars.app.getSequentialGraphDashboard().graphLevelNodeValueDistributionLineGraph.decorate();
              //  MySequentialGraphVars.app.getSequentialGraphDashboard().graphLevelSuccessorCountDistributionLineGraph.setSuccessorCountLineChart();
            }
        } else if (MySequentialGraphVars.getSequentialGraphViewer().multiNodes != null &&
            MySequentialGraphVars.getSequentialGraphViewer().multiNodes.size() > 0) {
          //  MySequentialGraphVars.app.getSequentialGraphDashboard().multiNodePredecessorValueDistributionLineChart.decorateValueLineChart();
          //  MySequentialGraphVars.app.getSequentialGraphDashboard().multiNodeSuccessorValueDistributionLineChart.decorateValueChart();
          //  MySequentialGraphVars.app.getSequentialGraphDashboard().multiNodeSharedPredecessorEdgeValueDistributionLineChart.decorate();
          //  MySequentialGraphVars.app.getSequentialGraphDashboard().multiNodeSharedSuccessorEdgeValueDistributionLineChart.decorate();
        } else if (MySequentialGraphVars.getSequentialGraphViewer().selectedNode != null) {
            //MySequentialGraphVars.app.getSequentialGraphDashboard().singleNodePredecessorEdgeCurrentValueDistributionLineChart.decorate();
           // MySequentialGraphVars.app.getSequentialGraphDashboard().singleNodeSuccessorEdgeCurrentValueDistributionLineChart.decorate();
        } else {
         //   MySequentialGraphVars.app.getSequentialGraphDashboard().graphLevelEdgeValueDistributionLineGraph.decorate();
        }
      //  MySequentialGraphVars.app.getSequentialGraphDashboard().revalidate();
     //   MySequentialGraphVars.app.getSequentialGraphDashboard().repaint();
    }

    public static void setDefaultViewerLook() {
        try {
            MyProgressBar pb = new MyProgressBar(false);
            if (MySequentialGraphVars.getSequentialGraphViewer().selectedNode != null) MySequentialGraphVars.getSequentialGraphViewer().selectedNode.pathLengthByDepthMap = null;
            MySequentialGraphVars.getSequentialGraphViewer().selectedNode = null;
            MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeNameSet = null;
            MySequentialGraphVars.getSequentialGraphViewer().vc.selectedNodePredecessorDepthNodeMap = null;
            MySequentialGraphVars.getSequentialGraphViewer().vc.selectedNodeSuccessorDepthNodeMap = null;
            MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodePredecessorMaps = null;
            MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeSuccessorMaps = null;
            MySequentialGraphVars.getSequentialGraphViewer().selectedTableNodeSet = null;
            MySequentialGraphVars.getSequentialGraphViewer().selectedSingleNodePredecessors = new HashSet<>();
            MySequentialGraphVars.getSequentialGraphViewer().selectedSingleNodeSuccessors = new HashSet<>();
            MySequentialGraphVars.getSequentialGraphViewer().multiNodes = null;
            MySequentialGraphVars.getSequentialGraphViewer().multiNodePredecessors = new HashSet<>();
            MySequentialGraphVars.getSequentialGraphViewer().multiNodeSuccessors = new HashSet<>();
            MySequentialGraphVars.getSequentialGraphViewer().sharedSuccessors = new HashSet<>();
            MySequentialGraphVars.getSequentialGraphViewer().sharedPredecessors = new HashSet<>();
            MySequentialGraphVars.getSequentialGraphViewer().predecessorsOnly = false;
            MySequentialGraphVars.getSequentialGraphViewer().successorsOnly = false;
            MySequentialGraphVars.getSequentialGraphViewer().neighborsOnly = false;
            MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueExcludeTxt.setText("");
            MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueExcludeTxt.setText("");
            MySequentialGraphVars.getSequentialGraphViewer().excluded = false;
            MySequentialGraphVars.getSequentialGraphViewer().isClustered = false;
            MySequentialGraphVars.currentGraphDepth = 0;
            MyViewerControlComponentUtil.setEdgeValueSelecterMenu();
            MyViewerControlComponentUtil.setDepthValueSelecterMenu();
            MyViewerControlComponentUtil.setNodeValueComboBoxMenu();
            pb.updateValue(60, 100);

            MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueExcludeSymbolSelecter.removeActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
            MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueExcludeSymbolSelecter.removeActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
            MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueBarChart.removeActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
            MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueBarChart.removeActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
            MySequentialGraphVars.getSequentialGraphViewer().vc.weightedNodeColor.removeActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
            MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.removeActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
            MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.removeActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
            MySequentialGraphVars.getSequentialGraphViewer().vc.edgeLabelSelecter.removeActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
            MySequentialGraphVars.getSequentialGraphViewer().vc.nodeLabelSelecter.removeActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
            MySequentialGraphVars.getSequentialGraphViewer().vc.clusteringSelector.removeActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
            if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthNeighborNodeTypeSelector != null) {
                MySequentialGraphVars.getSequentialGraphViewer().vc.depthNeighborNodeTypeSelector.removeActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
                if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthNeighborNodeTypeSelector.getItemCount() > 0) {
                    MySequentialGraphVars.getSequentialGraphViewer().vc.depthNeighborNodeTypeSelector.setSelectedIndex(0);
                }
                MySequentialGraphVars.getSequentialGraphViewer().vc.depthNeighborNodeTypeSelector.addActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
            }
            MySequentialGraphVars.getSequentialGraphViewer().vc.clusteringSelector.setSelectedIndex(0);
            MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueBarChart.setSelected(false);
            MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueBarChart.setSelected(false);
            MySequentialGraphVars.getSequentialGraphViewer().vc.weightedNodeColor.setSelected(false);
            MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.setSelectedIndex(0);
            MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.setSelectedIndex(1);
            MySequentialGraphVars.getSequentialGraphViewer().vc.edgeLabelSelecter.setSelectedIndex(0);
            MySequentialGraphVars.getSequentialGraphViewer().vc.nodeLabelSelecter.setSelectedIndex(1);
            MySequentialGraphVars.getSequentialGraphViewer().vc.depthExcludeSymbolSelecter.setSelectedIndex(0);
            MySequentialGraphVars.getSequentialGraphViewer().vc.depthExcludeSelecter.setSelectedIndex(0);
            MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueExcludeTxt.setText("");
            MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueExcludeTxt.setText("");
            MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueExcludeSymbolSelecter.setSelectedIndex(0);
            MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueExcludeSymbolSelecter.setSelectedIndex(0);
            MySequentialGraphVars.getSequentialGraphViewer().vc.clusteringSelector.addActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
            MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueExcludeSymbolSelecter.addActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
            MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueExcludeSymbolSelecter.addActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
            MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueBarChart.addActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
            MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueBarChart.addActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
            MySequentialGraphVars.getSequentialGraphViewer().vc.weightedNodeColor.addActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
            MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.addActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
            MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.addActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
            MySequentialGraphVars.getSequentialGraphViewer().vc.edgeLabelSelecter.addActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
            MySequentialGraphVars.getSequentialGraphViewer().vc.nodeLabelSelecter.addActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
            MySequentialGraphVars.getSequentialGraphViewer().vc.selectedNodeNeighborNodeTypeSelector.setVisible(false);
            MySequentialGraphVars.getSequentialGraphViewer().vc.depthNeighborNodeTypeSelector.setVisible(false);
            MySequentialGraphVars.getSequentialGraphViewer().vc.tableTabbedPane.setSelectedIndex(0);

            pb.updateValue(80, 100);
            MySequentialGraphVars.sequentialGraphDashBoard.setGraphLevelDashBoard();
            pb.updateValue(90, 100);

            MySequentialGraphVars.getSequentialGraphViewer().vc.nodeListTable.clearSelection();
            MySequentialGraphVars.getSequentialGraphViewer().vc.currentNodeListTable.clearSelection();
            MySequentialGraphVars.getSequentialGraphViewer().vc.statTable.clearSelection();
            MySequentialGraphVars.getSequentialGraphViewer().vc.edgeListTable.clearSelection();
//            MySequentialGraphVars.getSequentialGraphViewer().vc.pathSourceTable.clearSelection();
//            MySequentialGraphVars.getSequentialGraphViewer().vc.pathDestTable.clearSelection();

            MyNodeUtil.setDefaultValuesToNodes();
            MyEdgeUtil.setDefaultValuesToEdges();
            MySequentialGraphVars.getSequentialGraphViewer().vc.updateTableInfos();
            MySequentialGraphVars.getSequentialGraphViewer().vc.nodeLabelSelecter.setSelectedIndex(1);
            MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueBarChart.setText("N. V. B.");
            MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueBarChart.setText("E. V. B.");

            MyViewerControlComponentUtil.removeBarChartsFromViewer();
            MyViewerControlComponentUtil.removeEdgeValueBarChartFromViewer();
            MySequentialGraphVars.getSequentialGraphViewer().getRenderContext().setEdgeDrawPaintTransformer(MySequentialGraphVars.getSequentialGraphViewer().edgeColor);

            if (MySequentialGraphVars.getSequentialGraphViewer().vc.graphRemovalPanel != null) {
                MySequentialGraphVars.getSequentialGraphViewer().vc.graphRemovalPanel.setVisible(true);
            }
            MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.setVisible(true);
            MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecterLabel.setVisible(true);

            pb.updateValue(95, 100);
            pb.updateValue(100, 100);
            //MySequentialGraphVars.getSequentialGraphViewer().vc.vTxtStat.setTextStatistics();
            MySequentialGraphVars.getSequentialGraphViewer().revalidate();
            MySequentialGraphVars.getSequentialGraphViewer().repaint();
            pb.dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void setClusteringDefaultViewerLook() {
        try {
            MyProgressBar pb = new MyProgressBar(false);
            if (MySequentialGraphVars.getSequentialGraphViewer().selectedNode != null) MySequentialGraphVars.getSequentialGraphViewer().selectedNode.pathLengthByDepthMap = null;
            MySequentialGraphVars.getSequentialGraphViewer().selectedNode = null;
            MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeNameSet = null;
            MySequentialGraphVars.getSequentialGraphViewer().vc.selectedNodePredecessorDepthNodeMap = null;
            MySequentialGraphVars.getSequentialGraphViewer().vc.selectedNodeSuccessorDepthNodeMap = null;
            MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodePredecessorMaps = null;
            MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeSuccessorMaps = null;
            MySequentialGraphVars.getSequentialGraphViewer().selectedTableNodeSet = null;
            MySequentialGraphVars.getSequentialGraphViewer().multiNodes = null;
            MySequentialGraphVars.getSequentialGraphViewer().selectedSingleNodePredecessors = new HashSet<>();
            MySequentialGraphVars.getSequentialGraphViewer().selectedSingleNodeSuccessors = new HashSet<>();
            MySequentialGraphVars.getSequentialGraphViewer().multiNodePredecessors = new HashSet<>();
            MySequentialGraphVars.getSequentialGraphViewer().multiNodeSuccessors = new HashSet<>();
            MySequentialGraphVars.getSequentialGraphViewer().sharedSuccessors = new HashSet<>();
            MySequentialGraphVars.getSequentialGraphViewer().sharedPredecessors = new HashSet<>();
            MySequentialGraphVars.getSequentialGraphViewer().neighborsOnly = false;
            MySequentialGraphVars.getSequentialGraphViewer().predecessorsOnly = false;
            MySequentialGraphVars.getSequentialGraphViewer().successorsOnly = false;
            MySequentialGraphVars.getSequentialGraphViewer().excluded = false;
            MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueExcludeTxt.setText("");
            MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueExcludeTxt.setText("");
            pb.updateValue(60, 100);

            MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueExcludeSymbolSelecter.removeActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
            MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueExcludeSymbolSelecter.removeActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
            MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueBarChart.removeActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
            MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueBarChart.removeActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
            MySequentialGraphVars.getSequentialGraphViewer().vc.weightedNodeColor.removeActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
            MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.removeActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
            MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.removeActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
            MySequentialGraphVars.getSequentialGraphViewer().vc.edgeLabelSelecter.removeActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
            MySequentialGraphVars.getSequentialGraphViewer().vc.nodeLabelSelecter.removeActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
            MySequentialGraphVars.getSequentialGraphViewer().vc.clusteringSelector.removeActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
            MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueBarChart.setSelected(false);
            MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueBarChart.setSelected(false);
            MySequentialGraphVars.getSequentialGraphViewer().vc.weightedNodeColor.setSelected(false);
            MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.setSelectedIndex(0);
            MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.setSelectedIndex(1);
            MySequentialGraphVars.getSequentialGraphViewer().vc.edgeLabelSelecter.setSelectedIndex(0);
            MySequentialGraphVars.getSequentialGraphViewer().vc.nodeLabelSelecter.setSelectedIndex(1);
            MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueExcludeTxt.setText("");
            MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueExcludeTxt.setText("");
            MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueExcludeSymbolSelecter.setSelectedIndex(0);
            MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueExcludeSymbolSelecter.setSelectedIndex(0);
            MySequentialGraphVars.getSequentialGraphViewer().vc.depthExcludeSymbolSelecter.setSelectedIndex(0);
            MySequentialGraphVars.getSequentialGraphViewer().vc.depthExcludeSelecter.setSelectedIndex(0);
            MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueExcludeSymbolSelecter.addActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
            MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueExcludeSymbolSelecter.addActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
            MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueBarChart.addActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
            MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueBarChart.addActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
            MySequentialGraphVars.getSequentialGraphViewer().vc.weightedNodeColor.addActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
            MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.addActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
            MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.addActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
            MySequentialGraphVars.getSequentialGraphViewer().vc.edgeLabelSelecter.addActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
            MySequentialGraphVars.getSequentialGraphViewer().vc.nodeLabelSelecter.addActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
            MySequentialGraphVars.getSequentialGraphViewer().vc.selectedNodeNeighborNodeTypeSelector.setVisible(false);
            MySequentialGraphVars.getSequentialGraphViewer().vc.depthNeighborNodeTypeSelector.setVisible(false);

            pb.updateValue(80, 100);
            MySequentialGraphVars.getSequentialGraphViewer().vc.nodeListTable.clearSelection();
            MySequentialGraphVars.getSequentialGraphViewer().vc.currentNodeListTable.clearSelection();
            MySequentialGraphVars.getSequentialGraphViewer().vc.statTable.clearSelection();
            MySequentialGraphVars.getSequentialGraphViewer().vc.edgeListTable.clearSelection();
            MySequentialGraphVars.getSequentialGraphViewer().vc.pathSourceTable.clearSelection();
            MySequentialGraphVars.getSequentialGraphViewer().vc.pathDestTable.clearSelection();

            MyNodeUtil.setClusteringDefaultValuesToNodes();
            MyEdgeUtil.setClusteringDefaultValuesToEdges();
            MySequentialGraphVars.getSequentialGraphViewer().vc.updateTableInfos();
            MySequentialGraphVars.getSequentialGraphViewer().vc.nodeLabelSelecter.setSelectedIndex(1);
            MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueBarChart.setText("C. N. V. B.");
            MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueBarChart.setText("C. E. V. B.");
            pb.updateValue(95, 100);

            MyViewerControlComponentUtil.removeBarChartsFromViewer();
            MyViewerControlComponentUtil.removeEdgeValueBarChartFromViewer();
            MySequentialGraphVars.getSequentialGraphViewer().getRenderContext().setEdgeDrawPaintTransformer(MySequentialGraphVars.getSequentialGraphViewer().edgeColor);
            MySequentialGraphVars.getSequentialGraphViewer().vc.vTxtStat.setClusterTextStatistics();
            MySequentialGraphVars.getSequentialGraphViewer().revalidate();
            MySequentialGraphVars.getSequentialGraphViewer().repaint();
            pb.updateValue(100, 100);
            pb.dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static void setGraphLevelTableStatistics() {
        DefaultTableModel dm = (DefaultTableModel) MySequentialGraphVars.getSequentialGraphViewer().vc.statTable.getModel();
        dm.getDataVector().removeAllElements();

        ((DefaultTableModel) MySequentialGraphVars.getSequentialGraphViewer().vc.statTable.getModel()).addRow(new String[]{"NODES", MyMathUtil.getCommaSeperatedNumber(MySequentialGraphVars.g.getGraphNodeCount())});
        ((DefaultTableModel) MySequentialGraphVars.getSequentialGraphViewer().vc.statTable.getModel()).addRow(new String[]{"I. S.", MyMathUtil.getCommaSeperatedNumber(MySequentialGraphVars.g.getIsolatedNodeCount())});
        ((DefaultTableModel) MySequentialGraphVars.getSequentialGraphViewer().vc.statTable.getModel()).addRow(new String[]{"EDGES", MyMathUtil.getCommaSeperatedNumber(MySequentialGraphVars.g.getGraphEdgeCount())});
        ((DefaultTableModel) MySequentialGraphVars.getSequentialGraphViewer().vc.statTable.getModel()).addRow(new String[]{"DIAMETER", MyMathUtil.getCommaSeperatedNumber(MySequentialGraphVars.diameter)});

        ((DefaultTableModel) MySequentialGraphVars.getSequentialGraphViewer().vc.statTable.getModel()).addRow(new String[]{"AVG. U. N.", MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getAverageUnreachableNodeCount()))});
        ((DefaultTableModel) MySequentialGraphVars.getSequentialGraphViewer().vc.statTable.getModel()).addRow(new String[]{"MAX. U. N.", MyMathUtil.getCommaSeperatedNumber(MyNodeUtil.getMaxUnreachableNodeCount())});
        ((DefaultTableModel) MySequentialGraphVars.getSequentialGraphViewer().vc.statTable.getModel()).addRow(new String[]{"MIN. U. N.", MyMathUtil.getCommaSeperatedNumber(MyNodeUtil.getMinUnreachableNodeCount())});
        ((DefaultTableModel) MySequentialGraphVars.getSequentialGraphViewer().vc.statTable.getModel()).addRow(new String[]{"STD. U. N.", MyMathUtil.twoDecimalFormat(MyNodeUtil.getUnreachableNodeCountStandardDeviation())});

        ((DefaultTableModel) MySequentialGraphVars.getSequentialGraphViewer().vc.statTable.getModel()).addRow(new String[]{"R. N.", MyMathUtil.getCommaSeperatedNumber(MyNodeUtil.getRedNodeCount()) + "[" + MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getRedNodePercent()*100)) + "]"});
        ((DefaultTableModel) MySequentialGraphVars.getSequentialGraphViewer().vc.statTable.getModel()).addRow(new String[]{"B. N.", MyMathUtil.getCommaSeperatedNumber(MyNodeUtil.getBlueNodeCount()) + "[" + MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getBlueNodePercent()*100)) + "]"});
        ((DefaultTableModel) MySequentialGraphVars.getSequentialGraphViewer().vc.statTable.getModel()).addRow(new String[]{"G. N.", MyMathUtil.getCommaSeperatedNumber(MyNodeUtil.getGreenNodeCount()) + "[" + MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getGreenNodePercent()*100)) + "]"});

        ((DefaultTableModel) MySequentialGraphVars.getSequentialGraphViewer().vc.statTable.getModel()).addRow(new String[]{"AVG. N. V.", MySequentialGraphSysUtil.formatAverageValue(MyNodeUtil.getAverageNodeValue())});
        ((DefaultTableModel) MySequentialGraphVars.getSequentialGraphViewer().vc.statTable.getModel()).addRow(new String[]{"MAX. N. V.", MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getMaximumNodeValue()))});
        ((DefaultTableModel) MySequentialGraphVars.getSequentialGraphViewer().vc.statTable.getModel()).addRow(new String[]{"MIN. N. V.", MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getMinimumNodeValue()))});
        ((DefaultTableModel) MySequentialGraphVars.getSequentialGraphViewer().vc.statTable.getModel()).addRow(new String[]{"STD. N. V.", MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getNodeValueStandardDeviation()))});

        ((DefaultTableModel) MySequentialGraphVars.getSequentialGraphViewer().vc.statTable.getModel()).addRow(new String[]{"MAX. IN-N.", MyMathUtil.getCommaSeperatedNumber(MyNodeUtil.getMaximumInNodeCount())});
        ((DefaultTableModel) MySequentialGraphVars.getSequentialGraphViewer().vc.statTable.getModel()).addRow(new String[]{"MIN. IN-N.", MyMathUtil.getCommaSeperatedNumber(MyNodeUtil.getMinimumInNodeCount())});
        ((DefaultTableModel) MySequentialGraphVars.getSequentialGraphViewer().vc.statTable.getModel()).addRow(new String[]{"AVG. IN-N.", MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getAverageInNodeCount()))});
        ((DefaultTableModel) MySequentialGraphVars.getSequentialGraphViewer().vc.statTable.getModel()).addRow(new String[]{"STD. IN-N.", MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getInNodeCountStandardDeviation()))});

        ((DefaultTableModel) MySequentialGraphVars.getSequentialGraphViewer().vc.statTable.getModel()).addRow(new String[]{"MAX. OUT-N.", MyMathUtil.getCommaSeperatedNumber(MyNodeUtil.getMaximumOutNodeCount())});
        ((DefaultTableModel) MySequentialGraphVars.getSequentialGraphViewer().vc.statTable.getModel()).addRow(new String[]{"MIN. OUT-N.", MyMathUtil.getCommaSeperatedNumber(MyNodeUtil.getMinimumOutNodeCount())});
        ((DefaultTableModel) MySequentialGraphVars.getSequentialGraphViewer().vc.statTable.getModel()).addRow(new String[]{"AVG. OUT-N.", MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getAverageSuccessorCount()))});
        ((DefaultTableModel) MySequentialGraphVars.getSequentialGraphViewer().vc.statTable.getModel()).addRow(new String[]{"STD. OUT.N.", MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getOutNodeCountStandardDeviation()))});

        ((DefaultTableModel) MySequentialGraphVars.getSequentialGraphViewer().vc.statTable.getModel()).addRow(new String[]{"MAX. IN-C.", MyMathUtil.getCommaSeperatedNumber(MyNodeUtil.getMaximumInContribution())});
        ((DefaultTableModel) MySequentialGraphVars.getSequentialGraphViewer().vc.statTable.getModel()).addRow(new String[]{"MIN. IN-C.", MyMathUtil.getCommaSeperatedNumber(MyNodeUtil.getMinimumInContribution())});
        ((DefaultTableModel) MySequentialGraphVars.getSequentialGraphViewer().vc.statTable.getModel()).addRow(new String[]{"AVG. IN-C.", MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getAverageInContribution()))});
        ((DefaultTableModel) MySequentialGraphVars.getSequentialGraphViewer().vc.statTable.getModel()).addRow(new String[]{"STD. IN-C.", MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getInContributionStandardDeviation()))});

        ((DefaultTableModel) MySequentialGraphVars.getSequentialGraphViewer().vc.statTable.getModel()).addRow(new String[]{"MAX. OUT-C.", MyMathUtil.getCommaSeperatedNumber(MyNodeUtil.getMaximumOutContribution())});
        ((DefaultTableModel) MySequentialGraphVars.getSequentialGraphViewer().vc.statTable.getModel()).addRow(new String[]{"MIN. OUT-C.", MyMathUtil.getCommaSeperatedNumber(MyNodeUtil.getMinimumOutContribution())});
        ((DefaultTableModel) MySequentialGraphVars.getSequentialGraphViewer().vc.statTable.getModel()).addRow(new String[]{"AVG. OUT-C.", MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getAverageOutContribution()))});
        ((DefaultTableModel) MySequentialGraphVars.getSequentialGraphViewer().vc.statTable.getModel()).addRow(new String[]{"STD. OUT-C.", MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getOutContributionStandardDeviation()))});

        ((DefaultTableModel) MySequentialGraphVars.getSequentialGraphViewer().vc.statTable.getModel()).addRow(new String[]{"AVG. UNIQ. N. C.", MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getAverageUniqueContribution()))});
        ((DefaultTableModel) MySequentialGraphVars.getSequentialGraphViewer().vc.statTable.getModel()).addRow(new String[]{"MIN. UNIQ. N. C.", MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getMaximumUniqueContribution()))});
        ((DefaultTableModel) MySequentialGraphVars.getSequentialGraphViewer().vc.statTable.getModel()).addRow(new String[]{"MAX. UNIQ. N. C.", MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getMinimumUniqueContribution()))});
        ((DefaultTableModel) MySequentialGraphVars.getSequentialGraphViewer().vc.statTable.getModel()).addRow(new String[]{"STD. UNIQ. N. C.", MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyNodeUtil.getUniqueContributionStandardDeviation()))});

        ((DefaultTableModel) MySequentialGraphVars.getSequentialGraphViewer().vc.statTable.getModel()).addRow(new String[]{"AVG. UNIQ. E. C.", MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyEdgeUtil.getAverageUniqueContribution()))});
        ((DefaultTableModel) MySequentialGraphVars.getSequentialGraphViewer().vc.statTable.getModel()).addRow(new String[]{"MAX. UNIQ. E. C.", MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyEdgeUtil.getMaximumUniqueContribution()))});
        ((DefaultTableModel) MySequentialGraphVars.getSequentialGraphViewer().vc.statTable.getModel()).addRow(new String[]{"MIN. UNIQ. E. C.", MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyEdgeUtil.getMinimumUniqueContribution()))});
        ((DefaultTableModel) MySequentialGraphVars.getSequentialGraphViewer().vc.statTable.getModel()).addRow(new String[]{"STD. UNIQ. E. C.", MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyEdgeUtil.getUniqueContributionStandardDeviation()))});

        // Edge stats.
        ((DefaultTableModel) MySequentialGraphVars.getSequentialGraphViewer().vc.statTable.getModel()).addRow(new String[]{"AVG. E. V.", MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyEdgeUtil.getAverageEdgeValue()))});
        ((DefaultTableModel) MySequentialGraphVars.getSequentialGraphViewer().vc.statTable.getModel()).addRow(new String[]{"MAX. E. V.", MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyEdgeUtil.getMaximumEdgeValue()))});
        ((DefaultTableModel) MySequentialGraphVars.getSequentialGraphViewer().vc.statTable.getModel()).addRow(new String[]{"MIN. E. V.", MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyEdgeUtil.getMinimumEdgeValue()))});
        ((DefaultTableModel) MySequentialGraphVars.getSequentialGraphViewer().vc.statTable.getModel()).addRow(new String[]{"STD. E. V.", MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MyEdgeUtil.getEdgeValueStandardDeviation()))});

        // Avg. shortest distance.
        ((DefaultTableModel) MySequentialGraphVars.getSequentialGraphViewer().vc.statTable.getModel()).addRow(new String[]{"AVG. SHORT. D.", MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(MySequentialGraphSysUtil.getAverageShortestDistance()))});
        ((DefaultTableModel) MySequentialGraphVars.getSequentialGraphViewer().vc.statTable.getModel()).addRow(new String[]{"NO. OF GRAPHS", MyMathUtil.getCommaSeperatedNumber(MySequentialGraphVars.numberOfGraphs)});

        dm.fireTableDataChanged();
        MySequentialGraphVars.getSequentialGraphViewer().revalidate();
        MySequentialGraphVars.getSequentialGraphViewer().repaint();
    }

    public static void showEdgeLabel() {
        if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeLabelSelecter.getSelectedIndex() > 0) {
            MySequentialGraphVars.getSequentialGraphViewer().getRenderContext().setEdgeLabelTransformer(new Transformer<MyEdge, String>() {@Override
            public String transform(MyEdge e) {
                return e.edgeLabelMap.get(MySequentialGraphVars.getSequentialGraphViewer().vc.edgeLabelSelecter.getSelectedItem().toString().replaceAll(" ", ""));
            }});
        } else {
            MySequentialGraphVars.getSequentialGraphViewer().getRenderContext().setEdgeLabelTransformer(new Transformer<MyEdge, String>() {
                @Override public String transform(MyEdge e) {
                    return "";}}
            );
        }
        MySequentialGraphVars.getSequentialGraphViewer().revalidate();
        MySequentialGraphVars.getSequentialGraphViewer().repaint();
    }

    public static void showEdgeValue() {
        MySequentialGraphVars.getSequentialGraphViewer().edgeValName = MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedItem().toString().replaceAll(" ", "");
        if (MySequentialGraphVars.getSequentialGraphViewer().edgeValName.contains("NONE")) {
            MyMessageUtil.showErrorMsg("Select an edge value, first!");return;}
        new Thread(new Runnable() {@Override public void run() {
            MyEdgeValueRankFrame edgeValueRankFrame = new MyEdgeValueRankFrame();}}).start();
        MySequentialGraphVars.getSequentialGraphViewer().getRenderContext().setEdgeLabelTransformer(new Transformer<MyEdge, String>() {@Override
        public String transform(MyEdge e) {
            if (MySequentialGraphVars.getSequentialGraphViewer().selectedNode != null) {
                if (e.getSource() == MySequentialGraphVars.getSequentialGraphViewer().selectedNode || e.getDest() == MySequentialGraphVars.getSequentialGraphViewer().selectedNode) {
                    String value = MyMathUtil.getCommaSeperatedNumber((long)e.getCurrentValue());
                    if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeLabelSelecter.getSelectedIndex() > 0) {
                        return e.edgeLabelMap.get(MySequentialGraphVars.getSequentialGraphViewer().vc.edgeLabelSelecter.getSelectedItem().toString())+"["+ MySequentialGraphSysUtil.formatAverageValue(MySequentialGraphSysUtil.formatAverageValue(value))+"]";
                    } else {return MySequentialGraphSysUtil.formatAverageValue(value);}
                } else {
                    if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeLabelSelecter.getSelectedIndex() > 0) {
                        return e.edgeLabelMap.get(MySequentialGraphVars.getSequentialGraphViewer().vc.edgeLabelSelecter.getSelectedItem().toString());
                    } else {return "";}
                }
            } else {
                String value = MyMathUtil.getCommaSeperatedNumber((long)e.getCurrentValue());
                if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeLabelSelecter.getSelectedIndex() > 0) {
                    return e.edgeLabelMap.get(MySequentialGraphVars.getSequentialGraphViewer().vc.edgeLabelSelecter.getSelectedItem().toString())+"["+ MySequentialGraphSysUtil.formatAverageValue(MySequentialGraphSysUtil.formatAverageValue(value))+"]";
                } else {return MySequentialGraphSysUtil.formatAverageValue(value);}
            }}});
        MySequentialGraphVars.getSequentialGraphViewer().revalidate();
        MySequentialGraphVars.getSequentialGraphViewer().repaint();
    }

    public static void showNodeValue() {
        new Thread(new Runnable() {@Override public void run() {
            MyNodeValueRankFrame nodeValueRankFrame = new MyNodeValueRankFrame();}}).start();
        MySequentialGraphVars.getSequentialGraphViewer().getRenderContext().setVertexLabelTransformer(new Transformer<MyNode, String>() {@Override
        public String transform(MyNode n) {
            String value = MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(n.getCurrentValue()));
            value = (value.endsWith(".0") || value.endsWith(".00") ? value.substring(0, value.indexOf(".0")) : value);
            if (MySequentialGraphVars.getSequentialGraphViewer().selectedNode != null) {
                if (n == MySequentialGraphVars.getSequentialGraphViewer().selectedNode || MySequentialGraphVars.getSequentialGraphViewer().selectedSingleNodePredecessors.contains(n) ||
                        MySequentialGraphVars.getSequentialGraphViewer().selectedSingleNodeSuccessors.contains(n)) {
                    return value;
                } else {return "";}
            }
            return value;
        }});
    }

    public static void setSharedNodeLevelNodeValueBarChart() {
        if (MySequentialGraphVars.getSequentialGraphViewer().graphLevelNodeValueBarChart != null) {
            MySequentialGraphVars.getSequentialGraphViewer().remove(MySequentialGraphVars.getSequentialGraphViewer().graphLevelNodeValueBarChart);
        }
        if (MySequentialGraphVars.getSequentialGraphViewer().nodeLevelNeighborNodeValueBarChart != null) {
            MySequentialGraphVars.getSequentialGraphViewer().remove(MySequentialGraphVars.getSequentialGraphViewer().nodeLevelNeighborNodeValueBarChart);
        }
        if (MySequentialGraphVars.getSequentialGraphViewer().sharedNodeValueBarChart != null) {
            MySequentialGraphVars.getSequentialGraphViewer().remove(MySequentialGraphVars.getSequentialGraphViewer().sharedNodeValueBarChart);
        }
        MySequentialGraphVars.getSequentialGraphViewer().sharedNodeValueBarChart = new MyMultiLevelNeighborNodeValueBarChart();
        MySequentialGraphVars.getSequentialGraphViewer().add(MySequentialGraphVars.getSequentialGraphViewer().sharedNodeValueBarChart);
    }

    public static void removeSharedNodeValueBarCharts() {
        if (MySequentialGraphVars.getSequentialGraphViewer().sharedNodeValueBarChart != null) {
            MySequentialGraphVars.getSequentialGraphViewer().remove(MySequentialGraphVars.getSequentialGraphViewer().sharedNodeValueBarChart);
        }
    }

    public static void setShareNodeLevelEdgeValueBarChartToViewer() {
        if (MySequentialGraphVars.getSequentialGraphViewer().graphLelvelEdgeValueBarChart != null) {
            MySequentialGraphVars.getSequentialGraphViewer().remove(MySequentialGraphVars.getSequentialGraphViewer().graphLelvelEdgeValueBarChart);
        }
        if (MySequentialGraphVars.getSequentialGraphViewer().multiNodeLevelEdgeValueBarChart != null) {
            MySequentialGraphVars.getSequentialGraphViewer().remove(MySequentialGraphVars.getSequentialGraphViewer().multiNodeLevelEdgeValueBarChart);
        }
        MySequentialGraphVars.getSequentialGraphViewer().multiNodeLevelEdgeValueBarChart = new MyMultiLevelEdgeValueBarChart();
        MySequentialGraphVars.getSequentialGraphViewer().add(MySequentialGraphVars.getSequentialGraphViewer().multiNodeLevelEdgeValueBarChart);
    }

    public static void showNodeLabel() {
        if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeLabelSelecter.getSelectedIndex() == 0) {
            MySequentialGraphVars.getSequentialGraphViewer().getRenderContext().setVertexLabelTransformer(new Transformer<MyNode, String>() {@Override
            public String transform(MyNode n) {
                return "";
            }
            });
        } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeLabelSelecter.getSelectedIndex() == 1) {
            MySequentialGraphVars.getSequentialGraphViewer().getRenderContext().setVertexLabelTransformer(new Transformer<MyNode, String>() {@Override
            public String transform(MyNode n) {
                String name = "";
                if (n.getName().contains("x")) {
                    name = MySequentialGraphSysUtil.getDecodeVariableNodeName(n.getName());
                } else {
                    name = MySequentialGraphSysUtil.decodeNodeName(n.getName());
                }
                if (MySequentialGraphVars.getSequentialGraphViewer().multiNodes != null && MySequentialGraphVars.getSequentialGraphViewer().multiNodes.size() > 0) {
                    if (MySequentialGraphVars.getSequentialGraphViewer().multiNodes.contains(n) || MySequentialGraphVars.getSequentialGraphViewer().multiNodeSuccessors.contains(n) || MySequentialGraphVars.getSequentialGraphViewer().multiNodePredecessors.contains(n)) {
                        return name;
                    } else {
                        return "";
                    }
                } else if (MySequentialGraphVars.getSequentialGraphViewer().selectedNode != null) {
                    if (MySequentialGraphVars.getSequentialGraphViewer().predecessorsOnly) {
                        if (n == MySequentialGraphVars.getSequentialGraphViewer().selectedNode || MySequentialGraphVars.getSequentialGraphViewer().selectedSingleNodePredecessors.contains(n)) {
                            return name;
                        } else {
                            return "";
                        }
                    } else if (MySequentialGraphVars.getSequentialGraphViewer().successorsOnly) {
                        if (n == MySequentialGraphVars.getSequentialGraphViewer().selectedNode || MySequentialGraphVars.getSequentialGraphViewer().selectedSingleNodeSuccessors.contains(n)) {
                            return name;
                        } else {
                            return "";
                        }
                    } else if (MySequentialGraphVars.getSequentialGraphViewer().neighborsOnly) {
                        if (n == MySequentialGraphVars.getSequentialGraphViewer().selectedNode || MySequentialGraphVars.getSequentialGraphViewer().selectedSingleNodeSuccessors.contains(n) || MySequentialGraphVars.getSequentialGraphViewer().selectedSingleNodePredecessors.contains(n)) {
                            return name;
                        } else {
                            return "";
                        }
                    } else if (n == MySequentialGraphVars.getSequentialGraphViewer().selectedNode || MySequentialGraphVars.getSequentialGraphViewer().selectedSingleNodeSuccessors.contains(n) || MySequentialGraphVars.getSequentialGraphViewer().selectedSingleNodePredecessors.contains(n)) {
                        return name;
                    } else {
                        return "";
                    }
                } else {
                    return name;
                }
            }});
        } else {
            MySequentialGraphVars.getSequentialGraphViewer().getRenderContext().setVertexLabelTransformer(new Transformer<MyNode, String>() {@Override
            public String transform(MyNode n) {
                if (MySequentialGraphVars.getSequentialGraphViewer().multiNodes != null && MySequentialGraphVars.getSequentialGraphViewer().multiNodes.size() > 0) {
                    if (MySequentialGraphVars.getSequentialGraphViewer().multiNodes.contains(n) || MySequentialGraphVars.getSequentialGraphViewer().multiNodeSuccessors.contains(n) || MySequentialGraphVars.getSequentialGraphViewer().multiNodePredecessors.contains(n)) {
                        return n.nodeLabelMap.get(MySequentialGraphVars.getSequentialGraphViewer().vc.nodeLabelSelecter.getSelectedItem().toString());
                    } else {
                        return "";
                    }
                } else if (MySequentialGraphVars.getSequentialGraphViewer().selectedNode != null) {
                    if (MySequentialGraphVars.getSequentialGraphViewer().predecessorsOnly) {
                        if (n == MySequentialGraphVars.getSequentialGraphViewer().selectedNode || MySequentialGraphVars.getSequentialGraphViewer().selectedSingleNodePredecessors.contains(n)) {
                            return n.nodeLabelMap.get(MySequentialGraphVars.getSequentialGraphViewer().vc.nodeLabelSelecter.getSelectedItem().toString());
                        } else {
                            return "";
                        }
                    } else if (MySequentialGraphVars.getSequentialGraphViewer().successorsOnly) {
                        if (n == MySequentialGraphVars.getSequentialGraphViewer().selectedNode || MySequentialGraphVars.getSequentialGraphViewer().selectedSingleNodeSuccessors.contains(n)) {
                            return n.nodeLabelMap.get(MySequentialGraphVars.getSequentialGraphViewer().vc.nodeLabelSelecter.getSelectedItem().toString());
                        } else {
                            return "";
                        }
                    } else if (MySequentialGraphVars.getSequentialGraphViewer().neighborsOnly) {
                        if (n == MySequentialGraphVars.getSequentialGraphViewer().selectedNode || MySequentialGraphVars.getSequentialGraphViewer().selectedSingleNodeSuccessors.contains(n) || MySequentialGraphVars.getSequentialGraphViewer().selectedSingleNodePredecessors.contains(n)) {
                            return n.nodeLabelMap.get(MySequentialGraphVars.getSequentialGraphViewer().vc.nodeLabelSelecter.getSelectedItem().toString());
                        } else {
                            return "";
                        }
                    } else if (n == MySequentialGraphVars.getSequentialGraphViewer().selectedNode || MySequentialGraphVars.getSequentialGraphViewer().selectedSingleNodeSuccessors.contains(n) || MySequentialGraphVars.getSequentialGraphViewer().selectedSingleNodePredecessors.contains(n)) {
                        return n.nodeLabelMap.get(MySequentialGraphVars.getSequentialGraphViewer().vc.nodeLabelSelecter.getSelectedItem().toString());
                    } else {
                        return "";
                    }
                } else {
                    return n.nodeLabelMap.get(MySequentialGraphVars.getSequentialGraphViewer().vc.nodeLabelSelecter.getSelectedItem().toString());
                }
            }
            });
        }
        MySequentialGraphVars.getSequentialGraphViewer().revalidate();
        MySequentialGraphVars.getSequentialGraphViewer().repaint();
    }

    public static void adjustDepthNodeValueMenu() {
        MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.removeActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
        MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.removeAllItems();
        MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.addItem("NONE");
        MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.addItem("DEFALUT");
        MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.addItem("CONT.");
        MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.setSelectedIndex(0);
        MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.addActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);


        if (MySequentialGraphVars.isTimeOn) {
            MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.removeActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
            MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.removeAllItems();
            for (int i = 0; i < MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeValueItems.length; i++) {
                MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.addItem(MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeValueItems[i]);
            }
            MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.addActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);

            String[] depthNodeValueTooltips = new String[13];
            depthNodeValueTooltips[0] = "CONTRIBUTION";
            depthNodeValueTooltips[1] = "IN-CONTRIBUTION";
            depthNodeValueTooltips[2] = "OUT-CONTRIBUTION";
            depthNodeValueTooltips[3] = "INOUT-CONTRIBUTION DIFFERENCE";
            depthNodeValueTooltips[4] = "IN-NODE COUNT";
            depthNodeValueTooltips[5] = "OUT-NODE COUNT";
            depthNodeValueTooltips[6] = "INOUT-NODE DIFFERENCE";
            depthNodeValueTooltips[7] = "DURATION";
            depthNodeValueTooltips[8] = "REACH TIME";
            depthNodeValueTooltips[9] = "MAX. DURATION";
            depthNodeValueTooltips[10] = "MIN. DURATION";
            depthNodeValueTooltips[11] = "MAX. REACH TIME";
            depthNodeValueTooltips[12] = "MIN. REACH TIME";
            MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.setRenderer(new MyComboBoxTooltipRenderer(depthNodeValueTooltips));
        } else {
            MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.removeActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
            MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.removeAllItems();
            for (int i = 0; i < 6; i++) {
                MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.addItem(MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeValueItems[i]);
            }
            MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.addActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);

            String[] depthNodeValueTooltips = new String[6];
            depthNodeValueTooltips[0] = "CONTRIBUTION";
            depthNodeValueTooltips[1] = "IN-CONTRIBUTION";
            depthNodeValueTooltips[2] = "OUT-CONTRIBUTION";
            depthNodeValueTooltips[3] = "IN-NODE COUNT";
            depthNodeValueTooltips[4] = "OUT-NODE COUNT";
            depthNodeValueTooltips[5] = "INOUT-NODE DIFFERENCE";
            MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.setRenderer(new MyComboBoxTooltipRenderer(depthNodeValueTooltips));
        }
        MySequentialGraphVars.getSequentialGraphViewer().vc.nodeLabelSelecter.removeActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
        MySequentialGraphVars.getSequentialGraphViewer().vc.nodeLabelSelecter.setSelectedIndex(0);
        MySequentialGraphVars.getSequentialGraphViewer().vc.nodeLabelSelecter.addActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);

        MySequentialGraphVars.getSequentialGraphViewer().vc.edgeLabelSelecter.removeActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
        MySequentialGraphVars.getSequentialGraphViewer().vc.edgeLabelSelecter.setSelectedIndex(0);
        MySequentialGraphVars.getSequentialGraphViewer().vc.edgeLabelSelecter.addActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
    }

}
