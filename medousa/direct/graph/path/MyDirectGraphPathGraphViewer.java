package medousa.direct.graph.path;


import medousa.direct.graph.MyDirectEdge;
import medousa.direct.graph.MyDirectGraph;
import medousa.direct.graph.MyDirectNode;
import medousa.direct.utils.MyDirectGraphSysUtil;
import edu.uci.ics.jung.visualization.VisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import org.apache.commons.collections15.Transformer;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;

import static java.awt.Cursor.HAND_CURSOR;

public class MyDirectGraphPathGraphViewer
extends VisualizationViewer<MyDirectNode, MyDirectEdge>
implements Serializable {

    protected final float MIN_NODE_SZ = 4f;
    protected final float MAX_EDGE_STOKE = 26f;
    protected float MAX_NODE_SIZE = 0.00001f;
    protected float MAX_EDGE_SIZE = 0.00001f;
    protected float MAX_EDGE_VALUE = 0.00001f;
    protected float MIN_EDGE_VALUE = 100000000000.0f;
    protected float MAX_NODE_VALUE;
    protected float MIN_NODE_VALUE = 1000000000000.0f;
    private ScalingControl viewScaler;
    protected MyDirectNode selectedNode;
    protected MyDirectGraphPathGraphNodeValueBarChart betweenPathGraphNodeValueBarChart;
    protected MyDirectGraphPathGraphEdgeValueBarChart betweenPathGraphEdgeValueBarChart;
    private MyDirectGraph pathGraph;
    protected MyDirectGraphDepthFirstGraphPathSercher betweenPathGraphDepthFirstSearch;
    protected LinkedHashMap<String, Long> nodeOrderMap;

    public MyDirectGraphPathGraphViewer(VisualizationModel<MyDirectNode, MyDirectEdge> vm) {
        super(vm);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int)screenSize.getWidth()+500;
        int height = (int)screenSize.getHeight()+500;
        this.setPreferredSize(new Dimension(width, height));
        this.scale();
        this.pathGraph = (MyDirectGraph) vm.getGraphLayout().getGraph();
        this.setMaximumEdgeSize();
        this.setMaximumNodeSize();
        this.setMaximumNodeValue();
        this.setMinimumNodeValue();
        this.setMinimumEdgeValue();
        this.setMaximumEdgeValue();
        this.setDecodedNodeName();
        this.setNodeOrderMap();
        DefaultModalGraphMouse graphMouse = new DefaultModalGraphMouse();
        graphMouse.setMode(DefaultModalGraphMouse.Mode.PICKING);
        this.setGraphMouse(graphMouse);
        this.getRenderContext().setLabelOffset(25);
        Cursor handCursor = new Cursor(HAND_CURSOR);
        this.setCursor(handCursor);
        this.setBackground(Color.WHITE);
        this.setDoubleBuffered(true);
        this.setLayout(null);
        this.setVertexToolTipTransformer(this.defaultToolTipper);
        this.getRenderContext().setVertexDrawPaintTransformer(vertexDrawPainter);
        this.getRenderContext().setVertexLabelTransformer(vertexLabeller);
        this.getRenderContext().setVertexShapeTransformer(nodeSizer);
        this.getRenderContext().setEdgeDrawPaintTransformer(edgeDrawPainter);
        this.getRenderContext().setArrowDrawPaintTransformer(edgeArrowPainter);
        this.getRenderContext().setArrowFillPaintTransformer(edgeArrowPainter);
        this.getRenderContext().setVertexFontTransformer(new Transformer<MyDirectNode, Font>() {
            @Override public Font transform(MyDirectNode node) {
                return new Font("Noto Sans", Font.PLAIN, 32);
            }
        });
        //this.setVertexToolTipTransformer(this.defaultToolTipper);
        this.getRenderContext().setVertexFillPaintTransformer(this.unWeightedNodeColor);
        this.getRenderContext().setEdgeStrokeTransformer(new Transformer<MyDirectEdge, Stroke>() {
            public synchronized Stroke transform(MyDirectEdge e) {
                if (betweenPathGraphDepthFirstSearch.nodesByDepthComboBoxMenu.getSelectedIndex() > 0) {
                    return new BasicStroke(0f);
                } else {
                    return new BasicStroke(1f);
                }
            }
        });
        this.addMouseListener(new MyDirectGraphPathGraphViewerMouseListener(this));
        this.addMouseMotionListener(new MyDirectGraphPathGraphMouseMotionListener(this));
        this.addGraphMouseListener(new MyDirectGraphPathGraphMouseListener(this));
    }

    @Override public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    public Transformer<MyDirectNode, String> vertexLabeller = new Transformer<MyDirectNode, String>() {
        @Override  public String transform(MyDirectNode n) {
            if (betweenPathGraphDepthFirstSearch.nodesByDepthComboBoxMenu.getSelectedIndex() > 0) {
                if (n.shortestOutDistance != betweenPathGraphDepthFirstSearch.nodesByDepthComboBoxMenu.getSelectedIndex()) {
                    return "";
                } else {
                    return n.getName();
                }
            } else if (n.getContribution() == 0) {
                return "";
            } else if (selectedNode != null) {
                if (pathGraph.getSuccessors(n).contains(selectedNode)) {
                    return n.getName();
                } else if (pathGraph.getPredecessors(n).contains(selectedNode)) {
                    return n.getName();
                } else if (n == selectedNode) {
                    return n.getName();
                } else return "";
            } else {
                return n.getName();
            }
        }
    };

    public Transformer<MyDirectEdge, Paint> edgeArrowPainter = new Transformer<MyDirectEdge, Paint>() {
        @Override public Paint transform(MyDirectEdge e) {
            if (betweenPathGraphDepthFirstSearch.nodesByDepthComboBoxMenu.getSelectedIndex() > 0) {
                return new Color(0,0,0,0f);
            } else if (e.getDest().getContribution() == 0 || e.getSource().getContribution() == 0) {
                return new Color(0,0,0,0f);
            } else if (selectedNode != null) {
                if (e.getSource() != selectedNode && e.getDest() != selectedNode) {
                    return new Color(0,0,0,0f);
                } else if (pathGraph.getSuccessors(selectedNode).contains(e.getDest())) {
                    return Color.BLUE;
                } else {
                    return Color.RED;
                }
            } else {
                return Color.LIGHT_GRAY;
            }
        }
    };

    public Transformer<MyDirectEdge, Paint> edgeDrawPainter = new Transformer<MyDirectEdge, Paint>() {
        @Override public Paint transform(MyDirectEdge e) {
            if (betweenPathGraphDepthFirstSearch.nodesByDepthComboBoxMenu.getSelectedIndex() > 0) {
                return new Color(0,0,0,0f);
            } else if (e.getDest().getContribution() == 0 || e.getSource().getContribution() == 0) {
                return new Color(0,0,0,0f);
            } else if (selectedNode != null) {
                if (e.getSource() != selectedNode && e.getDest() != selectedNode) {
                    return new Color(0,0,0,0f);
                } else if (pathGraph.getSuccessors(selectedNode).contains(e.getDest())) {
                    return Color.BLUE;
                } else {
                    return Color.RED;
                }
            } else {
                return Color.LIGHT_GRAY;
            }
        }
    };

    public Transformer<MyDirectNode, Paint> vertexDrawPainter = new Transformer<MyDirectNode, Paint>() {
        @Override public Paint transform(MyDirectNode v) {
            if (betweenPathGraphDepthFirstSearch.nodesByDepthComboBoxMenu.getSelectedIndex() > 0) {
                 return new Color(0,0,0,0f);
            } else {
                return Color.DARK_GRAY;
            }
        }
    };

    public Transformer<MyDirectNode, Shape> nodeSizer = new Transformer<MyDirectNode, Shape>() {
        @Override public Shape transform(MyDirectNode n) {
            if (betweenPathGraphDepthFirstSearch.nodesByDepthComboBoxMenu.getSelectedIndex() > 0) {
                if (n.shortestOutDistance != betweenPathGraphDepthFirstSearch.nodesByDepthComboBoxMenu.getSelectedIndex()) {
                    return new Ellipse2D.Double(0, 0, 0, 0);
                } else {
                    n.setNodeSize(setNodeSize((float)n.getContribution()/MAX_NODE_SIZE));
                    return n.getNodeSize();
                }
            } else if (n.getContribution() == 0) {
                return new Ellipse2D.Double(0, 0, 0, 0);
            } else if (selectedNode == null) {
                if (pathGraph.getPredecessorCount(n) == 0 || pathGraph.getSuccessorCount(n) == 0) {
                    return new Ellipse2D.Double(-MIN_NODE_SZ, -MIN_NODE_SZ, MIN_NODE_SZ * 2, MIN_NODE_SZ * 2);
                }  else if (n.getNodeSize() == null) {
                    n.setNodeSize(setNodeSize((float) n.getContribution() / MAX_NODE_SIZE));
                    return n.getNodeSize();
                } else {
                    return n.getNodeSize();
                }
            } else if (n == selectedNode) {
                if (pathGraph.getPredecessorCount(n) == 0 || pathGraph.getSuccessorCount(n) == 0) {
                    return new Ellipse2D.Double(-MIN_NODE_SZ, -MIN_NODE_SZ, MIN_NODE_SZ * 2, MIN_NODE_SZ * 2);
                } else {
                    n.setNodeSize(setNodeSize((float) n.getContribution() / MAX_NODE_SIZE));
                    return n.getNodeSize();
                }
            } else if (pathGraph.getPredecessors(n).contains(selectedNode)) {
                if (pathGraph.getPredecessorCount(n) == 0 || pathGraph.getSuccessorCount(n) == 0) {
                    return new Ellipse2D.Double(-MIN_NODE_SZ, -MIN_NODE_SZ, MIN_NODE_SZ * 2, MIN_NODE_SZ * 2);
                } else {
                    n.setNodeSize(setNodeSize((float)n.getContribution()/MAX_NODE_SIZE));
                    return n.getNodeSize();
                }
            } else if (pathGraph.getSuccessors(n).contains(selectedNode)) {
                if (pathGraph.getPredecessorCount(n) == 0 || pathGraph.getSuccessorCount(n) == 0) {
                    return new Ellipse2D.Double(-MIN_NODE_SZ, -MIN_NODE_SZ, MIN_NODE_SZ * 2, MIN_NODE_SZ * 2);
                } else {
                    n.setNodeSize(setNodeSize((float)n.getContribution()/MAX_NODE_SIZE));
                    return n.getNodeSize();
                }
            } else {
                return new Ellipse2D.Double(0, 0, 0, 0);
            }
        }
    };

    private void scale() {
        this.viewScaler = new CrossoverScalingControl();
        double amount = -1.0;
        this.viewScaler.scale(this, amount > 0 ? 2.0f : 1/10.6f, new Point2D.Double(550, 250));
    }

    protected void setMaximumNodeSize() {
        Collection<MyDirectNode> nodes = pathGraph.getVertices();
        for (MyDirectNode n : nodes) {
            if (pathGraph.getPredecessorCount(n) > 0 && pathGraph.getSuccessorCount(n) > 0) {
                if (n.getContribution() > this.MAX_NODE_SIZE) {
                    this.MAX_NODE_SIZE = n.getContribution();
                }
            }
        }
    }

    protected long getTotalNodeContribution() {
        long totalNodeContribution = 0;
        Collection<MyDirectNode> nodes = pathGraph.getVertices();
        for (MyDirectNode n : nodes) {
            totalNodeContribution += n.getContribution();
        }
        return totalNodeContribution;
    }

    protected long getTotalEdgeContribution() {
        long totalEdgeContribution = 0;
        Collection<MyDirectEdge> edges = pathGraph.getEdges();
        for (MyDirectEdge e : edges) {
            totalEdgeContribution += e.getContribution();
        }
        return totalEdgeContribution;
    }

    protected void setMaximumNodeValue() {
        if (selectedNode == null) {
            Collection<MyDirectNode> nodes = pathGraph.getVertices();
            for (MyDirectNode n : nodes) {
                if (pathGraph.getPredecessorCount(n) > 0 && pathGraph.getSuccessorCount(n) > 0) {
                    if (this.MAX_NODE_VALUE < n.getContribution()) {
                        this.MAX_NODE_VALUE = n.getContribution();
                    }
                }
            }
            this.MAX_NODE_VALUE = this.MAX_NODE_VALUE + 46;
        } else {
            Collection<MyDirectEdge> outEdges = pathGraph.getOutEdges(selectedNode);
            for (MyDirectEdge e : outEdges) {
                e.getDest().setOriginalValue(e.getDest().getContribution());
                e.getDest().setContribution(e.getContribution());
            }

            Collection<MyDirectEdge> inEdges = pathGraph.getInEdges(selectedNode);
            for (MyDirectEdge e : inEdges) {
                e.getSource().setOriginalValue(e.getSource().getContribution());
                e.getSource().setContribution(e.getContribution());
            }
        }
    }

    protected void setMinimumNodeValue() {
        Collection<MyDirectNode> nodes = pathGraph.getVertices();
        for (MyDirectNode n : nodes) {
            if (pathGraph.getPredecessorCount(n) > 0 && pathGraph.getSuccessorCount(n) > 0) {
                if (n.getContribution() < this.MIN_NODE_VALUE) {
                    this.MIN_NODE_VALUE = n.getContribution();
                }
            }
        }
    }

    protected void resetNodeContributionFromOriginalValue() {
        Collection<MyDirectEdge> outEdges = pathGraph.getOutEdges(selectedNode);
        for (MyDirectEdge e : outEdges) {
            e.getDest().setContribution((int)e.getDest().getOriginalValue());
        }

        Collection<MyDirectEdge> inEdges = pathGraph.getInEdges(selectedNode);
        for (MyDirectEdge e : inEdges) {
            e.getSource().setContribution((int)e.getSource().getOriginalValue());
        }
    }

    public void setNodeOrderMap() {
        this.nodeOrderMap =  new LinkedHashMap<>();
        Collection<MyDirectNode> nodes = pathGraph.getVertices();
        for (MyDirectNode n : nodes) {
            this.nodeOrderMap.put(n.getName(), (long)n.getContribution());
        }
        LinkedHashMap orderedNodeMap = MyDirectGraphSysUtil.sortMapByLongValue(this.nodeOrderMap);
        long i=0;
        for (Object n : orderedNodeMap.keySet()) {
            this.nodeOrderMap.put((String)n, ++i);
        }
    }

    protected void setMaximumEdgeSize() {
        Collection<MyDirectEdge> edges = pathGraph.getEdges();
        for (MyDirectEdge e : edges) {
            if (e.getContribution() > this.MAX_EDGE_SIZE) {
                this.MAX_EDGE_SIZE = e.getContribution();
            }
        }
    }

    protected void setMaximumEdgeValue() {
        Collection<MyDirectEdge> edges = pathGraph.getEdges();
        for (MyDirectEdge e : edges) {
            if (e.getContribution() > this.MAX_EDGE_VALUE) {
                this.MAX_EDGE_VALUE = e.getContribution();
            }
        }
    }

    protected void setMinimumEdgeValue() {
        Collection<MyDirectEdge> edges = pathGraph.getEdges();
        for (MyDirectEdge e : edges) {
            if (e.getContribution() < this.MIN_EDGE_VALUE) {
                this.MIN_EDGE_VALUE = e.getContribution();
            }
        }
    }

    private void setDecodedNodeName() {
        Collection<MyDirectNode> nodes = pathGraph.getVertices();
        for (MyDirectNode n : nodes) {
            n.setName(n.getName());
        }
    }

    protected Transformer<MyDirectNode, Paint> unWeightedNodeColor = new Transformer<MyDirectNode, Paint>() {
        public Paint transform(MyDirectNode n) {
            if (pathGraph.getSuccessorCount(n) > 0 && pathGraph.getPredecessorCount(n) == 0) {
                return Color.BLUE;
            } else if (pathGraph.getSuccessorCount(n) == 0 && pathGraph.getPredecessorCount(n) > 0) {
                return Color.GREEN;
            } else if (pathGraph.getSuccessorCount(n) > 0 && pathGraph.getPredecessorCount(n) > 0) {
                return Color.RED;
            } else {
                return Color.WHITE;
            }
        }
    };

    private Transformer<MyDirectNode, String> defaultToolTipper = new Transformer<MyDirectNode, String>() {
        @Override public synchronized String transform(MyDirectNode n) {
            return n.getName();
        }
    };

    private Ellipse2D.Double setNodeSize(float nodeWeight) {
        if (nodeWeight <= 0.05) {
            float nodeSize = MIN_NODE_SZ/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        } else if (nodeWeight > 0.05 && nodeWeight <= 0.1) {
            float nodeSize = (MIN_NODE_SZ+4)/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        } else if (nodeWeight > 0.1 && nodeWeight <= 0.15) {
            float nodeSize = (MIN_NODE_SZ+8)/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        } else if (nodeWeight > 0.15 && nodeWeight <= 0.2) {
            float nodeSize = (MIN_NODE_SZ+12)/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        } else if (nodeWeight > 0.2 && nodeWeight <= 0.25) {
            float nodeSize = (MIN_NODE_SZ+16)/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        } else if (nodeWeight > 0.25 && nodeWeight <= 0.3) {
            float nodeSize = (MIN_NODE_SZ+20)/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        } else if (nodeWeight > 0.3 && nodeWeight <= 0.35) {
            float nodeSize = (MIN_NODE_SZ+24)/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        } else if (nodeWeight > 0.35 && nodeWeight <= 0.4) {
            float nodeSize = (MIN_NODE_SZ+28)/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        } else if (nodeWeight > 0.4 && nodeWeight <= 0.45) {
            float nodeSize = (MIN_NODE_SZ+32)/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        } else if (nodeWeight > 0.45 && nodeWeight <= 0.5) {
            float nodeSize = (MIN_NODE_SZ+36)/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        } else if (nodeWeight > 0.5 && nodeWeight <= 0.55) {
            float nodeSize = (MIN_NODE_SZ+40)/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        } else if (nodeWeight > 0.55 && nodeWeight <= 0.6) {
            float nodeSize = (MIN_NODE_SZ+44)/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        } else if (nodeWeight > 0.6 && nodeWeight <= 0.65) {
            float nodeSize = (MIN_NODE_SZ+48)/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        } else if (nodeWeight > 0.65 && nodeWeight <= 0.7) {
            float nodeSize = (MIN_NODE_SZ+52)/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        } else if (nodeWeight > 0.7 && nodeWeight <= 0.75) {
            float nodeSize = (MIN_NODE_SZ+56)/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        } else if (nodeWeight > 0.75 && nodeWeight <= 0.8) {
            float nodeSize = (MIN_NODE_SZ+60)/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        } else if (nodeWeight > 0.8 && nodeWeight <= 0.85) {
            float nodeSize = (MIN_NODE_SZ+64)/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        } else if (nodeWeight > 0.85 && nodeWeight <= 0.9) {
            float nodeSize = (MIN_NODE_SZ+69)/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        } else if (nodeWeight > 0.9 && nodeWeight <= 0.95) {
            float nodeSize = (MIN_NODE_SZ+72)/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        } else {
            float nodeSize = (MIN_NODE_SZ+77)/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        }
    }

    protected Transformer<MyDirectEdge, Paint> weightedEdgeColor = new Transformer<MyDirectEdge, Paint>() {
        @Override public Paint transform(MyDirectEdge e) {
            float value = (float)e.getContribution()/MAX_EDGE_VALUE;
            if (value >= 0.8) {
                return Color.LIGHT_GRAY;
            } else if (value < 0.8 && value >= 0.6) {
                return Color.decode("#dbdbdb");
            } else if (value < 0.6 && value >= 0.4) {
                return Color.decode("#e4e4e4");
            } else if (value < 0.4 && value >= 0.2) {
                return Color.decode("#ededed");
            } else {
                return Color.decode("#f6f6f6");
            }
        }
    };

    protected Transformer<MyDirectNode, Paint> weightedNodeColor = new Transformer<MyDirectNode, Paint>() {
        public Paint transform(MyDirectNode n) {
            float nodeColorWeight = (float)n.getContribution()/MAX_NODE_VALUE;
            if (pathGraph.getSuccessorCount(n) > 0 && pathGraph.getPredecessorCount(n) > 0) {
                return new Color(1f, 0f, 0f, nodeColorWeight);
            } else if (pathGraph.getSuccessorCount(n) == 0 && pathGraph.getPredecessorCount(n) > 0) {
                return new Color(0f, 1f, 0f, 1f);
            } else if (pathGraph.getSuccessorCount(n) > 0 && pathGraph.getPredecessorCount(n) == 0) {
                return new Color(0f, 0f, 1f, 1f);
            } else {
                return null;
            }
        }
    };


}
