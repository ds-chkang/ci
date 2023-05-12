package medousa.sequential.graph.analysis;

import medousa.sequential.graph.MyEdge;
import medousa.sequential.graph.MyNode;
import medousa.sequential.utils.MyMathUtil;
import medousa.sequential.utils.MySequentialGraphSysUtil;
import medousa.sequential.utils.MySequentialGraphVars;
import edu.uci.ics.jung.visualization.VisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import org.apache.commons.collections15.Transformer;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.Collection;

import static java.awt.Cursor.HAND_CURSOR;

public class MyGraphAnalyzerViewer
extends VisualizationViewer<MyNode, MyEdge>
implements Serializable {

    protected MyAnalysisGraphMouseListener graphMouseListener;
    protected MyGraphAnalyzer plusNetworkAnalyzer;
    protected float MAX_EDGE_VALUE = 0f;
    protected float MAX_NODE_VALUE = 0f;
    protected float MAX_EDGE_SIZE = 26f;
    protected float MAX_NODE_SIZE = 40f;
    protected Point mouseClickedLocation;
    protected MySelectedNodeEdgeValueBarChart selectedNodEdgeValueBarChart;

    public MyGraphAnalyzerViewer(VisualizationModel<MyNode, MyEdge> vm, JComboBox nodeOptionComboBox) {
        super(vm);
        try {
            this.setPreferredSize(new Dimension(400, 380));
            DefaultModalGraphMouse graphMouse = new DefaultModalGraphMouse();
            graphMouse.setMode(DefaultModalGraphMouse.Mode.PICKING);
            this.setGraphMouse(graphMouse);
            this.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.S);
            this.getRenderContext().setLabelOffset(10);
            Cursor handCursor = new Cursor(HAND_CURSOR);
            this.setCursor(handCursor);
            this.setBackground(Color.WHITE);
            this.setDoubleBuffered(true);
            this.setLayout(null);
            this.getRenderContext().setEdgeDrawPaintTransformer(this.edgeColor);
            this.getRenderContext().setVertexFillPaintTransformer(this.vertexColor);
            this.setVertexToolTipTransformer(this.defaultToolTipper);
            this.getRenderContext().setVertexShapeTransformer(this.nodeSizer);
            this.graphMouseListener = new MyAnalysisGraphMouseListener(this);
            this.selectedNodEdgeValueBarChart = new MySelectedNodeEdgeValueBarChart(this);
            this.addGraphMouseListener(this.graphMouseListener);
            this.addMouseListener(new MyGraphViewerMouseListener(this, nodeOptionComboBox));
           this.getRenderContext().setEdgeArrowStrokeTransformer(new Transformer<MyEdge, Stroke>() {
                @Override public Stroke transform(MyEdge e) {
                    float edgeWidth = ((BasicStroke)getRenderContext().getEdgeStrokeTransformer().transform(e)).getLineWidth()/2;
                    return new BasicStroke(edgeWidth);
                }
            });
			this.getRenderContext().setVertexFontTransformer(new Transformer<MyNode, Font>() {
                @Override public synchronized Font transform(MyNode n) {
                    if (n.getContribution() == 0) {return new Font("Noto Sans", Font.PLAIN, 0);
                    } else {return new Font("Noto Sans", Font.BOLD, 26);}
                }
            });

            this.getRenderContext().setEdgeFontTransformer(new Transformer<MyEdge, Font>() {
                @Override public synchronized Font transform(MyEdge e) {
                    if (e.getContribution() == 0) {return new Font("Noto Sans", Font.PLAIN, 0);
                    } else {return new Font("Noto Sans", Font.BOLD, 26);}
                }
            });
            this.scale();
        } catch (Exception ex) {}
    }

    private Transformer<MyNode, Shape> nodeSizer = new Transformer<MyNode, Shape>() {
        @Override public Shape transform(MyNode n) {
            return new Ellipse2D.Double(-40, -40, 80, 80);
        }
    };

    public Transformer<MyNode, String> defaultToolTipper = new Transformer<MyNode, String>() {
        @Override public String transform(MyNode n) {
            if (MySequentialGraphVars.isTimeOn) {
                String toolTip = "<HTML>" +
                        "<BODY>" +
                        "NODE: " + MySequentialGraphSysUtil.decodeNodeName(n.getName()) +
                        "<BR>" +
                        "CONTRIBUTION: " + MyMathUtil.getCommaSeperatedNumber(n.getContribution()) +
                        "<BR>" +
                        "UNIQUE CONTRIBUTION: " + MyMathUtil.getCommaSeperatedNumber(n.getUniqueContribution()) +
                        "<BR>" +
                        "END NODE COUNT: " + MySequentialGraphSysUtil.getCommaSeperateString(n.getEndNodeCount()) +
                        "<BR>" +
                        "PRDECESSORS: " + MySequentialGraphSysUtil.getCommaSeperateString(n.getPredecessorCount()) +
                        "<BR>" +
                        "SUCCESSORS: " + MySequentialGraphSysUtil.getCommaSeperateString(n.getSuccessorCount()) +
                        "</BODY>" +
                        "</HTML>";
                return toolTip;
            } else {
                String toolTip = "<HTML>" +
                        "<BODY>" +
                        "NODE: " + MySequentialGraphSysUtil.decodeNodeName(n.getName()) +
                        "<BR>" +
                        "CONTRIBUTION: " + MyMathUtil.getCommaSeperatedNumber(n.getContribution()) +
                        "<BR>" +
                        "UNIQUE CONTRIBUTION: " + MyMathUtil.getCommaSeperatedNumber(n.getUniqueContribution()) +
                        "<BR>" +
                        "END NODE COUNT: " + MySequentialGraphSysUtil.getCommaSeperateString(n.getEndNodeCount()) +
                        "<BR>" +
                        "PRDECESSORS: " + MySequentialGraphSysUtil.getCommaSeperateString(n.getPredecessorCount()) +
                        "<BR>" +
                        "SUCCESSORS: " + MySequentialGraphSysUtil.getCommaSeperateString(n.getSuccessorCount()) +
                        "</BODY>" +
                        "</HTML>";
                return toolTip;
            }
        }
    };

    public Transformer<MyNode, Paint> vertexColor = new Transformer<MyNode, Paint>() {
        public Paint transform(MyNode n) {
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

    private Transformer<MyEdge, Paint> edgeColor = new Transformer<MyEdge, Paint>() {
        @Override public Paint transform(MyEdge myEdge) {
            return Color.LIGHT_GRAY;
        }
    };

    public void setEdgeContributionValue() {
        Collection<MyEdge> edges = this.getGraphLayout().getGraph().getEdges();
        for (MyEdge edge : edges) {
            int edgeContribution = 0;
            for (int sequence = 0; sequence < MySequentialGraphVars.seqs.length; sequence++) {
                for (int i = 1; i < MySequentialGraphVars.seqs[sequence].length; i++) {
                    String predecessor = MySequentialGraphVars.seqs[sequence][i-1].split(":")[0];
                    String successor = MySequentialGraphVars.seqs[sequence][i].split(":")[0];
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
        Collection<MyEdge> edges = this.getGraphLayout().getGraph().getEdges();
        for (MyEdge edge : edges) {
            int edgeReachTime = 0;
            for (int sequence = 0; sequence < MySequentialGraphVars.seqs.length; sequence++) {
                for (int i = 1; i < MySequentialGraphVars.seqs[sequence].length; i++) {
                    String predecessor = MySequentialGraphVars.seqs[sequence][i-1].split(":")[0];
                    String successor = MySequentialGraphVars.seqs[sequence][i].split(":")[0];
                    long reachTime = Long.valueOf(MySequentialGraphVars.seqs[sequence][i].split(":")[1]);
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
