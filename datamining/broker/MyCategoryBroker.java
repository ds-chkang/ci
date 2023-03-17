package datamining.broker;

import datamining.category.MyCategorizer;
import datamining.category.MyCategoryList;

import java.util.ArrayList;

public class MyCategoryBroker
extends MyHeaderBroker {

    private MyCategorizer categorizer;

    public MyCategoryBroker() { super(); }

    public boolean categorize() {
        categorizer = new MyCategorizer(getInputFiles());
        return categorizer.categorize();

    }

    public long getRecordCount() {
        return categorizer.getRecordCount();
    }

    public MyCategoryList getCategoryList() {
        return categorizer.getCategoryList();
    }
    public ArrayList<ArrayList<String>> getRawData() {
        return categorizer.getRawData();
    }
}
