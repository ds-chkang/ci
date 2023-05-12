package medousa.sequential.graph.menu;

import medousa.sequential.graph.MyNodeLister;
import medousa.sequential.graph.stats.depthnode.MyDepthLevelEdgeStatistics;
import medousa.sequential.graph.stats.depthnode.MyDepthNodeStatistics;
import medousa.sequential.utils.MyFontChooser;
import medousa.sequential.utils.MySequentialGraphVars;
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
    private JMenuItem nodeFont = new JMenuItem("NODE FONT");
    private JMenuItem edgeFont = new JMenuItem("EDGE FONT");
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
        if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthSelecter.getSelectedIndex() > 0 && MySequentialGraphVars.getSequentialGraphViewer().vc.depthNeighborNodeTypeSelector.getSelectedIndex() > 0) {
            if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() == 2) {
                this.add(new JSeparator());
                this.setMenuItem(this.edgeStatistics);
            }
        }
        this.add(new JSeparator());
        this.setMenuItem(this.nodeFont);
        this.setMenuItem(this.edgeFont);
    }

    private void setMenuItem(JMenuItem menuItem) {
        menuItem.setFont(MySequentialGraphVars.tahomaPlainFont12);
        menuItem.addActionListener(this);
        menuItem.setPreferredSize(new Dimension(210, 26));
        this.add(menuItem);
    }

    @Override public void actionPerformed(ActionEvent e) {
        new Thread(new Runnable() {
            @Override public void run() {
                if (e.getSource() == picking) {
                    DefaultModalGraphMouse graphMouse = new DefaultModalGraphMouse();
                    graphMouse.setMode(DefaultModalGraphMouse.Mode.PICKING);
                    MySequentialGraphVars.getSequentialGraphViewer().setGraphMouse(graphMouse);
                } else if (e.getSource() == tranform) {
                    DefaultModalGraphMouse graphMouse = new DefaultModalGraphMouse();
                    graphMouse.setMode(DefaultModalGraphMouse.Mode.TRANSFORMING);
                    MySequentialGraphVars.getSequentialGraphViewer().setGraphMouse(graphMouse);
                } else if (e.getSource() == searchNode) {
                    MyNodeLister nodeSearch = new MyNodeLister();
                } else if (e.getSource() == nodeStatistics) {
                    MyDepthNodeStatistics depthNodeStatistics = new MyDepthNodeStatistics();
                }  else if (e.getSource() == edgeStatistics) {
                    MyDepthLevelEdgeStatistics depthNodeStatistics = new MyDepthLevelEdgeStatistics();
                } else if (e.getSource() == nodeFont) {
                    MyFontChooser fd = new MyFontChooser(new JFrame("NODE FONT CHOOSER"));
                    fd.setVisible(true);
                } else if (e.getSource() == edgeFont) {
                    MyFontChooser fd = new MyFontChooser(new JFrame("EDGE FONT CHOOSER"));
                    fd.setVisible(true);
                }
            }
        }).start();
    }
}
