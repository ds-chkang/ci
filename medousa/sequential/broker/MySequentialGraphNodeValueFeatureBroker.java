package medousa.sequential.broker;

import medousa.sequential.feature.MyNodeValueFeatureGenerator;

public class MySequentialGraphNodeValueFeatureBroker
extends MySequentialGraphEdgeLabelFeatureBroker {

    private MyNodeValueFeatureGenerator nodeValueFeatureGenerator;
    public MySequentialGraphNodeValueFeatureBroker() { super(); }

    public void generateNodeValueFeatures(String nodeValueVariable, String nodeValueType) {
        try {
            nodeValueFeatureGenerator = new MyNodeValueFeatureGenerator();
            nodeValueFeatureGenerator.generateValueFeatures(nodeValueVariable, getRawData(), nodeValueType);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}