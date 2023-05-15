package medousa.sequential.graph.analysis;

import medousa.sequential.graph.funnel.MyFunnelGraphNodeMenu;
import medousa.sequential.graph.funnel.MyFunnelViewerMenu;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MyAnalysisGraphViewerMouseListener
implements MouseListener {

    private MyAnalysisGraphViewer graphViewer;

    public MyAnalysisGraphViewerMouseListener(MyAnalysisGraphViewer funnelViewer) {
        this.graphViewer = funnelViewer;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        graphViewer.mouseClickedLocation = e.getLocationOnScreen();
        /**
        new Thread(new Runnable() {
            @Override public void run() {
                graphViewer.mouseClickedLocation = e.getLocationOnScreen();
                if (SwingUtilities.isRightMouseButton(e) && graphViewer.getPickedVertexState().getPicked().size() == 0) {
                        MyAnalysisGraphViewerMenu analysisGraphViewerMenu = new MyAnalysisGraphViewerMenu(graphViewer);
                        analysisGraphViewerMenu.show(graphViewer, e.getX(), e.getY());
                } else if (SwingUtilities.isLeftMouseButton(e) && graphViewer.getPickedVertexState().getPicked().size() == 0) {
                    graphViewer.graphMouseListener.selectedNode = null;
                }
            }
        }).start();
         */
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}
