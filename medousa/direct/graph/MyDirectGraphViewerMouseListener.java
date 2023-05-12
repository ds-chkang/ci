package medousa.direct.graph;

import medousa.direct.graph.menu.MyDirectGraphSelectedNodeMenu;
import medousa.direct.graph.menu.MyDirectGraphViewerMenu;
import medousa.MyProgressBar;
import medousa.direct.graph.barcharts.MyDirectGraphMultiNodeLevelNeighborNodeValueBarChart;
import medousa.direct.graph.barcharts.MyDirectGraphNodeValueBarChart;
import medousa.direct.utils.MyDirectGraphVars;
import edu.uci.ics.jung.visualization.picking.PickedState;
import org.apache.commons.collections15.Transformer;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class MyDirectGraphViewerMouseListener
implements MouseListener, MouseMotionListener, Serializable {

    private PickedState pickedState;

    public MyDirectGraphViewerMouseListener(MyDirectGraphViewer v) {
        this.pickedState = v.getPickedVertexState();
    }

    @Override public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e) && pickedState.getPicked().size() == 0) {
            if (MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode != null ||
                MyDirectGraphVars.getDirectGraphViewer().multiNodes != null ||
                MyDirectGraphVars.getDirectGraphViewer().vc.edgeValueComboBoxMenu.getSelectedIndex() > 0 ||
                MyDirectGraphVars.getDirectGraphViewer().vc.nodeValueComboBoxMenu.getSelectedIndex() > 0 ||
                MyDirectGraphVars.getDirectGraphViewer().isExcludeBtnOn ||
                MyDirectGraphVars.getDirectGraphViewer().vc.weightedNodeColorCheckBox.isSelected() ||
                MyDirectGraphVars.getDirectGraphViewer().vc.nodeValueCheckBox.isSelected() ||
                MyDirectGraphVars.getDirectGraphViewer().vc.edgeValueCheckBox.isSelected() ||
                MyDirectGraphVars.getDirectGraphViewer().vc.removeEdgeCheckBox.isSelected() ||
                MyDirectGraphVars.getDirectGraphViewer().vc.nodeLabelCheckBox.isSelected() ||
                MyDirectGraphVars.getDirectGraphViewer().vc.edgeLabelCheckBox.isSelected() ||
                MyDirectGraphVars.getDirectGraphViewer().isClustered ||
                MyDirectGraphVars.app.getDirectGraphDashBoard().topLevelTabbedPane.getSelectedIndex() == 3) {
                new Thread(new Runnable() {
                    @Override public void run() {
                            setDefaultView();
                        }
                }).start();
            }
        } else if (SwingUtilities.isRightMouseButton(e) && MyDirectGraphVars.getDirectGraphViewer().getPickedVertexState().getPicked().size() == 0) {
            new Thread(new Runnable() {
                @Override public void run() {
                    if (MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode != null) {
                        MyDirectGraphSelectedNodeMenu nodeMenu = new MyDirectGraphSelectedNodeMenu();
                        nodeMenu.show(MyDirectGraphVars.getDirectGraphViewer(), e.getX(), e.getY());
                    } else if (MyDirectGraphVars.getDirectGraphViewer().multiNodes != null) {

                    } else {
                        MyDirectGraphViewerMenu directGraphViewerMenu = new MyDirectGraphViewerMenu();
                        directGraphViewerMenu.show(MyDirectGraphVars.getDirectGraphViewer(), e.getX(), e.getY());
                    }
                }
            }).start();
        }
        MyDirectGraphVars.getDirectGraphViewer().revalidate();
        MyDirectGraphVars.getDirectGraphViewer().repaint();
    }

    public void setDefaultView() {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
            for (MyDirectNode n : nodes) {
                n.setCurrentValue(n.getContribution());
                if (MyDirectGraphVars.directGraph.maxNodeValue < n.getCurrentValue()) {
                    MyDirectGraphVars.directGraph.maxNodeValue = n.getCurrentValue();
                }
                n.clusteringColor = null;
            }
            pb.updateValue(20, 100);

            Collection<MyDirectEdge> edges = MyDirectGraphVars.directGraph.getEdges();
            for (MyDirectEdge e : edges) {
                e.setCurrentValue(-1);
            }
            pb.updateValue(40, 100);

            MyDirectGraphVars.getDirectGraphViewer().isExcludeBtnOn = false;
            MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode = null;
            MyDirectGraphVars.getDirectGraphViewer().multiNodes = null;
            MyDirectGraphVars.getDirectGraphViewer().selectedSingleNodePredecessorSet = null;
            MyDirectGraphVars.getDirectGraphViewer().selectedSingleNodeSuccessorSet = null;
            MyDirectGraphVars.getDirectGraphViewer().multiNodeSuccessorSet = null;
            MyDirectGraphVars.getDirectGraphViewer().multiNodePredecessorSet = null;
            MyDirectGraphVars.getDirectGraphViewer().multiNodeSharedSuccessorSet = null;
            MyDirectGraphVars.getDirectGraphViewer().multiNodeSharedPredecessorSet = null;
            MyDirectGraphVars.getDirectGraphViewer().isSharedPredecessorOnly = false;
            MyDirectGraphVars.getDirectGraphViewer().isSharedSuccessorOnly = false;
            MyDirectGraphVars.getDirectGraphViewer().isPredecessorOnly = false;
            MyDirectGraphVars.getDirectGraphViewer().isSuccessorOnly = false;
            MyDirectGraphVars.BAR_CHART_RECORD_LIMIT = 35;
            MyDirectGraphVars.getDirectGraphViewer().isClustered = false;
            MyDirectGraphVars.app.getDirectGraphDashBoard().visitedNodes = null;
            MyDirectGraphVars.app.getDirectGraphDashBoard().graphFilterPanel.isGraphFiltered = false;

            MyDirectGraphVars.getDirectGraphViewer().vc.edgeValueLabel.setVisible(true);
            MyDirectGraphVars.getDirectGraphViewer().vc.clusteringSectorLabel.setVisible(true);
            MyDirectGraphVars.getDirectGraphViewer().vc.edgeLabelLabel.setVisible(true);
            MyDirectGraphVars.getDirectGraphViewer().vc.edgeValueExcludeLabel.setVisible(true);
            MyDirectGraphVars.getDirectGraphViewer().vc.edgeLabelExcludeLabel.setVisible(true);
            MyDirectGraphVars.getDirectGraphViewer().vc.edgeLabelExcludeSymbolComboBoxMenu.setVisible(true);
            MyDirectGraphVars.getDirectGraphViewer().vc.edgeLabelValueExcludeComboBoxMenu.setVisible(true);
            MyDirectGraphVars.getDirectGraphViewer().vc.edgeValueExcludeSymbolComboBoxMenu.setVisible(true);
            MyDirectGraphVars.getDirectGraphViewer().vc.edgeValueExcludeTxt.setVisible(true);
            MyDirectGraphVars.getDirectGraphViewer().vc.edgeValueComboBoxMenu.setVisible(true);
            MyDirectGraphVars.getDirectGraphViewer().vc.edgeLabelComboBoxMenu.setVisible(true);
            MyDirectGraphVars.getDirectGraphViewer().vc.edgeLabelCheckBox.setVisible(true);
            MyDirectGraphVars.getDirectGraphViewer().vc.edgeValueCheckBox.setVisible(true);
            MyDirectGraphVars.getDirectGraphViewer().vc.removeEdgeCheckBox.setVisible(true);
            MyDirectGraphVars.getDirectGraphViewer().vc.clusteringSelector.setVisible(true);

            MyDirectGraphVars.getDirectGraphViewer().vc.nodeValueComboBoxMenu.removeActionListener(MyDirectGraphVars.getDirectGraphViewer().vc);
            MyDirectGraphVars.getDirectGraphViewer().vc.nodeLabelComboBoxMenu.removeActionListener(MyDirectGraphVars.getDirectGraphViewer().vc);
            MyDirectGraphVars.getDirectGraphViewer().vc.edgeValueComboBoxMenu.removeActionListener(MyDirectGraphVars.getDirectGraphViewer().vc);
            MyDirectGraphVars.getDirectGraphViewer().vc.edgeLabelComboBoxMenu.removeActionListener(MyDirectGraphVars.getDirectGraphViewer().vc);
            MyDirectGraphVars.getDirectGraphViewer().vc.excludeBtn.removeActionListener(MyDirectGraphVars.getDirectGraphViewer().vc);
            MyDirectGraphVars.getDirectGraphViewer().vc.removeEdgeCheckBox.removeActionListener(MyDirectGraphVars.getDirectGraphViewer().vc);
            MyDirectGraphVars.getDirectGraphViewer().vc.weightedNodeColorCheckBox.removeActionListener(MyDirectGraphVars.getDirectGraphViewer().vc);
            MyDirectGraphVars.getDirectGraphViewer().vc.nodeValueCheckBox.removeActionListener(MyDirectGraphVars.getDirectGraphViewer().vc);
            MyDirectGraphVars.getDirectGraphViewer().vc.edgeValueCheckBox.removeActionListener(MyDirectGraphVars.getDirectGraphViewer().vc);
            MyDirectGraphVars.getDirectGraphViewer().vc.edgeLabelCheckBox.removeActionListener(MyDirectGraphVars.getDirectGraphViewer().vc);
            MyDirectGraphVars.getDirectGraphViewer().vc.nodeLabelCheckBox.removeActionListener(MyDirectGraphVars.getDirectGraphViewer().vc);

            MyDirectGraphVars.getDirectGraphViewer().vc.removeEdgeCheckBox.setSelected(false);
            MyDirectGraphVars.getDirectGraphViewer().vc.weightedNodeColorCheckBox.setSelected(false);
            MyDirectGraphVars.getDirectGraphViewer().vc.nodeValueCheckBox.setSelected(false);
            MyDirectGraphVars.getDirectGraphViewer().vc.edgeValueCheckBox.setSelected(false);
            MyDirectGraphVars.getDirectGraphViewer().vc.edgeLabelCheckBox.setSelected(false);
            MyDirectGraphVars.getDirectGraphViewer().vc.nodeLabelCheckBox.setSelected(false);
            pb.updateValue(50, 100);

            MyDirectGraphVars.getDirectGraphViewer().vc.nodeValueComboBoxMenu.setSelectedIndex(0);
            MyDirectGraphVars.getDirectGraphViewer().vc.edgeValueComboBoxMenu.setSelectedIndex(0);
            MyDirectGraphVars.getDirectGraphViewer().vc.edgeLabelComboBoxMenu.setSelectedIndex(0);
            MyDirectGraphVars.getDirectGraphViewer().vc.nodeLabelComboBoxMenu.setSelectedIndex(0);
            MyDirectGraphVars.getDirectGraphViewer().vc.nodeValueExcludeSymbolComboBoxMenu.setSelectedIndex(0);
            MyDirectGraphVars.getDirectGraphViewer().vc.nodeValueExcludeTxt.setText("");

            MyDirectGraphVars.app.getDirectGraphDashBoard().nodeListTable.clearSelection();
            MyDirectGraphVars.app.getDirectGraphDashBoard().currentNodeListTable.clearSelection();
            MyDirectGraphVars.app.getDirectGraphDashBoard().edgeTable.clearSelection();
            MyDirectGraphVars.app.getDirectGraphDashBoard().statTable.clearSelection();
            MyDirectGraphVars.app.getDirectGraphDashBoard().sourceTable.clearSelection();
            MyDirectGraphVars.app.getDirectGraphDashBoard().destTable.clearSelection();

            MyDirectGraphVars.getDirectGraphViewer().vc.excludeBtn.addActionListener(MyDirectGraphVars.getDirectGraphViewer().vc);
            MyDirectGraphVars.getDirectGraphViewer().vc.removeEdgeCheckBox.addActionListener(MyDirectGraphVars.getDirectGraphViewer().vc);
            MyDirectGraphVars.getDirectGraphViewer().vc.weightedNodeColorCheckBox.addActionListener(MyDirectGraphVars.getDirectGraphViewer().vc);
            MyDirectGraphVars.getDirectGraphViewer().vc.nodeValueComboBoxMenu.addActionListener(MyDirectGraphVars.getDirectGraphViewer().vc);
            MyDirectGraphVars.getDirectGraphViewer().vc.nodeLabelComboBoxMenu.addActionListener(MyDirectGraphVars.getDirectGraphViewer().vc);
            MyDirectGraphVars.getDirectGraphViewer().vc.edgeValueComboBoxMenu.addActionListener(MyDirectGraphVars.getDirectGraphViewer().vc);
            MyDirectGraphVars.getDirectGraphViewer().vc.edgeLabelComboBoxMenu.addActionListener(MyDirectGraphVars.getDirectGraphViewer().vc);
            MyDirectGraphVars.getDirectGraphViewer().vc.nodeLabelComboBoxMenu.addActionListener(MyDirectGraphVars.getDirectGraphViewer().vc);
            MyDirectGraphVars.getDirectGraphViewer().vc.edgeValueCheckBox.addActionListener(MyDirectGraphVars.getDirectGraphViewer().vc);
            MyDirectGraphVars.getDirectGraphViewer().vc.edgeLabelCheckBox.addActionListener(MyDirectGraphVars.getDirectGraphViewer().vc);
            MyDirectGraphVars.getDirectGraphViewer().vc.nodeValueCheckBox.addActionListener(MyDirectGraphVars.getDirectGraphViewer().vc);
            pb.updateValue(60, 100);

            MyDirectGraphVars.getDirectGraphViewer().getRenderContext().setVertexLabelTransformer(new Transformer<MyDirectNode, String>() {
                @Override public String transform(MyDirectNode n) {return "";}});
            MyDirectGraphVars.app.getDirectGraphDashBoard().setTopLevelDashboard(MyDirectGraphVars.getDirectGraphViewer());
            pb.updateValue(80, 100);

            MyDirectGraphVars.getDirectGraphViewer().vc.removeBarCharts();
            MyDirectGraphVars.getDirectGraphViewer().nodeValueBarChart = new MyDirectGraphNodeValueBarChart();
            MyDirectGraphVars.getDirectGraphViewer().add(MyDirectGraphVars.getDirectGraphViewer().nodeValueBarChart);
            pb.updateValue(90, 100);

            MyDirectGraphVars.app.getDirectGraphDashBoard().txtStatistics.setTextStatistics();
            pb.updateValue(100, 100);
            pb.dispose();
        } catch (Exception ex) {
            pb.updateValue(100, 100);
            pb.dispose();
            ex.printStackTrace();
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
                        MyDirectGraphVars.getDirectGraphViewer().multiNodes = new HashSet<>();
                        MyDirectGraphVars.getDirectGraphViewer().multiNodes.add(pickedNode);
                        pb.updateValue(20, 100);
                        MyDirectGraphVars.getDirectGraphViewer().multiNodeSuccessorSet = new HashSet<>(MyDirectGraphVars.directGraph.getSuccessors(pickedNode));
                        MyDirectGraphVars.getDirectGraphViewer().multiNodePredecessorSet = new HashSet<>(MyDirectGraphVars.directGraph.getPredecessors(pickedNode));
                        MyDirectGraphVars.getDirectGraphViewer().multiNodeSharedPredecessorSet = new HashSet<>(MyDirectGraphVars.directGraph.getPredecessors(pickedNode));
                        MyDirectGraphVars.getDirectGraphViewer().multiNodeSharedSuccessorSet = new HashSet<>(MyDirectGraphVars.directGraph.getSuccessors(pickedNode));
                        pb.updateValue(50, 100);
                        while (itr.hasNext()) {
                            pickedNode = (MyDirectNode) itr.next();
                            MyDirectGraphVars.getDirectGraphViewer().multiNodes.add(pickedNode);
                            MyDirectGraphVars.getDirectGraphViewer().multiNodePredecessorSet.addAll(MyDirectGraphVars.directGraph.getPredecessors(pickedNode));
                            MyDirectGraphVars.getDirectGraphViewer().multiNodeSuccessorSet.addAll(MyDirectGraphVars.directGraph.getSuccessors(pickedNode));
                            MyDirectGraphVars.getDirectGraphViewer().multiNodeSharedSuccessorSet.retainAll(MyDirectGraphVars.directGraph.getSuccessors(pickedNode));
                            MyDirectGraphVars.getDirectGraphViewer().multiNodeSharedPredecessorSet.retainAll(MyDirectGraphVars.directGraph.getPredecessors(pickedNode));
                        }

                        if (MyDirectGraphVars.getDirectGraphViewer().vc.edgeValueComboBoxMenu.getSelectedIndex() == 0) {

                        } else {
                            for (MyDirectNode n : MyDirectGraphVars.getDirectGraphViewer().multiNodes) {
                                n.setCurrentValue(-1f);
                            }
                        }

                        pb.updateValue(80, 100);
                        MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode = null;
                        MyDirectGraphVars.app.getDirectGraphDashBoard().txtStatistics.setTextStatistics();
                        MyDirectGraphVars.app.getDirectGraphDashBoard().setMultiNodeLevelDashBoard(MyDirectGraphVars.getDirectGraphViewer());
                        MyDirectGraphVars.getDirectGraphViewer().getRenderContext().setEdgeDrawPaintTransformer(MyDirectGraphVars.getDirectGraphViewer().unWeightedEdgeColor);

                        MyDirectGraphVars.getDirectGraphViewer().vc.removeBarCharts();

                        MyDirectGraphVars.getDirectGraphViewer().multiNodeLevelNeighborNodeValueBarChart = new MyDirectGraphMultiNodeLevelNeighborNodeValueBarChart();
                        MyDirectGraphVars.getDirectGraphViewer().add(MyDirectGraphVars.getDirectGraphViewer().multiNodeLevelNeighborNodeValueBarChart);
                        pb.updateValue(100, 100);
                        pb.dispose();
                        MyDirectGraphVars.getDirectGraphViewer().revalidate();
                        MyDirectGraphVars.getDirectGraphViewer().repaint();
                    } catch (Exception ex) {
                        pb.updateValue(100, 100);
                        pb.dispose();
                        MyDirectGraphVars.getDirectGraphViewer().revalidate();
                        MyDirectGraphVars.getDirectGraphViewer().repaint();
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
