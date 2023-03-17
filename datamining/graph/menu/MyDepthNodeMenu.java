package datamining.graph.menu;

import datamining.graph.MyNodeLister;
import datamining.graph.stats.depthnode.MyDepthLevelEdgeStatistics;
import datamining.graph.stats.depthnode.MyDepthNodeStatistics;
import datamining.utils.system.MyVars;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyDepthNodeMenu
extends JPopupMenu
implements ActionListener {

    private JMenuItem picking = new JMenuItem("PICKING");
    private JMenuItem tranform = new JMenuItem("TRANSORMING");
    private JMenuItem searchNode = new JMenuItem("SEARCH NODE");
    private JMenuItem nodeStatistics = new JMenuItem("DEPTH NODE STATISTICS");
    private JMenuItem edgeStatistics = new JMenuItem("DEPTH EDGE STATISTICS");
    //private JMenuItem nodeHopCountDistribution = new JMenuItem("AVG. NODE HOP DISTRIBUTION");

    public MyDepthNodeMenu( ) {
        this.decorate();
    }

    private void decorate() {
        this.setMenuItem(this.picking);
        this.add(new JSeparator());
        this.setMenuItem(this.tranform);
        this.add(new JSeparator());
        this.setMenuItem(this.searchNode);
        this.add(new JSeparator());
        this.setMenuItem(this.nodeStatistics);
        if (MyVars.getViewer().vc.depthSelecter.getSelectedIndex() > 0 && MyVars.getViewer().vc.depthNeighborNodeTypeSelector.getSelectedIndex() > 0) {
            if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() == 2) {
                this.add(new JSeparator());
                this.setMenuItem(this.edgeStatistics);
            }
        }
    }

    private void setMenuItem(JMenuItem menuItem) {
        menuItem.setFont(MyVars.tahomaPlainFont12);
        menuItem.addActionListener(this);
        menuItem.setPreferredSize(new Dimension(210, 26));
        this.add(menuItem);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.picking) {
            DefaultModalGraphMouse graphMouse = new DefaultModalGraphMouse();
            graphMouse.setMode(DefaultModalGraphMouse.Mode.PICKING);
            MyVars.getViewer().setGraphMouse(graphMouse);
        } else if (e.getSource() == this.tranform) {
            DefaultModalGraphMouse graphMouse = new DefaultModalGraphMouse();
            graphMouse.setMode(DefaultModalGraphMouse.Mode.TRANSFORMING);
            MyVars.getViewer().setGraphMouse(graphMouse);
        } else if (e.getSource() == this.searchNode) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override public void run() {
                    new Thread(new Runnable() {
                        @Override public void run() {
                            MyNodeLister nodeSearch = new MyNodeLister();
                        }
                    }).start();
                }
            });
        } else if (e.getSource() == this.nodeStatistics) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override public void run() {
                    new Thread(new Runnable() {
                        @Override public void run() {
                            MyDepthNodeStatistics depthNodeStatistics = new MyDepthNodeStatistics();
                        }
                    }).start();
                }
            });
        }  else if (e.getSource() == this.edgeStatistics) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override public void run() {
                    new Thread(new Runnable() {
                        @Override public void run() {
                            MyDepthLevelEdgeStatistics depthNodeStatistics = new MyDepthLevelEdgeStatistics();
                        }
                    }).start();
                }
            });
        }
    }
}
