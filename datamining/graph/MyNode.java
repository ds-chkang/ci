package datamining.graph;

import datamining.utils.system.MyVars;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MyNode
implements Serializable, Cloneable, Comparable<MyNode> {

    protected int durationCount;
    protected double avgReachTime;
    protected double avgDuration;
    protected double maxDuration;
    protected int unreachableNodeCount;
    protected Ellipse2D.Double ellipse2D = null;
    protected String name;
    public int inContribution;
    public int outContribution;
    public long uniqueContribution;
    public long contribution;
    protected long totalPatternCount;
    protected long totalReachTime;
    protected long duration;
    protected int endNodeCount;
    protected int openNodeCount;
    protected int directRecurrenceCount;
    protected float value;
    protected double avgRecurrenceLength;
    protected float originalValue;
    protected double betweeness;
    protected double closeness;
    protected double eignevector;
    protected double pageRankScore;
    private Map<String, Integer> variableStrengthMap = new HashMap<>();
    public Map<Integer, Integer> propgationMap = new HashMap<>();
    public Map<Integer, MyNodeDepthInfo> nodeDepthInfoMap;
    public Map<String, Float> nodeNumericValueMap = new HashMap<>();
    public Map<String, String> nodeCategoryValueMap = new HashMap<>();
    public Map<String, String> nodeLabelMap = new HashMap<>();
    public Map<Integer, Long> pathLengthByDepthMap;
    protected double minDuration = 10000000000D;
    protected double avgShortestPathLen = 0;

    public MyNode() {}
    public void setAverageRecurrenceLength(double avgRecurrenceLength) {this.avgRecurrenceLength = avgRecurrenceLength;}
    public void setAverageShortestPathLength(double avgShortestPathLen) {this.avgShortestPathLen = avgShortestPathLen;}
    public double getAverageShortestPathLength() { return this.avgShortestPathLen; }
    public double getAverageRecurrenceLength() { return this.avgRecurrenceLength; }
    public void setUnreachableNodeCount(int unreachableNodeCount) {this.unreachableNodeCount += unreachableNodeCount;}
    public int getUnreachableNodeCount() {return this.unreachableNodeCount;}
    public void setNodeSize(Ellipse2D.Double ellipse2D) {
        this.ellipse2D = ellipse2D;
    }
    public Ellipse2D.Double getNodeSize() {
        return this.ellipse2D;
    }
    public MyNode(String name) { this.name = name; }
    public MyNode(String name, long contribution, long uniqueContribution) {
        this.name = name;
        this.contribution = contribution;
        this.uniqueContribution = uniqueContribution;
        this.value = contribution;
    }
    public MyNode(String name, long uniqueContribution, long contribution, long totalPatternCount) {
        this.name = name;
        this.uniqueContribution = uniqueContribution;
        this.contribution = contribution;
        this.totalPatternCount = totalPatternCount;
        this.value = contribution;
    }
    public void setVariableNodeStrength(String variableNode) {
        if (this.variableStrengthMap.containsKey(variableNode)) {
            this.variableStrengthMap.put(variableNode, this.variableStrengthMap.get(variableNode));
        } else {
            this.variableStrengthMap.put(variableNode, 1);
        }
    }
    public Map<String, Integer> getVariableStrengthMap() {return this.variableStrengthMap;}
    public int getVariableStrength(String variableNode) {
        return this.variableStrengthMap.get(variableNode);
    }
    public int getTotalVariableStrength() {
        int totalStrength = 0;
        for (Integer strength : this.variableStrengthMap.values()) {
            totalStrength += strength;
        }
        return totalStrength;
    }
    public void setPageRankScore(double pageRankScore) { this.pageRankScore = pageRankScore; }
    public double getPageRankScore() { return this.pageRankScore; }
    public void setNodeLabel(String nodeLabel, String value) {
        if (!this.nodeLabelMap.containsKey(nodeLabel)) {this.nodeLabelMap.put(nodeLabel, value);}
    }
    public void setNodeSumValue(String variable, float value) {
        if (this.nodeNumericValueMap.containsKey(variable)) {this.nodeNumericValueMap.put(variable, this.nodeNumericValueMap.get(variable)+value);
        } else {this.nodeNumericValueMap.put(variable, value);}
    }
    public void setNodeAverageValue(String variable, float value) {
        if (this.nodeNumericValueMap.containsKey(variable)) {
            float exAverageValue = this.nodeNumericValueMap.get(variable);
            this.nodeNumericValueMap.put(variable, (exAverageValue+value)/2);
        } else {this.nodeNumericValueMap.put(variable, value);}
    }
    public void setNodeDistinctValue(String variable, float value) {
        if (!this.nodeNumericValueMap.containsKey(variable)) {this.nodeNumericValueMap.put(variable, value);}
    }

    public void setNodeDistinctValue(String variable, String value) {
        if (!this.nodeCategoryValueMap.containsKey(variable)) {
            this.nodeCategoryValueMap.put(variable, value);
        }
    }

    public Map<Integer, MyNodeDepthInfo> getNodeDepthInfoMap() {return this.nodeDepthInfoMap;}
    public void setInContribution(int inContribution) {this.inContribution += inContribution;}
    public int getInContribution() { return this.inContribution; }
    public void setOutContribution(int outContribution) {this.outContribution += outContribution;}
    public int getOutContribution() { return this.outContribution; }
    public long getMaxOutContribution() {
        long max = 0L;
        Collection<MyEdge> edges = MyVars.g.getOutEdges(this);
        if (edges != null) {
            for (MyEdge e : edges) {
                if (max < e.getContribution()) {
                    max = e.getContribution();
                }
            }
        }
        return max;
    }
    public long getMaxInContribution() {
        long max = 0L;
        Collection<MyEdge> edges = MyVars.g.getInEdges(this);
        if (edges != null) {
            for (MyEdge e : edges) {
                if (max < e.getContribution()) {
                    max = e.getContribution();
                }
            }
        }
        return max;
    }
    public void setName(String name) { this.name = name; }
    public void setDirectRecurrenceCount(int directRecurrenceCount) {
        this.directRecurrenceCount += directRecurrenceCount;
    }
    public int getDirectRecurrenceCount() {return this.directRecurrenceCount;}
    public long getUniqueContribution() { return this.uniqueContribution; }
    public String getName() { return this.name; }
    public long getContribution() {return this.contribution;}
    public long getTotalReachTime() {return this.totalReachTime;}
    public void setTotalReachTime(long time) {
        this.totalReachTime += time;
    }
    public void setAverageReachTime(double avgReachTime) {
        this.avgReachTime = avgReachTime;
    }
    public void setDuration(long duration) {this.duration = duration;}
    public double getAverageInContribution() {
        if (MyVars.g.getPredecessorCount(this) == 0) {
            return 0.00D;
        }
        return (double)this.inContribution/MyVars.g.getPredecessorCount(this);
    }
    public double getAverageOutContribution() {
        if (MyVars.g.getSuccessorCount(this) == 0) {
            return 0.00D;
        }
        return (double)this.outContribution/MyVars.g.getSuccessorCount(this);
    }
    public long getMaximumDuration() {return (long)this.maxDuration;}
    public long getMinimumDuration() {
        long minDuration = (long)this.minDuration;
        if (minDuration == 10000000000L) {
            return 0L;
        }
        else return minDuration;
    }
    public void setBetweeness(double betweeness) {this.betweeness = betweeness;}
    public void setCloseness(double closeness) {
        if (Double.isNaN(closeness)) {this.closeness = 0.00D;
        } else {this.closeness = closeness;}
    }
    public void setEignevector(double eignevector) {this.eignevector = eignevector;}
    public double getBetweeness() { return this.betweeness; }
    public double getCloseness() { return this.closeness; }
    public double getEignevector() { return this.eignevector; }
    public long getDuration() {return this.duration;}
    public void setEndNodeCount(int endNodeCount) {
        this.endNodeCount += endNodeCount;
    }
    public int getEndNodeCount() {return this.endNodeCount;}
    public void setOpenNodeCount(int openNodeCount) {this.openNodeCount += openNodeCount;}
    public int getOpenNodeCount() { return this.openNodeCount; }
    public void setContribution(long contribution) {
        if (this.name.contains("x")) return;
        this.contribution = contribution;
    }
    public void updateContribution(long contribution) {
        this.contribution += contribution;
    }
    public void setCurrentValue(float value) { this.value = value;}
    public void setCurrentValue(long value) {this.value = value;}
    public double getAverageDuration() {
        if (this.durationCount == 0) {return 0.00D;}
        return (double)this.duration/this.durationCount;
    }
    public double getAverageReachTime() {
        long totalReachTime = 0L;
        for (Integer depth : this.nodeDepthInfoMap.keySet()) {
            totalReachTime += this.nodeDepthInfoMap.get(depth).getReachTime();
        }
        return (totalReachTime == 0 ? 0.00D : (double)totalReachTime/this.nodeDepthInfoMap.size());
    }
    public int getPredecessorCount() {
        return MyVars.g.getPredecessorCount(MyVars.g.vRefs.get(this.getName()));
    }
    public int getSuccessorCount() {
        return MyVars.g.getSuccessorCount(MyVars.g.vRefs.get(this.getName()));
    }
    public MyNodeDepthInfo getNodeDepthInfo(int depth) {
        if (this.nodeDepthInfoMap.containsKey(depth)) {
            return this.nodeDepthInfoMap.get(depth);
        } else {
            return null;
        }
    }
    public void setNodeDepthInfoMap(Map<Integer, MyNodeDepthInfo> nodeDepthInfoMap) {this.nodeDepthInfoMap = nodeDepthInfoMap;}
    public void setPropagation(int propagation) {
        if (this.propgationMap.containsKey(propagation)) {
            int propagationCount = this.propgationMap.get(propagation) + 1;
            this.propgationMap.put(propagation, propagationCount);
        } else {this.propgationMap.put(propagation, 1);}
    }
    public int getPropagation(int propagation) { return this.propgationMap.get(propagation); }
    public MyNode copyNode() {
        MyNode copyNode = new MyNode(this.name);
        copyNode.contribution = this.contribution;
        copyNode.uniqueContribution = this.uniqueContribution;
        copyNode.totalPatternCount = this.totalPatternCount;
        copyNode.value = this.value;
        copyNode.totalReachTime = this.totalReachTime;
        copyNode.duration = this.duration;
        copyNode.avgReachTime = this.getAverageReachTime();
        copyNode.avgDuration = this.getAverageDuration();
        copyNode.endNodeCount = this.endNodeCount;
        copyNode.nodeLabelMap = this.nodeLabelMap;
        copyNode.nodeDepthInfoMap = this.nodeDepthInfoMap;
        copyNode.nodeNumericValueMap = this.nodeNumericValueMap;
        return copyNode;
    }
    public float getCurrentValue() {return this.value;}
    public void setOriginalValue(float value) {this.originalValue = value;}
    public float getOriginalValue() { return this.originalValue; }
    @Override
    public int compareTo(MyNode n) {
        if (MyVars.isTimeOn) {
            switch (MyVars.nodeOrderByComboBoxIdx) {
                case 0: return (int) (this.getContribution() - n.getContribution());
                case 1: return (this.getInContribution() - n.getInContribution());
                case 2: return (this.getOutContribution() - n.getOutContribution());
                case 3: return (int) (this.getUniqueContribution() - n.getUniqueContribution());
                case 4: return (this.getOpenNodeCount() - n.getOpenNodeCount());
                case 5: return (this.getEndNodeCount() - n.getEndNodeCount());
                case 6: return (this.getSuccessorCount() - n.getSuccessorCount());
                case 7: return (this.getPredecessorCount() - n.getPredecessorCount());
                case 8: return (Math.abs(this.getSuccessorCount() - this.getPredecessorCount()) - Math.abs(n.getSuccessorCount() - n.getPredecessorCount()));
                case 9: return (this.getDirectRecurrenceCount() - n.getDirectRecurrenceCount());
                case 10: return (int) ((this.getBetweeness() - n.getBetweeness())*1000);
                case 11: return (int) ((this.getCloseness() - n.getCloseness())*1000);
                case 12: return (int) ((this.getEignevector() - n.getEignevector())*1000);
                case 13: return (int) (this.getTotalReachTime() - n.getTotalReachTime());
                case 14: return (int) (this.getDuration() - n.getDuration());
                default: return (int) (this.getContribution() - n.getContribution());
            }
        } else {
            switch (MyVars.nodeOrderByComboBoxIdx) {
                case 0: return (int) (this.getContribution() - n.getContribution());
                case 1: return (this.getInContribution() - n.getInContribution());
                case 2: return (this.getOutContribution() - n.getOutContribution());
                case 3: return (int) (this.getUniqueContribution() - n.getUniqueContribution());
                case 4: return (this.getOpenNodeCount() - n.getOpenNodeCount());
                case 5: return (this.getEndNodeCount() - n.getEndNodeCount());
                case 6: return (this.getSuccessorCount() - n.getSuccessorCount());
                case 7: return (this.getPredecessorCount() - n.getPredecessorCount());
                case 8: return (Math.abs(this.getSuccessorCount() - this.getPredecessorCount()) - Math.abs(n.getSuccessorCount() - n.getPredecessorCount()));
                case 9: return (this.getDirectRecurrenceCount() - n.getDirectRecurrenceCount());
                case 10: return (int) ((this.getBetweeness() - n.getBetweeness())*1000);
                case 11: return (int) ((this.getCloseness() - n.getCloseness())*1000);
                case 12: return (int) ((this.getEignevector() - n.getEignevector())*1000);
                default: return (int) (this.getContribution() - n.getContribution());
            }
        }
    }

    public String toString() {
        return name;
    }
}