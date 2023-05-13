package medousa.sequential.graph;

import edu.uci.ics.jung.algorithms.cluster.WeakComponentClusterer;
import medousa.sequential.graph.common.MyNodeBetweennessCentrality;
import medousa.sequential.graph.common.MyClosenessCentrality;
import medousa.sequential.graph.common.MyNodeEigenvectorCentrality;
import medousa.sequential.utils.MyMathUtil;
import medousa.sequential.utils.MySequentialGraphSysUtil;
import medousa.sequential.utils.MySequentialGraphVars;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class MyGraphBuilder {

    private void setEdgeValueNames() {
        for (int i = 0; i < MySequentialGraphVars.app.getSequentialGraphMsgBroker().getConfigPanel().getEdgeValueTable().getRowCount(); i++) {
            if (!MySequentialGraphVars.app.getSequentialGraphMsgBroker().getConfigPanel().getEdgeValueTable().getValueAt(i, 0).toString().contains("SELECT") &&
                !MySequentialGraphVars.app.getSequentialGraphMsgBroker().getConfigPanel().getEdgeValueTable().getValueAt(i, 1).toString().contains("NO") &&
                !MySequentialGraphVars.app.getSequentialGraphMsgBroker().getConfigPanel().getEdgeValueTable().getValueAt(i, 2).toString().contains("SELECT")) {
                String value = MySequentialGraphVars.app.getSequentialGraphMsgBroker().getConfigPanel().getEdgeValueTable().getValueAt(i, 0).toString().replaceAll(" ", "");
                String valueType = MySequentialGraphVars.app.getSequentialGraphMsgBroker().getConfigPanel().getEdgeValueTable().getValueAt(i, 2).toString().replaceAll(" ", "");
                MySequentialGraphVars.userDefinedEdgeValueMap.put(MySequentialGraphVars.app.getSequentialGraphMsgBroker().getConfigPanel().getEdgeValueTable().getValueAt(i, 0).toString().substring(2), valueType);
                MySequentialGraphVars.app.getSequentialGraphMsgBroker().generateEdgeValueFeatures(value, valueType);
            }
        }
    }

    private void setEdgeLabelNames() {
        for (int i = 0; i < MySequentialGraphVars.app.getSequentialGraphMsgBroker().getConfigPanel().getEdgeLabelTable().getRowCount(); i++) {
            if (!MySequentialGraphVars.app.getSequentialGraphMsgBroker().getConfigPanel().getEdgeLabelTable().getValueAt(i, 0).toString().contains("SELECT") &&
                !MySequentialGraphVars.app.getSequentialGraphMsgBroker().getConfigPanel().getEdgeLabelTable().getValueAt(i, 1).toString().contains("NO") &&
                !MySequentialGraphVars.app.getSequentialGraphMsgBroker().getConfigPanel().getEdgeLabelTable().getValueAt(i, 2).toString().contains("SELECT")) {
                String value = MySequentialGraphVars.app.getSequentialGraphMsgBroker().getConfigPanel().getEdgeLabelTable().getValueAt(i, 0).toString().substring(2);
                MySequentialGraphVars.userDefinedEdgeLabelSet.add(MySequentialGraphVars.app.getSequentialGraphMsgBroker().getConfigPanel().getEdgeLabelTable().getValueAt(i, 0).toString().substring(2));
                MySequentialGraphVars.app.getSequentialGraphMsgBroker().generateEdgeLabelFeatures(value, "category");
            }
        }
    }

    private void setNodeValueNames() {
        for (int i = 0; i < MySequentialGraphVars.app.getSequentialGraphMsgBroker().getConfigPanel().getNodeValueTable().getRowCount(); i++) {
            if (!MySequentialGraphVars.app.getSequentialGraphMsgBroker().getConfigPanel().getNodeValueTable().getValueAt(i, 0).toString().contains("SELECT") &&
                !MySequentialGraphVars.app.getSequentialGraphMsgBroker().getConfigPanel().getNodeValueTable().getValueAt(i, 1).toString().contains("NO") &&
                !MySequentialGraphVars.app.getSequentialGraphMsgBroker().getConfigPanel().getNodeValueTable().getValueAt(i, 2).toString().contains("SELECT")) {
                String value = MySequentialGraphVars.app.getSequentialGraphMsgBroker().getConfigPanel().getNodeValueTable().getValueAt(i, 0).toString().replaceAll(" ", "");
                String valueType = MySequentialGraphVars.app.getSequentialGraphMsgBroker().getConfigPanel().getNodeValueTable().getValueAt(i, 2).toString().replaceAll(" ", "");
                MySequentialGraphVars.userDefinedNodeValueMap.put(MySequentialGraphVars.app.getSequentialGraphMsgBroker().getConfigPanel().getNodeValueTable().getValueAt(i, 0).toString().substring(2), valueType);
                MySequentialGraphVars.app.getSequentialGraphMsgBroker().generateNodeValueFeatures(value, valueType);
            }
        }
    }

    private void setNodeLabelNames() {
        for (int i = 0; i < MySequentialGraphVars.app.getSequentialGraphMsgBroker().getConfigPanel().getNodeLabelTable().getRowCount(); i++) {
            if (!MySequentialGraphVars.app.getSequentialGraphMsgBroker().getConfigPanel().getNodeLabelTable().getValueAt(i, 0).toString().contains("SELECT") &&
                !MySequentialGraphVars.app.getSequentialGraphMsgBroker().getConfigPanel().getNodeLabelTable().getValueAt(i, 1).toString().contains("NO") &&
                !MySequentialGraphVars.app.getSequentialGraphMsgBroker().getConfigPanel().getNodeLabelTable().getValueAt(i, 2).toString().contains("SELECT")) {
                String value = MySequentialGraphVars.app.getSequentialGraphMsgBroker().getConfigPanel().getNodeLabelTable().getValueAt(i, 0).toString().replaceAll(" ", "");
                String valueType = MySequentialGraphVars.app.getSequentialGraphMsgBroker().getConfigPanel().getNodeLabelTable().getValueAt(i, 2).toString().replaceAll(" ", "");
                MySequentialGraphVars.userDefinedNodeLabelSet.add(MySequentialGraphVars.app.getSequentialGraphMsgBroker().getConfigPanel().getNodeLabelTable().getValueAt(i, 0).toString().substring(2));
                MySequentialGraphVars.app.getSequentialGraphMsgBroker().generateNodeLabelFeatures(value, valueType);
            }
        }
    }

    public MyGraphBuilder() {
        this.setEdgeValueNames();
        this.setEdgeLabelNames();
        this.setNodeValueNames();
        this.setNodeLabelNames();
        MySequentialGraphVars.g = new MyGraph();
    }

    public void createNodes() {
        File[] ptrnFiles = MySequentialGraphSysUtil.getFileList(MySequentialGraphVars.outputDir);
        for (File file : ptrnFiles) {
            String [] nodeFileProperties = file.getName().split(MySequentialGraphVars.contributionSymbol);
            String nodeName = nodeFileProperties[0];
            nodeFileProperties = nodeFileProperties[1].split(MySequentialGraphVars.uniqueContributionSeparator);
            long cont = Long.valueOf(nodeFileProperties[0]);
            long uniqCont = Long.valueOf(nodeFileProperties[0]);
            MyNode n = new MyNode(nodeName, cont, uniqCont);
            MySequentialGraphVars.g.addVertex(n);
            MySequentialGraphVars.g.vRefs.put(nodeName, n);
            if (uniqCont > MySequentialGraphVars.g.maxUniqueContribution) {
                MySequentialGraphVars.g.maxUniqueContribution = uniqCont;}
            if (uniqCont < MySequentialGraphVars.g.minUniqueContribution) {
                MySequentialGraphVars.g.minUniqueContribution = uniqCont;}
            if (cont > MySequentialGraphVars.g.maxNodeContribution) {
                MySequentialGraphVars.g.maxNodeContribution = cont;}
            if (cont < MySequentialGraphVars.g.minNodeContribution) {
                MySequentialGraphVars.g.minNodeContribution = cont;}
            MySequentialGraphVars.g.totalUniqueContribution += uniqCont;
            MySequentialGraphVars.g.totalContribution += cont;
        }
    }

    public void createEdges() {
        try {
            if (MySequentialGraphVars.isSupplementaryOn) {
                BufferedReader br = new BufferedReader(new FileReader(MySequentialGraphSysUtil.getWorkingDir()+ MySequentialGraphSysUtil.getDirectorySlash()+"sequences.txt"));
                String line = "";
                Map<MyEdge, Integer> plusEdgeUniqueContributionMap = new HashMap<>();
                while ((line = br.readLine()) != null) {
                    String[] itemsets = line.split(MySequentialGraphVars.hyphenDelimeter);
                    String p = itemsets[0].split(":")[0];
                    Map<MyEdge, Integer> uniqueContributionMap = new HashMap<>();
                    ((MyNode) MySequentialGraphVars.g.vRefs.get(p)).updateContribution(itemsets.length-1);
                    for (int i = 1; i < itemsets.length; i++) {
                        String [] successors = itemsets[i].split(":");
                        String edgeRef = p+"-"+successors[0];
                        MyEdge plusEdge = (MyEdge) MySequentialGraphVars.g.edgeRefMap.get(edgeRef);
                        if (plusEdge == null) {
                            MyNode pN = (MyNode) MySequentialGraphVars.g.vRefs.get(p);
                            MyNode sN = (MyNode) MySequentialGraphVars.g.vRefs.get(successors[0]);
                            plusEdge = new MyEdge(pN, sN);
                            MySequentialGraphVars.g.addEdge(plusEdge, pN, sN);
                            MySequentialGraphVars.g.edgeRefMap.put(edgeRef, plusEdge);
                        }
                    }
                }

                // Create edges between items.
                br = new BufferedReader(new FileReader(MySequentialGraphSysUtil.getWorkingDir()+ MySequentialGraphSysUtil.getDirectorySlash()+"sequences.txt"));
                while ((line = br.readLine()) != null) {
                    String [] itemsets = line.split(MySequentialGraphVars.hyphenDelimeter);
                    Map<MyEdge, Integer> uniqueContributionMap = new HashMap<>();
                    for (int i=1; i < itemsets.length; i++) {
                        String p = itemsets[i-1].split(":")[0];
                        String s = itemsets[i].split(":")[0];
                        String edgeRef = p + "-" + s;
                        MyEdge plusEdge = (MyEdge) MySequentialGraphVars.g.edgeRefMap.get(edgeRef);
                        if (plusEdge == null) {
                            MyNode pN = (MyNode) MySequentialGraphVars.g.vRefs.get(p);
                            MyNode sN = (MyNode) MySequentialGraphVars.g.vRefs.get(s);
                            plusEdge = new MyEdge(pN, sN);
                            MySequentialGraphVars.g.addEdge(plusEdge, pN, sN);
                            MySequentialGraphVars.g.edgeRefMap.put(edgeRef, plusEdge);
                        }
                    }

                }
            } else { // Not supplementary variables
                BufferedReader br = new BufferedReader(new FileReader(MySequentialGraphSysUtil.getWorkingDir()+ MySequentialGraphSysUtil.getDirectorySlash()+"sequences.txt"));
                String line = "";
                while ((line = br.readLine()) != null) {
                    Map<MyEdge, Integer> uniqueContributionMap = new HashMap<>();
                    String [] itemsets = line.split(MySequentialGraphVars.hyphenDelimeter);
                    for (int i=1; i < itemsets.length; i++) {
                        String p = itemsets[i-1].split(":")[0];
                        String s = itemsets[i].split(":")[0];
                        String edgeRef = p + "-" + s;
                        MyEdge plusEdge = (MyEdge) MySequentialGraphVars.g.edgeRefMap.get(edgeRef);
                        if (plusEdge == null) {
                            MyNode pN = (MyNode) MySequentialGraphVars.g.vRefs.get(p);
                            MyNode sN = (MyNode) MySequentialGraphVars.g.vRefs.get(s);
                            plusEdge = new MyEdge(pN, sN);
                            MySequentialGraphVars.g.addEdge(plusEdge, pN, sN);
                            MySequentialGraphVars.g.edgeRefMap.put(edgeRef, plusEdge);
                        };
                    }

                }
            }
        } catch (Exception ex) {ex.printStackTrace();}
    }

    public void setEdgeValues() {
        try {
            JTable edgeValueTable = MySequentialGraphVars.app.getSequentialGraphMsgBroker().getConfigPanel().getEdgeValueTable();
            for (int i = 0; i < edgeValueTable.getRowCount(); i++) {
                String value = edgeValueTable.getValueAt(i, 0).toString();
                String isValue = edgeValueTable.getValueAt(i, 1).toString();
                String valueType = edgeValueTable.getValueAt(i, 2).toString();
                if (isValue.replaceAll(" ", "").contains("YES") && (!valueType.contains("TIME") || !valueType.contains("DATE"))) {
                    String file = MySequentialGraphSysUtil.getWorkingDir()+ MySequentialGraphSysUtil.getDirectorySlash()+value + MySequentialGraphVars.edgeValueFileExt;
                    file = file.replaceAll(" ", "").toLowerCase();
                    BufferedReader bw = new BufferedReader(new FileReader(file));
                    if (valueType.contains("SUM")) {
                        String line = "";
                        while ((line = bw.readLine()) != null) {
                            String[] itemsets = line.split(MySequentialGraphVars.hyphenDelimeter);
                            if (itemsets.length < 3) continue;
                            for (int k = 2; k < itemsets.length; k++) {
                                String pred = itemsets[k-2];
                                String[] edgeWeightItems = itemsets[k-1].split(MySequentialGraphVars.commaDelimeter);
                                String succ = itemsets[k];
                                String edgeRef = pred + "-" + succ;
                                float edgeWeightTotalValue = 0.00f;
                                for (String edgeWeightItem : edgeWeightItems) {edgeWeightTotalValue += Double.valueOf(edgeWeightItem);}
                                MyEdge edge = (MyEdge) MySequentialGraphVars.g.edgeRefMap.get(edgeRef);
                                edge.setEdgeSumValue(value, edgeWeightTotalValue);
                                k++;
                            }
                        }
                    } else if (valueType.contains("AVERAGE")) {
                        String line = "";
                        while ((line = bw.readLine()) != null) {
                            String[] itemsets = line.split(MySequentialGraphVars.hyphenDelimeter);
                            if (itemsets.length == 2) continue;
                            for (int k = 2; k < itemsets.length; k++) {
                                String pred = itemsets[k-2];
                                String[] edgeWeightItems = itemsets[k-1].split(MySequentialGraphVars.commaDelimeter);
                                String succ = itemsets[k];
                                String edgeRef = pred + "-" + succ;
                                float totalEdgeValue = 0.00f;
                                for (String edgeWeightItem : edgeWeightItems) {totalEdgeValue += Double.valueOf(edgeWeightItem);}
                                totalEdgeValue = totalEdgeValue/edgeWeightItems.length;
                                MyEdge edge = (MyEdge) MySequentialGraphVars.g.edgeRefMap.get(edgeRef);
                                edge.setEdgeAverageValue(value, totalEdgeValue);
                                k++;
                            }
                        }
                    } else if (valueType.contains("DISTINCT")) {
                        String line = "";
                        while ((line = bw.readLine()) != null) {
                            String[] itemsets = line.split(MySequentialGraphVars.hyphenDelimeter);
                            if (itemsets.length == 2) continue;
                            for (int k = 2; k < itemsets.length; k++) {
                                String pred = itemsets[k-2];
                                String [] edgeValues = itemsets[k-1].split(MySequentialGraphVars.commaDelimeter);
                                String succ = itemsets[k];
                                String edgeRef = pred + "-" + succ;
                                MyEdge edge = (MyEdge) MySequentialGraphVars.g.edgeRefMap.get(edgeRef);
                                edge.setEdgeDistinctValue(value, Float.valueOf(edgeValues[0]));
                                k++;
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {ex.printStackTrace();}
    }

    public void setEdgeLabels() {
        try {
            JTable edgeLabelTable = MySequentialGraphVars.app.getSequentialGraphMsgBroker().getConfigPanel().getEdgeLabelTable();
            for (int i = 0; i < edgeLabelTable.getRowCount(); i++) {
                String value = edgeLabelTable.getValueAt(i, 0).toString();
                String isValue = edgeLabelTable.getValueAt(i, 1).toString();
                String valueType = edgeLabelTable.getValueAt(i, 2).toString();
                if (isValue.replaceAll(" ", "").contains("YES") && (!valueType.contains("TIME") || !valueType.contains("DATE"))) {
                    String file = MySequentialGraphSysUtil.getWorkingDir()+ MySequentialGraphSysUtil.getDirectorySlash()+value + MySequentialGraphVars.edgeLabelFileExt;
                    file = file.replaceAll(" ", "").toLowerCase();
                    BufferedReader bw = new BufferedReader(new FileReader(file));
                    String line = "";
                    while ((line = bw.readLine()) != null) {
                        String[] itemsets = line.split(MySequentialGraphVars.hyphenDelimeter);
                        if (itemsets.length < 3) continue;
                        for (int k = 2; k < itemsets.length; k++) {
                            String pred = itemsets[k-2];
                            String [] edgeWeightItems = itemsets[k-1].split(MySequentialGraphVars.commaDelimeter);
                            String succ = itemsets[k];
                            String edgeRef = pred + "-" + succ;
                            MyEdge edge = (MyEdge) MySequentialGraphVars.g.edgeRefMap.get(edgeRef);
                            edge.setEdgeLabel(value, edgeWeightItems[0]);
                            k++;
                        }
                    }
                }
            }
        } catch (Exception ex) {ex.printStackTrace();}
    }

    public void setNodeValues() {
        try {
            JTable noteValueTable = MySequentialGraphVars.app.getSequentialGraphMsgBroker().getConfigPanel().getNodeValueTable();
            for (int i = 0; i < noteValueTable.getRowCount(); i++) {
                String valueVariable = noteValueTable.getValueAt(i, 0).toString();
                String isValue = noteValueTable.getValueAt(i, 1).toString();
                String valueType = noteValueTable.getValueAt(i, 2).toString();
                if (isValue.replaceAll(" ", "").contains("YES") && (!valueType.contains("TIME") || !valueType.contains("DATE"))) {
                    String file = MySequentialGraphSysUtil.getWorkingDir()+ MySequentialGraphSysUtil.getDirectorySlash()+valueVariable + MySequentialGraphVars.nodeValueFileExt;
                    file = file.replaceAll(" ", "").toLowerCase();
                    BufferedReader bw = new BufferedReader(new FileReader(file));
                    if (valueType.contains("SUM")) {
                        String line = "";
                        while ((line = bw.readLine()) != null) {
                            String [] itemsets = line.split(MySequentialGraphVars.hyphenDelimeter);
                            for (int k = 1; k < itemsets.length; k++) {
                                String [] nodeValues = itemsets[k].split(MySequentialGraphVars.commaDelimeter);
                                float nodeValue = 0.00f;
                                for (String aValue : nodeValues) {nodeValue += Double.valueOf(aValue);}
                                MyNode n = (MyNode) MySequentialGraphVars.g.vRefs.get(itemsets[k-1]);
                                n.setNodeSumValue(valueVariable.substring(2), nodeValue);
                                k++;
                            }
                        }
                    } else if (valueType.contains("AVERAGE")) {
                        String line = "";
                        while ((line = bw.readLine()) != null) {
                            String [] itemsets = line.split(MySequentialGraphVars.hyphenDelimeter);
                            for (int k = 1; k < itemsets.length; k++) {
                                String [] nodeValues = itemsets[k].split(MySequentialGraphVars.commaDelimeter);
                                float totalValue = 0.00f;
                                for (String aValue : nodeValues) {totalValue += Float.valueOf(aValue);}
                                float averageValue = totalValue/nodeValues.length;
                                MyNode n = (MyNode) MySequentialGraphVars.g.vRefs.get(itemsets[k-1]);
                                n.setNodeAverageValue(valueVariable.substring(2), averageValue);
                                k++;
                            }
                        }
                    } else if (valueType.contains("DISTINCT")) {
                        String line = "";
                        while ((line = bw.readLine()) != null) {
                            String [] itemsets = line.split(MySequentialGraphVars.hyphenDelimeter);
                            for (int k = 1; k < itemsets.length; k++) {
                                String [] nodeValues = itemsets[k].split(MySequentialGraphVars.commaDelimeter);
                                MyNode n = (MyNode) MySequentialGraphVars.g.vRefs.get(itemsets[k-1]);
                                n.setNodeDistinctValue(valueVariable.substring(2), Float.valueOf(nodeValues[0]));
                                k++;
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {ex.printStackTrace();}
    }

    public void setNodeLabels() {
        try {
            JTable nodeLabelTable = MySequentialGraphVars.app.getSequentialGraphMsgBroker().getConfigPanel().getNodeLabelTable();
            for (int i = 0; i < nodeLabelTable.getRowCount(); i++) {
                String valueVariable = nodeLabelTable.getValueAt(i, 0).toString();
                String isValue = nodeLabelTable.getValueAt(i, 1).toString();
                String valueType = nodeLabelTable.getValueAt(i, 2).toString();
                if (isValue.replaceAll(" ", "").contains("YES") && (!valueType.contains("TIME") || !valueType.contains("DATE"))) {
                    String file = MySequentialGraphSysUtil.getWorkingDir()+ MySequentialGraphSysUtil.getDirectorySlash()+valueVariable+ MySequentialGraphVars.nodeLabelFileExt;
                    file = file.replaceAll(" ", "").toLowerCase();
                    BufferedReader bw = new BufferedReader(new FileReader(file));
                    String line = "";
                    while ((line = bw.readLine()) != null) {
                        String[] itemsets = line.split(MySequentialGraphVars.hyphenDelimeter);
                        for (int k=1; k < itemsets.length; k++) {
                            String [] nodeValues = itemsets[k].split(MySequentialGraphVars.commaDelimeter);
                            MyNode n = (MyNode) MySequentialGraphVars.g.vRefs.get(itemsets[k-1]);
                            n.setNodeLabel(valueVariable.substring(2), nodeValues[0]);
                            k++;
                        }
                    }
                }
            }
        } catch (Exception ex) {ex.printStackTrace();}
    }

    public void setEdgeTimeValues() {
        try {
            JTable edgeValueTable = MySequentialGraphVars.app.getSequentialGraphMsgBroker().getConfigPanel().getEdgeValueTable();
            for (int i = 0; i < edgeValueTable.getRowCount(); i++) {
                String value = edgeValueTable.getValueAt(i, 0).toString();
                String isValue = edgeValueTable.getValueAt(i, 1).toString();
                String valueType = edgeValueTable.getValueAt(i, 2).toString();
                if (valueType.contains("MAXIMUM")) {
                    String file = MySequentialGraphSysUtil.getWorkingDir() + MySequentialGraphSysUtil.getDirectorySlash() + value.replaceAll(" ", "").toLowerCase() + ".edgeval";
                    BufferedReader bw = new BufferedReader(new FileReader(file));
                    String line = "";
                    while ((line = bw.readLine()) != null) {
                        String [] itemsets = line.split(":");
                        if (itemsets.length < 4) continue;
                        for (int k = 3; k < itemsets.length; k++) {
                            String pred = itemsets[k-3];
                            String succ = itemsets[k-1];
                            String predTime = itemsets[k-2].split(MySequentialGraphVars.commaDelimeter)[0];
                            String succTime = itemsets[k].split(MySequentialGraphVars.commaDelimeter)[0];
                            String edgeRef = pred + "-" + succ;
                            MyEdge edge = (MyEdge) MySequentialGraphVars.g.edgeRefMap.get(edgeRef);
                            edge.setEdgeSumValue(value.substring(2), Math.abs(Float.valueOf(MySequentialGraphSysUtil.getTimeDifference(predTime, succTime))));
                            k++;
                        }
                    }
                } else if (valueType.contains("DIFFERENCE")) {
                    String file = MySequentialGraphSysUtil.getWorkingDir() + MySequentialGraphSysUtil.getDirectorySlash() + value.replaceAll(" ", "").toLowerCase() + ".edgeval";
                    BufferedReader bw = new BufferedReader(new FileReader(file));
                    String line = "";
                    while ((line = bw.readLine()) != null) {
                        String [] itemsets = line.split(":");
                        if (itemsets.length < 4) continue;
                        for (int k = 3; k < itemsets.length; k++) {
                            String pred = itemsets[k-3];
                            String succ = itemsets[k-1];
                            String predTime = itemsets[k-2].split(MySequentialGraphVars.commaDelimeter)[0];
                            String succTime = itemsets[k].split(MySequentialGraphVars.commaDelimeter)[0];
                            String edgeRef = pred + "-" + succ;
                            MyEdge edge = (MyEdge) MySequentialGraphVars.g.edgeRefMap.get(edgeRef);
                            edge.setEdgeSumValue(value.substring(2), Math.abs(Float.valueOf(MySequentialGraphSysUtil.getTimeDifference(predTime, succTime))));
                            k++;
                        }
                    }
                }
            }
        } catch (Exception ex) {ex.printStackTrace();}
    }

    public void setEndNodeCount() {
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                String itemset = MySequentialGraphVars.seqs[s][MySequentialGraphVars.seqs[s].length-1];
                if (itemset.equals(n.getName())) {n.setEndNodeCount(1);}
            }
        }
    }

    public void setStartPositionNodeCount() {
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                String itemset = MySequentialGraphVars.seqs[s][0];
                if (itemset.equals(n.getName())) {n.setOpenNodeCount(1);}
            }
        }
    }

    public void setNodePropagationCount() {
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                for (int i = 0; i < MySequentialGraphVars.seqs[s].length; i++) {
                    String itemset = MySequentialGraphVars.seqs[s][i];
                    if (itemset.equals(n.getName())) {
                        int propagation = MySequentialGraphVars.seqs[s].length-(i+1);
                        n.setPropagation(propagation);
                        break;
                    }
                }
            }
        }
    }

    public void setNumberOfGraphs() {
        // Get number of graphs.
        WeakComponentClusterer<MyNode, MyEdge> wcSearch = new WeakComponentClusterer<>();
        Set<Set<MyNode>> clusters = wcSearch.transform(MySequentialGraphVars.g);
        MySequentialGraphVars.numberOfGraphs = clusters.size();
    }

    public void setNodeRecurrenceCount() {
        for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
            for (int i = 1; i < MySequentialGraphVars.seqs[s].length; i++) {
                String p = MySequentialGraphVars.seqs[s][i-1].split(":")[0];
                String ss = MySequentialGraphVars.seqs[s][i].split(":")[0];
                if (ss.equals(p)) {
                    ((MyNode) MySequentialGraphVars.g.vRefs.get(p)).setRecurrenceCount(1);
                }
            }
        }
    }

    public void setAverageNodeRecurrenceLength() {
        if (MySequentialGraphVars.isSupplementaryOn) {
            Map<String, ArrayList<Integer>> nodeRecurrenceLengthMap = new HashMap<>();
            for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                Map<String, ArrayList<Integer>> sequenceNodeRecurrenceLengthMap = new HashMap<>();
                for (int i = 1; i < MySequentialGraphVars.seqs[s].length; i++) {
                    String n = MySequentialGraphVars.seqs[s][i].split(":")[0];
                    if (sequenceNodeRecurrenceLengthMap.containsKey(n)) {
                        ArrayList<Integer> recurrenceLengths = sequenceNodeRecurrenceLengthMap.get(n);
                        recurrenceLengths.add((i+1)-recurrenceLengths.get(recurrenceLengths.size()-1));
                        sequenceNodeRecurrenceLengthMap.put(n, sequenceNodeRecurrenceLengthMap.get(n));
                    } else {
                        ArrayList recurrenceLengths = new ArrayList();
                        recurrenceLengths.add((i+1));
                        sequenceNodeRecurrenceLengthMap.put(n, recurrenceLengths);
                    }
                }

                for (String n : sequenceNodeRecurrenceLengthMap.keySet()) {
                    ArrayList<Integer> recurrenceLengths = sequenceNodeRecurrenceLengthMap.get(n);
                    if (recurrenceLengths.size() > 0) {
                        if (nodeRecurrenceLengthMap.containsKey(n)) {
                            for (int i=recurrenceLengths.size()-1; i > 0; i--) {
                                nodeRecurrenceLengthMap.get(n).add(recurrenceLengths.get(i));
                            }
                        } else {
                            recurrenceLengths.remove(recurrenceLengths.size()-1);
                            nodeRecurrenceLengthMap.put(n, recurrenceLengths);
                        }
                    }
                }
            }

            for (String n : nodeRecurrenceLengthMap.keySet()) {
                ArrayList<Integer> lengths = nodeRecurrenceLengthMap.get(n);
                int totalLength = 0;
                for (Integer legnth : lengths) {
                    totalLength += legnth;
                }
                float avgLength = (totalLength == 0 ? 0 : (float)totalLength/lengths.size());
                ((MyNode) MySequentialGraphVars.g.vRefs.get(n)).setAverageRecurrenceLength(avgLength);
            }
        } else {
            Map<String, ArrayList<Integer>> nodeRecurrenceLengthMap = new HashMap<>();
            for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                Map<String, ArrayList<Integer>> sequenceNodeRecurrenceLengthMap = new HashMap<>();
                for (int i = 0; i < MySequentialGraphVars.seqs[s].length; i++) {
                    String n = MySequentialGraphVars.seqs[s][i].split(":")[0];
                    if (sequenceNodeRecurrenceLengthMap.containsKey(n)) {
                        ArrayList<Integer> recurrenceLengths = sequenceNodeRecurrenceLengthMap.get(n);
                        recurrenceLengths.add((i+1)-recurrenceLengths.get(recurrenceLengths.size()-1));
                        sequenceNodeRecurrenceLengthMap.put(n, sequenceNodeRecurrenceLengthMap.get(n));
                    } else {
                        ArrayList recurrenceLengths = new ArrayList();
                        recurrenceLengths.add((i+1));
                        sequenceNodeRecurrenceLengthMap.put(n, recurrenceLengths);
                    }
                }

                for (String n : sequenceNodeRecurrenceLengthMap.keySet()) {
                    ArrayList<Integer> recurrenceLengths = sequenceNodeRecurrenceLengthMap.get(n);
                    if (recurrenceLengths.size() > 0) {
                        if (nodeRecurrenceLengthMap.containsKey(n)) {
                            for (int i=recurrenceLengths.size()-1; i > 0; i--) {
                                nodeRecurrenceLengthMap.get(n).add(recurrenceLengths.get(i));
                            }
                        } else {
                            recurrenceLengths.remove(recurrenceLengths.size()-1);
                            nodeRecurrenceLengthMap.put(n, recurrenceLengths);
                        }
                    }
                }
            }

            for (String n : nodeRecurrenceLengthMap.keySet()) {
                ArrayList<Integer> lengths = nodeRecurrenceLengthMap.get(n);
                int totalLength = 0;
                for (Integer legnth : lengths) {
                    totalLength += legnth;
                }
                float avgLength = (totalLength == 0 ? 0 : (float) totalLength / lengths.size());
                ((MyNode) MySequentialGraphVars.g.vRefs.get(n)).setAverageRecurrenceLength(avgLength);
            }
        }
    }

    public void setNodeDepthInformation() {
        try {
            Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
            for (MyNode n : nodes) {
                Map<Integer, Set<String>> predecessorByDepthMap = new HashMap<>();
                Map<Integer, Set<String>> successorByDepthMap = new HashMap<>();
                Map<Integer, MyNodeDepthInfo> nodeDepthInfoMap = new HashMap<>();
                for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                    for (int i = 0; i < MySequentialGraphVars.seqs[s].length; i++) {
                        String [] itemset = MySequentialGraphVars.seqs[s][i].split(":");
                        if (itemset[0].equals(n.getName())) {
                            if (MySequentialGraphVars.seqs[s].length == 1) {
                                Set<String> predecessors = new HashSet<>();
                                predecessorByDepthMap.put(i+1, predecessors);
                                Set<String> successors= new HashSet<>();
                                predecessorByDepthMap.put(i+1, successors);
                                if (nodeDepthInfoMap.containsKey(i+1)) {
                                    MyNodeDepthInfo nodeDepthInfo = nodeDepthInfoMap.get(i+1);
                                    nodeDepthInfo.setContribution(1);
                                    nodeDepthInfoMap.put(i+1, nodeDepthInfo);
                                } else {
                                    MyNodeDepthInfo nodeDepthInfo = new MyNodeDepthInfo();
                                    nodeDepthInfo.setContribution(1);
                                    nodeDepthInfoMap.put(i+1, nodeDepthInfo);
                                }
                            } else if (i == 0) {
                                if (nodeDepthInfoMap.containsKey(i+1)) {
                                    MyNodeDepthInfo nodeDepthInfo = nodeDepthInfoMap.get(i+1);
                                    nodeDepthInfo.setContribution(1);
                                    nodeDepthInfo.setOutContribution(1);
                                    nodeDepthInfoMap.put(i+1, nodeDepthInfo);
                                    Set<String> successors = successorByDepthMap.get(i+1);
                                    successors.add(MySequentialGraphVars.seqs[s][i+1].split(":")[0]);
                                    successorByDepthMap.put(i+1, successors);
                                } else {
                                    MyNodeDepthInfo nodeDepthInfo = new MyNodeDepthInfo();
                                    nodeDepthInfo.setContribution(1);
                                    nodeDepthInfo.setOutContribution(1);
                                    nodeDepthInfoMap.put(i+1, nodeDepthInfo);
                                    Set<String> successors = new HashSet<>();
                                    successors.add(MySequentialGraphVars.seqs[s][i+1].split(":")[0]);
                                    successorByDepthMap.put(i+1, successors);
                                    Set<String> predecessors = new HashSet<>();
                                    predecessorByDepthMap.put(i+1, predecessors);
                                }
                            } else if ((i+1) == MySequentialGraphVars.seqs[s].length) {
                                if (nodeDepthInfoMap.containsKey(i+1)) {
                                    MyNodeDepthInfo nodeDepthInfo = nodeDepthInfoMap.get(i+1);
                                    nodeDepthInfo.setContribution(1);
                                    nodeDepthInfoMap.put(i+1, nodeDepthInfo);
                                    Set<String> predecessors = predecessorByDepthMap.get(i+1);
                                    predecessors.add(MySequentialGraphVars.seqs[s][i-1].split(":")[0]);
                                    predecessorByDepthMap.put(i+1, predecessors);
                                } else {
                                    MyNodeDepthInfo nodeDepthInfo = new MyNodeDepthInfo();
                                    nodeDepthInfo.setContribution(1);
                                    nodeDepthInfo.setInContribution(1);
                                    nodeDepthInfoMap.put(i+1, nodeDepthInfo);

                                    Set<String> predecessors = new HashSet<>();
                                    predecessors.add(MySequentialGraphVars.seqs[s][i-1].split(":")[0]);
                                    predecessorByDepthMap.put(i+1, predecessors);
                                    Set<String> successors = new HashSet<>();
                                    successorByDepthMap.put(i+1, successors);
                                }
                            } else {
                                if (nodeDepthInfoMap.containsKey(i+1)) {
                                    MyNodeDepthInfo nodeDepthInfo = nodeDepthInfoMap.get(i+1);
                                    nodeDepthInfo.setContribution(1);
                                    nodeDepthInfo.setOutContribution(1);
                                    nodeDepthInfo.setInContribution(1);

                                    nodeDepthInfoMap.put(i+1, nodeDepthInfo);
                                    Set<String> predecessors = predecessorByDepthMap.get(i+1);
                                    predecessors.add(MySequentialGraphVars.seqs[s][i-1].split(":")[0]);
                                    predecessorByDepthMap.put(i+1, predecessors);

                                    Set<String> successors = successorByDepthMap.get(i+1);
                                    successors.add(MySequentialGraphVars.seqs[s][i+1].split(":")[0]);
                                    successorByDepthMap.put(i+1, successors);
                                } else {
                                    MyNodeDepthInfo nodeDepthInfo = new MyNodeDepthInfo();
                                    nodeDepthInfo.setContribution(1);
                                    nodeDepthInfo.setOutContribution(1);
                                    nodeDepthInfo.setInContribution(1);

                                    nodeDepthInfoMap.put(i+1, nodeDepthInfo);
                                    Set<String> predecessors = new HashSet<>();
                                    predecessors.add(MySequentialGraphVars.seqs[s][i-1].split(":")[0]);
                                    predecessorByDepthMap.put(i+1, predecessors);

                                    Set<String> successors = new HashSet<>();
                                    successors.add(MySequentialGraphVars.seqs[s][i+1].split(":")[0]);
                                    successorByDepthMap.put(i+1, successors);
                                }
                            }
                        }
                    }
                }
                for (Integer i : predecessorByDepthMap.keySet()) {nodeDepthInfoMap.get(i).setPredecessors(predecessorByDepthMap.get(i).size());}
                for (Integer i : successorByDepthMap.keySet()) {nodeDepthInfoMap.get(i).setSuccessors(successorByDepthMap.get(i).size());}
                n.setNodeDepthInfoMap(nodeDepthInfoMap);
            }
        } catch (Exception ex) {ex.printStackTrace();}
    }

    public void setNodeInContributionCount() {
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                for (int i = 1; i < MySequentialGraphVars.seqs[s].length; i++) {
                    String itemset = MySequentialGraphVars.seqs[s][i];
                    if (itemset.equals(n.getName())) {n.setInContribution(1);}
                }
            }
        }
    }

    public void setNodeUniqueContribution() {
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                for (int i = 0; i < MySequentialGraphVars.seqs[s].length; i++) {
                    String itemset = MySequentialGraphVars.seqs[s][i].split(":")[0];
                    if (itemset.equals(n.getName())) {
                        n.setUniqueContribution();
                        break;
                    }
                }
            }
        }
    }

    public void setNodeOutContributionCount() {
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getName().contains("x")) {
                for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                    String itemset = MySequentialGraphVars.seqs[s][0].split(":")[0];
                    if (itemset.equals(n.getName())) {
                        int outCont = (MySequentialGraphVars.seqs[s].length-1);
                        n.setOutContribution(outCont);
                    }
                }
            } else {
                for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                    for (int i = 0; i < MySequentialGraphVars.seqs[s].length - 1; i++) {
                        String itemset = MySequentialGraphVars.seqs[s][i].split(":")[0];
                        if (itemset.equals(n.getName())) {
                            n.setOutContribution(1);
                        }
                    }
                }
            }
        }
    }

    public void setEdgeContribution() {
        Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
        for (MyEdge edge : edges) {
            for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                for (int i = 1; i < MySequentialGraphVars.seqs[s].length; i++) {
                    String ps = MySequentialGraphVars.seqs[s][i-1];
                    String ss = MySequentialGraphVars.seqs[s][i];
                    if (ps.equals(edge.getSource().getName()) && ss.equals(edge.getDest().getName())) {
                        edge.setContribution(1);
                    }
                }
            }
        }
    }

    public void setEngeUniqueContribution() {
        Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
        for (MyEdge edge : edges) {
            for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                for (int i = 1; i < MySequentialGraphVars.seqs[s].length; i++) {
                    String p = MySequentialGraphVars.seqs[s][i-1];
                    String ss = MySequentialGraphVars.seqs[s][i];
                    if (p.equals(edge.getSource().getName()) && ss.equals(edge.getDest().getName())) {
                        edge.setUniqueContribution(1);
                        break;
                    }
                }
            }
       }
    }

    public void setNodeRecursiveLength() {
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                int nodeRecursiveLength = -1;
                for (int i=0; i < MySequentialGraphVars.seqs[s].length; i++) {
                    String itemset = MySequentialGraphVars.seqs[s][i].split(":")[0];
                    if (itemset.equals(n.getName()) && nodeRecursiveLength == -1) {
                        nodeRecursiveLength = (i+1);
                    } else if (itemset.equals(n.getName()) && nodeRecursiveLength > 0) {
                        int recursiveLength = (i+1)-nodeRecursiveLength;
                        n.setRecursiveLength(recursiveLength);
                        nodeRecursiveLength = (i+1);
                    }
                }
            }
        }
    }


    public void setGraphLevelTotalEdgeContribution() {
        MySequentialGraphVars.g.setTotalEdgeContribution();
    }
    public void setEdgeSupport() {
        Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
        for (MyEdge edge : edges) {edge.setSupport();}
    }
    public void setEdgeConfidence() {
        Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
        for (MyEdge edge : edges) {edge.setConfidence();}
    }
    public void setEdgeLift() {
        Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
        for (MyEdge edge : edges) {edge.setLift();}
    }
    public void setNodeBetweenessCentrality() {
        MyNodeBetweennessCentrality betweennessCentrality = new MyNodeBetweennessCentrality(MySequentialGraphVars.g);
        betweennessCentrality.computeBetweenness(MySequentialGraphVars.g);
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {n.setBetweeness(Double.valueOf(MyMathUtil.twoDecimalFormat(betweennessCentrality.getVertexRankScore(n))));}
    }
    public void setNodeClosenessCentrality() {
        MyClosenessCentrality closenessCentrality = new MyClosenessCentrality(MySequentialGraphVars.g);
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            double value = closenessCentrality.getVertexScore(n);
            n.setCloseness(Double.valueOf(MyMathUtil.twoDecimalFormat((value == Double.NaN ? 0 : value))));
        }
    }
    public void setNodeEigenVectorCentrality() {
        MyNodeEigenvectorCentrality eigenvectorCentrality = new MyNodeEigenvectorCentrality(MySequentialGraphVars.g);
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {n.setEignevector(Double.valueOf(MyMathUtil.twoDecimalFormat((Double)eigenvectorCentrality.getVertexScore(n))));}
    }

    public void setVariableNodeStrength() {
        if (MySequentialGraphVars.isSupplementaryOn) {
            for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                String variableNode = MySequentialGraphSysUtil.getDecodedNodeName(MySequentialGraphVars.seqs[s][0]);
                ((MyNode) MySequentialGraphVars.g.vRefs.get(MySequentialGraphVars.seqs[s][0])).setVariableNodeStrength(variableNode);
            }
        }
    }

    public void setConnectance() {
        MySequentialGraphVars.g.connectance = (double) MySequentialGraphVars.g.getEdgeCount()/(MySequentialGraphVars.g.getVertexCount()*(MySequentialGraphVars.g.getVertexCount()-1));
    }
    public void setEbsilonOfGraph() {
        MySequentialGraphVars.g.graphEbasilon = (double) MySequentialGraphVars.g.getEdgeCount()/ MySequentialGraphVars.g.getVertexCount();
    }
    public void setMaxNodeValue() {
        MySequentialGraphVars.g.setMaxNodeValue();}
    public void setTotalEdgeContribution() {
        MySequentialGraphVars.g.setTotalEdgeContribution();}
    public void cleanWork(){ MySequentialGraphVars.g.edRefs = null; }
    public void setAverageUnWeightedGraphShortestPathLength(MyGraph<MyNode, MyEdge> g) {
        MySequentialGraphSysUtil.setAverageShortestDistance(g);}
    public void setConnectedNetworkComponentCountByGraph() {
        MyConnectedNetworkComponentFinder.setConnectedComponentsByGraph();
    }
}
