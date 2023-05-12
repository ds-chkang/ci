package medousa.sequential.graph.analysis;

import medousa.sequential.graph.MyNode;
import edu.uci.ics.jung.visualization.control.GraphMouseListener;

import javax.swing.*;
import java.awt.event.MouseEvent;

public class MyAnalysisGraphMouseListener
implements GraphMouseListener {

    private MyGraphAnalyzerViewer graphViewer;
    public MyNode selectedNode;

    public MyAnalysisGraphMouseListener(MyGraphAnalyzerViewer graphViewer) {
        this.graphViewer = graphViewer;
    }

    @Override public void graphClicked(Object o, MouseEvent e) {
        new Thread(new Runnable() {
            @Override public void run() {
                if (o != null && SwingUtilities.isLeftMouseButton(e)) {
                    selectedNode = (MyNode)o;
                    graphViewer.mouseClickedLocation = e.getLocationOnScreen();
                    if (graphViewer.selectedNodEdgeValueBarChart == null) {
                        graphViewer.selectedNodEdgeValueBarChart = new MySelectedNodeEdgeValueBarChart(graphViewer);
                    }
                    graphViewer.selectedNodEdgeValueBarChart.setEdgeValueBarChartForSelectedNode();
                    graphViewer.add(graphViewer.selectedNodEdgeValueBarChart);
                    graphViewer.revalidate();
                    graphViewer.repaint();
                } else if (o != null && SwingUtilities.isRightMouseButton(e)) {
                    MyGraphNodeMenu funnelNodeMenu = new MyGraphNodeMenu(graphViewer, ((MyNode)o));
                    funnelNodeMenu.show(graphViewer, e.getX(), e.getY());
                }
            }
        }).start();
    }

    @Override public void graphPressed(Object o, MouseEvent mouseEvent) {}
    @Override public void graphReleased(Object o, MouseEvent mouseEvent) {}
}
