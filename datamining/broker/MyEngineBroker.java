package datamining.broker;

import datamining.graph.engine.MyRelationPropertyExtractionManager;

public class MyEngineBroker
extends MyNodeLabelFeatureBroker {

    public MyEngineBroker() {
        super();
    }
    public boolean runEngine() {
        MyRelationPropertyExtractionManager markovChainEngine = new MyRelationPropertyExtractionManager();
        return markovChainEngine.run();
    }
}
