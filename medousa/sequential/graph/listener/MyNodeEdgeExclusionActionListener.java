package medousa.sequential.graph.listener;

import medousa.message.MyMessageUtil;
import medousa.sequential.graph.MyEdge;
import medousa.sequential.graph.MyNode;
import medousa.sequential.graph.MyViewerComponentController;
import medousa.sequential.utils.MyDepthNodeExcluder;
import medousa.sequential.utils.MyViewerComponentControllerUtil;
import medousa.sequential.utils.MySequentialGraphVars;

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

                if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueExcludeTxt.getText().length() > 0 && MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueExcludeTxt.getText().matches("[0-9]+")) {
                    doNodeValueExclusion();
                }

                if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeLabelExcludeSelecter.isShowing() && MySequentialGraphVars.getSequentialGraphViewer().vc.nodeLabelExcludeSelecter.getSelectedIndex() > 0 &&
                    MySequentialGraphVars.getSequentialGraphViewer().vc.nodeLabelExcludeMathSymbolSelecter.isShowing() && MySequentialGraphVars.getSequentialGraphViewer().vc.nodeLabelExcludeMathSymbolSelecter.getSelectedIndex() > 0) {
                    doNodeLabelExclusion();
                }

                if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueExcludeTxt.getText().length() > 0 && MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueExcludeTxt.getText().matches("[0-9]+")) {
                    if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() <= 1) {
                        MyMessageUtil.showInfoMsg(MySequentialGraphVars.app, "Select an edge value.");
                        return;
                    }
                    doEdgeValueExclusion();
                }

                if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeLabelExcludeSelecter.isShowing() && MySequentialGraphVars.getSequentialGraphViewer().vc.edgeLabelExcludeSelecter.getSelectedIndex() > 0 &&
                    MySequentialGraphVars.getSequentialGraphViewer().vc.edgeLabelExcludeMathSymbolSelecter.isShowing() && MySequentialGraphVars.getSequentialGraphViewer().vc.edgeLabelExcludeMathSymbolSelecter.getSelectedIndex() > 0) {
                    doEdgeLabelExclusion();
                }

                if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthExcludeSelecter.getSelectedIndex() > 0 && MySequentialGraphVars.getSequentialGraphViewer().vc.depthExcludeSymbolSelecter.getSelectedIndex() > 0) {
                    MyDepthNodeExcluder.exludeDepthNodes(Integer.parseInt(MySequentialGraphVars.getSequentialGraphViewer().vc.depthExcludeSelecter.getSelectedItem().toString()));
                }

                updateBarCharts();
                MySequentialGraphVars.getSequentialGraphViewer().revalidate();
                MySequentialGraphVars.getSequentialGraphViewer().repaint();
            }
        }).start();
    }

    private void updateBarCharts() {
        if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueBarChart.isSelected()) {
            MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueBarChart.setSelected(false);
            MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueBarChart.setSelected(true);
        }

        if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueBarChart.isSelected()) {
            MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueBarChart.setSelected(false);
            MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueBarChart.setSelected(true);
        }
    }

    private void doEdgeValueExclusion() {
        int edgesRemoved = 0;
        double edgeExcludeValue1 = Double.parseDouble(viewerController.edgeValueExcludeTxt.getText().trim());

        /**
         * Check edge value conditions.
         */
        Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
        if (viewerController.edgeValueExcludeSymbolSelecter.getSelectedIndex() == 1) {
            for (MyEdge e : edges) {
                if (e.getCurrentValue() > 0) {
                    if (e.getCurrentValue() > edgeExcludeValue1) {
                        e.setOriginalValue(e.getCurrentValue());
                        e.setCurrentValue(0.0f);
                        edgesRemoved++;
                    }
                }
            }
        } else if (viewerController.edgeValueExcludeSymbolSelecter.getSelectedIndex() == 2) {
            for (MyEdge e : edges) {
                if (e.getCurrentValue() > 0) {
                    if (e.getCurrentValue() >= edgeExcludeValue1) {
                        e.setOriginalValue(e.getCurrentValue());
                        e.setCurrentValue(0.0f);
                        edgesRemoved++;
                    }
                }
            }
        } else if (viewerController.edgeValueExcludeSymbolSelecter.getSelectedIndex() == 3) {
            for (MyEdge e : edges) {
                if (e.getCurrentValue() == 0) {
                    if (e.getCurrentValue() > edgeExcludeValue1) {
                        e.setOriginalValue(e.getCurrentValue());
                        e.setCurrentValue(0.0f);
                        edgesRemoved++;
                    }
                }
            }
        } else if (viewerController.edgeValueExcludeSymbolSelecter.getSelectedIndex() == 4) {
            for (MyEdge e : edges) {
                if (e.getCurrentValue() > 0) {
                    if (e.getCurrentValue() != edgeExcludeValue1) {
                        e.setOriginalValue(e.getCurrentValue());
                        e.setCurrentValue(0.0f);
                        edgesRemoved++;
                    }
                }
            }
        } else if (viewerController.edgeValueExcludeSymbolSelecter.getSelectedIndex() == 5) {
            for (MyEdge e : edges) {
                if (e.getCurrentValue() > 0) {
                    if (e.getCurrentValue() < edgeExcludeValue1) {
                        e.setOriginalValue(e.getCurrentValue());
                        e.setCurrentValue(0.0f);
                        edgesRemoved++;
                    }
                }
            }
        } else if (viewerController.edgeValueExcludeSymbolSelecter.getSelectedIndex() == 6) {
            for (MyEdge e : edges) {
                if (e.getCurrentValue() > 0) {
                    if (e.getCurrentValue() <= edgeExcludeValue1) {
                        e.setOriginalValue(e.getCurrentValue());
                        e.setCurrentValue(0.0f);
                        edgesRemoved++;
                    }
                }
            }
        } else if (viewerController.edgeLabelExcludeMathSymbolSelecter.getSelectedIndex() == 7) {
            String[] numberPair = viewerController.edgeValueExcludeTxt.getText().split(",");
            double leftNum = Double.parseDouble(numberPair[0]);
            double rightNum = Double.parseDouble(numberPair[1]);
            for (MyEdge e : edges) {
                if (e.getCurrentValue() > 0) {
                    if (e.getCurrentValue() >= leftNum || e.getCurrentValue() <= rightNum) {
                        e.setOriginalValue(e.getCurrentValue());
                        e.setCurrentValue(0.0f);
                        edgesRemoved++;
                    }
                }
            }
        }

        if (edgesRemoved > 0) {
            MySequentialGraphVars.g.remainingEdges = (MySequentialGraphVars.g.getEdgeCount() - edgesRemoved);
            MySequentialGraphVars.getSequentialGraphViewer().excluded = true;
        }

        /**
         * Remove nodes with edges of zero values of neighbor nodes.
         */
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            Collection<MyEdge> neighborEdges = MySequentialGraphVars.g.getIncidentEdges(n);
            boolean allZeroEdges = true;
            for (MyEdge e : neighborEdges) {
                if (e.getCurrentValue() > 0) {
                    allZeroEdges = false;
                    break;
                }
            }

            if (allZeroEdges) {
                n.setOriginalValue(n.getCurrentValue());
                n.setCurrentValue(0);
            }
        }

    }

    private void doNodeValueExclusion() {
        int nodesRemoved = 0;
        double nodeExcludeValue1 = Double.parseDouble(viewerController.nodeValueExcludeTxt.getText().trim());

        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        if (viewerController.nodeValueExcludeSymbolSelecter.getSelectedIndex() == 1) {
            float maxVal = 0f;
            for (MyNode n : nodes) {
                if (n.getCurrentValue() > 0) {
                    if (n.getCurrentValue() > nodeExcludeValue1) {
                        n.setOriginalValue(n.getCurrentValue());
                        n.setCurrentValue(0);
                        nodesRemoved++;

                        if (maxVal < n.getCurrentValue()) {
                            maxVal = n.getCurrentValue();
                        }
                    }
                }
            }
            MySequentialGraphVars.g.MX_N_VAL = maxVal;
        } else if (viewerController.nodeValueExcludeSymbolSelecter.getSelectedIndex() == 2) {
            float maxVal = 0f;
            for (MyNode n : nodes) {
                if (n.getCurrentValue() > 0) {
                    if (n.getCurrentValue() >= nodeExcludeValue1) {
                        n.setOriginalValue(n.getCurrentValue());
                        n.setCurrentValue(0);
                        nodesRemoved++;

                        if (maxVal < n.getCurrentValue()) {
                            maxVal = n.getCurrentValue();
                        }
                    }
                }
            }
            MySequentialGraphVars.g.MX_N_VAL = maxVal;
        } else if (viewerController.nodeValueExcludeSymbolSelecter.getSelectedIndex() == 3) {
            float maxVal = 0f;
            for (MyNode n : nodes) {
                if (n.getCurrentValue() > 0) {
                    if (n.getCurrentValue() == nodeExcludeValue1) {
                        n.setOriginalValue(n.getCurrentValue());
                        n.setCurrentValue(0);
                        nodesRemoved++;

                        if (maxVal < n.getCurrentValue()) {
                            maxVal = n.getCurrentValue();
                        }
                    }
                }
            }
            MySequentialGraphVars.g.MX_N_VAL = maxVal;
        } else if (viewerController.nodeValueExcludeSymbolSelecter.getSelectedIndex() == 4) {
            float maxVal = 0f;
            for (MyNode n : nodes) {
                if (n.getCurrentValue() > 0) {
                    if (n.getCurrentValue() != nodeExcludeValue1) {
                        n.setOriginalValue(n.getCurrentValue());
                        n.setCurrentValue(0);
                        nodesRemoved++;

                        if (maxVal < n.getCurrentValue()) {
                            maxVal = n.getCurrentValue();
                        }
                    }
                }
            }
            MySequentialGraphVars.g.MX_N_VAL = maxVal;
        } else if (viewerController.nodeValueExcludeSymbolSelecter.getSelectedIndex() == 5) {
            float maxVal = 0f;
            for (MyNode n : nodes) {
                if (n.getCurrentValue() > 0) {
                    if (n.getCurrentValue() < nodeExcludeValue1) {
                        n.setOriginalValue(n.getCurrentValue());
                        n.setCurrentValue(0);
                        nodesRemoved++;

                        if (maxVal < n.getCurrentValue()) {
                            maxVal = n.getCurrentValue();
                        }
                    }
                }
            }
            MySequentialGraphVars.g.MX_N_VAL = maxVal;
        } else if (viewerController.nodeValueExcludeSymbolSelecter.getSelectedIndex() == 6) {
            float maxVal = 0f;
            for (MyNode n : nodes) {
                if (n.getCurrentValue() > 0) {
                    if (n.getCurrentValue() <= nodeExcludeValue1) {
                        n.setOriginalValue(n.getCurrentValue());
                        n.setCurrentValue(0);
                        nodesRemoved++;

                        if (maxVal < n.getCurrentValue()) {
                            maxVal = n.getCurrentValue();
                        }
                    }
                }
            }
            MySequentialGraphVars.g.MX_N_VAL = maxVal;
        } else if (viewerController.nodeValueExcludeSymbolSelecter.getSelectedIndex() == 7) {
            float maxVal = 0f;
            String[] numberPair = viewerController.nodeValueExcludeTxt.getText().split(",");
            double leftNum = Double.parseDouble(numberPair[0]);
            double rightNum = Double.parseDouble(numberPair[1]);
            for (MyNode n : nodes) {
                if (n.getCurrentValue() > 0) {
                    if (n.getCurrentValue() >= leftNum || n.getCurrentValue() <= rightNum) {
                        n.setOriginalValue(n.getCurrentValue());
                        n.setCurrentValue(0);
                        nodesRemoved++;
                        if (maxVal < n.getCurrentValue()) {
                            maxVal = n.getCurrentValue();
                        }
                    }
                }
            }
            MySequentialGraphVars.g.MX_N_VAL = maxVal;
        }

        if (nodesRemoved > 0) {
            MySequentialGraphVars.getSequentialGraphViewer().excluded = true;
            MySequentialGraphVars.g.remainingNodes = (MySequentialGraphVars.g.getVertexCount() - nodesRemoved);
        }

        /**
         * Remvoe edges with zero values of nodes.
         */
        Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
        for (MyEdge e : edges) {
            if (e.getSource().getCurrentValue() == 0 || e.getDest().getCurrentValue() == 0) {
                e.setOriginalValue(e.getCurrentValue());
                e.setCurrentValue(0f);
            }
        }
    }

    private void doNodeLabelExclusion() {
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        int nodesRemoved = 0;
        if (viewerController.nodeLabelExcludeMathSymbolSelecter.getSelectedIndex() > 0) {// Node label condition is on.
            if (viewerController.nodeLabelExcludeMathSymbolSelecter.getSelectedIndex() == 1) {
                for (MyNode n : nodes) {
                    if (n.getCurrentValue() > 0) {
                        if (n.nodeLabelMap.containsValue(viewerController.nodeLabelExcludeSelecter.getSelectedItem().toString().trim())) {
                            n.setCurrentValue(0);
                            nodesRemoved++;
                        }
                    }
                }
            } else if (viewerController.nodeLabelExcludeMathSymbolSelecter.getSelectedIndex() == 2) {
                for (MyNode n : nodes) {
                    if (n.getCurrentValue() > 0) {
                        if (!n.nodeLabelMap.containsValue(viewerController.nodeLabelExcludeSelecter.getSelectedItem().toString().trim())) {
                            n.setCurrentValue(0);
                            nodesRemoved++;
                        }
                    }
                }
            }
        }

        if (nodesRemoved > 0) {
            MySequentialGraphVars.getSequentialGraphViewer().excluded = true;
            MySequentialGraphVars.g.remainingNodes = (MySequentialGraphVars.g.getVertexCount() - nodesRemoved);
        }
    }

    private void doEdgeLabelExclusion() {
        Collection<MyEdge> edges = MySequentialGraphVars.g.getVertices();
        int edgesRemoved = 0;
        if (viewerController.edgeLabelExcludeMathSymbolSelecter.getSelectedIndex() > 0) {// Edge label exclusion condition is on.
            if (viewerController.edgeLabelExcludeMathSymbolSelecter.getSelectedIndex() == 1) {
                for (MyEdge e : edges) {
                    if (e.getCurrentValue() > 0) {
                        if (e.edgeLabelMap.containsValue(viewerController.edgeLabelExcludeSelecter.getSelectedItem().toString().trim())) {
                            e.setOriginalValue(e.getCurrentValue());
                            e.setCurrentValue(0.0f);
                            e.getSource().setCurrentValue(0);
                            e.getDest().setCurrentValue(0);
                            edgesRemoved++;
                        }
                    }
                }
            } else if (viewerController.edgeLabelExcludeMathSymbolSelecter.getSelectedIndex() == 2) {
                for (MyEdge e : edges) {
                    if (e.getCurrentValue() > 0) {
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

        if (edgesRemoved > 0) {
            MySequentialGraphVars.g.remainingEdges = (MySequentialGraphVars.g.getEdgeCount() - edgesRemoved);
            MySequentialGraphVars.getSequentialGraphViewer().excluded = true;
        }
    }

}