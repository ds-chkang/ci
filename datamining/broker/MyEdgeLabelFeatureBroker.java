package datamining.broker;

import datamining.feature.MyEdgeLabelFeatureGenerator;
import datamining.feature.MyEdgeValueFeatureGenerator;

public class MyEdgeLabelFeatureBroker
extends MyEdgeValueFeatureBroker {

    private MyEdgeLabelFeatureGenerator edgeLabelFeatureGenerator;
    public MyEdgeLabelFeatureBroker() { super(); }

    public void generateEdgeLabelFeatures(String edgeValueVariable, String edgeValueType) {
        try {
            edgeLabelFeatureGenerator = new MyEdgeLabelFeatureGenerator();
            edgeLabelFeatureGenerator.generateEdgeLabelFeatures(edgeValueVariable, getRawData(), edgeValueType);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}