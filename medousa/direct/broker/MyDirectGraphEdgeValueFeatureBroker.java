package medousa.direct.broker;


import medousa.direct.feature.MyDirectGraphEdgeValueFeatureGenerator;

public class MyDirectGraphEdgeValueFeatureBroker
extends MyDirectGraphFeatureBroker {

    private MyDirectGraphEdgeValueFeatureGenerator edgeValueFeatureGenerator;
    public MyDirectGraphEdgeValueFeatureBroker() { super(); }

    public void generateEdgeValueFeatures(String edgeValueVariable, String edgeValueType) {
        try {
            edgeValueFeatureGenerator = new MyDirectGraphEdgeValueFeatureGenerator();
            edgeValueFeatureGenerator.generateEdgeValueFeatures(edgeValueVariable, getRawData(), edgeValueType);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}