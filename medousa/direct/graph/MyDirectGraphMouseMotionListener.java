package medousa.direct.graph;

import medousa.direct.utils.MyDirectGraphVars;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.HashSet;
import java.util.Set;

public class MyDirectGraphMouseMotionListener
implements MouseMotionListener {

    public MyDirectNode hoveredNode;
    public Set<MyDirectNode> neighborNodeSet;

    @Override public void mouseDragged(MouseEvent e) {
        try {}
        catch (Exception ex) {

        }
    }

    @Override public void mouseMoved(MouseEvent e) {
        new Thread(new Runnable() {
            @Override public void run() {
                if (MyDirectGraphVars.getDirectGraphViewer().vc.mouseHoverCheckBox.isSelected()) {
                    hoveredNode = MyDirectGraphVars.getDirectGraphViewer().getPickSupport().getVertex(MyDirectGraphVars.getDirectGraphViewer().getGraphLayout(), e.getX(), e.getY());
                    if (hoveredNode != null) {
                        synchronized (hoveredNode) {
                            neighborNodeSet = new HashSet<>(MyDirectGraphVars.directGraph.getNeighbors(hoveredNode));
                            neighborNodeSet.add(hoveredNode);
                            MyDirectGraphVars.app.getDirectGraphDashBoard().txtStatistics.setMouseHoveredNodeTextStatistics();
                            MyDirectGraphVars.getDirectGraphViewer().revalidate();
                            MyDirectGraphVars.getDirectGraphViewer().repaint();
                        }
                    } else {
                        neighborNodeSet = null;
                        MyDirectGraphVars.getDirectGraphViewer().repaint();
                    }
                }
            }
        }).start();
    }
}
