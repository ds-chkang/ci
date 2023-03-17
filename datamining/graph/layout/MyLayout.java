package datamining.graph.layout;


import java.awt.Dimension;
import java.awt.geom.Point2D;

import edu.uci.ics.jung.graph.Graph;
import org.apache.commons.collections15.Transformer;

public interface MyLayout<V, E> extends Transformer<V, Point2D> {
    void initialize();

    void setInitializer(Transformer<V, Point2D> var1);

    void setGraph(Graph<V, E> var1);

    Graph<V, E> getGraph();

    void reset();

    void setSize(Dimension var1);

    Dimension getSize();

    void lock(V var1, boolean var2);

    boolean isLocked(V var1);

    void setLocation(V var1, Point2D var2);
}
