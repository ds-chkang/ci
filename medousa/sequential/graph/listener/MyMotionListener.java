package medousa.sequential.graph.listener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class MyMotionListener
implements MouseMotionListener {

    private int minX;
    private int maxY;

    public MyMotionListener() {}

    @Override public void mouseDragged(MouseEvent e) {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {

                }
            }).start();
        } catch (Exception ex) {

        }
    }

    @Override public void mouseMoved(MouseEvent e) {
        try {

        } catch (Exception ex) {
            
        }
    }
}
