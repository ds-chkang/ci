package medousa.sequential.graph.funnel;

import medousa.MyProgressBar;
import medousa.sequential.graph.MyNode;
import edu.uci.ics.jung.visualization.control.GraphMouseListener;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseEvent;

public class MyAnalysisGraphMouseListener
implements GraphMouseListener {

    private MyAnalysisGraphViewer graphViewer;
    public MyNode selectedNode;

    public MyAnalysisGraphMouseListener(MyAnalysisGraphViewer graphViewer) {
        this.graphViewer = graphViewer;
    }

    @Override public void graphClicked(Object o, MouseEvent e) {
        new Thread(new Runnable() {
            @Override public void run() {
                if (o != null && SwingUtilities.isLeftMouseButton(e)) {
                    //MyProgressBar pb = new MyProgressBar(false);
                    graphViewer.analysisGraphApp.nodeOptionComboBoxMenu.removeActionListener(graphViewer.analysisGraphApp);
                    graphViewer.analysisGraphApp.nodeOptionComboBoxMenu.setSelectedIndex(0);
                    graphViewer.analysisGraphApp.nodeOptionComboBoxMenu.addActionListener(graphViewer.analysisGraphApp);

                    int row = graphViewer.analysisGraphApp.table.getRowCount();
                    while (row > 0) {
                        ((DefaultTableModel) graphViewer.analysisGraphApp.table.getModel()).removeRow(row-1);
                        row = graphViewer.analysisGraphApp.table.getRowCount();
                    }

                    selectedNode = (MyNode)o;
                    graphViewer.mouseClickedLocation = e.getLocationOnScreen();
                    graphViewer.revalidate();
                    graphViewer.repaint();
                    //pb.updateValue(100, 100);
                    //pb.dispose();
                } else if (o != null && SwingUtilities.isRightMouseButton(e)) {
                    MyAnalysisGraphNodeMenu funnelNodeMenu = new MyAnalysisGraphNodeMenu(graphViewer, ((MyNode)o));
                    funnelNodeMenu.show(graphViewer, e.getX(), e.getY());
                }
            }
        }).start();
    }

    @Override public void graphPressed(Object o, MouseEvent mouseEvent) {}
    @Override public void graphReleased(Object o, MouseEvent mouseEvent) {}
}
