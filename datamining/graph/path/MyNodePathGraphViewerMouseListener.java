package datamining.graph.path;

import datamining.graph.MyNode;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MyNodePathGraphViewerMouseListener
implements MouseListener {

    private MyNode mouseOverNode;
    private MyNodePathGraphViewer betweenPathGraphViewer;

    public MyNodePathGraphViewerMouseListener(MyNodePathGraphViewer betweenPathGraphViewer) {
        this.betweenPathGraphViewer = betweenPathGraphViewer;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                new Thread(new Runnable() {
                    @Override public void run() {
                        if (SwingUtilities.isRightMouseButton(e) &&
                            betweenPathGraphViewer.getPickSupport().getVertex(betweenPathGraphViewer.getGraphLayout(), e.getX(), e.getY()) == null) {
                            MyNodePathGraphViewerMenu betweenPathGraphViewerMenu = new MyNodePathGraphViewerMenu(betweenPathGraphViewer);
                            betweenPathGraphViewerMenu.show(betweenPathGraphViewer, e.getX(), e.getY());
                        } else if (betweenPathGraphViewer.selectedNode != null && SwingUtilities.isLeftMouseButton(e) &&
                            betweenPathGraphViewer.getPickSupport().getVertex(betweenPathGraphViewer.getGraphLayout(), e.getX(), e.getY()) == null) {
                            betweenPathGraphViewer.resetNodeContributionFromOriginalValue();
                            betweenPathGraphViewer.selectedNode = null;
                            betweenPathGraphViewer.revalidate();
                            betweenPathGraphViewer.repaint();
                        }
                    }
                }).start();
            }
        });
    }

    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}


}
