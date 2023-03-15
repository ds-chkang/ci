package datamining.system;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by changhee Kang on 17. 7. 17.
 */
public class MyMathUtil {

    /**
     * returns a comma separated number in string format.
     * @param number
     * @return
     */
    public static String getCommaSeperatedNumberString(long number) {
        return NumberFormat.
                getNumberInstance(Locale.US).
                format(number);
    }

    /**
     * returns a comma separated number in string format.
     * @param number
     * @return
     */
    public static String getCommaSeperatedNumberString(int number) {
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
                new DecimalFormat("##0.####");

        return formatter.
                format(targetNumber);
    }

    /**
     * return a number with six decimal points.
     * @param targetNumber
     * @return
     */
    public static String sixDecimalFormat(double targetNumber) {
        DecimalFormat formatter =
                new DecimalFormat("##0.######");

        return formatter.
                format(targetNumber);
    }

    /**
     * return a number with four decimal points.
     * @param targetNumber
     * @return
     */
    public static String threeDecimalFormat(double targetNumber) {
        DecimalFormat formatter =
                new DecimalFormat("##0.###");

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
}
