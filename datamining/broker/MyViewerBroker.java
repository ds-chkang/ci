package datamining.broker;

import datamining.graph.MyEdge;
import datamining.graph.MyViewer;
import datamining.graph.MyNode;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.DefaultVisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationViewer;

import java.awt.*;

public class MyViewerBroker
extends MyGraphBroker {

    protected MyViewer plusViewer;

    public MyViewerBroker() throws Exception {super();}



    public VisualizationViewer<MyNode, MyEdge> createPlusGraphView(
            final Layout<MyNode, MyEdge> l,
            final Dimension d) {
        this.plusViewer = new MyViewer(new DefaultVisualizationModel<>(l, d));
        return this.plusViewer.create();
    }

    public MyViewer getPlusMarkovChainContainer() { return this.plusViewer; }
}