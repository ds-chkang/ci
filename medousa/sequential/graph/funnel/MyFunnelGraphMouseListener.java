package medousa.sequential.graph.funnel;

import edu.uci.ics.jung.visualization.control.GraphMouseListener;
import medousa.sequential.graph.MyNode;

import javax.swing.*;
import java.awt.event.MouseEvent;

public class MyFunnelGraphMouseListener
implements GraphMouseListener {

    protected MyNode selectedNode;
    protected MyFunnelGraphViewer funnelGraphViewer;

    public MyFunnelGraphMouseListener(MyFunnelGraphViewer funnelGraphViewer) {
        this.funnelGraphViewer = funnelGraphViewer;
    }

    @Override
    public void graphClicked(Object o, MouseEvent e) {
        if (o != null && SwingUtilities.isRightMouseButton(e)) {
            new Thread(new Runnable() {
                @Override public void run() {
                    selectedNode = (MyNode) o;
                    MyFunnelGraphNodeMenu funnelGraphNodeMenu = new MyFunnelGraphNodeMenu(funnelGraphViewer);
                    funnelGraphNodeMenu.show(funnelGraphViewer, e.getX(), e.getY());
                }
            }).start();
        }
    }

    @Override
    public void graphPressed(Object o, MouseEvent mouseEvent) {

    }

    @Override
    public void graphReleased(Object o, MouseEvent mouseEvent) {

    }
}
