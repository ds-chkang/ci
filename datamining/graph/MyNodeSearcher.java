package datamining.graph;

import datamining.main.MyProgressBar;
import datamining.utils.MyMultiNodeUtil;
import datamining.utils.MySelectedNodeUtil;
import datamining.utils.MyViewerControlComponentUtil;
import datamining.utils.system.MySysUtil;
import datamining.utils.system.MyVars;

import java.util.*;

public class MyNodeSearcher {

    public MyNodeSearcher() {}

    public void setEdgeNodes(MyNode source, MyNode dest) {
        if (MyVars.getViewer().multiNodes != null && MyVars.getViewer().multiNodes.contains(source) && MyVars.getViewer().multiNodes.contains(dest)) {
            //MyMessageUtil.showInfoMsg(MyVars.app, "Select a different pair of source and dest nodes.");
            return;
        }

        MyProgressBar pb = new MyProgressBar(false);
        MyVars.getViewer().multiNodes = new HashSet<>();
        MyVars.getViewer().multiNodes.add(source);
        MyVars.getViewer().multiNodes.add(dest);
        pb.updateValue(10, 100);

        MyVars.getViewer().multiNodePredecessors.addAll(MyVars.g.getPredecessors(source));
        MyVars.getViewer().multiNodePredecessors.addAll(MyVars.g.getPredecessors(dest));
        pb.updateValue(20, 100);

        MyVars.getViewer().multiNodeSuccessors.addAll(MyVars.g.getSuccessors(source));
        MyVars.getViewer().multiNodeSuccessors.addAll(MyVars.g.getSuccessors(dest));
        pb.updateValue(30, 100);

        MyViewerControlComponentUtil.removeBarChartsFromViewer();
        pb.updateValue(50, 100);

        MyVars.getViewer().sharedPredecessors.addAll(MyVars.g.getPredecessors(source));
        MyVars.getViewer().sharedPredecessors.retainAll(MyVars.g.getPredecessors(dest));

        pb.updateValue(50, 100);

        MyVars.getViewer().sharedSuccessors.addAll(MyVars.g.getSuccessors(source));
        MyVars.getViewer().sharedSuccessors.retainAll(MyVars.g.getSuccessors(dest));
        pb.updateValue(70, 100);

        MyVars.getViewer().vc.nodeValueBarChart.setText("SHARED N. V. B.");
        MyMultiNodeUtil.adjustMultiNodeNeighborNodeValues();
        MyVars.getViewer().vc.vTxtStat.setTextStatistics();
        pb.updateValue(90, 100);
        MyVars.dashBoard.setMultiNodeDashBoard();
        MyVars.getViewer().getRenderContext().setEdgeDrawPaintTransformer(MyVars.getViewer().edgeColor);

        MyVars.app.getDashboard().revalidate();
        MyVars.app.getDashboard().repaint();
        pb.updateValue(100, 100);
        pb.dispose();
    }

    public void setSelectedNodes(MyNode selectedNode) {
        if (MyVars.getViewer().selectedNode != null && selectedNode == MyVars.getViewer().selectedNode) {return;}
        else if (MyVars.getViewer().multiNodes != null && MyVars.getViewer().multiNodes.contains(selectedNode)) {return;}

        if (MyVars.getViewer().selectedNode != null) {
            MyProgressBar pb = new MyProgressBar(false);
            pb.updateValue(10, 100);
            MyVars.getViewer().multiNodes = new HashSet<>();
            MyVars.getViewer().multiNodes.add(MyVars.getViewer().selectedNode);
            pb.updateValue(20, 100);
            MyVars.getViewer().multiNodes.add(selectedNode);
            pb.updateValue(30, 100);
            MyVars.getViewer().multiNodePredecessors.addAll(MyVars.g.getPredecessors(selectedNode));
            pb.updateValue(40, 100);
            MyVars.getViewer().multiNodeSuccessors.addAll(MyVars.g.getSuccessors(selectedNode));
            pb.updateValue(50, 100);
            MyVars.getViewer().multiNodePredecessors.addAll(MyVars.getViewer().selectedSingleNodePredecessors);
            pb.updateValue(60, 100);
            MyVars.getViewer().multiNodeSuccessors.addAll(MyVars.getViewer().selectedSingleNodeSuccessors);
            pb.updateValue(70, 100);
            MyVars.getViewer().selectedSingleNodeSuccessors = new HashSet<>();
            pb.updateValue(80, 100);
            MyVars.getViewer().selectedSingleNodePredecessors = new HashSet<>();
            pb.updateValue(90, 100);
            MyViewerControlComponentUtil.removeBarChartsFromViewer();
            MySysUtil.setSharedPredecessors(MyVars.getViewer().selectedNode, selectedNode);
            MySysUtil.setSharedSuccessors(MyVars.getViewer().selectedNode, selectedNode);
            MyVars.getViewer().selectedNode = null;
            MyMultiNodeUtil.adjustMultiNodeNeighborNodeValues();
            MyVars.getViewer().vc.updateTableInfos();
            MyVars.getViewer().vc.vTxtStat.setTextStatistics();
            MyVars.dashBoard.setMultiNodeDashBoard();
            MyVars.getViewer().getRenderContext().setEdgeDrawPaintTransformer(MyVars.getViewer().edgeColor);
            MyVars.getViewer().vc.nodeValueBarChart.setText("SHARED N. V. B.");

            pb.updateValue(100, 100);
            pb.dispose();
        } else if (MyVars.getViewer().multiNodes != null) {
            MyProgressBar pb = new MyProgressBar(false);
            MyVars.getViewer().multiNodes.add(selectedNode);
            pb.updateValue(10, 100);
            MyVars.getViewer().multiNodePredecessors.addAll(MyVars.g.getPredecessors(selectedNode));
            pb.updateValue(20, 100);
            MyVars.getViewer().multiNodeSuccessors.addAll(MyVars.g.getSuccessors(selectedNode));
            pb.updateValue(40, 100);
            MyViewerControlComponentUtil.removeBarChartsFromViewer();
            pb.updateValue(50, 100);
            MySysUtil.setSharedPredecessors(selectedNode);
            MySysUtil.setSharedSuccessors(selectedNode);
            pb.updateValue(80, 100);
            MyMultiNodeUtil.adjustMultiNodeNeighborNodeValues();
            MyVars.getViewer().vc.updateTableInfos();
            MyVars.getViewer().vc.nodeValueBarChart.setText("SHARED N. V. B.");
            MyVars.getViewer().vc.vTxtStat.setTextStatistics();
            MyVars.dashBoard.setMultiNodeDashBoard();
            MyVars.getViewer().getRenderContext().setEdgeDrawPaintTransformer(MyVars.getViewer().edgeColor);
            pb.updateValue(100, 100);
            pb.dispose();

        } else {
            MyProgressBar pb = new MyProgressBar(false);
            MyVars.getViewer().selectedNode = selectedNode;
            MyVars.getViewer().selectedSingleNodePredecessors = new HashSet<>(MyVars.g.getPredecessors(selectedNode));
            MyVars.getViewer().selectedSingleNodeSuccessors = new HashSet<>(MyVars.g.getSuccessors(selectedNode));

            MySelectedNodeUtil.adjustSelectedNodeNeighborNodeValues(MyVars.getViewer().selectedNode);

            MyVars.getViewer().vc.vTxtStat.setTextStatistics();
            pb.updateValue(85, 100);

            MyViewerControlComponentUtil.setDepthOptionForSelectedNode();
            pb.updateValue(90, 100);

            MyViewerControlComponentUtil.removeBarChartsFromViewer();
            MyViewerControlComponentUtil.removeSharedNodeValueBarCharts();
            pb.updateValue(95, 100);

            MyVars.dashBoard.setSingleNodeDashBoard();
            MyVars.getViewer().getRenderContext().setEdgeDrawPaintTransformer(MyVars.getViewer().edgeColor);
            pb.updateValue(97, 100);

            MyVars.getViewer().vc.updateTableInfos();
            //MyVars.getViewer().getRenderContext().setEdgeDrawPaintTransformer(MyVars.getViewer().edgeColor);
            MyVars.getViewer().vc.nodeValueBarChart.setText("P. & S. V. B.");

            MyVars.getViewer().revalidate();
            MyVars.getViewer().repaint();
            pb.updateValue(100, 100);
            pb.dispose();
        }
    }


}
