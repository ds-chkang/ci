package medousa.direct.broker;

import medousa.direct.graph.engine.MyDirectGraphRelationPropertyExtractionManager;

public class MyDirectGraphEngineBroker
extends MyDirectGraphNodeLabelFeatureBroker {

    public MyDirectGraphEngineBroker() {
        super();
    }
    public boolean runEngine() {
        MyDirectGraphRelationPropertyExtractionManager markovChainEngine = new MyDirectGraphRelationPropertyExtractionManager();
        return markovChainEngine.run();
    }
}
