package medousa.sequential.graph.funnel;

import medousa.MyProgressBar;
import medousa.message.MyMessageUtil;
import medousa.sequential.graph.MyEdge;
import medousa.sequential.graph.MyNode;
import medousa.sequential.utils.MyMathUtil;
import medousa.sequential.utils.MySequentialGraphSysUtil;
import medousa.sequential.utils.MySequentialGraphVars;
import org.apache.commons.collections15.Transformer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class MyAnalysisGraphViewerMenu
extends JPopupMenu
implements ActionListener {

    private MyAnalysisGraphViewer graphViewer;
    private JMenuItem showNodeContribution = new JMenuItem("SHOW NODE CONTRIBUTION");
    private JMenuItem hideContribution = new JMenuItem("HIDE NODE CONTRIBUTION");
    private JMenuItem showNodeLabel = new JMenuItem("SHOW NODE NAMES");
    private JMenuItem hideNodeLabel = new JMenuItem("HIDE NODE NAMES");
    private JMenuItem showEdgeContributionStrength = new JMenuItem("SHOW EDGE CONTRIBUTION");
    private JMenuItem hideEdgeContributionStrength = new JMenuItem("HIDE EDGE CONTRIBUTION");
    private JMenuItem showReachTime = new JMenuItem("SHOW EDGE REACH TIME");
    private JMenuItem hideEdgeReachTimeStrength = new JMenuItem("HIDE EDGE REACH TIME");
    private JMenuItem deleteNodes = new JMenuItem("DELETE NODES");

    public MyAnalysisGraphViewerMenu(MyAnalysisGraphViewer graphViewer) {
        this.graphViewer = graphViewer;
        this.setMenuItem(this.showNodeContribution);
        this.setMenuItem(this.hideContribution);
        this.add(new JSeparator());
        this.setMenuItem(this.showNodeLabel);
        this.setMenuItem(this.hideNodeLabel);
        this.add(new JSeparator());
        this.setMenuItem(this.showEdgeContributionStrength);
        this.setMenuItem(this.hideEdgeContributionStrength);
        if (MySequentialGraphVars.isTimeOn) {
            this.add(new JSeparator());
            this.setMenuItem(this.showReachTime);
            this.setMenuItem(this.hideEdgeReachTimeStrength);
        }
        this.add(new JSeparator());
        this.setMenuItem(this.deleteNodes);
    }

    private void setMenuItem(JMenuItem menuItem) {
        menuItem.setFont(MySequentialGraphVars.tahomaPlainFont12);
        menuItem.setPreferredSize(new Dimension(250, 24));
        this.add(menuItem);
        menuItem.addActionListener(this);
    }

    @Override public void actionPerformed(ActionEvent e) {
        new Thread(new Runnable() {@Override public void run() {
                if (e.getSource() == showNodeContribution) {
                    graphViewer.getRenderContext().setVertexLabelTransformer(new Transformer<MyNode, String>() {
                        @Override public String transform(MyNode node) {return MyMathUtil.getCommaSeperatedNumber(node.getContribution());}
                    });

                    graphViewer.getRenderContext().setVertexFontTransformer(new Transformer<MyNode, Font>() {
                        @Override public Font transform(MyNode myNode) {
                            return MySequentialGraphVars.f_bold_16;
                        }
                    });
                } else if (e.getSource() == hideContribution) {
                    graphViewer.getRenderContext().setVertexLabelTransformer(new Transformer<MyNode, String>() {
                        @Override public String transform(MyNode node) {
                            return "";
                        }
                    });
                    graphViewer.getRenderContext().setVertexFontTransformer(new Transformer<MyNode, Font>() {
                        @Override public Font transform(MyNode myNode) {
                            return new Font("Arial", Font.PLAIN, 0);
                        }
                    });
                } else if (e.getSource() == showNodeLabel) {
                    graphViewer.getRenderContext().setVertexLabelTransformer(new Transformer<MyNode, String>() {
                        @Override public String transform(MyNode node) {
                            return MySequentialGraphSysUtil.decodeNodeName(node.getName());
                        }
                    });
                    graphViewer.getRenderContext().setVertexFontTransformer(new Transformer<MyNode, Font>() {
                        @Override public Font transform(MyNode myNode) {
                            return MySequentialGraphVars.f_bold_16;
                        }
                    });
                } else if (e.getSource() == hideNodeLabel) {
                    graphViewer.getRenderContext().setVertexLabelTransformer(new Transformer<MyNode, String>() {
                        @Override public String transform(MyNode node) {
                            return "";
                        }
                    });
                    graphViewer.getRenderContext().setVertexFontTransformer(new Transformer<MyNode, Font>() {
                        @Override public Font transform(MyNode myNode) {
                            return new Font("Arial", Font.PLAIN, 0);
                        }
                    });
                } else if (e.getSource() == showEdgeContributionStrength) {
                    graphViewer.setEdgeContributionValue();
                    graphViewer.getRenderContext().setEdgeLabelTransformer(new Transformer<MyEdge, String>() {
                        @Override public String transform(MyEdge edge) {return MyMathUtil.getCommaSeperatedNumber((int)edge.getCurrentValue());}
                    });
                    graphViewer.getRenderContext().setEdgeFontTransformer(new Transformer<MyEdge, Font>() {
                        @Override public Font transform(MyEdge myNode) {
                            return MySequentialGraphVars.f_bold_16;
                        }
                    });
                } else if (e.getSource() == hideEdgeContributionStrength) {
                    graphViewer.getRenderContext().setEdgeLabelTransformer(new Transformer<MyEdge, String>() {
                        @Override public String transform(MyEdge edge) {
                            return "";
                        }
                    });
                    graphViewer.getRenderContext().setEdgeFontTransformer(new Transformer<MyEdge, Font>() {
                        @Override public Font transform(MyEdge myNode) {
                            return new Font("Arial", Font.PLAIN, 0);
                        }
                    });
                } else if (e.getSource() == showReachTime) {
                    graphViewer.setEdgeReachTimeValue();
                    graphViewer.getRenderContext().setEdgeLabelTransformer(new Transformer<MyEdge, String>() {
                        @Override public String transform(MyEdge edge) {return MyMathUtil.getCommaSeperatedNumber((long)edge.getCurrentValue());}
                    });
                    graphViewer.getRenderContext().setEdgeFontTransformer(new Transformer<MyEdge, Font>() {
                        @Override public Font transform(MyEdge myNode) {
                            return MySequentialGraphVars.f_bold_16;
                        }
                    });
                } else if (e.getSource() == hideEdgeReachTimeStrength) {
                    graphViewer.getRenderContext().setEdgeLabelTransformer(new Transformer<MyEdge, String>() {
                        @Override public String transform(MyEdge edge) {
                            return "";
                        }
                    });
                    graphViewer.getRenderContext().setEdgeFontTransformer(new Transformer<MyEdge, Font>() {
                        @Override public Font transform(MyEdge myNode) {
                            return new Font("Arial", Font.PLAIN, 0);
                        }
                    });
                } else if (e.getSource() == hideEdgeReachTimeStrength) {
                    graphViewer.getRenderContext().setEdgeLabelTransformer(new Transformer<MyEdge, String>() {
                        @Override public String transform(MyEdge edge) {
                            return "";
                        }
                    });
                    graphViewer.getRenderContext().setEdgeFontTransformer(new Transformer<MyEdge, Font>() {
                        @Override public Font transform(MyEdge myNode) {
                            return new Font("Arial", Font.PLAIN, 0);
                        }
                    });
                } else if (e.getSource() == deleteNodes) {
                    try {
                        Collection<MyNode> nodes = new ArrayList<>(graphViewer.getGraphLayout().getGraph().getVertices());
                        if (nodes != null) {
                            MyProgressBar pb = new MyProgressBar(false);
                            int pbCnt = 0;
                            for (MyNode node : nodes) {
                                graphViewer.getGraphLayout().getGraph().removeVertex(node);
                                pb.updateValue(++pbCnt, nodes.size());
                            }
                            ((MyAnalysisGraph) graphViewer.getGraphLayout().getGraph()).vRefs = new HashMap();
                            ((MyAnalysisGraph) graphViewer.getGraphLayout().getGraph()).edRefs = new HashSet();
                            graphViewer.revalidate();
                            graphViewer.repaint();
                            pb.updateValue(100, 100);
                            pb.dispose();
                        }
                        if (graphViewer.getGraphLayout().getGraph().getVertexCount() == 0) {
                            MyMessageUtil.showInfoMsg("All nodes have successfully been removed.");}
                    } catch (Exception ex) {MyMessageUtil.showErrorMsg("An exception occurred while removing nodes!");}
                }
                graphViewer.revalidate();
                graphViewer.repaint();
            }
        }).start();
    }
}
