package medousa.sequential.broker;

import medousa.sequential.feature.MyFeatureGenerator;
import medousa.sequential.feature.MyTimeConstrainedFeatureGenerator;
import medousa.sequential.feature.MyTimeConstrainedFeatureModifier;
import medousa.sequential.utils.MySequentialGraphVars;

public class MySequentialGraphFeatureBroker
extends MySequentialGraphCategoryBroker {

    private MyFeatureGenerator featureGenerator;
    private MyTimeConstrainedFeatureGenerator timeConstrainedFeatureGenerator;
    public MySequentialGraphFeatureBroker() {
        super();
    }

    public void generateInputFeatures() {
        try {
            MySequentialGraphVars.app.getSequentialGraphMsgBroker().getConfigPanel().getDefaultVariableTable().isTimeVariableOn();
            MySequentialGraphVars.app.getSequentialGraphMsgBroker().getConfigPanel().getSupplimentaryVariableTable().isSupplementaryOn();
            if (!MySequentialGraphVars.isTimeOn) {
                featureGenerator = new MyFeatureGenerator();
                featureGenerator.generateInputFeatures(getRawData());
            } else {
                timeConstrainedFeatureGenerator = new MyTimeConstrainedFeatureGenerator();
                timeConstrainedFeatureGenerator.generateInputFeatures(getRawData());
                MyTimeConstrainedFeatureModifier featureTimeModifier = new MyTimeConstrainedFeatureModifier();
                featureTimeModifier.modifyTime();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
