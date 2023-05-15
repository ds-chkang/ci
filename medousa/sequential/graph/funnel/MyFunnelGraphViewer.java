package medousa.sequential.graph.funnel;

import edu.uci.ics.jung.visualization.VisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
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

public class MyFunnelGraphViewer
extends VisualizationViewer<MyNode, MyEdge>
implements Serializable {

    protected MyFunnelAnalysisApp funnelAnalysisApp;
    protected Point mouseClickedLocation;
    protected float MAX_NODE_SIZE = 40f;

    protected MyFunnelGraphViewerMouseListener funnelGraphViewerMouseListener;
    protected MyFunnelGraphMouseListener funnelGraphMouseListener;

    public MyFunnelGraphViewer(VisualizationModel<MyNode, MyEdge> vm, MyFunnelAnalysisApp funnelAnalysisApp) {
        super(vm);
        this.funnelAnalysisApp = funnelAnalysisApp;
        this.setPreferredSize(new Dimension(400, 380));
        DefaultModalGraphMouse graphMouse = new DefaultModalGraphMouse();
        graphMouse.setMode(DefaultModalGraphMouse.Mode.PICKING);
        this.setGraphMouse(graphMouse);
        this.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.S);
        this.getRenderContext().setLabelOffset(10);
        this.funnelGraphMouseListener = new MyFunnelGraphMouseListener(this);
        this.funnelGraphViewerMouseListener = new MyFunnelGraphViewerMouseListener(this);
        this.addMouseListener(this.funnelGraphViewerMouseListener);
        this.getRenderContext().setVertexFillPaintTransformer(this.vertexColor);
        this.addGraphMouseListener(this.funnelGraphMouseListener);
        this.getRenderContext().setVertexShapeTransformer(this.nodeSizer);
        Cursor handCursor = new Cursor(HAND_CURSOR);
        this.setVertexToolTipTransformer(this.defaultToolTipper);
        this.setCursor(handCursor);
        this.setBackground(Color.WHITE);
        this.setDoubleBuffered(true);
        this.setLayout(null);

        this.getRenderContext().setEdgeArrowStrokeTransformer(new Transformer<MyEdge, Stroke>() {
            @Override public Stroke transform(MyEdge e) {
                float edgeWidth = ((BasicStroke)getRenderContext().getEdgeStrokeTransformer().transform(e)).getLineWidth()/2;
                return new BasicStroke(edgeWidth);
            }
        });

        this.getRenderContext().setVertexFontTransformer(new Transformer<MyNode, Font>() {
            @Override public synchronized Font transform(MyNode n) {
                return new Font("Noto Sans", Font.BOLD, 26);
            }
        });

        this.getRenderContext().setEdgeFontTransformer(new Transformer<MyEdge, Font>() {
            @Override public synchronized Font transform(MyEdge e) {
                return new Font("Noto Sans", Font.BOLD, 28);
            }
        });

        this.getRenderContext().setVertexLabelTransformer(new Transformer<MyNode, String>() {
            @Override public String transform(MyNode n) {
               return MySequentialGraphSysUtil.getNodeName(n.getName());
            }
        });

        this.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        this.scale();
    }

    public Transformer<MyNode, String> defaultToolTipper = new Transformer<MyNode, String>() {
        @Override public String transform(MyNode n) {
            String toolTip =
                    "<HTML>" +
                    "<BODY>" +
                    MySequentialGraphSysUtil.getNodeName(n.getName()) +
                    "</BODY>" +
                    "</HTML>";
            return toolTip;
        }
    };

    public Transformer<MyNode, Paint> vertexColor = new Transformer<MyNode, Paint>() {
        public Paint transform(MyNode n) {
            if (funnelGraphMouseListener.selectedNode != null && funnelGraphMouseListener.selectedNode == n) {return Color.YELLOW;
            } else if (getGraphLayout().getGraph().getPredecessorCount(n) > 0 && getGraphLayout().getGraph().getSuccessorCount(n) > 0) {return Color.BLUE;
            } else if (getGraphLayout().getGraph().getPredecessorCount(n) == 0 && getGraphLayout().getGraph().getSuccessorCount(n) > 0) {return Color.RED;
            } else if (getGraphLayout().getGraph().getPredecessorCount(n) > 0 && getGraphLayout().getGraph().getSuccessorCount(n) == 0) {return Color.GREEN;
            } else {return Color.RED;}
        }
    };

    private Transformer<MyNode, Shape> nodeSizer = new Transformer<MyNode, Shape>() {
        @Override public Shape transform(MyNode n) {
            return new Ellipse2D.Double(-40, -40, 80, 80);
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
}
