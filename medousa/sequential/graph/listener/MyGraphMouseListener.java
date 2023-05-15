package medousa.sequential.graph.listener;

import medousa.sequential.graph.MyEdge;
import medousa.MyProgressBar;
import medousa.sequential.graph.MyNode;
import medousa.sequential.graph.menu.MySingleNodeMenu;
import medousa.sequential.utils.MySelectedNodeUtil;
import medousa.sequential.utils.MySequentialGraphVars;
import medousa.sequential.utils.MyViewerControlComponentUtil;
import edu.uci.ics.jung.visualization.control.GraphMouseListener;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.HashSet;

public class MyGraphMouseListener
implements GraphMouseListener {

    public void setSingleNodeDashBoard(Object obj) {
        try {
            MyProgressBar pb = new MyProgressBar(false);
            MySequentialGraphVars.getSequentialGraphViewer().selectedNode = (MyNode) obj;
            MySequentialGraphVars.getSequentialGraphViewer().selectedSingleNodeSuccessors = new HashSet<>();
            MySequentialGraphVars.getSequentialGraphViewer().selectedSingleNodePredecessors = new HashSet<>();
            MySequentialGraphVars.getSequentialGraphViewer().selectedSingleNodePredecessors.addAll(MySequentialGraphVars.g.getPredecessors(obj));
            MySequentialGraphVars.getSequentialGraphViewer().selectedSingleNodeSuccessors.addAll(MySequentialGraphVars.g.getSuccessors(obj));
            pb.updateValue(35, 100);
            MySelectedNodeUtil.adjustSelectedNodeNeighborNodeValues(MySequentialGraphVars.getSequentialGraphViewer().selectedNode);
            pb.updateValue(65, 100);
            MyViewerControlComponentUtil.setDepthOptionForSelectedNode();
            pb.updateValue(90, 100);

            MyViewerControlComponentUtil.removeBarChartsFromViewer();
            MyViewerControlComponentUtil.removeSharedNodeValueBarCharts();
            pb.updateValue(95, 100);

            MySequentialGraphVars.sequentialGraphDashBoard.setSingleNodeDashBoard();
            MySequentialGraphVars.getSequentialGraphViewer().getRenderContext().setEdgeDrawPaintTransformer(MySequentialGraphVars.getSequentialGraphViewer().edgeColor);
            pb.updateValue(97, 100);

            MySequentialGraphVars.getSequentialGraphViewer().vc.updateTableInfos();
            if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueBarChart.isSelected()) {
                MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueBarChart.setSelected(false);
            }
            if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueBarChart.isSelected()) {
                MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueBarChart.setSelected(false);
            }
            if (MySequentialGraphVars.getSequentialGraphViewer().vc.weightedNodeColor.isSelected()) {
                MySequentialGraphVars.getSequentialGraphViewer().vc.weightedNodeColor.setSelected(false);
            }

            MySequentialGraphVars.getSequentialGraphViewer().vc.graphRemovalPanel.setVisible(false);

            MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueBarChart.setText("P. & S. V. B.");
            MySequentialGraphVars.getSequentialGraphViewer().revalidate();
            MySequentialGraphVars.getSequentialGraphViewer().repaint();
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
                    if (MySequentialGraphVars.getSequentialGraphViewer().multiNodes != null && MySequentialGraphVars.getSequentialGraphViewer().multiNodes.size() > 0) {
                        if (MySequentialGraphVars.getSequentialGraphViewer().multiNodes.contains(obj) && SwingUtilities.isRightMouseButton(e)) {
                            MySingleNodeMenu grapNodeMenu = new MySingleNodeMenu();
                            grapNodeMenu.show(MySequentialGraphVars.getSequentialGraphViewer(), e.getX(), e.getY());
                        }
                    } else if (MySequentialGraphVars.getSequentialGraphViewer().selectedNode != null &&
                            obj == MySequentialGraphVars.getSequentialGraphViewer().selectedNode &&
                            SwingUtilities.isRightMouseButton(e)) {
                        MySingleNodeMenu grapNodeMenu = new MySingleNodeMenu();
                        grapNodeMenu.show(MySequentialGraphVars.getSequentialGraphViewer(), e.getX(), e.getY());
                    } else if (MySequentialGraphVars.getSequentialGraphViewer().getPickedVertexState().getPicked().size() > 0 &&
                            MySequentialGraphVars.getSequentialGraphViewer().selectedNode != null &&
                            MySequentialGraphVars.getSequentialGraphViewer().selectedNode == MySequentialGraphVars.getSequentialGraphViewer().getPickedVertexState().getPicked().iterator().next()) {
                        ;
                    } else if (MySequentialGraphVars.getSequentialGraphViewer().getPickedVertexState().getPicked().size() == 0 &&
                            MySequentialGraphVars.getSequentialGraphViewer().selectedNode != null &&
                            MySequentialGraphVars.getSequentialGraphViewer().selectedNode != MySequentialGraphVars.getSequentialGraphViewer().getPickedVertexState().getPicked().iterator().next()) {
                        setSingleNodeDashBoard(obj);
                    } else if (SwingUtilities.isLeftMouseButton(e) &&
                        MySequentialGraphVars.getSequentialGraphViewer().selectedNode == null &&
                        MySequentialGraphVars.getSequentialGraphViewer().getPickedVertexState().getPicked().size() == 1 &&
                        MySequentialGraphVars.getSequentialGraphViewer().vc.depthSelecter.getSelectedIndex() == 0) {
                        new Thread(new Runnable() {
                            @Override public void run() {
                                MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecter.setVisible(false);
                                MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueSelecterLabel.setVisible(false);
                                MySequentialGraphVars.getSequentialGraphViewer().selectedTableNodeSet = null;
                                setSingleNodeDashBoard(obj);
                            }
                        }).start();
                    } else if (SwingUtilities.isLeftMouseButton(e) &&
                        MySequentialGraphVars.getSequentialGraphViewer().getPickedVertexState().getPicked().size() == 1 &&
                        MySequentialGraphVars.getSequentialGraphViewer().vc.depthSelecter.getSelectedIndex() > 0 &&
                        MySequentialGraphVars.getSequentialGraphViewer().vc.depthNeighborNodeTypeSelector.getSelectedIndex() > 0) {
                        try {
                            if (!MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeNameSet.contains(((MyNode)obj).getName())) return;
                            MyProgressBar pb = new MyProgressBar(false);
                            MySequentialGraphVars.getSequentialGraphViewer().selectedNode = (MyNode) obj;

                            int i=0;
                            Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
                            for (MyEdge e : edges) {
                                if (e.getSource() == MySequentialGraphVars.getSequentialGraphViewer().selectedNode || e.getDest() == MySequentialGraphVars.getSequentialGraphViewer().selectedNode) {
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
                            MySequentialGraphVars.getSequentialGraphViewer().selectedSingleNodePredecessors = new HashSet<>(MySequentialGraphVars.g.getPredecessors(MySequentialGraphVars.getSequentialGraphViewer().selectedNode));
                            MySequentialGraphVars.getSequentialGraphViewer().selectedSingleNodeSuccessors = new HashSet<>(MySequentialGraphVars.g.getSuccessors(MySequentialGraphVars.getSequentialGraphViewer().selectedNode));

                            MySequentialGraphVars.getSequentialGraphViewer().vc.vTxtStat.setTextStatistics();
                            MyViewerControlComponentUtil.removeBarChartsFromViewer();
                            MyViewerControlComponentUtil.removeEdgeValueBarChartFromViewer();
                            MySequentialGraphVars.sequentialGraphDashBoard.setSingleNodeDashBoard();
                            MySequentialGraphVars.getSequentialGraphViewer().vc.updateTableInfos();
                            pb.updateValue(100, 100);
                            pb.dispose();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    MySequentialGraphVars.getSequentialGraphViewer().revalidate();
                    MySequentialGraphVars.getSequentialGraphViewer().repaint();
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
