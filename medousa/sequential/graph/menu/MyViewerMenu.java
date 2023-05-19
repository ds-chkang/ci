package medousa.sequential.graph.menu;

import medousa.message.MyMessageUtil;
import medousa.sequential.graph.flow.MyFlowExplorerAnalyzer;
import medousa.sequential.utils.MyFontChooser;
import medousa.sequential.utils.MyViewerControlComponentUtil;
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

    private JMenuItem picking = new JMenuItem("PICKING");
    private JMenuItem tranform = new JMenuItem("TRANSORMING");
    private JMenuItem showNodeLabel = new JMenuItem("NODE LABEL");
    private JMenuItem showNodeValue = new JMenuItem("NODE VALUE");
    private JMenuItem showEdgeValue = new JMenuItem("EDGE VALUE");
    private JMenuItem showEdgeLabel = new JMenuItem("EDGE LABEL");
    private JMenuItem showNodeValueDistribution = new JMenuItem("NODE VALUE DISTRIBUTION");
    private JMenuItem showEdgeValueDistribution = new JMenuItem("EDGE VALUE DISTRIBUTION");
    private JMenuItem nodeStatistics = new JMenuItem("NODE SUMMARY STATISTICS");
    private JMenuItem edgeStatistics = new JMenuItem("EDGE SUMMARY STATISTICS");
    private JMenuItem nodeDepthAppearanceByDepthStatistics = new JMenuItem("NODE APPEARANCE STATISTICS BY DEPTH");
    private JMenuItem inOutDifferenceByDepthStatistics = new JMenuItem("INOUT VALUE DIFFERENCES BY DEPTH");
    private JMenuItem sequenceLengthDistribution = new JMenuItem("SEQUENCE DISTRIBUTION");
    private JMenuItem searchNode = new JMenuItem("SEARCH NODE");
    private JMenuItem showNodesBetweenNodes = new JMenuItem("NODES BETWEEN NODES");
    private JMenuItem showEndNodes = new JMenuItem("END NODES");
    private JMenuItem showBetweenNodeProperty = new JMenuItem("BETWEEN NODE PROPERTIES");
    private JMenuItem hopCountDistribution = new JMenuItem("AVERAGE HOP COUNT DISTRIBUTION");
    private JMenuItem sequenceTotalTimeDistribution = new JMenuItem("SEQUENCE TOTAL TIME DISTRIBUTION");
    private JMenuItem nodesByDepth = new JMenuItem("NODES BY DEPTH");
    private JMenuItem dataFlowGraph = new JMenuItem("DATA FLOWS");
    private JMenuItem clustering = new JMenuItem("CLUSTERING");
    private JMenuItem betweenTimeDistribution = new JMenuItem("BETWEEN TIME DISTRIBUTION");
    private JMenuItem nodeFont = new JMenuItem("NODE FONT");
    private JMenuItem edgeFont = new JMenuItem("EDGE FONT");


    public MyViewerMenu( ) {
        this.decorate();
    }

    private void decorate() {
        this.setMenuItem(this.picking);
        this.setMenuItem(this.tranform);
        this.add(new JSeparator());
        this.setMenuItem(this.searchNode);
        this.add(new JSeparator());
        this.setMenuItem(this.nodeStatistics);
        if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() > 1) {
            this.setMenuItem(this.edgeStatistics);
        }
        this.setMenuItem(this.nodeDepthAppearanceByDepthStatistics);
        this.setMenuItem(this.inOutDifferenceByDepthStatistics);
        this.add(new JSeparator());
        this.setMenuItem(this.sequenceLengthDistribution);
        if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() > 1) {
            this.setMenuItem(this.showEdgeValueDistribution);
        }
       // this.setMenuItem(this.hopCountDistribution);
        this.add(new JSeparator());
        if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeLabelSelecter.getSelectedIndex() > 1) {
            this.setMenuItem(this.showNodeLabel);
        }
        this.setMenuItem(this.showNodeValue);
        if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeLabelSelecter.getSelectedIndex() > 0) {
            this.setMenuItem(this.showEdgeLabel);
        }
        if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() > 1) {
            this.setMenuItem(this.showEdgeValue);
        }
       // this.setMenuItem(this.showEndNodes);
        this.setMenuItem(this.nodesByDepth);
        this.add(new JSeparator());
        this.setMenuItem(this.dataFlowGraph);
        if (MySequentialGraphVars.isTimeOn) {
            this.add(new JSeparator());
            this.setMenuItem(this.sequenceTotalTimeDistribution);
        }
        this.add(new JSeparator());
        this.setMenuItem(this.nodeFont);
        this.setMenuItem(this.edgeFont);
    }

    private void setMenuItem(JMenuItem menuItem) {
        menuItem.setFont(MySequentialGraphVars.tahomaPlainFont12);
        menuItem.addActionListener(this);
        menuItem.setPreferredSize(new Dimension(285, 26));
        this.add(menuItem);
    }

    @Override public void actionPerformed(ActionEvent e) {
        new Thread(new Runnable() {
            @Override public void run() {
                if (e.getSource() == betweenTimeDistribution) {
                    MyBetweenReachTimeDistributionLineChart betweenReachTimeDistributionLineChart = new MyBetweenReachTimeDistributionLineChart();
                    betweenReachTimeDistributionLineChart.show(betweenReachTimeDistributionLineChart);
                } else if (e.getSource() == picking) {
                    DefaultModalGraphMouse graphMouse = new DefaultModalGraphMouse();
                    graphMouse.setMode(DefaultModalGraphMouse.Mode.PICKING);
                    MySequentialGraphVars.getSequentialGraphViewer().setGraphMouse(graphMouse);
                } else if (e.getSource() == tranform) {
                    DefaultModalGraphMouse graphMouse = new DefaultModalGraphMouse();
                    graphMouse.setMode(DefaultModalGraphMouse.Mode.TRANSFORMING);
                    MySequentialGraphVars.getSequentialGraphViewer().setGraphMouse(graphMouse);
                } else if (e.getSource() == nodeStatistics) {
                    MyNodeStatistics nodeStatistics = new MyNodeStatistics();
                } else if (e.getSource() == nodeDepthAppearanceByDepthStatistics) {
                    MyNodeDepthAppearnaceStatistics nodeDepthAppearnaceStatistics = new MyNodeDepthAppearnaceStatistics();
                } else if (e.getSource() == inOutDifferenceByDepthStatistics) {
                    MyInOutValueDifferenceStatByDepthChart inOutDifferenceStatByDepthChart = new MyInOutValueDifferenceStatByDepthChart();
                    inOutDifferenceStatByDepthChart.enlarge();
                } else if (e.getSource() == edgeStatistics) {
                    MyEdgeStatistics edgeStatistics = new MyEdgeStatistics();
                } else if (e.getSource() == hopCountDistribution) {
                    MyGraphLevelNodeAverageHopCountDistributionLineChart hopCountDistribution = new MyGraphLevelNodeAverageHopCountDistributionLineChart();
                    hopCountDistribution.enlarge();
                } else if (e.getSource() == sequenceLengthDistribution) {
                    MyGraphLevelSequenceDistribution sequenceDistribution = new MyGraphLevelSequenceDistribution();
                } else if (e.getSource() == sequenceTotalTimeDistribution) {
                    MyGraphLevelSequenceTotalTimeDistribution sequenceTotalTimeDistribution = new MyGraphLevelSequenceTotalTimeDistribution();
                } else if (e.getSource() == showNodeValue) {
                    MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.removeActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
                    MySequentialGraphVars.getSequentialGraphViewer().vc.nodeLabelSelecter.setSelectedIndex(0);
                    MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.addActionListener(MySequentialGraphVars.getSequentialGraphViewer().vc);
                    MyViewerControlComponentUtil.showNodeValue();
                } else if (e.getSource() == showEdgeValue) {
                    MyViewerControlComponentUtil.showEdgeValue();
                } else if (e.getSource() == showEdgeLabel) {
                    MyViewerControlComponentUtil.showEdgeLabel();
                } else if (e.getSource() == nodesByDepth) {
                    MyNodeDepthAppearnaceStatistics nodeDepthAppearnaceStatistics = new MyNodeDepthAppearnaceStatistics();
                } else if (e.getSource() == searchNode) {
                    MyNodeLister nodeSearch = new MyNodeLister();
                } else if (e.getSource() == showNodesBetweenNodes) {
                    MyBetweenNodesFinder betweenNodeSetFinder = new MyBetweenNodesFinder();
                } else if (e.getSource() == showEndNodes) {
                    MyNodeLister nodeSearch = new MyNodeLister("SHOW ENDING NODES");
                } else if (e.getSource() == showBetweenNodeProperty) {
                    MyBetweenNodePropertyFinder betweenNodePropertyFinder = new MyBetweenNodePropertyFinder();
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
                    MyViewerControlComponentUtil.setDefaultViewerLook();
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

