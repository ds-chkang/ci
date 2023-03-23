package datamining.broker;

import datamining.feature.MyFeatureGenerator;
import datamining.feature.MyPlusFeatureGenerator;
import datamining.feature.MyTimeConstrainedFeatureGenerator;
import datamining.feature.MyTimeConstrainedFeatureModifier;
import datamining.utils.system.MyVars;

public class MyFeatureBroker
extends MyCategoryBroker {

    private MyFeatureGenerator featureGenerator;
    private MyPlusFeatureGenerator plusFeatureGenerator;
    private MyTimeConstrainedFeatureGenerator timeConstrainedFeatureGenerator;
    public MyFeatureBroker() {
        super();
    }



}
