package medousa.sequential.graph;

import medousa.MyProgressBar;
import medousa.sequential.utils.MyMultiNodeUtil;
import medousa.sequential.utils.MySelectedNodeUtil;
import medousa.sequential.utils.MyViewerComponentControllerUtil;
import medousa.sequential.utils.MySequentialGraphVars;

import java.util.*;

public class MyNodeSearcher {

    public MyNodeSearcher() {}

    public synchronized void setEdgeNodes(MyNode source, MyNode dest) {
        if (MySequentialGraphVars.getSequentialGraphViewer().multiNodes != null && MySequentialGraphVars.getSequentialGraphViewer().multiNodes.contains(source) && MySequentialGraphVars.getSequentialGraphViewer().multiNodes.contains(dest)) {
            return;
        }

        MyProgressBar pb = new MyProgressBar(false);
        MySequentialGraphVars.getSequentialGraphViewer().multiNodes = new HashSet<>();
        MySequentialGraphVars.getSequentialGraphViewer().multiNodes.add(source);
        MySequentialGraphVars.getSequentialGraphViewer().multiNodes.add(dest);
        pb.updateValue(10, 100);

        MySequentialGraphVars.getSequentialGraphViewer().multiNodePredecessors.addAll(MySequentialGraphVars.g.getPredecessors(source));
        MySequentialGraphVars.getSequentialGraphViewer().multiNodePredecessors.addAll(MySequentialGraphVars.g.getPredecessors(dest));
        pb.updateValue(20, 100);

        MySequentialGraphVars.getSequentialGraphViewer().multiNodeSuccessors.addAll(MySequentialGraphVars.g.getSuccessors(source));
        MySequentialGraphVars.getSequentialGraphViewer().multiNodeSuccessors.addAll(MySequentialGraphVars.g.getSuccessors(dest));
        pb.updateValue(30, 100);

        MyViewerComponentControllerUtil.removeNodeBarChartsFromViewer();
        pb.updateValue(50, 100);

        MySequentialGraphVars.getSequentialGraphViewer().sharedPredecessors.addAll(MySequentialGraphVars.g.getPredecessors(source));
        MySequentialGraphVars.getSequentialGraphViewer().sharedPredecessors.retainAll(MySequentialGraphVars.g.getPredecessors(dest));

        pb.updateValue(50, 100);

        MySequentialGraphVars.getSequentialGraphViewer().sharedSuccessors.addAll(MySequentialGraphVars.g.getSuccessors(source));
        MySequentialGraphVars.getSequentialGraphViewer().sharedSuccessors.retainAll(MySequentialGraphVars.g.getSuccessors(dest));
        pb.updateValue(70, 100);

        MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueBarChart.setText("SHARED N. V. B.");
        MyMultiNodeUtil.adjustMultiNodeNeighborNodeValues();
        MySequentialGraphVars.getSequentialGraphViewer().vc.vTxtStat.setTextStatistics();
        pb.updateValue(90, 100);
        MySequentialGraphVars.sequentialGraphDashBoard.setMultiNodeDashBoard();
        MySequentialGraphVars.getSequentialGraphViewer().getRenderContext().setEdgeDrawPaintTransformer(MySequentialGraphVars.getSequentialGraphViewer().edgeColor);

        MySequentialGraphVars.app.getSequentialGraphDashboard().revalidate();
        MySequentialGraphVars.app.getSequentialGraphDashboard().repaint();
        MySequentialGraphVars.getSequentialGraphViewer().vc.graphRemovalPanel.setVisible(false);
        pb.updateValue(100, 100);
        pb.dispose();
    }

    public synchronized void setSelectedNodes(MyNode selectedNode) {
        if (MySequentialGraphVars.getSequentialGraphViewer().selectedNode != null &&
            selectedNode == MySequentialGraphVars.getSequentialGraphViewer().selectedNode) {return;}
        else if (MySequentialGraphVars.getSequentialGraphViewer().multiNodes != null &&
            MySequentialGraphVars.getSequentialGraphViewer().multiNodes.contains(selectedNode)) {return;}

        if (MySequentialGraphVars.getSequentialGraphViewer().selectedNode != null) {
            MySequentialGraphVars.getSequentialGraphViewer().viewerMouseListener.setMultiNodes(selectedNode);
        } else if (MySequentialGraphVars.getSequentialGraphViewer().multiNodes != null) {
            MySequentialGraphVars.getSequentialGraphViewer().multiNodes.add(selectedNode);
            MySequentialGraphVars.getSequentialGraphViewer().viewerMouseListener.setMultiNodes(MySequentialGraphVars.getSequentialGraphViewer().multiNodes);
        } else {
            MyProgressBar pb = new MyProgressBar(false);
            MySequentialGraphVars.getSequentialGraphViewer().selectedNode = selectedNode;
            MySequentialGraphVars.getSequentialGraphViewer().selectedSingleNodePredecessors = new HashSet<>(MySequentialGraphVars.g.getPredecessors(selectedNode));
            MySequentialGraphVars.getSequentialGraphViewer().selectedSingleNodeSuccessors = new HashSet<>(MySequentialGraphVars.g.getSuccessors(selectedNode));
            MySelectedNodeUtil.adjustSelectedNodeNeighborNodeValues(MySequentialGraphVars.getSequentialGraphViewer().selectedNode);
            MySequentialGraphVars.getSequentialGraphViewer().vc.vTxtStat.setTextStatistics();
            pb.updateValue(85, 100);
            MyViewerComponentControllerUtil.setDepthOptionForSelectedNode();
            pb.updateValue(90, 100);
            MyViewerComponentControllerUtil.removeNodeBarChartsFromViewer();
            MyViewerComponentControllerUtil.removeSharedNodeValueBarCharts();
            pb.updateValue(95, 100);
            MySequentialGraphVars.sequentialGraphDashBoard.setSingleNodeDashBoard();
            MySequentialGraphVars.getSequentialGraphViewer().getRenderContext().setEdgeDrawPaintTransformer(MySequentialGraphVars.getSequentialGraphViewer().edgeColor);
            pb.updateValue(97, 100);
            MySequentialGraphVars.getSequentialGraphViewer().vc.updateTableInfos();
            MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueBarChart.setText("P. & S. V. B.");

            pb.updateValue(100, 100);
            pb.dispose();

            MySequentialGraphVars.getSequentialGraphViewer().revalidate();
            MySequentialGraphVars.getSequentialGraphViewer().repaint();
        }
        MySequentialGraphVars.getSequentialGraphViewer().vc.graphRemovalPanel.setVisible(false);
    }


}
