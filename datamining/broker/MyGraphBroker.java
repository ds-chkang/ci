package datamining.broker;

import datamining.main.MyProgressBar;
import datamining.graph.MyEdge;
import datamining.graph.MyBuilder;
import datamining.graph.MyNode;
import datamining.graph.MyTimeConstrainedBuilider;
import datamining.utils.system.MySysUtil;
import datamining.utils.system.MyVars;

import edu.uci.ics.jung.graph.Graph;

public class MyGraphBroker
extends MyEngineBroker {

    public MyGraphBroker() {
        super();
    }

    public Graph<MyNode, MyEdge> createGraph(MyProgressBar pb) {
        try {
            if (!MyVars.isTimeOn) {
                MyBuilder graphBuilder = new MyBuilder();
                graphBuilder.createNodes();
                graphBuilder.createEdges();
                graphBuilder.setEndNodeCount();
                graphBuilder.setNodeDepthInformation();
                graphBuilder.setOpenNodeCount();
                graphBuilder.setNodePropagationCount();
                graphBuilder.setNodeRecurrenceCount();
                graphBuilder.setNodeInContributionCount();
                graphBuilder.setNodeOutContributionCount();
                graphBuilder.setEngeUniqueContributionCount();
                graphBuilder.setTotalEdgeContribution();
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
                graphBuilder.setAverageUnWeightedGraphShortestPathLength(MyVars.g);
                graphBuilder.setAverageNodeRecurrenceLength();
                graphBuilder.setVariableNodeStrength();
                graphBuilder.setConnectance();
                graphBuilder.setEbsilonOfGraph();
                graphBuilder.setConnectedNetworkComponentCountByGraph();
                graphBuilder.cleanWork();
                MyVars.g.edRefs = null;
                MyVars.g.edgeRefMap = null;
            } else {
                MyTimeConstrainedBuilider timeConstrainedGraphBuilider = new MyTimeConstrainedBuilider();
                timeConstrainedGraphBuilider.createNodes();
                timeConstrainedGraphBuilider.createEdges();
                timeConstrainedGraphBuilider.setNodeDuration();
                timeConstrainedGraphBuilider.setEndNodeCount();
                timeConstrainedGraphBuilider.setNodeDepthInformation();
                timeConstrainedGraphBuilider.setOpenNodeCount();
                timeConstrainedGraphBuilider.setNodePropagationCount();
                timeConstrainedGraphBuilider.setDirectNodeRecurrenceCount();
                timeConstrainedGraphBuilider.setNodeInContributionCount();
                timeConstrainedGraphBuilider.setNodeOutContributionCount();
                timeConstrainedGraphBuilider.setEngeUniqueContributionCount();
                timeConstrainedGraphBuilider.setTotalEdgeContribution();
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
                timeConstrainedGraphBuilider.setEdgeReachTime();
                timeConstrainedGraphBuilider.setMaxNodeValue();
                timeConstrainedGraphBuilider.setAverageUnWeightedGraphShortestPathLength(MyVars.g);
                timeConstrainedGraphBuilider.setVariableNodeStrength();
                timeConstrainedGraphBuilider.setAverageNodeRecurrenceLength();
                timeConstrainedGraphBuilider.setConnectance();
                timeConstrainedGraphBuilider.setEbsilonOfGraph();
                timeConstrainedGraphBuilider.setConnectedNetworkComponentCountByGraph();
                timeConstrainedGraphBuilider.cleanWork();
                MyVars.g.edRefs = null;
                MyVars.g.edgeRefMap = null;
            }
            MySysUtil.setNodeNameMap();
        } catch (Exception ex) {ex.printStackTrace();}
        return MyVars.g;
    }
}
