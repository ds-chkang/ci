package medousa.direct.broker;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.DefaultVisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import medousa.direct.graph.MyDirectEdge;
import medousa.direct.graph.MyDirectGraphViewer;
import medousa.direct.graph.MyDirectNode;

import java.awt.*;

public class MyDirectGraphViewerBroker
extends MyDirectGraphGraphBroker {

    protected MyDirectGraphViewer directGraphViewer;

    public MyDirectGraphViewerBroker() throws Exception {super();}


    public VisualizationViewer<MyDirectNode, MyDirectEdge> createDirectGraphView(
            final Layout<MyDirectNode, MyDirectEdge> l,
            final Dimension d) {
        this.directGraphViewer = new MyDirectGraphViewer(new DefaultVisualizationModel<>(l, d));
        return this.directGraphViewer.create();
    }

    public MyDirectGraphViewer getDirectMarkovChainContainer() { return this.directGraphViewer; }
}