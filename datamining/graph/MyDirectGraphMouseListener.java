package datamining.graph;

import datamining.utils.system.MyVars;
import edu.uci.ics.jung.visualization.control.GraphMouseListener;

import javax.swing.*;
import java.awt.event.MouseEvent;

public class MyDirectGraphMouseListener
implements GraphMouseListener {


    @Override public void graphClicked(Object o, MouseEvent e) {
        try {
            new Thread(new Runnable() {
                @Override public void run() {
                if (o != null && SwingUtilities.isLeftMouseButton(e) && MyVars.getDirectGraphViewer().selectedSingleNode == null) {
                    MyGraphNodeSelecter graphNodeSearcher = new MyGraphNodeSelecter();
                    graphNodeSearcher.setSelectedNode((MyDirectNode) o);
                } else if (o != null && SwingUtilities.isLeftMouseButton(e) && MyVars.getDirectGraphViewer().selectedSingleNode != null) {
                    MyVars.getDirectGraphViewer().selectedSingleNode = null;
                    MyGraphNodeSelecter graphNodeSearcher = new MyGraphNodeSelecter();
                    graphNodeSearcher.setSelectedNode((MyDirectNode) o);
                }
            }}).start();
        } catch (Exception ex) {ex.printStackTrace();}
    }

    @Override public void graphPressed(Object o, MouseEvent mouseEvent) {}
    @Override public void graphReleased(Object o, MouseEvent mouseEvent) {}
}
