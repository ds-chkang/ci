package medousa.sequential.graph.flow;

import edu.uci.ics.jung.visualization.VisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.renderers.DefaultVertexLabelRenderer;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import medousa.sequential.graph.MyEdge;
import medousa.sequential.graph.MyNode;
import medousa.sequential.utils.MyMathUtil;
import medousa.sequential.utils.MySequentialGraphSysUtil;
import medousa.sequential.utils.MySequentialGraphVars;
import org.apache.commons.collections15.Transformer;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.io.Serializable;

import static java.awt.Cursor.HAND_CURSOR;

public class MyIntegratedGraphViewer
extends VisualizationViewer<MyNode, MyEdge>
implements Serializable {

    public float MAX_NODE_VALUE;
    private ScalingControl viewScaler;

    public MyIntegratedGraphViewer(VisualizationModel<MyNode, MyEdge> vm) {
        super(vm);
        getRenderContext().setLabelOffset(25);
        Cursor handCursor = new Cursor(HAND_CURSOR);
        setCursor(handCursor);
        setBackground(Color.WHITE);
        setDoubleBuffered(true);
        setLayout(null);
        DefaultModalGraphMouse graphMouse = new DefaultModalGraphMouse();
        graphMouse.setMode(DefaultModalGraphMouse.Mode.PICKING);
        setGraphMouse(graphMouse);
        setVertexToolTipTransformer(new Transformer<MyNode, String>() {
            @Override public String transform(MyNode n) {
                return  "<html><body>" +
                        MySequentialGraphSysUtil.getNodeName(n.getName().split("-")[0]) +
                        "<br>CONTRIBUTION: " + MyMathUtil.getCommaSeperatedNumber(n.contribution) +
                        "</body></html>";
            }
        });

        DefaultVertexLabelRenderer vertexLabelRenderer = new DefaultVertexLabelRenderer(Color.RED) {
            @Override public <V> Component getVertexLabelRendererComponent(JComponent vv, Object value, Font font, boolean isSelected, V n) {
                super.getVertexLabelRendererComponent(vv, value, font, isSelected, n);
                if (MySequentialGraphVars.getSequentialGraphViewer().singleNode != null && ((MyNode) n).getName().split("-")[0].equals(MySequentialGraphVars.getSequentialGraphViewer().singleNode.getName())) {
                    setForeground(Color.RED);
                }  else if (MySequentialGraphVars.getSequentialGraphViewer().multiNodes != null && MySequentialGraphVars.getSequentialGraphViewer().multiNodes.size() > 0) {
                    if (MySequentialGraphVars.getSequentialGraphViewer().multiNodes.contains(MySequentialGraphVars.g.vRefs.get(((MyNode)n).getName().split("-")[0]))) {
                        setForeground(Color.RED);
                    } else {
                        setForeground(Color.RED);
                    }
                } else {
                    setForeground(Color.DARK_GRAY);
                }
                return this;
            }};

        this.getRenderContext().setVertexLabelRenderer(vertexLabelRenderer);
        this.getRenderContext().setVertexLabelTransformer(this.nodeLabeller);

        this.getRenderContext().setVertexFontTransformer(new Transformer<MyNode, Font>() {
            @Override public Font transform(MyNode n) {
                if (MySequentialGraphVars.getSequentialGraphViewer().singleNode != null) {
                    if (n.getName().split("-")[0].equals(MySequentialGraphVars.getSequentialGraphViewer().singleNode.getName())) {
                        return new Font("Noto Sans", Font.BOLD, 26);
                    } else {
                        return new Font("Noto Sans", Font.BOLD, 20);
                    }
                } else if (MySequentialGraphVars.getSequentialGraphViewer().multiNodes != null && MySequentialGraphVars.getSequentialGraphViewer().multiNodes.size() > 0) {
                    if (MySequentialGraphVars.getSequentialGraphViewer().multiNodes.contains(MySequentialGraphVars.g.vRefs.get(n.getName().split("-")[0]))) {
                        return new Font("Noto Sans", Font.BOLD, 26);
                    } else {
                        return new Font("Noto Sans", Font.BOLD, 20);
                    }
                } else {
                    return new Font("Noto Sans", Font.BOLD, 20);
                }
            }
        });

        this.getRenderContext().setVertexShapeTransformer(new Transformer<MyNode, Shape>() {
            @Override public Shape transform(MyNode n) {
                float nodeSizeRatio = (float) n.getContribution()/MAX_NODE_VALUE;
                return setNodeSize(nodeSizeRatio);
            }});
        this.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.S);
        this.getRenderContext().setVertexFillPaintTransformer(this.nodeColor);

        this.scale();
    }

    private Transformer<MyNode, String> nodeLabeller = new Transformer<MyNode, String>() {
        @Override public String transform(MyNode n) {
            return MySequentialGraphSysUtil.getNodeName(n.getName());
        }};

    public Transformer<MyNode, Paint> nodeColor = new Transformer<MyNode, Paint>() {
        @Override public Paint transform(MyNode n) {
         if (getGraphLayout().getGraph().getSuccessorCount(n) == 0 && getGraphLayout().getGraph().getPredecessorCount(n) > 0) {
                return new Color(0.0f, 1.0f, 0.0f, 0.7f);
            } else if (getGraphLayout().getGraph().getSuccessorCount(n) > 0 && getGraphLayout().getGraph().getPredecessorCount(n) == 0) {
                return new Color(1.0f, 0.0f, 0.0f, 0.7f);
            } else {
                return new Color(0.0f, 0.0f, 1.0f, 0.7f);
            }
        }
    };


    private void scale() {
        this.viewScaler = new CrossoverScalingControl();
        double amount = -1.0;
        this.viewScaler.scale(this, amount > 0 ? 2.0f : 1 / 4.5f, new Point2D.Double(100, 50));
    }

    public final float MIN_NODE_SIZE = 5;

    private Ellipse2D.Double setNodeSize(float nodeWeight) {
        if (nodeWeight <= 0.05) {
            float nodeSize = MIN_NODE_SIZE/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        } else if (nodeWeight > 0.05 && nodeWeight <= 0.1) {
            float nodeSize = (MIN_NODE_SIZE+3)/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        } else if (nodeWeight > 0.1 && nodeWeight <= 0.15) {
            float nodeSize = (MIN_NODE_SIZE+6)/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        } else if (nodeWeight > 0.15 && nodeWeight <= 0.2) {
            float nodeSize = (MIN_NODE_SIZE+9)/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        } else if (nodeWeight > 0.2 && nodeWeight <= 0.25) {
            float nodeSize = (MIN_NODE_SIZE+12)/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        } else if (nodeWeight > 0.25 && nodeWeight <= 0.3) {
            float nodeSize = (MIN_NODE_SIZE+15)/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        } else if (nodeWeight > 0.3 && nodeWeight <= 0.35) {
            float nodeSize = (MIN_NODE_SIZE+18)/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        } else if (nodeWeight > 0.35 && nodeWeight <= 0.4) {
            float nodeSize = (MIN_NODE_SIZE+21)/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        } else if (nodeWeight > 0.4 && nodeWeight <= 0.45) {
            float nodeSize = (MIN_NODE_SIZE+24)/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        } else if (nodeWeight > 0.45 && nodeWeight <= 0.5) {
            float nodeSize = (MIN_NODE_SIZE+27)/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        } else if (nodeWeight > 0.5 && nodeWeight <= 0.55) {
            float nodeSize = (MIN_NODE_SIZE+30)/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        } else if (nodeWeight > 0.55 && nodeWeight <= 0.6) {
            float nodeSize = (MIN_NODE_SIZE+33)/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        } else if (nodeWeight > 0.6 && nodeWeight <= 0.65) {
            float nodeSize = (MIN_NODE_SIZE+36)/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        } else if (nodeWeight > 0.65 && nodeWeight <= 0.7) {
            float nodeSize = (MIN_NODE_SIZE+39)/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        } else if (nodeWeight > 0.7 && nodeWeight <= 0.75) {
            float nodeSize = (MIN_NODE_SIZE+42)/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        } else if (nodeWeight > 0.75 && nodeWeight <= 0.8) {
            float nodeSize = (MIN_NODE_SIZE+45)/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        } else if (nodeWeight > 0.8 && nodeWeight <= 0.85) {
            float nodeSize = (MIN_NODE_SIZE+48)/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        } else if (nodeWeight > 0.85 && nodeWeight <= 0.9) {
            float nodeSize = (MIN_NODE_SIZE+51)/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        } else if (nodeWeight > 0.9 && nodeWeight <= 0.95) {
            float nodeSize = (MIN_NODE_SIZE+54)/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        } else {
            float nodeSize = (MIN_NODE_SIZE+57)/2;
            return new Ellipse2D.Double(-nodeSize, -nodeSize, nodeSize*2, nodeSize*2);
        }
    }

}
