package datamining.graph.listener;

import datamining.utils.system.MyVars;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class MyMotionListener
implements MouseMotionListener {

    private int minX;
    private int maxY;

    public MyMotionListener() {}

    @Override public void mouseDragged(MouseEvent e) {
        new Thread(new Runnable() {
            @Override public void run() {
            }
        }).start();
    }

    @Override public void mouseMoved(MouseEvent e) {

    }
}
