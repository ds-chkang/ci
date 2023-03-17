package datamining.graph.menu;

import datamining.graph.MyFlowExplorerAnalyzer;
import datamining.graph.stats.MyNodeDepthDistributionChart;
import datamining.graph.stats.MyPredecessorStatistics;
import datamining.graph.stats.MySuccessorStatistics;
import datamining.graph.stats.MyVariableStatisticsByNode;
import datamining.utils.system.MyVars;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MySingleNodeMenu
extends JPopupMenu
implements ActionListener {

    private JMenuItem showRelatedVariables = new JMenuItem("SHOW VARIABLE STATISTICS");
    private JMenuItem predecessorStatistics = new JMenuItem("PREDECESSOR STATISTICS");
    private JMenuItem showPredecessorsOnly = new JMenuItem("SHOW PREDECESSORS ONLY");
    private JMenuItem showSuccessorsOnly = new JMenuItem("SHOW SUCCESSORS ONLY");
    private JMenuItem showNeighbors = new JMenuItem("SHOW PREDECESSORS & SUCCESSORS");
    private JMenuItem successorStatistics = new JMenuItem("SUCCESSOR STATISTICS");
    private JMenuItem nodeDepthDistribution = new JMenuItem("NODE DEPTH DISTRIBUTION");
    private JMenu download = new JMenu("DOWNLOAD");
    private JMenuItem downloadUsers = new JMenuItem("OBJECT ID LIST");
    private JMenuItem nodePathFlow = new JMenuItem("PATH FLOW");

    public MySingleNodeMenu( ) {
        this.decorate();
    }

    private void decorate() {
        if (MyVars.isSupplementaryOn) {
            if (!MyVars.getViewer().selectedNode.getName().contains("x")) {
                this.setMenuItem(null, this.showRelatedVariables);
                this.add(new JSeparator());
            }
        }
        this.setMenuItem(null, this.predecessorStatistics);
        this.add(new JSeparator());
        this.setMenuItem(null, this.successorStatistics);
        this.add(new JSeparator());
        this.setMenuItem(null, this.showPredecessorsOnly);
        this.add(new JSeparator());
        this.setMenuItem(null, this.showSuccessorsOnly);
        this.add(new JSeparator());
        this.setMenuItem(null, this.showNeighbors);
        this.add(new JSeparator());
        this.setMenuItem(null, this.nodeDepthDistribution);
        this.add(new JSeparator());
        this.setMenuItem(null, this.download);
        this.setMenuItem(this.download, this.downloadUsers);
        this.add(new JSeparator());
        this.setMenuItem(null, this.nodePathFlow);
        this.download.setEnabled(false);
    }

    private void setMenuItem(JMenu root, JMenuItem menuItem) {
        menuItem.setFont(MyVars.tahomaPlainFont12);
        menuItem.addActionListener(this);
        if (root == null) {
            this.add(menuItem);
        } else {
            root.add(menuItem);
        }
        menuItem.setPreferredSize(new Dimension(240, 26));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (e.getSource() == showRelatedVariables) {
                    MyVariableStatisticsByNode variableStatisticsByNode = new MyVariableStatisticsByNode();
                } else if (e.getSource() == predecessorStatistics) {
                    MyPredecessorStatistics predecessorStatistics = new MyPredecessorStatistics();
                } else if (e.getSource() == successorStatistics) {
                    MySuccessorStatistics successorStatistics = new MySuccessorStatistics();
                } else if (e.getSource() == nodeDepthDistribution) {
                    MyNodeDepthDistributionChart nodeDepthDistributionChart = new MyNodeDepthDistributionChart();
                    nodeDepthDistributionChart.enlarge();
                } else if (e.getSource() == showPredecessorsOnly) {
                    MyVars.getViewer().predecessorsOnly = true;
                    MyVars.getViewer().successorsOnly = false;
                    MyVars.getViewer().predecessorsAndSuccessors = false;
                    MyVars.getViewer().revalidate();
                    MyVars.getViewer().repaint();
                } else if (e.getSource() == showSuccessorsOnly) {
                    MyVars.getViewer().predecessorsOnly = false;
                    MyVars.getViewer().successorsOnly = true;
                    MyVars.getViewer().predecessorsAndSuccessors = false;
                    MyVars.getViewer().revalidate();
                    MyVars.getViewer().repaint();
                } else if (e.getSource() == showNeighbors) {
                    MyVars.getViewer().predecessorsOnly = false;
                    MyVars.getViewer().successorsOnly = false;
                    MyVars.getViewer().predecessorsAndSuccessors = true;
                    MyVars.getViewer().revalidate();
                    MyVars.getViewer().repaint();
                } else if (e.getSource() == nodePathFlow) {
                    MyFlowExplorerAnalyzer pathFlowAnalyzer = new MyFlowExplorerAnalyzer();
                    pathFlowAnalyzer.showNodePathFlows();
                }
            }
        }).start();
    }
}
