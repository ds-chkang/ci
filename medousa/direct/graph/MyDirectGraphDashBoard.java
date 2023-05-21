package medousa.direct.graph;

import medousa.direct.graph.barcharts.MyDirectGraphEdgeValueBarChart;
import medousa.direct.graph.barcharts.MyDirectGraphNodeValueBarChart;
import medousa.direct.graph.barcharts.MyDirectGraphNodesByShortestDistanceBarChart;
import medousa.direct.graph.multilevel.*;
import medousa.direct.graph.path.MyDirectGraphDepthFirstGraphPathSercher;
import medousa.direct.graph.singlelevel.*;
import medousa.direct.graph.toplevel.*;
import medousa.MyProgressBar;
import medousa.direct.utils.MyDirectGraphMathUtil;
import medousa.message.MyMessageUtil;
import medousa.direct.utils.MyDirectGraphSysUtil;
import medousa.direct.utils.MyDirectGraphVars;
import medousa.table.MyTableToolTipHeader;
import medousa.table.MyTableToolTipper;
import medousa.table.MyTableUtil;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class MyDirectGraphDashBoard
extends JPanel
implements ActionListener {

    public MyDirectGraphMultiNodeLevelPredecessorValueDistributionLineChart multiNodeLevelPredecessorValueDistributionLineChart;
    public MyDirectGraphMultiNodeLevelSuccessorValueDistributionLineChart multiNodeLevelSuccessorValueDistributionLineChart;
    public MyDirectGraphMultiNodeLevelPredecessorEdgeValueDistributionLineChart multiNodeLevelPredecessorEdgeValueDistributionLineChart;
    public MyDirectGraphMultiNodeLevelSuccessorEdgeValueDistributionLineChart multiNodeLevelSuccessorEdgeValueDistributionLineChart;
    public MyDirectGraphMultiNodeLevelSharedPredecessorEdgeValueDistributionLineChart multiNodeLevelSharedPredecessorsEdgeValueDistributionChart;
    public MyDirectGraphMultiNodeLevelSharedSuccessorEdgeValueDistributionLineChart multiNodeLevelSharedSuccessorEdgeValueDistributionLineChart;

    public MyDirectGraphTopLevelNodeValueDistribution topLevelNodeValueDistribution;
    public MyDirectGraphTopLevelEdgeValueDistribution topLevelEdgeValueDistribution;
    public MyDirectGraphTopLevelPredecessorCountDistribution topLevelPredecessorCountDistribution;
    public MyDirectGraphTopLevelSuccessorCountDistribution topLevelSuccessorCountDistribution;
    public MyDirectGraphTopLevelDistributionChart topLevelDistributionChart;
    public MyDirectGraphTopLevelNodeLabelDistribution topLevelNodeLabelDistribution;
    public MyDirectGraphNodeLabelBarChart nodeLabelBarChart;
    public MyDirectGraphEdgeLabelBarChart edgeLabelBarChart;
    public MyDirectGraphTopLevelEdgeLabelDistribution topLevelEdgeLabelDistribution;

    public MyDirectGraphNodeLevelPredecessorEdgeValueDistribution nodeLevelPredecessorEdgeValueDistribution;
    public MyDirectGraphNodeLevelSuccessorEdgeValueDistribution nodeLevelSuccessorEdgeValueDistribution;
    public MyDirectGraphNodeLevelPredecessorValueDistribution nodeLevelPredecessorValueDistribution;
    public MyDirectGraphNodeLevelSuccessorValueDistribution nodeLevelSuccessorValueDistribution;
    public MyDirectGraphNodeLevelEdgeValueDistribution nodeLevelEdgeValueDistribution;

    public MyDirectGraphTextStatistics txtStatistics;
    public JTable nodeListTable;
    public JTable currentNodeListTable;
    public JTable statTable;
    public JTable edgeTable;
    public JTable sourceTable;
    public JTable destTable;
    public JComboBox distanceMenu = new JComboBox();
    public JComboBox shortestDistancePathMenu = new JComboBox();
    public JTabbedPane topLevelTabbedPane;
    public JTabbedPane nodeLevelTabbedPane;
    public JTabbedPane multiNodeLevelTabbedPane;

    public MyDirectGraphDashBoard() {}
    public MyDirectNode startNode;
    public JPanel fromTablePanel;
    public JPanel destTablePanel;
    public boolean isTableUpdating;
    public Set<MyDirectNode> visitedNodes;
    public JTabbedPane chartTabbedPane;
    public JSplitPane labelChartSplitPane;

    private void setShortestOutDistancedNodes() {
        if (isTableUpdating || sourceTable.getSelectedRow() == -1) return;
        String fromTableNodeName = sourceTable.getValueAt(sourceTable.getSelectedRow(), 1).toString();
        if (startNode != null && fromTableNodeName.equals(startNode.getName())) {return;}
        if (shortestDistancePathMenu.getSelectedIndex() == 1) {
            MyMessageUtil.showInfoMsg("Select a node in the destination node table.");
            return;
        }

        isTableUpdating = true;
        MyProgressBar pb = new MyProgressBar(false);
        try {
            startNode = (MyDirectNode) MyDirectGraphVars.directGraph.vRefs.get(fromTableNodeName);
            Set<MyDirectNode> topLevelSuccessors = new HashSet<>(MyDirectGraphVars.directGraph.getSuccessors(startNode));
            topLevelSuccessors.add(startNode);
            double totalWork = 0D;
            double work = 50/topLevelSuccessors.size();
            Queue<MyDirectNode> Qu = new LinkedList<>();
            visitedNodes = new HashSet();
            Qu.add(startNode);
            Map<MyDirectNode, Integer> distanceMap =  new HashMap();

            int maxDistance = 0;
            distanceMap.put(startNode, 0);
            while (!Qu.isEmpty()) {
                MyDirectNode v = Qu.remove();
                Collection<MyDirectNode> successors = MyDirectGraphVars.directGraph.getSuccessors(v);
                for (MyDirectNode neighbor : successors) {
                    if (!visitedNodes.contains(neighbor)) {
                        int distance = distanceMap.get(v) + 1;
                        neighbor.shortestOutDistance = distance;
                        if (distance > maxDistance) {
                            maxDistance = distance;
                        }
                        distanceMap.put(neighbor, distance);
                        Qu.add(neighbor);
                    }
                }
                visitedNodes.add(v);
                if (topLevelSuccessors.contains(v)) {
                    totalWork += work;
                    pb.updateValue((int) totalWork, 100);
                }
            }

            while (destTable.getRowCount() > 0) {
                int row = destTable.getRowCount() - 1;
                ((DefaultTableModel) destTable.getModel()).removeRow(row);
            }
            pb.updateValue(60, 100);

            LinkedHashMap<String, Long> valueMap = new LinkedHashMap<>();
            float maxNodeVal = 0f;
            TreeSet<Float> distances = new TreeSet<>();
            Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
            for (MyDirectNode n : nodes) {
                if (visitedNodes.contains(n)) {
                    if (n == startNode && visitedNodes.size() == 1) {
                        n.setCurrentValue(0);
                        continue;
                    } else {
                        distances.add(n.shortestOutDistance);
                    }
                    valueMap.put(n.getName(), (long) n.getCurrentValue());
                } else {
                    n.setCurrentValue(0);
                }
            }
            valueMap = MyDirectGraphSysUtil.sortMapByLongValue(valueMap);
            pb.updateValue(80, 100);

            int i=0;
            if (visitedNodes.size() > 1) {
                work = (double)40/ visitedNodes.size();
                for (String n : valueMap.keySet()) {
                    totalWork += (10 + work);
                    ((DefaultTableModel) destTable.getModel()).addRow(new String[]{"" + (++i), n, MyDirectGraphMathUtil.getCommaSeperatedNumber(valueMap.get(n))});
                }

                for (float distance : distances) {
                    if (distance > maxNodeVal) {
                        maxNodeVal = distance;
                    }
                }
            }

            destTablePanel.remove(distanceMenu);
            distanceMenu = new JComboBox();
            distanceMenu.setFocusable(false);
            distanceMenu.setFont(MyDirectGraphVars.tahomaPlainFont12);
            distanceMenu.addItem("DISTANCE");
            for (float distance : distances) {
                distanceMenu.addItem("" + (int) distance);
            }
            distanceMenu.addActionListener(new ActionListener() {
                @Override public void actionPerformed(ActionEvent e) {
                    new Thread(new Runnable() {
                        @Override public void run() {
                            if (distanceMenu.getSelectedIndex() > 0) {
                                MyProgressBar pb = new MyProgressBar(false);
                                Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
                                for (MyDirectNode n : nodes) {
                                    if (n.getCurrentValue() > 0) {
                                        if (Integer.parseInt(distanceMenu.getSelectedItem().toString()) != n.shortestOutDistance) {
                                            if (n.getCurrentValue() != 0) {
                                                n.setOriginalValue(n.getCurrentValue());
                                                n.setCurrentValue(0);
                                            }
                                        }
                                    } else if (n.shortestOutDistance == Integer.parseInt(distanceMenu.getSelectedItem().toString()) && n.getCurrentValue() == 0) {
                                        n.setCurrentValue(n.getOriginalValue());
                                    }

                                    if (MyDirectGraphVars.directGraph.maxNodeValue < n.getCurrentValue()) {
                                        MyDirectGraphVars.directGraph.maxNodeValue = n.getCurrentValue();
                                    }
                                }

                                while (destTable.getRowCount() > 0) {
                                    int row = destTable.getRowCount() - 1;
                                    ((DefaultTableModel) destTable.getModel()).removeRow(row);
                                }

                                LinkedHashMap<String, Long> valueMap = new LinkedHashMap<>();
                                for (MyDirectNode n : nodes) {
                                    if (n.getCurrentValue() > 0) {
                                        valueMap.put(n.getName(), (long) n.getCurrentValue());
                                    }
                                }
                                valueMap = MyDirectGraphSysUtil.sortMapByLongValue(valueMap);

                                int i = 0;
                                for (String n : valueMap.keySet()) {
                                    (MyDirectGraphVars.directGraph.vRefs.get(n)).setCurrentValue(valueMap.get(n));
                                    ((DefaultTableModel) destTable.getModel()).addRow(new String[]{"" + (++i), n, MyDirectGraphMathUtil.getCommaSeperatedNumber(valueMap.get(n))});
                                }

                                MyDirectGraphVars.getDirectGraphViewer().vc.removeBarCharts();
                                MyDirectGraphVars.getDirectGraphViewer().nodeValueBarChart = new MyDirectGraphNodeValueBarChart();
                                MyDirectGraphVars.getDirectGraphViewer().add(MyDirectGraphVars.getDirectGraphViewer().nodeValueBarChart);

                                updateTopLevelCharts();
                                MyDirectGraphVars.app.getDirectGraphDashBoard().txtStatistics.setShortestPathTextStatistics();
                                MyDirectGraphVars.app.getDirectGraphDashBoard().updateEdgeTable();
                                MyDirectGraphVars.app.getDirectGraphDashBoard().updateStatTable();
                                MyDirectGraphVars.app.getDirectGraphDashBoard().updateRemainingNodeTable();

                                MyDirectGraphVars.getDirectGraphViewer().revalidate();
                                MyDirectGraphVars.getDirectGraphViewer().repaint();

                                pb.updateValue(100, 100);
                                pb.dispose();
                            } else if (distanceMenu.getSelectedIndex() == 0) {
                                MyProgressBar pb = new MyProgressBar(false);
                                Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
                                for (MyDirectNode n : nodes) {
                                    if (n.getCurrentValue() == 0) {
                                        n.setCurrentValue(n.getOriginalValue());
                                    }
                                }

                                while (destTable.getRowCount() > 0) {
                                    int row = destTable.getRowCount() - 1;
                                    ((DefaultTableModel) destTable.getModel()).removeRow(row);
                                }

                                float maxValue = 0f;
                                LinkedHashMap<String, Long> valueMap = new LinkedHashMap<>();
                                for (MyDirectNode n : nodes) {
                                    valueMap.put(n.getName(), (long) n.getCurrentValue());
                                    if (maxValue < n.getCurrentValue()) {
                                        maxValue = n.getCurrentValue();
                                    }
                                }
                                valueMap = MyDirectGraphSysUtil.sortMapByLongValue(valueMap);
                                if (maxValue > 0) {
                                    MyDirectGraphVars.directGraph.maxNodeValue = maxValue;
                                }

                                int i = 0;
                                for (String n : valueMap.keySet()) {
                                    ((MyDirectNode) MyDirectGraphVars.directGraph.vRefs.get(n)).setCurrentValue(valueMap.get(n));
                                    ((DefaultTableModel) destTable.getModel()).addRow(new String[]{"" + (++i), n, MyDirectGraphMathUtil.getCommaSeperatedNumber(valueMap.get(n))});
                                }

                                MyDirectGraphVars.getDirectGraphViewer().vc.removeBarCharts();
                                MyDirectGraphVars.getDirectGraphViewer().nodesByShortestDistanceBarChart = new MyDirectGraphNodesByShortestDistanceBarChart(MyDirectGraphVars.app.getDirectGraphDashBoard().visitedNodes);
                                MyDirectGraphVars.getDirectGraphViewer().add(MyDirectGraphVars.getDirectGraphViewer().nodesByShortestDistanceBarChart);

                                updateTopLevelCharts();
                                MyDirectGraphVars.app.getDirectGraphDashBoard().txtStatistics.setShortestPathTextStatistics();
                                MyDirectGraphVars.app.getDirectGraphDashBoard().updateEdgeTable();
                                MyDirectGraphVars.app.getDirectGraphDashBoard().updateStatTable();
                                MyDirectGraphVars.app.getDirectGraphDashBoard().updateRemainingNodeTable();

                                MyDirectGraphVars.getDirectGraphViewer().revalidate();
                                MyDirectGraphVars.getDirectGraphViewer().repaint();

                                pb.updateValue(100, 100);
                                pb.dispose();
                            }
                        }
                    }).start();
                }
            });

            destTablePanel.add(distanceMenu, BorderLayout.NORTH);
            MyDirectGraphVars.getDirectGraphViewer().vc.removeBarCharts();
            MyDirectGraphVars.getDirectGraphViewer().nodesByShortestDistanceBarChart = new MyDirectGraphNodesByShortestDistanceBarChart(visitedNodes);
            MyDirectGraphVars.getDirectGraphViewer().add(MyDirectGraphVars.getDirectGraphViewer().nodesByShortestDistanceBarChart);

            updateTopLevelCharts();
            MyDirectGraphVars.app.getDirectGraphDashBoard().txtStatistics.setShortestPathTextStatistics();
            MyDirectGraphVars.app.getDirectGraphDashBoard().updateEdgeTable();
            MyDirectGraphVars.app.getDirectGraphDashBoard().updateStatTable();
            MyDirectGraphVars.app.getDirectGraphDashBoard().updateRemainingNodeTable();
            MyDirectGraphVars.app.getDirectGraphDashBoard().revalidate();
            MyDirectGraphVars.app.getDirectGraphDashBoard().repaint();
            MyDirectGraphVars.getDirectGraphViewer().revalidate();
            MyDirectGraphVars.getDirectGraphViewer().repaint();
            pb.updateValue(100, 100);
            pb.dispose();
            isTableUpdating = false;
        } catch (Exception ex) {
            pb.updateValue(100, 100);
            pb.dispose();
            ex.printStackTrace();
        }
    }

    private void setShortestInDistancedNodes() {
        if (isTableUpdating || destTable.getSelectedRow() == -1) return;
        String fromTableNodeName = destTable.getValueAt(destTable.getSelectedRow(), 1).toString();
        if (startNode != null && fromTableNodeName.equals(startNode.getName())) {return;}
        if (shortestDistancePathMenu.getSelectedIndex() == 0) {
            MyMessageUtil.showInfoMsg("Select a node in the source node table.");
            return;
        }

        isTableUpdating = true;
        MyProgressBar pb = new MyProgressBar(false);
        try {
            startNode = (MyDirectNode) MyDirectGraphVars.directGraph.vRefs.get(fromTableNodeName);
            Set<MyDirectNode> topLevelPredecessors = new HashSet<>(MyDirectGraphVars.directGraph.getPredecessors(startNode));
            topLevelPredecessors.add(startNode);
            double totalWork = 0D;
            double work = 50/topLevelPredecessors.size();
            Queue<MyDirectNode> Qu = new LinkedList<>();
            visitedNodes = new HashSet();
            Qu.add(startNode);
            Map<MyDirectNode, Integer> distanceMap =  new HashMap();

            int maxDistance = 0;
            distanceMap.put(startNode, 0);
            while (!Qu.isEmpty()) {
                MyDirectNode successor = Qu.remove();
                Collection<MyDirectNode> predecessors = MyDirectGraphVars.directGraph.getPredecessors(successor);
                for (MyDirectNode predecessor : predecessors) {
                    if (!visitedNodes.contains(predecessor)) {
                        int distance = distanceMap.get(successor) + 1;
                        predecessor.shortestInDistance = distance;
                        if (distance > maxDistance) {
                            maxDistance = distance;
                        }
                        distanceMap.put(predecessor, distance);
                        Qu.add(predecessor);
                    }
                }
                visitedNodes.add(successor);
                if (topLevelPredecessors.contains(successor)) {
                    totalWork += work;
                    pb.updateValue((int) totalWork, 100);
                }
            }

            while (sourceTable.getRowCount() > 0) {
                int row = sourceTable.getRowCount() - 1;
                ((DefaultTableModel) sourceTable.getModel()).removeRow(row);
            }
            pb.updateValue(60, 100);

            LinkedHashMap<String, Long> valueMap = new LinkedHashMap<>();
            float maxNodeVal = 0f;
            TreeSet<Float> distances = new TreeSet<>();
            Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
            for (MyDirectNode n : nodes) {
                if (visitedNodes.contains(n)) {
                    if (n == startNode && visitedNodes.size() == 1) {
                        n.setCurrentValue(0);
                        continue;
                    } else {
                        distances.add(n.shortestInDistance);
                    }
                    valueMap.put(n.getName(), (long) n.getCurrentValue());
                } else {
                    n.setCurrentValue(0);
                }
            }
            valueMap = MyDirectGraphSysUtil.sortMapByLongValue(valueMap);
            pb.updateValue(80, 100);

            int i=0;
            if (visitedNodes.size() > 1) {
                work = (double)40/ visitedNodes.size();
                for (String n : valueMap.keySet()) {
                    totalWork += (10 + work);
                    ((DefaultTableModel) sourceTable.getModel()).addRow(new String[]{"" + (++i), n, MyDirectGraphMathUtil.getCommaSeperatedNumber(valueMap.get(n))});
                }

                for (float distance : distances) {
                    if (distance > maxNodeVal) {
                        maxNodeVal = distance;
                    }
                }
            }

            destTablePanel.remove(distanceMenu);
            distanceMenu = new JComboBox();
            distanceMenu.setFocusable(false);
            distanceMenu.setFont(MyDirectGraphVars.tahomaPlainFont12);
            distanceMenu.addItem("DISTANCE");
            for (float distance : distances) {
                distanceMenu.addItem("" + (int) distance);
            }
            distanceMenu.addActionListener(new ActionListener() {
                @Override public void actionPerformed(ActionEvent e) {
                    new Thread(new Runnable() {
                        @Override public void run() {
                            if (distanceMenu.getSelectedIndex() > 0) {
                                MyProgressBar pb = new MyProgressBar(false);
                                Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
                                for (MyDirectNode n : nodes) {
                                    if (n.getCurrentValue() > 0) {
                                        if (Integer.parseInt(distanceMenu.getSelectedItem().toString()) != n.shortestInDistance) {
                                            if (n.getCurrentValue() != 0) {
                                                n.setOriginalValue(n.getCurrentValue());
                                                n.setCurrentValue(0);
                                            }
                                        }
                                    } else if (n.shortestInDistance == Integer.parseInt(distanceMenu.getSelectedItem().toString()) && n.getCurrentValue() == 0) {
                                        n.setCurrentValue(n.getOriginalValue());
                                    }

                                    if (MyDirectGraphVars.directGraph.maxNodeValue < n.getCurrentValue()) {
                                        MyDirectGraphVars.directGraph.maxNodeValue = n.getCurrentValue();
                                    }
                                }
                                pb.updateValue(40, 100);

                                while (sourceTable.getRowCount() > 0) {
                                    int row = sourceTable.getRowCount() - 1;
                                    ((DefaultTableModel) sourceTable.getModel()).removeRow(row);
                                }
                                pb.updateValue(70, 100);

                                LinkedHashMap<String, Long> valueMap = new LinkedHashMap<>();
                                for (MyDirectNode n : nodes) {
                                    if (n.getCurrentValue() > 0) {
                                        valueMap.put(n.getName(), (long) n.getCurrentValue());
                                    }
                                }
                                valueMap = MyDirectGraphSysUtil.sortMapByLongValue(valueMap);
                                pb.updateValue(80, 100);

                                int i = 0;
                                for (String n : valueMap.keySet()) {
                                    ((MyDirectNode) MyDirectGraphVars.directGraph.vRefs.get(n)).setCurrentValue(valueMap.get(n));
                                    ((DefaultTableModel) sourceTable.getModel()).addRow(new String[]{"" + (++i), n, MyDirectGraphMathUtil.getCommaSeperatedNumber(valueMap.get(n))});
                                }

                                MyDirectGraphVars.getDirectGraphViewer().vc.removeBarCharts();
                                MyDirectGraphVars.getDirectGraphViewer().nodeValueBarChart = new MyDirectGraphNodeValueBarChart();
                                MyDirectGraphVars.getDirectGraphViewer().add(MyDirectGraphVars.getDirectGraphViewer().nodeValueBarChart);

                                updateTopLevelCharts();
                                MyDirectGraphVars.app.getDirectGraphDashBoard().txtStatistics.setShortestPathTextStatistics();
                                MyDirectGraphVars.app.getDirectGraphDashBoard().updateEdgeTable();
                                MyDirectGraphVars.app.getDirectGraphDashBoard().updateStatTable();
                                MyDirectGraphVars.app.getDirectGraphDashBoard().updateRemainingNodeTable();

                                MyDirectGraphVars.getDirectGraphViewer().revalidate();
                                MyDirectGraphVars.getDirectGraphViewer().repaint();
                                pb.updateValue(100, 100);
                                pb.dispose();
                            } else if (distanceMenu.getSelectedIndex() == 0) {
                                MyProgressBar pb = new MyProgressBar(false);
                                Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
                                for (MyDirectNode n : nodes) {
                                    if (n.getCurrentValue() == 0) {
                                        n.setCurrentValue(n.getOriginalValue());
                                    }
                                }

                                while (sourceTable.getRowCount() > 0) {
                                    int row = sourceTable.getRowCount() - 1;
                                    ((DefaultTableModel) sourceTable.getModel()).removeRow(row);
                                }

                                float maxValue = 0f;
                                LinkedHashMap<String, Long> valueMap = new LinkedHashMap<>();
                                for (MyDirectNode n : nodes) {
                                    valueMap.put(n.getName(), (long) n.getCurrentValue());
                                    if (maxValue < n.getCurrentValue()) {
                                        maxValue = n.getCurrentValue();
                                    }
                                }
                                valueMap = MyDirectGraphSysUtil.sortMapByLongValue(valueMap);
                                if (maxValue > 0) {
                                    MyDirectGraphVars.directGraph.maxNodeValue = maxValue;}

                                int i=0;
                                for (String n : valueMap.keySet()) {
                                    ((MyDirectNode) MyDirectGraphVars.directGraph.vRefs.get(n)).setCurrentValue(valueMap.get(n));
                                    ((DefaultTableModel) sourceTable.getModel()).addRow(new String[]{"" + (++i), n, MyDirectGraphMathUtil.getCommaSeperatedNumber(valueMap.get(n))});
                                }
                            }

                            MyDirectGraphVars.getDirectGraphViewer().vc.removeBarCharts();
                            MyDirectGraphVars.getDirectGraphViewer().nodesByShortestDistanceBarChart = new MyDirectGraphNodesByShortestDistanceBarChart(MyDirectGraphVars.app.getDirectGraphDashBoard().visitedNodes);
                            MyDirectGraphVars.getDirectGraphViewer().add(MyDirectGraphVars.getDirectGraphViewer().nodesByShortestDistanceBarChart);

                            updateTopLevelCharts();
                            MyDirectGraphVars.app.getDirectGraphDashBoard().txtStatistics.setShortestPathTextStatistics();
                            MyDirectGraphVars.app.getDirectGraphDashBoard().updateEdgeTable();
                            MyDirectGraphVars.app.getDirectGraphDashBoard().updateStatTable();
                            MyDirectGraphVars.app.getDirectGraphDashBoard().updateRemainingNodeTable();

                            MyDirectGraphVars.getDirectGraphViewer().revalidate();
                            MyDirectGraphVars.getDirectGraphViewer().repaint();
                            pb.updateValue(100, 100);
                            pb.dispose();
                        }
                    }).start();
                }
            });
            destTablePanel.add(distanceMenu, BorderLayout.NORTH);

            if (maxNodeVal > 0) {
                MyDirectGraphVars.directGraph.maxNodeValue = maxNodeVal;
            }

            destTablePanel.add(distanceMenu, BorderLayout.NORTH);
            MyDirectGraphVars.getDirectGraphViewer().vc.removeBarCharts();
            MyDirectGraphVars.getDirectGraphViewer().nodesByShortestDistanceBarChart = new MyDirectGraphNodesByShortestDistanceBarChart(visitedNodes);
            MyDirectGraphVars.getDirectGraphViewer().add(MyDirectGraphVars.getDirectGraphViewer().nodesByShortestDistanceBarChart);

            updateTopLevelCharts();
            MyDirectGraphVars.app.getDirectGraphDashBoard().txtStatistics.setShortestPathTextStatistics();
            MyDirectGraphVars.app.getDirectGraphDashBoard().updateEdgeTable();
            MyDirectGraphVars.app.getDirectGraphDashBoard().updateStatTable();
            MyDirectGraphVars.app.getDirectGraphDashBoard().updateRemainingNodeTable();
            MyDirectGraphVars.app.getDirectGraphDashBoard().revalidate();
            MyDirectGraphVars.app.getDirectGraphDashBoard().repaint();
            MyDirectGraphVars.getDirectGraphViewer().revalidate();
            MyDirectGraphVars.getDirectGraphViewer().repaint();
            pb.updateValue(100, 100);
            pb.dispose();
            isTableUpdating = false;
        } catch (Exception ex) {
            pb.updateValue(100, 100);
            pb.dispose();
            ex.printStackTrace();
        }
    }

    private JPanel setPathFindTable() {
        this.fromTablePanel = new JPanel();
        this.fromTablePanel.setLayout(new BorderLayout(2, 0));
        this.fromTablePanel.setBackground(Color.WHITE);

        this.shortestDistancePathMenu = new JComboBox();
        this.shortestDistancePathMenu.setFont(MyDirectGraphVars.tahomaPlainFont12);
        this.shortestDistancePathMenu.setFocusable(false);
        this.shortestDistancePathMenu.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                startNode = null;
            }
        });
        //pathMenu.addItem("PATH EXPLORATION");
        this.shortestDistancePathMenu.addItem("SHORTEST OUT-DISTANCE");
        this.shortestDistancePathMenu.addItem("SHORTEST IN-DISTANCE");

        String [] fromColumns = {"NO.", "SOURCE", "V."};
        String [][] fromData = {};
        DefaultTableModel fromTableModel = new DefaultTableModel(fromData, fromColumns);
        this.sourceTable = new JTable(fromTableModel) {
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

        String [] fromTableTooltips = {"NO.", "SOURCE NODE", "SOURCE NODE VALUE"};
        MyTableToolTipper fromTableTooltipHeader = new MyTableToolTipper(this.sourceTable.getColumnModel());
        fromTableTooltipHeader.setToolTipStrings(fromTableTooltips);
        this.sourceTable.setTableHeader(fromTableTooltipHeader);

        Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
        LinkedHashMap<String, Long> sourceNodeValueMap = new LinkedHashMap<>();
        int i = 0;
        for (MyDirectNode n : nodes) {
            sourceNodeValueMap.put(n.getName(), (long) n.getCurrentValue());
        }
        sourceNodeValueMap = MyDirectGraphSysUtil.sortMapByLongValue(sourceNodeValueMap);

        for (String n : sourceNodeValueMap.keySet()) {
            fromTableModel.addRow(new String[]{String.valueOf(++i), n, MyDirectGraphMathUtil.getCommaSeperatedNumber(sourceNodeValueMap.get(n))});
        }

        this.sourceTable.setRowHeight(19);
        this.sourceTable.setBackground(Color.WHITE);
        this.sourceTable.setFont(MyDirectGraphVars.f_pln_12);
        this.sourceTable.getTableHeader().setFont(MyDirectGraphVars.tahomaBoldFont12);
        this.sourceTable.getTableHeader().setOpaque(false);
        this.sourceTable.getTableHeader().setBackground(new Color(0,0,0,0f));
        this.sourceTable.getColumnModel().getColumn(0).setPreferredWidth(45);
        this.sourceTable.getColumnModel().getColumn(1).setPreferredWidth(80);
        this.sourceTable.getColumnModel().getColumn(2).setPreferredWidth(40);

        JTextField fromNodeSearchTxt = new JTextField();
        JButton fromNodeSelectBtn = new JButton();
        fromNodeSearchTxt.setToolTipText("TYPE A NODE TO SEARCH");
        fromNodeSearchTxt.setBackground(Color.WHITE);

        fromNodeSearchTxt.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        JPanel fromSearchAndSavePanel = MyTableUtil.searchAndSaveDataPanelForJTable2(this, fromNodeSearchTxt, fromNodeSelectBtn, fromTableModel, this.sourceTable);
        fromNodeSelectBtn.setPreferredSize(new Dimension(40, 25));
        fromNodeSelectBtn.removeActionListener(this);
        fromNodeSelectBtn.removeActionListener(this);
        fromSearchAndSavePanel.remove(fromNodeSelectBtn);
        fromNodeSearchTxt.setFont(MyDirectGraphVars.f_bold_12);

        add(fromSearchAndSavePanel, BorderLayout.SOUTH);
        this.sourceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.sourceTable.setSelectionBackground(Color.LIGHT_GRAY);
        this.sourceTable.setForeground(Color.BLACK);
        this.sourceTable.setSelectionForeground(Color.BLACK);
        this.sourceTable.setFocusable(false);
        this.sourceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.sourceTable.setRowSorter(MyTableUtil.setJTableRowSorterWithTextField(fromTableModel, fromNodeSearchTxt));
        this.sourceTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        if (shortestDistancePathMenu.getSelectedIndex() == 0) {
                            setShortestOutDistancedNodes();
                        }
                    }
                }).start();
            }
        });

        JScrollPane fromTableScrollPane = new JScrollPane(sourceTable);
        fromTableScrollPane.setOpaque(false);
        fromTableScrollPane.setBackground(new Color(0,0,0,0f));
        fromTableScrollPane.setPreferredSize(new Dimension(150, 500));
        fromTableScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(7, 0));
        fromTableScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 7));

        this.fromTablePanel.add(fromTableScrollPane, BorderLayout.CENTER);
        this.fromTablePanel.add(fromSearchAndSavePanel, BorderLayout.SOUTH);

        this.destTablePanel = new JPanel();
        this.destTablePanel.setLayout(new BorderLayout(2, 2));
        this.destTablePanel.setBackground(Color.WHITE);

        this.distanceMenu.setFont(MyDirectGraphVars.tahomaPlainFont12);
        this.distanceMenu.setFocusable(false);
        this.destTablePanel.add(this.distanceMenu, BorderLayout.NORTH);

        String [] toColumns = {"NO.", "DEST", "V."};
        String [][] toData = {};
        DefaultTableModel toTableModel = new DefaultTableModel(toData, toColumns);
        this.destTable = new JTable(toTableModel) {
            //Implement table cell tool tips.
            public String getToolTipText(MouseEvent e) {
                String tip = null;
                Point p = e.getPoint();
                int rowIndex = rowAtPoint(p);
                int colIndex = columnAtPoint(p);

                try {
                    tip = getValueAt(rowIndex, colIndex).toString();
                } catch (RuntimeException e1) {
                    //catch null pointer exception if mouse is over an empty line
                }

                return tip;
            }
        };

        String [] toTableTooltips = {"NO.", "DESTINATION NODE", "DESTINATION NODE VALUE"};
        MyTableToolTipper toTableTooltipHeader = new MyTableToolTipper(this.destTable.getColumnModel());
        toTableTooltipHeader.setToolTipStrings(toTableTooltips);
        this.destTable.setTableHeader(toTableTooltipHeader);

        i = 0;
        for (MyDirectNode n : nodes) {
            toTableModel.addRow(new String[]{String.valueOf(++i), n.getName(), "0"});
        }

        this.destTable.setRowHeight(19);
        this.destTable.setBackground(Color.WHITE);
        this.destTable.setFont(MyDirectGraphVars.f_pln_12);
        this.destTable.getTableHeader().setFont(MyDirectGraphVars.tahomaBoldFont12);
        this.destTable.getTableHeader().setOpaque(false);
        this.destTable.getTableHeader().setBackground(new Color(0,0,0,0f));
        this.destTable.getColumnModel().getColumn(0).setPreferredWidth(45);
        this.destTable.getColumnModel().getColumn(1).setPreferredWidth(80);
        this.destTable.getColumnModel().getColumn(2).setPreferredWidth(40);
        this.destTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        if (shortestDistancePathMenu.getSelectedIndex() == 1) {
                            setShortestInDistancedNodes();
                        }
                    }
                }).start();
            }
        });

        JTextField toNodeSearchTxt = new JTextField();
        JButton runBtn = new JButton("RUN");
        runBtn.setFocusable(false);
        runBtn.setFont(MyDirectGraphVars.tahomaPlainFont12);

        toNodeSearchTxt.setBackground(Color.WHITE);
        toNodeSearchTxt.setFont(MyDirectGraphVars.f_bold_12);
        toNodeSearchTxt.setToolTipText("TYPE A NODE NAME TO SEARCH");
        toNodeSearchTxt.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        JPanel toSearchAndSavePanel = MyTableUtil.searchAndSaveDataPanelForJTable2(this, toNodeSearchTxt, runBtn, toTableModel, this.destTable);

        runBtn.setPreferredSize(new Dimension(54, 25));
        runBtn.removeActionListener(this);
        runBtn.removeActionListener(this);
        runBtn.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                if (shortestDistancePathMenu.getSelectedIndex() == 1) {
                    new Thread(new Runnable() {
                        @Override public void run() {
                            if (sourceTable.getSelectedRow() >= 0 && destTable.getSelectedRow() >= 0) {
                                String fromTableNodeName = sourceTable.getValueAt(sourceTable.getSelectedRow(), 1).toString();
                                String toTableNodeName = destTable.getValueAt(destTable.getSelectedRow(), 1).toString();
                                if (fromTableNodeName.equals(toTableNodeName)) {
                                    MyMessageUtil.showInfoMsg("Choose different nodes to find paths.");
                                    return;
                                }
                                MyDirectGraphDepthFirstGraphPathSercher betweenPathGraphDepthFirstSearch = new MyDirectGraphDepthFirstGraphPathSercher();
                                betweenPathGraphDepthFirstSearch.decorate();
                                betweenPathGraphDepthFirstSearch.run(fromTableNodeName, toTableNodeName);
                                if (betweenPathGraphDepthFirstSearch.integratedGraph.getVertexCount() == 0) {
                                    betweenPathGraphDepthFirstSearch.dispose();
                                    MyMessageUtil.showInfoMsg("There are no reachable paths between [" + fromTableNodeName + "] AND [" + toTableNodeName + "]");
                                    return;
                                }
                                betweenPathGraphDepthFirstSearch.setAlwaysOnTop(true);
                                betweenPathGraphDepthFirstSearch.setVisible(true);
                                betweenPathGraphDepthFirstSearch.setAlwaysOnTop(false);
                            } else {
                                MyMessageUtil.showInfoMsg(MyDirectGraphVars.app, "Two nodes are required for the operation.");
                            }
                        }
                    }).start();
                } else if(shortestDistancePathMenu.getSelectedIndex()==2) {
                    new Thread(new Runnable() {
                        @Override public void run() {

                        }
                    }).start();
                }
            }
        });
        runBtn.setVisible(false);

        add(toSearchAndSavePanel, BorderLayout.SOUTH);
        this.destTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.destTable.setSelectionBackground(Color.LIGHT_GRAY);
        this.destTable.setSelectionForeground(Color.BLACK);
        this.destTable.setForeground(Color.BLACK);
        this.destTable.setFocusable(false);
        this.destTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.destTable.setRowSorter(MyTableUtil.setJTableRowSorterWithTextField(toTableModel, toNodeSearchTxt));

        JScrollPane toTableScrollPane = new JScrollPane(this.destTable);
        toTableScrollPane.setOpaque(false);
        toTableScrollPane.setBackground(new Color(0,0,0,0f));
        toTableScrollPane.setPreferredSize(new Dimension(150, 500));
        toTableScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(7, 0));
        toTableScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 7));

        this.destTablePanel.add(toTableScrollPane, BorderLayout.CENTER);
        this.destTablePanel.add(toSearchAndSavePanel, BorderLayout.SOUTH);

        JPanel pathPanel = new JPanel();
        pathPanel.setBackground(Color.WHITE);
        pathPanel.setLayout(new BorderLayout(3,1));

        JPanel tablePanel = new JPanel();
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setLayout(new GridLayout(2,1));
        tablePanel.add(this.fromTablePanel);
        tablePanel.add(this.destTablePanel);

        pathPanel.add(tablePanel, BorderLayout.CENTER);
        pathPanel.add(this.shortestDistancePathMenu, BorderLayout.NORTH);

        return pathPanel;
    }

    public void updateTopLevelCharts() {
        topLevelNodeValueDistribution.decorate();
        topLevelPredecessorCountDistribution.decorate();
        topLevelSuccessorCountDistribution.decorate();
        topLevelEdgeValueDistribution.decorate();
        topLevelDistributionChart.decorate();
        topLevelNodeLabelDistribution.decorate();
        topLevelEdgeLabelDistribution.decorate();
    }

    private JTabbedPane setNodeLevelCharts() {
        this.nodeLevelPredecessorEdgeValueDistribution = new MyDirectGraphNodeLevelPredecessorEdgeValueDistribution();
        this.nodeLevelSuccessorEdgeValueDistribution = new MyDirectGraphNodeLevelSuccessorEdgeValueDistribution();
        this.nodeLevelPredecessorValueDistribution = new MyDirectGraphNodeLevelPredecessorValueDistribution();
        this.nodeLevelSuccessorValueDistribution = new MyDirectGraphNodeLevelSuccessorValueDistribution();
        this.nodeLevelEdgeValueDistribution = new MyDirectGraphNodeLevelEdgeValueDistribution();

        JPanel nodeLevelStatPanel = new JPanel();
        nodeLevelStatPanel.setBackground(Color.WHITE);
        nodeLevelStatPanel.setLayout(new GridLayout(5, 1));

        nodeLevelStatPanel.add(this.nodeLevelPredecessorValueDistribution);
        nodeLevelStatPanel.add(this.nodeLevelSuccessorValueDistribution);
        nodeLevelStatPanel.add(this.nodeLevelEdgeValueDistribution);
        nodeLevelStatPanel.add(this.nodeLevelPredecessorEdgeValueDistribution);
        nodeLevelStatPanel.add(this.nodeLevelSuccessorEdgeValueDistribution);

        chartTabbedPane = new JTabbedPane();
        chartTabbedPane.addTab("V. D.", null, nodeLevelStatPanel, "VALUE DISTRIBUTIONS");
        chartTabbedPane.addTab("L. D.", null, setLabelDistributionChart(), "LABEL DISTRIBUTIONS");

        return chartTabbedPane;
    }

    private JTabbedPane setMultiNodeLevelCharts() {
        this.multiNodeLevelPredecessorValueDistributionLineChart = new MyDirectGraphMultiNodeLevelPredecessorValueDistributionLineChart();
        this.multiNodeLevelSuccessorValueDistributionLineChart = new MyDirectGraphMultiNodeLevelSuccessorValueDistributionLineChart();
        this.multiNodeLevelPredecessorEdgeValueDistributionLineChart = new MyDirectGraphMultiNodeLevelPredecessorEdgeValueDistributionLineChart();
        this.multiNodeLevelSuccessorEdgeValueDistributionLineChart = new MyDirectGraphMultiNodeLevelSuccessorEdgeValueDistributionLineChart();
        this.multiNodeLevelSharedPredecessorsEdgeValueDistributionChart = new MyDirectGraphMultiNodeLevelSharedPredecessorEdgeValueDistributionLineChart();
        this.multiNodeLevelSharedSuccessorEdgeValueDistributionLineChart = new MyDirectGraphMultiNodeLevelSharedSuccessorEdgeValueDistributionLineChart();

        JPanel multinodeLevelStatPanel = new JPanel();
        multinodeLevelStatPanel.setBackground(Color.WHITE);
        multinodeLevelStatPanel.setLayout(new GridLayout(6, 1));

        multinodeLevelStatPanel.add(this.multiNodeLevelPredecessorValueDistributionLineChart);
        multinodeLevelStatPanel.add(this.multiNodeLevelSuccessorValueDistributionLineChart);
        multinodeLevelStatPanel.add(this.multiNodeLevelPredecessorEdgeValueDistributionLineChart);
        multinodeLevelStatPanel.add(this.multiNodeLevelSuccessorEdgeValueDistributionLineChart);
        multinodeLevelStatPanel.add(this.multiNodeLevelSharedPredecessorsEdgeValueDistributionChart);
        multinodeLevelStatPanel.add(this.multiNodeLevelSharedSuccessorEdgeValueDistributionLineChart);

        chartTabbedPane = new JTabbedPane();
        chartTabbedPane.addTab("V. D.", null, multinodeLevelStatPanel, "VALUE DISTRIBUTIONS");
        chartTabbedPane.addTab("L. D.", null, setLabelDistributionChart(), "LABEL DISTRIBUTIONS");

        return chartTabbedPane;
    }

    public JButton nodeLabelEnlargeBtn = new JButton("+");
    public JButton edgeLabelEnlargeBtn = new JButton("+");

    private JSplitPane setLabelDistributionChart() {
        JPanel nodeLabelPanel = new JPanel();
        nodeLabelPanel.setBackground(Color.WHITE);
        nodeLabelPanel.setLayout(new BorderLayout(3,3));

        JLabel nodeLabelPanelTitle = new JLabel(" N. L.");
        nodeLabelPanelTitle.setBackground(Color.WHITE);
        nodeLabelPanelTitle.setFont(MyDirectGraphVars.tahomaBoldFont12);

        nodeLabelEnlargeBtn.setFont(MyDirectGraphVars.tahomaPlainFont11);
        nodeLabelEnlargeBtn.setBackground(Color.WHITE);
        nodeLabelEnlargeBtn.setFocusable(false);
        nodeLabelEnlargeBtn.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        nodeLabelBarChart.enlarge();
                    }
                }).start();
            }
        });
        nodeLabelEnlargeBtn.setEnabled(false);

        JPanel nodeLabelTopCenterPanel = new JPanel();
        nodeLabelTopCenterPanel.setBackground(Color.WHITE);

        JPanel nodeLabelTopPanel = new JPanel();
        nodeLabelTopPanel.setBackground(Color.WHITE);
        nodeLabelTopPanel.setLayout(new BorderLayout(2,2));
        nodeLabelTopPanel.add(nodeLabelEnlargeBtn, BorderLayout.EAST);
        nodeLabelTopPanel.add(nodeLabelPanelTitle, BorderLayout.WEST);
        nodeLabelTopPanel.add(nodeLabelTopCenterPanel, BorderLayout.CENTER);

        nodeLabelPanel.add(nodeLabelTopPanel, BorderLayout.NORTH);
        nodeLabelPanel.add(this.nodeLabelBarChart, BorderLayout.CENTER);

        JPanel edgeLabelPanel = new JPanel();
        edgeLabelPanel.setBackground(Color.WHITE);
        edgeLabelPanel.setLayout(new BorderLayout(3,3));

        JLabel edgeLabelPanelTitle = new JLabel(" E. L.");
        edgeLabelPanelTitle.setBackground(Color.WHITE);
        edgeLabelPanelTitle.setFont(MyDirectGraphVars.tahomaBoldFont12);

        edgeLabelEnlargeBtn.setFont(MyDirectGraphVars.tahomaPlainFont11);
        edgeLabelEnlargeBtn.setBackground(Color.WHITE);
        edgeLabelEnlargeBtn.setFocusable(false);
        edgeLabelEnlargeBtn.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        edgeLabelBarChart.enlarge();
                    }
                }).start();
            }
        });
        edgeLabelEnlargeBtn.setEnabled(false);

        JPanel edgeLabelTopCenterPanel = new JPanel();
        edgeLabelTopCenterPanel.setBackground(Color.WHITE);

        JPanel edgeLabelTopPanel = new JPanel();
        edgeLabelTopPanel.setBackground(Color.WHITE);
        edgeLabelTopPanel.setLayout(new BorderLayout(2,2));
        edgeLabelTopPanel.add(edgeLabelEnlargeBtn, BorderLayout.EAST);
        edgeLabelTopPanel.add(edgeLabelPanelTitle, BorderLayout.WEST);
        edgeLabelTopPanel.add(edgeLabelTopCenterPanel, BorderLayout.CENTER);

        edgeLabelPanel.add(edgeLabelTopPanel, BorderLayout.NORTH);
        edgeLabelPanel.add(this.edgeLabelBarChart, BorderLayout.CENTER);

        labelChartSplitPane = new JSplitPane();
        labelChartSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        labelChartSplitPane.setDividerSize(7);
        labelChartSplitPane.setTopComponent(nodeLabelPanel);
        labelChartSplitPane.setDividerLocation((MyDirectGraphSysUtil.screenHeight/2)-80);
        labelChartSplitPane.setBottomComponent(edgeLabelPanel);
        labelChartSplitPane.addComponentListener(new ComponentAdapter() {
            @Override public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                labelChartSplitPane.setDividerLocation((MyDirectGraphSysUtil.screenHeight/2)-80);
            }
        });
        return labelChartSplitPane;
    }

    public JTabbedPane setTopLevelCharts() {
        this.topLevelNodeValueDistribution = new MyDirectGraphTopLevelNodeValueDistribution();
        this.topLevelEdgeValueDistribution = new MyDirectGraphTopLevelEdgeValueDistribution();
        this.topLevelPredecessorCountDistribution = new MyDirectGraphTopLevelPredecessorCountDistribution();
        this.topLevelSuccessorCountDistribution = new MyDirectGraphTopLevelSuccessorCountDistribution();
        this.topLevelDistributionChart = new MyDirectGraphTopLevelDistributionChart();
        this.topLevelNodeLabelDistribution = new MyDirectGraphTopLevelNodeLabelDistribution();
        this.topLevelEdgeLabelDistribution = new MyDirectGraphTopLevelEdgeLabelDistribution();
        this.nodeLabelBarChart = new MyDirectGraphNodeLabelBarChart();
        this.edgeLabelBarChart = new MyDirectGraphEdgeLabelBarChart();

        JPanel grapLevelStatPanel = new JPanel();
        grapLevelStatPanel.setBackground(Color.WHITE);
        grapLevelStatPanel.setLayout(new GridLayout(5, 1));
        grapLevelStatPanel.add(this.topLevelNodeValueDistribution);
        grapLevelStatPanel.add(this.topLevelPredecessorCountDistribution);
        grapLevelStatPanel.add(this.topLevelSuccessorCountDistribution);
        grapLevelStatPanel.add(this.topLevelEdgeValueDistribution);
        grapLevelStatPanel.add(this.topLevelDistributionChart);
        //graphStatPanel.add(this.topLevelGraphNodeCountDistribution);
        //graphStatPanel.add(this.topLevelAverageShortestReachDistributionLineChart);

        chartTabbedPane = new JTabbedPane();
        chartTabbedPane.addTab("V. D.", null, grapLevelStatPanel, "VALUE DISTRIBUTIONS");
        chartTabbedPane.addTab("L. D.", null, setLabelDistributionChart(), "LABEL DISTRIBUTIONS");

        return chartTabbedPane;
    }

    private JPanel setTopLevelTabPane() {
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout(1,1));
        tablePanel.setBackground(Color.WHITE);

        this.topLevelTabbedPane = new JTabbedPane();
        this.topLevelTabbedPane.setFont(MyDirectGraphVars.tahomaPlainFont12);
        this.topLevelTabbedPane.setFocusable(false);
        this.topLevelTabbedPane.addTab("N.",  null, setNodeTable(), "NODES FOR THE CURRENT GRAPH.");
        this.topLevelTabbedPane.addTab("E.",  null, setEdgeTable(), "EDGES FOR THE CURRENT GRAPH.");
        this.topLevelTabbedPane.addTab("S.",  null, setTopLevelStatTable(), "STATISTICAL PROPERTIES FOR THE GRAPH.");
        this.topLevelTabbedPane.addTab("P.",  null, setPathFindTable(), "EXPLORE SHORTEST DISTANCES");
        this.topLevelTabbedPane.addChangeListener(new ChangeListener() {
            @Override public void stateChanged(ChangeEvent ae) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        if (topLevelTabbedPane.getSelectedIndex() == 3) {
                            MyProgressBar pb = new MyProgressBar(false);
                            graphFilterPanel.setVisible(false);

                            startNode = null;
                            isTableUpdating = false;
                            visitedNodes = null;
                            pb.updateValue(10, 100);

                            while (destTable.getRowCount() > 0) {
                                int row = destTable.getRowCount() - 1;
                                ((DefaultTableModel) destTable.getModel()).removeRow(row);
                            }
                            pb.updateValue(40, 100);

                            Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
                            LinkedHashMap<String, Long> valueMap = new LinkedHashMap<>();
                            for (MyDirectNode n : nodes) {
                                valueMap.put(n.getName(), (long) n.getCurrentValue());
                            }
                            valueMap = MyDirectGraphSysUtil.sortMapByLongValue(valueMap);
                            pb.updateValue(70, 100);

                            int i=0;
                            for (String n : valueMap.keySet()) {
                                ((MyDirectNode) MyDirectGraphVars.directGraph.vRefs.get(n)).setCurrentValue(valueMap.get(n));
                                ((DefaultTableModel) destTable.getModel()).addRow(new String[]{"" + (++i), n, MyDirectGraphMathUtil.getCommaSeperatedNumber(valueMap.get(n))});
                            }
                            pb.updateValue(90, 100);

                            MyDirectGraphVars.getDirectGraphViewer().vc.edgeValueLabel.setVisible(false);
                            MyDirectGraphVars.getDirectGraphViewer().vc.clusteringSectorLabel.setVisible(false);
                            MyDirectGraphVars.getDirectGraphViewer().vc.edgeLabelLabel.setVisible(false);
                            MyDirectGraphVars.getDirectGraphViewer().vc.edgeValueExcludeLabel.setVisible(false);
                            MyDirectGraphVars.getDirectGraphViewer().vc.edgeLabelExcludeLabel.setVisible(false);
                            MyDirectGraphVars.getDirectGraphViewer().vc.edgeLabelExcludeSymbolComboBoxMenu.setVisible(false);
                            MyDirectGraphVars.getDirectGraphViewer().vc.edgeLabelValueExcludeComboBoxMenu.setVisible(false);
                            MyDirectGraphVars.getDirectGraphViewer().vc.edgeValueExcludeSymbolComboBoxMenu.setVisible(false);
                            MyDirectGraphVars.getDirectGraphViewer().vc.edgeValueExcludeTxt.setVisible(false);
                            MyDirectGraphVars.getDirectGraphViewer().vc.edgeValueComboBoxMenu.setVisible(false);
                            MyDirectGraphVars.getDirectGraphViewer().vc.edgeLabelComboBoxMenu.setVisible(false);
                            MyDirectGraphVars.getDirectGraphViewer().vc.edgeLabelCheckBox.setVisible(false);
                            MyDirectGraphVars.getDirectGraphViewer().vc.edgeValueCheckBox.setVisible(false);
                            MyDirectGraphVars.getDirectGraphViewer().vc.removeEdgeCheckBox.setVisible(false);
                            MyDirectGraphVars.getDirectGraphViewer().vc.clusteringSelector.setVisible(false);
                            Collection<MyDirectEdge> edges = MyDirectGraphVars.directGraph.getEdges();
                            for (MyDirectEdge e : edges) {e.setCurrentValue(0);}
                            MyDirectGraphVars.getDirectGraphViewer().revalidate();
                            MyDirectGraphVars.getDirectGraphViewer().repaint();
                            pb.updateValue(100, 100);
                            pb.dispose();
                        } else if (topLevelTabbedPane.getSelectedIndex() == 0){
                            MyDirectGraphVars.getDirectGraphViewer().directGraphViewerMouseListener.setDefaultView();
                        }
                    }
                }).start();
            }
        });
        tablePanel.add(this.topLevelTabbedPane, BorderLayout.CENTER);
        TitledBorder border = BorderFactory.createTitledBorder("");
        border.setTitleFont(MyDirectGraphVars.tahomaBoldFont11);
       // tablePanel.setBorder(BorderFactory.createTitledBorder(border, "", TitledBorder.LEFT, TitledBorder.TOP));

        this.graphFilterPanel = new MyDirectGraphRemovalPanel();
        tablePanel.add(graphFilterPanel, BorderLayout.SOUTH);
        return tablePanel;
    }

    public MyDirectGraphRemovalPanel graphFilterPanel;

    private JTabbedPane setNodeLevelTabPane() {
        this.nodeLevelTabbedPane = new JTabbedPane();
        this.nodeLevelTabbedPane.setFocusable(false);
        this.nodeLevelTabbedPane.setOpaque(false);
        this.nodeLevelTabbedPane.setFont(MyDirectGraphVars.tahomaPlainFont12);
        this.nodeLevelTabbedPane.addTab("N.", null, setNodeTable(), "NODES FOR THE CURRENT GRAPH.");
        this.nodeLevelTabbedPane.addTab("E.", null, setEdgeTable(), "EDGES FOR THE CURRENT GRAPH.");
        this.nodeLevelTabbedPane.addTab("S.", null, setNodeLevelStatTable(), "STATISTICAL PROPERTIES FOR THE CURRENT GRAPH.");
        return this.nodeLevelTabbedPane;
    }

    private JTabbedPane setMultiNodeLevelTabPane() {
        this.multiNodeLevelTabbedPane = new JTabbedPane();
        this.multiNodeLevelTabbedPane.setFocusable(false);
        this.multiNodeLevelTabbedPane.setOpaque(false);
        this.multiNodeLevelTabbedPane.setFont(MyDirectGraphVars.tahomaPlainFont12);
        this.multiNodeLevelTabbedPane.addTab("N.", null, setNodeTable(), "NODES FOR THE CURRENT GRAPH.");
        this.multiNodeLevelTabbedPane.addTab("E.", null, setEdgeTable(), "EDGES FOR THE CURRENT GRAPH.");
        this.multiNodeLevelTabbedPane.addTab("S.", null, setMultiNodeLevelStatTable(), "STATISTICAL PROPERTIES FOR THE CURRENT GRAPH.");
        return this.multiNodeLevelTabbedPane;
    }

    public JTextField nodeListTableNodeSearchTxt;

    private JPanel setNodeTable() {
        JPanel componentPanel = new JPanel();
        try {
            componentPanel.setLayout(new BorderLayout(3, 3));
            componentPanel.setBackground(Color.WHITE);

            JPanel tablePanel = new JPanel();
            tablePanel.setLayout(new GridLayout(2, 1));
            tablePanel.setBackground(Color.WHITE);

            JPanel bottomTablePanel = new JPanel();
            bottomTablePanel.setLayout(new BorderLayout(3, 3));
            bottomTablePanel.setBackground(Color.WHITE);

            String[] bottomTableColumns = {"NO.", "NODE", "V."};
            String[][] bottomTableData = {};

            DefaultTableModel bottomTableModel = new DefaultTableModel(bottomTableData, bottomTableColumns);
            this.nodeListTable = new JTable(bottomTableModel) {
                public String getToolTipText(MouseEvent e) {
                    String tip = null;
                    Point p = e.getPoint();
                    int rowIndex = rowAtPoint(p);
                    int colIndex = columnAtPoint(p);
                    try {tip = getValueAt(rowIndex, colIndex).toString();
                    } catch (RuntimeException e1) {}
                    return tip;
                }
            };

            String[] toolTips = {"NO.", "NODE", "NODE VALUE"};
            MyTableToolTipHeader tooltipHeader = new MyTableToolTipHeader(this.nodeListTable.getColumnModel());
            tooltipHeader.setToolTipStrings(toolTips);
            this.nodeListTable.setTableHeader(tooltipHeader);

            Collection<MyDirectNode> bottomTableNodes = MyDirectGraphVars.directGraph.getVertices();
            LinkedHashMap<String, Float> bottomTableSortedNodes = new LinkedHashMap<>();
            for (MyDirectNode n : bottomTableNodes) {
                bottomTableSortedNodes.put(n.getName(), n.getCurrentValue());
            }
            bottomTableSortedNodes = MyDirectGraphSysUtil.sortMapByFloatValue(bottomTableSortedNodes);
            int i = -0;
            for (String n : bottomTableSortedNodes.keySet()) {
                bottomTableModel.addRow(
                        new String[]{
                                String.valueOf(++i),
                                n,
                                MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.oneDecimalFormat(bottomTableSortedNodes.get(n))).split("\\.")[0]
                        }
                );
            }

            this.nodeListTable.setRowHeight(20);
            this.nodeListTable.setBackground(Color.WHITE);
            this.nodeListTable.setFont(MyDirectGraphVars.f_pln_12);
            this.nodeListTable.getTableHeader().setFont(MyDirectGraphVars.tahomaBoldFont12);
            this.nodeListTable.getTableHeader().setOpaque(false);
            this.nodeListTable.getTableHeader().setBackground(new Color(0, 0, 0, 0f));
            this.nodeListTable.getColumnModel().getColumn(0).setPreferredWidth(45);
            this.nodeListTable.getColumnModel().getColumn(1).setPreferredWidth(80);
            this.nodeListTable.getColumnModel().getColumn(2).setPreferredWidth(40);
            this.nodeListTable.addComponentListener(new ComponentListener() {
                @Override public void componentResized(ComponentEvent e) {
                    new Thread(new Runnable() {
                        @Override public void run() {}
                    }).start();
                }
                @Override public void componentMoved(ComponentEvent e) {}
                @Override public void componentShown(ComponentEvent e) {}
                @Override public void componentHidden(ComponentEvent e) {}
            });

            JButton bottomTableNodeSelectBtn = new JButton("SEL.");
            bottomTableNodeSelectBtn.setFont(MyDirectGraphVars.tahomaPlainFont12);
            bottomTableNodeSelectBtn.setFocusable(false);
            this.nodeListTableNodeSearchTxt = new JTextField();
            this.nodeListTableNodeSearchTxt.setBackground(Color.WHITE);
            this.nodeListTableNodeSearchTxt.setBorder(BorderFactory.createLoweredSoftBevelBorder());
            JPanel bottomTableSearchAndSavePanel = MyTableUtil.searchAndSaveDataPanelForJTable2(this, this.nodeListTableNodeSearchTxt, bottomTableNodeSelectBtn, bottomTableModel, nodeListTable);
            this.nodeListTableNodeSearchTxt.setFont(MyDirectGraphVars.f_bold_12);
            this.nodeListTableNodeSearchTxt.setToolTipText("TYPE A NODE NAME TO SEARCH");
            this.nodeListTableNodeSearchTxt.setPreferredSize(new Dimension(90, 19));
            bottomTableNodeSelectBtn.setPreferredSize(new Dimension(50, 19));
            bottomTableNodeSelectBtn.setToolTipText("SELECT A NODE");
            bottomTableNodeSelectBtn.removeActionListener(this);
            bottomTableNodeSelectBtn.removeActionListener(this);
            bottomTableSearchAndSavePanel.remove(bottomTableNodeSelectBtn);

            this.nodeListTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            this.nodeListTable.setSelectionBackground(Color.LIGHT_GRAY);
            this.nodeListTable.setForeground(Color.BLACK);
            this.nodeListTable.setSelectionForeground(Color.BLACK);
            this.nodeListTable.setFocusable(false);
            this.nodeListTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent event) {
                    MyDirectGraphVars.currentThread = new Thread(new Runnable() {
                        @Override public void run() {
                            try {
                                if (nodeListTable.getSelectedRow() >= 0) {
                                    MyDirectNode n = (MyDirectNode) MyDirectGraphVars.directGraph.vRefs.get(nodeListTable.getValueAt(nodeListTable.getSelectedRow(), 1));
                                    if (n != MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode) {
                                        MyDirectGraphNodeSelecter graphNodeSearcher = new MyDirectGraphNodeSelecter();
                                        graphNodeSearcher.setSelectedNode(n);
                                    }
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                    MyDirectGraphVars.currentThread.start();
                    try {
                        MyDirectGraphVars.currentThread.join();
                        MyDirectGraphVars.currentThread = null;
                    } catch (Exception ex) {
                        MyDirectGraphVars.currentThread = null;
                    }
                }
            });

            JScrollPane bottomTableScrollPane = new JScrollPane(this.nodeListTable);
            bottomTableScrollPane.setOpaque(false);
            bottomTableScrollPane.setBackground(new Color(0, 0, 0, 0f));
            bottomTableScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(7, 0));
            bottomTableScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 7));

            JPanel topTablePanel = new JPanel();
            topTablePanel.setLayout(new BorderLayout(3, 3));
            topTablePanel.setBackground(Color.WHITE);

            String[] topTableColumns = {"NO.", "CURRENT N.", "V."};
            String[][] topTableData = {};
            DefaultTableModel topTableModel = new DefaultTableModel(topTableData, topTableColumns);
            this.currentNodeListTable = new JTable(topTableModel) {
                public String getToolTipText(MouseEvent e) {
                    String tip = null;
                    Point p = e.getPoint();
                    int rowIndex = rowAtPoint(p);
                    int colIndex = columnAtPoint(p);
                    try {tip = getValueAt(rowIndex, colIndex).toString();
                    } catch (RuntimeException e1) {}
                    return tip;
                }
            };

            String [] currentNodeTableHeaderTooltips = {"NO.", "CURRENT NODE", "CURRENT NODE VALUE"};
            MyTableToolTipHeader currentNodeTooltipHeader = new MyTableToolTipHeader(this.currentNodeListTable.getColumnModel());
            currentNodeTooltipHeader.setToolTipStrings(currentNodeTableHeaderTooltips);
            this.currentNodeListTable.setTableHeader(currentNodeTooltipHeader);

            Collection<MyDirectNode> topTableNodes = MyDirectGraphVars.directGraph.getVertices();
            LinkedHashMap<String, Float> topTableSortedNodes = new LinkedHashMap<>();
            for (MyDirectNode n : topTableNodes) {
                topTableSortedNodes.put(n.getName(), n.getCurrentValue());
            }

            topTableSortedNodes = MyDirectGraphSysUtil.sortMapByFloatValue(topTableSortedNodes);
            int k = -0;
            for (String n : topTableSortedNodes.keySet()) {
                topTableModel.addRow(
                    new String[]{
                        String.valueOf(++k),
                        n,
                        MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.oneDecimalFormat(topTableSortedNodes.get(n))).split("\\.")[0]
                    });
            }

            this.currentNodeListTable.setRowHeight(20);
            this.currentNodeListTable.setBackground(Color.WHITE);
            this.currentNodeListTable.setFont(MyDirectGraphVars.f_pln_12);
            this.currentNodeListTable.getTableHeader().setFont(MyDirectGraphVars.tahomaBoldFont12);
            this.currentNodeListTable.getTableHeader().setOpaque(false);
            this.currentNodeListTable.getTableHeader().setBackground(new Color(0, 0, 0, 0f));
            this.currentNodeListTable.getColumnModel().getColumn(0).setPreferredWidth(45);
            this.currentNodeListTable.getColumnModel().getColumn(1).setPreferredWidth(80);
            this.currentNodeListTable.getColumnModel().getColumn(2).setPreferredWidth(40);
            this.currentNodeListTable.addComponentListener(new ComponentListener() {
                @Override public void componentResized(ComponentEvent e) {
                    new Thread(new Runnable() {
                        @Override public void run() {}
                    }).start();
                }
                @Override public void componentMoved(ComponentEvent e) {}
                @Override public void componentShown(ComponentEvent e) {}
                @Override public void componentHidden(ComponentEvent e) {}
            });

            JTextField topTableNodeSearchTxt = new JTextField();
            JButton topTableNodeSelectBtn = new JButton("SEL.");
            topTableNodeSelectBtn.setFont(MyDirectGraphVars.tahomaPlainFont12);
            topTableNodeSelectBtn.setFocusable(false);
            topTableNodeSearchTxt.setBackground(Color.WHITE);
            topTableNodeSearchTxt.setBorder(BorderFactory.createLoweredSoftBevelBorder());
            JPanel topTableSearchAndSavePanel = MyTableUtil.searchAndSaveDataPanelForJTable2(this, topTableNodeSearchTxt, topTableNodeSelectBtn, topTableModel, currentNodeListTable);
            topTableNodeSearchTxt.setFont(MyDirectGraphVars.f_bold_12);
            topTableNodeSearchTxt.setToolTipText("TYPE A NODE NAME TO SEARCH");
            topTableNodeSearchTxt.setPreferredSize(new Dimension(90, 19));
            topTableNodeSelectBtn.setPreferredSize(new Dimension(54, 19));
            topTableNodeSelectBtn.removeActionListener(this);
            topTableNodeSelectBtn.removeActionListener(this);
            topTableSearchAndSavePanel.remove(topTableNodeSelectBtn);

            this.currentNodeListTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            this.currentNodeListTable.setSelectionBackground(Color.LIGHT_GRAY);
            this.currentNodeListTable.setForeground(Color.BLACK);
            this.currentNodeListTable.setSelectionForeground(Color.BLACK);
            this.currentNodeListTable.setFocusable(false);
            this.currentNodeListTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            this.currentNodeListTable.setRowSorter(MyTableUtil.setJTableRowSorterWithTextField(topTableModel, topTableNodeSearchTxt));

            JScrollPane topTableScrollPane = new JScrollPane(this.currentNodeListTable);
            topTableScrollPane.setOpaque(false);
            topTableScrollPane.setBackground(new Color(0, 0, 0, 0f));
            topTableScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(7, 0));
            topTableScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 7));

            bottomTablePanel.add(bottomTableScrollPane, BorderLayout.CENTER);
            bottomTablePanel.add(bottomTableSearchAndSavePanel, BorderLayout.SOUTH);
            topTablePanel.add(topTableScrollPane, BorderLayout.CENTER);
            topTablePanel.add(topTableSearchAndSavePanel, BorderLayout.SOUTH);
            tablePanel.add(topTablePanel);
            tablePanel.add(bottomTablePanel);

            componentPanel.add(tablePanel, BorderLayout.CENTER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return componentPanel;
    }

    private boolean isEdgeTableSelected = false;
    private JPanel setEdgeTable() {
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout(3,3));
        tablePanel.setBackground(Color.WHITE);

        JPanel bottomTablePanel = new JPanel();
        bottomTablePanel.setLayout(new BorderLayout(3,3));
        bottomTablePanel.setBackground(Color.WHITE);

        String [] bottomTableColumns = {"NO.", "S.", "D.", "V."};
        String [][] bottomTableData = {};
        DefaultTableModel bottomTableModel = new DefaultTableModel(bottomTableData, bottomTableColumns);
        this.edgeTable = new JTable(bottomTableModel) {
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

        String [] toolTips = {"NO.", "SOURCE", "DEST", "CURRENT EDGE VALUE"};
        MyTableToolTipHeader tooltipHeader = new MyTableToolTipHeader(this.edgeTable.getColumnModel());
        tooltipHeader.setToolTipStrings(toolTips);
        this.edgeTable.setTableHeader(tooltipHeader);

        Collection<MyDirectEdge> edges = MyDirectGraphVars.directGraph.getEdges();
        LinkedHashMap<String, Float> sortedEdges = new LinkedHashMap<>();
        for (MyDirectEdge e : edges) {
            String ename = e.getSource().getName() + "-" + e.getDest().getName();
            sortedEdges.put(ename, (float)e.getContribution());
        }
        sortedEdges = MyDirectGraphSysUtil.sortMapByFloatValue(sortedEdges);
        int i=-0;
        for (String e : sortedEdges.keySet()) {
            String source = e.split("-")[0];
            String dest = e.split("-")[1];
            bottomTableModel.addRow(
                    new String[]{
                            "" + (++i),
                            source,
                            dest,
                            (MyDirectGraphVars.getDirectGraphViewer().vc.edgeValueComboBoxMenu.getSelectedIndex() == 0 ?
                                    "0" : MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(sortedEdges.get(e))))
                    });
        }

        edgeTable.setRowHeight(20);
        edgeTable.setBackground(Color.WHITE);
        edgeTable.setFont(MyDirectGraphVars.f_pln_12);
        edgeTable.getTableHeader().setFont(MyDirectGraphVars.tahomaBoldFont12);
        edgeTable.getTableHeader().setOpaque(false);
        edgeTable.getTableHeader().setBackground(new Color(0,0,0,0f));
        edgeTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        edgeTable.getColumnModel().getColumn(1).setPreferredWidth(65);
        edgeTable.getColumnModel().getColumn(2).setPreferredWidth(65);
        edgeTable.getColumnModel().getColumn(3).setPreferredWidth(40);
        edgeTable.getColumnModel().getColumn(3).setMaxWidth(55);

        JTextField edgeTableNodeTableNodeSearchTxt = new JTextField();
        JButton edgeTableNodeSelectBtn = new JButton("SEL.");
        edgeTableNodeSelectBtn.setFont(MyDirectGraphVars.tahomaPlainFont12);
        edgeTableNodeSelectBtn.setFocusable(false);
        edgeTableNodeTableNodeSearchTxt.setBackground(Color.WHITE);
        edgeTableNodeTableNodeSearchTxt.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        JPanel bottomTableSearchAndSavePanel = MyTableUtil.searchAndSaveDataPanelForJTable2(this, edgeTableNodeTableNodeSearchTxt, edgeTableNodeSelectBtn, bottomTableModel, edgeTable);
        edgeTableNodeTableNodeSearchTxt.setFont(MyDirectGraphVars.f_bold_12);
        edgeTableNodeTableNodeSearchTxt.setToolTipText("TYPE A NODE NAME TO SEARCH");
        edgeTableNodeTableNodeSearchTxt.setPreferredSize(new Dimension(90, 19));
        edgeTableNodeSelectBtn.setPreferredSize(new Dimension(54, 19));
        bottomTableSearchAndSavePanel.remove(edgeTableNodeSelectBtn);

        edgeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        edgeTable.setSelectionBackground(Color.LIGHT_GRAY);
        edgeTable.setForeground(Color.BLACK);
        edgeTable.setSelectionForeground(Color.BLACK);
        edgeTable.setFocusable(false);
        edgeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        edgeTable.setRowSorter(MyTableUtil.setJTableRowSorterWithTextField(bottomTableModel, edgeTableNodeTableNodeSearchTxt));
        edgeTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        try {
                            if (!isEdgeTableSelected) {
                                isEdgeTableSelected = true;
                                MyDirectNode source = (MyDirectNode) MyDirectGraphVars.directGraph.vRefs.get(edgeTable.getValueAt(edgeTable.getSelectedRow(), 1));
                                MyDirectNode dest = (MyDirectNode) MyDirectGraphVars.directGraph.vRefs.get(edgeTable.getValueAt(edgeTable.getSelectedRow(), 2));
                                MyDirectGraphNodeSelecter graphNodeSearcher = new MyDirectGraphNodeSelecter();
                                graphNodeSearcher.setEdgeNodes(source, dest);
                                isEdgeTableSelected = false;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        JScrollPane edgeTableScrollPane = new JScrollPane(edgeTable);
        edgeTableScrollPane.setOpaque(false);
        edgeTableScrollPane.setBackground(new Color(0,0,0,0f));
        edgeTableScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(6, 0));
        edgeTableScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 6));

        tablePanel.add(edgeTableScrollPane, BorderLayout.CENTER);
        tablePanel.add(bottomTableSearchAndSavePanel, BorderLayout.SOUTH);
        return tablePanel;
    }

    private String [][] topLevelStatTablePropertyValuePairs;
    private JPanel setTopLevelStatTable() {
        JPanel statTablePanel = new JPanel();
        statTablePanel.setLayout(new BorderLayout(3,3));
        statTablePanel.setBackground(Color.WHITE);

        String [] statTableColumns = {"PROPERTY", "V."};
        String [][] statTableData = {};
        DefaultTableModel statTableModel = new DefaultTableModel(statTableData, statTableColumns);
        this.topLevelStatTablePropertyValuePairs = updateTopLevelGraphStatToolTips();

        this.statTable = new JTable(statTableModel) {
            public String getToolTipText(MouseEvent e) {
                String tip = null;
                Point p = e.getPoint();
                int rowIndex = rowAtPoint(p);
                int colIndex = columnAtPoint(p);
                try {
                    tip = topLevelStatTablePropertyValuePairs[rowIndex][colIndex];
                } catch (RuntimeException e1) {}
                return tip;
            }
        };

        String [] toolTips = {"PROPERTY.", "VALUE"};
        MyTableToolTipHeader tooltipHeader = new MyTableToolTipHeader(this.statTable.getColumnModel());
        tooltipHeader.setToolTipStrings(toolTips);
        this.statTable.setTableHeader(tooltipHeader);
        this.statTable.setRowHeight(19);
        this.statTable.setBackground(Color.WHITE);
        this.statTable.setFont(MyDirectGraphVars.tahomaPlainFont12);
        this.statTable.setFocusable(false);
        this.statTable.getTableHeader().setFont(MyDirectGraphVars.tahomaBoldFont12);
        this.statTable.getTableHeader().setOpaque(false);
        this.statTable.getTableHeader().setBackground(new Color(0,0,0,0f));
        this.statTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        this.statTable.setPreferredSize(new Dimension(145, 1199));
        this.statTable.setForeground(Color.BLACK);
        this.statTable.getColumnModel().getColumn(1).setPreferredWidth(60);
        this.statTable.setSelectionForeground(Color.BLACK);
        this.statTable.setSelectionBackground(Color.LIGHT_GRAY);

        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"G.: ", topLevelStatTablePropertyValuePairs[0][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"NODES", topLevelStatTablePropertyValuePairs[1][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"EDGES", topLevelStatTablePropertyValuePairs[2][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"MAX. D.", topLevelStatTablePropertyValuePairs[3][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"MAX. NO. OF N.", topLevelStatTablePropertyValuePairs[4][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"MIN. NO. OF N.", topLevelStatTablePropertyValuePairs[5][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"NO. OF G.", topLevelStatTablePropertyValuePairs[6][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"AVG. NO. OF N. BY G.", topLevelStatTablePropertyValuePairs[7][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"G. WITH MAX. NO. OF N.", topLevelStatTablePropertyValuePairs[8][1].split("\\[")[0]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"G. WITH MIN. NO. OF N.", topLevelStatTablePropertyValuePairs[9][1].split("\\[")[0]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"R. N.", topLevelStatTablePropertyValuePairs[10][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"B. N.", topLevelStatTablePropertyValuePairs[11][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"G. N.", topLevelStatTablePropertyValuePairs[12][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"AVG. N. V.", topLevelStatTablePropertyValuePairs[13][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"MAX. N. V.", topLevelStatTablePropertyValuePairs[14][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"MIN. N. V.", topLevelStatTablePropertyValuePairs[15][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"STD. N. V.", topLevelStatTablePropertyValuePairs[16][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"MAX. P.", topLevelStatTablePropertyValuePairs[17][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"AVG. P.", topLevelStatTablePropertyValuePairs[18][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"MIN. P.", topLevelStatTablePropertyValuePairs[19][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"MAX. S.", topLevelStatTablePropertyValuePairs[20][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"AVG. S.", topLevelStatTablePropertyValuePairs[21][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"MIN. S.", topLevelStatTablePropertyValuePairs[22][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"MAX. E. V.", "0"});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"AVG. E. V.", "0"});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"MIN. E. V.", "0"});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"STD. E. V.", "0"});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"ISLAND N.", MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.directGraph.getIslandNodes())});

        JScrollPane graphStatTableScrollPane = new JScrollPane(this.statTable);
        graphStatTableScrollPane.setOpaque(false);
        graphStatTableScrollPane.setPreferredSize(new Dimension(150, 1200));
        graphStatTableScrollPane.setBackground(Color.WHITE);
        graphStatTableScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(6, 0));
        graphStatTableScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 6));

        statTablePanel.add(graphStatTableScrollPane, BorderLayout.CENTER);
        return statTablePanel;
    }

    private JPanel setMultiNodeLevelStatTable() {
        JPanel statTablePanel = new JPanel();
        statTablePanel.setLayout(new BorderLayout(3,3));
        statTablePanel.setBackground(Color.WHITE);

        String [][] nodeLevelStatTableToolTipPairs = {
                {"GRAPH NUMBER", MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.directGraph.getNodeGraphNumber(MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode))},
                {"NODES", MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.directGraph.getVertexCount())},
                {"EDGES", MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.directGraph.getEdgeCount())},
                {"PREDECESSOR COUNT", MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.getDirectGraphViewer().multiNodePredecessorSet.size())},
                {"SUCCESSOR COUNT", MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.getDirectGraphViewer().multiNodeSuccessorSet.size())},
                {"SHARED PREDECESSOR COUNT", MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.getDirectGraphViewer().multiNodeSharedPredecessorSet.size())},
                {"SHARED SUCCESSOR COUNT", MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.getDirectGraphViewer().multiNodeSharedSuccessorSet.size())},
                {"MAX. IN-EDGE VALUE", MyDirectGraphMathUtil.getCommaSeperatedNumber((long) MyDirectGraphVars.directGraph.getMaxInEdgeValueForSelectedMultiNodes())},
                {"AVG. IN-EDGE VALUE", MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(MyDirectGraphVars.directGraph.getAverageInEdgeValueForSelectedMultiNodes()))},
                {"MIN. IN-EDGE VALUE", MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(MyDirectGraphVars.directGraph.getMinInEdgeValueForSelectedMultiNodes()))},
                {"IN-EDGE VALUE STANDARD DEVIATION", MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(MyDirectGraphVars.directGraph.getInEdgValueStandardDeviationForSelectedMultiNode()))},
                {"MAX. OUT-EDGE VALUE", MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(MyDirectGraphVars.directGraph.getMaxOutEdgeValueForSelectedMultiNodes()))},
                {"AVG. OUT-EDGE VALUE", MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(MyDirectGraphVars.directGraph.getAverageOutEdgeValueForSelectedMultiNodes()))},
                {"MIN. OUT-EDGE VALUE", MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(MyDirectGraphVars.directGraph.getMinOutEdgeValueForSelectedMultiNodes()))},
                {"OUT-EDGE VALUE STANDARD DEVIATION", MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(MyDirectGraphVars.directGraph.getOutEdgValueStandardDeviationForSelectedMultiNode()))}
        };

        String [] statTableColumns = {"PROPERTY", "V."};
        String [][] statTableData = {};
        DefaultTableModel statTableModel = new DefaultTableModel(statTableData, statTableColumns);

        this.statTable = new JTable(statTableModel) {
            public String getToolTipText(MouseEvent e) {
                String tip = null;
                Point p = e.getPoint();
                int rowIndex = rowAtPoint(p);
                int colIndex = columnAtPoint(p);
                try {
                    tip = nodeLevelStatTableToolTipPairs[rowIndex][colIndex];
                } catch (RuntimeException e1) {}
                return tip;
            }
        };

        String [] toolTips = {"PROPERTY.", "VALUE"};
        MyTableToolTipHeader tooltipHeader = new MyTableToolTipHeader(this.statTable.getColumnModel());
        tooltipHeader.setToolTipStrings(toolTips);
        this.statTable.setTableHeader(tooltipHeader);
        this.statTable.setRowHeight(19);
        this.statTable.setBackground(Color.WHITE);
        this.statTable.setFont(MyDirectGraphVars.tahomaPlainFont12);
        this.statTable.setFocusable(false);
        this.statTable.getTableHeader().setFont(MyDirectGraphVars.tahomaBoldFont12);
        this.statTable.getTableHeader().setOpaque(false);
        this.statTable.getTableHeader().setBackground(new Color(0,0,0,0f));
        this.statTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        this.statTable.setPreferredSize(new Dimension(145, 1199));
        this.statTable.setForeground(Color.BLACK);
        this.statTable.getColumnModel().getColumn(1).setPreferredWidth(60);
        this.statTable.setSelectionForeground(Color.BLACK);
        this.statTable.setSelectionBackground(Color.LIGHT_GRAY);

        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"G. N.", nodeLevelStatTableToolTipPairs[0][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"NODES", nodeLevelStatTableToolTipPairs[1][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"EDGES", nodeLevelStatTableToolTipPairs[2][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"P. C.", nodeLevelStatTableToolTipPairs[3][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"S. C", nodeLevelStatTableToolTipPairs[4][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"MAX. IN-E. V.", nodeLevelStatTableToolTipPairs[5][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"AVG. IN-E. V.", nodeLevelStatTableToolTipPairs[6][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"MIN. IN-E. V.", nodeLevelStatTableToolTipPairs[7][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"STD. IN-E. V.", nodeLevelStatTableToolTipPairs[8][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"MAX. OUT-E. V.", nodeLevelStatTableToolTipPairs[9][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"AVG. OUT-E. V.", nodeLevelStatTableToolTipPairs[10][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"MIN. OUT-E. V.", nodeLevelStatTableToolTipPairs[11][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"STD. OUT-E. V.", nodeLevelStatTableToolTipPairs[12][1]});

        JScrollPane graphStatTableScrollPane = new JScrollPane(this.statTable);
        graphStatTableScrollPane.setOpaque(false);
        graphStatTableScrollPane.setPreferredSize(new Dimension(150, 1200));
        graphStatTableScrollPane.setBackground(Color.WHITE);
        graphStatTableScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(6, 0));
        graphStatTableScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 6));

        statTablePanel.add(graphStatTableScrollPane, BorderLayout.CENTER);
        return statTablePanel;
    }

    private JPanel setNodeLevelStatTable() {
        JPanel statTablePanel = new JPanel();
        statTablePanel.setLayout(new BorderLayout(3,3));
        statTablePanel.setBackground(Color.WHITE);

        String [][] nodeLevelStatTableToolTipPairs = {
                {"GRAPH NUMBER", MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.directGraph.getNodeGraphNumber(MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode))},
                {"NODES", MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.directGraph.getVertexCount())},
                {"EDGES", MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.directGraph.getEdgeCount())},
                {"PREDECESSOR COUNT", MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.directGraph.getPredecessorCountForSelectedNode())},
                {"SUCCESSOR COUNT", MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.directGraph.getSuccessorCountForSelectedNode())},
                {"MAX. IN-EDGE VALUE", MyDirectGraphMathUtil.getCommaSeperatedNumber((long) MyDirectGraphVars.directGraph.getMaxInEdgeValueForSelectedNode())},
                {"AVG. IN-EDGE VALUE", MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(MyDirectGraphVars.directGraph.getAverageInEdgeValueForSelectedNode()))},
                {"MIN. IN-EDGE VALUE", MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(MyDirectGraphVars.directGraph.getMinInEdgeValueForSelectedNode()))},
                {"IN-EDGE VALUE STANDARD DEVIATION", MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(MyDirectGraphVars.directGraph.getInEdgValueStandardDeviationForSelectedNode()))},
                {"MAX. OUT-EDGE VALUE", MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(MyDirectGraphVars.directGraph.getMaxOutEdgeValueForSelectedNode()))},
                {"AVG. OUT-EDGE VALUE", MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(MyDirectGraphVars.directGraph.getAverageOutEdgeValueForSelectedNode()))},
                {"MIN. OUT-EDGE VALUE", MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(MyDirectGraphVars.directGraph.getMinOutEdgeValueForSelectedNode()))},
                {"OUT-EDGE VALUE STANDARD DEVIATION", MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(MyDirectGraphVars.directGraph.getOutEdgValueStandardDeviationForSelectedNode()))}
        };

        String [] statTableColumns = {"PROPERTY", "V."};
        String [][] statTableData = {};
        DefaultTableModel statTableModel = new DefaultTableModel(statTableData, statTableColumns);

        this.statTable = new JTable(statTableModel) {
            public String getToolTipText(MouseEvent e) {
                String tip = null;
                Point p = e.getPoint();
                int rowIndex = rowAtPoint(p);
                int colIndex = columnAtPoint(p);
                try {
                    tip = nodeLevelStatTableToolTipPairs[rowIndex][colIndex];
                } catch (RuntimeException e1) {}
                return tip;
            }
        };

        String [] toolTips = {"PROPERTY.", "VALUE"};
        MyTableToolTipHeader tooltipHeader = new MyTableToolTipHeader(this.statTable.getColumnModel());
        tooltipHeader.setToolTipStrings(toolTips);
        this.statTable.setTableHeader(tooltipHeader);
        this.statTable.setRowHeight(19);
        this.statTable.setBackground(Color.WHITE);
        this.statTable.setFont(MyDirectGraphVars.tahomaPlainFont12);
        this.statTable.setFocusable(false);
        this.statTable.getTableHeader().setFont(MyDirectGraphVars.tahomaBoldFont12);
        this.statTable.getTableHeader().setOpaque(false);
        this.statTable.getTableHeader().setBackground(new Color(0,0,0,0f));
        this.statTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        this.statTable.setPreferredSize(new Dimension(145, 1199));
        this.statTable.setForeground(Color.BLACK);
        this.statTable.getColumnModel().getColumn(1).setPreferredWidth(60);
        this.statTable.setSelectionForeground(Color.BLACK);
        this.statTable.setSelectionBackground(Color.LIGHT_GRAY);

        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"G. N.", nodeLevelStatTableToolTipPairs[0][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"NODES", nodeLevelStatTableToolTipPairs[1][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"EDGES", nodeLevelStatTableToolTipPairs[2][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"P. C.", nodeLevelStatTableToolTipPairs[3][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"S. C", nodeLevelStatTableToolTipPairs[4][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"MAX. IN-E. V.", nodeLevelStatTableToolTipPairs[5][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"AVG. IN-E. V.", nodeLevelStatTableToolTipPairs[6][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"MIN. IN-E. V.", nodeLevelStatTableToolTipPairs[7][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"STD. IN-E. V.", nodeLevelStatTableToolTipPairs[8][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"MAX. OUT-E. V.", nodeLevelStatTableToolTipPairs[9][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"AVG. OUT-E. V.", nodeLevelStatTableToolTipPairs[10][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"MIN. OUT-E. V.", nodeLevelStatTableToolTipPairs[11][1]});
        ((DefaultTableModel) this.statTable.getModel()).addRow(new String[]{"STD. OUT-E. V.", nodeLevelStatTableToolTipPairs[12][1]});

        JScrollPane graphStatTableScrollPane = new JScrollPane(this.statTable);
        graphStatTableScrollPane.setOpaque(false);
        graphStatTableScrollPane.setPreferredSize(new Dimension(150, 1200));
        graphStatTableScrollPane.setBackground(Color.WHITE);
        graphStatTableScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(6, 0));
        graphStatTableScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 6));

        statTablePanel.add(graphStatTableScrollPane, BorderLayout.CENTER);
        return statTablePanel;
    }

    private String [][] updateTopLevelGraphStatToolTips() {
        String [][] topLevelStatTablePropertyValuePairs = {
                {"NO. OF GRAPHS", MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.connectedComponentCountsByGraph.size())},
                {"NODES", MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.directGraph.getVertexCount())},
                {"EDGES", MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.directGraph.getEdgeCount())},
                {"MAX. DIAMETER", MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.diameter)},
                {"MAX. NO. OF NODES", MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.directGraph.getMaxNumberofNodesAmongGraphs())},
                {"MIN. NO. OF NODES", MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.directGraph.getMinNumberofNodesAmongGraphs())},
                {"NO. OF GRAPHS", MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.connectedComponentCountsByGraph.size())},
                {"AVG. NO. OF NODES BY GRAPH", MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(MyDirectGraphVars.directGraph.getAverageConnectedNodeCountByGraph()))},
                {"GRAPH WITH MAX. NO OF NODES", MyDirectGraphVars.directGraph.getGraphsWIthMaxNumberOfNodes()},
                {"GRAPH WITH MIN. NO OF NODES", MyDirectGraphVars.directGraph.getGraphsWithMinNumberOfNodes()},
                {"RED NODES", MyDirectGraphMathUtil.getCommaSeperatedNumber(getRedNodeCount()) + "[" + MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(getRedNodePercent()*100)) + "]"},
                {"BLUE NODES", MyDirectGraphMathUtil.getCommaSeperatedNumber(getBlueNodeCount()) + "[" + MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(getBlueNodePercent()*100)) + "]"},
                {"GREEN NODES", MyDirectGraphMathUtil.getCommaSeperatedNumber(getGreenNodeCount()) + "[" + MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(getGreenNodePercent()*100)) + "]"},
                {"AVG. NODE VALUE", MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(Double.parseDouble(getAverageNodeValue())))},
                {"MAX. NODE VALUE", MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(getMaximumNodeValue()))},
                {"MIN. NODE VALUE", MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(getMinimumNodeValue()))},
                {"NODE VALUE STANDARD DEVIATION", MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(getNodeValueStandardDeviation()))},
                {"MAX. PREDECESSOR COUNT", MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.directGraph.getMaxPredecessorCount())},
                {"AVG. PREDECESSOR COUNT", MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(getAveragePredecessorCount()))},
                {"MIN. PREDECESSOR COUNT", MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.directGraph.getMinPredecessorCount())},
                {"MAX. SUCCESSOR COUNT", MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.directGraph.getMaxSuccessorCount())},
                {"AVG. SUCCESSOR COUNT", MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(getAverageSuccessorCount()))},
                {"MIN. SUCCESSOR COUNT", MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.directGraph.getMinSuccesosrCount())},
                {"AVG. EDGE VALUE", MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(MyDirectGraphVars.directGraph.getAverageEdgeValue()))},
                {"MAX. EDGE VALUE", MyDirectGraphMathUtil.getCommaSeperatedNumber((long) MyDirectGraphVars.directGraph.getMaxEdgeValue())},
                {"MIN. EDGE VALUE", MyDirectGraphMathUtil.getCommaSeperatedNumber((long) MyDirectGraphVars.directGraph.getMinEdgeValue())},
                {"EDGE VALUE STANDARD DEVIATION", MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(MyDirectGraphVars.directGraph.getEdgValueStandardDeviation()))},
                {"ISLAND NODES: ", MyDirectGraphMathUtil.getCommaSeperatedNumber(MyDirectGraphVars.directGraph.getIslandNodes())}
        };
        return topLevelStatTablePropertyValuePairs;
    }


    public void setTopLevelDashboard(MyDirectGraphViewer directGraphViewer) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                removeAll();
                setBackground(Color.WHITE);
                setLayout(new BorderLayout(1, 1));
                MyDirectGraphVars.getDirectGraphViewer().vc = new MyDirectGraphController();

                JPanel graphViewer = new JPanel();
                graphViewer.setOpaque(false);
                graphViewer.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.0f));
                graphViewer.setLayout(new BorderLayout(3, 3));
                graphViewer.add(directGraphViewer, BorderLayout.CENTER);

                JSplitPane nodeListGraphSplitPane = new JSplitPane();
                nodeListGraphSplitPane.setDividerLocation((int) (MyDirectGraphSysUtil.getViewerWidth() * 0.095));
                nodeListGraphSplitPane.setDividerSize(5);
                nodeListGraphSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
                nodeListGraphSplitPane.setLeftComponent(setTopLevelTabPane());
                nodeListGraphSplitPane.setRightComponent(graphViewer);

                txtStatistics = new MyDirectGraphTextStatistics();

                JPanel statAndCheckBoxControlPanel = new JPanel();
                statAndCheckBoxControlPanel.setBackground(Color.WHITE);
                statAndCheckBoxControlPanel.setLayout(new BorderLayout(3,3));
                statAndCheckBoxControlPanel.add(txtStatistics, BorderLayout.CENTER);
                statAndCheckBoxControlPanel.add(MyDirectGraphVars.getDirectGraphViewer().vc.checkBoxControlPanel, BorderLayout.EAST);

                JPanel contentPanel = new JPanel();
                contentPanel.setBackground(Color.WHITE);
                contentPanel.setLayout(new BorderLayout(3,3));
                contentPanel.add(MyDirectGraphVars.getDirectGraphViewer().vc, BorderLayout.NORTH);
                contentPanel.add(nodeListGraphSplitPane, BorderLayout.CENTER);
                contentPanel.add(statAndCheckBoxControlPanel, BorderLayout.SOUTH);

                JSplitPane chartNodeListSplitPane = new JSplitPane();
                chartNodeListSplitPane.setDividerLocation((int) (MyDirectGraphSysUtil.getViewerWidth() * 0.133));
                chartNodeListSplitPane.setDividerSize(4);
                chartNodeListSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
                chartNodeListSplitPane.setLeftComponent(setTopLevelCharts());
                chartNodeListSplitPane.setRightComponent(contentPanel);

                MyDirectGraphVars.app.addComponentListener(new ComponentAdapter() {
                    public void componentResized(ComponentEvent evt) {
                        nodeListGraphSplitPane.setDividerLocation((int) (MyDirectGraphSysUtil.getViewerWidth() * 0.095));
                        chartNodeListSplitPane.setDividerLocation((int) (MyDirectGraphSysUtil.getViewerWidth() * 0.133));
                    }
                });

                add(chartNodeListSplitPane, BorderLayout.CENTER);
                MyDirectGraphVars.app.revalidate();
                MyDirectGraphVars.app.repaint();
            }
        });
    }

    public void setSelectedNodeLevelDashBoard(MyDirectGraphViewer directGraphViewer) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                removeAll();
                setBackground(Color.WHITE);
                setLayout(new BorderLayout(3, 3));

                JPanel graphViewer = new JPanel();
                graphViewer.setOpaque(false);
                graphViewer.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.0f));
                graphViewer.setLayout(new BorderLayout(3, 3));
                graphViewer.add(directGraphViewer, BorderLayout.CENTER);

                JSplitPane nodeListGraphSplitPane = new JSplitPane();
                nodeListGraphSplitPane.setDividerLocation((int) (MyDirectGraphSysUtil.getViewerWidth() * 0.095));
                nodeListGraphSplitPane.setDividerSize(5);
                nodeListGraphSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
                nodeListGraphSplitPane.setLeftComponent(setNodeLevelTabPane());
                nodeListGraphSplitPane.setRightComponent(graphViewer);

                txtStatistics = new MyDirectGraphTextStatistics();

                JPanel statAndCheckBoxControlPanel = new JPanel();
                statAndCheckBoxControlPanel.setBackground(Color.WHITE);
                statAndCheckBoxControlPanel.setLayout(new BorderLayout(3,3));
                statAndCheckBoxControlPanel.add(txtStatistics, BorderLayout.CENTER);
                statAndCheckBoxControlPanel.add(MyDirectGraphVars.getDirectGraphViewer().vc.checkBoxControlPanel, BorderLayout.EAST);

                JPanel contentPanel = new JPanel();
                contentPanel.setBackground(Color.WHITE);
                contentPanel.setLayout(new BorderLayout(3,3));
                contentPanel.add(MyDirectGraphVars.getDirectGraphViewer().vc, BorderLayout.NORTH);
                contentPanel.add(nodeListGraphSplitPane, BorderLayout.CENTER);
                contentPanel.add(statAndCheckBoxControlPanel, BorderLayout.SOUTH);

                JSplitPane chartNodeListSplitPane = new JSplitPane();
                chartNodeListSplitPane.setDividerLocation((int) (MyDirectGraphSysUtil.getViewerWidth() * 0.133));
                chartNodeListSplitPane.setDividerSize(4);
                chartNodeListSplitPane.getLeftComponent().setBackground(Color.WHITE);
                chartNodeListSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
                chartNodeListSplitPane.setLeftComponent(setNodeLevelCharts());
                chartNodeListSplitPane.setRightComponent(contentPanel);

                MyDirectGraphVars.app.addComponentListener(new ComponentAdapter() {
                    public void componentResized(ComponentEvent evt) {
                        nodeListGraphSplitPane.setDividerLocation((int) (MyDirectGraphSysUtil.getViewerWidth() * 0.095));
                        chartNodeListSplitPane.setDividerLocation((int) (MyDirectGraphSysUtil.getViewerWidth() * 0.133));
                    }
                });

                updateRemainingNodeTable();
                updateEdgeTable();

                add(chartNodeListSplitPane, BorderLayout.CENTER);
                MyDirectGraphVars.app.revalidate();
                MyDirectGraphVars.app.repaint();
            }
        });
    }

    public void setMultiNodeLevelDashBoard(MyDirectGraphViewer directGraphViewer) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                removeAll();
                setBackground(Color.WHITE);
                setLayout(new BorderLayout(3, 3));
                txtStatistics = new MyDirectGraphTextStatistics();

                JPanel checkBoxControlPanel = new JPanel();
                checkBoxControlPanel.setBackground(Color.WHITE);
                checkBoxControlPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3,3));
                checkBoxControlPanel.add(MyDirectGraphVars.getDirectGraphViewer().vc.weightedNodeColorCheckBox);
                checkBoxControlPanel.add(MyDirectGraphVars.getDirectGraphViewer().vc.removeEdgeCheckBox);
                checkBoxControlPanel.add(MyDirectGraphVars.getDirectGraphViewer().vc.nodeValueCheckBox);
                checkBoxControlPanel.add(MyDirectGraphVars.getDirectGraphViewer().vc.edgeValueCheckBox);

                JSplitPane nodeListGraphSplitPane = new JSplitPane();
                nodeListGraphSplitPane.setDividerLocation((int) (MyDirectGraphSysUtil.getViewerWidth() * 0.095));
                nodeListGraphSplitPane.setDividerSize(5);
                nodeListGraphSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
                nodeListGraphSplitPane.setLeftComponent(setMultiNodeLevelTabPane());
                nodeListGraphSplitPane.setRightComponent(directGraphViewer);

                txtStatistics = new MyDirectGraphTextStatistics();

                JPanel statAndCheckBoxControlPanel = new JPanel();
                statAndCheckBoxControlPanel.setBackground(Color.WHITE);
                statAndCheckBoxControlPanel.setLayout(new BorderLayout(3,3));
                statAndCheckBoxControlPanel.add(txtStatistics, BorderLayout.CENTER);
                statAndCheckBoxControlPanel.add(MyDirectGraphVars.getDirectGraphViewer().vc.checkBoxControlPanel, BorderLayout.EAST);

                JPanel contentPanel = new JPanel();
                contentPanel.setBackground(Color.WHITE);
                contentPanel.setLayout(new BorderLayout(3,3));
                contentPanel.add(MyDirectGraphVars.getDirectGraphViewer().vc, BorderLayout.NORTH);
                contentPanel.add(nodeListGraphSplitPane, BorderLayout.CENTER);
                contentPanel.add(statAndCheckBoxControlPanel, BorderLayout.SOUTH);

                JSplitPane chartNodeListSplitPane = new JSplitPane();
                chartNodeListSplitPane.setDividerLocation((int) (MyDirectGraphSysUtil.getViewerWidth() * 0.133));
                chartNodeListSplitPane.setDividerSize(4);
                chartNodeListSplitPane.getLeftComponent().setBackground(Color.WHITE);
                chartNodeListSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
                chartNodeListSplitPane.setLeftComponent(setMultiNodeLevelCharts());
                chartNodeListSplitPane.setRightComponent(contentPanel);

                MyDirectGraphVars.app.addComponentListener(new ComponentAdapter() {
                    public void componentResized(ComponentEvent evt) {
                        nodeListGraphSplitPane.setDividerLocation((int) (MyDirectGraphSysUtil.getViewerWidth() * 0.095));
                        chartNodeListSplitPane.setDividerLocation((int) (MyDirectGraphSysUtil.getViewerWidth() * 0.133));
                    }
                });


                updateEdgeTable();
                updateRemainingNodeTable();

                add(chartNodeListSplitPane, BorderLayout.CENTER);
                MyDirectGraphVars.app.revalidate();
                MyDirectGraphVars.app.repaint();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public int getRedNodeCount() {
        Collection<MyDirectNode> ns = MyDirectGraphVars.directGraph.getVertices();
        int cnt = 0;
        for (MyDirectNode n : ns) {
            if (n.getCurrentValue() > 0) {
                if (MyDirectGraphVars.directGraph.getPredecessorCount( n) == 0 && MyDirectGraphVars.directGraph.getSuccessorCount( n) > 0) {
                    cnt++;
                }
            }
        }
        return cnt;
    }

    public double getRedNodePercent() {
        Collection<MyDirectNode> ns = MyDirectGraphVars.directGraph.getVertices();
        int cnt = 0;
        for (MyDirectNode n : ns) {
            if (n.getCurrentValue() > 0) {
                if (MyDirectGraphVars.directGraph.getPredecessorCount( n) == 0 && MyDirectGraphVars.directGraph.getSuccessorCount( n) > 0) {
                    cnt++;
                }
            }
        }
        return ((double)cnt/ns.size());
    }

    public int getBlueNodeCount() {
        Collection<MyDirectNode> ns = MyDirectGraphVars.directGraph.getVertices();
        int cnt = 0;
        for (MyDirectNode n : ns) {
            if (n.getCurrentValue() > 0) {
                if (MyDirectGraphVars.directGraph.getPredecessorCount(n) > 0 && MyDirectGraphVars.directGraph.getSuccessorCount(n) > 0) {
                    cnt++;
                }
            }
        }
        return cnt;
    }

    public double getBlueNodePercent() {
        Collection<MyDirectNode> ns = MyDirectGraphVars.directGraph.getVertices();
        int cnt = 0;
        for (MyDirectNode n : ns) {
            if (n.getCurrentValue() > 0) {
                if (MyDirectGraphVars.directGraph.getPredecessorCount(n) > 0 && MyDirectGraphVars.directGraph.getSuccessorCount(n) > 0) {
                    cnt++;
                }
            }
        }
        return ((double)cnt/ns.size());
    }

    public double getGreenNodePercent() {
        Collection<MyDirectNode> ns = MyDirectGraphVars.directGraph.getVertices();
        int cnt = 0;
        for (MyDirectNode n : ns) {
            if (n.getCurrentValue() > 0) {
                if (MyDirectGraphVars.directGraph.getPredecessorCount(n) > 0 && MyDirectGraphVars.directGraph.getSuccessorCount(n) == 0) {
                    cnt++;
                }
            }
        }
        return ((double)cnt/ns.size());
    }

    public int getGreenNodeCount() {
        Collection<MyDirectNode> ns = MyDirectGraphVars.directGraph.getVertices();
        int cnt = 0;
        for (MyDirectNode n : ns) {
            if (n.getCurrentValue() > 0) {
                if (MyDirectGraphVars.directGraph.getPredecessorCount(n) > 0 && MyDirectGraphVars.directGraph.getSuccessorCount(n) == 0) {
                    cnt++;
                }
            }
        }
        return cnt;
    }

    public double getMaximumNodeValue() {
        double maxValue = 0D;
        Collection<MyDirectNode> ns = MyDirectGraphVars.directGraph.getVertices();
        for (MyDirectNode n : ns) {
            if (n.getCurrentValue() == 0) continue;
            if (maxValue < n.getCurrentValue()) {
                maxValue = n.getCurrentValue();
            }
        }
        return maxValue;
    }
    public double getMinimumNodeValue() {
        double minValue = 1000000000000D;
        Collection<MyDirectNode> ns = MyDirectGraphVars.directGraph.getVertices();
        for (MyDirectNode n : ns) {
            if (n.getCurrentValue() > 0 && minValue > n.getCurrentValue()) {
                minValue = n.getCurrentValue();
            }
        }
        return minValue;
    }

    public String getAverageNodeValue() {
        Collection<MyDirectNode> ns = MyDirectGraphVars.directGraph.getVertices();
        float totalVal = 0f;
        for (MyDirectNode n : ns) {
            if (n.getCurrentValue() > 0) {
                totalVal += n.getCurrentValue();
            }
        }
        return MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(totalVal/ns.size()));
    }

    public double getNodeValueStandardDeviation() {
        Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
        double sum = 0.00d;
        int numNodes = 0;
        for (MyDirectNode n : nodes) {
            if (n.getCurrentValue() == 0) {continue;}
            numNodes++;
            sum += n.getCurrentValue();
        }
        double mean = sum/numNodes;
        double std = 0.00D;
        for(MyDirectNode n : nodes) {std += Math.pow(n.getCurrentValue()-mean, 2);}
        return Math.sqrt(std/numNodes);
    }

    public double getAveragePredecessorCount() {
        long total = 0L;
        int ncnt = 0;
        Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
        for (MyDirectNode n : nodes) {
            if (n.getCurrentValue() == 0) continue;
            int val = MyDirectGraphVars.directGraph.getPredecessorCount(n);
            if (val > 0) {
                total += val;
                ncnt++;
            }
        }
        return (double)total/ncnt;
    }

    public double getAverageSuccessorCount() {
        long total = 0L;
        int ncnt = 0;
        Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
        for (MyDirectNode n : nodes) {
            if (n.getCurrentValue() == 0) continue;
            int val = MyDirectGraphVars.directGraph.getSuccessorCount(n);
            if (val > 0) {
                total += val;
                ncnt++;
            }
        }
        return (double)total/ncnt;
    }

    public synchronized void updateEdgeTable() {
        while (edgeTable.getModel().getRowCount() > 0) {
            int row = edgeTable.getModel().getRowCount() - 1;
            ((DefaultTableModel) edgeTable.getModel()).removeRow(row);
        }

        if (MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode != null) {
            LinkedHashMap<String, Long> edgeMap = new LinkedHashMap<>();
            ArrayList<MyDirectEdge> edges = new ArrayList<>(MyDirectGraphVars.directGraph.getInEdges(MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode));
            edges.addAll(MyDirectGraphVars.directGraph.getOutEdges(MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode));
            for (MyDirectEdge e : edges) {
                if (e.getSource().getCurrentValue() > 0 && e.getDest().getCurrentValue() > 0) {
                    String edgeName = e.getSource().getName() + "-" + e.getDest().getName();
                    edgeMap.put(edgeName, (long) e.getCurrentValue());
                }
            }

            edgeMap = MyDirectGraphSysUtil.sortMapByLongValue(edgeMap);
            int i = 0;
            for (String edge : edgeMap.keySet()) {
                String [] edgePair = edge.split("-");
                ((DefaultTableModel) edgeTable.getModel()).addRow(
                        new String[]{
                                "" + (++i),
                                edgePair[0],
                                edgePair[1],
                                (MyDirectGraphVars.getDirectGraphViewer().vc.edgeValueComboBoxMenu.getSelectedIndex() == 0 || edgeMap.get(edge) == -1 ?
                                        "0" : MyDirectGraphMathUtil.getCommaSeperatedNumber(edgeMap.get(edge)))});
            }
        } else if (MyDirectGraphVars.getDirectGraphViewer().multiNodes != null) {
            LinkedHashMap<String, Float> edgeMap = new LinkedHashMap<>();
            for (MyDirectNode selectedNode : MyDirectGraphVars.getDirectGraphViewer().multiNodes) {
                Collection<MyDirectEdge> edges = MyDirectGraphVars.directGraph.getInEdges(selectedNode);
                if (edges != null) {
                    for (MyDirectEdge e : edges) {
                        if (e.getSource().getCurrentValue() > 0 && e.getDest().getCurrentValue() > 0) {
                            String edgeName = e.getSource().getName() + "-" + e.getDest().getName();
                            edgeMap.put(edgeName, e.getCurrentValue());
                        }
                    }
                }
                edges = MyDirectGraphVars.directGraph.getOutEdges(selectedNode);

                if (edges != null) {
                    for (MyDirectEdge e : edges) {
                        String edgeName = e.getSource().getName() + "-" + e.getDest().getName();
                        edgeMap.put(edgeName, e.getCurrentValue());
                    }
                }
            }

            edgeMap = MyDirectGraphSysUtil.sortMapByFloatValue(edgeMap);
            int i = 0;
            for (String edgeName : edgeMap.keySet()) {
                String [] edgePair = edgeName.split("-");
                float edgeValue = edgeMap.get(edgeName);
                ((DefaultTableModel) edgeTable.getModel()).addRow(
                        new String[]{
                                "" + (++i),
                                edgePair[0],
                                edgePair[1],
                                (MyDirectGraphVars.getDirectGraphViewer().vc.edgeValueComboBoxMenu.getSelectedIndex() == 0 || edgeMap.get(edgeName) == -1 ?
                                        "0" : MyDirectGraphMathUtil.getCommaSeperatedNumber((long)edgeValue))});
            }
        } else {
            LinkedHashMap<String, Float> edgeMap = new LinkedHashMap<>();
            Collection<MyDirectEdge> edges = MyDirectGraphVars.directGraph.getEdges();
            for (MyDirectEdge e : edges) {
                if (e.getSource().getCurrentValue() > 0 && e.getDest().getCurrentValue() > 0) {
                    String edgeName = e.getSource().getName() + "-" + e.getDest().getName();
                    edgeMap.put(edgeName, e.getCurrentValue());
                }

            }
            edgeMap = MyDirectGraphSysUtil.sortMapByFloatValue(edgeMap);

            int i = 0;
            for (String edgeName : edgeMap.keySet()) {
                String [] edgePair = edgeName.split("-");
                float edgeValue = edgeMap.get(edgeName);
                ((DefaultTableModel) edgeTable.getModel()).addRow(
                        new String[]{
                                "" + (++i),
                                edgePair[0],
                                edgePair[1],
                                (MyDirectGraphVars.getDirectGraphViewer().vc.edgeValueComboBoxMenu.getSelectedIndex() == 0 ?
                                        "0" : MyDirectGraphMathUtil.getCommaSeperatedNumber((long)edgeValue))});
            }
        }
        edgeTable.revalidate();
        edgeTable.repaint();
    }

    public synchronized void updateRemainingNodeTable() {
        while (currentNodeListTable.getModel().getRowCount() > 0) {
            int row = currentNodeListTable.getRowCount()-1;
            ((DefaultTableModel) currentNodeListTable.getModel()).removeRow(row);
        }

        if (MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode != null) {
            LinkedHashMap<String, Long> nodeMap = new LinkedHashMap<>();
            ArrayList<MyDirectNode> nodes = new ArrayList<>(MyDirectGraphVars.getDirectGraphViewer().selectedSingleNodePredecessorSet);
            nodes.addAll(MyDirectGraphVars.getDirectGraphViewer().selectedSingleNodeSuccessorSet);
            for (MyDirectNode n : nodes) {
                if (n.getCurrentValue() == 0) continue;
                nodeMap.put(n.getName(), (long) n.getCurrentValue());
            }
            nodeMap = MyDirectGraphSysUtil.sortMapByLongValue(nodeMap);

            int i = 0;
            for (String n : nodeMap.keySet()) {
                MyDirectNode nn = (MyDirectNode) MyDirectGraphVars.directGraph.vRefs.get(n);
                if (nn.getCurrentValue() == 0) continue;
                ((DefaultTableModel) currentNodeListTable.getModel()).addRow(
                        new String[]{
                                "" + (++i),
                                n,
                                MyDirectGraphMathUtil.getCommaSeperatedNumber((long) nn.getCurrentValue())});
            }
        } else if (MyDirectGraphVars.getDirectGraphViewer().multiNodes != null) {
            LinkedHashMap<String, Long> nodeMap = new LinkedHashMap<>();
            ArrayList<MyDirectNode> nodes = new ArrayList<>(MyDirectGraphVars.getDirectGraphViewer().multiNodePredecessorSet);
            nodes.addAll(MyDirectGraphVars.getDirectGraphViewer().multiNodeSuccessorSet);
            for (MyDirectNode n : nodes) {
                if (n.getCurrentValue() == 0) continue;
                nodeMap.put(n.getName(), (long) n.getCurrentValue());
            }

            nodeMap = MyDirectGraphSysUtil.sortMapByLongValue(nodeMap);
            int i = 0;
            for (String n : nodeMap.keySet()) {
                MyDirectNode nn = (MyDirectNode) MyDirectGraphVars.directGraph.vRefs.get(n);
                if (nn.getCurrentValue() == 0) continue;

                ((DefaultTableModel) currentNodeListTable.getModel()).addRow(
                        new String[]{
                                "" + (++i),
                                n,
                                MyDirectGraphMathUtil.getCommaSeperatedNumber((long) nn.getCurrentValue())});
            }
        } else {
            LinkedHashMap<String, Long> nodeMap = new LinkedHashMap<>();
            Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
            for (MyDirectNode n : nodes) {
                nodeMap.put(n.getName(), (long) n.getCurrentValue());
            }
            nodeMap = MyDirectGraphSysUtil.sortMapByLongValue(nodeMap);

            int i = 0;
            for (String n : nodeMap.keySet()) {
                MyDirectNode nn = (MyDirectNode) MyDirectGraphVars.directGraph.vRefs.get(n);
                if (nn.getCurrentValue() <= 0) continue;;

                ((DefaultTableModel) currentNodeListTable.getModel()).addRow(
                        new String[]{
                                "" + (++i),
                                n,
                                MyDirectGraphMathUtil.getCommaSeperatedNumber((long) nn.getCurrentValue())});
            }
        }
    }

    public synchronized void updateStatTable() {
        if (MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode != null) {

        }
    }

    public synchronized void updateEdgeValueRelatedComponents() {
        if (MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode != null) {
            MyDirectGraphVars.app.getDirectGraphDashBoard().nodeLevelSuccessorEdgeValueDistribution.decorate();
            MyDirectGraphVars.app.getDirectGraphDashBoard().nodeLevelPredecessorEdgeValueDistribution.decorate();
            MyDirectGraphVars.app.getDirectGraphDashBoard().nodeLevelEdgeValueDistribution.decorate();
            if (MyDirectGraphVars.getDirectGraphViewer().vc.edgeValueComboBoxMenu.getSelectedIndex() == 0) {
                MyDirectGraphVars.getDirectGraphViewer().setUnWeightedEdgeColor();
                MyDirectGraphVars.getDirectGraphViewer().setUnWeigtedEdgeStroker();
                if (MyDirectGraphVars.getDirectGraphViewer().edgeValueBarChart != null) {
                    MyDirectGraphVars.getDirectGraphViewer().remove(MyDirectGraphVars.getDirectGraphViewer().edgeValueBarChart);
                }
                 (statTable.getModel()).setValueAt( "0.00", 5, 1);
                 (statTable.getModel()).setValueAt("0.00", 6, 1);
                 (statTable.getModel()).setValueAt("0.00", 7, 1);
                 (statTable.getModel()).setValueAt("0.00", 8, 1);
                 (statTable.getModel()).setValueAt("0.00", 9, 1);
                 (statTable.getModel()).setValueAt("0.00", 10, 1);
                 (statTable.getModel()).setValueAt("0.00", 11, 1);
                 (statTable.getModel()).setValueAt("0.00", 12, 1);
            } else {
                MyDirectGraphVars.getDirectGraphViewer().setWeightedEdgeColor();
                MyDirectGraphVars.getDirectGraphViewer().setWeightedEdgeStroker();
                if (MyDirectGraphVars.getDirectGraphViewer().edgeValueBarChart != null) {
                    MyDirectGraphVars.getDirectGraphViewer().remove(MyDirectGraphVars.getDirectGraphViewer().edgeValueBarChart);
                }

                if (MyDirectGraphVars.getDirectGraphViewer().isPredecessorOnly) {
                    MyDirectGraphVars.getDirectGraphViewer().edgeValueBarChart = new MyDirectGraphEdgeValueBarChart();
                    MyDirectGraphVars.getDirectGraphViewer().edgeValueBarChart.setPredecessorEdgeValueBarChart();
                    MyDirectGraphVars.getDirectGraphViewer().add(MyDirectGraphVars.getDirectGraphViewer().edgeValueBarChart);
                } else if (MyDirectGraphVars.getDirectGraphViewer().isSuccessorOnly) {
                    MyDirectGraphVars.getDirectGraphViewer().edgeValueBarChart = new MyDirectGraphEdgeValueBarChart();
                    MyDirectGraphVars.getDirectGraphViewer().edgeValueBarChart.setSuccessorEdgeValueBarChart();
                    MyDirectGraphVars.getDirectGraphViewer().add(MyDirectGraphVars.getDirectGraphViewer().edgeValueBarChart);
                } else {
                    MyDirectGraphVars.getDirectGraphViewer().edgeValueBarChart = new MyDirectGraphEdgeValueBarChart();
                    MyDirectGraphVars.getDirectGraphViewer().edgeValueBarChart.showPredecesorAndSuccessorEdgeBarChart();
                    MyDirectGraphVars.getDirectGraphViewer().add(MyDirectGraphVars.getDirectGraphViewer().edgeValueBarChart);
                }
                 (statTable.getModel()).setValueAt( MyDirectGraphMathUtil.getCommaSeperatedNumber((long) MyDirectGraphVars.directGraph.getMaxInEdgeValueForSelectedNode()), 5, 1);
                 (statTable.getModel()).setValueAt(MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(MyDirectGraphVars.directGraph.getAverageInEdgeValueForSelectedNode())), 6, 1);
                 (statTable.getModel()).setValueAt(MyDirectGraphMathUtil.getCommaSeperatedNumber((long) MyDirectGraphVars.directGraph.getMinInEdgeValueForSelectedNode()), 7, 1);
                 (statTable.getModel()).setValueAt(MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(MyDirectGraphVars.directGraph.getInEdgValueStandardDeviationForSelectedNode())), 8, 1);
                 (statTable.getModel()).setValueAt(MyDirectGraphMathUtil.getCommaSeperatedNumber((long) MyDirectGraphVars.directGraph.getMaxOutEdgeValueForSelectedNode()), 9, 1);
                 (statTable.getModel()).setValueAt(MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(MyDirectGraphVars.directGraph.getAverageOutEdgeValueForSelectedNode())), 10, 1);
                 (statTable.getModel()).setValueAt(MyDirectGraphMathUtil.getCommaSeperatedNumber((long) MyDirectGraphVars.directGraph.getMinOutEdgeValueForSelectedNode()), 11, 1);
                 (statTable.getModel()).setValueAt(MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(MyDirectGraphVars.directGraph.getOutEdgValueStandardDeviationForSelectedNode())), 12, 1);
             }
        } else if (MyDirectGraphVars.getDirectGraphViewer().multiNodes != null) {
            MyDirectGraphVars.app.getDirectGraphDashBoard().multiNodeLevelSuccessorEdgeValueDistributionLineChart.decorate();
            MyDirectGraphVars.app.getDirectGraphDashBoard().multiNodeLevelPredecessorEdgeValueDistributionLineChart.decorate();
            MyDirectGraphVars.app.getDirectGraphDashBoard().multiNodeLevelSharedSuccessorEdgeValueDistributionLineChart.decorate();
            MyDirectGraphVars.app.getDirectGraphDashBoard().multiNodeLevelSharedPredecessorsEdgeValueDistributionChart.decorate();
        } else {
            if (MyDirectGraphVars.getDirectGraphViewer().vc.edgeValueComboBoxMenu.getSelectedIndex() > 0) {
                MyDirectGraphVars.getDirectGraphViewer().setWeightedEdgeStroker();
                MyDirectGraphVars.getDirectGraphViewer().setWeightedEdgeColor();
                MyDirectGraphVars.app.getDirectGraphDashBoard().topLevelEdgeValueDistribution.decorate();
                if (MyDirectGraphVars.getDirectGraphViewer().edgeValueBarChart != null) {
                    MyDirectGraphVars.getDirectGraphViewer().remove(MyDirectGraphVars.getDirectGraphViewer().edgeValueBarChart);
                }
                MyDirectGraphVars.getDirectGraphViewer().edgeValueBarChart = new MyDirectGraphEdgeValueBarChart();
                MyDirectGraphVars.getDirectGraphViewer().edgeValueBarChart.setEdgeValueBarChart();
                MyDirectGraphVars.getDirectGraphViewer().add(MyDirectGraphVars.getDirectGraphViewer().edgeValueBarChart);
                (statTable.getModel()).setValueAt(MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(MyDirectGraphVars.directGraph.getAverageEdgeValue())), statTable.getRowCount()-4,1 );
                (statTable.getModel()).setValueAt(MyDirectGraphMathUtil.getCommaSeperatedNumber((long) MyDirectGraphVars.directGraph.getMaxEdgeValue()), statTable.getRowCount()-3, 1);
                (statTable.getModel()).setValueAt(MyDirectGraphMathUtil.getCommaSeperatedNumber((long) MyDirectGraphVars.directGraph.getMinEdgeValue()), statTable.getRowCount()-2, 1);
                (statTable.getModel()).setValueAt(MyDirectGraphMathUtil.getCommaSeperatedNumber((long) MyDirectGraphVars.directGraph.getEdgValueStandardDeviation()), statTable.getRowCount()-1, 1);
            } else {
                MyDirectGraphVars.app.getDirectGraphDashBoard().topLevelEdgeValueDistribution.decorate();
                if (MyDirectGraphVars.getDirectGraphViewer().vc.edgeValueComboBoxMenu.getSelectedIndex() == 0) {
                    MyDirectGraphVars.getDirectGraphViewer().setUnWeightedEdgeColor();
                    MyDirectGraphVars.getDirectGraphViewer().setUnWeigtedEdgeStroker();
                    topLevelStatTablePropertyValuePairs = updateTopLevelGraphStatToolTips();
                    (statTable.getModel()).setValueAt("0.00", statTable.getRowCount()-4,1 );
                    (statTable.getModel()).setValueAt("0", statTable.getRowCount()-3, 1);
                    (statTable.getModel()).setValueAt("0", statTable.getRowCount()-2, 1);
                    (statTable.getModel()).setValueAt("0.00", statTable.getRowCount()-1, 1);
                    statTable.revalidate();
                    statTable.repaint();
                    if (MyDirectGraphVars.getDirectGraphViewer().edgeValueBarChart != null) {
                        MyDirectGraphVars.getDirectGraphViewer().remove(MyDirectGraphVars.getDirectGraphViewer().edgeValueBarChart);
                    }
                } else {
                    (statTable.getModel()).setValueAt(MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(MyDirectGraphVars.directGraph.getAverageEdgeValue())), statTable.getRowCount()-4,1 );
                    (statTable.getModel()).setValueAt(MyDirectGraphMathUtil.getCommaSeperatedNumber((long) MyDirectGraphVars.directGraph.getMaxEdgeValue()), statTable.getRowCount()-3, 1);
                    (statTable.getModel()).setValueAt(MyDirectGraphMathUtil.getCommaSeperatedNumber((long) MyDirectGraphVars.directGraph.getMinEdgeValue()), statTable.getRowCount()-2, 1);
                    (statTable.getModel()).setValueAt(MyDirectGraphMathUtil.getCommaSeperatedNumber((long) MyDirectGraphVars.directGraph.getEdgValueStandardDeviation()), statTable.getRowCount()-1, 1);
                    MyDirectGraphVars.getDirectGraphViewer().setWeightedEdgeColor();
                    MyDirectGraphVars.getDirectGraphViewer().setWeightedEdgeStroker();
                    topLevelStatTablePropertyValuePairs = updateTopLevelGraphStatToolTips();
                    statTable.revalidate();
                    statTable.repaint();
                }
            }
        }
    }


}
