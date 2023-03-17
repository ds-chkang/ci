package datamining.graph.analysis;

import datamining.graph.MyNode;
import edu.uci.ics.jung.visualization.control.GraphMouseListener;

import javax.swing.*;
import java.awt.event.MouseEvent;

public class MyGraphMouseListener
implements GraphMouseListener {

    private MyNetworkAnalyzerViewer networkAnalyissGraphViewer;
    public MyNode selectedNode;

    public MyGraphMouseListener(MyNetworkAnalyzerViewer networkAnalyissGraphViewer) {
        this.networkAnalyissGraphViewer = networkAnalyissGraphViewer;
    }

    @Override public void graphClicked(Object o, MouseEvent e) {
        new Thread(new Runnable() {
            @Override public void run() {
                if (o != null && SwingUtilities.isLeftMouseButton(e)) {
                    networkAnalyissGraphViewer.mouseClickedLocation = e.getLocationOnScreen();
                    selectedNode = (MyNode)o;
                    networkAnalyissGraphViewer.revalidate();
                    networkAnalyissGraphViewer.repaint();
                } else if (o != null && SwingUtilities.isRightMouseButton(e)) {
                    MyNetworkNodeMenu funnelNodeMenu = new MyNetworkNodeMenu(networkAnalyissGraphViewer, ((MyNode)o));
                    funnelNodeMenu.show(networkAnalyissGraphViewer, e.getX(), e.getY());
                }
            }
        }).start();
    }

    @Override public void graphPressed(Object o, MouseEvent mouseEvent) {}
    @Override public void graphReleased(Object o, MouseEvent mouseEvent) {}
}
