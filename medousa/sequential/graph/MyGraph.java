package medousa.sequential.graph;

import medousa.sequential.graph.layout.MyDirectedSparseMultigraph;
import medousa.sequential.utils.MySequentialGraphVars;
import edu.uci.ics.jung.graph.util.EdgeType;

import java.io.Serializable;
import java.util.*;

public class MyGraph<V, E>
extends MyDirectedSparseMultigraph<V, E>
implements Serializable {

    public long totalEdgeContribution = 0;
    public double graphEbasilon;
    public long totalContribution;
    public long totalUniqueContribution;
    public long maxUniqueContribution;
    public long maxNodeContribution;
    public long totalReachTime = 0L;
    public long maxTotalReachTime = 0L;
    public Float MX_N_VAL = 0f;
    public double MN_X_VAL = 0f;
    public Float MX_E_VAL = 42f;
    public float DEFALUT_EDGE_VALUE = 3.8f;
    public long minNodeContribution = 1000000000L;
    public long minTotalReachTime = 10000000000000L;
    public long minUniqueContribution = 1000000000L;
    public int remainingNodes = 0;
    public int remainingEdges = 0;
    public double connectance;
    public long totalDuration = 0;
    public long maxDuration = 0;
    public long minDuration = 0;
    public Map<String, MyEdge> edgeRefMap = new HashMap<>();
    public void setMaxNodeValue() {
        Collection<V> nodes = this.getVertices();
        for (V v : nodes) {
            if (((MyNode)v).getCurrentValue() > this.MX_N_VAL) {
                this.MX_N_VAL = ((MyNode)v).getCurrentValue();
            }
        }
    }

    public int getIsolatedNodeCount() {
        int isolated = 0;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (MySequentialGraphVars.g.getSuccessorCount(n) == 0 && MySequentialGraphVars.g.getPredecessorCount(n) == 0) {
                isolated++;
            }
        }
        return isolated;
    }

    public void setTotalEdgeContribution() {
        Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
        for (MyEdge edge : edges) {
            this.totalEdgeContribution += edge.getContribution();
        }
    }
    public long getTotalEdgeContribution() {return this.totalEdgeContribution;}
    public MyGraph() { super(EdgeType.UNDIRECTED); }

    public Set<MyNode> getDepthNodes() {
        Set<MyNode> depthNodes = new HashSet<>();
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getNodeDepthInfo(MySequentialGraphVars.currentGraphDepth) != null) {
                depthNodes.add(n);
            }
        }
        return depthNodes;
    }

    public float getMinimumNodeValue() {
        float min = 1000000000000f;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0 || MyClusteringConfig.selectedClusterColor != null && (MyClusteringConfig.selectedClusterColor != n.clusteringColor)) continue;
            if (min > n.getCurrentValue()) {
                min = n.getCurrentValue();
            }
        }
        return (min == 1000000000000f ? 0f : min);
    }

    public float getClusteredMinimumNodeValue() {
        float min = 1000000000000f;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0 || MyClusteringConfig.selectedClusterColor != null && (MyClusteringConfig.selectedClusterColor != n.clusteringColor)) continue;
            if (min > n.getCurrentValue()) {
                min = n.getCurrentValue();
            }
        }
        return (min == 1000000000000f ? 0f : min);
    }

    public float getMaximumNodeValue() {
        float max = 0;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0 || MyClusteringConfig.selectedClusterColor != null && (MyClusteringConfig.selectedClusterColor != n.clusteringColor)) continue;
            if (max < n.getCurrentValue()) {
                max = n.getCurrentValue();
            }
        }
        return max;
    }

    public float getClusteredMaximumNodeValue() {
        float max = 0;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0 || MyClusteringConfig.selectedClusterColor != null && (MyClusteringConfig.selectedClusterColor != n.clusteringColor)) continue;
            if (max < n.getCurrentValue()) {
                max = n.getCurrentValue();
            }
        }
        return max;
    }

    public int getGraphPredecessorCount() {
        return MySequentialGraphVars.getSequentialGraphViewer().singleNodePredecessors.size();
    }

    public int getGraphSuccessorCount() {
        return MySequentialGraphVars.getSequentialGraphViewer().singleNodeSuccessors.size();
    }

    public float getClusteredAverageNodeValue() {
        float total = 0;
        int count = 0;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0 || MyClusteringConfig.selectedClusterColor != null && (MyClusteringConfig.selectedClusterColor != n.clusteringColor)) continue;
            total += n.getCurrentValue();
            count++;
        }
        return (count == 0 ? 0.0f : (total/count));
    }

    public float getMaximumEdgeValue() {
        if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() == 0) return 0f;
        float max = 0;
        Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
        for (MyEdge e : edges) {
            if (MyClusteringConfig.selectedClusterColor != null && (e.getSource().clusteringColor != MyClusteringConfig.selectedClusterColor ||
                    e.getDest().clusteringColor != MyClusteringConfig.selectedClusterColor)) continue;
            if (e.getCurrentValue() == 0 || e.getSource().getCurrentValue() == 0 || e.getDest().getCurrentValue() == 0) continue;
            if (max < e.getCurrentValue()) {
                max = e.getCurrentValue();
            }
        }
        return max;
    }

    public float getClusteredMaximumEdgeValue() {
        if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() == 0) return 0f;
        float max = 0;
        Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
        for (MyEdge e : edges) {
            if (MyClusteringConfig.selectedClusterColor != null && (e.getSource().clusteringColor != MyClusteringConfig.selectedClusterColor ||
                    e.getDest().clusteringColor != MyClusteringConfig.selectedClusterColor)) continue;
            if (e.getCurrentValue() == 0 || e.getSource().getCurrentValue() == 0 || e.getDest().getCurrentValue() == 0) continue;
            if (max < e.getCurrentValue()) {
                max = e.getCurrentValue();
            }
        }
        return max;
    }

    public float getMinimumEdgeValue() {
        if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() == 0) return 0f;
        float min = 10000000000f;
        Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
        for (MyEdge e : edges) {
            if (MyClusteringConfig.selectedClusterColor != null && (e.getSource().clusteringColor != MyClusteringConfig.selectedClusterColor ||
                    e.getDest().clusteringColor != MyClusteringConfig.selectedClusterColor)) continue;
            if (e.getCurrentValue() == 0 || e.getSource().getCurrentValue() == 0 || e.getDest().getCurrentValue() == 0) continue;
            if (min > e.getCurrentValue()) {
                min = e.getCurrentValue();
            }
        }
        return (min == 10000000000f ? 0f : min);
    }

    public float getClusteredMinimumEdgeValue() {
        if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() == 0) return 0f;
        float min = 10000000000f;
        Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
        for (MyEdge e : edges) {
            if (MyClusteringConfig.selectedClusterColor != null && (e.getSource().clusteringColor != MyClusteringConfig.selectedClusterColor ||
                    e.getDest().clusteringColor != MyClusteringConfig.selectedClusterColor)) continue;
            if (e.getCurrentValue() == 0 || e.getSource().getCurrentValue() == 0 || e.getDest().getCurrentValue() == 0) continue;
            if (min > e.getCurrentValue()) {
                min = e.getCurrentValue();
            }
        }
        return (min == 10000000000f ? 0f : min);
    }


    public float getAverageEdgeValue() {
        if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() == 0) return 0f;
        float total = 0;
        int count = 0;
        Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
        for (MyEdge e : edges) {
            if (MyClusteringConfig.selectedClusterColor != null && (e.getSource().clusteringColor != MyClusteringConfig.selectedClusterColor ||
                    e.getDest().clusteringColor != MyClusteringConfig.selectedClusterColor)) continue;
            if (e.getCurrentValue() == 0 || e.getSource().getCurrentValue() == 0 || e.getDest().getCurrentValue() == 0) continue;
            total += e.getCurrentValue();
            count++;
        }
        return (count == 0 ? 0.0f : (total/count));
    }

    public float getClusteredAverageEdgeValue() {
        if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() == 0) return 0f;
        float total = 0;
        int count = 0;
        Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
        for (MyEdge e : edges) {
            if (MyClusteringConfig.selectedClusterColor != null && (e.getSource().clusteringColor != MyClusteringConfig.selectedClusterColor ||
                    e.getDest().clusteringColor != MyClusteringConfig.selectedClusterColor)) continue;
            if (e.getCurrentValue() == 0 || e.getSource().getCurrentValue() == 0 || e.getDest().getCurrentValue() == 0) continue;
            total += e.getCurrentValue();
            count++;
        }
        return (count == 0 ? 0.0f : (total/count));
    }

    public int getMaximumSuccessorCount() {
        int max = 0;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0) continue;
            int value = MySequentialGraphVars.g.getSuccessorCount(n);
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    public int getClusteredMaximumSuccessorCount() {
        int max = 0;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0 || MyClusteringConfig.selectedClusterColor != null && (MyClusteringConfig.selectedClusterColor != n.clusteringColor)) continue;
            int value = MySequentialGraphVars.g.getSuccessorCount(n);
            if (value > max) {
                max = value;
            }
        }
        return max;
    }


    public float getAverageSuccessorCount() {
        float total = 0;
        int count = 0;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0 || MyClusteringConfig.selectedClusterColor != null && (MyClusteringConfig.selectedClusterColor != n.clusteringColor)) continue;
            total += MySequentialGraphVars.g.getSuccessorCount(n);
            count++;
        }
        return (count == 0 ? 0.0f : (total/count));
    }

    public float getClusteredAverageSuccessorCount() {
        float total = 0;
        int count = 0;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0 || MyClusteringConfig.selectedClusterColor != null && (MyClusteringConfig.selectedClusterColor != n.clusteringColor)) continue;
            total += MySequentialGraphVars.g.getSuccessorCount(n);
            count++;
        }
        return (count == 0 ? 0.0f : (total/count));
    }


    public float getAveragePredecessorCount() {
        long total = 0;
        int count = 0;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0 || MyClusteringConfig.selectedClusterColor != null && (MyClusteringConfig.selectedClusterColor != n.clusteringColor)) continue;
            total += MySequentialGraphVars.g.getPredecessorCount(n);
            count++;
        }
        return (count == 0 ? 0.0f : (total/count));
    }

    public float getAverageNodeValue() {
        long total = 0;
        int count = 0;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0 || MyClusteringConfig.selectedClusterColor != null && (MyClusteringConfig.selectedClusterColor != n.clusteringColor)) continue;
            total += n.getCurrentValue();
            count++;
        }
        return (count == 0 ? 0.0f : (total/count));
    }

    public float getClusteredAveragePredecessorCount() {
        float total = 0;
        int count = 0;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0 || MyClusteringConfig.selectedClusterColor != null && (MyClusteringConfig.selectedClusterColor != n.clusteringColor)) continue;
            total += MySequentialGraphVars.g.getPredecessorCount(n);
            count++;
        }
        return (count == 0 ? 0.0f : (total/count));
    }

    public float getNodeValueStandardDeviation() {
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        float sum = 0f;
        int numNodes = 0;
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0 || MyClusteringConfig.selectedClusterColor != null && (MyClusteringConfig.selectedClusterColor != n.clusteringColor)) continue;
            numNodes++;
            sum += n.getCurrentValue();
        }
        if (numNodes == 0) return 0f;

        float mean = sum/numNodes;
        float std = 0f;
        for(MyNode n : nodes) {
            std += Math.pow(n.getCurrentValue()-mean, 2);
        }
        return (std/numNodes);
    }

    public float getClusteredNodeValueStandardDeviation() {
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        float sum = 0f;
        int numNodes = 0;
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0 || MyClusteringConfig.selectedClusterColor != null && (MyClusteringConfig.selectedClusterColor != n.clusteringColor)) continue;
            numNodes++;
            sum += n.getCurrentValue();
        }
        if (numNodes == 0) return 0f;

        float mean = sum/numNodes;
        float std = 0f;
        for(MyNode n : nodes) {
            std += Math.pow(n.getCurrentValue()-mean, 2);
        }
        return (std/numNodes);
    }

    public float getEdgeValueStandardDeviation() {
        if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() == 0) return 0f;
        Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
        float sum = 0f;
        int count = 0;
        for (MyEdge e : edges) {
            if (e.getCurrentValue() == 0) {continue;}
            count++;
            sum += e.getCurrentValue();
        }
        if (count == 0) return 0f;

        float mean = sum/count;
        float std = 0f;
        for(MyEdge e : edges) {
            std += Math.pow(e.getCurrentValue()-mean, 2);
        }
        return (std/count);
    }

    public int getMaximumPredecessorCount() {
        int max = 0;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0) continue;
            int value = MySequentialGraphVars.g.getPredecessorCount(n);
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    public int getClusteredMaximumPredecessorCount() {
        int max = 0;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0 || MyClusteringConfig.selectedClusterColor != null && (MyClusteringConfig.selectedClusterColor != n.clusteringColor)) continue;
            int value = MySequentialGraphVars.g.getPredecessorCount(n);
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    public int getMinimumSuccessorCount() {
        int min = 100000000;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n: nodes) {
            if (n.getCurrentValue() == 0) continue;
            int value = MySequentialGraphVars.g.getSuccessorCount(n);
            if (value < min) {
                min = value;
            }
        }
        return (min == 100000000 ? 0 : min);
    }

    public int getMinimumPredecessorCount() {
        int min = 100000000;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n: nodes) {
            if (n.getCurrentValue() == 0) continue;
            int value = MySequentialGraphVars.g.getPredecessorCount(n);
            if (value < min) {
                min = value;
            }
        }
        return (min == 100000000 ? 0 : min);
    }

    public int getRedNodeCount() {
        int count = 0;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0 || MyClusteringConfig.selectedClusterColor != null && (MyClusteringConfig.selectedClusterColor != n.clusteringColor)) continue;
            if (MySequentialGraphVars.g.getPredecessorCount(n) == 0 && MySequentialGraphVars.g.getSuccessorCount(n) > 0) {
                count++;
            }
        }
        return count;
    }

    public int getBlueNodeCount() {
        int count = 0;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0 || MyClusteringConfig.selectedClusterColor != null && (MyClusteringConfig.selectedClusterColor != n.clusteringColor)) continue;
            if (MySequentialGraphVars.g.getPredecessorCount(n) > 0 && MySequentialGraphVars.g.getSuccessorCount(n) > 0) {
                count++;
            }
        }
        return count;
    }

    public int getClusteredRedNodeCount() {
        int count = 0;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0 || MyClusteringConfig.selectedClusterColor != null && (MyClusteringConfig.selectedClusterColor != n.clusteringColor)) continue;
            if (MySequentialGraphVars.g.getPredecessorCount(n) == 0 && MySequentialGraphVars.g.getSuccessorCount(n) > 0) {
                count++;
            }
        }
        return count;
    }

    public int getClusteredBlueNodeCount() {
        int count = 0;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0 || MyClusteringConfig.selectedClusterColor != null && (MyClusteringConfig.selectedClusterColor != n.clusteringColor)) continue;
            if (MySequentialGraphVars.g.getPredecessorCount(n) > 0 && MySequentialGraphVars.g.getSuccessorCount(n) > 0) {
                count++;
            }
        }
        return count;
    }


    public int getGreenNodeCount() {
        int count = 0;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0) continue;;
            if (MySequentialGraphVars.g.getPredecessorCount(n) > 0 && MySequentialGraphVars.g.getSuccessorCount(n) == 0) {
                count++;
            }
        }
        return count;
    }

    public int getClusteredGreenNodeCount() {
        int count = 0;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0 || MyClusteringConfig.selectedClusterColor != null && (MyClusteringConfig.selectedClusterColor != n.clusteringColor)) continue;
            if (MySequentialGraphVars.g.getPredecessorCount(n) > 0 && MySequentialGraphVars.g.getSuccessorCount(n) == 0) {
                count++;
            }
        }
        return count;
    }

    public int getGraphNodeCount() {
        int count = 0;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0) continue;;
            count++;
        }
        return count;
    }

    public int getClusteredGraphNodeCount() {
        int count = 0;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (MyClusteringConfig.selectedClusterColor != null && (MyClusteringConfig.selectedClusterColor != n.clusteringColor)) {
                continue;
            } else if (n.getCurrentValue() == 0) {
                continue;
            }
            count++;
        }
        return count;
    }

    public static int getTotalMultiNodeEdges() {
        Set<MyNode> nodes = new HashSet<>();
        for (MyNode n : MySequentialGraphVars.getSequentialGraphViewer().multiNodes) {
            nodes.addAll(MySequentialGraphVars.g.getNeighbors(n));
        }
        return nodes.size();
    }

    public static int getTotalMultiNodeNeighbors() {
        Set<MyNode> nodes = new HashSet<>();
        for (MyNode n : MySequentialGraphVars.getSequentialGraphViewer().multiNodes) {
            nodes.addAll(MySequentialGraphVars.g.getNeighbors(n));
        }
        return nodes.size();
    }

    public int getGraphEdgeCount() {
        int count = 0;
        Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
        for (MyEdge e : edges) {
            if (e.getCurrentValue() == 0 || e.getSource().getCurrentValue() == 0 || e.getDest().getCurrentValue() == 0) continue;;
            count++;
        }
        return count;
    }

    public int getClusteredGraphEdgeCount() {
        int count = 0;
        Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
        for (MyEdge e : edges) {
            if (MyClusteringConfig.selectedClusterColor != null && (e.getSource().clusteringColor != MyClusteringConfig.selectedClusterColor ||
                e.getDest().clusteringColor != MyClusteringConfig.selectedClusterColor)) continue;
            if (e.getCurrentValue() == 0 || e.getSource().getCurrentValue() == 0 || e.getDest().getCurrentValue() == 0) continue;
            count++;
        }
        return count;
    }

}


