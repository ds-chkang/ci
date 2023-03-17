package datamining.graph.analysis;

import datamining.main.MyProgressBar;
import datamining.graph.MyEdge;
import datamining.graph.MyNode;
import datamining.utils.MyMathUtil;
import datamining.utils.message.MyMessageUtil;
import datamining.utils.system.MySysUtil;
import datamining.utils.system.MyVars;
import org.apache.commons.collections15.Transformer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class MyNetworkViewerMenu
extends JPopupMenu
implements ActionListener {

    private MyNetworkAnalyzerViewer funnelViewer;
    private JMenuItem showNodeContribution = new JMenuItem("SHOW NODE CONTRIBUTION");
    private JMenuItem hideContribution = new JMenuItem("HIDE NODE CONTRIBUTION");
    private JMenuItem showNodeLabel = new JMenuItem("SHOW NODE NAMES");
    private JMenuItem hideNodeLabel = new JMenuItem("HIDE NODE NAMES");
    private JMenuItem showEdgeContributionStrength = new JMenuItem("SHOW EDGE CONTRIBUTION");
    private JMenuItem hideEdgeContributionStrength = new JMenuItem("HIDE EDGE CONTRIBUTION");
    private JMenuItem showReachTime = new JMenuItem("SHOW EDGE REACH TIME");
    private JMenuItem hideEdgeReachTimeStrength = new JMenuItem("HIDE EDGE REACH TIME");
    private JMenuItem deleteNodes = new JMenuItem("DELETE NODES");

    public MyNetworkViewerMenu(MyNetworkAnalyzerViewer funnelViewer) {
        this.funnelViewer = funnelViewer;
        this.setMenuItem(this.showNodeContribution);
        this.setMenuItem(this.hideContribution);
        this.add(new JSeparator());
        this.setMenuItem(this.showNodeLabel);
        this.setMenuItem(this.hideNodeLabel);
        this.add(new JSeparator());
        this.setMenuItem(this.showEdgeContributionStrength);
        this.setMenuItem(this.hideEdgeContributionStrength);
        if (MyVars.isTimeOn) {
            this.add(new JSeparator());
            this.setMenuItem(this.showReachTime);
            this.setMenuItem(this.hideEdgeReachTimeStrength);
        }
        this.add(new JSeparator());
        this.setMenuItem(this.deleteNodes);
    }

    private void setMenuItem(JMenuItem menuItem) {
        menuItem.setFont(MyVars.tahomaPlainFont12);
        menuItem.setPreferredSize(new Dimension(250, 24));
        this.add(menuItem);
        menuItem.addActionListener(this);
    }

    @Override public void actionPerformed(ActionEvent e) {
        new Thread(new Runnable() {@Override public void run() {
                if (e.getSource() == showNodeContribution) {
                    funnelViewer.getRenderContext().setVertexLabelTransformer(new Transformer<MyNode, String>() {
                        @Override public String transform(MyNode node) {return MyMathUtil.getCommaSeperatedNumber(node.getContribution());}
                    });

                    funnelViewer.getRenderContext().setVertexFontTransformer(new Transformer<MyNode, Font>() {
                        @Override public Font transform(MyNode myNode) {
                            return MyVars.f_bold_16;
                        }
                    });
                } else if (e.getSource() == hideContribution) {
                    funnelViewer.getRenderContext().setVertexLabelTransformer(new Transformer<MyNode, String>() {
                        @Override public String transform(MyNode node) {
                            return "";
                        }
                    });
                    funnelViewer.getRenderContext().setVertexFontTransformer(new Transformer<MyNode, Font>() {
                        @Override public Font transform(MyNode myNode) {
                            return new Font("Arial", Font.PLAIN, 0);
                        }
                    });
                } else if (e.getSource() == showNodeLabel) {
                    funnelViewer.getRenderContext().setVertexLabelTransformer(new Transformer<MyNode, String>() {
                        @Override public String transform(MyNode node) {
                            return MySysUtil.decodeNodeName(node.getName());
                        }
                    });
                    funnelViewer.getRenderContext().setVertexFontTransformer(new Transformer<MyNode, Font>() {
                        @Override public Font transform(MyNode myNode) {
                            return MyVars.f_bold_16;
                        }
                    });
                } else if (e.getSource() == hideNodeLabel) {
                    funnelViewer.getRenderContext().setVertexLabelTransformer(new Transformer<MyNode, String>() {
                        @Override public String transform(MyNode node) {
                            return "";
                        }
                    });
                    funnelViewer.getRenderContext().setVertexFontTransformer(new Transformer<MyNode, Font>() {
                        @Override public Font transform(MyNode myNode) {
                            return new Font("Arial", Font.PLAIN, 0);
                        }
                    });
                } else if (e.getSource() == showEdgeContributionStrength) {
                    funnelViewer.setEdgeContributionValue();
                    funnelViewer.getRenderContext().setEdgeLabelTransformer(new Transformer<MyEdge, String>() {
                        @Override public String transform(MyEdge edge) {return MyMathUtil.getCommaSeperatedNumber((int)edge.getCurrentValue());}
                    });
                    funnelViewer.getRenderContext().setEdgeFontTransformer(new Transformer<MyEdge, Font>() {
                        @Override public Font transform(MyEdge myNode) {
                            return MyVars.f_bold_16;
                        }
                    });
                } else if (e.getSource() == hideEdgeContributionStrength) {
                    funnelViewer.getRenderContext().setEdgeLabelTransformer(new Transformer<MyEdge, String>() {
                        @Override public String transform(MyEdge edge) {
                            return "";
                        }
                    });
                    funnelViewer.getRenderContext().setEdgeFontTransformer(new Transformer<MyEdge, Font>() {
                        @Override public Font transform(MyEdge myNode) {
                            return new Font("Arial", Font.PLAIN, 0);
                        }
                    });
                } else if (e.getSource() == showReachTime) {
                    funnelViewer.setEdgeReachTimeValue();
                    funnelViewer.getRenderContext().setEdgeLabelTransformer(new Transformer<MyEdge, String>() {
                        @Override public String transform(MyEdge edge) {return MyMathUtil.getCommaSeperatedNumber((long)edge.getCurrentValue());}
                    });
                    funnelViewer.getRenderContext().setEdgeFontTransformer(new Transformer<MyEdge, Font>() {
                        @Override public Font transform(MyEdge myNode) {
                            return MyVars.f_bold_16;
                        }
                    });
                } else if (e.getSource() == hideEdgeReachTimeStrength) {
                    funnelViewer.getRenderContext().setEdgeLabelTransformer(new Transformer<MyEdge, String>() {
                        @Override public String transform(MyEdge edge) {
                            return "";
                        }
                    });
                    funnelViewer.getRenderContext().setEdgeFontTransformer(new Transformer<MyEdge, Font>() {
                        @Override public Font transform(MyEdge myNode) {
                            return new Font("Arial", Font.PLAIN, 0);
                        }
                    });
                } else if (e.getSource() == hideEdgeReachTimeStrength) {
                    funnelViewer.getRenderContext().setEdgeLabelTransformer(new Transformer<MyEdge, String>() {
                        @Override public String transform(MyEdge edge) {
                            return "";
                        }
                    });
                    funnelViewer.getRenderContext().setEdgeFontTransformer(new Transformer<MyEdge, Font>() {
                        @Override public Font transform(MyEdge myNode) {
                            return new Font("Arial", Font.PLAIN, 0);
                        }
                    });
                } else if (e.getSource() == deleteNodes) {
                    try {
                        Collection<MyNode> nodes = new ArrayList<>(funnelViewer.getGraphLayout().getGraph().getVertices());
                        if (nodes != null) {
                            MyProgressBar pb = new MyProgressBar(false);
                            int pbCnt = 0;
                            for (MyNode node : nodes) {
                                funnelViewer.getGraphLayout().getGraph().removeVertex(node);
                                pb.updateValue(++pbCnt, nodes.size());
                            }
                            ((MyAnalyzerGraph)funnelViewer.getGraphLayout().getGraph()).vRefs = new HashMap();
                            ((MyAnalyzerGraph)funnelViewer.getGraphLayout().getGraph()).edRefs = new HashSet();
                            funnelViewer.revalidate();
                            funnelViewer.repaint();
                            pb.updateValue(100, 100);
                            pb.dispose();
                        }
                        if (funnelViewer.getGraphLayout().getGraph().getVertexCount() == 0) {MyMessageUtil.showInfoMsg("All nodes have successfully been removed.");}
                    } catch (Exception ex) {MyMessageUtil.showErrorMsg("An exception occurred while removing nodes!");}
                }
                funnelViewer.revalidate();
                funnelViewer.repaint();
            }
        }).start();
    }
}
