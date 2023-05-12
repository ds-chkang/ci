package medousa.sequential.graph.path;

import medousa.sequential.graph.MyNode;
import edu.uci.ics.jung.visualization.control.GraphMouseListener;

import javax.swing.*;
import java.awt.event.MouseEvent;

public class MyNodePathGraphMouseListener
implements GraphMouseListener {

    private MyNodePathGraphViewer betweenPathGraphViewer;

    public MyNodePathGraphMouseListener(MyNodePathGraphViewer betweenPathGraphViewer) {
        this.betweenPathGraphViewer = betweenPathGraphViewer;
    }

    @Override
    public void graphClicked(Object o, MouseEvent mouseEvent) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                new Thread(new Runnable() {
                    @Override public void run() {
                        if (o != null) {
                            betweenPathGraphViewer.getRenderContext().setEdgeDrawPaintTransformer(betweenPathGraphViewer.edgeDrawPainter);
                            betweenPathGraphViewer.revalidate();
                            betweenPathGraphViewer.repaint();
                            betweenPathGraphViewer.selectedNode = (MyNode)o;
                            betweenPathGraphViewer.setMaximumNodeValue();
                            betweenPathGraphViewer.setMaximumNodeSize();
                            betweenPathGraphViewer.revalidate();
                            betweenPathGraphViewer.repaint();
                        }
                    }
                }).start();
            }
        });
    }

    @Override
    public void graphPressed(Object o, MouseEvent mouseEvent) {

    }

    @Override
    public void graphReleased(Object o, MouseEvent mouseEvent) {

    }


}
