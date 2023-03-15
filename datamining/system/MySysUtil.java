package datamining.system;

import java.io.File;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class MySysUtil {

    /**
     * returns a human readable node name from encoded item ids.
     * @param nName
     * @return
     */


    /**
     * returns a boolean if a node is a list items of variables or items.
     * @param nName
     * @return
     */
    public static boolean isVariableFeatureExists(String nName) {
        if (!Character.isDigit(nName.charAt(0))) {
            return true;
        }
        // just return false if the node name is not from a variable node.
        return false;
    }

    /**
     * return a comma separated string from a long number.
     * @param n
     * @return
     */
    public static String getCommaSeperateString(long n) {
        return NumberFormat.getNumberInstance(Locale.US).format(n);
    }

    /**
     * return splited node name
     */
    public static String splitNodeName(String nName) {
        return (nName.indexOf('*') > -1) ?
                nName.split("\\*")[0] : nName;
    }

    /**
     * return the support count. Engine gets the value.
     * @return
     */
    public static int getMinimumContribution(double userSupR, int records) {
        // set the default record count to 1.
        int minCont = 1;
        // compute minimum support count from user provided support ratio.
        int calcedRecCnt = (int)(records*userSupR);
        // see if computed minimum support count is less than defalut record count.
        return (minCont > calcedRecCnt) ? minCont : calcedRecCnt;
    }

    /**
     * Calculation for Day difference
     */
    public static long calculateDayDifference(final String prevDate, final String curDate) throws Exception {
        DateFormat format = new SimpleDateFormat("yyyy-mm-dd", Locale.KOREAN);
        Date date1 = format.parse(prevDate);
        Date date2 = format.parse(curDate);
        long diff = date2.getTime() - date1.getTime();
        return Math.abs(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
    }


    public static int utf8Length(CharSequence cs) {
        return cs.codePoints()
                .map(cp -> cp<=0x7ff? cp<=0x7f? 1: 2: cp<=0xffff? 3: 4)
                .sum();
    }

    public static void deleteFiles(File [] files) {
        for (File f : files) {
            f.delete();
        }
    }

}
