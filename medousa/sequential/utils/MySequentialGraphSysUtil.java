package medousa.sequential.utils;

import medousa.message.MyMessageUtil;
import medousa.security.MyOSMonitor;
import medousa.sequential.feature.MyVariableMap;
import medousa.sequential.graph.MyGraph;
import medousa.MyProgressBar;
import medousa.sequential.graph.MyEdge;
import medousa.sequential.graph.MyNode;

import java.io.*;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class MySequentialGraphSysUtil {

    public static LinkedHashMap<String, Long> sortMapByLongValue(Map<String, Long> map) {
        List<Map.Entry<String, Long>> entries = new LinkedList<>(map.entrySet());
        Collections.sort(entries, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        LinkedHashMap<String, Long> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Long> entry : entries) {sortedMap.put(entry.getKey(), entry.getValue());}
        return sortedMap;
    }

    public static LinkedHashMap<String, Float> sortMapByFloatValue(Map<String, Float> map) {
        List<Map.Entry<String, Float>> entries = new LinkedList<>(map.entrySet());
        Collections.sort(entries, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        LinkedHashMap<String, Float> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Float> entry : entries) {sortedMap.put(entry.getKey(), entry.getValue());}
        return sortedMap;
    }

    public static double getAverageMultiNodeShortestPathLength() {
        double totalAverageLength = 0D;
        for (MyNode n : MySequentialGraphVars.getSequentialGraphViewer().multiNodes) {
            totalAverageLength += n.getAverageShortestDistance();
        }
        return totalAverageLength/ MySequentialGraphVars.getSequentialGraphViewer().multiNodes.size();
    }

    public static double getAverageShortestDistance() {
        double totalAverageLength = 0D;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            totalAverageLength += n.getAverageShortestDistance();
        }
        return totalAverageLength/nodes.size();
    }

    public static String getDirectorySlash() {
        return (MyOSMonitor.isWindows() ? "\\" : "//");
    }

    public static String getDecodedNodeName(String encodedNodeName) {
        return (encodedNodeName.contains("x") ? getDecodeVariableNodeName(encodedNodeName) : decodeNodeName(encodedNodeName));
    }
    public static String formatAverageValue(String avgrageValueStr) {
        String avgValueHeader = "";
        String[] avgValues = null;
        String result = "";
        try {
            avgValues = avgrageValueStr.split("\\.");
            if (avgValues[0].indexOf(',') == -1) {
                avgValueHeader = MyMathUtil.getCommaSeperatedNumber(Long.parseLong(avgValues[0]));
            }
            result = avgValueHeader + "." + avgValues[1];
        } catch (NumberFormatException n) {
            return "0.00";
        } catch (ArrayIndexOutOfBoundsException a) {result = avgValueHeader;}
        return result;
    }

    public static void sleepExecution(int sleepTime) {
        try {Thread.sleep(sleepTime);
        } catch (Exception ex) {ex.printStackTrace();}
    }

    public static String decodeNodeName(String nodeName) {
        if (nodeName.contains("x")) {
            String decodedVariableSet = "";
            for (String name : nodeName.split(MySequentialGraphVars.commaDelimeter)) {
                if (decodedVariableSet.length() == 0) {decodedVariableSet = decodeVariableSet(name);
                } else {decodedVariableSet += "," + decodeVariableSet(name);}
            }
            return decodedVariableSet;
        } else {
            String decodedItemSet = "";
            for (String name : nodeName.split(MySequentialGraphVars.commaDelimeter)) {
                if (decodedItemSet.length() == 0) {decodedItemSet = decodeItemSet(name);
                } else {decodedItemSet += "," + decodeItemSet(name);}
            }
            return decodedItemSet;
        }
    }

    public static String decodeItemSet(String itemset) {
        String decodedItemSet = "";
        try {
            String item = "" + itemset.charAt(0);
            for (int i = 1; i < itemset.length(); i++) {
                if (Character.isUpperCase(itemset.charAt(i))) {
                    if (decodedItemSet.length() == 0) {decodedItemSet = MySequentialGraphVars.itemToIdMap.get(item);
                    } else {decodedItemSet += MySequentialGraphVars.commaDelimeter + MySequentialGraphVars.itemToIdMap.get(item);}
                    item = "" + itemset.charAt(i);
                } else {item += itemset.charAt(i);}
            }
            if (decodedItemSet.length() == 0) {decodedItemSet = MySequentialGraphVars.itemToIdMap.get(item);
            } else {decodedItemSet += MySequentialGraphVars.commaDelimeter + MySequentialGraphVars.itemToIdMap.get(item);}
            return decodedItemSet;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String decodeVariableSet(String variableItemSet) {
        String decodedItemSet = "";
        String variableItem = "" + variableItemSet.charAt(0);
        for (int i = 1; i < variableItemSet.length(); i++) {
            if (variableItemSet.charAt(i) == 'x') {
                variableItem += variableItemSet.charAt(i);
                if (decodedItemSet.length() == 0) {decodedItemSet = MySequentialGraphVars.variableToIdMap.get(variableItem);
                } else {decodedItemSet += MySequentialGraphVars.commaDelimeter + MySequentialGraphVars.variableToIdMap.get(variableItem);}
                variableItem = "";
            } else {variableItem += variableItemSet.charAt(i);}
        }
        return decodedItemSet;
    }

    public static String getDecodeVariableNodeName(String variables) {
        String [] variableItems = variables.split(",");
        String decryptedVariable = "";
        for (String encryptedVariable : variableItems) {
            if (decryptedVariable.length() == 0) {
                decryptedVariable = MySequentialGraphVars.variableToIdMap.get(encryptedVariable);
            } else {
                decryptedVariable += "," + MySequentialGraphVars.variableToIdMap.get(encryptedVariable);
            }
        }
        return decryptedVariable;
    }

    public static String getCommaSeperateString(long n) {
        return NumberFormat.getNumberInstance(Locale.US).format(n);
    }

    public static File[] getFileList(String dir) {
        File outputDir = new File(dir);
        if (!outputDir.exists()) {
            MyMessageUtil.showErrorMsg("The provided directory does not exist.");
            return null;
        }
        return new File(dir).listFiles();
    }




    public static String encodeVariableItemSet(String itemset) {
        String encodedVariableItemset = "";
        String[] items = itemset.split(MySequentialGraphVars.commaDelimeter);
        for (int i = 0; i < items.length; i++) {
            for (Map.Entry<String, String> entry : MySequentialGraphVars.variableToIdMap.entrySet()) {
                if (Objects.equals(entry.getValue(), items[i])) {
                    if (encodedVariableItemset.length() == 0) {encodedVariableItemset = entry.getKey();
                    } else {encodedVariableItemset += "," + entry.getKey();}
                    break;
                }
            }
        }
        return encodedVariableItemset;
    }

    public static String encodeItemSet(String itemset) {
        String encodedItemset = "";
        if (itemset.contains("x")) {
            encodedItemset = encodeVariableItemSet(itemset);
        } else {
            String [] items = itemset.split(MySequentialGraphVars.commaDelimeter);
            for (int i = 0; i < items.length; i++) {
                for (Map.Entry<String, String> entry : MySequentialGraphVars.itemToIdMap.entrySet()) {
                    if (Objects.equals(entry.getValue(), items[i])) {
                        if (encodedItemset.length() == 0) {
                            encodedItemset = entry.getKey();
                        } else {
                            encodedItemset += "," + entry.getKey();
                        }
                        break;
                    }
                }
            }
        }
        return encodedItemset;
    }

    public static int getViewerWidth() {
        return MySequentialGraphVars.app.getWidth();
    }

    public static int getViewerHeight() {
        return MySequentialGraphVars.app.getHeight();
    }

    public static String getTimeDifference(String fromDate, String toDate) {
        try {
            if (fromDate.indexOf(' ') == -1) {fromDate = fromDate + " " + "00:00:00";}
            if (toDate.indexOf(' ') == -1) {toDate = toDate + " " + "00:00:00";}
            LocalDateTime d1 = LocalDateTime.parse(fromDate, DateTimeFormatter.ofPattern(MySequentialGraphVars.DATE_TIME_FORMAT));
            LocalDateTime d2 = LocalDateTime.parse(toDate, DateTimeFormatter.ofPattern(MySequentialGraphVars.DATE_TIME_FORMAT));
            Duration d = Duration.between(d1, d2);
            long diff = d.getSeconds();
            return String.valueOf(Math.abs(diff));
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public static void initVariables() {
        MySequentialGraphVars.g = null;

        MySequentialGraphVars.outputDir = "";
        MySequentialGraphVars.isTimeOn = false;
        MySequentialGraphVars.isSupplementaryOn = false;
        MySequentialGraphVars.seqs = null;
        MySequentialGraphVars.mxDepth = 0;
        MySequentialGraphVars.totalRecords = 0;
        MySequentialGraphVars.totalOutputSize = 0;
        MySequentialGraphVars.sequeceFeatureCount = 0;
        MySequentialGraphVars.sequenceWithObjectIDFileName = "";
        MySequentialGraphVars.sequenceFileName = "";
        MySequentialGraphVars.isDirectMarkovRelation = false;
        MySequentialGraphVars.edgeOrderByComboBoxIdx = -1;
        MySequentialGraphVars.nodeOrderByComboBoxIdx = -1;
        MySequentialGraphVars.currentGraphDepth = 0;
        MySequentialGraphVars.itemToIdMap = null;

        MySequentialGraphVars.userDefinedNodeLabelSet = new HashSet<>();
        MySequentialGraphVars.userDefinedNodeValueMap = new HashMap<>();
        MySequentialGraphVars.userDefinedEdgeLabelSet = new HashSet<>();
        MySequentialGraphVars.userDefinedEdgeValueMap = new HashMap<>();
        MySequentialGraphVars.variableToIdMap = new MyVariableMap();
        MySequentialGraphVars.nodeNameMap = new HashMap<>();
        MySequentialGraphVars.pathLengthByDepthMap = new HashMap<>();
    }

    public static void setNodeNameMap() {
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            MySequentialGraphVars.nodeNameMap.put(getDecodedNodeName(n.getName()), n.getName());
        }
    }

    public static void setSharedPredecessors(MyNode a, MyNode b) {
        synchronized (MySequentialGraphVars.getSequentialGraphViewer().sharedPredecessors) {
            MySequentialGraphVars.getSequentialGraphViewer().sharedPredecessors.addAll(MySequentialGraphVars.g.getPredecessors(a));
            MySequentialGraphVars.getSequentialGraphViewer().sharedPredecessors.retainAll(MySequentialGraphVars.g.getPredecessors(b));
        }
    }

    public static void setSharedPredecessors(MyNode a) {
        synchronized (MySequentialGraphVars.getSequentialGraphViewer().sharedPredecessors) {
            MySequentialGraphVars.getSequentialGraphViewer().sharedPredecessors.retainAll(MySequentialGraphVars.g.getPredecessors(a));
        }
    }

    public static void setSharedSuccessors(MyNode a, MyNode b) {
        synchronized (MySequentialGraphVars.getSequentialGraphViewer().sharedSuccessors) {
            MySequentialGraphVars.getSequentialGraphViewer().sharedSuccessors.addAll(MySequentialGraphVars.g.getSuccessors(a));
            MySequentialGraphVars.getSequentialGraphViewer().sharedSuccessors.retainAll(MySequentialGraphVars.g.getSuccessors(b));
        }
    }

    public static void setSharedSuccessors(MyNode a) {
        synchronized (MySequentialGraphVars.getSequentialGraphViewer().sharedSuccessors) {
            MySequentialGraphVars.getSequentialGraphViewer().sharedSuccessors.retainAll(MySequentialGraphVars.g.getSuccessors(a));
        }
    }

    public static String getWorkingDir() {
        return System.getProperty("user.dir");
    }

    public static void setAverageShortestDistance(MyGraph<MyNode, MyEdge> g) {
        StringBuilder b =  new StringBuilder();
        long gTotalL = 0L;
        long gCount = 0L;
        Collection<MyNode> nodes = g.getVertices();
        for (MyNode start : nodes) {
            Queue<MyNode> Qu =  new LinkedList<>();
            Set<MyNode> visited = new HashSet();
            Map<MyNode, Integer> found =  new HashMap();

            Qu.add(start);
            found.put(start, 0);
            while (!Qu.isEmpty()) {
                MyNode vertex = Qu.remove();
                Collection<MyNode> successors = g.getSuccessors(vertex);
                for (MyNode neighbor : successors) {
                    if (!found.keySet().contains(neighbor) && !visited.contains(neighbor)) {
                        found.put(neighbor, found.get(vertex) + 1);
                        Qu.add(neighbor);
                    }
                }
                //b.append(vertex.toString() + " ");
                visited.add(vertex);
            }

            // Calculate average path length.
            int totalL = 0;
            int count = 0;
            for (Integer l : found.values()) {
                if (l > 0) {
                    totalL += l;
                    count++;
                    if (l > MySequentialGraphVars.diameter) {
                        MySequentialGraphVars.diameter = l;
                    }
                }
            }

            if (count == 0) {
                start.setAverageShortestDistance(0);
            } else {
                start.setAverageShortestDistance((float) totalL / count);
            }

            gTotalL += totalL;
            gCount += count;
            start.setUnreachableNodeCount((MySequentialGraphVars.g.getVertexCount()-1)-count);
        }

        if (gTotalL > 0) {
            double avg = (double) gTotalL / gCount;
            MySequentialGraphVars.avgShortestPathLen = avg;
        } else {
            MySequentialGraphVars.avgShortestPathLen = 0.0D;
        }
    }

    public static String getNodeName(String encodedNodeName) {
        if (encodedNodeName.contains("x")) {
            return getDecodeVariableNodeName(encodedNodeName);
        } else {
            return getDecodedNodeName(encodedNodeName);
        }
    }

}
