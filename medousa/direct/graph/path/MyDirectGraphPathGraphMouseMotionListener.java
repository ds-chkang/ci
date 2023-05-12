package medousa.direct.graph.path;

import medousa.direct.graph.MyDirectEdge;
import medousa.direct.graph.MyDirectNode;
import medousa.direct.utils.MyDirectGraphMathUtil;
import medousa.direct.utils.MyDirectGraphSysUtil;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Collection;

public class MyDirectGraphPathGraphMouseMotionListener
implements MouseMotionListener {

    private MyDirectNode mouseOverNode;
    private MyDirectGraphPathGraphViewer betweenPathGraphViewer;

    public MyDirectGraphPathGraphMouseMotionListener(MyDirectGraphPathGraphViewer betweenPathGraphViewer) {
        this.betweenPathGraphViewer = betweenPathGraphViewer;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        try {}
        catch (Exception ex) {
            new Thread(new Runnable() {
                @Override public void run() {
                    betweenPathGraphViewer.revalidate();
                    betweenPathGraphViewer.repaint();
                }
            }).start();
        }
    }

    @Override
    public synchronized void mouseMoved(MouseEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                new Thread(new Runnable() {
                    @Override public void run() {
                        try {
                            mouseOverNode = betweenPathGraphViewer.getPickSupport().getVertex(betweenPathGraphViewer.getGraphLayout(), e.getX(), e.getY());
                            if (betweenPathGraphViewer != null) {
                                if (mouseOverNode != null) {
                                    betweenPathGraphViewer.betweenPathGraphDepthFirstSearch.currentNodeInfoLabel.setText("");
                                    betweenPathGraphViewer.betweenPathGraphDepthFirstSearch.currentNodeInfoLabel.setText(
                                            "  N: [" + mouseOverNode.getName() + "]" +
                                                    "   CONT: " + MyDirectGraphMathUtil.getCommaSeperatedNumber(mouseOverNode.getContribution()) +
                                                    "   SUC: " + MyDirectGraphMathUtil.getCommaSeperatedNumber(betweenPathGraphViewer.getGraphLayout().getGraph().getSuccessorCount(mouseOverNode)) +
                                                    "   PRED: " + MyDirectGraphMathUtil.getCommaSeperatedNumber(betweenPathGraphViewer.getGraphLayout().getGraph().getPredecessorCount(mouseOverNode)) +
                                                    "   AVG. IN-CONT: " + MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(setAverageInEdgeContribution())).split("\\.")[0] +
                                                    "   AVG. OUT-CONT: " + MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(setAverageOutEdgeContribution())).split("\\.")[0] +
                                                    "   N. CONT. RANK: " + MyDirectGraphMathUtil.getCommaSeperatedNumber(betweenPathGraphViewer.nodeOrderMap.get(mouseOverNode.getName())));
                                } else if (mouseOverNode == null) {
                                    betweenPathGraphViewer.betweenPathGraphDepthFirstSearch.currentNodeInfoLabel.setText("");
                                }
                            }
                        } catch (Exception ex) {
                        }
                    }
                }).start();
            }
        });
    }

    private double setAverageOutEdgeContribution() {
        long outEdgeContribution = 0L;
        Collection<MyDirectEdge> outEdges = this.betweenPathGraphViewer.getGraphLayout().getGraph().getOutEdges(this.mouseOverNode);
        try {
            for (MyDirectEdge e : outEdges) {
                outEdgeContribution += e.getContribution();
            }
        } catch (Exception ex) {}
        if (outEdgeContribution > 0) {
            return (double) outEdgeContribution / outEdges.size();
        } else {
            return 0.00D;
        }
    }

    private double setAverageInEdgeContribution() {
        long inEdgeContribution = 0L;
        Collection<MyDirectEdge> inEdges = this.betweenPathGraphViewer.getGraphLayout().getGraph().getInEdges(this.mouseOverNode);
        try {
            for (MyDirectEdge e : inEdges) {
                inEdgeContribution += e.getContribution();
            }
        } catch (Exception ex) {}
        if (inEdgeContribution > 0) {
            return (double) inEdgeContribution / inEdges.size();
        } else {
            return 0.00D;
        }
    }
}
