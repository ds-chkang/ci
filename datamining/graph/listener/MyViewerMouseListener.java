package datamining.graph.listener;

import datamining.graph.MyNode;
import datamining.graph.MyViewer;
import datamining.graph.menu.MyDepthNodeMenu;
import datamining.graph.menu.MyMultiNodeMenu;
import datamining.graph.menu.MyViewerMenu;
import datamining.main.MyProgressBar;
import datamining.utils.MyMultiNodeUtil;
import datamining.utils.MyViewerControlComponentUtil;
import datamining.utils.system.MySysUtil;
import datamining.utils.system.MyVars;
import edu.uci.ics.jung.visualization.picking.PickedState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import java.util.Iterator;

public class MyViewerMouseListener
implements MouseListener {

    private PickedState pickedState;

    public MyViewerMouseListener(MyViewer viewer) {
        pickedState = viewer.getPickedVertexState();
    }

    @Override public void mouseClicked(MouseEvent e) {
        try {
            SwingUtilities.invokeLater(new Runnable() {
                @Override public void run() {
                    try {
                        if (SwingUtilities.isLeftMouseButton(e) &&
                            MyVars.getViewer().getPickedVertexState().getPicked().size() == 0 &&
                            MyVars.currentGraphDepth == 0) {
                            MyViewerControlComponentUtil.removeBarChartsFromViewer();
                            MyViewerControlComponentUtil.removeEdgeValueBarChartFromViewer();
                            MyVars.getViewer().vc.nodeValueBarChart.removeActionListener(MyVars.getViewer().vc);
                            MyVars.getViewer().vc.edgeValueBarChart.removeActionListener(MyVars.getViewer().vc);
                            MyVars.getViewer().vc.nodeValueBarChart.setSelected(false);
                            MyVars.getViewer().vc.edgeValueBarChart.setSelected(false);
                            MyVars.getViewer().vc.nodeValueBarChart.addActionListener(MyVars.getViewer().vc);
                            MyVars.getViewer().vc.edgeValueBarChart.addActionListener(MyVars.getViewer().vc);
                            if (MyVars.getViewer().vc.depthSelecter.getSelectedIndex() == 0 && (MyVars.getViewer().selectedNode != null || MyVars.getViewer().multiNodes != null || MyVars.getViewer().isExcludeBtnOn)) {
                                MyViewerControlComponentUtil.setDefaultLookToViewer();
                                MyViewerControlComponentUtil.setViewerComponentDefaultValues();
                            }
                        } else if (SwingUtilities.isLeftMouseButton(e) &&
                            MyVars.getViewer().getPickedVertexState().getPicked().size() == 0 &&
                            MyVars.currentGraphDepth > 0) {
                            if (MyVars.getViewer().vc.depthNeighborNodeTypeSelector.isShowing()) {
                                new Thread(new Runnable() {
                                    @Override public void run() {
                                        if (MyVars.getViewer().vc.depthNeighborNodeTypeSelector.getSelectedIndex() > 0) {
                                            MyVars.getViewer().selectedNode = null;
                                            MyVars.getViewer().selectedSingleNodeSuccessors = null;
                                            MyVars.getViewer().selectedSingleNodePredecessors = null;
                                            MyVars.getViewer().vc.depthNeighborNodeTypeSelector.setSelectedIndex(0);
                                        }
                                    }
                                }).start();
                            } else if (MyVars.getViewer().vc.selectedNodeNeighborNodeTypeSelector.isShowing()) {
                                new Thread(new Runnable() {
                                    @Override public void run() {
                                        if (MyVars.getViewer().vc.selectedNodeNeighborNodeTypeSelector.getSelectedIndex() > 0) {
                                            MyVars.getViewer().selectedSingleNodeSuccessors = null;
                                            MyVars.getViewer().selectedSingleNodePredecessors = null;
                                            MyVars.getViewer().vc.selectedNodeNeighborNodeTypeSelector.setSelectedIndex(0);
                                        }
                                    }
                                }).start();
                            } else if (MyVars.getViewer().vc.depthSelecter.getSelectedIndex() == 0 && (MyVars.getViewer().selectedNode != null || MyVars.getViewer().multiNodes != null || MyVars.getViewer().isExcludeBtnOn)) {
                                MyViewerControlComponentUtil.setDefaultLookToViewer();
                                MyViewerControlComponentUtil.setViewerComponentDefaultValues();
                            }
                        } else if (SwingUtilities.isRightMouseButton(e) &&
                            MyVars.getViewer().multiNodes != null &&
                            MyVars.currentGraphDepth == 0) {
                            new Thread(new Runnable() {
                                @Override public void run() {
                                    MyMultiNodeMenu multiNodeMenu = new MyMultiNodeMenu();
                                    multiNodeMenu.show(MyVars.getViewer(), e.getX(), e.getY());
                                }
                            }).start();
                        } else if (SwingUtilities.isRightMouseButton(e) &&
                            MyVars.getViewer().getPickedVertexState().getPicked().size() == 0 &&
                            MyVars.getViewer().multiNodes == null &&
                            MyVars.getViewer().selectedNode == null &&
                            MyVars.currentGraphDepth == 0) {
                            new Thread(new Runnable() {
                                @Override public void run() {
                                    MyViewerMenu graphViewerMenu = new MyViewerMenu();
                                    graphViewerMenu.show(MyVars.getViewer(), e.getX(), e.getY());
                                }
                            }).start();
                        } else if (SwingUtilities.isRightMouseButton(e) &&
                            //MyVars.getViewer().getPickSupport().getVertex(MyVars.getViewer().getGraphLayout(), e.getX(), e.getY()) == null &&
                            MyVars.getViewer().getPickedVertexState().getPicked().size() == 0 &&
                            MyVars.getViewer().vc.depthSelecter.getSelectedIndex() > 0) {
                            new Thread(new Runnable() {
                                @Override public void run() {
                                    MyDepthNodeMenu graphViewerDepthMenu = new MyDepthNodeMenu();
                                    graphViewerDepthMenu.show(MyVars.getViewer(), e.getX(), e.getY());
                                }
                            }).start();
                        }
                    } catch (Exception ex) {
                        MyVars.getViewer().revalidate();
                        MyVars.getViewer().repaint();
                    }
                }
            });
        } catch (Exception ex) {}
    }

    @Override public void mousePressed(MouseEvent e) {
        try {
        } catch (Exception ex) {
            MyVars.getViewer().revalidate();
            MyVars.getViewer().repaint();
        }
    }

    @Override public void mouseReleased(MouseEvent e) {
            new Thread(new Runnable() {
                @Override public void run() {
                    if (SwingUtilities.isLeftMouseButton(e) && pickedState != null && pickedState.getPicked().size() > 1) {
                        MyProgressBar pb = new MyProgressBar(false);
                        try {
                            Iterator itr = pickedState.getPicked().iterator();
                            MyNode n = (MyNode) itr.next();
                            pb.updateValue(10, 100);
                            MyVars.getViewer().multiNodes = new HashSet<>();
                            MyVars.getViewer().multiNodes.add(n);
                            pb.updateValue(20, 100);
                            MyVars.getViewer().multiNodeSuccessors = new HashSet<>(MyVars.g.getSuccessors(n));
                            MyVars.getViewer().multiNodePredecessors = new HashSet<>(MyVars.g.getPredecessors(n));
                            MyVars.getViewer().sharedPredecessors = new HashSet<>(MyVars.g.getPredecessors(n));
                            MyVars.getViewer().sharedSuccessors = new HashSet<>(MyVars.g.getSuccessors(n));
                            pb.updateValue(50, 100);
                            while (itr.hasNext()) {
                                n = (MyNode) itr.next();
                                MyVars.getViewer().multiNodes.add(n);
                                MyVars.getViewer().multiNodePredecessors.addAll(MyVars.g.getPredecessors(n));
                                MyVars.getViewer().multiNodeSuccessors.addAll(MyVars.g.getSuccessors(n));
                                MyVars.getViewer().sharedSuccessors.retainAll(MyVars.g.getSuccessors(n));
                                MyVars.getViewer().sharedPredecessors.retainAll(MyVars.g.getPredecessors(n));
                            }
                            pb.updateValue(80, 100);
                            MyViewerControlComponentUtil.removeBarChartsFromViewer();
                            MyVars.getViewer().selectedNode = null;
                            MyMultiNodeUtil.adjustMultiNodeNeighborNodeValues();
                            MyVars.getViewer().vc.vTxtStat.setTextStatistics();
                            MyVars.getViewer().vc.updateTableInfos();
                            MyVars.getViewer().vc.nodeValueBarChart.setText("SHARED N. V. B.");
                            MyVars.dashBoard.setMultiNodeDashBoard();
                            MyVars.getViewer().getRenderContext().setEdgeDrawPaintTransformer(MyVars.getViewer().edgeColor);
                            pb.updateValue(100, 100);
                            pb.dispose();
                            MyVars.getViewer().revalidate();
                            MyVars.getViewer().repaint();
                        } catch (Exception ex) {
                            pb.updateValue(100, 100);
                            pb.dispose();
                            MyVars.getViewer().revalidate();
                            MyVars.getViewer().repaint();
                        }
                    }
                }
            }).start();
    }

    @Override public void mouseEntered(MouseEvent e) {
        try {} catch (Exception ex) {
            MyVars.getViewer().revalidate();
            MyVars.getViewer().repaint();
        }
    }

    @Override public void mouseExited(MouseEvent e) {
        try {} catch (Exception ex) {
            MyVars.getViewer().revalidate();
            MyVars.getViewer().repaint();
        }
    }

}
