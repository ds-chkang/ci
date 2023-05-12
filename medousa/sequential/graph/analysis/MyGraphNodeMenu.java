package medousa.sequential.graph.analysis;

import medousa.sequential.graph.MyEdge;
import medousa.sequential.graph.MyNode;
import medousa.sequential.utils.MySequentialGraphVars;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

public class MyGraphNodeMenu
extends JPopupMenu
implements ActionListener {

    private MyGraphAnalyzerViewer funnelViewer;
    private MyNode currentSelectedNode;
    private JMenuItem deleteNode = new JMenuItem("DELETE NODE");
    private JMenuItem addAllSuccessors = new JMenuItem("ADD ALL SUCCESSORS");
    private JMenuItem addAllPredecessors = new JMenuItem("ADD ALL PREDECESSORS");

    public MyGraphNodeMenu(MyGraphAnalyzerViewer funnelViewer, MyNode currentSelectedNode) {
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

    @Override
    public void actionPerformed(ActionEvent e) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (e.getSource() == deleteNode) {
                    Collection<MyEdge> edges = funnelViewer.getGraphLayout().getGraph().getEdges();
                    if (edges != null) {
                        for (MyEdge edge : edges) {
                            if (edge.getSource() == currentSelectedNode || edge.getDest() == currentSelectedNode) {
                                funnelViewer.getGraphLayout().getGraph().removeEdge(edge);
                            }
                        }
                    }
                    ((MyGraph)funnelViewer.getGraphLayout().getGraph()).vRefs.remove(currentSelectedNode.getName());
                    funnelViewer.getGraphLayout().getGraph().removeVertex(currentSelectedNode);
                    funnelViewer.graphMouseListener.selectedNode = null;
                }
                funnelViewer.revalidate();
                funnelViewer.repaint();
            }
        }).start();
    }
}
