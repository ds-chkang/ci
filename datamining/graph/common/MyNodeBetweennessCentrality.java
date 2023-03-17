package datamining.graph.common;

import edu.uci.ics.jung.algorithms.importance.AbstractRanker;
import edu.uci.ics.jung.graph.Graph;
import org.apache.commons.collections15.Buffer;
import org.apache.commons.collections15.buffer.UnboundedFifoBuffer;

import java.util.*;


public class MyNodeBetweennessCentrality<V,E> extends AbstractRanker<V,E> {

    public static final String CENTRALITY = "centrality.BetweennessCentrality";

    /**
     * Constructor which initializes the algorithm
     * @param g the graph whose nodes are to be analyzed
     */
    public MyNodeBetweennessCentrality(Graph<V,E> g) {
        initialize(g, true, true);
    }

    public MyNodeBetweennessCentrality(Graph<V,E> g, boolean rankNodes) {
        initialize(g, rankNodes, true);
    }

    public MyNodeBetweennessCentrality(Graph<V,E> g, boolean rankNodes, boolean rankEdges)
    {
        initialize(g, rankNodes, rankEdges);
    }

    public void computeBetweenness(Graph<V,E> graph) {

        Map<V,BetweennessData> decorator = new HashMap<V,BetweennessData>();
        Map<V,Number> bcVertexDecorator =
                vertexRankScores.get(getRankScoreKey());
        bcVertexDecorator.clear();
        Map<E,Number> bcEdgeDecorator =
                edgeRankScores.get(getRankScoreKey());
        bcEdgeDecorator.clear();

        Collection<V> vertices = graph.getVertices();

        for (V s : vertices) {

            initializeData(graph,decorator);

            decorator.get(s).numSPs = 1;
            decorator.get(s).distance = 0;

            Stack<V> stack = new Stack<V>();
            Buffer<V> queue = new UnboundedFifoBuffer<V>();
            queue.add(s);

            while (!queue.isEmpty()) {
                V v = queue.remove();
                stack.push(v);

                for(V w : getGraph().getSuccessors(v)) {

                    if (decorator.get(w).distance < 0) {
                        queue.add(w);
                        decorator.get(w).distance = decorator.get(v).distance + 1;
                    }

                    if (decorator.get(w).distance == decorator.get(v).distance + 1) {
                        decorator.get(w).numSPs += decorator.get(v).numSPs;
                        decorator.get(w).predecessors.add(v);
                    }
                }
            }

            while (!stack.isEmpty()) {
                V w = stack.pop();

                for (V v : decorator.get(w).predecessors) {

                    double partialDependency = (decorator.get(v).numSPs / decorator.get(w).numSPs);
                    partialDependency *= (1.0 + decorator.get(w).dependency);
                    decorator.get(v).dependency +=  partialDependency;
                    E currentEdge = getGraph().findEdge(v, w);
                    double edgeValue = bcEdgeDecorator.get(currentEdge).doubleValue();
                    edgeValue += partialDependency;
                    bcEdgeDecorator.put(currentEdge, edgeValue);
                }
                if (w != s) {
                    double bcValue = bcVertexDecorator.get(w).doubleValue();
                    bcValue += decorator.get(w).dependency;
                    bcVertexDecorator.put(w, bcValue);
                }
            }
        }

        /** It is not an undirected graph.
        if(graph instanceof UndirectedGraph) {
            for (V v : vertices) {
                double bcValue = bcVertexDecorator.get(v).doubleValue();
                bcValue /= 2.0;
                bcVertexDecorator.put(v, bcValue);
            }
            for (E e : graph.getEdges()) {
                double bcValue = bcEdgeDecorator.get(e).doubleValue();
                bcValue /= 2.0;
                bcEdgeDecorator.put(e, bcValue);
            }
        }
         */

        for (V vertex : vertices) {
            decorator.remove(vertex);
        }
    }

    private void initializeData(Graph<V,E> g, Map<V,BetweennessData> decorator) {
        for (V vertex : g.getVertices()) {

            Map<V,Number> bcVertexDecorator = vertexRankScores.get(getRankScoreKey());
            if(bcVertexDecorator.containsKey(vertex) == false) {
                bcVertexDecorator.put(vertex, 0.0);
            }
            decorator.put(vertex, new BetweennessData());
        }
        for (E e : g.getEdges()) {

            Map<E,Number> bcEdgeDecorator = edgeRankScores.get(getRankScoreKey());
            if(bcEdgeDecorator.containsKey(e) == false) {
                bcEdgeDecorator.put(e, 0.0);
            }
        }
    }

    /**
     * the user datum key used to store the rank scores
     * @return the key
     */
    @Override
    public String getRankScoreKey() {
        return CENTRALITY;
    }

    @Override
    public void step() {
        computeBetweenness(getGraph());
    }

    class BetweennessData {
        double distance;
        double numSPs;
        List<V> predecessors;
        double dependency;

        BetweennessData() {
            distance = -1;
            numSPs = 0;
            predecessors = new ArrayList<V>();
            dependency = 0;
        }
    }
}
