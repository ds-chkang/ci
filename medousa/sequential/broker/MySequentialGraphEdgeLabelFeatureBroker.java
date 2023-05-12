package medousa.sequential.broker;

import medousa.sequential.feature.MyEdgeLabelFeatureGenerator;

public class MySequentialGraphEdgeLabelFeatureBroker
extends MySequentialGraphEdgeValueFeatureBroker {

    private MyEdgeLabelFeatureGenerator edgeLabelFeatureGenerator;
    public MySequentialGraphEdgeLabelFeatureBroker() { super(); }

    public void generateEdgeLabelFeatures(String edgeValueVariable, String edgeValueType) {
        try {
            edgeLabelFeatureGenerator = new MyEdgeLabelFeatureGenerator();
            edgeLabelFeatureGenerator.generateEdgeLabelFeatures(edgeValueVariable, getRawData(), edgeValueType);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}