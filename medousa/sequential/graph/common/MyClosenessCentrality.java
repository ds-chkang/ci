/*
 * Created on Jul 12, 2007
 *
 * Copyright (c) 2007, the JUNG Project and the Regents of the University 
 * of California
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see either
 * "license.txt" or
 * http://jung.sourceforge.net/license.txt for a description.
 */
package medousa.sequential.graph.common;

import edu.uci.ics.jung.graph.Hypergraph;
import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.shortestpath.Distance;

/**
 * Assigns scores to each vertex based on the mean distance to each other vertex.
 * 
 * @author Joshua O'Madadhain
 */
public class MyClosenessCentrality<V,E> extends MyDistanceCentralityScorer<V,E>
{

    /**
     * Creates an instance using the specified vertex/vertex distance metric.
     * @param graph the input
     * @param distance the vertex/vertex distance metric.
     */
    public MyClosenessCentrality(Hypergraph<V,E> graph, Distance<V> distance)
    {
        super(graph, distance, true);
    }

    /**
     * Creates an instance which measures distance using the specified edge weights.
     * @param graph the input graph
     * @param edge_weights the edge weights to be used to determine vertex/vertex distances
     */
    public MyClosenessCentrality(Hypergraph<V,E> graph, Transformer<E, ? extends Number> edge_weights)
    {
        super(graph, edge_weights, true);
    }

    /**
     * Creates an instance which measures distance on the graph without edge weights.
     * @param graph
     */
    public MyClosenessCentrality(Hypergraph<V,E> graph)
    {
        super(graph, true);
    }
}
