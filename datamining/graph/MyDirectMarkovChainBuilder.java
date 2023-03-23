package datamining.graph;

import datamining.utils.system.MySysUtil;
import datamining.utils.system.MyVars;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;

import java.util.ArrayList;

public class MyDirectMarkovChainBuilder {

    private void setEdgeValues() {
        for (int i = 0; i < MyVars.main.getMsgBroker().getDirectConfigPanel().getEdgeValueTable().getRowCount(); i++) {
            if (!MyVars.main.getMsgBroker().getDirectConfigPanel().getEdgeValueTable().getValueAt(i, 0).toString().trim().contains("SELECT") &&
                !MyVars.main.getMsgBroker().getDirectConfigPanel().getEdgeValueTable().getValueAt(i, 1).toString().trim().contains("NO") &&
                !MyVars.main.getMsgBroker().getDirectConfigPanel().getEdgeValueTable().getValueAt(i, 2).toString().trim().contains("SELECT")) {
                String value = MyVars.main.getMsgBroker().getDirectConfigPanel().getEdgeValueTable().getValueAt(i, 0).toString().substring(2);
                String valueType = MyVars.main.getMsgBroker().getDirectConfigPanel().getEdgeValueTable().getValueAt(i, 2).toString().substring(2);
                MyVars.edgeValues.put(value, valueType);

            }
        }
    }

    private void setEdgeLabels() {
        for (int i = 0; i < MyVars.main.getMsgBroker().getDirectConfigPanel().getEdgeNameTable().getRowCount(); i++) {
            if (!MyVars.main.getMsgBroker().getDirectConfigPanel().getEdgeLabelTable().getValueAt(i, 1).toString().trim().contains("NO") &&
                !MyVars.main.getMsgBroker().getDirectConfigPanel().getEdgeLabelTable().getValueAt(i, 2).toString().trim().contains("SELECT")) {
                String value = MyVars.main.getMsgBroker().getDirectConfigPanel().getEdgeNameTable().getValueAt(i, 0).toString().substring(2);
                MyVars.edgeLabels.add(value);
            }
        }
    }

    private void setNodeValues() {
        for (int i = 0; i < MyVars.main.getMsgBroker().getDirectConfigPanel().getNodeValueTable().getRowCount(); i++) {
            if (!MyVars.main.getMsgBroker().getDirectConfigPanel().getNodeValueTable().getValueAt(i, 1).toString().trim().contains("SELECT") &&
                    !MyVars.main.getMsgBroker().getDirectConfigPanel().getNodeValueTable().getValueAt(i, 2).toString().trim().contains("NO")) {
                String value = MyVars.main.getMsgBroker().getDirectConfigPanel().getNodeValueTable().getValueAt(i, 1).toString().substring(2);
                String valueType = MyVars.main.getMsgBroker().getDirectConfigPanel().getNodeValueTable().getValueAt(i, 3).toString().substring(2);
                MyVars.nodeValues.put(value, valueType.replaceAll(" ", "").toUpperCase());
            }
        }
    }

    private void setNodeLabels() {
        for (int i = 0; i < MyVars.main.getMsgBroker().getDirectConfigPanel().getNodeLabelTable().getRowCount(); i++) {
            if (!MyVars.main.getMsgBroker().getDirectConfigPanel().getNodeLabelTable().getValueAt(i, 1).toString().trim().contains("SELECT") &&
                    !MyVars.main.getMsgBroker().getDirectConfigPanel().getNodeLabelTable().getValueAt(i, 2).toString().trim().contains("NO")) {
                String nodeLabel = MyVars.main.getMsgBroker().getDirectConfigPanel().getNodeLabelTable().getValueAt(i, 1).toString().substring(2);
                MyVars.nodeLabels.add(nodeLabel);
            }
        }
    }

    public MyDirectMarkovChainBuilder() {
        this.setEdgeValues();
        this.setEdgeLabels();
        this.setNodeValues();
        this.setNodeLabels();
        MyVars.directMarkovChain = new MyDirectMarkovChain(EdgeType.DIRECTED);
    }

    public MyDirectMarkovChain createDirectMarkovChain(ArrayList<ArrayList<String>> data) {
        try {
            for (ArrayList<String> row : data) {
                String fromItem = row.get(MyVars.main.getMsgBroker().getHeaderIndex("FROM ITEM"));
                String toItem = row.get(MyVars.main.getMsgBroker().getHeaderIndex("TO ITEM"));
                if (!MyVars.directMarkovChain.vRefs.containsKey(fromItem)) {
                    MyDirectNode n = new MyDirectNode(fromItem);
                    MyVars.directMarkovChain.vRefs.put(fromItem, n);
                    MyVars.directMarkovChain.addVertex(n);
                }
                if (!MyVars.directMarkovChain.vRefs.containsKey(toItem)) {
                    MyDirectNode n = new MyDirectNode(toItem);
                    MyVars.directMarkovChain.vRefs.put(toItem, n);
                    MyVars.directMarkovChain.addVertex(n);
                }
                ((MyDirectNode)MyVars.directMarkovChain.vRefs.get(fromItem)).setContribution(1);
                ((MyDirectNode)MyVars.directMarkovChain.vRefs.get(fromItem)).setOutContribution(1);
                ((MyDirectNode)MyVars.directMarkovChain.vRefs.get(toItem)).setContribution(1);
                ((MyDirectNode)MyVars.directMarkovChain.vRefs.get(toItem)).setInContribution(1);
                this.setEdge(fromItem, toItem, row);
            }
            return MyVars.directMarkovChain;
        } catch(Exception ex) {ex.printStackTrace();}
        return null;
    }

    private void setEdge(String predecessor, String successor, ArrayList<String> row) {
        String edgeRef = predecessor + "-" + successor;
        try {
            if (!MyVars.directMarkovChain.directMarkovChainEdgeRefMap.containsKey(edgeRef)) {
                MyDirectNode source = (MyDirectNode)MyVars.directMarkovChain.vRefs.get(predecessor);
                MyDirectNode dest = (MyDirectNode)MyVars.directMarkovChain.vRefs.get(successor);
                MyDirectEdge edge = new MyDirectEdge(source, dest);
                MyVars.directMarkovChain.addEdge(edge, source, dest);
                MyVars.directMarkovChain.directMarkovChainEdgeRefMap.put(edgeRef, edge);
            }
            MyDirectEdge edge = ((MyDirectEdge)MyVars.directMarkovChain.directMarkovChainEdgeRefMap.get(edgeRef));
            edge.setContribution(1);
            this.addEdgeValues(edge, row);
            this.addEdgeLabels(edge, row);
        } catch (Exception ex) {ex.printStackTrace();}
    }

    private void addEdgeValues(MyDirectEdge edge, ArrayList<String> row) {
        try {
            for (String numericEdgeValue : MyVars.edgeValues.keySet()) {
                int edgeValueHeaderIdx = MyVars.main.getMsgBroker().getHeaderIndex(numericEdgeValue);
                float value = Float.valueOf(row.get(edgeValueHeaderIdx));
                edge.addEdgeValue(numericEdgeValue, value);
            }
        } catch (Exception ex) {ex.printStackTrace();}
    }

    private void addEdgeLabels(MyDirectEdge edge, ArrayList<String> row) {
        try {
            for (String edgeLabel : MyVars.edgeLabels) {
                int edgeLabelHeaderIdx = MyVars.main.getMsgBroker().getHeaderIndex(edgeLabel);
                edge.addEdgeLabel(edgeLabel, row.get(edgeLabelHeaderIdx));
            }
        } catch (Exception ex) {ex.printStackTrace();}
    }

    public boolean addNodeValues() {
        try {
            if (MyVars.nodeValues.size() > 0) {
                ArrayList<ArrayList<String>> data = MyVars.main.getMsgBroker().getDirectConfigPanel().getNodeValueData();
                int itemIdx = MyVars.main.getMsgBroker().getDirectConfigPanel().getNodeValueHeaderIndex("ITEM");
                for (ArrayList<String> row : data) {
                    for (String variable : MyVars.nodeValues.keySet()) {
                        int valueIdx = MyVars.main.getMsgBroker().getDirectConfigPanel().getNodeValueHeaderIndex(variable);
                        MyDirectNode n = (MyDirectNode) MyVars.directMarkovChain.vRefs.get(row.get(itemIdx));
                        n.addNodeValue(variable, Float.valueOf(row.get(valueIdx)));
                    }
                }
            }
        }  catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public void addNodeLabels() {
        try {
            if (MyVars.nodeLabels.size() > 0) {
                ArrayList<ArrayList<String>> data = MyVars.main.getMsgBroker().getDirectConfigPanel().getNodeValueData();
                int itemIdx = MyVars.main.getMsgBroker().getDirectConfigPanel().getNodeValueHeaderIndex("ITEM");
                for (ArrayList<String> row : data) {
                    MyDirectNode n = (MyDirectNode) MyVars.directMarkovChain.vRefs.get(row.get(itemIdx));
                    for (String label : MyVars.nodeLabels) {
                        int valueIdx = MyVars.main.getMsgBroker().getDirectConfigPanel().getNodeValueHeaderIndex(label);
                        n.addNodeLabel(label, row.get(valueIdx));
                    }
                }
            }
        } catch (Exception ex) {ex.printStackTrace();}
    }
    public void setAverageUnWeightedDirectGraphShortestPathLength(Graph g) {
        MySysUtil.setAverageUnWeightedDirectGraphShortestPathLength(g);
    }

}