package datamining.utils;

import datamining.graph.MyEdge;
import datamining.graph.MyNode;
import datamining.graph.stats.MyTextStatistics;
import datamining.utils.system.MyVars;
import edu.uci.ics.jung.algorithms.importance.BetweennessCentrality;
import org.apache.commons.collections15.Transformer;

import java.awt.*;
import java.util.Collection;

public class MyEdgeUtil {

    public static void setDefaultValuesToEdges() {
        Collection<MyEdge> es = MyVars.g.getEdges();
        for (MyEdge e : es) {
            e.setCurrentValue(MyVars.g.DEFALUT_EDGE_VALUE);
        }
        synchronized (MyVars.g.MX_E_VAL) {
            MyVars.g.MX_E_VAL = 42f;
        }
    }

    public static void removeEdges() {
        MyVars.getViewer().getRenderContext().setEdgeDrawPaintTransformer(new Transformer<MyEdge, Paint>() {@Override
        public Paint transform(MyEdge e) {
            return new Color(0.0f, 0.0f, 0.0f, 0.0f);
        }
        });
        MyVars.getViewer().getRenderContext().setEdgeLabelTransformer(new Transformer<MyEdge, String>() {
            @Override
            public String transform(MyEdge e) {
                return "";
            }
        });
        MyVars.getViewer().revalidate();
        MyVars.getViewer().repaint();
    }

    public static void setEdgeColor() {
        MyVars.getViewer().getRenderContext().setEdgeDrawPaintTransformer(new Transformer<MyEdge, Paint>() {@Override
        public Paint transform(MyEdge e) {return MyVars.getViewer().setEdgeColor(e);}
        });
    }

    public static void setEdgeValue() {
        setEdgeColor();
        if (MyVars.getViewer().vc.selectedNodeNeighborNodeTypeSelector.getSelectedIndex() > 0) {
            if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() == 0) {
                MyVars.getViewer().edgeValName = "NONE";
                Collection<MyEdge> edges = MyVars.g.getEdges();
                for (MyEdge e : edges) {
                    if (e.getCurrentValue() > 0) {
                        e.setCurrentValue(0);
                    }
                }
                MyVars.getViewer().getRenderContext().setEdgeStrokeTransformer(new Transformer<MyEdge, Stroke>() {
                    @Override public Stroke transform(MyEdge myEdge) {
                        return new BasicStroke(0f);
                    }
                });
            } else if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() == 1) {
                MyVars.getViewer().edgeValName = "DEFALUT";
                Collection<MyEdge> edges = MyVars.g.getEdges();
                for (MyEdge e : edges) {
                    if (MyVars.getViewer().vc.depthNodeNameSet.contains(e.getSource().getName()) || MyVars.getViewer().vc.depthNodeNameSet.contains(e.getDest().getName())) {
                        e.setCurrentValue(MyVars.g.DEFALUT_EDGE_VALUE);
                    }
                }
                MyVars.getViewer().getRenderContext().setEdgeStrokeTransformer(new Transformer<MyEdge, Stroke>() {
                    @Override public Stroke transform(MyEdge e) {
                        if (e.getCurrentValue() == 0) {
                            return new BasicStroke(0.0f);
                        } else {
                            float edgeStrokeWeight = e.getCurrentValue()/MyVars.g.MX_E_VAL;
                            //System.out.println(e.getSource().getName() + " - " + e.getDest().getName() + ": " + edgeStrokeWeight*MX_E_STK);
                            if (MyVars.getViewer().vc.selectedNodeNeighborNodeTypeSelector.isShowing()) {
                                return new BasicStroke(10f+(edgeStrokeWeight * MyVars.getViewer().MX_E_STK));
                            } else {
                                return new BasicStroke(edgeStrokeWeight * MyVars.getViewer().MX_E_STK);
                            }
                        }
                    }
                });
                MyVars.g.MX_E_VAL = 40f;
            } else if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() == 2) {
                MyVars.getViewer().edgeValName = "CONTRIBUTION";
                float max = 0f;
                if (MyVars.getViewer().vc.selectedNodeNeighborNodeTypeSelector.getSelectedItem().toString().equals("S.")) {
                    Collection<MyEdge> edges = MyVars.g.getEdges();
                    for (MyEdge e : edges) {
                        if (MyVars.getViewer().vc.depthNodeNameSet.contains(e.getSource().getName())) {
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
                } else if (MyVars.getViewer().vc.selectedNodeNeighborNodeTypeSelector.getSelectedItem().toString().equals("P.")) {
                    Collection<MyEdge> edges = MyVars.g.getEdges();
                    for (MyEdge e : edges) {
                        if (MyVars.getViewer().vc.depthNodeNameSet.contains(e.getDest().getName())) {
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
                    MyVars.g.MX_E_VAL = max;
                    MyVars.g.MX_N_VAL = max;
                }
            }
        } else if (MyVars.getViewer().vc.depthNeighborNodeTypeSelector.getSelectedIndex() > 0) {
            if (MyVars.isTimeOn) {
                if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() == 0) {
                    MyVars.getViewer().edgeValName = "NONE";
                    removeEdges();
                    MyVars.getViewer().vc.edgeValueBarChart.setSelected(false);
                } else if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() == 1) {
                    MyVars.getViewer().edgeValName = "DEFAULT";
                    setDefaultValuesToEdges();
                } else if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() == 2) {
                    MyVars.getViewer().edgeValName = "CONTRIBUTION";
                    setContributionToDepthEdges();
                } else if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() == 3) {
                    MyVars.getViewer().edgeValName = "UNIQUE-CONTRIBUTION";
                } else if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() == 4) {
                    MyVars.getViewer().edgeValName = "AVERAGE-TIME";
                } else if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() == 5) {
                    MyVars.getViewer().edgeValName = "TOTAL-TIME";
                } else if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() == 6) {
                    MyVars.getViewer().edgeValName = "MAXIMUM-TIME";
                } else if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() == 7) {
                    MyVars.getViewer().edgeValName = "MINIMUM-TIME";
                } else if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() == 8) {
                    MyVars.getViewer().edgeValName = "SUPPORT";
                } else if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() == 9) {
                    MyVars.getViewer().edgeValName = "CONFIDENCE";
                } else if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() == 10) {
                    MyVars.getViewer().edgeValName = "LIFT";
                } else if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() == 11) {
                    MyVars.getViewer().edgeValName = "BETWEENESS";
                    setEdgeBetweenessToEdges();
                } else if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() > 11) {
                    MyVars.getViewer().edgeValName = MyVars.getViewer().vc.edgeValueSelecter.getSelectedItem().toString().replaceAll(" ", "");
                }
            } else {
                if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() == 0) {
                    MyVars.getViewer().edgeValName = "NONE";
                }else if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() == 1) {
                    MyVars.getViewer().edgeValName = "DEFAULT";
                } else if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() == 2) {
                    MyVars.getViewer().edgeValName = "CONTRIBUTION";
                } else if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() == 3) {
                    MyVars.getViewer().edgeValName = "UNIQUE CONTRIBUTION";
                }  else if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() == 4) {
                    MyVars.getViewer().edgeValName = "SUPPORT";
                } else if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() == 5) {
                    MyVars.getViewer().edgeValName = "CONFIDENCE";
                } else if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() == 6) {
                    MyVars.getViewer().edgeValName = "LIFT";
                }else if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() == 7) {
                    MyVars.getViewer().edgeValName = "BETWEENESS";
                    setEdgeBetweenessToEdges();
                } else if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() > 7) {
                    MyVars.getViewer().edgeValName = MyVars.getViewer().vc.edgeValueSelecter.getSelectedItem().toString().replaceAll(" ", "");
                }
            }
        } else {
            if (MyVars.isTimeOn) {
                if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() == 0) {
                    MyVars.getViewer().edgeValName = "NONE";
                    removeEdges();
                } else if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() == 1) {
                    MyVars.getViewer().edgeValName = "DEFAULT";
                    setDefaultValuesToEdges();
                } else if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() == 2) {
                    MyVars.getViewer().edgeValName = "CONTRIBUTION";
                    setContributionToEdges();
                    MyViewerControlComponentUtil.setGraphLevelTableStatistics();
                } else if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() == 3) {
                    MyVars.getViewer().edgeValName = "UNIQUE-CONTRIBUTION";
                    setUniqueContributionToEdges();
                    MyViewerControlComponentUtil.setGraphLevelTableStatistics();
                } else if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() == 4) {
                    MyVars.getViewer().edgeValName = "AVERAGE-TIME";
                    setAverageTimeToEdges();
                    MyViewerControlComponentUtil.setGraphLevelTableStatistics();
                } else if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() == 5) {
                    MyVars.getViewer().edgeValName = "TOTAL-TIME";
                    setTotalTimeToEdges();
                    MyViewerControlComponentUtil.setGraphLevelTableStatistics();
                } else if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() == 6) {
                    MyVars.getViewer().edgeValName = "MAXIMUM-TIME";
                    setMaximumTimeToEdges();
                    MyViewerControlComponentUtil.setGraphLevelTableStatistics();
                } else if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() == 7) {
                    MyVars.getViewer().edgeValName = "MINIMUM-TIME";
                    setMinimumTimeToEdges();
                    MyViewerControlComponentUtil.setGraphLevelTableStatistics();
                } else if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() == 8) {
                    MyVars.getViewer().edgeValName = "SUPPORT";
                    setSupportValueToEdges();
                    MyViewerControlComponentUtil.setGraphLevelTableStatistics();
                } else if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() == 9) {
                    MyVars.getViewer().edgeValName = "CONFIDENCE";
                    setConfidenceValueToEdges();
                    MyViewerControlComponentUtil.setGraphLevelTableStatistics();
                } else if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() == 10) {
                    MyVars.getViewer().edgeValName = "LIFT";
                    setLiftValueToEdges();
                    MyViewerControlComponentUtil.setGraphLevelTableStatistics();
                } else if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() == 11) {
                    MyVars.getViewer().edgeValName = "BETWEENESS";
                    setEdgeBetweenessToEdges();
                    MyViewerControlComponentUtil.setGraphLevelTableStatistics();
                } else if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() > 11) {
                    MyVars.getViewer().edgeValName = MyVars.getViewer().vc.edgeValueSelecter.getSelectedItem().toString().replaceAll(" ", "");
                    Collection<MyEdge> edges = MyVars.g.getEdges();
                    float maxVal = 0.00f;
                    for (MyEdge e : edges) {
                        double edgeValue = e.getEdgeValueMap().get(MyVars.getViewer().edgeValName);
                        synchronized (e) {
                            e.setCurrentValue((float) edgeValue);
                        }
                        if (maxVal < edgeValue) {
                            maxVal = e.getCurrentValue();
                        }
                    }
                    synchronized (MyVars.g.MX_E_VAL) {
                        MyVars.g.MX_E_VAL = maxVal;
                    }
                    MyViewerControlComponentUtil.setGraphLevelTableStatistics();
                }
            } else {
                if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() == 0) {
                    MyVars.getViewer().edgeValName = "NONE";
                    removeEdges();
                }else if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() == 1) {
                    MyVars.getViewer().edgeValName = "DEFAULT";
                    setDefaultValuesToEdges();
                } else if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() == 2) {
                    MyVars.getViewer().edgeValName = "CONTRIBUTION";
                    setContributionToEdges();
                    MyViewerControlComponentUtil.setGraphLevelTableStatistics();
                } else if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() == 3) {
                    MyVars.getViewer().edgeValName = "UNIQUE CONTRIBUTION";
                    setUniqueContributionToEdges();
                    MyViewerControlComponentUtil.setGraphLevelTableStatistics();
                }  else if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() == 4) {
                    MyVars.getViewer().edgeValName = "SUPPORT";
                    setSupportValueToEdges();
                    MyViewerControlComponentUtil.setGraphLevelTableStatistics();
                } else if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() == 5) {
                    MyVars.getViewer().edgeValName = "CONFIDENCE";
                    setConfidenceValueToEdges();
                    MyViewerControlComponentUtil.setGraphLevelTableStatistics();
                } else if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() == 6) {
                    MyVars.getViewer().edgeValName = "LIFT";
                    setLiftValueToEdges();
                    MyViewerControlComponentUtil.setGraphLevelTableStatistics();
                }  else if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() == 7) {
                    MyVars.getViewer().edgeValName = "BETWEENESS";
                    setEdgeBetweenessToEdges();
                    MyViewerControlComponentUtil.setGraphLevelTableStatistics();
                } else if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() > 7) {
                    MyVars.getViewer().edgeValName = MyVars.getViewer().vc.edgeValueSelecter.getSelectedItem().toString().replaceAll(" ", "");
                    Collection<MyEdge> edges = MyVars.g.getEdges();
                    float maxVal = 0.00f;
                    for (MyEdge e : edges) {
                        double edgeValue = e.getEdgeValueMap().get(MyVars.getViewer().edgeValName);
                            e.setCurrentValue((float) edgeValue);

                        if (maxVal < edgeValue) {maxVal = e.getCurrentValue();}
                    }
                        MyVars.g.MX_E_VAL = maxVal;

                    MyViewerControlComponentUtil.setGraphLevelTableStatistics();
                }
            }
            MyVars.getViewer().vc.vTxtStat.setTextStatistics();
        }
    }

    public static void setSupportValueToEdges() {
        float maxVal = 0;
        Collection<MyEdge> es = MyVars.g.getEdges();
        for (MyEdge e : es) {
            if (e.getCurrentValue() == 0 || e.getDest().getCurrentValue() == 0 || e.getSource().getCurrentValue() == 0) {
                continue;
            }
            e.tmpValue = (float) e.getSupport();

            if (maxVal < e.tmpValue) {
                maxVal = e.tmpValue;
            }
            e.setCurrentValue(e.tmpValue);
        }
        if (maxVal > 0) {
            MyVars.g.MX_E_VAL = Float.valueOf(maxVal);
        }
    }

    public static void setConfidenceValueToEdges() {
        float maxVal = 0;
        Collection<MyEdge> es = MyVars.g.getEdges();
        for (MyEdge e : es) {
            if (e.getCurrentValue() == 0 || e.getDest().getCurrentValue() == 0 || e.getSource().getCurrentValue() == 0) {
                continue;
            }
            e.tmpValue = (float) e.getConfidence();
            if (maxVal < e.tmpValue) {
                maxVal = e.tmpValue;
            }
            e.setCurrentValue(e.tmpValue);
        }
        if (maxVal > 0) {
            MyVars.g.MX_E_VAL = Float.valueOf(maxVal);
        }
    }

    public static void setLiftValueToEdges() {
        float maxVal = 0;
        Collection<MyEdge> es = MyVars.g.getEdges();
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
            MyVars.g.MX_E_VAL = Float.valueOf(maxVal);
        }
    }

    public static void setMaximumTimeToEdges() {
        float maxVal = 0.00f;
        Collection<MyEdge> edges = MyVars.g.getEdges();
        for (MyEdge e : edges) {
            if (e.getCurrentValue() == 0) continue;;
            float nodeMaxTime = 0;
            String graphEdgeName = e.getSource().getName()+"-"+e.getDest().getName();
            for (int s = 0; s < MyVars.seqs.length; s++) {
                for (int i = 1; i < MyVars.seqs[s].length; i++) {
                    String [] pProperty = MyVars.seqs[s][i-1].split(":");
                    String [] sProperty = MyVars.seqs[s][i].split(":");
                    String seqEdgeName = pProperty[0]+"-"+sProperty[0];
                    if (graphEdgeName.equals(seqEdgeName)) {
                        float currentSeqTime = Float.valueOf(sProperty[1]);
                        if (currentSeqTime > nodeMaxTime) {nodeMaxTime = currentSeqTime;}
                    }
                }
            }
            e.tmpValue = (float) e.getConfidence();
            if (nodeMaxTime > maxVal) {
                maxVal = nodeMaxTime;
            }
            e.setCurrentValue(e.tmpValue);
        }
        if (maxVal > 0) {
            MyVars.g.MX_E_VAL = Float.valueOf(maxVal);
        }
    }

    public static void setEdgesToZeroValues() {
        Collection<MyEdge> edges = MyVars.g.getEdges();
        for (MyEdge e : edges) {
            e.setCurrentValue(0f);
        }
    }

    public static void setTotalTimeToEdges() {
        float maxVal = 0.00f;
        Collection<MyEdge> edges = MyVars.g.getEdges();
        for (MyEdge e : edges) {
            if (e.getCurrentValue() == 0) continue;
            float totTime = 0;
            String graphEdgeName = e.getSource().getName() + "-" + e.getDest().getName();
            for (int s = 0; s < MyVars.seqs.length; s++) {
                for (int i = 1; i < MyVars.seqs[s].length; i++) {
                    String[] pProperty = MyVars.seqs[s][i - 1].split(":");
                    String[] sProperty = MyVars.seqs[s][i].split(":");
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
            MyVars.g.MX_E_VAL = Float.valueOf(maxVal);
        }
    }

    public static double getAverageEdgeValue() {
        if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() == 0) {return 0.00D;
        } else {
            float totalValue = 0.00f;
            Collection<MyEdge> edges = MyVars.g.getEdges();
            for (MyEdge e : edges) {
                if (e.getCurrentValue() == 0) continue;;
                totalValue += e.getCurrentValue();
            }
            return (totalValue/edges.size());
        }
    }

    private static void setContributionToEdges() {
        Collection<MyEdge> es = MyVars.g.getEdges();
        float maxVal = 0.00f;
        for (MyEdge e : es) {
            if (e.getCurrentValue() == 0 || e.getDest().getCurrentValue() == 0 || e.getSource().getCurrentValue() == 0) {
                continue;
            }
            e.tmpValue = e.getContribution();
            if (maxVal < e.tmpValue) {
                maxVal = e.tmpValue;
            }
            e.setCurrentValue(e.tmpValue);
        }
        if (maxVal > 0) {
            MyVars.g.MX_E_VAL = Float.valueOf(maxVal);
        }
    }

    private static void setContributionToDepthEdges() {
        Collection<MyEdge> edges = MyVars.g.getEdges();
        if (MyVars.getViewer().vc.depthNeighborNodeTypeSelector.isShowing() && MyVars.getViewer().vc.depthNeighborNodeTypeSelector.getSelectedItem().toString().equals("S.")) {
            float max = 0.0f;
            for (MyEdge e : edges) {
                if (MyVars.getViewer().vc.depthNodeSuccessorMaps.containsKey(e.getSource().getName()) &&
                    MyVars.getViewer().vc.depthNodeSuccessorMaps.get(e.getSource().getName()).containsKey(e.getDest().getName())) {
                    e.setCurrentValue(MyVars.getViewer().vc.depthNodeSuccessorMaps.get(e.getSource().getName()).get(e.getDest().getName()));
                    if (e.getCurrentValue() > max) {
                        max = e.getCurrentValue();
                    }
                } else {
                    e.setCurrentValue(0f);
                }
            }
            if (max > 0) {
                MyVars.g.MX_E_VAL = max;
            }
        } else if (MyVars.getViewer().vc.selectedNodeNeighborNodeTypeSelector.isShowing() && MyVars.getViewer().vc.selectedNodeNeighborNodeTypeSelector.getSelectedItem().toString().equals("S.")) {
            float max = 0.0f;
            for (MyEdge e : edges) {
                if (MyVars.getViewer().vc.depthNodeSuccessorMaps.containsKey(e.getSource().getName()) &&
                        MyVars.getViewer().vc.depthNodeSuccessorMaps.get(e.getSource().getName()).containsKey(e.getDest().getName())) {
                    e.setCurrentValue(MyVars.getViewer().vc.depthNodeSuccessorMaps.get(MyVars.getViewer().vc.depthNodeNameSet.iterator().next()).get(e.getDest().getName()));
                    if (e.getCurrentValue() > max) {
                        max = e.getCurrentValue();
                    }
                } else {
                    e.setCurrentValue(0f);
                }
            }
            if (max > 0) {
                MyVars.g.MX_E_VAL = max;
                MyVars.g.MX_N_VAL = max;
            }
            System.out.println(MyVars.g.MX_E_VAL);
        } else if (MyVars.getViewer().vc.depthNeighborNodeTypeSelector.getSelectedItem().toString().equals("P.")) {
            float max = 0.0f;
            for (MyEdge e : edges) {
                if (MyVars.getViewer().vc.depthNodePredecessorMaps.containsKey(e.getDest().getName()) &&
                    MyVars.getViewer().vc.depthNodePredecessorMaps.get(e.getDest().getName()).containsKey(e.getSource().getName())) {
                    e.setCurrentValue(MyVars.getViewer().vc.depthNodePredecessorMaps.get(e.getDest().getName()).get(e.getSource().getName()));
                    if (e.getCurrentValue() > max) {
                        max = e.getCurrentValue();
                    }
                } else {
                    e.setCurrentValue(0f);
                }
            }
            if (max > 0) {
                MyVars.g.MX_E_VAL = max;
            }
        }
    }

    private static void setUniqueContributionToEdges() {
        Collection<MyEdge> es = MyVars.g.getEdges();
        float maxVal = 0.00f;
        for (MyEdge e : es) {
            if (e.getCurrentValue() == 0 || e.getDest().getCurrentValue() == 0 || e.getSource().getCurrentValue() == 0) {
                continue;
            }
            e.tmpValue = (float) e.getUniqueContribution();
            if (maxVal < e.tmpValue) {
                maxVal = e.tmpValue;
            }
            e.setCurrentValue(e.tmpValue);
        }
        if (maxVal > 0) {
            MyVars.g.MX_E_VAL = Float.valueOf(maxVal);
        }
    }

    private static void setEdgeBetweenessToEdges() {
        BetweennessCentrality<MyNode, MyEdge> betweenness = new BetweennessCentrality<>(MyVars.g);
        betweenness.setRemoveRankScoresOnFinalize(false); // 최종 betweenness centrality 값 유지.
        betweenness.evaluate();
        Collection<MyEdge> edges = MyVars.g.getEdges();
        for (MyEdge e : edges) {
            if (e.getCurrentValue() > 0 && e.getSource().getCurrentValue() > 0 && e.getDest().getCurrentValue() > 0) {
                e.setCurrentValue(Float.valueOf(MyMathUtil.twoDecimalFormat(betweenness.getEdgeRankScore(e))));
            }
        }
    }

    public static int getEdgesGreaterThanZero() {
        Collection<MyEdge> edges = MyVars.g.getEdges();
        int total = 0;
        for (MyEdge e : edges) {
            if (e.getCurrentValue() > 0 && e.getSource().getCurrentValue() > 0 && e.getDest().getCurrentValue() > 0) {
                total++;
            }
        }
        return total;
    }

    public static int getOnEdgeCount() {
        Collection<MyEdge> es = MyVars.g.getEdges();
        int cnt = 0;
        for (MyEdge e : es) {
            if (e.getSource().getCurrentValue() > 0 && e.getDest().getCurrentValue() > 0 && e.getCurrentValue() > 0) {
                cnt++;
            }
        }
        return cnt;
    }

    public static double getMinimumEdgeValue() {
        if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() == 0) {
            return 0.00D;
        } else {
            Collection<MyEdge> edges = MyVars.g.getEdges();
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
        if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() == 0) {return 0.00D;
        } else {
            Collection<MyEdge> edges = MyVars.g.getEdges();
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

    public static double getMaximumUniqueContribution() {
        long maxVal = 0;
        Collection<MyEdge> edges = MyVars.g.getEdges();
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
        Collection<MyEdge> edges = MyVars.g.getEdges();
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
        if (MyVars.getViewer().vc.edgeValueSelecter.getSelectedIndex() < 2) {return 0.00D;}
        else {
            int numEdges = 0;
            Collection<MyEdge> edges = MyVars.g.getEdges();
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
        Collection<MyEdge> edges = MyVars.g.getEdges();
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
        Collection<MyEdge> edges = MyVars.g.getEdges();
        for (MyEdge e : edges) {
            if (e.getCurrentValue() == 0) continue;
            float nodeMaxTime = 0;
            String graphEdgeName = e.getSource().getName()+"-"+e.getDest().getName();
            for (int s = 0; s < MyVars.seqs.length; s++) {
                for (int i = 1; i < MyVars.seqs[s].length; i++) {
                    String [] pProperty = MyVars.seqs[s][i-1].split(":");
                    String [] sProperty = MyVars.seqs[s][i].split(":");
                    String seqEdgeName = pProperty[0]+"-"+sProperty[0];
                    if (graphEdgeName.equals(seqEdgeName)) {
                        float currentSeqTime = Float.valueOf(sProperty[1]);
                        if (currentSeqTime > nodeMaxTime) {nodeMaxTime = currentSeqTime;}
                    }
                }
            }
            if (nodeMaxTime > maxVal) {
                maxVal = nodeMaxTime;
            }
            e.setCurrentValue(nodeMaxTime);
        }
        if (maxVal > 0) {
            MyVars.g.MX_E_VAL = Float.valueOf(maxVal);
        }
    }

    public static double getAverageUniqueContribution() {
        Collection<MyEdge> es = MyVars.g.getEdges();
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
        Collection<MyEdge> edges = MyVars.g.getEdges();
        for (MyEdge e : edges) {
            if (e.getCurrentValue() == 0) continue;
            float totalTime = 0;
            int seqEdgeCnt = 0;
            String graphEdgeName = e.getSource().getName() + "-" + e.getDest().getName();
            for (int s = 0; s < MyVars.seqs.length; s++) {
                for (int i = 1; i < MyVars.seqs[s].length; i++) {
                    String [] pProperty = MyVars.seqs[s][i-1].split(":");
                    String [] sProperty = MyVars.seqs[s][i].split(":");
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
            MyVars.g.MX_E_VAL = Float.valueOf(maxVal);
        }
    }

    public static void resetEdgeValue() {
        MyVars.getViewer().vc.edgeValueSelecter.removeActionListener(MyVars.getViewer().vc);
        MyVars.getViewer().vc.edgeValueSelecter.setSelectedIndex(1);
        MyVars.getViewer().vc.edgeValueSelecter.addActionListener(MyVars.getViewer().vc);
    }

}
