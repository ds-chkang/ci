package datamining.graph.listener;

import datamining.graph.MyEdge;
import datamining.main.MyProgressBar;
import datamining.graph.MyNode;
import datamining.graph.menu.MySingleNodeMenu;
import datamining.utils.MySelectedNodeUtil;
import datamining.utils.system.MyVars;
import datamining.utils.MyViewerControlComponentUtil;
import edu.uci.ics.jung.visualization.control.GraphMouseListener;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.HashSet;

public class MyGraphMouseListener
implements GraphMouseListener {

    public synchronized void setSingleNodeDashBoard(Object obj) {
        try {
            MyProgressBar pb = new MyProgressBar(false);
            MyVars.getViewer().selectedNode = (MyNode) obj;
            MyVars.getViewer().selectedSingleNodeSuccessors = new HashSet<>();
            MyVars.getViewer().selectedSingleNodePredecessors = new HashSet<>();
            MyVars.getViewer().selectedSingleNodePredecessors.addAll(MyVars.g.getPredecessors(obj));
            MyVars.getViewer().selectedSingleNodeSuccessors.addAll(MyVars.g.getSuccessors(obj));
            pb.updateValue(35, 100);
            MySelectedNodeUtil.adjustSelectedNodeNeighborNodeValues(MyVars.getViewer().selectedNode);
            pb.updateValue(65, 100);

            MyVars.getViewer().vc.vTxtStat.setTextStatistics();
            pb.updateValue(85, 100);

            MyViewerControlComponentUtil.setDepthOptionForSelectedNode();
            pb.updateValue(90, 100);

            MyViewerControlComponentUtil.removeBarChartsFromViewer();
            MyViewerControlComponentUtil.removeSharedNodeValueBarCharts();
            pb.updateValue(95, 100);

            MyVars.dashBoard.setSingleNodeDashBoard();
            MyVars.getViewer().getRenderContext().setEdgeDrawPaintTransformer(MyVars.getViewer().edgeColor);
            pb.updateValue(97, 100);

            //MyVars.getViewer().getRenderContext().setEdgeDrawPaintTransformer(MyVars.getViewer().edgeColor);

            MyVars.getViewer().vc.updateTableInfos();
            MyVars.getViewer().vc.nodeValueBarChart.setText("P. & S. V. B.");
            MyVars.getViewer().revalidate();
            MyVars.getViewer().repaint();
            pb.updateValue(100, 100);
            pb.dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override public void graphClicked(Object obj, MouseEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                try {
                    if (MyVars.getViewer().multiNodes != null && MyVars.getViewer().multiNodes.size() > 0) {
                        if (MyVars.getViewer().multiNodes.contains(obj) && SwingUtilities.isRightMouseButton(e)) {
                            MySingleNodeMenu grapNodeMenu = new MySingleNodeMenu();
                            grapNodeMenu.show(MyVars.getViewer(), e.getX(), e.getY());
                        }
                    } else if (MyVars.getViewer().selectedNode != null &&
                            obj == MyVars.getViewer().selectedNode &&
                            SwingUtilities.isRightMouseButton(e)) {
                        MySingleNodeMenu grapNodeMenu = new MySingleNodeMenu();
                        grapNodeMenu.show(MyVars.getViewer(), e.getX(), e.getY());
                    } else if (MyVars.getViewer().getPickedVertexState().getPicked().iterator().next() != null &&
                            MyVars.getViewer().selectedNode != null &&
                            MyVars.getViewer().selectedNode == MyVars.getViewer().getPickedVertexState().getPicked().iterator().next()) {
                        ;
                    } else if (MyVars.getViewer().getPickedVertexState().getPicked().size() == 0 &&
                            MyVars.getViewer().selectedNode != null &&
                            MyVars.getViewer().selectedNode != MyVars.getViewer().getPickedVertexState().getPicked().iterator().next()) {
                        setSingleNodeDashBoard(obj);
                    } else if (SwingUtilities.isLeftMouseButton(e) &&
                            MyVars.getViewer().selectedNode == null &&
                            MyVars.getViewer().getPickedVertexState().getPicked().size() == 1 &&
                            MyVars.getViewer().vc.depthSelecter.getSelectedIndex() == 0) {
                        MyVars.getViewer().selectedTableNodeSet = null;
                        setSingleNodeDashBoard(obj);
                    } else if (SwingUtilities.isLeftMouseButton(e) &&
                        MyVars.getViewer().getPickedVertexState().getPicked().size() == 1 &&
                        MyVars.getViewer().vc.depthSelecter.getSelectedIndex() > 0 &&
                        MyVars.getViewer().vc.depthNeighborNodeTypeSelector.getSelectedIndex() > 0) {
                        try {
                            if (!MyVars.getViewer().vc.depthNodeNameSet.contains(((MyNode)obj).getName())) return;
                            MyProgressBar pb = new MyProgressBar(false);
                            MyVars.getViewer().selectedNode = (MyNode) obj;

                            int i=0;
                            Collection<MyEdge> edges = MyVars.g.getEdges();
                            for (MyEdge e : edges) {
                                if (e.getSource() == MyVars.getViewer().selectedNode || e.getDest() == MyVars.getViewer().selectedNode) {
                                    e.getSource().setCurrentValue(e.getSource().getContribution());
                                    e.getDest().setCurrentValue(e.getDest().getContribution());
                                    e.setCurrentValue(e.getContribution());
                                } else {
                                    e.getSource().setOriginalValue(0f);
                                    e.getDest().setOriginalValue(0f);
                                    e.setCurrentValue(0f);
                                }
                                pb.updateValue(++i, edges.size());
                            }
                            MyVars.getViewer().selectedSingleNodePredecessors = new HashSet<>(MyVars.g.getPredecessors(MyVars.getViewer().selectedNode));
                            MyVars.getViewer().selectedSingleNodeSuccessors = new HashSet<>(MyVars.g.getSuccessors(MyVars.getViewer().selectedNode));

                            MyVars.getViewer().vc.vTxtStat.setTextStatistics();
                            MyViewerControlComponentUtil.removeBarChartsFromViewer();
                            MyViewerControlComponentUtil.removeEdgeValueBarChartFromViewer();
                            MyVars.dashBoard.setSingleNodeDashBoard();
                            MyVars.getViewer().vc.updateTableInfos();
                            pb.updateValue(100, 100);
                            pb.dispose();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    MyVars.getViewer().revalidate();
                    MyVars.getViewer().repaint();
                } catch (Exception ex) {}
            }
        });
    }

    @Override public void graphPressed(Object o, MouseEvent mouseEvent) {
        try {} catch (Exception ex) {}
    }
    @Override public void graphReleased(Object o, MouseEvent mouseEvent) {
        try {} catch (Exception ex) {}
    }
}
