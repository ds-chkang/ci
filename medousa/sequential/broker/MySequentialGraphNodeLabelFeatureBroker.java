package medousa.sequential.broker;

import medousa.sequential.feature.MyNodeLabelFeatureGenerator;

public class MySequentialGraphNodeLabelFeatureBroker
extends MySequentialGraphNodeValueFeatureBroker {

    private MyNodeLabelFeatureGenerator nodeLabelFeatureGenerator;
    public MySequentialGraphNodeLabelFeatureBroker() { super(); }

    public void generateNodeLabelFeatures(String nodeLabelVariable, String nodeLabelType) {
        try {
            nodeLabelFeatureGenerator = new MyNodeLabelFeatureGenerator();
            nodeLabelFeatureGenerator.generateNodeLabelFeatures(nodeLabelVariable, getRawData(), nodeLabelType);
        } catch (Exception ex) {ex.printStackTrace();}
    }
}