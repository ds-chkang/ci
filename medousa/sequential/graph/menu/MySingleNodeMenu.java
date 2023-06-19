package medousa.sequential.graph.menu;

import medousa.MyProgressBar;
import medousa.sequential.graph.flow.MyFlowExplorerAnalyzer;
import medousa.sequential.graph.MyNode;
import medousa.sequential.graph.stats.*;
import medousa.sequential.utils.MyFontChooser;
import medousa.sequential.utils.MySequentialGraphVars;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

public class MySingleNodeMenu
extends JPopupMenu
implements ActionListener {

    private JMenuItem predecessorStatistics = new JMenuItem("PREDECESSOR STATISTICS");
    private JMenuItem showPredecessorsOnly = new JMenuItem("SHOW PREDECESSORS ONLY");
    private JMenuItem showSuccessorsOnly = new JMenuItem("SHOW SUCCESSORS ONLY");
    private JMenuItem showNeighbors = new JMenuItem("SHOW PREDECESSORS & SUCCESSORS");
    private JMenuItem successorStatistics = new JMenuItem("SUCCESSOR STATISTICS");
    private JMenu download = new JMenu("DOWNLOAD");
    private JMenuItem downloadUsers = new JMenuItem("OBJECT ID LIST");
    private JMenuItem fromPathFlow = new JMenuItem("FROM PATH FLOW");
    private JMenuItem toPathFlow = new JMenuItem("TO PATH FLOW");
    private JMenuItem betweenTimeDistribution = new JMenuItem("BETWEEN TIME DISTRIBUTION");
    private JMenuItem nodeFont = new JMenuItem("NODE FONT");
    private JMenuItem edgeFont = new JMenuItem("EDGE FONT");
    private JMenuItem contributionDistributionNyObject = new JMenuItem("CONTRIBUTION COUNT DISTRIBUTION BY OBJECT");

    public MySingleNodeMenu( ) {
        this.decorate();
    }

    private void decorate() {
        this.setMenuItem(null, this.predecessorStatistics, "PREDECESSOR STATISTICS");
        this.setMenuItem(null, this.successorStatistics, "SUCCESSOR STATISTICS");
        this.add(new JSeparator());
        this.setMenuItem(null, this.contributionDistributionNyObject, "CONTRIBUTION COUNT BY OBJECT DISTRIBUTION");
        this.add(new JSeparator());
        this.setMenuItem(null, this.showPredecessorsOnly, "PREDECESSORS ONLY VIEW");
        this.setMenuItem(null, this.showSuccessorsOnly, "SUCCESSORS ONLY VIEW");
        this.setMenuItem(null, this.showNeighbors, "NEIGHBOR NDOES");
        this.add(new JSeparator());
        this.setMenuItem(null, this.download, "DOWNLOAD");
        this.setMenuItem(this.download, this.downloadUsers, "DOWNLOAD USERS");
        if (MySequentialGraphVars.g.getSuccessorCount(MySequentialGraphVars.getSequentialGraphViewer().singleNode) > 0) {
            this.add(new JSeparator());
            this.setMenuItem(null, this.fromPathFlow, "FROM PATH FLOW");
        }
        if (MySequentialGraphVars.g.getPredecessorCount(MySequentialGraphVars.getSequentialGraphViewer().singleNode) > 0) {
            this.setMenuItem(null, this.toPathFlow, "TO PATH FLOW");
        }
       // if (MySequentialGraphVars.isTimeOn) {
       //     this.setMenuItem(null, this.betweenTimeDistribution, "TIME DISTRIBUTION");
       // }
        this.add(new JSeparator());
        this.setMenuItem(null, this.nodeFont, "NODE FONT SETUP DIALOG");
        this.setMenuItem(null, this.edgeFont, "EDGE FONT SETUP DIALOG");
        this.download.setEnabled(false);
    }

    private void setMenuItem(JMenu root, JMenuItem menuItem, String tooltip) {
        menuItem.setFont(MySequentialGraphVars.tahomaPlainFont12);
        menuItem.addActionListener(this);
        if (root == null) {
            this.add(menuItem);
        } else {
            root.add(menuItem);
        }
        menuItem.setPreferredSize(new Dimension(285, 26));
    }

    @Override public void actionPerformed(ActionEvent e) {
        new Thread(new Runnable() {
            @Override public void run() {
                if (e.getSource() == contributionDistributionNyObject) {
                    MyGraphLevelNodeContributionCountByObjectDistribution graphLevelContributionCountByObjectIDDistribution = new MyGraphLevelNodeContributionCountByObjectDistribution();
                    graphLevelContributionCountByObjectIDDistribution.enlarge();
                } else if (e.getSource() == predecessorStatistics) {
                    MyPredecessorStatistics predecessorStatistics = new MyPredecessorStatistics();
                } else if (e.getSource() == successorStatistics) {
                    MySuccessorStatistics successorStatistics = new MySuccessorStatistics();
                } else if (e.getSource() == showPredecessorsOnly) {
                    MySequentialGraphVars.getSequentialGraphViewer().predecessorsOnly = true;
                    MySequentialGraphVars.getSequentialGraphViewer().successorsOnly = false;
                    MySequentialGraphVars.getSequentialGraphViewer().neighborsOnly = false;
                    setOnlyPredecessorNodes();
                    MySequentialGraphVars.getSequentialGraphViewer().vc.updateTableInfos();
                    MySequentialGraphVars.getSequentialGraphViewer().vc.currentNodeListTable.revalidate();
                    MySequentialGraphVars.getSequentialGraphViewer().vc.currentNodeListTable.repaint();
                    MySequentialGraphVars.getSequentialGraphViewer().revalidate();
                    MySequentialGraphVars.getSequentialGraphViewer().repaint();
                } else if (e.getSource() == showSuccessorsOnly) {
                    MySequentialGraphVars.getSequentialGraphViewer().predecessorsOnly = false;
                    MySequentialGraphVars.getSequentialGraphViewer().successorsOnly = true;
                    MySequentialGraphVars.getSequentialGraphViewer().neighborsOnly = false;
                    setOnlySuccessorNodes();
                    MySequentialGraphVars.getSequentialGraphViewer().vc.updateTableInfos();
                    MySequentialGraphVars.getSequentialGraphViewer().vc.currentNodeListTable.revalidate();
                    MySequentialGraphVars.getSequentialGraphViewer().vc.currentNodeListTable.repaint();
                    MySequentialGraphVars.getSequentialGraphViewer().revalidate();
                    MySequentialGraphVars.getSequentialGraphViewer().repaint();
                } else if (e.getSource() == showNeighbors) {
                    MySequentialGraphVars.getSequentialGraphViewer().predecessorsOnly = false;
                    MySequentialGraphVars.getSequentialGraphViewer().successorsOnly = false;
                    MySequentialGraphVars.getSequentialGraphViewer().neighborsOnly = true;
                    setNeighborNodes();
                    MySequentialGraphVars.getSequentialGraphViewer().vc.updateTableInfos();
                    MySequentialGraphVars.getSequentialGraphViewer().vc.currentNodeListTable.revalidate();
                    MySequentialGraphVars.getSequentialGraphViewer().vc.currentNodeListTable.repaint();
                    MySequentialGraphVars.getSequentialGraphViewer().revalidate();
                    MySequentialGraphVars.getSequentialGraphViewer().repaint();
                } else if (e.getSource() == fromPathFlow) {
                    MyProgressBar pb = new MyProgressBar(false);
                    try {
                        MyFlowExplorerAnalyzer pathFlowAnalyzer = new MyFlowExplorerAnalyzer();
                        pathFlowAnalyzer.showNodeFromPathFlows();
                        pb.updateValue(100, 100);
                        pb.dispose();
                    } catch (Exception ex) {
                        MyFlowExplorerAnalyzer pathFlowAnalyzer = new MyFlowExplorerAnalyzer();
                        pathFlowAnalyzer.showNodeFromPathFlows();
                        pb.updateValue(100, 100);
                        pb.dispose();
                    }
                } else if (e.getSource() == toPathFlow) {
                    MyProgressBar pb = new MyProgressBar(false);
                    try {
                        MyFlowExplorerAnalyzer pathFlowAnalyzer = new MyFlowExplorerAnalyzer();
                        pathFlowAnalyzer.showNodeToPathFlows();
                        pb.updateValue(100, 100);
                        pb.dispose();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        MyFlowExplorerAnalyzer pathFlowAnalyzer = new MyFlowExplorerAnalyzer();
                        pathFlowAnalyzer.showNodeToPathFlows();
                        pb.updateValue(100, 100);
                        pb.dispose();
                    }
                } else if (e.getSource() == nodeFont) {
                    MyProgressBar pb = new MyProgressBar(false);
                    try {
                        MyFontChooser fd = new MyFontChooser(new JFrame("NODE FONT CHOOSER"));
                        fd.setVisible(true);
                        pb.updateValue(100, 100);
                        pb.dispose();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        MyFontChooser fd = new MyFontChooser(new JFrame("NODE FONT CHOOSER"));
                        fd.setVisible(true);
                        pb.updateValue(100, 100);
                        pb.dispose();
                    }
                } else if (e.getSource() == edgeFont) {
                    MyProgressBar pb = new MyProgressBar(false);
                    try {
                        MyFontChooser fd = new MyFontChooser(new JFrame("EDGE FONT CHOOSER"));
                        fd.setVisible(true);
                        pb.updateValue(100, 100);
                        pb.dispose();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        MyFontChooser fd = new MyFontChooser(new JFrame("EDGE FONT CHOOSER"));
                        fd.setVisible(true);
                        pb.updateValue(100, 100);
                        pb.dispose();
                    }
                }
            }
        }).start();
    }

    private void setOnlyPredecessorNodes() {
        Collection<MyNode> neighbors = MySequentialGraphVars.g.getNeighbors(MySequentialGraphVars.getSequentialGraphViewer().singleNode);
        for (MyNode n : neighbors) {
            if (MySequentialGraphVars.g.getSuccessors(MySequentialGraphVars.getSequentialGraphViewer().singleNode).contains(n)) {
                if (n.getCurrentValue() != 0) {
                    n.setOriginalValue(n.getCurrentValue());
                    n.setCurrentValue(0);
                }
            } else if (n.getCurrentValue() == 0) {
                n.setCurrentValue(n.getOriginalValue());
            }
        }
    }

    private void setOnlySuccessorNodes() {
        Collection<MyNode> neighbors = MySequentialGraphVars.g.getNeighbors(MySequentialGraphVars.getSequentialGraphViewer().singleNode);
        for (MyNode n : neighbors) {
            if (MySequentialGraphVars.g.getPredecessors(MySequentialGraphVars.getSequentialGraphViewer().singleNode).contains(n)) {
                if (n.getCurrentValue() != 0) {
                    n.setOriginalValue(n.getCurrentValue());
                    n.setCurrentValue(0);
                }
            } else if (n.getCurrentValue() == 0) {
                n.setCurrentValue(n.getOriginalValue());
            }
        }
    }

    private void setNeighborNodes() {
        Collection<MyNode> neighbors = MySequentialGraphVars.g.getNeighbors(MySequentialGraphVars.getSequentialGraphViewer().singleNode);
        for (MyNode n : neighbors) {
            if (n.getCurrentValue() == 0) {
                n.setCurrentValue(n.getOriginalValue());
            }
        }
    }
}
