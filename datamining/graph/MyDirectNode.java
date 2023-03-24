package datamining.graph;

import datamining.utils.system.MyVars;

import java.awt.geom.Ellipse2D;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MyDirectNode
implements Serializable {

    private String name;
    private String label;
    private int contribution;
    private float value;
    private float originalValue;
    private int inContribution;
    private int outContribution;
    private double betweeness;
    private double closeness;
    private double eignevector;
    private long edgeValueConsumptionRatio;
    protected Map<String, Float> nodeValues = new HashMap<>();
    protected Map<String, String> nodeLabels = new HashMap<>();
    double avgShortestPathLength = 0.00D;
    long unreachableNodeCount = 0;
    private double pageRankScore;
    protected Ellipse2D.Double ellipse2D = null;

    public MyDirectNode() {}
    public void setAverageShortestPathLength(double avgShortestPathLength) {
        this.avgShortestPathLength = avgShortestPathLength;
    }
    public double getAverageShortestPathLength() { return this.avgShortestPathLength; }

    public void setNodeSize(Ellipse2D.Double ellipse2D) {
        this.ellipse2D = ellipse2D;
    }
    public Ellipse2D.Double getNodeSize() {
        return this.ellipse2D;
    }
    public void updateContribution(int contribution) {this.contribution += contribution;}
    public double getAvgShortestPathLength() {return this.avgShortestPathLength;}
    public void setUnreachableNodeCount(long unreachableNodeCount) {
        this.unreachableNodeCount = unreachableNodeCount;
    }
    public long getUnreachableNodeCount() {return this.unreachableNodeCount;}
    public void addNodeValue(String variable, float value) {
        if (this.nodeValues.containsKey(variable)) {
            if (MyVars.nodeValues.get(variable).equals("SUM")) {
                this.nodeValues.put(variable, this.nodeValues.get(variable) + value);
            } else if (MyVars.nodeValues.get(variable).equals("AVERAGE")) {
                float totalValue = this.nodeValues.get(variable) + value;
                this.nodeValues.put(variable, totalValue/2);
            }
        } else {this.nodeValues.put(variable, value);}
    }

    public void addNodeLabel(String variable, String value) {
        if (!this.nodeLabels.containsKey(variable)) {
            this.nodeLabels.put(variable, value);
        }
    }

    public void setBetweeness(double betweeness) {this.betweeness = betweeness;}
    public void setCloseness(double closeness) {
        if (Double.isNaN(closeness)) {this.closeness = 0.00D;}
        else {this.closeness = closeness;}
    }

    public MyDirectNode copyNode() {
        MyDirectNode n = new MyDirectNode(this.name);
        n.setContribution(this.contribution);
        return n;
    }

    public void setPageRankScore(double score) {
        this.pageRankScore = score;
    }

    public double getPageRankScore() { return this.pageRankScore; }

    public void setEignevector(double eignevector) {this.eignevector = eignevector;}
    public double getBetweeness() { return this.betweeness; }
    public double getCloseness() { return this.closeness; }
    public double getEignevector() { return this.eignevector; }
    public MyDirectNode(String name) {
        this.name = name;
        this.label = name;
    }
    public void setName(String name) {this.name = name;}
    public String getName() {return this.name;}
    public void setLabel(String label) { this.label = label; }
    public String getLabel() { return this.label; }
    public void setCurrentValue(float value) { this.value = value; }
    public float getCurrentValue() { return this.value; }
    public void setOriginalValue(float value) { this.originalValue = value; }
    public float getOriginalValue() { return this.originalValue; }
    public void setContribution(int contribution) {this.contribution += contribution;}
    public int getContribution() {return this.contribution;}
    public void setInContribution(int inContribution) {this.inContribution += inContribution;}
    public int getInContribution() {return this.inContribution;}
    public void setOutContribution(int outContribution) {this.outContribution += outContribution;}
    public int getOutContribution() {return this.outContribution;}


}
