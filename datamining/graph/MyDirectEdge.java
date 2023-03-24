package datamining.graph;

import datamining.utils.system.MyVars;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MyDirectEdge
implements Serializable {

    private MyDirectNode source;
    private MyDirectNode dest;
    private int contribution;
    private float support;
    private float confidence;
    private float lift;
    private float closeness;
    private float currentValue = -1;
    public float betweeness;
    private String label;

    private float originalValue;
    protected Map<String, Float> edgeValues = new HashMap<>();
    protected Map<String, String> edgeLabels = new HashMap<>();

    public MyDirectEdge() {}
    public MyDirectEdge(MyDirectNode source, MyDirectNode dest) {
        this.source = source;
        this.dest = dest;
    }

    public void addEdgeValue(String variable, float value) {
        if (edgeValues.containsKey(variable)) {
            if (edgeValues.get(variable).equals("SUM")) {
                this.edgeValues.put(variable, this.edgeValues.get(variable) + value);
            } else if (edgeValues.get(variable).equals("AVERAGE")) {
                float totalValue = this.edgeValues.get(variable) + value;
                this.edgeValues.put(variable, totalValue/2);
            }
        } else {this.edgeValues.put(variable, value);}
    }

    public void addEdgeLabel(String variable, String value) {
        if (!this.edgeLabels.containsKey(variable)) {this.edgeLabels.put(variable, value);}
    }

    public void setCloseness(float closeness) {this.closeness = closeness;}
    public float getCloseness() {return this.closeness;}
    public float getOriginalValue() { return this.originalValue; }
    public void setOriginalNumericValue(float value) { this.originalValue = value; }
    public Map<String, Float> getEdgeValues() {return this.edgeValues;}
    public void setCurrentValue(float currentNumericValue) {this.currentValue = currentNumericValue;}
    public float getCurrentValue() {return this.currentValue;}
    public MyDirectNode getDest() {return this.dest;}
    public MyDirectNode getSource() {return this.source;}
    public void setCurrentLabel(String label) {this.label = label;}
    public String getCurrentLabel() {return this.label;}
    public String getName() {return this.source.getLabel() + "-" + this.dest.getLabel();}
    public void setContribution(int contribution) {this.contribution += contribution;}
    public int getContribution() {return this.contribution;}
    public float getSupport() {return this.support;}
    public float getConfidence() {return this.confidence;}
    public void setConfidence() {this.confidence = (float)this.getContribution()/this.getSource().getContribution();}
    public void setSupport() {this.support = (float)this.getContribution()/ MyVars.directMarkovChain.getTotalEdgeContribution();}
    public void setLift() {
        this.lift = ((float)this.getContribution()/MyVars.directMarkovChain.getTotalEdgeContribution())/
            (((float)this.getSource().getContribution()/MyVars.directMarkovChain.getTotalEdgeContribution())*((float)this.getDest().getContribution()/MyVars.directMarkovChain.getTotalEdgeContribution()));
    }
    public float getLift() {return this.lift;}
}
