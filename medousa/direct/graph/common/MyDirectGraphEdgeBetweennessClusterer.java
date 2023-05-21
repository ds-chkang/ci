/*
* Copyright (c) 2003, the JUNG Project and the Regents of the University 
* of California
* All rights reserved.
*
* This software is open-source under the BSD license; see either
* "license.txt" or
* http://jung.sourceforge.net/license.txt for a description.
*/
package medousa.direct.graph.common;

import java.util.*;

import edu.uci.ics.jung.graph.Graph;
import medousa.direct.graph.MyDirectEdge;
import medousa.MyProgressBar;
import edu.uci.ics.jung.algorithms.cluster.WeakComponentClusterer;
import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.scoring.BetweennessCentrality;
import edu.uci.ics.jung.graph.util.Pair;


/**
 * An algorithm for computing clusters (community structure) in graphs based on edge betweenness.
 * The betweenness of an edge is defined as the extent to which that edge lies along 
 * shortest paths between all pairs of nodes.
 *
 * This algorithm works by iteratively following the 2 step process:
 * <ul>
 * <li> Compute edge betweenness for all edges in current graph
 * <li> Remove edge with highest betweenness
 * </ul>
 * <p>
 * Running time is: O(kmn) where k is the number of edges to remove, m is the total number of edges, and
 * n is the total number of vertices. For very sparse graphs the running time is closer to O(kn^2) and for
 * graphs with strong community structure, the complexity is even lower.
 * <p>
 * This algorithm is a slight modification of the algorithm discussed below in that the number of edges
 * to be removed is parameterized.
 * @author Scott White
 * @author Tom Nelson (converted to jung2)
 * @see "Community structure in social and biological networks by Michelle Girvan and Mark Newman"
 */
public class MyDirectGraphEdgeBetweennessClusterer<V,E> implements Transformer<Graph<V,E>,Set<Set<V>>> {
    private int mNumEdgesToRemove;
    private Map<E, Pair<V>> edges_removed;
    private Map<MyDirectEdge, Float> scoresByRemovedEdge = new HashMap<>();
    private MyProgressBar pb;

    public MyDirectGraphEdgeBetweennessClusterer(int numEdgesToRemove) {
        mNumEdgesToRemove = numEdgesToRemove;
        edges_removed = new LinkedHashMap<E, Pair<V>>();
    }

    public Set<Set<V>> transform(Graph<V,E> graph) {
        if (mNumEdgesToRemove < 0 || mNumEdgesToRemove > graph.getEdgeCount()) {
            throw new IllegalArgumentException("Invalid number of edges passed in.");
        }

        edges_removed.clear();

        this.pb = new MyProgressBar(false);
        float work = (float)50/mNumEdgesToRemove;
        float totalWork = 0f;

        for (int k=0;k<mNumEdgesToRemove;k++) {
            BetweennessCentrality<V,E> bc = new BetweennessCentrality<V,E>(graph);
            E to_remove = null;
            float score = 0;
            for (E e : graph.getEdges()) {
                if (bc.getEdgeScore(e) > score) {
                    to_remove = e;
                    score = (float)(double) bc.getEdgeScore(e);
                }
            }
            scoresByRemovedEdge.put((MyDirectEdge)to_remove, score);
            edges_removed.put(to_remove, graph.getEndpoints(to_remove));
            graph.removeEdge(to_remove);

            totalWork += work;
            this.pb.updateValue((int)totalWork, 100);
        }

        WeakComponentClusterer<V,E> wcSearch = new WeakComponentClusterer<V,E>();
        Set<Set<V>> clusterSet = wcSearch.transform(graph);

        for (Map.Entry<E, Pair<V>> entry : edges_removed.entrySet())
        {
            Pair<V> endpoints = entry.getValue();
            graph.addEdge(entry.getKey(), endpoints.getFirst(), endpoints.getSecond());
        }
        return clusterSet;
    }

    public Map<MyDirectEdge, Float> getRemovedEdgeMap() {
        return scoresByRemovedEdge;
    }

    public Map<Integer, Integer> getClustersByEdge(Graph<V,E> graph) {
        if (mNumEdgesToRemove < 0 || mNumEdgesToRemove > graph.getEdgeCount()) {
            throw new IllegalArgumentException("Invalid number of edges passed in.");
        }

        float work = (float)50/mNumEdgesToRemove;
        float totalWork = 0f;
        edges_removed.clear();
        Map<Integer, Integer> clustersByEdge = new HashMap<>();

        for (int k=0;k<mNumEdgesToRemove;k++) {
            BetweennessCentrality<V,E> bc = new BetweennessCentrality<V,E>(graph);
            E to_remove = null;
            double score = 0;
            for (E e : graph.getEdges()) {
                if (bc.getEdgeScore(e) > score) {
                    to_remove = e;
                    score = bc.getEdgeScore(e);
                }
            }
            edges_removed.put(to_remove, graph.getEndpoints(to_remove));
            graph.removeEdge(to_remove);

            WeakComponentClusterer<V,E> wcSearch = new WeakComponentClusterer<V,E>();
            Set<Set<V>> clusterSet = wcSearch.transform(graph);

            clustersByEdge.put((k+1), clusterSet.size());
            totalWork += work;
            this.pb.updateValue((int)totalWork + 50, 100);
        }
        for (Map.Entry<E, Pair<V>> entry : edges_removed.entrySet())
        {
            Pair<V> endpoints = entry.getValue();
            graph.addEdge(entry.getKey(), endpoints.getFirst(), endpoints.getSecond());
        }
        this.pb.updateValue(100, 100);
        this.pb.dispose();
        return clustersByEdge;
    }

    public List<E> getEdgesRemoved()
    {
        return new ArrayList<E>(edges_removed.keySet());
    }
}
