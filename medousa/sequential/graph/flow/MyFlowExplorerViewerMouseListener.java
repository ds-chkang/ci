package medousa.sequential.graph.flow;

import medousa.sequential.utils.MySequentialGraphVars;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MyFlowExplorerViewerMouseListener
implements MouseListener {

    private MyFlowExplorerAnalyzer pathAnalyzer;
    private boolean isNotDataFlow;

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

    @Override
    public void mousePressed(MouseEvent e) {

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
