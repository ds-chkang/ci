package medousa.sequential.graph;

import medousa.sequential.utils.MySequentialGraphVars;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MyEdge
implements Serializable, Comparable<MyEdge> {

    public MyNode dest;
    public MyNode source;
    public float lift;
    public long totalTime;
    public float support;
    public float confidence;
    public long contribution;
    public float value = 4.8f;
    public long uniqueContribution;
    public float originalValue = 4.8f;
    public float tmpValue = 0.0f;
    public float avgTime = 0f;
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

    public void setAverageTime(float avgTime) {this.avgTime = avgTime;}
    public float getAverageTime() {return this.avgTime; }
    public MyNode getDest() {return this.dest;}
    public MyNode getSource() {return this.source;}
    public float getCurrentValue() {return this.value;}
    public void setCurrentValue(float value) {
        this.value = value;
    }
    public void setContribution(int contribution) {this.contribution += contribution;}
    public long getContribution() {return this.contribution;}
    public void setUniqueContribution(int uniqueContribution) {this.uniqueContribution += uniqueContribution;}
    public long getUniqueContribution() {return this.uniqueContribution;}
    public void setTotalTime(long totalTime) {this.totalTime += totalTime;}
    public long getTotalTime() {return this.totalTime;}
    public float getOriginalValue() { return this.originalValue; }
    public void setOriginalValue(float value) { this.originalValue = value; }
    public float getSupport() {return this.support;}
    public float getConfidence() {return this.confidence;}
    public float getLift() {return this.lift;}
    public void setConfidence() {
        this.confidence = (float)this.getContribution()/this.getSource().getContribution();
    }
    public Map<String, Float> getEdgeValueMap() {return this.edgeValueMap;}
    public void setSupport() {this.support = (float)this.getContribution()/ MySequentialGraphVars.g.getTotalEdgeContribution();}
    public void setLift() {
        this.lift = (float)(this.getSupport()/(((float)this.getSource().getContribution()/ MySequentialGraphVars.g.getTotalEdgeContribution())*((float)this.getDest().getContribution()/ MySequentialGraphVars.g.getTotalEdgeContribution())));
    }
    @Override public int compareTo(MyEdge e) {
        if (MySequentialGraphVars.isTimeOn) {
            if (MySequentialGraphVars.edgeOrderByComboBoxIdx == 0) {return (int)(this.uniqueContribution - e.getUniqueContribution());
            } else if (MySequentialGraphVars.edgeOrderByComboBoxIdx == 1) {return (int)(this.contribution - e.getContribution());
            } else if (MySequentialGraphVars.edgeOrderByComboBoxIdx == 2) {return (int) (this.totalTime - e.getTotalTime());
            } else if (MySequentialGraphVars.edgeOrderByComboBoxIdx == 3) {return (int) (this.support - e.getSupport());
            } else if (MySequentialGraphVars.edgeOrderByComboBoxIdx == 4) {return (int) ((this.confidence - e.getConfidence()) * 1000);
            } else if (MySequentialGraphVars.edgeOrderByComboBoxIdx == 5) {return (int) ((this.lift - e.getLift()) * 1000);
            } else if (MySequentialGraphVars.edgeOrderByComboBoxIdx > 5) {return (int)((this.value - e.getCurrentValue())*1000);
            } else {return (int)(this.contribution - e.contribution);}
        } else {
            if (MySequentialGraphVars.edgeOrderByComboBoxIdx == 0) {return (int)(this.uniqueContribution - e.getUniqueContribution());
            } else if (MySequentialGraphVars.edgeOrderByComboBoxIdx == 1) {return (int)(this.contribution - e.getContribution());
            } else if (MySequentialGraphVars.edgeOrderByComboBoxIdx == 2) {return (int) ((this.support - e.getSupport()) * 1000);
            } else if (MySequentialGraphVars.edgeOrderByComboBoxIdx == 3) {return (int) ((this.confidence - e.getConfidence()) * 1000);
            } else if (MySequentialGraphVars.edgeOrderByComboBoxIdx == 4) {return (int) ((this.lift - e.getLift()) * 1000);
            } else if (MySequentialGraphVars.edgeOrderByComboBoxIdx > 4) {return (int)((this.value - e.getCurrentValue())*1000);
            } else {return (int)(this.contribution - e.contribution);}
        }

    }
}
