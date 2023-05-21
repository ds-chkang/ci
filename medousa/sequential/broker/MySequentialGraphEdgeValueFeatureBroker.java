package medousa.sequential.broker;

import medousa.sequential.feature.MyEdgeValueFeatureGenerator;

public class MySequentialGraphEdgeValueFeatureBroker
extends MySequentialGraphFeatureBroker {

    private MyEdgeValueFeatureGenerator edgeValueFeatureGenerator;
    public MySequentialGraphEdgeValueFeatureBroker() { super(); }

    public void generateEdgeValueFeatures(String edgeValueVariable, String edgeValueType) {
        try {
            edgeValueFeatureGenerator = new MyEdgeValueFeatureGenerator();
            edgeValueFeatureGenerator.generateEdgeValueFeatures(edgeValueVariable, getRawData(), edgeValueType);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}