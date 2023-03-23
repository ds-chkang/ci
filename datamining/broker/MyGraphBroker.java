package datamining.broker;


import datamining.utils.system.MyVars;

import datamining.graph.MyDirectEdge;
import datamining.graph.MyDirectMarkovChain;
import datamining.graph.MyDirectMarkovChainBuilder;
import datamining.graph.MyDirectNode;
import edu.uci.ics.jung.graph.Graph;

public class MyGraphBroker
extends MyEngineBroker {

    public MyGraphBroker() {
        super();
    }

    public Graph<MyDirectNode, MyDirectEdge> createDirectGraph() {
        MyDirectMarkovChainBuilder directMarkovChainBuilder = new MyDirectMarkovChainBuilder();
        MyDirectMarkovChain directMarkovChain = directMarkovChainBuilder.createDirectMarkovChain(getRawData());
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
        directMarkovChainBuilder.setAverageUnWeightedDirectGraphShortestPathLength(MyVars.directMarkovChain);
        directMarkovChain.edRefs = null;
        directMarkovChain.directMarkovChainEdgeRefMap = null;
        return MyVars.directMarkovChain;
    }


}
