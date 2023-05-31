package medousa.sequential.graph;

import edu.uci.ics.jung.visualization.renderers.DefaultVertexLabelRenderer;
import medousa.sequential.graph.listener.MyMotionListener;
import medousa.sequential.graph.stats.barchart.*;
import medousa.sequential.graph.listener.MyGraphMouseListener;
import medousa.sequential.graph.listener.MyViewerMouseListener;
import medousa.sequential.utils.MyMathUtil;
import medousa.sequential.utils.MySequentialGraphSysUtil;
import medousa.sequential.utils.MySequentialGraphVars;
import edu.uci.ics.jung.visualization.VisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import org.apache.commons.collections15.Transformer;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.*;

import static java.awt.Cursor.HAND_CURSOR;

public class MySequentialGraphViewer
extends VisualizationViewer<MyNode, MyEdge>
implements Serializable {

    public MyNode startNode;
    public MyNode hoveredNode;
    public String nodeValueName = "CONTRIBUTION";
    public String edgeValName = "NONE";
    public boolean successorsOnly;
    public boolean predecessorsOnly;
    public boolean neighborsOnly;
    public boolean sharedPredecessorsOly;
    public boolean sharedSuccessorsOnly;
    public boolean sharedNeighborsOnly;
    public boolean excluded;
    private ScalingControl viewScaler;
    public final float MX_E_STK = 32.0f;
    public final float MIN_NODE_SZ = 50.0f;
    public Map<String, Integer> endNodesMap;
    public Set<String> nodesBetweenTwoNodes;
    public MyNode singleNode;
    public Set<MyNode> singleNodePredecessors;
    public Set<MyNode> singleNodeSuccessors;
    public Set<MyNode> multiNodes;
    public Set<MyNode> multiNodePredecessors;
    public Set<MyNode> multiNodeSuccessors;
    public MyViewerComponentController vc;
    public Set<MyNode> sharedPredecessors;
    public Set<MyNode> sharedSuccessors;
    public boolean isClustered;
    public Font nodeFont = new Font("Noto Sans", Font.PLAIN, 50);
    public Font edgeFont = new Font("Noto Sans", Font.PLAIN, 50);
    public MyGraphLevelNodeValueBarChart graphLevelNodeValueBarChart;
    public MyGraphLevelNodeLabelBarChart graphLevelNodeLabelBarChart;
    public MyGraphLevelEdgeValueBarChart graphLevelEdgeValueBarChart;
    public MyGraphLevelEdgeLabelBarChart graphLevelEdgeLabelBarChart;
    public MySingleNodeNeighborNodeValueBarChart nodeLevelNeighborNodeValueBarChart;
    public MyMultiLevelNeighborNodeValueBarChart sharedNodeValueBarChart;
    public MyMultiLevelEdgeValueBarChart multiNodeLevelEdgeValueBarChart;
    public MyDepthLevelNodeValueBarChart depthNodeLevelNodeValueBarChart;
    public MyDepthLevelNeighborNodeValueBarChart depthNodeLevelNeighborNodeValueBarChart;
    public MySingleNodeEdgeValueBarChart singleNodeLevelEdgeValueBarChart;
    public MyClusteredGraphLevelEdgeValueBarChart clusteredGraphLevelEdgeValueBarChart;
    public MyClusteredGraphLevelNodeValueBarChart clusteredGraphLevelNodeValueBarChart;
    public MyGraphMouseListener graphMouseListener;
    public MyViewerMouseListener viewerMouseListener;
    public Set<MyNode> selectedTableNodeSet;

    public MySequentialGraphViewer(VisualizationModel<MyNode, MyEdge> vm) {
        super(vm);
        try {
            this.init();
            this.setPreferredSize(new Dimension(5500, 4500));
            DefaultModalGraphMouse graphMouse = new DefaultModalGraphMouse();
            graphMouse.setMode(DefaultModalGraphMouse.Mode.PICKING);
            this.setGraphMouse(graphMouse);

            this.graphMouseListener = new MyGraphMouseListener();
            this.addGraphMouseListener(this.graphMouseListener);
            this.viewerMouseListener = new MyViewerMouseListener(this);
            this.addMouseListener(this.viewerMouseListener);
            this.addMouseMotionListener(new MyMotionListener());

            this.getRenderContext().setLabelOffset(25);
            Cursor handCursor = new Cursor(HAND_CURSOR);
            this.setCursor(handCursor);
            this.setBackground(Color.WHITE);
            this.setDoubleBuffered(true);
            this.setLayout(null);

            this.getRenderContext().setVertexShapeTransformer(this.nodeSizer);
            this.getRenderContext().setVertexFillPaintTransformer(this.unWeightedNodeColor);
            this.getRenderContext().setEdgeDrawPaintTransformer(this.edgeColor);
            this.setVertexToolTipTransformer(this.defaultToolTipper);
            this.getRenderContext().setEdgeStrokeTransformer(this.edgeStroker);
            this.getRenderContext().setVertexStrokeTransformer(this.nodeStroker);
            this.getRenderContext().setVertexDrawPaintTransformer(new Transformer<MyNode, Paint>() {
                @Override public Paint transform(MyNode n) {
                    if (vc.depthSelecter.getSelectedIndex() > 0) {
                        if (vc.nodeListTable.getSelectedRow() >= 0) {
                            String tableNode = vc.nodeListTable.getValueAt(vc.nodeListTable.getSelectedRow(), 1).toString();
                            if (tableNode.equals(MySequentialGraphSysUtil.getDecodedNodeName(n.getName()))) {return Color.ORANGE;
                            } else {return Color.DARK_GRAY;}
                        } else {return Color.DARK_GRAY;}
                    } else if ((singleNode != null && n == singleNode) || (multiNodes != null && multiNodes.contains(n))) {return Color.ORANGE;
                    } else {return Color.DARK_GRAY;}
                }
            });

            final Color vertexLabelColor = Color.BLACK;
            DefaultVertexLabelRenderer vertexLabelRenderer = new DefaultVertexLabelRenderer(vertexLabelColor) {
                @Override public <MyNode> Component getVertexLabelRendererComponent(
                    JComponent vv, Object value, Font font,
                    boolean isSelected, MyNode vertex) {
                    super.getVertexLabelRendererComponent(vv, value, font, isSelected, vertex);
                    setForeground(vertexLabelColor);
                    return this;
                }
            };

            this.getRenderContext().setVertexLabelRenderer(vertexLabelRenderer);
            this.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.S);
            this.getRenderContext().setVertexLabelTransformer(this.nodeLabeller);
            this.getRenderContext().setEdgeLabelTransformer(new Transformer<MyEdge, String>() {
                @Override public String transform(MyEdge edge) {
                    return "";
                }
            });

            this.getRenderContext().setVertexFontTransformer(new Transformer<MyNode, Font>() {
                @Override public Font transform(MyNode n) {
                    if (n.getCurrentValue() == 0) {return new Font("Noto Sans", Font.PLAIN, 0);
                    } else {return nodeFont;}
                }
            });

            this.getRenderContext().setEdgeFontTransformer(new Transformer<MyEdge, Font>() {
                @Override public Font transform(MyEdge e) {
                    if (e.getCurrentValue() == 0) {return new Font("Noto Sans", Font.PLAIN, 0);
                    } else {return edgeFont;}
                }
            });

            this.getRenderContext().setEdgeShapeTransformer(new EdgeShape.QuadCurve<MyNode, MyEdge>());
            this.vc = new MyViewerComponentController();

            Border blackline = BorderFactory.createLineBorder(Color.black);
            TitledBorder titledBorder = BorderFactory.createTitledBorder(blackline, "NETWORK");
            titledBorder.setTitleJustification(TitledBorder.LEFT);
            titledBorder.setTitleFont(MySequentialGraphVars.tahomaBoldFont12);
            titledBorder.setTitleColor(Color.DARK_GRAY);
            //this.vc.setBorder(titledBorder);

            this.addComponentListener(new ComponentAdapter() {
                @Override public void componentResized(ComponentEvent e) {
                    super.componentResized(e);
                    if (vc.nodeValueBarChart.isSelected()) {
                        if (graphLevelNodeValueBarChart != null) {
                            remove(graphLevelNodeValueBarChart);
                        }
                        graphLevelNodeValueBarChart = new MyGraphLevelNodeValueBarChart();
                        add(graphLevelNodeValueBarChart);
                        revalidate();
                        repaint();
                    }
                }
            });
        } catch (Exception ex) {}
    }

    private void init() {
        nodeValueName = "CONTRIBUTION";
        edgeValName = "NONE";
        endNodesMap = new HashMap<>();
        nodesBetweenTwoNodes = new HashSet<>();
        predecessorsOnly = false;
        successorsOnly = false;
        excluded = false;
        vc = null;
        startNode = null;
        multiNodes = null;
        singleNodePredecessors = new HashSet<>();
        singleNodeSuccessors = new HashSet<>();
        sharedSuccessors = new HashSet<>();
        sharedPredecessors = new HashSet<>();
        multiNodePredecessors = new HashSet<>();
        multiNodeSuccessors = new HashSet<>();
        singleNode = null;
        neighborsOnly = false;
    }

    private Transformer<MyNode, String> nodeLabeller = new Transformer<MyNode, String>() {
        @Override public String transform(MyNode n) {
            String name = MySequentialGraphSysUtil.getNodeName(n.getName());

            if (multiNodes != null) {
                if (predecessorsOnly) {
                    if (multiNodes.contains(n) || multiNodePredecessors.contains(n)) {
                        return name;
                    } else {
                        return "";
                    }
                } else if (successorsOnly) {
                    if (multiNodes.contains(n) || multiNodeSuccessors.contains(n)) {
                        return name;
                    } else {
                        return "";
                    }
                } else if (neighborsOnly) {
                    if (multiNodeSuccessors.contains(n) || multiNodePredecessors.contains(n)) {
                        return name;
                    } else {
                        return "";
                    }
                } else if (sharedPredecessorsOly) {
                    if (multiNodes.contains(n) || sharedPredecessors.contains(n)) {
                        return name;
                    } else {
                        return "";
                    }
                } else if (sharedSuccessorsOnly) {
                    if (multiNodes.contains(n) || sharedSuccessors.contains(n)) {
                        return name;
                    } else {
                        return "";
                    }
                } else if (sharedNeighborsOnly) {
                    if (multiNodes.contains(n) || sharedSuccessors.contains(n) || sharedPredecessors.contains(n)) {
                        return name;
                    } else {
                        return "";
                    }
                } else if (multiNodes.contains(n) || multiNodeSuccessors.contains(n) || multiNodePredecessors.contains(n)) {
                    return name;
                } else {
                    return "";
                }
            } else if (singleNode != null) {
                if (predecessorsOnly) {
                    if (n == singleNode || singleNodePredecessors.contains(n)) {
                        return name;
                    } else {
                        return "";
                    }
                } else if (successorsOnly) {
                    if (n == singleNode || singleNodeSuccessors.contains(n)) {
                        return name;
                    } else {
                        return "";
                    }
                } else if (neighborsOnly) {
                    if (n == singleNode || singleNodeSuccessors.contains(n) || singleNodePredecessors.contains(n)) {
                        return name;
                    } else {
                        return "";
                    }
                } else if (n == singleNode || singleNodeSuccessors.contains(n) || singleNodePredecessors.contains(n)) {
                    return name;
                } else {
                    return "";
                }
            } else {
                return name;
            }
    }};

    private Transformer<MyNode, Stroke> nodeStroker = new Transformer<MyNode, Stroke>() {
        @Override public Stroke transform(MyNode n) {
            if (vc.tableTabbedPane.getSelectedIndex() == 2) {
                return new BasicStroke(4f);
            } else if ((singleNode != null && n == singleNode) || (multiNodes != null && multiNodes.contains(n))) {
                return new BasicStroke(30f);
            } else {
                float currentNodeValueWeight = n.getCurrentValue() / MySequentialGraphVars.g.MX_N_VAL;
                return setNodeDrawStroke(currentNodeValueWeight);
            }
        }
    };

    public Transformer<MyEdge, Stroke> edgeStroker = new Transformer<MyEdge, Stroke>() {
        @Override public Stroke transform(MyEdge e) {
            float edgeStrokeWeight = e.getCurrentValue() / (MySequentialGraphVars.g.MX_E_VAL+5);
            if (hoveredNode != null) {
                if (e.getDest() == hoveredNode || e.getSource() == hoveredNode) {
                    float edgeStroke = edgeStrokeWeight * MX_E_STK;
                    return new BasicStroke(edgeStroke+8);
                } else {
                    float edgeStroke = edgeStrokeWeight * (MX_E_STK);
                    return new BasicStroke(edgeStroke);
                }
            } if (singleNode != null) {
                if (predecessorsOnly) {
                    if (singleNodePredecessors.contains(e.getSource())) {
                        float edgeStroke = edgeStrokeWeight * MX_E_STK;
                        if (edgeStroke < 1) {
                            edgeStroke = 0.2f;
                        }
                        return new BasicStroke(edgeStroke);
                    } else {
                        return new BasicStroke(0f);
                    }
                } else if (successorsOnly) {
                    if (singleNodeSuccessors.contains(e.getDest())) {
                        float edgeStroke = edgeStrokeWeight * MX_E_STK;
                        if (edgeStroke < 1) {
                            edgeStroke = 0.2f;
                            edgeStroke = 0.2f;
                        }
                        return new BasicStroke(edgeStroke);
                    } else {
                        return new BasicStroke(0f);
                    }
                } else if (neighborsOnly) {
                    if (singleNodeSuccessors.contains(e.getDest()) || singleNodePredecessors.contains(e.getSource())) {
                        float edgeStroke = edgeStrokeWeight * MX_E_STK;
                        if (edgeStroke < 1) {
                            edgeStroke = 0.2f;
                        }
                        return new BasicStroke(edgeStroke);
                    } else {
                        return new BasicStroke(0f);
                    }
                } else if (e.getSource() != singleNode && e.getDest() != singleNode) {
                    return new BasicStroke(0f);
                } else {
                    float edgeStroke = edgeStrokeWeight * MX_E_STK;
                    if (edgeStroke < 1) {
                        edgeStroke = 0.2f;
                    }
                    return new BasicStroke(edgeStroke);
                }
            } else if (multiNodes != null) {
                if (predecessorsOnly) {
                    if (multiNodes.contains(e.getDest()) && multiNodePredecessors.contains(e.getSource())) {
                        float edgeStroke = edgeStrokeWeight * MX_E_STK;
                        if (edgeStroke < 1) {
                            edgeStroke = 0.2f;
                        }
                        return new BasicStroke(edgeStroke);
                    } else {
                        return new BasicStroke(0f);
                    }
                } else if (successorsOnly) {
                    if (multiNodes.contains(e.getSource()) && multiNodeSuccessors.contains(e.getDest())) {
                        float edgeStroke = edgeStrokeWeight * MX_E_STK;
                        if (edgeStroke < 1) {
                            edgeStroke = 0.2f;
                        }
                        return new BasicStroke(edgeStroke);
                    } else {
                        return new BasicStroke(0f);
                    }
                } else if (neighborsOnly) {
                    if (multiNodeSuccessors.contains(e.getDest()) || multiNodePredecessors.contains(e.getSource())) {
                        float edgeStroke = edgeStrokeWeight * MX_E_STK;
                        if (edgeStroke < 1) {
                            edgeStroke = 0.2f;
                        }
                        return new BasicStroke(edgeStroke);
                    } else {
                        return new BasicStroke(0f);
                    }
                } else if (sharedPredecessorsOly) {
                    if (multiNodes.contains(e.getDest()) && multiNodePredecessors.contains(e.getSource())) {
                        float edgeStroke = edgeStrokeWeight * MX_E_STK;
                        if (edgeStroke < 1) {
                            edgeStroke = 0.2f;
                        }
                        return new BasicStroke(edgeStroke);
                    } else {
                        return new BasicStroke(0f);
                    }
                } else if (sharedSuccessorsOnly) {
                    if (multiNodes.contains(e.getSource()) && sharedSuccessors.contains(e.getDest())) {
                        float edgeStroke = edgeStrokeWeight * MX_E_STK;
                        if (edgeStroke < 1) {
                            edgeStroke = 0.2f;
                        }
                        return new BasicStroke(edgeStroke);
                    } else {
                        return new BasicStroke(0f);
                    }
                } else if (sharedNeighborsOnly) {
                    if (sharedSuccessors.contains(e.getDest()) || sharedPredecessors.contains(e.getSource())) {
                        float edgeStroke = edgeStrokeWeight * MX_E_STK;
                        if (edgeStroke < 1) {
                            edgeStroke = 0.2f;
                        }
                        return new BasicStroke(edgeStroke);
                    } else {
                        return new BasicStroke(0f);
                    }
                } else if (!multiNodes.contains(e.getSource()) && !multiNodes.contains(e.getDest())) {
                    return new BasicStroke(0f);
                } else {
                    float edgeStroke = edgeStrokeWeight * MX_E_STK;
                    if (edgeStroke < 1) {
                        edgeStroke = 0.2f;
                    }
                    return new BasicStroke(edgeStroke);
                }
            } else {
                if (vc.selectedNodeNeighborNodeTypeSelector.isShowing()) {
                    float edgeStroke = edgeStrokeWeight * MX_E_STK;
                    if (edgeStroke < 1) {
                        edgeStroke = 0.2f;
                    }
                    return new BasicStroke(edgeStroke);
                } else {
                    float edgeStroke = edgeStrokeWeight * MX_E_STK;
                    if (edgeStroke < 1) {
                        edgeStroke = 0.2f;
                    }
                    return new BasicStroke(edgeStroke);
                }
            }
        }
    };

    public Transformer<MyNode, Paint> unWeightedNodeColor = new Transformer<MyNode, Paint>() {
        @Override public Paint transform(MyNode n) {
            if (vc.shortestDistanceSourceNode != null && vc.shortestDistanceSourceNode == n) {
                return Color.ORANGE;
            } else if (isClustered) {
                return n.clusteringColor;
            } else if (hoveredNode != null) {
                if (n == hoveredNode) {
                    return Color.YELLOW;
                } else if (MySequentialGraphVars.g.getSuccessorCount(n) == 0 && MySequentialGraphVars.g.getPredecessorCount(n) > 0) {
                    return new Color(0.0f, 1.0f, 0.0f, 0.7f);
                } else if (MySequentialGraphVars.g.getSuccessorCount(n) > 0 && MySequentialGraphVars.g.getPredecessorCount(n) == 0) {
                    return new Color(1.0f, 0.0f, 0.0f, 0.7f);
                } else {
                    return new Color(0.0f, 0.0f, 1.0f, 0.7f);
                }
            } else if (multiNodes != null) {
                if (MySequentialGraphVars.g.getSuccessors(n).containsAll(multiNodes)) {
                    return Color.ORANGE;
                } else if (MySequentialGraphVars.g.getPredecessors(n).containsAll(multiNodes)) {
                    return Color.ORANGE;
                } else if (multiNodes.contains(n) || multiNodePredecessors.contains(n) || multiNodeSuccessors.contains(n)) {
                    if (MySequentialGraphVars.g.getSuccessorCount(n) == 0 && MySequentialGraphVars.g.getPredecessorCount(n) > 0) {
                        return new Color(0.0f, 1.0f, 0.0f, 0.7f);
                    } else if (MySequentialGraphVars.g.getSuccessorCount(n) > 0 && MySequentialGraphVars.g.getPredecessorCount(n) == 0) {
                        return new Color(1.0f, 0.0f, 0.0f, 0.7f);
                    } else {
                        return new Color(0.0f, 0.0f, 1.0f, 0.7f);
                    }
                } else {
                    return new Color(0.0f, 0.0f, 0.0f, 0f);
                }
            } else if (MySequentialGraphVars.g.getSuccessorCount(n) == 0 && MySequentialGraphVars.g.getPredecessorCount(n) > 0) {
                return new Color(0.0f, 1.0f, 0.0f, 0.7f);
            } else if (MySequentialGraphVars.g.getSuccessorCount(n) > 0 && MySequentialGraphVars.g.getPredecessorCount(n) == 0) {
                return new Color(1.0f, 0.0f, 0.0f, 0.7f);
            } else {
                return new Color(0.0f, 0.0f, 1.0f, 0.7f);
            }
        }
    };


    public Transformer<MyNode, Paint> weightedNodeColor = new Transformer<MyNode, Paint>() {
        @Override public Paint transform(MyNode n) {
            if (hoveredNode != null && hoveredNode == n) {
                if (n == hoveredNode) {
                    return Color.YELLOW;
                } else if (MySequentialGraphVars.g.getSuccessorCount(n) == 0 && MySequentialGraphVars.g.getPredecessorCount(n) > 0) {
                    return new Color(0.0f, 1.0f, 0.0f, 0.7f);
                } else if (MySequentialGraphVars.g.getSuccessorCount(n) > 0 && MySequentialGraphVars.g.getPredecessorCount(n) == 0) {
                    return new Color(1.0f, 0.0f, 0.0f, 0.7f);
                } else {
                    return new Color(0.0f, 0.0f, 1.0f, 0.7f);
                }
            } else if (vc.depthNeighborNodeTypeSelector.getSelectedIndex() > 0) {
                if (vc.depthNodeNameSet != null && vc.depthNodeNameSet.contains(n.getName())) {
                    return new Color(1.0f, 0.0f, 0.0f, 0.7f);
                } else if (vc.depthNodePredecessorMaps != null && vc.depthNodePredecessorMaps.containsKey(n.getName())) {
                    return new Color(0.0f, 0.0f, 0.0f, 0.5f);
                } else if (vc.depthNodeSuccessorMaps != null && vc.depthNodeSuccessorMaps.containsKey(n.getName())) {
                    return new Color(0.0f, 0.0f, 0.0f, 0.5f);
                } else {
                    return new Color(0.0f, 0.0f, 1.0f, 0.7f);
                }
            } else return setWeightedNodeColor(n);
        }
    };

    public Transformer<MyNode, Shape> nodeSizer = new Transformer<MyNode, Shape>() {
        @Override public Shape transform(MyNode n) {
             if (multiNodes != null) {
                if (predecessorsOnly) {
                    if (multiNodes.contains(n)) {
                        return setNodeSize(n.getCurrentValue()/ MySequentialGraphVars.g.MX_N_VAL);
                    } else if (multiNodePredecessors.contains(n)) {
                        return setNodeSize(n.getCurrentValue()/ MySequentialGraphVars.g.MX_N_VAL);
                    } else {
                        return new Ellipse2D.Double(0, 0, 0, 0);
                    }
                } else if (successorsOnly) {
                    if (multiNodes.contains(n)) {
                        return setNodeSize(n.getCurrentValue()/ MySequentialGraphVars.g.MX_N_VAL);
                    } else if (multiNodeSuccessors.contains(n)) {
                        return setNodeSize(n.getCurrentValue()/ MySequentialGraphVars.g.MX_N_VAL);
                    } else {
                        return new Ellipse2D.Double(0, 0, 0, 0);
                    }
                } else if (neighborsOnly) {
                    if (multiNodes.contains(n)) {
                        return setNodeSize(n.getCurrentValue()/ MySequentialGraphVars.g.MX_N_VAL);
                    } else if (multiNodeSuccessors.contains(n) || multiNodePredecessors.contains(n)) {
                        return setNodeSize(n.getCurrentValue()/ MySequentialGraphVars.g.MX_N_VAL);
                    } else {
                        return new Ellipse2D.Double(0, 0, 0, 0);
                    }
                } else if (sharedPredecessorsOly) {
                    if (multiNodes.contains(n)) {
                        return setNodeSize(n.getCurrentValue()/ MySequentialGraphVars.g.MX_N_VAL);
                    } else if (sharedPredecessors.contains(n)) {
                        return setNodeSize(n.getCurrentValue()/ MySequentialGraphVars.g.MX_N_VAL);
                    } else {
                        return new Ellipse2D.Double(0, 0, 0, 0);
                    }
                } else if (sharedSuccessorsOnly) {
                    if (multiNodes.contains(n)) {
                        return setNodeSize(n.getCurrentValue()/ MySequentialGraphVars.g.MX_N_VAL);
                    } else if (sharedSuccessors.contains(n)) {
                        return setNodeSize(n.getCurrentValue()/ MySequentialGraphVars.g.MX_N_VAL);
                    } else {
                        return new Ellipse2D.Double(0, 0, 0, 0);
                    }
                } else if (sharedNeighborsOnly) {
                    if (multiNodes.contains(n) || sharedSuccessors.contains(n) || sharedPredecessors.contains(n)) {
                        return setNodeSize(n.getCurrentValue()/ MySequentialGraphVars.g.MX_N_VAL);
                    } else {
                        return new Ellipse2D.Double(0, 0, 0, 0);
                    }
                } else if (multiNodes.contains(n) || multiNodeSuccessors.contains(n) || multiNodePredecessors.contains(n)) {
                    return setNodeSize(n.getCurrentValue()/ MySequentialGraphVars.g.MX_N_VAL);
                } else {
                    return new Ellipse2D.Double(0, 0, 0, 0);
                }
            } else if (singleNode != null) {
                if (predecessorsOnly) {
                    if (n == singleNode) {
                        return setNodeSize(n.getCurrentValue() / MySequentialGraphVars.g.MX_N_VAL);
                    } else if (singleNodePredecessors.contains(n)) {
                        return setNodeSize(n.getCurrentValue() / MySequentialGraphVars.g.MX_N_VAL);
                    } else {
                        return new Ellipse2D.Double(0, 0, 0, 0);
                    }
                } else if (successorsOnly) {
                    if (n == singleNode) {
                        return setNodeSize(n.getCurrentValue() / MySequentialGraphVars.g.MX_N_VAL);
                    } else if (singleNodeSuccessors.contains(n)) {
                        return setNodeSize(n.getCurrentValue() / MySequentialGraphVars.g.MX_N_VAL);
                    } else {
                        return new Ellipse2D.Double(0, 0, 0, 0);
                    }
                } else if (neighborsOnly) {
                    if (n == singleNode) {
                        return setNodeSize(n.getCurrentValue() / MySequentialGraphVars.g.MX_N_VAL);
                    } else if (singleNodeSuccessors.contains(n)) {
                        return setNodeSize(n.getCurrentValue() / MySequentialGraphVars.g.MX_N_VAL);
                    } else if (singleNodePredecessors.contains(n)) {
                        return setNodeSize(n.getCurrentValue() / MySequentialGraphVars.g.MX_N_VAL);
                    } else {
                        return new Ellipse2D.Double(0, 0, 0, 0);
                    }
                } else if (n.getCurrentValue() == 0) {
                    return new Ellipse2D.Double(0, 0, 0, 0);
                } else if (n != singleNode && !singleNodePredecessors.contains(n) && !singleNodeSuccessors.contains(n)) {
                    return new Ellipse2D.Double(0, 0, 0, 0);
                } else {
                    return setNodeSize(n.getCurrentValue() / MySequentialGraphVars.g.MX_N_VAL);
                }
            } else if (n.getCurrentValue() == 0) {
                return new Ellipse2D.Double(0, 0, 0, 0);
            } else {
                return setNodeSize(n.getCurrentValue() / MySequentialGraphVars.g.MX_N_VAL);
            }
        }
    };

    public Transformer<MyEdge, Paint> edgeColor = new Transformer<MyEdge, Paint>() {
        @Override public Paint transform(MyEdge e) {
            return setEdgeColor(e);
        }
    };

    public Transformer<MyNode, String> defaultToolTipper = new Transformer<MyNode, String>() {
        @Override public String transform(MyNode n) {
            if (MySequentialGraphVars.currentGraphDepth == 0) {return setTopNodeToolTip(n);}
            else if (vc.selectedNodeNeighborNodeTypeSelector.getSelectedIndex() > 0) {
                if (n == singleNode) {
                    return setDepthNodeToolTipForSingleSelectedNode(n, MySequentialGraphVars.currentGraphDepth);
                } else if (vc.selectedNodeNeighborNodeTypeSelector.getSelectedItem().toString().contains("P")) {
                    return setDepthNodeToolTipForSingleSelectedNode(n, MySequentialGraphVars.currentGraphDepth - 1);
                } else if (vc.selectedNodeNeighborNodeTypeSelector.getSelectedItem().toString().contains("S")) {
                    return setDepthNodeToolTipForSingleSelectedNode(n, MySequentialGraphVars.currentGraphDepth + 1);
                } else if (vc.selectedNodeNeighborNodeTypeSelector.getSelectedItem().toString().contains("B")) {
                    if (n.getNodeDepthInfo(MySequentialGraphVars.currentGraphDepth -1) != null && n.getNodeDepthInfo(MySequentialGraphVars.currentGraphDepth +1) != null) {
                        return "";  // Show both information when a node is a predecessor and a successor.
                    } else if (n.getNodeDepthInfo(MySequentialGraphVars.currentGraphDepth -1) != null) {
                        return ""; // Show predecessor node information.
                    } else if (n.getNodeDepthInfo(MySequentialGraphVars.currentGraphDepth +1) != null) {
                        return ""; // Show successor node information.
                    } else {
                        return "";
                    }
                } else {
                    return "";
                }
            } else if (n.getNodeDepthInfo(MySequentialGraphVars.currentGraphDepth) != null) {
                int depth = MySequentialGraphVars.currentGraphDepth;
                return setDepthNodeToolTip(n, depth);
            } else if (
                    MySequentialGraphVars.getSequentialGraphViewer().vc.depthNeighborNodeTypeSelector.getSelectedItem().toString().contains("P") &&
                    n.getNodeDepthInfo(MySequentialGraphVars.currentGraphDepth -1) != null) {
                int depth = MySequentialGraphVars.currentGraphDepth -1;
                return setDepthNodeToolTip(n, depth);
            } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthNeighborNodeTypeSelector.getSelectedItem().toString().contains("S") &&
                    n.getNodeDepthInfo(MySequentialGraphVars.currentGraphDepth +1) != null) {
                int depth = MySequentialGraphVars.currentGraphDepth +1;
                return setDepthNodeToolTip(n, depth);
            } else {return "";} // <== 이부분 확인.
        }
    };

    public MySequentialGraphViewer create() {
        this.scale();
        return this;
    }

    private void scale() {
        this.viewScaler = new CrossoverScalingControl();
        double amount = -1.0;
        viewScaler.scale(this, amount > 0 ? 2.0f : 1 / 11.5f, new Point2D.Double(360, 150));
    }

    @Override public void paintComponent(Graphics g) {
        try {super.paintComponent(g);
        } catch (Exception ex) {}
    }

    private Ellipse2D.Double setNodeSize(float nodeWeight) {
        if (nodeWeight <= 0.05) {
            float nodeSize = MIN_NODE_SZ/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        } else if (nodeWeight > 0.05 && nodeWeight <= 0.1) {
            float nodeSize = (MIN_NODE_SZ+25)/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        } else if (nodeWeight > 0.1 && nodeWeight <= 0.15) {
            float nodeSize = (MIN_NODE_SZ+35)/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        } else if (nodeWeight > 0.15 && nodeWeight <= 0.2) {
            float nodeSize = (MIN_NODE_SZ+45)/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        } else if (nodeWeight > 0.2 && nodeWeight <= 0.25) {
            float nodeSize = (MIN_NODE_SZ+55)/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        } else if (nodeWeight > 0.25 && nodeWeight <= 0.3) {
            float nodeSize = (MIN_NODE_SZ+65)/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        } else if (nodeWeight > 0.3 && nodeWeight <= 0.35) {
            float nodeSize = (MIN_NODE_SZ+75)/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        } else if (nodeWeight > 0.35 && nodeWeight <= 0.4) {
            float nodeSize = (MIN_NODE_SZ+85)/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        } else if (nodeWeight > 0.4 && nodeWeight <= 0.45) {
            float nodeSize = (MIN_NODE_SZ+95)/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        } else if (nodeWeight > 0.45 && nodeWeight <= 0.5) {
            float nodeSize = (MIN_NODE_SZ+113)/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        } else if (nodeWeight > 0.5 && nodeWeight <= 0.55) {
            float nodeSize = (MIN_NODE_SZ+120)/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        } else if (nodeWeight > 0.55 && nodeWeight <= 0.6) {
            float nodeSize = (MIN_NODE_SZ+130)/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        } else if (nodeWeight > 0.6 && nodeWeight <= 0.65) {
            float nodeSize = (MIN_NODE_SZ+135)/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        } else if (nodeWeight > 0.65 && nodeWeight <= 0.7) {
            float nodeSize = (MIN_NODE_SZ+145)/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        } else if (nodeWeight > 0.7 && nodeWeight <= 0.75) {
            float nodeSize = (MIN_NODE_SZ+155)/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        } else if (nodeWeight > 0.75 && nodeWeight <= 0.8) {
            float nodeSize = (MIN_NODE_SZ+165)/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        } else if (nodeWeight > 0.8 && nodeWeight <= 0.85) {
            float nodeSize = (MIN_NODE_SZ+175)/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        } else if (nodeWeight > 0.85 && nodeWeight <= 0.9) {
            float nodeSize = (MIN_NODE_SZ+183)/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        } else if (nodeWeight > 0.9 && nodeWeight <= 0.95) {
            float nodeSize = (MIN_NODE_SZ+195)/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        } else {
            float nodeSize = (MIN_NODE_SZ+210)/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        }
    }

    private Stroke setNodeDrawStroke(float nodeValueWeight) {
        if (nodeValueWeight <= 0.1) {
            return new BasicStroke(2f);
        } else if (nodeValueWeight > 0.1 && nodeValueWeight <= 0.2) {
            return new BasicStroke(4f);
        } else if (nodeValueWeight > 0.2 && nodeValueWeight <= 0.3) {
            return new BasicStroke(6f);
        } else if (nodeValueWeight > 0.3 && nodeValueWeight <= 0.4) {
            return new BasicStroke(8f);
        } else if (nodeValueWeight > 0.4 && nodeValueWeight <= 0.5) {
            return new BasicStroke(12f);
        } else if (nodeValueWeight > 0.5 && nodeValueWeight <= 0.6) {
            return new BasicStroke(15f);
        } else if (nodeValueWeight > 0.6 && nodeValueWeight <= 0.7) {
            return new BasicStroke(18f);
        } else if (nodeValueWeight > 0.7 && nodeValueWeight <= 0.8) {
            return new BasicStroke(21f);
        } else if (nodeValueWeight > 0.8 && nodeValueWeight <= 0.9) {
            return new BasicStroke(24f);
        } else {
            return new BasicStroke(28f);
        }
    }

    public Paint setEdgeColor(MyEdge e) {
        if (vc.depthNeighborNodeTypeSelector.getSelectedIndex() > 0) {
            if (singleNode != null && e.getSource() != singleNode && e.getDest() != singleNode) {
                return new Color(0f, 0f, 0.0f, 0.0f);
            } else if (vc.depthNeighborNodeTypeSelector.getSelectedItem().toString().equals("S.")) {
                if (vc.depthNodeSuccessorMaps != null && vc.depthNodeSuccessorMaps.containsKey(e.getSource().getName()) &&
                        vc.depthNodeSuccessorMaps.get(e.getSource().getName()).containsKey(e.getDest().getName())) {
                    return new Color(1,1,1, 0.56f);
                } else {
                    return new Color(0f, 0f, 0.0f, 0.0f);
                }
            } else if (vc.depthNeighborNodeTypeSelector.getSelectedItem().toString().equals("P.")) {
                if (vc.depthNodePredecessorMaps != null && vc.depthNodePredecessorMaps.containsKey(e.getDest().getName()) &&
                        vc.depthNodePredecessorMaps.get(e.getDest().getName()).containsKey(e.getSource().getName())) {
                    return new Color(1,1,1, 0.56f);
                } else {
                    return new Color(0f, 0f, 0.0f, 0.0f);
                }
            } else {
                return new Color(0f, 0f, 0.0f, 0.0f);
            }
        } else if (hoveredNode != null) {
            if (e.getSource() == hoveredNode) {
                return new Color(0, 0, 1, 0.45f);
            } else if (e.getDest() == hoveredNode) {
                return new Color(1, 0, 0, 0.6f);
            } else {
                return Color.decode("#d8d8d8");
            }
        } else if (multiNodes != null) {
            if (predecessorsOnly) {
                if (multiNodePredecessors.contains(e.getSource()) && multiNodes.contains(e.getDest())) {
                    return new Color(1,0,0, 0.25f);
                } else {
                    return new Color(0f, 0f, 0f, 0f);
                }
            } else if (successorsOnly) {
                if (multiNodeSuccessors.contains(e.getDest()) && multiNodes.contains(e.getSource())) {
                    return new Color(0f, 0f, 1f, 0.25f);
                } else {
                    return new Color(0f, 0f, 0f, 0f);
                }
            } else if (neighborsOnly) {
                if (multiNodePredecessors.contains(e.getSource()) && multiNodes.contains(e.getDest())) {
                    return new Color(1f, 0f, 0f, 0.25f);
                } else if (multiNodeSuccessors.contains(e.getDest()) && multiNodes.contains(e.getSource())) {
                    return new Color(0f, 0f, 1f, 0.25f);
                } else {
                    return new Color(0f, 0f, 0f, 0f);
                }
            } else if (sharedPredecessorsOly) {
                if (sharedPredecessors.contains(e.getSource()) && multiNodes.contains(e.getDest())) {
                    return new Color(1f, 0f, 0f, 0.25f);
                } else {
                    return new Color(0f, 0f, 0f, 0f);
                }
            } else if (sharedSuccessorsOnly) {
                if (sharedSuccessors.contains(e.getDest()) && multiNodes.contains(e.getSource())) {
                    return new Color(0f, 0f, 1f, 0.25f);
                } else {
                    return new Color(0f, 0f, 0f, 0f);
                }
            } else if (sharedNeighborsOnly) {
                if (sharedPredecessors.contains(e.getSource()) && multiNodes.contains(e.getDest())) {
                    return new Color(1f, 0f, 0f, 0.25f);
                } else if (sharedSuccessors.contains(e.getDest()) && multiNodes.contains(e.getSource())) {
                    return new Color(0f, 0f, 1f, 0.25f);
                } else {
                    return new Color(0f, 0f, 0f, 0f);
                }
            } else if (!multiNodes.contains(e.getSource()) && !multiNodes.contains(e.getDest())) {
                return new Color(0f, 0f, 0f, 0f);
            } else if (e.getDest() == e.getSource() && multiNodes.contains(e.getDest()) && multiNodes.contains(e.getSource())) {
                return Color.YELLOW;
            } else if (multiNodePredecessors.contains(e.getSource()) && multiNodes.contains(e.getDest())) {
                return new Color(1f, 0f, 0f, 0.25f);
            } else if (multiNodeSuccessors.contains(e.getDest()) && multiNodes.contains(e.getSource())) {
                return new Color(0f, 0f, 1f, 0.25f);
            } else if (e.getDest() != e.getSource() && multiNodes.contains(e.getDest()) && multiNodes.contains(e.getSource())) {
                return new Color(0f, 1f, 0f, 0.25f);
            } else {
                return new Color(0f, 0f, 0f, 0f);
            }
        } else if (singleNode != null) {
            if (predecessorsOnly) {
                if (e.getSource() == singleNode && e.getDest() == singleNode) {
                    return Color.ORANGE;
                } else if (e.getDest() == singleNode && singleNodePredecessors.contains(e.getSource())) {
                    return new Color(1.0f, 0, 0, 0.25f);
                } else {
                    return new Color(0, 0, 0, 0f);
                }
            } else if (successorsOnly) {
                if (e.getSource() == singleNode && e.getDest() == singleNode) {
                    return Color.BLACK;
                } else if (e.getSource() == singleNode && singleNodeSuccessors.contains(e.getDest())) {
                    return new Color(0, 0, 1.0f, 0.25f);
                } else {
                    return new Color(0, 0, 0, 0f);
                }
            } else if (neighborsOnly) {
                if (e.getSource() == singleNode && e.getDest() == singleNode) {
                    return Color.BLACK;
                } else if (e.getSource() == singleNode && singleNodeSuccessors.contains(e.getDest())) {
                    return new Color(0, 0, 1.0f, 0.25f);
                } else if (e.getDest() == singleNode && singleNodePredecessors.contains(e.getSource())) {
                    return new Color(1.0f, 0, 0, 0.25f);
                } else {
                    return new Color(0, 0, 0, 0f);
                }
            } else if (singleNode != e.getDest() && singleNode != e.getSource()) {
                return new Color(0.0f, 0.0f, 0.0f, 0.0f);
            } else if (e.getCurrentValue() == 0 || e.getSource().getCurrentValue() == 0 || e.getDest().getCurrentValue() == 0) {
                return new Color(0.0f, 0.0f, 0.0f, 0.0f);
            } else if (e.getSource() == singleNode && e.getDest() == singleNode) {
                return Color.ORANGE;
            } else if (e.getSource() == singleNode) {
                return new Color(0f, 0f, 1f, 1f);
            } else if (e.getDest() == singleNode) {
                return new Color(1f, 0f, 0f, 1f);
            } else {
                return new Color(0, 0, 0, 0f);
            }
        } else if (e.getCurrentValue() == 0) {
            return new Color(0.0f, 0.0f, 0.0f, 0.0f);
        } else if (e.getSource().getCurrentValue() == 0 || e.getDest().getCurrentValue() == 0) {
            return new Color(0.0f, 0.0f, 0.0f, 0.0f);
        } else if (e.getSource() == e.getDest()) {
            return Color.YELLOW;
        } else {
            return Color.LIGHT_GRAY;
        }
    }

    private Paint setWeightedNodeColor(MyNode n) {
        float nodeColorWeight = n.getCurrentValue()/ MySequentialGraphVars.g.MX_N_VAL;
        if (nodeColorWeight <= 0.05) {
            if (MySequentialGraphVars.g.getSuccessorCount(n) > 0 && MySequentialGraphVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 0.05f);
            } else if (MySequentialGraphVars.g.getSuccessorCount(n) == 0 && MySequentialGraphVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 0.05f);
            } else if (MySequentialGraphVars.g.getSuccessorCount(n) > 0 && MySequentialGraphVars.g.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 0.05f);
            } else {
                return null;
            }
        } else if (nodeColorWeight > 0.05 && nodeColorWeight <= 0.1) {
            if (MySequentialGraphVars.g.getSuccessorCount(n) > 0 && MySequentialGraphVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 0.1f);
            } else if (MySequentialGraphVars.g.getSuccessorCount(n) == 0 && MySequentialGraphVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 0.1f);
            } else if (MySequentialGraphVars.g.getSuccessorCount(n) > 0 && MySequentialGraphVars.g.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 0.1f);
            } else {
                return null;
            }
        } else if (nodeColorWeight > 0.1 && nodeColorWeight <= 0.15) {
            if (MySequentialGraphVars.g.getSuccessorCount(n) > 0 && MySequentialGraphVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 0.15f);
            } else if (MySequentialGraphVars.g.getSuccessorCount(n) == 0 && MySequentialGraphVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 0.15f);
            } else if (MySequentialGraphVars.g.getSuccessorCount(n) > 0 && MySequentialGraphVars.g.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 0.15f);
            } else {
                return null;
            }
        } else if (nodeColorWeight > 0.15 && nodeColorWeight <= 0.2) {
            if (MySequentialGraphVars.g.getSuccessorCount(n) > 0 && MySequentialGraphVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 0.2f);
            } else if (MySequentialGraphVars.g.getSuccessorCount(n) == 0 && MySequentialGraphVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 0.2f);
            } else if (MySequentialGraphVars.g.getSuccessorCount(n) > 0 && MySequentialGraphVars.g.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 0.2f);
            } else {
                return null;
            }
        } else if (nodeColorWeight > 0.2 && nodeColorWeight <= 0.25) {
            if (MySequentialGraphVars.g.getSuccessorCount(n) > 0 && MySequentialGraphVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 0.25f);
            } else if (MySequentialGraphVars.g.getSuccessorCount(n) == 0 && MySequentialGraphVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 0.25f);
            } else if (MySequentialGraphVars.g.getSuccessorCount(n) > 0 && MySequentialGraphVars.g.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 0.25f);
            } else {
                return null;
            }
        } else if (nodeColorWeight > 0.25 && nodeColorWeight <= 0.3) {
            if (MySequentialGraphVars.g.getSuccessorCount(n) > 0 && MySequentialGraphVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 0.3f);
            } else if (MySequentialGraphVars.g.getSuccessorCount(n) == 0 && MySequentialGraphVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 0.3f);
            } else if (MySequentialGraphVars.g.getSuccessorCount(n) > 0 && MySequentialGraphVars.g.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 0.3f);
            } else {
                return null;
            }
        } else if (nodeColorWeight > 0.3 && nodeColorWeight <= 0.35) {
            if (MySequentialGraphVars.g.getSuccessorCount(n) > 0 && MySequentialGraphVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 0.35f);
            } else if (MySequentialGraphVars.g.getSuccessorCount(n) == 0 && MySequentialGraphVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 0.35f);
            } else if (MySequentialGraphVars.g.getSuccessorCount(n) > 0 && MySequentialGraphVars.g.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 0.35f);
            } else {
                return null;
            }
        } else if (nodeColorWeight > 0.35 && nodeColorWeight <= 0.4) {
            if (MySequentialGraphVars.g.getSuccessorCount(n) > 0 && MySequentialGraphVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 0.4f);
            } else if (MySequentialGraphVars.g.getSuccessorCount(n) == 0 && MySequentialGraphVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 0.4f);
            } else if (MySequentialGraphVars.g.getSuccessorCount(n) > 0 && MySequentialGraphVars.g.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 0.4f);
            } else {
                return null;
            }
        } else if (nodeColorWeight > 0.4 && nodeColorWeight <= 0.45) {
            if (MySequentialGraphVars.g.getSuccessorCount(n) > 0 && MySequentialGraphVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 0.45f);
            } else if (MySequentialGraphVars.g.getSuccessorCount(n) == 0 && MySequentialGraphVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 0.45f);
            } else if (MySequentialGraphVars.g.getSuccessorCount(n) > 0 && MySequentialGraphVars.g.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 0.45f);
            } else {
                return null;
            }
        } else if (nodeColorWeight > 0.45 && nodeColorWeight <= 0.5) {
            if (MySequentialGraphVars.g.getSuccessorCount(n) > 0 && MySequentialGraphVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 0.5f);
            } else if (MySequentialGraphVars.g.getSuccessorCount(n) == 0 && MySequentialGraphVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 0.5f);
            } else if (MySequentialGraphVars.g.getSuccessorCount(n) > 0 && MySequentialGraphVars.g.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 0.5f);
            } else {
                return null;
            }
        } else if (nodeColorWeight > 0.5 && nodeColorWeight <= 0.55) {
            if (MySequentialGraphVars.g.getSuccessorCount(n) > 0 && MySequentialGraphVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 0.55f);
            } else if (MySequentialGraphVars.g.getSuccessorCount(n) == 0 && MySequentialGraphVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 0.55f);
            } else if (MySequentialGraphVars.g.getSuccessorCount(n) > 0 && MySequentialGraphVars.g.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 0.55f);
            } else {
                return null;
            }
        } else if (nodeColorWeight > 0.55 && nodeColorWeight <= 0.6) {
            if (MySequentialGraphVars.g.getSuccessorCount(n) > 0 && MySequentialGraphVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 0.6f);
            } else if (MySequentialGraphVars.g.getSuccessorCount(n) == 0 && MySequentialGraphVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 0.6f);
            } else if (MySequentialGraphVars.g.getSuccessorCount(n) > 0 && MySequentialGraphVars.g.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 0.6f);
            } else {
                return null;
            }
        } else if (nodeColorWeight > 0.6 && nodeColorWeight <= 0.65) {
            if (MySequentialGraphVars.g.getSuccessorCount(n) > 0 && MySequentialGraphVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 0.65f);
            } else if (MySequentialGraphVars.g.getSuccessorCount(n) == 0 && MySequentialGraphVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 0.65f);
            } else if (MySequentialGraphVars.g.getSuccessorCount(n) > 0 && MySequentialGraphVars.g.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 0.65f);
            } else {
                return null;
            }
        } else if (nodeColorWeight > 0.65 && nodeColorWeight <= 0.7) {
            if (MySequentialGraphVars.g.getSuccessorCount(n) > 0 && MySequentialGraphVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 0.7f);
            } else if (MySequentialGraphVars.g.getSuccessorCount(n) == 0 && MySequentialGraphVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 0.7f);
            } else if (MySequentialGraphVars.g.getSuccessorCount(n) > 0 && MySequentialGraphVars.g.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 0.7f);
            } else {
                return null;
            }
        } else if (nodeColorWeight > 0.7 && nodeColorWeight <= 0.75) {
            if (MySequentialGraphVars.g.getSuccessorCount(n) > 0 && MySequentialGraphVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 0.75f);
            } else if (MySequentialGraphVars.g.getSuccessorCount(n) == 0 && MySequentialGraphVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 0.75f);
            } else if (MySequentialGraphVars.g.getSuccessorCount(n) > 0 && MySequentialGraphVars.g.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 0.75f);
            } else {
                return null;
            }
        } else if (nodeColorWeight > 0.75 && nodeColorWeight <= 0.8) {
            if (MySequentialGraphVars.g.getSuccessorCount(n) > 0 && MySequentialGraphVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 0.8f);
            } else if (MySequentialGraphVars.g.getSuccessorCount(n) == 0 && MySequentialGraphVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 0.8f);
            } else if (MySequentialGraphVars.g.getSuccessorCount(n) > 0 && MySequentialGraphVars.g.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 0.8f);
            } else {
                return null;
            }
        } else if (nodeColorWeight > 0.8 && nodeColorWeight <= 0.85) {
            if (MySequentialGraphVars.g.getSuccessorCount(n) > 0 && MySequentialGraphVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 0.85f);
            } else if (MySequentialGraphVars.g.getSuccessorCount(n) == 0 && MySequentialGraphVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 0.85f);
            } else if (MySequentialGraphVars.g.getSuccessorCount(n) > 0 && MySequentialGraphVars.g.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 0.85f);
            } else {
                return null;
            }
        } else if (nodeColorWeight > 0.85 && nodeColorWeight <= 0.9) {
            if (MySequentialGraphVars.g.getSuccessorCount(n) > 0 && MySequentialGraphVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 0.9f);
            } else if (MySequentialGraphVars.g.getSuccessorCount(n) == 0 && MySequentialGraphVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 0.9f);
            } else if (MySequentialGraphVars.g.getSuccessorCount(n) > 0 && MySequentialGraphVars.g.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 0.9f);
            } else {
                return null;
            }
        } else if (nodeColorWeight > 0.9 && nodeColorWeight <= 0.95) {
            if (MySequentialGraphVars.g.getSuccessorCount(n) > 0 && MySequentialGraphVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 0.95f);
            } else if (MySequentialGraphVars.g.getSuccessorCount(n) == 0 && MySequentialGraphVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 0.95f);
            } else if (MySequentialGraphVars.g.getSuccessorCount(n) > 0 && MySequentialGraphVars.g.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 0.95f);
            } else {
                return null;
            }
        } else {
            if (MySequentialGraphVars.g.getSuccessorCount(n) > 0 && MySequentialGraphVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 1f);
            } else if (MySequentialGraphVars.g.getSuccessorCount(n) == 0 && MySequentialGraphVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 1f);
            } else if (MySequentialGraphVars.g.getSuccessorCount(n) > 0 && MySequentialGraphVars.g.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 1f);
            } else {
                return null;
            }
        }
    }

    private String setDepthNodeToolTipForSingleSelectedNode(MyNode n, int depth) {
        String tooltip = "";
        if (vc.selectedNodeNeighborNodeTypeSelector.getSelectedItem().toString().contains("S")) {
            if (vc.depthNodeNameSet != null && vc.depthNodeNameSet.contains(n.getName())) {
                tooltip =
                        "<HTML><BODY>" +
                                "NODE: " + MySequentialGraphSysUtil.getNodeName(n.getName()) +  "&nbsp;&nbsp;" +
                                "<BR>CONTRIBUTION: " + MyMathUtil.getCommaSeperatedNumber(((MyNode) MySequentialGraphVars.g.vRefs.get(vc.depthNodeNameSet.iterator().next())).getContribution()) +
                                "<BR>DEPTH: " + depth + " &nbsp;&nbsp;</BODY></HTML>";
            } else {
                tooltip =
                        "<HTML>" +
                                "<BODY>" +
                                "NODE: " + MySequentialGraphSysUtil.getNodeName(n.getName()) + "&nbsp;&nbsp;" +
                                "<BR>CONTRIBUTION: " + MyMathUtil.getCommaSeperatedNumber(vc.depthNodeSuccessorMaps.get(vc.depthNodeNameSet.iterator().next()).get(n.getName())) +
                                "<BR>DEPTH: " + depth + " &nbsp;&nbsp;</BODY></HTML>";
            }
        } else if  (vc.selectedNodeNeighborNodeTypeSelector.getSelectedItem().toString().contains("P")) {
            if (vc.depthNodeNameSet != null && vc.depthNodeNameSet.contains(n.getName())) {
                tooltip =
                        "<HTML><BODY>" +
                                "NODE: " + MySequentialGraphSysUtil.getNodeName(n.getName()) + "&nbsp;&nbsp;" +
                                "<BR>CONTRIBUTION: " + MyMathUtil.getCommaSeperatedNumber(n.getNodeDepthInfo(MySequentialGraphVars.currentGraphDepth).getContribution()) +
                                "<BR>DEPTH: " + depth + " &nbsp;&nbsp;</BODY></HTML>";
            } else {
                tooltip =
                        "<HTML><BODY>" +
                                "NODE: " + MySequentialGraphSysUtil.getNodeName(n.getName()) + "&nbsp;&nbsp;" +
                                "<BR>CONTRIBUTION: " + MyMathUtil.getCommaSeperatedNumber(vc.depthNodePredecessorMaps.get(vc.depthNodeNameSet.iterator().next()).get(n.getName())) +
                                "<BR>DEPTH: " + depth + " &nbsp;&nbsp;</BODY></HTML>";
            }
        } else if (n == singleNode) {
            tooltip =
                "<HTML><BODY>" +
                "NODE: " + MySequentialGraphSysUtil.getNodeName(n.getName()) +  "&nbsp;&nbsp;" +
                "<BR>CONTRIBUTION: " + MyMathUtil.getCommaSeperatedNumber(n.getNodeDepthInfo(depth).getContribution()) +  "&nbsp;&nbsp;</BODY></HTML>";
        }
        return tooltip;
    }

    private String setDepthNodeToolTip(MyNode n, int depth) {
        String toolTip = "<HTML>" +
            "<BODY>" +
            "NODE: " + MySequentialGraphSysUtil.getNodeName(n.getName()) +  "&nbsp;&nbsp;" +
            "<BR>CONTRIBUTION: " + MyMathUtil.getCommaSeperatedNumber(n.getNodeDepthInfo(depth).getContribution()) +  "&nbsp;&nbsp;" +
            "<BR>IN CONTRIBUTION: " + MyMathUtil.getCommaSeperatedNumber(n.getNodeDepthInfo(depth).getInContribution()) +  "&nbsp;&nbsp;" +
            "<BR>OUT CONTRIBUTION: " + MyMathUtil.getCommaSeperatedNumber(n.getNodeDepthInfo(depth).getOutContribution()) +  "&nbsp;&nbsp;" +
            "<BR>DEPTH: " +depth +  " &nbsp;&nbsp;";
            if (MySequentialGraphVars.isTimeOn) {
                toolTip += "<BR>REACH TIME: " + MyMathUtil.getCommaSeperatedNumber(n.getNodeDepthInfo(depth).getReachTime()) + "&nbsp;&nbsp;" +
                "<BR>AVG. REACH TIME: " + MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(n.getNodeDepthInfo(depth).getAverageReachTime())) + "&nbsp;&nbsp;" +
                "<BR>DURATION: " + MyMathUtil.getCommaSeperatedNumber(n.getNodeDepthInfo(depth).getDuration()) + "&nbsp;&nbsp;" +
                "<BR>AVG. DURATION: " + MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(n.getNodeDepthInfo(depth).getAverageDuration())) + "&nbsp;&nbsp;";
            }
            toolTip += "<BR>PRDECESSORS: " + MySequentialGraphSysUtil.getCommaSeperateString(n.getNodeDepthInfo(depth).getPredecessorCount()) +  "&nbsp;&nbsp;" +
            "<BR>SUCCESSORS: " + MySequentialGraphSysUtil.getCommaSeperateString(n.getNodeDepthInfo(depth).getSuccessorCount()) +  "&nbsp;&nbsp;</BODY></HTML>";
        return toolTip;
    }

    private String setTopNodeToolTip(MyNode n) {
        String toolTip = "<HTML>" +
            "<BODY>NODE: " + MySequentialGraphSysUtil.getNodeName(n.getName()) +
            (MySequentialGraphVars.isSupplementaryOn && !n.getName().contains("x") ? "<BR>RELATED VARIABLES: " + MyMathUtil.getCommaSeperatedNumber(n.getVariableStrengthMap().size()) : "") +
            (MySequentialGraphVars.isSupplementaryOn && !n.getName().contains("x") ? "<BR>VARIABLE STRENGTH: "  +  MyMathUtil.getCommaSeperatedNumber(n.getTotalVariableStrength()) : "") +
            "<BR>CONTRIBUTION: " + MyMathUtil.getCommaSeperatedNumber(n.getContribution()) +
            "<BR>IN-CONTRIBUTION: " + MyMathUtil.getCommaSeperatedNumber(n.getInContribution()) +
            "<BR>AVG. IN-CONTRIBUTION: " + MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(n.getAverageInContribution())) +
            "<BR>OUT CONTRIBUTION: " + MyMathUtil.getCommaSeperatedNumber(n.getOutContribution()) +
            "<BR>AVG. OUT-CONTRIBUTION: " + MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(n.getAverageOutContribution())) +
            "<BR>UNIQUE CONTRIBUTION: " + MyMathUtil.getCommaSeperatedNumber(n.getUniqueContribution()) +
            "<BR>AVG. RECCURRENCE LENGTH: " + MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(n.getAverageRecurrenceLength()))    ;
        if (MySequentialGraphVars.isTimeOn) {
            toolTip += "<BR>REACH TIME: " + MyMathUtil.getCommaSeperatedNumber(n.getTotalReachTime()) +
                "<BR>AVG. REACH TIME: " + MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(n.getAverageReachTime())) +
                "<BR>DURATION: " + MyMathUtil.getCommaSeperatedNumber(n.getTotalDuration()) +
                "<BR>MAX. DURATION: " + MyMathUtil.getCommaSeperatedNumber(n.getMaxDuration()) +
                "<BR>MIN. DURATION: " + MyMathUtil.getCommaSeperatedNumber(n.getMinDuration()) +
                "<BR>AVG. DURATION: " + MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(n.getAverageDuration()));
        }

        toolTip += "<BR>STARTING POISITION COUNT: " + MySequentialGraphSysUtil.getCommaSeperateString(n.getStartPositionNodeCount()) +
            "<BR>END POSITION COUNT: " + MySequentialGraphSysUtil.getCommaSeperateString(n.getEndPositionNodeCount()) +
            "<BR>PRDECESSORS: " + MySequentialGraphSysUtil.getCommaSeperateString(n.getPredecessorCount()) +
            "<BR>SUCCESSORS: " + MySequentialGraphSysUtil.getCommaSeperateString(n.getSuccessorCount()) +
            "<BR>RECURRENCE COUNT: " + MySequentialGraphSysUtil.getCommaSeperateString(n.getTotalNodeRecurrentCount()) +
            "<BR>UNREACHABLE NODE COUNT: " + MySequentialGraphSysUtil.getCommaSeperateString(n.getUnreachableNodeCount()) + "</BODY></HTML>";
        return toolTip;
    }

}
