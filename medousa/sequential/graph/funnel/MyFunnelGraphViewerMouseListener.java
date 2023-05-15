package medousa.sequential.graph.funnel;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MyFunnelGraphViewerMouseListener
implements MouseListener {

    private MyFunnelGraphViewer funnelGraphViewer;

    public MyFunnelGraphViewerMouseListener(MyFunnelGraphViewer funnelGraphViewer) {
        this.funnelGraphViewer = funnelGraphViewer;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        new Thread(new Runnable() {
            @Override public void run() {
                funnelGraphViewer.mouseClickedLocation = e.getLocationOnScreen();
                if (SwingUtilities.isRightMouseButton(e) &&
                    funnelGraphViewer.getPickedVertexState().getPicked().size() == 0) {
                    MyFunnelViewerMenu funnelViewerMenu = new MyFunnelViewerMenu(funnelGraphViewer);
                    funnelViewerMenu.show(funnelGraphViewer, e.getX(), e.getY());
                } else if (SwingUtilities.isLeftMouseButton(e) && funnelGraphViewer.getPickedVertexState().getPicked().size() == 0) {
                    funnelGraphViewer.funnelGraphMouseListener.selectedNode = null;
                }
            }
        }).start();
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
