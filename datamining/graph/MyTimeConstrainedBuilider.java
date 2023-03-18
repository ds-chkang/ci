package datamining.graph;

import edu.uci.ics.jung.algorithms.importance.BetweennessCentrality;
import datamining.graph.common.MyClosenessCentrality;
import datamining.graph.common.MyNodeEigenvectorCentrality;
import datamining.utils.MyMathUtil;
import datamining.utils.system.MySysUtil;
import datamining.utils.system.MyVars;
import edu.uci.ics.jung.algorithms.scoring.PageRank;
import edu.uci.ics.jung.graph.Graph;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class MyTimeConstrainedBuilider {


    private void setEdgeValueNames() {
        for (int i = 0; i < MyVars.app.getMsgBroker().getConfigPanel().getEdgeValueTable().getRowCount(); i++) {
            if (!MyVars.app.getMsgBroker().getConfigPanel().getEdgeValueTable().getValueAt(i, 0).toString().contains("SELECT") &&
                    !MyVars.app.getMsgBroker().getConfigPanel().getEdgeValueTable().getValueAt(i, 1).toString().contains("NO") &&
                    !MyVars.app.getMsgBroker().getConfigPanel().getEdgeValueTable().getValueAt(i, 2).toString().contains("SELECT")) {
                String value = MyVars.app.getMsgBroker().getConfigPanel().getEdgeValueTable().getValueAt(i, 0).toString().replaceAll(" ", "");
                String valueType = MyVars.app.getMsgBroker().getConfigPanel().getEdgeValueTable().getValueAt(i, 2).toString().replaceAll(" ", "");
                MyVars.edgeValueMap.put(MyVars.app.getMsgBroker().getConfigPanel().getEdgeValueTable().getValueAt(i, 0).toString().substring(2), valueType);
                MyVars.app.getMsgBroker().generateEdgeValueFeatures(value, valueType);
            }
        }
    }

    private void setEdgeLabelNames() {
        for (int i = 0; i < MyVars.app.getMsgBroker().getConfigPanel().getEdgeLabelTable().getRowCount(); i++) {
            if (!MyVars.app.getMsgBroker().getConfigPanel().getEdgeLabelTable().getValueAt(i, 0).toString().contains("SELECT") &&
                !MyVars.app.getMsgBroker().getConfigPanel().getEdgeLabelTable().getValueAt(i, 1).toString().contains("NO") &&
                !MyVars.app.getMsgBroker().getConfigPanel().getEdgeLabelTable().getValueAt(i, 2).toString().contains("SELECT")) {
                String value = MyVars.app.getMsgBroker().getConfigPanel().getEdgeLabelTable().getValueAt(i, 0).toString().substring(2);
                MyVars.edgeLabelSet.add(MyVars.app.getMsgBroker().getConfigPanel().getEdgeLabelTable().getValueAt(i, 0).toString().substring(2));
                MyVars.app.getMsgBroker().generateEdgeLabelFeatures(value, "category");
            }
        }
    }

    private void setNodeValueNames() {
        for (int i = 0; i < MyVars.app.getMsgBroker().getConfigPanel().getNodeValueTable().getRowCount(); i++) {
            if (!MyVars.app.getMsgBroker().getConfigPanel().getNodeValueTable().getValueAt(i, 0).toString().contains("SELECT") &&
                !MyVars.app.getMsgBroker().getConfigPanel().getNodeValueTable().getValueAt(i, 1).toString().contains("NO") &&
                !MyVars.app.getMsgBroker().getConfigPanel().getNodeValueTable().getValueAt(i, 2).toString().contains("SELECT")) {
                String value = MyVars.app.getMsgBroker().getConfigPanel().getNodeValueTable().getValueAt(i, 0).toString().replaceAll(" ", "");
                String valueType = MyVars.app.getMsgBroker().getConfigPanel().getNodeValueTable().getValueAt(i, 2).toString().replaceAll(" ", "");
                MyVars.nodeValueMap.put(MyVars.app.getMsgBroker().getConfigPanel().getNodeValueTable().getValueAt(i, 0).toString().substring(2), valueType);
                MyVars.app.getMsgBroker().generateNodeValueFeatures(value, valueType);
            }
        }
    }

    private void setNodeLabelNames() {
        for (int i = 0; i < MyVars.app.getMsgBroker().getConfigPanel().getNodeLabelTable().getRowCount(); i++) {
            if (!MyVars.app.getMsgBroker().getConfigPanel().getNodeLabelTable().getValueAt(i, 0).toString().contains("SELECT") &&
                !MyVars.app.getMsgBroker().getConfigPanel().getNodeLabelTable().getValueAt(i, 1).toString().contains("NO") &&
                !MyVars.app.getMsgBroker().getConfigPanel().getNodeLabelTable().getValueAt(i, 2).toString().contains("SELECT")) {
                String value = MyVars.app.getMsgBroker().getConfigPanel().getNodeLabelTable().getValueAt(i, 0).toString().replaceAll(" ", "");
                String valueType = MyVars.app.getMsgBroker().getConfigPanel().getNodeLabelTable().getValueAt(i, 2).toString().replaceAll(" ", "");
                MyVars.nodeLabelSet.add(MyVars.app.getMsgBroker().getConfigPanel().getNodeLabelTable().getValueAt(i, 0).toString().substring(2));
                MyVars.app.getMsgBroker().generateNodeLabelFeatures(value, valueType);
            }
        }
    }

    public MyTimeConstrainedBuilider() {
        this.setEdgeValueNames();
        this.setEdgeLabelNames();
        this.setNodeValueNames();
        this.setNodeLabelNames();
        MyVars.g = new MyGraph();
    }

    public void createNodes() {
        File [] patternFiles = MySysUtil.getFileList(MyVars.outputDir);
        for (File file : patternFiles) {
            String [] nodeFileProperties = file.getName().split(MyVars.contributionSymbol);
            String nodeName = nodeFileProperties[0];
            nodeFileProperties = nodeFileProperties[1].split(MyVars.uniqueContributionSeparator);
            long contribution = Long.valueOf(nodeFileProperties[0]);
            nodeFileProperties = nodeFileProperties[1].split(MyVars.timeSeparator);
            long uniqueContribution = Long.valueOf(nodeFileProperties[0]);
            nodeFileProperties = nodeFileProperties[1].split(MyVars.durationSeparator);
            long duration = Long.valueOf(nodeFileProperties[1]);
            long totalReachTime = 0L;
            MyNode n = new MyNode(nodeName, contribution, uniqueContribution);
            n.setDuration(duration);
            MyVars.g.addVertex(n);
            MyVars.g.vRefs.put(nodeName, n);
            if (uniqueContribution > MyVars.g.maxUniqueContribution) {MyVars.g.maxUniqueContribution = uniqueContribution;}
            if (uniqueContribution < MyVars.g.minUniqueContribution) {MyVars.g.minUniqueContribution = uniqueContribution;}
            if (contribution > MyVars.g.maxNodeContribution) {MyVars.g.maxNodeContribution = contribution;}
            if (contribution < MyVars.g.minNodeContribution) {MyVars.g.minNodeContribution = contribution;}
            if (totalReachTime > MyVars.g.maxTotalReachTime) {MyVars.g.maxTotalReachTime = totalReachTime;}
            if (totalReachTime < MyVars.g.minTotalReachTime) {MyVars.g.minTotalReachTime = totalReachTime;}
            MyVars.g.totalUniqueContribution += uniqueContribution;
            MyVars.g.totalContribution += contribution;
            MyVars.g.totalReachTime += totalReachTime;
        }
    }

    private void createEdgesWithVariables() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(MySysUtil.getWorkingDir() + MySysUtil.getDirectorySlash() + "sequences.txt"));
            String line = "";
            Map<MyEdge, Integer> globalEdgeUniqueContributionMap = new HashMap<>();

            /*******************************************************
             *  Create edges between variables and items.
             *******************************************************/
            while ((line = br.readLine()) != null) {
                String [] itemsets = line.split(MyVars.hyphenDelimeter);
                String p = itemsets[0].split(":")[0];
                Map<MyEdge, Integer> localEdgeUniqueContributionMap = new HashMap<>();
                ((MyNode) MyVars.g.vRefs.get(p)).updateContribution(itemsets.length-2);
                for (int i = 1; i < itemsets.length; i++) {
                    String s = itemsets[i].split(":")[0];
                    String edgeName = p + "-" + s;
                    if (!MyVars.g.edgeRefMap.containsKey(edgeName)) {
                        MyNode pN = (MyNode) MyVars.g.vRefs.get(p);
                        MyNode sN = (MyNode) MyVars.g.vRefs.get(s);
                        MyEdge edge = new MyEdge(pN, sN);
                        MyVars.g.addEdge(edge, pN, sN);
                        MyVars.g.edgeRefMap.put(edgeName, edge);
                        edge.setContribution(1);
                    } else {
                        ((MyEdge) MyVars.g.edgeRefMap.get(edgeName)).setContribution(1);
                        localEdgeUniqueContributionMap.put((MyEdge) MyVars.g.edgeRefMap.get(edgeName), 1);
                    }
                }

                for (MyEdge e : localEdgeUniqueContributionMap.keySet()) {
                    if (globalEdgeUniqueContributionMap.containsKey(e)) {
                        globalEdgeUniqueContributionMap.put(e, globalEdgeUniqueContributionMap.get(e)+1);
                    } else {
                        globalEdgeUniqueContributionMap.put(e, 1);
                    }
                }
            }

            for (MyEdge e : globalEdgeUniqueContributionMap.keySet()) {
                e.setUniqueContribution(globalEdgeUniqueContributionMap.get(e));
            }

            /********************************************************
             * Create edges between items.
             ********************************************************/
            globalEdgeUniqueContributionMap = new HashMap<>();
            br = new BufferedReader(new FileReader(MySysUtil.getWorkingDir() + MySysUtil.getDirectorySlash() + "sequences.txt"));
            while ((line = br.readLine()) != null) {
                String [] itemsets = line.split(MyVars.hyphenDelimeter);
                Map<MyEdge, Integer> localEdgeUniqueContributionMap = new HashMap<>();
                for (int i = 2; i < itemsets.length; i++) {
                    String p = itemsets[i-1].split(":")[0];
                    String s = itemsets[i].split(":")[0];
                    String edgeName = p + "-" + s;
                    if (!MyVars.g.edgeRefMap.containsKey(edgeName)) {
                        MyNode pN = (MyNode) MyVars.g.vRefs.get(p);
                        MyNode sN = (MyNode) MyVars.g.vRefs.get(s);
                        MyEdge edge = new MyEdge(pN, sN);
                        MyVars.g.addEdge(edge, pN, sN);
                        MyVars.g.edgeRefMap.put(edgeName, edge);
                        edge.setContribution(1);
                    } else {
                        ((MyEdge) MyVars.g.edgeRefMap.get(edgeName)).setContribution(1);
                        localEdgeUniqueContributionMap.put((MyEdge) MyVars.g.edgeRefMap.get(edgeName), 1);
                    }
                }

                for (MyEdge e : localEdgeUniqueContributionMap.keySet()) {
                    if (globalEdgeUniqueContributionMap.containsKey(e)) {
                        globalEdgeUniqueContributionMap.put(e, globalEdgeUniqueContributionMap.get(e)+1);
                    } else {
                        globalEdgeUniqueContributionMap.put(e, 1);
                    }
                }
            }

            for (MyEdge e : globalEdgeUniqueContributionMap.keySet()) {
                e.setUniqueContribution(globalEdgeUniqueContributionMap.get(e));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



    public void createEdges() {
        try {
            if (MyVars.isSupplementaryOn) {
                this.createEdgesWithVariables();
                //System.out.println(((MyPlusEdge)MyVars.plusMarkovChain.edgeRefMap.get("4x-20")).getContribution());
            } else { // Not supplementary variables
                BufferedReader br = new BufferedReader(new FileReader(MySysUtil.getWorkingDir()+MySysUtil.getDirectorySlash()+"sequences.txt"));
                String line = "";
                Map<MyEdge, Integer> plusEdgeUniqueContributionMap = new HashMap<>();
                while ((line = br.readLine()) != null) {
                    Map<MyEdge, Integer> uniqueContributionMap = new HashMap<>();
                    String [] itemsets = line.split(MyVars.hyphenDelimeter);
                    for (int i=1; i < itemsets.length; i++) {
                        String p = itemsets[i-1].split(":")[0];
                        String s = itemsets[i].split(":")[0];
                        String edgeRef = p + "-" + s;
                        MyEdge plusEdge = (MyEdge)MyVars.g.edgeRefMap.get(edgeRef);
                        if (plusEdge == null) {
                            MyNode pN = (MyNode)MyVars.g.vRefs.get(p);
                            MyNode sN = (MyNode)MyVars.g.vRefs.get(s);
                            plusEdge = new MyEdge(pN, sN);
                            MyVars.g.addEdge(plusEdge, pN, sN);
                            MyVars.g.edgeRefMap.put(edgeRef, plusEdge);
                        }
                        plusEdge.setContribution(1);
                        uniqueContributionMap.put(plusEdge, 1);
                    }
                    for (MyEdge e : uniqueContributionMap.keySet()) {
                        if (plusEdgeUniqueContributionMap.containsKey(e)) {
                            plusEdgeUniqueContributionMap.put(e, plusEdgeUniqueContributionMap.get(e)+1);
                        } else {plusEdgeUniqueContributionMap.put(e, 1);}
                    }
                }
                for (MyEdge e : plusEdgeUniqueContributionMap.keySet())  {
                    e.setUniqueContribution(plusEdgeUniqueContributionMap.get(e));
                }
            }
        } catch (Exception ex) {ex.printStackTrace();}
    }

    public void setTimeConstrainedEdgeValues() {
        try {
            JTable edgeValueTable = MyVars.app.getMsgBroker().getConfigPanel().getEdgeValueTable();
            for (int i = 0; i < edgeValueTable.getRowCount(); i++) {
                String value = edgeValueTable.getValueAt(i, 0).toString();
                String isValue = edgeValueTable.getValueAt(i, 1).toString();
                String valueType = edgeValueTable.getValueAt(i, 2).toString();
                if (valueType.contains("MAXIMUM")) {
                    String file = MySysUtil.getWorkingDir() + MySysUtil.getDirectorySlash() + value.replaceAll(" ", "").toLowerCase() + ".edgeval";
                    BufferedReader bw = new BufferedReader(new FileReader(file));
                    String line = "";
                    while ((line = bw.readLine()) != null) {
                        String [] itemsets = line.split(":");
                        if (itemsets.length < 4) continue;
                        for (int k = 3; k < itemsets.length; k++) {
                            String pred = itemsets[k-3];
                            String succ = itemsets[k-1];
                            String predTime = itemsets[k-2].split(MyVars.commaDelimeter)[0];
                            String succTime = itemsets[k].split(MyVars.commaDelimeter)[0];
                            String edgeRef = pred + "-" + succ;
                            MyEdge edge = (MyEdge) MyVars.g.edgeRefMap.get(edgeRef);
                            edge.setEdgeMaxValue(value.substring(2), Math.abs(Float.valueOf(MySysUtil.getTimeDifference(predTime, succTime))));
                            k++;
                        }
                    }
                } else if (valueType.contains("AVERAGE")) {
                    String file = MySysUtil.getWorkingDir() + MySysUtil.getDirectorySlash() + value.replaceAll(" ", "").toLowerCase() + ".edgeval";
                    BufferedReader bw = new BufferedReader(new FileReader(file));
                    String line = "";
                    while ((line = bw.readLine()) != null) {
                        String [] itemsets = line.split(":");
                        if (itemsets.length < 4) continue;
                        for (int k = 3; k < itemsets.length; k++) {
                            String pred = itemsets[k-3];
                            String succ = itemsets[k-1];
                            String predTime = itemsets[k-2].split(MyVars.commaDelimeter)[0];
                            String succTime = itemsets[k].split(MyVars.commaDelimeter)[0];
                            String edgeRef = pred + "-" + succ;
                            MyEdge edge = (MyEdge) MyVars.g.edgeRefMap.get(edgeRef);
                            edge.setEdgeAverageValue(value.substring(2), Math.abs(Float.valueOf(MySysUtil.getTimeDifference(predTime, succTime))));
                            k++;
                        }
                    }
                } else if (valueType.contains("DIFFERENCE")) {
                    String file = MySysUtil.getWorkingDir() + MySysUtil.getDirectorySlash() + value.replaceAll(" ", "").toLowerCase() + ".edgeval";
                    BufferedReader bw = new BufferedReader(new FileReader(file));
                    String line = "";
                    while ((line = bw.readLine()) != null) {
                        String [] itemsets = line.split(":");
                        if (itemsets.length < 4) continue;
                        for (int k = 3; k < itemsets.length; k++) {
                            String pred = itemsets[k-3];
                            String succ = itemsets[k-1];
                            String predTime = itemsets[k-2].split(MyVars.commaDelimeter)[0];
                            String succTime = itemsets[k].split(MyVars.commaDelimeter)[0];
                            String edgeRef = pred + "-" + succ;
                            MyEdge edge = (MyEdge) MyVars.g.edgeRefMap.get(edgeRef);
                            edge.setEdgeSumValue(value.substring(2), Math.abs(Float.valueOf(MySysUtil.getTimeDifference(predTime, succTime))));
                            k++;
                        }
                    }
                }
            }
        } catch (Exception ex) {ex.printStackTrace();}
    }

    public void setTimeConstrainedEdgeLabels() {
        try {
            JTable edgeLabelTable = MyVars.app.getMsgBroker().getConfigPanel().getEdgeLabelTable();
            for (int i = 0; i < edgeLabelTable.getRowCount(); i++) {
                String value = edgeLabelTable.getValueAt(i, 0).toString();
                String isValue = edgeLabelTable.getValueAt(i, 1).toString();
                String valueType = edgeLabelTable.getValueAt(i, 2).toString();
                if (isValue.replaceAll(" ", "").contains("YES") && (!valueType.contains("TIME") || !valueType.contains("DATE"))) {
                    String file = MySysUtil.getWorkingDir()+MySysUtil.getDirectorySlash()+value + MyVars.edgeLabelFileExt;
                    file = file.replaceAll(" ", "").toLowerCase();
                    BufferedReader bw = new BufferedReader(new FileReader(file));
                    String line = "";
                    while ((line = bw.readLine()) != null) {
                        String[] itemsets = line.split(MyVars.hyphenDelimeter);
                        if (itemsets.length < 3) continue;
                        for (int k = 2; k < itemsets.length; k++) {
                            String pred = itemsets[k-2];
                            String [] edgeWeightItems = itemsets[k-1].split(MyVars.commaDelimeter);
                            String succ = itemsets[k];
                            String edgeRef = pred + "-" + succ;
                            MyEdge edge = (MyEdge) MyVars.g.edgeRefMap.get(edgeRef);
                            edge.setEdgeLabel(value, edgeWeightItems[0]);
                            k++;
                        }
                    }
                }
            }
        } catch (Exception ex) {ex.printStackTrace();}
    }

    public void setTimeConstrainedNodeValues() {
        try {
            JTable noteValueTable = MyVars.app.getMsgBroker().getConfigPanel().getNodeValueTable();
            for (int i = 0; i < noteValueTable.getRowCount(); i++) {
                String valueVariable = noteValueTable.getValueAt(i, 0).toString();
                String isValue = noteValueTable.getValueAt(i, 1).toString();
                String valueType = noteValueTable.getValueAt(i, 2).toString();
                if (isValue.replaceAll(" ", "").contains("YES") && (!valueType.contains("TIME") || !valueType.contains("DATE"))) {
                    String file = MySysUtil.getWorkingDir()+MySysUtil.getDirectorySlash()+valueVariable + MyVars.nodeValueFileExt;
                    file = file.replaceAll(" ", "").toLowerCase();
                    BufferedReader bw = new BufferedReader(new FileReader(file));
                    if (valueType.contains("SUM")) {
                        String line = "";
                        while ((line = bw.readLine()) != null) {
                            String [] itemsets = line.split(MyVars.hyphenDelimeter);
                            for (int k = 1; k < itemsets.length; k++) {
                                String [] nodeValues = itemsets[k].split(MyVars.commaDelimeter);
                                float nodeValue = 0.00f;
                                for (String aValue : nodeValues) {nodeValue += Double.valueOf(aValue);}
                                MyNode n = (MyNode)MyVars.g.vRefs.get(itemsets[k-1]);
                                n.setNodeSumValue(valueVariable.substring(2), nodeValue);
                                k++;
                            }
                        }
                    } else if (valueType.contains("AVERAGE")) {
                        String line = "";
                        while ((line = bw.readLine()) != null) {
                            String [] itemsets = line.split(MyVars.hyphenDelimeter);
                            for (int k = 1; k < itemsets.length; k++) {
                                String [] nodeValues = itemsets[k].split(MyVars.commaDelimeter);
                                float totalValue = 0.00f;
                                for (String aValue : nodeValues) {totalValue += Float.valueOf(aValue);}
                                float averageValue = totalValue/nodeValues.length;
                                MyNode n = (MyNode)MyVars.g.vRefs.get(itemsets[k-1]);
                                n.setNodeAverageValue(valueVariable.substring(2), averageValue);
                                k++;
                            }
                        }
                    } else if (valueType.contains("DISTINCT")) {
                        String line = "";
                        while ((line = bw.readLine()) != null) {
                            String [] itemsets = line.split(MyVars.hyphenDelimeter);
                            for (int k = 1; k < itemsets.length; k++) {
                                String [] nodeValues = itemsets[k].split(MyVars.commaDelimeter);
                                MyNode n = (MyNode)MyVars.g.vRefs.get(itemsets[k-1]);
                                n.setNodeDistinctValue(valueVariable.substring(2), Float.valueOf(nodeValues[0]));
                                k++;
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {ex.printStackTrace();}
    }

    public void setTimeConstrainedNodeLabels() {
        try {
            JTable nodeLabelTable = MyVars.app.getMsgBroker().getConfigPanel().getNodeLabelTable();
            for (int i = 0; i < nodeLabelTable.getRowCount(); i++) {
                String valueVariable = nodeLabelTable.getValueAt(i, 0).toString();
                String isValue = nodeLabelTable.getValueAt(i, 1).toString();
                String valueType = nodeLabelTable.getValueAt(i, 2).toString();
                if (isValue.replaceAll(" ", "").contains("YES") && (!valueType.contains("TIME") || !valueType.contains("DATE"))) {
                    String file = MySysUtil.getWorkingDir()+MySysUtil.getDirectorySlash()+valueVariable+MyVars.nodeLabelFileExt;
                    file = file.replaceAll(" ", "").toLowerCase();
                    BufferedReader bw = new BufferedReader(new FileReader(file));
                    String line = "";
                    while ((line = bw.readLine()) != null) {
                        String[] itemsets = line.split(MyVars.hyphenDelimeter);
                        for (int k=1; k < itemsets.length; k++) {
                            String [] nodeValues = itemsets[k].split(MyVars.commaDelimeter);
                            MyNode n = (MyNode)MyVars.g.vRefs.get(itemsets[k-1]);
                            n.setNodeLabel(valueVariable.substring(2), nodeValues[0]);
                            k++;
                        }
                    }
                }
            }
        } catch (Exception ex) {ex.printStackTrace();}
    }

    public void setTimeConstrainedEdgeTimeValues() {
        try {
            JTable edgeValueTable = MyVars.app.getMsgBroker().getConfigPanel().getEdgeValueTable();
            for (int i = 0; i < edgeValueTable.getRowCount(); i++) {
                String value = edgeValueTable.getValueAt(i, 0).toString();
                String isValue = edgeValueTable.getValueAt(i, 1).toString();
                String valueType = edgeValueTable.getValueAt(i, 2).toString();
                if (valueType.contains("MAXIMUM")) {
                    String file = MySysUtil.getWorkingDir() + MySysUtil.getDirectorySlash() + value.replaceAll(" ", "").toLowerCase() + ".edgeval";
                    BufferedReader bw = new BufferedReader(new FileReader(file));
                    String line = "";
                    while ((line = bw.readLine()) != null) {
                        String [] itemsets = line.split(":");
                        if (itemsets.length < 4) continue;
                        for (int k = 3; k < itemsets.length; k++) {
                            String pred = itemsets[k-3];
                            String succ = itemsets[k-1];
                            String predTime = itemsets[k-2].split(MyVars.commaDelimeter)[0];
                            String succTime = itemsets[k].split(MyVars.commaDelimeter)[0];
                            String edgeRef = pred + "-" + succ;
                            MyEdge edge = (MyEdge) MyVars.g.edgeRefMap.get(edgeRef);
                            edge.setEdgeSumValue(value.substring(2), Math.abs(Float.valueOf(MySysUtil.getTimeDifference(predTime, succTime))));
                            k++;
                        }
                    }
                } else if (valueType.contains("DIFFERENCE")) {
                    String file = MySysUtil.getWorkingDir() + MySysUtil.getDirectorySlash() + value.replaceAll(" ", "").toLowerCase() + ".edgeval";
                    BufferedReader bw = new BufferedReader(new FileReader(file));
                    String line = "";
                    while ((line = bw.readLine()) != null) {
                        String [] itemsets = line.split(":");
                        if (itemsets.length < 4) continue;
                        for (int k = 3; k < itemsets.length; k++) {
                            String pred = itemsets[k-3];
                            String succ = itemsets[k-1];
                            String predTime = itemsets[k-2].split(MyVars.commaDelimeter)[0];
                            String succTime = itemsets[k].split(MyVars.commaDelimeter)[0];
                            String edgeRef = pred + "-" + succ;
                            MyEdge edge = (MyEdge) MyVars.g.edgeRefMap.get(edgeRef);
                            edge.setEdgeSumValue(value.substring(2), Math.abs(Float.valueOf(MySysUtil.getTimeDifference(predTime, succTime))));
                            k++;
                        }
                    }
                }
            }
        } catch (Exception ex) {ex.printStackTrace();}
    }

    public void setNodeDuration() {
        Collection<MyNode> nodes = MyVars.g.getVertices();
        for (MyNode n : nodes) {
            for (int s = 0; s < MyVars.seqs.length; s++) {
                for (int i = 1; i < MyVars.seqs[s].length; i++) {
                    String p = MyVars.seqs[s][i-1].split(":")[0];
                    if (p.equals(n.getName())) {
                        long duration = Long.valueOf(MyVars.seqs[s][i-1].split(":")[1]);
                        n.setDuration(duration);
                    }
                }
            }
        }
    }

    public void setEndNodeCount() {
        Collection<MyNode> nodes = MyVars.g.getVertices();
        for (MyNode n : nodes) {
            for (int s = 0; s < MyVars.seqs.length; s++) {
                String itemset = MyVars.seqs[s][MyVars.seqs[s].length-1].split(":")[0];
                if (itemset.equals(n.getName())) {
                    n.setEndNodeCount(1);
                }
            }
        }
    }

    public void setOpenNodeCount() {
        Collection<MyNode> nodes = MyVars.g.getVertices();
        for (MyNode n : nodes) {
            for (int s = 0; s < MyVars.seqs.length; s++) {
                if (MyVars.seqs[s].length == 1) {continue;}
                for (int i=0; i < 2; i++) {
                    String itemset = MyVars.seqs[s][i].split(":")[0];
                    if (itemset.equals(n.getName())) {
                        n.setOpenNodeCount(1);
                    }
                }
            }
        }
    }

    public void setNodePropagationCount() {
        Collection<MyNode> nodes = MyVars.g.getVertices();
        for (MyNode n : nodes) {
            for (int s=0; s < MyVars.seqs.length; s++) {
                for (int i=0; i < MyVars.seqs[s].length; i++) {
                    String itemset = MyVars.seqs[s][i].split(":")[0];
                    if (itemset.equals(n.getName())) {
                        int propagation = MyVars.seqs[s].length-(i+1);
                        n.setPropagation(propagation);
                        break;
                    }
                }
            }
        }
    }

    public void setDirectNodeRecurrenceCount() {
        for (int s=0; s < MyVars.seqs.length; s++) {
            for (int i = 1; i < MyVars.seqs[s].length; i++) {
                String p = MyVars.seqs[s][i-1].split(":")[0];
                String ss = MyVars.seqs[s][i].split(":")[0];
                if (ss.equals(p)) {
                    ((MyNode) MyVars.g.vRefs.get(p)).setDirectRecurrenceCount(1);
                }
            }
        }
    }

    public void setAverageNodeRecurrenceLength() {
        if (MyVars.isSupplementaryOn) {
            Map<String, ArrayList<Integer>> nodeRecurrenceLengthMap = new HashMap<>();
            for (int s = 0; s < MyVars.seqs.length; s++) {
                Map<String, ArrayList<Integer>> sequenceNodeRecurrenceLengthMap = new HashMap<>();
                for (int i = 1; i < MyVars.seqs[s].length; i++) {
                    String n = MyVars.seqs[s][i].split(":")[0];
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
                double avgLength = (totalLength == 0 ? 0 : (double)totalLength/lengths.size());
                ((MyNode)MyVars.g.vRefs.get(n)).setAverageRecurrenceLength(avgLength);
            }
        } else {
            Map<String, ArrayList<Integer>> nodeRecurrenceLengthMap = new HashMap<>();
            for (int s = 0; s < MyVars.seqs.length; s++) {
                Map<String, ArrayList<Integer>> sequenceNodeRecurrenceLengthMap = new HashMap<>();
                for (int i = 0; i < MyVars.seqs[s].length; i++) {
                    String n = MyVars.seqs[s][i].split(":")[0];
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
                double avgLength = (totalLength == 0 ? 0 : (double) totalLength / lengths.size());
                ((MyNode) MyVars.g.vRefs.get(n)).setAverageRecurrenceLength(avgLength);
            }
        }
    }

    public void setNodeDepthInformation() {
        try {
            Collection<MyNode> nodes = MyVars.g.getVertices();
            for (MyNode n : nodes) {
                Map<Integer, Set<String>> predecessorByDepthMap = new HashMap<>();
                Map<Integer, Set<String>> successorByDepthMap = new HashMap<>();
                Map<Integer, MyNodeDepthInfo> nodeDepthInfoMap = new HashMap<>();
                for (int s = 0; s < MyVars.seqs.length; s++) {
                    for (int i=0; i < MyVars.seqs[s].length; i++) {
                        String [] itemset = MyVars.seqs[s][i].split(":");
                        if (itemset[0].equals(n.getName())) {
                            if (MyVars.seqs[s].length==1) {
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
                                    successors.add(MyVars.seqs[s][i+1].split(":")[0]);
                                    successorByDepthMap.put(i+1, successors);
                                } else {
                                    MyNodeDepthInfo nodeDepthInfo = new MyNodeDepthInfo();
                                    nodeDepthInfo.setContribution(1);
                                    nodeDepthInfo.setOutContribution(1);
                                    nodeDepthInfoMap.put(i+1, nodeDepthInfo);

                                    Set<String> successors = new HashSet<>();
                                    successors.add(MyVars.seqs[s][i+1].split(":")[0]);
                                    successorByDepthMap.put(i+1, successors);

                                    Set<String> predecessors = new HashSet<>();
                                    predecessorByDepthMap.put(i+1, predecessors);
                                }
                            } else if ((i+1) == MyVars.seqs[s].length) {
                                if (nodeDepthInfoMap.containsKey(i+1)) {
                                    MyNodeDepthInfo nodeDepthInfo = nodeDepthInfoMap.get(i+1);
                                    nodeDepthInfo.setContribution(1);
                                    nodeDepthInfo.setReachTime(nodeDepthInfo.getReachTime()+Long.valueOf(itemset[1]));
                                    nodeDepthInfoMap.put(i+1, nodeDepthInfo);

                                    Set<String> predecessors = predecessorByDepthMap.get(i+1);
                                    predecessors.add(MyVars.seqs[s][i-1].split(":")[0]);
                                    predecessorByDepthMap.put(i+1, predecessors);
                                } else {
                                    MyNodeDepthInfo nodeDepthInfo = new MyNodeDepthInfo();
                                    nodeDepthInfo.setContribution(1);
                                    nodeDepthInfo.setInContribution(1);
                                    nodeDepthInfoMap.put(i+1, nodeDepthInfo);

                                    Set<String> predecessors = new HashSet<>();
                                    predecessors.add(MyVars.seqs[s][i-1].split(":")[0]);
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
                                    long duration = Long.valueOf(MyVars.seqs[s][i+1].split(":")[1]) + nodeDepthInfo.getDuration();
                                    nodeDepthInfo.setDuration(duration);
                                    nodeDepthInfo.setReachTime(nodeDepthInfo.getReachTime()+Long.valueOf(itemset[1]));
                                    nodeDepthInfoMap.put(i+1, nodeDepthInfo);

                                    Set<String> predecessors = predecessorByDepthMap.get(i+1);
                                    predecessors.add(MyVars.seqs[s][i-1].split(":")[0]);
                                    predecessorByDepthMap.put(i+1, predecessors);

                                    Set<String> successors = successorByDepthMap.get(i+1);
                                    successors.add(MyVars.seqs[s][i+1].split(":")[0]);
                                    successorByDepthMap.put(i+1, successors);
                                } else {
                                    MyNodeDepthInfo nodeDepthInfo = new MyNodeDepthInfo();
                                    nodeDepthInfo.setContribution(1);
                                    nodeDepthInfo.setOutContribution(1);
                                    nodeDepthInfo.setInContribution(1);
                                    nodeDepthInfo.setDuration(Long.valueOf(MyVars.seqs[s][i+1].split(":")[1]));
                                    nodeDepthInfo.setReachTime(Long.valueOf(itemset[1]));
                                    nodeDepthInfoMap.put(i+1, nodeDepthInfo);

                                    Set<String> predecessors = new HashSet<>();
                                    predecessors.add(MyVars.seqs[s][i-1].split(":")[0]);
                                    predecessorByDepthMap.put(i+1, predecessors);

                                    Set<String> successors = new HashSet<>();
                                    successors.add(MyVars.seqs[s][i+1].split(":")[0]);
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
        Collection<MyNode> nodes = MyVars.g.getVertices();
        for (MyNode n : nodes) {
            for (int s=0; s < MyVars.seqs.length; s++) {
                for (int i=1; i < MyVars.seqs[s].length; i++) {
                    String itemset = MyVars.seqs[s][i].split(":")[0];
                    if (itemset.equals(n.getName())) {n.setInContribution(1);}
                }
            }
        }
    }

    public void setNodeOutContributionCount() {
        Collection<MyNode> nodes = MyVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getName().contains("x")) {
                for (int s = 0; s < MyVars.seqs.length; s++) {
                    String itemset = MyVars.seqs[s][0].split(":")[0];
                    if (itemset.equals(n.getName())) {
                        int outCont = (MyVars.seqs[s].length-1);
                        n.setOutContribution(outCont);
                    }
                }
            } else {
                for (int s = 0; s < MyVars.seqs.length; s++) {
                    for (int i = 0; i < MyVars.seqs[s].length - 1; i++) {
                        String itemset = MyVars.seqs[s][i].split(":")[0];
                        if (itemset.equals(n.getName())) {
                            n.setOutContribution(1);
                        }
                    }
                }
            }
        }
    }

    public void setEdgeContributionCount() {
        Collection<MyEdge> edges = MyVars.g.getEdges();
        for (MyEdge e : edges) {
            for (int s=0; s < MyVars.seqs.length; s++) {
                for (int i=1; i < MyVars.seqs[s].length; i++) {
                    String p = MyVars.seqs[s][i-1].split(":")[0];
                    String ss = MyVars.seqs[s][i].split(":")[0];
                    if (p.equals(e.getSource().getName()) && ss.equals(e.getDest().getName())) {
                        e.setContribution(1);
                    }
                }
            }
        }
    }

    public void setEngeUniqueContributionCount() {
        Collection<MyEdge> edges = MyVars.g.getEdges();
        for (MyEdge e : edges) {
            for (int s=0; s < MyVars.seqs.length; s++) {
                for (int i=1; i < MyVars.seqs[s].length; i++) {
                    String p = MyVars.seqs[s][i-1].split(":")[0];
                    String ss = MyVars.seqs[s][i].split(":")[0];
                    if (p.equals(e.getSource().getName()) && ss.equals(e.getDest().getName())) {
                        e.setUniqueContribution(1);
                        break;
                    }
                }
            }
        }
    }

    public void setEdgeReachTime() {
        Collection<MyEdge> edges = MyVars.g.getEdges();
        for (MyEdge e : edges) {
            for (int s=0; s < MyVars.seqs.length; s++) {
                for (int i=1; i < MyVars.seqs[s].length; i++) {
                    String p = MyVars.seqs[s][i-1].split(":")[0];
                    String ss = MyVars.seqs[s][i].split(":")[0];
                    if (p.equals(e.getSource().getName()) && ss.equals(e.getDest().getName())) {
                        long reachTime = Long.valueOf(MyVars.seqs[s][i].split(":")[1]);
                        e.setReachTime(reachTime);
                    }
                }
            }
        }
    }

    public void setEdgeSupport() {
        Collection<MyEdge> edges = MyVars.g.getEdges();
        for (MyEdge edge : edges) {edge.setSupport();}
    }

    public void setEdgeConfidence() {
        Collection<MyEdge> edges = MyVars.g.getEdges();
        for (MyEdge edge : edges) {edge.setConfidence();}
    }

    public void setEdgeLift() {
        Collection<MyEdge> edges = MyVars.g.getEdges();
        for (MyEdge edge : edges) {edge.setLift();}
    }

    public void setTotalEdgeContribution() {
        MyVars.g.setTotalEdgeContribution();
    }

    public void setNodeBetweenessCentrality() {
        BetweennessCentrality<MyNode, MyEdge> betweenness = new BetweennessCentrality<>(MyVars.g);
        betweenness.setRemoveRankScoresOnFinalize(false); // 최종 betweenness centrality 값을 유지합니다.
        betweenness.evaluate();

        Collection<MyNode> nodes = MyVars.g.getVertices();
        for (MyNode n : nodes) {
            //System.out.println("Betweenness centrality for vertex " + n.getName() + ": " + betweenness.getVertexRankScore(n));
            n.setBetweeness(Double.valueOf(MyMathUtil.twoDecimalFormat(betweenness.getVertexRankScore(n))));
        }

    }

    public void setNodeClosenessCentrality() {
        MyClosenessCentrality closenessCentrality = new MyClosenessCentrality(MyVars.g);
        Collection<MyNode> nodes = MyVars.g.getVertices();
        for (MyNode n : nodes) {
            double value = closenessCentrality.getVertexScore(n);
            n.setCloseness(Double.valueOf(MyMathUtil.twoDecimalFormat((value == Double.NaN ? 0 : value))));
        }
    }

    public void setNodeEigenVectorCentrality() {
        MyNodeEigenvectorCentrality eigenvectorCentrality = new MyNodeEigenvectorCentrality(MyVars.g);
        eigenvectorCentrality.acceptDisconnectedGraph(true);
        eigenvectorCentrality.evaluate();

        Collection<MyNode> nodes = MyVars.g.getVertices();
        for(MyNode n : nodes){
            n.setEignevector(((double)eigenvectorCentrality.getVertexScore(n))*1000);
            //System.out.println(MySysUtil.getDecodedNodeName(n.getName()) + " :eigen:"+ eigenvectorCentrality.getVertexScore(n));
        }
    }

    public void setPageRankScore() {
        PageRank<MyNode, MyEdge> ranker = new PageRank<MyNode, MyEdge>(MyVars.g, 0.3);
        ranker.setMaxIterations(1000);
        ranker.evaluate();
        Collection<MyNode> nodes = MyVars.g.getVertices();
        for (MyNode n : nodes) {
            n.setPageRankScore((int) Math.round(1000 * ranker.getVertexScore(n)));
            //System.out.println(n.getPageRankScore());
        }
    }

    public void setVariableNodeStrength() {
        try {
            if (MyVars.isSupplementaryOn) {
                for (int s = 0; s < MyVars.seqs.length; s++) {
                    String variable = MySysUtil.getDecodedNodeName(MyVars.seqs[s][0].split(":")[0]);
                    for (int i = 1; i < MyVars.seqs[s].length; i++) {
                        MyNode n = (MyNode) MyVars.g.vRefs.get(MyVars.seqs[s][i].split(":")[0]);
                        if (MyVars.seqs[s][i].split(":")[0].equals(n.getName())) {
                            n.setVariableNodeStrength(variable);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setConnectance() {
        MyVars.g.connectance = (double)MyVars.g.getEdgeCount()/(MyVars.g.getVertexCount()*(MyVars.g.getVertexCount()-1));
    }

    public void setEbsilonOfGraph() {
        MyVars.g.graphEbasilon = (double)MyVars.g.getEdgeCount()/MyVars.g.getVertexCount();
    }

    public void setAverageUnWeightedGraphShortestPathLength(Graph g) {MySysUtil.setAverageUnWeightedShortestPathLength(g);}
    public void setMaxNodeValue() {MyVars.g.setMaxNodeValue();}
    public void cleanWork(){ MyVars.g.edRefs = null; }

    public void setConnectedNetworkComponentCountByGraph() {
        MyConnectedNetworkComponentFinder.setConnectedComponentsByGraph();
    }
}