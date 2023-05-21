package medousa.sequential.category;

import java.util.ArrayList;
import java.util.HashMap;

public class MySequentialGraphCategoryList
extends ArrayList<MySequentialGraphCategory> {

    // Utility variable for searching category index in a large nodeTableData set.
    private static HashMap<String, Integer> categoryMap = new HashMap<String, Integer>();

    public MySequentialGraphCategoryList() {}

    public void createCategoriesForRealVariable(MySequentialGraphCategory category) {
        if (this.size() > 0) this.categoryMap.put(category.getName().trim().toUpperCase(), (this.size()-1));
        else this.categoryMap.put(category.getName().trim().toUpperCase(), this.size());
        this.add(category);
    }

    public void display() {
        for (MySequentialGraphCategory category : this) {
            category.display();
        }
    }
    public MySequentialGraphCategory getCategory(int categoryIndex) { return this.get(categoryIndex); }
    public MySequentialGraphCategory getCategory(String categoryName) {
        return this.get(this.categoryMap.get(categoryName.trim().toUpperCase()));
    }

}
