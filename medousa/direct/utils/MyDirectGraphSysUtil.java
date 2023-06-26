package medousa.direct.utils;

import medousa.direct.feature.MyDirectGraphVariableMap;
import medousa.direct.graph.MyDirectEdge;
import medousa.direct.graph.MyDirectGraph;
import medousa.direct.graph.MyDirectNode;
import medousa.security.MyDirectGraphOSMonitor;
import medousa.message.MyMessageUtil;
import edu.uci.ics.jung.algorithms.cluster.WeakComponentClusterer;

import java.awt.*;
import java.io.*;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class MyDirectGraphSysUtil {

    public static LinkedHashMap<MyDirectNode, Long> sortNodeMapByLongValue(Map<MyDirectNode, Long> map) {
        List<Map.Entry<MyDirectNode, Long>> entries = new LinkedList<>(map.entrySet());
        Collections.sort(entries, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        LinkedHashMap<MyDirectNode, Long> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<MyDirectNode, Long> entry : entries) {sortedMap.put(entry.getKey(), entry.getValue());}
        return sortedMap;
    }

    public static LinkedHashMap<String, Long> sortMapByLongValue(Map<String, Long> map) {
        List<Map.Entry<String, Long>> entries = new LinkedList<>(map.entrySet());
        Collections.sort(entries, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        LinkedHashMap<String, Long> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Long> entry : entries) {sortedMap.put(entry.getKey(), entry.getValue());}
        return sortedMap;
    }

    public static LinkedHashMap<Long, Long> sortMapByLongKeyByLongValue(Map<Long, Long> map) {
        List<Map.Entry<Long, Long>> entries = new LinkedList<>(map.entrySet());
        Collections.sort(entries, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        LinkedHashMap<Long, Long> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<Long, Long> entry : entries) {sortedMap.put(entry.getKey(), entry.getValue());}
        return sortedMap;
    }

    public static LinkedHashMap<String, Float> sortMapByFloatValue(Map<String, Float> map) {
        List<Map.Entry<String, Float>> entries = new LinkedList<>(map.entrySet());
        Collections.sort(entries, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        LinkedHashMap<String, Float> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Float> entry : entries) {sortedMap.put(entry.getKey(), entry.getValue());}
        return sortedMap;
    }

    public static LinkedHashMap<String, Float> invertMap(Map<String, Float> map) {
        LinkedHashMap<String, Float> inversedMap = new LinkedHashMap<String, Float>();
        for (Map.Entry<String, Float> entry : map.entrySet()) {
            inversedMap.put(entry.getKey(), entry.getValue());
        }
        return inversedMap;
    }


    public static void loadVariablesAndValues() {
        try {
            String variablesAndValuesFileName = "." + MyDirectGraphSysUtil.getDirectorySlash() + ".variablesAndValues.txt";
            File variableAndValueFile = new File(variablesAndValuesFileName);
            if (!variableAndValueFile.exists()) {
                MyMessageUtil.showErrorMsg("The application property file does not exist.");
            } else {
                BufferedReader bw = new BufferedReader(new FileReader(variableAndValueFile));
                MyDirectGraphVars.sequeceFeatureCount = Integer.valueOf(bw.readLine());
                MyDirectGraphVars.totalOutputSize = Long.valueOf(bw.readLine());
                MyDirectGraphVars.globalPatternCount = Long.valueOf(bw.readLine());
                MyDirectGraphVars.mxDepth = Integer.valueOf(bw.readLine());
                MyDirectGraphVars.totalRecords = Integer.valueOf(bw.readLine());
                MyDirectGraphVars.outputDir = bw.readLine();
                bw.readLine();
                String value = "";
                MyDirectGraphVars.itemToIdMap = new HashMap<>();

                while ((value = bw.readLine()).length() > 0) {
                    String[] keyValues = value.split("=");
                    MyDirectGraphVars.itemToIdMap.put(keyValues[0], keyValues[1]);
                }

                while ((value = bw.readLine()).length() > 0) {
                    String[] keyValues = value.split("=");
                    MyDirectGraphVars.variableToIdMap.put(keyValues[0], keyValues[1]);
                }

                bw.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static String getDirectorySlash() {
        return (MyDirectGraphOSMonitor.isWindows() ? "\\" : "//");
    }

    public static String getDecodedNodeName(String encodedNodeName) {
        return (encodedNodeName.contains("x") ? decodeVariable(encodedNodeName) : decodeNodeName(encodedNodeName));
    }
    public static String formatAverageValue(String avgrageValueStr) {
        String avgValueHeader = "";
        String[] avgValues = null;
        String result = "";
        try {
            avgValues = avgrageValueStr.split("\\.");
            if (avgValues[0].indexOf(',') == -1) {
                avgValueHeader = MyDirectGraphMathUtil.getCommaSeperatedNumber(Long.parseLong(avgValues[0]));
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
            for (String name : nodeName.split(MyDirectGraphVars.commaDelimeter)) {
                if (decodedVariableSet.length() == 0) {decodedVariableSet = decodeVariableSet(name);
                } else {decodedVariableSet += "," + decodeVariableSet(name);}
            }
            return decodedVariableSet;
        } else {
            String decodedItemSet = "";
            for (String name : nodeName.split(MyDirectGraphVars.commaDelimeter)) {
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
                    if (decodedItemSet.length() == 0) {decodedItemSet = MyDirectGraphVars.itemToIdMap.get(item);
                    } else {decodedItemSet += MyDirectGraphVars.commaDelimeter + MyDirectGraphVars.itemToIdMap.get(item);}
                    item = "" + itemset.charAt(i);
                } else {item += itemset.charAt(i);}
            }
            if (decodedItemSet.length() == 0) {decodedItemSet = MyDirectGraphVars.itemToIdMap.get(item);
            } else {decodedItemSet += MyDirectGraphVars.commaDelimeter + MyDirectGraphVars.itemToIdMap.get(item);}
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
                if (decodedItemSet.length() == 0) {decodedItemSet = MyDirectGraphVars.variableToIdMap.get(variableItem);
                } else {decodedItemSet += MyDirectGraphVars.commaDelimeter + MyDirectGraphVars.variableToIdMap.get(variableItem);}
                variableItem = "";
            } else {variableItem += variableItemSet.charAt(i);}
        }
        return decodedItemSet;
    }

    public static String decodeVariable(String variables) {
        String [] variableItems = variables.split(",");
        String decryptedVariable = "";
        for (String encryptedVariable : variableItems) {
            if (decryptedVariable.length() == 0) {
                decryptedVariable = MyDirectGraphVars.variableToIdMap.get(encryptedVariable);
            } else {
                decryptedVariable += "," + MyDirectGraphVars.variableToIdMap.get(encryptedVariable);
            }
        }
        return decryptedVariable;
    }

    public static String getCommaSeperateString(long n) {
        return NumberFormat.getNumberInstance(Locale.US).format(n);
    }
    public static String encodeVariableItemSet(String itemset) {
        String encodedVariableItemset = "";
        String[] items = itemset.split(MyDirectGraphVars.commaDelimeter);
        for (int i = 0; i < items.length; i++) {
            for (Map.Entry<String, String> entry : MyDirectGraphVars.variableToIdMap.entrySet()) {
                if (Objects.equals(entry.getValue(), items[i])) {
                    if (encodedVariableItemset.length() == 0) {encodedVariableItemset = entry.getKey();
                    } else {encodedVariableItemset += "," + entry.getKey();}
                    break;
                }
            }
        }
        return encodedVariableItemset;
    }

    public static int getViewerWidth() {
        return MyDirectGraphVars.app.getWidth();
    }

    public static int getViewerHeight() {
        return MyDirectGraphVars.app.getHeight();
    }

    public static String getTimeDifference(String fromDate, String toDate) {
        try {
            if (fromDate.indexOf(' ') == -1) {fromDate = fromDate + " " + "00:00:00";}
            if (toDate.indexOf(' ') == -1) {toDate = toDate + " " + "00:00:00";}
            LocalDateTime d1 = LocalDateTime.parse(fromDate, DateTimeFormatter.ofPattern(MyDirectGraphVars.DATE_TIME_FORMAT));
            LocalDateTime d2 = LocalDateTime.parse(toDate, DateTimeFormatter.ofPattern(MyDirectGraphVars.DATE_TIME_FORMAT));
            Duration d = Duration.between(d1, d2);
            long diff = d.getSeconds();
            return String.valueOf(Math.abs(diff));
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public static void initVariables() {
        MyDirectGraphVars.directGraph = null;

        MyDirectGraphVars.outputDir = "";
        MyDirectGraphVars.isTimeOn = false;
        MyDirectGraphVars.isSupplementaryOn = false;
        MyDirectGraphVars.seqs = null;
        MyDirectGraphVars.mxDepth = 0;
        MyDirectGraphVars.diameter = 0;
        MyDirectGraphVars.totalRecords = 0;
        MyDirectGraphVars.totalOutputSize = 0;
        MyDirectGraphVars.sequeceFeatureCount = 0;
        MyDirectGraphVars.avgShortestOutDistance = 0;
        MyDirectGraphVars.sequenceWithObjectIDFileName = "";
        MyDirectGraphVars.sequenceFileName = "";
        MyDirectGraphVars.currentGraphDepth = 0;
        MyDirectGraphVars.itemToIdMap = null;

        MyDirectGraphVars.userDefinedNodeLabels = new HashSet<>();
        MyDirectGraphVars.userDefinedNodeValues = new HashMap<>();
        MyDirectGraphVars.userDefinedEdgeLabesl = new HashSet<>();
        MyDirectGraphVars.userDefinedEdgeValues = new HashMap<>();
        MyDirectGraphVars.variableToIdMap = new MyDirectGraphVariableMap();
        MyDirectGraphVars.pathLengthByDepthMap = new HashMap<>();
    }

    public static String getWorkingDir() {
        return System.getProperty("user.dir");
    }

    public static Set<Set<MyDirectNode>> getGraphs() {
        WeakComponentClusterer<MyDirectNode, MyDirectEdge> wcSearch = new WeakComponentClusterer<>();
        Set<Set<MyDirectNode>> clusters = wcSearch.transform(MyDirectGraphVars.directGraph);
        return clusters;
    }

    public static void setAverageShortestOutDistance(MyDirectGraph<MyDirectNode, MyDirectEdge> g) {
        StringBuilder b =  new StringBuilder();
        int maxDistance = 0;
        int totalNodeCount = (int) MyDirectGraphVars.directGraph.getGraphNodeCount();
        Collection<MyDirectNode> nodes = g.getVertices();
        for (MyDirectNode source : nodes) {
            if (source.getCurrentValue() <= 0) continue;
            Queue<MyDirectNode> Qu =  new LinkedList<>();
            Set<MyDirectNode> visited = new HashSet();
            Map<MyDirectNode, Integer> distanceMap =  new HashMap();

            Qu.add(source);
            distanceMap.put(source, 0);
            while (!Qu.isEmpty()) {
                MyDirectNode node = Qu.remove();
                Collection<MyDirectNode> successors = g.getSuccessors(node);
                for (MyDirectNode neighbor : successors) {
                    if (neighbor.getCurrentValue() <= 0) continue;
                    if (!visited.contains(neighbor)) {
                        int distance = distanceMap.get(node) + 1;
                        if (distance > maxDistance) maxDistance = distance;
                        distanceMap.put(neighbor, distance);
                        Qu.add(neighbor);
                    }
                }
                //b.append(vertex.toString() + " ");
                visited.add(node);
            }
            long totalDistance = 0L;
            for (Integer l : distanceMap.values()) {
                totalDistance += l;
                if (l > MyDirectGraphVars.diameter) {
                    MyDirectGraphVars.diameter = l;
                }
            }

            if (totalDistance == 0) {
                source.setAverageShortestOutDistance(0);
            } else {
                int unreachableNodeCount = (totalNodeCount-visited.size()) - 2;
                int unreachableNodeTotalDistance = unreachableNodeCount * (maxDistance + 1);
                source.setAverageShortestOutDistance((float) (totalDistance + unreachableNodeTotalDistance) / totalNodeCount);
            }

            source.setReachedOutNodeCount(visited.size()-1);
            source.setUnReachedOutNodeCount((totalNodeCount - visited.size()) - 2);
        }
    }

    static GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    public static int screenWidth = gd.getDisplayMode().getWidth();
    public static int screenHeight = gd.getDisplayMode().getHeight();

    public static void setAverageShortestInDistance(MyDirectGraph<MyDirectNode, MyDirectEdge> g) {
        StringBuilder b =  new StringBuilder();
        int maxDistance = 0;
        int totalNodeCount = (int) MyDirectGraphVars.directGraph.getGraphNodeCount();
        Collection<MyDirectNode> nodes = g.getVertices();
        for (MyDirectNode source : nodes) {
            if (source.getCurrentValue() <= 0) continue;
            Queue<MyDirectNode> Qu =  new LinkedList<>();
            Set<MyDirectNode> visited = new HashSet();
            Map<MyDirectNode, Integer> distanceMap =  new HashMap();

            Qu.add(source);
            distanceMap.put(source, 0);
            while (!Qu.isEmpty()) {
                MyDirectNode successor = Qu.remove();
                Collection<MyDirectNode> predecessors = g.getPredecessors(successor);
                for (MyDirectNode predecessor : predecessors) {
                    if (predecessor.getCurrentValue() <= 0) continue;
                    if (!visited.contains(predecessor)) {
                        int distance = distanceMap.get(successor) + 1;

                        if (distance > maxDistance) {
                            maxDistance = distance;
                        }

                        distanceMap.put(predecessor, distance);
                        Qu.add(predecessor);
                    }
                }
                //b.append(vertex.toString() + " ");
                visited.add(successor);
            }

            long totalDistance = 0L;
            for (Integer l : distanceMap.values()) {
                totalDistance += l;
            }

            if (totalDistance == 0) {
                source.setAverageShortestInDistance(0);
            } else {
                int unreachableNodeCount = (totalNodeCount - visited.size()) - 2;
                int unreachableNodeTotalDistance = unreachableNodeCount * (maxDistance + 1);
                source.setAverageShortestInDistance((float) (totalDistance + unreachableNodeTotalDistance) / totalNodeCount);
            }

            source.setReachedInNodeCount(visited.size()-1);
            source.setUnReachedInNodeCount((totalNodeCount - visited.size()) - 2);
        }
    }

    public static double getAverageShortestOutDistance() {
        double avgReach = 0D;
        Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
        for (MyDirectNode n : nodes) {
            avgReach += n.getAverageShortestOutDistance();
        }
        return (avgReach/nodes.size());
    }

    public static double getMultiNodeAverageShortestOutDistance() {
        double avgReach = 0D;
        for (MyDirectNode n : MyDirectGraphVars.getDirectGraphViewer().multiNodes) {
            avgReach += n.getAverageShortestOutDistance();
        }
        return (avgReach/ MyDirectGraphVars.getDirectGraphViewer().multiNodes.size());
    }

}
