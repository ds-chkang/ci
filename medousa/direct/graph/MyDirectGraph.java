package medousa.direct.graph;

import medousa.direct.graph.common.MyDirectGraphNodeBetweennessCentrality;
import medousa.direct.graph.common.MyDirectGraphDirectGraphClosenessCentrality;
import medousa.direct.graph.common.MyDirectGraphDirectGraphNodeEigenvectorCentrality;
import medousa.direct.utils.MyDirectGraphMathUtil;
import medousa.direct.utils.MyDirectGraphVars;
import edu.uci.ics.jung.algorithms.scoring.PageRank;
import edu.uci.ics.jung.graph.util.EdgeType;

import java.io.Serializable;
import java.util.*;

public class MyDirectGraph<V, E>
extends MyDirectSparseMultigraph<V, E>
implements Serializable {

    private long totalNodeContribution = 0;
    private long totalEdgeContribution = 0;
    private int maxNodeContribution = 0;
    private int minNodeContribution = 1000000000;
    private int maxEdgeContribution = 0;
    private int minEdgeContribution = 1000000000;
    public float maxEdgeValue = 0.00f;
    private float minEdgeValue = 0.00f;
    public float maxNodeValue = 0.00f;
    public float minNodeValue = 0.00f;
    public Map<String, MyDirectEdge> directMarkovChainEdgeRefMap = new HashMap<>();

    public MyDirectGraph(EdgeType directed) {
        super(directed);
    }

    public int getIslandNodes() {
        int total = 0;
        Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
        for (MyDirectNode n : nodes) {
            if (MyDirectGraphVars.directGraph.getPredecessorCount(n) == 0 && MyDirectGraphVars.directGraph.getSuccessorCount(n) == 0) {
                total++;
            }
        }
        return total;
    }

    public long getGraphEdgeCount() {
        long total = 0L;
        Collection<MyDirectEdge> edges = MyDirectGraphVars.directGraph.getEdges();
        for (MyDirectEdge e : edges) {
            if (e.getCurrentValue() == 0) continue;
            if (e.getSource().clusteringColor == e.getDest().clusteringColor && e.getSource().getCurrentValue() > 0 && e.getDest().getCurrentValue() > 0) {
                total++;
            }
        }
        return total;
    }

    public long getGraphNodeCount() {
        long total = 0L;
        Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
        for (MyDirectNode n : nodes) {
            if (n.getCurrentValue() > 0) {
                total++;
            }
        }
        return total;
    }

    public long getNonZeroNodes() {
        long total = 0;
        Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
        for (MyDirectNode n : nodes) {
            if (n.getCurrentValue() > 0) {
                total++;
            }
        }
        return total;
    }

    public long getNonZeroEdges() {
        long total = 0;
        Collection<MyDirectEdge> edges = MyDirectGraphVars.directGraph.getEdges();
        for (MyDirectEdge e : edges) {
            if (e.getCurrentValue() > 0) {
                total++;
            }
        }
        return total;
    }

    public double getNodeValueStandardDeviation() {
        Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
        double sum = 0.00d;
        int numNodes = 0;
        double std = 0.00D;
        if (nodes != null) {
            for (MyDirectNode n : nodes) {
                if (n.getCurrentValue() <= 0) {
                    continue;
                }
                numNodes++;
                sum += n.getCurrentValue();
            }
            double mean = sum / numNodes;
            for (MyDirectNode n : nodes) {
                std += Math.pow(n.getCurrentValue() - mean, 2);
            }
        }
        return (sum == 0.00D ? 0.00D : Math.sqrt(std /numNodes));
    }

    public double getEdgValueStandardDeviation() {
        int numEdges = 0;
        Collection<MyDirectEdge> edges = MyDirectGraphVars.directGraph.getEdges();
        double sum = 0.00d;
        double std = 0.00D;
        if (edges != null) {
            for (MyDirectEdge e : edges) {
                if (e.getCurrentValue() <= 0) {
                    continue;
                }
                numEdges++;
                sum += e.getCurrentValue();
            }
            double mean = sum / numEdges;
            for (MyDirectEdge e : edges) {
                std += Math.pow(e.getCurrentValue() - mean, 2);
            }
        }
        return (sum == 0.00D ? 0.00D : Math.sqrt(std / numEdges));
    }

    public double getInEdgValueStandardDeviationForSelectedNode() {
        int numEdges = 0;
        Collection<MyDirectEdge> edges = MyDirectGraphVars.directGraph.getInEdges(MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode);
        double sum = 0.00d;
        double std = 0.00D;
        if (edges != null) {
            for (MyDirectEdge e : edges) {
                if (e.getCurrentValue() <= 0) {
                    continue;
                }
                numEdges++;
                sum += e.getCurrentValue();
            }
            double mean = sum / numEdges;
            for (MyDirectEdge e : edges) {
                std += Math.pow(e.getCurrentValue() - mean, 2);
            }
        }
        return (sum == 0.00D ? 0.00D : Math.sqrt(std / numEdges));
    }

    public double getOutEdgValueStandardDeviationForSelectedNode() {
        int numEdges = 0;
        Collection<MyDirectEdge> edges = MyDirectGraphVars.directGraph.getOutEdges(MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode);
        double sum = 0.00d;
        double std = 0.00D;
        if (edges != null) {
            for (MyDirectEdge e : edges) {
                if (e.getCurrentValue() <= 0) {
                    continue;
                }
                numEdges++;
                sum += e.getCurrentValue();
            }
            double mean = sum / numEdges;
            for (MyDirectEdge e : edges) {
                std += Math.pow(e.getCurrentValue() - mean, 2);
            }
        }
        return (sum == 0.00D ? 0.00D : Math.sqrt(std / numEdges));
    }

    public double getOutEdgValueStandardDeviationForSelectedMultiNode() {
        int numEdges = 0;
        double sum = 0.00d;
        double std = 0.00D;
        for (MyDirectNode n : MyDirectGraphVars.getDirectGraphViewer().multiNodes) {
            Collection<MyDirectEdge> edges = MyDirectGraphVars.directGraph.getOutEdges(n);
            if (edges != null) {
                for (MyDirectEdge e : edges) {
                    if (e.getCurrentValue() <= 0) continue;
                    numEdges++;
                    sum += e.getCurrentValue();
                }
                double mean = sum / numEdges;
                for (MyDirectEdge e : edges) {
                    std += Math.pow(e.getCurrentValue() - mean, 2);
                }
            }
        }
        return (sum == 0.00D ? 0.00D : Math.sqrt(std / numEdges));
    }

    public double getInEdgValueStandardDeviationForSelectedMultiNode() {
        int numEdges = 0;
        double sum = 0.00d;
        double std = 0.00D;
        for (MyDirectNode n : MyDirectGraphVars.getDirectGraphViewer().multiNodes) {
            Collection<MyDirectEdge> edges = MyDirectGraphVars.directGraph.getInEdges(n);
            if (edges != null) {
                for (MyDirectEdge e : edges) {
                    if (e.getCurrentValue() <= 0) continue;
                    numEdges++;
                    sum += e.getCurrentValue();
                }
                double mean = sum / numEdges;
                for (MyDirectEdge e : edges) {
                    std += Math.pow(e.getCurrentValue() - mean, 2);
                }
            }
        }
        return (sum == 0.00D ? 0.00D : Math.sqrt(std / numEdges));
    }

    public void setMaxNodeContribution() {
        Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
        if (nodes != null) {
            for (MyDirectNode n : nodes) {
                if (maxNodeContribution < n.getContribution()) {
                    maxNodeContribution = n.getContribution();
                }
            }
        }
    }

    public void setMinNodeContribution() {
        Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
        if (nodes != null) {
            for (MyDirectNode n : nodes) {
                if (this.minNodeContribution > n.getContribution()) {
                    this.minNodeContribution = n.getContribution();
                }
            }
        }
    }

    public double getAverageInEdgeValueForSelectedMultiNodes() {
        double total = 0;
        long count = 0;
        for (MyDirectNode n : MyDirectGraphVars.getDirectGraphViewer().multiNodes) {
            Collection<MyDirectEdge> edges = MyDirectGraphVars.directGraph.getInEdges(n);
            if (edges != null) {
                for (MyDirectEdge e : edges) {
                    if (e.getCurrentValue() >= 0) {
                        total += e.getCurrentValue();
                        count++;
                    }
                }
            }
        }
        return (total == 0 ? 0.00D : total/count);
    }

    public double getMaxInEdgeValueForSelectedMultiNodes() {
        long max = 0;
        for (MyDirectNode n : MyDirectGraphVars.getDirectGraphViewer().multiNodes) {
            Collection<MyDirectEdge> edges = MyDirectGraphVars.directGraph.getInEdges(n);
            if (edges != null) {
                for (MyDirectEdge e : edges) {
                    if (e.getCurrentValue() >= 0) {
                        if (e.getCurrentValue() > max) {
                            max = (long)e.getCurrentValue();
                        }
                    }
                }
            }
        }
        return max;
    }

    public double getMaxOutEdgeValueForSelectedMultiNodes() {
        long max = 0;
        for (MyDirectNode n : MyDirectGraphVars.getDirectGraphViewer().multiNodes) {
            Collection<MyDirectEdge> edges = MyDirectGraphVars.directGraph.getOutEdges(n);
            if (edges != null) {
                for (MyDirectEdge e : edges) {
                    if (e.getCurrentValue() >= 0) {
                        if (e.getCurrentValue() > max) {
                            max = (long)e.getCurrentValue();
                        }
                    }
                }
            }
        }
        return max;
    }

    public double getMinInEdgeValueForSelectedMultiNodes() {
        long min = 1000000000000000L;
        for (MyDirectNode n : MyDirectGraphVars.getDirectGraphViewer().multiNodes) {
            Collection<MyDirectEdge> edges = MyDirectGraphVars.directGraph.getInEdges(n);
            if (edges != null) {
                for (MyDirectEdge e : edges) {
                    if (e.getCurrentValue() >= 0) {
                        if (e.getCurrentValue() < min) {
                            min = (long)e.getCurrentValue();
                        }
                    }
                }
            }
        }
        return (min == 1000000000000000L ? 0 : min);
    }

    public double getMinOutEdgeValueForSelectedMultiNodes() {
        long min = 1000000000000000L;
        for (MyDirectNode n : MyDirectGraphVars.getDirectGraphViewer().multiNodes) {
            Collection<MyDirectEdge> edges = MyDirectGraphVars.directGraph.getOutEdges(n);
            if (edges != null) {
                for (MyDirectEdge e : edges) {
                    if (e.getCurrentValue() >= 0) {
                        if (e.getCurrentValue() < min) {
                            min = (long)e.getCurrentValue();
                        }
                    }
                }
            }
        }
        return (min == 1000000000000000L ? 0 : min);
    }

    public double getAverageOutEdgeValueForSelectedMultiNodes() {
        double total = 0;
        long count = 0;
        for (MyDirectNode n : MyDirectGraphVars.getDirectGraphViewer().multiNodes) {
            Collection<MyDirectEdge> edges = MyDirectGraphVars.directGraph.getOutEdges(n);
            if (edges != null) {
                for (MyDirectEdge e : edges) {
                    if (e.getCurrentValue() >= 0) {
                        total += e.getCurrentValue();
                        count++;
                    }
                }
            }
        }
        return (total == 0 ? 0.00D : total/count);
    }

    public void setTotalNodeContribution() {
        Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
        if (nodes != null) {
            for (MyDirectNode n : nodes) {
                this.totalNodeContribution += n.getContribution();
            }
        }
    }

    public int getSharedPredecessorCount() {
        return MyDirectGraphVars.getDirectGraphViewer().multiNodeSharedPredecessorSet.size();
    }

    public int getSharedSuccessorCount() {
        return MyDirectGraphVars.getDirectGraphViewer().multiNodeSharedSuccessorSet.size();
    }

    public void setTotalEdgeContribution() {
        Collection<MyDirectEdge> edges = MyDirectGraphVars.directGraph.getEdges();
        if (edges != null) {
            for (MyDirectEdge edge : edges) {
                this.totalEdgeContribution += edge.getContribution();
            }
        }
    }

    public void setMaxEdgeContribution() {
        Collection<MyDirectEdge> edges = MyDirectGraphVars.directGraph.getEdges();
        if (edges != null) {
            for (MyDirectEdge edge : edges) {
                if (edge.getContribution() > this.maxEdgeContribution) {
                    this.maxEdgeContribution = edge.getContribution();
                }
            }
        }
    }

    public float getAverageEdgeValue() {
        Collection<MyDirectEdge> edges = MyDirectGraphVars.directGraph.getEdges();
        float total = 0f;
        int cnt = 0;
        if (edges != null) {
            for (MyDirectEdge edge : edges) {
                if (edge.getCurrentValue() > 0) {
                    total += edge.getCurrentValue();
                    cnt++;
                }
            }
        }
        return (cnt == 0 ? 0.00f : (total/cnt));
    }

    public float getAverageInEdgeValueForSelectedNode() {
        Collection<MyDirectEdge> edges = MyDirectGraphVars.directGraph.getInEdges(MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode);
        float total = 0f;
        if (edges != null) {
            for (MyDirectEdge edge : edges) {
                if (edge.getCurrentValue() > 0) {
                    total += edge.getCurrentValue();
                }
            }
        }
        return (total == 0 ? 0.00f : total/edges.size());
    }

    public float getAverageOutEdgeValueForSelectedNode() {
        Collection<MyDirectEdge> edges = MyDirectGraphVars.directGraph.getOutEdges(MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode);
        float total = 0f;
        if (edges != null) {
            for (MyDirectEdge edge : edges) {
                if (edge.getCurrentValue() > 0) {
                    total += edge.getCurrentValue();
                }
            }
        }
        return (total == 0 ? 0.00f : total/edges.size());
    }

    public void setMinEdgeContribution() {
        Collection<MyDirectEdge> edges = MyDirectGraphVars.directGraph.getEdges();
        if (edges != null) {
            for (MyDirectEdge edge : edges) {
                if (edge.getContribution() < this.minEdgeContribution) {
                    this.minEdgeContribution = edge.getContribution();
                }
            }
        }
    }

    public float getMinEdgeValue() {
        Collection<MyDirectEdge> edges = MyDirectGraphVars.directGraph.getEdges();
        float min = 100000000000.0f;
        if (edges != null) {
            for (MyDirectEdge edge : edges) {
                if (edge.getContribution() < min) {
                    min = edge.getContribution();
                }
            }
            this.minEdgeValue = min;
        }
        return this.minEdgeValue;
    }

    public int getOutContributionByNode(MyDirectNode node) {
        int outContribution = 0;
        Collection<MyDirectEdge> edges = MyDirectGraphVars.directGraph.getEdges();
        if (edges != null) {
            for (MyDirectEdge edge : edges) {
                if (edge.getSource().getName().equals(node.getName())) {
                    outContribution += edge.getContribution();
                }
            }
        }
        return outContribution;
    }

    public int getInContributionByNode(MyDirectNode node) {
        int contribution = 0;
        Collection<MyDirectNode> edges = MyDirectGraphVars.directGraph.getVertices();
        for (MyDirectNode n : edges) {
            if (n.getName().equals(node.getName())) {contribution += n.getContribution();}
        }
        return contribution;
    }

    public int getClosenessByNode(MyDirectNode node) {
        int closeness = 0;
        Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
        for (MyDirectNode n : nodes) {
            if (n.getName().equals(node.getName())) {closeness += n.getCloseness();}
        }
        return closeness;
    }

    public int getBetweenessByNode(MyDirectNode node) {
        int closeness = 0;
        Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
        for (MyDirectNode n : nodes) {
            if (n.getName().equals(node.getName())) {closeness += n.getBetweeness();}
        }
        return closeness;
    }

    public float getAverageSuccessors() {
        float totalSuccessors = 0;
        int nodeCnt = 0;
        Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
        for (MyDirectNode n : nodes) {
            if (n.getCurrentValue() == 0) continue;
            int sucCnt = MyDirectGraphVars.directGraph.getSuccessorCount(n);
            if (sucCnt > 0) {
                nodeCnt++;
                totalSuccessors += sucCnt;
            }
        }
        return (nodeCnt == 0 ? 0.00f : (totalSuccessors/nodeCnt));
    }

    public int getMaxPredecessorCount() {
        int max = 0;
        Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
        for (MyDirectNode n : nodes) {
            if (n.getCurrentValue() == 0) continue;
            int cnt = MyDirectGraphVars.directGraph.getPredecessorCount(n);
            if (cnt > 0) {
                cnt++;
                if (max < cnt) {
                    max = cnt;
                }
            }
        }
        return max;
    }

    public int getMaxSuccessorCount() {
        int max = 0;
        Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
        if (nodes != null) {
            for (MyDirectNode n : nodes) {
                if (n.getCurrentValue() == 0) continue;
                int cnt = MyDirectGraphVars.directGraph.getSuccessorCount(n);
                if (cnt > 0) {
                    cnt++;
                    if (max < cnt) {
                        max = cnt;
                    }
                }
            }
        }
        return max;
    }

    public float getAveragePredecessors() {
        float totalPredecessors = 0;
        int nodeCnt = 0;
        Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
        if (nodes != null) {
            for (MyDirectNode n : nodes) {
                if (n.getCurrentValue() == 0) continue;
                int preCnt = MyDirectGraphVars.directGraph.getPredecessorCount(n);
                if (preCnt > 0) {
                    nodeCnt++;
                    totalPredecessors += preCnt;
                }
            }
        }
        return (nodeCnt == 0 ? 0.00f : (totalPredecessors/nodeCnt));
    }

    public int getRedNodeCount() {
        int redCnt = 0;
        Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
        if (nodes != null) {
            for (MyDirectNode n : nodes) {
                if (n.getCurrentValue() == 0) continue;
                if (MyDirectGraphVars.directGraph.getSuccessorCount(n) > 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) == 0) {
                    redCnt++;
                }
            }
        }
        return redCnt;
    }

    public float getMaxInEdgeValueForSelectedNode() {
        float max = 0f;
        Collection<MyDirectEdge> edges = MyDirectGraphVars.directGraph.getInEdges(MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode);
        if (edges != null) {
            for (MyDirectEdge e : edges) {
                if (max < e.getCurrentValue()) {
                    max = e.getCurrentValue();
                }
            }
        }
        return (max <= 0 ? 0.00f : max);
    }

    public float getMaxOutEdgeValueForSelectedNode() {
        float max = 0f;
        Collection<MyDirectEdge> edges = MyDirectGraphVars.directGraph.getOutEdges(MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode);
        if (edges != null) {
            for (MyDirectEdge e : edges) {
                if (max < e.getCurrentValue()) {
                    max = e.getCurrentValue();
                }
            }
        }
        return (max <= 0 ? 0.00f : max);
    }

    public float getMinInEdgeValueForSelectedNode() {
        float min = 10000000000000f;
        Collection<MyDirectEdge> edges = MyDirectGraphVars.directGraph.getInEdges(MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode);
        if (edges != null) {
            for (MyDirectEdge e : edges) {
                if (min > e.getCurrentValue()) {
                    min = e.getCurrentValue();
                }
            }
        }
        return (min == 10000000000000f || min <= 0f ? 0.00f : min);
    }

    public float getMinOutEdgeValueForSelectedNode() {
        float min = 10000000000000f;
        Collection<MyDirectEdge> edges = MyDirectGraphVars.directGraph.getOutEdges(MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode);
        if (edges != null) {
            for (MyDirectEdge e : edges) {
                if (min > e.getCurrentValue()) {
                    min = e.getCurrentValue();
                }
            }
        }
        return (min == 10000000000000f || min <= 0f ? 0.00f : min);
    }

    public int getRedNodeCountForSelectedNode() {
        Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getSuccessors(MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode);
        int redCnt = 0;
        if (nodes != null) {
            for (MyDirectNode n : nodes) {
                if (n.getCurrentValue() == 0) continue;
                if (MyDirectGraphVars.directGraph.getSuccessorCount(n) > 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) == 0) {
                    redCnt++;
                }
            }
        }
        return redCnt;
    }

    public long getPredecessorCountForSelectedNode() {
        Collection<MyDirectNode> predecessors = MyDirectGraphVars.directGraph.getPredecessors(MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode);
        return (predecessors == null ? 0 : predecessors.size());
    }

    public long getSuccessorCountForSelectedNode() {
        Collection<MyDirectNode> successors = MyDirectGraphVars.directGraph.getSuccessors(MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode);
        return (successors == null ? 0 : successors.size());
    }

    public int getBlueNodeCount() {
        Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
        int blueCnt = 0;
        if (nodes != null) {
            for (MyDirectNode n : nodes) {
                if (n.getCurrentValue() == 0) continue;
                if (MyDirectGraphVars.directGraph.getSuccessorCount(n) == 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) > 0) {
                    blueCnt++;
                }
            }
        }
        return blueCnt;
    }

    public int getGreenNodeCount() {
        Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
        int greenCnt = 0;
        if (nodes != null) {
            for (MyDirectNode n : nodes) {
                if (n.getCurrentValue() == 0) continue;
                if (MyDirectGraphVars.directGraph.getSuccessorCount(n) > 0 && MyDirectGraphVars.directGraph.getPredecessorCount(n) > 0) {
                    greenCnt++;
                }
            }
        }
        return greenCnt;
    }

    public void setMaxNodeValue() {
        Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
        float max = 0.00f;
        if (nodes != null) {
            for (MyDirectNode n : nodes) {
                if (n.getCurrentValue() > max) {
                    max = n.getCurrentValue();
                }
            }
        }
        this.maxNodeValue = max;
    }

    public float getMinNodeValue() {
        Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
        float min = 100000000000.0f;
        if (nodes != null) {
            for (MyDirectNode n : nodes) {
                if (n.getCurrentValue() > 0 && n.getCurrentValue() < min) {
                    min = n.getCurrentValue();
                }
            }
        }
        this.minNodeValue = min;
        return (this.minNodeValue == 100000000000.0f ? 0f : this.minNodeValue);
    }

    public float getAverageNodeValue() {
        Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
        float total = 0f;
        int cnt = 0;
        if (nodes != null) {
            for (MyDirectNode n : nodes) {
                if (n.getCurrentValue() > 0) {
                    total += n.getCurrentValue();
                    cnt++;
                }
            }
        }
        return (cnt == 0 ? 0.00f : (total/cnt));
    }

    public void setDefaultNodeValues() {
        Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
        if (nodes != null) {
            for (MyDirectNode n : nodes) {
                n.setCurrentValue(n.getContribution());
            }
        }
    }

    public String getTotalInEdgeValue() {
        Collection<MyDirectEdge> edges = MyDirectGraphVars.directGraph.getInEdges(MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode);
        long totalInEdgeValue = 0L;
        if (edges != null) {
            for (MyDirectEdge e : edges) {
                totalInEdgeValue += e.getCurrentValue();
            }
        }
        return (totalInEdgeValue < 0 ? "0" : MyDirectGraphMathUtil.getCommaSeperatedNumber(totalInEdgeValue));
    }

    public String getTotalOutEdgeValue() {
        Collection<MyDirectEdge> edges = MyDirectGraphVars.directGraph.getOutEdges(MyDirectGraphVars.getDirectGraphViewer().selectedSingleNode);
        long totalOutEdgeValue = 0L;
        if (edges != null) {
            for (MyDirectEdge e : edges) {
                totalOutEdgeValue += e.getCurrentValue();
            }
        }
        return (totalOutEdgeValue < 0 ? "0" : MyDirectGraphMathUtil.getCommaSeperatedNumber(totalOutEdgeValue));
    }

    public int getNodeGraphNumber(MyDirectNode n) {
        int i = 0;
        for (; i < MyDirectGraphVars.connectedComponentCountsByGraph.size(); i++) {
            if (MyDirectGraphVars.connectedComponentCountsByGraph.get(i).contains(n)) {
                break;
            }
        }
        return (i+1);
    }

    public double getAverageConnectedNodeCountByGraph() {
        return (double) MyDirectGraphVars.directGraph.getVertexCount()/ MyDirectGraphVars.connectedComponentCountsByGraph.size();

    }

    public String getGraphsWithMinNumberOfNodes() {
        String minGraphs = "";
        long min = 100000000000000L;
        for (int i = 0; i < MyDirectGraphVars.connectedComponentCountsByGraph.size(); i++) {
            if (min == MyDirectGraphVars.connectedComponentCountsByGraph.get(i).size()) {
                if (minGraphs.length() == 0) {
                    minGraphs = "" + (i+1);
                } else {
                    minGraphs = minGraphs + "," + (i+1);
                }
            } else if (min > MyDirectGraphVars.connectedComponentCountsByGraph.get(i).size()) {
                min = MyDirectGraphVars.connectedComponentCountsByGraph.get(i).size();
                minGraphs = "" + (i+1);
            }
        }
        return (min == 100000000000000L ? "0" : "<html><body>" + minGraphs + "<br>[" + MyDirectGraphMathUtil.getCommaSeperatedNumber(minGraphs.split(",").length) + "]</body></html>");
    }

    public long getMaxNumberofNodesAmongGraphs() {
        long max = 0L;
        for (Set<MyDirectNode> nodeSet : MyDirectGraphVars.connectedComponentCountsByGraph) {
            if (max < nodeSet.size()) {
                max = nodeSet.size();
            }
        }
        return max;
    }

    public long getMinNumberofNodesAmongGraphs() {
        long min = 1000000000000000L;
        for (Set<MyDirectNode> nodeSet : MyDirectGraphVars.connectedComponentCountsByGraph) {
            if (min > nodeSet.size()) {
                min = nodeSet.size();
            }
        }
        return (min == 1000000000000000L ? 0 : min);
    }

    public String getGraphsWIthMaxNumberOfNodes() {
        String maxGraphs = "";
        long max = 0L;
        for (int i = 0; i < MyDirectGraphVars.connectedComponentCountsByGraph.size(); i++) {
            if (max == MyDirectGraphVars.connectedComponentCountsByGraph.get(i).size()) {
                if (maxGraphs.length() == 0) {
                    maxGraphs = "" + (i+1);
                } else {
                    maxGraphs = maxGraphs + "," + (i+1);
                }
            } else if (max < MyDirectGraphVars.connectedComponentCountsByGraph.get(i).size()) {
                max = MyDirectGraphVars.connectedComponentCountsByGraph.get(i).size();
                maxGraphs = "" + (i+1);
            }
        }
        return "<html><body>" + maxGraphs + "<br>[" + MyDirectGraphMathUtil.getCommaSeperatedNumber(maxGraphs.split(",").length) + "]</body></html>";
    }

    public void setConnectedNetworkComponentCountByGraph() {
        MyDirectGraphConnectedNetworkComponentFinder.setConnectedComponentsByGraph();
    }

    public float getMaxNodeValue() {return this.maxNodeValue;}

    public long getMinPredecessorCount() {
        long min = 10000000000000L;
        Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
        if (nodes != null) {
            for (MyDirectNode n : nodes) {
                long localMin = MyDirectGraphVars.directGraph.getPredecessorCount(n);
                if (localMin > 0 && localMin < min) {
                    min = localMin;
                }
            }
        }
        return min;
    }

    public long getMinSuccesosrCount() {
        long min = 10000000000000L;
        Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
        if (nodes != null) {
            for (MyDirectNode n : nodes) {
                long localMin = MyDirectGraphVars.directGraph.getSuccessorCount(n);
                if (localMin > 0 && localMin < min) {
                    min = localMin;
                }
            }
        }
        return (min == 10000000000000L ? 0 : min);
    }


    public float getMaxEdgeValue() {return this.maxEdgeValue;}
    public void setNodeBetweenessCentrality() {
        MyDirectGraphNodeBetweennessCentrality betweennessCentrality = new MyDirectGraphNodeBetweennessCentrality(MyDirectGraphVars.directGraph);
        betweennessCentrality.computeBetweenness(MyDirectGraphVars.directGraph);
        Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
        for (MyDirectNode n : nodes) {
            n.setBetweeness(Double.valueOf(MyDirectGraphMathUtil.twoDecimalFormat(betweennessCentrality.getVertexRankScore(n))));
        }
    }

    public void setNodeClosenessCentrality() {
        MyDirectGraphDirectGraphClosenessCentrality closenessCentrality = new MyDirectGraphDirectGraphClosenessCentrality(MyDirectGraphVars.directGraph);
        Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
        for (MyDirectNode n : nodes) {
            double value = closenessCentrality.getVertexScore(n);
            n.setCloseness(Double.valueOf(MyDirectGraphMathUtil.twoDecimalFormat((value == Double.NaN ? 0 : value))));
        }
    }

    public void setNodeEigenVectorCentrality() {
        MyDirectGraphDirectGraphNodeEigenvectorCentrality eigenvectorCentrality = new MyDirectGraphDirectGraphNodeEigenvectorCentrality(MyDirectGraphVars.directGraph);
        eigenvectorCentrality.acceptDisconnectedGraph(true);
        eigenvectorCentrality.evaluate();

        Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
        for(MyDirectNode n : nodes){
            n.setEignevector(((double)eigenvectorCentrality.getVertexScore(n))*1000);
            //System.out.println(MySysUtil.getDecodedNodeName(n.getName()) + " :eigen:"+ eigenvectorCentrality.getVertexScore(n));
        }
    }

    public void setPageRankScore() {
        PageRank<MyDirectNode, MyDirectEdge> ranker = new PageRank<MyDirectNode, MyDirectEdge>(MyDirectGraphVars.directGraph, 0.3);
        ranker.setMaxIterations(1000);
        ranker.evaluate();
        Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
        for (MyDirectNode n : nodes) {
            n.setPageRankScore((int) Math.round(1000 * ranker.getVertexScore(n)));
            //System.out.println(n.getPageRankScore());
        }
    }

    public long getTotalNodeContribution() {return this.totalNodeContribution;}
    public long getTotalEdgeContribution() {return this.totalEdgeContribution;}
}


