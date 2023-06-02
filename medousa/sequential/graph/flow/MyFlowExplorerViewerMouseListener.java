package medousa.sequential.graph.flow;

import medousa.sequential.graph.MyDepthNode;
import medousa.sequential.utils.MySequentialGraphVars;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collection;

public class MyFlowExplorerViewerMouseListener
implements MouseListener {

    private MyFlowExplorerAnalyzer pathAnalyzer;
    private boolean isNotDataFlow;
    public MyDepthNode selectedNode;

    public MyFlowExplorerViewerMouseListener(MyFlowExplorerAnalyzer pathAnalyzer) {
        this.pathAnalyzer = pathAnalyzer;
    }

    public MyFlowExplorerViewerMouseListener(MyFlowExplorerAnalyzer pathAnalyzer, boolean isNotDataFlow) {
        this.pathAnalyzer = pathAnalyzer;
        this.isNotDataFlow = isNotDataFlow;
    }

    @Override public void mouseClicked(MouseEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                new Thread(new Runnable() {
                    @Override public void run() {
                        if (isNotDataFlow) {
                            if (SwingUtilities.isLeftMouseButton(e) &&
                                pathAnalyzer.graphViewer.getPickedVertexState().getPicked().size() == 0) {
                                pathAnalyzer.nodeListTable.clearSelection();
                                pathAnalyzer.allDepth.setSelected(false);
                                pathAnalyzer.weightedEdge.setSelected(false);
                            } else if (SwingUtilities.isRightMouseButton(e) &&
                                    pathAnalyzer.graphViewer.getPickedVertexState().getPicked().size() == 0) {
                                MyFlowExplorerViewerMenu flowExplorerViewerMenu = new MyFlowExplorerViewerMenu(pathAnalyzer.pathFlowGraph);
                                flowExplorerViewerMenu.show(pathAnalyzer.graphViewer, e.getX(), e.getY());
                            }
                        } else {
                            if (SwingUtilities.isLeftMouseButton(e) &&
                                MySequentialGraphVars.getSequentialGraphViewer().getPickedVertexState().getPicked().size() == 0) {
                                pathAnalyzer.nodeListTable.clearSelection();
                                pathAnalyzer.allDepth.setSelected(false);
                                pathAnalyzer.weightedEdge.setSelected(false);
                            }
                        }
                    }
                }).start();
            }
        });
    }

    @Override public void mousePressed(MouseEvent e) {
        new Thread(new Runnable() {
            @Override public void run() {
                Point point = e.getPoint();
                if (pathAnalyzer.graphViewer.getPickSupport().getVertex(pathAnalyzer.graphViewer.getGraphLayout(), e.getX(), e.getY()) != null) {
                    selectedNode = pathAnalyzer.graphViewer.getPickSupport().getVertex(pathAnalyzer.graphViewer.getGraphLayout(), point.x, point.y);
                    selectedNode.isSelectedNodePath = true;
                    Collection<MyDepthNode> successors = pathAnalyzer.graphViewer.getGraphLayout().getGraph().getSuccessors(selectedNode);
                    for (MyDepthNode successor : successors) {
                        setSuccessorsToInPathTrue(successor);
                    }

                    Collection<MyDepthNode> predecessors = pathAnalyzer.graphViewer.getGraphLayout().getGraph().getPredecessors(selectedNode);
                    for (MyDepthNode predecessor : predecessors) {
                        setPredecessorsToInPathTrue(predecessor);
                    }

                } else if (selectedNode != null) {
                    selectedNode.isSelectedNodePath = false;
                    Collection<MyDepthNode> successors = pathAnalyzer.graphViewer.getGraphLayout().getGraph().getSuccessors(selectedNode);
                    for (MyDepthNode successor : successors) {
                        setSuccessorsInPathFalse(successor);
                    }

                    Collection<MyDepthNode> predecessors = pathAnalyzer.graphViewer.getGraphLayout().getGraph().getPredecessors(selectedNode);
                    for (MyDepthNode predecessor : predecessors) {
                        setPredecessorsInPathFalse(predecessor);
                    }
                    selectedNode = null;
                } else {

                }
                pathAnalyzer.graphViewer.revalidate();
                pathAnalyzer.graphViewer.repaint();
            }
        }).start();
    }

    private void setSuccessorsToInPathTrue(MyDepthNode n) {
        n.isSelectedNodePath = true;
        Collection<MyDepthNode> successors = pathAnalyzer.graphViewer.getGraphLayout().getGraph().getSuccessors(n);
        for (MyDepthNode successor : successors) {
            setSuccessorsToInPathTrue(successor);
        }
    }

    private void setPredecessorsToInPathTrue(MyDepthNode n) {
        n.isSelectedNodePath = true;
        Collection<MyDepthNode> predecessors = pathAnalyzer.graphViewer.getGraphLayout().getGraph().getPredecessors(n);
        for (MyDepthNode predecessor : predecessors) {
            setPredecessorsToInPathTrue(predecessor);
        }
    }

    private void setSuccessorsInPathFalse(MyDepthNode n) {
        n.isSelectedNodePath = false;
        Collection<MyDepthNode> successors = pathAnalyzer.graphViewer.getGraphLayout().getGraph().getSuccessors(n);
        for (MyDepthNode successor : successors) {
            setSuccessorsInPathFalse(successor);
        }
    }

    private void setPredecessorsInPathFalse(MyDepthNode n) {
        n.isSelectedNodePath = false;
        Collection<MyDepthNode> predecessors = pathAnalyzer.graphViewer.getGraphLayout().getGraph().getPredecessors(n);
        for (MyDepthNode predecessor : predecessors) {
            setPredecessorsInPathFalse(predecessor);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
