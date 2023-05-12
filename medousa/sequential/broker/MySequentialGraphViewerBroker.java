package medousa.sequential.broker;

import medousa.sequential.graph.MyEdge;
import medousa.sequential.graph.MySequentialGraphViewer;
import medousa.sequential.graph.MyNode;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.DefaultVisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationViewer;

import java.awt.*;

public class MySequentialGraphViewerBroker
extends MySequentialGraphBroker {

    protected MySequentialGraphViewer sequentialGraphViewer;

    public MySequentialGraphViewerBroker() throws Exception {super();}



    public VisualizationViewer<MyNode, MyEdge> createSequentialGraphView(
            final Layout<MyNode, MyEdge> l,
            final Dimension d) {
        this.sequentialGraphViewer = new MySequentialGraphViewer(new DefaultVisualizationModel<>(l, d));
        return this.sequentialGraphViewer.create();
    }

    public MySequentialGraphViewer getSequentialGraphContainer() { return this.sequentialGraphViewer; }
}