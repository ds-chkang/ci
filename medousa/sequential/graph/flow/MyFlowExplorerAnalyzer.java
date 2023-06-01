package medousa.sequential.graph.flow;

import edu.uci.ics.jung.visualization.renderers.DefaultVertexLabelRenderer;
import medousa.sequential.graph.MyDepthEdge;
import medousa.sequential.graph.MyDepthNode;
import medousa.sequential.graph.MyEdge;
import medousa.sequential.graph.MyNode;
import medousa.sequential.graph.layout.MyDirectedSparseMultigraph;
import medousa.sequential.graph.stats.MyPathFlowNodesByDepthLineBarChart;
import medousa.sequential.graph.stats.multinode.MyMultiNodeAppearanceByDepthLineChart;
import medousa.sequential.graph.stats.singlenode.MySingleNodeAppearanceByDepthLineChart;
import medousa.MyProgressBar;
import medousa.sequential.utils.MyMathUtil;
import medousa.sequential.utils.MySequentialGraphSysUtil;
import medousa.sequential.utils.MySequentialGraphVars;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import medousa.table.MyTableToolTipper;
import medousa.table.MyTableUtil;
import org.apache.commons.collections15.Transformer;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.HorizontalAlignment;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

import static java.awt.Cursor.HAND_CURSOR;

public class MyFlowExplorerAnalyzer
implements ActionListener {

    public Map<String, MyEdge> edgeRefMap = new HashMap<>();
    protected VisualizationViewer<MyDepthNode, MyDepthEdge> graphViewer;
    protected MyDirectedSparseMultigraph<MyDepthNode, MyDepthEdge> pathFlowGraph;
    private StaticLayout<MyDepthNode, MyDepthEdge> layout;
    public float MAX_NODE_VALUE;
    public final float MAX_EDGE_STROKE = 25f;
    public final float MIN_NODE_SIZE = 5;
    public float MAX_NODE_HEIGHT = 0f;
    public float MAX_NODE_WIDTH = 0f;
    public float MAX_EDGE_VALUE;
    private ScalingControl viewScaler;
    public JTable nodeListTable;
    private JPanel tablePanel;
    private String [] columns = {"NO.", "D.", "N.", "V."};
    private String [][] data = {};
    public JCheckBox allDepth;
    public JCheckBox weightedEdge;
    private int mxDepth;
    boolean isNotDataFlow = true;

    public MyFlowExplorerAnalyzer() {
        this.mxDepth = MySequentialGraphVars.mxDepth;
        this.pathFlowGraph = new MyDirectedSparseMultigraph<>();
        this.layout = new StaticLayout<>(this.pathFlowGraph);
        this.graphViewer = new VisualizationViewer<>(this.layout);
    }

    private void createFlowGraph() {
        for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
            for (int i = 1; i < MySequentialGraphVars.seqs[s].length; i++) {
                String ps = MySequentialGraphVars.seqs[s][i-1].split(":")[0] + "-" + (i-1);
                String ss = MySequentialGraphVars.seqs[s][i].split(":")[0] + "-" + i;
                String edgeRef = ps + "-" + ss;
                if (!this.edgeRefMap.containsKey(edgeRef)) {
                    if (!this.pathFlowGraph.vRefs.containsKey(ps)) {
                        MyDepthNode pn = new MyDepthNode(ps, i-1);
                        if ((i-1) != 0) {
                            pn.inContribution++;
                        }
                        pn.contribution++;
                        pn.outContribution++;
                        this.pathFlowGraph.addVertex(pn);
                        this.pathFlowGraph.vRefs.put(ps, pn);
                    } else {
                        if ((i-1) != 0) {
                            this.pathFlowGraph.vRefs.get(ps).inContribution++;
                        }
                        this.pathFlowGraph.vRefs.get(ps).outContribution++;
                        this.pathFlowGraph.vRefs.get(ps).contribution++;
                    }
                    if (!this.pathFlowGraph.vRefs.containsKey(ss)) {
                        MyDepthNode sn = new MyDepthNode(ss, i);
                        if ((i+1) != MySequentialGraphVars.seqs[s].length) {
                            sn.outContribution++;
                        }
                        sn.inContribution++;
                        sn.contribution++;
                        this.pathFlowGraph.addVertex(sn);
                        this.pathFlowGraph.vRefs.put(ss, sn);
                    } else {
                        if ((i+1) != MySequentialGraphVars.seqs[s].length) {
                            this.pathFlowGraph.vRefs.get(ss).outContribution++;
                        }
                        this.pathFlowGraph.vRefs.get(ss).inContribution++;
                        this.pathFlowGraph.vRefs.get(ss).contribution++;
                    }
                    MyDepthNode pn = (MyDepthNode) this.pathFlowGraph.vRefs.get(ps);
                    MyDepthNode sn = (MyDepthNode) this.pathFlowGraph.vRefs.get(ss);
                    MyDepthEdge edge = new MyDepthEdge(pn, sn);
                    edge.contribution++;
                    this.pathFlowGraph.addEdge(edge, pn, sn);
                    this.edgeRefMap.put(edgeRef, edge);
                } else {
                    MyDepthNode sNode = (MyDepthNode) this.pathFlowGraph.vRefs.get(ss);
                    MyDepthNode pNode = (MyDepthNode) this.pathFlowGraph.vRefs.get(ps);
                    pNode.contribution++;
                    pNode.outContribution++;
                    sNode.contribution++;
                    sNode.inContribution++;
                    if ((i+1) != MySequentialGraphVars.seqs[s].length) {
                        sNode.outContribution++;
                    }
                    if ((i-1) != 0) {
                        pNode.inContribution++;
                    }
                    this.edgeRefMap.get(edgeRef).contribution++;

                }
            }
        }
    }

    private void createBetweenPathGraph() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(MySequentialGraphSysUtil.getWorkingDir() + MySequentialGraphSysUtil.getDirectorySlash() + "pathSequences.txt"));
            String line = "";
            while ((line = br.readLine()) != null) {
                String [] itemsets = line.split("-");
                for (int i = 1; i < itemsets.length; i++) {
                    String ps = itemsets[i-1] + "-" + (i-1);
                    String ss = itemsets[i] + "-" + i;
                    String edgeRef = ps + "-" + ss;
                    if (!this.edgeRefMap.containsKey(edgeRef)) {
                        if (!this.pathFlowGraph.vRefs.containsKey(ps)) {
                            MyDepthNode pn = new MyDepthNode(ps, i-1);
                            if ((i-1) != 0) {
                                pn.inContribution++;
                            }
                            pn.contribution++;
                            pn.outContribution++;
                            this.pathFlowGraph.addVertex(pn);
                            this.pathFlowGraph.vRefs.put(ps, pn);
                        } else {
                            if ((i-1) != 0) {
                                this.pathFlowGraph.vRefs.get(ps).inContribution++;
                            }
                            this.pathFlowGraph.vRefs.get(ps).outContribution++;
                            this.pathFlowGraph.vRefs.get(ps).contribution++;
                        }
                        if (!this.pathFlowGraph.vRefs.containsKey(ss)) {
                            MyDepthNode sn = new MyDepthNode(ss, i);
                            if ((i+1) != itemsets.length) {
                                sn.outContribution++;
                            }
                            sn.inContribution++;
                            sn.contribution++;
                            this.pathFlowGraph.addVertex(sn);
                            this.pathFlowGraph.vRefs.put(ss, sn);
                        } else {
                            if ((i+1) != itemsets.length) {
                                this.pathFlowGraph.vRefs.get(ss).outContribution++;
                            }
                            this.pathFlowGraph.vRefs.get(ss).inContribution++;
                            this.pathFlowGraph.vRefs.get(ss).contribution++;
                        }
                        MyDepthNode pn = (MyDepthNode) this.pathFlowGraph.vRefs.get(ps);
                        MyDepthNode sn = (MyDepthNode) this.pathFlowGraph.vRefs.get(ss);
                        MyDepthEdge edge = new MyDepthEdge(pn, sn);
                        edge.contribution++;
                        this.pathFlowGraph.addEdge(edge, pn, sn);
                        this.edgeRefMap.put(edgeRef, edge);
                    } else {
                        MyDepthNode sNode = (MyDepthNode) this.pathFlowGraph.vRefs.get(ss);
                        MyDepthNode pNode = (MyDepthNode) this.pathFlowGraph.vRefs.get(ps);
                        pNode.contribution++;
                        pNode.outContribution++;
                        sNode.contribution++;
                        sNode.inContribution++;
                        if ((i+1) != itemsets.length) {
                            sNode.outContribution++;
                        }
                        if ((i-1) != 0) {
                            pNode.inContribution++;
                        }
                        this.edgeRefMap.get(edgeRef).contribution++;
                    }
                }
            }
            br.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void createSelectedNodeFromPathGraph() {
        for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
            for (int i = 1; i < MySequentialGraphVars.seqs[s].length; i++) {
                String ps = MySequentialGraphVars.seqs[s][i-1].split(":")[0];
                if (ps.equals(MySequentialGraphVars.getSequentialGraphViewer().singleNode.getName())) {
                    int depth = 1;
                    for (int j = i; j < MySequentialGraphVars.seqs[s].length; j++) {
                        ps = MySequentialGraphVars.seqs[s][j-1].split(":")[0] + "-" + (depth-1);
                        String ss = MySequentialGraphVars.seqs[s][j].split(":")[0] + "-" + depth;
                        String edgeRef = ps + "-" + ss;
                        if (!this.edgeRefMap.containsKey(edgeRef)) {
                            if (!this.pathFlowGraph.vRefs.containsKey(ps)) {
                                MyDepthNode pn = new MyDepthNode(ps, depth-1);
                                if ((depth-1) != 0) {
                                    pn.inContribution++;
                                }
                                pn.contribution++;
                                pn.outContribution++;
                                this.pathFlowGraph.addVertex(pn);
                                this.pathFlowGraph.vRefs.put(ps, pn);
                            } else {
                                if ((depth-1) != 0) {
                                    this.pathFlowGraph.vRefs.get(ps).inContribution++;
                                }
                                this.pathFlowGraph.vRefs.get(ps).outContribution++;
                                this.pathFlowGraph.vRefs.get(ps).contribution++;
                            }
                            if (!this.pathFlowGraph.vRefs.containsKey(ss)) {
                                MyDepthNode sn = new MyDepthNode(ss, depth);
                                if ((depth+1) != MySequentialGraphVars.seqs[s].length) {
                                    sn.outContribution++;
                                }
                                sn.inContribution++;
                                sn.contribution++;
                                this.pathFlowGraph.addVertex(sn);
                                this.pathFlowGraph.vRefs.put(ss, sn);
                            } else {
                                if ((depth+1) != MySequentialGraphVars.seqs[s].length) {
                                    this.pathFlowGraph.vRefs.get(ss).outContribution++;
                                }
                                this.pathFlowGraph.vRefs.get(ss).inContribution++;
                                this.pathFlowGraph.vRefs.get(ss).contribution++;
                            }
                            MyDepthNode pn = (MyDepthNode) this.pathFlowGraph.vRefs.get(ps);
                            MyDepthNode sn = (MyDepthNode) this.pathFlowGraph.vRefs.get(ss);
                            MyDepthEdge edge = new MyDepthEdge(pn, sn);
                            edge.contribution++;
                            this.pathFlowGraph.addEdge(edge, pn, sn);
                            this.edgeRefMap.put(edgeRef, edge);
                        } else {
                            MyDepthNode sNode = (MyDepthNode) this.pathFlowGraph.vRefs.get(ss);
                            MyDepthNode pNode = (MyDepthNode) this.pathFlowGraph.vRefs.get(ps);
                            pNode.contribution++;
                            pNode.outContribution++;
                            sNode.contribution++;
                            sNode.inContribution++;
                            if ((depth+1) != MySequentialGraphVars.seqs[s].length) {
                                sNode.outContribution++;
                            }
                            if ((depth-1) != 0) {
                                pNode.inContribution++;
                            }
                            this.edgeRefMap.get(edgeRef).contribution++;
                        }
                        depth++;
                    }
                    break;
                }
            }
        }
    }

    private void createSelectedNodeToPathGraph() {
        for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
            int itemset = isSelectedNodeExistsInSequence(s);
            if (itemset > 0) {
                for (int i = 1; i <= itemset; i++) {
                    String ps = MySequentialGraphVars.seqs[s][i-1].split(":")[0] + "-" + (i - 1);
                    String ss = MySequentialGraphVars.seqs[s][i].split(":")[0] + "-" + i;
                    String edgeRef = ps + "-" + ss;
                    if (!this.edgeRefMap.containsKey(edgeRef)) {
                        if (!this.pathFlowGraph.vRefs.containsKey(ps)) {
                            MyDepthNode pn = new MyDepthNode(ps, i-1);
                            if ((i - 1) != 0) {
                                pn.inContribution++;
                            }
                            pn.contribution++;
                            pn.outContribution++;
                            this.pathFlowGraph.addVertex(pn);
                            this.pathFlowGraph.vRefs.put(ps, pn);
                        } else {
                            if ((i - 1) != 0) {
                                this.pathFlowGraph.vRefs.get(ps).inContribution++;
                            }
                            this.pathFlowGraph.vRefs.get(ps).outContribution++;
                            this.pathFlowGraph.vRefs.get(ps).contribution++;
                        }
                        if (!this.pathFlowGraph.vRefs.containsKey(ss)) {
                            MyDepthNode sn = new MyDepthNode(ss, i);
                            if ((i + 1) != MySequentialGraphVars.seqs[s].length) {
                                sn.outContribution++;
                            }
                            sn.inContribution++;
                            sn.contribution++;
                            this.pathFlowGraph.addVertex(sn);
                            this.pathFlowGraph.vRefs.put(ss, sn);
                        } else {
                            if ((i + 1) != MySequentialGraphVars.seqs[s].length) {
                                this.pathFlowGraph.vRefs.get(ss).outContribution++;
                            }
                            this.pathFlowGraph.vRefs.get(ss).inContribution++;
                            this.pathFlowGraph.vRefs.get(ss).contribution++;
                        }
                        MyDepthNode pn = (MyDepthNode) this.pathFlowGraph.vRefs.get(ps);
                        MyDepthNode sn = (MyDepthNode) this.pathFlowGraph.vRefs.get(ss);
                        MyDepthEdge edge = new MyDepthEdge(pn, sn);
                        edge.contribution++;
                        this.pathFlowGraph.addEdge(edge, pn, sn);
                        this.edgeRefMap.put(edgeRef, edge);
                    } else {
                        MyDepthNode sNode = (MyDepthNode) this.pathFlowGraph.vRefs.get(ss);
                        MyDepthNode pNode = (MyDepthNode) this.pathFlowGraph.vRefs.get(ps);
                        pNode.contribution++;
                        pNode.outContribution++;
                        sNode.contribution++;
                        sNode.inContribution++;
                        if ((i + 1) != MySequentialGraphVars.seqs[s].length) {
                            sNode.outContribution++;
                        }
                        if ((i - 1) != 0) {
                            pNode.inContribution++;
                        }
                        this.edgeRefMap.get(edgeRef).contribution++;
                    }
                }
            }
        }
    }

    private int isSelectedNodeExistsInSequence(int s) {
        int itemset = -1;
        for (int i = 1; i < MySequentialGraphVars.seqs[s].length; i++) {
            String n = MySequentialGraphVars.seqs[s][i].split(":")[0];
            if (n.equals(MySequentialGraphVars.getSequentialGraphViewer().singleNode.getName())) {
                itemset = i;
            }
        }
        return itemset;
    }

    private void createMultiNodeFromPathGraph() {
        for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
            for (int i = 1; i < MySequentialGraphVars.seqs[s].length; i++) {
                String ps = MySequentialGraphVars.seqs[s][i-1].split(":")[0];
                if (MySequentialGraphVars.getSequentialGraphViewer().multiNodes.contains(MySequentialGraphVars.g.vRefs.get(ps))) {
                    int depth = 1;
                    for (int j = i; j < MySequentialGraphVars.seqs[s].length; j++) {
                        ps = MySequentialGraphVars.seqs[s][j-1].split(":")[0] + "-" + (depth-1);
                        String ss = MySequentialGraphVars.seqs[s][j].split(":")[0] + "-" + depth;
                        String edgeRef = ps + "-" + ss;
                        if (!this.edgeRefMap.containsKey(edgeRef)) {
                            if (!this.pathFlowGraph.vRefs.containsKey(ps)) {
                                MyDepthNode pn = new MyDepthNode(ps, depth-1);
                                if ((depth-1) != 0) {
                                    pn.inContribution++;
                                }
                                pn.contribution++;
                                pn.outContribution++;
                                this.pathFlowGraph.addVertex(pn);
                                this.pathFlowGraph.vRefs.put(ps, pn);
                            } else {
                                if ((depth-1) != 0) {
                                    this.pathFlowGraph.vRefs.get(ps).inContribution++;
                                }
                                this.pathFlowGraph.vRefs.get(ps).outContribution++;
                                this.pathFlowGraph.vRefs.get(ps).contribution++;
                            }
                            if (!this.pathFlowGraph.vRefs.containsKey(ss)) {
                                MyDepthNode sn = new MyDepthNode(ss, depth);
                                if ((depth+1) != MySequentialGraphVars.seqs[s].length) {
                                    sn.outContribution++;
                                }
                                sn.inContribution++;
                                sn.contribution++;
                                this.pathFlowGraph.addVertex(sn);
                                this.pathFlowGraph.vRefs.put(ss, sn);
                            } else {
                                if ((depth+1) != MySequentialGraphVars.seqs[s].length) {
                                    this.pathFlowGraph.vRefs.get(ss).outContribution++;
                                }
                                this.pathFlowGraph.vRefs.get(ss).inContribution++;
                                this.pathFlowGraph.vRefs.get(ss).contribution++;
                            }
                            MyDepthNode pn = (MyDepthNode) this.pathFlowGraph.vRefs.get(ps);
                            MyDepthNode sn = (MyDepthNode) this.pathFlowGraph.vRefs.get(ss);
                            MyDepthEdge edge = new MyDepthEdge(pn, sn);
                            edge.contribution++;
                            this.pathFlowGraph.addEdge(edge, pn, sn);
                            this.edgeRefMap.put(edgeRef, edge);
                        } else {
                            MyDepthNode sNode = (MyDepthNode) this.pathFlowGraph.vRefs.get(ss);
                            MyDepthNode pNode = (MyDepthNode) this.pathFlowGraph.vRefs.get(ps);
                            pNode.contribution++;
                            pNode.outContribution++;
                            sNode.contribution++;
                            sNode.inContribution++;
                            if ((depth+1) != MySequentialGraphVars.seqs[s].length) {
                                sNode.outContribution++;
                            }
                            if ((depth-1) != 0) {
                                pNode.inContribution++;
                            }
                            this.edgeRefMap.get(edgeRef).contribution++;
                        }
                        depth++;
                    }
                    break;
                }
            }
        }
    }

    public void setMaxNodeValue() {
        Collection<MyDepthNode> nodes = this.pathFlowGraph.getVertices();
        for (MyDepthNode n : nodes) {
            if (this.MAX_NODE_VALUE < n.getContribution()) {
                this.MAX_NODE_VALUE = n.getContribution();
            }
        }
    }

    public void setMaxEdgeValue() {
        Collection<MyDepthEdge> edges = this.pathFlowGraph.getEdges();
        for (MyDepthEdge e : edges) {
            if (this.MAX_EDGE_VALUE < e.getContribution()) {
                this.MAX_EDGE_VALUE = e.getContribution();
            }
        }
    }

    private void setNodeSizes() {
        Collection<MyDepthNode> nodes = this.pathFlowGraph.getVertices();
        for (MyDepthNode n : nodes) {
            n.nodeSize = this.setNodeSize((float) n.getContribution() / this.MAX_NODE_VALUE);
            if (n.nodeSize.getHeight() > this.MAX_NODE_HEIGHT) {
                this.MAX_NODE_HEIGHT = (float) n.nodeSize.getHeight();
            }
            if (n.nodeSize.getWidth() > this.MAX_NODE_WIDTH) {
                this.MAX_NODE_WIDTH = (float) n.nodeSize.getWidth();
            }

        }
    }

    private void setNodeLocations() {
        Collection<MyDepthNode> nodes = this.pathFlowGraph.getVertices();
        for (int i=1; i <= mxDepth; i++) {
            LinkedHashMap<String, Long> sortedNodeMap = new LinkedHashMap<>();
            for (MyDepthNode n : nodes) {
                if (n.relativeDepth == i) {
                    sortedNodeMap.put(n.getName(), n.getContribution());
                }
            }
            if (sortedNodeMap.size() == 0) continue;
            sortedNodeMap = MySequentialGraphSysUtil.sortMapByLongValue(sortedNodeMap);
            int vDepth = 0;
            for (String nn : sortedNodeMap.keySet()) {
                Point2D.Double currentLoc = new Point2D.Double();
                MyDepthNode n = (MyDepthNode) this.pathFlowGraph.vRefs.get(nn);
                if (i == 1) {
                    currentLoc.x = (10 + (this.MAX_NODE_WIDTH/2));
                    currentLoc.y = (((this.MAX_NODE_HEIGHT / 2) + 30) * (vDepth + 1)) + (this.MAX_NODE_HEIGHT * vDepth);
                } else {
                    currentLoc.x = (10 + (this.MAX_NODE_WIDTH/2)) + ((i-1) * 400);
                    currentLoc.y = (((this.MAX_NODE_HEIGHT / 2) + 30) * (vDepth + 1)) + (this.MAX_NODE_HEIGHT * vDepth);
                }
                ++vDepth;
                layout.setLocation(n, currentLoc);
            }
        }
    }

    public MyFlowExplorerViewerMouseListener flowExplorerViewerMouseListener;

    private void setViewer() {
        this.graphViewer.getRenderContext().setLabelOffset(25);
        Cursor handCursor = new Cursor(HAND_CURSOR);
        this.graphViewer.setCursor(handCursor);
        this.graphViewer.setBackground(Color.WHITE);
        this.graphViewer.setDoubleBuffered(true);
        this.graphViewer.setLayout(null);

        DefaultModalGraphMouse graphMouse = new DefaultModalGraphMouse();
        graphMouse.setMode(DefaultModalGraphMouse.Mode.PICKING);
        this.graphViewer.setGraphMouse(graphMouse);
        if (isNotDataFlow) {
            flowExplorerViewerMouseListener = new MyFlowExplorerViewerMouseListener(this, isNotDataFlow);
            this.graphViewer.addMouseListener(flowExplorerViewerMouseListener);
        } else {
            flowExplorerViewerMouseListener = new MyFlowExplorerViewerMouseListener(this);
            this.graphViewer.addMouseListener(flowExplorerViewerMouseListener);
        }
        this.graphViewer.getRenderContext().setVertexFillPaintTransformer(this.nodeColorer);
        this.graphViewer.setBackground(Color.WHITE);
        this.graphViewer.setPreferredSize(new Dimension(10000, 10000));
        this.graphViewer.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<>());
        this.graphViewer.getRenderContext().setVertexShapeTransformer(new Transformer<MyDepthNode, Shape>() {
            @Override public Shape transform(MyDepthNode n) {return n.nodeSize;}});
        this.graphViewer.getRenderContext().setEdgeDrawPaintTransformer(edgeDrawPainter);
        this.graphViewer.getRenderContext().setArrowDrawPaintTransformer(edgeArrowPainter);
        this.graphViewer.getRenderContext().setArrowFillPaintTransformer(edgeArrowPainter);
        this.graphViewer.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.S);
        this.graphViewer.getRenderContext().setEdgeStrokeTransformer(edgeStroker);

        DefaultVertexLabelRenderer vertexLabelRenderer = new DefaultVertexLabelRenderer(Color.RED) {
            @Override public <V> Component getVertexLabelRendererComponent(JComponent vv, Object value, Font font, boolean isSelected, V n) {
                super.getVertexLabelRendererComponent(vv, value, font, isSelected, n);
                if (MySequentialGraphVars.getSequentialGraphViewer().singleNode != null && ((MyDepthNode) n).getName().split("-")[0].equals(MySequentialGraphVars.getSequentialGraphViewer().singleNode.getName())) {
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
        this.graphViewer.getRenderContext().setVertexLabelRenderer(vertexLabelRenderer);

        this.graphViewer.getRenderContext().setVertexFontTransformer(new Transformer<MyDepthNode, Font>() {
            @Override public Font transform(MyDepthNode n) {
                if (MySequentialGraphVars.getSequentialGraphViewer().singleNode != null) {
                    if (n.getName().split("-")[0].equals(MySequentialGraphVars.getSequentialGraphViewer().singleNode.getName())) {
                        return new Font("Noto Sans", Font.BOLD, 30);
                    } else {
                        return new Font("Noto Sans", Font.BOLD, 24);
                    }
                } else if (MySequentialGraphVars.getSequentialGraphViewer().multiNodes != null && MySequentialGraphVars.getSequentialGraphViewer().multiNodes.size() > 0) {
                    if (MySequentialGraphVars.getSequentialGraphViewer().multiNodes.contains(MySequentialGraphVars.g.vRefs.get(n.getName().split("-")[0]))) {
                        return new Font("Noto Sans", Font.BOLD, 30);
                    } else {
                        return new Font("Noto Sans", Font.BOLD, 24);
                    }
                } else {
                    return new Font("Noto Sans", Font.BOLD, 24);
                }
            }
        });

        this.graphViewer.setVertexToolTipTransformer(new Transformer<MyDepthNode, String>() {
            @Override public String transform(MyDepthNode n) {
                return  "<html><body>" +
                        (n.getName().split("-")[0].contains("x") ? MySequentialGraphSysUtil.getDecodeVariableNodeName(n.getName().split("-")[0]) : MySequentialGraphSysUtil.getDecodedNodeName(n.getName().split("-")[0])) +
                        "<br>CONTRIBUTION: " + MyMathUtil.getCommaSeperatedNumber(n.contribution) +
                        "<br>IN-CONTRIBUTION: " + MyMathUtil.getCommaSeperatedNumber(n.inContribution) +
                        "<br>OUT-CONTRIBUTION: " + MyMathUtil.getCommaSeperatedNumber(n.outContribution) +
                        "<br>INOUT-DIFF.: " + MyMathUtil.getCommaSeperatedNumber(n.inContribution-n.outContribution) +
                        "</body></html>";
            }
        });

        this.graphViewer.getRenderContext().setVertexLabelTransformer(new Transformer<MyDepthNode, String>() {
            @Override public String transform(MyDepthNode n) {
                return  "<html><body>" +
                        MySequentialGraphSysUtil.getNodeName(n.getName().split("-")[0]) +
                        "<br>" + MyMathUtil.getCommaSeperatedNumber(n.getContribution()) +
                        "</body></html>";
            }
        });

        this.graphViewer.getRenderContext().setVertexDrawPaintTransformer(new Transformer<MyDepthNode, Paint>() {
            @Override public Paint transform(MyDepthNode n) {
                return Color.BLACK;
            }
        });

        this.graphViewer.getRenderContext().setEdgeShapeTransformer(new EdgeShape.QuadCurve<MyDepthNode, MyDepthEdge>());
        this.scale();
    }

    private void scale() {
        this.viewScaler = new CrossoverScalingControl();
        double amount = -1.0;
        this.viewScaler.scale(this.graphViewer, amount > 0 ? 2.0f : 1 / 2.5f, new Point2D.Double(100, 50));
    }

    private Transformer<MyDepthEdge, Paint> edgeArrowPainter = new Transformer<MyDepthEdge, Paint>() {
        @Override public Paint transform(MyDepthEdge e) {
            return new Color(0, 0, 0, 0f);
        }
    };

    private Transformer<MyDepthEdge, Paint> edgeDrawPainter = new Transformer<MyDepthEdge, Paint>() {
        @Override public Paint transform(MyDepthEdge e) {
            if (nodeListTable.getSelectedRow() >= 0) {
                int depth = (Integer.parseInt(nodeListTable.getValueAt(nodeListTable.getSelectedRow(), 1).toString()) - 1);
                String targetNodeName = MySequentialGraphVars.nodeNameMap.get(nodeListTable.getValueAt(nodeListTable.getSelectedRow(), 2).toString());
                String targetFullNodeName = MySequentialGraphVars.nodeNameMap.get(nodeListTable.getValueAt(nodeListTable.getSelectedRow(), 2).toString()) + "-" + depth;
                if (flowExplorerViewerMouseListener.selectedNode != null) {
                    if (((MyDepthNode)e.getSource()).isSelectedNodePath && ((MyDepthNode)e.getDest()).isSelectedNodePath) {
                        return new Color(1f, 0f, 0f, 0.15f);
                    } else {
                        return new Color(0f, 0f, 0f, 0.025f);
                    }
                } else if (allDepth.isSelected()) {
                    if (e.getSource().getName().split("-")[0].equals(targetNodeName)) {
                        return new Color(1f, 0f, 0f, 0.15f);
                    } else if (e.getDest().getName().split("-")[0].equals(targetNodeName)) {
                        return new Color(1f, 0f, 0f, 0.15f);
                    } else {
                        return new Color(0f, 0f, 0f, 0.025f);
                    }
                } else {
                    if (e.getSource().getName().equals(targetFullNodeName)) {
                        return new Color(1f, 0f, 0f, 0.15f);
                    } else if (e.getDest().getName().equals(targetFullNodeName)) {
                        return new Color(1f, 0f, 0f, 0.15f);
                    } else {
                        return new Color(0f, 0f, 0f, 0.025f);
                    }
                }
            } else if (flowExplorerViewerMouseListener.selectedNode != null) {
                if (((MyDepthNode)e.getSource()).isSelectedNodePath && ((MyDepthNode)e.getDest()).isSelectedNodePath) {
                    return new Color(1f, 0f, 0f, 0.15f);
                } else {
                    return new Color(0f, 0f, 0f, 0.15f);
                }
            } else {
                return new Color(0f, 0f, 0f, 0.15f);
            }
        }
    };

    private Transformer<MyDepthNode, Paint> nodeColorer = new Transformer<MyDepthNode, Paint>() {
        @Override public Paint transform(MyDepthNode n) {
            if (nodeListTable.getSelectedRow() >= 0) {
                int depth = (Integer.parseInt(nodeListTable.getValueAt(nodeListTable.getSelectedRow(), 1).toString())-1);
                String targetNodeName = MySequentialGraphVars.nodeNameMap.get(nodeListTable.getValueAt(nodeListTable.getSelectedRow(), 2).toString());
                String targetFullNodeName = MySequentialGraphVars.nodeNameMap.get(nodeListTable.getValueAt(nodeListTable.getSelectedRow(), 2).toString()) + "-" + depth;
                //MyDepthNode targetNode = (MyDepthNode) directedSparseMultiGraph.vRefs.get(fullNodeName);
                if (allDepth.isSelected()) {
                    if (n.getName().split("-")[0].equals(targetNodeName)) {
                        return Color.ORANGE;
                    } else if (pathFlowGraph.getSuccessorCount(n) == 0 && pathFlowGraph.getPredecessorCount(n) > 0) {
                        return new Color(0.0f, 1.0f, 0.0f, 0.6f);
                    } else if (pathFlowGraph.getSuccessorCount(n) > 0 && pathFlowGraph.getPredecessorCount(n) == 0) {
                        return new Color(1.0f, 0.0f, 0.0f, 0.6f);
                    } else {
                        return new Color(0.0f, 0.0f, 1.0f, 0.6f);
                    }
                } else if (n.getName().equals(targetFullNodeName)) {
                    return Color.ORANGE;
                } else {
                    if (pathFlowGraph.getSuccessorCount(n) == 0 && pathFlowGraph.getPredecessorCount(n) > 0) {
                        return new Color(0.0f, 1.0f, 0.0f, 0.6f);
                    } else if (pathFlowGraph.getSuccessorCount(n) > 0 && pathFlowGraph.getPredecessorCount(n) == 0) {
                        return new Color(1.0f, 0.0f, 0.0f, 0.6f);
                    } else {
                        return new Color(0.0f, 0.0f, 1.0f, 0.6f);
                    }
                }
            } else {
                if (pathFlowGraph.getSuccessorCount(n) == 0 && pathFlowGraph.getPredecessorCount(n) > 0) {
                    return new Color(0.0f, 1.0f, 0.0f, 0.6f);
                } else if (pathFlowGraph.getSuccessorCount(n) > 0 && pathFlowGraph.getPredecessorCount(n) == 0) {
                    return new Color(1.0f, 0.0f, 0.0f, 0.6f);
                } else {
                    return new Color(0.0f, 0.0f, 1.0f, 0.6f);
                }
            }
        }
    };

    private Transformer<MyDepthEdge, Stroke> edgeStroker = new Transformer<MyDepthEdge, Stroke>() {
        @Override public Stroke transform(MyDepthEdge e) {
            if (weightedEdge.isSelected()) {
                float edgeStrokeWeight = e.getContribution() / MAX_EDGE_VALUE;
                return new BasicStroke(edgeStrokeWeight * MAX_EDGE_STROKE);
            } else {
                return new BasicStroke(1f);
            }
        }
    };

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

    private void setNodeTable() {
        this.allDepth = new JCheckBox("A. D.");
        this.allDepth.setBackground(Color.WHITE);
        this.allDepth.setFont(MySequentialGraphVars.tahomaPlainFont11);
        this.allDepth.setFocusable(false);
        this.allDepth.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                graphViewer.revalidate();
                graphViewer.repaint();

                MySequentialGraphVars.getSequentialGraphViewer().revalidate();
                MySequentialGraphVars.getSequentialGraphViewer().repaint();
            }
        });

        this.weightedEdge = new JCheckBox("WGT. E.");
        this.weightedEdge.setBackground(Color.WHITE);
        this.weightedEdge.setFont(MySequentialGraphVars.tahomaPlainFont11);
        this.weightedEdge.setFocusable(false);
        this.weightedEdge.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                graphViewer.revalidate();
                graphViewer.repaint();

                MySequentialGraphVars.getSequentialGraphViewer().revalidate();
                MySequentialGraphVars.getSequentialGraphViewer().repaint();
            }
        });

        DefaultTableModel bottomTableModel = new DefaultTableModel(data, columns);
        this.nodeListTable = new JTable(bottomTableModel) {
            public String getToolTipText(MouseEvent e) {
                String tip = null;
                Point p = e.getPoint();
                int rowIndex = rowAtPoint(p);
                int colIndex = columnAtPoint(p);
                try {
                    tip = getValueAt(rowIndex, colIndex).toString();
                } catch (RuntimeException e1) {}
                return tip;
            }
        };
        String [] toolTips = {"NO.", "DEPTH", "NODE", "NODE VALUE"};
        MyTableToolTipper tooltipHeader = new MyTableToolTipper(this.nodeListTable.getColumnModel());
        tooltipHeader.setToolTipStrings(toolTips);
        this.nodeListTable.setTableHeader(tooltipHeader);

        Collection<MyDepthNode> nodes = this.pathFlowGraph.getVertices();
        LinkedHashMap<String, Long> sortedNodes = new LinkedHashMap<>();
        for (MyDepthNode n : nodes) {
            sortedNodes.put(n.getName(), n.getContribution());
        }
        sortedNodes = MySequentialGraphSysUtil.sortMapByLongValue(sortedNodes);
        int i=-0;
        for (String n : sortedNodes.keySet()) {
            bottomTableModel.addRow(
                new String[]{
                    String.valueOf(++i),
                        MyMathUtil.getCommaSeperatedNumber(Integer.parseInt(n.split("-")[1]) + 1),
                        MySequentialGraphSysUtil.getDecodedNodeName(n.split("-")[0]),
                        MySequentialGraphSysUtil.formatAverageValue(MyMathUtil.oneDecimalFormat(sortedNodes.get(n))
                )
            });
        }

        this.nodeListTable.setRowHeight(18);
        this.nodeListTable.setBackground(Color.WHITE);
        this.nodeListTable.setFont(MySequentialGraphVars.f_pln_9);
        this.nodeListTable.getTableHeader().setFont(MySequentialGraphVars.tahomaBoldFont10);
        this.nodeListTable.getTableHeader().setOpaque(false);
        this.nodeListTable.setPreferredSize(new Dimension(210, 2000));
        this.nodeListTable.getTableHeader().setBackground(new Color(0, 0, 0, 0f));
        this.nodeListTable.getColumnModel().getColumn(0).setPreferredWidth(55);
        this.nodeListTable.getColumnModel().getColumn(0).setMaxWidth(55);
        this.nodeListTable.getColumnModel().getColumn(1).setPreferredWidth(50);
        this.nodeListTable.getColumnModel().getColumn(1).setMaxWidth(50);
        this.nodeListTable.getColumnModel().getColumn(2).setPreferredWidth(140);
        this.nodeListTable.getColumnModel().getColumn(3).setPreferredWidth(50);
        this.nodeListTable.getColumnModel().getColumn(3).setMaxWidth(50);
        this.nodeListTable.addComponentListener(new ComponentListener() {
            @Override public void componentResized(ComponentEvent e) {}
            @Override public void componentMoved(ComponentEvent e) {}
            @Override public void componentShown(ComponentEvent e) {}
            @Override public void componentHidden(ComponentEvent e) {}
        });

        this.nodeListTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        try {
                            graphViewer.revalidate();
                            graphViewer.repaint();

                            MySequentialGraphVars.getSequentialGraphViewer().revalidate();
                            MySequentialGraphVars.getSequentialGraphViewer().repaint();
                        } catch (Exception ex) {}
                    }
                }).start();
            }
        });

        JTextField searchTxt = new JTextField();
        JButton selectBtn = new JButton("SEL.");
        selectBtn.setFont(MySequentialGraphVars.tahomaPlainFont10);
        selectBtn.setFocusable(false);
        searchTxt.setBackground(Color.WHITE);
        searchTxt.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        this.tablePanel = MyTableUtil.searchAndSaveDataPanelForJTable2(this, searchTxt, selectBtn, bottomTableModel, nodeListTable);
        searchTxt.setFont(MySequentialGraphVars.f_bold_10);
        searchTxt.setToolTipText("TYPE A NODE NAME TO SEARCH");
        searchTxt.setPreferredSize(new Dimension(100, 20));
        this.tablePanel.remove(selectBtn);

        JPanel tableControlPanel = new JPanel();
        tableControlPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3,3));
        tableControlPanel.setBackground(Color.WHITE);
       // tableControlPanel.add(this.allDepth);
        tableControlPanel.add(this.weightedEdge);

        JScrollPane nodeListScrollPane = new JScrollPane(this.nodeListTable);
        this.tablePanel.setLayout(new BorderLayout(3,3));
        this.tablePanel.add(tableControlPanel, BorderLayout.NORTH);
        this.tablePanel.add(nodeListScrollPane, BorderLayout.CENTER);
        this.tablePanel.add(searchTxt, BorderLayout.SOUTH);
        nodeListScrollPane.setPreferredSize(new Dimension(220, 2000));
        nodeListScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(7, 0));
        nodeListScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 7));
    }

    private JPanel setAverageContributionByDepthLineChart() {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries depthContAvgSeries = new XYSeries("AVG. C.");
        Map<Integer, Double> depthAverageMap = new HashMap<>();
        Collection<MyDepthNode> nodes = pathFlowGraph.getVertices();
        for (int i=1; i <= this.mxDepth; i++) {
            long totalCont = 0L;
            long nodeCount = 0L;
            for (MyDepthNode n : nodes) {
                if (n.relativeDepth == i) {
                    nodeCount++;
                    totalCont += n.contribution;
                }
            }
            if (nodeCount > 0) {
                double depthContAvg = (double) totalCont / nodeCount;
                depthAverageMap.put(i, depthContAvg);
            }
        }

        for (Integer depth : depthAverageMap.keySet()) {
            depthContAvgSeries.add(depth, depthAverageMap.get(depth));
        }
        dataset.addSeries(depthContAvgSeries);

        JFreeChart chart = ChartFactory.createXYLineChart("", "MAX. C.", "", dataset);
        chart.getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
        chart.getXYPlot().setBackgroundPaint(Color.WHITE);
        chart.getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
        chart.getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);
        chart.getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
        chart.getXYPlot().getDomainAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
        chart.getXYPlot().getDomainAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
        chart.getXYPlot().getRangeAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
        chart.getXYPlot().getRangeAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
        chart.getXYPlot().getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        chart.setBackgroundPaint(Color.WHITE);

        XYPlot plot = (XYPlot) chart.getPlot();
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, Color.DARK_GRAY);
        renderer.setSeriesStroke(0, new BasicStroke(1.6f));
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
        renderer.setSeriesFillPaint(0, Color.WHITE);
        renderer.setUseFillPaint(true);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(350, 367));

        JLabel titleLabel = new JLabel(" AVG. CONTRIBUTION");
        titleLabel.setFont(MySequentialGraphVars.tahomaBoldFont11);
        titleLabel.setBackground(Color.WHITE);
        titleLabel.setForeground(Color.DARK_GRAY);

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
        titlePanel.add(titleLabel);

        JButton enlargeBtn = new JButton("+");
        enlargeBtn.setFont(MySequentialGraphVars.tahomaPlainFont11);
        enlargeBtn.setBackground(Color.WHITE);
        enlargeBtn.setFocusable(false);
        enlargeBtn.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        enlarge();
                    }
                }).start();
            }
        });

        JPanel enlargePanel = new JPanel();
        enlargePanel.setBackground(Color.WHITE);
        enlargePanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 3));
        enlargePanel.add(enlargeBtn);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout(3, 3));
        topPanel.setBackground(Color.WHITE);
        topPanel.add(titlePanel, BorderLayout.WEST);
        topPanel.add(enlargePanel, BorderLayout.EAST);

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BorderLayout(3,3));
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(chartPanel, BorderLayout.CENTER);
        renderer.setBaseLegendTextFont(MySequentialGraphVars.tahomaPlainFont11);
        return panel;
    }

    private JPanel setNumberOfNodesByDepthLineChart() {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries depthNodesByDepthSeries = new XYSeries("NO OF N.");
        Map<Integer, Long> numberOfNodesByDepthMap = new HashMap<>();
        Collection<MyDepthNode> nodes = pathFlowGraph.getVertices();
        for (int i=1; i <= this.mxDepth; i++) {
            long nodeCount = 0L;
            for (MyDepthNode n : nodes) {
                if (n.relativeDepth == i) {
                    nodeCount++;
                }
            }
            if (nodeCount > 0) {
                numberOfNodesByDepthMap.put(i, nodeCount);
            }
        }

        for (Integer depth : numberOfNodesByDepthMap.keySet()) {
            depthNodesByDepthSeries.add(depth, numberOfNodesByDepthMap.get(depth));
        }
        dataset.addSeries(depthNodesByDepthSeries);

        JFreeChart chart = ChartFactory.createXYLineChart("", "NO. OF N.", "", dataset);
        chart.getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
        chart.getXYPlot().setBackgroundPaint(Color.WHITE);
        chart.getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
        chart.getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);
        chart.getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
        chart.getXYPlot().getDomainAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
        chart.getXYPlot().getDomainAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
        chart.getXYPlot().getRangeAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
        chart.getXYPlot().getRangeAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
        chart.getXYPlot().getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        chart.setBackgroundPaint(Color.WHITE);

        XYPlot plot = (XYPlot) chart.getPlot();
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, Color.DARK_GRAY);
        renderer.setSeriesStroke(0, new BasicStroke(1.6f));
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
        renderer.setSeriesFillPaint(0, Color.WHITE);
        renderer.setUseFillPaint(true);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(350, 367));

        JLabel titleLabel = new JLabel(" NO. OF NODES");
        titleLabel.setFont(MySequentialGraphVars.tahomaBoldFont11);
        titleLabel.setBackground(Color.WHITE);
        titleLabel.setForeground(Color.DARK_GRAY);

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
        titlePanel.add(titleLabel);

        JButton enlargeBtn = new JButton("+");
        enlargeBtn.setFont(MySequentialGraphVars.tahomaPlainFont11);
        enlargeBtn.setBackground(Color.WHITE);
        enlargeBtn.setFocusable(false);
        enlargeBtn.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        enlarge();
                    }
                }).start();
            }
        });

        JPanel enlargePanel = new JPanel();
        enlargePanel.setBackground(Color.WHITE);
        enlargePanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 3));
        enlargePanel.add(enlargeBtn);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout(3, 3));
        topPanel.setBackground(Color.WHITE);
        topPanel.add(titlePanel, BorderLayout.WEST);
        topPanel.add(enlargePanel, BorderLayout.EAST);

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BorderLayout(3,3));
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(chartPanel, BorderLayout.CENTER);
        renderer.setBaseLegendTextFont(MySequentialGraphVars.tahomaPlainFont11);
        return panel;
    }

    private JPanel setMaxContributionByDepthLineChart() {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries maxContributionByDepthSeries = new XYSeries("MAX. V.");
        Map<Integer, Long> maxByDepthMap = new HashMap<>();
        Collection<MyDepthNode> nodes = pathFlowGraph.getVertices();
        for (int i=1; i <= this.mxDepth; i++) {
            long max = 0L;
            for (MyDepthNode n : nodes) {
                if (n.relativeDepth == i) {
                    if (max < n.getContribution()) {
                        max = n.getContribution();
                    }
                }
            }

            if (max > 0) {
                maxByDepthMap.put(i, max);
            }
        }

        for (Integer depth : maxByDepthMap.keySet()) {
            maxContributionByDepthSeries.add(depth, maxByDepthMap.get(depth));
        }
        dataset.addSeries(maxContributionByDepthSeries);

        JFreeChart chart = ChartFactory.createXYLineChart("", "MAX. C.", "", dataset);
        chart.getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
        chart.getXYPlot().setBackgroundPaint(Color.WHITE);
        chart.getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
        chart.getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);
        chart.getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
        chart.getXYPlot().getDomainAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
        chart.getXYPlot().getDomainAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
        chart.getXYPlot().getRangeAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
        chart.getXYPlot().getRangeAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
        chart.getXYPlot().getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        chart.setBackgroundPaint(Color.WHITE);

        XYPlot plot = (XYPlot) chart.getPlot();
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, Color.DARK_GRAY);
        renderer.setSeriesStroke(0, new BasicStroke(1.6f));
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
        renderer.setSeriesFillPaint(0, Color.WHITE);
        renderer.setUseFillPaint(true);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(350, 367));

        JLabel titleLabel = new JLabel(" MAX. CONTRIBUTION");
        titleLabel.setFont(MySequentialGraphVars.tahomaBoldFont11);
        titleLabel.setBackground(Color.WHITE);
        titleLabel.setForeground(Color.DARK_GRAY);

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
        titlePanel.add(titleLabel);

        JButton enlargeBtn = new JButton("+");
        enlargeBtn.setFont(MySequentialGraphVars.tahomaPlainFont11);
        enlargeBtn.setBackground(Color.WHITE);
        enlargeBtn.setFocusable(false);
        enlargeBtn.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        enlarge();
                    }
                }).start();
            }
        });

        JPanel enlargePanel = new JPanel();
        enlargePanel.setBackground(Color.WHITE);
        enlargePanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 3));
        enlargePanel.add(enlargeBtn);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout(3, 3));
        topPanel.setBackground(Color.WHITE);
        topPanel.add(titlePanel, BorderLayout.WEST);
        topPanel.add(enlargePanel, BorderLayout.EAST);

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BorderLayout(3,3));
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(chartPanel, BorderLayout.CENTER);
        renderer.setBaseLegendTextFont(MySequentialGraphVars.tahomaPlainFont11);
        return panel;
    }

    private JPanel setMinContributionByDepthLineChart() {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries minContributionByDepthSeries = new XYSeries("MIN. C.");
        Map<Integer, Long> minByDepthMap = new HashMap<>();
        Collection<MyDepthNode> nodes = pathFlowGraph.getVertices();
        for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
            long min = 1000000000000L;
            for (MyDepthNode n : nodes) {
                if (n.relativeDepth == i) {
                    if (min > n.getContribution()) {
                        min = n.getContribution();
                    }
                }
            }

            if (min < 1000000000000L) {
                minByDepthMap.put(i, min);
            }
        }

        for (Integer depth : minByDepthMap.keySet()) {
            minContributionByDepthSeries.add(depth, minByDepthMap.get(depth));
        }
        dataset.addSeries(minContributionByDepthSeries);

        JFreeChart chart = ChartFactory.createXYLineChart("", "MIN. C.", "", dataset);
        chart.getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
        chart.getXYPlot().setBackgroundPaint(Color.WHITE);
        chart.getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
        chart.getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);
        chart.getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
        chart.getXYPlot().getDomainAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
        chart.getXYPlot().getDomainAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
        chart.getXYPlot().getRangeAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
        chart.getXYPlot().getRangeAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
        chart.getXYPlot().getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        chart.setBackgroundPaint(Color.WHITE);

        XYPlot plot = (XYPlot) chart.getPlot();
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, Color.DARK_GRAY);
        renderer.setSeriesStroke(0, new BasicStroke(1.6f));
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
        renderer.setSeriesFillPaint(0, Color.WHITE);
        renderer.setUseFillPaint(true);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(350, 367));

        JLabel titleLabel = new JLabel(" MIN. CONTRIBUTION");
        titleLabel.setFont(MySequentialGraphVars.tahomaBoldFont11);
        titleLabel.setBackground(Color.WHITE);
        titleLabel.setForeground(Color.DARK_GRAY);

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
        titlePanel.add(titleLabel);

        JButton enlargeBtn = new JButton("+");
        enlargeBtn.setFont(MySequentialGraphVars.tahomaPlainFont11);
        enlargeBtn.setBackground(Color.WHITE);
        enlargeBtn.setFocusable(false);
        enlargeBtn.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        enlarge();
                    }
                }).start();
            }
        });

        JPanel enlargePanel = new JPanel();
        enlargePanel.setBackground(Color.WHITE);
        enlargePanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 3));
        enlargePanel.add(enlargeBtn);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout(3, 3));
        topPanel.setBackground(Color.WHITE);
        topPanel.add(titlePanel, BorderLayout.WEST);
        topPanel.add(enlargePanel, BorderLayout.EAST);

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BorderLayout(3,3));
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(chartPanel, BorderLayout.CENTER);
        renderer.setBaseLegendTextFont(MySequentialGraphVars.tahomaPlainFont11);
        return panel;
    }

    private JPanel setStdContributionByDepthLineChart() {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries minContributionByDepthSeries = new XYSeries("STD. C.");
        Map<Integer, Long> stdByDepthMap = new HashMap<>();
        Collection<MyDepthNode> nodes = pathFlowGraph.getVertices();
        for (int i=1; i <= this.mxDepth; i++) {
            long totalCont = 0L;
            long nodeCount = 0L;
            for (MyDepthNode n : nodes) {
                if (n.relativeDepth == i) {
                    nodeCount++;
                    totalCont += n.contribution;
                }
            }

            if (nodeCount > 0) {
                double depthContAvg = (double) totalCont / nodeCount;
                double totalResiduals = 0D;
                nodeCount = 0;
                for (MyDepthNode n : nodes) {
                    if (n.relativeDepth == i) {
                        totalResiduals += Math.pow((double) n.getContribution() - depthContAvg, 2);
                        nodeCount++;
                    }
                }
                stdByDepthMap.put(i, (long) (totalResiduals / nodeCount));
            }
        }

        for (Integer depth : stdByDepthMap.keySet()) {
            minContributionByDepthSeries.add(depth, stdByDepthMap.get(depth));
        }
        dataset.addSeries(minContributionByDepthSeries);

        JFreeChart chart = ChartFactory.createXYLineChart("", "STD. C.", "", dataset);
        chart.getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
        chart.getXYPlot().setBackgroundPaint(Color.WHITE);
        chart.getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
        chart.getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);
        chart.getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
        chart.getXYPlot().getDomainAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
        chart.getXYPlot().getDomainAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
        chart.getXYPlot().getRangeAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
        chart.getXYPlot().getRangeAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
        chart.getXYPlot().getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        chart.setBackgroundPaint(Color.WHITE);

        XYPlot plot = (XYPlot) chart.getPlot();
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, Color.DARK_GRAY);
        renderer.setSeriesStroke(0, new BasicStroke(1.6f));
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
        renderer.setSeriesFillPaint(0, Color.WHITE);
        renderer.setUseFillPaint(true);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(350, 367));

        JLabel titleLabel = new JLabel(" STD. DEV. CONTRIBUTION");
        titleLabel.setFont(MySequentialGraphVars.tahomaBoldFont11);
        titleLabel.setBackground(Color.WHITE);
        titleLabel.setForeground(Color.DARK_GRAY);

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
        titlePanel.add(titleLabel);

        JButton enlargeBtn = new JButton("+");
        enlargeBtn.setFont(MySequentialGraphVars.tahomaPlainFont11);
        enlargeBtn.setBackground(Color.WHITE);
        enlargeBtn.setFocusable(false);
        enlargeBtn.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        enlarge();
                    }
                }).start();
            }
        });

        JPanel enlargePanel = new JPanel();
        enlargePanel.setBackground(Color.WHITE);
        enlargePanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 3));
        enlargePanel.add(enlargeBtn);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout(3, 3));
        topPanel.setBackground(Color.WHITE);
        topPanel.add(titlePanel, BorderLayout.WEST);
        topPanel.add(enlargePanel, BorderLayout.EAST);

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BorderLayout(3,3));
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(chartPanel, BorderLayout.CENTER);
        renderer.setBaseLegendTextFont(MySequentialGraphVars.tahomaPlainFont11);
        return panel;
    }

    public void enlarge() {

    }

    public void showMultiNodeFromPathFlows() {
        try {
            this.createMultiNodeFromPathGraph();
            this.setMaxNodeValue();
            this.setMaxEdgeValue();
            this.setNodeSizes();
            this.setNodeLocations();
            this.setViewer();
            this.setNodeTable();

            Border blackline = BorderFactory.createLineBorder(Color.black);
            TitledBorder titledBorder = BorderFactory.createTitledBorder(blackline, "");
            titledBorder.setTitleJustification(TitledBorder.LEFT);
            titledBorder.setTitleFont(MySequentialGraphVars.tahomaBoldFont11);

            JPanel vvPanel = new JPanel();
            vvPanel.setLayout(new BorderLayout(3,3));
            vvPanel.setBackground(Color.WHITE);
            vvPanel.setBorder(titledBorder);
            vvPanel.add(this.graphViewer, BorderLayout.CENTER);

            JPanel chartPanel = new JPanel();
            chartPanel.setLayout(new GridLayout(1, 2));
            chartPanel.setBackground(Color.WHITE);
            chartPanel.add(new MyMultiNodeAppearanceByDepthLineChart());
            chartPanel.add(setAverageContributionByDepthLineChart());
            chartPanel.add(setMaxContributionByDepthLineChart());
            chartPanel.add(setMinContributionByDepthLineChart());
            chartPanel.add(setStdContributionByDepthLineChart());
            chartPanel.add(setNumberOfNodesByDepthLineChart());

            JSplitPane chartGraphSplitPane = new JSplitPane();
            chartGraphSplitPane.setDividerLocation(0.80);
            chartGraphSplitPane.setDividerSize(5);
            chartGraphSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
            chartGraphSplitPane.setTopComponent(vvPanel);
            chartGraphSplitPane.setBottomComponent(chartPanel);

            JSplitPane splitPaneWithNodeListTableAndGraph = new JSplitPane();
            splitPaneWithNodeListTableAndGraph.setDividerLocation(0.13);
            splitPaneWithNodeListTableAndGraph.setDividerSize(5);
            splitPaneWithNodeListTableAndGraph.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
            splitPaneWithNodeListTableAndGraph.setLeftComponent(this.tablePanel);
            splitPaneWithNodeListTableAndGraph.setRightComponent(chartGraphSplitPane);

            JFrame f = new JFrame("PATH FLOW ANALYZER");
            f.setBackground(Color.WHITE);
            f.setPreferredSize(new Dimension(750, 600));
            f.setLayout(new BorderLayout(3,3));
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.getContentPane().add(splitPaneWithNodeListTableAndGraph, BorderLayout.CENTER);
            f.addComponentListener(new ComponentAdapter() {
                public void componentResized(ComponentEvent evt) {
                    splitPaneWithNodeListTableAndGraph.setDividerSize(5);
                    splitPaneWithNodeListTableAndGraph.setDividerLocation((int)(f.getWidth()*0.13));

                    chartGraphSplitPane.setDividerSize(5);
                    chartGraphSplitPane.setDividerLocation((int)(f.getHeight()*0.80));

                    MySequentialGraphVars.getSequentialGraphViewer().revalidate();
                    MySequentialGraphVars.getSequentialGraphViewer().repaint();
                }
            });

            f.pack();
            f.setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void showNodeFromPathFlows() {
        try {
            this.createSelectedNodeFromPathGraph();
            this.setMaxNodeValue();
            this.setMaxEdgeValue();
            this.setNodeSizes();
            this.setNodeLocations();
            this.setViewer();
            this.setNodeTable();

            Border blackline = BorderFactory.createLineBorder(Color.black);
            TitledBorder titledBorder = BorderFactory.createTitledBorder(blackline, "");
            titledBorder.setTitleJustification(TitledBorder.LEFT);
            titledBorder.setTitleFont(MySequentialGraphVars.tahomaBoldFont11);

            JPanel vvPanel = new JPanel();
            vvPanel.setLayout(new BorderLayout(3,3));
            vvPanel.setBackground(Color.WHITE);
            vvPanel.setBorder(titledBorder);
            vvPanel.add(this.graphViewer, BorderLayout.CENTER);

            JPanel chartPanel = new JPanel();
            chartPanel.setLayout(new GridLayout(1, 2));
            chartPanel.setBackground(Color.WHITE);
            chartPanel.add(new MySingleNodeAppearanceByDepthLineChart());
            chartPanel.add(setAverageContributionByDepthLineChart());
            chartPanel.add(setMaxContributionByDepthLineChart());
            chartPanel.add(setMinContributionByDepthLineChart());
            chartPanel.add(setStdContributionByDepthLineChart());
            chartPanel.add(setNumberOfNodesByDepthLineChart());

            JSplitPane chartGraphSplitPane = new JSplitPane();
            chartGraphSplitPane.setDividerLocation(0.80);
            chartGraphSplitPane.setDividerSize(5);
            chartGraphSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
            chartGraphSplitPane.setTopComponent(vvPanel);
            chartGraphSplitPane.setBottomComponent(chartPanel);

            JSplitPane splitPaneWithNodeListTableAndGraph = new JSplitPane();
            splitPaneWithNodeListTableAndGraph.setDividerLocation(0.13);
            splitPaneWithNodeListTableAndGraph.setDividerSize(5);
            splitPaneWithNodeListTableAndGraph.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
            splitPaneWithNodeListTableAndGraph.setLeftComponent(this.tablePanel);
            splitPaneWithNodeListTableAndGraph.setRightComponent(chartGraphSplitPane);

            JFrame f = new JFrame("PATH FLOW EXPLORER");
            f.setBackground(Color.WHITE);
            f.setPreferredSize(new Dimension(750, 600));
            f.setLayout(new BorderLayout(3,3));
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.getContentPane().add(splitPaneWithNodeListTableAndGraph, BorderLayout.CENTER);
            f.addComponentListener(new ComponentAdapter() {
                public void componentResized(ComponentEvent evt) {
                    splitPaneWithNodeListTableAndGraph.setDividerSize(5);
                    splitPaneWithNodeListTableAndGraph.setDividerLocation((int)(f.getWidth()*0.13));

                    chartGraphSplitPane.setDividerSize(5);
                    chartGraphSplitPane.setDividerLocation((int)(f.getHeight()*0.80));

                    MySequentialGraphVars.getSequentialGraphViewer().revalidate();
                    MySequentialGraphVars.getSequentialGraphViewer().repaint();
                }
            });

            f.pack();
            f.setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void showNodeToPathFlows() {
        try {
            this.createSelectedNodeToPathGraph();
            this.setMaxNodeValue();
            this.setMaxEdgeValue();
            this.setNodeSizes();
            this.setNodeLocations();
            this.setViewer();
            this.setNodeTable();

            Border blackline = BorderFactory.createLineBorder(Color.black);
            TitledBorder titledBorder = BorderFactory.createTitledBorder(blackline, "");
            titledBorder.setTitleJustification(TitledBorder.LEFT);
            titledBorder.setTitleFont(MySequentialGraphVars.tahomaBoldFont11);

            JPanel vvPanel = new JPanel();
            vvPanel.setLayout(new BorderLayout(3,3));
            vvPanel.setBackground(Color.WHITE);
            vvPanel.setBorder(titledBorder);
            vvPanel.add(this.graphViewer, BorderLayout.CENTER);

            JPanel chartPanel = new JPanel();
            chartPanel.setLayout(new GridLayout(1, 2));
            chartPanel.setBackground(Color.WHITE);
            chartPanel.add(new MySingleNodeAppearanceByDepthLineChart());
            chartPanel.add(setAverageContributionByDepthLineChart());
            chartPanel.add(setMaxContributionByDepthLineChart());
            chartPanel.add(setMinContributionByDepthLineChart());
            chartPanel.add(setStdContributionByDepthLineChart());
            chartPanel.add(setNumberOfNodesByDepthLineChart());

            JSplitPane chartGraphSplitPane = new JSplitPane();
            chartGraphSplitPane.setDividerLocation(0.80);
            chartGraphSplitPane.setDividerSize(5);
            chartGraphSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
            chartGraphSplitPane.setTopComponent(vvPanel);
            chartGraphSplitPane.setBottomComponent(chartPanel);

            JSplitPane splitPaneWithNodeListTableAndGraph = new JSplitPane();
            splitPaneWithNodeListTableAndGraph.setDividerLocation(0.13);
            splitPaneWithNodeListTableAndGraph.setDividerSize(5);
            splitPaneWithNodeListTableAndGraph.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
            splitPaneWithNodeListTableAndGraph.setLeftComponent(this.tablePanel);
            splitPaneWithNodeListTableAndGraph.setRightComponent(chartGraphSplitPane);

            JFrame f = new JFrame("PATH FLOW EXPLORER");
            f.setBackground(Color.WHITE);
            f.setPreferredSize(new Dimension(750, 600));
            f.setLayout(new BorderLayout(3,3));
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.getContentPane().add(splitPaneWithNodeListTableAndGraph, BorderLayout.CENTER);
            f.addComponentListener(new ComponentAdapter() {
                public void componentResized(ComponentEvent evt) {
                    splitPaneWithNodeListTableAndGraph.setDividerSize(5);
                    splitPaneWithNodeListTableAndGraph.setDividerLocation((int)(f.getWidth()*0.13));

                    chartGraphSplitPane.setDividerSize(5);
                    chartGraphSplitPane.setDividerLocation((int)(f.getHeight()*0.80));

                    MySequentialGraphVars.getSequentialGraphViewer().revalidate();
                    MySequentialGraphVars.getSequentialGraphViewer().repaint();
                }
            });

            f.pack();
            f.setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void showBetweenPathFlows(int mxDepth) {
        try {
            this.mxDepth = mxDepth;
            this.createBetweenPathGraph();
            this.setMaxNodeValue();
            this.setMaxEdgeValue();
            this.setNodeSizes();
            this.setNodeLocations();
            this.setViewer();
            this.setNodeTable();

            Border blackline = BorderFactory.createLineBorder(Color.black);
            TitledBorder titledBorder = BorderFactory.createTitledBorder(blackline, "");
            titledBorder.setTitleJustification(TitledBorder.LEFT);
            titledBorder.setTitleFont(MySequentialGraphVars.tahomaBoldFont11);

            JPanel vvPanel = new JPanel();
            vvPanel.setLayout(new BorderLayout(3,3));
            vvPanel.setBackground(Color.WHITE);
            vvPanel.setBorder(titledBorder);
            vvPanel.add(this.graphViewer, BorderLayout.CENTER);

            JPanel chartPanel = new JPanel();
            chartPanel.setLayout(new GridLayout(1, 2));
            chartPanel.setBackground(Color.WHITE);
            chartPanel.add(setAverageContributionByDepthLineChart());
            chartPanel.add(setMaxContributionByDepthLineChart());
            chartPanel.add(setMinContributionByDepthLineChart());
            chartPanel.add(setStdContributionByDepthLineChart());
            chartPanel.add(setNumberOfNodesByDepthLineChart());

            JSplitPane chartGraphSplitPane = new JSplitPane();
            chartGraphSplitPane.setDividerLocation(0.80);
            chartGraphSplitPane.setDividerSize(5);
            chartGraphSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
            chartGraphSplitPane.setTopComponent(vvPanel);
            chartGraphSplitPane.setBottomComponent(chartPanel);

            JSplitPane splitPaneWithNodeListTableAndGraph = new JSplitPane();
            splitPaneWithNodeListTableAndGraph.setDividerLocation(0.13);
            splitPaneWithNodeListTableAndGraph.setDividerSize(5);
            splitPaneWithNodeListTableAndGraph.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
            splitPaneWithNodeListTableAndGraph.setLeftComponent(this.tablePanel);
            splitPaneWithNodeListTableAndGraph.setRightComponent(chartGraphSplitPane);

            JFrame f = new JFrame("PATH FLOW EXPLORER");
            f.setBackground(Color.WHITE);
            f.setPreferredSize(new Dimension(750, 600));
            f.setLayout(new BorderLayout(3,3));
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.getContentPane().add(splitPaneWithNodeListTableAndGraph, BorderLayout.CENTER);
            f.addComponentListener(new ComponentAdapter() {
                public void componentResized(ComponentEvent evt) {
                    splitPaneWithNodeListTableAndGraph.setDividerSize(5);
                    splitPaneWithNodeListTableAndGraph.setDividerLocation((int)(f.getWidth()*0.13));

                    chartGraphSplitPane.setDividerSize(5);
                    chartGraphSplitPane.setDividerLocation((int)(f.getHeight()*0.80));

                    MySequentialGraphVars.getSequentialGraphViewer().revalidate();
                    MySequentialGraphVars.getSequentialGraphViewer().repaint();
                }
            });

            f.pack();
            f.setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void showDataFlows() {
        try {
            this.createFlowGraph();
            isNotDataFlow = false;
            this.setMaxNodeValue();
            this.setMaxEdgeValue();
            this.setNodeSizes();
            this.setNodeLocations();
            this.setViewer();
            this.setNodeTable();

            Border blackline = BorderFactory.createLineBorder(Color.black);
            TitledBorder titledBorder = BorderFactory.createTitledBorder(blackline, "");
            titledBorder.setTitleJustification(TitledBorder.LEFT);
            titledBorder.setTitleFont(MySequentialGraphVars.tahomaBoldFont11);

            JPanel vvPanel = new JPanel();
            vvPanel.setLayout(new BorderLayout(3,3));
            vvPanel.setBackground(Color.WHITE);
            vvPanel.setBorder(titledBorder);
            vvPanel.add(this.graphViewer, BorderLayout.CENTER);

            JPanel chartPanel = new JPanel();
            chartPanel.setLayout(new GridLayout(1, 6));
            chartPanel.setBackground(Color.WHITE);
            chartPanel.add(setAverageContributionByDepthLineChart());
            chartPanel.add(setMaxContributionByDepthLineChart());
            chartPanel.add(setMinContributionByDepthLineChart());
            chartPanel.add(setStdContributionByDepthLineChart());
            chartPanel.add(setNumberOfNodesByDepthLineChart());
            chartPanel.add(new MyPathFlowNodesByDepthLineBarChart());

            JSplitPane chartGraphSplitPane = new JSplitPane();
            chartGraphSplitPane.setDividerSize(5);
            chartGraphSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
            chartGraphSplitPane.setTopComponent(vvPanel);
            chartGraphSplitPane.setBottomComponent(chartPanel);
            chartGraphSplitPane.setDividerLocation(0.80);
            chartGraphSplitPane.setDividerSize(5);
            chartGraphSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
            chartGraphSplitPane.setTopComponent(vvPanel);
            chartGraphSplitPane.setBottomComponent(chartPanel);

            JSplitPane splitPaneWithNodeListTableAndGraph = new JSplitPane();
            splitPaneWithNodeListTableAndGraph.setDividerLocation(0.13);
            splitPaneWithNodeListTableAndGraph.setDividerSize(5);
            splitPaneWithNodeListTableAndGraph.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
            splitPaneWithNodeListTableAndGraph.setLeftComponent(this.tablePanel);
            splitPaneWithNodeListTableAndGraph.setRightComponent(chartGraphSplitPane);

            JFrame f = new JFrame("PATH FLOW EXPLORER");
            f.setBackground(Color.WHITE);
            f.setPreferredSize(new Dimension(750, 600));
            f.setLayout(new BorderLayout(3,3));
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.getContentPane().add(splitPaneWithNodeListTableAndGraph, BorderLayout.CENTER);
            f.addComponentListener(new ComponentAdapter() {
                public void componentResized(ComponentEvent evt) {
                    splitPaneWithNodeListTableAndGraph.setDividerSize(5);
                    splitPaneWithNodeListTableAndGraph.setDividerLocation((int)(f.getWidth()*0.13));

                    chartGraphSplitPane.setDividerSize(5);
                    chartGraphSplitPane.setDividerLocation((int)(f.getHeight()*0.80));

                    MySequentialGraphVars.getSequentialGraphViewer().revalidate();
                    MySequentialGraphVars.getSequentialGraphViewer().repaint();
                }
            });

            f.pack();
            f.setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            MyFlowExplorerAnalyzer staticLayoutExample = new MyFlowExplorerAnalyzer();
            staticLayoutExample.createFlowGraph();
            staticLayoutExample.setMaxNodeValue();
            staticLayoutExample.setNodeSizes();
            staticLayoutExample.setNodeLocations();
            staticLayoutExample.setViewer();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
