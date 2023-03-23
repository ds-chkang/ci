package datamining.graph.analysis;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MyNetworkViewerMouseListener
implements MouseListener {

    private MyNetworkAnalyzerViewer graphViewer;
    protected MyGraphAnalyzer graphAnalyzer;

    public MyNetworkViewerMouseListener(MyNetworkAnalyzerViewer funnelViewer, MyGraphAnalyzer graphAnalyzer) {
        this.graphAnalyzer = graphAnalyzer;
        this.graphViewer = funnelViewer;
    }

    @Override public void mouseClicked(MouseEvent e) {
        new Thread(new Runnable() {
            @Override public void run() {
                graphViewer.mouseClickedLocation = e.getLocationOnScreen();
                if (SwingUtilities.isRightMouseButton(e) && graphViewer.getPickedVertexState().getPicked().size() == 0) {
                    if (graphViewer.getGraphLayout().getGraph().getVertexCount() > 0) {
                        MyNetworkViewerMenu funnelViewerMenu = new MyNetworkViewerMenu(graphViewer);
                        funnelViewerMenu.show(graphViewer, e.getX(), e.getY());
                    }
                } else if (SwingUtilities.isLeftMouseButton(e) && graphViewer.getPickedVertexState().getPicked().size() == 0) {
                    graphViewer.graphMouseListener.selectedNode = null;

                }
            }
        }).start();
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}
