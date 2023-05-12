package medousa.direct.graph.analysis;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MyDirectGraphAnalysisDirectGraphViewerMouseListener
implements MouseListener {

    private MyDirectGraphAnalysisDirectGraphViewer graphViewer;
    protected MyDirectGraphAnalysisDirectGraphAnalyzer graphAnalyzer;

    public MyDirectGraphAnalysisDirectGraphViewerMouseListener(MyDirectGraphAnalysisDirectGraphViewer funnelViewer, MyDirectGraphAnalysisDirectGraphAnalyzer graphAnalyzer) {
        this.graphAnalyzer = graphAnalyzer;
        this.graphViewer = funnelViewer;
    }

    @Override public void mouseClicked(MouseEvent e) {
        new Thread(new Runnable() {
            @Override public void run() {
                graphViewer.mouseClickedLocation = e.getLocationOnScreen();
                if (SwingUtilities.isRightMouseButton(e) && graphViewer.getPickedVertexState().getPicked().size() == 0) {
                    if (graphViewer.getGraphLayout().getGraph().getVertexCount() > 0) {
                        MyDirectGraphAnalysisDirectGraphViewerMenu funnelViewerMenu = new MyDirectGraphAnalysisDirectGraphViewerMenu(graphViewer);
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
