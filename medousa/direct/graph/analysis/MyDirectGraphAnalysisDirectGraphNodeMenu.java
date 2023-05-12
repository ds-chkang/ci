package medousa.direct.graph.analysis;

import medousa.direct.graph.MyDirectEdge;
import medousa.direct.graph.MyDirectNode;
import medousa.direct.utils.MyDirectGraphVars;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

public class MyDirectGraphAnalysisDirectGraphNodeMenu
extends JPopupMenu
implements ActionListener {

    private MyDirectGraphAnalysisDirectGraphViewer funnelViewer;
    private MyDirectNode currentSelectedNode;
    private JMenuItem deleteNode = new JMenuItem("DELETE NODE");
    private JMenuItem addAllSuccessors = new JMenuItem("ADD ALL SUCCESSORS");
    private JMenuItem addAllPredecessors = new JMenuItem("ADD ALL PREDECESSORS");

    public MyDirectGraphAnalysisDirectGraphNodeMenu(MyDirectGraphAnalysisDirectGraphViewer funnelViewer, MyDirectNode currentSelectedNode) {
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
        menuItem.setFont(MyDirectGraphVars.tahomaPlainFont12);
        menuItem.addActionListener(this);
        this.add(menuItem);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (e.getSource() == deleteNode) {
                    Collection<MyDirectEdge> edges = funnelViewer.getGraphLayout().getGraph().getEdges();
                    if (edges != null) {
                        for (MyDirectEdge edge : edges) {
                            if (edge.getSource() == currentSelectedNode || edge.getDest() == currentSelectedNode) {
                                funnelViewer.getGraphLayout().getGraph().removeEdge(edge);
                            }
                        }
                    }
                    ((MyDirectGraphAnalysisGraph)funnelViewer.getGraphLayout().getGraph()).vRefs.remove(currentSelectedNode.getName());
                    funnelViewer.getGraphLayout().getGraph().removeVertex(currentSelectedNode);
                    funnelViewer.graphMouseListener.selectedNode = null;
                }
                funnelViewer.revalidate();
                funnelViewer.repaint();
            }
        }).start();
    }
}
