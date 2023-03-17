package datamining.graph;

import datamining.utils.system.MyVars;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MyEdge
implements Serializable, Comparable<MyEdge> {

    public MyNode dest;
    public MyNode source;
    public float lift;
    public long reachTime;
    public float support;
    public float confidence;
    public int contribution;
    public float value = 4.8f;
    public int uniqueContribution;
    public float originalValue = 4.8f;
    public float tmpValue = 0.0f;
    public Map<String, Float> edgeValueMap = new HashMap<>();
    public Map<String, String> edgeLabelMap = new HashMap<>();
    public MyEdge() {}
    public MyEdge(int cont, MyNode source, MyNode dest) {
        this.contribution = cont;
        this.source = source;
        this.dest = dest;
    }
    public MyEdge(MyNode source, MyNode dest) {
        this.source = source;
        this.dest = dest;
    }
    public void setEdgeSumValue(String edgeValueVariable, float value) {
        if (this.edgeValueMap.containsKey(edgeValueVariable)) {this.edgeValueMap.put(edgeValueVariable, this.edgeValueMap.get(edgeValueVariable)+value);
        } else {this.edgeValueMap.put(edgeValueVariable, value);}
    }

    public void setEdgeMaxValue(String edgeValueVariable, float value) {
        if (this.edgeValueMap.containsKey(edgeValueVariable)) {
            float exValue = this.edgeValueMap.get(edgeValueVariable);
            if (exValue < value) {
                this.edgeValueMap.put(edgeValueVariable, value);
            }
        } else {this.edgeValueMap.put(edgeValueVariable, value);}
    }

    public void setEdgeDistinctValue(String variable, float value) {
        if (!this.edgeLabelMap.containsKey(variable)) {
            this.edgeValueMap.put(variable, value);
        }
    }

    public void setEdgeLabel(String edgeLabel, String value) {
        if (!this.edgeLabelMap.containsKey(edgeLabel)) {
            this.edgeLabelMap.put(edgeLabel.substring(2), value);
        }
    }

    public void setEdgeAverageValue(String edgeValueVariable, float value) {
        if (this.edgeValueMap.containsKey(edgeValueVariable)) {
            float exAverageWgt = this.edgeValueMap.get(edgeValueVariable);
            this.edgeValueMap.put(edgeValueVariable, (exAverageWgt+value)/2);
        } else {this.edgeValueMap.put(edgeValueVariable, value);}
    }

    public MyNode getDest() {return this.dest;}
    public MyNode getSource() {return this.source;}
    public float getCurrentValue() {return this.value;}
    public void setCurrentValue(float value) {
        this.value = value;
    }
    public void setContribution(int contribution) {this.contribution += contribution;}
    public int getContribution() {return this.contribution;}
    public void setUniqueContribution(int uniqueContribution) {this.uniqueContribution += uniqueContribution;}
    public int getUniqueContribution() {return this.uniqueContribution;}
    public void setReachTime(long reachTime) {this.reachTime += reachTime;}
    public long getReachTime() {return this.reachTime;}
    public float getOriginalValue() { return this.originalValue; }
    public void setOriginalValue(float value) { this.originalValue = value; }
    public double getSupport() {return this.support;}
    public double getConfidence() {return this.confidence;}
    public float getLift() {return this.lift;}
    public void setConfidence() {
        this.confidence = (float)this.getContribution()/this.getSource().getContribution();
    }
    public Map<String, Float> getEdgeValueMap() {return this.edgeValueMap;}
    public void setSupport() {this.support = (float)this.getContribution()/MyVars.g.getTotalEdgeContribution();}
    public void setLift() {
        this.lift = (float)(this.getSupport()/(((float)this.getSource().getContribution()/MyVars.g.getTotalEdgeContribution())*((float)this.getDest().getContribution()/MyVars.g.getTotalEdgeContribution())));
    }
    @Override public int compareTo(MyEdge e) {
        if (MyVars.isTimeOn) {
            if (MyVars.edgeOrderByComboBoxIdx == 0) {return this.uniqueContribution - e.getUniqueContribution();
            } else if (MyVars.edgeOrderByComboBoxIdx == 1) {return this.contribution - e.getContribution();
            } else if (MyVars.edgeOrderByComboBoxIdx == 2) {return (int) (this.reachTime - e.getReachTime());
            } else if (MyVars.edgeOrderByComboBoxIdx == 3) {return (int) (this.support - e.getSupport());
            } else if (MyVars.edgeOrderByComboBoxIdx == 4) {return (int) ((this.confidence - e.getConfidence()) * 1000);
            } else if (MyVars.edgeOrderByComboBoxIdx == 5) {return (int) ((this.lift - e.getLift()) * 1000);
            } else if (MyVars.edgeOrderByComboBoxIdx > 5) {return (int)((this.value - e.getCurrentValue())*1000);
            } else {return this.contribution - e.contribution;}
        } else {
            if (MyVars.edgeOrderByComboBoxIdx == 0) {return this.uniqueContribution - e.getUniqueContribution();
            } else if (MyVars.edgeOrderByComboBoxIdx == 1) {return this.contribution - e.getContribution();
            } else if (MyVars.edgeOrderByComboBoxIdx == 2) {return (int) ((this.support - e.getSupport()) * 1000);
            } else if (MyVars.edgeOrderByComboBoxIdx == 3) {return (int) ((this.confidence - e.getConfidence()) * 1000);
            } else if (MyVars.edgeOrderByComboBoxIdx == 4) {return (int) ((this.lift - e.getLift()) * 1000);
            } else if (MyVars.edgeOrderByComboBoxIdx > 4) {return (int)((this.value - e.getCurrentValue())*1000);
            } else {return this.contribution - e.contribution;}
        }

    }
}
