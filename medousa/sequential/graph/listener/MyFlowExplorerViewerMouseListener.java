package medousa.sequential.graph.listener;

import medousa.sequential.graph.MyFlowExplorerAnalyzer;
import medousa.sequential.utils.MySequentialGraphVars;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MyFlowExplorerViewerMouseListener
implements MouseListener {

    private MyFlowExplorerAnalyzer pathAnalyzer;

    public MyFlowExplorerViewerMouseListener(MyFlowExplorerAnalyzer pathAnalyzer) {
        this.pathAnalyzer = pathAnalyzer;
    }

    @Override public void mouseClicked(MouseEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                new Thread(new Runnable() {
                    @Override public void run() {
                        if (SwingUtilities.isLeftMouseButton(e) &&
                                MySequentialGraphVars.getSequentialGraphViewer().getPickedVertexState().getPicked().size() == 0) {
                            pathAnalyzer.nodeListTable.clearSelection();
                            pathAnalyzer.allDepth.setSelected(false);
                            pathAnalyzer.weightedEdge.setSelected(false);
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
