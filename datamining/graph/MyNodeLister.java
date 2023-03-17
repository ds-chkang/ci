package datamining.graph;

import datamining.main.MyProgressBar;
import datamining.utils.MyViewerControlComponentUtil;
import datamining.utils.message.MyMessageUtil;
import datamining.utils.system.MySysUtil;
import datamining.utils.system.MyVars;
import datamining.utils.table.MyTableUtil;

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
        this.table.setRowHeight(25);
        this.table.setBackground(Color.WHITE);
        this.table.setFont(MyVars.f_pln_12);
        this.table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        this.table.getColumnModel().getColumn(0).setPreferredWidth(70);
        this.table.getColumnModel().getColumn(1).setPreferredWidth(400);
        this.nodeSearchTxt.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        JPanel searchAndSavePanel = MyTableUtil.searchAndSaveDataPanelForJTable2(this, this.nodeSearchTxt, this.nodeSelectBtn, this.tableModel, this.table);
        this.nodeSearchTxt.setFont(MyVars.f_bold_12);
        this.add(searchAndSavePanel, BorderLayout.SOUTH);
        this.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.table.setRowSorter(MyTableUtil.setJTableRowSorterWithTextField(this.tableModel, this.nodeSearchTxt));
        this.table.setFocusable(false);
        this.table.getTableHeader().setFont(MyVars.tahomaBoldFont12);
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
        Collection<MyNode> nodes = MyVars.g.getVertices();
        int cnt = 0;
        for (MyNode n : nodes) {
            if (MyVars.getViewer().multiNodes != null && MyVars.getViewer().multiNodes.size() > 0 && MyVars.getViewer().multiNodes.contains(n)) continue;
            else if (MyVars.getViewer().selectedNode == n) continue;
            else if (MyVars.getViewer().vc.depthSelecter.getSelectedIndex() > 0 && n.getCurrentValue() == 0) continue;
            String decodedNode = MySysUtil.decodeNodeName(n.getName());
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
        String predecessor = MySysUtil.encodeItemSet(txt.getText().trim());
        if (predecessor.equals("")) {predecessor = MySysUtil.encodeVariableItemSet(txt.getText().trim());}
        pb.updateValue(20, 100);
        Set<String> successors = new HashSet<>();
        for (int s=0; s < MyVars.seqs.length; s++) {
            for (int i=0; i < MyVars.seqs[s].length; i++) {
                String itemset = MyVars.seqs[s][i].split(":")[0];
                if (itemset.equals(predecessor)) {
                    for (i=i+1; i < MyVars.seqs[s].length; i++) {
                        String ss = MyVars.seqs[s][i].split(":")[0];
                        if (!successors.contains(ss)) {
                            String decodedNode = MySysUtil.decodeNodeName(ss);
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
        MyNode n = (MyNode)MyVars.g.vRefs.get(nodeName);
        dispose();
        MyProgressBar pb = new MyProgressBar(false);
        if (MyVars.getViewer().selectedNode != null) {
            synchronized (MyVars.getViewer().multiNodes) {
                pb.updateValue(10, 100);
                MyVars.getViewer().multiNodes = new HashSet<>();
                MyVars.getViewer().multiNodes.add(MyVars.getViewer().selectedNode);
                pb.updateValue(20, 100);
                MyVars.getViewer().multiNodes.add(n);
                pb.updateValue(30, 100);
                MyVars.getViewer().multiNodePredecessors.addAll(MyVars.g.getPredecessors(n));
                pb.updateValue(40, 100);
                MyVars.getViewer().multiNodeSuccessors.addAll(MyVars.g.getSuccessors(n));
                pb.updateValue(50, 100);
                MyVars.getViewer().multiNodePredecessors.addAll(MyVars.getViewer().selectedSingleNodePredecessors);
                pb.updateValue(60, 100);
                MyVars.getViewer().multiNodeSuccessors.addAll(MyVars.getViewer().selectedSingleNodeSuccessors);
                pb.updateValue(70, 100);
                MyVars.getViewer().selectedSingleNodeSuccessors = new HashSet<>();
                pb.updateValue(80, 100);
                MyVars.getViewer().selectedSingleNodePredecessors = new HashSet<>();
                MyVars.getViewer().selectedNode = null;
            }
            pb.updateValue(90, 100);
            MyVars.getViewer().vc.vTxtStat.setTextStatistics();
            MyViewerControlComponentUtil.removeBarChartsFromViewer();
            MySysUtil.setSharedPredecessors(MyVars.getViewer().selectedNode, n);
            MySysUtil.setSharedSuccessors(MyVars.getViewer().selectedNode, n);
            MyVars.dashBoard.setMultiNodeDashBoard();
            MyViewerControlComponentUtil.setSharedNodeLevelNodeValueBarChart();
            pb.updateValue(95, 100);
        } else if (MyVars.getViewer().multiNodes != null && MyVars.getViewer().multiNodes.size() > 0) {
            MyVars.getViewer().multiNodes.add(n);
            pb.updateValue(10, 100);
            MyVars.getViewer().multiNodePredecessors.addAll(MyVars.g.getPredecessors(n));
            pb.updateValue(20, 100);
            MyVars.getViewer().multiNodeSuccessors.addAll(MyVars.g.getSuccessors(n));
            pb.updateValue(30, 100);
            MyVars.getViewer().vc.vTxtStat.setTextStatistics();
            pb.updateValue(40, 100);
            MyViewerControlComponentUtil.removeBarChartsFromViewer();
            pb.updateValue(50, 100);
            MySysUtil.setSharedPredecessors(n);
            pb.updateValue(60, 100);
            MySysUtil.setSharedSuccessors(n);
            pb.updateValue(70, 100);
            MyVars.dashBoard.setMultiNodeDashBoard();
            MyViewerControlComponentUtil.setSharedNodeLevelNodeValueBarChart();
            pb.updateValue(95, 100);

        } else {
            MyVars.getViewer().selectedNode = n;
            pb.updateValue(10, 100);
            MyVars.getViewer().selectedSingleNodeSuccessors = new HashSet<>();
            pb.updateValue(20, 100);
            MyVars.getViewer().selectedSingleNodePredecessors = new HashSet<>();
            pb.updateValue(30, 100);
            MyVars.getViewer().selectedSingleNodePredecessors.addAll(MyVars.g.getPredecessors(n));
            pb.updateValue(40, 100);
            MyVars.getViewer().selectedSingleNodeSuccessors.addAll(MyVars.g.getSuccessors(n));
            pb.updateValue(50, 100);
            MyVars.getViewer().vc.vTxtStat.setTextStatistics();
            pb.updateValue(60, 100);
            MyViewerControlComponentUtil.setDepthOptionForSelectedNode();
            pb.updateValue(70, 100);
            MyViewerControlComponentUtil.removeBarChartsFromViewer();
            pb.updateValue(80, 100);
            MyViewerControlComponentUtil.setSelectedNodeNeighborValueBarChartToViewer();
            MyVars.dashBoard.setSingleNodeDashBoard();
            pb.updateValue(95, 100);
        }
        MyVars.app.getDashboard().revalidate();
        MyVars.app.getDashboard().repaint();
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
                    for (int s=0; s < MyVars.seqs.length; s++) {
                        for (int i=0; i < MyVars.seqs[s].length-1; i++) {
                            String itemset = MyVars.seqs[s][i].split(":")[0];
                            if (itemset.equals(startNodeName)) {
                                String endItemset = MyVars.seqs[s][MyVars.seqs[s].length-1].split(":")[0];
                                if (endNodesMap.containsKey(endItemset)) {endNodesMap.put(endItemset, endNodesMap.get(endItemset)+1);}
                                else {endNodesMap.put(endItemset, 1);}
                                if (maxStrength < endNodesMap.get(endItemset)) {maxStrength = endNodesMap.get(endItemset);}
                                break;
                            }
                        }
                        pb.updateValue(++cnt, MyVars.sequeceFeatureCount);
                    }
                    if (endNodesMap.size() == 1) {
                        pb.updateValue(100, 100);
                        pb.dispose();
                        setVisible(false);
                        MyMessageUtil.showInfoMsg("There is no end rootNode for the selected rootNode!");
                        dispose();
                        return;
                    }
                    if (MyVars.g.edRefs == null) {
                        MyVars.g.edRefs = new HashSet();
                    }
                    MyNode startNode = (MyNode) MyVars.g.vRefs.get(startNodeName);
                    MyVars.getViewer().startNode = startNode;
                    Collection<MyEdge> rootOutEdges = MyVars.g.getOutEdges(startNode);
                    Collection<MyEdge> edges = new ArrayList<>(MyVars.g.getEdges());
                    for (MyEdge edge : edges) {
                        if (endNodesMap.keySet().contains(edge.getDest().getName()) && !edge.getDest().getName().equals(startNodeName)) {
                            if (!rootOutEdges.contains(edge)) {
                                MyEdge newEdge = new MyEdge((MyNode) MyVars.g.vRefs.get(startNodeName), edge.getDest());
                                String edgeName = newEdge.getSource().getName() + "-" + newEdge.getDest().getName();
                                if (!MyVars.g.edRefs.contains(edgeName)) {
                                    MyVars.g.addEdge(newEdge, newEdge.getSource(), newEdge.getDest());
                                    MyVars.g.edRefs.add(edgeName);
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
                        Collection<MyEdge> outEdges = MyVars.g.getOutEdges(startNodeName);
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
                            MyVars.g.addEdge(newEdge, newEdge.getSource(), newEdge.getDest());
                            MyVars.g.edRefs.add(edgeName);
                            float edgeValue = (float)endNodesMap.get(newEdge.getDest().getName())/maxStrength;
                            newEdge.setCurrentValue(edgeValue);
                        }
                    }
                    MyVars.getViewer().endNodesMap = endNodesMap;
                    synchronized (MyVars.g.MX_E_VAL) {
                        MyVars.g.MX_E_VAL = Float.valueOf(maxStrength);
                    }
                    pb.updateValue(100, 100);
                    pb.dispose();
                    MyVars.getViewer().revalidate();
                    MyVars.getViewer().repaint();
                    dispose();
                }
            }
        }).start();
    }
}
