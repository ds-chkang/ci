package datamining.pattern;

import datamining.graph.*;
import datamining.graph.layout.MyDirectedSparseMultigraph;
import datamining.main.MyProgressBar;
import datamining.utils.MyMathUtil;
import datamining.utils.message.MyMessageUtil;
import datamining.utils.system.MySysUtil;
import datamining.utils.system.MyVars;
import datamining.utils.table.MyTableUtil;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import org.apache.commons.collections15.Transformer;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.*;

public class MyPatternMiner2
extends JFrame
implements ActionListener {

    private ScalingControl viewScaler;
    private MyDirectedSparseMultigraph<MyPatternNode, MyPatternEdge> patternGraph;
    private StaticLayout<MyPatternNode, MyPatternEdge> layout;
    private VisualizationViewer<MyPatternNode, MyPatternEdge> vv;
    private JTextField patternSearchTxt = new JTextField();
    private JTable table;
    public float MAX_NODE_VALUE;
    public final float MAX_EDGE_STROKE = 20f;
    public final float MIN_NODE_SIZE = 22;
    public float MAX_NODE_HEIGHT = 0f;
    public float MAX_NODE_WIDTH = 0f;
    public float MAX_EDGE_VALUE;
    public long [] pathNodeContributions;
    public long [] pathNodeUniqueContributions;
    public long [] times;

    private JLabel statBoard;
    private boolean passNotFound = false;
    public Map<String, MyPatternEdge> edgeRefMap = new HashMap<>();

    public MyPatternMiner2() {
        super("SEQUENTIAL PATTERN EXPLORATION");
        try {
            this.statBoard = new JLabel();
            this.statBoard.setFont(MyVars.tahomaPlainFont12);
            this.statBoard.setBackground(Color.WHITE);
            initiGraph();
            decorate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initiGraph() {
        this.patternGraph = new MyDirectedSparseMultigraph<>();
        this.layout = new StaticLayout<>(this.patternGraph);
        this.vv = new VisualizationViewer<>(this.layout);
        this.vv.setBackground(Color.WHITE);
    }

    String [] searchItemsets;

    private JPanel setSearchPatternPanel() {
        final MyPatternMiner2 pm = this;
        JPanel searchPatternPanel = new JPanel();
        searchPatternPanel.setLayout(new BorderLayout(3,3));
        patternSearchTxt.setFont(MyVars.f_pln_12);
        patternSearchTxt.setBackground(Color.WHITE);
        patternSearchTxt.addKeyListener(new KeyAdapter() {
            public synchronized void keyTyped(KeyEvent e) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        String searchTxt = patternSearchTxt.getText().replaceAll(" ", "");
                        if (searchTxt.length() > 0) {
                            if (searchTxt.endsWith("-")) return;
                            searchItemsets = searchTxt.split("-");
                            for (int i = 0; i < searchItemsets.length; i++) { // Encode item names.
                                if (searchItemsets[i].equals("!")) continue;
                                if (MyVars.nodeNameMap.containsKey(searchItemsets[i])) {
                                    searchItemsets[i] = MyVars.nodeNameMap.get(searchItemsets[i]);
                                } else {
                                    return;
                                }
                            }

                            MyProgressBar pb = new MyProgressBar(false);
                            double work = 50/MyVars.seqs.length;
                            double workDone = 0;
                            Set<String> nodeSet = new HashSet<>();
                            for (int i = 0; i < MyVars.seqs.length; i++) {
                                int matched = 0;
                                int nextPos = 0;
                                for (int j = 0; j < MyVars.seqs[i].length; j++) {
                                    String n = MyVars.seqs[i][j].split(":")[0];
                                    if (searchItemsets[matched].equals(n)) {
                                        matched++;
                                        if (matched == 1) {nextPos = j;}
                                        if (matched == searchItemsets.length) {
                                            if ((j + 1) < MyVars.seqs[i].length) {
                                                nodeSet.add(MyVars.seqs[i][j + 1].split(":")[0]);
                                                matched = 0;
                                                j = nextPos;
                                            }
                                        }
                                    } else {
                                        matched = 0;
                                    }
                                }

                                workDone = workDone + work;
                                pb.updateValue((int) workDone, 100);
                            }

                            for (int i = table.getRowCount() - 1; i >= 0; i--) {
                                ((DefaultTableModel) table.getModel()).removeRow(i);
                            }

                            int i = 0;
                            for (String n : nodeSet) {
                                ((DefaultTableModel) table.getModel()).addRow(new String[]{"" + (++i), (n.contains("x") ? MySysUtil.decodeVariable(n) : MySysUtil.getDecodedNodeName(n))});
                                pb.updateValue(i, nodeSet.size());
                            }

                            pb.updateValue(100, 100);
                            pb.dispose();
                        } else{
                            MyProgressBar pb = new MyProgressBar(false);

                            for (int i = table.getRowCount() - 1; i >= 0; i--) {
                                ((DefaultTableModel) table.getModel()).removeRow(i);
                            }

                            int i = 0;
                            Collection<MyNode> nodes = MyVars.g.getVertices();
                            for (MyNode n : nodes) {
                                ((DefaultTableModel) table.getModel()).addRow(new String[]{"" + (++i), (n.getName().contains("x") ? MySysUtil.decodeVariable(n.getName()) : MySysUtil.getDecodedNodeName(n.getName()))});
                                pb.updateValue(i, nodes.size());
                            }
                            pb.updateValue(100, 100);
                            pb.dispose();
                        }
                    }
                }).start();
            }
            public void keyReleased(KeyEvent e) {}
            public void keyPressed(KeyEvent e) {}
        });

        JButton exBtn = new JButton("!");
        exBtn.setFont(MyVars.tahomaBoldFont12);
        exBtn.setFocusable(false);
        exBtn.setPreferredSize(new Dimension(35, 26));
        exBtn.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                if (patternSearchTxt.getText().length() == 0) {
                    patternSearchTxt.setText("!");
                } else {
                    patternSearchTxt.setText(patternSearchTxt.getText() + "-" + "!");
                }

                searchItemsets = patternSearchTxt.getText().replaceAll(" ", "").split("-");
                for (int i = 0; i < searchItemsets.length; i++) { // Encode item names.
                    if (searchItemsets[i].equals("!")) continue;
                    if (MyVars.nodeNameMap.containsKey(searchItemsets[i])) {
                        searchItemsets[i] = MyVars.nodeNameMap.get(searchItemsets[i]);
                    } else {
                        return;
                    }
                }

                Set<String> nodeSet = new HashSet<>();
                for (int i = 0; i < MyVars.seqs.length; i++) {
                    int matched = 0;
                    int nextPos = 0;
                    for (int j = 0; j < MyVars.seqs[i].length; j++) {
                        String [] n = MyVars.seqs[i][j].split(":");
                        if (searchItemsets[matched].equals("!")) {
                            matched++;
                            if (matched == 1) {nextPos = j;}
                            if (matched == searchItemsets.length) {
                                if ((j+1) < MyVars.seqs[i].length) {
                                    nodeSet.add(MyVars.seqs[i][j+1].split(":")[0]);
                                    matched = 0;
                                    j = nextPos;
                                }
                            }
                        } else if (searchItemsets[matched].equals(n[0])) {
                            matched++;
                            if (matched == 1) {nextPos = j;}
                            if (matched == searchItemsets.length) {
                                if ((j+1) < MyVars.seqs[i].length) {
                                    nodeSet.add(MyVars.seqs[i][j+1].split(":")[0]);
                                    matched = 0;
                                    j = nextPos;
                                }
                            }
                        } else {
                            matched = 0;
                        }
                    }
                }

                for (int i = table.getRowCount() - 1; i >= 0; i--) {
                    ((DefaultTableModel) table.getModel()).removeRow(i);
                }

                int i = 0;
                for (String n : nodeSet) {
                    ((DefaultTableModel) table.getModel()).addRow(new String[]{"" + (++i), (n.contains("x") ? MySysUtil.decodeVariable(n) : MySysUtil.getDecodedNodeName(n))});
                }
            }
        });

        JButton runBtn = new JButton("RUN");
        runBtn.setFont(MyVars.tahomaPlainFont12);
        runBtn.setPreferredSize(new Dimension(53, 26));
        runBtn.setFocusable(false);
        runBtn.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        MyProgressBar pb = new MyProgressBar(false);
                        try {
                            if (patternSearchTxt.getText().length() == 0) {
                                pb.updateValue(100, 100);
                                pb.dispose();
                                return;
                            }

                            searchItemsets = patternSearchTxt.getText().replaceAll(" ", "").split("-");
                            for (int i = 0; i < searchItemsets.length; i++) { // Encode item names.
                                if (searchItemsets[i].equals("!")) continue;
                                if (MyVars.nodeNameMap.containsKey(searchItemsets[i])) {
                                    searchItemsets[i] = MyVars.nodeNameMap.get(searchItemsets[i]);
                                } else {
                                    pb.updateValue(100, 100);
                                    pb.dispose();
                                    MyMessageUtil.showInfoMsg(pm, "Check the items in the pattern search string.");
                                    return;
                                }
                            }

                            //BufferedWriter bw = new BufferedWriter(new FileWriter(MySysUtil.getWorkingDir() + MySysUtil.getDirectorySlash() + "patterns.txt"));
                            times = new long[searchItemsets.length];
                            pathNodeUniqueContributions = new long[searchItemsets.length];
                            pathNodeContributions = new long[searchItemsets.length];
                            double work = 50/MyVars.seqs.length;
                            double workDone = 0;
                            String pattern = "";
                            for (int i = 0; i < MyVars.seqs.length; i++) {
                                int matched = 0;
                                int nextPos = 0;
                                boolean [] isContributionChanged = new boolean[searchItemsets.length];
                                for (int j = 0; j < MyVars.seqs[i].length; j++) {
                                    String [] n = MyVars.seqs[i][j].split(":");
                                    if (searchItemsets[matched].equals("!")) {
                                        matched++;
                                        if (matched == 1) {
                                            nextPos = j;
                                            pattern = n[0];
                                        } else {
                                            pattern += "-" + n[0];
                                        }
                                        if (matched == searchItemsets.length) {
                                            if ((j+1) < MyVars.seqs[i].length) {
                                                //bw.write(pattern + "\n");
                                                matched = 0;
                                                j = nextPos;
                                                pattern = "";
                                            }
                                        }
                                    } else if (searchItemsets[matched].equals(n[0])) {
                                        times[matched] += Long.parseLong(n[1]);
                                        pathNodeContributions[matched]++;
                                        isContributionChanged[matched] = true;
                                        matched++;
                                        if (matched == 1) {
                                            nextPos = j;
                                            pattern = n[0];
                                        } else {
                                            pattern += "-" + n[0];
                                        }
                                        if (matched == searchItemsets.length) {
                                            if ((j+1) < MyVars.seqs[i].length) {
                                                //bw.write(pattern + "\n");
                                                matched = 0;
                                                j = nextPos;
                                                pattern = "";
                                            }
                                        }
                                    } else {
                                        pattern = "";
                                        matched = 0;
                                   }
                                }

                                for (int j=0; j < isContributionChanged.length; j++) {
                                    if (isContributionChanged[j]) {
                                        pathNodeUniqueContributions[j]++;
                                    }
                                }

                                workDone = workDone + work;
                                pb.updateValue((int) workDone, 100);
                            }

                            //bw.close();

                            createGraph();
                            setMaxNodeValue();
                            pb.updateValue(60, 100);
                            setMaxEdgeValue();
                            pb.updateValue(70, 100);
                            setNodeSizes();
                            pb.updateValue(80, 100);
                            setNodeLocations();
                            pb.updateValue(90, 100);
                            setViewer();
                            vv.revalidate();
                            vv.repaint();
                            pb.updateValue(100, 100);
                            pb.dispose();
                        } catch (Exception ex) {
                            pb.updateValue(100, 100);
                            pb.dispose();
                            ex.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
       // btnPanel.add(exBtn);
        btnPanel.add(runBtn);

        searchPatternPanel.add(patternSearchTxt, BorderLayout.CENTER);
        searchPatternPanel.add(btnPanel, BorderLayout.EAST);
        return searchPatternPanel;
    }

    private void setNodeLocations() {
        Collection<MyPatternNode> nodes = this.patternGraph.getVertices();
        for (int i=1; i <= searchItemsets.length; i++) {
            LinkedHashMap<String, Long> sortedNodeMap = new LinkedHashMap<>();
            for (MyDepthNode n : nodes) {
                if (n.depth == i) {
                    sortedNodeMap.put(n.getName(), n.getContribution());
                }
            }
            sortedNodeMap = MySysUtil.sortMapByLongValue(sortedNodeMap);

            int vDepth = 0;
            for (String nn : sortedNodeMap.keySet()) {
                Point2D.Double currentLoc = new Point2D.Double();
                MyPatternNode n = (MyPatternNode) this.patternGraph.vRefs.get(nn);
                if (i == 1) {
                    currentLoc.x = (250 + (this.MAX_NODE_WIDTH/2));
                    currentLoc.y = (((this.MAX_NODE_HEIGHT / 2) + 150) * (vDepth + 1)) + (this.MAX_NODE_HEIGHT * vDepth);
                } else {
                    currentLoc.x = (250 + (this.MAX_NODE_WIDTH/2)) + ((i-1) * 350);
                    currentLoc.y = (((this.MAX_NODE_HEIGHT / 2) + 150) * (vDepth + 1)) + (this.MAX_NODE_HEIGHT * vDepth);
                }
                ++vDepth;
                layout.setLocation(n, currentLoc);
            }
        }
    }

    public void setMaxNodeValue() {
        Collection<MyPatternNode> nodes = this.patternGraph.getVertices();
        for (MyPatternNode n : nodes) {
            if (this.MAX_NODE_VALUE < n.getContribution()) {
                this.MAX_NODE_VALUE = n.getContribution();
            }
        }
    }

    public void setMaxEdgeValue() {
        Collection<MyPatternEdge> edges = this.patternGraph.getEdges();
        for (MyPatternEdge e : edges) {
            if (this.MAX_EDGE_VALUE < e.getContribution()) {
                this.MAX_EDGE_VALUE = e.getContribution();
            }
        }
    }

    private void setNodeSizes() {
        Collection<MyPatternNode> nodes = this.patternGraph.getVertices();
        for (MyPatternNode n : nodes) {
            n.nodeSize = this.setNodeSize((float) n.getContribution() / this.MAX_NODE_VALUE);
            if (n.nodeSize.getHeight() > this.MAX_NODE_HEIGHT) {
                this.MAX_NODE_HEIGHT = (float) n.nodeSize.getHeight();
            }

            if (n.nodeSize.getWidth() > this.MAX_NODE_WIDTH) {
                this.MAX_NODE_WIDTH = (float) n.nodeSize.getWidth();
            }
        }
    }

    private void createGraph() {
        for (int i = 1; i < searchItemsets.length; i++) {
            String ps = searchItemsets[i-1] + "-" + (i-1);
            String ss = searchItemsets[i] + "-" + i;
            String edgeRef = ps + "-" + ss;

            if (!this.edgeRefMap.containsKey(edgeRef)) {
                if (!this.patternGraph.vRefs.containsKey(ps)) {
                    MyPatternNode pn = new MyPatternNode(ps, i-1);
                    pn.contribution = pathNodeContributions[i-1];
                    pn.uniqueContribution = pathNodeUniqueContributions[i-1];
                    pn.time = times[i-1];
                    pn.paths++;
                    this.patternGraph.addVertex(pn);
                    this.patternGraph.vRefs.put(ps, pn);
                } else {
                    ((MyPatternNode) patternGraph.vRefs.get(ps)).contribution++;
                    ((MyPatternNode) patternGraph.vRefs.get(ps)).paths++;
                }

                if (!this.patternGraph.vRefs.containsKey(ss)) {
                    MyPatternNode sn = new MyPatternNode(ss, i);
                    sn.uniqueContribution = pathNodeUniqueContributions[i];
                    sn.time = times[i];
                    if ((i+1) == searchItemsets.length) {
                        sn.contribution = pathNodeContributions[i];
                        sn.paths++;
                    }
                    this.patternGraph.addVertex(sn);
                    this.patternGraph.vRefs.put(ss, sn);
                } else {
                    if ((i+1) == searchItemsets.length) {
                        ((MyPatternNode) patternGraph.vRefs.get(ss)).contribution = pathNodeContributions[i];
                        ((MyPatternNode) patternGraph.vRefs.get(ss)).paths++;
                    }
                    ((MyPatternNode) patternGraph.vRefs.get(ss)).contribution += pathNodeContributions[i];
                }

                MyPatternNode pn = (MyPatternNode) this.patternGraph.vRefs.get(ps);
                MyPatternNode sn = (MyPatternNode) this.patternGraph.vRefs.get(ss);
                MyPatternEdge edge = new MyPatternEdge(pn, sn);
                edge.contribution = pathNodeContributions[i];
                this.patternGraph.addEdge(edge, pn, sn);
                this.edgeRefMap.put(edgeRef, edge);
            } else {
                MyPatternNode pn = (MyPatternNode) this.patternGraph.vRefs.get(ps);
                MyPatternNode sn = (MyPatternNode) this.patternGraph.vRefs.get(ss);
                pn.paths++;
                if ((i+1) == searchItemsets.length) {
                    sn.contribution += pathNodeContributions[i];
                    sn.paths++;
                }
                MyPatternEdge edge = patternGraph.findEdge(pn, sn);
                edge.contribution += pathNodeContributions[i];
                if (!pn.getName().contains("x")) pn.contribution += pathNodeContributions[i-1];

            }
        }
    }

    private void setViewer() {
        this.vv.getRenderContext().setLabelOffset(25);
        Cursor handCursor = new Cursor(HAND_CURSOR);
        this.vv.setCursor(handCursor);
        this.vv.setBackground(Color.WHITE);
        this.vv.setDoubleBuffered(true);
        this.vv.setLayout(null);

        DefaultModalGraphMouse graphMouse = new DefaultModalGraphMouse();
        graphMouse.setMode(DefaultModalGraphMouse.Mode.PICKING);
        this.vv.setGraphMouse(graphMouse);
        this.vv.getRenderContext().setVertexFillPaintTransformer(this.nodeColorer);
        this.vv.setBackground(Color.WHITE);
        this.vv.setPreferredSize(new Dimension(10000, 10000));
        this.vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<>());
        this.vv.getRenderContext().setVertexShapeTransformer(new Transformer<MyPatternNode, Shape>() {
            @Override public Shape transform(MyPatternNode n) {return n.nodeSize;}});
        this.vv.getRenderContext().setEdgeDrawPaintTransformer(edgeDrawPainter);
        this.vv.getRenderContext().setArrowDrawPaintTransformer(edgeArrowPainter);
        this.vv.getRenderContext().setArrowFillPaintTransformer(edgeArrowPainter);
        this.vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.S);
        this.vv.getRenderContext().setEdgeStrokeTransformer(edgeStroker);
        this.vv.getRenderContext().setVertexFontTransformer(new Transformer<MyPatternNode, Font>() {
            @Override public Font transform(MyPatternNode node) {
                return new Font("Noto Sans", Font.BOLD, 20);
            }
        });
        this.vv.getRenderContext().setEdgeLabelTransformer(new Transformer<MyPatternEdge, String>() {
            @Override public String transform(MyPatternEdge e) {
                return MyMathUtil.getCommaSeperatedNumber(e.contribution);
            }
        });
        this.vv.getRenderContext().setEdgeFontTransformer(new Transformer<MyPatternEdge, Font>() {
            @Override public Font transform(MyPatternEdge e) {
                return new Font("Noto Sans", Font.BOLD, 22);
            }
        });
        this.vv.setVertexToolTipTransformer(new Transformer<MyPatternNode, String>() {
            @Override public String transform(MyPatternNode n) {
                if (MyVars.isTimeOn) {
                    return "<html><body>" +
                            (n.getName().split("-")[0].contains("x") ? MySysUtil.decodeVariable(n.getName().split("-")[0]) : MySysUtil.getDecodedNodeName(n.getName().split("-")[0])) +
                            "<br>GRAPH CONT.: " + MyMathUtil.getCommaSeperatedNumber(((MyNode) MyVars.g.vRefs.get(n.getName().split("-")[0])).getContribution()) +
                            "<br>PATH CONT.: " + MyMathUtil.getCommaSeperatedNumber(n.contribution) +
                            "<br>U. CONT.:" + MyMathUtil.getCommaSeperatedNumber(n.uniqueContribution) + "[" + MyMathUtil.twoDecimalFormat(((double) n.contribution / MyVars.seqs.length) * 100) + "%]" +
                            "<br>TIME: " + MyMathUtil.getCommaSeperatedNumber(n.time) +
                            "<br>NO.OF PATHS: " + MyMathUtil.getCommaSeperatedNumber(n.paths) +
                            "</body></html>";
                } else {
                    return "<html><body>" +
                            (n.getName().split("-")[0].contains("x") ? MySysUtil.decodeVariable(n.getName().split("-")[0]) : MySysUtil.getDecodedNodeName(n.getName().split("-")[0])) +
                            "<br>GRAPH CONT.: " + MyMathUtil.getCommaSeperatedNumber(((MyNode) MyVars.g.vRefs.get(n.getName().split("-")[0])).getContribution()) +
                            "<br>PATH CONT.: " + MyMathUtil.getCommaSeperatedNumber(n.contribution) +
                            "<br>U. CONT.:" + MyMathUtil.getCommaSeperatedNumber(n.uniqueContribution) + "[" + MyMathUtil.twoDecimalFormat(((double) n.contribution / MyVars.seqs.length) * 100) + "%]" +
                            "<br>TIME: " + MyMathUtil.getCommaSeperatedNumber(n.time) +
                            "<br>NO.OF PATHS: " + MyMathUtil.getCommaSeperatedNumber(n.paths) +
                            "</body></html>";
                }
            }
        });
        this.vv.getRenderContext().setVertexLabelTransformer(new Transformer<MyPatternNode, String>() {
            @Override public String transform(MyPatternNode n) {
                if (MyVars.isTimeOn) {
                    return "<html><body>" +
                            (n.getName().split("-")[0].contains("x") ? MySysUtil.decodeVariable(n.getName().split("-")[0]) : MySysUtil.getDecodedNodeName(n.getName().split("-")[0])) +
                            "<br>GRAPH CONT.: " + MyMathUtil.getCommaSeperatedNumber(((MyNode) MyVars.g.vRefs.get(n.getName().split("-")[0])).getContribution()) +
                            "<br>PATH CONT.: " + MyMathUtil.getCommaSeperatedNumber(n.contribution) +
                            "<br>U. CONT.:" + MyMathUtil.getCommaSeperatedNumber(n.uniqueContribution) + "[" + MyMathUtil.twoDecimalFormat(((double) n.contribution / MyVars.seqs.length) * 100) + "%]" +
                            "<br>TIME: " + MyMathUtil.getCommaSeperatedNumber(n.time) +
                            "<br>NO.OF PATHS: " + MyMathUtil.getCommaSeperatedNumber(n.paths) +
                            "</body></html>";
                } else {
                    return "<html><body>" +
                            (n.getName().split("-")[0].contains("x") ? MySysUtil.decodeVariable(n.getName().split("-")[0]) : MySysUtil.getDecodedNodeName(n.getName().split("-")[0])) +
                            "<br>GRAPH CONT.: " + MyMathUtil.getCommaSeperatedNumber(((MyNode) MyVars.g.vRefs.get(n.getName().split("-")[0])).getContribution()) +
                            "<br>PATH CONT.: " + MyMathUtil.getCommaSeperatedNumber(n.contribution) +
                            "<br>U. CONT.:" + MyMathUtil.getCommaSeperatedNumber(n.uniqueContribution) + "[" + MyMathUtil.twoDecimalFormat(((double) n.contribution / MyVars.seqs.length) * 100) + "%]" +
                            "<br>TIME: " + MyMathUtil.getCommaSeperatedNumber(n.time) +
                            "<br>NO.OF PATHS: " + MyMathUtil.getCommaSeperatedNumber(n.paths) +
                            "</body></html>";
                }
            }
        });
        this.vv.getRenderContext().setVertexDrawPaintTransformer(new Transformer<MyPatternNode, Paint>() {
            @Override public Paint transform(MyPatternNode n) {
                return Color.BLACK;
            }
        });
        this.vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.QuadCurve<MyPatternNode, MyPatternEdge>());
        this.scale();
    }

    private Transformer<MyPatternEdge, Paint> edgeArrowPainter = new Transformer<MyPatternEdge, Paint>() {
        @Override public Paint transform(MyPatternEdge e) {
            return new Color(0, 0, 0, 0f);
        }
    };

    private Transformer<MyPatternEdge, Paint> edgeDrawPainter = new Transformer<MyPatternEdge, Paint>() {
        @Override public Paint transform(MyPatternEdge e) {
            return new Color(0f, 0f, 0f, 0.1f);
        }
    };

    private Transformer<MyPatternNode, Paint> nodeColorer = new Transformer<MyPatternNode, Paint>() {
        @Override public Paint transform(MyPatternNode n) {
            if (patternGraph.getSuccessorCount(n) == 0 && patternGraph.getPredecessorCount(n) > 0) {
                return Color.GREEN;
            } else if (patternGraph.getSuccessorCount(n) > 0 && patternGraph.getPredecessorCount(n) == 0) {
                return Color.RED;
            } else {
                return Color.BLUE;
            }
        }
    };

    private Transformer<MyPatternEdge, Stroke> edgeStroker = new Transformer<MyPatternEdge, Stroke>() {
        @Override public Stroke transform(MyPatternEdge e) {
            float edgeStrokeWeight = ((float)e.contribution)/MAX_EDGE_VALUE;
            return new BasicStroke(edgeStrokeWeight * MAX_EDGE_STROKE);
        }
    };

    private JSplitPane setContentPanel() {
        JPanel contentPanel = new JPanel();
        Border blackline = BorderFactory.createLineBorder(Color.black);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(blackline, "");
        titledBorder.setTitleJustification(TitledBorder.LEFT);
        titledBorder.setTitleFont(new Font("Arial", Font.PLAIN, 0));
        titledBorder.setTitleColor(Color.DARK_GRAY);

        contentPanel.setBackground(Color.WHITE);
        contentPanel.setLayout(new BorderLayout(3,3));
        contentPanel.add(setSearchPatternPanel(), BorderLayout.NORTH);
        vv.setBorder(titledBorder);
        contentPanel.add(vv, BorderLayout.CENTER);
        contentPanel.add(this.statBoard, BorderLayout.SOUTH);

        String [] columns = {"NO.", "NODE"};
        String [][] data = {};

        DefaultTableModel m = new DefaultTableModel(data, columns);
        this.table = new JTable(m);
        table.getTableHeader().setFont(MyVars.tahomaBoldFont12);
        table.getTableHeader().setOpaque(false);
        table.getTableHeader().setBackground(new Color(0,0,0,0));
        table.getColumnModel().getColumn(0).setPreferredWidth(45);
        table.getColumnModel().getColumn(0).setMaxWidth(55);
        table.setRowHeight(20);
        table.setFont(MyVars.f_pln_11);
        table.setBackground(Color.WHITE);
        Collection<MyNode> nodes = MyVars.g.getVertices();
        int i=0;
        for (MyNode n : nodes) {
            m.addRow(new String[]{""+(++i), (n.getName().contains("x") ? MySysUtil.decodeVariable(n.getName()): MySysUtil.getDecodedNodeName(n.getName()))});
        }

        JButton selectBtn = new JButton("SEL.");
        selectBtn.setFont(MyVars.tahomaPlainFont12);

        JTextField searchNodeTxt = new JTextField();
        searchNodeTxt.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout(3,3));
        tablePanel.setBackground(Color.WHITE);
        JPanel bottomPanel = MyTableUtil.searchAndSelectPanelForJTable(this, searchNodeTxt, selectBtn, m, table);
        selectBtn.setPreferredSize(new Dimension(53, 25));
        selectBtn.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                if (table.getSelectedRow() == -1) return;
                String searchTxt = patternSearchTxt.getText();
                if (searchTxt.length() > 0) {
                    if (searchTxt.endsWith("-")) {
                        patternSearchTxt.setText(searchTxt + table.getValueAt(table.getSelectedRow(), 1).toString());
                    } else {
                        patternSearchTxt.setText(searchTxt + "-" + table.getValueAt(table.getSelectedRow(), 1).toString());
                    }

                    searchItemsets = patternSearchTxt.getText().replaceAll(" ", "").split("-");
                    for (int i = 0; i < searchItemsets.length; i++) {
                        if (searchItemsets[i].equals("!")) continue;
                        searchItemsets[i] = MyVars.nodeNameMap.get(searchItemsets[i]);
                    }
                } else {
                    searchItemsets = new String[1];
                    searchItemsets[0] = MyVars.nodeNameMap.get(table.getValueAt(table.getSelectedRow(), 1).toString());
                    patternSearchTxt.setText(table.getValueAt(table.getSelectedRow(), 1).toString());
                }

                for (int i = table.getRowCount() - 1; i >= 0; i--) {
                    ((DefaultTableModel) table.getModel()).removeRow(i);
                }

                Set<String> nodeSet = new HashSet<>();
                for (int i=0; i < MyVars.seqs.length; i++) {
                    int matched = 0;
                    int nextPos = -1;
                    for (int j=0; j < MyVars.seqs[i].length; j++) {
                        String n = MyVars.seqs[i][j].split(":")[0];
                        if (searchItemsets[matched].equals("!")) {
                            matched++;
                            if (matched == 1) {nextPos = j;}
                            if (matched == searchItemsets.length) {
                                if ((j+1) < MyVars.seqs[i].length) {
                                    nodeSet.add(MyVars.seqs[i][j+1].split(":")[0]);
                                    matched = 0;
                                    j = nextPos;
                                }
                            }
                        } else if (searchItemsets[matched].equals(n)) {
                            matched++;
                            if (matched == 1) {nextPos = j;}
                            if (matched == searchItemsets.length) {
                                if ((j+1) < MyVars.seqs[i].length) {
                                    String s = MyVars.seqs[i][j+1].split(":")[0];
                                    nodeSet.add(s);
                                    j = nextPos;
                                    matched = 0;
                                }
                            }
                        } else {
                            matched = 0;
                        }
                    }
                }

                int i=0;
                for (String n : nodeSet) {
                    m.addRow(new String[]{""+(++i), (n.contains("x") ? MySysUtil.decodeVariable(n): MySysUtil.getDecodedNodeName(n))});
                }
            }
        });
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
        tableScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 8));
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);
        tablePanel.add(bottomPanel, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane();
        splitPane.setDividerLocation(0.13);
        splitPane.setDividerSize(5);
        splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(tablePanel);
        splitPane.setRightComponent(contentPanel);
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
                splitPane.setDividerSize(4);
                splitPane.setDividerLocation((int)(MySysUtil.getViewerWidth()*0.13));
            }
        });
        return splitPane;
    }

    private void scale() {
        this.viewScaler = new CrossoverScalingControl();
        double amount = -1.0;
        this.viewScaler.scale(this.vv, amount > 0 ? 2.0f : 1 / 2.5f, new Point2D.Double(0, 0));
    }

    private void decorate() {
        this.setLayout(new BorderLayout(3,3));
        this.setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(850, 650));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.getContentPane().add(this.setContentPanel(), BorderLayout.CENTER);
        this.setAlwaysOnTop(true);
        this.pack();
        this.setVisible(true);
        this.setAlwaysOnTop(false);
    }

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

    @Override public void actionPerformed(ActionEvent e) {}
}
