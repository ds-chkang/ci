package datamining.graph;

import datamining.main.MyProgressBar;
import datamining.graph.barcharts.MyMultiNodeLevelNeighborNodeValueBarChart;
import datamining.graph.barcharts.MyNodeValueBarChart;
import datamining.graph.menu.MyDirectGraphSelectedNodeMenu;
import datamining.graph.menu.MyDirectGraphViewerMenu;
import datamining.utils.system.MyVars;
import edu.uci.ics.jung.visualization.picking.PickedState;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class MyDirectGraphViewerMouseListener
implements MouseListener, MouseMotionListener {

    private PickedState pickedState;

    public MyDirectGraphViewerMouseListener(MyDirectMarkovChainViewer v) {
        this.pickedState = v.getPickedVertexState();
    }

    @Override public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e) && MyVars.getDirectGraphViewer().getPickedVertexState().getPicked().size() == 0) {
            if (MyVars.getDirectGraphViewer().selectedSingleNode != null || MyVars.getDirectGraphViewer().multiNodes != null ||
                    MyVars.getDirectGraphViewer().directGraphViewerControlPanel.edgeValueComboBoxMenu.getSelectedIndex() > 0 ||
                    MyVars.getDirectGraphViewer().directGraphViewerControlPanel.nodeValueComboBoxMenu.getSelectedIndex() > 0 ||
                    MyVars.getDirectGraphViewer().isExcludeBtnOn ||
                    MyVars.getDirectGraphViewer().directGraphViewerControlPanel.weightedNodeColorCheckBox.isSelected() ||
                    MyVars.getDirectGraphViewer().directGraphViewerControlPanel.nodeValueCheckBox.isSelected() ||
                    MyVars.getDirectGraphViewer().directGraphViewerControlPanel.edgeValueCheckBox.isSelected() ||
                    MyVars.getDirectGraphViewer().directGraphViewerControlPanel.removeEdgeCheckBox.isSelected() ||
                    MyVars.getDirectGraphViewer().directGraphViewerControlPanel.nodeLabelCheckBox.isSelected() ||
                    MyVars.getDirectGraphViewer().directGraphViewerControlPanel.edgeLabelCheckBox.isSelected()) {
                if (MyVars.currentThread == null) {
                    new Thread(new Runnable() {
                        @Override public void run() {
                            setDefaultView();
                        }
                    }).start();
                }

            }
        } else if (SwingUtilities.isRightMouseButton(e) && MyVars.getDirectGraphViewer().getPickedVertexState().getPicked().size() == 0) {
            new Thread(new Runnable() {
                @Override public void run() {
                    if (MyVars.getDirectGraphViewer().selectedSingleNode != null) {
                        MyDirectGraphSelectedNodeMenu nodeMenu = new MyDirectGraphSelectedNodeMenu();
                        nodeMenu.show(MyVars.getDirectGraphViewer(), e.getX(), e.getY());
                    } else if (MyVars.getDirectGraphViewer().multiNodes != null) {

                    } else {
                        MyDirectGraphViewerMenu directGraphViewerMenu = new MyDirectGraphViewerMenu();
                        directGraphViewerMenu.show(MyVars.getDirectGraphViewer(), e.getX(), e.getY());
                    }
                }
            }).start();
        }
        MyVars.getDirectGraphViewer().revalidate();
        MyVars.getDirectGraphViewer().repaint();
    }

    public void setDefaultView() {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            Collection<MyDirectNode> nodes = MyVars.directMarkovChain.getVertices();
            for (MyDirectNode n : nodes) {
                n.setCurrentValue(n.getContribution());
            }
            pb.updateValue(20, 100);

            Collection<MyDirectEdge> edges = MyVars.directMarkovChain.getEdges();
            for (MyDirectEdge e : edges) {
                e.setCurrentValue(-1);
            }
            pb.updateValue(40, 100);

            MyVars.getDirectGraphViewer().isExcludeBtnOn = false;
            MyVars.getDirectGraphViewer().selectedSingleNode = null;
            MyVars.getDirectGraphViewer().multiNodes = null;
            MyVars.getDirectGraphViewer().selectedSingleNodePredecessorSet = null;
            MyVars.getDirectGraphViewer().selectedSingleNodeSuccessorSet = null;
            MyVars.getDirectGraphViewer().multiNodeSuccessorSet = null;
            MyVars.getDirectGraphViewer().multiNodePredecessorSet = null;
            MyVars.getDirectGraphViewer().multiNodeSharedSuccessorSet = null;
            MyVars.getDirectGraphViewer().multiNodeSharedPredecessorSet = null;
            MyVars.getDirectGraphViewer().isSharedPredecessorOnly = false;
            MyVars.getDirectGraphViewer().isSharedSuccessorOnly = false;
            MyVars.getDirectGraphViewer().isPredecessorOnly = false;
            MyVars.getDirectGraphViewer().isSuccessorOnly = false;
            MyVars.BAR_CHART_RECORD_LIMIT = 35;

            MyVars.getDirectGraphViewer().directGraphViewerControlPanel.nodeValueComboBoxMenu.removeActionListener(MyVars.getDirectGraphViewer().directGraphViewerControlPanel);
            MyVars.getDirectGraphViewer().directGraphViewerControlPanel.nodeLabelComboBoxMenu.removeActionListener(MyVars.getDirectGraphViewer().directGraphViewerControlPanel);
            MyVars.getDirectGraphViewer().directGraphViewerControlPanel.edgeValueComboBoxMenu.removeActionListener(MyVars.getDirectGraphViewer().directGraphViewerControlPanel);
            MyVars.getDirectGraphViewer().directGraphViewerControlPanel.edgeLabelComboBoxMenu.removeActionListener(MyVars.getDirectGraphViewer().directGraphViewerControlPanel);
            MyVars.getDirectGraphViewer().directGraphViewerControlPanel.excludeBtn.removeActionListener(MyVars.getDirectGraphViewer().directGraphViewerControlPanel);
            MyVars.getDirectGraphViewer().directGraphViewerControlPanel.removeEdgeCheckBox.removeActionListener(MyVars.getDirectGraphViewer().directGraphViewerControlPanel);
            MyVars.getDirectGraphViewer().directGraphViewerControlPanel.weightedNodeColorCheckBox.removeActionListener(MyVars.getDirectGraphViewer().directGraphViewerControlPanel);
            MyVars.getDirectGraphViewer().directGraphViewerControlPanel.nodeValueCheckBox.removeActionListener(MyVars.getDirectGraphViewer().directGraphViewerControlPanel);
            MyVars.getDirectGraphViewer().directGraphViewerControlPanel.edgeValueCheckBox.removeActionListener(MyVars.getDirectGraphViewer().directGraphViewerControlPanel);
            MyVars.getDirectGraphViewer().directGraphViewerControlPanel.edgeLabelCheckBox.removeActionListener(MyVars.getDirectGraphViewer().directGraphViewerControlPanel);
            MyVars.getDirectGraphViewer().directGraphViewerControlPanel.nodeLabelCheckBox.removeActionListener(MyVars.getDirectGraphViewer().directGraphViewerControlPanel);

            MyVars.getDirectGraphViewer().directGraphViewerControlPanel.removeEdgeCheckBox.setSelected(false);
            MyVars.getDirectGraphViewer().directGraphViewerControlPanel.weightedNodeColorCheckBox.setSelected(false);
            MyVars.getDirectGraphViewer().directGraphViewerControlPanel.nodeValueCheckBox.setSelected(false);
            MyVars.getDirectGraphViewer().directGraphViewerControlPanel.edgeValueCheckBox.setSelected(false);
            MyVars.getDirectGraphViewer().directGraphViewerControlPanel.edgeLabelCheckBox.setSelected(false);
            MyVars.getDirectGraphViewer().directGraphViewerControlPanel.nodeLabelCheckBox.setSelected(false);
            pb.updateValue(50, 100);

            MyVars.getDirectGraphViewer().directGraphViewerControlPanel.nodeValueComboBoxMenu.setSelectedIndex(0);
            MyVars.getDirectGraphViewer().directGraphViewerControlPanel.edgeValueComboBoxMenu.setSelectedIndex(0);
            MyVars.getDirectGraphViewer().directGraphViewerControlPanel.edgeLabelComboBoxMenu.setSelectedIndex(0);
            MyVars.getDirectGraphViewer().directGraphViewerControlPanel.nodeLabelComboBoxMenu.setSelectedIndex(0);
            MyVars.getDirectGraphViewer().directGraphViewerControlPanel.nodeValueExcludeSymbolComboBoxMenu.setSelectedIndex(0);
//            MyVars.getDirectGraphViewer().directGraphViewerControlPanel.nodeValueExcludeComboBoxMenu.setSelectedIndex(0);
            MyVars.getDirectGraphViewer().directGraphViewerControlPanel.nodeValueExcludeTxt.setText("");

            MyVars.main.getDirectMarkovChainDashBoard().nodeListTable.clearSelection();
            MyVars.main.getDirectMarkovChainDashBoard().currentNodeListTable.clearSelection();
            MyVars.main.getDirectMarkovChainDashBoard().edgeTable.clearSelection();
            MyVars.main.getDirectMarkovChainDashBoard().statTable.clearSelection();

            MyVars.getDirectGraphViewer().directGraphViewerControlPanel.excludeBtn.addActionListener(MyVars.getDirectGraphViewer().directGraphViewerControlPanel);
            MyVars.getDirectGraphViewer().directGraphViewerControlPanel.removeEdgeCheckBox.addActionListener(MyVars.getDirectGraphViewer().directGraphViewerControlPanel);
            MyVars.getDirectGraphViewer().directGraphViewerControlPanel.weightedNodeColorCheckBox.addActionListener(MyVars.getDirectGraphViewer().directGraphViewerControlPanel);
            MyVars.getDirectGraphViewer().directGraphViewerControlPanel.nodeValueComboBoxMenu.addActionListener(MyVars.getDirectGraphViewer().directGraphViewerControlPanel);
            MyVars.getDirectGraphViewer().directGraphViewerControlPanel.nodeLabelComboBoxMenu.addActionListener(MyVars.getDirectGraphViewer().directGraphViewerControlPanel);
            MyVars.getDirectGraphViewer().directGraphViewerControlPanel.edgeValueComboBoxMenu.addActionListener(MyVars.getDirectGraphViewer().directGraphViewerControlPanel);
            MyVars.getDirectGraphViewer().directGraphViewerControlPanel.edgeLabelComboBoxMenu.addActionListener(MyVars.getDirectGraphViewer().directGraphViewerControlPanel);
            MyVars.getDirectGraphViewer().directGraphViewerControlPanel.edgeValueCheckBox.addActionListener(MyVars.getDirectGraphViewer().directGraphViewerControlPanel);
            MyVars.getDirectGraphViewer().directGraphViewerControlPanel.edgeLabelCheckBox.addActionListener(MyVars.getDirectGraphViewer().directGraphViewerControlPanel);
            MyVars.getDirectGraphViewer().directGraphViewerControlPanel.nodeValueCheckBox.addActionListener(MyVars.getDirectGraphViewer().directGraphViewerControlPanel);
            MyVars.getDirectGraphViewer().directGraphViewerControlPanel.nodeLabelCheckBox.addActionListener(MyVars.getDirectGraphViewer().directGraphViewerControlPanel);
            pb.updateValue(60, 100);

            MyVars.main.getDirectMarkovChainDashBoard().setTopLevelDashboard(MyVars.getDirectGraphViewer());
            pb.updateValue(80, 100);

            MyVars.getDirectGraphViewer().directGraphViewerControlPanel.removeBarCharts();
            MyVars.getDirectGraphViewer().nodeValueRankBarChart = new MyNodeValueBarChart();
            MyVars.getDirectGraphViewer().add(MyVars.getDirectGraphViewer().nodeValueRankBarChart);
            pb.updateValue(90, 100);

            MyVars.main.getDirectMarkovChainDashBoard().directGraphTextStatisticsPanel.setTextStatistics();
            pb.updateValue(100, 100);
            pb.dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
            pb.updateValue(100, 100);
            pb.dispose();
        }
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {
        new Thread(new Runnable() {
            @Override public void run() {
                if (SwingUtilities.isLeftMouseButton(e) && pickedState != null && pickedState.getPicked().size() > 1) {
                    MyProgressBar pb = new MyProgressBar(false);
                    try {
                        Iterator itr = pickedState.getPicked().iterator();
                        MyDirectNode pickedNode = (MyDirectNode) itr.next();
                        pb.updateValue(10, 100);
                        MyVars.getDirectGraphViewer().multiNodes = new HashSet<>();
                        MyVars.getDirectGraphViewer().multiNodes.add(pickedNode);
                        pb.updateValue(20, 100);
                        MyVars.getDirectGraphViewer().multiNodeSuccessorSet = new HashSet<>(MyVars.directMarkovChain.getSuccessors(pickedNode));
                        MyVars.getDirectGraphViewer().multiNodePredecessorSet = new HashSet<>(MyVars.directMarkovChain.getPredecessors(pickedNode));
                        MyVars.getDirectGraphViewer().multiNodeSharedPredecessorSet = new HashSet<>(MyVars.directMarkovChain.getPredecessors(pickedNode));
                        MyVars.getDirectGraphViewer().multiNodeSharedSuccessorSet = new HashSet<>(MyVars.directMarkovChain.getSuccessors(pickedNode));
                        pb.updateValue(50, 100);
                        while (itr.hasNext()) {
                            pickedNode = (MyDirectNode) itr.next();
                            MyVars.getDirectGraphViewer().multiNodes.add(pickedNode);
                            MyVars.getDirectGraphViewer().multiNodePredecessorSet.addAll(MyVars.directMarkovChain.getPredecessors(pickedNode));
                            MyVars.getDirectGraphViewer().multiNodeSuccessorSet.addAll(MyVars.directMarkovChain.getSuccessors(pickedNode));
                            MyVars.getDirectGraphViewer().multiNodeSharedSuccessorSet.retainAll(MyVars.directMarkovChain.getSuccessors(pickedNode));
                            MyVars.getDirectGraphViewer().multiNodeSharedPredecessorSet.retainAll(MyVars.directMarkovChain.getPredecessors(pickedNode));
                        }

                        if (MyVars.getDirectGraphViewer().directGraphViewerControlPanel.edgeValueComboBoxMenu.getSelectedIndex() == 0) {

                        } else {
                            for (MyDirectNode n : MyVars.getDirectGraphViewer().multiNodes) {
                                n.setCurrentValue(-1f);
                            }
                        }

                        pb.updateValue(80, 100);
                        MyVars.getDirectGraphViewer().selectedSingleNode = null;
                        MyVars.main.getDirectMarkovChainDashBoard().directGraphTextStatisticsPanel.setTextStatistics();
                        MyVars.main.getDirectMarkovChainDashBoard().setMultiNodeLevelDashBoard(MyVars.getDirectGraphViewer());
                        MyVars.getDirectGraphViewer().getRenderContext().setEdgeDrawPaintTransformer(MyVars.getDirectGraphViewer().unWeightedEdgeColor);
                        MyVars.getDirectGraphViewer().multiNodeLevelNeighborNodeValueBarChart = new MyMultiNodeLevelNeighborNodeValueBarChart();
                        MyVars.getDirectGraphViewer().add(MyVars.getDirectGraphViewer().multiNodeLevelNeighborNodeValueBarChart);
                        pb.updateValue(100, 100);
                        pb.dispose();
                        MyVars.getDirectGraphViewer().revalidate();
                        MyVars.getDirectGraphViewer().repaint();
                    } catch (Exception ex) {
                        pb.updateValue(100, 100);
                        pb.dispose();
                        MyVars.getDirectGraphViewer().revalidate();
                        MyVars.getDirectGraphViewer().repaint();
                    }
                }
            }
        }).start();
    }
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mouseDragged(MouseEvent e) {}
    @Override public void mouseMoved(MouseEvent e) {}
}
