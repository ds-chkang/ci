package datamining.graph;

import datamining.main.MyProgressBar;
import datamining.utils.message.MyMessageUtil;
import datamining.utils.system.MySysUtil;
import datamining.utils.system.MyVars;
import edu.uci.ics.jung.algorithms.scoring.BetweennessCentrality;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Pair;
import org.apache.commons.collections15.Transformer;

import javax.media.j3d.Link;
import java.util.*;

/**
 * An algorithm for computing clusters (community structure) in graphs based on edge betweenness.
 * The betweenness of an edge is defined as the extent to which that edge lies along
 * shortest paths between all pairs of nodes.
 *
 * This algorithm works by iteratively following the 2 step process:
 * <ul>
 * <li> Compute edge betweenness for all edges in current graph
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
public class MyEdgeBetweennessComputer<V, E> implements Transformer<Graph<V, E>, Set<Set<V>>> {
    private Map<E, Pair<V>> edges;

    public MyEdgeBetweennessComputer() {
        edges = new LinkedHashMap<E, Pair<V>>();
    }

    /**
     * Finds the set of clusters which have the strongest "community structure".
     * The more edges removed the smaller and more cohesive the clusters.
     * @param graph the graph
     */
    public Set<Set<V>> transform(Graph<V, E> graph) {
        edges.clear();
        for (int k = 0; k < graph.getEdgeCount(); k++) {
            BetweennessCentrality<V, E> bc = new BetweennessCentrality<V, E>(graph);
            for (E e : graph.getEdges()) {
                double score = bc.getEdgeScore(e);
                ((MyDirectEdge)e).betweeness = (float) score;
            }
        }
        return null;
    }

    public void compute() {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            int edgeCount = MyVars.directMarkovChain.getEdgeCount();
            edges.clear();
            for (int k = 0; k < edgeCount; ) {
                pb.updateValue(++k, edgeCount);
                BetweennessCentrality<V, E> bc = new BetweennessCentrality<V, E>(MyVars.directMarkovChain);
                for (Object e : MyVars.directMarkovChain.getEdges()) {
                    if (MyVars.currentThread.isInterrupted()) {
                        new Thread(new Runnable() {
                            @Override public void run() {
                                pb.updateValue(100, 100);
                                pb.dispose();
                                MyMessageUtil.showErrorMsg("The current execution has been interrupted.");
                                MyVars.getDirectGraphViewer().directGraphViewerMouseListener.setDefaultView();
                            }
                        }).start();
                        return;
                    } else if (((MyDirectEdge) e).getCurrentValue() != 0) {
                        double score = bc.getEdgeScore((E) e);
                        ((MyDirectEdge) e).betweeness = (float) score;
                        if (score < 0) {
                            System.out.println(score);
                        }
                    }
                }
            }
            pb.updateValue(100, 100);
            pb.dispose();
        } catch (Exception ex) {
            pb.updateValue(100, 100);
            pb.dispose();
        }
    }

    public LinkedHashMap getRankedEdgeBetweeness() {
        LinkedHashMap<String, Float> sortedEdgeMap = new LinkedHashMap();
        MyProgressBar pb = new MyProgressBar(false);
        try {
            int edgeCount = MyVars.directMarkovChain.getEdgeCount();
            edges.clear();
            for (int k = 0; k < edgeCount; ) {
                pb.updateValue(++k, edgeCount);
                BetweennessCentrality<V, E> bc = new BetweennessCentrality<V, E>(MyVars.directMarkovChain);
                for (Object e : MyVars.directMarkovChain.getEdges()) {
                    String edge = ((MyDirectEdge)e).getSource().getName() + "-" + ((MyDirectEdge)e).getDest().getName();
                    if (MyVars.currentThread.isInterrupted()) {
                        new Thread(new Runnable() {
                            @Override public void run() {
                                pb.updateValue(100, 100);
                                pb.dispose();
                                MyMessageUtil.showErrorMsg("The current execution has been interrupted.");
                                MyVars.getDirectGraphViewer().directGraphViewerMouseListener.setDefaultView();
                            }
                        }).start();
                    }
                    double score = bc.getEdgeScore((E) e);
                    if (sortedEdgeMap.containsKey(((MyDirectEdge) e).getName())) {
                        sortedEdgeMap.put(edge, sortedEdgeMap.get(edge) + 1f);
                    } else {
                        sortedEdgeMap.put(edge, (float)score);
                    }
                }
            }
            sortedEdgeMap = MySysUtil.sortMapByFloatValue(sortedEdgeMap);
            pb.updateValue(100, 100);
            pb.dispose();
            return sortedEdgeMap;
        } catch (Exception ex) {
            ex.printStackTrace();
            pb.updateValue(100, 100);
            pb.dispose();
            return null;
        }
    }

    /**
     * Retrieves the list of all edges that were removed
     * (assuming extract(...) was previously called).
     * The edges returned
     * are stored in order in which they were removed.
     *
     * @return the edges in the original graph
     */
    public List<E> getEdgesRemoved() {
        return new ArrayList<E>(edges.keySet());
    }
}
