package datamining.broker;

import datamining.feature.MyFeatureGenerator;
import datamining.feature.MyTimeConstrainedFeatureGenerator;
import datamining.feature.MyTimeConstrainedFeatureModifier;
import datamining.utils.system.MyVars;

public class MyFeatureBroker
extends MyCategoryBroker {

    private MyFeatureGenerator featureGenerator;
    private MyTimeConstrainedFeatureGenerator timeConstrainedFeatureGenerator;
    public MyFeatureBroker() {
        super();
    }

    public void generateInputFeatures() {
        try {
            MyVars.app.getMsgBroker().getConfigPanel().getDefaultVariableTable().isTimeVariableOn();
            MyVars.app.getMsgBroker().getConfigPanel().getSupplimentaryVariableTable().isSupplementaryOn();
            if (!MyVars.isTimeOn) {
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
