package medousa.sequential.graph;

import edu.uci.ics.jung.algorithms.cluster.WeakComponentClusterer;
import edu.uci.ics.jung.algorithms.importance.BetweennessCentrality;
import medousa.sequential.graph.common.MyClosenessCentrality;
import medousa.sequential.graph.common.MyNodeEigenvectorCentrality;
import medousa.sequential.utils.MyMathUtil;
import medousa.sequential.utils.MySequentialGraphSysUtil;
import medousa.sequential.utils.MySequentialGraphVars;
import edu.uci.ics.jung.algorithms.scoring.PageRank;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class MyTimeConstrainedBuilider {


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

    public MyTimeConstrainedBuilider() {
        this.setEdgeValueNames();
        this.setEdgeLabelNames();
        this.setNodeValueNames();
        this.setNodeLabelNames();
        MySequentialGraphVars.g = new MyGraph();
    }

    private void createGraphWithVariables() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(MySequentialGraphSysUtil.getWorkingDir() + MySequentialGraphSysUtil.getDirectorySlash() + "sequences.txt"));
            String line = "";

            /*******************************************************
             *  Create edges between variables and items.
             *******************************************************/
            while ((line = br.readLine()) != null) {

                String [] itemsets = line.split(MySequentialGraphVars.hyphenDelimeter);
                Set<String> sequenceEdgeSet = new HashSet<>();
                Set<String> sequenceNodeSet = new HashSet<>();

                for (int i = 1; i < itemsets.length; i++) {
                    String p = itemsets[0].split(":")[0];
                    String s = itemsets[i].split(":")[0];
                    String edgeName = p + "-" + s;

                    sequenceNodeSet.add(p);
                    sequenceNodeSet.add(s);
                    sequenceEdgeSet.add(edgeName);

                    if (!MySequentialGraphVars.g.edgeRefMap.containsKey(edgeName)) {
                        MyNode pNode = (MyNode) MySequentialGraphVars.g.vRefs.get(p);
                        MyNode sNode = (MyNode) MySequentialGraphVars.g.vRefs.get(s);

                        pNode.setContribution(1);
                        pNode.setOutContribution(1);
                        //sNode.setContribution(1);
                        sNode.setInContribution(1);

                        MyEdge edge = new MyEdge(pNode, sNode);
                        edge.setContribution(1);

                        MySequentialGraphVars.g.addEdge(edge, pNode, sNode);
                        MySequentialGraphVars.g.edgeRefMap.put(edgeName, edge);
                    } else {
                        MyNode pNode = (MyNode) MySequentialGraphVars.g.vRefs.get(p);
                        MyNode sNode = (MyNode) MySequentialGraphVars.g.vRefs.get(s);

                        pNode.setContribution(1);
                        pNode.setOutContribution(1);
                        //sNode.setContribution(1);
                        sNode.setInContribution(1);

                        ((MyEdge) MySequentialGraphVars.g.edgeRefMap.get(edgeName)).setContribution(1);
                    }
                }

                /**********************************************************
                 *  Set Node unique contribution.
                 **********************************************************/
                for (String n : sequenceNodeSet) {
                    ((MyNode) MySequentialGraphVars.g.vRefs.get(n)).setUniqueContribution();
                }

                /**********************************************************
                 *  Set Edge unique contribution.
                 **********************************************************/
                for (String e : sequenceEdgeSet) {
                    ((MyEdge) MySequentialGraphVars.g.edgeRefMap.get(e)).setUniqueContribution(1);
                }
            }

            /********************************************************
             * Create edges between items.
             ********************************************************/
            br = new BufferedReader(new FileReader(
                 MySequentialGraphSysUtil.getWorkingDir() + MySequentialGraphSysUtil.getDirectorySlash() + "sequences.txt"));
            while ((line = br.readLine()) != null) {
                String [] itemsets = line.split(MySequentialGraphVars.hyphenDelimeter);
                Set<String> sequenceEdgeSet = new HashSet<>();
                Set<String> sequenceNodeSet = new HashSet<>();

                for (int i = 2; i < itemsets.length; i++) {
                    String p = itemsets[i-1].split(":")[0];
                    String s = itemsets[i].split(":")[0];
                    String edgeName = p + "-" + s;

                    sequenceNodeSet.add(p);
                    sequenceNodeSet.add(s);
                    sequenceEdgeSet.add(edgeName);

                    if (!MySequentialGraphVars.g.edgeRefMap.containsKey(edgeName)) {
                        MyNode pNode = (MyNode) MySequentialGraphVars.g.vRefs.get(p);
                        MyNode sNode = (MyNode) MySequentialGraphVars.g.vRefs.get(s);

                        pNode.setContribution(1);
                        pNode.setOutContribution(1);
                        if ((i+1) == itemsets.length) {
                            sNode.setContribution(1);
                        }
                        sNode.setInContribution(1);

                        MyEdge edge = new MyEdge(pNode, sNode);
                        edge.setContribution(1);

                        MySequentialGraphVars.g.addEdge(edge, pNode, sNode);
                        MySequentialGraphVars.g.edgeRefMap.put(edgeName, edge);
                    } else {
                        MyNode pNode = (MyNode) MySequentialGraphVars.g.vRefs.get(p);
                        MyNode sNode = (MyNode) MySequentialGraphVars.g.vRefs.get(s);

                        pNode.setContribution(1);
                        pNode.setOutContribution(1);
                        if ((i+1) == itemsets.length) {
                            sNode.setContribution(1);
                        }
                        sNode.setInContribution(1);
                        ((MyEdge) MySequentialGraphVars.g.edgeRefMap.get(edgeName)).setContribution(1);
                    }
                }

                /**********************************************************
                 *  Set Node unique contribution.
                 **********************************************************/
                for (String n : sequenceNodeSet) {
                    ((MyNode) MySequentialGraphVars.g.vRefs.get(n)).setUniqueContribution();
                }

                /**********************************************************
                 *  Set Edge unique contribution.
                 **********************************************************/
                for (String e : sequenceEdgeSet) {
                    ((MyEdge) MySequentialGraphVars.g.edgeRefMap.get(e)).setUniqueContribution(1);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void createNodes() {
        File[] ptrnFiles = MySequentialGraphSysUtil.getFileList(MySequentialGraphVars.outputDir);
        for (File file : ptrnFiles) {
            String node = file.getName().split(MySequentialGraphVars.contributionSymbol)[0];
            MyNode n = new MyNode(node);
            MySequentialGraphVars.g.addVertex(n);
            MySequentialGraphVars.g.vRefs.put(node, n);
        }
    }

    public void createGraph() {
        try {
            createNodes();
            if (MySequentialGraphVars.isSupplementaryOn) {
                this.createGraphWithVariables();
            } else { // Not supplementary variables
                createGraphWithoutVariables();
            }
        } catch (Exception ex) {ex.printStackTrace();}
    }

    private void createGraphWithoutVariables() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(MySequentialGraphSysUtil.getWorkingDir() + MySequentialGraphSysUtil.getDirectorySlash() + "sequences.txt"));
            String line = "";

            while ((line = br.readLine()) != null) {
                String[] itemsets = line.split(MySequentialGraphVars.hyphenDelimeter);
                Set<String> sequenceEdgeSet = new HashSet<>();
                Set<String> sequenceNodeSet = new HashSet<>();

                for (int i = 1; i < itemsets.length; i++) {
                    String p = itemsets[i-1].split(":")[0];
                    String s = itemsets[i].split(":")[0];
                    String edgeName = p + "-" + s;

                    sequenceNodeSet.add(p);
                    sequenceNodeSet.add(s);
                    sequenceEdgeSet.add(edgeName);

                    if (!MySequentialGraphVars.g.edgeRefMap.containsKey(edgeName)) {
                        MyNode pNode = (MyNode) MySequentialGraphVars.g.vRefs.get(p);
                        MyNode sNode = (MyNode) MySequentialGraphVars.g.vRefs.get(s);

                        pNode.setContribution(1);
                        pNode.setOutContribution(1);
                        if ((i+1) == itemsets.length) {
                            sNode.setContribution(1);
                        }
                        sNode.setInContribution(1);

                        MyEdge edge = new MyEdge(pNode, sNode);
                        MySequentialGraphVars.g.addEdge(edge, pNode, sNode);
                        MySequentialGraphVars.g.edgeRefMap.put(edgeName, edge);
                    } else {
                        MyNode pNode = (MyNode) MySequentialGraphVars.g.vRefs.get(p);
                        MyNode sNode = (MyNode) MySequentialGraphVars.g.vRefs.get(s);

                        pNode.setContribution(1);
                        pNode.setOutContribution(1);
                        if ((i+1) == itemsets.length) {
                            sNode.setContribution(1);
                        }
                        sNode.setInContribution(1);

                        ((MyEdge) MySequentialGraphVars.g.edgeRefMap.get(edgeName)).setContribution(1);
                    }
                }

                /**********************************************************
                 *  Set Node unique contribution.
                 **********************************************************/
                for (String n : sequenceNodeSet) {
                    ((MyNode) MySequentialGraphVars.g.vRefs.get(n)).setUniqueContribution();
                }

                /**********************************************************
                 *  Set Edge unique contribution.
                 **********************************************************/
                for (String e : sequenceEdgeSet) {
                    ((MyEdge) MySequentialGraphVars.g.edgeRefMap.get(e)).setUniqueContribution(1);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setTimeConstrainedEdgeValues() {
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
                            edge.setEdgeMaxValue(value.substring(2), Math.abs(Float.valueOf(MySequentialGraphSysUtil.getTimeDifference(predTime, succTime))));
                            k++;
                        }
                    }
                } else if (valueType.contains("AVERAGE")) {
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
                            edge.setEdgeAverageValue(value.substring(2), Math.abs(Float.valueOf(MySequentialGraphSysUtil.getTimeDifference(predTime, succTime))));
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

    public void setTimeConstrainedEdgeLabels() {
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

    public void setTimeConstrainedNodeValues() {
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

    public void setTimeConstrainedNodeLabels() {
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

    public void setTimeConstrainedEdgeTimeValues() {
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

    public void setTotalNodeDuration() {
        for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
            for (int i = 1; i < MySequentialGraphVars.seqs[s].length; i++) {
                String pitemset = MySequentialGraphVars.seqs[s][i-1].split(":")[0];
                long duration = Long.parseLong(MySequentialGraphVars.seqs[s][i].split(":")[1]);
                MyNode n = (MyNode) MySequentialGraphVars.g.vRefs.get(pitemset);
                n.setTotalDuration(duration);
            }
        }
    }

    public void setEndNodeCount() {
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                String itemset = MySequentialGraphVars.seqs[s][MySequentialGraphVars.seqs[s].length-1].split(":")[0];
                if (itemset.equals(n.getName())) {
                    n.setEndPositionNodeCount(1);
                }
            }
        }
    }

    public void setStartPositionNodeCount() {
        if (MySequentialGraphVars.isSupplementaryOn) {
            for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                String n = MySequentialGraphVars.seqs[s][1].split(":")[0];
                ((MyNode) MySequentialGraphVars.g.vRefs.get(n)).setStartPositionNodeCount(1);
            }
        } else {
            for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                String n = MySequentialGraphVars.seqs[s][0].split(":")[0];
                ((MyNode) MySequentialGraphVars.g.vRefs.get(n)).setStartPositionNodeCount(1);
            }
        }
    }

    public void setNodePropagationCount() {
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                for (int i = 0; i < MySequentialGraphVars.seqs[s].length; i++) {
                    String itemset = MySequentialGraphVars.seqs[s][i].split(":")[0];
                    if (itemset.equals(n.getName())) {
                        int propagation = MySequentialGraphVars.seqs[s].length-(i+1);
                        n.setPropagation(propagation);
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

    public void setNodeRecurrence() {
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

    public void setNodeTotalReachTime() {
        for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
            for (int i = 1; i < MySequentialGraphVars.seqs[s].length; i++) {
                String ss = MySequentialGraphVars.seqs[s][i].split(":")[0];
                long time = Long.parseLong(MySequentialGraphVars.seqs[s][i].split(":")[1]);
                MyNode n = (MyNode) MySequentialGraphVars.g.vRefs.get(ss);
                n.setTotalReachTime(time);
            }
        }
    }

    public void setNumberOfGraphs() {
        // Get number of graphs.
        WeakComponentClusterer<MyNode, MyEdge> wcSearch = new WeakComponentClusterer<>();
        Set<Set<MyNode>> clusters = wcSearch.transform(MySequentialGraphVars.g);
        MySequentialGraphVars.numberOfGraphs = clusters.size();
    }

    public void setNodeTotalRecurrenceTime() {
        for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
            for (int i = 1; i < MySequentialGraphVars.seqs[s].length; i++) {
                String ps = MySequentialGraphVars.seqs[s][i-1].split(":")[0];
                String ss = MySequentialGraphVars.seqs[s][i].split(":")[0];
                if (ps.equals(ss)) {

                    long pTime = Long.parseLong(MySequentialGraphVars.seqs[s][i-1].split(":")[1]);
                    long sTime = Long.parseLong(MySequentialGraphVars.seqs[s][i].split(":")[1]);

                    long rTime = sTime - pTime;
                    MyNode n = (MyNode) MySequentialGraphVars.g.vRefs.get(ss);
                    n.setTotalNodeRecurrenceTime(rTime);
                }
            }
        }
    }

    public void setNodeAverageRecurrenceTime() {
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getTotalNodeRecurrentCount() > 0) {
                n.setAverageNodeRecurrenceTime((float) n.getTotalNodeRecurrenceTime() / n.getTotalNodeRecurrentCount());
            }
        }
    }

    public void setAverageRecursiveTime() {
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            long totalTime = 0L;
            int totalCount = 0;
            for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                long sequenceTotalTime = 0L;
                int sequenceCount = -1;
                for (int i = 0; i < MySequentialGraphVars.seqs[s].length; i++) {
                    String nn = MySequentialGraphVars.seqs[s][i].split(":")[0];
                    if (nn.equals(n.getName())) {
                        sequenceCount++;
                        if (sequenceCount > 0) {
                            sequenceTotalTime += Long.parseLong(MySequentialGraphVars.seqs[s][i].split(":")[1]);
                            totalTime += sequenceTotalTime;
                            sequenceTotalTime = 0;
                            totalCount++;
                        }
                    } else if (sequenceCount > -1) {
                        sequenceTotalTime += Long.parseLong(MySequentialGraphVars.seqs[s][i].split(":")[1]);
                    }
                }
            }
            if (totalCount > 0) {
                n.setAverageRecursiveTime((float) totalTime / totalCount);
            }
        }
    }

    public void setNodeAverageReachTime() {
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            int count = 0;
            for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                for (int i = 1; i < MySequentialGraphVars.seqs[s].length; i++) {
                    String itemset = MySequentialGraphVars.seqs[s][i].split(":")[0];
                    if (itemset.equals(n.getName())) {
                        count++;
                    }
                }
            }
            n.setAverageTime((double) n.getTotalReachTime()/count);
        }
    }

    public void setAverageNodeRecurrenceLength() {
        Map<String, ArrayList<Integer>> nodeRecurrenceLengthMap = new HashMap<>();
        for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
            int i=1;
            int length = 0;
            String ps = MySequentialGraphVars.seqs[s][i-1].split(":")[0];
            for (; i < MySequentialGraphVars.seqs[s].length; i++) {
                String ss = MySequentialGraphVars.seqs[s][i].split(":")[0];
                if (ps.equals(ss)) {
                    length++;
                } else {
                    if (nodeRecurrenceLengthMap.containsKey(ps)) {
                        ArrayList<Integer> lengths = nodeRecurrenceLengthMap.get(ps);
                        lengths.add(length);
                        nodeRecurrenceLengthMap.put(ps, lengths);
                    } else {
                        ArrayList<Integer> lengths = new ArrayList<>();
                        lengths.add(length);
                        nodeRecurrenceLengthMap.put(ps, lengths);
                    }
                    length = 0;
                    ps = ss;

                }
            }
        }

        for (String nn : nodeRecurrenceLengthMap.keySet()) {
            ArrayList<Integer> lengths = nodeRecurrenceLengthMap.get(nn);
            int totalLength = 0;
            for (int length : lengths) {
                totalLength += length;
            }
            MyNode n = (MyNode) MySequentialGraphVars.g.vRefs.get(nn);
            n.setAverageRecurrenceLength((float)totalLength/lengths.size());
        }
    }

    public void setNodeContributionCountByObject() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(
                MySequentialGraphSysUtil.getWorkingDir() + MySequentialGraphSysUtil.getDirectorySlash() + "sequencesWithObjectIDs.txt")));
            int i= 1;
            String line = "";
            while ((line = br.readLine()) != null) {
                String [] itemsets = line.split(MySequentialGraphVars.hyphenDelimeter);
                String objectID = itemsets[0];
                for (; i < itemsets.length; i++) {
                    String itemset = itemsets[i].split(":")[0];
                    MyNode n = (MyNode) MySequentialGraphVars.g.vRefs.get(itemset);
                    if (n.contributionCountMapByObjectID.containsKey(objectID)) {
                        n.contributionCountMapByObjectID.put(objectID, n.contributionCountMapByObjectID.get(objectID) + 1);
                    } else {
                        n.contributionCountMapByObjectID.put(objectID, 1);
                    }
                }
                i= 1;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setReachTimesByObject() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(
                MySequentialGraphSysUtil.getWorkingDir() + MySequentialGraphSysUtil.getDirectorySlash() + "sequencesWithObjectIDs.txt")));
            int i= (MySequentialGraphVars.isSupplementaryOn ? 3 : 2);
            String line = "";
            while ((line = br.readLine()) != null) {
                String [] itemsets = line.split(MySequentialGraphVars.hyphenDelimeter);
                String objectID = itemsets[0];
                for (; i < itemsets.length; i++) {
                    String [] itemset = itemsets[i].split(":");
                    MyNode n = (MyNode) MySequentialGraphVars.g.vRefs.get(itemset[0]);
                    if (n.reachTimeMapByObjectID.containsKey(objectID)) {
                        if (n.reachTimeMapByObjectID.get(objectID).containsKey(Long.parseLong(itemset[1]))) {
                            Map<Long, Integer> reachTimeMap = n.reachTimeMapByObjectID.get(objectID);
                            long reachTimeKey = Long.parseLong(itemset[1]);
                            reachTimeMap.put(reachTimeKey, reachTimeMap.get(reachTimeKey) + 1);
                            n.reachTimeMapByObjectID.put(objectID, reachTimeMap);
                        } else {
                            Map<Long, Integer> reachTimeMap = new HashMap<>();
                            reachTimeMap.put(Long.parseLong(itemset[1]), 1);
                            n.reachTimeMapByObjectID.put(objectID, reachTimeMap);
                        }
                    } else {
                        Map<Long, Integer> reachTimeMap = new HashMap<>();
                        reachTimeMap.put(Long.parseLong(itemset[1]), 1);
                        n.reachTimeMapByObjectID.put(objectID, reachTimeMap);
                    }
                }
                i= (MySequentialGraphVars.isSupplementaryOn ? 3 : 2);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
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
                            if (MySequentialGraphVars.seqs[s].length==1) {
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
                                    long duration = Long.parseLong(MySequentialGraphVars.seqs[s][i+1].split(":")[1]);
                                    nodeDepthInfo.setDuration(duration);
                                    nodeDepthInfoMap.put(i+1, nodeDepthInfo);

                                    Set<String> successors = successorByDepthMap.get(i+1);
                                    successors.add(MySequentialGraphVars.seqs[s][i+1].split(":")[0]);
                                    successorByDepthMap.put(i+1, successors);
                                } else {
                                    MyNodeDepthInfo nodeDepthInfo = new MyNodeDepthInfo();
                                    nodeDepthInfo.setContribution(1);
                                    nodeDepthInfo.setOutContribution(1);
                                    long duration = Long.parseLong(MySequentialGraphVars.seqs[s][i+1].split(":")[1]);
                                    nodeDepthInfo.setDuration(duration);
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
                                    nodeDepthInfo.setReachTime(Long.parseLong(itemset[1]));
                                    nodeDepthInfoMap.put(i+1, nodeDepthInfo);

                                    Set<String> predecessors = predecessorByDepthMap.get(i+1);
                                    predecessors.add(MySequentialGraphVars.seqs[s][i-1].split(":")[0]);
                                    predecessorByDepthMap.put(i+1, predecessors);
                                } else {
                                    MyNodeDepthInfo nodeDepthInfo = new MyNodeDepthInfo();
                                    nodeDepthInfo.setContribution(1);
                                    nodeDepthInfo.setInContribution(1);
                                    nodeDepthInfo.setReachTime(Long.parseLong(itemset[1]));
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
                                    long duration = Long.parseLong(MySequentialGraphVars.seqs[s][i+1].split(":")[1]);
                                    nodeDepthInfo.setDuration(duration);
                                    nodeDepthInfo.setReachTime(Long.parseLong(itemset[1]));
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
                                    long duration = Long.parseLong(MySequentialGraphVars.seqs[s][i+1].split(":")[1]);
                                    nodeDepthInfo.setDuration(duration);
                                    nodeDepthInfo.setReachTime(Long.parseLong(itemset[1]));
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

                for (Integer i : predecessorByDepthMap.keySet()) {
                    nodeDepthInfoMap.get(i).setPredecessors(predecessorByDepthMap.get(i).size());
                }

                for (Integer i : successorByDepthMap.keySet()) {
                    nodeDepthInfoMap.get(i).setSuccessors(successorByDepthMap.get(i).size());
                }

                n.setNodeDepthInfoMap(nodeDepthInfoMap);
            }
        } catch (Exception ex) {ex.printStackTrace();}
    }

    public void setEdgeTotalAndAverageTime() {
        Collection<MyEdge> edges = MySequentialGraphVars.g.getEdges();
        TreeMap<MyEdge, Integer> frequencyMap = new TreeMap<>();
        for (MyEdge e : edges) {
            for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                for (int i = 1; i < MySequentialGraphVars.seqs[s].length; i++) {
                    String p = MySequentialGraphVars.seqs[s][i-1].split(":")[0];
                    String ss = MySequentialGraphVars.seqs[s][i].split(":")[0];
                    if (p.equals(e.getSource().getName()) && ss.equals(e.getDest().getName())) {
                        long time = Long.valueOf(MySequentialGraphVars.seqs[s][i].split(":")[1]);
                        e.setTotalTime(time);
                        if (frequencyMap.containsKey(e)) {
                            frequencyMap.put(e, frequencyMap.get(e) + 1);
                        } else {
                            frequencyMap.put(e, 1);
                        }
                    }
                }
            }
        }

        for (MyEdge e : frequencyMap.keySet()) {
            e.setAverageTime((float) e.getTotalTime()/frequencyMap.get(e));
        }
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

    public void setGraphLevelTotalEdgeContribution() {
        MySequentialGraphVars.g.setTotalEdgeContribution();
    }

    public void setNodeBetweenessCentrality() {
        BetweennessCentrality<MyNode, MyEdge> betweenness = new BetweennessCentrality<>(MySequentialGraphVars.g);
        betweenness.setRemoveRankScoresOnFinalize(false); // 최종 betweenness centrality 값을 유지합니다.
        betweenness.evaluate();

        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            //System.out.println("Betweenness centrality for vertex " + n.getName() + ": " + betweenness.getVertexRankScore(n));
            n.setBetweeness(Double.valueOf(MyMathUtil.twoDecimalFormat(betweenness.getVertexRankScore(n))));
        }

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
        eigenvectorCentrality.acceptDisconnectedGraph(true);
        eigenvectorCentrality.evaluate();

        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for(MyNode n : nodes){
            n.setEignevector(((double)eigenvectorCentrality.getVertexScore(n))*1000);
            //System.out.println(MySysUtil.getDecodedNodeName(n.getName()) + " :eigen:"+ eigenvectorCentrality.getVertexScore(n));
        }
    }

    public void setPageRankScore() {
        PageRank<MyNode, MyEdge> ranker = new PageRank<MyNode, MyEdge>(MySequentialGraphVars.g, 0.3);
        ranker.setMaxIterations(1000);
        ranker.evaluate();
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            n.setPageRankScore((int) Math.round(1000 * ranker.getVertexScore(n)));
            //System.out.println(n.getPageRankScore());
        }
    }

    public void setVariableNodeStrength() {
        try {
            if (MySequentialGraphVars.isSupplementaryOn) {
                for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                    String variable = MySequentialGraphSysUtil.getDecodedNodeName(MySequentialGraphVars.seqs[s][0].split(":")[0]);
                    for (int i = 1; i < MySequentialGraphVars.seqs[s].length; i++) {
                        MyNode n = (MyNode) MySequentialGraphVars.g.vRefs.get(MySequentialGraphVars.seqs[s][i].split(":")[0]);
                        if (MySequentialGraphVars.seqs[s][i].split(":")[0].equals(n.getName())) {
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
        MySequentialGraphVars.g.connectance = (double) MySequentialGraphVars.g.getEdgeCount()/(MySequentialGraphVars.g.getVertexCount()*(MySequentialGraphVars.g.getVertexCount()-1));
    }

    public void setEbsilonOfGraph() {
        MySequentialGraphVars.g.graphEbasilon = (double) MySequentialGraphVars.g.getEdgeCount()/ MySequentialGraphVars.g.getVertexCount();
    }

    public void setShortestDistanceProperties(MyGraph<MyNode, MyEdge> g) {
        long gTotalD = 0L;
        long gCount = 0L;
        Collection<MyNode> nodes = g.getVertices();
        for (MyNode start : nodes) {
            int maxShortestDistance = 0;
            Queue<MyNode> Qu =  new LinkedList<>();
            Set<MyNode> visitedNodeSet = new HashSet();
            Map<MyNode, Integer> distanceMap =  new HashMap();

            Qu.add(start);
            distanceMap.put(start, 0);
            while (!Qu.isEmpty()) {
                MyNode vertex = Qu.remove();
                Collection<MyNode> successors = g.getSuccessors(vertex);
                for (MyNode neighbor : successors) {
                    if (!visitedNodeSet.contains(neighbor)) {
                        distanceMap.put(neighbor, distanceMap.get(vertex) + 1);
                        Qu.add(neighbor);
                        maxShortestDistance = distanceMap.get(vertex) + 1;
                    }
                }
                visitedNodeSet.add(vertex);
            }
            start.maxShortestOutDistance = maxShortestDistance;

            // Calculate average path length.
            int totalD = 0;
            int count = 0;
            for (int d : distanceMap.values()) {
                if (d > 0) {
                    totalD += d;
                    count++;
                    if (d > MySequentialGraphVars.diameter) {
                        MySequentialGraphVars.diameter = d;
                    }
                }
            }

            if (count == 0) {
                start.setAverageShortestDistance(0);
            } else {
                start.setAverageShortestDistance((float) totalD / count);
            }

            gTotalD += totalD;
            gCount += count;
            start.setUnreachableNodeCount((MySequentialGraphVars.g.getVertexCount()-1)-count);
        }

        if (gTotalD > 0) {
            double avg = (double) gTotalD / gCount;
            MySequentialGraphVars.avgShortestDistance = avg;
        } else {
            MySequentialGraphVars.avgShortestDistance = 0.0D;
        }    }
    public void setMaxNodeValue() {
        MySequentialGraphVars.g.setMaxNodeValue();}
    public void cleanWork(){ MySequentialGraphVars.g.edRefs = null; }

    public void setConnectedNetworkComponentCountByGraph() {
        MyConnectedNetworkComponentFinder.setConnectedComponentsByGraph();
    }
}