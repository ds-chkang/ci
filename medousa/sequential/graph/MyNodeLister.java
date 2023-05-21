package medousa.sequential.graph;

import medousa.MyProgressBar;
import medousa.message.MyMessageUtil;
import medousa.sequential.utils.MyViewerComponentControllerUtil;
import medousa.sequential.utils.MySequentialGraphSysUtil;
import medousa.sequential.utils.MySequentialGraphVars;
import medousa.table.MyTableUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class MyNodeLister
extends JFrame
implements ActionListener {

    private JTable table;
    private DefaultTableModel tableModel;
    private String [] columns = {"NO.", "NODE"};
    private String [][] data = {};
    private JTextField nodeSearchTxt = new JTextField();
    private JButton nodeSelectBtn = new JButton("SELECT");
    private MyProgressBar pb = new MyProgressBar(false);
    private JTextField focusedTxt;
    private JFrame f;
    private int whatToDo = 0;
    private Map<String, String> itemMap;

    public MyNodeLister() {
        super("NODE SEARCH");
        this.setNodes();
        this.decorate();
    }

    public MyNodeLister(String frameTitle) {
        super(frameTitle);
        this.setNodes();
        this.decorate();
        this.whatToDo = 3;  // End node search.
    }

    public MyNodeLister(JTextField txt, JFrame f) {
        super("NODE SEARCH");
        this.f = f;
        this.focusedTxt = txt;
        this.setNodes();
        this.decorate();
        this.whatToDo = 1; // From MyBetweenNodePropertyFrinder search node.
    }

    public MyNodeLister(JTextField txt1, JTextField txt2, JFrame f) {
        super("NODE SEARCH");
        this.f = f;
        this.focusedTxt = txt2;
        this.setNodes(txt1);
        this.decorate();
        this.whatToDo = 2;
    }

    private void decorate() {
        this.setLayout(new BorderLayout(5,5));
        this.setBackground(Color.WHITE);
        this.table.getTableHeader().setOpaque(false);
        this.table.getTableHeader().setBackground(new Color(0,0,0,0));
        this.table.setRowHeight(24);
        this.table.setBackground(Color.WHITE);
        this.table.setFont(MySequentialGraphVars.f_pln_12);
        this.table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        this.table.getColumnModel().getColumn(0).setPreferredWidth(70);
        this.table.getColumnModel().getColumn(1).setPreferredWidth(400);
        this.nodeSearchTxt.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        JPanel searchAndSavePanel = MyTableUtil.searchAndSaveDataPanelForJTable2(this, this.nodeSearchTxt, this.nodeSelectBtn, this.tableModel, this.table);
        this.nodeSearchTxt.setFont(MySequentialGraphVars.f_bold_12);
        this.add(searchAndSavePanel, BorderLayout.SOUTH);
        this.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.table.setRowSorter(MyTableUtil.setJTableRowSorterWithTextField(this.tableModel, this.nodeSearchTxt));
        this.table.setFocusable(false);
        this.table.getTableHeader().setFont(MySequentialGraphVars.tahomaBoldFont12);
        this.nodeSelectBtn.setFocusable(false);
        this.nodeSelectBtn.setPreferredSize(new Dimension(80, 29));
        this.nodeSelectBtn.addActionListener(this);
        JScrollPane tableScrollPane = new JScrollPane(this.table);
        this.getContentPane().add(tableScrollPane, BorderLayout.CENTER);
        this.setPreferredSize(new Dimension(480, 700));
        this.pack();
        pb.updateValue(100, 100);
        pb.dispose();
        this.setAlwaysOnTop(true);
        this.setVisible(true);
        this.setAlwaysOnTop(false);
    }

    private void setNodes() {
        this.itemMap = new HashMap<>();
        this.tableModel = new DefaultTableModel(this.data, this.columns);
        this.table = new JTable(this.tableModel);
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        int cnt = 0;
        for (MyNode n : nodes) {
            if (MySequentialGraphVars.getSequentialGraphViewer().multiNodes != null && MySequentialGraphVars.getSequentialGraphViewer().multiNodes.size() > 0 && MySequentialGraphVars.getSequentialGraphViewer().multiNodes.contains(n)) continue;
            else if (MySequentialGraphVars.getSequentialGraphViewer().selectedNode == n) continue;
            else if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthSelecter.getSelectedIndex() > 0 && n.getCurrentValue() == 0) continue;
            String decodedNode = MySequentialGraphSysUtil.decodeNodeName(n.getName());
            this.tableModel.addRow(new String[]{String.valueOf(++cnt), decodedNode});
            this.itemMap.put(decodedNode, n.getName());
            pb.updateValue(cnt, nodes.size());
        }
    }

    private void setNodes(JTextField txt) { // txtID is just used for a flag to differentiate from the above method.
        this.itemMap = new HashMap<>();
        this.tableModel = new DefaultTableModel(this.data, this.columns);
        this.table = new JTable(this.tableModel);
        int cnt = 0;
        String predecessor = MySequentialGraphSysUtil.encodeItemSet(txt.getText().trim());
        if (predecessor.equals("")) {predecessor = MySequentialGraphSysUtil.encodeVariableItemSet(txt.getText().trim());}
        pb.updateValue(20, 100);
        Set<String> successors = new HashSet<>();
        for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
            for (int i = 0; i < MySequentialGraphVars.seqs[s].length; i++) {
                String itemset = MySequentialGraphVars.seqs[s][i].split(":")[0];
                if (itemset.equals(predecessor)) {
                    for (i=i+1; i < MySequentialGraphVars.seqs[s].length; i++) {
                        String ss = MySequentialGraphVars.seqs[s][i].split(":")[0];
                        if (!successors.contains(ss)) {
                            String decodedNode = MySequentialGraphSysUtil.decodeNodeName(ss);
                            this.itemMap.put(decodedNode, ss);
                            this.tableModel.addRow(new String[]{String.valueOf(++cnt), decodedNode});
                            successors.add(ss);
                        }
                    }
                    break;
                }
            }
        }
        pb.updateValue(90, 100);
    }

    private void setSelectedNodes() {
        String nodeName = itemMap.get(table.getValueAt(table.getSelectedRow(), 1).toString());
        MyNode n = (MyNode) MySequentialGraphVars.g.vRefs.get(nodeName);
        dispose();
        MyProgressBar pb = new MyProgressBar(false);
        if (MySequentialGraphVars.getSequentialGraphViewer().selectedNode != null) {
            pb.updateValue(10, 100);
            MySequentialGraphVars.getSequentialGraphViewer().multiNodes = new HashSet<>();
            MySequentialGraphVars.getSequentialGraphViewer().multiNodes.add(MySequentialGraphVars.getSequentialGraphViewer().selectedNode);
            pb.updateValue(20, 100);
            MySequentialGraphVars.getSequentialGraphViewer().multiNodes.add(n);
            pb.updateValue(30, 100);
            MySequentialGraphVars.getSequentialGraphViewer().multiNodePredecessors.addAll(MySequentialGraphVars.g.getPredecessors(n));
            pb.updateValue(40, 100);
            MySequentialGraphVars.getSequentialGraphViewer().multiNodeSuccessors.addAll(MySequentialGraphVars.g.getSuccessors(n));
            pb.updateValue(50, 100);
            MySequentialGraphVars.getSequentialGraphViewer().multiNodePredecessors.addAll(MySequentialGraphVars.getSequentialGraphViewer().selectedSingleNodePredecessors);
            pb.updateValue(60, 100);
            MySequentialGraphVars.getSequentialGraphViewer().multiNodeSuccessors.addAll(MySequentialGraphVars.getSequentialGraphViewer().selectedSingleNodeSuccessors);
            pb.updateValue(70, 100);
            MySequentialGraphVars.getSequentialGraphViewer().selectedSingleNodeSuccessors = new HashSet<>();
            pb.updateValue(80, 100);
            MySequentialGraphVars.getSequentialGraphViewer().selectedSingleNodePredecessors = new HashSet<>();
            MySequentialGraphVars.getSequentialGraphViewer().selectedNode = null;

            pb.updateValue(90, 100);
            MySequentialGraphVars.getSequentialGraphViewer().vc.vTxtStat.setTextStatistics();
            MyViewerComponentControllerUtil.removeBarChartsFromViewer();
            MySequentialGraphSysUtil.setSharedPredecessors(MySequentialGraphVars.getSequentialGraphViewer().selectedNode, n);
            MySequentialGraphSysUtil.setSharedSuccessors(MySequentialGraphVars.getSequentialGraphViewer().selectedNode, n);
            MySequentialGraphVars.sequentialGraphDashBoard.setMultiNodeDashBoard();
            MyViewerComponentControllerUtil.setSharedNodeLevelNodeValueBarChart();
            pb.updateValue(95, 100);
        } else if (MySequentialGraphVars.getSequentialGraphViewer().multiNodes != null && MySequentialGraphVars.getSequentialGraphViewer().multiNodes.size() > 0) {
            MySequentialGraphVars.getSequentialGraphViewer().multiNodes.add(n);
            pb.updateValue(10, 100);
            MySequentialGraphVars.getSequentialGraphViewer().multiNodePredecessors.addAll(MySequentialGraphVars.g.getPredecessors(n));
            pb.updateValue(20, 100);
            MySequentialGraphVars.getSequentialGraphViewer().multiNodeSuccessors.addAll(MySequentialGraphVars.g.getSuccessors(n));
            pb.updateValue(30, 100);
            MySequentialGraphVars.getSequentialGraphViewer().vc.vTxtStat.setTextStatistics();
            pb.updateValue(40, 100);
            MyViewerComponentControllerUtil.removeBarChartsFromViewer();
            pb.updateValue(50, 100);
            MySequentialGraphSysUtil.setSharedPredecessors(n);
            pb.updateValue(60, 100);
            MySequentialGraphSysUtil.setSharedSuccessors(n);
            pb.updateValue(70, 100);
            MySequentialGraphVars.sequentialGraphDashBoard.setMultiNodeDashBoard();
            MyViewerComponentControllerUtil.setSharedNodeLevelNodeValueBarChart();
            pb.updateValue(95, 100);

        } else {
            MySequentialGraphVars.getSequentialGraphViewer().selectedNode = n;
            pb.updateValue(10, 100);
            MySequentialGraphVars.getSequentialGraphViewer().selectedSingleNodeSuccessors = new HashSet<>();
            pb.updateValue(20, 100);
            MySequentialGraphVars.getSequentialGraphViewer().selectedSingleNodePredecessors = new HashSet<>();
            pb.updateValue(30, 100);
            MySequentialGraphVars.getSequentialGraphViewer().selectedSingleNodePredecessors.addAll(MySequentialGraphVars.g.getPredecessors(n));
            pb.updateValue(40, 100);
            MySequentialGraphVars.getSequentialGraphViewer().selectedSingleNodeSuccessors.addAll(MySequentialGraphVars.g.getSuccessors(n));
            pb.updateValue(50, 100);
            MySequentialGraphVars.getSequentialGraphViewer().vc.vTxtStat.setTextStatistics();
            pb.updateValue(60, 100);
            MyViewerComponentControllerUtil.setDepthOptionForSelectedNode();
            pb.updateValue(70, 100);
            MyViewerComponentControllerUtil.removeBarChartsFromViewer();
            pb.updateValue(80, 100);
            MyViewerComponentControllerUtil.setSelectedNodeNeighborValueBarChartToViewer();
            MySequentialGraphVars.sequentialGraphDashBoard.setSingleNodeDashBoard();
            pb.updateValue(95, 100);
        }
        MySequentialGraphVars.app.getSequentialGraphDashboard().revalidate();
        MySequentialGraphVars.app.getSequentialGraphDashboard().repaint();
        pb.updateValue(100, 100);
        pb.dispose();
    }

    @Override public void actionPerformed(ActionEvent e) {
        new Thread(new Runnable() {@Override
            public void run() {
                if (whatToDo == 0) {
                    setSelectedNodes();
                } else if (whatToDo == 1) {
                    nodeSelectBtn.setEnabled(false);
                    String nodeName = (String)table.getValueAt(table.getSelectedRow(), table.getSelectedColumn());
                    focusedTxt.setText("");
                    focusedTxt.setText(nodeName);
                    dispose();
                    f.setAlwaysOnTop(true);
                    f.setAlwaysOnTop(false);
                }  else if (whatToDo == 2) {
                    nodeSelectBtn.setEnabled(false);
                    String nodeName = table.getValueAt(table.getSelectedRow(), table.getSelectedColumn()).toString();
                    focusedTxt.setText(nodeName);
                    dispose();
                    f.setAlwaysOnTop(true);
                    f.setAlwaysOnTop(false);
                } else if (whatToDo == 3) { // Find end nodes.
                    setVisible(false);
                    MyProgressBar pb = new MyProgressBar(false);
                    int cnt = 0;
                    int maxStrength = 0;
                    Map<String, Integer> endNodesMap = new HashMap<>();
                    String startNodeName = itemMap.get(table.getValueAt(table.getSelectedRow(), table.getSelectedColumn()).toString());
                    endNodesMap.put(startNodeName, 0);
                    for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                        for (int i = 0; i < MySequentialGraphVars.seqs[s].length-1; i++) {
                            String itemset = MySequentialGraphVars.seqs[s][i].split(":")[0];
                            if (itemset.equals(startNodeName)) {
                                String endItemset = MySequentialGraphVars.seqs[s][MySequentialGraphVars.seqs[s].length-1].split(":")[0];
                                if (endNodesMap.containsKey(endItemset)) {endNodesMap.put(endItemset, endNodesMap.get(endItemset)+1);}
                                else {endNodesMap.put(endItemset, 1);}
                                if (maxStrength < endNodesMap.get(endItemset)) {maxStrength = endNodesMap.get(endItemset);}
                                break;
                            }
                        }
                        pb.updateValue(++cnt, MySequentialGraphVars.sequeceFeatureCount);
                    }
                    if (endNodesMap.size() == 1) {
                        pb.updateValue(100, 100);
                        pb.dispose();
                        setVisible(false);
                        MyMessageUtil.showInfoMsg("There is no end rootNode for the selected rootNode!");
                        dispose();
                        return;
                    }
                    if (MySequentialGraphVars.g.edRefs == null) {
                        MySequentialGraphVars.g.edRefs = new HashSet();
                    }
                    MyNode startNode = (MyNode) MySequentialGraphVars.g.vRefs.get(startNodeName);
                    MySequentialGraphVars.getSequentialGraphViewer().startNode = startNode;
                    Collection<MyEdge> rootOutEdges = MySequentialGraphVars.g.getOutEdges(startNode);
                    Collection<MyEdge> edges = new ArrayList<>(MySequentialGraphVars.g.getEdges());
                    for (MyEdge edge : edges) {
                        if (endNodesMap.keySet().contains(edge.getDest().getName()) && !edge.getDest().getName().equals(startNodeName)) {
                            if (!rootOutEdges.contains(edge)) {
                                MyEdge newEdge = new MyEdge((MyNode) MySequentialGraphVars.g.vRefs.get(startNodeName), edge.getDest());
                                String edgeName = newEdge.getSource().getName() + "-" + newEdge.getDest().getName();
                                if (!MySequentialGraphVars.g.edRefs.contains(edgeName)) {
                                    MySequentialGraphVars.g.addEdge(newEdge, newEdge.getSource(), newEdge.getDest());
                                    MySequentialGraphVars.g.edRefs.add(edgeName);
                                }
                                float edgeValue = (float)endNodesMap.get(newEdge.getDest().getName())/maxStrength;
                                newEdge.setCurrentValue(edgeValue);
                            } else {
                                float edgeValue = (float)endNodesMap.get(edge.getDest().getName())/maxStrength;
                                edge.setCurrentValue(edgeValue);
                            }
                        }
                    }
                    if (endNodesMap.get(startNodeName) > 0) {
                        boolean recursiveExists = false;
                        Collection<MyEdge> outEdges = MySequentialGraphVars.g.getOutEdges(startNodeName);
                        for (MyEdge outEdge : outEdges) {
                            if (outEdge.getDest() == startNode && outEdge.getSource() == startNode) {
                                recursiveExists = true;
                                float edgeValue = (float)endNodesMap.get(outEdge.getDest().getName())/maxStrength;
                                outEdge.setCurrentValue(edgeValue);
                                break;
                            }
                        }
                        if (!recursiveExists) {
                            MyEdge newEdge = new MyEdge(startNode, startNode);
                            String edgeName = newEdge.getSource().getName() + "-" + newEdge.getDest().getName();
                            MySequentialGraphVars.g.addEdge(newEdge, newEdge.getSource(), newEdge.getDest());
                            MySequentialGraphVars.g.edRefs.add(edgeName);
                            float edgeValue = (float)endNodesMap.get(newEdge.getDest().getName())/maxStrength;
                            newEdge.setCurrentValue(edgeValue);
                        }
                    }
                    MySequentialGraphVars.getSequentialGraphViewer().endNodesMap = endNodesMap;
                    MySequentialGraphVars.g.MX_E_VAL = Float.valueOf(maxStrength);
                    pb.updateValue(100, 100);
                    pb.dispose();
                    MySequentialGraphVars.getSequentialGraphViewer().revalidate();
                    MySequentialGraphVars.getSequentialGraphViewer().repaint();
                    dispose();
                }
            }
        }).start();
    }
}
