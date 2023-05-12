package medousa.direct.graph.analysis;

import medousa.direct.graph.MyDirectEdge;
import medousa.direct.graph.MyDirectNode;
import medousa.MyProgressBar;
import medousa.direct.utils.MyDirectGraphMathUtil;
import medousa.message.MyMessageUtil;
import medousa.direct.utils.MyDirectGraphSysUtil;
import medousa.direct.utils.MyDirectGraphVars;
import org.apache.commons.collections15.Transformer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class MyDirectGraphAnalysisDirectGraphViewerMenu
extends JPopupMenu
implements ActionListener {

    private MyDirectGraphAnalysisDirectGraphViewer funnelViewer;
    private JMenuItem showNodeContribution = new JMenuItem("SHOW NODE CONTRIBUTION");
    private JMenuItem hideContribution = new JMenuItem("HIDE NODE CONTRIBUTION");
    private JMenuItem showNodeLabel = new JMenuItem("SHOW NODE NAMES");
    private JMenuItem hideNodeLabel = new JMenuItem("HIDE NODE NAMES");
    private JMenuItem showEdgeContributionStrength = new JMenuItem("SHOW EDGE CONTRIBUTION");
    private JMenuItem hideEdgeContributionStrength = new JMenuItem("HIDE EDGE CONTRIBUTION");
    private JMenuItem showReachTime = new JMenuItem("SHOW EDGE REACH TIME");
    private JMenuItem hideEdgeReachTimeStrength = new JMenuItem("HIDE EDGE REACH TIME");
    private JMenuItem deleteNodes = new JMenuItem("DELETE NODES");

    public MyDirectGraphAnalysisDirectGraphViewerMenu(MyDirectGraphAnalysisDirectGraphViewer funnelViewer) {
        this.funnelViewer = funnelViewer;
        this.setMenuItem(this.showNodeContribution);
        this.setMenuItem(this.hideContribution);
        this.add(new JSeparator());
        this.setMenuItem(this.showNodeLabel);
        this.setMenuItem(this.hideNodeLabel);
        this.add(new JSeparator());
        this.setMenuItem(this.showEdgeContributionStrength);
        this.setMenuItem(this.hideEdgeContributionStrength);
        if (MyDirectGraphVars.isTimeOn) {
            this.add(new JSeparator());
            this.setMenuItem(this.showReachTime);
            this.setMenuItem(this.hideEdgeReachTimeStrength);
        }
        this.add(new JSeparator());
        this.setMenuItem(this.deleteNodes);
    }

    private void setMenuItem(JMenuItem menuItem) {
        menuItem.setFont(MyDirectGraphVars.tahomaPlainFont12);
        menuItem.setPreferredSize(new Dimension(250, 24));
        this.add(menuItem);
        menuItem.addActionListener(this);
    }

    @Override public void actionPerformed(ActionEvent e) {
        new Thread(new Runnable() {@Override public void run() {
                if (e.getSource() == showNodeContribution) {
                    funnelViewer.getRenderContext().setVertexLabelTransformer(new Transformer<MyDirectNode, String>() {
                        @Override public String transform(MyDirectNode node) {return MyDirectGraphMathUtil.getCommaSeperatedNumber(node.getContribution());}
                    });

                    funnelViewer.getRenderContext().setVertexFontTransformer(new Transformer<MyDirectNode, Font>() {
                        @Override public Font transform(MyDirectNode myNode) {
                            return MyDirectGraphVars.f_bold_16;
                        }
                    });
                } else if (e.getSource() == hideContribution) {
                    funnelViewer.getRenderContext().setVertexLabelTransformer(new Transformer<MyDirectNode, String>() {
                        @Override public String transform(MyDirectNode node) {
                            return "";
                        }
                    });
                    funnelViewer.getRenderContext().setVertexFontTransformer(new Transformer<MyDirectNode, Font>() {
                        @Override public Font transform(MyDirectNode myNode) {
                            return new Font("Arial", Font.PLAIN, 0);
                        }
                    });
                } else if (e.getSource() == showNodeLabel) {
                    funnelViewer.getRenderContext().setVertexLabelTransformer(new Transformer<MyDirectNode, String>() {
                        @Override public String transform(MyDirectNode node) {
                            return MyDirectGraphSysUtil.decodeNodeName(node.getName());
                        }
                    });
                    funnelViewer.getRenderContext().setVertexFontTransformer(new Transformer<MyDirectNode, Font>() {
                        @Override public Font transform(MyDirectNode myNode) {
                            return MyDirectGraphVars.f_bold_16;
                        }
                    });
                } else if (e.getSource() == hideNodeLabel) {
                    funnelViewer.getRenderContext().setVertexLabelTransformer(new Transformer<MyDirectNode, String>() {
                        @Override public String transform(MyDirectNode node) {
                            return "";
                        }
                    });
                    funnelViewer.getRenderContext().setVertexFontTransformer(new Transformer<MyDirectNode, Font>() {
                        @Override public Font transform(MyDirectNode myNode) {
                            return new Font("Arial", Font.PLAIN, 0);
                        }
                    });
                } else if (e.getSource() == showEdgeContributionStrength) {
                    funnelViewer.setEdgeContributionValue();
                    funnelViewer.getRenderContext().setEdgeLabelTransformer(new Transformer<MyDirectEdge, String>() {
                        @Override public String transform(MyDirectEdge edge) {return MyDirectGraphMathUtil.getCommaSeperatedNumber((int)edge.getCurrentValue());}
                    });
                    funnelViewer.getRenderContext().setEdgeFontTransformer(new Transformer<MyDirectEdge, Font>() {
                        @Override public Font transform(MyDirectEdge myNode) {
                            return MyDirectGraphVars.f_bold_16;
                        }
                    });
                } else if (e.getSource() == hideEdgeContributionStrength) {
                    funnelViewer.getRenderContext().setEdgeLabelTransformer(new Transformer<MyDirectEdge, String>() {
                        @Override public String transform(MyDirectEdge edge) {
                            return "";
                        }
                    });
                    funnelViewer.getRenderContext().setEdgeFontTransformer(new Transformer<MyDirectEdge, Font>() {
                        @Override public Font transform(MyDirectEdge myNode) {
                            return new Font("Arial", Font.PLAIN, 0);
                        }
                    });
                } else if (e.getSource() == showReachTime) {
                    funnelViewer.setEdgeReachTimeValue();
                    funnelViewer.getRenderContext().setEdgeLabelTransformer(new Transformer<MyDirectEdge, String>() {
                        @Override public String transform(MyDirectEdge edge) {return MyDirectGraphMathUtil.getCommaSeperatedNumber((long)edge.getCurrentValue());}
                    });
                    funnelViewer.getRenderContext().setEdgeFontTransformer(new Transformer<MyDirectEdge, Font>() {
                        @Override public Font transform(MyDirectEdge myNode) {
                            return MyDirectGraphVars.f_bold_16;
                        }
                    });
                } else if (e.getSource() == hideEdgeReachTimeStrength) {
                    funnelViewer.getRenderContext().setEdgeLabelTransformer(new Transformer<MyDirectEdge, String>() {
                        @Override public String transform(MyDirectEdge edge) {
                            return "";
                        }
                    });
                    funnelViewer.getRenderContext().setEdgeFontTransformer(new Transformer<MyDirectEdge, Font>() {
                        @Override public Font transform(MyDirectEdge myNode) {
                            return new Font("Arial", Font.PLAIN, 0);
                        }
                    });
                } else if (e.getSource() == hideEdgeReachTimeStrength) {
                    funnelViewer.getRenderContext().setEdgeLabelTransformer(new Transformer<MyDirectEdge, String>() {
                        @Override public String transform(MyDirectEdge edge) {
                            return "";
                        }
                    });
                    funnelViewer.getRenderContext().setEdgeFontTransformer(new Transformer<MyDirectEdge, Font>() {
                        @Override public Font transform(MyDirectEdge myNode) {
                            return new Font("Arial", Font.PLAIN, 0);
                        }
                    });
                } else if (e.getSource() == deleteNodes) {
                    try {
                        Collection<MyDirectNode> nodes = new ArrayList<>(funnelViewer.getGraphLayout().getGraph().getVertices());
                        if (nodes != null) {
                            MyProgressBar pb = new MyProgressBar(false);
                            int pbCnt = 0;
                            for (MyDirectNode node : nodes) {
                                funnelViewer.getGraphLayout().getGraph().removeVertex(node);
                                pb.updateValue(++pbCnt, nodes.size());
                            }
                            ((MyDirectGraphAnalysisGraph)funnelViewer.getGraphLayout().getGraph()).vRefs = new HashMap();
                            ((MyDirectGraphAnalysisGraph)funnelViewer.getGraphLayout().getGraph()).edRefs = new HashSet();
                            funnelViewer.revalidate();
                            funnelViewer.repaint();
                            pb.updateValue(100, 100);
                            pb.dispose();
                        }
                        if (funnelViewer.getGraphLayout().getGraph().getVertexCount() == 0) {
                            MyMessageUtil.showInfoMsg("All nodes have successfully been removed.");}
                    } catch (Exception ex) {
                        MyMessageUtil.showErrorMsg("An exception occurred while removing nodes!");}
                }
                funnelViewer.revalidate();
                funnelViewer.repaint();
            }
        }).start();
    }
}
