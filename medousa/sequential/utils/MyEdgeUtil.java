package medousa.sequential.utils;

import medousa.sequential.graph.MyClusteringConfig;
import medousa.sequential.graph.MyEdge;
import medousa.sequential.graph.MyNode;
import edu.uci.ics.jung.algorithms.importance.BetweennessCentrality;
import org.apache.commons.collections15.Transformer;

import java.awt.*;
import java.util.Collection;

public class MyEdgeUtil {

    public static void setDefaultValuesToEdges() {
        Collection<MyEdge> es = MySequentialGraphVars.g.getEdges();
        for (MyEdge e : es) {
            e.setCurrentValue(MySequentialGraphVars.g.DEFALUT_EDGE_VALUE);
        }
        MySequentialGraphVars.g.MX_E_VAL = 42f;
    }

    public static void setClusteringDefaultValuesToEdges() {
        Collection<MyEdge> es = MySequentialGraphVars.g.getEdges();
        for (MyEdge e : es) {
            if (MyClusteringConfig.selectedClusterColor == null || e.getSource().clusteringColor != MyClusteringConfig.selectedClusterColor || e.getDest().clusteringColor != MyClusteringConfig.selectedClusterColor) continue;
            e.setCurrentValue(MySequentialGraphVars.g.DEFALUT_EDGE_VALUE);
        }
        MySequentialGraphVars.g.MX_E_VAL = 42f;
    }

    public static void removeEdges() {
        MySequentialGraphVars.getSequentialGraphViewer().getRenderContext().setEdgeDrawPaintTransformer(new Transformer<MyEdge, Paint>() {@Override
        public Paint transform(MyEdge e) {
            return new Color(0.0f, 0.0f, 0.0f, 0.0f);
        }
        });
        MySequentialGraphVars.getSequentialGraphViewer().getRenderContext().setEdgeLabelTransformer(new Transformer<MyEdge, String>() {
            @Override
            public String transform(MyEdge e) {
                return "";
            }
        });
        MySequentialGraphVars.getSequentialGraphViewer().revalidate();
        MySequentialGraphVars.getSequentialGraphViewer().repaint();
    }

    public static void setEdgeColor() {
        MySequentialGraphVars.getSequentialGraphViewer().getRenderContext().setEdgeDrawPaintTransformer(new Transformer<MyEdge, Paint>() {@Override
        public Paint transform(MyEdge e) {return MySequentialGraphVars.getSequentialGraphViewer().setEdgeColor(e);}
        });
    }

    public static void setEdgeValue() {
        if (MySequentialGraphVars.getSequentialGraphViewer().vc.selectedNodeNeighborNodeTypeSelector.getSelectedIndex() > 0) {
            if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() == 0) {
                MySequentialGraphVars.getSequentialGraphViewer().edgeValName = "NONE";
                Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
                for (MyEdge e : edges) {
                    if (e.getCurrentValue() > 0) {
                        e.setCurrentValue(0);
                    }
                }
                MySequentialGraphVars.getSequentialGraphViewer().getRenderContext().setEdgeStrokeTransformer(new Transformer<MyEdge, Stroke>() {
                    @Override public Stroke transform(MyEdge myEdge) {
                        return new BasicStroke(0f);
                    }
                });
            } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() == 1) {
                MySequentialGraphVars.getSequentialGraphViewer().edgeValName = "DEFALUT";
                Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
                for (MyEdge e : edges) {
                    if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeNameSet.contains(e.getSource().getName()) || MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeNameSet.contains(e.getDest().getName())) {
                        e.setCurrentValue(MySequentialGraphVars.g.DEFALUT_EDGE_VALUE);
                    }
                }
                MySequentialGraphVars.getSequentialGraphViewer().getRenderContext().setEdgeStrokeTransformer(new Transformer<MyEdge, Stroke>() {
                    @Override public Stroke transform(MyEdge e) {
                        if (e.getCurrentValue() == 0) {
                            return new BasicStroke(0.0f);
                        } else {
                            float edgeStrokeWeight = e.getCurrentValue()/ MySequentialGraphVars.g.MX_E_VAL;
                            //System.out.println(e.getSource().getName() + " - " + e.getDest().getName() + ": " + edgeStrokeWeight*MX_E_STK);
                            if (MySequentialGraphVars.getSequentialGraphViewer().vc.selectedNodeNeighborNodeTypeSelector.isShowing()) {
                                return new BasicStroke(10f+(edgeStrokeWeight * MySequentialGraphVars.getSequentialGraphViewer().MX_E_STK));
                            } else {
                                return new BasicStroke(edgeStrokeWeight * MySequentialGraphVars.getSequentialGraphViewer().MX_E_STK);
                            }
                        }
                    }
                });
                MySequentialGraphVars.g.MX_E_VAL = 40f;
            } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() == 2) {
                MySequentialGraphVars.getSequentialGraphViewer().edgeValName = "CONTRIBUTION";
                float max = 0f;
                if (MySequentialGraphVars.getSequentialGraphViewer().vc.selectedNodeNeighborNodeTypeSelector.getSelectedItem().toString().equals("S.")) {
                    Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
                    for (MyEdge e : edges) {
                        if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeNameSet.contains(e.getSource().getName())) {
                            e.setCurrentValue(e.getContribution());
                            e.getSource().setCurrentValue(e.getCurrentValue());
                            if (max < e.getCurrentValue()) {
                                max = e.getCurrentValue();
                            }
                        } else {
                            e.setCurrentValue(0f);
                            e.getSource().setCurrentValue(0f);
                            e.getDest().setCurrentValue(0f);
                        }
                    }
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.selectedNodeNeighborNodeTypeSelector.getSelectedItem().toString().equals("P.")) {
                    Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
                    for (MyEdge e : edges) {
                        if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeNameSet.contains(e.getDest().getName())) {
                            e.setCurrentValue(e.getContribution());
                            e.getSource().setCurrentValue(e.getCurrentValue());
                            if (max < e.getCurrentValue()) {
                                max = e.getCurrentValue();
                            }
                        } else {
                            e.setCurrentValue(0f);
                            e.getSource().setCurrentValue(0f);
                            e.getDest().setCurrentValue(0f);
                        }
                    }
                }
                if (max > 0) {
                    MySequentialGraphVars.g.MX_E_VAL = max;
                    MySequentialGraphVars.g.MX_N_VAL = max;
                }
            }
        } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthNeighborNodeTypeSelector.getSelectedIndex() > 0) {
            if (MySequentialGraphVars.isTimeOn) {
                if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() == 0) {
                    MySequentialGraphVars.getSequentialGraphViewer().edgeValName = "NONE";
                    removeEdges();
                    MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueBarChart.setSelected(false);
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() == 1) {
                    MySequentialGraphVars.getSequentialGraphViewer().edgeValName = "DEFAULT";
                    setDefaultValuesToEdges();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() == 2) {
                    MySequentialGraphVars.getSequentialGraphViewer().edgeValName = "CONTRIBUTION";
                    setContributionToDepthEdges();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() == 3) {
                    MySequentialGraphVars.getSequentialGraphViewer().edgeValName = "UNIQUE-CONTRIBUTION";
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() == 4) {
                    MySequentialGraphVars.getSequentialGraphViewer().edgeValName = "AVERAGE-TIME";
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() == 5) {
                    MySequentialGraphVars.getSequentialGraphViewer().edgeValName = "TOTAL-TIME";
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() == 6) {
                    MySequentialGraphVars.getSequentialGraphViewer().edgeValName = "MAXIMUM-TIME";
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() == 7) {
                    MySequentialGraphVars.getSequentialGraphViewer().edgeValName = "MINIMUM-TIME";
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() == 8) {
                    MySequentialGraphVars.getSequentialGraphViewer().edgeValName = "SUPPORT";
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() == 9) {
                    MySequentialGraphVars.getSequentialGraphViewer().edgeValName = "CONFIDENCE";
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() == 10) {
                    MySequentialGraphVars.getSequentialGraphViewer().edgeValName = "LIFT";
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() == 11) {
                    MySequentialGraphVars.getSequentialGraphViewer().edgeValName = "BETWEENESS";
                    setEdgeBetweenessToEdges();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() > 11) {
                    MySequentialGraphVars.getSequentialGraphViewer().edgeValName = MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedItem().toString().replaceAll(" ", "");
                }
                //MySequentialGraphVars.getSequentialGraphViewer().vc.vTxtStat.setTextStatistics();
                MySequentialGraphVars.getSequentialGraphViewer().revalidate();
                MySequentialGraphVars.getSequentialGraphViewer().repaint();
            } else {
                if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() == 0) {
                    MySequentialGraphVars.getSequentialGraphViewer().edgeValName = "NONE";
                }else if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() == 1) {
                    MySequentialGraphVars.getSequentialGraphViewer().edgeValName = "DEFAULT";
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() == 2) {
                    MySequentialGraphVars.getSequentialGraphViewer().edgeValName = "CONTRIBUTION";
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() == 3) {
                    MySequentialGraphVars.getSequentialGraphViewer().edgeValName = "UNIQUE CONTRIBUTION";
                }  else if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() == 4) {
                    MySequentialGraphVars.getSequentialGraphViewer().edgeValName = "SUPPORT";
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() == 5) {
                    MySequentialGraphVars.getSequentialGraphViewer().edgeValName = "CONFIDENCE";
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() == 6) {
                    MySequentialGraphVars.getSequentialGraphViewer().edgeValName = "LIFT";
                }else if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() == 7) {
                    MySequentialGraphVars.getSequentialGraphViewer().edgeValName = "BETWEENESS";
                    setEdgeBetweenessToEdges();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() > 7) {
                    MySequentialGraphVars.getSequentialGraphViewer().edgeValName = MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedItem().toString().replaceAll(" ", "");
                }
                //MySequentialGraphVars.getSequentialGraphViewer().vc.vTxtStat.setTextStatistics();
                MySequentialGraphVars.getSequentialGraphViewer().revalidate();
                MySequentialGraphVars.getSequentialGraphViewer().repaint();
            }
        } else {
            if (MySequentialGraphVars.isTimeOn) {
                if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() == 0) {
                    MySequentialGraphVars.getSequentialGraphViewer().edgeValName = "NONE";
                    removeEdges();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() == 1) {
                    MySequentialGraphVars.getSequentialGraphViewer().edgeValName = "DEFAULT";
                    setDefaultValuesToEdges();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() == 2) {
                    MySequentialGraphVars.getSequentialGraphViewer().edgeValName = "CONTRIBUTION";
                    setContributionToEdges();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() == 3) {
                    MySequentialGraphVars.getSequentialGraphViewer().edgeValName = "UNIQUE-CONTRIBUTION";
                    setUniqueContributionToEdges();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() == 4) {
                    MySequentialGraphVars.getSequentialGraphViewer().edgeValName = "AVERAGE-TIME";
                    setAverageTimeToEdges();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() == 5) {
                    MySequentialGraphVars.getSequentialGraphViewer().edgeValName = "TOTAL-TIME";
                    setTotalTimeToEdges();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() == 6) {
                    MySequentialGraphVars.getSequentialGraphViewer().edgeValName = "MAXIMUM-TIME";
                    setMaximumTimeToEdges();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() == 7) {
                    MySequentialGraphVars.getSequentialGraphViewer().edgeValName = "MINIMUM-TIME";
                    setMinimumTimeToEdges();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() == 8) {
                    MySequentialGraphVars.getSequentialGraphViewer().edgeValName = "SUPPORT";
                    setSupportValueToEdges();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() == 9) {
                    MySequentialGraphVars.getSequentialGraphViewer().edgeValName = "CONFIDENCE";
                    setConfidenceValueToEdges();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() == 10) {
                    MySequentialGraphVars.getSequentialGraphViewer().edgeValName = "LIFT";
                    setLiftValueToEdges();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() == 11) {
                    MySequentialGraphVars.getSequentialGraphViewer().edgeValName = "BETWEENESS";
                    setEdgeBetweenessToEdges();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() > 11) {
                    MySequentialGraphVars.getSequentialGraphViewer().edgeValName = MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedItem().toString().replaceAll(" ", "");
                    Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
                    float maxVal = 0.00f;
                    for (MyEdge e : edges) {
                        double edgeValue = e.getEdgeValueMap().get(MySequentialGraphVars.getSequentialGraphViewer().edgeValName);
                        synchronized (e) {
                            e.setCurrentValue((float) edgeValue);
                        }
                        if (maxVal < edgeValue) {
                            maxVal = e.getCurrentValue();
                        }
                    }
                    MySequentialGraphVars.g.MX_E_VAL = maxVal;
                }
                if (MySequentialGraphVars.getSequentialGraphViewer().selectedNode != null) {
                    MySequentialGraphVars.getSequentialGraphViewer().vc.updateSelectedNodeStatTable();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().multiNodes != null) {
                    MySequentialGraphVars.getSequentialGraphViewer().vc.updateMultiNodeStatTable();
                } else {
                    MyViewerComponentControllerUtil.setGraphLevelTableStatistics();
                }
                MySequentialGraphVars.getSequentialGraphViewer().revalidate();
                MySequentialGraphVars.getSequentialGraphViewer().repaint();
            } else {
                if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() == 0) {
                    MySequentialGraphVars.getSequentialGraphViewer().edgeValName = "NONE";
                    removeEdges();
                }else if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() == 1) {
                    MySequentialGraphVars.getSequentialGraphViewer().edgeValName = "DEFAULT";
                    setDefaultValuesToEdges();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() == 2) {
                    MySequentialGraphVars.getSequentialGraphViewer().edgeValName = "CONTRIBUTION";
                    setContributionToEdges();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() == 3) {
                    MySequentialGraphVars.getSequentialGraphViewer().edgeValName = "UNIQUE CONTRIBUTION";
                    setUniqueContributionToEdges();
                }  else if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() == 4) {
                    MySequentialGraphVars.getSequentialGraphViewer().edgeValName = "SUPPORT";
                    setSupportValueToEdges();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() == 5) {
                    MySequentialGraphVars.getSequentialGraphViewer().edgeValName = "CONFIDENCE";
                    setConfidenceValueToEdges();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() == 6) {
                    MySequentialGraphVars.getSequentialGraphViewer().edgeValName = "LIFT";
                    setLiftValueToEdges();
                }  else if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() == 7) {
                    MySequentialGraphVars.getSequentialGraphViewer().edgeValName = "BETWEENESS";
                    setEdgeBetweenessToEdges();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() > 7) {
                    MySequentialGraphVars.getSequentialGraphViewer().edgeValName = MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedItem().toString().replaceAll(" ", "");
                    Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
                    float maxVal = 0.00f;
                    for (MyEdge e : edges) {
                        double edgeValue = e.getEdgeValueMap().get(MySequentialGraphVars.getSequentialGraphViewer().edgeValName);
                            e.setCurrentValue((float) edgeValue);
                        if (maxVal < edgeValue) {maxVal = e.getCurrentValue();}
                    }
                    MySequentialGraphVars.g.MX_E_VAL = maxVal;
                }
                if (MySequentialGraphVars.getSequentialGraphViewer().selectedNode != null) {
                    MySequentialGraphVars.getSequentialGraphViewer().vc.updateSelectedNodeStatTable();
                } else if (MySequentialGraphVars.getSequentialGraphViewer().multiNodes != null) {
                    MySequentialGraphVars.getSequentialGraphViewer().vc.updateMultiNodeStatTable();
                } else {
                    MyViewerComponentControllerUtil.setGraphLevelTableStatistics();
                }
                MySequentialGraphVars.getSequentialGraphViewer().revalidate();
                MySequentialGraphVars.getSequentialGraphViewer().repaint();
            }
        }
        setEdgeColor();
       // MySequentialGraphVars.app.getSequentialGraphDashboard().networkTitledBorder.setTitle(MySequentialGraphVars.app.getSequentialGraphDashboard().setGraphLevelTextStatistics());
       // MySequentialGraphVars.app.revalidate();
       // MySequentialGraphVars.app.repaint();
    }

    public static void setSupportValueToEdges() {
        float maxVal = 0;
        Collection<MyEdge> es = MySequentialGraphVars.g.getEdges();
        for (MyEdge e : es) {
            if (e.getCurrentValue() == 0 || e.getDest().getCurrentValue() == 0 || e.getSource().getCurrentValue() == 0) {
                continue;
            }

            e.setCurrentValue(e.getSupport());
            if (maxVal < e.getCurrentValue()) {
                maxVal = e.getCurrentValue();
            }
        }

        if (maxVal > 0) {
            MySequentialGraphVars.g.MX_E_VAL = maxVal;
        }
    }

    public static void setConfidenceValueToEdges() {
        float maxVal = 0;
        Collection<MyEdge> es = MySequentialGraphVars.g.getEdges();
        for (MyEdge e : es) {
            if (e.getCurrentValue() == 0 || e.getDest().getCurrentValue() == 0 || e.getSource().getCurrentValue() == 0) {
                continue;
            }

            e.setCurrentValue(e.getConfidence());
            if (maxVal < e.getCurrentValue()) {
                maxVal = e.getCurrentValue();
            }
        }

        if (maxVal > 0) {
            MySequentialGraphVars.g.MX_E_VAL = maxVal;
        }
    }

    public static void setLiftValueToEdges() {
        float maxVal = 0;
        Collection<MyEdge> es = MySequentialGraphVars.g.getEdges();
        for (MyEdge e : es) {
            if (e.getCurrentValue() == 0 || e.getDest().getCurrentValue() == 0 || e.getSource().getCurrentValue() == 0) {
                continue;
            }
            e.tmpValue = e.getLift();
            if (maxVal < e.tmpValue) {
                maxVal = e.tmpValue;
            }
            e.setCurrentValue(e.tmpValue);
        }

        if (maxVal > 0) {
            MySequentialGraphVars.g.MX_E_VAL = maxVal;
        }
    }

    public static void setMaximumTimeToEdges() {
        float maxVal = 0.00f;
        Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
        for (MyEdge e : edges) {
            if (e.getCurrentValue() == 0) continue;;
            float nodeMaxTime = 0;
            String graphEdgeName = e.getSource().getName()+"-"+e.getDest().getName();
            for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                for (int i = 1; i < MySequentialGraphVars.seqs[s].length; i++) {
                    String [] pProperty = MySequentialGraphVars.seqs[s][i-1].split(":");
                    String [] sProperty = MySequentialGraphVars.seqs[s][i].split(":");
                    String seqEdgeName = pProperty[0]+"-"+sProperty[0];
                    if (graphEdgeName.equals(seqEdgeName)) {
                        float currentSeqTime = Float.valueOf(sProperty[1]);
                        if (currentSeqTime > nodeMaxTime) {nodeMaxTime = currentSeqTime;}
                    }
                }
            }
            e.setCurrentValue(e.getConfidence());
            if (maxVal < e.getCurrentValue()) {
                maxVal = e.getCurrentValue();
            }
        }

        if (maxVal > 0) {
            MySequentialGraphVars.g.MX_E_VAL = maxVal;
        }
    }

    public static void setEdgesToZeroValues() {
        Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
        for (MyEdge e : edges) {
            e.setCurrentValue(0f);
        }
    }

    public static void setTotalTimeToEdges() {
        float maxVal = 0.00f;
        Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
        for (MyEdge e : edges) {
            if (e.getCurrentValue() == 0) continue;
            float totTime = 0;
            String graphEdgeName = e.getSource().getName() + "-" + e.getDest().getName();
            for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                for (int i = 1; i < MySequentialGraphVars.seqs[s].length; i++) {
                    String[] pProperty = MySequentialGraphVars.seqs[s][i - 1].split(":");
                    String[] sProperty = MySequentialGraphVars.seqs[s][i].split(":");
                    String seqEdgeName = pProperty[0] + "-" + sProperty[0];
                    if (graphEdgeName.equals(seqEdgeName)) {
                        totTime += Float.valueOf(sProperty[1]);
                    }
                }
            }
            if (totTime > maxVal) {
                maxVal = totTime;
            }
            e.tmpValue = totTime;
            e.setCurrentValue(e.tmpValue);
        }

        if (maxVal > 0) {
            MySequentialGraphVars.g.MX_E_VAL = maxVal;
        }
    }

    public static double getAverageEdgeValue() {
        if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() < 2) {
            return 0.00D;
        } else {
            float totalValue = 0.00f;
            Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
            for (MyEdge e : edges) {
                if (e.getCurrentValue() == 0 || e.getDest().getCurrentValue() == 0 || e.getSource().getCurrentValue() == 0) {
                    continue;
                }
                totalValue += e.getCurrentValue();
            }
            return (totalValue/edges.size());
        }
    }

    private static void setContributionToEdges() {
        Collection<MyEdge> es = MySequentialGraphVars.g.getEdges();
        float maxVal = 0.00f;
        for (MyEdge e : es) {
            if (e.getCurrentValue() == 0 || e.getDest().getCurrentValue() == 0 || e.getSource().getCurrentValue() == 0) {
                continue;
            }
            e.setCurrentValue(e.getContribution());
            if (maxVal < e.getCurrentValue()) {
                maxVal = e.getCurrentValue();
            }
        }

        if (maxVal > 0) {
            MySequentialGraphVars.g.MX_E_VAL = maxVal;
        }
    }

    private static void setContributionToDepthEdges() {
        Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
        if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthNeighborNodeTypeSelector.isShowing() && MySequentialGraphVars.getSequentialGraphViewer().vc.depthNeighborNodeTypeSelector.getSelectedItem().toString().equals("S.")) {
            float maxVal = 0.0f;
            for (MyEdge e : edges) {
                if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeSuccessorMaps.containsKey(e.getSource().getName()) &&
                    MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeSuccessorMaps.get(e.getSource().getName()).containsKey(e.getDest().getName())) {
                    e.setCurrentValue(MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeSuccessorMaps.get(e.getSource().getName()).get(e.getDest().getName()));
                    if (e.getCurrentValue() > maxVal) {
                        maxVal = e.getCurrentValue();
                    }
                } else {
                    e.setCurrentValue(0f);
                }
            }
            if (maxVal > 0) {
                MySequentialGraphVars.g.MX_E_VAL = maxVal;
            }
        } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.selectedNodeNeighborNodeTypeSelector.isShowing() && MySequentialGraphVars.getSequentialGraphViewer().vc.selectedNodeNeighborNodeTypeSelector.getSelectedItem().toString().equals("S.")) {
            float maxVal = 0.0f;
            for (MyEdge e : edges) {
                if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeSuccessorMaps.containsKey(e.getSource().getName()) &&
                        MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeSuccessorMaps.get(e.getSource().getName()).containsKey(e.getDest().getName())) {
                    e.setCurrentValue(MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeSuccessorMaps.get(MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeNameSet.iterator().next()).get(e.getDest().getName()));
                    if (e.getCurrentValue() > maxVal) {
                        maxVal = e.getCurrentValue();
                    }
                } else {
                    e.setCurrentValue(0f);
                }
            }
            if (maxVal > 0) {
                MySequentialGraphVars.g.MX_E_VAL = maxVal;
                MySequentialGraphVars.g.MX_N_VAL = maxVal;
            }
            //System.out.println(MySequentialGraphVars.g.MX_E_VAL);
        } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthNeighborNodeTypeSelector.getSelectedItem().toString().equals("P.")) {
            float maxVal = 0.0f;
            for (MyEdge e : edges) {
                if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodePredecessorMaps.containsKey(e.getDest().getName()) &&
                    MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodePredecessorMaps.get(e.getDest().getName()).containsKey(e.getSource().getName())) {
                    e.setCurrentValue(MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodePredecessorMaps.get(e.getDest().getName()).get(e.getSource().getName()));
                    if (e.getCurrentValue() > maxVal) {
                        maxVal = e.getCurrentValue();
                    }
                } else {
                    e.setCurrentValue(0f);
                }
            }

            if (maxVal > 0) {
                MySequentialGraphVars.g.MX_E_VAL = maxVal;
            }
        }
    }

    private static void setUniqueContributionToEdges() {
        Collection<MyEdge> es = MySequentialGraphVars.g.getEdges();
        float maxVal = 0.00f;
        for (MyEdge e : es) {
            if (e.getCurrentValue() == 0 || e.getDest().getCurrentValue() == 0 || e.getSource().getCurrentValue() == 0) {
                continue;
            }
            e.setCurrentValue(e.getContribution());
            if (maxVal < e.getCurrentValue()) {
                maxVal = e.getCurrentValue();
            }
        }

        if (maxVal > 0) {
            MySequentialGraphVars.g.MX_E_VAL = maxVal;
        }
    }

    private static void setEdgeBetweenessToEdges() {
        float maxVal = 0f;
        BetweennessCentrality<MyNode, MyEdge> betweenness = new BetweennessCentrality<>(MySequentialGraphVars.g);
        betweenness.setRemoveRankScoresOnFinalize(false); // 최종 betweenness centrality 값 유지.
        betweenness.evaluate();
        Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
        for (MyEdge e : edges) {
            if (e.getCurrentValue() > 0 && e.getSource().getCurrentValue() > 0 && e.getDest().getCurrentValue() > 0) {
                e.setCurrentValue(Float.valueOf(MyMathUtil.twoDecimalFormat(betweenness.getEdgeRankScore(e))));
                if (maxVal < e.getCurrentValue()) {
                    maxVal = e.getCurrentValue();
                }
            }
        }

        if (maxVal > 0) {
            MySequentialGraphVars.g.MX_E_VAL = maxVal;
        }
    }

    public static int getEdgesGreaterThanZero() {
        Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
        int total = 0;
        for (MyEdge e : edges) {
            if (e.getCurrentValue() > 0 && e.getSource().getCurrentValue() > 0 && e.getDest().getCurrentValue() > 0) {
                total++;
            }
        }
        return total;
    }

    public static int getOnEdgeCount() {
        Collection<MyEdge> es = MySequentialGraphVars.g.getEdges();
        int cnt = 0;
        for (MyEdge e : es) {
            if (e.getSource().getCurrentValue() > 0 && e.getDest().getCurrentValue() > 0 && e.getCurrentValue() > 0) {
                cnt++;
            }
        }
        return cnt;
    }

    public static double getMinimumEdgeValue() {
        if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() < 2) {
            return 0.00D;
        } else {
            Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
            float minVal = 1000000000.00f;
            for (MyEdge e : edges) {
                if (e.getSource().getCurrentValue() > 0 && e.getDest().getCurrentValue() > 0 && e.getCurrentValue() > 0) {
                    if (minVal > e.getCurrentValue()) {
                        minVal = e.getCurrentValue();
                    }
                }
            }
            return minVal;
        }
    }

    public static double getMaximumEdgeValue() {
        double maxVal = 0d;
        if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() < 2) {
            return 0.00D;
        } else {
            Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
            for (MyEdge e : edges) {
                if (e.getSource().getCurrentValue() > 0 && e.getDest().getCurrentValue() > 0 && e.getCurrentValue() > 0) {
                    if (maxVal < e.getCurrentValue()) {
                        maxVal = e.getCurrentValue();
                    }
                }
            }
        }
        return maxVal;
    }

    public static float getMaximumUniqueContribution() {
        float maxVal = 0;
        Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
        for (MyEdge e : edges) {
            if (e.getSource().getCurrentValue() > 0 && e.getDest().getCurrentValue() > 0 && e.getCurrentValue() > 0) {
                if (maxVal < e.getUniqueContribution()) {
                    maxVal = e.getUniqueContribution();
                }
            }
        }
        return maxVal;
    }

    public static double getMinimumUniqueContribution() {
        long minVal = 100000000000000L;
        Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
        for (MyEdge e : edges) {
            if (e.getCurrentValue() == 0 && (e.getDest().getCurrentValue() == 0 || e.getSource().getCurrentValue() == 0)) {
                continue;
            }

            if (minVal > e.getUniqueContribution()) {
                minVal = e.getUniqueContribution();
            }
        }
        return (minVal == 100000000000000L ? 0 : minVal);
    }

    public static double getEdgeValueStandardDeviation() {
        if (MySequentialGraphVars.getSequentialGraphViewer().vc.edgeValueSelecter.getSelectedIndex() < 2) {
            return 0.00D;
        } else {
            int numEdges = 0;
            Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
            double sum = 0.00d;
            for (MyEdge e : edges) {
                if (e.getCurrentValue() == 0 && (e.getDest().getCurrentValue() == 0 || e.getSource().getCurrentValue() == 0)) {
                    continue;
                }
                numEdges++;
                sum += e.getCurrentValue();
            }
            double mean = sum/numEdges;
            double std = 0.00D;
            for(MyEdge e : edges) {
                if (e.getCurrentValue() == 0 && (e.getDest().getCurrentValue() == 0 || e.getSource().getCurrentValue() == 0)) {
                    continue;
                }
                std += Math.pow(e.getCurrentValue() - mean, 2);
            }
            return (std/numEdges);
        }
    }

    public static double getUniqueContributionStandardDeviation() {
        int numEdges = 0;
        Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
        double sum = 0.00d;
        for (MyEdge e : edges) {
            if (e.getCurrentValue() == 0 && (e.getDest().getCurrentValue() == 0 || e.getSource().getCurrentValue() == 0)) {
                continue;
            }
            numEdges++;
            sum += e.getUniqueContribution();
        }
        double mean = sum / numEdges;
        double std = 0.00D;
        for (MyEdge e : edges) {
            if (e.getCurrentValue() == 0 && (e.getDest().getCurrentValue() == 0 || e.getSource().getCurrentValue() == 0)) {
                continue;
            }
            std += Math.pow(e.getCurrentValue() - mean, 2);
        }
        return Math.sqrt(std / numEdges);

    }

    public static void setMinimumTimeToEdges() {
        float maxVal = 0.00f;
        Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
        for (MyEdge e : edges) {
            if (e.getCurrentValue() == 0) continue;
            float maxTime = 0;
            String graphEdgeName = e.getSource().getName()+"-"+e.getDest().getName();
            for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                for (int i = 1; i < MySequentialGraphVars.seqs[s].length; i++) {
                    String [] pProperty = MySequentialGraphVars.seqs[s][i-1].split(":");
                    String [] sProperty = MySequentialGraphVars.seqs[s][i].split(":");
                    String seqEdgeName = pProperty[0]+"-"+sProperty[0];
                    if (graphEdgeName.equals(seqEdgeName)) {
                        float currentSeqTime = Float.valueOf(sProperty[1]);
                        if (currentSeqTime > maxTime) {
                            maxTime = currentSeqTime;
                        }
                    }
                }
            }
            if (maxTime > maxVal) {
                maxVal = maxTime;
            }
            e.setCurrentValue(maxTime);
        }

        if (maxVal > 0) {
            MySequentialGraphVars.g.MX_E_VAL = maxVal;
        }
    }

    public static double getAverageUniqueContribution() {
        Collection<MyEdge> es = MySequentialGraphVars.g.getEdges();
        double total = 0D;
        int count = 0;
        for (MyEdge e : es) {
            if (e.getCurrentValue() == 0 || e.getDest().getCurrentValue() == 0 || e.getSource().getCurrentValue() == 0) {
                continue;
            }
            total += (float)e.getUniqueContribution();
            count++;
        }
        return (count > 0 ? total/count : 0.00D);
    }

    public static void setAverageTimeToEdges() {
        float maxVal = 0.00f;
        Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
        for (MyEdge e : edges) {
            if (e.getCurrentValue() == 0) continue;
            float totalTime = 0;
            int seqEdgeCnt = 0;
            String graphEdgeName = e.getSource().getName() + "-" + e.getDest().getName();
            for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                for (int i = 1; i < MySequentialGraphVars.seqs[s].length; i++) {
                    String [] pProperty = MySequentialGraphVars.seqs[s][i-1].split(":");
                    String [] sProperty = MySequentialGraphVars.seqs[s][i].split(":");
                    String seqEdgeName = pProperty[0]+"-"+sProperty[0];
                    if (graphEdgeName.equals(seqEdgeName)) {
                        totalTime += Float.valueOf(sProperty[1]);
                        seqEdgeCnt++;
                    }
                }
            }
            float avgTime = (totalTime == 0 ? 0.00f : (totalTime/seqEdgeCnt));
            if (avgTime > maxVal) {
                maxVal = avgTime;
            }
            e.setCurrentValue(Float.valueOf(MyMathUtil.twoDecimalFormat(avgTime)));
        }

        if (maxVal > 0) {
            MySequentialGraphVars.g.MX_E_VAL = maxVal;
        }
    }

}
