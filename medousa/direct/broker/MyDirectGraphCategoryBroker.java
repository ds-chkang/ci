package medousa.direct.broker;

import medousa.direct.category.MyDirectGraphCategorizer;
import medousa.direct.category.MyDirectGraphCategoryList;

import java.util.ArrayList;

public class MyDirectGraphCategoryBroker
extends MyDirectGraphHeaderBroker {

    private MyDirectGraphCategorizer categorizer;

    public MyDirectGraphCategoryBroker() { super(); }

    public boolean categorize() {
        categorizer = new MyDirectGraphCategorizer(getInputFiles());
        return categorizer.categorize();

    }

    public long getRecordCount() {
        return categorizer.getRecordCount();
    }

    public MyDirectGraphCategoryList getCategoryList() {
        return categorizer.getCategoryList();
    }
    public ArrayList<ArrayList<String>> getRawData() {
        return categorizer.getRawData();
    }
}
