package datamining.utils.system;

import datamining.feature.MyVariableMap;
import datamining.main.MyProgressBar;
import datamining.main.MyProgressBarWaitMessage;
import datamining.graph.MyDirectEdge;
import datamining.graph.MyDirectNode;
import datamining.utils.MyMathUtil;
import datamining.utils.security.MyOSMonitor;
import datamining.utils.message.MyMessageUtil;
import edu.uci.ics.jung.graph.Graph;

import java.io.*;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class MySysUtil
{

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

    public static LinkedHashMap<String, Float> sortMapByFloatValue(Map<String, Float> map) {
        List<Map.Entry<String, Float>> entries = new LinkedList<>(map.entrySet());
        Collections.sort(entries, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        LinkedHashMap<String, Float> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Float> entry : entries) {sortedMap.put(entry.getKey(), entry.getValue());}
        return sortedMap;
    }


    public static void loadVariablesAndValues() {
        try {
            String variablesAndValuesFileName = "." + MySysUtil.getDirectorySlash() + ".variablesAndValues.txt";
            File variableAndValueFile = new File(variablesAndValuesFileName);
            if (!variableAndValueFile.exists()) {
                MyMessageUtil.showErrorMsg("The application property file does not exist.");
            } else {
                BufferedReader bw = new BufferedReader(new FileReader(variableAndValueFile));
                MyVars.sequeceFeatureCount = Integer.valueOf(bw.readLine());
                MyVars.totalOutputSize = Long.valueOf(bw.readLine());
                MyVars.globalPatternCount = Long.valueOf(bw.readLine());
                MyVars.mxDepth = Integer.valueOf(bw.readLine());
                MyVars.totalRecords = Integer.valueOf(bw.readLine());
                MyVars.outputDir = bw.readLine();
                bw.readLine();
                String value = "";
                MyVars.itemToIdMap = new HashMap<>();

                while ((value = bw.readLine()).length() > 0) {
                    String[] keyValues = value.split("=");
                    MyVars.itemToIdMap.put(keyValues[0], keyValues[1]);
                }

                while ((value = bw.readLine()).length() > 0) {
                    String[] keyValues = value.split("=");
                    MyVars.variableToIdMap.put(keyValues[0], keyValues[1]);
                }

                bw.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static String getDirectorySlash() {
        return (MyOSMonitor.isWindows() ? "\\" : "//");
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
            for (String name : nodeName.split(MyVars.commaDelimeter)) {
                if (decodedVariableSet.length() == 0) {decodedVariableSet = decodeVariableSet(name);
                } else {decodedVariableSet += "," + decodeVariableSet(name);}
            }
            return decodedVariableSet;
        } else {
            String decodedItemSet = "";
            for (String name : nodeName.split(MyVars.commaDelimeter)) {
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
                    if (decodedItemSet.length() == 0) {decodedItemSet = MyVars.itemToIdMap.get(item);
                    } else {decodedItemSet += MyVars.commaDelimeter + MyVars.itemToIdMap.get(item);}
                    item = "" + itemset.charAt(i);
                } else {item += itemset.charAt(i);}
            }
            if (decodedItemSet.length() == 0) {decodedItemSet = MyVars.itemToIdMap.get(item);
            } else {decodedItemSet += MyVars.commaDelimeter + MyVars.itemToIdMap.get(item);}
            return decodedItemSet;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }

    public static String decodeVariableSet(String variableItemSet) {
        String decodedItemSet = "";
        String variableItem = "" + variableItemSet.charAt(0);
        for (int i = 1; i < variableItemSet.length(); i++) {
            if (variableItemSet.charAt(i) == 'x') {
                variableItem += variableItemSet.charAt(i);
                if (decodedItemSet.length() == 0) {decodedItemSet = MyVars.variableToIdMap.get(variableItem);
                } else {decodedItemSet += MyVars.commaDelimeter + MyVars.variableToIdMap.get(variableItem);}
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
                decryptedVariable = MyVars.variableToIdMap.get(encryptedVariable);
            } else {
                decryptedVariable += "," + MyVars.variableToIdMap.get(encryptedVariable);
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
        String[] items = itemset.split(MyVars.commaDelimeter);
        for (int i = 0; i < items.length; i++) {
            for (Map.Entry<String, String> entry : MyVars.variableToIdMap.entrySet()) {
                if (Objects.equals(entry.getValue(), items[i])) {
                    if (encodedVariableItemset.length() == 0) {encodedVariableItemset = entry.getKey();
                    } else {encodedVariableItemset += "," + entry.getKey();}
                    break;
                }
            }
        }
        return encodedVariableItemset;
    }

    /**
    public static String encodeItemSet(String itemset) {
        String encodedItemset = "";
        String[] items = itemset.split(MyVars.commaDelimeter);
        for (int i = 0; i < items.length; i++) {
            for (Map.Entry<String, String> entry : MyVars.itemToIdMap.entrySet()) {
                if (Objects.equals(entry.getValue(), items[i])) {
                    if (encodedItemset.length() == 0) {encodedItemset = entry.getKey();
                    } else {encodedItemset += "," + entry.getKey();}
                    break;
                }
            }
        }
        return encodedItemset;
    }*/

    public static String encodeItemSet(String itemset) {
        String encodedItemset = "";
        if (itemset.contains("x")) {
            encodedItemset = encodeVariableItemSet(itemset);
        } else {
            String [] items = itemset.split(MyVars.commaDelimeter);
            for (int i = 0; i < items.length; i++) {
                for (Map.Entry<String, String> entry : MyVars.itemToIdMap.entrySet()) {
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
        return MyVars.main.getWidth();
    }

    public static int getViewerHeight() {
        return MyVars.main.getHeight();
    }

    public static String getTimeDifference(String fromDate, String toDate) {
        try {
            if (fromDate.indexOf(' ') == -1) {fromDate = fromDate + " " + "00:00:00";}
            if (toDate.indexOf(' ') == -1) {toDate = toDate + " " + "00:00:00";}
            LocalDateTime d1 = LocalDateTime.parse(fromDate, DateTimeFormatter.ofPattern(MyVars.DATE_TIME_FORMAT));
            LocalDateTime d2 = LocalDateTime.parse(toDate, DateTimeFormatter.ofPattern(MyVars.DATE_TIME_FORMAT));
            Duration d = Duration.between(d1, d2);
            long diff = d.getSeconds();
            return String.valueOf(Math.abs(diff));
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public static void resetVariables() {
        MyVars.directMarkovChain = null;

        MyVars.outputDir = "";
        MyVars.isTimeOn = false;
        MyVars.isSupplementaryOn = false;
        MyVars.seqs = null;
        MyVars.mxDepth = 0;
        MyVars.totalRecords = 0;
        MyVars.totalOutputSize = 0;
        MyVars.sequeceFeatureCount = 0;
        MyVars.sequenceWithObjectIDFileName = "";
        MyVars.sequenceFileName = "";
        MyVars.isDirectMarkovRelation = false;
        MyVars.edgeOrderByComboBoxIdx = -1;
        MyVars.nodeOrderByComboBoxIdx = -1;
        MyVars.currentGraphDepth = 0;
        MyVars.itemToIdMap = null;

        MyVars.nodeLabels = new HashSet<>();
        MyVars.nodeValues = new HashMap<>();
        MyVars.edgeLabels = new HashSet<>();
        MyVars.edgeValues = new HashMap<>();
        MyVars.variableToIdMap = new MyVariableMap();
        MyVars.nodeNameMap = new HashMap<>();
        MyVars.pathLengthByDepthMap = new HashMap<>();
        MyVars.currentGraphDepth = 0;
    }

    public static String getWorkingDir() {
        return System.getProperty("user.dir");
    }

    public static void setGraphLevelPathLengthDistributions(MyProgressBar pb) {
        int nodeCount = 0;
        Collection<MyDirectNode> startNodes = MyVars.directMarkovChain.getVertices();
        Collection<MyDirectNode> endNodes = MyVars.directMarkovChain.getVertices();
        double currentTotalWork = 20;
        double workfraction = (double) 65/startNodes.size();
        for (MyDirectNode startNode : startNodes) {
            int unreachableNodeCount = 0;
            String workPercent = MyMathUtil.twoDecimalFormat(((double)(++nodeCount)/MyVars.directMarkovChain.getVertexCount())*100);
            MyProgressBarWaitMessage.waitingMsgLabel.setText("Computing path lengths: "+ nodeCount + "/" + MyMathUtil.getCommaSeperatedNumber(MyVars.directMarkovChain.getVertexCount()) + "(" + workPercent + "%)");
            for (MyDirectNode endNode : endNodes) {
                if (startNode != endNode) {
                    doGraphLevelDFSForAllPathSearch(endNode, startNode, new HashSet<MyDirectNode>(), new LinkedList<>());
                    if (unreachable) unreachableNodeCount++;
                    else unreachable = true;
                }
            }
            startNode.setUnreachableNodeCount(unreachableNodeCount);
            currentTotalWork += workfraction;
            pb.updateValue((int)currentTotalWork, 100);
        }
        MyProgressBarWaitMessage.waitingMsgLabel.setText(MyProgressBarWaitMessage.waitMsg);
    }

    private static boolean unreachable = true;

    private static void doGraphLevelDFSForAllPathSearch(MyDirectNode endNode, MyDirectNode successor, Set<MyDirectNode> visited, LinkedList<MyDirectNode> currpath) {
        try {
            if (visited.contains(successor)) {
                return;
            }
            visited.add(successor);
            currpath.addLast(successor);

            if (successor == endNode) {
                unreachable = false;
                int pathLength = currpath.size();
                if (MyVars.pathLengthByDepthMap.containsKey(pathLength)) {
                    MyVars.pathLengthByDepthMap.put(pathLength, MyVars.pathLengthByDepthMap.get(pathLength)+1);
                } else {
                    MyVars.pathLengthByDepthMap.put(pathLength, 1L);
                }
                currpath.removeLast();
                visited.remove(successor);
                return;
            }

            Collection<MyDirectEdge> outEdges = MyVars.directMarkovChain.getOutEdges(successor);
            for (MyDirectEdge edge : outEdges) {;
                doGraphLevelDFSForAllPathSearch(endNode, edge.getDest(), visited, currpath);
            }
            currpath.removeLast();
            visited.remove(successor);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static int getUnWeightedBetweenNodeShortestPathLength(MyDirectNode source, MyDirectNode dest){
        MyProgressBar pb = new MyProgressBar(false);
        Queue<MyDirectNode> Qu =  new LinkedList<>();
        Set<MyDirectNode> visited = new HashSet();
        Map<MyDirectNode, Integer> found =  new HashMap();

        Qu.add(source);
        found.put(source, 0);
        while (!Qu.isEmpty()) {
            MyDirectNode vertex = Qu.remove();
            Collection<MyDirectNode> successors = MyVars.directMarkovChain.getSuccessors(vertex);
            if (successors == null) {
                pb.updateValue(100, 100);
                pb.dispose();
                return 0;
            }
            for (MyDirectNode neighbor : successors) {
                if (!found.keySet().contains(neighbor) && !visited.contains(neighbor)) {
                    found.put(neighbor, found.get(vertex) + 1);
                    Qu.add(neighbor);
                }
            }
            visited.add(vertex);
        }
        pb.updateValue(100, 100);
        pb.dispose();
        return (found.containsKey(dest) ? found.get(dest) : 0);
    }

    public static void setAverageUnWeightedDirectGraphShortestPathLength(Graph g) {
        StringBuilder b =  new StringBuilder();
        long gTotalL = 0L;
        long gCount = 0L;
        Collection<MyDirectNode> nodes = g.getVertices();
        for (MyDirectNode start : nodes) {
            Queue<MyDirectNode> Qu =  new LinkedList<>();
            Set<MyDirectNode> visited = new HashSet();
            Map<MyDirectNode, Integer> found =  new HashMap();

            Qu.add(start);
            found.put(start, 0);
            while (!Qu.isEmpty()) {
                MyDirectNode vertex = Qu.remove();
                Collection<MyDirectNode> successors = g.getSuccessors(vertex);
                for (MyDirectNode neighbor : successors) {
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
                    if (l > MyVars.diameter) {
                        MyVars.diameter = l;
                    }
                }
            }

            if (count == 0) {
                start.setAverageShortestPathLength(0);
            } else {
                start.setAverageShortestPathLength((double) totalL / count);
            }

            gTotalL += totalL;
            gCount += count;
            start.setUnreachableNodeCount((MyVars.directMarkovChain.getVertexCount()-1)-count);
        }

        if (gTotalL > 0) {
            double avg = (double) gTotalL / gCount;
            MyVars.avgShortestPathLen = avg;
        } else {
            MyVars.avgShortestPathLen = 0.0D;
        }
    }

}
