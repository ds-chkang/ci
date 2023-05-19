package medousa.sequential.utils;

import medousa.MyMedousa;
import medousa.sequential.feature.MyVariableMap;
import medousa.sequential.graph.MyGraph;
import medousa.sequential.graph.MySequentialGraphDashBoard;
import medousa.sequential.graph.MyNode;
import medousa.sequential.graph.MySequentialGraphViewer;

import java.util.*;
import java.awt.*;

public class MySequentialGraphVars {
    public static Font f_pln_9 = new Font( "Noto Sans", Font.PLAIN, 9);
    public static Font f_pln_10 = new Font( "Noto Sans", Font.PLAIN, 10);
    public static Font f_pln_11 = new Font( "Noto Sans", Font.PLAIN, 11);
    public static Font f_pln_12 = new Font( "Noto Sans", Font.PLAIN, 12);
    public static Font f_bold_16 = new Font("Noto Sans", Font.BOLD, 16);
    public static Font f_bold_14 = new Font("Noto Sans", Font.BOLD, 14);
    public static Font f_bold_12 = new Font("Noto Sans", Font.BOLD, 12);
    public static Font f_bold_11 = new Font("Noto Sans", Font.BOLD, 11);
    public static Font f_bold_10 = new Font("Noto Sans", Font.BOLD, 10);
    public static Font f_pln_6 = new Font("Noto Sans", Font.PLAIN, 6);
    public static Font f_pln_13 = new Font("Noto Sans", Font.PLAIN, 13);
    public final static Font tahomaPlainFont12 = new Font("Tahoma", Font.PLAIN, 12);
    public final static Font tahomaPlainFont13 = new Font("Tahoma", Font.PLAIN, 13);
    public final static Font tahomaPlainFont11 = new Font("Tahoma", Font.PLAIN, 11);
    public final static Font tahomaPlainFont10 = new Font("Tahoma", Font.PLAIN, 10);
    public final static Font tahomaPlainFont7 = new Font("Tahoma", Font.PLAIN, 7);
    public final static Font tahomaBoldFont13 = new Font("Tahoma", Font.BOLD, 13);
    public final static Font tahomaBoldFont14 = new Font("Tahoma", Font.BOLD, 14);
    public final static Font tahomaBoldFont12 = new Font("Tahoma", Font.BOLD, 12);
    public final static Font tahomaBoldFont10 = new Font("Tahoma", Font.BOLD, 10);
    public final static Font tahomaBoldFont11 = new Font("Tahoma", Font.BOLD, 11);
    public final static Font tahomaBoldFont16 = new Font("Tahoma", Font.BOLD, 16);
    public final static Font tahomaBoldFont28 = new Font("Tahoma", Font.BOLD, 28);
    public final static Font tahomaPlainFont18 = new Font("Tahoma", Font.PLAIN, 18);
    public final static Font tahomaPlainFont16 = new Font("Tahoma", Font.PLAIN, 16);

    public static int numberOfGraphs;
    public static String outputDir = "";
    public static MyGraph g;
    public static String [][] seqs;
    public static MyMedousa app = null;
    public static HashMap<String, String> itemToIdMap = null;
    public static int mxDepth = 0;
    public static int diameter = 0;
    public static double avgShortestPathLen = 0.0D;
    public static int currentGraphDepth = -1;
    public static String mergedFileLocation;
    public static int accesssTimeColumnIdx;

    public static long totalOutputSize = 0L;
    public static long globalPatternCount = 0L;
    public static int sequeceFeatureCount = 0;
    public static boolean isTimeOn = false;
    public static boolean isSupplementaryOn = false;
    public static int totalRecords;
    public static int minSub;
    public static String patternCountSymbol = "#";
    public static boolean isDirectMarkovRelation = false;
    public static int edgeOrderByComboBoxIdx = -1;
    public static int nodeOrderByComboBoxIdx = -1;
    public static int BAR_CHART_RECORD_LIMIT = 40;
    public static boolean isAppStarted = false;
    public static Set<String> userDefinedEdgeLabelSet = new HashSet<>();
    public static Set<String> userDefinedNodeLabelSet = new HashSet<>();
    public static Map<Integer, Long> pathLengthByDepthMap = new HashMap<>();
    public static Map<String, String> nodeNameMap = new HashMap<>();
    public static Map<String, String> userDefinedEdgeValueMap = new HashMap<>();
    public static Map<String, String> userDefinedNodeValueMap = new HashMap<>();
    public static MyVariableMap variableToIdMap = new MyVariableMap();
    public static java.util.List<Set<MyNode>> connectedComponentCountsByGraph = new ArrayList<>();
    public static MySequentialGraphDashBoard sequentialGraphDashBoard;
    public static MySequentialGraphViewer getSequentialGraphViewer() {
        return MySequentialGraphVars.app.getSequentialGraphMsgBroker().getSequentialGraphContainer();
    }
    public static String nodeLabelFileExt = ".nodelbl";
    public static String nodeValueFileExt = ".nodeVal";
    public static String edgeLabelFileExt = ".edgelbl";
    public static String edgeValueFileExt = ".edgeVal";
    public static String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String stopAppMsg = "Would you like to close medousa?";
    public static final String statusAppVerMsg = " MEDOUSA CONNECTED INTELLIGENCE VERSION 1.0 IS RUNNING.";
    public static String appFrameTitleMsg = "medousa 1.0";
    public static String appTitleMsg = "medousa - Connected Intelligence";
    public static String dateSecurityFileName = "thomas.sec";
    public static String dateSecurityFormat = "MM/dd/yyyy";
    public static final String commaDelimeter = ",";
    public static final String contributionSymbol = "@";
    public static final String uniqueContributionSeparator = "!";
    public static String sequenceWithObjectIDFileName = "";
    public static final String hyphenDelimeter = "-";
    public static final String timeSeparator = "a";
    public static final String imgDir = "/images/";
    public static String durationSeparator = "#";
    public static String inputSequenceFile = "";
    public static String sequenceFileName = "";
}