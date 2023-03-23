package datamining.graph.layout;

import datamining.graph.MyDirectNode;
import datamining.utils.system.MySysUtil;
import datamining.utils.system.MyVars;
import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.util.RandomLocationTransformer;
import edu.uci.ics.jung.algorithms.util.IterativeContext;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Pair;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.map.LazyMap;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;

public class MyDelegateLayout<V, E>
extends AbstractLayout<V, E>
implements IterativeContext {

    private double forceConstant;
    private double temperature;
    private int currentIteration;
    private int mMaxIterations = 1000000;

    private Map<V, FRVertexData> frVertexData = LazyMap.decorate(new HashMap<V,FRVertexData>(),
        new Factory<FRVertexData>() {
            public FRVertexData create() {
                            return new FRVertexData();
                        }
    });

    private double attraction_multiplier = 1.8;
    private double attraction_constant;
    private double repulsion_multiplier = 0.75; // from 0.55
    private double repulsion_constant;
    private double max_dimension;

    /**
     * Creates an instance for the specified datamining.spm.edu.uci.ics.jung.graph.
     */
    public MyDelegateLayout(Graph<V, E> g) {
        super(g);
    }

    /**
     * Creates an instance of size {@code d} for the specified datamining.spm.edu.uci.ics.jung.graph.
     */
    public MyDelegateLayout(Graph<V, E> g, Dimension d) {
        super(g, new RandomLocationTransformer<V>(d), d);
        initialize();
        if (!MyVars.isSupplementaryOn) {
            this.repulsion_multiplier = 0.46;
        }
        max_dimension = Math.max(d.height, d.width);
    }

    @Override
    public void setSize(Dimension size) {
        if(initialized == false) {
            setInitializer(new RandomLocationTransformer<V>(size));
        }
        super.setSize(size);
        max_dimension = Math.max(size.height, size.width);
    }

    /**
     * Sets the attraction multiplier.
     */
    public void setAttractionMultiplier(double attraction) {
        this.attraction_multiplier = attraction;
    }

    /**
     * Sets the repulsion multiplier.
     */
    public void setRepulsionMultiplier(double repulsion) {
        this.repulsion_multiplier = repulsion;
    }

    public void reset() {
        doInit();
    }

    public void initialize() {
        doInit();
    }

    private void doInit() {
        Graph<V,E> graph = getGraph();
        Dimension d = getSize();
        if(graph != null && d != null) {
            currentIteration = 0;
            temperature = d.getWidth() / 55;
            forceConstant = Math.sqrt(d.getHeight()*d.getWidth()/graph.getVertexCount());
            attraction_constant = attraction_multiplier * forceConstant;
            repulsion_constant = repulsion_multiplier * forceConstant;
        }
    }

    private double EPSILON = 0.0000001D;

    /**
     * Moves the iteration forward one notch, calculation attraction and
     * repulsion between vertices and edges and cooling the temperature.
     */
    public synchronized void step() {
        currentIteration++;
        System.out.print(currentIteration);

        /**
         * Calculate repulsion
         */
        while(true) {

            try {
                for(V v1 : getGraph().getVertices()) {
                    calcRepulsion(v1);
                }
                break;
            } catch(ConcurrentModificationException cme) {}
        }

        /**
         * Calculate attraction
         */
        while(true) {
            try {
                for(E e : getGraph().getEdges()) {
                    calcAttraction(e);
                }
                break;
            } catch(ConcurrentModificationException cme) {}
        }


        while(true) {
            try {
                for(V v : getGraph().getVertices()) {
                    if (isLocked(v)) continue;
                    calcPositions(v);
                }
                break;
            } catch(ConcurrentModificationException cme) {}
        }
        cool();
    }

    protected synchronized void calcPositions(V v) {
        FRVertexData fvd = getFRData(v);
        if(fvd == null) return;
        Point2D xyd = transform(v);
        double deltaLength = Math.max(EPSILON, fvd.norm());

        double newXDisp = fvd.getX() / deltaLength
                * Math.min(deltaLength, temperature);

        if (Double.isNaN(newXDisp)) {
            throw new IllegalArgumentException(
                    "Unexpected mathematical result in MyFRLayout:calcPositions [xdisp]");
        }

        double newYDisp = fvd.getY() / deltaLength
                * Math.min(deltaLength, temperature);
        xyd.setLocation(xyd.getX()+newXDisp, xyd.getY()+newYDisp);

        double borderWidth = getSize().getWidth() / 350.0;
        double newXPos = xyd.getX();
        if (newXPos < borderWidth) {
            newXPos = borderWidth + Math.random() * borderWidth * 2.0;
        } else if (newXPos > (getSize().getWidth() - borderWidth)) {
            newXPos = getSize().getWidth() - borderWidth - Math.random()
                    * borderWidth * 2.0;
        }

        double newYPos = xyd.getY();
        if (newYPos < borderWidth) {
            newYPos = borderWidth + Math.random() * borderWidth * 2.0;
        } else if (newYPos > (getSize().getHeight() - borderWidth)) {
            newYPos = getSize().getHeight() - borderWidth - Math.random() * borderWidth * 2.0;
        }

        System.out.println(MySysUtil.getDecodedNodeName(((MyDirectNode) v).getName()) + "   " + newXPos + "     " + newYPos);

        xyd.setLocation(newXPos, newYPos);
    }

    protected void calcAttraction(E e) {
        Pair<V> endpoints = getGraph().getEndpoints(e);

        V v1 = endpoints.getFirst();
        V v2 = endpoints.getSecond();

        boolean v1_locked = isLocked(v1);
        boolean v2_locked = isLocked(v2);

        if(v1_locked && v2_locked) {
            // both locked, do nothing
            return;
        }

        Point2D p1 = transform(v1);
        Point2D p2 = transform(v2);
        if(p1 == null || p2 == null) return;
        double xDelta = p1.getX() - p2.getX();
        double yDelta = p1.getY() - p2.getY();

        double deltaLength = Math.max(EPSILON, Math.sqrt((xDelta * xDelta) + (yDelta * yDelta)));
        double force = (deltaLength * deltaLength) / attraction_constant;

        if (Double.isNaN(force)) { throw new IllegalArgumentException(
                "Unexpected mathematical result in My3DFRLayout:calcPositions [force]"); }

        double dx = (xDelta / deltaLength) * force;
        double dy = (yDelta / deltaLength) * force;

        if(v1_locked == false) {
            FRVertexData fvd1 = getFRData(v1);
            fvd1.offset(-dx, -dy);
        }

        if(v2_locked == false) {
            FRVertexData fvd2 = getFRData(v2);
            fvd2.offset(dx, dy);
        }

    }

    protected void calcRepulsion(V v1) {
        FRVertexData fvd1 = getFRData(v1);
        if(fvd1 == null)
            return;
        fvd1.setLocation(0, 0);

        try {
            for(V v2 : getGraph().getVertices()) {

//                if (isLocked(v2)) continue;
                if (v1 != v2) {
                    Point2D p1 = transform(v1);
                    Point2D p2 = transform(v2);
                    if(p1 == null || p2 == null) continue;
                    double xDelta = p1.getX() - p2.getX();
                    double yDelta = p1.getY() - p2.getY();

                    double deltaLength = Math.max(EPSILON, Math
                            .sqrt((xDelta * xDelta) + (yDelta * yDelta)));

                    double force = (repulsion_constant * repulsion_constant) / deltaLength;

                    if (Double.isNaN(force)) { throw new RuntimeException(
                            "Unexpected mathematical result in My3DFRLayout:calcPositions [repulsion]"); }

                    fvd1.offset((xDelta / deltaLength) * force,
                            (yDelta / deltaLength) * force);
                }
            }
        } catch(ConcurrentModificationException cme) {
            calcRepulsion(v1);
        }
    }

    private void cool() {
        temperature *= (1.0 - currentIteration / (double) mMaxIterations);
    }

    /**
     * Sets the maximum number of iterations.
     */
    public void setMaxIterations(int maxIterations) {
        mMaxIterations = maxIterations;
    }

    protected FRVertexData getFRData(V v) {
        return frVertexData.get(v);
    }

    /**
     * This one is an incremental visualization.
     */
    public boolean isIncremental() {
        return true;
    }

    /**
     * Returns true once the current iteration has passed the maximum count,
     * <tt>MAX_ITERATIONS</tt>.
     */
    public boolean done() {
        if (currentIteration > mMaxIterations || temperature < 1.0/max_dimension)
        {
            return true;
        }
        return false;
    }

    protected static class FRVertexData extends Point2D.Double
    {
        protected void offset(double x, double y)
        {
            this.x += x;
            this.y += y;
        }

        protected double norm()
        {
            return Math.sqrt(x*x + y*y);
        }
    }

    public int getCurrentIteration() { return this.currentIteration; }
    public int getTototalIteration() { return this.mMaxIterations; }

}