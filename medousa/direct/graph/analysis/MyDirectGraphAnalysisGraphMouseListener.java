package medousa.direct.graph.analysis;

import medousa.direct.graph.MyDirectNode;
import edu.uci.ics.jung.visualization.control.GraphMouseListener;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.io.Serializable;

public class MyDirectGraphAnalysisGraphMouseListener
implements GraphMouseListener, Serializable {

    private MyDirectGraphAnalysisDirectGraphViewer networkAnalyissGraphViewer;
    public MyDirectNode selectedNode;

    public MyDirectGraphAnalysisGraphMouseListener(MyDirectGraphAnalysisDirectGraphViewer networkAnalyissGraphViewer) {
        this.networkAnalyissGraphViewer = networkAnalyissGraphViewer;
    }

    @Override public void graphClicked(Object o, MouseEvent e) {
        new Thread(new Runnable() {
            @Override public void run() {
                if (o != null && SwingUtilities.isLeftMouseButton(e)) {
                    networkAnalyissGraphViewer.mouseClickedLocation = e.getLocationOnScreen();
                    selectedNode = (MyDirectNode)o;
                    networkAnalyissGraphViewer.revalidate();
                    networkAnalyissGraphViewer.repaint();
                } else if (o != null && SwingUtilities.isRightMouseButton(e)) {
                    MyDirectGraphAnalysisDirectGraphNodeMenu funnelNodeMenu = new MyDirectGraphAnalysisDirectGraphNodeMenu(networkAnalyissGraphViewer, ((MyDirectNode)o));
                    funnelNodeMenu.show(networkAnalyissGraphViewer, e.getX(), e.getY());
                }
            }
        }).start();
    }

    @Override public void graphPressed(Object o, MouseEvent mouseEvent) {}
    @Override public void graphReleased(Object o, MouseEvent mouseEvent) {}
}
