package medousa.direct.broker;

import medousa.direct.feature.MyDirectGraphNodeValueFeatureGenerator;

public class MyDirectGraphNodeValueFeatureBroker
extends MyDirectGraphEdgeLabelFeatureBroker {

    private MyDirectGraphNodeValueFeatureGenerator nodeValueFeatureGenerator;
    public MyDirectGraphNodeValueFeatureBroker() { super(); }

    public void generateNodeValueFeatures(String nodeValueVariable, String nodeValueType) {
        try {
            nodeValueFeatureGenerator = new MyDirectGraphNodeValueFeatureGenerator();
            nodeValueFeatureGenerator.generateValueFeatures(nodeValueVariable, getRawData(), nodeValueType);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}