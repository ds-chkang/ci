package medousa.direct.graph;

import medousa.direct.utils.MyDirectGraphVars;
import edu.uci.ics.jung.visualization.control.GraphMouseListener;
import edu.uci.ics.jung.visualization.picking.PickedState;

import javax.swing.*;
import java.awt.event.MouseEvent;

public class MyDirectGraphMouseListener
implements GraphMouseListener {

    private PickedState<MyDirectNode> pickedState;

    public MyDirectGraphMouseListener(PickedState<MyDirectNode> pickedState) {
        this.pickedState = pickedState;
    }

    @Override public void graphClicked(Object o, MouseEvent e) {
        try {
            new Thread(new Runnable() {
                @Override public void run() {
                    if (MyDirectGraphVars.app.getDirectGraphDashBoard().topLevelTabbedPane.getSelectedIndex() == 3) {
                    } else {
                        if (o != null && SwingUtilities.isLeftMouseButton(e) && MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode == null) {
                            MyDirectGraphNodeSelecter graphNodeSearcher = new MyDirectGraphNodeSelecter();
                            graphNodeSearcher.setSelectedNode((MyDirectNode) o);
                            MyDirectGraphVars.app.getDirectGraphDashBoard().setSelectedNodeLevelDashBoard(MyDirectGraphVars.getDirectGraphViewer());
                        } else if (o != null && SwingUtilities.isLeftMouseButton(e) && MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode != null) {
                            MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode = null;
                            MyDirectGraphNodeSelecter graphNodeSearcher = new MyDirectGraphNodeSelecter();
                            graphNodeSearcher.setSelectedNode((MyDirectNode) o);
                            MyDirectGraphVars.app.getDirectGraphDashBoard().setMultiNodeLevelDashBoard(MyDirectGraphVars.getDirectGraphViewer());
                        } else if (o != null && SwingUtilities.isLeftMouseButton(e) && MyDirectGraphVars.getDirectGraphViewer().multiNodes != null && MyDirectGraphVars.getDirectGraphViewer().multiNodes.size() > 0) {
                            MyDirectGraphNodeSelecter graphNodeSearcher = new MyDirectGraphNodeSelecter();
                            graphNodeSearcher.setSelectedNode((MyDirectNode) o);
                            MyDirectGraphVars.app.getDirectGraphDashBoard().setMultiNodeLevelDashBoard(MyDirectGraphVars.getDirectGraphViewer());
                        }
                    }

                    if (MyDirectGraphVars.getDirectGraphViewer().vc.nodeLabelComboBoxMenu.getSelectedIndex() > 0) {
                        MyDirectGraphVars.app.getDirectGraphDashBoard().nodeLabelBarChart.decorate();
                        MyDirectGraphVars.app.getDirectGraphDashBoard().chartTabbedPane.setSelectedIndex(1);

                        MyDirectGraphVars.app.getDirectGraphDashBoard().nodeLabelBarChart.revalidate();
                        MyDirectGraphVars.app.getDirectGraphDashBoard().nodeLabelBarChart.repaint();

                        MyDirectGraphVars.getDirectGraphViewer().revalidate();
                        MyDirectGraphVars.getDirectGraphViewer().repaint();
                    }
                }
            }).start();
        } catch (Exception ex) {ex.printStackTrace();}
    }

    @Override public void graphPressed(Object o, MouseEvent mouseEvent) {}
    @Override public void graphReleased(Object o, MouseEvent mouseEvent) {}
}
