package datamining.broker;

import datamining.feature.MyNodeLabelFeatureGenerator;

public class MyNodeLabelFeatureBroker
extends MyNodeValueFeatureBroker {

    private MyNodeLabelFeatureGenerator nodeLabelFeatureGenerator;
    public MyNodeLabelFeatureBroker() { super(); }

    public void generateNodeLabelFeatures(String nodeLabelVariable, String nodeLabelType) {
        try {
            nodeLabelFeatureGenerator = new MyNodeLabelFeatureGenerator();
            nodeLabelFeatureGenerator.generateNodeLabelFeatures(nodeLabelVariable, getRawData(), nodeLabelType);
        } catch (Exception ex) {ex.printStackTrace();}
    }
}