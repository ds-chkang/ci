package datamining.graph.listener;

import datamining.graph.MyEdge;
import datamining.graph.MyNode;
import datamining.graph.MyViewerComponentController;
import datamining.utils.MyViewerControlComponentUtil;
import datamining.utils.system.MyVars;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

public class MyNodeEdgeExclusionActionListener
implements ActionListener {

    private MyViewerComponentController viewerController;

    public MyNodeEdgeExclusionActionListener(MyViewerComponentController viewerController) {
        this.viewerController = viewerController;
    }

    @Override public void actionPerformed(ActionEvent e) {
        new Thread(new Runnable() {
            @Override public void run() {
                if (MyVars.getViewer().vc.nodeValueExcludeTxt.getText().length() > 0 && MyVars.getViewer().vc.edgeValueExcludeTxt.getText().length() > 0) {
                    doNodeExclusion();
                    doEdgeExclusion();
                } else if (MyVars.getViewer().vc.nodeValueExcludeTxt.getText().length() > 0 && MyVars.getViewer().vc.edgeValueExcludeTxt.getText().length() == 0) {
                    doNodeExclusion();
                } else if (MyVars.getViewer().vc.nodeValueExcludeTxt.getText().length() == 0 && MyVars.getViewer().vc.edgeValueExcludeTxt.getText().length() > 0) {
                    doEdgeExclusion();
                }
            }
        }).start();
    }

    private void doEdgeExclusion() {
        int edgesRemoved = 0;
        double edgeExcludeValue1 = Double.parseDouble(viewerController.edgeValueExcludeTxt.getText().trim());


        /**
         * Check edge value conditions.
         */
        Collection<MyEdge> edges = MyVars.g.getEdges();
        if (viewerController.edgeValueExcludeSymbolSelecter.getSelectedIndex() == 1) {
            for (MyEdge e : edges) {
                if (e.getCurrentValue() > 0) {
                    if (e.getCurrentValue() > edgeExcludeValue1) {
                        synchronized (e) {
                            e.setOriginalValue(e.getCurrentValue());
                            e.setCurrentValue(0.0f);
                            e.getSource().setCurrentValue(0);
                            e.getDest().setCurrentValue(0);
                        }
                        edgesRemoved++;
                    }
                }
            }
        } else if (viewerController.edgeValueExcludeSymbolSelecter.getSelectedIndex() == 2) {
            for (MyEdge e : edges) {
                if (e.getCurrentValue() > 0) {
                    if (e.getCurrentValue() >= edgeExcludeValue1) {
                        synchronized (e) {
                            e.setOriginalValue(e.getCurrentValue());
                            e.setCurrentValue(0.0f);
                            e.getSource().setCurrentValue(0);
                            e.getDest().setCurrentValue(0);
                        }
                        edgesRemoved++;
                    }
                }
            }
        } else if (viewerController.edgeValueExcludeSymbolSelecter.getSelectedIndex() == 3) {
            for (MyEdge e : edges) {
                if (e.getCurrentValue() == 0) {
                    if (e.getCurrentValue() > edgeExcludeValue1) {
                        synchronized (e) {
                            e.setOriginalValue(e.getCurrentValue());
                            e.setCurrentValue(0.0f);
                            e.getSource().setCurrentValue(0);
                            e.getDest().setCurrentValue(0);
                        }
                        edgesRemoved++;
                    }
                }
            }
        } else if (viewerController.edgeValueExcludeSymbolSelecter.getSelectedIndex() == 4) {
            for (MyEdge e : edges) {
                if (e.getCurrentValue() > 0) {
                    if (e.getCurrentValue() != edgeExcludeValue1) {
                        synchronized (e) {
                            e.setOriginalValue(e.getCurrentValue());
                            e.setCurrentValue(0.0f);
                            e.getSource().setCurrentValue(0);
                            e.getDest().setCurrentValue(0);
                        }
                        edgesRemoved++;
                    }
                }
            }
        } else if (viewerController.edgeValueExcludeSymbolSelecter.getSelectedIndex() == 5) {
            for (MyEdge e : edges) {
                if (e.getCurrentValue() > 0) {
                    if (e.getCurrentValue() < edgeExcludeValue1) {
                        synchronized (e) {
                            e.setOriginalValue(e.getCurrentValue());
                            e.setCurrentValue(0.0f);
                            e.getSource().setCurrentValue(0);
                            e.getDest().setCurrentValue(0);
                        }
                        edgesRemoved++;
                    }
                }
            }
        } else if (viewerController.edgeValueExcludeSymbolSelecter.getSelectedIndex() == 6) {
            for (MyEdge e : edges) {
                if (e.getCurrentValue() > 0) {
                    if (e.getCurrentValue() <= edgeExcludeValue1) {
                        synchronized (e) {
                            e.setOriginalValue(e.getCurrentValue());
                            e.setCurrentValue(0.0f);
                            e.getSource().setCurrentValue(0);
                            e.getDest().setCurrentValue(0);
                        }
                        edgesRemoved++;
                    }
                }
            }
        } else if (viewerController.edgeLabelExcludeSymbolSelecter.getSelectedIndex() == 7) {
            String[] numberPair = viewerController.edgeValueExcludeTxt.getText().split(",");
            double leftNum = Double.parseDouble(numberPair[0]);
            double rightNum = Double.parseDouble(numberPair[1]);
            for (MyEdge e : edges) {
                if (e.getCurrentValue() > 0) {
                    if (e.getCurrentValue() >= leftNum || e.getCurrentValue() <= rightNum) {
                        synchronized (e) {
                            e.setOriginalValue(e.getCurrentValue());
                            e.setCurrentValue(0.0f);
                            e.getSource().setCurrentValue(0);
                            e.getDest().setCurrentValue(0);
                        }
                        edgesRemoved++;
                    }
                }
            }
        }

        /**
         * 3. Check edge label conditions.
         */
        if (viewerController.edgeLabelExcludeSymbolSelecter.getSelectedIndex() > 0) {// Edge label exclusion condition is on.
            if (viewerController.edgeLabelExcludeSymbolSelecter.getSelectedIndex() == 1) {
                for (MyEdge e : edges) {
                    if (e.getCurrentValue() > 0) {
                        synchronized (e) {
                            if (e.edgeLabelMap.containsValue(viewerController.edgeLabelExcludeSelecter.getSelectedItem().toString().trim())) {
                                e.setOriginalValue(e.getCurrentValue());
                                e.setCurrentValue(0.0f);
                                e.getSource().setCurrentValue(0);
                                e.getDest().setCurrentValue(0);
                                edgesRemoved++;
                            }
                        }
                    }
                }
            } else if (viewerController.edgeLabelExcludeSymbolSelecter.getSelectedIndex() == 2) {
                for (MyEdge e : edges) {
                    if (e.getCurrentValue() > 0) {
                        synchronized (e) {
                            if (!e.edgeLabelMap.containsValue(viewerController.edgeLabelExcludeSelecter.getSelectedItem().toString().trim())) {
                                e.setOriginalValue(e.getCurrentValue());
                                e.setCurrentValue(0.0f);
                                e.getSource().setCurrentValue(0);
                                e.getDest().setCurrentValue(0);
                                edgesRemoved++;
                            }
                        }
                    }
                }
            }
        }

        MyViewerControlComponentUtil.setBottomCharts();
        MyVars.g.remainingEdges = (MyVars.g.getEdgeCount() - edgesRemoved);
        MyVars.getViewer().isExcludeBtnOn = true;
        MyVars.getViewer().vc.vTxtStat.setTextStatistics();
        MyViewerControlComponentUtil.updateDepthCharts();
    }

    private void doNodeExclusion() {
        int nodesRemoved = 0;
        double nodeExcludeValue1 = Double.parseDouble(viewerController.nodeValueExcludeTxt.getText().trim());

        Collection<MyNode> nodes = MyVars.g.getVertices();
        if (viewerController.nodeValueExcludeSymbolSelecter.getSelectedIndex() == 1) {
            float maxVal = 0f;
            for (MyNode n : nodes) {
                if (n.getCurrentValue() > 0) {
                    if (n.getCurrentValue() > nodeExcludeValue1) {
                        synchronized (n) {
                            n.setOriginalValue(n.getCurrentValue());
                            n.setCurrentValue(0);
                            nodesRemoved++;
                        }
                        if (maxVal < n.getCurrentValue()) {
                            maxVal = n.getCurrentValue();
                        }
                    }
                }
            }
            synchronized (MyVars.g.MX_N_VAL) {
                MyVars.g.MX_N_VAL = maxVal;
            }
        } else if (viewerController.nodeValueExcludeSymbolSelecter.getSelectedIndex() == 2) {
            float maxVal = 0f;
            for (MyNode n : nodes) {
                if (n.getCurrentValue() > 0) {
                    if (n.getCurrentValue() >= nodeExcludeValue1) {
                        synchronized (n) {
                            n.setOriginalValue(n.getCurrentValue());
                            n.setCurrentValue(0);
                            nodesRemoved++;
                        }
                        if (maxVal < n.getCurrentValue()) {
                            maxVal = n.getCurrentValue();
                        }
                    }
                }
            }
            synchronized (MyVars.g.MX_N_VAL) {
                MyVars.g.MX_N_VAL = maxVal;
            }
        } else if (viewerController.nodeValueExcludeSymbolSelecter.getSelectedIndex() == 3) {
            float maxVal = 0f;
            for (MyNode n : nodes) {
                if (n.getCurrentValue() > 0) {
                    if (n.getCurrentValue() == nodeExcludeValue1) {
                        synchronized (n) {
                            n.setOriginalValue(n.getCurrentValue());
                            n.setCurrentValue(0);
                            nodesRemoved++;
                        }
                        if (maxVal < n.getCurrentValue()) {
                            maxVal = n.getCurrentValue();
                        }
                    }
                }
            }
            synchronized (MyVars.g.MX_N_VAL) {
                MyVars.g.MX_N_VAL = maxVal;
            }
        } else if (viewerController.nodeValueExcludeSymbolSelecter.getSelectedIndex() == 4) {
            float maxVal = 0f;
            for (MyNode n : nodes) {
                if (n.getCurrentValue() > 0) {
                    if (n.getCurrentValue() != nodeExcludeValue1) {
                        synchronized (n) {
                            n.setOriginalValue(n.getCurrentValue());
                            n.setCurrentValue(0);
                            nodesRemoved++;
                        }
                        if (maxVal < n.getCurrentValue()) {
                            maxVal = n.getCurrentValue();
                        }
                    }
                }
            }
            synchronized (MyVars.g.MX_N_VAL) {
                MyVars.g.MX_N_VAL = maxVal;
            }
        } else if (viewerController.nodeValueExcludeSymbolSelecter.getSelectedIndex() == 5) {
            float maxVal = 0f;
            for (MyNode n : nodes) {
                if (n.getCurrentValue() > 0) {
                    if (n.getCurrentValue() < nodeExcludeValue1) {
                        synchronized (n) {
                            n.setOriginalValue(n.getCurrentValue());
                            n.setCurrentValue(0);
                            nodesRemoved++;
                        }
                        if (maxVal < n.getCurrentValue()) {
                            maxVal = n.getCurrentValue();
                        }
                    }
                }
            }
            synchronized (MyVars.g.MX_N_VAL) {
                MyVars.g.MX_N_VAL = maxVal;
            }
        } else if (viewerController.nodeValueExcludeSymbolSelecter.getSelectedIndex() == 6) {
            float maxVal = 0f;
            for (MyNode n : nodes) {
                if (n.getCurrentValue() > 0) {
                    if (n.getCurrentValue() <= nodeExcludeValue1) {
                        synchronized (n) {
                            n.setOriginalValue(n.getCurrentValue());
                            n.setCurrentValue(0);
                            nodesRemoved++;
                        }
                        if (maxVal < n.getCurrentValue()) {
                            maxVal = n.getCurrentValue();
                        }
                    }
                }
            }
            synchronized (MyVars.g.MX_N_VAL) {
                MyVars.g.MX_N_VAL = maxVal;
            }
        } else if (viewerController.nodeValueExcludeSymbolSelecter.getSelectedIndex() == 7) {
            float maxVal = 0f;
            String [] numberPair = viewerController.nodeValueExcludeTxt.getText().split(",");
            double leftNum = Double.parseDouble(numberPair[0]);
            double rightNum = Double.parseDouble(numberPair[1]);
            for (MyNode n : nodes) {
                if (n.getCurrentValue() > 0) {
                    if (n.getCurrentValue() >= leftNum || n.getCurrentValue() <= rightNum) {
                        synchronized (n) {
                            n.setOriginalValue(n.getCurrentValue());
                            n.setCurrentValue(0);
                            nodesRemoved++;
                        }
                        if (maxVal < n.getCurrentValue()) {
                            maxVal = n.getCurrentValue();
                        }
                    }
                }
            }
            synchronized (MyVars.g.MX_N_VAL) {
                MyVars.g.MX_N_VAL = maxVal;
            }
        }

        /**
         * Check node label conditions.
         */
        if (viewerController.nodeLabelExcludeSymbolSelecter.getSelectedIndex() > 0) {// Node label condition is on.
            if (viewerController.nodeLabelExcludeSymbolSelecter.getSelectedIndex() == 1) {
                for (MyNode n : nodes) {
                    if (n.getCurrentValue() > 0) {
                        synchronized (n) {
                            if (n.nodeLabelMap.containsValue(viewerController.nodeLabelExcludeSelecter.getSelectedItem().toString().trim())) {
                                n.setCurrentValue(0);
                                nodesRemoved++;
                            }
                        }
                    }
                }
            } else if (viewerController.nodeLabelExcludeSymbolSelecter.getSelectedIndex() == 2) {
                for (MyNode n : nodes) {
                    if (n.getCurrentValue() > 0) {
                        synchronized (n) {
                            if (!n.nodeLabelMap.containsValue(viewerController.nodeLabelExcludeSelecter.getSelectedItem().toString().trim())) {
                                n.setCurrentValue(0);
                                nodesRemoved++;
                            }
                        }
                    }
                }
            }
        }
        MyViewerControlComponentUtil.setBottomCharts();
        MyVars.g.remainingNodes= (MyVars.g.getVertexCount() - nodesRemoved);
        MyVars.getViewer().isExcludeBtnOn = true;
        MyVars.getViewer().vc.vTxtStat.setTextStatistics();
        MyViewerControlComponentUtil.updateDepthCharts();
    }

}
