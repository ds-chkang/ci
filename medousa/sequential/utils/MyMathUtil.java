package medousa.sequential.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by changhee Kang on 17. 7. 17.
 */
public class MyMathUtil {

    public static String downArrayUnicode = "\u2193";
    public static String upArrayUnicode = "\u2191";

    public static String fourDecimalPercent(double nominator, double denominator) {return MyMathUtil.fourDecimalFormat((nominator/denominator)*100)+"%";}

    public static String twoDecimalPercent(double nominator, double denominator) {return MyMathUtil.twoDecimalFormat((nominator/denominator)*100)+"%";}

    public static String twoDecimalPercent(double value) {
        return MyMathUtil.twoDecimalFormat(value*100)+"%";
    }

    public static String twoDecimal(double value) {
        return MyMathUtil.twoDecimalFormat(value*100);
    }

    public static String threeDecimalPercent(double value) {
        return MyMathUtil.threeDecimalFormat(value*100)+"%";
    }

    public static String fourDecimalPercent(double value) {
        return MyMathUtil.fourDecimalFormat(value*100)+"%";
    }

    public static String fiveDecimalPercent(double value) {
        return MyMathUtil.FiveDecimalFormat(value*100)+"%";
    }

    public static String sixDecimalPercent(double value) {
        return MyMathUtil.sixDecimalFormat(value*100)+"%";
    }

    /**
     * returns a comma separated number in string format.
     * @param number
     * @return
     */
    public static String getCommaSeperatedNumber(long number) {
        return NumberFormat.
                getNumberInstance(Locale.US).
                format(number);
    }

    /**
     * returns a comma separated number in string format.
     * @param number
     * @return
     */
    public static String getCommaSeperatedNumber(int number) {
        return NumberFormat.
                getNumberInstance(Locale.US).
                format(number);
    }

    /**
     * return a number with four decimal points.
     * @param targetNumber
     * @return
     */
    public static String fourDecimalFormat(double targetNumber) {
        DecimalFormat formatter =
                new DecimalFormat("##0.0000");

        return formatter.
                format(targetNumber);
    }

    public static String FiveDecimalFormat(double targetNumber) {
        DecimalFormat formatter =
                new DecimalFormat("##0.00000");

        return formatter.format(targetNumber);
    }

    public static String sixDecimalFormat(double targetNumber) {
        DecimalFormat formatter =
                new DecimalFormat("##0.000000");

        return formatter.format(targetNumber);
    }

    /**
     * return a number with four decimal points.
     * @param targetNumber
     * @return
     */
    public static String threeDecimalFormat(double targetNumber) {
        DecimalFormat formatter =
                new DecimalFormat("##0.000");

        return formatter.
                format(targetNumber);
    }

    /**
     * returns a two decial format number.
     * @param targetNumber
     * @return
     */
    public static String twoDecimalFormat(double targetNumber) {
        DecimalFormat formatter =
                new DecimalFormat("##0.00");

        return formatter.
                format(targetNumber);
    }

    public static String oneDecimalFormat(double targetNumber) {
        DecimalFormat formatter =
                new DecimalFormat("##0.0");

        return formatter.
                format(targetNumber);
    }
}
