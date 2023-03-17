package datamining.graph.analysis;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MyNetworkViewerMouseListener
implements MouseListener {

    private MyNetworkAnalyzerViewer plusNetworkAnalyzerViewer;
    protected JComboBox nodeOptionComboBox;

    public MyNetworkViewerMouseListener(MyNetworkAnalyzerViewer funnelViewer, JComboBox nodeOptionComboBox) {
        this.nodeOptionComboBox = nodeOptionComboBox;
        this.plusNetworkAnalyzerViewer = funnelViewer;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        new Thread(new Runnable() {
            @Override public void run() {
                plusNetworkAnalyzerViewer.mouseClickedLocation = e.getLocationOnScreen();
                if (SwingUtilities.isRightMouseButton(e) && plusNetworkAnalyzerViewer.getPickedVertexState().getPicked().size() == 0) {
                    if (plusNetworkAnalyzerViewer.getGraphLayout().getGraph().getVertexCount() > 0) {
                        MyNetworkViewerMenu funnelViewerMenu = new MyNetworkViewerMenu(plusNetworkAnalyzerViewer);
                        funnelViewerMenu.show(plusNetworkAnalyzerViewer, e.getX(), e.getY());
                    }
                } else if (SwingUtilities.isLeftMouseButton(e) && plusNetworkAnalyzerViewer.getPickedVertexState().getPicked().size() == 0) {
                    plusNetworkAnalyzerViewer.graphMouseListener.selectedNode = null;

                }
            }
        }).start();
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}
