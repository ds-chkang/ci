package medousa.direct.graph.layout;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;

import java.awt.Dimension;
import java.awt.geom.Point2D;

import edu.uci.ics.jung.graph.Graph;
import org.apache.commons.collections15.Transformer;

public class MyDirectGraphStaticLayout<V, E> extends AbstractLayout<V, E> {
    public MyDirectGraphStaticLayout(Graph<V, E> graph, Transformer<V, Point2D> initializer, Dimension size) {
        super(graph, initializer, size);
    }

    public MyDirectGraphStaticLayout(Graph<V, E> graph, Transformer<V, Point2D> initializer) {
        super(graph, initializer);
    }

    public MyDirectGraphStaticLayout(Graph<V, E> graph) {
        super(graph);
    }

    public MyDirectGraphStaticLayout(Graph<V, E> graph, Dimension size) {
        super(graph, size);
    }

    public void initialize() {
    }

    public void reset() {
    }
}