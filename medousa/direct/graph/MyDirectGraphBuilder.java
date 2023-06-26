package medousa.direct.graph;

import medousa.direct.utils.MyDirectGraphSysUtil;
import medousa.direct.utils.MyDirectGraphVars;
import edu.uci.ics.jung.graph.util.EdgeType;

import java.util.ArrayList;

public class MyDirectGraphBuilder {

    private void setEdgeValues() {
        for (int i = 0; i < MyDirectGraphVars.app.getDirectGraphMsgBroker().getDirectConfigPanel().getEdgeValueTable().getRowCount(); i++) {
            if (!MyDirectGraphVars.app.getDirectGraphMsgBroker().getDirectConfigPanel().getEdgeValueTable().getValueAt(i, 0).toString().trim().contains("SELECT") &&
                !MyDirectGraphVars.app.getDirectGraphMsgBroker().getDirectConfigPanel().getEdgeValueTable().getValueAt(i, 1).toString().trim().contains("NO") &&
                !MyDirectGraphVars.app.getDirectGraphMsgBroker().getDirectConfigPanel().getEdgeValueTable().getValueAt(i, 2).toString().trim().contains("SELECT")) {
                String value = MyDirectGraphVars.app.getDirectGraphMsgBroker().getDirectConfigPanel().getEdgeValueTable().getValueAt(i, 0).toString().substring(2);
                String valueType = MyDirectGraphVars.app.getDirectGraphMsgBroker().getDirectConfigPanel().getEdgeValueTable().getValueAt(i, 2).toString().substring(2);
                MyDirectGraphVars.userDefinedEdgeValues.put(value, valueType);

            }
        }
    }

    private void setEdgeLabels() {
        for (int i = 0; i < MyDirectGraphVars.app.getDirectGraphMsgBroker().getDirectConfigPanel().getEdgeNameTable().getRowCount(); i++) {
            if (!MyDirectGraphVars.app.getDirectGraphMsgBroker().getDirectConfigPanel().getEdgeLabelTable().getValueAt(i, 1).toString().trim().contains("NO") &&
                !MyDirectGraphVars.app.getDirectGraphMsgBroker().getDirectConfigPanel().getEdgeLabelTable().getValueAt(i, 2).toString().trim().contains("SELECT")) {
                String value = MyDirectGraphVars.app.getDirectGraphMsgBroker().getDirectConfigPanel().getEdgeNameTable().getValueAt(i, 0).toString().substring(2);
                MyDirectGraphVars.userDefinedEdgeLabesl.add(value);
            }
        }
    }

    private void setNodeValues() {
        for (int i = 0; i < MyDirectGraphVars.app.getDirectGraphMsgBroker().getDirectConfigPanel().getNodeValueTable().getRowCount(); i++) {
            if (!MyDirectGraphVars.app.getDirectGraphMsgBroker().getDirectConfigPanel().getNodeValueTable().getValueAt(i, 1).toString().trim().contains("SELECT") &&
                    !MyDirectGraphVars.app.getDirectGraphMsgBroker().getDirectConfigPanel().getNodeValueTable().getValueAt(i, 2).toString().trim().contains("NO")) {
                String value = MyDirectGraphVars.app.getDirectGraphMsgBroker().getDirectConfigPanel().getNodeValueTable().getValueAt(i, 1).toString().substring(2);
                String valueType = MyDirectGraphVars.app.getDirectGraphMsgBroker().getDirectConfigPanel().getNodeValueTable().getValueAt(i, 3).toString().substring(2);
                MyDirectGraphVars.userDefinedNodeValues.put(value, valueType.replaceAll(" ", "").toUpperCase());
            }
        }
    }

    private void setNodeLabels() {
        for (int i = 0; i < MyDirectGraphVars.app.getDirectGraphMsgBroker().getDirectConfigPanel().getNodeLabelTable().getRowCount(); i++) {
            if (!MyDirectGraphVars.app.getDirectGraphMsgBroker().getDirectConfigPanel().getNodeLabelTable().getValueAt(i, 1).toString().trim().contains("SELECT") &&
                    !MyDirectGraphVars.app.getDirectGraphMsgBroker().getDirectConfigPanel().getNodeLabelTable().getValueAt(i, 2).toString().trim().contains("NO")) {
                String nodeLabel = MyDirectGraphVars.app.getDirectGraphMsgBroker().getDirectConfigPanel().getNodeLabelTable().getValueAt(i, 1).toString().substring(2);
                MyDirectGraphVars.userDefinedNodeLabels.add(nodeLabel);
            }
        }
    }

    public MyDirectGraphBuilder() {
        this.setEdgeValues();
        System.out.println("setEdgeValues() done.");
        this.setEdgeLabels();
        System.out.println("setEdgeLabels() done.");
        this.setNodeValues();
        System.out.println("setNodeValues() done.");
        this.setNodeLabels();
        System.out.println("setNodeLabels() done.");
        MyDirectGraphVars.directGraph = new MyDirectGraph(EdgeType.DIRECTED);
    }

    public MyDirectGraph createDirectGraph(ArrayList<ArrayList<String>> data) {
        try {
            int i = 0;
            for (ArrayList<String> row : data) {
                i++;
                if (row.size() % i == 1000) {System.out.println(i+1);}
                String fromItem = row.get(MyDirectGraphVars.app.getDirectGraphMsgBroker().getHeaderIndex("FROM ITEM"));
                String toItem = row.get(MyDirectGraphVars.app.getDirectGraphMsgBroker().getHeaderIndex("TO ITEM"));
                if (!MyDirectGraphVars.directGraph.vRefs.containsKey(fromItem)) {
                    MyDirectNode n = new MyDirectNode(fromItem);
                    MyDirectGraphVars.directGraph.vRefs.put(fromItem, n);
                    MyDirectGraphVars.directGraph.addVertex(n);
                }
                if (!MyDirectGraphVars.directGraph.vRefs.containsKey(toItem)) {
                    MyDirectNode n = new MyDirectNode(toItem);
                    MyDirectGraphVars.directGraph.vRefs.put(toItem, n);
                    MyDirectGraphVars.directGraph.addVertex(n);
                }
                (MyDirectGraphVars.directGraph.vRefs.get(fromItem)).setContribution(1);
                (MyDirectGraphVars.directGraph.vRefs.get(fromItem)).setOutContribution(1);
                (MyDirectGraphVars.directGraph.vRefs.get(toItem)).setContribution(1);
                (MyDirectGraphVars.directGraph.vRefs.get(toItem)).setInContribution(1);
                this.setEdge(fromItem, toItem, row);
            }
            return MyDirectGraphVars.directGraph;
        } catch(Exception ex) {ex.printStackTrace();}
        return null;
    }

    private void setEdge(String predecessor, String successor, ArrayList<String> row) {
        String edgeRef = predecessor + "-" + successor;
        try {
            if (!MyDirectGraphVars.directGraph.directGraphEdgeRefMap.containsKey(edgeRef)) {
                MyDirectNode source = MyDirectGraphVars.directGraph.vRefs.get(predecessor);
                MyDirectNode dest = MyDirectGraphVars.directGraph.vRefs.get(successor);
                MyDirectEdge edge = new MyDirectEdge(source, dest);
                MyDirectGraphVars.directGraph.addEdge(edge, source, dest);
                MyDirectGraphVars.directGraph.directGraphEdgeRefMap.put(edgeRef, edge);
            }
            MyDirectEdge edge = ((MyDirectEdge) MyDirectGraphVars.directGraph.directGraphEdgeRefMap.get(edgeRef));
            edge.setContribution(1);
            this.addEdgeValues(edge, row);
            this.addEdgeLabels(edge, row);
        } catch (Exception ex) {ex.printStackTrace();}
    }

    private void addEdgeValues(MyDirectEdge edge, ArrayList<String> row) {
        try {
            for (String numericEdgeValue : MyDirectGraphVars.userDefinedEdgeValues.keySet()) {
                int edgeValueHeaderIdx = MyDirectGraphVars.app.getDirectGraphMsgBroker().getHeaderIndex(numericEdgeValue);
                float value = Float.valueOf(row.get(edgeValueHeaderIdx));
                edge.addEdgeValue(numericEdgeValue, value);
            }
        } catch (Exception ex) {ex.printStackTrace();}
    }

    private void addEdgeLabels(MyDirectEdge edge, ArrayList<String> row) {
        try {
            for (String edgeLabel : MyDirectGraphVars.userDefinedEdgeLabesl) {
                int edgeLabelHeaderIdx = MyDirectGraphVars.app.getDirectGraphMsgBroker().getHeaderIndex(edgeLabel);
                edge.addEdgeLabel(edgeLabel, row.get(edgeLabelHeaderIdx));
            }
        } catch (Exception ex) {ex.printStackTrace();}
    }

    public boolean addNodeValues() {
        try {
            if (MyDirectGraphVars.userDefinedNodeValues.size() > 0) {
                ArrayList<ArrayList<String>> data = MyDirectGraphVars.app.getDirectGraphMsgBroker().getDirectConfigPanel().getNodeValueData();
                int itemIdx = MyDirectGraphVars.app.getDirectGraphMsgBroker().getDirectConfigPanel().getNodeValueHeaderIndex("ITEM");
                for (ArrayList<String> row : data) {
                    for (String variable : MyDirectGraphVars.userDefinedNodeValues.keySet()) {
                        int valueIdx = MyDirectGraphVars.app.getDirectGraphMsgBroker().getDirectConfigPanel().getNodeValueHeaderIndex(variable);
                        MyDirectNode n = (MyDirectNode) MyDirectGraphVars.directGraph.vRefs.get(row.get(itemIdx));
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
            if (MyDirectGraphVars.userDefinedNodeLabels.size() > 0) {
                ArrayList<ArrayList<String>> data = MyDirectGraphVars.app.getDirectGraphMsgBroker().getDirectConfigPanel().getNodeValueData();
                int itemIdx = MyDirectGraphVars.app.getDirectGraphMsgBroker().getDirectConfigPanel().getNodeValueHeaderIndex("ITEM");
                for (ArrayList<String> row : data) {
                    MyDirectNode n = (MyDirectNode) MyDirectGraphVars.directGraph.vRefs.get(row.get(itemIdx));
                    for (String label : MyDirectGraphVars.userDefinedNodeLabels) {
                        int valueIdx = MyDirectGraphVars.app.getDirectGraphMsgBroker().getDirectConfigPanel().getNodeValueHeaderIndex(label);
                        n.addNodeLabel(label, row.get(valueIdx));
                    }
                }
            }
        } catch (Exception ex) {ex.printStackTrace();}
    }
    public void setAverageShortestOutDistance(MyDirectGraph<MyDirectNode, MyDirectEdge> g) {
        MyDirectGraphSysUtil.setAverageShortestOutDistance(g);
    }
    public void setAverageShortestInDistance(MyDirectGraph<MyDirectNode, MyDirectEdge> g) {
        MyDirectGraphSysUtil.setAverageShortestInDistance(g);}

}