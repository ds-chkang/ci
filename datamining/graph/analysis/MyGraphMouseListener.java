package datamining.graph.analysis;

import datamining.graph.MyDirectNode;
import edu.uci.ics.jung.visualization.control.GraphMouseListener;

import javax.swing.*;
import java.awt.event.MouseEvent;

public class MyGraphMouseListener
implements GraphMouseListener {

    private MyGraphAnalyzerViewer networkAnalyissGraphViewer;
    public MyDirectNode selectedNode;

    public MyGraphMouseListener(MyGraphAnalyzerViewer networkAnalyissGraphViewer) {
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
                    MyNodeMenu funnelNodeMenu = new MyNodeMenu(networkAnalyissGraphViewer, ((MyDirectNode)o));
                    funnelNodeMenu.show(networkAnalyissGraphViewer, e.getX(), e.getY());
                }
            }
        }).start();
    }

    @Override public void graphPressed(Object o, MouseEvent mouseEvent) {}
    @Override public void graphReleased(Object o, MouseEvent mouseEvent) {}
}
