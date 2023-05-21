package medousa.sequential.graph.flow;

import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import medousa.sequential.graph.MyDepthEdge;
import medousa.sequential.graph.MyDepthNode;
import medousa.sequential.graph.MyEdge;
import medousa.sequential.graph.MyNode;
import medousa.sequential.graph.layout.MyDirectedSparseMultigraph;
import medousa.sequential.graph.layout.MyFRLayout;
import medousa.sequential.utils.MyMathUtil;
import medousa.sequential.utils.MySequentialGraphSysUtil;
import org.apache.commons.collections15.Transformer;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.Collection;

import static java.awt.Cursor.HAND_CURSOR;

public class MyIntegratedGraph<V, E>
extends MyDirectedSparseMultigraph<V, E>
implements Serializable {

    private MyDirectedSparseMultigraph<MyDepthNode, MyDepthEdge> pathFlowGraph;
    public MyIntegratedGraph(MyDirectedSparseMultigraph<MyDepthNode, MyDepthEdge> pathFlowGraph) {
        this.pathFlowGraph = pathFlowGraph;

        Collection<MyDepthEdge> depthEdges = this.pathFlowGraph.getEdges();
        for (MyDepthEdge depthEdge : depthEdges) {
            String ps = depthEdge.getSource().getName().split("-")[0];
            String ss = depthEdge.getDest().getName().split("-")[0];

            String edgeName = ps + "-" + ss;
            if (!edRefs.contains(edgeName)) {
                MyNode p = null;
                if (!vRefs.containsKey(ps)) {
                    p = new MyNode(ps);
                    p.contribution = depthEdge.getSource().getContribution();
                    vRefs.put(ps, p);
                    addVertex((V)p);
                } else {
                    p = vRefs.get(ps);
                }

                MyNode s = null;
                if (!vRefs.containsKey(ss)) {
                    s = new MyNode(ss);
                    s.contribution = depthEdge.getDest().getContribution();
                    vRefs.put(ss, s);
                    addVertex((V)s);
                } else {
                    s = vRefs.get(ss);
                }

                MyEdge e = new MyEdge((int) depthEdge.getDest().getContribution(), p, s);
                edRefs.add(edgeName);
                addEdge((E)e, (V)p, (V)s);


            } else {
                vRefs.get(ss).contribution += depthEdge.getDest().getContribution();
            }
        }
    }

    public float getMaximumNodeValue() {
        float max = 0f;
        Collection<V> nodes = getVertices();
        for (Object o : nodes) {
            MyNode n = (MyNode) o;
            if (max < n.getContribution()) {
                max = n.getContribution();
            }
        }

        return max;
    }
}
