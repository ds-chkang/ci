package medousa.sequential.broker;

import edu.uci.ics.jung.graph.Graph;
import medousa.MyProgressBar;
import medousa.sequential.graph.MyEdge;
import medousa.sequential.graph.MyGraphBuilder;
import medousa.sequential.graph.MyNode;
import medousa.sequential.graph.MyTimeConstrainedBuilider;
import medousa.sequential.utils.MySequentialGraphSysUtil;
import medousa.sequential.utils.MySequentialGraphVars;

public class MySequentialGraphBroker
extends MySequentialGraphEngineBroker {

    public MySequentialGraphBroker() {
        super();
    }

    public Graph<MyNode, MyEdge> createGraph() {
        try {
            if (!MySequentialGraphVars.isTimeOn) {
                MyGraphBuilder graphBuilder = new MyGraphBuilder();
                graphBuilder.createGraph();
                graphBuilder.setEndNodeCount();
                graphBuilder.setNodeDepthInformation();
                graphBuilder.setStartPositionNodeCount();
                graphBuilder.setNodePropagationCount();
                graphBuilder.setNodeRecursiveLength();
                graphBuilder.setNodeRecurrenceCount();
                graphBuilder.setEdgeSupport();
                graphBuilder.setEdgeConfidence();
                graphBuilder.setEdgeLift();
                graphBuilder.setNodeBetweenessCentrality();
                graphBuilder.setNodeClosenessCentrality();
                graphBuilder.setNodeEigenVectorCentrality();
                graphBuilder.setEdgeValues();
                graphBuilder.setEdgeLabels();
                graphBuilder.setNodeValues();
                graphBuilder.setNodeLabels();
                graphBuilder.setMaxNodeValue();
                graphBuilder.setAverageShortestDistance(MySequentialGraphVars.g);
                graphBuilder.setAverageNodeRecurrenceLength();
                graphBuilder.setVariableNodeStrength();
                graphBuilder.setConnectance();
                graphBuilder.setEbsilonOfGraph();
                graphBuilder.setConnectedNetworkComponentCountByGraph();
                graphBuilder.setGraphLevelTotalEdgeContribution();
                graphBuilder.setNumberOfGraphs();
                graphBuilder.setNodeContributionCountByObject();
                graphBuilder.setClusteringCoefficient();
                MySequentialGraphVars.g.edRefs = null;
                MySequentialGraphVars.g.edgeRefMap = null;
            } else {
                MyTimeConstrainedBuilider timeConstrainedGraphBuilider = new MyTimeConstrainedBuilider();
                timeConstrainedGraphBuilider.createGraph();
                timeConstrainedGraphBuilider.setTotalNodeDuration();
                timeConstrainedGraphBuilider.setStartPositionNodeCount();
                timeConstrainedGraphBuilider.setEndNodeCount();
                timeConstrainedGraphBuilider.setNodeDepthInformation();
                timeConstrainedGraphBuilider.setNodePropagationCount();
                timeConstrainedGraphBuilider.setNodeRecursiveLength();
                timeConstrainedGraphBuilider.setAverageRecursiveTime();
                timeConstrainedGraphBuilider.setNodeRecurrence();
                timeConstrainedGraphBuilider.setEdgeSupport();
                timeConstrainedGraphBuilider.setEdgeConfidence();
                timeConstrainedGraphBuilider.setEdgeLift();
                timeConstrainedGraphBuilider.setNodeBetweenessCentrality();
                timeConstrainedGraphBuilider.setNodeClosenessCentrality();
                timeConstrainedGraphBuilider.setNodeEigenVectorCentrality();
                timeConstrainedGraphBuilider.setPageRankScore();
                timeConstrainedGraphBuilider.setTimeConstrainedEdgeValues();
                timeConstrainedGraphBuilider.setTimeConstrainedEdgeTimeValues();
                timeConstrainedGraphBuilider.setTimeConstrainedEdgeLabels();
                timeConstrainedGraphBuilider.setTimeConstrainedNodeValues();
                timeConstrainedGraphBuilider.setTimeConstrainedNodeLabels();
                timeConstrainedGraphBuilider.setNodeTotalReachTime();
                timeConstrainedGraphBuilider.setNodeTotalRecurrenceTime();
                timeConstrainedGraphBuilider.setNodeAverageRecurrenceTime();
                timeConstrainedGraphBuilider.setNodeAverageReachTime();
                timeConstrainedGraphBuilider.setEdgeTotalAndAverageTime();
                timeConstrainedGraphBuilider.setMaxNodeValue();
                timeConstrainedGraphBuilider.setShortestDistanceProperties(MySequentialGraphVars.g);
                timeConstrainedGraphBuilider.setVariableNodeStrength();
                timeConstrainedGraphBuilider.setAverageNodeRecurrenceLength();
                timeConstrainedGraphBuilider.setConnectance();
                timeConstrainedGraphBuilider.setEbsilonOfGraph();
                timeConstrainedGraphBuilider.setConnectedNetworkComponentCountByGraph();
                timeConstrainedGraphBuilider.setGraphLevelTotalEdgeContribution();
                timeConstrainedGraphBuilider.setNumberOfGraphs();
                timeConstrainedGraphBuilider.setNodeContributionCountByObject();
                timeConstrainedGraphBuilider.setReachTimesByObject();
                timeConstrainedGraphBuilider.setClusteringCoefficient();
                MySequentialGraphVars.g.edRefs = null;
                MySequentialGraphVars.g.edgeRefMap = null;
            }

            for (String itemset : MySequentialGraphVars.itemToIdMap.keySet()) {
                MySequentialGraphVars.nodeNameMap.put(MySequentialGraphVars.itemToIdMap.get(itemset), itemset);
            }

        } catch (Exception ex) {ex.printStackTrace();}
        return MySequentialGraphVars.g;
    }
}
