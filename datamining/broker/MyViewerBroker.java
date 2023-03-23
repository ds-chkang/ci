package datamining.broker;

import datamining.graph.MyDirectEdge;
import datamining.graph.MyDirectMarkovChainViewer;
import datamining.graph.MyDirectNode;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.DefaultVisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationViewer;

import java.awt.*;

public class MyViewerBroker
extends MyGraphBroker {

    protected MyDirectMarkovChainViewer directGraphViewer;

    public MyViewerBroker() throws Exception {super();}


    public VisualizationViewer<MyDirectNode, MyDirectEdge> createDirectGraphView(
            final Layout<MyDirectNode, MyDirectEdge> l,
            final Dimension d) {
        this.directGraphViewer = new MyDirectMarkovChainViewer(new DefaultVisualizationModel<>(l, d));
        return this.directGraphViewer.create();
    }

    public MyDirectMarkovChainViewer getDirectMarkovChainContainer() { return this.directGraphViewer; }
}