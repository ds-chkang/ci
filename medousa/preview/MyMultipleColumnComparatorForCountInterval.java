package medousa.preview;

import java.util.Comparator;

public class MyMultipleColumnComparatorForCountInterval
implements Comparator<Object[]> {

    public MyMultipleColumnComparatorForCountInterval() {}

    @Override
    public int compare(Object[] row1, Object[] row2) {
        int result = ((Comparable) row1[3]).compareTo(row2[3]);
        //if (result == 0) {
        //    result = ((Comparable) row1[3]).compareTo(row2[3]);
        //}
        if (result == 0) {
            result = ((Comparable) row1[2]).compareTo(row2[2]);
        }
        if (result == 0) {
            result = ((Comparable) row1[1]).compareTo(row2[1]);
        }
        if (result == 0) {
            result = ((Comparable) row1[0]).compareTo(row2[0]);
        }
        return result;
    }
}
