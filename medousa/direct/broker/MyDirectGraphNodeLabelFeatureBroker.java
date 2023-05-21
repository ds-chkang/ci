package medousa.direct.broker;

import medousa.direct.feature.MyDirectGraphNodeLabelFeatureGenerator;

public class MyDirectGraphNodeLabelFeatureBroker
extends MyDirectGraphNodeValueFeatureBroker {

    private MyDirectGraphNodeLabelFeatureGenerator nodeLabelFeatureGenerator;
    public MyDirectGraphNodeLabelFeatureBroker() { super(); }

    public void generateNodeLabelFeatures(String nodeLabelVariable, String nodeLabelType) {
        try {
            nodeLabelFeatureGenerator = new MyDirectGraphNodeLabelFeatureGenerator();
            nodeLabelFeatureGenerator.generateNodeLabelFeatures(nodeLabelVariable, getRawData(), nodeLabelType);
        } catch (Exception ex) {ex.printStackTrace();}
    }
}