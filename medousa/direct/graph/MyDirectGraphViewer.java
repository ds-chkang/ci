package medousa.direct.graph;

import medousa.direct.graph.barcharts.*;
import medousa.direct.graph.layout.MyDirectGraphFRLayout;
import medousa.direct.utils.MyDirectGraphMathUtil;
import medousa.direct.utils.MyDirectGraphSysUtil;
import medousa.direct.utils.MyDirectGraphVars;
import edu.uci.ics.jung.visualization.VisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import org.apache.commons.collections15.Transformer;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.Set;

import static java.awt.Cursor.HAND_CURSOR;

public class MyDirectGraphViewer
extends VisualizationViewer<MyDirectNode, MyDirectEdge>
implements Serializable {

    private ScalingControl viewScaler;
    private float defaultNodeSize = 20.0f;
    private final float MAX_NODE_SIZE = 80.0f;
    public final float MIN_EDGE_STROKE = 1.5f;
    private final float MAX_EDGE_STROKE = 50.0f;
    public boolean isExcludeBtnOn;
    public MyDirectNode selectedSingleNode = null;
    public Set<MyDirectNode> selectedSingleNodePredecessorSet = null;
    public Set<MyDirectNode> selectedSingleNodeSuccessorSet = null;
    public Set<MyDirectNode> multiNodes;
    public Set<MyDirectNode> multiNodePredecessorSet = null;
    public Set<MyDirectNode> multiNodeSuccessorSet = null;
    public Set<MyDirectNode> multiNodeSharedPredecessorSet = null;
    public Set<MyDirectNode> multiNodeSharedSuccessorSet = null;
    public MyDirectGraphController vc;
    public MyDirectGraphNeighborNodeValueBarChart neighborNodeValueRankBarChart;
    public MyDirectGraphEdgeValueBarChart edgeValueBarChart;
    public MyDirectGraphNodeValueBarChart nodeValueBarChart;
    public MyDirectGraphNodesByShortestDistanceBarChart nodesByShortestDistanceBarChart;
    public MyDirectGraphMultiNodeLevelNeighborNodeValueBarChart multiNodeLevelNeighborNodeValueBarChart;
    public boolean isSharedPredecessorOnly = false;
    public boolean isSharedSuccessorOnly = false;
    public boolean isPredecessorOnly = false;
    public boolean isSuccessorOnly = false;
    public MyDirectGraphViewerMouseListener directGraphViewerMouseListener;
    public MyDirectGraphMouseMotionListener graphMouseMotionListener;
    public MyDirectGraphFRLayout layout;
    public boolean isClustered;

    public MyDirectGraphViewer(VisualizationModel<MyDirectNode, MyDirectEdge> vm) {
        super(vm);
        try {
            this.setPreferredSize(new Dimension(3500, 4000));
            DefaultModalGraphMouse graphMouse = new DefaultModalGraphMouse();
            graphMouse.setMode(DefaultModalGraphMouse.Mode.PICKING);
            this.setGraphMouse(graphMouse);
            this.graphMouseMotionListener = new MyDirectGraphMouseMotionListener();
            this.addGraphMouseListener(new MyDirectGraphMouseListener(this.getPickedVertexState()));
            this.addMouseMotionListener(this.graphMouseMotionListener);
            this.getRenderContext().setLabelOffset(25);
            Cursor handCursor = new Cursor(HAND_CURSOR);
            this.setCursor(handCursor);
            this.setBackground(Color.WHITE);
            this.setDoubleBuffered(true);
            this.setLayout(null);
            this.directGraphViewerMouseListener = new MyDirectGraphViewerMouseListener(this);
            this.addMouseListener(directGraphViewerMouseListener);
            this.addComponentListener(new ComponentAdapter() {
                @Override public void componentResized(ComponentEvent e) {
                    super.componentResized(e);
                    if (selectedSingleNode != null) {
                        vc.removeBarCharts();
                        if (vc.nodeLabelComboBoxMenu.getSelectedIndex() == 0) {
                            neighborNodeValueRankBarChart = new MyDirectGraphNeighborNodeValueBarChart();
                            add(neighborNodeValueRankBarChart);
                        } else {

                        }
                        revalidate();
                        repaint();
                    } else if (multiNodes != null && multiNodes.size() > 0) {
                        vc.removeBarCharts();
                        if (vc.nodeLabelComboBoxMenu.getSelectedIndex() == 0) {
                            multiNodeLevelNeighborNodeValueBarChart = new MyDirectGraphMultiNodeLevelNeighborNodeValueBarChart();
                            add(multiNodeLevelNeighborNodeValueBarChart);
                        } else {

                        }
                        revalidate();
                        repaint();
                    } else if (MyDirectGraphVars.app.getDirectGraphDashBoard().topLevelTabbedPane.getSelectedIndex() == 3) {
                        if (vc.nodeLabelComboBoxMenu.getSelectedIndex() > 0) {vc.nodeLabelComboBoxMenu.setSelectedIndex(0);}
                        if (MyDirectGraphVars.app.getDirectGraphDashBoard().distanceMenu.getItemCount() > 1 && MyDirectGraphVars.app.getDirectGraphDashBoard().distanceMenu.getSelectedIndex() == 0) {
                            vc.removeBarCharts();
                            nodesByShortestDistanceBarChart = new MyDirectGraphNodesByShortestDistanceBarChart(MyDirectGraphVars.app.getDirectGraphDashBoard().visitedNodes);
                            add(nodesByShortestDistanceBarChart);
                            revalidate();
                            repaint();
                        } else {
                            vc.removeBarCharts();
                            nodeValueBarChart = new MyDirectGraphNodeValueBarChart();
                            add(nodeValueBarChart);
                            revalidate();
                            repaint();
                        }
                    } else {
                        vc.removeBarCharts();
                        nodeValueBarChart = new MyDirectGraphNodeValueBarChart();
                        add(nodeValueBarChart);
                        revalidate();
                        repaint();
                    }

                    if (vc.edgeValueComboBoxMenu.getSelectedItem().toString().length() > 2) {
                        edgeValueBarChart = new MyDirectGraphEdgeValueBarChart();
                        edgeValueBarChart.setEdgeValueBarChart();
                        add(edgeValueBarChart);
                        revalidate();
                        repaint();
                    }

                }
            });

            this.getRenderContext().setArrowDrawPaintTransformer(new Transformer<MyDirectEdge, Paint>() {
                @Override
                public Paint transform(MyDirectEdge e) {
                    return new Color(0, 0, 0, 0f);
                }
            });
            this.getRenderContext().setArrowFillPaintTransformer(new Transformer<MyDirectEdge, Paint>() {
                @Override
                public Paint transform(MyDirectEdge e) {
                    return new Color(0, 0, 0, 0f);
                }
            });
            this.setVertexToolTipTransformer(this.defaultToolTipper);
            this.getRenderContext().setVertexShapeTransformer(this.nodeSizer);
            this.getRenderContext().setVertexFillPaintTransformer(this.unWeightedNodeColor);
            this.getRenderContext().setEdgeShapeTransformer(new EdgeShape.QuadCurve<MyDirectNode, MyDirectEdge>());
            this.getRenderContext().setEdgeStrokeTransformer(this.unWeightedEdgeStroker);
            this.getRenderContext().setEdgeDrawPaintTransformer(this.unWeightedEdgeColor);
            this.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.S);
            this.getRenderContext().setVertexLabelTransformer(this.nodeLabeller);
            this.getRenderContext().setVertexStrokeTransformer(this.nodeStroker);
            this.getRenderContext().setVertexFontTransformer(new Transformer<MyDirectNode, Font>() {
                @Override public Font transform(MyDirectNode node) {
                    return new Font("Noto Sans", Font.PLAIN, 0);
                }
            });
            this.getRenderContext().setEdgeFontTransformer(new Transformer<MyDirectEdge, Font>() {
                @Override public Font transform(MyDirectEdge e) {
                    return new Font("Noto Sans", Font.PLAIN, 0);
                }
            });
            this.getRenderContext().setEdgeLabelTransformer(new Transformer<MyDirectEdge, String>() {
                @Override public String transform(MyDirectEdge e) {
                    String edgeLabel = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(e.getCurrentValue()));
                    if (edgeLabel.endsWith(".00")) {
                        return edgeLabel.substring(0, edgeLabel.indexOf(".00"));
                    } else {
                        return MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(e.getCurrentValue()));
                    }
                }
            });

            this.getRenderContext().setVertexDrawPaintTransformer(new Transformer<MyDirectNode, Paint>() {
                @Override public Paint transform(MyDirectNode n) {
                    if (MyDirectGraphVars.getDirectGraphViewer().vc.mouseHoverCheckBox.isSelected() && graphMouseMotionListener.neighborNodeSet != null) {
                        if (!graphMouseMotionListener.neighborNodeSet.contains(n)) {
                            return new Color(0, 0, 0, 0f);
                        } else {
                            return Color.DARK_GRAY;
                        }
                    } else if (selectedSingleNode != null && selectedSingleNode == n) {
                        return Color.ORANGE;
                    } else if (multiNodes != null && multiNodes.contains(n)) {
                        return Color.ORANGE;
                    } else {
                        return Color.DARK_GRAY;
                    }
                }
            });

            this.getRenderContext().setEdgeArrowStrokeTransformer(new Transformer<MyDirectEdge, Stroke>() {
                @Override public Stroke transform(MyDirectEdge myDirectEdge) {
                    return new BasicStroke(5f);
                }
            });

            this.nodeValueBarChart = new MyDirectGraphNodeValueBarChart();
            this.add(this.nodeValueBarChart);
        } catch (Exception ex) {}
    }

    public MyDirectGraphViewer create() {
        this.scale();
        return this;
    }

    public Transformer<MyDirectNode, Paint> weightedNodeColor = new Transformer<MyDirectNode, Paint>() {
        @Override public Paint transform(MyDirectNode n) {
            return setWeightedNodeColor(n);
        }
    };

    public Transformer<MyDirectNode, String> nodeLabeller = new Transformer<MyDirectNode, String>() {
        @Override public String transform(MyDirectNode n) {
            return "<html><body>" + n.getCurrentLabel() + "</body></html>";
        }
    };

    private Transformer<MyDirectNode, Stroke> nodeStroker = new Transformer<MyDirectNode, Stroke>() {
        @Override public Stroke transform(MyDirectNode n) {
            if (MyDirectGraphVars.getDirectGraphViewer().vc.mouseHoverCheckBox.isSelected() && graphMouseMotionListener.neighborNodeSet != null) {
                if (!graphMouseMotionListener.neighborNodeSet.contains(n)) {
                    return new BasicStroke(3.5f);
                } else {
                    return new BasicStroke(3.5f);
                }
            } else if (selectedSingleNode != null && n == selectedSingleNode) {
                return new BasicStroke(28f);
            } else if (multiNodes != null && multiNodes.contains(n)) {
                return new BasicStroke(28f);
            } else {
                return new BasicStroke(3.5f);
            }
        }
    };

    private void scale() {
        this.viewScaler = new CrossoverScalingControl();
        double amount = -1.0;
        if (MyDirectGraphVars.app.getWidth() > 1800) {
            viewScaler.scale(this, amount > 0 ? 2.0f : 1 / 7.6f, new Point2D.Double(310, 100));
        } else {
            viewScaler.scale(this, amount > 0 ? 2.0f : 1 / 9.1f, new Point2D.Double(250, 60));
        }
    }

    private Transformer<MyDirectNode, Shape> nodeSizer = new Transformer<MyDirectNode, Shape>() {@Override
        public Shape transform(MyDirectNode n) {
            try {
                if (n.getCurrentValue() == 0) {
                    return new Ellipse2D.Double(0, 0, 0, 0);
                } else if (isPredecessorOnly) {
                    if (selectedSingleNode != null && n == selectedSingleNode) {
                        return setNodeSizeByNodeValue(n.getCurrentValue()/ MyDirectGraphVars.directGraph.getMaxNodeValue());
                    } else if (multiNodes != null && multiNodes.contains(n)) {
                        return setNodeSizeByNodeValue(n.getCurrentValue()/ MyDirectGraphVars.directGraph.getMaxNodeValue());
                    } else if (multiNodes != null && multiNodePredecessorSet.contains(n)) {
                        return setNodeSizeByNodeValue(n.getCurrentValue() / MyDirectGraphVars.directGraph.getMaxNodeValue());
                    } else if (selectedSingleNodePredecessorSet != null && selectedSingleNodePredecessorSet.contains(n)) {
                        return setNodeSizeByNodeValue(n.getCurrentValue()/ MyDirectGraphVars.directGraph.getMaxNodeValue());
                    } else {
                        return new Ellipse2D.Double(0, 0, 0, 0);
                    }
                } else if (isSuccessorOnly) {
                    if (selectedSingleNode != null && selectedSingleNode == n) {
                        return setNodeSizeByNodeValue(n.getCurrentValue()/ MyDirectGraphVars.directGraph.getMaxNodeValue());
                    } else if (multiNodes != null && multiNodes.contains(n)) {
                        return setNodeSizeByNodeValue(n.getCurrentValue()/ MyDirectGraphVars.directGraph.getMaxNodeValue());
                    } else if (multiNodes != null && multiNodeSharedSuccessorSet.contains(n)) {
                        return setNodeSizeByNodeValue(n.getCurrentValue() / MyDirectGraphVars.directGraph.getMaxNodeValue());
                    } else if (selectedSingleNodeSuccessorSet.contains(n)) {
                        return setNodeSizeByNodeValue(n.getCurrentValue()/ MyDirectGraphVars.directGraph.getMaxNodeValue());
                    } else if (selectedSingleNodeSuccessorSet != null && selectedSingleNodeSuccessorSet.contains(n)) {
                        return setNodeSizeByNodeValue(n.getCurrentValue()/ MyDirectGraphVars.directGraph.getMaxNodeValue());
                    } else {
                        return new Ellipse2D.Double(0, 0, 0, 0);
                    }
                } else if (isSharedPredecessorOnly) {
                    if (multiNodes != null && multiNodes.contains(n)) {
                        return setNodeSizeByNodeValue(n.getCurrentValue()/ MyDirectGraphVars.directGraph.getMaxNodeValue());
                    } else if (multiNodeSharedPredecessorSet != null && multiNodeSharedPredecessorSet.contains(n)) {
                        return setNodeSizeByNodeValue(n.getCurrentValue()/ MyDirectGraphVars.directGraph.getMaxNodeValue());
                    } else {
                        return new Ellipse2D.Double(0, 0, 0, 0);
                    }
                } else if (isSharedSuccessorOnly) {
                    if (multiNodes != null && multiNodes.contains(n)) {
                        return setNodeSizeByNodeValue(n.getCurrentValue()/ MyDirectGraphVars.directGraph.getMaxNodeValue());
                    } else if (multiNodeSharedSuccessorSet != null && multiNodeSharedSuccessorSet.contains(n)) {
                        return setNodeSizeByNodeValue(n.getCurrentValue()/ MyDirectGraphVars.directGraph.getMaxNodeValue());
                    } else {
                        return new Ellipse2D.Double(0, 0, 0, 0);
                    }
                } else if (selectedSingleNode != null) {
                    if (n == selectedSingleNode) {
                        return setNodeSizeByNodeValue(n.getCurrentValue()/ MyDirectGraphVars.directGraph.getMaxNodeValue());
                    } else if (selectedSingleNodePredecessorSet != null && selectedSingleNodePredecessorSet.contains(n)) {
                        return setNodeSizeByNodeValue(n.getCurrentValue()/ MyDirectGraphVars.directGraph.getMaxNodeValue());
                    } else if (selectedSingleNodePredecessorSet != null && selectedSingleNodeSuccessorSet.contains(n)) {
                        return setNodeSizeByNodeValue(n.getCurrentValue()/ MyDirectGraphVars.directGraph.getMaxNodeValue());
                    } else {
                        return new Ellipse2D.Double(0, 0, 0, 0);
                    }
                } else if (multiNodes != null) {
                    if (multiNodes.contains(n)) {
                        return setNodeSizeByNodeValue(n.getCurrentValue()/ MyDirectGraphVars.directGraph.getMaxNodeValue());
                    } else if (multiNodePredecessorSet != null && multiNodePredecessorSet.contains(n)) {
                        return setNodeSizeByNodeValue(n.getCurrentValue()/ MyDirectGraphVars.directGraph.getMaxNodeValue());
                    } else if (multiNodeSuccessorSet != null && multiNodeSuccessorSet.contains(n)) {
                        return setNodeSizeByNodeValue(n.getCurrentValue()/ MyDirectGraphVars.directGraph.getMaxNodeValue());
                    } else {
                        return new Ellipse2D.Double(0, 0, 0, 0);
                    }
                } else {
                    return setNodeSizeByNodeValue(n.getCurrentValue()/ MyDirectGraphVars.directGraph.getMaxNodeValue());
                }
            } catch (Exception ex) {
                return null;
            }
        }
    };

    public Transformer<MyDirectNode, String> defaultToolTipper = new Transformer<MyDirectNode, String>() {
        @Override public String transform(MyDirectNode n) {
            return n.getCurrentLabel();
        }
    };

    public Transformer<MyDirectEdge, Stroke> weightedEdgeStroker = new Transformer<MyDirectEdge, Stroke>() {
        @Override public Stroke transform(MyDirectEdge e) {
            if (isPredecessorOnly) {
                if (selectedSingleNode != null && e.getDest() != selectedSingleNode) {
                    return new BasicStroke(0f);
                } else if (multiNodes != null && !multiNodes.contains(e.getDest())) {
                    return new BasicStroke(0f);
                } else if (e.getCurrentValue() <= 0) {
                    return new BasicStroke(0f);
                } else {
                    float edgeStrokeWeight = e.getCurrentValue() / MyDirectGraphVars.directGraph.getMaxEdgeValue();
                    return new BasicStroke(edgeStrokeWeight * MAX_EDGE_STROKE);
                }
            } else if (isSuccessorOnly) {
                if (selectedSingleNode != null && e.getSource() != selectedSingleNode) {
                    return new BasicStroke(0f);
                } else if (multiNodes != null && !multiNodes.contains(e.getSource())) {
                    return new BasicStroke(0f);
                } else if (e.getCurrentValue() <= 0) {
                    return new BasicStroke(0f);
                } else {
                    float edgeStrokeWeight = e.getCurrentValue() / MyDirectGraphVars.directGraph.getMaxEdgeValue();
                    return new BasicStroke(edgeStrokeWeight * MAX_EDGE_STROKE);
                }
            } else if (selectedSingleNode != null && e.getDest() != selectedSingleNode && e.getSource() != selectedSingleNode) {
                return new BasicStroke(0f);
            } else if (e.getCurrentValue() == 0) {
                return new BasicStroke(0f);
            } else if (multiNodes != null && (!multiNodes.contains(e.getSource()) && !multiNodes.contains(e.getDest()))) {
                return new BasicStroke(0.0f);
            } else if (e.getCurrentValue() == -1) {
                return new BasicStroke(1.0f);
            } else {
                float edgeStrokeWeight = e.getCurrentValue() / MyDirectGraphVars.directGraph.getMaxEdgeValue();
                return new BasicStroke(edgeStrokeWeight * MAX_EDGE_STROKE);
            }
        }
    };

    private Transformer<MyDirectEdge, Stroke> unWeightedEdgeStroker = new Transformer<MyDirectEdge, Stroke>() {
        @Override public Stroke transform(MyDirectEdge e) {
            if (isPredecessorOnly) {
                if (selectedSingleNode != null && e.getDest() != selectedSingleNode) {
                    return new BasicStroke(0f);
                } else if (multiNodes != null && !multiNodes.contains(e.getDest())) {
                    return new BasicStroke(0f);
                } else if (e.getCurrentValue() <= 0) {
                    return new BasicStroke(0f);
                } else {
                    return new BasicStroke(MIN_EDGE_STROKE);
                }
            } else if (isSuccessorOnly) {
                if (selectedSingleNode != null && e.getSource() != selectedSingleNode) {
                    return new BasicStroke(0f);
                } else if (multiNodes != null && !multiNodes.contains(e.getSource())) {
                    return new BasicStroke(0f);
                } else if (e.getCurrentValue() <= 0) {
                    return new BasicStroke(0f);
                } else {
                    return new BasicStroke(MIN_EDGE_STROKE);
                }
            } else if (selectedSingleNode != null && e.getDest() != selectedSingleNode && e.getSource() != selectedSingleNode) {
                return new BasicStroke(0f);
            } else if (multiNodes != null && !multiNodes.contains(e.getSource()) && !multiNodes.contains(e.getDest())) {
                return new BasicStroke(0f);
            } else if (e.getCurrentValue() == 0) {
                return new BasicStroke(0f);
            } else {
                return new BasicStroke(MIN_EDGE_STROKE);
            }
    }};

    public Transformer<MyDirectEdge, Paint> weightedEdgeColor = new Transformer<MyDirectEdge, Paint>() {
        @Override public Paint transform(MyDirectEdge e) {
            if (vc.mouseHoverCheckBox.isSelected() && graphMouseMotionListener.neighborNodeSet != null) {
                if (!graphMouseMotionListener.neighborNodeSet.contains(e.getSource()) && !graphMouseMotionListener.neighborNodeSet.contains(e.getDest())) {
                    return new Color(0, 0, 0, 0f);
                } else {
                    return Color.GRAY;
                }
            } else if (isPredecessorOnly) {
                if (selectedSingleNode != null && e.getDest() != selectedSingleNode) {
                    return new Color(0, 0, 0, 0.0f);
                } else if (multiNodes != null && !multiNodes.contains(e.getDest())) {
                    return new Color(0, 0, 0, 0.0f);
                } else {
                    return Color.RED;
                }
            } else if (isSuccessorOnly) {
                if (selectedSingleNode != null && e.getSource() != selectedSingleNode) {
                    return new Color(0, 0, 0, 0.0f);
                } else if (multiNodes != null && !multiNodes.contains(e.getSource())) {
                    return new Color(0, 0, 0, 0.0f);
                } else {
                    return Color.BLUE;
                }
            } else if (selectedSingleNode != null && e.getSource() != selectedSingleNode && e.getDest() != selectedSingleNode) {
                return new Color(0f, 0f, 0f, 0f);
            } else if (multiNodes != null && !multiNodes.contains(e.getSource()) && !multiNodes.contains(e.getDest())) {
               return new Color(0f, 0f, 0f, 0f);
            }  else if (e.getCurrentValue() == 0) {
                return new Color(0f, 0f, 0f, 0f);
            } else if (e.getSource().getCurrentValue() == 0 || e.getDest().getCurrentValue() == 0) {
                return new Color(0, 0, 0, 0.0f);
            } else {
                return Color.GRAY;
            }
        }
    };

    public Transformer<MyDirectEdge, Paint> unWeightedEdgeColor = new Transformer<MyDirectEdge, Paint>() {
        @Override public Paint transform(MyDirectEdge e) {
            if (vc.mouseHoverCheckBox.isSelected() && graphMouseMotionListener.neighborNodeSet != null) {
                if ((graphMouseMotionListener.neighborNodeSet.contains(e.getSource()) && graphMouseMotionListener.neighborNodeSet.contains(e.getDest()))) {
                    return Color.GRAY;
                } else {
                    return new Color(0,0,0,0f);
                }
            } else if (e.getSource().getCurrentValue() == 0 || e.getDest().getCurrentValue() == 0) {
                return new Color(0, 0, 0, 0);
            } else if (isPredecessorOnly) {
                if (selectedSingleNode != null && e.getDest() != selectedSingleNode) {
                    return new Color(0, 0, 0, 0.0f);
                } else if (multiNodes != null && !multiNodes.contains(e.getDest())) {
                    return new Color(0, 0, 0, 0.0f);
                } else {
                    return Color.RED;
                }
            } else if (isSuccessorOnly) {
                if (selectedSingleNode != null && e.getSource() != selectedSingleNode) {
                    return new Color(0, 0, 0, 0.0f);
                } else if (multiNodes != null && !multiNodes.contains(e.getSource())) {
                    return new Color(0, 0, 0, 0.0f);
                } else {
                    return Color.BLUE;
                }
            } else if (selectedSingleNode != null && e.getDest() != selectedSingleNode && e.getSource() != selectedSingleNode) {
                return new Color(0, 0, 0, 0.0f);
            } else if (multiNodes != null && !multiNodes.contains(e.getSource()) && !multiNodes.contains(e.getDest())) {
                return new Color(0f, 0f, 0f, 0f);
            } else if (e.getCurrentValue() == 0) {
                return new Color(0f, 0f, 0f, 0f);
            } else if (e.getSource().getCurrentValue() == 0 && e.getDest().getCurrentValue() == 0) {
                return new Color(0, 0, 0, 0.0f);
            } else if (selectedSingleNode != null && e.getSource() == selectedSingleNode && e.getDest() == selectedSingleNode) {
                return Color.ORANGE;
            } else if (selectedSingleNode != null && e.getDest() == selectedSingleNode) {
                return Color.RED;
            } else if (selectedSingleNode != null && e.getSource() == selectedSingleNode) {
                return Color.BLUE;
            } else if (multiNodes != null && multiNodes.contains(e.getSource()) && multiNodes.contains(e.getDest())) {
                return Color.ORANGE;
            } else if (multiNodes != null && multiNodes.contains(e.getSource())) {
                return Color.BLUE;
            } else if (multiNodes != null && multiNodes.contains(e.getDest())) {
                return Color.RED;
            } else {
                return Color.GRAY;
            }
        }
    };

    public Transformer<MyDirectNode, Paint> unWeightedNodeColor = new Transformer<MyDirectNode, Paint>() {@Override
        public Paint transform(MyDirectNode n) {
        if (n.clusteringColor != null) {
            return n.clusteringColor;
        } else if (vc.mouseHoverCheckBox.isSelected() && graphMouseMotionListener.neighborNodeSet != null) {
            if (!graphMouseMotionListener.neighborNodeSet.contains(n)) {
                return new Color(0, 0, 0, 0);
            } else {
                if (n.getInContribution() == 0 && n.getOutContribution() == 0) {
                    return new Color(0, 0, 0, 1f);
                } else if (n.getInContribution() > 0 && n.getOutContribution() > 0) {
                    return new Color(0.0f, 0f, 1.0f, 0.7f);
                } else if (n.getInContribution() > 0 && n.getOutContribution() == 0) {
                    return new Color(0.0f, 1.0f, 0f, 1.0f);
                } else if (n.getInContribution() == 0 && n.getOutContribution() > 0) {
                    return new Color(1.0f, 0.0f, 0.0f, 0.7f);
                } else {
                    return Color.BLACK;
                }
            }
        } else if (multiNodes != null && (multiNodeSharedSuccessorSet.contains(n) || multiNodeSharedPredecessorSet.contains(n))) {
            return Color.ORANGE;
        } else if (selectedSingleNode != null && n == selectedSingleNode) {
            if (n.getInContribution() > 0 && n.getOutContribution() > 0) {
                return new Color(0.0f, 0f, 1.0f, 0.7f);
            } else if (n.getInContribution() > 0 && n.getOutContribution() == 0) {
                return new Color(0.0f, 1.0f, 0f, 1.0f);
            } else if (n.getInContribution() == 0 && n.getOutContribution() > 0) {
                return new Color(1.0f, 0.0f, 0.0f, 0.7f);
            } else {
                return Color.BLACK;
            }
        } else if (n.getInContribution() > 0 && n.getOutContribution() > 0) {
            return new Color(0.0f, 0f, 1.0f, 0.7f);
        } else if (n.getInContribution() > 0 && n.getOutContribution() == 0) {
            return new Color(0.0f, 1.0f, 0f, 1.0f);
        } else if (n.getInContribution() == 0 && n.getOutContribution() > 0) {
            return new Color(1.0f, 0.0f, 0.0f, 0.7f);
        } else {
            return Color.BLACK;
        }
    }};

    private Ellipse2D.Double setNodeSizeByNodeValue(double nodeWeight) {
        if (nodeWeight <= 0.05) {
            float nodeSize = (this.defaultNodeSize)/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize * 2, nodeSize * 2);
        } else if (nodeWeight > 0.05 && nodeWeight <= 0.1) {
            float nodeSize = (this.defaultNodeSize + 3) / 2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize * 2, nodeSize * 2);
        } else if (nodeWeight > 0.05 && nodeWeight <= 0.125) {
            float nodeSize = (this.defaultNodeSize + 9) / 2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize * 2, nodeSize * 2);
        } else if (nodeWeight > 0.1 && nodeWeight <= 0.15) {
            float nodeSize = (this.defaultNodeSize + 16) / 2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize * 2, nodeSize * 2);
        } else if (nodeWeight > 0.1 && nodeWeight <= 0.175) {
            float nodeSize = (this.defaultNodeSize + 20) / 2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize * 2, nodeSize * 2);
        } else if (nodeWeight > 0.15 && nodeWeight <= 0.2) {
            float nodeSize = (this.defaultNodeSize + 28) / 2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize * 2, nodeSize * 2);
        } else if (nodeWeight > 0.2 && nodeWeight <= 0.225) {
            float nodeSize = (this.defaultNodeSize + 36) / 2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize * 2, nodeSize * 2);
        } else if (nodeWeight > 0.2 && nodeWeight <= 0.25) {
            float nodeSize = (this.defaultNodeSize + 44) / 2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize * 2, nodeSize * 2);
        } else if (nodeWeight > 0.2 && nodeWeight <= 0.275) {
            float nodeSize = (this.defaultNodeSize + 52) / 2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize * 2, nodeSize * 2);
        } else if (nodeWeight > 0.25 && nodeWeight <= 0.3) {
            float nodeSize = (this.defaultNodeSize + 60) / 2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize * 2, nodeSize * 2);
        } else if (nodeWeight > 0.3 && nodeWeight <= 0.325) {
            float nodeSize = (this.defaultNodeSize + 68) / 2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize * 2, nodeSize * 2);
        } else if (nodeWeight > 0.3 && nodeWeight <= 0.35) {
            float nodeSize = (this.defaultNodeSize + 76) / 2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize * 2, nodeSize * 2);
        } else if (nodeWeight > 0.3 && nodeWeight <= 0.375) {
            float nodeSize = (this.defaultNodeSize + 84) / 2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize * 2, nodeSize * 2);
        } else if (nodeWeight > 0.35 && nodeWeight <= 0.40) {
            float nodeSize = (this.defaultNodeSize + 94) / 2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize * 2, nodeSize * 2);
        } else if (nodeWeight > 0.3 && nodeWeight <= 0.425) {
            float nodeSize = (this.defaultNodeSize + 104) / 2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize * 2, nodeSize * 2);
        } else if (nodeWeight > 0.3 && nodeWeight <= 0.45) {
            float nodeSize = (this.defaultNodeSize + 114) / 2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize * 2, nodeSize * 2);
        } else if (nodeWeight > 0.3 && nodeWeight <= 0.475) {
            float nodeSize = (this.defaultNodeSize + 124) / 2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize * 2, nodeSize * 2);
        } else if (nodeWeight > 0.45 && nodeWeight <= 0.50) {
            float nodeSize = (this.defaultNodeSize + 134) / 2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize * 2, nodeSize * 2);
        } else if (nodeWeight > 0.5 && nodeWeight <= 0.525) {
            float nodeSize = (this.defaultNodeSize + 144) / 2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize * 2, nodeSize * 2);
        } else if (nodeWeight > 0.5 && nodeWeight <= 0.55) {
            float nodeSize = (this.defaultNodeSize + 154) / 2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize * 2, nodeSize * 2);
        } else if (nodeWeight > 0.5 && nodeWeight <= 0.575) {
            float nodeSize = (this.defaultNodeSize + 164) / 2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize * 2, nodeSize * 2);
        } else if (nodeWeight > 0.55 && nodeWeight <= 0.6) {
            float nodeSize = (this.defaultNodeSize + 174) / 2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize * 2, nodeSize * 2);
        } else if (nodeWeight > 0.6 && nodeWeight <= 0.625) {
            float nodeSize = (this.defaultNodeSize + 184) / 2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize * 2, nodeSize * 2);
        } else if (nodeWeight > 0.6 && nodeWeight <= 0.65) {
            float nodeSize = (this.defaultNodeSize + 194) / 2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize * 2, nodeSize * 2);
        } else if (nodeWeight > 0.6 && nodeWeight <= 0.675) {
            float nodeSize = (this.defaultNodeSize + 200) / 2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize * 2, nodeSize * 2);
        } else if (nodeWeight > 0.65 && nodeWeight <= 0.7) {
            float nodeSize = (this.defaultNodeSize + 214) / 2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize * 2, nodeSize * 2);
        } else if (nodeWeight > 0.7 && nodeWeight <= 0.725) {
            float nodeSize = (this.defaultNodeSize + 228) / 2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize * 2, nodeSize * 2);
        } else if (nodeWeight > 0.7 && nodeWeight <= 0.75) {
            float nodeSize = (this.defaultNodeSize + 242) / 2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize * 2, nodeSize * 2);
        } else if (nodeWeight > 0.7 && nodeWeight <= 0.775) {
            float nodeSize = (this.defaultNodeSize + 246) / 2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize * 2, nodeSize * 2);
        } else if (nodeWeight > 0.75 && nodeWeight <= 0.8) {
            float nodeSize = (this.defaultNodeSize + 260) / 2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize * 2, nodeSize * 2);
        } else if (nodeWeight > 0.8 && nodeWeight <= 0.825) {
            float nodeSize = (this.defaultNodeSize + 284) / 2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize * 2, nodeSize * 2);
        } else if (nodeWeight > 0.8 && nodeWeight <= 0.85) {
            float nodeSize = (this.defaultNodeSize + 304) / 2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize * 2, nodeSize * 2);
        } else if (nodeWeight > 0.8 && nodeWeight <= 0.875) {
            float nodeSize = (this.defaultNodeSize + 324) / 2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize * 2, nodeSize * 2);
        } else if (nodeWeight > 0.85 && nodeWeight <= 0.9) {
            float nodeSize = (this.defaultNodeSize + 344) / 2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize * 2, nodeSize * 2);
        } else if (nodeWeight > 0.9 && nodeWeight <= 0.925) {
            float nodeSize = (this.defaultNodeSize + 364) / 2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize * 2, nodeSize * 2);
        } else if (nodeWeight > 0.9 && nodeWeight <= 0.95) {
            float nodeSize = (this.defaultNodeSize + 384) / 2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize * 2, nodeSize * 2);
        } else if (nodeWeight > 0.9 && nodeWeight <= 0.975) {
            float nodeSize = (this.defaultNodeSize + 404) / 2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize * 2, nodeSize * 2);
        } else {
            float nodeSize = (this.defaultNodeSize + 424) / 2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize * 2, nodeSize * 2);
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

    private Paint setWeightedNodeColor(MyDirectNode n) {
        float nodeColorWeight = n.getCurrentValue()/ MyDirectGraphVars.directGraph.getMaxNodeValue();
        if (nodeColorWeight <= 0.05) {
            if (MyDirectGraphVars.directGraph.getSuccessorCount(n) > 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 0.05f);
            } else if (MyDirectGraphVars.directGraph.getSuccessorCount(n) == 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 0.05f);
            } else if (MyDirectGraphVars.directGraph.getSuccessorCount(n) > 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 0.05f);
            } else {
                return null;
            }
        } else if (nodeColorWeight > 0.05 && nodeColorWeight <= 0.1) {
            if (MyDirectGraphVars.directGraph.getSuccessorCount(n) > 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 0.1f);
            } else if (MyDirectGraphVars.directGraph.getSuccessorCount(n) == 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 0.1f);
            } else if (MyDirectGraphVars.directGraph.getSuccessorCount(n) > 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 0.1f);
            } else {
                return null;
            }
        } else if (nodeColorWeight > 0.1 && nodeColorWeight <= 0.15) {
            if (MyDirectGraphVars.directGraph.getSuccessorCount(n) > 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 0.15f);
            } else if (MyDirectGraphVars.directGraph.getSuccessorCount(n) == 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 0.15f);
            } else if (MyDirectGraphVars.directGraph.getSuccessorCount(n) > 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 0.15f);
            } else {
                return null;
            }
        } else if (nodeColorWeight > 0.15 && nodeColorWeight <= 0.2) {
            if (MyDirectGraphVars.directGraph.getSuccessorCount(n) > 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 0.2f);
            } else if (MyDirectGraphVars.directGraph.getSuccessorCount(n) == 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 0.2f);
            } else if (MyDirectGraphVars.directGraph.getSuccessorCount(n) > 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 0.2f);
            } else {
                return null;
            }
        } else if (nodeColorWeight > 0.2 && nodeColorWeight <= 0.25) {
            if (MyDirectGraphVars.directGraph.getSuccessorCount(n) > 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 0.25f);
            } else if (MyDirectGraphVars.directGraph.getSuccessorCount(n) == 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 0.25f);
            } else if (MyDirectGraphVars.directGraph.getSuccessorCount(n) > 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 0.25f);
            } else {
                return null;
            }
        } else if (nodeColorWeight > 0.25 && nodeColorWeight <= 0.3) {
            if (MyDirectGraphVars.directGraph.getSuccessorCount(n) > 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 0.3f);
            } else if (MyDirectGraphVars.directGraph.getSuccessorCount(n) == 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 0.3f);
            } else if (MyDirectGraphVars.directGraph.getSuccessorCount(n) > 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 0.3f);
            } else {
                return null;
            }
        } else if (nodeColorWeight > 0.3 && nodeColorWeight <= 0.35) {
            if (MyDirectGraphVars.directGraph.getSuccessorCount(n) > 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 0.35f);
            } else if (MyDirectGraphVars.directGraph.getSuccessorCount(n) == 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 0.35f);
            } else if (MyDirectGraphVars.directGraph.getSuccessorCount(n) > 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 0.35f);
            } else {
                return null;
            }
        } else if (nodeColorWeight > 0.35 && nodeColorWeight <= 0.4) {
            if (MyDirectGraphVars.directGraph.getSuccessorCount(n) > 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 0.4f);
            } else if (MyDirectGraphVars.directGraph.getSuccessorCount(n) == 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 0.4f);
            } else if (MyDirectGraphVars.directGraph.getSuccessorCount(n) > 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 0.4f);
            } else {
                return null;
            }
        } else if (nodeColorWeight > 0.4 && nodeColorWeight <= 0.45) {
            if (MyDirectGraphVars.directGraph.getSuccessorCount(n) > 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 0.45f);
            } else if (MyDirectGraphVars.directGraph.getSuccessorCount(n) == 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 0.45f);
            } else if (MyDirectGraphVars.directGraph.getSuccessorCount(n) > 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 0.45f);
            } else {
                return null;
            }
        } else if (nodeColorWeight > 0.45 && nodeColorWeight <= 0.5) {
            if (MyDirectGraphVars.directGraph.getSuccessorCount(n) > 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 0.5f);
            } else if (MyDirectGraphVars.directGraph.getSuccessorCount(n) == 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 0.5f);
            } else if (MyDirectGraphVars.directGraph.getSuccessorCount(n) > 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 0.5f);
            } else {
                return null;
            }
        } else if (nodeColorWeight > 0.5 && nodeColorWeight <= 0.55) {
            if (MyDirectGraphVars.directGraph.getSuccessorCount(n) > 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 0.55f);
            } else if (MyDirectGraphVars.directGraph.getSuccessorCount(n) == 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 0.55f);
            } else if (MyDirectGraphVars.directGraph.getSuccessorCount(n) > 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 0.55f);
            } else {
                return null;
            }
        } else if (nodeColorWeight > 0.55 && nodeColorWeight <= 0.6) {
            if (MyDirectGraphVars.directGraph.getSuccessorCount(n) > 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 0.6f);
            } else if (MyDirectGraphVars.directGraph.getSuccessorCount(n) == 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 0.6f);
            } else if (MyDirectGraphVars.directGraph.getSuccessorCount(n) > 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 0.6f);
            } else {
                return null;
            }
        } else if (nodeColorWeight > 0.6 && nodeColorWeight <= 0.65) {
            if (MyDirectGraphVars.directGraph.getSuccessorCount(n) > 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 0.65f);
            } else if (MyDirectGraphVars.directGraph.getSuccessorCount(n) == 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 0.65f);
            } else if (MyDirectGraphVars.directGraph.getSuccessorCount(n) > 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 0.65f);
            } else {
                return null;
            }
        } else if (nodeColorWeight > 0.65 && nodeColorWeight <= 0.7) {
            if (MyDirectGraphVars.directGraph.getSuccessorCount(n) > 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 0.7f);
            } else if (MyDirectGraphVars.directGraph.getSuccessorCount(n) == 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 0.7f);
            } else if (MyDirectGraphVars.directGraph.getSuccessorCount(n) > 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 0.7f);
            } else {
                return null;
            }
        } else if (nodeColorWeight > 0.7 && nodeColorWeight <= 0.75) {
            if (MyDirectGraphVars.directGraph.getSuccessorCount(n) > 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 0.75f);
            } else if (MyDirectGraphVars.directGraph.getSuccessorCount(n) == 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 0.75f);
            } else if (MyDirectGraphVars.directGraph.getSuccessorCount(n) > 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 0.75f);
            } else {
                return null;
            }
        } else if (nodeColorWeight > 0.75 && nodeColorWeight <= 0.8) {
            if (MyDirectGraphVars.directGraph.getSuccessorCount(n) > 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 0.8f);
            } else if (MyDirectGraphVars.directGraph.getSuccessorCount(n) == 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 0.8f);
            } else if (MyDirectGraphVars.directGraph.getSuccessorCount(n) > 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 0.8f);
            } else {
                return null;
            }
        } else if (nodeColorWeight > 0.8 && nodeColorWeight <= 0.85) {
            if (MyDirectGraphVars.directGraph.getSuccessorCount(n) > 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 0.85f);
            } else if (MyDirectGraphVars.directGraph.getSuccessorCount(n) == 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 0.85f);
            } else if (MyDirectGraphVars.directGraph.getSuccessorCount(n) > 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 0.85f);
            } else {
                return null;
            }
        } else if (nodeColorWeight > 0.85 && nodeColorWeight <= 0.9) {
            if (MyDirectGraphVars.directGraph.getSuccessorCount(n) > 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 0.9f);
            } else if (MyDirectGraphVars.directGraph.getSuccessorCount(n) == 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 0.9f);
            } else if (MyDirectGraphVars.directGraph.getSuccessorCount(n) > 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 0.9f);
            } else {
                return null;
            }
        } else if (nodeColorWeight > 0.9 && nodeColorWeight <= 0.95) {
            if (MyDirectGraphVars.directGraph.getSuccessorCount(n) > 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 0.95f);
            } else if (MyDirectGraphVars.directGraph.getSuccessorCount(n) == 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 0.95f);
            } else if (MyDirectGraphVars.directGraph.getSuccessorCount(n) > 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 0.95f);
            } else {
                return null;
            }
        } else {
            if (MyDirectGraphVars.directGraph.getSuccessorCount(n) > 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) > 0) {
                return new Color(0f, 0f, 1f, 1f);
            } else if (MyDirectGraphVars.directGraph.getSuccessorCount(n) == 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 1f);
            } else if (MyDirectGraphVars.directGraph.getSuccessorCount(n) > 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) == 0) {
                return new Color(1f, 0f, 0f, 1f);
            } else {
                return null;
            }
        }
    }

    public void setWeightedEdgeStroker() {this.getRenderContext().setEdgeStrokeTransformer(this.weightedEdgeStroker);}
    public void setUnWeigtedEdgeStroker() {this.getRenderContext().setEdgeStrokeTransformer(this.unWeightedEdgeStroker);}
    public void setWeightedEdgeColor() {this.getRenderContext().setEdgeDrawPaintTransformer(this.weightedEdgeColor);}
    public void setUnWeightedEdgeColor() {this.getRenderContext().setEdgeDrawPaintTransformer(this.unWeightedEdgeColor);}
    public void setUnWeightedNodeColor() {this.getRenderContext().setVertexFillPaintTransformer(this.unWeightedNodeColor);}
    public void setWeightedNodeColor() {this.getRenderContext().setVertexFillPaintTransformer(this.weightedNodeColor);}

}
