package medousa.sequential.graph.menu;

import medousa.message.MyMessageUtil;
import medousa.sequential.graph.clustering.MyClusteringConfig;
import medousa.sequential.graph.flow.MyFlowExplorerAnalyzer;
import medousa.sequential.utils.MyFontChooser;
import medousa.sequential.utils.MyViewerComponentControllerUtil;
import medousa.sequential.utils.MySequentialGraphVars;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import medousa.sequential.graph.*;
import medousa.sequential.graph.stats.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

public class MyViewerMenu
extends JPopupMenu
implements ActionListener {

    private static boolean isTooltipOff;
    private static boolean isNodeColorWeighted;

    private JMenuItem contributionCountByObjectDistribution = new JMenuItem("CONT. CNT. BY OBJ. DIST.");
    private JMenuItem picking = new JMenuItem("PICKING");
    private JMenuItem tranform = new JMenuItem("TRANSORMING");
    private JMenuItem showNodeLabel = new JMenuItem("NODE LABEL");
    private JMenuItem showNodeValue = new JMenuItem("NODE VALUE");
    private JMenuItem showEdgeValue = new JMenuItem("EDGE VALUE");
    private JMenuItem showEdgeLabel = new JMenuItem("EDGE LABEL");
    private JMenuItem showNodeValueDistribution = new JMenuItem("NODE VALUE DIST.");
    private JMenuItem showEdgeValueDistribution = new JMenuItem("EDGE VALUE DIST.");
    private JMenuItem nodeStatistics = new JMenuItem("NODE STATISTICS");
    private JMenuItem edgeStatistics = new JMenuItem("EDGE STATISTICS");
    private JMenuItem sequenceLengthDistribution = new JMenuItem("SEQ. DIST.");
    private JMenuItem searchNode = new JMenuItem("SEARCH NODE");
    private JMenuItem showEndNodes = new JMenuItem("END NODES");
    private JMenuItem inOutDifferenceByDepthStatistics = new JMenuItem("INOUT CONT. DIFF. BY DEP.");
    private JMenuItem betweenContributionByObjectDistribution = new JMenuItem("BTW. CONT. CNT. DIST. BY OBJ.");
    private JMenuItem betweenReachTimeDistribution = new JMenuItem("BTW. TIME DIST.");
    private JMenuItem avgHopCountDistribution = new JMenuItem("AVG. HOP CNT. DIST.");
    private JMenuItem reachTimeDistribution = new JMenuItem("REACH TIME DIST.");
    private JMenuItem durationDistribution = new JMenuItem("DURATION DIST.");
    private JMenuItem nodesByDepth = new JMenuItem("NODES BY DEPTH");
    private JMenuItem dataFlowGraph = new JMenuItem("DATA FLOWS");
    private JMenuItem clustering = new JMenuItem("CLUSTERING");
    private JMenuItem nodeFont = new JMenuItem("NODE FONT");
    private JMenuItem edgeFont = new JMenuItem("EDGE FONT");
    private JMenuItem nodeToolTipOnOff = new JMenuItem("NODE TOOLTIP-OFF");
    private JMenuItem weightedNodeColor = new JMenuItem("WEIGHT NODE COLORS");

    public MyViewerMenu( ) {
        this.decorate();
    }

    private void decorate() {
        this.setMenuItem(this.picking, "PICKING");
        this.setMenuItem(this.tranform, "TRANSFORMING");
        this.add(new JSeparator());
        this.setMenuItem(this.searchNode, "SEARCH NODE");
        this.add(new JSeparator());
        this.setMenuItem(this.showNodeValue, "SHOW NODE VALUE");
        this.setMenuItem(this.nodeStatistics, "SHOW NODE STATISTICS");
        if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() > 1) {
            this.setMenuItem(this.edgeStatistics, "SHOW EDGE STATISTICS");
        }
        this.setMenuItem(this.nodesByDepth, "SHOW NODES BY DEPTH");
        if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeLabelSelecter.getSelectedIndex() > 1) {
            this.setMenuItem(this.showNodeLabel, "SHOW NODE LABLE");
        }
        if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeLabelSelecter.getSelectedIndex() > 0) {
            this.setMenuItem(this.showEdgeLabel, "SHOW EDGE LABEL");
        }
        if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() > 1) {
            this.setMenuItem(this.showEdgeValue, "SHOW EDGE VALUE");
        }
        if (!isNodeColorWeighted) {
            this.weightedNodeColor.setText("WEIGHT NODE COLORS");
            this.setMenuItem(this.weightedNodeColor, "SHOW UNWEIGHTED NODE COLORS");
        } else {
            this.weightedNodeColor.setText("UNWEIGHT NODE COLORS");
            this.setMenuItem(this.weightedNodeColor, "SHOW UNWEIGHTED NODE COLROS");
        }
        this.add(new JSeparator());
        //this.setMenuItem(this.inOutDifferenceByDepthStatistics, "INOUT-DIFFERENCE BY DEPTH STATISTICS");
        //this.setMenuItem(this.sequenceLengthDistribution, "SEQUENCE LENGTH DISTRIBUTION");
        //this.setMenuItem(this.contributionCountByObjectDistribution, "CONTRIBUTION COUNT BY OBJECT DISTRIBUTION");
        //this.setMenuItem(this.betweenContributionByObjectDistribution, "CONTRIBUTION COUNT BY OBECT DISTRIBUTION BETWEEN TWO NODES");
        //if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() > 1) {
        //    this.setMenuItem(this.showEdgeValueDistribution, "SHOW CURRENT EDGE VALUE DISTRIBUTION");
        //}
        //if (MySequentialGraphVars.isTimeOn) {
        //    this.setMenuItem(this.betweenReachTimeDistribution, "SHOW BETWEEN TIME DISTRIBUTION");
        //    this.setMenuItem(this.reachTimeDistribution, "SHOW REACH TIME DISTRIBUTION");
        //    this.setMenuItem(this.durationDistribution, "SHOW DURATION DISTRIBUTION");
        //}
        //this.add(new JSeparator());
        this.setMenuItem(this.dataFlowGraph, "SHOW DATA FLOW GRAPH");
        this.add(new JSeparator());
        this.setMenuItem(this.nodeFont, "SET NODE FONT");
        this.setMenuItem(this.edgeFont, "SET EDGE FONT");
        this.add(new JSeparator());
        if (!isTooltipOff) {
            this.nodeToolTipOnOff.setText("NODE TOOLTIP-OFF");
            this.setMenuItem(this.nodeToolTipOnOff, "NODE TOOLTIP");
        } else {
            this.nodeToolTipOnOff.setText("NODE TOOLTIP-ON");
            this.setMenuItem(this.nodeToolTipOnOff, "NODE TOOLTIP");
        }
    }

    private void setMenuItem(JMenuItem menuItem, String tooltip) {
        menuItem.setToolTipText(tooltip);
        menuItem.setFont(MySequentialGraphVars.tahomaPlainFont12);
        menuItem.addActionListener(this);
        menuItem.setPreferredSize(new Dimension(220, 26));
        this.add(menuItem);
    }

    @Override public void actionPerformed(ActionEvent e) {
        new Thread(new Runnable() {
            @Override public void run() {
                if (e.getSource() == nodeToolTipOnOff) {
                    if (!isTooltipOff) {
                        MySequentialGraphVars.getSequentialGraphViewer().setVertexToolTipTransformer(null);
                        nodeToolTipOnOff.setText("NODE TOOLTIP-ON");
                        isTooltipOff = true;
                    } else {
                        MySequentialGraphVars.getSequentialGraphViewer().setVertexToolTipTransformer(
                            MySequentialGraphVars.getSequentialGraphViewer().defaultToolTipper
                        );
                        nodeToolTipOnOff.setText("NODE TOOLTIP-OFF");
                        isTooltipOff = false;
                    }
                } else if (e.getSource() == contributionCountByObjectDistribution) {
                    MyGraphLevelTopLevelNodeContributionCountByObjectIDDistribution graphLevelContributionCountByObjectIDDistribution = new MyGraphLevelTopLevelNodeContributionCountByObjectIDDistribution();
                    graphLevelContributionCountByObjectIDDistribution.enlarge();
                } else if (e.getSource() == weightedNodeColor) {
                    if (!isNodeColorWeighted) {
                        weightedNodeColor.setText("UNWEIGHT NODE COLORS");
                        MySequentialGraphVars.getSequentialGraphViewer().getRenderContext().setVertexFillPaintTransformer(MySequentialGraphVars.getSequentialGraphViewer().weightedNodeColor);
                        isNodeColorWeighted = true;
                    } else {
                        weightedNodeColor.setText("WEIGHT NODE COLORS");
                        MySequentialGraphVars.getSequentialGraphViewer().getRenderContext().setVertexFillPaintTransformer(MySequentialGraphVars.getSequentialGraphViewer().unWeightedNodeColor);
                        isNodeColorWeighted = false;
                    }
                } else if (e.getSource() == betweenContributionByObjectDistribution) {
                    MyBetweenContributionDistributionByObjectLineChart betweenContributionDistributionByObject = new MyBetweenContributionDistributionByObjectLineChart();
                    betweenContributionDistributionByObject.enlarge();
                } else if (e.getSource() == picking) {
                    DefaultModalGraphMouse graphMouse = new DefaultModalGraphMouse();
                    graphMouse.setMode(DefaultModalGraphMouse.Mode.PICKING);
                    MySequentialGraphVars.getSequentialGraphViewer().setGraphMouse(graphMouse);
                } else if (e.getSource() == tranform) {
                    DefaultModalGraphMouse graphMouse = new DefaultModalGraphMouse();
                    graphMouse.setMode(DefaultModalGraphMouse.Mode.TRANSFORMING);
                    MySequentialGraphVars.getSequentialGraphViewer().setGraphMouse(graphMouse);
                } else if (e.getSource() == nodesByDepth) {
                    MyNodeDepthAppearnaceStatistics nodeDepthAppearnaceStatistics = new MyNodeDepthAppearnaceStatistics();
                } else if (e.getSource() == nodeStatistics) {
                    MyNodeStatistics nodeStatistics = new MyNodeStatistics();
                } else if (e.getSource() == inOutDifferenceByDepthStatistics) {
                    MyInOutValueDifferenceStatByDepthChart inOutDifferenceStatByDepthChart = new MyInOutValueDifferenceStatByDepthChart();
                    inOutDifferenceStatByDepthChart.enlarge();
                } else if (e.getSource() == edgeStatistics) {
                    MyEdgeStatistics edgeStatistics = new MyEdgeStatistics();
                } else if (e.getSource() == avgHopCountDistribution) {
                    MyGraphLevelNodeAverageHopCountDistributionLineChart hopCountDistribution = new MyGraphLevelNodeAverageHopCountDistributionLineChart();
                    hopCountDistribution.enlarge();
                } else if (e.getSource() == sequenceLengthDistribution) {
                    MyGraphLevelSequenceLengthDistribution sequenceLengthDistribution = new MyGraphLevelSequenceLengthDistribution();
                } else if (e.getSource() == reachTimeDistribution) {
                    MyGraphTopLevelReachTimeDistribution timeDistribution = new MyGraphTopLevelReachTimeDistribution();
                    timeDistribution.enlarge();
                } else if (e.getSource() == betweenReachTimeDistribution) {
                    MyBetweenReachTimeDistributionLineChart betweenReachTimeDistribution = new MyBetweenReachTimeDistributionLineChart();
                    betweenReachTimeDistribution.enlarge();
                } else if (e.getSource() == durationDistribution) {
                    MyGraphTopLevelDurationDistribution durationDistribution = new MyGraphTopLevelDurationDistribution();
                    durationDistribution.enlarge();
                } else if (e.getSource() == showNodeValue) {
                   MyNodeValue nodeValue = new MyNodeValue();
                } else if (e.getSource() == showEdgeValue) {
                    MyViewerComponentControllerUtil.showEdgeValue();
                } else if (e.getSource() == showEdgeLabel) {
                    MyViewerComponentControllerUtil.showEdgeLabel();
                } else if (e.getSource() == searchNode) {
                    MyNodeLister nodeSearch = new MyNodeLister();
                } else if (e.getSource() == showEndNodes) {
                    MyNodeLister nodeSearch = new MyNodeLister("SHOW ENDING NODES");
                } else if (e.getSource() == showNodeValueDistribution) {
                    MyValueDistributionChartGenerator nodeValueDistributionChart = new MyValueDistributionChartGenerator("NODE VALUE DISTRIBUTION", "NODE VALUE");
                } else if (e.getSource() == showEdgeValueDistribution) {
                    boolean isAllDefaultValues = true;
                    Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
                    for (MyEdge edge : edges) {
                        if ((long) edge.getCurrentValue() != 4) {
                            isAllDefaultValues = false;
                            break;
                        }
                    }
                    if (isAllDefaultValues) {
                        MyMessageUtil.showErrorMsg("Set edge values, first.");
                    } else {
                        MyEdgeValueDistributionChart edgeValueDistributionChart = new MyEdgeValueDistributionChart("EDGE VALUE DISTRIBUTION", "EDGE VALUE");
                    }
                } else if (e.getSource() == dataFlowGraph) {
                    MyFlowExplorerAnalyzer dataFlowGrapher = new MyFlowExplorerAnalyzer();
                    dataFlowGrapher.showDataFlows();
                } else if (e.getSource() == clustering) {
                    MyViewerComponentControllerUtil.setDefaultViewerLook();
                    MyClusteringConfig clusteringConfig = new MyClusteringConfig();
                } else if (e.getSource() == nodeFont) {
                    MyFontChooser fd = new MyFontChooser(new JFrame("NODE FONT CHOOSER"));
                    fd.setVisible(true);
                } else if (e.getSource() == edgeFont) {
                    MyFontChooser fd = new MyFontChooser(new JFrame("EDGE FONT CHOOSER"));
                    fd.setVisible(true);
                }
                MySequentialGraphVars.getSequentialGraphViewer().revalidate();
                MySequentialGraphVars.getSequentialGraphViewer().repaint();
            }
        }).start();

    }
}

