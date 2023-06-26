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
        MyDirectGraphBuilder directGraphBuilder = new MyDirectGraphBuilder();
        MyDirectGraph directGraph = directGraphBuilder.createDirectGraph(getRawData());
        directGraph.setMaxNodeContribution();
        directGraph.setMinNodeContribution();
        directGraph.setMaxEdgeContribution();
        directGraph.setMinEdgeContribution();
        directGraph.setTotalNodeContribution();
        directGraph.setTotalEdgeContribution();
        directGraph.setDefaultNodeValues();
        directGraph.setMaxNodeValue();
        directGraph.setNodeClosenessCentrality();
        directGraph.setNodeBetweenessCentrality();
        directGraph.setNodeEigenVectorCentrality();
        directGraph.setPageRankScore();
        directGraph.setEdgeBetweeness();
        directGraph.setConnectedNetworkComponentCountByGraph();
        directGraphBuilder.addNodeValues();
        directGraphBuilder.addNodeLabels();
        directGraphBuilder.setAverageShortestOutDistance(MyDirectGraphVars.directGraph);
        //directGraphBuilder.setAverageShortestInDistance(MyDirectGraphVars.directGraph);
        directGraph.edRefs = null;
        directGraph.directGraphEdgeRefMap = null;
        return MyDirectGraphVars.directGraph;
    }


}
