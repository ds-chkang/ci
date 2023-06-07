package medousa.sequential.graph.listener;

import medousa.sequential.graph.MyNode;
import medousa.sequential.graph.MySequentialGraphViewer;
import medousa.sequential.graph.menu.MyDepthNodeMenu;
import medousa.sequential.graph.menu.MyMultiNodeMenu;
import medousa.sequential.graph.menu.MySingleNodeMenu;
import medousa.sequential.graph.menu.MyViewerMenu;
import medousa.MyProgressBar;
import medousa.sequential.utils.MyMultiNodeUtil;
import medousa.sequential.utils.MyViewerComponentControllerUtil;
import medousa.sequential.utils.MySequentialGraphVars;
import edu.uci.ics.jung.visualization.picking.PickedState;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MyViewerMouseListener
implements MouseListener {

    private PickedState pickedState;

    public MyViewerMouseListener(MySequentialGraphViewer viewer) {
        pickedState = viewer.getPickedVertexState();
    }

    @Override public void mouseClicked(MouseEvent e) {
        try {
            if (SwingUtilities.isLeftMouseButton(e) &&
                MySequentialGraphVars.getSequentialGraphViewer().getPickedVertexState().getPicked().size() == 0 &&
                MySequentialGraphVars.getSequentialGraphViewer().vc.depthSelecter.getSelectedIndex() == 0) {
                if (MySequentialGraphVars.getSequentialGraphViewer().singleNode != null ||
                    MySequentialGraphVars.getSequentialGraphViewer().multiNodes != null ||
                    MySequentialGraphVars.getSequentialGraphViewer().vc.tableTabbedPane.getSelectedIndex() == 2 ||
                    MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.getSelectedIndex() > 0 ||
                    MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() != 1 ||
                    MySequentialGraphVars.getSequentialGraphViewer().vc.nodeLabelSelecter.getSelectedIndex() != 1 ||
                    MySequentialGraphVars.getSequentialGraphViewer().vc.edgeLabelSelecter.getSelectedIndex() > 0 ||
                    MySequentialGraphVars.getSequentialGraphViewer().hoveredNode != null ||
                    MySequentialGraphVars.getSequentialGraphViewer().excluded) {
                    new Thread(new Runnable() {
                        @Override public void run() {
                            MyViewerComponentControllerUtil.setDefaultViewerLook();
                        }
                    }).start();
                }
            } else if (SwingUtilities.isLeftMouseButton(e) &&
                MySequentialGraphVars.getSequentialGraphViewer().getPickedVertexState().getPicked().size() == 0 &&
                MySequentialGraphVars.getSequentialGraphViewer().vc.depthSelecter.getSelectedIndex() > 0) {
                if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthNeighborNodeTypeSelector.isShowing()) {
                    new Thread(new Runnable() {
                        @Override public void run() {
                            if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthNeighborNodeTypeSelector.getSelectedIndex() > 0) {
                                MySequentialGraphVars.getSequentialGraphViewer().singleNode = null;
                                MySequentialGraphVars.getSequentialGraphViewer().singleNodeSuccessors = null;
                                MySequentialGraphVars.getSequentialGraphViewer().singleNodePredecessors = null;
                                MySequentialGraphVars.getSequentialGraphViewer().vc.depthNeighborNodeTypeSelector.setSelectedIndex(0);
                            }
                        }
                    }).start();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.selectedNodeNeighborNodeTypeSelector.isShowing()) {
                    new Thread(new Runnable() {
                        @Override public void run() {
                            if (MySequentialGraphVars.getSequentialGraphViewer().vc.selectedNodeNeighborNodeTypeSelector.getSelectedIndex() > 0) {
                                MySequentialGraphVars.getSequentialGraphViewer().singleNodeSuccessors = null;
                                MySequentialGraphVars.getSequentialGraphViewer().singleNodePredecessors = null;
                                MySequentialGraphVars.getSequentialGraphViewer().vc.selectedNodeNeighborNodeTypeSelector.setSelectedIndex(0);
                            }
                        }
                    }).start();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthSelecter.getSelectedIndex() == 0 && (MySequentialGraphVars.getSequentialGraphViewer().singleNode != null || MySequentialGraphVars.getSequentialGraphViewer().multiNodes != null || MySequentialGraphVars.getSequentialGraphViewer().excluded)) {
                    MyViewerComponentControllerUtil.setDefaultViewerLook();
                }
            } else if (SwingUtilities.isRightMouseButton(e) &&
                MySequentialGraphVars.getSequentialGraphViewer().multiNodes != null &&
                MySequentialGraphVars.getSequentialGraphViewer().vc.depthSelecter.getSelectedIndex() == 0) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        MyMultiNodeMenu multiNodeMenu = new MyMultiNodeMenu();
                        multiNodeMenu.show(MySequentialGraphVars.getSequentialGraphViewer(), e.getX(), e.getY());
                    }
                }).start();
            } else if (SwingUtilities.isRightMouseButton(e) &&
                MySequentialGraphVars.getSequentialGraphViewer().getPickedVertexState().getPicked().size() == 0 &&
                MySequentialGraphVars.getSequentialGraphViewer().multiNodes == null &&
                MySequentialGraphVars.getSequentialGraphViewer().singleNode == null &&
                MySequentialGraphVars.getSequentialGraphViewer().vc.depthSelecter.getSelectedIndex() == 0) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        MyViewerMenu graphViewerMenu = new MyViewerMenu();
                        graphViewerMenu.show(MySequentialGraphVars.getSequentialGraphViewer(), e.getX(), e.getY());
                    }
                }).start();
            } else if (SwingUtilities.isRightMouseButton(e) &&
                MySequentialGraphVars.getSequentialGraphViewer().getPickedVertexState().getPicked().size() == 0 &&
                MySequentialGraphVars.getSequentialGraphViewer().vc.depthSelecter.getSelectedIndex() > 0) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        MyDepthNodeMenu graphViewerDepthMenu = new MyDepthNodeMenu();
                        graphViewerDepthMenu.show(MySequentialGraphVars.getSequentialGraphViewer(), e.getX(), e.getY());
                    }
                }).start();
            } else if (SwingUtilities.isRightMouseButton(e) &&
                MySequentialGraphVars.getSequentialGraphViewer().singleNode != null) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        MySingleNodeMenu singleNodeMenu = new MySingleNodeMenu();
                        singleNodeMenu.show(MySequentialGraphVars.getSequentialGraphViewer(), e.getX(), e.getY());
                    }
                }).start();
            }
        } catch (Exception ex) {
            MySequentialGraphVars.getSequentialGraphViewer().revalidate();
            MySequentialGraphVars.getSequentialGraphViewer().repaint();
        }
    }

    @Override public void mousePressed(MouseEvent e) {
        try {
        } catch (Exception ex) {
            MySequentialGraphVars.getSequentialGraphViewer().revalidate();
            MySequentialGraphVars.getSequentialGraphViewer().repaint();
        }
    }

    @Override public void mouseReleased(MouseEvent e) {
            new Thread(new Runnable() {
                @Override public void run() {
                    if (SwingUtilities.isLeftMouseButton(e) && pickedState != null && pickedState.getPicked().size() > 1) {
                        setMultiNodes(pickedState.getPicked());
                    }
                }
            }).start();
    }

    @Override public void mouseEntered(MouseEvent e) {
        try {} catch (Exception ex) {
            MySequentialGraphVars.getSequentialGraphViewer().revalidate();
            MySequentialGraphVars.getSequentialGraphViewer().repaint();
        }
    }

    @Override public void mouseExited(MouseEvent e) {
        try {} catch (Exception ex) {
            MySequentialGraphVars.getSequentialGraphViewer().revalidate();
            MySequentialGraphVars.getSequentialGraphViewer().repaint();
        }
    }

    public void setMultiNodes(Set<MyNode> selectedMultiNodes) {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            MySequentialGraphVars.getSequentialGraphViewer().hoveredNode = null;
            Iterator itr = selectedMultiNodes.iterator();
            MyNode n = (MyNode) itr.next();
            pb.updateValue(10, 100);
            MySequentialGraphVars.getSequentialGraphViewer().multiNodes = new HashSet<>();
            MySequentialGraphVars.getSequentialGraphViewer().multiNodes.add(n);
            pb.updateValue(20, 100);
            MySequentialGraphVars.getSequentialGraphViewer().multiNodeSuccessors = new HashSet<>(MySequentialGraphVars.g.getSuccessors(n));
            MySequentialGraphVars.getSequentialGraphViewer().multiNodePredecessors = new HashSet<>(MySequentialGraphVars.g.getPredecessors(n));
            MySequentialGraphVars.getSequentialGraphViewer().sharedPredecessors = new HashSet<>(MySequentialGraphVars.g.getPredecessors(n));
            MySequentialGraphVars.getSequentialGraphViewer().sharedSuccessors = new HashSet<>(MySequentialGraphVars.g.getSuccessors(n));
            pb.updateValue(50, 100);
            while (itr.hasNext()) {
                n = (MyNode) itr.next();
                MySequentialGraphVars.getSequentialGraphViewer().multiNodes.add(n);
                MySequentialGraphVars.getSequentialGraphViewer().multiNodePredecessors.addAll(MySequentialGraphVars.g.getPredecessors(n));
                MySequentialGraphVars.getSequentialGraphViewer().multiNodeSuccessors.addAll(MySequentialGraphVars.g.getSuccessors(n));
                MySequentialGraphVars.getSequentialGraphViewer().sharedSuccessors.retainAll(MySequentialGraphVars.g.getSuccessors(n));
                MySequentialGraphVars.getSequentialGraphViewer().sharedPredecessors.retainAll(MySequentialGraphVars.g.getPredecessors(n));
            }
            pb.updateValue(80, 100);
            MyViewerComponentControllerUtil.removeNodeBarChartsFromViewer();
            MySequentialGraphVars.getSequentialGraphViewer().singleNode = null;
            MySequentialGraphVars.getSequentialGraphViewer().singleNodePredecessors = null;
            MySequentialGraphVars.getSequentialGraphViewer().singleNodeSuccessors = null;
            MyMultiNodeUtil.adjustMultiNodeNeighborNodeValues();
            MySequentialGraphVars.getSequentialGraphViewer().vc.updateTableInfos();
            MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueBarChart.setText("SHARED N. V. B.");
            MySequentialGraphVars.sequentialGraphDashBoard.setMultiNodeDashBoard();

            MySequentialGraphVars.getSequentialGraphViewer().vc.graphRemovalPanel.setVisible(false);
            pb.updateValue(100, 100);
            pb.dispose();

            MySequentialGraphVars.getSequentialGraphViewer().revalidate();
            MySequentialGraphVars.getSequentialGraphViewer().repaint();
        } catch (Exception ex) {
            pb.updateValue(100, 100);
            pb.dispose();
            MySequentialGraphVars.getSequentialGraphViewer().revalidate();
            MySequentialGraphVars.getSequentialGraphViewer().repaint();
        }
    }

    public void setMultiNodes(MyNode selectedNode) {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            MySequentialGraphVars.getSequentialGraphViewer().hoveredNode = null;
            MySequentialGraphVars.getSequentialGraphViewer().multiNodes = new HashSet<>();
            MySequentialGraphVars.getSequentialGraphViewer().multiNodes.add(selectedNode);
            MySequentialGraphVars.getSequentialGraphViewer().multiNodes.add(MySequentialGraphVars.getSequentialGraphViewer().singleNode);
            Iterator itr = MySequentialGraphVars.getSequentialGraphViewer().multiNodes.iterator();
            MyNode n = (MyNode) itr.next();
            MySequentialGraphVars.getSequentialGraphViewer().multiNodeSuccessors = new HashSet<>(MySequentialGraphVars.g.getSuccessors(n));
            MySequentialGraphVars.getSequentialGraphViewer().multiNodePredecessors = new HashSet<>(MySequentialGraphVars.g.getPredecessors(n));
            MySequentialGraphVars.getSequentialGraphViewer().sharedPredecessors = new HashSet<>(MySequentialGraphVars.g.getPredecessors(n));
            MySequentialGraphVars.getSequentialGraphViewer().sharedSuccessors = new HashSet<>(MySequentialGraphVars.g.getSuccessors(n));
            pb.updateValue(50, 100);
            while (itr.hasNext()) {
                n = (MyNode) itr.next();
                MySequentialGraphVars.getSequentialGraphViewer().multiNodes.add(n);
                MySequentialGraphVars.getSequentialGraphViewer().multiNodePredecessors.addAll(MySequentialGraphVars.g.getPredecessors(n));
                MySequentialGraphVars.getSequentialGraphViewer().multiNodeSuccessors.addAll(MySequentialGraphVars.g.getSuccessors(n));
                MySequentialGraphVars.getSequentialGraphViewer().sharedSuccessors.retainAll(MySequentialGraphVars.g.getSuccessors(n));
                MySequentialGraphVars.getSequentialGraphViewer().sharedPredecessors.retainAll(MySequentialGraphVars.g.getPredecessors(n));
            }
            pb.updateValue(80, 100);
            MyViewerComponentControllerUtil.removeNodeBarChartsFromViewer();
            MySequentialGraphVars.getSequentialGraphViewer().singleNode = null;
            MySequentialGraphVars.getSequentialGraphViewer().singleNodePredecessors = null;
            MySequentialGraphVars.getSequentialGraphViewer().singleNodeSuccessors = null;
            MyMultiNodeUtil.adjustMultiNodeNeighborNodeValues();
            MySequentialGraphVars.getSequentialGraphViewer().vc.vTxtStat.setTextStatistics();
            MySequentialGraphVars.getSequentialGraphViewer().vc.updateTableInfos();
            MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueBarChart.setText("SHARED N. V. B.");
            MySequentialGraphVars.sequentialGraphDashBoard.setMultiNodeDashBoard();
            MySequentialGraphVars.getSequentialGraphViewer().getRenderContext().setEdgeDrawPaintTransformer(MySequentialGraphVars.getSequentialGraphViewer().edgeColor);
            MySequentialGraphVars.getSequentialGraphViewer().vc.graphRemovalPanel.setVisible(false);
            pb.updateValue(100, 100);
            pb.dispose();

            MySequentialGraphVars.getSequentialGraphViewer().revalidate();
            MySequentialGraphVars.getSequentialGraphViewer().repaint();
        } catch (Exception ex) {
            pb.updateValue(100, 100);
            pb.dispose();
            MySequentialGraphVars.getSequentialGraphViewer().revalidate();
            MySequentialGraphVars.getSequentialGraphViewer().repaint();
        }
    }

}
