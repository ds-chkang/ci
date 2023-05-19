package medousa.sequential.graph.funnel;

import medousa.sequential.graph.MyEdge;
import medousa.sequential.graph.MyNode;
import medousa.sequential.utils.MySequentialGraphVars;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

public class MyAnalysisGraphNodeMenu
extends JPopupMenu
implements ActionListener {

    private MyAnalysisGraphViewer funnelViewer;
    private MyNode currentSelectedNode;
    private JMenuItem deleteNode = new JMenuItem("DELETE NODE");
    private JMenuItem addAllSuccessors = new JMenuItem("ADD ALL SUCCESSORS");
    private JMenuItem addAllPredecessors = new JMenuItem("ADD ALL PREDECESSORS");

    public MyAnalysisGraphNodeMenu(MyAnalysisGraphViewer funnelViewer, MyNode currentSelectedNode) {
        this.funnelViewer = funnelViewer;
        this.currentSelectedNode = currentSelectedNode;
        this.decorate();
    }

    private void decorate() {
        //this.setMenuItem(this.addAllPredecessors);
        //this.add(new JSeparator());
        //this.setMenuItem(this.addAllSuccessors);
        //this.add(new JSeparator());
        this.setMenuItem(this.deleteNode);

    }

    private void setMenuItem(JMenuItem menuItem) {
        menuItem.setFont(MySequentialGraphVars.tahomaPlainFont12);
        menuItem.addActionListener(this);
        this.add(menuItem);
    }

    @Override public void actionPerformed(ActionEvent e) {
        new Thread(new Runnable() {
            @Override public void run() {
                if (e.getSource() == deleteNode) {
                    Collection<MyEdge> edges = funnelViewer.getGraphLayout().getGraph().getEdges();
                    if (edges != null) {
                        for (MyEdge edge : edges) {
                            if (edge.getSource() == currentSelectedNode) {
                                ((MyAnalysisGraph)funnelViewer.getGraphLayout().getGraph()).vRefs.remove(edge.getSource().getName());
                                ((MyAnalysisGraph)funnelViewer.getGraphLayout().getGraph()).edRefs.remove(edge.getSource().getName() + "-" + edge.getDest().getName());
                                funnelViewer.getGraphLayout().getGraph().removeVertex(edge.getSource());
                                funnelViewer.getGraphLayout().getGraph().removeEdge(edge);
                            } else if (edge.getDest() == currentSelectedNode) {
                                ((MyAnalysisGraph)funnelViewer.getGraphLayout().getGraph()).vRefs.remove(edge.getDest().getName());
                                ((MyAnalysisGraph)funnelViewer.getGraphLayout().getGraph()).edRefs.remove(edge.getSource().getName() + "-" + edge.getDest().getName());
                                funnelViewer.getGraphLayout().getGraph().removeVertex(edge.getDest());
                                funnelViewer.getGraphLayout().getGraph().removeEdge(edge);
                            }
                        }
                    }
                    ((MyAnalysisGraph) funnelViewer.getGraphLayout().getGraph()).vRefs.remove(currentSelectedNode.getName());
                    funnelViewer.getGraphLayout().getGraph().removeVertex(currentSelectedNode);
                    funnelViewer.graphMouseListener.selectedNode = null;
                }
                funnelViewer.revalidate();
                funnelViewer.repaint();
            }
        }).start();
    }
}
