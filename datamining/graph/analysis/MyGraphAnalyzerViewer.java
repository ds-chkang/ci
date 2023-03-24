package datamining.graph.analysis;

import datamining.graph.MyDirectEdge;
import datamining.graph.MyDirectNode;
import datamining.utils.MyMathUtil;
import datamining.utils.system.MyVars;
import edu.uci.ics.jung.visualization.VisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import org.apache.commons.collections15.Transformer;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.Collection;

import static java.awt.Cursor.HAND_CURSOR;

public class MyGraphAnalyzerViewer
extends VisualizationViewer<MyDirectNode, MyDirectEdge>
implements Serializable {

    protected MyGraphMouseListener graphMouseListener;
    protected MyGraphAnalyzer graphAnalyzer;
    protected float MAX_EDGE_VALUE = 0f;
    protected float MAX_NODE_VALUE = 0f;
    protected float MAX_EDGE_SIZE = 18f;
    protected float MAX_NODE_SIZE = 48f;
    protected Point mouseClickedLocation;


    public MyGraphAnalyzerViewer(VisualizationModel<MyDirectNode, MyDirectEdge> vm, MyGraphAnalyzer graphAnalyzer) {
        super(vm);
        try {
            this.setPreferredSize(new Dimension(4000, 4000));
            DefaultModalGraphMouse graphMouse = new DefaultModalGraphMouse();
            graphMouse.setMode(DefaultModalGraphMouse.Mode.PICKING);
            this.setGraphMouse(graphMouse);
            this.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.S);
            this.getRenderContext().setLabelOffset(25);
            Cursor handCursor = new Cursor(HAND_CURSOR);
            this.setCursor(handCursor);
            this.setBackground(Color.WHITE);
            this.setDoubleBuffered(true);
            this.getRenderContext().setEdgeArrowStrokeTransformer(new Transformer<MyDirectEdge, Stroke>() {
                @Override public Stroke transform(MyDirectEdge e) {
                    float edgeWidth = ((BasicStroke)getRenderContext().getEdgeStrokeTransformer().transform(e)).getLineWidth()/2;
                    return new BasicStroke(edgeWidth);
                }
            });
            this.setLayout(null);
            this.getRenderContext().setEdgeDrawPaintTransformer(this.edgeColor);
            this.getRenderContext().setVertexFillPaintTransformer(this.vertexColor);
            this.setVertexToolTipTransformer(this.defaultToolTipper);
            this.getRenderContext().setVertexShapeTransformer(this.nodeSizer);
            this.graphMouseListener = new MyGraphMouseListener(this);
            this.addGraphMouseListener(this.graphMouseListener);
            this.addMouseListener(new MyGraphViewerMouseListener(this, graphAnalyzer));
            this.getRenderContext().setVertexFontTransformer(new Transformer<MyDirectNode, Font>() {
                @Override public synchronized Font transform(MyDirectNode n) {
                    if (n.getContribution() == 0) {return new Font("Noto Sans", Font.PLAIN, 0);
                    } else {return new Font("Noto Sans", Font.BOLD, 24);}
                }
            });

            this.getRenderContext().setEdgeFontTransformer(new Transformer<MyDirectEdge, Font>() {
                @Override public synchronized Font transform(MyDirectEdge e) {
                    if (e.getContribution() == 0) {return new Font("Noto Sans", Font.PLAIN, 0);
                    } else {return new Font("Noto Sans", Font.BOLD, 24);}
                }
            });
            this.scale();
        } catch (Exception ex) {}
    }

    private Transformer<MyDirectNode, Shape> nodeSizer = new Transformer<MyDirectNode, Shape>() {
        @Override public Shape transform(MyDirectNode n) {
            return new Ellipse2D.Double(-40, -40, 80, 80);
        }
    };

    public Transformer<MyDirectNode, String> defaultToolTipper = new Transformer<MyDirectNode, String>() {
        @Override public String transform(MyDirectNode n) {

                String toolTip = "<HTML>" +
                        "<BODY>" +
                        "NODE: " + n.getName() +
                        "<BR>" +
                        "CONTRIBUTION: " + MyMathUtil.getCommaSeperatedNumber(n.getContribution()) +
                        "</BODY>" +
                        "</HTML>";
                return toolTip;

        }
    };

    public Transformer<MyDirectNode, Paint> vertexColor = new Transformer<MyDirectNode, Paint>() {
        public Paint transform(MyDirectNode n) {
            if (graphMouseListener.selectedNode != null && graphMouseListener.selectedNode == n) {return Color.YELLOW;
            } else if (getGraphLayout().getGraph().getPredecessorCount(n) > 0 && getGraphLayout().getGraph().getSuccessorCount(n) > 0) {return Color.BLUE;
            } else if (getGraphLayout().getGraph().getPredecessorCount(n) == 0 && getGraphLayout().getGraph().getSuccessorCount(n) > 0) {return Color.RED;
            } else if (getGraphLayout().getGraph().getPredecessorCount(n) > 0 && getGraphLayout().getGraph().getSuccessorCount(n) == 0) {return Color.GREEN;
            } else {return Color.RED;}
        }
    };

    @Override public void paintComponent(Graphics g) {
        try {super.paintComponent(g);
        } catch (Exception ex) {}
    }

    private void scale() {
        ScalingControl scalingControl = new CrossoverScalingControl();
        double amount = -1.0;
        scalingControl.scale(this, amount > 0 ? 2.0f : 1 / 2.0f, new Point2D.Double(30, 50));
    }

    private Transformer<MyDirectEdge, Paint> edgeColor = new Transformer<MyDirectEdge, Paint>() {
        @Override public Paint transform(MyDirectEdge myEdge) {
            return new Color(0, 0, 0, 0.1f);
        }
    };

    public void setEdgeContributionValue() {
        Collection<MyDirectEdge> edges = this.getGraphLayout().getGraph().getEdges();
        for (MyDirectEdge edge : edges) {
            int edgeContribution = 0;
            for (int sequence=0; sequence < MyVars.seqs.length; sequence++) {
                for (int i=1; i < MyVars.seqs[sequence].length; i++) {
                    String predecessor = MyVars.seqs[sequence][i-1].split(":")[0];
                    String successor = MyVars.seqs[sequence][i].split(":")[0];
                    if (predecessor.equals(edge.getSource().getName()) && successor.equals(edge.getDest().getName())) {
                        edgeContribution++;
                    }
                }
            }
            if (((MyGraph)this.getGraphLayout().getGraph()).MAX_EDGE_VALUE < edgeContribution) {
                ((MyGraph)this.getGraphLayout().getGraph()).MAX_EDGE_VALUE = edgeContribution;
            }
            edge.setCurrentValue(edgeContribution);
        }
    }

    public void setEdgeReachTimeValue() {
        Collection<MyDirectEdge> edges = this.getGraphLayout().getGraph().getEdges();
        for (MyDirectEdge edge : edges) {
            int edgeReachTime = 0;
            for (int sequence=0; sequence < MyVars.seqs.length; sequence++) {
                for (int i=1; i < MyVars.seqs[sequence].length; i++) {
                    String predecessor = MyVars.seqs[sequence][i-1].split(":")[0];
                    String successor = MyVars.seqs[sequence][i].split(":")[0];
                    long reachTime = Long.valueOf(MyVars.seqs[sequence][i].split(":")[1]);
                    if (predecessor.equals(edge.getSource().getName()) && successor.equals(edge.getDest().getName())) {
                        edgeReachTime += reachTime;
                    }
                }
            }
            if (((MyGraph)this.getGraphLayout().getGraph()).MAX_EDGE_VALUE < edgeReachTime) {
                ((MyGraph)this.getGraphLayout().getGraph()).MAX_EDGE_VALUE = edgeReachTime;
            }
            edge.setCurrentValue(edgeReachTime);
        }
    }


}
