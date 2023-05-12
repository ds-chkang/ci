package medousa.direct.graph.path;

import medousa.direct.graph.MyDirectNode;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MyDirectGraphPathGraphViewerMouseListener
implements MouseListener {

    private MyDirectNode mouseOverNode;
    private MyDirectGraphPathGraphViewer betweenPathGraphViewer;

    public MyDirectGraphPathGraphViewerMouseListener(MyDirectGraphPathGraphViewer betweenPathGraphViewer) {
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
                            MyDirectGraphPathGraphViewerMenu betweenPathGraphViewerMenu = new MyDirectGraphPathGraphViewerMenu(betweenPathGraphViewer);
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
