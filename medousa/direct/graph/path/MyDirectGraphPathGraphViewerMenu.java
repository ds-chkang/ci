package medousa.direct.graph.path;

import medousa.direct.utils.MyDirectGraphVars;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyDirectGraphPathGraphViewerMenu
extends JPopupMenu
implements ActionListener {

    private MyDirectGraphPathGraphViewer betweenPathGraphViewer;
    private JMenuItem pick = new JMenuItem("PICK");
    private JMenuItem transform = new JMenuItem("TRANSFORM");
    private JMenuItem nodeContributionStatistics = new JMenuItem("NODE CONTRIBUTION STATISTICS");
    private JMenuItem edgeContributionStatistics = new JMenuItem("EDGE CONTRIBUTION STATISTICS");
    private JMenuItem showPathFlow = new JMenuItem("PATH FLOW");

    public MyDirectGraphPathGraphViewerMenu(MyDirectGraphPathGraphViewer betweenPathGraphViewer) {
        this.betweenPathGraphViewer = betweenPathGraphViewer;
        decorate();
    }

    private void decorate() {
        this.pick.setFont(MyDirectGraphVars.tahomaPlainFont12);
        this.pick.addActionListener(this);
        this.transform.setFont(MyDirectGraphVars.tahomaPlainFont12);
        this.transform.addActionListener(this);
        this.nodeContributionStatistics.setFont(MyDirectGraphVars.tahomaPlainFont12);
        this.nodeContributionStatistics.addActionListener(this);
        this.edgeContributionStatistics.setFont(MyDirectGraphVars.tahomaPlainFont12);
        this.edgeContributionStatistics.addActionListener(this);
        this.showPathFlow.setFont(MyDirectGraphVars.tahomaPlainFont12);
        this.showPathFlow.addActionListener(this);

        this.add(pick);
        this.add(new JSeparator());
        this.add(transform);
        this.add(new JSeparator());
        this.add(nodeContributionStatistics);
        this.add(new JSeparator());
        this.add(edgeContributionStatistics);
        //this.add(new JSeparator());
        //this.add(showPathFlow);

        this.betweenPathGraphViewer.revalidate();
        this.betweenPathGraphViewer.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        new Thread(new Runnable() {
            @Override public void run() {
                if (e.getSource() == pick) {
                    DefaultModalGraphMouse graphMouse = new DefaultModalGraphMouse();
                    graphMouse.setMode(DefaultModalGraphMouse.Mode.PICKING);
                    betweenPathGraphViewer.setGraphMouse(graphMouse);
                } else if (e.getSource() == transform) {
                    DefaultModalGraphMouse graphMouse = new DefaultModalGraphMouse();
                    graphMouse.setMode(DefaultModalGraphMouse.Mode.TRANSFORMING);
                    betweenPathGraphViewer.setGraphMouse(graphMouse);
                } //else if (e.getSource() == showPathFlow) {
                  //  MyFlowExplorerAnalyzer pathFlowAnalyzer = new MyFlowExplorerAnalyzer();
                  //  pathFlowAnalyzer.showBetweenPathFlows(betweenPathGraphViewer.betweenPathGraphDepthFirstSearch.mxDepth);
                //}
                betweenPathGraphViewer.revalidate();
                betweenPathGraphViewer.repaint();
            }
        }).start();
    }

}


