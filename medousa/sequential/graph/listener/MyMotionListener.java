package medousa.sequential.graph.listener;

import medousa.sequential.graph.MyNode;
import medousa.sequential.utils.MySequentialGraphVars;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class MyMotionListener
implements MouseMotionListener {
    public MyMotionListener() {}

    @Override public void mouseDragged(MouseEvent e) {
        try {
            new Thread(new Runnable() {
                @Override public void run() {

                }
            }).start();
        } catch (Exception ex) {

        }
    }

    @Override public void mouseMoved(MouseEvent e) {
        try {
            Point point = e.getPoint();
            MyNode n = MySequentialGraphVars.getSequentialGraphViewer().getPickSupport().getVertex(MySequentialGraphVars.getSequentialGraphViewer().getGraphLayout(), point.getX(), point.getY());
            if (n != null) {
                MySequentialGraphVars.getSequentialGraphViewer().hoveredNode = n;
                MySequentialGraphVars.getSequentialGraphViewer().revalidate();
                MySequentialGraphVars.getSequentialGraphViewer().repaint();
            } else if (n == null &&
                MySequentialGraphVars.getSequentialGraphViewer().hoveredNode != null) {
                //point.x = (int) (point.getX() + 120);
                if (MySequentialGraphVars.getSequentialGraphViewer().vc.currentNodeListTable.getWidth() < point.getX()) {
                    MySequentialGraphVars.getSequentialGraphViewer().hoveredNode = null;
                }
            }
        } catch (Exception ex) {}
    }
}
