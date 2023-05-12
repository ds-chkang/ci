package medousa.direct.broker;

import medousa.direct.feature.MyDirectGraphFeatureGenerator;
import medousa.direct.feature.MyDirectGraphPlusFeatureGenerator;
import medousa.direct.feature.MyDirectGraphTimeConstrainedFeatureGenerator;

public class MyDirectGraphFeatureBroker
extends MyDirectGraphCategoryBroker {

    private MyDirectGraphFeatureGenerator featureGenerator;
    private MyDirectGraphPlusFeatureGenerator plusFeatureGenerator;
    private MyDirectGraphTimeConstrainedFeatureGenerator timeConstrainedFeatureGenerator;
    public MyDirectGraphFeatureBroker() {
        super();
    }



}
