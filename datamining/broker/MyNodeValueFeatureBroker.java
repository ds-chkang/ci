package datamining.broker;

import datamining.feature.MyEdgeLabelFeatureGenerator;
import datamining.feature.MyNodeValueFeatureGenerator;

public class MyNodeValueFeatureBroker
extends MyEdgeLabelFeatureBroker {

    private MyNodeValueFeatureGenerator nodeValueFeatureGenerator;
    public MyNodeValueFeatureBroker() { super(); }

    public void generateNodeValueFeatures(String nodeValueVariable, String nodeValueType) {
        try {
            nodeValueFeatureGenerator = new MyNodeValueFeatureGenerator();
            nodeValueFeatureGenerator.generateValueFeatures(nodeValueVariable, getRawData(), nodeValueType);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}