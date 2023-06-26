package medousa.sequential.graph.layout;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.util.RandomLocationTransformer;
import edu.uci.ics.jung.algorithms.util.IterativeContext;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Pair;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.*;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.map.LazyMap;

public class MyFRLayout<V, E> extends AbstractLayout<V, E> implements IterativeContext {
    private double forceConstant;
    private double temperature;
    private int currentIteration;
    private int mMaxIterations = 100000;
    private Map<V, FRVertexData> frVertexData;
    private double attraction_multiplier;
    private double attraction_constant;
    private double repulsion_multiplier;
    private double repulsion_constant;
    private double max_dimension;
    private double EPSILON;

    public MyFRLayout(Graph<V, E> g, Dimension d) {
        super(g, new RandomLocationTransformer(d), d);

        class NamelessClass_1 implements Factory<FRVertexData> {
            NamelessClass_1() {
            }

            public FRVertexData create() {
                return new FRVertexData();
            }
        }

        this.frVertexData = LazyMap.decorate(new HashMap(), new NamelessClass_1());
        this.attraction_multiplier = 0.55;
        this.repulsion_multiplier = 0.55;
        this.EPSILON = 1.0E-6;
        this.initialize();
        this.max_dimension = (double)Math.max(d.height, d.width);
    }

    public void setSize(Dimension size) {
        if (!this.initialized) {
            this.setInitializer(new RandomLocationTransformer(size));
        }

        super.setSize(size);
        this.max_dimension = (double)Math.max(size.height, size.width);
    }

    public void setAttractionMultiplier(double attraction) {
        this.attraction_multiplier = attraction;
    }

    public void setRepulsionMultiplier(double repulsion) {
        this.repulsion_multiplier = repulsion;
    }

    public void reset() {
        this.doInit();
    }

    public void initialize() {
        this.doInit();
    }

    private void doInit() {
        Graph<V, E> graph = this.getGraph();
        Dimension d = this.getSize();
        if (graph != null && d != null) {
            this.currentIteration = 0;
            this.temperature = d.getWidth() / 100.0;
            this.forceConstant = Math.sqrt(d.getHeight() * d.getWidth() / (double)graph.getVertexCount());
            this.attraction_constant = this.attraction_multiplier * this.forceConstant;
            this.repulsion_constant = this.repulsion_multiplier * this.forceConstant;
        }

    }

    public synchronized void step() {
        ++this.currentIteration;

        Iterator i$;
        Object v;
        label53:
        while(true) {
            try {
                i$ = this.getGraph().getVertices().iterator();

                while(true) {
                    if (!i$.hasNext()) {
                        break label53;
                    }

                    v = (V)i$.next();
                    this.calcRepulsion((V)v);
                }
            } catch (ConcurrentModificationException var5) {
            }
        }

        label44:
        while(true) {
            try {
                i$ = this.getGraph().getEdges().iterator();

                while(true) {
                    if (!i$.hasNext()) {
                        break label44;
                    }

                    v = i$.next();
                    this.calcAttraction((E)v);
                }
            } catch (ConcurrentModificationException var4) {
            }
        }

        label35:
        while(true) {
            try {
                i$ = this.getGraph().getVertices().iterator();
                while(true) {
                    if (!i$.hasNext()) {
                        break label35;
                    }

                    v = (V)i$.next();
                    if (!this.isLocked((V)v)) {
                        this.calcPositions((V)v);
                    }
                }
            } catch (ConcurrentModificationException var3) {
            }
        }

        this.cool();
    }

    protected synchronized void calcPositions(V v) {
        FRVertexData fvd = this.getFRData(v);
        if (fvd != null) {
            Point2D xyd = this.transform(v);
            double deltaLength = Math.max(this.EPSILON, fvd.norm());
            double newXDisp = fvd.getX() / deltaLength * Math.min(deltaLength, this.temperature);
            if (Double.isNaN(newXDisp)) {
                throw new IllegalArgumentException("Unexpected mathematical result in FRLayout:calcPositions [xdisp]");
            } else {
                double newYDisp = fvd.getY() / deltaLength * Math.min(deltaLength, this.temperature);
                xyd.setLocation(xyd.getX() + newXDisp, xyd.getY() + newYDisp);
                double borderWidth = this.getSize().getWidth() / 50.0;
                double newXPos = xyd.getX();
                if (newXPos < borderWidth) {
                    newXPos = borderWidth + Math.random() * borderWidth * 2.0;
                } else if (newXPos > this.getSize().getWidth() - borderWidth) {
                    newXPos = this.getSize().getWidth() - borderWidth - Math.random() * borderWidth * 2.0;
                }

                double newYPos = xyd.getY();
                if (newYPos < borderWidth) {
                    newYPos = borderWidth + Math.random() * borderWidth * 2.0;
                } else if (newYPos > this.getSize().getHeight() - borderWidth) {
                    newYPos = this.getSize().getHeight() - borderWidth - Math.random() * borderWidth * 2.0;
                }

                xyd.setLocation(newXPos, newYPos);

                //System.out.println(xyd);
            }
        }
    }

    protected void calcAttraction(E e) {
        Pair<V> endpoints = this.getGraph().getEndpoints(e);
        V v1 = endpoints.getFirst();
        V v2 = endpoints.getSecond();
        boolean v1_locked = this.isLocked(v1);
        boolean v2_locked = this.isLocked(v2);
        if (!v1_locked || !v2_locked) {
            Point2D p1 = this.transform(v1);
            Point2D p2 = this.transform(v2);
            if (p1 != null && p2 != null) {
                double xDelta = p1.getX() - p2.getX();
                double yDelta = p1.getY() - p2.getY();
                double deltaLength = Math.max(this.EPSILON, Math.sqrt(xDelta * xDelta + yDelta * yDelta));
                double force = deltaLength * deltaLength / this.attraction_constant;
                if (Double.isNaN(force)) {
                    throw new IllegalArgumentException("Unexpected mathematical result in FRLayout:calcPositions [force]");
                } else {
                    double dx = xDelta / deltaLength * force;
                    double dy = yDelta / deltaLength * force;
                    FRVertexData fvd2;
                    if (!v1_locked) {
                        fvd2 = this.getFRData(v1);
                        fvd2.offset(-dx, -dy);
                    }

                    if (!v2_locked) {
                        fvd2 = this.getFRData(v2);
                        fvd2.offset(dx, dy);
                    }

                }
            }
        }
    }

    protected void calcRepulsion(V v1) {
        FRVertexData fvd1 = this.getFRData(v1);
        if (fvd1 != null) {
            fvd1.setLocation(0.0, 0.0);

            try {
                Iterator i$ = this.getGraph().getVertices().iterator();

                while(i$.hasNext()) {
                    V v2 = (V)i$.next();
                    if (v1 != v2) {
                        Point2D p1 = this.transform(v1);
                        Point2D p2 = this.transform(v2);
                        if (p1 != null && p2 != null) {
                            double xDelta = p1.getX() - p2.getX();
                            double yDelta = p1.getY() - p2.getY();
                            double deltaLength = Math.max(this.EPSILON, Math.sqrt(xDelta * xDelta + yDelta * yDelta));
                            double force = this.repulsion_constant * this.repulsion_constant / deltaLength;
                            if (Double.isNaN(force)) {
                                throw new RuntimeException("Unexpected mathematical result in FRLayout:calcPositions [repulsion]");
                            }

                            fvd1.offset(xDelta / deltaLength * force, yDelta / deltaLength * force);
                        }
                    }
                }
            } catch (ConcurrentModificationException var15) {
                this.calcRepulsion(v1);
            }

        }
    }

    private void cool() {
        this.temperature *= 1.0 - (double)this.currentIteration / (double)this.mMaxIterations;
    }

    public void setMaxIterations(int maxIterations) {
        this.mMaxIterations = maxIterations;
    }

    protected FRVertexData getFRData(V v) {
        return this.frVertexData.get(v);
    }

    public boolean isIncremental() {
        return true;
    }

    public boolean done() {
        return this.currentIteration > this.mMaxIterations || this.temperature < 1.0 / this.max_dimension;
    }

    protected static class FRVertexData extends Point2D.Double {
        protected FRVertexData() {
        }

        protected void offset(double x, double y) {
            this.x += x;
            this.y += y;
        }

        protected double norm() {
            return Math.sqrt(this.x * this.x + this.y * this.y);
        }
    }
}
