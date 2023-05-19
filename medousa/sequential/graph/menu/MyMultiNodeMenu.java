package medousa.sequential.graph.menu;

import medousa.sequential.graph.flow.MyFlowExplorerAnalyzer;
import medousa.sequential.utils.MyFontChooser;
import medousa.sequential.utils.MySequentialGraphVars;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyMultiNodeMenu
extends JPopupMenu
implements ActionListener {

    private JMenuItem showPredecessorsOnly = new JMenuItem("SHOW PREDECESSORS ONLY");
    private JMenuItem showSuccessorsOnly = new JMenuItem("SHOW SUCCESSORS ONLY");
    private JMenuItem showSharedPredecessorsOnly = new JMenuItem("SHOW SHARED PREDECESSORS ONLY");
    private JMenuItem showSharedSuccessorsOnly = new JMenuItem("SHOW SHARED SUCCESSORS ONLY");
    private JMenuItem showSharedNeighbors = new JMenuItem("SHOW SHARED NEIGHBORS");
    private JMenuItem showNeighbors = new JMenuItem("SHOW NEIGHBORS");
    private JMenu download = new JMenu("DOWNLOAD");
    private JMenuItem downloadUsers = new JMenuItem("OBJECT ID LIST");
    private JMenuItem multiNodePathFlow = new JMenuItem("FROM PATH FLOW");
    private JMenuItem nodeFont = new JMenuItem("NODE FONT");
    private JMenuItem edgeFont = new JMenuItem("EDGE FONT");
    public MyMultiNodeMenu( ) {
        this.decorate();
    }

    private void decorate() {
        this.setMenuItem(null, this.showPredecessorsOnly);
        this.setMenuItem(null, this.showSuccessorsOnly);
        this.setMenuItem(null, this.showNeighbors);
        this.add(new JSeparator());
        this.setMenuItem(null, this.showSharedPredecessorsOnly);
        this.setMenuItem(null, this.showSharedSuccessorsOnly);
        this.setMenuItem(null, this.showSharedNeighbors);
        this.add(new JSeparator());
        this.setMenuItem(null, this.download);
        this.setMenuItem(this.download, this.downloadUsers);
        this.add(new JSeparator());
        this.setMenuItem(null, multiNodePathFlow);
        this.add(new JSeparator());
        this.setMenuItem(null, this.nodeFont);
        this.setMenuItem(null, this.edgeFont);
        this.download.setEnabled(false);
    }

    private void setMenuItem(JMenu root, JMenuItem menuItem) {
        menuItem.setFont(MySequentialGraphVars.tahomaPlainFont12);
        menuItem.addActionListener(this);
        if (root == null) {
            this.add(menuItem);
        } else {
            root.add(menuItem);
        }
        menuItem.setPreferredSize(new Dimension(240, 26));
    }

    @Override public void actionPerformed(ActionEvent e) {
        new Thread(new Runnable() {
            @Override public void run() {
                if (e.getSource() == showPredecessorsOnly) {
                    MySequentialGraphVars.getSequentialGraphViewer().predecessorsOnly = true;
                    MySequentialGraphVars.getSequentialGraphViewer().successorsOnly = false;
                    MySequentialGraphVars.getSequentialGraphViewer().neighborsOnly = false;
                    MySequentialGraphVars.getSequentialGraphViewer().revalidate();
                    MySequentialGraphVars.getSequentialGraphViewer().repaint();
                } else if (e.getSource() == showSuccessorsOnly) {
                    MySequentialGraphVars.getSequentialGraphViewer().predecessorsOnly = false;
                    MySequentialGraphVars.getSequentialGraphViewer().successorsOnly = true;
                    MySequentialGraphVars.getSequentialGraphViewer().neighborsOnly = false;
                    MySequentialGraphVars.getSequentialGraphViewer().revalidate();
                    MySequentialGraphVars.getSequentialGraphViewer().repaint();
                } else if (e.getSource() == showNeighbors) {
                    MySequentialGraphVars.getSequentialGraphViewer().predecessorsOnly = false;
                    MySequentialGraphVars.getSequentialGraphViewer().successorsOnly = false;
                    MySequentialGraphVars.getSequentialGraphViewer().neighborsOnly = true;
                    MySequentialGraphVars.getSequentialGraphViewer().revalidate();
                    MySequentialGraphVars.getSequentialGraphViewer().repaint();
                } else if (e.getSource() == showSharedPredecessorsOnly) {
                    MySequentialGraphVars.getSequentialGraphViewer().sharedPredecessorsOly = true;
                    MySequentialGraphVars.getSequentialGraphViewer().sharedSuccessorsOnly = false;
                    MySequentialGraphVars.getSequentialGraphViewer().sharedNeighborsOnly = false;
                    MySequentialGraphVars.getSequentialGraphViewer().revalidate();
                    MySequentialGraphVars.getSequentialGraphViewer().repaint();
                } else if (e.getSource() == showSharedSuccessorsOnly) {
                    MySequentialGraphVars.getSequentialGraphViewer().sharedPredecessorsOly = false;
                    MySequentialGraphVars.getSequentialGraphViewer().sharedSuccessorsOnly = true;
                    MySequentialGraphVars.getSequentialGraphViewer().sharedNeighborsOnly = false;
                    MySequentialGraphVars.getSequentialGraphViewer().revalidate();
                    MySequentialGraphVars.getSequentialGraphViewer().repaint();
                } else if (e.getSource() == showSharedNeighbors) {
                    MySequentialGraphVars.getSequentialGraphViewer().sharedPredecessorsOly = false;
                    MySequentialGraphVars.getSequentialGraphViewer().sharedSuccessorsOnly = false;
                    MySequentialGraphVars.getSequentialGraphViewer().sharedNeighborsOnly = true;
                    MySequentialGraphVars.getSequentialGraphViewer().revalidate();
                    MySequentialGraphVars.getSequentialGraphViewer().repaint();
                } else if (e.getSource() == multiNodePathFlow) {
                    MyFlowExplorerAnalyzer pathFlowAnalyzer = new MyFlowExplorerAnalyzer();
                    pathFlowAnalyzer.showMultiNodeFromPathFlows();
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
