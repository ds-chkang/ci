package medousa.sequential.graph;

import medousa.sequential.utils.MySequentialGraphVars;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MyNode
implements Serializable, Cloneable, Comparable<MyNode> {

    protected float value;
    public Color clusteringColor;
    public int unreachableNodeCount;

    public Ellipse2D.Double ellipse2D = null;

    public String name;

    public int inContribution;
    public int outContribution;
    public long uniqueContribution;
    public long contribution;
    public long totalPatternCount;

    public long totalReachTime;
    public long maxReachTime;
    public long minReachTime = 500000000000000L;

    protected double avgReachTime;

    protected int durationCount;
    public long duration;
    protected float maxDuration;
    public long minDuration = 100000000000000L;

    public int endNodeCount;
    public int openNodeCount;

    public int nodeRecurrentCount;
    public float avgRecurrenceLength;
    public long totalRecurrenceTime;
    public float avgRecurrenceTime;

    protected float originalValue;
    protected double betweeness;
    protected double closeness;
    protected double eignevector;
    protected double pageRankScore;
    protected Map<Integer, Integer> recursiveLengthMap = new HashMap<>();
    public float avgRecursiveTime;

    private Map<String, Integer> variableStrengthMap = new HashMap<>();
    public Map<Integer, Integer> propgationMap = new HashMap<>();
    public Map<Integer, MyNodeDepthInfo> nodeDepthInfoMap;
    public Map<String, Float> nodeNumericValueMap = new HashMap<>();
    public Map<String, String> nodeCategoryValueMap = new HashMap<>();
    public Map<String, String> nodeLabelMap = new HashMap<>();
    public Map<Integer, Long> pathLengthByDepthMap;
    public float avgShortestDistance = 0;

    public MyNode() {}
    public void setAverageRecursiveTime(float avgRecursiveTime) {
        this.avgRecursiveTime = avgRecursiveTime;
    }
    public float getAvgRecursiveTime() {return this.avgRecursiveTime;}
    public void setMaxReachTime(long reachTime) {
        if (this.maxReachTime < reachTime) {
            this.maxReachTime = reachTime;
        }
    }
    public long getMaxReachTime() {return this.maxReachTime;}
    public void setMinReachTime(long reachTime) {
        if (reachTime > 0 && this.minReachTime > reachTime) {
            this.minReachTime = reachTime;
        }
    }
    public void setRecursiveLength(int recursiveLength) {
        if (recursiveLengthMap.containsKey(recursiveLength)) {
            recursiveLengthMap.put(recursiveLength, recursiveLengthMap.get(recursiveLength)+1);
        } else {
            recursiveLengthMap.put(recursiveLength, 1);
        }
    }
    public int getMaxRecursiveLength() {
        int max = 0;
        for (int recursiveLength : this.recursiveLengthMap.keySet()) {
            if (recursiveLength > max) {
                max = recursiveLength;
            }
        }
        return max;
    }

    public void setContribution(int contribution) {
        this.contribution += contribution;
    }
    public int getMinRecursiveLength() {
        int min = 1000000000;
        for (int recursiveLength : this.recursiveLengthMap.keySet()) {
            if (recursiveLength < min) {
                min = recursiveLength;
            }
        }
        return (min == 1000000000 ? 0 : min);
    }
    public long getTotalRecursiveLength() {
        long total = 0;
        for (int recursiveLength : this.recursiveLengthMap.keySet()) {
            total += (this.recursiveLengthMap.get(recursiveLength)*recursiveLength);
        }
        return total;
    }
    public float getAverageRecursiveLength() {
        long total = 0;
        int i = 0;
        for (int recursiveLength : this.recursiveLengthMap.keySet()) {
            total += (this.recursiveLengthMap.get(recursiveLength)*recursiveLength);
            i += this.recursiveLengthMap.get(recursiveLength);
        }
        return (total == 0 ? 0f : (float) total/i);
    }
    public void setTotalNodeRecurrenceTime(long time) {this.totalRecurrenceTime += time;}
    public long getTotalNodeRecurrenceTime() {return this.totalRecurrenceTime;}
    public void setAverageNodeRecurrenceTime(float avgRecurrenceTime) {this.avgRecurrenceTime = avgRecurrenceTime;}
    public float getAvgRecurrenceTime() {return this.avgRecurrenceTime;}
    public void setAverageRecurrenceLength(float avgRecurrenceLength) {this.avgRecurrenceLength = avgRecurrenceLength;}
    public void setAverageShortestDistance(float avgShortestDistance) {this.avgShortestDistance = avgShortestDistance;}
    public float getAverageShortestDistance() { return this.avgShortestDistance; }
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
    public Map<Integer, MyNodeDepthInfo> getNodeDepthInfoMap() {return this.nodeDepthInfoMap;}
    public void setInContribution(int inContribution) {this.inContribution += inContribution;}
    public int getInContribution() { return this.inContribution; }
    public void setOutContribution(int outContribution) {this.outContribution += outContribution;}
    public int getOutContribution() { return this.outContribution; }
    public long getMaxOutContribution() {
        long max = 0L;
        Collection<MyEdge> edges = MySequentialGraphVars.g.getOutEdges(this);
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
        Collection<MyEdge> edges = MySequentialGraphVars.g.getInEdges(this);
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
    public void setRecurrenceCount(int recurrenceCount) {
        this.nodeRecurrentCount += recurrenceCount;
    }
    public int getTotalNodeRecurrentCount() {return this.nodeRecurrentCount;}
    public long getUniqueContribution() { return this.uniqueContribution; }
    public String getName() { return this.name; }
    public long getContribution() {return this.contribution;}
    public long getTotalReachTime() {return this.totalReachTime;}
    public void setTotalReachTime(long time) {
        this.totalReachTime += time;
        this.setMaxReachTime(time);
        this.setMinReachTime(time);
    }
    public void setAverageTime(double avgReachTime) {
        this.avgReachTime = avgReachTime;
    }
    public void setDuration(long duration) {
        this.duration += duration;
        this.durationCount++;
        this.setMaxDuration(duration);
        this.setMinDuration(duration);
    }
    public float getAverageInContribution() {
        if (this.inContribution == 0) {
            return 0.00f;
        }
        return (float)this.inContribution/ MySequentialGraphVars.g.getPredecessorCount(this);
    }
    public float getAverageOutContribution() {
        if (this.outContribution == 0) {
            return 0.00f;
        }
        return (float)this.outContribution/ MySequentialGraphVars.g.getSuccessorCount(this);
    }
    public long getMaxDuration() {return (long)this.maxDuration;}
    public void setMaxDuration(long duration) {
        if (this.maxDuration < duration) {
            this.maxDuration = duration;
        }
    }
    public void setMinDuration(long duration) {
        if (duration > 0 && this.minDuration > duration) {
            this.minDuration = duration;
        }
    }
    public long getMinDuration() {
        if (this.minDuration == 100000000000000L) {return 0L;}
        return minDuration;
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
    public long getMinReachTime() {
        if (this.minReachTime == 500000000000000L) {return 0L;}
        return this.minReachTime;
    }
    public void setUniqueContribution() {
        this.uniqueContribution++;
    }
    public void updateContribution(long contribution) {
        this.contribution += contribution;
    }
    public void setCurrentValue(float value) { this.value = value;}
    public void setCurrentValue(long value) {this.value = value;}
    public float getAverageDuration() {
        if (this.durationCount == 0) {return 0.00f;}
        return (float)this.duration/this.durationCount;
    }
    public double getAverageReachTime() {
        long totalReachTime = 0L;
        for (Integer depth : this.nodeDepthInfoMap.keySet()) {
            totalReachTime += this.nodeDepthInfoMap.get(depth).getReachTime();
        }
        return (totalReachTime == 0 ? 0.00D : (double)totalReachTime/this.nodeDepthInfoMap.size());
    }
    public int getPredecessorCount() {
        return MySequentialGraphVars.g.getPredecessorCount(MySequentialGraphVars.g.vRefs.get(this.getName()));
    }
    public int getSuccessorCount() {
        return MySequentialGraphVars.g.getSuccessorCount(MySequentialGraphVars.g.vRefs.get(this.getName()));
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

    public float getCurrentValue() {return this.value;}
    public void setOriginalValue(float value) {this.originalValue = value;}
    public float getOriginalValue() { return this.originalValue; }
    @Override
    public int compareTo(MyNode n) {
        if (MySequentialGraphVars.isTimeOn) {
            switch (MySequentialGraphVars.nodeOrderByComboBoxIdx) {
                case 0: return (int) (this.getContribution() - n.getContribution());
                case 1: return (this.getInContribution() - n.getInContribution());
                case 2: return (this.getOutContribution() - n.getOutContribution());
                case 3: return (int) (this.getUniqueContribution() - n.getUniqueContribution());
                case 4: return (this.getOpenNodeCount() - n.getOpenNodeCount());
                case 5: return (this.getEndNodeCount() - n.getEndNodeCount());
                case 6: return (this.getSuccessorCount() - n.getSuccessorCount());
                case 7: return (this.getPredecessorCount() - n.getPredecessorCount());
                case 8: return (Math.abs(this.getSuccessorCount() - this.getPredecessorCount()) - Math.abs(n.getSuccessorCount() - n.getPredecessorCount()));
                case 9: return (this.getTotalNodeRecurrentCount() - n.getTotalNodeRecurrentCount());
                case 10: return (int) ((this.getBetweeness() - n.getBetweeness())*1000);
                case 11: return (int) ((this.getCloseness() - n.getCloseness())*1000);
                case 12: return (int) ((this.getEignevector() - n.getEignevector())*1000);
                case 13: return (int) (this.getTotalReachTime() - n.getTotalReachTime());
                case 14: return (int) (this.getDuration() - n.getDuration());
                default: return (int) (this.getContribution() - n.getContribution());
            }
        } else {
            switch (MySequentialGraphVars.nodeOrderByComboBoxIdx) {
                case 0: return (int) (this.getContribution() - n.getContribution());
                case 1: return (this.getInContribution() - n.getInContribution());
                case 2: return (this.getOutContribution() - n.getOutContribution());
                case 3: return (int) (this.getUniqueContribution() - n.getUniqueContribution());
                case 4: return (this.getOpenNodeCount() - n.getOpenNodeCount());
                case 5: return (this.getEndNodeCount() - n.getEndNodeCount());
                case 6: return (this.getSuccessorCount() - n.getSuccessorCount());
                case 7: return (this.getPredecessorCount() - n.getPredecessorCount());
                case 8: return (Math.abs(this.getSuccessorCount() - this.getPredecessorCount()) - Math.abs(n.getSuccessorCount() - n.getPredecessorCount()));
                case 9: return (this.getTotalNodeRecurrentCount() - n.getTotalNodeRecurrentCount());
                case 10: return (int) ((this.getBetweeness() - n.getBetweeness())*1000);
                case 11: return (int) ((this.getCloseness() - n.getCloseness())*1000);
                case 12: return (int) ((this.getEignevector() - n.getEignevector())*1000);
                default: return (int) (this.getContribution() - n.getContribution());
            }
        }
    }

    public int getVariableStrength(String variableNode) {
        return this.variableStrengthMap.get(variableNode);
    }
    public void setNodeDistinctValue(String variable, String value) {
        if (!this.nodeCategoryValueMap.containsKey(variable)) {
            this.nodeCategoryValueMap.put(variable, value);
        }
    }

    public String toString() {
        return name;
    }
}