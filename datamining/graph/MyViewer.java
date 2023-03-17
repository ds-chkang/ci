package datamining.graph;

import datamining.graph.listener.MyMotionListener;
import datamining.graph.stats.depthnode.MyDepthLevelNeighborNodeValueBarChart;
import datamining.graph.stats.depthnode.MyDepthLevelNodeValueBarChart;
import datamining.graph.stats.MyGraphLevelEdgeValueBarChart;
import datamining.graph.stats.MyGraphLevelNodeValueBarChart;
import datamining.graph.stats.singlenode.MySingleNodeEdgeValueBarChart;
import datamining.graph.stats.singlenode.MySingleNodeNeighborNodeValueBarChart;
import datamining.graph.stats.multinode.MyMultiLevelEdgeValueBarChart;
import datamining.graph.stats.multinode.MyMultiLevelNeighborNodeValueBarChart;
import datamining.graph.listener.MyGraphMouseListener;
import datamining.graph.listener.MyViewerMouseListener;
import datamining.utils.MyMathUtil;
import datamining.utils.system.MySysUtil;
import datamining.utils.system.MyVars;
import edu.uci.ics.jung.visualization.VisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import org.apache.commons.collections15.Transformer;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.*;

import static java.awt.Cursor.HAND_CURSOR;

public class MyViewer
extends VisualizationViewer<MyNode, MyEdge>
implements Serializable {

    public MyNode startNode;
    public String nodeValueName = "CONTRIBUTION";
    public String edgeValName = "NONE";
    public boolean successorsOnly;
    public boolean predecessorsOnly;
    public boolean predecessorsAndSuccessors;
    public boolean isExcludeBtnOn;
    private ScalingControl viewScaler;
    public final float MX_E_STK = 30.0f;
    public final float MIN_NODE_SZ = 54.0f;
    public Map<String, Integer> endNodesMap;
    public Set<String> nodesBetweenTwoNodes;
    public MyNode selectedNode;
    public Set<MyNode> selectedSingleNodePredecessors;
    public Set<MyNode> selectedSingleNodeSuccessors;
    public Set<MyNode> multiNodes;
    public Set<MyNode> multiNodePredecessors;
    public Set<MyNode> multiNodeSuccessors;
    public MyViewerComponentController vc;
    public Set<MyNode> sharedPredecessors;
    public Set<MyNode> sharedSuccessors;
    public MyGraphLevelNodeValueBarChart graphLevelNodeValueBarChart;
    public MyGraphLevelEdgeValueBarChart graphLelvelEdgeValueBarChart;
    public MySingleNodeNeighborNodeValueBarChart nodeLevelNeighborNodeValueBarChart;
    public MyMultiLevelNeighborNodeValueBarChart sharedNodeValueBarChart;
    public MyMultiLevelEdgeValueBarChart multiNodeLevelEdgeValueBarChart;
    public MyDepthLevelNodeValueBarChart depthNodeLevelNodeValueBarChart;
    public MyDepthLevelNeighborNodeValueBarChart depthNodeLevelNeighborNodeValueBarChart;
    public MySingleNodeEdgeValueBarChart singleNodeLevelEdgeValueBarChart;
    public MyGraphMouseListener graphMouseListener;
    public Set<MyNode> selectedTableNodeSet;
    public Point MOUSE_START_POS = new Point();
    public Point MOUSE_END_POS = new Point();

    public MyViewer(VisualizationModel<MyNode, MyEdge> vm) {
        super(vm);
        try {
            this.init();
            this.setPreferredSize(new Dimension(5500, 4500));
            DefaultModalGraphMouse graphMouse = new DefaultModalGraphMouse();
            graphMouse.setMode(DefaultModalGraphMouse.Mode.PICKING);
            this.setGraphMouse(graphMouse);

            this.graphMouseListener = new MyGraphMouseListener();
            this.addGraphMouseListener(this.graphMouseListener);
            this.addMouseListener(new MyViewerMouseListener(this));
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
            this.getRenderContext().setVertexStrokeTransformer(nodeStroker);
            this.getRenderContext().setVertexDrawPaintTransformer(new Transformer<MyNode, Paint>() {
                @Override public Paint transform(MyNode n) {
                    if (vc.depthSelecter.getSelectedIndex() > 0) {
                        if (vc.nodeListTable.getSelectedRow() >= 0) {
                            String tableNode = vc.nodeListTable.getValueAt(vc.nodeListTable.getSelectedRow(), 1).toString();
                            if (tableNode.equals(MySysUtil.getDecodedNodeName(n.getName()))) {return Color.ORANGE;
                            } else {return Color.DARK_GRAY;}
                        } else {return Color.DARK_GRAY;}
                    } else if ((selectedNode != null && n == selectedNode) || (multiNodes != null && multiNodes.contains(n))) {return Color.ORANGE;
                    } else {return Color.DARK_GRAY;}
                }
            });
            this.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.S);
            this.getRenderContext().setVertexLabelTransformer(this.nodeLabeller);
            this.getRenderContext().setEdgeLabelTransformer(new Transformer<MyEdge, String>() {
                @Override public synchronized String transform(MyEdge edge) {
                    return "";
                }
            });
            this.getRenderContext().setVertexFontTransformer(new Transformer<MyNode, Font>() {
                @Override public synchronized Font transform(MyNode node) {
                    if (node.getCurrentValue() == 0) {return new Font("Noto Sans", Font.PLAIN, 0);
                    } else {return new Font("Noto Sans", Font.BOLD, 30);}
                }
            });
            this.getRenderContext().setEdgeFontTransformer(new Transformer<MyEdge, Font>() {
                @Override public synchronized Font transform(MyEdge e) {
                    if (e.getCurrentValue() == 0) {return new Font("Noto Sans", Font.PLAIN, 0);
                    } else {return new Font("Noto Sans", Font.BOLD, 40);}
                }
            });
            this.getRenderContext().setEdgeShapeTransformer(new EdgeShape.QuadCurve<MyNode, MyEdge>());
            this.vc = new MyViewerComponentController();
        } catch (Exception ex) {}
    }

    private void init() {
        nodeValueName = "CONTRIBUTION";
        edgeValName = "NONE";
        endNodesMap = new HashMap<>();
        nodesBetweenTwoNodes = new HashSet<>();
        predecessorsOnly = false;
        successorsOnly = false;
        isExcludeBtnOn = false;
        vc = null;
        startNode = null;
        multiNodes = null;
        selectedSingleNodePredecessors = new HashSet<>();
        selectedSingleNodeSuccessors = new HashSet<>();
        sharedSuccessors = new HashSet<>();
        sharedPredecessors = new HashSet<>();
        multiNodePredecessors = new HashSet<>();
        multiNodeSuccessors = new HashSet<>();
        selectedNode = null;
        predecessorsAndSuccessors = false;
    }

    private Transformer<MyNode, String> nodeLabeller = new Transformer<MyNode, String>() {
        @Override public String transform(MyNode n) {
            String name = "";
            if (n.getName().contains("x")) {
                name = MySysUtil.decodeVariable(n.getName());
            } else {
                name = MySysUtil.decodeNodeName(n.getName());
            }
            if (multiNodes != null) {
                if (multiNodes.contains(n) || multiNodeSuccessors.contains(n) || multiNodePredecessors.contains(n)) {
                    return name;
                } else {
                    return "";
                }
            } else if (selectedNode != null) {
                if (predecessorsOnly) {
                    if (n == selectedNode || selectedSingleNodePredecessors.contains(n)) {
                        return name;
                    } else {
                        return "";
                    }
                } else if (successorsOnly) {
                    if (n == selectedNode || selectedSingleNodeSuccessors.contains(n)) {
                        return name;
                    } else {
                        return "";
                    }
                } else if (predecessorsAndSuccessors) {
                    if (n == selectedNode || selectedSingleNodeSuccessors.contains(n) || selectedSingleNodePredecessors.contains(n)) {
                        return name;
                    } else {
                        return "";
                    }
                } else if (n == selectedNode || selectedSingleNodeSuccessors.contains(n) || selectedSingleNodePredecessors.contains(n)) {
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
            if ((selectedNode != null && n == selectedNode) || (multiNodes != null && multiNodes.contains(n))) {
                return new BasicStroke(30f);
            } else {
                float currentNodeValueWeight = n.getCurrentValue()/MyVars.g.MX_N_VAL;
                return setNodeDrawStroke(currentNodeValueWeight);
            }
        }
    };

    private Transformer<MyEdge, Stroke> edgeStroker = new Transformer<MyEdge, Stroke>() {
        @Override public Stroke transform(MyEdge e) {
            if (e.getCurrentValue() == 0) {
                return new BasicStroke(0.0f);
            } else if (e.getSource() == e.getDest()) {
                return new BasicStroke(MX_E_STK/2);
            } else {
                float edgeStrokeWeight = e.getCurrentValue()/MyVars.g.MX_E_VAL;
                //System.out.println(e.getSource().getName() + " - " + e.getDest().getName() + ": " + edgeStrokeWeight*MX_E_STK);
                if (MyVars.getViewer().vc.selectedNodeNeighborNodeTypeSelector.isShowing()) {
                    return new BasicStroke(10f+(edgeStrokeWeight * MX_E_STK));
                } else {
                    return new BasicStroke(edgeStrokeWeight * MX_E_STK);
                }
            }
        }
    };

    public Transformer<MyNode, Paint> unWeightedNodeColor = new Transformer<MyNode, Paint>() {
        @Override public Paint transform(MyNode n) {
            if (vc.depthNeighborNodeTypeSelector.isShowing() || vc.selectedNodeNeighborNodeTypeSelector.isShowing()) {
                if (vc.depthNodeNameSet != null && vc.depthNodeNameSet.contains(n.getName())) {
                    return new Color(1.0f, 0.0f, 0.0f, 0.7f);
                } else if (vc.depthNodePredecessorMaps != null && vc.depthNodePredecessorMaps.containsKey(n.getName())) {
                    return new Color(0.0f, 0.0f, 0.0f, 0.5f);
                } else if (vc.depthNodeSuccessorMaps != null && vc.depthNodeSuccessorMaps.containsKey(n.getName())) {
                    return new Color(0.0f, 0.0f, 0.0f, 0.5f);
                } else {
                    return new Color(0.0f, 0.0f, 1.0f, 0.7f);
                }
            } else if (multiNodes != null) {
                if (MyVars.g.getSuccessors(n).containsAll(multiNodes)) {
                    return Color.ORANGE;
                } else if (MyVars.g.getPredecessors(n).containsAll(multiNodes)) {
                    return Color.ORANGE;
                } else if (multiNodes.contains(n) || multiNodePredecessors.contains(n) || multiNodeSuccessors.contains(n)) {
                    if (MyVars.g.getSuccessorCount(n) == 0 && MyVars.g.getPredecessorCount(n) > 0) {
                        return new Color(0.0f, 1.0f, 0.0f, 0.7f);
                    } else if (MyVars.g.getSuccessorCount(n) > 0 && MyVars.g.getPredecessorCount(n) == 0) {
                        return new Color(1.0f, 0.0f, 0.0f, 0.7f);
                    } else {
                        return new Color(0.0f, 0.0f, 1.0f, 0.7f);
                    }
                } else {
                    return new Color(0.0f, 0.0f, 0.0f, 0f);
                }
            } else if (MyVars.g.getSuccessorCount(n) == 0 && MyVars.g.getPredecessorCount(n) > 0) {
                return new Color(0.0f, 1.0f, 0.0f, 0.7f);
            } else if (MyVars.g.getSuccessorCount(n) > 0 && MyVars.g.getPredecessorCount(n) == 0) {
                return new Color(1.0f, 0.0f, 0.0f, 0.7f);
            } else {
                return new Color(0.0f, 0.0f, 1.0f, 0.7f);
            }
        }
    };


    public Transformer<MyNode, Paint> weightedNodeColor = new Transformer<MyNode, Paint>() {
        @Override public Paint transform(MyNode n) {
            if (vc.depthNeighborNodeTypeSelector.getSelectedIndex() > 0) {
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

    private boolean sharedPredecessorsOly;
    private boolean sharedSuccessorsOnly;

    private Transformer<MyNode, Shape> nodeSizer = new Transformer<MyNode, Shape>() {
        @Override public Shape transform(MyNode n) {
            if (n.getCurrentValue() == 0) {
                return new Ellipse2D.Double(0, 0, 0, 0);
            } else if (multiNodes != null) {
                if (predecessorsOnly) {
                    if (multiNodes.contains(n)) {
                        return setNodeSize(n.getCurrentValue()/MyVars.g.MX_N_VAL);
                    } else if (multiNodePredecessors.contains(n)) {
                        return setNodeSize(n.getCurrentValue()/MyVars.g.MX_N_VAL);
                    } else {
                        return new Ellipse2D.Double(0, 0, 0, 0);
                    }
                } else if (successorsOnly) {
                    if (multiNodes.contains(n)) {
                        return setNodeSize(n.getCurrentValue()/MyVars.g.MX_N_VAL);
                    } else if (multiNodeSuccessors.contains(n)) {
                        return setNodeSize(n.getCurrentValue()/MyVars.g.MX_N_VAL);
                    } else {
                        return new Ellipse2D.Double(0, 0, 0, 0);
                    }
                } else if (predecessorsAndSuccessors) {
                    if (multiNodes.contains(n)) {
                        return setNodeSize(n.getCurrentValue()/MyVars.g.MX_N_VAL);
                    } else if (multiNodeSuccessors.contains(n) || multiNodePredecessors.contains(n)) {
                        return setNodeSize(n.getCurrentValue()/MyVars.g.MX_N_VAL);
                    } else {
                        return new Ellipse2D.Double(0, 0, 0, 0);
                    }
                } else if (multiNodes.contains(n)) {
                    return setNodeSize(n.getCurrentValue()/MyVars.g.MX_N_VAL);
                } else if (multiNodeSuccessors.contains(n) || multiNodePredecessors.contains(n)) {
                    return setNodeSize(n.getCurrentValue()/MyVars.g.MX_N_VAL);
                } else {
                    return new Ellipse2D.Double(0, 0, 0, 0);
                }
            } else if (selectedNode != null) {
                if (predecessorsOnly) {
                    if (n == selectedNode) {
                        return setNodeSize(n.getCurrentValue() / MyVars.g.MX_N_VAL);
                    } else if (selectedSingleNodePredecessors.contains(n)) {
                        return setNodeSize(n.getCurrentValue() / MyVars.g.MX_N_VAL);
                    } else {
                        return new Ellipse2D.Double(0, 0, 0, 0);
                    }
                } else if (successorsOnly) {
                    if (n == selectedNode) {
                        return setNodeSize(n.getCurrentValue() / MyVars.g.MX_N_VAL);
                    } else if (selectedSingleNodeSuccessors.contains(n)) {
                        return setNodeSize(n.getCurrentValue() / MyVars.g.MX_N_VAL);
                    } else {
                        return new Ellipse2D.Double(0, 0, 0, 0);
                    }
                } else if (predecessorsAndSuccessors) {
                    if (n == selectedNode) {
                        return setNodeSize(n.getCurrentValue() / MyVars.g.MX_N_VAL);
                    } else if (selectedSingleNodeSuccessors.contains(n)) {
                        return setNodeSize(n.getCurrentValue() / MyVars.g.MX_N_VAL);
                    } else if (selectedSingleNodePredecessors.contains(n)) {
                        return setNodeSize(n.getCurrentValue() / MyVars.g.MX_N_VAL);
                    } else {
                        return new Ellipse2D.Double(0, 0, 0, 0);
                    }
                } else if (n != selectedNode &&
                        !selectedSingleNodePredecessors.contains(n) &&
                        !selectedSingleNodeSuccessors.contains(n)) {
                    return new Ellipse2D.Double(0, 0, 0, 0);
                } else {
                    return setNodeSize(n.getCurrentValue() / MyVars.g.MX_N_VAL);
                }
            } else if (n.getCurrentValue() == 0) {
                return new Ellipse2D.Double(0, 0, 0, 0);
            } else {
                return setNodeSize(n.getCurrentValue() / MyVars.g.MX_N_VAL);
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
            if (MyVars.currentGraphDepth == 0) {return setTopNodeToolTip(n);}
            else if (vc.selectedNodeNeighborNodeTypeSelector.getSelectedIndex() > 0) {
                if (n == selectedNode) {
                    return setDepthNodeToolTipForSingleSelectedNode(n, MyVars.currentGraphDepth);
                } else if (vc.selectedNodeNeighborNodeTypeSelector.getSelectedItem().toString().contains("P")) {
                    return setDepthNodeToolTipForSingleSelectedNode(n, MyVars.currentGraphDepth - 1);
                } else if (vc.selectedNodeNeighborNodeTypeSelector.getSelectedItem().toString().contains("S")) {
                    return setDepthNodeToolTipForSingleSelectedNode(n, MyVars.currentGraphDepth + 1);
                } else if (vc.selectedNodeNeighborNodeTypeSelector.getSelectedItem().toString().contains("B")) {
                    if (n.getNodeDepthInfo(MyVars.currentGraphDepth -1) != null && n.getNodeDepthInfo(MyVars.currentGraphDepth +1) != null) {
                        return "";  // Show both information when a node is a predecessor and a successor.
                    } else if (n.getNodeDepthInfo(MyVars.currentGraphDepth -1) != null) {
                        return ""; // Show predecessor node information.
                    } else if (n.getNodeDepthInfo(MyVars.currentGraphDepth +1) != null) {
                        return ""; // Show successor node information.
                    } else {
                        return "";
                    }
                } else {
                    return "";
                }
            } else if (n.getNodeDepthInfo(MyVars.currentGraphDepth) != null) {
                int depth = MyVars.currentGraphDepth;
                return setDepthNodeToolTip(n, depth);
            } else if (MyVars.getViewer().vc.depthNeighborNodeTypeSelector.getSelectedItem().toString().contains("P") &&
                    n.getNodeDepthInfo(MyVars.currentGraphDepth -1) != null) {
                int depth = MyVars.currentGraphDepth -1;
                return setDepthNodeToolTip(n, depth);
            } else if (MyVars.getViewer().vc.depthNeighborNodeTypeSelector.getSelectedItem().toString().contains("S") &&
                    n.getNodeDepthInfo(MyVars.currentGraphDepth +1) != null) {
                int depth = MyVars.currentGraphDepth +1;
                return setDepthNodeToolTip(n, depth);
            } else {return "";} // <== 이부분 확인.
        }
    };

    public MyViewer create() {
        this.scale();
        return this;
    }

    private void scale() {
        this.viewScaler = new CrossoverScalingControl();
        double amount = -1.0;
        if (MyVars.app.getWidth() <= 1800) {
            viewScaler.scale(this, amount > 0 ? 2.0f : 1 / 10.1f, new Point2D.Double(320, 120));
        } else {
            viewScaler.scale(this, amount > 0 ? 2.0f : 1 / 13.4f, new Point2D.Double(120, 60));
        }
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

    public synchronized Paint setEdgeColor(MyEdge e) {
        if (vc.depthNeighborNodeTypeSelector.getSelectedIndex() > 0) {
            if (selectedNode != null && e.getSource() != selectedNode && e.getDest() != selectedNode) {
                return new Color(0f, 0f, 0.0f, 0.0f);
            } //else if (vc.depthNodeNameSet != null && vc.depthNodeNameSet.contains(e.getSource().getName()) && vc.depthNodeNameSet.contains(e.getDest().getName())) {
            //  return new Color(0f, 0f, 0.0f, 0.0f);}
            else if (vc.depthNeighborNodeTypeSelector.getSelectedItem().toString().equals("S.")) {
                if (vc.depthNodeSuccessorMaps != null && vc.depthNodeSuccessorMaps.containsKey(e.getSource().getName()) &&
                        vc.depthNodeSuccessorMaps.get(e.getSource().getName()).containsKey(e.getDest().getName())) {
                    return new Color(0f, 0f, 0.0f, 0.7f);
                } else {
                    return new Color(0f, 0f, 0.0f, 0.0f);
                }
            } else if (vc.depthNeighborNodeTypeSelector.getSelectedItem().toString().equals("P.")) {
                if (vc.depthNodePredecessorMaps != null && vc.depthNodePredecessorMaps.containsKey(e.getDest().getName()) &&
                        vc.depthNodePredecessorMaps.get(e.getDest().getName()).containsKey(e.getSource().getName())) {
                    return new Color(0f, 0f, 0.0f, 0.7f);
                } else {
                    return new Color(0f, 0f, 0.0f, 0.0f);
                }
            } else {
                return new Color(0f, 0f, 0.0f, 0.0f);
            }
        } else if (multiNodes != null) {
            if (e.getDest() == e.getSource() && multiNodes.contains(e.getDest())) {
                return Color.YELLOW;
            } else if (multiNodes.contains(e.getSource()) && multiNodeSuccessors.contains(e.getDest())) {
                return new Color(0f, 0f, 1.0f, 0.25f);
            } else if (multiNodes.contains(e.getDest()) && multiNodePredecessors.contains(e.getSource())) {
                return new Color(1f, 0f, 0f, 0.25f);
            } else {
                return new Color(0f, 0f, 0f, 0f);
            }
        } else if (selectedNode != null) {
            if (predecessorsOnly) {
                if (e.getSource() == selectedNode && e.getDest() == selectedNode) {
                    return Color.BLACK;
                } else if (e.getDest() == selectedNode && selectedSingleNodePredecessors.contains(e.getSource())) {
                    return new Color(1.0f, 0, 0, 0.25f);
                } else {
                    return new Color(0, 0, 0, 0f);
                }
            } else if (successorsOnly) {
                if (e.getSource() == selectedNode && e.getDest() == selectedNode) {
                    return Color.BLACK;
                } else if (e.getSource() == selectedNode && selectedSingleNodeSuccessors.contains(e.getDest())) {
                    return new Color(0, 0, 1.0f, 0.25f);
                } else {
                    return new Color(0, 0, 0, 0f);
                }
            } else if (predecessorsAndSuccessors) {
                if (e.getSource() == selectedNode && e.getDest() == selectedNode) {
                    return Color.BLACK;
                } else if (e.getSource() == selectedNode && selectedSingleNodeSuccessors.contains(e.getDest())) {
                    return new Color(0, 0, 1.0f, 0.25f);
                } else if (e.getDest() == selectedNode && selectedSingleNodePredecessors.contains(e.getSource())) {
                    return new Color(1.0f, 0, 0, 0.25f);
                } else {
                    return new Color(0, 0, 0, 0f);
                }
            } else if (selectedNode != e.getDest() && selectedNode != e.getSource()) {
                return new Color(0.0f, 0.0f, 0.0f, 0.0f);
            } else if (e.getCurrentValue() == 0 || e.getSource().getCurrentValue() == 0 || e.getDest().getCurrentValue() == 0) {
                return new Color(0.0f, 0.0f, 0.0f, 0.0f);
            } else if (e.getSource() == selectedNode && e.getDest() == selectedNode) {
                return Color.ORANGE;
            } else if (e.getSource() == selectedNode) {
                return new Color(0f, 0f, 1f, 0.25f);
            } else if (e.getDest() == selectedNode) {
                return new Color(1f, 0f, 0f, 0.25f);
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
        float nodeColorWeight = n.getCurrentValue()/MyVars.g.MX_N_VAL;
        if (nodeColorWeight <= 0.05) {
            if (MyVars.g.getSuccessorCount(n) > 0 && MyVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 0.05f);
            } else if (MyVars.g.getSuccessorCount(n) == 0 && MyVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 0.05f);
            } else if (MyVars.g.getSuccessorCount(n) > 0 && MyVars.g.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 0.05f);
            } else {
                return null;
            }
        } else if (nodeColorWeight > 0.05 && nodeColorWeight <= 0.1) {
            if (MyVars.g.getSuccessorCount(n) > 0 && MyVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 0.1f);
            } else if (MyVars.g.getSuccessorCount(n) == 0 && MyVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 0.1f);
            } else if (MyVars.g.getSuccessorCount(n) > 0 && MyVars.g.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 0.1f);
            } else {
                return null;
            }
        } else if (nodeColorWeight > 0.1 && nodeColorWeight <= 0.15) {
            if (MyVars.g.getSuccessorCount(n) > 0 && MyVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 0.15f);
            } else if (MyVars.g.getSuccessorCount(n) == 0 && MyVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 0.15f);
            } else if (MyVars.g.getSuccessorCount(n) > 0 && MyVars.g.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 0.15f);
            } else {
                return null;
            }
        } else if (nodeColorWeight > 0.15 && nodeColorWeight <= 0.2) {
            if (MyVars.g.getSuccessorCount(n) > 0 && MyVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 0.2f);
            } else if (MyVars.g.getSuccessorCount(n) == 0 && MyVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 0.2f);
            } else if (MyVars.g.getSuccessorCount(n) > 0 && MyVars.g.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 0.2f);
            } else {
                return null;
            }
        } else if (nodeColorWeight > 0.2 && nodeColorWeight <= 0.25) {
            if (MyVars.g.getSuccessorCount(n) > 0 && MyVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 0.25f);
            } else if (MyVars.g.getSuccessorCount(n) == 0 && MyVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 0.25f);
            } else if (MyVars.g.getSuccessorCount(n) > 0 && MyVars.g.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 0.25f);
            } else {
                return null;
            }
        } else if (nodeColorWeight > 0.25 && nodeColorWeight <= 0.3) {
            if (MyVars.g.getSuccessorCount(n) > 0 && MyVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 0.3f);
            } else if (MyVars.g.getSuccessorCount(n) == 0 && MyVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 0.3f);
            } else if (MyVars.g.getSuccessorCount(n) > 0 && MyVars.g.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 0.3f);
            } else {
                return null;
            }
        } else if (nodeColorWeight > 0.3 && nodeColorWeight <= 0.35) {
            if (MyVars.g.getSuccessorCount(n) > 0 && MyVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 0.35f);
            } else if (MyVars.g.getSuccessorCount(n) == 0 && MyVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 0.35f);
            } else if (MyVars.g.getSuccessorCount(n) > 0 && MyVars.g.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 0.35f);
            } else {
                return null;
            }
        } else if (nodeColorWeight > 0.35 && nodeColorWeight <= 0.4) {
            if (MyVars.g.getSuccessorCount(n) > 0 && MyVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 0.4f);
            } else if (MyVars.g.getSuccessorCount(n) == 0 && MyVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 0.4f);
            } else if (MyVars.g.getSuccessorCount(n) > 0 && MyVars.g.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 0.4f);
            } else {
                return null;
            }
        } else if (nodeColorWeight > 0.4 && nodeColorWeight <= 0.45) {
            if (MyVars.g.getSuccessorCount(n) > 0 && MyVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 0.45f);
            } else if (MyVars.g.getSuccessorCount(n) == 0 && MyVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 0.45f);
            } else if (MyVars.g.getSuccessorCount(n) > 0 && MyVars.g.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 0.45f);
            } else {
                return null;
            }
        } else if (nodeColorWeight > 0.45 && nodeColorWeight <= 0.5) {
            if (MyVars.g.getSuccessorCount(n) > 0 && MyVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 0.5f);
            } else if (MyVars.g.getSuccessorCount(n) == 0 && MyVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 0.5f);
            } else if (MyVars.g.getSuccessorCount(n) > 0 && MyVars.g.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 0.5f);
            } else {
                return null;
            }
        } else if (nodeColorWeight > 0.5 && nodeColorWeight <= 0.55) {
            if (MyVars.g.getSuccessorCount(n) > 0 && MyVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 0.55f);
            } else if (MyVars.g.getSuccessorCount(n) == 0 && MyVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 0.55f);
            } else if (MyVars.g.getSuccessorCount(n) > 0 && MyVars.g.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 0.55f);
            } else {
                return null;
            }
        } else if (nodeColorWeight > 0.55 && nodeColorWeight <= 0.6) {
            if (MyVars.g.getSuccessorCount(n) > 0 && MyVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 0.6f);
            } else if (MyVars.g.getSuccessorCount(n) == 0 && MyVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 0.6f);
            } else if (MyVars.g.getSuccessorCount(n) > 0 && MyVars.g.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 0.6f);
            } else {
                return null;
            }
        } else if (nodeColorWeight > 0.6 && nodeColorWeight <= 0.65) {
            if (MyVars.g.getSuccessorCount(n) > 0 && MyVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 0.65f);
            } else if (MyVars.g.getSuccessorCount(n) == 0 && MyVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 0.65f);
            } else if (MyVars.g.getSuccessorCount(n) > 0 && MyVars.g.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 0.65f);
            } else {
                return null;
            }
        } else if (nodeColorWeight > 0.65 && nodeColorWeight <= 0.7) {
            if (MyVars.g.getSuccessorCount(n) > 0 && MyVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 0.7f);
            } else if (MyVars.g.getSuccessorCount(n) == 0 && MyVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 0.7f);
            } else if (MyVars.g.getSuccessorCount(n) > 0 && MyVars.g.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 0.7f);
            } else {
                return null;
            }
        } else if (nodeColorWeight > 0.7 && nodeColorWeight <= 0.75) {
            if (MyVars.g.getSuccessorCount(n) > 0 && MyVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 0.75f);
            } else if (MyVars.g.getSuccessorCount(n) == 0 && MyVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 0.75f);
            } else if (MyVars.g.getSuccessorCount(n) > 0 && MyVars.g.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 0.75f);
            } else {
                return null;
            }
        } else if (nodeColorWeight > 0.75 && nodeColorWeight <= 0.8) {
            if (MyVars.g.getSuccessorCount(n) > 0 && MyVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 0.8f);
            } else if (MyVars.g.getSuccessorCount(n) == 0 && MyVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 0.8f);
            } else if (MyVars.g.getSuccessorCount(n) > 0 && MyVars.g.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 0.8f);
            } else {
                return null;
            }
        } else if (nodeColorWeight > 0.8 && nodeColorWeight <= 0.85) {
            if (MyVars.g.getSuccessorCount(n) > 0 && MyVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 0.85f);
            } else if (MyVars.g.getSuccessorCount(n) == 0 && MyVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 0.85f);
            } else if (MyVars.g.getSuccessorCount(n) > 0 && MyVars.g.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 0.85f);
            } else {
                return null;
            }
        } else if (nodeColorWeight > 0.85 && nodeColorWeight <= 0.9) {
            if (MyVars.g.getSuccessorCount(n) > 0 && MyVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 0.9f);
            } else if (MyVars.g.getSuccessorCount(n) == 0 && MyVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 0.9f);
            } else if (MyVars.g.getSuccessorCount(n) > 0 && MyVars.g.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 0.9f);
            } else {
                return null;
            }
        } else if (nodeColorWeight > 0.9 && nodeColorWeight <= 0.95) {
            if (MyVars.g.getSuccessorCount(n) > 0 && MyVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 0.95f);
            } else if (MyVars.g.getSuccessorCount(n) == 0 && MyVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 0.95f);
            } else if (MyVars.g.getSuccessorCount(n) > 0 && MyVars.g.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 0.95f);
            } else {
                return null;
            }
        } else {
            if (MyVars.g.getSuccessorCount(n) > 0 && MyVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 1f);
            } else if (MyVars.g.getSuccessorCount(n) == 0 && MyVars.g.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 1f);
            } else if (MyVars.g.getSuccessorCount(n) > 0 && MyVars.g.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 1f);
            } else {
                return null;
            }
        }
    }

    private String setDepthNodeToolTipForSingleSelectedNode(MyNode n, int depth) {
        String tooltip = "";
        if (vc.selectedNodeNeighborNodeTypeSelector.getSelectedItem().toString().contains("S")) {
            if (vc.depthNodeNameSet.contains(n.getName())) {
                tooltip =
                        "<HTML><BODY>" +
                                "NODE: " + (n.getName().contains("x") ? MySysUtil.decodeVariable(n.getName()) : MySysUtil.decodeNodeName(n.getName())) +  "&nbsp;&nbsp;" +
                                "<BR>CONTRIBUTION: " + MyMathUtil.getCommaSeperatedNumber(((MyNode)MyVars.g.vRefs.get(vc.depthNodeNameSet.iterator().next())).getContribution()) +
                                "<BR>DEPTH: " + depth + " &nbsp;&nbsp;</BODY></HTML>";
            } else {
                tooltip =
                        "<HTML>" +
                                "<BODY>" +
                                "NODE: " + (n.getName().contains("x") ? MySysUtil.decodeVariable(n.getName()) : MySysUtil.decodeNodeName(n.getName())) + "&nbsp;&nbsp;" +
                                "<BR>CONTRIBUTION: " + MyMathUtil.getCommaSeperatedNumber(vc.depthNodeSuccessorMaps.get(vc.depthNodeNameSet.iterator().next()).get(n.getName())) +
                                "<BR>DEPTH: " + depth + " &nbsp;&nbsp;</BODY></HTML>";
            }
        } else if  (vc.selectedNodeNeighborNodeTypeSelector.getSelectedItem().toString().contains("P")) {
            if (vc.depthNodeNameSet.contains(n.getName())) {
                tooltip =
                        "<HTML><BODY>" +
                                "NODE: " + (n.getName().contains("x") ? MySysUtil.decodeVariable(n.getName()) : MySysUtil.decodeNodeName(n.getName())) + "&nbsp;&nbsp;" +
                                "<BR>CONTRIBUTION: " + MyMathUtil.getCommaSeperatedNumber(n.getNodeDepthInfo(MyVars.currentGraphDepth).getContribution()) +
                                "<BR>DEPTH: " + depth + " &nbsp;&nbsp;</BODY></HTML>";
            } else {
                tooltip =
                        "<HTML><BODY>" +
                                "NODE: " + (n.getName().contains("x") ? MySysUtil.decodeVariable(n.getName()) : MySysUtil.decodeNodeName(n.getName())) + "&nbsp;&nbsp;" +
                                "<BR>CONTRIBUTION: " + MyMathUtil.getCommaSeperatedNumber(vc.depthNodePredecessorMaps.get(vc.depthNodeNameSet.iterator().next()).get(n.getName())) +
                                "<BR>DEPTH: " + depth + " &nbsp;&nbsp;</BODY></HTML>";
            }
        } else if (n == selectedNode) {
            tooltip =
                "<HTML><BODY>" +
                "NODE: " + (n.getName().contains("x") ? MySysUtil.decodeVariable(n.getName()) : MySysUtil.decodeNodeName(n.getName())) +  "&nbsp;&nbsp;" +
                "<BR>CONTRIBUTION: " + MyMathUtil.getCommaSeperatedNumber(n.getNodeDepthInfo(depth).getContribution()) +  "&nbsp;&nbsp;</BODY></HTML>";
        }
        return tooltip;
    }

    private String setDepthNodeToolTip(MyNode n, int depth) {
        String toolTip = "<HTML>" +
            "<BODY>" +
            "NODE: " +(n.getName().contains("x") ? MySysUtil.decodeVariable(n.getName()) : MySysUtil.decodeNodeName(n.getName())) +  "&nbsp;&nbsp;" +
            "<BR>CONTRIBUTION: " + MyMathUtil.getCommaSeperatedNumber(n.getNodeDepthInfo(depth).getContribution()) +  "&nbsp;&nbsp;" +
            "<BR>IN CONTRIBUTION: " + MyMathUtil.getCommaSeperatedNumber(n.getNodeDepthInfo(depth).getInContribution()) +  "&nbsp;&nbsp;" +
            "<BR>OUT CONTRIBUTION: " + MyMathUtil.getCommaSeperatedNumber(n.getNodeDepthInfo(depth).getOutContribution()) +  "&nbsp;&nbsp;" +
            "<BR>DEPTH: " +depth +  " &nbsp;&nbsp;";
            if (MyVars.isTimeOn) {
                toolTip += "<BR>REACH TIME: " + MyMathUtil.getCommaSeperatedNumber(n.getNodeDepthInfo(depth).getReachTime()) + "&nbsp;&nbsp;" +
                "<BR>AVG. REACH TIME: " + MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(n.getNodeDepthInfo(depth).getAverageReachTime())) + "&nbsp;&nbsp;" +
                "<BR>DURATION: " + MyMathUtil.getCommaSeperatedNumber(n.getNodeDepthInfo(depth).getDuration()) + "&nbsp;&nbsp;" +
                "<BR>AVG. DURATION: " + MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(n.getNodeDepthInfo(depth).getAverageDuration())) + "&nbsp;&nbsp;";
            }
            toolTip += "<BR>PRDECESSORS: " + MySysUtil.getCommaSeperateString(n.getNodeDepthInfo(depth).getPredecessorCount()) +  "&nbsp;&nbsp;" +
            "<BR>SUCCESSORS: " + MySysUtil.getCommaSeperateString(n.getNodeDepthInfo(depth).getSuccessorCount()) +  "&nbsp;&nbsp;</BODY></HTML>";
        return toolTip;
    }

    private String setTopNodeToolTip(MyNode n) {
        String toolTip = "<HTML>" +
            "<BODY>NODE: " + (n.getName().contains("x") ? MySysUtil.decodeVariable(n.getName()) : MySysUtil.decodeNodeName(n.getName())) +
            (MyVars.isSupplementaryOn && !n.getName().contains("x") ? "<BR>RELATED VARIABLES: " + MyMathUtil.getCommaSeperatedNumber(n.getVariableStrengthMap().size()) : "") +
            (MyVars.isSupplementaryOn && !n.getName().contains("x") ? "<BR>VARIABLE STRENGTH: "  +  MyMathUtil.getCommaSeperatedNumber(n.getTotalVariableStrength()) : "") +
            "<BR>CONTRIBUTION: " + MyMathUtil.getCommaSeperatedNumber(n.getContribution()) +
            "<BR>IN-CONTRIBUTION: " + MyMathUtil.getCommaSeperatedNumber(n.getInContribution()) +
            "<BR>AVG. IN-CONTRIBUTION: " + MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(n.getAverageInContribution())) +
            "<BR>OUT CONTRIBUTION: " + MyMathUtil.getCommaSeperatedNumber(n.getOutContribution()) +
            "<BR>AVG. OUT-CONTRIBUTION: " + MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(n.getAverageOutContribution())) +
            "<BR>UNIQUE CONTRIBUTION: " + MyMathUtil.getCommaSeperatedNumber(n.getUniqueContribution()) +
            "<BR>AVG. RECCURRENCE LENGTH: " + MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(n.getAverageRecurrenceLength()))    ;
        if (MyVars.isTimeOn) {
            toolTip += "<BR>REACH TIME: " + MyMathUtil.getCommaSeperatedNumber(n.getTotalReachTime()) +
                "<BR>AVG. REACH TIME: " + MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(n.getAverageReachTime())) +
                "<BR>DURATION: " + MyMathUtil.getCommaSeperatedNumber(n.getDuration()) +
                "<BR>MAX. DURATION: " + MyMathUtil.getCommaSeperatedNumber(n.getMaximumDuration()) +
                "<BR>MIN. DURATION: " + MyMathUtil.getCommaSeperatedNumber(n.getMinimumDuration()) +
                "<BR>AVG. DURATION: " + MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat(n.getAverageDuration()));
        }

        toolTip += "<BR>OPEN NODE COUNT: " + MySysUtil.getCommaSeperateString(n.getOpenNodeCount()) +
            "<BR>END NODE COUNT: " + MySysUtil.getCommaSeperateString(n.getEndNodeCount()) +
            "<BR>PRDECESSORS: " + MySysUtil.getCommaSeperateString(n.getPredecessorCount()) +
            "<BR>SUCCESSORS: " + MySysUtil.getCommaSeperateString(n.getSuccessorCount()) +
            "<BR>D. RECURRENCE: " + MySysUtil.getCommaSeperateString(n.getDirectRecurrenceCount()) +
            "<BR>U. NODE COUNT: " + MySysUtil.getCommaSeperateString(n.getUnreachableNodeCount()) + "</BODY></HTML>";
        return toolTip;
    }

}
