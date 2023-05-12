package medousa.direct.graph.path;

import medousa.direct.graph.MyDirectNode;
import edu.uci.ics.jung.visualization.control.GraphMouseListener;

import javax.swing.*;
import java.awt.event.MouseEvent;

public class MyDirectGraphPathGraphMouseListener
implements GraphMouseListener {

    private MyDirectGraphPathGraphViewer betweenPathGraphViewer;

    public MyDirectGraphPathGraphMouseListener(MyDirectGraphPathGraphViewer betweenPathGraphViewer) {
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
                            betweenPathGraphViewer.selectedNode = (MyDirectNode)o;
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
