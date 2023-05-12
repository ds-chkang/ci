package medousa.sequential.broker;

import medousa.sequential.graph.element.MyRelationPropertyExtractionManager;

public class MySequentialGraphEngineBroker
extends MySequentialGraphNodeLabelFeatureBroker {

    public MySequentialGraphEngineBroker() {
        super();
    }
    public boolean runEngine() {
        MyRelationPropertyExtractionManager markovChainEngine = new MyRelationPropertyExtractionManager();
        return markovChainEngine.run();
    }
}
