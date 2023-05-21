package medousa.sequential.broker;

import medousa.sequential.category.MySequentialGraphCategorizer;
import medousa.sequential.category.MySequentialGraphCategoryList;

import java.util.ArrayList;

public class MySequentialGraphCategoryBroker
extends MySequentialGraphHeaderBroker {

    private MySequentialGraphCategorizer categorizer;

    public MySequentialGraphCategoryBroker() { super(); }

    public boolean categorize() {
        categorizer = new MySequentialGraphCategorizer(getInputFiles());
        return categorizer.categorize();

    }

    public MySequentialGraphCategoryList getCategoryList() {
        return categorizer.getCategoryList();
    }
    public ArrayList<ArrayList<String>> getRawData() {
        return categorizer.getRawData();
    }
}
