package medousa.direct.broker;



import medousa.direct.graph.MyDirectEdge;
import medousa.direct.graph.MyDirectGraph;
import medousa.direct.graph.MyDirectGraphBuilder;
import medousa.direct.graph.MyDirectNode;
import medousa.direct.utils.MyDirectGraphVars;

public class MyDirectGraphGraphBroker
extends MyDirectGraphEngineBroker {

    public MyDirectGraphGraphBroker() {
        super();
    }

    public MyDirectGraph<MyDirectNode, MyDirectEdge> createDirectGraph() {
        MyDirectGraphBuilder directMarkovChainBuilder = new MyDirectGraphBuilder();
        MyDirectGraph directMarkovChain = directMarkovChainBuilder.createDirectMarkovChain(getRawData());
        directMarkovChain.setMaxNodeContribution();
        directMarkovChain.setMinNodeContribution();
        directMarkovChain.setMaxEdgeContribution();
        directMarkovChain.setMinEdgeContribution();
        directMarkovChain.setTotalNodeContribution();
        directMarkovChain.setTotalEdgeContribution();
        directMarkovChain.setDefaultNodeValues();
        directMarkovChain.setMaxNodeValue();
        directMarkovChain.setNodeClosenessCentrality();
        directMarkovChain.setNodeBetweenessCentrality();
        directMarkovChain.setNodeEigenVectorCentrality();
        directMarkovChain.setPageRankScore();
        directMarkovChain.setConnectedNetworkComponentCountByGraph();
        directMarkovChainBuilder.addNodeValues();
        directMarkovChainBuilder.addNodeLabels();
        directMarkovChainBuilder.setAverageShortestOutDistance(MyDirectGraphVars.directGraph);
        directMarkovChainBuilder.setAverageShortestInDistance(MyDirectGraphVars.directGraph);
        directMarkovChain.edRefs = null;
        directMarkovChain.directMarkovChainEdgeRefMap = null;
        return MyDirectGraphVars.directGraph;
    }


}
