package datamining.broker;


import datamining.feature.MyEdgeValueFeatureGenerator;

public class MyEdgeValueFeatureBroker
extends MyFeatureBroker {

    private MyEdgeValueFeatureGenerator edgeValueFeatureGenerator;
    public MyEdgeValueFeatureBroker() { super(); }

    public void generateEdgeValueFeatures(String edgeValueVariable, String edgeValueType) {
        try {
            edgeValueFeatureGenerator = new MyEdgeValueFeatureGenerator();
            edgeValueFeatureGenerator.generateEdgeValueFeatures(edgeValueVariable, getRawData(), edgeValueType);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}