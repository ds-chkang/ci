package medousa.sequential.graph.analysis;

import medousa.sequential.graph.MyNode;
import edu.uci.ics.jung.visualization.control.GraphMouseListener;

import javax.swing.*;
import java.awt.event.MouseEvent;

public class MyGraphMouseListener
implements GraphMouseListener {

    private MyGraphAnalyzerViewer graphViewer;
    public MyNode selectedNode;

    public MyGraphMouseListener(MyGraphAnalyzerViewer graphViewer) {
        this.graphViewer = graphViewer;
    }

    @Override public void graphClicked(Object o, MouseEvent e) {
        new Thread(new Runnable() {
            @Override public void run() {
                if (o != null && SwingUtilities.isLeftMouseButton(e)) {
                    graphViewer.mouseClickedLocation = e.getLocationOnScreen();
                    selectedNode = (MyNode)o;
                    graphViewer.revalidate();
                    graphViewer.repaint();
                } else if (o != null && SwingUtilities.isRightMouseButton(e)) {
                    MyGraphNodeMenu funnelNodeMenu = new MyGraphNodeMenu(graphViewer, ((MyNode)o));
                    funnelNodeMenu.show(graphViewer, e.getX(), e.getY());
                }
            }
        }).start();
    }

    @Override public void graphPressed(Object o, MouseEvent mouseEvent) {}
    @Override public void graphReleased(Object o, MouseEvent mouseEvent) {}
}
