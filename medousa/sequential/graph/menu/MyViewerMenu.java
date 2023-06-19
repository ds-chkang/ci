package medousa.sequential.graph.menu;

import edu.uci.ics.jung.visualization.renderers.DefaultEdgeLabelRenderer;
import edu.uci.ics.jung.visualization.renderers.DefaultVertexLabelRenderer;
import medousa.message.MyMessageUtil;
import medousa.sequential.graph.clustering.MyClusteringConfig;
import medousa.sequential.graph.flow.MyFlowExplorerAnalyzer;
import medousa.sequential.utils.MyFontChooser;
import medousa.sequential.utils.MyViewerComponentControllerUtil;
import medousa.sequential.utils.MySequentialGraphVars;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import medousa.sequential.graph.*;
import medousa.sequential.graph.stats.*;
import org.apache.commons.collections15.Transformer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyViewerMenu
extends JPopupMenu
implements ActionListener {

    private static boolean isTooltipOff;
    private static boolean isNodeColorWeighted;

    private JMenuItem picking = new JMenuItem("PICKING");
    private JMenuItem tranform = new JMenuItem("TRANSORMING");
    private JMenuItem showNodeLabel = new JMenuItem("NODE LABEL");
    private JMenuItem showNodeValue = new JMenuItem("NODE VALUE");
    private JMenuItem showEdgeValue = new JMenuItem("EDGE VALUE");
    private JMenuItem showEdgeLabel = new JMenuItem("EDGE LABEL");
    private JMenuItem currentNodeValueDistribution = new JMenuItem("CUR. NODE VALUE DIST.");
    private JMenuItem currentEdgeValueDistribution = new JMenuItem("CUR. EDGE VALUE DIST.");
    private JMenuItem nodeStatistics = new JMenuItem("NODE STATISTICS");
    private JMenuItem edgeStatistics = new JMenuItem("EDGE STATISTICS");
    private JMenuItem searchNode = new JMenuItem("SEARCH NODE");
    private JMenuItem showEndNodes = new JMenuItem("END NODES");
    private JMenuItem nodesByDepth = new JMenuItem("NODES BY DEPTH");
    private JMenuItem dataFlowGraph = new JMenuItem("DATA FLOWS");
    private JMenuItem clustering = new JMenuItem("CLUSTERING");
    private JMenuItem nodeFont = new JMenuItem("NODE FONT");
    private JMenuItem edgeFont = new JMenuItem("EDGE FONT");
    private JMenuItem nodeToolTipOnOff = new JMenuItem("NODE TOOLTIP-OFF");
    private JMenuItem weightedNodeColor = new JMenuItem("WEIGHT NODE COLORS");
    private JMenuItem background = new JMenuItem("SET BACKGROUND COLOR");
    private JMenuItem nodeLabelColor = new JMenuItem("SET NODE LABEL COLOR");
    private JMenuItem edgeLabelColor = new JMenuItem("SET EDGE LABEL COLOR");
    private JMenuItem edgeColor = new JMenuItem("SET EDGE COLOR");

    public MyViewerMenu( ) {
        this.decorate();
    }

    private void decorate() {
        this.setMenuItem(this.picking, "PICKING");
        this.setMenuItem(this.tranform, "TRANSFORMING");
        this.add(new JSeparator());
        this.setMenuItem(this.searchNode, "SEARCH NODE");
        this.add(new JSeparator());
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

        this.add(new JSeparator());
        if (!isNodeColorWeighted) {
            this.weightedNodeColor.setText("WEIGHT NODE COLORS");
            this.setMenuItem(this.weightedNodeColor, "SHOW UNWEIGHTED NODE COLORS");
        } else {
            this.weightedNodeColor.setText("UNWEIGHT NODE COLORS");
            this.setMenuItem(this.weightedNodeColor, "SHOW UNWEIGHTED NODE COLROS");
        }
        this.add(new JSeparator());
        this.setMenuItem(this.dataFlowGraph, "SHOW DATA FLOW GRAPH");
        this.add(new JSeparator());
        this.setMenuItem(this.nodeFont, "SET NODE FONT");
        this.setMenuItem(this.edgeFont, "SET EDGE FONT");
        this.add(new JSeparator());
        this.setMenuItem(this.background, "SET BACKGROUND COLOR");
        this.setMenuItem(this.nodeLabelColor, "SET NODE LABEL COLOR");
        this.setMenuItem(this.edgeLabelColor, "SET EDGE LABEL COLOR");
        this.setMenuItem(this.edgeColor, "SET EDGE COLOR");
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
        menuItem.setPreferredSize(new Dimension(200, 26));
        this.add(menuItem);
    }

    @Override public void actionPerformed(ActionEvent e) {
        new Thread(new Runnable() {
            @Override public void run() {
                if (e.getSource() == edgeColor) {
                    Color newColor = JColorChooser.showDialog(null, "CHOOSE AN EDGE LABEL COLOR", Color.DARK_GRAY);
                    if (newColor != null) {
                        MySequentialGraphVars.getSequentialGraphViewer().getRenderContext().setEdgeDrawPaintTransformer(new Transformer<MyEdge, Paint>() {
                            @Override public Paint transform(MyEdge myEdge) {
                                return newColor;
                            }
                        });
                    }
                } else if (e.getSource() == edgeLabelColor) {
                    Color newColor = JColorChooser.showDialog(null, "CHOOSE AN EDGE LABEL COLOR", Color.DARK_GRAY);
                    if (newColor != null) {
                        DefaultEdgeLabelRenderer edgeLabelRenderer = new DefaultEdgeLabelRenderer(Color.DARK_GRAY) {
                            @Override
                            public <MyEdge> Component getEdgeLabelRendererComponent(
                                    JComponent vv, Object value, Font font,
                                    boolean isSelected, MyEdge edge) {
                                super.getEdgeLabelRendererComponent(vv, value, font, isSelected, edge);
                                setForeground(newColor);
                                return this;
                            }
                        };
                        MySequentialGraphVars.getSequentialGraphViewer().getRenderContext().setEdgeLabelRenderer(edgeLabelRenderer);
                    }
                } else if (e.getSource() == nodeLabelColor) {
                    Color newColor = JColorChooser.showDialog(null, "CHOOSE A NODE LABEL COLOR", Color.DARK_GRAY);
                    if (newColor != null) {
                        DefaultVertexLabelRenderer vertexLabelRenderer = new DefaultVertexLabelRenderer(Color.DARK_GRAY) {
                            @Override
                            public <MyNode> Component getVertexLabelRendererComponent(
                                    JComponent vv, Object value, Font font,
                                    boolean isSelected, MyNode vertex) {
                                super.getVertexLabelRendererComponent(vv, value, font, isSelected, vertex);
                                setForeground(newColor);
                                return this;
                            }
                        };
                        MySequentialGraphVars.getSequentialGraphViewer().getRenderContext().setVertexLabelRenderer(vertexLabelRenderer);
                    }
                } else if (e.getSource() == background) {
                    Color current = MySequentialGraphVars.getSequentialGraphViewer().getBackground();
                    Color newColor = JColorChooser.showDialog(null, "CHOOSE A VIEWER BACKGOUND COLOR", current);
                    if (newColor != null) {
                        MySequentialGraphVars.getSequentialGraphViewer().setBackground(newColor);
                        MySequentialGraphVars.getSequentialGraphViewer().revalidate();
                        MySequentialGraphVars.getSequentialGraphViewer().repaint();
                    }
                } else if (e.getSource() == nodeToolTipOnOff) {
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
                } else if (e.getSource() == edgeStatistics) {
                    MyEdgeStatistics edgeStatistics = new MyEdgeStatistics();
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
                } else if (e.getSource() == currentNodeValueDistribution) {
                    if (MySequentialGraphVars.getSequentialGraphViewer().nodeValueName.contains("TIME") ||
                        MySequentialGraphVars.getSequentialGraphViewer().nodeValueName.contains("DURATION")) {
                        MyGraphLevelNodeTimeValueDistribution graphLevelTopLevelNodeTimeValueDistribution = new MyGraphLevelNodeTimeValueDistribution();
                        graphLevelTopLevelNodeTimeValueDistribution.enlarge();
                    } else {
                        MyGraphLevelNodeValueDistribution graphLevelTopLevelNodeValueDistribution = new MyGraphLevelNodeValueDistribution();
                        graphLevelTopLevelNodeValueDistribution.enlarge();
                    }
                } else if (e.getSource() == currentEdgeValueDistribution) {
                    if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() > 1) {
                        MyGraphLevelEdgeValueDistribution graphLevelTopLevelEdgeValueDistribution = new MyGraphLevelEdgeValueDistribution();
                        graphLevelTopLevelEdgeValueDistribution.enlarge();
                    } else {
                        MyMessageUtil.showInfoMsg(MySequentialGraphVars.app, "Select an edge value.");
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

