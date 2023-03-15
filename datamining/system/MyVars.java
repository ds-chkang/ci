package datamining.system;
import javax.swing.*;
import java.util.HashMap;
import java.awt.*;
import java.util.Map;

/**
 * Created by changhee on 17. 7. 2013.
 */
public class MyVars {

    public static Font f_pln_11 = new Font( "Sans Serif", Font.PLAIN, 11);
    public static Font f_pln_9 = new Font( "Sans Serif", Font.PLAIN, 9);
    public static Font f_bold_9 = new Font( "Sans Serif", Font.BOLD, 9);
    public static Font f_bold_8 = new Font( "Sans Serif", Font.BOLD, 8);
    public static Font f_pln_8 = new Font( "Sans Serif", Font.PLAIN, 8);
    public static Font f_pln_10 = new Font( "Sans Serif", Font.PLAIN, 10);
    public static Font f_bold_10 = new Font( "Sans Serif", Font.BOLD, 10);
    public static Font f_pln_12 = new Font( "Sans Serif", Font.PLAIN, 12);
    public static Font f_pln_15 = new Font("Sans Serif", Font.PLAIN, 15);
    public static Font f_pln_13 = new Font("Sans Serif", Font.PLAIN, 13);
    public static Font f_pln_14 = new Font("Sans Serif", Font.PLAIN, 14);
    public static Font f_bold_14 = new Font("Sans Serif", Font.BOLD, 14);
    public static Font f_pln_16 = new Font("Sans Serif", Font.PLAIN, 16);
    public static Font f_bold_13 = new Font("Sans Serif", Font.BOLD, 13);
    public static Font f_bold_15 = new Font("Sans Serif", Font.BOLD, 15);
    public static Font f_bold_12 = new Font("Sans Serif", Font.BOLD, 12);
    public static Font f_bold_11 = new Font("Sans Serif", Font.BOLD, 11);
    public static Font f_bold_21 = new Font("Sans Serif", Font.BOLD, 21);
    public static Font f_bold_25 = new Font("Sans Serif", Font.BOLD, 25);

    public static JDialog rootDialog;

    public static Thread currentJob;
    public static short freeTrialDays = 15;
    public static String dateSecurityFileName = "thomas.sec";
    public static String dateSecurityFormat = "MM/dd/yyyy";
    public static boolean isAveragePathByLengthStatisticsDone = false;
    public static String headerColumnDelimiter = ",";
    public static String dataColumnDelimiter = ",";
    public static String dirSym = "\\"; // directory symbol.
    public static final String itemSeparator = ","; // item separator.
    public static final String contributionSymbol = "@"; // contribution separator.
    public static final String itemSetSeparator = "-"; // itemset separator.
    public static final String imgDir = "/images/";
    public static String inputFeatureFile = "C:\\Users\\dschk\\IdeaProjects\\features.txt";
    public static String inputFeatureWithTimeFile = "C:\\Users\\dschk\\IdeaProjects\\featuresWithTime.txt";
    public static String [][] seqs;
    public static long [] pathContributionListByLength;
    public static long [] pathCountListByLength;
    public static long [] maxPathContributionListByLength;
    public static String dataMiningTime = "";
    public static String graphBuildingTime = "";

    /** Variables that are needed to be ready before the graph is loaded. */
    public static Map<Character, Short> pathLengthEncryptionMap;
    public static HashMap<String, String> itemToIdMap = null;
    public static int gap = 0;
    public static boolean isGraphLoaded = false;
    public static int minSup = 0;
    public static int maxPathDepth = 0;
    public static long totalOutputSize = 0L;
    public static long globalPatternCount = 0L;
    public static int sequeceFeatureCount = 0;
    public static String patternOutputDir = System.getProperty("user.dir") + "\\output\\";
    public static String patternCountSymbol = "#";
    public static String timeSym = ":";
    public static int totalRecords;
    public static Map<Integer, Integer> uniqueNodesByDepth = new HashMap<>();
    public static Map<Integer, Long> timeDurationByDepth = new HashMap<>();
    public static Map<Integer, Long> nodesWithTimeDurationByDepth = new HashMap<>();
    public static Map<Integer, Long> totalNodesByDepth = new HashMap<>();
    public static String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

}