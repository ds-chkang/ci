package datamining.graph;

import datamining.utils.system.MyVars;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.HashSet;
import java.util.Set;

public class MyDirectGraphMouseMotionListener
implements MouseMotionListener {

    public MyDirectNode hoveredNode;
    public Set<MyDirectNode> neighborNodeSet;

    @Override public void mouseDragged(MouseEvent e) {

    }

    @Override public void mouseMoved(MouseEvent e) {
        new Thread(new Runnable() {
            @Override public void run() {
                if (MyVars.getDirectGraphViewer().directGraphViewerControlPanel.mouseHoverCheckBox.isSelected()) {
                    hoveredNode = MyVars.getDirectGraphViewer().getPickSupport().getVertex(MyVars.getDirectGraphViewer().getGraphLayout(), e.getX(), e.getY());
                    if (hoveredNode != null) {
                        synchronized (hoveredNode) {
                            neighborNodeSet = new HashSet<>(MyVars.directMarkovChain.getNeighbors(hoveredNode));
                            neighborNodeSet.add(hoveredNode);
                            MyVars.main.getDirectMarkovChainDashBoard().directGraphTextStatisticsPanel.setMouseHoveredNodeTextStatistics();
                            MyVars.getDirectGraphViewer().revalidate();
                            MyVars.getDirectGraphViewer().repaint();
                        }
                    } else {
                        neighborNodeSet = null;
                        MyVars.getDirectGraphViewer().repaint();
                    }
                }
            }
        }).start();
    }
}
