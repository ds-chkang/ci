package datamining.graph;

import edu.uci.ics.jung.visualization.control.GraphMouseListener;

import javax.swing.*;
import java.awt.event.MouseEvent;

public class MyDirectGraphMouseListener
implements GraphMouseListener {


    @Override public void graphClicked(Object o, MouseEvent e) {
        try {
            new Thread(new Runnable() {
                @Override public void run() {
                if (o != null && SwingUtilities.isLeftMouseButton(e)) {
                    MyGraphNodeSearcher graphNodeSearcher = new MyGraphNodeSearcher();
                    graphNodeSearcher.setSelectedNode((MyDirectNode) o);
                }
            }}).start();
        } catch (Exception ex) {ex.printStackTrace();}
    }

    @Override public void graphPressed(Object o, MouseEvent mouseEvent) {}
    @Override public void graphReleased(Object o, MouseEvent mouseEvent) {}
}
