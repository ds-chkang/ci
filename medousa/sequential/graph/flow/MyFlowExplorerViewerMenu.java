package medousa.sequential.graph.flow;

import edu.uci.ics.jung.visualization.DefaultVisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationModel;
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.Collection;

import static java.awt.Cursor.HAND_CURSOR;

public class MyFlowExplorerViewerMenu
extends JPopupMenu
implements ActionListener  {

    private JMenuItem showIntegratedGraph = new JMenuItem("INTEGRATED GRAPH");
    private MyDirectedSparseMultigraph<MyDepthNode, MyDepthEdge> pathFlowGraph;

    public MyFlowExplorerViewerMenu(MyDirectedSparseMultigraph<MyDepthNode, MyDepthEdge> pathFlowGraph) {
        this.pathFlowGraph = pathFlowGraph;
        this.add(this.showIntegratedGraph);
        this.showIntegratedGraph.addActionListener(this);
    }

    @Override public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.showIntegratedGraph) {
            new Thread(new Runnable() {
                @Override public void run() {
                    MyIntegratedGraph integratedGraph = new MyIntegratedGraph(pathFlowGraph);
                    MyFRLayout integratedLayout = new MyFRLayout<>(integratedGraph, new Dimension(1500, 1500));
                    MyIntegratedGraphViewer integratedViewer = new MyIntegratedGraphViewer(new DefaultVisualizationModel<>(integratedLayout, new Dimension(1500, 1500)));
                    integratedViewer.MAX_NODE_VALUE = integratedGraph.getMaximumNodeValue();

                    JFrame f = new JFrame("INTEGRATED GRAPH VIEW");
                    f.setBackground(Color.WHITE);
                    f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    f.setLayout(new BorderLayout(3,3));
                    f.getContentPane().add(integratedViewer);
                    f.setPreferredSize(new Dimension(600, 600));
                    f.pack();
                    f.setVisible(true);
                }
            }).start();
        }
    }
}
