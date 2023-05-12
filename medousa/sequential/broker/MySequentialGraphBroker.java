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

    public Graph<MyNode, MyEdge> createGraph(MyProgressBar pb) {
        try {
            if (!MySequentialGraphVars.isTimeOn) {
                MyGraphBuilder graphBuilder = new MyGraphBuilder();
                graphBuilder.createNodes();
                graphBuilder.createEdges();
                graphBuilder.setEndNodeCount();
                graphBuilder.setNodeDepthInformation();
                graphBuilder.setOpenNodeCount();
                graphBuilder.setNodePropagationCount();
                graphBuilder.setNodeRecursiveLength();
                graphBuilder.setNodeRecurrenceCount();
                graphBuilder.setNodeInContributionCount();
                graphBuilder.setNodeOutContributionCount();
                graphBuilder.setNodeUniqueContribution();
                graphBuilder.setEdgeContribution();
                graphBuilder.setEngeUniqueContribution();
                graphBuilder.setEdgeSupport();
                graphBuilder.setEdgeConfidence();
                graphBuilder.setEdgeLift();
                graphBuilder.setNodeBetweenessCentrality();
                graphBuilder.setNodeClosenessCentrality();
                graphBuilder.setNodeEigenVectorCentrality();
                graphBuilder.setEdgeValues();
                graphBuilder.setEdgeTimeValues();
                graphBuilder.setEdgeLabels();
                graphBuilder.setNodeValues();
                graphBuilder.setNodeLabels();
                graphBuilder.setMaxNodeValue();
                graphBuilder.setAverageUnWeightedGraphShortestPathLength(MySequentialGraphVars.g);
                graphBuilder.setAverageNodeRecurrenceLength();
                graphBuilder.setVariableNodeStrength();
                graphBuilder.setConnectance();
                graphBuilder.setEbsilonOfGraph();
                graphBuilder.setConnectedNetworkComponentCountByGraph();
                graphBuilder.setGraphLevelTotalEdgeContribution();
                graphBuilder.setNumberOfGraphs();
                MySequentialGraphVars.g.edRefs = null;
                MySequentialGraphVars.g.edgeRefMap = null;
            } else {
                MyTimeConstrainedBuilider timeConstrainedGraphBuilider = new MyTimeConstrainedBuilider();
                timeConstrainedGraphBuilider.createGraph();
                timeConstrainedGraphBuilider.setNodeDuration();
                timeConstrainedGraphBuilider.setEndNodeCount();
                timeConstrainedGraphBuilider.setNodeDepthInformation();
                timeConstrainedGraphBuilider.setOpenNodeCount();
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
                timeConstrainedGraphBuilider.setNodeAverageTime();
                timeConstrainedGraphBuilider.setEdgeTotalAndAverageTime();
                timeConstrainedGraphBuilider.setMaxNodeValue();
                timeConstrainedGraphBuilider.setAverageShortestDistance(MySequentialGraphVars.g);
                timeConstrainedGraphBuilider.setVariableNodeStrength();
                timeConstrainedGraphBuilider.setAverageNodeRecurrenceLength();
                timeConstrainedGraphBuilider.setConnectance();
                timeConstrainedGraphBuilider.setEbsilonOfGraph();
                timeConstrainedGraphBuilider.setConnectedNetworkComponentCountByGraph();
                timeConstrainedGraphBuilider.setGraphLevelTotalEdgeContribution();
                timeConstrainedGraphBuilider.setNumberOfGraphs();
                MySequentialGraphVars.g.edRefs = null;
                MySequentialGraphVars.g.edgeRefMap = null;
            }
            //System.out.println(MySequentialGraphVars.sequenceFileName);
            MySequentialGraphSysUtil.setNodeNameMap();
        } catch (Exception ex) {ex.printStackTrace();}
        return MySequentialGraphVars.g;
    }
}
